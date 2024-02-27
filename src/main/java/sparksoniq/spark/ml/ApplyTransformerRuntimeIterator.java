package sparksoniq.spark.ml;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.MLNotADataFrameException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sparksoniq.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;


public class ApplyTransformerRuntimeIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String transformerShortName;
    private Transformer transformer;

    private JSoundDataFrame inputDataset;
    private Item paramMapItem;
    private List<String> columnNamesOfGeneratedVectors = new ArrayList<>();

    public ApplyTransformerRuntimeIterator(
            String transformerShortName,
            Transformer transformer,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.transformerShortName = transformerShortName;
        this.transformer = transformer;
    }

    public Transformer getTransformer() {
        return this.transformer;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        this.inputDataset = getInputDataset(context);
        this.paramMapItem = getParamMapItem(context);

        processSpecialParamsForVectorization();

        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            this.transformerShortName,
            this.transformer,
            this.paramMapItem,
            getMetadata()
        );

        try {
            Dataset<Row> result = this.transformer.transform(this.inputDataset.getDataFrame(), paramMap);
            for (String name : this.columnNamesOfGeneratedVectors) {
                result = result.drop(name);
            }
            return new JSoundDataFrame(result, BuiltinTypesCatalogue.objectItem);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            if (e.getMessage().matches(".*DecimalType.*is not supported.*")) {
                throw new InvalidRumbleMLParamException(
                        "Parameters provided to "
                            + this.transformerShortName
                            + " causes the following error: "
                            + "Transformer can not operate on data of decimal type given in inputCol. "
                            + "Please try converting the data to double type (eg. with annotate() function). ",
                        getMetadata()
                );
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
                        getMetadata()
                );
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
                        getMetadata()
                );
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
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private JSoundDataFrame getInputDataset(DynamicContext context) {
        Name transformerInputVariableName = GetTransformerFunctionIterator.transformerParameterNames
            .get(0);

        if (!context.getVariableValues().contains(transformerInputVariableName)) {
            throw new OurBadException("Transformer's input data is not available in the dynamic context");
        }

        if (context.getVariableValues().isDataFrame(transformerInputVariableName, getMetadata())) {
            return context.getVariableValues()
                .getDataFrameVariableValue(
                    transformerInputVariableName,
                    getMetadata()
                );
        }

        throw new MLNotADataFrameException(
                "Transformers operate on DataFrames. "
                    +
                    "Please consider using 'annotate' built-in function to generate a DataFrame.",
                getMetadata()
        );
    }

    private Item getParamMapItem(DynamicContext context) {
        List<Item> paramMapItemList = context.getVariableValues()
            .getLocalVariableValue(
                GetTransformerFunctionIterator.transformerParameterNames.get(1),
                getMetadata()
            );
        if (paramMapItemList.size() != 1) {
            throw new OurBadException(
                    "Applying a transformer takes a single object as the second parameter.",
                    getMetadata()
            );
        }
        return paramMapItemList.get(0);
    }

    private void processSpecialParamsForVectorization() {
        // update input dataset and paramMapItem based on the needs of special params
        for (String specialParamName : RumbleMLCatalog.specialParamsThatMayReferToAColumnOfVectors) {
            boolean transformerExpectsSpecialParam = RumbleMLCatalog.getTransformerParams(
                this.transformerShortName,
                getMetadata()
            ).contains(specialParamName);
            if (!transformerExpectsSpecialParam) {
                continue;
            }

            boolean transformerExpectsVector = RumbleMLCatalog
                .shouldTransformerColumnReferencedBySpecialParamContainVectors(
                    this.transformerShortName,
                    specialParamName,
                    getMetadata()
                );

            if (!transformerExpectsVector) {
                continue;
            }

            String[] paramValue = calculateParamValue(specialParamName);
            if (!isVectorizationNeededForParam(specialParamName, paramValue)) {
                continue;
            }

            String columnNameForVectorizationResult = RumbleMLCatalog.getUUIDOfOfSpecialParam(specialParamName);
            this.inputDataset = RumbleMLUtils.createDataFrameContainingVectorizedColumn(
                this.inputDataset,
                specialParamName,
                paramValue,
                columnNameForVectorizationResult,
                getMetadata()
            );
            this.columnNamesOfGeneratedVectors.add(columnNameForVectorizationResult);

            this.setSparkMLTransformerParamToValue(specialParamName, columnNameForVectorizationResult);
        }
    }

    private String[] calculateParamValue(String specialParamName) {
        Item paramValueItem = this.paramMapItem.getItemByKey(specialParamName);
        if (paramValueItem != null) {
            // remove this param from the map to prevent processing the param again
            this.paramMapItem = RumbleMLUtils.removeParameter(this.paramMapItem, specialParamName, getMetadata());

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
                        + this.transformerShortName
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

    private boolean isVectorizationNeededForParam(String specialParamName, String[] paramValue) {
        StructType schema = this.inputDataset.getDataFrame().schema();
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
                        getMetadata()
                );
            }
            return !(columnType instanceof VectorUDT);
        }
        return true;
    }


    private void setSparkMLTransformerParamToValue(String paramName, String value) {
        try {
            this.transformer
                .getClass()
                .getMethod("set" + StringUtils.capitalize(paramName), String.class)
                .invoke(this.transformer, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new OurBadException("Failed to set " + paramName + " on the transformer");
        }
    }
}
