package org.rumbledb.runtime.xml;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.*;

public class SlashExprIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;
    private List<Item> results = null;
    private int nextResultCounter = 0;
    private Item nextResult;


    public SlashExprIterator(
            RuntimeIterator sequence,
            RuntimeIterator stepIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(sequence, stepIterator), staticContext);
        this.leftIterator = sequence;
        this.rightIterator = stepIterator;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.leftIterator.getRDD(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new SlashExprClosure(this.rightIterator, dynamicContext);
        return childRDD.flatMap(transformation);
    }

    @Override
    public void openLocal() {
        setNextResult();
    }

    @Override
    public void closeLocal() {
        this.hasNext = false;
        this.results = null;
        this.nextResult = null;
        this.nextResultCounter = 0;
        this.leftIterator.close();
        this.rightIterator.close();
    }

    @Override
    public void resetLocal() {
        this.results = null;
        this.nextResultCounter = 0;
        setNextResult();
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item nextResult = this.nextResult;
            setNextResult();
            return nextResult;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in Slash Expression",
                getMetadata()
        );
    }

    private void setNextResult() {
        if (this.results == null) {
            List<Item> left = this.leftIterator.materialize(this.currentDynamicContextForLocalExecution);
            this.currentDynamicContextForLocalExecution.getVariableValues()
                .addVariableValue(Name.CONTEXT_ITEM, left);
            this.results = this.rightIterator.materialize(this.currentDynamicContextForLocalExecution);
        }
        if (this.nextResultCounter < this.results.size()) {
            this.nextResult = this.results.get(nextResultCounter++);
        } else {
            this.hasNext = false;
        }
    }
}
