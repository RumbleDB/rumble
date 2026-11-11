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
package org.rumbledb.compiler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;

import org.rumbledb.compiler.typing.ArithmeticTyping;
import org.rumbledb.compiler.typing.AtomizedOperands;
import org.rumbledb.compiler.typing.CastTyping;
import org.rumbledb.compiler.typing.ComparisonTyping;
import org.rumbledb.compiler.typing.FunctionConversionTyping;
import org.rumbledb.compiler.typing.LookupTyping;
import org.rumbledb.compiler.typing.PathStepTyping;
import org.rumbledb.compiler.typing.ReturnTypeContext;
import org.rumbledb.compiler.typing.StaticTypeErrors;
import org.rumbledb.compiler.typing.TypeErrorMessages;
import org.rumbledb.compiler.typing.TypeNotation;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.IsStaticallyUnexpectedTypeException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.comparison.NodeComparisonExpression;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TryCatchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByVariableDeclaration;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.SimpleMapExpression;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.*;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.DecimalLiteralExpression;
import org.rumbledb.expressions.primary.DoubleLiteralExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.MapConstructorExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.block.BlockExpression;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.ConditionalStatement;
import org.rumbledb.expressions.scripting.control.SwitchCaseStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TryCatchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;
import org.rumbledb.expressions.scripting.declaration.CommaVariableDeclStatement;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.BreakStatement;
import org.rumbledb.expressions.scripting.loops.ContinueStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.FunctionArgumentConversionExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.IsStaticallyExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.typing.ValidateExpression;
import org.rumbledb.expressions.typing.ValidateTypeExpression;
import org.rumbledb.expressions.update.AppendExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.CreateCollectionExpression;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.DeleteIndexFromCollectionExpression;
import org.rumbledb.expressions.update.DeleteSearchFromCollectionExpression;
import org.rumbledb.expressions.update.EditCollectionExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.InsertIndexIntoCollectionExpression;
import org.rumbledb.expressions.update.InsertSearchIntoCollectionExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.update.TruncateCollectionExpression;
import org.rumbledb.expressions.xml.AttributeNodeContentExpression;
import org.rumbledb.expressions.xml.AttributeNodeExpression;
import org.rumbledb.expressions.xml.CommentNodeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedAttributeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedElementConstructorExpression;
import org.rumbledb.expressions.xml.ComputedNamespaceConstructorExpression;
import org.rumbledb.expressions.xml.ComputedPIConstructorExpression;
import org.rumbledb.expressions.xml.DirElemConstructorExpression;
import org.rumbledb.expressions.xml.DirPIConstructorExpression;
import org.rumbledb.expressions.xml.DirectCommentConstructorExpression;
import org.rumbledb.expressions.xml.DocumentNodeConstructorExpression;
import org.rumbledb.expressions.xml.PathRootExpression;
import org.rumbledb.expressions.xml.PostfixLookupExpression;
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.TextNodeConstructorExpression;
import org.rumbledb.expressions.xml.TextNodeExpression;
import org.rumbledb.expressions.xml.UnaryLookupExpression;
import org.rumbledb.resources.ResourceResolver;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.DocumentNodeItemType;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SchemaElementNodeItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;
import org.rumbledb.xml.schema.XmlSchemaCatalog;

/**
 * This visitor infers a static SequenceType for each expression in the query
 */
@Log4j2
public class InferTypeVisitor extends AbstractNodeVisitor<StaticContext> {

    private final RumbleConfiguration rumbleRuntimeConfiguration;
    private final StaticTypeErrors errors;

    private final ArithmeticTyping arithmetic;

    private final ComparisonTyping comparison;

    private final CastTyping cast;

    private final ResourceResolver resourceResolver;

    private final PathStepTyping pathSteps;

    private final LookupTyping lookups;

    /**
     * Infers the types of a module and throws the static type errors found in it, if any. An exception that stops the
     * typing after an error is thrown after the errors, as it may follow from them.
     */
    static void inferTypes(Module module, RumbleConfiguration configuration, ResourceResolver resourceResolver) {
        InferTypeVisitor visitor = new InferTypeVisitor(configuration, resourceResolver);
        RumbleException stoppedBy = null;
        try {
            visitor.visit(module, module.getStaticContext());
        } catch (RumbleException exception) {
            stoppedBy = exception;
        }
        visitor.errors.throwCollected(stoppedBy);
    }

    /**
     * Builds a new visitor.
     *
     * @param rumbleRuntimeConfiguration the configuration.
     */
    InferTypeVisitor(RumbleConfiguration rumbleRuntimeConfiguration, ResourceResolver resourceResolver) {
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
        this.errors = new StaticTypeErrors(rumbleRuntimeConfiguration);
        this.arithmetic = new ArithmeticTyping(this.errors);
        this.comparison = new ComparisonTyping(this.errors);
        this.cast = new CastTyping(this.errors);
        this.resourceResolver = resourceResolver;
        this.pathSteps = new PathStepTyping(this.errors);
        this.lookups = new LookupTyping(this.errors);
    }

    /**
     * The static type errors found so far, for tools in this package that type a module with a visitor of their own
     * rather than through {@link #inferTypes}, and so are not thrown the errors.
     */
    List<RumbleException> getStaticTypeErrors() {
        return this.errors.getErrors();
    }

    /**
     * Types the node, and gives the unknown type to an expression with an error so that the checks of its parent
     * report nothing. Unexpected compiler exceptions are allowed to propagate; they are not evidence of a
     * cascading type error.
     */
    @Override
    public StaticContext visit(Node node, StaticContext argument) {
        StaticContext result = super.visit(node, argument);
        if (this.errors.hasFailed(node) && node instanceof Expression expression) {
            expression.setStaticSequenceType(StaticTypeErrors.UNKNOWN);
        }
        return result;
    }

    /** Returns the type inferred for a child of the node, which every visited child has. */
    private static SequenceType typeOf(Expression child, Node parent) {
        return StaticTypeErrors.requireInferredType(child.getStaticSequenceType(), parent);
    }

    private static SequenceType typeOf(Statement child, Node parent) {
        return StaticTypeErrors.requireInferredType(child.getStaticSequenceType(), parent);
    }

    /** Visits the children of a node whose own type does not depend on theirs, and sets that type. */
    private StaticContext withType(Node node, SequenceType type, StaticContext argument) {
        visitDescendants(node, argument);
        if (node instanceof Expression expression) {
            expression.setStaticSequenceType(type);
        } else {
            ((Statement) node).setStaticSequenceType(type);
        }
        return argument;
    }

    @Override
    public StaticContext visitCommaExpression(CommaExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType inferredType = SequenceType.createSequenceType("()");

        for (Expression childExpression : expression.getExpressions()) {
            SequenceType childExpressionInferredType = typeOf(childExpression, expression);

            inferredType = inferredType.concatenateWith(childExpressionInferredType);
        }

        expression.setStaticSequenceType(inferredType);
        return argument;
    }

    // region primary

