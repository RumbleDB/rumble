package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;

import java.util.List;

public class DynamicItemTypeIterator extends AtMostOneItemLocalRuntimeIterator {
    private List<Item> materializedArgument;
    private ItemType itemType;

    public DynamicItemTypeIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        materializeArgument(context);
        setArgumentType();
        return getLeastCommonSupertype();
    }

    private void materializeArgument(DynamicContext context) {
        this.materializedArgument = this.children.get(0).materialize(context);
    }

    private void setArgumentType() {
        this.itemType = this.materializedArgument.get(0).getDynamicType();
    }

    private Item getLeastCommonSupertype() {
        List<Item> structureItems = getStructureItems();
        ItemType structureCommonType = structureItems.get(0).getDynamicType();
        for (Item item : structureItems) {
            structureCommonType = structureCommonType.findLeastCommonSuperTypeWith(item.getDynamicType());
        }
        return ItemFactory.getInstance().createStringItem(structureCommonType.getIdentifierString());
    }

    private List<Item> getStructureItems() {
        if (this.itemType.isArrayItemType()) {
            return this.materializedArgument.get(0).getItems();
        }
        return this.materializedArgument;
    }
}
