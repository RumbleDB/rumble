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

import java.io.InputStream;
import java.io.Serial;
import java.net.URI;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class DocAvailableFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public DocAvailableFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item uriItem = this.getChild(0).materializeFirstOrNull(context);
        if (uriItem == null) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        try {
            URI uri = FileSystemUtil.resolveURI(
                    this.staticContext.getStaticURI(), uriItem.getStringValue(), getMetadata());
            InputStream xmlFileStream = FileSystemUtil.getDataInputStream(uri, getMetadata());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.newDocumentBuilder().parse(xmlFileStream);
            return ItemFactory.getInstance().createBooleanItem(true);
        } catch (Exception e) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
    }
}
