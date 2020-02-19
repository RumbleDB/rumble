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

package org.rumbledb.runtime.functions.sequences.general;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class SubsequenceFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private Item nextResult;
    private int currentPosition;
    private int startPosition;
    private int length;

    public SubsequenceFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        this.currentPosition = 1; // JSONiq indices start from 1

        this.length = -1; // unassigned
        // if length param is given, process it
        RuntimeIterator lengthIterator;
        Item lengthItem = null;
        if (this.children.size() == 3) {
            lengthIterator = this.children.get(2);

            lengthIterator.open(context);
            if (!lengthIterator.hasNext()) {
                throw new UnexpectedTypeException(
                        "Invalid args. subsequence can't be performed with empty sequence as the length",
                        getMetadata()
                );
            }
            lengthItem = lengthIterator.next();
            if (lengthItem.isArray()) {
                throw new NonAtomicKeyException(
                        "Invalid args. subsequence can't be performed with an array parameter as the length",
                        getMetadata()
                );
            } else if (lengthItem.isObject()) {
                throw new NonAtomicKeyException(
                        "Invalid args. subsequence can't be performed with an object parameter as the length",
                        getMetadata()
                );
            } else if (!(lengthItem.isNumeric())) {
                throw new UnexpectedTypeException(
                        "Invalid args. Length parameter should be an numeric(Integer/Decimal/Double)",
                        getMetadata()
                );
            }
            lengthIterator.close();
            // round double to nearest int
            try {
                this.length = (int) Math.round((lengthItem.castToDoubleValue()));

            } catch (IteratorFlowException e) {
                throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
            }
        }

        // process start position param
        RuntimeIterator positionIterator = this.children.get(1);
        positionIterator.open(context);
        if (!positionIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. subsequence can't be performed with empty sequence as the position",
                    getMetadata()
            );
        }
        Item positionItem = positionIterator.next();
        if (positionItem.isArray()) {
            throw new NonAtomicKeyException(
                    "Invalid args. subsequence can't be performed with an array parameter as the position",
                    getMetadata()
            );
        } else if (positionItem instanceof ObjectItem) {
            throw new NonAtomicKeyException(
                    "Invalid args. subsequence can't be performed with an object parameter as the position",
                    getMetadata()
            );
        } else if (!(positionItem.isNumeric())) {
            throw new UnexpectedTypeException(
                    "Invalid args. Position parameter should be a numeric(Integer/Decimal/Double)",
                    getMetadata()
            );
        }
        positionIterator.close();
        // round double to nearest int
        this.startPosition = (int) Math.round((positionItem.castToDoubleValue()));

        // first, perform all parameter checks (above)
        // if length is 0, just return empty sequence
        if (this.length == 0) {
            this.hasNext = false;
            return;
        } else {
            this.sequenceIterator = this.children.get(0);
            this.sequenceIterator.open(context);

            // find the start of the subsequence
            while (this.sequenceIterator.hasNext()) {
                if (this.currentPosition < this.startPosition) {
                    this.sequenceIterator.next(); // skip item
                } else {
                    this.nextResult = this.sequenceIterator.next();
                    // if length is specified, decrement it
                    if (this.length != -1) {
                        this.length--;
                    }
                    break;
                }
                this.currentPosition++;
            }
        }

        // if startPosition overshoots, return empty sequence
        if (this.nextResult == null) {
            this.hasNext = false;
            this.sequenceIterator.close();
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "subsequence function", getMetadata());
    }

    public void setNextResult() {
        this.nextResult = null;

        if (this.length != 0) {
            if (this.sequenceIterator.hasNext()) {
                if (this.length > 0) { // take length many items -> decrement the value for each item until 0
                    this.nextResult = this.sequenceIterator.next();
                    this.length--;
                } else if (this.length == -1) { // length not specified -> take all items until the end
                    this.nextResult = this.sequenceIterator.next();
                } else {
                    throw new OurBadException(
                            "Unexpected length value found."
                    );
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
