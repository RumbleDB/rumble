package org.rumbledb.expressions.scripting.annotations;

import org.rumbledb.context.Name;

public class AnnotationConstants {
    public static final Name SEQUENTIAL = new Name(Name.AN_NS, "an", "sequential");
    public static final Name NON_SEQUENTIAL = new Name(Name.AN_NS, "an", "nonsequential");

    public static final Name ASSIGNABLE = new Name(Name.AN_NS, "an", "assignable");

    public static final Name NON_ASSIGNABLE = new Name(Name.AN_NS, "an", "nonassignable");
}
