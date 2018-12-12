package sparksoniq.spark.closures;

import org.apache.spark.api.java.function.Function;
import scala.Tuple2;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.tuple.FlworTuple;

public class CountClauseClosure implements Function<Tuple2<FlworTuple, Long>, FlworTuple> {
    private String variableName;
    private IteratorMetadata metadata;

    public CountClauseClosure(String variableName, IteratorMetadata metadata) {
        this.variableName = variableName;
        this.metadata = metadata;
    }

    @Override
    public FlworTuple call(Tuple2<FlworTuple, Long> inputTuple) throws Exception {
        FlworTuple result = inputTuple._1;
        result.putValue(
                variableName,
                new IntegerItem(inputTuple._2.intValue(), ItemMetadata.fromIteratorMetadata(metadata)),
                true
        );
        return result;
    }
}
