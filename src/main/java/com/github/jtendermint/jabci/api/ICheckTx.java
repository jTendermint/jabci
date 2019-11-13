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

import com.github.jtendermint.jabci.types.RequestCheckTx;
import com.github.jtendermint.jabci.types.ResponseCheckTx;

public interface ICheckTx {

    /**
     * Validate a transaction. This message should not mutate the state. Transactions are first run through CheckTx before broadcast to
     * peers in the mempool layer. You can make CheckTx semi-stateful and clear the state upon Commit or BeginBlock, to allow for dependent
     * sequences of transactions in the same block.
     *
     * <p>Arguments</p>:
     * <ul>
     *  <li>{@code Tx ([]byte)}: The request transaction bytes
     *  <li>{@code Type (CheckTxType)}: What type of `CheckTx` request is this? At present,
     *     there are two possible values: `CheckTx_New` (the default, which says
     *     that a full check is required), and `CheckTx_Recheck` (when the mempool is
     *     initiating a normal recheck of a transaction).
     * </ul>
     * <p>Returns:</p>
     * <ul>
     *  <li>{@code Code (uint32)}: Response code
     *  <li>{@code Data ([]byte)}: Result bytes, if any.
     *  <li>{@code Log (string)}: The output of the application's logger. May
     *     be non-deterministic.
     *  <li>{@code Info (string)}: Additional information. May
     *     be non-deterministic.
     *  <li>{@code GasWanted (int64)}: Amount of gas requested for transaction.
     *  <li>{@code GasUsed (int64)}: Amount of gas consumed by transaction.
     *  <li>{@code Tags ([]cmn.KVPair)}: Key-Value tags for filtering and indexing
     *     transactions (eg. by account).
     *  <li>{@code Codespace (string)}: Namespace for the `Code`.
     * </ul>
     * @param req
     * @return
     * @see <a href="https://tendermint.com/docs/spec/abci/abci.html#checktx">In Documentation</a>
     */
    ResponseCheckTx requestCheckTx(RequestCheckTx req);

}
