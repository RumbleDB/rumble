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
package org.rumbledb.cli;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.zip.GZIPInputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Black-box tests: the child JVM never receives target/classes or the test classpath. */
public class CLIJarIT {
    private static String classpath;
    private static String mainClass;

    @TempDir
    Path directory;

    @BeforeAll
    static void locateAssembly() throws Exception {
        Path jar = Path.of(System.getProperty("cli.jar")).toAbsolutePath();
        assertTrue(Files.isRegularFile(jar), "Missing assembly JAR: " + jar);
        try (JarFile archive = new JarFile(jar.toFile())) {
            mainClass = archive.getManifest().getMainAttributes().getValue("Main-Class");
            assertEquals("org.rumbledb.cli.Main", mainClass);
        }
        classpath = jar
                + File.pathSeparator
                + Files.readString(Path.of(System.getProperty("cli.providedClasspath")))
                        .trim();
    }

    // Each group exercises the packaged CLI in a separate JVM, including its exit code.
    // Parser tests cover exact key mappings and both boolean forms for run and repl.
    @Nested
    class CommandsAndQuerySources {
        @Test
        void helpForLauncherRunAndRepl() throws Exception {
            for (String[] arguments :
                    List.of(new String[] {"--help"}, new String[] {"-h"}, new String[] {"run", "--help"}, new String[] {
                        "repl", "-h"
                    })) {
                Result result = run("", arguments);
                assertEquals(0, result.code(), result.toString());
                assertTrue(result.out().contains("Usage: rumbledb"), result.toString());
                assertEquals("", result.err(), result.toString());
            }
        }

        @Test
        void inlineAndFileQueriesIncludingPathsWithSpaces() throws Exception {
            assertSuccess(run("", "run", "--query", "1 + 1"), "2");
            Path query = directory.resolve("query with spaces.jq");
            Files.writeString(query, "for $i in 1 to 3 return $i * $i");
            assertSuccess(run("", "run", query.toString()), "1\n4\n9");
            assertSuccess(run("", "run", "--query-path", query.toString()), "1\n4\n9");
        }

        @Test
        void replEvaluatesQueriesAndFiltersTheirOutput() throws Exception {
            Result result = run(
                    "41 + 1\n\nexit\n",
                    "repl",
                    "--shell-filter",
                    "tr 42 99",
                    "--output-format",
                    "serialize",
                    "--output-format-option",
                    "method=adaptive");
            assertEquals(0, result.code(), result.toString());
            assertTrue(
                    (result.out() + "\n" + result.err())
                            .replaceAll("\\x1B\\[[0-9;]*m", "")
                            .lines()
                            .anyMatch(line -> line.strip().equals("99")),
                    result.toString());
            assertFalse(result.err().contains("CLI Error:"), result.toString());
            assertFalse(result.err().contains("⚠️"), result.toString());
        }
    }

