/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 
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
package com.github.jtmsp.socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jtmsp.api.IAppendTx;
import com.github.jtmsp.api.IBeginBlock;
import com.github.jtmsp.api.ICheckTx;
import com.github.jtmsp.api.ICommit;
import com.github.jtmsp.api.IEcho;
import com.github.jtmsp.api.IEndBlock;
import com.github.jtmsp.api.IFlush;
import com.github.jtmsp.api.IInfo;
import com.github.jtmsp.api.IInitChain;
import com.github.jtmsp.api.IQuery;
import com.github.jtmsp.api.ISetOption;
import com.github.jtmsp.api.TMSPAPI;
import com.github.jtmsp.types.Types;
import com.github.jtmsp.types.Types.RequestAppendTx;
import com.github.jtmsp.types.Types.RequestBeginBlock;
import com.github.jtmsp.types.Types.RequestCheckTx;
import com.github.jtmsp.types.Types.RequestCommit;
import com.github.jtmsp.types.Types.RequestEcho;
import com.github.jtmsp.types.Types.RequestEndBlock;
import com.github.jtmsp.types.Types.RequestFlush;
import com.github.jtmsp.types.Types.RequestInfo;
import com.github.jtmsp.types.Types.RequestInitChain;
import com.github.jtmsp.types.Types.RequestQuery;
import com.github.jtmsp.types.Types.RequestSetOption;
import com.github.jtmsp.types.Types.Response;
import com.github.jtmsp.types.Types.ResponseAppendTx;
import com.github.jtmsp.types.Types.ResponseBeginBlock;
import com.github.jtmsp.types.Types.ResponseCheckTx;
import com.github.jtmsp.types.Types.ResponseCommit;
import com.github.jtmsp.types.Types.ResponseEcho;
import com.github.jtmsp.types.Types.ResponseEndBlock;
import com.github.jtmsp.types.Types.ResponseFlush;
import com.github.jtmsp.types.Types.ResponseInfo;
import com.github.jtmsp.types.Types.ResponseInitChain;
import com.github.jtmsp.types.Types.ResponseQuery;
import com.github.jtmsp.types.Types.ResponseSetOption;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessage;

/**
 * The TSocket acts as the main socket connection to the Tendermint-Node
 * 
 * 
 * @author srmo, wolfposd
 */
@SuppressWarnings("synthetic-access")
public class TSocket {

    public static final int DEFAULT_LISTEN_SOCKET_PORT = 46658;
    private final static Logger SOCKET_LOG = LoggerFactory.getLogger(TSocket.class);
    private final static Logger HANDLER_LOG = LoggerFactory.getLogger(SocketHandler.class);

    private final static AtomicInteger runningThreads = new AtomicInteger();
    private List<Object> _listeners = new ArrayList<>();

    class SocketHandler implements Runnable {
        private final int threadNumber;
        private final Socket socket;
        private CodedInputStream inputStream;
        private BufferedOutputStream outputStream;

        public SocketHandler(Socket socket) {
            this.socket = socket;
            this.threadNumber = runningThreads.getAndIncrement();
        }

