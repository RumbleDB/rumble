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

import java.util.NoSuchElementException;
import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

/**
 * Local cursor that evaluates a condition and delegates to exactly one branch.
 *
 * @param <T> the branch value type
 */
final class ConditionalLocalCursor<T> extends AbstractLocalCursor<T> {

    private final RuntimePlan<Item> conditionPlan;
    private final RuntimePlan<T> thenPlan;
    private final RuntimePlan<T> elsePlan;
    private final DynamicContext context;
    private LocalCursor<T> selectedCursor;

    public ConditionalLocalCursor(
            RuntimePlan<Item> conditionPlan,
            RuntimePlan<T> thenPlan,
            RuntimePlan<T> elsePlan,
            DynamicContext context
    ) {
        this.conditionPlan = Objects.requireNonNull(conditionPlan, "condition plan cannot be null");
        this.thenPlan = Objects.requireNonNull(thenPlan, "then plan cannot be null");
        this.elsePlan = Objects.requireNonNull(elsePlan, "else plan cannot be null");
        this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
    }

    @Override
    protected void openLocal() {
        RuntimePlan<T> selectedPlan = EffectiveBooleanValue.evaluate(this.conditionPlan, this.context)
            ? this.thenPlan
            : this.elsePlan;
        this.selectedCursor = selectedPlan.createLocalCursor(this.context);
        this.selectedCursor.open();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.selectedCursor.hasNext();
    }

    @Override
    protected T nextLocal() {
        if (!this.selectedCursor.hasNext()) {
            throw new NoSuchElementException("Conditional cursor is exhausted.");
        }
        return this.selectedCursor.next();
    }

    @Override
    protected void closeLocal() {
        if (this.selectedCursor != null) {
            this.selectedCursor.close();
            this.selectedCursor = null;
        }
    }
}
