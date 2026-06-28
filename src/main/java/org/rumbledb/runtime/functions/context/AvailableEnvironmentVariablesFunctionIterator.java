package org.rumbledb.runtime.functions.context;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.ArrayList;
import java.util.List;

public class AvailableEnvironmentVariablesFunctionIterator extends LocalFunctionCallIterator {
    private static final long serialVersionUID = 1L;
    private List<String> names;
    private int index;

    public AvailableEnvironmentVariablesFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.names = new ArrayList<>(System.getenv().keySet());
        this.index = 0;
        this.hasNext = !this.names.isEmpty();
    }

    @Override
    public void close() {
        super.close();
        this.names = null;
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.index = 0;
        this.hasNext = this.names != null && !this.names.isEmpty();
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    FLOW_EXCEPTION_MESSAGE + "available-environment-variables function",
                    getMetadata()
            );
        }
        Item result = ItemFactory.getInstance().createStringItem(this.names.get(this.index));
        this.index++;
        this.hasNext = this.index < this.names.size();
        return result;
    }
}
