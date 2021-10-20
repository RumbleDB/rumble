package org.rumbledb.items;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.FunctionsNonSerializableException;

public class Serializer {
    enum Method {
        JSON,
        TYSON,
        XML_JSON_HYBRID
    };
    
    String encoding;
    Method method;
    boolean indent;
    String itemSeparator;
    
    public Serializer(String encoding, Method method, boolean indent, String itemSeparator)
    {
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
    
    String serialize(Item i)
    {
        StringBuffer sb = new StringBuffer();
        serialize(i, sb, 0);
        return sb.toString();
    }
    
    void serialize(Item item, StringBuffer sb, int indent)
    {
        if(item.isFunction())
        {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAtomic())
        {
            switch (this.method)
            {
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
                    if(isStringValue)
                    {
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
        if (item.isArray())
        {
            if(this.method.equals(Method.TYSON))
            {
                sb.append("(\"");
                sb.append(item.getDynamicType().getIdentifierString());
                sb.append("\") ");
            }
            sb.append("[");

            String separator = " ";
            if(this.indent)
            {
                separator = "\n";
                for(int i = 0; i < indent; ++i)
                {
                    separator += " ";
                }
            }
            for (Item member : item.getItems()) {
                sb.append(separator);
                separator = "," + separator;
                if(this.indent)
                {
                    serialize(member, sb, indent + 2);
                } else {
                    serialize(member, sb, 0);
                }

            }
            sb.append(" ]");
            return;
        }
        if(item.isObject())
        {
            if(this.method.equals(Method.TYSON))
            {
                sb.append("(\"");
                sb.append(item.getDynamicType().getIdentifierString());
                sb.append("\") ");
            }
            sb.append("{ ");
            String separator = " ";
            if(this.indent)
            {
                separator = "\n";
                for(int i = 0; i < indent; ++i)
                {
                    separator += " ";
                }
            }
            for (String key : item.getKeys()) {
                sb.append(separator);
                sb.append(separator);
                separator = "," + separator;
                Item value = item.getItemByKey(key);
                sb.append("\"").append(StringEscapeUtils.escapeJson(key)).append("\"").append(" : ");
                if(this.indent)
                {
                    serialize(value, sb, indent + 2);
                } else {
                    serialize(value, sb, 0);
                }
            }
            sb.append("}");
        }
    }
}
