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
package org.rumbledb.compiler.frontend;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.rumbledb.compiler.analysis.VariableDependenciesVisitor;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.module.LibraryModule;

/** Shared module import semantics for the JSONiq and XQuery frontends. */
final class ModuleImportLoader {

    private ModuleImportLoader() {}

    public static LibraryModule load(
            String namespace,
            List<String> locationHints,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration,
            ExceptionMetadata metadata) {
        URI baseURI = importingModuleContext.getStaticBaseURI();
        String normalizedNamespace = URILiteralUtils.normalizeAsAnyURI(namespace);
        List<String> candidates = locationHints.isEmpty() ? List.of(normalizedNamespace) : locationHints;
        Exception lastFailure = null;

        for (String candidate : candidates) {
            URI location;
            try {
                location = URILiteralUtils.resolve(baseURI, candidate, metadata);
            } catch (RumbleException e) {
                lastFailure = e;
                continue;
            }
            try {
                ModuleSource source = ModuleSourceReader.read(location, compilationConfiguration, metadata);
                LibraryModule module =
                        ModuleParser.parseLibraryModule(source, importingModuleContext, compilationConfiguration);
                // The dependency visitor does not traverse imports from a prolog, so each library
                // needs its own pass. Run it before namespace validation and duplicate-import pruning.
                new VariableDependenciesVisitor(compilationConfiguration.runtimeConfiguration()).visit(module, null);

                if (!normalizedNamespace.equals(module.getNamespace())) {
                    throw new ModuleNotFoundException(
                            "A module with namespace "
                                    + normalizedNamespace
                                    + " was not found. The namespace of the module at this location was: "
                                    + module.getNamespace(),
                            metadata);
                }

                return module;
            } catch (IOException | CannotRetrieveResourceException e) {
                lastFailure = e;
            }
        }

        RumbleException exception = new ModuleNotFoundException(
                "Module not found: %s, cause: %s"
                        .formatted(normalizedNamespace, lastFailure != null ? lastFailure.getMessage() : "unknown"),
                metadata);
        if (lastFailure != null) {
            exception.initCause(lastFailure);
        }
        throw exception;
    }
}
