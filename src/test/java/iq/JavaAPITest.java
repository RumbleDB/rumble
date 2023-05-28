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

import org.apache.log4j.LogManager;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.config.RumbleRuntimeConfiguration;

import org.rumbledb.exceptions.CliException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.optimizations.Profiler;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import sparksoniq.spark.SparkSessionManager;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaAPITest {

    public JavaAPITest() {
    }

    @BeforeClass
    public static void setupSparkSession() {
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
        sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.executor.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");
        sparkConfiguration.set("spark.driver.bindAddress", "127.0.0.1");
        sparkConfiguration.set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension");
        sparkConfiguration.set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog");
        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);

    }

    @Test(timeout = 1000000)
    public void testLocal() throws Throwable {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        SequenceOfItems iterator = rumble.runQuery("for $i in 1 to 5 return { \"foo\" : $i }");
        Assert.assertTrue(!iterator.isOpen());
        Assert.assertTrue(!iterator.availableAsRDD());
        iterator.open();
        Assert.assertTrue(iterator.isOpen());
        Assert.assertTrue(iterator.hasNext());
        for (int i = 1; i <= 5; ++i) {
            Assert.assertTrue(iterator.hasNext());
            Item item = iterator.next();
            Assert.assertTrue(item.isObject());
            List<String> keys = item.getKeys();
            Assert.assertTrue(keys.size() == 1);
            String key = keys.get(0);
            Assert.assertTrue(key.contentEquals("foo"));
            Item value = item.getItemByKey(key);
            Assert.assertTrue(value.isInteger());
            Assert.assertTrue(value.getIntValue() == i);
        }
        iterator.close();
        Assert.assertTrue(!iterator.isOpen());
    }

    @Test(timeout = 1000000)
    public void testCollect() throws Throwable {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        SequenceOfItems iterator = rumble.runQuery("for $i in parallelize(1 to 5) return { \"foo\" : $i }");
        Assert.assertTrue(!iterator.isOpen());
        Assert.assertTrue(iterator.availableAsRDD());
        iterator.open();
        Assert.assertTrue(iterator.isOpen());
        Assert.assertTrue(iterator.hasNext());
        for (int i = 1; i <= 5; ++i) {
            Assert.assertTrue(iterator.hasNext());
            Item item = iterator.next();
            Assert.assertTrue(item.isObject());
            List<String> keys = item.getKeys();
            Assert.assertTrue(keys.size() == 1);
            String key = keys.get(0);
            Assert.assertTrue(key.contentEquals("foo"));
            Item value = item.getItemByKey(key);
            Assert.assertTrue(value.isInteger());
            Assert.assertTrue(value.getIntValue() == i);
        }
        iterator.close();
        Assert.assertTrue(!iterator.isOpen());
    }

    @Test(timeout = 1000000)
    public void testRDD() throws Throwable {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        SequenceOfItems iterator = rumble.runQuery("for $i in parallelize(1 to 5) return { \"foo\" : $i }");
        Assert.assertTrue(!iterator.isOpen());
        Assert.assertTrue(iterator.availableAsRDD());
        JavaRDD<Item> items = iterator.getAsRDD();
        List<Item> list = items.collect();
        for (int i = 1; i <= 5; ++i) {
            Item item = list.get(i - 1);
            Assert.assertTrue(item.isObject());
            List<String> keys = item.getKeys();
            Assert.assertTrue(keys.size() == 1);
            String key = keys.get(0);
            Assert.assertTrue(key.contentEquals("foo"));
            Item value = item.getItemByKey(key);
            Assert.assertTrue(value.isInteger());
            Assert.assertTrue(value.getIntValue() == i);
        }
    }

//    @Test(timeout = 1000000)
//    public void testDeltaWrite() throws Throwable {
//        RumbleRuntimeConfiguration config = RumbleRuntimeConfiguration.getDefaultConfiguration();
//        config.setOutputFormat("delta");
//        config.setOutputPath("file:///home/davidl/Documents/Thesis/rumble/sample_json_delta");
//        config.setQueryPath("file:///home/davidl/Documents/Thesis/rumble/test.jq");
//        JsoniqQueryExecutor executor = new JsoniqQueryExecutor(config);
//
//        executor.runQuery();
//
//    }
//
//    @Test(timeout = 1000000)
//    public void testDeltaRead() throws Throwable {
//        RumbleRuntimeConfiguration config = RumbleRuntimeConfiguration.getDefaultConfiguration();
//        config.setOutputFormat("json");
//        config.setOutputPath("file:///home/davidl/Documents/Thesis/rumble/test_res_json.json");
//        config.setQueryPath("file:///home/davidl/Documents/Thesis/rumble/delta_file_query.jq");
//        JsoniqQueryExecutor executor = new JsoniqQueryExecutor(config);
//        executor.runQuery();
//
//    }
}
