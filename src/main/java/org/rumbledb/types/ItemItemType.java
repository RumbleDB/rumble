package org.rumbledb.types;

import org.rumbledb.context.Name;

/**
 * Class representing the generic 'item' item type
 */
public class ItemItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    static final ItemType item = new ItemItemType(Name.createVariableInDefaultTypeNamespace("item"));
    private Name name;

    public ItemItemType(){

    }

    private ItemItemType(Name name){
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        // no need to check the class because ItemItemType is a singleton and it is only equal to its only instance
        return o == item;
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
        return superType == item;
    }

    @Override
    public ItemType findCommonSuperType(ItemType other) {
        return item;
    }

    @Override
    public String toString() {
        return this.name.toString();
    }
}
