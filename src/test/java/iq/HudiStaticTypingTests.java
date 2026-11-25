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

import org.rumbledb.config.RumbleConfiguration;

/**
 * Hudi tests that need static typing, which reports the static type errors that the update tests do not, such as a
 * lookup of a key that the type of hudi-table() does not have.
 */
public class HudiStaticTypingTests extends HudiUpdateRuntimeTests {

    public static final File staticTypingTestsDirectory =
            new File(System.getProperty("user.dir") + "/src/test/resources/test_files/runtime-hudi-static-typing");

    @Override
    protected File testDirectory() {
        return staticTypingTestsDirectory;
    }

    @Override
    public RumbleConfiguration getConfiguration() {
        return super.getConfiguration()
            .toBuilder()
            .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
            .build();
    }
}
