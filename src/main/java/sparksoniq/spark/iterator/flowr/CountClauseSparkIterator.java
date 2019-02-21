package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.CountClauseClosure;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;

import java.util.Collections;

public class CountClauseSparkIterator extends SparkRuntimeTupleIterator {
    VariableReferenceIterator _variableReference;
    public CountClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator variableReference, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        _variableReference = (VariableReferenceIterator) variableReference;
    }

    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        if (this._rdd == null) {
            this._rdd = _child.getRDD(context);
            String variableName = _variableReference.getVariableName();
            // zipWithIndex starts from 0, increment indices by 1 for jsoniq convention
            this._rdd = this._rdd.zipWithIndex().mapValues(index -> index + 1).map(new CountClauseClosure(variableName, getMetadata()));
        }
        return _rdd;
    }
}
