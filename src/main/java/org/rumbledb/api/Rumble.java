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
    private final org.rumbledb.config.RumbleConfiguration configuration;

    /**
     * Creates a new Rumble instance. This initializes a brand new Spark session.
     *
     * @param configuration a RumbleConfiguration object containing the configuration.
     */
    public Rumble(RumbleConfiguration configuration) {
        this.configuration = configuration.getInternalConfiguration();
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
        this.configuration = new RumbleConfiguration().getInternalConfiguration();
        SparkSessionManager.getInstance(session);
    }

    /**
     * Gets the configuration
     * 
     * @return the configuration
     */
    public RumbleConfiguration getConfiguration() {
        return new RumbleConfiguration(this.configuration);
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param query the content of the JSONiq main module.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(String query) {
        return runQuery(query, ExternalBindings.empty());
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param query the content of the JSONiq main module.
     * @param bindings the external bindings to apply for this execution.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(String query, ExternalBindings bindings) {
        return runQuery(query, bindings.getInternalBindings());
    }

    /**
     * Internal entry point used by command-line integrations whose bindings may include lexical and file sources.
     *
     * @param query the content of the JSONiq main module
     * @param bindings the internal external bindings to apply
     * @return the resulting sequence
     */
    public SequenceOfItems runQuery(String query, org.rumbledb.bindings.ExternalBindings bindings) {
        MainModule mainModule = VisitorHelpers.parseMainModuleFromQuery(
            query,
            configuration,
            bindings
        );
        return createSequence(mainModule, bindings);
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param location the JSONiq main module location.
     * @throws java.io.IOException if there was an issue reading a module.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(URI location) throws IOException {
        return runQuery(location, ExternalBindings.empty());
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
        return runQuery(location, bindings.getInternalBindings());
    }

    /**
     * Internal entry point used by command-line integrations whose bindings may include lexical and file sources.
     *
     * @param location the JSONiq main module location
     * @param bindings the internal external bindings to apply
     * @return the resulting sequence
     * @throws IOException if the module cannot be read
     */
    public SequenceOfItems runQuery(URI location, org.rumbledb.bindings.ExternalBindings bindings) throws IOException {
        MainModule mainModule = VisitorHelpers.parseMainModuleFromLocation(
            location,
            configuration,
            bindings
        );
        return createSequence(mainModule, bindings);
    }

    private SequenceOfItems createSequence(
            MainModule mainModule,
            org.rumbledb.bindings.ExternalBindings bindings
    ) {
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(
            mainModule,
            this.configuration,
            bindings
        );
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
            configuration,
            ExternalBindings.empty().getInternalBindings()
        );
        StringBuilder sb = new StringBuilder();
        mainModule.serializeToJSONiq(sb, 0);
        return sb.toString();
    }
}
