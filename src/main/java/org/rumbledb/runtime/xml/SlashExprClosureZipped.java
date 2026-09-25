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
package org.rumbledb.runtime.xml;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;

import scala.Tuple2;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class SlashExprClosureZipped implements FlatMapFunction<Tuple2<Item, Long>, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan rightIterator;
    private final DynamicContext dynamicContext;
    private final long contextSize;

    public SlashExprClosureZipped(ItemRuntimePlan rightIterator, DynamicContext dynamicContext, long contextSize) {
        this.rightIterator = rightIterator;
        if (this.rightIterator.isSparkJobNeeded()) {
            throw new JobWithinAJobException(
                    "The right-hand side of this slash expression requires parallel execution, but the slash expression is itself executed in parallel.",
                    this.rightIterator.getRuntimeStaticContext().getMetadata());
        }
        this.dynamicContext = dynamicContext;
        this.contextSize = contextSize;
    }

    @Override
    public Iterator<Item> call(Tuple2<Item, Long> itemWithIndex) {
        List<Item> currentItems = new ArrayList<>();
        currentItems.add(itemWithIndex._1());
        DynamicContext currentContext = new DynamicContext(this.dynamicContext);
        currentContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, currentItems);
        currentContext.getVariableValues().setPosition(itemWithIndex._2() + 1);
        currentContext.getVariableValues().setLast(this.contextSize);
        List<Item> result = this.rightIterator.materialize(currentContext);
        return result.iterator();
    }
}
