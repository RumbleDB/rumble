package org.rumbledb.errorcodes;

import java.util.Collections;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.context.VariableValues;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.SequenceType;

public final class ErrorVariables {
    public static final Name ERROR_CODE = new Name(Name.ERROR_NS, "err", "code");
    public static final Name ERROR_DESCRIPTION = new Name(Name.ERROR_NS, "err", "description");
    public static final Name ERROR_VALUE = new Name(Name.ERROR_NS, "err", "value");
    public static final Name ERROR_MODULE = new Name(Name.ERROR_NS, "err", "module");
    public static final Name ERROR_LINE_NUMBER = new Name(Name.ERROR_NS, "err", "line-number");
    public static final Name ERROR_COLUMN_NUMBER = new Name(Name.ERROR_NS, "err", "column-number");
    public static final Name ERROR_ADITIONAL = new Name(Name.ERROR_NS, "err", "additional");

    public static void injectStaticContext(StaticContext context, ExceptionMetadata metadata) {
        context.addVariable(
                ErrorVariables.ERROR_CODE,
                SequenceType.createSequenceType("QName"),
                metadata);
        context.addVariable(
                ErrorVariables.ERROR_DESCRIPTION,
                SequenceType.createSequenceType("string?"),
                metadata);
        context.addVariable(
                ErrorVariables.ERROR_VALUE,
                SequenceType.createSequenceType("item*"),
                metadata);
        context.addVariable(
                ErrorVariables.ERROR_MODULE,
                SequenceType.createSequenceType("string?"),
                metadata);
        context.addVariable(
                ErrorVariables.ERROR_LINE_NUMBER,
                SequenceType.createSequenceType("integer?"),
                metadata);
        context.addVariable(
                ErrorVariables.ERROR_COLUMN_NUMBER,
                SequenceType.createSequenceType("integer?"),
                metadata);
        context.addVariable(
                ErrorVariables.ERROR_ADITIONAL,
                SequenceType.createSequenceType("item*"),
                metadata);
    }

    public static void injectDynamicContext(DynamicContext context, RumbleException exception) {
        VariableValues variableValues = context.getVariableValues();
        ItemFactory itemFactory = ItemFactory.getInstance();

        variableValues.addVariableValue(
                ErrorVariables.ERROR_CODE,
                Collections
                        .singletonList(itemFactory.createQNameItem(exception.getErrorCode().getName())));
        variableValues.addVariableValue(
                ErrorVariables.ERROR_DESCRIPTION,
                Collections.singletonList(itemFactory.createStringItem(exception.getMessage())));
        variableValues.addVariableValue(
                ErrorVariables.ERROR_VALUE,
                exception.getErrorValue());
        variableValues.addVariableValue(
                ErrorVariables.ERROR_MODULE,
                Collections.singletonList(itemFactory.createStringItem(exception.getMetadata().getLocation())));
        variableValues.addVariableValue(
                ErrorVariables.ERROR_LINE_NUMBER,
                Collections.singletonList(itemFactory.createIntItem(exception.getMetadata().getTokenLineNumber())));
        variableValues.addVariableValue(
                ErrorVariables.ERROR_COLUMN_NUMBER,
                Collections.singletonList(itemFactory.createIntItem(exception.getMetadata().getTokenColumnNumber())));

        /// Value of err:additional is implementation-defined. For now, we set it to an
        /// empty sequence, but in the future, it could be used to provide additional
        /// information about the error.
        variableValues.addVariableValue(
                ErrorVariables.ERROR_ADITIONAL,
                Collections.emptyList());
    }
}
