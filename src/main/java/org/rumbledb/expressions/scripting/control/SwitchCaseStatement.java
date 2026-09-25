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

import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.scripting.statement.Statement;

/**
 * Helper class that organizes children statements of a switch statement.
 * From a tree perspective, all statements in this class are considered to
 * be direct children of the SwitchStatement.
 */
@Getter
public class SwitchCaseStatement {
    private final List<Expression> conditionExpressions;
    private final Statement returnStatement;

    public SwitchCaseStatement(List<Expression> conditionExpressions, Statement returnStatement) {
        this.conditionExpressions = conditionExpressions;
        this.returnStatement = returnStatement;
    }
}
