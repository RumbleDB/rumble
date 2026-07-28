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

import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.SequenceExceptionExactlyOne;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class ExactlyOneIterator extends AtMostOneItemLocalRuntimeIterator {


    @Serial
    private static final long serialVersionUID = 1L;

    public ExactlyOneIterator(
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
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        try {
            Item value = this.getChild(0).materializeAtMostOneItemOrNull(dynamicContext);
            if (value == null) {
                throw new SequenceExceptionExactlyOne(
                        "fn:exactly-one() called with a sequence that doesn't contain exactly one item",
                        getMetadata()
                );

            }
            return value;
        } catch (MoreThanOneItemException e) {
            throw new SequenceExceptionExactlyOne(
                    "fn:exactly-one() called with a sequence that doesn't contain exactly one item",
                    getMetadata()
            );
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return this.getChild(0).generateNativeQuery(nativeClauseContext);
    }

    private static final class Cursor extends AtMostOneLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;

        private Cursor(
                @NonNull RuntimePlan<Item> childPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.childPlan = childPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected Item materializeFirstItemOrNull() {
            try {
                Item value = this.childPlan.materializeAtMostOne(this.context);
                if (value == null) {
                    throw invalidCardinality();
                }
                return value;
            } catch (MoreThanOneItemException exception) {
                throw invalidCardinality();
            }
        }

        private SequenceExceptionExactlyOne invalidCardinality() {
            return new SequenceExceptionExactlyOne(
                    "fn:exactly-one() called with a sequence that doesn't contain exactly one item",
                    this.metadata
            );
        }
    }
}
