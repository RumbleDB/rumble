package org.rumbledb.cli.bindings;

import java.util.Objects;
import lombok.Value;

@Value
public class LexicalValue implements VariableSource {
    private final String value;

    public LexicalValue(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public boolean isLexicalValue() {
        return true;
    }

    @Override
    public String getLexicalValue() {
        return this.value;
    }
}
