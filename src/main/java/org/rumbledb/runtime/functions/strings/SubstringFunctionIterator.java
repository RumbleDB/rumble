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
package org.rumbledb.runtime.functions.strings;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class SubstringFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public SubstringFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item stringItem = this.getChild(0).materializeFirstOrNull(context);
        if (stringItem == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        Item indexItem = this.getChild(1).materializeFirstOrNull(context);
        if (indexItem == null) {
            throw new UnexpectedTypeException(
                    "Type error; Start index parameter can't be empty sequence ", getMetadata());
        }
        double start = roundPosition(indexItem.getDoubleValue());
        double end = Double.POSITIVE_INFINITY;
        if (this.getChildren().size() > 2) {
            Item lengthItem = this.getChild(2).materializeFirstOrNull(context);
            if (lengthItem == null) {
                throw new UnexpectedTypeException(
                        "Type error; End index parameter can't be empty sequence ", getMetadata());
            }
            end = start + roundPosition(lengthItem.getDoubleValue());
        }

        String value = stringItem.getStringValue();
        int characterCount = value.codePointCount(0, value.length());
        // Keep positions as doubles until clamping so NaN, infinities and very large values
        // retain their specified comparison behavior. Positions count Unicode code points.
        if (Double.isNaN(start) || Double.isNaN(end) || start >= end || start > characterCount || end <= 1) {
            return ItemFactory.getInstance().createStringItem("");
        }
        int from = (int) Math.max(0, start - 1);
        int to = (int) Math.min(characterCount, end - 1);
        String result = value.substring(value.offsetByCodePoints(0, from), value.offsetByCodePoints(0, to));
        return ItemFactory.getInstance().createStringItem(result);
    }

    private static double roundPosition(double value) {
        double floor = Math.floor(value);
        return value - floor >= 0.5 ? floor + 1 : floor;
    }
}
