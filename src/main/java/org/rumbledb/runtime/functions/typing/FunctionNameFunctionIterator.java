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
package org.rumbledb.runtime.functions.typing;

import java.io.Serial;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.SequenceType;

@Log4j2
public class FunctionNameFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public FunctionNameFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        validateStaticType();
        return evaluate(this.getChild(0).materializeFirstOrNull(context));
    }

    private void validateStaticType() {
        if (!this.getChild(0)
                .getRuntimeStaticContext()
                .getStaticType()
                .isSubtypeOf(SequenceType.createSequenceType("function"))) {
            throw new UnexpectedTypeException(
                    "fn:function-name expects a function item, found "
                            + this.getChild(0).getRuntimeStaticContext().getStaticType(),
                    getMetadata());
        }
    }

    private Item evaluate(Item functionItem) {
        if (!(functionItem instanceof FunctionItem function)) {
            throw new OurBadException("Expected argument to be of type function and not be null");
        }
        if (function.getIdentifier() == null || function.getIdentifier().getName() == null) {
            return null;
        }
        Name name = function.getIdentifier().getName();
        return ItemFactory.getInstance().createQNameItem(name);
    }
}
