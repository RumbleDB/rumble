package org.rumbledb.runtime.functions.sequences.aggregate;

import org.apache.spark.api.java.function.Function2;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.operational.AdditiveOperationIterator;

public class SumClosure implements Function2<Item, Item, Item> {

    private static final long serialVersionUID = 1L;
    private ExceptionMetadata metadata;

    public SumClosure(ExceptionMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public Item call(Item v1, Item v2) throws Exception {
        return AdditiveOperationIterator.processItem(v1, v2, false, this.metadata);
    }

}
