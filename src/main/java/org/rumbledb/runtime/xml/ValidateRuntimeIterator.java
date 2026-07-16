/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.xml;

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.SchemaCatalog;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.exceptions.ValidateException;
import org.rumbledb.expressions.typing.ValidateExpression.ValidationMode;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.xml.schema.XmlSchemaValidator;

/** Local evaluation of XQuery XML Schema validate expressions. */
public final class ValidateRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator operandIterator;
    private final ValidationMode validationMode;
    private final Name typeName;
    private final transient XmlSchemaValidator schemaValidator;

    public ValidateRuntimeIterator(
            RuntimeIterator operandIterator,
            ValidationMode validationMode,
            Name typeName,
            SchemaCatalog schemaCatalog,
            RuntimeStaticContext staticContext
    ) {
        super(List.of(operandIterator), staticContext);
        this.operandIterator = operandIterator;
        this.validationMode = validationMode;
        this.typeName = typeName;
        this.schemaValidator = new XmlSchemaValidator(schemaCatalog);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Item operand = materializeOperand(dynamicContext);
        Item validationRoot = requireValidationRoot(operand);

        switch (this.validationMode) {
            case STRICT -> this.schemaValidator.validateStrict(validationRoot, getMetadata());
            case TYPE -> this.schemaValidator.validateType(validationRoot, this.typeName, getMetadata());
            case LAX -> throw new UnsupportedFeatureException(
                    "XQuery lax XML Schema validation is not supported yet.",
                    getMetadata()
            );
        }

        Item result = operand.copy(false);
        resetAnnotationsAndParents(result, null);
        return result;
    }

    private Item materializeOperand(DynamicContext dynamicContext) {
        Item operand;
        try {
            operand = this.operandIterator.materializeAtMostOneItemOrNull(dynamicContext);
        } catch (MoreThanOneItemException exception) {
            throw operandTypeError("The operand contains more than one item.");
        }
        if (operand == null) {
            throw operandTypeError("The operand is an empty sequence.");
        }
        if (!operand.isDocumentNode() && !operand.isElementNode()) {
            throw operandTypeError("The operand must be a document or element node.");
        }
        return operand;
    }

    private ValidateException operandTypeError(String detail) {
        return new ValidateException(
                "A validate expression requires exactly one document or element node. " + detail,
                ErrorCode.ValidateOperandTypeErrorCode,
                getMetadata()
        );
    }

    private Item requireValidationRoot(Item operand) {
        if (operand.isElementNode()) {
            return operand;
        }

        Item documentElement = null;
        for (Item child : operand.children()) {
            if (child.isElementNode()) {
                if (documentElement != null) {
                    throw documentStructureError();
                }
                documentElement = child;
            } else if (!child.isCommentNode() && !child.isProcessingInstructionNode()) {
                throw documentStructureError();
            }
        }
        if (documentElement == null) {
            throw documentStructureError();
        }
        return documentElement;
    }

    private ValidateException documentStructureError() {
        return new ValidateException(
                "A document validated by a validate expression must contain exactly one element child and only "
                    + "comment or processing-instruction siblings.",
                ErrorCode.ValidateDocumentStructureErrorCode,
                getMetadata()
        );
    }

    private static void resetAnnotationsAndParents(Item item, Item parent) {
        if (item.isElementNode()) {
            item.setParent(parent);
            item.setSchemaType(null);
            for (Item attribute : item.attributes()) {
                attribute.setParent(item);
                attribute.setSchemaType(null);
            }
            for (Item child : item.children()) {
                resetAnnotationsAndParents(child, item);
            }
        } else if (item.isDocumentNode()) {
            for (Item child : item.children()) {
                resetAnnotationsAndParents(child, item);
            }
        } else if (parent != null) {
            item.setParent(parent);
        }
    }
}
