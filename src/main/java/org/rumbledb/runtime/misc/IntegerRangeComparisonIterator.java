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
package org.rumbledb.runtime.misc;

import java.io.Serial;
import java.math.BigInteger;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * General comparison of an integer singleton with a direct integer range.
 *
 * General comparisons are existential: x < (a to b) asks whether any range value exceeds x.
 * For a nonempty range this is simply x < b, so no range items or Spark join are needed.
 * The compiler selects this plan only for integer singletons; other numeric types retain
 * the ordinary comparison path and its numeric-promotion rules.
 */
public final class IntegerRangeComparisonIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final RangeOperationIterator range;
    private final ItemRuntimePlan scalar;
    private final boolean rangeOnLeft;
    private final ComparisonOperator operator;

    public IntegerRangeComparisonIterator(
            RangeOperationIterator range,
            ItemRuntimePlan scalar,
            boolean rangeOnLeft,
            ComparisonOperator operator,
            RuntimeStaticContext context) {
        // Preserve source order for child traversal as well as evaluation.
        super(rangeOnLeft ? List.of(range, scalar) : List.of(scalar, range), context);
        this.range = range;
        this.scalar = scalar;
        this.rangeOnLeft = rangeOnLeft;
        this.operator = operator;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        RangeOperationIterator.Bounds bounds;
        Item item;

        // Integer type does not imply side-effect-free evaluation. Preserve source order
        // when evaluating the scalar and range endpoints, as in ordinary comparisons.
        if (this.rangeOnLeft) {
            bounds = this.range.getBounds(context);
            item = this.scalar.materializeAtMostOne(context);
        } else {
            item = this.scalar.materializeAtMostOne(context);
            bounds = this.range.getBounds(context);
        }

        // An empty operand supplies no matching pair, even for !=.
        if (item == null || bounds.size().signum() == 0) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        BigInteger value = item.castToIntegerValue();

        // Compare the scalar with both endpoints. rangeOnLeft reverses the inequalities.
        int first = value.compareTo(bounds.first());
        int last = value.compareTo(bounds.last());
        boolean result =
                switch (this.operator) {
                    case GC_EQ -> first >= 0 && last <= 0;
                        // != means some value differs, not that the scalar is absent from the range.
                    case GC_NE -> !bounds.first().equals(bounds.last()) || first != 0;
                    case GC_LT -> this.rangeOnLeft ? first > 0 : last < 0;
                    case GC_LE -> this.rangeOnLeft ? first >= 0 : last <= 0;
                    case GC_GT -> this.rangeOnLeft ? last < 0 : first > 0;
                    case GC_GE -> this.rangeOnLeft ? last <= 0 : first >= 0;
                    default -> throw new IllegalStateException("Expected a general comparison");
                };
        return ItemFactory.getInstance().createBooleanItem(result);
    }
}
