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
import org.junit.Assert;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import utils.FileManager;
import utils.annotations.AnnotationParseException;
import utils.annotations.AnnotationProcessor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnnotationsTestsBase {
    protected static int counter = 0;
    protected List<File> testFiles = new ArrayList<>();
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

    public RumbleRuntimeConfiguration getConfiguration() {
        return defaultConfiguration;
    }

    public void initializeTests(File dir) {
        FileManager.loadJiqFiles(dir).forEach(file -> this.testFiles.add(file));
        this.testFiles.sort(Comparator.comparing(File::getName));
    }

    /**
     * Tests annotations
     */
    public static void testAnnotations(
            String path,
            RumbleRuntimeConfiguration configuration,
            boolean checkOutput,
            boolean applyUpdates,
            int resultSizeCap
    )
            throws IOException {
        AnnotationProcessor.TestAnnotation currentAnnotation = null;
        try {
            currentAnnotation = AnnotationProcessor.readAnnotation(new FileReader(path));
        } catch (AnnotationParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
        SequenceOfItems sequence = null;
        try {
            URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                path,
                configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
            Rumble rumble = new Rumble(configuration);
            sequence = rumble.runQuery(uri);
        } catch (ParsingException exception) {
            String errorOutput = exception.getMessage();
            checkErrorCode(
                errorOutput,
                currentAnnotation.getErrorCode(),
                currentAnnotation.getErrorMetadata()
            );
            if (currentAnnotation.shouldParse()) {
                Assert.fail("Program did not parse when expected to.\nError output: " + errorOutput + "\n");
                return;
            } else {
                System.out.println(errorOutput);
                return;
            }

            // SEMANTIC
        } catch (SemanticException exception) {
            String errorOutput = exception.getMessage();
            checkErrorCode(
                errorOutput,
                currentAnnotation.getErrorCode(),
                currentAnnotation.getErrorMetadata()
            );
            try {
                if (currentAnnotation.shouldCompile()) {
                    Assert.fail("Program did not compile when expected to.\nError output: " + errorOutput + "\n");
                    return;
                } else {
                    System.out.println(errorOutput);
                    Assert.assertTrue(true);
                    return;
                }
            } catch (Exception ex) {
            }

            // RUNTIME
        } catch (RumbleException exception) {
            String errorOutput = exception.getMessage();
            checkErrorCode(
                errorOutput,
                currentAnnotation.getErrorCode(),
                currentAnnotation.getErrorMetadata()
            );
            try {
                if (currentAnnotation.shouldRun()) {
                    Assert.fail("Program did not run when expected to.\nError output: " + errorOutput + "\n");
                    return;
                } else {
                    System.out.println(errorOutput);
                    Assert.assertTrue(true);
                    return;
                }
            } catch (Exception ex) {
            }
        }

        try {
            if (!currentAnnotation.shouldCompile()) {
                Assert.fail("Program compiled when not expected to.\n");
                return;
            }
        } catch (Exception ex) {
        }

        if (!currentAnnotation.shouldParse()) {
            Assert.fail("Program parsed when not expected to.\n");
            return;
        }

        // PROGRAM SHOULD RUN
        if (
            currentAnnotation instanceof AnnotationProcessor.RunnableTestAnnotation
                &&
                currentAnnotation.shouldRun()
        ) {
            try {
                checkExpectedOutput(currentAnnotation.getOutput(), sequence, checkOutput, applyUpdates, resultSizeCap);
            } catch (RumbleException exception) {
                String errorOutput = exception.getMessage();
                errorOutput += "\n" + ExceptionUtils.getStackTrace(exception);
                Assert.fail("Program did not run when expected to.\nError output: " + errorOutput + "\n");
            }
        } else {
            // PROGRAM SHOULD CRASH
            if (
                currentAnnotation instanceof AnnotationProcessor.UnrunnableTestAnnotation
                    &&
                    !currentAnnotation.shouldRun()
            ) {
                try {
                    checkExpectedOutput(
                        currentAnnotation.getOutput(),
                        sequence,
                        checkOutput,
                        applyUpdates,
                        resultSizeCap
                    );
                } catch (Exception exception) {
                    String errorOutput = exception.getMessage();
                    checkErrorCode(
                        errorOutput,
                        currentAnnotation.getErrorCode(),
                        currentAnnotation.getErrorMetadata()
                    );
                    return;
                }

                Assert.fail("Program executed when not expected to");
            }
        }
        return;
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
        Assert.assertTrue(
            "Expected output: " + expectedOutput + "\nActual result: " + actualOutput,
            expectedOutput.equals(actualOutput)
        );
    }

    protected static void checkErrorCode(String errorOutput, String expectedErrorCode, String errorMetadata) {
        if (errorOutput != null && expectedErrorCode != null)
            Assert.assertTrue(
                "Unexpected error code returned; Expected: "
                    + expectedErrorCode
                    +
                    "; Error: "
                    + errorOutput,
                errorOutput.contains(expectedErrorCode)
            );
        if (errorOutput != null && errorMetadata != null)
            Assert.assertTrue(
                "Unexpected metadata returned; Expected: "
                    + errorMetadata
                    +
                    "; Error: "
                    + errorOutput,
                errorOutput.contains(errorMetadata)
            );
    }

    public static String getIteratorOutput(SequenceOfItems sequence, int resultSizeCap) {
        sequence.open();
        Item result = null;
        if (sequence.hasNext()) {
            result = sequence.next();
        }
        if (result == null) {
            return "";
        }
        String singleOutput = result.serialize();
        if (!sequence.hasNext()) {
            return singleOutput;
        } else {
            int itemCount = 1;
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(result.serialize());
            sb.append(", ");
            while (
                sequence.hasNext()
                    &&
                    ((itemCount < resultSizeCap
                        && resultSizeCap > 0)
                        ||
                        resultSizeCap == 0)
            ) {
                sb.append(sequence.next().serialize());
                sb.append(", ");
                itemCount++;
            }
            if (sequence.hasNext() && itemCount == resultSizeCap) {
                System.err.println(
                    "Warning! The output sequence contains a large number of items but its materialization was capped at "
                        + resultSizeCap
                        + " items. This value can be configured with the --result-size parameter at startup"
                );
            }
            // remove last comma
            String output = sb.toString();
            output = output.substring(0, output.length() - 2);
            output += ")";
            return output;
        }
    }
}
