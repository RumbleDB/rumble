package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;

import java.util.List;

public abstract class UpdatePrimitive implements UpdatePrimitiveInterface {

    protected UpdatePrimitiveTarget target;

    protected UpdatePrimitiveSource source;

    public UpdatePrimitive(UpdatePrimitiveTarget target, UpdatePrimitiveSource source) {
        this.target = target;
        this.source = source;
    }

    public UpdatePrimitive(Item target, Item locator, Item source) {
        this(new UpdatePrimitiveTarget(target), new UpdatePrimitiveSource(source, locator));
    }

    public UpdatePrimitive(Item target, Item locator, List<? extends Item> source) {
        this(new UpdatePrimitiveTarget(target), new UpdatePrimitiveSource(source, locator));
    }

    public UpdatePrimitive(Item target, Item source) {
        this(new UpdatePrimitiveTarget(target), new UpdatePrimitiveSource(source));
    }

    public UpdatePrimitive(Item target, List<? extends Item> source) {
        this(new UpdatePrimitiveTarget(target), new UpdatePrimitiveSource(source));
    }

    @Override
    public abstract void apply();

    @Override
    public UpdatePrimitiveTarget getTarget() {
        return target;
    }

    @Override
    public UpdatePrimitiveSource getSource() {
        return source;
    }
}
