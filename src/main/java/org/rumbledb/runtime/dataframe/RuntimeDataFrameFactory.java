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

import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;

/**
 * Creates a typed runtime DataFrame from the logical values stored in an RDD.
 *
 * @param <T> the logical runtime value represented by each DataFrame row
 */
public interface RuntimeDataFrameFactory<T> extends Serializable {

    RuntimeDataFrame<T> fromList(List<T> values, DynamicContext context, RuntimeStaticContext staticContext);

    RuntimeDataFrame<T> fromRDD(JavaRDD<T> rdd, DynamicContext context, RuntimeStaticContext staticContext);
}
