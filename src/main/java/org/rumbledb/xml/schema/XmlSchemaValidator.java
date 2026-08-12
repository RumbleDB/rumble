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

import javax.xml.validation.ValidatorHandler;

import org.apache.xerces.xs.PSVIProvider;
import org.apache.xerces.xs.XSTypeDefinition;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ValidateException;

/** Validates local Rumble XML nodes against an imported Xerces schema catalog. */
public final class XmlSchemaValidator {

    private static final String ROOT_TYPE_DEFINITION =
            "http://apache.org/xml/properties/validation/schema/root-type-definition";
    private static final String ID_IDREF_CHECKING = "http://apache.org/xml/features/validation/id-idref-checking";
    private static final String UNPARSED_ENTITY_CHECKING =
            "http://apache.org/xml/features/validation/unparsed-entity-checking";
    private static final Name ANY_TYPE = new Name(Name.XS_NS, "xs", "anyType");

    private final XmlSchemaCatalog catalog;

    public XmlSchemaValidator(XmlSchemaCatalog catalog) {
        if (catalog == null) {
            throw new IllegalArgumentException("An XML Schema catalog cannot be null.");
        }
        this.catalog = catalog;
    }

    /** Validates the root using its global element declaration. */
    public Item validateStrict(Item input, ExceptionMetadata metadata) {
        Item element = validationElement(input, metadata);
        if (this.catalog
                        .getSchemaModel()
                        .getElementDeclaration(
                                element.nodeName().getLocalName(),
                                emptyToNull(element.nodeName().getNamespace()))
                == null) {
            throw new ValidateException(
                    "No global XML Schema element declaration is available for " + element.nodeName() + ".",
                    ErrorCode.ValidateStrictNoDeclarationErrorCode,
                    metadata);
        }
        return validate(input, null, metadata);
    }

    /** Performs lax validation, using xs:anyType when the root has no global declaration. */
    public Item validateLax(Item input, ExceptionMetadata metadata) {
        Item element = validationElement(input, metadata);
        XSTypeDefinition rootType = null;
        if (this.catalog
                        .getSchemaModel()
                        .getElementDeclaration(
                                element.nodeName().getLocalName(),
                                emptyToNull(element.nodeName().getNamespace()))
                == null) {
            rootType = this.catalog
                    .getTypeDefinition(ANY_TYPE)
                    .orElseThrow(() -> new OurBadException("The built-in xs:anyType definition is unavailable."));
        }
        return validate(input, rootType, metadata);
    }

    /** Validates the root against a statically resolved imported schema type. */
    public Item validateType(Item input, Name typeName, ExceptionMetadata metadata) {
        XSTypeDefinition rootType = this.catalog
                .getTypeDefinition(typeName)
                .orElseThrow(() -> new OurBadException(
                        "The statically resolved XML Schema type " + typeName + " is unavailable at runtime.",
                        metadata));
        validationElement(input, metadata);
        return validate(input, rootType, metadata);
    }

    private Item validate(Item input, XSTypeDefinition rootType, ExceptionMetadata metadata) {
        ValidatorHandler handler = createHandler(rootType, input.isDocumentNode(), metadata);
        if (!(handler instanceof PSVIProvider psviProvider)) {
            throw new OurBadException("The Xerces validator does not expose PSVI information.", metadata);
        }
        PsviNodeBuilder builder = new PsviNodeBuilder(psviProvider, this.catalog, input.isDocumentNode());
        handler.setContentHandler(builder);
        try {
            new XmlItemSaxEmitter(handler, builder::comment).emit(input);
            return builder.getResult();
        } catch (SAXException exception) {
            throw invalidInstance(exception, metadata);
        }
    }

    private ValidatorHandler createHandler(
            XSTypeDefinition rootType, boolean documentValidation, ExceptionMetadata metadata) {
        ValidatorHandler handler = this.catalog.getValidationSchema().newValidatorHandler();
        handler.setErrorHandler(new ThrowingErrorHandler());
        try {
            handler.setFeature(ID_IDREF_CHECKING, documentValidation);
            handler.setFeature(UNPARSED_ENTITY_CHECKING, false);
            if (rootType != null) {
                handler.setProperty(ROOT_TYPE_DEFINITION, rootType);
            }
        } catch (SAXException exception) {
            throw new OurBadException(
                    "Unable to configure the Xerces schema validator: " + exception.getMessage(), metadata);
        }
        return handler;
    }

    private static Item validationElement(Item input, ExceptionMetadata metadata) {
        if (input == null || (!input.isElementNode() && !input.isDocumentNode())) {
            throw new InvalidInstanceException("XML Schema validation requires an element or document node.", metadata);
        }
        if (input.isElementNode()) {
            return input;
        }

        Item element = null;
        for (Item child : input.children()) {
            if (child.isElementNode()) {
                if (element != null) {
                    throw invalidDocument(metadata);
                }
                element = child;
            } else if (!child.isCommentNode() && !child.isProcessingInstructionNode()) {
                throw invalidDocument(metadata);
            }
        }
        if (element == null) {
            throw invalidDocument(metadata);
        }
        return element;
    }

    private static InvalidInstanceException invalidDocument(ExceptionMetadata metadata) {
        return new InvalidInstanceException(
                "A document node being validated must have exactly one element child and only comment or "
                        + "processing-instruction siblings.",
                metadata);
    }

    private static InvalidInstanceException invalidInstance(SAXException cause, ExceptionMetadata metadata) {
        InvalidInstanceException exception =
                new InvalidInstanceException("XML Schema validation failed: " + cause.getMessage(), metadata);
        exception.initCause(cause);
        return exception;
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
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
