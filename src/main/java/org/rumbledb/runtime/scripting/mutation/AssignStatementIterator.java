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
package org.rumbledb.runtime.scripting.mutation;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.typing.TreatTypeValidator;
import org.rumbledb.types.SequenceType;

public class AssignStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan assignExpression;
    private final Name variableName;
    /** Checks the assigned value against the declared type of the variable, or null if it has none. */
    private final TreatTypeValidator validator;

    public AssignStatementIterator(
            ItemRuntimePlan assignExpression,
            Name variableName,
            SequenceType variableType,
            RuntimeStaticContext staticContext) {
        super(Collections.singletonList(assignExpression), staticContext);
        this.assignExpression = assignExpression;
        this.variableName = variableName;
        this.validator = variableType == null || variableType.equals(SequenceType.createSequenceType("item*"))
                ? null
                : new TreatTypeValidator(variableType, ErrorCode.UnexpectedTypeErrorCode, getMetadata());
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        List<Item> exprItems = this.assignExpression.materialize(context);
        if (this.validator != null) {
            this.validator.resolve(context);
            for (int i = 0; i < exprItems.size(); i++) {
                Item item = exprItems.get(i);
                if (!item.getDynamicType().isResolved()) {
                    item.getDynamicType().resolve(context, getMetadata());
                }
                this.validator.validateItem(item, i + 1);
            }
            this.validator.validateEmpty(exprItems.size());
        }
        context.getVariableValues().changeVariableValue(this.variableName, exprItems);
        return null;
    }
}
