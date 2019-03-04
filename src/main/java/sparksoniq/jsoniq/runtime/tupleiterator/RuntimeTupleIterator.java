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
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;

public abstract class RuntimeTupleIterator implements RuntimeTupleIteratorInterface, KryoSerializable {
    protected static final String FLOW_EXCEPTION_MESSAGE = "Invalid next() call; ";
    private final IteratorMetadata metadata;
    protected boolean _hasNext;
    protected boolean _isOpen;
    protected RuntimeTupleIterator _child;
    protected DynamicContext _currentDynamicContext;

    protected RuntimeTupleIterator(RuntimeTupleIterator child, IteratorMetadata metadata) {
        this.metadata = metadata;
        this._isOpen = false;
        this._child = child;
    }

    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime tuple iterator cannot be opened twice" + ", this: " +this.toString(), getMetadata());
        this._isOpen = true;
        this._hasNext = true;
        this._currentDynamicContext = context;
    }

    public void close() {
        this._isOpen = false;
        this._child.close();
    }

    public void reset(DynamicContext context) {
        this._hasNext = true;
        this._currentDynamicContext = context;
        this._child.reset(context);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(_hasNext);
        output.writeBoolean(_isOpen);
        kryo.writeObject(output, this._currentDynamicContext);
        kryo.writeObject(output, this._child);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._hasNext = input.readBoolean();
        this._isOpen = input.readBoolean();
        this._currentDynamicContext = kryo.readObject(input, DynamicContext.class);
        this._child = kryo.readObject(input, RuntimeTupleIterator.class);
    }

    public boolean isOpen() {
        return _isOpen;
    }

    public boolean hasNext() {
        return this._hasNext;
    }

    public abstract FlworTuple next();

    public IteratorMetadata getMetadata() {
        return metadata;
    }

    public abstract boolean isRDD();

    public abstract JavaRDD<FlworTuple> getRDD(DynamicContext context);


}
