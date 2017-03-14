package com.github.jtendermint.jabci.socket;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jtendermint.jabci.api.IBeginBlock;
import com.github.jtendermint.jabci.api.ICheckTx;
import com.github.jtendermint.jabci.api.ICommit;
import com.github.jtendermint.jabci.api.IDeliverTx;
import com.github.jtendermint.jabci.api.IEcho;
import com.github.jtendermint.jabci.api.IEndBlock;
import com.github.jtendermint.jabci.api.IFlush;
import com.github.jtendermint.jabci.api.IInfo;
import com.github.jtendermint.jabci.api.IInitChain;
import com.github.jtendermint.jabci.api.IQuery;
import com.github.jtendermint.jabci.api.ISetOption;
import com.github.jtendermint.jabci.types.Types;
import com.github.jtendermint.jabci.types.Types.Request;
import com.github.jtendermint.jabci.types.Types.RequestBeginBlock;
import com.github.jtendermint.jabci.types.Types.RequestCheckTx;
import com.github.jtendermint.jabci.types.Types.RequestCommit;
import com.github.jtendermint.jabci.types.Types.RequestDeliverTx;
import com.github.jtendermint.jabci.types.Types.RequestEcho;
import com.github.jtendermint.jabci.types.Types.RequestEndBlock;
import com.github.jtendermint.jabci.types.Types.RequestFlush;
import com.github.jtendermint.jabci.types.Types.RequestInfo;
import com.github.jtendermint.jabci.types.Types.RequestInitChain;
import com.github.jtendermint.jabci.types.Types.RequestQuery;
import com.github.jtendermint.jabci.types.Types.RequestSetOption;
import com.github.jtendermint.jabci.types.Types.Response;
import com.github.jtendermint.jabci.types.Types.ResponseBeginBlock;
import com.github.jtendermint.jabci.types.Types.ResponseCheckTx;
import com.github.jtendermint.jabci.types.Types.ResponseCommit;
import com.github.jtendermint.jabci.types.Types.ResponseDeliverTx;
import com.github.jtendermint.jabci.types.Types.ResponseEcho;
import com.github.jtendermint.jabci.types.Types.ResponseEndBlock;
import com.github.jtendermint.jabci.types.Types.ResponseFlush;
import com.github.jtendermint.jabci.types.Types.ResponseInfo;
import com.github.jtendermint.jabci.types.Types.ResponseInitChain;
import com.github.jtendermint.jabci.types.Types.ResponseQuery;
import com.github.jtendermint.jabci.types.Types.ResponseSetOption;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessageV3;

public abstract class ASocket {

    private final static Logger HANDLER_LOG = LoggerFactory.getLogger(ASocket.class);

    public final static int DEFAULT_LISTEN_SOCKET_PORT = 46658;

    private List<Object> _listeners = new ArrayList<>();

