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
import org.rumbledb.xml.schema.XmlSchemaSimpleTypeValidator;

public class ConstructorFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator argumentIterator;
    private final SequenceType targetSequenceType;
    private final XmlSchemaSimpleTypeValidator schemaValidator;

    public ConstructorFunctionIterator(
            FunctionIdentifier identifier,
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        this(BuiltinTypesCatalogue.getItemTypeByName(identifier.getName()), null, arguments, staticContext);
    }

    public ConstructorFunctionIterator(
            ItemType targetType,
            XmlSchemaSimpleTypeValidator schemaValidator,
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.argumentIterator = arguments.get(0);
        this.targetSequenceType = new SequenceType(targetType, SequenceType.Arity.OneOrZero);
        this.schemaValidator = schemaValidator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        RuntimeIterator castIterator = new CastIterator(
                this.argumentIterator,
                this.targetSequenceType,
                this.staticContext.withStaticType(this.targetSequenceType)
        );
        Item result = castIterator.materializeFirstItemOrNull(dynamicContext);
        if (result != null && this.schemaValidator != null) {
            return this.schemaValidator.validate(result, this.staticContext).get(0);
        }
        return result;
    }
}
