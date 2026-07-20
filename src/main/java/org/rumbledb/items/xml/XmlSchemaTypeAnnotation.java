/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.items.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rumbledb.context.Name;
import org.rumbledb.xml.schema.XmlSchemaTypeHierarchy;

/** The XML Schema type assigned to an element or attribute by validation. */
public record XmlSchemaTypeAnnotation(
        Name name,
        List<Name> typeHierarchy,
        Variety variety,
        ContentType contentType) implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final XmlSchemaTypeAnnotation UNTYPED_ELEMENT = new XmlSchemaTypeAnnotation(
            xsName("untyped"),
            XmlSchemaTypeHierarchy.forXdmType(xsName("untyped")),
            Variety.COMPLEX,
            ContentType.MIXED
    );
    private static final XmlSchemaTypeAnnotation ANY_TYPE = new XmlSchemaTypeAnnotation(
            xsName("anyType"),
            List.of(xsName("anyType")),
            Variety.COMPLEX,
            ContentType.MIXED
    );
    private static final XmlSchemaTypeAnnotation UNTYPED_ATTRIBUTE = new XmlSchemaTypeAnnotation(
            xsName("untypedAtomic"),
            XmlSchemaTypeHierarchy.forXdmType(xsName("untypedAtomic")),
            Variety.ATOMIC,
            ContentType.NOT_APPLICABLE
    );

    public XmlSchemaTypeAnnotation {
        typeHierarchy = new ArrayList<>(typeHierarchy);
    }

    @Override
    public List<Name> typeHierarchy() {
        return Collections.unmodifiableList(this.typeHierarchy);
    }

    public boolean isDerivedFrom(Name typeName) {
        return this.typeHierarchy.contains(typeName);
    }

    public static XmlSchemaTypeAnnotation untypedElement() {
        return UNTYPED_ELEMENT;
    }

    public static XmlSchemaTypeAnnotation anyType() {
        return ANY_TYPE;
    }

    public static XmlSchemaTypeAnnotation untypedAttribute() {
        return UNTYPED_ATTRIBUTE;
    }

    private static Name xsName(String localName) {
        return new Name(Name.XS_NS, "xs", localName);
    }

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
