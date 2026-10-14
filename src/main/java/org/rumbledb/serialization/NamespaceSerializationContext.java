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
package org.rumbledb.serialization;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.rumbledb.api.Item;
import org.rumbledb.items.xml.NamespaceItem;

/** Namespace scopes of the elements in the output traversal, independent of source-tree parents. */
final class NamespaceSerializationContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Deque<Map<String, String>> scopes = new ArrayDeque<>();

    void enterElement(Item element) {
        Map<String, String> scope = new LinkedHashMap<>();
        for (Item node : element.namespaceNodes()) {
            NamespaceItem namespace = (NamespaceItem) node;
            scope.put(namespace.getPrefix(), namespace.getUri());
        }
        this.scopes.push(scope);
    }

    void leaveElement() {
        this.scopes.pop();
    }

    List<Item> declarations(Item element, BiPredicate<String, String> shouldUndeclare) {
        var iterator = this.scopes.iterator();
        Map<String, String> current = iterator.next();
        Map<String, String> parent = iterator.hasNext() ? iterator.next() : Map.of();
        List<Item> result = new ArrayList<>();
        for (Map.Entry<String, String> binding : current.entrySet()) {
            if (!"xml".equals(binding.getKey()) && !Objects.equals(parent.get(binding.getKey()), binding.getValue())) {
                result.add(namespaceNode(element, binding.getKey(), binding.getValue()));
            }
        }
        for (Map.Entry<String, String> binding : parent.entrySet()) {
            if (!"xml".equals(binding.getKey())
                    && !current.containsKey(binding.getKey())
                    && shouldUndeclare.test(binding.getKey(), binding.getValue())) {
                result.add(namespaceNode(element, binding.getKey(), ""));
            }
        }
        return result;
    }

    private static Item namespaceNode(Item parent, String prefix, String uri) {
        Item node = new NamespaceItem(prefix, uri);
        node.setParent(parent);
        return node;
    }
}
