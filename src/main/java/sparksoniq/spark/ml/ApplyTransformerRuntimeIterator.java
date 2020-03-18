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

import static sparksoniq.spark.ml.RumbleMLCatalog.featuresColParamDefaultValue;
import static sparksoniq.spark.ml.RumbleMLCatalog.featuresColParamName;
import static sparksoniq.spark.ml.RumbleMLCatalog.inputColParamName;
import static sparksoniq.spark.ml.RumbleMLCatalog.rumbleMLFeaturesColJavaTypeName;
import static sparksoniq.spark.ml.RumbleMLCatalog.rumbleMLInputColJavaTypeName;
import static sparksoniq.spark.ml.RumbleMLCatalog.rumbleMLNameOfVectorizedFeaturesCol;
import static sparksoniq.spark.ml.RumbleMLCatalog.rumbleMLNameOfVectorizedInputCol;
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

        boolean transformerExpectsFeaturesColParam = RumbleMLCatalog
            .getTransformerParams(this.transformerShortName, getMetadata())
            .contains(featuresColParamName);

        boolean isFeaturesColumnGenerated = false;

        if (transformerExpectsFeaturesColParam) {
            Object featuresColValue = new String[] { featuresColParamDefaultValue };
            if (paramMapItem.getItemByKey(featuresColParamName) != null) {
                RumbleMLCatalog.validateTransformerParameterByName(
                    this.transformerShortName,
                    featuresColParamName,
                    getMetadata()
                );

                Item featureColumnsParam = paramMapItem.getItemByKey(featuresColParamName);
                paramMapItem = RumbleMLUtils.removeParameter(paramMapItem, featuresColParamName, getMetadata());

                featuresColValue = RumbleMLUtils.convertParamItemToJava(
                    featuresColParamName,
                    featureColumnsParam,
                    rumbleMLFeaturesColJavaTypeName,
                    getMetadata()
                );
            }

            inputDataset = RumbleMLUtils.generateAndAddVectorizedColumn(
                inputDataset,
                featuresColParamName,
                featuresColValue,
                rumbleMLNameOfVectorizedFeaturesCol,
                getMetadata()
            );
            isFeaturesColumnGenerated = true;

            this.setTransformerStringParamToValue(featuresColParamName, rumbleMLNameOfVectorizedFeaturesCol);
        }

        boolean transformerExpectsVectorizedInputColParam =
            this.transformerShortName.equals("BucketedRandomProjectionLSHModel")
                || this.transformerShortName.equals("IDFModel")
                || this.transformerShortName.equals("MaxAbsScalerModel")
                || this.transformerShortName.equals("MinHashLSHModel")
                || this.transformerShortName.equals("MinMaxScalerModel")
                || this.transformerShortName.equals("PCAModel")
                || this.transformerShortName.equals("StandardScalerModel")
                || this.transformerShortName.equals("VectorIndexerModel")
                || this.transformerShortName.equals("DCT")
                || this.transformerShortName.equals("ElementwiseProduct")
                || this.transformerShortName.equals("Normalizer")
                || this.transformerShortName.equals("PolynomialExpansion")
                || this.transformerShortName.equals("VectorSizeHint")
                || this.transformerShortName.equals("VectorSlicer");

        if (transformerExpectsVectorizedInputColParam) {
            Item inputColParam = paramMapItem.getItemByKey(inputColParamName);
            if (inputColParam == null) {
                throw new InvalidRumbleMLParamException(
                        "Parameters provided to "
                            + this.transformerShortName
                            + " causes the following error: "
                            + "Missing parameter value for '"
                            + inputColParamName
                            + "'.",
                        getMetadata()
                );
            }

            paramMapItem = RumbleMLUtils.removeParameter(paramMapItem, inputColParamName, getMetadata());

            Object inputColValue = RumbleMLUtils.convertParamItemToJava(
                featuresColParamName,
                inputColParam,
                rumbleMLInputColJavaTypeName,
                getMetadata()
            );

            inputDataset = RumbleMLUtils.generateAndAddVectorizedColumn(
                inputDataset,
                inputColParamName,
                inputColValue,
                rumbleMLNameOfVectorizedInputCol,
                getMetadata()
            );

            this.setTransformerStringParamToValue(inputColParamName, rumbleMLNameOfVectorizedInputCol);
        }


        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            this.transformerShortName,
            this.transformer,
            paramMapItem,
            getMetadata()
        );

        try {
            Dataset<Row> result = this.transformer.transform(inputDataset, paramMap);
            if (transformerExpectsFeaturesColParam && isFeaturesColumnGenerated) {
                result = result.drop(rumbleMLNameOfVectorizedFeaturesCol);
            }
            if (transformerExpectsVectorizedInputColParam) {
                result = result.drop(rumbleMLNameOfVectorizedInputCol);
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
