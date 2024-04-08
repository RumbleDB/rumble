package org.rumbledb.runtime.scripting.block;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class StatementsOnlyIterator extends HybridRuntimeIterator {
    private RuntimeIterator currentChild;
    private int childIndex;

    public StatementsOnlyIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
        for (RuntimeIterator child : children) {
            if (child.isSequential()) {
                this.isSequential = child.isSequential();
            }
        }
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.currentDynamicContextForLocalExecution = context;
        startLocal();
        return null;
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

        while (this.childIndex < this.children.size()) {
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
                // We have a statement with next. Result is ignored
                this.currentChild.next();
            }
        }

        this.hasNext = this.currentChild != null && this.currentChild.hasNext();
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
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            setNextResult();
            // Always return null
            return ItemFactory.getInstance().createNullItem();
        }
        throw new IteratorFlowException("Invalid next() call in StatementsWithExpression", getMetadata());
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
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
}
