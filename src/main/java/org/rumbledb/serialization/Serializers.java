package org.rumbledb.serialization;

import org.rumbledb.context.serialization.SerializationParameters;
import org.rumbledb.exceptions.OurBadException;

public final class Serializers {

    private Serializers() {
    }

    public static Serializer from(SerializationParameters params) {
        String method = params != null ? params.getMethod() : null;
        if (method == null || method.equalsIgnoreCase("json")) {
            return new JsonSerializer(params != null ? params : SerializationParameters.defaults());
        }
        if (method.equalsIgnoreCase("yaml")) {
            return new YamlSerializer(params);
        }
        if (method.equalsIgnoreCase("tyson")) {
            return new TysonSerializer(params);
        }
        if (
            method.equalsIgnoreCase("xml_json_hybrid")
                || method.equalsIgnoreCase("xml-json-hybrid")
                || method.equalsIgnoreCase("xmljsonhybrid")
        ) {
            return new XmlJsonHybridSerializer(params);
        }
        throw new OurBadException("Unsupported serialization method: " + method);
    }
}


