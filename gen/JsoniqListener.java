// Generated from /Users/dbuzatu/ETH/Thesis/rumble/src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.13.1

// Java header
package org.rumbledb.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JsoniqParser}.
 */
public interface JsoniqListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#moduleAndThisIsIt}.
	 * @param ctx the parse tree
	 */
	void enterModuleAndThisIsIt(JsoniqParser.ModuleAndThisIsItContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#moduleAndThisIsIt}.
	 * @param ctx the parse tree
	 */
	void exitModuleAndThisIsIt(JsoniqParser.ModuleAndThisIsItContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(JsoniqParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(JsoniqParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#mainModule}.
	 * @param ctx the parse tree
	 */
	void enterMainModule(JsoniqParser.MainModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#mainModule}.
	 * @param ctx the parse tree
	 */
	void exitMainModule(JsoniqParser.MainModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#libraryModule}.
	 * @param ctx the parse tree
	 */
	void enterLibraryModule(JsoniqParser.LibraryModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#libraryModule}.
	 * @param ctx the parse tree
	 */
	void exitLibraryModule(JsoniqParser.LibraryModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#prolog}.
	 * @param ctx the parse tree
	 */
	void enterProlog(JsoniqParser.PrologContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#prolog}.
	 * @param ctx the parse tree
	 */
	void exitProlog(JsoniqParser.PrologContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(JsoniqParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(JsoniqParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#setter}.
	 * @param ctx the parse tree
	 */
	void enterSetter(JsoniqParser.SetterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#setter}.
	 * @param ctx the parse tree
	 */
	void exitSetter(JsoniqParser.SetterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#namespaceDecl}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceDecl(JsoniqParser.NamespaceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#namespaceDecl}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceDecl(JsoniqParser.NamespaceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#annotatedDecl}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedDecl(JsoniqParser.AnnotatedDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#annotatedDecl}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedDecl(JsoniqParser.AnnotatedDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#defaultCollationDecl}.
	 * @param ctx the parse tree
	 */
	void enterDefaultCollationDecl(JsoniqParser.DefaultCollationDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#defaultCollationDecl}.
	 * @param ctx the parse tree
	 */
	void exitDefaultCollationDecl(JsoniqParser.DefaultCollationDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#orderingModeDecl}.
	 * @param ctx the parse tree
	 */
	void enterOrderingModeDecl(JsoniqParser.OrderingModeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#orderingModeDecl}.
	 * @param ctx the parse tree
	 */
	void exitOrderingModeDecl(JsoniqParser.OrderingModeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#emptyOrderDecl}.
	 * @param ctx the parse tree
	 */
	void enterEmptyOrderDecl(JsoniqParser.EmptyOrderDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#emptyOrderDecl}.
	 * @param ctx the parse tree
	 */
	void exitEmptyOrderDecl(JsoniqParser.EmptyOrderDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#decimalFormatDecl}.
	 * @param ctx the parse tree
	 */
	void enterDecimalFormatDecl(JsoniqParser.DecimalFormatDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#decimalFormatDecl}.
	 * @param ctx the parse tree
	 */
	void exitDecimalFormatDecl(JsoniqParser.DecimalFormatDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#qname}.
	 * @param ctx the parse tree
	 */
	void enterQname(JsoniqParser.QnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#qname}.
	 * @param ctx the parse tree
	 */
	void exitQname(JsoniqParser.QnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#dfPropertyName}.
	 * @param ctx the parse tree
	 */
	void enterDfPropertyName(JsoniqParser.DfPropertyNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#dfPropertyName}.
	 * @param ctx the parse tree
	 */
	void exitDfPropertyName(JsoniqParser.DfPropertyNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#moduleImport}.
	 * @param ctx the parse tree
	 */
	void enterModuleImport(JsoniqParser.ModuleImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#moduleImport}.
	 * @param ctx the parse tree
	 */
	void exitModuleImport(JsoniqParser.ModuleImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(JsoniqParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(JsoniqParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#contextItemDecl}.
	 * @param ctx the parse tree
	 */
	void enterContextItemDecl(JsoniqParser.ContextItemDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#contextItemDecl}.
	 * @param ctx the parse tree
	 */
	void exitContextItemDecl(JsoniqParser.ContextItemDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDecl(JsoniqParser.FunctionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDecl(JsoniqParser.FunctionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#typeDecl}.
	 * @param ctx the parse tree
	 */
	void enterTypeDecl(JsoniqParser.TypeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#typeDecl}.
	 * @param ctx the parse tree
	 */
	void exitTypeDecl(JsoniqParser.TypeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#schemaLanguage}.
	 * @param ctx the parse tree
	 */
	void enterSchemaLanguage(JsoniqParser.SchemaLanguageContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#schemaLanguage}.
	 * @param ctx the parse tree
	 */
	void exitSchemaLanguage(JsoniqParser.SchemaLanguageContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(JsoniqParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(JsoniqParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(JsoniqParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(JsoniqParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(JsoniqParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(JsoniqParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#exprSingle}.
	 * @param ctx the parse tree
	 */
	void enterExprSingle(JsoniqParser.ExprSingleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#exprSingle}.
	 * @param ctx the parse tree
	 */
	void exitExprSingle(JsoniqParser.ExprSingleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#exprSimple}.
	 * @param ctx the parse tree
	 */
	void enterExprSimple(JsoniqParser.ExprSimpleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#exprSimple}.
	 * @param ctx the parse tree
	 */
	void exitExprSimple(JsoniqParser.ExprSimpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#flowrExpr}.
	 * @param ctx the parse tree
	 */
	void enterFlowrExpr(JsoniqParser.FlowrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#flowrExpr}.
	 * @param ctx the parse tree
	 */
	void exitFlowrExpr(JsoniqParser.FlowrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#forClause}.
	 * @param ctx the parse tree
	 */
	void enterForClause(JsoniqParser.ForClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#forClause}.
	 * @param ctx the parse tree
	 */
	void exitForClause(JsoniqParser.ForClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#forVar}.
	 * @param ctx the parse tree
	 */
	void enterForVar(JsoniqParser.ForVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#forVar}.
	 * @param ctx the parse tree
	 */
	void exitForVar(JsoniqParser.ForVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#letClause}.
	 * @param ctx the parse tree
	 */
	void enterLetClause(JsoniqParser.LetClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#letClause}.
	 * @param ctx the parse tree
	 */
	void exitLetClause(JsoniqParser.LetClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#letVar}.
	 * @param ctx the parse tree
	 */
	void enterLetVar(JsoniqParser.LetVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#letVar}.
	 * @param ctx the parse tree
	 */
	void exitLetVar(JsoniqParser.LetVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(JsoniqParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(JsoniqParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#groupByClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupByClause(JsoniqParser.GroupByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#groupByClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupByClause(JsoniqParser.GroupByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#groupByVar}.
	 * @param ctx the parse tree
	 */
	void enterGroupByVar(JsoniqParser.GroupByVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#groupByVar}.
	 * @param ctx the parse tree
	 */
	void exitGroupByVar(JsoniqParser.GroupByVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(JsoniqParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(JsoniqParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#orderByExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrderByExpr(JsoniqParser.OrderByExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#orderByExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrderByExpr(JsoniqParser.OrderByExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#countClause}.
	 * @param ctx the parse tree
	 */
	void enterCountClause(JsoniqParser.CountClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#countClause}.
	 * @param ctx the parse tree
	 */
	void exitCountClause(JsoniqParser.CountClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#quantifiedExpr}.
	 * @param ctx the parse tree
	 */
	void enterQuantifiedExpr(JsoniqParser.QuantifiedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#quantifiedExpr}.
	 * @param ctx the parse tree
	 */
	void exitQuantifiedExpr(JsoniqParser.QuantifiedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#quantifiedExprVar}.
	 * @param ctx the parse tree
	 */
	void enterQuantifiedExprVar(JsoniqParser.QuantifiedExprVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#quantifiedExprVar}.
	 * @param ctx the parse tree
	 */
	void exitQuantifiedExprVar(JsoniqParser.QuantifiedExprVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#switchExpr}.
	 * @param ctx the parse tree
	 */
	void enterSwitchExpr(JsoniqParser.SwitchExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#switchExpr}.
	 * @param ctx the parse tree
	 */
	void exitSwitchExpr(JsoniqParser.SwitchExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#switchCaseClause}.
	 * @param ctx the parse tree
	 */
	void enterSwitchCaseClause(JsoniqParser.SwitchCaseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#switchCaseClause}.
	 * @param ctx the parse tree
	 */
	void exitSwitchCaseClause(JsoniqParser.SwitchCaseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#typeSwitchExpr}.
	 * @param ctx the parse tree
	 */
	void enterTypeSwitchExpr(JsoniqParser.TypeSwitchExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#typeSwitchExpr}.
	 * @param ctx the parse tree
	 */
	void exitTypeSwitchExpr(JsoniqParser.TypeSwitchExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#caseClause}.
	 * @param ctx the parse tree
	 */
	void enterCaseClause(JsoniqParser.CaseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#caseClause}.
	 * @param ctx the parse tree
	 */
	void exitCaseClause(JsoniqParser.CaseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#ifExpr}.
	 * @param ctx the parse tree
	 */
	void enterIfExpr(JsoniqParser.IfExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#ifExpr}.
	 * @param ctx the parse tree
	 */
	void exitIfExpr(JsoniqParser.IfExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#tryCatchExpr}.
	 * @param ctx the parse tree
	 */
	void enterTryCatchExpr(JsoniqParser.TryCatchExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#tryCatchExpr}.
	 * @param ctx the parse tree
	 */
	void exitTryCatchExpr(JsoniqParser.TryCatchExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(JsoniqParser.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(JsoniqParser.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(JsoniqParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(JsoniqParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(JsoniqParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(JsoniqParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#notExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(JsoniqParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#notExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(JsoniqParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpr(JsoniqParser.ComparisonExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpr(JsoniqParser.ComparisonExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#stringConcatExpr}.
	 * @param ctx the parse tree
	 */
	void enterStringConcatExpr(JsoniqParser.StringConcatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#stringConcatExpr}.
	 * @param ctx the parse tree
	 */
	void exitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#rangeExpr}.
	 * @param ctx the parse tree
	 */
	void enterRangeExpr(JsoniqParser.RangeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#rangeExpr}.
	 * @param ctx the parse tree
	 */
	void exitRangeExpr(JsoniqParser.RangeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpr(JsoniqParser.AdditiveExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpr(JsoniqParser.AdditiveExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#instanceOfExpr}.
	 * @param ctx the parse tree
	 */
	void enterInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#instanceOfExpr}.
	 * @param ctx the parse tree
	 */
	void exitInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#isStaticallyExpr}.
	 * @param ctx the parse tree
	 */
	void enterIsStaticallyExpr(JsoniqParser.IsStaticallyExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#isStaticallyExpr}.
	 * @param ctx the parse tree
	 */
	void exitIsStaticallyExpr(JsoniqParser.IsStaticallyExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#treatExpr}.
	 * @param ctx the parse tree
	 */
	void enterTreatExpr(JsoniqParser.TreatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#treatExpr}.
	 * @param ctx the parse tree
	 */
	void exitTreatExpr(JsoniqParser.TreatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#castableExpr}.
	 * @param ctx the parse tree
	 */
	void enterCastableExpr(JsoniqParser.CastableExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#castableExpr}.
	 * @param ctx the parse tree
	 */
	void exitCastableExpr(JsoniqParser.CastableExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#castExpr}.
	 * @param ctx the parse tree
	 */
	void enterCastExpr(JsoniqParser.CastExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#castExpr}.
	 * @param ctx the parse tree
	 */
	void exitCastExpr(JsoniqParser.CastExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#arrowExpr}.
	 * @param ctx the parse tree
	 */
	void enterArrowExpr(JsoniqParser.ArrowExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#arrowExpr}.
	 * @param ctx the parse tree
	 */
	void exitArrowExpr(JsoniqParser.ArrowExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#arrowFunctionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterArrowFunctionSpecifier(JsoniqParser.ArrowFunctionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#arrowFunctionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitArrowFunctionSpecifier(JsoniqParser.ArrowFunctionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(JsoniqParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(JsoniqParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#valueExpr}.
	 * @param ctx the parse tree
	 */
	void enterValueExpr(JsoniqParser.ValueExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#valueExpr}.
	 * @param ctx the parse tree
	 */
	void exitValueExpr(JsoniqParser.ValueExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#validateExpr}.
	 * @param ctx the parse tree
	 */
	void enterValidateExpr(JsoniqParser.ValidateExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#validateExpr}.
	 * @param ctx the parse tree
	 */
	void exitValidateExpr(JsoniqParser.ValidateExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#annotateExpr}.
	 * @param ctx the parse tree
	 */
	void enterAnnotateExpr(JsoniqParser.AnnotateExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#annotateExpr}.
	 * @param ctx the parse tree
	 */
	void exitAnnotateExpr(JsoniqParser.AnnotateExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#simpleMapExpr}.
	 * @param ctx the parse tree
	 */
	void enterSimpleMapExpr(JsoniqParser.SimpleMapExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#simpleMapExpr}.
	 * @param ctx the parse tree
	 */
	void exitSimpleMapExpr(JsoniqParser.SimpleMapExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#postFixExpr}.
	 * @param ctx the parse tree
	 */
	void enterPostFixExpr(JsoniqParser.PostFixExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#postFixExpr}.
	 * @param ctx the parse tree
	 */
	void exitPostFixExpr(JsoniqParser.PostFixExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#arrayLookup}.
	 * @param ctx the parse tree
	 */
	void enterArrayLookup(JsoniqParser.ArrayLookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#arrayLookup}.
	 * @param ctx the parse tree
	 */
	void exitArrayLookup(JsoniqParser.ArrayLookupContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#arrayUnboxing}.
	 * @param ctx the parse tree
	 */
	void enterArrayUnboxing(JsoniqParser.ArrayUnboxingContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#arrayUnboxing}.
	 * @param ctx the parse tree
	 */
	void exitArrayUnboxing(JsoniqParser.ArrayUnboxingContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(JsoniqParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(JsoniqParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#objectLookup}.
	 * @param ctx the parse tree
	 */
	void enterObjectLookup(JsoniqParser.ObjectLookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#objectLookup}.
	 * @param ctx the parse tree
	 */
	void exitObjectLookup(JsoniqParser.ObjectLookupContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(JsoniqParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(JsoniqParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#blockExpr}.
	 * @param ctx the parse tree
	 */
	void enterBlockExpr(JsoniqParser.BlockExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#blockExpr}.
	 * @param ctx the parse tree
	 */
	void exitBlockExpr(JsoniqParser.BlockExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#varRef}.
	 * @param ctx the parse tree
	 */
	void enterVarRef(JsoniqParser.VarRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#varRef}.
	 * @param ctx the parse tree
	 */
	void exitVarRef(JsoniqParser.VarRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#parenthesizedExpr}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpr(JsoniqParser.ParenthesizedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#parenthesizedExpr}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpr(JsoniqParser.ParenthesizedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#contextItemExpr}.
	 * @param ctx the parse tree
	 */
	void enterContextItemExpr(JsoniqParser.ContextItemExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#contextItemExpr}.
	 * @param ctx the parse tree
	 */
	void exitContextItemExpr(JsoniqParser.ContextItemExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#orderedExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrderedExpr(JsoniqParser.OrderedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#orderedExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrderedExpr(JsoniqParser.OrderedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#unorderedExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnorderedExpr(JsoniqParser.UnorderedExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#unorderedExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnorderedExpr(JsoniqParser.UnorderedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(JsoniqParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(JsoniqParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(JsoniqParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(JsoniqParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(JsoniqParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(JsoniqParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#functionItemExpr}.
	 * @param ctx the parse tree
	 */
	void enterFunctionItemExpr(JsoniqParser.FunctionItemExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#functionItemExpr}.
	 * @param ctx the parse tree
	 */
	void exitFunctionItemExpr(JsoniqParser.FunctionItemExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#namedFunctionRef}.
	 * @param ctx the parse tree
	 */
	void enterNamedFunctionRef(JsoniqParser.NamedFunctionRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#namedFunctionRef}.
	 * @param ctx the parse tree
	 */
	void exitNamedFunctionRef(JsoniqParser.NamedFunctionRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#inlineFunctionExpr}.
	 * @param ctx the parse tree
	 */
	void enterInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#inlineFunctionExpr}.
	 * @param ctx the parse tree
	 */
	void exitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#insertExpr}.
	 * @param ctx the parse tree
	 */
	void enterInsertExpr(JsoniqParser.InsertExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#insertExpr}.
	 * @param ctx the parse tree
	 */
	void exitInsertExpr(JsoniqParser.InsertExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#deleteExpr}.
	 * @param ctx the parse tree
	 */
	void enterDeleteExpr(JsoniqParser.DeleteExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#deleteExpr}.
	 * @param ctx the parse tree
	 */
	void exitDeleteExpr(JsoniqParser.DeleteExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#renameExpr}.
	 * @param ctx the parse tree
	 */
	void enterRenameExpr(JsoniqParser.RenameExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#renameExpr}.
	 * @param ctx the parse tree
	 */
	void exitRenameExpr(JsoniqParser.RenameExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#replaceExpr}.
	 * @param ctx the parse tree
	 */
	void enterReplaceExpr(JsoniqParser.ReplaceExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#replaceExpr}.
	 * @param ctx the parse tree
	 */
	void exitReplaceExpr(JsoniqParser.ReplaceExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#transformExpr}.
	 * @param ctx the parse tree
	 */
	void enterTransformExpr(JsoniqParser.TransformExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#transformExpr}.
	 * @param ctx the parse tree
	 */
	void exitTransformExpr(JsoniqParser.TransformExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#appendExpr}.
	 * @param ctx the parse tree
	 */
	void enterAppendExpr(JsoniqParser.AppendExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#appendExpr}.
	 * @param ctx the parse tree
	 */
	void exitAppendExpr(JsoniqParser.AppendExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#updateLocator}.
	 * @param ctx the parse tree
	 */
	void enterUpdateLocator(JsoniqParser.UpdateLocatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#updateLocator}.
	 * @param ctx the parse tree
	 */
	void exitUpdateLocator(JsoniqParser.UpdateLocatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#copyDecl}.
	 * @param ctx the parse tree
	 */
	void enterCopyDecl(JsoniqParser.CopyDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#copyDecl}.
	 * @param ctx the parse tree
	 */
	void exitCopyDecl(JsoniqParser.CopyDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(JsoniqParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(JsoniqParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#statementsAndExpr}.
	 * @param ctx the parse tree
	 */
	void enterStatementsAndExpr(JsoniqParser.StatementsAndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#statementsAndExpr}.
	 * @param ctx the parse tree
	 */
	void exitStatementsAndExpr(JsoniqParser.StatementsAndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#statementsAndOptionalExpr}.
	 * @param ctx the parse tree
	 */
	void enterStatementsAndOptionalExpr(JsoniqParser.StatementsAndOptionalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#statementsAndOptionalExpr}.
	 * @param ctx the parse tree
	 */
	void exitStatementsAndOptionalExpr(JsoniqParser.StatementsAndOptionalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(JsoniqParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(JsoniqParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#applyStatement}.
	 * @param ctx the parse tree
	 */
	void enterApplyStatement(JsoniqParser.ApplyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#applyStatement}.
	 * @param ctx the parse tree
	 */
	void exitApplyStatement(JsoniqParser.ApplyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#assignStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignStatement(JsoniqParser.AssignStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#assignStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignStatement(JsoniqParser.AssignStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(JsoniqParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(JsoniqParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(JsoniqParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(JsoniqParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(JsoniqParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(JsoniqParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#exitStatement}.
	 * @param ctx the parse tree
	 */
	void enterExitStatement(JsoniqParser.ExitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#exitStatement}.
	 * @param ctx the parse tree
	 */
	void exitExitStatement(JsoniqParser.ExitStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#flowrStatement}.
	 * @param ctx the parse tree
	 */
	void enterFlowrStatement(JsoniqParser.FlowrStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#flowrStatement}.
	 * @param ctx the parse tree
	 */
	void exitFlowrStatement(JsoniqParser.FlowrStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(JsoniqParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(JsoniqParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(JsoniqParser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(JsoniqParser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#switchCaseStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchCaseStatement(JsoniqParser.SwitchCaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#switchCaseStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchCaseStatement(JsoniqParser.SwitchCaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryCatchStatement(JsoniqParser.TryCatchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#tryCatchStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryCatchStatement(JsoniqParser.TryCatchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#catchCaseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCatchCaseStatement(JsoniqParser.CatchCaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#catchCaseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCatchCaseStatement(JsoniqParser.CatchCaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#typeSwitchStatement}.
	 * @param ctx the parse tree
	 */
	void enterTypeSwitchStatement(JsoniqParser.TypeSwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#typeSwitchStatement}.
	 * @param ctx the parse tree
	 */
	void exitTypeSwitchStatement(JsoniqParser.TypeSwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(JsoniqParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(JsoniqParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(JsoniqParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(JsoniqParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#annotations}.
	 * @param ctx the parse tree
	 */
	void enterAnnotations(JsoniqParser.AnnotationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#annotations}.
	 * @param ctx the parse tree
	 */
	void exitAnnotations(JsoniqParser.AnnotationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#varDeclStatement}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclStatement(JsoniqParser.VarDeclStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#varDeclStatement}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclStatement(JsoniqParser.VarDeclStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#varDeclOther}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclOther(JsoniqParser.VarDeclOtherContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#varDeclOther}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclOther(JsoniqParser.VarDeclOtherContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(JsoniqParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(JsoniqParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#sequenceType}.
	 * @param ctx the parse tree
	 */
	void enterSequenceType(JsoniqParser.SequenceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#sequenceType}.
	 * @param ctx the parse tree
	 */
	void exitSequenceType(JsoniqParser.SequenceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#objectConstructor}.
	 * @param ctx the parse tree
	 */
	void enterObjectConstructor(JsoniqParser.ObjectConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#objectConstructor}.
	 * @param ctx the parse tree
	 */
	void exitObjectConstructor(JsoniqParser.ObjectConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#itemType}.
	 * @param ctx the parse tree
	 */
	void enterItemType(JsoniqParser.ItemTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#itemType}.
	 * @param ctx the parse tree
	 */
	void exitItemType(JsoniqParser.ItemTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#functionTest}.
	 * @param ctx the parse tree
	 */
	void enterFunctionTest(JsoniqParser.FunctionTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#functionTest}.
	 * @param ctx the parse tree
	 */
	void exitFunctionTest(JsoniqParser.FunctionTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#anyFunctionTest}.
	 * @param ctx the parse tree
	 */
	void enterAnyFunctionTest(JsoniqParser.AnyFunctionTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#anyFunctionTest}.
	 * @param ctx the parse tree
	 */
	void exitAnyFunctionTest(JsoniqParser.AnyFunctionTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#typedFunctionTest}.
	 * @param ctx the parse tree
	 */
	void enterTypedFunctionTest(JsoniqParser.TypedFunctionTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#typedFunctionTest}.
	 * @param ctx the parse tree
	 */
	void exitTypedFunctionTest(JsoniqParser.TypedFunctionTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#singleType}.
	 * @param ctx the parse tree
	 */
	void enterSingleType(JsoniqParser.SingleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#singleType}.
	 * @param ctx the parse tree
	 */
	void exitSingleType(JsoniqParser.SingleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#pairConstructor}.
	 * @param ctx the parse tree
	 */
	void enterPairConstructor(JsoniqParser.PairConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#pairConstructor}.
	 * @param ctx the parse tree
	 */
	void exitPairConstructor(JsoniqParser.PairConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#arrayConstructor}.
	 * @param ctx the parse tree
	 */
	void enterArrayConstructor(JsoniqParser.ArrayConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#arrayConstructor}.
	 * @param ctx the parse tree
	 */
	void exitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#uriLiteral}.
	 * @param ctx the parse tree
	 */
	void enterUriLiteral(JsoniqParser.UriLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#uriLiteral}.
	 * @param ctx the parse tree
	 */
	void exitUriLiteral(JsoniqParser.UriLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(JsoniqParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(JsoniqParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JsoniqParser#keyWords}.
	 * @param ctx the parse tree
	 */
	void enterKeyWords(JsoniqParser.KeyWordsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsoniqParser#keyWords}.
	 * @param ctx the parse tree
	 */
	void exitKeyWords(JsoniqParser.KeyWordsContext ctx);
}