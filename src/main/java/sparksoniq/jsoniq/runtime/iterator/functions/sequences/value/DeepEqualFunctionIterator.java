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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.value;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class DeepEqualFunctionIterator extends LocalFunctionCallIterator {

    public DeepEqualFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;

            RuntimeIterator sequenceIterator1 = this._children.get(0);
            RuntimeIterator sequenceIterator2 = this._children.get(1);

            List<Item> items1 = getItemsFromIteratorWithCurrentContext(sequenceIterator1);
            List<Item> items2 = getItemsFromIteratorWithCurrentContext(sequenceIterator2);

            boolean res = checkDeepEqual(items1, items2);
            return new BooleanItem(res);
        } else {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "deep-equal function", getMetadata());
        }
    }


    public boolean checkDeepEqual(List<Item> items1, List<Item> items2) {
        if (items1.size() != items2.size()) {
            return false;
        } else {
            for (int i = 0; i < items1.size(); i++){
                Item item1 = items1.get(i);
                Item item2 = items2.get(i);

                if (item1.isArray()) {
                    // if item types don't match
                    if (!(item2.isArray())) {
                        return false;
                    } else {
                        // if types match, recursively check if array is deep-equal
                        ArrayItem arrItem1 = (ArrayItem)item1;
                        ArrayItem arrItem2 = (ArrayItem)item2;

                        if (!checkDeepEqual(arrItem1.getItems(), arrItem2.getItems())) {
                            return false;
                        }
                    }
                } else if (item1.isObject()) {
                    // if item types don't match
                    if (!(item2.isObject())) {
                        return false;
                    } else {
                        // if types match, recursively check if object is deep-equal
                        if (item1.getKeys().equals(item2.getKeys())) {
                            if (!checkDeepEqual((List<Item>)item1.getValues(), (List<Item>)item2.getValues())) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                } else if (Item.compareItems(item1, item2) != 0){
                    // if atomic items' values are not equal
                    return false;
                } else {
                    // do nothing
                }
            }
            return true;
        }
    }
}
