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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.model.QueryLanguage;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.JSONParser;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.items.parsing.XmlParsingUtils;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

/** Resolves logical resource URIs to source content during compilation and execution. */
public final class ResourceResolver implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<URI, Item> documents = new ConcurrentHashMap<>();

    private final Map<URI, List<URI>> directoryMembers = new ConcurrentHashMap<>();
    private final Map<URI, ResourceCollection> directoryCollections = new ConcurrentHashMap<>();

    private final Map<URI, URI> mappings;
    private final Map<URI, ResourceCollection> collections;
    private final ResourceCollection defaultCollection;

    /**
     * Creates a resolver with no registered resources.
     */
    public ResourceResolver() {
        this(Map.of());
    }

    /**
     * Creates a resolver with URI mappings and no registered collections.
     *
     * @param mappings logical URIs mapped to physical resource URIs
     */
    public ResourceResolver(Map<URI, URI> mappings) {
        this(mappings, Map.of(), null);
    }

    /**
     * Creates a resolver with the supplied mappings and collections.
     *
     * @param mappings logical URIs mapped to physical resource URIs
     * @param collections named collections keyed by their absolute URIs
     * @param defaultCollection the default collection, or {@code null} if none is defined
     */
    public ResourceResolver(
            Map<URI, URI> mappings, Map<URI, ResourceCollection> collections, ResourceCollection defaultCollection) {
        this.mappings = Map.copyOf(mappings);
        this.collections = Map.copyOf(collections);
        this.defaultCollection = defaultCollection;
    }

    /**
     * Starts a query with the same registered resources and fresh retrieval caches.
     *
     * @return a resolver for the new query
     */
    public ResourceResolver forQuery() {
        return new ResourceResolver(this.mappings, this.collections, this.defaultCollection);
    }

    /**
     * Returns a registered collection or loads and caches a local directory collection.
     *
     * @param uri the collection URI, or {@code null} for the default collection
     * @param queryLanguage the query language used to select parsing semantics
     * @param configuration the configuration governing resource parsing
     * @param metadata the query location used in error reports
     * @return the collection items and member URIs
     * @throws CannotRetrieveResourceException if the collection is undefined or a resource cannot be read
     */
    public ResourceCollection collection(
            URI uri, QueryLanguage queryLanguage, RumbleConfiguration configuration, ExceptionMetadata metadata) {
        ResourceCollection registered = registeredCollection(uri);
        if (registered != null) {
            return registered;
        }
        List<URI> members = directoryMembers(uri, metadata);
        return this.directoryCollections.computeIfAbsent(uri, key -> {
            List<Item> items = new ArrayList<>();
            for (URI member : members) {
                String path = member.getPath();
                if (path.endsWith(".xml")) {
                    items.add(loadXMLDocument(member, queryLanguage, configuration, metadata));
                } else if (path.endsWith(".json")) {
                    loadJsonFile(member, queryLanguage, configuration, items, metadata);
                } else if (path.endsWith(".jsonl")) {
                    loadJsonLinesFile(member, queryLanguage, configuration, items, metadata);
                } else {
                    // Ignore unsupported files, as in directory discovery.
                    continue;
                }
            }
            return new ResourceCollection(items, members);
        });
    }

    /**
     * Lists collection member URIs without loading their contents.
     *
     * @param uri the collection URI, or {@code null} for the default collection
     * @param metadata the query location used in error reports
     * @return the registered member URIs or the cached directory listing
     */
    public List<URI> collectionUris(URI uri, ExceptionMetadata metadata) {
        ResourceCollection registered = registeredCollection(uri);
        return registered == null ? directoryMembers(uri, metadata) : registered.uris();
    }

    /**
     * Looks up a registered collection.
     *
     * @param uri the collection URI, or {@code null} for the default collection
     * @return the registered collection, or {@code null} if none is defined
     */
    private ResourceCollection registeredCollection(URI uri) {
        return uri == null ? this.defaultCollection : this.collections.get(uri);
    }

    /**
     * Returns the directory listing cached for this query, discovering members on first access.
     *
     * @param uri the directory URI
     * @param metadata the query location used in error reports
     * @return the member URIs in sorted path order
     */
    private List<URI> directoryMembers(URI uri, ExceptionMetadata metadata) {
        if (uri == null) {
            throw new CannotRetrieveResourceException("No default collection is defined.", metadata);
        }
        return this.directoryMembers.computeIfAbsent(uri, key -> listDirectory(key, metadata));
    }

    /**
     * Lists immediate regular files with XML, JSON, or JSON Lines extensions in a local directory.
     *
     * @param uri the local directory URI
     * @param metadata the query location used in error reports
     * @return the supported member URIs in sorted path order
     * @throws CannotRetrieveResourceException if the URI is unsupported or the directory cannot be listed
     */
    private List<URI> listDirectory(URI uri, ExceptionMetadata metadata) {
        if (!uri.isOpaque()
                && "file".equalsIgnoreCase(uri.getScheme())
                && uri.getRawAuthority() == null
                && uri.getRawQuery() == null
                && uri.getRawFragment() == null) {
            try {
                Path directory = Path.of(uri);
                if (Files.isDirectory(directory)) {
                    try (Stream<Path> entries = Files.list(directory)) {
                        return entries.filter(Files::isRegularFile)
                                .filter(path -> {
                                    String name = path.getFileName().toString();
                                    return name.endsWith(".xml") || name.endsWith(".json") || name.endsWith(".jsonl");
                                })
                                .sorted()
                                .map(Path::toUri)
                                .toList();
                    }
                }
            } catch (IOException | UncheckedIOException | IllegalArgumentException exception) {
                CannotRetrieveResourceException error = new CannotRetrieveResourceException(
                        "Unable to list collection directory " + uri + ".", metadata);
                error.initCause(exception);
                throw error;
            }
        }
        throw new CannotRetrieveResourceException("Collection " + uri + " is not defined.", metadata);
    }

    /**
     * Reads a UTF-8 JSON file and appends its parsed value to the collection.
     *
     * @param uri the local JSON file URI
     * @param queryLanguage the query language used to select JSON parsing semantics
     * @param configuration the configuration supplying the XML character rules for JSON strings
     * @param items the destination list for parsed items
     * @param metadata the query location used in error reports
     */
    private void loadJsonFile(
            URI uri,
            QueryLanguage queryLanguage,
            RumbleConfiguration configuration,
            List<Item> items,
            ExceptionMetadata metadata) {
        try {
            appendJsonValue(
                    Files.readString(Path.of(uri)),
                    uri,
                    "JSON file " + uri,
                    queryLanguage,
                    configuration,
                    items,
                    metadata);
        } catch (IOException exception) {
            throw unreadableMember(uri, metadata, exception);
        }
    }

    /**
     * Reads a UTF-8 JSON Lines file and appends its parsed values in line order.
     *
     * @param uri the local JSON Lines file URI
     * @param queryLanguage the query language used to select JSON parsing semantics
     * @param configuration the configuration supplying the XML character rules for JSON strings
     * @param items the destination list for parsed items
     * @param metadata the query location used in error reports
     */
    private void loadJsonLinesFile(
            URI uri,
            QueryLanguage queryLanguage,
            RumbleConfiguration configuration,
            List<Item> items,
            ExceptionMetadata metadata) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(uri))) {
            String line;
            long lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                appendJsonValue(
                        line,
                        uri,
                        "JSON Lines file " + uri + ", line " + lineNumber,
                        queryLanguage,
                        configuration,
                        items,
                        metadata);
            }
        } catch (IOException exception) {
            throw unreadableMember(uri, metadata, exception);
        }
    }

    /**
     * Parses a JSON value and appends it when the language represents it as an item.
     *
     * @param text the JSON source text
     * @param uri the source file URI
     * @param description the input description used in parser errors
     * @param queryLanguage the query language used to select JSON parsing semantics
     * @param configuration the configuration supplying the XML character rules for JSON strings
     * @param items the destination list for parsed items
     * @param metadata the query location used in error reports
     */
    private void appendJsonValue(
            String text,
            URI uri,
            String description,
            QueryLanguage queryLanguage,
            RumbleConfiguration configuration,
            List<Item> items,
            ExceptionMetadata metadata) {
        boolean jsoniq10 = queryLanguage == QueryLanguage.JSONIQ_10;
        Item item = JSONParser.parseResource(
                text,
                JSONParsingOptions.defaultInstance(jsoniq10),
                configuration.semantics().xmlVersion(),
                jsoniq10,
                uri,
                description,
                metadata);
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * Creates a retrieval error for a collection member that could not be read.
     *
     * @param uri the member URI
     * @param metadata the query location used in error reports
     * @param cause the underlying read failure
     * @return the retrieval error with the original cause
     */
    private CannotRetrieveResourceException unreadableMember(URI uri, ExceptionMetadata metadata, IOException cause) {
        CannotRetrieveResourceException error =
                new CannotRetrieveResourceException("Unable to read collection member " + uri + ".", metadata);
        error.initCause(cause);
        return error;
    }

    /**
     * Returns an existing XML document node or parses and caches it.
     *
     * @param uri the logical document URI
     * @param queryLanguage the query language used to determine whitespace handling
     * @param configuration the configuration governing resource access and node construction
     * @param metadata the query location used in error reports
     * @return the document node associated with the URI
     */
    public Item loadXMLDocument(
            URI uri, QueryLanguage queryLanguage, RumbleConfiguration configuration, ExceptionMetadata metadata) {
        Item collectionDocument = document(uri);
        if (collectionDocument != null) {
            return collectionDocument;
        }
        try (ResolvedResource resource = resolve(uri, configuration, metadata)) {
            InputSource source = new InputSource(resource.getInputStream());
            source.setSystemId(resource.getSystemId().toString());
            Document xmlDocument = XmlParsingUtils.parseResource(
                    source, uri.toString(), "XML document \"" + uri + "\" for fn:doc()", metadata);
            // XQuery retains parsed whitespace; preserve JSONiq's existing whitespace handling.
            DocumentItem documentItem = ItemParser.getDocumentItemFromXML(
                    xmlDocument,
                    uri.toString(),
                    configuration.optimization().optimizeParentPointers(),
                    queryLanguage != null && queryLanguage.isXQuery());
            documentItem.setConstructionBaseUri(uri);
            documentItem.setDocumentUri(uri);
            return cacheDocument(uri, documentItem);
        } catch (IOException e) {
            CannotRetrieveResourceException ex =
                    new CannotRetrieveResourceException("Unable to read the resource supplied to fn:doc().", metadata);
            ex.initCause(e);
            throw ex;
        }
    }

    /**
     * Finds a document by its URI in the registered collections, including the default collection.
     *
     * @param uri the document URI
     * @return the matching document node, or {@code null} if none is registered
     */
    public Item collectionDocument(URI uri) {
        return Stream.concat(this.collections.values().stream(), Stream.ofNullable(this.defaultCollection))
                .flatMap(collection -> collection.items().stream())
                .filter(Item::isDocumentNode)
                .filter(document -> document.documentUri().stream()
                        .anyMatch(value -> uri.toString().equals(value.getStringValue())))
                .findFirst()
                .orElse(null);
    }

    /**
     * Looks up a document in registered collections and then in the retrieval cache.
     *
     * @param uri the document URI
     * @return the matching document node, or {@code null} if it has not been registered or cached
     */
    public Item document(URI uri) {
        Item collectionDocument = collectionDocument(uri);
        return collectionDocument == null ? this.documents.get(uri) : collectionDocument;
    }

    /**
     * Caches a retrieved document while preserving the identity of any previously cached node.
     *
     * @param uri the document URI used as the cache key
     * @param document the retrieved document node
     * @return the cached node for the URI
     */
    public Item cacheDocument(URI uri, Item document) {
        Item previous = this.documents.putIfAbsent(uri, document);
        return previous == null ? document : previous;
    }

    /**
     * Checks whether an explicit physical URI mapping exists.
     *
     * @param requestedURI the logical resource URI
     * @return whether the URI has an explicit mapping
     */
    public boolean hasMapping(URI requestedURI) {
        return this.mappings.containsKey(requestedURI);
    }

    /**
     * Opens a resource at its mapped physical URI.
     *
     * @param requestedURI the logical resource URI
     * @param configuration the runtime configuration, reserved for resource access settings
     * @param metadata the query location used in error reports
     * @return the physical URI and open stream, which the caller must close
     */
    public ResolvedResource resolve(URI requestedURI, RumbleConfiguration configuration, ExceptionMetadata metadata) {
        URI physicalURI = resolveURI(requestedURI);
        return new ResolvedResource(physicalURI, FileSystemUtil.getDataInputStream(physicalURI, metadata));
    }

    /**
     * Looks up the physical URI for a resource.
     *
     * @param requestedURI the logical resource URI
     * @return the mapped physical URI or the requested URI if it has no explicit mapping
     */
    public URI resolveURI(URI requestedURI) {
        return this.mappings.getOrDefault(requestedURI, requestedURI);
    }
}
