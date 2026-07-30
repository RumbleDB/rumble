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

import org.rumbledb.runtime.plan.EvaluationArguments;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.misc.CollationSupport;

import java.io.Serial;
import java.util.List;

public class StartsWithFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public StartsWithFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(
            EvaluationArguments.lazy(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            )
        );
    }

    private Item evaluate(EvaluationArguments<Item> arguments) {
        String collation = arguments.size() == 3
            ? arguments.get(2).getStringValue()
            : getRuntimeStaticContext().getDefaultCollation();

        Item substringItem = arguments.get(1);
        if (substringItem == null || substringItem.getStringValue().isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(true);
        }
        Item stringItem = arguments.get(0);
        if (stringItem == null || stringItem.getStringValue().isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        boolean result = CollationSupport.startsWith(
            stringItem.getStringValue(),
            substringItem.getStringValue(),
            collation,
            getMetadata()
        );
        return ItemFactory.getInstance().createBooleanItem(result);
    }

}
