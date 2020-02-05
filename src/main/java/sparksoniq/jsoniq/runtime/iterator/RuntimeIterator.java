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

package sparksoniq.jsoniq.runtime.iterator;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class RuntimeIterator implements RuntimeIteratorInterface, KryoSerializable {

    protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    private static final long serialVersionUID = 1L;
    protected transient boolean _hasNext;
    protected transient boolean _isOpen;
    protected List<RuntimeIterator> _children;
    protected transient DynamicContext _currentDynamicContextForLocalExecution;
    private IteratorMetadata metadata;

    protected ExecutionMode _highestExecutionMode;

    protected RuntimeIterator(List<RuntimeIterator> children, ExecutionMode executionMode, IteratorMetadata metadata) {
        this.metadata = metadata;
        this._isOpen = false;
        this._highestExecutionMode = executionMode;
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
     * If the sequence is a single numeric item and a non-null position is supplied, then instead
     * it is checked whether the numeric item is equal to the position.
     * 
     * @param iterator has to be opened before calling this function
     * @param position the context position, or null if none
     * @return
     */
    public static boolean getEffectiveBooleanValueOrCheckPosition(RuntimeIterator iterator, Item position) {
        if (iterator.hasNext()) {
            Item item = iterator.next();
            boolean result;
            if (item.isBoolean())
                result = item.getBooleanValue();
            else if (item.isNumeric()) {
                if (position == null) {
                    if (item.isInteger())
                        result = item.getIntegerValue() != 0;
                    else if (item.isDouble())
                        result = item.getDoubleValue() != 0;
                    else if (item.isDecimal())
                        result = !item.getDecimalValue().equals(BigDecimal.ZERO);
                    else {
                        throw new OurBadException(
                                "Unexpected numeric type found while calculating effective boolean value."
                        );
                    }
                } else {
                    result = item.equals(position);
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
                throw new InvalidArgumentTypeException(
                        "Effective boolean value not defined for items of type "
                            +
                            ItemTypes.getItemTypeName(item.getClass().getSimpleName()),
                        iterator.getMetadata()
                );
            }

            if (iterator.hasNext()) {
                throw new InvalidArgumentTypeException(
                        "Effective boolean value not defined for sequences of more than one atomic item. "
                            + "Sequence containing: "
                            + item.serialize()
                            + " must be a singleton.",
                        iterator.getMetadata()
                );
            }

            return result;
        } else {
            return false;
        }

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
        return getEffectiveBooleanValueOrCheckPosition(iterator, null);
    }

    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._hasNext = true;
        this._currentDynamicContextForLocalExecution = context;
    }

    public void close() {
        this._isOpen = false;
        this._children.forEach(c -> c.close());
    }

    public void reset(DynamicContext context) {
        this._hasNext = true;
        this._currentDynamicContextForLocalExecution = context;
        this._children.forEach(c -> c.reset(context));
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this._children);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this._hasNext = false;
        this._isOpen = false;
        this._currentDynamicContextForLocalExecution = null;
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

    public ExecutionMode getHighestExecutionMode() {
        return _highestExecutionMode;
    }

    public boolean isRDD() {
        if (_highestExecutionMode == ExecutionMode.UNSET) {
            throw new OurBadException("isRDD field in iterator without execution mode being set.");
        }
        return _highestExecutionMode.isRDD();
    }

    public JavaRDD<Item> getRDD(DynamicContext context) {
        throw new OurBadException("RDDs are not implemented for the iterator", getMetadata());
    }

    public boolean isDataFrame() {
        if (_highestExecutionMode == ExecutionMode.UNSET) {
            throw new OurBadException("isDataFrame accessed in iterator without execution mode being set.");
        }
        return _highestExecutionMode.isDataFrame();
    }

    public Dataset<Row> getDataFrame(DynamicContext context) {
        throw new OurBadException("DataFrames are not implemented for the iterator", getMetadata());
    }

    public abstract Item next();

    public List<Item> materialize(DynamicContext context) {
        List<Item> result = new ArrayList<>();
        this.open(context);
        while (this.hasNext())
            result.add(this.next());
        this.close();
        return result;
    }

    public Item materializeFirstItemOrNull(
            DynamicContext context
    ) {
        this.open(context);
        Item result = this.hasNext() ? this.next() : null;
        this.close();
        return result;
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<>();
        for (RuntimeIterator iterator : _children) {
            DynamicContext.mergeVariableDependencies(result, iterator.getVariableDependencies());
        }
        return result;
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
            buffer.append(v).append("(").append(dependencies.get(v)).append(")").append(" ");
        }
        buffer.append("\n");
        for (RuntimeIterator iterator : this._children) {
            iterator.print(buffer, indent + 1);
        }
    }
}
