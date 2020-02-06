package sparksoniq.jsoniq.runtime.iterator.control;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.Collections;
import java.util.List;

public class TypeSwitchRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final List<TypeSwitchCase> cases;
    private final TypeSwitchCase defaultCase;
    private RuntimeIterator matchingIterator = null;
    private Item testValue;


    public TypeSwitchRuntimeIterator(
            RuntimeIterator test,
            List<TypeSwitchCase> cases,
            TypeSwitchCase defaultCase,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this._children.add(test);
        for (TypeSwitchCase typeSwitchCase : cases) {
            this._children.add(typeSwitchCase.getReturnIterator());
        }
        this._children.add(defaultCase.getReturnIterator());

        this.testField = test;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        initializeIterator(testField, cases, defaultCase);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            matchingIterator.open(_currentDynamicContextForLocalExecution);
            Item nextItem = matchingIterator.next();
            matchingIterator.close();
            this._hasNext = false;
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
        initializeIterator(testField, cases, defaultCase);
    }

    private void initializeIterator(RuntimeIterator test, List<TypeSwitchCase> cases, TypeSwitchCase defaultCase) {

        testValue = test.materializeFirstItemOrNull(_currentDynamicContextForLocalExecution);

        for (TypeSwitchCase typeSwitchCase : cases) {
            if (testTypeMatch(typeSwitchCase))
                break;
        }

        if (matchingIterator == null) {
            if (defaultCase.getVariableName() != null) {
                _currentDynamicContextForLocalExecution.addVariableValue(
                    defaultCase.getVariableName(),
                    Collections.singletonList(testValue)
                );
            }
            matchingIterator = defaultCase.getReturnIterator();
        }

        matchingIterator.open(_currentDynamicContextForLocalExecution);
        this._hasNext = matchingIterator.hasNext();
        matchingIterator.close();
    }

    private boolean testTypeMatch(TypeSwitchCase typeSwitchCase) {
        for (FlworVarSequenceType sequenceType : typeSwitchCase.getSequenceTypeUnion()) {
            if (testValue != null && testValue.isTypeOf(sequenceType.getSequence().getItemType())) {
                if (typeSwitchCase.getVariableName() != null) {
                    _currentDynamicContextForLocalExecution.addVariableValue(
                        typeSwitchCase.getVariableName(),
                        Collections.singletonList(testValue)
                    );
                }
                matchingIterator = typeSwitchCase.getReturnIterator();
                return true;
            }
        }
        return false;
    }
}
