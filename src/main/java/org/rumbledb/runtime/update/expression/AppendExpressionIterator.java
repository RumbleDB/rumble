package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class AppendExpressionIterator extends HybridRuntimeIterator {
    protected AppendExpressionIterator(List<RuntimeIterator> children, ExecutionMode executionMode, ExceptionMetadata iteratorMetadata) {
        super(children, executionMode, iteratorMetadata);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    @Override
    protected void openLocal() {

    }

    @Override
    protected void closeLocal() {

    }

    @Override
    protected void resetLocal() {

    }

    @Override
    protected boolean hasNextLocal() {
        return false;
    }

    @Override
    protected Item nextLocal() {
        return null;
    }
}
