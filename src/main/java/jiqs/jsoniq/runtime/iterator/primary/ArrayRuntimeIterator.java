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

import jiqs.jsoniq.item.ArrayItem;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.exceptions.IteratorFlowException;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

public class ArrayRuntimeIterator extends LocalRuntimeIterator {

    public ArrayRuntimeIterator(RuntimeIterator arrayItems) {
        super(new ArrayList<>());
        if(arrayItems!=null)
            this._children.add(arrayItems);
    }

    @Override
    public ArrayItem next() {
        if(this._hasNext) {
            List<Item> result = this.runChildrenIterators(this._currentDynamicContext);
            this._item = new ArrayItem(result);
            this._hasNext = false;
            return _item;
        }
        else throw new IteratorFlowException("Invalid next() call on array iterator");
    }

    private ArrayItem _item = null;
}
