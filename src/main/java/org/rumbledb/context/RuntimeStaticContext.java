package org.rumbledb.context;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

public class RuntimeStaticContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private RumbleRuntimeConfiguration configuration;
    private SequenceType staticType;
    private ExecutionMode executionMode;
    private ExceptionMetadata metadata;
    private final Map<String, String> staticallyKnownNamespaces;

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this(configuration, staticType, executionMode, metadata, null);
    }

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            Map<String, String> staticallyKnownNamespaces
    ) {
        this.configuration = configuration;
        this.staticType = staticType;
        this.executionMode = executionMode;
        this.metadata = metadata;
        this.staticallyKnownNamespaces = staticallyKnownNamespaces;
    }

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this(configuration, null, executionMode, metadata, null);
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

    public Map<String, String> getStaticallyKnownNamespaces() {
        if (this.staticallyKnownNamespaces == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(this.staticallyKnownNamespaces);
    }

}
