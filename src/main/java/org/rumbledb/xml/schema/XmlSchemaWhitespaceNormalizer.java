/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

/** Applies XML Schema whitespace normalization without moving non-text children. */
final class XmlSchemaWhitespaceNormalizer {

    private XmlSchemaWhitespaceNormalizer() {
    }

    static void normalize(List<Item> children, ElementPSVI psvi) {
        String normalizedValue = psvi.getSchemaNormalizedValue();
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

    private static void normalizeText(
            List<Item> children,
            List<StringBuilder> normalizedText,
            WhitespaceMode mode
    ) {
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
            int end = index == lastText
                ? psviValue.length()
                : Math.min(offset + text.length(), psviValue.length());
            text.setLength(0);
            text.append(psviValue, offset, end);
            offset = end;
        }
    }

    private static int lastTextIndex(List<StringBuilder> normalizedText) {
        for (int index = normalizedText.size() - 1; index >= 0; index--) {
            if (normalizedText.get(index) != null) {
                return index;
            }
        }
        return -1;
    }

    private static void replaceTextChildren(
            List<Item> children,
            List<StringBuilder> normalizedText,
            String normalizedValue
    ) {
        List<Item> rebuiltChildren = new ArrayList<>(children.size());
        for (int index = 0; index < children.size(); index++) {
            Item child = children.get(index);
            if (!child.isTextNode()) {
                rebuiltChildren.add(child);
            } else if (!normalizedText.get(index).isEmpty()) {
                rebuiltChildren.add(
                    ItemFactory.getInstance().createXmlTextNode(normalizedText.get(index).toString())
                );
            }
        }
        if (lastTextIndex(normalizedText) < 0 && !normalizedValue.isEmpty()) {
            rebuiltChildren.add(0, ItemFactory.getInstance().createXmlTextNode(normalizedValue));
        }
        children.clear();
        children.addAll(rebuiltChildren);
    }

    private static WhitespaceMode whitespaceMode(XSTypeDefinition type) {
        XSSimpleTypeDefinition simpleType = simpleType(type);
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

    private static XSSimpleTypeDefinition simpleType(XSTypeDefinition type) {
        if (type instanceof XSSimpleTypeDefinition simpleType) {
            return simpleType;
        }
        if (type instanceof XSComplexTypeDefinition complexType) {
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
