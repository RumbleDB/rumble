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
package org.rumbledb.expressions.flowr;

import lombok.Getter;
import lombok.Setter;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.types.SequenceType;

public class GroupByVariableDeclaration {

    @Getter
    protected Name variableName;

    @Getter
    protected Expression expression;

    protected SequenceType sequenceType;

    @Getter
    private final ExceptionMetadata variableMetadata;

    // The type visible immediately after this grouping binding, independent of later clauses.
    @Getter
    @Setter
    private SequenceType variableSequenceType;

    @Getter
    protected String collationURI;

    public GroupByVariableDeclaration(
            Name variableName,
            SequenceType sequenceType,
            Expression expression,
            String collationURI,
            ExceptionMetadata variableMetadata) {
        if (variableName == null) {
            throw new IllegalArgumentException("Flowr var decls cannot be empty");
        }
        this.variableName = variableName;
        this.variableMetadata = variableMetadata;
        this.sequenceType = sequenceType;
        this.expression = expression;
        this.collationURI = collationURI;
    }

    public SequenceType getActualSequenceType() {
        return this.sequenceType;
    }
}
