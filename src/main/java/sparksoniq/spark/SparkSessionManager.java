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

package sparksoniq.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.api.Item;
import org.rumbledb.cli.Main;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.items.DecimalItem;
import org.rumbledb.items.DoubleItem;
import org.rumbledb.items.IntegerItem;
import org.rumbledb.items.NullItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.List;

public class SparkSessionManager {

    private static final String APP_NAME = "jsoniq-on-spark";
    public static int COLLECT_ITEM_LIMIT = 0;
    private static SparkSessionManager instance;
    private static Level LOG_LEVEL = Level.FATAL;
    private SparkConf configuration;
    private SparkSession session;
    private JavaSparkContext javaSparkContext;

    public static String atomicJSONiqItemColumnName = "0d08af5d-10bb-4a73-af84-c6aac917a830";
    public static String temporaryColumnName = "0f7b4040-b404-4239-99dd-9b4cf2900594";

    private SparkSessionManager() {
    }

    public static boolean LIMIT_COLLECT() {
        return COLLECT_ITEM_LIMIT > 0;
    }

    public static SparkSessionManager getInstance() {
        if (instance == null) {
            instance = new SparkSessionManager();
        }
        return instance;
    }

    public SparkSession getOrCreateSession() {
        if (this.session == null) {
            if (this.configuration == null) {
                setDefaultConfiguration();
            }
            initialize();
        }
        return this.session;
    }

    private void setDefaultConfiguration() {
        this.configuration = new SparkConf()
            .setAppName(APP_NAME)
            .set("spark.sql.crossJoin.enabled", "true"); // enables cartesian product
    }

    private void initialize() {
        if (this.session == null) {
            initializeKryoSerialization();
            Logger.getLogger("org").setLevel(LOG_LEVEL);
            Logger.getLogger("akka").setLevel(LOG_LEVEL);

            this.session = SparkSession.builder().config(this.configuration).getOrCreate();
        } else {
            throw new OurBadException("Session already exists: new session initialization prevented.");
        }
    }

    private void initializeKryoSerialization() {
        if (!this.configuration.contains("spark.serializer")) {
            this.configuration.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
            Class<?>[] serializedClasses = new Class[] {
                Item.class,
                ArrayItem.class,
                ObjectItem.class,
                StringItem.class,
                IntegerItem.class,
                DoubleItem.class,
                DecimalItem.class,
                NullItem.class,
                BooleanItem.class,
                DynamicContext.class,
                FlworTuple.class,
                FlworKey.class,
                RuntimeIterator.class,
                RuntimeTupleIterator.class };

            this.configuration.registerKryoClasses(serializedClasses);
        }
    }


    public void initializeConfigurationAndSession() {
        setDefaultConfiguration();
        initialize();
    }

    public void initializeConfigurationAndSession(SparkConf conf, boolean setAppName) {
        if (setAppName) {
            conf.setAppName(APP_NAME);
        }
        this.configuration = conf;
        initializeKryoSerialization();
        initialize();
    }

    public JavaSparkContext getJavaSparkContext() {
        if (this.javaSparkContext == null) {
            this.javaSparkContext = JavaSparkContext.fromSparkContext(this.getOrCreateSession().sparkContext());
        }
        return this.javaSparkContext;
    }

    public static <T> List<T> collectRDDwithLimit(JavaRDD<T> rdd) {
        String truncationMessage = "Results have been truncated to:"
            + SparkSessionManager.COLLECT_ITEM_LIMIT
            + " items. This value can be configured with the --result-size parameter at startup.\n";
        return collectRDDwithLimit(rdd, truncationMessage);
    }

    public static <T> List<T> collectRDDwithLimit(JavaRDD<T> rdd, String customTruncationMessage) {
        if (SparkSessionManager.LIMIT_COLLECT()) {
            List<T> result = rdd.take(SparkSessionManager.COLLECT_ITEM_LIMIT);
            if (result.size() == SparkSessionManager.COLLECT_ITEM_LIMIT) {
                Main.printMessageToLog(customTruncationMessage);
            }
            return result;
        } else {
            return rdd.collect();
        }
    }
}
