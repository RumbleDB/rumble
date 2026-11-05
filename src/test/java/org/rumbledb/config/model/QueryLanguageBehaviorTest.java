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
package org.rumbledb.config.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.RumbleException;

class QueryLanguageBehaviorTest {

    private static Rumble rumble(String language) {
        return new Rumble(RumbleConfiguration.builder()
                .with("semantics.queryLanguage", language)
                .build());
    }

    private static void assertErrorCode(String expected, String language, String query) {
        RumbleException exception = assertThrows(
                RumbleException.class, () -> rumble(language).runQuery(query).serialize());
        assertEquals(expected, exception.getErrorCode().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"xquery version \"4.0\"; 1", "jsoniq version \"4.0\"; 1"})
    void unsupportedVersion40DeclarationsAreRejected(String query) {
        RumbleException exception = assertThrows(
                RumbleException.class, () -> rumble("jsoniq10").runQuery(query).serialize());
        assertEquals("XQST0031", exception.getErrorCode().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.0", "3.0", "3.1"})
    void unprefixedFunctionDeclarationsFollowXQueryRulesInEveryVersion(String version) {
        assertErrorCode("XQST0045", "jsoniq10", "xquery version \"" + version + "\"; declare function f() {1}; 1");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.0", "3.0", "3.1"})
    void contextItemDeclarationsFollowXQueryRulesInEveryVersion(String version) {
        assertErrorCode(
                "XPST0003", "jsoniq10", "xquery version \"" + version + "\"; declare context item as item()* := 1; 1");
    }

    @ParameterizedTest
    @ValueSource(strings = {"3.0", "3.1"})
    void stringJoinAcceptsAtomicItemsFromXQuery31(String version) {
        String query = "xquery version \"" + version + "\"; string-join((1,2),\",\")";
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>1,2",
                rumble("jsoniq10").runQuery(query).serialize());
    }

    @Test
    void stringJoinRequiresStringsInXQuery10AndJsoniq() {
        assertErrorCode("XPTY0004", "jsoniq10", "xquery version \"1.0\"; string-join((1,2),\",\")");
        assertErrorCode("XPTY0004", "jsoniq31", "string-join((1,2),\",\")");
    }

    @ParameterizedTest
    @ValueSource(strings = {"xquery40", "jsoniq40"})
    void unsupportedVersion40ConfigurationIsRejected(String language) {
        assertThrows(IllegalArgumentException.class, () -> rumble(language));
    }
}
