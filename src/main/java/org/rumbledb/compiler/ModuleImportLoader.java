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

/** Loads the modules imported by one compilation, each from its location only once. */
public final class ModuleImportLoader {

    private final CompilationConfiguration configuration;
    private final Map<URI, LibraryModule> modules = new LinkedHashMap<>();
    private final Map<String, List<URI>> locationsByNamespace = new LinkedHashMap<>();

    ModuleImportLoader(CompilationConfiguration configuration) {
        this.configuration = configuration;
    }

    /** Replaces a root module's imports with their acyclic groups, once every import has been loaded. */
    void normalizeImports(Prolog rootProlog) {
        List<LibraryModule> imports = rootProlog.getImportedModules();
        List<LibraryModule> normalized = ModuleImportGraph.normalize(List.copyOf(imports), this.modules.values());
        imports.clear();
        imports.addAll(normalized);
    }

    /** Loads every location of the namespace, as modules sharing a namespace are compiled together. */
    public LibraryModule load(
            String namespace, List<String> locationHints, StaticContext importingContext, ExceptionMetadata metadata) {
        List<URI> locations;
        try {
            locations = findLocations(namespace, locationHints, importingContext.getStaticBaseURI(), metadata);
        } catch (RumbleException e) {
            throw moduleNotFound(namespace, e, metadata);
        }
        LibraryModule firstLoaded = null;
        Exception lastFailure = null;
        for (URI location : locations) {
            try {
                LibraryModule module = loadLocation(location, namespace, importingContext, metadata);
                firstLoaded = firstLoaded == null ? module : firstLoaded;
            } catch (IOException | CannotRetrieveResourceException | ModuleNotFoundException e) {
                lastFailure = e;
            }
        }
        if (firstLoaded == null) {
            throw moduleNotFound(namespace, lastFailure, metadata);
        }
        return firstLoaded;
    }

    /** An import without hints reuses the hints seen for its namespace, or else treats the namespace as a location. */
    private List<URI> findLocations(String namespace, List<String> hints, URI baseURI, ExceptionMetadata metadata) {
        if (hints.isEmpty()) {
            List<URI> known = this.locationsByNamespace.get(namespace);
            return known != null ? known : List.of(URILiteralUtils.resolve(baseURI, namespace, metadata));
        }
        List<URI> locations = hints.stream()
                .map(hint -> URILiteralUtils.resolve(baseURI, hint, metadata))
                .toList();
        this.locationsByNamespace.putIfAbsent(namespace, locations);
        return locations;
    }

    private LibraryModule loadLocation(
            URI location, String namespace, StaticContext importingContext, ExceptionMetadata metadata)
            throws IOException {
        URI resolved =
                this.configuration.resourceResolver().resolveURI(location).normalize();
        LibraryModule module = this.modules.get(resolved);
        if (module == null) {
            // An import cycle reaches this location again while it is being translated. An empty module
            // answers that import, which ModuleImportGraph only reads the namespace of.
            this.modules.put(resolved, new LibraryModule(new Prolog(metadata), namespace, metadata));
            try {
                module = translate(location, importingContext, metadata);
                this.modules.put(resolved, module);
            } catch (IOException | RuntimeException e) {
                this.modules.remove(resolved);
                throw e;
            }
        }
        if (!namespace.equals(module.getNamespace())) {
            throw new ModuleNotFoundException(
                    "A module with namespace "
                            + namespace
                            + " was not found. The namespace of the module at this location was: "
                            + module.getNamespace(),
                    metadata);
        }
        return module;
    }

    private LibraryModule translate(URI location, StaticContext importingContext, ExceptionMetadata metadata)
            throws IOException {
        ModuleSourceLoader.ModuleSource source =
                ModuleSourceLoader.readModuleSource(location, this.configuration, metadata);
        return ModuleParser.parseLibraryModule(
                source.query(), source.systemId(), importingContext, this.configuration, this);
    }

    private static RumbleException moduleNotFound(String namespace, Exception cause, ExceptionMetadata metadata) {
        RumbleException exception = new ModuleNotFoundException(
                "Module not found: %s, cause: %s".formatted(namespace, cause != null ? cause.getMessage() : "unknown"),
                metadata);
        if (cause != null) {
            exception.initCause(cause);
        }
        return exception;
    }
}
