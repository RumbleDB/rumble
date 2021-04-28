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

package org.rumbledb.runtime.primary;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VariableReferenceIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private SequenceType sequence;
    private Name variableName;
    private List<Item> items = null;
    private int currentIndex = 0;

    public VariableReferenceIterator(
            Name variableName,
            SequenceType seq,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.variableName = variableName;
        this.sequence = seq;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return context.getVariableValues().getRDDVariableValue(this.variableName, getMetadata());
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        return context.getVariableValues().getDataFrameVariableValue(this.variableName, getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        String name = this.variableName.toString();
        DataType schema = nativeClauseContext.getSchema();
        if (!(schema instanceof StructType)) {
            System.err.println("Variable " + name + ": failed because schema is not a struct.");
            return NativeClauseContext.NoNativeQuery;
        }
        // check if name is in the schema
        StructType structSchema = (StructType) schema;
        if (!FlworDataFrameUtils.hasColumnForVariable(structSchema, this.variableName)) {
            List<Item> items = nativeClauseContext.getContext()
                .getVariableValues()
                .getLocalVariableValue(this.variableName, getMetadata());
            if (items.size() != 1) {
                // only possible to turn into native, sequence of length 1
                System.err.println(
                    "Variable " + name + ": failed because value in dynamic context is not exactly one item"
                );
                return NativeClauseContext.NoNativeQuery;
            }
            System.err.println("Variable " + name + ": " + items.get(0).toString());
            return items.get(0).generateNativeQuery(nativeClauseContext);
        }
        if (!FlworDataFrameUtils.isVariableAvailableAsNativeItem(structSchema, this.variableName)) {
            structSchema.printTreeString();
            System.err.println("Variable " + name + ": failed because value in input tuple is not available natively");
            return NativeClauseContext.NoNativeQuery;
        }
        String escapedName = name.replace("`", FlworDataFrameUtils.backtickEscape);
        StructField field = structSchema.fields()[structSchema.fieldIndex(escapedName)];
        DataType fieldType = field.dataType();
        ItemType variableType = FlworDataFrameUtils.mapToJsoniqType(fieldType);
        System.err.println("Variable " + name + ": type " + variableType);
        NativeClauseContext newContext = new NativeClauseContext(
                nativeClauseContext,
                "`" + escapedName + "`",
                variableType
        );
        newContext.setSchema(fieldType);
        return newContext;
    }

    @Override
    public Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this.variableName,
                    getMetadata()
            );
        }
        Item item = this.items.get(this.currentIndex);
        this.currentIndex++;
        if (this.currentIndex == this.items.size()) {
            this.hasNext = false;
        }
        return item;
    }

    @Override
    public void openLocal() {
        this.currentIndex = 0;
        this.items = this.currentDynamicContextForLocalExecution.getVariableValues()
            .getLocalVariableValue(
                this.variableName,
                getMetadata()
            );
        this.hasNext = this.items.size() != 0;
    }

    @Override
    protected void closeLocal() {
        // do nothing
    }

    @Override
    public void resetLocal() {
        this.currentIndex = 0;
        this.items = null;
    }

    public SequenceType getSequence() {
        return this.sequence;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        result.put(this.variableName, DynamicContext.VariableDependency.FULL);
        return result;
    }
}
