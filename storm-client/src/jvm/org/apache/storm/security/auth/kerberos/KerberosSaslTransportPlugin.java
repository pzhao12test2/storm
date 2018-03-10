/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.storm.security.auth.kerberos;

import java.io.IOException;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;
import javax.security.sasl.Sasl;

import org.apache.storm.messaging.netty.Login;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.transport.TSaslClientTransport;
import org.apache.thrift.transport.TSaslServerTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;
import org.apache.zookeeper.server.auth.KerberosName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.storm.security.auth.AuthUtils;
import org.apache.storm.security.auth.SaslTransportPlugin;

public class KerberosSaslTransportPlugin extends SaslTransportPlugin {
    public static final String KERBEROS = "GSSAPI"; 
    private static final Logger LOG = LoggerFactory.getLogger(KerberosSaslTransportPlugin.class);
    private static Map <LoginCacheKey, Login> loginCache = new ConcurrentHashMap<>();
    private static final String DISABLE_LOGIN_CACHE = "disableLoginCache";

    private class LoginCacheKey {
        private String keyString = null;

        public LoginCacheKey(SortedMap<String, ?> authConf) throws IOException {
            if (authConf != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String configKey: authConf.keySet()) {
                    //DISABLE_LOGIN_CACHE indicates whether or not to use the LoginCache.
                    //So we exclude it from the keyString
                    if (configKey.equals(DISABLE_LOGIN_CACHE)) {
                        continue;
                    }
                    String configValue = (String) authConf.get(configKey);
                    stringBuilder.append(configKey);
                    stringBuilder.append(configValue);
                }
                keyString = stringBuilder.toString();
            } else {
                throw new IllegalArgumentException("Configuration should not be null");
            }
        }

