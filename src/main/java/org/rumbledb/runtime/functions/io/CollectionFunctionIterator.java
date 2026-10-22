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
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.resources.ResourceCollection;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

public class CollectionFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    public CollectionFunctionIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    protected ResourceCollection collection(DynamicContext context) {
        Item argument = getChildren().isEmpty() ? null : getChild(0).materializeAtMostOne(context);
        URI uri = null;
        if (argument != null) {
            try {
                uri = URI.create(argument.getStringValue());
                if (!uri.isAbsolute()) {
                    uri = this.staticContext.getStaticURI().resolve(uri);
                }
            } catch (IllegalArgumentException exception) {
                throw new CannotRetrieveResourceException(
                        "Invalid collection URI: " + argument.getStringValue(), getMetadata());
            }
        }
        return context.getResourceResolver().collection(uri, getMetadata());
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> collection(context).items().iterator(), getMetadata());
    }
}
