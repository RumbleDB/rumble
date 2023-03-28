package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;

import java.util.List;

public abstract class UpdatePrimitive implements UpdatePrimitiveInterface {

    protected UpdatePrimitiveTarget target;
    protected UpdatePrimitiveSelector selector;
    protected UpdatePrimitiveSource source;

    public UpdatePrimitive(
            UpdatePrimitiveTarget target,
            UpdatePrimitiveSelector selector,
            UpdatePrimitiveSource source
    ) {
        this.target = target;
        this.selector = selector;
        this.source = source;
    }

    public UpdatePrimitive(Item target, Item selector, Item source) {
        this(
            new UpdatePrimitiveTarget(target),
            new UpdatePrimitiveSelector(selector),
            new UpdatePrimitiveSource(source)
        );
    }

    public UpdatePrimitive(Item target, Item selector, List<? extends Item> source) {
        this(
            new UpdatePrimitiveTarget(target),
            new UpdatePrimitiveSelector(selector),
            new UpdatePrimitiveSource(source)
        );
    }

    public UpdatePrimitive(Item target, Item source) {
        this(new UpdatePrimitiveTarget(target), null, new UpdatePrimitiveSource(source));
    }

    public UpdatePrimitive(Item target, List<? extends Item> source) {
        this(new UpdatePrimitiveTarget(target), null, new UpdatePrimitiveSource(source));
    }

    @Override
    public abstract void apply();

    @Override
    public boolean hasSelector() {
        return this.selector == null;
    }

    @Override
    public UpdatePrimitiveTarget getTarget() {
        return target;
    }

    @Override
    public UpdatePrimitiveSelector getSelector() {
        if (!this.hasSelector()) {
            throw new OurBadException("Invalid call to getSelector when selector is null");
        }
        return selector;
    }

    @Override
    public UpdatePrimitiveSource getSource() {
        return source;
    }
}
