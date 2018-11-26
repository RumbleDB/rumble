package sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.storage.StorageLevel;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SequenceExceptionZeroOrOne;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class ZeroOrOneIterator extends CardinalityFunctionIterator {

    private Item _nextResult;

    public ZeroOrOneIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return _nextResult;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ZERO-OR-ONE function",
                getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this._children.get(0);

        if (!sequenceIterator.isRDD()) {
            sequenceIterator.open(context);
            if (!sequenceIterator.hasNext()) {
                this._hasNext = false;
            } else {
                _nextResult = sequenceIterator.next();
                if (sequenceIterator.hasNext()) {
                    throw new SequenceExceptionZeroOrOne("fn:zero-or-one() called with a sequence containing more than one item", getMetadata());
                } else {
                    this._hasNext = true;
                }
            }
            sequenceIterator.close();
        } else {
            JavaRDD<Item> rdd = sequenceIterator.getRDD();
            rdd.persist(StorageLevel.MEMORY_ONLY());
            long count = rdd.count();
            if (count == 0) {
                this._hasNext = false;
            } else if (count == 1) {
                this._hasNext = true;
                _nextResult = rdd.collect().get(0);
            } else if (count > 1) {
                throw new SequenceExceptionZeroOrOne("fn:zero-or-one() called with a sequence containing more than one item", getMetadata());
            }
        }
    }
}
