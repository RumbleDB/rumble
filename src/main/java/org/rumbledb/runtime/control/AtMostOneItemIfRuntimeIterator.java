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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class AtMostOneItemIfRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public AtMostOneItemIfRuntimeIterator(
            RuntimeIterator condition,
            RuntimeIterator branch,
            RuntimeIterator elseBranch,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(condition, branch, elseBranch), staticContext);
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
        if (conditionResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext thenResult = this.children.get(1)
            .generateNativeQuery(new NativeClauseContext(conditionResult, null, null));
        if (thenResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext elseResult = this.children.get(2)
            .generateNativeQuery(new NativeClauseContext(thenResult, null, null));
        if (elseResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!conditionResult.getResultingType().getItemType().equals(BuiltinTypesCatalogue.booleanItem)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!thenResult.getResultingType().getItemType().isSubtypeOf(BuiltinTypesCatalogue.numericItem)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!elseResult.getResultingType().getItemType().isSubtypeOf(BuiltinTypesCatalogue.numericItem)) {
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
        SequenceType.Arity resultingArity = (thenResult.getResultingType().getArity() == SequenceType.Arity.One
            && elseResult.getResultingType().getArity() == SequenceType.Arity.One)
                ? SequenceType.Arity.One
                : SequenceType.Arity.OneOrZero;
        return new NativeClauseContext(
                elseResult,
                resultingQuery,
                new SequenceType(
                        thenResult.getResultingType()
                            .getItemType()
                            .findLeastCommonSuperTypeWith(elseResult.getResultingType().getItemType()),
                        resultingArity
                )
        );
    }
}
