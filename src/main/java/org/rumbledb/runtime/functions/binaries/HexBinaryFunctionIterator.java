package org.rumbledb.runtime.functions.binaries;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.ItemType;

import java.util.List;

public class HexBinaryFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item item = null;

    public HexBinaryFunctionIterator(
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
            if (
                InstanceOfIterator.doesItemTypeMatchItem(ItemType.base64BinaryItem, this.item)
                    || InstanceOfIterator.doesItemTypeMatchItem(ItemType.hexBinaryItem, this.item)
                    || InstanceOfIterator.doesItemTypeMatchItem(ItemType.stringItem, this.item)
            ) {
                try {
                    return this.item.castAs(ItemType.hexBinaryItem);
                } catch (Exception e) {
                    String message = String.format(
                        "\"%s\": value of type %s is not castable to type %s: ",
                        this.item.serialize(),
                        this.item.getDynamicType(),
                        "hexBinary"
                    );
                    throw new CastException(message, getMetadata());
                }
            }
            String message = String.format(
                "\"%s\": value of type %s is not castable to type %s: ",
                this.item.serialize(),
                this.item.getDynamicType(),
                "hexBinary"
            );
            throw new UnexpectedTypeException(message, getMetadata());
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " hexBinary function",
                getMetadata()
        );
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.item = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.item != null;
    }
}
