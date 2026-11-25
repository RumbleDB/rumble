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

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Return type rules shared by several builtin functions, and helpers of the rules of single functions. */
public final class ReturnTypeRules {

    private ReturnTypeRules() {}

    /**
     * The rule of a function that returns items of its first argument, with a cardinality that depends on the
     * argument's, such as fn:head and fn:reverse.
     */
    public static ReturnTypeRule itemsOfFirstArgument(UnaryOperator<SequenceCardinality> cardinality) {
        return (call, context) -> {
            SequenceType input = context.argumentType(0);
            return input.isEmptySequence()
                    ? Optional.of(input)
                    : Optional.of(new SequenceType(input.getItemType(), cardinality.apply(input.getCardinality())));
        };
    }

    /**
     * The rule of the math functions, which are declared on xs:double? because an empty argument gives an empty
     * result: the result of one number is one xs:double.
     */
    public static ReturnTypeRule oneDoubleForOneNumber() {
        return (call, context) -> {
            SequenceType input = TypeAtomization.inferType(context.argumentType(0));
            return input.getArity() == SequenceType.Arity.One
                    ? Optional.of(new SequenceType(BuiltinTypesCatalogue.doubleItem))
                    : Optional.empty();
        };
    }

    /** The rule of jn:project and jn:remove-keys, which change the fields of each object and return other items. */
    public static ReturnTypeRule objectsChanged() {
        return (call, context) -> {
            SequenceType input = context.argumentType(0);
            if (input.isEmptySequence()) {
                return Optional.of(input);
            }
            List<ItemType> members = input.getItemType().getMemberTypes().stream()
                    .map(member -> member.isObjectItemType() ? BuiltinTypesCatalogue.objectItem : member)
                    .toList();
            return Optional.of(
                    new SequenceType(ItemTypeFactory.createInferredUnionType(members), input.getCardinality()));
        };
    }

    /** The rule of array:get and array:head, which return a member of their array. */
    public static ReturnTypeRule arrayMember() {
        return (call, context) -> arrayMemberType(context.argumentType(0));
    }

    /** The rule of a function that returns an array of members of its array, such as array:tail. */
    public static ReturnTypeRule arrayOfSameMembers() {
        return (call, context) -> arrayMemberType(context.argumentType(0)).map(ReturnTypeRules::arrayOf);
    }

    /**
     * The rule of a function that returns an array of members of its array or of the value of an argument, such as
     * array:append.
     */
    public static ReturnTypeRule arrayWithMembersOf(int argument) {
        return (call, context) -> arrayMemberType(context.argumentType(0))
                .map(member -> arrayOf(member.leastCommonSupertypeWith(context.argumentType(argument))));
    }

    /** The type of the members of the first argument, when it is one array. */
    private static Optional<SequenceType> arrayMemberType(SequenceType type) {
        ItemType array = type.getItemType();
        return type.getArity() == SequenceType.Arity.One && (array.isArrayItemType() || array.isXQueryArrayItemType())
                ? Optional.of(LookupTyping.inferLookupCallType(array))
                : Optional.empty();
    }

    /** An array(M), which also holds a JSON array whose members are items of type M. */
    private static SequenceType arrayOf(SequenceType member) {
        return new SequenceType(ItemTypeFactory.xqueryArrayOf(member));
    }

    /**
     * The type of the items read from a file whose path is the first argument, a string literal, if the file exists.
     *
     * @param readSchemaType reads the schema of the file at a path, and throws if it cannot
     */
    public static Optional<SequenceType> fileSchemaType(
            FunctionCallExpression call, ReturnTypeContext context, Function<String, ItemType> readSchemaType) {
        if (!(call.getArguments().get(0) instanceof StringLiteralExpression path)) {
            return Optional.empty();
        }
        URI uri = FileSystemUtil.resolveFileSystemURI(
                context.staticContext().getStaticBaseURI(), path.getValue(), call.getMetadata());
        if (!FileSystemUtil.exists(uri, call.getMetadata())) {
            return Optional.empty();
        }
        // TODO : check if arity is correct
        ItemType itemType;
        try {
            itemType = readSchemaType.apply(FileSystemUtil.convertURIToStringForSpark(uri));
        } catch (Exception e) {
            // An unreadable schema, such as that of a Delta table without Delta in the Spark session, leaves the type
            // unknown, so that the rest of the module is still typed. Running the query reports the problem.
            return Optional.empty();
        }
        return Optional.of(new SequenceType(itemType, SequenceType.Arity.ZeroOrMore));
    }
}
