/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.spark.ml;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;

import lombok.Getter;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;

import static org.rumbledb.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;

public class ApplyTransformerRuntimeIterator extends ItemRuntimePlan implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String transformerShortName;

    @Getter
    private final Transformer transformer;

    /**
     * The inputs of one application of the transformer. They are computed for each evaluation, so that the plan
     * itself is not modified.
     */
    private static final class TransformInput {
        private RuntimeDataFrame<Item> dataset;
        private Item paramMapItem;
        private final List<String> generatedVectorColumns = new ArrayList<>();
        private final Map<String, String> vectorParamValues = new LinkedHashMap<>();

        private TransformInput(RuntimeDataFrame<Item> dataset, Item paramMapItem) {
            this.dataset = dataset;
            this.paramMapItem = paramMapItem;
        }
    }

    public ApplyTransformerRuntimeIterator(
            String transformerShortName, Transformer transformer, RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
        this.transformerShortName = transformerShortName;
        this.transformer = transformer;
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        TransformInput input = new TransformInput(getInputDataset(context), getParamMapItem(context));

        processSpecialParamsForVectorization(input);

        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
                this.transformerShortName, this.transformer, input.paramMapItem, getMetadata());
        // The vectorized columns are passed in the parameter map, which Spark applies to a copy of the transformer,
        // instead of being set on the transformer that this plan holds.
        for (Map.Entry<String, String> entry : input.vectorParamValues.entrySet()) {
            paramMap.put(getSparkMLTransformerParam(entry.getKey()), entry.getValue());
        }

        try {
            Dataset<Row> result = this.transformer.transform(input.dataset.getDataFrame(), paramMap);
            for (String name : input.generatedVectorColumns) {
                result = result.drop(name);
            }
            return new HomogeneousItemDataFrame(result, BuiltinTypesCatalogue.objectItem);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            if (e.getMessage().matches(".*DecimalType.*is not supported.*")) {
                throw new InvalidRumbleMLParamException(
                        "Parameters provided to "
                                + this.transformerShortName
                                + " causes the following error: "
                                + "Transformer can not operate on data of decimal type given in inputCol. "
                                + "Please try converting the data to double type (eg. with annotate() function). ",
                        getMetadata());
            }
            String message = e.getMessage();
            Pattern pattern = Pattern.compile("(.* ]) does not exist. Available: (.*)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                RumbleException ex = new InvalidRumbleMLParamException(
                        "There is an issue with the parameters provided to the transformer "
                                + this.transformerShortName
                                + "."
                                + "\nIt seems you provided an array of strings ("
                                + matcher.group(1)
                                + ") for parameter featuresCol, inputCol or similar."
                                + "\nHowever, this parameter should be a string, which is the name of the field associated with an array of features to train on or to transform."
                                + "\nIf you do not have such a field in your data, then you can build it with the VectorAssembler transformer by combining the fields you want to include."
                                + "\n\nFor example:"
                                + "\nlet $vector-assembler := get-transformer(\"VectorAssembler\")"
                                + "\nlet $data-with-features := $vector-assembler($data, {\"inputCols\" : [ \"age\", \"weight\" ], \"outputCol\" : \"features\" })"
                                + "\n\nand then"
                                + "\nlet $est := get-transformer(\""
                                + this.transformerShortName
                                + "\")"
                                + "\nlet $model := $est($data-with-features, {\"inputCol\" : \"features\" }) (: assuming inputCol is the parameter :)"
                                + "\n\nIf the features are in already your data, you can specify that field name with the parameter 'featuresCol' or 'inputCol' (check the documentation of the estimator to be sure) passed to your estimator.",
                        getMetadata());
                ex.initCause(e);
                throw ex;
            }
            pattern = Pattern.compile("(.*) does not exist. Available: (.*)");
            matcher = pattern.matcher(message);
            if (matcher.find()) {
                RumbleException ex = new InvalidRumbleMLParamException(
                        "There is an issue with the parameters provided to the transformer "
                                + this.transformerShortName
                                + "."
                                + "\nIt seems you provided a field ("
                                + matcher.group(1)
                                + ") that does not exist"
                                + "\nThe available fields are: "
                                + matcher.group(2),
                        getMetadata());
                ex.initCause(e);
                throw ex;
            }
            pattern = Pattern.compile(
                    "requirement failed: Column (.*) must be of type struct<type:tinyint,size:int,indices:array<int>,values:array<double>> but was actually .*");
            matcher = pattern.matcher(message);
            if (matcher.find()) {
                RumbleException ex = new InvalidRumbleMLParamException(
                        "There is an issue with the parameters provided to the transformer "
                                + this.transformerShortName
                                + "."
                                + "\nIt seems you provided an field that is not an array of features for parameter featuresCol, inputCol or similar."
                                + "\nIf you do not have such a field in your data, then you can build it with the VectorAssembler transformer by combining the fields you want to include."
                                + "\n\nFor example:"
                                + "\nlet $vector-assembler := get-transformer(\"VectorAssembler\")"
                                + "\nlet $data-with-features := $vector-assembler($data, {\"inputCols\" : [ \"age\", \"weight\" ], \"outputCol\" : \"features\" })"
                                + "\n\nand then"
                                + "\nlet $est := get-estimator(\""
                                + this.transformerShortName
                                + "\")"
                                + "\nlet $model := $est($data-with-features, {\"inputCol\" : \"features\" }) (: assuming inputCol is the parameter :)"
                                + "\n\nIf the features are already in your data, you can specify that field name with the parameter 'featuresCol' or 'inputCol' (check the documentation of the estimator to be sure) passed to your estimator.",
                        getMetadata());
                ex.initCause(e);
                throw ex;
            }
            RumbleException ex = new InvalidRumbleMLParamException(
                    "Parameters provided to "
                            + this.transformerShortName
                            + " causes the following error: "
                            + e.getMessage()
                            + "\n\nWe are happy to give you a few hints:"
                            + "\nBy default, we look for the features used to apply the model in the field 'features'."
                            + "\nIf this field does not exist, you can build it with the VectorAssembler transformer by combining the fields you want to include."
                            + "\n\nFor example:"
                            + "\nlet $vector-assembler := get-transformer(\"VectorAssembler\")"
                            + "\nlet $data := $vector-assembler($data, {\"inputCols\" : [ \"age\", \"weight\" ], \"outputCol\" : \"features\" })"
                            + "\n\nIf the features are in your data, but in a different field than 'features', you can specify that different field name with the parameter 'featuresCol' or 'inputCol' (check the documentation of the estimator to be sure) passed to your estimator."
                            + "\n\nIf the error says that it must be of the type struct<type:tinyint,size:int,indices:array<int>,values:array<double>> but was actually something different, then it means you specified a field that is not an assembled features array. You need to use the VectorAssembler to prepare it.",
                    getMetadata());
            ex.initCause(e);
            throw ex;
        }
    }

    private RuntimeDataFrame<Item> getInputDataset(DynamicContext context) {
        Name transformerInputVariableName = GetTransformerFunctionIterator.transformerParameterNames.get(0);
        return RumbleMLUtils.getDataFrameOrInferFromVariable(
                context, transformerInputVariableName, this.staticContext, getMetadata());
    }

    private Item getParamMapItem(DynamicContext context) {
        List<Item> paramMapItemList = context.getVariableValues()
                .getLocalVariableValue(GetTransformerFunctionIterator.transformerParameterNames.get(1), getMetadata());
        if (paramMapItemList.size() != 1) {
            throw new OurBadException(
                    "Applying a transformer takes a single object as the second parameter.", getMetadata());
        }
        return paramMapItemList.get(0);
    }

    private void processSpecialParamsForVectorization(TransformInput input) {
        // update input dataset and paramMapItem based on the needs of special params
        for (String specialParamName : RumbleMLCatalog.specialParamsThatMayReferToAColumnOfVectors) {
            boolean transformerExpectsSpecialParam = RumbleMLCatalog.getTransformerParams(
                            this.transformerShortName, getMetadata())
                    .contains(specialParamName);
            if (!transformerExpectsSpecialParam) {
                continue;
            }

            boolean transformerExpectsVector =
                    RumbleMLCatalog.shouldTransformerColumnReferencedBySpecialParamContainVectors(
                            this.transformerShortName, specialParamName, getMetadata());

            if (!transformerExpectsVector) {
                continue;
            }

            String[] paramValue = calculateParamValue(input, specialParamName);
            if (!isVectorizationNeededForParam(input, specialParamName, paramValue)) {
                continue;
            }

            String columnNameForVectorizationResult = RumbleMLCatalog.getUUIDOfOfSpecialParam(specialParamName);
            input.dataset = RumbleMLUtils.createDataFrameContainingVectorizedColumn(
                    input.dataset, specialParamName, paramValue, columnNameForVectorizationResult, getMetadata());
            input.generatedVectorColumns.add(columnNameForVectorizationResult);
            input.vectorParamValues.put(specialParamName, columnNameForVectorizationResult);
        }
    }

    private String[] calculateParamValue(TransformInput input, String specialParamName) {
        Item paramValueItem = input.paramMapItem.getItemByKey(specialParamName);
        if (paramValueItem != null) {
            // remove this param from the map to prevent processing the param again
            input.paramMapItem = RumbleMLUtils.removeParameter(input.paramMapItem, specialParamName, getMetadata());

            return (String[]) RumbleMLUtils.convertParamItemToJava(
                    specialParamName, paramValueItem, RumbleMLCatalog.javaTypeNameOfSpecialParams, getMetadata());
        }

        if (RumbleMLCatalog.specialParamHasNoDefaultSparkMLValue(specialParamName)) {
            throw new InvalidRumbleMLParamException(
                    "Parameters provided to "
                            + this.transformerShortName
                            + " causes the following error: "
                            + "Missing parameter value for '"
                            + specialParamName
                            + "'.",
                    getMetadata());
        }

        String defaultSparkMLParamValue = RumbleMLCatalog.getDefaultSparkMLValueOfSpecialParam(specialParamName);
        return new String[] {defaultSparkMLParamValue};
    }

    private boolean isVectorizationNeededForParam(TransformInput input, String specialParamName, String[] paramValue) {
        StructType schema = input.dataset.getDataFrame().schema();
        if (paramValue.length == 1) {
            String columnName = paramValue[0];
            DataType columnType;
            try {
                columnType = schema.fields()[schema.fieldIndex(columnName)].dataType();
            } catch (IllegalArgumentException ex) {
                throw new InvalidRumbleMLParamException(
                        "Parameters provided to "
                                + specialParamName
                                + " of "
                                + this.transformerShortName
                                + " causes the following error: "
                                + ex.getMessage()
                                + "'.",
                        getMetadata());
            }
            return !(columnType instanceof VectorUDT);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Param<Object> getSparkMLTransformerParam(String paramName) {
        try {
            return (Param<Object>) this.transformer.getParam(paramName);
        } catch (NoSuchElementException e) {
            throw new OurBadException("Failed to set " + paramName + " on the transformer");
        }
    }
}
