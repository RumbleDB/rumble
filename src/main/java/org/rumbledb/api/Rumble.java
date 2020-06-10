package org.rumbledb.api;

import org.antlr.v4.runtime.CharStreams;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

/**
 * The entry point for Java applications that want to execute JSONiq queries with Rumble.
 *
 * The query must be provided as a string and a sequence of items is returned.
 *
 * It is possible for the queries to use the text-file() and json-file() functions if Spark and either the local file
 * system or HDFS are properly configured.
 *
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 */
public class Rumble {

    private RumbleConf conf;

    /**
     * Creates a new Rumble instance. This does NOT initialize Spark. You need to do so before instantiating Rumble.
     *
     * @param conf a RumbleConf object containing the configuration.
     */
    public Rumble(RumbleConf conf) {
        this.conf = conf;
        SparkSessionManager.COLLECT_ITEM_LIMIT = this.conf.getResultsSizeCap();
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param query the JSONiq query.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(String query) {
        MainModule mainModule = VisitorHelpers.parseMainModule(CharStreams.fromString(query), RumbleRuntimeConfiguration.getDefaultConfiguration());
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(mainModule);
        return new SequenceOfItems(iterator);
    }
}
