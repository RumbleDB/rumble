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

import java.io.File;
import java.util.List;

import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.MethodSource;
import org.rumbledb.config.RumbleRuntimeConfiguration;

@ParameterizedClass
@MethodSource("testFiles")
public class MLTests extends RuntimeTests {

    public static final File sparkRuntimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/RumbleML"
    );

    public RumbleRuntimeConfiguration getConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--escape-backticks",
                    "yes",
                    "--native-execution",
                    "no",
                    "--result-size",
                    "200",
                    "--materialization-cap",
                    "200"
                }
        );
    }

    public static List<File> testFiles() {
        return loadTestFiles(sparkRuntimeTestsDirectory);
    }
}
