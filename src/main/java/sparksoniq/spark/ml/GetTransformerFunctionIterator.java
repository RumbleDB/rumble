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

import org.apache.spark.ml.Transformer;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTransformerFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    public static final List<Name> transformerParameterNames = new ArrayList<>(
            Arrays.asList(
                Name.createVariableInDefaultFunctionNamespace(
                    "transformer-input-9470aa1b-13cb-405b-b598-910cb2d18224"
                ),
                Name.createVariableInDefaultFunctionNamespace(
                    "transformer-paramobject-e05c895c-be12-4df1-8a86-8b90f10a7129"
                )
            )
    );
    private String transformerShortName;
    private Class<?> transformerSparkMLClass;

    public GetTransformerFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator nameIterator = this.children.get(0);
        nameIterator.open(context);
        if (!nameIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. Transformer lookup can't be performed with empty sequence as the transformer name",
                    getMetadata()
            );
        }
        this.transformerShortName = nameIterator.next().getStringValue();
        if (nameIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Transformer lookup can't be performed on a sequence.",
                    getMetadata()
            );
        }
        nameIterator.close();

        String transformerFullClassName = RumbleMLCatalog.getTransformerFullClassName(
            this.transformerShortName,
            getMetadata()
        );
        try {
            this.transformerSparkMLClass = Class.forName(transformerFullClassName);
            this.hasNext = true;
        } catch (ClassNotFoundException e) {
            throw new OurBadException(
                    "No SparkML transformer implementation found with the given full class name."
            );
        }
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            try {
                Transformer transformer = (Transformer) this.transformerSparkMLClass.newInstance();
                Map<Long, RuntimeIterator> bodyIterators = new HashMap<>();
                bodyIterators.put(
                    0L,
                    new ApplyTransformerRuntimeIterator(
                            this.transformerShortName,
                            transformer,
                            ExecutionMode.LOCAL,
                            getMetadata()
                    )
                );
                bodyIterators.put(
                    1L,
                    new ApplyTransformerRuntimeIterator(
                            this.transformerShortName,
                            transformer,
                            ExecutionMode.DATAFRAME,
                            getMetadata()
                    )
                );
                List<SequenceType> paramTypes = Collections.unmodifiableList(
                    Arrays.asList(
                        new SequenceType(
                                BuiltinTypesCatalogue.item, // TODO: revert back to ObjectItem
                                SequenceType.Arity.ZeroOrMore
                        ),
                        new SequenceType(
                                BuiltinTypesCatalogue.objectItem,
                                SequenceType.Arity.One
                        )
                    )
                );
                SequenceType returnType = new SequenceType(
                        BuiltinTypesCatalogue.objectItem,
                        SequenceType.Arity.ZeroOrMore
                );

                return new FunctionItem(
                        new FunctionIdentifier(
                                Name.createVariableInDefaultFunctionNamespace(
                                    this.transformerSparkMLClass.getName()
                                ),
                                2
                        ),
                        transformerParameterNames,
                        new FunctionSignature(
                                paramTypes,
                                returnType
                        ),
                        new DynamicContext(this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration()),
                        bodyIterators
                );

            } catch (InstantiationException | IllegalAccessException e) {
                throw new OurBadException("Error while generating an instance from transformer class.", getMetadata());
            }
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "get-transformer function",
                getMetadata()
        );
    }
}
