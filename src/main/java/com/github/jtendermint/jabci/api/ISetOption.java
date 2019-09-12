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

import com.github.jtendermint.jabci.types.RequestSetOption;
import com.github.jtendermint.jabci.types.ResponseSetOption;

public interface ISetOption {

    /**
     * Set non-consensus critical application specific options. e.g. Key="min-fee",
     * Value="100fermion" could set the minimum fee required for CheckTx (but not
     * DeliverTx - that would be consensus critical).
     *
     * <p>Arguments:</p>
     * <ul>
     *   <li>{@code Key (string)}: Key to set
     *   <li>{@code Value (string)}: Value to set for key
     * </ul>
     * <p>Returns:</p>
     * <ul>
     *   <li>{@code Code (uint32)}: Response code
     *   <li>{@code Log (string)}: The output of the application's logger. May
     *     be non-deterministic.
     *   <li>{@code Info (string)}: Additional information. May
     *     be non-deterministic.
     * </ul>
     * @param req
     * @return
     *
     * @see <a href="https://tendermint.com/docs/spec/abci/abci.html#setoption">In Documentation</a>
     */
    ResponseSetOption requestSetOption(RequestSetOption req);

}
