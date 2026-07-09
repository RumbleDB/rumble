package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * The `DocFunctionIterator` class implements the `doc` function from XQuery.
 * It retrieves and parses an XML document from a given URI.
 */
public class DocFunctionIterator extends LocalFunctionCallIterator {
    private static final long serialVersionUID = 1L;
    private RuntimeIterator pathIterator;

    public DocFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.pathIterator = this.children.get(0);
        this.pathIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.pathIterator.hasNext();
        this.pathIterator.close();
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item path = this.pathIterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            try {
                URI uri = FileSystemUtil.resolveURI(this.staticURI, path.getStringValue(), getMetadata());
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setNamespaceAware(true);
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                try (
                    InputStream xmlFileStream = FileSystemUtil.getDataInputStream(
                        uri,
                        getMetadata()
                    )
                ) {
                    Document xmlDocument = documentBuilder.parse(xmlFileStream);
                    return ItemParser.getItemFromXML(
                        xmlDocument,
                        uri.toString(),
                        this.currentDynamicContextForLocalExecution.getRumbleConfiguration()
                            .optimization()
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
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " xml-doc function", getMetadata());
    }
}
