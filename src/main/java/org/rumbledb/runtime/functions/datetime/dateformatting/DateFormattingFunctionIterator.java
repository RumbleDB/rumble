package org.rumbledb.runtime.functions.datetime.dateformatting;

import java.io.Serial;
import java.time.OffsetDateTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.functions.util.formatting.FormattingContext;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

abstract class DateFormattingFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public DateFormattingFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item valueItem = this.getChild(0).materializeFirstOrNull(context);
        Item pictureItem = this.getChild(1).materializeFirstOrNull(context);
        Item languageItem = this.getChildren().size() > 2 ? this.getChild(2).materializeFirstOrNull(context) : null;
        Item calendarItem = this.getChildren().size() > 3 ? this.getChild(3).materializeFirstOrNull(context) : null;
        Item placeItem = this.getChildren().size() > 4 ? this.getChild(4).materializeFirstOrNull(context) : null;

        // If $value is the empty sequence, the functions return the empty sequence
        if (valueItem == null) {
            return null;
        }

        String language = getOptionalString(languageItem);
        String calendar = getOptionalString(calendarItem);
        String place = getOptionalString(placeItem);

        // Ensures that calls with the 2-arity signature behave the same as calls with the 5-arity signature
        // when the last three arguments are empty sequences.
        if (language == null) {
            language = getConfiguration().formatting().defaultFormattingLanguage();
        }
        if (calendar == null) {
            calendar = getConfiguration().formatting().defaultFormattingCalendar();
        }

        FormattingContext formattingContext = FormattingContext.fromArguments(
                language, calendar, place, getRuntimeStaticContext().getStaticallyKnownNamespaces(), getMetadata());

        // Retrieves the individual temporal value used by a date-format function.
        OffsetDateTime temporalValue = getTemporalValue(valueItem);
        boolean hasExplicitTimezone = valueItem.hasTimeZone();

        // Apply the timezone before rendering, if set explicitly.
        OffsetDateTime renderingValue = applyConfiguredZone(temporalValue, hasExplicitTimezone, formattingContext);

        String result = TemporalPictureFormatter.format(
                renderingValue,
                pictureItem.getStringValue(),
                hasExplicitTimezone,
                formattingContext,
                this::supportsComponent,
                getMetadata());

        return ItemFactory.getInstance().createStringItem(result);
    }

    /**
     * Returns the String of a String item, or null if the item holds null or the item reference is null.
     * This function does not check whether the item is a String item.
     */
    private static String getOptionalString(Item item) {
        return item != null && !item.isNull() ? item.getStringValue() : null;
    }

    static OffsetDateTime applyConfiguredZone(
            OffsetDateTime value, boolean hasExplicitTimezone, FormattingContext formattingContext) {
        if (!hasExplicitTimezone || !formattingContext.shouldAdjustToPlaceTimezone()) {
            return value;
        }

        return value.toInstant().atZone(formattingContext.placeZoneId).toOffsetDateTime();
    }

    // Turns a non-castable item into a CastException naming the expected type.
    private OffsetDateTime getTemporalValue(Item valueItem) {
        try {
            return extractTemporalValue(valueItem);
        } catch (UnsupportedOperationException e) {
            CastException ex = new CastException(
                    "\"" + valueItem.serialize() + "\": not castable to type " + temporalTypeName(), getMetadata());
            ex.initCause(e);
            throw ex;
        }
    }

    // Extracts the raw value; throws UnsupportedOperationException if not castable.
    protected abstract OffsetDateTime extractTemporalValue(Item valueItem);

    // Type name for the cast error message: "date", "dateTime" or "time".
    protected abstract String temporalTypeName();

    protected abstract boolean supportsComponent(char component);
}
