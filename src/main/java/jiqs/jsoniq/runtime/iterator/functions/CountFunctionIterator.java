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
import jiqs.jsoniq.item.IntegerItem;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;

import java.util.ArrayList;
import java.util.List;

public class CountFunctionIterator extends LocalFunctionCallIterator {
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
            return new IntegerItem(results.size());
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " count function");
    }

    public CountFunctionIterator(List<RuntimeIterator> arguments) {
        super(arguments);
        if(arguments.size() != 1)
            throw new SparksoniqRuntimeException("Incorrect number of arguments for count function; " +
                    "Only one sequence argument is allowed");
    }


}
