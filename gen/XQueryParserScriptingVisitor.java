// Generated from /Users/dbuzatu/ETH/Thesis/rumble/src/main/java/org/rumbledb/parser/XQueryParserScripting.g4 by ANTLR 4.13.1

// Java header
package org.rumbledb.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XQueryParserScripting}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface XQueryParserScriptingVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(XQueryParserScripting.ModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#xqDocComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXqDocComment(XQueryParserScripting.XqDocCommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#versionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersionDecl(XQueryParserScripting.VersionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mainModule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainModule(XQueryParserScripting.MainModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(XQueryParserScripting.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#libraryModule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryModule(XQueryParserScripting.LibraryModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#moduleDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleDecl(XQueryParserScripting.ModuleDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#prolog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProlog(XQueryParserScripting.PrologContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#annotatedDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotatedDecl(XQueryParserScripting.AnnotatedDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#defaultNamespaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultNamespaceDecl(XQueryParserScripting.DefaultNamespaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#setter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetter(XQueryParserScripting.SetterContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#boundarySpaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundarySpaceDecl(XQueryParserScripting.BoundarySpaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#defaultCollationDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultCollationDecl(XQueryParserScripting.DefaultCollationDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#baseURIDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseURIDecl(XQueryParserScripting.BaseURIDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#constructionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructionDecl(XQueryParserScripting.ConstructionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#orderingModeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderingModeDecl(XQueryParserScripting.OrderingModeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#emptyOrderDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyOrderDecl(XQueryParserScripting.EmptyOrderDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#copyNamespacesDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopyNamespacesDecl(XQueryParserScripting.CopyNamespacesDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#preserveMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreserveMode(XQueryParserScripting.PreserveModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#inheritMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInheritMode(XQueryParserScripting.InheritModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#decimalFormatDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalFormatDecl(XQueryParserScripting.DecimalFormatDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#schemaImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaImport(XQueryParserScripting.SchemaImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#schemaPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaPrefix(XQueryParserScripting.SchemaPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#moduleImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleImport(XQueryParserScripting.ModuleImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#namespaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceDecl(XQueryParserScripting.NamespaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(XQueryParserScripting.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#varValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarValue(XQueryParserScripting.VarValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#varDefaultValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDefaultValue(XQueryParserScripting.VarDefaultValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#contextItemDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextItemDecl(XQueryParserScripting.ContextItemDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(XQueryParserScripting.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParams(XQueryParserScripting.FunctionParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParam(XQueryParserScripting.FunctionParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#annotations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotations(XQueryParserScripting.AnnotationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(XQueryParserScripting.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#annotList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotList(XQueryParserScripting.AnnotListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#annotationParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationParam(XQueryParserScripting.AnnotationParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionReturn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReturn(XQueryParserScripting.FunctionReturnContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#optionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionDecl(XQueryParserScripting.OptionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatements(XQueryParserScripting.StatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#statementsAndExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementsAndExpr(XQueryParserScripting.StatementsAndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#statementsAndOptionalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementsAndOptionalExpr(XQueryParserScripting.StatementsAndOptionalExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(XQueryParserScripting.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#exprSingle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSingle(XQueryParserScripting.ExprSingleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#exprSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSimple(XQueryParserScripting.ExprSimpleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#blockExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockExpr(XQueryParserScripting.BlockExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#flworExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlworExpr(XQueryParserScripting.FlworExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#initialClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitialClause(XQueryParserScripting.InitialClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#intermediateClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntermediateClause(XQueryParserScripting.IntermediateClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#forClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForClause(XQueryParserScripting.ForClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#forBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForBinding(XQueryParserScripting.ForBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#allowingEmpty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllowingEmpty(XQueryParserScripting.AllowingEmptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#positionalVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionalVar(XQueryParserScripting.PositionalVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#letClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetClause(XQueryParserScripting.LetClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#letBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetBinding(XQueryParserScripting.LetBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#windowClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowClause(XQueryParserScripting.WindowClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#tumblingWindowClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTumblingWindowClause(XQueryParserScripting.TumblingWindowClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#slidingWindowClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlidingWindowClause(XQueryParserScripting.SlidingWindowClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#windowStartCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowStartCondition(XQueryParserScripting.WindowStartConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#windowEndCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowEndCondition(XQueryParserScripting.WindowEndConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#windowVars}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowVars(XQueryParserScripting.WindowVarsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#countClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCountClause(XQueryParserScripting.CountClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(XQueryParserScripting.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClause(XQueryParserScripting.GroupByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#groupingSpecList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupingSpecList(XQueryParserScripting.GroupingSpecListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#groupingSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupingSpec(XQueryParserScripting.GroupingSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(XQueryParserScripting.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#orderSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderSpec(XQueryParserScripting.OrderSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#returnClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnClause(XQueryParserScripting.ReturnClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#quantifiedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifiedExpr(XQueryParserScripting.QuantifiedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#quantifiedVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifiedVar(XQueryParserScripting.QuantifiedVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#switchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchExpr(XQueryParserScripting.SwitchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#switchCaseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCaseClause(XQueryParserScripting.SwitchCaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#switchCaseOperand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCaseOperand(XQueryParserScripting.SwitchCaseOperandContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#typeswitchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeswitchExpr(XQueryParserScripting.TypeswitchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#caseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseClause(XQueryParserScripting.CaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#sequenceUnionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceUnionType(XQueryParserScripting.SequenceUnionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#ifExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpr(XQueryParserScripting.IfExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#tryCatchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatchExpr(XQueryParserScripting.TryCatchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#tryClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryClause(XQueryParserScripting.TryClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#enclosedTryTargetExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedTryTargetExpression(XQueryParserScripting.EnclosedTryTargetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#catchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchClause(XQueryParserScripting.CatchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#enclosedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedExpression(XQueryParserScripting.EnclosedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#catchErrorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchErrorList(XQueryParserScripting.CatchErrorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#existUpdateExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistUpdateExpr(XQueryParserScripting.ExistUpdateExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#existReplaceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistReplaceExpr(XQueryParserScripting.ExistReplaceExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#existValueExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistValueExpr(XQueryParserScripting.ExistValueExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#existInsertExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistInsertExpr(XQueryParserScripting.ExistInsertExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#existDeleteExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistDeleteExpr(XQueryParserScripting.ExistDeleteExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#existRenameExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistRenameExpr(XQueryParserScripting.ExistRenameExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(XQueryParserScripting.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(XQueryParserScripting.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpr(XQueryParserScripting.ComparisonExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringConcatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConcatExpr(XQueryParserScripting.StringConcatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#rangeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeExpr(XQueryParserScripting.RangeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#additiveExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(XQueryParserScripting.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#multiplicativeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpr(XQueryParserScripting.MultiplicativeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#unionExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionExpr(XQueryParserScripting.UnionExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#intersectExceptExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntersectExceptExpr(XQueryParserScripting.IntersectExceptExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#instanceOfExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceOfExpr(XQueryParserScripting.InstanceOfExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#treatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTreatExpr(XQueryParserScripting.TreatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#castableExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastableExpr(XQueryParserScripting.CastableExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#castExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpr(XQueryParserScripting.CastExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#arrowExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowExpr(XQueryParserScripting.ArrowExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#complexArrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexArrow(XQueryParserScripting.ComplexArrowContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#unaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(XQueryParserScripting.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#valueExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExpr(XQueryParserScripting.ValueExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#generalComp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneralComp(XQueryParserScripting.GeneralCompContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#valueComp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueComp(XQueryParserScripting.ValueCompContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#nodeComp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeComp(XQueryParserScripting.NodeCompContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#validateExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidateExpr(XQueryParserScripting.ValidateExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#validationMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidationMode(XQueryParserScripting.ValidationModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#extensionExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtensionExpr(XQueryParserScripting.ExtensionExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#simpleMapExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleMapExpr(XQueryParserScripting.SimpleMapExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(XQueryParserScripting.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#applyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApplyStatement(XQueryParserScripting.ApplyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#assignStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStatement(XQueryParserScripting.AssignStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(XQueryParserScripting.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(XQueryParserScripting.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(XQueryParserScripting.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#exitStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExitStatement(XQueryParserScripting.ExitStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#flworStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlworStatement(XQueryParserScripting.FlworStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(XQueryParserScripting.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(XQueryParserScripting.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#switchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchStatement(XQueryParserScripting.SwitchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#switchCaseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCaseStatement(XQueryParserScripting.SwitchCaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#tryCatchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatchStatement(XQueryParserScripting.TryCatchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#typeswitchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeswitchStatement(XQueryParserScripting.TypeswitchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#caseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseStatement(XQueryParserScripting.CaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#varDeclStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclStatement(XQueryParserScripting.VarDeclStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(XQueryParserScripting.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#pathExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathExpr(XQueryParserScripting.PathExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#relativePathExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelativePathExpr(XQueryParserScripting.RelativePathExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stepExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStepExpr(XQueryParserScripting.StepExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#axisStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAxisStep(XQueryParserScripting.AxisStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#forwardStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForwardStep(XQueryParserScripting.ForwardStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#forwardAxis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForwardAxis(XQueryParserScripting.ForwardAxisContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#abbrevForwardStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbbrevForwardStep(XQueryParserScripting.AbbrevForwardStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#reverseStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverseStep(XQueryParserScripting.ReverseStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#reverseAxis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverseAxis(XQueryParserScripting.ReverseAxisContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#abbrevReverseStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbbrevReverseStep(XQueryParserScripting.AbbrevReverseStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#nodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeTest(XQueryParserScripting.NodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#nameTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNameTest(XQueryParserScripting.NameTestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allNames}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllNames(XQueryParserScripting.AllNamesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allWithNS}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllWithNS(XQueryParserScripting.AllWithNSContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allWithLocal}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllWithLocal(XQueryParserScripting.AllWithLocalContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#postfixExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpr(XQueryParserScripting.PostfixExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(XQueryParserScripting.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#predicateList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateList(XQueryParserScripting.PredicateListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(XQueryParserScripting.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#lookup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLookup(XQueryParserScripting.LookupContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#keySpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeySpecifier(XQueryParserScripting.KeySpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#arrowFunctionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowFunctionSpecifier(XQueryParserScripting.ArrowFunctionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(XQueryParserScripting.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(XQueryParserScripting.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(XQueryParserScripting.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#varRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarRef(XQueryParserScripting.VarRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#varName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarName(XQueryParserScripting.VarNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#parenthesizedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpr(XQueryParserScripting.ParenthesizedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#contextItemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextItemExpr(XQueryParserScripting.ContextItemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#orderedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderedExpr(XQueryParserScripting.OrderedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#unorderedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnorderedExpr(XQueryParserScripting.UnorderedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(XQueryParserScripting.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(XQueryParserScripting.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#nodeConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeConstructor(XQueryParserScripting.NodeConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#directConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectConstructor(XQueryParserScripting.DirectConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirElemConstructorOpenClose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirElemConstructorOpenClose(XQueryParserScripting.DirElemConstructorOpenCloseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirElemConstructorSingleTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirElemConstructorSingleTag(XQueryParserScripting.DirElemConstructorSingleTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirAttributeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeList(XQueryParserScripting.DirAttributeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirAttributeValueApos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeValueApos(XQueryParserScripting.DirAttributeValueAposContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirAttributeValueQuot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeValueQuot(XQueryParserScripting.DirAttributeValueQuotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirAttributeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeValue(XQueryParserScripting.DirAttributeValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirAttributeContentQuot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeContentQuot(XQueryParserScripting.DirAttributeContentQuotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirAttributeContentApos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeContentApos(XQueryParserScripting.DirAttributeContentAposContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#dirElemContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirElemContent(XQueryParserScripting.DirElemContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#commonContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonContent(XQueryParserScripting.CommonContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#contentExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContentExpr(XQueryParserScripting.ContentExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#computedConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComputedConstructor(XQueryParserScripting.ComputedConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compMLJSONConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONConstructor(XQueryParserScripting.CompMLJSONConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compMLJSONArrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONArrayConstructor(XQueryParserScripting.CompMLJSONArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compMLJSONObjectConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONObjectConstructor(XQueryParserScripting.CompMLJSONObjectConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compMLJSONNumberConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONNumberConstructor(XQueryParserScripting.CompMLJSONNumberConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compMLJSONBooleanConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONBooleanConstructor(XQueryParserScripting.CompMLJSONBooleanConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compMLJSONNullConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONNullConstructor(XQueryParserScripting.CompMLJSONNullConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compBinaryConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompBinaryConstructor(XQueryParserScripting.CompBinaryConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compDocConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompDocConstructor(XQueryParserScripting.CompDocConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compElemConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompElemConstructor(XQueryParserScripting.CompElemConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#enclosedContentExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedContentExpr(XQueryParserScripting.EnclosedContentExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compAttrConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompAttrConstructor(XQueryParserScripting.CompAttrConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compNamespaceConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompNamespaceConstructor(XQueryParserScripting.CompNamespaceConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(XQueryParserScripting.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#enclosedPrefixExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedPrefixExpr(XQueryParserScripting.EnclosedPrefixExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#enclosedURIExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedURIExpr(XQueryParserScripting.EnclosedURIExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compTextConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompTextConstructor(XQueryParserScripting.CompTextConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compCommentConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompCommentConstructor(XQueryParserScripting.CompCommentConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#compPIConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompPIConstructor(XQueryParserScripting.CompPIConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionItemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionItemExpr(XQueryParserScripting.FunctionItemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#namedFunctionRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedFunctionRef(XQueryParserScripting.NamedFunctionRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#inlineFunctionRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineFunctionRef(XQueryParserScripting.InlineFunctionRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(XQueryParserScripting.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mapConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapConstructor(XQueryParserScripting.MapConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mapConstructorEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapConstructorEntry(XQueryParserScripting.MapConstructorEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#arrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayConstructor(XQueryParserScripting.ArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#squareArrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSquareArrayConstructor(XQueryParserScripting.SquareArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#curlyArrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurlyArrayConstructor(XQueryParserScripting.CurlyArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstructor(XQueryParserScripting.StringConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringConstructorContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstructorContent(XQueryParserScripting.StringConstructorContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#charNoGrave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharNoGrave(XQueryParserScripting.CharNoGraveContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#charNoLBrace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharNoLBrace(XQueryParserScripting.CharNoLBraceContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#charNoRBrack}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharNoRBrack(XQueryParserScripting.CharNoRBrackContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringConstructorChars}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstructorChars(XQueryParserScripting.StringConstructorCharsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringConstructorInterpolation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstructorInterpolation(XQueryParserScripting.StringConstructorInterpolationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#unaryLookup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryLookup(XQueryParserScripting.UnaryLookupContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#singleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleType(XQueryParserScripting.SingleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(XQueryParserScripting.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#sequenceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceType(XQueryParserScripting.SequenceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#itemType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItemType(XQueryParserScripting.ItemTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#atomicOrUnionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicOrUnionType(XQueryParserScripting.AtomicOrUnionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#kindTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKindTest(XQueryParserScripting.KindTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#anyKindTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyKindTest(XQueryParserScripting.AnyKindTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#binaryNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryNodeTest(XQueryParserScripting.BinaryNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#documentTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocumentTest(XQueryParserScripting.DocumentTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#textTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextTest(XQueryParserScripting.TextTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#commentTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentTest(XQueryParserScripting.CommentTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#namespaceNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceNodeTest(XQueryParserScripting.NamespaceNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#piTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPiTest(XQueryParserScripting.PiTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#attributeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeTest(XQueryParserScripting.AttributeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#attributeNameOrWildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeNameOrWildcard(XQueryParserScripting.AttributeNameOrWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#schemaAttributeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaAttributeTest(XQueryParserScripting.SchemaAttributeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#elementTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementTest(XQueryParserScripting.ElementTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#elementNameOrWildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementNameOrWildcard(XQueryParserScripting.ElementNameOrWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#schemaElementTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaElementTest(XQueryParserScripting.SchemaElementTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#elementDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementDeclaration(XQueryParserScripting.ElementDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#attributeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeName(XQueryParserScripting.AttributeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#elementName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementName(XQueryParserScripting.ElementNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#simpleTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTypeName(XQueryParserScripting.SimpleTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(XQueryParserScripting.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionTest(XQueryParserScripting.FunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#anyFunctionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyFunctionTest(XQueryParserScripting.AnyFunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#typedFunctionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedFunctionTest(XQueryParserScripting.TypedFunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mapTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapTest(XQueryParserScripting.MapTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#anyMapTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyMapTest(XQueryParserScripting.AnyMapTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#typedMapTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedMapTest(XQueryParserScripting.TypedMapTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#arrayTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayTest(XQueryParserScripting.ArrayTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#anyArrayTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyArrayTest(XQueryParserScripting.AnyArrayTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#typedArrayTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedArrayTest(XQueryParserScripting.TypedArrayTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#parenthesizedItemTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedItemTest(XQueryParserScripting.ParenthesizedItemTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#attributeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeDeclaration(XQueryParserScripting.AttributeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mlNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlNodeTest(XQueryParserScripting.MlNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mlArrayNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlArrayNodeTest(XQueryParserScripting.MlArrayNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mlObjectNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlObjectNodeTest(XQueryParserScripting.MlObjectNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mlNumberNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlNumberNodeTest(XQueryParserScripting.MlNumberNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mlBooleanNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlBooleanNodeTest(XQueryParserScripting.MlBooleanNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#mlNullNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlNullNodeTest(XQueryParserScripting.MlNullNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#eqName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqName(XQueryParserScripting.EqNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#qName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQName(XQueryParserScripting.QNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#ncName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNcName(XQueryParserScripting.NcNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(XQueryParserScripting.FunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(XQueryParserScripting.KeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#keywordNotOKForFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordNotOKForFunction(XQueryParserScripting.KeywordNotOKForFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#keywordOKForFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordOKForFunction(XQueryParserScripting.KeywordOKForFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#uriLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUriLiteral(XQueryParserScripting.UriLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringLiteralQuot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteralQuot(XQueryParserScripting.StringLiteralQuotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringLiteralApos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteralApos(XQueryParserScripting.StringLiteralAposContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(XQueryParserScripting.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringContentQuot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringContentQuot(XQueryParserScripting.StringContentQuotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#stringContentApos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringContentApos(XQueryParserScripting.StringContentAposContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParserScripting#noQuotesNoBracesNoAmpNoLAng}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoQuotesNoBracesNoAmpNoLAng(XQueryParserScripting.NoQuotesNoBracesNoAmpNoLAngContext ctx);
}