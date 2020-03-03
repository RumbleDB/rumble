package sparksoniq.spark.ml;

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

import java.util.List;

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

        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            this.transformerShortName,
            this.transformer,
            paramMapItem,
            getMetadata()
        );

        try {
            return this.transformer.transform(inputDataset, paramMap);
        } catch (IllegalArgumentException e) {
            throw new InvalidRumbleMLParamException(
                    "Parameter provided to "
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
}
