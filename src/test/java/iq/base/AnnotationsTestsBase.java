/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package iq.base;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import utils.FileManager;
import utils.annotations.AnnotationParseException;
import utils.annotations.AnnotationProcessor;
import utils.annotations.AnnotationProcessor.AnnotationExpectation;
import utils.annotations.AnnotationProcessor.TestStage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationsTestsBase {
    protected static int counter = 0;

    protected static final RumbleRuntimeConfiguration defaultConfiguration = new RumbleRuntimeConfiguration(
            new String[] {
                "--print-iterator-tree",
                "yes",
                "--variable:externalUnparsedString",
                "unparsed string",
                "--materialization-cap",
                "200" }
    ).setExternalVariableValue(
        Name.createVariableInNoNamespace("externalStringItem"),
        Collections.singletonList(ItemFactory.getInstance().createStringItem("this is a string"))
    )
        .setExternalVariableValue(
            Name.createVariableInNoNamespace("externalIntegerItems"),
            Arrays.asList(
                new Item[] {
                    ItemFactory.getInstance().createIntItem(1),
                    ItemFactory.getInstance().createIntItem(2),
                    ItemFactory.getInstance().createIntItem(3),
                    ItemFactory.getInstance().createIntItem(4),
                    ItemFactory.getInstance().createIntItem(5),
                }
            )
        );

    private record QueryExecutionResult(SequenceOfItems sequence, TestStage failureStage, String failureMessage) {
        private static QueryExecutionResult success(SequenceOfItems sequence) {
            return new QueryExecutionResult(sequence, null, null);
        }

        private static QueryExecutionResult failure(TestStage failureStage, String failureMessage) {
            return new QueryExecutionResult(null, failureStage, failureMessage);
        }

        private boolean failed() {
            return this.failureStage() != null;
        }
    }

    public RumbleRuntimeConfiguration getConfiguration() {
        return defaultConfiguration;
    }

    protected static List<File> loadTestFiles(File dir) {
        return loadTestFiles(dir, true);
    }

    protected static List<File> loadTestFiles(File dir, boolean isJsoniq) {
        List<File> files = isJsoniq
            ? FileManager.loadJiqFiles(dir)
            : FileManager.loadXqFiles(dir);

        return files.stream()
            .sorted(Comparator.comparing(File::getName))
            .collect(Collectors.toList());
    }

    protected void runAnnotationTest(File testFile, boolean checkOutput) throws Throwable {
        System.err.println(counter++ + " : " + testFile);
        RumbleRuntimeConfiguration configuration = getConfiguration();
        testAnnotations(
            testFile.getAbsolutePath(),
            configuration,
            checkOutput,
            configuration.applyUpdates(),
            configuration.getResultSizeCap()
        );
    }

    public static void testAnnotations(
            String path,
            RumbleRuntimeConfiguration configuration,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    )
            throws IOException {
        AnnotationProcessor.TestAnnotation annotation = readAnnotation(path);
        QueryExecutionResult executionResult = executeQuery(path, configuration);

        // Parse, compile, or early runtime failures are handled before any result materialization.
        if (executionResult.failed()) {
            assertExpectedFailure(annotation, executionResult);
            return;
        }

        switch (annotation.expectation()) {
            case UNPARSABLE, UNCOMPILABLE:
                // Fail the test because the query was expected to fail during parsing or compilation, but it succeeded.
                Assertions.fail(unexpectedSuccessMessage(annotation.expectation().stage()));
                return;
            case PARSABLE, COMPILABLE:
                return;
            case RUNNABLE:
                assertOutput(annotation, executionResult.sequence(), checkOutput, applyUpdates, resultSizeCap);
                return;
            case UNRUNNABLE:
                // Runtime errors may be deferred until the sequence is materialized.
                assertExpectedRuntimeFailureDuringMaterialization(
                    annotation,
                    executionResult.sequence(),
                    applyUpdates,
                    resultSizeCap
                );
                return;
            default:
                throw new IllegalStateException("Unhandled expectation: " + annotation.expectation());
        }
    }

    private static AnnotationProcessor.TestAnnotation readAnnotation(String path) throws IOException {
        try (Reader annotationReader = new FileReader(path)) {
            return AnnotationProcessor.readAnnotation(annotationReader);
        } catch (AnnotationParseException e) {
            throw new AssertionError("Could not parse test annotation for " + path, e);
        }
    }

    private static QueryExecutionResult executeQuery(String path, RumbleRuntimeConfiguration configuration) {
        try {
            URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                path,
                configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
            Rumble rumble = new Rumble(configuration);
            return QueryExecutionResult.success(rumble.runQuery(uri));
        } catch (ParsingException exception) {
            return QueryExecutionResult.failure(TestStage.PARSING, exception.getMessage());
        } catch (SemanticException exception) {
            return QueryExecutionResult.failure(TestStage.COMPILATION, exception.getMessage());
        } catch (RumbleException exception) {
            return QueryExecutionResult.failure(TestStage.RUNTIME, exception.getMessage());
        } catch (IOException exception) {
            throw new AssertionError("Could not execute test query for " + path, exception);
        }
    }

    private static void assertExpectedFailure(
            AnnotationProcessor.TestAnnotation annotation,
            QueryExecutionResult executionResult
    ) {
        AnnotationExpectation expectation = annotation.expectation();
        if (!expectation.acceptsFailureAt(executionResult.failureStage())) {
            Assertions.fail(unexpectedFailureMessage(expectation, executionResult));
        }

        checkErrorCode(
            executionResult.failureMessage(),
            annotation.errorCode(),
            annotation.errorMetadata()
        );
        System.out.println(executionResult.failureMessage());
    }

    private static String unexpectedFailureMessage(
            AnnotationExpectation expectation,
            QueryExecutionResult executionResult
    ) {
        if (expectation.expectsSuccess()) {
            return unexpectedFailureMessage(executionResult.failureStage(), executionResult.failureMessage());
        }
        return "Program failed during "
            + executionResult.failureStage().verb()
            + " when expected to fail during "
            + expectation.stage().verb()
            + ".\nError output: "
            + executionResult.failureMessage()
            + "\n";
    }

    private static String unexpectedFailureMessage(TestStage stage, String errorOutput) {
        return "Program did not " + stage.verb() + " when expected to.\nError output: " + errorOutput + "\n";
    }

    private static String unexpectedSuccessMessage(TestStage stage) {
        return "Program did " + stage.verb() + " when not expected to.\n";
    }

    private static void assertOutput(
            AnnotationProcessor.TestAnnotation annotation,
            SequenceOfItems sequence,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        try {
            checkExpectedOutput(annotation.output(), sequence, checkOutput, applyUpdates, resultSizeCap);
        } catch (RumbleException exception) {
            String errorOutput = exception.getMessage() + "\n" + ExceptionUtils.getStackTrace(exception);
            Assertions.fail(unexpectedFailureMessage(TestStage.RUNTIME, errorOutput));
        }
    }

    private static void assertExpectedRuntimeFailureDuringMaterialization(
            AnnotationProcessor.TestAnnotation annotation,
            SequenceOfItems sequence,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        try {
            // Crash tests materialize the sequence only to trigger a runtime error; their output is undefined.
            materializeSequence(sequence, applyUpdates, resultSizeCap);
            Assertions.fail(unexpectedSuccessMessage(TestStage.RUNTIME));
        } catch (Exception exception) {
            checkErrorCode(exception.getMessage(), annotation.errorCode(), annotation.errorMetadata());
        }
    }

    static void checkExpectedOutput(
            String expectedOutput,
            SequenceOfItems sequence,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        String actualOutput = materializeSequence(sequence, applyUpdates, resultSizeCap);
        if (!checkOutput) {
            return;
        }
        Assertions.assertEquals(expectedOutput, actualOutput, "Unexpected query output.");
    }

    private static String materializeSequence(
            SequenceOfItems sequence,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        String output = getIteratorOutput(sequence, resultSizeCap);
        if (applyUpdates && sequence.availableAsPUL()) {
            sequence.applyPUL();
        }
        return output;
    }

    protected static void checkErrorCode(String errorOutput, String expectedErrorCode, String errorMetadata) {
        assertErrorContains(errorOutput, "error code", expectedErrorCode);
        assertErrorContains(errorOutput, "metadata", errorMetadata);
    }

    private static void assertErrorContains(String errorOutput, String label, String expectedValue) {
        if (expectedValue == null) {
            return;
        }
        Assertions.assertNotNull(errorOutput, "Missing error output; Expected " + label + ": " + expectedValue);
        Assertions.assertTrue(
            errorOutput.contains(expectedValue),
            "Unexpected " + label + "; Expected: " + expectedValue + "; Error: " + errorOutput
        );
    }

    public static String getIteratorOutput(SequenceOfItems sequence, int resultSizeCap) {
        sequence.open();
        if (!sequence.hasNext()) {
            return "";
        }

        StringBuilder output = new StringBuilder(sequence.next().serialize());
        if (!sequence.hasNext()) {
            return output.toString();
        }

        int itemCount = 1;
        output.insert(0, "(");
        while (sequence.hasNext() && isWithinResultSizeCap(itemCount, resultSizeCap)) {
            output.append(", ");
            output.append(sequence.next().serialize());
            itemCount++;
        }
        output.append(")");

        if (sequence.hasNext() && resultSizeCap > 0 && itemCount == resultSizeCap) {
            System.err.println(
                "Warning! The output sequence contains a large number of items but its materialization was capped at "
                    + resultSizeCap
                    + " items. This value can be configured with the --result-size parameter at startup"
            );
        }

        return output.toString();
    }

    private static boolean isWithinResultSizeCap(int itemCount, int resultSizeCap) {
        return resultSizeCap == 0 || itemCount < resultSizeCap;
    }
}
