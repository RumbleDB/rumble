package org.rumbledb.runtime.functions.typing;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.ItemType;

public class DynamicItemTypeIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public DynamicItemTypeIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        ItemRuntimePlan argumentPlan = this.getChild(0);
        List<Item> argument = argumentPlan.materialize(context);
        ItemType itemType = argument.get(0).getDynamicType();
        List<Item> structureItems = getStructureItems(argument, itemType);
        ItemType commonType = getLeastCommonSupertype(structureItems);
        return ItemFactory.getInstance().createStringItem(commonType.getIdentifierString());
    }

    private static ItemType getLeastCommonSupertype(List<Item> structureItems) {
        ItemType structureCommonType = structureItems.get(0).getDynamicType();
        for (Item item : structureItems) {
            structureCommonType = structureCommonType.findLeastCommonSuperTypeWith(item.getDynamicType());
        }
        return structureCommonType;
    }

    private static List<Item> getStructureItems(List<Item> argument, ItemType itemType) {
        if (itemType.isArrayItemType()) {
            return argument.get(0).getItemMembers();
        }
        return argument;
    }
}
