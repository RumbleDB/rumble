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
package sparksoniq.spark;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;

public class SparkContextManager {

    public static int COLLECT_ITEM_LIMIT = 0;
    private static Level LOG_LEVEL = Level.FATAL;
    private static final String APP_NAME = "jsoniq-on-spark";

    public static boolean LIMIT_COLLECT() {
        return COLLECT_ITEM_LIMIT > 0;
    }

    public static SparkContextManager getInstance() {

        if (_instance == null)
            _instance = new SparkContextManager();
        return _instance;
    }

    private static SparkContextManager _instance;

    private SparkContextManager() {
    }

    public void initializeConfigurationAndContext() {
        configuration = new SparkConf().setAppName(APP_NAME);
        initialize();
    }

    public void initializeConfigurationAndContext(SparkConf conf, boolean setAppName) {
        if (setAppName)
            conf.setAppName(APP_NAME);
        configuration = conf;
        initialize();
    }

    public JavaSparkContext getContext() {
        if (context == null) {
            if (this.configuration == null)
                initializeConfigurationAndContext();
            initialize();
        }
        return context;
    }

    private void initialize() {
        initializeKryoSerialization();
        Logger.getLogger("org").setLevel(LOG_LEVEL);
        Logger.getLogger("akka").setLevel(LOG_LEVEL);
        if (context == null)
            context = new JavaSparkContext(this.configuration);
    }

    private void initializeKryoSerialization() {
        configuration.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        Class[] serializedClasses = new Class[]{Item.class, ArrayItem.class, ObjectItem.class,
                StringItem.class, IntegerItem.class, DoubleItem.class, DecimalItem.class, NullItem.class,
                BooleanItem.class, DynamicContext.class, FlworTuple.class, FlworKey.class,
                FlowrClauseSparkIterator.class, RuntimeIterator.class};
        configuration.registerKryoClasses(serializedClasses);
    }


    private SparkConf configuration;
    private JavaSparkContext context;
}
