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
 * Class representing element() and element(QName) item types.
 *
 * Wildcard element() is represented with no node-name restriction.
 * element(QName) is represented with a concrete node-name restriction.
 */
public class ElementNodeItemType extends AbstractItemType {

    @Serial
    private static final long serialVersionUID = 1L;

    private Name catalogueName;

    @Getter
    private Name nodeName;

    @Getter
    private Name schemaTypeName;

    private final List<Name> schemaTypeHierarchy;

    @Getter
    private final boolean nillable;

    public ElementNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("element");
        this.nodeName = null;
        this.schemaTypeName = null;
        this.schemaTypeHierarchy = List.of();
        this.nillable = false;
    }

    public ElementNodeItemType(Name nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("Element node name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeName = null;
        this.schemaTypeHierarchy = List.of();
        this.nillable = false;
    }

    public ElementNodeItemType(Name nodeName, Name schemaTypeName, List<Name> schemaTypeHierarchy, boolean nillable) {
        if (schemaTypeName == null || schemaTypeHierarchy == null || schemaTypeHierarchy.isEmpty()) {
            throw new IllegalArgumentException("A typed element test requires a schema type hierarchy.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeName = schemaTypeName;
        this.schemaTypeHierarchy = List.copyOf(schemaTypeHierarchy);
        this.nillable = nillable;
    }

    private boolean isWildcardElement() {
        return this.nodeName == null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(
                ElementNodeItemType.class, this.catalogueName, this.nodeName, this.schemaTypeName, this.nillable);
    }

    @Override
    public boolean hasName() {
        return this.catalogueName != null;
    }

    @Override
    public Name getName() {
        if (this.catalogueName == null) {
            throw new UnsupportedOperationException("Named element node item type has no builtin QName");
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
        if (!(superType instanceof ElementNodeItemType other)) {
            return false;
        }
        if (other.isWildcardElement()) {
            return other.schemaTypeName == null || this.hasCompatibleSchemaType(other);
        }
        return this.nodeName != null
                && this.nodeName.equals(other.nodeName)
                && (other.schemaTypeName == null || this.hasCompatibleSchemaType(other));
    }

    private boolean hasCompatibleSchemaType(ElementNodeItemType superType) {
        return this.schemaTypeName != null
                && this.schemaTypeHierarchy.contains(superType.schemaTypeName)
                && (!this.nillable || superType.nillable);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other instanceof ElementNodeItemType) {
            return BuiltinTypesCatalogue.elementNode;
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
        throw new UnsupportedOperationException("element node item type does not support facets");
    }

    @Override
    public String toString() {
        if (this.catalogueName != null) {
            return this.catalogueName.toString();
        }
        String name = this.nodeName == null ? "*" : this.nodeName.toString();
        if (this.schemaTypeName == null) {
            return "element(" + name + ")";
        }
        return "element(" + name + ", " + this.schemaTypeName + (this.nillable ? "?" : "") + ")";
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
