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
import java.io.StringReader;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.SchemaImportException;
import org.rumbledb.expressions.module.SchemaImport;
import org.rumbledb.resources.ResolvedResource;

/** Loads the XSD 1.0 component model declared by an XQuery module's schema imports. */
public final class XmlSchemaCatalogLoader {

    private static final String EMPTY_SCHEMA = "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"/>";
    private static final String EMPTY_SCHEMA_SYSTEM_ID = "urn:rumbledb:built-in-schema-environment";

    private XmlSchemaCatalogLoader() {}

    public static Optional<XmlSchemaCatalog> load(
            List<SchemaImport> schemaImports, URI staticBaseUri, CompilationConfiguration compilationConfiguration) {
        Objects.requireNonNull(schemaImports, "schemaImports must not be null");
        Objects.requireNonNull(staticBaseUri, "staticBaseUri must not be null");
        Objects.requireNonNull(compilationConfiguration, "compilationConfiguration must not be null");
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

    /** Creates the schema environment used by strict/lax validation when no user schema is imported. */
    public static XmlSchemaCatalog loadBuiltInCatalog() {
        ExceptionMetadata metadata = ExceptionMetadata.EMPTY_METADATA;
        try {
            SchemaErrorHandler errorHandler = new SchemaErrorHandler();
            XMLSchemaFactory schemaFactory = new XMLSchemaFactory();
            schemaFactory.setErrorHandler(errorHandler);
            StreamSource source = new StreamSource(new StringReader(EMPTY_SCHEMA));
            source.setSystemId(EMPTY_SCHEMA_SYSTEM_ID);
            LoadedSchema loadedSchema = loadedSchema(schemaFactory.newSchema(source), errorHandler, metadata);
            return new XmlSchemaCatalog(loadedSchema.schemaModel(), loadedSchema.validationSchema());
        } catch (SchemaImportException exception) {
            throw exception;
        } catch (SAXException | RuntimeException exception) {
            throw new SchemaImportException(
                    "Unable to initialize the built-in XML Schema environment: " + exception.getMessage(),
                    metadata,
                    exception);
        }
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
            return loadedSchema(validationSchema, errorHandler, metadata);
        } catch (SchemaImportException exception) {
            throw exception;
        } catch (SAXException | RuntimeException exception) {
            throw new SchemaImportException(
                    "Unable to initialize or run the Xerces XML Schema loader: " + exception.getMessage(),
                    metadata,
                    exception);
        }
    }

    private static LoadedSchema loadedSchema(
            Schema validationSchema, SchemaErrorHandler errorHandler, ExceptionMetadata metadata) {
        if (errorHandler.getFirstError() != null) {
            throw new SchemaImportException(
                    "Unable to process an XML Schema: " + errorHandler.getFirstError(), metadata);
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
    }

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

        private Source resolveSource(URI location) {
            SchemaSource source = resolve(location);
            StreamSource result = new StreamSource(new ByteArrayInputStream(source.content()));
            result.setSystemId(source.systemId().toString());
            return result;
        }

        @Override
        public LSInput resolveResource(
                String type, String namespaceUri, String publicId, String systemId, String baseUri) {
            URI location = resolveLocation(namespaceUri, systemId, baseUri);
            return location == null ? null : toInput(publicId, resolve(location));
        }

        private URI resolveLocation(String namespaceUri, String systemId, String baseUri) {
            if (systemId != null && !systemId.isEmpty()) {
                URI base = baseUri == null || baseUri.isEmpty() ? this.defaultBaseUri : URI.create(baseUri);
                return URILiteralUtils.resolve(base, systemId, this.metadata);
            }
            List<URI> locations = this.locationsByNamespace.get(namespaceUri == null ? "" : namespaceUri);
            return locations == null || locations.isEmpty() ? null : locations.get(0);
        }

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

        private static LSInput toInput(String publicId, SchemaSource source) {
            return new DOMInputImpl(
                    publicId, source.systemId().toString(), null, new ByteArrayInputStream(source.content()), null);
        }
    }
}
