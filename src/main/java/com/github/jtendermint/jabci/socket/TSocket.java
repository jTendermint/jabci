/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 - 2018
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.CodedOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jtendermint.jabci.socket.ExceptionListener.Event;
import com.github.jtendermint.jabci.types.Request;
import com.github.jtendermint.jabci.types.ResponseException;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * The TSocket acts as the main socket connection to the Tendermint-Node
 *
 * @author srmo, wolfposd
 */
public class TSocket extends ASocket {

    public static final int DEFAULT_LISTEN_SOCKET_PORT = 26658;
    public static final String INFO_SOCKET = "-Info";
    public static final String MEMPOOL_SOCKET = "-MemPool";
    public static final String CONSENSUS_SOCKET = "-Consensus";
    public static final int DEFAULT_LISTEN_SOCKET_TIMEOUT = 1000;
    
    private static final Logger TSOCKET_LOG = LoggerFactory.getLogger(TSocket.class);
    private static final Logger HANDLER_LOG = LoggerFactory.getLogger(SocketHandler.class);
    
    private final Set<SocketHandler> runningThreads = Collections.newSetFromMap(new ConcurrentHashMap<SocketHandler, Boolean>());
    private long lastConnectedSocketTime = -1;
    private boolean continueRunning = true;
    private ExceptionListener exceptionListener;
    private ConnectionListener connectionListener;
    private DisconnectListener disconnectListener;
    
    public TSocket() {
        this((socket, cause, exception) -> {
        }, (name, count) -> {
        }, (name, count) -> {
        });
    }

    /**
     * Creates this TSocket with the ability to register for exceptions that occur
     * and listen to new socket connections
     * 
     * @param exceptionListener
     *            listens to exceptions
     * @param connectionListener
     *            listens to new connections
     * @param disconnectListener
     *            listens to disconnects
     */
    public TSocket(ExceptionListener exceptionListener, ConnectionListener connectionListener, DisconnectListener disconnectListener) {
        this.connectionListener = Objects.requireNonNull(connectionListener, "requires a connectionListener");
        this.exceptionListener = Objects.requireNonNull(exceptionListener, "requires an exceptionListener");
        this.disconnectListener = Objects.requireNonNull(disconnectListener, "requires a disconnectListener");
    }

    /**
     * Start listening on the default ABCI port 46658
     */
    public void start() {
        this.start(DEFAULT_LISTEN_SOCKET_PORT, DEFAULT_LISTEN_SOCKET_TIMEOUT);
    }

    /**
     * Start listening on the specified port
     *
     * @param portNumber tendermint abci port
     */
    public void start(final int portNumber) {
        this.start(portNumber, DEFAULT_LISTEN_SOCKET_TIMEOUT);
    }

