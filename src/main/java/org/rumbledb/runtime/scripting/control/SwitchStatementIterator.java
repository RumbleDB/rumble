package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;

import java.util.Map;

public class SwitchStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final Map<RuntimeIterator, RuntimeIterator> cases;
    private final RuntimeIterator defaultReturn;

    public SwitchStatementIterator(
            RuntimeIterator testField,
            Map<RuntimeIterator, RuntimeIterator> cases,
            RuntimeIterator defaultReturn,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.children.add(testField);
        this.children.addAll(cases.keySet());
        this.children.addAll(cases.values());
        this.children.add(defaultReturn);
        this.testField = testField;
        this.cases = cases;
        this.defaultReturn = defaultReturn;
    }

    private RuntimeIterator selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        Item testValue = this.testField.materializeFirstItemOrNull(dynamicContext);

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

        for (RuntimeIterator caseKey : this.cases.keySet()) {
            Item caseValue = caseKey.materializeFirstItemOrNull(dynamicContext);

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
            long comparison = ComparisonIterator.compareItems(
                testValue,
                caseValue,
                ComparisonExpression.ComparisonOperator.VC_EQ,
                getMetadata()
            );
            if (comparison == 0) {
                return this.cases.get(caseKey);
            }
        }

        return this.defaultReturn;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        RuntimeIterator matchingIterator = this.selectApplicableIterator(dynamicContext);
        DynamicContext childContext = new DynamicContext(dynamicContext);
        matchingIterator.materialize(childContext);
        return null;
    }
}
