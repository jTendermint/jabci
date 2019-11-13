/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 - 2019
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
package com.github.jtendermint.jabci.api;

import com.github.jtendermint.jabci.types.RequestInfo;
import com.github.jtendermint.jabci.types.ResponseInfo;

public interface IInfo {

    /**
     * <p>Return information about the application state. </p>
     * <p>Used to sync Tendermint with the application during a handshake that happens on startup.</p>
     * <p>The returned {@code AppVersion} will be included in the Header of every block.</p>
     * <p>Tendermint expects {@code LastBlockAppHash} and {@code LastBlockHeight} to be updated during {@code Commit},
     * ensuring that {@code Commit} is never called twice for the same block height.</p>
     *
     *
     * <p>Arguments:</p>
     * <ul>
     * <li>{@code Version (string)}: The Tendermint software semantic version
     * <li>{@code BlockVersion (uint64)}: The Tendermint Block Protocol version
     * <li>{@code P2PVersion (uint64)}: The Tendermint P2P Protocol version
     * </ul>
     * <p>Returns:</p>
     * <ul>
     * <li>{@code Data (string)}: Some arbitrary information
     * <li>{@code Version (string)}: The application software semantic version
     * <li>{@code AppVersion (uint64)}: The application protocol version
     * <li>{@code LastBlockHeight (int64)}: Latest block for which the app has
     called Commit
     * <li>{@code LastBlockAppHash ([]byte)}: Latest result of Commit
     * </ul>
     * @param req
     * @return
     *
     * @see <a href="https://tendermint.com/docs/spec/abci/abci.html#info">In Documentation</a>
     */
    ResponseInfo requestInfo(RequestInfo req);

}
