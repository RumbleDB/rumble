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

package utils.annotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Credit to Andrei Barsan, teammate from ACD ;)
 */
public class AnnotationProcessor {
    private static final String TOKEN_SEPARATOR = "\\s*;\\s*";

    public static final String OUTPUT_KEY = "Output";
    public static final String UPDATE_DIM_KEY = "UpdateDim";
    public static final String ERROR_MESSAGE = "ErrorCode";
    public static final String ERROR_METADATA = "ErrorMetadata";
    public static final String SHOULD_PARSE = "ShouldParse";
    public static final String SHOULD_NOT_PARSE = "ShouldNotParse";
    public static final String SHOULD_COMPILE = "ShouldCompile";
    public static final String SHOULD_NOT_COMPILE = "ShouldNotCompile";
    public static final String SHOULD_RUN = "ShouldRun";
    public static final String SHOULD_CRASH = "ShouldCrash";
    public static final String MAGIC_COOKIE = "(:JIQS:";

    public static String readAnnotationText(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        return String
            .join(
                " ",
                (Iterable<String>) bufferedReader.lines()
                    .map(String::trim)
                    .filter((line) -> line.startsWith(MAGIC_COOKIE))
                    .map((line) -> line.substring(MAGIC_COOKIE.length()).trim())::iterator
            )
            .replace(":)", "")
            .trim();
    }

    public static TestAnnotation readAnnotation(Reader reader) throws IOException, AnnotationParseException {
        String annotationText = readAnnotationText(reader);
        if (annotationText.isEmpty()) {
            throw new AnnotationParseException(annotationText, "Found empty annotation.");
        }
        String[] annotationTokens = annotationText.split(TOKEN_SEPARATOR);

        AnnotationExpectation expectation = null;
        Map<String, String> parameters = new HashMap<>();
        for (String token : annotationTokens) {
            AnnotationExpectation tokenExpectation = AnnotationExpectation.fromToken(token);
            if (tokenExpectation != null) {
                if (expectation != null) {
                    throw new AnnotationParseException(annotationText, "Found multiple test expectations.");
                }
                expectation = tokenExpectation;
            } else if (token.contains("=")) {
                String[] tokenParts = token.split("=", 2);
                String key = tokenParts[0].trim();
                String value = tokenParts[1].trim();
                if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                value = value.replaceAll("([^\\\\])\\\\n", "$1\n").replaceAll("\\\\\\\\n", "\\\\n");
                parameters.put(key, value);
            }
        }

        if (expectation == null) {
            throw new AnnotationParseException(
                    annotationText,
                    String.format("Missing test expectation (e.g. [%s]).", SHOULD_PARSE)
            );
        }

        return new TestAnnotation(
                expectation,
                parameters.get(OUTPUT_KEY),
                parameters.get(ERROR_MESSAGE),
                parameters.get(ERROR_METADATA)
        );
    }

    public static UpdateDimensions readUpdateDimensions(Reader reader) throws IOException, AnnotationParseException {
        String annotationText = readAnnotationText(reader);
        if (annotationText.isEmpty()) {
            throw new AnnotationParseException(annotationText, "Found empty annotation.");
        }
        String[] annotationTokens = annotationText.split(TOKEN_SEPARATOR);
        for (String token : annotationTokens) {
            if (token.contains(UPDATE_DIM_KEY)) {

                String[] tokenParts = token.split("=", 2);
                String value = tokenParts[1].trim();
                return parseUpdateDimensions(annotationText, value);
            }
        }
        throw new AnnotationParseException(annotationText, "No UpdateDim key found.");
    }

    private static UpdateDimensions parseUpdateDimensions(String annotationText, String value)
            throws AnnotationParseException {
        if (!value.matches("\\[\\d+,\\d+]")) {
            throw new AnnotationParseException(
                    annotationText,
                    "UpdateDim key does not match regex: \"\\[\\d+,\\d+]\""
            );
        }
        String[] dimensions = value.substring(1, value.length() - 1).split(",");
        return new UpdateDimensions(
                Integer.parseInt(dimensions[0]),
                Integer.parseInt(dimensions[1])
        );
    }

    // Declaration order follows the query pipeline and is used for stage comparisons.
    public enum TestStage {
        PARSING("parse"),
        COMPILATION("compile"),
        RUNTIME("run");

        private final String verb;

        TestStage(String verb) {
            this.verb = verb;
        }

        public String verb() {
            return this.verb;
        }

        private boolean isAfter(TestStage other) {
            return this.ordinal() > other.ordinal();
        }
    }

    public enum ExpectedOutcome {
        SUCCESS,
        FAILURE
    }

    public enum AnnotationExpectation {
        UNPARSABLE(SHOULD_NOT_PARSE, TestStage.PARSING, ExpectedOutcome.FAILURE),
        PARSABLE(SHOULD_PARSE, TestStage.PARSING, ExpectedOutcome.SUCCESS),
        UNCOMPILABLE(SHOULD_NOT_COMPILE, TestStage.COMPILATION, ExpectedOutcome.FAILURE),
        COMPILABLE(SHOULD_COMPILE, TestStage.COMPILATION, ExpectedOutcome.SUCCESS),
        UNRUNNABLE(SHOULD_CRASH, TestStage.RUNTIME, ExpectedOutcome.FAILURE),
        RUNNABLE(SHOULD_RUN, TestStage.RUNTIME, ExpectedOutcome.SUCCESS);

        private final String token;
        private final TestStage stage;
        private final ExpectedOutcome outcome;

        AnnotationExpectation(String token, TestStage stage, ExpectedOutcome outcome) {
            this.token = token;
            this.stage = stage;
            this.outcome = outcome;
        }

        public TestStage stage() {
            return this.stage;
        }

        public boolean expectsSuccess() {
            return this.outcome == ExpectedOutcome.SUCCESS;
        }

        public boolean acceptsFailureAt(TestStage failureStage) {
            // Positive annotations guarantee only their target stage; later failures are out of scope.
            return expectsSuccess()
                ? failureStage.isAfter(this.stage)
                : failureStage == this.stage;
        }

        private static AnnotationExpectation fromToken(String token) {
            for (AnnotationExpectation expectation : values()) {
                if (expectation.token.equals(token)) {
                    return expectation;
                }
            }
            return null;
        }
    }

    public record TestAnnotation(
            AnnotationExpectation expectation,
            String output,
            String errorCode,
            String errorMetadata) {
    }

    // Update tests use these coordinates to run files in deterministic dependency order.
    public record UpdateDimensions(int dimension1, int dimension2) {
    }

}
