/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.context;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.config.SerializationParameterBuilder;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.SerializationParameterUtils;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import lombok.Getter;
import lombok.Setter;

public class StaticContext {

    @Getter
    private Map<Name, InScopeVariable> inScopeVariables = new HashMap<>();

    private Map<String, String> staticallyKnownNamespaces = new HashMap<>();
    private UserDefinedFunctionExecutionModes userDefinedFunctionExecutionModes;
    private InScopeSchemaTypes inScopeSchemaTypes = new InScopeSchemaTypes();

    @Setter
    private String queryLanguage;

    @Getter
    private StaticContext parent;

    private URI staticBaseURI;
    private boolean emptySequenceOrderLeast = true;
    private boolean boundarySpacePreserve = true;

    @Setter
    private SerializationParameters serializationParameters = SerializationParameters.defaults();

    private Set<String> explicitSerializationParameterNames = new LinkedHashSet<>();
    private boolean isQuerySideEffecting;
    private Set<String> staticallyKnownCollations = CollationCatalogue.defaultStaticallyKnownCollations();
    private String defaultCollation = CollationCatalogue.CODEPOINT_COLLATION;

    /**
     * XQuery {@code declare default function namespace}; when null, unprefixed function names use
     * {@link Name#JSONIQ_DEFAULT_FUNCTION_NS} (Rumble's usual fn/jn/... resolution path).
     */
    private String defaultFunctionNamespaceUri;

    @Getter
    @Setter
    private SequenceType contextItemStaticType;
    private Map<FunctionIdentifier, FunctionSignature> staticallyKnownFunctionSignatures =
        new HashMap<>();
    private static final Map<String, String> DEFAULT_BINDINGS = Map.ofEntries(
        Map.entry("local", Name.LOCAL_NS),
        Map.entry("fn", Name.FN_NS),
        Map.entry("math", Name.MATH_NS),
        Map.entry("map", Name.MAP_NS),
        Map.entry("array", Name.ARRAY_NS),
        Map.entry("xs", Name.XS_NS),
        Map.entry("xsi", Name.XSI_NS),
        Map.entry("xml", Name.XML_NS),
        Map.entry("jn", Name.JN_NS),
        Map.entry("js", Name.JS_NS),
        Map.entry("err", Name.ERROR_NS),
        Map.entry("an", Name.JSONIQ_ANNOTATIONS_NS)
    );

    private DecimalFormatDefinition defaultDecimalFormat = DecimalFormatDefinition.defaultInstance();
    private Map<Name, DecimalFormatDefinition> decimalFormats = new HashMap<>();

    @Getter
    @Setter
    private int currentMutabilityLevel;

    private RumbleRuntimeConfiguration configuration;

    public StaticContext(URI staticBaseURI, RumbleRuntimeConfiguration configuration) {
        this.staticBaseURI = staticBaseURI;
        this.configuration = configuration;
        if (configuration != null) {
            this.queryLanguage = configuration.getQueryLanguage();
            this.serializationParameters = SerializationParameters.copy(configuration.getSerializationParameters());
        }
    }

    /**
     * Initialize a child static context
     * 
     * @param parent the parent static context
     */
    public StaticContext(StaticContext parent) {
        this.parent = Objects.requireNonNull(parent, "parent");
        this.currentMutabilityLevel = parent.currentMutabilityLevel;
        this.inScopeSchemaTypes = parent.inScopeSchemaTypes;
        this.serializationParameters = SerializationParameters.copy(parent.serializationParameters);

        // Local maps are initialized at declaration.
        // Other fields inherit through parent lookup.
    }

    public RumbleRuntimeConfiguration getRumbleConfiguration() {
        if (this.configuration != null) {
            return this.configuration;
        }
        if (this.parent != null) {
            return this.parent.getRumbleConfiguration();
        }
        throw new OurBadException("Configuration not set.");
    }

