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
package org.rumbledb.runtime.plan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.types.SequenceType;

/**
 * Shared immutable compilation state for plans that produce items.
 */
public abstract class ItemRuntimePlan extends RuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<ItemRuntimePlan> children;

    protected ItemRuntimePlan(
            @NonNull List<? extends ItemRuntimePlan> children, @NonNull RuntimeStaticContext staticContext) {
        super(staticContext, ItemRuntimeDataFrameFactory.INSTANCE);
        this.children = new ArrayList<>(children);
    }

    protected final ItemRuntimePlan getChild(int index) {
        return this.children.get(index);
    }

    protected final List<ItemRuntimePlan> getChildren() {
        return this.children;
    }

    public final SequenceType getStaticType() {
        return this.staticContext.getStaticType();
    }

    public boolean isSparkJobNeeded() {
        return this.staticContext.getExecutionMode().isRDDOrDataFrame()
                || this.children.stream().anyMatch(ItemRuntimePlan::isSparkJobNeeded);
    }

    public void print(StringBuilder buffer, int indent) {
        for (int i = 0; i < indent; i++) {
            buffer.append("  ");
        }
        buffer.append(this.getClass().getSimpleName())
                .append(" | ")
                .append(this.staticContext.getExecutionMode())
                .append(" | ")
                .append(this.staticContext.getStaticType())
                .append('\n');
        for (ItemRuntimePlan child : this.children) {
            child.print(buffer, indent + 1);
        }
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (ItemRuntimePlan child : this.children) {
            DynamicContext.mergeVariableDependencies(result, child.getVariableDependencies());
        }
        return result;
    }
}
