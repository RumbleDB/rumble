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
package org.rumbledb.runtime.functions.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

public class InnermostFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> computeResults(context).iterator(), getMetadata());
    }

    private static final long serialVersionUID = 1L;

    public InnermostFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    private List<Item> computeResults(DynamicContext context) {
        List<Item> nodes = this.getChild(0).materialize(context);
        for (Item node : nodes) {
            if (!node.isNode()) {
                throw new UnexpectedTypeException("fn:innermost requires a sequence of nodes", getMetadata());
            }
        }
        Set<Item> nodeSet = new HashSet<>(nodes);
        Set<Item> excluded = new HashSet<>();
        for (Item node : nodes) {
            Item current = node.parent();
            while (current != null) {
                if (nodeSet.contains(current)) {
                    excluded.add(current);
                }
                current = current.parent();
            }
        }
        List<Item> distinctResult = new ArrayList<>();
        Set<Item> seen = new HashSet<>();
        for (Item node : nodes) {
            if (!excluded.contains(node) && seen.add(node)) {
                distinctResult.add(node);
            }
        }
        distinctResult.sort((a, b) -> a.getXmlDocumentPosition().compareTo(b.getXmlDocumentPosition()));
        return distinctResult;
    }
}
