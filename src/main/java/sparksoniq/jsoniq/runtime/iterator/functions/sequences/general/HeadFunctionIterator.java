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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class HeadFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private Item _result;

    public HeadFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
        this._iterator = this._children.get(0);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return this._result;
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
        if (this._iterator.isRDD()) {
            List<Item> i = this._iterator.getRDD(this._currentDynamicContextForLocalExecution).take(1);
            if (i.isEmpty()) {
                this._hasNext = false;
                return;
            }
            this._hasNext = true;
            this._result = i.get(0);
        }
        this._iterator.open(this._currentDynamicContextForLocalExecution);
        if (this._iterator.hasNext()) {
            this._hasNext = true;
            this._result = this._iterator.next();
        } else {
            this._hasNext = false;
        }
        this._iterator.close();
    }

}
