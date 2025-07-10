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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GetEstimatorFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

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

    public GetEstimatorFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        String estimatorShortName = this.children.get(0).materializeFirstItemOrNull(dynamicContext).getStringValue();
        Item paramMapItem = null;
        if (this.children.size() >= 2) {
            paramMapItem = this.children.get(1).materializeFirstItemOrNull(dynamicContext);
        }

        String estimatorFullClassName = RumbleMLCatalog.getEstimatorFullClassName(
            estimatorShortName,
            getMetadata()
        );

        Class<?> estimatorSparkMLClass = null;
        try {
            estimatorSparkMLClass = Class.forName(estimatorFullClassName);
        } catch (ClassNotFoundException e) {
            throw new OurBadException(
                    estimatorShortName
                        + ": we could not find any estimator with that name. Please check the documentation."
            );
        }

        try {
            Estimator<?> estimator = (Estimator<?>) estimatorSparkMLClass.newInstance();

            if (paramMapItem != null) {
                for (int paramIndex = 0; paramIndex < paramMapItem.getKeys().size(); paramIndex++) {
                    String paramName = paramMapItem.getKeys().get(paramIndex);
                    Item paramValue = paramMapItem.getValues().get(paramIndex);

                    RumbleMLCatalog.validateEstimatorParameterByName(estimatorShortName, paramName, getMetadata());

                    String paramJavaTypeName = RumbleMLCatalog.getJavaTypeNameOfParamByName(paramName, getMetadata());
                    Object paramValueInJava = RumbleMLUtils.convertParamItemToJava(
                        paramName,
                        paramValue,
                        paramJavaTypeName,
                        getMetadata()
                    );

                    estimator.set(paramName, paramValueInJava);
                }
            }

            RuntimeIterator bodyIterator = new ApplyEstimatorRuntimeIterator(
                    estimatorShortName,
                    estimator,
                    new RuntimeStaticContext(
                            getConfiguration(),
                            SequenceType.FUNCTION,
                            ExecutionMode.LOCAL,
                            getMetadata()
                    )
            );
            List<SequenceType> paramTypes = Collections.unmodifiableList(
                Arrays.asList(
                    SequenceType.createSequenceType("object*"),
                    SequenceType.createSequenceType("object")
                )
            );
            SequenceType returnType = SequenceType.createSequenceType("function(object*, object) as object*");

            return new FunctionItem(
                    new FunctionIdentifier(
                            Name.createVariableInDefaultFunctionNamespace(
                                estimatorSparkMLClass.getName()
                            ),
                            2
                    ),
                    estimatorFunctionParameterNames,
                    new FunctionSignature(
                            paramTypes,
                            returnType
                    ),
                    new DynamicContext(dynamicContext.getRumbleRuntimeConfiguration()),
                    bodyIterator
            );

        } catch (InstantiationException | IllegalAccessException e) {
            throw new OurBadException(
                    "Error while generating an instance from the estimator class " + estimatorFullClassName,
                    getMetadata()
            );
        }
    }
}
