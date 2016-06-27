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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
import com.google.protobuf.GeneratedMessage;

/**
 * The TSocket acts as the main socket connection to the Tendermint-Node
 * 
 * 
 * @author srmo, wolfposd
 */
public class TSocket {

    public static final int DEFAULT_LISTEN_SOCKET_PORT = 46658;
    private final static Logger SOCKET_LOG = LoggerFactory.getLogger(TSocket.class);
    private final static Logger HANDLER_LOG = LoggerFactory.getLogger(SocketHandler.class);

    private final static AtomicInteger runningThreads = new AtomicInteger();
    private List<Object> _listeners = new ArrayList<>();

    class SocketHandler implements Runnable {
        private final int threadNumber;
        private final Socket socket;
        private BufferedInputStream inputStream;
        private BufferedOutputStream outputStream;

        public SocketHandler(Socket socket) {
            this.socket = socket;
            this.threadNumber = runningThreads.getAndIncrement();
        }

        @Override
        public void run() {
            HANDLER_LOG.info("Starting ThreadNo: " + threadNumber);
            HANDLER_LOG.info("accepting new client");
            try {
                inputStream = new BufferedInputStream(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());
                while (true) {
                    HANDLER_LOG.info("start reading");
                    int nRead;
                    byte[] varintLengthByte = new byte[1];
                    // requires check for negative numbers. see src/github.com/tendermint/go-wire/int.go:306
                    nRead = inputStream.read(varintLengthByte, 0, 1);
                    if (nRead < 0) {
                        HANDLER_LOG.info("EOF encountered. Closing socket and ending thread");
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                        break;
                    }
                    int varintLenght = varintLengthByte[0];
                    handleMessage(varintLenght);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            HANDLER_LOG.info("Stopping ThreadNo " + threadNumber);
            runningThreads.getAndDecrement();
        }

        public void handleMessage(int varintLenght) throws IOException {

            if (varintLenght > 8) {
                throw new IllegalStateException("Varint bigger than 8 bytes are not supported!");
            }

            byte[] varintBytes = new byte[varintLenght];
            // assume the maximum number of bytes for a
            // long which makes using a bytebuffer easier
            int nRead = inputStream.read(varintBytes, 0, varintLenght);

            long msgSizeAsLong = new BigInteger(varintBytes).longValue();
            if (msgSizeAsLong > Integer.MAX_VALUE) {
                throw new IllegalStateException("Sorry! Currently only messages with maxLenght=INTEGER.MAX_VALUE supported");
            }
            int msgSizeAsInt = (int) msgSizeAsLong;
            HANDLER_LOG.info("Assuming message length: " + msgSizeAsInt);

            // ok, this is stupid and ugly. what about gargantuan messages?
            byte[] message = new byte[msgSizeAsInt];
            inputStream.read(message, 0, msgSizeAsInt);
            final Types.Request request = Types.Request.parseFrom(message);
            switch (request.getValueCase()) {
                case ECHO: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.ECHO);
                    RequestEcho req = RequestEcho.parseFrom(message);
                    ResponseEcho response = getListenerForType(IEcho.class).requestEcho(req);
                    if (response != null)
                        writeMessage(Response.newBuilder().setEcho(response).build());
                    break;
                }
                case FLUSH: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.FLUSH);
                    RequestFlush req = RequestFlush.parseFrom(message);
                    ResponseFlush response = getListenerForType(IFlush.class).requestFlush(req);
                    writeMessage(Response.newBuilder().setFlush(response).build());
                    break;
                }
                case INFO: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.INFO);
                    RequestInfo req = RequestInfo.parseFrom(message);
                    ResponseInfo response = getListenerForType(IInfo.class).requestInfo(req);
                    if (response != null)
                        writeMessage(Response.newBuilder().setInfo(response).build());
                    break;
                }
                case SET_OPTION: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.SET_OPTION);
                    RequestSetOption req = RequestSetOption.parseFrom(message);
                    ResponseSetOption response = getListenerForType(ISetOption.class).requestSetOption(req);
                    if (response != null)
                        writeMessage(Response.newBuilder().setSetOption(response).build());
                    break;
                }
                case APPEND_TX: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.APPEND_TX);
                    RequestAppendTx req = RequestAppendTx.parseFrom(message);
                    ResponseAppendTx response = getListenerForType(IAppendTx.class).receivedAppendTx(req);
                    if (response != null)
                        writeMessage(Response.newBuilder().setAppendTx(response).build());
                    break;
                }
                case CHECK_TX: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.CHECK_TX);
                    RequestCheckTx req = RequestCheckTx.parseFrom(message);
                    ResponseCheckTx response = getListenerForType(ICheckTx.class).requestCheckTx(req);
                    writeMessage(Response.newBuilder().setCheckTx(response).build());
                    break;
                }
                case COMMIT: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.COMMIT);
                    RequestCommit req = RequestCommit.parseFrom(message);
                    ResponseCommit response = getListenerForType(ICommit.class).requestCommit(req);
                    writeMessage(Response.newBuilder().setCommit(response).build());
                    break;
                }
                case QUERY: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.QUERY);
                    RequestQuery req = RequestQuery.parseFrom(message);
                    ResponseQuery response = getListenerForType(IQuery.class).requestQuery(req);
                    writeMessage(Response.newBuilder().setQuery(response).build());
                    break;
                }
                case INIT_CHAIN: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.INIT_CHAIN);
                    RequestInitChain req = RequestInitChain.parseFrom(message);
                    ResponseInitChain response = getListenerForType(IInitChain.class).requestInitChain(req);
                    writeMessage(Response.newBuilder().setInitChain(response).build());
                    break;
                }
                case BEGIN_BLOCK: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.BEGIN_BLOCK);
                    RequestBeginBlock req = RequestBeginBlock.parseFrom(message);
                    ResponseBeginBlock response = getListenerForType(IBeginBlock.class).requestBeginBlock(req);
                    writeMessage(Response.newBuilder().setBeginBlock(response).build());
                    break;
                }
                case END_BLOCK: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.END_BLOCK);
                    RequestEndBlock req = RequestEndBlock.parseFrom(message);
                    ResponseEndBlock response = getListenerForType(IEndBlock.class).requestEndBlock(req);
                    writeMessage(Response.newBuilder().setEndBlock(response).build());
                    break;
                }
                case VALUE_NOT_SET: {
                    HANDLER_LOG.info("Received " + Types.Request.ValueCase.VALUE_NOT_SET);
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
                HANDLER_LOG.info("writing message " + message.getAllFields().keySet());
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
        SOCKET_LOG.info("starting serversocket");

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new SocketHandler(clientSocket)).start();
                SOCKET_LOG.info("Started thread for sockethandling...");
            }
        } catch (IOException e) {
            SOCKET_LOG.info("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            SOCKET_LOG.info(e.getMessage());
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
        List<T> list = (List<T>) _listeners.stream().filter(t -> {
            List<Class<?>> clssss = Arrays.asList(t.getClass().getInterfaces());
            return clssss.contains(klass) || clssss.contains(TMSPAPI.class);
        }).collect(Collectors.toList());
        if (list.size() > 0)
            return list.get(0);
        else
            return (T) new DefaultFallbackListener();
    }
}
