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
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.SourcePosition;
import org.rumbledb.exceptions.SourceRange;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.expressions.primary.VariableReferenceExpression;

class FlworBindingMetadataTest {
    private static final String QUERY =
            """
                for $i at $pos in 1 to 10
                let $value := $i + 1
                group by $key := $value mod 2
                return ($key, $i, $pos, $value)
                """;

    @ParameterizedTest
    @ValueSource(strings = {"jq", "xq"})
    void recordsVariableRangesAndTypes(String extension) {
        List<Node> nodes = compile(QUERY, extension);
        ForClause forClause = find(nodes, ForClause.class);
        assertRange(QUERY, "$i", forClause.getVariableMetadata());
        assertRange(QUERY, "$pos", forClause.getPositionalVariableMetadata());
        assertType("xs:integer", forClause, forClause.getVariableName());
        assertType("xs:integer", forClause, forClause.getPositionalVariableName());
        assertEquals(
                forClause.getVariableMetadata(),
                forClause
                        .getNextClause()
                        .getStaticContext()
                        .getInScopeVariables()
                        .get(forClause.getVariableName())
                        .getMetadata());

        LetClause letClause = find(nodes, LetClause.class);
        assertRange(QUERY, "$value", letClause.getVariableMetadata());
        assertType("xs:integer", letClause, letClause.getVariableName());
        GroupByClause groupBy = find(nodes, GroupByClause.class);
        var groupVariable = groupBy.getGroupVariables().get(0);
        assertRange(QUERY, "$key", groupVariable.getVariableMetadata());
        assertType("xs:integer", groupBy, groupVariable.getVariableName());
    }

    @Test
    void infersWindowVariableTypesFromTheInput() {
        String query =
                """
                    for tumbling window $w in (1, 2, 3)
                    start $s previous $p when $s gt 0
                    return ($w, $s, $p)
                    """;
        List<Node> nodes = compile(query, "jq");
        WindowClause window = find(nodes, WindowClause.class);
        var start = window.getStartCondition();
        // The condition sees the refined type of its own variables.
        assertEquals(
                "xs:integer",
                start.expression()
                        .getStaticContext()
                        .getVariableSequenceType(start.variables().currentItem())
                        .toString());
        assertType("xs:integer+", window, window.getWindowVariable());
        assertType("xs:integer", window, start.variables().currentItem());
        assertType("xs:integer?", window, start.variables().previousItem());
        // References see the refined types too, not placeholders from before type inference.
        for (VariableReferenceExpression reference : findAll(nodes, VariableReferenceExpression.class)) {
            assertEquals(
                    reference.getStaticContext().getVariableSequenceType(reference.getVariableName()),
                    reference.getStaticSequenceType(),
                    reference.getVariableName().toString());
        }
    }

    /** A binding's type is the one visible to the clauses after it. */
    private static void assertType(String expected, Clause clause, Name variable) {
        assertEquals(
                expected,
                clause.getNextClause()
                        .getStaticContext()
                        .getVariableSequenceType(variable)
                        .toString());
    }

    private static List<Node> compile(String query, String extension) {
        var module = CompilationPipeline.compileMainModule(
                query,
                URI.create("file:///binding-metadata." + extension),
                new CompilationConfiguration(RumbleConfiguration.defaultConfiguration()),
                ExternalBindings.empty());
        return new AbstractNodeVisitor<List<Node>>() {
            @Override
            protected List<Node> defaultAction(Node node, List<Node> nodes) {
                nodes.add(node);
                return visitDescendants(node, nodes);
            }
        }.visit(module, new ArrayList<>());
    }

    private static <T extends Clause> T find(List<Node> nodes, Class<T> type) {
        return findAll(nodes, type).get(0);
    }

    private static <T extends Node> List<T> findAll(List<Node> nodes, Class<T> type) {
        return nodes.stream().filter(type::isInstance).map(type::cast).toList();
    }

    private static void assertRange(String query, String name, ExceptionMetadata metadata) {
        assertNotNull(metadata);
        int offset = query.indexOf(name);
        String before = query.substring(0, offset);
        int line = (int) before.chars().filter(c -> c == '\n').count() + 1;
        int column = before.length() - before.lastIndexOf('\n') - 1;
        assertEquals(
                new SourceRange(new SourcePosition(line, column), new SourcePosition(line, column + name.length())),
                metadata.getRange());
    }
}
