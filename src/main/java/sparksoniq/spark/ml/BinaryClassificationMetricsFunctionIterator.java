package sparksoniq.spark.ml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.ConstantRDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import scala.Function1;
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
		JavaPairRDD<Object, Object> predictionAndLabels = scoresAndLabels.mapToPair(p ->
		  new Tuple2<>(p.getItemAt(0).castToDoubleValue(), p.getItemAt(1).castToDoubleValue()));
		BinaryClassificationMetrics bcm = new BinaryClassificationMetrics(predictionAndLabels.rdd());
		Item objectItem = ItemFactory.getInstance().createObjectItem();
		objectItem.putItemByKey("areaUnderPR", ItemFactory.getInstance().createDoubleItem(bcm.areaUnderPR()));
		objectItem.putItemByKey("areaUnderROC", ItemFactory.getInstance().createDoubleItem(bcm.areaUnderROC()));
		JavaRDD<Tuple2<Object, Object>> pr1 = bcm.pr().toJavaRDD();
		JavaRDD<Item> pr2 = pr1.map(
				new Function<Tuple2<Object, Object>, Item>() {
					private static final long serialVersionUID = 1L;

					public Item call(Tuple2<Object, Object> a) {
						return ItemFactory.getInstance().createArrayItem(Arrays.asList(
								//ItemFactory.getInstance().createDoubleItem((double)a._1()),
								/*ItemFactory.getInstance().createDoubleItem((double)a._2())*/));
					}
				}
		);
		objectItem.putItemByKey("pr", new FunctionItem(
	            null,
	            Collections.emptyList(),
	            new FunctionSignature(Collections.emptyList(), SequenceType.ITEM_STAR),
	            context,
	            new ConstantRDDRuntimeIterator(pr2, getMetadata())
	    ));
		
		return objectItem;
	}

}
