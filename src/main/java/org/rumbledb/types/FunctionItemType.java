package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;

import java.io.Serial;
import java.util.Set;

public class FunctionItemType extends AbstractItemType {

    @Serial
    private static final long serialVersionUID = 1L;

    private final boolean isGeneric;
    private final FunctionSignature signature;

    static final FunctionItemType anyFunctionItem = new FunctionItemType(true);

    FunctionItemType(FunctionSignature signature) {
        if (signature == null) {
            throw new OurBadException("a new function item type must have a signature");
        }
        this.isGeneric = false;
        this.signature = signature;
    }

    // we have a parameter because the empty one is public and inherited
    FunctionItemType(boolean isGeneric) {
        this.isGeneric = true;
        this.signature = null;
    }



    @Override
    public Name getName() {
        return null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(FunctionItemType.class, this.isGeneric, this.signature);
    }

    @Override
    public boolean isFunctionItemType() {
        return true;
    }

    @Override
    public FunctionSignature getSignature() {
        return this.signature;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (
            this.equals(superType) || superType.equals(anyFunctionItem) || superType.equals(BuiltinTypesCatalogue.item)
        ) {
            return true;
        }
        if (this.signature == null) {
            return false;
        }
        if (superType.isFunctionItemType() && this.signature.isSubtypeOf(superType.getSignature())) {
            return true;
        }
        return false;
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other.isMapItemType()) {
            return anyFunctionItem;
        }
        if (other.isFunctionItemType()) {
            return anyFunctionItem;
        }
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public int getTypeTreeDepth() {
        return this.equals(anyFunctionItem) ? 1 : 2;
    }

    @Override
    public ItemType getBaseType() {
        return this.equals(anyFunctionItem) ? BuiltinTypesCatalogue.item : anyFunctionItem;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("function item types does not support facets");
    }

    @Override
    public String getIdentifierString() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.isGeneric ? "function(*)" : this.signature.toString();
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }
}
