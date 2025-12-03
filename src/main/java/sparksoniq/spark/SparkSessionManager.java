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

import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.parquet.format.IntType;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.BooleanType;
import org.apache.spark.sql.types.DoubleType;
import org.apache.spark.sql.types.FloatType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.AnnotatedItem;
import org.rumbledb.items.AnyURIItem;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.Base64BinaryItem;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.items.DateItem;
import org.rumbledb.items.DateTimeItem;
import org.rumbledb.items.DateTimeStampItem;
import org.rumbledb.items.DayTimeDurationItem;
import org.rumbledb.items.DecimalItem;
import org.rumbledb.items.DoubleItem;
import org.rumbledb.items.DurationItem;
import org.rumbledb.items.FloatItem;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.HexBinaryItem;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.IntegerItem;
import org.rumbledb.items.NullItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.items.TimeItem;
import org.rumbledb.items.YearMonthDurationItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;

public class SparkSessionManager {

    private static final String APP_NAME = "Rumble application";
    private static SparkSessionManager instance;
    private static Level LOG_LEVEL = Level.FATAL;
    private SparkConf configuration;
    private SparkSession session;
    private JavaSparkContext javaSparkContext;

    public static String atomicJSONiqItemColumnName = "atomic0d08af5d-10bb-4a73-af84-c6aac917a830";
    public static String emptyObjectJSONiqItemColumnName = "emptyobja84bc646-05af-4383-8853-2e9f31a710f2";
    public static String temporaryColumnName = "tmp0f7b4040-b404-4239-99dd-9b4cf2900594";
    public static String countColumnName = "count5af0c0c8-e84c-482a-82ce-1887565cf448";
    public static String rightHandSideHashColumnName = "rhsdb273b7d-d927-4c0d-b9c1-665af71faa2b";
    public static String leftHandSideHashColumnName = "lhs171bdb70-7400-48ed-a105-d132f4e38a2d";
    public static String sparkSqlVariableName = "sparksql73706172-6b73-716c-7661-726961626c65";
    public static String sequenceColumnName = "sequence56415249-4142-4c45-5345-5155454e4345";

    // Temporary column names for insert operations
    public static String tempMaxRowIdColumnName = "maxRowId_2d8f6a4c0b8a4dd88c782b64e1b93a77";
    public static String tempMinRowOrderColumnName = "minRowOrder_9f7d9d6a5ee14c8b9d1074f4799c5d30";
    public static String tempMaxRowOrderColumnName = "maxRowOrder_3e8b7a4c1d2e4f6b8c9d0a1b2c3d4e5f";
    public static String tempRowNumColumnName = "rowNum_f0a3c5e92a214f0c8cd50d6de3c4f9d9";
    public static String tempRowNumSeqColumnName = "rowNumSeq_d2f8b4a18e1f4fdb9c54cc5e4a3c23a6";
    public static String tempRowNumOrderColumnName = "rowNumOrder_b7c6a1f4ff4b4d099ad01f2b76c9a8e1";

    // Special private column names
    public static String mutabilityLevelColumnName = "__mutabilityLevel";
    public static String rowIdColumnName = "__rowID";
    public static String pathInColumnName = "__pathIn";
    public static String tableLocationColumnName = "__tableLocation";
    public static String rowOrderColumnName = "__rowOrder";

    private SparkSessionManager() {
    }

    private SparkSessionManager(SparkConf conf) {
        if (this.configuration == null) {
            this.configuration = conf;
        } else {
            throw new OurBadException("Configuration already exists: new configuration initialization prevented.");
        }
    }

    private SparkSessionManager(SparkSession session) {
        if (this.session == null) {
            this.session = session;
            this.javaSparkContext = JavaSparkContext.fromSparkContext(session.sparkContext());
            if (this.configuration == null) {
                setDefaultConfiguration();
            }
            initializeKryoSerialization();
            Configurator.setLevel("org", LOG_LEVEL);
            Configurator.setLevel("akka", LOG_LEVEL);
        } else {
            throw new OurBadException("Session already exists: new session initialization prevented.");
        }
    }

    public static SparkSessionManager getInstance() {
        if (instance == null) {
            instance = new SparkSessionManager();
        }
        return instance;
    }

    public static SparkSessionManager getInstance(SparkConf conf) {
        if (instance == null) {
            instance = new SparkSessionManager(conf);
        }
        return instance;
    }

    public static SparkSessionManager getInstance(SparkSession session) {
        if (instance == null) {
            instance = new SparkSessionManager(session);
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
            this.configuration = new SparkConf();
            if (this.configuration.get("spark.app.name", "<none>").equals("<none")) {
                LogManager.getLogger("SparkSessionManager")
                    .warn(
                        "No app name specified (you can do so with --conf spark.app.name=your_name). Setting to "
                            + APP_NAME
                    );
                this.configuration.setAppName(APP_NAME);
            }
            this.configuration.set("spark.mongodb.read.connection.uri", "mongodb://127.0.0.1/test.myCollection");
            this.configuration.set("spark.sql.crossJoin.enabled", "true"); // enables cartesian product
            this.configuration.set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension");
            this.configuration.set(
                "spark.sql.catalog.spark_catalog",
                "org.apache.spark.sql.delta.catalog.DeltaCatalog"
            );
            if (!this.configuration.contains("spark.master")) {
                this.configuration.set("spark.master", "local[*]");
            }
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException(
                    "It seems your query needs Spark, but it is not available. You need to use spark-submit in an environment in which Spark is configured."
            );
        }
    }

    public void resetSession() {
        if (this.session != null) {
            this.session.stop();
            this.session = null;
        }
        this.javaSparkContext = null;
        this.configuration = null;
    }

    private void initializeSession() {
        if (this.session == null) {
            initializeKryoSerialization();
            Configurator.setLevel("org", LOG_LEVEL);
            Configurator.setLevel("akka", LOG_LEVEL);

            this.session = SparkSession.builder().config(this.configuration).enableHiveSupport().getOrCreate();
        } else {
            throw new OurBadException("Session already exists: new session initialization prevented.");
        }
    }

    private void initializeKryoSerialization() {
        if (!this.configuration.contains("spark.serializer")) {
            this.configuration.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
            // this.configuration.set("spark.kryo.registrationRequired", "true");
            Class<?>[] serializedClasses = new Class[] {
                Item.class,
                AnnotatedItem.class,
                ArrayItem.class,
                ObjectItem.class,
                AnyURIItem.class,
                Base64BinaryItem.class,
                BooleanItem.class,
                DateItem.class,
                DateTimeItem.class,
                DateTimeStampItem.class,
                DayTimeDurationItem.class,
                DecimalItem.class,
                DoubleItem.class,
                DurationItem.class,
                FloatItem.class,
                HexBinaryItem.class,
                IntegerItem.class,
                IntItem.class,
                NullItem.class,
                StringItem.class,
                TimeItem.class,
                YearMonthDurationItem.class,
                FunctionItem.class,
                FunctionIdentifier.class,
                Name.class,
                SequenceType.class,
                SequenceType.Arity.class,
                ItemType.class,
                DynamicContext.class,
                FlworTuple.class,
                FlworKey.class,
                RuntimeIterator.class,
                RuntimeTupleIterator.class,
                StructType.class,
                StructType[].class,
                StructField.class,
                StructField[].class,
                BooleanType.class,
                DoubleType.class,
                FloatType.class,
                IntType.class,
                BooleanType.class,
                BooleanType.class,
                BooleanType.class,
                BooleanType.class,
            };

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

}
