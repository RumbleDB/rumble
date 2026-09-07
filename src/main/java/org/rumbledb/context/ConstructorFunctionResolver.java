package org.rumbledb.context;

import java.util.List;

import org.rumbledb.runtime.functions.ConstructorFunctionIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

/** Resolves constructor names and signatures separately from ordinary built-in functions. */
public final class ConstructorFunctionResolver {
    private ConstructorFunctionResolver() {}

    /** A resolved cast target and its function signature; it contains no runtime schema state. */
    public record ResolvedConstructor(
            FunctionIdentifier identifier, FunctionSignature signature, boolean usesSchemaCaster) {
        public BuiltinFunction asBuiltinFunction() {
            return new BuiltinFunction(
                    this.identifier,
                    this.signature,
                    ConstructorFunctionIterator.class,
                    BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL);
        }
    }

    /** Resolves the built-in constructors supported in the selected query language. */
    public static ResolvedConstructor resolveBuiltIn(FunctionIdentifier identifier, String queryLanguage) {
        Name functionName = identifier.getName();
        if (identifier.getArity() != 1) {
            return null;
        }
        Name typeName = functionName;
        if (Name.JSONIQ_DEFAULT_FUNCTION_NS.equals(functionName.getNamespace())) {
            if (queryLanguage == null || !queryLanguage.startsWith("jsoniq")) {
                return null;
            }
            if ("boolean".equals(functionName.getLocalName())
                    || "string".equals(functionName.getLocalName())
                    || "QName".equals(functionName.getLocalName())
                    || "error".equals(functionName.getLocalName())) {
                return null;
            }
            typeName = Name.createVariableInDefaultTypeNamespace(functionName.getLocalName());
        } else if (!Name.XS_NS.equals(functionName.getNamespace())) {
            return null;
        }
        ItemType listItemType =
                switch (typeName.getLocalName()) {
                    case "IDREFS" -> BuiltinTypesCatalogue.IDREFItem;
                    case "NMTOKENS" -> BuiltinTypesCatalogue.NMTOKENItem;
                    case "ENTITIES" -> BuiltinTypesCatalogue.ENTITYItem;
                    default -> null;
                };
        if (listItemType != null) {
            return resolved(typeName, new SequenceType(listItemType, Arity.ZeroOrMore), true);
        }
        if (!BuiltinTypesCatalogue.typeExists(typeName)) {
            return null;
        }
        ItemType targetType = BuiltinTypesCatalogue.getItemTypeByName(typeName);
        if (!(targetType.isAtomicItemType()
                        || (targetType.isUnionType()
                                && targetType.getTypes().stream().allMatch(ItemType::isAtomicItemType)))
                || targetType.equals(BuiltinTypesCatalogue.atomicItem)
                || targetType.equals(BuiltinTypesCatalogue.NOTATIONItem)) {
            return null;
        }
        return resolved(typeName, new SequenceType(targetType, Arity.OneOrZero), false);
    }

    private static ResolvedConstructor resolved(Name typeName, SequenceType returnType, boolean usesSchemaCaster) {
        return new ResolvedConstructor(
                new FunctionIdentifier(typeName, 1),
                new FunctionSignature(List.of(SequenceType.createSequenceType("anyAtomicType?")), returnType),
                usesSchemaCaster);
    }
}
