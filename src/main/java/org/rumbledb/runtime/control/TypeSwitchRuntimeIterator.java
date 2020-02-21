package org.rumbledb.runtime.control;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.Collections;
import java.util.List;

public class TypeSwitchRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final List<TypeSwitchRuntimeIteratorCase> cases;
    private final TypeSwitchRuntimeIteratorCase defaultCase;
    private RuntimeIterator matchingIterator = null;
    private Item testValue;


    public TypeSwitchRuntimeIterator(
            RuntimeIterator test,
            List<TypeSwitchRuntimeIteratorCase> cases,
            TypeSwitchRuntimeIteratorCase defaultCase,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.add(test);
        for (TypeSwitchRuntimeIteratorCase typeSwitchCase : cases) {
            this.children.add(typeSwitchCase.getReturnIterator());
        }
        this.children.add(defaultCase.getReturnIterator());

        this.testField = test;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        initializeIterator(this.testField, this.cases, this.defaultCase);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.matchingIterator.open(this.currentDynamicContextForLocalExecution);
            Item nextItem = this.matchingIterator.next();
            this.matchingIterator.close();
            this.hasNext = false;
            return nextItem;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in typeSwitch statement",
                getMetadata()
        );
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.matchingIterator = null;
        initializeIterator(this.testField, this.cases, this.defaultCase);
    }

    private void initializeIterator(RuntimeIterator test, List<TypeSwitchRuntimeIteratorCase> cases, TypeSwitchRuntimeIteratorCase defaultCase) {

        this.testValue = test.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

        for (TypeSwitchRuntimeIteratorCase typeSwitchCase : cases) {
            if (testTypeMatch(typeSwitchCase))
                break;
        }

        if (this.matchingIterator == null) {
            if (defaultCase.getVariableName() != null) {
                this.currentDynamicContextForLocalExecution.addVariableValue(
                    defaultCase.getVariableName(),
                    Collections.singletonList(this.testValue)
                );
            }
            this.matchingIterator = defaultCase.getReturnIterator();
        }

        this.matchingIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.matchingIterator.hasNext();
        this.matchingIterator.close();
    }

    private boolean testTypeMatch(TypeSwitchRuntimeIteratorCase typeSwitchCase) {
        for (FlworVarSequenceType sequenceType : typeSwitchCase.getSequenceTypeUnion()) {
            if (this.testValue != null && this.testValue.isTypeOf(sequenceType.getSequence().getItemType())) {
                if (typeSwitchCase.getVariableName() != null) {
                    this.currentDynamicContextForLocalExecution.addVariableValue(
                        typeSwitchCase.getVariableName(),
                        Collections.singletonList(this.testValue)
                    );
                }
                this.matchingIterator = typeSwitchCase.getReturnIterator();
                return true;
            }
        }
        return false;
    }
}
