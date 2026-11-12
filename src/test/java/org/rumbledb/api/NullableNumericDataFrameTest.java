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
package org.rumbledb.api;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.typing.TypeInferrenceUtils;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.TypeMappings;

class NullableNumericDataFrameTest {
    @TempDir
    Path directory;

    @ParameterizedTest
    @ValueSource(
            strings = {
                "NullableNumeric1",
                "NullableNumeric2",
                "NullableNumeric3",
                "NullableNumeric4",
                "NullableNumeric5",
                "NullableNumeric6"
            })
    void writesNullableNumericValuesLocallyAndFromRDD(String name) throws Exception {
        String query;
        try (var input =
                getClass().getResourceAsStream("/test_files/runtime-spark/NullableNumericDataFrames/" + name + ".jq")) {
            assertNotNull(input);
            query = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
        SequenceOfItems sequence = new Rumble(new RumbleConfiguration()).runQuery(query);
        List<Item> items = sequence.getAsList();
        String staticTypeBefore =
                sequence.getRuntimeStaticContext().getStaticType().toString();
        var expectedType = query.contains("e0") ? DataTypes.DoubleType : TypeMappings.decimalType;
        Dataset<Row> local = sequence.getAsDataFrame();
        assertEquals(expectedType, local.schema().apply("n").dataType());
        assertTrue(local.schema().apply("n").nullable());
        assertValues(items, local.collectAsList());

        Path output = this.directory.resolve("parquet");
        sequence.write().format("parquet").save(output.toString());
        Dataset<Row> restored = local.sparkSession().read().parquet(output.toString());
        assertEquals(expectedType, restored.schema().apply("n").dataType());
        assertValues(items, restored.collectAsList());

        var rdd = SparkSessionManager.getInstance().getJavaSparkContext().parallelize(items, 3);
        ItemType inferred = TypeInferrenceUtils.inferItemTypeOfRDDItems(
                rdd, ExceptionMetadata.EMPTY_METADATA, TypeInferrenceUtils.TypeMergeMode.DATAFRAME);
        assertTrue(inferred.isCompatibleWithDataFrames(
                sequence.getRuntimeStaticContext().getConfiguration()));
        Dataset<Row> distributed = ItemRuntimeDataFrameFactory.INSTANCE
                .fromRDD(
                        rdd,
                        new DynamicContext(sequence.getRuntimeStaticContext().getConfiguration()),
                        sequence.getRuntimeStaticContext())
                .getDataFrame();
        assertEquals(expectedType, distributed.schema().apply("n").dataType());
        assertValues(items, distributed.collectAsList());
        assertEquals(
                staticTypeBefore,
                sequence.getRuntimeStaticContext().getStaticType().toString());
    }

    @Test
    void convertsTopLevelAndNestedNullableNumericUnions() {
        Rumble rumble = new Rumble(new RumbleConfiguration());
        Dataset<Row> withoutNull = rumble.runQuery("({\"n\":1},{\"n\":2.5e0})").getAsDataFrame();
        assertEquals(DataTypes.DoubleType, withoutNull.schema().apply("n").dataType());
        assertEquals(
                List.of(1.0, 2.5),
                withoutNull.collectAsList().stream()
                        .map(row -> row.getDouble(0))
                        .toList());
        Dataset<Row> atomic = rumble.runQuery("1, null, 2.5e0").getAsDataFrame();
        assertEquals(DataTypes.DoubleType, atomic.schema().apply(0).dataType());
        assertEquals(3, atomic.collectAsList().size());
        Dataset<Row> nested = rumble.runQuery("{\"a\":[{\"n\":1},{\"n\":null},{\"n\":2.5e0}]}")
                .getAsDataFrame();
        List<Row> elements = nested.collectAsList().get(0).getList(0);
        assertEquals(1.0, elements.get(0).getDouble(0));
        assertTrue(elements.get(1).isNullAt(0));
        assertEquals(2.5, elements.get(2).getDouble(0));
    }

    private void assertValues(List<Item> items, List<Row> rows) {
        assertEquals(3, rows.size());
        assertEquals(1, rows.stream().filter(row -> row.isNullAt(0)).count());
        List<Double> values = rows.stream()
                .filter(row -> !row.isNullAt(0))
                .map(row -> ((Number) row.get(0)).doubleValue())
                .sorted()
                .toList();
        List<Double> expected = items.stream()
                .map(item -> item.getItemByKey("n"))
                .filter(item -> !item.isNull())
                .map(Item::castToDoubleValue)
                .sorted()
                .toList();
        assertEquals(expected, values);
    }
}
