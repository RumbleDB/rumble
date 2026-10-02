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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class SlashExprClosure implements FlatMapFunction<Item, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan rightIterator;
    private final DynamicContext dynamicContext;

    public SlashExprClosure(ItemRuntimePlan rightIterator, DynamicContext dynamicContext) {
        this.rightIterator = rightIterator;
        this.dynamicContext = new DynamicContext(dynamicContext);
    }

    @Override
    public Iterator<Item> call(Item item) throws Exception {
        List<Item> currentItems = new ArrayList<>();
        currentItems.add(item);
        this.dynamicContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, currentItems);
        List<Item> result = this.rightIterator.materialize(this.dynamicContext);
        return result.iterator();
    }
}