    public String getQueryLanguage() {
        if (this.queryLanguage != null) {
            return this.queryLanguage;
        }
        if (this.parent != null) {
            return this.parent.getQueryLanguage();
        }
        throw new OurBadException("Query language not set.");
    }

    public URI getStaticBaseURI() {
        if (this.staticBaseURI != null) {
            return this.staticBaseURI;
        }
        if (this.parent != null) {
            return this.parent.getStaticBaseURI();
        }
        throw new OurBadException("Static base URI not set.");
    }

    public void setStaticBaseUri(URI staticBaseURI) {
        this.staticBaseURI = staticBaseURI;
    }

    public boolean isInScope(Name varName) {
        StaticContext context = this;
        while (context != null) {
            if (context.inScopeVariables.containsKey(varName)) {
                return true;
            }
            context = context.parent;
        }
        return false;
    }

    private InScopeVariable getInScopeVariable(Name varName) {
        StaticContext context = this;
        while (context != null) {
            InScopeVariable variable = context.inScopeVariables.get(varName);
            if (variable != null) {
                return variable;
            }
            context = context.parent;
        }
        throw new SemanticException("Variable " + varName + " not in scope", ExceptionMetadata.EMPTY_METADATA);
    }

    public FunctionSignature getFunctionSignature(FunctionIdentifier identifier) {
        StaticContext context = this;
        while (context != null) {
            FunctionSignature signature = context.staticallyKnownFunctionSignatures.get(identifier);
            if (signature != null) {
                return signature;
            }
            context = context.parent;
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                ExceptionMetadata.EMPTY_METADATA
        );
    }

    // replace the sequence type of an existing InScopeVariable, throws an error if the variable does not exists
    public void replaceVariableSequenceType(Name varName, SequenceType newSequenceType) {
        InScopeVariable variable = this.getInScopeVariable(varName);
        this.inScopeVariables.replace(
            varName,
            new InScopeVariable(
                    varName,
                    newSequenceType,
                    variable.getMetadata(),
                    variable.getStorageMode(),
                    variable.isAssignable()
            )
        );
    }

    public SequenceType getVariableSequenceType(Name varName) {
        return this.getInScopeVariable(varName).getSequenceType();
    }

    public ExecutionMode getVariableStorageMode(Name varName) {
        return this.getInScopeVariable(varName).getStorageMode();
    }

    public void setVariableStorageMode(Name varName, ExecutionMode mode) {
        this.getInScopeVariable(varName).setStorageMode(mode);
    }

    public void addVariable(
            Name varName,
            SequenceType type,
            ExceptionMetadata metadata
    ) {
        this.inScopeVariables.put(
            varName,
            new InScopeVariable(varName, type, metadata, ExecutionMode.UNSET)
        );
    }

    public void addVariable(
            Name varName,
            SequenceType type,
            ExceptionMetadata metadata,
            boolean isAssignable
    ) {
        this.inScopeVariables.put(
            varName,
            new InScopeVariable(varName, type, metadata, ExecutionMode.UNSET, isAssignable)
        );
    }

