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

package org.rumbledb.runtime;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.expressions.ExecutionMode;

import java.util.List;

public abstract class AtMostOneItemLocalRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item result;

    protected AtMostOneItemLocalRuntimeIterator(
            List<RuntimeIterator> children,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(children, executionMode, iteratorMetadata);
    }

    public abstract Item materializeFirstItemOrNull(
            DynamicContext context
    );

    @Override
    public void open(DynamicContext dynamicContext) {
        super.open(dynamicContext);
        this.result = materializeFirstItemOrNull(dynamicContext);
        this.hasNext = result != null;
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasNext = false;
        return result;
    }

    @Override
    public void reset(DynamicContext dynamicContext) {
        super.reset(dynamicContext);
        this.result = materializeFirstItemOrNull(dynamicContext);
        this.hasNext = result != null;
    }

    @Override
    public void close() {
        super.close();
        this.result = null;
    }

    public Item materializeExactlyOneItem(
            DynamicContext dynamicContext
    )
            throws NoItemException,
                MoreThanOneItemException {
        Item result = materializeFirstItemOrNull(dynamicContext);
        if (result == null) {
            throw new NoItemException();
        }
        return result;
    }

    public Item materializeAtMostOneItemOrNull(
            DynamicContext dynamicContext
    )
            throws MoreThanOneItemException {
        return materializeFirstItemOrNull(dynamicContext);
    }

    public void materialize(DynamicContext dynamicContext, List<Item> result) {
        result.clear();
        Item item = materializeFirstItemOrNull(dynamicContext);
        if (item != null) {
            result.add(item);
        }
        this.close();
    }

    public void materializeNFirstItems(DynamicContext dynamicContext, List<Item> result, int n) {
        result.clear();
        if (n == 0) {
            return;
        }
        Item item = materializeFirstItemOrNull(dynamicContext);
        if (item != null) {
            result.add(item);
        }
        this.close();
    }
}
