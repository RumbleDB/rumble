package sparksoniq.spark.ml;

import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.AtomicItem;
import org.rumbledb.types.ItemType;
import org.rumbledb.items.ItemFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
            if (!(param instanceof ArrayItem)) {
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
            return convertRumbleAtomicToJava((AtomicItem) param, paramJavaTypeName);
        } else {
            // complex SparkML parameters such as Estimator, Transformer, Classifier etc. are not implemented yet
            throw new OurBadException("Not Implemented");
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

    private static Object convertRumbleAtomicToJava(AtomicItem atomicItem, String javaTypeName) {
        switch (javaTypeName) {
            case "boolean":
                return atomicItem.castAs(ItemType.booleanItem).getBooleanValue();
            case "String":
                return atomicItem.castAs(ItemType.stringItem).getStringValue();
            case "int":
                return atomicItem.castAs(ItemType.integerItem).getIntegerValue();
            case "double":
                return atomicItem.castAs(ItemType.doubleItem).getDoubleValue();
            case "long":
                return atomicItem.castAs(ItemType.decimalItem).getDecimalValue().longValue();
            default:
                throw new OurBadException(
                        "Unrecognized Java type name found \""
                            + javaTypeName
                            + "\" while casting from atomic parameters."
                );
        }
    }

    public static Item removeParameter(Item paramMapItem, String key, ExceptionMetadata metadata) {
        LinkedHashMap<String, Item> newContent = new LinkedHashMap<>(paramMapItem.getAsMap());
        newContent.remove(key);
        return ItemFactory.getInstance().createObjectItem(newContent);
    }

    public static Dataset<Row> createDataFrameContainingVectorizedColumn(
            Dataset<Row> inputDataset,
            String paramNameExposedToTheUser,
            String[] arrayOfInputColumnNames,
            String outputColumnName,
            ExceptionMetadata metadata
    ) {
        VectorAssembler vectorAssembler = new VectorAssembler();
        vectorAssembler.setInputCols(arrayOfInputColumnNames);
        vectorAssembler.setOutputCol(outputColumnName);

        try {
            return vectorAssembler.transform(inputDataset);
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
