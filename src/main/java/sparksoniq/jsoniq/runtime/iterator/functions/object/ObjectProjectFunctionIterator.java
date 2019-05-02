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

package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.InvalidSelectorException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class ObjectProjectFunctionIterator extends ObjectFunctionIterator {

    private RuntimeIterator _iterator;
    private Item _nextResult;
    private List<Item> _projKeys;

    public ObjectProjectFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.PROJECT, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);

        _projKeys = getItemsFromIteratorWithCurrentContext(this._children.get(1));
        if (_projKeys.isEmpty()) {
            throw new InvalidSelectorException("Invalid Projection Key; Object projection can't be performed with zero keys: "
                    , getMetadata());
        }

        setNextResult();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " PROJECT function",
                getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        if (_iterator.hasNext()) {
            Item item = _iterator.next();
            if (item.isObject()) {
                _nextResult = getProjection(item, _projKeys);
            } else {
                _nextResult = item;
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    public Item getProjection(Item objItem, List<Item> keys) {
        ArrayList<String> finalKeylist = new ArrayList<>();
        ArrayList<Item> finalValueList = new ArrayList<>();
        for (Item keyItem : keys) {
            String key = keyItem.getStringValue();
            Item value = objItem.getItemByKey(key);
            if (value != null) {
                finalKeylist.add(key);
                finalValueList.add(value);
            }
        }
        return new ObjectItem(finalKeylist, finalValueList, ItemMetadata.fromIteratorMetadata(getMetadata()));

    }
}
