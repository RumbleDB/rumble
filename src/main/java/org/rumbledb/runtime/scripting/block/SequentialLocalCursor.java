/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.scripting.block;

import java.util.List;

import lombok.NonNull;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Drains a sequence of side-effecting plans before streaming a result plan.
 */
final class SequentialLocalCursor<T> extends AbstractLocalCursor<T> {

    private final List<? extends RuntimePlan<?>> prefixPlans;
    private final RuntimePlan<T> resultPlan;
    private final DynamicContext context;
    private Cursor<T> resultCursor;

    public SequentialLocalCursor(
            @NonNull List<? extends RuntimePlan<?>> prefixPlans,
            @NonNull RuntimePlan<T> resultPlan,
            @NonNull DynamicContext context,
            @NonNull ExceptionMetadata metadata) {
        super(metadata);
        this.prefixPlans = List.copyOf(prefixPlans);
        this.resultPlan = resultPlan;
        this.context = context;
    }

    @Override
    protected void openLocal() {
        for (RuntimePlan<?> prefixPlan : this.prefixPlans) {
            drain(prefixPlan);
        }
        this.resultCursor = this.resultPlan.getCursor(this.context);
    }

    private <V> void drain(RuntimePlan<V> plan) {
        try (Cursor<V> cursor = plan.getCursor(this.context)) {
            while (cursor.hasNext()) {
                cursor.next();
            }
        }
    }

    @Override
    protected boolean hasNextLocal() {
        return this.resultCursor.hasNext();
    }

    @Override
    protected T nextLocal() {
        return this.resultCursor.next();
    }

    @Override
    protected void closeLocal() {
        if (this.resultCursor != null) {
            this.resultCursor.close();
        }
        this.resultCursor = null;
    }
}
