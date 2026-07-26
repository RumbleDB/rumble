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
 * Authors: Stefan Irimescu, Can Berker Cikis, Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.runtime;

import java.io.Serial;
import java.util.*;

import lombok.NonNull;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.typing.TypeInferrenceUtils;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;


public abstract class RuntimeIterator implements RuntimeIteratorInterface<Item>, RuntimePlan<Item> {

    protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    @Serial
    private static final long serialVersionUID = 1L;
    protected transient boolean hasNext;
    protected transient boolean isOpen;
    private final List<RuntimeIterator> children;
    protected transient DynamicContext currentDynamicContextForLocalExecution;
    protected RuntimeStaticContext staticContext;

    protected RuntimeIterator(List<RuntimeIterator> children, @NonNull RuntimeStaticContext staticContext) {
        this.staticContext = staticContext;
        if (this.staticContext.getStaticType() == null) {
            throw new OurBadException(
                    "Runtime iterator created without a static type! " + this.getClass().getCanonicalName()
            );
        }
        this.isOpen = false;
        this.children = List.copyOf(Objects.requireNonNullElse(children, Collections.emptyList()));
    }

    @Override
    public abstract LocalCursor<Item> createLocalCursor(DynamicContext context);

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
        try {
            open(dynamicContext);
            return EffectiveBooleanValue.evaluateOpenSequence(
                this::hasNext,
                this::next,
                this.staticContext,
                position
            );
        } finally {
            this.close();
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

    @Override
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

    @Override
    public void close() {
        this.isOpen = false;
    }



    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    protected final RuntimeIterator getChild(int index) {
        return this.children.get(index);
    }

    protected final List<RuntimeIterator> getChildren() {
        return this.children;
    }

    public ExceptionMetadata getMetadata() {
        return this.staticContext.getMetadata();
    }

    public ExecutionMode getHighestExecutionMode() {
        return this.staticContext.getExecutionMode();
    }

    public SequenceType getStaticType() {
        return this.staticContext.getStaticType();
    }

    public RumbleRuntimeConfiguration getConfiguration() {
        return this.staticContext.getConfiguration();
    }

    public RuntimeStaticContext getRuntimeStaticContext() {
        return this.staticContext;
    }

    public boolean isRDDOrDataFrame() {
        if (this.staticContext.getExecutionMode() == ExecutionMode.UNSET) {
            throw new OurBadException("isRDDorDataFrame field in iterator without execution mode being set.");
        }
        return this.staticContext.getExecutionMode().isRDDOrDataFrame();
    }

    public boolean isRDD() {
        if (this.staticContext.getExecutionMode() == ExecutionMode.UNSET) {
            throw new OurBadException("isRDD field in iterator without execution mode being set.");
        }
        return this.staticContext.getExecutionMode().isRDD();
    }

    public boolean isLocal() {
        if (this.staticContext.getExecutionMode() == ExecutionMode.UNSET) {
            throw new OurBadException("isLocal field in iterator without execution mode being set.");
        }
        return this.staticContext.getExecutionMode().isLocal();
    }

    public JavaRDD<Item> getRDD(DynamicContext context) {
        throw new OurBadException(
                "RDDs are not implemented for the iterator " + getClass().getCanonicalName(),
                getMetadata()
        );
    }

    /**
     * Checks whether this iterator natively produces DataFrames.
     * 
     * @return true if it does, false otherwise.
     */
    public boolean isDataFrame() {
        if (this.staticContext.getExecutionMode() == ExecutionMode.UNSET) {
            throw new OurBadException("isDataFrame accessed in iterator without execution mode being set.");
        }
        return this.staticContext.getExecutionMode().isDataFrame();
    }

    /**
     * Checks whether this iterator can produce valid DataFrames with no error (natively or not).
     * 
     * @return true if it can, false otherwise.
     */
    public boolean canProduceDataFrame() {
        return isDataFrame()
            || this.getStaticType().getItemType().isCompatibleWithDataFrames(this.getConfiguration());
    }

    public JSoundDataFrame getDataFrame(DynamicContext context) {
        throw new OurBadException(
                "DataFrames are not implemented for the iterator " + getClass().getCanonicalName(),
                getMetadata()
        );
    }

    /**
     * Gets the output as a DataFrame. If necessary and possible, forcibly converts the items to a DataFrame.
     * 
     * @return the DataFrame.
     */
    public final JSoundDataFrame getOrCreateDataFrame(DynamicContext context) {
        if (isDataFrame()) {
            return this.getDataFrame(context);
        }
        if (isRDD()) {
            if (this.getStaticType().getItemType().isCompatibleWithDataFrames(this.getConfiguration())) {
                return ValidateTypeIterator.convertRDDToValidDataFrame(
                    this.getRDD(context),
                    this.getStaticType().getItemType(),
                    context,
                    true,
                    this.staticContext
                );
            } else {
                JavaRDD<Item> rdd = this.getRDD(context);
                ItemType type = TypeInferrenceUtils.inferItemTypeOfRDDItems(
                    rdd,
                    getMetadata(),
                    TypeInferrenceUtils.TypeMergeMode.LAX
                );
                return ValidateTypeIterator.convertRDDToValidDataFrame(
                    rdd,
                    type,
                    context,
                    true,
                    this.staticContext
                );
            }
        }
        List<Item> items = LocalCursorUtils.materialize(this, context);
        if (this.getStaticType().getItemType().isCompatibleWithDataFrames(this.getConfiguration())) {
            return ValidateTypeIterator.convertLocalItemsToDataFrame(
                items,
                this.getStaticType().getItemType(),
                context,
                true,
                this.staticContext
            );
        } else {
            ItemType type = TypeInferrenceUtils.inferItemTypeOfLocalItems(
                items,
                getMetadata(),
                TypeInferrenceUtils.TypeMergeMode.LAX
            );
            if (this.getConfiguration().printInferredTypes()) {
                System.err.println("Inferred DataFrame type:\n" + this.getStaticType().getItemType());
            }
            return ValidateTypeIterator.convertLocalItemsToDataFrame(
                items,
                type,
                context,
                true,
                this.staticContext
            );
        }
    }

    public boolean isUpdating() {
        return this.staticContext.isUpdating();
    }

    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        throw new OurBadException(
                "Pending Update Lists are not implemented for the iterator " + getClass().getCanonicalName(),
                getMetadata()
        );
    }

