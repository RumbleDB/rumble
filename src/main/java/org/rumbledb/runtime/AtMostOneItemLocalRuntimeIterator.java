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
 * Authors: Ghislain Fourny
 *
 */

package org.rumbledb.runtime;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.plan.VariableDependencyRuntimePlan;
import org.rumbledb.runtime.plan.AtMostOneLocalRuntimePlan;

import java.io.Serial;
import java.util.List;

public abstract class AtMostOneItemLocalRuntimeIterator extends RuntimePlan<Item>
        implements
            AtMostOneLocalRuntimePlan<Item>,
            NativeQueryRuntimePlan,
            VariableDependencyRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    protected AtMostOneItemLocalRuntimeIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext, ItemRuntimeDataFrameFactory.INSTANCE);
    }

    @Override
    public final Cursor<Item> createNativeCursor(DynamicContext context) {
        return new AtMostOneLocalCursor<>(getMetadata()) {
            @Override
            protected Item materializeOneItemOrNull() {
                return AtMostOneItemLocalRuntimeIterator.this.evaluateAtMostOne(context);
            }
        };
    }

    @Override
    public abstract Item evaluateAtMostOne(
            DynamicContext context
    );
}
