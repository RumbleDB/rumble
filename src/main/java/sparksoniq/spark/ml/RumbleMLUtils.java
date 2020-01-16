package sparksoniq.spark.ml;

import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamMap;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.OurBadException;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.lang.reflect.InvocationTargetException;

public class RumbleMLUtils {
    public static ParamMap convertRumbleObjectItemToSparkMLParamMap(
            Class<?> transformerClass,
            String transformerName,
            Transformer transformer,
            Item item,
            IteratorMetadata metadata
    ) {
        ParamMap result = new ParamMap();
        for (int paramIndex = 0; paramIndex < item.getKeys().size(); paramIndex++) {
            String paramName = item.getKeys().get(paramIndex);
            Item paramValue = item.getValues().get(paramIndex);

            RumbleMLCatalog.validateParameterForTransformer(transformerName, paramName, metadata);
            String paramJavaTypeName = RumbleMLCatalog.getParamJavaTypeName(paramName, metadata);

            Object paramValueInJava;
            switch (paramJavaTypeName) {
                case "String":
                    paramValueInJava = paramValue.getStringValue();
                    break;
                default:
                    throw new OurBadException("Java param type \"" + paramJavaTypeName + "\" is not handled correct during conversion", metadata);
            }

            try {
                Param<Object> sparkMLParam = (Param<Object>) transformerClass.getMethod(paramName).invoke(transformer);
                result.put(sparkMLParam.w(paramValueInJava));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
                throw new OurBadException(
                        "Error while extracting " + paramName + " for " + transformerName + ".",
                        metadata
                );
            }
        }
        return result;
    }
}
