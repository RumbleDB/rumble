package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import java.util.List;
import java.util.Random;

/**
 * Builds the three-entry map (number/next/permute) returned by fn:random-number-generator, per F&amp;O 3.1
 * 4.9.1. Given the same seed, always builds an equal map (number and the seed chain reachable via next()
 * are a pure function of the seed).
 */
final class RandomNumberGeneratorMapBuilder {

    static final Name PERMUTE_PARAM_NAME = Name.createVariableInDefaultFunctionNamespace(
        "random-number-generator-permute-arg-b3f6a3f0-6c39-4d3b-9c62-2a6e6b0f9a49"
    );

    private RandomNumberGeneratorMapBuilder() {
    }

    static Item build(
            long seed,
            RuntimeStaticContext staticContext,
            DynamicContext moduleContext,
            ExceptionMetadata metadata
    ) {
        double number = new Random(seed).nextDouble();

        FunctionItem next = new FunctionItem(
                new FunctionIdentifier(
                        Name.createVariableInDefaultFunctionNamespace(
                            "random-number-generator-next-" + seed
                        ),
                        0
                ),
                List.of(),
                new FunctionSignature(List.of(), SequenceType.createSequenceType("map")),
                moduleContext,
                new RandomNumberGeneratorNextBodyIterator(seed, staticContext)
        );

        FunctionItem permute = new FunctionItem(
                new FunctionIdentifier(
                        Name.createVariableInDefaultFunctionNamespace(
                            "random-number-generator-permute-" + seed
                        ),
                        1
                ),
                List.of(PERMUTE_PARAM_NAME),
                new FunctionSignature(
                        List.of(SequenceType.createSequenceType("item*")),
                        SequenceType.createSequenceType("item*")
                ),
                moduleContext,
                new RandomNumberGeneratorPermuteBodyIterator(seed, staticContext)
        );

        List<Item> keys = List.of(
            ItemFactory.getInstance().createStringItem("number"),
            ItemFactory.getInstance().createStringItem("next"),
            ItemFactory.getInstance().createStringItem("permute")
        );
        List<List<Item>> values = List.of(
            List.of(ItemFactory.getInstance().createDoubleItem(number)),
            List.of(next),
            List.of(permute)
        );
        return ItemFactory.getInstance().createMapItem(keys, values, metadata, false);
    }
}
