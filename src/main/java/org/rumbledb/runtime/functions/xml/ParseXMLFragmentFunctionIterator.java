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
package org.rumbledb.runtime.functions.xml;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.XmlParsingUtils;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ParseXMLFragmentFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ParseXMLFragmentFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item arg = this.getChild(0).materializeFirstOrNull(context);
        if (arg == null) {
            return null;
        }
        DocumentFragment fragment = XmlParsingUtils.parseFragment(
                arg.getStringValue(), "the argument of fn:parse-xml-fragment", getMetadata());

        boolean removeParentPointers =
                context.getRumbleConfiguration().optimization().optimizeParentPointers();
        String path = XMLDocumentPosition.generateConstructedTreePath();
        List<Item> children = new ArrayList<>();
        NodeList nodeList = fragment.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node child = nodeList.item(i);
            if (isSignificantChild(child)) {
                children.add(ItemParser.getItemFromXML(child, path, removeParentPointers));
            }
        }

        DocumentItem documentItem = ItemFactory.getInstance().createXmlDocumentNode(children);
        documentItem.setConstructionBaseUri(this.staticContext.getStaticURI());
        if (!removeParentPointers) {
            documentItem.addParentToDescendants();
        }
        documentItem.setXmlDocumentPosition(path, 0);
        return documentItem;
    }

    private static boolean isSignificantChild(Node node) {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
            case Node.COMMENT_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
                return true;
            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
                return !node.getTextContent().trim().isEmpty();
            default:
                return false;
        }
    }
}
