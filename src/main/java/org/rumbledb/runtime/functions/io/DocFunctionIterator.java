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
package org.rumbledb.runtime.functions.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.net.URI;
import java.util.List;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.XmlParsingUtils;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * The `DocFunctionIterator` class implements the `doc` function from XQuery.
 * It retrieves and parses an XML document from a given URI.
 */
public class DocFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public DocFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item path = this.getChild(0).materializeFirstOrNull(context);
        return path == null ? null : loadDocument(path, context);
    }

    private Item loadDocument(Item path, DynamicContext context) {
        URI uri = FileSystemUtil.resolveURI(this.staticContext.getStaticURI(), path.getStringValue(), getMetadata());
        try (InputStream xmlFileStream = FileSystemUtil.getDataInputStream(uri, getMetadata())) {
            Document xmlDocument = XmlParsingUtils.parseResource(
                    new InputSource(xmlFileStream),
                    uri.toString(),
                    "XML document \"" + uri + "\" for fn:doc()",
                    getMetadata());
            DocumentItem documentItem = ItemParser.getDocumentItemFromXML(
                    xmlDocument,
                    uri.toString(),
                    context.getRumbleConfiguration().optimization().optimizeParentPointers());
            documentItem.setConstructionBaseUri(uri);
            return documentItem;
        } catch (IOException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException(
                    "Unable to read the resource supplied to fn:doc().", getMetadata());
            ex.initCause(e);
            throw ex;
        }
    }
}
