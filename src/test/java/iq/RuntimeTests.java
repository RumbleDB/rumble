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

import iq.base.AnnotationsTestsBase;
import org.apache.spark.SparkConf;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sparksoniq.jsoniq.compiler.JsoniqExpressionTreeVisitor;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;
import utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class RuntimeTests extends AnnotationsTestsBase {

    public static final File runtimeTestsDirectory = new File(System.getProperty("user.dir") +
            "/src/main/resources/test_files/runtime");
    protected final File _testFile;
    protected static List<File> _testFiles = new ArrayList<>();

    public static void readFileList(File dir) {
        FileManager.loadJiqFiles(dir).forEach(file -> RuntimeTests._testFiles.add(file));
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Collection<Object[]> testFiles() {
        List<Object[]> result = new ArrayList<>();
        RuntimeTests.readFileList(RuntimeTests.runtimeTestsDirectory);
        RuntimeTests._testFiles.forEach(file -> result.add(new Object[]{file}));
        return result;
    }

    @Test(timeout = 1000000)
    public void testRuntimeIterators() throws Throwable {
        System.err.println(AnnotationsTestsBase.counter++ + " : " + _testFile);
        JsoniqExpressionTreeVisitor visitor = new JsoniqExpressionTreeVisitor();
        testAnnotations(_testFile.getAbsolutePath(), visitor);
    }

    @BeforeClass
    public static void setupSparkSession() {
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
//        sparkConfiguration.set("spark.driver.memory", "2g");
//        sparkConfiguration.set("spark.executor.memory",   "2g");
        sparkConfiguration.set("spark.speculation", "true");
        sparkConfiguration.set("spark.speculation.quantile", "0.5");
        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);
    }

    public RuntimeTests(File testFile) {

        this._testFile = testFile;
    }

    protected String runIterators(RuntimeIterator iterator) {
        String actualOutput = getIteratorOutput(iterator);
        return actualOutput;
    }

    @Override
    protected void checkExpectedOutput(String expectedOutput, RuntimeIterator runtimeIterator) {
        String actualOutput = runIterators(runtimeIterator);
        Assert.assertTrue("Expected output: " + expectedOutput + " Actual result: "
                        + actualOutput,
                expectedOutput.equals(actualOutput));
                //unorderedItemSequenceStringsAreEqual(expectedOutput, actualOutput));
    }


    protected String getIteratorOutput(RuntimeIterator iterator) {
        iterator.open(new DynamicContext());
        Item result = null;
        if(iterator.hasNext()) {
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
            //remove last comma
            output = output.substring(0, output.length() - 2);
            output += ")";
            return output;
        }
    }


}
