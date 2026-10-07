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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import lombok.extern.log4j.Log4j2;

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
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
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
import org.rumbledb.expressions.xml.axis.ForwardAxis;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;
import org.rumbledb.expressions.xml.axis.ReverseAxis;
import org.rumbledb.expressions.xml.axis.ReverseStepExpr;
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
import org.rumbledb.runtime.functions.ConstructorFunctionIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.AttributeNodeItemType;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.DocumentNodeItemType;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.FieldDescriptor;
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

    /**
     * Builds a new visitor.
     *
     * @param rumbleRuntimeConfiguration the configuration.
     */
    InferTypeVisitor(RumbleConfiguration rumbleRuntimeConfiguration) {
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
    }

    private void throwStaticTypeException(String message, ErrorCode code) {
        if (this.rumbleRuntimeConfiguration.analysis().enableStaticTyping()) {
            throw new UnexpectedStaticTypeException(message, code);
        }
    }

    private void throwStaticTypeException(String message, ExceptionMetadata metadata) {
        if (this.rumbleRuntimeConfiguration.analysis().enableStaticTyping()) {
            throw new UnexpectedStaticTypeException(message, metadata);
        }
    }

    private void throwStaticTypeException(String message, ErrorCode code, ExceptionMetadata metadata) {
        if (this.rumbleRuntimeConfiguration.analysis().enableStaticTyping()) {
            throw new UnexpectedStaticTypeException(message, code, metadata);
        }
    }

    private SequenceType requireInferredType(SequenceType type, String nodeName) {
        if (type == null) {
            throw new OurBadException("A child expression of a " + nodeName + " has no inferred type");
        }
        return type;
    }

    /**
     * Perform basic checks on a list of SequenceType, available checks are for null (OurBad exception) and inferred the
     * empty sequence (XPST0005)
     *
     * @param types list of sequence types to check
     * @param nodeName name of the node to use in the errors
     * @param nullCheck flag indicating to perform null check
     * @param inferredEmptyCheck flag indicating to perform empty sequence check
     */
    private void basicChecks(
            List<SequenceType> types,
            String nodeName,
            boolean nullCheck,
            boolean inferredEmptyCheck,
            ExceptionMetadata metadata) {
        if (nullCheck) {
            for (SequenceType type : types) {
                if (type == null) {
                    throw new OurBadException("A child expression of a " + nodeName + " has no inferred type");
                }
            }
        }
        if (inferredEmptyCheck) {
            for (SequenceType type : types) {
                if (type.isEmptySequence()) {
                    throwStaticTypeException(
                            "Inferred type for "
                                    + nodeName
                                    + " is empty sequence (with active static typing feature, only allowed for CommaExpression)",
                            ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
                }
            }
        }
    }

    /**
     * Perform basic checks on a SequenceType, available checks are for null (OurBad exception) and inferred the empty
     * sequence (XPST0005)
     *
     * @param type sequence types to check
     * @param nodeName name of the node to use in the errors
     * @param nullCheck flag indicating to perform null check
     * @param inferredEmptyCheck flag indicating to perform empty sequence check
     */
    private void basicChecks(
            SequenceType type,
            String nodeName,
            boolean nullCheck,
            boolean inferredEmptyCheck,
            ExceptionMetadata metadata) {
        if (nullCheck) {
            if (type == null) {
                throw new OurBadException("A child expression of a " + nodeName + " has no inferred type");
            }
        }
        if (inferredEmptyCheck) {
            if (type != null && type.isEmptySequence()) {
                throwStaticTypeException(
                        "Inferred type for "
                                + nodeName
                                + " is empty sequence (with active static typing feature, only allowed for CommaExpression)",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
            }
        }
    }

    @Override
    public StaticContext visitCommaExpression(CommaExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType inferredType = SequenceType.createSequenceType("()");

        for (Expression childExpression : expression.getExpressions()) {
            SequenceType childExpressionInferredType =
                    requireInferredType(childExpression.getStaticSequenceType(), "CommaExpression");

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
        basicChecks(variableType, expression.getClass().getSimpleName(), false, true, expression.getMetadata());
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
        ItemType contentItemType = contentExpr.getStaticSequenceType().getItemType();
        ItemType arrayType = ItemTypeFactory.createAnonymousArrayType(contentItemType);
        expression.setStaticSequenceType(new SequenceType(arrayType));
        return argument;
    }

    @Override
    public StaticContext visitStringConstructor(StringConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.stringItem));
        return argument;
    }

    @Override
    public StaticContext visitObjectConstructor(ObjectConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        if (expression.isMergedConstructor()) {
            // if it is a merged constructor the child must be a subtype of object* inferred type
            SequenceType childSequenceType = requireInferredType(
                    ((Expression) expression.getChildren().get(0)).getStaticSequenceType(),
                    expression.getClass().getSimpleName());
            if (!childSequenceType.isSubtypeOf(SequenceType.createSequenceType("object*"))) {
                throwStaticTypeException(
                        "The child expression must have object* sequence type, instead found: " + childSequenceType,
                        expression.getMetadata());
            }
        } else {
            for (Expression keyExpression : expression.getKeys()) {
                SequenceType keySequenceType = requireInferredType(
                        keyExpression.getStaticSequenceType(),
                        expression.getClass().getSimpleName());
                if (!keySequenceType.isSubtypeOf(SequenceType.createSequenceType("string"))
                        && !keySequenceType.isSubtypeOf(SequenceType.createSequenceType("anyURI"))) {
                    throwStaticTypeException(
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
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.mapItem));
        return argument;
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
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.processingInstructionNode));
        return argument;
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
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.processingInstructionNode));
        return argument;
    }

    @Override
    public StaticContext visitComputedAttributeConstructor(
            ComputedAttributeConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.attributeNode));
        return argument;
    }

    @Override
    public StaticContext visitComputedNamespaceConstructor(
            ComputedNamespaceConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.namespaceNode));
        return argument;
    }

    @Override
    public StaticContext visitDocumentNodeConstructor(
            DocumentNodeConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.documentNode));
        return argument;
    }

    @Override
    public StaticContext visitPathRootExpr(PathRootExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.documentNode));
        return argument;
    }

    @Override
    public StaticContext visitCommentNodeConstructor(
            CommentNodeConstructorExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.commentNode));
        return argument;
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
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.textNode));
        return argument;
    }

    @Override
    public StaticContext visitTextNode(TextNodeExpression expression, StaticContext argument) {
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.textNode));
        return argument;
    }

    @Override
    public StaticContext visitAttributeNode(AttributeNodeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.attributeNode));
        return argument;
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
            returnType = expression.getBody().getExpression().getStaticSequenceType();
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

    private SequenceType validateStrictAggregateInputType(
            FunctionCallExpression expression, Expression inputExpression, String functionName) {
        SequenceType inputType = requireInferredType(
                inputExpression.getStaticSequenceType(), expression.getClass().getSimpleName());
        if (inputType.isEmptySequence()) {
            return inputType;
        }

        ItemType inputItemType = normalizeAggregateItemType(inputType.getItemType());
        if (!inputItemType.isSubtypeOf(BuiltinTypesCatalogue.numericItem)
                && !inputItemType.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                && !inputItemType.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem)
                && !(TypeAtomization.containsNode(inputType.getItemType())
                        && isStrictAggregateOperandType(inputType.getItemType()))) {
            throwStaticTypeException(
                    functionName
                            + " requires its inferred input sequence type to be empty or have an item type that is a subtype of xs:numeric, xs:yearMonthDuration, or xs:dayTimeDuration, found "
                            + inputType,
                    ErrorCode.InvalidArgumentType,
                    expression.getMetadata());
        }

        return new SequenceType(
                inputItemType, TypeAtomization.inferType(inputType).getCardinality());
    }

    private boolean isStrictAggregateOperandType(ItemType type) {
        return type.allMemberTypesMatch(member -> {
            if (TypeAtomization.hasUnknownTypedValue(member)) {
                return true;
            }
            ItemType normalized = normalizeAggregateItemType(member);
            return normalized.isNumeric()
                    || normalized.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                    || normalized.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem);
        });
    }

    // Aggregates can convert untyped values and produce values outside a derived type's restrictions.
    private ItemType normalizeAggregateItemType(ItemType type) {
        if (type.isUnionType()) {
            return ItemTypeFactory.createInferredUnionType(type.getMemberTypes().stream()
                    .map(this::normalizeAggregateItemType)
                    .collect(Collectors.toList()));
        }
        if (TypeAtomization.containsNode(type)) {
            type = TypeAtomization.atomizedItemType(type);
        }
        if (type.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)) {
            return BuiltinTypesCatalogue.doubleItem;
        }
        if (type.isNumeric()
                || type.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem)) {
            return type.getCastingPrimitiveType();
        }
        if (type.isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
            return BuiltinTypesCatalogue.stringItem;
        }
        return type;
    }

    private SequenceType inferSumReturnType(FunctionCallExpression expression) {
        SequenceType inputType = validateStrictAggregateInputType(
                expression, expression.getArguments().get(0), "fn:sum");
        SequenceType zeroType = expression.getArguments().size() > 1
                ? expression.getArguments().get(1).getStaticSequenceType()
                : new SequenceType(BuiltinTypesCatalogue.integerItem);
        if (!zeroType.isEmptySequence() && TypeAtomization.containsNode(zeroType.getItemType())) {
            zeroType = TypeAtomization.inferType(zeroType);
            // The zero parameter is atomic?: successful function conversion has
            // already excluded multiple typed values before sum is evaluated.
            zeroType = new SequenceType(
                    zeroType.getItemType(),
                    zeroType.getCardinality().allowsZero() ? SequenceCardinality.ZERO_OR_ONE : SequenceCardinality.ONE);
        }
        if (inputType.isEmptySequence()) {
            return zeroType;
        }
        // A non-empty input sums to one item; an empty input returns the zero argument instead.
        SequenceType sumType = new SequenceType(inputType.getItemType());
        if (!inputType.getCardinality().allowsZero()) {
            return sumType;
        }
        return sumType.leastCommonSupertypeWith(zeroType);
    }

    private SequenceType inferStrictAggregateReturnType(FunctionCallExpression expression, Expression inputExpression) {
        SequenceType inputType = validateStrictAggregateInputType(expression, inputExpression, "fn:avg");
        if (inputType.isEmptySequence()) {
            return SequenceType.createSequenceType("anyAtomicType?");
        }

        ItemType inputItemType = inputType.getItemType();
        ItemType returnItemType = inputItemType.isSubtypeOf(BuiltinTypesCatalogue.numericItem)
                ? BuiltinTypesCatalogue.numericItem
                : inputType.getItemType();

        SequenceType.Arity returnArity =
                (inputType.getArity() == SequenceType.Arity.One || inputType.getArity() == SequenceType.Arity.OneOrMore)
                        ? SequenceType.Arity.One
                        : SequenceType.Arity.OneOrZero;
        return new SequenceType(returnItemType, returnArity);
    }

    private SequenceType inferStrictMinMaxReturnType(
            FunctionCallExpression expression, Expression inputExpression, String functionName) {
        SequenceType inputType = requireInferredType(
                inputExpression.getStaticSequenceType(), expression.getClass().getSimpleName());
        if (inputType.isEmptySequence()) {
            return SequenceType.createSequenceType("anyAtomicType?");
        }

        // Untyped values are compared as xs:double. A node's typed value is only known at runtime.
        ItemType inputItemType = normalizeAggregateItemType(inputType.getItemType());
        boolean nodeInput = TypeAtomization.containsNode(inputType.getItemType())
                && TypeAtomization.isAtomicOrNode(inputType.getItemType());
        if (!nodeInput && !hasMutuallyComparableItems(inputItemType)) {
            throwStaticTypeException(
                    functionName
                            + " requires an atomic input type other than xs:anyAtomicType whose member types can be"
                            + " compared with each other, found "
                            + inputType,
                    ErrorCode.InvalidArgumentType,
                    expression.getMetadata());
        }

        inputType = TypeAtomization.inferType(inputType);
        SequenceType.Arity returnArity =
                (inputType.getArity() == SequenceType.Arity.One || inputType.getArity() == SequenceType.Arity.OneOrMore)
                        ? SequenceType.Arity.One
                        : SequenceType.Arity.OneOrZero;
        return new SequenceType(inputItemType, returnArity);
    }

    /**
     * fn:min and fn:max compare every input item with the others, so every pair of member types must support
     * ordering, e.g. (xs:string | xs:integer) is rejected.
     */
    private static boolean hasMutuallyComparableItems(ItemType itemType) {
        if (!itemType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)
                || itemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            return false;
        }
        return itemType.allMemberTypesMatch(left -> itemType.allMemberTypesMatch(
                right -> areMemberTypesComparable(left, right, ComparisonExpression.ComparisonOperator.VC_LT)));
    }

    private boolean isBuiltinFunctionName(Name functionName, String localName) {
        return functionName.getLocalName().equals(localName)
                && (functionName.getNamespace().equals(Name.JSONIQ_DEFAULT_FUNCTION_NS)
                        || functionName.getNamespace().equals(Name.FN_NS));
    }

    /**
     * For specific input functions we read the schema and annotate static type precisely
     *
     * @param expression function call expression to be annotated
     * @return true if we perform the annotation or false if it is not one of this specific cases
     */
    private boolean tryAnnotateSpecificFunctions(FunctionCallExpression expression, StaticContext staticContext) {
        Name functionName = expression.getFunctionName();
        List<Expression> args = expression.getArguments();

        if (isBuiltinFunctionName(functionName, "data") && args.size() == 1) {
            expression.setStaticSequenceType(
                    TypeAtomization.inferType(args.get(0).getStaticSequenceType()));
            return true;
        }

        // handle 'parquet-file' function
        if (functionName.equals(Name.createVariableInDefaultFunctionNamespace("parquet-file"))
                && args.size() > 0
                && args.get(0) instanceof StringLiteralExpression stringLiteralExpr) {
            String path = stringLiteralExpr.getValue();
            URI uri = FileSystemUtil.resolveFileSystemURI(
                    staticContext.getStaticBaseURI(), path, expression.getMetadata());
            if (!FileSystemUtil.exists(uri, expression.getMetadata())) {
                return false;
            }
            try {
                StructType s = SparkSessionManager.getInstance()
                        .getOrCreateSession()
                        .read()
                        .parquet(FileSystemUtil.convertURIToStringForSpark(uri))
                        .schema();
                ItemType schemaItemType = ItemTypeFactory.createItemType(s);
                // TODO : check if arity is correct
                expression.setStaticSequenceType(new SequenceType(schemaItemType, SequenceType.Arity.ZeroOrMore));
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        // handle 'delta-file' function
        if (functionName.equals(Name.createVariableInDefaultFunctionNamespace("delta-file"))
                && args.size() > 0
                && args.get(0) instanceof StringLiteralExpression stringLiteralExpr) {
            String path = stringLiteralExpr.getValue();
            URI uri = FileSystemUtil.resolveFileSystemURI(
                    staticContext.getStaticBaseURI(), path, expression.getMetadata());
            if (!FileSystemUtil.exists(uri, expression.getMetadata())) {
                return false;
            }
            StructType s = SparkSessionManager.getInstance()
                    .getOrCreateSession()
                    .read()
                    .format("delta")
                    .load(FileSystemUtil.convertURIToStringForSpark(uri))
                    .schema();
            ItemType schemaItemType = ItemTypeFactory.createItemType(s);
            // TODO : check if arity is correct
            expression.setStaticSequenceType(new SequenceType(schemaItemType, SequenceType.Arity.ZeroOrMore));
            return true;
        }

        // handle 'table' function
        if (functionName.equals(Name.createVariableInDefaultFunctionNamespace("table"))
                && args.size() > 0
                && args.get(0) instanceof StringLiteralExpression stringLiteralExpr) {
            String name = stringLiteralExpr.getValue();
            SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
            if (session.catalog().tableExists(name) == false) {
                return false;
            }

            StructType s = session.read().table(name).schema();
            ItemType schemaItemType = ItemTypeFactory.createItemType(s);
            expression.setStaticSequenceType(new SequenceType(schemaItemType, SequenceType.Arity.ZeroOrMore));
            return true;
        }

        // handle 'round' function
        if (isBuiltinFunctionName(functionName, "round")) {
            // set output type to the same of the first argument (special handling of numeric)
            SequenceType input = args.get(0).getStaticSequenceType();
            expression.setStaticSequenceType(
                    TypeAtomization.containsNode(input.getItemType())
                            ? new SequenceType(BuiltinTypesCatalogue.numericItem, SequenceCardinality.ZERO_OR_ONE)
                            : input);
            return true;
        }
        // handle 'size' function
        if (isBuiltinFunctionName(functionName, "size")
                && args.get(0).getStaticSequenceType().getArity() == SequenceType.Arity.One) {
            // set output type to 'Integer' if inputType is 'Array'
            expression.setStaticSequenceType(
                    new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One));
            return true;
        }

        if (isBuiltinFunctionName(functionName, "sum")) {
            expression.setStaticSequenceType(inferSumReturnType(expression));
            return true;
        }

        if (isBuiltinFunctionName(functionName, "avg")) {
            expression.setStaticSequenceType(inferStrictAggregateReturnType(expression, args.get(0)));
            return true;
        }

        if (isBuiltinFunctionName(functionName, "min")) {
            expression.setStaticSequenceType(inferStrictMinMaxReturnType(expression, args.get(0), "fn:min"));
            return true;
        }

        if (isBuiltinFunctionName(functionName, "max")) {
            expression.setStaticSequenceType(inferStrictMinMaxReturnType(expression, args.get(0), "fn:max"));
            return true;
        }

        return false;
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
                SequenceType actualType = parameterExpressions.get(i).getStaticSequenceType();
                if (actualType == null) {
                    throw new OurBadException("No static type inferred for expression " + parameterExpressions.get(i));
                }
                SequenceType expectedType = parameterTypes.get(i);
                if (!isFunctionArgumentCompatible(actualType, expectedType)) {
                    throwStaticTypeException(
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
            // try annotate specific functions
            if (!tryAnnotateSpecificFunctions(expression, argument)) {
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
                        SequenceType argumentType = parameterExpressions.get(0).getStaticSequenceType();
                        if (argumentType != null
                                && TypeAtomization.inferType(argumentType).getCardinality() == SequenceCardinality.ONE
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
        XmlSchemaCatalog schemaCatalog = argument.getInScopeSchemaTypes().getXmlSchemaCatalog();
        if (isSchemaCastTarget(expression.getSequenceType(), schemaCatalog)) {
            checkCastOperand(expression.getMainExpression().getStaticSequenceType(), expression);
            expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem));
            return argument;
        }
        ItemType itemType = expression.getSequenceType().getItemType();
        if (itemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            throwStaticTypeException(
                    "atomic item type is not allowed in castable expression",
                    ErrorCode.CastableErrorCode,
                    expression.getMetadata());
        }
        checkCastOperand(expression.getMainExpression().getStaticSequenceType(), expression);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitCastExpression(CastExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        XmlSchemaCatalog schemaCatalog = argument.getInScopeSchemaTypes().getXmlSchemaCatalog();
        if (isSchemaCastTarget(expression.getSequenceType(), schemaCatalog)) {
            SequenceType expressionType = expression.getMainExpression().getStaticSequenceType();
            checkCastOperand(expressionType, expression);

            if (expressionType.isEmptySequence()) {
                if (expression.getSequenceType().getArity() != SequenceType.Arity.OneOrZero) {
                    throwStaticTypeException(
                            "Empty sequence cannot be cast to a non-optional XML Schema simple type.",
                            expression.getMetadata());
                }
                expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.Zero));
                return argument;
            }

            SequenceType atomizedOperand = atomizedCastOperand(expressionType);
            if (atomizedOperand != null
                    && !atomizedOperand.isAritySubtypeOf(
                            expression.getSequenceType().getArity())) {
                throwStaticTypeException(
                        castCardinalityMessage(expressionType, atomizedOperand, expression.getSequenceType()),
                        expression.getMetadata());
            }

            // Check the type of result will casting to this schema type produce
            SequenceType resultType = schemaCatalog.getSimpleTypeCastResultType(
                    expression.getSequenceType().getItemType().getName());

            if (resultType.getArity() == SequenceType.Arity.One
                    && expression.getSequenceType().getArity() == SequenceType.Arity.OneOrZero
                    && (atomizedOperand == null || atomizedOperand.getArity() != SequenceType.Arity.One)) {
                // Because getSimpleTypeCastResultType does not take into account the arity of the cast expression,
                // this if-statement is needed to ensure that the result type is correctly set to OneOrZero when the
                // cast expression has an optional arity.
                resultType = new SequenceType(resultType.getItemType(), SequenceType.Arity.OneOrZero);
            }
            expression.setStaticSequenceType(resultType);
            return argument;
        }

        // check at static time for casting errors (note cast only allows for normal or ? arity)
        SequenceType expressionSequenceType = expression.getMainExpression().getStaticSequenceType();
        SequenceType castedSequenceType = expression.getSequenceType();

        if (castedSequenceType.getItemType().equals(BuiltinTypesCatalogue.atomicItem)) {
            throwStaticTypeException(
                    "atomic item type is not allowed in cast expression",
                    ErrorCode.CastableErrorCode,
                    expression.getMetadata());
        }

        // Empty sequence case
        if (expressionSequenceType.isEmptySequence()) {
            if (castedSequenceType.getArity() != SequenceType.Arity.OneOrZero) {
                throwStaticTypeException(
                        "Empty sequence cannot be cast to type with quantifier different from '?'",
                        expression.getMetadata());
            } else {
                // no additional check is needed
                expression.setStaticSequenceType(castedSequenceType);
                return argument;
            }
        }

        SequenceType atomizedOperand = atomizedCastOperand(expressionSequenceType);
        if (atomizedOperand != null && !atomizedOperand.isAritySubtypeOf(castedSequenceType.getArity())) {
            throwStaticTypeException(
                    castCardinalityMessage(expressionSequenceType, atomizedOperand, castedSequenceType),
                    expression.getMetadata());
        }

        checkCastOperand(expressionSequenceType, expression);
        // Non-atomic operands were reported above; with static typing disabled, leave them to runtime.
        if (TypeAtomization.isAtomicOrNode(expressionSequenceType.getItemType())
                && !castedSequenceType.getItemType().equals(BuiltinTypesCatalogue.errorItem)
                && !isCastOperandTypeCompatible(
                        expressionSequenceType.getItemType(), castedSequenceType.getItemType())) {
            throwStaticTypeException(
                    "It is never possible to cast a " + expressionSequenceType + " as " + castedSequenceType,
                    ErrorCode.UnexpectedTypeErrorCode,
                    expression.getMetadata());
        }
        if (TypeAtomization.inferType(expressionSequenceType).getCardinality() == SequenceCardinality.ONE) {
            castedSequenceType = new SequenceType(castedSequenceType.getItemType(), SequenceType.Arity.One);
        }
        expression.setStaticSequenceType(castedSequenceType);
        return argument;
    }

    /**
     * Cast cardinality applies after atomization: one node can yield zero values (a nilled element), one value, or
     * several (a schema list). Returns null when the typed value is only known at runtime.
     */
    private static SequenceType atomizedCastOperand(SequenceType operand) {
        if (!TypeAtomization.containsNode(operand.getItemType())) {
            return operand;
        }
        return TypeAtomization.hasUnknownTypedValue(operand.getItemType()) ? null : TypeAtomization.inferType(operand);
    }

    private static String castCardinalityMessage(SequenceType operand, SequenceType atomized, SequenceType target) {
        String found = operand.equals(atomized) ? operand.toString() : operand + " with typed value " + atomized;
        return "with static type feature it is not possible to cast a " + found + " as " + target;
    }

    private boolean isSchemaCastTarget(SequenceType sequenceType, XmlSchemaCatalog schemaCatalog) {
        ItemType itemType = sequenceType.getItemType();
        return itemType.hasName() && schemaCatalog.isSchemaCastTarget(itemType.getName());
    }

    /**
     * Accepts atomic operands and nodes whose typed values are atomized at runtime.
     * A node is not itself atomic, but its typed value can supply atomic cast operands.
     * The static node types used here do not say whether atomization succeeds or how many
     * atomic values it produces; the runtime checks those properties after atomization.
     */
    private void checkCastOperand(SequenceType operandType, Expression expression) {
        basicChecks(operandType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());
        if (!operandType.isEmptySequence() && !TypeAtomization.isAtomicOrNode(operandType.getItemType())) {
            throwStaticTypeException(
                    "A cast operand must be atomic after atomization, found " + operandType,
                    operandType.getItemType().isSubtypeOf(BuiltinTypesCatalogue.JSONItem)
                            ? ErrorCode.NonAtomicElementErrorCode
                            : ErrorCode.AtomizationError,
                    expression.getMetadata());
        }
    }

    private boolean isCastOperandTypeCompatible(ItemType source, ItemType target) {
        // A node is cast through its typed value: an unknown one is checked at runtime, a known one statically.
        return source.allMemberTypesMatch(member -> TypeAtomization.hasUnknownTypedValue(member)
                || TypeAtomization.atomizedItemType(member)
                        .allMemberTypesMatch(atomized -> atomized.isStaticallyCastableAs(target)));
    }

    @Override
    public StaticContext visitIsStaticallyExpr(IsStaticallyExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType inferred = expression.getMainExpression().getStaticSequenceType();
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
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitTreatExpression(TreatExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        // check at static time for treat errors
        SequenceType expressionSequenceType = expression.getMainExpression().getStaticSequenceType();
        SequenceType treatedSequenceType = expression.getSequenceType();

        if (expressionSequenceType == null || treatedSequenceType == null) {
            throwStaticTypeException(
                    "The child expression of a Treat expression has no inferred type or it is being treated as null sequence type",
                    expression.getMetadata());
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
        SequenceType argumentType =
                requireInferredType(expression.getArgument().getStaticSequenceType(), "FunctionArgumentConversion");
        SequenceType parameterType = expression.getParameterType();
        if (!isFunctionArgumentCompatible(argumentType, parameterType)) {
            throwStaticTypeException(
                    "Argument requires " + parameterType + " but " + argumentType + " was found",
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
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitRenameExpression(RenameExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitReplaceExpression(ReplaceExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitInsertExpression(InsertExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitAppendExpression(AppendExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitTransformExpression(TransformExpression expression, StaticContext argument) {
        for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
            visit(copyDecl.getSourceExpression(), argument);
            SequenceType declaredType = copyDecl.getSourceSequenceType();
            SequenceType inferredType;
            if (declaredType == null) {
                inferredType = copyDecl.getSourceExpression().getStaticSequenceType();
            } else {
                inferredType = declaredType;
            }
            checkAndUpdateVariableStaticType(
                    declaredType,
                    inferredType,
                    argument,
                    expression.getClass().getSimpleName(),
                    copyDecl.getVariableName(),
                    expression.getMetadata());
        }
        visit(expression.getModifyExpression(), argument);
        visit(expression.getReturnExpression(), argument);
        expression.setStaticSequenceType(expression.getReturnExpression().getStaticSequenceType());

        return argument;
    }

    @Override
    public StaticContext visitCreateCollectionExpression(
            CreateCollectionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitDeleteIndexFromCollectionExpression(
            DeleteIndexFromCollectionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitDeleteSearchFromCollectionExpression(
            DeleteSearchFromCollectionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitEditCollectionExpression(EditCollectionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitInsertIndexIntoCollectionExpression(
            InsertIndexIntoCollectionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitInsertSearchIntoCollectionExpression(
            InsertSearchIntoCollectionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitTruncateCollectionExpression(
            TruncateCollectionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    // endregion

    // region arithmetic

    @Override
    public StaticContext visitAdditiveExpr(AdditiveExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType leftInferredType = expression.getLeftExpression().getStaticSequenceType();
        SequenceType rightInferredType = expression.getRightExpression().getStaticSequenceType();

        basicChecks(
                Arrays.asList(leftInferredType, rightInferredType),
                expression.getClass().getSimpleName(),
                true,
                true,
                expression.getMetadata());

        ItemType inferredType = null;
        SequenceType.Arity inferredArity = resolveArities(leftInferredType, rightInferredType);

        // arity check
        if (inferredArity == null) {
            throwStaticTypeException(
                    "'+' and '*' arities are not allowed for additive expressions", expression.getMetadata());
            inferredArity = SequenceType.Arity.OneOrZero;
        }

        ItemType leftItemType = leftInferredType.getItemType();
        ItemType rightItemType = rightInferredType.getItemType();

        inferredType = inferBinaryOperationType(
                leftItemType, rightItemType, (left, right) -> inferAdditiveItemType(left, right, expression.isMinus()));

        if (inferredType == null) {
            if (inferredArity == SequenceType.Arity.OneOrZero) {
                // Only possible resulting type is empty sequence so throw error XPST0005
                throwStaticTypeException(
                        "Inferred type is empty sequence and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                        expression.getMetadata());
            } else {
                throwStaticTypeException(
                        "The following types operation is not possible: "
                                + leftInferredType
                                + (expression.isMinus() ? " - " : " + ")
                                + rightInferredType,
                        expression.getMetadata());
            }
            inferredType = BuiltinTypesCatalogue.atomicItem;
        }

        expression.setStaticSequenceType(new SequenceType(inferredType, inferredArity));
        return argument;
    }

    // Evaluate each possible operand pair. A null result means an unsupported pair.
    private ItemType inferBinaryOperationType(
            ItemType left, ItemType right, BiFunction<ItemType, ItemType, ItemType> operation) {
        List<ItemType> results = new ArrayList<>();
        for (ItemType leftMember : left.getMemberTypes()) {
            for (ItemType rightMember : right.getMemberTypes()) {
                ItemType result = inferMemberOperationType(leftMember, rightMember, operation);
                if (result == null) {
                    return null;
                }
                results.add(result);
            }
        }
        return ItemTypeFactory.createInferredUnionType(results);
    }

    private ItemType inferMemberOperationType(
            ItemType left, ItemType right, BiFunction<ItemType, ItemType, ItemType> operation) {
        if (!TypeAtomization.isAtomicOrNode(left) || !TypeAtomization.isAtomicOrNode(right)) {
            return null;
        }
        if (!TypeAtomization.hasUnknownTypedValue(left)
                && !TypeAtomization.hasUnknownTypedValue(right)
                && (left.equals(BuiltinTypesCatalogue.atomicItem) || right.equals(BuiltinTypesCatalogue.atomicItem))) {
            return null; // Retain strict checks for declared, unrefined atomic operands.
        }
        return operation.apply(normalizeArithmeticItemType(left), normalizeArithmeticItemType(right));
    }

    private ItemType normalizeArithmeticItemType(ItemType type) {
        ItemType atomized = TypeAtomization.atomizedItemType(type);
        return atomized.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)
                ? BuiltinTypesCatalogue.doubleItem
                : atomized;
    }

    private ItemType inferAdditiveItemType(ItemType leftItemType, ItemType rightItemType, boolean minus) {
        if (leftItemType.equals(BuiltinTypesCatalogue.atomicItem)
                || rightItemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            // An unknown node typed value leaves successful arithmetic results atomic.
            return BuiltinTypesCatalogue.atomicItem;
        }
        leftItemType = leftItemType.getCastingPrimitiveType();
        rightItemType = rightItemType.getCastingPrimitiveType();
        ItemType result = null;
        // check item type combination
        if (leftItemType.isNumeric()) {
            if (rightItemType.isNumeric()) {
                result = resolveNumericType(leftItemType, rightItemType);
            }
        } else if (leftItemType.equals(BuiltinTypesCatalogue.dateItem)
                || leftItemType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            if (rightItemType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)
                    || rightItemType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
                result = leftItemType;
            } else if (minus && rightItemType.equals(leftItemType)) {
                result = BuiltinTypesCatalogue.dayTimeDurationItem;
            }
        } else if (leftItemType.equals(BuiltinTypesCatalogue.timeItem)) {
            if (rightItemType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
                result = leftItemType;
            } else if (minus && rightItemType.equals(leftItemType)) {
                result = BuiltinTypesCatalogue.dayTimeDurationItem;
            }
        } else if (leftItemType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
            if (rightItemType.equals(leftItemType)) {
                result = leftItemType;
            } else if (!minus
                    && (rightItemType.equals(BuiltinTypesCatalogue.dateTimeItem)
                            || rightItemType.equals(BuiltinTypesCatalogue.dateItem)
                            || rightItemType.equals(BuiltinTypesCatalogue.timeItem))) {
                result = rightItemType;
            }
        } else if (leftItemType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
            if (rightItemType.equals(leftItemType)) {
                result = leftItemType;
            } else if (!minus
                    && (rightItemType.equals(BuiltinTypesCatalogue.dateTimeItem)
                            || rightItemType.equals(BuiltinTypesCatalogue.dateItem))) {
                result = rightItemType;
            }
        }

        return result;
    }

    private ItemType inferMultiplicativeItemType(
            ItemType leftItemType, ItemType rightItemType, MultiplicativeExpression.MultiplicativeOperator operator) {
        if (leftItemType.equals(BuiltinTypesCatalogue.atomicItem)
                || rightItemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            return operator == MultiplicativeExpression.MultiplicativeOperator.IDIV
                    ? BuiltinTypesCatalogue.integerItem
                    : BuiltinTypesCatalogue.atomicItem;
        }
        leftItemType = leftItemType.getCastingPrimitiveType();
        rightItemType = rightItemType.getCastingPrimitiveType();
        ItemType result = null;
        // check resulting item for each operation
        if (leftItemType.isNumeric()) {
            if (rightItemType.isNumeric()) {
                if (operator == MultiplicativeExpression.MultiplicativeOperator.IDIV) {
                    result = BuiltinTypesCatalogue.integerItem;
                } else if (operator == MultiplicativeExpression.MultiplicativeOperator.DIV) {
                    result = resolveNumericType(
                            BuiltinTypesCatalogue.decimalItem, resolveNumericType(leftItemType, rightItemType));
                } else {
                    result = resolveNumericType(leftItemType, rightItemType);
                }
            } else if (rightItemType.isSubtypeOf(BuiltinTypesCatalogue.durationItem)
                    && !rightItemType.equals(BuiltinTypesCatalogue.durationItem)
                    && operator == MultiplicativeExpression.MultiplicativeOperator.MUL) {
                result = rightItemType;
            }
        } else if (leftItemType.isSubtypeOf(BuiltinTypesCatalogue.durationItem)
                && !leftItemType.equals(BuiltinTypesCatalogue.durationItem)) {
            if (rightItemType.isNumeric()
                    && (operator == MultiplicativeExpression.MultiplicativeOperator.MUL
                            || operator == MultiplicativeExpression.MultiplicativeOperator.DIV)) {
                result = leftItemType;
            } else if (rightItemType.equals(leftItemType)) {
                result = BuiltinTypesCatalogue.decimalItem;
            }
        }

        return result;
    }

    // Both operands are concrete numeric types.
    private ItemType resolveNumericType(ItemType left, ItemType right) {
        if (left.equals(BuiltinTypesCatalogue.doubleItem) || right.equals(BuiltinTypesCatalogue.doubleItem)) {
            return BuiltinTypesCatalogue.doubleItem;
        } else if (left.equals(BuiltinTypesCatalogue.floatItem) || right.equals(BuiltinTypesCatalogue.floatItem)) {
            return BuiltinTypesCatalogue.floatItem;
        } else if (left.equals(BuiltinTypesCatalogue.decimalItem) || right.equals(BuiltinTypesCatalogue.decimalItem)) {
            return BuiltinTypesCatalogue.decimalItem;
        } else {
            return BuiltinTypesCatalogue.integerItem;
        }
    }

    // Singleton operators constrain atomized values. Unknown node typed values are
    // checked at runtime; known multi-valued atomic operands remain static errors.
    private SequenceType.Arity atomizedSingletonArity(SequenceType source) {
        SequenceType atomized =
                TypeAtomization.isAtomicOrNode(source.getItemType()) ? TypeAtomization.inferType(source) : source;
        if (atomized.getCardinality().allowsMany() && !TypeAtomization.hasUnknownTypedValue(source.getItemType())) {
            return null;
        }
        return atomized.getCardinality().allowsZero() ? SequenceType.Arity.OneOrZero : SequenceType.Arity.One;
    }

    private SequenceType.Arity resolveArities(SequenceType leftType, SequenceType rightType) {
        SequenceType.Arity left = atomizedSingletonArity(leftType);
        SequenceType.Arity right = atomizedSingletonArity(rightType);
        if (left == null || right == null) return null;
        return (left == SequenceType.Arity.OneOrZero || right == SequenceType.Arity.OneOrZero)
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.One;
    }

    private boolean checkSwitchType(List<Expression> expressions, SequenceType testType, ExceptionMetadata metadata) {
        boolean addToReturnType = false;
        for (Expression caseExpression : expressions) {
            // test the case expression
            checkSwitchType(caseExpression.getStaticSequenceType(), metadata);
            // if has overlap with the test condition will add the return type to the possible ones
            if (caseExpression.getStaticSequenceType().hasOverlapWith(testType)) {
                addToReturnType = true;
            }
        }
        return addToReturnType;
    }

    @Override
    public StaticContext visitMultiplicativeExpr(MultiplicativeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType leftInferredType = expression.getLeftExpression().getStaticSequenceType();
        SequenceType rightInferredType = expression.getRightExpression().getStaticSequenceType();

        basicChecks(
                Arrays.asList(leftInferredType, rightInferredType),
                expression.getClass().getSimpleName(),
                true,
                true,
                expression.getMetadata());

        ItemType inferredType = null;
        SequenceType.Arity inferredArity = resolveArities(leftInferredType, rightInferredType);

        if (inferredArity == null) {
            throwStaticTypeException(
                    "'+' and '*' arities are not allowed for multiplicative expressions", expression.getMetadata());
            inferredArity = SequenceType.Arity.OneOrZero;
        }

        ItemType leftItemType = leftInferredType.getItemType();
        ItemType rightItemType = rightInferredType.getItemType();

        inferredType = inferBinaryOperationType(
                leftItemType,
                rightItemType,
                (left, right) -> inferMultiplicativeItemType(left, right, expression.getMultiplicativeOperator()));

        if (inferredType == null) {
            if (inferredArity == SequenceType.Arity.OneOrZero) {
                // Only possible resulting type is empty sequence so throw error XPST0005
                throwStaticTypeException(
                        "Inferred type is empty sequence and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                        expression.getMetadata());
            } else {
                throwStaticTypeException(
                        "The following types expression is not valid: "
                                + leftItemType
                                + " "
                                + expression.getMultiplicativeOperator()
                                + " "
                                + rightItemType,
                        expression.getMetadata());
            }
            inferredType = BuiltinTypesCatalogue.atomicItem;
        }

        expression.setStaticSequenceType(new SequenceType(inferredType, inferredArity));
        return argument;
    }

    @Override
    public StaticContext visitUnaryExpr(UnaryExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType childInferredType =
                requireInferredType(expression.getMainExpression().getStaticSequenceType(), "UnaryExpression");

        // if the child is the empty sequence just infer the empty sequence
        if (childInferredType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }

        SequenceType.Arity resultArity = atomizedSingletonArity(childInferredType);
        if (resultArity == null) {
            throwStaticTypeException(
                    "'+' and '*' arities are not allowed for unary expressions", expression.getMetadata());
            resultArity = SequenceType.Arity.OneOrZero;
        }

        // if inferred arity does not allow for empty sequence and static type is not an accepted one throw a static
        // error
        ItemType childItemType = inferUnaryItemType(childInferredType.getItemType());
        if (childItemType == null) {
            if (childInferredType.getArity() == SequenceType.Arity.OneOrZero) {
                throwStaticTypeException(
                        "Inferred type is empty sequence and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                        expression.getMetadata());
            } else {
                throwStaticTypeException(
                        "It is not possible to have an Unary expression with the following type: " + childInferredType,
                        expression.getMetadata());
            }
            childItemType = BuiltinTypesCatalogue.numericItem;
        }

        expression.setStaticSequenceType(new SequenceType(childItemType, resultArity));
        return argument;
    }

    private ItemType inferUnaryItemType(ItemType type) {
        List<ItemType> results = new ArrayList<>();
        for (ItemType member : type.getMemberTypes()) {
            if (!TypeAtomization.isAtomicOrNode(member)) {
                return null;
            }
            if (TypeAtomization.hasUnknownTypedValue(member)) {
                results.add(BuiltinTypesCatalogue.numericItem);
                continue;
            }
            ItemType normalized = normalizeArithmeticItemType(member);
            if (!normalized.isNumeric()) {
                return null;
            }
            results.add(normalized);
        }
        return ItemTypeFactory.createInferredUnionType(results);
    }

    // endregion

    // region logic

    private StaticContext visitAndOrExpr(Expression expression, StaticContext argument, String expressionName) {
        visitDescendants(expression, argument);

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = requireInferredType(
                ((Expression) childrenExpressions.get(0)).getStaticSequenceType(), expressionName + "Expression");
        SequenceType rightInferredType = requireInferredType(
                ((Expression) childrenExpressions.get(1)).getStaticSequenceType(), expressionName + "Expression");

        if (!leftInferredType.hasEffectiveBooleanValue()) {
            throwStaticTypeException(
                    "left expression of a "
                            + expressionName
                            + "Expression has "
                            + leftInferredType
                            + " inferred type, which has no effective boolean value",
                    expression.getMetadata());
        }

        if (!rightInferredType.hasEffectiveBooleanValue()) {
            throwStaticTypeException(
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

        SequenceType childInferredType =
                requireInferredType(expression.getMainExpression().getStaticSequenceType(), "NotExpression");
        if (!childInferredType.hasEffectiveBooleanValue()) {
            throwStaticTypeException(
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

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = requireInferredType(
                ((Expression) childrenExpressions.get(0)).getStaticSequenceType(), "ComparisonExpression");
        SequenceType rightInferredType = requireInferredType(
                ((Expression) childrenExpressions.get(1)).getStaticSequenceType(), "ComparisonExpression");
        SequenceType.Arity returnArity = SequenceType.Arity.One;

        ComparisonExpression.ComparisonOperator operator = expression.getComparisonOperator();

        // for value comparison arities * and + are not allowed, also if one return the empty sequence for sure throw
        // XPST0005 error
        if (operator.isValueComparison()) {
            if (leftInferredType.isEmptySequence() || rightInferredType.isEmptySequence()) {
                throwStaticTypeException(
                        "Inferred type is empty sequence and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                        expression.getMetadata());
            }
            returnArity = resolveArities(leftInferredType, rightInferredType);
            if (returnArity == null) {
                throwStaticTypeException(
                        "'+' and '*' arities are not allowed for this comparison operator: " + operator,
                        expression.getMetadata());
                returnArity = SequenceType.Arity.OneOrZero;
            }
        }

        // if any of the element is the empty sequence, we set its sequence type to the other, if both are we do not
        // need additional checks
        boolean isLeftEmpty = leftInferredType.isEmptySequence();
        boolean isRightEmpty = rightInferredType.isEmptySequence();
        if (!isLeftEmpty || !isRightEmpty) {

            ItemType leftItemType = isLeftEmpty ? rightInferredType.getItemType() : leftInferredType.getItemType();
            ItemType rightItemType = isRightEmpty ? leftInferredType.getItemType() : rightInferredType.getItemType();

            if (!TypeAtomization.isAtomicOrNode(leftItemType) || !TypeAtomization.isAtomicOrNode(rightItemType)) {
                throwStaticTypeException(
                        "It is not possible to compare with non-atomic types",
                        ErrorCode.NonAtomicElementErrorCode,
                        expression.getMetadata());
            }

            if (!areComparisonTypesCompatible(leftItemType, rightItemType, operator)) {
                throwStaticTypeException(
                        "It is not possible to compare these types: "
                                + leftItemType
                                + " "
                                + operator
                                + " "
                                + rightItemType,
                        expression.getMetadata());
            }
        }

        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem, returnArity));
        return argument;
    }

    // Static typing is pessimistic (XQuery 3.1, 2.2.3.1): every pair of member types must be comparable.
    private static boolean areComparisonTypesCompatible(
            ItemType left, ItemType right, ComparisonExpression.ComparisonOperator operator) {
        return left.allMemberTypesMatch(leftMember -> right.allMemberTypesMatch(
                rightMember -> areAtomizedComparisonTypesCompatible(leftMember, rightMember, operator)));
    }

    private static boolean areAtomizedComparisonTypesCompatible(
            ItemType left, ItemType right, ComparisonExpression.ComparisonOperator operator) {
        if (TypeAtomization.hasUnknownTypedValue(left) || TypeAtomization.hasUnknownTypedValue(right)) {
            return true; // Unknown typed values require runtime comparison checks.
        }
        left = TypeAtomization.atomizedItemType(left);
        right = TypeAtomization.atomizedItemType(right);
        boolean leftUntyped = left.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem);
        boolean rightUntyped = right.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem);
        if (leftUntyped) {
            left = operator.isValueComparison() || rightUntyped
                    ? BuiltinTypesCatalogue.stringItem
                    : right.isNumeric() ? BuiltinTypesCatalogue.doubleItem : right.getCastingPrimitiveType();
        }
        if (rightUntyped) {
            right = operator.isValueComparison() || leftUntyped
                    ? BuiltinTypesCatalogue.stringItem
                    : left.isNumeric() ? BuiltinTypesCatalogue.doubleItem : left.getCastingPrimitiveType();
        }
        return areMemberTypesComparable(left, right, operator);
    }

    private static boolean areMemberTypesComparable(
            ItemType left, ItemType right, ComparisonExpression.ComparisonOperator operator) {
        // JSONiq null is comparable with every atomic value, including for ordering.
        if (left.equals(BuiltinTypesCatalogue.nullItem) || right.equals(BuiltinTypesCatalogue.nullItem)) {
            return true;
        }
        boolean compatible = left.equals(right)
                || (left.isNumeric() && right.isNumeric())
                || (left.isSubtypeOf(BuiltinTypesCatalogue.durationItem)
                        && right.isSubtypeOf(BuiltinTypesCatalogue.durationItem))
                || (left.canBePromotedTo(BuiltinTypesCatalogue.stringItem)
                        && right.canBePromotedTo(BuiltinTypesCatalogue.stringItem));
        if (!compatible) {
            return false;
        }
        boolean ordered = operator != ComparisonExpression.ComparisonOperator.VC_EQ
                && operator != ComparisonExpression.ComparisonOperator.VC_NE
                && operator != ComparisonExpression.ComparisonOperator.GC_EQ
                && operator != ComparisonExpression.ComparisonOperator.GC_NE;
        // XQuery 3.1 supports binary ordering. Durations are ordered only within
        // the day-time subtype or within the year-month subtype.
        return !ordered
                || !(left.isSubtypeOf(BuiltinTypesCatalogue.durationItem)
                        || right.isSubtypeOf(BuiltinTypesCatalogue.durationItem))
                || (left.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem)
                        && right.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem))
                || (left.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                        && right.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem));
    }

    @Override
    public StaticContext visitNodeComparisonExpr(NodeComparisonExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType leftType =
                requireInferredType(expression.getLeftExpression().getStaticSequenceType(), "NodeComparisonExpression");
        SequenceType rightType = requireInferredType(
                expression.getRightExpression().getStaticSequenceType(), "NodeComparisonExpression");

        SequenceType operandType = new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceCardinality.ZERO_OR_ONE);
        for (SequenceType type : List.of(leftType, rightType)) {
            if (!type.isSubtypeOf(operandType)) {
                throwStaticTypeException(
                        "A node comparison operand must be a single node or empty, found " + type,
                        expression.getMetadata());
            }
        }

        // XQuery 3.1 section 3.7.3: an empty operand produces an empty result. Otherwise,
        // a successful comparison produces one boolean, even when operand checks are deferred to runtime.
        SequenceCardinality cardinality;
        if (leftType.isEmptySequence() || rightType.isEmptySequence()) {
            // The strict Static Typing Feature rejects known-empty expressions other than () and data(()).
            throwStaticTypeException(
                    "The node comparison is statically inferred to return an empty sequence",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
            cardinality = SequenceCardinality.EMPTY;
        } else if (leftType.getCardinality().allowsZero()
                || rightType.getCardinality().allowsZero()) {
            cardinality = SequenceCardinality.ZERO_OR_ONE;
        } else {
            cardinality = SequenceCardinality.ONE;
        }
        expression.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem, cardinality));
        return argument;
    }

    @Override
    public StaticContext visitNodeSetExpr(NodeSetExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType leftType = expression.getLeftExpression().getStaticSequenceType();
        SequenceType rightType = expression.getRightExpression().getStaticSequenceType();
        basicChecks(
                Arrays.asList(leftType, rightType),
                expression.getClass().getSimpleName(),
                false,
                false,
                expression.getMetadata());
        if (!leftType.isEmptySequence() && !leftType.getItemType().isNodeItemType()) {
            throwStaticTypeException(
                    "Left operand of a node set expression must be a sequence of nodes, got " + leftType,
                    expression.getMetadata());
        }
        if (!rightType.isEmptySequence() && !rightType.getItemType().isNodeItemType()) {
            throwStaticTypeException(
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

        SequenceType ifType = expression.getCondition().getStaticSequenceType();
        SequenceType thenType = expression.getBranch().getStaticSequenceType();
        SequenceType elseType = expression.getElseBranch().getStaticSequenceType();

        if (ifType == null || thenType == null || elseType == null) {
            throw new OurBadException(
                    "A child expression of a ConditionalExpression has no inferred type", expression.getMetadata());
        }

        if (!ifType.hasEffectiveBooleanValue()) {
            throwStaticTypeException(
                    "The condition in the 'if' must have effective boolean value, found inferred type: "
                            + ifType
                            + " (which has not effective boolean value)",
                    expression.getMetadata());
        }

        // if the if branch is false at static time (i.e. subtype of null?) we only use else branch
        SequenceType resultingType = ifType.isSubtypeOf(SequenceType.createSequenceType("null?"))
                ? elseType
                : thenType.leastCommonSupertypeWith(elseType);

        if (resultingType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }

        expression.setStaticSequenceType(resultingType);
        return argument;
    }

    // throw errors if [type] does not conform to switch test and cases requirements
    public void checkSwitchType(SequenceType type, ExceptionMetadata metadata) {
        if (type == null) {
            throw new OurBadException("A child expression of a SwitchExpression has no inferred type", metadata);
        }
        if (type.isEmptySequence()) {
            return; // no further check is required
        }
        if (type.getArity() == SequenceType.Arity.OneOrMore || type.getArity() == SequenceType.Arity.ZeroOrMore) {
            throwStaticTypeException(
                    "+ and * arities are not allowed for the expressions of switch test condition and cases", metadata);
        }
        ItemType itemType = type.getItemType();
        if (itemType.isFunctionItemType()) {
            throwStaticTypeException(
                    "function item not allowed for the expressions of switch test condition and cases",
                    ErrorCode.UnexpectedFunctionItem,
                    metadata);
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.JSONItem)) {
            throwStaticTypeException(
                    "switch test condition and cases expressions' item type must match atomic, instead inferred: "
                            + itemType,
                    ErrorCode.NonAtomicElementErrorCode,
                    metadata);
        }
        if (!itemType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            throwStaticTypeException(
                    "switch test condition and cases expressions' item type must match atomic, instead inferred: "
                            + itemType,
                    metadata);
        }
    }

    @Override
    public StaticContext visitSwitchExpression(SwitchExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType testType = expression.getTestCondition().getStaticSequenceType();
        checkSwitchType(testType, expression.getMetadata());

        SequenceType returnType = expression.getDefaultExpression().getStaticSequenceType();
        if (returnType == null) {
            throw new OurBadException(
                    "A child expression of a SwitchExpression has no inferred type", expression.getMetadata());
        }

        for (SwitchCase switchCase : expression.getCases()) {
            boolean addToReturnType =
                    checkSwitchType(switchCase.getConditionExpressions(), testType, expression.getMetadata());
            SequenceType caseReturnType = switchCase.getReturnExpression().getStaticSequenceType();
            if (caseReturnType == null) {
                throw new OurBadException(
                        "A child expression of a SwitchExpression has no inferred type", expression.getMetadata());
            }
            if (addToReturnType) {
                returnType = returnType.leastCommonSupertypeWith(caseReturnType);
            }
        }

        expression.setStaticSequenceType(returnType);
        return argument;
    }

    @Override
    public StaticContext visitTryCatchExpression(TryCatchExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType inferredType = null;

        for (Node childNode : expression.getChildren()) {
            SequenceType childType = ((Expression) childNode).getStaticSequenceType();
            if (childType == null) {
                throw new OurBadException(
                        "A child expression of a TryCatchExpression has no inferred type", expression.getMetadata());
            }

            if (inferredType == null) {
                inferredType = childType;
            } else {
                inferredType = inferredType.leastCommonSupertypeWith(childType);
            }
        }
        inferredType = requireInferredType(inferredType, expression.getClass().getSimpleName());

        if (inferredType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }
        expression.setStaticSequenceType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitTypeSwitchExpression(TypeSwitchExpression expression, StaticContext argument) {
        visit(expression.getTestCondition(), argument);
        SequenceType inferredType = null;

        SequenceType conditionType = expression.getTestCondition().getStaticSequenceType();
        basicChecks(conditionType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());

        for (TypeswitchCase typeswitchCase : expression.getCases()) {
            Name variableName = typeswitchCase.getVariableName();
            Expression returnExpression = typeswitchCase.getReturnExpression();
            // if we bind a variable we add the static type of it in the context of the return expression
            if (variableName != null) {
                SequenceType variableType = null;
                for (SequenceType st : typeswitchCase.getUnion()) {
                    variableType = variableType == null ? st : variableType.leastCommonSupertypeWith(st);
                }
                returnExpression.getStaticContext().replaceVariableSequenceType(variableName, variableType);
            }

            visit(returnExpression, argument);
            SequenceType caseType = returnExpression.getStaticSequenceType();
            basicChecks(caseType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());
            inferredType = inferredType == null ? caseType : inferredType.leastCommonSupertypeWith(caseType);
        }

        Name variableName = expression.getDefaultCase().getVariableName();
        Expression returnExpression = expression.getDefaultCase().getReturnExpression();
        // if we bind a variable in the default case, we infer testCondition type
        if (variableName != null) {
            returnExpression.getStaticContext().replaceVariableSequenceType(variableName, conditionType);
        }
        visit(returnExpression, argument);
        SequenceType defaultType = returnExpression.getStaticSequenceType();
        basicChecks(defaultType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());
        inferredType = inferredType == null ? defaultType : inferredType.leastCommonSupertypeWith(defaultType);

        basicChecks(inferredType, expression.getClass().getSimpleName(), false, true, expression.getMetadata());
        expression.setStaticSequenceType(inferredType);
        return argument;
    }

    // endregion

    // region miscellaneous

    @Override
    public StaticContext visitRangeExpr(RangeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> children = expression.getChildren();
        SequenceType leftType = ((Expression) children.get(0)).getStaticSequenceType();
        SequenceType rightType = ((Expression) children.get(1)).getStaticSequenceType();

        if (leftType == null) {
            throw new OurBadException(
                    "A child expression of a RangeExpression has no inferred type",
                    ((Expression) children.get(0)).getMetadata());
        }

        if (rightType == null) {
            throw new OurBadException(
                    "A child expression of a RangeExpression has no inferred type",
                    ((Expression) children.get(1)).getMetadata());
        }

        if (leftType.isEmptySequence() || rightType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }

        SequenceType intOpt = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.OneOrZero);
        if (!leftType.isSubtypeOf(intOpt) || !rightType.isSubtypeOf(intOpt)) {
            throwStaticTypeException(
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
        SequenceType leftType = ((Expression) children.get(0)).getStaticSequenceType();
        SequenceType rightType = ((Expression) children.get(1)).getStaticSequenceType();

        if (leftType == null || rightType == null) {
            throw new OurBadException(
                    "A child expression of a ConcatExpression has no inferred type", expression.getMetadata());
        }

        if ((!leftType.isEmptySequence() && !TypeAtomization.isAtomicOrNode(leftType.getItemType()))
                || (!rightType.isEmptySequence() && !TypeAtomization.isAtomicOrNode(rightType.getItemType()))
                || resolveArities(leftType, rightType) == null) {
            throwStaticTypeException(
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

        SequenceType mainType = expression.getMainExpression().getStaticSequenceType();
        SequenceType lookupType = expression.getLookupExpression().getStaticSequenceType();

        if (mainType == null || lookupType == null) {
            throw new OurBadException(
                    "A child expression of a ArrayLookupExpression has no inferred type", expression.getMetadata());
        }

        if (!lookupType.isSubtypeOf(SequenceType.createSequenceType("integer"))) {
            throwStaticTypeException(
                    "the lookup expression type must match integer, instead " + lookupType + " was inferred",
                    expression.getMetadata());
        }

        if (!mainType.hasOverlapWith(SequenceType.createSequenceType("array*")) || mainType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }

        SequenceType.Arity inferredArity = mainType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.ZeroOrMore;
        ItemType resultItemType = BuiltinTypesCatalogue.item;
        ItemType itemType = mainType.getItemType();
        ItemType arrayContentType = inferArrayContentType(itemType);
        if (arrayContentType != null) {
            resultItemType = arrayContentType;
        }
        expression.setStaticSequenceType(new SequenceType(resultItemType, inferredArity));
        return argument;
    }

    @Override
    public StaticContext visitObjectLookupExpression(ObjectLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType mainType = expression.getMainExpression().getStaticSequenceType();
        SequenceType lookupType = expression.getLookupExpression().getStaticSequenceType();

        if (mainType == null || lookupType == null) {
            throw new OurBadException(
                    "A child expression of a ObjectLookupExpression has no inferred type", expression.getMetadata());
        }

        // must be castable to string
        if (!lookupType.isSubtypeOf(SequenceType.createSequenceType("anyAtomicType"))) {
            throwStaticTypeException(
                    "the lookup expression type must be castable to string (i.e. must match atomic), instead "
                            + lookupType
                            + " was inferred",
                    expression.getMetadata());
        }

        boolean overlapsObject = mainType.hasOverlapWith(SequenceType.createSequenceType("object*"));
        boolean overlapsMap = mainType.hasOverlapWith(SequenceType.createSequenceType("map*"));
        if ((!overlapsObject && !overlapsMap) || mainType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }

        String key =
                expression.getLookupExpression() instanceof StringLiteralExpression literal ? literal.getValue() : null;
        SequenceType result = inferObjectLookupType(mainType.getItemType(), mainType.getArity(), key);
        if (result.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }
        expression.setStaticSequenceType(result);
        return argument;
    }

    /**
     * Infers JSONiq dot selection separately for each possible input type.
     * For example, selecting n from ({n: integer} | null) yields integer?:
     * the object branch yields an integer, while the null branch yields the empty sequence.
     * A null key here denotes an expression whose string value is not known statically.
     */
    private SequenceType inferObjectLookupType(ItemType itemType, SequenceType.Arity inputArity, String key) {
        if (itemType.isUnionType()) {
            SequenceType result = null;
            for (ItemType member : itemType.getTypes()) {
                SequenceType memberResult = inferObjectLookupType(member, inputArity, key);
                // Include empty branches: they make a required field's lookup result optional.
                result = result == null ? memberResult : result.leastCommonSupertypeWith(memberResult);
            }
            return result == null ? SequenceType.createSequenceType("()") : result;
        }
        if (itemType.isMapItemType()) {
            return itemType.getMapValueSequenceType();
        }
        SequenceType.Arity outputArity = inputArity.isSubtypeOf(SequenceType.Arity.OneOrZero)
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.ZeroOrMore;
        if (itemType.isObjectItemType() && key != null) {
            FieldDescriptor field = itemType.getObjectContentFacet(key);
            if (field != null) {
                return new SequenceType(field.getType(), field.isRequired() ? inputArity : outputArity);
            }
            if (itemType.getClosedFacet()) {
                return SequenceType.createSequenceType("()");
            }
        }
        SequenceType type = new SequenceType(itemType, inputArity);
        if (!type.hasOverlapWith(SequenceType.createSequenceType("object*"))
                && !type.hasOverlapWith(SequenceType.createSequenceType("map*"))) {
            // JSONiq object selection ignores nonobjects, including null.
            return SequenceType.createSequenceType("()");
        }
        return new SequenceType(BuiltinTypesCatalogue.item, outputArity);
    }

    /**
     * Combines content types of possible arrays for JSONiq lookup and unboxing.
     * Nonarray alternatives contribute no results. Returns Java null when no array is possible;
     * a broad type that could contain arrays instead contributes item.
     */
    private ItemType inferArrayContentType(ItemType itemType) {
        if (itemType.isUnionType()) {
            ItemType result = null;
            for (ItemType member : itemType.getTypes()) {
                ItemType memberContent = inferArrayContentType(member);
                if (memberContent != null) {
                    result = result == null ? memberContent : result.findLeastCommonSuperTypeWith(memberContent);
                }
            }
            return result;
        }
        if (itemType.isArrayItemType()) {
            return itemType.getArrayContentFacet();
        }
        return BuiltinTypesCatalogue.arrayItem.isSubtypeOf(itemType) ? BuiltinTypesCatalogue.item : null;
    }

    @Override
    public StaticContext visitPostfixLookupExpression(PostfixLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType mainType = expression.getMainExpression().getStaticSequenceType();
        // no need to check lookupexpression and it might be null if wildcard

        if (mainType == null) {
            throw new OurBadException(
                    "A child expression of a ObjectLookupExpression has no inferred type", expression.getMetadata());
        }

        boolean overlapsObject = mainType.hasOverlapWith(SequenceType.createSequenceType("object*"));
        boolean overlapsMap = mainType.hasOverlapWith(SequenceType.createSequenceType("map*"));
        if ((!overlapsObject && !overlapsMap) || mainType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }

        SequenceType.Arity inferredArity = mainType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.ZeroOrMore;

        ItemType inferredType = BuiltinTypesCatalogue.item;
        if (mainType.getItemType().isMapItemType()) {
            SequenceType mapValueType = mainType.getItemType().getMapValueSequenceType();
            inferredType = mapValueType.getItemType();
            inferredArity = mapValueType.getArity();
        }

        expression.setStaticSequenceType(new SequenceType(inferredType, inferredArity));
        return argument;
    }

    @Override
    public StaticContext visitUnaryLookupExpression(UnaryLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setStaticSequenceType(SequenceType.createSequenceType("item*"));
        return argument;
    }

    @Override
    public StaticContext visitArrayUnboxingExpression(ArrayUnboxingExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType mainType = expression.getMainExpression().getStaticSequenceType();

        if (mainType == null) {
            throw new OurBadException(
                    "A child expression of a ArrayUnboxingExpression has no inferred type", expression.getMetadata());
        }

        if (!mainType.hasOverlapWith(SequenceType.createSequenceType("array*")) || mainType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }
        ItemType contentType = inferArrayContentType(mainType.getItemType());
        if (contentType != null) {
            SequenceType sequenceType = new SequenceType(contentType, SequenceType.Arity.ZeroOrMore);
            expression.setStaticSequenceType(sequenceType);
            return argument;
        }
        expression.setStaticSequenceType(SequenceType.createSequenceType("item*"));
        return argument;
    }

    @Override
    public StaticContext visitFilterExpression(FilterExpression expression, StaticContext argument) {
        visit(expression.getMainExpression(), argument);
        SequenceType mainType = expression.getMainExpression().getStaticSequenceType();
        basicChecks(mainType, expression.getClass().getSimpleName(), true, true, expression.getMetadata());

        Expression predicateExpression = expression.getPredicateExpression();
        // set context item static type
        predicateExpression.getStaticContext().setContextItemStaticType(new SequenceType(mainType.getItemType()));
        visit(predicateExpression, argument);
        SequenceType predicateType = predicateExpression.getStaticSequenceType();
        // unset context item static type
        predicateExpression.getStaticContext().setContextItemStaticType(null);

        basicChecks(predicateType, expression.getClass().getSimpleName(), true, true, expression.getMetadata());
        // always false so the return type is for sure ()
        if (predicateType.isSubtypeOf(SequenceType.createSequenceType("null?"))) {
            throwStaticTypeException(
                    "Inferred type for FilterExpression is empty sequence (with active static typing feature, only allowed for CommaExpression)",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }
        if (!predicateType.hasEffectiveBooleanValue()) {
            throwStaticTypeException(
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
    public boolean checkArguments(List<SequenceType> expectedTypes, List<SequenceType> types) {
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

        SequenceType mainType = expression.getMainExpression().getStaticSequenceType();
        basicChecks(mainType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());
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
            throwStaticTypeException(
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
        List<SequenceType> partialFormalParameterTypes = new ArrayList<>();
        boolean isPartialApplication = false;
        int i = 0;
        for (Expression e : expression.getArguments()) {
            if (e == null) {
                isPartialApplication = true;
                partialFormalParameterTypes.add(formalParameterTypes.get(i));
            }
            if (e != null) {
                actualParameterTypes.add(e.getStaticSequenceType());
            }
            ++i;
        }
        if (isPartialApplication) {
            FunctionSignature newSignature = new FunctionSignature(
                    partialFormalParameterTypes, signature.getReturnType(), expression.isUpdating());
            return new SequenceType(ItemTypeFactory.createFunctionItemType(newSignature));
        }
        if (!checkArguments(formalParameterTypes, actualParameterTypes)) {
            throwStaticTypeException(
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
        SequenceType leftType = leftExpression.getStaticSequenceType();
        basicChecks(leftType, expression.getClass().getSimpleName(), true, true, expression.getMetadata());

        // set context item static type
        rightExpression.getStaticContext().setContextItemStaticType(new SequenceType(leftType.getItemType()));
        visit(rightExpression, argument);
        rightExpression.getStaticContext().setContextItemStaticType(null);

        SequenceType rightType = rightExpression.getStaticSequenceType();
        basicChecks(rightType, expression.getClass().getSimpleName(), true, true, expression.getMetadata());

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
            try {
                this.visit(clause, clause.getStaticContext());
            } catch (UnexpectedStaticTypeException e) {
                if (forCardinality == SequenceCardinality.EMPTY
                        && clause.getClauseType().equals(FLWOR_CLAUSES.WHERE)) {
                    clause = clause.getNextClause();
                    continue;
                }
                throw e;
            }
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
        SequenceType returnType = expression.getReturnClause().getReturnExpr().getStaticSequenceType();
        basicChecks(returnType, expression.getClass().getSimpleName(), true, true, expression.getMetadata());
        returnType = new SequenceType(
                returnType.getItemType(), returnType.getCardinality().repeated(forCardinality));
        expression.setStaticSequenceType(returnType);
        return argument;
    }

    @Override
    public StaticContext visitForClause(ForClause expression, StaticContext argument) {
        expression.getExpression().accept(this, argument);
        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType = SequenceType.createSequenceType("item*");
        if (declaredType == null) {
            inferredType = expression.getExpression().getStaticSequenceType();
        } else {
            inferredType = declaredType;
        }

        basicChecks(inferredType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());
        if (inferredType.isEmptySequence()) {
            if (!expression.isAllowEmpty()) {
                // for sure we will not have any tuple to process and return the empty sequence
                throwStaticTypeException(
                        "In for clause Inferred type is empty sequence, empty is not allowed, so the result returned is for sure () and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                        expression.getMetadata());
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

        checkAndUpdateVariableStaticType(
                declaredType,
                inferredType,
                expression.getNextClause().getStaticContext(),
                expression.getClass().getSimpleName(),
                expression.getVariableName(),
                expression.getMetadata());
        return argument;
    }

    @Override
    public StaticContext visitWindowClause(WindowClause expression, StaticContext argument) {
        visit(expression.getExpression(), argument);
        SequenceType sourceType = expression.getActualSequenceType() == null
                ? expression.getExpression().getStaticSequenceType()
                : expression.getActualSequenceType();
        basicChecks(sourceType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());
        // Condition variables bind items of the input, while a declared type applies to the window variable.
        ItemType inputItemType =
                expression.getExpression().getStaticSequenceType().getItemType();
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
        SequenceType conditionType = condition.getStaticSequenceType();
        basicChecks(conditionType, clause.getClass().getSimpleName(), true, false, clause.getMetadata());
        if (!conditionType.hasEffectiveBooleanValue()) {
            throwStaticTypeException("Window condition has no effective boolean value", clause.getMetadata());
        }
    }

    @Override
    public StaticContext visitLetClause(LetClause expression, StaticContext argument) {
        visit(expression.getExpression(), argument);
        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType = (declaredType == null
                        ? expression.getExpression()
                        : ((TreatExpression) expression.getExpression()).getMainExpression())
                .getStaticSequenceType();
        checkAndUpdateVariableStaticType(
                declaredType,
                inferredType,
                expression.getNextClause().getStaticContext(),
                expression.getClass().getSimpleName(),
                expression.getVariableName(),
                expression.getMetadata());
        expression.setStaticType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitWhereClause(WhereClause expression, StaticContext argument) {
        visit(expression.getWhereExpression(), argument);
        SequenceType whereType = expression.getWhereExpression().getStaticSequenceType();
        basicChecks(whereType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());
        if (!whereType.hasEffectiveBooleanValue()) {
            throwStaticTypeException(
                    "where clause inferred type (" + whereType + ") has no effective boolean value",
                    expression.getMetadata());
        }
        if (whereType.isEmptySequence() || whereType.isSubtypeOf(SequenceType.createSequenceType("null?"))) {
            throwStaticTypeException(
                    "where clause always return false, so return expression inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    expression.getMetadata());
        }
        return argument;
    }

    @Override
    public StaticContext visitGroupByClause(GroupByClause expression, StaticContext argument) {
        Clause nextClause = expression.getNextClause(); // != null because group by cannot be last clause of FLOWR
        // expression
        Set<Name> groupingVars = new HashSet<>();
        for (GroupByVariableDeclaration groupByVar : expression.getGroupVariables()) {
            // if we are grouping by an existing var (i.e. expr is null), then the appropriate type is already inferred
            Expression groupByVarExpr = groupByVar.getExpression();
            SequenceType expectedType;
            if (groupByVarExpr != null) {
                visit(groupByVarExpr, groupByVarExpr.getStaticContext());
                SequenceType declaredType = groupByVar.getActualSequenceType();
                SequenceType inferredType;
                if (declaredType == null) {
                    inferredType = groupByVarExpr.getStaticSequenceType();
                    expectedType = inferredType;
                } else {
                    inferredType = groupByVarExpr.getStaticSequenceType();
                    expectedType = declaredType;
                }
                checkAndUpdateVariableStaticType(
                        declaredType,
                        inferredType,
                        nextClause.getStaticContext(),
                        expression.getClass().getSimpleName(),
                        groupByVar.getVariableName(),
                        expression.getMetadata());
            } else {
                expectedType = expression.getStaticContext().getVariableSequenceType(groupByVar.getVariableName());
            }
            // check that expectedType is a subtype of atomic?
            if (expectedType.isSubtypeOf(SequenceType.createSequenceType("json-item*"))) {
                throwStaticTypeException(
                        "group by variable "
                                + groupByVar.getVariableName()
                                + " must match atomic? instead found "
                                + expectedType,
                        ErrorCode.NonAtomicElementErrorCode,
                        expression.getMetadata());
            }
            if (!expectedType.isSubtypeOf(SequenceType.createSequenceType("anyAtomicType?"))) {
                throwStaticTypeException(
                        "group by variable "
                                + groupByVar.getVariableName()
                                + " must match atomic? instead found "
                                + expectedType,
                        expression.getMetadata());
            }
            groupingVars.add(groupByVar.getVariableName());
        }

        // finally if there was a for clause we need to change the arity of the variables bound so far in the flwor
        // expression, from ? to * and from 1 to +
        // excluding the grouping variables
        StaticContext nextClauseStaticContext = expression.getNextClause().getStaticContext();
        nextClause.getStaticContext().incrementArities(nextClauseStaticContext, groupingVars);
        return argument;
    }

    @Override
    public StaticContext visitOrderByClause(OrderByClause expression, StaticContext argument) {
        for (OrderByClauseSortingKey orderClause : expression.getSortingKeys()) {
            visit(orderClause.getExpression(), argument);
            SequenceType orderType = orderClause.getExpression().getStaticSequenceType();
            basicChecks(orderType, expression.getClass().getSimpleName(), true, false, expression.getMetadata());
            if (orderType.isSubtypeOf(SequenceType.createSequenceType("json-item*"))) {
                throwStaticTypeException(
                        "order by sorting expression's type must match atomic? and be comparable using 'gt' operator (so duration, hexBinary, base64Binary and atomic item type are not allowed), instead inferred: "
                                + orderType,
                        ErrorCode.NonAtomicElementErrorCode,
                        expression.getMetadata());
            }
            if (!orderType.isSubtypeOf(SequenceType.createSequenceType("anyAtomicType?"))
                    || orderType.getItemType().equals(BuiltinTypesCatalogue.atomicItem)
                    || orderType.getItemType().equals(BuiltinTypesCatalogue.durationItem)
                    || orderType.getItemType().equals(BuiltinTypesCatalogue.hexBinaryItem)
                    || orderType.getItemType().equals(BuiltinTypesCatalogue.base64BinaryItem)) {
                throwStaticTypeException(
                        "order by sorting expression's type must match atomic? and be comparable using 'gt' operator (so duration, hexBinary, base64Binary and atomic item type are not allowed), instead inferred: "
                                + orderType,
                        expression.getMetadata());
            }
        }

        return argument;
    }

    @Override
    public StaticContext visitCountClause(CountClause clause, StaticContext argument) {
        checkAndUpdateVariableStaticType(
                null,
                SequenceType.createSequenceType("integer"),
                clause.getNextClause().getStaticContext(),
                clause.getClass().getSimpleName(),
                clause.getCountVariableName(),
                clause.getMetadata());
        return argument;
    }

    // endregion

    // region module

    // if [declaredType] is not null, check if the inferred type matches or can be promoted to the declared type
    // (otherwise throw type error)
    // if [declaredType] is null, replace the type of [variableName] in the [context] with the inferred type
    public void checkAndUpdateVariableStaticType(
            SequenceType declaredType,
            SequenceType inferredType,
            StaticContext context,
            String nodeName,
            Name variableName,
            ExceptionMetadata metadata) {
        basicChecks(inferredType, nodeName, true, false, metadata);

        if (declaredType == null) {
            // if declared type is null, we overwrite the type in the correspondent InScopeVariable with the inferred
            // type
            context.replaceVariableSequenceType(variableName, inferredType);
        } else {
            if (!inferredType.isSubtypeOf(declaredType)) {
                throwStaticTypeException(
                        "In a "
                                + nodeName
                                + ", the variable $"
                                + variableName
                                + " inferred type "
                                + inferredType
                                + " does not match or can be promoted to the declared type "
                                + declaredType,
                        metadata);
            }
        }
    }

    @Override
    public StaticContext visitVariableDeclaration(VariableDeclaration expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType = SequenceType.createSequenceType("item*");
        if (declaredType == null) {
            if (expression.getExpression() != null) {
                if (argument.getIsAssignable(expression.getVariableName())) {
                    inferredType = SequenceType.createSequenceType("item*");
                } else {
                    inferredType = expression.getExpression().getStaticSequenceType();
                }
            }
        } else {
            inferredType = declaredType;
        }
        checkAndUpdateVariableStaticType(
                declaredType,
                inferredType,
                argument,
                expression.getClass().getSimpleName(),
                expression.getVariableName(),
                expression.getMetadata());

        return argument;
    }

    @Override
    public StaticContext visitFunctionDeclaration(FunctionDeclaration expression, StaticContext argument) {
        visitDescendants(expression, argument);
        InlineFunctionExpression inlineExpression = (InlineFunctionExpression) expression.getExpression();
        SequenceType inferredType = inlineExpression.getBody().getExpression().getStaticSequenceType();
        SequenceType expectedType = inlineExpression.getActualReturnType();

        if (expectedType == null) {
            expectedType = inferredType;
        } else if (!inferredType.isSubtypeOfOrCanBePromotedTo(expectedType)) {
            throwStaticTypeException(
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
                expression.getMainExpression().getStaticSequenceType().getItemType();
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
    // // begin scripting
    private SequenceType getSequenceTypeFromChildren(
            SequenceType inferredType, SequenceType childSequenceType, ExceptionMetadata childMetadata) {
        // if a child expression has no inferred type throw an error
        if (childSequenceType == null) {
            throwStaticTypeException("A child expression of a BlockStatement has no inferred type", childMetadata);
            return inferredType;
        }

        return inferredType.concatenateWith(childSequenceType);
    }

    @Override
    public StaticContext visitBlockStatement(BlockStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);

        SequenceType inferredType = SequenceType.createSequenceType("()");

        for (Statement childStatement : statement.getBlockStatements()) {
            inferredType = getSequenceTypeFromChildren(
                    inferredType, childStatement.getStaticSequenceType(), childStatement.getMetadata());
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
            throw new UnexpectedStaticTypeException(
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
        SequenceType returnType =
                statement.getReturnStatementClause().getReturnStatement().getStaticSequenceType();
        basicChecks(returnType, statement.getClass().getSimpleName(), true, true, statement.getMetadata());
        statement.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitConditionalStatement(ConditionalStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);

        SequenceType ifType = statement.getCondition().getStaticSequenceType();
        SequenceType thenType = statement.getBranch().getStaticSequenceType();
        SequenceType elseType = statement.getElseBranch().getStaticSequenceType();

        if (ifType == null || thenType == null || elseType == null) {
            throw new OurBadException(
                    "A child expression of a ConditionalStatement has no inferred type", statement.getMetadata());
        }

        if (!ifType.hasEffectiveBooleanValue()) {
            throwStaticTypeException(
                    "The condition in the 'if' must have effective boolean value, found inferred type: "
                            + ifType
                            + " (which has not effective boolean value)",
                    statement.getMetadata());
        }

        // if the if branch is false at static time (i.e. subtype of null?) we only use else branch
        SequenceType resultingType = ifType.isSubtypeOf(SequenceType.createSequenceType("null?"))
                ? elseType
                : thenType.leastCommonSupertypeWith(elseType);

        if (resultingType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    statement.getMetadata());
        }

        statement.setStaticSequenceType(resultingType);
        return argument;
    }

    // TODO: Refactor where code can be reused
    @Override
    public StaticContext visitSwitchStatement(SwitchStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        SequenceType testType = statement.getTestCondition().getStaticSequenceType();
        checkSwitchType(testType, statement.getMetadata());

        SequenceType returnType = statement.getDefaultStatement().getStaticSequenceType();
        if (returnType == null) {
            throw new OurBadException(
                    "A child statement of a SwitchExpression has no inferred type", statement.getMetadata());
        }

        for (SwitchCaseStatement switchCase : statement.getCases()) {
            boolean addToReturnType =
                    checkSwitchType(switchCase.getConditionExpressions(), testType, statement.getMetadata());
            SequenceType caseReturnType = switchCase.getReturnStatement().getStaticSequenceType();
            if (caseReturnType == null) {
                throw new OurBadException(
                        "A child statement of a SwitchStatement has no inferred type", statement.getMetadata());
            }
            if (addToReturnType) {
                returnType = returnType.leastCommonSupertypeWith(caseReturnType);
            }
        }

        statement.setStaticSequenceType(returnType);
        return argument;
    }

    @Override
    public StaticContext visitTryCatchStatement(TryCatchStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        SequenceType inferredType = null;

        for (Node childNode : statement.getChildren()) {
            SequenceType childType = ((BlockStatement) childNode).getStaticSequenceType();
            if (childType == null) {
                throw new OurBadException(
                        "A child statement of a TryCatchStatement has no inferred type", statement.getMetadata());
            }

            if (inferredType == null) {
                inferredType = childType;
            } else {
                inferredType = inferredType.leastCommonSupertypeWith(childType);
            }
        }
        inferredType = requireInferredType(inferredType, statement.getClass().getSimpleName());

        if (inferredType.isEmptySequence()) {
            throwStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression,
                    statement.getMetadata());
        }
        statement.setStaticSequenceType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitTypeSwitchStatement(TypeSwitchStatement statement, StaticContext argument) {
        visit(statement.getTestCondition(), argument);
        SequenceType inferredType = null;

        SequenceType conditionType = statement.getTestCondition().getStaticSequenceType();
        basicChecks(conditionType, statement.getClass().getSimpleName(), true, false, statement.getMetadata());

        for (TypeSwitchStatementCase typeswitchCase : statement.getCases()) {
            Name variableName = typeswitchCase.getVariableName();
            Statement returnStatement = typeswitchCase.getReturnStatement();
            // if we bind a variable we add the static type of it in the context of the return expression
            if (variableName != null) {
                SequenceType variableType = null;
                for (SequenceType st : typeswitchCase.getUnion()) {
                    variableType = variableType == null ? st : variableType.leastCommonSupertypeWith(st);
                }
                returnStatement.getStaticContext().replaceVariableSequenceType(variableName, variableType);
            }

            visit(returnStatement, argument);
            SequenceType caseType = returnStatement.getStaticSequenceType();
            basicChecks(caseType, statement.getClass().getSimpleName(), true, false, statement.getMetadata());
            inferredType = inferredType == null ? caseType : inferredType.leastCommonSupertypeWith(caseType);
        }

        Name variableName = statement.getDefaultCase().getVariableName();
        Statement returnStatement = statement.getDefaultCase().getReturnStatement();
        // if we bind a variable in the default case, we infer testCondition type
        if (variableName != null) {
            returnStatement.getStaticContext().replaceVariableSequenceType(variableName, conditionType);
        }
        visit(returnStatement, argument);
        SequenceType defaultType = returnStatement.getStaticSequenceType();
        basicChecks(defaultType, statement.getClass().getSimpleName(), true, false, statement.getMetadata());
        inferredType = inferredType == null ? defaultType : inferredType.leastCommonSupertypeWith(defaultType);

        basicChecks(inferredType, statement.getClass().getSimpleName(), false, true, statement.getMetadata());
        statement.setStaticSequenceType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitVariableDeclStatement(VariableDeclStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        SequenceType declaredType = statement.getActualSequenceType();
        SequenceType inferredType = getInferredSequenceType(statement, declaredType);
        if (declaredType == null) {
            if (!statement.isAssignable()) {
                // Non-assignable typ
                checkAndUpdateVariableStaticType(
                        null,
                        inferredType,
                        statement.getStaticContext(),
                        statement.getClass().getSimpleName(),
                        statement.getVariableName(),
                        statement.getMetadata());
            } else {
                // Assignable variables without a declared type are have Item* type.
                checkAndUpdateVariableStaticType(
                        null,
                        SequenceType.createSequenceType("item*"),
                        statement.getStaticContext(),
                        statement.getClass().getSimpleName(),
                        statement.getVariableName(),
                        statement.getMetadata());
            }
        }
        statement.setStaticSequenceType(
                statement.getStaticContext().getVariableSequenceType(statement.getVariableName()));
        return argument;
    }

    private static SequenceType getInferredSequenceType(VariableDeclStatement statement, SequenceType declaredType) {
        SequenceType inferredType = SequenceType.createSequenceType("item*");
        if (declaredType == null) {
            if (statement.getVariableExpression() != null) {
                inferredType = statement.getVariableExpression().getStaticSequenceType();
            }
        } else {
            if (statement.getVariableExpression() != null) {
                inferredType = statement.getVariableExpression().getStaticSequenceType();
            } else {
                inferredType = declaredType;
            }
        }
        return inferredType;
    }

    @Override
    public StaticContext visitCommaVariableDeclStatement(CommaVariableDeclStatement statement, StaticContext argument) {
        visitDescendants(statement, argument);
        statement.setStaticSequenceType(SequenceType.createSequenceType("()"));
        return argument;
    }

    @Override
    public StaticContext visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr statementsAndOptionalExpr, StaticContext argument) {
        SequenceType inferredType = SequenceType.createSequenceType("()");
        visitDescendants(statementsAndOptionalExpr, argument);
        if (statementsAndOptionalExpr.getExpression() != null) {
            inferredType = getSequenceTypeFromChildren(
                    inferredType,
                    statementsAndOptionalExpr.getExpression().getStaticSequenceType(),
                    statementsAndOptionalExpr.getExpression().getMetadata());
        }
        statementsAndOptionalExpr.setStaticSequenceType(inferredType);
        return argument;
    }

    @Override
    public StaticContext visitStatementsAndExpr(StatementsAndExpr statementsAndExpr, StaticContext argument) {
        visitDescendants(statementsAndExpr, argument);
        SequenceType inferredType = getSequenceTypeFromChildren(
                SequenceType.createSequenceType("()"),
                statementsAndExpr.getExpression().getStaticSequenceType(),
                statementsAndExpr.getExpression().getMetadata());

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
        SequenceType leftType =
                requireInferredType(slashExpr.getLeftExpression().getStaticSequenceType(), "SlashExpr");
        basicChecks(leftType, slashExpr.getClass().getSimpleName(), true, false, slashExpr.getMetadata());

        Expression rightExpression = slashExpr.getRightExpression();
        rightExpression.getStaticContext().setContextItemStaticType(new SequenceType(leftType.getItemType()));
        try {
            visit(rightExpression, argument);
        } finally {
            rightExpression.getStaticContext().setContextItemStaticType(null);
        }
        SequenceType rightType = requireInferredType(rightExpression.getStaticSequenceType(), "SlashExpr");
        basicChecks(rightType, slashExpr.getClass().getSimpleName(), true, false, slashExpr.getMetadata());

        // XPath removes duplicate nodes, so multiple inputs need not yield multiple results.
        SequenceType.Arity resultingArity = leftType.getArity().multiplyWith(rightType.getArity());
        slashExpr.setStaticSequenceType(new SequenceType(rightType.getItemType(), resultingArity));

        // E//S abbreviates E/descendant-or-self::node()/S, so S applies to E and to each of its descendants.
        if (slashExpr.getLeftExpression() instanceof SlashExpr left
                && left.getRightExpression() instanceof ForwardStepExpr descendantOrSelf
                && descendantOrSelf.getForwardAxis().equals(ForwardAxis.DESCENDANT_OR_SELF)
                && descendantOrSelf.getNodeTest() instanceof AnyKindTest
                && rightExpression instanceof StepExpr step) {
            inferSchemaStepType(step, left.getLeftExpression().getStaticSequenceType(), true)
                    .ifPresent(type -> {
                        slashExpr.setStaticSequenceType(type);
                        // As for other steps, it is an error if no descendant can match the step.
                        basicChecks(type, step.getClass().getSimpleName(), true, true, step.getMetadata());
                    });
        }
        return argument;
    }

    @Override
    public StaticContext visitStepExpr(StepExpr stepExpr, StaticContext argument) {
        SequenceType contextType = stepExpr.getStaticContext().getContextItemStaticType();
        SequenceType inferredType;
        if (contextType != null
                && contextType.getItemType().isNodeItemType()
                && isStaticallyEmptyStep(stepExpr, contextType.getItemType())) {
            inferredType = SequenceType.createSequenceType("()");
        } else {
            inferredType = inferSchemaStepType(stepExpr, contextType)
                    .orElseGet(() -> new SequenceType(
                            inferStepResultItemType(stepExpr), inferStepResultArity(stepExpr, contextType)));
        }

        stepExpr.setStaticSequenceType(inferredType);
        basicChecks(inferredType, stepExpr.getClass().getSimpleName(), true, true, stepExpr.getMetadata());
        return argument;
    }

    private Optional<SequenceType> inferSchemaStepType(StepExpr stepExpr, SequenceType contextType) {
        return inferSchemaStepType(stepExpr, contextType, false);
    }

    /**
     * Child, attribute, and descendant steps from a schema-typed element or document select the nodes its schema
     * declares. With fromDescendants, a child or attribute step also applies to each descendant of the context.
     */
    private Optional<SequenceType> inferSchemaStepType(
            StepExpr stepExpr, SequenceType contextType, boolean fromDescendants) {
        if (contextType == null
                || !XmlSchemaCatalog.isSchemaTyped(contextType.getItemType())
                || !(stepExpr instanceof ForwardStepExpr forwardStep)) {
            return Optional.empty();
        }
        ForwardAxis axis = forwardStep.getForwardAxis();
        boolean attributeAxis = axis.equals(ForwardAxis.ATTRIBUTE);
        // descendant::N selects the N children of the context and of each of its descendants.
        boolean descendants = fromDescendants || axis.equals(ForwardAxis.DESCENDANT);
        if (!attributeAxis && !axis.equals(ForwardAxis.CHILD) && !axis.equals(ForwardAxis.DESCENDANT)) {
            return Optional.empty();
        }
        NodeTest nodeTest = stepExpr.getNodeTest();
        Name name;
        if (nodeTest instanceof NameTest nameTest && (nameTest.hasQName() || nameTest.hasWildcardOnly())) {
            name = nameTest.hasQName() ? nameTest.getExpandedName() : null;
        } else if (!attributeAxis && nodeTest instanceof ElementTest elementTest) {
            if (elementTest.isNameWithoutTypeCheck()) {
                name = elementTest.getElementName();
            } else if (elementTest.isEmptyCheck() || elementTest.isWildcardOnly()) {
                name = null;
            } else {
                return Optional.empty();
            }
        } else if (attributeAxis
                && nodeTest instanceof AttributeTest attributeTest
                && attributeTest.isNameWithoutTypeCheck()) {
            name = attributeTest.getAttributeName();
        } else {
            return Optional.empty();
        }
        XmlSchemaCatalog catalog =
                stepExpr.getStaticContext().getInScopeSchemaTypes().getXmlSchemaCatalog();
        Optional<SequenceType> stepType = descendants
                ? catalog.getDescendantStepType(contextType.getItemType(), attributeAxis, name)
                : catalog.getStepType(contextType.getItemType(), attributeAxis, name);
        return stepType.map(type ->
                new SequenceType(type.getItemType(), type.getCardinality().repeated(contextType.getCardinality())));
    }

    private SequenceType.Arity inferStepResultArity(StepExpr stepExpr, SequenceType contextType) {
        if (contextType == null || !contextType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)) {
            return SequenceType.Arity.ZeroOrMore;
        }
        if ((stepExpr instanceof ForwardStepExpr forwardStep
                        && forwardStep.getForwardAxis().equals(ForwardAxis.SELF))
                || (stepExpr instanceof ReverseStepExpr reverseStep
                        && reverseStep.getReverseAxis().equals(ReverseAxis.PARENT))) {
            return SequenceType.Arity.OneOrZero;
        }
        return SequenceType.Arity.ZeroOrMore;
    }

    private ItemType inferStepResultItemType(StepExpr stepExpr) {
        NodeTest nodeTest = stepExpr.getNodeTest();
        if (nodeTest instanceof SchemaNodeTest schemaTest) {
            return schemaTest.itemType();
        }
        if (nodeTest instanceof AnyKindTest) {
            return BuiltinTypesCatalogue.nodeItem;
        }
        if (nodeTest instanceof TextTest) {
            return BuiltinTypesCatalogue.textNode;
        }
        if (nodeTest instanceof CommentTest) {
            return BuiltinTypesCatalogue.commentNode;
        }
        if (nodeTest instanceof NamespaceNodeTest) {
            return BuiltinTypesCatalogue.namespaceNode;
        }
        if (nodeTest instanceof PITest piTest) {
            return piTest.hasTargetName()
                    ? ItemTypeFactory.processingInstructionNodeItemType(piTest.getTargetName())
                    : BuiltinTypesCatalogue.processingInstructionNode;
        }
        if (nodeTest instanceof DocumentTest documentTest) {
            if (documentTest.isEmptyCheck()) {
                return BuiltinTypesCatalogue.documentNode;
            }
            NodeTest innerTest = documentTest.getNodeTest();
            if (innerTest instanceof ElementTest elementTest && elementTest.isNameWithoutTypeCheck()) {
                return ItemTypeFactory.documentNodeItemType(
                        ItemTypeFactory.elementNodeItemType(elementTest.getElementName()));
            }
            return BuiltinTypesCatalogue.documentNode;
        }
        if (nodeTest instanceof AttributeTest attributeTest) {
            return attributeTest.isNameWithoutTypeCheck()
                    ? ItemTypeFactory.attributeNodeItemType(attributeTest.getAttributeName())
                    : BuiltinTypesCatalogue.attributeNode;
        }
        if (nodeTest instanceof ElementTest elementTest) {
            return elementTest.isNameWithoutTypeCheck()
                    ? ItemTypeFactory.elementNodeItemType(elementTest.getElementName())
                    : BuiltinTypesCatalogue.elementNode;
        }
        if (nodeTest instanceof NameTest nameTest) {
            boolean attributePrincipalKind = stepExpr instanceof ForwardStepExpr forwardStep
                    && forwardStep.getForwardAxis().equals(ForwardAxis.ATTRIBUTE);
            if (attributePrincipalKind) {
                return nameTest.hasQName()
                        ? ItemTypeFactory.attributeNodeItemType(nameTest.getExpandedName())
                        : BuiltinTypesCatalogue.attributeNode;
            }
            return nameTest.hasQName()
                    ? ItemTypeFactory.elementNodeItemType(nameTest.getExpandedName())
                    : BuiltinTypesCatalogue.elementNode;
        }
        return BuiltinTypesCatalogue.nodeItem;
    }

    private static boolean hasNoChildren(ItemType nodeType) {
        return nodeType.isSubtypeOf(BuiltinTypesCatalogue.attributeNode)
                || nodeType.isSubtypeOf(BuiltinTypesCatalogue.textNode)
                || nodeType.isSubtypeOf(BuiltinTypesCatalogue.commentNode)
                || nodeType.isSubtypeOf(BuiltinTypesCatalogue.namespaceNode)
                || nodeType.isSubtypeOf(BuiltinTypesCatalogue.processingInstructionNode);
    }

    private boolean isStaticallyEmptyStep(StepExpr stepExpr, ItemType contextItemType) {
        if (stepExpr instanceof ForwardStepExpr forwardStep) {
            ForwardAxis axis = forwardStep.getForwardAxis();
            if (axis.equals(ForwardAxis.ATTRIBUTE)) {
                // Only elements have attributes, but a context such as node() may still be one.
                return hasNoChildren(contextItemType)
                        || contextItemType.isSubtypeOf(BuiltinTypesCatalogue.documentNode);
            }
            if (axis.equals(ForwardAxis.SELF)) {
                return !nodeTestCanMatchContextNode(stepExpr.getNodeTest(), contextItemType, axis);
            }
            if (axis.equals(ForwardAxis.CHILD)
                    || axis.equals(ForwardAxis.DESCENDANT)
                    || axis.equals(ForwardAxis.DESCENDANT_OR_SELF)) {
                boolean hasNoDescendants = hasNoChildren(contextItemType);
                if (axis.equals(ForwardAxis.DESCENDANT_OR_SELF)) {
                    return hasNoDescendants
                            && !nodeTestCanMatchContextNode(stepExpr.getNodeTest(), contextItemType, axis);
                }
                return hasNoDescendants;
            }
            return false;
        }
        if (stepExpr instanceof ReverseStepExpr reverseStep) {
            ReverseAxis axis = reverseStep.getReverseAxis();
            if (axis.equals(ReverseAxis.PARENT)) {
                return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.documentNode);
            }
            if (axis.equals(ReverseAxis.ANCESTOR_OR_SELF)) {
                return !nodeTestCanMatchContextNode(stepExpr.getNodeTest(), contextItemType, null);
            }
        }
        return false;
    }

    private boolean nodeTestCanMatchContextNode(NodeTest nodeTest, ItemType contextItemType, ForwardAxis axis) {
        if (contextItemType instanceof SchemaElementNodeItemType schemaType) {
            return schemaType.getAlternatives().stream()
                    .anyMatch(type -> nodeTestCanMatchContextNode(nodeTest, type, axis));
        }
        if (nodeTest instanceof AnyKindTest) {
            return true;
        }
        if (nodeTest instanceof TextTest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.textNode);
        }
        if (nodeTest instanceof CommentTest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.commentNode);
        }
        if (nodeTest instanceof NamespaceNodeTest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.namespaceNode);
        }
        if (nodeTest instanceof PITest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.processingInstructionNode);
        }
        if (nodeTest instanceof DocumentTest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.documentNode);
        }
        if (nodeTest instanceof AttributeTest attributeTest) {
            if (!contextItemType.isSubtypeOf(BuiltinTypesCatalogue.attributeNode)) {
                return false;
            }
            if (!attributeTest.isNameWithoutTypeCheck()) {
                return true;
            }
            if (contextItemType instanceof AttributeNodeItemType namedAttributeType) {
                return attributeTest.getAttributeName().equals(namedAttributeType.getNodeName());
            }
            return true;
        }
        if (nodeTest instanceof ElementTest elementTest) {
            if (!contextItemType.isSubtypeOf(BuiltinTypesCatalogue.elementNode)) {
                return false;
            }
            if (!elementTest.isNameWithoutTypeCheck()) {
                return true;
            }
            if (contextItemType instanceof ElementNodeItemType namedElementType) {
                return elementTest.getElementName().equals(namedElementType.getNodeName());
            }
            return true;
        }
        if (nodeTest instanceof NameTest nameTest) {
            boolean attributePrincipalKind = axis != null && axis.equals(ForwardAxis.ATTRIBUTE);
            if (attributePrincipalKind) {
                if (!contextItemType.isSubtypeOf(BuiltinTypesCatalogue.attributeNode)) {
                    return false;
                }
                if (!nameTest.hasQName()) {
                    return true;
                }
                if (contextItemType instanceof AttributeNodeItemType namedAttributeType) {
                    return nameTest.getExpandedName().equals(namedAttributeType.getNodeName());
                }
                return true;
            }
            if (!contextItemType.isSubtypeOf(BuiltinTypesCatalogue.elementNode)) {
                return false;
            }
            if (!nameTest.hasQName()) {
                return true;
            }
            if (contextItemType instanceof ElementNodeItemType namedElementType) {
                return nameTest.getExpandedName().equals(namedElementType.getNodeName());
            }
            return true;
        }
        return true;
    }

    // end xml
}
