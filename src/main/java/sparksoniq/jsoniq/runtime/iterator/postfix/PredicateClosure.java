package sparksoniq.jsoniq.runtime.iterator.postfix;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.Function;

public class PredicateClosure implements Function<Item, Boolean> {
    private final RuntimeIterator _expression;
    private final DynamicContext _dynamicContext;

    public PredicateClosure(RuntimeIterator expression, DynamicContext dynamicContext) {
        _expression = expression;
        _dynamicContext = dynamicContext;
    }

    @Override
    public Boolean call(Item v1) throws Exception {
        List<Item> currentItems = new ArrayList<>();
        currentItems.add(v1);
        DynamicContext dynamicContext = new DynamicContext(_dynamicContext);
        dynamicContext.addVariableValue("$$", currentItems);

        _expression.open(dynamicContext);
        Item result = _expression.next();
        _expression.close();
        return result.getBooleanValue();
    }

};