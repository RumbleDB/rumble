/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.SchemaCatalog;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.items.xml.XmlSchemaNodeProperties;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/** A statically resolved global element or attribute declaration test. */
final class XmlSchemaDeclarationConstraint implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    enum Kind {
        ELEMENT,
        ATTRIBUTE
    }

    private Kind kind;
    private Name declarationName;
    private List<DeclarationMatch> declarations;

    public XmlSchemaDeclarationConstraint() {
    }

    XmlSchemaDeclarationConstraint(Kind kind, Name declarationName) {
        this.kind = kind;
        this.declarationName = declarationName;
    }

    Name getDeclarationName() {
        return this.declarationName;
    }

    boolean matches(Item item) {
        if (this.kind == Kind.ELEMENT && !item.isElementNode()) {
            return false;
        }
        if (this.kind == Kind.ATTRIBUTE && !item.isAttributeNode()) {
            return false;
        }
        DeclarationMatch declaration = declarationFor(item.nodeName());
        if (declaration == null) {
            return false;
        }
        XmlSchemaNodeProperties properties = item.getXmlSchemaProperties();
        XmlSchemaTypeAnnotation annotation = properties.typeAnnotation() == null
            ? defaultAnnotation()
            : properties.typeAnnotation();
        boolean typeMatches = declaration.typeName() == null
            ? declaration.name().equals(properties.globalDeclarationName())
            : annotation.isDerivedFrom(declaration.typeName());
        return typeMatches
            && (declaration.allowsNilled() || properties.nilled() != XmlSchemaNodeProperties.Nilled.TRUE);
    }

    boolean isSubtypeOf(XmlSchemaDeclarationConstraint other) {
        if (this.kind != other.kind) {
            return false;
        }
        Set<Name> otherNames = new HashSet<>();
        for (DeclarationMatch declaration : other.declarations) {
            otherNames.add(declaration.name());
        }
        return this.declarations.stream().map(DeclarationMatch::name).allMatch(otherNames::contains);
    }

    boolean isResolved() {
        return this.declarations != null;
    }

    void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (isResolved()) {
            return;
        }
        SchemaCatalog catalog = context.getSchemaCatalog();
        this.declarations = switch (this.kind) {
            case ELEMENT -> resolveElementDeclarations(catalog, metadata);
            case ATTRIBUTE -> resolveAttributeDeclaration(catalog, metadata);
        };
    }

    void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!isResolved()) {
            throw new OurBadException("An XML Schema declaration constraint must be resolved statically.", metadata);
        }
    }

    private List<DeclarationMatch> resolveElementDeclarations(
            SchemaCatalog catalog,
            ExceptionMetadata metadata
    ) {
        if (catalog == null) {
            throw unknownDeclaration(metadata);
        }
        return catalog.getElementDeclarations(this.declarationName)
            .orElseThrow(() -> unknownDeclaration(metadata))
            .stream()
            .map(XmlSchemaDeclarationConstraint::declarationMatch)
            .toList();
    }

    private List<DeclarationMatch> resolveAttributeDeclaration(
            SchemaCatalog catalog,
            ExceptionMetadata metadata
    ) {
        if (catalog == null) {
            throw unknownDeclaration(metadata);
        }
        return List.of(
            declarationMatch(
                catalog.getAttributeDeclaration(this.declarationName)
                    .orElseThrow(() -> unknownDeclaration(metadata))
            )
        );
    }

    private static DeclarationMatch declarationMatch(SchemaCatalog.Declaration declaration) {
        return new DeclarationMatch(
                declaration.name(),
                declaration.typeName(),
                declaration.allowsNilled()
        );
    }

    private DeclarationMatch declarationFor(Name name) {
        for (DeclarationMatch declaration : this.declarations) {
            if (declaration.name().equals(name)) {
                return declaration;
            }
        }
        return null;
    }

    private XmlSchemaTypeAnnotation defaultAnnotation() {
        return this.kind == Kind.ELEMENT
            ? XmlSchemaTypeAnnotation.untypedElement()
            : XmlSchemaTypeAnnotation.untypedAttribute();
    }

    private SemanticException unknownDeclaration(ExceptionMetadata metadata) {
        return new SemanticException(
                "Schema " + this.kind.name().toLowerCase() + " declaration is not defined: " + this.declarationName,
                ErrorCode.UndeclaredVariableErrorCode,
                metadata
        );
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeByte(this.kind.ordinal());
        kryo.writeObject(output, this.declarationName);
        output.writeBoolean(this.declarations != null);
        if (this.declarations == null) {
            return;
        }
        output.writeInt(this.declarations.size(), true);
        for (DeclarationMatch declaration : this.declarations) {
            kryo.writeObject(output, declaration.name());
            kryo.writeObjectOrNull(output, declaration.typeName(), Name.class);
            output.writeBoolean(declaration.allowsNilled());
        }
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.kind = Kind.values()[input.readByte()];
        this.declarationName = kryo.readObject(input, Name.class);
        if (!input.readBoolean()) {
            this.declarations = null;
            return;
        }
        int size = input.readInt(true);
        List<DeclarationMatch> result = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            result.add(
                new DeclarationMatch(
                        kryo.readObject(input, Name.class),
                        kryo.readObjectOrNull(input, Name.class),
                        input.readBoolean()
                )
            );
        }
        this.declarations = List.copyOf(result);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof XmlSchemaDeclarationConstraint other
            && this.kind == other.kind
            && this.declarationName.equals(other.declarationName);
    }

    @Override
    public int hashCode() {
        return 31 * this.kind.hashCode() + this.declarationName.hashCode();
    }

    private record DeclarationMatch(Name name, Name typeName, boolean allowsNilled) implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}
