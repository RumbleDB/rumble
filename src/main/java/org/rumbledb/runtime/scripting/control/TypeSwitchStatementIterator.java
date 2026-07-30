package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.control.TypeswitchRuntimeIteratorCase;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class TypeSwitchStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final List<TypeswitchRuntimeIteratorCase> cases;
    private final TypeswitchRuntimeIteratorCase defaultCase;

    public TypeSwitchStatementIterator(
            RuntimeIterator testField,
            List<TypeswitchRuntimeIteratorCase> cases,
            TypeswitchRuntimeIteratorCase defaultCase,
            RuntimeStaticContext staticContext
    ) {
        super(
            Stream.of(
                Stream.of(testField),
                cases.stream().map(TypeswitchRuntimeIteratorCase::getReturnIterator),
                Stream.of(defaultCase.getReturnIterator())
            ).flatMap(Function.identity()).toList(),
            staticContext
        );
        this.testField = testField;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        DynamicContext childContext = new DynamicContext(context);
        return execute(
            this.testField.materializeFirstOrNull(context),
            childContext,
            RuntimeIterator::materialize
        );
    }

    private Item execute(
            Item value,
            DynamicContext childContext,
            java.util.function.BiConsumer<RuntimeIterator, DynamicContext> materialize
    ) {
        RuntimeIterator selected = selectIterator(value, childContext);
        materialize.accept(selected, childContext);
        return null;
    }

    private RuntimeIterator selectIterator(Item value, DynamicContext childContext) {
        for (TypeswitchRuntimeIteratorCase typeSwitchCase : this.cases) {
            RuntimeIterator selected = testTypeMatchAndReturnCorrespondingIterator(typeSwitchCase, value);
            if (selected != null) {
                if (typeSwitchCase.getVariableName() != null) {
                    childContext.getVariableValues()
                        .addVariableValue(
                            typeSwitchCase.getVariableName(),
                            Collections.singletonList(value)
                        );
                }
                return selected;
            }
        }

        if (this.defaultCase.getVariableName() != null) {
            childContext.getVariableValues()
                .addVariableValue(
                    this.defaultCase.getVariableName(),
                    Collections.singletonList(value)
                );
        }
        return this.defaultCase.getReturnIterator();
    }

    private RuntimeIterator testTypeMatchAndReturnCorrespondingIterator(
            TypeswitchRuntimeIteratorCase typeSwitchCase,
            Item value
    ) {
        if (typeSwitchCase.getSequenceTypeUnion() != null) {
            for (SequenceType sequenceType : typeSwitchCase.getSequenceTypeUnion()) {
                if (value == null && sequenceType.isEmptySequence()) {
                    return typeSwitchCase.getReturnIterator();
                }
                if (
                    value != null
                        && InstanceOfIterator.doesItemTypeMatchItem(sequenceType.getItemType(), value)
                ) {
                    return typeSwitchCase.getReturnIterator();
                }
            }
        }
        return null;
    }
}
