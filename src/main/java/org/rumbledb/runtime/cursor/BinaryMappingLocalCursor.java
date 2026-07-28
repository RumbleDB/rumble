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

import java.util.function.BiFunction;

import lombok.NonNull;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * At-most-one cursor that combines one value from each of two plans.
 *
 * @param <L> the left input type
 * @param <R> the right input type
 * @param <O> the output type
 */
public final class BinaryMappingLocalCursor<L, R, O> extends AtMostOneLocalCursor<O> {

    private final RuntimePlan<L> leftPlan;
    private final RuntimePlan<R> rightPlan;
    private final DynamicContext context;
    private final BiFunction<? super L, ? super R, ? extends O> mapper;
    private final boolean evaluateRightWhenLeftIsEmpty;

    private BinaryMappingLocalCursor(
            RuntimePlan<L> leftPlan,
            RuntimePlan<R> rightPlan,
            DynamicContext context,
            BiFunction<? super L, ? super R, ? extends O> mapper,
            boolean evaluateRightWhenLeftIsEmpty,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.leftPlan = leftPlan;
        this.rightPlan = rightPlan;
        this.context = context;
        this.mapper = mapper;
        this.evaluateRightWhenLeftIsEmpty = evaluateRightWhenLeftIsEmpty;
    }

    /**
     * Creates a cursor that skips the right plan when the left plan is empty.
     *
     * @param leftPlan the left input plan
     * @param rightPlan the right input plan
     * @param context the evaluation context
     * @param mapper the function applied when both plans produce a value
     * @return the cursor
     */
    public static <L, R, O> BinaryMappingLocalCursor<L, R, O> shortCircuiting(
            @NonNull RuntimePlan<L> leftPlan,
            @NonNull RuntimePlan<R> rightPlan,
            @NonNull DynamicContext context,
            @NonNull BiFunction<? super L, ? super R, ? extends O> mapper,
            @NonNull ExceptionMetadata metadata
    ) {
        return new BinaryMappingLocalCursor<>(leftPlan, rightPlan, context, mapper, false, metadata);
    }

    /**
     * Creates a cursor that evaluates both plans from left to right, even when the left plan is empty.
     *
     * @param leftPlan the left input plan
     * @param rightPlan the right input plan
     * @param context the evaluation context
     * @param mapper the function applied when both plans produce a value
     * @return the cursor
     */
    public static <L, R, O> BinaryMappingLocalCursor<L, R, O> eager(
            @NonNull RuntimePlan<L> leftPlan,
            @NonNull RuntimePlan<R> rightPlan,
            @NonNull DynamicContext context,
            @NonNull BiFunction<? super L, ? super R, ? extends O> mapper,
            @NonNull ExceptionMetadata metadata
    ) {
        return new BinaryMappingLocalCursor<>(leftPlan, rightPlan, context, mapper, true, metadata);
    }

    @Override
    protected O materializeFirstItemOrNull() {
        L left = this.leftPlan.materializeFirstOrNull(this.context);
        if (left == null && !this.evaluateRightWhenLeftIsEmpty) {
            return null;
        }
        R right = this.rightPlan.materializeFirstOrNull(this.context);
        if (left == null || right == null) {
            return null;
        }
        return this.mapper.apply(left, right);
    }
}
