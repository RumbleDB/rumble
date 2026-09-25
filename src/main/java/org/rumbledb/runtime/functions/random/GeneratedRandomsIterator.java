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

import java.util.Iterator;
import java.util.Random;

import org.rumbledb.api.Item;

public abstract class GeneratedRandomsIterator implements Iterator<Item> {
    protected Random random;

    protected GeneratedRandomsIterator() {
        this.random = new Random();
    }

    protected GeneratedRandomsIterator(int seed) {
        this.random = new Random();
        this.random.setSeed(seed);
    }

    public abstract Item getNextRandom();

    @Override
    public final Item next() {
        return getNextRandom();
    }

    @Override
    public abstract boolean hasNext();
}
