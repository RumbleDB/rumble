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

import java.util.PrimitiveIterator;

import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

public class GeneratedRandomIntegersIterator extends GeneratedRandomsIterator {
    private final PrimitiveIterator.OfInt iterator;

    public GeneratedRandomIntegersIterator(int size, int low, int high) {
        this.iterator = this.random.ints(size, low, high).iterator();
    }

    public GeneratedRandomIntegersIterator(int size, int low, int high, int seed) {
        super(seed);
        this.iterator = this.random.ints(size, low, high).iterator();
    }

    @Override
    public Item getNextRandom() {
        return ItemFactory.getInstance().createIntItem(this.iterator.next());
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }
}
