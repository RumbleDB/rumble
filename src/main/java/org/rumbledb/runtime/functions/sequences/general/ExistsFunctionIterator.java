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

package org.rumbledb.runtime.functions.sequences.general;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.util.List;

public class ExistsFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public ExistsFunctionIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        if (this.children.get(0).isDataFrame()) {
            return ItemFactory.getInstance()
                .createBooleanItem(
                    !this.children.get(0).getDataFrame(dynamicContext).take(1).isEmpty()
                );
        }
        if (this.children.get(0).isRDDOrDataFrame()) {
            List<Item> i = this.children.get(0).getRDD(dynamicContext).take(1);
            return ItemFactory.getInstance().createBooleanItem(!i.isEmpty());
        }
        Item first = this.children.get(0).materializeFirstItemOrNull(dynamicContext);
        if (first == null) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        return ItemFactory.getInstance().createBooleanItem(true);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext childQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
        if (childQuery != NativeClauseContext.NoNativeQuery) {
            if (
                childQuery.getResultingType().getItemType().isArrayItemType()
                    || SequenceType.Arity.OneOrMore.isSubtypeOf(childQuery.getResultingType().getArity())
            ) {
                String resultingQuery = String.format("size(%s) > 0", childQuery.getResultingQuery());
                return new NativeClauseContext(
                        childQuery,
                        resultingQuery,
                        new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One)
                );
            } else {
                String resultingQuery = String.format("not %s is null", childQuery.getResultingQuery());
                return new NativeClauseContext(
                        childQuery,
                        resultingQuery,
                        new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One)
                );
            }
        }
        return NativeClauseContext.NoNativeQuery;
    }
}
