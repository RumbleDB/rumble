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
import org.rumbledb.config.model.LanguageVersion.Language;
import org.rumbledb.config.model.LanguageVersion.Version;
import org.rumbledb.exceptions.CliException;

class LanguageVersionTest {

    @Test
    void versionsAreDeclaredInAscendingOrder() {
        List<Version> versions = List.of(Version.values());
        for (int i = 1; i < versions.size(); i++) {
            assertTrue(versions.get(i - 1).compareTo(versions.get(i)) < 0);
        }
        assertTrue(LanguageVersion.XQUERY_31.isAtLeast(Version.V3_1));
        assertTrue(LanguageVersion.JSONIQ_31.isAtLeast(Version.V1_0));
        assertFalse(LanguageVersion.XQUERY_10.isAtLeast(Version.V3_1));
    }

    @Test
    void everyLanguageRoundTripsThroughItsId() {
        for (LanguageVersion language : LanguageVersion.values()) {
            assertEquals(language, LanguageVersion.fromId(language.id()));
        }
        assertEquals("xquery31", LanguageVersion.XQUERY_31.id());
        assertEquals("jsoniq10", LanguageVersion.JSONIQ_10.toString());
    }

    @Test
    void xquery30IsAnAliasOfXquery31() {
        assertEquals(LanguageVersion.XQUERY_31, LanguageVersion.fromId("xquery30"));
        assertEquals(Optional.of(LanguageVersion.XQUERY_31), LanguageVersion.fromDeclaration(Language.XQUERY, "3.0"));
        assertEquals(Optional.empty(), LanguageVersion.fromDeclaration(Language.JSONIQ, "3.0"));
    }

    @Test
    void eachLanguageHasADefaultVersionOfThatLanguage() {
        assertEquals(LanguageVersion.DEFAULT_JSONIQ, LanguageVersion.defaultFor(Language.JSONIQ));
        assertEquals(LanguageVersion.DEFAULT_XQUERY, LanguageVersion.defaultFor(Language.XQUERY));
        for (Language language : Language.values()) {
            assertEquals(language, LanguageVersion.defaultFor(language).language());
        }
    }

    @Test
    void unknownLanguagesAreRejected() {
        CliException exception = assertThrows(CliException.class, () -> LanguageVersion.fromId("xqeury31"));
        assertTrue(exception.getMessage().contains("xqeury31"));
        assertEquals(Optional.empty(), LanguageVersion.fromDeclaration(Language.XQUERY, "2.0"));
    }

    @Test
    void configurationAcceptsAndExposesExternalNames() {
        RumbleConfiguration fromPath = RumbleConfiguration.builder()
                .with("semantics.queryLanguage", "xquery31")
                .build();
        assertEquals(LanguageVersion.XQUERY_31, fromPath.semantics().queryLanguage());
        assertEquals("xquery31", RumbleConfigurationResolver.get(fromPath, "semantics.queryLanguage"));

        assertEquals(
                LanguageVersion.XQUERY_31,
                SemanticsConfig.builder().queryLanguage("xquery31").build().queryLanguage());
        assertEquals(
                LanguageVersion.XQUERY_31,
                SemanticsConfig.builder()
                        .queryLanguage(LanguageVersion.XQUERY_31)
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
