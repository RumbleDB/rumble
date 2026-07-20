/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.context;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import javax.xml.validation.Schema;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.xml.schema.XmlSchemaTypeHierarchy;
import org.xml.sax.SAXException;

/** The XML Schema components compiled for an XQuery module. */
public record SchemaCatalog(
        Schema validationSchema,
        XSModel schemaModel,
        List<SchemaDocument> documents) {

    private static final SchemaCatalog BUILT_IN = createBuiltIn();

    public static SchemaCatalog builtIn() {
        return BUILT_IN;
    }

    public XSTypeDefinition getTypeDefinition(Name name) {
        XSTypeDefinition type = this.schemaModel.getTypeDefinition(name.getLocalName(), name.getNamespace());
        return type == null ? getBuiltInTypeDefinition(name) : type;
    }

    public List<Name> getTypeHierarchy(Name name) {
        List<Name> xdmTypeHierarchy = XmlSchemaTypeHierarchy.forXdmType(name);
        if (!xdmTypeHierarchy.isEmpty()) {
            return xdmTypeHierarchy;
        }
        XSTypeDefinition type = getTypeDefinition(name);
        if (type == null) {
            return List.of();
        }
        return XmlSchemaTypeHierarchy.forSchemaType(type, name);
    }

    public Optional<List<Declaration>> getElementDeclarations(Name name) {
        XSElementDeclaration head = this.schemaModel.getElementDeclaration(
            name.getLocalName(),
            schemaNamespace(name)
        );
        if (head == null) {
            return Optional.empty();
        }
        List<Declaration> result = new ArrayList<>();
        addElementDeclaration(result, head);
        XSObjectList substitutionGroup = this.schemaModel.getSubstitutionGroup(head);
        for (int index = 0; index < substitutionGroup.getLength(); index++) {
            addElementDeclaration(result, (XSElementDeclaration) substitutionGroup.item(index));
        }
        return Optional.of(List.copyOf(result));
    }

    public Optional<Declaration> getAttributeDeclaration(Name name) {
        XSAttributeDeclaration declaration = this.schemaModel.getAttributeDeclaration(
            name.getLocalName(),
            schemaNamespace(name)
        );
        if (declaration == null) {
            return Optional.empty();
        }
        return Optional.of(
            new Declaration(
                    new Name(declaration.getNamespace(), null, declaration.getName()),
                    XmlSchemaTypeHierarchy.declaredNameOf(declaration.getTypeDefinition()),
                    false
            )
        );
    }

    private static void addElementDeclaration(List<Declaration> result, XSElementDeclaration declaration) {
        if (!declaration.getAbstract()) {
            result.add(
                new Declaration(
                        new Name(declaration.getNamespace(), null, declaration.getName()),
                        XmlSchemaTypeHierarchy.declaredNameOf(declaration.getTypeDefinition()),
                        declaration.getNillable()
                )
            );
        }
    }

    private static String schemaNamespace(Name name) {
        String namespace = name.getNamespace();
        return namespace == null || namespace.isEmpty() ? null : namespace;
    }

    public record Declaration(Name name, Name typeName, boolean allowsNilled) {
    }

    public record SchemaDocument(URI systemId, byte[] content) implements Serializable {

        private static final long serialVersionUID = 1L;
    }

    private static XSTypeDefinition getBuiltInTypeDefinition(Name name) {
        return Name.XS_NS.equals(name.getNamespace())
            ? SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(name.getLocalName())
            : null;
    }

    private static SchemaCatalog createBuiltIn() {
        try {
            return new SchemaCatalog(
                    new XMLSchemaFactory().newSchema(),
                    SchemaGrammar.SG_SchemaNS.toXSModel(),
                    List.of()
            );
        } catch (SAXException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }
}
