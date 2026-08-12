package org.rumbledb.runtime.functions.arrays;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

public class ArrayHeadFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan arrayIterator;

    public ArrayHeadFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        if (arguments.size() != 1) {
            throw new OurBadException("array:head must have exactly one argument.");
        }
        this.arrayIterator = arguments.get(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> headArgument(this.arrayIterator.materialize(context)).iterator(), getMetadata());
    }

    private List<Item> head(Item arrayItem) {
        if (arrayItem == null) {
            return List.of();
        }
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException("Type error; argument to array:head must be an array.", getMetadata());
        }

        int size = arrayItem.getSize();
        if (size == 0) {
            throw new ArrayIndexOutOfBoundsException("array:head called on an empty array.", getMetadata());
        }

        if (arrayItem.isArrayOfItems()) {
            return List.of(arrayItem.getItemAt(0));
        }
        return arrayItem.getSequenceAt(0);
    }

    private List<Item> headArgument(List<Item> items) {
        if (items.size() > 1) {
            throw new UnexpectedTypeException("array:head expects exactly one array argument.", getMetadata());
        }
        return head(items.isEmpty() ? null : items.get(0));
    }
}
