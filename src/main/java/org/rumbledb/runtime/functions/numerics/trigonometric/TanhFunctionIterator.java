package org.rumbledb.runtime.functions.numerics.trigonometric;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class TanhFunctionIterator
        extends AbstractAtMostOneItemRuntimePlan
        implements NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan argument;

    public TanhFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext) {
        super(arguments, staticContext);

        this.argument = arguments.get(0);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        Item value = this.argument.materializeFirstOrNull(dynamicContext);

        if (value == null) {
            return null;
        }

        return evaluate(value);
    }

    private static Item evaluate(Item value) {
        double dvalue = value.getDoubleValue();

        if (Double.isNaN(dvalue) || Double.isInfinite(dvalue)) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }

        return ItemFactory.getInstance().createDoubleItem(Math.tanh(dvalue));
    }

    @Override
    public NativeClauseContext generateNativeQuery(
            NativeClauseContext nativeClauseContext) {

        NativeClauseContext childQuery =
                NativeQueryRuntimePlan.generate(this.argument, nativeClauseContext);

        if (childQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }

        if (SequenceType.Arity.OneOrMore.isSubtypeOf(
                childQuery.getResultingType().getArity())) {
            return NativeClauseContext.NoNativeQuery;
        }

        String resultingQuery =
                "TANH( " + childQuery.getResultingQuery() + " )";

        return new NativeClauseContext(
                childQuery,
                resultingQuery,
                new SequenceType(
                        BuiltinTypesCatalogue.doubleItem,
                        childQuery.getResultingType().getArity()));
    }
}