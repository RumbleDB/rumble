package org.rumbledb.api;

import org.apache.spark.sql.SparkSession;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.spark.SparkSessionManager;

import java.io.IOException;
import java.net.URI;

/**
 * The entry point for Java applications that want to execute JSONiq queries with Rumble.
 *
 * The query must be provided as a string and a sequence of items is returned.
 *
 * It is possible for the queries to use the text-file() and json-lines() functions if Spark and either the local file
 * system or HDFS are properly configured.
 *
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 */
public class Rumble {

    private final RumbleConfiguration configuration;

    /**
     * Creates a new Rumble instance. This initializes a brand new Spark session.
     *
     * @param configuration a RumbleConfiguration object containing the configuration.
     */
    public Rumble(RumbleConfiguration configuration) {
        this.configuration = configuration;
        SparkSessionManager.getInstance().getOrCreateSession();
    }

    /**
     * Creates a new Rumble instance from internal configuration.
     * This should only be used for internal purposes.
     * 
     * @param configuration
     */
    public Rumble(org.rumbledb.config.RumbleConfiguration configuration) {
        this(new RumbleConfiguration(configuration));
    }

    /**
     * Creates a new Rumble instance. It uses the supplied spark session.
     *
     */
    public Rumble(SparkSession session) {
        this.configuration = new RumbleConfiguration();
        SparkSessionManager.getInstance(session);
    }

    /**
     * Gets the configuration
     * 
     * @return the configuration
     */
    public RumbleConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param query the content of the JSONiq main module.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(String query) {
        return runQuery(query, new ExternalBindings());
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param query the content of the JSONiq main module.
     * @param bindings the external bindings to apply for this execution.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(String query, ExternalBindings bindings) {
        MainModule mainModule = VisitorHelpers.parseMainModuleFromQuery(
            query,
            configuration.getInternalConfiguration()
        );
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(
            mainModule,
            configuration.getInternalConfiguration()
        );
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(
            mainModule,
            configuration.getInternalConfiguration()
        );

        return new SequenceOfItems(iterator, dynamicContext, configuration);
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param location the JSONiq main module location.
     * @throws java.io.IOException if there was an issue reading a module.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(URI location) throws IOException {
        return runQuery(location, new ExternalBindings());
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param location the JSONiq main module location.
     * @param bindings the external bindings to apply for this execution.
     * @throws java.io.IOException if there was an issue reading a module.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(URI location, ExternalBindings bindings) throws IOException {
        MainModule mainModule = VisitorHelpers.parseMainModuleFromLocation(
            location,
            configuration.getInternalConfiguration()
        );
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(
            mainModule,
            configuration.getInternalConfiguration()
        );
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(
            mainModule,
            configuration.getInternalConfiguration()
        );

        return new SequenceOfItems(iterator, dynamicContext, configuration);
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
            configuration.getInternalConfiguration()
        );
        StringBuilder sb = new StringBuilder();
        mainModule.serializeToJSONiq(sb, 0);
        return sb.toString();
    }
}
