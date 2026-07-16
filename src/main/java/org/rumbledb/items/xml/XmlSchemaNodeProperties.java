/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.items.xml;

import java.io.Serializable;

import org.rumbledb.types.ItemType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/** Schema-derived XDM properties shared by element and attribute nodes. */
public record XmlSchemaNodeProperties(
        ItemType typeAnnotation,
        Nilled nilled,
        boolean id,
        boolean idRefs) implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final XmlSchemaNodeProperties NONE = new XmlSchemaNodeProperties(
            null,
            Nilled.ABSENT,
            false,
            false
    );

    public enum Nilled {
        ABSENT,
        FALSE,
        TRUE
    }

    public XmlSchemaNodeProperties {
        if (nilled == null) {
            throw new IllegalArgumentException("The nilled property must not be null.");
        }
    }

    public static XmlSchemaNodeProperties none() {
        return NONE;
    }

    public XmlSchemaNodeProperties withTypeAnnotation(ItemType typeAnnotation) {
        return new XmlSchemaNodeProperties(typeAnnotation, this.nilled, this.id, this.idRefs);
    }

    public XmlSchemaNodeProperties withNilled(Nilled nilled) {
        return new XmlSchemaNodeProperties(this.typeAnnotation, nilled, this.id, this.idRefs);
    }

    public XmlSchemaNodeProperties withIdentityProperties(boolean id, boolean idRefs) {
        return new XmlSchemaNodeProperties(this.typeAnnotation, this.nilled, id, idRefs);
    }

    public void write(Kryo kryo, Output output) {
        kryo.writeClassAndObject(output, this.typeAnnotation);
        output.writeByte(this.nilled.ordinal());
        output.writeBoolean(this.id);
        output.writeBoolean(this.idRefs);
    }

    public static XmlSchemaNodeProperties read(Kryo kryo, Input input) {
        ItemType typeAnnotation = (ItemType) kryo.readClassAndObject(input);
        Nilled nilled = Nilled.values()[input.readByte()];
        return new XmlSchemaNodeProperties(
                typeAnnotation,
                nilled,
                input.readBoolean(),
                input.readBoolean()
        );
    }
}
