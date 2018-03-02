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
package com.github.jtendermint.jabci;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.github.jtendermint.jabci.api.CodeType;
import com.github.jtendermint.jabci.api.ICheckTx;
import com.github.jtendermint.jabci.api.ICommit;
import com.github.jtendermint.jabci.api.IDeliverTx;
import com.github.jtendermint.jabci.api.IQuery;
import com.github.jtendermint.jabci.socket.TSocket;
import com.github.jtendermint.jabci.types.RequestCheckTx;
import com.github.jtendermint.jabci.types.RequestCommit;
import com.github.jtendermint.jabci.types.RequestDeliverTx;
import com.github.jtendermint.jabci.types.RequestQuery;
import com.github.jtendermint.jabci.types.ResponseCheckTx;
import com.github.jtendermint.jabci.types.ResponseCommit;
import com.github.jtendermint.jabci.types.ResponseDeliverTx;
import com.github.jtendermint.jabci.types.ResponseQuery;
import com.google.protobuf.ByteString;

/**
 * Implements a sample counter app. every tx-data must be bigger than the current amount of tx
 *
 * @author wolfposd
 */
public final class JavaCounter implements IDeliverTx, ICheckTx, ICommit, IQuery {

    private int hashCount = 0;
    private int txCount = 0;
    private TSocket socket;

    public JavaCounter() throws InterruptedException {
        System.out.println("starting counter");
        socket = new TSocket();

        socket.registerListener(this);

        // TODO this is wrong once you subclass this JavaCounter
        /*
         * The constructor starts a thread. This is likely to be wrong if the class is ever extended/subclassed, since the thread will be
         * started before the subclass constructor is started.
         */
        Thread t = new Thread(() -> socket.start(46658));
        t.setName("Java Counter Main Thread");
        t.start();
        while (true) {
            Thread.sleep(1000L);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new JavaCounter();
    }

    @Override
    public ResponseDeliverTx receivedDeliverTx(RequestDeliverTx req) {
        ByteString tx = req.getTx();
        System.out.println("got deliver tx, with" + TSocket.byteArrayToString(tx.toByteArray()));

        if (tx.size() == 0) {
            System.out.println("returning BAD, transaction is empty");
            return ResponseDeliverTx.newBuilder().setCode(CodeType.BadNonce).setLog("transaction is empty").build();
        } else if (tx.size() <= 4) {
            int x = new BigInteger(1, tx.toByteArray()).intValueExact();
            // this is an int now, if not throws an ArithmeticException
            // but we dont actually care what it is.

            if (x != txCount) {
                String message = "Invalid Nonce. Expected " + txCount + ", got " + x;
                System.out.println("returning BAD, " + message);
                return ResponseDeliverTx.newBuilder().setCode(CodeType.BadNonce).setLog(message).build();
            }

        } else {
            System.out.println("returning BAD, got a bad value");
            return ResponseDeliverTx.newBuilder().setCode(CodeType.BadNonce).setLog("got a bad value").build();
        }

        txCount += 1;
        System.out.println("TX Count is now: " + txCount);
        return ResponseDeliverTx.newBuilder().setCode(CodeType.OK).build();
    }

    @Override
    public ResponseCheckTx requestCheckTx(RequestCheckTx req) {
        System.out.println("got check tx");

        ByteString tx = req.getTx();
        if (tx.size() <= 4) {
            // hopefully parsable integer
            int txCheck = new BigInteger(1, tx.toByteArray()).intValueExact();

            System.out.println("tx value is: " + txCheck);

            if (txCheck < txCount) {
                String err = "Invalid nonce. Expected >= " + txCount + ", got " + txCheck;
                System.out.println(err);
                return ResponseCheckTx.newBuilder().setCode(CodeType.BadNonce).setLog(err).build();
            }
        }

        System.out.println("SENDING OK");
        return ResponseCheckTx.newBuilder().setCode(CodeType.OK).build();
    }

    @Override
    public ResponseCommit requestCommit(RequestCommit requestCommit) {
        hashCount += 1;

        if (txCount == 0) {
            return ResponseCommit.newBuilder().build();
        } else {
            ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES);
            buf.putInt(txCount);
            buf.rewind();
            return ResponseCommit.newBuilder().setData(ByteString.copyFrom(buf)).build();
        }
    }

    @Override
    public ResponseQuery requestQuery(RequestQuery req) {
        final String query = new String(req.getData().toByteArray(), Charset.forName("UTF-8"));
        switch (query) {
            case "hash":
                return ResponseQuery.newBuilder().setCode(CodeType.OK)
                    .setValue(ByteString.copyFrom(("" + hashCount).getBytes(Charset.forName("UTF-8")))).build();
            case "tx":
                return ResponseQuery.newBuilder().setCode(CodeType.OK)
                    .setValue(ByteString.copyFrom(("" + txCount).getBytes(Charset.forName("UTF-8")))).build();
            default:
                return ResponseQuery.newBuilder().setCode(CodeType.BadNonce).setLog("Invalid query path. Expected hash or tx, got " + query)
                    .build();
        }
    }
}
