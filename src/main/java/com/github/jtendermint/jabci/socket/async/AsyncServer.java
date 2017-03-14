package com.github.jtendermint.jabci.socket.async;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.jtendermint.jabci.socket.ASocket;
import com.github.jtendermint.jabci.types.Types;
import com.google.protobuf.GeneratedMessageV3;

public class AsyncServer extends ASocket {

    private InetSocketAddress sockAddr;

    public static void main(String[] args) {

        newConnection();

        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }

    }

    public static void newConnection() {
        new Thread(() -> {
            try {
                System.out.println("starting server");
                AsyncServer server = new AsyncServer("127.0.0.1", DEFAULT_LISTEN_SOCKET_PORT);
                server.acceptConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public AsyncServer(String bindIpAddress, int bindPort) throws IOException {
        sockAddr = new InetSocketAddress(bindIpAddress, bindPort);
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
                System.out.println("accepting connections from : " + client.getRemoteAddress());
            } catch (IOException e) {
                System.out.println("accepting connections from : " + client);
            }
            att.server.accept(att, new ConnectionHandler());

            Attachment newAttachment = new Attachment(att.server, client, ByteBuffer.allocate(1), att.isRead);
            System.out.println(newAttachment);

            client.read(newAttachment.buffer, newAttachment, new FirstRead());
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            System.err.println("Failed to accept a connection from: " + attachment);
            exc.printStackTrace();
        }
    }

    class FirstRead implements CompletionHandler<Integer, Attachment> {

        @Override
        public void completed(Integer bytes, Attachment attachment) {
            AsynchronousSocketChannel client = attachment.client;
            final ByteBuffer buf1 = attachment.buffer;

            if (bytes == 1) {
                System.out.println("reading 1 byte");
                final int varintlength = buf1.get(0);
                final ByteBuffer buf2 = ByteBuffer.allocate(varintlength);

                client.read(buf2, buf2, ReadCompletion.make((bytes2, attmn2) -> {

                    System.out.println("reading next bytes");
                    long messageLengthLong = new BigInteger(buf2.array()).longValue();

                    final ByteBuffer buf3 = ByteBuffer.allocate((int) messageLengthLong);
                    client.read(buf3, buf3, ReadCompletion.make((bytes3, attmn3) -> {
                        System.out.println("reading last bytes");
                        System.out.println(byteArrayToString(buf3.array()));

                        try {

                            final Types.Request request = Types.Request.parseFrom(buf3.array());
                            GeneratedMessageV3 msg = handleRequest(request);
                            ByteBuffer output = responseToByteBuffer(msg);
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

        }

    }
}
