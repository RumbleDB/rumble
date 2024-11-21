// Generated from ./src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.9.3

// Java header
package org.rumbledb.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JsoniqParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JsoniqVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#moduleAndThisIsIt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleAndThisIsIt(JsoniqParser.ModuleAndThisIsItContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(JsoniqParser.ModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#mainModule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainModule(JsoniqParser.MainModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#libraryModule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryModule(JsoniqParser.LibraryModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#prolog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProlog(JsoniqParser.PrologContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(JsoniqParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatements(JsoniqParser.StatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#statementsAndExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementsAndExpr(JsoniqParser.StatementsAndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#statementsAndOptionalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementsAndOptionalExpr(JsoniqParser.StatementsAndOptionalExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(JsoniqParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#applyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApplyStatement(JsoniqParser.ApplyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#assignStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStatement(JsoniqParser.AssignStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(JsoniqParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(JsoniqParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(JsoniqParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#exitStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExitStatement(JsoniqParser.ExitStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#flowrStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowrStatement(JsoniqParser.FlowrStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(JsoniqParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#switchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchStatement(JsoniqParser.SwitchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#switchCaseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCaseStatement(JsoniqParser.SwitchCaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatchStatement(JsoniqParser.TryCatchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#catchCaseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchCaseStatement(JsoniqParser.CatchCaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#typeSwitchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSwitchStatement(JsoniqParser.TypeSwitchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#caseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseStatement(JsoniqParser.CaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(JsoniqParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#annotations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotations(JsoniqParser.AnnotationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#varDeclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclStatement(JsoniqParser.VarDeclStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#varDeclForStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclForStatement(JsoniqParser.VarDeclForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(JsoniqParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#setter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetter(JsoniqParser.SetterContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#namespaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceDecl(JsoniqParser.NamespaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#annotatedDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotatedDecl(JsoniqParser.AnnotatedDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#defaultCollationDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultCollationDecl(JsoniqParser.DefaultCollationDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#orderingModeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderingModeDecl(JsoniqParser.OrderingModeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#emptyOrderDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyOrderDecl(JsoniqParser.EmptyOrderDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#decimalFormatDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalFormatDecl(JsoniqParser.DecimalFormatDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#qname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQname(JsoniqParser.QnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#dfPropertyName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDfPropertyName(JsoniqParser.DfPropertyNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#moduleImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleImport(JsoniqParser.ModuleImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(JsoniqParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#contextItemDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextItemDecl(JsoniqParser.ContextItemDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(JsoniqParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#typeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDecl(JsoniqParser.TypeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#schemaLanguage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaLanguage(JsoniqParser.SchemaLanguageContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(JsoniqParser.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(JsoniqParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(JsoniqParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#exprSingle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSingle(JsoniqParser.ExprSingleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#exprSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSimple(JsoniqParser.ExprSimpleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#flowrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowrExpr(JsoniqParser.FlowrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#forClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForClause(JsoniqParser.ForClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#forVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForVar(JsoniqParser.ForVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#letClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetClause(JsoniqParser.LetClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#letVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetVar(JsoniqParser.LetVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(JsoniqParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClause(JsoniqParser.GroupByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#groupByVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByVar(JsoniqParser.GroupByVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(JsoniqParser.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#orderByExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByExpr(JsoniqParser.OrderByExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#countClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCountClause(JsoniqParser.CountClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#quantifiedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifiedExpr(JsoniqParser.QuantifiedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#quantifiedExprVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifiedExprVar(JsoniqParser.QuantifiedExprVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#switchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchExpr(JsoniqParser.SwitchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#switchCaseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCaseClause(JsoniqParser.SwitchCaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#typeSwitchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSwitchExpr(JsoniqParser.TypeSwitchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#caseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseClause(JsoniqParser.CaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#ifExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpr(JsoniqParser.IfExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#tryCatchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatchExpr(JsoniqParser.TryCatchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#catchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchClause(JsoniqParser.CatchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(JsoniqParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(JsoniqParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#notExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(JsoniqParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpr(JsoniqParser.ComparisonExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#stringConcatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#rangeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeExpr(JsoniqParser.RangeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#additiveExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(JsoniqParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#instanceOfExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#isStaticallyExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsStaticallyExpr(JsoniqParser.IsStaticallyExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#treatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTreatExpr(JsoniqParser.TreatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#castableExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastableExpr(JsoniqParser.CastableExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#castExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpr(JsoniqParser.CastExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#arrowExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowExpr(JsoniqParser.ArrowExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#arrowFunctionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowFunctionSpecifier(JsoniqParser.ArrowFunctionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#unaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(JsoniqParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#valueExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExpr(JsoniqParser.ValueExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#validateExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidateExpr(JsoniqParser.ValidateExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#annotateExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotateExpr(JsoniqParser.AnnotateExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#simpleMapExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleMapExpr(JsoniqParser.SimpleMapExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#postFixExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostFixExpr(JsoniqParser.PostFixExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#arrayLookup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLookup(JsoniqParser.ArrayLookupContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#arrayUnboxing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayUnboxing(JsoniqParser.ArrayUnboxingContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(JsoniqParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#objectLookup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectLookup(JsoniqParser.ObjectLookupContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(JsoniqParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#blockExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockExpr(JsoniqParser.BlockExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#varRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarRef(JsoniqParser.VarRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#parenthesizedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpr(JsoniqParser.ParenthesizedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#contextItemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#orderedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderedExpr(JsoniqParser.OrderedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#unorderedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnorderedExpr(JsoniqParser.UnorderedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(JsoniqParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(JsoniqParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(JsoniqParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#functionItemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionItemExpr(JsoniqParser.FunctionItemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#namedFunctionRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedFunctionRef(JsoniqParser.NamedFunctionRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#inlineFunctionExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#insertExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertExpr(JsoniqParser.InsertExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#deleteExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteExpr(JsoniqParser.DeleteExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#renameExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameExpr(JsoniqParser.RenameExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#replaceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceExpr(JsoniqParser.ReplaceExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#transformExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformExpr(JsoniqParser.TransformExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#appendExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAppendExpr(JsoniqParser.AppendExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#updateLocator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateLocator(JsoniqParser.UpdateLocatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#copyDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopyDecl(JsoniqParser.CopyDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#pathExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathExpr(JsoniqParser.PathExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#relativePathExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelativePathExpr(JsoniqParser.RelativePathExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#stepExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStepExpr(JsoniqParser.StepExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#axisStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAxisStep(JsoniqParser.AxisStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#forwardStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForwardStep(JsoniqParser.ForwardStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#forwardAxis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForwardAxis(JsoniqParser.ForwardAxisContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#abbrevForwardStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbbrevForwardStep(JsoniqParser.AbbrevForwardStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#reverseStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverseStep(JsoniqParser.ReverseStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#reverseAxis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverseAxis(JsoniqParser.ReverseAxisContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#abbrevReverseStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbbrevReverseStep(JsoniqParser.AbbrevReverseStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#nodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeTest(JsoniqParser.NodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#nameTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNameTest(JsoniqParser.NameTestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allNames}
	 * labeled alternative in {@link JsoniqParser#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllNames(JsoniqParser.AllNamesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allWithNS}
	 * labeled alternative in {@link JsoniqParser#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllWithNS(JsoniqParser.AllWithNSContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allWithLocal}
	 * labeled alternative in {@link JsoniqParser#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllWithLocal(JsoniqParser.AllWithLocalContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#nCNameWithLocalWildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNCNameWithLocalWildcard(JsoniqParser.NCNameWithLocalWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#nCNameWithPrefixWildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNCNameWithPrefixWildcard(JsoniqParser.NCNameWithPrefixWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#predicateList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateList(JsoniqParser.PredicateListContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#kindTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKindTest(JsoniqParser.KindTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#anyKindTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyKindTest(JsoniqParser.AnyKindTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#binaryNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryNodeTest(JsoniqParser.BinaryNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#documentTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocumentTest(JsoniqParser.DocumentTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#textTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextTest(JsoniqParser.TextTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#commentTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentTest(JsoniqParser.CommentTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#namespaceNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceNodeTest(JsoniqParser.NamespaceNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#piTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPiTest(JsoniqParser.PiTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#attributeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeTest(JsoniqParser.AttributeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#attributeNameOrWildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeNameOrWildcard(JsoniqParser.AttributeNameOrWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#schemaAttributeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaAttributeTest(JsoniqParser.SchemaAttributeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#attributeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeDeclaration(JsoniqParser.AttributeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#elementTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementTest(JsoniqParser.ElementTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#elementNameOrWildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementNameOrWildcard(JsoniqParser.ElementNameOrWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#schemaElementTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaElementTest(JsoniqParser.SchemaElementTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#elementDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementDeclaration(JsoniqParser.ElementDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#attributeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeName(JsoniqParser.AttributeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#elementName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementName(JsoniqParser.ElementNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#simpleTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTypeName(JsoniqParser.SimpleTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(JsoniqParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#sequenceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceType(JsoniqParser.SequenceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#objectConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectConstructor(JsoniqParser.ObjectConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#itemType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItemType(JsoniqParser.ItemTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#functionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionTest(JsoniqParser.FunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#anyFunctionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyFunctionTest(JsoniqParser.AnyFunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#typedFunctionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedFunctionTest(JsoniqParser.TypedFunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#singleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleType(JsoniqParser.SingleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#pairConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairConstructor(JsoniqParser.PairConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#arrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#uriLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUriLiteral(JsoniqParser.UriLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(JsoniqParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsoniqParser#keyWords}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyWords(JsoniqParser.KeyWordsContext ctx);
}