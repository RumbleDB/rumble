package org.rumbledb.runtime.functions.arrays;


import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class ArrayGetFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> arrayIterator;
    private final RuntimePlan<Item> positionIterator;

    public ArrayGetFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:get must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.positionIterator = arguments.get(1);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> getMember(
                    this.arrayIterator.materialize(context),
                    this.positionIterator.materialize(context)
                ).iterator(),
                getMetadata()
        );
    }

    private List<Item> getMember(List<Item> arrays, List<Item> positions) {
        if (arrays.isEmpty()) {
            return List.of();
        }
        if (arrays.size() > 1) {
            throw new UnexpectedTypeException(
                    "array:get expects exactly one array argument.",
                    getMetadata()
            );
        }
        Item arrayItem = arrays.get(0);
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:get must be an array.",
                    getMetadata()
            );
        }

        if (positions.size() != 1) {
            throw new UnexpectedTypeException(
                    "array:get expects exactly one position argument.",
                    getMetadata()
            );
        }
        Item positionItem = positions.get(0);
        if (!positionItem.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; position argument to array:get must be numeric.",
                    getMetadata()
            );
        }

        java.math.BigInteger positionInteger;
        if (positionItem.isInteger()) {
            positionInteger = positionItem.castToIntegerValue();
        } else {
            positionInteger = java.math.BigInteger.valueOf(positionItem.castToIntValue());
        }

        if (
            positionInteger.compareTo(java.math.BigInteger.ONE) < 0
                || positionInteger.compareTo(java.math.BigInteger.valueOf(arrayItem.getSize())) > 0
        ) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: "
                        + positionInteger
                        + ", of array with length: "
                        + arrayItem.getSize(),
                    getMetadata()
            );
        }

        int lookup = positionInteger.intValue();

        if (arrayItem.isArrayOfItems()) {
            return List.of(arrayItem.getItemAt(lookup - 1));
        }
        return arrayItem.getSequenceAt(lookup - 1);
    }
}
