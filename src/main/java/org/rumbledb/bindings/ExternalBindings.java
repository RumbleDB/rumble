/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.bindings;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.rumbledb.context.Name;

/**
 * Query-scoped external bindings supplied at execution time.
 *
 * This is intentionally separate from {@code RumbleConfiguration}: bindings are execution inputs, not engine
 * configuration. The model is broader than what the current runtime bridge can consume so the public API can settle
 * while the remaining binding kinds are connected to the execution pipeline.
 */
public final class ExternalBindings implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Name, Binding> variables;

    public ExternalBindings() {
        this.variables = new LinkedHashMap<>();
    }

    public static ExternalBindings empty() {
        return new ExternalBindings();
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

    public <T extends Binding> Optional<T> get(Name name, Class<T> bindingClass) {
        return this.get(name).filter(bindingClass::isInstance).map(bindingClass::cast);
    }

    public Set<Name> names() {
        return this.variables.keySet();
    }

    public ExternalBindings snapshot() {
        return new ExternalBindings(this.variables);
    }
}
