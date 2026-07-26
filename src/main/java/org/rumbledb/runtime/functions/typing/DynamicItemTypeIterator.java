package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
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
        return evaluate(context);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new Cursor(this, context);
    }

    private Item evaluate(DynamicContext context) {
        List<Item> argument = LocalCursorUtils.materialize(getChild(0), context);
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

        private final DynamicItemTypeIterator plan;
        private final DynamicContext context;

        private Cursor(DynamicItemTypeIterator plan, DynamicContext context) {
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected Item materializeFirstItemOrNull() {
            return this.plan.evaluate(this.context);
        }
    }
}
