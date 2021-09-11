package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class SubstringAfterFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public SubstringAfterFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item stringItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        Item substringItem = this.children.get(1)
            .materializeFirstItemOrNull(context);
        if (this.children.size() == 3) {
            String collation = this.children.get(2).materializeFirstItemOrNull(context).getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new UnsupportedCollationException("Wrong collation parameter", getMetadata());
            }
        }
        if (stringItem == null || stringItem.getStringValue().isEmpty()) {
            return ItemFactory.getInstance().createStringItem("");
        }
        if (substringItem == null || substringItem.getStringValue().isEmpty()) {
            return ItemFactory.getInstance().createStringItem(stringItem.getStringValue());
        }

        int indexOfOccurrence = stringItem.getStringValue().indexOf(substringItem.getStringValue());
        return indexOfOccurrence == -1
            ? ItemFactory.getInstance().createStringItem("")
            : ItemFactory.getInstance()
                .createStringItem(
                    stringItem.getStringValue()
                        .substring(
                            indexOfOccurrence + substringItem.getStringValue().length()
                        )
                );
    }

}
