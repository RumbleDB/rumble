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
package org.rumbledb.items;

import java.io.Serial;

import org.rumbledb.api.Item;

/**
 * Base class that gives every atomic item one Java equality and hashing contract.
 */
public abstract class AbstractAtomicItem implements Item {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public final boolean equals(Object other) {
        return other instanceof Item item && AtomicItemEquivalence.equivalent(this, item);
    }

    @Override
    public final int hashCode() {
        return AtomicItemEquivalence.hash(this);
    }
}
