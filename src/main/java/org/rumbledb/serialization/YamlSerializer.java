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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.OurBadException;

public class YamlSerializer implements Serializer, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private final SerializationParameters params;

    public YamlSerializer(SerializationParameters params) {
        this.params = params;
    }

    @Override
    public String serialize(Item i) {
        StringBuilder sb = new StringBuilder();
        serialize(i, sb, "", true);
        return sb.toString();
    }

    @Override
    public void serialize(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        YAMLFactory yamlFactory = new YAMLFactory();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            YAMLGenerator yamlGenerator = yamlFactory.createGenerator(baos);
            generateYAML(item, yamlGenerator);
            yamlGenerator.flush();
        } catch (IOException ioe) {
            RuntimeException e = new OurBadException("Not able to output YAML.");
            e.initCause(ioe);
            throw e;
        }
        sb.append(baos.toString());
    }

    private void generateYAML(Item item, YAMLGenerator yamlGenerator) throws IOException {
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAtomic()) {
            generateYAMLAtomicValue(item, yamlGenerator);
            return;
        }
        if (item.isArray()) {
            yamlGenerator.writeStartArray();
            for (Item member : item.getItemMembers()) {
                generateYAML(member, yamlGenerator);
            }
            yamlGenerator.writeEndArray();
            return;
        }
        if (item.isMap() && !item.isObject()) {
            yamlGenerator.writeStartObject();
            for (Item key : item.getItemKeys()) {
                yamlGenerator.writeFieldName(key.getStringValue());
                appendMapValue(item, key, yamlGenerator);
            }
            yamlGenerator.writeEndObject();
            return;
        }
        if (item.isObject()) {
            yamlGenerator.writeStartObject();
            for (String key : item.getStringKeys()) {
                yamlGenerator.writeFieldName(key);
                Item value = item.getItemByKey(key);
                generateYAML(value, yamlGenerator);
            }
            yamlGenerator.writeEndObject();
        }
    }

    private void appendMapValue(Item mapItem, Item key, YAMLGenerator yamlGenerator) throws IOException {
        List<Item> sequence = mapItem.getSequenceByKey(key);
        if (sequence == null || sequence.isEmpty()) {
            yamlGenerator.writeStartArray();
            yamlGenerator.writeEndArray();
            return;
        }
        if (sequence.size() == 1) {
            generateYAML(sequence.get(0), yamlGenerator);
            return;
        }
        yamlGenerator.writeStartArray();
        for (Item value : sequence) {
            generateYAML(value, yamlGenerator);
        }
        yamlGenerator.writeEndArray();
    }

    private void generateYAMLAtomicValue(Item item, YAMLGenerator generator) throws IOException {
        if (item.isDouble()) {
            generator.writeNumber(item.getDoubleValue());
        } else if (item.isFloat()) {
            generator.writeNumber(item.getFloatValue());
        } else if (item.isInt()) {
            generator.writeNumber(item.getIntValue());
        } else if (item.isInteger()) {
            generator.writeNumber(item.getIntegerValue());
        } else if (item.isDecimal()) {
            generator.writeNumber(item.getDecimalValue());
        } else {
            generator.writeString(item.getStringValue());
        }
    }
}
