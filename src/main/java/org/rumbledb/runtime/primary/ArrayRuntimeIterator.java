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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.ArrayItemType;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class ArrayRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private boolean isSquareConstructor;

    /**
     * Curly array constructor: single child whose items become singleton members.
     */
    public ArrayRuntimeIterator(
            RuntimeIterator arrayItems,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.isSquareConstructor = false;
        if (arrayItems != null) {
            this.children.add(arrayItems);
        }
    }

    /**
     * Square array constructor: each child iterator produces one member (possibly a sequence).
     */
    public ArrayRuntimeIterator(
            List<RuntimeIterator> memberIterators,
            boolean isSquareConstructor,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.isSquareConstructor = isSquareConstructor;
        if (memberIterators != null) {
            this.children.addAll(memberIterators);
        }
    }

    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        if (isEffectiveSquareConstructor()) {
            boolean allSingleton = true;
            List<List<Item>> memberSequences = new ArrayList<>();
            for (RuntimeIterator child : this.children) {
                List<Item> member = child.materialize(dynamicContext);
                if (allSingleton && member.size() != 1) {
                    allSingleton = false;
                }
                memberSequences.add(member);
            }
            if (allSingleton) {
                List<Item> items = new ArrayList<>();
                for (List<Item> member : memberSequences) {
                    items.add(member.get(0));
                }
                return ItemFactory.getInstance().createArrayItem(items, true);
            } else {
                return ItemFactory.getInstance().createSequenceArrayItem(memberSequences, true);
            }
        }
        List<Item> result = new ArrayList<>();
        for (RuntimeIterator child : this.children) {
            result.addAll(child.materialize(dynamicContext));
        }
        return ItemFactory.getInstance().createArrayItem(result, true);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (isEffectiveSquareConstructor()) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (this.children.size() == 1) {
            NativeClauseContext childQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
            if (childQuery == NativeClauseContext.NoNativeQuery) {
                return NativeClauseContext.NoNativeQuery;
            }
            String resultingQuery;
            if (this.children.get(0) instanceof CommaExpressionIterator) {
                resultingQuery = childQuery.getResultingQuery();
            } else {
                resultingQuery = "array( "
                    + childQuery.getResultingQuery()
                    + " )";
            }
            return new NativeClauseContext(
                    childQuery,
                    resultingQuery,
                    new SequenceType(
                            ArrayItemType.arrayOf(childQuery.getResultingType().getItemType()),
                            SequenceType.Arity.One
                    )
            );
        } else {
            return new NativeClauseContext(
                    nativeClauseContext,
                    "array()",
                    new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceType.Arity.One)
            );
        }
    }

    private boolean isEffectiveSquareConstructor() {
        if (!this.isSquareConstructor) {
            return false;
        }
        String queryLanguage = getConfiguration().getQueryLanguage();
        return queryLanguage.equals("xquery30") || queryLanguage.equals("xquery31");
    }
}
