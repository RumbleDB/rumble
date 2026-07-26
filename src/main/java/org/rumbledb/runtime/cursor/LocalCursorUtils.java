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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.NonNull;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.runtime.plan.RuntimePlan;

public final class LocalCursorUtils {

    private LocalCursorUtils() {
    }

    /**
     * Evaluates a plan locally and materializes all its values.
     *
     * @param plan the plan to evaluate
     * @param context the dynamic context for the evaluation
     * @param <T> the value type
     * @return the materialized values
     */
    public static <T> List<T> materialize(@NonNull RuntimePlan<T> plan, @NonNull DynamicContext context) {
        List<T> result = new ArrayList<>();
        try (LocalCursor<T> cursor = plan.createLocalCursor(context)) {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        }
        return result;
    }

    /**
     * Evaluates a plan locally and returns its first value, or {@code null} for an empty sequence.
     *
     * @param plan the plan to evaluate
     * @param context the dynamic context for the evaluation
     * @param <T> the value type
     * @return the first value, or {@code null}
     */
    public static <T> T materializeFirst(@NonNull RuntimePlan<T> plan, @NonNull DynamicContext context) {
        try (LocalCursor<T> cursor = plan.createLocalCursor(context)) {
            return cursor.hasNext() ? cursor.next() : null;
        }
    }

    /**
     * Evaluates a plan locally and returns its single value, or {@code null} for an empty sequence.
     *
     * @param plan the plan to evaluate
     * @param context the dynamic context for the evaluation
     * @param <T> the value type
     * @return the single value, or {@code null}
     * @throws MoreThanOneItemException if the plan produces more than one value
     */
    public static <T> T materializeAtMostOne(RuntimePlan<T> plan, DynamicContext context)
            throws MoreThanOneItemException {
        Objects.requireNonNull(plan, "plan cannot be null");
        Objects.requireNonNull(context, "dynamic context cannot be null");
        try (LocalCursor<T> cursor = plan.createLocalCursor(context)) {
            if (!cursor.hasNext()) {
                return null;
            }
            T result = cursor.next();
            if (cursor.hasNext()) {
                throw new MoreThanOneItemException();
            }
            return result;
        }
    }

    /**
     * Evaluates a plan locally and substitutes a default value for an empty sequence.
     *
     * @param plan the plan to evaluate
     * @param context the dynamic context for the evaluation
     * @param defaultValue the value returned for an empty sequence
     * @param <T> the value type
     * @return the single value or {@code defaultValue}
     * @throws MoreThanOneItemException if the plan produces more than one value
     */
    public static <T> T materializeAtMostOneOrDefault(
            RuntimePlan<T> plan,
            DynamicContext context,
            T defaultValue
    )
            throws MoreThanOneItemException {
        T result = materializeAtMostOne(plan, context);
        return result == null ? defaultValue : result;
    }
}
