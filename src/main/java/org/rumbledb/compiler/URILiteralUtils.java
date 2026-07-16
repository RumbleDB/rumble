/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.compiler;

import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidURILiteralException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.net.URI;

final class URILiteralUtils {

    private URILiteralUtils() {
    }

    static URI resolve(URI baseURI, String literal, ExceptionMetadata metadata) {
        try {
            return FileSystemUtil.resolveURI(baseURI, literal, metadata);
        } catch (CannotRetrieveResourceException exception) {
            InvalidURILiteralException result = new InvalidURILiteralException(
                    "Invalid URI literal: " + literal,
                    metadata
            );
            result.initCause(exception);
            throw result;
        }
    }
}
