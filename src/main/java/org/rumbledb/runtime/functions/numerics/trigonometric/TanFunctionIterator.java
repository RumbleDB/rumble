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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.MappingLocalCursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.List;

public class TanFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator argument;

    public TanFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.argument = arguments.get(0);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new MappingLocalCursor<>(this.argument, context, TanFunctionIterator::evaluate, getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item value = this.argument.materializeFirstItemOrNull(context);
        if (value == null) {
            return null;
        }
        return evaluate(value);
    }

    private static Item evaluate(Item value) {
        double dvalue = value.getDoubleValue();
        if (Double.isNaN(dvalue) || Double.isInfinite(dvalue)) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
        return ItemFactory.getInstance().createDoubleItem(Math.tan(dvalue));

    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext childQuery = this.argument.generateNativeQuery(nativeClauseContext);
        if (childQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(childQuery.getResultingType().getArity())) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "TAN( "
            + childQuery.getResultingQuery()
            + " )";
        return new NativeClauseContext(
                childQuery,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.doubleItem, childQuery.getResultingType().getArity())
        );
    }

}
