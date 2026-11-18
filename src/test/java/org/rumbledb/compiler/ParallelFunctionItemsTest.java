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
package org.rumbledb.compiler;

import java.net.URI;
import java.util.List;

import org.apache.spark.SparkConf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.spark.SparkSessionManager;

import static org.junit.jupiter.api.Assertions.*;

class ParallelFunctionItemsTest {
    @BeforeAll
    static void spark() {
        SparkSessionManager.getInstance().resetSession();
        SparkSessionManager.getInstance().initializeConfigurationAndSession(
                new SparkConf().setMaster("local[2]").setAppName("ParallelFunctionItemsTest")
                        .set("spark.driver.host", "127.0.0.1").set("spark.sql.shuffle.partitions", "2")
                        .set("spark.log.level", "ERROR"), true);
    }

    private static SequenceOfItems query(String text) {
        return query(text, true);
    }

    private static SequenceOfItems query(String text, boolean dataFrames) {
        RumbleConfiguration config = RumbleConfiguration.builder()
                .configureRuntime(runtime -> runtime.materializationCap(20).resultsSizeCap(20)
                        .useDataFrameExecution(dataFrames))
                .configureOptimization(optimization -> optimization.useFunctionInlining(false))
                .build();
        return new Rumble(config).runQuery(text, URI.create("file:///parallel-function-items.jq"));
    }

    @Test
    void localOnlyQueriesDoNotCompileDistributedVariants() {
        String q = "function($rows as integer*) as integer* { $rows }";
        var f = (org.rumbledb.items.FunctionItem) query(q).getAsList().get(0);
        assertFalse(f.hasParallelVariants());
    }

    @Test
    void identityDoesNotCollectItsDistributedArgument() {
        String q = "let $f := function($x as integer*) as integer* { $x } "
                + "return $f(parallelize(1 to 100))";
        assertEquals(100, query(q).getAsRDD().count());
        assertEquals(100, query(q, false).getAsRDD().count());
    }

    @Test
    void distributedParameterCanBeSecondAndCaptureALocalVariable() {
        String q = "let $offset := 3 let $f := function($min as integer, $rows as integer*) as integer* { "
                + "for $x in $rows where $x gt $min return $x + $offset } "
                + "return $f(50, parallelize(1 to 100))";
        List<Item> items = query(q).getAsRDD().collect();
        assertEquals(50, items.size());
        assertTrue(items.stream().allMatch(i -> i.getIntValue() > 53));
    }

    @Test
    void distributedInputCanProduceOneScalar() {
        String q = "let $f := function($x as integer*) as integer { count($x) } "
                + "return $f(parallelize(1 to 100))";
        assertEquals(100, query(q).getAsList().get(0).getIntValue());
    }

    @Test
    void namedReferenceSelectsADistributedBody() {
        String q = "declare function local:f($x as integer*) as integer* { $x }; "
                + "let $f := local:f#1 return $f(parallelize(1 to 100))";
        assertEquals(100, query(q).getAsRDD().count());
    }

    @Test
    void localAndDistributedCallsOfTheSameValueDoNotChangeEachOther() {
        String q = "let $f := function($x as integer*) as integer { count($x) } "
                + "return ($f((1,2)), $f(parallelize(1 to 100)), $f((1,2,3)))";
        assertEquals(List.of(2,100,3), query(q).getAsList().stream().map(Item::getIntValue).toList());
    }

    @Test
    void localParameterCanBePartiallyApplied() {
        String q = "let $f := function($min as integer, $rows as integer*) as integer* { "
                + "for $x in $rows where $x gt $min return $x } "
                + "let $g := $f(50, ?) return $g(parallelize(1 to 100))";
        assertEquals(50, query(q).getAsRDD().count());
    }

    @Test
    void structuredInputStaysADataFrame() {
        String q = "declare type local:row as {\"n\": \"integer\"}; "
                + "let $f := function($min as integer, $rows as local:row*) as local:row* { "
                + "for $r in $rows where $r.n gt $min return $r } "
                + "return $f(50, validate type local:row* { parallelize(for $i in 1 to 100 return {\"n\":$i}) })";
        SequenceOfItems result = query(q);
        assertTrue(result.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame());
        assertEquals(50, result.getAsDataFrame().count());
    }

    @Test
    void coercionPreservesDistributedDispatch() {
        String q = "declare function local:apply($g as function(integer*) as double*) as double* { "
                + "$g(parallelize(1 to 100)) }; "
                + "local:apply(function($rows as integer*) as integer* { $rows })";
        List<Item> result = query(q).getAsRDD().collect();
        assertEquals(100, result.size());
        assertTrue(result.stream().allMatch(Item::isDouble));
    }

    @Test
    void variantsAreSharedAcrossFunctionValuesAndRepeatedCalls() {
        String q = "declare function local:f($rows as integer*) as integer* { $rows }; "
                + "let $f := local:f#1 return if (count($f(parallelize(1 to 100))) eq 100) then $f else $f";
        org.rumbledb.items.FunctionItem f = (org.rumbledb.items.FunctionItem) query(q).getAsList().get(0);
        var name = f.getParameterNames().get(0);
        var mode = org.rumbledb.expressions.ExecutionMode.RDD;
        var plan = f.getParallelVariant(name, mode);
        assertNotNull(plan);
        assertSame(plan, f.getParallelVariant(name, mode));
        assertSame(plan, f.copyForLookup().getParallelVariant(name, mode));
        assertSame(f.getCalleePlan(), f.copyForLookup().getCalleePlan());
    }

    @Test
    void specializedFunctionValuesCanBeSentToSparkWorkers() {
        String q = "let $f := function($rows as integer*) as integer { count($rows) } "
                + "let $n := $f(parallelize(1 to 100)) "
                + "return for $i in parallelize(1 to 3) return $f(($i, $n))";
        assertEquals(List.of(2,2,2), query(q).getAsRDD().collect().stream().map(Item::getIntValue).toList());
    }

    @Test
    void genericParametersCanConsumeTypedDataFramesAsRDDs() {
        String q = "let $f := function($rows as item*) as item* { $rows } "
                + "return $f(parallelize(1 to 100))";
        assertEquals(100, query(q).getAsRDD().count());
    }

    @Test
    void namedCallsWithinASpecializationDispatchWithoutChangingStaticCalls() {
        String q = "declare function local:g($rows as integer*) as integer* { $rows }; "
                + "let $f := function($rows as integer*) as integer* { local:g($rows) } "
                + "return (count(local:g((1,2))), count($f(parallelize(1 to 100))))";
        assertEquals(List.of(2,100), query(q).getAsList().stream().map(Item::getIntValue).toList());
    }

    @Test
    void distributedPartialApplicationHasAnExplicitDiagnostic() {
        String q = "let $f := function($rows as integer*, $n as integer) as integer* { $rows } "
                + "return $f(parallelize(1 to 100), ?)";
        UnsupportedFeatureException e = assertThrows(UnsupportedFeatureException.class, () -> query(q).getAsList());
        assertTrue(e.getMessage().contains("supply the distributed parameter in the final call"));
    }

    @Test
    void twoDistributedParametersAreRejected() {
        String q = "let $f := function($x as integer*, $y as integer*) as integer { count($x)+count($y) } "
                + "return $f(parallelize(1 to 100), parallelize(1 to 100))";
        UnsupportedFeatureException e = assertThrows(UnsupportedFeatureException.class, () -> query(q).getAsList());
        assertTrue(e.getMessage().contains("only one distributed parameter"));
    }
}
