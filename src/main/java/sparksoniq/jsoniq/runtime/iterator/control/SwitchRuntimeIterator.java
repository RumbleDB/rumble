package sparksoniq.jsoniq.runtime.iterator.control;

import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.Map;


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
        return matchingIterator.next();
    }

    @Override
    public boolean hasNext() {
        matchingIterator.open(_currentDynamicContext);
        boolean returnValue = false;
        if (matchingIterator.hasNext()) {
            returnValue = true;
        }
        matchingIterator.close();
        return returnValue;
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
        for (RuntimeIterator caseKey : cases.keySet()) {

            Item caseValue = getSingleItemOfTypeFromIterator(caseKey, Item.class,
                    new NonAtomicKeyException("Switch case test must be atomic", getMetadata().getExpressionMetadata()));
            caseKey.close();
            if (Item.checkEquality(testValue, caseValue)) {
                matchingIterator = cases.get(caseKey);
                break;
            }
        }

        if (matchingIterator == null)
            matchingIterator = defaultReturn;
    }
}
