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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void help() throws Exception {
        for (String[] arguments :
                List.of(new String[] {"--help"}, new String[] {"run", "--help"}, new String[] {"repl", "--help"})) {
            Result result = run("", arguments);
            assertEquals(0, result.code(), result.toString());
            assertTrue(result.out().contains("Usage: rumbledb"), result.toString());
            assertEquals("", result.err(), result.toString());
        }
    }

    @Test
    void inlineQuery() throws Exception {
        assertSuccess(run("", "run", "--query", "1 + 1"), "2");
    }

    @Test
    void queryFilesAndOutput() throws Exception {
        Path query = this.directory.resolve("query with spaces.jq");
        Files.writeString(query, "for $i in 1 to 3 return $i * $i");
        assertSuccess(run("", "run", query.toString()), "1\n4\n9");
        Path output = this.directory.resolve("result with spaces.json");
        Result result = run(
                "",
                "run",
                "--query-path",
                query.toString(),
                "--output-path",
                output.toString(),
                "--output-format",
                "serialize",
                "-P",
                "1");
        assertSuccess(result, "");
        assertEquals("1\n4\n9", Files.readString(output).replace("\r\n", "\n").strip());
        Result conflict = run("", "run", "-q", "42", "-o", output.toString());
        assertEquals(42, conflict.code(), conflict.toString());
        assertTrue(conflict.err().contains("already exists"), conflict.toString());
        assertSuccess(
                run(
                        "",
                        "run",
                        "-q",
                        "42",
                        "-o",
                        output.toString(),
                        "--overwrite",
                        "--output-format",
                        "serialize",
                        "-P",
                        "1"),
                "");
        assertEquals("42", Files.readString(output).strip());
    }

    @Test
    void externalVariable() throws Exception {
        assertSuccess(
                run(
                        "",
                        "run",
                        "-q",
                        "declare variable $answer as integer external; $answer + 1",
                        "--variable",
                        "answer=41"),
                "42");
    }

    @Test
    void standardInputContextItem() throws Exception {
        assertSuccess(run("{\"answer\":42}", "run", "-q", "$$.answer", "--context-item-input", "-"), "42");
    }

    @Test
    void xquery() throws Exception {
        assertSuccess(
                run("", "run", "-q", "count(<answer>{6 * 7}</answer>)", "--default-language", "xquery31"),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>1");
    }

    @Test
    void serializeXmlWithoutDataFrameConversion() throws Exception {
        assertSuccess(
                run(
                        "",
                        "run",
                        "-q",
                        "<answer>{6 * 7}</answer>",
                        "--default-language",
                        "xquery31",
                        "--output-format",
                        "serialize",
                        "--output-format-option",
                        "method=xml",
                        "--output-format-option",
                        "omit-xml-declaration=yes"),
                "<answer>42</answer>");
        // An unrelated option must not change XQuery's XML method default.
        assertSuccess(
                run(
                        "",
                        "run",
                        "-q",
                        "<answer>42</answer>",
                        "--default-language",
                        "xquery31",
                        "--output-format-option",
                        "omit-xml-declaration=yes"),
                "<answer>42</answer>");
    }

    @Test
    void serializeSequenceToStdoutAndFile() throws Exception {
        String query = "(1, 2, 3)";
        assertSuccess(
                run(
                        "",
                        "run",
                        "-q",
                        query,
                        "-f",
                        "serialize",
                        "--output-format-option",
                        "method=text",
                        "--output-format-option",
                        "item-separator=|"),
                "1|2|3");
        Path output = this.directory.resolve("serialized.txt");
        assertSuccess(
                run(
                        "",
                        "run",
                        "-q",
                        query,
                        "-f",
                        "serialize",
                        "-o",
                        output.toString(),
                        "--output-format-option",
                        "method=text",
                        "--output-format-option",
                        "item-separator=|"),
                "");
        assertEquals("1|2|3", Files.readString(output));
    }

    @Test
    void xmlDeclarationAppearsOnceForTheSequence() throws Exception {
        Result result = run("", "run", "-q", "(<a/>, <b/>)", "--default-language", "xquery31");
        assertEquals(0, result.code(), result.toString());
        assertTrue(result.out().startsWith("<?xml "), result.toString());
        assertEquals(result.out().indexOf("<?xml"), result.out().lastIndexOf("<?xml"), result.toString());
        assertTrue(result.out().contains("<a"), result.toString());
        assertTrue(result.out().contains("<b"), result.toString());
        Path output = this.directory.resolve("sequence.xml");
        assertSuccess(
                run("", "run", "-q", "(<a/>, <b/>)", "--default-language", "xquery31", "-o", output.toString()), "");
        assertEquals(result.out(), Files.readString(output));
    }

    @Test
    void incompatibleOutputOptionsAreRejected() throws Exception {
        Result sparkStdout = run("", "run", "-q", "1", "-f", "json");
        assertEquals(42, sparkStdout.code(), sparkStdout.toString());
        assertTrue(sparkStdout.err().contains("require --output-path"), sparkStdout.toString());
        Result partitions = run("", "run", "-q", "1", "-f", "serialize", "-P", "2");
        assertEquals(42, partitions.code(), partitions.toString());
        assertTrue(partitions.err().contains("single stream"), partitions.toString());
    }

    @Test
    void serializeJsonRejectsMultipleTopLevelItems() throws Exception {
        Result result = run("", "run", "-q", "(1, 2)", "-f", "serialize", "--output-format-option", "method=json");
        assertEquals(42, result.code(), result.toString());
        assertTrue(result.err().contains("SERE0023"), result.toString());
        assertSuccess(run("", "run", "-q", "()", "-f", "serialize", "--output-format-option", "method=json"), "null");
    }

    @Test
    void serializedFileUsesRequestedEncoding() throws Exception {
        Path output = this.directory.resolve("unicode.txt");
        assertSuccess(
                run(
                        "",
                        "run",
                        "-q",
                        "\"héllo 世界\"",
                        "-f",
                        "serialize",
                        "-o",
                        output.toString(),
                        "--output-format-option",
                        "method=text",
                        "--output-format-option",
                        "encoding=UTF-16LE"),
                "");
        assertEquals("héllo 世界", Files.readString(output, java.nio.charset.StandardCharsets.UTF_16LE));
    }

    @Test
    void sparkJsonOutputStillUsesDataSourceWriter() throws Exception {
        Path output = this.directory.resolve("spark-json");
        assertSuccess(run("", "run", "-q", "{\"answer\":42}", "-f", "json", "-o", output.toString(), "-P", "1"), "");
        assertTrue(Files.isDirectory(output));
        try (var files = Files.list(output)) {
            Path part = files.filter(path -> path.getFileName().toString().startsWith("part-"))
                    .findFirst()
                    .orElseThrow();
            assertEquals("{\"answer\":42}", Files.readString(part).strip());
        }
    }

    @Test
    void invalidArguments() throws Exception {
        for (String[] arguments : List.of(new String[] {"run"}, new String[] {"run", "--not-an-option"}, new String[] {
            "run", "-q", "1", "query.jq"
        })) {
            Result result = run("", arguments);
            assertEquals(42, result.code(), result.toString());
            assertEquals("", result.out(), result.toString());
            assertTrue(result.err().contains("CLI Error:"), result.toString());
        }
    }

    @Test
    void invalidQuery() throws Exception {
        Result result = run("", "run", "-q", "1 +");
        assertEquals(42, result.code(), result.toString());
        assertEquals("", result.out(), result.toString());
        assertTrue(result.err().contains("XPST0003"), result.toString());
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
                    Files.readString(stdout).replace("\r\n", "\n"),
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
