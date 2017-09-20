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

import jiqs.exceptions.SparksoniqRuntimeException;
import jiqs.exceptions.IteratorFlowException;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.item.ObjectItem;
import jiqs.jsoniq.item.StringItem;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import jiqs.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class ObjectFunctionIterator extends LocalFunctionCallIterator {
    public enum ObjectFunctionOperators{
        KEYS,
        VALUES
    }
    public ObjectFunctionIterator(List<RuntimeIterator> arguments, ObjectFunctionOperators op) {
        super(arguments);
        if(arguments.size() != 1)
            throw new SparksoniqRuntimeException("Incorrect number of arguments for object function; " +
                    "Only one object argument is allowed");
        this._operator = op;
    }

    @Override
    public Item next() {
        if(this._hasNext) {
            if(results == null) {
                _currentIndex = 0;
                results = new ArrayList<>();
                RuntimeIterator objectIterator = this._children.get(0);
                objectIterator.open(_currentDynamicContext);
                Item iteratorResult = objectIterator.next();
                if(!(iteratorResult instanceof ObjectItem))
                    throw new SparksoniqRuntimeException("Invalid argument to "
                            + _operator.toString() + " function, object expected");
                ObjectItem object = (ObjectItem) iteratorResult;
                objectIterator.close();
                switch (_operator) {
                    case KEYS:
                        for(String key : object.getKeys())
                            results.add(new StringItem(key));
                        break;
                    case VALUES:
                        for(Item item : object.getValues())
                            results.add(item);
                        break;
                }
            }
            if(_currentIndex == results.size() - 1)
                this._hasNext = false;
            return results.get(_currentIndex++);
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + _operator.toString() + " function");
    }

    @Override
    public void reset(DynamicContext context){
        super.reset(context);
        this.results = null;
    }

    private List<Item> results = null;
    private int _currentIndex;
    private final ObjectFunctionOperators _operator;
}
