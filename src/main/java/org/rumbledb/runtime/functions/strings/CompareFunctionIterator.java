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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.misc.CollationSupport;

import java.io.Serial;
import java.math.BigInteger;
import java.util.List;

public class CompareFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public CompareFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ComputedLocalCursor.fromArguments(this.getChildren(), context, this::evaluate, getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(
            ComputedLocalCursor.arguments(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            )
        );
    }

    private Item evaluate(ComputedLocalCursor.Arguments<Item> arguments) {
        String collation = arguments.size() == 3
            ? arguments.get(2).getStringValue()
            : getRuntimeStaticContext().getDefaultCollation();
        Item firstStringItem = arguments.get(0);
        Item secondStringItem = arguments.get(1);
        if (firstStringItem == null || secondStringItem == null) {
            return null;
        }
        int result = Integer.signum(
            CollationSupport.compareStrings(
                firstStringItem.getStringValue(),
                secondStringItem.getStringValue(),
                collation,
                getMetadata()
            )
        );
        return ItemFactory.getInstance().createIntegerItem(BigInteger.valueOf(result));
    }
}
