package org.rumbledb.runtime.typing;

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
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.functions.sequences.general.TreatAsClosure;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;
import org.rumbledb.types.SequenceType.Arity;

import sparksoniq.spark.SparkSessionManager;

import lombok.NonNull;
import java.io.Serial;
import java.util.Collections;
import java.util.List;


public class TreatIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator iterator;
    private final TreatTypeValidator validator;

    private Item nextResult;
    private Item currentResult;
    private int resultCount;

    public TreatIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            ErrorCode errorCode,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(iterator), staticContext);
        this.iterator = iterator;
        this.validator = new TreatTypeValidator(sequenceType, errorCode, getMetadata());
        if (
            !getHighestExecutionMode().equals(ExecutionMode.LOCAL)
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
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new Cursor(this.iterator, context, this.validator);
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public void closeLocal() {
        this.iterator.close();
    }

    @Override
    public void openLocal() {
        this.validator.resolve(this.currentDynamicContextForLocalExecution);
        this.resultCount = 0;
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            this.currentResult = this.nextResult;
            setNextResult();
            return this.currentResult;
        } else {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
    }

    private void setNextResult() {
        this.nextResult = null;
        if (this.iterator.hasNext()) {
            if (this.iterator.isRDDOrDataFrame()) {
                if (this.currentResult == null) {
                    JavaRDD<Item> childRDD = this.iterator.getRDD(this.currentDynamicContextForLocalExecution);
                    int size = childRDD.take(2).size();
                    this.validator.validateMaximumCardinality(size);
                    this.nextResult = childRDD.first();
                } else {
                    this.nextResult = null;
                }
            } else {
                this.nextResult = this.iterator.next();
            }
            if (this.nextResult != null && !this.nextResult.getDynamicType().isResolved()) {
                this.nextResult.getDynamicType().resolve(this.currentDynamicContextForLocalExecution, getMetadata());
            }
            if (this.nextResult != null) {
                this.resultCount++;
            }
        } else {
            this.validator.validateEmpty(this.resultCount);
        }

        this.hasNext = this.nextResult != null;
        if (!hasNext()) {
            return;
        }

        this.validator.validateItem(this.nextResult, this.resultCount);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        this.validator.resolve(dynamicContext);
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);

        if (this.validator.getSequenceType().getArity() != SequenceType.Arity.ZeroOrMore) {
            this.validator.validateEmpty(childRDD.take(2).size());
        }

        Function<Item, Boolean> transformation = new TreatAsClosure(this.validator);
        return childRDD.filter(transformation);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
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
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        this.validator.resolve(dynamicContext);
        JSoundDataFrame df = this.iterator.getDataFrame(dynamicContext);
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
        return this.iterator.getPendingUpdateList(context);
    }

    /**
     * Converts a homogeneous RDD of atomic values to a DataFrame
     * 
     * @param rdd the RDD containing the atomic values.
     * @param itemType the dynamic type of these values.
     * @return
     */
    public static JSoundDataFrame convertToDataFrame(
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
        return new JSoundDataFrame(df, itemType);
    }

    private static final class Cursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final DynamicContext context;
        private final TreatTypeValidator validator;

        private LocalCursor<Item> childCursor;
        private Item nextResult;
        private int resultCount;

        private Cursor(
                @NonNull RuntimePlan<Item> childPlan,
                @NonNull DynamicContext context,
                @NonNull TreatTypeValidator validator
        ) {
            super(validator.getMetadata());
            this.childPlan = childPlan;
            this.context = context;
            this.validator = validator;
        }

        @Override
        protected void openLocal() {
            this.validator.resolve(this.context);
            this.resultCount = 0;
            this.childCursor = this.childPlan.createLocalCursor(this.context);
            this.childCursor.open();
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
                        RuntimeIterator.FLOW_EXCEPTION_MESSAGE,
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
                candidate.getDynamicType().resolve(this.context, this.validator.getMetadata());
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
        return this.iterator.generateNativeQuery(nativeClauseContext);
    }
}
