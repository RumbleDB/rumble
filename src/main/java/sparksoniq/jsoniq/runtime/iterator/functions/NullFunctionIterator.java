package sparksoniq.jsoniq.runtime.iterator.functions;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class NullFunctionIterator extends LocalFunctionCallIterator {

    protected NullFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        return new NullItem();
    }
}