    public boolean isSequential() {
        return this.staticContext.isSequential();
    }

    @Override
    public abstract Item next();

    public List<Item> materialize(DynamicContext context) {
        return LocalCursorUtils.materialize(this, context);
    }

    public Item materializeFirstItemOrNull(
            DynamicContext context
    ) {
        return LocalCursorUtils.materializeFirst(this, context);
    }

    public Item materializeExactlyOneItem(
            DynamicContext context
    )
            throws NoItemException,
                MoreThanOneItemException {
        Item result = LocalCursorUtils.materializeAtMostOne(this, context);
        if (result == null) {
            throw new NoItemException();
        }
        return result;
    }

    public Item materializeAtMostOneItemOrNull(
            DynamicContext context
    )
            throws MoreThanOneItemException {
        return LocalCursorUtils.materializeAtMostOne(this, context);
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<>();
        for (RuntimeIterator iterator : this.children) {
            DynamicContext.mergeVariableDependencies(result, iterator.getVariableDependencies());
        }
        return result;
    }

    public void printToStandardError() {
        StringBuilder sb = new StringBuilder();
        this.print(sb, 0);
        System.err.println(sb);
    }

    public void print(StringBuilder buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | ");
        buffer.append(this.staticContext.getExecutionMode());
        buffer.append(" | ");
        buffer.append(getStaticType());
        buffer.append(" | ");
        buffer.append(this.isUpdating() ? "updating" : "simple");
        buffer.append(" | ");
        buffer.append(this.isSequential() ? "sequential" : "non-sequential");
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
        switch (this.staticContext.getExecutionMode()) {
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
