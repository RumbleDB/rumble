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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.errorcodes.ErrorCode;

class GeneralComparisonTest {
    @ParameterizedTest
    @ValueSource(strings = {
        "(xs:untypedAtomic('a'), xs:untypedAtomic('b')) = 'b'",
        "tokenize('a b', ' ') = xs:untypedAtomic('b')",
        "xs:untypedAtomic('b') = tokenize('a b', ' ')",
        "(<a>a</a>, <a>b</a>) = (<a>b</a>, <a>c</a>)",
        "(<a>1</a>, <a>2</a>) = 2",
        "(true(), <a>0</a>, 1) = xs:untypedAtomic('1')",
        "let $a := <a><b>1</b><b>2</b></a> return $a/b = $a/b[2]",
        "not(() = xs:untypedAtomic('1'))",
        "not(<a/>/b = '1')"
    })
    void comparesEachAtomicPair(String query) {
        assertEquals(true, new Rumble(RumbleConfiguration.defaultConfiguration())
            .runQuery(query, URI.create("file:///comparison.xq")).getAsList().get(0).getBooleanValue());
    }

    @Test
    void valueComparisonStillRejectsSequences() {
        RumbleException exception = assertThrows(RumbleException.class, () ->
            new Rumble(RumbleConfiguration.defaultConfiguration())
                .runQuery("(1, 2) eq 2", URI.create("file:///comparison.xq")).getAsList());
        assertEquals(ErrorCode.UnexpectedTypeErrorCode, exception.getErrorCode());
    }
}
