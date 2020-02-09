package sparksoniq.spark.ml;

import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionSignature;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static sparksoniq.spark.ml.RumbleMLUtils.convertRumbleObjectItemToSparkMLParamMap;


public class ApplyEstimatorRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String _estimatorShortName;
    private Estimator<?> _estimator;

    public ApplyEstimatorRuntimeIterator(
            String estimatorShortName,
            Estimator<?> estimator,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        super(null, executionMode, metadata);
        this._estimatorShortName = estimatorShortName;
        this._estimator = estimator;
    }

    @Override
    public Item next() {
        if (!this.hasNext()) {
            throw new IteratorFlowException("Invalid next() call in ApplyEstimatorRuntimeIterator", getMetadata());
        }
        this._hasNext = false;

        Dataset<Row> inputDataset = getInputDataset(_currentDynamicContextForLocalExecution);
        Item paramMapItem = getParamMapItem(_currentDynamicContextForLocalExecution);

        ParamMap paramMap = convertRumbleObjectItemToSparkMLParamMap(
            _estimatorShortName,
            _estimator,
            paramMapItem,
            getMetadata()
        );

        Transformer fittedModel;
        try {
            fittedModel = _estimator.fit(inputDataset, paramMap);
        } catch (IllegalArgumentException e) {
            throw new InvalidRumbleMLParamException(
                    "Parameter provided to " + _estimatorShortName + " causes the following error: " + e.getMessage(),
                    getMetadata()
            );
        }

        return generateTransformerFunctionItem(fittedModel);
    }

    private Dataset<Row> getInputDataset(DynamicContext context) {
        return context.getDataFrameVariableValue(
            GetEstimatorFunctionIterator.estimatorParameterNames.get(0),
            getMetadata()
        );
    }

    private Item getParamMapItem(DynamicContext context) {
        List<Item> paramMapItemList = context.getLocalVariableValue(
            GetEstimatorFunctionIterator.estimatorParameterNames.get(1),
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
                        new ItemType(ItemTypes.Item), // TODO: revert back to ObjectItem
                        SequenceType.Arity.ZeroOrMore
                ),
                new SequenceType(
                        new ItemType(ItemTypes.ObjectItem),
                        SequenceType.Arity.One
                )
            )
        );
        SequenceType returnType = new SequenceType(
                new ItemType(ItemTypes.ObjectItem),
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
