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

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Objects;

public class Attachment {
    public final AsynchronousServerSocketChannel server;
    public final AsynchronousSocketChannel client;
    public ByteBuffer buffer;
    public boolean isRead;

    public Attachment(AsynchronousServerSocketChannel server, AsynchronousSocketChannel client, ByteBuffer buffer, boolean isRead) {

        this.server = server;
        this.client = client;
        this.buffer = buffer;
        this.isRead = isRead;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attachment) {
            Attachment other = (Attachment) obj;
            return Objects.equals(server, other.server) && Objects.equals(client, other.client);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return client.hashCode();
    }

    public Attachment clone(ByteBuffer buffer) {
        return new Attachment(this.server, this.client, buffer, this.isRead);
    }

    @Override
    public String toString() {

        try {
            return "Attachment [server=" + server.getLocalAddress() + ", client=" + client.getLocalAddress() + "/"
                    + client.getRemoteAddress() + ", buffer=" + buffer + ", isRead=" + isRead + "]";
        } catch (Exception e) {
            return "Attachment [server=" + server + ", client=" + client + ", buffer=" + buffer + ", isRead=" + isRead + "]";
        }
    }

}