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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.flwor.FlworDataFrame;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.tuple.FlworTuple;

public class WindowClauseIterator extends RuntimeTupleIterator {

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
    private final RuntimeIterator sourceIterator;

    /**
     * The iterator that evaluates the start condition of the window.
     * For example, {@code start $x when $x > 1}
     */
    private final RuntimeIterator startCondition;

    /**
     * The iterator that evaluates the end condition of the window.
     * It is {@code null} for a tumbling window without an explicit end clause.
     * For example, {@code end $y when $y < 3}
     */
    private final RuntimeIterator endCondition;

    /**
     * Store generated window tuples waiting to be consumed.
     */
    private final Deque<FlworTuple> pendingResults = new ArrayDeque<>();

    /**
     * The next result to be returned by the {@code next()} method.
     */
    private transient FlworTuple nextResult;

    /**
     * The input tuple currently being processed
     * If the child iterator is {@code null}, this is always {@code null} as well (for example, when window is the start
     * clause of a FLWOR expression).
     */
    private transient FlworTuple currentInputTuple;

    public WindowClauseIterator(
            RuntimeTupleIterator child,
            WindowClause clause,
            RuntimeIterator sourceIterator,
            RuntimeIterator startCondition,
            RuntimeIterator endCondition,
            RuntimeStaticContext staticContext
    ) {
        super(child, staticContext);
        this.windowType = clause.getWindowType();
        this.windowVariable = clause.getWindowVariable();
        this.declaredWindowType = clause.getActualSequenceType();
        this.startVariables = clause.getStartCondition().variables();
        this.endVariables = clause.getEndCondition() == null ? null : clause.getEndCondition().variables();
        this.endConditionOnly = clause.getEndCondition() != null && clause.getEndCondition().only();
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
    public void open(DynamicContext context) {
        super.open(context);
        this.pendingResults.clear();
        this.nextResult = null;
        this.currentInputTuple = null;
        if (!this.hasActiveChild()) {
            // No upcoming tuple
            this.prepareForTuple(null);
            this.prepareNextResult();
        } else {
            this.child.open(this.currentDynamicContext);
            this.prepareNextTuple();
        }
    }

    @Override
    public void reset(DynamicContext context) {
        this.closeExpressionIterators();
        this.hasNext = true;
        this.currentDynamicContext = context;
        this.pendingResults.clear();
        this.nextResult = null;
        this.currentInputTuple = null;

        if (!this.hasActiveChild()) {
            this.prepareForTuple(null);
            this.prepareNextResult();
        } else {
            this.child.reset(this.currentDynamicContext);
            this.prepareNextTuple();
        }
    }

    @Override
    public void close() {
        this.closeExpressionIterators();
        if (this.hasActiveChild() && this.child.isOpen()) {
            this.child.close();
        }
        this.pendingResults.clear();
        this.nextResult = null;
        this.currentInputTuple = null;
        this.isOpen = false;
    }

    private boolean hasActiveChild() {
        return this.child != null && this.evaluationDepthLimit != 0;
    }

    private void closeExpressionIterators() {
        if (this.sourceIterator.isOpen()) {
            this.sourceIterator.close();
        }
        if (this.startCondition.isOpen()) {
            this.startCondition.close();
        }
        if (this.endCondition != null && this.endCondition.isOpen()) {
            this.endCondition.close();
        }
    }

    @Override
    public FlworTuple next() {
        if (!this.hasNext) {
            throw new IteratorFlowException("Invalid next() call in window clause", this.getMetadata());
        }
        FlworTuple result = this.nextResult;
        this.prepareNextResult();
        return result;
    }

    private void prepareNextResult() {
        if (!this.pendingResults.isEmpty()) {
            // Consume from the deque if it's not empty
            this.nextResult = this.pendingResults.removeFirst();
            this.hasNext = true;
            return;
        }

        // In case that the deque is empty, we need to query for more
        if (!this.hasActiveChild()) {
            // No upcoming tuple
            this.hasNext = false;
            return;
        }

        // Prepare for the next tuple from the child iterator
        this.prepareNextTuple();
    }

    private void prepareNextTuple() {
        while (this.child.hasNext()) {
            FlworTuple inputTuple = this.child.next();
            this.prepareForTuple(inputTuple);
            if (!this.pendingResults.isEmpty()) {
                // At lease one window has been generated
                this.nextResult = this.pendingResults.removeFirst();
                this.hasNext = true;
                return;
            }
        }

        // If we reach here, it means that the child iterator has no more tuples to provide
        this.hasNext = false;
        this.child.close();
    }


    /**
     * Used for intermediate window clauses to process the tuple coming from the child iterator.
     * 
     * @param inputTuple if the window clause is not the first clause of the FLWOR expression, this is the tuple coming
     *        from the child iterator. Otherwise, it is {@code null}.
     */
    private void prepareForTuple(FlworTuple inputTuple) {
        this.currentInputTuple = inputTuple;
        List<Item> items = this.materializeSource(inputTuple);

        if (!items.isEmpty()) {
            if (this.windowType == WindowClause.WindowType.TUMBLING) {
                this.generateTumblingWindows(items, inputTuple);
            } else {
                this.generateSlidingWindows(items, inputTuple);
            }
        }
    }

    /**
     * Evaluate the expression after {@code in} and materialize the results into a list of items.
     * 
     * @param inputTuple if the window clause is not the first clause of the FLWOR expression, this is the tuple coming
     *        from the child iterator. Otherwise, it is {@code null}.
     * @return a list of items produced by the source iterator
     */
    private List<Item> materializeSource(FlworTuple inputTuple) {
        DynamicContext sourceContext = new DynamicContext(this.currentDynamicContext);
        if (inputTuple != null) {
            // Bind the variables from the input tuple to the source context so that the source iterator can access them
            // For example: for $x in ... for sliding window $w in $x
            // Here we have a flwor tuple with $x variable that needs to be bind to the source iterator so it can
            // evaluate
            sourceContext.getVariableValues().setBindingsFromTuple(inputTuple, this.getMetadata());
        }
        if (this.sourceIterator.isRDDOrDataFrame()) {
            throw new UnsupportedFeatureException("Window clauses require local execution.", this.getMetadata());
        }

        List<Item> items = new ArrayList<>();
        this.sourceIterator.open(sourceContext);
        try {
            while (this.sourceIterator.hasNext()) {
                items.add(this.sourceIterator.next());
            }
        } finally {
            if (this.sourceIterator.isOpen()) {
                this.sourceIterator.close();
            }
        }
        return items;
    }

    /**
     * Generate tumbling windows (no overlap) from the list of items and add them to the pending results deque.
     * 
     * @param items the list of items produced by the source iterator
     * @param inputTuple if the window clause is not the first clause of the FLWOR expression, this is the tuple coming
     *        from the child iterator. Otherwise, it is {@code null}.
     */
    private void generateTumblingWindows(List<Item> items, FlworTuple inputTuple) {
        int start = 0;
        while (start < items.size()) {
            while (
                start < items.size()
                    && !this.matches(this.startCondition, this.startVariables, false, items, start, start)
            ) {
                // Skip items until we find a start condition match (fine because tumbling windows do not overlap)
                start++;
            }
            if (start >= items.size()) {
                // No more start condition matches, we are done
                return;
            }
            // Find the end of the window starting from the current start position
            int end = this.findEnd(items, start);
            if (end < 0) {
                // No end condition match found, we are done
                return;
            }

            // Create a new tuple for the window and add it to the pending results deque
            this.pendingResults.add(this.createTuple(inputTuple, items, start, end));
            start = end + 1;
        }
    }

    /**
     * Generate sliding windows (can overlap) from the list of items and add them to the pending results deque.
     * 
     * @param items the list of items produced by the source iterator
     * @param inputTuple if the window clause is not the first clause of the FLWOR expression, this is the tuple coming
     *        from the child iterator. Otherwise, it is {@code null}.
     */
    private void generateSlidingWindows(List<Item> items, FlworTuple inputTuple) {
        for (int start = 0; start < items.size(); start++) {
            if (!this.matches(this.startCondition, this.startVariables, false, items, start, start)) {
                // This item does not match the start condition, skip it
                continue;
            }
            int end = this.findEnd(items, start);
            if (end >= 0) {
                this.pendingResults.add(this.createTuple(inputTuple, items, start, end));
            }
        }
    }

    private int findEnd(List<Item> items, int start) {
        if (this.endCondition == null) {
            // This can only happen for tumbling windows without an explicit end clause
            // In that case, the end of the window will be the item before the next start condition match, or the last
            // item in the list if there is no next start condition match
            for (int nextStart = start + 1; nextStart < items.size(); nextStart++) {
                if (this.matches(this.startCondition, this.startVariables, false, items, nextStart, nextStart)) {
                    return nextStart - 1;
                }
            }
            return items.size() - 1;
        }

        for (int end = start; end < items.size(); end++) {
            if (this.matches(this.endCondition, this.endVariables, true, items, end, start)) {
                return end;
            }
        }
        return this.endConditionOnly ? -1 : items.size() - 1;
    }

    /**
     * Check if the window condition matches for the given position in the list of items.
     */
    private boolean matches(
            RuntimeIterator conditionIterator,
            WindowClause.WindowVars variables,
            boolean endCondition,
            List<Item> items,
            int position,
            int startPosition
    ) {
        if (conditionIterator.isRDDOrDataFrame()) {
            throw new UnsupportedFeatureException("Window clauses require local execution.", this.getMetadata());
        }

        // Evaluate each condition in a child context so temporary window bindings from one condition do not leak into
        // the next one, while outer bindings (including external variables) remain visible.
        DynamicContext conditionContext = new DynamicContext(this.currentDynamicContext);
        conditionContext.getVariableValues().removeAllVariables();

        if (this.currentInputTuple != null) {
            // Bind the variables from the input tuple to the current dynamic context so that the condition iterator can
            // access them
            conditionContext.getVariableValues().setBindingsFromTuple(this.currentInputTuple, this.getMetadata());
        }

        if (endCondition) {
            // Bind the variables from the start condition to the current dynamic context so that the end condition can
            // access them
            bindTupleContext(
                conditionContext,
                items,
                startPosition,
                this.startVariables
            );
        }

        bindTupleContext(conditionContext, items, position, variables);
        try {
            return conditionIterator.getEffectiveBooleanValue(conditionContext);
        } finally {
            if (conditionIterator.isOpen()) {
                conditionIterator.close();
            }
        }
    }

    /**
     * Bind the window variables to the current dynamic context for the given position in the list of items.
     * 
     * For each condition, it can have up to 4 variables: current item, previous item, next item, and position.
     */
    private static void bindTupleContext(
            DynamicContext context,
            List<Item> items,
            int position,
            WindowClause.WindowVars variables
    ) {
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
    private FlworTuple createTuple(FlworTuple inputTuple, List<Item> items, int start, int end) {
        FlworTuple result = inputTuple == null ? new FlworTuple(this.getConfiguration()) : new FlworTuple(inputTuple);
        List<Item> windowItems = new ArrayList<>(items.subList(start, end + 1));
        this.validateWindowType(windowItems);

        // Window variable will be bound to the list of items in the window
        result.putValue(this.windowVariable, windowItems);

        // Bind the variables from the start and end conditions to the tuple
        addBindings(result, items, start, this.startVariables);
        if (this.endVariables != null) {
            addBindings(result, items, end, this.endVariables);
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
    private void validateWindowType(List<Item> windowItems) {
        SequenceType declaredType = this.declaredWindowType;
        if (declaredType == null) {
            return;
        }

        boolean validCardinality = switch (declaredType.getArity()) {
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
                    this.getMetadata()
            );
        }
        for (Item item : windowItems) {
            if (!InstanceOfIterator.doesItemTypeMatchItem(declaredType.getItemType(), item)) {
                throw new UnexpectedTypeException(
                        item.getDynamicType() + " is not expected here. The expected type is " + declaredType,
                        this.getMetadata()
                );
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
            FlworTuple tuple,
            List<Item> items,
            int position,
            WindowClause.WindowVars variables
    ) {
        if (variables.currentItem() != null)
            tuple.putValue(variables.currentItem(), items.get(position));
        if (variables.position() != null)
            tuple.putValue(variables.position(), ItemFactory.getInstance().createLongItem(position + 1));
        if (variables.previousItem() != null) {
            tuple.putValue(
                variables.previousItem(),
                position == 0 ? Collections.emptyList() : List.of(items.get(position - 1))
            );
        }
        if (variables.nextItem() != null) {
            tuple.putValue(
                variables.nextItem(),
                position + 1 >= items.size() ? Collections.emptyList() : List.of(items.get(position + 1))
            );
        }
    }

    @Override
    public FlworDataFrame getDataFrame(DynamicContext context) {
        throw new UnsupportedFeatureException("Window clauses require local execution.", this.getMetadata());
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
            Set<Name> locallyBoundVariables
    ) {
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
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        if (!this.hasActiveChild()) {
            return Collections.emptyMap();
        }

        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>(parentProjection);
        Set<Name> childVariables = this.child.getOutputTupleVariableNames();
        Set<Name> startBoundVariables = new HashSet<>(this.startVariables.names());
        Set<Name> endBoundVariables = this.endVariables == null
            ? Collections.emptySet()
            : new HashSet<>(this.endVariables.names());

        // Dependencies requested by following clauses for variables introduced by this window stop here. Dependencies
        // on same-named outer variables used by the source or conditions are added back below from those expressions.
        this.getWindowVariables().forEach(result::remove);
        this.addDependencies(
            result,
            this.sourceIterator.getVariableDependencies(),
            Collections.emptySet(),
            childVariables
        );
        this.addDependencies(
            result,
            this.startCondition.getVariableDependencies(),
            startBoundVariables,
            childVariables
        );
        if (this.endCondition != null) {
            Set<Name> conditionBoundVariables = new HashSet<>(startBoundVariables);
            conditionBoundVariables.addAll(endBoundVariables);
            this.addDependencies(
                result,
                this.endCondition.getVariableDependencies(),
                conditionBoundVariables,
                childVariables
            );
        }
        return result;
    }

    private void addDependencies(
            Map<Name, DynamicContext.VariableDependency> target,
            Map<Name, DynamicContext.VariableDependency> dependencies,
            Set<Name> locallyBoundVariables,
            Set<Name> childVariables
    ) {
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
