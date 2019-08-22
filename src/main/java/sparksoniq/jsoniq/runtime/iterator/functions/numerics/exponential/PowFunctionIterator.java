/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class PowFunctionIterator extends LocalFunctionCallIterator {


	private static final long serialVersionUID = 1L;
	private RuntimeIterator _iterator;

    public PowFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _iterator = this._children.get(0);
        _iterator.open(_currentDynamicContext);
        if (_iterator.hasNext()) {
            this._hasNext = true;
        } else {
            this._hasNext = false;
        }
        _iterator.close();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item base = this.getSingleItemOfTypeFromIterator(_iterator, Item.class);
            Item exponent;
            RuntimeIterator exponentIterator = this._children.get(1);
            exponentIterator.open(_currentDynamicContext);
            if (exponentIterator.hasNext()) {
                exponent = exponentIterator.next();
            } else {
                throw new UnexpectedTypeException("Type error; Exponent parameter can't be empty sequence ", getMetadata());
            }
            if (base.isNumeric() && exponent.isNumeric()) {
                try {
                    Double result = Math.pow(base.getNumericValue(Double.class)
                            , exponent.getNumericValue(Double.class));
                    this._hasNext = false;
                    return ItemFactory.getInstance().createDoubleItem(result);
                } catch (IteratorFlowException e) {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else {
                throw new UnexpectedTypeException("Pow expression has non numeric args " +
                        base.serialize() + ", " + exponent.serialize(), getMetadata());
            }

        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " pow function", getMetadata());
    }


}
