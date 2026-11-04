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

import java.io.Serial;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.RumbleException;
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
        URI uri;
        try {
            uri = FileSystemUtil.resolveURI(this.staticContext.getStaticURI(), path.getStringValue(), getMetadata());
        } catch (CannotRetrieveResourceException exception) {
            if (exception.getCause() instanceof URISyntaxException) {
                throw new RumbleException(
                        "Invalid URI supplied to fn:doc(): " + path.getStringValue(),
                        ErrorCode.InvalidDocumentURIErrorCode,
                        getMetadata());
            }
            throw exception;
        }
        return context.getResourceResolver()
                .loadDocument(
                        uri, this.staticContext.getQueryLanguage(), context.getRumbleConfiguration(), getMetadata());
    }
}
