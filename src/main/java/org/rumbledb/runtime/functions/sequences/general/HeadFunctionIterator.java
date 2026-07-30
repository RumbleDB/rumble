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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import lombok.NonNull;
import java.io.Serial;
import java.util.List;

public class HeadFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    @Serial
    private static final long serialVersionUID = 1L;

    public HeadFunctionIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(getChild(0), context, this.getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        if (this.getChild(0).isRDDOrDataFrame()) {
            List<Item> i = this.getChild(0).getRDD(dynamicContext).take(1);
            if (i.isEmpty()) {
                return null;
            }
            return i.get(0);
        }
        return this.getChild(0).materializeFirstItemOrNull(dynamicContext);
    }

    private static final class EvaluationCursor extends AtMostOneLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final DynamicContext context;

        private EvaluationCursor(
                @NonNull RuntimePlan<Item> childPlan,
                @NonNull DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.childPlan = childPlan;
            this.context = context;
        }

        @Override
        protected Item materializeOneItemOrNull() {
            return this.childPlan.materializeFirstOrNull(this.context);
        }
    }
}
