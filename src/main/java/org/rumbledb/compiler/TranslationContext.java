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
package org.rumbledb.compiler;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import org.rumbledb.context.StaticContext;

/** Shared mutable state used while either grammar translates a module. */
final class TranslationContext {

    private final StaticContext moduleContext;
    private final ArrayDeque<Map<String, String>> constructorNamespaceFrames;
    private final TranslationNameResolver nameResolver;

    TranslationContext(StaticContext moduleContext) {
        this.moduleContext = moduleContext;
        this.moduleContext.bindDefaultNamespaces();
        this.constructorNamespaceFrames = new ArrayDeque<>();
        this.nameResolver = new TranslationNameResolver(this);
    }

    StaticContext moduleContext() {
        return this.moduleContext;
    }

    TranslationNameResolver names() {
        return this.nameResolver;
    }

    void pushConstructorNamespaceFrame() {
        this.constructorNamespaceFrames.push(new HashMap<>());
    }

    void popConstructorNamespaceFrame() {
        this.constructorNamespaceFrames.pop();
    }

    void bindConstructorNamespace(String prefix, String namespace) {
        if (!this.constructorNamespaceFrames.isEmpty()) {
            this.constructorNamespaceFrames.peek().put(prefix, namespace);
        }
    }

    String resolveNamespace(String prefix) {
        for (Map<String, String> frame : this.constructorNamespaceFrames) {
            if (frame.containsKey(prefix)) {
                return frame.get(prefix);
            }
        }
        return this.moduleContext.resolveNamespace(prefix);
    }
}
