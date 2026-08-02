package org.rumbledb.runtime.functions.xml;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.InvalidXmlDocumentException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serial;
import java.io.StringReader;
import java.util.List;

public class ParseXMLFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ParseXMLFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item arg = this.getChild(0).materializeFirstOrNull(context);
        if (arg == null) {
            return null;
        }
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(new InputSource(new StringReader(arg.getStringValue())));
            return ItemParser.getItemFromXML(
                xmlDocument,
                XMLDocumentPosition.generateConstructedTreePath(),
                context.getRumbleRuntimeConfiguration().optimizeParentPointers()
            );
        } catch (ParserConfigurationException e) {
            throw new OurBadException("Document builder creation failed with: " + e);
        } catch (SAXException | IOException e) {
            throw new InvalidXmlDocumentException(
                    "fn:parse-xml: the argument is not a well-formed and namespace-well-formed XML document",
                    getMetadata()
            );
        }
    }
}
