package org.rumbledb.runtime.xml;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.runtime.RuntimeIterator;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SlashExprClosureZipped implements FlatMapFunction<Tuple2<Item, Long>, Item> {


    private static final long serialVersionUID = 1L;
    private final RuntimeIterator rightIterator;
    private final DynamicContext dynamicContext;
    private final long contextSize;

    public SlashExprClosureZipped(
            RuntimeIterator rightIterator,
            DynamicContext dynamicContext,
            long contextSize
    ) {
        this.rightIterator = rightIterator;
        if (this.rightIterator.isSparkJobNeeded()) {
            throw new JobWithinAJobException(
                    "The right-hand side of this slash expression requires parallel execution, but the slash expression is itself executed in parallel.",
                    this.rightIterator.getMetadata()
            );
        }
        this.dynamicContext = dynamicContext;
        this.contextSize = contextSize;
    }

    public Iterator<Item> call(Tuple2<Item, Long> itemWithIndex) {
        List<Item> currentItems = new ArrayList<>();
        currentItems.add(itemWithIndex._1());
        DynamicContext currentContext = new DynamicContext(this.dynamicContext);
        currentContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, currentItems);
        currentContext.getVariableValues().setPosition(itemWithIndex._2() + 1);
        currentContext.getVariableValues().setLast(this.contextSize);
        List<Item> result = this.rightIterator.materialize(currentContext);
        return result.iterator();
    }
}
