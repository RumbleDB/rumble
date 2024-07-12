// Generated from /Users/dbuzatu/ETH/Thesis/rumble/src/main/java/org/rumbledb/parser/XQueryParserScripting.g4 by ANTLR 4.13.1

// Java header
package org.rumbledb.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XQueryParserScripting}.
 */
public interface XQueryParserScriptingListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(XQueryParserScripting.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(XQueryParserScripting.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#xqDocComment}.
	 * @param ctx the parse tree
	 */
	void enterXqDocComment(XQueryParserScripting.XqDocCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#xqDocComment}.
	 * @param ctx the parse tree
	 */
	void exitXqDocComment(XQueryParserScripting.XqDocCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#versionDecl}.
	 * @param ctx the parse tree
	 */
	void enterVersionDecl(XQueryParserScripting.VersionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#versionDecl}.
	 * @param ctx the parse tree
	 */
	void exitVersionDecl(XQueryParserScripting.VersionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mainModule}.
	 * @param ctx the parse tree
	 */
	void enterMainModule(XQueryParserScripting.MainModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mainModule}.
	 * @param ctx the parse tree
	 */
	void exitMainModule(XQueryParserScripting.MainModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(XQueryParserScripting.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(XQueryParserScripting.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#libraryModule}.
	 * @param ctx the parse tree
	 */
	void enterLibraryModule(XQueryParserScripting.LibraryModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#libraryModule}.
	 * @param ctx the parse tree
	 */
	void exitLibraryModule(XQueryParserScripting.LibraryModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#moduleDecl}.
	 * @param ctx the parse tree
	 */
	void enterModuleDecl(XQueryParserScripting.ModuleDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#moduleDecl}.
	 * @param ctx the parse tree
	 */
	void exitModuleDecl(XQueryParserScripting.ModuleDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#prolog}.
	 * @param ctx the parse tree
	 */
	void enterProlog(XQueryParserScripting.PrologContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#prolog}.
	 * @param ctx the parse tree
	 */
	void exitProlog(XQueryParserScripting.PrologContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#annotatedDecl}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedDecl(XQueryParserScripting.AnnotatedDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#annotatedDecl}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedDecl(XQueryParserScripting.AnnotatedDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#defaultNamespaceDecl}.
	 * @param ctx the parse tree
	 */
	void enterDefaultNamespaceDecl(XQueryParserScripting.DefaultNamespaceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#defaultNamespaceDecl}.
	 * @param ctx the parse tree
	 */
	void exitDefaultNamespaceDecl(XQueryParserScripting.DefaultNamespaceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#setter}.
	 * @param ctx the parse tree
	 */
	void enterSetter(XQueryParserScripting.SetterContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#setter}.
	 * @param ctx the parse tree
	 */
	void exitSetter(XQueryParserScripting.SetterContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#boundarySpaceDecl}.
	 * @param ctx the parse tree
	 */
	void enterBoundarySpaceDecl(XQueryParserScripting.BoundarySpaceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#boundarySpaceDecl}.
	 * @param ctx the parse tree
	 */
	void exitBoundarySpaceDecl(XQueryParserScripting.BoundarySpaceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#defaultCollationDecl}.
	 * @param ctx the parse tree
	 */
	void enterDefaultCollationDecl(XQueryParserScripting.DefaultCollationDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#defaultCollationDecl}.
	 * @param ctx the parse tree
	 */
	void exitDefaultCollationDecl(XQueryParserScripting.DefaultCollationDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#baseURIDecl}.
	 * @param ctx the parse tree
	 */
	void enterBaseURIDecl(XQueryParserScripting.BaseURIDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#baseURIDecl}.
	 * @param ctx the parse tree
	 */
	void exitBaseURIDecl(XQueryParserScripting.BaseURIDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#constructionDecl}.
	 * @param ctx the parse tree
	 */
	void enterConstructionDecl(XQueryParserScripting.ConstructionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#constructionDecl}.
	 * @param ctx the parse tree
	 */
	void exitConstructionDecl(XQueryParserScripting.ConstructionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#orderingModeDecl}.
	 * @param ctx the parse tree
	 */
	void enterOrderingModeDecl(XQueryParserScripting.OrderingModeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#orderingModeDecl}.
	 * @param ctx the parse tree
	 */
	void exitOrderingModeDecl(XQueryParserScripting.OrderingModeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#emptyOrderDecl}.
	 * @param ctx the parse tree
	 */
	void enterEmptyOrderDecl(XQueryParserScripting.EmptyOrderDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#emptyOrderDecl}.
	 * @param ctx the parse tree
	 */
	void exitEmptyOrderDecl(XQueryParserScripting.EmptyOrderDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#copyNamespacesDecl}.
	 * @param ctx the parse tree
	 */
	void enterCopyNamespacesDecl(XQueryParserScripting.CopyNamespacesDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#copyNamespacesDecl}.
	 * @param ctx the parse tree
	 */
	void exitCopyNamespacesDecl(XQueryParserScripting.CopyNamespacesDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#preserveMode}.
	 * @param ctx the parse tree
	 */
	void enterPreserveMode(XQueryParserScripting.PreserveModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#preserveMode}.
	 * @param ctx the parse tree
	 */
	void exitPreserveMode(XQueryParserScripting.PreserveModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#inheritMode}.
	 * @param ctx the parse tree
	 */
	void enterInheritMode(XQueryParserScripting.InheritModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#inheritMode}.
	 * @param ctx the parse tree
	 */
	void exitInheritMode(XQueryParserScripting.InheritModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#decimalFormatDecl}.
	 * @param ctx the parse tree
	 */
	void enterDecimalFormatDecl(XQueryParserScripting.DecimalFormatDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#decimalFormatDecl}.
	 * @param ctx the parse tree
	 */
	void exitDecimalFormatDecl(XQueryParserScripting.DecimalFormatDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#schemaImport}.
	 * @param ctx the parse tree
	 */
	void enterSchemaImport(XQueryParserScripting.SchemaImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#schemaImport}.
	 * @param ctx the parse tree
	 */
	void exitSchemaImport(XQueryParserScripting.SchemaImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#schemaPrefix}.
	 * @param ctx the parse tree
	 */
	void enterSchemaPrefix(XQueryParserScripting.SchemaPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#schemaPrefix}.
	 * @param ctx the parse tree
	 */
	void exitSchemaPrefix(XQueryParserScripting.SchemaPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#moduleImport}.
	 * @param ctx the parse tree
	 */
	void enterModuleImport(XQueryParserScripting.ModuleImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#moduleImport}.
	 * @param ctx the parse tree
	 */
	void exitModuleImport(XQueryParserScripting.ModuleImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#namespaceDecl}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceDecl(XQueryParserScripting.NamespaceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#namespaceDecl}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceDecl(XQueryParserScripting.NamespaceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(XQueryParserScripting.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(XQueryParserScripting.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#varValue}.
	 * @param ctx the parse tree
	 */
	void enterVarValue(XQueryParserScripting.VarValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#varValue}.
	 * @param ctx the parse tree
	 */
	void exitVarValue(XQueryParserScripting.VarValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#varDefaultValue}.
	 * @param ctx the parse tree
	 */
	void enterVarDefaultValue(XQueryParserScripting.VarDefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#varDefaultValue}.
	 * @param ctx the parse tree
	 */
	void exitVarDefaultValue(XQueryParserScripting.VarDefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#contextItemDecl}.
	 * @param ctx the parse tree
	 */
	void enterContextItemDecl(XQueryParserScripting.ContextItemDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#contextItemDecl}.
	 * @param ctx the parse tree
	 */
	void exitContextItemDecl(XQueryParserScripting.ContextItemDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionDecl}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDecl(XQueryParserScripting.FunctionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionDecl}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDecl(XQueryParserScripting.FunctionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionParams}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParams(XQueryParserScripting.FunctionParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionParams}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParams(XQueryParserScripting.FunctionParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionParam}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParam(XQueryParserScripting.FunctionParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionParam}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParam(XQueryParserScripting.FunctionParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#annotations}.
	 * @param ctx the parse tree
	 */
	void enterAnnotations(XQueryParserScripting.AnnotationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#annotations}.
	 * @param ctx the parse tree
	 */
	void exitAnnotations(XQueryParserScripting.AnnotationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(XQueryParserScripting.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(XQueryParserScripting.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#annotList}.
	 * @param ctx the parse tree
	 */
	void enterAnnotList(XQueryParserScripting.AnnotListContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#annotList}.
	 * @param ctx the parse tree
	 */
	void exitAnnotList(XQueryParserScripting.AnnotListContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#annotationParam}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationParam(XQueryParserScripting.AnnotationParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#annotationParam}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationParam(XQueryParserScripting.AnnotationParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionReturn}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReturn(XQueryParserScripting.FunctionReturnContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionReturn}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReturn(XQueryParserScripting.FunctionReturnContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#optionDecl}.
	 * @param ctx the parse tree
	 */
	void enterOptionDecl(XQueryParserScripting.OptionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#optionDecl}.
	 * @param ctx the parse tree
	 */
	void exitOptionDecl(XQueryParserScripting.OptionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(XQueryParserScripting.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(XQueryParserScripting.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#statementsAndExpr}.
	 * @param ctx the parse tree
	 */
	void enterStatementsAndExpr(XQueryParserScripting.StatementsAndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#statementsAndExpr}.
	 * @param ctx the parse tree
	 */
	void exitStatementsAndExpr(XQueryParserScripting.StatementsAndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#statementsAndOptionalExpr}.
	 * @param ctx the parse tree
	 */
	void enterStatementsAndOptionalExpr(XQueryParserScripting.StatementsAndOptionalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#statementsAndOptionalExpr}.
	 * @param ctx the parse tree
	 */
	void exitStatementsAndOptionalExpr(XQueryParserScripting.StatementsAndOptionalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(XQueryParserScripting.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(XQueryParserScripting.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#exprSingle}.
	 * @param ctx the parse tree
	 */
	void enterExprSingle(XQueryParserScripting.ExprSingleContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#exprSingle}.
	 * @param ctx the parse tree
	 */
	void exitExprSingle(XQueryParserScripting.ExprSingleContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#exprSimple}.
	 * @param ctx the parse tree
	 */
	void enterExprSimple(XQueryParserScripting.ExprSimpleContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#exprSimple}.
	 * @param ctx the parse tree
	 */
	void exitExprSimple(XQueryParserScripting.ExprSimpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#blockExpr}.
	 * @param ctx the parse tree
	 */
	void enterBlockExpr(XQueryParserScripting.BlockExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#blockExpr}.
	 * @param ctx the parse tree
	 */
	void exitBlockExpr(XQueryParserScripting.BlockExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#flworExpr}.
	 * @param ctx the parse tree
	 */
	void enterFlworExpr(XQueryParserScripting.FlworExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#flworExpr}.
	 * @param ctx the parse tree
	 */
	void exitFlworExpr(XQueryParserScripting.FlworExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#initialClause}.
	 * @param ctx the parse tree
	 */
	void enterInitialClause(XQueryParserScripting.InitialClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#initialClause}.
	 * @param ctx the parse tree
	 */
	void exitInitialClause(XQueryParserScripting.InitialClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#intermediateClause}.
	 * @param ctx the parse tree
	 */
	void enterIntermediateClause(XQueryParserScripting.IntermediateClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#intermediateClause}.
	 * @param ctx the parse tree
	 */
	void exitIntermediateClause(XQueryParserScripting.IntermediateClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#forClause}.
	 * @param ctx the parse tree
	 */
	void enterForClause(XQueryParserScripting.ForClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#forClause}.
	 * @param ctx the parse tree
	 */
	void exitForClause(XQueryParserScripting.ForClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#forBinding}.
	 * @param ctx the parse tree
	 */
	void enterForBinding(XQueryParserScripting.ForBindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#forBinding}.
	 * @param ctx the parse tree
	 */
	void exitForBinding(XQueryParserScripting.ForBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#allowingEmpty}.
	 * @param ctx the parse tree
	 */
	void enterAllowingEmpty(XQueryParserScripting.AllowingEmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#allowingEmpty}.
	 * @param ctx the parse tree
	 */
	void exitAllowingEmpty(XQueryParserScripting.AllowingEmptyContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#positionalVar}.
	 * @param ctx the parse tree
	 */
	void enterPositionalVar(XQueryParserScripting.PositionalVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#positionalVar}.
	 * @param ctx the parse tree
	 */
	void exitPositionalVar(XQueryParserScripting.PositionalVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#letClause}.
	 * @param ctx the parse tree
	 */
	void enterLetClause(XQueryParserScripting.LetClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#letClause}.
	 * @param ctx the parse tree
	 */
	void exitLetClause(XQueryParserScripting.LetClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#letBinding}.
	 * @param ctx the parse tree
	 */
	void enterLetBinding(XQueryParserScripting.LetBindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#letBinding}.
	 * @param ctx the parse tree
	 */
	void exitLetBinding(XQueryParserScripting.LetBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#windowClause}.
	 * @param ctx the parse tree
	 */
	void enterWindowClause(XQueryParserScripting.WindowClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#windowClause}.
	 * @param ctx the parse tree
	 */
	void exitWindowClause(XQueryParserScripting.WindowClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#tumblingWindowClause}.
	 * @param ctx the parse tree
	 */
	void enterTumblingWindowClause(XQueryParserScripting.TumblingWindowClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#tumblingWindowClause}.
	 * @param ctx the parse tree
	 */
	void exitTumblingWindowClause(XQueryParserScripting.TumblingWindowClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#slidingWindowClause}.
	 * @param ctx the parse tree
	 */
	void enterSlidingWindowClause(XQueryParserScripting.SlidingWindowClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#slidingWindowClause}.
	 * @param ctx the parse tree
	 */
	void exitSlidingWindowClause(XQueryParserScripting.SlidingWindowClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#windowStartCondition}.
	 * @param ctx the parse tree
	 */
	void enterWindowStartCondition(XQueryParserScripting.WindowStartConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#windowStartCondition}.
	 * @param ctx the parse tree
	 */
	void exitWindowStartCondition(XQueryParserScripting.WindowStartConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#windowEndCondition}.
	 * @param ctx the parse tree
	 */
	void enterWindowEndCondition(XQueryParserScripting.WindowEndConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#windowEndCondition}.
	 * @param ctx the parse tree
	 */
	void exitWindowEndCondition(XQueryParserScripting.WindowEndConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#windowVars}.
	 * @param ctx the parse tree
	 */
	void enterWindowVars(XQueryParserScripting.WindowVarsContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#windowVars}.
	 * @param ctx the parse tree
	 */
	void exitWindowVars(XQueryParserScripting.WindowVarsContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#countClause}.
	 * @param ctx the parse tree
	 */
	void enterCountClause(XQueryParserScripting.CountClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#countClause}.
	 * @param ctx the parse tree
	 */
	void exitCountClause(XQueryParserScripting.CountClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(XQueryParserScripting.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(XQueryParserScripting.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#groupByClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupByClause(XQueryParserScripting.GroupByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#groupByClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupByClause(XQueryParserScripting.GroupByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#groupingSpecList}.
	 * @param ctx the parse tree
	 */
	void enterGroupingSpecList(XQueryParserScripting.GroupingSpecListContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#groupingSpecList}.
	 * @param ctx the parse tree
	 */
	void exitGroupingSpecList(XQueryParserScripting.GroupingSpecListContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#groupingSpec}.
	 * @param ctx the parse tree
	 */
	void enterGroupingSpec(XQueryParserScripting.GroupingSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#groupingSpec}.
	 * @param ctx the parse tree
	 */
	void exitGroupingSpec(XQueryParserScripting.GroupingSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(XQueryParserScripting.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(XQueryParserScripting.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#orderSpec}.
	 * @param ctx the parse tree
	 */
	void enterOrderSpec(XQueryParserScripting.OrderSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#orderSpec}.
	 * @param ctx the parse tree
	 */
	void exitOrderSpec(XQueryParserScripting.OrderSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#returnClause}.
	 * @param ctx the parse tree
	 */
	void enterReturnClause(XQueryParserScripting.ReturnClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#returnClause}.
	 * @param ctx the parse tree
	 */
	void exitReturnClause(XQueryParserScripting.ReturnClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#quantifiedExpr}.
	 * @param ctx the parse tree
	 */
	void enterQuantifiedExpr(XQueryParserScripting.QuantifiedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#quantifiedExpr}.
	 * @param ctx the parse tree
	 */
	void exitQuantifiedExpr(XQueryParserScripting.QuantifiedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#quantifiedVar}.
	 * @param ctx the parse tree
	 */
	void enterQuantifiedVar(XQueryParserScripting.QuantifiedVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#quantifiedVar}.
	 * @param ctx the parse tree
	 */
	void exitQuantifiedVar(XQueryParserScripting.QuantifiedVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#switchExpr}.
	 * @param ctx the parse tree
	 */
	void enterSwitchExpr(XQueryParserScripting.SwitchExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#switchExpr}.
	 * @param ctx the parse tree
	 */
	void exitSwitchExpr(XQueryParserScripting.SwitchExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#switchCaseClause}.
	 * @param ctx the parse tree
	 */
	void enterSwitchCaseClause(XQueryParserScripting.SwitchCaseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#switchCaseClause}.
	 * @param ctx the parse tree
	 */
	void exitSwitchCaseClause(XQueryParserScripting.SwitchCaseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#switchCaseOperand}.
	 * @param ctx the parse tree
	 */
	void enterSwitchCaseOperand(XQueryParserScripting.SwitchCaseOperandContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#switchCaseOperand}.
	 * @param ctx the parse tree
	 */
	void exitSwitchCaseOperand(XQueryParserScripting.SwitchCaseOperandContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#typeswitchExpr}.
	 * @param ctx the parse tree
	 */
	void enterTypeswitchExpr(XQueryParserScripting.TypeswitchExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#typeswitchExpr}.
	 * @param ctx the parse tree
	 */
	void exitTypeswitchExpr(XQueryParserScripting.TypeswitchExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#caseClause}.
	 * @param ctx the parse tree
	 */
	void enterCaseClause(XQueryParserScripting.CaseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#caseClause}.
	 * @param ctx the parse tree
	 */
	void exitCaseClause(XQueryParserScripting.CaseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#sequenceUnionType}.
	 * @param ctx the parse tree
	 */
	void enterSequenceUnionType(XQueryParserScripting.SequenceUnionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#sequenceUnionType}.
	 * @param ctx the parse tree
	 */
	void exitSequenceUnionType(XQueryParserScripting.SequenceUnionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#ifExpr}.
	 * @param ctx the parse tree
	 */
	void enterIfExpr(XQueryParserScripting.IfExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#ifExpr}.
	 * @param ctx the parse tree
	 */
	void exitIfExpr(XQueryParserScripting.IfExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#tryCatchExpr}.
	 * @param ctx the parse tree
	 */
	void enterTryCatchExpr(XQueryParserScripting.TryCatchExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#tryCatchExpr}.
	 * @param ctx the parse tree
	 */
	void exitTryCatchExpr(XQueryParserScripting.TryCatchExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#tryClause}.
	 * @param ctx the parse tree
	 */
	void enterTryClause(XQueryParserScripting.TryClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#tryClause}.
	 * @param ctx the parse tree
	 */
	void exitTryClause(XQueryParserScripting.TryClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#enclosedTryTargetExpression}.
	 * @param ctx the parse tree
	 */
	void enterEnclosedTryTargetExpression(XQueryParserScripting.EnclosedTryTargetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#enclosedTryTargetExpression}.
	 * @param ctx the parse tree
	 */
	void exitEnclosedTryTargetExpression(XQueryParserScripting.EnclosedTryTargetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(XQueryParserScripting.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(XQueryParserScripting.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#enclosedExpression}.
	 * @param ctx the parse tree
	 */
	void enterEnclosedExpression(XQueryParserScripting.EnclosedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#enclosedExpression}.
	 * @param ctx the parse tree
	 */
	void exitEnclosedExpression(XQueryParserScripting.EnclosedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#catchErrorList}.
	 * @param ctx the parse tree
	 */
	void enterCatchErrorList(XQueryParserScripting.CatchErrorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#catchErrorList}.
	 * @param ctx the parse tree
	 */
	void exitCatchErrorList(XQueryParserScripting.CatchErrorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#existUpdateExpr}.
	 * @param ctx the parse tree
	 */
	void enterExistUpdateExpr(XQueryParserScripting.ExistUpdateExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#existUpdateExpr}.
	 * @param ctx the parse tree
	 */
	void exitExistUpdateExpr(XQueryParserScripting.ExistUpdateExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#existReplaceExpr}.
	 * @param ctx the parse tree
	 */
	void enterExistReplaceExpr(XQueryParserScripting.ExistReplaceExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#existReplaceExpr}.
	 * @param ctx the parse tree
	 */
	void exitExistReplaceExpr(XQueryParserScripting.ExistReplaceExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#existValueExpr}.
	 * @param ctx the parse tree
	 */
	void enterExistValueExpr(XQueryParserScripting.ExistValueExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#existValueExpr}.
	 * @param ctx the parse tree
	 */
	void exitExistValueExpr(XQueryParserScripting.ExistValueExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#existInsertExpr}.
	 * @param ctx the parse tree
	 */
	void enterExistInsertExpr(XQueryParserScripting.ExistInsertExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#existInsertExpr}.
	 * @param ctx the parse tree
	 */
	void exitExistInsertExpr(XQueryParserScripting.ExistInsertExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#existDeleteExpr}.
	 * @param ctx the parse tree
	 */
	void enterExistDeleteExpr(XQueryParserScripting.ExistDeleteExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#existDeleteExpr}.
	 * @param ctx the parse tree
	 */
	void exitExistDeleteExpr(XQueryParserScripting.ExistDeleteExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#existRenameExpr}.
	 * @param ctx the parse tree
	 */
	void enterExistRenameExpr(XQueryParserScripting.ExistRenameExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#existRenameExpr}.
	 * @param ctx the parse tree
	 */
	void exitExistRenameExpr(XQueryParserScripting.ExistRenameExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(XQueryParserScripting.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(XQueryParserScripting.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(XQueryParserScripting.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(XQueryParserScripting.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpr(XQueryParserScripting.ComparisonExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpr(XQueryParserScripting.ComparisonExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringConcatExpr}.
	 * @param ctx the parse tree
	 */
	void enterStringConcatExpr(XQueryParserScripting.StringConcatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringConcatExpr}.
	 * @param ctx the parse tree
	 */
	void exitStringConcatExpr(XQueryParserScripting.StringConcatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#rangeExpr}.
	 * @param ctx the parse tree
	 */
	void enterRangeExpr(XQueryParserScripting.RangeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#rangeExpr}.
	 * @param ctx the parse tree
	 */
	void exitRangeExpr(XQueryParserScripting.RangeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpr(XQueryParserScripting.AdditiveExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpr(XQueryParserScripting.AdditiveExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#multiplicativeExpr}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpr(XQueryParserScripting.MultiplicativeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#multiplicativeExpr}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpr(XQueryParserScripting.MultiplicativeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#unionExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnionExpr(XQueryParserScripting.UnionExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#unionExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnionExpr(XQueryParserScripting.UnionExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#intersectExceptExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntersectExceptExpr(XQueryParserScripting.IntersectExceptExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#intersectExceptExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntersectExceptExpr(XQueryParserScripting.IntersectExceptExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#instanceOfExpr}.
	 * @param ctx the parse tree
	 */
	void enterInstanceOfExpr(XQueryParserScripting.InstanceOfExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#instanceOfExpr}.
	 * @param ctx the parse tree
	 */
	void exitInstanceOfExpr(XQueryParserScripting.InstanceOfExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#treatExpr}.
	 * @param ctx the parse tree
	 */
	void enterTreatExpr(XQueryParserScripting.TreatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#treatExpr}.
	 * @param ctx the parse tree
	 */
	void exitTreatExpr(XQueryParserScripting.TreatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#castableExpr}.
	 * @param ctx the parse tree
	 */
	void enterCastableExpr(XQueryParserScripting.CastableExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#castableExpr}.
	 * @param ctx the parse tree
	 */
	void exitCastableExpr(XQueryParserScripting.CastableExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#castExpr}.
	 * @param ctx the parse tree
	 */
	void enterCastExpr(XQueryParserScripting.CastExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#castExpr}.
	 * @param ctx the parse tree
	 */
	void exitCastExpr(XQueryParserScripting.CastExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#arrowExpr}.
	 * @param ctx the parse tree
	 */
	void enterArrowExpr(XQueryParserScripting.ArrowExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#arrowExpr}.
	 * @param ctx the parse tree
	 */
	void exitArrowExpr(XQueryParserScripting.ArrowExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#complexArrow}.
	 * @param ctx the parse tree
	 */
	void enterComplexArrow(XQueryParserScripting.ComplexArrowContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#complexArrow}.
	 * @param ctx the parse tree
	 */
	void exitComplexArrow(XQueryParserScripting.ComplexArrowContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(XQueryParserScripting.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(XQueryParserScripting.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#valueExpr}.
	 * @param ctx the parse tree
	 */
	void enterValueExpr(XQueryParserScripting.ValueExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#valueExpr}.
	 * @param ctx the parse tree
	 */
	void exitValueExpr(XQueryParserScripting.ValueExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#generalComp}.
	 * @param ctx the parse tree
	 */
	void enterGeneralComp(XQueryParserScripting.GeneralCompContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#generalComp}.
	 * @param ctx the parse tree
	 */
	void exitGeneralComp(XQueryParserScripting.GeneralCompContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#valueComp}.
	 * @param ctx the parse tree
	 */
	void enterValueComp(XQueryParserScripting.ValueCompContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#valueComp}.
	 * @param ctx the parse tree
	 */
	void exitValueComp(XQueryParserScripting.ValueCompContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#nodeComp}.
	 * @param ctx the parse tree
	 */
	void enterNodeComp(XQueryParserScripting.NodeCompContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#nodeComp}.
	 * @param ctx the parse tree
	 */
	void exitNodeComp(XQueryParserScripting.NodeCompContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#validateExpr}.
	 * @param ctx the parse tree
	 */
	void enterValidateExpr(XQueryParserScripting.ValidateExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#validateExpr}.
	 * @param ctx the parse tree
	 */
	void exitValidateExpr(XQueryParserScripting.ValidateExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#validationMode}.
	 * @param ctx the parse tree
	 */
	void enterValidationMode(XQueryParserScripting.ValidationModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#validationMode}.
	 * @param ctx the parse tree
	 */
	void exitValidationMode(XQueryParserScripting.ValidationModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#extensionExpr}.
	 * @param ctx the parse tree
	 */
	void enterExtensionExpr(XQueryParserScripting.ExtensionExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#extensionExpr}.
	 * @param ctx the parse tree
	 */
	void exitExtensionExpr(XQueryParserScripting.ExtensionExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#simpleMapExpr}.
	 * @param ctx the parse tree
	 */
	void enterSimpleMapExpr(XQueryParserScripting.SimpleMapExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#simpleMapExpr}.
	 * @param ctx the parse tree
	 */
	void exitSimpleMapExpr(XQueryParserScripting.SimpleMapExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(XQueryParserScripting.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(XQueryParserScripting.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#applyStatement}.
	 * @param ctx the parse tree
	 */
	void enterApplyStatement(XQueryParserScripting.ApplyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#applyStatement}.
	 * @param ctx the parse tree
	 */
	void exitApplyStatement(XQueryParserScripting.ApplyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#assignStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignStatement(XQueryParserScripting.AssignStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#assignStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignStatement(XQueryParserScripting.AssignStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(XQueryParserScripting.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(XQueryParserScripting.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(XQueryParserScripting.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(XQueryParserScripting.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(XQueryParserScripting.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(XQueryParserScripting.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#exitStatement}.
	 * @param ctx the parse tree
	 */
	void enterExitStatement(XQueryParserScripting.ExitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#exitStatement}.
	 * @param ctx the parse tree
	 */
	void exitExitStatement(XQueryParserScripting.ExitStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#flworStatement}.
	 * @param ctx the parse tree
	 */
	void enterFlworStatement(XQueryParserScripting.FlworStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#flworStatement}.
	 * @param ctx the parse tree
	 */
	void exitFlworStatement(XQueryParserScripting.FlworStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(XQueryParserScripting.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(XQueryParserScripting.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(XQueryParserScripting.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(XQueryParserScripting.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#switchStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(XQueryParserScripting.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#switchStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(XQueryParserScripting.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#switchCaseStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchCaseStatement(XQueryParserScripting.SwitchCaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#switchCaseStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchCaseStatement(XQueryParserScripting.SwitchCaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryCatchStatement(XQueryParserScripting.TryCatchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryCatchStatement(XQueryParserScripting.TryCatchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#typeswitchStatement}.
	 * @param ctx the parse tree
	 */
	void enterTypeswitchStatement(XQueryParserScripting.TypeswitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#typeswitchStatement}.
	 * @param ctx the parse tree
	 */
	void exitTypeswitchStatement(XQueryParserScripting.TypeswitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(XQueryParserScripting.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(XQueryParserScripting.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#varDeclStatement}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclStatement(XQueryParserScripting.VarDeclStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#varDeclStatement}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclStatement(XQueryParserScripting.VarDeclStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(XQueryParserScripting.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(XQueryParserScripting.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#pathExpr}.
	 * @param ctx the parse tree
	 */
	void enterPathExpr(XQueryParserScripting.PathExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#pathExpr}.
	 * @param ctx the parse tree
	 */
	void exitPathExpr(XQueryParserScripting.PathExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#relativePathExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelativePathExpr(XQueryParserScripting.RelativePathExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#relativePathExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelativePathExpr(XQueryParserScripting.RelativePathExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stepExpr}.
	 * @param ctx the parse tree
	 */
	void enterStepExpr(XQueryParserScripting.StepExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stepExpr}.
	 * @param ctx the parse tree
	 */
	void exitStepExpr(XQueryParserScripting.StepExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#axisStep}.
	 * @param ctx the parse tree
	 */
	void enterAxisStep(XQueryParserScripting.AxisStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#axisStep}.
	 * @param ctx the parse tree
	 */
	void exitAxisStep(XQueryParserScripting.AxisStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#forwardStep}.
	 * @param ctx the parse tree
	 */
	void enterForwardStep(XQueryParserScripting.ForwardStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#forwardStep}.
	 * @param ctx the parse tree
	 */
	void exitForwardStep(XQueryParserScripting.ForwardStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#forwardAxis}.
	 * @param ctx the parse tree
	 */
	void enterForwardAxis(XQueryParserScripting.ForwardAxisContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#forwardAxis}.
	 * @param ctx the parse tree
	 */
	void exitForwardAxis(XQueryParserScripting.ForwardAxisContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#abbrevForwardStep}.
	 * @param ctx the parse tree
	 */
	void enterAbbrevForwardStep(XQueryParserScripting.AbbrevForwardStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#abbrevForwardStep}.
	 * @param ctx the parse tree
	 */
	void exitAbbrevForwardStep(XQueryParserScripting.AbbrevForwardStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#reverseStep}.
	 * @param ctx the parse tree
	 */
	void enterReverseStep(XQueryParserScripting.ReverseStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#reverseStep}.
	 * @param ctx the parse tree
	 */
	void exitReverseStep(XQueryParserScripting.ReverseStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#reverseAxis}.
	 * @param ctx the parse tree
	 */
	void enterReverseAxis(XQueryParserScripting.ReverseAxisContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#reverseAxis}.
	 * @param ctx the parse tree
	 */
	void exitReverseAxis(XQueryParserScripting.ReverseAxisContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#abbrevReverseStep}.
	 * @param ctx the parse tree
	 */
	void enterAbbrevReverseStep(XQueryParserScripting.AbbrevReverseStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#abbrevReverseStep}.
	 * @param ctx the parse tree
	 */
	void exitAbbrevReverseStep(XQueryParserScripting.AbbrevReverseStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#nodeTest}.
	 * @param ctx the parse tree
	 */
	void enterNodeTest(XQueryParserScripting.NodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#nodeTest}.
	 * @param ctx the parse tree
	 */
	void exitNodeTest(XQueryParserScripting.NodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#nameTest}.
	 * @param ctx the parse tree
	 */
	void enterNameTest(XQueryParserScripting.NameTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#nameTest}.
	 * @param ctx the parse tree
	 */
	void exitNameTest(XQueryParserScripting.NameTestContext ctx);
	/**
	 * Enter a parse tree produced by the {@code allNames}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterAllNames(XQueryParserScripting.AllNamesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code allNames}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitAllNames(XQueryParserScripting.AllNamesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code allWithNS}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterAllWithNS(XQueryParserScripting.AllWithNSContext ctx);
	/**
	 * Exit a parse tree produced by the {@code allWithNS}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitAllWithNS(XQueryParserScripting.AllWithNSContext ctx);
	/**
	 * Enter a parse tree produced by the {@code allWithLocal}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterAllWithLocal(XQueryParserScripting.AllWithLocalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code allWithLocal}
	 * labeled alternative in {@link XQueryParserScripting#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitAllWithLocal(XQueryParserScripting.AllWithLocalContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#postfixExpr}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpr(XQueryParserScripting.PostfixExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#postfixExpr}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpr(XQueryParserScripting.PostfixExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(XQueryParserScripting.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(XQueryParserScripting.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#predicateList}.
	 * @param ctx the parse tree
	 */
	void enterPredicateList(XQueryParserScripting.PredicateListContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#predicateList}.
	 * @param ctx the parse tree
	 */
	void exitPredicateList(XQueryParserScripting.PredicateListContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(XQueryParserScripting.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(XQueryParserScripting.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#lookup}.
	 * @param ctx the parse tree
	 */
	void enterLookup(XQueryParserScripting.LookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#lookup}.
	 * @param ctx the parse tree
	 */
	void exitLookup(XQueryParserScripting.LookupContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#keySpecifier}.
	 * @param ctx the parse tree
	 */
	void enterKeySpecifier(XQueryParserScripting.KeySpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#keySpecifier}.
	 * @param ctx the parse tree
	 */
	void exitKeySpecifier(XQueryParserScripting.KeySpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#arrowFunctionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterArrowFunctionSpecifier(XQueryParserScripting.ArrowFunctionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#arrowFunctionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitArrowFunctionSpecifier(XQueryParserScripting.ArrowFunctionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(XQueryParserScripting.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(XQueryParserScripting.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(XQueryParserScripting.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(XQueryParserScripting.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(XQueryParserScripting.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(XQueryParserScripting.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#varRef}.
	 * @param ctx the parse tree
	 */
	void enterVarRef(XQueryParserScripting.VarRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#varRef}.
	 * @param ctx the parse tree
	 */
	void exitVarRef(XQueryParserScripting.VarRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#varName}.
	 * @param ctx the parse tree
	 */
	void enterVarName(XQueryParserScripting.VarNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#varName}.
	 * @param ctx the parse tree
	 */
	void exitVarName(XQueryParserScripting.VarNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#parenthesizedExpr}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpr(XQueryParserScripting.ParenthesizedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#parenthesizedExpr}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpr(XQueryParserScripting.ParenthesizedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#contextItemExpr}.
	 * @param ctx the parse tree
	 */
	void enterContextItemExpr(XQueryParserScripting.ContextItemExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#contextItemExpr}.
	 * @param ctx the parse tree
	 */
	void exitContextItemExpr(XQueryParserScripting.ContextItemExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#orderedExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrderedExpr(XQueryParserScripting.OrderedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#orderedExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrderedExpr(XQueryParserScripting.OrderedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#unorderedExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnorderedExpr(XQueryParserScripting.UnorderedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#unorderedExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnorderedExpr(XQueryParserScripting.UnorderedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(XQueryParserScripting.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(XQueryParserScripting.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(XQueryParserScripting.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(XQueryParserScripting.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#nodeConstructor}.
	 * @param ctx the parse tree
	 */
	void enterNodeConstructor(XQueryParserScripting.NodeConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#nodeConstructor}.
	 * @param ctx the parse tree
	 */
	void exitNodeConstructor(XQueryParserScripting.NodeConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#directConstructor}.
	 * @param ctx the parse tree
	 */
	void enterDirectConstructor(XQueryParserScripting.DirectConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#directConstructor}.
	 * @param ctx the parse tree
	 */
	void exitDirectConstructor(XQueryParserScripting.DirectConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirElemConstructorOpenClose}.
	 * @param ctx the parse tree
	 */
	void enterDirElemConstructorOpenClose(XQueryParserScripting.DirElemConstructorOpenCloseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirElemConstructorOpenClose}.
	 * @param ctx the parse tree
	 */
	void exitDirElemConstructorOpenClose(XQueryParserScripting.DirElemConstructorOpenCloseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirElemConstructorSingleTag}.
	 * @param ctx the parse tree
	 */
	void enterDirElemConstructorSingleTag(XQueryParserScripting.DirElemConstructorSingleTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirElemConstructorSingleTag}.
	 * @param ctx the parse tree
	 */
	void exitDirElemConstructorSingleTag(XQueryParserScripting.DirElemConstructorSingleTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirAttributeList}.
	 * @param ctx the parse tree
	 */
	void enterDirAttributeList(XQueryParserScripting.DirAttributeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirAttributeList}.
	 * @param ctx the parse tree
	 */
	void exitDirAttributeList(XQueryParserScripting.DirAttributeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirAttributeValueApos}.
	 * @param ctx the parse tree
	 */
	void enterDirAttributeValueApos(XQueryParserScripting.DirAttributeValueAposContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirAttributeValueApos}.
	 * @param ctx the parse tree
	 */
	void exitDirAttributeValueApos(XQueryParserScripting.DirAttributeValueAposContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirAttributeValueQuot}.
	 * @param ctx the parse tree
	 */
	void enterDirAttributeValueQuot(XQueryParserScripting.DirAttributeValueQuotContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirAttributeValueQuot}.
	 * @param ctx the parse tree
	 */
	void exitDirAttributeValueQuot(XQueryParserScripting.DirAttributeValueQuotContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirAttributeValue}.
	 * @param ctx the parse tree
	 */
	void enterDirAttributeValue(XQueryParserScripting.DirAttributeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirAttributeValue}.
	 * @param ctx the parse tree
	 */
	void exitDirAttributeValue(XQueryParserScripting.DirAttributeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirAttributeContentQuot}.
	 * @param ctx the parse tree
	 */
	void enterDirAttributeContentQuot(XQueryParserScripting.DirAttributeContentQuotContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirAttributeContentQuot}.
	 * @param ctx the parse tree
	 */
	void exitDirAttributeContentQuot(XQueryParserScripting.DirAttributeContentQuotContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirAttributeContentApos}.
	 * @param ctx the parse tree
	 */
	void enterDirAttributeContentApos(XQueryParserScripting.DirAttributeContentAposContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirAttributeContentApos}.
	 * @param ctx the parse tree
	 */
	void exitDirAttributeContentApos(XQueryParserScripting.DirAttributeContentAposContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#dirElemContent}.
	 * @param ctx the parse tree
	 */
	void enterDirElemContent(XQueryParserScripting.DirElemContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#dirElemContent}.
	 * @param ctx the parse tree
	 */
	void exitDirElemContent(XQueryParserScripting.DirElemContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#commonContent}.
	 * @param ctx the parse tree
	 */
	void enterCommonContent(XQueryParserScripting.CommonContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#commonContent}.
	 * @param ctx the parse tree
	 */
	void exitCommonContent(XQueryParserScripting.CommonContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#contentExpr}.
	 * @param ctx the parse tree
	 */
	void enterContentExpr(XQueryParserScripting.ContentExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#contentExpr}.
	 * @param ctx the parse tree
	 */
	void exitContentExpr(XQueryParserScripting.ContentExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#computedConstructor}.
	 * @param ctx the parse tree
	 */
	void enterComputedConstructor(XQueryParserScripting.ComputedConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#computedConstructor}.
	 * @param ctx the parse tree
	 */
	void exitComputedConstructor(XQueryParserScripting.ComputedConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compMLJSONConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompMLJSONConstructor(XQueryParserScripting.CompMLJSONConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compMLJSONConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompMLJSONConstructor(XQueryParserScripting.CompMLJSONConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compMLJSONArrayConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompMLJSONArrayConstructor(XQueryParserScripting.CompMLJSONArrayConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compMLJSONArrayConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompMLJSONArrayConstructor(XQueryParserScripting.CompMLJSONArrayConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compMLJSONObjectConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompMLJSONObjectConstructor(XQueryParserScripting.CompMLJSONObjectConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compMLJSONObjectConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompMLJSONObjectConstructor(XQueryParserScripting.CompMLJSONObjectConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compMLJSONNumberConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompMLJSONNumberConstructor(XQueryParserScripting.CompMLJSONNumberConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compMLJSONNumberConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompMLJSONNumberConstructor(XQueryParserScripting.CompMLJSONNumberConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compMLJSONBooleanConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompMLJSONBooleanConstructor(XQueryParserScripting.CompMLJSONBooleanConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compMLJSONBooleanConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompMLJSONBooleanConstructor(XQueryParserScripting.CompMLJSONBooleanConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compMLJSONNullConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompMLJSONNullConstructor(XQueryParserScripting.CompMLJSONNullConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compMLJSONNullConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompMLJSONNullConstructor(XQueryParserScripting.CompMLJSONNullConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compBinaryConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompBinaryConstructor(XQueryParserScripting.CompBinaryConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compBinaryConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompBinaryConstructor(XQueryParserScripting.CompBinaryConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compDocConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompDocConstructor(XQueryParserScripting.CompDocConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compDocConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompDocConstructor(XQueryParserScripting.CompDocConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compElemConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompElemConstructor(XQueryParserScripting.CompElemConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compElemConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompElemConstructor(XQueryParserScripting.CompElemConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#enclosedContentExpr}.
	 * @param ctx the parse tree
	 */
	void enterEnclosedContentExpr(XQueryParserScripting.EnclosedContentExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#enclosedContentExpr}.
	 * @param ctx the parse tree
	 */
	void exitEnclosedContentExpr(XQueryParserScripting.EnclosedContentExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compAttrConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompAttrConstructor(XQueryParserScripting.CompAttrConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compAttrConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompAttrConstructor(XQueryParserScripting.CompAttrConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compNamespaceConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompNamespaceConstructor(XQueryParserScripting.CompNamespaceConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compNamespaceConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompNamespaceConstructor(XQueryParserScripting.CompNamespaceConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#prefix}.
	 * @param ctx the parse tree
	 */
	void enterPrefix(XQueryParserScripting.PrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#prefix}.
	 * @param ctx the parse tree
	 */
	void exitPrefix(XQueryParserScripting.PrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#enclosedPrefixExpr}.
	 * @param ctx the parse tree
	 */
	void enterEnclosedPrefixExpr(XQueryParserScripting.EnclosedPrefixExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#enclosedPrefixExpr}.
	 * @param ctx the parse tree
	 */
	void exitEnclosedPrefixExpr(XQueryParserScripting.EnclosedPrefixExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#enclosedURIExpr}.
	 * @param ctx the parse tree
	 */
	void enterEnclosedURIExpr(XQueryParserScripting.EnclosedURIExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#enclosedURIExpr}.
	 * @param ctx the parse tree
	 */
	void exitEnclosedURIExpr(XQueryParserScripting.EnclosedURIExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compTextConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompTextConstructor(XQueryParserScripting.CompTextConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compTextConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompTextConstructor(XQueryParserScripting.CompTextConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compCommentConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompCommentConstructor(XQueryParserScripting.CompCommentConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compCommentConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompCommentConstructor(XQueryParserScripting.CompCommentConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#compPIConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCompPIConstructor(XQueryParserScripting.CompPIConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#compPIConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCompPIConstructor(XQueryParserScripting.CompPIConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionItemExpr}.
	 * @param ctx the parse tree
	 */
	void enterFunctionItemExpr(XQueryParserScripting.FunctionItemExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionItemExpr}.
	 * @param ctx the parse tree
	 */
	void exitFunctionItemExpr(XQueryParserScripting.FunctionItemExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#namedFunctionRef}.
	 * @param ctx the parse tree
	 */
	void enterNamedFunctionRef(XQueryParserScripting.NamedFunctionRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#namedFunctionRef}.
	 * @param ctx the parse tree
	 */
	void exitNamedFunctionRef(XQueryParserScripting.NamedFunctionRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#inlineFunctionRef}.
	 * @param ctx the parse tree
	 */
	void enterInlineFunctionRef(XQueryParserScripting.InlineFunctionRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#inlineFunctionRef}.
	 * @param ctx the parse tree
	 */
	void exitInlineFunctionRef(XQueryParserScripting.InlineFunctionRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(XQueryParserScripting.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(XQueryParserScripting.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mapConstructor}.
	 * @param ctx the parse tree
	 */
	void enterMapConstructor(XQueryParserScripting.MapConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mapConstructor}.
	 * @param ctx the parse tree
	 */
	void exitMapConstructor(XQueryParserScripting.MapConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mapConstructorEntry}.
	 * @param ctx the parse tree
	 */
	void enterMapConstructorEntry(XQueryParserScripting.MapConstructorEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mapConstructorEntry}.
	 * @param ctx the parse tree
	 */
	void exitMapConstructorEntry(XQueryParserScripting.MapConstructorEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#arrayConstructor}.
	 * @param ctx the parse tree
	 */
	void enterArrayConstructor(XQueryParserScripting.ArrayConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#arrayConstructor}.
	 * @param ctx the parse tree
	 */
	void exitArrayConstructor(XQueryParserScripting.ArrayConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#squareArrayConstructor}.
	 * @param ctx the parse tree
	 */
	void enterSquareArrayConstructor(XQueryParserScripting.SquareArrayConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#squareArrayConstructor}.
	 * @param ctx the parse tree
	 */
	void exitSquareArrayConstructor(XQueryParserScripting.SquareArrayConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#curlyArrayConstructor}.
	 * @param ctx the parse tree
	 */
	void enterCurlyArrayConstructor(XQueryParserScripting.CurlyArrayConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#curlyArrayConstructor}.
	 * @param ctx the parse tree
	 */
	void exitCurlyArrayConstructor(XQueryParserScripting.CurlyArrayConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringConstructor}.
	 * @param ctx the parse tree
	 */
	void enterStringConstructor(XQueryParserScripting.StringConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringConstructor}.
	 * @param ctx the parse tree
	 */
	void exitStringConstructor(XQueryParserScripting.StringConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringConstructorContent}.
	 * @param ctx the parse tree
	 */
	void enterStringConstructorContent(XQueryParserScripting.StringConstructorContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringConstructorContent}.
	 * @param ctx the parse tree
	 */
	void exitStringConstructorContent(XQueryParserScripting.StringConstructorContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#charNoGrave}.
	 * @param ctx the parse tree
	 */
	void enterCharNoGrave(XQueryParserScripting.CharNoGraveContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#charNoGrave}.
	 * @param ctx the parse tree
	 */
	void exitCharNoGrave(XQueryParserScripting.CharNoGraveContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#charNoLBrace}.
	 * @param ctx the parse tree
	 */
	void enterCharNoLBrace(XQueryParserScripting.CharNoLBraceContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#charNoLBrace}.
	 * @param ctx the parse tree
	 */
	void exitCharNoLBrace(XQueryParserScripting.CharNoLBraceContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#charNoRBrack}.
	 * @param ctx the parse tree
	 */
	void enterCharNoRBrack(XQueryParserScripting.CharNoRBrackContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#charNoRBrack}.
	 * @param ctx the parse tree
	 */
	void exitCharNoRBrack(XQueryParserScripting.CharNoRBrackContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringConstructorChars}.
	 * @param ctx the parse tree
	 */
	void enterStringConstructorChars(XQueryParserScripting.StringConstructorCharsContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringConstructorChars}.
	 * @param ctx the parse tree
	 */
	void exitStringConstructorChars(XQueryParserScripting.StringConstructorCharsContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringConstructorInterpolation}.
	 * @param ctx the parse tree
	 */
	void enterStringConstructorInterpolation(XQueryParserScripting.StringConstructorInterpolationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringConstructorInterpolation}.
	 * @param ctx the parse tree
	 */
	void exitStringConstructorInterpolation(XQueryParserScripting.StringConstructorInterpolationContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#unaryLookup}.
	 * @param ctx the parse tree
	 */
	void enterUnaryLookup(XQueryParserScripting.UnaryLookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#unaryLookup}.
	 * @param ctx the parse tree
	 */
	void exitUnaryLookup(XQueryParserScripting.UnaryLookupContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#singleType}.
	 * @param ctx the parse tree
	 */
	void enterSingleType(XQueryParserScripting.SingleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#singleType}.
	 * @param ctx the parse tree
	 */
	void exitSingleType(XQueryParserScripting.SingleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(XQueryParserScripting.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(XQueryParserScripting.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#sequenceType}.
	 * @param ctx the parse tree
	 */
	void enterSequenceType(XQueryParserScripting.SequenceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#sequenceType}.
	 * @param ctx the parse tree
	 */
	void exitSequenceType(XQueryParserScripting.SequenceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#itemType}.
	 * @param ctx the parse tree
	 */
	void enterItemType(XQueryParserScripting.ItemTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#itemType}.
	 * @param ctx the parse tree
	 */
	void exitItemType(XQueryParserScripting.ItemTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#atomicOrUnionType}.
	 * @param ctx the parse tree
	 */
	void enterAtomicOrUnionType(XQueryParserScripting.AtomicOrUnionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#atomicOrUnionType}.
	 * @param ctx the parse tree
	 */
	void exitAtomicOrUnionType(XQueryParserScripting.AtomicOrUnionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#kindTest}.
	 * @param ctx the parse tree
	 */
	void enterKindTest(XQueryParserScripting.KindTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#kindTest}.
	 * @param ctx the parse tree
	 */
	void exitKindTest(XQueryParserScripting.KindTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#anyKindTest}.
	 * @param ctx the parse tree
	 */
	void enterAnyKindTest(XQueryParserScripting.AnyKindTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#anyKindTest}.
	 * @param ctx the parse tree
	 */
	void exitAnyKindTest(XQueryParserScripting.AnyKindTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#binaryNodeTest}.
	 * @param ctx the parse tree
	 */
	void enterBinaryNodeTest(XQueryParserScripting.BinaryNodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#binaryNodeTest}.
	 * @param ctx the parse tree
	 */
	void exitBinaryNodeTest(XQueryParserScripting.BinaryNodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#documentTest}.
	 * @param ctx the parse tree
	 */
	void enterDocumentTest(XQueryParserScripting.DocumentTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#documentTest}.
	 * @param ctx the parse tree
	 */
	void exitDocumentTest(XQueryParserScripting.DocumentTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#textTest}.
	 * @param ctx the parse tree
	 */
	void enterTextTest(XQueryParserScripting.TextTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#textTest}.
	 * @param ctx the parse tree
	 */
	void exitTextTest(XQueryParserScripting.TextTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#commentTest}.
	 * @param ctx the parse tree
	 */
	void enterCommentTest(XQueryParserScripting.CommentTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#commentTest}.
	 * @param ctx the parse tree
	 */
	void exitCommentTest(XQueryParserScripting.CommentTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#namespaceNodeTest}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceNodeTest(XQueryParserScripting.NamespaceNodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#namespaceNodeTest}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceNodeTest(XQueryParserScripting.NamespaceNodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#piTest}.
	 * @param ctx the parse tree
	 */
	void enterPiTest(XQueryParserScripting.PiTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#piTest}.
	 * @param ctx the parse tree
	 */
	void exitPiTest(XQueryParserScripting.PiTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#attributeTest}.
	 * @param ctx the parse tree
	 */
	void enterAttributeTest(XQueryParserScripting.AttributeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#attributeTest}.
	 * @param ctx the parse tree
	 */
	void exitAttributeTest(XQueryParserScripting.AttributeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#attributeNameOrWildcard}.
	 * @param ctx the parse tree
	 */
	void enterAttributeNameOrWildcard(XQueryParserScripting.AttributeNameOrWildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#attributeNameOrWildcard}.
	 * @param ctx the parse tree
	 */
	void exitAttributeNameOrWildcard(XQueryParserScripting.AttributeNameOrWildcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#schemaAttributeTest}.
	 * @param ctx the parse tree
	 */
	void enterSchemaAttributeTest(XQueryParserScripting.SchemaAttributeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#schemaAttributeTest}.
	 * @param ctx the parse tree
	 */
	void exitSchemaAttributeTest(XQueryParserScripting.SchemaAttributeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#elementTest}.
	 * @param ctx the parse tree
	 */
	void enterElementTest(XQueryParserScripting.ElementTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#elementTest}.
	 * @param ctx the parse tree
	 */
	void exitElementTest(XQueryParserScripting.ElementTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#elementNameOrWildcard}.
	 * @param ctx the parse tree
	 */
	void enterElementNameOrWildcard(XQueryParserScripting.ElementNameOrWildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#elementNameOrWildcard}.
	 * @param ctx the parse tree
	 */
	void exitElementNameOrWildcard(XQueryParserScripting.ElementNameOrWildcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#schemaElementTest}.
	 * @param ctx the parse tree
	 */
	void enterSchemaElementTest(XQueryParserScripting.SchemaElementTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#schemaElementTest}.
	 * @param ctx the parse tree
	 */
	void exitSchemaElementTest(XQueryParserScripting.SchemaElementTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#elementDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterElementDeclaration(XQueryParserScripting.ElementDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#elementDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitElementDeclaration(XQueryParserScripting.ElementDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttributeName(XQueryParserScripting.AttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttributeName(XQueryParserScripting.AttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#elementName}.
	 * @param ctx the parse tree
	 */
	void enterElementName(XQueryParserScripting.ElementNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#elementName}.
	 * @param ctx the parse tree
	 */
	void exitElementName(XQueryParserScripting.ElementNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#simpleTypeName}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTypeName(XQueryParserScripting.SimpleTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#simpleTypeName}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTypeName(XQueryParserScripting.SimpleTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(XQueryParserScripting.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(XQueryParserScripting.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionTest}.
	 * @param ctx the parse tree
	 */
	void enterFunctionTest(XQueryParserScripting.FunctionTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionTest}.
	 * @param ctx the parse tree
	 */
	void exitFunctionTest(XQueryParserScripting.FunctionTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#anyFunctionTest}.
	 * @param ctx the parse tree
	 */
	void enterAnyFunctionTest(XQueryParserScripting.AnyFunctionTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#anyFunctionTest}.
	 * @param ctx the parse tree
	 */
	void exitAnyFunctionTest(XQueryParserScripting.AnyFunctionTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#typedFunctionTest}.
	 * @param ctx the parse tree
	 */
	void enterTypedFunctionTest(XQueryParserScripting.TypedFunctionTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#typedFunctionTest}.
	 * @param ctx the parse tree
	 */
	void exitTypedFunctionTest(XQueryParserScripting.TypedFunctionTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mapTest}.
	 * @param ctx the parse tree
	 */
	void enterMapTest(XQueryParserScripting.MapTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mapTest}.
	 * @param ctx the parse tree
	 */
	void exitMapTest(XQueryParserScripting.MapTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#anyMapTest}.
	 * @param ctx the parse tree
	 */
	void enterAnyMapTest(XQueryParserScripting.AnyMapTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#anyMapTest}.
	 * @param ctx the parse tree
	 */
	void exitAnyMapTest(XQueryParserScripting.AnyMapTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#typedMapTest}.
	 * @param ctx the parse tree
	 */
	void enterTypedMapTest(XQueryParserScripting.TypedMapTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#typedMapTest}.
	 * @param ctx the parse tree
	 */
	void exitTypedMapTest(XQueryParserScripting.TypedMapTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#arrayTest}.
	 * @param ctx the parse tree
	 */
	void enterArrayTest(XQueryParserScripting.ArrayTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#arrayTest}.
	 * @param ctx the parse tree
	 */
	void exitArrayTest(XQueryParserScripting.ArrayTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#anyArrayTest}.
	 * @param ctx the parse tree
	 */
	void enterAnyArrayTest(XQueryParserScripting.AnyArrayTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#anyArrayTest}.
	 * @param ctx the parse tree
	 */
	void exitAnyArrayTest(XQueryParserScripting.AnyArrayTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#typedArrayTest}.
	 * @param ctx the parse tree
	 */
	void enterTypedArrayTest(XQueryParserScripting.TypedArrayTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#typedArrayTest}.
	 * @param ctx the parse tree
	 */
	void exitTypedArrayTest(XQueryParserScripting.TypedArrayTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#parenthesizedItemTest}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedItemTest(XQueryParserScripting.ParenthesizedItemTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#parenthesizedItemTest}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedItemTest(XQueryParserScripting.ParenthesizedItemTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#attributeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAttributeDeclaration(XQueryParserScripting.AttributeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#attributeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAttributeDeclaration(XQueryParserScripting.AttributeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mlNodeTest}.
	 * @param ctx the parse tree
	 */
	void enterMlNodeTest(XQueryParserScripting.MlNodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mlNodeTest}.
	 * @param ctx the parse tree
	 */
	void exitMlNodeTest(XQueryParserScripting.MlNodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mlArrayNodeTest}.
	 * @param ctx the parse tree
	 */
	void enterMlArrayNodeTest(XQueryParserScripting.MlArrayNodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mlArrayNodeTest}.
	 * @param ctx the parse tree
	 */
	void exitMlArrayNodeTest(XQueryParserScripting.MlArrayNodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mlObjectNodeTest}.
	 * @param ctx the parse tree
	 */
	void enterMlObjectNodeTest(XQueryParserScripting.MlObjectNodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mlObjectNodeTest}.
	 * @param ctx the parse tree
	 */
	void exitMlObjectNodeTest(XQueryParserScripting.MlObjectNodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mlNumberNodeTest}.
	 * @param ctx the parse tree
	 */
	void enterMlNumberNodeTest(XQueryParserScripting.MlNumberNodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mlNumberNodeTest}.
	 * @param ctx the parse tree
	 */
	void exitMlNumberNodeTest(XQueryParserScripting.MlNumberNodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mlBooleanNodeTest}.
	 * @param ctx the parse tree
	 */
	void enterMlBooleanNodeTest(XQueryParserScripting.MlBooleanNodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mlBooleanNodeTest}.
	 * @param ctx the parse tree
	 */
	void exitMlBooleanNodeTest(XQueryParserScripting.MlBooleanNodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#mlNullNodeTest}.
	 * @param ctx the parse tree
	 */
	void enterMlNullNodeTest(XQueryParserScripting.MlNullNodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#mlNullNodeTest}.
	 * @param ctx the parse tree
	 */
	void exitMlNullNodeTest(XQueryParserScripting.MlNullNodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#eqName}.
	 * @param ctx the parse tree
	 */
	void enterEqName(XQueryParserScripting.EqNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#eqName}.
	 * @param ctx the parse tree
	 */
	void exitEqName(XQueryParserScripting.EqNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#qName}.
	 * @param ctx the parse tree
	 */
	void enterQName(XQueryParserScripting.QNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#qName}.
	 * @param ctx the parse tree
	 */
	void exitQName(XQueryParserScripting.QNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#ncName}.
	 * @param ctx the parse tree
	 */
	void enterNcName(XQueryParserScripting.NcNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#ncName}.
	 * @param ctx the parse tree
	 */
	void exitNcName(XQueryParserScripting.NcNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(XQueryParserScripting.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(XQueryParserScripting.FunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(XQueryParserScripting.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(XQueryParserScripting.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#keywordNotOKForFunction}.
	 * @param ctx the parse tree
	 */
	void enterKeywordNotOKForFunction(XQueryParserScripting.KeywordNotOKForFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#keywordNotOKForFunction}.
	 * @param ctx the parse tree
	 */
	void exitKeywordNotOKForFunction(XQueryParserScripting.KeywordNotOKForFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#keywordOKForFunction}.
	 * @param ctx the parse tree
	 */
	void enterKeywordOKForFunction(XQueryParserScripting.KeywordOKForFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#keywordOKForFunction}.
	 * @param ctx the parse tree
	 */
	void exitKeywordOKForFunction(XQueryParserScripting.KeywordOKForFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#uriLiteral}.
	 * @param ctx the parse tree
	 */
	void enterUriLiteral(XQueryParserScripting.UriLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#uriLiteral}.
	 * @param ctx the parse tree
	 */
	void exitUriLiteral(XQueryParserScripting.UriLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringLiteralQuot}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteralQuot(XQueryParserScripting.StringLiteralQuotContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringLiteralQuot}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteralQuot(XQueryParserScripting.StringLiteralQuotContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringLiteralApos}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteralApos(XQueryParserScripting.StringLiteralAposContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringLiteralApos}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteralApos(XQueryParserScripting.StringLiteralAposContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(XQueryParserScripting.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(XQueryParserScripting.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringContentQuot}.
	 * @param ctx the parse tree
	 */
	void enterStringContentQuot(XQueryParserScripting.StringContentQuotContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringContentQuot}.
	 * @param ctx the parse tree
	 */
	void exitStringContentQuot(XQueryParserScripting.StringContentQuotContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#stringContentApos}.
	 * @param ctx the parse tree
	 */
	void enterStringContentApos(XQueryParserScripting.StringContentAposContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#stringContentApos}.
	 * @param ctx the parse tree
	 */
	void exitStringContentApos(XQueryParserScripting.StringContentAposContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParserScripting#noQuotesNoBracesNoAmpNoLAng}.
	 * @param ctx the parse tree
	 */
	void enterNoQuotesNoBracesNoAmpNoLAng(XQueryParserScripting.NoQuotesNoBracesNoAmpNoLAngContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParserScripting#noQuotesNoBracesNoAmpNoLAng}.
	 * @param ctx the parse tree
	 */
	void exitNoQuotesNoBracesNoAmpNoLAng(XQueryParserScripting.NoQuotesNoBracesNoAmpNoLAngContext ctx);
}