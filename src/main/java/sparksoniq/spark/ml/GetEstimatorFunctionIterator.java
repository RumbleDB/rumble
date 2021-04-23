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

import org.apache.spark.ml.Estimator;
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
import java.util.List;

public class GetEstimatorFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    public static final List<Name> estimatorFunctionParameterNames = new ArrayList<>(
            Arrays.asList(
                Name.createVariableInDefaultFunctionNamespace(
                    "estimator-input-f6c87df3-fcba-47c7-a5ff-a1a7553b1cab"
                ),
                Name.createVariableInDefaultFunctionNamespace(
                    "estimator-paramobject-ded8adb9-df6f-42b2-b493-863a421a2754"
                )
            )
    );
    private String estimatorShortName;
    private Class<?> estimatorSparkMLClass;

    public GetEstimatorFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        super(arguments, executionMode, metadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator nameIterator = this.children.get(0);
        nameIterator.open(context);
        if (!nameIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. Estimator lookup can't be performed with empty sequence as the transformer name",
                    getMetadata()
            );
        }
        this.estimatorShortName = nameIterator.next().getStringValue();
        if (nameIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Estimator lookup can't be performed on a sequence.",
                    getMetadata()
            );
        }
        nameIterator.close();

        String estimatorFullClassName = RumbleMLCatalog.getEstimatorFullClassName(
            this.estimatorShortName,
            getMetadata()
        );
        try {
            this.estimatorSparkMLClass = Class.forName(estimatorFullClassName);
            this.hasNext = true;
        } catch (ClassNotFoundException e) {
            throw new OurBadException(
                    "No SparkML estimator implementation found with the given full class name."
            );
        }
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            try {
                Estimator<?> estimator = (Estimator<?>) this.estimatorSparkMLClass.newInstance();
                RuntimeIterator bodyIterator = new ApplyEstimatorRuntimeIterator(
                        this.estimatorShortName,
                        estimator,
                        ExecutionMode.LOCAL,
                        getMetadata()
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
                        BuiltinTypesCatalogue.anyFunctionItem,
                        SequenceType.Arity.One
                );

                return new FunctionItem(
                        new FunctionIdentifier(
                                Name.createVariableInDefaultFunctionNamespace(
                                    this.estimatorSparkMLClass.getName()
                                ),
                                2
                        ),
                        estimatorFunctionParameterNames,
                        new FunctionSignature(
                                paramTypes,
                                returnType
                        ),
                        new DynamicContext(this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration()),
                        bodyIterator
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
