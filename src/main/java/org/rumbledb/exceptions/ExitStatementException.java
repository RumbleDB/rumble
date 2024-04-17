package org.rumbledb.exceptions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.util.List;

public class ExitStatementException extends RuntimeException {
    private final PendingUpdateList pendingUpdateList;
    private final List<Item> localResult;
    private final JavaRDD<Item> rddResult;
    private final JSoundDataFrame dataFrameResult;

    public ExitStatementException(
            PendingUpdateList pendingUpdateList,
            List<Item> localResult,
            JavaRDD<Item> rddResult,
            JSoundDataFrame dataFrameResult
    ) {
        this.pendingUpdateList = pendingUpdateList;
        this.localResult = localResult;
        this.rddResult = rddResult;
        this.dataFrameResult = dataFrameResult;
    }

    public PendingUpdateList getPendingUpdateList() {
        return this.pendingUpdateList;
    }

    public List<Item> getLocalResult() {
        return this.localResult;
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