        @Override
        public void run() {
            HANDLER_LOG.debug("Starting ThreadNo: " + threadNumber);
            HANDLER_LOG.debug("accepting new client");
            try {
                inputStream = CodedInputStream.newInstance(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());
                while (true) {
                    HANDLER_LOG.debug("start reading");

                    // Each Message is prefixed by a header from the gitrepos.com/tendermint/go-wire protocol.
                    // We get the message length from this header and then parse the actual message using protobuf.
                    // To facilitate reading an exact number of bytes we use a CodedInputStream.
                    // BufferedInputStream.read would still need to be called in a loop, to ensure that all data is received.

                    // Size counter is used to enforce a size limit per message (see CodedInputStream.setSizeLimit()).
                    // We need to reset it before reading the next message:
                    inputStream.resetSizeCounter();

                    // HEADER: first byte is length of length field
                    byte varintLength = inputStream.readRawByte();
                    if (varintLength > 4) {
                        throw new IllegalStateException("Varint bigger than 4 bytes are not supported!");
                    }

                    // HEADER: next varintLength bytes contain messageLength:
                    // It is a Big-Endian encoded unsigned integer.
                    // We only allow 4 bytes, but then have to parse it with one zero byte prepended,
                    // since Java does not support unsigned integers.
                    byte[] messageLengthBytes = inputStream.readRawBytes(varintLength);
                    byte[] messageLengthLongBytes = new byte[5];
                    System.arraycopy(messageLengthBytes, 0, messageLengthLongBytes, 5 - varintLength, varintLength);
                    long messageLengthLong = new BigInteger(messageLengthLongBytes).longValue();
                    if(messageLengthLong > Integer.MAX_VALUE) {
                        throw new IllegalStateException("Message lengths of more than Integer.MAX_VALUE are not supported.");
                    }
                    int messageLength = (int) messageLengthLong;
                    HANDLER_LOG.debug("Assuming message length: " + messageLength);

                    // PAYLOAD: limit CodedInputStream to messageLength bytes and parse Request using Protobuf:
                    int oldLimit = inputStream.pushLimit(messageLength);
                    final Types.Request request = Types.Request.parseFrom(inputStream);
                    inputStream.popLimit(oldLimit);

                    // Process the request that was just read:
                    handleRequest(request);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            HANDLER_LOG.debug("Stopping ThreadNo " + threadNumber);
            runningThreads.getAndDecrement();
        }

        private void handleRequest(Types.Request request) throws IOException {
            switch (request.getValueCase()) {
                case ECHO: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.ECHO);
                    RequestEcho req = request.getEcho();
                    ResponseEcho response = getListenerForType(IEcho.class).requestEcho(req);
                    if (response != null)
                        writeMessage(Response.newBuilder().setEcho(response).build());
                    break;
                }
                case FLUSH: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.FLUSH);
                    RequestFlush req = request.getFlush();
                    ResponseFlush response = getListenerForType(IFlush.class).requestFlush(req);
                    writeMessage(Response.newBuilder().setFlush(response).build());
                    break;
                }
                case INFO: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.INFO);
                    RequestInfo req = request.getInfo();
                    ResponseInfo response = getListenerForType(IInfo.class).requestInfo(req);
                    if (response != null)
                        writeMessage(Response.newBuilder().setInfo(response).build());
                    break;
                }
                case SET_OPTION: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.SET_OPTION);
                    RequestSetOption req = request.getSetOption();
                    ResponseSetOption response = getListenerForType(ISetOption.class).requestSetOption(req);
                    if (response != null)
                        writeMessage(Response.newBuilder().setSetOption(response).build());
                    break;
                }
                case APPEND_TX: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.APPEND_TX);
                    RequestAppendTx req = request.getAppendTx();
                    ResponseAppendTx response = getListenerForType(IAppendTx.class).receivedAppendTx(req);
                    if (response != null)
                        writeMessage(Response.newBuilder().setAppendTx(response).build());
                    break;
                }
                case CHECK_TX: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.CHECK_TX);
                    RequestCheckTx req = request.getCheckTx();
                    ResponseCheckTx response = getListenerForType(ICheckTx.class).requestCheckTx(req);
                    writeMessage(Response.newBuilder().setCheckTx(response).build());
                    break;
                }
                case COMMIT: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.COMMIT);
                    RequestCommit req = request.getCommit();
                    ResponseCommit response = getListenerForType(ICommit.class).requestCommit(req);
                    writeMessage(Response.newBuilder().setCommit(response).build());
                    break;
                }
                case QUERY: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.QUERY);
                    RequestQuery req = request.getQuery();
                    ResponseQuery response = getListenerForType(IQuery.class).requestQuery(req);
                    writeMessage(Response.newBuilder().setQuery(response).build());
                    break;
                }
                case INIT_CHAIN: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.INIT_CHAIN);
                    RequestInitChain req = request.getInitChain();
                    ResponseInitChain response = getListenerForType(IInitChain.class).requestInitChain(req);
                    writeMessage(Response.newBuilder().setInitChain(response).build());
                    break;
                }
                case BEGIN_BLOCK: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.BEGIN_BLOCK);
                    RequestBeginBlock req = request.getBeginBlock();
                    ResponseBeginBlock response = getListenerForType(IBeginBlock.class).requestBeginBlock(req);
                    writeMessage(Response.newBuilder().setBeginBlock(response).build());
                    break;
                }
                case END_BLOCK: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.END_BLOCK);
                    RequestEndBlock req = request.getEndBlock();
                    ResponseEndBlock response = getListenerForType(IEndBlock.class).requestEndBlock(req);
                    writeMessage(Response.newBuilder().setEndBlock(response).build());
                    break;
                }
                case VALUE_NOT_SET: {
                    HANDLER_LOG.debug("Received " + Types.Request.ValueCase.VALUE_NOT_SET);
                    break;
                }
                default:
                    throw new IllegalStateException(String.format("Received unknown ValueCase '%s' in request", request.getValueCase()));
            }
        }

        /**
         * Writes a {@link GeneratedMessage} to the socket output stream
         * @param message
         * @throws IOException
         */
        public void writeMessage(GeneratedMessage message) throws IOException {
            if (message != null) {
                HANDLER_LOG.debug("writing message " + message.getAllFields().keySet());
                writeMessage(message.toByteArray());
            }
        }

        /**
         * writes raw-bytes to the socket output stream
         * @param message
         * @throws IOException
         */
        public void writeMessage(byte[] message) throws IOException {
            long length = message.length;
            byte[] varint = BigInteger.valueOf(length).toByteArray();
            long varintLength = varint.length;
            byte[] varintPrefix = BigInteger.valueOf(varintLength).toByteArray();

            if (outputStream != null) {
                outputStream.write(varintPrefix);
                outputStream.write(varint);
                outputStream.write(message);
                outputStream.flush();
            }
        }

    }

    /**
     * Start listening on the default tmsp port 46658
     */
    public void start() {
        this.start(DEFAULT_LISTEN_SOCKET_PORT);
    }

    /**
     * Start listening on the specified port
     * @param portNumber
     */
    public void start(int portNumber) {
        SOCKET_LOG.debug("starting serversocket");

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new SocketHandler(clientSocket)).start();
                SOCKET_LOG.debug("Started thread for sockethandling...");
            }
        } catch (IOException e) {
            SOCKET_LOG.debug("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            SOCKET_LOG.debug(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Register a new listener of type TMSPAPI or supertype
     * @param listener
     */
    public void registerListener(Object listener) {
        _listeners.add(listener);
    }

    /**
     * Register a new listener of type TMSPAPI or supertype
     * @param listener
     */
    public void removeListener(Object listener) {
        _listeners.remove(listener);
    }

    /**
     * Returns the <b>first</b> listener for specified class or a
     * {@link DefaultFallbackListener} if nothing was found
     * 
     * @param klass
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getListenerForType(Class<T> klass) {
        for (Object object : _listeners) {
            List<Class<?>> clssss = Arrays.asList(object.getClass().getInterfaces());
            if (clssss.contains(klass) || clssss.contains(TMSPAPI.class)) {
                return (T) object;
            }
        }
        return (T) DefaultFallbackListener.instance;
    }

    public void printByteArray(byte[] bArr) {
        for (byte b : bArr) {
            System.out.format("0x%x ", b);
        }
        System.out.print("\n");
    }

}
