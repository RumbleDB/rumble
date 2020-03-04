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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

public class ArrayRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ArrayRuntimeIterator(
            RuntimeIterator arrayItems,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        if (arrayItems != null) {
            this.children.add(arrayItems);
        }
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            List<Item> result = new ArrayList<>();
            if (!this.children.isEmpty()) {
                result.addAll(this.children.get(0).materialize(this.currentDynamicContextForLocalExecution));
            }
            Item item = ItemFactory.getInstance().createArrayItem(result);
            this.hasNext = false;
            return item;
        } else {
            throw new IteratorFlowException("Invalid next() call on array iterator", getMetadata());
        }
    }
}
