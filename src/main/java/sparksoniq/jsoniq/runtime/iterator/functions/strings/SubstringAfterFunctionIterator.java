package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class SubstringAfterFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public SubstringAfterFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            IteratorMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            Item stringItem = this._children.get(0).materializeFirstItemOrNull(_currentDynamicContextForLocalExecution);
            Item substringItem = this._children.get(1)
                .materializeFirstItemOrNull(_currentDynamicContextForLocalExecution);

            if (stringItem == null || stringItem.getStringValue().isEmpty())
                return ItemFactory.getInstance().createStringItem("");
            if (substringItem == null || substringItem.getStringValue().isEmpty())
                return ItemFactory.getInstance().createStringItem(stringItem.getStringValue());

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
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring-after function",
                    getMetadata()
            );

    }
}
