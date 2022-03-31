/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.runtime.flwor.clauses.ForClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.LetClauseSparkIterator;

import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class RuntimeTupleIterator implements RuntimeTupleIteratorInterface, KryoSerializable {

    private static final long serialVersionUID = 1L;
    protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    private final ExceptionMetadata metadata;
    protected RuntimeTupleIterator child;
    protected ExecutionMode highestExecutionMode;
    protected int evaluationDepthLimit;

    protected transient DynamicContext currentDynamicContext;
    protected transient boolean hasNext;
    protected transient boolean isOpen;
    protected transient Map<Name, DynamicContext.VariableDependency> inputTupleProjection;
    protected transient Map<Name, DynamicContext.VariableDependency> outputTupleProjection;

    protected RuntimeTupleIterator(
            RuntimeTupleIterator child,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this.metadata = metadata;
        this.isOpen = false;
        this.highestExecutionMode = executionMode;
        this.child = child;
        this.evaluationDepthLimit = -1;
    }

    public RuntimeTupleIterator getChildIterator() {
        return this.child;
    }

    public void open(DynamicContext context) {
        if (this.isOpen) {
            throw new IteratorFlowException(
                    "Runtime tuple iterator cannot be opened twice" + ", this: " + this.toString(),
                    getMetadata()
            );
        }
        this.isOpen = true;
        this.hasNext = true;
        this.currentDynamicContext = context;
    }

    public void close() {
        this.isOpen = false;
        this.child.close();
    }

    public void reset(DynamicContext context) {
        this.hasNext = true;
        this.currentDynamicContext = context;
        this.child.reset(context);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(this.hasNext);
        output.writeBoolean(this.isOpen);
        kryo.writeObject(output, this.currentDynamicContext);
        kryo.writeObject(output, this.child);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.hasNext = input.readBoolean();
        this.isOpen = input.readBoolean();
        this.currentDynamicContext = kryo.readObject(input, DynamicContext.class);
        this.child = kryo.readObject(input, RuntimeTupleIterator.class);
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    public abstract FlworTuple next();

    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    public ExecutionMode getHighestExecutionMode() {
        return this.highestExecutionMode;
    }

    public boolean isDataFrame() {
        if (this.highestExecutionMode == ExecutionMode.UNSET) {
            throw new OurBadException("isDataFrame accessed in iterator without execution mode being set.");
        }
        return this.highestExecutionMode.isDataFrame();
    }

    /**
     * Obtains the dataframe from the child clause.
     * It is possible, with the second parameter, to specify the variables it needs to project the others away,
     * or that only a count is needed for a specific variable, which allows projecting away the actual items.
     *
     * @param context the dynamic context in which the evaluate the child clause's dataframe.
     * @return the DataFrame with the tuples returned by the child clause.
     */
    public abstract Dataset<Row> getDataFrame(
            DynamicContext context
    );

    /**
     * Builds the DataFrame projection that this clause needs to receive from its child clause.
     * The intent is that the result of this method is forwarded to the child clause in getDataFrame() so it can
     * optimize some values away.
     * Invariant: all keys in getInputTupleVariableDependencies(...) MUST be output tuple variables,
     * i.e., appear in this.child.getOutputTupleVariableNames()
     *
     * @param parentProjection the projection needed by the parent clause.
     * @return the projection needed by this clause.
     */
    protected abstract Map<Name, DynamicContext.VariableDependency> getInputTupleVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    );

    /**
     * Computes and stores the DataFrame projection that this clause needs to receive from its child clause.
     * Also stores that of its parent for future purposes.
     * The intent is that the result of this method is used in getDataFrame() so it can
     * optimize some values away.
     * Invariant: all keys MUST be output tuple variables,
     * i.e., appear in this.child.getOutputTupleVariableNames()
     *
     * @param parentProjection the projection needed by the parent clause.
     */
    public void setInputAndOutputTupleVariableDependencies(
            Map<Name, DynamicContext.VariableDependency> parentProjection
    ) {
        this.outputTupleProjection = parentProjection;
        this.inputTupleProjection = this.getInputTupleVariableDependencies(parentProjection);
        if (this.child != null) {
            this.child.setInputAndOutputTupleVariableDependencies(this.inputTupleProjection);
        }
    }

    /**
     * Variable dependencies are variables that MUST be provided by the parent clause in the dynamic context
     * for successful execution of this clause.
     *
     * These variables are:
     * 1. All variables that the expression of the clause depends on (recursive call of getVariableDependencies on the
     * expression)
     * 2. Except those variables bound in the current FLWOR (obtained from the auxiliary method
     * getVariablesBoundInCurrentFLWORExpression), because those are provided in the Tuples
     * 3. Plus (recursively calling getVariableDependencies) all the Variable Dependencies of the child clause if it
     * exists.
     *
     * @return a map of variable names to dependencies (FULL, COUNT, ...) that this clause needs to obtain from the
     *         dynamic context.
     */
    public Map<Name, DynamicContext.VariableDependency> getDynamicContextVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<Name, DynamicContext.VariableDependency>();
        result.putAll(this.child.getDynamicContextVariableDependencies());
        return result;
    }

    /**
     * Returns the output tuple variable names.
     * These variables can be removed from the dependencies of expressions in ascendent (subsequent) clauses,
     * because their values are provided in the tuples rather than the dynamic context object.
     *
     * @return the set of variable names that are bound by descendant clauses.
     */
    public Set<Name> getOutputTupleVariableNames() {
        return new HashSet<Name>();
    }

    /**
     * Returns the limit on how deep the evaluation occurs.
     * If it is 0, the clause ignores its child (this is for join purposes).
     * 
     * @return The evaluation depth limit. -1 if none.
     */
    public int getEvaluationDepthLimit() {
        return this.evaluationDepthLimit;
    }

    /**
     * Sets the limit on how deep the evaluation occurs.
     * 0 to stop here.
     * 
     * @param limit the limit to set. Must be between 0 and getHeight(), inclusive.
     */
    public void setEvaluationDepthLimit(int limit) {
        this.evaluationDepthLimit = limit;
        if (limit == 0) {
            if (!(this instanceof ForClauseSparkIterator || this instanceof LetClauseSparkIterator)) {
                throw new OurBadException(
                        "We cannot stop the evaluation of FLWOR clauses at any other place than a let or a for clause."
                );
            }
        }
        if (limit == -1) {
            if (this.child != null) {
                this.child.setEvaluationDepthLimit(-1);
                return;
            }
        }
        if (this.child == null) {
            if (limit > 0) {
                throw new OurBadException(
                        "We cannot stop the evaluation of FLWOR clauses beyond the height of the tree."
                );
            }
        }
        if (this.child != null) {
            this.child.setEvaluationDepthLimit(limit - 1);
            return;
        }
    }

    /**
     * Tells whether it is possible to set the limit on how deep the evaluation occurs.
     * 0 to stop here.
     * 
     * @param limit the limit to set. Must be between 0 and getHeight(), inclusive.
     */
    public boolean canSetEvaluationDepthLimit(int limit) {
        if (limit == 0) {
            return this instanceof ForClauseSparkIterator || this instanceof LetClauseSparkIterator;
        }
        if (limit == -1) {
            return true;
        }
        if (this.child == null) {
            return limit <= 0;
        }
        return this.child.canSetEvaluationDepthLimit(limit - 1);
    }

    /**
     * Returns the clause subtree at the specified offset.
     * The parameter is compatible with setEvaluationDepthLimit, i.e., it returns the subtree right
     * below where the evaluation stops with the same limit.
     * 
     * @return The evaluation depth limit. -1 if none.
     */
    public RuntimeTupleIterator getSubtreeBeyondLimit(int limit) {
        if (this.child == null) {
            throw new OurBadException(
                    "Trying to get FLWOR clause subtree at depth " + limit + " but there are not further descendants."
            );
        }
        if (limit == 0) {
            return this.child;
        } else {
            return this.child.getSubtreeBeyondLimit(limit - 1);
        }
    }

    /**
     * Returns the height of the clause within the current FLWOR expression, i.e.,
     * the number of descendant clauses.
     * 
     * @return The number of descendant clauses. 0 if it is a starting clause.
     */
    public int getHeight() {
        if (this.child == null) {
            return 0;
        }
        return 1 + this.child.getHeight();
    }

    /**
     * Says whether or not the clause and its descendants include a clause
     * of the specified kind.
     * 
     * @param kind the kind of clause to test for.
     * @return true if there is one. False otherwise.
     */
    public abstract boolean containsClause(FLWOR_CLAUSES kind);

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        print(stringBuffer, 0);
        return stringBuffer.toString();
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append("\n");

        for (int i = 0; i < indent + 6; ++i) {
            buffer.append("  ");
        }
        buffer.append("Dynamic context variable dependencies: ");
        Map<Name, DynamicContext.VariableDependency> dependencies = getDynamicContextVariableDependencies();
        for (Name v : dependencies.keySet()) {
            buffer.append(v + "(" + dependencies.get(v) + ")" + " ");
        }
        buffer.append("\n");

        for (int i = 0; i < indent + 6; ++i) {
            buffer.append("  ");
        }
        buffer.append("Input variable dependencies: ");
        for (Name v : this.inputTupleProjection.keySet()) {
            buffer.append(v + "(" + this.inputTupleProjection.get(v) + ")" + " ");
        }
        buffer.append("\n");

        for (int i = 0; i < indent + 6; ++i) {
            buffer.append("  ");
        }
        buffer.append("Output variable dependencies: ");
        for (Name v : this.outputTupleProjection.keySet()) {
            buffer.append(v + "(" + this.outputTupleProjection.get(v) + ")" + " ");
        }
        buffer.append("\n");

        for (int i = 0; i < indent + 6; ++i) {
            buffer.append("  ");
        }
        buffer.append("Variables bound in current FLWOR: ");
        for (Name v : getOutputTupleVariableNames()) {
            buffer.append(v + " ");
        }
        buffer.append("\n");

        for (int i = 0; i < indent + 6; ++i) {
            buffer.append("  ");
        }
        buffer.append("Height: " + this.getHeight());
        buffer.append("\n");
        for (int i = 0; i < indent + 6; ++i) {
            buffer.append("  ");
        }
        buffer.append("Limit: " + this.evaluationDepthLimit);
        buffer.append("\n");

        if (this.child != null) {
            this.child.print(buffer, indent + 1);
        }
    }

    /**
     * Says whether this expression evaluation triggers a Spark job.
     *
     * @return true if the execution triggers a Spark, false otherwise, null if undetermined yet.
     */
    public abstract boolean isSparkJobNeeded();
}
