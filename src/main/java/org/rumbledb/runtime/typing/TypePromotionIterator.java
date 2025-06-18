package org.rumbledb.runtime.typing;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.functions.sequences.general.TypePromotionClosure;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import sparksoniq.spark.SparkSessionManager;

import java.util.Collections;

public class TypePromotionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final String exceptionMessage;
    private RuntimeIterator iterator;
    private SequenceType sequenceType;

    private ItemType itemType;

    private Item nextResult;
    private int childIndex;

    public TypePromotionIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(iterator), staticContext);
        this.exceptionMessage = exceptionMessage;
        this.iterator = iterator;
        this.sequenceType = sequenceType;
        this.itemType = this.sequenceType.getItemType();
        if (
            (sequenceType.isEmptySequence()
                || sequenceType.getArity().equals(Arity.One)
                || sequenceType.getArity().equals(Arity.OneOrZero))
        ) {
            throw new OurBadException(
                    "This promotion iterator is not meant to be used if the sequence type arity is 0, 1 or ?."
            );
        }
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public void resetLocal() {
        this.iterator.reset(this.currentDynamicContextForLocalExecution);
        this.childIndex = 0;
        setNextResult();
    }

    @Override
    public void closeLocal() {
        this.iterator.close();
    }

    @Override
    public void openLocal() {
        this.childIndex = 0;
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.setNextResult();
    }


    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item currentResult = this.nextResult;
            setNextResult();
            return currentResult;
        } else {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
    }

    private void setNextResult() {
        this.nextResult = null;
        if (this.iterator.hasNext()) {
            this.nextResult = this.iterator.next();
            if (this.nextResult != null && !this.nextResult.getDynamicType().isResolved()) {
                this.nextResult.getDynamicType().resolve(this.currentDynamicContextForLocalExecution, getMetadata());
            }
            this.childIndex++;
        } else {
            checkEmptySequence(this.childIndex);
        }

        this.hasNext = this.nextResult != null;
        if (!hasNext()) {
            return;
        }

        checkItemsSize(this.childIndex);
        if (!InstanceOfIterator.doesItemTypeMatchItem(this.itemType, this.nextResult)) {
            checkTypePromotion();
        }
    }

    private void checkEmptySequence(long size) {
        if (
            size == 0
                && this.sequenceType.getArity() == SequenceType.Arity.OneOrMore
        ) {
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        + "Expecting"
                        + ((this.sequenceType.getArity() == SequenceType.Arity.OneOrMore) ? " at least" : "")
                        + " one item, but the value provided is the empty sequence.",
                    getMetadata()
            );
        }
    }

    private void checkItemsSize(long size) {
        if (size > 0 && this.sequenceType.isEmptySequence()) {
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        + "Expecting empty sequence, but the value provided has at least one item.",
                    getMetadata()
            );
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);

        int count = childRDD.take(2).size();
        checkEmptySequence(count);
        checkItemsSize(count);
        Function<Item, Item> transformation = new TypePromotionClosure(
                this.exceptionMessage,
                this.sequenceType,
                getMetadata()
        );
        return childRDD.map(transformation);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        JSoundDataFrame df = this.iterator.getDataFrame(dynamicContext);
        checkEmptySequence(df.isEmptySequence() ? 0 : 1);
        if (df.isEmptySequence()) {
            return df;
        }
        ItemType dataItemType = df.getItemType();
        if (
            dataItemType.isSubtypeOf(BuiltinTypesCatalogue.decimalItem)
                && this.itemType.equals(BuiltinTypesCatalogue.doubleItem)
        ) {
            String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
            df = df.evaluateSQL(
                String.format(
                    "SELECT CAST (`"
                        + SparkSessionManager.atomicJSONiqItemColumnName
                        + "` AS double) AS `"
                        + SparkSessionManager.atomicJSONiqItemColumnName
                        + "` FROM %s",
                    input
                ),
                this.itemType
            );
        }
        dataItemType = df.getItemType();
        if (dataItemType.isSubtypeOf(this.itemType)) {
            return df;
        }
        throw new UnexpectedTypeException(
                this.exceptionMessage
                    + dataItemType
                    + " cannot be promoted to type "
                    + this.sequenceType
                    + ".",
                getMetadata()
        );
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext childContext = this.iterator.generateNativeQuery(nativeClauseContext);
        if (childContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(childContext.getResultingType().getArity())) {
            return childContext;
        }
        if (childContext.getResultingType().getItemType().isSubtypeOf(this.itemType)) {
            return childContext;
        }
        if (
            childContext.getResultingType().getItemType().isSubtypeOf(BuiltinTypesCatalogue.decimalItem)
                && this.itemType.equals(BuiltinTypesCatalogue.doubleItem)
        ) {
            return new NativeClauseContext(
                    childContext,
                    "CAST (" + childContext.getResultingQuery() + " AS DOUBLE)",
                    new SequenceType(BuiltinTypesCatalogue.doubleItem, childContext.getResultingType().getArity())
            );
        }
        return NativeClauseContext.NoNativeQuery;
    }

    private void checkTypePromotion() {
        if (this.nextResult.isFunction()) {
            return;
        }
        if (!this.nextResult.getDynamicType().canBePromotedTo(this.sequenceType.getItemType())) {
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        + this.nextResult.getDynamicType().toString()
                        + " cannot be promoted to type "
                        + this.sequenceType
                        + ".",
                    getMetadata()
            );
        }
        this.nextResult = CastIterator.castItemToType(this.nextResult, this.sequenceType.getItemType(), getMetadata());
        if (this.nextResult == null) {
            throw new OurBadException(
                    "We were not able to promote " + this.nextResult + " to type " + this.sequenceType.getItemType()
            );
        }
    }
}
