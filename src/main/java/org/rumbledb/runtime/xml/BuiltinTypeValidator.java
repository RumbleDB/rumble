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

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.DatetimeOverflowOrUnderflow;
import org.rumbledb.exceptions.DurationOverflowOrUnderflow;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.InvalidLexicalValueException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.ItemType;

/** Validates an XML element or document against a built-in atomic XML Schema type. */
public final class BuiltinTypeValidator {

    private BuiltinTypeValidator() {}

    public static Item validate(Item item, ItemType itemType, ExceptionMetadata metadata) {
        if (!itemType.isAtomicItemType()) {
            throw new OurBadException("Built-in XML validation requires an atomic target type.", metadata);
        }

        Item copiedRoot;
        Item validatedElement;
        if (item.isDocumentNode()) {
            validateAtomicDocumentShape(item, metadata);
            copiedRoot = item.copy(false);
            reattachXmlParents(copiedRoot, null);
            validatedElement = getSingleElementChild(copiedRoot);
        } else if (item.isElementNode()) {
            validateAtomicElementShape(item, metadata);
            copiedRoot = item.copy(false);
            reattachXmlParents(copiedRoot, null);
            validatedElement = copiedRoot;
        } else {
            throw new InvalidInstanceException(
                    "Atomic XML validation is only supported for document and element nodes.", metadata);
        }
        copiedRoot.setXmlDocumentPosition(XMLDocumentPosition.generateConstructedTreePath(), 0);

        Item typedValue;
        try {
            typedValue = CastIterator.castItemToType(
                    ItemFactory.getInstance().createUntypedAtomicItem(validatedElement.getStringValue()),
                    itemType,
                    metadata,
                    NamespaceBindingUtils.namespaceResolver(validatedElement));
        } catch (CastException
                | DatetimeOverflowOrUnderflow
                | DurationOverflowOrUnderflow
                | InvalidLexicalValueException
                | UnexpectedTypeException exception) {
            throw invalidValue(item, itemType, metadata, exception);
        }
        if (typedValue == null) {
            throw invalidValue(item, itemType, metadata, null);
        }
        validatedElement.setSchemaType(XmlSchemaTypeAnnotation.forAtomicItemType(itemType), List.of(typedValue));
        return copiedRoot;
    }

    private static InvalidInstanceException invalidValue(
            Item item, ItemType itemType, ExceptionMetadata metadata, Exception cause) {
        InvalidInstanceException exception = new InvalidInstanceException(
                "The value of " + item.serialize() + " is not valid for " + itemType.getIdentifierString() + ".",
                metadata);
        if (cause != null) {
            exception.initCause(cause);
        }
        return exception;
    }

    private static void validateAtomicDocumentShape(Item document, ExceptionMetadata metadata) {
        if (!hasValidDocumentStructure(document)) {
            throw new InvalidInstanceException(
                    "A document node must have exactly one element child and only comment or "
                            + "processing-instruction siblings.",
                    metadata);
        }
        validateAtomicElementShape(getSingleElementChild(document), metadata);
    }

    public static boolean hasValidDocumentStructure(Item document) {
        if (!document.isDocumentNode() || getSingleElementChild(document) == null) {
            return false;
        }
        for (Item child : document.children()) {
            if (!child.isElementNode() && !child.isCommentNode() && !child.isProcessingInstructionNode()) {
                return false;
            }
        }
        return true;
    }

    private static void validateAtomicElementShape(Item element, ExceptionMetadata metadata) {
        if (!element.attributes().isEmpty()) {
            throw new InvalidInstanceException(
                    "An element validated against an atomic type cannot have attributes.", metadata);
        }
        for (Item child : element.children()) {
            if (child.isElementNode()) {
                throw new InvalidInstanceException(
                        "An element validated against an atomic type cannot have element children.", metadata);
            }
        }
    }

    public static Item getSingleElementChild(Item document) {
        Item elementChild = null;
        for (Item child : document.children()) {
            if (!child.isElementNode()) {
                continue;
            }
            if (elementChild != null) {
                return null;
            }
            elementChild = child;
        }
        return elementChild;
    }

    private static void reattachXmlParents(Item node, Item parent) {
        if (parent != null) {
            node.setParent(parent);
        }
        for (Item attribute : node.attributes()) {
            attribute.setParent(node);
        }
        for (Item child : node.children()) {
            reattachXmlParents(child, node);
        }
    }
}
