package org.rumbledb.runtime.functions;

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

public class ConstructorFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator argumentIterator;
    private final SequenceType targetSequenceType;

    public ConstructorFunctionIterator(
            FunctionIdentifier identifier,
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.argumentIterator = arguments.get(0);
        ItemType targetType = BuiltinTypesCatalogue.getItemTypeByName(identifier.getName());
        this.targetSequenceType = new SequenceType(targetType, SequenceType.Arity.OneOrZero);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        RuntimeIterator castIterator = new CastIterator(
                this.argumentIterator,
                this.targetSequenceType,
                this.staticContext.withStaticType(this.targetSequenceType)
        );
        return castIterator.materializeFirstItemOrNull(dynamicContext);
    }
}
