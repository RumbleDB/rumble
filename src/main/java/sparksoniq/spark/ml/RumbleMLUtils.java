package sparksoniq.spark.ml;

import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamMap;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.typing.CastIterator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class RumbleMLUtils {
    public static ParamMap convertRumbleObjectItemToSparkMLParamMap(
            String transformerShortName,
            Transformer transformer,
            Item paramMapItem,
            ExceptionMetadata metadata
    ) {
        ParamMap result = new ParamMap();
        // paramMapItem is expected to be an ObjectItem
        for (int paramIndex = 0; paramIndex < paramMapItem.getKeys().size(); paramIndex++) {
            String paramName = paramMapItem.getKeys().get(paramIndex);
            Item paramValue = paramMapItem.getValues().get(paramIndex);

            RumbleMLCatalog.validateTransformerParameterByName(transformerShortName, paramName, metadata);
            String paramJavaTypeName = RumbleMLCatalog.getJavaTypeNameOfParamByName(paramName, metadata);

            Object paramValueInJava = convertParamItemToJava(paramName, paramValue, paramJavaTypeName, metadata);

            try {
                @SuppressWarnings("unchecked")
                Param<Object> sparkMLParam = (Param<Object>) transformer.getClass()
                    .getMethod(paramName)
                    .invoke(transformer);
                result.put(sparkMLParam.w(paramValueInJava));
            } catch (
                    NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException
                    | ClassCastException e
            ) {
                throw new OurBadException(
                        "Error while extracting " + paramName + " for " + transformerShortName + ".",
                        metadata
                );
            }
        }
        return result;
    }

    public static ParamMap convertRumbleObjectItemToSparkMLParamMap(
            String estimatorShortName,
            Estimator<?> estimator,
            Item paramMapItem,
            ExceptionMetadata metadata
    ) {
        ParamMap result = new ParamMap();
        // paramMapItem is expected to be an ObjectItem
        for (int paramIndex = 0; paramIndex < paramMapItem.getKeys().size(); paramIndex++) {
            String paramName = paramMapItem.getKeys().get(paramIndex);
            Item paramValue = paramMapItem.getValues().get(paramIndex);

            RumbleMLCatalog.validateEstimatorParameterByName(estimatorShortName, paramName, metadata);
            String paramJavaTypeName = RumbleMLCatalog.getJavaTypeNameOfParamByName(paramName, metadata);

            Object paramValueInJava = convertParamItemToJava(paramName, paramValue, paramJavaTypeName, metadata);

            try {
                @SuppressWarnings("unchecked")
                Param<Object> sparkMLParam = (Param<Object>) estimator.getClass()
                    .getMethod(paramName)
                    .invoke(estimator);
                result.put(sparkMLParam.w(paramValueInJava));
            } catch (
                    NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException
                    | ClassCastException e
            ) {
                throw new OurBadException(
                        "Error while extracting " + paramName + " for " + estimatorShortName + ".",
                        metadata
                );
            }
        }
        return result;
    }

    public static Object convertParamItemToJava(
            String paramName,
            Item param,
            String paramJavaTypeName,
            ExceptionMetadata metadata
    ) {
        if (paramJavaTypeName.endsWith("[]")) {
            if (!(param.isArray())) {
                throw new InvalidArgumentTypeException(paramName + " is expected to be an array type", metadata);
            }
            List<Object> paramAsListInJava = new ArrayList<>();
            // paramValue is expected to be an ArrayItem
            param.getItems().forEach(item -> {
                paramAsListInJava.add(
                    convertParamItemToJava(
                        paramName,
                        item,
                        paramJavaTypeName.substring(0, paramJavaTypeName.length() - 2),
                        metadata
                    )
                );
            });
            return convertArrayListToPrimitiveArray(paramAsListInJava, paramJavaTypeName);
        } else if (expectedJavaTypeMatchesRumbleAtomic(paramJavaTypeName)) {
            return convertRumbleItemToJava(param, paramJavaTypeName);
        } else if (paramJavaTypeName.equals("PipelineStage")) {
            if (param.isEstimator()) {
                return param.getEstimator();
            }
            if (param.isTransformer()) {
                return param.getTransformer();
            }
            throw new InvalidArgumentTypeException(paramName + " is expected to be an pipeline stage", metadata);
        } else {
            // complex SparkML parameters such as Estimator, Transformer, Classifier etc. are not implemented yet
            throw new OurBadException("We have not implemented parameter support for type " + paramJavaTypeName);
        }
    }

    private static Object convertArrayListToPrimitiveArray(List<Object> paramAsListInJava, String paramJavaTypeName) {
        switch (paramJavaTypeName) {
            case "String[]":
                String[] stringArray = new String[paramAsListInJava.size()];
                for (int i = 0; i < stringArray.length; i++) {
                    stringArray[i] = (String) paramAsListInJava.get(i);
                }
                return stringArray;
            case "int[]":
                int[] intArray = new int[paramAsListInJava.size()];
                for (int i = 0; i < intArray.length; i++) {
                    intArray[i] = (Integer) paramAsListInJava.get(i);
                }
                return intArray;
            case "double[]":
                double[] doubleArray = new double[paramAsListInJava.size()];
                for (int i = 0; i < doubleArray.length; i++) {
                    doubleArray[i] = (Double) paramAsListInJava.get(i);
                }
                return doubleArray;
            case "PipelineStage[]":
                PipelineStage[] stageArray = new PipelineStage[paramAsListInJava.size()];
                for (int i = 0; i < stageArray.length; i++) {
                    stageArray[i] = (PipelineStage) paramAsListInJava.get(i);
                }
                return stageArray;
            default:
                throw new OurBadException("Unhandled case of arrayList to primitive[] conversion found.");
        }
    }


    private static boolean expectedJavaTypeMatchesRumbleAtomic(String javaTypeName) {
        return (javaTypeName.equals("boolean")
            || javaTypeName.equals("String")
            || javaTypeName.equals("int")
            || javaTypeName.equals("double")
            || javaTypeName.equals("long"));
    }

    private static Object convertRumbleItemToJava(Item atomicItem, String javaTypeName) {
        if (!atomicItem.isAtomic()) {
            // if we want to get a String, we try to serialize instead of casting
            if (javaTypeName.equals("String")) {
                return atomicItem.serialize();
            }
        }

        Item castItem;
        switch (javaTypeName) {
            case "boolean":
                castItem = CastIterator.castItemToType(
                    atomicItem,
                    BuiltinTypesCatalogue.booleanItem,
                    ExceptionMetadata.EMPTY_METADATA
                );
                if (castItem == null) {
                    throw new OurBadException("We were not able to cast " + atomicItem + " to " + javaTypeName);
                }
                return castItem.getBooleanValue();
            case "String":
                castItem = CastIterator.castItemToType(
                    atomicItem,
                    BuiltinTypesCatalogue.stringItem,
                    ExceptionMetadata.EMPTY_METADATA
                );
                if (castItem == null) {
                    throw new OurBadException("We were not able to cast " + atomicItem + " to " + javaTypeName);
                }
                return castItem.getStringValue();
            case "int":
                castItem = CastIterator.castItemToType(
                    atomicItem,
                    BuiltinTypesCatalogue.integerItem,
                    ExceptionMetadata.EMPTY_METADATA
                );
                if (castItem == null) {
                    throw new OurBadException("We were not able to cast " + atomicItem + " to " + javaTypeName);
                }
                return castItem.getIntValue();
            case "double":
                castItem = CastIterator.castItemToType(
                    atomicItem,
                    BuiltinTypesCatalogue.doubleItem,
                    ExceptionMetadata.EMPTY_METADATA
                );
                if (castItem == null) {
                    throw new OurBadException("We were not able to cast " + atomicItem + " to " + javaTypeName);
                }
                return castItem.getDoubleValue();
            case "long":
                castItem = CastIterator.castItemToType(
                    atomicItem,
                    BuiltinTypesCatalogue.decimalItem,
                    ExceptionMetadata.EMPTY_METADATA
                );
                if (castItem == null) {
                    throw new OurBadException("We were not able to cast " + atomicItem + " to " + javaTypeName);
                }
                return castItem.getDecimalValue()
                    .longValue();
            default:
                throw new OurBadException(
                        "Unrecognized Java type name found \""
                            + javaTypeName
                            + "\" while casting from atomic parameters."
                );
        }
    }

    public static Item removeParameter(Item paramMapItem, String key, ExceptionMetadata metadata) {
        List<String> keys = paramMapItem.getKeys();
        List<Item> values = paramMapItem.getValues();
        int indexToRemove = keys.indexOf(key);
        keys.remove(indexToRemove);
        values.remove(indexToRemove);
        return ItemFactory.getInstance().createObjectItem(keys, values, metadata, true);
    }

    public static JSoundDataFrame createDataFrameContainingVectorizedColumn(
            JSoundDataFrame inputDataset,
            String paramNameExposedToTheUser,
            String[] arrayOfInputColumnNames,
            String outputColumnName,
            ExceptionMetadata metadata
    ) {
        VectorAssembler vectorAssembler = new VectorAssembler();
        vectorAssembler.setInputCols(arrayOfInputColumnNames);
        vectorAssembler.setOutputCol(outputColumnName);

        try {
            return new JSoundDataFrame(
                    vectorAssembler.transform(inputDataset.getDataFrame()),
                    BuiltinTypesCatalogue.objectItem
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidRumbleMLParamException(
                    "Parameter provided to "
                        + paramNameExposedToTheUser
                        + " causes the following error: "
                        + e.getMessage(),
                    metadata
            );
        }
    }
}
