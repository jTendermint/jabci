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
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jtendermint.jabci.types.Types;
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
    private final static Logger SOCKET_LOG = LoggerFactory.getLogger(TSocket.class);
    private final static Logger HANDLER_LOG = LoggerFactory.getLogger(SocketHandler.class);

    private final static AtomicInteger runningThreads = new AtomicInteger();

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
                    if (messageLengthLong > Integer.MAX_VALUE) {
                        throw new IllegalStateException("Message lengths of more than Integer.MAX_VALUE are not supported.");
                    }
                    int messageLength = (int) messageLengthLong;
                    HANDLER_LOG.debug("Assuming message length: " + messageLength);

                    // PAYLOAD: limit CodedInputStream to messageLength bytes and parse Request using Protobuf:
                    int oldLimit = inputStream.pushLimit(messageLength);
                    final Types.Request request = Types.Request.parseFrom(inputStream);
                    inputStream.popLimit(oldLimit);

                    // Process the request that was just read:
                    GeneratedMessageV3 response = handleRequest(request);
                    writeMessage(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            HANDLER_LOG.debug("Stopping ThreadNo " + threadNumber);
            runningThreads.getAndDecrement();
        }


        /**
         * Writes a {@link GeneratedMessageV3} to the socket output stream
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
}
