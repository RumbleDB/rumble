package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.control.TypeswitchRuntimeIteratorCase;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

import java.util.Collections;
import java.util.List;

public class TypeSwitchStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final List<TypeswitchRuntimeIteratorCase> cases;
    private final TypeswitchRuntimeIteratorCase defaultCase;
    private RuntimeIterator matchingIterator;
    private Item testValue;

    public TypeSwitchStatementIterator(
            RuntimeIterator testField,
            List<TypeswitchRuntimeIteratorCase> cases,
            TypeswitchRuntimeIteratorCase defaultCase,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.children.add(testField);
        for (TypeswitchRuntimeIteratorCase typeSwitchCase : cases) {
            this.children.add(typeSwitchCase.getReturnIterator());
        }
        this.children.add(defaultCase.getReturnIterator());

        this.testField = testField;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        DynamicContext childContext = new DynamicContext(context);
        this.currentDynamicContextForLocalExecution = childContext;
        initializeIterator();
        this.matchingIterator.materialize(childContext);
        return null;
    }

    private void initializeIterator() {

        this.testValue = this.testField.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

        for (TypeswitchRuntimeIteratorCase typeSwitchCase : this.cases) {
            this.matchingIterator = testTypeMatchAndReturnCorrespondingIterator(typeSwitchCase);
            if (this.matchingIterator != null) {
                if (typeSwitchCase.getVariableName() != null) {
                    this.currentDynamicContextForLocalExecution.getVariableValues()
                        .addVariableValue(
                            typeSwitchCase.getVariableName(),
                            Collections.singletonList(this.testValue)
                        );
                }
                break;
            }
        }

        if (this.matchingIterator == null) {
            if (this.defaultCase.getVariableName() != null) {
                this.currentDynamicContextForLocalExecution.getVariableValues()
                    .addVariableValue(
                        this.defaultCase.getVariableName(),
                        Collections.singletonList(this.testValue)
                    );
            }
            this.matchingIterator = this.defaultCase.getReturnIterator();
        }
    }

    private RuntimeIterator testTypeMatchAndReturnCorrespondingIterator(TypeswitchRuntimeIteratorCase typeSwitchCase) {
        if (typeSwitchCase.getSequenceTypeUnion() != null) {
            for (SequenceType sequenceType : typeSwitchCase.getSequenceTypeUnion()) {
                if (this.testValue == null && sequenceType.isEmptySequence()) {
                    return typeSwitchCase.getReturnIterator();
                }
                if (
                    this.testValue != null
                        && InstanceOfIterator.doesItemTypeMatchItem(sequenceType.getItemType(), this.testValue)
                ) {
                    return typeSwitchCase.getReturnIterator();
                }
            }
        }
        return null;
    }
}
