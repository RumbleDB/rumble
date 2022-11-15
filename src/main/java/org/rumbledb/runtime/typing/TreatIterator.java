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
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.TreatException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.TreatAsClosure;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import sparksoniq.spark.SparkSessionManager;

import java.util.Collections;
import java.util.List;


public class TreatIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private final SequenceType sequenceType;
    private ErrorCode errorCode;

    private ItemType itemType;

    private Item nextResult;
    private Item currentResult;
    private int resultCount;

    public TreatIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            ErrorCode errorCode,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(iterator), executionMode, iteratorMetadata);
        this.iterator = iterator;
        this.sequenceType = sequenceType;
        this.errorCode = errorCode;
        if (!this.sequenceType.isEmptySequence()) {
            this.itemType = this.sequenceType.getItemType();
        }
        if (
            !executionMode.equals(ExecutionMode.LOCAL)
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
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public void resetLocal() {
        this.resultCount = 0;
        this.iterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    public void closeLocal() {
        this.iterator.close();
    }

    @Override
    public void openLocal() {
        if (!this.sequenceType.isResolved()) {
            this.sequenceType.resolve(this.currentDynamicContextForLocalExecution, getMetadata());
        }
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
                    checkMoreThanOneItemSequence(size);
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
            checkEmptySequence(this.resultCount);
        }

        this.hasNext = this.nextResult != null;
        if (!hasNext()) {
            return;
        }

        checkTreatAsEmptySequence(this.resultCount);
        checkMoreThanOneItemSequence(this.resultCount);
        if (!InstanceOfIterator.doesItemTypeMatchItem(this.itemType, this.nextResult)) {
            throw errorToThrow(this.nextResult.getDynamicType().toString());
        }
    }

    private RuntimeException errorToThrow(String type) {
        switch (this.errorCode) {
            case DynamicTypeTreatErrorCode:
                return new TreatException(
                        type
                            + " cannot be treated as type "
                            + this.sequenceType,
                        this.getMetadata()
                );
            case UnexpectedTypeErrorCode:
                return new UnexpectedTypeException(
                        type
                            + " is not expected here. The expected type is "
                            + this.sequenceType,
                        this.getMetadata()
                );
            case InvalidInstance:
                return new InvalidInstanceException(
                        "Invalid instance because of arity mismatch. The expected arity is "
                            + this.sequenceType.getArity(),
                        this.getMetadata()
                );
            default:
                return new OurBadException("Unexpected error code in treat as iterator.", this.getMetadata());
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        if (!this.sequenceType.isResolved()) {
            this.sequenceType.resolve(dynamicContext, getMetadata());
        }
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);

        if (this.sequenceType.getArity() != SequenceType.Arity.ZeroOrMore) {
            checkEmptySequence(childRDD.take(2).size());
        }

        Function<Item, Boolean> transformation = new TreatAsClosure(
                this.sequenceType,
                this.errorCode,
                getMetadata()
        );
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
        if (fields.length == 1 && fields[0].name().equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
            dataType = fields[0].dataType();
        }
        return ItemTypeFactory.createItemType(dataType);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        if (!this.sequenceType.isResolved()) {
            this.sequenceType.resolve(dynamicContext, getMetadata());
        }
        JSoundDataFrame df = this.iterator.getDataFrame(dynamicContext);
        checkEmptySequence(df.isEmptySequence() ? 0 : 1);
        if (df.isEmptySequence()) {
            return df;
        }
        ItemType dataItemType = df.getItemType();
        if (dataItemType.isSubtypeOf(this.sequenceType.getItemType())) {
            return df;
        }
        throw errorToThrow("" + dataItemType);
    }

    /**
     * Converts a homogeneous RDD of atomic values to a DataFrame
     * 
     * @param rdd the RDD containing the atomic values.
     * @param itemType the dynamic type of these values.
     * @return
     */
    public static JSoundDataFrame convertToDataFrame(JavaRDD<?> rdd, ItemType itemType) {
        List<StructField> fields = Collections.singletonList(
            DataTypes.createStructField(
                SparkSessionManager.atomicJSONiqItemColumnName,
                itemType.toDataFrameType(),
                true
            )
        );
        StructType schema = DataTypes.createStructType(fields);

        JavaRDD<Row> rowRDD = rdd.map(i -> RowFactory.create(i));

        // apply the schema to row RDD
        Dataset<Row> df = SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema);
        return new JSoundDataFrame(df, itemType);
    }

    private void checkEmptySequence(int size) {
        if (
            size == 0
                && !this.sequenceType.isEmptySequence()
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this.sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            throw errorToThrow("Empty sequence");
        }
    }

    private void checkTreatAsEmptySequence(int size) {
        if (size > 0 && this.sequenceType.isEmptySequence()) {
            throw errorToThrow(this.nextResult.getDynamicType().toString());
        }
    }

    private void checkMoreThanOneItemSequence(int size) {
        if (
            size > 1
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this.sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            throw errorToThrow("A sequence of more than one item");
        }
    }
}

