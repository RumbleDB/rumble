package org.rumbledb.bindings;

import lombok.Value;
import lombok.experimental.Accessors;
import org.rumbledb.context.Name;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Query-scoped external bindings supplied at execution time.
 *
 * This is intentionally separate from {@code RumbleConfiguration}: bindings are execution inputs, not engine
 * configuration. The model is broader than what the current runtime bridge can consume so the public API can settle
 * before the remaining plumbing is migrated away from {@code RumbleRuntimeConfiguration}.
 */
@Value
@Accessors(fluent = true)
public class ExternalBindings {
    private Map<Name, Binding> variables;

    public ExternalBindings() {
        this.variables = new LinkedHashMap<>();
    }

    public void bind(Name name, Binding binding) {
        if (this.variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " is already bound.");
        }

        this.variables.put(name, binding);
    }

    public void unbind(Name name) {
        this.variables.remove(name);
    }

    public Optional<Binding> get(Name name) {
        return Optional.ofNullable(this.variables.get(name));
    }

    public Set<Name> names() {
        return this.variables.keySet();
    }
}
