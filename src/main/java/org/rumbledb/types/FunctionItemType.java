package org.rumbledb.types;

import org.rumbledb.exceptions.OurBadException;

public class FunctionItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private boolean isGeneric;
    private FunctionSignature signature;
    static FunctionItemType anyFunctionItem = new FunctionItemType(true);

    public FunctionItemType(FunctionSignature signature) {
        if (signature == null) {
            throw new OurBadException("a new function item type must have a signature");
        }
        this.isGeneric = false;
        this.signature = signature;
    }

    // we have a parameter because the empty one is public and inherited
    private FunctionItemType(boolean isGeneric) {
        this.isGeneric = true;
        this.signature = null;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FunctionItemType)) {
            return false;
        }
        return this.toString().equals(other.toString());
    }

    @Override
    public boolean isFunctionItem() {
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
        if (superType.isFunctionItem() && this.signature.isSubtypeOf(superType.getSignature())) {
            return true;
        }
        return false;
    }

    @Override
    public ItemType findCommonSuperType(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other.isFunctionItem()) {
            return anyFunctionItem;
        }
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public String toString() {
        return this.isGeneric ? "function(*)" : this.signature.toString();
    }
}
