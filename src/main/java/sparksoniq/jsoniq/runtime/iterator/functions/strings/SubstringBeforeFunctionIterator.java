package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class SubstringBeforeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public SubstringBeforeFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            Item stringItem = this.getSingleItemFromIterator(
                this._children.get(0)
            );
            Item substringItem = this.getSingleItemFromIterator(
                this._children.get(1)
            );
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
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring-before function",
                    getMetadata()
            );

    }
}
