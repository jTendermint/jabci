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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jtendermint.jabci.types.Types;
import com.github.jtendermint.jabci.types.Types.Request;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessageV3;

/**
 * The TSocket acts as the main socket connection to the Tendermint-Node
 * 
 * 
 * @author srmo, wolfposd
 */
@SuppressWarnings("synthetic-access")
public class TSocket extends ASocket {

    public static final int DEFAULT_LISTEN_SOCKET_PORT = 46658;
    private static final Logger TSOCKET_LOG = LoggerFactory.getLogger(TSocket.class);
    private static final Logger HANDLER_LOG = LoggerFactory.getLogger(SocketHandler.class);

    public static final String INFO_SOCKET = "-Info";
    public static final String MEMPOOL_SOCKET = "-MemPool";
    public static final String CONSENSUS_SOCKET = "-Consensus";

    private final HashSet<SocketHandler> runningThreads = new HashSet<>();
    
    private long lastConnectedSocketTime = -1;

    private boolean continueRunning = true;

    /**
     * Start listening on the default ABCI port 46658
     */
    public void start() {
        this.start(DEFAULT_LISTEN_SOCKET_PORT);
    }

    /**
     * Start listening on the specified port
     * 
     * @param portNumber
     */
    public void start(int portNumber) {
        TSOCKET_LOG.debug("starting serversocket");
        continueRunning = true;
        int socketcount = 0;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (continueRunning) {
                Socket clientSocket = serverSocket.accept();
                lastConnectedSocketTime = System.currentTimeMillis();
                String socketName = socketNameForCount(++socketcount);
                TSOCKET_LOG.debug("starting socket with: {}", socketName);
                SocketHandler t = (socketName != null) ? new SocketHandler(clientSocket, socketName) : new SocketHandler(clientSocket);
                t.start();
                runningThreads.add(t);
                TSOCKET_LOG.debug("Started thread for sockethandling...");
            }
            TSOCKET_LOG.debug("TSocket Stopped Running");
        } catch (IOException e) {
            TSOCKET_LOG.error("Exception caught when trying to listen on port " + portNumber + " or listening for a connection", e);
        }
    }

    private String socketNameForCount(int c) {
        switch (c) {
            case 1 :
                return INFO_SOCKET;
            case 2 :
                return MEMPOOL_SOCKET;
            case 3 :
                return CONSENSUS_SOCKET;
            default :
                return null;
        }
    }

    public void stop() {
        continueRunning = false;
        runningThreads.forEach(t -> t.interrupt());

        try {
            // wait two seconds to finalize last messages on streams
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        // close socket connections
        runningThreads.forEach(t -> {
            t.closeConnections();
        });

        runningThreads.clear();
        Thread.currentThread().interrupt();
    }
    /**
     * @return the amount of connected sockets, this should usually be 3: info,mempool and consensus
     */
    public int sizeOfConnectedABCISockets() {
        return runningThreads.size();
    }
    
    public long getLastConnectedTime() {
        return lastConnectedSocketTime;
    }
    
    class SocketHandler extends Thread {

        private final Socket socket;
        private CodedInputStream inputStream;
        private BufferedOutputStream outputStream;
        private boolean nameSet = false;

        public SocketHandler(Socket socket) {
            this.setDaemon(true);
            this.socket = socket;
            this.updateName("" + socket.getPort());
        }
        public SocketHandler(Socket socket, String name) {
            this.setDaemon(true);
            this.socket = socket;
            nameSet = true;
            this.updateName(name);
        }

        public void updateName(String name) {
            this.setName("SocketThread " + name);
        }

        @Override
        public void interrupt() {
            HANDLER_LOG.debug("{} being interrupted", this.getName());
            super.interrupt();
        }

        private void closeConnections() {
            try {
                if (socket != null) {
                    HANDLER_LOG.debug("{} outputStream close", getName());
                    if (!socket.isClosed())
                        socket.getOutputStream().close();

                    HANDLER_LOG.debug("{} inputStream close", getName());
                    if (!socket.isClosed())
                        socket.getInputStream().close();

                    HANDLER_LOG.debug("{} socket close", getName());
                    socket.close();
                }
            } catch (Exception e) {
            }
            runningThreads.remove(this);
        }

        @Override
        public void run() {
            HANDLER_LOG.debug("Starting ThreadNo: " + getName());
            HANDLER_LOG.debug("accepting new client");
            try {
                inputStream = CodedInputStream.newInstance(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());
                while (!isInterrupted() && !socket.isClosed()) {
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
                    if (messageLengthLong > Integer.MAX_VALUE) {
                        throw new IllegalStateException("Message lengths of more than Integer.MAX_VALUE are not supported.");
                    }
                    int messageLength = (int) messageLengthLong;
                    HANDLER_LOG.debug("Assuming message length: " + messageLength);

                    // PAYLOAD: limit CodedInputStream to messageLength bytes and parse Request using Protobuf:
                    int oldLimit = inputStream.pushLimit(messageLength);
                    final Types.Request request = Types.Request.parseFrom(inputStream);
                    inputStream.popLimit(oldLimit);

                    if (!nameSet) {
                        determineSocketNameAndUpdate(request.getValueCase());
                    }

                    // Process the request that was just read:
                    GeneratedMessageV3 response = handleRequest(request);
                    writeMessage(response);
                }
            } catch (IOException e) {
                if (!isInterrupted()) {
                    HANDLER_LOG.error("Error with " + this.getName(), e);
                    HANDLER_LOG.info("Note: If \"the input ended unexpectedly\" it could mean the protobuf file is not up to date.");
                }
            }
            HANDLER_LOG.debug("Stopping Thread " + this.getName());
            Thread.currentThread().interrupt();
            runningThreads.remove(this);
        }

        private void determineSocketNameAndUpdate(Request.ValueCase valuecase) {
            switch (valuecase) {
                case FLUSH :
                    break;
                case INFO :
                    this.updateName(INFO_SOCKET);
                    nameSet = true;
                    break;
                case CHECK_TX :
                    this.updateName(MEMPOOL_SOCKET);
                    nameSet = true;
                    break;
                default :
                    this.updateName(CONSENSUS_SOCKET);
                    nameSet = true;
                    break;
            }
        }

        /**
         * Writes a {@link GeneratedMessageV3} to the socket output stream
         * 
         * @param message
         * @throws IOException
         */
        public void writeMessage(GeneratedMessageV3 message) throws IOException {
            if (message != null) {
                HANDLER_LOG.debug("writing message " + message.getAllFields().keySet());
                writeMessage(message.toByteArray());
            }
        }

        /**
         * writes raw-bytes to the socket output stream
         * 
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
}
