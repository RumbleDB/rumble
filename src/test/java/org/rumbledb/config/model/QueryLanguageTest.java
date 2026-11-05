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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.RumbleConfigurationResolver;
import org.rumbledb.config.model.QueryLanguage.Standard;
import org.rumbledb.config.model.QueryLanguage.Version;
import org.rumbledb.exceptions.CliException;

class QueryLanguageTest {

    @Test
    void versionsAreDeclaredInAscendingOrder() {
        List<Version> versions = List.of(Version.values());
        for (int i = 1; i < versions.size(); i++) {
            assertTrue(versions.get(i - 1).compareTo(versions.get(i)) < 0);
        }
        assertTrue(QueryLanguage.XQUERY_31.isAtLeast(Version.V3_1));
        assertTrue(QueryLanguage.JSONIQ_31.isAtLeast(Version.V1_0));
        assertFalse(QueryLanguage.XQUERY_10.isAtLeast(Version.V3_1));
    }

    @Test
    void everyLanguageRoundTripsThroughItsId() {
        for (QueryLanguage language : QueryLanguage.values()) {
            assertEquals(language, QueryLanguage.fromId(language.id()));
        }
        assertEquals("xquery31", QueryLanguage.XQUERY_31.id());
        assertEquals("jsoniq10", QueryLanguage.JSONIQ_10.toString());
    }

    @Test
    void xquery30IsAnAliasOfXquery31() {
        assertEquals(QueryLanguage.XQUERY_31, QueryLanguage.fromId("xquery30"));
        assertEquals(Optional.of(QueryLanguage.XQUERY_31), QueryLanguage.fromDeclaration(Standard.XQUERY, "3.0"));
        assertEquals(Optional.empty(), QueryLanguage.fromDeclaration(Standard.JSONIQ, "3.0"));
    }

    @Test
    void eachStandardHasADefaultLanguageOfThatStandard() {
        assertEquals(QueryLanguage.DEFAULT_JSONIQ, QueryLanguage.defaultFor(Standard.JSONIQ));
        assertEquals(QueryLanguage.DEFAULT_XQUERY, QueryLanguage.defaultFor(Standard.XQUERY));
        for (Standard standard : Standard.values()) {
            assertEquals(standard, QueryLanguage.defaultFor(standard).standard());
        }
    }

    @Test
    void unknownLanguagesAreRejected() {
        CliException exception = assertThrows(CliException.class, () -> QueryLanguage.fromId("xqeury31"));
        assertTrue(exception.getMessage().contains("xqeury31"));
        assertEquals(Optional.empty(), QueryLanguage.fromDeclaration(Standard.XQUERY, "2.0"));
    }

    @Test
    void configurationAcceptsAndExposesExternalNames() {
        RumbleConfiguration fromPath = RumbleConfiguration.builder()
                .with("semantics.queryLanguage", "xquery31")
                .build();
        assertEquals(QueryLanguage.XQUERY_31, fromPath.semantics().queryLanguage());
        assertEquals("xquery31", RumbleConfigurationResolver.get(fromPath, "semantics.queryLanguage"));

        assertEquals(
                QueryLanguage.XQUERY_31,
                SemanticsConfig.builder().queryLanguage("xquery31").build().queryLanguage());
        assertEquals(
                QueryLanguage.XQUERY_31,
                SemanticsConfig.builder()
                        .queryLanguage(QueryLanguage.XQUERY_31)
                        .build()
                        .queryLanguage());
        assertEquals(
                SemanticsConfig.DEFAULT_QUERY_LANGUAGE,
                SemanticsConfig.builder().build().queryLanguage());
    }

    @Test
    void configurationRejectsUnknownLanguages() {
        assertThrows(IllegalArgumentException.class, () -> RumbleConfiguration.builder()
                .with("semantics.queryLanguage", "bogus")
                .build());
        assertThrows(CliException.class, () -> SemanticsConfig.builder().queryLanguage("bogus"));
    }
}
