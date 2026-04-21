package org.rumbledb.serialization;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.OurBadException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class YamlSerializer implements Serializer, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private final org.rumbledb.serialization.SerializationParameters params;

    public YamlSerializer(SerializationParameters params) {
        this.params = params;
    }

    @Override
    public String serialize(Item i) {
        StringBuffer sb = new StringBuffer();
        serialize(i, sb, "", true);
        return sb.toString();
    }

    @Override
    public void serialize(Item item, StringBuffer sb, String indent, boolean isTopLevel) {
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
        java.util.List<Item> sequence = mapItem.getSequenceByKey(key);
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