    /**
     * Start listening on the specified port
     *
     * @param portNumber
     * @param socketTimeout
     */
    public void start(final int portNumber, final int socketTimeout) {
        TSOCKET_LOG.debug("starting serversocket");
        continueRunning = true;
        int socketcount = 0;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            serverSocket.setSoTimeout(socketTimeout);
            while (continueRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    lastConnectedSocketTime = System.currentTimeMillis();
                    String socketName = socketNameForCount(++socketcount);
                    TSOCKET_LOG.debug("starting socket with: {}", socketName);
                    SocketHandler t = (socketName != null) ? new SocketHandler(clientSocket, socketName) : new SocketHandler(clientSocket);
                    t.start();
                    runningThreads.add(t);
                    TSOCKET_LOG.debug("Started thread for sockethandling...");
                    connectionListener.connected(Optional.ofNullable(socketName), runningThreads.size());
                } catch (SocketTimeoutException ste) {
                    // this is triggered by accept()
                    exceptionListener.notifyExceptionOccured(Optional.ofNullable(socketNameForCount(socketcount)), Event.Socket_accept, ste);
                }
            }
            TSOCKET_LOG.debug("TSocket Stopped Running");
        } catch (IOException e) {
            exceptionListener.notifyExceptionOccured(Optional.ofNullable(socketNameForCount(socketcount)), Event.ServerSocket, e);
            TSOCKET_LOG.error("Exception caught when trying to listen on port " + portNumber + " or listening for a connection", e);
        }
        TSOCKET_LOG.debug("Exited main-run-while loop");
    }

    private String socketNameForCount(int c) {
        switch (c) {
            case 1:
                return INFO_SOCKET;
            case 2:
                return MEMPOOL_SOCKET;
            case 3:
                return CONSENSUS_SOCKET;
            default:
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
            exceptionListener.notifyExceptionOccured(Optional.empty(), Event.Thread_sleep, e);
        }

        // close socket connections
        runningThreads.forEach(t -> {
            t.closeConnections();
        });

        runningThreads.clear();
        Thread.currentThread().interrupt();
        TSOCKET_LOG.debug("Finished calling stop on members.");
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
        private CodedOutputStream outputStream;
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
            this.setName("SocketThread" + name);
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
                exceptionListener.notifyExceptionOccured(Optional.ofNullable(this.getName()), Event.SocketHandler_closeConnections, e);
            }
            runningThreads.remove(this);
        }

        @Override
        public void run() {

            HANDLER_LOG.debug("Starting ThreadNo: " + getName());
            HANDLER_LOG.debug("accepting new client");
            try {
                inputStream = CodedInputStream.newInstance(socket.getInputStream());
                outputStream = CodedOutputStream.newInstance(socket.getOutputStream());
                while (!isInterrupted() && !socket.isClosed()) {
                    HANDLER_LOG.debug("start reading");

                    // Size counter is used to enforce a size limit per message (see CodedInputStream.setSizeLimit()).
                    // We need to reset it before reading the next message:
                    inputStream.resetSizeCounter();

                    // HEADER: first byte(s) is length of the following message;
                    // it is protobuf encoded as a varint-uint64
                    try {
                        int varintLengthByte = (int) CodedInputStream.decodeZigZag64(inputStream.readUInt64());

                        int oldLimit = inputStream.pushLimit(varintLengthByte);
                        final Request request = Request.parseFrom(inputStream);
                        inputStream.popLimit(oldLimit);

                        if (!nameSet) {
                            determineSocketNameAndUpdate(request.getValueCase());
                        }

                        // Process the request that was just read:
                        try {
                            GeneratedMessageV3 response = handleRequest(request);
                            writeMessage(response);
                        } catch (Exception e) {
                            exceptionListener.notifyExceptionOccured(Optional.ofNullable(this.getName()), Event.SocketHandler_handleRequest, e);
                            ResponseException exception = ResponseException.newBuilder().setError(e.getMessage()).build();
                            writeMessage(exception);
                        }

                    } catch (InvalidProtocolBufferException ipbe) {
                        this.interrupt();
                        this.socket.close();
                        exceptionListener.notifyExceptionOccured(Optional.ofNullable(this.getName()), Event.SocketHandler_readFromStream, ipbe);
                    }
                }
            } catch (IOException e) {
                exceptionListener.notifyExceptionOccured(Optional.ofNullable(this.getName()), Event.SocketHandler_run, e);
                if (!isInterrupted()) {
                    HANDLER_LOG.error("Error with " + this.getName(), e);
                    HANDLER_LOG.info("Note: If \"the input ended unexpectedly\" it could mean: \n - tendermint was shut down\n - the protobuf file is not up to date.");
                }
            }
            HANDLER_LOG.debug("Stopping Thread " + this.getName());
            Thread.currentThread().interrupt();
            runningThreads.remove(this);
            disconnectListener.disconnected(Optional.ofNullable(this.getName()), runningThreads.size());
        }

        private void determineSocketNameAndUpdate(Request.ValueCase valuecase) {
            switch (valuecase) {
                case FLUSH:
                    break;
                case INFO:
                    this.updateName(INFO_SOCKET);
                    nameSet = true;
                    break;
                case CHECK_TX:
                    this.updateName(MEMPOOL_SOCKET);
                    nameSet = true;
                    break;
                default:
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
                long length = message.getSerializedSize();

                // HEADER: first byte(s) is varint encoded length of the message
                // also, see writeMessage for the other way round
                outputStream.writeUInt64NoTag(CodedOutputStream.encodeZigZag64(length));
                message.writeTo(outputStream);
                outputStream.flush();
            }
        }
    }

}
