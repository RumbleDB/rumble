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
package org.rumbledb.runtime.flwor.closures;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import org.rumbledb.api.Item;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.udfs.DataFrameContext;

public class ItemsToBinaryColumn implements Function<Item, Row> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final DataFrameContext dataFrameContext;

    public ItemsToBinaryColumn() {
        this.dataFrameContext = new DataFrameContext();
    }

    /**
     * @param item the item to serialize.
     * @return Row object, containing byte array of a singleton list containing the given item
     */
    @Override
    public Row call(Item item) {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        return RowFactory.create((Object) FlworDataFrameUtils.serializeItemList(
                itemList, this.dataFrameContext.getKryo(), this.dataFrameContext.getOutput()));
    }
}
