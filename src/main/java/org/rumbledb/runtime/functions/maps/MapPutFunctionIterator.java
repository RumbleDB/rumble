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
import java.util.HashMap;
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

        // 4) Build a new map: keep entries whose key is not op:same-key to $key. Walk keys and value
        // sequences by index (one pass); reuse value lists for unchanged entries like map:remove.
        if (mapItem.isObject() && key.isString() && valueSequence.size() == 1) {
            // fast path:construct an object item
            HashMap<String, Item> newKeyValuePairs = new HashMap<>();
            // optimization: short circuit the op:same-key check if the key is already found
            boolean keyFound = false;
            for (Item existingKey : mapItem.getItemKeys()) {
                if (!keyFound && MapAtomicSameKey.sameKey(existingKey, key)) {
                    keyFound = true;
                    continue;
                }
                newKeyValuePairs.put(existingKey.getStringValue(), mapItem.getItemByKey(existingKey));
            }
            newKeyValuePairs.put(key.getStringValue(), valueSequence.get(0));
            return ItemFactory.getInstance().createObjectItemOptimized(newKeyValuePairs, false);
        } else {
            HashMap<Item, List<Item>> newKeyValuePairs = new HashMap<>();
            // optimization: short circuit the op:same-key check if the key is already found
            boolean keyFound = false;
            for (Item existingKey : mapItem.getItemKeys()) {
                if (!keyFound && MapAtomicSameKey.sameKey(existingKey, key)) {
                    keyFound = true;
                    continue;
                }
                newKeyValuePairs.put(existingKey, mapItem.getSequenceByKey(existingKey));
            }
            newKeyValuePairs.put(key, valueSequence);

            return ItemFactory.getInstance().createMapItem(newKeyValuePairs, getMetadata(), false);
        }
    }
}

