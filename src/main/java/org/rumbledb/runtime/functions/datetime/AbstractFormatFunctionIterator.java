package org.rumbledb.runtime.functions.datetime;

import java.time.OffsetDateTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

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

        FormattingOptions formattingOptions = FormattingOptionsResolver.resolve(
            this.children.size(),
            languageItem,
            calendarItem,
            placeItem,
            pictureItem.serialize(),
            getMetadata()
        );

        OffsetDateTime temporalValue = getTemporalValue(valueItem);
        boolean hasExplicitTimezone = valueItem.hasTimeZone();

        String result = TemporalPictureFormatter.format(
            temporalValue,
            pictureItem.getStringValue(),
            pictureItem.serialize(),
            hasExplicitTimezone,
            formattingOptions,
            this::supportsComponent,
            getMetadata()
        );

        return ItemFactory.getInstance().createStringItem(result);
    }

    protected abstract OffsetDateTime getTemporalValue(Item valueItem);

    protected abstract boolean supportsComponent(char component);
}
