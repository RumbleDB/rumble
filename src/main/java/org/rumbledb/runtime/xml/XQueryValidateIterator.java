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

package org.rumbledb.runtime.xml;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ValidateException;
import org.rumbledb.expressions.typing.ValidateExpression.ValidationMode;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.xml.schema.XmlSchemaCatalog;
import org.rumbledb.xml.schema.XmlSchemaValidator;

/** Local evaluation of an XQuery validate expression. */
public final class XQueryValidateIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan operand;
    private final ValidationMode validationMode;
    private final Name targetTypeName;

    /** Xerces grammars are intentionally local-only and are not serialized with a runtime plan. */
    private final transient XmlSchemaCatalog schemaCatalog;

    public XQueryValidateIterator(
            ItemRuntimePlan operand,
            ValidationMode validationMode,
            Name targetTypeName,
            XmlSchemaCatalog schemaCatalog,
            RuntimeStaticContext staticContext) {
        super(List.of(operand), staticContext);
        if (validationMode == null) {
            throw new OurBadException("A validate runtime plan must have a validation mode.");
        }
        if ((validationMode == ValidationMode.TYPE) != (targetTypeName != null)) {
            throw new OurBadException("A validate type runtime plan must have exactly one target type name.");
        }
        this.operand = operand;
        this.validationMode = validationMode;
        this.targetTypeName = targetTypeName;
        this.schemaCatalog = schemaCatalog;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item item;
        try {
            item = this.operand.materializeAtMostOne(context);
        } catch (MoreThanOneItemException exception) {
            throw operandTypeError("The operand contains more than one item.");
        }
        if (item == null) {
            throw operandTypeError("The operand is an empty sequence.");
        }
        if (!item.isDocumentNode() && !item.isElementNode()) {
            throw operandTypeError("The operand is neither a document nor an element node.");
        }
        if (item.isDocumentNode() && !BuiltinTypeValidator.hasValidDocumentStructure(item)) {
            throw new ValidateException(
                    "A document node being validated must have exactly one element child and only comment or "
                            + "processing-instruction siblings.",
                    ErrorCode.InvalidValidateDocumentStructureErrorCode,
                    getMetadata());
        }
        if (this.validationMode == ValidationMode.TYPE
                && Name.XS_NS.equals(this.targetTypeName.getNamespace())
                && BuiltinTypesCatalogue.typeExists(this.targetTypeName)) {
            return BuiltinTypeValidator.validate(
                    item, BuiltinTypesCatalogue.getItemTypeByName(this.targetTypeName), getMetadata());
        }
        if (this.schemaCatalog == null) {
            throw new OurBadException(
                    "The local XML Schema catalog is unavailable to the validate runtime plan.", getMetadata());
        }

        XmlSchemaValidator validator = new XmlSchemaValidator(this.schemaCatalog);
        return switch (this.validationMode) {
            case STRICT -> validator.validateStrict(item, getMetadata());
            case LAX -> validator.validateLax(item, getMetadata());
            case TYPE -> validator.validateType(item, this.targetTypeName, getMetadata());
        };
    }

    private ValidateException operandTypeError(String detail) {
        return new ValidateException(
                "A validate expression requires exactly one document or element node. " + detail,
                ErrorCode.ValidateOperandTypeErrorCode,
                getMetadata());
    }
}
