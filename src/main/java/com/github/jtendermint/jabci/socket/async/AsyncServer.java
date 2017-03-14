package com.github.jtendermint.jabci.socket.async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

import com.github.jtendermint.jabci.socket.ASocket;
import com.github.jtendermint.jabci.types.Types.Request;
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

            client.supportedOptions().forEach(e -> {
                try {
                    System.out.println(e.name() + ", " + e.type() + ", " + client.getOption(e));
                } catch (IOException e1) {
                }

            });
            

            Attachment newAttachment = new Attachment(att.server, client, ByteBuffer.allocate(4096), att.isRead);
            System.out.println(newAttachment);
            client.read(newAttachment.buffer, newAttachment, new ReadWriteHandler());
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
            System.err.println("Failed to accept a connection from: " + attachment);
            exc.printStackTrace();
        }

    }

    class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
        @Override
        public void completed(Integer result, Attachment attachment) {
            if (result == -1) {
                try {
                    attachment.client.close();
                    System.out.format("Stopped listening to the client %s%n", attachment.clientAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            if (attachment.isRead) {
                attachment.buffer.flip();

                try {
                    System.out.format("Client at  %s  says: %s%n", attachment.client.getRemoteAddress(),
                            byteArrayToString(attachment.buffer.array()));
                } catch (IOException e1) {
                }

                // try {
                // Request request = readMessage(attachment.buffer);
                // if (request != null) {
                // GeneratedMessageV3 answerMessage = handleRequest(request);
                // if (answerMessage != null) {
                // for (ByteBuffer buff : responseBytes(answerMessage)) {
                // System.out.println("writing: " + ASocket.byteArrayToString(buff.array()));
                // attachment.client.write(buff, attachment, new EmptyHandler());
                // }
                // }
                // }
                // } catch (Exception e) {
                // e.printStackTrace();
                // }

                 attachment.buffer = outputForInput(attachment.buffer);
                 System.out.println("writing answer: " + byteArrayToString(attachment.buffer.array()));
                 attachment.isRead = false; // It is a write
                 System.out.println("writing attachment " + attachment);
                 attachment.client.write(attachment.buffer, attachment, new EmptyHandler());

                 
                 
                System.out.println("resetting buffers for read");
                attachment.isRead = true;
                attachment.buffer = ByteBuffer.allocate(4096);
                attachment.client.read(attachment.buffer, attachment, this);
            } else {
                System.out.println("got a write incoming");
            }
        }

        @Override
        public void failed(Throwable e, Attachment attach) {
            e.printStackTrace();
        }

    }

    class EmptyHandler implements CompletionHandler<Integer, Attachment> {

        @Override
        public void completed(Integer result, Attachment attachment) {
            System.out.println("wrote Something: " + attachment);
        }

        @Override
        public void failed(Throwable exc, Attachment attachment) {
        }

    }

}
