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
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class ObjectRemoveKeysFunctionIterator extends ObjectFunctionIterator {

    private RuntimeIterator _iterator;
    private Item _nextResult;
    private List<String> _removalKeys;

    public ObjectRemoveKeysFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.REMOVEKEYS, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);

        List<Item> removalKeys = getItemsFromIteratorWithCurrentContext(this._children.get(1));
        if (removalKeys.isEmpty()) {
            throw new InvalidSelectorException("Invalid Key Removal Parameter; Object key removal can't be performed with zero keys: "
                    , getMetadata());
        }
        _removalKeys = new ArrayList<>();
        for (Item removalKeyItem : removalKeys) {
            if (!removalKeyItem.isString()) {
                throw new UnexpectedTypeException("Remove-keys function has non-string key args.", getMetadata());
            }
            String removalKey = removalKeyItem.getStringValue();
            _removalKeys.add(removalKey);
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
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " REMOVE-KEYS function",
                getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        if (_iterator.hasNext()) {
            Item item = _iterator.next();
            if (item.isObject()) {
                _nextResult = removeKeys(item, _removalKeys);
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

    public Item removeKeys(Item objItem, List<String> removalKeys) {
        ArrayList<String> finalKeylist = new ArrayList<>();
        ArrayList<Item> finalValueList = new ArrayList<>();

        for (String objectKey : objItem.getKeys()) {
            if (!removalKeys.contains(objectKey)) {
                finalKeylist.add(objectKey);
                finalValueList.add(objItem.getItemByKey(objectKey));
            }
        }
        return ItemFactory.getInstance().createObjectItem(finalKeylist, finalValueList, ItemMetadata.fromIteratorMetadata(getMetadata()));
    }
}
