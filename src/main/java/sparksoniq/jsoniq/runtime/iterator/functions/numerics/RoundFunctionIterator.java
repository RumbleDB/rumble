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

package sparksoniq.jsoniq.runtime.iterator.functions.numerics;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.rumbledb.api.Item;

public class RoundFunctionIterator extends LocalFunctionCallIterator {


	private static final long serialVersionUID = 1L;
	private RuntimeIterator _iterator;

    public RoundFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
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
            this._hasNext = false;
            Item value = this.getSingleItemOfTypeFromIterator(_iterator, Item.class);
            Item precision;
            if (this._children.size() > 1) {
                RuntimeIterator precisionIterator = this._children.get(1);
                precisionIterator.open(_currentDynamicContext);
                if (precisionIterator.hasNext()) {
                    precision = precisionIterator.next();
                } else {
                    throw new UnexpectedTypeException("Type error; Precision parameter can't be empty sequence ", getMetadata());
                }
            }
            // if second param is not given precision is set as 0 (rounds to a whole number)
            else {
                precision = ItemFactory.getInstance().createIntegerItem(0);
            }
            if (value.isNumeric() && precision.isNumeric()) {
                try {

                    Double val = value.castToDoubleValue();
                    Integer prec = precision.castToIntegerValue();

                    BigDecimal bd = new BigDecimal(val);
                    bd = bd.setScale(prec, RoundingMode.HALF_UP);
                    Double result = bd.doubleValue();

                    return ItemFactory.getInstance().createDoubleItem(result);

                } catch (IteratorFlowException e) {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else {
                throw new UnexpectedTypeException("Round expression has non numeric args " +
                        value.serialize() + ", " + precision.serialize(), getMetadata());
            }

        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " round function", getMetadata());
    }
}
