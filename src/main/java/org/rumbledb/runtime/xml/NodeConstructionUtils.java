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

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.TypedValueUnavailableException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;

/** Applies construction mode to new elements and copied nodes, without changing source nodes. */
final class NodeConstructionUtils {
    private static final Name ANY_TYPE = new Name(Name.XS_NS, "xs", "anyType");

    private NodeConstructionUtils() {}

    static void initializeElement(ElementItem element, RuntimeStaticContext context) {
        if (context.isConstructionPreserve()) {
            element.setSchemaType(
                    new XmlSchemaTypeAnnotation(ANY_TYPE, List.of(ANY_TYPE)),
                    List.of(ItemFactory.getInstance().createUntypedAtomicItem(element.getStringValue())));
            element.setXmlSchemaNilled(false);
        }
    }

    static Item copyNode(Item item, RuntimeStaticContext context) {
        if (context.isConstructionPreserve()) {
            checkNamespaceSensitiveCopy(item, context, item.isAttributeNode());
        }
        Item copy = NamespaceFixupUtils.copyNodeForConstructor(item, context);
        if (!context.isConstructionPreserve()) {
            stripTypes(copy);
        }
        return copy;
    }

    private static void stripTypes(Item node) {
        if (node.isElementNode() || node.isAttributeNode()) {
            node.clearSchemaType();
            if (node.isAttributeNode()) {
                Name name = node.nodeName();
                node.setXmlSchemaIdentityProperties(
                        name != null && Name.XML_NS.equals(name.getNamespace()) && "id".equals(name.getLocalName()),
                        false);
            }
        }
        if (node.isElementNode()) {
            node.attributes().forEach(NodeConstructionUtils::stripTypes);
        }
        if (node.isElementNode() || node.isDocumentNode()) {
            node.children().forEach(NodeConstructionUtils::stripTypes);
        }
    }

    private static void checkNamespaceSensitiveCopy(
            Item node, RuntimeStaticContext context, boolean detachedAttribute) {
        if ((detachedAttribute || !context.isCopyNamespacesPreserve()) && hasNamespaceSensitiveValue(node)) {
            throw new RumbleException(
                    "Cannot preserve a QName typed value without its namespace bindings.",
                    ErrorCode.NamespaceSensitiveConstructionErrorCode,
                    context.getMetadata());
        }
        if (!context.isCopyNamespacesPreserve() && node.isElementNode()) {
            for (Item attribute : node.attributes()) {
                checkNamespaceSensitiveCopy(attribute, context, false);
            }
            for (Item child : node.children()) {
                checkNamespaceSensitiveCopy(child, context, false);
            }
        }
    }

    private static boolean hasNamespaceSensitiveValue(Item node) {
        if ((!node.isElementNode() && !node.isAttributeNode()) || node.getSchemaTypeAnnotation() == null) {
            return false;
        }
        try {
            return node.typedValue().stream().anyMatch(Item::isQName);
        } catch (TypedValueUnavailableException e) {
            // Element-only complex content has no typed value; its attributes and descendants are checked separately.
            return false;
        }
    }
}
