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
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.resources.ResolvedResource;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

/** Loads module text without replacing its source identity with a configured base URI. */
public final class ModuleSourceReader {
    private ModuleSourceReader() {}

    public static ModuleSource read(URI location, CompilationConfiguration configuration, ExceptionMetadata metadata)
            throws IOException {
        RumbleConfiguration runtime = configuration.runtimeConfiguration();
        try (ResolvedResource resource = configuration.resourceResolver().resolve(location, runtime, metadata)) {
            String text = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            URI sourceUri = resource.getSystemId();
            URI baseUri = runtime.semantics().staticBaseUri() == null
                    ? sourceUri
                    : resolveBaseUri(runtime.semantics().staticBaseUri());
            return new ModuleSource(text, sourceUri, baseUri);
        }
    }

    public static ModuleSource fromQuery(String query, CompilationConfiguration configuration) {
        String configuredBaseUri =
                configuration.runtimeConfiguration().semantics().staticBaseUri();
        URI baseUri = resolveBaseUri(configuredBaseUri == null ? "." : configuredBaseUri);
        // In-memory queries have no independently supplied source identity.
        return new ModuleSource(query, baseUri, baseUri);
    }

    private static URI resolveBaseUri(String value) {
        URI resolved = FileSystemUtil.resolveURIAgainstWorkingDirectory(value, ExceptionMetadata.EMPTY_METADATA);
        if (value.endsWith("/") && !resolved.toString().endsWith("/")) {
            resolved = URI.create(resolved + "/");
        }
        return resolved;
    }
}
