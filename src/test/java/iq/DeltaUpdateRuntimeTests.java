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

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.tests.commons.RumbleDBTestCommons;

import utils.annotations.AnnotationParseException;
import utils.annotations.AnnotationProcessor;
import org.apache.spark.SparkConf;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import scala.Function0;
import scala.util.Properties;
import sparksoniq.spark.SparkSessionManager;
import utils.FileManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@RunWith(Parameterized.class)
public class DeltaUpdateRuntimeTests {

    public static final File runtimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/runtime-delta-updates"
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

    public RumbleRuntimeConfiguration getConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--escape-backticks",
                    "yes",
                    "--dates-with-timezone",
                    "yes",
                    "--print-iterator-tree",
                    "yes",
                    "--show-error-info",
                    "yes",
                    "--apply-updates",
                    "yes",
                    "--materialization-cap",
                    "900000",
                    "--result-size",
                    "900000"
                }
        );
    }

    protected static final RumbleRuntimeConfiguration createDeltaConfiguration = new RumbleRuntimeConfiguration(
            new String[] {
                "--print-iterator-tree",
                "yes",
                "--output-format",
                "delta",
                "--show-error-info",
                "yes",
                "--apply-updates",
                "yes",
            }
    );

    protected static final RumbleRuntimeConfiguration deleteDeltaConfiguration = new RumbleRuntimeConfiguration(
            new String[] {
                "--print-iterator-tree",
                "yes",
                "--output-format",
                "delta",
                "--show-error-info",
                "yes",
                "--apply-updates",
                "yes",
            }
    );

    protected static Map<Integer, Map<Integer, File>> _testFilesMap;

    protected final File testFile;


    public DeltaUpdateRuntimeTests(File testFile) {
        this.testFile = testFile;
    }

    public static void readFileList(File dir) throws IOException, AnnotationParseException {
        Map<Integer, Map<Integer, File>> updateDimToFileMap = new TreeMap<>();
        Map<Integer, File> innerMap;
        for (File file : FileManager.loadJiqFiles(dir)) {
            FileReader reader = new FileReader(file.getPath());
            String dims = AnnotationProcessor.getUpdateDimensionAnnotation(reader);
            String[] dimsArr = dims.substring(1, dims.length() - 1).split(",");
            int dim1 = Integer.parseInt(dimsArr[0]);
            int dim2 = Integer.parseInt(dimsArr[1]);
            innerMap = updateDimToFileMap.getOrDefault(dim1, new TreeMap<>());
            innerMap.put(dim2, file);
            updateDimToFileMap.put(dim1, innerMap);
        }
        DeltaUpdateRuntimeTests._testFilesMap = updateDimToFileMap;
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Collection<Object[]> testFiles() throws IOException, AnnotationParseException {
        List<Object[]> result = new ArrayList<>();

        // Base test folder
        File baseDir = DeltaUpdateRuntimeTests.runtimeTestsDirectory;

        // Optional subdirectory via -Ddir
        String subDir = System.getProperty("dir");
        File chosenDir = (subDir == null || subDir.trim().isEmpty())
            ? baseDir
            : new File(baseDir, subDir.trim());

        // Check existence
        if (!chosenDir.exists() || !chosenDir.isDirectory()) {
            throw new IOException("[DeltaUpdateRuntimeTests] Subdirectory not found: " + chosenDir.getAbsolutePath());
        }

        DeltaUpdateRuntimeTests.readFileList(chosenDir);

        Map<Integer, File> innerMap;
        File curr;
        for (int i : DeltaUpdateRuntimeTests._testFilesMap.keySet()) {
            innerMap = DeltaUpdateRuntimeTests._testFilesMap.get(i);
            for (int j : innerMap.keySet()) {
                curr = innerMap.get(j);
                result.add(new Object[] { curr });
            }
        }
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
        try {
            AnnotationProcessor.TestAnnotation currentAnnotation = AnnotationProcessor.readAnnotation(
                new FileReader(this.testFile.getAbsolutePath())
            );
        } catch (AnnotationParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
        RumbleDBTestCommons.testAnnotations(
            this.testFile.getAbsolutePath(),
            getConfiguration(),
            true
        );
    }

}
