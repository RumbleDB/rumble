package org.rumbledb.runtime.functions.xml;

import java.io.IOException;
import java.io.Serial;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidXmlDocumentException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ParseXMLFragmentFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String WRAPPER_ELEMENT_NAME = "rumble-parse-xml-fragment-wrapper";

    public ParseXMLFragmentFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item arg = this.getChild(0).materializeFirstOrNull(context);
        if (arg == null) {
            return null;
        }
        String wrapped = "<" + WRAPPER_ELEMENT_NAME + ">" + arg.getStringValue() + "</" + WRAPPER_ELEMENT_NAME + ">";
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(new InputSource(new StringReader(wrapped)));
            Node wrapperElement = xmlDocument.getDocumentElement();

            boolean removeParentPointers =
                    context.getRumbleConfiguration().optimization().optimizeParentPointers();
            String path = XMLDocumentPosition.generateConstructedTreePath();
            List<Item> children = new ArrayList<>();
            NodeList nodeList = wrapperElement.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node child = nodeList.item(i);
                if (isSignificantChild(child)) {
                    children.add(ItemParser.getItemFromXML(child, path, removeParentPointers));
                }
            }

            Item documentItem = ItemFactory.getInstance().createXmlDocumentNode(children);
            if (!removeParentPointers) {
                documentItem.addParentToDescendants();
            }
            documentItem.setXmlDocumentPosition(path, 0);
            return documentItem;
        } catch (ParserConfigurationException e) {
            throw new OurBadException("Document builder creation failed with: " + e);
        } catch (SAXException | IOException e) {
            throw new InvalidXmlDocumentException(
                    "fn:parse-xml-fragment: the argument is not a well-formed external general parsed entity",
                    getMetadata());
        }
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
