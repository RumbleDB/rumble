package org.rumbledb.runtime.functions.maps;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * W3C XPath/XQuery {@code map:entry}:
 * <ul>
 * <li>atomizes a single key into exactly one atomic value</li>
 * <li>materializes the value into a general sequence (possibly empty)</li>
 * <li>returns a map containing a single key/value binding</li>
 * </ul>
 */
public class MapEntryFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator keyIterator;
    private final RuntimeIterator valueIterator;

    public MapEntryFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("map:entry must have exactly two arguments.");
        }
        this.keyIterator = arguments.get(0);
        this.valueIterator = arguments.get(1);
    }

    private static Item atomizeSingleMapKey(
            RuntimeIterator keyIterator,
            DynamicContext dynamicContext,
            ExceptionMetadata metadata
    ) {
        List<Item> keySequence = new ArrayList<>();
        keyIterator.materialize(dynamicContext, keySequence);

        List<Item> atomized = new ArrayList<>();
        for (Item item : keySequence) {
            atomized.addAll(item.atomizedValue());
        }

        if (atomized.size() != 1) {
            throw new UnexpectedTypeException(
                    "Map entry key must atomize to a single atomic value [err:XPTY0004].",
                    metadata
            );
        }

        Item k = atomized.get(0);
        if (!k.isAtomic()) {
            throw new UnexpectedTypeException(
                    "Map entry key must atomize to a single atomic value [err:XPTY0004].",
                    metadata
            );
        }

        return k;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Item key = atomizeSingleMapKey(this.keyIterator, dynamicContext, getMetadata());

        List<Item> valueSequence = new ArrayList<>();
        this.valueIterator.materialize(dynamicContext, valueSequence);

        return ItemFactory.getInstance()
            .createMapItem(
                Collections.singletonMap(key, valueSequence),
                getMetadata(),
                false
            );
    }
}

