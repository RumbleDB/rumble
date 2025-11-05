package org.rumbledb.serialization;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.rumbledb.api.Item;
import org.rumbledb.context.serialization.SerializationParameters;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.OurBadException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class YamlSerializer implements Serializer, java.io.Serializable {

    private final String encoding;
    private final boolean indent;
    private final String itemSeparator;

    public YamlSerializer(SerializationParameters params) {
        this.encoding = params.getEncoding();
        this.indent = params.getIndent() != null && params.getIndent();
        this.itemSeparator = params.getItemSeparator();
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
            for (Item member : item.getItems()) {
                generateYAML(member, yamlGenerator);
            }
            yamlGenerator.writeEndArray();
            return;
        }
        if (item.isObject()) {
            yamlGenerator.writeStartObject();
            for (String key : item.getKeys()) {
                yamlGenerator.writeFieldName(key);
                Item value = item.getItemByKey(key);
                generateYAML(value, yamlGenerator);
            }
            yamlGenerator.writeEndObject();
        }
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


