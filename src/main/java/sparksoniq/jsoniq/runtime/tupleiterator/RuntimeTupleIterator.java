/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.jsoniq.runtime.tupleiterator;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.ArrayList;
import java.util.List;

public abstract class RuntimeTupleIterator implements RuntimeTupleIteratorInterface, KryoSerializable {
    protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    private final IteratorMetadata metadata;
    protected boolean _hasNext;
    protected boolean _isOpen;
    protected List<RuntimeTupleIterator> _children;
    protected DynamicContext _currentDynamicContext;

    protected RuntimeTupleIterator(List<RuntimeTupleIterator> children, IteratorMetadata metadata) {
        this.metadata = metadata;
        this._isOpen = false;
        this._children = new ArrayList<>();
        if (children != null && !children.isEmpty())
            this._children.addAll(children);
    }

    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime tuple iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._hasNext = true;
        this._currentDynamicContext = context;
    }

    public void close() {
        this._isOpen = false;
        this._children.forEach(c -> c.close());
    }

    public void reset(DynamicContext context) {
        this._hasNext = true;
        this._currentDynamicContext = context;
        this._children.forEach(c -> c.reset(context));
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(_hasNext);
        output.writeBoolean(_isOpen);
        kryo.writeObject(output, this._currentDynamicContext);
        kryo.writeObject(output, this._children);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._hasNext = input.readBoolean();
        this._isOpen = input.readBoolean();
        this._currentDynamicContext = kryo.readObject(input, DynamicContext.class);
        this._children = kryo.readObject(input, ArrayList.class);
    }

    public boolean hasNext() {
        return this._hasNext;
    }

    public boolean isOpen() {
        return _isOpen;
    }

    public IteratorMetadata getMetadata() {
        return metadata;
    }

    public abstract boolean isRDD();

    public abstract JavaRDD<FlworTuple> getRDD();

    public abstract FlworTuple next();

    protected List<FlworTuple> runChildrenIterators(DynamicContext context) {
        List<FlworTuple> values = new ArrayList<>();
        for (RuntimeTupleIterator iterator : this._children) {
            iterator.open(context);
            while (iterator.hasNext())
                values.add(iterator.next());
            iterator.close();
        }
        return values;
    }

    protected List<FlworTuple> getTuplesFromIteratorWithCurrentContext(RuntimeTupleIterator iterator) {
        List<FlworTuple> result = new ArrayList<>();
        iterator.open(_currentDynamicContext);
        while (iterator.hasNext())
            result.add(iterator.next());
        iterator.close();
        return result;
    }

    protected <T extends FlworTuple> T getSingleTupleOfTypeFromIterator(RuntimeTupleIterator iterator, Class<T> type) {
        return getSingleTupleOfTypeFromIterator(iterator, type,
                new SparksoniqRuntimeException("Iterator was expected to return a single tuple but returned a sequence",
                        iterator.getMetadata().getExpressionMetadata()));
    }

    protected <T extends FlworTuple, E extends SparksoniqRuntimeException> T getSingleTupleOfTypeFromIterator(RuntimeTupleIterator iterator,
                                                                                                       Class<T> type, E nonAtomicException) {
        iterator.open(_currentDynamicContext);
        FlworTuple result = null;
        if(iterator.hasNext()) {
            result = iterator.next();
            if (iterator.hasNext()) {
                throw nonAtomicException;
            }
        }
        iterator.close();
        if (result != null && !(type.isInstance(result)))
            throw new UnexpectedTypeException("Invalid tuple type returned by tuple iterator", iterator.getMetadata());
        return (T) result;
    }
}
