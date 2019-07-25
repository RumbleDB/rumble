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

package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class SubstringFunctionIterator extends LocalFunctionCallIterator {
    public SubstringFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            String result;
            StringItem stringItem = this.getSingleItemOfTypeFromIterator(this._children.get(0), StringItem.class);
            if (stringItem == null) {
                return ItemFactory.getInstance().createStringItem("");
            }
            IntegerItem indexItem = this.getSingleItemOfTypeFromIterator(this._children.get(1), IntegerItem.class);
            if (indexItem == null) {
                throw new UnexpectedTypeException("Type error; Start index parameter can't be empty sequence ", getMetadata());
            }
            int index = sanitizeIndexItem(indexItem);
            if (this._children.size() > 2) {
                IntegerItem endIndexItem = this.getSingleItemOfTypeFromIterator(this._children.get(2), IntegerItem.class);
                if (endIndexItem == null) {
                    throw new UnexpectedTypeException("Type error; End index parameter can't be empty sequence ", getMetadata());
                }
                int endIndex = sanitizeEndIndex(stringItem, endIndexItem, index);
                result = stringItem.getStringValue().substring(index, endIndex);
            } else {
                result = stringItem.getStringValue().substring(index);
            }

            return ItemFactory.getInstance().createStringItem(result);
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring function", getMetadata());
    }

    private int sanitizeEndIndex(StringItem stringItem, IntegerItem endIndexItem, int startIndex) {
        //char indexing starts from 1 in JSONiq
        return Math.min(stringItem.getStringValue().length(), startIndex + endIndexItem.getIntegerValue());
    }

    private int sanitizeIndexItem(IntegerItem indexItem) {
        //char indexing starts from 1 in JSONiq
        return indexItem.getIntegerValue() - 1 > 0 ? indexItem.getIntegerValue() - 1 : 0;
    }
}
