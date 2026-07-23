package org.rumbledb.context;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
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
@Builder(toBuilder = true)
public class RuntimeStaticContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final URI staticURI;

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

    @Builder.Default
    private final Map<String, String> staticallyKnownNamespaces = Collections.emptyMap();

    @Builder.Default
    private final Set<String> staticallyKnownCollations = CollationCatalogue.defaultStaticallyKnownCollations();

    private final SerializationParameters serializationParameters;

    @Builder.Default
    private final String defaultCollation = CollationCatalogue.CODEPOINT_COLLATION;

    /**
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

    @Builder.Default
    private final boolean copyNamespacesPreserve = true;

    @Builder.Default
    private final boolean copyNamespacesInherit = true;

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
        sb.append("  copyNamespacesPreserve: ").append(this.copyNamespacesPreserve).append("\n");
        sb.append("  copyNamespacesInherit: ").append(this.copyNamespacesInherit).append("\n");
        sb.append("  decimalFormats: ").append(this.decimalFormats).append("\n");
        sb.append("  defaultDecimalFormat: ").append(this.defaultDecimalFormat).append("\n");
        sb.append("  serializationParameters: ").append(this.serializationParameters).append("\n");
        sb.append("  isQuerySideEffecting: ").append(this.isQuerySideEffecting).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Lombok generates the body of this class.
     * Without this declaration, Javadoc generation will return error because it cannot find symbol
     */
    public static class RuntimeStaticContextBuilder {
    }

    /**
     * Returns a builder seeded with the settings that originate in a {@link StaticContext}.
     *
     * @param staticContext the static context to copy settings from; must not be {@code null}
     * @return a builder for completing a runtime static context
     */
    public static RuntimeStaticContextBuilder fromStaticContext(@NonNull StaticContext staticContext) {
        return builder()
            .staticURI(staticContext.getStaticBaseURI())
            .queryLanguage(staticContext.getQueryLanguage())
            .staticallyKnownNamespaces(staticContext.getInScopeNamespaceBindings())
            .staticallyKnownCollations(staticContext.getStaticallyKnownCollations())
            .serializationParameters(staticContext.getSerializationParameters())
            .defaultCollation(staticContext.getDefaultCollation())
            .defaultDecimalFormat(staticContext.getDefaultDecimalFormat())
            .decimalFormats(staticContext.getDecimalFormats())
            .isQuerySideEffecting(staticContext.isQuerySideEffecting())
            .copyNamespacesPreserve(staticContext.isCopyNamespacesPreserve())
            .copyNamespacesInherit(staticContext.isCopyNamespacesInherit());
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
     * Creates a context without decimal format definitions.
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

    public boolean isCopyNamespacesPreserve() {
        return this.copyNamespacesPreserve;
    }

    public boolean isCopyNamespacesInherit() {
        return this.copyNamespacesInherit;
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
