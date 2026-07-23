package org.rumbledb.runtime.functions;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotConvertToQNameException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.io.Serial;
import java.util.Collections;

/**
 * Function conversion step for arguments: cast runtime xs:untypedAtomic values to the requested atomic type
 * before the generic type-promotion layer runs. Non-untyped values flow through unchanged.
 */
public class FunctionUntypedAtomicCastIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator iterator;
    private final ItemType targetType;
    private final String exceptionMessage;

    public FunctionUntypedAtomicCastIterator(
            RuntimeIterator iterator,
            ItemType targetType,
            String exceptionMessage,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(iterator), staticContext);
        this.iterator = iterator;
        this.targetType = targetType;
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        return this.iterator.getDataFrame(dynamicContext);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return this.iterator.getRDD(context)
            .map(
                new Function<Item, Item>() {
                    @Serial
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Item call(Item item) {
                        return castIfUntypedAtomic(item);
                    }
                }
            );
    }

    @Override
    public void openLocal() {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.iterator.hasNext();
    }

    @Override
    public Item nextLocal() {
        return castIfUntypedAtomic(this.iterator.next());
    }

    @Override
    public void closeLocal() {
        this.iterator.close();
    }

    @Override
    public void resetLocal() {
        this.iterator.reset(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.iterator.hasNext();
    }

    @Override
    public boolean hasNextLocal() {
        return this.iterator.hasNext();
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }

    private Item castIfUntypedAtomic(Item item) {
        if (item == null || !item.isUntypedAtomic()) {
            return item;
        }
        if (item.getDynamicType().isSubtypeOf(this.targetType)) {
            return item;
        }
        if (
            isQNameLikeTargetType(this.targetType)
                || isNotationLikeTargetType(this.targetType)
        ) {
            if (usesQNameCoercionErrorSemantics()) {
                throw new CannotConvertToQNameException(
                        this.exceptionMessage
                            + item.getDynamicType()
                            + " cannot be implicitly converted to type "
                            + this.targetType
                            + ".",
                        getMetadata()
                );
            }
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        + item.getDynamicType()
                        + " cannot be implicitly converted to type "
                        + this.targetType
                        + ".",
                    getMetadata()
            );
        }
        Item converted = CastIterator.castItemToType(item, this.targetType, getMetadata(), this.staticContext);
        if (converted == null) {
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        + item.getDynamicType()
                        + " cannot be implicitly converted to type "
                        + this.targetType
                        + ".",
                    getMetadata()
            );
        }
        return converted;
    }

    private boolean isQNameLikeTargetType(ItemType itemType) {
        return itemType.equals(BuiltinTypesCatalogue.QNameItem)
            || (itemType.isAtomicItemType()
                && !itemType.equals(BuiltinTypesCatalogue.errorItem)
                && itemType.getCastingPrimitiveType().equals(BuiltinTypesCatalogue.QNameItem));
    }

    private boolean isNotationLikeTargetType(ItemType itemType) {
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
