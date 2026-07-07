package org.rumbledb.bindings;

import com.esotericsoftware.kryo.KryoSerializable;

import java.io.Serializable;

public sealed interface Binding
        extends
            Serializable,
            KryoSerializable
        permits DataFrameBinding, FileBinding, ItemSequenceBinding, LexicalBinding, StandardInputBinding {
}