    @Override
    public StaticContext visitString(StringLiteralExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.stringItem));
        return argument;
    }

    @Override
    public StaticContext visitInteger(IntegerLiteralExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.integerItem));
        return argument;
    }

    @Override
    public StaticContext visitDouble(DoubleLiteralExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.doubleItem));
        return argument;
    }

    @Override
    public StaticContext visitDecimal(DecimalLiteralExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.decimalItem));
        return argument;
    }

    @Override
    public StaticContext visitNull(NullLiteralExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.nullItem));
        return argument;
    }

    @Override
    public StaticContext visitBoolean(BooleanLiteralExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitVariableReference(VariableReferenceExpression expression, StaticContext argument) {
        SequenceType variableType = expression.getActualType();
        if (variableType == null) {
            // if is null, no 'as [SequenceType]' part was present in the declaration, therefore we infer it
            variableType = expression.getStaticContext().getVariableSequenceType(expression.getVariableName());
            // we also set variableReference type
            if (variableType == null) {
                log.warn(
                        "Variable reference type was null so we infer it. Please let us know as we would like to look into it.");
                variableType = SequenceType.createSequenceType("item*");
            }
            expression.setActualType(variableType);
        }
        this.errors.rejectInferredEmpty(
                variableType, expression, "$" + expression.getVariableName() + " is always empty");
        expression.setStaticSequenceType(variableType);
        return argument;
    }

    @Override
    public StaticContext visitArrayConstructor(ArrayConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        if (expression.isFixedSlotsArrayConstructor()) {
            // An array of item* members is typed array(*), which would lose that a member is unknown, so that a
            // lookup of it would report the member's error again.
            boolean unknownMember = expression.getMemberExpressions().stream()
                    .anyMatch(member -> StaticTypeErrors.isUnknown(member.getStaticSequenceType()));
            expression.setStaticSequenceType(
                    unknownMember ? StaticTypeErrors.UNKNOWN : new SequenceType(inferFixedSlotsArrayType(expression)));
            return argument;
        }
        Expression contentExpr = expression.getExpression();
        if (contentExpr == null) {
            expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.arrayItem));
            return argument;
        }
        ItemType contentItemType = contentExpr.getStaticSequenceType().getItemType();
        ItemType arrayType = ItemTypeFactory.createAnonymousArrayType(contentItemType);
        expression.setStaticSequenceType(new SequenceType(arrayType));
        return argument;
    }

    /**
     * An array [E1, ..., En] whose members have the types of E1 to En. When each member is one item, it is a JSON
     * array of these items, as the runtime builds it.
     */
    private static ItemType inferFixedSlotsArrayType(ArrayConstructorExpression expression) {
        SequenceType memberType = null;
        for (Expression member : expression.getMemberExpressions()) {
            SequenceType type = typeOf(member, expression);
            memberType = memberType == null ? type : memberType.leastCommonSupertypeWith(type);
        }
        if (memberType == null) {
            return BuiltinTypesCatalogue.arrayItem;
        }
        return memberType.getArity() == SequenceType.Arity.One
                ? ItemTypeFactory.createAnonymousArrayType(memberType.getItemType())
                : ItemTypeFactory.xqueryArrayOf(memberType);
    }

    @Override
    public StaticContext visitStringConstructor(StringConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.stringItem), argument);
    }

    @Override
    public StaticContext visitObjectConstructor(ObjectConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        if (expression.isMergedConstructor()) {
            // if it is a merged constructor the child must be a subtype of object* inferred type
            SequenceType childSequenceType =
                    typeOf((Expression) expression.getChildren().get(0), expression);
            SequenceType objects = SequenceType.createSequenceType("object*");
            if (!childSequenceType.isSubtypeOf(objects)) {
                this.errors.reportMismatch(
                        "A merged object constructor expects objects, but its content is "
                                + TypeNotation.shortened(childSequenceType)
                                + ".",
                        childSequenceType,
                        objects,
                        expression);
            }
        } else {
            for (Expression keyExpression : expression.getKeys()) {
                SequenceType keySequenceType = typeOf(keyExpression, expression);
                SequenceType string = SequenceType.createSequenceType("string");
                SequenceType anyURI = SequenceType.createSequenceType("anyURI");
                if (!keySequenceType.isSubtypeOf(string) && !keySequenceType.isSubtypeOf(anyURI)) {
                    this.errors.report(
                            "An object key must be one xs:string or xs:anyURI, but this one is "
                                    + TypeNotation.shortened(keySequenceType)
                                    + ".",
                            expression,
                            keySequenceType.mayMatch(string) || keySequenceType.mayMatch(anyURI));
                }
            }
        }
        List<StringLiteralExpression> stringLiteralKeys = new ArrayList<>();
        if (expression.getKeys() != null) {
            for (Expression key : expression.getKeys()) {
                if (!(key instanceof StringLiteralExpression stringLiteralKey)) {
                    stringLiteralKeys.clear();
                    break;
                }
                stringLiteralKeys.add(stringLiteralKey);
            }
        }
        List<String> literalKeys = stringLiteralKeys.stream()
                .map(StringLiteralExpression::getValue)
                .toList();
        boolean literalKeysOnly = expression.getKeys() != null
                && literalKeys.size() == expression.getKeys().size();
        if (literalKeysOnly && new HashSet<>(literalKeys).size() < literalKeys.size()) {
            // Duplicate keys always make the constructor fail at runtime, so it returns no value, like fn:error().
            expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.errorItem));
        } else if (literalKeysOnly) {
            // Literal keys define the shape even when a value expression can return zero or many items.
            // Infer each stored item after the constructor's null/array conversion.
            expression.setStaticSequenceType(new SequenceType(ItemTypeFactory.createAnonymousObjectType(
                    literalKeys,
                    expression.getValues().stream()
                            .map(value -> ItemTypeFactory.createObjectFieldType(value.getStaticSequenceType()))
                            .collect(Collectors.toList()))));
        } else {
            expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.objectItem));
        }
        return argument;
    }

    @Override
    public StaticContext visitMapConstructor(MapConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(inferMapConstructorType(expression)));
        return argument;
    }

    /**
     * A map whose keys have the atomized types of the key expressions, and whose values have their types. An empty
     * map, or one with a key that is always empty, and so always fails, is typed map(*).
     */
    private static ItemType inferMapConstructorType(MapConstructorExpression expression) {
        List<ItemType> keyTypes = new ArrayList<>();
        SequenceType valueType = null;
        for (int i = 0; i < expression.getKeys().size(); i++) {
            SequenceType keyType = typeOf(expression.getKeys().get(i), expression);
            if (keyType.isEmptySequence()) {
                return BuiltinTypesCatalogue.mapItem;
            }
            keyTypes.add(TypeAtomization.atomizedItemType(keyType.getItemType()));
            SequenceType entryValueType = typeOf(expression.getValues().get(i), expression);
            valueType = valueType == null ? entryValueType : valueType.leastCommonSupertypeWith(entryValueType);
        }
        if (keyTypes.isEmpty()) {
            return BuiltinTypesCatalogue.mapItem;
        }
        return ItemTypeFactory.mapOf(ItemTypeFactory.createInferredUnionType(keyTypes), valueType);
    }

    @Override
    public StaticContext visitDirElemConstructor(DirElemConstructorExpression expression, StaticContext argument) {
        // Translation has already resolved the element's expanded QName, including local namespaces.
        return withType(
                expression,
                new SequenceType(ItemTypeFactory.constructedElementNodeItemType(
                        expression.getNodeName(), expression.getStaticContext().isConstructionPreserve())),
                argument);
    }

    @Override
    public StaticContext visitDirPIConstructor(DirPIConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.processingInstructionNode), argument);
    }

    @Override
    public StaticContext visitComputedElementConstructor(
            ComputedElementConstructorExpression expression, StaticContext argument) {
        return withType(
                expression,
                new SequenceType(ItemTypeFactory.constructedElementNodeItemType(
                        expression.hasStaticName() ? expression.getElementName() : null,
                        expression.getStaticContext().isConstructionPreserve())),
                argument);
    }

    @Override
    public StaticContext visitComputedPIConstructor(
            ComputedPIConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.processingInstructionNode), argument);
    }

    @Override
    public StaticContext visitComputedAttributeConstructor(
            ComputedAttributeConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.attributeNode), argument);
    }

    @Override
    public StaticContext visitComputedNamespaceConstructor(
            ComputedNamespaceConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.namespaceNode), argument);
    }

    @Override
    public StaticContext visitDocumentNodeConstructor(
            DocumentNodeConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.documentNode), argument);
    }

    @Override
    public StaticContext visitPathRootExpr(PathRootExpression expression, StaticContext argument) {
        SequenceType contextType = expression.getStaticContext().getContextItemStaticType();
        if (contextType == null && expression.getStaticContext().isInScope(Name.CONTEXT_ITEM)) {
            contextType = expression.getStaticContext().getVariableSequenceType(Name.CONTEXT_ITEM);
        }
        ItemType rootType =
                contextType != null && contextType.getItemType().isSubtypeOf(BuiltinTypesCatalogue.documentNode)
                        ? contextType.getItemType()
                        : BuiltinTypesCatalogue.documentNode;
        expression.setStaticSequenceType(new SequenceType(rootType));
        return argument;
    }

    @Override
    public StaticContext visitCommentNodeConstructor(
            CommentNodeConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.commentNode), argument);
    }

    @Override
    public StaticContext visitDirectCommentConstructor(
            DirectCommentConstructorExpression expression, StaticContext argument) {
        // Direct comment constructors are literal, no descendants.
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.commentNode));
        return argument;
    }

    @Override
    public StaticContext visitTextNodeConstructor(TextNodeConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.textNode), argument);
    }

    @Override
    public StaticContext visitTextNode(TextNodeExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.textNode));
        return argument;
    }

    @Override
    public StaticContext visitAttributeNode(AttributeNodeExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.attributeNode), argument);
    }

    @Override
    public StaticContext visitAttributeNodeContent(AttributeNodeContentExpression expression, StaticContext argument) {
        // Attribute content is atomized as xs:untypedAtomic
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.untypedAtomicItem));
        return argument;
    }

    @Override
    public StaticContext visitContextExpr(ContextItemExpression expression, StaticContext argument) {
        SequenceType contextType = expression.getStaticContext().getContextItemStaticType();
        if (contextType == null) {
            contextType = new SequenceType(BuiltinTypesCatalogue.item);
        }
        expression.setStaticSequenceType(contextType);
        return argument;
    }

    @Override
    public StaticContext visitInlineFunctionExpr(InlineFunctionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        Expression body = expression.getBody().getExpression();
        SequenceType inferredType = typeOf(body, expression);
        SequenceType declaredType = expression.getActualReturnType();
        if (declaredType != null
                && !StaticTypeErrors.isUnknown(inferredType)
                && !FunctionConversionTyping.isCompatible(inferredType, declaredType)) {
            this.errors.report(
                    TypeErrorMessages.returnMismatch(expression.getName(), declaredType, inferredType),
                    resultExpression(body),
                    FunctionConversionTyping.mayConvertTo(inferredType, declaredType));
        }
        List<SequenceType> params = new ArrayList<>(expression.getParams().values());
        FunctionSignature signature =
                new FunctionSignature(params, functionReturnType(expression), expression.isUpdating());
        expression.setStaticSequenceType(new SequenceType(ItemTypeFactory.createFunctionItemType(signature)));
        return argument;
    }

    /** Explicit annotations define the contract; early exits need more than the final expression's type. */
    private static SequenceType functionReturnType(InlineFunctionExpression function) {
        if (function.getActualReturnType() != null || function.hasExitStatement()) {
            return function.getReturnType();
        }
        return typeOf(function.getBody().getExpression(), function);
    }

    private FunctionSignature getSignature(FunctionIdentifier identifier, StaticContext staticContext) {
        BuiltinFunction function = BuiltinFunctionCatalogue.getBuiltinFunction(identifier, staticContext);
        return function == null ? staticContext.getFunctionSignature(identifier) : function.getSignature();
    }

    @Override
    public StaticContext visitNamedFunctionRef(NamedFunctionReferenceExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        try {
            FunctionSignature signature = getSignature(expression.getIdentifier(), expression.getStaticContext());
            expression.setStaticSequenceType(new SequenceType(ItemTypeFactory.createFunctionItemType(signature)));
        } catch (UnknownFunctionCallException e) {
            this.errors.reportStaticError(
                    new UnknownFunctionCallException(
                            expression.getIdentifier().getName(),
                            expression.getIdentifier().getArity(),
                            expression.getMetadata()),
                    expression);
        }
        return argument;
    }

    private static boolean isBuiltinFunctionName(Name functionName, String localName) {
        return functionName.getLocalName().equals(localName)
                && (functionName.getNamespace().equals(Name.JSONIQ_DEFAULT_FUNCTION_NS)
                        || functionName.getNamespace().equals(Name.FN_NS));
    }

    /**
     * Checks an input of a static or dynamic call using the same function-conversion rules for explicit and implicit
     * arguments. The function is described by its name, or by its type for a dynamic call.
     */
    private void checkFunctionInput(
            Expression call, String function, String input, SequenceType actual, SequenceType expected) {
        if (!StaticTypeErrors.isUnknown(actual) && !FunctionConversionTyping.isCompatible(actual, expected)) {
            this.errors.report(
                    TypeErrorMessages.argumentMismatch(function, input, expected, actual),
                    call,
                    FunctionConversionTyping.mayConvertTo(actual, expected));
        }
    }

    /** Checks the context item that replaces an omitted final argument, and returns its type after conversion. */
    private SequenceType checkContextArgument(FunctionCallExpression call, BuiltinFunction.ContextArgument input) {
        SequenceType type = call.getStaticContext().getContextItemStaticType();
        // No known focus type does not prove absence. Leave the presence check to the runtime.
        if (type == null) {
            return input.conversion() == BuiltinFunction.ContextConversion.STRING_VALUE
                    ? SequenceType.createSequenceType("string")
                    : new SequenceType(BuiltinTypesCatalogue.item);
        }
        if (StaticTypeErrors.isUnknown(type)) {
            return type;
        }
        if (input.conversion() == BuiltinFunction.ContextConversion.STRING_VALUE) {
            // fn:string(.) takes the string value, not the atomized value; maps and arrays are function items too.
            boolean mayBeFunction = type.getItemType().getMemberTypes().stream()
                    // xs:error has no values, but is a subtype of every item type.
                    .filter(member -> !member.equals(BuiltinTypesCatalogue.errorItem))
                    .anyMatch(member -> member.isSubtypeOf(BuiltinTypesCatalogue.anyFunctionItem));
            if (mayBeFunction) {
                this.errors.report(
                        TypeErrorMessages.noStringValue(call.getFunctionName(), type),
                        call,
                        !type.getItemType().isSubtypeOf(BuiltinTypesCatalogue.anyFunctionItem));
            }
            type = SequenceType.createSequenceType("string");
        }
        checkFunctionInput(call, call.getFunctionName().toString(), "the context item", type, input.parameterType());
        return type;
    }

    @Override
    public StaticContext visitFunctionCall(FunctionCallExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        BuiltinFunction builtinFunction = BuiltinFunctionCatalogue.getBuiltinFunction(
                expression.getFunctionIdentifier(), expression.getStaticContext());
        if (builtinFunction != null && expression.isPartialApplication()) {
            // Builtin partial applications must have been rewritten before inference.
            throw new UnsupportedFeatureException(
                    "Partial application on built-in functions are not supported.", expression.getMetadata());
        }
        FunctionSignature signature;
        try {
            signature = builtinFunction == null
                    ? expression.getStaticContext().getFunctionSignature(expression.getFunctionIdentifier())
                    : builtinFunction.getSignature();
        } catch (UnknownFunctionCallException e) {
            this.errors.reportStaticError(
                    new UnknownFunctionCallException(expression.getFunctionIdentifier(), expression.getMetadata()),
                    expression);
            return argument;
        }
        List<SequenceType> inputTypes = checkFunctionArguments(
                expression,
                expression.getFunctionName().toString(),
                expression.getArguments(),
                signature.getParameterTypes());
        if (expression.isPartialApplication()) {
            expression.setStaticSequenceType(
                    inferPartialApplicationType(expression.getArguments(), signature, expression.isUpdating()));
            return argument;
        }
        if (builtinFunction != null && builtinFunction.getContextArgument() != null) {
            inputTypes.add(checkContextArgument(expression, builtinFunction.getContextArgument()));
        }

        Optional<SequenceType> returnType = inferBuiltinReturnType(expression, builtinFunction, inputTypes, argument);
        expression.setStaticSequenceType(returnType.orElse(signature.getReturnType()));
        return argument;
    }

    /**
     * Checks the supplied arguments of a static or dynamic call and collects their types, skipping partial-application
     * placeholders. The function is described by its name, or by its type for a dynamic call.
     */
    private List<SequenceType> checkFunctionArguments(
            Expression call, String function, List<Expression> arguments, List<SequenceType> parameterTypes) {
        List<SequenceType> argumentTypes = new ArrayList<>();
        for (int i = 0; i < arguments.size(); ++i) {
            Expression argument = arguments.get(i);
            if (argument == null) {
                continue;
            }
            SequenceType actualType = typeOf(argument, call);
            checkFunctionInput(call, function, "argument " + (i + 1), actualType, parameterTypes.get(i));
            argumentTypes.add(actualType);
        }
        return argumentTypes;
    }

    /** The resulting function takes the parameters left as placeholders. */
    private static SequenceType inferPartialApplicationType(
            List<Expression> arguments, FunctionSignature signature, boolean updating) {
        List<SequenceType> parameters = new ArrayList<>();
        for (int i = 0; i < arguments.size(); ++i) {
            if (arguments.get(i) == null) {
                parameters.add(signature.getParameterTypes().get(i));
            }
        }
        FunctionSignature partialSignature = new FunctionSignature(parameters, signature.getReturnType(), updating);
        return new SequenceType(ItemTypeFactory.createFunctionItemType(partialSignature));
    }

    /** Applies a builtin's return rule to the types of its inputs, the context item included. */
    private Optional<SequenceType> inferBuiltinReturnType(
            FunctionCallExpression call,
            BuiltinFunction function,
            List<SequenceType> inputTypes,
            StaticContext staticContext) {
        if (function == null) {
            return Optional.empty();
        }
        return function.getReturnTypeRule()
                .infer(
                        call,
                        new ReturnTypeContext(
                                this.errors,
                                this.rumbleRuntimeConfiguration,
                                this.resourceResolver,
                                staticContext,
                                inputTypes));
    }

    // endregion

    // region typing

    @Override
    public StaticContext visitCastableExpression(CastableExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(this.cast.inferCastableType(
                typeOf(expression.getMainExpression(), expression),
                expression.getSequenceType(),
                argument.getInScopeSchemaTypes().getXmlSchemaCatalog(),
                expression));
        return argument;
    }

    @Override
    public StaticContext visitCastExpression(CastExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(this.cast.inferCastType(
                typeOf(expression.getMainExpression(), expression),
                expression.getSequenceType(),
                argument.getInScopeSchemaTypes().getXmlSchemaCatalog(),
                expression));
        return argument;
    }

    @Override
    public StaticContext visitIsStaticallyExpr(IsStaticallyExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType inferred = expression.getMainExpression().getStaticSequenceType();
        SequenceType expected = expression.getSequenceType();
        // Equality compares declared occurrence indicators, which is all an assertion's type can express.
        if (!inferred.equals(expected)) {
            this.errors.reportAssertion(
                    new IsStaticallyUnexpectedTypeException(
                            "expected static type is "
                                    + TypeNotation.shortened(expected)
                                    + " instead "
                                    + TypeNotation.shortened(inferred)
                                    + " was inferred",
                            expression.getMetadata()),
                    expression);
        }

        // An assertion checks the type without discarding its inference refinements.
        expression.setStaticSequenceType(inferred);
        return argument;
    }

    @Override
    public StaticContext visitInstanceOfExpression(InstanceOfExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.booleanItem), argument);
    }

    @Override
    public StaticContext visitTreatExpression(TreatExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType expressionSequenceType = typeOf(expression.getMainExpression(), expression);
        SequenceType treatedSequenceType = expression.getSequenceType();

        if (expression.checksDeclaredType()) {
            if (!expressionSequenceType.isSubtypeOf(treatedSequenceType)) {
                this.errors.reportMismatch(
                        TypeErrorMessages.declaredMismatch(
                                expression.getDeclaredTypeVariable(), treatedSequenceType, expressionSequenceType),
                        expressionSequenceType,
                        treatedSequenceType,
                        expression);
            } else {
                // A matching value is passed unchanged, so it keeps its more precise type.
                treatedSequenceType = expressionSequenceType;
            }
        }
        if (SequenceType.createSequenceType("item*").equals(treatedSequenceType)) {
            treatedSequenceType = expressionSequenceType;
        }
        expression.setStaticSequenceType(treatedSequenceType);
        return argument;
    }

    @Override
    public StaticContext visitFunctionArgumentConversion(
            FunctionArgumentConversionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType argumentType = typeOf(expression.getArgument(), expression);
        SequenceType parameterType = expression.getParameterType();
        if (!FunctionConversionTyping.isCompatible(argumentType, parameterType)) {
            this.errors.report(
                    expression.isReturnValue()
                            ? TypeErrorMessages.returnMismatch(
                                    expression.getFunctionName(), parameterType, argumentType)
                            : TypeErrorMessages.argumentMismatch(
                                    expression.getFunctionName(), "this argument", parameterType, argumentType),
                    expression,
                    FunctionConversionTyping.mayConvertTo(argumentType, parameterType));
        }
        // A matching argument is passed unchanged, so it keeps its more precise type.
        expression.setStaticSequenceType(argumentType.isSubtypeOf(parameterType) ? argumentType : parameterType);
        return argument;
    }

    // endregion

    // region updating

    @Override
    public StaticContext visitDeleteExpression(DeleteExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitRenameExpression(RenameExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitReplaceExpression(ReplaceExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitInsertExpression(InsertExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitAppendExpression(AppendExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitTransformExpression(TransformExpression expression, StaticContext argument) {
        StaticContext copyVariables = expression.getStaticContext();
        for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
            visit(copyDecl.getSourceExpression(), argument);
            SequenceType sourceType = typeOf(copyDecl.getSourceExpression(), expression);
            copyVariables.replaceVariableSequenceType(copyDecl.getVariableName(), copiedType(sourceType));
        }
        visit(expression.getModifyExpression(), argument);
        visit(expression.getReturnExpression(), argument);
        expression.setStaticSequenceType(expression.getReturnExpression().getStaticSequenceType());

        return argument;
    }

    /** The kinds of items that an update can modify, keeping their kind. */
    private static final List<ItemType> UPDATABLE_KINDS = List.of(
            BuiltinTypesCatalogue.objectItem,
            BuiltinTypesCatalogue.arrayItem,
            BuiltinTypesCatalogue.elementNode,
            BuiltinTypesCatalogue.attributeNode,
            BuiltinTypesCatalogue.documentNode,
            BuiltinTypesCatalogue.textNode,
            BuiltinTypesCatalogue.commentNode,
            BuiltinTypesCatalogue.processingInstructionNode,
            BuiltinTypesCatalogue.namespaceNode);

    /**
     * The type of a copy in the modify and return clauses. An update keeps the kind of each item, but may change its
     * names, fields, members, content and type annotations, so only the kind of the source is known.
     */
    private static SequenceType copiedType(SequenceType sourceType) {
        if (sourceType.isEmptySequence()) {
            return sourceType;
        }
        List<ItemType> kinds = sourceType.getItemType().getMemberTypes().stream()
                .map(member -> UPDATABLE_KINDS.stream()
                        .filter(member::isSubtypeOf)
                        .findFirst()
                        .orElse(member))
                .collect(Collectors.toList());
        return new SequenceType(ItemTypeFactory.createInferredUnionType(kinds), sourceType.getCardinality());
    }

    @Override
    public StaticContext visitCreateCollectionExpression(
            CreateCollectionExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitDeleteIndexFromCollectionExpression(
            DeleteIndexFromCollectionExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitDeleteSearchFromCollectionExpression(
            DeleteSearchFromCollectionExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitEditCollectionExpression(EditCollectionExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitInsertIndexIntoCollectionExpression(
            InsertIndexIntoCollectionExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitInsertSearchIntoCollectionExpression(
            InsertSearchIntoCollectionExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitTruncateCollectionExpression(
            TruncateCollectionExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("()"), argument);
    }

    // endregion

    // region arithmetic

    @Override
    public StaticContext visitAdditiveExpr(AdditiveExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(this.arithmetic.inferAdditiveType(
                typeOf(expression.getLeftExpression(), expression),
                typeOf(expression.getRightExpression(), expression),
                expression.isMinus(),
                expression));
        return argument;
    }

    @Override
    public StaticContext visitMultiplicativeExpr(MultiplicativeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(this.arithmetic.inferMultiplicativeType(
                typeOf(expression.getLeftExpression(), expression),
                typeOf(expression.getRightExpression(), expression),
                expression.getMultiplicativeOperator(),
                expression));
        return argument;
    }

    @Override
    public StaticContext visitUnaryExpr(UnaryExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(this.arithmetic.inferUnaryType(
                typeOf(expression.getMainExpression(), expression), expression.isNegated(), expression));
        return argument;
    }

    // endregion

    // region logic

    /** Reports an operand that may have no effective boolean value, such as a map, where a condition is expected. */
    private void checkEffectiveBooleanValue(SequenceType type, String operand, Node node) {
        if (!type.hasEffectiveBooleanValue()) {
            this.errors.report(
                    TypeErrorMessages.noEffectiveBooleanValue(operand, type),
                    node,
                    type.mayHaveEffectiveBooleanValue());
        }
    }

    private StaticContext visitAndOrExpr(Expression expression, StaticContext argument, String operator) {
        visitDescendants(expression, argument);

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = typeOf((Expression) childrenExpressions.get(0), expression);
        SequenceType rightInferredType = typeOf((Expression) childrenExpressions.get(1), expression);

        checkEffectiveBooleanValue(leftInferredType, "The left operand of '" + operator + "'", expression);
        checkEffectiveBooleanValue(rightInferredType, "The right operand of '" + operator + "'", expression);

        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitAndExpr(AndExpression expression, StaticContext argument) {
        return visitAndOrExpr(expression, argument, "and");
    }

    @Override
    public StaticContext visitOrExpr(OrExpression expression, StaticContext argument) {
        return visitAndOrExpr(expression, argument, "or");
    }

    @Override
    public StaticContext visitNotExpr(NotExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType childInferredType = typeOf(expression.getMainExpression(), expression);
        checkEffectiveBooleanValue(childInferredType, "The operand of not", expression);

        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem));
        return argument;
    }

    // endregion

    // region comparison

    @Override
    public StaticContext visitComparisonExpr(ComparisonExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        List<Node> children = expression.getChildren();
        expression.setStaticSequenceType(this.comparison.inferComparisonType(
                typeOf((Expression) children.get(0), expression),
                typeOf((Expression) children.get(1), expression),
                expression.getComparisonOperator(),
                expression));
        return argument;
    }

    @Override
    public StaticContext visitNodeComparisonExpr(NodeComparisonExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(this.comparison.inferNodeComparisonType(
                typeOf(expression.getLeftExpression(), expression),
                typeOf(expression.getRightExpression(), expression),
                expression.getOperator().toString(),
                expression));
        return argument;
    }

    @Override
    public StaticContext visitNodeSetExpr(NodeSetExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType leftType = typeOf(expression.getLeftExpression(), expression);
        SequenceType rightType = typeOf(expression.getRightExpression(), expression);
        checkNodeSetOperand(leftType, "its left operand", expression);
        checkNodeSetOperand(rightType, "its right operand", expression);

        expression.setStaticSequenceType(
                new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceType.Arity.ZeroOrMore));
        return argument;
    }

    private void checkNodeSetOperand(SequenceType type, String operand, Node node) {
        if (!type.isEmptySequence() && !type.getItemType().isNodeItemType()) {
            this.errors.reportMismatch(
                    "A node set operator expects nodes, but " + operand + " is " + TypeNotation.shortened(type) + ".",
                    type,
                    SequenceType.createSequenceType("node()*"),
                    node);
        }
    }

    // endregion

    // region control

    @Override
    public StaticContext visitConditionalExpression(ConditionalExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(inferConditionalType(
                typeOf(expression.getCondition(), expression),
                typeOf(expression.getBranch(), expression),
                typeOf(expression.getElseBranch(), expression),
                expression));
        return argument;
    }

    private SequenceType inferConditionalType(
            SequenceType ifType, SequenceType thenType, SequenceType elseType, Node node) {
        checkEffectiveBooleanValue(ifType, "The condition of this if", node);

        // if the if branch is false at static time (i.e. subtype of null?) we only use else branch
        SequenceType resultingType = ifType.isSubtypeOf(SequenceType.createSequenceType("null?"))
                ? elseType
                : thenType.leastCommonSupertypeWith(elseType);

        this.errors.rejectInferredEmpty(resultingType, node, "every branch that it may take does");
        return resultingType;
    }

    // throw errors if [type] does not conform to switch test and cases requirements
    private void checkSwitchType(SequenceType type, Node switchNode) {
        if (type.isEmptySequence()) {
            return; // no further check is required
        }
        if (type.getArity() == SequenceType.Arity.OneOrMore || type.getArity() == SequenceType.Arity.ZeroOrMore) {
            this.errors.report(
                    TypeErrorMessages.severalValues("A switch", "one of its operands", type),
                    switchNode,
                    type.getCardinality().allowsZero() || type.getCardinality().allowsOne());
        }
        ItemType itemType = type.getItemType();
        if (itemType.isFunctionItemType()) {
            this.errors.report(
                    "A switch expects atomizable values, but one of its operands is "
                            + TypeNotation.shortened(type)
                            + ".",
                    ErrorCode.UnexpectedFunctionItem,
                    switchNode);
        }
        // the operands are atomized, so nodes and arrays are allowed, but not objects or maps
        if (!TypeAtomization.isAtomizable(itemType)) {
            this.errors.report(
                    TypeErrorMessages.notAtomizable("A switch", "one of its operands", type),
                    switchNode,
                    !TypeAtomization.isNeverAtomizable(itemType));
        }
    }

    // check the case conditions and tell whether any of them may match the test condition
    private boolean switchCaseMayMatch(List<Expression> conditions, SequenceType testType, Node node) {
        boolean mayMatch = false;
        for (Expression condition : conditions) {
            SequenceType conditionType = typeOf(condition, node);
            checkSwitchType(conditionType, node);
            if (conditionType.hasOverlapWith(testType)) {
                mayMatch = true;
            }
        }
        return mayMatch;
    }

    @Override
    public StaticContext visitSwitchExpression(SwitchExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType testType = typeOf(expression.getTestCondition(), expression);
        checkSwitchType(testType, expression);

        SequenceType returnType = typeOf(expression.getDefaultExpression(), expression);
        for (SwitchCase switchCase : expression.getCases()) {
            boolean mayMatch = switchCaseMayMatch(switchCase.getConditionExpressions(), testType, expression);
            SequenceType caseReturnType = typeOf(switchCase.getReturnExpression(), expression);
            if (mayMatch) {
                returnType = returnType.leastCommonSupertypeWith(caseReturnType);
            }
        }

        expression.setStaticSequenceType(returnType);
        return argument;
    }

    @Override
    public StaticContext visitTryCatchExpression(TryCatchExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        List<SequenceType> branchTypes = new ArrayList<>();
        for (Node child : expression.getChildren()) {
            branchTypes.add(typeOf((Expression) child, expression));
        }
        expression.setStaticSequenceType(inferBranchesType(branchTypes, expression));
        return argument;
    }

    // the least common supertype of the types a try/catch or typeswitch may return
    private SequenceType inferBranchesType(List<SequenceType> branchTypes, Node node) {
        SequenceType inferredType = StaticTypeErrors.requireInferredType(leastCommonSupertype(branchTypes), node);
        this.errors.rejectInferredEmpty(inferredType, node, "every branch does");
        return inferredType;
    }

    private static SequenceType leastCommonSupertype(List<SequenceType> types) {
        return types.stream().reduce(SequenceType::leastCommonSupertypeWith).orElse(null);
    }

    @Override
    public StaticContext visitTypeSwitchExpression(TypeSwitchExpression expression, StaticContext argument) {
        visit(expression.getTestCondition(), argument);
        SequenceType conditionType = typeOf(expression.getTestCondition(), expression);

        List<SequenceType> branchTypes = new ArrayList<>();
        for (TypeswitchCase typeswitchCase : expression.getCases()) {
            Expression returnExpression = typeswitchCase.getReturnExpression();
            bindCaseVariable(
                    typeswitchCase.getVariableName(),
                    leastCommonSupertype(typeswitchCase.getUnion()),
                    typeswitchCase.getStaticContext());
            visit(returnExpression, argument);
            branchTypes.add(typeOf(returnExpression, expression));
        }

        Expression defaultExpression = expression.getDefaultCase().getReturnExpression();
        bindCaseVariable(
                expression.getDefaultCase().getVariableName(),
                conditionType,
                expression.getDefaultCase().getStaticContext());
        visit(defaultExpression, argument);
        branchTypes.add(typeOf(defaultExpression, expression));

        expression.setStaticSequenceType(inferBranchesType(branchTypes, expression));
        return argument;
    }

    // a case variable is bound to the union of the case types, the default one to the test condition type
    private static void bindCaseVariable(Name variableName, SequenceType variableType, StaticContext caseContext) {
        if (variableName != null) {
            caseContext.replaceVariableSequenceType(variableName, variableType);
        }
    }

    // endregion

    // region miscellaneous

    @Override
    public StaticContext visitRangeExpr(RangeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> children = expression.getChildren();
        SequenceType leftType = typeOf((Expression) children.get(0), expression);
        SequenceType rightType = typeOf((Expression) children.get(1), expression);

        this.errors.rejectEmptyOperands(leftType, rightType, expression);

        SequenceType intOpt = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.OneOrZero);
        if (!leftType.isSubtypeOf(intOpt) || !rightType.isSubtypeOf(intOpt)) {
            this.errors.report(
                    "'to' expects operands of type xs:integer?, but they are "
                            + TypeNotation.shortened(leftType)
                            + " and "
                            + TypeNotation.shortened(rightType)
                            + ".",
                    expression,
                    leftType.mayMatch(intOpt) && rightType.mayMatch(intOpt));
        }

        expression.setStaticSequenceType(
                new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.ZeroOrMore));
        return argument;
    }

    @Override
    public StaticContext visitStringConcatExpr(StringConcatExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> children = expression.getChildren();
        SequenceType leftType = typeOf((Expression) children.get(0), expression);
        SequenceType rightType = typeOf((Expression) children.get(1), expression);

        AtomizedOperands.checkAtomizableOperands(this.errors, "||", leftType, rightType, expression);
        AtomizedOperands.checkSingletonOperands(this.errors, "||", leftType, rightType, expression);

        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.stringItem));
        return argument;
    }

    // endregion

    // region postfix

    /**
     * Sets the type of a lookup, whose items are unknown when its input or key is: the inferred type would no longer
     * show it.
     */
    private static void setLookupType(Expression lookup, SequenceType type) {
        lookup.setStaticSequenceType(StaticTypeErrors.hasUnknownOperand(lookup) ? StaticTypeErrors.UNKNOWN : type);
    }

    @Override
    public StaticContext visitArrayLookupExpression(ArrayLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        setLookupType(
                expression,
                this.lookups.inferArrayLookupType(
                        typeOf(expression.getMainExpression(), expression),
                        typeOf(expression.getLookupExpression(), expression),
                        expression));
        return argument;
    }

    @Override
    public StaticContext visitObjectLookupExpression(ObjectLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        String key =
                expression.getLookupExpression() instanceof StringLiteralExpression literal ? literal.getValue() : null;
        setLookupType(
                expression,
                this.lookups.inferObjectLookupType(
                        typeOf(expression.getMainExpression(), expression),
                        typeOf(expression.getLookupExpression(), expression),
                        key,
                        expression));
        return argument;
    }

    @Override
    public StaticContext visitPostfixLookupExpression(PostfixLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        Expression key = expression.getLookupExpression();
        setLookupType(
                expression,
                this.lookups.inferPostfixLookupType(
                        typeOf(expression.getMainExpression(), expression),
                        key == null ? null : typeOf(key, expression),
                        expression));
        return argument;
    }

    @Override
    public StaticContext visitUnaryLookupExpression(UnaryLookupExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("item*"), argument);
    }

    @Override
    public StaticContext visitArrayUnboxingExpression(ArrayUnboxingExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        setLookupType(
                expression,
                this.lookups.inferArrayUnboxingType(typeOf(expression.getMainExpression(), expression), expression));
        return argument;
    }

    @Override
    public StaticContext visitFilterExpression(FilterExpression expression, StaticContext argument) {
        visit(expression.getMainExpression(), argument);
        SequenceType mainType = typeOf(expression.getMainExpression(), expression);
        this.errors.rejectInferredEmpty(mainType, expression, "the sequence that it filters does");

        Expression predicateExpression = expression.getPredicateExpression();
        // set context item static type
        predicateExpression.getStaticContext().setContextItemStaticType(new SequenceType(mainType.getItemType()));
        visit(predicateExpression, argument);
        // unset context item static type
        predicateExpression.getStaticContext().setContextItemStaticType(null);
        SequenceType predicateType = typeOf(predicateExpression, expression);
        // An empty or null predicate is always false, so the filter selects nothing.
        if (predicateType.isSubtypeOf(SequenceType.createSequenceType("null?"))) {
            this.errors.reportInferredEmpty(expression, "its predicate is always false");
        }
        checkEffectiveBooleanValue(predicateType, "This predicate", expression);

        // Filtering at most one item, or selecting one position, keeps at most one item. A nonempty sequence always
        // has a first and a last item.
        boolean selectsOnePosition = predicateType.getItemType().equals(BuiltinTypesCatalogue.integerItem)
                && isSameForEveryItem(predicateExpression);
        boolean selectsAnEnd = isFirstPosition(predicateExpression) || isLastCall(predicateExpression);
        SequenceType.Arity inferredArity;
        if (selectsAnEnd && !mainType.getCardinality().allowsZero()) {
            inferredArity = SequenceType.Arity.One;
        } else if (mainType.isAritySubtypeOf(SequenceType.Arity.OneOrZero) || selectsOnePosition) {
            inferredArity = SequenceType.Arity.OneOrZero;
        } else {
            inferredArity = SequenceType.Arity.ZeroOrMore;
        }
        expression.setStaticSequenceType(new SequenceType(mainType.getItemType(), inferredArity));
        return argument;
    }

    private static boolean isFirstPosition(Expression predicate) {
        return predicate instanceof IntegerLiteralExpression literal
                && new BigInteger(literal.getLexicalValue()).equals(BigInteger.ONE);
    }

    /** Whether the predicate is last(), the size of the filtered sequence. */
    private static boolean isLastCall(Expression predicate) {
        if (!(predicate instanceof FunctionCallExpression call)) {
            return false;
        }
        return call.getArguments().isEmpty() && isBuiltinFunctionName(call.getFunctionName(), "last");
    }

    /**
     * Whether a predicate has the same value for every item it filters, so that an integer selects one position:
     * e.g. $s[1], $s[$i] or $s[last() - 1], but not $s[position()] or $s[.], which may select every item.
     */
    private static boolean isSameForEveryItem(Expression predicate) {
        if (predicate instanceof IntegerLiteralExpression
                || predicate instanceof VariableReferenceExpression
                || isLastCall(predicate)) {
            return true;
        }
        if (predicate instanceof AdditiveExpression
                || predicate instanceof MultiplicativeExpression
                || predicate instanceof UnaryExpression) {
            return predicate.getChildren().stream().allMatch(child -> isSameForEveryItem((Expression) child));
        }
        return false;
    }

    @Override
    public StaticContext visitDynamicFunctionCallExpression(
            DynamicFunctionCallExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType mainType = typeOf(expression.getMainExpression(), expression);
        if (mainType.isEmptySequence()) {
            expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
            return argument;
        }

        checkLookupCallArity(expression, mainType.getItemType());
        expression.setStaticSequenceType(inferDynamicFunctionCallType(expression, mainType.getItemType()));
        return argument;
    }

    /**
     * A map or an array is called with exactly one argument, the key or position to select, and fails with any other
     * number. The error is only possible when the called item may also be a function that takes that many.
     */
    private void checkLookupCallArity(DynamicFunctionCallExpression expression, ItemType type) {
        int arity = expression.getArguments().size();
        if (arity == 1 || type.getMemberTypes().stream().noneMatch(LookupTyping::isCallableLookupInput)) {
            return;
        }
        boolean mayPass = type.getMemberTypes().stream()
                .anyMatch(member -> member.isFunctionItemType()
                        && (member.equals(BuiltinTypesCatalogue.anyFunctionItem)
                                || member.getSignature().getParameterTypes().size() == arity));
        this.errors.report(
                "A map or an array takes one argument when it is called, but this call has " + arity + ".",
                expression,
                mayPass);
    }

    private SequenceType inferDynamicFunctionCallType(DynamicFunctionCallExpression expression, ItemType type) {
        if (type.isUnionType()) {
            SequenceType result = null;
            for (ItemType member : type.getTypes()) {
                SequenceType memberResult = inferDynamicFunctionCallType(expression, member);
                result = result == null ? memberResult : result.leastCommonSupertypeWith(memberResult);
            }
            return result;
        }
        if (LookupTyping.isCallableLookupInput(type)) {
            return LookupTyping.inferLookupCallType(type);
        }
        if (!type.isFunctionItemType()) {
            this.errors.report(
                    "Only a function, a map or an array can be called, but this is "
                            + TypeNotation.shortened(type)
                            + ".",
                    expression,
                    type.mayOverlapWith(BuiltinTypesCatalogue.anyFunctionItem));
            return SequenceType.createSequenceType("item*");
        }

        if (type.equals(BuiltinTypesCatalogue.anyFunctionItem)) {
            return SequenceType.createSequenceType("item*");
        }

        FunctionSignature signature = type.getSignature();
        List<SequenceType> formalParameterTypes = signature.getParameterTypes();
        if (formalParameterTypes.size() != expression.getArguments().size()) {
            this.errors.report(
                    "This function takes "
                            + formalParameterTypes.size()
                            + (formalParameterTypes.size() == 1 ? " argument" : " arguments")
                            + ", but this call has "
                            + expression.getArguments().size()
                            + ".",
                    expression);
            // With optional static typing disabled, an unreachable branch may still have
            // a mismatched signature. Leave enforcement to the runtime call if it is evaluated.
            return SequenceType.createSequenceType("item*");
        }
        // A dynamic call has no function name, so errors describe the function by its type.
        checkFunctionArguments(
                expression, TypeNotation.shortened(type), expression.getArguments(), formalParameterTypes);
        if (expression.getArguments().contains(null)) {
            return inferPartialApplicationType(expression.getArguments(), signature, expression.isUpdating());
        }
        return signature.getReturnType();
    }

    @Override
    public StaticContext visitSimpleMapExpr(SimpleMapExpression expression, StaticContext argument) {
        List<Node> nodes = expression.getChildren();
        Expression leftExpression = (Expression) nodes.get(0);
        Expression rightExpression = (Expression) nodes.get(1);

        visit(leftExpression, argument);
        SequenceType leftType = typeOf(leftExpression, expression);
        this.errors.rejectInferredEmpty(leftType, expression, "its left operand does");

        // set context item static type
        rightExpression.getStaticContext().setContextItemStaticType(new SequenceType(leftType.getItemType()));
        visit(rightExpression, argument);
        rightExpression.getStaticContext().setContextItemStaticType(null);

        SequenceType rightType = typeOf(rightExpression, expression);
        this.errors.rejectInferredEmpty(rightType, expression, "its right operand does");

        // The right expression is evaluated once per item of the left expression.
        SequenceCardinality resultingCardinality = rightType.getCardinality().repeated(leftType.getCardinality());
        expression.setStaticSequenceType(new SequenceType(rightType.getItemType(), resultingCardinality));
        return argument;
    }

    // endregion

    // region FLOWR

    /**
     * Visits the clauses of a FLWOR expression or statement, starting with the given clause,
     * and returns how many tuples can reach the return clause.
     */
    private SequenceCardinality visitFlworClauses(Clause clause) {
        SequenceCardinality forCardinality = SequenceCardinality.ONE;
        while (clause != null) {
            this.visit(clause, clause.getStaticContext());
            if (clause.getClauseType() == FLWOR_CLAUSES.FOR) {
                // Each tuple so far is repeated once per item of the for clause's sequence.
                SequenceType forType = ((ForClause) clause).getExpression().getStaticSequenceType();
                if (!forType.isEmptySequence()) {
                    SequenceCardinality sourceCardinality = forType.getCardinality();
                    if (((ForClause) clause).isAllowEmpty()) {
                        // An empty source still emits one tuple with an empty binding.
                        sourceCardinality = sourceCardinality.replaceZeroWithOne();
                    }
                    forCardinality = sourceCardinality.repeated(forCardinality);
                } else if (!((ForClause) clause).isAllowEmpty()) {
                    // Without allowing empty, an empty source produces no tuples;
                    // with it, each tuple continues once with an empty binding.
                    forCardinality = SequenceCardinality.EMPTY;
                }
            } else if (clause.getClauseType() == FLWOR_CLAUSES.GROUP_BY) {
                // Multiple input tuples can collapse into a single group.
                forCardinality = forCardinality.grouped();
            } else if (clause.getClauseType() == FLWOR_CLAUSES.WHERE) {
                // Filtering tuples can leave zero, one, or multiple tuples.
                forCardinality = forCardinality.filtered();
            } else if (clause.getClauseType() == FLWOR_CLAUSES.WINDOW) {
                forCardinality = SequenceCardinality.ANY;
            }
            clause = clause.getNextClause();
        }

        return forCardinality;
    }

    @Override
    public StaticContext visitFlowrExpression(FlworExpression expression, StaticContext argument) {
        SequenceCardinality forCardinality =
                visitFlworClauses(expression.getReturnClause().getFirstClause());
        SequenceType returnType = typeOf(expression.getReturnClause().getReturnExpr(), expression);
        this.errors.rejectInferredEmpty(returnType, expression, "its return expression does");
        returnType = new SequenceType(
                returnType.getItemType(), returnType.getCardinality().repeated(forCardinality));
        expression.setStaticSequenceType(returnType);
        return argument;
    }

    @Override
    public StaticContext visitForClause(ForClause expression, StaticContext argument) {
        visit(expression.getExpression(), argument);
        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType =
                declaredType == null ? typeOf(expression.getExpression(), expression) : declaredType;
        if (inferredType.isEmptySequence()) {
            if (!expression.isAllowEmpty()) {
                // for sure we will not have any tuple to process and return the empty sequence
                this.errors.reportInferredEmpty(
                        "This for clause iterates over the empty sequence, so its FLWOR expression always returns the"
                                + " empty sequence.",
                        expression);
                // The variable is never bound, so its uses would only repeat the error.
                if (this.errors.hasFailed(expression)) {
                    inferredType = StaticTypeErrors.UNKNOWN;
                }
            }
        } else {
            // we take the single arity version of the inferred type or optional arity if we allow empty and the
            // sequence allows () (i.e. arity ? or *)
            if (expression.isAllowEmpty()
                    && (inferredType.getArity() == SequenceType.Arity.OneOrZero
                            || inferredType.getArity() == SequenceType.Arity.ZeroOrMore)) {
                inferredType = new SequenceType(inferredType.getItemType(), SequenceType.Arity.OneOrZero);
            } else {
                inferredType = new SequenceType(inferredType.getItemType());
            }
        }

        if (declaredType == null) {
            expression
                    .getNextClause()
                    .getStaticContext()
                    .replaceVariableSequenceType(expression.getVariableName(), inferredType);
        }
        return argument;
    }

    @Override
    public StaticContext visitWindowClause(WindowClause expression, StaticContext argument) {
        visit(expression.getExpression(), argument);
        // Condition variables bind items of the input, while a declared type applies to the window variable.
        ItemType inputItemType = typeOf(expression.getExpression(), expression).getItemType();
        WindowClause.WindowCondition start = expression.getStartCondition();
        WindowClause.WindowCondition end = expression.getEndCondition();
        refineWindowConditionVariables(
                start.variables(), inputItemType, start.expression().getStaticContext());
        visit(start.expression(), start.expression().getStaticContext());
        checkWindowConditionType(start.expression(), expression);
        if (end != null) {
            refineWindowConditionVariables(
                    end.variables(), inputItemType, end.expression().getStaticContext());
            visit(end.expression(), end.expression().getStaticContext());
            checkWindowConditionType(end.expression(), expression);
        }

        StaticContext followingContext = expression.getNextClause().getStaticContext();
        if (expression.getActualSequenceType() == null) {
            // A window always contains at least one item.
            followingContext.replaceVariableSequenceType(
                    expression.getWindowVariable(), new SequenceType(inputItemType, SequenceType.Arity.OneOrMore));
        }
        refineWindowConditionVariables(start.variables(), inputItemType, followingContext);
        if (end != null) {
            refineWindowConditionVariables(end.variables(), inputItemType, followingContext);
        }
        return argument;
    }

    // The current item always exists, while the previous and next items may not.
    private static void refineWindowConditionVariables(
            WindowClause.WindowVars variables, ItemType inputItemType, StaticContext context) {
        if (variables.currentItem() != null) {
            context.replaceVariableSequenceType(variables.currentItem(), new SequenceType(inputItemType));
        }
        for (Name item : Arrays.asList(variables.previousItem(), variables.nextItem())) {
            if (item != null) {
                context.replaceVariableSequenceType(
                        item, new SequenceType(inputItemType, SequenceType.Arity.OneOrZero));
            }
        }
    }

    private void checkWindowConditionType(Expression condition, WindowClause clause) {
        SequenceType conditionType = typeOf(condition, clause);
        checkEffectiveBooleanValue(conditionType, "This window condition", clause);
    }

    @Override
    public StaticContext visitLetClause(LetClause expression, StaticContext argument) {
        visit(expression.getExpression(), argument);
        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType = typeOf(expression.getExpression(), expression);
        if (declaredType == null) {
            expression
                    .getNextClause()
                    .getStaticContext()
                    .replaceVariableSequenceType(expression.getVariableName(), inferredType);
        }
        expression.setStaticType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitWhereClause(WhereClause expression, StaticContext argument) {
        visit(expression.getWhereExpression(), argument);
        SequenceType whereType = typeOf(expression.getWhereExpression(), expression);
        checkEffectiveBooleanValue(whereType, "The condition of this where clause", expression);
        if (whereType.isEmptySequence() || whereType.isSubtypeOf(SequenceType.createSequenceType("null?"))) {
            this.errors.reportInferredEmpty(
                    "This where clause is always false, so its FLWOR expression always returns the empty sequence.",
                    expression);
        }
        return argument;
    }

    @Override
    public StaticContext visitGroupByClause(GroupByClause expression, StaticContext argument) {
        // group by cannot be the last clause of a FLWOR expression
        StaticContext nextClauseContext = expression.getNextClause().getStaticContext();
        Map<Name, SequenceType> groupingVariableTypes = new HashMap<>();
        for (GroupByVariableDeclaration groupByVar : expression.getGroupVariables()) {
            groupingVariableTypes.put(groupByVar.getVariableName(), inferGroupingVariableType(groupByVar, expression));
        }
        // the other variables bound so far now hold the values of every tuple in a group, so ? becomes * and 1
        // becomes +
        nextClauseContext.incrementArities(nextClauseContext, groupingVariableTypes.keySet());
        groupingVariableTypes.forEach(nextClauseContext::replaceVariableSequenceType);
        return argument;
    }

    /** A grouping variable is bound to its atomized grouping key, which the runtime also checks. */
    private SequenceType inferGroupingVariableType(GroupByVariableDeclaration groupByVar, GroupByClause clause) {
        Name variableName = groupByVar.getVariableName();
        Expression keyExpression = groupByVar.getExpression();
        SequenceType keyType;
        if (keyExpression == null) {
            // grouping by a variable in scope, which is how XQuery bindings are normalized, uses its value as the key
            keyType = clause.getStaticContext().getVariableSequenceType(variableName);
        } else {
            visit(keyExpression, keyExpression.getStaticContext());
            keyType = typeOf(keyExpression, clause);
        }
        SequenceType declaredType = groupByVar.getActualSequenceType();
        // A key that failed was reported where it was typed. A variable in scope is not an operand of the clause, so
        // its checks would not be suppressed as those of a key expression are.
        boolean unknownKey = StaticTypeErrors.isUnknown(keyType);
        if (unknownKey && declaredType == null) {
            return keyType;
        }
        SequenceType atomizedKeyType =
                unknownKey ? null : checkSingleAtomizedKey(keyType, "A group by clause", "$" + variableName, clause);
        if (atomizedKeyType == null) {
            // without static typing, the runtime atomizes the key or fails
            atomizedKeyType = SequenceType.createSequenceType("anyAtomicType?");
        }
        if (declaredType == null) {
            return atomizedKeyType;
        }
        if (!declaredType.isSubtypeOf(SequenceType.createSequenceType("anyAtomicType?"))) {
            this.errors.report(
                    "A group by clause expects an atomic declared type, but $"
                            + variableName
                            + " is declared as "
                            + TypeNotation.shortened(declaredType)
                            + ".",
                    clause);
        }
        // a typed value only known at runtime is checked against the declared type then, and so is the number of values
        // of an array, of which only the type is checked here
        boolean unknownCount = !keyType.isEmptySequence()
                && !atomizedKeyType.isEmptySequence()
                && TypeAtomization.hasUnknownAtomizedCount(keyType.getItemType());
        SequenceType checkedKeyType = unknownCount
                ? new SequenceType(atomizedKeyType.getItemType(), declaredType.getArity())
                : atomizedKeyType;
        if (!unknownKey
                && !TypeAtomization.hasUnknownAtomizedType(keyType.getItemType())
                && !checkedKeyType.isSubtypeOf(declaredType)) {
            this.errors.report(
                    "$"
                            + variableName
                            + " is declared as "
                            + TypeNotation.shortened(declaredType)
                            + ", but its key is "
                            + TypeNotation.shortened(atomizedKeyType)
                            + ".",
                    clause,
                    atomizedKeyType.mayMatch(declaredType));
        }
        return declaredType;
    }

    @Override
    public StaticContext visitOrderByClause(OrderByClause expression, StaticContext argument) {
        for (OrderByClauseSortingKey orderClause : expression.getSortingKeys()) {
            visit(orderClause.getExpression(), argument);
            SequenceType keyType = typeOf(orderClause.getExpression(), expression);
            SequenceType atomizedKeyType =
                    checkSingleAtomizedKey(keyType, "An order by clause", "this key", expression);
            // every key is compared with the others, like the input of fn:min and fn:max; a typed value only known at
            // runtime is compared then
            if (atomizedKeyType != null
                    && !atomizedKeyType.isEmptySequence()
                    && !TypeAtomization.hasUnknownAtomizedType(keyType.getItemType())
                    && !ComparisonTyping.hasMutuallyComparableItems(atomizedKeyType.getItemType())) {
                this.errors.report(
                        "An order by clause expects keys that can be compared with each other, but this key is "
                                + AtomizedOperands.describe(keyType)
                                + ".",
                        expression,
                        mayAtomizeToAtMostOne(keyType) && TypeAtomization.mayAtomizeToAnyType(keyType.getItemType()));
            }
        }
        return argument;
    }

    /**
     * Checks that a group-by or order-by key atomizes to at most one atomic value, and returns the type of that value.
     * Returns null when it may not, which is reported unless static typing is off: with FOTY0013 when it can never be
     * atomized, such as an object, and XPTY0004 otherwise.
     */
    private SequenceType checkSingleAtomizedKey(SequenceType keyType, String clause, String key, Node node) {
        if (!keyType.isEmptySequence() && !TypeAtomization.isAtomizable(keyType.getItemType())) {
            this.errors.report(TypeErrorMessages.notAtomizable(clause, key, keyType), node, false);
            return null;
        }
        SequenceType atomizedKeyType = singleAtomizedKeyType(keyType);
        if (atomizedKeyType == null) {
            this.errors.report(
                    TypeErrorMessages.severalValues(clause, key, keyType), node, mayAtomizeToAtMostOne(keyType));
        }
        return atomizedKeyType;
    }

    /**
     * Returns the type of the at most one atomic value a group-by or order-by key atomizes to. Returns null when the
     * key may atomize to more than one value, or is not atomizable. As for singleton operators, a typed value only
     * known at runtime is assumed to be a single value, and checked then.
     */
    private SequenceType singleAtomizedKeyType(SequenceType keyType) {
        if (keyType.isEmptySequence()) {
            return keyType;
        }
        SequenceType.Arity arity = AtomizedOperands.atomizedSingletonArity(keyType);
        if (arity == null || !TypeAtomization.isAtomizable(keyType.getItemType())) {
            return null;
        }
        return new SequenceType(TypeAtomization.atomizedItemType(keyType.getItemType()), arity);
    }

    /** Whether some value of a group-by or order-by key atomizes to at most one atomic value. */
    private static boolean mayAtomizeToAtMostOne(SequenceType keyType) {
        return keyType.isEmptySequence()
                || (AtomizedOperands.mayBeSingleton(keyType)
                        && !TypeAtomization.isNeverAtomizable(keyType.getItemType()));
    }

    @Override
    public StaticContext visitCountClause(CountClause clause, StaticContext argument) {
        clause.getNextClause()
                .getStaticContext()
                .replaceVariableSequenceType(clause.getCountVariableName(), SequenceType.createSequenceType("integer"));
        return argument;
    }

    // endregion

    // region module

    /**
     * An assignable variable without a declared type can later be assigned any value. A variable whose value could not
     * be typed is unknown, even if it is assignable, so that its uses do not report errors that follow from its value.
     */
    private static SequenceType inferUndeclaredVariableType(Expression initializer, boolean assignable, Node node) {
        if (initializer == null) {
            return SequenceType.createSequenceType("item*");
        }
        SequenceType type = typeOf(initializer, node);
        return assignable && !StaticTypeErrors.isUnknown(type) ? SequenceType.createSequenceType("item*") : type;
    }

    @Override
    public StaticContext visitVariableDeclaration(VariableDeclaration expression, StaticContext argument) {
        visitDescendants(expression, argument);
        if (expression.getActualSequenceType() == null) {
            argument.replaceVariableSequenceType(
                    expression.getVariableName(),
                    inferUndeclaredVariableType(
                            expression.getExpression(),
                            argument.getIsAssignable(expression.getVariableName()),
                            expression));
        }
        // A non-external context initializer fixes the initial focus more precisely than its declared item type.
        // XQuery supplies item() even when the declaration has no explicit type annotation.
        if (Name.CONTEXT_ITEM.equals(expression.getVariableName())
                && !expression.external()
                && expression.getExpression() != null) {
            Expression initializer = expression.getExpression();
            if (initializer instanceof TreatExpression treat) {
                initializer = treat.getMainExpression();
            }
            SequenceType initializerType = initializer.getStaticSequenceType();
            if (initializerType.isSubtypeOf(argument.getVariableSequenceType(Name.CONTEXT_ITEM))) {
                argument.replaceVariableSequenceType(Name.CONTEXT_ITEM, initializerType);
            }
        }
        return argument;
    }

    @Override
    public StaticContext visitFunctionDeclaration(FunctionDeclaration expression, StaticContext argument) {
        visitDescendants(expression, argument);
        InlineFunctionExpression function = (InlineFunctionExpression) expression.getExpression();
        // Dependencies are inferred first. Cyclic signatures stay conservative until fixed-point inference is
        // supported.
        if (function.getActualReturnType() == null && !expression.hasCyclicTypeDependencies()) {
            argument.getFunctionSignature(expression.getFunctionIdentifier())
                    .refineReturnType(functionReturnType(function));
        }
        return argument;
    }

    /**
     * The expression that gives a function body its result, where a return type mismatch is reported rather than on
     * the whole function. It is the return expression of a FLWOR with only let clauses, which has the type of the
     * FLWOR. Other clauses can change the number of results, so other FLWORs are reported as a whole.
     */
    private static Expression resultExpression(Expression body) {
        if (!(body instanceof FlworExpression flwor)) {
            return body;
        }
        ReturnClause returnClause = flwor.getReturnClause();
        for (Clause clause = returnClause.getPreviousClause(); clause != null; clause = clause.getPreviousClause()) {
            if (clause.getClauseType() != FLWOR_CLAUSES.LET) {
                return body;
            }
        }
        return resultExpression(returnClause.getReturnExpr());
    }

    @Override
    public StaticContext visitMainModule(MainModule mainModule, StaticContext argument) {
        StaticContext generatedContext = visitDescendants(mainModule, mainModule.getStaticContext());
        return generatedContext;
    }

    @Override
    public StaticContext visitLibraryModule(LibraryModule libraryModule, StaticContext argument) {
        visitDescendants(libraryModule, libraryModule.getStaticContext());
        return argument;
    }

    @Override
    public StaticContext visitProlog(Prolog prolog, StaticContext argument) {
        for (Node child : prolog.getChildren()) {
            StaticContext declarationContext = prolog.getDeclarationContext(child, argument);
            visit(child, declarationContext);
            // Inferring a variable's type replaces its entry in one context only.
            if (!prolog.getSourceModuleContexts().isEmpty()) {
                prolog.shareGroupDeclarations(declarationContext, argument);
            }
        }
        return argument;
    }

    @Override
    public StaticContext visitValidateTypeExpression(ValidateTypeExpression expression, StaticContext argument) {
        visitDescendants(expression, expression.getStaticContext());
        SequenceType sourceType = expression.getMainExpression().getStaticSequenceType();
        expression.setStaticSequenceType(
                expression.getSequenceType().refineCardinalityIfSubtype(sourceType.getCardinality()));
        return argument;
    }

    @Override
    public StaticContext visitValidateExpression(ValidateExpression expression, StaticContext argument) {
        visitDescendants(expression, expression.getStaticContext());
        if (expression.getValidationMode() == ValidateExpression.ValidationMode.TYPE) {
            Name typeName = expression.getTypeName();
            boolean builtInType =
                    Name.XS_NS.equals(typeName.getNamespace()) && BuiltinTypesCatalogue.typeExists(typeName);
            XmlSchemaCatalog schemaCatalog =
                    expression.getStaticContext().getInScopeSchemaTypes().getXmlSchemaCatalog();
            boolean importedType = schemaCatalog.getTypeDefinition(typeName).isPresent();
            if (!builtInType && !importedType) {
                this.errors.reportStaticError(
                        new SemanticException(
                                "The type " + typeName + " is not defined in the in-scope schema types.",
                                ErrorCode.ValidateTypeNotFoundErrorCode,
                                expression.getMetadata()),
                        expression);
                return argument;
            }
            if (builtInType) {
                ItemType targetType = BuiltinTypesCatalogue.getItemTypeByName(typeName);
                if (!targetType.isAtomicItemType()
                        || targetType.equals(BuiltinTypesCatalogue.atomicItem)
                        || targetType.equals(BuiltinTypesCatalogue.NOTATIONItem)) {
                    this.errors.reportStaticError(
                            new UnsupportedFeatureException(
                                    "Validate type currently supports concrete built-in XML Schema atomic types.",
                                    expression.getMetadata()),
                            expression);
                    return argument;
                }
            }
        }
        // Preserve a statically known element or document subtype; otherwise use node(), since invalid operand kinds
        // are reported dynamically as XQTY0030.
        ItemType sourceItemType =
                expression.getMainExpression().getStaticSequenceType().getItemType();
        ItemType resultItemType = sourceItemType.isSubtypeOf(BuiltinTypesCatalogue.elementNode)
                        || sourceItemType.isSubtypeOf(BuiltinTypesCatalogue.documentNode)
                ? withoutTypeAnnotation(sourceItemType)
                : BuiltinTypesCatalogue.nodeItem;
        resultItemType = inferValidatedType(expression, sourceItemType).orElse(resultItemType);

        // Successful validation always returns exactly one copied node.
        expression.setStaticSequenceType(new SequenceType(resultItemType, SequenceType.Arity.One));
        return argument;
    }

    /**
     * Validation annotates the copy anew, so its type annotation is dropped: element(a, xs:untyped) becomes element(a),
     * and document-node(element(a, xs:untyped)) becomes document-node(element(a)).
     */
    private static ItemType withoutTypeAnnotation(ItemType source) {
        if (source instanceof DocumentNodeItemType document && document.getElementTestType() != null) {
            ItemType element = withoutTypeAnnotation(document.getElementTestType());
            return element == document.getElementTestType() ? source : ItemTypeFactory.documentNodeItemType(element);
        }
        if (!(source instanceof ElementNodeItemType element)
                || source instanceof SchemaElementNodeItemType
                || element.getSchemaTypeName() == null) {
            return source;
        }
        return element.getNodeName() == null
                ? BuiltinTypesCatalogue.elementNode
                : ItemTypeFactory.elementNodeItemType(element.getNodeName());
    }

    /** Validating a document validates its single element child. */
    private Optional<ItemType> inferValidatedType(ValidateExpression expression, ItemType source) {
        if (source instanceof DocumentNodeItemType document) {
            ItemType root = document.getElementTestType() == null
                    ? BuiltinTypesCatalogue.elementNode
                    : document.getElementTestType();
            return inferValidatedElementType(expression, root).map(ItemTypeFactory::documentNodeItemType);
        }
        return inferValidatedElementType(expression, source);
    }

    /** The validated copy of an element is annotated by its global declaration or by the requested type. */
    private Optional<ItemType> inferValidatedElementType(ValidateExpression expression, ItemType source) {
        if (!(source instanceof ElementNodeItemType element) || source instanceof SchemaElementNodeItemType) {
            return Optional.empty();
        }
        XmlSchemaCatalog schemaCatalog =
                expression.getStaticContext().getInScopeSchemaTypes().getXmlSchemaCatalog();
        if (expression.getValidationMode() == ValidateExpression.ValidationMode.TYPE) {
            return Optional.of(schemaCatalog.getElementTest(
                    element.getNodeName(), expression.getTypeName(), false, expression.getMetadata()));
        }
        // Strict validation requires a global declaration with the element's name (XQDY0084), so with a single
        // declaration the name is known. Lax validation leaves an element without a declaration unannotated.
        Name name = element.getNodeName();
        if (name == null && expression.getValidationMode() == ValidateExpression.ValidationMode.STRICT) {
            name = schemaCatalog.getOnlyElementDeclarationName().orElse(null);
        }
        return Optional.ofNullable(name).flatMap(schemaCatalog::findSchemaElementTest);
    }

    // endregion
    //
    // region scripting

    @Override
    public StaticContext visitBlockStatement(BlockStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);

        SequenceType inferredType = SequenceType.createSequenceType("()");

        for (Statement childStatement : statement.getBlockStatements()) {
            inferredType = inferredType.concatenateWith(typeOf(childStatement, statement));
        }

        statement.setStaticSequenceType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitApplyStatement(ApplyStatement statement, StaticContext argument) {
        visit(statement.getApplyExpression(), argument);
        statement.setStaticSequenceType(statement.getApplyExpression().getStaticSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitAssignStatement(AssignStatement statement, StaticContext argument) {
        visit(statement.getAssignExpression(), argument);
        SequenceType variableDeclaredType = statement.getStaticContext().getVariableSequenceType(statement.getName());
        SequenceType expressionType = statement.getAssignExpression().getStaticSequenceType();
        if (!expressionType.isSubtypeOf(variableDeclaredType)) {
            this.errors.reportMismatch(
                    TypeErrorMessages.declaredMismatch(statement.getName(), variableDeclaredType, expressionType),
                    expressionType,
                    variableDeclaredType,
                    statement);
        }
        // We take the declared type
        statement.setStaticSequenceType(variableDeclaredType);
        return argument;
    }

    @Override
    public StaticContext visitBreakStatement(BreakStatement statement, StaticContext argument) {
        statement.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitContinueStatement(ContinueStatement statement, StaticContext argument) {
        statement.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitExitStatement(ExitStatement statement, StaticContext argument) {
        visit(statement.getExitExpression(), argument);
        statement.setStaticSequenceType(statement.getExitExpression().getStaticSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitWhileStatement(WhileStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        statement.setStaticSequenceType(statement.getStatement().getStaticSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitFlowrStatement(FlowrStatement statement, StaticContext argument) {
        visitFlworClauses(statement.getReturnStatementClause().getFirstClause());
        SequenceType returnType = typeOf(statement.getReturnStatementClause().getReturnStatement(), statement);
        this.errors.rejectInferredEmpty(returnType, statement, "its return statement does");
        statement.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitConditionalStatement(ConditionalStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        statement.setStaticSequenceType(inferConditionalType(
                typeOf(statement.getCondition(), statement),
                typeOf(statement.getBranch(), statement),
                typeOf(statement.getElseBranch(), statement),
                statement));
        return argument;
    }

    @Override
    public StaticContext visitSwitchStatement(SwitchStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        SequenceType testType = typeOf(statement.getTestCondition(), statement);
        checkSwitchType(testType, statement);

        SequenceType returnType = typeOf(statement.getDefaultStatement(), statement);
        for (SwitchCaseStatement switchCase : statement.getCases()) {
            boolean mayMatch = switchCaseMayMatch(switchCase.getConditionExpressions(), testType, statement);
            SequenceType caseReturnType = typeOf(switchCase.getReturnStatement(), statement);
            if (mayMatch) {
                returnType = returnType.leastCommonSupertypeWith(caseReturnType);
            }
        }

        statement.setStaticSequenceType(returnType);
        return argument;
    }

    @Override
    public StaticContext visitTryCatchStatement(TryCatchStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        List<SequenceType> branchTypes = new ArrayList<>();
        for (Node child : statement.getChildren()) {
            branchTypes.add(typeOf((BlockStatement) child, statement));
        }
        statement.setStaticSequenceType(inferBranchesType(branchTypes, statement));
        return argument;
    }

    @Override
    public StaticContext visitTypeSwitchStatement(TypeSwitchStatement statement, StaticContext argument) {
        visit(statement.getTestCondition(), argument);
        SequenceType conditionType = typeOf(statement.getTestCondition(), statement);

        List<SequenceType> branchTypes = new ArrayList<>();
        for (TypeSwitchStatementCase typeswitchCase : statement.getCases()) {
            Statement returnStatement = typeswitchCase.getReturnStatement();
            bindCaseVariable(
                    typeswitchCase.getVariableName(),
                    leastCommonSupertype(typeswitchCase.getUnion()),
                    typeswitchCase.getStaticContext());
            visit(returnStatement, argument);
            branchTypes.add(typeOf(returnStatement, statement));
        }

        Statement defaultStatement = statement.getDefaultCase().getReturnStatement();
        bindCaseVariable(
                statement.getDefaultCase().getVariableName(),
                conditionType,
                statement.getDefaultCase().getStaticContext());
        visit(defaultStatement, argument);
        branchTypes.add(typeOf(defaultStatement, statement));

        statement.setStaticSequenceType(inferBranchesType(branchTypes, statement));
        return argument;
    }

    @Override
    public StaticContext visitVariableDeclStatement(VariableDeclStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        if (statement.getActualSequenceType() == null) {
            statement
                    .getStaticContext()
                    .replaceVariableSequenceType(
                            statement.getVariableName(),
                            inferUndeclaredVariableType(
                                    statement.getVariableExpression(), statement.isAssignable(), statement));
        }
        statement.setStaticSequenceType(
                statement.getStaticContext().getVariableSequenceType(statement.getVariableName()));
        return argument;
    }

    @Override
    public StaticContext visitCommaVariableDeclStatement(CommaVariableDeclStatement statement, StaticContext argument) {
        return withType(statement, SequenceType.createSequenceType("()"), argument);
    }

    @Override
    public StaticContext visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr statementsAndOptionalExpr, StaticContext argument) {
        SequenceType inferredType = SequenceType.createSequenceType("()");
        visitDescendants(statementsAndOptionalExpr, argument);
        if (statementsAndOptionalExpr.getExpression() != null) {
            inferredType = inferredType.concatenateWith(
                    typeOf(statementsAndOptionalExpr.getExpression(), statementsAndOptionalExpr));
        }
        statementsAndOptionalExpr.setStaticSequenceType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitStatementsAndExpr(StatementsAndExpr statementsAndExpr, StaticContext argument) {
        visitDescendants(statementsAndExpr, argument);
        SequenceType inferredType = SequenceType.createSequenceType("()")
                .concatenateWith(typeOf(statementsAndExpr.getExpression(), statementsAndExpr));

        statementsAndExpr.setStaticSequenceType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitBlockExpr(BlockExpression expression, StaticContext argument) {
        visitStatementsAndExpr(expression.getStatementsAndExpr(), argument);
        expression.setStaticSequenceType(expression.getStatementsAndExpr().getStaticSequenceType());
        return argument;
    }

    // endregion

    // region xml

    @Override
    public StaticContext visitSlashExpr(SlashExpr slashExpr, StaticContext argument) {
        visit(slashExpr.getLeftExpression(), argument);
        SequenceType leftType = typeOf(slashExpr.getLeftExpression(), slashExpr);

        Expression rightExpression = slashExpr.getRightExpression();
        rightExpression.getStaticContext().setContextItemStaticType(new SequenceType(leftType.getItemType()));
        try {
            visit(rightExpression, argument);
        } finally {
            rightExpression.getStaticContext().setContextItemStaticType(null);
        }
        SequenceType rightType = typeOf(rightExpression, slashExpr);

        // XPath removes duplicate nodes, so multiple inputs need not yield multiple results.
        SequenceType.Arity resultingArity = leftType.getArity().multiplyWith(rightType.getArity());
        slashExpr.setStaticSequenceType(new SequenceType(rightType.getItemType(), resultingArity));
        return argument;
    }

    @Override
    public StaticContext visitStepExpr(StepExpr stepExpr, StaticContext argument) {
        stepExpr.setStaticSequenceType(this.pathSteps.inferStepType(stepExpr));
        return argument;
    }

    // endregion
}
