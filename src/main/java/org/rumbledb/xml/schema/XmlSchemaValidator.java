/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.HashSet;
import java.util.Set;

import javax.xml.validation.ValidatorHandler;

import org.apache.xerces.xs.PSVIProvider;
import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.SchemaCatalog;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ValidateException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/** Xerces-backed validation against the XML Schema environment of an XQuery module. */
public final class XmlSchemaValidator {

    private static final String ROOT_TYPE_DEFINITION =
        "http://apache.org/xml/properties/validation/schema/root-type-definition";
    private static final String ID_IDREF_CHECKING =
        "http://apache.org/xml/features/validation/id-idref-checking";
    private static final String UNPARSED_ENTITY_CHECKING =
        "http://apache.org/xml/features/validation/unparsed-entity-checking";
    private static final Name ANY_TYPE = new Name(Name.XS_NS, "xs", "anyType");
    private final SchemaCatalog schemaCatalog;
    private final XmlSchemaTypeMapper typeMapper;

    public XmlSchemaValidator(SchemaCatalog schemaCatalog) {
        this.schemaCatalog = schemaCatalog;
        this.typeMapper = new XmlSchemaTypeMapper();
    }

    public Item validateStrict(
            Item validationRoot,
            boolean documentValidation,
            ExceptionMetadata metadata
    ) {
        Name rootName = validationRoot.nodeName();
        if (!hasElementDeclaration(rootName)) {
            throw new ValidateException(
                    "No top-level element declaration is available for " + rootName + ".",
                    ErrorCode.ValidateStrictNoDeclarationErrorCode,
                    metadata
            );
        }
        return validate(validationRoot, null, false, documentValidation, metadata);
    }

    public Item validateLax(
            Item validationRoot,
            boolean documentValidation,
            ExceptionMetadata metadata
    ) {
        if (hasElementDeclaration(validationRoot.nodeName())) {
            return validate(validationRoot, null, false, documentValidation, metadata);
        }
        XSTypeDefinition anyType = this.schemaCatalog.getTypeDefinition(ANY_TYPE);
        if (anyType == null) {
            throw new OurBadException("The built-in xs:anyType definition is unavailable.", metadata);
        }
        return validate(validationRoot, anyType, true, documentValidation, metadata);
    }

    public Item validateType(
            Item validationRoot,
            Name typeName,
            boolean documentValidation,
            ExceptionMetadata metadata
    ) {
        XSTypeDefinition type = this.schemaCatalog.getTypeDefinition(typeName);
        if (type == null) {
            throw new OurBadException("The statically resolved XML Schema type is unavailable at runtime.", metadata);
        }
        return validate(validationRoot, type, false, documentValidation, metadata);
    }

    private Item validate(
            Item validationRoot,
            XSTypeDefinition rootType,
            boolean laxWildcardRoot,
            boolean documentValidation,
            ExceptionMetadata metadata
    ) {
        ValidatorHandler handler = createHandler(rootType, documentValidation, metadata);
        if (!(handler instanceof PSVIProvider psviProvider)) {
            throw new OurBadException("The Xerces validator does not expose PSVI information.", metadata);
        }
        PsviNodeBuilder builder = new PsviNodeBuilder(psviProvider, this.typeMapper);
        handler.setContentHandler(builder);
        try {
            XmlItemSaxEmitter emitter = new XmlItemSaxEmitter(handler, builder::comment);
            if (laxWildcardRoot) {
                emitter.emitAsLaxWildcard(validationRoot);
                Item result = builder.getResult().children().get(0);
                result.setParent(null);
                checkUniqueXmlIds(result, new HashSet<>(), metadata);
                return result;
            }
            emitter.emit(validationRoot);
            Item result = builder.getResult();
            checkUniqueXmlIds(result, new HashSet<>(), metadata);
            return result;
        } catch (SAXException exception) {
            throw new InvalidInstanceException(
                    "XML Schema validation failed: " + exception.getMessage(),
                    metadata
            );
        }
    }

    private static void checkUniqueXmlIds(
            Item node,
            Set<String> values,
            ExceptionMetadata metadata
    ) {
        if (node.isElementNode()) {
            for (Item attribute : node.attributes()) {
                Name name = attribute.nodeName();
                if (
                    Name.XML_NS.equals(name.getNamespace())
                        && "id".equals(name.getLocalName())
                        && !values.add(attribute.getStringValue())
                ) {
                    throw new InvalidInstanceException(
                            "XML Schema validation failed: duplicate xml:id value "
                                + attribute.getStringValue()
                                + ".",
                            metadata
                    );
                }
            }
        }
        for (Item child : node.children()) {
            checkUniqueXmlIds(child, values, metadata);
        }
    }

    private ValidatorHandler createHandler(
            XSTypeDefinition rootType,
            boolean documentValidation,
            ExceptionMetadata metadata
    ) {
        ValidatorHandler handler = this.schemaCatalog.validationSchema().newValidatorHandler();
        handler.setErrorHandler(new ThrowingErrorHandler());
        try {
            handler.setFeature(ID_IDREF_CHECKING, documentValidation);
            handler.setFeature(UNPARSED_ENTITY_CHECKING, false);
            if (rootType != null) {
                handler.setProperty(ROOT_TYPE_DEFINITION, rootType);
            }
        } catch (SAXException exception) {
            throw new OurBadException(
                    "Unable to configure the Xerces schema validator: " + exception.getMessage(),
                    metadata
            );
        }
        return handler;
    }

    private static String schemaNamespace(Name name) {
        String namespace = name.getNamespace();
        return namespace == null || namespace.isEmpty() ? null : namespace;
    }

    private boolean hasElementDeclaration(Name name) {
        return this.schemaCatalog.schemaModel()
            .getElementDeclaration(name.getLocalName(), schemaNamespace(name)) != null;
    }

    private static final class ThrowingErrorHandler extends DefaultHandler {

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
    }
}
