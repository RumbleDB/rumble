package org.rumbledb.bindings;

import java.util.Objects;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public final class LexicalBinding implements Binding {
    private static final long serialVersionUID = 1L;

    String value;

    public LexicalBinding(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }
}
