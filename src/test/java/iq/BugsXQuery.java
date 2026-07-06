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

package iq;

import iq.base.SparkAnnotationsTestsBase;
import iq.base.TestFileDiscovery;
import org.rumbledb.config.RumbleConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BugsXQuery extends SparkAnnotationsTestsBase {

    public static final File runtimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/bugs-xquery"
    );

    @Override
    public RumbleConfiguration getConfiguration() {
        return RumbleConfiguration.builder()
            .configureDebug(debug -> debug.printIteratorTree(true))
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
