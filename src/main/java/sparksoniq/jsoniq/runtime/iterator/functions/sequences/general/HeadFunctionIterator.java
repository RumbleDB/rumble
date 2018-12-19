package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import java.util.List;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class HeadFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _iterator;
    private Item _result;

    public HeadFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return _result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "head function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);
        if (_iterator.hasNext()) {
            this._hasNext = true;
            _result = _iterator.next();
        } else {
            this._hasNext = false;
        }
        _iterator.close();
    }
}
