package org.rumbledb.types;

import java.io.Serial;
import java.util.Set;

import lombok.Getter;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UndefinedTypeException;
import org.rumbledb.xml.schema.XmlSchemaCatalog;

/**
 * Class representing element(), element(QName), and schema-typed element item types.
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
    private Name typeName;

    @Getter
    private boolean nillable;

    private boolean resolved;

    public ElementNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("element");
        this.nodeName = null;
        this.typeName = null;
        this.nillable = false;
        this.resolved = true;
    }

    public ElementNodeItemType(Name nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("Element node name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.typeName = null;
        this.nillable = false;
        this.resolved = true;
    }

    public ElementNodeItemType(Name nodeName, Name typeName, boolean nillable) {
        if (typeName == null) {
            throw new IllegalArgumentException("Element schema type name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.typeName = typeName;
        this.nillable = nillable;
        this.resolved = false;
    }

    private boolean isWildcardElement() {
        return this.nodeName == null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(
                ElementNodeItemType.class, this.catalogueName, this.nodeName, this.typeName, this.nillable);
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
        if (other.nodeName != null && (this.nodeName == null || !this.nodeName.equals(other.nodeName))) {
            return false;
        }
        if (other.typeName == null) {
            return true;
        }
        return this.typeName != null && this.typeName.equals(other.typeName) && (!this.nillable || other.nillable);
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
        if (isWildcardElement() && this.typeName == null) {
            return this.catalogueName.toString();
        }
        String name = this.nodeName == null ? "*" : this.nodeName.toString();
        if (this.typeName == null) {
            return "element(" + name + ")";
        }
        return "element(" + name + ", " + this.typeName + (this.nillable ? "?" : "") + ")";
    }

    @Override
    public boolean isResolved() {
        return this.resolved;
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (this.resolved) {
            return;
        }
        this.typeName = ItemTypeReference.renameAtomic(context, this.typeName);
        XmlSchemaCatalog catalog = context.getXmlSchemaCatalog();
        if (!context.getInScopeSchemaTypes().checkInScopeSchemaTypeExists(this.typeName)
                && !isBuiltInElementSchemaType(this.typeName)
                && (catalog == null || catalog.getTypeDefinition(this.typeName).isEmpty())) {
            throw new UndefinedTypeException("Type undefined: " + this.typeName, metadata);
        }
        this.resolved = true;
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (this.resolved) {
            return;
        }
        if (!context.getInScopeSchemaTypes().checkInScopeSchemaTypeExists(this.typeName)
                && !isBuiltInElementSchemaType(this.typeName)) {
            throw new UndefinedTypeException("Type undefined: " + this.typeName, metadata);
        }
        this.resolved = true;
    }

    private static boolean isBuiltInElementSchemaType(Name name) {
        if (!Name.XS_NS.equals(name.getNamespace())) {
            return false;
        }
        return name.getLocalName().equals("anyType")
                || name.getLocalName().equals("anySimpleType")
                || name.getLocalName().equals("untyped");
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleConfiguration configuration) {
        return false;
    }
}
