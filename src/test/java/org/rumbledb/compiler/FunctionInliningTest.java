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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.FunctionCallExpression;

class FunctionInliningTest {
    private static final URI QUERY_URI = URI.create("file:///inlining.xq");
    private static final String FUNCTION = "declare function local:f($a as xs:integer) { $a }; ";

    @Test
    void inlinedCallsConvertArgumentsLikeTheCallTheyReplace() {
        String query = FUNCTION + "local:f(<a>1</a>) instance of xs:integer";
        assertFalse(containsCall(query), "A function with a typed parameter should be inlined");
        // The element is atomized and its untyped value cast to the parameter type.
        assertTrue(run(query).getBooleanValue());
    }

    @Test
    void inlinedCallsRejectArgumentsOfTheWrongType() {
        RumbleException exception = assertThrows(RumbleException.class, () -> run(FUNCTION + "local:f(\"x\")"));
        assertEquals(ErrorCode.UnexpectedTypeErrorCode, exception.getErrorCode());
    }

    private static Item run(String query) {
        return new Rumble(RumbleConfiguration.defaultConfiguration())
                .runQuery(query, QUERY_URI)
                .getAsList()
                .get(0);
    }

    private static boolean containsCall(String query) {
        Node module = CompilationPipeline.compileMainModule(
                query,
                QUERY_URI,
                new CompilationConfiguration(RumbleConfiguration.defaultConfiguration()),
                ExternalBindings.empty());
        return new AbstractNodeVisitor<Boolean>() {
            @Override
            protected Boolean defaultAction(Node node, Boolean found) {
                return found || node instanceof FunctionCallExpression || visitDescendants(node, false);
            }
        }.visit(module, false);
    }
}
