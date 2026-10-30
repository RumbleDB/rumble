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
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.resources.ResolvedResource;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

/** Loads module text while preserving resolved source locations and resolver ownership. */
final class ModuleSourceLoader {

    private ModuleSourceLoader() {}

    private static URI resolveStaticBaseUri(String url) {
        URI resolved = FileSystemUtil.resolveURIAgainstWorkingDirectory(url, ExceptionMetadata.EMPTY_METADATA);
        if (url != null && url.endsWith("/") && !resolved.toString().endsWith("/")) {
            resolved = URI.create(resolved + "/");
        }
        return resolved;
    }

    record ModuleSource(String query, URI systemId) {}

    static ModuleSource readModuleSource(
            URI location, CompilationConfiguration compilationConfiguration, ExceptionMetadata metadata)
            throws IOException {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        try (ResolvedResource resource =
                compilationConfiguration.resourceResolver().resolve(location, configuration, metadata)) {
            String query = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            return new ModuleSource(query, resource.getSystemId());
        }
    }

    /** The configured base URI applies to the main module, not to imported library modules. */
    static URI mainModuleLocation(URI sourceLocation, RumbleConfiguration configuration) {
        String url = configuration.semantics().staticBaseUri();
        return url == null ? sourceLocation : resolveStaticBaseUri(url);
    }

    static URI queryLocation(RumbleConfiguration configuration) {
        String url = configuration.semantics().staticBaseUri();
        return resolveStaticBaseUri(url == null ? "." : url);
    }
}
