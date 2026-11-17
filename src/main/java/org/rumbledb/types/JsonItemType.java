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
package org.rumbledb.types;

import java.io.Serial;
import java.util.Set;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;

/**
 * Class representing the generic 'item' item type
 */
public class JsonItemType extends AbstractItemType {

    @Serial
    private static final long serialVersionUID = 1L;

    static final ItemType jsonItem = new JsonItemType();
    private final Name name;

    JsonItemType() {
        this.name = new Name(Name.JS_NS, "js", "json-item");
    }

    @Override
    public boolean hasName() {
        return true;
    }

    @Override
    public Name getName() {
        return this.name;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        return superType.equals(BuiltinTypesCatalogue.item) || superType.equals(jsonItem);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        while (other.getTypeTreeDepth() > 1) {
            other = other.getBaseType();
        }
        return other.equals(jsonItem) ? jsonItem : BuiltinTypesCatalogue.item;
    }

    @Override
    public int getTypeTreeDepth() {
        return 1;
    }

    @Override
    public ItemType getBaseType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("item type does not support facets");
    }

    @Override
    public String toString() {
        return this.name.toString();
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleConfiguration configuration) {
        return false;
    }
}
