package org.rumbledb.runtime.functions.maps;

import org.rumbledb.runtime.HybridRuntimeIterator;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * W3C XPath/XQuery {@code map:get}:
 * <ul>
 * <li>requires exactly one map argument</li>
 * <li>atomizes the key and requires exactly one atomic value</li>
 * <li>returns the associated value sequence, if present</li>
 * </ul>
 *
 * This built-in is local execution only (consistent with map/array accessors).
 */
public class MapGetFunctionIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> mapIterator;
    private final RuntimePlan<Item> keyIterator;

    public MapGetFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("map:get must have exactly two arguments.");
        }
        this.mapIterator = arguments.get(0);
        this.keyIterator = arguments.get(1);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> lookup(
                    this.mapIterator.materialize(context),
                    this.keyIterator.materialize(context)
                ).iterator(),
                getMetadata()
        );
    }

    private List<Item> lookup(List<Item> maps, List<Item> rawKey) {
        if (maps.size() != 1) {
            throw new UnexpectedTypeException(
                    "map:get expects exactly one map argument.",
                    getMetadata()
            );
        }
        Item mapItem = maps.get(0);
        if (mapItem == null || !mapItem.isMap()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to map:get must be a map.",
                    getMetadata()
            );
        }

        // Atomize $key and require that it atomizes to exactly one atomic value.
        List<Item> atomized = new ArrayList<>();
        for (Item it : rawKey) {
            atomized.addAll(it.atomizedValue());
        }

        if (atomized.size() != 1 || !atomized.get(0).isAtomic()) {
            throw new UnexpectedTypeException(
                    "Map lookup key must atomize to a single atomic value [err:XPTY0004].",
                    getMetadata()
            );
        }

        Item key = atomized.get(0);
        List<Item> seq = mapItem.getSequenceByKey(key);
        return seq == null ? List.of() : seq;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("map:get is currently supported only in local execution mode.");
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("map:get is currently supported only in local execution mode.");
    }
}
