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
package org.rumbledb.runtime.functions.random;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

public class RandomSequenceWithBoundsAndSeedIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    public RandomSequenceWithBoundsAndSeedIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> createRandomNumberStream(context), getMetadata());
    }

    private GeneratedRandomsIterator createRandomNumberStream(DynamicContext context) {
        return createRandomNumberStream(
                this.getChild(0).materializeFirstOrNull(context),
                this.getChild(1).materializeFirstOrNull(context),
                this.getChild(2).materializeFirstOrNull(context).castToIntValue(),
                this.getChild(3).materializeFirstOrNull(context),
                this.getChild(4).materializeFirstOrNull(context).castToIntValue());
    }

    private GeneratedRandomsIterator createRandomNumberStream(Item low, Item high, int size, Item type, int seed) {
        if (type.getStringValue().equals("integer")) {
            return new GeneratedRandomIntegersIterator(size, low.castToIntValue(), high.castToIntValue(), seed);
        } else {
            // Generate doubles otherwise
            return new GeneratedRandomDoublesIterator(size, low.castToDoubleValue(), high.castToDoubleValue(), seed);
        }
    }
}
