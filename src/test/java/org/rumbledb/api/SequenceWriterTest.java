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
package org.rumbledb.api;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SequenceWriterTest {
    @TempDir
    Path directory;

    @Test
    void defaultWritersFollowTheQueryLanguageAndExplicitFormatsStillOverrideThem() throws Exception {
        Rumble jsoniq = new Rumble(new RumbleConfiguration());
        Path each = directory.resolve("jsoniq.txt");
        jsoniq.runQuery("1, 2, 3")
                .write()
                .option("method", "text")
                .option("item-separator", "|")
                .save(each.toString());
        // The unspecified partition count produces partition files without collecting the full sequence.
        try (var parts = Files.list(each)) {
            assertEquals(
                    List.of("1", "2", "3"),
                    parts.filter(p -> p.getFileName().toString().startsWith("part-"))
                            .flatMap(p -> {
                                try {
                                    return Files.readAllLines(p).stream();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .sorted()
                            .toList());
        }
        Rumble xquery = new Rumble(RumbleConfiguration.builder()
                .with("semantics.queryLanguage", "xquery31")
                .build());
        Path whole = directory.resolve("xquery.xml");
        xquery.runQuery("<a/>, <b/>")
                .write()
                .option("omit-xml-declaration", "yes")
                .save(whole.toString());
        assertEquals("<a/><b/>", Files.readString(whole));
        assertEquals(
                "serialize-each-item",
                jsoniq.runQuery("1")
                        .getRuntimeStaticContext()
                        .getConfiguration()
                        .output()
                        .effectiveOutputFormat("jsoniq10"));
        assertEquals(
                "serialize",
                xquery.runQuery("1")
                        .getRuntimeStaticContext()
                        .getConfiguration()
                        .output()
                        .effectiveOutputFormat("xquery31"));
    }

    @Test
    void eachItemFilesHonorEncodingInEveryPartition() throws Exception {
        Rumble rumble = new Rumble(RumbleConfiguration.builder()
                .with("output.outputFormat", "serialize-each-item")
                .with("output.numberOfOutputPartitions", 2)
                .build());
        Path output = directory.resolve("encoded-items");
        rumble.runQuery("\"Grüezi\", \"東京\"")
                .write()
                .option("method", "text")
                .option("encoding", "UTF-16")
                .save(output.toString());
        try (var files = Files.list(output)) {
            List<Path> parts = files.filter(p -> p.getFileName().toString().startsWith("part-"))
                    .toList();
            assertEquals(2, parts.size());
            java.util.ArrayList<String> values = new java.util.ArrayList<>();
            for (Path part : parts) {
                values.addAll(Files.readAllLines(part, StandardCharsets.UTF_16));
            }
            assertEquals(List.of("Grüezi", "東京"), values.stream().sorted().toList());
        }
    }

    @Test
    void detectedLanguageChangesDefaultsButPreservesExplicitSerializationOptions() throws Exception {
        Rumble rumble = new Rumble(new RumbleConfiguration());
        SequenceOfItems xquery = rumble.runQuery("xquery version \"3.1\"; 1, 2");
        assertEquals(
                "xml",
                xquery.getRuntimeStaticContext().getSerializationParameters().getMethod());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>1 2", xquery.serialize());
        Rumble configured = new Rumble(RumbleConfiguration.builder()
                .with("output.serializationParameters.method", "text")
                .with("output.serializationParameters.itemSeparator", "|")
                .build());
        assertEquals("1|2", configured.runQuery("xquery version \"3.1\"; 1, 2").serialize());
    }

    @ParameterizedTest
    @CsvSource({"jsoniq10", "jsoniq31", "jsoniq40", "xquery10", "xquery30", "xquery31"})
    void textSerializationUsesW3CSpacingInBothLanguagesUnlessASeparatorIsExplicit(String language) {
        Rumble rumble = new Rumble(RumbleConfiguration.builder()
                .with("semantics.queryLanguage", language)
                .build());
        // QT3 ser/method-text.xml:Serialization-text-19: no item-separator declaration.
        String prolog = "declare namespace output = \"http://www.w3.org/2010/xslt-xquery-serialization\"; "
                + "declare option output:method \"text\"; ";
        assertEquals("1 2 3 4 5", rumble.runQuery(prolog + "[1, 2, 3, 4, 5]").serialize());
        assertEquals(
                "1 2 3 4 5", rumble.runQuery(prolog + "(1, [2, [3, 4]], 5)").serialize());
        assertEquals(
                "1|2|3",
                rumble.runQuery(prolog + "declare option output:item-separator \"|\"; [1, 2, 3]")
                        .serialize());
        assertEquals(
                "123",
                rumble.runQuery(prolog + "declare option output:item-separator \"\"; [1, 2, 3]")
                        .serialize());
    }

    @Test
    void jsoniqWholeSequenceSerializationHasNoDefaultItemSeparator() {
        Rumble rumble = new Rumble(new RumbleConfiguration());
        assertEquals("1 2 3", rumble.runQuery("1, 2, 3").serialize());
    }

    @Test
    void changingFileFormatDoesNotChangeTheSerializationMethodOrTheOriginalWriter() {
        SequenceOfItems sequence = new Rumble(new RumbleConfiguration()).runQuery("1");
        SequenceWriter original = sequence.write();
        SequenceWriter csv = original.format("csv");
        assertEquals(
                original.getSerializer().serialize(sequence.getAsList().get(0)),
                csv.getSerializer().serialize(sequence.getAsList().get(0)));
        assertEquals(
                "xml-json-hybrid",
                sequence.getRuntimeStaticContext().getSerializationParameters().getMethod());
    }

    @Test
    void serializationOptionsProduceOneStringAndLeaveTheSequenceConfigurationUnchanged() throws Exception {
        SequenceOfItems sequence = new Rumble(new RumbleConfiguration()).runQuery("1, [2, 3], 4");
        Path output = directory.resolve("sequence.txt");
        sequence.write()
                .format("serialize")
                .options(Map.of("method", "text", "item-separator", "|"))
                .save(output.toString());
        assertEquals("1|2|3|4", Files.readString(output));
        assertEquals(
                "xml-json-hybrid",
                sequence.getRuntimeStaticContext().getSerializationParameters().getMethod());
    }

    @Test
    void apiSerializationAndFnSerializeUseTheSameSequenceNormalization() {
        Rumble rumble = new Rumble(RumbleConfiguration.builder()
                .with("output.serializationParameters.method", "text")
                .with("output.serializationParameters.itemSeparator", "|")
                .build());
        assertEquals(
                rumble.runQuery("serialize((1, [2, 3], 4), {\"method\":\"text\", \"item-separator\":\"|\"})")
                        .getAsList()
                        .get(0)
                        .getStringValue(),
                rumble.runQuery("1, [2, 3], 4").serialize());
    }

    @Test
    void changingStructuredFormatPreservesWriterPartitioningOptions() throws Exception {
        Rumble rumble = new Rumble(new RumbleConfiguration());
        Path output = directory.resolve("partitioned");
        rumble.runQuery(
                        "annotate(({\"id\":1, \"state\":\"CA\"}, {\"id\":2, \"state\":\"MA\"}), {\"id\":\"integer\", \"state\":\"string\"})")
                .write()
                .format("json")
                .partitionBy("state")
                .format("parquet")
                .save(output.toString());
        assertTrue(Files.isDirectory(output.resolve("state=CA")));
        assertTrue(Files.isDirectory(output.resolve("state=MA")));
    }
}
