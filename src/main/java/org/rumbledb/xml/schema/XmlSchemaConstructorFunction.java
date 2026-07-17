/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.io.Serializable;
import java.util.List;

import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.SchemaCatalog;
import org.rumbledb.context.StaticContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

/**
 * Describes the constructor function implicitly supplied by an imported XML Schema simple type.
 */
public record XmlSchemaConstructorFunction(
        FunctionIdentifier identifier,
        ItemType resultItemType,
        Variety variety,
        XmlSchemaSimpleTypeValidator validator) implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Variety {
        GENERALIZED_ATOMIC,
        LIST,
        UNION
    }

    public static XmlSchemaConstructorFunction resolve(
            FunctionIdentifier identifier,
            StaticContext staticContext
    ) {
        return staticContext.getXmlSchemaConstructors().get(identifier);
    }

    public static XmlSchemaConstructorFunction createGeneralizedAtomic(
            FunctionIdentifier identifier,
            ItemType resultItemType,
            SchemaCatalog schemaCatalog,
            XSTypeDefinition schemaType
    ) {
        return new XmlSchemaConstructorFunction(
                identifier,
                resultItemType,
                Variety.GENERALIZED_ATOMIC,
                new XmlSchemaSimpleTypeValidator(identifier.getName(), schemaCatalog.documents(), schemaType)
        );
    }

    public static XmlSchemaConstructorFunction createList(
            FunctionIdentifier identifier,
            ItemType itemType,
            SchemaCatalog schemaCatalog,
            XSTypeDefinition schemaType
    ) {
        return new XmlSchemaConstructorFunction(
                identifier,
                itemType,
                Variety.LIST,
                new XmlSchemaSimpleTypeValidator(identifier.getName(), schemaCatalog.documents(), schemaType)
        );
    }

    public static XmlSchemaConstructorFunction createUnion(
            FunctionIdentifier identifier,
            SchemaCatalog schemaCatalog,
            XSTypeDefinition schemaType
    ) {
        return new XmlSchemaConstructorFunction(
                identifier,
                BuiltinTypesCatalogue.atomicItem,
                Variety.UNION,
                new XmlSchemaSimpleTypeValidator(identifier.getName(), schemaCatalog.documents(), schemaType)
        );
    }

    public boolean isGeneralizedAtomic() {
        return this.variety == Variety.GENERALIZED_ATOMIC;
    }

    public FunctionSignature signature() {
        return new FunctionSignature(
                List.of(SequenceType.createSequenceType("anyAtomicType?")),
                new SequenceType(
                        this.resultItemType,
                        isGeneralizedAtomic() ? SequenceType.Arity.OneOrZero : SequenceType.Arity.ZeroOrMore
                )
        );
    }
}
