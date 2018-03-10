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
/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.storm.generated;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)")
public class TopologyHistoryInfo implements org.apache.thrift.TBase<TopologyHistoryInfo, TopologyHistoryInfo._Fields>, java.io.Serializable, Cloneable, Comparable<TopologyHistoryInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TopologyHistoryInfo");

  private static final org.apache.thrift.protocol.TField TOPO_IDS_FIELD_DESC = new org.apache.thrift.protocol.TField("topo_ids", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TopologyHistoryInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TopologyHistoryInfoTupleSchemeFactory());
  }

  private List<String> topo_ids; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TOPO_IDS((short)1, "topo_ids");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // TOPO_IDS
          return TOPO_IDS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TOPO_IDS, new org.apache.thrift.meta_data.FieldMetaData("topo_ids", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TopologyHistoryInfo.class, metaDataMap);
  }

  public TopologyHistoryInfo() {
  }

  public TopologyHistoryInfo(
    List<String> topo_ids)
  {
    this();
    this.topo_ids = topo_ids;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TopologyHistoryInfo(TopologyHistoryInfo other) {
    if (other.is_set_topo_ids()) {
      List<String> __this__topo_ids = new ArrayList<String>(other.topo_ids);
      this.topo_ids = __this__topo_ids;
    }
  }

  public TopologyHistoryInfo deepCopy() {
    return new TopologyHistoryInfo(this);
  }

  @Override
  public void clear() {
    this.topo_ids = null;
  }

  public int get_topo_ids_size() {
    return (this.topo_ids == null) ? 0 : this.topo_ids.size();
  }

  public java.util.Iterator<String> get_topo_ids_iterator() {
    return (this.topo_ids == null) ? null : this.topo_ids.iterator();
  }

  public void add_to_topo_ids(String elem) {
    if (this.topo_ids == null) {
      this.topo_ids = new ArrayList<String>();
    }
    this.topo_ids.add(elem);
  }

  public List<String> get_topo_ids() {
    return this.topo_ids;
  }

  public void set_topo_ids(List<String> topo_ids) {
    this.topo_ids = topo_ids;
  }

  public void unset_topo_ids() {
    this.topo_ids = null;
  }

  /** Returns true if field topo_ids is set (has been assigned a value) and false otherwise */
  public boolean is_set_topo_ids() {
    return this.topo_ids != null;
  }

  public void set_topo_ids_isSet(boolean value) {
    if (!value) {
      this.topo_ids = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TOPO_IDS:
      if (value == null) {
        unset_topo_ids();
      } else {
        set_topo_ids((List<String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TOPO_IDS:
      return get_topo_ids();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TOPO_IDS:
      return is_set_topo_ids();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TopologyHistoryInfo)
      return this.equals((TopologyHistoryInfo)that);
    return false;
  }

  public boolean equals(TopologyHistoryInfo that) {
    if (that == null)
      return false;

    boolean this_present_topo_ids = true && this.is_set_topo_ids();
    boolean that_present_topo_ids = true && that.is_set_topo_ids();
    if (this_present_topo_ids || that_present_topo_ids) {
      if (!(this_present_topo_ids && that_present_topo_ids))
        return false;
      if (!this.topo_ids.equals(that.topo_ids))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_topo_ids = true && (is_set_topo_ids());
    list.add(present_topo_ids);
    if (present_topo_ids)
      list.add(topo_ids);

    return list.hashCode();
  }

  @Override
  public int compareTo(TopologyHistoryInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(is_set_topo_ids()).compareTo(other.is_set_topo_ids());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (is_set_topo_ids()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.topo_ids, other.topo_ids);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TopologyHistoryInfo(");
    boolean first = true;

    sb.append("topo_ids:");
    if (this.topo_ids == null) {
      sb.append("null");
    } else {
      sb.append(this.topo_ids);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TopologyHistoryInfoStandardSchemeFactory implements SchemeFactory {
    public TopologyHistoryInfoStandardScheme getScheme() {
      return new TopologyHistoryInfoStandardScheme();
    }
  }

  private static class TopologyHistoryInfoStandardScheme extends StandardScheme<TopologyHistoryInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TopologyHistoryInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TOPO_IDS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list842 = iprot.readListBegin();
                struct.topo_ids = new ArrayList<String>(_list842.size);
                String _elem843;
                for (int _i844 = 0; _i844 < _list842.size; ++_i844)
                {
                  _elem843 = iprot.readString();
                  struct.topo_ids.add(_elem843);
                }
                iprot.readListEnd();
              }
              struct.set_topo_ids_isSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TopologyHistoryInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.topo_ids != null) {
        oprot.writeFieldBegin(TOPO_IDS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.topo_ids.size()));
          for (String _iter845 : struct.topo_ids)
          {
            oprot.writeString(_iter845);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TopologyHistoryInfoTupleSchemeFactory implements SchemeFactory {
    public TopologyHistoryInfoTupleScheme getScheme() {
      return new TopologyHistoryInfoTupleScheme();
    }
  }

  private static class TopologyHistoryInfoTupleScheme extends TupleScheme<TopologyHistoryInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TopologyHistoryInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.is_set_topo_ids()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.is_set_topo_ids()) {
        {
          oprot.writeI32(struct.topo_ids.size());
          for (String _iter846 : struct.topo_ids)
          {
            oprot.writeString(_iter846);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TopologyHistoryInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list847 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.topo_ids = new ArrayList<String>(_list847.size);
          String _elem848;
          for (int _i849 = 0; _i849 < _list847.size; ++_i849)
          {
            _elem848 = iprot.readString();
            struct.topo_ids.add(_elem848);
          }
        }
        struct.set_topo_ids_isSet(true);
      }
    }
  }

}

