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

package org.rumbledb.runtime.navigation;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

public class PredicateClosure implements Function<Item, Boolean> {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator expression;
    private final DynamicContext dynamicContext;

    public PredicateClosure(RuntimeIterator expression, DynamicContext dynamicContext) {
        this.expression = expression;
        if (this.expression.isSparkJobNeeded()) {
            throw new JobWithinAJobException(
                    "The expression in this predicate requires parallel execution, but the predicate is itself executed in parallel. Please consider moving it up or unnest it if it is independent on previous FLWOR variables.",
                    this.expression.getMetadata()
            );
        }
        this.dynamicContext = dynamicContext;
    }

    @Override
    public Boolean call(Item v1) throws Exception {
        List<Item> currentItems = new ArrayList<>();
        currentItems.add(v1);
        DynamicContext dynamicContext = new DynamicContext(this.dynamicContext);
        dynamicContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, currentItems);

        boolean result = this.expression.getEffectiveBooleanValue(dynamicContext);
        return result;

    }

};
