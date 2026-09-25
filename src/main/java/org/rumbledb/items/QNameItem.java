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
import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/**
 * Atomic item representing an {@code xs:QName} value as an expanded name ({@link Name}).
 */
public class QNameItem extends AbstractAtomicItem {

    @Serial
    private static final long serialVersionUID = 1L;

    private Name name;

    public QNameItem(Name name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public Item copy(boolean mutable) {
        return new QNameItem(this.name);
    }

    @Override
    public String getStringValue() {
        return this.name.toString();
    }

    @Override
    public Object getVariantValue() {
        return this.name;
    }

    @Override
    public boolean isQName() {
        return true;
    }

    @Override
    public Name getQNameValue() {
        return this.name;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.QNameItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
