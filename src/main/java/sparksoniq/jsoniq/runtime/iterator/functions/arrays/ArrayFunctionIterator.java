package sparksoniq.jsoniq.runtime.iterator.functions.arrays;

import java.util.List;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public abstract class ArrayFunctionIterator extends LocalFunctionCallIterator {
    private final ArrayFunctionOperators _operator;

    protected ArrayFunctionIterator(List<RuntimeIterator> arguments, ArrayFunctionOperators op,
                                    IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        this._operator = op;
    }

    public enum ArrayFunctionOperators {
        SIZE,
        MEMBERS,
        DESCENDANT,
        FLATTEN
    }
}
