package org.rumbledb.api;

import java.net.URI;
import java.io.IOException;

import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

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

    private RumbleRuntimeConfiguration configuration;
    private DynamicContext dynamicContext;
    private StaticContext staticContext;

    /**
     * Creates a new Rumble instance. This does NOT initialize Spark. You need to do so before instantiating Rumble.
     *
     * @param configuration a RumbleRuntimeConfiguration object containing the configuration.
     */
    public Rumble(RumbleRuntimeConfiguration configuration) {
        setConfiguration(configuration);
        this.dynamicContext = null;
    }

    /**
     * Changes the configuration;
     *
     * @param configuration a RumbleRuntimeConfiguration object containing the configuration.
     */
    public void setConfiguration(RumbleRuntimeConfiguration configuration) {
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
        if(this.configuration.getResetDynamicContextForEachQuery() || this.staticContext == null)
        {
        	URI location = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                    ".",
                    this.configuration,
                    ExceptionMetadata.EMPTY_METADATA
                );
        	this.staticContext = new StaticContext(location, this.configuration);
        }
        MainModule mainModule = VisitorHelpers.parseMainModuleFromQuery(
            query,
            this.staticContext,
            this.configuration
        );
        if(this.configuration.getResetDynamicContextForEachQuery() || this.dynamicContext == null)
        {
        	this.dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        }
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(
            mainModule,
            this.configuration
        );
        return new SequenceOfItems(iterator, this.dynamicContext, this.configuration);
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param location the JSONiq main module location.
     * @throws java.io.IOException if there was an issue reading a module.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(URI location) throws IOException {
        if(this.configuration.getResetDynamicContextForEachQuery() || this.staticContext == null)
        {
        	this.staticContext = new StaticContext(location, this.configuration);
        }
        MainModule mainModule = VisitorHelpers.parseMainModuleFromLocation(
            location,
            this.staticContext,
            this.configuration
        );
        if(this.configuration.getResetDynamicContextForEachQuery() || this.dynamicContext == null)
        {
        	this.dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        }
        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(
            mainModule,
            this.configuration
        );
        return new SequenceOfItems(iterator, this.dynamicContext, this.configuration);
    }

    /**
     * Creates JSONiq Expression Tree from a query and returns serialization of the Tree.
     *
     * @param query the content of the JSONiq or XQuery main module.
     * @return serialization of the JSONiq Expression Tree.
     */
    public String serializeToJSONiq(String query) {
    	if(this.configuration.getResetDynamicContextForEachQuery() || this.staticContext == null)
        {
    		URI location = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                    ".",
                    this.configuration,
                    ExceptionMetadata.EMPTY_METADATA
                );
    		this.staticContext = new StaticContext(location, this.configuration);
        }
        MainModule mainModule = VisitorHelpers.parseMainModuleFromQuery(
            query,
            this.staticContext,
            this.configuration
        );
        StringBuffer stringBuffer = new StringBuffer();
        mainModule.serializeToJSONiq(stringBuffer, 0);
        return stringBuffer.toString();
    }
}
