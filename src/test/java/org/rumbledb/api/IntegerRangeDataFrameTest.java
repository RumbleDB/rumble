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

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.TypeMappings;

class IntegerRangeDataFrameTest {
    @TempDir
    Path directory;

    static Stream<Arguments> integerSchemas() {
        return Stream.of(
                Arguments.of("({\"n\":1},{})", DataTypes.IntegerType),
                Arguments.of("({\"n\":null},{},{\"n\":3})", DataTypes.IntegerType),
                Arguments.of("({\"n\":-2147483648},{\"n\":2147483647})", DataTypes.IntegerType),
                Arguments.of("({\"n\":2147483648},{\"n\":null},{\"n\":1})", DataTypes.LongType),
                Arguments.of("({\"n\":-2147483649},{\"n\":1})", DataTypes.LongType),
                Arguments.of("({\"n\":-9223372036854775808},{\"n\":9223372036854775807})", DataTypes.LongType),
                Arguments.of("({\"n\":9223372036854775808},{\"n\":null},{\"n\":1})", TypeMappings.integerType),
                Arguments.of("({\"n\":-9223372036854775809},{\"n\":1})", TypeMappings.integerType),
                Arguments.of("({\"n\":1},{\"n\":null},{\"n\":1.5})", TypeMappings.decimalType),
                Arguments.of("({\"n\":1},{\"n\":null},{\"n\":1.5e0})", DataTypes.DoubleType));
    }

    @ParameterizedTest
    @MethodSource("integerSchemas")
    void narrowsByValueRangeWithoutTruncationLocallyAndAcrossPartitions(String query, DataType expected) {
        SequenceOfItems sequence = new Rumble(new RumbleConfiguration()).runQuery(query);
        var staticType = sequence.getRuntimeStaticContext().getStaticType();
        List<Item> items = sequence.getAsList();
        List<BigDecimal> values = items.stream()
                .map(item -> item.getItemByKey("n"))
                .map(item -> item == null || item.isNull()
                        ? null
                        : new BigDecimal(item.getStringValue()).stripTrailingZeros())
                .toList();
        Dataset<Row> local = sequence.getAsDataFrame();
        assertEquals(expected, local.schema().apply("n").dataType());
        assertValues(values, local);

        var rdd = SparkSessionManager.getInstance().getJavaSparkContext().parallelize(items, 3);
        Dataset<Row> distributed = ItemRuntimeDataFrameFactory.INSTANCE
                .fromRDD(
                        rdd,
                        new DynamicContext(sequence.getRuntimeStaticContext().getConfiguration()),
                        sequence.getRuntimeStaticContext())
                .getDataFrame();
        assertEquals(expected, distributed.schema().apply("n").dataType());
        assertValues(values, distributed);
        String output = this.directory.resolve("parquet").toString();
        local.write().parquet(output);
        assertValues(values, local.sparkSession().read().parquet(output));
        assertSame(staticType, sequence.getRuntimeStaticContext().getStaticType());
    }

    private void assertValues(List<BigDecimal> expected, Dataset<Row> frame) {
        // Parquet may return rows in a different order.
        List<BigDecimal> actual = frame.collectAsList().stream()
                .map(row -> row.isNullAt(0) ? null : new BigDecimal(row.get(0).toString()).stripTrailingZeros())
                .toList();
        assertEquals(expected.size(), actual.size());
        assertEquals(
                expected.stream().filter(value -> value == null).count(),
                actual.stream().filter(value -> value == null).count());
        assertEquals(
                expected.stream().filter(value -> value != null).sorted().toList(),
                actual.stream().filter(value -> value != null).sorted().toList());
    }

    @Test
    void narrowsNestedAndNullableIntegersButKeepsStaticAndDeclaredTypes() {
        Rumble rumble = new Rumble(new RumbleConfiguration());
        var sequence = rumble.runQuery("({\"n\":1},{\"n\":null},{\"n\":3})");
        var staticType = sequence.getRuntimeStaticContext().getStaticType();
        assertEquals(
                BuiltinTypesCatalogue.integerItem,
                staticType.getItemType().getObjectContentFacet("n").getType().getSingleNullableType());
        Dataset<Row> nullable = sequence.getAsDataFrame();
        assertEquals(DataTypes.IntegerType, nullable.schema().apply("n").dataType());
        assertTrue(nullable.schema().apply("n").nullable());
        assertSame(staticType, sequence.getRuntimeStaticContext().getStaticType());
        Dataset<Row> nested =
                rumble.runQuery("{\"o\":{\"n\":1},\"a\":[1,null,2147483648]}").getAsDataFrame();
        assertEquals(
                DataTypes.IntegerType,
                ((StructType) nested.schema().apply("o").dataType()).apply("n").dataType());
        assertEquals(DataTypes.LongType, ((ArrayType) nested.schema().apply("a").dataType()).elementType());
        assertEquals(
                List.of(1L, 2147483648L),
                nested.collectAsList().get(0).<Long>getList(1).stream()
                        .filter(value -> value != null)
                        .toList());
        Dataset<Row> explicit = rumble.runQuery(
                        "declare type local:row as {\"n\":\"integer\"}; validate type local:row* { ({\"n\":1},{\"n\":2}) }")
                .getAsDataFrame();
        assertEquals(TypeMappings.integerType, explicit.schema().apply("n").dataType());
    }
}
