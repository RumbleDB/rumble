package sparksoniq.spark.ml;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.MLNotADataFrameException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.NoSuchElementException;

import static sparksoniq.spark.ml.RumbleMLCatalog.specialParams;
import static sparksoniq.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;


public class ApplyTransformerRuntimeIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String transformerShortName;
    private Transformer transformer;

    public ApplyTransformerRuntimeIterator(
            String transformerShortName,
            Transformer transformer,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.transformerShortName = transformerShortName;
        this.transformer = transformer;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        Dataset<Row> inputDataset = getInputDataset(context);
        Item paramMapItem = getParamMapItem(context);

        // update input dataset and paramMapItem based on the needs of special params
        for (String specialParamName : RumbleMLCatalog.specialParams) {
            if (
                !RumbleMLCatalog.getTransformerParams(this.transformerShortName, getMetadata())
                    .contains(specialParamName)
            ) {
                continue;
            }
            boolean shouldVectorizeColumnContents = RumbleMLCatalog
                .shouldTransformerColumnReferencedByParamContainVectors(
                    this.transformerShortName,
                    specialParamName,
                    getMetadata()
                );

            if (shouldVectorizeColumnContents) {
                Object paramValue;
                String javaTypeName = RumbleMLCatalog.getJavaTypeNameOfOfSpecialParam(specialParamName);

                Item paramItemForSpecialParam = paramMapItem.getItemByKey(specialParamName);
                if (paramItemForSpecialParam == null) {
                    if (RumbleMLCatalog.specialParamHasNoDefaultvalue(specialParamName)) {
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
                    if (javaTypeName.equals("String[]")) {
                        paramValue = new String[] { RumbleMLCatalog.getDefaultValueOfSpecialParam(specialParamName) };
                    } else {
                        throw new OurBadException(
                                "Unhandled javaTypeName '"
                                    + javaTypeName
                                    + "' found while handling the default value of special param '"
                                    + specialParamName
                                    + "'."
                        );
                    }
                } else {
                    // remove this param from the map to prevent processing the param again
                    paramMapItem = RumbleMLUtils.removeParameter(paramMapItem, specialParamName, getMetadata());

                    paramValue = RumbleMLUtils.convertParamItemToJava(
                        specialParamName,
                        paramItemForSpecialParam,
                        javaTypeName,
                        getMetadata()
                    );
                }

                String nameOfColumnToGenerate = RumbleMLCatalog.getUUIDOfOfSpecialParam(specialParamName);
                inputDataset = RumbleMLUtils.createDataFrameContainingVectorizedColumn(
                    inputDataset,
                    specialParamName,
                    paramValue,
                    nameOfColumnToGenerate,
                    getMetadata()
                );

                this.setTransformerStringParamToValue(specialParamName, nameOfColumnToGenerate);
            }
        }

        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            this.transformerShortName,
            this.transformer,
            paramMapItem,
            getMetadata()
        );

        try {
            Dataset<Row> result = this.transformer.transform(inputDataset, paramMap);

            for (String specialParamName : specialParams) {
                if (
                    !RumbleMLCatalog.getTransformerParams(this.transformerShortName, getMetadata())
                        .contains(specialParamName)
                ) {
                    continue;
                }
                boolean isTemporaryColumnGenerated = RumbleMLCatalog
                    .shouldTransformerColumnReferencedByParamContainVectors(
                        this.transformerShortName,
                        specialParamName,
                        getMetadata()
                    );
                if (isTemporaryColumnGenerated) {
                    String nameOfTemporaryColumn = RumbleMLCatalog.getUUIDOfOfSpecialParam(specialParamName);
                    result = result.drop(nameOfTemporaryColumn);
                }
            }

            return result;
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
            throw new InvalidRumbleMLParamException(
                    "Parameters provided to "
                        + this.transformerShortName
                        + " causes the following error: "
                        + e.getMessage(),
                    getMetadata()
            );
        }
    }

    private Dataset<Row> getInputDataset(DynamicContext context) {
        String transformerInputVariableName = GetTransformerFunctionIterator.transformerParameterNames.get(0);

        if (!context.contains(transformerInputVariableName)) {
            throw new OurBadException("Transformer's input data is not available in the dynamic context");
        }

        if (context.isDataFrame(transformerInputVariableName, getMetadata())) {
            return context.getDataFrameVariableValue(
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
        List<Item> paramMapItemList = context.getLocalVariableValue(
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

    private void setTransformerStringParamToValue(String paramName, String value) {
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
