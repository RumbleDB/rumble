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
import java.util.List;
import java.util.Set;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.Prolog;

/**
 * Removes duplicate declarations while retaining import visibility for shared modules.
 *
 */
public class ModulePruningVisitor extends AbstractNodeVisitor<Void> {

    @SuppressWarnings("unused")
    private final RumbleConfiguration configuration;

    private final Set<String> visitedModules;

    /**
     * Builds a new visitor.
     *
     * @param configuration the configuration. This is used for trigerring or not debug output.
     */
    ModulePruningVisitor(RumbleConfiguration configuration) {
        this.configuration = configuration;
        this.visitedModules = new HashSet<>();
    }

    @Override
    public Void visitProlog(Prolog prolog, Void argument) {
        // Prune in depth-first order: a later sibling may already have been imported by an earlier one.
        for (var schema : prolog.getSchemaImports()) {
            visit(schema, argument);
        }
        var imports = prolog.getImportedModules().listIterator();
        while (imports.hasNext()) {
            LibraryModule module = imports.next();
            String key = module.getProlog().getModuleGroupKey();
            if (key == null) {
                key = module.getMetadata().getLocation();
            }
            if (!this.visitedModules.add(key)) {
                // Keep the import visible to this module without revisiting shared declarations.
                Prolog referenceProlog = new Prolog(
                        List.of(), List.of(), List.of(), module.getProlog().getMetadata());
                referenceProlog.setModuleGroupKey(key);
                LibraryModule reference =
                        new LibraryModule(referenceProlog, module.getNamespace(), module.getMetadata());
                reference.setStaticContext(module.getStaticContext());
                imports.set(reference);
            } else {
                visit(module, argument);
            }
        }
        for (var declaration : prolog.getDeclarations()) {
            visit(declaration, argument);
        }
        return argument;
    }
}
