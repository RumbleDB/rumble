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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rumbledb.api.Item;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import scala.Function0;
import scala.util.Properties;
import sparksoniq.spark.SparkSessionManager;
import utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class XMLTests extends AnnotationsTestsBase {

    public static final File runtimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/xml"
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

    public XMLTests(File testFile) {
        this.testFile = testFile;
    }

    public RumbleRuntimeConfiguration getConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--print-iterator-tree",
                    "yes",
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--result-size",
                    "200",
                    "--apply-updates",
                    "yes" }
        );
    }

    public static void readFileList(File dir) {
        XMLTests._testFiles.addAll(FileManager.loadJiqFiles(dir));
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Collection<Object[]> testFiles() {
        List<Object[]> result = new ArrayList<>();
        XMLTests.readFileList(XMLTests.runtimeTestsDirectory);
        XMLTests._testFiles.forEach(file -> result.add(new Object[] { file }));
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
        testAnnotations(this.testFile.getAbsolutePath(), getConfiguration());
    }

    @Override
    protected void checkExpectedOutput(
            String expectedOutput,
            SequenceOfItems sequence
    ) {
        String actualOutput;
        if (!sequence.availableAsRDD()) {
            actualOutput = getIteratorOutput(sequence);
        } else {
            actualOutput = getRDDResults(sequence);
        }
        Assert.assertTrue(
            "Expected output: " + expectedOutput + "\nActual result: " + actualOutput,
            expectedOutput.equals(actualOutput)
        );
    }

    protected String getIteratorOutput(SequenceOfItems sequence) {
        sequence.open();
        Item result = null;
        if (sequence.hasNext()) {
            result = sequence.next();
        }
        if (result == null) {
            return "";
        }
        String singleOutput = result.serialize();
        if (!sequence.hasNext()) {
            return singleOutput;
        } else {
            int itemCount = 1;
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(result.serialize());
            sb.append(", ");
            while (
                sequence.hasNext()
                    &&
                    ((itemCount < getConfiguration().getResultSizeCap()
                        && getConfiguration().getResultSizeCap() > 0)
                        ||
                        getConfiguration().getResultSizeCap() == 0)
            ) {
                sb.append(sequence.next().serialize());
                sb.append(", ");
                itemCount++;
            }
            if (sequence.hasNext() && itemCount == getConfiguration().getResultSizeCap()) {
                System.err.println(
                    "Warning! The output sequence contains a large number of items but its materialization was capped at "
                        + getConfiguration().getResultSizeCap()
                        + " items. This value can be configured with the --result-size parameter at startup"
                );
            }
            // remove last comma
            String output = sb.toString();
            output = output.substring(0, output.length() - 2);
            output += ")";
            return output;
        }
    }

    private String getRDDResults(SequenceOfItems sequence) {
        List<Item> res = sequence.getFirstItemsAsList(getConfiguration().getResultSizeCap());
        List<String> collectedOutput = res.stream().map(item -> item.serialize()).collect(Collectors.toList());

        if (collectedOutput.isEmpty()) {
            return "";
        }

        if (collectedOutput.size() == 1) {
            return collectedOutput.get(0);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String item : collectedOutput) {
            sb.append(item);
            sb.append(", ");
        }

        String result = sb.toString();
        result = result.substring(0, result.length() - 2);
        result += ")";
        return result;
    }
}
