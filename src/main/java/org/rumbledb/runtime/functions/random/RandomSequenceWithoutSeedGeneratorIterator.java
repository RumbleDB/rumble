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

public class RandomSequenceWithoutSeedGeneratorIterator extends AtMostOneItemLocalRuntimeIterator {
    public RandomSequenceWithoutSeedGeneratorIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Random random = new Random();
        Item sequenceLength = this.children.get(0).materializeFirstItemOrNull(context);
        List<Item> randomSequence = new ArrayList<>();
        random.doubles(sequenceLength.castToIntValue()).forEach(randomDouble -> {
            randomSequence.add(ItemFactory.getInstance().createDoubleItem(randomDouble));
        });
        return ItemFactory.getInstance().createArrayItem(randomSequence);
    }
}
