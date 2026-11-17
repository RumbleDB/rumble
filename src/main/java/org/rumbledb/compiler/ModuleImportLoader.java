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
import java.util.function.Supplier;

import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;

/** Shared module import semantics for the JSONiq and XQuery frontends. */
public final class ModuleImportLoader {

    private ModuleImportLoader() {}

    private static final ThreadLocal<ImportSession> CURRENT_SESSION = new ThreadLocal<>();

    static final class ImportSession {
        final Map<URI, LibraryModule> modules = new LinkedHashMap<>();
        final Map<String, List<String>> locations = new LinkedHashMap<>();
        final Map<LibraryModule, LibraryModule> completedModules = new IdentityHashMap<>();
    }

    /** Normalize all imports together so sibling imports share the same fragment groups. */
    static MainModule loadMainModule(Supplier<MainModule> parser, RumbleConfiguration configuration) {
        ImportSession previous = CURRENT_SESSION.get();
        ImportSession session = new ImportSession();
        CURRENT_SESSION.set(session);
        try {
            MainModule module = parser.get();
            List<LibraryModule> imports = module.getProlog().getImportedModules();
            if (!imports.isEmpty()) {
                List<LibraryModule> normalized = ModuleImportGraph.normalize(imports, session, configuration);
                imports.clear();
                imports.addAll(normalized);
            }
            return module;
        } finally {
            if (previous == null) CURRENT_SESSION.remove();
            else CURRENT_SESSION.set(previous);
        }
    }

    public static LibraryModule load(
            String namespace,
            List<String> locationHints,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration,
            ExceptionMetadata metadata) {
        ImportSession session = CURRENT_SESSION.get();
        boolean outermost = session == null;
        if (outermost) {
            session = new ImportSession();
            CURRENT_SESSION.set(session);
        }
        try {
            LibraryModule result = loadModules(
                    namespace, locationHints, importingModuleContext, compilationConfiguration, metadata, session);
            return outermost
                    ? ModuleImportGraph.normalize(result, session, compilationConfiguration.runtimeConfiguration())
                    : result;
        } finally {
            if (outermost) {
                CURRENT_SESSION.remove();
            }
        }
    }

    private static LibraryModule loadModules(
            String namespace,
            List<String> locationHints,
            StaticContext importingModuleContext,
            CompilationConfiguration compilationConfiguration,
            ExceptionMetadata metadata,
            ImportSession session) {
        URI baseURI = importingModuleContext.getStaticBaseURI();
        String normalizedNamespace = URILiteralUtils.normalizeAsAnyURI(namespace);
        if (!locationHints.isEmpty()) {
            // Retain absolute hints so a same-namespace import in a fragment can find its peers.
            session.locations.putIfAbsent(
                    normalizedNamespace,
                    locationHints.stream()
                            .map(hint -> URILiteralUtils.resolve(baseURI, hint, metadata)
                                    .toString())
                            .toList());
        }
        // Explicit hints belong to this import; cached hints only supply locations for hintless back edges.
        List<String> candidates = locationHints.isEmpty()
                ? session.locations.getOrDefault(normalizedNamespace, List.of(normalizedNamespace))
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
            URI physical = compilationConfiguration
                    .resourceResolver()
                    .resolveURI(location)
                    .normalize();
            LibraryModule module = session.modules.get(physical);
            if (module == null) {
                // Back edges refer to an empty shell until this module has finished translation.
                // The completed graph resolves these shells before any analysis pass runs.
                ExceptionMetadata source =
                        new ExceptionMetadata(physical.toString(), metadata.getRange(), metadata.getCode());
                module = new LibraryModule(
                        new Prolog(List.of(), List.of(), List.of(), source), normalizedNamespace, source);
                session.modules.put(physical, module);
                LibraryModule placeholder = module;
                try {
                    module = CompilationPipeline.prepareLibraryModuleFromLocation(
                            location, importingModuleContext, compilationConfiguration, metadata);
                    session.modules.put(physical, module);
                    session.completedModules.put(placeholder, module);
                } catch (IOException | CannotRetrieveResourceException | ModuleNotFoundException e) {
                    session.modules.remove(physical);
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
