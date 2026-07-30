package org.rumbledb.runtime.functions.io;

import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;


import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.net.URI;
import java.util.List;

/**
 * The `DocFunctionIterator` class implements the `doc` function from XQuery.
 * It retrieves and parses an XML document from a given URI.
 */
public class DocFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public DocFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item path = this.getChild(0).materializeFirstOrNull(context);
        return path == null ? null : loadDocument(path, context);
    }

    private Item loadDocument(Item path, DynamicContext context) {
        try {
            URI uri = FileSystemUtil.resolveURI(
                this.staticContext.getStaticURI(),
                path.getStringValue(),
                getMetadata()
            );
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            try (
                InputStream xmlFileStream = FileSystemUtil.getDataInputStream(
                    uri,
                    context.getRumbleRuntimeConfiguration(),
                    getMetadata()
                )
            ) {
                Document xmlDocument = documentBuilder.parse(xmlFileStream);
                return ItemParser.getItemFromXML(
                    xmlDocument,
                    uri.toString(),
                    context.getRumbleRuntimeConfiguration()
                        .optimizeParentPointers()
                );
            }
        } catch (ParserConfigurationException e) {
            throw new OurBadException("Document builder creation failed with: " + e);
        } catch (CannotRetrieveResourceException e) {
            throw e;
        } catch (IOException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException(
                    "Unable to read the resource supplied to fn:doc().",
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        } catch (SAXException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException(
                    "Unable to parse the resource supplied to fn:doc() as well-formed XML.",
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }
}
