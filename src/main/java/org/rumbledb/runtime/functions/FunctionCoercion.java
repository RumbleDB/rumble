package org.rumbledb.runtime.functions;

import org.rumbledb.api.Item;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public final class FunctionCoercion {

    private static final String COERCED_FUNCTION_LOCAL_NAME = "coerced-function";

    private FunctionCoercion() {
    }

    public static boolean canBeFunctionCoercedTo(SequenceType sourceType, SequenceType targetType) {
        if (sourceType.isEmptySequence()) {
            return false;
        }
        if (!sourceType.isAritySubtypeOf(targetType.getArity())) {
            return false;
        }
        return canItemTypeBeFunctionCoercedTo(sourceType.getItemType(), targetType.getItemType());
    }

    public static boolean canItemTypeBeFunctionCoercedTo(ItemType sourceItemType, ItemType targetItemType) {
        if (!targetItemType.isFunctionItemType() || targetItemType.getSignature() == null) {
            return false;
        }
        if (
            !sourceItemType.isFunctionItemType()
                && !sourceItemType.isMapItemType()
                && !sourceItemType.isArrayItemType()
                && !sourceItemType.isXQueryArrayItemType()
                && !sourceItemType.isObjectItemType()
        ) {
            return false;
        }
        FunctionSignature targetSignature = targetItemType.getSignature();
        int targetArity = targetSignature.getParameterTypes().size();
        if (
            sourceItemType.isMapItemType()
                || sourceItemType.isObjectItemType()
                || sourceItemType.isArrayItemType()
                || sourceItemType.isXQueryArrayItemType()
        ) {
            return targetArity == 1;
        }
        FunctionSignature sourceSignature = sourceItemType.getSignature();
        return sourceSignature == null || sourceSignature.getParameterTypes().size() == targetArity;
    }

    private static ItemType getCallableItemType(Item item) {
        if (item.isFunction()) {
            return ItemTypeFactory.createFunctionItemType(item.getSignature());
        }
        return item.getDynamicType();
    }

    public static Item coerceToFunctionItem(
            Item item,
            ItemType targetItemType,
            RuntimeStaticContext staticContext,
            String exceptionMessage
    ) {
        ItemType sourceItemType = getCallableItemType(item);
        if (!canItemTypeBeFunctionCoercedTo(sourceItemType, targetItemType)) {
            throw new UnexpectedTypeException(
                    exceptionMessage
                        + sourceItemType
                        + " cannot be promoted to type "
                        + new SequenceType(targetItemType)
                        + ".",
                    staticContext.getMetadata()
            );
        }

        FunctionSignature expectedSignature = targetItemType.getSignature();
        List<Name> parameterNames = new ArrayList<>(expectedSignature.getParameterTypes().size());
        for (int i = 0; i < expectedSignature.getParameterTypes().size(); i++) {
            parameterNames.add(Name.createVariableInNoNamespace("$coerced" + i));
        }

        FunctionIdentifier identifier;
        if (item.isFunction() && item.getIdentifier() != null) {
            identifier = item.getIdentifier();
        } else {
            identifier = new FunctionIdentifier(
                    new Name(Name.LOCAL_NS, "local", COERCED_FUNCTION_LOCAL_NAME),
                    parameterNames.size()
            );
        }

        return new FunctionItem(
                identifier,
                parameterNames,
                expectedSignature,
                item.isFunction()
                    ? item.getModuleDynamicContext()
                    : new DynamicContext(staticContext.getConfiguration()),
                new FunctionCoercionRuntimeIterator(item, parameterNames, staticContext),
                false
        );
    }
}
