package sparksoniq.spark.ml;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.ml.Estimator;
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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static sparksoniq.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;


public class ApplyEstimatorRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String estimatorShortName;
    private Estimator<?> estimator;

    private Dataset<Row> inputDataset;
    private Item paramMapItem;

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

        this.inputDataset = getInputDataset(this.currentDynamicContextForLocalExecution);
        this.paramMapItem = getParamMapItem(this.currentDynamicContextForLocalExecution);

        processSpecialParamsForVectorization();

        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            this.estimatorShortName,
            this.estimator,
            this.paramMapItem,
            getMetadata()
        );

        Transformer fittedModel;
        try {
            fittedModel = this.estimator.fit(this.inputDataset, paramMap);
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

    private void processSpecialParamsForVectorization() {
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

            this.setSparkMLEstimatorParamToValue(specialParamName, columnNameForVectorizationResult);
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
}
