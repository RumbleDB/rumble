package sparksoniq.spark.ml;

import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamMap;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.OurBadException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.semantics.types.AtomicTypes;

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

            RumbleMLCatalog.validateParameterForTransformer(transformerShortName, paramName, metadata);
            String paramJavaTypeName = RumbleMLCatalog.getParamJavaTypeName(paramName, metadata);

            Object paramValueInJava = convertParamItemToJava(paramName, paramValue, paramJavaTypeName, metadata);

            try {
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

            RumbleMLCatalog.validateParameterForEstimator(estimatorShortName, paramName, metadata);
            String paramJavaTypeName = RumbleMLCatalog.getParamJavaTypeName(paramName, metadata);

            Object paramValueInJava = convertParamItemToJava(paramName, paramValue, paramJavaTypeName, metadata);

            try {
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

    private static Object convertParamItemToJava(
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
                return paramAsListInJava.toArray();
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
                return atomicItem.castAs(AtomicTypes.BooleanItem).getBooleanValue();
            case "String":
                return atomicItem.castAs(AtomicTypes.StringItem).getStringValue();
            case "int":
                return atomicItem.castAs(AtomicTypes.IntegerItem).getIntegerValue();
            case "double":
                return atomicItem.castAs(AtomicTypes.DoubleItem).getDoubleValue();
            case "long":
                return (long) atomicItem.castAs(AtomicTypes.DoubleItem).getDoubleValue();
            default:
                throw new OurBadException(
                        "Unrecognized Java type name found \""
                            + javaTypeName
                            + "\" while casting from atomic parameters."
                );
        }
    }
}
