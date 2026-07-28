package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.util.Comparator;
import java.util.List;

public class ContainsTokenFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ContainsTokenFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(() -> evaluate(context), getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(context);
    }

    private Item evaluate(DynamicContext context) {
        String token = getNormalizedToken(context);
        if (token.isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }

        Comparator<String> comparator = resolveComparator(context);
        return ItemFactory.getInstance().createBooleanItem(cursorContainsToken(context, token, comparator));
    }

    private Comparator<String> resolveComparator(DynamicContext context) {
        if (this.getChildren().size() < 3) {
            return null;
        }
        Item collationItem = this.getChild(2).materializeFirstItemOrNull(context);
        String collationUri = collationItem == null ? null : collationItem.getStringValue();
        return CollationResolver.resolve(collationUri, getMetadata());
    }

    private String getNormalizedToken(DynamicContext context) {
        Item tokenItem = this.getChild(1).materializeFirstItemOrNull(context);
        return trimXmlWhitespace(tokenItem.getStringValue());
    }

    private static String trimXmlWhitespace(String value) {
        int start = 0;
        int end = value.length();
        while (start < end && isXmlWhitespace(value.charAt(start))) {
            start++;
        }
        while (end > start && isXmlWhitespace(value.charAt(end - 1))) {
            end--;
        }
        return value.substring(start, end);
    }

    private static boolean isXmlWhitespace(char character) {
        return character == ' ' || character == '\t' || character == '\n' || character == '\r';
    }

    private boolean cursorContainsToken(
            DynamicContext context,
            String token,
            Comparator<String> comparator
    ) {
        try (LocalCursor<Item> cursor = this.getChild(0).createLocalCursor(context)) {
            while (cursor.hasNext()) {
                String[] inputTokens = cursor.next().getStringValue().split("[\\t\\n\\r ]+");
                if (isTokenInSequence(inputTokens, token, comparator)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean isTokenInSequence(String[] inputTokens, String token, Comparator<String> comparator) {
        for (String inputToken : inputTokens) {
            boolean matches = comparator == null
                ? inputToken.equals(token)
                : comparator.compare(inputToken, token) == 0;
            if (matches) {
                return true;
            }
        }
        return false;
    }
}