    @Nested
    class OutputFormatsAndRoundTrips {
        @ParameterizedTest(name = "serialize-each-item with method {0}")
        @ValueSource(strings = {"text", "adaptive", "xml", "xml-json-hybrid"})
        void eachItemSerializationAlwaysUsesNewlinesBetweenItems(String method) throws Exception {
            Path output = directory.resolve("each-item.txt");
            String[] options = {
                "--output-format-option",
                "method=" + method,
                "--output-format-option",
                "item-separator=|",
                "--output-format-option",
                "omit-xml-declaration=yes"
            };
            List<String> arguments = new ArrayList<>(List.of("run", "-q", "1, 2, 3", "-f", "serialize-each-item"));
            arguments.addAll(List.of(options));
            assertSuccess(run("", arguments.toArray(String[]::new)), "1\n2\n3");
            arguments.addAll(List.of("-o", output.toString(), "-P", "1"));
            assertSuccess(run("", arguments.toArray(String[]::new)), "");
            assertEquals("1\n2\n3\n", Files.readString(output));
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "unparsed-text(" + quote(output.toUri().toString()) + ")",
                            "-f",
                            "serialize-each-item",
                            "--output-format-option",
                            "method=text"),
                    "1\n2\n3");
        }

        @Test
        void eachItemJsonCanSerializeMultipleResultsAndReadThemBack() throws Exception {
            Path output = directory.resolve("stores serialized");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            storesQuery(),
                            "-f",
                            "serialize-each-item",
                            "--output-format-option",
                            "method=json",
                            "--output-format-option",
                            "item-separator=|",
                            "-o",
                            output.toString(),
                            "-P",
                            "2"),
                    "");
            assertEquals(2, partFiles(output).size());
            for (Path part : partFiles(output)) {
                for (String line : Files.readAllLines(part)) {
                    assertTrue(JSON.readTree(line).has("storeid"), line);
                }
            }
            assertStoresRoundTrip("json-lines(" + quote(output.toUri().toString()) + ")");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "1, 2, 3",
                            "-f",
                            "serialize-each-item",
                            "--output-format-option",
                            "method=json"),
                    "1\n2\n3");
        }

        @Test
        void serializationWithinEachItemKeepsItsOwnParametersAndDisplayCap() throws Exception {
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "[1, [2, 3]], 4",
                            "-f",
                            "serialize-each-item",
                            "--output-format-option",
                            "method=text",
                            "--output-format-option",
                            "item-separator=|"),
                    "1|2|3\n4");
            assertSuccess(run("", "run", "-q", "1 to 3", "-f", "serialize-each-item", "--result-size", "2"), "1\n2");
            assertSuccess(
                    run("", "run", "-q", "()", "-f", "serialize-each-item", "--output-format-option", "method=json"),
                    "");
        }

        @Test
        void languageDefaultsApplyToStdoutFilesAndDetectedXQuerySources() throws Exception {
            assertSuccess(run("", "run", "-q", "1, 2"), "1\n2");
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a/><b/>";
            assertSuccess(run("", "run", "-q", "<a/>, <b/>", "--default-language", "xquery31"), xml);
            Path output = directory.resolve("default-xquery.xml");
            assertSuccess(
                    run("", "run", "-q", "<a/>, <b/>", "--default-language", "xquery31", "-o", output.toString()), "");
            assertEquals(xml, Files.readString(output));
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "xquery version \"3.1\"; 1, 2",
                            "--output-format-option",
                            "omit-xml-declaration=yes"),
                    "1 2");
            Path query = directory.resolve("detected.xq");
            Files.writeString(query, "1, 2");
            assertSuccess(
                    run("", "run", query.toString(), "--output-format-option", "omit-xml-declaration=yes"), "1 2");
            Path jsoniqOutput = directory.resolve("default-jsoniq.txt");
            assertSuccess(run("", "run", "-q", "1, 2", "-o", jsoniqOutput.toString(), "-P", "1"), "");
            assertEquals("1\n2\n", Files.readString(jsoniqOutput));
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "1, 2",
                            "--default-language",
                            "xquery31",
                            "-f",
                            "serialize-each-item",
                            "--output-format-option",
                            "method=json"),
                    "1\n2");
        }

        @ParameterizedTest(name = "{0}: file format, partitions and all records survive read-back")
        @ValueSource(strings = {"json", "csv", "parquet", "avro"})
        void structuredFormatsCanBeReadBack(String format) throws Exception {
            Path output = directory.resolve("stores output." + format);
            List<String> arguments = new ArrayList<>(List.of(
                    "run",
                    "-q",
                    storesQuery(),
                    "--output-path",
                    output.toString(),
                    "--output-format",
                    format,
                    "--number-of-output-partitions",
                    "2"));
            if (format.equals("csv")) {
                arguments.addAll(List.of("--output-format-option", "header=true", "--output-format-option", "sep=;"));
            }
            assertSuccess(run("", arguments.toArray(String[]::new)), "");
            List<Path> parts = partFiles(output);
            assertEquals(2, parts.size(), "Requested two output partitions: " + parts);
            for (Path part : parts) {
                assertTrue(part.getFileName().toString().contains("." + format), part.toString());
                if (format.equals("parquet")) {
                    byte[] bytes = Files.readAllBytes(part);
                    assertEquals("PAR1", new String(bytes, 0, 4, StandardCharsets.US_ASCII));
                    assertEquals("PAR1", new String(bytes, bytes.length - 4, 4, StandardCharsets.US_ASCII));
                } else if (format.equals("avro")) {
                    byte[] bytes = Files.readAllBytes(part);
                    assertArrayEquals(new byte[] {'O', 'b', 'j', 1}, Arrays.copyOf(bytes, 4));
                } else if (format.equals("csv")) {
                    List<String> lines = Files.readAllLines(part);
                    assertTrue(lines.get(0).contains(";"), "CSV header must use the requested delimiter");
                    assertTrue(lines.get(0).contains("storeid"), lines.toString());
                } else {
                    for (String line : Files.readAllLines(part)) {
                        assertTrue(JSON.readTree(line).isObject(), line);
                    }
                }
            }
            String reader =
                    switch (format) {
                        case "json" -> "json-lines(" + quote(output.toUri() + "part-*.json") + ")";
                        case "csv" -> "csv-file("
                                + quote(output.toUri().toString())
                                + ", {\"header\": true, \"sep\": \";\", \"inferSchema\": true})";
                        default -> format + "-file(" + quote(output.toUri().toString()) + ")";
                    };
            assertStoresRoundTrip(reader);
        }

        @Test
        void csvCompressionOptionProducesReadableGzipFiles() throws Exception {
            Path output = directory.resolve("compressed csv");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            storesQuery(),
                            "-o",
                            output.toString(),
                            "-f",
                            "csv",
                            "-P",
                            "1",
                            "--output-format-option",
                            "header=true",
                            "--output-format-option",
                            "compression=gzip",
                            "--output-format-option",
                            "method=xml"),
                    "");
            List<Path> parts = partFiles(output);
            assertEquals(1, parts.size());
            assertTrue(parts.get(0).toString().endsWith(".csv.gz"), parts.toString());
            try (var input = new GZIPInputStream(Files.newInputStream(parts.get(0)))) {
                String csv = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                assertTrue(csv.contains("storeid"), csv);
                assertEquals(8, csv.lines().count(), csv);
            }
            assertStoresRoundTrip("csv-file(" + quote(output.toUri().toString()) + ", {\"header\": true})");
        }

        @Test
        void jsonScalarResultsCanBeReadBackWithoutWrappingThemInObjects() throws Exception {
            Path output = directory.resolve("scalars.json");
            assertSuccess(run("", "run", "-q", "1 to 3", "-f", "json", "-o", output.toString(), "-P", "1"), "");
            assertTrue(Files.isRegularFile(output));
            assertEquals(List.of("1", "2", "3"), Files.readAllLines(output));
            assertSuccess(
                    run("", "run", "-q", "json-lines(" + quote(output.toUri().toString()) + ")", "-f", "json"),
                    "1\n2\n3");
        }

        @Test
        void localJsonOutputIsIndentedAndReadable() throws Exception {
            Path output = directory.resolve("local object.json");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "{\"answer\": 42}",
                            "-o",
                            output.toString(),
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=json",
                            "-P",
                            "1",
                            "--no-data-frame-execution",
                            "--output-format-option",
                            "indent=yes",
                            "--output-format-option",
                            "indent-spaces=4"),
                    "");
            String content = Files.readString(output);
            assertTrue(content.contains("\n    \"answer\""), content);
            assertEquals(JSON.readTree("{\"answer\":42}"), JSON.readTree(content));
            assertSuccess(
                    run("", "run", "-q", "json-doc(" + quote(output.toUri().toString()) + ").answer"), "42");
        }

        @Test
        void stdoutUsesRequestedFormatAndQuerySerializationDeclarations() throws Exception {
            assertSuccess(run("", "run", "-q", "\"hello\"", "-f", "json"), "\"hello\"");
            assertSuccess(
                    run("", "run", "-q", "\"hello\"", "-f", "serialize", "--output-format-option", "method=text"),
                    "hello");
            // A query's explicit serialization declaration takes precedence over the CLI default.
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "declare namespace output = \"http://www.w3.org/2010/xslt-xquery-serialization\"; "
                                    + "declare option output:method \"text\"; \"hello\"",
                            "-f",
                            "serialize"),
                    "hello");
        }

        @Test
        void outputFormatOptionMethodWorksWithoutOutputFormat() throws Exception {
            assertSuccess(run("", "run", "-q", "\"hello\"", "--output-format-option", "method=text"), "hello");
        }

        @Test
        void serializeNormalizesTheWholeSequenceAndHonorsItsSeparator() throws Exception {
            Path output = directory.resolve("serialized sequence.txt");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "1, [2, 3], 4",
                            "-f",
                            "serialize",
                            "-o",
                            output.toString(),
                            "--output-format-option",
                            "method=text",
                            "--output-format-option",
                            "item-separator=|",
                            "--result-size",
                            "1"),
                    "");
            assertEquals(
                    "1|2|3|4",
                    Files.readString(output),
                    "serialize must not add a trailing newline or truncate at result-size");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "unparsed-text(" + quote(output.toUri().toString()) + ")",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "1|2|3|4");
        }

        @Test
        void serializeWritesTheRequestedEncodingWithoutCorruptingUnicode() throws Exception {
            Path output = directory.resolve("unicode.txt");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "\"Grüezi 東京\"",
                            "-f",
                            "serialize",
                            "-o",
                            output.toString(),
                            "--output-format-option",
                            "method=text",
                            "--output-format-option",
                            "encoding=UTF-16"),
                    "");
            assertEquals("Grüezi 東京", Files.readString(output, StandardCharsets.UTF_16));
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "unparsed-text(" + quote(output.toUri().toString()) + ", \"UTF-16\")",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "Grüezi 東京");
        }

        @Test
        void serializeRejectsPartitioningAndEnforcesItsMaterializationCap() throws Exception {
            Path output = directory.resolve("one-string.txt");
            Result partitions = run("", "run", "-q", "1, 2", "-f", "serialize", "-o", output.toString(), "-P", "2");
            assertEquals(42, partitions.code(), partitions.toString());
            assertTrue(partitions.err().contains("single string"), partitions.toString());
            assertFalse(Files.exists(output));
            Result capped = run(
                    "",
                    "run",
                    "-q",
                    storesQuery(),
                    "-f",
                    "serialize",
                    "-o",
                    output.toString(),
                    "--materialization-cap",
                    "2",
                    "--output-format-option",
                    "method=adaptive");
            assertEquals(42, capped.code(), capped.toString());
            assertTrue(capped.err().contains("Cannot materialize"), capped.toString());
            assertFalse(Files.exists(output));
        }

        @Test
        void serializeJsonRejectsMultipleTopLevelItemsAndDoesNotCreateOutput() throws Exception {
            Path output = directory.resolve("invalid sequence.json");
            Result result = run(
                    "",
                    "run",
                    "-q",
                    "1, 2",
                    "-f",
                    "serialize",
                    "-o",
                    output.toString(),
                    "--output-format-option",
                    "method=json");
            assertEquals(42, result.code(), result.toString());
            assertTrue(result.err().contains("SERE0023"), result.toString());
            assertFalse(Files.exists(output));
        }

        @Test
        void serializeWritesXmlWithExactlyOneDeclaration() throws Exception {
            Path output = directory.resolve("answer.xml");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "<answer>42</answer>",
                            "--default-language",
                            "xquery31",
                            "-f",
                            "serialize",
                            "-o",
                            output.toString(),
                            "--output-format-option",
                            "indent=yes"),
                    "");
            String xml = Files.readString(output);
            assertEquals(1, xml.split("<\\?xml", -1).length - 1, xml);
            var document = javax.xml.parsers.DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(output.toFile());
            assertEquals("answer", document.getDocumentElement().getTagName());
            assertEquals("42", document.getDocumentElement().getTextContent());
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "string(doc(" + quote(output.toUri().toString()) + ")/answer)",
                            "--default-language",
                            "xquery31",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "42");
        }

        @Test
        void unstructuredResultsRejectParquetRatherThanSilentlyWritingText() throws Exception {
            Result result = run(
                    "",
                    "run",
                    "-q",
                    "1, {\"a\":2}",
                    "-o",
                    directory.resolve("not-parquet").toString(),
                    "-f",
                    "parquet");
            assertEquals(42, result.code(), result.toString());
            assertTrue(
                    result.err().contains("DataFrame schema") || result.err().contains("structured collection"),
                    result.toString());
        }

        @Test
        void overwriteProtectsExistingOutputAndThenReplacesIt() throws Exception {
            Path output = directory.resolve("result with spaces.txt");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "1, 4, 9",
                            "-o",
                            output.toString(),
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=adaptive",
                            "-P",
                            "1"),
                    "");
            assertEquals("1\n4\n9", Files.readString(output).strip());
            Result conflict = run("", "run", "-q", "42", "-o", output.toString(), "--no-overwrite");
            assertEquals(42, conflict.code(), conflict.toString());
            assertTrue(conflict.err().contains("already exists"), conflict.toString());
            assertEquals("1\n4\n9", Files.readString(output).strip());
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "42",
                            "-o",
                            output.toString(),
                            "-O",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=adaptive",
                            "-P",
                            "1"),
                    "");
            assertEquals("42", Files.readString(output).strip());
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "unparsed-text(" + quote(output.toUri().toString()) + ")",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "42");
        }
    }

    @Nested
    class Bindings {
        @Test
        void literalAndFileVariablesAreBothAvailable() throws Exception {
            Path payload = fixture("singleLine.json");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "declare variable $answer as integer external; declare variable $payload external; "
                                    + "($answer + 1, $payload instance of object)",
                            "--variable",
                            "answer=41",
                            "--variable-from-file",
                            "payload=" + payload),
                    "42\ntrue");
        }

        @Test
        void literalAndJsonFileContextItems() throws Exception {
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "string($$)",
                            "-I",
                            "hello",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "hello");
            Path input = directory.resolve("context with spaces.json");
            Files.writeString(input, "{\"answer\":42}");
            assertSuccess(
                    run("", "run", "-q", "$$.answer", "-i", input.toString(), "--context-item-input-format", "json"),
                    "42");
        }

        @Test
        void standardInputAndFileContextCanBeParsedAsText() throws Exception {
            assertSuccess(run("{\"answer\":42}", "run", "-q", "$$.answer", "--context-item-input", "-"), "42");
            assertSuccess(
                    run(
                            "hello",
                            "run",
                            "-q",
                            "upper-case(string($$))",
                            "-i",
                            "-",
                            "--context-item-input-format",
                            "text",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "HELLO");
            Path text = directory.resolve("context.txt");
            Files.writeString(text, "hello");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "upper-case(string($$))",
                            "-i",
                            text.toString(),
                            "--context-item-input-format",
                            "text",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "HELLO");
        }
    }

    @Nested
    class RuntimeAndOptimizations {
        @Test
        void resultCapLimitsDisplayButNotSavedOutput() throws Exception {
            Result limited = run("", "run", "-q", "1 to 4", "--result-size", "2", "-c", "100");
            assertSuccess(limited, "1\n2");
            assertTrue(limited.err().contains("capped"), limited.toString());
            Path output = directory.resolve("uncapped.txt");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "1 to 4",
                            "--result-size",
                            "2",
                            "-o",
                            output.toString(),
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=adaptive",
                            "-P",
                            "1"),
                    "");
            assertEquals("1\n2\n3\n4", Files.readString(output).strip());
        }

        @Test
        void materializationCapIsEnforcedForDistributedArrays() throws Exception {
            String query =
                    "[json-lines(" + quote(fixture("stores.jsonl").toUri().toString()) + ")]";
            Result limited = run("", "run", "-q", query, "--materialization-cap", "2");
            assertNotEquals(0, limited.code(), limited.toString());
            assertTrue(limited.err().toLowerCase().contains("materializ"), limited.toString());
            Result complete = run("", "run", "-q", "size(" + query + ")", "-c", "100");
            assertSuccess(complete, "7");
        }

        @ParameterizedTest(name = "{0}: enabled and disabled preserve query results")
        @ValueSource(
                strings = {
                    "native-sql-predicates",
                    "data-frame-execution-mode-detection",
                    "parallel-execution",
                    "data-frame-execution",
                    "native-execution",
                    "function-inlining",
                    "tail-call-optimization",
                    "optimize-general-comparison-to-value-comparison",
                    "optimize-steps",
                    "optimize-steps-experimental",
                    "optimize-parent-pointers"
                })
        void executionAndOptimizationSwitchesPreserveQueryResults(String option) throws Exception {
            String query = "declare function local:identity($x) { $x }; "
                    + "for $s in "
                    + storesQuery()
                    + " where $s.state = \"MA\" "
                    + "order by $s.storeid return local:identity($s.storeid)";
            String expected = "2\n3";
            String language = "jsoniq10";
            if (option.equals("data-frame-execution-mode-detection")) {
                query = "sum(for-each(" + storesQuery() + ", function($s) { integer($s.storeid) }))";
                expected = "28";
            } else if (option.equals("tail-call-optimization")) {
                query = "declare function local:sum($n as integer, $total as integer) as integer { "
                        + "if ($n eq 0) then $total else local:sum($n - 1, $total + $n) }; local:sum(100, 0)";
                expected = "5050";
            } else if (option.equals("optimize-steps")
                    || option.equals("optimize-steps-experimental")
                    || option.equals("optimize-parent-pointers")) {
                query = "let $root := <root><a/><a/></root> return count($root/a/..)";
                language = "xquery31";
                expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>1";
            }
            assertSuccess(run("", "run", "-q", query, "--default-language", language, "--" + option), expected);
            assertSuccess(run("", "run", "-q", query, "--default-language", language, "--no-" + option), expected);
        }

        @Test
        void staticTypingAndBuiltinReturnChecksExecute() throws Exception {
            assertSuccess(run("", "run", "-q", "sum(1 to 3)", "-t", "--check-return-types-of-builtin-functions"), "6");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "sum(1 to 3)",
                            "--no-static-typing",
                            "--no-check-return-types-of-builtin-functions"),
                    "6");
        }

        @Test
        void applyUpdatesCanBeEnabledAndDisabled() throws Exception {
            String query = "let $data := {\"bool\":true, \"int\":10} return delete json $data.bool";
            assertSuccess(run("", "run", "-q", query, "--apply-updates"), "");
            assertSuccess(run("", "run", "-q", query, "--no-apply-updates"), "");
        }
    }

    @Nested
    class LanguageAndFormatting {
        @ParameterizedTest(name = "query language {0}")
        @ValueSource(strings = {"jsoniq10", "jsoniq31", "xquery31"})
        void queryLanguages(String language) throws Exception {
            String expected = language.startsWith("xquery") ? "<?xml version=\"1.0\" encoding=\"UTF-8\"?>2" : "2";
            assertSuccess(run("", "run", "-q", "1 + 1", "--default-language", language), expected);
        }

        @ParameterizedTest(name = "XML version {0}")
        @ValueSource(strings = {"1.0", "1.1"})
        void xmlVersions(String version) throws Exception {
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "count(<answer>{6 * 7}</answer>)",
                            "--default-language",
                            "xquery31",
                            "--xml-version",
                            version),
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>1");
        }

        @Test
        void staticBaseUriControlsRelativeInputPaths() throws Exception {
            Path input = fixture("stores.jsonl");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "count(json-lines(\"stores.jsonl\"))",
                            "--static-base-uri",
                            input.getParent().toUri().toString()),
                    "7");
        }

        @Test
        void defaultFormattingLanguageCalendarAndTimezoneAffectResults() throws Exception {
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "format-date(date(\"2004-04-12\"), \"[MNn]\")",
                            "--default-formatting-language",
                            "fr",
                            "--default-formatting-calendar",
                            "ISO",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "Avril");
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "format-time(time(\"12:00:00Z\"), \"[H00]:[m00]\")",
                            "--default-formatting-place",
                            "America/New_York",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "07:00");
            // An explicit query argument takes precedence over the configured place.
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "format-time(time(\"12:00:00Z\"), \"[H00]:[m00]\", (), (), \"UTC\")",
                            "--default-formatting-place",
                            "America/New_York",
                            "-f",
                            "serialize",
                            "--output-format-option",
                            "method=text"),
                    "12:00");
        }

        @Test
        void timezoneAndNullValidationOptionsExecute() throws Exception {
            for (String prefix : List.of("--", "--no-")) {
                assertSuccess(
                        run("", "run", "-q", "42", prefix + "dates-with-timezone", prefix + "lax-json-null-validation"),
                        "42");
            }
            assertSuccess(run("", "run", "-q", "42", "--lax-json-null-valication"), "42");
        }
    }

    @Nested
    class DiagnosticsAndErrors {
        @Test
        void executionLogIsWrittenToRequestedPath() throws Exception {
            Path log = directory.resolve("execution log.txt");
            assertSuccess(run("", "run", "-q", "42", "--log-path", log.toString()), "42");
            String content = Files.readString(log);
            assertTrue(content.contains("[ExecTime]"), content);
            assertTrue(content.contains("[ProfilerCount]"), content);
        }

        @Test
        void planDiagnosticsAndLoggingDoNotPolluteResults() throws Exception {
            Result result =
                    run("", "run", "-q", "1 + 1", "--print-iterator-tree", "--debug", "--spark-log-level", "off");
            assertSuccess(result, "2");
            assertTrue(result.err().contains("Expression tree"), result.toString());
            assertSuccess(
                    run(
                            "",
                            "run",
                            "-q",
                            "1 + 1",
                            "--no-print-iterator-tree",
                            "--no-debug",
                            "--log-level",
                            "off",
                            "--spark-log-level",
                            "warn"),
                    "2");
        }

        @Test
        void inferredTypesCanBePrinted() throws Exception {
            Result result = run(
                    "", "run", "-q", "annotate({\"answer\":42}, {\"answer\":\"integer\"})", "--print-inferred-types");
            assertEquals(0, result.code(), result.toString());
            assertTrue(result.out().contains("42"), result.toString());
            assertTrue(result.err().contains("Inferred DataFrame type"), result.toString());
        }

        @Test
        void errorDetailsCanBeEnabledAndDisabled() throws Exception {
            Result terse = run("", "run", "-q", "1 +", "--no-show-error-info");
            Result verbose = run("", "run", "-q", "1 +", "-v");
            assertEquals(42, terse.code(), terse.toString());
            assertEquals(42, verbose.code(), verbose.toString());
            assertTrue(terse.err().contains("XPST0003"), terse.toString());
            assertTrue(verbose.err().contains("XPST0003"), verbose.toString());
            assertFalse(terse.err().contains("at org.rumbledb"), terse.toString());
            assertTrue(verbose.err().contains("at org.rumbledb"), verbose.toString());
        }

        @Test
        void invalidArgumentsAndRemovedServerModeAreRejected() throws Exception {
            for (String[] arguments : List.of(
                    new String[] {"run"},
                    new String[] {"run", "--not-an-option"},
                    new String[] {"run", "-q", "1", "query.jq"},
                    new String[] {"serve"},
                    new String[] {"run", "-q", "1", "--log-level", "invalid"},
                    new String[] {"run", "-q", "1", "--spark-log-level", "invalid"},
                    new String[] {"run", "-q", "1", "--static-typing", "yes"})) {
                Result result = run("", arguments);
                assertEquals(42, result.code(), result.toString());
                assertEquals("", result.out(), result.toString());
                assertTrue(result.err().contains("CLI Error:"), result.toString());
            }
        }
    }

    private static final ObjectMapper JSON = new ObjectMapper();

    private Path fixture(String name) throws Exception {
        Path destination = directory.resolve(name);
        if (!Files.exists(destination)) {
            try (var input = CLIJarIT.class.getResourceAsStream("/queries/" + name)) {
                assertNotNull(input, "Missing existing test fixture: " + name);
                Files.copy(input, destination);
            }
        }
        return destination;
    }

    private String storesQuery() throws Exception {
        return "annotate(json-lines("
                + quote(fixture("stores.jsonl").toUri().toString())
                + "), {\"storeid\":\"integer\", \"state\":\"string\"})";
    }

    private static String quote(String value) throws Exception {
        return JSON.writeValueAsString(value);
    }

    private static List<Path> partFiles(Path output) throws Exception {
        assertTrue(Files.isDirectory(output), "Expected partition directory: " + output);
        try (var files = Files.list(output)) {
            return files.filter(path -> path.getFileName().toString().startsWith("part-"))
                    .sorted()
                    .toList();
        }
    }

    private void assertStoresRoundTrip(String reader) throws Exception {
        String query = "for $s in "
                + reader
                + " order by integer($s.storeid) "
                + "return {\"storeid\":integer($s.storeid), \"state\":$s.state}";
        Result readBack = run("", "run", "-q", query, "-f", "json");
        assertEquals(0, readBack.code(), readBack.toString());
        List<JsonNode> expected = new ArrayList<>();
        for (String line : Files.readAllLines(fixture("stores.jsonl"))) {
            expected.add(JSON.readTree(line));
        }
        List<JsonNode> actual = new ArrayList<>();
        for (String line : readBack.out().lines().toList()) {
            actual.add(JSON.readTree(line));
        }
        assertEquals(expected, actual, "All records and values must survive the format round-trip: " + readBack);
    }

    private static void assertSuccess(Result result, String output) {
        assertEquals(0, result.code(), result.toString());
        assertEquals(output, result.out(), result.toString());
    }

    private Result run(String input, String... arguments) throws Exception {
        List<String> command = new ArrayList<>(List.of(
                Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                "-Dfile.encoding=UTF-8",
                "-Dspark.app.name=RumbleDB-CLI-tests",
                "-Dspark.master=local[2]",
                "-Dspark.ui.enabled=false",
                // Match the Java 17 module access used by the existing Spark test runner.
                "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
                "--add-opens=java.base/java.net=ALL-UNNAMED",
                "--add-opens=java.base/java.nio=ALL-UNNAMED",
                "--add-opens=java.base/java.util=ALL-UNNAMED",
                "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
                "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED",
                "-cp",
                classpath,
                mainClass));
        command.addAll(List.of(arguments));
        Path stdout = Files.createTempFile(this.directory, "stdout-", ".txt");
        Path stderr = Files.createTempFile(this.directory, "stderr-", ".txt");
        ProcessBuilder builder = new ProcessBuilder(command)
                .directory(this.directory.toFile())
                .redirectOutput(stdout.toFile())
                .redirectError(stderr.toFile());
        builder.environment().put("SPARK_LOCAL_IP", "127.0.0.1");
        Process process = builder.start();
        try {
            try (var stdin = process.getOutputStream()) {
                stdin.write(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            assertTrue(process.waitFor(60, TimeUnit.SECONDS), () -> "CLI timed out: " + command);
            return new Result(
                    process.exitValue(),
                    Files.readString(stdout).replace("\r\n", "\n").strip(),
                    Files.readString(stderr).replace("\r\n", "\n").strip());
        } finally {
            if (process.isAlive()) {
                try {
                    process.descendants().forEach(ProcessHandle::destroyForcibly);
                } finally {
                    process.destroyForcibly();
                    process.waitFor(10, TimeUnit.SECONDS);
                }
            }
        }
    }

    private record Result(int code, String out, String err) {}
}
