/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.items.xml;

import java.io.Serializable;

import org.rumbledb.context.Name;

/** The XML Schema type assigned to an element or attribute by validation. */
public record XmlSchemaTypeAnnotation(Name name, Variety variety, ContentType contentType) implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Variety {
        ANY_SIMPLE,
        ATOMIC,
        LIST,
        UNION,
        COMPLEX
    }

    public enum ContentType {
        NOT_APPLICABLE,
        EMPTY,
        SIMPLE,
        ELEMENT_ONLY,
        MIXED
    }
}
