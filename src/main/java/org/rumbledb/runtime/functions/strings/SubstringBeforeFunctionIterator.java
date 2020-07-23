package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class SubstringBeforeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public SubstringBeforeFunctionIterator(
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
            Item stringItem = this.children.get(0)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            Item substringItem = this.children.get(1)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            if (
                substringItem == null
                    || substringItem.getStringValue().isEmpty()
                    ||
                    stringItem == null
                    || stringItem.getStringValue().isEmpty()
            ) {
                return ItemFactory.getInstance().createStringItem("");
            }
            int indexOfOccurrence = stringItem.getStringValue().indexOf(substringItem.getStringValue());
            return indexOfOccurrence == -1
                ? ItemFactory.getInstance().createStringItem("")
                : ItemFactory.getInstance()
                    .createStringItem(
                        stringItem.getStringValue()
                            .substring(
                                0,
                                indexOfOccurrence
                            )
                    );
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring-before function",
                    getMetadata()
            );
        }

    }
}
