package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.typing.TreatTypeValidator;
import org.rumbledb.types.SequenceType;

import lombok.NonNull;
import java.io.Serial;

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
