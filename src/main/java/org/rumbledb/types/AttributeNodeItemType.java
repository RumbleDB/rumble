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
import java.util.List;
import java.util.Set;

import lombok.Getter;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;

/**
 * Class representing attribute() and attribute(QName) item types.
 *
 * Wildcard attribute() is represented with no node-name restriction.
 * attribute(QName) is represented with a concrete node-name restriction.
 */
public class AttributeNodeItemType extends AbstractItemType {

    @Serial
    private static final long serialVersionUID = 1L;

    private Name catalogueName;

    @Getter
    private Name nodeName;

    @Getter
    private Name schemaTypeName;

    private List<Name> schemaTypeHierarchy;

    public AttributeNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("attribute");
        this.nodeName = null;
        this.schemaTypeName = null;
        this.schemaTypeHierarchy = List.of();
    }

    public AttributeNodeItemType(Name nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("Attribute node name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeName = null;
        this.schemaTypeHierarchy = List.of();
    }

    public AttributeNodeItemType(Name nodeName, Name schemaTypeName, List<Name> schemaTypeHierarchy) {
        if (schemaTypeName == null || schemaTypeHierarchy == null || schemaTypeHierarchy.isEmpty()) {
            throw new IllegalArgumentException("A typed attribute test requires a schema type hierarchy.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeName = schemaTypeName;
        this.schemaTypeHierarchy = List.copyOf(schemaTypeHierarchy);
    }

    private boolean isWildcardAttribute() {
        return this.nodeName == null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(AttributeNodeItemType.class, this.catalogueName, this.nodeName, this.schemaTypeName);
    }

    @Override
    public boolean hasName() {
        return this.catalogueName != null;
    }

    @Override
    public Name getName() {
        if (this.catalogueName == null) {
            throw new UnsupportedOperationException("Named attribute node item type has no builtin QName");
        }
        return this.catalogueName;
    }

    @Override
    public boolean isNodeItemType() {
        return true;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (superType.isUnionType()) {
            for (ItemType unionItemType : superType.getTypes()) {
                if (this.isSubtypeOf(unionItemType)) {
                    return true;
                }
            }
        }
        if (this.equals(superType)
                || superType.equals(BuiltinTypesCatalogue.item)
                || superType.equals(BuiltinTypesCatalogue.nodeItem)) {
            return true;
        }
        if (!(superType instanceof AttributeNodeItemType other)) {
            return false;
        }
        if (other.isWildcardAttribute()) {
            return other.schemaTypeName == null || hasCompatibleSchemaType(other);
        }
        return this.nodeName != null
                && this.nodeName.equals(other.nodeName)
                && (other.schemaTypeName == null || hasCompatibleSchemaType(other));
    }

    private boolean hasCompatibleSchemaType(AttributeNodeItemType superType) {
        return this.schemaTypeName != null && this.schemaTypeHierarchy.contains(superType.schemaTypeName);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other instanceof AttributeNodeItemType) {
            return BuiltinTypesCatalogue.attributeNode;
        }
        ItemType current = this;
        ItemType o = other;
        while (o.getTypeTreeDepth() > current.getTypeTreeDepth()) {
            o = o.getBaseType();
        }
        while (o.getTypeTreeDepth() < current.getTypeTreeDepth()) {
            current = current.getBaseType();
        }
        while (!current.equals(o)) {
            current = current.getBaseType();
            o = o.getBaseType();
        }
        return current;
    }

    @Override
    public int getTypeTreeDepth() {
        return 2;
    }

    @Override
    public ItemType getBaseType() {
        return BuiltinTypesCatalogue.nodeItem;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("attribute node item type does not support facets");
    }

    @Override
    public String toString() {
        if (this.catalogueName != null) {
            return this.catalogueName.toString();
        }
        String name = this.nodeName == null ? "*" : this.nodeName.toString();
        return this.schemaTypeName == null
                ? "attribute(" + name + ")"
                : "attribute(" + name + ", " + this.schemaTypeName + ")";
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
