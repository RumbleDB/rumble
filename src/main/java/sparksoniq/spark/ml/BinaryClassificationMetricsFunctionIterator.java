package sparksoniq.spark.ml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.ConstantRDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import scala.Tuple2;

public class BinaryClassificationMetricsFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public BinaryClassificationMetricsFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        super(arguments, executionMode, metadata);
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
        Item objectItem = ItemFactory.getInstance().createObjectItem();
        objectItem.putItemByKey("areaUnderPR", ItemFactory.getInstance().createDoubleItem(bcm.areaUnderPR()));
        objectItem.putItemByKey("areaUnderROC", ItemFactory.getInstance().createDoubleItem(bcm.areaUnderROC()));
        JavaRDD<Item> pr = tupleToArrays(bcm.pr().toJavaRDD());
        addEntry(objectItem, "pr", context, pr);
        JavaRDD<Item> fMeasureByThreshold = tupleToArrays(bcm.fMeasureByThreshold().toJavaRDD());
        addEntry(objectItem, "fMeasureByThreshold", context, fMeasureByThreshold);
        JavaRDD<Item> precisionByThreshold = tupleToArrays(bcm.fMeasureByThreshold().toJavaRDD());
        addEntry(objectItem, "precisionByThreshold", context, precisionByThreshold);
        JavaRDD<Item> recallByThreshold = tupleToArrays(bcm.precisionByThreshold().toJavaRDD());
        addEntry(objectItem, "recallByThreshold", context, recallByThreshold);
        JavaRDD<Item> roc = tupleToArrays(bcm.recallByThreshold().toJavaRDD());
        addEntry(objectItem, "roc", context, roc);

        return objectItem;
    }

    private void addEntry(Item objectItem, String name, DynamicContext context, JavaRDD<Item> items) {
        objectItem.putItemByKey(
            name,
            new FunctionItem(
                    null,
                    Collections.emptyList(),
                    new FunctionSignature(Collections.emptyList(), SequenceType.ITEM_STAR),
                    context,
                    new ConstantRDDRuntimeIterator(items, getMetadata())
            )
        );
    }

    private JavaRDD<Item> tupleToArrays(JavaRDD<Tuple2<Object, Object>> pr1) {
        return pr1.map(
            new Function<Tuple2<Object, Object>, Item>() {
                private static final long serialVersionUID = 1L;

                public Item call(Tuple2<Object, Object> a) {
                    List<Item> list = new ArrayList<>();
                    list.add(ItemFactory.getInstance().createDoubleItem((double) a._1()));
                    list.add(ItemFactory.getInstance().createDoubleItem((double) a._2()));
                    return ItemFactory.getInstance().createArrayItem(list);
                }
            }
        );
    }

}
