// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: types.proto

package com.github.jtendermint.jabci.proto.types;

public interface BlockIDOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.github.jtendermint.jabci.proto.types.BlockID)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bytes hash = 1;</code>
   */
  com.google.protobuf.ByteString getHash();

  /**
   * <code>.com.github.jtendermint.jabci.proto.types.PartSetHeader parts_header = 2;</code>
   */
  boolean hasPartsHeader();
  /**
   * <code>.com.github.jtendermint.jabci.proto.types.PartSetHeader parts_header = 2;</code>
   */
  com.github.jtendermint.jabci.proto.types.PartSetHeader getPartsHeader();
  /**
   * <code>.com.github.jtendermint.jabci.proto.types.PartSetHeader parts_header = 2;</code>
   */
  com.github.jtendermint.jabci.proto.types.PartSetHeaderOrBuilder getPartsHeaderOrBuilder();
}
