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
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class ArrayFunctionIterator extends LocalFunctionCallIterator {

    public enum ArrayFunctionOperators{
        SIZE
    }

    public ArrayFunctionIterator(List<RuntimeIterator> arguments, ArrayFunctionOperators op,
                                 IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        this._operator = op;
    }

    @Override
    public Item next() {
        if(this._hasNext) {
            this._hasNext = false;
            RuntimeIterator arrayIterator = this._children.get(0);
            arrayIterator.open(_currentDynamicContext);
            Item iteratorResult = arrayIterator.next();
            if(!(iteratorResult instanceof ArrayItem))
                throw new SparksoniqRuntimeException("Invalid argument to "
                        + _operator.toString() + " function, array expected");
            ArrayItem array = (ArrayItem) iteratorResult;
            arrayIterator.close();
            return new IntegerItem(array.getSize());
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + _operator.toString() + " function",
                getMetadata());
    }

    private final ArrayFunctionOperators _operator;
}
