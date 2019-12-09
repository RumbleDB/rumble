/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package sparksoniq.spark.udf;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.api.java.UDF2;
import org.rumbledb.api.Item;
import scala.collection.mutable.WrappedArray;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LetClauseUDF implements UDF2<WrappedArray<byte[]>, WrappedArray<Long>, byte[]> {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _expression;

    List<String> _binaryColumnNames;
    List<String> _longColumnNames;

    private List<List<Item>> _deserializedParams;
    private List<Item> _longParams;
    private DynamicContext _parentContext;
    private DynamicContext _context;
    private List<Item> _nextResult;

    private transient Kryo _kryo;
    private transient Output _output;
    private transient Input _input;

    public LetClauseUDF(
            RuntimeIterator expression,
            DynamicContext context,
            List<String> binaryColumnNames,
            List<String> longColumnNames
    ) {
        _expression = expression;

        _deserializedParams = new ArrayList<>();
        _longParams = new ArrayList<>();
        _parentContext = context;
        _context = new DynamicContext(_parentContext);
        _nextResult = new ArrayList<>();

        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _output = new Output(128, -1);
        _input = new Input();

        _binaryColumnNames = binaryColumnNames;
        _longColumnNames = longColumnNames;
    }


    @Override
    public byte[] call(WrappedArray<byte[]> wrappedParameters, WrappedArray<Long> wrappedParametersLong) {
        _deserializedParams.clear();
        _longParams.clear();
        _context.removeAllVariables();
        _nextResult.clear();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams, _kryo, _input);

        // Long parameters correspond to pre-computed counts, when a materialization of the
        // actual sequence was avoided upfront.
        Object[] longParams = (Object[]) wrappedParametersLong.array();
        for (Object longParam : longParams) {
            Item count = ItemFactory.getInstance().createIntegerItem(((Long) longParam).intValue());
            _longParams.add(count);
        }

        DataFrameUtils.prepareDynamicContext(
            _context,
            _binaryColumnNames,
            _longColumnNames,
            _deserializedParams,
            _longParams
        );

        // apply expression in the dynamic context
        _expression.open(_context);
        while (_expression.hasNext()) {
            Item nextItem = _expression.next();
            _nextResult.add(nextItem);
        }
        _expression.close();

        return DataFrameUtils.serializeItemList(_nextResult, _kryo, _output);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _output = new Output(128, -1);
        _input = new Input();
    }

}
