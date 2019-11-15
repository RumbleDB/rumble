/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

import org.rumbledb.api.Item;

public class ConcatFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public ConcatFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            StringBuilder builder = new StringBuilder("");
            for (RuntimeIterator iterator : this._children) {
                Item item = this.getSingleItemOfTypeFromIterator(iterator, Item.class);
                // if not empty sequence
                if (item != null) {
                    String stringValue = "";
                    if (item.isAtomic()) {
                        stringValue = item.serialize(); // for atomic items (not array or object) returns the equivalent
                                                        // string value
                    } else {
                        throw new UnexpectedTypeException(
                                "String concat function has arguments that can't be converted to a string "
                                    +
                                    item.serialize(),
                                getMetadata()
                        );
                    }
                    if (stringValue != "") {
                        builder.append(stringValue);
                    }
                }
            }
            this._hasNext = false;
            return ItemFactory.getInstance().createStringItem(builder.toString());
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring function",
                    getMetadata()
            );
    }
}
