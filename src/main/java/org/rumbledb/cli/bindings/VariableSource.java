package org.rumbledb.cli.bindings;

import java.net.URI;

public interface VariableSource {
    public default boolean isLexicalValue() {
        return false;
    }

    public default String getLexicalValue() {
        throw new UnsupportedOperationException("Not a lexical value");
    }

    public default boolean isFileValue() {
        return false;
    }

    public default URI getFileLocation() {
        throw new UnsupportedOperationException("Not a file value");
    }

    public default boolean isStandardInput() {
        return false;
    }

    public default StandardInputValue.InputFormat getStandardInputFormat() {
        throw new UnsupportedOperationException("Not a standard input value");
    }
}
