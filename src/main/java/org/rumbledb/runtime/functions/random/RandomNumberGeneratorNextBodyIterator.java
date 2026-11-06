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
import java.util.Random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

/**
 * Body of the "next" entry of a random-number-generator map: deterministically derives the next seed in
 * the chain and builds a fresh three-entry map from it.
 */
public class RandomNumberGeneratorNextBodyIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final long seed;

    public RandomNumberGeneratorNextBodyIterator(long seed, RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
        this.seed = seed;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        long nextSeed = new Random(this.seed).nextLong();
        return RandomNumberGeneratorMapBuilder.build(
                nextSeed, this.staticContext, new DynamicContext(context.getRumbleConfiguration()), getMetadata());
    }
}
