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
 * Authors: Stefan Irimescu, Can Berker Cikis, Marco Schöb
 *
 */

package iq;

import iq.base.AnnotationsTestsBase;
import org.apache.spark.SparkConf;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import scala.util.Properties;
import sparksoniq.spark.SparkSessionManager;
import utils.FileManager;
import scala.Function0;

import java.io.File;
import java.util.*;

@RunWith(Parameterized.class)
public class BackwardsCompatibilityTests extends AnnotationsTestsBase {

    public static final File runtimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/backwards-compatibility"
    );
    public static final String javaVersion =
        System.getProperty("java.version");
    public static final String scalaVersion =
        Properties.scalaPropOrElse("version.number", new Function0<String>() {
            @Override
            public String apply() {
                return "unknown";
            }
        });
    protected static List<File> _testFiles = new ArrayList<>();
    protected final File testFile;

    public BackwardsCompatibilityTests(File testFile) {
        this.testFile = testFile;
    }

    public RumbleRuntimeConfiguration getConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--print-iterator-tree",
                    "yes",
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--apply-updates",
                    "yes" }
        );
    }

    public static void readFileList(File dir) {
        BackwardsCompatibilityTests._testFiles.addAll(FileManager.loadJiqFiles(dir));
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Collection<Object[]> testFiles() {
        List<Object[]> result = new ArrayList<>();
        BackwardsCompatibilityTests.readFileList(BackwardsCompatibilityTests.runtimeTestsDirectory);
        BackwardsCompatibilityTests._testFiles.forEach(file -> result.add(new Object[] { file }));
        return result;
    }

    @BeforeClass
    public static void setupSparkSession() {
        SparkSessionManager.getInstance().resetSession();
        System.err.println("Java version: " + javaVersion);
        System.err.println("Scala version: " + scalaVersion);
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
        sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.executor.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.extraClassPath", "lib/");
        sparkConfiguration.set("spark.sql.crossJoin.enabled", "true"); // enables cartesian product
        sparkConfiguration.set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension"); // enables delta
        sparkConfiguration.set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog"); // enables

        // prevents spark from failing to start on MacOS when disconnected from the internet
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");

        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);
        System.err.println("Spark version: " + SparkSessionManager.getInstance().getJavaSparkContext().version());
    }

    @Test(timeout = 1000000)
    public final void testRuntimeIterators() throws Throwable {
        System.err.println(AnnotationsTestsBase.counter++ + " : " + this.testFile);
        AnnotationsTestsBase.testAnnotations(
            this.testFile.getAbsolutePath(),
            getConfiguration(),
            true,
            getConfiguration().applyUpdates(),
            getConfiguration().getResultSizeCap()
        );
    }
}
