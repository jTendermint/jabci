// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: types.proto

package com.github.jtendermint.jabci.proto.types;

public interface ResponseEndBlockOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.github.jtendermint.jabci.proto.types.ResponseEndBlock)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.ValidatorUpdate validator_updates = 1;</code>
   */
  java.util.List<com.github.jtendermint.jabci.proto.types.ValidatorUpdate> 
      getValidatorUpdatesList();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.ValidatorUpdate validator_updates = 1;</code>
   */
  com.github.jtendermint.jabci.proto.types.ValidatorUpdate getValidatorUpdates(int index);
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.ValidatorUpdate validator_updates = 1;</code>
   */
  int getValidatorUpdatesCount();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.ValidatorUpdate validator_updates = 1;</code>
   */
  java.util.List<? extends com.github.jtendermint.jabci.proto.types.ValidatorUpdateOrBuilder> 
      getValidatorUpdatesOrBuilderList();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.ValidatorUpdate validator_updates = 1;</code>
   */
  com.github.jtendermint.jabci.proto.types.ValidatorUpdateOrBuilder getValidatorUpdatesOrBuilder(
      int index);

  /**
   * <code>.com.github.jtendermint.jabci.proto.types.ConsensusParams consensus_param_updates = 2;</code>
   */
  boolean hasConsensusParamUpdates();
  /**
   * <code>.com.github.jtendermint.jabci.proto.types.ConsensusParams consensus_param_updates = 2;</code>
   */
  com.github.jtendermint.jabci.proto.types.ConsensusParams getConsensusParamUpdates();
  /**
   * <code>.com.github.jtendermint.jabci.proto.types.ConsensusParams consensus_param_updates = 2;</code>
   */
  com.github.jtendermint.jabci.proto.types.ConsensusParamsOrBuilder getConsensusParamUpdatesOrBuilder();

  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.Event events = 3;</code>
   */
  java.util.List<com.github.jtendermint.jabci.proto.types.Event> 
      getEventsList();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.Event events = 3;</code>
   */
  com.github.jtendermint.jabci.proto.types.Event getEvents(int index);
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.Event events = 3;</code>
   */
  int getEventsCount();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.Event events = 3;</code>
   */
  java.util.List<? extends com.github.jtendermint.jabci.proto.types.EventOrBuilder> 
      getEventsOrBuilderList();
  /**
   * <code>repeated .com.github.jtendermint.jabci.proto.types.Event events = 3;</code>
   */
  com.github.jtendermint.jabci.proto.types.EventOrBuilder getEventsOrBuilder(
      int index);
}
