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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;

/** Serializes a sequence as one stream, with one declaration and sequence-level separators. */
public final class SequenceSerializer {
    private SequenceSerializer() {}

    public static String serialize(List<Item> items, SerializationParameters parameters) {
        StringBuilder result = new StringBuilder();
        try {
            write(items.iterator(), parameters, result);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result.toString();
    }

    public static void write(Iterator<Item> items, SerializationParameters parameters, Appendable output)
            throws IOException {
        SerializationParameters params = SerializationParameters.copy(parameters);
        String method = Serializers.normalizeMethodName(params.getMethod());
        boolean xml = "xml".equalsIgnoreCase(method);
        SerializationParameters itemParams = SerializationParameters.copy(params);
        if (xml) {
            itemParams.setOmitXmlDeclaration(true);
        }
        Serializer serializer = Serializers.from(itemParams);
        if ("json".equalsIgnoreCase(method)) {
            if (!items.hasNext()) {
                output.append("null");
                return;
            }
            Item item = items.next();
            if (items.hasNext()) {
                throw new RumbleException(
                        "JSON serialization requires the top-level sequence to contain at most one item.",
                        new ErrorCode(new Name(Name.ERROR_NS, "err", "SERE0023")),
                        ExceptionMetadata.EMPTY_METADATA);
            }
            output.append(serializer.serialize(item));
            return;
        }
        if (xml && !params.getOmitXmlDeclaration() && items.hasNext()) {
            StringBuilder declaration = new StringBuilder();
            SerializerUtils.appendXmlDeclaration(declaration, params);
            output.append(declaration);
        }
        String separator = params.getItemSeparator();
        if (separator == null) {
            separator = "adaptive".equalsIgnoreCase(method) ? "\n" : "";
        }
        boolean first = true;
        while (items.hasNext()) {
            if (!first) {
                output.append(separator);
            }
            output.append(serializer.serialize(items.next()));
            first = false;
        }
    }
}
