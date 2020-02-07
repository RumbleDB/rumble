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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class InsertBeforeFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private RuntimeIterator insertIterator;
    private Item nextResult;
    private int insertPosition; // position to start inserting
    private int currentPosition; // current position
    private boolean insertingNow; // check if currently iterating over insertIterator
    private boolean insertingCompleted;

    public InsertBeforeFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "insert-before function", getMetadata());
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.currentPosition = 1; // initialize index as the first item
        this.insertingNow = false;
        this.insertingCompleted = false;

        RuntimeIterator positionIterator = this.children.get(1);
        positionIterator.open(context);
        if (!positionIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. insert-before can't be performed with empty sequence as the position",
                    getMetadata()
            );
        }
        Item positionItem = positionIterator.next();
        if (positionItem.isArray()) {
            throw new NonAtomicKeyException(
                    "Invalid args. insert-before can't be performed with an array parameter as the position",
                    getMetadata()
            );
        } else if (positionItem.isObject()) {
            throw new NonAtomicKeyException(
                    "Invalid args. insert-before can't be performed with an object parameter as the position",
                    getMetadata()
            );
        } else if (!(positionItem.isInteger())) {
            throw new UnexpectedTypeException(
                    "Invalid args. Position parameter should be an integer",
                    getMetadata()
            );
        }
        this.insertPosition = ((IntegerItem) positionItem).getIntegerValue();
        positionIterator.close();

        this.sequenceIterator = this.children.get(0);
        this.insertIterator = this.children.get(2);

        this.sequenceIterator.open(context);
        this.insertIterator.open(context);

        setNextResult();
    }

    public void setNextResult() {
        this.nextResult = null;

        // don't check for insertion triggers once insertion is completed
        if (this.insertingCompleted == false) {
            if (!this.insertingNow) {
                if (this.insertPosition <= this.currentPosition) { // start inserting if condition is met
                    if (this.insertIterator.hasNext()) {
                        this.insertingNow = true;
                        this.nextResult = this.insertIterator.next();
                    } else {
                        this.insertingNow = false;
                        this.insertingCompleted = true;
                    }
                }
            } else { // if inserting
                if (this.insertIterator.hasNext()) { // return an item from insertIterator at each iteration
                    this.nextResult = this.insertIterator.next();
                } else {
                    this.insertingNow = false;
                    this.insertingCompleted = true;
                }
            }
        }

        // if not inserting, take the next element from input sequence
        if (this.insertingNow == false) {
            if (this.sequenceIterator.hasNext()) {
                this.nextResult = this.sequenceIterator.next();
                this.currentPosition++;
            } else if (this.insertIterator.hasNext()) {
                this.nextResult = this.insertIterator.next();
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.sequenceIterator.close();
            this.insertIterator.close();
        } else {
            this.hasNext = true;
        }
    }
}
