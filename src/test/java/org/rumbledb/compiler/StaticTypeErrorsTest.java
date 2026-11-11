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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.IsStaticallyUnexpectedTypeException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.StaticTypeErrorsException;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;

/**
 * Static type errors are reported together. An expression with an error is typed as unknown, so that errors that only
 * follow from it are not reported.
 */
class StaticTypeErrorsTest {
    private static final RumbleConfiguration CONFIGURATION = RumbleConfiguration.builder()
            .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
            .build();

    @Test
    void reportsAnErrorPerDeclarationAndInTheMainBody() {
        List<RumbleException> errors = errors("declare function local:f() { 1 + \"a\" };\n"
                + "declare function local:g() { 2 + \"b\" };\n"
                + "3 + \"c\"");
        Assertions.assertEquals(List.of(1, 2, 3), lines(errors));
    }

    @Test
    void reportsIndependentErrorsOfTheSameExpression() {
        List<RumbleException> errors = errors("(1 + \"a\",\n 2 + \"b\")");
        Assertions.assertEquals(List.of(1, 2), lines(errors));
    }

    @Test
    void doesNotReportAnErrorOfAnOperandWithAnError() {
        List<RumbleException> errors = errors("(1 + \"a\") * 2");
        Assertions.assertEquals(List.of(1), lines(errors));
    }

    @Test
    void doesNotReportUsesOfAVariableBoundToAnError() {
        List<RumbleException> errors = errors("declare variable $x := 1 + \"a\";\n"
                + "declare function local:f() { $x + 1 };\n"
                + "declare function local:g() { 2 + \"b\" };\n"
                + "let $y := 3 + \"c\"\n"
                + "return $y - 1");
        Assertions.assertEquals(List.of(1, 3, 4), lines(errors));
    }

