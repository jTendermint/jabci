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
package com.github.jtendermint.jabci.api;

import com.github.jtendermint.jabci.types.RequestEndBlock;
import com.github.jtendermint.jabci.types.ResponseEndBlock;

public interface IEndBlock {

    /**
     * Signals the end of a block. Called prior to each Commit after all
     * transactions Returns:<br>
     * Validators ([]Validator): Changed validators with new voting powers (0 to
     * remove)
     * 
     * @param req the Request representing data about the EndBlock (height,...)
     */
    ResponseEndBlock requestEndBlock(RequestEndBlock req);

}
