package org.rumbledb.runtime.functions.arrays;


import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

public class ArrayFunctionCallIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> lookupLocally(context).iterator(),
                getMetadata()
        );
    }

    private List<Item> lookupLocally(DynamicContext context) {
        if (this.indexIterator == null) {
            throw new UnexpectedTypeException(
                    "Array function calls must have exactly one argument.",
                    getMetadata()
            );
        }
        List<Item> selectors = this.indexIterator.materialize(context);
        if (selectors.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid array function call; array lookup can't be performed with no key.",
                    getMetadata()
            );
        }
        if (selectors.size() > 1) {
            throw new InvalidSelectorException(
                    "Invalid array function call; array lookup can't be performed with multiple keys.",
                    getMetadata()
            );
        }
        Item selector = selectors.get(0);
        if (!selector.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; non numeric array lookup for : " + selector.serialize(),
                    getMetadata()
            );
        }
        if (!this.arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Array function calls can only be performed on arrays.",
                    getMetadata()
            );
        }
        int position = selector.castToIntValue();
        if (position <= 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: "
                        + position
                        + ", of array with length: "
                        + this.arrayItem.getSize(),
                    getMetadata()
            );
        }
        if (this.arrayItem.isArrayOfItems()) {
            return List.of(this.arrayItem.getItemAt(position - 1));
        }
        return this.arrayItem.getSequenceAt(position - 1);
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final Item arrayItem;
    private final ItemRuntimePlan indexIterator;

    public ArrayFunctionCallIterator(
            Item arrayItem,
            ItemRuntimePlan indexIterator,
            RuntimeStaticContext staticContext
    ) {
        super(indexIterator == null ? null : Collections.singletonList(indexIterator), staticContext);
        this.arrayItem = arrayItem;
        this.indexIterator = indexIterator;
    }
}
