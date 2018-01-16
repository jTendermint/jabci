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
package com.github.jtendermint.jabci.socket.async;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.jtendermint.jabci.protobuf.types.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jtendermint.jabci.socket.ASocket;

import com.google.protobuf.GeneratedMessageV3;

public class AsyncServer extends ASocket {

    private final static Logger LOG = LoggerFactory.getLogger(AsyncServer.class);

    private InetSocketAddress sockAddr;

    public AsyncServer() throws IOException {
        this(DEFAULT_LISTEN_SOCKET_PORT);
    }

    public AsyncServer(int bindPort) throws IOException {
        sockAddr = new InetSocketAddress("localhost", bindPort);
    }

    public void start() {
        try {
            acceptConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnection() throws IOException {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(sockAddr);
        Attachment attachment = new Attachment(server, null, ByteBuffer.allocate(4096), true);

        server.accept(attachment, new ConnectionHandler());
    }

    class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {

        @Override
        public void completed(AsynchronousSocketChannel client, Attachment att) {

            try {
                LOG.debug("accepting connections from : {}", client.getRemoteAddress());
            } catch (IOException e) {
                LOG.debug("accepting connections from : {}", client);
            }
            att.server.accept(att, new ConnectionHandler());

            Attachment newAttachment = new Attachment(att.server, client, ByteBuffer.allocate(1), att.isRead);

            client.read(newAttachment.buffer, newAttachment, new FirstRead());
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            LOG.error("Failed to connect", exc);
        }
    }

    class FirstRead implements CompletionHandler<Integer, Attachment> {

        @Override
        public void completed(Integer bytes, Attachment attachment) {
            AsynchronousSocketChannel client = attachment.client;
            final ByteBuffer buf1 = attachment.buffer;

            if (bytes == 1) {
                // read 1. byte
                final int varintlength = buf1.get(0);
                final ByteBuffer buf2 = ByteBuffer.allocate(varintlength);

                client.read(buf2, buf2, ReadCompletion.lambda((bytes2, attmn2) -> {

                    // read bytes that define message length
                    long messageLengthLong = new BigInteger(buf2.array()).longValue();
                    final ByteBuffer buf3 = ByteBuffer.allocate((int) messageLengthLong);

                    client.read(buf3, buf3, ReadCompletion.lambda((bytes3, attmn3) -> {
                        try {
                            // read the actual message
                            final Request request = Request.parseFrom(buf3.array());
                            final GeneratedMessageV3 msg = handleRequest(request);
                            final ByteBuffer output = responseToByteBuffer(msg);
                            client.write(output);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        client.read(ByteBuffer.allocate(1), attachment, FirstRead.this);
                    }));

                }));

            }
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            LOG.error("Failed to read", exc);
        }

    }
}
