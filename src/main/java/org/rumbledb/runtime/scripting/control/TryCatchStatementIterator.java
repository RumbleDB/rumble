/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.scripting.control;

import java.io.Serial;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorVariables;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.control.CatchPattern;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class TryCatchStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan tryStatementIterator;
    private final Map<CatchPattern, ? extends ItemRuntimePlan> catchStatements;

    public TryCatchStatementIterator(
            ItemRuntimePlan tryStatement,
            Map<CatchPattern, ? extends ItemRuntimePlan> catchStatements,
            RuntimeStaticContext staticContext) {
        super(
                Stream.concat(Stream.of(tryStatement), catchStatements.values().stream())
                        .toList(),
                staticContext);
        this.tryStatementIterator = tryStatement;
        this.catchStatements = catchStatements;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        BiConsumer<ItemRuntimePlan, DynamicContext> materialize = ItemRuntimePlan::materialize;
        try {
            DynamicContext childContext = new DynamicContext(context);
            materialize.accept(this.tryStatementIterator, childContext);
        } catch (Throwable throwable) {
            // If we catch a break or continue exception, our catch should not be allowed to act on it
            if (throwable instanceof BreakStatementException
                    || throwable instanceof ContinueStatementException
                    || throwable instanceof ExitStatementException) {
                throw throwable;
            }
            RumbleException unnestedException = RumbleException.unnestException(throwable);
            ItemRuntimePlan catchingStatementIterator = findMatchingCatch(unnestedException);
            if (catchingStatementIterator != null) {
                DynamicContext childContext = new DynamicContext(context);
                ErrorVariables.injectDynamicContext(childContext, unnestedException);
                materialize.accept(catchingStatementIterator, childContext);
            } else {
                throw throwable;
            }
        }
        return null;
    }

    private ItemRuntimePlan findMatchingCatch(RumbleException exception) {
        for (Map.Entry<CatchPattern, ? extends ItemRuntimePlan> entry : this.catchStatements.entrySet()) {
            if (entry.getKey().matches(exception.getErrorCode())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
