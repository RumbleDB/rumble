/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.runtime.typing;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.TreatException;
import org.rumbledb.exceptions.UnexpectedNodeException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

/**
 * Shared type and cardinality validation for treat-as execution.
 */
public final class TreatTypeValidator implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final SequenceType sequenceType;

    @Getter
    private final ErrorCode errorCode;

    @Getter
    private final ExceptionMetadata metadata;

    public TreatTypeValidator(
            @NonNull SequenceType sequenceType,
            @NonNull ErrorCode errorCode,
            @NonNull ExceptionMetadata metadata
    ) {
        this.sequenceType = sequenceType;
        this.errorCode = errorCode;
        this.metadata = metadata;
    }

    public void resolve(DynamicContext context) {
        if (!this.sequenceType.isResolved()) {
            this.sequenceType.resolve(context, this.metadata);
        }
    }

    public void validateItem(@NonNull Item item, long itemCount) {
        if (this.sequenceType.isEmptySequence()) {
            throw error(item.getDynamicType().toString());
        }
        validateMaximumCardinality(itemCount);
        ItemType itemType = this.sequenceType.getItemType();
        if (!InstanceOfIterator.doesItemTypeMatchItem(itemType, item)) {
            throw error(item.getDynamicType().toString());
        }
    }

    public void validateEmpty(long itemCount) {
        if (
            itemCount == 0
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    || this.sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            throw error("Empty sequence");
        }
    }

    public void validateMaximumCardinality(long itemCount) {
        if (
            itemCount > 1
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    || this.sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            throw error("A sequence of more than one item");
        }
    }

    public RuntimeException error(String type) {
        if (this.errorCode.equals(ErrorCode.DynamicTypeTreatErrorCode)) {
            return new TreatException(
                    type + " cannot be treated as type " + this.sequenceType,
                    this.metadata
            );
        }
        if (this.errorCode.equals(ErrorCode.UnexpectedTypeErrorCode)) {
            return new UnexpectedTypeException(
                    type + " is not expected here. The expected type is " + this.sequenceType,
                    this.metadata
            );
        }
        if (this.errorCode.equals(ErrorCode.InvalidInstance)) {
            return new InvalidInstanceException(
                    "Invalid instance because of arity mismatch. The expected arity is "
                        + this.sequenceType.getArity(),
                    this.metadata
            );
        }
        if (this.errorCode.equals(ErrorCode.UnexpectedNode)) {
            return new UnexpectedNodeException(
                    type + " is not expected here. The expected type is " + this.sequenceType,
                    this.metadata
            );
        }
        return new OurBadException("Unexpected error code in treat as iterator.", this.metadata);
    }
}
