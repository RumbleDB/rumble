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
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
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
import java.util.NoSuchElementException;

public class GetTransformerFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

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

    public GetTransformerFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        String transformerShortName = this.children.get(0).materializeFirstItemOrNull(dynamicContext).getStringValue();
        Item paramMapItem = null;
        if (this.children.size() >= 2) {
            paramMapItem = this.children.get(1).materializeFirstItemOrNull(dynamicContext);
        }

        String transformerFullClassName = RumbleMLCatalog.getTransformerFullClassName(
            transformerShortName,
            getMetadata()
        );

        Class<?> transformerSparkMLClass = null;
        try {
            transformerSparkMLClass = Class.forName(transformerFullClassName);
        } catch (ClassNotFoundException e) {
            throw new OurBadException(
                    transformerShortName
                        + ": we could not find any transformer with that name. Please check the documentation."
            );
        }

        try {
            Transformer transformer = (Transformer) transformerSparkMLClass.newInstance();

            if (paramMapItem != null) {
                for (int paramIndex = 0; paramIndex < paramMapItem.getKeys().size(); paramIndex++) {
                    String paramName = paramMapItem.getKeys().get(paramIndex);
                    Item paramValue = paramMapItem.getValues().get(paramIndex);

                    RumbleMLCatalog.validateTransformerParameterByName(transformerShortName, paramName, getMetadata());

                    String paramJavaTypeName = RumbleMLCatalog.getJavaTypeNameOfParamByName(paramName, getMetadata());
                    Object paramValueInJava = RumbleMLUtils.convertParamItemToJava(
                        paramName,
                        paramValue,
                        paramJavaTypeName,
                        getMetadata()
                    );

                    System.err.println("Setting " + paramJavaTypeName + " to " + paramValueInJava);
                    try {
                        transformer.set(paramName, paramValueInJava);
                    } catch (NoSuchElementException e) {
                        RumbleException ex = new OurBadException(
                                "Error in a parameter for transformer " + transformerShortName + ": " + e.getMessage(),
                                getMetadata()
                        );
                        ex.initCause(e);
                    }
                }
            }
            RuntimeIterator bodyIterator = new ApplyTransformerRuntimeIterator(
                    transformerShortName,
                    transformer,
                    ExecutionMode.DATAFRAME,
                    getMetadata()
            );
            List<SequenceType> paramTypes = Collections.unmodifiableList(
                Arrays.asList(
                    SequenceType.createSequenceType("object*"),
                    SequenceType.createSequenceType("object")
                )
            );
            SequenceType returnType = SequenceType.createSequenceType("object*");

            return new FunctionItem(
                    new FunctionIdentifier(
                            Name.createVariableInDefaultFunctionNamespace(
                                transformerSparkMLClass.getName()
                            ),
                            2
                    ),
                    transformerParameterNames,
                    new FunctionSignature(
                            paramTypes,
                            returnType
                    ),
                    new DynamicContext(this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration()),
                    bodyIterator
            );

        } catch (InstantiationException | IllegalAccessException e) {
            throw new OurBadException(
                    "Error while generating an instance from transformer class + " + transformerFullClassName,
                    getMetadata()
            );
        }
    }
}
