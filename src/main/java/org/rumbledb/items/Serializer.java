package org.rumbledb.items;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.FunctionsNonSerializableException;

public class Serializer {
    public enum Method {
        JSON,
        TYSON,
        XML_JSON_HYBRID
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

    public String serialize(Item i) {
        StringBuffer sb = new StringBuffer();
        serialize(i, sb, "");
        return sb.toString();
    }

    public void serialize(Item item, StringBuffer sb, String indent) {
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAtomic()) {
            switch (this.method) {
                case JSON:
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
                    sb.append(item.getStringValue());
                    return;
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
                    serialize(member, sb, indent + "  ");
                } else {
                    serialize(member, sb, "");
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
            for (String key : item.getKeys()) {
                sb.append(separator);
                if (firstTime) {
                    separator = "," + separator;
                    firstTime = false;
                }
                Item value = item.getItemByKey(key);
                sb.append("\"").append(StringEscapeUtils.escapeJson(key)).append("\"").append(" : ");
                if (this.indent) {
                    serialize(value, sb, indent + "  ");
                } else {
                    serialize(value, sb, "");
                }
            }
            if (this.indent) {
                sb.append("\n" + indent);
            } else {
                sb.append(" ");
            }
            sb.append("}");
        }
    }
}
