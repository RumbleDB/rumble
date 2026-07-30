package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

public class ConstructorFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> argumentIterator;
    private final SequenceType targetSequenceType;

    public ConstructorFunctionIterator(
            FunctionIdentifier identifier,
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        ItemType targetType = BuiltinTypesCatalogue.getItemTypeByName(identifier.getName());
        this.argumentIterator = arguments.get(0);
        this.targetSequenceType = new SequenceType(targetType, SequenceType.Arity.OneOrZero);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        return createCastIterator().materializeFirstOrNull(dynamicContext);
    }

    private org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> createCastIterator() {
        return new CastIterator(
                this.argumentIterator,
                this.targetSequenceType,
                this.staticContext.toBuilder().staticType(this.targetSequenceType).build()
        );
    }
}
