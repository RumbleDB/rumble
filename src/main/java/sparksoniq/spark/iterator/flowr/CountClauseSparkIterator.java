package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.CountClauseClosure;

import java.util.ArrayList;
import java.util.List;

public class CountClauseSparkIterator extends SparkRuntimeTupleIterator {
    private String _variableName;
    private FlworTuple _nextLocalTupleResult;
    private int _currentCountIndex;

    public CountClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator variableReference, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        _variableName = ((VariableReferenceIterator) variableReference).getVariableName();
        _currentCountIndex = 1 ;    // indices start at 1 in JSONiq
    }

    @Override
    public boolean isRDD() {
        return _child.isRDD();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        // isRDD checks omitted, as open is used for non-RDD(local) operations

        if (this._child != null) {
            _child.open(_currentDynamicContext);

            setNextLocalTupleResult();
        } else {
            throw new SparksoniqRuntimeException("Invalid count clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if(_hasNext == true){
            FlworTuple result = _nextLocalTupleResult;      // save the result to be returned
            setNextLocalTupleResult();              // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in count flwor clause", getMetadata());
    }

    private void setNextLocalTupleResult() {
        if (_child.hasNext()) {
            FlworTuple inputTuple = _child.next();

            List<Item> results = new ArrayList<>();
            results.add(new IntegerItem(_currentCountIndex++));

            FlworTuple newTuple = new FlworTuple(inputTuple, _variableName, results);
            _nextLocalTupleResult = newTuple;
            this._hasNext = true;
        } else {
            _child.close();
            this._hasNext = false;
        }
    }


    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        String variableName = _variableName;
        // zipWithIndex starts from 0, increment indices by 1 for jsoniq convention
        return _child.getRDD(context).zipWithIndex()
                .mapValues(index -> index + 1)
                .map(new CountClauseClosure(variableName, getMetadata()));
    }
}
