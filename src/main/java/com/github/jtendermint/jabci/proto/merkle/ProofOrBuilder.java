// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: merkle.proto

package com.github.jtendermint.jabci.proto.merkle;

public interface ProofOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.github.jtendermint.jabci.proto.merkle.Proof)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.merkle.ProofOp ops = 1;</code>
   */
  java.util.List<com.github.jtendermint.jabci.proto.merkle.ProofOp> 
      getOpsList();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.merkle.ProofOp ops = 1;</code>
   */
  com.github.jtendermint.jabci.proto.merkle.ProofOp getOps(int index);
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.merkle.ProofOp ops = 1;</code>
   */
  int getOpsCount();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.merkle.ProofOp ops = 1;</code>
   */
  java.util.List<? extends com.github.jtendermint.jabci.proto.merkle.ProofOpOrBuilder> 
      getOpsOrBuilderList();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.merkle.ProofOp ops = 1;</code>
   */
  com.github.jtendermint.jabci.proto.merkle.ProofOpOrBuilder getOpsOrBuilder(
      int index);
}
