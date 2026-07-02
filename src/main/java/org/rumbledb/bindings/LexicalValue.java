package org.rumbledb.bindings;

import java.util.Objects;
import lombok.Value;

@Value
public class LexicalValue implements Binding {
    private final String value;

    public LexicalValue(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public boolean isLexicalBinding() {
        return true;
    }

    @Override
    public String getLexicalValue() {
        return this.value;
    }
}
