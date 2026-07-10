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

import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorVariables;
import org.rumbledb.expressions.control.CatchPattern;


public class TryCatchRuntimeIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private final RuntimeIterator tryExpression;
    private final Map<CatchPattern, RuntimeIterator> catchExpressions;
    private List<Item> results = null;
    private Item nextResult = null;
    private int nextPosition = 0;

    public TryCatchRuntimeIterator(
            RuntimeIterator tryExpression,
            Map<CatchPattern, RuntimeIterator> catchExpressions,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.children.add(tryExpression);
        for (RuntimeIterator value : catchExpressions.values())
            this.children.add(value);
        this.tryExpression = tryExpression;
        this.catchExpressions = catchExpressions;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        setNextResult();
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item nextItem = this.nextResult;
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
        this.results = null;
        setNextResult();
    }

    @Override
    public void close() {
        super.close();
        this.results = null;
    }

    private void setNextResult() {
        if (this.results == null) {
            this.nextPosition = 0;
            this.results = new ArrayList<>();
            try {
                this.tryExpression.open(this.currentDynamicContextForLocalExecution);
                while (this.tryExpression.hasNext()) {
                    this.results.add(this.tryExpression.next());
                }
                this.tryExpression.close();

            } catch (Throwable throwable) {
                RumbleException exception = RumbleException.unnestException(throwable);
                this.results.clear();
                RuntimeIterator catchingExpression = findMatchingCatch(exception);
                if (catchingExpression != null) {
                    DynamicContext context = new DynamicContext(this.currentDynamicContextForLocalExecution);
                    ErrorVariables.injectDynamicContext(context, exception);
                    catchingExpression.open(context);
                    while (catchingExpression.hasNext()) {
                        this.results.add(catchingExpression.next());
                    }
                    catchingExpression.close();
                } else {
                    throw throwable;
                }
            }
        }
        if (this.nextPosition < this.results.size()) {
            this.nextResult = this.results.get(this.nextPosition++);
        } else {
            this.hasNext = false;
        }
    }

    private RuntimeIterator findMatchingCatch(RumbleException exception) {
        for (Map.Entry<CatchPattern, RuntimeIterator> entry : this.catchExpressions.entrySet()) {
            if (entry.getKey().matches(exception.getErrorCode())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
