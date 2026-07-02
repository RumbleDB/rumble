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

import iq.base.AnnotationsTestsBase;
import org.apache.spark.SparkConf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.MethodSource;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import scala.Function0;
import scala.util.Properties;
import sparksoniq.spark.SparkSessionManager;

import java.io.File;
import java.util.List;

@ParameterizedClass
@MethodSource("testFiles")
public class BugsXQuery extends AnnotationsTestsBase {

    public static final File runtimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/bugs-xquery"
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
    @Parameter
    File testFile;

    public RumbleRuntimeConfiguration getConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--print-iterator-tree",
                    "yes",
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--apply-updates",
                    "yes",
                    "--default-language",
                    "xquery31" }
        );
    }

    public static List<File> testFiles() {
        return loadTestFiles(runtimeTestsDirectory, false);
    }

    @BeforeAll
    public static void setupSparkSession() {
        SparkSessionManager.getInstance().resetSession();
        System.err.println("Java version: " + javaVersion);
        System.err.println("Scala version: " + scalaVersion);
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
        sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.executor.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.extraClassPath", "lib/");
        sparkConfiguration.set("spark.sql.crossJoin.enabled", "true");
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");

        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);
        System.err.println("Spark version: " + SparkSessionManager.getInstance().getJavaSparkContext().version());
    }

    @Test
    @Timeout(1000)
    public void testRuntimeIterators() throws Throwable {
        runAnnotationTest(this.testFile, true);
    }
}
