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
package org.rumbledb.runtime.functions.sequences.value;

import org.rumbledb.api.Item;
import org.rumbledb.runtime.misc.AtomicDeepEqual;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shared {@code fn:deep-equal} comparison logic for local execution and Spark partition checks.
 */
public final class DeepEqualComparison implements Serializable {

    private static final long serialVersionUID = 1L;

    private DeepEqualComparison() {
    }

    public static boolean deepEqualSequences(List<Item> items1, List<Item> items2) {
        if (items1.size() != items2.size()) {
            return false;
        }
        for (int i = 0; i < items1.size(); i++) {
            if (!deepEqualItems(items1.get(i), items2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean deepEqualItems(Item item1, Item item2) {
        if (
            ((item1.isFloat() && Float.isNaN(item1.getFloatValue()))
                || (item1.isDouble() && Double.isNaN(item1.getDoubleValue())))
                && ((item2.isFloat() && Float.isNaN(item2.getFloatValue()))
                    || (item2.isDouble() && Double.isNaN(item2.getDoubleValue())))
        ) {
            return true;
        }

        if (item1.isMap() && item2.isMap()) {
            List<Item> keys1 = item1.getItemKeys();
            List<Item> keys2 = item2.getItemKeys();
            if (keys1.size() != keys2.size()) {
                return false;
            }
            for (Item k1 : keys1) {
                List<Item> v1 = item1.getSequenceByKey(k1);
                List<Item> v2 = item2.getSequenceByKey(k1);
                if (v1 == null || v2 == null) {
                    return false;
                }
                if (!deepEqualSequences(v1, v2)) {
                    return false;
                }
            }
            for (Item k2 : keys2) {
                if (item1.getSequenceByKey(k2) == null) {
                    return false;
                }
            }
            return true;
        }
        if (item1.isArray() && item2.isArray()) {
            if (item1.getSize() != item2.getSize()) {
                return false;
            }
            for (int i = 0; i < item1.getSize(); i++) {
                if (!deepEqualItems(item1.getItemAt(i), item2.getItemAt(i))) {
                    return false;
                }
            }
            return true;
        }

        if (item1.isNode() && item2.isNode()) {
            return deepEqualNodes(item1, item2);
        }

        return AtomicDeepEqual.deepEqual(item1, item2);
    }

    private static boolean deepEqualNodes(Item node1, Item node2) {
        if (!sameNodeKind(node1, node2)) {
            return false;
        }

        if (node1.isDocumentNode() && node2.isDocumentNode()) {
            return deepEqualSequences(getElementsAndTextNodes(node1), getElementsAndTextNodes(node2));
        }

        if (node1.isElementNode() && node2.isElementNode()) {
            return deepEqualElementNodes(node1, node2);
        }

        if (node1.isAttributeNode() && node2.isAttributeNode()) {
            return deepEqualAttributeNodes(node1, node2);
        }

        if (node1.isProcessingInstructionNode() && node2.isProcessingInstructionNode()) {
            if (!node1.nodeName().equals(node2.nodeName())) {
                return false;
            }
            return node1.getStringValue().equals(node2.getStringValue());
        }

        if (
            (node1.isTextNode() && node2.isTextNode())
                || (node1.isCommentNode() && node2.isCommentNode())
        ) {
            return node1.getStringValue().equals(node2.getStringValue());
        }

        return false;
    }

    private static boolean sameNodeKind(Item node1, Item node2) {
        return (node1.isDocumentNode() && node2.isDocumentNode())
            ||
            (node1.isElementNode() && node2.isElementNode())
            ||
            (node1.isAttributeNode() && node2.isAttributeNode())
            ||
            (node1.isTextNode() && node2.isTextNode())
            ||
            (node1.isCommentNode() && node2.isCommentNode())
            ||
            (node1.isProcessingInstructionNode() && node2.isProcessingInstructionNode());
    }

    private static List<Item> getElementsAndTextNodes(Item node) {
        return node.children()
            .stream()
            .filter(child -> child.isElementNode() || child.isTextNode())
            .collect(Collectors.toList());
    }

    private static boolean deepEqualElementNodes(Item element1, Item element2) {
        if (!element1.nodeName().equals(element2.nodeName())) {
            return false;
        }

        if (!deepEqualAttributes(element1.attributes(), element2.attributes())) {
            return false;
        }

        return deepEqualSequences(getElementsAndTextNodes(element1), getElementsAndTextNodes(element2));
    }

    private static boolean deepEqualAttributeNodes(Item attr1, Item attr2) {
        if (!attr1.nodeName().equals(attr2.nodeName())) {
            return false;
        }
        return deepEqualSequences(attr1.atomizedValue(), attr2.atomizedValue());
    }

    private static boolean deepEqualAttributes(List<Item> attrs1, List<Item> attrs2) {
        if (attrs1.size() != attrs2.size()) {
            return false;
        }

        for (Item attr1 : attrs1) {
            boolean found = false;
            for (Item attr2 : attrs2) {
                if (deepEqualAttributeNodes(attr1, attr2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }
}
