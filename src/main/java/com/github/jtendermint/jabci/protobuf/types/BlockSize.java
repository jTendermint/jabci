// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: types.proto

package com.github.jtendermint.jabci.protobuf.types;

/**
 * <pre>
 * BlockSize contain limits on the block size.
 * </pre>
 *
 * Protobuf type {@code types.BlockSize}
 */
public  final class BlockSize extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:types.BlockSize)
    BlockSizeOrBuilder {
private static final long serialVersionUID = 0L;
  // Use BlockSize.newBuilder() to construct.
  private BlockSize(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private BlockSize() {
    maxBytes_ = 0;
    maxTxs_ = 0;
    maxGas_ = 0L;
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private BlockSize(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
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
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            maxBytes_ = input.readInt32();
            break;
          }
          case 16: {

            maxTxs_ = input.readInt32();
            break;
          }
          case 24: {

            maxGas_ = input.readInt64();
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
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return Types.internal_static_types_BlockSize_descriptor;
  }

  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return Types.internal_static_types_BlockSize_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            BlockSize.class, BlockSize.Builder.class);
  }

  public static final int MAX_BYTES_FIELD_NUMBER = 1;
  private int maxBytes_;
  /**
   * <code>int32 max_bytes = 1;</code>
   */
  public int getMaxBytes() {
    return maxBytes_;
  }

  public static final int MAX_TXS_FIELD_NUMBER = 2;
  private int maxTxs_;
  /**
   * <code>int32 max_txs = 2;</code>
   */
  public int getMaxTxs() {
    return maxTxs_;
  }

  public static final int MAX_GAS_FIELD_NUMBER = 3;
  private long maxGas_;
  /**
   * <code>int64 max_gas = 3;</code>
   */
  public long getMaxGas() {
    return maxGas_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (maxBytes_ != 0) {
      output.writeInt32(1, maxBytes_);
    }
    if (maxTxs_ != 0) {
      output.writeInt32(2, maxTxs_);
    }
    if (maxGas_ != 0L) {
      output.writeInt64(3, maxGas_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (maxBytes_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, maxBytes_);
    }
    if (maxTxs_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, maxTxs_);
    }
    if (maxGas_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, maxGas_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof BlockSize)) {
      return super.equals(obj);
    }
    BlockSize other = (BlockSize) obj;

    boolean result = true;
    result = result && (getMaxBytes()
        == other.getMaxBytes());
    result = result && (getMaxTxs()
        == other.getMaxTxs());
    result = result && (getMaxGas()
        == other.getMaxGas());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + MAX_BYTES_FIELD_NUMBER;
    hash = (53 * hash) + getMaxBytes();
    hash = (37 * hash) + MAX_TXS_FIELD_NUMBER;
    hash = (53 * hash) + getMaxTxs();
    hash = (37 * hash) + MAX_GAS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getMaxGas());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static BlockSize parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BlockSize parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BlockSize parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BlockSize parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BlockSize parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BlockSize parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BlockSize parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static BlockSize parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static BlockSize parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static BlockSize parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static BlockSize parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static BlockSize parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(BlockSize prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * BlockSize contain limits on the block size.
   * </pre>
   *
   * Protobuf type {@code types.BlockSize}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:types.BlockSize)
      BlockSizeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return Types.internal_static_types_BlockSize_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return Types.internal_static_types_BlockSize_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              BlockSize.class, BlockSize.Builder.class);
    }

    // Construct using com.github.jtendermint.jabci.protobuf.types.BlockSize.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      maxBytes_ = 0;

      maxTxs_ = 0;

      maxGas_ = 0L;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return Types.internal_static_types_BlockSize_descriptor;
    }

    public BlockSize getDefaultInstanceForType() {
      return BlockSize.getDefaultInstance();
    }

    public BlockSize build() {
      BlockSize result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public BlockSize buildPartial() {
      BlockSize result = new BlockSize(this);
      result.maxBytes_ = maxBytes_;
      result.maxTxs_ = maxTxs_;
      result.maxGas_ = maxGas_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof BlockSize) {
        return mergeFrom((BlockSize)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(BlockSize other) {
      if (other == BlockSize.getDefaultInstance()) return this;
      if (other.getMaxBytes() != 0) {
        setMaxBytes(other.getMaxBytes());
      }
      if (other.getMaxTxs() != 0) {
        setMaxTxs(other.getMaxTxs());
      }
      if (other.getMaxGas() != 0L) {
        setMaxGas(other.getMaxGas());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      BlockSize parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (BlockSize) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int maxBytes_ ;
    /**
     * <code>int32 max_bytes = 1;</code>
     */
    public int getMaxBytes() {
      return maxBytes_;
    }
    /**
     * <code>int32 max_bytes = 1;</code>
     */
    public Builder setMaxBytes(int value) {

      maxBytes_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 max_bytes = 1;</code>
     */
    public Builder clearMaxBytes() {

      maxBytes_ = 0;
      onChanged();
      return this;
    }

    private int maxTxs_ ;
    /**
     * <code>int32 max_txs = 2;</code>
     */
    public int getMaxTxs() {
      return maxTxs_;
    }
    /**
     * <code>int32 max_txs = 2;</code>
     */
    public Builder setMaxTxs(int value) {

      maxTxs_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 max_txs = 2;</code>
     */
    public Builder clearMaxTxs() {

      maxTxs_ = 0;
      onChanged();
      return this;
    }

    private long maxGas_ ;
    /**
     * <code>int64 max_gas = 3;</code>
     */
    public long getMaxGas() {
      return maxGas_;
    }
    /**
     * <code>int64 max_gas = 3;</code>
     */
    public Builder setMaxGas(long value) {

      maxGas_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 max_gas = 3;</code>
     */
    public Builder clearMaxGas() {

      maxGas_ = 0L;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:types.BlockSize)
  }

  // @@protoc_insertion_point(class_scope:types.BlockSize)
  private static final BlockSize DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new BlockSize();
  }

  public static BlockSize getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BlockSize>
      PARSER = new com.google.protobuf.AbstractParser<BlockSize>() {
    public BlockSize parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new BlockSize(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<BlockSize> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<BlockSize> getParserForType() {
    return PARSER;
  }

  public BlockSize getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

