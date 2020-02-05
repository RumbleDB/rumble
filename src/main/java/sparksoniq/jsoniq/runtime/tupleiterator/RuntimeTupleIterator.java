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

package sparksoniq.jsoniq.runtime.tupleiterator;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;

import sparksoniq.jsoniq.ExecutionMode;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class RuntimeTupleIterator implements RuntimeTupleIteratorInterface, KryoSerializable {

    private static final long serialVersionUID = 1L;
    protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    private final ExceptionMetadata metadata;
    protected boolean _hasNext;
    protected boolean _isOpen;
    protected RuntimeTupleIterator _child;
    protected DynamicContext _currentDynamicContext;
    protected ExecutionMode _highestExecutionMode;

    protected RuntimeTupleIterator(RuntimeTupleIterator child, ExecutionMode executionMode, ExceptionMetadata metadata) {
        this.metadata = metadata;
        this._isOpen = false;
        this._highestExecutionMode = executionMode;
        this._child = child;
    }

    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException(
                    "Runtime tuple iterator cannot be opened twice" + ", this: " + this.toString(),
                    getMetadata()
            );
        this._isOpen = true;
        this._hasNext = true;
        this._currentDynamicContext = context;
    }

    public void close() {
        this._isOpen = false;
        this._child.close();
    }

    public void reset(DynamicContext context) {
        this._hasNext = true;
        this._currentDynamicContext = context;
        this._child.reset(context);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(_hasNext);
        output.writeBoolean(_isOpen);
        kryo.writeObject(output, this._currentDynamicContext);
        kryo.writeObject(output, this._child);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._hasNext = input.readBoolean();
        this._isOpen = input.readBoolean();
        this._currentDynamicContext = kryo.readObject(input, DynamicContext.class);
        this._child = kryo.readObject(input, RuntimeTupleIterator.class);
    }

    public boolean isOpen() {
        return _isOpen;
    }

    public boolean hasNext() {
        return this._hasNext;
    }

    public abstract FlworTuple next();

    public ExceptionMetadata getMetadata() {
        return metadata;
    }

    public ExecutionMode getHighestExecutionMode() {
        return _highestExecutionMode;
    }

    public boolean isDataFrame() {
        if (_highestExecutionMode == ExecutionMode.UNSET) {
            throw new OurBadException("isDataFrame accessed in iterator without execution mode being set.");
        }
        return _highestExecutionMode.isDataFrame();
    }

    /**
     * Obtains the dataframe from the child clause.
     * It is possible, with the second parameter, to specify the variables it needs to project the others away,
     * or that only a count is needed for a specific variable, which allows projecting away the actual items.
     *
     * @param context the dynamic context in which the evaluate the child clause's dataframe.
     * @param parentProjection information on the projection needed by the caller.
     * @return the DataFrame with the tuples returned by the child clause.
     */
    public abstract Dataset<Row> getDataFrame(
            DynamicContext context,
            Map<String, DynamicContext.VariableDependency> parentProjection
    );

    /**
     * Builds the DataFrame projection that this clause needs to receive from its child clause.
     * The intent is that the result of this method is forwarded to the child clause in getDataFrame() so it can
     * optimize some values away.
     * 
     * @parentProjection the projection needed by the parent clause.
     * @return the projection needed by this clause.
     */
    public abstract Map<String, DynamicContext.VariableDependency> getProjection(
            Map<String, DynamicContext.VariableDependency> parentProjection
    );

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
    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<String, DynamicContext.VariableDependency>();
        result.putAll(_child.getVariableDependencies());
        return result;
    }

    /**
     * Returns the variables bound in descendant (previous) clauses of the current FLWOR.
     * These variables can be removed from the dependencies of expressions in ascendent (subsequent) clauses,
     * because their values are provided in the tuples rather than the dynamic context object.
     * 
     * @return the set of variable names that are bound by descendant clauses.
     */
    public Set<String> getVariablesBoundInCurrentFLWORExpression() {
        return new HashSet<String>();
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getName());
        buffer.append(" | ");

        buffer.append("Variable dependencies: ");
        Map<String, DynamicContext.VariableDependency> dependencies = getVariableDependencies();
        for (String v : dependencies.keySet()) {
            buffer.append(v + "(" + dependencies.get(v) + ")" + " ");
        }
        buffer.append(" | ");

        buffer.append("Variables bound in current FLWOR: ");
        for (String v : getVariablesBoundInCurrentFLWORExpression()) {
            buffer.append(v + " ");
        }
        buffer.append("\n");

        if (_child != null) {
            _child.print(buffer, indent + 1);
        }
    }
}
