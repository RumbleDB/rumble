package org.rumbledb.serialization;

import org.rumbledb.exceptions.UnsupportedNormalizationFormSerializationException;
import org.rumbledb.exceptions.OurBadException;

public final class Serializers {

    private Serializers() {
    }

    public static Serializer from(SerializationParameters params) {
        SerializationParameters effectiveParams = params != null ? params : SerializationParameters.defaults();
        validateNormalizationForm(effectiveParams);
        String method = effectiveParams.getMethod();
        if (method == null || method.equalsIgnoreCase("json")) {
            return new JsonSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("xml")) {
            return new XmlSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("html")) {
            return new HtmlSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("text")) {
            return new TextSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("adaptive")) {
            return new AdaptiveSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("yaml")) {
            return new YamlSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("tyson")) {
            return new TysonSerializer(effectiveParams);
        }
        if (
            method.equalsIgnoreCase("xml_json_hybrid")
                || method.equalsIgnoreCase("xml-json-hybrid")
                || method.equalsIgnoreCase("xmljsonhybrid")
        ) {
            return new XmlJsonHybridSerializer(effectiveParams);
        }
        throw new OurBadException("Unsupported serialization method: " + method);
    }

    private static void validateNormalizationForm(SerializationParameters params) {
        String normalizationForm = params.getNormalizationForm();
        if (
            normalizationForm == null
                || normalizationForm.equals("none")
                || normalizationForm.equals("NFC")
        ) {
            return;
        }
        throw new UnsupportedNormalizationFormSerializationException(normalizationForm);
    }
}
