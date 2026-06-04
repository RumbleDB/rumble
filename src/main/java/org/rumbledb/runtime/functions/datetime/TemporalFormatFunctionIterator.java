package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.time.OffsetDateTime;
import java.util.List;

abstract class TemporalFormatFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public TemporalFormatFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item valueItem = this.children.get(0).materializeFirstItemOrNull(context);
        Item pictureItem = this.children.get(1).materializeFirstItemOrNull(context);

        Item languageItem = this.children.size() > 2
            ? this.children.get(2).materializeFirstItemOrNull(context)
            : null;
        Item calendarItem = this.children.size() > 3
            ? this.children.get(3).materializeFirstItemOrNull(context)
            : null;
        Item placeItem = this.children.size() > 4
            ? this.children.get(4).materializeFirstItemOrNull(context)
            : null;
        if (valueItem == null || pictureItem == null) {
            return null;
        }
        if (valueItem.isNull()) {
            return valueItem;
        }

        String language = getOptionalString(languageItem);
        String calendar = getOptionalString(calendarItem);
        String place = getOptionalString(placeItem);

        if (language == null) {
            language = getConfiguration().getDefaultFormattingLanguage();
        }

        if (calendar == null) {
            calendar = getConfiguration().getDefaultFormattingCalendar();
        }

        FormattingOptions formattingOptions = FormattingOptions.fromArguments(
            language,
            calendar,
            place,
            getRuntimeStaticContext().getStaticallyKnownNamespaces(),
            getMetadata()
        );


        OffsetDateTime temporalValue = getTemporalValue(valueItem);
        boolean hasExplicitTimezone = valueItem.hasTimeZone();

        OffsetDateTime renderingValue = applyConfiguredZone(
            temporalValue,
            hasExplicitTimezone,
            formattingOptions
        );

        String result = TemporalPictureFormatter.format(
            renderingValue,
            pictureItem.getStringValue(),
            hasExplicitTimezone,
            formattingOptions,
            this::supportsComponent,
            getMetadata()
        );

        return ItemFactory.getInstance().createStringItem(result);
    }

    private static String getOptionalString(Item item) {
        return item != null && !item.isNull()
            ? item.getStringValue()
            : null;
    }

    static OffsetDateTime applyConfiguredZone(
            OffsetDateTime value,
            boolean hasExplicitTimezone,
            FormattingOptions options
    ) {
        if (
            !hasExplicitTimezone
                || options == null
                || !options.shouldAdjustToPlaceTimezone()
        ) {
            return value;
        }

        return value.toInstant()
            .atZone(options.placeZoneId)
            .toOffsetDateTime();
    }

    protected abstract OffsetDateTime getTemporalValue(Item valueItem);

    protected abstract boolean supportsComponent(char component);

}
