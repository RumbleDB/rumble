package org.rumbledb.api;

import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.net.URI;

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

    private RumbleRuntimeConfiguration configuration;

    /**
     * Creates a new Rumble instance. This does NOT initialize Spark. You need to do so before instantiating Rumble.
     *
     * @param configuration a RumbleRuntimeConfiguration object containing the configuration.
     */
    public Rumble(RumbleRuntimeConfiguration configuration) {
        this.configuration = configuration;
        SparkSessionManager.COLLECT_ITEM_LIMIT = this.configuration.getResultSizeCap();
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param query the content of the JSONiq main module.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(String query) {
        MainModule mainModule = VisitorHelpers.parseMainModuleFromQuery(
            query,
            this.configuration
        );
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(
            mainModule,
            this.configuration
        );

        return new SequenceOfItems(iterator, dynamicContext, this.configuration);
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param location the JSONiq main module location.
     * @throws java.io.IOException if there was an issue reading a module.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(URI location) throws IOException {
        MainModule mainModule = VisitorHelpers.parseMainModuleFromLocation(
            location,
            this.configuration
        );
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(
            mainModule,
            this.configuration
        );

        return new SequenceOfItems(iterator, dynamicContext, this.configuration);
    }

    /**
     * Creates JSONiq Expression Tree from a query and returns serialization of the Tree.
     *
     * @param query the content of the JSONiq or XQuery main module.
     * @return serialization of the JSONiq Expression Tree.
     */
    public String serializeToJSONiq(String query) {
        MainModule mainModule = VisitorHelpers.parseMainModuleFromQuery(
            query,
            this.configuration
        );
        StringBuffer stringBuffer = new StringBuffer();
        mainModule.serializeToJSONiq(stringBuffer, 0);
        return stringBuffer.toString();
    }
}
