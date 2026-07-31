package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.misc.AtomicDeepEqual;

import java.io.Serial;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class SwitchStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> testField;
    private final Map<RuntimePlan<Item>, RuntimePlan<Item>> cases;
    private final RuntimePlan<Item> defaultReturn;

    public SwitchStatementIterator(
            RuntimePlan<Item> testField,
            Map<RuntimePlan<Item>, RuntimePlan<Item>> cases,
            RuntimePlan<Item> defaultReturn,
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

    private RuntimePlan<Item> selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        return selectApplicableIterator(
            iterator -> iterator.materializeFirstOrNull(dynamicContext)
        );
    }

    private RuntimePlan<Item> selectApplicableIterator(
            Function<RuntimePlan<Item>, Item> materializeFirst
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

        for (RuntimePlan<Item> caseKey : this.cases.keySet()) {
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
        RuntimePlan<Item> matchingIterator = this.selectApplicableIterator(
            dynamicContext
        );
        DynamicContext childContext = new DynamicContext(dynamicContext);
        matchingIterator.materialize(childContext);
        return null;
    }
}
