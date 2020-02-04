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

package sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class ATan2FunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public ATan2FunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            IteratorMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item y;
            RuntimeIterator yIterator = this._children.get(0);
            yIterator.open(_currentDynamicContextForLocalExecution);
            if (yIterator.hasNext()) {
                y = yIterator.next();
            } else {
                throw new UnexpectedTypeException("Type error; y parameter can't be empty sequence ", getMetadata());
            }

            Item x;
            RuntimeIterator xIterator = this._children.get(1);
            xIterator.open(_currentDynamicContextForLocalExecution);
            if (xIterator.hasNext()) {
                x = xIterator.next();
            } else {
                throw new UnexpectedTypeException("Type error; x parameter can't be empty sequence ", getMetadata());
            }

            if (y.isNumeric() && x.isNumeric()) {
                try {
                    this._hasNext = false;
                    return ItemFactory.getInstance()
                        .createDoubleItem(Math.atan2(y.castToDoubleValue(), x.castToDoubleValue()));

                } catch (IteratorFlowException e) {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else {
                throw new UnexpectedTypeException(
                        "ATan2 expression has non numeric args "
                            +
                            y.serialize()
                            + ", "
                            + x.serialize(),
                        getMetadata()
                );
            }
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " atan2 function", getMetadata());
    }
}
