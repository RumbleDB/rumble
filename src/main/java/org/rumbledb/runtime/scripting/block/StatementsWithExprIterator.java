package org.rumbledb.runtime.scripting.block;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class StatementsWithExprIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private RuntimeIterator currentChild;
    private int childIndex;
    private Item result;

    public StatementsWithExprIterator(
            List<RuntimeIterator> statements,
            RuntimeIterator exprIterator,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        // Expect an expression to be present
        assert exprIterator != null;

        this.children.addAll(statements);
        this.children.add(exprIterator);

        for (RuntimeIterator child : this.children) {
            if (child.isSequential()) {
                this.isSequential = child.isSequential();
            }
        }
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.currentDynamicContextForLocalExecution = context;
        startLocal();
        return this.result;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        if (!this.children.isEmpty()) {
            this.childIndex = 0;
            this.currentChild = this.children.get(this.childIndex);

            JavaRDD<Item> childRDD = this.currentChild.getRDD(dynamicContext);
            this.childIndex++;

            while (this.childIndex < this.children.size()) {
                this.currentChild = this.children.get(this.childIndex);
                JavaRDD<Item> nextChildRDD = this.currentChild.getRDD(dynamicContext);
                childRDD = childRDD.union(nextChildRDD);
                this.childIndex++;
            }
            return childRDD;
        } else {
            JavaSparkContext sparkContext = SparkSessionManager.getInstance().getJavaSparkContext();
            return sparkContext.emptyRDD();
        }
    }


    private void startLocal() {
        this.childIndex = 0;
        this.currentChild = this.children.get(this.childIndex);
        this.currentChild.open(this.currentDynamicContextForLocalExecution);

        setNextResult();
    }

    public void setNextResult() {
        if (this.currentChild == null) {
            this.hasNext = false;
            return;
        }

        this.result = null;
        while (this.result == null) {
            if (!this.currentChild.hasNext()) {
                this.currentChild.close();
                if (++this.childIndex == this.children.size()) {
                    this.currentChild = null;
                    break;
                } else {
                    this.currentChild = this.children.get(this.childIndex);
                    this.currentChild.open(this.currentDynamicContextForLocalExecution);
                }
            } else {
                if (this.childIndex == this.children.size() - 1) {
                    // Result is only the expression's result
                    this.result = this.currentChild.next();
                } else {
                    // We have a statement with next. Result is ignored
                    this.currentChild.next();
                }
            }
        }

        this.hasNext = this.result != null;
    }

    @Override
    public void openLocal() {
        startLocal();
    }

    @Override
    public void closeLocal() {
        if (this.currentChild != null) {
            this.currentChild.close();
        }
    }

    @Override
    public void resetLocal() {
        startLocal();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.result; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in StatementsWithExpression", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        int childIndex = 0;
        while (childIndex < this.children.size() - 1) {
            this.children.get(childIndex).getDataFrame(dynamicContext);
            ++childIndex;
        }
        RuntimeIterator exprIterator = this.children.get(childIndex);
        return exprIterator.getDataFrame(dynamicContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        RuntimeIterator exprIterator = this.children.get(this.children.size() - 1);
        if (exprIterator.isUpdating()) {
            return exprIterator.getPendingUpdateList(context);
        }
        return new PendingUpdateList();
    }
}
