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

import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.MethodSource;
import org.rumbledb.config.RumbleRuntimeConfiguration;

import java.io.File;
import java.util.List;

@ParameterizedClass
@MethodSource("testFiles")
public class NativeFLWORRuntimeTestsDataFramesDeactivated extends RuntimeTests {

    @Override
    public RumbleRuntimeConfiguration getConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--escape-backticks",
                    "yes",
                    "--data-frame-execution",
                    "no",
                    "--materialization-cap",
                    "100000",
                    "--result-size",
                    "200"
                }
        );
    }

    public static final File nativeFlworRuntimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/runtime-native-flwor"
    );

    public static List<File> testFiles() {
        return loadTestFiles(nativeFlworRuntimeTestsDirectory);
    }
}
