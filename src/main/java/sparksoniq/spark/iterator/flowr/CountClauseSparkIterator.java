package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.spark.closures.CountClauseClosure;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import sparksoniq.spark.tuple.FlworTuple;

import java.util.Collections;

public class CountClauseSparkIterator extends FlowrClauseSparkIterator {
    public CountClauseSparkIterator(RuntimeIterator child, IteratorMetadata iteratorMetadata) {
        super(Collections.singletonList(child), FLWOR_CLAUSES.COUNT, iteratorMetadata);
    }

    @Override
    public JavaRDD<FlworTuple> getTupleRDD() {
        if (this._rdd == null) {
            VariableReferenceIterator variableReference = (VariableReferenceIterator)this._children.get(0);

            this._rdd = _previousClause.getTupleRDD();
            String variableName = variableReference.getVariableName();
            this._rdd = this._rdd.zipWithIndex().map(new CountClauseClosure(variableName, getMetadata()));
        }
        return _rdd;
    }
}
