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

package org.rumbledb.runtime.functions.numerics.trigonometric;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.List;

public class ATanFunctionIterator extends AbstractAtMostOneItemRuntimePlan implements NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan argument;

    public ATanFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.argument = arguments.get(0);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        Item value = this.argument.materializeFirstOrNull(dynamicContext);
        if (value == null) {
            return null;
        }
        return evaluate(value);
    }

    private static Item evaluate(Item value) {
        double dvalue = value.getDoubleValue();
        if (Double.isNaN(dvalue)) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
        return ItemFactory.getInstance().createDoubleItem(Math.atan(dvalue));
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext childQuery = NativeQueryRuntimePlan.generate(
            this.argument,
            nativeClauseContext
        );
        if (childQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(childQuery.getResultingType().getArity())) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "ATAN( "
            + childQuery.getResultingQuery()
            + " )";
        return new NativeClauseContext(
                childQuery,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.doubleItem, childQuery.getResultingType().getArity())
        );
    }

}
