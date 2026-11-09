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
package org.rumbledb.runtime.functions.numerics;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Supplier;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class RoundHalfToEvenFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public RoundHalfToEvenFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item value = this.getChild(0).materializeFirstOrNull(context);
        return evaluate(
                value,
                () -> this.getChildren().size() > 1
                        ? this.getChild(1).materializeFirstOrNull(context).getIntegerValue()
                        : BigInteger.ZERO);
    }

    private Item evaluate(Item value, Supplier<BigInteger> precisionSupplier) {
        if (value == null) {
            return null;
        }
        if ((value.isDouble() && Double.isNaN(value.getDoubleValue()))
                || (value.isFloat() && Float.isNaN(value.getFloatValue()))) {
            return value;
        }
        if ((value.isDouble() && Double.isInfinite(value.getDoubleValue()))
                || (value.isFloat() && Float.isInfinite(value.getFloatValue()))) {
            return value;
        }
        if ((value.isDouble() && Double.compare(value.getDoubleValue(), -0d) == 0
                || (value.isFloat() && Float.compare(value.getFloatValue(), -0f) == 0))) {
            return value;
        }

        BigInteger precision = precisionSupplier.get();
        try {
            if (value.isInt()) {
                BigDecimal bd = roundToPrecision(new BigDecimal(value.getIntValue()), precision);
                return ItemFactory.getInstance().createIntItem(bd.intValue());
            }
            if (value.isInteger()) {
                BigDecimal bd = roundToPrecision(new BigDecimal(value.getIntegerValue()), precision);
                return ItemFactory.getInstance().createIntegerItem(bd.toBigInteger());
            }
            if (value.isDecimal()) {
                BigDecimal bd = roundToPrecision(value.getDecimalValue(), precision);
                return ItemFactory.getInstance().createDecimalItem(bd);
            }
            if (value.isDouble()) {
                double sign = getSign(value.getDoubleValue());
                BigDecimal bd = roundToPrecision(new BigDecimal(value.getDoubleValue()), precision);
                return ItemFactory.getInstance().createDoubleItem(sign * Math.abs(bd.doubleValue()));
            }
            if (value.isFloat()) {
                double sign = getSign(value.getFloatValue());
                BigDecimal bd = roundToPrecision(new BigDecimal(value.getFloatValue()), precision);
                return ItemFactory.getInstance().createFloatItem((float) sign * Math.abs(bd.floatValue()));
            }
            throw new UnexpectedTypeException(
                    "Unexpected value in round-half-to-even(): " + value.getDynamicType(), getMetadata());

        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }
    }

    private static BigDecimal roundToPrecision(BigDecimal value, BigInteger precision) {
        // Large positive precisions leave the value unchanged; large negative ones round to zero.
        // Check these bounds before narrowing xs:integer or allocating a power of ten in setScale().
        if (precision.compareTo(BigInteger.valueOf(value.scale())) >= 0) {
            return value;
        }
        long zeroPrecision = (long) value.scale() - value.precision();
        if (precision.compareTo(BigInteger.valueOf(zeroPrecision)) < 0) {
            return BigDecimal.ZERO;
        }
        return value.setScale(precision.intValueExact(), RoundingMode.HALF_EVEN);
    }

    private double getSign(double doubleValue) {
        double sign = 0;
        if (doubleValue > 0) sign = 1;
        if (doubleValue < 0) sign = -1;
        return sign;
    }
}
