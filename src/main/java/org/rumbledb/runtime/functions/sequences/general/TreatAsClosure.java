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

import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.typing.TreatTypeValidator;
import org.rumbledb.types.SequenceType;

public class TreatAsClosure implements Function<Item, Boolean> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final TreatTypeValidator validator;

    public TreatAsClosure(SequenceType sequenceType, ErrorCode errorCode, ExceptionMetadata metadata) {
        this(new TreatTypeValidator(sequenceType, errorCode, metadata));
    }

    public TreatAsClosure(@NonNull TreatTypeValidator validator) {
        this.validator = validator;
    }

    @Override
    public Boolean call(Item input) {
        this.validator.validateItem(input, 1);
        return true;
    }
}
