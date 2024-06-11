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

public class RandomSequenceGeneratorIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public RandomSequenceGeneratorIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Random random = new Random();
        List<Item> randomSequence = new ArrayList<>();

        Item seed = this.children.get(0).materializeFirstItemOrNull(context);
        Item sequenceLength = this.children.get(1).materializeFirstItemOrNull(context);
        random.setSeed(seed.castToIntValue());
        random.doubles(sequenceLength.castToIntValue()).forEach(randomDouble -> {
            randomSequence.add(ItemFactory.getInstance().createDoubleItem(randomDouble));
        });
        return ItemFactory.getInstance().createArrayItem(randomSequence);
    }
}
