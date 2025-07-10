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

package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class StringJoinFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public StringJoinFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item joinString = ItemFactory.getInstance().createStringItem("");
        if (this.children.size() > 1) {
            joinString = this.children.get(1).materializeFirstItemOrNull(context);
        }
        List<Item> strings = this.children.get(0).materialize(context);

        StringBuilder stringBuilder = new StringBuilder();
        for (Item item : strings) {
            if (!(item.isString())) {
                throw new UnexpectedTypeException("String item expected", this.children.get(0).getMetadata());
            }
            if (!stringBuilder.toString().isEmpty()) {
                stringBuilder.append(joinString.getStringValue());
            }
            stringBuilder.append(item.getStringValue());
        }

        return ItemFactory.getInstance().createStringItem(stringBuilder.toString());
    }


}
