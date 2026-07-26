package org.rumbledb.runtime.functions.maps;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.MapAtomicSameKey;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;

/**
 * W3C XPath/XQuery {@code map:remove}:
 * {@code map:remove($map as map(*), $keys as xs:anyAtomicType*) as map(*)}.
 *
 * Removes all entries whose key is the same-key as any supplied key (op:same-key).
 * This built-in is local execution only (consistent with other map/array accessors).
 */
public class MapRemoveFunctionIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> computeResult(context),
                getMetadata()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator mapIterator;
    private final RuntimeIterator keysIterator;

    public MapRemoveFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("map:remove must have exactly two arguments.");
        }
        this.mapIterator = arguments.get(0);
        this.keysIterator = arguments.get(1);
    }

    private Item computeResult(DynamicContext context) {
        Item mapItem = null;
        try {
            mapItem = LocalCursorUtils.materializeAtMostOne(this.mapIterator, context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "map:remove expects exactly one map argument [err:XPTY0004].",
                    getMetadata()
            );
        }

        if (mapItem == null || !mapItem.isMap()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to map:remove must be a map [err:XPTY0004].",
                    getMetadata()
            );
        }

        List<Item> rawKeys = LocalCursorUtils.materialize(this.keysIterator, context);

        if (rawKeys.isEmpty()) {
            return mapItem;
        }

        List<Item> keysToRemove = new ArrayList<>();
        for (Item it : rawKeys) {
            List<Item> atomized = it.atomizedValue();
            for (Item a : atomized) {
                if (a == null || !a.isAtomic()) {
                    throw new UnexpectedTypeException(
                            "map:remove expects keys that atomize to atomic items [err:XPTY0004].",
                            getMetadata()
                    );
                }
                keysToRemove.add(a);
            }
        }

        if (keysToRemove.isEmpty()) {
            return mapItem;
        }
        if (mapItem.getMutabilityLevel() == -1) {
            return ItemFactory.getInstance().createMapItemRemovingKeys(mapItem, keysToRemove);
        }
        List<Item> mapKeys = mapItem.getItemKeys();
        List<List<Item>> mapValueSequences = mapItem.getSequenceValues();
        boolean allKeysString = true;
        boolean allValuesSingletons = true;
        HashMap<Item, List<Item>> newKeyValuePairs = new HashMap<>();
        HashMap<String, Item> newStringKeyValuePairs = new HashMap<>();
        for (int i = 0; i < mapKeys.size(); i++) {
            Item mapKey = mapKeys.get(i);
            if (shouldRemoveKey(mapKey, keysToRemove)) {
                continue;
            }
            List<Item> seq = mapValueSequences.get(i);
            if (allKeysString && !mapKey.isString()) {
                allKeysString = false;
                // optimization: free up memory by removing pointer to the string hash map
                newStringKeyValuePairs = null;
            }
            if (allValuesSingletons && seq.size() != 1) {
                allValuesSingletons = false;
                // optimization: free up memory by removing pointer to the string hash map
                newStringKeyValuePairs = null;
            }
            if (allKeysString && allValuesSingletons) {
                if (newStringKeyValuePairs == null) {
                    newStringKeyValuePairs = new HashMap<>();
                }
                newStringKeyValuePairs.put(mapKey.getStringValue(), seq.get(0));
            }
            newKeyValuePairs.put(mapKey, seq);
        }
        if (allKeysString && allValuesSingletons) {
            return ItemFactory.getInstance()
                .createObjectItemOptimized(
                    newStringKeyValuePairs,
                    this.getRuntimeStaticContext().isQuerySideEffecting()
                );
        }
        return ItemFactory.getInstance()
            .createMapItem(newKeyValuePairs, getMetadata(), this.getRuntimeStaticContext().isQuerySideEffecting());
    }

    private static boolean shouldRemoveKey(Item mapKey, List<Item> keysToRemove) {
        for (Item keyToRemove : keysToRemove) {
            if (MapAtomicSameKey.sameKey(mapKey, keyToRemove)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException("map:remove is currently supported only in local execution mode.");
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("map:remove is currently supported only in local execution mode.");
    }
}
