package org.rumbledb.runtime.flwor.clauses;

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

    private static final long serialVersionUID = 1L;

    /**
     * The window clause that this iterator is evaluating.
     */
    private final WindowClause clause;

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
        this.clause = clause;
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
        if (this.child == null || this.evaluationDepthLimit == 0) {
            // No upcoming tuple
            prepareForTuple(null);
            prepareNextResult();
        } else {
            this.child.open(this.currentDynamicContext);
            prepareNextTuple();
        }
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.pendingResults.clear();
        if (this.child == null || this.evaluationDepthLimit == 0) {
            prepareForTuple(null);
            prepareNextResult();
        } else {
            this.child.reset(this.currentDynamicContext);
            prepareNextTuple();
        }
    }

    @Override
    public void close() {
        if (this.sourceIterator.isOpen()) {
            this.sourceIterator.close();
        }
        if (this.child != null && this.child.isOpen()) {
            this.child.close();
        }
        this.isOpen = false;
    }

    @Override
    public FlworTuple next() {
        if (!this.hasNext) {
            throw new IteratorFlowException("Invalid next() call in window clause", getMetadata());
        }
        FlworTuple result = this.nextResult;
        prepareNextResult();
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
        if (this.child == null || this.evaluationDepthLimit == 0) {
            // No upcoming tuple
            this.hasNext = false;
            return;
        }

        // Prepare for the next tuple from the child iterator
        prepareNextTuple();
    }

    private void prepareNextTuple() {
        while (this.child.hasNext()) {
            FlworTuple inputTuple = this.child.next();
            prepareForTuple(inputTuple);
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
        List<Item> items = materializeSource(inputTuple);

        if (!items.isEmpty()) {
            if (this.clause.getWindowType() == WindowClause.WindowType.TUMBLING) {
                generateTumblingWindows(items, inputTuple);
            } else {
                generateSlidingWindows(items, inputTuple);
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
            sourceContext.getVariableValues().setBindingsFromTuple(inputTuple, getMetadata());
        }
        if (this.sourceIterator.isRDDOrDataFrame()) {
            throw new UnsupportedFeatureException("Window clauses require local execution.", getMetadata());
        }

        List<Item> items = new ArrayList<>();
        this.sourceIterator.open(sourceContext);
        while (this.sourceIterator.hasNext()) {
            items.add(this.sourceIterator.next());
        }
        this.sourceIterator.close();
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
                    && !matches(this.startCondition, this.clause.getStartCondition(), items, start, start)
            ) {
                // Skip items until we find a start condition match (fine because tumbling windows do not overlap)
                start++;
            }
            if (start >= items.size()) {
                // No more start condition matches, we are done
                return;
            }
            // Find the end of the window starting from the current start position
            int end = findEnd(items, start);
            if (end < 0) {
                // No end condition match found, we are done
                return;
            }

            // Create a new tuple for the window and add it to the pending results deque
            this.pendingResults.add(createTuple(inputTuple, items, start, end));
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
            if (!matches(this.startCondition, this.clause.getStartCondition(), items, start, start)) {
                // This item does not match the start condition, skip it
                continue;
            }
            int end = findEnd(items, start);
            if (end >= 0) {
                this.pendingResults.add(createTuple(inputTuple, items, start, end));
            }
        }
    }

    private int findEnd(List<Item> items, int start) {
        for (int end = start; end < items.size(); end++) {
            if (matches(this.endCondition, this.clause.getEndCondition(), items, end, start)) {
                return end;
            }
        }
        return this.clause.getEndCondition().only() ? -1 : items.size() - 1;
    }

    /**
     * Check if the window condition matches for the given position in the list of items.
     */
    private boolean matches(
            RuntimeIterator conditionIterator,
            WindowClause.WindowCondition condition,
            List<Item> items,
            int position,
            int startPosition
    ) {
        // Clear the variable bindings in the current dynamic context before evaluating the condition (to avoid variable
        // name clashes)
        this.currentDynamicContext.getVariableValues().removeAllVariables();

        if (this.currentInputTuple != null) {
            // Bind the variables from the input tuple to the current dynamic context so that the condition iterator can
            // access them
            this.currentDynamicContext.getVariableValues().setBindingsFromTuple(this.currentInputTuple, getMetadata());
        }

        if (condition == this.clause.getEndCondition()) {
            // Bind the variables from the start condition to the current dynamic context so that the end condition can
            // access them
            bindTupleContext(
                this.currentDynamicContext,
                items,
                startPosition,
                this.clause.getStartCondition().variables()
            );
        }

        bindTupleContext(this.currentDynamicContext, items, position, condition.variables());
        return conditionIterator.getEffectiveBooleanValue(this.currentDynamicContext);
    }

    /**
     * Bind the window variables to the current dynamic context for the given position in the list of items.
     * 
     * For each condition, it can have up to 4 variables: current item, previous item, next item, and position.
     */
    private void bindTupleContext(
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

    private void putItem(DynamicContext context, Name name, Item item) {
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
        FlworTuple result = inputTuple == null ? new FlworTuple(getConfiguration()) : new FlworTuple(inputTuple);
        List<Item> windowItems = new ArrayList<>(items.subList(start, end + 1));
        validateWindowType(windowItems);

        // Window variable will be bound to the list of items in the window
        result.putValue(this.clause.getWindowVariable(), windowItems);

        // Bind the variables from the start and end conditions to the tuple
        addBindings(result, items, start, this.clause.getStartCondition().variables());
        if (this.clause.getEndCondition() != null) {
            addBindings(result, items, end, this.clause.getEndCondition().variables());
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
        SequenceType declaredType = this.clause.getActualSequenceType();
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
                    getMetadata()
            );
        }
        for (Item item : windowItems) {
            if (!InstanceOfIterator.doesItemTypeMatchItem(declaredType.getItemType(), item)) {
                throw new UnexpectedTypeException(
                        item.getDynamicType() + " is not expected here. The expected type is " + declaredType,
                        getMetadata()
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
    private void addBindings(FlworTuple tuple, List<Item> items, int position, WindowClause.WindowVars variables) {
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
        throw new UnsupportedFeatureException("Window clauses require local execution.", getMetadata());
    }

    @Override
    protected Map<Name, DynamicContext.VariableDependency> getInputTupleVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>(parentProjection);
        addDependencies(result, this.sourceIterator.getVariableDependencies());
        addDependencies(result, this.startCondition.getVariableDependencies());
        if (this.endCondition != null)
            addDependencies(result, this.endCondition.getVariableDependencies());
        return result;
    }

    private void addDependencies(
            Map<Name, DynamicContext.VariableDependency> target,
            Map<Name, DynamicContext.VariableDependency> dependencies
    ) {
        dependencies.forEach((name, dependency) -> {
            if (this.child != null && this.child.getOutputTupleVariableNames().contains(name)) {
                target.put(name, dependency);
            }
        });
    }

    /**
     * Reports all variables available after the window clause
     */
    @Override
    public Set<Name> getOutputTupleVariableNames() {
        Set<Name> result = this.child == null ? new HashSet<>() : this.child.getOutputTupleVariableNames();
        result.add(this.clause.getWindowVariable());
        result.addAll(this.clause.getStartCondition().variables().names());
        if (this.clause.getEndCondition() != null)
            result.addAll(this.clause.getEndCondition().variables().names());
        return result;
    }

    @Override
    public boolean containsClause(FLWOR_CLAUSES kind) {
        return kind == FLWOR_CLAUSES.WINDOW || (this.child != null && this.child.containsClause(kind));
    }

    @Override
    public boolean isSparkJobNeeded() {
        return false;
    }
}
