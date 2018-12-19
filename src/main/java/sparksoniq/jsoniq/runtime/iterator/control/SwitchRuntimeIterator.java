package sparksoniq.jsoniq.runtime.iterator.control;

import java.util.Map;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;


public class SwitchRuntimeIterator extends LocalRuntimeIterator {

    private final RuntimeIterator testField;
    private final Map<RuntimeIterator, RuntimeIterator> cases;
    private final RuntimeIterator defaultReturn;
    private RuntimeIterator matchingIterator = null;

    public SwitchRuntimeIterator(RuntimeIterator test, Map<RuntimeIterator, RuntimeIterator> cases,
                                 RuntimeIterator defaultReturn, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(test);
        for (RuntimeIterator key : cases.keySet())
            this._children.add(key);
        for (RuntimeIterator value : cases.values())
            this._children.add(value);
        this._children.add(defaultReturn);
        this.testField = test;
        this.cases = cases;
        this.defaultReturn = defaultReturn;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        initializeIterator(testField, cases, defaultReturn);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            matchingIterator.open(_currentDynamicContext);
            Item nextItem = matchingIterator.next();
            matchingIterator.close();
            this._hasNext = false;
            return nextItem;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in switch statement",
                getMetadata());
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.matchingIterator = null;
        initializeIterator(testField, cases, defaultReturn);
    }

    private void initializeIterator(RuntimeIterator test, Map<RuntimeIterator, RuntimeIterator> cases,
                                    RuntimeIterator defaultReturn) {
        Item testValue = getSingleItemOfTypeFromIterator(test, Item.class,
                new NonAtomicKeyException("Switch test must be atomic", getMetadata().getExpressionMetadata()));

        if (testValue instanceof ArrayItem) {
            throw new NonAtomicKeyException("Invalid args. Switch condition can't be an array type", getMetadata().getExpressionMetadata());
        }
        else if (testValue instanceof ObjectItem) {
            throw new NonAtomicKeyException("Invalid args. Switch condition  can't be an object type", getMetadata().getExpressionMetadata());
        }

        for (RuntimeIterator caseKey : cases.keySet()) {
            Item caseValue = getSingleItemOfTypeFromIterator(caseKey, Item.class,
                    new NonAtomicKeyException("Switch case test must be atomic", getMetadata().getExpressionMetadata()));

            if (caseValue instanceof ArrayItem) {
                throw new NonAtomicKeyException("Invalid args. Switch case can't be an array type", getMetadata().getExpressionMetadata());
            }
            else if (caseValue instanceof ObjectItem) {
                throw new NonAtomicKeyException("Invalid args. Switch case  can't be an object type", getMetadata().getExpressionMetadata());
            }

            // both are empty sequences
            if (testValue == null) {
                if (caseValue == null) {
                    matchingIterator = cases.get(caseKey);
                    break;
                } else {
                    // no match, do nothing
                }
            }
            else if (caseValue != null && Item.checkEquality(testValue, caseValue)) {
                matchingIterator = cases.get(caseKey);
                break;
            }
        }

        if (matchingIterator == null)
            matchingIterator = defaultReturn;

        matchingIterator.open(_currentDynamicContext);
        this._hasNext = matchingIterator.hasNext();
        matchingIterator.close();
    }
}
