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
package org.rumbledb.runtime.plan;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;

/**
 * Native execution using a DataFrame of serialized items, exposed as an RDD capability.
 *
 * <p>
 * This representation preserves heterogeneous items. It deliberately does not implement
 * {@link DataFrameRuntimePlan}: consumers of that capability expect homogeneous, queryable columns.
 * The default RDD adapter lets the central plan dispatcher handle cursor and materialization boundaries.
 */
public interface ItemDataFrameRuntimePlan extends RDDRuntimePlan<Item> {
    Dataset<Row> createNativeItemDataFrame(DynamicContext context);

    @Override
    default JavaRDD<Item> createNativeRDD(DynamicContext context) {
        return createNativeItemDataFrame(context).select("item").toJavaRDD().map(row -> (Item) row.getAs("item"));
    }
}
