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

import jiqs.semantics.DynamicContext;
import jiqs.semantics.types.SequenceType;
import jiqs.exceptions.IteratorFlowException;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;

import java.util.List;

public class VariableReferenceIterator extends LocalRuntimeIterator {

    public VariableReferenceIterator(String variableName, SequenceType seq) {
        super(null);
        this._variableName = "$" + variableName;
        this.sequence = seq;
    }

    @Override
    public Item next() {
        if(!this.hasNext())
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "$" + _variableName);
        if(items == null){
            items = this._currentDynamicContext.getVariableValue(this._variableName);
        }
        Item item = items.get(currentIndex);
        currentIndex++;
        if(currentIndex == items.size())
            this._hasNext = false;
        return item;
    }

    @Override
    public void reset(DynamicContext context){
        super.reset(context);
        this.currentIndex = 0;
        this.items = null;
    }

    @Override
    public void open(DynamicContext context){
        if(this.isOpen())
            throw new IteratorFlowException("Variable reference iterator already open");
        this._currentDynamicContext = context;
        this.currentIndex = 0;
        this.items = null;
        this._hasNext = true;

    }

    public SequenceType getSequence() {
        return sequence;
    }


    public String getVariableName() {
        return _variableName;
    }

    private SequenceType sequence;
    private String _variableName;
    private List<Item> items = null;
    private int currentIndex  = 0;
}
