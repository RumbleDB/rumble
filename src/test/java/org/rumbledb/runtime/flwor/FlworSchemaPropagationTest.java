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
        FlworDataFrame frame = new FlworDataFrame(spark.range(3).selectExpr("id AS x"));
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
        assertTrue(result.getSchema().isBytes(N));
        assertNull(result.getSchema().getNativeType(N));
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
        FlworDataFrame source = new FlworDataFrame(spark.sql("SELECT 1 AS x, 'a' AS y UNION ALL SELECT 1, 'b'"));
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
        FlworDataFrame right = new FlworDataFrame(spark.sql("SELECT 'value' AS y"));
        right.setVariableType(Y, SequenceType.createSequenceType("string"));
        Name flag = Name.createVariableInNoNamespace("flag");
        FlworDataFrame left = input();
        left = left.withDataFrame(left.getDataFrame().selectExpr("*", "true AS flag"));
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
        source = source.withDataFrame(source.getDataFrame().selectExpr("*", "true AS flag"));
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
        FlworDataFrame rebound = source.withDataFrame(spark.sql("SELECT 'replacement' AS x"), X);
        assertEquals(SequenceType.createSequenceType("item*"), rebound.getVariableType(X));
        FlworDataFrame projected = source.withDataFrame(source.getDataFrame().selectExpr("x AS y"));
        assertFalse(projected.hasVariableName(X));
        assertEquals(SequenceType.createSequenceType("item*"), projected.getVariableType(Y));
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
