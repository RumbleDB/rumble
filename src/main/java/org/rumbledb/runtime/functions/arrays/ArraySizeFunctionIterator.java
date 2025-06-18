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

package org.rumbledb.runtime.functions.arrays;

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

public class ArraySizeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public ArraySizeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item array = this.children.get(0).materializeFirstItemOrNull(context);
        if (array == null) {
            return null;
        }
        return ItemFactory.getInstance().createIntItem(array.getSize());
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext nativeChildQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
        if (nativeChildQuery != NativeClauseContext.NoNativeQuery) {
            return new NativeClauseContext(
                    nativeClauseContext,
                    "SIZE (" + nativeChildQuery.getResultingQuery() + ")",
                    new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One)
            );
        }
        return NativeClauseContext.NoNativeQuery;
    }
}
