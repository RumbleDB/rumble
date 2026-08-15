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

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorVariables;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.control.CatchPattern;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

public class TryCatchRuntimeIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan tryExpression;
    private final Map<CatchPattern, ? extends ItemRuntimePlan> catchExpressions;

    public TryCatchRuntimeIterator(
            ItemRuntimePlan tryExpression,
            Map<CatchPattern, ? extends ItemRuntimePlan> catchExpressions,
            RuntimeStaticContext staticContext) {
        super(
                Stream.concat(Stream.of(tryExpression), catchExpressions.values().stream())
                        .toList(),
                staticContext);
        this.tryExpression = tryExpression;
        this.catchExpressions = catchExpressions;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> evaluate(context).iterator(), getMetadata());
    }

    private List<Item> evaluate(DynamicContext context) {
        try {
            return this.tryExpression.materialize(context);
        } catch (Throwable throwable) {
            RumbleException exception = RumbleException.unnestException(throwable);
            ItemRuntimePlan catchingExpression = findMatchingCatch(exception);
            if (catchingExpression == null) {
                throw throwable;
            }
            DynamicContext catchContext = new DynamicContext(context);
            ErrorVariables.injectDynamicContext(catchContext, exception);
            return catchingExpression.materialize(catchContext);
        }
    }

    private ItemRuntimePlan findMatchingCatch(RumbleException exception) {
        for (Map.Entry<CatchPattern, ? extends ItemRuntimePlan> entry : this.catchExpressions.entrySet()) {
            if (entry.getKey().matches(exception.getErrorCode())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
