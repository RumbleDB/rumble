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
import java.nio.file.Path;
import java.util.List;

import iq.base.SparkAnnotationsTestsBase;
import iq.base.TestFileDiscovery;

import org.rumbledb.config.RumbleConfiguration;

/** Annotation regressions that check both inferred types and materialized results. */
public class TypeInferenceTests extends SparkAnnotationsTestsBase {
    private static final Path ROOT =
            Path.of(System.getProperty("user.dir"), "src/test/resources/test_files/type-inference");

    @Override
    protected File testDirectory() {
        return ROOT.toFile();
    }

    @Override
    protected List<File> testFiles() throws IOException {
        return TestFileDiscovery.files(testDirectory(), ".jq", ".xq");
    }

    @Override
    protected RumbleConfiguration getConfiguration(File testFile) {
        boolean strictTyping = testFile.toPath().startsWith(ROOT.resolve("strict"));
        return RumbleConfiguration.builder()
                .configureAnalysis(analysis -> analysis.enableStaticTyping(strictTyping))
                .configureRuntime(runtime -> runtime.resultsSizeCap(200))
                .build();
    }
}
