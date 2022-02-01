// Generated from ./src/main/java/org/rumbledb/parser/XQueryParser.g4 by ANTLR 4.8

// Java header
package org.rumbledb.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XQueryParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface XQueryParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link XQueryParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(XQueryParser.ModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#xqDocComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXqDocComment(XQueryParser.XqDocCommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#versionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersionDecl(XQueryParser.VersionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mainModule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainModule(XQueryParser.MainModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#queryBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryBody(XQueryParser.QueryBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#libraryModule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryModule(XQueryParser.LibraryModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#moduleDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleDecl(XQueryParser.ModuleDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#prolog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProlog(XQueryParser.PrologContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#annotatedDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotatedDecl(XQueryParser.AnnotatedDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#defaultNamespaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultNamespaceDecl(XQueryParser.DefaultNamespaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#setter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetter(XQueryParser.SetterContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#boundarySpaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundarySpaceDecl(XQueryParser.BoundarySpaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#defaultCollationDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultCollationDecl(XQueryParser.DefaultCollationDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#baseURIDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseURIDecl(XQueryParser.BaseURIDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#constructionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructionDecl(XQueryParser.ConstructionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#orderingModeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderingModeDecl(XQueryParser.OrderingModeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#emptyOrderDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyOrderDecl(XQueryParser.EmptyOrderDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#copyNamespacesDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopyNamespacesDecl(XQueryParser.CopyNamespacesDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#preserveMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreserveMode(XQueryParser.PreserveModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#inheritMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInheritMode(XQueryParser.InheritModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#decimalFormatDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalFormatDecl(XQueryParser.DecimalFormatDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#schemaImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaImport(XQueryParser.SchemaImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#schemaPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaPrefix(XQueryParser.SchemaPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#moduleImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleImport(XQueryParser.ModuleImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#namespaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceDecl(XQueryParser.NamespaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(XQueryParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#varValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarValue(XQueryParser.VarValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#varDefaultValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDefaultValue(XQueryParser.VarDefaultValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#contextItemDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextItemDecl(XQueryParser.ContextItemDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(XQueryParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParams(XQueryParser.FunctionParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParam(XQueryParser.FunctionParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#annotations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotations(XQueryParser.AnnotationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(XQueryParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#annotList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotList(XQueryParser.AnnotListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#annotationParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationParam(XQueryParser.AnnotationParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionReturn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReturn(XQueryParser.FunctionReturnContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#optionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionDecl(XQueryParser.OptionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(XQueryParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#exprSingle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSingle(XQueryParser.ExprSingleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#flworExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlworExpr(XQueryParser.FlworExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#initialClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitialClause(XQueryParser.InitialClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#intermediateClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntermediateClause(XQueryParser.IntermediateClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#forClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForClause(XQueryParser.ForClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#forBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForBinding(XQueryParser.ForBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#allowingEmpty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllowingEmpty(XQueryParser.AllowingEmptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#positionalVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionalVar(XQueryParser.PositionalVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#letClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetClause(XQueryParser.LetClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#letBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetBinding(XQueryParser.LetBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#windowClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowClause(XQueryParser.WindowClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#tumblingWindowClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTumblingWindowClause(XQueryParser.TumblingWindowClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#slidingWindowClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlidingWindowClause(XQueryParser.SlidingWindowClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#windowStartCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowStartCondition(XQueryParser.WindowStartConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#windowEndCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowEndCondition(XQueryParser.WindowEndConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#windowVars}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowVars(XQueryParser.WindowVarsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#countClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCountClause(XQueryParser.CountClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(XQueryParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClause(XQueryParser.GroupByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#groupingSpecList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupingSpecList(XQueryParser.GroupingSpecListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#groupingSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupingSpec(XQueryParser.GroupingSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(XQueryParser.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#orderSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderSpec(XQueryParser.OrderSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#returnClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnClause(XQueryParser.ReturnClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#quantifiedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifiedExpr(XQueryParser.QuantifiedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#quantifiedVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifiedVar(XQueryParser.QuantifiedVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#switchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchExpr(XQueryParser.SwitchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#switchCaseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCaseClause(XQueryParser.SwitchCaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#switchCaseOperand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCaseOperand(XQueryParser.SwitchCaseOperandContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#typeswitchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeswitchExpr(XQueryParser.TypeswitchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#caseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseClause(XQueryParser.CaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#sequenceUnionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceUnionType(XQueryParser.SequenceUnionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#ifExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpr(XQueryParser.IfExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#tryCatchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatchExpr(XQueryParser.TryCatchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#tryClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryClause(XQueryParser.TryClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#enclosedTryTargetExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedTryTargetExpression(XQueryParser.EnclosedTryTargetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#catchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchClause(XQueryParser.CatchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#enclosedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedExpression(XQueryParser.EnclosedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#catchErrorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchErrorList(XQueryParser.CatchErrorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#existUpdateExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistUpdateExpr(XQueryParser.ExistUpdateExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#existReplaceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistReplaceExpr(XQueryParser.ExistReplaceExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#existValueExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistValueExpr(XQueryParser.ExistValueExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#existInsertExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistInsertExpr(XQueryParser.ExistInsertExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#existDeleteExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistDeleteExpr(XQueryParser.ExistDeleteExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#existRenameExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistRenameExpr(XQueryParser.ExistRenameExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(XQueryParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(XQueryParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpr(XQueryParser.ComparisonExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringConcatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConcatExpr(XQueryParser.StringConcatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#rangeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeExpr(XQueryParser.RangeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#additiveExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(XQueryParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpr(XQueryParser.MultiplicativeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#unionExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionExpr(XQueryParser.UnionExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#intersectExceptExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntersectExceptExpr(XQueryParser.IntersectExceptExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#instanceOfExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceOfExpr(XQueryParser.InstanceOfExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#treatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTreatExpr(XQueryParser.TreatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#castableExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastableExpr(XQueryParser.CastableExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#castExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpr(XQueryParser.CastExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#arrowExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowExpr(XQueryParser.ArrowExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#complexArrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexArrow(XQueryParser.ComplexArrowContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#unaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(XQueryParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#valueExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExpr(XQueryParser.ValueExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#generalComp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneralComp(XQueryParser.GeneralCompContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#valueComp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueComp(XQueryParser.ValueCompContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#nodeComp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeComp(XQueryParser.NodeCompContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#validateExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidateExpr(XQueryParser.ValidateExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#validationMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidationMode(XQueryParser.ValidationModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#extensionExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtensionExpr(XQueryParser.ExtensionExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#simpleMapExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleMapExpr(XQueryParser.SimpleMapExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#pathExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathExpr(XQueryParser.PathExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#relativePathExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelativePathExpr(XQueryParser.RelativePathExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stepExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStepExpr(XQueryParser.StepExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#axisStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAxisStep(XQueryParser.AxisStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#forwardStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForwardStep(XQueryParser.ForwardStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#forwardAxis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForwardAxis(XQueryParser.ForwardAxisContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#abbrevForwardStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbbrevForwardStep(XQueryParser.AbbrevForwardStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#reverseStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverseStep(XQueryParser.ReverseStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#reverseAxis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverseAxis(XQueryParser.ReverseAxisContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#abbrevReverseStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbbrevReverseStep(XQueryParser.AbbrevReverseStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#nodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeTest(XQueryParser.NodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#nameTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNameTest(XQueryParser.NameTestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allNames}
	 * labeled alternative in {@link XQueryParser#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllNames(XQueryParser.AllNamesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allWithNS}
	 * labeled alternative in {@link XQueryParser#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllWithNS(XQueryParser.AllWithNSContext ctx);
	/**
	 * Visit a parse tree produced by the {@code allWithLocal}
	 * labeled alternative in {@link XQueryParser#wildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllWithLocal(XQueryParser.AllWithLocalContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#postfixExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpr(XQueryParser.PostfixExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(XQueryParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#predicateList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateList(XQueryParser.PredicateListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(XQueryParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#lookup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLookup(XQueryParser.LookupContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#keySpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeySpecifier(XQueryParser.KeySpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#arrowFunctionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrowFunctionSpecifier(XQueryParser.ArrowFunctionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(XQueryParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(XQueryParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(XQueryParser.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#varRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarRef(XQueryParser.VarRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#varName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarName(XQueryParser.VarNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#parenthesizedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpr(XQueryParser.ParenthesizedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#contextItemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextItemExpr(XQueryParser.ContextItemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#orderedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderedExpr(XQueryParser.OrderedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#unorderedExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnorderedExpr(XQueryParser.UnorderedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(XQueryParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(XQueryParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#nodeConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeConstructor(XQueryParser.NodeConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#directConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectConstructor(XQueryParser.DirectConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirElemConstructorOpenClose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirElemConstructorOpenClose(XQueryParser.DirElemConstructorOpenCloseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirElemConstructorSingleTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirElemConstructorSingleTag(XQueryParser.DirElemConstructorSingleTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirAttributeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeList(XQueryParser.DirAttributeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirAttributeValueApos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeValueApos(XQueryParser.DirAttributeValueAposContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirAttributeValueQuot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeValueQuot(XQueryParser.DirAttributeValueQuotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirAttributeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeValue(XQueryParser.DirAttributeValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirAttributeContentQuot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeContentQuot(XQueryParser.DirAttributeContentQuotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirAttributeContentApos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirAttributeContentApos(XQueryParser.DirAttributeContentAposContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#dirElemContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirElemContent(XQueryParser.DirElemContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#commonContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonContent(XQueryParser.CommonContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#computedConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComputedConstructor(XQueryParser.ComputedConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compMLJSONConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONConstructor(XQueryParser.CompMLJSONConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compMLJSONArrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONArrayConstructor(XQueryParser.CompMLJSONArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compMLJSONObjectConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONObjectConstructor(XQueryParser.CompMLJSONObjectConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compMLJSONNumberConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONNumberConstructor(XQueryParser.CompMLJSONNumberConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compMLJSONBooleanConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONBooleanConstructor(XQueryParser.CompMLJSONBooleanConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compMLJSONNullConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompMLJSONNullConstructor(XQueryParser.CompMLJSONNullConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compBinaryConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompBinaryConstructor(XQueryParser.CompBinaryConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compDocConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompDocConstructor(XQueryParser.CompDocConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compElemConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompElemConstructor(XQueryParser.CompElemConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#enclosedContentExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedContentExpr(XQueryParser.EnclosedContentExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compAttrConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompAttrConstructor(XQueryParser.CompAttrConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compNamespaceConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompNamespaceConstructor(XQueryParser.CompNamespaceConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(XQueryParser.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#enclosedPrefixExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedPrefixExpr(XQueryParser.EnclosedPrefixExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#enclosedURIExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnclosedURIExpr(XQueryParser.EnclosedURIExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compTextConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompTextConstructor(XQueryParser.CompTextConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compCommentConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompCommentConstructor(XQueryParser.CompCommentConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#compPIConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompPIConstructor(XQueryParser.CompPIConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionItemExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionItemExpr(XQueryParser.FunctionItemExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#namedFunctionRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedFunctionRef(XQueryParser.NamedFunctionRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#inlineFunctionRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineFunctionRef(XQueryParser.InlineFunctionRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(XQueryParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mapConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapConstructor(XQueryParser.MapConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mapConstructorEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapConstructorEntry(XQueryParser.MapConstructorEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#arrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayConstructor(XQueryParser.ArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#squareArrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSquareArrayConstructor(XQueryParser.SquareArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#curlyArrayConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurlyArrayConstructor(XQueryParser.CurlyArrayConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstructor(XQueryParser.StringConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringConstructorContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstructorContent(XQueryParser.StringConstructorContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#charNoGrave}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharNoGrave(XQueryParser.CharNoGraveContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#charNoLBrace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharNoLBrace(XQueryParser.CharNoLBraceContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#charNoRBrack}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharNoRBrack(XQueryParser.CharNoRBrackContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringConstructorChars}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstructorChars(XQueryParser.StringConstructorCharsContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringConstructorInterpolation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstructorInterpolation(XQueryParser.StringConstructorInterpolationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#unaryLookup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryLookup(XQueryParser.UnaryLookupContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#singleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleType(XQueryParser.SingleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(XQueryParser.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#sequenceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceType(XQueryParser.SequenceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#itemType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItemType(XQueryParser.ItemTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#atomicOrUnionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomicOrUnionType(XQueryParser.AtomicOrUnionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#kindTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKindTest(XQueryParser.KindTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#anyKindTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyKindTest(XQueryParser.AnyKindTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#binaryNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryNodeTest(XQueryParser.BinaryNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#documentTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocumentTest(XQueryParser.DocumentTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#textTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextTest(XQueryParser.TextTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#commentTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommentTest(XQueryParser.CommentTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#namespaceNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceNodeTest(XQueryParser.NamespaceNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#piTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPiTest(XQueryParser.PiTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#attributeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeTest(XQueryParser.AttributeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#attributeNameOrWildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeNameOrWildcard(XQueryParser.AttributeNameOrWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#schemaAttributeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaAttributeTest(XQueryParser.SchemaAttributeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#elementTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementTest(XQueryParser.ElementTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#elementNameOrWildcard}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementNameOrWildcard(XQueryParser.ElementNameOrWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#schemaElementTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaElementTest(XQueryParser.SchemaElementTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#elementDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementDeclaration(XQueryParser.ElementDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#attributeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeName(XQueryParser.AttributeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#elementName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementName(XQueryParser.ElementNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#simpleTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTypeName(XQueryParser.SimpleTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(XQueryParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionTest(XQueryParser.FunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#anyFunctionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyFunctionTest(XQueryParser.AnyFunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#typedFunctionTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedFunctionTest(XQueryParser.TypedFunctionTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mapTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapTest(XQueryParser.MapTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#anyMapTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyMapTest(XQueryParser.AnyMapTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#typedMapTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedMapTest(XQueryParser.TypedMapTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#arrayTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayTest(XQueryParser.ArrayTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#anyArrayTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyArrayTest(XQueryParser.AnyArrayTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#typedArrayTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedArrayTest(XQueryParser.TypedArrayTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#parenthesizedItemTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedItemTest(XQueryParser.ParenthesizedItemTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#attributeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeDeclaration(XQueryParser.AttributeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mlNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlNodeTest(XQueryParser.MlNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mlArrayNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlArrayNodeTest(XQueryParser.MlArrayNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mlObjectNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlObjectNodeTest(XQueryParser.MlObjectNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mlNumberNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlNumberNodeTest(XQueryParser.MlNumberNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mlBooleanNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlBooleanNodeTest(XQueryParser.MlBooleanNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#mlNullNodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMlNullNodeTest(XQueryParser.MlNullNodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#eqName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqName(XQueryParser.EqNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#qName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQName(XQueryParser.QNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#ncName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNcName(XQueryParser.NcNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(XQueryParser.FunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(XQueryParser.KeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#keywordNotOKForFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordNotOKForFunction(XQueryParser.KeywordNotOKForFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#keywordOKForFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordOKForFunction(XQueryParser.KeywordOKForFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#uriLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUriLiteral(XQueryParser.UriLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringLiteralQuot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteralQuot(XQueryParser.StringLiteralQuotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringLiteralApos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteralApos(XQueryParser.StringLiteralAposContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(XQueryParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringContentQuot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringContentQuot(XQueryParser.StringContentQuotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#stringContentApos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringContentApos(XQueryParser.StringContentAposContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQueryParser#noQuotesNoBracesNoAmpNoLAng}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoQuotesNoBracesNoAmpNoLAng(XQueryParser.NoQuotesNoBracesNoAmpNoLAngContext ctx);
}