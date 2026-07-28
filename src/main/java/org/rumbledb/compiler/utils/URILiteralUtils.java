/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.compiler.utils;

import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidURILiteralException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.net.URI;
import java.net.URISyntaxException;

public final class URILiteralUtils {

    private static final String XML_WHITESPACE_SEQUENCE = "[\\t\\n\\r ]+";

    private URILiteralUtils() {
    }

    public static String normalizeAsAnyURI(String literal) {
        return literal.replaceAll(XML_WHITESPACE_SEQUENCE, " ").trim();
    }

    public static URI resolve(URI baseURI, String literal, ExceptionMetadata metadata) {
        String normalizedLiteral = normalizeAsAnyURI(literal);
        try {
            return FileSystemUtil.resolveURI(baseURI, normalizedLiteral, metadata);
        } catch (CannotRetrieveResourceException exception) {
            InvalidURILiteralException result = new InvalidURILiteralException(
                    "Invalid URI literal: " + normalizedLiteral,
                    metadata
            );
            result.initCause(exception);
            throw result;
        }
    }

    public static String toStaticBaseUriString(URI resolvedURI, String literal) {
        String normalizedLiteral = normalizeAsAnyURI(literal);
        try {
            URI parsedLiteral = new URI(normalizedLiteral);
            return parsedLiteral.isAbsolute() ? normalizedLiteral : resolvedURI.toString();
        } catch (URISyntaxException e) {
            return normalizedLiteral;
        }
    }
}
