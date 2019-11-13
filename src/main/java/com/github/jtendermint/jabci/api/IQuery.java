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

import com.github.jtendermint.jabci.types.RequestQuery;
import com.github.jtendermint.jabci.types.ResponseQuery;

public interface IQuery {

    /**
     * <p>Query for data from the application at current or past height.</p>
     * <p>Optionally return Merkle proof.</p>
     * <p>Merkle proof includes self-describing type field to support many types of Merkle trees and encoding formats.</p>
     *
     * <p>Arguments:</p>
     * <ul>
     *   <li>{@code Data ([]byte)}: Raw query bytes. Can be used with or in lieu
     *     of Path.
     *   <li>{@code Path (string)}: Path of request, like an HTTP GET path. Can be
     *     used with or in liue of Data.
     *   <li>{@code Height (int64)}: The block height for which you want the query
     *     (default=0 returns data for the latest committed block). Note
     *     that this is the height of the block containing the
     *     application's Merkle root hash, which represents the state as it
     *     was after committing the block at Height-1
     *   <li>{@code Prove (bool)}: Return Merkle proof with response if possible
     * </ul>
     * <p>Returns:</p>
     * <ul>
     *   <li>{@code Code (uint32)}: Response code.
     *   <li>{@code Log (string)}: The output of the application's logger. May
     *     be non-deterministic.
     *   <li>{@code Info (string)}: Additional information. May
     *     be non-deterministic.
     *   <li>{@code Index (int64)}: The index of the key in the tree.
     *   <li>{@code Key ([]byte)}: The key of the matching data.
     *   <li>{@code Value ([]byte)}: The value of the matching data.
     *   <li>{@code Proof (Proof)}: Serialized proof for the value data, if requested, to be
     *     verified against the `AppHash` for the given Height.
     *   <li>{@code Height (int64)}: The block height from which data was derived.
     *     Note that this is the height of the block containing the
     *     application's Merkle root hash, which represents the state as it
     *     was after committing the block at Height-1
     *   <li>{@code Codespace (string)}: Namespace for the `Code`.
     * </ul>
     * @param req
     * @see <a href="https://tendermint.com/docs/spec/abci/abci.html#query">In Documentation</a>
     */
    ResponseQuery requestQuery(RequestQuery req);

}
