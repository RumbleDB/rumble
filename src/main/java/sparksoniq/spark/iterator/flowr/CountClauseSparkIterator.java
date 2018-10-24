package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import sparksoniq.spark.tuple.FlworTuple;

import java.util.Collections;

public class CountClauseSparkIterator extends FlowrClauseSparkIterator {
    public CountClauseSparkIterator(RuntimeIterator child, IteratorMetadata iteratorMetadata) {
        super(Collections.singletonList(child), FLWOR_CLAUSES.COUNT, iteratorMetadata);
    }

    @Override
    public JavaRDD<FlworTuple> getTupleRDD() {
        return null;
    }
}
