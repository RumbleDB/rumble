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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;

class OneItemVariableReferenceTest {
    @ParameterizedTest
    @ValueSource(strings = {
        "let $values := [1, 2, 3]?* return deep-equal($values, (1, 2, 3))",
        "let $values := map {'a': (1, 2)}?a return deep-equal($values, (1, 2))",
        "let $values := (map {'a': 1}, map {'a': 2})?a return deep-equal($values, (1, 2))",
        "let $values := map {'a': 1, 'b': 2}?* return count($values) eq 2",
        "declare function local:filter($s) { $s[. gt 1] }; "
            + "let $values := [1, 2, 3]?* return deep-equal(local:filter($values), (2, 3))",
        "declare function local:select($e, $names) { $e/@*[name() = $names] }; "
            + "deep-equal((count(local:select(<a x='1' y='2'/>, ('x', 'y'))), "
            + "count(local:select(<a x='1' y='2'/>, 'x'))), (2, 1))",
        "let $node := <a><b>text</b></a>/b/text() "
            + "let $ancestors := $node/ancestor-or-self::* return count($ancestors) eq 2",
        "for $i in (1, 2, 3) group by $key := 1 return deep-equal($i, (1, 2, 3))"
    })
    void preservesSequencesWhenSelectingTheSingletonPlan(String query) {
        assertTrue(new Rumble(RumbleConfiguration.defaultConfiguration())
                .runQuery(query, URI.create("file:///one-item-variable.xq"))
                .getAsList().get(0).getBooleanValue());
    }
}
