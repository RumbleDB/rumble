/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package utils.annotations;

import scala.util.parsing.combinator.testing.Str;

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
    public static final String ERROR_MESSAGE = "ErrorCode";
    public static final String SHOULD_PARSE = "ShouldParse";
    public static final String SHOULD_NOT_PARSE = "ShouldNotParse";
    public static final String SHOULD_COMPILE = "ShouldCompile";
    public static final String SHOULD_NOT_COMPILE = "ShouldNotCompile";
    public static final String SHOULD_RUN = "ShouldRun";
    public static final String SHOULD_CRASH = "ShouldCrash";

    public static abstract class TestAnnotation {
        public TestAnnotation() {
        }

        public String getOutput() {
            return expectedOutput;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public abstract boolean shouldParse();
        public abstract boolean shouldCompile();
        public abstract boolean shouldRun();

        protected String expectedOutput = "";
        protected String errorCode = "";
    }

    public static class RunnableTestAnnotation extends TestAnnotation {
        public String getOutput() {
            return expectedOutput;
        }

        public RunnableTestAnnotation(String expectedOut) {
            super();
            this.expectedOutput = expectedOut;
            this.errorCode = null;
        }

        @Override
        public boolean shouldParse() {
            return true;
        }

        @Override
        public boolean shouldCompile() {return true;}

        @Override
        public boolean shouldRun() {return true;}
    }

    public static class UnrunnableTestAnnotation extends TestAnnotation {

        public UnrunnableTestAnnotation(String errorCode) {
            super();
            this.errorCode = errorCode;
        }

        @Override
        public boolean shouldParse() {
            return true;
        }

        @Override
        public boolean shouldCompile() {return true;}

        @Override
        public boolean shouldRun() {return false;}

    }

    public static class UncompilableTestAnnotation extends TestAnnotation {

        public UncompilableTestAnnotation(String errorCode) {
            super();
            this.errorCode = errorCode;
        }

        @Override
        public boolean shouldParse() {
            return true;
        }

        @Override
        public boolean shouldCompile() {return false;}

        @Override
        public boolean shouldRun() {
            throw new RuntimeException("Compile annotations runLocal request");
        }
    }

    public static class CompilableTestAnnotation extends TestAnnotation {

        public CompilableTestAnnotation(String errorCode) {
            super();
            this.errorCode = errorCode;
        }

        public CompilableTestAnnotation() {
            super();
            this.errorCode = null;
        }

        @Override
        public boolean shouldParse() {
            return true;
        }

        @Override
        public boolean shouldCompile() {return true;}

        @Override
        public boolean shouldRun() {
            throw new RuntimeException("Compile annotations runLocal request");
        }
    }

    public static class ParsableTestAnnotation extends TestAnnotation {

        public ParsableTestAnnotation() {
            super();
            this.errorCode = null;
        }

        public ParsableTestAnnotation(String errorCode) {
            super();
            this.errorCode = errorCode;
        }

        @Override
        public boolean shouldParse() {
            return true;
        }

        @Override
        public boolean shouldCompile() {
            throw new RuntimeException("Parsable annotations compilation request");
        }

        @Override
        public boolean shouldRun() {
            throw new RuntimeException("Parsable annotations runLocal request");
        }
    }

    public static class UnparsableTestAnnotation extends TestAnnotation {
        public UnparsableTestAnnotation(String errorCode) {
            super();
            this.errorCode = errorCode;
        }

        @Override
        public boolean shouldParse() {
            return false;
        }

        @Override
        public boolean shouldCompile() {
            throw new RuntimeException("Parsable annotations compilation request");
        }

        @Override
        public boolean shouldRun() {
            throw new RuntimeException("Parsable annotations runLocal request");
        }

    }


    public static final String MAGIC_COOKIE = "(:JIQS:";

    public static String readAnnotationText(Reader reader) throws IOException {
        BufferedReader bread = new BufferedReader(reader);
        return String
                .join(" ",
                        (Iterable<String>) bread.lines().map(String::trim)
                                .filter((line) -> line.startsWith(MAGIC_COOKIE))
                                .map((line) -> line.substring(MAGIC_COOKIE.length()).trim())::iterator)
                .replace(":)", "").trim();
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
            } else if (token.contains("=")) {

            String[] tokenParts = token.split("=", 2);
            String key = tokenParts[0].trim();
            String value = tokenParts[1].trim();
            // Trim any quotes surrounding the value.
            if (value.startsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            value = value.replace("\\n", String.format("%n"));
            parameters.put(key, value);
        }
        }

        if (!shouldParse.isPresent() && !shouldCompile.isPresent() && !shouldRun.isPresent()) {
            throw new AnnotationParseException(annotationText,
                    String.format("Missing compilability indicator (e.g. [%s]).", SHOULD_PARSE));
        }

        if(shouldRun.isPresent()){
            if(shouldRun.get())
                return new RunnableTestAnnotation(parameters.get(OUTPUT_KEY));
            else
                return new UnrunnableTestAnnotation(parameters.get(ERROR_MESSAGE));
        }

        if(shouldCompile.isPresent()){
            if(shouldCompile.get())
                return new CompilableTestAnnotation();
            else
                return new UncompilableTestAnnotation(parameters.get(ERROR_MESSAGE));
        }

        if (shouldParse.get())
            return new ParsableTestAnnotation();
        else
            return new UnparsableTestAnnotation(parameters.get(ERROR_MESSAGE));
    }
}
