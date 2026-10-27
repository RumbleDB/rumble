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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rumbledb.compiler.typing.ArithmeticTyping;
import org.rumbledb.compiler.typing.AtomizedOperands;
import org.rumbledb.compiler.typing.BuiltinFunctionTyping;
import org.rumbledb.compiler.typing.CastTyping;
import org.rumbledb.compiler.typing.ComparisonTyping;
import org.rumbledb.compiler.typing.LookupTyping;
import org.rumbledb.compiler.typing.PathStepTyping;
import org.rumbledb.compiler.typing.StaticTypeErrors;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IsStaticallyUnexpectedTypeException;
import org.rumbledb.exceptions.OurBadException;
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
import org.rumbledb.runtime.functions.ConstructorFunctionIterator;
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
public class InferTypeVisitor extends AbstractNodeVisitor<StaticContext> {

    private final RumbleConfiguration rumbleRuntimeConfiguration;

    private final StaticTypeErrors errors;

    private final ArithmeticTyping arithmetic;

    private final ComparisonTyping comparison;

    private final CastTyping cast;

    private final BuiltinFunctionTyping builtinFunctions;

    private final PathStepTyping pathSteps;

    private final LookupTyping lookups;

    /**
     * Builds a new visitor.
     *
     * @param rumbleRuntimeConfiguration the configuration.
     */
    InferTypeVisitor(RumbleConfiguration rumbleRuntimeConfiguration) {
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
        this.errors = new StaticTypeErrors(rumbleRuntimeConfiguration);
        this.arithmetic = new ArithmeticTyping(this.errors);
        this.comparison = new ComparisonTyping(this.errors);
        this.cast = new CastTyping(this.errors);
        this.builtinFunctions = new BuiltinFunctionTyping(this.errors);
        this.pathSteps = new PathStepTyping(this.errors);
        this.lookups = new LookupTyping(this.errors);
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
            // Without a declared type, the type was inferred when the variable was bound.
            variableType = expression.getStaticContext().getVariableSequenceType(expression.getVariableName());
            if (variableType == null) {
                throw new OurBadException(
                        "No type was inferred for $" + expression.getVariableName() + " before its use",
                        expression.getMetadata());
            }
            expression.setActualType(variableType);
        }
        this.errors.rejectInferredEmpty(variableType, expression);
        expression.setStaticSequenceType(variableType);
        return argument;
    }

    @Override
    public StaticContext visitArrayConstructor(ArrayConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        if (expression.isFixedSlotsArrayConstructor()) {
            // Conservative: type as array(*) for fixed-slots constructors for now.
            expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.arrayItem));
            return argument;
        }
        Expression contentExpr = expression.getExpression();
        if (contentExpr == null) {
            expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.arrayItem));
            return argument;
        }
        ItemType contentItemType = typeOf(contentExpr, expression).getItemType();
        ItemType arrayType = ItemTypeFactory.createAnonymousArrayType(contentItemType);
        expression.setStaticSequenceType(new SequenceType(arrayType));
        return argument;
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
            if (!childSequenceType.isSubtypeOf(SequenceType.createSequenceType("object*"))) {
                this.errors.report(
                        "The child expression must have object* sequence type, instead found: " + childSequenceType,
                        expression.getMetadata());
            }
        } else {
            for (Expression keyExpression : expression.getKeys()) {
                SequenceType keySequenceType = typeOf(keyExpression, expression);
                if (!keySequenceType.isSubtypeOf(SequenceType.createSequenceType("string"))
                        && !keySequenceType.isSubtypeOf(SequenceType.createSequenceType("anyURI"))) {
                    this.errors.report(
                            "The inferred static sequence types for the keys of an Object must be a subtype of string or anyURI, instead found a: "
                                    + keySequenceType,
                            expression.getMetadata());
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
                            .map(value -> ItemTypeFactory.createObjectFieldType(typeOf(value, expression)))
                            .collect(Collectors.toList()))));
        } else {
            expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.objectItem));
        }
        return argument;
    }

    @Override
    public StaticContext visitMapConstructor(MapConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.mapItem), argument);
    }

    @Override
    public StaticContext visitDirElemConstructor(DirElemConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        // Translation has already resolved the element's expanded QName, including local namespaces.
        expression.setStaticSequenceType(
                new SequenceType(ItemTypeFactory.elementNodeItemType(expression.getNodeName())));
        return argument;
    }

    @Override
    public StaticContext visitDirPIConstructor(DirPIConstructorExpression expression, StaticContext argument) {
        return withType(expression, new SequenceType(BuiltinTypesCatalogue.processingInstructionNode), argument);
    }

    @Override
    public StaticContext visitComputedElementConstructor(
            ComputedElementConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        ItemType elementType = expression.hasStaticName()
                ? ItemTypeFactory.elementNodeItemType(expression.getElementName())
                : BuiltinTypesCatalogue.elementNode;
        expression.setStaticSequenceType(new SequenceType(elementType));
        return argument;
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
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.documentNode));
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
        SequenceType returnType = expression.getActualReturnType();
        if (returnType == null) {
            returnType = typeOf(expression.getBody().getExpression(), expression);
        }
        List<SequenceType> params = new ArrayList<>(expression.getParams().values());
        FunctionSignature signature = new FunctionSignature(params, returnType, expression.isUpdating());
        expression.setStaticSequenceType(new SequenceType(ItemTypeFactory.createFunctionItemType(signature)));
        return argument;
    }

    private FunctionSignature getSignature(FunctionIdentifier identifier, StaticContext staticContext) {
        BuiltinFunction function = null;
        FunctionSignature signature = null;
        function = BuiltinFunctionCatalogue.getBuiltinFunction(identifier, staticContext);
        if (function != null) {
            signature = function.getSignature();
        } else {
            signature = staticContext.getFunctionSignature(identifier);
        }
        return signature;
    }

    @Override
    public StaticContext visitNamedFunctionRef(NamedFunctionReferenceExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        try {
            FunctionSignature signature = getSignature(expression.getIdentifier(), expression.getStaticContext());
            expression.setStaticSequenceType(new SequenceType(ItemTypeFactory.createFunctionItemType(signature)));
        } catch (UnknownFunctionCallException e) {
            throw new UnknownFunctionCallException(
                    expression.getIdentifier().getName(),
                    expression.getIdentifier().getArity(),
                    expression.getMetadata());
        }
        return argument;
    }

    @Override
    public StaticContext visitFunctionCall(FunctionCallExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        if (BuiltinFunctionCatalogue.exists(expression.getFunctionIdentifier(), expression.getStaticContext())) {
            if (expression.isPartialApplication()) {
                // This should never be reached because partial application on built-in functions should have been
                // rewritten before
                throw new UnsupportedFeatureException(
                        "Partial application on built-in functions are not supported.", expression.getMetadata());
            }
            BuiltinFunction builtinFunction = BuiltinFunctionCatalogue.getBuiltinFunction(
                    expression.getFunctionIdentifier(), expression.getStaticContext());
            if (builtinFunction == null) {
                throw new UnknownFunctionCallException(
                        expression.getFunctionIdentifier().getName(),
                        expression.getFunctionIdentifier().getArity(),
                        expression.getMetadata());
            }
        }
        // The zero-argument form has an implicit node argument. Check the focus type
        // just as the one-argument form checks its explicit argument under static typing.
        Name functionName = expression.getFunctionIdentifier().getName();
        if ((Name.FN_NS.equals(functionName.getNamespace())
                        || Name.JSONIQ_DEFAULT_FUNCTION_NS.equals(functionName.getNamespace()))
                && functionName.getLocalName().equals("has-children")
                && expression.getArguments().isEmpty()) {
            SequenceType contextType = expression.getStaticContext().getContextItemStaticType();
            if (contextType != null && !contextType.getItemType().isSubtypeOf(BuiltinTypesCatalogue.nodeItem)) {
                this.errors.report(
                        "fn:has-children() requires a node context item, but " + contextType + " was inferred.",
                        expression.getMetadata());
            }
        }
        FunctionSignature signature = null;
        try {
            signature = getSignature(expression.getFunctionIdentifier(), expression.getStaticContext());
        } catch (UnknownFunctionCallException e) {
            throw new UnknownFunctionCallException(expression.getFunctionIdentifier(), expression.getMetadata());
        }
        List<Expression> parameterExpressions = expression.getArguments();
        List<SequenceType> parameterTypes = signature.getParameterTypes();
        List<SequenceType> partialParams = new ArrayList<>();
        int paramsLength = parameterExpressions.size();

        // check arguments are of correct type
        for (int i = 0; i < paramsLength; ++i) {
            if (parameterExpressions.get(i) != null) {
                SequenceType actualType = typeOf(parameterExpressions.get(i), expression);
                SequenceType expectedType = parameterTypes.get(i);
                if (!isFunctionArgumentCompatible(actualType, expectedType)) {
                    this.errors.report(
                            "Argument " + i + " requires " + expectedType + " but " + actualType + " was found",
                            expression.getMetadata());
                }
            } else {
                partialParams.add(parameterTypes.get(i));
            }
        }

        if (expression.isPartialApplication()) {
            FunctionSignature partialSignature =
                    new FunctionSignature(partialParams, signature.getReturnType(), expression.isUpdating());
            expression.setStaticSequenceType(
                    new SequenceType(ItemTypeFactory.createFunctionItemType(partialSignature)));
        } else {
            Optional<SequenceType> specificType = this.builtinFunctions.inferSpecificReturnType(expression, argument);
            if (specificType.isPresent()) {
                expression.setStaticSequenceType(specificType.get());
            } else {
                // we did not annotate a specific function, therefore we use default return type
                SequenceType returnType = signature.getReturnType();
                if (returnType == null) {
                    returnType = SequenceType.createSequenceType("item*");
                }
                if (BuiltinFunctionCatalogue.exists(expression.getFunctionIdentifier(), expression.getStaticContext())
                        && parameterExpressions.size() == 1) {
                    BuiltinFunction builtinFunction = BuiltinFunctionCatalogue.getBuiltinFunction(
                            expression.getFunctionIdentifier(), expression.getStaticContext());
                    if (builtinFunction != null
                            && builtinFunction.getFunctionIteratorClass().equals(ConstructorFunctionIterator.class)) {
                        SequenceType argumentType = typeOf(parameterExpressions.get(0), expression);
                        if (TypeAtomization.inferType(argumentType).getCardinality() == SequenceCardinality.ONE
                                && returnType.getArity().equals(SequenceType.Arity.OneOrZero)) {
                            returnType = new SequenceType(returnType.getItemType(), SequenceType.Arity.One);
                        }
                    }
                }
                expression.setStaticSequenceType(returnType);
            }
        }

        return argument;
    }

    private boolean isFunctionArgumentCompatible(SequenceType actual, SequenceType expected) {
        if (actual.isEmptySequence()) {
            return actual.isSubtypeOfOrCanBePromotedTo(expected);
        }
        ItemType itemType = actual.getItemType();
        if (itemType.isUnionType()) {
            return itemType.allMemberTypesMatch(member ->
                    isFunctionArgumentCompatible(new SequenceType(member, actual.getCardinality()), expected));
        }
        if (expected.getItemType().isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            if (TypeAtomization.hasUnknownTypedValue(itemType)) {
                // Runtime function conversion checks the type and size of the typed value, just as for casts.
                return true;
            }
            if (TypeAtomization.containsNode(itemType)) {
                // Function conversion atomizes the node, so its known typed value must match.
                return isFunctionArgumentCompatible(TypeAtomization.inferType(actual), expected);
            }
            if (itemType.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return actual.getCardinality().isSubtypeOf(expected.getCardinality());
            }
        }
        return actual.isSubtypeOfOrCanBePromotedTo(expected);
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

        SequenceType inferred = typeOf(expression.getMainExpression(), expression);
        SequenceType expected = expression.getSequenceType();
        // Equality compares declared occurrence indicators, which is all an assertion's type can express.
        if (!inferred.equals(expected)) {
            throw new IsStaticallyUnexpectedTypeException(
                    "expected static type is " + expected + " instead " + inferred + " was inferred",
                    expression.getMetadata());
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
                this.errors.report(
                        "The inferred type "
                                + expressionSequenceType
                                + " does not match the declared type, which requires "
                                + treatedSequenceType,
                        expression.getMetadata());
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
        if (!isFunctionArgumentCompatible(argumentType, parameterType)) {
            this.errors.report(
                    (expression.isReturnValue() ? "Return value requires " : "Argument requires ")
                            + parameterType
                            + " but "
                            + argumentType
                            + " was found",
                    expression.getMetadata());
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
        expression.setStaticSequenceType(typeOf(expression.getReturnExpression(), expression));

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
        expression.setStaticSequenceType(
                this.arithmetic.inferUnaryType(typeOf(expression.getMainExpression(), expression), expression));
        return argument;
    }

    // endregion

    // region logic

    private StaticContext visitAndOrExpr(Expression expression, StaticContext argument, String expressionName) {
        visitDescendants(expression, argument);

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = typeOf((Expression) childrenExpressions.get(0), expression);
        SequenceType rightInferredType = typeOf((Expression) childrenExpressions.get(1), expression);

        if (!leftInferredType.hasEffectiveBooleanValue()) {
            this.errors.report(
                    "left expression of a "
                            + expressionName
                            + "Expression has "
                            + leftInferredType
                            + " inferred type, which has no effective boolean value",
                    expression.getMetadata());
        }

        if (!rightInferredType.hasEffectiveBooleanValue()) {
            this.errors.report(
                    "right expression of a "
                            + expressionName
                            + "Expression has "
                            + rightInferredType
                            + " inferred type, which has no effective boolean value",
                    expression.getMetadata());
        }

        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitAndExpr(AndExpression expression, StaticContext argument) {
        return visitAndOrExpr(expression, argument, "And");
    }

    @Override
    public StaticContext visitOrExpr(OrExpression expression, StaticContext argument) {
        return visitAndOrExpr(expression, argument, "Or");
    }

    @Override
    public StaticContext visitNotExpr(NotExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType childInferredType = typeOf(expression.getMainExpression(), expression);
        if (!childInferredType.hasEffectiveBooleanValue()) {
            this.errors.report(
                    "The child expression of NotExpression has "
                            + childInferredType
                            + " inferred type, which has no effective boolean value",
                    expression.getMetadata());
        }

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
                expression));
        return argument;
    }

    @Override
    public StaticContext visitNodeSetExpr(NodeSetExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType leftType = typeOf(expression.getLeftExpression(), expression);
        SequenceType rightType = typeOf(expression.getRightExpression(), expression);
        if (!leftType.isEmptySequence() && !leftType.getItemType().isNodeItemType()) {
            this.errors.report(
                    "Left operand of a node set expression must be a sequence of nodes, got " + leftType,
                    expression.getMetadata());
        }
        if (!rightType.isEmptySequence() && !rightType.getItemType().isNodeItemType()) {
            this.errors.report(
                    "Right operand of a node set expression must be a sequence of nodes, got " + rightType,
                    expression.getMetadata());
        }

        expression.setStaticSequenceType(
                new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceType.Arity.ZeroOrMore));
        return argument;
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
        if (!ifType.hasEffectiveBooleanValue()) {
            this.errors.report(
                    "The condition in the 'if' must have effective boolean value, found inferred type: "
                            + ifType
                            + " (which has not effective boolean value)",
                    node.getMetadata());
        }

        // if the if branch is false at static time (i.e. subtype of null?) we only use else branch
        SequenceType resultingType = ifType.isSubtypeOf(SequenceType.createSequenceType("null?"))
                ? elseType
                : thenType.leastCommonSupertypeWith(elseType);

        this.errors.rejectInferredEmpty(resultingType, node);
        return resultingType;
    }

    // throw errors if [type] does not conform to switch test and cases requirements
    private void checkSwitchType(SequenceType type, Node switchNode) {
        ExceptionMetadata metadata = switchNode.getMetadata();
        if (type.isEmptySequence()) {
            return; // no further check is required
        }
        if (type.getArity() == SequenceType.Arity.OneOrMore || type.getArity() == SequenceType.Arity.ZeroOrMore) {
            this.errors.report(
                    "+ and * arities are not allowed for the expressions of switch test condition and cases", metadata);
        }
        ItemType itemType = type.getItemType();
        if (itemType.isFunctionItemType()) {
            this.errors.report(
                    "function item not allowed for the expressions of switch test condition and cases",
                    ErrorCode.UnexpectedFunctionItem,
                    metadata);
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.JSONItem)) {
            this.errors.report(
                    "switch test condition and cases expressions' item type must match atomic, instead inferred: "
                            + itemType,
                    ErrorCode.NonAtomicElementErrorCode,
                    metadata);
        }
        if (!itemType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            this.errors.report(
                    "switch test condition and cases expressions' item type must match atomic, instead inferred: "
                            + itemType,
                    metadata);
        }
    }

    // check the case conditions and tell whether any of them may match the test condition
    private boolean switchCaseMayMatch(List<Expression> conditions, SequenceType testType, Node switchNode) {
        boolean mayMatch = false;
        for (Expression condition : conditions) {
            SequenceType conditionType = typeOf(condition, switchNode);
            checkSwitchType(conditionType, switchNode);
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
        this.errors.rejectInferredEmpty(inferredType, node);
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
                    returnExpression.getStaticContext());
            visit(returnExpression, argument);
            branchTypes.add(typeOf(returnExpression, expression));
        }

        Expression defaultExpression = expression.getDefaultCase().getReturnExpression();
        bindCaseVariable(
                expression.getDefaultCase().getVariableName(), conditionType, defaultExpression.getStaticContext());
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

        if (leftType.isEmptySequence() || rightType.isEmptySequence()) {
            this.errors.reportInferredEmpty(expression);
        }

        SequenceType intOpt = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.OneOrZero);
        if (!leftType.isSubtypeOf(intOpt) || !rightType.isSubtypeOf(intOpt)) {
            this.errors.report(
                    "operands of the range expression must match type integer? instead found: "
                            + leftType
                            + " and "
                            + rightType,
                    expression.getMetadata());
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

        if ((!leftType.isEmptySequence() && !TypeAtomization.isAtomicOrNode(leftType.getItemType()))
                || (!rightType.isEmptySequence() && !TypeAtomization.isAtomicOrNode(rightType.getItemType()))
                || AtomizedOperands.resolveArities(leftType, rightType) == null) {
            this.errors.report(
                    "operands of the concat expression must match type atomic? instead found: "
                            + leftType
                            + " and "
                            + rightType,
                    expression.getMetadata());
        }

        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.stringItem));
        return argument;
    }

    // endregion

    // region postfix

    @Override
    public StaticContext visitArrayLookupExpression(ArrayLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(this.lookups.inferArrayLookupType(
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
        expression.setStaticSequenceType(this.lookups.inferObjectLookupType(
                typeOf(expression.getMainExpression(), expression),
                typeOf(expression.getLookupExpression(), expression),
                key,
                expression));
        return argument;
    }

    @Override
    public StaticContext visitPostfixLookupExpression(PostfixLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(
                this.lookups.inferPostfixLookupType(typeOf(expression.getMainExpression(), expression), expression));
        return argument;
    }

    @Override
    public StaticContext visitUnaryLookupExpression(UnaryLookupExpression expression, StaticContext argument) {
        return withType(expression, SequenceType.createSequenceType("item*"), argument);
    }

    @Override
    public StaticContext visitArrayUnboxingExpression(ArrayUnboxingExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(
                this.lookups.inferArrayUnboxingType(typeOf(expression.getMainExpression(), expression), expression));
        return argument;
    }

    @Override
    public StaticContext visitFilterExpression(FilterExpression expression, StaticContext argument) {
        visit(expression.getMainExpression(), argument);
        SequenceType mainType = typeOf(expression.getMainExpression(), expression);
        this.errors.rejectInferredEmpty(mainType, expression);

        Expression predicateExpression = expression.getPredicateExpression();
        // set context item static type
        predicateExpression.getStaticContext().setContextItemStaticType(new SequenceType(mainType.getItemType()));
        visit(predicateExpression, argument);
        // unset context item static type
        predicateExpression.getStaticContext().setContextItemStaticType(null);
        SequenceType predicateType = typeOf(predicateExpression, expression);
        this.errors.rejectInferredEmpty(predicateType, expression);
        // always false so the return type is for sure ()
        if (predicateType.isSubtypeOf(SequenceType.createSequenceType("null?"))) {
            this.errors.reportInferredEmpty(
                    "Inferred type for FilterExpression is empty sequence (with active static typing feature, only allowed for CommaExpression)",
                    expression);
        }
        if (!predicateType.hasEffectiveBooleanValue()) {
            this.errors.report(
                    "Inferred type " + predicateType + " in FilterExpression has no effective boolean value",
                    expression.getMetadata());
        }

        // if we are filter one or less items or we use an integer to select a specific position we return at most one
        // element, otherwise *
        SequenceType.Arity inferredArity = (mainType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)
                        || predicateType.getItemType().equals(BuiltinTypesCatalogue.integerItem))
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.ZeroOrMore;
        expression.setStaticSequenceType(new SequenceType(mainType.getItemType(), inferredArity));
        return argument;
    }

    // return [true] if [types] are subtype of or can be promoted to expected types, [false] otherwise
    private boolean checkArguments(List<SequenceType> expectedTypes, List<SequenceType> types) {
        int length = expectedTypes.size();
        if (length != types.size()) {
            return false;
        }
        for (int i = 0; i < length; ++i) {
            if (!isFunctionArgumentCompatible(types.get(i), expectedTypes.get(i))) {
                return false;
            }
        }
        return true;
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

        expression.setStaticSequenceType(inferDynamicFunctionCallType(expression, mainType.getItemType()));
        return argument;
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
        if (type.isArrayItemType() || type.isMapItemType()) {
            return SequenceType.createSequenceType("item*");
        }
        if (!type.isFunctionItemType()) {
            this.errors.report(
                    "the type of a dynamic function call main expression must be function, array, or map, instead inferred "
                            + type,
                    expression.getMetadata());
            return SequenceType.createSequenceType("item*");
        }

        if (type.equals(BuiltinTypesCatalogue.anyFunctionItem)) {
            return SequenceType.createSequenceType("item*");
        }

        FunctionSignature signature = type.getSignature();
        List<SequenceType> actualParameterTypes = new ArrayList<>();
        List<SequenceType> formalParameterTypes = signature.getParameterTypes();
        if (formalParameterTypes.size() != expression.getArguments().size()) {
            this.errors.report(
                    "The arguments of a dynamic function call have the wrong arity.", expression.getMetadata());
            // With optional static typing disabled, an unreachable branch may still have
            // a mismatched signature. Leave enforcement to the runtime call if it is evaluated.
            return SequenceType.createSequenceType("item*");
        }
        List<SequenceType> partialFormalParameterTypes = new ArrayList<>();
        boolean isPartialApplication = false;
        int i = 0;
        for (Expression e : expression.getArguments()) {
            if (e == null) {
                isPartialApplication = true;
                partialFormalParameterTypes.add(formalParameterTypes.get(i));
            }
            if (e != null) {
                actualParameterTypes.add(typeOf(e, expression));
            }
            ++i;
        }
        if (isPartialApplication) {
            FunctionSignature newSignature = new FunctionSignature(
                    partialFormalParameterTypes, signature.getReturnType(), expression.isUpdating());
            return new SequenceType(ItemTypeFactory.createFunctionItemType(newSignature));
        }
        if (!checkArguments(formalParameterTypes, actualParameterTypes)) {
            this.errors.report(
                    "the arguments of a dynamic function call do not match the signature of " + type,
                    expression.getMetadata());
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
        this.errors.rejectInferredEmpty(leftType, expression);

        // set context item static type
        rightExpression.getStaticContext().setContextItemStaticType(new SequenceType(leftType.getItemType()));
        visit(rightExpression, argument);
        rightExpression.getStaticContext().setContextItemStaticType(null);

        SequenceType rightType = typeOf(rightExpression, expression);
        this.errors.rejectInferredEmpty(rightType, expression);

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
                SequenceType forType = typeOf(((ForClause) clause).getExpression(), clause);
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
        this.errors.rejectInferredEmpty(returnType, expression);
        returnType = new SequenceType(
                returnType.getItemType(), returnType.getCardinality().repeated(forCardinality));
        expression.setStaticSequenceType(returnType);
        return argument;
    }

    @Override
    public StaticContext visitForClause(ForClause expression, StaticContext argument) {
        expression.getExpression().accept(this, argument);
        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType =
                declaredType == null ? typeOf(expression.getExpression(), expression) : declaredType;
        if (inferredType.isEmptySequence()) {
            if (!expression.isAllowEmpty()) {
                // for sure we will not have any tuple to process and return the empty sequence
                this.errors.reportInferredEmpty(
                        "In for clause Inferred type is empty sequence, empty is not allowed, so the result returned is for sure () and this is not a CommaExpression",
                        expression);
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
        if (!conditionType.hasEffectiveBooleanValue()) {
            this.errors.report("Window condition has no effective boolean value", clause.getMetadata());
        }
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
        if (!whereType.hasEffectiveBooleanValue()) {
            this.errors.report(
                    "where clause inferred type (" + whereType + ") has no effective boolean value",
                    expression.getMetadata());
        }
        if (whereType.isEmptySequence() || whereType.isSubtypeOf(SequenceType.createSequenceType("null?"))) {
            this.errors.reportInferredEmpty(
                    "where clause always return false, so return expression inferred type is empty sequence and this is not a CommaExpression",
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
        String message = "group by variable " + variableName + " must match atomic? instead found ";
        if (keyType.isSubtypeOf(SequenceType.createSequenceType("json-item*"))) {
            this.errors.report(message + keyType, ErrorCode.NonAtomicElementErrorCode, clause.getMetadata());
        }
        SequenceType atomizedKeyType = atomizedKeyType(keyType);
        if (atomizedKeyType == null) {
            this.errors.report(
                    message
                            + AtomizedOperands.describeAtomizedType(
                                    keyType, AtomizedOperands.knownAtomizedType(keyType)),
                    clause.getMetadata());
            // without static typing, the runtime atomizes the key or fails
            atomizedKeyType = SequenceType.createSequenceType("anyAtomicType?");
        }
        SequenceType declaredType = groupByVar.getActualSequenceType();
        if (declaredType == null) {
            return atomizedKeyType;
        }
        if (!declaredType.isSubtypeOf(SequenceType.createSequenceType("anyAtomicType?"))) {
            this.errors.report(message + declaredType, clause.getMetadata());
        }
        // a typed value only known at runtime is checked against the declared type then
        if (!TypeAtomization.hasUnknownTypedValue(keyType.getItemType())
                && !atomizedKeyType.isSubtypeOf(declaredType)) {
            this.errors.report(
                    "group by variable "
                            + variableName
                            + " is bound to "
                            + AtomizedOperands.describeAtomizedType(keyType, atomizedKeyType)
                            + ", which does not match its declared type "
                            + declaredType,
                    clause.getMetadata());
        }
        return declaredType;
    }

    @Override
    public StaticContext visitOrderByClause(OrderByClause expression, StaticContext argument) {
        String message =
                "order by sorting expression's type must match atomic? and be comparable using 'gt' operator, instead inferred: ";
        for (OrderByClauseSortingKey orderClause : expression.getSortingKeys()) {
            visit(orderClause.getExpression(), argument);
            SequenceType keyType = typeOf(orderClause.getExpression(), expression);
            if (keyType.isSubtypeOf(SequenceType.createSequenceType("json-item*"))) {
                this.errors.report(message + keyType, ErrorCode.NonAtomicElementErrorCode, expression.getMetadata());
            }
            SequenceType atomizedKeyType = atomizedKeyType(keyType);
            // every key is compared with the others, like the input of fn:min and fn:max
            boolean sortable = atomizedKeyType != null
                    && (TypeAtomization.hasUnknownTypedValue(keyType.getItemType())
                            || ComparisonTyping.hasMutuallyComparableItems(atomizedKeyType.getItemType()));
            if (!sortable) {
                this.errors.report(
                        message
                                + AtomizedOperands.describeAtomizedType(
                                        keyType, AtomizedOperands.knownAtomizedType(keyType)),
                        expression.getMetadata());
            }
        }
        return argument;
    }

    /**
     * Returns the type of the at most one atomic value a group-by or order-by key atomizes to, or null when it may be
     * more. As for singleton operators, a typed value only known at runtime is checked then.
     */
    private SequenceType atomizedKeyType(SequenceType keyType) {
        if (keyType.isEmptySequence()) {
            return keyType;
        }
        SequenceType.Arity arity = AtomizedOperands.atomizedSingletonArity(keyType);
        if (arity == null || !TypeAtomization.isAtomicOrNode(keyType.getItemType())) {
            return null;
        }
        return new SequenceType(TypeAtomization.atomizedItemType(keyType.getItemType()), arity);
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

    /** An assignable variable without a declared type can later be assigned any value. */
    private static SequenceType inferUndeclaredVariableType(Expression initializer, boolean assignable, Node node) {
        return initializer == null || assignable ? SequenceType.createSequenceType("item*") : typeOf(initializer, node);
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
        return argument;
    }

    @Override
    public StaticContext visitFunctionDeclaration(FunctionDeclaration expression, StaticContext argument) {
        visitDescendants(expression, argument);
        InlineFunctionExpression inlineExpression = (InlineFunctionExpression) expression.getExpression();
        SequenceType inferredType = typeOf(inlineExpression.getBody().getExpression(), expression);
        SequenceType expectedType = inlineExpression.getActualReturnType();

        if (expectedType == null) {
            expectedType = inferredType;
        } else if (!inferredType.isSubtypeOfOrCanBePromotedTo(expectedType)) {
            this.errors.report(
                    "The declared function return inferred type "
                            + inferredType
                            + " does not match or can be promoted to the expected return type "
                            + expectedType,
                    expression.getMetadata());
        }

        return argument;
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
            visit(child, argument);
        }
        return argument;
    }

    @Override
    public StaticContext visitValidateTypeExpression(ValidateTypeExpression expression, StaticContext argument) {
        visitDescendants(expression, expression.getStaticContext());
        SequenceType sourceType = typeOf(expression.getMainExpression(), expression);
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
                throw new SemanticException(
                        "The type " + typeName + " is not defined in the in-scope schema types.",
                        ErrorCode.ValidateTypeNotFoundErrorCode,
                        expression.getMetadata());
            }
            if (builtInType) {
                ItemType targetType = BuiltinTypesCatalogue.getItemTypeByName(typeName);
                if (!targetType.isAtomicItemType()
                        || targetType.equals(BuiltinTypesCatalogue.atomicItem)
                        || targetType.equals(BuiltinTypesCatalogue.NOTATIONItem)) {
                    throw new UnsupportedFeatureException(
                            "Validate type currently supports concrete built-in XML Schema atomic types.",
                            expression.getMetadata());
                }
            }
        }
        // Preserve a statically known element or document subtype; otherwise use node(), since invalid operand kinds
        // are reported dynamically as XQTY0030.
        ItemType sourceItemType =
                typeOf(expression.getMainExpression(), expression).getItemType();
        ItemType resultItemType = sourceItemType.isSubtypeOf(BuiltinTypesCatalogue.elementNode)
                        || sourceItemType.isSubtypeOf(BuiltinTypesCatalogue.documentNode)
                ? sourceItemType
                : BuiltinTypesCatalogue.nodeItem;
        resultItemType = inferValidatedType(expression, sourceItemType).orElse(resultItemType);

        // Successful validation always returns exactly one copied node.
        expression.setStaticSequenceType(new SequenceType(resultItemType, SequenceType.Arity.One));
        return argument;
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
        statement.setStaticSequenceType(typeOf(statement.getApplyExpression(), statement));
        return argument;
    }

    @Override
    public StaticContext visitAssignStatement(AssignStatement statement, StaticContext argument) {
        visit(statement.getAssignExpression(), argument);
        SequenceType variableDeclaredType = statement.getStaticContext().getVariableSequenceType(statement.getName());
        SequenceType expressionType = typeOf(statement.getAssignExpression(), statement);
        if (!expressionType.isSubtypeOf(variableDeclaredType)) {
            this.errors.report(
                    "Declared type: "
                            + variableDeclaredType
                            + " of variable: $"
                            + statement.getName()
                            + " is not a supertype of assigned expression type: "
                            + expressionType,
                    statement.getMetadata());
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
        statement.setStaticSequenceType(typeOf(statement.getExitExpression(), statement));
        return argument;
    }

    @Override
    public StaticContext visitWhileStatement(WhileStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        statement.setStaticSequenceType(typeOf(statement.getStatement(), statement));
        return argument;
    }

    @Override
    public StaticContext visitFlowrStatement(FlowrStatement statement, StaticContext argument) {
        visitFlworClauses(statement.getReturnStatementClause().getFirstClause());
        SequenceType returnType = typeOf(statement.getReturnStatementClause().getReturnStatement(), statement);
        this.errors.rejectInferredEmpty(returnType, statement);
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
                    returnStatement.getStaticContext());
            visit(returnStatement, argument);
            branchTypes.add(typeOf(returnStatement, statement));
        }

        Statement defaultStatement = statement.getDefaultCase().getReturnStatement();
        bindCaseVariable(
                statement.getDefaultCase().getVariableName(), conditionType, defaultStatement.getStaticContext());
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
        expression.setStaticSequenceType(StaticTypeErrors.requireInferredType(
                expression.getStatementsAndExpr().getStaticSequenceType(), expression));
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
