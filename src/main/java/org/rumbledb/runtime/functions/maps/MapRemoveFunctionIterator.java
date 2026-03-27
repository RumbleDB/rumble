package org.rumbledb.runtime.functions.maps;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * W3C XPath/XQuery {@code map:remove}:
 * {@code map:remove($map as map(*), $keys as xs:anyAtomicType*) as map(*)}.
 *
 * Removes all entries whose key is the same-key as any supplied key (op:same-key).
 * This built-in is local execution only (consistent with other map/array accessors).
 */
public class MapRemoveFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator mapIterator;
    private final RuntimeIterator keysIterator;

    private Item resultItem;
    private boolean hasProducedResult;

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
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    protected void openLocal() {
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    private void initializeResult(DynamicContext context) {
        Item mapItem;
        try {
            mapItem = this.mapIterator.materializeExactlyOneItem(context);
        } catch (NoItemException | MoreThanOneItemException e) {
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

        List<Item> rawKeys = new ArrayList<>();
        this.keysIterator.materialize(context, rawKeys);

        if (rawKeys.isEmpty()) {
            this.resultItem = mapItem;
            return;
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
            this.resultItem = mapItem;
            return;
        }

        // Use existing map API only:
        // 1) create a copy of the map,
        // 2) remove keys on the copy using removeItemByKey (op:same-key).
        Map<Item, List<Item>> keyValuePairs = new HashMap<>();
        List<Item> mapKeys = mapItem.getItemKeys();
        List<List<Item>> mapValueSequences = mapItem.getSequenceValues();
        for (int i = 0; i < mapKeys.size(); i++) {
            List<Item> seq = mapValueSequences.get(i);
            keyValuePairs.put(mapKeys.get(i), seq == null ? new ArrayList<>() : seq);
        }

        org.rumbledb.items.MapItem copy = new org.rumbledb.items.MapItem(keyValuePairs, getMetadata());
        copy.setMutabilityLevel(0);
        for (Item k : keysToRemove) {
            copy.removeItemByKey(k);
        }
        copy.setMutabilityLevel(-1);
        this.resultItem = copy;
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext || this.hasProducedResult) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasProducedResult = true;
        this.hasNext = false;
        return this.resultItem;
    }

    @Override
    protected void resetLocal() {
        this.mapIterator.reset(this.currentDynamicContextForLocalExecution);
        this.keysIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.mapIterator.isOpen()) {
            this.mapIterator.close();
        }
        if (this.keysIterator.isOpen()) {
            this.keysIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
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
