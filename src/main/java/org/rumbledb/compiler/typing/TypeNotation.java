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
package org.rumbledb.compiler.typing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

/**
 * The notation of a type in an error message. An inferred type can be very large, such as the type of a dataset with
 * hundreds of nested fields, and in full it buries the sentence around it. A type that is too long for a message is
 * shortened instead: an object type keeps its own fields and hides what is nested in them, as in
 * { id: xs:integer, actor?: { … }, commits?: [{ … }] }.
 */
public final class TypeNotation {

    /** The longest notation that a message shows in full. */
    private static final int MAX_LENGTH = 120;

    /** The longest notation of a nested type that a shortened type keeps. */
    private static final int MAX_NESTED_LENGTH = 40;

    /** How long the fields of a shortened object type can be before only the first ones are listed. */
    private static final int MAX_FIELDS_LENGTH = 600;

    private TypeNotation() {}

    /** The notation of a sequence type, shortened if it is too long for a message. */
    public static String shortened(SequenceType type) {
        String full = type.toString();
        if (full.length() <= MAX_LENGTH) {
            return full;
        }
        return shortened(type.getItemType(), false) + type.getArity().getSymbol();
    }

    /** The notation of an item type, shortened if it is too long for a message. */
    public static String shortened(ItemType type) {
        String full = type.toString();
        return full.length() <= MAX_LENGTH ? full : shortened(type, false);
    }

    /** Shortens the notation of an anonymous type, of which a nested object type hides all its fields. */
    private static String shortened(ItemType type, boolean nested) {
        if ((type.hasName() && !type.getName().getLocalName().isEmpty()) || !type.isResolved()) {
            return type.toString();
        }
        if (type.isObjectItemType()) {
            return nested ? "{ … }" : shortenedFields(type.getObjectContentFacet());
        }
        if (type.isArrayItemType() && type.getArrayContentFacet() != null) {
            return "[" + shortened(type.getArrayContentFacet(), nested) + "]";
        }
        if (type.isUnionType()) {
            return type.getTypes().stream()
                    .map(member -> shortened(member, nested))
                    .collect(Collectors.joining(" | ", "(", ")"));
        }
        return type.toString();
    }

    private static String shortenedFields(List<FieldDescriptor> fields) {
        List<String> notations = new ArrayList<>();
        for (FieldDescriptor field : fields) {
            String type = field.getType().toString();
            notations.add(field.getName()
                    + (field.isRequired() ? "" : "?")
                    + ": "
                    + (type.length() <= MAX_NESTED_LENGTH ? type : shortened(field.getType(), true)));
        }
        String all = String.join(", ", notations);
        if (all.length() <= MAX_FIELDS_LENGTH) {
            return "{ " + all + " }";
        }
        // Counting the rest only pays off when it hides more than a few fields.
        StringBuilder listed = new StringBuilder();
        int count = 0;
        for (String notation : notations) {
            if (listed.length() + notation.length() > MAX_FIELDS_LENGTH * 3 / 4) {
                break;
            }
            listed.append(notation).append(", ");
            count++;
        }
        return "{ " + listed + "… " + (notations.size() - count) + " more fields }";
    }
}
