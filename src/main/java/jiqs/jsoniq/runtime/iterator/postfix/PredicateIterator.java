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
 package jiqs.jsoniq.runtime.iterator.postfix;

import jiqs.jsoniq.item.Item;
import jiqs.semantics.DynamicContext;
import jiqs.jsoniq.exceptions.IqRuntimeException;
import jiqs.jsoniq.exceptions.IteratorFlowException;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

public class PredicateIterator extends LocalRuntimeIterator {

    public PredicateIterator(RuntimeIterator sequence, RuntimeIterator filterExpression) {
        super(null);
        this._children.add(sequence);
        this._children.add(filterExpression);
    }


    @Override
    public Item next() {
        if(this._children.size() < 2)
            throw new IqRuntimeException("Invalid Predicate! Must initialize filter before calling next");

        if(result == null) {
            unfilteredSequence = new ArrayList<>();
            RuntimeIterator sequence = this._children.get(0);
            //get unfiltered sequence
            sequence.open(_currentDynamicContext);
            while (sequence.hasNext())
                unfilteredSequence.add(sequence.next());
            sequence.close();
            // get filtered list
            result = new ArrayList<>();
            RuntimeIterator filter = this._children.get(1);
            for(Item item : unfilteredSequence){
                //set the current item for the $$ children
                List<Item> currentItems = new ArrayList<>();
                currentItems.add(item);
                _currentDynamicContext.addVariableValue("$$", currentItems);
                filter.open(_currentDynamicContext);
                if (Item.getEffectiveBooleanValue(filter.next()))
                    result.add(item);
                filter.close();
                filter.reset(_currentDynamicContext);
            }
            _currentDynamicContext.removeVariable("$$");

        }

        if(currentIndex <= result.size() - 1){
            if(currentIndex + 1 == result.size())
                this._hasNext =false;
            return result.get(currentIndex++);
        }

        throw new IteratorFlowException("Invalid next() call in Predicate!");
    }

    @Override
    public void reset(DynamicContext dc) {
        super.reset(dc);
        this.result = null;
    }

    private List<Item> unfilteredSequence = null;
    private List<Item> result = null;
    private int currentIndex = 0;

}
