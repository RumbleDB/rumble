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

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeMappings;

import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ContextExpressionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public ContextExpressionIterator(RuntimeStaticContext staticContext) {
        super(null, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        return getContextItem(dynamicContext);
    }

    private Item getContextItem(DynamicContext dynamicContext) {
        List<Item> items = dynamicContext.getVariableValues()
            .getLocalVariableValue(
                Name.CONTEXT_ITEM,
                getMetadata()
            );
        if (items.isEmpty()) {
            throw new UnexpectedTypeException("The context item cannot be an empty sequence.", getMetadata());
        }
        return items.get(0);
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        result.put(Name.CONTEXT_ITEM, DynamicContext.VariableDependency.FULL);
        return result;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        DataType schema = nativeClauseContext.getSchema();
        if (!(schema instanceof StructType structSchema)) {
            return NativeClauseContext.NoNativeQuery;
        }
        // check if name is in the schema
        if (!FlworDataFrameUtils.hasColumnForVariable(structSchema, Name.CONTEXT_ITEM)) {
            return getContextItem(nativeClauseContext.getContext()).generateNativeQuery(nativeClauseContext);
        }
        if (!FlworDataFrameUtils.isVariableAvailableAsNativeItem(structSchema, Name.CONTEXT_ITEM)) {
            return NativeClauseContext.NoNativeQuery;
        }
        StructField field = structSchema.fields()[structSchema.fieldIndex(
            SparkSessionManager.nonObjectJSONiqItemColumnName
        )];
        DataType fieldType = field.dataType();
        ItemType variableType = TypeMappings.getItemTypeFromDataFrameDataType(fieldType);
        return new NativeClauseContext(
                nativeClauseContext,
                "`" + SparkSessionManager.nonObjectJSONiqItemColumnName + "`",
                new SequenceType(variableType, SequenceType.Arity.One)
        );
    }
}
