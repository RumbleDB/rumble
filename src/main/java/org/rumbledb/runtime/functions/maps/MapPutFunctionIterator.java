package org.rumbledb.runtime.functions.maps;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.MapAtomicSameKey;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * W3C XPath/XQuery {@code map:put}:
 * <ul>
 * <li>requires exactly one map argument</li>
 * <li>atomizes the key and requires exactly one atomic value</li>
 * <li>returns a new map with the entry added or replaced (key equivalence via op:same-key)</li>
 * </ul>
 *
 * This built-in is local execution only.
 */
public class MapPutFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator mapIterator;
    private final RuntimeIterator keyIterator;
    private final RuntimeIterator valueIterator;

    public MapPutFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("map:put must have exactly three arguments.");
        }
        this.mapIterator = arguments.get(0);
        this.keyIterator = arguments.get(1);
        this.valueIterator = arguments.get(2);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        // 1) Materialize $map as exactly one map(*)
        Item mapItem;
        try {
            mapItem = this.mapIterator.materializeExactlyOneItem(context);
        } catch (NoItemException | MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "map:put expects exactly one map argument [err:XPTY0004].",
                    getMetadata()
            );
        }
        if (mapItem == null || !mapItem.isMap()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to map:put must be a map [err:XPTY0004].",
                    getMetadata()
            );
        }

        // 2) Atomize $key and require that it atomizes to exactly one atomic value.
        List<Item> rawKey = new ArrayList<>();
        this.keyIterator.materialize(context, rawKey);

        List<Item> atomized = new ArrayList<>();
        for (Item it : rawKey) {
            atomized.addAll(it.atomizedValue());
        }

        if (atomized.size() != 1 || !atomized.get(0).isAtomic()) {
            throw new UnexpectedTypeException(
                    "Map key must atomize to a single atomic value [err:XPTY0004].",
                    getMetadata()
            );
        }
        Item key = atomized.get(0);

        // 3) Materialize $value as item()*
        List<Item> valueSequence = new ArrayList<>();
        this.valueIterator.materialize(context, valueSequence);

        // 4) Build a new map: copy all entries except those whose key is op:same-key to $key
        List<Item> newKeys = new ArrayList<>();
        List<List<Item>> newValueSequences = new ArrayList<>();

        List<Item> keys = mapItem.getItemKeys();
        for (Item existingKey : keys) {
            if (MapAtomicSameKey.sameKey(existingKey, key)) {
                continue; // replace existing entry
            }
            List<Item> existingSeq = mapItem.getSequenceByKey(existingKey);
            newKeys.add(existingKey);
            newValueSequences.add(existingSeq == null ? new ArrayList<>() : new ArrayList<>(existingSeq));
        }

        newKeys.add(key);
        newValueSequences.add(new ArrayList<>(valueSequence));

        return ItemFactory.getInstance()
            .createMapItem(newKeys, newValueSequences, getMetadata(), false);
    }
}

