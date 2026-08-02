package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.misc.AtomicDeepEqual;

import java.io.Serial;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class SwitchStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan testField;
    private final Map<ItemRuntimePlan, ItemRuntimePlan> cases;
    private final ItemRuntimePlan defaultReturn;

    public SwitchStatementIterator(
            ItemRuntimePlan testField,
            Map<ItemRuntimePlan, ItemRuntimePlan> cases,
            ItemRuntimePlan defaultReturn,
            RuntimeStaticContext staticContext
    ) {
        super(
            Stream.of(Stream.of(testField), cases.keySet().stream(), cases.values().stream(), Stream.of(defaultReturn))
                .flatMap(Function.identity())
                .toList(),
            staticContext
        );

        this.testField = testField;
        this.cases = cases;
        this.defaultReturn = defaultReturn;
    }

    private ItemRuntimePlan selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        return selectApplicableIterator(
            iterator -> iterator.materializeFirstOrNull(dynamicContext)
        );
    }

    private ItemRuntimePlan selectApplicableIterator(
            Function<ItemRuntimePlan, Item> materializeFirst
    ) {
        Item testValue = materializeFirst.apply(this.testField);

        if (testValue != null) {
            if (testValue.isArray()) {
                throw new NonAtomicKeyException(
                        "Invalid args. Switch condition cannot be an array type",
                        getMetadata()
                );
            } else if (testValue.isObject()) {
                throw new NonAtomicKeyException(
                        "Invalid args. Switch condition cannot be an object type",
                        getMetadata()
                );
            }
        }

        for (ItemRuntimePlan caseKey : this.cases.keySet()) {
            Item caseValue = materializeFirst.apply(caseKey);

            if (caseValue != null) {
                if (caseValue.isArray()) {
                    throw new NonAtomicKeyException(
                            "Invalid args. Switch case cannot be an array type",
                            getMetadata()
                    );
                } else if (caseValue.isObject()) {
                    throw new NonAtomicKeyException(
                            "Invalid args. Switch case  cannot be an object type",
                            getMetadata()
                    );
                }
            }

            // both are empty sequences
            if (testValue == null) {
                if (caseValue == null) {
                    return this.cases.get(caseKey);
                } else {
                    break;
                }
            }
            if (AtomicDeepEqual.deepEqual(testValue, caseValue)) {
                return this.cases.get(caseKey);
            }
        }

        return this.defaultReturn;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        ItemRuntimePlan matchingIterator = this.selectApplicableIterator(
            dynamicContext
        );
        DynamicContext childContext = new DynamicContext(dynamicContext);
        matchingIterator.materialize(childContext);
        return null;
    }
}
