// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: types.proto

package com.github.jtendermint.jabci.protobuf.types;

public interface RequestInitChainOrBuilder extends
    // @@protoc_insertion_point(interface_extends:types.RequestInitChain)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .types.Validator validators = 1;</code>
   */
  java.util.List<Validator>
      getValidatorsList();
  /**
   * <code>repeated .types.Validator validators = 1;</code>
   */
  Validator getValidators(int index);
  /**
   * <code>repeated .types.Validator validators = 1;</code>
   */
  int getValidatorsCount();
  /**
   * <code>repeated .types.Validator validators = 1;</code>
   */
  java.util.List<? extends ValidatorOrBuilder>
      getValidatorsOrBuilderList();
  /**
   * <code>repeated .types.Validator validators = 1;</code>
   */
  ValidatorOrBuilder getValidatorsOrBuilder(
      int index);
}
