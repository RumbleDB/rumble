package org.rumbledb.serialization;

import org.rumbledb.api.Item;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.RumbleException;

public class TextSerializer implements Serializer, java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private final SerializationParameters params;

    public TextSerializer(SerializationParameters params) {
        this.params = params != null ? params : SerializationParameters.defaults();
    }

    @Override
    public String serialize(Item item) {
        StringBuilder sb = new StringBuilder();
        serialize(item, sb, "", true);
        return sb.toString();
    }

    @Override
    public void serialize(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAttributeNode() || item.isNamespaceNode()) {
            throw new RumbleException(
                    "Top-level attribute and namespace nodes cannot be serialized with the text method.",
                    ErrorCode.FunctionsNonSerializable,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        if (item.isAtomic() || item.isTextNode() || item.isCommentNode() || item.isProcessingInstructionNode()) {
            sb.append(item.getStringValue());
            return;
        }
        if (item.isDocumentNode() || item.isElementNode()) {
            sb.append(item.getStringValue());
            return;
        }
        if (item.isArray()) {
            appendArrayMembers(item, sb, indent);
            return;
        }
        if (item.isMap() || item.isObject()) {
            throw new RumbleException(
                    "Serialization method text does not support arrays or maps.",
                    new ErrorCode(org.rumbledb.context.Name.createVariableInNoNamespace("SENR0001")),
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    private void appendArrayMembers(Item array, StringBuilder sb, String indent) {
        String separator = this.params.getItemSeparator() == null ? "" : this.params.getItemSeparator();
        boolean first = true;
        for (java.util.List<Item> memberSequence : array.getSequenceMembers()) {
            for (Item member : memberSequence) {
                if (!first) {
                    sb.append(separator);
                }
                serialize(member, sb, indent, false);
                first = false;
            }
        }
    }
}
