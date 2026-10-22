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

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import java.util.stream.Stream;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

/** Resolves logical resource URIs to source content during compilation and execution. */
public final class ResourceResolver implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<URI, URI> mappings;
    private final Map<URI, ResourceCollection> collections;
    private final ResourceCollection defaultCollection;

    public ResourceResolver() {
        this(Map.of());
    }

    public ResourceResolver(Map<URI, URI> mappings) {
        this(mappings, Map.of(), null);
    }

    public ResourceResolver(
            Map<URI, URI> mappings, Map<URI, ResourceCollection> collections, ResourceCollection defaultCollection) {
        this.mappings = Map.copyOf(mappings);
        this.collections = Map.copyOf(collections);
        this.defaultCollection = defaultCollection;
    }

    public ResourceCollection collection(URI uri, ExceptionMetadata metadata) {
        ResourceCollection result = uri == null ? this.defaultCollection : this.collections.get(uri);
        if (result == null) {
            throw new CannotRetrieveResourceException(
                    uri == null ? "No default collection is defined." : "Collection " + uri + " is not defined.",
                    metadata);
        }
        return result;
    }

    /** Reuses collection document identity for fn:doc(document-uri($document)). */
    public Item collectionDocument(URI uri) {
        return Stream.concat(this.collections.values().stream(), Stream.ofNullable(this.defaultCollection))
                .flatMap(collection -> collection.items().stream())
                .filter(Item::isDocumentNode)
                .filter(document -> document.documentUri().stream()
                        .anyMatch(value -> uri.toString().equals(value.getStringValue())))
                .findFirst()
                .orElse(null);
    }

    public boolean hasMapping(URI requestedURI) {
        return this.mappings.containsKey(requestedURI);
    }

    public ResolvedResource resolve(URI requestedURI, RumbleConfiguration configuration, ExceptionMetadata metadata) {
        URI physicalURI = resolveURI(requestedURI);
        return new ResolvedResource(physicalURI, FileSystemUtil.getDataInputStream(physicalURI, metadata));
    }

    public URI resolveURI(URI requestedURI) {
        return this.mappings.getOrDefault(requestedURI, requestedURI);
    }
}
