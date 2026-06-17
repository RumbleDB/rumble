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
import java.util.Optional;

/**
 * Credit to Andrei Barsan, teammate from ACD ;)
 */
public class AnnotationProcessor {

    public static final String OUTPUT_KEY = "Output";
    public static final String UPDATE_DIM_KEY = "UpdateDim";
    public static final String UPDATE_TABLE_KEY = "UpdateTable";
    public static final String CREATE_TABLE = "CreateTable";
    public static final String DELETE_TABLE = "DeleteTable";
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
        BufferedReader bread = new BufferedReader(reader);
        return String
            .join(
                " ",
                (Iterable<String>) bread.lines()
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
        String[] annotationTokens = annotationText.split("\\s*;\\s*");

        Optional<Boolean> shouldParse = Optional.empty();
        Optional<Boolean> shouldCompile = Optional.empty();
        Optional<Boolean> shouldRun = Optional.empty();

        Optional<Boolean> deleteTable = Optional.of(false);
        Optional<Boolean> createTable = Optional.of(false);

        Optional<Integer> updateDim1 = Optional.empty();
        Optional<Integer> updateDim2 = Optional.empty();

        Map<String, String> parameters = new HashMap<>();
        for (String token : annotationTokens) {
            if (token.equals(SHOULD_PARSE)) {
                shouldParse = Optional.of(true);
            } else if (token.equals(SHOULD_NOT_PARSE)) {
                shouldParse = Optional.of(false);
            }
            if (token.equals(SHOULD_COMPILE)) {
                shouldCompile = Optional.of(true);
            } else if (token.equals(SHOULD_NOT_COMPILE)) {
                shouldCompile = Optional.of(false);
            }
            if (token.equals(SHOULD_RUN)) {
                shouldRun = Optional.of(true);
            } else if (token.equals(SHOULD_CRASH)) {
                shouldRun = Optional.of(false);
            } else if (token.equals(DELETE_TABLE)) {
                deleteTable = Optional.of(true);
            } else if (token.equals(CREATE_TABLE)) {
                createTable = Optional.of(true);
            } else if (token.contains("=")) {

                String[] tokenParts = token.split("=", 2);
                String key = tokenParts[0].trim();
                String value = tokenParts[1].trim();
                // Trim any quotes surrounding the value.
                if (value.startsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                if (key.equals(UPDATE_DIM_KEY)) {
                    String[] dimsArr = value.substring(1, value.length() - 1).split(",");
                    updateDim1 = Optional.of(Integer.parseInt(dimsArr[0]));
                    updateDim2 = Optional.of(Integer.parseInt(dimsArr[1]));
                }

                value = value.replaceAll("([^\\\\])\\\\n", "$1\n").replaceAll("\\\\\\\\n", "\\\\n");
                parameters.put(key, value);
            }
        }

        if (!shouldParse.isPresent() && !shouldCompile.isPresent() && !shouldRun.isPresent()) {
            throw new AnnotationParseException(
                    annotationText,
                    String.format("Missing compilability indicator (e.g. [%s]).", SHOULD_PARSE)
            );
        }

        if (shouldRun.isPresent()) {
            if (shouldRun.get())
                if (updateDim1.isPresent() && updateDim2.isPresent()) {
                    return new UpdatingRunnableTestAnnotation(
                            parameters.get(OUTPUT_KEY),
                            parameters.get(UPDATE_TABLE_KEY),
                            updateDim1.get(),
                            updateDim2.get(),
                            deleteTable.get(),
                            createTable.get()
                    );
                } else {
                    return new RunnableTestAnnotation(parameters.get(OUTPUT_KEY));
                }
            else
                return new UnrunnableTestAnnotation(
                        parameters.get(ERROR_MESSAGE),
                        parameters.get(ERROR_METADATA)
                );
        }

        if (shouldCompile.isPresent()) {
            if (shouldCompile.get())
                return new CompilableTestAnnotation();
            else
                return new UncompilableTestAnnotation(
                        parameters.get(ERROR_MESSAGE),
                        parameters.get(ERROR_METADATA)
                );
        }

        if (shouldParse.get())
            return new ParsableTestAnnotation();
        else
            return new UnparsableTestAnnotation(
                    parameters.get(ERROR_MESSAGE),
                    parameters.get(ERROR_METADATA)
            );
    }

    public static String getUpdateDimensionAnnotation(Reader reader) throws IOException, AnnotationParseException {
        String annotationText = readAnnotationText(reader);
        if (annotationText.isEmpty()) {
            throw new AnnotationParseException(annotationText, "Found empty annotation.");
        }
        String[] annotationTokens = annotationText.split("\\s*;\\s*");
        for (String token : annotationTokens) {
            if (token.contains(UPDATE_DIM_KEY)) {

                String[] tokenParts = token.split("=", 2);
                String value = tokenParts[1].trim();
                if (!value.matches("\\[\\d+,\\d+]")) {
                    throw new AnnotationParseException(
                            annotationText,
                            "UpdateDim key does not match regex: \"\\[\\d+,\\d+]\""
                    );
                }
                return value;
            }
        }
        throw new AnnotationParseException(annotationText, "No UpdateDim key found.");
    }

    public enum PhaseExpectation {
        MUST_SUCCEED,
        MUST_FAIL,
        NOT_APPLICABLE
    }

    public enum AnnotationExpectation {
        UNPARSABLE(PhaseExpectation.NOT_APPLICABLE, PhaseExpectation.NOT_APPLICABLE),
        PARSABLE(PhaseExpectation.NOT_APPLICABLE, PhaseExpectation.NOT_APPLICABLE),
        UNCOMPILABLE(PhaseExpectation.MUST_FAIL, PhaseExpectation.NOT_APPLICABLE),
        COMPILABLE(PhaseExpectation.MUST_SUCCEED, PhaseExpectation.NOT_APPLICABLE),
        UNRUNNABLE(PhaseExpectation.MUST_SUCCEED, PhaseExpectation.MUST_FAIL),
        RUNNABLE(PhaseExpectation.MUST_SUCCEED, PhaseExpectation.MUST_SUCCEED);

        private final PhaseExpectation compilation;
        private final PhaseExpectation runtime;

        AnnotationExpectation(
                PhaseExpectation compilation,
                PhaseExpectation runtime
        ) {
            this.compilation = compilation;
            this.runtime = runtime;
        }

        public PhaseExpectation parsing() {
            return this == UNPARSABLE ? PhaseExpectation.MUST_FAIL : PhaseExpectation.MUST_SUCCEED;
        }

        public PhaseExpectation compilation() {
            return this.compilation;
        }

        public PhaseExpectation runtime() {
            return this.runtime;
        }
    }

    public static abstract class TestAnnotation {
        private final AnnotationExpectation expectation;
        protected String expectedOutput = "";
        protected String errorCode = "";
        protected String errorMetadata = "";
        protected String deltaTablePath = "";
        protected int updatingDim1 = -1;
        protected int updatingDim2 = -1;
        protected boolean shouldDeleteTable = false;
        protected boolean shouldCreateTable = false;

        public TestAnnotation(AnnotationExpectation expectation) {
            this.expectation = expectation;
        }

        public AnnotationExpectation getExpectation() {
            return this.expectation;
        }

        public String getOutput() {
            return this.expectedOutput;
        }

        public String getErrorCode() {
            return this.errorCode;
        }

        public String getErrorMetadata() {
            return this.errorMetadata;
        }

        public String getDeltaTablePath() {
            return this.deltaTablePath;
        }

        public int getUpdatingDim1() {
            return this.updatingDim1;
        }

        public int getUpdatingDim2() {
            return this.updatingDim2;
        }

        public boolean shouldDeleteTable() {
            return this.shouldDeleteTable;
        }

        public boolean shouldCreateTable() {
            return this.shouldCreateTable;
        }
    }

    public static class RunnableTestAnnotation extends TestAnnotation {
        public RunnableTestAnnotation(String expectedOut) {
            super(AnnotationExpectation.RUNNABLE);
            this.expectedOutput = expectedOut;
            this.errorCode = null;
            this.errorMetadata = null;
        }

        public String getOutput() {
            return this.expectedOutput;
        }
    }

    public static class UpdatingRunnableTestAnnotation extends RunnableTestAnnotation {
        public UpdatingRunnableTestAnnotation(
                String expectedOut,
                String deltaTablePath,
                int dim1,
                int dim2,
                boolean shouldDeleteTable,
                boolean shouldCreateTable
        ) {
            super(expectedOut);
            this.deltaTablePath = deltaTablePath;
            this.updatingDim1 = dim1;
            this.updatingDim2 = dim2;
            this.shouldDeleteTable = shouldDeleteTable;
            this.shouldCreateTable = shouldCreateTable;
        }
    }

    public static class UnrunnableTestAnnotation extends TestAnnotation {
        public UnrunnableTestAnnotation(String errorCode, String errorMetadata) {
            super(AnnotationExpectation.UNRUNNABLE);
            this.errorCode = errorCode;
            this.errorMetadata = errorMetadata;
        }
    }

    public static class UncompilableTestAnnotation extends TestAnnotation {
        public UncompilableTestAnnotation(String errorCode, String errorMetadata) {
            super(AnnotationExpectation.UNCOMPILABLE);
            this.errorCode = errorCode;
            this.errorMetadata = errorMetadata;
        }
    }

    public static class CompilableTestAnnotation extends TestAnnotation {
        public CompilableTestAnnotation() {
            super(AnnotationExpectation.COMPILABLE);
            this.errorCode = null;
        }
    }

    public static class ParsableTestAnnotation extends TestAnnotation {
        public ParsableTestAnnotation() {
            super(AnnotationExpectation.PARSABLE);
            this.errorCode = null;
        }
    }

    public static class UnparsableTestAnnotation extends TestAnnotation {
        public UnparsableTestAnnotation(String errorCode, String errorMetadata) {
            super(AnnotationExpectation.UNPARSABLE);
            this.errorCode = errorCode;
            this.errorMetadata = errorMetadata;
        }
    }
}
