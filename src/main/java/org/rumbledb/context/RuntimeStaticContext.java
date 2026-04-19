package org.rumbledb.context;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

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
    private final Map<String, String> staticallyKnownNamespaces;
    private final SerializationParameters serializationParameters;

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this(configuration, staticType, executionMode, metadata, null, configuration.getSerializationParameters());
    }

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            Map<String, String> staticallyKnownNamespaces
    ) {
        this(
            configuration,
            staticType,
            executionMode,
            metadata,
            staticallyKnownNamespaces,
            configuration.getSerializationParameters()
        );
    }

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            Map<String, String> staticallyKnownNamespaces,
            SerializationParameters serializationParameters
    ) {
        this.configuration = configuration;
        this.staticType = staticType;
        this.executionMode = executionMode;
        this.metadata = metadata;
        this.staticallyKnownNamespaces = staticallyKnownNamespaces;
        this.serializationParameters = serializationParameters;
    }

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this(configuration, null, executionMode, metadata, null, configuration.getSerializationParameters());
    }

    public RuntimeStaticContext(
            StaticContext staticContext,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this(
            staticContext.getRumbleConfiguration(),
            staticType,
            executionMode,
            metadata,
            staticContext.getInScopeNamespaceBindings(),
            staticContext.getSerializationParameters()
        );
    }

    public RuntimeStaticContext(
            RuntimeStaticContext staticContext,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this(
            staticContext.configuration,
            staticType,
            executionMode,
            metadata,
            staticContext.staticallyKnownNamespaces,
            staticContext.serializationParameters
        );
    }

    public RuntimeStaticContext(
            RuntimeStaticContext staticContext,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            Map<String, String> staticallyKnownNamespaces
    ) {
        this(
            staticContext.configuration,
            staticType,
            executionMode,
            metadata,
            staticallyKnownNamespaces,
            staticContext.serializationParameters
        );
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

    public SerializationParameters getSerializationParameters() {
        return this.serializationParameters;
    }

    /**
     * Resolves a namespace prefix using in-scope bindings from this context, falling back to built-in
     * prefixes (fn, xs, ...). For the default element/type namespace, pass {@code ""}.
     *
     * @return the namespace URI, or {@code null} if the prefix is not bound
     */
    public String resolvePrefix(String prefix) {
        if (this.staticallyKnownNamespaces != null && this.staticallyKnownNamespaces.containsKey(prefix)) {
            return this.staticallyKnownNamespaces.get(prefix);
        }
        return StaticContext.getBuiltinNamespaceBinding(prefix);
    }

    /**
     * Same configuration, metadata, and namespace map; replaces static type and execution mode (e.g. when building
     * nested iterator contexts from a call-site {@link RuntimeStaticContext}).
     */
    public RuntimeStaticContext withStaticTypeAndExecutionMode(
            SequenceType newStaticType,
            ExecutionMode newExecutionMode
    ) {
        return new RuntimeStaticContext(
                this.configuration,
                newStaticType,
                newExecutionMode,
                this.metadata,
                this.staticallyKnownNamespaces
        );
    }

}
