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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.serialization.SerializationParameterUtils;

class LanguageVersionBehaviorTest {

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

    private static URI write(Path directory, String fileName, String query) throws IOException {
        return Files.writeString(directory.resolve(fileName), query).toUri();
    }

    @Test
    void jsoniqFilesUseJsoniqBehaviourUnderAnXQueryDefault(@TempDir Path directory) throws IOException {
        URI stringJoin = write(directory, "join.jq", "string-join((1,2),\",\")");
        RumbleException exception = assertThrows(
                RumbleException.class,
                () -> rumble("xquery31").runQuery(stringJoin).serialize());
        assertEquals("XPTY0004", exception.getErrorCode().toString());

        URI plain = write(directory, "plain.jq", "1");
        assertEquals("1", rumble("xquery31").runQuery(plain).serialize());
    }

    @Test
    void jsoniqFilesUseTheDefaultJsoniqVersionUnlessTheyDeclareOne(@TempDir Path directory) throws IOException {
        URI undeclared = write(directory, "undeclared.jq", "{a : 1}");
        assertEquals("{ \"a\" : 1 }", rumble("xquery31").runQuery(undeclared).serialize());

        URI declared = write(directory, "declared.jq", "jsoniq version \"3.1\"; {a : 1}");
        assertThrows(
                RumbleException.class,
                () -> rumble("xquery31").runQuery(declared).serialize());
    }

    @Test
    void xqueryFilesUseXQueryBehaviourUnderAJsoniqDefault(@TempDir Path directory) throws IOException {
        URI stringJoin = write(directory, "join.xq", "string-join((1,2),\",\")");
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>1,2",
                rumble("jsoniq10").runQuery(stringJoin).serialize());
    }

    @Test
    void outputOptionsDoNotKeepTheConfiguredLanguageMethodForAJsoniqFile(@TempDir Path directory) throws IOException {
        URI plain = write(directory, "plain.jq", "1");
        RumbleConfiguration configuration = RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage(LanguageVersion.XQUERY_31))
                .configureOutput(output -> output.serializationParameters(SerializationParameterUtils.buildFromConfig(
                        Map.of("indent", "yes"), LanguageVersion.XQUERY_31)))
                .build();
        assertEquals("1", new Rumble(configuration).runQuery(plain).serialize());
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
    void stringJoinAcceptsAtomicItemsInJsoniq31() {
        assertEquals(
                "1,2", rumble("jsoniq31").runQuery("string-join((1,2),\",\")").serialize());
        assertEquals(
                "1,2",
                rumble("jsoniq10")
                        .runQuery("jsoniq version \"3.1\"; string-join((1,2),\",\")")
                        .serialize());
    }

    @Test
    void stringJoinRequiresStringsInVersion10OfBothLanguages() {
        assertErrorCode("XPTY0004", "jsoniq10", "xquery version \"1.0\"; string-join((1,2),\",\")");
        assertErrorCode("XPTY0004", "jsoniq10", "string-join((1,2),\",\")");
    }

    @Test
    void encodingDeclarationsWorkTheSameInBothLanguages() {
        assertEquals(
                "1", rumble("jsoniq10").runQuery("jsoniq encoding \"UTF-8\"; 1").serialize());
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>1",
                rumble("jsoniq10").runQuery("xquery encoding \"UTF-8\"; 1").serialize());
        String invalid = ErrorCode.InvalidEncodingNameErrorCode.toString();
        assertErrorCode(invalid, "jsoniq10", "jsoniq encoding \"!bad\"; 1");
        assertErrorCode(invalid, "jsoniq10", "xquery encoding \"!bad\"; 1");
    }

    @ParameterizedTest
    @ValueSource(strings = {"jsoniq10", "xquery31"})
    void languageKeywordsRemainUsableAsNamesInBothLanguages(String language) {
        assertEquals(
                "3",
                rumble(language)
                        .runQuery("declare function local:xquery($jsoniq) { $jsoniq + 1 };"
                                + " let $xquery := 2 return local:xquery($xquery)")
                        .serialize()
                        .replaceFirst("^<\\?xml.*\\?>", ""));
    }

    @ParameterizedTest
    @ValueSource(strings = {"jsoniq version \"3.1\"", "xquery version \"3.1\"", "declare variable $x := 2"})
    void declarationsAfterTheFirstMainModuleAreRejected(String declaration) {
        assertErrorCode("XPST0003", "xquery31", "1; " + declaration + "; 2");
    }

    @Test
    void statementsSeparatedBySemicolonsStillRunInBothLanguages() {
        assertEquals("2", rumble("jsoniq10").runQuery("1; 2").serialize());
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>2",
                rumble("xquery31").runQuery("1; 2").serialize());
    }

    @ParameterizedTest
    @ValueSource(strings = {"xquery40", "jsoniq40"})
    void unsupportedVersion40ConfigurationIsRejected(String language) {
        assertThrows(IllegalArgumentException.class, () -> rumble(language));
    }
}
