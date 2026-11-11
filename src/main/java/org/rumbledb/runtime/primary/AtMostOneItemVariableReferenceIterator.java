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
package org.rumbledb.runtime.primary;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

public class AtMostOneItemVariableReferenceIterator extends AbstractAtMostOneItemRuntimePlan {

    private static final long serialVersionUID = 1L;
    private Name variableName;

    public AtMostOneItemVariableReferenceIterator(Name variableName, RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
        this.variableName = variableName;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        result.put(this.variableName, DynamicContext.VariableDependency.FULL);
        return result;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        List<Item> items = context.getVariableValues().getLocalVariableValue(this.variableName, getMetadata());
        if (items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }
}
