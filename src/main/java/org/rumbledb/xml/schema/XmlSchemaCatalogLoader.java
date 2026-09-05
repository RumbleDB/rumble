/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.xml.schema;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xs.XSModel;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import lombok.NonNull;

import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.SchemaImportException;
import org.rumbledb.expressions.module.SchemaImport;
import org.rumbledb.resources.ResolvedResource;

/**
 * Loads the XSD 1.0 component model declared by an XQuery module's schema
 * imports.
 */
public final class XmlSchemaCatalogLoader {

    private XmlSchemaCatalogLoader() {}

    public static Optional<XmlSchemaCatalog> load(
            @NonNull List<SchemaImport> schemaImports,
            @NonNull URI staticBaseUri,
            @NonNull CompilationConfiguration compilationConfiguration) {
        if (schemaImports.isEmpty()) {
            return Optional.empty();
        }

        ResolvedImports resolvedImports = resolveLocations(schemaImports, staticBaseUri);
        if (resolvedImports.locations().isEmpty()) {
            return Optional.empty();
        }

        ExceptionMetadata metadata = schemaImports.get(0).getMetadata();
        SchemaResourceResolver resolver = new SchemaResourceResolver(
                staticBaseUri, resolvedImports.locationsByNamespace(), compilationConfiguration, metadata);
        LoadedSchema loadedSchema = loadSchema(resolvedImports.locations(), resolver, metadata);
        XmlSchemaCatalog catalog = new XmlSchemaCatalog(loadedSchema.schemaModel(), loadedSchema.validationSchema());
        verifyImportedNamespaces(schemaImports, catalog);

        return Optional.of(catalog);
    }

    private static ResolvedImports resolveLocations(List<SchemaImport> schemaImports, URI staticBaseUri) {
        Map<String, List<URI>> locationsByNamespace = new LinkedHashMap<>();
        for (SchemaImport schemaImport : schemaImports) {
            List<String> hints = schemaImport.getLocationHints();
            if (hints.isEmpty()) {
                if (Name.XS_NS.equals(schemaImport.getTargetNamespace())) {
                    continue;
                }
                hints = List.of(schemaImport.getTargetNamespace());
            }
            locationsByNamespace.put(
                    schemaImport.getTargetNamespace(),
                    hints.stream()
                            .map(hint -> URILiteralUtils.resolve(staticBaseUri, hint, schemaImport.getMetadata()))
                            .toList());
        }
        List<URI> locations = locationsByNamespace.values().stream()
                .flatMap(List::stream)
                .distinct()
                .toList();
        return new ResolvedImports(Map.copyOf(locationsByNamespace), locations);
    }

    private static LoadedSchema loadSchema(
            List<URI> locations, SchemaResourceResolver resolver, ExceptionMetadata metadata) {
        try {
            SchemaErrorHandler errorHandler = new SchemaErrorHandler();
            XMLSchemaFactory schemaFactory = new XMLSchemaFactory();
            schemaFactory.setResourceResolver(resolver);
            schemaFactory.setErrorHandler(errorHandler);
            Source[] sources = locations.stream().map(resolver::resolveSource).toArray(Source[]::new);
            Schema validationSchema = schemaFactory.newSchema(sources);

            if (errorHandler.getFirstError() != null) {
                throw new SchemaImportException(
                        "Unable to process an imported XML Schema: " + errorHandler.getFirstError(), metadata);
            }
            if (!(validationSchema instanceof XSGrammarPoolContainer grammarPoolContainer)) {
                throw new SchemaImportException("The Xerces validation grammar pool is unavailable.", metadata);
            }
            Grammar[] grammars =
                    grammarPoolContainer.getGrammarPool().retrieveInitialGrammarSet(XMLGrammarDescription.XML_SCHEMA);
            XSGrammar[] schemaGrammars = new XSGrammar[grammars.length];
            for (int index = 0; index < grammars.length; index++) {
                if (!(grammars[index] instanceof XSGrammar schemaGrammar)) {
                    throw new SchemaImportException("Xerces returned a non-schema validation grammar.", metadata);
                }
                schemaGrammars[index] = schemaGrammar;
            }
            if (schemaGrammars.length == 0) {
                throw new SchemaImportException("Unable to build the XML Schema component model.", metadata);
            }
            XSModel schemaModel = schemaGrammars[0].toXSModel(schemaGrammars);
            return new LoadedSchema(validationSchema, schemaModel);
        } catch (SchemaImportException exception) {
            throw exception;
        } catch (SAXException | RuntimeException exception) {
            throw new SchemaImportException(
                    "Unable to initialize or run the Xerces XML Schema loader: " + exception.getMessage(),
                    metadata,
                    exception);
        }
    }

