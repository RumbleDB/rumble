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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.PrologDeclaration;

/** Loads the modules imported by one compilation, each from its location only once. */
public final class ModuleImportLoader {

    private final CompilationConfiguration configuration;
    private final Map<URI, LibraryModule> modules = new LinkedHashMap<>();
    private int moduleOwners;

    ModuleImportLoader(CompilationConfiguration configuration) {
        this.configuration = configuration;
    }

    public String newModuleOwner() {
        return "module-" + this.moduleOwners++;
    }

    public boolean hasModules(String namespace) {
        return !modulesOf(namespace).isEmpty();
    }

    /**
     * Gives the root prolog the declarations of every module it reaches, each keeping its module's context. This keeps
     * import cycles out of the tree, and lets dependency ordering work across module boundaries.
     */
    void flattenImports(Prolog rootProlog, StaticContext rootContext) {
        flattenImports(rootProlog, rootContext, null);
    }

    /** A library analyzed on its own is loaded again if an import cycle leads back to its location. */
    void flattenImports(Prolog rootProlog, StaticContext rootContext, URI rootLocation) {
        // Only what the root reaches: failed location hints can leave unrelated modules behind.
        Set<String> reachable = new HashSet<>();
        collectReachable(rootContext, reachable);
        URI resolvedRoot = rootLocation == null
                ? null
                : this.configuration.resourceResolver().resolveURI(rootLocation).normalize();
        List<PrologDeclaration> declarations = new ArrayList<>();
        this.modules.forEach((location, module) -> {
            if (reachable.contains(module.getNamespace()) && !location.equals(resolvedRoot)) {
                declarations.addAll(module.getProlog().getDeclarations());
            }
        });
        declarations.addAll(rootProlog.getDeclarations());
        rootProlog.setDeclarations(declarations);
    }

    private void collectReachable(StaticContext importer, Set<String> reachable) {
        importer.getImportedNamespaces().forEach((namespace, metadata) -> {
            if (reachable.add(namespace)) {
                // Every hint has been followed by now, so a namespace still without modules is its own location.
                if (modulesOf(namespace).isEmpty()) {
                    load(namespace, List.of(namespace), importer, metadata);
                }
                modulesOf(namespace).forEach(module -> collectReachable(module.getStaticContext(), reachable));
            }
        });
    }

    private List<LibraryModule> modulesOf(String namespace) {
        return this.modules.values().stream()
                .filter(module -> namespace.equals(module.getNamespace()))
                .toList();
    }

    /** Loads every location of the namespace, as modules sharing a namespace are compiled together. */
    public void load(
            String namespace, List<String> locationHints, StaticContext importingContext, ExceptionMetadata metadata) {
        List<URI> locations;
        try {
            locations = locationHints.stream()
                    .map(hint -> URILiteralUtils.resolve(importingContext.getStaticBaseURI(), hint, metadata))
                    .toList();
        } catch (RumbleException e) {
            throw moduleNotFound(namespace, e, metadata);
        }
        boolean loaded = false;
        Exception lastFailure = null;
        for (URI location : locations) {
            try {
                loadLocation(location, namespace, importingContext, metadata);
                loaded = true;
            } catch (IOException | CannotRetrieveResourceException | ModuleNotFoundException e) {
                lastFailure = e;
            }
        }
        if (!loaded) {
            throw moduleNotFound(namespace, lastFailure, metadata);
        }
    }

    private void loadLocation(
            URI location, String namespace, StaticContext importingContext, ExceptionMetadata metadata)
            throws IOException {
        URI resolved =
                this.configuration.resourceResolver().resolveURI(location).normalize();
        LibraryModule module = this.modules.get(resolved);
        if (module == null) {
            // An import cycle reaches this location again while it is being translated. An empty module
            // answers that import, and translation replaces it before flattening.
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
                "Module not found: %s, cause: %s".formatted(namespace, cause.getMessage()), metadata);
        exception.initCause(cause);
        return exception;
    }
}
