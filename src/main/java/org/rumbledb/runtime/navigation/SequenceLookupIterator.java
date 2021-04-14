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

package org.rumbledb.runtime.navigation;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequenceLookupIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private int position;

    public SequenceLookupIterator(
            RuntimeIterator sequence,
            int position,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(sequence), executionMode, iteratorMetadata);
        this.iterator = sequence;
        this.position = position;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        List<Item> materializedItems = new ArrayList<>();
        this.iterator.materializeNFirstItems(
            this.currentDynamicContextForLocalExecution,
            materializedItems,
            this.position
        );
        if (materializedItems.size() >= this.position) {
            return materializedItems.get(this.position - 1);
        } else {
            return null;
        }
    }

}
