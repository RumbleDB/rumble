package sparksoniq.spark.ml;

import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.OurBadException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.DataFrameRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

import static sparksoniq.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;


public class ApplyTransformerRuntimeIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String _transformerShortName;
    private Transformer _transformer;

    public ApplyTransformerRuntimeIterator(
            String transformerShortName,
            Transformer transformer,
            ExecutionMode executionMode,
            IteratorMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this._transformerShortName = transformerShortName;
        this._transformer = transformer;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        Dataset<Row> inputDataset = context.getDataFrameVariableValue(
            GetTransformerFunctionIterator.transformerParameterNames.get(0),
            getMetadata()
        );
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
        Item paramMapItem = paramMapItemList.get(0);
        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            _transformerShortName,
            _transformer,
            paramMapItem,
            getMetadata()
        );

        try {
            return _transformer.transform(inputDataset, paramMap);
        } catch (IllegalArgumentException e) {
            throw new InvalidRumbleMLParamException(
                    "Parameter provided to " + _transformerShortName + " causes the following error: " + e.getMessage(),
                    getMetadata()
            );
        }
    }
}
