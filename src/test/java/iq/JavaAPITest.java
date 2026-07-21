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

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.items.parsing.RowToItemMapper;
import org.rumbledb.types.ItemTypeFactory;

import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class JavaAPITest {

    public JavaAPITest() {
    }

    @BeforeAll
    public static void setupSparkSession() {
        SparkSessionManager.getInstance().resetSession();
        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
        sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.executor.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");
        sparkConfiguration.set("spark.driver.bindAddress", "127.0.0.1");
        sparkConfiguration.set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension");
        sparkConfiguration.set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog");
        sparkConfiguration.set("spark.databricks.delta.schema.autoMerge.enabled", "true");
        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);

    }

    @Test
    @Timeout(1000)
    public void testLocal() throws Throwable {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        SequenceOfItems iterator = rumble.runQuery("for $i in 1 to 5 return { \"foo\" : $i }");
        Assertions.assertTrue(!iterator.isOpen());
        Assertions.assertTrue(!iterator.availableAsRDD());
        iterator.open();
        Assertions.assertTrue(iterator.isOpen());
        Assertions.assertTrue(iterator.hasNext());
        for (int i = 1; i <= 5; ++i) {
            Assertions.assertTrue(iterator.hasNext());
            Item item = iterator.next();
            Assertions.assertTrue(item.isObject());
            List<String> keys = item.getStringKeys();
            Assertions.assertTrue(keys.size() == 1);
            String key = keys.get(0);
            Assertions.assertTrue(key.contentEquals("foo"));
            Item value = item.getItemByKey(key);
            Assertions.assertTrue(value.isInteger());
            Assertions.assertTrue(value.getIntValue() == i);
        }
        iterator.close();
        Assertions.assertTrue(!iterator.isOpen());
    }

    @Test
    @Timeout(1000)
    public void testCollect() throws Throwable {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        SequenceOfItems iterator = rumble.runQuery("for $i in parallelize(1 to 5) return { \"foo\" : $i }");
        Assertions.assertTrue(!iterator.isOpen());
        Assertions.assertTrue(iterator.availableAsRDD());
        iterator.open();
        Assertions.assertTrue(iterator.isOpen());
        Assertions.assertTrue(iterator.hasNext());
        for (int i = 1; i <= 5; ++i) {
            Assertions.assertTrue(iterator.hasNext());
            Item item = iterator.next();
            Assertions.assertTrue(item.isObject());
            List<String> keys = item.getStringKeys();
            Assertions.assertTrue(keys.size() == 1);
            String key = keys.get(0);
            Assertions.assertTrue(key.contentEquals("foo"));
            Item value = item.getItemByKey(key);
            Assertions.assertTrue(value.isInteger());
            Assertions.assertTrue(value.getIntValue() == i);
        }
        iterator.close();
        Assertions.assertTrue(!iterator.isOpen());
    }

    @Test
    @Timeout(1000)
    public void testRDD() throws Throwable {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        SequenceOfItems iterator = rumble.runQuery("for $i in parallelize(1 to 5) return { \"foo\" : $i }");
        Assertions.assertTrue(!iterator.isOpen());
        Assertions.assertTrue(iterator.availableAsRDD());
        JavaRDD<Item> items = iterator.getAsRDD();
        List<Item> list = items.collect();
        for (int i = 1; i <= 5; ++i) {
            Item item = list.get(i - 1);
            Assertions.assertTrue(item.isObject());
            List<String> keys = item.getStringKeys();
            Assertions.assertTrue(keys.size() == 1);
            String key = keys.get(0);
            Assertions.assertTrue(key.contentEquals("foo"));
            Item value = item.getItemByKey(key);
            Assertions.assertTrue(value.isInteger());
            Assertions.assertTrue(value.getIntValue() == i);
        }
    }

    @Test
    @Timeout(1000)
    public void testGetAsDataFramePreservesArrayMembers() throws Throwable {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        SequenceOfItems iterator = rumble.runQuery("({ \"arr\" : [ { \"x\" : 1 }]},{\"arr\":[{\"x\":\"s\"}]})");

        Dataset<Row> dataFrame = iterator.getAsDataFrame();
        List<Row> rows = dataFrame.collectAsList();

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(1, rows.get(0).getList(0).size());
        Assertions.assertEquals(1, rows.get(1).getList(0).size());

        JavaRDD<Item> itemRDD = dataFrame.javaRDD()
            .map(
                new RowToItemMapper(
                        org.rumbledb.exceptions.ExceptionMetadata.EMPTY_METADATA,
                        ItemTypeFactory.createItemType(dataFrame.schema())
                )
            );
        List<Item> items = itemRDD.collect();

        Assertions.assertEquals("1", items.get(0).getItemByKey("arr").getItemAt(0).getItemByKey("x").getStringValue());
        Assertions.assertEquals("s", items.get(1).getItemByKey("arr").getItemAt(0).getItemByKey("x").getStringValue());
    }

    @Test
    @Timeout(1000)
    public void testJsonSerializationEscapesUnencodableSupplementaryCharacterForAscii() {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        String supplementaryCharacter = new String(Character.toChars(0x10330));
        String query = String.join(
            "\n",
            "declare namespace output = \"http://www.w3.org/2010/xslt-xquery-serialization\";",
            "declare option output:method \"json\";",
            "declare option output:encoding \"US-ASCII\";",
            "\"" + supplementaryCharacter + "\""
        );

        Assertions.assertEquals("\"\\uD800\\uDF30\"", rumble.runQuery(query).serialize());
    }

    @Test
    @Timeout(1000)
    public void testJsonSerializationEscapesEuroCharacterForAscii() {
        Rumble rumble = new Rumble(RumbleRuntimeConfiguration.getDefaultConfiguration());
        String query = String.join(
            "\n",
            "declare namespace output = \"http://www.w3.org/2010/xslt-xquery-serialization\";",
            "declare option output:method \"json\";",
            "declare option output:encoding \"US-ASCII\";",
            "\"€\""
        );

        Assertions.assertEquals("\"\\u20AC\"", rumble.runQuery(query).serialize());
    }

}
