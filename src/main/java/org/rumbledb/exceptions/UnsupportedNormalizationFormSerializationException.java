package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

/**
 * Exception for SESU0011: the serializer does not support the requested normalization form.
 */
public class UnsupportedNormalizationFormSerializationException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnsupportedNormalizationFormSerializationException(String normalizationForm) {
        super(
            "The serializer does not support the requested normalization-form: " + normalizationForm,
            ErrorCode.UnsupportedNormalizationFormSerialization
        );
    }
}
