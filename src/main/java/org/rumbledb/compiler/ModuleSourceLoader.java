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

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

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

    private static Charset declaredCharset(String encodingName, ExceptionMetadata metadata) {
        try {
            return Charset.forName(encodingName);
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

        try (ResolvedResource resource =
                compilationConfiguration.resourceResolver().resolve(location, configuration, metadata)) {
            BOMInputStream stream = BOMInputStream.builder()
                    .setInputStream(resource.getInputStream())
                    .setByteOrderMarks(
                            ByteOrderMark.UTF_8,
                            ByteOrderMark.UTF_16BE,
                            ByteOrderMark.UTF_16LE,
                            ByteOrderMark.UTF_32BE,
                            ByteOrderMark.UTF_32LE)
                    .get();
            String bomCharset = stream.getBOMCharsetName();
            byte[] bytes = stream.readAllBytes();
            URI systemId = resource.getSystemId();

            // A BOM identifies the charset and, for UTF-16 and UTF-32, the byte order.
            if (bomCharset != null) {
                return new ModuleSource(new String(bytes, Charset.forName(bomCharset)), systemId);
            }

            String utf8Query = new String(bytes, StandardCharsets.UTF_8);
            Charset charset = ModuleParser.declaredEncoding(utf8Query, systemId, configuration)
                    .map(encoding -> declaredCharset(encoding, metadata))
                    .orElse(StandardCharsets.UTF_8);
            String query = charset.equals(StandardCharsets.UTF_8) ? utf8Query : new String(bytes, charset);
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
