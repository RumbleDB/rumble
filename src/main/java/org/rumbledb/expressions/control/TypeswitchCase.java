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
package org.rumbledb.expressions.control;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.types.SequenceType;

/**
 * This is a helper class that organizes the children expressions of a typeswitch expression.
 * From a tree perspective, all expressions in there are considered
 * to be direct children of the TypeswitchExpression.
 */
@Getter
public class TypeswitchCase {

    private final Name variableName;
    private final List<SequenceType> union;
    private final Expression returnExpression;

    public TypeswitchCase(Name variableName, List<SequenceType> union, Expression returnExpression) {
        this.variableName = variableName;
        this.union = new ArrayList<>(union);
        this.returnExpression = returnExpression;
    }

    public TypeswitchCase(Name variableName, Expression returnExpression) {
        this.variableName = variableName;
        this.union = null;
        this.returnExpression = returnExpression;
    }
}
