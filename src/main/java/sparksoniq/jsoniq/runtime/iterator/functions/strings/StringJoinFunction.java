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
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class StringJoinFunction extends LocalFunctionCallIterator {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StringJoinFunction(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item joinString = ItemFactory.getInstance().createStringItem("");
            List<Item> strings = getItemsFromIteratorWithCurrentContext(this._children.get(0));
            if (this._children.size() > 1) {
                RuntimeIterator joinStringIterator = this._children.get(1);
                joinStringIterator.open(_currentDynamicContext);
                if (joinStringIterator.hasNext()) {
                    Item item = joinStringIterator.next();
                    joinString = (StringItem) item;
                }
            }

            StringBuilder stringBuilder = new StringBuilder("");
            for (Item item : strings) {
                if (!(item.isString()))
                    throw new UnexpectedTypeException("String item expected", this._children.get(0).getMetadata());
                stringBuilder = !stringBuilder.toString().isEmpty() ? stringBuilder.append(joinString.getStringValue()) : stringBuilder;
                stringBuilder = stringBuilder.append(((StringItem) item).getStringValue());
            }
            this._hasNext = false;
            return ItemFactory.getInstance().createStringItem(stringBuilder.toString());
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " string-join function", getMetadata());
    }
}
