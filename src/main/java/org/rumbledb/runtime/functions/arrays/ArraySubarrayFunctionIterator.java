package org.rumbledb.runtime.functions.arrays;



import java.io.Serial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.ArrayInvalidSubarrayLengthException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;

public class ArraySubarrayFunctionIterator extends AbstractAtMostOneItemRuntimePlan {


    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return computeResult(context);
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> arrayIterator;
    private final RuntimePlan<Item> startIterator;
    private final RuntimePlan<Item> lengthIterator;

    public ArraySubarrayFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2 && arguments.size() != 3) {
            throw new OurBadException("array:subarray must have either two or three arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.startIterator = arguments.get(1);
        this.lengthIterator = arguments.size() == 3 ? arguments.get(2) : null;
    }

    private Item computeResult(DynamicContext context) {
        Item arrayItem = null;
        try {
            arrayItem = this.arrayIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:subarray expects exactly one array argument.",
                    getMetadata()
            );
        }
        if (arrayItem == null) {
            return null;
        }
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:subarray must be an array.",
                    getMetadata()
            );
        }

        BigInteger start = materializeIntegerArgument(context, this.startIterator, "start");

        int size = arrayItem.getSize();
        BigInteger min = BigInteger.ONE;
        BigInteger max = BigInteger.valueOf((long) size).add(BigInteger.ONE);
        if (start.compareTo(min) < 0 || start.compareTo(max) > 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: " + start + ", of array with length: " + size,
                    getMetadata()
            );
        }

        BigInteger length;
        if (this.lengthIterator == null) {
            length = BigInteger.valueOf((long) size).subtract(start).add(BigInteger.ONE);
        } else {
            length = materializeIntegerArgument(context, this.lengthIterator, "length");
            if (length.compareTo(BigInteger.ZERO) < 0) {
                throw new ArrayInvalidSubarrayLengthException(
                        "array:subarray length must be non-negative. Found: " + length,
                        getMetadata()
                );
            }
        }

        if (start.add(length).compareTo(BigInteger.valueOf((long) size).add(BigInteger.ONE)) > 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "array:subarray start + length is out of bounds: start="
                        + start
                        + ", length="
                        + length
                        + ", array length="
                        + size,
                    getMetadata()
            );
        }

        int fromIndex = start.intValue() - 1;
        int toIndex = fromIndex + length.intValue();

        if (arrayItem.isArrayOfItems()) {
            List<Item> originalMembers = arrayItem.getItemMembers();
            List<Item> slicedMembers = new ArrayList<>(Math.max(0, toIndex - fromIndex));
            for (int i = fromIndex; i < toIndex; i++) {
                slicedMembers.add(originalMembers.get(i));
            }
            // TODO: optimization: if the subarray contains only singleton members, we can create an array of items
            // instead.
            return ItemFactory.getInstance()
                .createArrayItem(slicedMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
        List<List<Item>> originalMembers = arrayItem.getSequenceMembers();
        List<List<Item>> slicedMembers = new ArrayList<>(Math.max(0, toIndex - fromIndex));
        for (int i = fromIndex; i < toIndex; i++) {
            slicedMembers.add(originalMembers.get(i));
        }
        return ItemFactory.getInstance()
            .createSequenceArrayItem(slicedMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
    }

    private BigInteger materializeIntegerArgument(
            DynamicContext context,
            RuntimePlan<Item> iterator,
            String label
    ) {
        Item item = null;
        try {
            item = iterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:subarray expects exactly one " + label + " argument.",
                    getMetadata()
            );
        }

        if (item == null || !item.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; " + label + " argument to array:subarray must be numeric.",
                    getMetadata()
            );
        }

        if (item.isInteger()) {
            return item.castToIntegerValue();
        }
        return BigInteger.valueOf(item.castToIntValue());
    }
}
