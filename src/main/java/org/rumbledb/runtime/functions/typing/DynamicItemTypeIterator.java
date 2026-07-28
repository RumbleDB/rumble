package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.types.ItemType;

import java.io.Serial;
import java.util.List;

public class DynamicItemTypeIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public DynamicItemTypeIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(this.getChild(0), context);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new Cursor(this.getChild(0), context, this.getMetadata());
    }

    private static Item evaluate(RuntimeIterator argumentPlan, DynamicContext context) {
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

    private static final class Cursor extends AtMostOneLocalCursor<Item> {

        private final RuntimeIterator argumentPlan;
        private final DynamicContext context;

        private Cursor(RuntimeIterator argumentPlan, DynamicContext context, ExceptionMetadata metadata) {
            super(metadata);
            this.argumentPlan = argumentPlan;
            this.context = context;
        }

        @Override
        protected Item materializeOneItemOrNull() {
            return evaluate(this.argumentPlan, this.context);
        }
    }
}
