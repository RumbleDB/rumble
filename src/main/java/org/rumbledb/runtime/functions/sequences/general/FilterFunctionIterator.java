package org.rumbledb.runtime.functions.sequences.general;


import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class FilterFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new FilterLocalCursor(
                this.sequenceIterator,
                this.predicateIterator,
                context,
                getRuntimeStaticContext()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> sequenceIterator;
    private final RuntimePlan<Item> predicateIterator;

    public FilterFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("fn:filter must have exactly two arguments.");
        }
        this.sequenceIterator = arguments.get(0);
        this.predicateIterator = arguments.get(1);
    }

    private static Item resolvePredicate(
            RuntimePlan<Item> predicateIterator,
            DynamicContext context,
            RuntimeStaticContext staticContext
    ) {
        List<Item> predicateItems = predicateIterator.materialize(context);
        if (predicateItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "The second argument of fn:filter must be a single function item [err:XPTY0004].",
                    staticContext.getMetadata()
            );
        }
        Item predicate = predicateItems.get(0);
        if (!acceptsSingleArgument(predicate)) {
            throw new UnexpectedTypeException(
                    "The function passed to fn:filter must accept exactly one argument [err:XPTY0004].",
                    staticContext.getMetadata()
            );
        }

        return predicate;
    }

    private static boolean matches(
            Item predicate,
            Item item,
            DynamicContext context,
            RuntimeStaticContext staticContext
    ) {
        RuntimeStaticContext argumentContext = RuntimeStaticContext.builder()
            .configuration(staticContext.getConfiguration())
            .staticType(SequenceType.createSequenceType("item"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(staticContext.getMetadata())
            .build();
        List<RuntimePlan<Item>> callbackArguments = new ArrayList<>(1);
        callbackArguments.add(new ConstantRuntimeIterator(item, argumentContext));
        RuntimeStaticContext functionItemContext = RuntimeStaticContext.builder()
            .configuration(staticContext.getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(staticContext.getMetadata())
            .build();
        RuntimePlan<Item> callback = new DynamicFunctionCallIterator(
                new ConstantRuntimeIterator(predicate, functionItemContext),
                callbackArguments,
                functionItemContext
        );
        return EffectiveBooleanValue.evaluate(callback, context);
    }

    private static boolean acceptsSingleArgument(Item item) {
        if (item.isMap() || item.isArray()) {
            return true;
        }
        return item.isFunction() && item.getIdentifier().getArity() == 1;
    }

    private static final class FilterLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> sequencePlan;
        private final RuntimePlan<Item> predicatePlan;
        private final DynamicContext context;
        private final RuntimeStaticContext staticContext;
        private Cursor<Item> sequenceCursor;
        private Item predicate;
        private Item nextResult;

        private FilterLocalCursor(
                RuntimePlan<Item> sequencePlan,
                RuntimePlan<Item> predicatePlan,
                DynamicContext context,
                RuntimeStaticContext staticContext
        ) {
            super(staticContext.getMetadata());
            this.sequencePlan = sequencePlan;
            this.predicatePlan = predicatePlan;
            this.context = context;
            this.staticContext = staticContext;
        }

        @Override
        protected void openLocal() {
            this.predicate = resolvePredicate(this.predicatePlan, this.context, this.staticContext);
            this.sequenceCursor = this.sequencePlan.getCursor(this.context);
            advance();
        }

        private void advance() {
            this.nextResult = null;
            while (this.sequenceCursor.hasNext()) {
                Item candidate = this.sequenceCursor.next();
                if (matches(this.predicate, candidate, this.context, this.staticContext)) {
                    this.nextResult = candidate;
                    return;
                }
            }
        }

        @Override
        protected boolean hasNextLocal() {
            return this.nextResult != null;
        }

        @Override
        protected Item nextLocal() {
            if (this.nextResult == null) {
                throw invalidState("No more fn:filter results are available.");
            }
            Item result = this.nextResult;
            advance();
            return result;
        }

        @Override
        protected void closeLocal() {
            if (this.sequenceCursor != null) {
                this.sequenceCursor.close();
                this.sequenceCursor = null;
            }
            this.predicate = null;
            this.nextResult = null;
        }
    }
}
