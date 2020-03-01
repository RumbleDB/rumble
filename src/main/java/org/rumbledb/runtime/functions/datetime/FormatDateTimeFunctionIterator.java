package org.rumbledb.runtime.functions.datetime;

import org.joda.time.DateTime;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ComponentSpecifierNotAvailableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class FormatDateTimeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item valueDateTimeItem = null;
    private Item pictureStringItem = null;

    public FormatDateTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            try {
                DateTime dateTimeValue = this.valueDateTimeItem.getDateTimeValue();
                String pictureString = this.pictureStringItem.getStringValue();
                StringBuilder sb = new StringBuilder();

                boolean variableMarker = false;
                for (int i = 0; i < pictureString.length(); ++i) {
                    if (variableMarker) {
                        switch (pictureString.charAt(i)) {
                            case 'Y':
                                sb.append(dateTimeValue.getYear());
                                break;
                            case 'M':
                                sb.append(dateTimeValue.getMonthOfYear());
                                break;
                            case 'D':
                                sb.append(dateTimeValue.getDayOfMonth());
                                break;
                            case 'd':
                                sb.append(dateTimeValue.getDayOfYear());
                                break;
                            case 'F':
                                sb.append(dateTimeValue.getDayOfWeek());
                                break;
                            case 'H':
                                sb.append(dateTimeValue.getHourOfDay());
                                break;
                            case 'm':
                                sb.append(dateTimeValue.getMinuteOfHour());
                                break;
                            case 's':
                                sb.append(dateTimeValue.getSecondOfMinute());
                                break;
                            default:
                                String message = String.format(
                                    "\"%s\": a component specifier refers to components"
                                        + "that are not available in the %s type",
                                    this.pictureStringItem.serialize(),
                                    "dateTime"
                                );
                                throw new ComponentSpecifierNotAvailableException(message, getMetadata());
                        }
                        i++;
                        if (i == pictureString.length() || pictureString.charAt(i) != ']') {
                            String message = String.format(
                                "\"%s\": incorrect syntax",
                                this.pictureStringItem.serialize()
                            );
                            throw new IncorrectSyntaxFormatDateTimeException(message, getMetadata());
                        }
                        variableMarker = false;
                    } else {
                        if (pictureString.charAt(i) == '[')
                            variableMarker = true;
                        else
                            sb.append(pictureString.charAt(i));
                    }
                }
                return ItemFactory.getInstance().createStringItem(sb.toString());
            } catch (UnsupportedOperationException | IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": not castable to type %s",
                    this.valueDateTimeItem.serialize(),
                    "dateTime"
                );
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " format-dateTime function",
                    getMetadata()
            );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.valueDateTimeItem = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.pictureStringItem = this.children.get(1)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.valueDateTimeItem != null && this.pictureStringItem != null;
    }
}
