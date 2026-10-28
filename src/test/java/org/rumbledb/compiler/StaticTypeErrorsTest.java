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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
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

    @Test
    void reportsTheErrorsAfterAPossibleOneInTheSameUnit() {
        List<RumbleException> errors = errors("declare function local:f($x as integer) { $x };\n"
                + "declare function local:g($y) {\n  (local:f($y),\n  1 + \"a\")\n};\n"
                + "1");
        Assertions.assertEquals(List.of(3, 4), lines(errors));
        Assertions.assertEquals(List.of(false, true), certainties(errors));
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
