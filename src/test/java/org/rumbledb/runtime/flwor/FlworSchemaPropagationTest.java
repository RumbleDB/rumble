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
package org.rumbledb.runtime.flwor;

import java.util.*;

import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.*;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FlworSchemaValidationError;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.TupleRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.clauses.*;
import org.rumbledb.runtime.flwor.expression.*;
import org.rumbledb.runtime.flwor.tuple.FlworTuple;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.*;

class FlworSchemaPropagationTest {
    private static final Name X = Name.createVariableInNoNamespace("x");
    private static final Name N = Name.createVariableInNoNamespace("n");
    private static final Name Y = Name.createVariableInNoNamespace("y");
    private static SparkSession spark;

    @BeforeAll
    static void startSpark() {
        spark = SparkSessionManager.getInstance().getOrCreateSession();
    }

    private RuntimeStaticContext context(RumbleConfiguration config, ExecutionMode mode, String type) {
        return RuntimeStaticContext.builder()
                .configuration(config)
                .executionMode(mode)
                .metadata(ExceptionMetadata.EMPTY_METADATA)
                .staticType(SequenceType.createSequenceType(type))
                .build();
    }

    private FlworDataFrame input() {
        FLWORSchema schema = new FLWORSchema();
        schema.addColumn(
                X,
                new FLWORSchema.ColumnBinding(
                        "x",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.LongType));
        FlworDataFrame frame = new FlworDataFrame(spark.range(3).selectExpr("id AS x"), schema);
        // Spark sees a long, but the logical type is deliberately more precise.
        frame.setVariableType(X, SequenceType.createSequenceType("int"));
        return frame;
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void countWhereOrderAndLetPreserveLogicalTypes(boolean nativeExecution) {
        RumbleConfiguration config = RumbleConfiguration.builder()
                .configureRuntime(r -> r.useNativeExecution(nativeExecution))
                .build();
        RuntimeStaticContext dfContext = context(config, ExecutionMode.DATAFRAME, "item*");
        DynamicContext dynamic = new DynamicContext(config);
        FlworDataFrame source = input();
        TupleRuntimePlan plan = new Source(source, dfContext);
        plan = new CountClauseIterator(plan, N, dfContext);
        plan.setInputAndOutputTupleVariableDependencies(new HashMap<>(
                Map.of(X, DynamicContext.VariableDependency.FULL, N, DynamicContext.VariableDependency.FULL)));
        FLWORSchema countSchema = ((FlworDataFrame) plan.getDataFrame(dynamic)).getSchema();
        plan = new WhereClauseIterator(
                plan,
                new ConstantRuntimeIterator(
                        ItemFactory.getInstance().createBooleanItem(true),
                        context(config, ExecutionMode.LOCAL, "boolean")),
                dfContext);
        plan = new OrderByClauseIterator(
                plan,
                List.of(new OrderByClauseAnnotatedChildIterator(
                        new VariableReferenceIterator(X, context(config, ExecutionMode.LOCAL, "int")),
                        true,
                        CollationCatalogue.CODEPOINT_COLLATION,
                        OrderByClauseSortingKey.EMPTY_ORDER.LEAST)),
                false,
                dfContext);
        plan = new LetClauseIterator(
                plan,
                Y,
                SequenceType.createSequenceType("string"),
                new ConstantRuntimeIterator(
                        ItemFactory.getInstance().createStringItem("value"),
                        context(config, ExecutionMode.LOCAL, "string")),
                dfContext);
        plan.setInputAndOutputTupleVariableDependencies(new HashMap<>(Map.of(
                X,
                DynamicContext.VariableDependency.FULL,
                N,
                DynamicContext.VariableDependency.FULL,
                Y,
                DynamicContext.VariableDependency.FULL)));
        FlworDataFrame result = (FlworDataFrame) plan.getDataFrame(dynamic);
        assertEquals(SequenceType.createSequenceType("int"), result.getSchema().getVariableType(X));
        assertEquals(SequenceType.createSequenceType("long"), result.getSchema().getVariableType(N));
        assertEquals(
                SequenceType.createSequenceType("string"), result.getSchema().getVariableType(Y));
        assertEquals(countSchema.isBytes(N), result.getSchema().isBytes(N));
        assertEquals(countSchema.getNativeType(N), result.getSchema().getNativeType(N));
        assertTrue(result.isConsistent());
        assertEquals(3, result.getDataFrame().count());
        assertEquals(SequenceType.createSequenceType("int"), source.getVariableType(X));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void forPreservesInputTypesAndRecordsPosition(boolean nativeExecution) {
        RumbleConfiguration config = RumbleConfiguration.builder()
                .configureRuntime(r -> r.useNativeExecution(nativeExecution))
                .build();
        RuntimeStaticContext dfContext = context(config, ExecutionMode.DATAFRAME, "item*");
        ForClauseIterator plan = new ForClauseIterator(
                new Source(input(), dfContext),
                Y,
                N,
                false,
                new ConstantRuntimeIterator(
                        ItemFactory.getInstance().createStringItem("value"),
                        context(config, ExecutionMode.LOCAL, "string")),
                dfContext);
        plan.setInputAndOutputTupleVariableDependencies(new HashMap<>(Map.of(
                X,
                DynamicContext.VariableDependency.FULL,
                Y,
                DynamicContext.VariableDependency.FULL,
                N,
                DynamicContext.VariableDependency.FULL)));
        FlworDataFrame result = (FlworDataFrame) plan.getDataFrame(new DynamicContext(config));
        assertEquals(SequenceType.createSequenceType("int"), result.getVariableType(X));
        assertEquals(SequenceType.createSequenceType("string"), result.getVariableType(Y));
        assertEquals(SequenceType.createSequenceType("long"), result.getVariableType(N));
        assertEquals(3, result.getDataFrame().count());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void groupingPreservesItemTypesAndWidensCollectedSequences(boolean nativeExecution) {
        RumbleConfiguration config = RumbleConfiguration.builder()
                .configureRuntime(r -> r.useNativeExecution(nativeExecution))
                .build();
        RuntimeStaticContext dfContext = context(config, ExecutionMode.DATAFRAME, "item*");
        FLWORSchema schema = new FLWORSchema();
        schema.addColumn(
                X,
                new FLWORSchema.ColumnBinding(
                        "x",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.IntegerType));
        schema.addColumn(
                Y,
                new FLWORSchema.ColumnBinding(
                        "y",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.StringType));
        FlworDataFrame source =
                new FlworDataFrame(spark.sql("SELECT 1 AS x, 'a' AS y UNION ALL SELECT 1, 'b'"), schema);
        source.setVariableType(X, SequenceType.createSequenceType("int"));
        source.setVariableType(Y, SequenceType.createSequenceType("string"));
        GroupByClauseIterator plan = new GroupByClauseIterator(
                new Source(source, dfContext),
                List.of(new GroupByClauseSparkIteratorExpression(
                        null, X, CollationCatalogue.CODEPOINT_COLLATION, null)),
                dfContext);
        plan.setInputAndOutputTupleVariableDependencies(new HashMap<>(
                Map.of(X, DynamicContext.VariableDependency.FULL, Y, DynamicContext.VariableDependency.FULL)));
        FlworDataFrame result = (FlworDataFrame) plan.getDataFrame(new DynamicContext(config));
        assertEquals(SequenceType.createSequenceType("int"), result.getVariableType(X));
        assertEquals(SequenceType.createSequenceType("string*"), result.getVariableType(Y));
        assertEquals(1, result.getDataFrame().count());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void joinsCarryBothInputsAndMakeOuterBindingsOptional(boolean leftOuter) {
        RumbleConfiguration config = RumbleConfiguration.defaultConfiguration();
        RuntimeStaticContext dfContext = context(config, ExecutionMode.DATAFRAME, "item*");
        FLWORSchema schema = new FLWORSchema();
        schema.addColumn(
                Y,
                new FLWORSchema.ColumnBinding(
                        "stored_y",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.StringType));
        FlworDataFrame right = new FlworDataFrame(spark.sql("SELECT 'value' AS stored_y"), schema);
        right.setVariableType(Y, SequenceType.createSequenceType("string"));
        Name flag = Name.createVariableInNoNamespace("flag");
        FlworDataFrame left = input();
        FLWORSchema withFlag = left.getSchema();
        withFlag.replaceVariable(
                X,
                SequenceType.createSequenceType("int"),
                List.of(new FLWORSchema.ColumnBinding(
                        "stored_x",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.LongType)));
        withFlag.replaceVariable(
                flag,
                SequenceType.createSequenceType("boolean"),
                List.of(new FLWORSchema.ColumnBinding(
                        "flag",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.BooleanType)));
        left = new FlworDataFrame(left.getDataFrame().selectExpr("x AS stored_x", "true AS flag"), withFlag);
        FlworDataFrame result = JoinClauseIterator.joinInputTupleWithSequenceOnPredicate(
                new DynamicContext(config),
                left,
                right,
                Map.of(X, DynamicContext.VariableDependency.FULL, Y, DynamicContext.VariableDependency.FULL),
                List.of(X, flag),
                List.of(Y),
                new VariableReferenceIterator(flag, context(config, ExecutionMode.LOCAL, "boolean")),
                leftOuter,
                null,
                ExceptionMetadata.EMPTY_METADATA,
                dfContext);
        assertEquals(SequenceType.createSequenceType("int"), result.getVariableType(X));
        assertEquals(SequenceType.createSequenceType(leftOuter ? "string?" : "string"), result.getVariableType(Y));
        assertEquals(3, result.getDataFrame().count());
    }

    @Test
    void nativeWherePreservesLogicalMetadata() {
        RumbleConfiguration config = RumbleConfiguration.defaultConfiguration();
        Name flag = Name.createVariableInNoNamespace("flag");
        FlworDataFrame source = input();
        FLWORSchema withFlag = source.getSchema();
        withFlag.replaceVariable(
                flag,
                SequenceType.createSequenceType("boolean"),
                List.of(new FLWORSchema.ColumnBinding(
                        "flag",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.BooleanType)));
        source = new FlworDataFrame(source.getDataFrame().selectExpr("*", "true AS flag"), withFlag);
        FlworDataFrame result = WhereClauseIterator.tryNativeQuery(
                source,
                new VariableReferenceIterator(flag, context(config, ExecutionMode.LOCAL, "boolean")),
                new DynamicContext(config),
                ExceptionMetadata.EMPTY_METADATA);
        assertNotNull(result, "The test must exercise native SQL, not a fallback");
        assertEquals(SequenceType.createSequenceType("int"), result.getVariableType(X));
        assertEquals(3, result.getDataFrame().count());
    }

    @Test
    void letRebindingReplacesThePreviousLogicalType() {
        RumbleConfiguration config = RumbleConfiguration.defaultConfiguration();
        RuntimeStaticContext dfContext = context(config, ExecutionMode.DATAFRAME, "item*");
        FlworDataFrame source = input();
        LetClauseIterator plan = new LetClauseIterator(
                new Source(source, dfContext),
                X,
                SequenceType.createSequenceType("string"),
                new ConstantRuntimeIterator(
                        ItemFactory.getInstance().createStringItem("replacement"),
                        context(config, ExecutionMode.LOCAL, "string")),
                dfContext);
        plan.setInputAndOutputTupleVariableDependencies(
                new HashMap<>(Map.of(X, DynamicContext.VariableDependency.FULL)));
        FlworDataFrame result = (FlworDataFrame) plan.getDataFrame(new DynamicContext(config));
        assertEquals(
                SequenceType.createSequenceType("string"), result.getSchema().getVariableType(X));
        assertEquals(SequenceType.createSequenceType("int"), source.getVariableType(X));
        assertEquals(3, result.getDataFrame().count());
    }

    @Test
    void projectionRebindingAndSnapshotsDoNotLeakTypes() {
        FlworDataFrame source = input();
        FLWORSchema snapshot = source.getSchema();
        snapshot.setVariableType(X, SequenceType.createSequenceType("string"));
        assertEquals(SequenceType.createSequenceType("int"), source.getVariableType(X));
        FlworDataFrame filtered = source.sql("SELECT * FROM " + source.createTempView() + " WHERE x > 0");
        assertEquals(SequenceType.createSequenceType("int"), filtered.getVariableType(X));
        FLWORSchema replacement = source.getSchema();
        replacement.replaceVariable(
                X,
                SequenceType.createSequenceType("string"),
                List.of(new FLWORSchema.ColumnBinding(
                        "x",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.StringType)));
        FlworDataFrame rebound = new FlworDataFrame(spark.sql("SELECT 'replacement' AS x"), replacement);
        assertEquals(SequenceType.createSequenceType("string"), rebound.getVariableType(X));
        assertEquals(SequenceType.createSequenceType("int"), source.getVariableType(X));
        assertThrows(
                FlworSchemaValidationError.class,
                () -> new FlworDataFrame(spark.sql("SELECT 'replacement' AS x"), source.getSchema()));
        assertThrows(
                FlworSchemaValidationError.class,
                () -> new FlworDataFrame(
                        source.getDataFrame().selectExpr("x AS y"),
                        source.getSchema().selectVariables(List.of())));
    }

    @Test
    void explicitBindingsDriveColumnsDependenciesAndProjection() {
        FLWORSchema schema = new FLWORSchema();
        schema.addColumn(
                X,
                new FLWORSchema.ColumnBinding(
                        "value_column",
                        FlworDataFrameColumn.ColumnFormat.FULLY_NATIVE,
                        org.apache.spark.sql.types.DataTypes.IntegerType));
        schema.addColumn(
                X,
                new FLWORSchema.ColumnBinding(
                        "count_column",
                        FlworDataFrameColumn.ColumnFormat.COUNT,
                        org.apache.spark.sql.types.DataTypes.LongType));
        schema.setVariableType(X, SequenceType.createSequenceType("int"));
        FlworDataFrame frame =
                new FlworDataFrame(spark.sql("SELECT 7 AS value_column, CAST(1 AS BIGINT) AS count_column"), schema);
        assertEquals(List.of(X), frame.getVariableNames());
        assertEquals(
                "value_column",
                frame.getColumns(Map.of(X, DynamicContext.VariableDependency.FULL), null, null)
                        .get(0)
                        .getColumnName());
        assertEquals(
                "count_column",
                frame.getColumns(Map.of(X, DynamicContext.VariableDependency.COUNT), null, null)
                        .get(0)
                        .getColumnName());
        assertTrue(frame.getColumns(null, null, List.of(X)).isEmpty());
        FlworDataFrame projected = new FlworDataFrame(
                frame.getDataFrame().select("value_column"),
                frame.getSchema().project(Map.of(X, DynamicContext.VariableDependency.FULL), false));
        assertEquals(List.of(X), projected.getVariableNames());
        assertEquals(SequenceType.createSequenceType("int"), projected.getVariableType(X));
        assertEquals("`value_column`", projected.getColumns().get(0).toString());
        assertTrue(projected.isConsistent());
        assertEquals(7, projected.getDataFrame().first().getInt(0));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void forReplacementBuildsANewBindingBeforeWrappingRows(boolean nativeExecution) {
        RumbleConfiguration config = RumbleConfiguration.builder()
                .configureRuntime(r -> r.useNativeExecution(nativeExecution))
                .build();
        RuntimeStaticContext dfContext = context(config, ExecutionMode.DATAFRAME, "item*");
        FlworDataFrame source = input();
        ForClauseIterator plan = new ForClauseIterator(
                new Source(source, dfContext),
                X,
                null,
                false,
                new ConstantRuntimeIterator(
                        ItemFactory.getInstance().createStringItem("replacement"),
                        context(config, ExecutionMode.LOCAL, "string")),
                dfContext);
        plan.setInputAndOutputTupleVariableDependencies(
                new HashMap<>(Map.of(X, DynamicContext.VariableDependency.FULL)));
        FlworDataFrame result = (FlworDataFrame) plan.getDataFrame(new DynamicContext(config));
        assertEquals(SequenceType.createSequenceType("string"), result.getVariableType(X));
        assertEquals(SequenceType.createSequenceType("int"), source.getVariableType(X));
        assertTrue(result.isConsistent());
        assertEquals(3, result.getDataFrame().count());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void groupingWithNoOutputVariablesKeepsGroupCount(boolean nativeExecution) {
        RumbleConfiguration config = RumbleConfiguration.builder()
                .configureRuntime(r -> r.useNativeExecution(nativeExecution))
                .build();
        RuntimeStaticContext dfContext = context(config, ExecutionMode.DATAFRAME, "item*");
        GroupByClauseIterator plan = new GroupByClauseIterator(
                new Source(input(), dfContext),
                List.of(new GroupByClauseSparkIteratorExpression(
                        null, X, CollationCatalogue.CODEPOINT_COLLATION, null)),
                dfContext);
        plan.setInputAndOutputTupleVariableDependencies(new HashMap<>());
        FlworDataFrame result = (FlworDataFrame) plan.getDataFrame(new DynamicContext(config));
        assertTrue(result.getVariableNames().isEmpty());
        assertEquals(0, result.getDataFrame().schema().fields().length);
        assertTrue(result.isConsistent());
        assertEquals(3, result.getDataFrame().count());
    }

    private static final class Source extends TupleRuntimePlan implements DataFrameRuntimePlan<FlworTuple> {
        private final FlworDataFrame frame;

        Source(FlworDataFrame frame, RuntimeStaticContext context) {
            super(null, context);
            this.frame = frame;
        }

        public FlworDataFrame createNativeDataFrame(DynamicContext context) {
            return this.frame;
        }

        public boolean isSparkJobNeeded() {
            return false;
        }

        public boolean containsClause(org.rumbledb.expressions.flowr.FLWOR_CLAUSES kind) {
            return false;
        }

        public Cursor<FlworTuple> createNativeCursor(DynamicContext context) {
            throw new UnsupportedOperationException();
        }

        public Set<Name> getOutputTupleVariableNames() {
            return new HashSet<>(this.frame.getVariableNames());
        }

        public Map<Name, DynamicContext.VariableDependency> getDynamicContextVariableDependencies() {
            return new HashMap<>();
        }

        protected Map<Name, DynamicContext.VariableDependency> getInputTupleVariableDependencies(
                Map<Name, DynamicContext.VariableDependency> projection) {
            return new HashMap<>();
        }
    }
}
