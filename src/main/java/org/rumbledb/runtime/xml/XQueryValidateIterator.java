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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.ValidateException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.ItemType;

/** Local evaluation of XQuery {@code validate type} for built-in atomic types. */
public final class XQueryValidateIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan operand;
    private final ItemType targetType;

    public XQueryValidateIterator(ItemRuntimePlan operand, ItemType targetType, RuntimeStaticContext staticContext) {
        super(List.of(operand), staticContext);
        this.operand = operand;
        this.targetType = targetType;
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
        return BuiltinTypeValidator.validate(item, this.targetType, getMetadata());
    }

    private ValidateException operandTypeError(String detail) {
        return new ValidateException(
                "A validate expression requires exactly one document or element node. " + detail,
                ErrorCode.ValidateOperandTypeErrorCode,
                getMetadata());
    }
}
