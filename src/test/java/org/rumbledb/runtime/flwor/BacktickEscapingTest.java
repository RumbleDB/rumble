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
package org.rumbledb.runtime.flwor;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.rumbledb.api.Rumble;
import org.rumbledb.cli.arguments.RuntimeArguments;
import picocli.CommandLine;
import org.rumbledb.config.RumbleConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BacktickEscapingTest {
    @TempDir
    Path directory;

    @Test
    void configurationDefaultsToDisabledAndSupportsOverrides() {
        assertFalse(RumbleConfiguration.defaultConfiguration().runtime().escapeBackticks());
        assertTrue(RumbleConfiguration.builder().with("runtime.escapeBackticks", true).build()
                .runtime().escapeBackticks());
    }

    @Test
    void cliEnablesAndDisablesEscaping() {
        RuntimeArguments enabled = new RuntimeArguments();
        new CommandLine(enabled).parseArgs("--escape-backticks");
        assertTrue(enabled.toConfig().escapeBackticks());
        RuntimeArguments disabled = new RuntimeArguments();
        new CommandLine(disabled).parseArgs("--no-escape-backticks");
        assertFalse(disabled.toConfig().escapeBackticks());
    }

    @Test
    void quotesSingleIdentifiersWithoutChangingDotsOrMarkerText() {
        assertEquals("`a``.b`", FlworDataFrameUtils.quoteIdentifier("a`.b", true));
        assertEquals("`a`.b`", FlworDataFrameUtils.quoteIdentifier("a`.b", false));
        assertEquals("`" + FlworDataFrameUtils.backtickEscape + "`",
                FlworDataFrameUtils.quoteIdentifier(FlworDataFrameUtils.backtickEscape, true));
    }

    @Test
    void disabledEscapingLeavesOrdinaryIdentifiersWorking() throws Exception {
        Path input = directory.resolve("ordinary.json");
        Files.writeString(input, "{\"value\":7}\n");
        for (boolean enabled : new boolean[] {false, true}) {
            Rumble rumble = new Rumble(RumbleConfiguration.builder()
                    .configureRuntime(runtime -> runtime.escapeBackticks(enabled)).build());
            assertEquals(7, rumble.runQuery("for $x in structured-json-lines(\"" + input.toUri()
                    + "\") where $x.value eq 7 return $x.value").getAsList().get(0).getIntValue());
        }
    }

    @Test
    void disabledEscapingDoesNotRewriteIdentifiers() throws Exception {
        Path input = directory.resolve("disabled.json");
        Files.writeString(input, "{\"a`b\":1}\n");
        Rumble rumble = new Rumble(RumbleConfiguration.defaultConfiguration());
        assertThrows(org.apache.spark.sql.catalyst.parser.ParseException.class,
                () -> rumble.runQuery("for $x in structured-json-lines(\"" + input.toUri()
                        + "\") return $x.\"a`b\"").getAsList());
    }

    @Test
    void nativeLookupPreservesBackticksAndNestedNames() throws Exception {
        Path input = directory.resolve("backticks.json");
        Files.writeString(input, "{\"a`b\":1,\"nested`\":{\"c`.d\":2}}\n"
                + "{\"a`b\":3,\"nested`\":{\"c`.d\":4}}\n");
        Rumble rumble = new Rumble(RumbleConfiguration.builder()
                .configureRuntime(runtime -> runtime.escapeBackticks(true)).build());
        String source = "structured-json-lines(\"" + input.toUri() + "\")";
        assertEquals(4, rumble.runQuery("for $x in " + source
                + " where $x.\"a`b\" eq 3 return $x.\"nested`\".\"c`.d\"")
                .getAsList().get(0).getIntValue());
        assertEquals(2, rumble.runQuery("for $x in " + source
                + " group by $key := $x.\"a`b\" return $key").getAsList().size());
        assertEquals(2, rumble.runQuery("for $x in " + source + " for $y in " + source
                + " where $x.\"a`b\" eq $y.\"a`b\" return $x.\"a`b\"").getAsList().size());
        assertEquals(2, rumble.runQuery(source + ".\"nested`\"").getAsList().size());
        assertTrue(rumble.runQuery("for $x in " + source + " return $x")
                .getAsList().get(0).getStringKeys().contains("a`b"));
    }
}
