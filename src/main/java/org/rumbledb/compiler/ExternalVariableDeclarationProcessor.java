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
package org.rumbledb.compiler;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.bindings.DataFrameBinding;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

/** Adds global declarations implied by external API bindings. */
final class ExternalVariableDeclarationProcessor {

    private ExternalVariableDeclarationProcessor() {}

    /** Returns whether a context-item declaration was synthesized. */
    static boolean process(Prolog prolog, ExternalBindings externalBindings, ExceptionMetadata metadata) {
        boolean addedContextItem = false;
        if (!prolog.hasContextItemDeclaration()
                && getExternalVariableType(externalBindings, Name.CONTEXT_ITEM) != null) {
            prolog.addDeclaration(new VariableDeclaration(
                    Name.CONTEXT_ITEM, true, SequenceType.createSequenceType("item"), null, null, metadata));
            addedContextItem = true;
        }

        for (Name externalVariable : externalBindings.names()) {
            if (externalVariable.equals(Name.CONTEXT_ITEM) || hasDeclaration(prolog, externalVariable)) {
                continue;
            }
            SequenceType sequenceType = getExternalVariableType(externalBindings, externalVariable);
            if (sequenceType != null) {
                prolog.addDeclaration(
                        new VariableDeclaration(externalVariable, true, sequenceType, null, null, metadata));
            }
        }
        return addedContextItem;
    }

    private static boolean hasDeclaration(Prolog prolog, Name variableName) {
        return prolog.getVariableDeclarations().stream()
                .anyMatch(declaration -> declaration.getVariableName().equals(variableName));
    }

    private static SequenceType getExternalVariableType(ExternalBindings externalBindings, Name variableName) {
        DataFrameBinding dataFrameBinding =
                externalBindings.get(variableName, DataFrameBinding.class).orElse(null);
        if (dataFrameBinding != null) {
            Dataset<Row> dataFrame = dataFrameBinding.getDataFrame();
            ItemType itemType = ItemTypeFactory.createItemType(dataFrame.schema());
            return new SequenceType(itemType, SequenceType.Arity.ZeroOrMore);
        }
        return externalBindings.get(variableName).isPresent() ? SequenceType.createSequenceType("item*") : null;
    }
}
