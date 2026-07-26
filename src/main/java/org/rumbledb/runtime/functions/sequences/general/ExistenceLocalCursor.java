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
 */

package org.rumbledb.runtime.functions.sequences.general;

import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;

/**
 * Local cursor for testing whether a sequence is empty or non-empty.
 */
final class ExistenceLocalCursor extends AtMostOneLocalCursor<Item> {

    private final RuntimePlan<Item> childPlan;
    private final DynamicContext context;
    private final boolean expectedToExist;

    private ExistenceLocalCursor(
            @NonNull RuntimePlan<Item> childPlan,
            @NonNull DynamicContext context,
            boolean expectedToExist
    ) {
        this.childPlan = childPlan;
        this.context = context;
        this.expectedToExist = expectedToExist;
    }

    public static ExistenceLocalCursor empty(RuntimePlan<Item> childPlan, DynamicContext context) {
        return new ExistenceLocalCursor(childPlan, context, false);
    }

    public static ExistenceLocalCursor exists(RuntimePlan<Item> childPlan, DynamicContext context) {
        return new ExistenceLocalCursor(childPlan, context, true);
    }

    @Override
    protected Item materializeFirstItemOrNull() {
        boolean exists = LocalCursorUtils.materializeFirst(this.childPlan, this.context) != null;
        return ItemFactory.getInstance().createBooleanItem(exists == this.expectedToExist);
    }
}
