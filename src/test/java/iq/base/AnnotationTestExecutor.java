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
 */

package iq.base;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import utils.annotations.AnnotationParseException;
import utils.annotations.AnnotationProcessor;
import utils.annotations.AnnotationProcessor.AnnotationExpectation;
import utils.annotations.AnnotationProcessor.TestStage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;

public final class AnnotationTestExecutor {

    private AnnotationTestExecutor() {
    }

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

    public static void run(
            File testFile,
            RumbleRuntimeConfiguration configuration,
            boolean checkOutput
    )
            throws IOException {
        run(
            testFile.getAbsolutePath(),
            configuration,
            checkOutput,
            configuration.applyUpdates(),
            configuration.getResultSizeCap()
        );
    }

    static void run(
            String path,
            RumbleRuntimeConfiguration configuration,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    )
            throws IOException {
        AnnotationProcessor.TestAnnotation annotation = readAnnotation(path);
        QueryExecutionResult executionResult = executeQuery(path, configuration);

        if (executionResult.failed()) {
            assertExpectedFailure(annotation, executionResult);
            return;
        }

        switch (annotation.expectation()) {
            case UNPARSABLE, UNCOMPILABLE:
                Assertions.fail(withTestFile(path, unexpectedSuccessMessage(annotation.expectation().stage())));
                return;
            case PARSABLE, COMPILABLE:
                return;
            case RUNNABLE:
                assertOutput(path, annotation, executionResult.sequence(), checkOutput, applyUpdates, resultSizeCap);
                return;
            case UNRUNNABLE:
                assertExpectedRuntimeFailureDuringMaterialization(
                    annotation,
                    path,
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
        } catch (AnnotationParseException exception) {
            throw new AssertionError("Could not parse test annotation for " + path, exception);
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
        } catch (Throwable exception) {
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
            String path,
            AnnotationProcessor.TestAnnotation annotation,
            SequenceOfItems sequence,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        try {
            checkExpectedOutput(path, annotation.output(), sequence, checkOutput, applyUpdates, resultSizeCap);
        } catch (RumbleException exception) {
            String errorOutput = exception.getMessage() + "\n" + ExceptionUtils.getStackTrace(exception);
            Assertions.fail(withTestFile(path, unexpectedFailureMessage(TestStage.RUNTIME, errorOutput)));
        } catch (Throwable exception) {
            // Catch all other exceptions not given by Rumble
            Assertions.fail(withTestFile(path, unexpectedFailureMessage(TestStage.RUNTIME, exception.getMessage())));
        }
    }

    private static void assertExpectedRuntimeFailureDuringMaterialization(
            AnnotationProcessor.TestAnnotation annotation,
            String path,
            SequenceOfItems sequence,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        try {
            materializeSequence(sequence, applyUpdates, resultSizeCap);
            Assertions.fail(withTestFile(path, unexpectedSuccessMessage(TestStage.RUNTIME)));
        } catch (Throwable exception) {
            checkErrorCode(exception.getMessage(), annotation.errorCode(), annotation.errorMetadata());
        }
    }

    private static void checkExpectedOutput(
            String path,
            String expectedOutput,
            SequenceOfItems sequence,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        String actualOutput = materializeSequence(sequence, applyUpdates, resultSizeCap);
        if (checkOutput) {
            Assertions.assertEquals(expectedOutput, actualOutput, withTestFile(path, "Unexpected query output."));
        }
    }

    private static String withTestFile(String path, String message) {
        return "Test file: " + path + "\n" + message;
    }

    private static String materializeSequence(
            SequenceOfItems sequence,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        String output = sequence.serialize();
        if (applyUpdates && sequence.availableAsPUL()) {
            sequence.applyPUL();
        }
        return output;
    }

    private static void checkErrorCode(String errorOutput, String expectedErrorCode, String errorMetadata) {
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

}
