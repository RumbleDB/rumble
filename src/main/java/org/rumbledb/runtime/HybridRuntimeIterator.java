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

package org.rumbledb.runtime;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.AtMostOneLocalRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.VariableDependencyRuntimePlan;

import java.io.Serial;
import java.util.List;

public abstract class HybridRuntimeIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            NativeQueryRuntimePlan,
            VariableDependencyRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    protected HybridRuntimeIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        if (this instanceof AtMostOneLocalRuntimePlan<?> atMostOnePlan) {
            return new AtMostOneLocalCursor<>(getMetadata()) {
                @Override
                protected Item materializeOneItemOrNull() {
                    @SuppressWarnings("unchecked")
                    AtMostOneLocalRuntimePlan<Item> itemPlan = (AtMostOneLocalRuntimePlan<Item>) atMostOnePlan;
                    return itemPlan.evaluateAtMostOne(context);
                }
            };
        }
        throw new OurBadException(
                "The runtime plan "
                    + this.getClass().getCanonicalName()
                    + " does not provide a native local cursor.",
                getMetadata()
        );
    }

    @Override
    public final JavaRDD<Item> createNativeRDD(DynamicContext context) {
        return getRDDAux(context);
    }

    protected abstract JavaRDD<Item> getRDDAux(DynamicContext context);
}
