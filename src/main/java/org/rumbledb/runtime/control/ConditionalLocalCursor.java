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

package org.rumbledb.runtime.control;

import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.cursor.AbstractDelegatingLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

/**
 * Local cursor that evaluates a condition and delegates to exactly one branch.
 *
 * @param <T> the branch value type
 */
final class ConditionalLocalCursor<T> extends AbstractDelegatingLocalCursor<T> {

    private final RuntimePlan<Item> conditionPlan;
    private final RuntimePlan<T> thenPlan;
    private final RuntimePlan<T> elsePlan;
    private final DynamicContext context;

    public ConditionalLocalCursor(
            RuntimePlan<Item> conditionPlan,
            RuntimePlan<T> thenPlan,
            RuntimePlan<T> elsePlan,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.conditionPlan = Objects.requireNonNull(conditionPlan, "condition plan cannot be null");
        this.thenPlan = Objects.requireNonNull(thenPlan, "then plan cannot be null");
        this.elsePlan = Objects.requireNonNull(elsePlan, "else plan cannot be null");
        this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
    }

    @Override
    protected Cursor<T> createDelegateCursor() {
        RuntimePlan<T> selectedPlan = EffectiveBooleanValue.evaluate(this.conditionPlan, this.context)
            ? this.thenPlan
            : this.elsePlan;
        return selectedPlan.getCursor(this.context);
    }
}
