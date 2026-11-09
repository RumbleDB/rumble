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
package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.net.URI;
import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.runtime.update.primitives.Mode;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

public class TruncateCollectionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan targetIterator;
    private final Mode mode;

    public TruncateCollectionIterator(ItemRuntimePlan targetIterator, Mode mode, RuntimeStaticContext staticContext) {
        super(
                Arrays.asList(targetIterator),
                staticContext.toBuilder().isUpdating(true).build());
        this.targetIterator = targetIterator;
        this.mode = mode;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        Item collectionNameItem = null;
        try {
            collectionNameItem = this.targetIterator.materializeExactlyOne(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a unique string, but more than one item was provided.",
                    this.getRuntimeStaticContext().getMetadata());
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a string, but no item was provided.",
                    this.getRuntimeStaticContext().getMetadata());
        }

        if (!collectionNameItem.isString()) {
            throw new InvalidUpdateTargetException(
                    "Expecting collection name as a String, but it was: "
                            + collectionNameItem.getDynamicType().getIdentifierString(),
                    this.getRuntimeStaticContext().getMetadata());
        }
        String logicalPath = collectionNameItem.getStringValue();
        Mode mode = this.mode;
        if (mode == Mode.DELTA) {
            URI uri =
                    FileSystemUtil.resolveFileSystemURI(this.staticContext.getStaticURI(), logicalPath, getMetadata());
            if (!FileSystemUtil.exists(uri, getMetadata())) {
                throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
            }
            logicalPath = FileSystemUtil.convertURIToStringForSpark(uri);
        }
        Collection collection = new Collection(mode, logicalPath);

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = factory.createTruncateCollectionPrimitive(collection, this.getMetadata());

        PendingUpdateList pul = new PendingUpdateList();
        pul.addUpdatePrimitive(up);

        return pul;
    }
}
