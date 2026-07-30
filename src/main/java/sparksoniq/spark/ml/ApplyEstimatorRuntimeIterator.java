package sparksoniq.spark.ml;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sparksoniq.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;

public class ApplyEstimatorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String estimatorShortName;
    @Getter
    private final Estimator<?> estimator;

    public ApplyEstimatorRuntimeIterator(
            String estimatorShortName,
            Estimator<?> estimator,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.estimatorShortName = estimatorShortName;
        this.estimator = estimator;
    }

    @Override
    public Item evaluateAtMostOne(
            DynamicContext dynamicContext
    ) {
        EstimatorInputs inputs = new EstimatorInputs(
                getInputDataset(dynamicContext),
                getParamMapItem(dynamicContext)
        );

        processSpecialParamsForVectorization(inputs);

        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            this.estimatorShortName,
            this.estimator,
            inputs.paramMapItem,
            getMetadata()
        );

        Transformer fittedModel;
        try {
            fittedModel = this.estimator.fit(inputs.inputDataset.getDataFrame(), paramMap);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            String message = e.getMessage();
            if (message == null) {
                System.err.println("Exception stack trace:");
                e.printStackTrace();
                RumbleException ex = new InvalidRumbleMLParamException(
                        "Parameters provided to "
                            + this.estimatorShortName
                            + " caused an error with no message."
                            + "Exception class: "
                            + e.getClass().getName()
                            + "\n"
                            + "\n\nWe are happy to give you a few hints:"
                            + "\nBy default, we look for the features used to train the model in the field 'features'."
                            + "\nIf this field does not exist, you can build it with the VectorAssembler transformer by combining the fields you want to include."
                            + "\n\nFor example:"
                            + "\nlet $vector-assembler := get-transformer(\"VectorAssembler\")"
                            + "\nlet $data := $vector-assembler($data, {\"inputCols\" : [ \"age\", \"weight\" ], \"outputCol\" : \"features\" })"
                            + "\n\nIf the features are in your data, but in a different field than 'features', you can specify that different field name with the parameter 'featuresCol' or 'inputCol' (check the documentation of the estimator to be sure) passed to your estimator."
                            + "\n\nIf the error says that it must be of the type struct<type:tinyint,size:int,indices:array<int>,values:array<double>> but was actually something different, then it means you specified a field that is not an assembled features array. You need to use the VectorAssembler to prepare it.",
                        getMetadata()
                );
                ex.initCause(e);
                throw ex;
            }
            Pattern pattern = Pattern.compile("(.* ]) does not exist. Available: (.*)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                RumbleException ex = new InvalidRumbleMLParamException(
                        "There is an issue with the parameters provided to the estimator "
                            + this.estimatorShortName
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
                            + "\nlet $est := get-estimator(\""
                            + this.estimatorShortName
                            + "\")"
                            + "\nlet $model := $est($data-with-features, {\"featuresCol\" : \"features\" }) (: assuming featuresCol is the parameter :)"
                            + "\n\nIf the features are in already your data, you can specify that field name with the parameter 'featuresCol' or 'inputCol' (check the documentation of the estimator to be sure) passed to your estimator.",
                        getMetadata()
                );
                ex.initCause(e);
                throw ex;
            }
            pattern = Pattern.compile("(.*) does not exist. Available: (.*)");
            matcher = pattern.matcher(message);
            if (matcher.find()) {
                RumbleException ex = new InvalidRumbleMLParamException(
                        "There is an issue with the parameters provided to the estimator "
                            + this.estimatorShortName
                            + "."
                            + "\nIt seems you provided a field ("
                            + matcher.group(1)
                            + ") that does not exist"
                            + "\nThe available fields are: "
                            + matcher.group(2),
                        getMetadata()
                );
                ex.initCause(e);
                throw ex;
            }
            pattern = Pattern.compile(
                "requirement failed: Column (.*) must be of type struct<type:tinyint,size:int,indices:array<int>,values:array<double>> but was actually .*"
            );
            matcher = pattern.matcher(message);
            if (matcher.find()) {
                RumbleException ex = new InvalidRumbleMLParamException(
                        "There is an issue with the parameters provided to the estimator "
                            + this.estimatorShortName
                            + "."
                            + "\nIt seems you provided an field that is not an array of features for parameter featuresCol, inputCol or similar."
                            + "\nIf you do not have such a field in your data, then you can build it with the VectorAssembler transformer by combining the fields you want to include."
                            + "\n\nFor example:"
                            + "\nlet $vector-assembler := get-transformer(\"VectorAssembler\")"
                            + "\nlet $data-with-features := $vector-assembler($data, {\"inputCols\" : [ \"age\", \"weight\" ], \"outputCol\" : \"features\" })"
                            + "\n\nand then"
                            + "\nlet $est := get-estimator(\""
                            + this.estimatorShortName
                            + "\")"
                            + "\nlet $model := $est($data-with-features, {\"featuresCol\" : \"features\" }) (: assuming featuresCol is the parameter :)"
                            + "\n\nIf the features are already in your data, you can specify that field name with the parameter 'featuresCol' or 'inputCol' (check the documentation of the estimator to be sure) passed to your estimator.",
                        getMetadata()
                );
                ex.initCause(e);
                throw ex;
            }
            pattern = Pattern.compile(
                "requirement failed: Column (.*) must be of type struct<type:tinyint,size:int,indices:array<int>,values:array<double>> but was actually .*"
            );
            matcher = pattern.matcher(message);
            if (matcher.find()) {
                RumbleException ex = new InvalidRumbleMLParamException(
                        "There is an issue with the parameters provided to the estimator "
                            + this.estimatorShortName
                            + "."
                            + "\nIt seems you provided an field that is not an array of features for parameter featuresCol, inputCol or similar."
                            + "\nIf you do not have such a field in your data, then you can build it with the VectorAssembler transformer by combining the fields you want to include."
                            + "\n\nFor example:"
                            + "\nlet $vector-assembler := get-transformer(\"VectorAssembler\")"
                            + "\nlet $data-with-features := $vector-assembler($data, {\"inputCols\" : [ \"age\", \"weight\" ], \"outputCol\" : \"features\" })"
                            + "\n\nand then"
                            + "\nlet $est := get-estimator(\""
                            + this.estimatorShortName
                            + "\")"
                            + "\nlet $model := $est($data-with-features, {\"featuresCol\" : \"features\" }) (: assuming featuresCol is the parameter :)"
                            + "\n\nIf the features are already in your data, you can specify that field name with the parameter 'featuresCol' or 'inputCol' (check the documentation of the estimator to be sure) passed to your estimator.",
                        getMetadata()
                );
                ex.initCause(e);
                throw ex;
            }
            RumbleException ex = new InvalidRumbleMLParamException(
                    "Parameters provided to "
                        + this.estimatorShortName
                        + " causes the following error: "
                        + e.getMessage()
                        + "\n\nWe are happy to give you a few hints:"
                        + "\nBy default, we look for the features used to train the model in the field 'features'."
                        + "\nIf this field does not exist, you can build it with the VectorAssembler transformer by combining the fields you want to include."
                        + "\n\nFor example:"
                        + "\nlet $vector-assembler := get-transformer(\"VectorAssembler\")"
                        + "\nlet $data := $vector-assembler($data, {\"inputCols\" : [ \"age\", \"weight\" ], \"outputCol\" : \"features\" })"
                        + "\n\nIf the features are in your data, but in a different field than 'features', you can specify that different field name with the parameter 'featuresCol' or 'inputCol' (check the documentation of the estimator to be sure) passed to your estimator."
                        + "\n\nIf the error says that it must be of the type struct<type:tinyint,size:int,indices:array<int>,values:array<double>> but was actually something different, then it means you specified a field that is not an assembled features array. You need to use the VectorAssembler to prepare it.",
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }

        return generateTransformerFunctionItem(fittedModel, dynamicContext);
    }

    private RuntimeDataFrame<Item> getInputDataset(DynamicContext context) {
        Name estimatorInputVariableName = GetEstimatorFunctionIterator.estimatorFunctionParameterNames
            .get(0);
        return RumbleMLUtils.getDataFrameOrInferFromVariable(
            context,
            estimatorInputVariableName,
            this.staticContext,
            getMetadata()
        );
    }

    private Item getParamMapItem(DynamicContext context) {
        List<Item> paramMapItemList = context.getVariableValues()
            .getLocalVariableValue(
                GetEstimatorFunctionIterator.estimatorFunctionParameterNames.get(1),
                getMetadata()
            );
        if (paramMapItemList.size() != 1) {
            throw new OurBadException(
                    "Applying an estimator takes a single object as the second parameter.",
                    getMetadata()
            );
        }
        return paramMapItemList.get(0);
    }

    private void processSpecialParamsForVectorization(EstimatorInputs inputs) {
        // update input dataset and paramMapItem based on the needs of special params
        for (String specialParamName : RumbleMLCatalog.specialParamsThatMayReferToAColumnOfVectors) {
            boolean estimatorExpectsSpecialParam = RumbleMLCatalog.getEstimatorParams(
                this.estimatorShortName,
                getMetadata()
            ).contains(specialParamName);
            if (
                !estimatorExpectsSpecialParam
            ) {
                continue;
            }

            boolean estimatorExpectsVector = RumbleMLCatalog
                .shouldEstimatorColumnReferencedBySpecialParamContainVectors(
                    this.estimatorShortName,
                    specialParamName,
                    getMetadata()
                );

            if (!estimatorExpectsVector) {
                continue;
            }

            String[] paramValue = calculateParamValue(inputs, specialParamName);
            if (!isVectorizationNeededForParam(inputs, specialParamName, paramValue)) {
                continue;
            }

            String columnNameForVectorizationResult = RumbleMLCatalog.getUUIDOfOfSpecialParam(specialParamName);
            inputs.inputDataset = RumbleMLUtils.createDataFrameContainingVectorizedColumn(
                inputs.inputDataset,
                specialParamName,
                paramValue,
                columnNameForVectorizationResult,
                getMetadata()
            );

            this.setSparkMLEstimatorParamToValue(specialParamName, columnNameForVectorizationResult);
        }
    }

    private String[] calculateParamValue(EstimatorInputs inputs, String specialParamName) {
        Item paramValueItem = inputs.paramMapItem.getItemByKey(specialParamName);
        if (paramValueItem != null) {
            // remove this param from the map to prevent processing the param again
            inputs.paramMapItem = RumbleMLUtils.removeParameter(inputs.paramMapItem, specialParamName, getMetadata());

            return (String[]) RumbleMLUtils.convertParamItemToJava(
                specialParamName,
                paramValueItem,
                RumbleMLCatalog.javaTypeNameOfSpecialParams,
                getMetadata()
            );
        }

        if (RumbleMLCatalog.specialParamHasNoDefaultSparkMLValue(specialParamName)) {
            throw new InvalidRumbleMLParamException(
                    "Parameters provided to "
                        + this.estimatorShortName
                        + " causes the following error: "
                        + "Missing parameter value for '"
                        + specialParamName
                        + "'.",
                    getMetadata()
            );
        }

        String defaultSparkMLParamValue = RumbleMLCatalog.getDefaultSparkMLValueOfSpecialParam(specialParamName);
        return new String[] { defaultSparkMLParamValue };
    }

    private boolean isVectorizationNeededForParam(
            EstimatorInputs inputs,
            String specialParamName,
            String[] paramValue
    ) {
        StructType schema = inputs.inputDataset.getDataFrame().schema();
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
                            + this.estimatorShortName
                            + " causes the following error: "
                            + ex.getMessage()
                            + "'.",
                        getMetadata()
                );
            }
            return !(columnType instanceof VectorUDT);
        }
        return true;
    }

    private void setSparkMLEstimatorParamToValue(String paramName, String value) {
        try {
            this.estimator
                .getClass()
                .getMethod("set" + StringUtils.capitalize(paramName), String.class)
                .invoke(this.estimator, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new OurBadException("Failed to set " + paramName + " on the estimator");
        }
    }

    private Item generateTransformerFunctionItem(Transformer fittedModel, DynamicContext dynamicContext) {
        RuntimeIterator bodyIterator = new ApplyTransformerRuntimeIterator(
                RumbleMLCatalog.getRumbleMLShortName(fittedModel.getClass().getName()),
                fittedModel,
                this.staticContext
                    .toBuilder()
                    .staticType(new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.One))
                    .executionMode(ExecutionMode.DATAFRAME)
                    .metadata(getMetadata())
                    .build()
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
                        Name.createVariableInDefaultFunctionNamespace(fittedModel.getClass().getName()),
                        2
                ),
                GetTransformerFunctionIterator.transformerParameterNames,
                new FunctionSignature(
                        paramTypes,
                        returnType
                ),
                new DynamicContext(dynamicContext.getRumbleRuntimeConfiguration()),
                bodyIterator
        );
    }

    private static final class EstimatorInputs {
        private RuntimeDataFrame<Item> inputDataset;
        private Item paramMapItem;

        private EstimatorInputs(RuntimeDataFrame<Item> inputDataset, Item paramMapItem) {
            this.inputDataset = inputDataset;
            this.paramMapItem = paramMapItem;
        }
    }
}
