package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnimplementedFunctionException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class ContainsTokenFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public ContainsTokenFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        if (this.children.size() == 3) {
            throw new UnimplementedFunctionException("fn:contains-token#3", getMetadata());
        }

        String token = getNormalizedToken(context);
        if (token.isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }

        return ItemFactory.getInstance().createBooleanItem(inputContainsToken(context, token));
    }

    private String getNormalizedToken(DynamicContext context) {
        Item tokenItem = this.children.get(1).materializeFirstItemOrNull(context);
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

    private boolean inputContainsToken(DynamicContext context, String token) {
        RuntimeIterator inputIterator = this.children.get(0);
        inputIterator.open(context);
        try {
            return iteratorContainsToken(inputIterator, token);
        } finally {
            inputIterator.close();
        }
    }

    private boolean iteratorContainsToken(RuntimeIterator inputIterator, String token) {
        while (inputIterator.hasNext()) {
            Item inputItem = inputIterator.next();
            String[] inputTokens = inputItem.getStringValue().split("[\\t\\n\\r ]+");
            if (isTokenInSequence(inputTokens, token))
                return true;
        }
        return false;
    }

    private static boolean isTokenInSequence(String[] inputTokens, String token) {
        for (String inputToken : inputTokens) {
            if (inputToken.equals(token)) {
                return true;
            }
        }
        return false;
    }
}
