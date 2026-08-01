package org.rumbledb.runtime.functions;

import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotConvertToQNameException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.MappingLocalCursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import lombok.NonNull;
import java.io.Serial;
import java.util.Collections;

/**
 * Function conversion step for arguments: cast runtime xs:untypedAtomic values to the requested atomic type
 * before the generic type-promotion layer runs. Non-untyped values flow through unchanged.
 */
public class FunctionUntypedAtomicCastIterator
        extends
            ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item>,
            NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> iterator;
    private final UntypedAtomicCaster caster;

    public FunctionUntypedAtomicCastIterator(
            RuntimePlan<Item> iterator,
            ItemType targetType,
            String exceptionMessage,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(iterator), staticContext);
        this.iterator = iterator;
        this.caster = new UntypedAtomicCaster(targetType, exceptionMessage, staticContext, getMetadata());
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new MappingLocalCursor<>(
                this.iterator,
                context,
                this.caster::call,
                getMetadata()
        );
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            this.iterator,
            dynamicContext
        );
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        return this.iterator.getRDD(context).map(this.caster);
    }



    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }

    public static final class UntypedAtomicCaster implements Function<Item, Item> {

        @Serial
        private static final long serialVersionUID = 1L;

        private final ItemType targetType;
        private final String exceptionMessage;
        private final RuntimeStaticContext staticContext;
        private final ExceptionMetadata metadata;

        public UntypedAtomicCaster(
                @NonNull ItemType targetType,
                @NonNull String exceptionMessage,
                @NonNull RuntimeStaticContext staticContext,
                @NonNull ExceptionMetadata metadata
        ) {
            this.targetType = targetType;
            this.exceptionMessage = exceptionMessage;
            this.staticContext = staticContext;
            this.metadata = metadata;
        }

        @Override
        public Item call(Item item) {
            if (item == null || !item.isUntypedAtomic()) {
                return item;
            }
            if (item.getDynamicType().isSubtypeOf(this.targetType)) {
                return item;
            }
            if (isQNameLikeTargetType(this.targetType) || isNotationLikeTargetType(this.targetType)) {
                throw invalidQNameOrNotationConversion(item);
            }

            Item converted = CastIterator.castItemToType(
                item,
                this.targetType,
                this.metadata,
                this.staticContext
            );
            if (converted == null) {
                throw unexpectedType(item);
            }
            return converted;
        }

        private RuntimeException invalidQNameOrNotationConversion(Item item) {
            if (usesQNameCoercionErrorSemantics()) {
                return new CannotConvertToQNameException(errorMessage(item), this.metadata);
            }
            return unexpectedType(item);
        }

        private UnexpectedTypeException unexpectedType(Item item) {
            return new UnexpectedTypeException(errorMessage(item), this.metadata);
        }

        private String errorMessage(Item item) {
            return this.exceptionMessage
                + item.getDynamicType()
                + " cannot be implicitly converted to type "
                + this.targetType
                + ".";
        }

        private static boolean isQNameLikeTargetType(ItemType itemType) {
            return itemType.equals(BuiltinTypesCatalogue.QNameItem)
                || (itemType.isAtomicItemType()
                    && !itemType.equals(BuiltinTypesCatalogue.errorItem)
                    && itemType.getCastingPrimitiveType().equals(BuiltinTypesCatalogue.QNameItem));
        }

        private static boolean isNotationLikeTargetType(ItemType itemType) {
            return itemType.equals(BuiltinTypesCatalogue.NOTATIONItem)
                || (itemType.isAtomicItemType()
                    && !itemType.equals(BuiltinTypesCatalogue.errorItem)
                    && itemType.getCastingPrimitiveType().equals(BuiltinTypesCatalogue.NOTATIONItem));
        }

        private boolean usesQNameCoercionErrorSemantics() {
            String queryLanguage = this.staticContext.getQueryLanguage();
            return !queryLanguage.equals("xquery10") && !queryLanguage.equals("jsoniq10");
        }
    }

}
