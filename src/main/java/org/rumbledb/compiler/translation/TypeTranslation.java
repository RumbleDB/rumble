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
package org.rumbledb.compiler.translation;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.SingleTypeCheckExprContext;
import org.rumbledb.compiler.context.TypeCheckExprContext;
import org.rumbledb.compiler.context.type.ArrayTestContext;
import org.rumbledb.compiler.context.type.FunctionTestContext;
import org.rumbledb.compiler.context.type.ItemTypeContext;
import org.rumbledb.compiler.context.type.KindTestContext;
import org.rumbledb.compiler.context.type.MapTestContext;
import org.rumbledb.compiler.context.type.SequenceTypeContext;
import org.rumbledb.compiler.context.type.SingleTypeContext;
import org.rumbledb.compiler.context.xml.AttributeTestContext;
import org.rumbledb.compiler.context.xml.DocumentTestContext;
import org.rumbledb.compiler.context.xml.ElementTestContext;
import org.rumbledb.compiler.context.xml.PiTestContext;
import org.rumbledb.compiler.context.xml.SchemaAttributeTestContext;
import org.rumbledb.compiler.context.xml.SchemaElementTestContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.IsStaticallyExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.ItemTypeReference;
import org.rumbledb.types.SequenceType;

public final class TypeTranslation {

    private TypeTranslation() {}

