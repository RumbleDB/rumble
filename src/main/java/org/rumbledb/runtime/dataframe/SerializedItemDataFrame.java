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
package org.rumbledb.runtime.dataframe;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.StructType;

import org.rumbledb.api.Item;
import org.rumbledb.items.parsing.ItemUserDefinedType;
import org.rumbledb.spark.SparkSessionManager;

/** Builds DataFrames that preserve heterogeneous items without schema inference. */
public final class SerializedItemDataFrame {
    private SerializedItemDataFrame() {}

    public static Dataset<Row> fromRDD(JavaRDD<Item> items) {
        StructType schema = new StructType().add("item", new ItemUserDefinedType(), false);
        return SparkSessionManager.getInstance()
                .getOrCreateSession()
                .createDataFrame(items.map(item -> RowFactory.create(item)), schema);
    }
}
