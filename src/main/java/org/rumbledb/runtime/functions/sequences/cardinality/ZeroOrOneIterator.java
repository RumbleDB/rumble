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

package org.rumbledb.runtime.functions.sequences.cardinality;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.SequenceExceptionZeroOrOne;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

public class ZeroOrOneIterator extends AtMostOneItemLocalRuntimeIterator {


    @Serial
    private static final long serialVersionUID = 1L;

    public ZeroOrOneIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new Cursor(getChild(0), context, getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator sequenceIterator = this.getChild(0);
        Item result = null;
        try {
            result = sequenceIterator.materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new SequenceExceptionZeroOrOne(
                    "fn:zero-or-one() called with a sequence containing more than one item",
                    getMetadata()
            );
        }
        return result;
    }

    private static final class Cursor extends AtMostOneLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;

        private Cursor(RuntimePlan<Item> childPlan, DynamicContext context, ExceptionMetadata metadata) {
            this.childPlan = Objects.requireNonNull(childPlan, "child plan cannot be null");
            this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
            this.metadata = Objects.requireNonNull(metadata, "metadata cannot be null");
        }

        @Override
        protected Item materializeFirstItemOrNull() {
            try {
                return LocalCursorUtils.materializeAtMostOne(this.childPlan, this.context);
            } catch (MoreThanOneItemException exception) {
                throw new SequenceExceptionZeroOrOne(
                        "fn:zero-or-one() called with a sequence containing more than one item",
                        this.metadata
                );
            }
        }
    }
}
