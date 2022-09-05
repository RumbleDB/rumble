package org.rumbledb.serialization;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.FunctionsNonSerializableException;

public class Serializer {
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

    public String serialize(Item i) {
        StringBuffer sb = new StringBuffer();
        serialize(i, sb, "", true);
        return sb.toString();
    }

    public void serialize(Item item, StringBuffer sb, String indent, boolean isTopLevel) {
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAtomic()) {
            switch (this.method) {
                case JSON:
                    appendJSONAtomicItem(item, sb);
                    return;
                case YAML:
                    appendYAMLAtomicItem(item, sb);
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
            }
        }
        if (item.isArray()) {
            if (this.method.equals(Method.YAML)) {
                for (Item member : item.getItems()) {
                    sb.append("\n" + indent + "  ");
                    sb.append("- ");
                    if (member.isObject()) {
                        serialize(member, sb, indent + "    ", false);
                    } else {
                        serialize(member, sb, indent + "  ", false);
                    }
                }
                return;
            }
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
            if (this.method.equals(Method.YAML)) {
                String separator = "";
                for (String key : item.getKeys()) {
                    sb.append(separator);
                    separator = "\n" + indent;
                    sb.append(key);
                    sb.append(":");
                    Item value = item.getItemByKey(key);
                    if (value.isObject()) {
                        sb.append(separator);
                        sb.append("  ");
                        serialize(value, sb, indent + "  ", false);
                    } else if (value.isArray()) {
                        serialize(value, sb, indent, false);
                    } else {
                        sb.append(" ");
                        serialize(value, sb, indent, false);
                    }
                }
                return;
            }
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
        }
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

    private void appendYAMLAtomicItem(Item item, StringBuffer sb) {
        boolean isStringValue = false;
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
}