    public static <MainExprCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext>
            Expression instanceOfExpr(
                    TypeCheckExprContext<MainExprCtx, SeqTypeCtx> ctx,
                    TranslationContext translationContext,
                    Function<MainExprCtx, Expression> visitIsStaticallyExpr,
                    Function<SeqTypeCtx, SequenceType> processSequenceType) {
        Expression mainExpression = visitIsStaticallyExpr.apply(ctx.mainExpr());
        if (ctx.seq() == null || ctx.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(ctx.seq());
        return new InstanceOfExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <MainExprCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext>
            Expression isStaticallyExpr(
                    TypeCheckExprContext<MainExprCtx, SeqTypeCtx> ctx,
                    TranslationContext translationContext,
                    Function<MainExprCtx, Expression> visitTreatExpr,
                    Function<SeqTypeCtx, SequenceType> processSequenceType) {
        Expression mainExpression = visitTreatExpr.apply(ctx.mainExpr());
        if (ctx.seq() == null || ctx.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(ctx.seq());
        return new IsStaticallyExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <MainExprCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext> Expression treatExpr(
            TypeCheckExprContext<MainExprCtx, SeqTypeCtx> ctx,
            TranslationContext translationContext,
            Function<MainExprCtx, Expression> visitCastableExpr,
            Function<SeqTypeCtx, SequenceType> processSequenceType) {
        Expression mainExpression = visitCastableExpr.apply(ctx.mainExpr());
        if (ctx.seq() == null || ctx.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(ctx.seq());
        return new TreatExpression(
                mainExpression,
                sequenceType,
                ErrorCode.DynamicTypeTreatErrorCode,
                translationContext.metadata(ctx.context()));
    }

    public static <MainExprCtx extends ParserRuleContext, SingleTypeCtx extends ParserRuleContext>
            Expression castableExpr(
                    SingleTypeCheckExprContext<MainExprCtx, SingleTypeCtx> ctx,
                    TranslationContext translationContext,
                    Function<MainExprCtx, Expression> visitCastExpr,
                    Function<SingleTypeCtx, SequenceType> processSingleType) {
        Expression mainExpression = visitCastExpr.apply(ctx.mainExpr());
        if (ctx.single() == null || ctx.single().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSingleType.apply(ctx.single());
        return new CastableExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <MainExprCtx extends ParserRuleContext, SingleTypeCtx extends ParserRuleContext> Expression castExpr(
            SingleTypeCheckExprContext<MainExprCtx, SingleTypeCtx> ctx,
            TranslationContext translationContext,
            Function<MainExprCtx, Expression> visitArrowExpr,
            Function<SingleTypeCtx, SequenceType> processSingleType) {
        Expression mainExpression = visitArrowExpr.apply(ctx.mainExpr());
        if (ctx.single() == null || ctx.single().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSingleType.apply(ctx.single());
        return new CastExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <ItemTypeCtx extends ParserRuleContext> SequenceType sequenceType(
            SequenceTypeContext<ItemTypeCtx> ctx, Function<ItemTypeCtx, ItemType> processItemType) {
        if (ctx.item() == null) {
            return SequenceType.createSequenceType("()");
        }
        ItemType itemType = processItemType.apply(ctx.item());
        if (ctx.hasQuestion()) {
            return new SequenceType(itemType, SequenceType.Arity.OneOrZero);
        }
        if (ctx.hasStar()) {
            return new SequenceType(itemType, SequenceType.Arity.ZeroOrMore);
        }
        if (ctx.hasPlus()) {
            return new SequenceType(itemType, SequenceType.Arity.OneOrMore);
        }
        return new SequenceType(itemType);
    }

    public static <ItemTypeCtx extends ParserRuleContext> SequenceType singleType(
            SingleTypeContext<ItemTypeCtx> ctx, Function<ItemTypeCtx, ItemType> processItemType) {
        if (ctx.item() == null) {
            return SequenceType.createSequenceType("()");
        }
        ItemType itemType = processItemType.apply(ctx.item());
        if (ctx.hasQuestion()) {
            return new SequenceType(itemType, SequenceType.Arity.OneOrZero);
        }
        return new SequenceType(itemType);
    }

    public static <
                    SeqTypeCtx extends ParserRuleContext,
                    ItemTypeCtx extends ParserRuleContext,
                    EqNameCtx extends ParserRuleContext,
                    AnnotationCtx extends ParserRuleContext,
                    StringLiteralCtx extends ParserRuleContext>
            ItemType itemType(
                    ItemTypeContext<SeqTypeCtx, ItemTypeCtx, EqNameCtx, AnnotationCtx, StringLiteralCtx> ctx,
                    TranslationContext translationContext,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName,
                    Consumer<List<AnnotationCtx>> processAnnotations,
                    Function<StringLiteralCtx, String> processStringLiteral,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ItemTypeCtx, ItemType> processItemType) {
        if (ctx.parenthesizedItemType() != null) {
            return processItemType.apply(ctx.parenthesizedItemType());
        }
        if (ctx.isItem()) {
            return BuiltinTypesCatalogue.item;
        }
        if (ctx.isNull()) {
            return BuiltinTypesCatalogue.nullItem;
        }
        if (ctx.functionTest() != null) {
            FunctionTestContext<SeqTypeCtx, AnnotationCtx> fnCtx = ctx.functionTest();
            if (fnCtx.annotations() != null && !fnCtx.annotations().isEmpty()) {
                processAnnotations.accept(fnCtx.annotations());
            }
            if (!fnCtx.isAnyFunction()) {
                SequenceType rt = processSequenceType.apply(fnCtx.returnType());
                List<SequenceType> st =
                        fnCtx.parameterTypes().stream().map(processSequenceType).collect(Collectors.toList());
                FunctionSignature signature = new FunctionSignature(st, rt);
                return ItemTypeFactory.createFunctionItemType(signature);
            } else {
                return BuiltinTypesCatalogue.anyFunctionItem;
            }
        }
        if (ctx.mapTest() != null) {
            MapTestContext<SeqTypeCtx, EqNameCtx> mapTestCtx = ctx.mapTest();
            if (mapTestCtx.isAnyMap()) {
                return BuiltinTypesCatalogue.mapItem;
            }
            Name keyName = parseEqName.apply(mapTestCtx.keyName(), NameRole.TYPE);
            keyName = ItemTypeReference.renameAtomic(translationContext.moduleContext(), keyName);
            ItemType keyType;
            if (!BuiltinTypesCatalogue.typeExists(keyName)) {
                keyType = new ItemTypeReference(keyName);
            } else {
                keyType = BuiltinTypesCatalogue.getItemTypeByName(keyName);
            }
            SequenceType valueSequenceType = processSequenceType.apply(mapTestCtx.valueSequenceType());
            return ItemTypeFactory.mapOf(keyType, valueSequenceType);
        }
        if (ctx.arrayTest() != null) {
            ArrayTestContext<SeqTypeCtx> arrayTestCtx = ctx.arrayTest();
            if (arrayTestCtx.isAnyArray()) {
                return BuiltinTypesCatalogue.xqueryArrayItem;
            }
            SequenceType contentSequenceType = processSequenceType.apply(arrayTestCtx.contentSequenceType());
            return ItemTypeFactory.xqueryArrayOf(contentSequenceType);
        }
        if (ctx.eqName() != null) {
            Name name = parseEqName.apply(ctx.eqName(), NameRole.TYPE);
            name = ItemTypeReference.renameAtomic(translationContext.moduleContext(), name);
            if (!BuiltinTypesCatalogue.typeExists(name)) {
                return new ItemTypeReference(name);
            }
            return BuiltinTypesCatalogue.getItemTypeByName(name);
        }
        if (ctx.kindTest() != null) {
            return kindTest(ctx.kindTest(), translationContext, parseEqName, processStringLiteral);
        }
        throw new UnsupportedFeatureException("Unsupported itemtype encountered", ExceptionMetadata.EMPTY_METADATA);
    }

    public static <EqNameCtx extends ParserRuleContext, StringLiteralCtx extends ParserRuleContext> ItemType kindTest(
            KindTestContext<EqNameCtx, StringLiteralCtx> kindTestContext,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName,
            Function<StringLiteralCtx, String> processStringLiteral) {
        if (kindTestContext.schemaElementTest() != null) {
            return schemaElementTestAsItemType(kindTestContext.schemaElementTest(), translationContext, parseEqName);
        }
        if (kindTestContext.schemaAttributeTest() != null) {
            return schemaAttributeTestAsItemType(
                    kindTestContext.schemaAttributeTest(), translationContext, parseEqName);
        }
        if (kindTestContext.isAnyKindTest()) {
            return BuiltinTypesCatalogue.nodeItem;
        }
        if (kindTestContext.documentTest() != null) {
            DocumentTestContext<EqNameCtx> documentTestContext = kindTestContext.documentTest();
            if (documentTestContext.schemaElementTest() != null) {
                return ItemTypeFactory.documentNodeItemType(schemaElementTestAsItemType(
                        documentTestContext.schemaElementTest(), translationContext, parseEqName));
            }
            if (documentTestContext.elementTest() != null) {
                ElementNodeItemType elementTestType =
                        elementTestAsItemType(documentTestContext.elementTest(), translationContext, parseEqName);
                return ItemTypeFactory.documentNodeItemType(elementTestType);
            }
            return BuiltinTypesCatalogue.documentNode;
        }
        if (kindTestContext.elementTest() != null) {
            return elementTestAsItemType(kindTestContext.elementTest(), translationContext, parseEqName);
        }
        if (kindTestContext.attributeTest() != null) {
            AttributeTestContext<EqNameCtx> attributeTestContext = kindTestContext.attributeTest();
            Name attributeName = attributeTestContext.hasAttributeNameOrWildcard() && !attributeTestContext.isWildcard()
                    ? parseEqName.apply(attributeTestContext.attributeNameEqName(), NameRole.NO_DEFAULT_NAMESPACE)
                    : null;
            if (attributeTestContext.typeNameEqName() == null) {
                return attributeName == null
                        ? BuiltinTypesCatalogue.attributeNode
                        : ItemTypeFactory.attributeNodeItemType(attributeName);
            }
            Name typeName = parseEqName.apply(attributeTestContext.typeNameEqName(), NameRole.TYPE);
            return ItemTypeFactory.attributeNodeItemType(
                    attributeName,
                    typeName,
                    translationContext
                            .moduleContext()
                            .getInScopeSchemaTypes()
                            .getXmlSchemaCatalog()
                            .getTypeHierarchy(typeName, translationContext.metadata(attributeTestContext.context())));
        }
        if (kindTestContext.isCommentTest()) {
            return BuiltinTypesCatalogue.commentNode;
        }
        if (kindTestContext.isTextTest()) {
            return BuiltinTypesCatalogue.textNode;
        }
        if (kindTestContext.isNamespaceNodeTest()) {
            return BuiltinTypesCatalogue.namespaceNode;
        }
        if (kindTestContext.piTest() != null) {
            PiTestContext<StringLiteralCtx> piTestContext = kindTestContext.piTest();
            if (piTestContext.ncName() != null) {
                return ItemTypeFactory.processingInstructionNodeItemType(piTestContext.ncName());
            }
            if (piTestContext.stringLiteral() != null) {
                String targetName = processStringLiteral.apply(piTestContext.stringLiteral());
                return ItemTypeFactory.processingInstructionNodeItemType(targetName);
            }
            return BuiltinTypesCatalogue.processingInstructionNode;
        }
        throw new UnsupportedFeatureException(
                "Unsupported kind test in item type: "
                        + kindTestContext.context().getText(),
                translationContext.metadata(kindTestContext.context()));
    }

    private static <EqNameCtx extends ParserRuleContext> ElementNodeItemType schemaElementTestAsItemType(
            SchemaElementTestContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        Name name = parseEqName.apply(ctx.eqName(), NameRole.ELEMENT_CONSTRUCTOR);
        return translationContext
                .moduleContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getSchemaElementTest(name, translationContext.metadata(ctx.context()));
    }

    private static <EqNameCtx extends ParserRuleContext> ItemType schemaAttributeTestAsItemType(
            SchemaAttributeTestContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        Name name = parseEqName.apply(ctx.eqName(), NameRole.NO_DEFAULT_NAMESPACE);
        return translationContext
                .moduleContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getSchemaAttributeTest(name, translationContext.metadata(ctx.context()));
    }

    private static <EqNameCtx extends ParserRuleContext> ElementNodeItemType elementTestAsItemType(
            ElementTestContext<EqNameCtx> elementTestContext,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        Name elementName = elementTestContext.hasElementNameOrWildcard() && !elementTestContext.isWildcard()
                ? parseEqName.apply(elementTestContext.elementNameEqName(), NameRole.ELEMENT_CONSTRUCTOR)
                : null;
        if (elementTestContext.typeNameEqName() == null) {
            return elementName == null
                    ? (ElementNodeItemType) BuiltinTypesCatalogue.elementNode
                    : (ElementNodeItemType) ItemTypeFactory.elementNodeItemType(elementName);
        }
        Name typeName = parseEqName.apply(elementTestContext.typeNameEqName(), NameRole.TYPE);
        return (ElementNodeItemType) ItemTypeFactory.elementNodeItemType(
                elementName,
                typeName,
                translationContext
                        .moduleContext()
                        .getInScopeSchemaTypes()
                        .getXmlSchemaCatalog()
                        .getTypeHierarchy(typeName, translationContext.metadata(elementTestContext.context())),
                elementTestContext.hasOptional());
    }
}
