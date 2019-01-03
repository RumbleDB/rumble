package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.closures.CountClauseClosure;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;

import java.util.Collections;

public class CountClauseSparkIterator extends FlowrClauseSparkIterator {
    public CountClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator variableReference, IteratorMetadata iteratorMetadata) {
        super(child, Collections.singletonList(variableReference), FLWOR_CLAUSES.COUNT, iteratorMetadata);
    }

    @Override
    public JavaRDD<FlworTuple> getRDD() {
        if (this._rdd == null) {
            VariableReferenceIterator variableReference = (VariableReferenceIterator)this._children.get(0);

            this._rdd = _child.getRDD();
            String variableName = variableReference.getVariableName();
            // zipWithIndex starts from 0, increment indices by 1 for jsoniq convention
            this._rdd = this._rdd.zipWithIndex().mapValues(index -> index + 1).map(new CountClauseClosure(variableName, getMetadata()));
        }
        return _rdd;
    }
}
