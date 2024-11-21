package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
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
                InputStream xmlFileStream = FileSystemUtil.getDataInputStream(
                    uri,
                    this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                    getMetadata()
                );
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document xmlDocument = documentBuilder.parse(xmlFileStream);
                Item res = ItemParser.getItemFromXML(xmlDocument, uri.toString());
                return res;
            } catch (ParserConfigurationException e) {
                throw new OurBadException("Document builder creation failed with: " + e);
            } catch (IOException e) {
                throw new RuntimeException("IOException while reading XML document." + e);
            } catch (SAXException e) {
                throw new RuntimeException("SAXException while reading XML document." + e);
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " xml-doc function", getMetadata());
    }
}
