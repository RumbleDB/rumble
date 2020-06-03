package org.rumbledb.expressions.module;

import org.rumbledb.exceptions.OurBadException;

public class FunctionOrVariableName {
    private String namespace;
    private String prefix;
    private String localName;
    private static final String RUMBLE_NS = "http://rumbledb.org/main-namespace";
    public static final FunctionOrVariableName CONTEXT_ITEM = createVariableInNoNamespace("$");
    public static final FunctionOrVariableName CONTEXT_POSITION = createVariableInNoNamespace("$position");
    public static final FunctionOrVariableName CONTEXT_COUNT = createVariableInNoNamespace("$count");

    public FunctionOrVariableName(String namespace, String prefix, String localName) {
        this.namespace = namespace;
        this.prefix = prefix;
        this.localName = localName;
        if (this.prefix != null && this.namespace == null) {
            throw new OurBadException("Namespace is null, but prefix is present");
        }
    }

    public static FunctionOrVariableName createVariableInNoNamespace(String localName) {
        return new FunctionOrVariableName(null, null, localName);
    }

    public static FunctionOrVariableName createVariableInRumbleNamespace(String localName) {
        return new FunctionOrVariableName(RUMBLE_NS, "", localName);
    }

    public FunctionOrVariableName addArityToFunctionName(int arity) {
        return new FunctionOrVariableName(this.namespace, this.prefix, this.localName + "#" + arity);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getLocalName() {
        return this.localName;
    }

    @Override
    public String toString() {
        if (this.namespace != null) {
            return "{" + this.namespace + "}" + this.localName;
        }
        return this.localName;
    }

}
