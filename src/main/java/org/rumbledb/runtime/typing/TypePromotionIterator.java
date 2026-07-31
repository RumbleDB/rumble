package org.rumbledb.runtime.typing;

import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.functions.FunctionCoercion;
import org.rumbledb.runtime.functions.sequences.general.TypePromotionClosure;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import sparksoniq.spark.SparkSessionManager;

import lombok.NonNull;
import java.io.Serial;
import java.util.Collections;

public class TypePromotionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String exceptionMessage;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> iterator;
    private final SequenceType sequenceType;
    private final ItemType itemType;

    public TypePromotionIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> iterator,
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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(
                this.iterator,
                context,
                this.sequenceType,
                this.itemType,
                this.exceptionMessage,
                getRuntimeStaticContext(),
                getMetadata()
        );
    }

    private static void checkEmptySequence(
            long size,
            SequenceType sequenceType,
            String exceptionMessage,
            ExceptionMetadata metadata
    ) {
        if (
            size == 0
                && sequenceType.getArity() == SequenceType.Arity.OneOrMore
        ) {
            throw new UnexpectedTypeException(
                    exceptionMessage
                        + "Expecting at least one item, but the value provided is the empty sequence.",
                    metadata
            );
        }
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);

        int count = childRDD.take(2).size();
        checkEmptySequence(count, this.sequenceType, this.exceptionMessage, getMetadata());
        Function<Item, Item> transformation = new TypePromotionClosure(
                this.exceptionMessage,
                this.sequenceType,
                getMetadata()
        );
        return childRDD.map(transformation);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        HomogeneousItemDataFrame df = org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            this.iterator,
            dynamicContext
        );
        checkEmptySequence(
            df.isEmptySequence() ? 0 : 1,
            this.sequenceType,
            this.exceptionMessage,
            getMetadata()
        );
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
                        + SparkSessionManager.nonObjectJSONiqItemColumnName
                        + "` AS double) AS `"
                        + SparkSessionManager.nonObjectJSONiqItemColumnName
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
        NativeClauseContext childContext = org.rumbledb.runtime.plan.NativeQueryRuntimePlan.generate(
            this.iterator,
            nativeClauseContext
        );
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

    private static Item promoteItem(
            Item item,
            ItemType itemType,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext staticContext,
            ExceptionMetadata metadata
    ) {
        if (
            item.isFunction()
                && item.getIdentifier() != null
                && item.getIdentifier().getArity() == 0
                && Name.TAIL_CALL_OPTIMIZATION.equals(item.getIdentifier().getName())
        ) {
            return item;
        }
        if (
            (item.isFunction() || item.isMap() || item.isArray())
                && itemType.isFunctionItemType()
                && itemType.getSignature() != null
        ) {
            return FunctionCoercion.coerceToFunctionItem(
                item,
                itemType,
                staticContext,
                exceptionMessage
            );
        }
        if (!item.getDynamicType().canBePromotedTo(sequenceType.getItemType())) {
            throw new UnexpectedTypeException(
                    exceptionMessage
                        + item.getDynamicType().toString()
                        + " cannot be promoted to type "
                        + sequenceType
                        + ".",
                    metadata
            );
        }
        Item promotedItem = CastIterator.castItemToType(
            item,
            sequenceType.getItemType(),
            metadata,
            staticContext
        );
        if (promotedItem == null) {
            throw new OurBadException(
                    "We were not able to promote " + item + " to type " + sequenceType.getItemType()
            );
        }
        return promotedItem;
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final DynamicContext context;
        private final SequenceType sequenceType;
        private final ItemType itemType;
        private final String exceptionMessage;
        private final RuntimeStaticContext staticContext;
        private final ExceptionMetadata metadata;

        private Cursor<Item> childCursor;
        private Item nextResult;
        private long childIndex;

        private EvaluationCursor(
                @NonNull RuntimePlan<Item> childPlan,
                @NonNull DynamicContext context,
                @NonNull SequenceType sequenceType,
                @NonNull ItemType itemType,
                @NonNull String exceptionMessage,
                @NonNull RuntimeStaticContext staticContext,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.childPlan = childPlan;
            this.context = context;
            this.sequenceType = sequenceType;
            this.itemType = itemType;
            this.exceptionMessage = exceptionMessage;
            this.staticContext = staticContext;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.childIndex = 0;
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
                throw new IteratorFlowException(IteratorFlowException.FLOW_EXCEPTION_MESSAGE, this.metadata);
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
                checkEmptySequence(
                    this.childIndex,
                    this.sequenceType,
                    this.exceptionMessage,
                    this.metadata
                );
                return;
            }

            Item candidate = this.childCursor.next();
            if (candidate != null && !candidate.getDynamicType().isResolved()) {
                candidate.getDynamicType().resolve(this.context, this.metadata);
            }
            this.childIndex++;
            if (candidate == null) {
                return;
            }

            if (!InstanceOfIterator.doesItemTypeMatchItem(this.itemType, candidate)) {
                candidate = promoteItem(
                    candidate,
                    this.itemType,
                    this.sequenceType,
                    this.exceptionMessage,
                    this.staticContext,
                    this.metadata
                );
            }
            this.nextResult = candidate;
        }

    }

}
