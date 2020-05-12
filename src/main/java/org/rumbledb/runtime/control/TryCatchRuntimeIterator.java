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

package org.rumbledb.runtime.control;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;


public class TryCatchRuntimeIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private final RuntimeIterator _tryExpression;
    private final Map<String, RuntimeIterator> _catchExpressions;
    private final RuntimeIterator _catchAllExpression;
    private List<Item> _results = null;
    private Item _nextResult = null;
    private int _nextPosition = 0;

    public TryCatchRuntimeIterator(
            RuntimeIterator tryExpression,
            Map<String, RuntimeIterator> catchExpressions,
            RuntimeIterator catchAllExpression,
            ExecutionMode mode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, mode, iteratorMetadata);
        this.children.add(tryExpression);
        for (RuntimeIterator value : catchExpressions.values())
            this.children.add(value);
        this.children.add(catchAllExpression);
        _tryExpression = tryExpression;
        _catchExpressions = catchExpressions;
        _catchAllExpression = catchAllExpression;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        setNextResult();
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item nextItem = _nextResult;
            setNextResult();
            return nextItem;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in try-catch statement",
                getMetadata()
        );
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        _results = null;
        setNextResult();
    }

    @Override
    public void close() {
        super.close();
        _results = null;
    }

    private void setNextResult() {
        if (_results == null) {
            _nextPosition = 0;
            _results = new ArrayList<>();
            try {
                _tryExpression.open(this.currentDynamicContextForLocalExecution);
                while (_tryExpression.hasNext()) {
                    _results.add(_tryExpression.next());
                }
                _tryExpression.close();

            } catch (Throwable throwable) {
                RumbleException exception = RumbleException.unnestException(throwable);
                String code = exception.getErrorCode();
                _results.clear();
                if (_catchExpressions.containsKey(code)) {
                    RuntimeIterator catchingExpression = _catchExpressions.get(code);
                    catchingExpression.open(this.currentDynamicContextForLocalExecution);
                    while (catchingExpression.hasNext()) {
                        _results.add(catchingExpression.next());
                    }
                    catchingExpression.close();
                } else if (_catchAllExpression != null) {
                    _catchAllExpression.open(this.currentDynamicContextForLocalExecution);
                    while (_catchAllExpression.hasNext()) {
                        _results.add(_catchAllExpression.next());
                    }
                    _catchAllExpression.close();
                } else {
                    throw throwable;
                }
            }
        }
        if (_nextPosition < _results.size()) {
            _nextResult = _results.get(_nextPosition++);
        } else {
            hasNext = false;
        }
    }
}
