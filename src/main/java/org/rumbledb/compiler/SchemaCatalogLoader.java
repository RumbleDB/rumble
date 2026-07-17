/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.compiler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.SchemaCatalog;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.SchemaImportException;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.SchemaImport;
import org.rumbledb.resources.ResolvedResource;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.xml.schema.XmlSchemaConstructorFunction;
import org.rumbledb.xml.schema.XmlSchemaTypeMapper;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

final class SchemaCatalogLoader {

    private SchemaCatalogLoader() {
    }

    static void load(
            Prolog prolog,
            StaticContext context,
            CompilationConfiguration compilationConfiguration
    ) {
        List<SchemaImport> imports = prolog.getSchemaImports();
        ExceptionMetadata metadata = imports.isEmpty()
            ? ExceptionMetadata.EMPTY_METADATA
            : imports.get(0).getMetadata();
        Map<String, List<URI>> locationsByNamespace = resolveLocations(imports, context.getStaticBaseURI());
        List<URI> locations = locationsByNamespace.values().stream().flatMap(List::stream).distinct().toList();

        try {
            if (locations.isEmpty()) {
                SchemaCatalog catalog = SchemaCatalog.builtIn();
                context.setSchemaCatalog(catalog);
                registerConstructorFunctions(context, catalog.schemaModel(), new XmlSchemaTypeMapper());
                return;
            }
            XMLSchemaFactory factory = new XMLSchemaFactory();
            SchemaResourceResolver resolver = new SchemaResourceResolver(
                    context.getStaticBaseURI(),
                    locationsByNamespace,
                    compilationConfiguration,
                    metadata
            );
            factory.setResourceResolver(resolver);
            Source[] sources = locations.stream().map(resolver::resolveSource).toArray(Source[]::new);
            Schema schema = factory.newSchema(sources);
            XSModel schemaModel = loadSchemaModel(locations, resolver, metadata);
            verifyImportedNamespaces(imports, schemaModel);

            context.setSchemaCatalog(new SchemaCatalog(schema, schemaModel, resolver.getSchemaDocuments()));
            XmlSchemaTypeMapper typeMapper = new XmlSchemaTypeMapper();
            registerGeneralizedAtomicTypes(context, schemaModel, metadata, typeMapper);
            registerConstructorFunctions(context, schemaModel, typeMapper);
        } catch (SAXException exception) {
            throw new SchemaImportException(
                    "Unable to process imported schemas: " + exception.getMessage(),
                    metadata,
                    exception
            );
        }
    }

    private static void registerConstructorFunctions(
            StaticContext context,
            XSModel schemaModel,
            XmlSchemaTypeMapper typeMapper
    ) {
        Map<FunctionIdentifier, XmlSchemaConstructorFunction> constructors = new HashMap<>();
        XSNamedMap schemaTypes = schemaModel.getComponents(XSConstants.TYPE_DEFINITION);
        for (int index = 0; index < schemaTypes.getLength(); index++) {
            XSTypeDefinition schemaType = (XSTypeDefinition) schemaTypes.item(index);
            if (schemaType.getName() == null) {
                continue;
            }
            Name name = new Name(schemaType.getNamespace(), null, schemaType.getName());
            FunctionIdentifier identifier = new FunctionIdentifier(name, 1);
            XmlSchemaConstructorFunction constructor = XmlSchemaConstructorFunction.resolve(identifier, context);
            if (constructor == null) {
                constructor = typeMapper.getListItemType(schemaType)
                    .map(
                        itemType -> XmlSchemaConstructorFunction.createList(
                            identifier,
                            itemType,
                            context.getSchemaCatalog(),
                            schemaType
                        )
                    )
                    .orElse(null);
            }
            if (
                constructor == null
                    && schemaType instanceof XSSimpleTypeDefinition simpleType
                    && simpleType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION
            ) {
                constructor = XmlSchemaConstructorFunction.createUnion(
                    identifier,
                    typeMapper.getUnionAtomicMemberTypes(schemaType),
                    context.getSchemaCatalog(),
                    schemaType
                );
            }
            if (constructor != null) {
                constructors.put(identifier, constructor);
            }
        }
        context.setXmlSchemaConstructors(constructors);
    }

    private static void registerGeneralizedAtomicTypes(
            StaticContext context,
            XSModel schemaModel,
            ExceptionMetadata metadata,
            XmlSchemaTypeMapper typeMapper
    ) {
        XSNamedMap schemaTypes = schemaModel.getComponents(XSConstants.TYPE_DEFINITION);
        for (int index = 0; index < schemaTypes.getLength(); index++) {
            XSTypeDefinition schemaType = (XSTypeDefinition) schemaTypes.item(index);
            typeMapper.getGeneralizedAtomicType(schemaType)
                .filter(ItemType::hasName)
                .filter(type -> !BuiltinTypesCatalogue.typeExists(type.getName()))
                .ifPresent(type -> context.getInScopeSchemaTypes().addInScopeSchemaType(type, metadata));
        }
    }

