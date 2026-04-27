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
        this.configuration = oldContext.configuration;
        this.staticType = oldContext.staticType;
        this.executionMode = oldContext.executionMode;
        this.metadata = oldContext.metadata;
        this.staticallyKnownNamespaces = oldContext.staticallyKnownNamespaces;
        this.decimalFormats = oldContext.decimalFormats;
        this.defaultDecimalFormat = oldContext.defaultDecimalFormat;
        this.serializationParameters = oldContext.serializationParameters;
    }

    /*
     * TODO at all the places where it is used, it should instead be obtained by modifying the existing fully populated
     * context.
     */
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
        this.serializationParameters = staticContext == null ? null : staticContext.getSerializationParameters();
    }

    /**
     * Returns the runtime configuration associated with this context, which is used for error reporting and to
     * determine limits such as the materialization cap. The returned configuration is never {@code null}.
     * 
     * @return the runtime configuration associated with this context, which is used for error reporting and to
     *         determine limits such as the materialization cap; the returned configuration is never {@code null}
     */
    public RumbleRuntimeConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Returns the static type of expressions in this context, or {@code null} if no static type is defined for this
     * context. Note that clauses do not have static types, so calling this method on a context associated with a clause
     * will throw an exception.
     * 
     * @return the static type of expressions in this context, or {@code null} if no static type is defined for this
     *         context; note that clauses do not have static types, so calling this method on a context associated with
     *         a clause will throw an exception
     */
    public SequenceType getStaticType() {
        if (this.staticType == null) {
            throw new OurBadException("Clauses do not have static types.");
        }
        return this.staticType;
    }

    /**
     * Returns the execution mode in which expressions in this context should be evaluated. The returned execution mode
     * is never {@code null}.
     * 
     * @return the execution mode in which expressions in this context should be evaluated; the returned execution mode
     *         is never {@code null}
     */
    public ExecutionMode getExecutionMode() {
        return this.executionMode;
    }

    /**
     * Returns the metadata associated with this context, which is used for error reporting. The returned metadata is
     * never {@code null}.
     * 
     * @return the metadata associated with this context, which is used for error reporting; the returned metadata is
     *         never {@code null}
     */
    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    /**
     * Returns the namespace bindings that are statically known in this context, i.e. the namespace bindings that are
     * defined in the static context from which this runtime static context was created. The returned map is
     * unmodifiable. If no namespace bindings are statically known in this context, returns an empty map.
     * 
     * @return the namespace bindings that are statically known in this context, i.e. the namespace bindings that are
     *         defined in the static context from which this runtime static context was created; the returned map is
     *         unmodifiable; if no namespace bindings are statically known in this context, returns an empty map
     */
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
     * Drops the decimal format definitions defined in this context, if any. After calling this method,
     * {@link #getDecimalFormats()} will return {@code null} and {@link #getDefaultDecimalFormat()} will return
     * {@code null}.
     */
    public void dropDecimalFormats() {
        this.decimalFormats = null;
        this.defaultDecimalFormat = null;
    }

    /**
     * Returns the decimal format definitions defined in this context, or {@code null} if no decimal formats are defined
     * in this context.
     * 
     * @return the decimal format definitions defined in this context, or {@code null} if no decimal formats are defined
     *         in this context
     */
    public Map<Name, DecimalFormatDefinition> getDecimalFormats() {
        return this.decimalFormats;
    }

    /**
     * Returns the default decimal format definition, or {@code null} if no default decimal format is defined in this
     * context.
     * 
     * @return the default decimal format definition, or {@code null} if no default decimal format is defined in this
     *         context
     */
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
     * 
     * @param newStaticType the new static type to use in the returned context
     * 
     * @return a new {@link RuntimeStaticContext} with the same configuration, execution mode, and metadata as this
     *         context, but with the specified static type
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
     * 
     * @param newExecutionMode the new execution mode to use in the returned context
     * 
     * @return a new {@link RuntimeStaticContext} with the same configuration, static type, and metadata as this
     *         context, but with the specified execution mode
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
     * 
     * @param newMetadata the new metadata to use in the returned context
     * 
     * @return a new {@link RuntimeStaticContext} with the same configuration, static type, and execution mode as this
     *         context, but with the specified metadata
     */
    public RuntimeStaticContext withMetadata(
            ExceptionMetadata newMetadata
    ) {
        RuntimeStaticContext result = new RuntimeStaticContext(this);
        result.metadata = newMetadata;
        return result;
    }

}
