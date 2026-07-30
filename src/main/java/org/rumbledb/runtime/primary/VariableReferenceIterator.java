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

import org.rumbledb.runtime.HybridRuntimeIterator;

import lombok.Getter;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.types.*;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;

import lombok.NonNull;
import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VariableReferenceIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item> {


    @Serial
    private static final long serialVersionUID = 1L;
    @Getter
    private final Name variableName;
    private List<Item> items = null;
    private int currentIndex = 0;

    public VariableReferenceIterator(
            Name variableName,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.variableName = variableName;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(this.variableName, context, getMetadata());
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return context.getVariableValues().getRDDVariableValue(this.variableName, getMetadata());
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext context) {
        return context.getVariableValues().getDataFrameVariableValue(this.variableName, getMetadata());
    }


    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        Name name = nativeClauseContext.getVariable(this.variableName);
        DataType schema = nativeClauseContext.getSchema();
        if (!(schema instanceof StructType structSchema)) {
            return NativeClauseContext.NoNativeQuery;
        }
        // check if name is in the schema
        if (!FlworDataFrameUtils.hasColumnForVariable(structSchema, name)) {
            List<Item> items = nativeClauseContext.getContext()
                .getVariableValues()
                .getLocalVariableValue(this.variableName, getMetadata());
            if (items.size() != 1) {
                // only possible to turn into native, sequence of length 1
                return NativeClauseContext.NoNativeQuery;
            }
            return items.get(0).generateNativeQuery(nativeClauseContext);
        }
        String escapedName = name.toString().replace("`", FlworDataFrameUtils.backtickEscape);
        SequenceType.Arity arity;
        if (FlworDataFrameUtils.isVariableAvailableAsNativeSequence(structSchema, name)) {
            escapedName = escapedName + ".sequence";
            arity = SequenceType.Arity.ZeroOrMore;
        } else if (FlworDataFrameUtils.isVariableAvailableAsCountOnly(structSchema, name)) {
            escapedName = escapedName + ".count";
            arity = SequenceType.Arity.One;
        } else if (!FlworDataFrameUtils.isVariableAvailableAsNativeItem(structSchema, name)) {
            return NativeClauseContext.NoNativeQuery;
        } else {
            arity = SequenceType.Arity.OneOrZero;
        }
        StructField field = structSchema.fields()[structSchema.fieldIndex(escapedName)];
        DataType fieldType = field.dataType();
        ItemType variableType = TypeMappings.getItemTypeFromDataFrameDataType(fieldType);
        if (arity == SequenceType.Arity.ZeroOrMore && fieldType instanceof ArrayType arrayType) {
            if (arrayType.elementType().equals(DataTypes.BinaryType)) {
                return NativeClauseContext.NoNativeQuery;
            }
            variableType = variableType.getArrayContentFacet();
        }
        NativeClauseContext newContext = new NativeClauseContext(
                nativeClauseContext,
                "`" + escapedName + "`",
                new SequenceType(variableType, arity)
        );
        newContext.setSchema(fieldType);
        return newContext;
    }



    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        result.put(this.variableName, DynamicContext.VariableDependency.FULL);
        return result;
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final Name variableName;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private List<Item> items;
        private int currentIndex;

        private EvaluationCursor(
                @NonNull Name variableName,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.variableName = variableName;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.items = this.context.getVariableValues()
                .getLocalVariableValue(this.variableName, this.metadata);
            this.currentIndex = 0;
        }

        @Override
        protected boolean hasNextLocal() {
            return this.currentIndex < this.items.size();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw new IteratorFlowException(
                        RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this.variableName,
                        this.metadata
                );
            }
            return this.items.get(this.currentIndex++);
        }

        @Override
        protected void closeLocal() {
            this.items = null;
            this.currentIndex = 0;
        }

    }
}