        @Override
        public int hashCode() {
            return keyString.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof LoginCacheKey) && keyString.equals(((LoginCacheKey)obj).keyString);
        }

        @Override
        public String toString() {
            return (keyString);
        }
    }

    public TTransportFactory getServerTransportFactory() throws IOException {
        //create an authentication callback handler
        CallbackHandler server_callback_handler = new ServerCallbackHandler(login_conf, topoConf);
        
        //login our principal
        Subject subject = null;
        try {
            //specify a configuration object to be used
            Configuration.setConfiguration(login_conf);
            //now login
            Login login = new Login(AuthUtils.LOGIN_CONTEXT_SERVER, server_callback_handler);
            subject = login.getSubject();
            login.startThreadIfNeeded();
        } catch (LoginException ex) {
            LOG.error("Server failed to login in principal:" + ex, ex);
            throw new RuntimeException(ex);
        }

        //check the credential of our principal
        if (subject.getPrivateCredentials(KerberosTicket.class).isEmpty()) { 
            throw new RuntimeException("Fail to verify user principal with section \""
                    +AuthUtils.LOGIN_CONTEXT_SERVER+"\" in login configuration file "+ login_conf);
        }

        String principal = AuthUtils.get(login_conf, AuthUtils.LOGIN_CONTEXT_SERVER, "principal"); 
        LOG.debug("principal:"+principal);  
        KerberosName serviceKerberosName = new KerberosName(principal);
        String serviceName = serviceKerberosName.getServiceName();
        String hostName = serviceKerberosName.getHostName();
        Map<String, String> props = new TreeMap<String,String>();
        props.put(Sasl.QOP, "auth");
        props.put(Sasl.SERVER_AUTH, "false");

        //create a transport factory that will invoke our auth callback for digest
        TSaslServerTransport.Factory factory = new TSaslServerTransport.Factory();
        factory.addServerDefinition(KERBEROS, serviceName, hostName, props, server_callback_handler);

        //create a wrap transport factory so that we could apply user credential during connections
        TUGIAssumingTransportFactory wrapFactory = new TUGIAssumingTransportFactory(factory, subject); 

        LOG.info("SASL GSSAPI transport factory will be used");
        return wrapFactory;
    }

    private Login mkLogin() throws IOException {
        try {
            //create an authentication callback handler
            ClientCallbackHandler client_callback_handler = new ClientCallbackHandler(login_conf);
            //specify a configuration object to be used
            Configuration.setConfiguration(login_conf);
            //now login
            Login login = new Login(AuthUtils.LOGIN_CONTEXT_CLIENT, client_callback_handler);
            login.startThreadIfNeeded();
            return login;
        } catch (LoginException ex) {
            LOG.error("Server failed to login in principal:" + ex, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public TTransport connect(TTransport transport, String serverHost, String asUser) throws TTransportException, IOException {
        //login our user
        SortedMap<String, ?> authConf = AuthUtils.pullConfig(login_conf, AuthUtils.LOGIN_CONTEXT_CLIENT);
        if (authConf == null) {
            throw new RuntimeException("Error in parsing the kerberos login Configuration, returned null");
        }

        boolean disableLoginCache = false;
        if (authConf.containsKey(DISABLE_LOGIN_CACHE)) {
            disableLoginCache = Boolean.valueOf((String) authConf.get(DISABLE_LOGIN_CACHE));
        }

        Login login;
        LoginCacheKey key = new LoginCacheKey(authConf);
        if (disableLoginCache) {
            LOG.debug("Kerberos Login Cache is disabled, attempting to contact the Kerberos Server");
            login = mkLogin();
            //this is to prevent the potential bug that
            //if the Login Cache is (1) enabled, and then (2) disabled and then (3) enabled again,
            //and if the LoginCacheKey remains unchanged, (3) will use the Login cache from (1), which could be wrong,
            //because the TGT cache (as well as the principle) could have been changed during (2)
            loginCache.remove(key);
        } else {
            LOG.debug("Trying to get the Kerberos Login from the Login Cache");
            login = loginCache.get(key);
            if (login == null) {
                synchronized (loginCache) {
                    login = loginCache.get(key);
                    if (login == null) {
                        LOG.debug("Kerberos Login was not found in the Login Cache, attempting to contact the Kerberos Server");
                        login = mkLogin();
                        loginCache.put(key, login);
                    }
                }
            }
        }

        final Subject subject = login.getSubject();
        if (subject.getPrivateCredentials(KerberosTicket.class).isEmpty()) { //error
            throw new RuntimeException("Fail to verify user principal with section \""
                        +AuthUtils.LOGIN_CONTEXT_CLIENT+"\" in login configuration file "+ login_conf);
        }

        final String principal = StringUtils.isBlank(asUser) ? getPrincipal(subject) : asUser;
        String serviceName = AuthUtils.get(login_conf, AuthUtils.LOGIN_CONTEXT_CLIENT, "serviceName");
        if (serviceName == null) {
            serviceName = AuthUtils.SERVICE; 
        }
        Map<String, String> props = new TreeMap<String,String>();
        props.put(Sasl.QOP, "auth");
        props.put(Sasl.SERVER_AUTH, "false");

        LOG.debug("SASL GSSAPI client transport is being established");
        final TTransport sasalTransport = new TSaslClientTransport(KERBEROS, 
                principal, 
                serviceName, 
                serverHost,
                props,
                null, 
                transport);

        //open Sasl transport with the login credential
        try {
            Subject.doAs(subject,
                    new PrivilegedExceptionAction<Void>() {
                public Void run() {
                    try {
                        LOG.debug("do as:"+ principal);
                        sasalTransport.open();
                    }
                    catch (Exception e) {
                        LOG.error("Client failed to open SaslClientTransport to interact with a server during session initiation: " + e, e);
                    }
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw new RuntimeException(e);
        }

        return sasalTransport;
    }

    private String getPrincipal(Subject subject) {
        Set<Principal> principals = (Set<Principal>)subject.getPrincipals();
        if (principals==null || principals.size()<1) {
            LOG.info("No principal found in login subject");
            return null;
        }
        return ((Principal)(principals.toArray()[0])).getName();
    }

    /** A TransportFactory that wraps another one, but assumes a specified UGI
     * before calling through.                                                                                                                                                      
     *                                                                                                                                                                              
     * This is used on the server side to assume the server's Principal when accepting                                                                                              
     * clients.                                                                                                                                                                     
     */
    static class TUGIAssumingTransportFactory extends TTransportFactory {
        private final Subject subject;
        private final TTransportFactory wrapped;

        public TUGIAssumingTransportFactory(TTransportFactory wrapped, Subject subject) {
            this.wrapped = wrapped;
            this.subject = subject;

            Set<Principal> principals = (Set<Principal>)subject.getPrincipals();
            if (principals.size()>0) 
                LOG.info("Service principal:"+ ((Principal)(principals.toArray()[0])).getName());
        }

        @Override
        public TTransport getTransport(final TTransport trans) {
            try {
                return Subject.doAs(subject,
                        new PrivilegedExceptionAction<TTransport>() {
                    public TTransport run() {
                        try {
                            return wrapped.getTransport(trans);
                        }
                        catch (Exception e) {
                            LOG.debug("Storm server failed to open transport " +
                                    "to interact with a client during session initiation: " + e, e);
                            return new NoOpTTrasport(null);
                        }
                    }
                });
            } catch (PrivilegedActionException e) {
                LOG.error("Storm server experienced a PrivilegedActionException exception while creating a transport using a JAAS principal context:" + e, e);
                return null;
            }
        }
    }
}
