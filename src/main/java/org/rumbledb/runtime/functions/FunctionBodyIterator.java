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
package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;

/**
 * The body of a user-defined function. Every call of a function evaluates its body, so this is where exit statements
 * and tail calls are handled, whatever the kind of call:
 *
 * <ul>
 * <li>An exit statement ends the function call: its results replace the rest of the body's results.</li>
 * <li>A tail call returns a function item with its arguments bound. The body of that function is then evaluated in its
 * place, in a loop, so that a tail recursion does not grow the stack.</li>
 * </ul>
 *
 * Only a body that contains an exit statement or a tail call reads its results through a
 * {@link FunctionBodyLocalCursor}, so that other calls do not pay for it.
 */
public class FunctionBodyIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item>, UpdatingRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan body;
    private final boolean hasExitStatement;
    private final boolean hasTailCalls;

    public FunctionBodyIterator(ItemRuntimePlan body, boolean hasExitStatement, boolean hasTailCalls) {
        super(List.of(body), body.getRuntimeStaticContext());
        this.body = body;
        this.hasExitStatement = hasExitStatement;
        this.hasTailCalls = hasTailCalls;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        if (!this.hasExitStatement && !this.hasTailCalls) {
            return this.body.getCursor(context);
        }
        return new FunctionBodyLocalCursor(context);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        try {
            return this.body.getRDD(context);
        } catch (ExitStatementException e) {
            return e.getRddResult();
        }
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        try {
            return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(this.body, context);
        } catch (ExitStatementException e) {
            return e.getDataFrameResult();
        }
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        return this.body.getRuntimeStaticContext().isUpdating()
                ? UpdatingRuntimePlan.get(this.body, context)
                : new PendingUpdateList();
    }

    /**
     * Whether an item is the result of a tail call: a function item, with all arguments bound, that stands for the rest
     * of the evaluation.
     */
    private static boolean isTailCall(Item item) {
        return item != null
                && item.isFunction()
                && item.getIdentifier().getArity() == 0
                && Name.TAIL_CALL_OPTIMIZATION.equals(item.getIdentifier().getName());
    }

    private final class FunctionBodyLocalCursor extends AbstractLocalCursor<Item> {

        // The body being evaluated and its context: this body in the call context, then the body of each tail call.
        private ItemRuntimePlan body;
        private DynamicContext context;
        private Cursor<Item> delegate;
        // The next result, read ahead because an exit statement may be raised by hasNext, or by next after hasNext
        // returned true, and may then have no results.
        private Item nextResult;

        private FunctionBodyLocalCursor(DynamicContext context) {
            super(FunctionBodyIterator.this.getMetadata());
            this.body = FunctionBodyIterator.this.body;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            evaluateBody();
            while (FunctionBodyIterator.this.hasTailCalls && isTailCall(this.nextResult)) {
                FunctionItem tailCall = (FunctionItem) this.nextResult;
                closeDelegate();
                // The body of the called function, without its FunctionBodyIterator, whose loop would be nested here.
                // A tail call only calls the function itself (see TailCallOptimizationVisitor), so the return type
                // conversion around this body also applies to its result.
                this.body = ((FunctionBodyIterator) tailCall.getBodyIterator()).body;
                this.context = FunctionCall.createCallContext(tailCall, List.of(), this.context);
                evaluateBody();
            }
        }

        private void evaluateBody() {
            // Some plans evaluate their input when their cursor is created.
            try {
                this.delegate = this.body.getCursor(this.context);
            } catch (ExitStatementException e) {
                exit(e);
            }
            advance();
        }

        private void advance() {
            try {
                this.nextResult = this.delegate.hasNext() ? this.delegate.next() : null;
            } catch (ExitStatementException e) {
                exit(e);
                advance();
            }
        }

        private void exit(ExitStatementException exception) {
            closeDelegate();
            List<Item> results = exception.getLocalResult();
            this.delegate = new IteratorLocalCursor<>(results::iterator, getMetadata());
        }

        @Override
        protected boolean hasNextLocal() {
            return this.nextResult != null;
        }

        @Override
        protected Item nextLocal() {
            if (this.nextResult == null) {
                throw invalidState("No more function results are available.");
            }
            Item result = this.nextResult;
            advance();
            return result;
        }

        private void closeDelegate() {
            if (this.delegate != null) {
                this.delegate.close();
                this.delegate = null;
            }
        }

        @Override
        protected void closeLocal() {
            closeDelegate();
            this.nextResult = null;
        }
    }
}
