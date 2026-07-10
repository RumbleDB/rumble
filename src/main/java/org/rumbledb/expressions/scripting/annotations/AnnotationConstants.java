package org.rumbledb.expressions.scripting.annotations;

import org.rumbledb.context.Name;

public class AnnotationConstants {
    public static final Name PUBLIC = new Name(Name.XQUERY_ANNOTATIONS_NS, "", "public");
    public static final Name PRIVATE = new Name(Name.XQUERY_ANNOTATIONS_NS, "", "private");
    public static final Name SEQUENTIAL = new Name(Name.JSONIQ_ANNOTATIONS_NS, "an", "sequential");
    public static final Name NON_SEQUENTIAL = new Name(Name.JSONIQ_ANNOTATIONS_NS, "an", "nonsequential");
    public static final Name UPDATING = new Name(Name.XQUERY_ANNOTATIONS_NS, "", "updating");
    public static final Name SIMPLE = new Name(Name.XQUERY_ANNOTATIONS_NS, "", "simple");

    public static final Name ASSIGNABLE = new Name(Name.JSONIQ_ANNOTATIONS_NS, "an", "assignable");

    public static final Name NON_ASSIGNABLE = new Name(Name.JSONIQ_ANNOTATIONS_NS, "an", "nonassignable");
}
