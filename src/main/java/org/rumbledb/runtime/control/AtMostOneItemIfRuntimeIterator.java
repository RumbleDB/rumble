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

package org.rumbledb.runtime.control;

import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class AtMostOneItemIfRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public AtMostOneItemIfRuntimeIterator(
            RuntimeIterator condition,
            RuntimeIterator branch,
            RuntimeIterator elseBranch,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(condition, branch, elseBranch), executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        RuntimeIterator condition = this.children.get(0);
        boolean effectiveBooleanValue = condition.getEffectiveBooleanValue(dynamicContext);

        if (effectiveBooleanValue) {
            return this.children.get(1).materializeFirstItemOrNull(dynamicContext);
        } else {
            return this.children.get(2).materializeFirstItemOrNull(dynamicContext);
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext conditionResult = this.children.get(0).generateNativeQuery(nativeClauseContext);
        NativeClauseContext thenResult = this.children.get(1).generateNativeQuery(nativeClauseContext);
        NativeClauseContext elseResult = this.children.get(2).generateNativeQuery(nativeClauseContext);
        if (
            conditionResult == NativeClauseContext.NoNativeQuery
                || thenResult == NativeClauseContext.NoNativeQuery
                || elseResult == NativeClauseContext.NoNativeQuery
        ) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!conditionResult.getResultingType().getItemType().equals(BuiltinTypesCatalogue.booleanItem)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!thenResult.getResultingType().getItemType().equals(BuiltinTypesCatalogue.floatItem)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!elseResult.getResultingType().getItemType().equals(BuiltinTypesCatalogue.floatItem)) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "( "
            + "IF( "
            + conditionResult.getResultingQuery()
            + ", "
            + thenResult.getResultingQuery()
            + ", "
            + elseResult.getResultingQuery()
            + " ) )";
        return new NativeClauseContext(
                nativeClauseContext,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.floatItem, SequenceType.Arity.One)
        );
    }
}
