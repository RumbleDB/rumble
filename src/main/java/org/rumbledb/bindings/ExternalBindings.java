package org.rumbledb.bindings;

import org.rumbledb.context.Name;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Query-scoped external bindings supplied at execution time.
 *
 * This is intentionally separate from {@code RumbleConfiguration}: bindings are execution inputs, not engine
 * configuration. The model is broader than what the current runtime bridge can consume so the public API can settle
 * before the remaining plumbing is migrated away from {@code RumbleRuntimeConfiguration}.
 */
public final class ExternalBindings {
    private final Map<Name, Binding> variables;

    public ExternalBindings() {
        this.variables = new LinkedHashMap<>();
    }

    private ExternalBindings(Map<Name, Binding> variables) {
        this.variables = new LinkedHashMap<>(variables);
    }

    public void bind(Name name, Binding binding) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(binding, "binding");
        if (this.variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " is already bound.");
        }

        this.variables.put(name, binding);
    }

    public void unbind(Name name) {
        this.variables.remove(Objects.requireNonNull(name, "name"));
    }

    public Optional<Binding> get(Name name) {
        return Optional.ofNullable(this.variables.get(Objects.requireNonNull(name, "name")));
    }

    public Set<Name> names() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(this.variables.keySet()));
    }

    public ExternalBindings snapshot() {
        return new ExternalBindings(this.variables);
    }
}
