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
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.misc.CollationSupport;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.math.BigInteger;
import java.util.List;

public class CompareFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public CompareFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(context);
    }

    private Item evaluate(DynamicContext context) {
        String collation = this.getChildren().size() == 3
            ? this.getChild(2).materializeFirstOrNull(context).getStringValue()
            : getRuntimeStaticContext().getDefaultCollation();
        Item firstStringItem = this.getChild(0).materializeFirstOrNull(context);
        Item secondStringItem = this.getChild(1).materializeFirstOrNull(context);
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
