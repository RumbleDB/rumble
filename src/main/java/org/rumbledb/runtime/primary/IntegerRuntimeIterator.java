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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.SequenceType;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

public class IntegerRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private Item item;

    public IntegerRuntimeIterator(
            String lexicalValue,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.item = ItemFactory.getInstance().createIntegerItem(lexicalValue);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return this.item;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return new NativeClauseContext(
                nativeClauseContext,
                "" + this.item.getIntValue(),
                SequenceType.INTEGER
        );
    }
}
