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
package org.rumbledb.runtime.xml;

import java.io.Serial;
import java.util.Collections;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

public class PathRootRuntimeIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public PathRootRuntimeIterator(RuntimeStaticContext staticContext) {
        super(Collections.emptyList(), staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        Item node = dynamicContext
                .getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0);
        if (!node.isNode()) {
            throw new UnexpectedStaticTypeException(
                    "Leading slash path expressions require the context item to be a node [err:XPDY0050].",
                    ErrorCode.DynamicTypeTreatErrorCode,
                    getMetadata());
        }
        Item current = node;
        while (current.parent() != null) {
            current = current.parent();
        }
        if (!current.isDocumentNode()) {
            throw new UnexpectedStaticTypeException(
                    "Leading slash path expressions require the root of the context item to be a document node [err:XPDY0050].",
                    ErrorCode.DynamicTypeTreatErrorCode,
                    getMetadata());
        }
        return current;
    }
}