    protected ByteBuffer outputForInput(ByteBuffer buffer) {
        try {
            Request req = readMessage(buffer);
            if (req != null) {
                GeneratedMessageV3 handleRequest = handleRequest(req);
                if (handleRequest != null)
                    return responseToByteBuffer(handleRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Request readMessage(ByteBuffer buffer) throws IOException {

        // attachment.buffer.flip();
        buffer.rewind();
        int limits = buffer.limit();

        if (limits == 0)
            return null;

        byte bytes[] = new byte[limits];
        buffer.get(bytes, 0, limits);

        HANDLER_LOG.info("readMessage, capacity: {} , bytes: {}, limit: {}", buffer.capacity(), ASocket.byteArrayToString(bytes), limits);

        // ===============================================================

        CodedInputStream inputStream = CodedInputStream.newInstance(bytes);
        HANDLER_LOG.debug("start reading");

        // Each Message is prefixed by a header from the gitrepos.com/tendermint/go-wire protocol.
        // We get the message length from this header and then parse the actual message using protobuf.
        // To facilitate reading an exact number of bytes we use a CodedInputStream.
        // BufferedInputStream.read would still need to be called in a loop, to ensure that all data is received.

        // Size counter is used to enforce a size limit per message (see CodedInputStream.setSizeLimit()).
        // We need to reset it before reading the next message:
        // inputStream.resetSizeCounter();

        // HEADER: first byte is length of length field
        byte varintLength = inputStream.readRawByte();
        if (varintLength > 4) {
            throw new IllegalStateException("Varint bigger than 4 bytes are not supported! " + varintLength);
        }

        // HEADER: next varintLength bytes contain messageLength:
        // It is a Big-Endian encoded unsigned integer.
        // We only allow 4 bytes, but then have to parse it with one zero byte prepended,
        // since Java does not support unsigned integers.
        byte[] messageLengthBytes = inputStream.readRawBytes(varintLength);
        byte[] messageLengthLongBytes = new byte[5];
        System.arraycopy(messageLengthBytes, 0, messageLengthLongBytes, 5 - varintLength, varintLength);
        long messageLengthLong = new BigInteger(messageLengthLongBytes).longValue();
        if (messageLengthLong > Integer.MAX_VALUE) {
            throw new IllegalStateException("Message lengths of more than Integer.MAX_VALUE are not supported.");
        }
        int messageLength = (int) messageLengthLong;
        HANDLER_LOG.debug("Assuming message length: " + messageLength);

        // PAYLOAD: limit CodedInputStream to messageLength bytes and parse Request using Protobuf:
        int oldLimit = inputStream.pushLimit(messageLength);
        final Types.Request request = Types.Request.parseFrom(inputStream);
        inputStream.popLimit(oldLimit);

        // ===============================================================

        return request;
    }

    protected GeneratedMessageV3 handleRequest(Request request) throws IOException {
        switch (request.getValueCase()) {
        case ECHO: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.ECHO);
            RequestEcho req = request.getEcho();
            ResponseEcho response = getListenerForType(IEcho.class).requestEcho(req);
            if (response != null)
                return (Response.newBuilder().setEcho(response).build());
            break;
        }
        case FLUSH: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.FLUSH);
            RequestFlush req = request.getFlush();
            ResponseFlush response = getListenerForType(IFlush.class).requestFlush(req);
            return (Response.newBuilder().setFlush(response).build());
        }
        case INFO: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.INFO);
            RequestInfo req = request.getInfo();
            ResponseInfo response = getListenerForType(IInfo.class).requestInfo(req);
            return (Response.newBuilder().setInfo(response).build());
        }
        case SET_OPTION: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.SET_OPTION);
            RequestSetOption req = request.getSetOption();
            ResponseSetOption response = getListenerForType(ISetOption.class).requestSetOption(req);
            return (Response.newBuilder().setSetOption(response).build());
        }
        case DELIVER_TX: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.DELIVER_TX);
            RequestDeliverTx req = request.getDeliverTx();
            ResponseDeliverTx response = getListenerForType(IDeliverTx.class).receivedDeliverTx(req);
            return (Response.newBuilder().setDeliverTx(response).build());
        }
        case CHECK_TX: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.CHECK_TX);
            RequestCheckTx req = request.getCheckTx();
            ResponseCheckTx response = getListenerForType(ICheckTx.class).requestCheckTx(req);
            return (Response.newBuilder().setCheckTx(response).build());
        }
        case COMMIT: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.COMMIT);
            RequestCommit req = request.getCommit();
            ResponseCommit response = getListenerForType(ICommit.class).requestCommit(req);
            return (Response.newBuilder().setCommit(response).build());
        }
        case QUERY: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.QUERY);
            RequestQuery req = request.getQuery();
            ResponseQuery response = getListenerForType(IQuery.class).requestQuery(req);
            return (Response.newBuilder().setQuery(response).build());
        }
        case INIT_CHAIN: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.INIT_CHAIN);
            RequestInitChain req = request.getInitChain();
            ResponseInitChain response = getListenerForType(IInitChain.class).requestInitChain(req);
            return (Response.newBuilder().setInitChain(response).build());
        }
        case BEGIN_BLOCK: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.BEGIN_BLOCK);
            RequestBeginBlock req = request.getBeginBlock();
            ResponseBeginBlock response = getListenerForType(IBeginBlock.class).requestBeginBlock(req);
            return Response.newBuilder().setBeginBlock(response).build();
        }
        case END_BLOCK: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.END_BLOCK);
            RequestEndBlock req = request.getEndBlock();
            ResponseEndBlock response = getListenerForType(IEndBlock.class).requestEndBlock(req);
            return (Response.newBuilder().setEndBlock(response).build());
        }
        case VALUE_NOT_SET: {
            HANDLER_LOG.debug("Received " + Types.Request.ValueCase.VALUE_NOT_SET);
            return null;
        }
        default:
            throw new IllegalStateException(String.format("Received unknown ValueCase '%s' in request", request.getValueCase()));
        }
        return null;
    }

    protected ByteBuffer responseToByteBuffer(GeneratedMessageV3 message) {
        int length = message.toByteArray().length;
        byte[] varint = BigInteger.valueOf(length).toByteArray();
        long varintLength = varint.length;
        byte[] varintPrefix = BigInteger.valueOf(varintLength).toByteArray();

        ByteBuffer combined = ByteBuffer.allocate(varintPrefix.length + varint.length + length);

        combined.put(varintPrefix);
        combined.put(varint);
        combined.put(message.toByteArray());
        combined.flip();
        return combined;
    }

    protected ByteBuffer[] responseBytes(GeneratedMessageV3 message) {
        int length = message.toByteArray().length;
        byte[] varint = BigInteger.valueOf(length).toByteArray();
        long varintLength = varint.length;
        byte[] varintPrefix = BigInteger.valueOf(varintLength).toByteArray();

        ByteBuffer[] result = new ByteBuffer[] { ByteBuffer.wrap(varintPrefix), ByteBuffer.wrap(varint),
                ByteBuffer.wrap(message.toByteArray()) };

        return result;
    }

    /**
     * Register a new listener of type TMSPAPI or supertype
     * 
     * @param listener
     */
    public void registerListener(Object listener) {
        _listeners.add(listener);
    }

    /**
     * Register a new listener of type TMSPAPI or supertype
     * 
     * @param listener
     */
    public void removeListener(Object listener) {
        _listeners.remove(listener);
    }

    /**
     * Returns the <b>first</b> listener for specified class or a {@link DefaultFallbackListener} if nothing was found
     * 
     * @param klass
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getListenerForType(Class<T> klass) {
        for (Object object : _listeners) {
            for (Class<?> clsss : object.getClass().getInterfaces()) {
                if (clsss == klass)
                    return (T) object;
            }
            //
            // List<Class<?>> clssss = Arrays.asList(object.getClass().getInterfaces());
            // if (clssss.contains(klass) || clssss.contains(ABCIAPI.class)) {
            // return (T) object;
            // }
        }
        return (T) DefaultFallbackListener.instance;
    }

    public static String byteArrayToString(byte[] bArr) {
        StringBuilder build = new StringBuilder();
        build.append("< ");
        for (byte b : bArr) {
            build.append(String.format("0x%x ", b));
        }
        build.append(">");
        return build.toString();
    }

}
