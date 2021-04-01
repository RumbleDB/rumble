package org.rumbledb.runtime.functions.sequences.aggregate;

import org.apache.spark.api.java.function.Function2;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.runtime.arithmetics.AdditiveOperationIterator;

public class SumClosure implements Function2<Item, Item, Item> {

    private static final long serialVersionUID = 1L;
    private ExceptionMetadata metadata;

    public SumClosure(ExceptionMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public Item call(Item v1, Item v2) throws Exception {
        Item result = AdditiveOperationIterator.processItem(v1, v2, false);
        if (result == null) {
            throw new InvalidArgumentTypeException(
                    " \"+\": operation not possible with parameters of type \""
                        + v1.getDynamicType().toString()
                        + "\" and \""
                        + v2.getDynamicType().toString()
                        + "\"",
                    this.metadata
            );
        }
        return result;
    }

}