    private static Map<String, List<URI>> resolveLocations(List<SchemaImport> imports, URI baseURI) {
        Map<String, List<URI>> result = new LinkedHashMap<>();
        for (SchemaImport schemaImport : imports) {
            List<String> locationHints = schemaImport.getLocationHints();
            if (locationHints.isEmpty() && !schemaImport.getTargetNamespace().equals(Name.XS_NS)) {
                locationHints = List.of(schemaImport.getTargetNamespace());
            }
            result.put(
                schemaImport.getTargetNamespace(),
                locationHints
                    .stream()
                    .map(location -> URILiteralUtils.resolve(baseURI, location, schemaImport.getMetadata()))
                    .toList()
            );
        }
        return result;
    }

    private static XSModel loadSchemaModel(
            List<URI> locations,
            SchemaResourceResolver resolver,
            ExceptionMetadata metadata
    ) {
        try {
            XSImplementation implementation = (XSImplementation) DOMImplementationRegistry.newInstance()
                .getDOMImplementation("XS-Loader");
            if (implementation == null) {
                throw new SchemaImportException("Xerces XML Schema loader is unavailable.", metadata);
            }
            XSLoader loader = implementation.createXSLoader(null);
            loader.getConfig().setParameter("resource-resolver", resolver);
            LSInput[] inputs = locations.stream().map(resolver::resolveInput).toArray(LSInput[]::new);
            XSModel model = loader.loadInputList(implementation.createLSInputList(inputs));
            if (model == null) {
                throw new SchemaImportException("Unable to build the XML Schema component model.", metadata);
            }
            return model;
        } catch (ReflectiveOperationException exception) {
            throw new SchemaImportException("Unable to initialize the Xerces XML Schema loader.", metadata, exception);
        }
    }

    private static void verifyImportedNamespaces(List<SchemaImport> imports, XSModel schemaModel) {
        for (SchemaImport schemaImport : imports) {
            String namespace = schemaImport.getTargetNamespace();
            if (namespace.equals(Name.XS_NS)) {
                continue;
            }
            String schemaNamespace = namespace.isEmpty() ? null : namespace;
            if (!schemaModel.getNamespaces().contains(schemaNamespace)) {
                throw new SchemaImportException(
                        "No schema with target namespace " + namespace + " was found.",
                        schemaImport.getMetadata()
                );
            }
        }
    }

    private static final class SchemaResourceResolver implements LSResourceResolver {

        private final URI defaultBaseURI;
        private final Map<String, List<URI>> locationsByNamespace;
        private final CompilationConfiguration compilationConfiguration;
        private final ExceptionMetadata metadata;
        private final Map<URI, SchemaSource> sources;

        private SchemaResourceResolver(
                URI defaultBaseURI,
                Map<String, List<URI>> locationsByNamespace,
                CompilationConfiguration compilationConfiguration,
                ExceptionMetadata metadata
        ) {
            this.defaultBaseURI = defaultBaseURI;
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

        private LSInput resolveInput(URI location) {
            SchemaSource source = resolve(location);
            return new DOMInputImpl(
                    null,
                    source.systemId().toString(),
                    null,
                    new ByteArrayInputStream(source.content()),
                    null
            );
        }

        @Override
        public LSInput resolveResource(
                String type,
                String namespaceURI,
                String publicId,
                String systemId,
                String baseURI
        ) {
            URI location = resolveLocation(namespaceURI, systemId, baseURI);
            if (location == null) {
                return null;
            }
            SchemaSource source = resolve(location);
            return new DOMInputImpl(
                    publicId,
                    source.systemId().toString(),
                    baseURI,
                    new ByteArrayInputStream(source.content()),
                    null
            );
        }

        private SchemaSource resolve(URI location) {
            return this.sources.computeIfAbsent(location, this::read);
        }

        private SchemaSource read(URI location) {
            try (
                ResolvedResource resource = this.compilationConfiguration.resourceResolver()
                    .resolve(
                        location,
                        this.compilationConfiguration.runtimeConfiguration(),
                        this.metadata
                    )
            ) {
                return new SchemaSource(resource.getSystemId(), resource.getInputStream().readAllBytes());
            } catch (IOException exception) {
                throw new SchemaImportException("Unable to read schema " + location + ".", this.metadata, exception);
            }
        }

        private URI resolveLocation(String namespace, String systemId, String baseURI) {
            if (systemId != null) {
                URI base = baseURI == null ? this.defaultBaseURI : URI.create(baseURI);
                return URILiteralUtils.resolve(base, systemId, this.metadata);
            }
            List<URI> locations = this.locationsByNamespace.get(namespace == null ? "" : namespace);
            return locations == null || locations.isEmpty() ? null : locations.get(0);
        }

        private List<SchemaCatalog.SchemaDocument> getSchemaDocuments() {
            return this.sources.values()
                .stream()
                .sorted((first, second) -> first.systemId().compareTo(second.systemId()))
                .map(source -> new SchemaCatalog.SchemaDocument(source.systemId(), source.content()))
                .toList();
        }
    }

    private record SchemaSource(URI systemId, byte[] content) {
    }
}
