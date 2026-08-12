package org.rumbledb.runtime.flwor.clauses;

import java.io.Serial;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.TupleRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.tuple.FlworTuple;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

public class WindowClauseIterator extends TupleRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The window clause that this iterator is evaluating.
     */
    private final WindowClause.WindowType windowType;

    private final Name windowVariable;
    private final SequenceType declaredWindowType;
    private final WindowClause.WindowVars startVariables;
    private final WindowClause.WindowVars endVariables;
    private final boolean endConditionOnly;

    /**
     * The iterator that produces the items to be windowed.
     * For example, {@code for sliding window in (1, 2, 3)}
     */
    private final ItemRuntimePlan sourceIterator;

    /**
     * The iterator that evaluates the start condition of the window.
     * For example, {@code start $x when $x > 1}
     */
    private final ItemRuntimePlan startCondition;

    /**
     * The iterator that evaluates the end condition of the window.
     * It is {@code null} for a tumbling window without an explicit end clause.
     * For example, {@code end $y when $y < 3}
     */
    private final ItemRuntimePlan endCondition;

    public WindowClauseIterator(
            TupleRuntimePlan child,
            WindowClause clause,
            ItemRuntimePlan sourceIterator,
            ItemRuntimePlan startCondition,
            ItemRuntimePlan endCondition,
            RuntimeStaticContext staticContext) {
        this(
                child,
                clause.getWindowType(),
                clause.getWindowVariable(),
                clause.getActualSequenceType(),
                clause.getStartCondition().variables(),
                clause.getEndCondition() == null
                        ? null
                        : clause.getEndCondition().variables(),
                clause.getEndCondition() != null && clause.getEndCondition().only(),
                sourceIterator,
                startCondition,
                endCondition,
                staticContext);
    }

    private WindowClauseIterator(
            TupleRuntimePlan child,
            WindowClause.WindowType windowType,
            Name windowVariable,
            SequenceType declaredWindowType,
            WindowClause.WindowVars startVariables,
            WindowClause.WindowVars endVariables,
            boolean endConditionOnly,
            ItemRuntimePlan sourceIterator,
            ItemRuntimePlan startCondition,
            ItemRuntimePlan endCondition,
            RuntimeStaticContext staticContext) {
        super(child, staticContext);
        this.windowType = windowType;
        this.windowVariable = windowVariable;
        this.declaredWindowType = declaredWindowType;
        this.startVariables = startVariables;
        this.endVariables = endVariables;
        this.endConditionOnly = endConditionOnly;
        this.sourceIterator = sourceIterator;
        this.startCondition = startCondition;
        this.endCondition = endCondition;
        this.sourceIterator.getVariableDependencies();
        this.startCondition.getVariableDependencies();
        if (this.endCondition != null) {
            this.endCondition.getVariableDependencies();
        }
    }

    @Override
    public Cursor<FlworTuple> createNativeCursor(DynamicContext context) {
        return new WindowLocalCursor(
                new WindowSpec(
                        this.child,
                        this.evaluationDepthLimit,
                        this.windowType,
                        this.windowVariable,
                        this.declaredWindowType,
                        this.startVariables,
                        this.endVariables,
                        this.endConditionOnly,
                        this.sourceIterator,
                        this.startCondition,
                        this.endCondition,
                        getRuntimeStaticContext()),
                context);
    }

    private static final class WindowLocalCursor extends AbstractLocalCursor<FlworTuple> {

        private final WindowSpec spec;
        private final DynamicContext context;
        private final Deque<FlworTuple> pending = new ArrayDeque<>();
        private Cursor<FlworTuple> childCursor;

        private WindowLocalCursor(WindowSpec spec, DynamicContext context) {
            super(spec.staticContext.getMetadata());
            this.spec = spec;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            if (this.spec.hasActiveChild()) {
                this.childCursor = this.spec.childPlan.createNativeCursor(this.context);
                fillPending();
            } else {
                this.pending.addAll(generateWindows(this.spec, this.context, null));
            }
        }

        private void fillPending() {
            while (this.pending.isEmpty() && this.childCursor != null && this.childCursor.hasNext()) {
                FlworTuple inputTuple = this.childCursor.next();
                this.pending.addAll(generateWindows(this.spec, this.context, inputTuple));
            }
        }

        @Override
        protected boolean hasNextLocal() {
            fillPending();
            return !this.pending.isEmpty();
        }

        @Override
        protected FlworTuple nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState("No more window-clause tuples are available.");
            }
            return this.pending.removeFirst();
        }

        @Override
        protected void closeLocal() {
            if (this.childCursor != null) {
                this.childCursor.close();
                this.childCursor = null;
            }
            this.pending.clear();
        }
    }

    private static List<FlworTuple> generateWindows(WindowSpec spec, DynamicContext context, FlworTuple inputTuple) {
        DynamicContext sourceContext = new DynamicContext(context);
        if (inputTuple != null) {
            sourceContext.getVariableValues().setBindingsFromTuple(inputTuple, spec.staticContext.getMetadata());
        }
        if (spec.sourcePlan.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            throw new UnsupportedFeatureException(
                    "Window clauses require local execution.", spec.staticContext.getMetadata());
        }
        List<Item> items = spec.sourcePlan.materialize(sourceContext);
        List<FlworTuple> results = new ArrayList<>();
        if (spec.windowType == WindowClause.WindowType.TUMBLING) {
            int start = 0;
            while (start < items.size()) {
                while (start < items.size()
                        && !matches(
                                spec,
                                context,
                                inputTuple,
                                spec.startCondition,
                                spec.startVariables,
                                false,
                                items,
                                start,
                                start)) {
                    start++;
                }
                if (start >= items.size()) {
                    break;
                }
                int end = findEnd(spec, context, inputTuple, items, start);
                if (end < 0) {
                    break;
                }
                results.add(createTuple(spec, inputTuple, items, start, end));
                start = end + 1;
            }
        } else {
            for (int start = 0; start < items.size(); start++) {
                if (!matches(
                        spec,
                        context,
                        inputTuple,
                        spec.startCondition,
                        spec.startVariables,
                        false,
                        items,
                        start,
                        start)) {
                    continue;
                }
                int end = findEnd(spec, context, inputTuple, items, start);
                if (end >= 0) {
                    results.add(createTuple(spec, inputTuple, items, start, end));
                }
            }
        }
        return results;
    }

    private static int findEnd(
            WindowSpec spec, DynamicContext context, FlworTuple inputTuple, List<Item> items, int start) {
        if (spec.endCondition == null) {
            for (int nextStart = start + 1; nextStart < items.size(); nextStart++) {
                if (matches(
                        spec,
                        context,
                        inputTuple,
                        spec.startCondition,
                        spec.startVariables,
                        false,
                        items,
                        nextStart,
                        nextStart)) {
                    return nextStart - 1;
                }
            }
            return items.size() - 1;
        }
        for (int end = start; end < items.size(); end++) {
            if (matches(spec, context, inputTuple, spec.endCondition, spec.endVariables, true, items, end, start)) {
                return end;
            }
        }
        return spec.endConditionOnly ? -1 : items.size() - 1;
    }

    private static boolean matches(
            WindowSpec spec,
            DynamicContext context,
            FlworTuple inputTuple,
            ItemRuntimePlan condition,
            WindowClause.WindowVars variables,
            boolean isEndCondition,
            List<Item> items,
            int position,
            int startPosition) {
        if (condition.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            throw new UnsupportedFeatureException(
                    "Window clauses require local execution.", spec.staticContext.getMetadata());
        }
        DynamicContext conditionContext = new DynamicContext(context);
        conditionContext.getVariableValues().removeAllVariables();
        if (inputTuple != null) {
            conditionContext.getVariableValues().setBindingsFromTuple(inputTuple, spec.staticContext.getMetadata());
        }
        if (isEndCondition) {
            bindTupleContext(conditionContext, items, startPosition, spec.startVariables);
        }
        bindTupleContext(conditionContext, items, position, variables);
        return EffectiveBooleanValue.evaluate(condition, conditionContext);
    }

    private static final class WindowSpec {

        private final TupleRuntimePlan childPlan;
        private final int evaluationDepthLimit;
        private final WindowClause.WindowType windowType;
        private final Name windowVariable;
        private final SequenceType declaredWindowType;
        private final WindowClause.WindowVars startVariables;
        private final WindowClause.WindowVars endVariables;
        private final boolean endConditionOnly;
        private final ItemRuntimePlan sourcePlan;
        private final ItemRuntimePlan startCondition;
        private final ItemRuntimePlan endCondition;
        private final RuntimeStaticContext staticContext;

        private WindowSpec(
                TupleRuntimePlan childPlan,
                int evaluationDepthLimit,
                WindowClause.WindowType windowType,
                Name windowVariable,
                SequenceType declaredWindowType,
                WindowClause.WindowVars startVariables,
                WindowClause.WindowVars endVariables,
                boolean endConditionOnly,
                ItemRuntimePlan sourcePlan,
                ItemRuntimePlan startCondition,
                ItemRuntimePlan endCondition,
                RuntimeStaticContext staticContext) {
            this.childPlan = childPlan;
            this.evaluationDepthLimit = evaluationDepthLimit;
            this.windowType = windowType;
            this.windowVariable = windowVariable;
            this.declaredWindowType = declaredWindowType;
            this.startVariables = startVariables;
            this.endVariables = endVariables;
            this.endConditionOnly = endConditionOnly;
            this.sourcePlan = sourcePlan;
            this.startCondition = startCondition;
            this.endCondition = endCondition;
            this.staticContext = staticContext;
        }

        private boolean hasActiveChild() {
            return this.childPlan != null && this.evaluationDepthLimit != 0;
        }
    }

    private boolean hasActiveChild() {
        return this.child != null && this.evaluationDepthLimit != 0;
    }

    /**
     * Bind the window variables to the current dynamic context for the given position in the list of items.
     *
     * For each condition, it can have up to 4 variables: current item, previous item, next item, and position.
     */
    private static void bindTupleContext(
            DynamicContext context, List<Item> items, int position, WindowClause.WindowVars variables) {
        putItem(context, variables.currentItem(), items.get(position));
        putItem(context, variables.previousItem(), position == 0 ? null : items.get(position - 1));
        putItem(context, variables.nextItem(), position + 1 >= items.size() ? null : items.get(position + 1));
        putItem(context, variables.position(), ItemFactory.getInstance().createLongItem(position + 1));
    }

    private static void putItem(DynamicContext context, Name name, Item item) {
        if (name != null) {
            context.getVariableValues().addVariableValue(name, item == null ? Collections.emptyList() : List.of(item));
        }
    }

    /**
     * Create a new tuple for the window and add it to the pending results deque.
     *
     * @param inputTuple if the window clause is not the first clause of the FLWOR expression, this is the tuple coming
     *        from the child iterator. Otherwise, it is {@code null}. This is used to create a new tuple that includes
     *        the window variable and any other variables from the input tuple.
     * @param items the list of items produced by the source iterator
     * @param start the start position of the window in the list of items
     * @param end the end position of the window in the list of items
     * @return a new tuple that includes the window variable and any other variables from the input tuple
     */
    private static FlworTuple createTuple(
            WindowSpec spec, FlworTuple inputTuple, List<Item> items, int start, int end) {
        FlworTuple result =
                inputTuple == null ? new FlworTuple(spec.staticContext.getConfiguration()) : new FlworTuple(inputTuple);
        List<Item> windowItems = new ArrayList<>(items.subList(start, end + 1));
        validateWindowType(windowItems, spec.declaredWindowType, spec.staticContext);

        // Window variable will be bound to the list of items in the window
        result.putValue(spec.windowVariable, windowItems);

        // Bind the variables from the start and end conditions to the tuple
        addBindings(result, items, start, spec.startVariables);
        if (spec.endVariables != null) {
            addBindings(result, items, end, spec.endVariables);
        }

        return result;
    }

    /**
     * Check that the list of items in the window matches the declared sequence type of the window variable.
     *
     * This has to be done at runtime because the size of the window can vary depending on the input data and the window
     * conditions.
     *
     * @param windowItems the list of items in the window
     * @throws UnexpectedTypeException if the list of items does not match the declared sequence type
     *         of the window variable
     */
    private static void validateWindowType(
            List<Item> windowItems, SequenceType declaredType, RuntimeStaticContext staticContext) {
        if (declaredType == null) {
            return;
        }

        boolean validCardinality =
                switch (declaredType.getArity()) {
                    case Zero -> windowItems.isEmpty();
                    case One -> windowItems.size() == 1;
                    case OneOrZero -> windowItems.size() <= 1;
                    case OneOrMore -> !windowItems.isEmpty();
                    case ZeroOrMore -> true;
                };
        if (!validCardinality) {
            throw new UnexpectedTypeException(
                    "The window sequence has cardinality "
                            + windowItems.size()
                            + ", but the expected type is "
                            + declaredType,
                    staticContext.getMetadata());
        }
        for (Item item : windowItems) {
            if (!InstanceOfIterator.doesItemTypeMatchItem(declaredType.getItemType(), item)) {
                throw new UnexpectedTypeException(
                        item.getDynamicType() + " is not expected here. The expected type is " + declaredType,
                        staticContext.getMetadata());
            }
        }
    }

    /**
     * Add the variables from the window condition to the tuple.
     *
     * @param tuple the tuple to which the variables will be added
     * @param items the list of items produced by the source iterator
     * @param position the position of the item in the list of items
     * @param variables the window variables to be added to the tuple
     */
    private static void addBindings(
            FlworTuple tuple, List<Item> items, int position, WindowClause.WindowVars variables) {
        if (variables.currentItem() != null) tuple.putValue(variables.currentItem(), items.get(position));
        if (variables.position() != null)
            tuple.putValue(variables.position(), ItemFactory.getInstance().createLongItem(position + 1));
        if (variables.previousItem() != null) {
            tuple.putValue(
                    variables.previousItem(),
                    position == 0 ? Collections.emptyList() : List.of(items.get(position - 1)));
        }
        if (variables.nextItem() != null) {
            tuple.putValue(
                    variables.nextItem(),
                    position + 1 >= items.size() ? Collections.emptyList() : List.of(items.get(position + 1)));
        }
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getDynamicContextVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();

        Set<Name> startBoundVariables = new HashSet<>(this.startVariables.names());
        // The source expression is evaluated before the window binds any variables, so none of its dependencies are
        // filtered out. In particular, a source reference with the same name as the window variable still refers to an
        // outer binding.
        mergeDependencies(result, this.sourceIterator.getVariableDependencies(), Collections.emptySet());
        // Start variables are supplied by the iterator for each candidate start item and are therefore not dynamic
        // context dependencies. References to all other variables, including an outer variable shadowed later by the
        // window variable, must be preserved.
        mergeDependencies(result, this.startCondition.getVariableDependencies(), startBoundVariables);

        if (this.endCondition != null) {
            // The end condition receives both the start bindings and its own end bindings from the iterator.
            Set<Name> conditionBoundVariables = new HashSet<>(startBoundVariables);
            conditionBoundVariables.addAll(this.endVariables.names());
            mergeDependencies(result, this.endCondition.getVariableDependencies(), conditionBoundVariables);
        }

        if (this.hasActiveChild()) {
            // Variables produced by preceding FLWOR clauses arrive in the input tuple rather than the dynamic context.
            for (Name variable : this.child.getOutputTupleVariableNames()) {
                result.remove(variable);
            }
            DynamicContext.mergeVariableDependencies(result, this.child.getDynamicContextVariableDependencies());
        }

        return result;
    }

    /**
     * Merges expression dependencies after filtering variables supplied locally by the relevant window condition.
     * Only the variables bound in that condition are filtered: the window variable itself is not in scope in either
     * condition, so a same-named reference may still denote an outer variable and must remain a dependency.
     *
     * @param target accumulated dependencies for the entire window clause
     * @param dependencies dependencies reported by the source or condition expression
     * @param locallyBoundVariables variables supplied by the window iterator, which must not be treated as external
     */
    private static void mergeDependencies(
            Map<Name, DynamicContext.VariableDependency> target,
            Map<Name, DynamicContext.VariableDependency> dependencies,
            Set<Name> locallyBoundVariables) {
        Map<Name, DynamicContext.VariableDependency> filteredDependencies = new TreeMap<>();
        dependencies.forEach((name, dependency) -> {
            if (!locallyBoundVariables.contains(name)) {
                filteredDependencies.put(name, dependency);
            }
        });
        DynamicContext.mergeVariableDependencies(target, filteredDependencies);
    }

    @Override
    protected Map<Name, DynamicContext.VariableDependency> getInputTupleVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> parentProjection) {
        if (!this.hasActiveChild()) {
            return Collections.emptyMap();
        }

        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>(parentProjection);
        Set<Name> childVariables = this.child.getOutputTupleVariableNames();
        Set<Name> startBoundVariables = new HashSet<>(this.startVariables.names());
        Set<Name> endBoundVariables =
                this.endVariables == null ? Collections.emptySet() : new HashSet<>(this.endVariables.names());

        // Dependencies requested by following clauses for variables introduced by this window stop here. Dependencies
        // on same-named outer variables used by the source or conditions are added back below from those expressions.
        this.getWindowVariables().forEach(result::remove);
        this.addDependencies(
                result, this.sourceIterator.getVariableDependencies(), Collections.emptySet(), childVariables);
        this.addDependencies(
                result, this.startCondition.getVariableDependencies(), startBoundVariables, childVariables);
        if (this.endCondition != null) {
            Set<Name> conditionBoundVariables = new HashSet<>(startBoundVariables);
            conditionBoundVariables.addAll(endBoundVariables);
            this.addDependencies(
                    result, this.endCondition.getVariableDependencies(), conditionBoundVariables, childVariables);
        }
        return result;
    }

    private void addDependencies(
            Map<Name, DynamicContext.VariableDependency> target,
            Map<Name, DynamicContext.VariableDependency> dependencies,
            Set<Name> locallyBoundVariables,
            Set<Name> childVariables) {
        Map<Name, DynamicContext.VariableDependency> filteredDependencies = new TreeMap<>();
        dependencies.forEach((name, dependency) -> {
            // Forward only genuine input-tuple dependencies: condition-local bindings are produced by this iterator,
            // and names not produced by the child must instead be obtained from the dynamic context.
            if (!locallyBoundVariables.contains(name) && childVariables.contains(name)) {
                filteredDependencies.put(name, dependency);
            }
        });
        DynamicContext.mergeVariableDependencies(target, filteredDependencies);
    }

    private Set<Name> getWindowVariables() {
        Set<Name> result = new HashSet<>();
        result.add(this.windowVariable);
        result.addAll(this.startVariables.names());
        if (this.endVariables != null) {
            result.addAll(this.endVariables.names());
        }
        return result;
    }

    /**
     * Reports all variables available after the window clause
     */
    @Override
    public Set<Name> getOutputTupleVariableNames() {
        Set<Name> result = new HashSet<>();
        if (this.hasActiveChild()) {
            result.addAll(this.child.getOutputTupleVariableNames());
        }
        result.addAll(this.getWindowVariables());
        return result;
    }

    @Override
    public boolean containsClause(FLWOR_CLAUSES kind) {
        return kind == FLWOR_CLAUSES.WINDOW || (this.child != null && this.child.containsClause(kind));
    }

    @Override
    public boolean isSparkJobNeeded() {
        // Currently, only local execution is supported for window clauses
        return false;
    }
}
