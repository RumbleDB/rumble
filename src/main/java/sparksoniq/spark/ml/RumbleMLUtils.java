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

            try {
                Param sparkMLParam = (Param) transformerClass.getMethod(paramName).invoke(transformer);

                // TODO: determine the type of the Param and convert paramValueItem to that type
                // TODO: add to paramMap
                result.put(sparkMLParam, paramValue);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new OurBadException(
                        "Error while extracting " + paramName + " for " + transformerName + ".",
                        metadata
                );
            }
        }
        return result;
    }
}
