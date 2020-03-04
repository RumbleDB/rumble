/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.functions.sequences.value;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class IndexOfFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private Item search;
    private Item nextResult;
    private int currentIndex;

    public IndexOfFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "index-of function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        this.sequenceIterator = this.children.get(0);
        RuntimeIterator searchIterator = this.children.get(1);
        this.currentIndex = 0;

        this.sequenceIterator.open(context);
        searchIterator.open(context);

        if (!searchIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. index-of can't be performed with empty sequences",
                    getMetadata()
            );
        }
        this.search = searchIterator.next();
        if (searchIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. index-of can't be performed with sequences with more than one items",
                    getMetadata()
            );
        }
        if (!this.search.isAtomic()) {
            throw new NonAtomicKeyException(
                    "Invalid args. index-of can't be performed with a non-atomic parameter",
                    getMetadata()
            );
        }
        searchIterator.close();

        setNextResult();
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.sequenceIterator.hasNext()) {
            this.currentIndex += 1;
            Item item = this.sequenceIterator.next();
            if (!item.isAtomic()) {
                throw new NonAtomicKeyException(
                        "Invalid args. index-of can't be performed with a non-atomic in the input sequence",
                        getMetadata()
                );
            } else {
                if (item.compareTo(this.search) == 0) {
                    this.nextResult = ItemFactory.getInstance().createIntegerItem(this.currentIndex);
                    break;
                }
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.sequenceIterator.close();
        } else {
            this.hasNext = true;
        }
    }
}
