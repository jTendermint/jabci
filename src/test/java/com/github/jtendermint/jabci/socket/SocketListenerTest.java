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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.github.jtendermint.jabci.api.ABCIAPI;
import com.github.jtendermint.jabci.api.CodeType;
import com.github.jtendermint.jabci.api.ICheckTx;
import com.github.jtendermint.jabci.api.IInfo;
import com.github.jtendermint.jabci.types.Request;
import com.github.jtendermint.jabci.types.RequestBeginBlock;
import com.github.jtendermint.jabci.types.RequestCheckTx;
import com.github.jtendermint.jabci.types.RequestCommit;
import com.github.jtendermint.jabci.types.RequestDeliverTx;
import com.github.jtendermint.jabci.types.RequestEcho;
import com.github.jtendermint.jabci.types.RequestEndBlock;
import com.github.jtendermint.jabci.types.RequestFlush;
import com.github.jtendermint.jabci.types.RequestInfo;
import com.github.jtendermint.jabci.types.RequestInitChain;
import com.github.jtendermint.jabci.types.RequestQuery;
import com.github.jtendermint.jabci.types.RequestSetOption;
import com.github.jtendermint.jabci.types.ResponseBeginBlock;
import com.github.jtendermint.jabci.types.ResponseCheckTx;
import com.github.jtendermint.jabci.types.ResponseCommit;
import com.github.jtendermint.jabci.types.ResponseDeliverTx;
import com.github.jtendermint.jabci.types.ResponseEcho;
import com.github.jtendermint.jabci.types.ResponseEndBlock;
import com.github.jtendermint.jabci.types.ResponseFlush;
import com.github.jtendermint.jabci.types.ResponseInfo;
import com.github.jtendermint.jabci.types.ResponseInitChain;
import com.github.jtendermint.jabci.types.ResponseQuery;
import com.github.jtendermint.jabci.types.ResponseSetOption;
import com.google.protobuf.ByteString;

public class SocketListenerTest {

    @Test
    public void testSingleSocketListenerSelection() throws IOException {

        Wrapper<Boolean> wasCalled = new Wrapper<>(false);

        TSocket socket = new TSocket();

        socket.registerListener((IInfo) req -> {
            wasCalled.value = true;
            return ResponseInfo.newBuilder().build();
        });

        // Test IInfo
        Request r = Request.newBuilder().setInfo(RequestInfo.newBuilder().build()).build();
        socket.handleRequest(r);
        assertTrue(wasCalled.value.booleanValue());
    }

    @Test
    public void testMultipleSocketListenerSelection() throws IOException {

        Wrapper<Boolean> wasCalled = new Wrapper<>(false);

        TSocket socket = new TSocket();

        socket.registerListener((IInfo) req -> {
            wasCalled.value = true;
            return ResponseInfo.newBuilder().build();
        });

        socket.registerListener((ICheckTx) tx -> {
            wasCalled.value = true;
            return ResponseCheckTx.newBuilder().setCode(CodeType.OK).build();
        });

        // Test IInfo
        Request r = Request.newBuilder().setInfo(RequestInfo.newBuilder().build()).build();
        socket.handleRequest(r);
        assertTrue(wasCalled.value);

        // Reset Value
        wasCalled.value = false;
        assertFalse(wasCalled.value);

        // Test CheckTx
        r = Request.newBuilder().setCheckTx(RequestCheckTx.newBuilder().setTx(ByteString.EMPTY).build()).build();
        socket.handleRequest(r);
        assertTrue(wasCalled.value);

    }

