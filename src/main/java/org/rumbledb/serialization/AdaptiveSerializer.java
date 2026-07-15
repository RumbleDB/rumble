package org.rumbledb.serialization;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.xml.NamespaceItem;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.List;

/**
 * Serializer for the W3C adaptive output method.
 */
public class AdaptiveSerializer implements Serializer, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public AdaptiveSerializer(SerializationParameters params) {
        // reserved for future adaptive serialization parameters
    }

    @Override
    public String serialize(Item item) {
        StringBuilder sb = new StringBuilder();
        serialize(item, sb, "", true);
        return sb.toString();
    }

    @Override
    public void serialize(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        appendItem(item, sb);
    }

    private void appendItem(Item item, StringBuilder sb) {
        if (item.isArray()) {
            appendArray(item, sb);
            return;
        }
        if (item.isMap()) {
            appendMap(item, sb);
            return;
        }
        if (item.isFunction()) {
            appendFunctionItem(item, sb);
            return;
        }
        if (item.isNode()) {
            appendNode(item, sb, false);
            return;
        }
        if (item.isAtomic()) {
            appendAtomic(item, sb);
            return;
        }
        throw new OurBadException("Unsupported item kind for adaptive serialization: " + item.getDynamicType());
    }

    private void appendArray(Item item, StringBuilder sb) {
        sb.append("[");
        boolean first = true;
        if (item.isArrayOfItems()) {
            for (Item member : item.getItemMembers()) {
                if (!first) {
                    sb.append(",");
                }
                appendItem(member, sb);
                first = false;
            }
        } else {
            for (List<Item> memberSequence : item.getSequenceMembers()) {
                if (!first) {
                    sb.append(",");
                }
                appendSequence(memberSequence, sb);
                first = false;
            }
        }
        sb.append("]");
    }

    private void appendMap(Item item, StringBuilder sb) {
        sb.append("map{");
        boolean first = true;
        List<Item> keys = item.getItemKeys();
        List<List<Item>> values = item.getSequenceValues();
        for (int i = 0; i < keys.size(); i++) {
            if (!first) {
                sb.append(",");
            }
            Item key = keys.get(i);
            appendAtomicKey(key, sb);
            sb.append(":");
            appendSequence(values.get(i), sb);
            first = false;
        }
        sb.append("}");
    }

    private void appendSequence(List<Item> sequence, StringBuilder sb) {
        if (sequence == null || sequence.isEmpty()) {
            sb.append("()");
            return;
        }
        if (sequence.size() == 1) {
            appendItem(sequence.get(0), sb);
            return;
        }
        sb.append("(");
        for (int i = 0; i < sequence.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            appendItem(sequence.get(i), sb);
        }
        sb.append(")");
    }

    private void appendAtomicKey(Item key, StringBuilder sb) {
        if (!key.isAtomic()) {
            throw new OurBadException("Adaptive serialization requires atomic map keys.");
        }
        appendAtomic(key, sb);
    }

    private void appendAtomic(Item item, StringBuilder sb) {
        ItemType type = item.getDynamicType();

        if (item.isNull()) {
            sb.append("null");
            return;
        }
        if (item.isBoolean()) {
            sb.append(item.getBooleanValue() ? "true()" : "false()");
            return;
        }
        if (
            item.isString()
                || item.isUntypedAtomic()
                || item.isAnyURI()
                || type.isSubtypeOf(BuiltinTypesCatalogue.stringItem)
        ) {
            sb.append(quoteAsLiteral(item.getStringValue()));
            return;
        }
        if (item.isQName()) {
            appendQName(item.getQNameValue(), sb);
            return;
        }
        if (item.isInteger() || item.isInt() || item.isDecimal()) {
            sb.append(item.getStringValue());
            return;
        }
        if (item.isDouble()) {
            sb.append(serializeDouble(item));
            return;
        }
        if (item.isFloat()) {
            sb.append("xs:float(");
            sb.append(quoteAsLiteral(item.getStringValue()));
            sb.append(")");
            return;
        }
        if (item.isDateTime()) {
            appendTypedAtomic(
                type.equals(BuiltinTypesCatalogue.dateTimeStampItem) ? "xs:dateTime" : "xs:dateTime",
                item,
                sb
            );
            return;
        }
        if (item.isDate()) {
            appendTypedAtomic("xs:date", item, sb);
            return;
        }
        if (item.isTime()) {
            appendTypedAtomic("xs:time", item, sb);
            return;
        }
        if (item.isDuration()) {
            appendTypedAtomic("xs:duration", item, sb);
            return;
        }
        if (item.isBinary()) {
            appendTypedAtomic(type.getName().toString(), item, sb);
            return;
        }
        if (
            item.isGYearMonth()
                || item.isGYear()
                || item.isGMonthDay()
                || item.isGDay()
                || item.isGMonth()
        ) {
            appendTypedAtomic(type.getName().toString(), item, sb);
            return;
        }

        Name typeName = type.getName();
        if (typeName != null) {
            appendTypedAtomic(typeName.toString(), item, sb);
            return;
        }
        sb.append(item.getStringValue());
    }

    private String serializeDouble(Item item) {
        double value = item.getDoubleValue();
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return item.getStringValue();
        }
        String s = Double.toString(value).replace("E", "e");
        if (!s.contains("e")) {
            if (!s.contains(".")) {
                s += ".0";
            }
            s += "e0";
            return s;
        }
        int e = s.indexOf('e');
        String mantissa = s.substring(0, e);
        String exponent = s.substring(e + 1);
        if (exponent.startsWith("+")) {
            exponent = exponent.substring(1);
        }
        boolean negative = exponent.startsWith("-");
        if (negative) {
            exponent = exponent.substring(1);
        }
        exponent = exponent.replaceFirst("^0+(?!$)", "");
        if (exponent.isEmpty()) {
            exponent = "0";
        }
        return mantissa + "e" + (negative ? "-" : "") + exponent;
    }

    private void appendTypedAtomic(String constructorName, Item item, StringBuilder sb) {
        sb.append(constructorName);
        sb.append("(");
        sb.append(quoteAsLiteral(item.getStringValue()));
        sb.append(")");
    }

    private String quoteAsLiteral(String value) {
        int singleQuotes = count(value, '\'');
        int doubleQuotes = count(value, '"');
        if (doubleQuotes <= singleQuotes) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return "'" + value.replace("'", "''") + "'";
    }

    private int count(String value, char ch) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }

    private void appendQName(Name name, StringBuilder sb) {
        String namespace = name.getNamespace();
        sb.append("Q{");
        if (namespace != null) {
            sb.append(namespace);
        }
        sb.append("}");
        sb.append(name.getLocalName());
    }

    private void appendFunctionItem(Item item, StringBuilder sb) {
        FunctionIdentifier identifier = item.getIdentifier();
        if (item.isBuiltinFunction()) {
            Name functionName = identifier.getName();
            if (functionName.getPrefix() != null && !functionName.getPrefix().isEmpty()) {
                sb.append(functionName.getPrefix()).append(":").append(functionName.getLocalName());
            } else if (Name.FN_NS.equals(functionName.getNamespace())) {
                sb.append("fn:").append(functionName.getLocalName());
            } else {
                sb.append(functionName.getLocalName());
            }
        } else {
            sb.append("(anonymous-function)");
        }
        sb.append("#");
        sb.append(identifier.getArity());
    }

    private void appendNode(Item item, StringBuilder sb, boolean inElementMarkup) {
        if (item.isDocumentNode()) {
            for (Item child : item.children()) {
                appendNode(child, sb, false);
            }
            return;
        }
        if (item.isElementNode()) {
            sb.append("<");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            for (Item attribute : item.attributes()) {
                appendNode(attribute, sb, true);
            }
            for (Item namespace : item.declaredNamespaceNodes()) {
                appendNode(namespace, sb, true);
            }
            sb.append(">");
            for (Item child : item.children()) {
                appendNode(child, sb, false);
            }
            sb.append("</");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            sb.append(">");
            return;
        }
        if (item.isAttributeNode()) {
            if (inElementMarkup) {
                sb.append(" ");
            }
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            sb.append("=\"");
            sb.append(StringEscapeUtils.escapeXml11(item.getStringValue()));
            sb.append("\"");
            return;
        }
        if (item.isNamespaceNode()) {
            NamespaceItem ns = (NamespaceItem) item;
            if (inElementMarkup) {
                sb.append(" ");
            }
            String nsPrefix = ns.getPrefix();
            if (nsPrefix == null || nsPrefix.isEmpty()) {
                sb.append("xmlns=\"");
            } else {
                sb.append("xmlns:").append(nsPrefix).append("=\"");
            }
            sb.append(StringEscapeUtils.escapeXml11(ns.getUri()));
            sb.append("\"");
            return;
        }
        if (item.isTextNode()) {
            sb.append(StringEscapeUtils.escapeXml11(item.getStringValue()));
            return;
        }
        if (item.isCommentNode()) {
            sb.append("<!--").append(item.getStringValue()).append("-->");
            return;
        }
        if (item.isProcessingInstructionNode()) {
            sb.append("<?");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            String content = item.getStringValue();
            if (content != null && !content.isEmpty()) {
                sb.append(" ").append(content);
            }
            sb.append("?>");
            return;
        }
        throw new OurBadException("Unsupported node kind for adaptive serialization.");
    }
}
