package org.rumbledb.exceptions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.util.List;

import static org.rumbledb.runtime.HybridRuntimeIterator.dataFrameToRDDOfItems;

public class ExitStatementException extends RuntimeException {
    private final PendingUpdateList pendingUpdateList;
    private final List<Item> localResult;
    private final JavaRDD<Item> rddResult;
    private final JSoundDataFrame dataFrameResult;
    private final ExceptionMetadata exceptionMetadata;

    public ExitStatementException(
            PendingUpdateList pendingUpdateList,
            List<Item> localResult,
            JavaRDD<Item> rddResult,
            JSoundDataFrame dataFrameResult,
            ExceptionMetadata exceptionMetadata
    ) {
        this.pendingUpdateList = pendingUpdateList;
        this.localResult = localResult;
        this.rddResult = rddResult;
        this.dataFrameResult = dataFrameResult;
        this.exceptionMetadata = exceptionMetadata;
    }

    public PendingUpdateList getPendingUpdateList() {
        return this.pendingUpdateList;
    }

    public List<Item> getLocalResult() {
        if (hasLocalResult()) {
            return this.localResult;
        } else if (hasRDDResult()) {
            return this.rddResult.collect();
        } else if (hasDataFrameResult()) {
            return dataFrameToRDDOfItems(this.dataFrameResult, this.exceptionMetadata).collect();
        }
        throw new OurBadException("Expected local result but there was nothing to return from the exit statement!");
    }

    public JavaRDD<Item> getRddResult() {
        return this.rddResult;
    }

    public JSoundDataFrame getDataFrameResult() {
        return this.dataFrameResult;
    }

    public boolean hasLocalResult() {
        return this.localResult != null;
    }

    public boolean hasRDDResult() {
        return this.rddResult != null;
    }

    public boolean hasDataFrameResult() {
        return this.dataFrameResult != null;
    }
}
