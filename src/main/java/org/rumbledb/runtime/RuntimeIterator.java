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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public abstract class RuntimeIterator implements RuntimeIteratorInterface, KryoSerializable {

    protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    private static final long serialVersionUID = 1L;
    protected transient boolean hasNext;
    protected transient boolean isOpen;
    protected List<RuntimeIterator> children;
    protected transient DynamicContext currentDynamicContextForLocalExecution;
    private ExceptionMetadata metadata;
    protected URI staticURI;
    // private StaticContext staticContext;

    protected ExecutionMode highestExecutionMode;

    protected RuntimeIterator(List<RuntimeIterator> children, ExecutionMode executionMode, ExceptionMetadata metadata) {
        this.metadata = metadata;
        this.isOpen = false;
        this.highestExecutionMode = executionMode;
        this.children = new ArrayList<>();
        if (children != null && !children.isEmpty()) {
            this.children.addAll(children);
        }
    }

    // For performance reasons, and as only the static URI is really needed at the moment, we only store it.
    // This avoids the deserialization of many static context copies at runtime.
    public void setStaticContext(StaticContext staticContext) {
        if (this.staticURI != null) {
            throw new OurBadException("Static context already consumed.");
        }
        this.staticURI = staticContext.getStaticBaseURI();
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
     * @param dynamicContext the dynamic context
     * @param position the context position, or null if none
     * @return the effective boolean value.
     */
    public boolean getEffectiveBooleanValueOrCheckPosition(DynamicContext dynamicContext, Item position) {
        open(dynamicContext);
        if (hasNext()) {
            Item item = this.next();
            boolean result;
            if (item.isBoolean()) {
                result = item.getBooleanValue();
            } else if (item.isNumeric()) {
                if (position == null) {
                    if (item.isInt()) {
                        result = item.getIntValue() != 0;
                    } else if (item.isInteger()) {
                        result = !item.getIntegerValue().equals(BigInteger.ZERO);
                    } else if (item.isDouble()) {
                        result = item.getDoubleValue() != 0;
                    } else if (item.isFloat()) {
                        result = item.getFloatValue() != 0;
                    } else if (item.isDecimal()) {
                        result = !item.getDecimalValue().equals(BigDecimal.ZERO);
                    } else {
                        throw new OurBadException(
                                "Unexpected numeric type found while calculating effective boolean value."
                        );
                    }
                } else {
                    result = ComparisonIterator.compareItems(
                        item,
                        position,
                        ComparisonOperator.VC_EQ,
                        getMetadata()
                    ) == 0;
                }
            } else if (item.isNull()) {
                result = false;
            } else if (item.getDynamicType().canBePromotedTo(BuiltinTypesCatalogue.stringItem)) {
                result = !item.getStringValue().isEmpty();
            } else if (item.isObject()) {
                this.close();
                return true;
            } else if (item.isArray()) {
                this.close();
                return true;
            } else {
                throw new InvalidArgumentTypeException(
                        "Effective boolean value not defined for items of type "
                            +
                            item.getDynamicType().toString(),
                        getMetadata()
                );
            }

            if (hasNext()) {
                throw new InvalidArgumentTypeException(
                        "Effective boolean value not defined for sequences of more than one atomic item. "
                            + "Sequence containing: "
                            + item.serialize()
                            + " must be a singleton.",
                        getMetadata()
                );
            }

            this.close();
            return result;
        } else {
            this.close();
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
     * @param dynamicContext the dynamic context
     * @return the effective boolean value.
     */
    public boolean getEffectiveBooleanValue(DynamicContext dynamicContext) {
        return this.getEffectiveBooleanValueOrCheckPosition(dynamicContext, null);
    }

    public void open(DynamicContext context) {
        if (context == null) {
            throw new IteratorFlowException(
                    "No dynamic context was provided when opening an interator.",
                    getMetadata()
            );
        }
        if (this.isOpen) {
            throw new IteratorFlowException("Runtime iterator cannot be opened twice.", getMetadata());
        }
        this.isOpen = true;
        this.hasNext = true;
        this.currentDynamicContextForLocalExecution = context;
    }

    public void close() {
        this.isOpen = false;
    }

    public void reset(DynamicContext context) {
        this.hasNext = true;
        this.currentDynamicContextForLocalExecution = context;
        this.children.forEach(c -> c.reset(context));
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.children);
        // TODO serializer other fields
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.hasNext = false;
        this.isOpen = false;
        this.currentDynamicContextForLocalExecution = null;
        this.children = kryo.readObject(input, ArrayList.class);
        // TODO serializer other fields
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    public ExecutionMode getHighestExecutionMode() {
        return this.highestExecutionMode;
    }

    public boolean isRDDOrDataFrame() {
        if (this.highestExecutionMode == ExecutionMode.UNSET) {
            throw new OurBadException("isRDD field in iterator without execution mode being set.");
        }
        return this.highestExecutionMode.isRDDOrDataFrame();
    }

    public JavaRDD<Item> getRDD(DynamicContext context) {
        throw new OurBadException(
                "RDDs are not implemented for the iterator " + getClass().getCanonicalName(),
                getMetadata()
        );
    }

    public boolean isDataFrame() {
        if (this.highestExecutionMode == ExecutionMode.UNSET) {
            throw new OurBadException("isDataFrame accessed in iterator without execution mode being set.");
        }
        return this.highestExecutionMode.isDataFrame();
    }

    public JSoundDataFrame getDataFrame(DynamicContext context) {
        throw new OurBadException(
                "DataFrames are not implemented for the iterator " + getClass().getCanonicalName(),
                getMetadata()
        );
    }

    public abstract Item next();

    public List<Item> materialize(DynamicContext context) {
        List<Item> result = new ArrayList<>();
        this.open(context);
        while (this.hasNext()) {
            result.add(this.next());
        }
        this.close();
        return result;
    }

    public void materialize(DynamicContext context, List<Item> result) {
        result.clear();
        this.open(context);
        while (this.hasNext()) {
            result.add(this.next());
        }
        this.close();
    }

    public void materializeNFirstItems(DynamicContext context, List<Item> result, int n) {
        result.clear();
        this.open(context);
        int i = 0;
        while (this.hasNext() && i < n) {
            result.add(this.next());
            ++i;
        }
        this.close();
    }

    public Item materializeFirstItemOrNull(
            DynamicContext context
    ) {
        this.open(context);
        Item result = this.hasNext() ? this.next() : null;
        this.close();
        return result;
    }

    public Item materializeExactlyOneItem(
            DynamicContext context
    )
            throws NoItemException,
                MoreThanOneItemException {
        this.open(context);
        if (!this.hasNext()) {
            throw new NoItemException();
        }
        Item result = this.next();
        if (this.hasNext()) {
            throw new MoreThanOneItemException();
        }
        this.close();
        return result;
    }

    public Item materializeAtMostOneItemOrNull(
            DynamicContext context
    )
            throws MoreThanOneItemException {
        this.open(context);
        if (!this.hasNext()) {
            this.close();
            return null;
        }
        Item result = this.next();
        if (this.hasNext()) {
            throw new MoreThanOneItemException();
        }
        this.close();
        return result;
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<>();
        for (RuntimeIterator iterator : this.children) {
            DynamicContext.mergeVariableDependencies(result, iterator.getVariableDependencies());
        }
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | ");
        buffer.append(this.highestExecutionMode);
        buffer.append(" | ");

        buffer.append("Variable dependencies: ");
        Map<Name, DynamicContext.VariableDependency> dependencies = getVariableDependencies();
        for (Name v : dependencies.keySet()) {
            buffer.append(v).append("(").append(dependencies.get(v)).append(")").append(" ");
        }
        buffer.append("\n");
        for (RuntimeIterator iterator : this.children) {
            iterator.print(buffer, indent + 1);
        }
    }

    public void bindToVariableInDynamicContext(
            DynamicContext targetContext,
            Name variable,
            DynamicContext executionContext
    ) {
        if (this.isDataFrame()) {
            targetContext.getVariableValues().addVariableValue(variable, this.getDataFrame(executionContext));
        } else if (this.isRDDOrDataFrame()) {
            targetContext.getVariableValues().addVariableValue(variable, this.getRDD(executionContext));
        } else {
            targetContext.getVariableValues().addVariableValue(variable, this.materialize(executionContext));
        }
    }

    public RuntimeIterator deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            byte[] data = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (RuntimeIterator) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            RumbleException rumbleException = new OurBadException(
                    "Error while deep copying the function body runtimeIterator"
            );
            rumbleException.initCause(e);
            throw rumbleException;
        }
    }

    /**
     * This function generate (if possible) a native spark-sql query that maps the inner working of the iterator
     *
     * @return a native clause context with the spark-sql native query to get an equivalent result of the iterator, or
     *         [NativeClauseContext.NoNativeQuery] if
     *         it is not possible
     * @param nativeClauseContext context information to generate the native query
     */
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }

    /**
     * Says whether this expression evaluation triggers a Spark job.
     *
     * @return true if the execution triggers a Spark, false otherwise, null if undetermined yet.
     */
    public boolean isSparkJobNeeded() {
        for (RuntimeIterator n : this.children) {
            if (n.isSparkJobNeeded()) {
                return true;
            }
        }
        switch (this.highestExecutionMode) {
            case DATAFRAME:
                return true;
            case LOCAL:
                return false;
            case RDD:
                return true;
            case UNSET:
                return false;
            default:
                return false;
        }
    }
}
