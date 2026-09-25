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
package org.rumbledb.expressions.scripting.control;

import java.util.List;

import lombok.Getter;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.types.SequenceType;

/**
 * This is a helper class that organizes the children statements of a TypeSwitchStatement.
 * From a tree perspective, all statements in there are considered
 * to be direct children of the TypeSwitchStatement.
 */
@Getter
public class TypeSwitchStatementCase {
    private final Name variableName;
    private final List<SequenceType> union;
    private final Statement returnStatement;

    public TypeSwitchStatementCase(Name variableName, List<SequenceType> union, Statement returnStatement) {
        this.variableName = variableName;
        this.union = union;
        this.returnStatement = returnStatement;
    }

    public TypeSwitchStatementCase(Name variableName, Statement returnStatement) {
        this.variableName = variableName;
        this.union = null;
        this.returnStatement = returnStatement;
    }
}
