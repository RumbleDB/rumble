package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

abstract class AbstractFormatFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public AbstractFormatFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
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

        if (language == null) {
            language = getConfiguration().getDefaultFormattingLanguage();
        }

        if (calendar == null) {
            calendar = getConfiguration().getDefaultFormattingCalendar();
        }

        // RumbleDB recognizes place values that are valid Java ZoneIds. Other place
        // representations allowed by the spec, such as ISO country codes, are currently
        // treated as unrecognized and therefore fall back to the dynamic-context default.
        String place = normalizePlace(getOptionalString(placeItem));
        ZoneId placeZoneId = null;

        if (place == null) {
            placeZoneId = getConfiguration().getDefaultFormattingPlace();
            place = placeZoneId.getId();
        } else {
            // Keep the original place string even if it is not a valid Java ZoneId.
            // The XQuery/XPath formatting spec also allows place values such as country codes
            // (for example "us"), which Java's ZoneId does not resolve. Such values may still
            // be useful later for timezone-name selection. Full country-code based place
            // resolution is not implemented here.
            try {
                placeZoneId = ZoneId.of(place);
            } catch (DateTimeException e) {
                placeZoneId = null;
            }
        }

        FormattingOptions formattingOptions = FormattingOptions.fromArguments(
            this.children.size(),
            language,
            calendar,
            place,
            placeZoneId,
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

    private static String normalizePlace(String place) {
        if (place == null) {
            return null;
        }

        String trimmed = place.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    static OffsetDateTime applyConfiguredZone(
            OffsetDateTime value,
            boolean hasExplicitTimezone,
            FormattingOptions options
    ) {
        if (!hasExplicitTimezone || options == null || options.placeZoneId == null) {
            return value;
        }

        return value.toInstant()
                .atZone(options.placeZoneId)
                .toOffsetDateTime();
    }

    protected abstract OffsetDateTime getTemporalValue(Item valueItem);

    protected abstract boolean supportsComponent(char component);

}
