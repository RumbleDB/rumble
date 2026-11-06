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
package org.rumbledb.runtime.functions.sequences.general;

import java.io.Serial;

import org.apache.spark.api.java.function.Function;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

public class TypePromotionClosure implements Function<Item, Item> {
    private final String exceptionMessage;
    private final SequenceType sequenceType;
    private final ExceptionMetadata metadata;

    @Serial
    private static final long serialVersionUID = 1L;

    public TypePromotionClosure(String exceptionMessage, SequenceType sequenceType, ExceptionMetadata metadata) {
        this.exceptionMessage = exceptionMessage;
        this.sequenceType = sequenceType;
        this.metadata = metadata;
    }

    @Override
    public Item call(Item input) throws Exception {
        if (input != null && !InstanceOfIterator.doesItemTypeMatchItem(this.sequenceType.getItemType(), input)) {
            if (input.getDynamicType().canBePromotedTo(this.sequenceType.getItemType())) {
                Item result = CastIterator.castItemToType(input, this.sequenceType.getItemType(), this.metadata);
                if (result == null) {
                    throw new OurBadException(
                            "We were not able to promote " + input + " to type " + this.sequenceType.getItemType());
                }
                return result;
            }
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                            + input.getDynamicType().toString()
                            + " cannot be promoted to type "
                            + this.sequenceType.getItemType().toString()
                            + this.sequenceType.getArity().getSymbol(),
                    this.metadata);
        }
        return input;
    }
}
