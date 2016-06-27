/*
 * Copyright (c) 2016 wolfposd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.jtmsp;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import com.github.jtmsp.api.IAppendTx;
import com.github.jtmsp.api.ICheckTx;
import com.github.jtmsp.api.ICommit;
import com.github.jtmsp.socket.TSocket;
import com.github.jtmsp.types.Types.CodeType;
import com.github.jtmsp.types.Types.RequestAppendTx;
import com.github.jtmsp.types.Types.RequestCheckTx;
import com.github.jtmsp.types.Types.RequestCommit;
import com.github.jtmsp.types.Types.ResponseAppendTx;
import com.github.jtmsp.types.Types.ResponseCheckTx;
import com.github.jtmsp.types.Types.ResponseCommit;
import com.google.protobuf.ByteString;

/**
 * 
 * Implements a sample counter app. every tx-data must be bigger than the current amount of tx
 * 
 * @author wolfposd
 */
public class JavaCounter implements IAppendTx, ICheckTx, ICommit {

    public static void main(String[] args) throws InterruptedException {
        new JavaCounter();
    }

    private int hashCount = 0;
    private int txCount = 0;
    private TSocket socket;

    public JavaCounter() throws InterruptedException {

        socket = new TSocket();

        socket.registerListener(this);

        new Thread(socket::start).start();
        while (true) {
            Thread.sleep(1000L);
        }
    }

    @Override
    public ResponseAppendTx receivedAppendTx(RequestAppendTx req) {
        System.out.println("got append tx");
        ByteString tx = req.getTx();

        socket.printByteArray(tx.toByteArray());

        if (tx.size() == 0) {
            return ResponseAppendTx.newBuilder().setCode(CodeType.BadNonce).setLog("transaction is empty").build();
        } else if (tx.size() <= 4) {
            int x = new BigInteger(tx.toByteArray()).intValue();
            // this is an int, so okay
            // but we dont actually care what it is. 
        } else {
            return ResponseAppendTx.newBuilder().setCode(CodeType.BadNonce).setLog("got a bad value").build();
        }

        txCount += 1;
        return ResponseAppendTx.newBuilder().setCode(CodeType.OK).build();
    }

    @Override
    public ResponseCheckTx requestCheckTx(RequestCheckTx req) {
        System.out.println("got check tx");
        ByteString tx = req.getTx();
        if (tx.size() <= 4) {
            // hopefully parsable integer
            int txCheck = new BigInteger(tx.toByteArray()).intValue();
            if (txCheck < txCount) {
                System.out.println("txcheck is smaller als txCount, got " + txCheck + " and " + txCount);
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
