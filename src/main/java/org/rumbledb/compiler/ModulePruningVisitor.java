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

import java.util.HashSet;
import java.util.Set;

import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.Prolog;

/** Keeps one copy of each imported module's declarations; repeated imports keep only its context bindings. */
public class ModulePruningVisitor extends AbstractNodeVisitor<Void> {

    private final Set<StaticContext> visitedModules = new HashSet<>();

    @Override
    public Void visitProlog(Prolog prolog, Void argument) {
        prolog.getImportedModules().replaceAll(module -> {
            if (this.visitedModules.add(module.getStaticContext())) {
                visit(module, argument);
                return module;
            }
            return emptyReferenceTo(module);
        });
        return argument;
    }

    private static LibraryModule emptyReferenceTo(LibraryModule module) {
        LibraryModule reference = new LibraryModule(
                new Prolog(module.getProlog().getMetadata()), module.getNamespace(), module.getMetadata());
        reference.setStaticContext(module.getStaticContext());
        return reference;
    }
}
