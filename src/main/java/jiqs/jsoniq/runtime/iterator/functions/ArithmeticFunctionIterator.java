/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package jiqs.jsoniq.runtime.iterator.functions;

import jiqs.jsoniq.exceptions.IqRuntimeException;
import jiqs.jsoniq.exceptions.IteratorFlowException;
import jiqs.jsoniq.item.DecimalItem;
import jiqs.jsoniq.item.IntegerItem;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ArithmeticFunctionIterator extends LocalFunctionCallIterator {

    public enum ArithmeticFunctionOperator {
        MIN,
        MAX,
        AVG,
        SUM,
    }

    @Override
    public Item next() {
        if(this._hasNext) {
            List<Item> results = new ArrayList<>();
            RuntimeIterator sequenceIterator = this._children.get(0);
            sequenceIterator.open(_currentDynamicContext);
            while (sequenceIterator.hasNext())
                results.add(sequenceIterator.next());
            sequenceIterator.close();
            this._hasNext = false;
            results.forEach(r -> {
                if(!Item.isNumeric(r))
                    throw new IllegalArgumentException("Arithmetic function argument is non numeric");
            });
            //TODO refactor empty items
            Item itemResult;
            if(results.size() == 0)
                return null;
            switch (this._operator){
                //TODO check numeric types conversions
                case AVG:
                    BigDecimal sum = new BigDecimal(0);
                    for(Item r: results)
                        sum = sum.add(Item.getNumericValue(r, BigDecimal.class));
                    return new DecimalItem(sum.divide(new BigDecimal(results.size())));
                case MIN:
                    itemResult = results.get(0);
                    BigDecimal min  = Item.getNumericValue(results.get(0), BigDecimal.class);
                    for(Item r: results) {
                        BigDecimal current = Item.getNumericValue(r, BigDecimal.class);
                        if(min.compareTo(current) > 0){
                            min = current;
                            itemResult = r;
                        }

                    }
                    return itemResult;
                case MAX:
                    itemResult = results.get(0);
                    BigDecimal max  = Item.getNumericValue(results.get(0), BigDecimal.class);
                    for(Item r: results) {
                        BigDecimal current = Item.getNumericValue(r, BigDecimal.class);
                        if(max.compareTo(current) < 0) {
                            max = current;
                            itemResult = r;
                        }
                    }
                    return itemResult;
                case SUM:
                    BigDecimal sumResult  = new BigDecimal(0);
                    for(Item r: results) {
                        BigDecimal current = Item.getNumericValue(r, BigDecimal.class);
                        sumResult = sumResult.add(current);
                    }
                    return new DecimalItem(sumResult);

            }
            throw new IteratorFlowException("Unsupported arithmetic function");
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + _operator.toString() + " function");
    }

    public ArithmeticFunctionIterator(List<RuntimeIterator> arguments,
                                      ArithmeticFunctionOperator operator) {
        super(arguments);
        if(arguments.size() != 1)
        throw new IqRuntimeException("Incorrect number of arguments for arithmetic function; " +
                "Only one sequence argument is allowed");
        this._operator  = operator;
    }

    private final ArithmeticFunctionOperator _operator;
}
