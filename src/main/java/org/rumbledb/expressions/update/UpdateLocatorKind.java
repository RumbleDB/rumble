package org.rumbledb.expressions.update;

public enum UpdateLocatorKind {
    OBJECT_LOOKUP,
    ARRAY_LOOKUP;

    public boolean isObjectLookup() {
        return this == UpdateLocatorKind.OBJECT_LOOKUP;
    }

    public boolean isArrayLookup() {
        return this == UpdateLocatorKind.ARRAY_LOOKUP;
    }

    public String toString() {
        switch (this) {
            case OBJECT_LOOKUP:
                return "object_lookup";
            case ARRAY_LOOKUP:
                return "array_lookup";
        }
        return null;
    }
}
