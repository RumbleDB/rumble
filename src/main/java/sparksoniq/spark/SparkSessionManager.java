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
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotMaterializeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.items.DecimalItem;
import org.rumbledb.items.DoubleItem;
import org.rumbledb.items.FloatItem;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.IntegerItem;
import org.rumbledb.items.NullItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.List;
import java.util.stream.Collectors;

public class SparkSessionManager {

    private static final String APP_NAME = "jsoniq-on-spark";
    public static int COLLECT_ITEM_LIMIT = 0;
    private static SparkSessionManager instance;
    private static Level LOG_LEVEL = Level.FATAL;
    private SparkConf configuration;
    private SparkSession session;
    private JavaSparkContext javaSparkContext;

    public static String atomicJSONiqItemColumnName = "0d08af5d-10bb-4a73-af84-c6aac917a830";
    public static String emptyObjectJSONiqItemColumnName = "a84bc646-05af-4383-8853-2e9f31a710f2";
    public static String temporaryColumnName = "0f7b4040-b404-4239-99dd-9b4cf2900594";
    public static String expressionHashColumnName = "db273b7d-d927-4c0d-b9c1-665af71faa2b ";
    public static String inputTupleHashColumnName = "171bdb70-7400-48ed-a105-d132f4e38a2d";

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
        if (this.configuration == null) {
            setDefaultConfiguration();
        }
        if (this.session == null) {
            initializeSession();
        }
        return this.session;
    }

    private void setDefaultConfiguration() {
        try {
            this.configuration = new SparkConf()
                .setAppName(APP_NAME)
                .set("spark.sql.crossJoin.enabled", "true"); // enables cartesian product
            if (!this.configuration.contains("spark.master")) {
                this.configuration.set("spark.master", "local[*]");
            }
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException(
                    "It seems your query needs Spark, but it is not available. You need to use spark-submit in an environment in which Spark is configured."
            );
        }
    }

    private void initializeSession() {
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
                IntItem.class,
                IntegerItem.class,
                FloatItem.class,
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


    public void initializeConfigurationAndSession(SparkConf conf, boolean setAppName) {
        if (setAppName) {
            conf.setAppName(APP_NAME);
        }
        this.configuration = conf;
        initializeSession();
    }

    public JavaSparkContext getJavaSparkContext() {
        if (this.configuration == null) {
            setDefaultConfiguration();
        }
        if (this.session == null) {
            initializeSession();
        }
        if (this.javaSparkContext == null) {
            this.javaSparkContext = JavaSparkContext.fromSparkContext(this.getOrCreateSession().sparkContext());
        }
        return this.javaSparkContext;
    }

    public static <T> List<T> collectRDDwithLimit(JavaRDD<T> rdd, ExceptionMetadata metadata) {
        if (SparkSessionManager.LIMIT_COLLECT()) {
            List<T> result = rdd.take(SparkSessionManager.COLLECT_ITEM_LIMIT + 1);
            if (result.size() == SparkSessionManager.COLLECT_ITEM_LIMIT + 1) {
                long count = rdd.count();
                throw new CannotMaterializeException(
                        "Cannot materialize a sequence of "
                            + count
                            + " items because the limit is set to "
                            + SparkSessionManager.COLLECT_ITEM_LIMIT
                            + ". This value can be configured with the --result-size parameter at startup",
                        metadata
                );
            }
            return result;
        } else {
            return rdd.collect();
        }
    }

    public static <T> long collectRDDwithLimitWarningOnly(JavaRDD<T> rdd, List<T> outputList) {
        outputList.clear();
        long count = -1;
        if (SparkSessionManager.LIMIT_COLLECT()) {
            List<T> result = rdd.take(SparkSessionManager.COLLECT_ITEM_LIMIT + 1);
            if (result.size() == SparkSessionManager.COLLECT_ITEM_LIMIT + 1) {
                count = rdd.count();
            }
            result.stream()
                .limit(SparkSessionManager.COLLECT_ITEM_LIMIT)
                .collect(Collectors.toCollection(() -> outputList));
            return count;
        } else {
            outputList.addAll(rdd.collect());
            return count;
        }
    }
}
