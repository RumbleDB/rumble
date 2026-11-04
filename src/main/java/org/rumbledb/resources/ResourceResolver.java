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

    /** Starts a query with the same registered resources and fresh retrieval caches. */
    public ResourceResolver forQuery() {
        return new ResourceResolver(this.mappings, this.collections, this.defaultCollection);
    }

    public ResourceCollection collection(
            URI uri, String queryLanguage, RumbleConfiguration configuration, ExceptionMetadata metadata) {
        ResourceCollection registered = registeredCollection(uri);
        if (registered != null) {
            return registered;
        }
        List<URI> members = directoryMembers(uri, metadata);
        return this.directoryCollections.computeIfAbsent(uri, key -> {
            List<Item> items = new ArrayList<>();
            for (URI member : members) {
                if (member.getPath().endsWith(".xml")) {
                    items.add(loadDocument(member, queryLanguage, configuration, metadata));
                } else {
                    loadJsonFile(member, queryLanguage, configuration, items, metadata);
                }
            }
            return new ResourceCollection(items, members);
        });
    }

    public List<URI> collectionUris(URI uri, ExceptionMetadata metadata) {
        ResourceCollection registered = registeredCollection(uri);
        return registered == null ? directoryMembers(uri, metadata) : registered.uris();
    }

    private ResourceCollection registeredCollection(URI uri) {
        return uri == null ? this.defaultCollection : this.collections.get(uri);
    }

    private List<URI> directoryMembers(URI uri, ExceptionMetadata metadata) {
        if (uri == null) {
            throw new CannotRetrieveResourceException("No default collection is defined.", metadata);
        }
        return this.directoryMembers.computeIfAbsent(uri, key -> listDirectory(key, metadata));
    }

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

    private void loadJsonFile(
            URI uri,
            String queryLanguage,
            RumbleConfiguration configuration,
            List<Item> items,
            ExceptionMetadata metadata) {
        boolean jsoniq10 = "jsoniq10".equals(queryLanguage);
        JSONParsingOptions options = JSONParsingOptions.defaultInstance(jsoniq10);
        String xmlVersion = configuration.semantics().xmlVersion();
        Path file = Path.of(uri);
        try {
            if (file.getFileName().toString().endsWith(".jsonl")) {
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    long lineNumber = 0;
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        Item item = JSONParser.parseResource(
                                line,
                                options,
                                xmlVersion,
                                jsoniq10,
                                uri,
                                "JSON Lines file " + uri + ", line " + lineNumber,
                                metadata);
                        if (item != null) {
                            items.add(item);
                        }
                    }
                }
            } else {
                Item item = JSONParser.parseResource(
                        Files.readString(file), options, xmlVersion, jsoniq10, uri, "JSON file " + uri, metadata);
                if (item != null) {
                    items.add(item);
                }
            }
        } catch (IOException exception) {
            CannotRetrieveResourceException error =
                    new CannotRetrieveResourceException("Unable to read collection member " + uri + ".", metadata);
            error.initCause(exception);
            throw error;
        }
    }

    public Item loadDocument(
            URI uri, String queryLanguage, RumbleConfiguration configuration, ExceptionMetadata metadata) {
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
                    queryLanguage != null && queryLanguage.startsWith("xquery"));
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

    /** Reuses retrieved documents so their document URIs resolve to the same node. */
    public Item document(URI uri) {
        Item collectionDocument = collectionDocument(uri);
        return collectionDocument == null ? this.documents.get(uri) : collectionDocument;
    }

    public Item cacheDocument(URI uri, Item document) {
        Item previous = this.documents.putIfAbsent(uri, document);
        return previous == null ? document : previous;
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
