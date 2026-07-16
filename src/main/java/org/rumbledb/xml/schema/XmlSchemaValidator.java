/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import javax.xml.validation.ValidatorHandler;

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

    private final SchemaCatalog schemaCatalog;

    public XmlSchemaValidator(SchemaCatalog schemaCatalog) {
        this.schemaCatalog = schemaCatalog;
    }

    public void validateStrict(Item validationRoot, ExceptionMetadata metadata) {
        Name rootName = validationRoot.nodeName();
        if (
            this.schemaCatalog.schemaModel()
                .getElementDeclaration(rootName.getLocalName(), schemaNamespace(rootName)) == null
        ) {
            throw new ValidateException(
                    "No top-level element declaration is available for " + rootName + ".",
                    ErrorCode.ValidateStrictNoDeclarationErrorCode,
                    metadata
            );
        }
        validate(validationRoot, null, metadata);
    }

    public void validateType(Item validationRoot, Name typeName, ExceptionMetadata metadata) {
        XSTypeDefinition type = this.schemaCatalog.getTypeDefinition(typeName);
        if (type == null) {
            throw new OurBadException("The statically resolved XML Schema type is unavailable at runtime.", metadata);
        }
        validate(validationRoot, type, metadata);
    }

    private void validate(Item validationRoot, XSTypeDefinition rootType, ExceptionMetadata metadata) {
        ValidatorHandler handler = createHandler(rootType, metadata);
        try {
            new XmlItemSaxEmitter(handler).emit(validationRoot);
        } catch (SAXException exception) {
            throw new InvalidInstanceException(
                    "XML Schema validation failed: " + exception.getMessage(),
                    metadata
            );
        }
    }

    private ValidatorHandler createHandler(XSTypeDefinition rootType, ExceptionMetadata metadata) {
        ValidatorHandler handler = this.schemaCatalog.validationSchema().newValidatorHandler();
        handler.setErrorHandler(new ThrowingErrorHandler());
        handler.setContentHandler(new DefaultHandler());
        if (rootType != null) {
            try {
                handler.setProperty(ROOT_TYPE_DEFINITION, rootType);
            } catch (SAXException exception) {
                throw new OurBadException(
                        "Unable to configure the Xerces schema validator: " + exception.getMessage(),
                        metadata
                );
            }
        }
        return handler;
    }

    private static String schemaNamespace(Name name) {
        String namespace = name.getNamespace();
        return namespace == null || namespace.isEmpty() ? null : namespace;
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
