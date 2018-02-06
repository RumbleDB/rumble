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
 package sparksoniq.jsoniq.runtime.iterator.functions;

import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

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
            RuntimeIterator sequenceIterator = this._children.get(0);
            List<Item> results = getItemsFromIteratorWithCurrentContext(sequenceIterator);
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
                    return new DecimalItem(sum.divide(new BigDecimal(results.size())),
                            ItemMetadata.fromIteratorMetadata(getMetadata()));
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
                    return new DecimalItem(sumResult, ItemMetadata.fromIteratorMetadata(getMetadata()));

            }
            throw new IteratorFlowException("Unsupported arithmetic function", getMetadata());
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + _operator.toString() + " function",
                    getMetadata());
    }

    public ArithmeticFunctionIterator(List<RuntimeIterator> arguments,
                                      ArithmeticFunctionOperator operator, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        if(arguments.size() != 1)
        throw new SparksoniqRuntimeException("Incorrect number of arguments for arithmetic function; " +
                "Only one sequence argument is allowed");
        this._operator  = operator;
    }

    private final ArithmeticFunctionOperator _operator;
}
