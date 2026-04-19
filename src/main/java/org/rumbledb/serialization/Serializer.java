package org.rumbledb.serialization;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.xml.NamespaceItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Serializer implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public enum Method {
        JSON,
        TYSON,
        XML_JSON_HYBRID,
        YAML
    };

    String encoding;
    Method method;
    boolean indent;
    String itemSeparator;

    public Serializer(String encoding, Method method, boolean indent, String itemSeparator) {
        this.encoding = encoding;
        this.method = method;
        this.indent = indent;
        this.itemSeparator = itemSeparator;
    }

    String getEncoding() {
        return this.encoding;
    }

    Method getMethod() {
        return this.method;
    }

    boolean getIndent() {
        return this.indent;
    }

    String getItemSeparator() {
        return this.itemSeparator;
    }

    private static void appendDmNodeNameLexical(StringBuffer sb, Item item) {
        Name n = item.nodeName();
        if (n != null) {
            String p = n.getPrefix();
            if (p != null && !p.isEmpty()) {
                sb.append(p).append(':').append(n.getLocalName());
            } else {
                sb.append(n.getLocalName());
            }
        }
    }

    public String serialize(Item i) {
        StringBuffer sb = new StringBuffer();
        serialize(i, sb, "", true);
        return sb.toString();
    }

    public void serialize(Item item, StringBuffer sb, String indent, boolean isTopLevel) {
        if (this.method.equals(Method.YAML)) {
            YAMLFactory yamlFactory = new YAMLFactory();
            // yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
            // yamlFactory.enable(YAMLGenerator.Feature.CANONICAL_OUTPUT);
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
            return;
        }
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAtomic()) {
            switch (this.method) {
                case JSON:
                    appendJSONAtomicItem(item, sb);
                    return;
                case TYSON:
                    sb.append("(\"");
                    sb.append(item.getDynamicType().getIdentifierString());
                    sb.append("\") ");
                    sb.append("\"");
                    sb.append(StringEscapeUtils.escapeJson(item.getStringValue()));
                    sb.append("\"");
                    return;
                case XML_JSON_HYBRID:
                    if (isTopLevel) {
                        sb.append(item.getStringValue());
                    } else {
                        appendJSONAtomicItem(item, sb);
                    }
                    return;
                case YAML:
            }
        }
        if (item.isArray()) {
            if (this.method.equals(Method.TYSON)) {
                sb.append("(\"");
                sb.append(item.getDynamicType().getIdentifierString());
                sb.append("\") ");
            }
            sb.append("[");

            String separator = " ";
            if (this.indent) {
                separator = "\n" + indent + "  ";
            }
            boolean firstTime = true;
            for (Item member : item.getItems()) {
                sb.append(separator);
                if (firstTime) {
                    separator = "," + separator;
                    firstTime = false;
                }
                if (this.indent) {
                    serialize(member, sb, indent + "  ", false);
                } else {
                    serialize(member, sb, "", false);
                }

            }
            if (this.indent) {
                sb.append("\n" + indent);
            } else {
                sb.append(" ");
            }
            sb.append("]");
            return;
        }
        if (item.isObject()) {
            if (this.method.equals(Method.TYSON)) {
                sb.append("(\"");
                sb.append(item.getDynamicType().getIdentifierString());
                sb.append("\") ");
            }
            sb.append("{");
            String separator = " ";
            if (this.indent) {
                separator = "\n" + indent + "  ";
            }
            boolean firstTime = true;
            for (String key : item.getStringKeys()) {
                sb.append(separator);
                if (firstTime) {
                    separator = "," + separator;
                    firstTime = false;
                }
                Item value = item.getItemByKey(key);
                sb.append("\"").append(StringEscapeUtils.escapeJson(key)).append("\"").append(" : ");
                if (this.indent) {
                    serialize(value, sb, indent + "  ", false);
                } else {
                    serialize(value, sb, "", false);
                }
            }
            if (this.indent) {
                sb.append("\n" + indent);
            } else {
                sb.append(" ");
            }
            sb.append("}");
            return;
        }
        if (item.isMap()) {
            serializeMapAsJsonSafeObject(item, sb, indent);
            return;
        }
        if (item.isDocumentNode()) {
            for (Item child : item.children()) {
                StringBuffer childBuffer = new StringBuffer();
                serialize(child, childBuffer, indent, isTopLevel);
                if (childBuffer.length() > 0 && childBuffer.charAt(childBuffer.length() - 1) == '\n') {
                    childBuffer.setLength(childBuffer.length() - 1);
                }
                sb.append(childBuffer);
            }
        }
        if (item.isElementNode()) {
            sb.append(indent);
            sb.append("<");
            appendDmNodeNameLexical(sb, item);
            for (Item attribute : item.attributes()) {
                serialize(attribute, sb, indent, isTopLevel);
            }
            // only serialize the namespace nodes that are associated with the element
            // this avoids repetition of namespace nodes that are already in scope, but associated with parent elements
            for (Item namespace : item.declaredNamespaceNodes()) {
                serialize(namespace, sb, indent, isTopLevel);
            }
            sb.append(">");
            sb.append("\n");

            for (Item child : item.children()) {
                serialize(child, sb, indent + "  ", isTopLevel);
            }
            sb.append(indent);
            sb.append("</");
            appendDmNodeNameLexical(sb, item);
            sb.append(">");
            sb.append("\n");
        }

        if (item.isNamespaceNode()) {
            NamespaceItem ns = (NamespaceItem) item;
            sb.append(" ");
            String nsPrefix = ns.getPrefix();
            if (nsPrefix == null || nsPrefix.isEmpty()) {
                sb.append("xmlns=\"");
            } else {
                sb.append("xmlns:");
                sb.append(nsPrefix);
                sb.append("=\"");
            }
            sb.append(StringEscapeUtils.escapeXml11(ns.getUri()));
            sb.append("\"");
            return;
        }

        if (item.isAttributeNode()) {
            sb.append(" ");
            appendDmNodeNameLexical(sb, item);
            sb.append("=");
            sb.append("\"");
            sb.append(StringEscapeUtils.escapeXml11(item.getStringValue()));
            sb.append("\"");
            return;
        }

        if (item.isProcessingInstructionNode()) {
            sb.append(indent);
            sb.append("<?");
            appendDmNodeNameLexical(sb, item);
            String content = item.getStringValue();
            if (content != null && !content.isEmpty()) {
                sb.append(" ");
                sb.append(content);
            }
            sb.append("?>");
            sb.append("\n");
        }

        if (item.isTextNode()) {
            sb.append(indent);
            sb.append(item.getStringValue());
            sb.append("\n");
            return;
        }

        if (item.isCommentNode()) {
            sb.append(indent);
            sb.append("<!--");
            sb.append(item.getStringValue());
            sb.append("-->");
            sb.append("\n");
            return;
        }
    }

    public void generateYAML(Item item, YAMLGenerator yamlGenerator) throws IOException {
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAtomic()) {
            generateYAMLAtomicValue(item, yamlGenerator);
        }
        if (item.isArray()) {
            yamlGenerator.writeStartArray();
            for (Item member : item.getItems()) {
                generateYAML(member, yamlGenerator);
            }
            yamlGenerator.writeEndArray();
        }
        if (item.isMap() && !item.isObject()) {
            yamlGenerator.writeStartObject();
            for (Item key : item.getItemKeys()) {
                yamlGenerator.writeFieldName(key.getStringValue());
                appendMapValue(item, key, yamlGenerator);
            }
            yamlGenerator.writeEndObject();
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

    private void serializeMapAsJsonSafeObject(Item item, StringBuffer sb, String indent) {
        if (this.method.equals(Method.TYSON)) {
            sb.append("(\"");
            sb.append(item.getDynamicType().getIdentifierString());
            sb.append("\") ");
        }
        sb.append("{");
        String separator = " ";
        if (this.indent) {
            separator = "\n" + indent + "  ";
        }
        boolean firstTime = true;
        for (Item key : item.getItemKeys()) {
            sb.append(separator);
            if (firstTime) {
                separator = "," + separator;
                firstTime = false;
            }
            sb.append("\"").append(StringEscapeUtils.escapeJson(key.getStringValue())).append("\"").append(" : ");
            appendMapValue(item, key, sb, indent);
        }
        if (this.indent) {
            sb.append("\n").append(indent);
        } else {
            sb.append(" ");
        }
        sb.append("}");
    }

    private void appendMapValue(Item mapItem, Item key, StringBuffer sb, String indent) {
        java.util.List<Item> sequence = mapItem.getSequenceByKey(key);
        if (sequence == null || sequence.isEmpty()) {
            sb.append("[]");
            return;
        }
        if (sequence.size() == 1) {
            if (this.indent) {
                serialize(sequence.get(0), sb, indent + "  ", false);
            } else {
                serialize(sequence.get(0), sb, "", false);
            }
            return;
        }
        sb.append("[");
        String separator = " ";
        if (this.indent) {
            separator = "\n" + indent + "    ";
        }
        boolean firstTime = true;
        for (Item value : sequence) {
            sb.append(separator);
            if (firstTime) {
                separator = "," + separator;
                firstTime = false;
            }
            if (this.indent) {
                serialize(value, sb, indent + "    ", false);
            } else {
                serialize(value, sb, "", false);
            }
        }
        if (this.indent) {
            sb.append("\n").append(indent).append("  ");
        } else {
            sb.append(" ");
        }
        sb.append("]");
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

    private void appendJSONAtomicItem(Item item, StringBuffer sb) {
        boolean isStringValue = item.isAtomic() && !item.isNumeric() && !item.isBoolean() && !item.isNull();
        if (item.isDouble()) {
            if (Double.isNaN(item.getDoubleValue()) || Double.isInfinite(item.getDoubleValue())) {
                isStringValue = true;
            }
        }
        if (item.isFloat()) {
            if (Float.isNaN(item.getFloatValue()) || Float.isInfinite(item.getFloatValue())) {
                isStringValue = true;
            }
        }
        if (isStringValue) {
            sb.append("\"");
            sb.append(StringEscapeUtils.escapeJson(item.getStringValue()));
            sb.append("\"");
        } else {
            sb.append(item.getStringValue());
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
