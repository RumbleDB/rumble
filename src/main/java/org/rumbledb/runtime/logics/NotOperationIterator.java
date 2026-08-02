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

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import java.io.Serial;
import java.util.Collections;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class NotOperationIterator extends AbstractAtMostOneItemRuntimePlan implements NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan child;

    public NotOperationIterator(
            ItemRuntimePlan child,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(child), staticContext);
        this.child = child;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        boolean effectiveBooleanValue = EffectiveBooleanValue.evaluate(this.child, dynamicContext);
        return ItemFactory.getInstance().createBooleanItem(!(effectiveBooleanValue));
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext childResult = NativeQueryRuntimePlan.generate(
            this.child,
            nativeClauseContext
        );
        if (childResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(childResult.getResultingType().getArity())) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "( NOT " + childResult.getResultingQuery() + " )";
        return new NativeClauseContext(
                nativeClauseContext,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.booleanItem, childResult.getResultingType().getArity())
        );
    }
}
