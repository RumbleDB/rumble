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

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import utils.annotations.AnnotationParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AnnotationsTestsBase {
    public RumbleRuntimeConfiguration getConfiguration() {
        return TestConfigurations.defaultConfiguration();
    }

    protected abstract File testDirectory();

    protected List<File> testFiles() throws IOException, AnnotationParseException {
        // By default we load JSONiq files, but this can be overriden in subclasses like XQueryTests
        return TestFileDiscovery.jsoniqFiles(testDirectory());
    }

    protected boolean checkOutput() {
        return true;
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("testFiles")
    @Timeout(1000)
    final void testAnnotation(File testFile) throws IOException {
        System.err.println(testFile);
        AnnotationTestExecutor.run(testFile, getConfiguration(), checkOutput());
    }
}
