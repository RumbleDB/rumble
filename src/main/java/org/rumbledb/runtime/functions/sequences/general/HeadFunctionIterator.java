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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class HeadFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item result;

    public HeadFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
        this.iterator = this.children.get(0);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this.hasNext = false;
            return this.result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "head function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        setResult();
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        setResult();
    }

    public void setResult() {
        if (this.iterator.isRDDOrDataFrame()) {
            List<Item> i = this.iterator.getRDD(this.currentDynamicContextForLocalExecution).take(1);
            if (i.isEmpty()) {
                this.hasNext = false;
                return;
            }
            this.hasNext = true;
            this.result = i.get(0);
        } else {
            this.iterator.open(this.currentDynamicContextForLocalExecution);
            if (this.iterator.hasNext()) {
                this.hasNext = true;
                this.result = this.iterator.next();
            } else {
                this.hasNext = false;
            }
            this.iterator.close();
        }
    }
}