    @Test
    public void testMultipleAndABCIAPISocketListenerSelection() throws IOException {

        Wrapper<Boolean> wasCalled = new Wrapper<>(false);

        TSocket socket = new TSocket();

        socket.registerListener((IInfo) req -> {
            wasCalled.value = true;
            return ResponseInfo.newBuilder().build();
        });

        socket.registerListener((ICheckTx) tx -> {
            wasCalled.value = true;
            return ResponseCheckTx.newBuilder().setCode(CodeType.OK).build();
        });

        // setup a whole ABCI-API, which changes the value to false for Info and CheckTx
        // but returns true for beginblock
        socket.registerListener(new ABCIAPI() {
            public ResponseEcho requestEcho(RequestEcho req) {
                return null;
            }

            public ResponseSetOption requestSetOption(RequestSetOption req) {
                return null;
            }

            public ResponseQuery requestQuery(RequestQuery req) {
                return null;
            }

            public ResponseInitChain requestInitChain(RequestInitChain req) {
                return null;
            }

            public ResponseInfo requestInfo(RequestInfo req) {
                wasCalled.value = false;
                return ResponseInfo.newBuilder().build();
            }

            public ResponseFlush requestFlush(RequestFlush reqfl) {
                return null;
            }

            public ResponseEndBlock requestEndBlock(RequestEndBlock req) {
                return null;
            }

            public ResponseCommit requestCommit(RequestCommit requestCommit) {
                return null;
            }

            public ResponseCheckTx requestCheckTx(RequestCheckTx req) {
                wasCalled.value = false;
                return ResponseCheckTx.newBuilder().setCode(CodeType.OK).build();
            }

            public ResponseBeginBlock requestBeginBlock(RequestBeginBlock req) {
                wasCalled.value = true;
                return ResponseBeginBlock.newBuilder().build();
            }

            public ResponseDeliverTx receivedDeliverTx(RequestDeliverTx req) {
                return null;
            }
        });

        // Test IInfo
        Request r = Request.newBuilder().setInfo(RequestInfo.newBuilder().build()).build();
        socket.handleRequest(r);
        assertTrue(wasCalled.value);

        // Reset Value
        wasCalled.value = false;
        assertFalse(wasCalled.value);

        // Test CheckTx
        r = Request.newBuilder().setCheckTx(RequestCheckTx.newBuilder().setTx(ByteString.EMPTY).build()).build();
        socket.handleRequest(r);
        assertTrue(wasCalled.value);

        // Reset Value
        wasCalled.value = false;
        assertFalse(wasCalled.value);

        // Test ABCIAPI
        r = Request.newBuilder().setBeginBlock(RequestBeginBlock.newBuilder().build()).build();
        socket.handleRequest(r);
        assertTrue(wasCalled.value);
    }

    @Test
    public void testMultiInheritance() throws IOException {
        Wrapper<Boolean> wasCalled = new Wrapper<>(false);

        TSocket socket = new TSocket();

        socket.registerListener(new MultiInherit(wasCalled));

        // Test MultiInherit - RequestInfo
        Request r = Request.newBuilder().setInfo(RequestInfo.newBuilder().build()).build();
        socket.handleRequest(r);
        assertTrue(wasCalled.value);

    }

    public abstract class SomeAbstractClass implements ABCIAPI {

    }

    public abstract class SomeOtherAbstractClass extends SomeAbstractClass {

    }

    public class MultiInherit extends SomeOtherAbstractClass {

        public Wrapper<Boolean> wasCalled;

        public MultiInherit(Wrapper<Boolean> wasCalled) {
            this.wasCalled = wasCalled;
        }

        public ResponseInfo requestInfo(RequestInfo req) {
            wasCalled.value = true;
            return ResponseInfo.newBuilder().build();
        }

        public ResponseDeliverTx receivedDeliverTx(RequestDeliverTx req) {
            return null;
        }

        public ResponseBeginBlock requestBeginBlock(RequestBeginBlock req) {
            return null;
        }

        public ResponseCheckTx requestCheckTx(RequestCheckTx req) {
            return null;
        }

        public ResponseCommit requestCommit(RequestCommit requestCommit) {
            return null;
        }

        public ResponseEndBlock requestEndBlock(RequestEndBlock req) {
            return null;
        }

        public ResponseFlush requestFlush(RequestFlush reqfl) {
            return null;
        }

        public ResponseInitChain requestInitChain(RequestInitChain req) {
            return null;
        }

        public ResponseQuery requestQuery(RequestQuery req) {
            return null;
        }

        public ResponseSetOption requestSetOption(RequestSetOption req) {
            return null;
        }

        public ResponseEcho requestEcho(RequestEcho req) {
            return null;
        }

    }

    public class Wrapper<K> {

        public K value;

        public Wrapper(K k) {
            value = k;
        }

    }
}
