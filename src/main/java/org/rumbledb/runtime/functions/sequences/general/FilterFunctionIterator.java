package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class FilterFunctionIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new FilterLocalCursor(
                this.sequenceIterator,
                this.predicateIterator,
                context,
                getRuntimeStaticContext()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator;
    private final RuntimeIterator predicateIterator;

    public FilterFunctionIterator(
            List<RuntimeIterator> arguments,
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
            RuntimeIterator predicateIterator,
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
        List<RuntimeIterator> callbackArguments = new ArrayList<>(1);
        callbackArguments.add(new ConstantRuntimeIterator(item, argumentContext));
        RuntimeStaticContext functionItemContext = RuntimeStaticContext.builder()
            .configuration(staticContext.getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(staticContext.getMetadata())
            .build();
        RuntimeIterator callback = new DynamicFunctionCallIterator(
                new ConstantRuntimeIterator(predicate, functionItemContext),
                callbackArguments,
                functionItemContext
        );
        return callback.getEffectiveBooleanValue(context);
    }

    private static boolean acceptsSingleArgument(Item item) {
        if (item.isMap() || item.isArray()) {
            return true;
        }
        return item.isFunction() && item.getIdentifier().getArity() == 1;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:filter is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:filter is currently supported only in local execution mode.");
    }

    private static final class FilterLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator sequencePlan;
        private final RuntimeIterator predicatePlan;
        private final DynamicContext context;
        private final RuntimeStaticContext staticContext;
        private LocalCursor<Item> sequenceCursor;
        private Item predicate;
        private Item nextResult;

        private FilterLocalCursor(
                RuntimeIterator sequencePlan,
                RuntimeIterator predicatePlan,
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
            this.sequenceCursor = this.sequencePlan.createLocalCursor(this.context);
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
