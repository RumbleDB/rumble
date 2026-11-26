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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.parallel.Isolated;

import iq.base.AnnotationsTestsBase;
import iq.base.TestFileDiscovery;

import org.rumbledb.config.RumbleConfiguration;

@Isolated("Changes the JVM default timezone")
public class XQueryDriftTests extends AnnotationsTestsBase {

    public static final File runtimeTestsDirectory =
            new File(System.getProperty("user.dir") + "/src/test/resources/test_files/xquery-drift");

    private TimeZone originalTimezone;

    @BeforeAll
    void setDriftTestTimezone() {
        this.originalTimezone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));
    }

    @AfterAll
    void restoreTimezone() {
        TimeZone.setDefault(this.originalTimezone);
    }

    @Override
    public RumbleConfiguration getConfiguration() {
        return RumbleConfiguration.builder()
                .configureRuntime(runtime -> runtime.shouldApplyUpdates(true))
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build();
    }

    @Override
    protected File testDirectory() {
        return runtimeTestsDirectory;
    }

    @Override
    protected List<File> testFiles() throws IOException {
        return TestFileDiscovery.xqueryFiles(testDirectory());
    }
}
