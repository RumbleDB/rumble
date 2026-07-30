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

import java.io.Serial;
import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class AtMostOneItemIfRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public AtMostOneItemIfRuntimeIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> condition,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> branch,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> elseBranch,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(condition, branch, elseBranch), staticContext);
    }

    @Override
    public Item evaluateAtMostOne(
            DynamicContext dynamicContext
    ) {
        org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> condition = this.getChild(0);
        boolean effectiveBooleanValue = org.rumbledb.runtime.EffectiveBooleanValue.evaluate(condition, dynamicContext);

        if (effectiveBooleanValue) {
            return this.getChild(1).materializeFirstOrNull(dynamicContext);
        } else {
            return this.getChild(2).materializeFirstOrNull(dynamicContext);
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext conditionResult = org.rumbledb.runtime.plan.NativeQueryRuntimePlan.generate(
            this.getChild(0),
            nativeClauseContext
        );
        if (conditionResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext thenResult = org.rumbledb.runtime.plan.NativeQueryRuntimePlan.generate(
            this.getChild(1),
            new NativeClauseContext(conditionResult, null, null)
        );
        if (thenResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext elseResult = org.rumbledb.runtime.plan.NativeQueryRuntimePlan.generate(
            this.getChild(2),
            new NativeClauseContext(thenResult, null, null)
        );
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
