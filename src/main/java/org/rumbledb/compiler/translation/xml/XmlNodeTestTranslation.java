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
package org.rumbledb.compiler.translation.xml;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.xml.AttributeTestContext;
import org.rumbledb.compiler.context.xml.DocumentTestContext;
import org.rumbledb.compiler.context.xml.ElementTestContext;
import org.rumbledb.compiler.context.xml.NameTestContext;
import org.rumbledb.compiler.context.xml.PiTestContext;
import org.rumbledb.compiler.context.xml.SchemaAttributeTestContext;
import org.rumbledb.compiler.context.xml.SchemaElementTestContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.xml.node_test.AnyKindTest;
import org.rumbledb.expressions.xml.node_test.AttributeTest;
import org.rumbledb.expressions.xml.node_test.CommentTest;
import org.rumbledb.expressions.xml.node_test.DocumentTest;
import org.rumbledb.expressions.xml.node_test.ElementTest;
import org.rumbledb.expressions.xml.node_test.NameTest;
import org.rumbledb.expressions.xml.node_test.NamespaceNodeTest;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.expressions.xml.node_test.PITest;
import org.rumbledb.expressions.xml.node_test.SchemaNodeTest;
import org.rumbledb.expressions.xml.node_test.TextTest;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

public final class XmlNodeTestTranslation {

    private XmlNodeTestTranslation() {}

    public static <EqNameCtx extends ParserRuleContext> NodeTest nameTest(
            NameTestContext<EqNameCtx> ctx,
            boolean unprefixedUsesDefaultElementNamespace,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        if (ctx.wildcard() == null) {
            NameRole role = unprefixedUsesDefaultElementNamespace
                    ? NameRole.ELEMENT_CONSTRUCTOR
                    : NameRole.NO_DEFAULT_NAMESPACE;
            Name name = parseEqName.apply(ctx.eqName(), role);
            return new NameTest(name);
        } else {
            return new NameTest(ctx.wildcard());
        }
    }

    public static <EqNameCtx extends ParserRuleContext> NodeTest elementTest(
            ElementTestContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        if (ctx.hasOptional()) {
            throw new UnsupportedFeatureException(
                    "Nillable element tests (element(name, type?)) are not supported (validation feature)",
                    translationContext.metadata(ctx.context()));
        }
        if (ctx.hasElementNameOrWildcard()) {
            if (!ctx.isWildcard()) {
                Name elementName = parseEqName.apply(ctx.elementNameEqName(), NameRole.ELEMENT_CONSTRUCTOR);
                if (ctx.typeNameEqName() == null) {
                    return new ElementTest(elementName, null);
                }
                Name typeName = parseEqName.apply(ctx.typeNameEqName(), NameRole.TYPE);
                return new ElementTest(elementName, typeName);
            }
            // Wildcard case: element(*) or element(*, type)
            if (ctx.typeNameEqName() != null) {
                Name typeName = parseEqName.apply(ctx.typeNameEqName(), NameRole.TYPE);
                return new ElementTest(typeName);
            }
            return new ElementTest(true);
        }
        return new ElementTest();
    }

    public static <EqNameCtx extends ParserRuleContext> NodeTest attributeTest(
            AttributeTestContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        if (ctx.hasAttributeNameOrWildcard()) {
            if (!ctx.isWildcard()) {
                Name attributeName = parseEqName.apply(ctx.attributeNameEqName(), NameRole.NO_DEFAULT_NAMESPACE);
                if (ctx.typeNameEqName() != null) {
                    Name typeName = parseEqName.apply(ctx.typeNameEqName(), NameRole.TYPE);
                    return new AttributeTest(attributeName, typeName);
                } else {
                    return new AttributeTest(attributeName, null);
                }
            } else {
                // Wildcard case: attribute(*) or attribute(*, type)
                if (ctx.typeNameEqName() != null) {
                    Name typeName = parseEqName.apply(ctx.typeNameEqName(), NameRole.TYPE);
                    return new AttributeTest(typeName);
                }
                return new AttributeTest(true);
            }
        }
        return new AttributeTest();
    }

    public static <EqNameCtx extends ParserRuleContext> SchemaNodeTest schemaElementTest(
            SchemaElementTestContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        Name name = parseEqName.apply(ctx.eqName(), NameRole.ELEMENT_CONSTRUCTOR);
        ItemType type = translationContext
                .moduleContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getSchemaElementTest(name, translationContext.metadata(ctx.context()));
        return new SchemaNodeTest(type);
    }

    public static <EqNameCtx extends ParserRuleContext> SchemaNodeTest schemaAttributeTest(
            SchemaAttributeTestContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        Name name = parseEqName.apply(ctx.eqName(), NameRole.NO_DEFAULT_NAMESPACE);
        ItemType type = translationContext
                .moduleContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getSchemaAttributeTest(name, translationContext.metadata(ctx.context()));
        return new SchemaNodeTest(type);
    }

    public static <EqNameCtx extends ParserRuleContext> NodeTest documentTest(
            DocumentTestContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        if (ctx.schemaElementTest() != null) {
            SchemaNodeTest schemaTest = schemaElementTest(ctx.schemaElementTest(), translationContext, parseEqName);
            return new SchemaNodeTest(ItemTypeFactory.documentNodeItemType(schemaTest.itemType()));
        }
        if (ctx.elementTest() == null) {
            return new DocumentTest(null);
        }
        return new DocumentTest(elementTest(ctx.elementTest(), translationContext, parseEqName));
    }

    public static <StringLiteralCtx extends ParserRuleContext> NodeTest piTest(
            PiTestContext<StringLiteralCtx> ctx, Function<StringLiteralCtx, String> processStringLiteral) {
        if (ctx.ncName() != null) {
            return new PITest(ctx.ncName());
        }
        if (ctx.stringLiteral() != null) {
            String targetName = processStringLiteral.apply(ctx.stringLiteral());
            return new PITest(targetName);
        }
        return new PITest();
    }

    public static NodeTest commentTest() {
        return new CommentTest();
    }

    public static NodeTest textTest() {
        return new TextTest();
    }

    public static NodeTest namespaceNodeTest() {
        return new NamespaceNodeTest();
    }

    public static NodeTest anyKindTest() {
        return new AnyKindTest();
    }
}
