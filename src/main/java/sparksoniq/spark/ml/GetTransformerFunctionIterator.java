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

package sparksoniq.spark.ml;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.OurBadException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionSignature;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GetTransformerFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    public static final List<String> transformerParameterNames = new ArrayList<>(Arrays.asList("input", "params"));
    private String _transformerName;
    private Class<?> _transformerSparkMLClass;

    public GetTransformerFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator nameIterator = this._children.get(0);
        nameIterator.open(context);
        if (!nameIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. Transformer lookup can't be performed with empty sequence as the transformer name",
                    getMetadata()
            );
        }
        _transformerName = nameIterator.next().getStringValue();
        if (nameIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Transformer lookup can't be performed on a sequence.",
                    getMetadata()
            );
        }
        nameIterator.close();

        String transformerFullClassName = RumbleMLCatalog.getTransformerFullClassName(
            _transformerName,
            getMetadata()
        );
        try {
            _transformerSparkMLClass = Class.forName(transformerFullClassName);
            this._hasNext = true;
        } catch (ClassNotFoundException e) {
            throw new OurBadException(
                    "Given full class name of the transformer does not match a SparkML implementation."
            );
        }
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;

            RuntimeIterator bodyIterator = new ApplyTransformerRuntimeIterator(
                    _transformerName,
                    _transformerSparkMLClass,
                    ExecutionMode.DATAFRAME,
                    getMetadata()
            );
            List<SequenceType> paramTypes = Collections.unmodifiableList(
                Arrays.asList(
                    new SequenceType(
                            new ItemType(ItemTypes.Item), // TODO: revert back to ObjectItem
                            SequenceType.Arity.ZeroOrMore
                    ),
                    new SequenceType(
                            new ItemType(ItemTypes.ObjectItem),
                            SequenceType.Arity.One
                    )
                )
            );
            SequenceType returnType = new SequenceType(
                    new ItemType(ItemTypes.ObjectItem),
                    SequenceType.Arity.ZeroOrMore
            );

            return new FunctionItem(
                    new FunctionIdentifier(_transformerSparkMLClass.getName(), 2),
                    transformerParameterNames,
                    new FunctionSignature(
                            paramTypes,
                            returnType
                    ),
                    bodyIterator
            );
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "SIZE function",
                getMetadata()
        );
    }
}
