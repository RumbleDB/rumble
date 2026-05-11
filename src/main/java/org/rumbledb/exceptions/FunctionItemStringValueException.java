/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

/**
 * FOTY0014: no string value for maps, arrays, or function items (e.g. {@code fn:string}).
 */
public class FunctionItemStringValueException extends RumbleException {

    private static final long serialVersionUID = 1L;

    /** Message used when {@code getStringValue()} is invoked on a map, array, or function item. */
    public static final String DEFAULT_MESSAGE =
        "A string value cannot be extracted from a map, array, or function item.";

    public FunctionItemStringValueException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.FunctionItemStringValueErrorCode, metadata);
    }
}
