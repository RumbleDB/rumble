/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.serialization;

import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnsupportedNormalizationFormSerializationException;

public final class Serializers {

    private Serializers() {}

    public static Serializer from(SerializationParameters params) {
        SerializationParameters effectiveParams = params != null ? params : SerializationParameters.defaults();
        validateNormalizationForm(effectiveParams);
        String method = normalizeMethodName(effectiveParams.getMethod());
        if (method == null || method.equalsIgnoreCase("json")) {
            return new JsonSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("xml")) {
            return new XmlSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("html")) {
            return new HtmlSerializer(effectiveParams);
        }
        if (method.equalsIgnoreCase("xhtml")) {
            return new XhtmlSerializer(effectiveParams);
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
        if (method.equalsIgnoreCase("xml_json_hybrid")
                || method.equalsIgnoreCase("xml-json-hybrid")
                || method.equalsIgnoreCase("xmljsonhybrid")) {
            return new XmlJsonHybridSerializer(effectiveParams);
        }
        throw new OurBadException("Unsupported serialization method: " + method);
    }

    private static String normalizeMethodName(String method) {
        if (method == null) {
            return null;
        }
        String trimmed = method.trim();
        if (!trimmed.startsWith("Q{")) {
            return trimmed;
        }
        int closingBrace = trimmed.indexOf('}');
        if (closingBrace < 0) {
            return trimmed;
        }
        String namespace = trimmed.substring(2, closingBrace);
        String localName = trimmed.substring(closingBrace + 1);
        if (namespace.isEmpty()) {
            return localName;
        }
        return trimmed;
    }

    private static void validateNormalizationForm(SerializationParameters params) {
        String normalizationForm = params.getNormalizationForm();
        if (normalizationForm == null || normalizationForm.equals("none") || normalizationForm.equals("NFC")) {
            return;
        }
        throw new UnsupportedNormalizationFormSerializationException(normalizationForm);
    }
}
