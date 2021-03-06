// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: merkle.proto

package com.github.jtendermint.jabci.types;

/**
 * <pre>
 * Proof is Merkle proof defined by the list of ProofOps
 * </pre>
 *
 * Protobuf type {@code com.github.jtendermint.jabci.types.Proof}
 */
public  final class Proof extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.github.jtendermint.jabci.types.Proof)
    ProofOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Proof.newBuilder() to construct.
  private Proof(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Proof() {
    ops_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Proof(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              ops_ = new java.util.ArrayList<com.github.jtendermint.jabci.types.ProofOp>();
              mutable_bitField0_ |= 0x00000001;
            }
            ops_.add(
                input.readMessage(com.github.jtendermint.jabci.types.ProofOp.parser(), extensionRegistry));
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        ops_ = java.util.Collections.unmodifiableList(ops_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.github.jtendermint.jabci.types.Merkle.internal_static_com_github_jtendermint_jabci_types_Proof_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.github.jtendermint.jabci.types.Merkle.internal_static_com_github_jtendermint_jabci_types_Proof_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.github.jtendermint.jabci.types.Proof.class, com.github.jtendermint.jabci.types.Proof.Builder.class);
  }

  public static final int OPS_FIELD_NUMBER = 1;
  private java.util.List<com.github.jtendermint.jabci.types.ProofOp> ops_;
  /**
   * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
   */
  public java.util.List<com.github.jtendermint.jabci.types.ProofOp> getOpsList() {
    return ops_;
  }
  /**
   * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
   */
  public java.util.List<? extends com.github.jtendermint.jabci.types.ProofOpOrBuilder> 
      getOpsOrBuilderList() {
    return ops_;
  }
  /**
   * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
   */
  public int getOpsCount() {
    return ops_.size();
  }
  /**
   * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
   */
  public com.github.jtendermint.jabci.types.ProofOp getOps(int index) {
    return ops_.get(index);
  }
  /**
   * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
   */
  public com.github.jtendermint.jabci.types.ProofOpOrBuilder getOpsOrBuilder(
      int index) {
    return ops_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < ops_.size(); i++) {
      output.writeMessage(1, ops_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < ops_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, ops_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.github.jtendermint.jabci.types.Proof)) {
      return super.equals(obj);
    }
    com.github.jtendermint.jabci.types.Proof other = (com.github.jtendermint.jabci.types.Proof) obj;

    if (!getOpsList()
        .equals(other.getOpsList())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getOpsCount() > 0) {
      hash = (37 * hash) + OPS_FIELD_NUMBER;
      hash = (53 * hash) + getOpsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.github.jtendermint.jabci.types.Proof parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.github.jtendermint.jabci.types.Proof parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.github.jtendermint.jabci.types.Proof parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.github.jtendermint.jabci.types.Proof parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.github.jtendermint.jabci.types.Proof prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * Proof is Merkle proof defined by the list of ProofOps
   * </pre>
   *
   * Protobuf type {@code com.github.jtendermint.jabci.types.Proof}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.github.jtendermint.jabci.types.Proof)
      com.github.jtendermint.jabci.types.ProofOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.github.jtendermint.jabci.types.Merkle.internal_static_com_github_jtendermint_jabci_types_Proof_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.github.jtendermint.jabci.types.Merkle.internal_static_com_github_jtendermint_jabci_types_Proof_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.github.jtendermint.jabci.types.Proof.class, com.github.jtendermint.jabci.types.Proof.Builder.class);
    }

    // Construct using com.github.jtendermint.jabci.types.Proof.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getOpsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (opsBuilder_ == null) {
        ops_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        opsBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.github.jtendermint.jabci.types.Merkle.internal_static_com_github_jtendermint_jabci_types_Proof_descriptor;
    }

    @java.lang.Override
    public com.github.jtendermint.jabci.types.Proof getDefaultInstanceForType() {
      return com.github.jtendermint.jabci.types.Proof.getDefaultInstance();
    }

    @java.lang.Override
    public com.github.jtendermint.jabci.types.Proof build() {
      com.github.jtendermint.jabci.types.Proof result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.github.jtendermint.jabci.types.Proof buildPartial() {
      com.github.jtendermint.jabci.types.Proof result = new com.github.jtendermint.jabci.types.Proof(this);
      int from_bitField0_ = bitField0_;
      if (opsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          ops_ = java.util.Collections.unmodifiableList(ops_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.ops_ = ops_;
      } else {
        result.ops_ = opsBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.github.jtendermint.jabci.types.Proof) {
        return mergeFrom((com.github.jtendermint.jabci.types.Proof)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.github.jtendermint.jabci.types.Proof other) {
      if (other == com.github.jtendermint.jabci.types.Proof.getDefaultInstance()) return this;
      if (opsBuilder_ == null) {
        if (!other.ops_.isEmpty()) {
          if (ops_.isEmpty()) {
            ops_ = other.ops_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureOpsIsMutable();
            ops_.addAll(other.ops_);
          }
          onChanged();
        }
      } else {
        if (!other.ops_.isEmpty()) {
          if (opsBuilder_.isEmpty()) {
            opsBuilder_.dispose();
            opsBuilder_ = null;
            ops_ = other.ops_;
            bitField0_ = (bitField0_ & ~0x00000001);
            opsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getOpsFieldBuilder() : null;
          } else {
            opsBuilder_.addAllMessages(other.ops_);
          }
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.github.jtendermint.jabci.types.Proof parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.github.jtendermint.jabci.types.Proof) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<com.github.jtendermint.jabci.types.ProofOp> ops_ =
      java.util.Collections.emptyList();
    private void ensureOpsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        ops_ = new java.util.ArrayList<com.github.jtendermint.jabci.types.ProofOp>(ops_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.github.jtendermint.jabci.types.ProofOp, com.github.jtendermint.jabci.types.ProofOp.Builder, com.github.jtendermint.jabci.types.ProofOpOrBuilder> opsBuilder_;

    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public java.util.List<com.github.jtendermint.jabci.types.ProofOp> getOpsList() {
      if (opsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(ops_);
      } else {
        return opsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public int getOpsCount() {
      if (opsBuilder_ == null) {
        return ops_.size();
      } else {
        return opsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public com.github.jtendermint.jabci.types.ProofOp getOps(int index) {
      if (opsBuilder_ == null) {
        return ops_.get(index);
      } else {
        return opsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder setOps(
        int index, com.github.jtendermint.jabci.types.ProofOp value) {
      if (opsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureOpsIsMutable();
        ops_.set(index, value);
        onChanged();
      } else {
        opsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder setOps(
        int index, com.github.jtendermint.jabci.types.ProofOp.Builder builderForValue) {
      if (opsBuilder_ == null) {
        ensureOpsIsMutable();
        ops_.set(index, builderForValue.build());
        onChanged();
      } else {
        opsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder addOps(com.github.jtendermint.jabci.types.ProofOp value) {
      if (opsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureOpsIsMutable();
        ops_.add(value);
        onChanged();
      } else {
        opsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder addOps(
        int index, com.github.jtendermint.jabci.types.ProofOp value) {
      if (opsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureOpsIsMutable();
        ops_.add(index, value);
        onChanged();
      } else {
        opsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder addOps(
        com.github.jtendermint.jabci.types.ProofOp.Builder builderForValue) {
      if (opsBuilder_ == null) {
        ensureOpsIsMutable();
        ops_.add(builderForValue.build());
        onChanged();
      } else {
        opsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder addOps(
        int index, com.github.jtendermint.jabci.types.ProofOp.Builder builderForValue) {
      if (opsBuilder_ == null) {
        ensureOpsIsMutable();
        ops_.add(index, builderForValue.build());
        onChanged();
      } else {
        opsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder addAllOps(
        java.lang.Iterable<? extends com.github.jtendermint.jabci.types.ProofOp> values) {
      if (opsBuilder_ == null) {
        ensureOpsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, ops_);
        onChanged();
      } else {
        opsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder clearOps() {
      if (opsBuilder_ == null) {
        ops_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        opsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public Builder removeOps(int index) {
      if (opsBuilder_ == null) {
        ensureOpsIsMutable();
        ops_.remove(index);
        onChanged();
      } else {
        opsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public com.github.jtendermint.jabci.types.ProofOp.Builder getOpsBuilder(
        int index) {
      return getOpsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public com.github.jtendermint.jabci.types.ProofOpOrBuilder getOpsOrBuilder(
        int index) {
      if (opsBuilder_ == null) {
        return ops_.get(index);  } else {
        return opsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public java.util.List<? extends com.github.jtendermint.jabci.types.ProofOpOrBuilder> 
         getOpsOrBuilderList() {
      if (opsBuilder_ != null) {
        return opsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(ops_);
      }
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public com.github.jtendermint.jabci.types.ProofOp.Builder addOpsBuilder() {
      return getOpsFieldBuilder().addBuilder(
          com.github.jtendermint.jabci.types.ProofOp.getDefaultInstance());
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public com.github.jtendermint.jabci.types.ProofOp.Builder addOpsBuilder(
        int index) {
      return getOpsFieldBuilder().addBuilder(
          index, com.github.jtendermint.jabci.types.ProofOp.getDefaultInstance());
    }
    /**
     * <code>repeated .com.github.jtendermint.jabci.types.ProofOp ops = 1;</code>
     */
    public java.util.List<com.github.jtendermint.jabci.types.ProofOp.Builder> 
         getOpsBuilderList() {
      return getOpsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.github.jtendermint.jabci.types.ProofOp, com.github.jtendermint.jabci.types.ProofOp.Builder, com.github.jtendermint.jabci.types.ProofOpOrBuilder> 
        getOpsFieldBuilder() {
      if (opsBuilder_ == null) {
        opsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.github.jtendermint.jabci.types.ProofOp, com.github.jtendermint.jabci.types.ProofOp.Builder, com.github.jtendermint.jabci.types.ProofOpOrBuilder>(
                ops_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        ops_ = null;
      }
      return opsBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.github.jtendermint.jabci.types.Proof)
  }

  // @@protoc_insertion_point(class_scope:com.github.jtendermint.jabci.types.Proof)
  private static final com.github.jtendermint.jabci.types.Proof DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.github.jtendermint.jabci.types.Proof();
  }

  public static com.github.jtendermint.jabci.types.Proof getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Proof>
      PARSER = new com.google.protobuf.AbstractParser<Proof>() {
    @java.lang.Override
    public Proof parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Proof(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Proof> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Proof> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.github.jtendermint.jabci.types.Proof getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

