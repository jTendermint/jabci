/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 - 2017
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
import com.github.jtendermint.jabci.protobuf.types.Request;
import com.github.jtendermint.jabci.protobuf.types.RequestBeginBlock;
import com.github.jtendermint.jabci.protobuf.types.RequestCheckTx;
import com.github.jtendermint.jabci.protobuf.types.RequestCommit;
import com.github.jtendermint.jabci.protobuf.types.RequestDeliverTx;
import com.github.jtendermint.jabci.protobuf.types.RequestEcho;
import com.github.jtendermint.jabci.protobuf.types.RequestEndBlock;
import com.github.jtendermint.jabci.protobuf.types.RequestFlush;
import com.github.jtendermint.jabci.protobuf.types.RequestInfo;
import com.github.jtendermint.jabci.protobuf.types.RequestInitChain;
import com.github.jtendermint.jabci.protobuf.types.RequestQuery;
import com.github.jtendermint.jabci.protobuf.types.RequestSetOption;
import com.github.jtendermint.jabci.protobuf.types.Response;
import com.github.jtendermint.jabci.protobuf.types.ResponseBeginBlock;
import com.github.jtendermint.jabci.protobuf.types.ResponseCheckTx;
import com.github.jtendermint.jabci.protobuf.types.ResponseCommit;
import com.github.jtendermint.jabci.protobuf.types.ResponseDeliverTx;
import com.github.jtendermint.jabci.protobuf.types.ResponseEcho;
import com.github.jtendermint.jabci.protobuf.types.ResponseEndBlock;
import com.github.jtendermint.jabci.protobuf.types.ResponseFlush;
import com.github.jtendermint.jabci.protobuf.types.ResponseInfo;
import com.github.jtendermint.jabci.protobuf.types.ResponseInitChain;
import com.github.jtendermint.jabci.protobuf.types.ResponseQuery;
import com.github.jtendermint.jabci.protobuf.types.ResponseSetOption;
import com.google.protobuf.GeneratedMessageV3;

public abstract class ASocket {

    public final static int DEFAULT_LISTEN_SOCKET_PORT = 46658;
    private final static Logger HANDLER_LOG = LoggerFactory.getLogger(ASocket.class);
    private List<Object> _listeners = new ArrayList<>();

    public static String byteArrayToString(byte[] bArr) {
        StringBuilder build = new StringBuilder();
        build.append("< ");
        for (byte b : bArr) {
            build.append(String.format("0x%x ", b));
        }
        build.append(">");
        return build.toString();
    }

    public static void printByteArray(byte[] bArr) {
        for (byte b : bArr) {
            System.out.format("0x%x ", b);
        }
        System.out.print("\n");
    }

    protected GeneratedMessageV3 handleRequest(Request request) throws IOException {
        switch (request.getValueCase()) {
            case ECHO: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.ECHO);
                RequestEcho req = request.getEcho();
                ResponseEcho response = getListenerForType(IEcho.class).requestEcho(req);
                if (response != null)
                    return (Response.newBuilder().setEcho(response).build());
                break;
            }
            case FLUSH: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.FLUSH);
                RequestFlush req = request.getFlush();
                ResponseFlush response = getListenerForType(IFlush.class).requestFlush(req);
                return (Response.newBuilder().setFlush(response).build());
            }
            case INFO: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.INFO);
                RequestInfo req = request.getInfo();
                ResponseInfo response = getListenerForType(IInfo.class).requestInfo(req);
                return (Response.newBuilder().setInfo(response).build());
            }
            case SET_OPTION: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.SET_OPTION);
                RequestSetOption req = request.getSetOption();
                ResponseSetOption response = getListenerForType(ISetOption.class).requestSetOption(req);
                return (Response.newBuilder().setSetOption(response).build());
            }
            case DELIVER_TX: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.DELIVER_TX);
                RequestDeliverTx req = request.getDeliverTx();
                ResponseDeliverTx response = getListenerForType(IDeliverTx.class).receivedDeliverTx(req);
                return (Response.newBuilder().setDeliverTx(response).build());
            }
            case CHECK_TX: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.CHECK_TX);
                RequestCheckTx req = request.getCheckTx();
                ResponseCheckTx response = getListenerForType(ICheckTx.class).requestCheckTx(req);
                return (Response.newBuilder().setCheckTx(response).build());
            }
            case COMMIT: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.COMMIT);
                RequestCommit req = request.getCommit();
                ResponseCommit response = getListenerForType(ICommit.class).requestCommit(req);
                return (Response.newBuilder().setCommit(response).build());
            }
            case QUERY: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.QUERY);
                RequestQuery req = request.getQuery();
                ResponseQuery response = getListenerForType(IQuery.class).requestQuery(req);
                return (Response.newBuilder().setQuery(response).build());
            }
            case INIT_CHAIN: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.INIT_CHAIN);
                RequestInitChain req = request.getInitChain();
                ResponseInitChain response = getListenerForType(IInitChain.class).requestInitChain(req);
                return (Response.newBuilder().setInitChain(response).build());
            }
            case BEGIN_BLOCK: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.BEGIN_BLOCK);
                RequestBeginBlock req = request.getBeginBlock();
                ResponseBeginBlock response = getListenerForType(IBeginBlock.class).requestBeginBlock(req);
                return Response.newBuilder().setBeginBlock(response).build();
            }
            case END_BLOCK: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.END_BLOCK);
                RequestEndBlock req = request.getEndBlock();
                ResponseEndBlock response = getListenerForType(IEndBlock.class).requestEndBlock(req);
                return (Response.newBuilder().setEndBlock(response).build());
            }
            case VALUE_NOT_SET: {
                HANDLER_LOG.debug("Received " + Request.ValueCase.VALUE_NOT_SET);
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

    /**
     * Register a new listener of type ABCIAPI or supertype
     *
     * @param listener
     */
    public void registerListener(Object listener) {
        _listeners.add(listener);
    }

    /**
     * Register a new listener of type ABCIAPI or supertype
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
            if (klass.isInstance(object))
                return (T) object;
        }
        return (T) DefaultFallbackListener.instance;
    }

}
