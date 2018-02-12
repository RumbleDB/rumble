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
package iq;

import org.apache.spark.SparkConf;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sparksoniq.spark.SparkContextManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class SparkRuntimeTests extends RuntimeTests {

    public static final File sparkRuntimeTestsDirectory = new File(System.getProperty("user.dir") +
            "/src/main/resources/test_files/runtime-spark");

    public SparkRuntimeTests(File testFile) {
        super(testFile);
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
//        sparkConfiguration.set("spark.driver.memory", "2g");
//        sparkConfiguration.set("spark.executor.memory",   "2g");
        sparkConfiguration.set("spark.speculation", "true");
        sparkConfiguration.set("spark.speculation.quantile", "0.5");
        SparkContextManager.getInstance().initializeConfigurationAndContext(sparkConfiguration, true);

    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Collection<Object[]> testFiles() {
        List<Object[]> result = new ArrayList<>();
        _testFiles.clear();
        readFileList(sparkRuntimeTestsDirectory);
        _testFiles.forEach(file -> result.add(new Object[]{file}));
        return result;
    }

}
