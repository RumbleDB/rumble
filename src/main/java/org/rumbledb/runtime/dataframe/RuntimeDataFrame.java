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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * A Spark DataFrame whose rows represent runtime values of type {@code T}.
 *
 * <p>
 * Spark stores the physical representation as {@link Row}; implementations define how rows are mapped back to the
 * logical runtime type.
 * </p>
 *
 * @param <T> the logical runtime value represented by each row
 */
public interface RuntimeDataFrame<T> extends Serializable {

    /**
     * Returns the underlying physical Spark DataFrame.
     */
    Dataset<Row> getDataFrame();

    /**
     * Converts this DataFrame to its logical runtime representation.
     *
     * @param metadata query metadata used if a row cannot be decoded
     * @return an RDD of logical runtime values
     */
    JavaRDD<T> toRDD(ExceptionMetadata metadata);

    /**
     * Materializes this DataFrame as logical runtime values.
     *
     * @param metadata query metadata used if a row cannot be decoded
     * @return a list of logical runtime values
     */
    default List<T> toList(ExceptionMetadata metadata) {
        return toRDD(metadata).collect();
    }
}
