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

import java.io.File;

import org.rumbledb.config.RumbleConfiguration;

/**
 * Base class for the per-language fn:format-integer word formatting suites.
 *
 * <p>
 * These tests depend on CLDR data shipped with ICU4J rather than on RumbleDB's own logic, so they live outside
 * {@code test_files/runtime} and run in their own workflow. That way an ICU4J upgrade shows up as a self-contained
 * failure naming the affected language instead of a red {@code RuntimeTests}.
 * </p>
 *
 * <p>
 * No Spark session is needed, so this extends {@link AnnotationsTestsBase} directly.
 * </p>
 */
public abstract class FormattingLanguageTestsBase extends AnnotationsTestsBase {

    private static final String TEST_FILES_ROOT =
            System.getProperty("user.dir") + "/src/test/resources/test_files/formatting-languages/";

    /**
     * @return the BCP 47 language subtag whose directory holds this suite's test files.
     */
    protected abstract String language();

    @Override
    protected File testDirectory() {
        return new File(TEST_FILES_ROOT + language());
    }

    @Override
    public RumbleConfiguration getConfiguration() {
        // The default result size cap is well below the number of expressions in these files, which would
        // silently truncate the compared sequence instead of failing.
        return TestConfigurations.defaultConfigurationBuilder()
                .configureRuntime(runtime -> runtime.resultsSizeCap(200).materializationCap(100000))
                .build();
    }
}
