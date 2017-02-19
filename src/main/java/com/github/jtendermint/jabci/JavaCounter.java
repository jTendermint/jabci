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

import com.github.jtendermint.jabci.api.ICheckTx;
import com.github.jtendermint.jabci.api.ICommit;
import com.github.jtendermint.jabci.api.IDeliverTx;
import com.github.jtendermint.jabci.socket.TSocket;
import com.github.jtendermint.jabci.types.Types.CodeType;
import com.github.jtendermint.jabci.types.Types.RequestCheckTx;
import com.github.jtendermint.jabci.types.Types.RequestCommit;
import com.github.jtendermint.jabci.types.Types.RequestDeliverTx;
import com.github.jtendermint.jabci.types.Types.ResponseCheckTx;
import com.github.jtendermint.jabci.types.Types.ResponseCommit;
import com.github.jtendermint.jabci.types.Types.ResponseDeliverTx;
import com.google.protobuf.ByteString;

/**
 * 
 * Implements a sample counter app. every tx-data must be bigger than the current amount of tx
 * 
 * @author wolfposd
 */
public class JavaCounter implements IDeliverTx, ICheckTx, ICommit {

    public static void main(String[] args) throws InterruptedException {
        new JavaCounter();
    }

    private int hashCount = 0;
    private int txCount = 0;
    private TSocket socket;

    public JavaCounter() throws InterruptedException {
        System.out.println("starting counter");
        socket = new TSocket();

        socket.registerListener(this);

        new Thread(socket::start).start();
        while (true) {
            Thread.sleep(1000L);
        }
    }

    @Override
    public ResponseDeliverTx receivedDeliverTx(RequestDeliverTx req) {
        System.out.println("got deliver tx");
        ByteString tx = req.getTx();

        socket.printByteArray(tx.toByteArray());

        if (tx.size() == 0) {
            return ResponseDeliverTx.newBuilder().setCode(CodeType.BadNonce).setLog("transaction is empty").build();
        } else if (tx.size() <= 4) {
            int x = new BigInteger(1, tx.toByteArray()).intValueExact();
            // this is an int now, if not throws an ArithmeticException
            // but we dont actually care what it is. 
        } else {
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
            if (txCheck < txCount) {
                System.out.println("txcheck is smaller than txCount, got " + txCheck + " and " + txCount);
                return ResponseCheckTx.newBuilder().setCode(CodeType.BadNonce).setLog("tx-value is smaller than tx-count").build();
            }
        }

        System.out.println("SENDING OK");
        return ResponseCheckTx.newBuilder().setCode(CodeType.OK).build();
    }

    @Override
    public ResponseCommit requestCommit(RequestCommit requestCommit) {
        hashCount += 1;

        if (txCount == 0) {
            return ResponseCommit.newBuilder().setCode(CodeType.OK).build();
        } else {
            ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE);
            buf.putInt(txCount);
            return ResponseCommit.newBuilder().setCode(CodeType.OK).setData(ByteString.copyFrom(buf)).build();
        }
    }
}
