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
import java.util.Optional;

import org.rumbledb.api.Item;
import org.rumbledb.compiler.typing.ReturnTypeContext;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.items.parsing.XmlParsingUtils;
import org.rumbledb.resources.ResolvedResource;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

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
        return path == null ? null : loadXMLDocument(path, context);
    }

    private Item loadXMLDocument(Item path, DynamicContext context) {
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
                .loadXMLDocument(
                        uri, this.staticContext.getQueryLanguage(), context.getRumbleConfiguration(), getMetadata());
    }

    /** Types a document at a literal location by the name of its root element; validation supplies its schema later. */
    public static Optional<SequenceType> inferReturnType(FunctionCallExpression call, ReturnTypeContext context) {
        if (!(call.getArguments().get(0) instanceof StringLiteralExpression literal)) {
            return Optional.empty();
        }
        try {
            URI uri = FileSystemUtil.resolveURI(
                    context.staticContext().getStaticBaseURI(), literal.getValue(), call.getMetadata());
            // A supplied/cached document takes precedence over the physical resource at runtime.
            if (context.resourceResolver().document(uri) != null) {
                return Optional.empty();
            }
            try (ResolvedResource resource =
                    context.resourceResolver().resolve(uri, context.configuration(), call.getMetadata())) {
                return XmlParsingUtils.peekDocumentElementName(resource.getInputStream())
                        .map(root -> new SequenceType(
                                ItemTypeFactory.documentNodeItemType(ItemTypeFactory.elementNodeItemType(root)),
                                SequenceType.Arity.One));
            }
        } catch (Exception ignored) {
            // Retrieval and parsing errors belong to evaluation, including in unexecuted branches.
            return Optional.empty();
        }
    }
}
