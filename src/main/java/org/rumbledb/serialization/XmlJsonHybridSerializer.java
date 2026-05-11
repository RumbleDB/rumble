package org.rumbledb.serialization;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.items.xml.NamespaceItem;

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
            for (Item member : item.getItemMembers()) {
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
            for (String key : item.getStringKeys()) {
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
        if (item.isMap()) {
            SerializerUtils.serializeMapAsJsonSafeObject(this, this.params, item, sb, indent, null);
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
            return;
        }
        if (item.isElementNode()) {
            sb.append(indent);
            sb.append("<");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            for (Item attribute : item.attributes()) {
                serialize(attribute, sb, indent, isTopLevel);
            }
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
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            sb.append(">");
            sb.append("\n");
            return;
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
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            sb.append("=");
            sb.append("\"");
            sb.append(StringEscapeUtils.escapeXml11(item.getStringValue()));
            sb.append("\"");
            return;
        }
        if (item.isProcessingInstructionNode()) {
            sb.append(indent);
            sb.append("<?");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            String content = item.getStringValue();
            if (content != null && !content.isEmpty()) {
                sb.append(" ");
                sb.append(content);
            }
            sb.append("?>");
            sb.append("\n");
            return;
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


