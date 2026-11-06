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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.model.LanguageVersion.Language;

class ModuleLanguageDetectionTest {

    private static final URI NO_EXTENSION = URI.create("file:///query");

    private static Language detect(String query, URI uri, String defaultLanguage) {
        return ModuleParser.detectLanguage(
                query,
                uri,
                RumbleConfiguration.builder()
                        .with("semantics.queryLanguage", defaultLanguage)
                        .build());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "xquery version \"3.1\"; 1",
                "xquery encoding \"UTF-8\"; 1",
                "xquery version \"3.1\" encoding \"UTF-8\"; 1",
                "  \n\t xquery version \"3.1\"; 1",
                "(: a comment :) xquery version \"3.1\"; 1",
                "(: outer (: nested :) comment :)\n(: another :) xquery version \"3.1\"; 1",
                "xquery version \"3&#46;1\"; 1",
                "xquery encoding \"UTF&#45;8\"; 1"
            })
    void xqueryDeclarationsSelectXQuery(String query) {
        assertEquals(Language.XQUERY, detect(query, NO_EXTENSION, "jsoniq10"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "jsoniq version \"3.1\"; 1",
                "jsoniq encoding \"UTF-8\"; 1",
                "  \n\t jsoniq version \"3.1\"; 1",
                "(: outer (: nested :) comment :) jsoniq version \"1.0\"; 1"
            })
    void jsoniqDeclarationsSelectJsoniq(String query) {
        assertEquals(Language.JSONIQ, detect(query, NO_EXTENSION, "xquery31"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "\"xquery version\"",
                "\"xquery encoding\"",
                "(: xquery version \"3.1\"; :) 1",
                "1 (: xquery version :)",
                "xquery",
                "xquery, version",
                "1, \"xquery version \\\"3.1\\\";\"",
                "{ \"a\" : \"xquery version\" }"
            })
    void textThatOnlyMentionsAnXQueryDeclarationKeepsTheDefault(String query) {
        assertEquals(Language.JSONIQ, detect(query, NO_EXTENSION, "jsoniq10"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"jsoniq version\"", "(: jsoniq version \"3.1\"; :) 1", "jsoniq", "1, \"jsoniq version\""})
    void textThatOnlyMentionsAJsoniqDeclarationKeepsTheDefault(String query) {
        assertEquals(Language.XQUERY, detect(query, NO_EXTENSION, "xquery31"));
    }

    @Test
    void declarationsTakePrecedenceOverFileExtensions() {
        assertEquals(Language.XQUERY, detect("xquery version \"3.1\"; 1", URI.create("file:///query.jq"), "jsoniq10"));
        assertEquals(Language.JSONIQ, detect("jsoniq version \"3.1\"; 1", URI.create("file:///query.xq"), "xquery31"));
    }

    @Test
    void fileExtensionsTakePrecedenceOverTheDefault() {
        assertEquals(Language.XQUERY, detect("1", URI.create("file:///query.xq"), "jsoniq10"));
        assertEquals(Language.JSONIQ, detect("1", URI.create("file:///query.jq"), "xquery31"));
        assertEquals(Language.JSONIQ, detect("\"xquery version\"", URI.create("file:///query.jq"), "xquery31"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "(: only a comment :)", "(: unterminated", "`", "}"})
    void queriesThatDoNotLexCleanlyAreLeftForTheParserToReport(String query) {
        assertEquals(Language.JSONIQ, detect(query, NO_EXTENSION, "jsoniq10"));
    }
}
