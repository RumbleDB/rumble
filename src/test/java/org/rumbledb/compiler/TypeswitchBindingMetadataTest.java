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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;

/** The passes that rewrite a module after type inference keep the context in which each case binds its variable. */
class TypeswitchBindingMetadataTest {

    @Test
    void keepsTheContextOfEachExpressionCase() {
        List<Node> nodes = compile(
                """
                    typeswitch(1)
                    case $v as xs:string return string($v)
                    case $v as xs:integer return string($v)
                    default $v return string($v)
                    """);
        TypeSwitchExpression expression = find(nodes, TypeSwitchExpression.class);
        List<String> types = new ArrayList<>();
        for (TypeswitchCase switchCase : expression.getCases()) {
            types.add(bindingType(switchCase.getStaticContext(), switchCase.getVariableName()));
        }
        TypeswitchCase defaultCase = expression.getDefaultCase();
        types.add(bindingType(defaultCase.getStaticContext(), defaultCase.getVariableName()));
        assertEquals(List.of("xs:string", "xs:integer", "xs:integer"), types);
    }

    @Test
    void keepsTheContextOfEachStatementCase() {
        List<Node> nodes = compile(
                """
                    variable $value as xs:integer := 1;
                    typeswitch($value)
                    case $branch as xs:string return $value := string-length($branch);
                    default $branch return $value := $branch;
                    $value
                    """);
        TypeSwitchStatement statement = find(nodes, TypeSwitchStatement.class);
        List<String> types = new ArrayList<>();
        for (TypeSwitchStatementCase switchCase : statement.getCases()) {
            types.add(bindingType(switchCase.getStaticContext(), switchCase.getVariableName()));
        }
        TypeSwitchStatementCase defaultCase = statement.getDefaultCase();
        types.add(bindingType(defaultCase.getStaticContext(), defaultCase.getVariableName()));
        assertEquals(List.of("xs:string", "xs:integer"), types);
    }

    private static String bindingType(StaticContext context, Name variable) {
        return context == null
                ? "no context"
                : context.getVariableSequenceType(variable).toString();
    }

    private static List<Node> compile(String query) {
        var module = CompilationPipeline.compileMainModule(
                query,
                URI.create("file:///typeswitch-binding-metadata.jq"),
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

    private static <T extends Node> T find(List<Node> nodes, Class<T> type) {
        return nodes.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow();
    }
}
