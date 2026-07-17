/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.SchemaCatalog.SchemaDocument;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.NoNamespaceFoundForPrefixException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.ItemType;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/** Validates a constructed atomic value against its Xerces simple-type definition. */
public final class XmlSchemaSimpleTypeValidator implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Name typeName;
    private final List<SchemaDocument> documents;
    private transient XSTypeDefinition schemaType;

    public XmlSchemaSimpleTypeValidator(
            Name typeName,
            List<SchemaDocument> documents,
            XSTypeDefinition schemaType
    ) {
        this.typeName = typeName;
        this.documents = new ArrayList<>(documents);
        this.schemaType = schemaType;
    }

    public List<Item> validate(
            Item item,
            RuntimeStaticContext staticContext
    ) {
        XSTypeDefinition schemaType = getSchemaType();
        if (!(schemaType instanceof XSSimpleType simpleType)) {
            throw new OurBadException("An XML Schema constructor must target a simple type.");
        }
        try {
            ValidatedInfo schemaValue = new ValidatedInfo();
            simpleType.validate(
                item.getStringValue(),
                new ConstructorValidationContext(staticContext),
                schemaValue
            );
            return new XmlSchemaTypedValueFactory(new XmlSchemaTypeMapper()).create(schemaValue, simpleType);
        } catch (InvalidDatatypeValueException exception) {
            if ("UndeclaredPrefix".equals(exception.getKey())) {
                throw new NoNamespaceFoundForPrefixException(exception.getMessage(), staticContext.getMetadata());
            }
            throw castException(item, schemaType, staticContext);
        }
    }

    public List<Item> validateUnionFromNonString(
            Item item,
            RuntimeStaticContext staticContext
    ) {
        XSTypeDefinition schemaType = getSchemaType();
        if (
            !(schemaType instanceof XSSimpleType unionType)
                || unionType.getVariety() != XSSimpleTypeDefinition.VARIETY_UNION
        ) {
            throw new OurBadException("A union constructor must target an XML Schema union type.");
        }

        ConstructorValidationContext validationContext = new ConstructorValidationContext(staticContext);
        XmlSchemaTypeMapper typeMapper = new XmlSchemaTypeMapper();
        List<XSSimpleType> atomicMemberTypes = new ArrayList<>();
        collectAtomicMemberTypes(unionType, atomicMemberTypes);
        for (XSSimpleType atomicMemberType : atomicMemberTypes) {
            ItemType itemType = typeMapper.getAtomicType(atomicMemberType).orElse(null);
            if (itemType == null) {
                continue;
            }
            Item converted;
            try {
                converted = CastIterator.castItemToType(item, itemType, staticContext.getMetadata(), staticContext);
            } catch (RumbleException exception) {
                continue;
            }
            if (converted == null) {
                continue;
            }

            ValidatedInfo schemaValue = new ValidatedInfo();
            try {
                atomicMemberType.validate(converted.getStringValue(), validationContext, schemaValue);
            } catch (InvalidDatatypeValueException exception) {
                continue;
            }
            // Apply facets declared on the union, but retain the selected atomic member's value.
            // The lexical union result may otherwise be captured by an earlier list member.
            try {
                unionType.validate(converted.getStringValue(), validationContext, new ValidatedInfo());
            } catch (InvalidDatatypeValueException exception) {
                throw castException(item, schemaType, staticContext);
            }
            return new XmlSchemaTypedValueFactory(typeMapper).create(schemaValue, atomicMemberType);
        }
        throw castException(item, schemaType, staticContext);
    }

    private static void collectAtomicMemberTypes(XSSimpleTypeDefinition schemaType, List<XSSimpleType> result) {
        XSObjectList memberTypes = schemaType.getMemberTypes();
        for (int index = 0; index < memberTypes.getLength(); index++) {
            XSSimpleType memberType = (XSSimpleType) memberTypes.item(index);
            if (memberType.getVariety() == XSSimpleTypeDefinition.VARIETY_ATOMIC) {
                result.add(memberType);
            } else if (memberType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION) {
                collectAtomicMemberTypes(memberType, result);
            }
        }
    }

    private static CastException castException(
            Item item,
            XSTypeDefinition schemaType,
            RuntimeStaticContext staticContext
    ) {
        return new CastException(
                "\"" + item.getStringValue() + "\" is not valid for type " + schemaType.getName() + ".",
                staticContext.getMetadata()
        );
    }

    private synchronized XSTypeDefinition getSchemaType() {
        if (this.schemaType != null) {
            return this.schemaType;
        }
        if (this.documents.isEmpty() && Name.XS_NS.equals(this.typeName.getNamespace())) {
            this.schemaType = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(this.typeName.getLocalName());
            if (this.schemaType != null) {
                return this.schemaType;
            }
        }
        try {
            XSImplementation implementation = (XSImplementation) DOMImplementationRegistry.newInstance()
                .getDOMImplementation("XS-Loader");
            XSLoader loader = implementation.createXSLoader(null);
            loader.getConfig()
                .setParameter(
                    "resource-resolver",
                    (LSResourceResolver) this::resolveDocument
                );
            LSInput[] inputs = this.documents.stream().map(this::toInput).toArray(LSInput[]::new);
            XSModel model = loader.loadInputList(implementation.createLSInputList(inputs));
            this.schemaType = model.getTypeDefinition(this.typeName.getLocalName(), schemaNamespace());
            if (this.schemaType == null) {
                throw new OurBadException("Unable to reload imported XML Schema type " + this.typeName + ".");
            }
            return this.schemaType;
        } catch (ReflectiveOperationException exception) {
            OurBadException result = new OurBadException("Unable to reload the imported XML Schema model.");
            result.initCause(exception);
            throw result;
        }
    }

    private LSInput resolveDocument(
            String type,
            String namespaceURI,
            String publicId,
            String systemId,
            String baseURI
    ) {
        if (systemId == null) {
            return null;
        }
        URI location = baseURI == null ? URI.create(systemId) : URI.create(baseURI).resolve(systemId);
        return this.documents.stream()
            .filter(document -> document.systemId().equals(location))
            .findFirst()
            .map(document -> toInput(document, publicId, baseURI))
            .orElse(null);
    }

    private LSInput toInput(SchemaDocument document) {
        return toInput(document, null, null);
    }

    private LSInput toInput(SchemaDocument document, String publicId, String baseURI) {
        return new DOMInputImpl(
                publicId,
                document.systemId().toString(),
                baseURI,
                new ByteArrayInputStream(document.content()),
                null
        );
    }

    private String schemaNamespace() {
        String namespace = this.typeName.getNamespace();
        return namespace == null || namespace.isEmpty() ? null : namespace;
    }

    private record ConstructorValidationContext(RuntimeStaticContext staticContext) implements ValidationContext {

        @Override
        public boolean needFacetChecking() {
            return true;
        }

        @Override
        public boolean needExtraChecking() {
            return true;
        }

        @Override
        public boolean needToNormalize() {
            return true;
        }

        @Override
        public boolean useNamespaces() {
            return true;
        }

        @Override
        public boolean isEntityDeclared(String name) {
            return false;
        }

        @Override
        public boolean isEntityUnparsed(String name) {
            return false;
        }

        @Override
        public boolean isIdDeclared(String name) {
            return false;
        }

        @Override
        public void addId(String name) {
        }

        @Override
        public void addIdRef(String name) {
        }

        @Override
        public String getSymbol(String symbol) {
            return symbol;
        }

        @Override
        public String getURI(String prefix) {
            return this.staticContext.getStaticallyKnownNamespaces().get(prefix);
        }

        @Override
        public Locale getLocale() {
            return Locale.ROOT;
        }
    }
}
