/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.types;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.SchemaCatalog;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/** A statically resolved XML Schema type restriction used by typed node tests. */
final class XmlSchemaTypeConstraint implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    private Name typeName;
    private List<Name> typeHierarchy;

    public XmlSchemaTypeConstraint() {
    }

    XmlSchemaTypeConstraint(Name typeName) {
        this.typeName = Objects.requireNonNull(typeName, "Schema type name cannot be null.");
    }

    Name getTypeName() {
        return this.typeName;
    }

    boolean matches(XmlSchemaTypeAnnotation annotation) {
        return annotation.isDerivedFrom(this.typeName);
    }

    boolean isSubtypeOf(XmlSchemaTypeConstraint other) {
        return this.typeHierarchy.contains(other.typeName);
    }

    boolean isResolved() {
        return this.typeHierarchy != null;
    }

    void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (isResolved()) {
            return;
        }
        SchemaCatalog catalog = context.getSchemaCatalog();
        this.typeHierarchy = catalog == null ? List.of() : catalog.getTypeHierarchy(this.typeName);
        if (this.typeHierarchy.isEmpty()) {
            throw new SemanticException(
                    "Schema type is not defined: " + this.typeName,
                    ErrorCode.UndeclaredVariableErrorCode,
                    metadata
            );
        }
    }

    void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!isResolved()) {
            throw new OurBadException("An XML Schema type constraint must be resolved statically.", metadata);
        }
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.typeName);
        kryo.writeClassAndObject(output, this.typeHierarchy);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.typeName = kryo.readObject(input, Name.class);
        @SuppressWarnings("unchecked")
        List<Name> hierarchy = (List<Name>) kryo.readClassAndObject(input);
        this.typeHierarchy = hierarchy;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof XmlSchemaTypeConstraint other && this.typeName.equals(other.typeName);
    }

    @Override
    public int hashCode() {
        return this.typeName.hashCode();
    }
}
