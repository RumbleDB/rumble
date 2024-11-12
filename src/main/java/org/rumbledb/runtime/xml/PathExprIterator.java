package org.rumbledb.runtime.xml;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathExprIterator extends HybridRuntimeIterator {
    private final RuntimeIterator getRootIterator;
    private final List<RuntimeIterator> stepIterators;
    private List<Item> results = null;
    private int nextResultCounter = 0;
    private Item nextResult;

    public PathExprIterator(
            List<RuntimeIterator> stepIterators,
            RuntimeIterator getRootIterator,
            RuntimeStaticContext staticContext
    ) {
        super(stepIterators, staticContext);
        this.getRootIterator = getRootIterator;
        this.stepIterators = stepIterators;
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
        this.stepIterators.forEach(RuntimeIterator::close);
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
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in Path Expression",
                getMetadata()
        );
    }

    private void setNextResult() {
        if (this.results == null) {
            RuntimeIterator finalIterator = this.stepIterators.get(this.stepIterators.size() - 1);
            if (this.getRootIterator != null) {
                List<Item> root = this.getRootIterator.materialize(this.currentDynamicContextForLocalExecution);
                this.currentDynamicContextForLocalExecution.getVariableValues()
                    .addVariableValue(Name.CONTEXT_ITEM, root);
            }
            for (int i = 0; i < this.stepIterators.size() - 1; ++i) {
                // TODO: Verify that the type of each item is node
                List<Item> nextContext = this.stepIterators.get(i)
                    .materialize(this.currentDynamicContextForLocalExecution);
                this.currentDynamicContextForLocalExecution.getVariableValues()
                    .addVariableValue(Name.CONTEXT_ITEM, nextContext);
            }
            results = finalIterator.materialize(this.currentDynamicContextForLocalExecution);
        }
        if (this.nextResultCounter < this.results.size()) {
            this.nextResult = this.results.get(nextResultCounter++);
        } else {
            this.hasNext = false;
        }
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        // Get the root RDD if the root iterator is not null
        JavaRDD<Item> currentRDD = null;
        JavaPairRDD<List<Item>, DynamicContext> RDDwCon = null;
        if (this.getRootIterator != null) {
            currentRDD = this.getRootIterator.getRDD(context);
            RDDwCon = currentRDD.mapToPair(
                item -> new Tuple2<>(
                        new ArrayList<>(Collections.singleton(item)),
                        new DynamicContext(context.getRumbleRuntimeConfiguration())
                )
            );
        }
        // Iterate through the step iterators to transform the RDD
        for (RuntimeIterator stepIterator : this.stepIterators) {
            if (currentRDD == null) {
                currentRDD = stepIterator.getRDD(context);
                RDDwCon = currentRDD.mapToPair(
                    item -> new Tuple2<>(
                            new ArrayList<>(Collections.singleton(item)),
                            new DynamicContext(context.getRumbleRuntimeConfiguration())
                    )
                );
            } else {
                RDDwCon = RDDwCon.mapToPair(
                    tuple -> {
                        List<Item> items = tuple._1;
                        DynamicContext ctx = tuple._2;
                        ctx.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, items);
                        List<Item> result = stepIterator.applyInPath(ctx);
                        return new Tuple2<>(result, ctx);
                    }
                );
            }
        }
        return RDDwCon.keys().flatMap(List::iterator);
    }
}
