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

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

import org.rumbledb.api.Item;

public class TailFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private Item _nextResult;

    public TailFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            Item result = _nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "tail function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);

        if (!_iterator.hasNext()) {
            this._hasNext = false;
        } else {
            _iterator.next(); // skip the first item
            setNextResult();
        }
    }

    public void setNextResult() {
        _nextResult = null;

        if (_iterator.hasNext()) {
            _nextResult = _iterator.next();
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

}
