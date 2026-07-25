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

import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Local cursor for boolean operators based on effective boolean values.
 */
public final class BooleanLocalCursor extends AtMostOneLocalCursor<Item> {

    private enum Operator {
        AND,
        OR,
        VALUE,
        NOT
    }

    private final Operator operator;
    private final RuntimePlan<Item> leftPlan;
    private final RuntimePlan<Item> rightPlan;
    private final DynamicContext context;

    private BooleanLocalCursor(
            Operator operator,
            RuntimePlan<Item> leftPlan,
            RuntimePlan<Item> rightPlan,
            DynamicContext context
    ) {
        this.operator = operator;
        this.leftPlan = Objects.requireNonNull(leftPlan, "left plan cannot be null");
        this.rightPlan = rightPlan;
        this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
    }

    public static BooleanLocalCursor and(
            RuntimePlan<Item> leftPlan,
            RuntimePlan<Item> rightPlan,
            DynamicContext context
    ) {
        return new BooleanLocalCursor(
                Operator.AND,
                leftPlan,
                Objects.requireNonNull(rightPlan, "right plan cannot be null"),
                context
        );
    }

    public static BooleanLocalCursor or(
            RuntimePlan<Item> leftPlan,
            RuntimePlan<Item> rightPlan,
            DynamicContext context
    ) {
        return new BooleanLocalCursor(
                Operator.OR,
                leftPlan,
                Objects.requireNonNull(rightPlan, "right plan cannot be null"),
                context
        );
    }

    public static BooleanLocalCursor not(RuntimePlan<Item> plan, DynamicContext context) {
        return new BooleanLocalCursor(Operator.NOT, plan, null, context);
    }

    public static BooleanLocalCursor value(RuntimePlan<Item> plan, DynamicContext context) {
        return new BooleanLocalCursor(Operator.VALUE, plan, null, context);
    }

    @Override
    protected Item materializeFirstItemOrNull() {
        boolean left = EffectiveBooleanValue.evaluate(this.leftPlan, this.context);
        if (this.operator == Operator.VALUE) {
            return ItemFactory.getInstance().createBooleanItem(left);
        }
        if (this.operator == Operator.NOT) {
            return ItemFactory.getInstance().createBooleanItem(!left);
        }

        // Preserve the legacy eager evaluation of both operands.
        boolean right = EffectiveBooleanValue.evaluate(this.rightPlan, this.context);
        boolean result = this.operator == Operator.AND ? left && right : left || right;
        return ItemFactory.getInstance().createBooleanItem(result);
    }
}
