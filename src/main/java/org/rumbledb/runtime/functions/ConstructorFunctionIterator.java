package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.XmlSchemaCastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.xml.schema.XmlSchemaCatalog;
import org.rumbledb.xml.schema.XmlSchemaCatalogLoader;

public class ConstructorFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan castPlan;

    public ConstructorFunctionIterator(
            FunctionIdentifier identifier, List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        if (XmlSchemaCatalog.isBuiltInListType(identifier.getName())) {
            this.castPlan = new XmlSchemaCastIterator(
                    arguments.get(0),
                    identifier.getName(),
                    true,
                    XmlSchemaCatalogLoader.loadBuiltInCatalog(),
                    staticContext);
        } else {
            ItemType targetType = BuiltinTypesCatalogue.getItemTypeByName(identifier.getName());
            SequenceType targetSequenceType = new SequenceType(targetType, SequenceType.Arity.OneOrZero);
            this.castPlan = new CastIterator(
                    arguments.get(0),
                    targetSequenceType,
                    staticContext.toBuilder().staticType(targetSequenceType).build());
        }
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext dynamicContext) {
        return this.castPlan.getCursor(dynamicContext);
    }
}
