// Generated from ./src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.8

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