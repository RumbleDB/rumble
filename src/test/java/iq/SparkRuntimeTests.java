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
package iq;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Isolated;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import iq.base.AnnotationTestExecutor;
import iq.base.TestFileDiscovery;

import org.rumbledb.config.RumbleConfiguration;

@Isolated("The implicit timezone regression temporarily changes the JVM default timezone")
public class SparkRuntimeTests extends RuntimeTests {

    public static final File sparkRuntimeTestsDirectory =
            new File(System.getProperty("user.dir") + "/src/test/resources/test_files/runtime-spark");

    public RumbleConfiguration getConfiguration() {
        return RumbleConfiguration.builder()
                .configureRuntime(runtime -> runtime.resultsSizeCap(200).materializationCap(100000))
                .configureSemantics(semantics -> semantics.datesWithTimeZone(true))
                .build();
    }

    @Override
    protected File testDirectory() {
        return sparkRuntimeTestsDirectory;
    }

    List<File> xqueryTestFiles() throws IOException {
        return TestFileDiscovery.xqueryFiles(testDirectory());
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("xqueryTestFiles")
    @Timeout(1000)
    void testXQueryAnnotation(File testFile) throws IOException {
        TimeZone originalTimezone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+01:00"));
        try {
            AnnotationTestExecutor.run(testFile, getConfiguration(), getExternalBindings(), checkOutput());
        } finally {
            TimeZone.setDefault(originalTimezone);
        }
    }
}
