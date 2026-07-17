/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.context;

import java.util.List;

import javax.xml.validation.Schema;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.xml.schema.XmlSchemaTypeHierarchy;
import org.xml.sax.SAXException;

/** The XML Schema components compiled for an XQuery module. */
public record SchemaCatalog(Schema validationSchema, XSModel schemaModel) {

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

    private static XSTypeDefinition getBuiltInTypeDefinition(Name name) {
        return Name.XS_NS.equals(name.getNamespace())
            ? SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(name.getLocalName())
            : null;
    }

    private static SchemaCatalog createBuiltIn() {
        try {
            return new SchemaCatalog(
                    new XMLSchemaFactory().newSchema(),
                    SchemaGrammar.SG_SchemaNS.toXSModel()
            );
        } catch (SAXException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }
}
