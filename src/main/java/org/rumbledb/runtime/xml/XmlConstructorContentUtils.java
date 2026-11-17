/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.xml;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionItemInConstructorContentException;
import org.rumbledb.items.ItemFactory;

final class XmlConstructorContentUtils {

    private XmlConstructorContentUtils() {}

    /** Joins atomic runs before document nodes are replaced by their children. */
    static List<Item> normalizeContent(List<Item> items, ExceptionMetadata metadata) {
        List<Item> flattened = new ArrayList<>();
        for (Item item : items) {
            appendExpandedItem(item, flattened, false);
        }
        List<Item> result = new ArrayList<>();
        StringBuilder atomicRun = null;
        for (Item item : flattened) {
            // Arrays have already been flattened; remaining functions and maps cannot become XML content.
            if (item.isFunction() || item.isMap()) {
                throw new FunctionItemInConstructorContentException(
                        "A function item cannot be used as element or document constructor content.", metadata);
            }
            if (!item.isNode()) {
                if (atomicRun == null) {
                    atomicRun = new StringBuilder();
                } else {
                    atomicRun.append(' ');
                }
                atomicRun.append(item.getStringValue());
            } else {
                appendAtomicRun(atomicRun, result);
                atomicRun = null;
                result.add(item);
            }
        }
        appendAtomicRun(atomicRun, result);
        return result;
    }

    private static void appendAtomicRun(StringBuilder atomicRun, List<Item> result) {
        if (atomicRun != null && !atomicRun.isEmpty()) {
            result.add(ItemFactory.getInstance().createXmlTextNode(atomicRun.toString()));
        }
    }

    static void appendExpandedItem(Item item, List<Item> result) {
        appendExpandedItem(item, result, true);
    }

    private static void appendExpandedItem(Item item, List<Item> result, boolean expandDocuments) {
        if (expandDocuments && item.isDocumentNode()) {
            for (Item child : item.children()) {
                appendExpandedItem(child, result, true);
            }
            return;
        }
        if (!item.isArray()) {
            result.add(item);
            return;
        }
        if (item.isArrayOfItems()) {
            for (Item member : item.getItemMembers()) {
                appendExpandedItem(member, result, expandDocuments);
            }
            return;
        }
        for (List<Item> memberSequence : item.getSequenceMembers()) {
            for (Item member : memberSequence) {
                appendExpandedItem(member, result, expandDocuments);
            }
        }
    }
}
