package org.rumbledb.serialization;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.FunctionsNonSerializableException;

public class XmlJsonHybridSerializer implements Serializer, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private final org.rumbledb.serialization.SerializationParameters params;

    public XmlJsonHybridSerializer(SerializationParameters params) {
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
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAtomic()) {
            if (isTopLevel) {
                sb.append(item.getStringValue());
            } else {
                appendJSONAtomicItem(item, sb);
            }
            return;
        }
        if (item.isArray()) {
            sb.append("[");

            String separator = " ";
            if (this.params.getIndent()) {
                separator = "\n" + indent + "  ";
            }
            boolean firstTime = true;
            for (Item member : item.getItems()) {
                sb.append(separator);
                if (firstTime) {
                    separator = "," + separator;
                    firstTime = false;
                }
                if (this.params.getIndent()) {
                    serialize(member, sb, indent + "  ", false);
                } else {
                    serialize(member, sb, "", false);
                }
            }
            if (this.params.getIndent()) {
                sb.append("\n" + indent);
            } else {
                sb.append(" ");
            }
            sb.append("]");
            return;
        }
        if (item.isObject()) {
            sb.append("{");
            String separator = " ";
            if (this.params.getIndent()) {
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
                if (this.params.getIndent()) {
                    serialize(value, sb, indent + "  ", false);
                } else {
                    serialize(value, sb, "", false);
                }
            }
            if (this.params.getIndent()) {
                sb.append("\n" + indent);
            } else {
                sb.append(" ");
            }
            sb.append("}");
            return;
        }
        if (item.isDocumentNode()) {
            for (Item child : item.children()) {
                sb.append("<");
                sb.append(child.nodeName());
                sb.append(">");
                sb.append("\n");

                for (Item descendant : child.children()) {
                    serialize(descendant, sb, indent + "  ", isTopLevel);
                }
                sb.append("</");
                sb.append(child.nodeName());
                sb.append(">");
            }
            return;
        }
        if (item.isElementNode()) {
            sb.append(indent);
            sb.append("<");
            sb.append(item.nodeName());
            for (Item attribute : item.attributes()) {
                serialize(attribute, sb, indent, isTopLevel);
            }
            sb.append(">");
            sb.append("\n");

            for (Item child : item.children()) {
                serialize(child, sb, indent + "  ", isTopLevel);
            }
            sb.append(indent);
            sb.append("</");
            sb.append(item.nodeName());
            sb.append(">");
            sb.append("\n");
            return;
        }
        if (item.isAttributeNode()) {
            sb.append(" ");
            sb.append(item.nodeName());
            sb.append("=");
            sb.append("\"");
            sb.append(item.getStringValue());
            sb.append("\"");
            return;
        }
        if (item.isTextNode()) {
            sb.append(indent);
            sb.append(item.getStringValue());
            sb.append("\n");
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
}


