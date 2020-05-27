package org.rumbledb.expressions.module;

public class FunctionOrVariableName {
    private String namespace;
    private String prefix;
    private String localName;

    public FunctionOrVariableName(String namespace, String prefix, String localName) {
        this.namespace = namespace;
        this.prefix = prefix;
        this.localName = localName;
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
