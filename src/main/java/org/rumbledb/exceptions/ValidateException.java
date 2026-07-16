/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

/** An error raised while evaluating an XQuery validate expression. */
public final class ValidateException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public ValidateException(String message, ErrorCode errorCode, ExceptionMetadata metadata) {
        super(message, errorCode, metadata);
    }
}
