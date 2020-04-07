package org.rumbledb.runtime.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.Collections;
import java.util.List;

public class TypeswitchRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final List<TypeswitchRuntimeIteratorCase> cases;
    private final TypeswitchRuntimeIteratorCase defaultCase;
    private RuntimeIterator matchingIterator;
    private Item testValue;


    public TypeswitchRuntimeIterator(
            RuntimeIterator test,
            List<TypeswitchRuntimeIteratorCase> cases,
            TypeswitchRuntimeIteratorCase defaultCase,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.add(test);
        for (TypeswitchRuntimeIteratorCase typeSwitchCase : cases) {
            this.children.add(typeSwitchCase.getReturnIterator());
        }
        this.children.add(defaultCase.getReturnIterator());

        this.testField = test;
        this.cases = cases;
        this.defaultCase = defaultCase;
        this.matchingIterator = null;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        // this.matchingIterator is null at that point;
        if (this.matchingIterator != null) {
            throw new IteratorFlowException(
                    "The matching iterator should be null when opening the typeswitch iterator!"
            );
        }
        initializeIterator();
    }

    private void resetMatchingIteratorToNull() {
        if (this.matchingIterator != null) {
            this.matchingIterator.close();
        }
        this.matchingIterator = null;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item nextItem = this.matchingIterator.next();
            this.hasNext = this.matchingIterator.hasNext();
            if (!this.hasNext()) {
                resetMatchingIteratorToNull();
            }
            return nextItem;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in typeSwitch statement",
                getMetadata()
        );
    }

    @Override
    public void close() {
        super.close();
        resetMatchingIteratorToNull();
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        resetMatchingIteratorToNull();
        initializeIterator();
    }

    private void initializeIterator() {

        this.testValue = this.testField.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

        for (TypeswitchRuntimeIteratorCase typeSwitchCase : this.cases) {
            this.matchingIterator = testTypeMatchAndReturnCorrespondingIterator(typeSwitchCase);
            if (this.matchingIterator != null) {
                if (typeSwitchCase.getVariableName() != null) {
                    this.currentDynamicContextForLocalExecution.addVariableValue(
                        typeSwitchCase.getVariableName(),
                        Collections.singletonList(this.testValue)
                    );
                }
                break;
            }
        }

        if (this.matchingIterator == null) {
            if (this.defaultCase.getVariableName() != null) {
                this.currentDynamicContextForLocalExecution.addVariableValue(
                    this.defaultCase.getVariableName(),
                    Collections.singletonList(this.testValue)
                );
            }
            this.matchingIterator = this.defaultCase.getReturnIterator();
        }

        this.matchingIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.matchingIterator.hasNext();
    }

    private RuntimeIterator testTypeMatchAndReturnCorrespondingIterator(TypeswitchRuntimeIteratorCase typeSwitchCase) {
        if (typeSwitchCase.getSequenceTypeUnion() != null) {
            for (SequenceType sequenceType : typeSwitchCase.getSequenceTypeUnion()) {
                if (this.testValue != null && this.testValue.isTypeOf(sequenceType.getItemType())) {
                    return typeSwitchCase.getReturnIterator();
                }
            }
        }
        return null;
    }
}
