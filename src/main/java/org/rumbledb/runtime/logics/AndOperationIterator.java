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

package org.rumbledb.runtime.logics;

import java.io.Serial;
import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class AndOperationIterator extends AbstractAtMostOneItemRuntimePlan implements NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan leftIterator;
    private final ItemRuntimePlan rightIterator;

    public AndOperationIterator(
            ItemRuntimePlan leftIterator,
            ItemRuntimePlan rightIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
    }

    public ItemRuntimePlan getLeftIterator() {
        return this.leftIterator;
    }

    public ItemRuntimePlan getRightIterator() {
        return this.rightIterator;
    }

    @Override
    public Item evaluateAtMostOne(
            DynamicContext dynamicContext
    ) {
        boolean leftEffectiveBooleanValue = EffectiveBooleanValue.evaluate(
            this.leftIterator,
            dynamicContext
        );
        boolean rightEffectiveBooleanValue = EffectiveBooleanValue.evaluate(
            this.rightIterator,
            dynamicContext
        );

        return ItemFactory.getInstance()
            .createBooleanItem((leftEffectiveBooleanValue && rightEffectiveBooleanValue));
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext leftResult = NativeQueryRuntimePlan.generate(
            this.leftIterator,
            nativeClauseContext
        );
        if (leftResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext rightResult = NativeQueryRuntimePlan.generate(
            this.rightIterator,
            new NativeClauseContext(leftResult, null, null)
        );
        if (rightResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (
            SequenceType.Arity.OneOrMore.isSubtypeOf(leftResult.getResultingType().getArity())
                ||
                SequenceType.Arity.OneOrMore.isSubtypeOf(rightResult.getResultingType().getArity())
        ) {
            return NativeClauseContext.NoNativeQuery;
        }
        SequenceType.Arity resultingArity = (leftResult.getResultingType().getArity() == SequenceType.Arity.One
            && rightResult.getResultingType().getArity() == SequenceType.Arity.One)
                ? SequenceType.Arity.One
                : SequenceType.Arity.OneOrZero;
        String resultingQuery = "( "
            + leftResult.getResultingQuery()
            + " AND "
            + rightResult.getResultingQuery()
            + " )";
        return new NativeClauseContext(
                rightResult,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.booleanItem, resultingArity)
        );
    }
}
