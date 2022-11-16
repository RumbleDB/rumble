package org.rumbledb.runtime.control;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

import java.util.Collections;
import java.util.List;

public class TypeswitchRuntimeIterator extends HybridRuntimeIterator {

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
    public void openLocal() {
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
    public Item nextLocal() {
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
    public void closeLocal() {
        resetMatchingIteratorToNull();
    }

    @Override
    public void resetLocal() {
        resetMatchingIteratorToNull();
        initializeIterator();
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

        this.matchingIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.matchingIterator.hasNext();
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

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        this.testValue = this.testField.materializeFirstItemOrNull(dynamicContext);
        RuntimeIterator localMatchingIterator;

        for (TypeswitchRuntimeIteratorCase typeSwitchCase : this.cases) {
            localMatchingIterator = testTypeMatchAndReturnCorrespondingIterator(typeSwitchCase);
            if (localMatchingIterator != null) {
                if (typeSwitchCase.getVariableName() != null) {
                    dynamicContext.getVariableValues()
                        .addVariableValue(
                            typeSwitchCase.getVariableName(),
                            Collections.singletonList(this.testValue)
                        );
                }

                return localMatchingIterator.getRDD(dynamicContext);
            }
        }

        if (this.defaultCase.getVariableName() != null) {
            dynamicContext.getVariableValues()
                .addVariableValue(
                    this.defaultCase.getVariableName(),
                    Collections.singletonList(this.testValue)
                );
        }

        return this.defaultCase.getReturnIterator().getRDD(dynamicContext);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        this.testValue = this.testField.materializeFirstItemOrNull(context);
        RuntimeIterator localMatchingIterator;

        for (TypeswitchRuntimeIteratorCase typeSwitchCase : this.cases) {
            localMatchingIterator = testTypeMatchAndReturnCorrespondingIterator(typeSwitchCase);
            if (localMatchingIterator != null) {
                if (typeSwitchCase.getVariableName() != null) {
                    context.getVariableValues()
                            .addVariableValue(
                                    typeSwitchCase.getVariableName(),
                                    Collections.singletonList(this.testValue)
                            );
                }

                return localMatchingIterator.getDataFrame(context);
            }
        }

        if (this.defaultCase.getVariableName() != null) {
            context.getVariableValues()
                    .addVariableValue(
                            this.defaultCase.getVariableName(),
                            Collections.singletonList(this.testValue)
                    );
        }

        return this.defaultCase.getReturnIterator().getDataFrame(context);
    }
}
