/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.serialization;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.RumbleException;

public class TextSerializer implements Serializer, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
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
                    ExceptionMetadata.EMPTY_METADATA);
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
                    new ErrorCode(new Name(Name.ERROR_NS, "err", "SENR0001")),
                    ExceptionMetadata.EMPTY_METADATA);
        }
    }

    private void appendArrayMembers(Item array, StringBuilder sb, String indent) {
        boolean first = true;
        for (List<Item> memberSequence : array.getSequenceMembers()) {
            for (Item member : memberSequence) {
                if (!first) {
                    sb.append(" ");
                }
                serialize(member, sb, indent, false);
                first = false;
            }
        }
    }
}
