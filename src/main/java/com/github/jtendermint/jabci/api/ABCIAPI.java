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
package com.github.jtendermint.jabci.api;

/**
 * ABCI-Protocol<br>
 * Bundled Interface of Sub-Interfaces
 *
 * <br>
 * See
 * <a href="https://github.com/tendermint/abci">https://github.com/tendermint
 * /abci</a> for more info
 *
 * @version 06.11.2018
 *
 */
public interface ABCIAPI extends //
        IEcho, // RequestEcho
        IFlush, // RequestFlush
        IInfo, // RequestInfo
        ISetOption, // RequestSetOption
        IInitChain, // RequestInitChain
        IQuery, // RequestQuery
        IBeginBlock, // RequestBeginBlock
        ICheckTx, // RequestCheckTx
        IDeliverTx, // RequestDeliverTx
        IEndBlock, // RequestEndBlock
        ICommit // RequestCommit
{
    // message Request {
    // oneof value {
    // RequestEcho echo = 2;
    // RequestFlush flush = 3;
    // RequestInfo info = 4;
    // RequestSetOption set_option = 5;
    // RequestInitChain init_chain = 6;
    // RequestQuery query = 7;
    // RequestBeginBlock begin_block = 8;
    // RequestCheckTx check_tx = 9;
    // RequestDeliverTx deliver_tx = 19;
    // RequestEndBlock end_block = 11;
    // RequestCommit commit = 12;
    // }
    // }
}
