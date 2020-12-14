package org.rumbledb.types;

import org.rumbledb.exceptions.OurBadException;

public class FunctionItemType extends ItemType {

    private boolean isGeneric;
    private FunctionSignature signature;
    public static FunctionItemType ANYFUNCTION = new FunctionItemType(true);

    public FunctionItemType(FunctionSignature signature){
        if(signature == null){
            throw new OurBadException("a new function item type must have a signature");
        }
        this.isGeneric = false;
        this.signature = signature;
        this.name = signature.toString();
    }

    // we have a parameter because the empty one is public and inherited
    private FunctionItemType(boolean isGeneric){
        this.isGeneric = true;
        this.signature = null;
        this.name = "function(*)";
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
    public boolean equals(Object other) {
        if(!(other instanceof ItemType)){
            return false;
        }
        return this.name.equals(other.toString());
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if(this.equals(superType) || superType.equals(ANYFUNCTION) || superType.equals(ItemType.item)){
            return true;
        }
        if(superType.isFunctionItem() && this.signature.isSubtypeOf(superType.getSignature())){
            return true;
        }
        return false;
    }

    @Override
    public ItemType findCommonSuperType(ItemType other) {
        if(this.equals(other)){
            return this;
        }
        if(other.isFunctionItem()){
            return ANYFUNCTION;
        }
        return ItemType.item;
    }
}
