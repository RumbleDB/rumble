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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.SourcePosition;
import org.rumbledb.exceptions.SourceRange;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.LetClause;

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
        assertEquals("xs:integer", forClause.getVariableSequenceType().toString());
        assertEquals("xs:integer", forClause.getPositionalVariableSequenceType().toString());
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
        assertEquals("xs:integer", letClause.getVariableSequenceType().toString());
        var groupVariable = find(nodes, GroupByClause.class).getGroupVariables().get(0);
        assertRange(QUERY, "$key", groupVariable.getVariableMetadata());
        assertEquals("xs:integer", groupVariable.getVariableSequenceType().toString());
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
        return nodes.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow();
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
