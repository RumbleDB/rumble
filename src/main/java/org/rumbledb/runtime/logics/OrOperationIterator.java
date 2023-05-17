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

import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.SequenceType;

public class OrOperationIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;

    public OrOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        boolean leftEffectiveBooleanValue = this.leftIterator.getEffectiveBooleanValue(dynamicContext);
        boolean rightEffectiveBooleanValue = this.rightIterator.getEffectiveBooleanValue(dynamicContext);

        return ItemFactory.getInstance().createBooleanItem((leftEffectiveBooleanValue || rightEffectiveBooleanValue));
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext leftResult = this.leftIterator.generateNativeQuery(nativeClauseContext);
        NativeClauseContext rightResult = this.rightIterator.generateNativeQuery(nativeClauseContext);
        if (leftResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (rightResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!this.leftIterator.getStaticType().equals(SequenceType.BOOLEAN)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!this.leftIterator.getStaticType().equals(SequenceType.BOOLEAN)) {
            return NativeClauseContext.NoNativeQuery;
        }

        String resultingQuery = "( " + leftResult.getResultingQuery() + " OR " + rightResult.getResultingQuery() + " )";
        return new NativeClauseContext(nativeClauseContext, resultingQuery);
    }
}
