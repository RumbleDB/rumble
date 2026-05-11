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

package org.rumbledb.runtime.flwor.expression;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// for now unused since we always use SimpleMapExpressionClosureZipped
// can be used if the zipping with position is not needed as optimization, similar to PredicateIterator
public class SimpleMapExpressionClosure implements FlatMapFunction<Item, Item> {


    private static final long serialVersionUID = 1L;
    private final RuntimeIterator rightIterator;
    private final DynamicContext dynamicContext;

    public SimpleMapExpressionClosure(RuntimeIterator rightIterator, DynamicContext dynamicContext) {
        this.rightIterator = rightIterator;
        if (this.rightIterator.isSparkJobNeeded()) {
            throw new JobWithinAJobException(
                    "The expression in this simple map requires parallel execution, but the simple map is itself executed in parallel. Please consider moving it up or unnest it if it is independent on previous FLWOR variables.",
                    this.rightIterator.getMetadata()
            );
        }
        this.dynamicContext = new DynamicContext(dynamicContext);
    }

    public Iterator<Item> call(Item item) throws Exception {
        List<Item> currentItems = new ArrayList<>();

        this.dynamicContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, currentItems);
        currentItems.add(item);
        List<Item> mapValuesRaw = this.rightIterator.materialize(this.dynamicContext);
        this.dynamicContext.getVariableValues().removeVariable(Name.CONTEXT_ITEM);
        return mapValuesRaw.iterator();
    }
}
