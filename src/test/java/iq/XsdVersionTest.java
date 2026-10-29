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
package iq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.config.model.SemanticsConfig;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.strings.RegexPatternUtils;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class XsdVersionTest {
    @Test
    public void validatesVersionAndPreservesDefault() {
        Assertions.assertEquals("1.1", SemanticsConfig.builder().build().xsdVersion());
        Assertions.assertThrows(
                RuntimeException.class,
                () -> SemanticsConfig.builder().xsdVersion("2.0").build());
        var config = org.rumbledb.config.RumbleConfiguration.builder()
                .with("semantics.xsdVersion", "1.0")
                .build();
        Assertions.assertEquals("1.0", config.semantics().xsdVersion());
    }

    @Test
    public void cliSelectsXsdVersionIndependentlyOfXmlVersion() {
        var arguments = new org.rumbledb.cli.arguments.SemanticsArguments();
        new picocli.CommandLine(arguments).parseArgs("--xsd-version", "1.0", "--xml-version", "1.1");
        Assertions.assertEquals("1.0", arguments.toConfig().xsdVersion());
        Assertions.assertEquals("1.1", arguments.toConfig().xmlVersion());
    }

    @Test
    public void castsRespectVersionWithoutGlobalState() {
        String[] inputs = {"+INF", "%gg", "0000", "0000-05"};
        ItemType[] types = {
            BuiltinTypesCatalogue.doubleItem,
            BuiltinTypesCatalogue.anyURIItem,
            BuiltinTypesCatalogue.gYearItem,
            BuiltinTypesCatalogue.gYearMonthItem
        };
        for (int i = 0; i < inputs.length; i++) {
            for (String version : new String[] {"1.0", "1.1", "1.0"}) {
                var config = org.rumbledb.config.RumbleConfiguration.builder()
                        .configureSemantics(s -> s.xsdVersion(version))
                        .build();
                var result = CastIterator.castItemToType(
                        ItemFactory.getInstance().createStringItem(inputs[i]),
                        types[i],
                        ExceptionMetadata.EMPTY_METADATA,
                        new StaticContext(java.net.URI.create("file:/tmp/"), config));
                Assertions.assertEquals("1.0".equals(version), result == null, inputs[i]);
            }
        }
    }

    @Test
    public void regexHyphensRespectVersionAndQuoteFlag() {
        var metadata = ExceptionMetadata.EMPTY_METADATA;
        for (String pattern : new String[] {"[0-9-.]*/", "[^a-d-b-c]", "[a-c-1-4x-z-7-9]*", "[a-a-x-x]+"}) {
            Assertions.assertThrows(
                    org.rumbledb.exceptions.InvalidRegexPatternException.class,
                    () -> RegexPatternUtils.compileRegex(pattern, "", metadata, "1.0"));
            Assertions.assertDoesNotThrow(() -> RegexPatternUtils.compileRegex(pattern, "", metadata, "1.1"));
            Assertions.assertDoesNotThrow(() -> RegexPatternUtils.compileRegex(pattern, "q", metadata, "1.0"));
        }
        for (String pattern : new String[] {"[-a-z]", "[a-z-]", "[a-z-[b-d]]", "[a-z\\-x]"}) {
            Assertions.assertDoesNotThrow(() -> RegexPatternUtils.compileRegex(pattern, "", metadata, "1.0"));
        }
    }
}
