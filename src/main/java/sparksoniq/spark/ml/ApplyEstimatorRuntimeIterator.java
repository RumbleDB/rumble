package sparksoniq.spark.ml;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MLNotADataFrameException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.runtime.functions.base.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
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


public class ApplyEstimatorRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String estimatorShortName;
    private Estimator<?> estimator;

    public ApplyEstimatorRuntimeIterator(
            String estimatorShortName,
            Estimator<?> estimator,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        super(null, executionMode, metadata);
        this.estimatorShortName = estimatorShortName;
        this.estimator = estimator;
    }

    @Override
    public Item next() {
        if (!this.hasNext()) {
            throw new IteratorFlowException("Invalid next() call in ApplyEstimatorRuntimeIterator", getMetadata());
        }
        this.hasNext = false;

        Dataset<Row> inputDataset = getInputDataset(this.currentDynamicContextForLocalExecution);
        Item paramMapItem = getParamMapItem(this.currentDynamicContextForLocalExecution);

        boolean estimatorExpectsVectorizedFeaturesColParam = !this.estimatorShortName.equals("RFormula")
            && RumbleMLCatalog
                .getEstimatorParams(this.estimatorShortName, getMetadata())
                .contains(featuresColParamName);

        if (estimatorExpectsVectorizedFeaturesColParam) {
            Object featuresColValue = new String[] { featuresColParamDefaultValue };

            if (paramMapItem.getItemByKey(featuresColParamName) != null) {
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

            this.setEstimatorStringParamToValue(featuresColParamName, rumbleMLNameOfVectorizedFeaturesCol);
        }

        boolean estimatorExpectsVectorizedInputColParam =
            this.estimatorShortName.equals("BucketedRandomProjectionLSH")
                || this.estimatorShortName.equals("IDF")
                || this.estimatorShortName.equals("MaxAbsScaler")
                || this.estimatorShortName.equals("MinHashLSH")
                || this.estimatorShortName.equals("MinMaxScaler")
                || this.estimatorShortName.equals("PCA")
                || this.estimatorShortName.equals("StandardScaler")
                || this.estimatorShortName.equals("VectorIndexer");


        if (estimatorExpectsVectorizedInputColParam) {
            Item inputColParam = paramMapItem.getItemByKey(inputColParamName);
            if (inputColParam == null) {
                throw new InvalidRumbleMLParamException(
                        "Parameters provided to "
                            + this.estimatorShortName
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

            this.setEstimatorStringParamToValue(inputColParamName, rumbleMLNameOfVectorizedInputCol);
        }

        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            this.estimatorShortName,
            this.estimator,
            paramMapItem,
            getMetadata()
        );

        Transformer fittedModel;
        try {
            fittedModel = this.estimator.fit(inputDataset, paramMap);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new InvalidRumbleMLParamException(
                    "Parameters provided to "
                        + this.estimatorShortName
                        + " causes the following error: "
                        + e.getMessage(),
                    getMetadata()
            );
        }

        return generateTransformerFunctionItem(fittedModel);
    }

    private Dataset<Row> getInputDataset(DynamicContext context) {
        String estimatorInputVariableName = GetEstimatorFunctionIterator.estimatorFunctionParameterNames.get(0);

        if (!context.contains(estimatorInputVariableName)) {
            throw new OurBadException("Estimator's input data is not available in the dynamic context");
        }

        if (context.isDataFrame(estimatorInputVariableName, getMetadata())) {
            return context.getDataFrameVariableValue(
                estimatorInputVariableName,
                getMetadata()
            );
        }

        throw new MLNotADataFrameException(
                "Estimators operate on DataFrames. "
                    +
                    "Please consider using 'annotate' built-in function to generate a DataFrame.",
                getMetadata()
        );
    }

    private Item getParamMapItem(DynamicContext context) {
        List<Item> paramMapItemList = context.getLocalVariableValue(
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

    private Item generateTransformerFunctionItem(Transformer fittedModel) {
        RuntimeIterator bodyIterator = new ApplyTransformerRuntimeIterator(
                RumbleMLCatalog.getRumbleMLShortName(fittedModel.getClass().getName()),
                fittedModel,
                ExecutionMode.DATAFRAME,
                getMetadata()
        );
        List<SequenceType> paramTypes = Collections.unmodifiableList(
            Arrays.asList(
                new SequenceType(
                        ItemType.item, // TODO: revert back to ObjectItem
                        SequenceType.Arity.ZeroOrMore
                ),
                new SequenceType(
                        ItemType.objectItem,
                        SequenceType.Arity.One
                )
            )
        );
        SequenceType returnType = new SequenceType(
                ItemType.objectItem,
                SequenceType.Arity.ZeroOrMore
        );

        return new FunctionItem(
                new FunctionIdentifier(fittedModel.getClass().getName(), 2),
                GetTransformerFunctionIterator.transformerParameterNames,
                new FunctionSignature(
                        paramTypes,
                        returnType
                ),
                bodyIterator
        );
    }

    private void setEstimatorStringParamToValue(String paramName, String value) {
        try {
            this.estimator
                .getClass()
                .getMethod("set" + StringUtils.capitalize(paramName), String.class)
                .invoke(this.estimator, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new OurBadException("Failed to set " + paramName + " on the estimator");
        }
    }
}
