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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.MLNotADataFrameException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static sparksoniq.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;


public class ApplyTransformerRuntimeIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String transformerShortName;
    private Transformer transformer;

    private Dataset<Row> inputDataset;
    private Item paramMapItem;
    private List<String> columnNamesOfGeneratedVectors = new ArrayList<>();

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
            Dataset<Row> result = this.transformer.transform(this.inputDataset, paramMap);
            for (String name : this.columnNamesOfGeneratedVectors) {
                result = result.drop(name);
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
        StructType schema = this.inputDataset.schema();
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
