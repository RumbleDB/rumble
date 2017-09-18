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
 package jiqs.jsoniq.runtime.iterator.primary;

import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.exceptions.IteratorFlowException;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.postfix.PredicateIterator;

import java.util.ArrayList;
import java.util.List;

public class ContextExpressionIterator extends LocalRuntimeIterator {
    public ContextExpressionIterator() {
        super(null);
    }

    @Override
    public Item next() {
        if(hasNext()){
            this._hasNext = false;
            List<Item> results = new ArrayList<>();
            if(results.size() > 1)
                throw new IteratorFlowException("Invalid context item expression");
            return _currentDynamicContext.getVariableValue("$$").get(0);
        }
            throw new IteratorFlowException("Invalid next() call in Context Expression!");
    }



}
