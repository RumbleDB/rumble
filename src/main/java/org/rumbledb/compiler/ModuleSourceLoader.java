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
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidEncodingException;
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

    // Pattern to match encoding declaration in XQuery/JSONiq files
    // Matches: xquery/jsoniq version "..." encoding "..." OR xquery/jsoniq encoding "..."
    private static final Pattern ENCODING_PATTERN = Pattern.compile(
            "^\\s*(?:xquery|jsoniq)\\s+(?:version\\s+['\"].*?['\"]\\s+)?encoding\\s+['\"]([A-Za-z][A-Za-z0-9._-]*)['\"]",
            Pattern.CASE_INSENSITIVE);

    /**
     * Extracts the encoding declaration from the query text if present.
     *
     * @param query The query text to parse
     * @return The declared encoding name, or empty if no encoding is declared
     */
    private static Optional<String> extractEncodingDeclaration(String query) {
        Matcher matcher = ENCODING_PATTERN.matcher(query);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    /**
     * Validates and returns a Charset for the given encoding name.
     *
     * @param encodingName The encoding name from the declaration
     * @param metadata Exception metadata for error reporting
     * @return The validated Charset
     * @throws InvalidEncodingException if the encoding name is invalid or unsupported
     */
    private static Charset validateEncoding(String encodingName, ExceptionMetadata metadata) {
        try {
            return Charset.forName(encodingName);
        } catch (IllegalCharsetNameException e) {
            throw new InvalidEncodingException("Invalid encoding name: " + encodingName, metadata);
        } catch (UnsupportedCharsetException e) {
            throw new InvalidEncodingException("Unsupported encoding: " + encodingName, metadata);
        }
    }

    static ModuleSource readModuleSource(
            URI location, CompilationConfiguration compilationConfiguration, ExceptionMetadata metadata)
            throws IOException {
        RumbleConfiguration configuration = compilationConfiguration.runtimeConfiguration();
        if (compilationConfiguration.resourceResolver().resolveURI(location).isOpaque()) {
            throw new CannotRetrieveResourceException("No module resource mapping for " + location, metadata);
        }

        // First pass: Read with UTF-8 to extract encoding declaration
        String initialQuery;
        URI systemId;
        try (ResolvedResource resource =
                compilationConfiguration.resourceResolver().resolve(location, configuration, metadata)) {
            initialQuery = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            systemId = resource.getSystemId();
        }

        // Check for encoding declaration
        Optional<String> declaredEncoding = extractEncodingDeclaration(initialQuery);
        if (declaredEncoding.isEmpty()) {
            // No encoding declared, use UTF-8 query
            return new ModuleSource(initialQuery, systemId);
        }

        // Validate the declared encoding
        Charset charset = validateEncoding(declaredEncoding.get(), metadata);

        // If declared encoding is UTF-8, no need to re-read
        if (charset.equals(StandardCharsets.UTF_8)) {
            return new ModuleSource(initialQuery, systemId);
        }

        // Second pass: Re-read with the declared encoding
        try (ResolvedResource resource =
                compilationConfiguration.resourceResolver().resolve(location, configuration, metadata)) {
            String query = IOUtils.toString(resource.getInputStream(), charset);
            return new ModuleSource(query, systemId);
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
