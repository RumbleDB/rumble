package org.rumbledb.runtime.typing;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.FunctionCoercion;
import org.rumbledb.runtime.functions.FunctionUntypedAtomicCastIterator;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.io.Serial;
import java.util.Collections;

public class AtMostOneItemTypePromotionIterator extends AbstractAtMostOneItemRuntimePlan
        implements
            NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String exceptionMessage;
    private final ItemRuntimePlan iterator;
    private final SequenceType sequenceType;
    private final ItemType itemType;
    private final FunctionUntypedAtomicCastIterator.UntypedAtomicCaster untypedAtomicCaster;

    public AtMostOneItemTypePromotionIterator(
            ItemRuntimePlan iterator,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext staticContext
    ) {
        this(iterator, sequenceType, exceptionMessage, staticContext, null);
    }

    /**
     * Creates a scalar function-argument promotion. When {@code untypedAtomicTargetType} is set, untyped atomic
     * values are converted directly during scalar evaluation instead of through an intermediate mapping cursor.
     */
    public AtMostOneItemTypePromotionIterator(
            ItemRuntimePlan iterator,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext staticContext,
            ItemType untypedAtomicTargetType
    ) {
        super(Collections.singletonList(iterator), staticContext);
        this.exceptionMessage = exceptionMessage;
        this.iterator = iterator;
        this.sequenceType = sequenceType;
        this.itemType = this.sequenceType.getItemType();
        this.untypedAtomicCaster = untypedAtomicTargetType == null
            ? null
            : new FunctionUntypedAtomicCastIterator.UntypedAtomicCaster(
                    untypedAtomicTargetType,
                    exceptionMessage,
                    staticContext,
                    getMetadata()
            );
        if (!this.staticContext.getExecutionMode().equals(ExecutionMode.LOCAL)) {
            throw new OurBadException(
                    "A promotion iterator should never be executed in parallel if the sequence type arity is 0, 1 or ?."
            );
        }
        if (
            !sequenceType.isEmptySequence()
                && !sequenceType.getArity().equals(Arity.One)
                && !sequenceType.getArity().equals(Arity.OneOrZero)
        ) {
            throw new OurBadException(
                    "A type promotion iterator was instantiated that can only output at most one item, but the sequence type is + or *."
            );
        }
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(
            this.iterator,
            this.sequenceType,
            this.itemType,
            this.exceptionMessage,
            getRuntimeStaticContext(),
            this.untypedAtomicCaster,
            context
        );
    }

    private static Item evaluate(
            ItemRuntimePlan iterator,
            SequenceType sequenceType,
            ItemType itemType,
            String exceptionMessage,
            RuntimeStaticContext staticContext,
            FunctionUntypedAtomicCastIterator.UntypedAtomicCaster untypedAtomicCaster,
            DynamicContext context
    ) {
        if (!sequenceType.isResolved()) {
            sequenceType.resolve(context, staticContext.getMetadata());
        }
        Item item = null;
        try {
            item = iterator.materializeAtMostOne(context);
            if (item != null && !item.getDynamicType().isResolved()) {
                item.getDynamicType().resolve(context, staticContext.getMetadata());
            }
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    exceptionMessage
                        + "Expecting at most one item, but the value provided has at least two items.",
                    staticContext.getMetadata()
            );

        }
        if (
            item == null && (sequenceType.getArity() == SequenceType.Arity.One)
        ) {
            throw new UnexpectedTypeException(
                    exceptionMessage
                        + "Expecting one item, but the value provided is the empty sequence.",
                    staticContext.getMetadata()
            );
        }
        if (item == null) {
            return null;
        }

        if (untypedAtomicCaster != null) {
            item = untypedAtomicCaster.call(item);
        }

        if (!InstanceOfIterator.doesItemTypeMatchItem(itemType, item)) {
            item = checkTypePromotion(item, itemType, sequenceType, exceptionMessage, staticContext);
        }
        return item;
    }

    private static Item checkTypePromotion(
            Item item,
            ItemType itemType,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext staticContext
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
        if (item.isAnyURI() && itemType.equals(BuiltinTypesCatalogue.stringItem)) {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        }
        if (item.isFloat() && itemType.equals(BuiltinTypesCatalogue.doubleItem)) {
            return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        if (item.isDecimal() && itemType.equals(BuiltinTypesCatalogue.doubleItem)) {
            return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        if (item.isDecimal() && itemType.equals(BuiltinTypesCatalogue.floatItem)) {
            return ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
        }
        throw new UnexpectedTypeException(
                exceptionMessage
                    + item.getDynamicType().toString()
                    + " cannot be promoted to type "
                    + sequenceType
                    + ".",
                staticContext.getMetadata()
        );
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext value = NativeQueryRuntimePlan.generate(
            this.getChild(0),
            nativeClauseContext
        );
        if (value.equals(NativeClauseContext.NoNativeQuery)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (value.getResultingType().getItemType().isSubtypeOf(this.itemType)) {
            return value;
        }
        if (
            this.itemType.equals(BuiltinTypesCatalogue.stringItem)
                && value.getResultingType().getItemType().equals(BuiltinTypesCatalogue.anyURIItem)
        ) {
            return new NativeClauseContext(
                    value,
                    "CAST (" + value.getResultingQuery() + " AS STRING)",
                    new SequenceType(BuiltinTypesCatalogue.stringItem, value.getResultingType().getArity())
            );
        }
        if (
            this.itemType.equals(BuiltinTypesCatalogue.doubleItem)
                && (value.getResultingType().getItemType().equals(BuiltinTypesCatalogue.floatItem)
                    || value.getResultingType().getItemType().equals(BuiltinTypesCatalogue.decimalItem))
        ) {
            return new NativeClauseContext(
                    value,
                    "CAST (" + value.getResultingQuery() + " AS DOUBLE)",
                    new SequenceType(BuiltinTypesCatalogue.doubleItem, value.getResultingType().getArity())
            );
        }
        if (
            this.itemType.equals(BuiltinTypesCatalogue.floatItem)
                && value.getResultingType().getItemType().equals(BuiltinTypesCatalogue.decimalItem)
        ) {
            return new NativeClauseContext(
                    value,
                    "CAST (" + value.getResultingQuery() + " AS FLOAT)",
                    new SequenceType(BuiltinTypesCatalogue.floatItem, value.getResultingType().getArity())
            );
        }
        throw new UnexpectedTypeException(
                this.exceptionMessage
                    + value.getResultingType().getItemType().toString()
                    + " cannot be promoted to type "
                    + this.sequenceType
                    + ".",
                getMetadata()
        );
    }

}
