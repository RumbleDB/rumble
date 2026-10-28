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
package org.rumbledb.xml.schema;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
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

    // Make xml:id visible to Xerces's ID table even when no user schema declares it.
    private static final String XML_ID_SCHEMA = """
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
                   targetNamespace="http://www.w3.org/XML/1998/namespace">
          <xs:attribute name="id" type="xs:ID"/>
        </xs:schema>
        """;
    private static final String XML_ID_SCHEMA_SYSTEM_ID = "urn:rumbledb:xml-id-schema";

    private XmlSchemaCatalogLoader() {}

    public static XmlSchemaCatalog loadBuiltInCatalog() {
        ExceptionMetadata metadata = ExceptionMetadata.EMPTY_METADATA;
        try {
            SchemaErrorHandler errorHandler = new SchemaErrorHandler();
            XMLSchemaFactory schemaFactory = new XMLSchemaFactory();
            schemaFactory.setErrorHandler(errorHandler);

            // Xerces also supplies the standard built-in types such as xs:anyType.
            // This allows lax and strict validation to run even when the query has no schema imports.
            StreamSource source = new StreamSource(new StringReader(XML_ID_SCHEMA));
            source.setSystemId(XML_ID_SCHEMA_SYSTEM_ID);

            LoadedSchema loadedSchema = loadedSchema(schemaFactory.newSchema(source), errorHandler, metadata);
            return new XmlSchemaCatalog(loadedSchema.schemaModel(), loadedSchema.validationSchema(), Map.of());
        } catch (SAXException | RuntimeException exception) {
            throw new SchemaImportException(
                    "Unable to initialize the built-in XML Schema environment: " + exception.getMessage(),
                    metadata,
                    exception);
        }
    }

    /** Supplies the standard JSON XML schema without importing its types into the query's static context. */
    public static XmlSchemaCatalog loadXPathFunctionsCatalog() {
        LoadedSchema schema = XPathFunctionsSchemaHolder.SCHEMA;
        // Type mappers contain mutable caches, so each validation gets its own catalog.
        return new XmlSchemaCatalog(schema.schemaModel(), schema.validationSchema(), Map.of(Name.FN_NS, "fn"));
    }

    private static final class XPathFunctionsSchemaHolder {
        private static final LoadedSchema SCHEMA = loadXPathFunctionsSchema();
    }

    private static LoadedSchema loadXPathFunctionsSchema() {
        ExceptionMetadata metadata = ExceptionMetadata.EMPTY_METADATA;
        try (InputStream input = XmlSchemaCatalogLoader.class.getResourceAsStream("/schemas/xpath-functions.xsd")) {
            if (input == null) {
                throw new SchemaImportException("The bundled XPath functions schema is unavailable.", metadata);
            }
            SchemaErrorHandler errorHandler = new SchemaErrorHandler();
            XMLSchemaFactory factory = new XMLSchemaFactory();
            factory.setErrorHandler(errorHandler);
            StreamSource source = new StreamSource(input);
            source.setSystemId(Name.FN_NS);
            return loadedSchema(factory.newSchema(source), errorHandler, metadata);
        } catch (SAXException | IOException exception) {
            throw new SchemaImportException(
                    "Unable to initialize the XPath functions schema: " + exception.getMessage(), metadata, exception);
        }
    }

    public static Optional<XmlSchemaCatalog> load(
            @NonNull List<SchemaImport> schemaImports,
            @NonNull URI staticBaseUri,
            @NonNull CompilationConfiguration compilationConfiguration) {
        return load(schemaImports, staticBaseUri, compilationConfiguration, uri -> {});
    }

    /**
     * Reports each schema URI before reading it, including nested imports and includes.
     * Failed reads are reported too, so callers can detect when a missing schema becomes available.
     */
    public static Optional<XmlSchemaCatalog> load(
            @NonNull List<SchemaImport> schemaImports,
            @NonNull URI staticBaseUri,
            @NonNull CompilationConfiguration compilationConfiguration,
            @NonNull Consumer<URI> schemaResourceObserver) {
        if (schemaImports.isEmpty()) {
            return Optional.empty();
        }

        ResolvedImports resolvedImports = resolveLocations(schemaImports, staticBaseUri);
        if (resolvedImports.locations().isEmpty()) {
            return Optional.empty();
        }

        ExceptionMetadata metadata = schemaImports.get(0).getMetadata();
        SchemaResourceResolver resolver = new SchemaResourceResolver(
                staticBaseUri,
                resolvedImports.locationsByNamespace(),
                compilationConfiguration,
                metadata,
                schemaResourceObserver);
        LoadedSchema loadedSchema = loadSchema(resolvedImports.locations(), resolver, metadata);
        Map<String, String> namespacePrefixes = new LinkedHashMap<>();
        for (SchemaImport schemaImport : schemaImports) {
            if (schemaImport.getBindingKind() == SchemaImport.BindingKind.PREFIX) {
                namespacePrefixes.put(schemaImport.getTargetNamespace(), schemaImport.getPrefix());
            }
        }
        XmlSchemaCatalog catalog =
                new XmlSchemaCatalog(loadedSchema.schemaModel(), loadedSchema.validationSchema(), namespacePrefixes);
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
            return loadSchema(locations, resolver, metadata, new XMLSchemaFactory());
        } catch (SchemaImportException exception) {
            throw exception;
        } catch (SAXException | RuntimeException exception) {
            throw new SchemaImportException(
                    "Unable to initialize or run the Xerces XML Schema loader: " + exception.getMessage(),
                    metadata,
                    exception);
        }
    }

    private static LoadedSchema loadSchema(
            List<URI> locations,
            SchemaResourceResolver resolver,
            ExceptionMetadata metadata,
            javax.xml.validation.SchemaFactory schemaFactory)
            throws SAXException {
        SchemaErrorHandler errorHandler = new SchemaErrorHandler();
        // Multiple location hints may contribute declarations to the same target namespace.
        schemaFactory.setFeature("http://apache.org/xml/features/namespace-growth", true);
        schemaFactory.setResourceResolver(resolver);
        schemaFactory.setErrorHandler(errorHandler);
        List<Source> sources = new ArrayList<>(locations.stream().map(resolver::resolveSource).toList());
        LoadedSchema loaded = loadedSchema(schemaFactory.newSchema(sources.toArray(Source[]::new)), errorHandler, metadata);
        if (loaded.schemaModel().getAttributeDeclaration("id", Name.XML_NS) != null) {
            return loaded;
        }
        // Preserve explicit XML namespace declarations, adding only a missing xml:id declaration.
        // Resolve fresh streams because Xerces consumed the first set while compiling the user schemas.
        sources = new ArrayList<>(locations.stream().map(resolver::resolveSource).toList());
        StreamSource xmlId = new StreamSource(new StringReader(XML_ID_SCHEMA));
        xmlId.setSystemId(XML_ID_SCHEMA_SYSTEM_ID);
        sources.add(xmlId);
        return loadedSchema(schemaFactory.newSchema(sources.toArray(Source[]::new)), errorHandler, metadata);
    }

    private static LoadedSchema loadedSchema(
            Schema validationSchema, SchemaErrorHandler errorHandler, ExceptionMetadata metadata) {
        if (errorHandler.getFirstError() != null) {
            throw new SchemaImportException(
                    "Unable to process an imported XML Schema: " + errorHandler.getFirstError(), metadata);
        }
        if (!(validationSchema instanceof XSGrammarPoolContainer grammarPoolContainer)) {
            throw new SchemaImportException("The Xerces validation grammar pool is unavailable.", metadata);
        }

        // Grammar pool is the collection of compiled XML Schema definitions
        // We convert it into an XSModel, which will be used for validation and type checking.
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
        private final Consumer<URI> schemaResourceObserver;

        private SchemaResourceResolver(
                URI defaultBaseUri,
                Map<String, List<URI>> locationsByNamespace,
                CompilationConfiguration compilationConfiguration,
                ExceptionMetadata metadata,
                Consumer<URI> schemaResourceObserver) {
            this.defaultBaseUri = defaultBaseUri;
            this.locationsByNamespace = locationsByNamespace;
            this.compilationConfiguration = compilationConfiguration;
            this.metadata = metadata;
            this.sources = new HashMap<>();
            this.schemaResourceObserver = schemaResourceObserver;
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
            this.schemaResourceObserver.accept(location);
            try (ResolvedResource resource = resolveResource(location)) {
                return new SchemaSource(
                        resource.getSystemId(), resource.getInputStream().readAllBytes());
            } catch (IOException | RumbleException exception) {
                throw new SchemaImportException(
                        "Unable to read imported XML Schema " + location + ".", this.metadata, exception);
            }
        }

        private ResolvedResource resolveResource(URI location) {
            var resolver = this.compilationConfiguration.resourceResolver();
            if (Name.FN_NS.equals(location.toString()) && !resolver.hasMapping(location)) {
                InputStream schema = XmlSchemaCatalogLoader.class.getResourceAsStream("/schemas/xpath-functions.xsd");
                if (schema == null) {
                    throw new SchemaImportException(
                            "The bundled XPath functions schema is unavailable.", this.metadata);
                }
                return new ResolvedResource(location, schema);
            }
            return resolver.resolve(location, this.compilationConfiguration.runtimeConfiguration(), this.metadata);
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
