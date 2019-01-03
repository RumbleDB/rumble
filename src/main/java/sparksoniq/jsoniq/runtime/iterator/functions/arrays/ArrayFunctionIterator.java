package sparksoniq.jsoniq.runtime.iterator.functions.arrays;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

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
