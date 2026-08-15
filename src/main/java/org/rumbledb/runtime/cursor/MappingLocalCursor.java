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

import java.util.function.Function;

import lombok.NonNull;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Cursor that lazily applies a one-to-one transformation to another local plan.
 *
 * @param <I> the input type
 * @param <O> the output type
 */
public final class MappingLocalCursor<I, O> extends AbstractLocalCursor<O> {

    private final RuntimePlan<I> inputPlan;
    private final DynamicContext context;
    private final Function<? super I, ? extends O> mapper;
    private Cursor<I> inputCursor;

    public MappingLocalCursor(
            @NonNull RuntimePlan<I> inputPlan,
            @NonNull DynamicContext context,
            @NonNull Function<? super I, ? extends O> mapper,
            @NonNull ExceptionMetadata metadata) {
        super(metadata);
        this.inputPlan = inputPlan;
        this.context = context;
        this.mapper = mapper;
    }

    @Override
    protected void openLocal() {
        this.inputCursor = this.inputPlan.getCursor(this.context);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.inputCursor.hasNext();
    }

    @Override
    protected O nextLocal() {
        return this.mapper.apply(this.inputCursor.next());
    }

    @Override
    protected void closeLocal() {
        if (this.inputCursor != null) {
            this.inputCursor.close();
            this.inputCursor = null;
        }
    }
}