    @Test
    void doesNotReportUsesOfAForVariableOverAnError() {
        // The variable is one item of the sequence, which keeps it unknown.
        List<RumbleException> errors = errors("for $i in (1 + \"a\")\nreturn $i * 2");
        Assertions.assertEquals(List.of(1), lines(errors));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "for $x in (1 + \"a\") group by $k := $x return $k",
                "[1 + \"a\"]?1 + 1",
                "declare function local:f() as xs:integer { 1 + \"a\" }; local:f() + 1",
                "for $x in () return $x + 1"
            })
    void reportsAnErrorOnceThroughVariablesArraysAndInlinedFunctions(String query) {
        Assertions.assertEquals(1, errors(query).size());
    }

    @Test
    void explainsWhyAnExpressionIsAlwaysEmpty() {
        Assertions.assertEquals(
                "This expression always returns the empty sequence, because its right operand does.",
                errors("1 + ()").get(0).getJSONiqErrorMessage());
    }

    @ParameterizedTest
    @MethodSource("sharedErrors")
    void namesTheOperandAndItsTypeInSharedErrors(String query, String code, String message) {
        RumbleException error = errors(query).get(0);
        Assertions.assertEquals(code, error.getErrorCode().toString());
        Assertions.assertEquals(message, error.getJSONiqErrorMessage());
    }

    private static Stream<Arguments> sharedErrors() {
        return Stream.of(
                Arguments.of(
                        "declare function local:f($x as integer) { $x }; local:f(\"a\")",
                        "XPTY0004",
                        "local:f expects xs:integer for this argument, but it is xs:string."),
                Arguments.of(
                        "declare function local:f($x as string) { $x }; local:f({\"a\": 1})",
                        "FOTY0013",
                        "local:f expects xs:string for this argument, but it is { a: xs:integer }."),
                Arguments.of(
                        "declare function local:f() as integer { \"a\" }; local:f()",
                        "XPTY0004",
                        "local:f declares the return type xs:integer, but returns xs:string."),
                Arguments.of(
                        "let $x as integer := \"a\" return $x",
                        "XPTY0004",
                        "$x is declared as xs:integer, but its value is xs:string."),
                Arguments.of(
                        "(1, 2) + 1",
                        "XPTY0004",
                        "'+' expects at most one value, but its left operand is xs:integer+."),
                Arguments.of(
                        "1 eq {\"a\": 1}",
                        "FOTY0013",
                        "'eq' expects atomizable values, but its right operand is { a: xs:integer }."),
                Arguments.of(
                        "if (function() { 1 }) then 1 else 2",
                        "XPTY0004",
                        "The condition of this if is function() as item*, which has no effective boolean value."),
                Arguments.of(
                        "declare function local:f($x) { if ($x) then 1 else 2 }; local:f(1)",
                        "XPTY0004",
                        "The condition of this if is item*, which may have no effective boolean value."));
    }

    @Test
    void describesAnInferredUnionByItsMembers() {
        Assertions.assertEquals(
                "'+' is not defined for (xs:integer | xs:string) and xs:boolean.",
                errors("declare function local:f($x as xs:boolean) { (if ($x) then 1 else \"a\") + $x }; 1")
                        .get(0)
                        .getJSONiqErrorMessage());
    }

    @Test
    void reportsUsesOfAVariableWithADeclaredType() {
        // The declared type of $x is known even though its value has an error.
        List<RumbleException> errors = errors("declare variable $x as integer := 1 + \"a\";\n$x + \"b\"");
        Assertions.assertEquals(List.of(1, 2), lines(errors));
    }

    @Test
    void reportsAnErrorPerStatementOfTheMainProgram() {
        // The statement on line 4 uses $a, whose value has an error.
        List<RumbleException> errors = errors("variable $a := 1 + \"a\";\n"
                + "variable $b as integer := 2;\n"
                + "variable $d as integer := \"d\";\n"
                + "variable $c := $a + 1;\n"
                + "$b + \"c\"");
        Assertions.assertEquals(List.of(1, 3, 5), lines(errors));
    }

    @Test
    void tellsWhetherAnErrorIsCertain() {
        // An item* value is an integer when it is one integer, but a string never is.
        List<RumbleException> errors = errors("declare function local:f($x) as integer { $x };\n"
                + "declare function local:g() as integer { \"a\" };\n"
                + "1");
        Assertions.assertEquals(List.of(false, true), certainties(errors));
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                // Possible: some values pass at runtime.
                "declare function local:f($x) { $x * 2 }; 1 | false",
                "declare function local:f($x as item()) { $x + 1 }; 1 | false",
                "declare function local:f($x) { if ($x) then 1 else 2 }; 1 | false",
                "declare function local:f($x as item()) { $x eq \"s\" }; 1 | false",
                "declare function local:f($x) { $x cast as integer }; 1 | false",
                // Certain: no value passes.
                "1 + \"a\" | true",
                "(1, 2) eq 1 | true",
                "if (xs:date(\"2020-01-01\")) then 1 else 2 | true",
                "{\"a\": 1} + 1 | true"
            })
    void classifiesOperandChecks(String query, boolean certain) {
        List<RumbleException> errors = errors(query);
        Assertions.assertEquals(List.of(certain), certainties(errors), query);
    }

    @Test
    void reportsTheErrorsAfterAPossibleOneInTheSameUnit() {
        List<RumbleException> errors = errors("declare function local:f($x as integer) { $x };\n"
                + "declare function local:g($y) {\n  (local:f($y),\n  1 + \"a\")\n};\n"
                + "1");
        Assertions.assertEquals(List.of(3, 4), lines(errors));
        Assertions.assertEquals(List.of(false, true), certainties(errors));
    }

    @Test
    void reportsAReturnTypeMismatchAtTheResult() {
        List<RumbleException> errors = errors("declare function local:f($x as xs:double?) as xs:double {\n"
                + "  let $y := $x\n"
                + "  return math:sin($y)\n"
                + "};\n"
                + "1");
        Assertions.assertEquals(List.of(3), lines(errors));
        Assertions.assertEquals(9, errors.get(0).getMetadata().getStart().column());
    }

    @Test
    void reportsAReturnTypeMismatchAtAFlworThatChangesTheNumberOfResults() {
        // The for clause makes the result a sequence, so the whole FLWOR is reported.
        List<RumbleException> errors = errors("declare function local:f() as xs:integer {\n"
                + "  for $i in (1, 2)\n"
                + "  return $i\n"
                + "};\n"
                + "1");
        Assertions.assertEquals(List.of(2), lines(errors));
    }

    @Test
    void throwsASingleErrorAsItIs() {
        RumbleException error = Assertions.assertThrows(RumbleException.class, () -> compile("1 + \"a\""));
        Assertions.assertEquals(UnexpectedStaticTypeException.class, error.getClass());
    }

    @Test
    void throwsSeveralErrorsWithTheFirstOnesCodeAndLocation() {
        RumbleException error =
                Assertions.assertThrows(StaticTypeErrorsException.class, () -> compile("(1 + \"a\",\n 2 + \"b\")"));
        Assertions.assertEquals("XPTY0004", error.getErrorCode().toString());
        Assertions.assertEquals(1, error.getMetadata().getStart().line());
    }

    @Test
    void preservesIndependentAssertionsAndContinuesWithLaterSiblings() {
        List<RumbleException> errors = errors("(1 + \"a\",\n 1 is statically string,\n 2 + \"b\")");
        Assertions.assertEquals(List.of(1, 2, 3), lines(errors));
        Assertions.assertInstanceOf(IsStaticallyUnexpectedTypeException.class, errors.get(1));
    }

    @Test
    void reportsUndeclaredFunctionsAndUnknownValidationTypesWithTheOtherErrors() {
        List<RumbleException> errors = errors("xquery version \"3.1\";\n"
                + "(local:nope(1 + \"a\"), concat#0, validate type xs:nope { <a/> }, 2 + \"b\")");
        Assertions.assertEquals(
                List.of("XPTY0004", "XPST0017", "XPST0017", "XQST0104", "XPTY0004"),
                errors.stream().map(error -> error.getErrorCode().toString()).toList());
    }

    @Test
    void suppressesAnAssertionWhoseOperandAlreadyFailed() {
        Assertions.assertEquals(1, errors("(1 + \"a\") is statically string").size());
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                "(if ($x) then 1 else \"a\") + 1 | false",
                "-(if ($x) then 1 else \"a\") | false",
                "(if ($x) then \"a\" else true()) + 1 | true",
                "(if ($x) then 1 else \"a\") eq 1 | false",
                "sum(if ($x) then 1 else \"a\") | false",
                "min(if ($x) then 1 else \"a\") | false",
                "sum(if ($x) then \"a\" else \"b\") | true",
                "(if ($x) then 1 else xs:date(\"2000-01-01\")) cast as integer | false",
                "(if ($x) then xs:date(\"2000-01-01\") else xs:time(\"12:00:00\")) cast as integer | true"
            })
    void anErrorIsPossibleWhenSomeAlternativePasses(String body, boolean certain) {
        Assertions.assertEquals(
                List.of(certain), certainties(errors("declare function local:f($x as boolean) { " + body + " }; 1")));
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                "map(xs:string, xs:integer) | map(xs:integer, xs:string) | false",
                "array(xs:integer) | array(xs:string) | false",
                "element(*, xs:untyped) | element(a) | false",
                "element(a) | element(b) | true",
                "element(a) | attribute(a) | true",
                "document-node(element(a)) | document-node(element(b)) | true"
            })
    void aMismatchIsPossibleWhenTheTypesMayShareAnItem(String type, String returnType, boolean certain) {
        Assertions.assertEquals(
                List.of(certain),
                certainties(errors("declare function local:f($x as " + type + ") as " + returnType + " { $x }; 1")));
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                "map{1: 2} | true",
                "if ($x) then map{1: 2} else function($a, $b) { $a } | false",
                "if ($x) then map{1: 2} else function($a) { $a } | true"
            })
    void aMapOrArrayCalledWithTwoArgumentsFailsUnlessItMayBeSuchAFunction(String callee, boolean certain) {
        Assertions.assertEquals(
                List.of(certain),
                certainties(errors("xquery version \"3.1\";\n"
                        + "declare function local:f($x as xs:boolean) { ("
                        + callee
                        + ")(1, 2) }; 1")));
    }

    @Test
    void anEmptyOptionalOperandCanPassAComparison() {
        Assertions.assertEquals(
                List.of(false), certainties(errors("declare function local:f($x as xs:string?) { $x eq 1 }; 1")));
    }

    @Test
    void dynamicAndNamedCallsUseTheSameFunctionConversionForCertainty() {
        List<RumbleException> errors = errors("declare function local:g($y as xs:integer) { $y };\n"
                + "declare function local:f($x as xs:untypedAtomic?) {\n"
                + "  (local:g($x),\n (function($y as xs:integer) { $y })($x))\n}; 1");
        Assertions.assertEquals(List.of(false, false), certainties(errors));
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {"xs:integer+ | $x + \"a\"", "xs:integer+ | $x eq \"a\"", "xs:date+ | $x cast as xs:integer"})
    void aLaterCertainFailureReplacesAnEarlierPossibleFailure(String type, String body) {
        List<RumbleException> errors = errors("declare function local:f($x as " + type + ") { " + body + " }; 1");
        Assertions.assertEquals(List.of(true), certainties(errors));
    }

    @Test
    void aStrengthenedFailureSuppressesCascadingErrorsAndKeepsDiagnosticOrder() {
        List<RumbleException> errors =
                errors("declare function local:f($x as xs:integer+) {\n" + "  (($x + \"a\") * 2,\n 3 + \"b\")\n}; 1");
        Assertions.assertEquals(List.of(2, 3), lines(errors));
        Assertions.assertEquals(List.of(true, true), certainties(errors));
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                "nme | Did you mean \"name\"? It has the keys name, url.",
                "NAME | Did you mean \"name\"? It has the keys name, url.",
                "license | It has the keys name, url."
            })
    void listsTheKeysOfAnObjectThatLacksTheKeyOfALookup(String key, String keys) {
        Assertions.assertEquals(
                "This expression always returns the empty sequence, because its input never has the key \""
                        + key
                        + "\". "
                        + keys,
                errors("declare type local:t as { \"name\": \"string\", \"url\": \"string\" };\n"
                                + "declare function local:f($x as local:t) { $x."
                                + key
                                + " };\n1")
                        .get(0)
                        .getJSONiqErrorMessage());
    }

    @Test
    void summarizesTheKeysOfAnObjectWithManyKeys() {
        String fields = IntStream.rangeClosed(1, 12)
                .mapToObj(index -> "\"k" + index + "\": \"integer\"")
                .collect(Collectors.joining(", "));
        Assertions.assertEquals(
                "This expression always returns the empty sequence, because its input never has the key \"other\". "
                        + "It has the keys k1, k2, k3, k4, k5, k6, k7, k8, k9, k10, and 2 more.",
                errors("declare type local:t as { "
                                + fields
                                + " };\n"
                                + "declare function local:f($x as local:t) { $x.other };\n1")
                        .get(0)
                        .getJSONiqErrorMessage());
    }

    @Test
    void hidesWhatIsNestedInTheFieldsOfALongType() {
        String user = "{\"avatar_url\": \"a\", \"events_url\": \"b\", \"followers_url\": \"c\"}";
        Assertions.assertEquals(
                "local:f expects xs:string for this argument, but it is "
                        + "{ id: xs:integer, actor: { … }, reviewers: [{ … }], repo: { name: xs:string } }.",
                errors("declare function local:f($x as string) { $x }; local:f({\"id\": 1, \"actor\": "
                                + user
                                + ", \"reviewers\": ["
                                + user
                                + "], \"repo\": {\"name\": \"r\"}})")
                        .get(0)
                        .getJSONiqErrorMessage());
    }

    @Test
    void listsOnlyTheFirstFieldsOfATypeWithVeryManyFields() {
        String fields = IntStream.rangeClosed(1, 100)
                .mapToObj(index -> "\"field_" + index + "\": " + index)
                .collect(Collectors.joining(", "));
        String message = errors("declare function local:f($x as string) { $x }; local:f({" + fields + "})")
                .get(0)
                .getJSONiqErrorMessage();
        Assertions.assertTrue(
                message.startsWith(
                        "local:f expects xs:string for this argument, but it is { field_1: xs:integer, field_2: xs:integer, "),
                message);
        Assertions.assertTrue(message.endsWith(", field_20: xs:integer, … 80 more fields }."), message);
    }

    private static List<RumbleException> errors(String query) {
        RumbleException error = Assertions.assertThrows(RumbleException.class, () -> compile(query));
        return error instanceof StaticTypeErrorsException errors ? errors.getErrors() : List.of(error);
    }

    private static List<Integer> lines(List<RumbleException> errors) {
        return errors.stream()
                .map(error -> error.getMetadata().getStart().line())
                .toList();
    }

    private static List<Boolean> certainties(List<RumbleException> errors) {
        return errors.stream()
                .map(error -> ((UnexpectedStaticTypeException) error).isCertain())
                .toList();
    }

    private static void compile(String query) {
        CompilationPipeline.compileMainModule(
                query,
                URI.create("file:///static-type-errors.jq"),
                new CompilationConfiguration(CONFIGURATION),
                ExternalBindings.empty());
    }
}
