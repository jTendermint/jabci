package com.github.jtendermint.jabci.protobuf;

public class CodeType {

    public static final int OK = 0;
    public static final int InternalError = 1;
    public static final int EncodingError = 2;
    public static final int BadNonce = 3;
    public static final int Unauthorized = 4;
    public static final int InsufficientFunds = 5;
    public static final int UnknownRequest = 6;
    public static final int BaseDuplicateAddress = 101;
    public static final int BaseEncodingError = 102;
    public static final int BaseInsufficientFees = 103;
    public static final int BaseInsufficientFunds = 104;
    public static final int BaseInsufficientGasPrice = 105;
    public static final int BaseInvalidInput = 106;
    public static final int BaseInvalidOutput = 107;
    public static final int BaseInvalidPubKey = 108;
    public static final int BaseInvalidSequence = 109;
    public static final int BaseInvalidSignature = 110;
    public static final int BaseUnknownAddress = 111;
    public static final int BaseUnknownPubKey = 112;
    public static final int BaseUnknownPlugin = 113;
    public static final int GovUnknownEntity = 201;
    public static final int GovUnknownGroup = 202;
    public static final int GovUnknownProposal = 203;
    public static final int GovDuplicateGroup = 204;
    public static final int GovDuplicateMember = 205;
    public static final int GovDuplicateProposal = 206;
    public static final int GovDuplicateVote = 207;
    public static final int GovInvalidMember = 208;
    public static final int GovInvalidVote = 209;
    public static final int GovInvalidVotingPower = 210;
    public static final int UNRECOGNIZED = -1;
}
