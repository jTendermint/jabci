package com.github.jtendermint.jabci;

public enum CodeType
        implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>OK = 0;</code>
     */
    OK(0),
    /**
     * <pre>
     * General response codes, 0 ~ 99
     * </pre>
     *
     * <code>InternalError = 1;</code>
     */
    InternalError(1),
    /**
     * <code>EncodingError = 2;</code>
     */
    EncodingError(2),
    /**
     * <code>BadNonce = 3;</code>
     */
    BadNonce(3),
    /**
     * <code>Unauthorized = 4;</code>
     */
    Unauthorized(4),
    /**
     * <code>InsufficientFunds = 5;</code>
     */
    InsufficientFunds(5),
    /**
     * <code>UnknownRequest = 6;</code>
     */
    UnknownRequest(6),
    /**
     * <pre>
     * Reserved for basecoin, 100 ~ 199
     * </pre>
     *
     * <code>BaseDuplicateAddress = 101;</code>
     */
    BaseDuplicateAddress(101),
    /**
     * <code>BaseEncodingError = 102;</code>
     */
    BaseEncodingError(102),
    /**
     * <code>BaseInsufficientFees = 103;</code>
     */
    BaseInsufficientFees(103),
    /**
     * <code>BaseInsufficientFunds = 104;</code>
     */
    BaseInsufficientFunds(104),
    /**
     * <code>BaseInsufficientGasPrice = 105;</code>
     */
    BaseInsufficientGasPrice(105),
    /**
     * <code>BaseInvalidInput = 106;</code>
     */
    BaseInvalidInput(106),
    /**
     * <code>BaseInvalidOutput = 107;</code>
     */
    BaseInvalidOutput(107),
    /**
     * <code>BaseInvalidPubKey = 108;</code>
     */
    BaseInvalidPubKey(108),
    /**
     * <code>BaseInvalidSequence = 109;</code>
     */
    BaseInvalidSequence(109),
    /**
     * <code>BaseInvalidSignature = 110;</code>
     */
    BaseInvalidSignature(110),
    /**
     * <code>BaseUnknownAddress = 111;</code>
     */
    BaseUnknownAddress(111),
    /**
     * <code>BaseUnknownPubKey = 112;</code>
     */
    BaseUnknownPubKey(112),
    /**
     * <code>BaseUnknownPlugin = 113;</code>
     */
    BaseUnknownPlugin(113),
    /**
     * <pre>
     * Reserved for governance, 200 ~ 299
     * </pre>
     *
     * <code>GovUnknownEntity = 201;</code>
     */
    GovUnknownEntity(201),
    /**
     * <code>GovUnknownGroup = 202;</code>
     */
    GovUnknownGroup(202),
    /**
     * <code>GovUnknownProposal = 203;</code>
     */
    GovUnknownProposal(203),
    /**
     * <code>GovDuplicateGroup = 204;</code>
     */
    GovDuplicateGroup(204),
    /**
     * <code>GovDuplicateMember = 205;</code>
     */
    GovDuplicateMember(205),
    /**
     * <code>GovDuplicateProposal = 206;</code>
     */
    GovDuplicateProposal(206),
    /**
     * <code>GovDuplicateVote = 207;</code>
     */
    GovDuplicateVote(207),
    /**
     * <code>GovInvalidMember = 208;</code>
     */
    GovInvalidMember(208),
    /**
     * <code>GovInvalidVote = 209;</code>
     */
    GovInvalidVote(209),
    /**
     * <code>GovInvalidVotingPower = 210;</code>
     */
    GovInvalidVotingPower(210),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>OK = 0;</code>
     */
    public static final int OK_VALUE = 0;
    /**
     * <pre>
     * General response codes, 0 ~ 99
     * </pre>
     *
     * <code>InternalError = 1;</code>
     */
    public static final int InternalError_VALUE = 1;
    /**
     * <code>EncodingError = 2;</code>
     */
    public static final int EncodingError_VALUE = 2;
    /**
     * <code>BadNonce = 3;</code>
     */
    public static final int BadNonce_VALUE = 3;
    /**
     * <code>Unauthorized = 4;</code>
     */
    public static final int Unauthorized_VALUE = 4;
    /**
     * <code>InsufficientFunds = 5;</code>
     */
    public static final int InsufficientFunds_VALUE = 5;
    /**
     * <code>UnknownRequest = 6;</code>
     */
    public static final int UnknownRequest_VALUE = 6;
    /**
     * <pre>
     * Reserved for basecoin, 100 ~ 199
     * </pre>
     *
     * <code>BaseDuplicateAddress = 101;</code>
     */
    public static final int BaseDuplicateAddress_VALUE = 101;
    /**
     * <code>BaseEncodingError = 102;</code>
     */
    public static final int BaseEncodingError_VALUE = 102;
    /**
     * <code>BaseInsufficientFees = 103;</code>
     */
    public static final int BaseInsufficientFees_VALUE = 103;
    /**
     * <code>BaseInsufficientFunds = 104;</code>
     */
    public static final int BaseInsufficientFunds_VALUE = 104;
    /**
     * <code>BaseInsufficientGasPrice = 105;</code>
     */
    public static final int BaseInsufficientGasPrice_VALUE = 105;
    /**
     * <code>BaseInvalidInput = 106;</code>
     */
    public static final int BaseInvalidInput_VALUE = 106;
    /**
     * <code>BaseInvalidOutput = 107;</code>
     */
    public static final int BaseInvalidOutput_VALUE = 107;
    /**
     * <code>BaseInvalidPubKey = 108;</code>
     */
    public static final int BaseInvalidPubKey_VALUE = 108;
    /**
     * <code>BaseInvalidSequence = 109;</code>
     */
    public static final int BaseInvalidSequence_VALUE = 109;
    /**
     * <code>BaseInvalidSignature = 110;</code>
     */
    public static final int BaseInvalidSignature_VALUE = 110;
    /**
     * <code>BaseUnknownAddress = 111;</code>
     */
    public static final int BaseUnknownAddress_VALUE = 111;
    /**
     * <code>BaseUnknownPubKey = 112;</code>
     */
    public static final int BaseUnknownPubKey_VALUE = 112;
    /**
     * <code>BaseUnknownPlugin = 113;</code>
     */
    public static final int BaseUnknownPlugin_VALUE = 113;
    /**
     * <pre>
     * Reserved for governance, 200 ~ 299
     * </pre>
     *
     * <code>GovUnknownEntity = 201;</code>
     */
    public static final int GovUnknownEntity_VALUE = 201;
    /**
     * <code>GovUnknownGroup = 202;</code>
     */
    public static final int GovUnknownGroup_VALUE = 202;
    /**
     * <code>GovUnknownProposal = 203;</code>
     */
    public static final int GovUnknownProposal_VALUE = 203;
    /**
     * <code>GovDuplicateGroup = 204;</code>
     */
    public static final int GovDuplicateGroup_VALUE = 204;
    /**
     * <code>GovDuplicateMember = 205;</code>
     */
    public static final int GovDuplicateMember_VALUE = 205;
    /**
     * <code>GovDuplicateProposal = 206;</code>
     */
    public static final int GovDuplicateProposal_VALUE = 206;
    /**
     * <code>GovDuplicateVote = 207;</code>
     */
    public static final int GovDuplicateVote_VALUE = 207;
    /**
     * <code>GovInvalidMember = 208;</code>
     */
    public static final int GovInvalidMember_VALUE = 208;
    /**
     * <code>GovInvalidVote = 209;</code>
     */
    public static final int GovInvalidVote_VALUE = 209;
    /**
     * <code>GovInvalidVotingPower = 210;</code>
     */
    public static final int GovInvalidVotingPower_VALUE = 210;


    public final int getNumber() {
        if (this == UNRECOGNIZED) {
            throw new java.lang.IllegalArgumentException(
                    "Can't get the number of an unknown enum value.");
        }
        return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static CodeType valueOf(int value) {
        return forNumber(value);
    }

    public static CodeType forNumber(int value) {
        switch (value) {
            case 0: return OK;
            case 1: return InternalError;
            case 2: return EncodingError;
            case 3: return BadNonce;
            case 4: return Unauthorized;
            case 5: return InsufficientFunds;
            case 6: return UnknownRequest;
            case 101: return BaseDuplicateAddress;
            case 102: return BaseEncodingError;
            case 103: return BaseInsufficientFees;
            case 104: return BaseInsufficientFunds;
            case 105: return BaseInsufficientGasPrice;
            case 106: return BaseInvalidInput;
            case 107: return BaseInvalidOutput;
            case 108: return BaseInvalidPubKey;
            case 109: return BaseInvalidSequence;
            case 110: return BaseInvalidSignature;
            case 111: return BaseUnknownAddress;
            case 112: return BaseUnknownPubKey;
            case 113: return BaseUnknownPlugin;
            case 201: return GovUnknownEntity;
            case 202: return GovUnknownGroup;
            case 203: return GovUnknownProposal;
            case 204: return GovDuplicateGroup;
            case 205: return GovDuplicateMember;
            case 206: return GovDuplicateProposal;
            case 207: return GovDuplicateVote;
            case 208: return GovInvalidMember;
            case 209: return GovInvalidVote;
            case 210: return GovInvalidVotingPower;
            default: return null;
        }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<CodeType>
    internalGetValueMap() {
        return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
            CodeType> internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<CodeType>() {
                public CodeType findValueByNumber(int number) {
                    return CodeType.forNumber(number);
                }
            };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
    getValueDescriptor() {
        return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
    getDescriptorForType() {
        return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
    getDescriptor() {
        return com.github.jtendermint.jabci.types.Types.getDescriptor().getEnumTypes().get(0);
    }

    private static final CodeType[] VALUES = values();

    public static CodeType valueOf(
            com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
            throw new java.lang.IllegalArgumentException(
                    "EnumValueDescriptor is not for this type.");
        }
        if (desc.getIndex() == -1) {
            return UNRECOGNIZED;
        }
        return VALUES[desc.getIndex()];
    }

    private final int value;

    private CodeType(int value) {
        this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.github.jtendermint.jabci.types.CodeType)
}