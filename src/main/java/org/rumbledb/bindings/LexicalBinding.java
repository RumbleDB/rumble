package org.rumbledb.bindings;

import java.util.Objects;

import lombok.Value;

@Value
public final class LexicalBinding implements Binding {
    String value;

    public LexicalBinding(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }
}
