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
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class CollectionFunctionIterator extends ItemRuntimePlan implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public CollectionFunctionIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    // TODO: implement collection function

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        if (this.getChildren().isEmpty()) {
            throw new CannotRetrieveResourceException("No default collection is defined.", getMetadata());
        }
        Item stringItem = this.getChild(0).materializeFirstOrNull(context);
        if (stringItem == null) {
            throw new CannotRetrieveResourceException("No default collection is defined.", getMetadata());
        }
        String url = stringItem.getStringValue();
        URI uri = FileSystemUtil.resolveFileSystemURI(this.staticContext.getStaticURI(), url, getMetadata());
        if (!FileSystemUtil.exists(uri, getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }
        // DataFrameReader dfr = SparkSessionManager.getInstance().getOrCreateSession().read();
        return HomogeneousItemDataFrame.emptyDataFrame();
    }
}
