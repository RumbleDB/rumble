package org.rumbledb.bindings;

import java.io.Serializable;

public sealed interface Binding
        extends
            Serializable
        permits DataFrameBinding, FileBinding, ItemSequenceBinding, LexicalBinding, StandardInputBinding {
}
