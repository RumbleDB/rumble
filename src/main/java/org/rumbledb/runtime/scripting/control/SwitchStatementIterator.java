package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.misc.AtomicDeepEqual;

import java.io.Serial;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class SwitchStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
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

    private RuntimeIterator selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        return selectApplicableIterator(
            iterator -> iterator.materializeFirstItemOrNull(dynamicContext)
        );
    }

    private RuntimeIterator selectApplicableIterator(Function<RuntimeIterator, Item> materializeFirst) {
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

        for (RuntimeIterator caseKey : this.cases.keySet()) {
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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> {
                    RuntimeIterator matchingIterator = selectApplicableIterator(
                        iterator -> iterator.materializeFirstOrNull(context)
                    );
                    matchingIterator.materialize(new DynamicContext(context));
                    return null;
                },
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        RuntimeIterator matchingIterator = this.selectApplicableIterator(dynamicContext);
        DynamicContext childContext = new DynamicContext(dynamicContext);
        matchingIterator.materialize(childContext);
        return null;
    }
}
