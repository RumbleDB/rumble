package org.rumbledb.runtime.typing;

import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.functions.sequences.general.TreatAsClosure;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;
import org.rumbledb.types.SequenceType.Arity;

import org.rumbledb.spark.SparkSessionManager;

import lombok.NonNull;
import java.io.Serial;
import java.util.Collections;
import java.util.Objects;
import java.util.List;


public class TreatIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item>,
            UpdatingRuntimePlan,
            NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan iterator;
    private final TreatTypeValidator validator;

    public TreatIterator(
            ItemRuntimePlan iterator,
            SequenceType sequenceType,
            ErrorCode errorCode,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(iterator), staticContext);
        this.iterator = iterator;
        this.validator = new TreatTypeValidator(sequenceType, errorCode, getMetadata());
        if (
            !this.staticContext.getExecutionMode().equals(ExecutionMode.LOCAL)
                && (sequenceType.isEmptySequence()
                    || sequenceType.getArity().equals(Arity.One)
                    || sequenceType.getArity().equals(Arity.OneOrZero))
        ) {
            throw new OurBadException(
                    "A treat as iterator should never be executed in parallel if the sequence type arity is 0, 1 or ?."
            );
        }
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(this.iterator, context, this.validator);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        this.validator.resolve(dynamicContext);
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);

        if (this.validator.getSequenceType().getArity() != SequenceType.Arity.ZeroOrMore) {
            this.validator.validateEmpty(childRDD.take(2).size());
        }

        Function<Item, Boolean> transformation = new TreatAsClosure(this.validator);
        return childRDD.filter(transformation);
    }

    public static ItemType getItemType(Dataset<Row> df) {
        StructType type = df.schema();
        DataType dataType = type;
        StructField[] fields = type.fields();
        if (fields.length == 1 && fields[0].name().equals(SparkSessionManager.nonObjectJSONiqItemColumnName)) {
            dataType = fields[0].dataType();
        }
        return ItemTypeFactory.createItemType(dataType);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        this.validator.resolve(dynamicContext);
        HomogeneousItemDataFrame df = ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            this.iterator,
            dynamicContext
        );
        this.validator.validateEmpty(df.isEmptySequence() ? 0 : 1);
        if (df.isEmptySequence()) {
            return df;
        }
        ItemType dataItemType = df.getItemType();
        if (dataItemType.isSubtypeOf(this.validator.getSequenceType().getItemType())) {
            return df;
        }
        throw this.validator.error(dataItemType.toString());
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        return UpdatingRuntimePlan.get(this.iterator, context);
    }

    /**
     * Converts a homogeneous RDD of atomic values to a DataFrame
     * 
     * @param rdd the RDD containing the atomic values.
     * @param itemType the dynamic type of these values.
     * @return
     */
    public static HomogeneousItemDataFrame convertToDataFrame(
            JavaRDD<?> rdd,
            ItemType itemType,
            RuntimeStaticContext staticContext
    ) {
        List<StructField> fields = Collections.singletonList(
            DataTypes.createStructField(
                SparkSessionManager.nonObjectJSONiqItemColumnName,
                TypeMappings.getDataFrameDataTypeFromItemType(itemType, staticContext),
                true
            )
        );
        StructType schema = DataTypes.createStructType(fields);

        JavaRDD<Row> rowRDD = rdd.map(i -> RowFactory.create(i));

        // apply the schema to row RDD
        Dataset<Row> df = SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema);
        return new HomogeneousItemDataFrame(df, itemType);
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan childPlan;
        private final DynamicContext context;
        private final TreatTypeValidator validator;

        private Cursor<Item> childCursor;
        private Item nextResult;
        private int resultCount;

        private EvaluationCursor(
                @NonNull ItemRuntimePlan childPlan,
                @NonNull DynamicContext context,
                TreatTypeValidator validator
        ) {
            super(Objects.requireNonNull(validator, "validator").getMetadata());
            this.childPlan = childPlan;
            this.context = context;
            this.validator = validator;
        }

        @Override
        protected void openLocal() {
            this.validator.resolve(this.context);
            this.resultCount = 0;
            this.childCursor = this.childPlan.getCursor(this.context);
            setNextResult();
        }

        @Override
        protected boolean hasNextLocal() {
            return this.nextResult != null;
        }

        @Override
        protected Item nextLocal() {
            if (this.nextResult == null) {
                throw new IteratorFlowException(
                        IteratorFlowException.FLOW_EXCEPTION_MESSAGE,
                        this.validator.getMetadata()
                );
            }
            Item result = this.nextResult;
            setNextResult();
            return result;
        }

        @Override
        protected void closeLocal() {
            if (this.childCursor != null) {
                this.childCursor.close();
                this.childCursor = null;
            }
            this.nextResult = null;
        }

        private void setNextResult() {
            this.nextResult = null;
            if (!this.childCursor.hasNext()) {
                this.validator.validateEmpty(this.resultCount);
                return;
            }

            Item candidate = this.childCursor.next();
            if (candidate != null && !candidate.getDynamicType().isResolved()) {
                candidate.getDynamicType()
                    .resolve(this.context, this.validator.getMetadata());
            }
            if (candidate == null) {
                return;
            }

            this.resultCount++;
            this.validator.validateItem(candidate, this.resultCount);
            this.nextResult = candidate;
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeQueryRuntimePlan.generate(this.iterator, nativeClauseContext);
    }
}
