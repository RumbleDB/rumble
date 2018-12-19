package sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality;

import java.util.List;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public abstract class CardinalityFunctionIterator extends LocalFunctionCallIterator {

    protected CardinalityFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

}
