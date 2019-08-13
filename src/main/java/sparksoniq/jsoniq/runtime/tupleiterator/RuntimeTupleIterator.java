/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.DynamicContext.VariableDependency;

public abstract class RuntimeTupleIterator implements RuntimeTupleIteratorInterface, KryoSerializable {
    protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    private final IteratorMetadata metadata;
    protected boolean _hasNext;
    protected boolean _isOpen;
    protected RuntimeTupleIterator _child;
    protected DynamicContext _currentDynamicContext;
    protected Map<String, DynamicContext.VariableDependency> _parentDependencies;

    protected RuntimeTupleIterator(RuntimeTupleIterator child, IteratorMetadata metadata) {
        this.metadata = metadata;
        this._isOpen = false;
        this._child = child;
    }

    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime tuple iterator cannot be opened twice" + ", this: " + this.toString(), getMetadata());
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

    public IteratorMetadata getMetadata() {
        return metadata;
    }

    public abstract boolean isRDD();

    public abstract JavaRDD<FlworTuple> getRDD(DynamicContext context);

    public abstract boolean isDataFrame();

    public abstract Dataset<Row> getDataFrame(DynamicContext context);
    
    public abstract void setParentDependencies(Map<String, DynamicContext.VariableDependency> parentDependencies);

    /*
    * Variable dependencies are variables that MUST be provided in the dynamic context
    * for successful execution.
    * 
    * These variables are:
    * 1. All variables that the expression of the clause depends on (recursive call of getVariableDependencies on the expression)
    * 2. Except those variables bound in the current FLWOR (obtained from the auxiliary method getVariablesBoundInCurrentFLWORExpression), because those are provided in the Tuples
    * 3. Plus (recursively calling getVariableDependencies) all the Variable Dependencies of the child clause if it exists.
    * 
    */
    public Map<String, DynamicContext.VariableDependency> getVariableDependencies()
    {
        Map<String, DynamicContext.VariableDependency> result = new TreeMap<String, DynamicContext.VariableDependency>();
        result.putAll(_child.getVariableDependencies());
        return result;
    }

    /*
     * Returns the variables bound in previous clauses of the current FLWOR.
     * These variables can be removed from the dependencies of expressions in subsequent clauses,
     * because their values are provided in the tuples rather than the dynamic context object.
     */
    public Set<String> getVariablesBoundInCurrentFLWORExpression()
    {
        return new HashSet<String>();
    }
    
    public void print(StringBuffer buffer, int indent)
    {
        for (int i = 0; i < indent; ++i)
        {
            buffer.append("  ");
        }
        buffer.append(getClass().getName());
        buffer.append(" | ");

        buffer.append("Variable dependencies: ");
        Map<String, DynamicContext.VariableDependency> dependencies = getVariableDependencies();
        for(String v : dependencies.keySet())
        {
          buffer.append(v + "(" + dependencies.get(v) + ")"  + " ");
        }
        buffer.append(" | ");

        buffer.append("Variables bound in current FLWOR: ");
        for(String v : getVariablesBoundInCurrentFLWORExpression())
        {
          buffer.append(v + " ");
        }
        buffer.append("\n");

        if(_child != null)
        {
          _child.print(buffer, indent + 1);
        }
    }
}
