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

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import org.rumbledb.exceptions.ExceptionMetadata;

import java.util.List;

public class StringJoinFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public StringJoinFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item joinString = ItemFactory.getInstance().createStringItem("");
            List<Item> strings = this._children.get(0).materialize(_currentDynamicContextForLocalExecution);
            if (this._children.size() > 1) {
                RuntimeIterator joinStringIterator = this._children.get(1);
                joinStringIterator.open(_currentDynamicContextForLocalExecution);
                if (joinStringIterator.hasNext()) {
                    joinString = joinStringIterator.next();
                }
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (Item item : strings) {
                if (!(item.isString()))
                    throw new UnexpectedTypeException("String item expected", this._children.get(0).getMetadata());
                if (!stringBuilder.toString().isEmpty()) {
                    stringBuilder.append(joinString.getStringValue());
                }
                stringBuilder.append(item.getStringValue());
            }
            this._hasNext = false;
            return ItemFactory.getInstance().createStringItem(stringBuilder.toString());
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " string-join function",
                    getMetadata()
            );
    }
}
