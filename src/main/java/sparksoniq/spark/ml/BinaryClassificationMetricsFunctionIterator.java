package sparksoniq.spark.ml;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.ConstantRDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import scala.Tuple2;

public class BinaryClassificationMetricsFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public BinaryClassificationMetricsFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        JavaRDD<Item> scoresAndLabels = this.children.get(0).getRDD(context);
        String scoreCol = this.children.get(1).materializeFirstItemOrNull(context).getStringValue();
        String labelCol = this.children.get(2).materializeFirstItemOrNull(context).getStringValue();
        int numBins = -1;
        if (this.children.size() > 3) {
            numBins = this.children.get(3).materializeFirstItemOrNull(context).getIntValue();
        }
        JavaPairRDD<Object, Object> predictionAndLabels = scoresAndLabels.mapToPair(
            p -> new Tuple2<>(
                    p.getItemByKey(scoreCol).castToDoubleValue(),
                    p.getItemByKey(labelCol).castToDoubleValue()
            )
        );
        BinaryClassificationMetrics bcm = null;
        if (numBins == -1) {
            bcm = new BinaryClassificationMetrics(predictionAndLabels.rdd());
        } else {
            bcm = new BinaryClassificationMetrics(predictionAndLabels.rdd(), numBins);
        }
        Item objectItem = ItemFactory.getInstance().createLazyObjectItem();
        objectItem.putItemByKey("areaUnderPR", ItemFactory.getInstance().createDoubleItem(bcm.areaUnderPR()));
        objectItem.putItemByKey("areaUnderROC", ItemFactory.getInstance().createDoubleItem(bcm.areaUnderROC()));
        JavaRDD<Item> rdd = tupleToArrays(bcm.pr().toJavaRDD(), "recall", "precision");
        RuntimeIterator it = new ConstantRDDRuntimeIterator(
                rdd,
                new RuntimeStaticContext(getConfiguration(), SequenceType.OBJECTS, ExecutionMode.RDD, getMetadata())
        );
        objectItem.putLazyItemByKey("pr", it, context, true);
        rdd = tupleToArrays(bcm.fMeasureByThreshold().toJavaRDD(), "threshold", "F-Measure");
        it = new ConstantRDDRuntimeIterator(
                rdd,
                new RuntimeStaticContext(getConfiguration(), SequenceType.OBJECTS, ExecutionMode.RDD, getMetadata())
        );
        objectItem.putLazyItemByKey("fMeasureByThreshold", it, context, true);
        rdd = tupleToArrays(bcm.precisionByThreshold().toJavaRDD(), "threshold", "precision");
        it = new ConstantRDDRuntimeIterator(
                rdd,
                new RuntimeStaticContext(getConfiguration(), SequenceType.OBJECTS, ExecutionMode.RDD, getMetadata())
        );
        objectItem.putLazyItemByKey("precisionByThreshold", it, context, true);
        rdd = tupleToArrays(bcm.recallByThreshold().toJavaRDD(), "threshold", "recall");
        it = new ConstantRDDRuntimeIterator(
                rdd,
                new RuntimeStaticContext(getConfiguration(), SequenceType.OBJECTS, ExecutionMode.RDD, getMetadata())
        );
        objectItem.putLazyItemByKey("recallByThreshold", it, context, true);
        rdd = tupleToArrays(bcm.roc().toJavaRDD(), "false positive rate", "true positive rate");
        it = new ConstantRDDRuntimeIterator(
                rdd,
                new RuntimeStaticContext(getConfiguration(), SequenceType.OBJECTS, ExecutionMode.RDD, getMetadata())
        );
        objectItem.putLazyItemByKey("roc", it, context, true);

        return objectItem;
    }

    private JavaRDD<Item> tupleToArrays(JavaRDD<Tuple2<Object, Object>> pr1, String key1, String key2) {
        return pr1.map(
            new Function<Tuple2<Object, Object>, Item>() {
                private static final long serialVersionUID = 1L;

                public Item call(Tuple2<Object, Object> a) {
                    List<String> keys = new ArrayList<>();
                    keys.add(key1);
                    keys.add(key2);
                    List<Item> values = new ArrayList<>();
                    values.add(ItemFactory.getInstance().createDoubleItem((double) a._1()));
                    values.add(ItemFactory.getInstance().createDoubleItem((double) a._2()));
                    return ItemFactory.getInstance()
                        .createObjectItem(keys, values, ExceptionMetadata.EMPTY_METADATA, true);
                }
            }
        );
    }

}
