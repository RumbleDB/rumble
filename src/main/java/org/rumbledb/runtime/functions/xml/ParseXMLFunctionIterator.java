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

import java.io.IOException;
import java.io.Serial;
import java.io.StringReader;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidXmlDocumentException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ParseXMLFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ParseXMLFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
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
                    context.getRumbleConfiguration().optimization().optimizeParentPointers());
        } catch (ParserConfigurationException e) {
            throw new OurBadException("Document builder creation failed with: " + e);
        } catch (SAXException | IOException e) {
            throw new InvalidXmlDocumentException(
                    "fn:parse-xml: the argument is not a well-formed and namespace-well-formed XML document",
                    getMetadata());
        }
    }
}
