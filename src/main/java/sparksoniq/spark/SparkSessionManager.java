package sparksoniq.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;

public class SparkSessionManager {

    private static SparkSessionManager _instance;
    private SparkConf configuration;
    private SparkSession session;
    private JavaSparkContext javaSparkContext;

    public static int COLLECT_ITEM_LIMIT = 0;
    private static Level LOG_LEVEL = Level.FATAL;
    private static final String APP_NAME = "jsoniq-on-spark";


    public static boolean LIMIT_COLLECT() {
        return COLLECT_ITEM_LIMIT > 0;
    }

    public static SparkSessionManager getInstance() {
        if (_instance == null)
            _instance = new SparkSessionManager();
        return _instance;
    }

    private SparkSessionManager() {
    }

    public SparkSession getSession() {
        if (session == null) {
            if (this.configuration == null) {
                setDefaultConfiguration();
            }
            initialize();
        }
        return session;
    }

    private void setDefaultConfiguration () {
        configuration = new SparkConf().setAppName(APP_NAME);
    }

    private void initialize() {
        initializeKryoSerialization();
        Logger.getLogger("org").setLevel(LOG_LEVEL);
        Logger.getLogger("akka").setLevel(LOG_LEVEL);
        if (session == null)
            session= SparkSession.builder().config(this.configuration).getOrCreate();
    }

    private void initializeKryoSerialization() {
        configuration.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        Class[] serializedClasses = new Class[]{Item.class, ArrayItem.class, ObjectItem.class,
                StringItem.class, IntegerItem.class, DoubleItem.class, DecimalItem.class, NullItem.class,
                BooleanItem.class, DynamicContext.class, FlworTuple.class, FlworKey.class,
                SparkRuntimeTupleIterator.class, RuntimeIterator.class, RuntimeTupleIterator.class};
        configuration.registerKryoClasses(serializedClasses);
    }


    public void initializeConfigurationAndSession() {
        setDefaultConfiguration();
        initialize();
    }

    public void initializeConfigurationAndSession(SparkConf conf, boolean setAppName) {
        if (setAppName)
            conf.setAppName(APP_NAME);
        configuration = conf;
        initialize();
    }


    public JavaSparkContext getJavaSparkContext() {
        if (javaSparkContext == null) {
            javaSparkContext = JavaSparkContext.fromSparkContext(this.getSession().sparkContext());
        }
        return javaSparkContext;
    }

}
