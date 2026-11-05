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
import java.util.HashMap;
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

    private ModuleImportLoader() {}

    private static final ThreadLocal<Map<String, StaticContext>> ACTIVE_IMPORTS = ThreadLocal.withInitial(HashMap::new);

    /** Active imports are linked by context, without repeating their declaration trees. */
    public static boolean isActive(String namespace) {
        return ACTIVE_IMPORTS.get().containsKey(namespace);
    }

    public static void registerActiveContext(String namespace, StaticContext context) {
        if (isActive(namespace)) {
            ACTIVE_IMPORTS.get().put(namespace, context);
        }
    }

    public static LibraryModule activeReference(String namespace, ExceptionMetadata metadata) {
        LibraryModule reference =
                new LibraryModule(new Prolog(List.of(), List.of(), List.of(), metadata), namespace, metadata);
        reference.setStaticContext(ACTIVE_IMPORTS.get().get(namespace));
        reference.setImportBackReference(true);
        return reference;
    }

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
                LibraryModule module;
                Map<String, StaticContext> active = ACTIVE_IMPORTS.get();
                active.put(normalizedNamespace, null);
                try {
                    module = CompilationPipeline.prepareLibraryModuleFromLocation(
                            location, importingModuleContext, compilationConfiguration, metadata);
                } finally {
                    active.remove(normalizedNamespace);
                    if (active.isEmpty()) {
                        ACTIVE_IMPORTS.remove();
                    }
                }

                if (!normalizedNamespace.equals(module.getNamespace())) {
                    throw new ModuleNotFoundException(
                            "A module with namespace "
                                    + normalizedNamespace
                                    + " was not found. The namespace of the module at this location was: "
                                    + module.getNamespace(),
                            metadata);
                }

                return module;
            } catch (IOException | CannotRetrieveResourceException | ModuleNotFoundException e) {
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
