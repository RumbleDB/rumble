package org.rumbledb.context;

import java.io.Serializable;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.types.SequenceType;

public class RuntimeStaticContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private RumbleRuntimeConfiguration configuration;
    private SequenceType staticType;
    private ExecutionMode executionMode;
    private ExceptionMetadata metadata;
    private final SerializationParameters serializationParameters;

    /**
     * Creates a new runtime static context from a static context.
     * 
     * @param staticContext the static context.
     * @param staticType the static type of the runtime static context.
     * @param executionMode the execution mode of the runtime static context.
     * @param metadata the metadata of the runtime static context.
     */
    public RuntimeStaticContext(
        StaticContext staticContext,
        SequenceType staticType,
        ExecutionMode executionMode,
        ExceptionMetadata metadata
    ) {
        this(staticContext.getRumbleConfiguration(), staticType, executionMode, metadata, staticContext.getSerializationParameters());
    }

    /**
     * Creates a new runtime static context from an existing runtime static context.
     * 
     * @param staticContext the existing runtime static context.
     * @param staticType the static type of the runtime static context.
     * @param executionMode the execution mode of the runtime static context.
     * @param metadata the metadata of the runtime static context.
     */
    public RuntimeStaticContext(
        RuntimeStaticContext staticContext,
        SequenceType staticType,
        ExecutionMode executionMode,
        ExceptionMetadata metadata
    ) {
        this(staticContext.configuration, staticType, executionMode, metadata, staticContext.serializationParameters);
    }

    private RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            SerializationParameters serializationParameters
    ) {
        this.configuration = configuration;
        this.staticType = staticType;
        this.executionMode = executionMode;
        this.metadata = metadata;
        this.serializationParameters = serializationParameters;
    }

    public RumbleRuntimeConfiguration getConfiguration() {
        return this.configuration;
    }

    public SequenceType getStaticType() {
        if (this.staticType == null) {
            throw new OurBadException("Clauses do not have static types.");
        }
        return this.staticType;
    }

    public ExecutionMode getExecutionMode() {
        return this.executionMode;
    }

    public void setExecutionMode(ExecutionMode mode) {
        this.executionMode = mode;
    }

    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    /**
     * Returns the default serialization parameters associated with this runtime static context.
     *
     * The returned instance is immutable from the caller's perspective: any mutating
     * operation should be performed on a defensive copy.
     *
     * Spec references:
     * - XQuery 3.1 Static Context Components — default serialization parameters
     * (https://www.w3.org/TR/xquery-31/#id-xq-static-context-components)
     * - XSLT and XQuery Serialization 3.1 — Serialization Parameters
     * (https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)
     */
    public SerializationParameters getSerializationParameters() {
        return this.serializationParameters;
    }


}
