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
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> testField;
    private final Map<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>, org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> cases;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> defaultReturn;

    public SwitchStatementIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> testField,
            Map<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>, org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> cases,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> defaultReturn,
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

    private org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        return selectApplicableIterator(
            iterator -> iterator.materializeFirstOrNull(dynamicContext)
        );
    }

    private org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> selectApplicableIterator(
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

        for (org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> caseKey : this.cases.keySet()) {
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
        org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> matchingIterator = this.selectApplicableIterator(
            dynamicContext
        );
        DynamicContext childContext = new DynamicContext(dynamicContext);
        matchingIterator.materialize(childContext);
        return null;
    }
}
