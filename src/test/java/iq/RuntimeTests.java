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
import org.apache.spark.api.java.JavaRDD;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rumbledb.api.Item;
import org.rumbledb.compiler.TranslationVisitor;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;
import utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class RuntimeTests extends AnnotationsTestsBase {

    public static final File runtimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/main/resources/test_files/runtime"
    );
    protected static List<File> _testFiles = new ArrayList<>();
    protected final File _testFile;

    public RuntimeTests(File testFile) {
        this._testFile = testFile;
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
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
        sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.executor.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.extraClassPath", "lib/");
        sparkConfiguration.set("spark.sql.crossJoin.enabled", "true"); // enables cartesian product

        // prevents spark from failing to start on MacOS when disconnected from the internet
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");


        // sparkConfiguration.set("spark.driver.memory", "2g");
        // sparkConfiguration.set("spark.executor.memory", "2g");
        // sparkConfiguration.set("spark.speculation", "true");
        // sparkConfiguration.set("spark.speculation.quantile", "0.5");
        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);

    }

    @Test(timeout = 1000000)
    public void testRuntimeIterators() throws Throwable {
        System.err.println(AnnotationsTestsBase.counter++ + " : " + this._testFile);
        TranslationVisitor visitor = new TranslationVisitor();
        testAnnotations(this._testFile.getAbsolutePath());
    }

    @Override
    protected void checkExpectedOutput(String expectedOutput, RuntimeIterator runtimeIterator) {
        String actualOutput;
        if (!runtimeIterator.isRDD()) {
            actualOutput = runIterators(runtimeIterator);
        } else {
            actualOutput = getRDDResults(runtimeIterator);
        }
        Assert.assertTrue(
            "Expected output: " + expectedOutput + " Actual result: " + actualOutput,
            expectedOutput.equals(actualOutput)
        );
        // unorderedItemSequenceStringsAreEqual(expectedOutput, actualOutput));
    }

    protected String runIterators(RuntimeIterator iterator) {
        String actualOutput = getIteratorOutput(iterator);
        return actualOutput;
    }

    protected String getIteratorOutput(RuntimeIterator iterator) {
        iterator.open(new DynamicContext());
        Item result = null;
        if (iterator.hasNext()) {
            result = iterator.next();
        }
        if (result == null) {
            return "";
        }
        String singleOutput = result.serialize();
        if (!iterator.hasNext())
            return singleOutput;
        else {
            String output = "(" + result.serialize() + ", ";
            while (iterator.hasNext()) {
                result = iterator.next();
                if (result != null)
                    output += result.serialize() + ", ";
            }
            // remove last comma
            output = output.substring(0, output.length() - 2);
            output += ")";
            return output;
        }
    }

    private String getRDDResults(RuntimeIterator runtimeIterator) {
        JavaRDD<Item> rdd = runtimeIterator.getRDD(new DynamicContext());
        JavaRDD<String> output = rdd.map(o -> o.serialize());
        int resultCount = output.take(2).size();
        if (resultCount == 0) {
            return "";
        }
        if (resultCount == 1) {
            return output.collect().get(0);
        }
        if (resultCount > 1) {
            List<String> collectedOutput;
            /*
             * if (SparkSessionManager.LIMIT_COLLECT()) {
             * collectedOutput = output.take(SparkSessionManager.COLLECT_ITEM_LIMIT);
             * if (collectedOutput.size() == SparkSessionManager.COLLECT_ITEM_LIMIT) {
             * ShellStart.terminal.output("\nWarning: Results have been truncated to: " +
             * SparkSessionManager.COLLECT_ITEM_LIMIT
             * + " items. This value can be configured with the --result-size parameter at startup.\n");
             * }
             * } else {
             */
            collectedOutput = output.collect();
            // }

            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (String item : collectedOutput) {
                sb.append(item + ", ");
            }

            sb.delete(sb.length() - 2, sb.length());
            sb.append(")");
            return sb.toString();
        }
        throw new OurBadException("Unexpected rdd result count in getRDDResults()");
    }
}
