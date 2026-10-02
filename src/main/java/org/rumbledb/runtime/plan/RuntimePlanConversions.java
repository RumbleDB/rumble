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

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.exceptions.CannotMaterializeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.spark.SparkSessionManager;

/**
 * Stateless conversions between the local, RDD, and DataFrame representations used by runtime plans.
 */
public final class RuntimePlanConversions {

    private RuntimePlanConversions() {}

    public static <T> Cursor<T> rddToCursor(JavaRDD<T> rdd, int materializationCap, ExceptionMetadata metadata) {
        return new IteratorLocalCursor<>(
                () -> collectRDDWithLimit(rdd, materializationCap, metadata).iterator(), metadata);
    }

    public static <T> List<T> collectRDDWithLimit(JavaRDD<T> rdd, int materializationCap, ExceptionMetadata metadata) {
        if (materializationCap <= 0) {
            return rdd.collect();
        }

        List<T> result = rdd.take(materializationCap + 1);
        if (result.size() <= materializationCap) {
            return result;
        }

        throw new CannotMaterializeException(
                "Cannot materialize a sequence containing more than "
                        + materializationCap
                        + " items because the limit is set to "
                        + materializationCap
                        + ". This value can be configured with the --materialization-cap parameter at startup",
                metadata);
    }

    public static <T> JavaRDD<T> cursorToRDD(Cursor<T> cursor, int materializationCap, ExceptionMetadata metadata) {
        return SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .parallelize(materializeCursor(cursor, materializationCap, metadata));
    }

    public static <T> List<T> materializeCursor(Cursor<T> cursor) {
        List<T> items = new ArrayList<>();
        try (cursor) {
            while (cursor.hasNext()) {
                items.add(cursor.next());
            }
        }
        return items;
    }

    private static <T> List<T> materializeCursor(Cursor<T> cursor, int materializationCap, ExceptionMetadata metadata) {
        List<T> items = new ArrayList<>();
        try (cursor) {
            while (cursor.hasNext()) {
                if (materializationCap > 0 && items.size() == materializationCap) {
                    throw new CannotMaterializeException(
                            "Cannot convert a local sequence containing more than "
                                    + materializationCap
                                    + " items to an RDD because the materialization limit is set to "
                                    + materializationCap
                                    + ".",
                            metadata);
                }
                items.add(cursor.next());
            }
        }
        return items;
    }
}
