package org.rumbledb.compiler;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TryCatchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
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
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
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
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.SwitchCaseStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TryCatchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;
import org.rumbledb.expressions.scripting.declaration.CommaVariableDeclStatement;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.IsStaticallyExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.typing.ValidateTypeExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CloneVisitor extends AbstractNodeVisitor<Node> {
    @Override
    protected Node defaultAction(Node node, Node argument) {
        return node;
    }

    @Override
    public Node visitMainModule(MainModule module, Node argument) {
        MainModule result = new MainModule(
                (Prolog) visit(module.getProlog(), module.getProlog()),
                (Program) visit(module.getProgram(), argument),
                module.getMetadata()
        );
        result.setStaticContext(module.getStaticContext());
        return result;
    }

    @Override
    public Node visitLibraryModule(LibraryModule module, Node argument) {
        LibraryModule result = new LibraryModule(
                (Prolog) visit(module.getProlog(), module.getProlog()),
                module.getNamespace(),
                module.getMetadata()
        );
        result.setStaticContext(module.getStaticContext());
        return result;
    }

    @Override
    public Node visitProlog(Prolog expression, Node argument) {
        List<LibraryModule> libraryModules = expression.getImportedModules()
            .stream()
            .map(libraryModule -> (LibraryModule) visit(libraryModule, argument))
            .collect(Collectors.toList());
        List<Node> declarations = expression.getFunctionDeclarations()
            .stream()
            .map(expr -> visit(expr, argument))
            .collect(Collectors.toList());
        declarations.addAll(expression.getVariableDeclarations());
        declarations.addAll(expression.getTypeDeclarations());
        expression.setDeclarations(declarations);
        expression.getImportedModules().clear();
        expression.getImportedModules().addAll(libraryModules);
        return expression;
    }

    @Override
    public Node visitProgram(Program program, Node argument) {
        StatementsAndOptionalExpr statementsAndOptionalExpr = (StatementsAndOptionalExpr) visit(
            program.getStatementsAndOptionalExpr(),
            argument
        );
        return new Program(statementsAndOptionalExpr, statementsAndOptionalExpr.getMetadata());
    }

    @Override
    public Node visitStatementsAndOptionalExpr(StatementsAndOptionalExpr statementsAndOptionalExpr, Node argument) {
        List<Statement> statements = new ArrayList<>();
        statementsAndOptionalExpr.getStatements().forEach(statement -> {
            statements.add((Statement) visit(statement, argument));
        });
        Expression optionalExpr = null;
        if (statementsAndOptionalExpr.getExpression() != null) {
            optionalExpr = (Expression) visit(statementsAndOptionalExpr.getExpression(), argument);
            optionalExpr.setStaticContext(statementsAndOptionalExpr.getExpression().getStaticContext());
            optionalExpr.setStaticSequenceType(statementsAndOptionalExpr.getExpression().getStaticSequenceType());
        }
        StatementsAndOptionalExpr result = new StatementsAndOptionalExpr(
                statements,
                optionalExpr,
                statementsAndOptionalExpr.getMetadata()
        );
        result.setStaticContext(statementsAndOptionalExpr.getStaticContext());
        result.setStaticSequenceType(statementsAndOptionalExpr.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitStatementsAndExpr(StatementsAndExpr statementsAndExpr, Node argument) {
        List<Statement> statements = new ArrayList<>();
        statementsAndExpr.getStatements().forEach(statement -> {
            statements.add((Statement) visit(statement, argument));
        });
        Expression expression = (Expression) visit(statementsAndExpr.getExpression(), argument);
        expression.setStaticContext(statementsAndExpr.getExpression().getStaticContext());
        expression.setStaticSequenceType(statementsAndExpr.getExpression().getStaticSequenceType());
        StatementsAndExpr result = new StatementsAndExpr(
                statements,
                expression,
                statementsAndExpr.getMetadata()
        );
        result.setStaticContext(statementsAndExpr.getStaticContext());
        result.setStaticSequenceType(statementsAndExpr.getStaticSequenceType());
        return result;
    }


    @Override
    public Node visitCommaExpression(CommaExpression expression, Node argument) {
        List<Expression> children = new ArrayList<>();
        for (Expression child : expression.getExpressions()) {
            children.add((Expression) visit(child, argument));
        }
        CommaExpression result = new CommaExpression(children, expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    // region flwor
    @Override
    public Node visitFlowrExpression(FlworExpression expression, Node argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        Clause result = null;
        while (clause != null) {
            Clause temp = (Clause) this.visit(clause, argument);
            if (result != null) {
                result.chainWith(temp);
            }
            result = temp;
            clause = clause.getNextClause();
        }
        Expression resultingExpression = new FlworExpression((ReturnClause) result, expression.getMetadata());
        resultingExpression.setStaticContext(expression.getStaticContext());
        resultingExpression.setStaticSequenceType(expression.getStaticSequenceType());
        return resultingExpression;
    }

    public Node visitVariableReference(VariableReferenceExpression expression, Node argument) {
        VariableReferenceExpression result = new VariableReferenceExpression(
                expression.getVariableName(),
                expression.getMetadata()
        );
        result.setActualType(expression.getActualType());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitForClause(ForClause clause, Node argument) {
        Clause result = new ForClause(
                clause.getVariableName(),
                clause.isAllowEmpty(),
                clause.getActualSequenceType(),
                clause.getPositionalVariableName(),
                (Expression) visit(clause.getExpression(), argument),
                clause.getMetadata()
        );
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitLetClause(LetClause clause, Node argument) {
        LetClause result = new LetClause(
                clause.getVariableName(),
                clause.getActualSequenceType(),
                (Expression) visit(clause.getExpression(), argument),
                clause.getMetadata()
        );
        result.setStaticType(clause.getStaticType());
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitGroupByClause(GroupByClause clause, Node argument) {
        List<GroupByVariableDeclaration> groupByVariableDeclarations = new ArrayList<>();
        for (GroupByVariableDeclaration variable : clause.getGroupVariables()) {
            groupByVariableDeclarations.add(
                new GroupByVariableDeclaration(
                        variable.getVariableName(),
                        variable.getActualSequenceType(),
                        (variable.getExpression() == null)
                            ? variable.getExpression()
                            : (Expression) visit(variable.getExpression(), argument)
                )
            );
        }
        Clause result = new GroupByClause(groupByVariableDeclarations, clause.getMetadata());
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitOrderByClause(OrderByClause clause, Node argument) {
        List<OrderByClauseSortingKey> groupByVariableDeclarations = new ArrayList<>();
        for (OrderByClauseSortingKey orderByClauseSortingKey : clause.getSortingKeys()) {
            groupByVariableDeclarations.add(
                new OrderByClauseSortingKey(
                        (Expression) visit(orderByClauseSortingKey.getExpression(), argument),
                        orderByClauseSortingKey.isAscending(),
                        orderByClauseSortingKey.getUri(),
                        orderByClauseSortingKey.getEmptyOrder()
                )
            );

        }
        Clause result = new OrderByClause(groupByVariableDeclarations, clause.isStable(), clause.getMetadata());
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitCountClause(CountClause expression, Node argument) {
        Clause result = new CountClause(
                expression.getCountVariableName(),
                expression.getMetadata()
        );
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitWhereClause(WhereClause clause, Node argument) {
        Clause result = new WhereClause(
                (Expression) visit(clause.getWhereExpression(), argument),
                clause.getMetadata()
        );
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitReturnClause(ReturnClause clause, Node argument) {
        Clause result = new ReturnClause((Expression) visit(clause.getReturnExpr(), argument), clause.getMetadata());
        result.setStaticContext(clause.getStaticContext());
        return result;
    }
    // endregion

    // region postfix
    @Override
    public Node visitArrayUnboxingExpression(ArrayUnboxingExpression expression, Node argument) {
        ArrayUnboxingExpression result = new ArrayUnboxingExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitArrayLookupExpression(ArrayLookupExpression expression, Node argument) {
        ArrayLookupExpression result = new ArrayLookupExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                (Expression) visit(expression.getLookupExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitObjectLookupExpression(ObjectLookupExpression expression, Node argument) {
        ObjectLookupExpression result = new ObjectLookupExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                (Expression) visit(expression.getLookupExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitXQueryLookupExpression(XQueryLookupExpression expression, Node argument) {
        XQueryLookupExpression result = new XQueryLookupExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                (Expression) visit(expression.getLookupExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitFilterExpression(FilterExpression expression, Node argument) {
        FilterExpression result = new FilterExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                (Expression) visit(expression.getPredicateExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitDynamicFunctionCallExpression(DynamicFunctionCallExpression expression, Node argument) {
        DynamicFunctionCallExpression result = new DynamicFunctionCallExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getArguments(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }
    // endregion

    // region primary
    public Node visitArrayConstructor(ArrayConstructorExpression expression, Node argument) {
        ArrayConstructorExpression result = new ArrayConstructorExpression(
                (expression.getExpression() == null)
                    ? expression.getExpression()
                    : (Expression) visit(expression.getExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitObjectConstructor(ObjectConstructorExpression expression, Node argument) {
        if (expression.isMergedConstructor()) {
            Expression result = new ObjectConstructorExpression(
                    (Expression) visit(expression.getChildren().get(0), argument),
                    expression.getMetadata()
            );
            result.setStaticContext(expression.getStaticContext());
            result.setStaticSequenceType(expression.getStaticSequenceType());
            return result;
        } else {
            List<Expression> keys = expression.getKeys()
                .stream()
                .map(key -> (Expression) visit(key, argument))
                .collect(Collectors.toList());
            List<Expression> values = expression.getValues()
                .stream()
                .map(key -> (Expression) visit(key, argument))
                .collect(Collectors.toList());
            Expression result = new ObjectConstructorExpression(keys, values, expression.getMetadata());
            result.setStaticContext(expression.getStaticContext());
            result.setStaticSequenceType(expression.getStaticSequenceType());
            return result;
        }
    }

    @Override
    public Node visitContextExpr(ContextItemExpression expression, Node argument) {
        Expression result = new ContextItemExpression(expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        List<Expression> arguments = expression.getArguments()
            .stream()
            .map(expr -> expr != null ? (Expression) visit(expr, argument) : null)
            .collect(Collectors.toList());
        Expression result = new FunctionCallExpression(
                expression.getFunctionName(),
                arguments,
                expression.getMetadata()
        );
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitInlineFunctionExpr(InlineFunctionExpression expression, Node argument) {
        InlineFunctionExpression result = new InlineFunctionExpression(
                expression.getAnnotations(),
                expression.getName(),
                expression.getParams(),
                expression.getActualReturnType(),
                (StatementsAndOptionalExpr) visit(expression.getBody(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        result.setSequential(expression.isSequential());
        return result;
    }

    public Node visitNamedFunctionRef(NamedFunctionReferenceExpression expression, Node argument) {
        Expression result = new NamedFunctionReferenceExpression(expression.getIdentifier(), expression.getMetadata());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }
    // endregion

    // region literal
    public Node visitInteger(IntegerLiteralExpression expression, Node argument) {
        Expression result = new IntegerLiteralExpression(expression.getLexicalValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitString(StringLiteralExpression expression, Node argument) {
        Expression result = new StringLiteralExpression(expression.getValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitDouble(DoubleLiteralExpression expression, Node argument) {
        Expression result = new DoubleLiteralExpression(expression.getValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitDecimal(DecimalLiteralExpression expression, Node argument) {
        Expression result = new DecimalLiteralExpression(expression.getValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitNull(NullLiteralExpression expression, Node argument) {
        Expression result = new NullLiteralExpression(expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitBoolean(BooleanLiteralExpression expression, Node argument) {
        Expression result = new BooleanLiteralExpression(expression.getValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }
    // endregion

    // region operational
    @Override
    public Node visitAdditiveExpr(AdditiveExpression expression, Node argument) {
        AdditiveExpression result = new AdditiveExpression(
                (Expression) visit(expression.getLeftExpression(), argument),
                (Expression) visit(expression.getRightExpression(), argument),
                expression.isMinus(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitMultiplicativeExpr(MultiplicativeExpression expression, Node argument) {
        MultiplicativeExpression result = new MultiplicativeExpression(
                (Expression) visit(expression.getLeftExpression(), argument),
                (Expression) visit(expression.getRightExpression(), argument),
                expression.getMultiplicativeOperator(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitSimpleMapExpr(SimpleMapExpression expression, Node argument) {
        SimpleMapExpression result = new SimpleMapExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitAndExpr(AndExpression expression, Node argument) {
        AndExpression result = new AndExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitOrExpr(OrExpression expression, Node argument) {
        OrExpression result = new OrExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitNotExpr(NotExpression expression, Node argument) {
        NotExpression result = new NotExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitUnaryExpr(UnaryExpression expression, Node argument) {
        UnaryExpression result = new UnaryExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.isNegated(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitRangeExpr(RangeExpression expression, Node argument) {
        RangeExpression result = new RangeExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitStringConcatExpr(StringConcatExpression expression, Node argument) {
        StringConcatExpression result = new StringConcatExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitComparisonExpr(ComparisonExpression expression, Node argument) {
        ComparisonExpression result = new ComparisonExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getComparisonOperator(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitInstanceOfExpression(InstanceOfExpression expression, Node argument) {
        InstanceOfExpression result = new InstanceOfExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitIsStaticallyExpr(IsStaticallyExpression expression, Node argument) {
        IsStaticallyExpression result = new IsStaticallyExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitTreatExpression(TreatExpression expression, Node argument) {
        TreatExpression result = new TreatExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.errorCodeThatShouldBeThrown(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        result.setSequential(expression.isSequential());
        return result;
    }

    @Override
    public Node visitCastableExpression(CastableExpression expression, Node argument) {
        CastableExpression result = new CastableExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitCastExpression(CastExpression expression, Node argument) {
        CastExpression result = new CastExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }
    // endregion

    // region control
    @Override
    public Node visitConditionalExpression(ConditionalExpression expression, Node argument) {
        ConditionalExpression result = new ConditionalExpression(
                (Expression) visit(expression.getCondition(), argument),
                (Expression) visit(expression.getBranch(), argument),
                (Expression) visit(expression.getElseBranch(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitSwitchExpression(SwitchExpression expression, Node argument) {
        List<SwitchCase> resultCases = new ArrayList<>();
        for (SwitchCase switchCase : expression.getCases()) {
            List<Expression> children = new ArrayList<>();
            for (Expression child : switchCase.getConditionExpressions()) {
                children.add((Expression) visit(child, argument));
            }
            resultCases.add(new SwitchCase(children, (Expression) visit(switchCase.getReturnExpression(), argument)));
        }
        SwitchExpression result = new SwitchExpression(
                (Expression) visit(expression.getTestCondition(), argument),
                resultCases,
                (Expression) visit(expression.getDefaultExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitTypeSwitchExpression(TypeSwitchExpression expression, Node argument) {
        List<TypeswitchCase> resultCases = new ArrayList<>();
        for (TypeswitchCase switchCase : expression.getCases()) {
            if (switchCase.getUnion() == null) {
                resultCases.add(
                    new TypeswitchCase(
                            switchCase.getVariableName(),
                            (Expression) visit(switchCase.getReturnExpression(), argument)
                    )
                );
            } else {
                resultCases.add(
                    new TypeswitchCase(
                            switchCase.getVariableName(),
                            switchCase.getUnion(),
                            (Expression) visit(switchCase.getReturnExpression(), argument)
                    )
                );
            }
        }
        TypeswitchCase defaultCase = (expression.getDefaultCase().getUnion() == null)
            ? new TypeswitchCase(
                    expression.getDefaultCase().getVariableName(),
                    (Expression) visit(expression.getDefaultCase().getReturnExpression(), argument)
            )
            : new TypeswitchCase(
                    expression.getDefaultCase().getVariableName(),
                    expression.getDefaultCase().getUnion(),
                    (Expression) visit(expression.getDefaultCase().getReturnExpression(), argument)
            );

        TypeSwitchExpression result = new TypeSwitchExpression(
                (Expression) visit(expression.getTestCondition(), argument),
                resultCases,
                defaultCase,
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitTryCatchExpression(TryCatchExpression expression, Node argument) {
        Map<String, Expression> catchExpressions = new HashMap<>();
        for (String key : expression.getCatchExpressions().keySet()) {
            catchExpressions.put(key, (Expression) visit(expression.getCatchExpressions().get(key), argument));
        }
        TryCatchExpression result = new TryCatchExpression(
                (Expression) visit(expression.getTryExpression(), argument),
                catchExpressions,
                (expression.getExpressionCatchingAll() == null)
                    ? expression.getExpressionCatchingAll()
                    : (Expression) visit(expression.getExpressionCatchingAll(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }
    // endregion

    // region prolog
    @Override
    public Node visitVariableDeclaration(VariableDeclaration expression, Node argument) {
        return new VariableDeclaration(
                expression.getVariableName(),
                expression.external(),
                expression.getActualSequenceType(),
                (Expression) visit(expression.getExpression(), argument),
                expression.getAnnotations(),
                expression.getMetadata()
        );
    }

    @Override
    public Node visitFunctionDeclaration(FunctionDeclaration expression, Node argument) {
        return new FunctionDeclaration(
                (InlineFunctionExpression) visit(expression.getExpression(), argument),
                expression.getMetadata()
        );
    }

    public Node visitTypeDeclaration(TypeDeclaration expression, Node argument) {
        return new TypeDeclaration(expression.getDefinition(), expression.getMetadata());
    }

    @Override
    public Node visitValidateTypeExpression(ValidateTypeExpression expression, Node argument) {
        Expression result = new ValidateTypeExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.isValidate(),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    // region scripting
    @Override
    public Node visitApplyStatement(ApplyStatement statement, Node argument) {
        Expression exprSingle = (Expression) visit(statement.getApplyExpression(), argument);
        ApplyStatement result = new ApplyStatement(exprSingle, statement.getMetadata());
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }

    @Override
    public Node visitAssignStatement(AssignStatement statement, Node argument) {
        Expression exprSingle = (Expression) visit(statement.getAssignExpression(), argument);
        AssignStatement result = new AssignStatement(exprSingle, statement.getName(), statement.getMetadata());
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }

    @Override
    public Node visitBlockStatement(BlockStatement statement, Node argument) {
        List<Statement> statements = new ArrayList<>();
        statement.getBlockStatements().forEach(stmt -> {
            statements.add((Statement) visit(stmt, argument));
        });
        BlockStatement result = new BlockStatement(statements, statement.getMetadata());
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }

    @Override
    public Node visitExitStatement(ExitStatement statement, Node argument) {
        Expression exprSingle = (Expression) visit(statement.getExitExpression(), argument);
        ExitStatement result = new ExitStatement(exprSingle, statement.getMetadata());
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }

    @Override
    public Node visitFlowrStatement(FlowrStatement statement, Node argument) {
        Clause flowrClause = statement.getReturnStatementClause().getFirstClause();
        Clause newClause = null;
        while (flowrClause != null) {
            Clause temp = (Clause) this.visit(flowrClause, argument);
            if (newClause != null) {
                newClause.chainWith(temp);
            }
            newClause = temp;
            flowrClause = flowrClause.getNextClause();
        }
        FlowrStatement resultStatement = new FlowrStatement((ReturnStatementClause) newClause, statement.getMetadata());
        resultStatement.setStaticContext(statement.getStaticContext());
        resultStatement.setStaticSequenceType(statement.getStaticSequenceType());
        resultStatement.setSequential(statement.isSequential());
        return resultStatement;
    }

    @Override
    public Node visitReturnStatementClause(ReturnStatementClause clause, Node argument) {
        Clause result = new ReturnStatementClause(
                (Statement) visit(clause.getReturnStatement(), argument),
                clause.getMetadata()
        );
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitTryCatchStatement(TryCatchStatement statement, Node argument) {
        Map<String, BlockStatement> catchStatements = new HashMap<>();
        statement.getCatchStatements()
            .forEach((key, value) -> catchStatements.put(key, (BlockStatement) visit(value, argument)));
        TryCatchStatement result = new TryCatchStatement(
                (BlockStatement) visit(statement.getTryStatement(), argument),
                catchStatements,
                (statement.getCatchAllStatement() == null)
                    ? statement.getCatchAllStatement()
                    : (BlockStatement) visit(statement.getCatchAllStatement(), argument),
                statement.getMetadata()
        );
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }

    @Override
    public Node visitSwitchStatement(SwitchStatement statement, Node argument) {
        List<SwitchCaseStatement> resultCases = new ArrayList<>();
        statement.getCases().forEach(swcCase -> {
            List<Expression> children = new ArrayList<>();
            swcCase.getConditionExpressions().forEach(expr -> {
                children.add((Expression) visit(expr, argument));
            });
            resultCases.add(
                new SwitchCaseStatement(children, (Statement) visit(swcCase.getReturnStatement(), argument))
            );
        });
        SwitchStatement result = new SwitchStatement(
                (Expression) visit(statement.getTestCondition(), argument),
                resultCases,
                (Statement) visit(statement.getDefaultStatement(), argument),
                statement.getMetadata()
        );
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }

    @Override
    public Node visitTypeSwitchStatement(TypeSwitchStatement statement, Node argument) {
        List<TypeSwitchStatementCase> resultCases = new ArrayList<>();
        statement.getCases().forEach(twsCase -> {
            if (twsCase.getUnion() == null) {
                resultCases.add(
                    new TypeSwitchStatementCase(
                            twsCase.getVariableName(),
                            (Statement) visit(twsCase.getReturnStatement(), argument)
                    )
                );
            } else {
                resultCases.add(
                    new TypeSwitchStatementCase(
                            twsCase.getVariableName(),
                            twsCase.getUnion(),
                            (Statement) visit(twsCase.getReturnStatement(), argument)
                    )
                );
            }
        });
        TypeSwitchStatementCase defaultCase = (statement.getDefaultCase().getUnion() == null)
            ? new TypeSwitchStatementCase(
                    statement.getDefaultCase().getVariableName(),
                    (Statement) visit(statement.getDefaultCase().getReturnStatement(), argument)
            )
            : new TypeSwitchStatementCase(
                    statement.getDefaultCase().getVariableName(),
                    statement.getDefaultCase().getUnion(),
                    (Statement) visit(statement.getDefaultCase().getReturnStatement(), argument)
            );

        TypeSwitchStatement result = new TypeSwitchStatement(
                (Expression) visit(statement.getTestCondition(), argument),
                resultCases,
                defaultCase,
                statement.getMetadata()
        );
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }

    @Override
    public Node visitWhileStatement(WhileStatement statement, Node argument) {
        Expression testExpr = (Expression) visit(statement.getTestCondition(), argument);
        Statement body = (Statement) visit(statement.getStatement(), argument);
        WhileStatement result = new WhileStatement(testExpr, body, statement.getMetadata());
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }


    @Override
    public Node visitCommaVariableDeclStatement(CommaVariableDeclStatement statement, Node argument) {
        List<VariableDeclStatement> variables = new ArrayList<>();
        statement.getVariables().forEach(variable -> {
            variables.add((VariableDeclStatement) visit(variable, argument));
        });
        CommaVariableDeclStatement result = new CommaVariableDeclStatement(variables, statement.getMetadata());
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getStaticSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }

    @Override
    public Node visitVariableDeclStatement(VariableDeclStatement statement, Node argument) {
        if (statement.getVariableExpression() != null) {
            VariableDeclStatement result = new VariableDeclStatement(
                    statement.getAnnotations(),
                    statement.getVariableName(),
                    statement.getActualSequenceType(),
                    (Expression) visit(statement.getVariableExpression(), argument),
                    statement.getMetadata()
            );
            result.setSequential(statement.isSequential());
            result.setStaticContext(statement.getStaticContext());
            result.setStaticSequenceType(statement.getSequenceType());
            return result;
        }
        VariableDeclStatement result = new VariableDeclStatement(
                statement.getAnnotations(),
                statement.getVariableName(),
                statement.getActualSequenceType(),
                null,
                statement.getMetadata()
        );
        result.setStaticContext(statement.getStaticContext());
        result.setStaticSequenceType(statement.getSequenceType());
        result.setSequential(statement.isSequential());
        return result;
    }
    // end region scripting
}
