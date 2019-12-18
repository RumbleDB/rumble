package sparksoniq.spark.ml;

import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.InvalidRumbleMLParamException;
import sparksoniq.exceptions.OurBadException;
import sparksoniq.jsoniq.runtime.iterator.DataFrameRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

import static sparksoniq.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;


public class ApplyTransformerRuntimeIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String _transformerName;
    private Class<?> _transformerSparkMLClass;

    public ApplyTransformerRuntimeIterator(
            String transformerName,
            Class<?> transformerSparkMLClass,
            IteratorMetadata iteratorMetadata
    ) {
        super(null, iteratorMetadata);
        this._transformerName = transformerName;
        this._transformerSparkMLClass = transformerSparkMLClass;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        try {
            Transformer transformer = (Transformer) _transformerSparkMLClass.newInstance();

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
                _transformerSparkMLClass,
                _transformerName,
                transformer,
                paramMapItem,
                getMetadata()
            );
            return transformer.transform(inputDataset, paramMap);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new OurBadException("Error while generating an instance from transformer class.", getMetadata());
        } catch (IllegalArgumentException e) {
            throw new InvalidRumbleMLParamException(
                    "Parameter provided to " + _transformerName + " causes the following error: " + e.getMessage(),
                    getMetadata()
            );
        }
    }
}
