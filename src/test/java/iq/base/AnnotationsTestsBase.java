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
import utils.annotations.AnnotationProcessor.PhaseExpectation;

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

    private enum FailureStage {
        PARSING("parse"),
        SEMANTIC("compile"),
        RUNTIME("run");

        private final String verb;

        FailureStage(String verb) {
            this.verb = verb;
        }

        private String unexpectedFailureMessage(String errorOutput) {
            return "Program did not " + this.verb + " when expected to.\nError output: " + errorOutput + "\n";
        }

        private String unexpectedSuccessMessage() {
            return "Program did " + this.verb + " when not expected to.\n";
        }
    }

    private static final class QueryExecutionResult {
        private final SequenceOfItems sequence;
        private final FailureStage failureStage;
        private final String failureMessage;

        private QueryExecutionResult(SequenceOfItems sequence, FailureStage failureStage, String failureMessage) {
            this.sequence = sequence;
            this.failureStage = failureStage;
            this.failureMessage = failureMessage;
        }

        private static QueryExecutionResult success(SequenceOfItems sequence) {
            return new QueryExecutionResult(sequence, null, null);
        }

        private static QueryExecutionResult failure(FailureStage failureStage, String failureMessage) {
            return new QueryExecutionResult(null, failureStage, failureMessage);
        }

        private boolean failed() {
            return this.failureStage != null;
        }
    }

    public RumbleRuntimeConfiguration getConfiguration() {
        return defaultConfiguration;
    }

    protected List<File> loadTestFiles(File dir) {
        return FileManager.loadJiqFiles(dir)
            .stream()
            .sorted(Comparator.comparing(File::getName))
            .collect(Collectors.toList());
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

        if (executionResult.failed()) {
            assertExpectedFailure(annotation, executionResult);
            return;
        }

        assertSuccessfulParseAndCompile(annotation);
        PhaseExpectation runtimeExpectation = annotation.getExpectation().runtime();
        if (runtimeExpectation == PhaseExpectation.NOT_APPLICABLE) {
            return;
        }
        if (runtimeExpectation == PhaseExpectation.MUST_SUCCEED) {
            assertOutput(annotation, executionResult.sequence, checkOutput, applyUpdates, resultSizeCap);
            return;
        }

        assertExpectedRuntimeFailureDuringMaterialization(
            annotation,
            executionResult.sequence,
            checkOutput,
            applyUpdates,
            resultSizeCap
        );
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
            return QueryExecutionResult.failure(FailureStage.PARSING, exception.getMessage());
        } catch (SemanticException exception) {
            return QueryExecutionResult.failure(FailureStage.SEMANTIC, exception.getMessage());
        } catch (RumbleException exception) {
            return QueryExecutionResult.failure(FailureStage.RUNTIME, exception.getMessage());
        } catch (IOException exception) {
            throw new AssertionError("Could not execute test query for " + path, exception);
        }
    }

    private static void assertExpectedFailure(
            AnnotationProcessor.TestAnnotation annotation,
            QueryExecutionResult executionResult
    ) {
        checkErrorCode(
            executionResult.failureMessage,
            annotation.getErrorCode(),
            annotation.getErrorMetadata()
        );

        if (shouldReachStage(annotation, executionResult.failureStage)) {
            Assertions.fail(executionResult.failureStage.unexpectedFailureMessage(executionResult.failureMessage));
        }

        System.out.println(executionResult.failureMessage);
    }

    private static boolean shouldReachStage(
            AnnotationProcessor.TestAnnotation annotation,
            FailureStage failureStage
    ) {
        return expectationForStage(annotation.getExpectation(), failureStage) == PhaseExpectation.MUST_SUCCEED;
    }

    private static void assertSuccessfulParseAndCompile(AnnotationProcessor.TestAnnotation annotation) {
        AnnotationExpectation expectation = annotation.getExpectation();
        if (expectation.parsing() == PhaseExpectation.MUST_FAIL) {
            Assertions.fail(FailureStage.PARSING.unexpectedSuccessMessage());
        }
        PhaseExpectation compileExpectation = expectation.compilation();
        if (compileExpectation == PhaseExpectation.NOT_APPLICABLE) {
            return;
        }
        if (compileExpectation == PhaseExpectation.MUST_FAIL) {
            Assertions.fail(FailureStage.SEMANTIC.unexpectedSuccessMessage());
        }
    }

    private static PhaseExpectation expectationForStage(
            AnnotationExpectation expectation,
            FailureStage failureStage
    ) {
        switch (failureStage) {
            case PARSING:
                return expectation.parsing();
            case SEMANTIC:
                return expectation.compilation();
            case RUNTIME:
                return expectation.runtime();
            default:
                throw new IllegalStateException("Unhandled failure stage: " + failureStage);
        }
    }

    private static void assertOutput(
            AnnotationProcessor.TestAnnotation annotation,
            SequenceOfItems sequence,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        try {
            checkExpectedOutput(annotation.getOutput(), sequence, checkOutput, applyUpdates, resultSizeCap);
        } catch (RumbleException exception) {
            String errorOutput = exception.getMessage() + "\n" + ExceptionUtils.getStackTrace(exception);
            Assertions.fail(FailureStage.RUNTIME.unexpectedFailureMessage(errorOutput));
        }
    }

    private static void assertExpectedRuntimeFailureDuringMaterialization(
            AnnotationProcessor.TestAnnotation annotation,
            SequenceOfItems sequence,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        try {
            checkExpectedOutput(annotation.getOutput(), sequence, checkOutput, applyUpdates, resultSizeCap);
            Assertions.fail(FailureStage.RUNTIME.unexpectedSuccessMessage());
        } catch (Exception exception) {
            checkErrorCode(exception.getMessage(), annotation.getErrorCode(), annotation.getErrorMetadata());
        }
    }

    static void checkExpectedOutput(
            String expectedOutput,
            SequenceOfItems sequence,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    ) {
        String actualOutput = getIteratorOutput(sequence, resultSizeCap);
        if (applyUpdates && sequence.availableAsPUL()) {
            sequence.applyPUL();
        }
        if (!checkOutput) {
            return;
        }
        Assertions.assertEquals(expectedOutput, actualOutput, "Unexpected query output.");
    }

    protected static void checkErrorCode(String errorOutput, String expectedErrorCode, String errorMetadata) {
        if (errorOutput != null && expectedErrorCode != null) {
            Assertions.assertTrue(
                errorOutput.contains(expectedErrorCode),
                "Unexpected error code returned; Expected: " + expectedErrorCode + "; Error: " + errorOutput
            );
        }
        if (errorOutput != null && errorMetadata != null) {
            Assertions.assertTrue(
                errorOutput.contains(errorMetadata),
                "Unexpected metadata returned; Expected: " + errorMetadata + "; Error: " + errorOutput
            );
        }
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
