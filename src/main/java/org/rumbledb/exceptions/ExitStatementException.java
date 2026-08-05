package org.rumbledb.exceptions;

import java.io.Serial;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import lombok.Getter;

import org.rumbledb.api.Item;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.update.PendingUpdateList;

public class ExitStatementException extends RuntimeException {
    @Serial private static final long serialVersionUID = 1L;
    @Getter private final PendingUpdateList pendingUpdateList;
    private final List<Item> localResult;
    @Getter private final JavaRDD<Item> rddResult;
    @Getter private final HomogeneousItemDataFrame dataFrameResult;
    private final ExceptionMetadata exceptionMetadata;

    public ExitStatementException(
            PendingUpdateList pendingUpdateList,
            List<Item> localResult,
            JavaRDD<Item> rddResult,
            HomogeneousItemDataFrame dataFrameResult,
            ExceptionMetadata exceptionMetadata) {
        this.pendingUpdateList = pendingUpdateList;
        this.localResult = localResult;
        this.rddResult = rddResult;
        this.dataFrameResult = dataFrameResult;
        this.exceptionMetadata = exceptionMetadata;
    }

    public List<Item> getLocalResult() {
        if (hasLocalResult()) {
            return this.localResult;
        } else if (hasRDDResult()) {
            return this.rddResult.collect();
        } else if (hasDataFrameResult()) {
            return this.dataFrameResult.toRDD(this.exceptionMetadata).collect();
        }
        throw new OurBadException(
                "Expected local result but there was nothing to return from the exit statement!");
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
