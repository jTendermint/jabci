// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: types.proto

package com.github.jtendermint.jabci.proto.types;

public interface EventOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.github.jtendermint.jabci.proto.types.Event)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string type = 1;</code>
   */
  java.lang.String getType();
  /**
   * <code>string type = 1;</code>
   */
  com.google.protobuf.ByteString
      getTypeBytes();

  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.common.KVPair attributes = 2;</code>
   */
  java.util.List<com.github.jtendermint.jabci.proto.common.KVPair> 
      getAttributesList();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.common.KVPair attributes = 2;</code>
   */
  com.github.jtendermint.jabci.proto.common.KVPair getAttributes(int index);
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.common.KVPair attributes = 2;</code>
   */
  int getAttributesCount();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.common.KVPair attributes = 2;</code>
   */
  java.util.List<? extends com.github.jtendermint.jabci.proto.common.KVPairOrBuilder> 
      getAttributesOrBuilderList();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.common.KVPair attributes = 2;</code>
   */
  com.github.jtendermint.jabci.proto.common.KVPairOrBuilder getAttributesOrBuilder(
      int index);
}
