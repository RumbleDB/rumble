package org.rumbledb.runtime.scripting.block;

import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.update.PendingUpdateList;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.List;
import java.util.stream.Stream;

public class StatementsWithExprIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item>,
            UpdatingRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public StatementsWithExprIterator(
            List<RuntimePlan<Item>> statements,
            RuntimePlan<Item> exprIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            Stream.concat(statements.stream(), Stream.of(exprIterator)).toList(),
            staticContext.toBuilder()
                .isUpdating(exprIterator.getRuntimeStaticContext().isUpdating())
                .isSequential(isSequential(statements, exprIterator))
                .build()
        );
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        int resultIndex = this.getChildren().size() - 1;
        return new SequentialLocalCursor<>(
                this.getChildren().subList(0, resultIndex),
                this.getChild(resultIndex),
                context,
                getMetadata()
        );
    }

    private static boolean isSequential(
            List<RuntimePlan<Item>> statements,
            RuntimePlan<Item> exprIterator
    ) {
        return exprIterator.getRuntimeStaticContext().isSequential()
            || statements.stream().anyMatch(statement -> statement.getRuntimeStaticContext().isSequential());
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        if (!this.getChildren().isEmpty()) {
            int childIndex = 0;
            RuntimePlan<Item> currentChild = this.getChild(childIndex);

            JavaRDD<Item> childRDD = currentChild.getRDD(dynamicContext);
            childIndex++;

            while (childIndex < this.getChildren().size()) {
                currentChild = this.getChild(childIndex);
                JavaRDD<Item> nextChildRDD = currentChild.getRDD(dynamicContext);
                childRDD = childRDD.union(nextChildRDD);
                childIndex++;
            }
            return childRDD;
        } else {
            JavaSparkContext sparkContext = SparkSessionManager.getInstance().getJavaSparkContext();
            return sparkContext.emptyRDD();
        }
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        int childIndex = 0;
        while (childIndex < this.getChildren().size() - 1) {
            ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
                this.getChild(childIndex),
                dynamicContext
            );
            ++childIndex;
        }
        RuntimePlan<Item> exprIterator = this.getChild(childIndex);
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            exprIterator,
            dynamicContext
        );
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        RuntimePlan<Item> exprIterator = this.getChild(
            this.getChildren().size() - 1
        );
        if (exprIterator.getRuntimeStaticContext().isUpdating()) {
            return UpdatingRuntimePlan.get(exprIterator, context);
        }
        return new PendingUpdateList();
    }
}
