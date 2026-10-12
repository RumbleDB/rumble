/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
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
import java.util.function.BiConsumer;

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

    private final WindowClause.WindowType windowType;
    private final Name windowVariable;

    /**
     * The declared type of the window variable, or {@code null} if none is declared.
     */
    private final SequenceType declaredWindowType;

    private final WindowClause.WindowVars startVariables;

    /**
     * The variables bound by the end condition, or {@code null} for a tumbling window without an end condition.
     */
    private final WindowClause.WindowVars endVariables;

    /**
     * Whether the end condition is declared with {@code only}, in which case a window whose end condition never holds
     * is dropped instead of ending with the sequence.
     */
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
        super(child, staticContext);
        this.windowType = clause.getWindowType();
        this.windowVariable = clause.getWindowVariable();
        this.declaredWindowType = clause.getActualSequenceType();
        this.startVariables = clause.getStartCondition().variables();
        this.endVariables = clause.getEndCondition() == null
                ? null
                : clause.getEndCondition().variables();
        this.endConditionOnly =
                clause.getEndCondition() != null && clause.getEndCondition().only();
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
        return new WindowLocalCursor(context);
    }

    private final class WindowLocalCursor extends AbstractLocalCursor<FlworTuple> {

        private final DynamicContext context;
        private final Deque<FlworTuple> pending = new ArrayDeque<>();
        private Cursor<FlworTuple> childCursor;

        private WindowLocalCursor(DynamicContext context) {
            super(WindowClauseIterator.this.getMetadata());
            this.context = context;
        }

        @Override
        protected void openLocal() {
            if (hasActiveChild()) {
                this.childCursor = WindowClauseIterator.this.child.createNativeCursor(this.context);
                fillPending();
            } else {
                this.pending.addAll(generateWindows(this.context, null));
            }
        }

        private void fillPending() {
            while (this.pending.isEmpty() && this.childCursor != null && this.childCursor.hasNext()) {
                this.pending.addAll(generateWindows(this.context, this.childCursor.next()));
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

    /**
     * Generates all windows over the source sequence for one input tuple.
     *
     * @param inputTuple the tuple from the child clause, or {@code null} if the window clause is the first clause
     */
    private List<FlworTuple> generateWindows(DynamicContext context, FlworTuple inputTuple) {
        DynamicContext sourceContext = new DynamicContext(context);
        if (inputTuple != null) {
            sourceContext.getVariableValues().setBindingsFromTuple(inputTuple, getMetadata());
        }
        if (this.sourceIterator.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            throw new UnsupportedFeatureException("Window clauses require local execution.", getMetadata());
        }
        List<Item> items = this.sourceIterator.materialize(sourceContext);
        List<FlworTuple> results = new ArrayList<>();
        int start = 0;
        while (start < items.size()) {
            if (!startMatches(context, inputTuple, items, start)) {
                start++;
                continue;
            }
            int end = findEnd(context, inputTuple, items, start);
            if (end >= 0) {
                results.add(createTuple(inputTuple, items, start, end));
            }
            if (this.windowType == WindowClause.WindowType.SLIDING) {
                start++;
            } else if (end >= 0) {
                // Tumbling windows do not overlap, so the next window can only start after this one ends.
                start = end + 1;
            } else {
                // A dropped tumbling window would have extended to the end of the sequence.
                break;
            }
        }
        return results;
    }

    /**
     * Returns the position of the last item of the window starting at {@code start}, or -1 if no window is generated.
     */
    private int findEnd(DynamicContext context, FlworTuple inputTuple, List<Item> items, int start) {
        if (this.endCondition == null) {
            // A tumbling window without an end condition ends before the next item that satisfies the start condition.
            for (int nextStart = start + 1; nextStart < items.size(); nextStart++) {
                if (startMatches(context, inputTuple, items, nextStart)) {
                    return nextStart - 1;
                }
            }
            return items.size() - 1;
        }
        for (int end = start; end < items.size(); end++) {
            if (endMatches(context, inputTuple, items, start, end)) {
                return end;
            }
        }
        return this.endConditionOnly ? -1 : items.size() - 1;
    }

    private boolean startMatches(DynamicContext context, FlworTuple inputTuple, List<Item> items, int start) {
        if (this.startCondition.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            throw new UnsupportedFeatureException("Window clauses require local execution.", getMetadata());
        }
        DynamicContext conditionContext = createConditionContext(context, inputTuple);
        bindVariables(items, start, this.startVariables, conditionContext.getVariableValues()::addVariableValue);
        return EffectiveBooleanValue.evaluate(this.startCondition, conditionContext);
    }

    private boolean endMatches(DynamicContext context, FlworTuple inputTuple, List<Item> items, int start, int end) {
        if (this.endCondition.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            throw new UnsupportedFeatureException("Window clauses require local execution.", getMetadata());
        }
        DynamicContext conditionContext = createConditionContext(context, inputTuple);
        bindVariables(items, start, this.startVariables, conditionContext.getVariableValues()::addVariableValue);
        bindVariables(items, end, this.endVariables, conditionContext.getVariableValues()::addVariableValue);
        return EffectiveBooleanValue.evaluate(this.endCondition, conditionContext);
    }

    /**
     * Creates the context in which a condition is evaluated, so that the bindings of one condition do not leak into
     * the next one, while outer bindings remain visible.
     */
    private DynamicContext createConditionContext(DynamicContext context, FlworTuple inputTuple) {
        DynamicContext conditionContext = new DynamicContext(context);
        conditionContext.getVariableValues().removeAllVariables();
        if (inputTuple != null) {
            conditionContext.getVariableValues().setBindingsFromTuple(inputTuple, getMetadata());
        }
        return conditionContext;
    }

    /**
     * Binds the variables of a window condition for the item at the given position: the current item, its position,
     * and the previous and next items, which are empty at the boundaries of the sequence.
     */
    private static void bindVariables(
            List<Item> items, int position, WindowClause.WindowVars variables, BiConsumer<Name, List<Item>> binder) {
        bindIfDeclared(binder, variables.currentItem(), items.get(position));
        bindIfDeclared(binder, variables.position(), ItemFactory.getInstance().createLongItem(position + 1));
        bindIfDeclared(binder, variables.previousItem(), position == 0 ? null : items.get(position - 1));
        bindIfDeclared(binder, variables.nextItem(), position + 1 >= items.size() ? null : items.get(position + 1));
    }

    private static void bindIfDeclared(BiConsumer<Name, List<Item>> binder, Name name, Item item) {
        if (name != null) {
            binder.accept(name, item == null ? Collections.emptyList() : List.of(item));
        }
    }

    /**
     * Creates the output tuple for the window from {@code start} to {@code end}, which extends the input tuple with
     * the window variable and the variables of the start and end conditions.
     */
    private FlworTuple createTuple(FlworTuple inputTuple, List<Item> items, int start, int end) {
        FlworTuple result = inputTuple == null
                ? new FlworTuple(getRuntimeStaticContext().getConfiguration())
                : new FlworTuple(inputTuple);
        List<Item> windowItems = new ArrayList<>(items.subList(start, end + 1));
        validateWindowType(windowItems);
        result.putValue(this.windowVariable, windowItems);
        bindVariables(items, start, this.startVariables, result::putValue);
        if (this.endVariables != null) {
            bindVariables(items, end, this.endVariables, result::putValue);
        }
        return result;
    }

    /**
     * Checks that the window matches the declared type of the window variable. This is done at runtime because the
     * size of a window depends on the data.
     *
     * @throws UnexpectedTypeException if the window does not match the declared type
     */
    private void validateWindowType(List<Item> windowItems) {
        if (this.declaredWindowType == null) {
            return;
        }
        boolean validCardinality =
                switch (this.declaredWindowType.getArity()) {
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
                            + this.declaredWindowType,
                    getMetadata());
        }
        for (Item item : windowItems) {
            if (!InstanceOfIterator.doesItemTypeMatchItem(this.declaredWindowType.getItemType(), item)) {
                throw new UnexpectedTypeException(
                        item.getDynamicType()
                                + " is not expected here. The expected type is "
                                + this.declaredWindowType,
                        getMetadata());
            }
        }
    }

    private boolean hasActiveChild() {
        return this.child != null && this.evaluationDepthLimit != 0;
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
