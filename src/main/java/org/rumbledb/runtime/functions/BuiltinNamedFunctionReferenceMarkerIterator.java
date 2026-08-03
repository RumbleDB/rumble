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
 */
package org.rumbledb.runtime.functions;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;

import java.io.Serial;
import java.util.List;

/**
 * Placeholder body iterator for {@link org.rumbledb.items.FunctionItem}s that represent
 * a builtin named function reference ({@code fn:abs#1}). The real call path uses
 * {@link org.rumbledb.context.NamedFunctions#getBuiltInFunctionIterator}; this iterator
 * must not be evaluated as a normal function body.
 */
public class BuiltinNamedFunctionReferenceMarkerIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public BuiltinNamedFunctionReferenceMarkerIterator(RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new AtMostOneLocalCursor<>(null, this.getRuntimeStaticContext().getMetadata());
    }

}
