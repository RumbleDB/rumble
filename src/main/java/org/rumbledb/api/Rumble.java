package org.rumbledb.api;

import org.apache.spark.sql.SparkSession;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

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

    private RumbleRuntimeConfiguration configuration;
    private CompilationConfiguration compilationConfiguration;

    /**
     * Creates a new Rumble instance. This initializes a brand new Spark session.
     *
     * @param configuration a RumbleRuntimeConfiguration object containing the configuration.
     */
    public Rumble(RumbleRuntimeConfiguration configuration) {
        this(new CompilationConfiguration(configuration));
    }

    /**
     * Creates a new Rumble instance with explicit compilation configuration.
     *
     * @param compilationConfiguration the configuration used to compile queries.
     */
    public Rumble(CompilationConfiguration compilationConfiguration) {
        this.compilationConfiguration = compilationConfiguration;
        this.configuration = compilationConfiguration.runtimeConfiguration();
        SparkSessionManager.getInstance().getOrCreateSession();
    }

    /**
     * Creates a new Rumble instance. It uses the supplied spark session.
     *
     */
    public Rumble(SparkSession session) {
        this.configuration = new RumbleRuntimeConfiguration();
        this.compilationConfiguration = new CompilationConfiguration(this.configuration);
        SparkSessionManager.getInstance(session);
    }

    /**
     * Gets the configuration
     * 
     * @return the configuration
     */
    public RumbleRuntimeConfiguration getConfiguration() {
        return this.configuration;
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
            this.compilationConfiguration
        );
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(
            mainModule,
            this.configuration
        );

        return new SequenceOfItems(iterator, dynamicContext, this.configuration);
    }

    /**
     * Runs a query and returns its serialized result using the method and options declared in the
     * static context.
     *
     * @param query the content of the JSONiq main module.
     * @return the serialized query result.
     */
    public String runQueryToString(String query) {
        return runQuery(query).serialize();
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
            this.compilationConfiguration
        );
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(
            mainModule,
            this.configuration
        );

        return new SequenceOfItems(iterator, dynamicContext, this.configuration);
    }

    /**
     * Runs a query from a location and returns its serialized result using the method and options
     * declared in the static context.
     *
     * @param location the JSONiq main module location.
     * @throws java.io.IOException if there was an issue reading a module.
     * @return the serialized query result.
     */
    public String runQueryToString(URI location) throws IOException {
        return runQuery(location).serialize();
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
            this.compilationConfiguration
        );
        StringBuilder sb = new StringBuilder();
        mainModule.serializeToJSONiq(sb, 0);
        return sb.toString();
    }
}
