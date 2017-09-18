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
import jiqs.jsoniq.item.ArrayItem;
import jiqs.jsoniq.item.IntegerItem;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.item.ObjectItem;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class ArrayFunctionIterator extends LocalFunctionCallIterator {

    public enum ArrayFunctionOperators{
        SIZE
    }

    public ArrayFunctionIterator(List<RuntimeIterator> arguments, ArrayFunctionOperators op) {
        super(arguments);
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
                throw new IqRuntimeException("Invalid argument to "
                        + _operator.toString() + " function, array expected");
            ArrayItem array = (ArrayItem) iteratorResult;
            arrayIterator.close();
            return new IntegerItem(array.getSize());
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + _operator.toString() + " function");
    }

    private final ArrayFunctionOperators _operator;
}
