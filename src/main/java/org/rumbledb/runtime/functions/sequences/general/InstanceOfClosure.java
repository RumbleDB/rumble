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
package org.rumbledb.runtime.functions.sequences.general;

import java.io.Serial;

import org.apache.spark.api.java.function.Function;

import org.rumbledb.api.Item;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.ItemType;

public class InstanceOfClosure implements Function<Item, Boolean> {
    private final ItemType itemType;

    @Serial
    private static final long serialVersionUID = 1L;

    public InstanceOfClosure(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public Boolean call(Item input) throws Exception {
        return !InstanceOfIterator.doesItemTypeMatchItem(this.itemType, input);
    }
}
