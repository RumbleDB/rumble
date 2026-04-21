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
    private DecimalFormatDefinition defaultDecimalFormat;
    private Map<Name, DecimalFormatDefinition> decimalFormats;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RuntimeStaticContext {\n");
        sb.append("  configuration: ").append(this.configuration).append("\n");
        sb.append("  staticType: ").append(this.staticType).append("\n");
        sb.append("  executionMode: ").append(this.executionMode).append("\n");
        sb.append("  metadata: ").append(this.metadata).append("\n");
        sb.append("  staticallyKnownNamespaces: ").append(this.staticallyKnownNamespaces).append("\n");
        sb.append("  decimalFormats: ").append(this.decimalFormats).append("\n");
        sb.append("  defaultDecimalFormat: ").append(this.defaultDecimalFormat).append("\n");
        sb.append("  serializationParameters: ").append(this.serializationParameters).append("\n");
        sb.append("}");
        return sb.toString();
    }

    public RuntimeStaticContext(
            RuntimeStaticContext oldContext
    ) {
        this.configuration = oldContext.getConfiguration();
        this.staticType = oldContext.getStaticType();
        this.executionMode = oldContext.getExecutionMode();
        this.metadata = oldContext.getMetadata();
        this.staticallyKnownNamespaces = oldContext.getStaticallyKnownNamespaces();
        this.decimalFormats = oldContext.getDecimalFormats();
        this.defaultDecimalFormat = oldContext.getDefaultDecimalFormat();
        this.serializationParameters = oldContext.getSerializationParameters();
    }

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
            StaticContext staticContext
    ) {
        this.configuration = configuration;
        this.staticType = staticType;
        this.executionMode = executionMode;
        this.metadata = metadata;
        staticallyKnownNamespaces = staticContext == null
            ? Collections.emptyMap()
            : staticContext.getInScopeNamespaceBindings();
        this.decimalFormats = staticContext == null ? null : staticContext.getDecimalFormats();
        this.defaultDecimalFormat = staticContext == null ? null : staticContext.getDefaultDecimalFormat();
        this.serializationParameters = configuration.getSerializationParameters();
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


    public SerializationParameters getSerializationParameters() {
        return this.serializationParameters;
    }

    public void dropDecimalFormats() {
        this.decimalFormats = null;
        this.defaultDecimalFormat = null;
    }

    public Map<Name, DecimalFormatDefinition> getDecimalFormats() {
        return this.decimalFormats;
    }

    public DecimalFormatDefinition getDefaultDecimalFormat() {
        return this.defaultDecimalFormat;
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
     * Creates a new context with a different static type (e.g. when building
     * nested iterator contexts from a call-site {@link RuntimeStaticContext}).
     */
    public RuntimeStaticContext withStaticType(
            SequenceType newStaticType
    ) {
        RuntimeStaticContext result = new RuntimeStaticContext(this);
        result.staticType = newStaticType;
        return result;
    }

    /**
     * Creates a new context with a different execution mode (e.g. when building
     * nested iterator contexts from a call-site {@link RuntimeStaticContext}).
     */
    public RuntimeStaticContext withExecutionMode(
            ExecutionMode newExecutionMode
    ) {
        RuntimeStaticContext result = new RuntimeStaticContext(this);
        result.executionMode = newExecutionMode;
        return result;
    }

    /**
     * Creates a new context with different metadata (e.g. when building
     * nested iterator contexts from a call-site {@link RuntimeStaticContext}).
     */
    public RuntimeStaticContext withMetadata(
            ExceptionMetadata newMetadata
    ) {
        RuntimeStaticContext result = new RuntimeStaticContext(this);
        result.metadata = newMetadata;
        return result;
    }

}
