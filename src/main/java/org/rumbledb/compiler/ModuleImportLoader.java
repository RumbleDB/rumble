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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.Prolog;

/** Shared module import semantics for the JSONiq and XQuery frontends. */
public final class ModuleImportLoader {

    private final CompilationConfiguration configuration;
    final Map<URI, LibraryModule> modules = new LinkedHashMap<>();
    final Map<LibraryModule, LibraryModule> completedModules = new IdentityHashMap<>();
    private final Map<String, List<String>> locations = new LinkedHashMap<>();

    /** Holds the state of one compilation's imports, so create one per compiled module tree. */
    ModuleImportLoader(CompilationConfiguration configuration) {
        this.configuration = configuration;
    }

    CompilationConfiguration configuration() {
        return this.configuration;
    }

    /** Normalize all of a root module's imports together so siblings share the same fragment groups. */
    void normalizeImports(Prolog rootProlog) {
        List<LibraryModule> imports = rootProlog.getImportedModules();
        if (!imports.isEmpty()) {
            List<LibraryModule> normalized = ModuleImportGraph.normalize(imports, this);
            imports.clear();
            imports.addAll(normalized);
        }
    }

    public LibraryModule load(
            String namespace,
            List<String> locationHints,
            StaticContext importingModuleContext,
            ExceptionMetadata metadata) {
        URI baseURI = importingModuleContext.getStaticBaseURI();
        String normalizedNamespace = URILiteralUtils.normalizeAsAnyURI(namespace);
        if (!locationHints.isEmpty()) {
            // Retain absolute hints so a same-namespace import in a fragment can find its peers.
            this.locations.putIfAbsent(
                    normalizedNamespace,
                    locationHints.stream()
                            .map(hint -> URILiteralUtils.resolve(baseURI, hint, metadata)
                                    .toString())
                            .toList());
        }
        // Explicit hints belong to this import; cached hints only supply locations for hintless back edges.
        List<String> candidates = locationHints.isEmpty()
                ? this.locations.getOrDefault(normalizedNamespace, List.of(normalizedNamespace))
                : locationHints;
        Exception lastFailure = null;
        List<LibraryModule> loaded = new ArrayList<>();
        for (String candidate : candidates) {
            URI location;
            try {
                location = URILiteralUtils.resolve(baseURI, candidate, metadata);
            } catch (RumbleException e) {
                lastFailure = e;
                continue;
            }
            URI physical =
                    this.configuration.resourceResolver().resolveURI(location).normalize();
            LibraryModule module = this.modules.get(physical);
            if (module == null) {
                // Back edges refer to an empty shell until this module has finished translation.
                // The completed graph resolves these shells before any analysis pass runs.
                ExceptionMetadata source =
                        new ExceptionMetadata(physical.toString(), metadata.getRange(), metadata.getCode());
                module = new LibraryModule(
                        new Prolog(List.of(), List.of(), List.of(), source), normalizedNamespace, source);
                this.modules.put(physical, module);
                LibraryModule placeholder = module;
                try {
                    module = CompilationPipeline.prepareLibraryModuleFromLocation(
                            location, importingModuleContext, this, metadata);
                    this.modules.put(physical, module);
                    this.completedModules.put(placeholder, module);
                } catch (IOException | CannotRetrieveResourceException | ModuleNotFoundException e) {
                    this.modules.remove(physical);
                    lastFailure = e;
                    continue;
                }
            }
            if (!normalizedNamespace.equals(module.getNamespace())) {
                lastFailure = new ModuleNotFoundException(
                        "A module with namespace "
                                + normalizedNamespace
                                + " was not found. The namespace of the module at this location was: "
                                + module.getNamespace(),
                        metadata);
                continue;
            }
            loaded.add(module);
        }
        if (!loaded.isEmpty()) {
            return loaded.get(0);
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