    /**
     * Verifies that each imported namespace actually occurs in the final catalog.
     * Otherwise it raises a schema-import
     * error.
     */
    private static void verifyImportedNamespaces(List<SchemaImport> schemaImports, XmlSchemaCatalog catalog) {
        for (SchemaImport schemaImport : schemaImports) {
            String namespace = schemaImport.getTargetNamespace();
            if (Name.XS_NS.equals(namespace) && schemaImport.getLocationHints().isEmpty()) {
                continue;
            }
            if (!catalog.containsNamespace(namespace)) {
                throw new SchemaImportException(
                        "No XML Schema with target namespace " + namespace + " was found.", schemaImport.getMetadata());
            }
        }
    }

    private record ResolvedImports(Map<String, List<URI>> locationsByNamespace, List<URI> locations) {}

    private record SchemaSource(URI systemId, byte[] content) {}

    private record LoadedSchema(Schema validationSchema, XSModel schemaModel) {}

    private static final class SchemaErrorHandler implements ErrorHandler {

        private String firstError;

        @Override
        public void warning(SAXParseException exception) {}

        @Override
        public void error(SAXParseException exception) {
            if (this.firstError == null) {
                this.firstError = exception.getMessage();
            }
        }

        @Override
        public void fatalError(SAXParseException exception) {
            error(exception);
        }

        private String getFirstError() {
            return this.firstError;
        }
    }

    /**
     * Custom loader for XML Schema resources that ensures Xerces reads resources
     * through RumbleDB’s own
     * ResourceResolver, rather than directly from the file system or network.
     */
    private static final class SchemaResourceResolver implements LSResourceResolver {

        private final URI defaultBaseUri;
        private final Map<String, List<URI>> locationsByNamespace;
        private final CompilationConfiguration compilationConfiguration;
        private final ExceptionMetadata metadata;
        private final Map<URI, SchemaSource> sources;

        private SchemaResourceResolver(
                URI defaultBaseUri,
                Map<String, List<URI>> locationsByNamespace,
                CompilationConfiguration compilationConfiguration,
                ExceptionMetadata metadata) {
            this.defaultBaseUri = defaultBaseUri;
            this.locationsByNamespace = locationsByNamespace;
            this.compilationConfiguration = compilationConfiguration;
            this.metadata = metadata;
            this.sources = new HashMap<>();
        }

        /**
         * Loads an initial schema given its URI.
         */
        private Source resolveSource(URI location) {
            SchemaSource source = resolve(location);
            StreamSource result = new StreamSource(new ByteArrayInputStream(source.content()));
            result.setSystemId(source.systemId().toString());
            return result;
        }

        /**
         * Xerces callback for nested xs:include or xs:import.
         */
        @Override
        public LSInput resolveResource(
                String type, String namespaceUri, String publicId, String systemId, String baseUri) {
            URI location = resolveLocation(namespaceUri, systemId, baseUri);
            return location == null ? null : toInput(publicId, resolve(location));
        }

        /**
         * Resolves the location of an imported schema.
         * <p>
         * if Xerces gives a systemId, resolve it relative to baseUri—this handles
         * relative includes;
         * otherwise, choose the configured location for the requested namespace—this
         * handles namespace-only imports.
         */
        private URI resolveLocation(String namespaceUri, String systemId, String baseUri) {
            if (systemId != null && !systemId.isEmpty()) {
                URI base = baseUri == null || baseUri.isEmpty() ? this.defaultBaseUri : URI.create(baseUri);
                return URILiteralUtils.resolve(base, systemId, this.metadata);
            }
            List<URI> locations = this.locationsByNamespace.get(namespaceUri == null ? "" : namespaceUri);
            return locations == null || locations.isEmpty() ? null : locations.get(0);
        }

        /**
         * Reads and caches a schema.
         */
        private SchemaSource resolve(URI location) {
            SchemaSource cached = this.sources.get(location);
            if (cached != null) {
                return cached;
            }
            SchemaSource source = read(location);
            this.sources.put(location, source);
            this.sources.putIfAbsent(source.systemId(), source);
            return source;
        }

        /**
         * Reads a schema from the given URI using RumbleDB’s ResourceResolver.
         */
        private SchemaSource read(URI location) {
            try (ResolvedResource resource = this.compilationConfiguration
                    .resourceResolver()
                    .resolve(location, this.compilationConfiguration.runtimeConfiguration(), this.metadata)) {
                return new SchemaSource(
                        resource.getSystemId(), resource.getInputStream().readAllBytes());
            } catch (IOException | RumbleException exception) {
                throw new SchemaImportException(
                        "Unable to read imported XML Schema " + location + ".", this.metadata, exception);
            }
        }

        /**
         * Converts a SchemaSource to an LSInput.
         */
        private static LSInput toInput(String publicId, SchemaSource source) {
            return new DOMInputImpl(
                    publicId, source.systemId().toString(), null, new ByteArrayInputStream(source.content()), null);
        }
    }
}