    public void addFunctionSignature(FunctionIdentifier identifier, FunctionSignature signature) {
        this.staticallyKnownFunctionSignatures.put(identifier, signature);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Static context with variables:\n");
        for (Entry<Name, InScopeVariable> entry : this.inScopeVariables.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" as " + entry.getValue().getSequenceType());
            stringBuilder.append(" (namespace " + entry.getKey().getNamespace() + ")");
            stringBuilder.append(" | " + entry.getValue().getStorageMode());
            if (entry.getValue().isAssignable()) {
                stringBuilder.append(" | assignable");
            } else {
                stringBuilder.append(" | not assignable");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("Static context with user-defined functions:\n");
        for (Entry<FunctionIdentifier, FunctionSignature> entry : this.staticallyKnownFunctionSignatures.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" as " + entry.getValue());
            stringBuilder.append(" (namespace " + entry.getKey().getName().getNamespace() + ")");
            stringBuilder.append("\n");
        }
        stringBuilder.append("Static context with user-defined types:\n");
        for (ItemType itemType : this.inScopeSchemaTypes.getInScopeSchemaTypes()) {
            stringBuilder.append(itemType.getName());
            stringBuilder.append(itemType.isResolved() ? " (resolved)" : " (unresolved)");
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        if (this.userDefinedFunctionExecutionModes != null) {
            stringBuilder.append(this.userDefinedFunctionExecutionModes);
        }
        if (this.parent != null) {
            stringBuilder.append("\nParent:");
            stringBuilder.append(this.parent);
        }
        return stringBuilder.toString();
    }

    public boolean hasVariableInScopeOnly(Name variableName) {
        return this.inScopeVariables.containsKey(variableName);
    }

    public boolean bindNamespace(String prefix, String namespace) {
        if (this.canBindNamespace(prefix)) {
            this.staticallyKnownNamespaces.put(prefix, namespace);
            return true;
        }
        return false;
    }

    /**
     * Explicitly removes a namespace binding in this context, shadowing any inherited or predeclared binding.
     */
    public boolean unbindNamespace(String prefix) {
        if (!this.canBindNamespace(prefix)) {
            return false;
        }
        this.staticallyKnownNamespaces.put(prefix, null);
        return true;
    }

    private boolean canBindNamespace(String prefix) {
        if (!this.staticallyKnownNamespaces.containsKey(prefix)) {
            return true;
        }
        return DEFAULT_BINDINGS.containsKey(prefix)
            && DEFAULT_BINDINGS.get(prefix).equals(this.staticallyKnownNamespaces.get(prefix));
    }

    public String resolveNamespace(String prefix) {
        if (this.staticallyKnownNamespaces.containsKey(prefix)) {
            return this.staticallyKnownNamespaces.get(prefix);
        }
        if (this.parent != null) {
            return this.parent.resolveNamespace(prefix);
        }
        return null;
    }

    public Map<String, String> getInScopeNamespaceBindings() {
        Map<String, String> bindings = new HashMap<>();
        if (this.parent != null) {
            bindings.putAll(this.parent.getInScopeNamespaceBindings());
        }
        bindings.putAll(this.staticallyKnownNamespaces);
        return bindings;
    }

    /**
     * Returns the default serialization parameters stored in the static context.
     * Spec references:
     * 
     * <ul>
     * <li>XQuery 3.1 Static Context Components (link:
     * https://www.w3.org/TR/xquery-31/#id-xq-static-context-components)</li>
     * <li>Serialization 3.1 — Serialization Parameters (link:
     * https://www.w3.org/TR/xslt-xquery-serialization-31/#serparam)</li>
     * </ul>
     */
    public SerializationParameters getSerializationParameters() {
        return this.serializationParameters;
    }


    public void overrideSerializationParameter(String name, String value, ExceptionMetadata metadata) {
        if ("parameter-document".equals(name)) {
            SerializationParameterUtils.applyParameterDocument(
                this.serializationParameters,
                this,
                value,
                this.getExplicitSerializationParameterNames(),
                metadata
            );
            return;
        }
        if ("cdata-section-elements".equals(name) || "suppress-indentation".equals(name)) {
            value = this.expandSerializationQNames(value);
        }
        this.getExplicitSerializationParameterNames().add(name);
        // update the local copy of theserialization parameters with the provided parameter name and value
        SerializationParameterBuilder.update(this.serializationParameters, name, value);
    }

    private Set<String> getExplicitSerializationParameterNames() {
        return this.explicitSerializationParameterNames;
    }

    private String expandSerializationQNames(String value) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }
        StringBuilder sb = new StringBuilder();
        String separator = "";
        for (String token : value.trim().split("[,\\s]+")) {
            if (token.isEmpty()) {
                continue;
            }
            sb.append(separator).append(this.expandSerializationQName(token));
            separator = " ";
        }
        return sb.toString();
    }

    private String expandSerializationQName(String lexicalQName) {
        if (lexicalQName.startsWith("Q{")) {
            return lexicalQName;
        }
        int colon = lexicalQName.indexOf(':');
        if (colon < 0) {
            String namespace = this.getInScopeNamespaceBindings().get("");
            if (namespace == null || namespace.isEmpty()) {
                return lexicalQName;
            }
            return "Q{" + namespace + "}" + lexicalQName;
        }
        String prefix = lexicalQName.substring(0, colon);
        String localName = lexicalQName.substring(colon + 1);
        String namespace = this.getInScopeNamespaceBindings().get(prefix);
        if (namespace == null) {
            return lexicalQName;
        }
        return "Q{" + namespace + "}" + localName;
    }

    public void importModuleContext(StaticContext moduleContext) {
        this.inScopeVariables.putAll(moduleContext.inScopeVariables);
        this.staticallyKnownFunctionSignatures.putAll(moduleContext.staticallyKnownFunctionSignatures);
    }

    public void setUserDefinedFunctionsExecutionModes(
            UserDefinedFunctionExecutionModes staticallyKnownFunctionSignatures
    ) {
        if (this.parent != null) {
            throw new OurBadException("Statically known function signatures can only be stored in the module context.");
        }
        this.userDefinedFunctionExecutionModes = staticallyKnownFunctionSignatures;
    }

    public UserDefinedFunctionExecutionModes getUserDefinedFunctionsExecutionModes() {
        if (this.userDefinedFunctionExecutionModes != null) {
            return this.userDefinedFunctionExecutionModes;
        }
        if (this.parent != null) {
            return this.parent.getUserDefinedFunctionsExecutionModes();
        }
        throw new OurBadException("Statically known function signatures are not set up properly in static context.");
    }

    public void setEmptySequenceOrderLeast(boolean emptySequenceOrderLeast) {
        if (this.parent != null) {
            throw new OurBadException("Empty sequence ordering can only be set in the root static context.");
        }
        this.emptySequenceOrderLeast = emptySequenceOrderLeast;
    }

    public void setBoundarySpacePreserve(boolean boundarySpacePreserve) {
        if (this.parent != null) {
            throw new OurBadException("Boundary-space policy can only be set in the root static context.");
        }
        this.boundarySpacePreserve = boundarySpacePreserve;
    }

    /**
     * Default function namespace URI for unprefixed function names (XQuery prolog). Root/module context only.
     */
    public void setDefaultFunctionNamespaceUri(String uri) {
        if (this.parent != null) {
            throw new OurBadException("Default function namespace can only be set in the root static context.");
        }
        this.defaultFunctionNamespaceUri = uri;
    }

    /**
     * @return the declared default function namespace URI, or null if not set (use JSONiq default function NS)
     */
    public String getDefaultFunctionNamespaceUri() {
        if (this.parent != null) {
            return this.parent.getDefaultFunctionNamespaceUri();
        }
        return this.defaultFunctionNamespaceUri;
    }

    public boolean isEmptySequenceOrderLeast() {
        if (this.parent != null) {
            return this.parent.isEmptySequenceOrderLeast();
        }
        return this.emptySequenceOrderLeast;
    }

    public boolean isBoundarySpacePreserve() {
        if (this.parent != null) {
            return this.parent.isBoundarySpacePreserve();
        }
        return this.boundarySpacePreserve;
    }

    public boolean isStaticallyKnownCollation(String uri) {
        return this.getStaticallyKnownCollations().contains(uri);
    }

    public Set<String> getStaticallyKnownCollations() {
        if (this.parent != null) {
            return this.parent.getStaticallyKnownCollations();
        }
        return Collections.unmodifiableSet(this.staticallyKnownCollations);
    }

    public void setDefaultCollation(String uri) {
        if (this.parent != null) {
            throw new OurBadException("Default collation can only be set in the root static context.");
        }
        if (!this.staticallyKnownCollations.contains(uri)) {
            throw new OurBadException("Default collation must be statically known.");
        }
        this.defaultCollation = uri;
    }

    public String getDefaultCollation() {
        if (this.parent != null) {
            return this.parent.getDefaultCollation();
        }
        return this.defaultCollation;
    }

    // replace all inScopeVariable in this context and all parents until [stopContext] with name not in [varToExclude]
    // with same variable with sequence type arity changed from 1 to + and form ? to *
    // used by groupBy clause
    public void incrementArities(StaticContext stopContext, Set<Name> varToExclude) {
        this.inScopeVariables.replaceAll(
            (key, value) -> varToExclude.contains(key)
                ? value
                : new InScopeVariable(
                        value.getName(),
                        this.incrementArity(value.getSequenceType()),
                        value.getMetadata(),
                        value.getStorageMode()
                )
        );
        StaticContext current = this.parent;
        while (current != null && current != stopContext) {
            for (Map.Entry<Name, InScopeVariable> entry : current.inScopeVariables.entrySet()) {
                if (!this.inScopeVariables.containsKey(entry.getKey())) {
                    this.addVariable(
                        entry.getKey(),
                        varToExclude.contains(entry.getKey())
                            ? entry.getValue().getSequenceType()
                            : this.incrementArity(entry.getValue().getSequenceType()),
                        entry.getValue().getMetadata(),
                        entry.getValue().isAssignable()
                    );
                }
            }
            current = current.parent;
        }
    }

    private SequenceType incrementArity(SequenceType sequenceType) {
        return sequenceType == null
            ? SequenceType.createSequenceType("item*")
            : sequenceType.incrementArity();
    }

    public void bindDefaultNamespaces() {
        for (Map.Entry<String, String> binding : DEFAULT_BINDINGS.entrySet()) {
            this.bindNamespace(binding.getKey(), binding.getValue());
        }
    }

    /**
     * Built-in namespace bindings (fn, xs, map, ...) used when resolving QNames without a full static context.
     */
    public static String getBuiltinNamespaceBinding(String prefix) {
        return DEFAULT_BINDINGS.get(prefix);
    }

    public InScopeSchemaTypes getInScopeSchemaTypes() {
        return this.inScopeSchemaTypes;
    }

    public boolean getIsAssignable(Name name) {
        return this.getInScopeVariable(name).isAssignable();
    }

    public void setDefaultDecimalFormat(DecimalFormatDefinition decimalFormat) {
        if (this.parent != null) {
            this.parent.setDefaultDecimalFormat(decimalFormat);
            return;
        }
        this.defaultDecimalFormat = decimalFormat;
    }

    public void addDecimalFormat(Name name, DecimalFormatDefinition decimalFormat, ExceptionMetadata metadata) {
        if (this.parent != null) {
            this.parent.addDecimalFormat(name, decimalFormat, metadata);
            return;
        }
        if (this.decimalFormats.containsKey(name)) {
            throw new SemanticException(
                    "Decimal format already declared: " + name,
                    metadata
            );
        }
        this.decimalFormats.put(name, decimalFormat);
    }

    public DecimalFormatDefinition getDefaultDecimalFormat() {
        if (this.parent != null) {
            return this.parent.getDefaultDecimalFormat();
        }
        return this.defaultDecimalFormat;
    }

    public Map<Name, DecimalFormatDefinition> getDecimalFormats() {
        if (this.parent != null) {
            return this.parent.getDecimalFormats();
        }
        return Collections.unmodifiableMap(this.decimalFormats);
    }

    public boolean isQuerySideEffecting() {
        if (this.parent != null) {
            return this.parent.isQuerySideEffecting();
        }
        return this.isQuerySideEffecting;
    }

    public void setIsQuerySideEffecting(boolean isQuerySideEffecting) {
        if (this.parent != null) {
            this.parent.setIsQuerySideEffecting(isQuerySideEffecting);
            return;
        }
        this.isQuerySideEffecting = isQuerySideEffecting;
    }
}
