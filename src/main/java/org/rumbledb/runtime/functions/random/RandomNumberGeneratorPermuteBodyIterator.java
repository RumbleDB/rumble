package org.rumbledb.runtime.functions.random;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Body of the "permute" entry of a random-number-generator map: a seed-deterministic Fisher-Yates shuffle
 * of the bound "arg" parameter.
 */
public class RandomNumberGeneratorPermuteBodyIterator extends HybridRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private final long seed;
    private List<Item> results;
    private int currentIndex;

    public RandomNumberGeneratorPermuteBodyIterator(long seed, RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
        this.seed = seed;
    }

    @Override
    protected void openLocal() {
        List<Item> items = new ArrayList<>(
                this.currentDynamicContextForLocalExecution.getVariableValues()
                    .getLocalVariableValue(RandomNumberGeneratorMapBuilder.PERMUTE_PARAM_NAME, getMetadata())
        );
        Random random = new Random(this.seed);
        for (int i = items.size() - 1; i > 0; --i) {
            int j = random.nextInt(i + 1);
            Item temp = items.get(i);
            items.set(i, items.get(j));
            items.set(j, temp);
        }
        this.results = items;
        this.currentIndex = 0;
        this.hasNext = !this.results.isEmpty();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " random-number-generator permute",
                    getMetadata()
            );
        }
        Item result = this.results.get(this.currentIndex++);
        this.hasNext = this.currentIndex < this.results.size();
        return result;
    }

    @Override
    protected void closeLocal() {
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException(
                "random-number-generator permute is currently supported only in local execution mode."
        );
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "random-number-generator permute is currently supported only in local execution mode."
        );
    }
}
