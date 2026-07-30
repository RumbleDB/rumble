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
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigInteger;
import java.util.List;

public class CompareFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public CompareFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        if (this.children.size() == 3) {
            String collation = this.children.get(2).materializeFirstItemOrNull(context).getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new UnsupportedCollationException("Wrong collation parameter", getMetadata());
            }
        }
        Item firstStringItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        Item secondStringItem = this.children.get(1)
            .materializeFirstItemOrNull(context);
        if (firstStringItem == null || secondStringItem == null) {
            return null;
        }
        int result = Integer.signum(
            firstStringItem.getStringValue()
                .compareTo(
                    secondStringItem.getStringValue()
                )
        );
        return ItemFactory.getInstance().createIntegerItem(BigInteger.valueOf(result));
    }
}
