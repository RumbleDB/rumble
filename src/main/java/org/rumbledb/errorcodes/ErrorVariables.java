package org.rumbledb.errorcodes;

import org.rumbledb.context.Name;

public final class ErrorVariables {
    public static final Name ERROR_CODE = new Name(Name.ERROR_NS, "err", "code");
    public static final Name ERROR_DESCRIPTION = new Name(Name.ERROR_NS, "err", "description");
    public static final Name ERROR_VALUE = new Name(Name.ERROR_NS, "err", "value");
    public static final Name ERROR_MODULE = new Name(Name.ERROR_NS, "err", "module");
    public static final Name ERROR_LINE_NUMBER = new Name(Name.ERROR_NS, "err", "line-number");
    public static final Name ERROR_COLUMN_NUMBER = new Name(Name.ERROR_NS, "err", "column-number");
    public static final Name ERROR_ADITIONAL = new Name(Name.ERROR_NS, "err", "additional");
}
