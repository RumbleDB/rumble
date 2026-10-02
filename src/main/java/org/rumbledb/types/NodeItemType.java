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
 * Class representing the abstract node() item type.
 * This is the supertype for all 7 concrete XML node types
 * (element, attribute, document-node, comment, text, namespace-node, processing-instruction)
 * as defined by the XPath Data Model 3.1, Section 2.7.4.
 *
 * node() sits at depth 1 in the type hierarchy, with item at depth 0.
 */
public class NodeItemType extends AbstractItemType {

    @Serial
    private static final long serialVersionUID = 1L;

    static final ItemType nodeItem = new NodeItemType();
    private final Name name;

    NodeItemType() {
        this.name = Name.createVariableInDefaultTypeNamespace("node");
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
    public boolean isNodeItemType() {
        return true;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        return superType.equals(BuiltinTypesCatalogue.item) || superType.equals(nodeItem);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        while (other.getTypeTreeDepth() > 1) {
            other = other.getBaseType();
        }
        return other.equals(nodeItem) ? nodeItem : BuiltinTypesCatalogue.item;
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
        throw new UnsupportedOperationException("node item type does not support facets");
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
