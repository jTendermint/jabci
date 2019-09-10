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

import com.github.jtendermint.jabci.proto.types.RequestInitChain;
import com.github.jtendermint.jabci.proto.types.ResponseInitChain;

public interface IInitChain {
    /**
     * <p>Called once upon genesis</p>
     * <p>If ResponseInitChain.Validators is empty, the initial validator set will be
     * the RequestInitChain.Validators</p>
     * <p>If ResponseInitChain.Validators is not empty,
     * the initial validator set will be the ResponseInitChain.Validators
     * (regardless of what is in RequestInitChain.Validators).</p>
     * <p>This allows the app to decide if it wants to accept the initial validator set proposed by
     * tendermint (ie. in the genesis file), or if it wants to use a different one
     * (perhaps computed based on some application specific information in the
     * genesis file).</p>
     *
     * <p>Arguments:</p>
     * <ul>
     *   <li>{@code Time (google.protobuf.Timestamp)}: Genesis time.
     *   <li>{@code ChainID (string)}: ID of the blockchain.
     *   <li>{@code ConsensusParams (ConsensusParams)}: Initial consensus-critical parameters.
     *   <li>{@code Validators ([]ValidatorUpdate)}: Initial genesis validators.
     *   <li>{@code AppStateBytes ([]byte)}: Serialized initial application state. Amino-encoded JSON bytes.
     * </ul>
     * <p>Returns:</p>
     * <ul>
     *   <li>{@code ConsensusParams (ConsensusParams)}: Initial
     *     consensus-critical parameters.
     *   <li>{@code Validators ([]ValidatorUpdate)}: Initial validator set (if non empty).
     * </ul>
     * @param req
     * @see <a href="https://tendermint.com/docs/spec/abci/abci.html#initchain">In Documentation</a>
     */
    ResponseInitChain requestInitChain(RequestInitChain req);
}
