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

/** Static type errors are reported together, keeping the first error of each top-level unit. */
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
    void keepsOnlyTheFirstErrorOfAUnit() {
        List<RumbleException> errors = errors("declare function local:f() {\n  (1 + \"a\", 2 + \"b\")\n};\n3");
        Assertions.assertEquals(List.of(2), lines(errors));
    }

    @Test
    void doesNotReportUsesOfAVariableWhoseDeclarationFailed() {
        // $x is typed with a fallback after its error, which would make $x + 1 an error too.
        List<RumbleException> errors = errors("declare variable $x := 1 + \"a\";\n"
                + "declare function local:f() { $x + 1 };\n"
                + "declare function local:g() { 2 + \"b\" };\n"
                + "3");
        Assertions.assertEquals(List.of(1, 3), lines(errors));
    }

    @Test
    void throwsASingleErrorAsItIs() {
        RumbleException error = Assertions.assertThrows(RumbleException.class, () -> compile("1 + \"a\""));
        Assertions.assertEquals(UnexpectedStaticTypeException.class, error.getClass());
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

    private static void compile(String query) {
        CompilationPipeline.compileMainModule(
                query,
                URI.create("file:///static-type-errors.jq"),
                new CompilationConfiguration(CONFIGURATION),
                ExternalBindings.empty());
    }
}
