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
package org.rumbledb.resources;

import java.net.URI;
import java.util.Map;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

/** Resolves logical resource URIs to source content used during query compilation. */
public final class ResourceResolver {

    private final Map<URI, URI> mappings;

    public ResourceResolver() {
        this(Map.of());
    }

    public ResourceResolver(Map<URI, URI> mappings) {
        this.mappings = Map.copyOf(mappings);
    }

    public ResolvedResource resolve(URI requestedURI, RumbleConfiguration configuration, ExceptionMetadata metadata) {
        URI physicalURI = this.mappings.getOrDefault(requestedURI, requestedURI);
        return new ResolvedResource(physicalURI, FileSystemUtil.getDataInputStream(physicalURI, metadata));
    }
}
