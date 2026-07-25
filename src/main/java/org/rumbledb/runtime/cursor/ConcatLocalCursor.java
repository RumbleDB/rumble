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

package org.rumbledb.runtime.cursor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Local cursor that concatenates the values produced by an ordered list of child plans.
 *
 * @param <T> the value type
 */
public final class ConcatLocalCursor<T> extends AbstractLocalCursor<T> {

    private final List<? extends RuntimePlan<T>> childPlans;
    private final DynamicContext context;
    private int childIndex;
    private LocalCursor<T> currentChild;
    private T nextValue;
    private boolean hasNext;

    public ConcatLocalCursor(List<? extends RuntimePlan<T>> childPlans, DynamicContext context) {
        this.childPlans = List.copyOf(Objects.requireNonNull(childPlans, "child plans cannot be null"));
        this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
    }

    @Override
    protected void openLocal() {
        this.childIndex = 0;
        this.currentChild = null;
        setNextValue();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected T nextLocal() {
        if (!this.hasNext) {
            throw new NoSuchElementException("Concatenating cursor is exhausted.");
        }
        T result = this.nextValue;
        setNextValue();
        return result;
    }

    @Override
    protected void closeLocal() {
        if (this.currentChild != null) {
            this.currentChild.close();
            this.currentChild = null;
        }
        this.nextValue = null;
        this.hasNext = false;
    }

    private void setNextValue() {
        while (true) {
            if (this.currentChild == null) {
                if (this.childIndex == this.childPlans.size()) {
                    this.nextValue = null;
                    this.hasNext = false;
                    return;
                }
                this.currentChild = this.childPlans.get(this.childIndex).createLocalCursor(this.context);
                this.childIndex++;
                this.currentChild.open();
            }
            if (this.currentChild.hasNext()) {
                this.nextValue = this.currentChild.next();
                this.hasNext = true;
                return;
            }
            this.currentChild.close();
            this.currentChild = null;
        }
    }
}
