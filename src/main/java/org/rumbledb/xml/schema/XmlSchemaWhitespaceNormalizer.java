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

import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSValue;

import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

/** Reconciles simple element content with Xerces's schema-normalized value. */
final class XmlSchemaWhitespaceNormalizer {

    private XmlSchemaWhitespaceNormalizer() {}

    static void normalize(List<Item> children, ElementPSVI psvi) {
        XSValue schemaValue = psvi.getSchemaValue();
        if (schemaValue == null) {
            return;
        }
        String normalizedValue = schemaValue.getNormalizedValue();
        if (normalizedValue == null || children.stream().anyMatch(Item::isElementNode)) {
            return;
        }

        List<StringBuilder> normalizedText = createTextBuffers(children);
        normalizeText(children, normalizedText, whitespaceMode(psvi.getTypeDefinition()));
        reconcileWithPsviValue(normalizedText, normalizedValue);
        replaceTextChildren(children, normalizedText, normalizedValue);
    }

    private static List<StringBuilder> createTextBuffers(List<Item> children) {
        List<StringBuilder> result = new ArrayList<>(children.size());
        for (Item child : children) {
            result.add(child.isTextNode() ? new StringBuilder() : null);
        }
        return result;
    }

    private static void normalizeText(List<Item> children, List<StringBuilder> normalizedText, WhitespaceMode mode) {
        boolean hasOutput = false;
        int pendingWhitespace = -1;
        for (int index = 0; index < children.size(); index++) {
            Item child = children.get(index);
            if (!child.isTextNode()) {
                continue;
            }
            StringBuilder output = normalizedText.get(index);
            for (char character : child.getStringValue().toCharArray()) {
                if (mode == WhitespaceMode.PRESERVE) {
                    output.append(character);
                    hasOutput = true;
                } else if (isXmlWhitespace(character)) {
                    if (mode == WhitespaceMode.REPLACE) {
                        output.append(' ');
                        hasOutput = true;
                    } else if (hasOutput && pendingWhitespace < 0) {
                        pendingWhitespace = index;
                    }
                } else {
                    if (pendingWhitespace >= 0) {
                        normalizedText.get(pendingWhitespace).append(' ');
                        pendingWhitespace = -1;
                    }
                    output.append(character);
                    hasOutput = true;
                }
            }
        }
    }

    private static void reconcileWithPsviValue(List<StringBuilder> normalizedText, String psviValue) {
        StringBuilder completeValue = new StringBuilder();
        for (StringBuilder text : normalizedText) {
            if (text != null) {
                completeValue.append(text);
            }
        }
        if (completeValue.toString().equals(psviValue)) {
            return;
        }

        int offset = 0;
        int lastText = lastTextIndex(normalizedText);
        for (int index = 0; index < normalizedText.size(); index++) {
            StringBuilder text = normalizedText.get(index);
            if (text == null) {
                continue;
            }
            int end = index == lastText ? psviValue.length() : Math.min(offset + text.length(), psviValue.length());
            text.setLength(0);
            text.append(psviValue, offset, end);
            offset = end;
        }
    }

    private static void replaceTextChildren(
            List<Item> children, List<StringBuilder> normalizedText, String normalizedValue) {
        List<Item> rebuiltChildren = new ArrayList<>(children.size());
        for (int index = 0; index < children.size(); index++) {
            Item child = children.get(index);
            if (!child.isTextNode()) {
                rebuiltChildren.add(child);
            } else if (!normalizedText.get(index).isEmpty()) {
                rebuiltChildren.add(ItemFactory.getInstance()
                        .createXmlTextNode(normalizedText.get(index).toString()));
            }
        }
        if (lastTextIndex(normalizedText) < 0 && !normalizedValue.isEmpty()) {
            rebuiltChildren.add(0, ItemFactory.getInstance().createXmlTextNode(normalizedValue));
        }
        children.clear();
        children.addAll(rebuiltChildren);
    }

    private static int lastTextIndex(List<StringBuilder> normalizedText) {
        for (int index = normalizedText.size() - 1; index >= 0; index--) {
            if (normalizedText.get(index) != null) {
                return index;
            }
        }
        return -1;
    }

    private static WhitespaceMode whitespaceMode(XSTypeDefinition schemaType) {
        XSSimpleTypeDefinition simpleType = simpleType(schemaType);
        while (simpleType != null) {
            String facet = simpleType.getLexicalFacetValue(XSSimpleTypeDefinition.FACET_WHITESPACE);
            if (facet != null) {
                return switch (facet) {
                    case "replace" -> WhitespaceMode.REPLACE;
                    case "collapse" -> WhitespaceMode.COLLAPSE;
                    default -> WhitespaceMode.PRESERVE;
                };
            }
            XSTypeDefinition baseType = simpleType.getBaseType();
            simpleType = baseType instanceof XSSimpleTypeDefinition baseSimpleType ? baseSimpleType : null;
        }
        return WhitespaceMode.PRESERVE;
    }

    private static XSSimpleTypeDefinition simpleType(XSTypeDefinition schemaType) {
        if (schemaType instanceof XSSimpleTypeDefinition simpleType) {
            return simpleType;
        }
        if (schemaType instanceof XSComplexTypeDefinition complexType) {
            return complexType.getSimpleType();
        }
        return null;
    }

    private static boolean isXmlWhitespace(char character) {
        return character == ' ' || character == '\t' || character == '\n' || character == '\r';
    }

    private enum WhitespaceMode {
        PRESERVE,
        REPLACE,
        COLLAPSE
    }
}
