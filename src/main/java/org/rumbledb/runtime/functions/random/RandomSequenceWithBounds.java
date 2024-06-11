package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSequenceWithBounds extends AtMostOneItemLocalRuntimeIterator {
    public RandomSequenceWithBounds(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item low = this.children.get(0).materializeFirstItemOrNull(context);
        Item high = this.children.get(1).materializeFirstItemOrNull(context);
        Item size = this.children.get(2).materializeFirstItemOrNull(context);
        Item type = this.children.get(3).materializeFirstItemOrNull(context);
        Random random = new Random();
        List<Item> result = new ArrayList<>();
        if (type.getStringValue().equals("integer")) {
            random.ints(size.castToIntValue(), low.castToIntValue(), high.castToIntValue())
                .forEach(randomInteger -> result.add(ItemFactory.getInstance().createIntItem(randomInteger)));
        } else {
            // Generate doubles otherwise
            random.doubles(size.castToIntValue(), low.castToDoubleValue(), high.castToDoubleValue())
                .forEach(randomDouble -> result.add(ItemFactory.getInstance().createDoubleItem(randomDouble)));
        }
        return ItemFactory.getInstance().createArrayItem(result);
    }
}
