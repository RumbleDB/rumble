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

import sparksoniq.Main;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class SparkSessionManager {

    private static final String APP_NAME = "jsoniq-on-spark";
    public static int COLLECT_ITEM_LIMIT = 0;
    private static SparkSessionManager _instance;
    private static Level LOG_LEVEL = Level.FATAL;
    private SparkConf configuration;
    private SparkSession session;
    private JavaSparkContext javaSparkContext;


    private SparkSessionManager() {
    }

    public static boolean LIMIT_COLLECT() {
        return COLLECT_ITEM_LIMIT > 0;
    }

    public static SparkSessionManager getInstance() {
        if (_instance == null)
            _instance = new SparkSessionManager();
        return _instance;
    }

    public SparkSession getOrCreateSession() {
        if (session == null) {
            if (this.configuration == null) {
                setDefaultConfiguration();
            }
            initialize();
        }
        return session;
    }

    private void setDefaultConfiguration() {
        configuration = new SparkConf().setAppName(APP_NAME);
    }

    private void initialize() {
        if (session == null) {
            initializeKryoSerialization();
            Logger.getLogger("org").setLevel(LOG_LEVEL);
            Logger.getLogger("akka").setLevel(LOG_LEVEL);

            session = SparkSession.builder().config(this.configuration).getOrCreate();
        } else {
            throw new SparksoniqRuntimeException("Session already exists: new session initialization prevented.");
        }
    }

    private void initializeKryoSerialization() {
        configuration.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
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

        configuration.registerKryoClasses(serializedClasses);
    }


    public void initializeConfigurationAndSession() {
        setDefaultConfiguration();
        initialize();
    }

    public void initializeConfigurationAndSession(SparkConf conf, boolean setAppName) {
        if (setAppName)
            conf.setAppName(APP_NAME);
        this.configuration = conf;
        initializeKryoSerialization();
        initialize();
    }

    public JavaSparkContext getJavaSparkContext() {
        if (javaSparkContext == null) {
            javaSparkContext = JavaSparkContext.fromSparkContext(this.getOrCreateSession().sparkContext());
        }
        return javaSparkContext;
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
