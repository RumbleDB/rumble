/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package sparksoniq.jsoniq.runtime.iterator.primary;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;

public class ArrayRuntimeIterator extends LocalRuntimeIterator {


	private static final long serialVersionUID = 1L;
	private Item _item = null;

    public ArrayRuntimeIterator(RuntimeIterator arrayItems, IteratorMetadata iteratorMetadata) {
        super(new ArrayList<>(), iteratorMetadata);
        if (arrayItems != null)
            this._children.add(arrayItems);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            List<Item> result = this.runChildrenIterators(this._currentDynamicContext);
            this._item = ItemFactory.getInstance().createArrayItem(result);
            this._hasNext = false;
            return _item;
        } else throw new IteratorFlowException("Invalid next() call on array iterator", getMetadata());
    }
}
