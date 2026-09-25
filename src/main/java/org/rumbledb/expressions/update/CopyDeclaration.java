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
package org.rumbledb.expressions.update;

import lombok.Getter;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.types.SequenceType;

@Getter
public class CopyDeclaration {

    private Name variableName;
    private Expression sourceExpression;

    public CopyDeclaration(Name variableName, Expression sourceExpression) {
        if (variableName == null) {
            throw new IllegalArgumentException("Copy clause var decls cannot be empty");
        }
        this.variableName = variableName;
        this.sourceExpression = sourceExpression;
    }

    public SequenceType getSourceSequenceType() {
        if (this.sourceExpression != null && this.sourceExpression.getStaticSequenceType() != null) {
            return this.sourceExpression.getStaticSequenceType();
        }
        return SequenceType.createSequenceType("item*");
    }
}
