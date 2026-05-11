package org.rumbledb.runtime.xml;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SlashExprClosure implements FlatMapFunction<Item, Item> {


    private static final long serialVersionUID = 1L;
    private final RuntimeIterator rightIterator;
    private final DynamicContext dynamicContext;

    public SlashExprClosure(RuntimeIterator rightIterator, DynamicContext dynamicContext) {
        this.rightIterator = rightIterator;
        this.dynamicContext = new DynamicContext(dynamicContext);
    }

    public Iterator<Item> call(Item item) throws Exception {
        List<Item> currentItems = new ArrayList<>();
        currentItems.add(item);
        this.dynamicContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, currentItems);
        List<Item> result = this.rightIterator.materialize(this.dynamicContext);
        return result.iterator();
    }
}
