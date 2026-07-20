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

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/** Schema-derived XDM properties shared by element and attribute nodes. */
public record XmlSchemaNodeProperties(
        XmlSchemaTypeAnnotation typeAnnotation,
        Name globalDeclarationName,
        List<Item> typedValue,
        boolean hasTypedValue,
        Nilled nilled,
        boolean id,
        boolean idRefs) implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final XmlSchemaNodeProperties NONE = new XmlSchemaNodeProperties(
            null,
            null,
            List.of(),
            false,
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
        typedValue = new ArrayList<>(typedValue);
    }

    @Override
    public List<Item> typedValue() {
        return Collections.unmodifiableList(this.typedValue);
    }

    public static XmlSchemaNodeProperties none() {
        return NONE;
    }

    public XmlSchemaNodeProperties withSchemaType(
            XmlSchemaTypeAnnotation typeAnnotation,
            List<Item> typedValue
    ) {
        return new XmlSchemaNodeProperties(
                typeAnnotation,
                this.globalDeclarationName,
                typedValue,
                true,
                this.nilled,
                this.id,
                this.idRefs
        );
    }

    public XmlSchemaNodeProperties withSchemaTypeWithoutTypedValue(XmlSchemaTypeAnnotation typeAnnotation) {
        return new XmlSchemaNodeProperties(
                typeAnnotation,
                this.globalDeclarationName,
                List.of(),
                false,
                this.nilled,
                this.id,
                this.idRefs
        );
    }

    public XmlSchemaNodeProperties withNilled(Nilled nilled) {
        return new XmlSchemaNodeProperties(
                this.typeAnnotation,
                this.globalDeclarationName,
                this.typedValue,
                this.hasTypedValue,
                nilled,
                this.id,
                this.idRefs
        );
    }

    public XmlSchemaNodeProperties withIdentityProperties(boolean id, boolean idRefs) {
        return new XmlSchemaNodeProperties(
                this.typeAnnotation,
                this.globalDeclarationName,
                this.typedValue,
                this.hasTypedValue,
                this.nilled,
                id,
                idRefs
        );
    }

    public XmlSchemaNodeProperties withGlobalDeclaration(Name declarationName) {
        return new XmlSchemaNodeProperties(
                this.typeAnnotation,
                declarationName,
                this.typedValue,
                this.hasTypedValue,
                this.nilled,
                this.id,
                this.idRefs
        );
    }

    public void write(Kryo kryo, Output output) {
        kryo.writeClassAndObject(output, this.typeAnnotation);
        kryo.writeObjectOrNull(output, this.globalDeclarationName, Name.class);
        output.writeInt(this.typedValue.size(), true);
        for (Item item : this.typedValue) {
            kryo.writeClassAndObject(output, item);
        }
        output.writeBoolean(this.hasTypedValue);
        output.writeByte(this.nilled.ordinal());
        output.writeBoolean(this.id);
        output.writeBoolean(this.idRefs);
    }

    public static XmlSchemaNodeProperties read(Kryo kryo, Input input) {
        XmlSchemaTypeAnnotation typeAnnotation = (XmlSchemaTypeAnnotation) kryo.readClassAndObject(input);
        Name globalDeclarationName = kryo.readObjectOrNull(input, Name.class);
        int typedValueSize = input.readInt(true);
        List<Item> typedValue = new ArrayList<>(typedValueSize);
        for (int index = 0; index < typedValueSize; index++) {
            typedValue.add((Item) kryo.readClassAndObject(input));
        }
        boolean hasTypedValue = input.readBoolean();
        Nilled nilled = Nilled.values()[input.readByte()];
        return new XmlSchemaNodeProperties(
                typeAnnotation,
                globalDeclarationName,
                typedValue,
                hasTypedValue,
                nilled,
                input.readBoolean(),
                input.readBoolean()
        );
    }
}
