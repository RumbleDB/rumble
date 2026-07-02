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

import scala.Function0;
import scala.util.Properties;

import org.apache.spark.SparkConf;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.tests.commons.RumbleDBTestCommons;

import sparksoniq.spark.SparkSessionManager;
import utils.FileManager;
import java.io.File;
import java.util.*;

@RunWith(Parameterized.class)
public class RuntimeTests {

    public static final File runtimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/runtime"
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

    public RuntimeTests(File testFile) {
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
                    "yes",
                    "--lax-json-null-validation",
                    "no",
                    "--result-size",
                    "200",
                    "--materialization-cap",
                    "200" }
        ).setExternalVariableValue(
            Name.createVariableInNoNamespace("externalStringItem"),
            Collections.singletonList(ItemFactory.getInstance().createStringItem("this is a string"))
        )
            .setExternalVariableValue(
                Name.createVariableInNoNamespace("externalIntegerItems"),
                Arrays.asList(
                    new Item[] {
                        ItemFactory.getInstance().createIntItem(1),
                        ItemFactory.getInstance().createIntItem(2),
                        ItemFactory.getInstance().createIntItem(3),
                        ItemFactory.getInstance().createIntItem(4),
                        ItemFactory.getInstance().createIntItem(5),
                    }
                )
            );
    }

    public static void readFileList(File dir) {
        FileManager.loadJiqFiles(dir).forEach(file -> RuntimeTests._testFiles.add(file));
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Collection<Object[]> testFiles() {
        List<Object[]> result = new ArrayList<>();
        RuntimeTests.readFileList(RuntimeTests.runtimeTestsDirectory);
        RuntimeTests._testFiles.forEach(file -> result.add(new Object[] { file }));
        return result;
    }

    @BeforeClass
    public static void setupSparkSession() {
        SparkSessionManager.getInstance().resetSession();
        System.err.println("Java version: " + javaVersion);
        System.err.println("Scala version: " + scalaVersion);
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
        sparkConfiguration.set("spark.sql.adaptive.enabled", "false");
        sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.executor.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.extraClassPath", "lib/");
        sparkConfiguration.set("spark.sql.crossJoin.enabled", "true"); // enables cartesian product
        sparkConfiguration.set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension"); // enables delta
                                                                                                   // store
        sparkConfiguration.set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog"); // enables
                                                                                                                      // delta
                                                                                                                      // store

        // prevents spark from failing to start on MacOS when disconnected from the internet
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");


        // sparkConfiguration.set("spark.driver.memory", "2g");
        // sparkConfiguration.set("spark.executor.memory", "2g");
        // sparkConfiguration.set("spark.speculation", "true");
        // sparkConfiguration.set("spark.speculation.quantile", "0.5");
        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);
        System.err.println("Spark version: " + SparkSessionManager.getInstance().getJavaSparkContext().version());
    }

    @Test(timeout = 1000000)
    public void testRuntimeIterators() throws Throwable {
        // System.err.println(AnnotationsTestsBase.counter++ + " : " + this.testFile);
        RumbleDBTestCommons.testAnnotations(
            this.testFile.getAbsolutePath(),
            getConfiguration(),
            true
        );
    }
}
