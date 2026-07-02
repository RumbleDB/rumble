package org.rumbledb.api.binding;

public interface Binding {
    public enum Kind {
        ITEM_SEQUENCE,
        DATAFRAME
    }

    Kind kind();
}
