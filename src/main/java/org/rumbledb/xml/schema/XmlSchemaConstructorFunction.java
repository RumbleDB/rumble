/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.List;
import java.io.Serializable;

import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.StaticContext;
import org.rumbledb.context.SchemaCatalog;
import org.rumbledb.context.InScopeSchemaTypes;
import org.apache.xerces.xs.XSTypeDefinition;
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
        LIST
    }

    public static XmlSchemaConstructorFunction resolve(
            FunctionIdentifier identifier,
            StaticContext staticContext
    ) {
        XmlSchemaConstructorFunction registered = staticContext.getXmlSchemaConstructors().get(identifier);
        if (registered != null) {
            return registered;
        }
        return resolve(
            identifier,
            staticContext.getSchemaCatalog(),
            staticContext.getInScopeSchemaTypes()
        );
    }

    public static XmlSchemaConstructorFunction resolve(
            FunctionIdentifier identifier,
            SchemaCatalog schemaCatalog,
            InScopeSchemaTypes schemaTypes
    ) {
        if (
            identifier.getArity() != 1
                || BuiltinTypesCatalogue.typeExists(identifier.getName())
                || schemaCatalog == null
                || schemaTypes == null
        ) {
            return null;
        }
        XSTypeDefinition schemaType = schemaCatalog.getTypeDefinition(identifier.getName());
        if (schemaType == null || schemaType.getTypeCategory() != XSTypeDefinition.SIMPLE_TYPE) {
            return null;
        }
        ItemType targetType = schemaTypes.getInScopeSchemaType(identifier.getName());
        if (
            targetType == null
                || !(targetType.isAtomicItemType()
                    || (targetType.isUnionType()
                        && targetType.getTypes().stream().allMatch(ItemType::isAtomicItemType)))
        ) {
            return null;
        }
        return new XmlSchemaConstructorFunction(
                identifier,
                targetType,
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

    public boolean isList() {
        return this.variety == Variety.LIST;
    }

    public FunctionSignature signature() {
        return new FunctionSignature(
                List.of(SequenceType.createSequenceType("anyAtomicType?")),
                new SequenceType(
                        this.resultItemType,
                        isList() ? SequenceType.Arity.ZeroOrMore : SequenceType.Arity.OneOrZero
                )
        );
    }
}
