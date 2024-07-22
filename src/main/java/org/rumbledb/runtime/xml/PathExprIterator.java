package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class PathExprIterator extends LocalRuntimeIterator {

    protected PathExprIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
    }

    @Override
    public Item next() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }
}
