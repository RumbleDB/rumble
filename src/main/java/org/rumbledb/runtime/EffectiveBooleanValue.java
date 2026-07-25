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

package org.rumbledb.runtime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;

/**
 * Effective-boolean-value semantics shared by legacy iterators and local cursors.
 */
public final class EffectiveBooleanValue {

    private EffectiveBooleanValue() {
    }

    public static boolean evaluate(RuntimePlan<Item> plan, DynamicContext context) {
        return evaluateOrCheckPosition(plan, context, null);
    }

    public static boolean evaluateOrCheckPosition(
            RuntimePlan<Item> plan,
            DynamicContext context,
            Item position
    ) {
        Objects.requireNonNull(plan, "plan cannot be null");
        Objects.requireNonNull(context, "dynamic context cannot be null");
        try (LocalCursor<Item> cursor = plan.createLocalCursor(context)) {
            cursor.open();
            return evaluateOpenSequence(
                cursor::hasNext,
                cursor::next,
                plan.getRuntimeStaticContext(),
                position
            );
        }
    }

    static boolean evaluateOpenSequence(
            BooleanSupplier hasNext,
            Supplier<Item> next,
            RuntimeStaticContext staticContext,
            Item position
    ) {
        if (!hasNext.getAsBoolean()) {
            return false;
        }

        Item item = next.get();
        boolean result;
        if (item.isBoolean()) {
            result = item.getBooleanValue();
        } else if (item.isNumeric()) {
            result = evaluateNumeric(item, position, staticContext);
        } else if (item.isNull()) {
            result = false;
        } else if (item.getDynamicType().canBePromotedTo(BuiltinTypesCatalogue.stringItem)) {
            result = !item.getStringValue().isEmpty();
        } else if (item.isNode()) {
            return true;
        } else {
            if (staticContext.getQueryLanguage().equals("jsoniq10") && (item.isObject() || item.isArray())) {
                return true;
            }
            if (item.isObject() || item.isArray()) {
                System.err.println(
                    "Note: effective boolean value of "
                        + (item.isObject() ? "Object " : "Array ")
                        + "accessed which throws error in JSONiq 3.1 or 4.0 in alignment with Xquery 3.1 or 4.0 spec.\n"
                        + " If you want to revert to the old functionality use the --default-language jsoniq10 "
                        + "command line option"
                );
            }
            throw new InvalidArgumentTypeException(
                    "Effective boolean value not defined for items of type " + item.getDynamicType(),
                    staticContext.getMetadata()
            );
        }

        if (hasNext.getAsBoolean()) {
            throw new InvalidArgumentTypeException(
                    "Effective boolean value not defined for sequences of more than one atomic item. "
                        + "Sequence containing: "
                        + item.serialize()
                        + " must be a singleton.",
                    staticContext.getMetadata()
            );
        }
        return result;
    }

    private static boolean evaluateNumeric(
            Item item,
            Item position,
            RuntimeStaticContext staticContext
    ) {
        if (position != null) {
            return ComparisonIterator.compareItems(
                item,
                position,
                ComparisonOperator.VC_EQ,
                staticContext.getMetadata()
            ) == 0;
        }
        if (item.isInt()) {
            return item.getIntValue() != 0;
        }
        if (item.isInteger()) {
            return !item.getIntegerValue().equals(BigInteger.ZERO);
        }
        if (item.isDouble()) {
            return !item.isNaN() && item.getDoubleValue() != 0;
        }
        if (item.isFloat()) {
            return !item.isNaN() && item.getFloatValue() != 0;
        }
        if (item.isDecimal()) {
            return item.getDecimalValue().compareTo(BigDecimal.ZERO) != 0;
        }
        throw new OurBadException("Unexpected numeric type found while calculating effective boolean value.");
    }
}
