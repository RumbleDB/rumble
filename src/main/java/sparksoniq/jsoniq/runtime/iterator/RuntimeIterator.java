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

package sparksoniq.jsoniq.runtime.iterator;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import sparksoniq.exceptions.InvalidArgumentTypeException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class RuntimeIterator implements RuntimeIteratorInterface, KryoSerializable {

	private static final long serialVersionUID = 1L;
	protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    private final IteratorMetadata metadata;
    protected boolean _hasNext;
    protected boolean _isOpen;
    protected List<RuntimeIterator> _children;
    protected DynamicContext _currentDynamicContext;

    protected RuntimeIterator(List<RuntimeIterator> children, IteratorMetadata metadata) {
        this.metadata = metadata;
        this._isOpen = false;
        this._children = new ArrayList<>();
        if (children != null && !children.isEmpty())
            this._children.addAll(children);
    }

    /**
     * This function calculates the effective boolean value of the sequence given by iterator.
     * Non-empty objects and arrays always return true.
     * Empty sequence returns false.
     * Singleton atomic values are evaluated to their effective boolean value.
     * Multiple atomic values throw an exception.
     *
     * @param iterator has to be opened before calling this function
     * @return
     */
    public static boolean getEffectiveBooleanValue(RuntimeIterator iterator) {
        if (iterator.hasNext()) {
            Item item = iterator.next();
            boolean result;
            if (item.isBoolean())
                result = item.getBooleanValue();
            else if (item.isNumeric()) {
                if (item.isInteger())
                    result = item.getIntegerValue() != 0;
                else if (item.isDouble())
                    result = item.getDoubleValue() != 0;
                else if (item.isDecimal())
                    result = !item.getDecimalValue().equals(0);
                else {
                    throw new SparksoniqRuntimeException("Unexpected numeric type found while calculating effective boolean value.");
                }
            } else if (item.isNull())
                result = false;
            else if (item.isString())
                result = !item.getStringValue().isEmpty();
            else if (item.isObject())
                return true;
            else if (item.isArray())
                return true;
            else {
                throw new SparksoniqRuntimeException("Unexpected item type found while calculating effective boolean value.");
            }

            if (iterator.hasNext()) {
                throw new InvalidArgumentTypeException(
                        "Effective boolean value not defined for sequences of more than one atomic item. "
                                + "Sequence containing: " + item.serialize() + " must be a singleton."
                        , iterator.getMetadata());
            }

            return result;
        } else {
            return false;
        }

    }

    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._hasNext = true;
        this._currentDynamicContext = context;
    }

    public void close() {
        this._isOpen = false;
        this._children.forEach(c -> c.close());
    }

    public void reset(DynamicContext context) {
        this._hasNext = true;
        this._currentDynamicContext = context;
        this._children.forEach(c -> c.reset(context));
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(_hasNext);
        output.writeBoolean(_isOpen);
        kryo.writeObject(output, this._currentDynamicContext);
        kryo.writeObject(output, this._children);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._hasNext = input.readBoolean();
        this._isOpen = input.readBoolean();
        this._currentDynamicContext = kryo.readObject(input, DynamicContext.class);
        this._children = kryo.readObject(input, ArrayList.class);
    }

    public boolean hasNext() {
        return this._hasNext;
    }

    public boolean isOpen() {
        return _isOpen;
    }

    public IteratorMetadata getMetadata() {
        return metadata;
    }

    public abstract boolean isRDD();

    public abstract JavaRDD<Item> getRDD(DynamicContext dynamicContext);

    public abstract boolean isDataFrame();

    public abstract boolean getDataFrame();

    public abstract Item next();

    protected List<Item> runChildrenIterators(DynamicContext context) {
        List<Item> values = new ArrayList<>();
        for (RuntimeIterator iterator : this._children) {
            iterator.open(context);
            while (iterator.hasNext())
                values.add(iterator.next());
            iterator.close();
        }
        return values;
    }

    protected List<Item> getItemsFromIteratorWithCurrentContext(RuntimeIterator iterator) {
        List<Item> result = new ArrayList<>();
        iterator.open(_currentDynamicContext);
        while (iterator.hasNext())
            result.add(iterator.next());
        iterator.close();
        return result;
    }

    protected <T extends Item> T getSingleItemOfTypeFromIterator(RuntimeIterator iterator, Class<T> type) {
        return getSingleItemOfTypeFromIterator(iterator, type,
                new SparksoniqRuntimeException("Iterator was expected to return a single item but returned a sequence",
                        iterator.getMetadata().getExpressionMetadata()));
    }

    protected <T extends Item, E extends SparksoniqRuntimeException> T getSingleItemOfTypeFromIterator(RuntimeIterator iterator,
                                                                                                       Class<T> type, E nonAtomicException) {
        iterator.open(_currentDynamicContext);
        Item result = null;
        if (iterator.hasNext()) {
            result = iterator.next();
            if (iterator.hasNext()) {
                throw nonAtomicException;
            }
        }
        iterator.close();
        if (result != null && !(type.isInstance(result)))
            throw new UnexpectedTypeException("Invalid item type returned by iterator", iterator.getMetadata());
        return (T) result;
    }
    
    public Map<String, DynamicContext.VariableDependency> getVariableDependencies()
    {
        Map<String, DynamicContext.VariableDependency> result = new TreeMap<String, DynamicContext.VariableDependency>();
        for(RuntimeIterator iterator : _children)
        {
            result.putAll(iterator.getVariableDependencies());
        }
        return result;
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
        buffer.append("\n");
        for (RuntimeIterator iterator : this._children) {
            iterator.print(buffer, indent + 1);
        }
    }
}
