package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.ConstructorFunctionResolver.ResolvedConstructor;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.XmlSchemaCastIterator;
import org.rumbledb.types.SequenceType;

/** Executes an already resolved constructor using the cast plan bound to its static context. */
public class ConstructorFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan castPlan;

    public ConstructorFunctionIterator(
            ResolvedConstructor constructor, List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        if (constructor.usesSchemaCaster()) {
            this.castPlan = new XmlSchemaCastIterator(
                    arguments.get(0),
                    constructor.identifier().getName(),
                    true,
                    staticContext.getXmlSchemaCatalog(),
                    staticContext);
        } else {
            SequenceType targetSequenceType = constructor.signature().getReturnType();
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
