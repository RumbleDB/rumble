package org.rumbledb.bindings;

public sealed interface Binding
        permits DataFrameBinding, FileBinding, ItemSequenceBinding, LexicalBinding, StandardInputBinding {
}
