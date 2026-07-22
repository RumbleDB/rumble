package org.rumbledb.context;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.types.SequenceType;

@Value
public class RuntimeStaticContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Query language associated with this context, which is used for error reporting and to determine the
     * semantics of certain operations.
     */
    private final String queryLanguage;

    /**
     * Runtime configuration associated with this context, which is used for error reporting and to
     * determine limits such as the materialization cap; the returned configuration is never {@code null}
     */
    @NonNull
    private final RumbleRuntimeConfiguration configuration;

    private final SequenceType staticType;

    /**
     * Execution mode in which expressions in this context should be evaluated; the returned execution mode
     * is never {@code null}
     */
    @NonNull
    private final ExecutionMode executionMode;

    /**
     * Metadata associated with this context, which is used for error reporting.
     */
    @NonNull
    private final ExceptionMetadata metadata;

    private final Map<String, String> staticallyKnownNamespaces;
    private final Set<String> staticallyKnownCollations;

    private final SerializationParameters serializationParameters;

    private final String defaultCollation;
    /**
     * -- GETTER --
     * Default decimal format definition, or {@code null} if no default decimal format is defined in this
     * context
     */
    private final DecimalFormatDefinition defaultDecimalFormat;

    /**
     * Decimal format definitions defined in this context, or {@code null} if no decimal formats are defined
     * in this context
     */
    private final Map<Name, DecimalFormatDefinition> decimalFormats;

    /**
     * Whether this context is associated with a query that has side effects. This is used to determine whether
     * certain optimizations are allowed, such as reordering of expressions or elimination of redundant expressions.
     */
    private final boolean isQuerySideEffecting;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RuntimeStaticContext {\n");
        sb.append("  query language: ").append(this.queryLanguage).append("\n");
        sb.append("  configuration: ").append(this.configuration).append("\n");
        sb.append("  staticType: ").append(this.staticType).append("\n");
        sb.append("  executionMode: ").append(this.executionMode).append("\n");
        sb.append("  metadata: ").append(this.metadata).append("\n");
        sb.append("  staticallyKnownNamespaces: ").append(this.staticallyKnownNamespaces).append("\n");
        sb.append("  staticallyKnownCollations: ").append(this.staticallyKnownCollations).append("\n");
        sb.append("  defaultCollation: ").append(this.defaultCollation).append("\n");
        sb.append("  decimalFormats: ").append(this.decimalFormats).append("\n");
        sb.append("  defaultDecimalFormat: ").append(this.defaultDecimalFormat).append("\n");
        sb.append("  serializationParameters: ").append(this.serializationParameters).append("\n");
        sb.append("  isQuerySideEffecting: ").append(this.isQuerySideEffecting).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Returns a builder seeded with the settings that originate in a {@link StaticContext}.
     *
     * @param staticContext the static context to copy settings from; {@code null} uses the runtime defaults
     * @return a builder for completing a runtime static context
     */
    public static RuntimeStaticContextBuilder fromStaticContext(StaticContext staticContext) {
        return builder()
            .queryLanguage(staticContext == null ? null : staticContext.getQueryLanguage())
            .staticallyKnownNamespaces(
                staticContext == null ? Collections.emptyMap() : staticContext.getInScopeNamespaceBindings()
            )
            .staticallyKnownCollations(
                staticContext == null
                    ? CollationCatalogue.defaultStaticallyKnownCollations()
                    : staticContext.getStaticallyKnownCollations()
            )
            .serializationParameters(staticContext == null ? null : staticContext.getSerializationParameters())
            .defaultCollation(staticContext == null ? null : staticContext.getDefaultCollation())
            .defaultDecimalFormat(staticContext == null ? null : staticContext.getDefaultDecimalFormat())
            .decimalFormats(staticContext == null ? null : staticContext.getDecimalFormats())
            .isQuerySideEffecting(staticContext != null && staticContext.isQuerySideEffecting());
    }

    @Builder(toBuilder = true)
    private RuntimeStaticContext(
            String queryLanguage,
            @NonNull RumbleRuntimeConfiguration configuration,
            SequenceType staticType,
            @NonNull ExecutionMode executionMode,
            @NonNull ExceptionMetadata metadata,
            Map<String, String> staticallyKnownNamespaces,
            Set<String> staticallyKnownCollations,
            SerializationParameters serializationParameters,
            String defaultCollation,
            DecimalFormatDefinition defaultDecimalFormat,
            Map<Name, DecimalFormatDefinition> decimalFormats,
            boolean isQuerySideEffecting
    ) {
        this.queryLanguage = queryLanguage;
        this.configuration = configuration;
        this.staticType = staticType;
        this.executionMode = executionMode;
        this.metadata = metadata;
        this.staticallyKnownNamespaces = Collections.unmodifiableMap(
            new HashMap<>(staticallyKnownNamespaces == null ? Collections.emptyMap() : staticallyKnownNamespaces)
        );
        this.staticallyKnownCollations = Collections.unmodifiableSet(
            new HashSet<>(
                    staticallyKnownCollations == null
                        ? CollationCatalogue.defaultStaticallyKnownCollations()
                        : staticallyKnownCollations
            )
        );
        this.serializationParameters = serializationParameters;
        this.defaultCollation = defaultCollation == null ? CollationCatalogue.CODEPOINT_COLLATION : defaultCollation;
        this.defaultDecimalFormat = defaultDecimalFormat;
        this.decimalFormats = decimalFormats == null
            ? null
            : Collections.unmodifiableMap(new HashMap<>(decimalFormats));
        this.isQuerySideEffecting = isQuerySideEffecting;
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

    public Set<String> getStaticallyKnownCollations() {
        if (this.staticallyKnownCollations == null) {
            return CollationCatalogue.defaultStaticallyKnownCollations();
        }
        return Collections.unmodifiableSet(this.staticallyKnownCollations);
    }

    public String getDefaultCollation() {
        if (this.defaultCollation == null) {
            return CollationCatalogue.CODEPOINT_COLLATION;
        }
        return this.defaultCollation;
    }

    /**
     * Creates a context without decimal format definitions. In the returned context,
     * {@link #getDecimalFormats()} and {@link #getDefaultDecimalFormat()} return {@code null}.
     *
     * @return a copy of this context without decimal format definitions
     */
    public RuntimeStaticContext withoutDecimalFormats() {
        return this.toBuilder()
            .decimalFormats(null)
            .defaultDecimalFormat(null)
            .build();
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
        return this.toBuilder().staticType(newStaticType).build();
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
        return this.toBuilder().executionMode(newExecutionMode).build();
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
        return this.toBuilder().metadata(newMetadata).build();
    }

}
