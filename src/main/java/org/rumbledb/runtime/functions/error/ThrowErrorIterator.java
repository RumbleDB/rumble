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
package org.rumbledb.runtime.functions.error;

import java.io.Serial;
import java.util.List;
import java.util.function.Supplier;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ThrowErrorIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ThrowErrorIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Supplier<List<Item>> errorValue =
                this.getChildren().size() == 3 ? () -> this.getChild(2).materialize(context) : List::of;
        if (this.getChildren().isEmpty()) {
            // No argument case.
            throw new RumbleException(
                    "An error has been raised without an error description or code.",
                    ErrorCode.UnidentifiedErrorExceptionCode,
                    this.getRuntimeStaticContext().getMetadata());
        }
        Item errorCodeItem = this.getChild(0).materializeFirstOrNull(context);
        if (errorCodeItem == null) {
            throw new RumbleException(
                    "An error has been raised without an error description or code.",
                    ErrorCode.UnidentifiedErrorExceptionCode,
                    this.getRuntimeStaticContext().getMetadata());
        }
        Name errorCode = errorCodeItem.getQNameValue();
        if (this.getChildren().size() == 1) {
            // Error code argument case.
            throw new RumbleException(
                    "An error has been raised without an error description.",
                    new ErrorCode(errorCode),
                    this.getRuntimeStaticContext().getMetadata());
        }
        String description = this.getChild(1).materializeFirstOrNull(context).getStringValue();
        if (this.getChildren().size() == 2) {
            // Error code and description arguments case.
            throw new RumbleException(
                    description,
                    new ErrorCode(errorCode),
                    this.getRuntimeStaticContext().getMetadata());
        } else {
            // Error code, description, and object case.
            throw new RumbleException(
                    description,
                    new ErrorCode(errorCode),
                    this.getRuntimeStaticContext().getMetadata(),
                    errorValue.get());
        }
    }
}
