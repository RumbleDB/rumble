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
package org.rumbledb.api;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.bindings.DataFrameBinding;
import org.rumbledb.bindings.ItemSequenceBinding;
import org.rumbledb.bindings.LexicalBinding;
import org.rumbledb.context.Name;

public class ExternalBindings {
    private final org.rumbledb.bindings.ExternalBindings bindings;

    public ExternalBindings() {
        this.bindings = org.rumbledb.bindings.ExternalBindings.empty();
    }

    public static ExternalBindings empty() {
        return new ExternalBindings();
    }

    public void bindItem(String variableName, Item item) {
        this.bindItems(variableName, List.of(item));
    }

    public void bindItems(String variableName, List<Item> items) {
        this.bindings.bind(Name.createVariableInNoNamespace(variableName), new ItemSequenceBinding(items));
    }

    public void bindLiteral(String variableName, String value) {
        this.bindings.bind(Name.createVariableInNoNamespace(variableName), new LexicalBinding(value));
    }

    public void bindDataFrame(String variableName, Dataset<Row> dataFrame) {
        this.bindings.bind(Name.createVariableInNoNamespace(variableName), new DataFrameBinding(dataFrame));
    }

    public void unbind(String variableName) {
        this.bindings.unbind(Name.createVariableInNoNamespace(variableName));
    }

    org.rumbledb.bindings.ExternalBindings getInternalBindings() {
        return this.bindings;
    }
}
