// Generated from /Users/dbuzatu/ETH/Thesis/rumble/src/main/java/org/rumbledb/parser/XQueryParserScripting.g4 by ANTLR 4.13.1

// Java header
package org.rumbledb.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class XQueryParserScripting extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		EscapeQuot=1, EscapeApos=2, DOUBLE_LBRACE=3, DOUBLE_RBRACE=4, IntegerLiteral=5, 
		DecimalLiteral=6, DoubleLiteral=7, DFPropertyName=8, PredefinedEntityRef=9, 
		CharRef=10, Quot=11, Apos=12, COMMENT=13, XMLDECL=14, PI=15, CDATA=16, 
		PRAGMA=17, WS=18, EQUAL=19, NOT_EQUAL=20, LPAREN=21, RPAREN=22, LBRACKET=23, 
		RBRACKET=24, LBRACE=25, RBRACE=26, STAR=27, PLUS=28, MINUS=29, COMMA=30, 
		DOT=31, DDOT=32, COLON=33, COLON_EQ=34, SEMICOLON=35, SLASH=36, DSLASH=37, 
		BACKSLASH=38, VBAR=39, LANGLE=40, RANGLE=41, QUESTION=42, AT=43, DOLLAR=44, 
		MOD=45, BANG=46, HASH=47, CARAT=48, ARROW=49, GRAVE=50, CONCATENATION=51, 
		TILDE=52, KW_ALLOWING=53, KW_ANCESTOR=54, KW_ANCESTOR_OR_SELF=55, KW_AND=56, 
		KW_ARRAY=57, KW_AS=58, KW_ASCENDING=59, KW_AT=60, KW_ATTRIBUTE=61, KW_BASE_URI=62, 
		KW_BOUNDARY_SPACE=63, KW_BINARY=64, KW_BY=65, KW_CASE=66, KW_CAST=67, 
		KW_CASTABLE=68, KW_CATCH=69, KW_CHILD=70, KW_COLLATION=71, KW_COMMENT=72, 
		KW_CONSTRUCTION=73, KW_CONTEXT=74, KW_COPY_NS=75, KW_COUNT=76, KW_DECLARE=77, 
		KW_DEFAULT=78, KW_DESCENDANT=79, KW_DESCENDANT_OR_SELF=80, KW_DESCENDING=81, 
		KW_DECIMAL_FORMAT=82, KW_DIV=83, KW_DOCUMENT=84, KW_DOCUMENT_NODE=85, 
		KW_ELEMENT=86, KW_ELSE=87, KW_EMPTY=88, KW_EMPTY_SEQUENCE=89, KW_ENCODING=90, 
		KW_END=91, KW_EQ=92, KW_EVERY=93, KW_EXCEPT=94, KW_EXTERNAL=95, KW_FOLLOWING=96, 
		KW_FOLLOWING_SIBLING=97, KW_FOR=98, KW_FUNCTION=99, KW_GE=100, KW_GREATEST=101, 
		KW_GROUP=102, KW_GT=103, KW_IDIV=104, KW_IF=105, KW_IMPORT=106, KW_IN=107, 
		KW_INHERIT=108, KW_INSTANCE=109, KW_INTERSECT=110, KW_IS=111, KW_ITEM=112, 
		KW_LAX=113, KW_LE=114, KW_LEAST=115, KW_LET=116, KW_LT=117, KW_MAP=118, 
		KW_MOD=119, KW_MODULE=120, KW_NAMESPACE=121, KW_NE=122, KW_NEXT=123, KW_NAMESPACE_NODE=124, 
		KW_NO_INHERIT=125, KW_NO_PRESERVE=126, KW_NODE=127, KW_OF=128, KW_ONLY=129, 
		KW_OPTION=130, KW_OR=131, KW_ORDER=132, KW_ORDERED=133, KW_ORDERING=134, 
		KW_PARENT=135, KW_PRECEDING=136, KW_PRECEDING_SIBLING=137, KW_PRESERVE=138, 
		KW_PREVIOUS=139, KW_PI=140, KW_RETURN=141, KW_SATISFIES=142, KW_SCHEMA=143, 
		KW_SCHEMA_ATTR=144, KW_SCHEMA_ELEM=145, KW_SELF=146, KW_SLIDING=147, KW_SOME=148, 
		KW_STABLE=149, KW_START=150, KW_STRICT=151, KW_STRIP=152, KW_SWITCH=153, 
		KW_TEXT=154, KW_THEN=155, KW_TO=156, KW_TREAT=157, KW_TRY=158, KW_TUMBLING=159, 
		KW_TYPE=160, KW_TYPESWITCH=161, KW_UNION=162, KW_UNORDERED=163, KW_UPDATE=164, 
		KW_VALIDATE=165, KW_VARIABLE=166, KW_VERSION=167, KW_WHEN=168, KW_WHERE=169, 
		KW_WINDOW=170, KW_XQUERY=171, KW_BREAK=172, KW_LOOP=173, KW_CONTINUE=174, 
		KW_EXIT=175, KW_RETURNING=176, KW_WHILE=177, KW_ARRAY_NODE=178, KW_BOOLEAN_NODE=179, 
		KW_NULL_NODE=180, KW_NUMBER_NODE=181, KW_OBJECT_NODE=182, KW_REPLACE=183, 
		KW_WITH=184, KW_VALUE=185, KW_INSERT=186, KW_INTO=187, KW_DELETE=188, 
		KW_RENAME=189, URIQualifiedName=190, FullQName=191, NCNameWithLocalWildcard=192, 
		NCNameWithPrefixWildcard=193, NCName=194, XQDOC_COMMENT_START=195, XQDOC_COMMENT_END=196, 
		XQDocComment=197, XQComment=198, CHAR=199, ENTER_STRING=200, EXIT_INTERPOLATION=201, 
		ContentChar=202, BASIC_CHAR=203, ENTER_INTERPOLATION=204, EXIT_STRING=205, 
		EscapeQuot_QuotString=206, DOUBLE_LBRACE_QuotString=207, DOUBLE_RBRACE_QuotString=208, 
		EscapeApos_AposString=209;
	public static final int
		RULE_module = 0, RULE_xqDocComment = 1, RULE_versionDecl = 2, RULE_mainModule = 3, 
		RULE_program = 4, RULE_libraryModule = 5, RULE_moduleDecl = 6, RULE_prolog = 7, 
		RULE_annotatedDecl = 8, RULE_defaultNamespaceDecl = 9, RULE_setter = 10, 
		RULE_boundarySpaceDecl = 11, RULE_defaultCollationDecl = 12, RULE_baseURIDecl = 13, 
		RULE_constructionDecl = 14, RULE_orderingModeDecl = 15, RULE_emptyOrderDecl = 16, 
		RULE_copyNamespacesDecl = 17, RULE_preserveMode = 18, RULE_inheritMode = 19, 
		RULE_decimalFormatDecl = 20, RULE_schemaImport = 21, RULE_schemaPrefix = 22, 
		RULE_moduleImport = 23, RULE_namespaceDecl = 24, RULE_varDecl = 25, RULE_varValue = 26, 
		RULE_varDefaultValue = 27, RULE_contextItemDecl = 28, RULE_functionDecl = 29, 
		RULE_functionParams = 30, RULE_functionParam = 31, RULE_annotations = 32, 
		RULE_annotation = 33, RULE_annotList = 34, RULE_annotationParam = 35, 
		RULE_functionReturn = 36, RULE_optionDecl = 37, RULE_statements = 38, 
		RULE_statementsAndExpr = 39, RULE_statementsAndOptionalExpr = 40, RULE_expr = 41, 
		RULE_exprSingle = 42, RULE_exprSimple = 43, RULE_blockExpr = 44, RULE_flworExpr = 45, 
		RULE_initialClause = 46, RULE_intermediateClause = 47, RULE_forClause = 48, 
		RULE_forBinding = 49, RULE_allowingEmpty = 50, RULE_positionalVar = 51, 
		RULE_letClause = 52, RULE_letBinding = 53, RULE_windowClause = 54, RULE_tumblingWindowClause = 55, 
		RULE_slidingWindowClause = 56, RULE_windowStartCondition = 57, RULE_windowEndCondition = 58, 
		RULE_windowVars = 59, RULE_countClause = 60, RULE_whereClause = 61, RULE_groupByClause = 62, 
		RULE_groupingSpecList = 63, RULE_groupingSpec = 64, RULE_orderByClause = 65, 
		RULE_orderSpec = 66, RULE_returnClause = 67, RULE_quantifiedExpr = 68, 
		RULE_quantifiedVar = 69, RULE_switchExpr = 70, RULE_switchCaseClause = 71, 
		RULE_switchCaseOperand = 72, RULE_typeswitchExpr = 73, RULE_caseClause = 74, 
		RULE_sequenceUnionType = 75, RULE_ifExpr = 76, RULE_tryCatchExpr = 77, 
		RULE_tryClause = 78, RULE_enclosedTryTargetExpression = 79, RULE_catchClause = 80, 
		RULE_enclosedExpression = 81, RULE_catchErrorList = 82, RULE_existUpdateExpr = 83, 
		RULE_existReplaceExpr = 84, RULE_existValueExpr = 85, RULE_existInsertExpr = 86, 
		RULE_existDeleteExpr = 87, RULE_existRenameExpr = 88, RULE_orExpr = 89, 
		RULE_andExpr = 90, RULE_comparisonExpr = 91, RULE_stringConcatExpr = 92, 
		RULE_rangeExpr = 93, RULE_additiveExpr = 94, RULE_multiplicativeExpr = 95, 
		RULE_unionExpr = 96, RULE_intersectExceptExpr = 97, RULE_instanceOfExpr = 98, 
		RULE_treatExpr = 99, RULE_castableExpr = 100, RULE_castExpr = 101, RULE_arrowExpr = 102, 
		RULE_complexArrow = 103, RULE_unaryExpr = 104, RULE_valueExpr = 105, RULE_generalComp = 106, 
		RULE_valueComp = 107, RULE_nodeComp = 108, RULE_validateExpr = 109, RULE_validationMode = 110, 
		RULE_extensionExpr = 111, RULE_simpleMapExpr = 112, RULE_statement = 113, 
		RULE_applyStatement = 114, RULE_assignStatement = 115, RULE_blockStatement = 116, 
		RULE_breakStatement = 117, RULE_continueStatement = 118, RULE_exitStatement = 119, 
		RULE_flworStatement = 120, RULE_returnStatement = 121, RULE_ifStatement = 122, 
		RULE_switchStatement = 123, RULE_switchCaseStatement = 124, RULE_tryCatchStatement = 125, 
		RULE_typeswitchStatement = 126, RULE_caseStatement = 127, RULE_varDeclStatement = 128, 
		RULE_whileStatement = 129, RULE_pathExpr = 130, RULE_relativePathExpr = 131, 
		RULE_stepExpr = 132, RULE_axisStep = 133, RULE_forwardStep = 134, RULE_forwardAxis = 135, 
		RULE_abbrevForwardStep = 136, RULE_reverseStep = 137, RULE_reverseAxis = 138, 
		RULE_abbrevReverseStep = 139, RULE_nodeTest = 140, RULE_nameTest = 141, 
		RULE_wildcard = 142, RULE_postfixExpr = 143, RULE_argumentList = 144, 
		RULE_predicateList = 145, RULE_predicate = 146, RULE_lookup = 147, RULE_keySpecifier = 148, 
		RULE_arrowFunctionSpecifier = 149, RULE_primaryExpr = 150, RULE_literal = 151, 
		RULE_numericLiteral = 152, RULE_varRef = 153, RULE_varName = 154, RULE_parenthesizedExpr = 155, 
		RULE_contextItemExpr = 156, RULE_orderedExpr = 157, RULE_unorderedExpr = 158, 
		RULE_functionCall = 159, RULE_argument = 160, RULE_nodeConstructor = 161, 
		RULE_directConstructor = 162, RULE_dirElemConstructorOpenClose = 163, 
		RULE_dirElemConstructorSingleTag = 164, RULE_dirAttributeList = 165, RULE_dirAttributeValueApos = 166, 
		RULE_dirAttributeValueQuot = 167, RULE_dirAttributeValue = 168, RULE_dirAttributeContentQuot = 169, 
		RULE_dirAttributeContentApos = 170, RULE_dirElemContent = 171, RULE_commonContent = 172, 
		RULE_contentExpr = 173, RULE_computedConstructor = 174, RULE_compMLJSONConstructor = 175, 
		RULE_compMLJSONArrayConstructor = 176, RULE_compMLJSONObjectConstructor = 177, 
		RULE_compMLJSONNumberConstructor = 178, RULE_compMLJSONBooleanConstructor = 179, 
		RULE_compMLJSONNullConstructor = 180, RULE_compBinaryConstructor = 181, 
		RULE_compDocConstructor = 182, RULE_compElemConstructor = 183, RULE_enclosedContentExpr = 184, 
		RULE_compAttrConstructor = 185, RULE_compNamespaceConstructor = 186, RULE_prefix = 187, 
		RULE_enclosedPrefixExpr = 188, RULE_enclosedURIExpr = 189, RULE_compTextConstructor = 190, 
		RULE_compCommentConstructor = 191, RULE_compPIConstructor = 192, RULE_functionItemExpr = 193, 
		RULE_namedFunctionRef = 194, RULE_inlineFunctionRef = 195, RULE_functionBody = 196, 
		RULE_mapConstructor = 197, RULE_mapConstructorEntry = 198, RULE_arrayConstructor = 199, 
		RULE_squareArrayConstructor = 200, RULE_curlyArrayConstructor = 201, RULE_stringConstructor = 202, 
		RULE_stringConstructorContent = 203, RULE_charNoGrave = 204, RULE_charNoLBrace = 205, 
		RULE_charNoRBrack = 206, RULE_stringConstructorChars = 207, RULE_stringConstructorInterpolation = 208, 
		RULE_unaryLookup = 209, RULE_singleType = 210, RULE_typeDeclaration = 211, 
		RULE_sequenceType = 212, RULE_itemType = 213, RULE_atomicOrUnionType = 214, 
		RULE_kindTest = 215, RULE_anyKindTest = 216, RULE_binaryNodeTest = 217, 
		RULE_documentTest = 218, RULE_textTest = 219, RULE_commentTest = 220, 
		RULE_namespaceNodeTest = 221, RULE_piTest = 222, RULE_attributeTest = 223, 
		RULE_attributeNameOrWildcard = 224, RULE_schemaAttributeTest = 225, RULE_elementTest = 226, 
		RULE_elementNameOrWildcard = 227, RULE_schemaElementTest = 228, RULE_elementDeclaration = 229, 
		RULE_attributeName = 230, RULE_elementName = 231, RULE_simpleTypeName = 232, 
		RULE_typeName = 233, RULE_functionTest = 234, RULE_anyFunctionTest = 235, 
		RULE_typedFunctionTest = 236, RULE_mapTest = 237, RULE_anyMapTest = 238, 
		RULE_typedMapTest = 239, RULE_arrayTest = 240, RULE_anyArrayTest = 241, 
		RULE_typedArrayTest = 242, RULE_parenthesizedItemTest = 243, RULE_attributeDeclaration = 244, 
		RULE_mlNodeTest = 245, RULE_mlArrayNodeTest = 246, RULE_mlObjectNodeTest = 247, 
		RULE_mlNumberNodeTest = 248, RULE_mlBooleanNodeTest = 249, RULE_mlNullNodeTest = 250, 
		RULE_eqName = 251, RULE_qName = 252, RULE_ncName = 253, RULE_functionName = 254, 
		RULE_keyword = 255, RULE_keywordNotOKForFunction = 256, RULE_keywordOKForFunction = 257, 
		RULE_uriLiteral = 258, RULE_stringLiteralQuot = 259, RULE_stringLiteralApos = 260, 
		RULE_stringLiteral = 261, RULE_stringContentQuot = 262, RULE_stringContentApos = 263, 
		RULE_noQuotesNoBracesNoAmpNoLAng = 264;
	private static String[] makeRuleNames() {
		return new String[] {
			"module", "xqDocComment", "versionDecl", "mainModule", "program", "libraryModule", 
			"moduleDecl", "prolog", "annotatedDecl", "defaultNamespaceDecl", "setter", 
			"boundarySpaceDecl", "defaultCollationDecl", "baseURIDecl", "constructionDecl", 
			"orderingModeDecl", "emptyOrderDecl", "copyNamespacesDecl", "preserveMode", 
			"inheritMode", "decimalFormatDecl", "schemaImport", "schemaPrefix", "moduleImport", 
			"namespaceDecl", "varDecl", "varValue", "varDefaultValue", "contextItemDecl", 
			"functionDecl", "functionParams", "functionParam", "annotations", "annotation", 
			"annotList", "annotationParam", "functionReturn", "optionDecl", "statements", 
			"statementsAndExpr", "statementsAndOptionalExpr", "expr", "exprSingle", 
			"exprSimple", "blockExpr", "flworExpr", "initialClause", "intermediateClause", 
			"forClause", "forBinding", "allowingEmpty", "positionalVar", "letClause", 
			"letBinding", "windowClause", "tumblingWindowClause", "slidingWindowClause", 
			"windowStartCondition", "windowEndCondition", "windowVars", "countClause", 
			"whereClause", "groupByClause", "groupingSpecList", "groupingSpec", "orderByClause", 
			"orderSpec", "returnClause", "quantifiedExpr", "quantifiedVar", "switchExpr", 
			"switchCaseClause", "switchCaseOperand", "typeswitchExpr", "caseClause", 
			"sequenceUnionType", "ifExpr", "tryCatchExpr", "tryClause", "enclosedTryTargetExpression", 
			"catchClause", "enclosedExpression", "catchErrorList", "existUpdateExpr", 
			"existReplaceExpr", "existValueExpr", "existInsertExpr", "existDeleteExpr", 
			"existRenameExpr", "orExpr", "andExpr", "comparisonExpr", "stringConcatExpr", 
			"rangeExpr", "additiveExpr", "multiplicativeExpr", "unionExpr", "intersectExceptExpr", 
			"instanceOfExpr", "treatExpr", "castableExpr", "castExpr", "arrowExpr", 
			"complexArrow", "unaryExpr", "valueExpr", "generalComp", "valueComp", 
			"nodeComp", "validateExpr", "validationMode", "extensionExpr", "simpleMapExpr", 
			"statement", "applyStatement", "assignStatement", "blockStatement", "breakStatement", 
			"continueStatement", "exitStatement", "flworStatement", "returnStatement", 
			"ifStatement", "switchStatement", "switchCaseStatement", "tryCatchStatement", 
			"typeswitchStatement", "caseStatement", "varDeclStatement", "whileStatement", 
			"pathExpr", "relativePathExpr", "stepExpr", "axisStep", "forwardStep", 
			"forwardAxis", "abbrevForwardStep", "reverseStep", "reverseAxis", "abbrevReverseStep", 
			"nodeTest", "nameTest", "wildcard", "postfixExpr", "argumentList", "predicateList", 
			"predicate", "lookup", "keySpecifier", "arrowFunctionSpecifier", "primaryExpr", 
			"literal", "numericLiteral", "varRef", "varName", "parenthesizedExpr", 
			"contextItemExpr", "orderedExpr", "unorderedExpr", "functionCall", "argument", 
			"nodeConstructor", "directConstructor", "dirElemConstructorOpenClose", 
			"dirElemConstructorSingleTag", "dirAttributeList", "dirAttributeValueApos", 
			"dirAttributeValueQuot", "dirAttributeValue", "dirAttributeContentQuot", 
			"dirAttributeContentApos", "dirElemContent", "commonContent", "contentExpr", 
			"computedConstructor", "compMLJSONConstructor", "compMLJSONArrayConstructor", 
			"compMLJSONObjectConstructor", "compMLJSONNumberConstructor", "compMLJSONBooleanConstructor", 
			"compMLJSONNullConstructor", "compBinaryConstructor", "compDocConstructor", 
			"compElemConstructor", "enclosedContentExpr", "compAttrConstructor", 
			"compNamespaceConstructor", "prefix", "enclosedPrefixExpr", "enclosedURIExpr", 
			"compTextConstructor", "compCommentConstructor", "compPIConstructor", 
			"functionItemExpr", "namedFunctionRef", "inlineFunctionRef", "functionBody", 
			"mapConstructor", "mapConstructorEntry", "arrayConstructor", "squareArrayConstructor", 
			"curlyArrayConstructor", "stringConstructor", "stringConstructorContent", 
			"charNoGrave", "charNoLBrace", "charNoRBrack", "stringConstructorChars", 
			"stringConstructorInterpolation", "unaryLookup", "singleType", "typeDeclaration", 
			"sequenceType", "itemType", "atomicOrUnionType", "kindTest", "anyKindTest", 
			"binaryNodeTest", "documentTest", "textTest", "commentTest", "namespaceNodeTest", 
			"piTest", "attributeTest", "attributeNameOrWildcard", "schemaAttributeTest", 
			"elementTest", "elementNameOrWildcard", "schemaElementTest", "elementDeclaration", 
			"attributeName", "elementName", "simpleTypeName", "typeName", "functionTest", 
			"anyFunctionTest", "typedFunctionTest", "mapTest", "anyMapTest", "typedMapTest", 
			"arrayTest", "anyArrayTest", "typedArrayTest", "parenthesizedItemTest", 
			"attributeDeclaration", "mlNodeTest", "mlArrayNodeTest", "mlObjectNodeTest", 
			"mlNumberNodeTest", "mlBooleanNodeTest", "mlNullNodeTest", "eqName", 
			"qName", "ncName", "functionName", "keyword", "keywordNotOKForFunction", 
			"keywordOKForFunction", "uriLiteral", "stringLiteralQuot", "stringLiteralApos", 
			"stringLiteral", "stringContentQuot", "stringContentApos", "noQuotesNoBracesNoAmpNoLAng"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "EscapeQuot", "EscapeApos", "DOUBLE_LBRACE", "DOUBLE_RBRACE", "IntegerLiteral", 
			"DecimalLiteral", "DoubleLiteral", "DFPropertyName", "PredefinedEntityRef", 
			"CharRef", "Quot", "Apos", "COMMENT", "XMLDECL", "PI", "CDATA", "PRAGMA", 
			"WS", "EQUAL", "NOT_EQUAL", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", 
			"LBRACE", "RBRACE", "STAR", "PLUS", "MINUS", "COMMA", "DOT", "DDOT", 
			"COLON", "COLON_EQ", "SEMICOLON", "SLASH", "DSLASH", "BACKSLASH", "VBAR", 
			"LANGLE", "RANGLE", "QUESTION", "AT", "DOLLAR", "MOD", "BANG", "HASH", 
			"CARAT", "ARROW", "GRAVE", "CONCATENATION", "TILDE", "KW_ALLOWING", "KW_ANCESTOR", 
			"KW_ANCESTOR_OR_SELF", "KW_AND", "KW_ARRAY", "KW_AS", "KW_ASCENDING", 
			"KW_AT", "KW_ATTRIBUTE", "KW_BASE_URI", "KW_BOUNDARY_SPACE", "KW_BINARY", 
			"KW_BY", "KW_CASE", "KW_CAST", "KW_CASTABLE", "KW_CATCH", "KW_CHILD", 
			"KW_COLLATION", "KW_COMMENT", "KW_CONSTRUCTION", "KW_CONTEXT", "KW_COPY_NS", 
			"KW_COUNT", "KW_DECLARE", "KW_DEFAULT", "KW_DESCENDANT", "KW_DESCENDANT_OR_SELF", 
			"KW_DESCENDING", "KW_DECIMAL_FORMAT", "KW_DIV", "KW_DOCUMENT", "KW_DOCUMENT_NODE", 
			"KW_ELEMENT", "KW_ELSE", "KW_EMPTY", "KW_EMPTY_SEQUENCE", "KW_ENCODING", 
			"KW_END", "KW_EQ", "KW_EVERY", "KW_EXCEPT", "KW_EXTERNAL", "KW_FOLLOWING", 
			"KW_FOLLOWING_SIBLING", "KW_FOR", "KW_FUNCTION", "KW_GE", "KW_GREATEST", 
			"KW_GROUP", "KW_GT", "KW_IDIV", "KW_IF", "KW_IMPORT", "KW_IN", "KW_INHERIT", 
			"KW_INSTANCE", "KW_INTERSECT", "KW_IS", "KW_ITEM", "KW_LAX", "KW_LE", 
			"KW_LEAST", "KW_LET", "KW_LT", "KW_MAP", "KW_MOD", "KW_MODULE", "KW_NAMESPACE", 
			"KW_NE", "KW_NEXT", "KW_NAMESPACE_NODE", "KW_NO_INHERIT", "KW_NO_PRESERVE", 
			"KW_NODE", "KW_OF", "KW_ONLY", "KW_OPTION", "KW_OR", "KW_ORDER", "KW_ORDERED", 
			"KW_ORDERING", "KW_PARENT", "KW_PRECEDING", "KW_PRECEDING_SIBLING", "KW_PRESERVE", 
			"KW_PREVIOUS", "KW_PI", "KW_RETURN", "KW_SATISFIES", "KW_SCHEMA", "KW_SCHEMA_ATTR", 
			"KW_SCHEMA_ELEM", "KW_SELF", "KW_SLIDING", "KW_SOME", "KW_STABLE", "KW_START", 
			"KW_STRICT", "KW_STRIP", "KW_SWITCH", "KW_TEXT", "KW_THEN", "KW_TO", 
			"KW_TREAT", "KW_TRY", "KW_TUMBLING", "KW_TYPE", "KW_TYPESWITCH", "KW_UNION", 
			"KW_UNORDERED", "KW_UPDATE", "KW_VALIDATE", "KW_VARIABLE", "KW_VERSION", 
			"KW_WHEN", "KW_WHERE", "KW_WINDOW", "KW_XQUERY", "KW_BREAK", "KW_LOOP", 
			"KW_CONTINUE", "KW_EXIT", "KW_RETURNING", "KW_WHILE", "KW_ARRAY_NODE", 
			"KW_BOOLEAN_NODE", "KW_NULL_NODE", "KW_NUMBER_NODE", "KW_OBJECT_NODE", 
			"KW_REPLACE", "KW_WITH", "KW_VALUE", "KW_INSERT", "KW_INTO", "KW_DELETE", 
			"KW_RENAME", "URIQualifiedName", "FullQName", "NCNameWithLocalWildcard", 
			"NCNameWithPrefixWildcard", "NCName", "XQDOC_COMMENT_START", "XQDOC_COMMENT_END", 
			"XQDocComment", "XQComment", "CHAR", "ENTER_STRING", "EXIT_INTERPOLATION", 
			"ContentChar", "BASIC_CHAR", "ENTER_INTERPOLATION", "EXIT_STRING", "EscapeQuot_QuotString", 
			"DOUBLE_LBRACE_QuotString", "DOUBLE_RBRACE_QuotString", "EscapeApos_AposString"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "XQueryParserScripting.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public XQueryParserScripting(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleContext extends ParserRuleContext {
		public LibraryModuleContext libraryModule() {
			return getRuleContext(LibraryModuleContext.class,0);
		}
		public MainModuleContext mainModule() {
			return getRuleContext(MainModuleContext.class,0);
		}
		public List<XqDocCommentContext> xqDocComment() {
			return getRuleContexts(XqDocCommentContext.class);
		}
		public XqDocCommentContext xqDocComment(int i) {
			return getRuleContext(XqDocCommentContext.class,i);
		}
		public VersionDeclContext versionDecl() {
			return getRuleContext(VersionDeclContext.class,0);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_module);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(530);
				xqDocComment();
				}
				break;
			}
			setState(534);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(533);
				versionDecl();
				}
				break;
			}
			setState(537);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(536);
				xqDocComment();
				}
				break;
			}
			setState(541);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(539);
				libraryModule();
				}
				break;
			case 2:
				{
				setState(540);
				mainModule();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class XqDocCommentContext extends ParserRuleContext {
		public TerminalNode XQDocComment() { return getToken(XQueryParserScripting.XQDocComment, 0); }
		public XqDocCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xqDocComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterXqDocComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitXqDocComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitXqDocComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XqDocCommentContext xqDocComment() throws RecognitionException {
		XqDocCommentContext _localctx = new XqDocCommentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_xqDocComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(543);
			match(XQDocComment);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VersionDeclContext extends ParserRuleContext {
		public StringLiteralContext version;
		public StringLiteralContext encoding;
		public TerminalNode KW_XQUERY() { return getToken(XQueryParserScripting.KW_XQUERY, 0); }
		public TerminalNode KW_VERSION() { return getToken(XQueryParserScripting.KW_VERSION, 0); }
		public TerminalNode SEMICOLON() { return getToken(XQueryParserScripting.SEMICOLON, 0); }
		public List<StringLiteralContext> stringLiteral() {
			return getRuleContexts(StringLiteralContext.class);
		}
		public StringLiteralContext stringLiteral(int i) {
			return getRuleContext(StringLiteralContext.class,i);
		}
		public TerminalNode KW_ENCODING() { return getToken(XQueryParserScripting.KW_ENCODING, 0); }
		public VersionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_versionDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterVersionDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitVersionDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitVersionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VersionDeclContext versionDecl() throws RecognitionException {
		VersionDeclContext _localctx = new VersionDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_versionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			match(KW_XQUERY);
			setState(546);
			match(KW_VERSION);
			setState(547);
			((VersionDeclContext)_localctx).version = stringLiteral();
			setState(550);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_ENCODING) {
				{
				setState(548);
				match(KW_ENCODING);
				setState(549);
				((VersionDeclContext)_localctx).encoding = stringLiteral();
				}
			}

			setState(552);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MainModuleContext extends ParserRuleContext {
		public PrologContext prolog() {
			return getRuleContext(PrologContext.class,0);
		}
		public ProgramContext program() {
			return getRuleContext(ProgramContext.class,0);
		}
		public MainModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mainModule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMainModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMainModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMainModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainModuleContext mainModule() throws RecognitionException {
		MainModuleContext _localctx = new MainModuleContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_mainModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(554);
			prolog();
			setState(555);
			program();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public StatementsAndOptionalExprContext statementsAndOptionalExpr() {
			return getRuleContext(StatementsAndOptionalExprContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(557);
			statementsAndOptionalExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LibraryModuleContext extends ParserRuleContext {
		public ModuleDeclContext moduleDecl() {
			return getRuleContext(ModuleDeclContext.class,0);
		}
		public PrologContext prolog() {
			return getRuleContext(PrologContext.class,0);
		}
		public LibraryModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_libraryModule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterLibraryModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitLibraryModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitLibraryModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryModuleContext libraryModule() throws RecognitionException {
		LibraryModuleContext _localctx = new LibraryModuleContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_libraryModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559);
			moduleDecl();
			setState(560);
			prolog();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleDeclContext extends ParserRuleContext {
		public TerminalNode KW_MODULE() { return getToken(XQueryParserScripting.KW_MODULE, 0); }
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParserScripting.KW_NAMESPACE, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(XQueryParserScripting.EQUAL, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParserScripting.SEMICOLON, 0); }
		public ModuleDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterModuleDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitModuleDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitModuleDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleDeclContext moduleDecl() throws RecognitionException {
		ModuleDeclContext _localctx = new ModuleDeclContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_moduleDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(562);
			match(KW_MODULE);
			setState(563);
			match(KW_NAMESPACE);
			setState(564);
			ncName();
			setState(565);
			match(EQUAL);
			setState(566);
			uriLiteral();
			setState(567);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrologContext extends ParserRuleContext {
		public List<TerminalNode> SEMICOLON() { return getTokens(XQueryParserScripting.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(XQueryParserScripting.SEMICOLON, i);
		}
		public List<AnnotatedDeclContext> annotatedDecl() {
			return getRuleContexts(AnnotatedDeclContext.class);
		}
		public AnnotatedDeclContext annotatedDecl(int i) {
			return getRuleContext(AnnotatedDeclContext.class,i);
		}
		public List<DefaultNamespaceDeclContext> defaultNamespaceDecl() {
			return getRuleContexts(DefaultNamespaceDeclContext.class);
		}
		public DefaultNamespaceDeclContext defaultNamespaceDecl(int i) {
			return getRuleContext(DefaultNamespaceDeclContext.class,i);
		}
		public List<SetterContext> setter() {
			return getRuleContexts(SetterContext.class);
		}
		public SetterContext setter(int i) {
			return getRuleContext(SetterContext.class,i);
		}
		public List<NamespaceDeclContext> namespaceDecl() {
			return getRuleContexts(NamespaceDeclContext.class);
		}
		public NamespaceDeclContext namespaceDecl(int i) {
			return getRuleContext(NamespaceDeclContext.class,i);
		}
		public List<SchemaImportContext> schemaImport() {
			return getRuleContexts(SchemaImportContext.class);
		}
		public SchemaImportContext schemaImport(int i) {
			return getRuleContext(SchemaImportContext.class,i);
		}
		public List<ModuleImportContext> moduleImport() {
			return getRuleContexts(ModuleImportContext.class);
		}
		public ModuleImportContext moduleImport(int i) {
			return getRuleContext(ModuleImportContext.class,i);
		}
		public List<XqDocCommentContext> xqDocComment() {
			return getRuleContexts(XqDocCommentContext.class);
		}
		public XqDocCommentContext xqDocComment(int i) {
			return getRuleContext(XqDocCommentContext.class,i);
		}
		public PrologContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prolog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterProlog(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitProlog(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitProlog(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrologContext prolog() throws RecognitionException {
		PrologContext _localctx = new PrologContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_prolog);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(580);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(574);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						setState(569);
						defaultNamespaceDecl();
						}
						break;
					case 2:
						{
						setState(570);
						setter();
						}
						break;
					case 3:
						{
						setState(571);
						namespaceDecl();
						}
						break;
					case 4:
						{
						setState(572);
						schemaImport();
						}
						break;
					case 5:
						{
						setState(573);
						moduleImport();
						}
						break;
					}
					setState(576);
					match(SEMICOLON);
					}
					} 
				}
				setState(582);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(591);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(584);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==XQDocComment) {
						{
						setState(583);
						xqDocComment();
						}
					}

					setState(586);
					annotatedDecl();
					setState(587);
					match(SEMICOLON);
					}
					} 
				}
				setState(593);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotatedDeclContext extends ParserRuleContext {
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
		}
		public ContextItemDeclContext contextItemDecl() {
			return getRuleContext(ContextItemDeclContext.class,0);
		}
		public OptionDeclContext optionDecl() {
			return getRuleContext(OptionDeclContext.class,0);
		}
		public AnnotatedDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotatedDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnnotatedDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnnotatedDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnnotatedDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotatedDeclContext annotatedDecl() throws RecognitionException {
		AnnotatedDeclContext _localctx = new AnnotatedDeclContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_annotatedDecl);
		try {
			setState(598);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(594);
				varDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(595);
				functionDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(596);
				contextItemDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(597);
				optionDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefaultNamespaceDeclContext extends ParserRuleContext {
		public Token type;
		public StringLiteralContext uri;
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParserScripting.KW_NAMESPACE, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParserScripting.KW_ELEMENT, 0); }
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParserScripting.KW_FUNCTION, 0); }
		public DefaultNamespaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultNamespaceDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDefaultNamespaceDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDefaultNamespaceDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDefaultNamespaceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultNamespaceDeclContext defaultNamespaceDecl() throws RecognitionException {
		DefaultNamespaceDeclContext _localctx = new DefaultNamespaceDeclContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_defaultNamespaceDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(600);
			match(KW_DECLARE);
			setState(601);
			match(KW_DEFAULT);
			setState(602);
			((DefaultNamespaceDeclContext)_localctx).type = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==KW_ELEMENT || _la==KW_FUNCTION) ) {
				((DefaultNamespaceDeclContext)_localctx).type = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(603);
			match(KW_NAMESPACE);
			setState(604);
			((DefaultNamespaceDeclContext)_localctx).uri = stringLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SetterContext extends ParserRuleContext {
		public BoundarySpaceDeclContext boundarySpaceDecl() {
			return getRuleContext(BoundarySpaceDeclContext.class,0);
		}
		public DefaultCollationDeclContext defaultCollationDecl() {
			return getRuleContext(DefaultCollationDeclContext.class,0);
		}
		public BaseURIDeclContext baseURIDecl() {
			return getRuleContext(BaseURIDeclContext.class,0);
		}
		public ConstructionDeclContext constructionDecl() {
			return getRuleContext(ConstructionDeclContext.class,0);
		}
		public OrderingModeDeclContext orderingModeDecl() {
			return getRuleContext(OrderingModeDeclContext.class,0);
		}
		public EmptyOrderDeclContext emptyOrderDecl() {
			return getRuleContext(EmptyOrderDeclContext.class,0);
		}
		public CopyNamespacesDeclContext copyNamespacesDecl() {
			return getRuleContext(CopyNamespacesDeclContext.class,0);
		}
		public DecimalFormatDeclContext decimalFormatDecl() {
			return getRuleContext(DecimalFormatDeclContext.class,0);
		}
		public SetterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSetter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSetter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSetter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetterContext setter() throws RecognitionException {
		SetterContext _localctx = new SetterContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_setter);
		try {
			setState(614);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(606);
				boundarySpaceDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(607);
				defaultCollationDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(608);
				baseURIDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(609);
				constructionDecl();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(610);
				orderingModeDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(611);
				emptyOrderDecl();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(612);
				copyNamespacesDecl();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(613);
				decimalFormatDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BoundarySpaceDeclContext extends ParserRuleContext {
		public Token type;
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_BOUNDARY_SPACE() { return getToken(XQueryParserScripting.KW_BOUNDARY_SPACE, 0); }
		public TerminalNode KW_PRESERVE() { return getToken(XQueryParserScripting.KW_PRESERVE, 0); }
		public TerminalNode KW_STRIP() { return getToken(XQueryParserScripting.KW_STRIP, 0); }
		public BoundarySpaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boundarySpaceDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterBoundarySpaceDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitBoundarySpaceDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitBoundarySpaceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoundarySpaceDeclContext boundarySpaceDecl() throws RecognitionException {
		BoundarySpaceDeclContext _localctx = new BoundarySpaceDeclContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_boundarySpaceDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(616);
			match(KW_DECLARE);
			setState(617);
			match(KW_BOUNDARY_SPACE);
			setState(618);
			((BoundarySpaceDeclContext)_localctx).type = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==KW_PRESERVE || _la==KW_STRIP) ) {
				((BoundarySpaceDeclContext)_localctx).type = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefaultCollationDeclContext extends ParserRuleContext {
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_COLLATION() { return getToken(XQueryParserScripting.KW_COLLATION, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public DefaultCollationDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultCollationDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDefaultCollationDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDefaultCollationDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDefaultCollationDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultCollationDeclContext defaultCollationDecl() throws RecognitionException {
		DefaultCollationDeclContext _localctx = new DefaultCollationDeclContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(620);
			match(KW_DECLARE);
			setState(621);
			match(KW_DEFAULT);
			setState(622);
			match(KW_COLLATION);
			setState(623);
			uriLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BaseURIDeclContext extends ParserRuleContext {
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_BASE_URI() { return getToken(XQueryParserScripting.KW_BASE_URI, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public BaseURIDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseURIDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterBaseURIDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitBaseURIDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitBaseURIDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseURIDeclContext baseURIDecl() throws RecognitionException {
		BaseURIDeclContext _localctx = new BaseURIDeclContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_baseURIDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(625);
			match(KW_DECLARE);
			setState(626);
			match(KW_BASE_URI);
			setState(627);
			uriLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructionDeclContext extends ParserRuleContext {
		public Token type;
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_CONSTRUCTION() { return getToken(XQueryParserScripting.KW_CONSTRUCTION, 0); }
		public TerminalNode KW_STRIP() { return getToken(XQueryParserScripting.KW_STRIP, 0); }
		public TerminalNode KW_PRESERVE() { return getToken(XQueryParserScripting.KW_PRESERVE, 0); }
		public ConstructionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructionDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterConstructionDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitConstructionDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitConstructionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructionDeclContext constructionDecl() throws RecognitionException {
		ConstructionDeclContext _localctx = new ConstructionDeclContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_constructionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(629);
			match(KW_DECLARE);
			setState(630);
			match(KW_CONSTRUCTION);
			setState(631);
			((ConstructionDeclContext)_localctx).type = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==KW_PRESERVE || _la==KW_STRIP) ) {
				((ConstructionDeclContext)_localctx).type = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderingModeDeclContext extends ParserRuleContext {
		public Token type;
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_ORDERING() { return getToken(XQueryParserScripting.KW_ORDERING, 0); }
		public TerminalNode KW_ORDERED() { return getToken(XQueryParserScripting.KW_ORDERED, 0); }
		public TerminalNode KW_UNORDERED() { return getToken(XQueryParserScripting.KW_UNORDERED, 0); }
		public OrderingModeDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderingModeDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterOrderingModeDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitOrderingModeDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitOrderingModeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderingModeDeclContext orderingModeDecl() throws RecognitionException {
		OrderingModeDeclContext _localctx = new OrderingModeDeclContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(633);
			match(KW_DECLARE);
			setState(634);
			match(KW_ORDERING);
			setState(635);
			((OrderingModeDeclContext)_localctx).type = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==KW_ORDERED || _la==KW_UNORDERED) ) {
				((OrderingModeDeclContext)_localctx).type = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptyOrderDeclContext extends ParserRuleContext {
		public Token type;
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_ORDER() { return getToken(XQueryParserScripting.KW_ORDER, 0); }
		public TerminalNode KW_EMPTY() { return getToken(XQueryParserScripting.KW_EMPTY, 0); }
		public TerminalNode KW_GREATEST() { return getToken(XQueryParserScripting.KW_GREATEST, 0); }
		public TerminalNode KW_LEAST() { return getToken(XQueryParserScripting.KW_LEAST, 0); }
		public EmptyOrderDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyOrderDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterEmptyOrderDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitEmptyOrderDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitEmptyOrderDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyOrderDeclContext emptyOrderDecl() throws RecognitionException {
		EmptyOrderDeclContext _localctx = new EmptyOrderDeclContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			match(KW_DECLARE);
			setState(638);
			match(KW_DEFAULT);
			setState(639);
			match(KW_ORDER);
			setState(640);
			match(KW_EMPTY);
			setState(641);
			((EmptyOrderDeclContext)_localctx).type = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==KW_GREATEST || _la==KW_LEAST) ) {
				((EmptyOrderDeclContext)_localctx).type = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CopyNamespacesDeclContext extends ParserRuleContext {
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_COPY_NS() { return getToken(XQueryParserScripting.KW_COPY_NS, 0); }
		public PreserveModeContext preserveMode() {
			return getRuleContext(PreserveModeContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParserScripting.COMMA, 0); }
		public InheritModeContext inheritMode() {
			return getRuleContext(InheritModeContext.class,0);
		}
		public CopyNamespacesDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_copyNamespacesDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCopyNamespacesDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCopyNamespacesDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCopyNamespacesDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CopyNamespacesDeclContext copyNamespacesDecl() throws RecognitionException {
		CopyNamespacesDeclContext _localctx = new CopyNamespacesDeclContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_copyNamespacesDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(643);
			match(KW_DECLARE);
			setState(644);
			match(KW_COPY_NS);
			setState(645);
			preserveMode();
			setState(646);
			match(COMMA);
			setState(647);
			inheritMode();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PreserveModeContext extends ParserRuleContext {
		public TerminalNode KW_PRESERVE() { return getToken(XQueryParserScripting.KW_PRESERVE, 0); }
		public TerminalNode KW_NO_PRESERVE() { return getToken(XQueryParserScripting.KW_NO_PRESERVE, 0); }
		public PreserveModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_preserveMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPreserveMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPreserveMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPreserveMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PreserveModeContext preserveMode() throws RecognitionException {
		PreserveModeContext _localctx = new PreserveModeContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_preserveMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(649);
			_la = _input.LA(1);
			if ( !(_la==KW_NO_PRESERVE || _la==KW_PRESERVE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InheritModeContext extends ParserRuleContext {
		public TerminalNode KW_INHERIT() { return getToken(XQueryParserScripting.KW_INHERIT, 0); }
		public TerminalNode KW_NO_INHERIT() { return getToken(XQueryParserScripting.KW_NO_INHERIT, 0); }
		public InheritModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inheritMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterInheritMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitInheritMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitInheritMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InheritModeContext inheritMode() throws RecognitionException {
		InheritModeContext _localctx = new InheritModeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_inheritMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(651);
			_la = _input.LA(1);
			if ( !(_la==KW_INHERIT || _la==KW_NO_INHERIT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DecimalFormatDeclContext extends ParserRuleContext {
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public List<TerminalNode> DFPropertyName() { return getTokens(XQueryParserScripting.DFPropertyName); }
		public TerminalNode DFPropertyName(int i) {
			return getToken(XQueryParserScripting.DFPropertyName, i);
		}
		public List<TerminalNode> EQUAL() { return getTokens(XQueryParserScripting.EQUAL); }
		public TerminalNode EQUAL(int i) {
			return getToken(XQueryParserScripting.EQUAL, i);
		}
		public List<StringLiteralContext> stringLiteral() {
			return getRuleContexts(StringLiteralContext.class);
		}
		public StringLiteralContext stringLiteral(int i) {
			return getRuleContext(StringLiteralContext.class,i);
		}
		public TerminalNode KW_DECIMAL_FORMAT() { return getToken(XQueryParserScripting.KW_DECIMAL_FORMAT, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public DecimalFormatDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalFormatDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDecimalFormatDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDecimalFormatDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDecimalFormatDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalFormatDeclContext decimalFormatDecl() throws RecognitionException {
		DecimalFormatDeclContext _localctx = new DecimalFormatDeclContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(653);
			match(KW_DECLARE);
			setState(658);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_DECIMAL_FORMAT:
				{
				{
				setState(654);
				match(KW_DECIMAL_FORMAT);
				setState(655);
				eqName();
				}
				}
				break;
			case KW_DEFAULT:
				{
				{
				setState(656);
				match(KW_DEFAULT);
				setState(657);
				match(KW_DECIMAL_FORMAT);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(665);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DFPropertyName) {
				{
				{
				setState(660);
				match(DFPropertyName);
				setState(661);
				match(EQUAL);
				setState(662);
				stringLiteral();
				}
				}
				setState(667);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SchemaImportContext extends ParserRuleContext {
		public UriLiteralContext nsURI;
		public UriLiteralContext uriLiteral;
		public List<UriLiteralContext> locations = new ArrayList<UriLiteralContext>();
		public TerminalNode KW_IMPORT() { return getToken(XQueryParserScripting.KW_IMPORT, 0); }
		public TerminalNode KW_SCHEMA() { return getToken(XQueryParserScripting.KW_SCHEMA, 0); }
		public List<UriLiteralContext> uriLiteral() {
			return getRuleContexts(UriLiteralContext.class);
		}
		public UriLiteralContext uriLiteral(int i) {
			return getRuleContext(UriLiteralContext.class,i);
		}
		public SchemaPrefixContext schemaPrefix() {
			return getRuleContext(SchemaPrefixContext.class,0);
		}
		public TerminalNode KW_AT() { return getToken(XQueryParserScripting.KW_AT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public SchemaImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaImport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSchemaImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSchemaImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSchemaImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaImportContext schemaImport() throws RecognitionException {
		SchemaImportContext _localctx = new SchemaImportContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_schemaImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(668);
			match(KW_IMPORT);
			setState(669);
			match(KW_SCHEMA);
			setState(671);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_DEFAULT || _la==KW_NAMESPACE) {
				{
				setState(670);
				schemaPrefix();
				}
			}

			setState(673);
			((SchemaImportContext)_localctx).nsURI = uriLiteral();
			setState(683);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AT) {
				{
				setState(674);
				match(KW_AT);
				setState(675);
				((SchemaImportContext)_localctx).uriLiteral = uriLiteral();
				((SchemaImportContext)_localctx).locations.add(((SchemaImportContext)_localctx).uriLiteral);
				setState(680);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(676);
					match(COMMA);
					setState(677);
					((SchemaImportContext)_localctx).uriLiteral = uriLiteral();
					((SchemaImportContext)_localctx).locations.add(((SchemaImportContext)_localctx).uriLiteral);
					}
					}
					setState(682);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SchemaPrefixContext extends ParserRuleContext {
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParserScripting.KW_NAMESPACE, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(XQueryParserScripting.EQUAL, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParserScripting.KW_ELEMENT, 0); }
		public SchemaPrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaPrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSchemaPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSchemaPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSchemaPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaPrefixContext schemaPrefix() throws RecognitionException {
		SchemaPrefixContext _localctx = new SchemaPrefixContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_schemaPrefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(692);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_NAMESPACE:
				{
				setState(685);
				match(KW_NAMESPACE);
				setState(686);
				ncName();
				setState(687);
				match(EQUAL);
				}
				break;
			case KW_DEFAULT:
				{
				setState(689);
				match(KW_DEFAULT);
				setState(690);
				match(KW_ELEMENT);
				setState(691);
				match(KW_NAMESPACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModuleImportContext extends ParserRuleContext {
		public UriLiteralContext nsURI;
		public UriLiteralContext uriLiteral;
		public List<UriLiteralContext> locations = new ArrayList<UriLiteralContext>();
		public TerminalNode KW_IMPORT() { return getToken(XQueryParserScripting.KW_IMPORT, 0); }
		public TerminalNode KW_MODULE() { return getToken(XQueryParserScripting.KW_MODULE, 0); }
		public List<UriLiteralContext> uriLiteral() {
			return getRuleContexts(UriLiteralContext.class);
		}
		public UriLiteralContext uriLiteral(int i) {
			return getRuleContext(UriLiteralContext.class,i);
		}
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParserScripting.KW_NAMESPACE, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(XQueryParserScripting.EQUAL, 0); }
		public TerminalNode KW_AT() { return getToken(XQueryParserScripting.KW_AT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public ModuleImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleImport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterModuleImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitModuleImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitModuleImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleImportContext moduleImport() throws RecognitionException {
		ModuleImportContext _localctx = new ModuleImportContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(694);
			match(KW_IMPORT);
			setState(695);
			match(KW_MODULE);
			setState(700);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_NAMESPACE) {
				{
				setState(696);
				match(KW_NAMESPACE);
				setState(697);
				ncName();
				setState(698);
				match(EQUAL);
				}
			}

			setState(702);
			((ModuleImportContext)_localctx).nsURI = uriLiteral();
			setState(712);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AT) {
				{
				setState(703);
				match(KW_AT);
				setState(704);
				((ModuleImportContext)_localctx).uriLiteral = uriLiteral();
				((ModuleImportContext)_localctx).locations.add(((ModuleImportContext)_localctx).uriLiteral);
				setState(709);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(705);
					match(COMMA);
					setState(706);
					((ModuleImportContext)_localctx).uriLiteral = uriLiteral();
					((ModuleImportContext)_localctx).locations.add(((ModuleImportContext)_localctx).uriLiteral);
					}
					}
					setState(711);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceDeclContext extends ParserRuleContext {
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParserScripting.KW_NAMESPACE, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(XQueryParserScripting.EQUAL, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public NamespaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNamespaceDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNamespaceDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNamespaceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclContext namespaceDecl() throws RecognitionException {
		NamespaceDeclContext _localctx = new NamespaceDeclContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_namespaceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(714);
			match(KW_DECLARE);
			setState(715);
			match(KW_NAMESPACE);
			setState(716);
			ncName();
			setState(717);
			match(EQUAL);
			setState(718);
			uriLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclContext extends ParserRuleContext {
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode KW_VARIABLE() { return getToken(XQueryParserScripting.KW_VARIABLE, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParserScripting.COLON_EQ, 0); }
		public VarValueContext varValue() {
			return getRuleContext(VarValueContext.class,0);
		}
		public TerminalNode KW_EXTERNAL() { return getToken(XQueryParserScripting.KW_EXTERNAL, 0); }
		public VarDefaultValueContext varDefaultValue() {
			return getRuleContext(VarDefaultValueContext.class,0);
		}
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterVarDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitVarDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(720);
			match(KW_DECLARE);
			setState(721);
			annotations();
			setState(722);
			match(KW_VARIABLE);
			setState(723);
			match(DOLLAR);
			setState(724);
			varName();
			setState(726);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(725);
				typeDeclaration();
				}
			}

			setState(735);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLON_EQ:
				{
				{
				setState(728);
				match(COLON_EQ);
				setState(729);
				varValue();
				}
				}
				break;
			case KW_EXTERNAL:
				{
				{
				setState(730);
				match(KW_EXTERNAL);
				setState(733);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON_EQ) {
					{
					setState(731);
					match(COLON_EQ);
					setState(732);
					varDefaultValue();
					}
				}

				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarValueContext extends ParserRuleContext {
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public VarValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterVarValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitVarValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitVarValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarValueContext varValue() throws RecognitionException {
		VarValueContext _localctx = new VarValueContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_varValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(737);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDefaultValueContext extends ParserRuleContext {
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public VarDefaultValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDefaultValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterVarDefaultValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitVarDefaultValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitVarDefaultValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDefaultValueContext varDefaultValue() throws RecognitionException {
		VarDefaultValueContext _localctx = new VarDefaultValueContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_varDefaultValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(739);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContextItemDeclContext extends ParserRuleContext {
		public ExprSingleContext value;
		public ExprSingleContext defaultValue;
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_CONTEXT() { return getToken(XQueryParserScripting.KW_CONTEXT, 0); }
		public TerminalNode KW_ITEM() { return getToken(XQueryParserScripting.KW_ITEM, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParserScripting.COLON_EQ, 0); }
		public TerminalNode KW_EXTERNAL() { return getToken(XQueryParserScripting.KW_EXTERNAL, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ContextItemDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextItemDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterContextItemDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitContextItemDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitContextItemDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemDeclContext contextItemDecl() throws RecognitionException {
		ContextItemDeclContext _localctx = new ContextItemDeclContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_contextItemDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(741);
			match(KW_DECLARE);
			setState(742);
			match(KW_CONTEXT);
			setState(743);
			match(KW_ITEM);
			setState(746);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(744);
				match(KW_AS);
				setState(745);
				itemType();
				}
			}

			setState(755);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLON_EQ:
				{
				{
				setState(748);
				match(COLON_EQ);
				setState(749);
				((ContextItemDeclContext)_localctx).value = exprSingle();
				}
				}
				break;
			case KW_EXTERNAL:
				{
				{
				setState(750);
				match(KW_EXTERNAL);
				setState(753);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON_EQ) {
					{
					setState(751);
					match(COLON_EQ);
					setState(752);
					((ContextItemDeclContext)_localctx).defaultValue = exprSingle();
					}
				}

				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDeclContext extends ParserRuleContext {
		public EqNameContext name;
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParserScripting.KW_FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public TerminalNode KW_EXTERNAL() { return getToken(XQueryParserScripting.KW_EXTERNAL, 0); }
		public FunctionParamsContext functionParams() {
			return getRuleContext(FunctionParamsContext.class,0);
		}
		public FunctionReturnContext functionReturn() {
			return getRuleContext(FunctionReturnContext.class,0);
		}
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(757);
			match(KW_DECLARE);
			setState(758);
			annotations();
			setState(759);
			match(KW_FUNCTION);
			setState(760);
			((FunctionDeclContext)_localctx).name = eqName();
			setState(761);
			match(LPAREN);
			setState(763);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(762);
				functionParams();
				}
			}

			setState(765);
			match(RPAREN);
			setState(767);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(766);
				functionReturn();
				}
			}

			setState(771);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				{
				setState(769);
				functionBody();
				}
				break;
			case KW_EXTERNAL:
				{
				setState(770);
				match(KW_EXTERNAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionParamsContext extends ParserRuleContext {
		public List<FunctionParamContext> functionParam() {
			return getRuleContexts(FunctionParamContext.class);
		}
		public FunctionParamContext functionParam(int i) {
			return getRuleContext(FunctionParamContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public FunctionParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionParams; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionParams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionParams(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionParams(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionParamsContext functionParams() throws RecognitionException {
		FunctionParamsContext _localctx = new FunctionParamsContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_functionParams);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(773);
			functionParam();
			setState(778);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(774);
				match(COMMA);
				setState(775);
				functionParam();
				}
				}
				setState(780);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionParamContext extends ParserRuleContext {
		public QNameContext name;
		public TypeDeclarationContext type;
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public FunctionParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionParamContext functionParam() throws RecognitionException {
		FunctionParamContext _localctx = new FunctionParamContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_functionParam);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(781);
			match(DOLLAR);
			setState(782);
			((FunctionParamContext)_localctx).name = qName();
			setState(784);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(783);
				((FunctionParamContext)_localctx).type = typeDeclaration();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationsContext extends ParserRuleContext {
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public AnnotationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnnotations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnnotations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnnotations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationsContext annotations() throws RecognitionException {
		AnnotationsContext _localctx = new AnnotationsContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_annotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(789);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==MOD) {
				{
				{
				setState(786);
				annotation();
				}
				}
				setState(791);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationContext extends ParserRuleContext {
		public TerminalNode MOD() { return getToken(XQueryParserScripting.MOD, 0); }
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public AnnotListContext annotList() {
			return getRuleContext(AnnotListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(792);
			match(MOD);
			setState(793);
			qName();
			setState(798);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(794);
				match(LPAREN);
				setState(795);
				annotList();
				setState(796);
				match(RPAREN);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotListContext extends ParserRuleContext {
		public List<AnnotationParamContext> annotationParam() {
			return getRuleContexts(AnnotationParamContext.class);
		}
		public AnnotationParamContext annotationParam(int i) {
			return getRuleContext(AnnotationParamContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public AnnotListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnnotList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnnotList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnnotList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotListContext annotList() throws RecognitionException {
		AnnotListContext _localctx = new AnnotListContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_annotList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			annotationParam();
			setState(805);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(801);
				match(COMMA);
				setState(802);
				annotationParam();
				}
				}
				setState(807);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationParamContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public AnnotationParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnnotationParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnnotationParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnnotationParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationParamContext annotationParam() throws RecognitionException {
		AnnotationParamContext _localctx = new AnnotationParamContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_annotationParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(808);
			literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionReturnContext extends ParserRuleContext {
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public FunctionReturnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReturn; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionReturn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionReturn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionReturn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReturnContext functionReturn() throws RecognitionException {
		FunctionReturnContext _localctx = new FunctionReturnContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_functionReturn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(810);
			match(KW_AS);
			setState(811);
			sequenceType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OptionDeclContext extends ParserRuleContext {
		public QNameContext name;
		public StringLiteralContext value;
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_OPTION() { return getToken(XQueryParserScripting.KW_OPTION, 0); }
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public OptionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optionDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterOptionDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitOptionDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitOptionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptionDeclContext optionDecl() throws RecognitionException {
		OptionDeclContext _localctx = new OptionDeclContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_optionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(813);
			match(KW_DECLARE);
			setState(814);
			match(KW_OPTION);
			setState(815);
			((OptionDeclContext)_localctx).name = qName();
			setState(816);
			((OptionDeclContext)_localctx).value = stringLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementsContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStatements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStatements(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStatements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_statements);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(818);
					statement();
					}
					} 
				}
				setState(823);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementsAndExprContext extends ParserRuleContext {
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementsAndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementsAndExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStatementsAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStatementsAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStatementsAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsAndExprContext statementsAndExpr() throws RecognitionException {
		StatementsAndExprContext _localctx = new StatementsAndExprContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_statementsAndExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(824);
			statements();
			setState(825);
			expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementsAndOptionalExprContext extends ParserRuleContext {
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementsAndOptionalExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementsAndOptionalExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStatementsAndOptionalExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStatementsAndOptionalExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStatementsAndOptionalExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsAndOptionalExprContext statementsAndOptionalExpr() throws RecognitionException {
		StatementsAndOptionalExprContext _localctx = new StatementsAndOptionalExprContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_statementsAndOptionalExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(827);
			statements();
			setState(829);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
				{
				setState(828);
				expr();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_expr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(831);
			exprSingle();
			setState(836);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(832);
					match(COMMA);
					setState(833);
					exprSingle();
					}
					} 
				}
				setState(838);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprSingleContext extends ParserRuleContext {
		public FlworExprContext flworExpr() {
			return getRuleContext(FlworExprContext.class,0);
		}
		public ExprSimpleContext exprSimple() {
			return getRuleContext(ExprSimpleContext.class,0);
		}
		public SwitchExprContext switchExpr() {
			return getRuleContext(SwitchExprContext.class,0);
		}
		public TypeswitchExprContext typeswitchExpr() {
			return getRuleContext(TypeswitchExprContext.class,0);
		}
		public IfExprContext ifExpr() {
			return getRuleContext(IfExprContext.class,0);
		}
		public TryCatchExprContext tryCatchExpr() {
			return getRuleContext(TryCatchExprContext.class,0);
		}
		public ExprSingleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprSingle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExprSingle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExprSingle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExprSingle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSingleContext exprSingle() throws RecognitionException {
		ExprSingleContext _localctx = new ExprSingleContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_exprSingle);
		try {
			setState(845);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(839);
				flworExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(840);
				exprSimple();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(841);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(842);
				typeswitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(843);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(844);
				tryCatchExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprSimpleContext extends ParserRuleContext {
		public QuantifiedExprContext quantifiedExpr() {
			return getRuleContext(QuantifiedExprContext.class,0);
		}
		public OrExprContext orExpr() {
			return getRuleContext(OrExprContext.class,0);
		}
		public ExistUpdateExprContext existUpdateExpr() {
			return getRuleContext(ExistUpdateExprContext.class,0);
		}
		public ExprSimpleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprSimple; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExprSimple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExprSimple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExprSimple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSimpleContext exprSimple() throws RecognitionException {
		ExprSimpleContext _localctx = new ExprSimpleContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_exprSimple);
		try {
			setState(850);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(847);
				quantifiedExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(848);
				orExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(849);
				existUpdateExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockExprContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public StatementsAndExprContext statementsAndExpr() {
			return getRuleContext(StatementsAndExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public BlockExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterBlockExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitBlockExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitBlockExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockExprContext blockExpr() throws RecognitionException {
		BlockExprContext _localctx = new BlockExprContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_blockExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(852);
			match(LBRACE);
			setState(853);
			statementsAndExpr();
			setState(854);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlworExprContext extends ParserRuleContext {
		public InitialClauseContext initialClause() {
			return getRuleContext(InitialClauseContext.class,0);
		}
		public ReturnClauseContext returnClause() {
			return getRuleContext(ReturnClauseContext.class,0);
		}
		public List<IntermediateClauseContext> intermediateClause() {
			return getRuleContexts(IntermediateClauseContext.class);
		}
		public IntermediateClauseContext intermediateClause(int i) {
			return getRuleContext(IntermediateClauseContext.class,i);
		}
		public FlworExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flworExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFlworExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFlworExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFlworExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlworExprContext flworExpr() throws RecognitionException {
		FlworExprContext _localctx = new FlworExprContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_flworExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(856);
			initialClause();
			setState(860);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 72058693620858881L) != 0) || _la==KW_STABLE || _la==KW_WHERE) {
				{
				{
				setState(857);
				intermediateClause();
				}
				}
				setState(862);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(863);
			returnClause();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InitialClauseContext extends ParserRuleContext {
		public ForClauseContext forClause() {
			return getRuleContext(ForClauseContext.class,0);
		}
		public LetClauseContext letClause() {
			return getRuleContext(LetClauseContext.class,0);
		}
		public WindowClauseContext windowClause() {
			return getRuleContext(WindowClauseContext.class,0);
		}
		public InitialClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initialClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterInitialClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitInitialClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitInitialClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitialClauseContext initialClause() throws RecognitionException {
		InitialClauseContext _localctx = new InitialClauseContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_initialClause);
		try {
			setState(868);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(865);
				forClause();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(866);
				letClause();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(867);
				windowClause();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntermediateClauseContext extends ParserRuleContext {
		public InitialClauseContext initialClause() {
			return getRuleContext(InitialClauseContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public GroupByClauseContext groupByClause() {
			return getRuleContext(GroupByClauseContext.class,0);
		}
		public OrderByClauseContext orderByClause() {
			return getRuleContext(OrderByClauseContext.class,0);
		}
		public CountClauseContext countClause() {
			return getRuleContext(CountClauseContext.class,0);
		}
		public IntermediateClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intermediateClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterIntermediateClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitIntermediateClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitIntermediateClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntermediateClauseContext intermediateClause() throws RecognitionException {
		IntermediateClauseContext _localctx = new IntermediateClauseContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_intermediateClause);
		try {
			setState(875);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_FOR:
			case KW_LET:
				enterOuterAlt(_localctx, 1);
				{
				setState(870);
				initialClause();
				}
				break;
			case KW_WHERE:
				enterOuterAlt(_localctx, 2);
				{
				setState(871);
				whereClause();
				}
				break;
			case KW_GROUP:
				enterOuterAlt(_localctx, 3);
				{
				setState(872);
				groupByClause();
				}
				break;
			case KW_ORDER:
			case KW_STABLE:
				enterOuterAlt(_localctx, 4);
				{
				setState(873);
				orderByClause();
				}
				break;
			case KW_COUNT:
				enterOuterAlt(_localctx, 5);
				{
				setState(874);
				countClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForClauseContext extends ParserRuleContext {
		public ForBindingContext forBinding;
		public List<ForBindingContext> vars = new ArrayList<ForBindingContext>();
		public TerminalNode KW_FOR() { return getToken(XQueryParserScripting.KW_FOR, 0); }
		public List<ForBindingContext> forBinding() {
			return getRuleContexts(ForBindingContext.class);
		}
		public ForBindingContext forBinding(int i) {
			return getRuleContext(ForBindingContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public ForClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterForClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitForClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitForClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForClauseContext forClause() throws RecognitionException {
		ForClauseContext _localctx = new ForClauseContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(877);
			match(KW_FOR);
			setState(878);
			((ForClauseContext)_localctx).forBinding = forBinding();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forBinding);
			setState(883);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(879);
				match(COMMA);
				setState(880);
				((ForClauseContext)_localctx).forBinding = forBinding();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forBinding);
				}
				}
				setState(885);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForBindingContext extends ParserRuleContext {
		public VarNameContext name;
		public TypeDeclarationContext seq;
		public AllowingEmptyContext flag;
		public PositionalVarContext at;
		public ExprSingleContext ex;
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public TerminalNode KW_IN() { return getToken(XQueryParserScripting.KW_IN, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public AllowingEmptyContext allowingEmpty() {
			return getRuleContext(AllowingEmptyContext.class,0);
		}
		public PositionalVarContext positionalVar() {
			return getRuleContext(PositionalVarContext.class,0);
		}
		public ForBindingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forBinding; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterForBinding(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitForBinding(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitForBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForBindingContext forBinding() throws RecognitionException {
		ForBindingContext _localctx = new ForBindingContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_forBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(886);
			match(DOLLAR);
			setState(887);
			((ForBindingContext)_localctx).name = varName();
			setState(889);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(888);
				((ForBindingContext)_localctx).seq = typeDeclaration();
				}
			}

			setState(892);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_ALLOWING) {
				{
				setState(891);
				((ForBindingContext)_localctx).flag = allowingEmpty();
				}
			}

			setState(895);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AT) {
				{
				setState(894);
				((ForBindingContext)_localctx).at = positionalVar();
				}
			}

			setState(897);
			match(KW_IN);
			setState(898);
			((ForBindingContext)_localctx).ex = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AllowingEmptyContext extends ParserRuleContext {
		public TerminalNode KW_ALLOWING() { return getToken(XQueryParserScripting.KW_ALLOWING, 0); }
		public TerminalNode KW_EMPTY() { return getToken(XQueryParserScripting.KW_EMPTY, 0); }
		public AllowingEmptyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_allowingEmpty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAllowingEmpty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAllowingEmpty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAllowingEmpty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AllowingEmptyContext allowingEmpty() throws RecognitionException {
		AllowingEmptyContext _localctx = new AllowingEmptyContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_allowingEmpty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(900);
			match(KW_ALLOWING);
			setState(901);
			match(KW_EMPTY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PositionalVarContext extends ParserRuleContext {
		public VarNameContext pvar;
		public TerminalNode KW_AT() { return getToken(XQueryParserScripting.KW_AT, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public PositionalVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_positionalVar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPositionalVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPositionalVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPositionalVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PositionalVarContext positionalVar() throws RecognitionException {
		PositionalVarContext _localctx = new PositionalVarContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_positionalVar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(903);
			match(KW_AT);
			setState(904);
			match(DOLLAR);
			setState(905);
			((PositionalVarContext)_localctx).pvar = varName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LetClauseContext extends ParserRuleContext {
		public LetBindingContext letBinding;
		public List<LetBindingContext> vars = new ArrayList<LetBindingContext>();
		public TerminalNode KW_LET() { return getToken(XQueryParserScripting.KW_LET, 0); }
		public List<LetBindingContext> letBinding() {
			return getRuleContexts(LetBindingContext.class);
		}
		public LetBindingContext letBinding(int i) {
			return getRuleContext(LetBindingContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public LetClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterLetClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitLetClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitLetClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetClauseContext letClause() throws RecognitionException {
		LetClauseContext _localctx = new LetClauseContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(907);
			match(KW_LET);
			setState(908);
			((LetClauseContext)_localctx).letBinding = letBinding();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letBinding);
			setState(913);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(909);
				match(COMMA);
				setState(910);
				((LetClauseContext)_localctx).letBinding = letBinding();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letBinding);
				}
				}
				setState(915);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LetBindingContext extends ParserRuleContext {
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParserScripting.COLON_EQ, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public LetBindingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letBinding; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterLetBinding(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitLetBinding(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitLetBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetBindingContext letBinding() throws RecognitionException {
		LetBindingContext _localctx = new LetBindingContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(916);
			match(DOLLAR);
			setState(917);
			varName();
			setState(919);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(918);
				typeDeclaration();
				}
			}

			setState(921);
			match(COLON_EQ);
			setState(922);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WindowClauseContext extends ParserRuleContext {
		public TerminalNode KW_FOR() { return getToken(XQueryParserScripting.KW_FOR, 0); }
		public TumblingWindowClauseContext tumblingWindowClause() {
			return getRuleContext(TumblingWindowClauseContext.class,0);
		}
		public SlidingWindowClauseContext slidingWindowClause() {
			return getRuleContext(SlidingWindowClauseContext.class,0);
		}
		public WindowClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_windowClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterWindowClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitWindowClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitWindowClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowClauseContext windowClause() throws RecognitionException {
		WindowClauseContext _localctx = new WindowClauseContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_windowClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(924);
			match(KW_FOR);
			setState(927);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_TUMBLING:
				{
				setState(925);
				tumblingWindowClause();
				}
				break;
			case KW_SLIDING:
				{
				setState(926);
				slidingWindowClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TumblingWindowClauseContext extends ParserRuleContext {
		public QNameContext name;
		public TypeDeclarationContext type;
		public TerminalNode KW_TUMBLING() { return getToken(XQueryParserScripting.KW_TUMBLING, 0); }
		public TerminalNode KW_WINDOW() { return getToken(XQueryParserScripting.KW_WINDOW, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public TerminalNode KW_IN() { return getToken(XQueryParserScripting.KW_IN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public WindowStartConditionContext windowStartCondition() {
			return getRuleContext(WindowStartConditionContext.class,0);
		}
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public WindowEndConditionContext windowEndCondition() {
			return getRuleContext(WindowEndConditionContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public TumblingWindowClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tumblingWindowClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTumblingWindowClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTumblingWindowClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTumblingWindowClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TumblingWindowClauseContext tumblingWindowClause() throws RecognitionException {
		TumblingWindowClauseContext _localctx = new TumblingWindowClauseContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_tumblingWindowClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			match(KW_TUMBLING);
			setState(930);
			match(KW_WINDOW);
			setState(931);
			match(DOLLAR);
			setState(932);
			((TumblingWindowClauseContext)_localctx).name = qName();
			setState(934);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(933);
				((TumblingWindowClauseContext)_localctx).type = typeDeclaration();
				}
			}

			setState(936);
			match(KW_IN);
			setState(937);
			exprSingle();
			setState(938);
			windowStartCondition();
			setState(940);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_END || _la==KW_ONLY) {
				{
				setState(939);
				windowEndCondition();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SlidingWindowClauseContext extends ParserRuleContext {
		public QNameContext name;
		public TypeDeclarationContext type;
		public TerminalNode KW_SLIDING() { return getToken(XQueryParserScripting.KW_SLIDING, 0); }
		public TerminalNode KW_WINDOW() { return getToken(XQueryParserScripting.KW_WINDOW, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public TerminalNode KW_IN() { return getToken(XQueryParserScripting.KW_IN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public WindowStartConditionContext windowStartCondition() {
			return getRuleContext(WindowStartConditionContext.class,0);
		}
		public WindowEndConditionContext windowEndCondition() {
			return getRuleContext(WindowEndConditionContext.class,0);
		}
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public SlidingWindowClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slidingWindowClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSlidingWindowClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSlidingWindowClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSlidingWindowClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SlidingWindowClauseContext slidingWindowClause() throws RecognitionException {
		SlidingWindowClauseContext _localctx = new SlidingWindowClauseContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_slidingWindowClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(942);
			match(KW_SLIDING);
			setState(943);
			match(KW_WINDOW);
			setState(944);
			match(DOLLAR);
			setState(945);
			((SlidingWindowClauseContext)_localctx).name = qName();
			setState(947);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(946);
				((SlidingWindowClauseContext)_localctx).type = typeDeclaration();
				}
			}

			setState(949);
			match(KW_IN);
			setState(950);
			exprSingle();
			setState(951);
			windowStartCondition();
			setState(952);
			windowEndCondition();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WindowStartConditionContext extends ParserRuleContext {
		public TerminalNode KW_START() { return getToken(XQueryParserScripting.KW_START, 0); }
		public WindowVarsContext windowVars() {
			return getRuleContext(WindowVarsContext.class,0);
		}
		public TerminalNode KW_WHEN() { return getToken(XQueryParserScripting.KW_WHEN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public WindowStartConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_windowStartCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterWindowStartCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitWindowStartCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitWindowStartCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowStartConditionContext windowStartCondition() throws RecognitionException {
		WindowStartConditionContext _localctx = new WindowStartConditionContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_windowStartCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(954);
			match(KW_START);
			setState(955);
			windowVars();
			setState(956);
			match(KW_WHEN);
			setState(957);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WindowEndConditionContext extends ParserRuleContext {
		public TerminalNode KW_END() { return getToken(XQueryParserScripting.KW_END, 0); }
		public WindowVarsContext windowVars() {
			return getRuleContext(WindowVarsContext.class,0);
		}
		public TerminalNode KW_WHEN() { return getToken(XQueryParserScripting.KW_WHEN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode KW_ONLY() { return getToken(XQueryParserScripting.KW_ONLY, 0); }
		public WindowEndConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_windowEndCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterWindowEndCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitWindowEndCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitWindowEndCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowEndConditionContext windowEndCondition() throws RecognitionException {
		WindowEndConditionContext _localctx = new WindowEndConditionContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_windowEndCondition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(960);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_ONLY) {
				{
				setState(959);
				match(KW_ONLY);
				}
			}

			setState(962);
			match(KW_END);
			setState(963);
			windowVars();
			setState(964);
			match(KW_WHEN);
			setState(965);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WindowVarsContext extends ParserRuleContext {
		public EqNameContext currentItem;
		public EqNameContext previousItem;
		public EqNameContext nextItem;
		public List<TerminalNode> DOLLAR() { return getTokens(XQueryParserScripting.DOLLAR); }
		public TerminalNode DOLLAR(int i) {
			return getToken(XQueryParserScripting.DOLLAR, i);
		}
		public PositionalVarContext positionalVar() {
			return getRuleContext(PositionalVarContext.class,0);
		}
		public TerminalNode KW_PREVIOUS() { return getToken(XQueryParserScripting.KW_PREVIOUS, 0); }
		public TerminalNode KW_NEXT() { return getToken(XQueryParserScripting.KW_NEXT, 0); }
		public List<EqNameContext> eqName() {
			return getRuleContexts(EqNameContext.class);
		}
		public EqNameContext eqName(int i) {
			return getRuleContext(EqNameContext.class,i);
		}
		public WindowVarsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_windowVars; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterWindowVars(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitWindowVars(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitWindowVars(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowVarsContext windowVars() throws RecognitionException {
		WindowVarsContext _localctx = new WindowVarsContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_windowVars);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(969);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(967);
				match(DOLLAR);
				setState(968);
				((WindowVarsContext)_localctx).currentItem = eqName();
				}
			}

			setState(972);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AT) {
				{
				setState(971);
				positionalVar();
				}
			}

			setState(977);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_PREVIOUS) {
				{
				setState(974);
				match(KW_PREVIOUS);
				setState(975);
				match(DOLLAR);
				setState(976);
				((WindowVarsContext)_localctx).previousItem = eqName();
				}
			}

			setState(982);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_NEXT) {
				{
				setState(979);
				match(KW_NEXT);
				setState(980);
				match(DOLLAR);
				setState(981);
				((WindowVarsContext)_localctx).nextItem = eqName();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CountClauseContext extends ParserRuleContext {
		public TerminalNode KW_COUNT() { return getToken(XQueryParserScripting.KW_COUNT, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public CountClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_countClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCountClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCountClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCountClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountClauseContext countClause() throws RecognitionException {
		CountClauseContext _localctx = new CountClauseContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(984);
			match(KW_COUNT);
			setState(985);
			match(DOLLAR);
			setState(986);
			varName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereClauseContext extends ParserRuleContext {
		public ExprSingleContext whereExpr;
		public TerminalNode KW_WHERE() { return getToken(XQueryParserScripting.KW_WHERE, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitWhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(988);
			match(KW_WHERE);
			setState(989);
			((WhereClauseContext)_localctx).whereExpr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupByClauseContext extends ParserRuleContext {
		public TerminalNode KW_GROUP() { return getToken(XQueryParserScripting.KW_GROUP, 0); }
		public TerminalNode KW_BY() { return getToken(XQueryParserScripting.KW_BY, 0); }
		public GroupingSpecListContext groupingSpecList() {
			return getRuleContext(GroupingSpecListContext.class,0);
		}
		public GroupByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterGroupByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitGroupByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitGroupByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByClauseContext groupByClause() throws RecognitionException {
		GroupByClauseContext _localctx = new GroupByClauseContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_groupByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(991);
			match(KW_GROUP);
			setState(992);
			match(KW_BY);
			setState(993);
			groupingSpecList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupingSpecListContext extends ParserRuleContext {
		public GroupingSpecContext groupingSpec;
		public List<GroupingSpecContext> vars = new ArrayList<GroupingSpecContext>();
		public List<GroupingSpecContext> groupingSpec() {
			return getRuleContexts(GroupingSpecContext.class);
		}
		public GroupingSpecContext groupingSpec(int i) {
			return getRuleContext(GroupingSpecContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public GroupingSpecListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupingSpecList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterGroupingSpecList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitGroupingSpecList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitGroupingSpecList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupingSpecListContext groupingSpecList() throws RecognitionException {
		GroupingSpecListContext _localctx = new GroupingSpecListContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_groupingSpecList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(995);
			((GroupingSpecListContext)_localctx).groupingSpec = groupingSpec();
			((GroupingSpecListContext)_localctx).vars.add(((GroupingSpecListContext)_localctx).groupingSpec);
			setState(1000);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(996);
				match(COMMA);
				setState(997);
				((GroupingSpecListContext)_localctx).groupingSpec = groupingSpec();
				((GroupingSpecListContext)_localctx).vars.add(((GroupingSpecListContext)_localctx).groupingSpec);
				}
				}
				setState(1002);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupingSpecContext extends ParserRuleContext {
		public VarNameContext name;
		public TypeDeclarationContext type;
		public UriLiteralContext uri;
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParserScripting.COLON_EQ, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode KW_COLLATION() { return getToken(XQueryParserScripting.KW_COLLATION, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public GroupingSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupingSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterGroupingSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitGroupingSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitGroupingSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupingSpecContext groupingSpec() throws RecognitionException {
		GroupingSpecContext _localctx = new GroupingSpecContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_groupingSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1003);
			match(DOLLAR);
			setState(1004);
			((GroupingSpecContext)_localctx).name = varName();
			setState(1010);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON_EQ || _la==KW_AS) {
				{
				setState(1006);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KW_AS) {
					{
					setState(1005);
					((GroupingSpecContext)_localctx).type = typeDeclaration();
					}
				}

				setState(1008);
				match(COLON_EQ);
				setState(1009);
				exprSingle();
				}
			}

			setState(1014);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_COLLATION) {
				{
				setState(1012);
				match(KW_COLLATION);
				setState(1013);
				((GroupingSpecContext)_localctx).uri = uriLiteral();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderByClauseContext extends ParserRuleContext {
		public OrderSpecContext orderSpec;
		public List<OrderSpecContext> specs = new ArrayList<OrderSpecContext>();
		public TerminalNode KW_ORDER() { return getToken(XQueryParserScripting.KW_ORDER, 0); }
		public TerminalNode KW_BY() { return getToken(XQueryParserScripting.KW_BY, 0); }
		public List<OrderSpecContext> orderSpec() {
			return getRuleContexts(OrderSpecContext.class);
		}
		public OrderSpecContext orderSpec(int i) {
			return getRuleContext(OrderSpecContext.class,i);
		}
		public TerminalNode KW_STABLE() { return getToken(XQueryParserScripting.KW_STABLE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterOrderByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitOrderByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1017);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_STABLE) {
				{
				setState(1016);
				match(KW_STABLE);
				}
			}

			setState(1019);
			match(KW_ORDER);
			setState(1020);
			match(KW_BY);
			setState(1021);
			((OrderByClauseContext)_localctx).orderSpec = orderSpec();
			((OrderByClauseContext)_localctx).specs.add(((OrderByClauseContext)_localctx).orderSpec);
			setState(1026);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1022);
				match(COMMA);
				setState(1023);
				((OrderByClauseContext)_localctx).orderSpec = orderSpec();
				((OrderByClauseContext)_localctx).specs.add(((OrderByClauseContext)_localctx).orderSpec);
				}
				}
				setState(1028);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderSpecContext extends ParserRuleContext {
		public ExprSingleContext ex;
		public Token desc;
		public Token gr;
		public Token ls;
		public UriLiteralContext uril;
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode KW_ASCENDING() { return getToken(XQueryParserScripting.KW_ASCENDING, 0); }
		public TerminalNode KW_EMPTY() { return getToken(XQueryParserScripting.KW_EMPTY, 0); }
		public TerminalNode KW_COLLATION() { return getToken(XQueryParserScripting.KW_COLLATION, 0); }
		public TerminalNode KW_DESCENDING() { return getToken(XQueryParserScripting.KW_DESCENDING, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public TerminalNode KW_GREATEST() { return getToken(XQueryParserScripting.KW_GREATEST, 0); }
		public TerminalNode KW_LEAST() { return getToken(XQueryParserScripting.KW_LEAST, 0); }
		public OrderSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterOrderSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitOrderSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitOrderSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderSpecContext orderSpec() throws RecognitionException {
		OrderSpecContext _localctx = new OrderSpecContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_orderSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1029);
			((OrderSpecContext)_localctx).ex = exprSingle();
			setState(1032);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ASCENDING:
				{
				setState(1030);
				match(KW_ASCENDING);
				}
				break;
			case KW_DESCENDING:
				{
				setState(1031);
				((OrderSpecContext)_localctx).desc = match(KW_DESCENDING);
				}
				break;
			case COMMA:
			case KW_COLLATION:
			case KW_COUNT:
			case KW_EMPTY:
			case KW_FOR:
			case KW_GROUP:
			case KW_LET:
			case KW_ORDER:
			case KW_RETURN:
			case KW_STABLE:
			case KW_WHERE:
				break;
			default:
				break;
			}
			setState(1039);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_EMPTY) {
				{
				setState(1034);
				match(KW_EMPTY);
				setState(1037);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case KW_GREATEST:
					{
					setState(1035);
					((OrderSpecContext)_localctx).gr = match(KW_GREATEST);
					}
					break;
				case KW_LEAST:
					{
					setState(1036);
					((OrderSpecContext)_localctx).ls = match(KW_LEAST);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(1043);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_COLLATION) {
				{
				setState(1041);
				match(KW_COLLATION);
				setState(1042);
				((OrderSpecContext)_localctx).uril = uriLiteral();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnClauseContext extends ParserRuleContext {
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ReturnClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterReturnClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitReturnClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitReturnClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnClauseContext returnClause() throws RecognitionException {
		ReturnClauseContext _localctx = new ReturnClauseContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_returnClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1045);
			match(KW_RETURN);
			setState(1046);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QuantifiedExprContext extends ParserRuleContext {
		public Token so;
		public Token ev;
		public QuantifiedVarContext quantifiedVar;
		public List<QuantifiedVarContext> vars = new ArrayList<QuantifiedVarContext>();
		public ExprSingleContext value;
		public TerminalNode KW_SATISFIES() { return getToken(XQueryParserScripting.KW_SATISFIES, 0); }
		public List<QuantifiedVarContext> quantifiedVar() {
			return getRuleContexts(QuantifiedVarContext.class);
		}
		public QuantifiedVarContext quantifiedVar(int i) {
			return getRuleContext(QuantifiedVarContext.class,i);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode KW_SOME() { return getToken(XQueryParserScripting.KW_SOME, 0); }
		public TerminalNode KW_EVERY() { return getToken(XQueryParserScripting.KW_EVERY, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public QuantifiedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantifiedExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterQuantifiedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitQuantifiedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitQuantifiedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedExprContext quantifiedExpr() throws RecognitionException {
		QuantifiedExprContext _localctx = new QuantifiedExprContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1050);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_SOME:
				{
				setState(1048);
				((QuantifiedExprContext)_localctx).so = match(KW_SOME);
				}
				break;
			case KW_EVERY:
				{
				setState(1049);
				((QuantifiedExprContext)_localctx).ev = match(KW_EVERY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1052);
			((QuantifiedExprContext)_localctx).quantifiedVar = quantifiedVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedVar);
			setState(1057);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1053);
				match(COMMA);
				setState(1054);
				((QuantifiedExprContext)_localctx).quantifiedVar = quantifiedVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedVar);
				}
				}
				setState(1059);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1060);
			match(KW_SATISFIES);
			setState(1061);
			((QuantifiedExprContext)_localctx).value = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QuantifiedVarContext extends ParserRuleContext {
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode KW_IN() { return getToken(XQueryParserScripting.KW_IN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public QuantifiedVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantifiedVar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterQuantifiedVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitQuantifiedVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitQuantifiedVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedVarContext quantifiedVar() throws RecognitionException {
		QuantifiedVarContext _localctx = new QuantifiedVarContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_quantifiedVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1063);
			match(DOLLAR);
			setState(1064);
			varName();
			setState(1066);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(1065);
				typeDeclaration();
				}
			}

			setState(1068);
			match(KW_IN);
			setState(1069);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchExprContext extends ParserRuleContext {
		public ExprContext cond;
		public SwitchCaseClauseContext switchCaseClause;
		public List<SwitchCaseClauseContext> cases = new ArrayList<SwitchCaseClauseContext>();
		public ExprSingleContext def;
		public TerminalNode KW_SWITCH() { return getToken(XQueryParserScripting.KW_SWITCH, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public List<SwitchCaseClauseContext> switchCaseClause() {
			return getRuleContexts(SwitchCaseClauseContext.class);
		}
		public SwitchCaseClauseContext switchCaseClause(int i) {
			return getRuleContext(SwitchCaseClauseContext.class,i);
		}
		public SwitchExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSwitchExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSwitchExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSwitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchExprContext switchExpr() throws RecognitionException {
		SwitchExprContext _localctx = new SwitchExprContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1071);
			match(KW_SWITCH);
			setState(1072);
			match(LPAREN);
			setState(1073);
			((SwitchExprContext)_localctx).cond = expr();
			setState(1074);
			match(RPAREN);
			setState(1076); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1075);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(1078); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1080);
			match(KW_DEFAULT);
			setState(1081);
			match(KW_RETURN);
			setState(1082);
			((SwitchExprContext)_localctx).def = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchCaseClauseContext extends ParserRuleContext {
		public SwitchCaseOperandContext switchCaseOperand;
		public List<SwitchCaseOperandContext> cond = new ArrayList<SwitchCaseOperandContext>();
		public ExprSingleContext ret;
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public List<TerminalNode> KW_CASE() { return getTokens(XQueryParserScripting.KW_CASE); }
		public TerminalNode KW_CASE(int i) {
			return getToken(XQueryParserScripting.KW_CASE, i);
		}
		public List<SwitchCaseOperandContext> switchCaseOperand() {
			return getRuleContexts(SwitchCaseOperandContext.class);
		}
		public SwitchCaseOperandContext switchCaseOperand(int i) {
			return getRuleContext(SwitchCaseOperandContext.class,i);
		}
		public SwitchCaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCaseClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSwitchCaseClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSwitchCaseClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSwitchCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseClauseContext switchCaseClause() throws RecognitionException {
		SwitchCaseClauseContext _localctx = new SwitchCaseClauseContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1086); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1084);
				match(KW_CASE);
				setState(1085);
				((SwitchCaseClauseContext)_localctx).switchCaseOperand = switchCaseOperand();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).switchCaseOperand);
				}
				}
				setState(1088); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1090);
			match(KW_RETURN);
			setState(1091);
			((SwitchCaseClauseContext)_localctx).ret = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchCaseOperandContext extends ParserRuleContext {
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public SwitchCaseOperandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCaseOperand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSwitchCaseOperand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSwitchCaseOperand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSwitchCaseOperand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseOperandContext switchCaseOperand() throws RecognitionException {
		SwitchCaseOperandContext _localctx = new SwitchCaseOperandContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_switchCaseOperand);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1093);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeswitchExprContext extends ParserRuleContext {
		public ExprContext cond;
		public CaseClauseContext caseClause;
		public List<CaseClauseContext> cses = new ArrayList<CaseClauseContext>();
		public VarNameContext var_ref;
		public ExprSingleContext def;
		public TerminalNode KW_TYPESWITCH() { return getToken(XQueryParserScripting.KW_TYPESWITCH, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public List<CaseClauseContext> caseClause() {
			return getRuleContexts(CaseClauseContext.class);
		}
		public CaseClauseContext caseClause(int i) {
			return getRuleContext(CaseClauseContext.class,i);
		}
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TypeswitchExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeswitchExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTypeswitchExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTypeswitchExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTypeswitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeswitchExprContext typeswitchExpr() throws RecognitionException {
		TypeswitchExprContext _localctx = new TypeswitchExprContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_typeswitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1095);
			match(KW_TYPESWITCH);
			setState(1096);
			match(LPAREN);
			setState(1097);
			((TypeswitchExprContext)_localctx).cond = expr();
			setState(1098);
			match(RPAREN);
			setState(1100); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1099);
				((TypeswitchExprContext)_localctx).caseClause = caseClause();
				((TypeswitchExprContext)_localctx).cses.add(((TypeswitchExprContext)_localctx).caseClause);
				}
				}
				setState(1102); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1104);
			match(KW_DEFAULT);
			setState(1107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(1105);
				match(DOLLAR);
				setState(1106);
				((TypeswitchExprContext)_localctx).var_ref = varName();
				}
			}

			setState(1109);
			match(KW_RETURN);
			setState(1110);
			((TypeswitchExprContext)_localctx).def = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaseClauseContext extends ParserRuleContext {
		public VarNameContext var_ref;
		public SequenceUnionTypeContext union;
		public ExprSingleContext ret;
		public TerminalNode KW_CASE() { return getToken(XQueryParserScripting.KW_CASE, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public SequenceUnionTypeContext sequenceUnionType() {
			return getRuleContext(SequenceUnionTypeContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public CaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCaseClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCaseClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseClauseContext caseClause() throws RecognitionException {
		CaseClauseContext _localctx = new CaseClauseContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1112);
			match(KW_CASE);
			setState(1117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(1113);
				match(DOLLAR);
				setState(1114);
				((CaseClauseContext)_localctx).var_ref = varName();
				setState(1115);
				match(KW_AS);
				}
			}

			setState(1119);
			((CaseClauseContext)_localctx).union = sequenceUnionType();
			setState(1120);
			match(KW_RETURN);
			setState(1121);
			((CaseClauseContext)_localctx).ret = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SequenceUnionTypeContext extends ParserRuleContext {
		public SequenceTypeContext sequenceType;
		public List<SequenceTypeContext> seq = new ArrayList<SequenceTypeContext>();
		public List<SequenceTypeContext> sequenceType() {
			return getRuleContexts(SequenceTypeContext.class);
		}
		public SequenceTypeContext sequenceType(int i) {
			return getRuleContext(SequenceTypeContext.class,i);
		}
		public List<TerminalNode> VBAR() { return getTokens(XQueryParserScripting.VBAR); }
		public TerminalNode VBAR(int i) {
			return getToken(XQueryParserScripting.VBAR, i);
		}
		public SequenceUnionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceUnionType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSequenceUnionType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSequenceUnionType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSequenceUnionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceUnionTypeContext sequenceUnionType() throws RecognitionException {
		SequenceUnionTypeContext _localctx = new SequenceUnionTypeContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_sequenceUnionType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1123);
			((SequenceUnionTypeContext)_localctx).sequenceType = sequenceType();
			((SequenceUnionTypeContext)_localctx).seq.add(((SequenceUnionTypeContext)_localctx).sequenceType);
			setState(1128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VBAR) {
				{
				{
				setState(1124);
				match(VBAR);
				setState(1125);
				((SequenceUnionTypeContext)_localctx).sequenceType = sequenceType();
				((SequenceUnionTypeContext)_localctx).seq.add(((SequenceUnionTypeContext)_localctx).sequenceType);
				}
				}
				setState(1130);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfExprContext extends ParserRuleContext {
		public ExprContext test_condition;
		public ExprSingleContext branch;
		public ExprSingleContext else_branch;
		public TerminalNode KW_IF() { return getToken(XQueryParserScripting.KW_IF, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TerminalNode KW_THEN() { return getToken(XQueryParserScripting.KW_THEN, 0); }
		public TerminalNode KW_ELSE() { return getToken(XQueryParserScripting.KW_ELSE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public IfExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterIfExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitIfExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitIfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfExprContext ifExpr() throws RecognitionException {
		IfExprContext _localctx = new IfExprContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1131);
			match(KW_IF);
			setState(1132);
			match(LPAREN);
			setState(1133);
			((IfExprContext)_localctx).test_condition = expr();
			setState(1134);
			match(RPAREN);
			setState(1135);
			match(KW_THEN);
			setState(1136);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(1137);
			match(KW_ELSE);
			setState(1138);
			((IfExprContext)_localctx).else_branch = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TryCatchExprContext extends ParserRuleContext {
		public CatchClauseContext catchClause;
		public List<CatchClauseContext> catches = new ArrayList<CatchClauseContext>();
		public TryClauseContext tryClause() {
			return getRuleContext(TryClauseContext.class,0);
		}
		public List<CatchClauseContext> catchClause() {
			return getRuleContexts(CatchClauseContext.class);
		}
		public CatchClauseContext catchClause(int i) {
			return getRuleContext(CatchClauseContext.class,i);
		}
		public TryCatchExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryCatchExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTryCatchExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTryCatchExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTryCatchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryCatchExprContext tryCatchExpr() throws RecognitionException {
		TryCatchExprContext _localctx = new TryCatchExprContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_tryCatchExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1140);
			tryClause();
			setState(1142); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1141);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1144); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TryClauseContext extends ParserRuleContext {
		public TerminalNode KW_TRY() { return getToken(XQueryParserScripting.KW_TRY, 0); }
		public EnclosedTryTargetExpressionContext enclosedTryTargetExpression() {
			return getRuleContext(EnclosedTryTargetExpressionContext.class,0);
		}
		public TryClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTryClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTryClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTryClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryClauseContext tryClause() throws RecognitionException {
		TryClauseContext _localctx = new TryClauseContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_tryClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1146);
			match(KW_TRY);
			setState(1147);
			enclosedTryTargetExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnclosedTryTargetExpressionContext extends ParserRuleContext {
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public EnclosedTryTargetExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enclosedTryTargetExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterEnclosedTryTargetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitEnclosedTryTargetExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitEnclosedTryTargetExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedTryTargetExpressionContext enclosedTryTargetExpression() throws RecognitionException {
		EnclosedTryTargetExpressionContext _localctx = new EnclosedTryTargetExpressionContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_enclosedTryTargetExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CatchClauseContext extends ParserRuleContext {
		public TerminalNode KW_CATCH() { return getToken(XQueryParserScripting.KW_CATCH, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public CatchErrorListContext catchErrorList() {
			return getRuleContext(CatchErrorListContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCatchClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCatchClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCatchClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_catchClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1151);
			match(KW_CATCH);
			setState(1158);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case STAR:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case URIQualifiedName:
			case FullQName:
			case NCNameWithLocalWildcard:
			case NCNameWithPrefixWildcard:
			case NCName:
				{
				setState(1152);
				catchErrorList();
				}
				break;
			case LPAREN:
				{
				{
				setState(1153);
				match(LPAREN);
				setState(1154);
				match(DOLLAR);
				setState(1155);
				varName();
				setState(1156);
				match(RPAREN);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1160);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnclosedExpressionContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public EnclosedExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enclosedExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterEnclosedExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitEnclosedExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitEnclosedExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedExpressionContext enclosedExpression() throws RecognitionException {
		EnclosedExpressionContext _localctx = new EnclosedExpressionContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_enclosedExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1162);
			match(LBRACE);
			setState(1164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
				{
				setState(1163);
				expr();
				}
			}

			setState(1166);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CatchErrorListContext extends ParserRuleContext {
		public NameTestContext nameTest;
		public List<NameTestContext> errors = new ArrayList<NameTestContext>();
		public List<NameTestContext> nameTest() {
			return getRuleContexts(NameTestContext.class);
		}
		public NameTestContext nameTest(int i) {
			return getRuleContext(NameTestContext.class,i);
		}
		public List<TerminalNode> VBAR() { return getTokens(XQueryParserScripting.VBAR); }
		public TerminalNode VBAR(int i) {
			return getToken(XQueryParserScripting.VBAR, i);
		}
		public CatchErrorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchErrorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCatchErrorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCatchErrorList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCatchErrorList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchErrorListContext catchErrorList() throws RecognitionException {
		CatchErrorListContext _localctx = new CatchErrorListContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_catchErrorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1168);
			((CatchErrorListContext)_localctx).nameTest = nameTest();
			((CatchErrorListContext)_localctx).errors.add(((CatchErrorListContext)_localctx).nameTest);
			setState(1173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VBAR) {
				{
				{
				setState(1169);
				match(VBAR);
				setState(1170);
				((CatchErrorListContext)_localctx).nameTest = nameTest();
				((CatchErrorListContext)_localctx).errors.add(((CatchErrorListContext)_localctx).nameTest);
				}
				}
				setState(1175);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExistUpdateExprContext extends ParserRuleContext {
		public TerminalNode KW_UPDATE() { return getToken(XQueryParserScripting.KW_UPDATE, 0); }
		public ExistReplaceExprContext existReplaceExpr() {
			return getRuleContext(ExistReplaceExprContext.class,0);
		}
		public ExistValueExprContext existValueExpr() {
			return getRuleContext(ExistValueExprContext.class,0);
		}
		public ExistInsertExprContext existInsertExpr() {
			return getRuleContext(ExistInsertExprContext.class,0);
		}
		public ExistDeleteExprContext existDeleteExpr() {
			return getRuleContext(ExistDeleteExprContext.class,0);
		}
		public ExistRenameExprContext existRenameExpr() {
			return getRuleContext(ExistRenameExprContext.class,0);
		}
		public ExistUpdateExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existUpdateExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExistUpdateExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExistUpdateExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExistUpdateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistUpdateExprContext existUpdateExpr() throws RecognitionException {
		ExistUpdateExprContext _localctx = new ExistUpdateExprContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_existUpdateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1176);
			match(KW_UPDATE);
			setState(1182);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_REPLACE:
				{
				setState(1177);
				existReplaceExpr();
				}
				break;
			case KW_VALUE:
				{
				setState(1178);
				existValueExpr();
				}
				break;
			case KW_INSERT:
				{
				setState(1179);
				existInsertExpr();
				}
				break;
			case KW_DELETE:
				{
				setState(1180);
				existDeleteExpr();
				}
				break;
			case KW_RENAME:
				{
				setState(1181);
				existRenameExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExistReplaceExprContext extends ParserRuleContext {
		public TerminalNode KW_REPLACE() { return getToken(XQueryParserScripting.KW_REPLACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode KW_WITH() { return getToken(XQueryParserScripting.KW_WITH, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ExistReplaceExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existReplaceExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExistReplaceExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExistReplaceExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExistReplaceExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistReplaceExprContext existReplaceExpr() throws RecognitionException {
		ExistReplaceExprContext _localctx = new ExistReplaceExprContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_existReplaceExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1184);
			match(KW_REPLACE);
			setState(1185);
			expr();
			setState(1186);
			match(KW_WITH);
			setState(1187);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExistValueExprContext extends ParserRuleContext {
		public TerminalNode KW_VALUE() { return getToken(XQueryParserScripting.KW_VALUE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode KW_WITH() { return getToken(XQueryParserScripting.KW_WITH, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ExistValueExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existValueExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExistValueExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExistValueExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExistValueExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistValueExprContext existValueExpr() throws RecognitionException {
		ExistValueExprContext _localctx = new ExistValueExprContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_existValueExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1189);
			match(KW_VALUE);
			setState(1190);
			expr();
			setState(1191);
			match(KW_WITH);
			setState(1192);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExistInsertExprContext extends ParserRuleContext {
		public TerminalNode KW_INSERT() { return getToken(XQueryParserScripting.KW_INSERT, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public TerminalNode KW_INTO() { return getToken(XQueryParserScripting.KW_INTO, 0); }
		public TerminalNode KW_PRECEDING() { return getToken(XQueryParserScripting.KW_PRECEDING, 0); }
		public TerminalNode KW_FOLLOWING() { return getToken(XQueryParserScripting.KW_FOLLOWING, 0); }
		public ExistInsertExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existInsertExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExistInsertExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExistInsertExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExistInsertExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistInsertExprContext existInsertExpr() throws RecognitionException {
		ExistInsertExprContext _localctx = new ExistInsertExprContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_existInsertExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1194);
			match(KW_INSERT);
			setState(1195);
			exprSingle();
			setState(1196);
			_la = _input.LA(1);
			if ( !(_la==KW_FOLLOWING || _la==KW_PRECEDING || _la==KW_INTO) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1197);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExistDeleteExprContext extends ParserRuleContext {
		public TerminalNode KW_DELETE() { return getToken(XQueryParserScripting.KW_DELETE, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ExistDeleteExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existDeleteExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExistDeleteExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExistDeleteExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExistDeleteExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistDeleteExprContext existDeleteExpr() throws RecognitionException {
		ExistDeleteExprContext _localctx = new ExistDeleteExprContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_existDeleteExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			match(KW_DELETE);
			setState(1200);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExistRenameExprContext extends ParserRuleContext {
		public TerminalNode KW_RENAME() { return getToken(XQueryParserScripting.KW_RENAME, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public ExistRenameExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existRenameExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExistRenameExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExistRenameExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExistRenameExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistRenameExprContext existRenameExpr() throws RecognitionException {
		ExistRenameExprContext _localctx = new ExistRenameExprContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_existRenameExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1202);
			match(KW_RENAME);
			setState(1203);
			exprSingle();
			setState(1204);
			match(KW_AS);
			setState(1205);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrExprContext extends ParserRuleContext {
		public AndExprContext main_expr;
		public AndExprContext andExpr;
		public List<AndExprContext> rhs = new ArrayList<AndExprContext>();
		public List<AndExprContext> andExpr() {
			return getRuleContexts(AndExprContext.class);
		}
		public AndExprContext andExpr(int i) {
			return getRuleContext(AndExprContext.class,i);
		}
		public List<TerminalNode> KW_OR() { return getTokens(XQueryParserScripting.KW_OR); }
		public TerminalNode KW_OR(int i) {
			return getToken(XQueryParserScripting.KW_OR, i);
		}
		public OrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrExprContext orExpr() throws RecognitionException {
		OrExprContext _localctx = new OrExprContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1207);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(1212);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,81,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1208);
					match(KW_OR);
					setState(1209);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(1214);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,81,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AndExprContext extends ParserRuleContext {
		public ComparisonExprContext main_expr;
		public ComparisonExprContext comparisonExpr;
		public List<ComparisonExprContext> rhs = new ArrayList<ComparisonExprContext>();
		public List<ComparisonExprContext> comparisonExpr() {
			return getRuleContexts(ComparisonExprContext.class);
		}
		public ComparisonExprContext comparisonExpr(int i) {
			return getRuleContext(ComparisonExprContext.class,i);
		}
		public List<TerminalNode> KW_AND() { return getTokens(XQueryParserScripting.KW_AND); }
		public TerminalNode KW_AND(int i) {
			return getToken(XQueryParserScripting.KW_AND, i);
		}
		public AndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1215);
			((AndExprContext)_localctx).main_expr = comparisonExpr();
			setState(1220);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1216);
					match(KW_AND);
					setState(1217);
					((AndExprContext)_localctx).comparisonExpr = comparisonExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).comparisonExpr);
					}
					} 
				}
				setState(1222);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonExprContext extends ParserRuleContext {
		public StringConcatExprContext main_expr;
		public StringConcatExprContext stringConcatExpr;
		public List<StringConcatExprContext> rhs = new ArrayList<StringConcatExprContext>();
		public List<StringConcatExprContext> stringConcatExpr() {
			return getRuleContexts(StringConcatExprContext.class);
		}
		public StringConcatExprContext stringConcatExpr(int i) {
			return getRuleContext(StringConcatExprContext.class,i);
		}
		public ValueCompContext valueComp() {
			return getRuleContext(ValueCompContext.class,0);
		}
		public GeneralCompContext generalComp() {
			return getRuleContext(GeneralCompContext.class,0);
		}
		public NodeCompContext nodeComp() {
			return getRuleContext(NodeCompContext.class,0);
		}
		public ComparisonExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterComparisonExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitComparisonExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitComparisonExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonExprContext comparisonExpr() throws RecognitionException {
		ComparisonExprContext _localctx = new ComparisonExprContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_comparisonExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1223);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(1231);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(1227);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
				case 1:
					{
					setState(1224);
					valueComp();
					}
					break;
				case 2:
					{
					setState(1225);
					generalComp();
					}
					break;
				case 3:
					{
					setState(1226);
					nodeComp();
					}
					break;
				}
				setState(1229);
				((ComparisonExprContext)_localctx).stringConcatExpr = stringConcatExpr();
				((ComparisonExprContext)_localctx).rhs.add(((ComparisonExprContext)_localctx).stringConcatExpr);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringConcatExprContext extends ParserRuleContext {
		public RangeExprContext main_expr;
		public RangeExprContext rangeExpr;
		public List<RangeExprContext> rhs = new ArrayList<RangeExprContext>();
		public List<RangeExprContext> rangeExpr() {
			return getRuleContexts(RangeExprContext.class);
		}
		public RangeExprContext rangeExpr(int i) {
			return getRuleContext(RangeExprContext.class,i);
		}
		public List<TerminalNode> CONCATENATION() { return getTokens(XQueryParserScripting.CONCATENATION); }
		public TerminalNode CONCATENATION(int i) {
			return getToken(XQueryParserScripting.CONCATENATION, i);
		}
		public StringConcatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConcatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringConcatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringConcatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringConcatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConcatExprContext stringConcatExpr() throws RecognitionException {
		StringConcatExprContext _localctx = new StringConcatExprContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1233);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(1238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CONCATENATION) {
				{
				{
				setState(1234);
				match(CONCATENATION);
				setState(1235);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(1240);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RangeExprContext extends ParserRuleContext {
		public AdditiveExprContext main_expr;
		public AdditiveExprContext additiveExpr;
		public List<AdditiveExprContext> rhs = new ArrayList<AdditiveExprContext>();
		public List<AdditiveExprContext> additiveExpr() {
			return getRuleContexts(AdditiveExprContext.class);
		}
		public AdditiveExprContext additiveExpr(int i) {
			return getRuleContext(AdditiveExprContext.class,i);
		}
		public TerminalNode KW_TO() { return getToken(XQueryParserScripting.KW_TO, 0); }
		public RangeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterRangeExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitRangeExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitRangeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeExprContext rangeExpr() throws RecognitionException {
		RangeExprContext _localctx = new RangeExprContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1241);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(1244);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				setState(1242);
				match(KW_TO);
				setState(1243);
				((RangeExprContext)_localctx).additiveExpr = additiveExpr();
				((RangeExprContext)_localctx).rhs.add(((RangeExprContext)_localctx).additiveExpr);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AdditiveExprContext extends ParserRuleContext {
		public MultiplicativeExprContext main_expr;
		public Token PLUS;
		public List<Token> op = new ArrayList<Token>();
		public Token MINUS;
		public Token _tset1859;
		public MultiplicativeExprContext multiplicativeExpr;
		public List<MultiplicativeExprContext> rhs = new ArrayList<MultiplicativeExprContext>();
		public List<MultiplicativeExprContext> multiplicativeExpr() {
			return getRuleContexts(MultiplicativeExprContext.class);
		}
		public MultiplicativeExprContext multiplicativeExpr(int i) {
			return getRuleContext(MultiplicativeExprContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(XQueryParserScripting.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(XQueryParserScripting.PLUS, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(XQueryParserScripting.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(XQueryParserScripting.MINUS, i);
		}
		public AdditiveExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAdditiveExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAdditiveExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAdditiveExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExprContext additiveExpr() throws RecognitionException {
		AdditiveExprContext _localctx = new AdditiveExprContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1246);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(1251);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,87,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1247);
					((AdditiveExprContext)_localctx)._tset1859 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==PLUS || _la==MINUS) ) {
						((AdditiveExprContext)_localctx)._tset1859 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1859);
					setState(1248);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(1253);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,87,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicativeExprContext extends ParserRuleContext {
		public UnionExprContext main_expr;
		public Token STAR;
		public List<Token> op = new ArrayList<Token>();
		public Token KW_DIV;
		public Token KW_IDIV;
		public Token KW_MOD;
		public Token _tset1887;
		public UnionExprContext unionExpr;
		public List<UnionExprContext> rhs = new ArrayList<UnionExprContext>();
		public List<UnionExprContext> unionExpr() {
			return getRuleContexts(UnionExprContext.class);
		}
		public UnionExprContext unionExpr(int i) {
			return getRuleContext(UnionExprContext.class,i);
		}
		public List<TerminalNode> STAR() { return getTokens(XQueryParserScripting.STAR); }
		public TerminalNode STAR(int i) {
			return getToken(XQueryParserScripting.STAR, i);
		}
		public List<TerminalNode> KW_DIV() { return getTokens(XQueryParserScripting.KW_DIV); }
		public TerminalNode KW_DIV(int i) {
			return getToken(XQueryParserScripting.KW_DIV, i);
		}
		public List<TerminalNode> KW_IDIV() { return getTokens(XQueryParserScripting.KW_IDIV); }
		public TerminalNode KW_IDIV(int i) {
			return getToken(XQueryParserScripting.KW_IDIV, i);
		}
		public List<TerminalNode> KW_MOD() { return getTokens(XQueryParserScripting.KW_MOD); }
		public TerminalNode KW_MOD(int i) {
			return getToken(XQueryParserScripting.KW_MOD, i);
		}
		public MultiplicativeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMultiplicativeExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMultiplicativeExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMultiplicativeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExprContext multiplicativeExpr() throws RecognitionException {
		MultiplicativeExprContext _localctx = new MultiplicativeExprContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_multiplicativeExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1254);
			((MultiplicativeExprContext)_localctx).main_expr = unionExpr();
			setState(1259);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,88,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1255);
					((MultiplicativeExprContext)_localctx)._tset1887 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==STAR || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & 68721573889L) != 0)) ) {
						((MultiplicativeExprContext)_localctx)._tset1887 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1887);
					setState(1256);
					((MultiplicativeExprContext)_localctx).unionExpr = unionExpr();
					((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).unionExpr);
					}
					} 
				}
				setState(1261);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,88,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnionExprContext extends ParserRuleContext {
		public IntersectExceptExprContext main_expr;
		public IntersectExceptExprContext intersectExceptExpr;
		public List<IntersectExceptExprContext> rhs = new ArrayList<IntersectExceptExprContext>();
		public List<IntersectExceptExprContext> intersectExceptExpr() {
			return getRuleContexts(IntersectExceptExprContext.class);
		}
		public IntersectExceptExprContext intersectExceptExpr(int i) {
			return getRuleContext(IntersectExceptExprContext.class,i);
		}
		public List<TerminalNode> KW_UNION() { return getTokens(XQueryParserScripting.KW_UNION); }
		public TerminalNode KW_UNION(int i) {
			return getToken(XQueryParserScripting.KW_UNION, i);
		}
		public List<TerminalNode> VBAR() { return getTokens(XQueryParserScripting.VBAR); }
		public TerminalNode VBAR(int i) {
			return getToken(XQueryParserScripting.VBAR, i);
		}
		public UnionExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterUnionExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitUnionExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitUnionExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionExprContext unionExpr() throws RecognitionException {
		UnionExprContext _localctx = new UnionExprContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_unionExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1262);
			((UnionExprContext)_localctx).main_expr = intersectExceptExpr();
			setState(1267);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,89,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1263);
					_la = _input.LA(1);
					if ( !(_la==VBAR || _la==KW_UNION) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1264);
					((UnionExprContext)_localctx).intersectExceptExpr = intersectExceptExpr();
					((UnionExprContext)_localctx).rhs.add(((UnionExprContext)_localctx).intersectExceptExpr);
					}
					} 
				}
				setState(1269);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,89,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntersectExceptExprContext extends ParserRuleContext {
		public InstanceOfExprContext main_expr;
		public InstanceOfExprContext instanceOfExpr;
		public List<InstanceOfExprContext> rhs = new ArrayList<InstanceOfExprContext>();
		public List<InstanceOfExprContext> instanceOfExpr() {
			return getRuleContexts(InstanceOfExprContext.class);
		}
		public InstanceOfExprContext instanceOfExpr(int i) {
			return getRuleContext(InstanceOfExprContext.class,i);
		}
		public List<TerminalNode> KW_INTERSECT() { return getTokens(XQueryParserScripting.KW_INTERSECT); }
		public TerminalNode KW_INTERSECT(int i) {
			return getToken(XQueryParserScripting.KW_INTERSECT, i);
		}
		public List<TerminalNode> KW_EXCEPT() { return getTokens(XQueryParserScripting.KW_EXCEPT); }
		public TerminalNode KW_EXCEPT(int i) {
			return getToken(XQueryParserScripting.KW_EXCEPT, i);
		}
		public IntersectExceptExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intersectExceptExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterIntersectExceptExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitIntersectExceptExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitIntersectExceptExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntersectExceptExprContext intersectExceptExpr() throws RecognitionException {
		IntersectExceptExprContext _localctx = new IntersectExceptExprContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_intersectExceptExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1270);
			((IntersectExceptExprContext)_localctx).main_expr = instanceOfExpr();
			setState(1275);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1271);
					_la = _input.LA(1);
					if ( !(_la==KW_EXCEPT || _la==KW_INTERSECT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1272);
					((IntersectExceptExprContext)_localctx).instanceOfExpr = instanceOfExpr();
					((IntersectExceptExprContext)_localctx).rhs.add(((IntersectExceptExprContext)_localctx).instanceOfExpr);
					}
					} 
				}
				setState(1277);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstanceOfExprContext extends ParserRuleContext {
		public TreatExprContext main_expr;
		public SequenceTypeContext seq;
		public TreatExprContext treatExpr() {
			return getRuleContext(TreatExprContext.class,0);
		}
		public TerminalNode KW_INSTANCE() { return getToken(XQueryParserScripting.KW_INSTANCE, 0); }
		public TerminalNode KW_OF() { return getToken(XQueryParserScripting.KW_OF, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public InstanceOfExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instanceOfExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterInstanceOfExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitInstanceOfExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitInstanceOfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstanceOfExprContext instanceOfExpr() throws RecognitionException {
		InstanceOfExprContext _localctx = new InstanceOfExprContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1278);
			((InstanceOfExprContext)_localctx).main_expr = treatExpr();
			setState(1282);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				{
				setState(1279);
				match(KW_INSTANCE);
				setState(1280);
				match(KW_OF);
				setState(1281);
				((InstanceOfExprContext)_localctx).seq = sequenceType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TreatExprContext extends ParserRuleContext {
		public CastableExprContext main_expr;
		public SequenceTypeContext seq;
		public CastableExprContext castableExpr() {
			return getRuleContext(CastableExprContext.class,0);
		}
		public TerminalNode KW_TREAT() { return getToken(XQueryParserScripting.KW_TREAT, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TreatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_treatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTreatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTreatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTreatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TreatExprContext treatExpr() throws RecognitionException {
		TreatExprContext _localctx = new TreatExprContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1284);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(1288);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				{
				setState(1285);
				match(KW_TREAT);
				setState(1286);
				match(KW_AS);
				setState(1287);
				((TreatExprContext)_localctx).seq = sequenceType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CastableExprContext extends ParserRuleContext {
		public CastExprContext main_expr;
		public SingleTypeContext single;
		public CastExprContext castExpr() {
			return getRuleContext(CastExprContext.class,0);
		}
		public TerminalNode KW_CASTABLE() { return getToken(XQueryParserScripting.KW_CASTABLE, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public SingleTypeContext singleType() {
			return getRuleContext(SingleTypeContext.class,0);
		}
		public CastableExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castableExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCastableExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCastableExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCastableExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastableExprContext castableExpr() throws RecognitionException {
		CastableExprContext _localctx = new CastableExprContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1290);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(1294);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				{
				setState(1291);
				match(KW_CASTABLE);
				setState(1292);
				match(KW_AS);
				setState(1293);
				((CastableExprContext)_localctx).single = singleType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CastExprContext extends ParserRuleContext {
		public ArrowExprContext main_expr;
		public SingleTypeContext single;
		public ArrowExprContext arrowExpr() {
			return getRuleContext(ArrowExprContext.class,0);
		}
		public TerminalNode KW_CAST() { return getToken(XQueryParserScripting.KW_CAST, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public SingleTypeContext singleType() {
			return getRuleContext(SingleTypeContext.class,0);
		}
		public CastExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCastExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCastExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCastExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastExprContext castExpr() throws RecognitionException {
		CastExprContext _localctx = new CastExprContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1296);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(1300);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				{
				setState(1297);
				match(KW_CAST);
				setState(1298);
				match(KW_AS);
				setState(1299);
				((CastExprContext)_localctx).single = singleType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrowExprContext extends ParserRuleContext {
		public UnaryExprContext main_expr;
		public ComplexArrowContext complexArrow;
		public List<ComplexArrowContext> function_call_expr = new ArrayList<ComplexArrowContext>();
		public UnaryExprContext unaryExpr() {
			return getRuleContext(UnaryExprContext.class,0);
		}
		public List<TerminalNode> ARROW() { return getTokens(XQueryParserScripting.ARROW); }
		public TerminalNode ARROW(int i) {
			return getToken(XQueryParserScripting.ARROW, i);
		}
		public List<ComplexArrowContext> complexArrow() {
			return getRuleContexts(ComplexArrowContext.class);
		}
		public ComplexArrowContext complexArrow(int i) {
			return getRuleContext(ComplexArrowContext.class,i);
		}
		public ArrowExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrowExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterArrowExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitArrowExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitArrowExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowExprContext arrowExpr() throws RecognitionException {
		ArrowExprContext _localctx = new ArrowExprContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1302);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(1307);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,95,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1303);
					match(ARROW);
					setState(1304);
					((ArrowExprContext)_localctx).complexArrow = complexArrow();
					((ArrowExprContext)_localctx).function_call_expr.add(((ArrowExprContext)_localctx).complexArrow);
					}
					} 
				}
				setState(1309);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,95,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComplexArrowContext extends ParserRuleContext {
		public ArrowFunctionSpecifierContext arrowFunctionSpecifier() {
			return getRuleContext(ArrowFunctionSpecifierContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ComplexArrowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexArrow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterComplexArrow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitComplexArrow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitComplexArrow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplexArrowContext complexArrow() throws RecognitionException {
		ComplexArrowContext _localctx = new ComplexArrowContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_complexArrow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1310);
			arrowFunctionSpecifier();
			setState(1311);
			argumentList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExprContext extends ParserRuleContext {
		public Token MINUS;
		public List<Token> op = new ArrayList<Token>();
		public Token PLUS;
		public Token _tset2077;
		public ValueExprContext main_expr;
		public ValueExprContext valueExpr() {
			return getRuleContext(ValueExprContext.class,0);
		}
		public List<TerminalNode> MINUS() { return getTokens(XQueryParserScripting.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(XQueryParserScripting.MINUS, i);
		}
		public List<TerminalNode> PLUS() { return getTokens(XQueryParserScripting.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(XQueryParserScripting.PLUS, i);
		}
		public UnaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterUnaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitUnaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExprContext unaryExpr() throws RecognitionException {
		UnaryExprContext _localctx = new UnaryExprContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1316);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PLUS || _la==MINUS) {
				{
				{
				setState(1313);
				((UnaryExprContext)_localctx)._tset2077 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
					((UnaryExprContext)_localctx)._tset2077 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset2077);
				}
				}
				setState(1318);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1319);
			((UnaryExprContext)_localctx).main_expr = valueExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueExprContext extends ParserRuleContext {
		public ValidateExprContext validateExpr() {
			return getRuleContext(ValidateExprContext.class,0);
		}
		public ExtensionExprContext extensionExpr() {
			return getRuleContext(ExtensionExprContext.class,0);
		}
		public SimpleMapExprContext simpleMapExpr() {
			return getRuleContext(SimpleMapExprContext.class,0);
		}
		public ValueExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterValueExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitValueExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitValueExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueExprContext valueExpr() throws RecognitionException {
		ValueExprContext _localctx = new ValueExprContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_valueExpr);
		try {
			setState(1324);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1321);
				validateExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1322);
				extensionExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1323);
				simpleMapExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GeneralCompContext extends ParserRuleContext {
		public TerminalNode EQUAL() { return getToken(XQueryParserScripting.EQUAL, 0); }
		public TerminalNode NOT_EQUAL() { return getToken(XQueryParserScripting.NOT_EQUAL, 0); }
		public TerminalNode LANGLE() { return getToken(XQueryParserScripting.LANGLE, 0); }
		public TerminalNode RANGLE() { return getToken(XQueryParserScripting.RANGLE, 0); }
		public GeneralCompContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_generalComp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterGeneralComp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitGeneralComp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitGeneralComp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GeneralCompContext generalComp() throws RecognitionException {
		GeneralCompContext _localctx = new GeneralCompContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_generalComp);
		try {
			setState(1334);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1326);
				match(EQUAL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1327);
				match(NOT_EQUAL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1328);
				match(LANGLE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				{
				setState(1329);
				match(LANGLE);
				setState(1330);
				match(EQUAL);
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1331);
				match(RANGLE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				{
				setState(1332);
				match(RANGLE);
				setState(1333);
				match(EQUAL);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueCompContext extends ParserRuleContext {
		public TerminalNode KW_EQ() { return getToken(XQueryParserScripting.KW_EQ, 0); }
		public TerminalNode KW_NE() { return getToken(XQueryParserScripting.KW_NE, 0); }
		public TerminalNode KW_LT() { return getToken(XQueryParserScripting.KW_LT, 0); }
		public TerminalNode KW_LE() { return getToken(XQueryParserScripting.KW_LE, 0); }
		public TerminalNode KW_GT() { return getToken(XQueryParserScripting.KW_GT, 0); }
		public TerminalNode KW_GE() { return getToken(XQueryParserScripting.KW_GE, 0); }
		public ValueCompContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueComp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterValueComp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitValueComp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitValueComp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueCompContext valueComp() throws RecognitionException {
		ValueCompContext _localctx = new ValueCompContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_valueComp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1336);
			_la = _input.LA(1);
			if ( !(((((_la - 92)) & ~0x3f) == 0 && ((1L << (_la - 92)) & 1111492865L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NodeCompContext extends ParserRuleContext {
		public TerminalNode KW_IS() { return getToken(XQueryParserScripting.KW_IS, 0); }
		public List<TerminalNode> LANGLE() { return getTokens(XQueryParserScripting.LANGLE); }
		public TerminalNode LANGLE(int i) {
			return getToken(XQueryParserScripting.LANGLE, i);
		}
		public List<TerminalNode> RANGLE() { return getTokens(XQueryParserScripting.RANGLE); }
		public TerminalNode RANGLE(int i) {
			return getToken(XQueryParserScripting.RANGLE, i);
		}
		public NodeCompContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeComp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNodeComp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNodeComp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNodeComp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeCompContext nodeComp() throws RecognitionException {
		NodeCompContext _localctx = new NodeCompContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_nodeComp);
		try {
			setState(1343);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_IS:
				enterOuterAlt(_localctx, 1);
				{
				setState(1338);
				match(KW_IS);
				}
				break;
			case LANGLE:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1339);
				match(LANGLE);
				setState(1340);
				match(LANGLE);
				}
				}
				break;
			case RANGLE:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(1341);
				match(RANGLE);
				setState(1342);
				match(RANGLE);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValidateExprContext extends ParserRuleContext {
		public TerminalNode KW_VALIDATE() { return getToken(XQueryParserScripting.KW_VALIDATE, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public ValidationModeContext validationMode() {
			return getRuleContext(ValidationModeContext.class,0);
		}
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode KW_TYPE() { return getToken(XQueryParserScripting.KW_TYPE, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public ValidateExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validateExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterValidateExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitValidateExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitValidateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidateExprContext validateExpr() throws RecognitionException {
		ValidateExprContext _localctx = new ValidateExprContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_validateExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1345);
			match(KW_VALIDATE);
			setState(1349);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_LAX:
			case KW_STRICT:
				{
				setState(1346);
				validationMode();
				}
				break;
			case KW_AS:
			case KW_TYPE:
				{
				{
				setState(1347);
				_la = _input.LA(1);
				if ( !(_la==KW_AS || _la==KW_TYPE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1348);
				typeName();
				}
				}
				break;
			case LBRACE:
				break;
			default:
				break;
			}
			setState(1351);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValidationModeContext extends ParserRuleContext {
		public TerminalNode KW_LAX() { return getToken(XQueryParserScripting.KW_LAX, 0); }
		public TerminalNode KW_STRICT() { return getToken(XQueryParserScripting.KW_STRICT, 0); }
		public ValidationModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validationMode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterValidationMode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitValidationMode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitValidationMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidationModeContext validationMode() throws RecognitionException {
		ValidationModeContext _localctx = new ValidationModeContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_validationMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1353);
			_la = _input.LA(1);
			if ( !(_la==KW_LAX || _la==KW_STRICT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExtensionExprContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public List<TerminalNode> PRAGMA() { return getTokens(XQueryParserScripting.PRAGMA); }
		public TerminalNode PRAGMA(int i) {
			return getToken(XQueryParserScripting.PRAGMA, i);
		}
		public ExtensionExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extensionExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExtensionExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExtensionExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExtensionExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtensionExprContext extensionExpr() throws RecognitionException {
		ExtensionExprContext _localctx = new ExtensionExprContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_extensionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1356); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1355);
				match(PRAGMA);
				}
				}
				setState(1358); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==PRAGMA );
			setState(1360);
			match(LBRACE);
			setState(1361);
			expr();
			setState(1362);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleMapExprContext extends ParserRuleContext {
		public PathExprContext main_expr;
		public PathExprContext pathExpr;
		public List<PathExprContext> map_expr = new ArrayList<PathExprContext>();
		public List<PathExprContext> pathExpr() {
			return getRuleContexts(PathExprContext.class);
		}
		public PathExprContext pathExpr(int i) {
			return getRuleContext(PathExprContext.class,i);
		}
		public List<TerminalNode> BANG() { return getTokens(XQueryParserScripting.BANG); }
		public TerminalNode BANG(int i) {
			return getToken(XQueryParserScripting.BANG, i);
		}
		public SimpleMapExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleMapExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSimpleMapExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSimpleMapExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSimpleMapExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleMapExprContext simpleMapExpr() throws RecognitionException {
		SimpleMapExprContext _localctx = new SimpleMapExprContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_simpleMapExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1364);
			((SimpleMapExprContext)_localctx).main_expr = pathExpr();
			setState(1369);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1365);
					match(BANG);
					setState(1366);
					((SimpleMapExprContext)_localctx).pathExpr = pathExpr();
					((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).pathExpr);
					}
					} 
				}
				setState(1371);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public ApplyStatementContext applyStatement() {
			return getRuleContext(ApplyStatementContext.class,0);
		}
		public AssignStatementContext assignStatement() {
			return getRuleContext(AssignStatementContext.class,0);
		}
		public BlockStatementContext blockStatement() {
			return getRuleContext(BlockStatementContext.class,0);
		}
		public BreakStatementContext breakStatement() {
			return getRuleContext(BreakStatementContext.class,0);
		}
		public ContinueStatementContext continueStatement() {
			return getRuleContext(ContinueStatementContext.class,0);
		}
		public ExitStatementContext exitStatement() {
			return getRuleContext(ExitStatementContext.class,0);
		}
		public FlworStatementContext flworStatement() {
			return getRuleContext(FlworStatementContext.class,0);
		}
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public SwitchStatementContext switchStatement() {
			return getRuleContext(SwitchStatementContext.class,0);
		}
		public TryCatchStatementContext tryCatchStatement() {
			return getRuleContext(TryCatchStatementContext.class,0);
		}
		public TypeswitchStatementContext typeswitchStatement() {
			return getRuleContext(TypeswitchStatementContext.class,0);
		}
		public VarDeclStatementContext varDeclStatement() {
			return getRuleContext(VarDeclStatementContext.class,0);
		}
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_statement);
		try {
			setState(1385);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1372);
				applyStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1373);
				assignStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1374);
				blockStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1375);
				breakStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1376);
				continueStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1377);
				exitStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1378);
				flworStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1379);
				ifStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1380);
				switchStatement();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1381);
				tryCatchStatement();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1382);
				typeswitchStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1383);
				varDeclStatement();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1384);
				whileStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ApplyStatementContext extends ParserRuleContext {
		public ExprSimpleContext exprSimple() {
			return getRuleContext(ExprSimpleContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParserScripting.SEMICOLON, 0); }
		public ApplyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_applyStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterApplyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitApplyStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitApplyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ApplyStatementContext applyStatement() throws RecognitionException {
		ApplyStatementContext _localctx = new ApplyStatementContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_applyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1387);
			exprSimple();
			setState(1388);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignStatementContext extends ParserRuleContext {
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParserScripting.COLON_EQ, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParserScripting.SEMICOLON, 0); }
		public AssignStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAssignStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAssignStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAssignStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignStatementContext assignStatement() throws RecognitionException {
		AssignStatementContext _localctx = new AssignStatementContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_assignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1390);
			match(DOLLAR);
			setState(1391);
			varName();
			setState(1392);
			match(COLON_EQ);
			setState(1393);
			exprSingle();
			setState(1394);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitBlockStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_blockStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1396);
			match(LBRACE);
			setState(1397);
			statements();
			setState(1398);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakStatementContext extends ParserRuleContext {
		public TerminalNode KW_BREAK() { return getToken(XQueryParserScripting.KW_BREAK, 0); }
		public TerminalNode KW_LOOP() { return getToken(XQueryParserScripting.KW_LOOP, 0); }
		public TerminalNode SEMICOLON() { return getToken(XQueryParserScripting.SEMICOLON, 0); }
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterBreakStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitBreakStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1400);
			match(KW_BREAK);
			setState(1401);
			match(KW_LOOP);
			setState(1402);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends ParserRuleContext {
		public TerminalNode KW_CONTINUE() { return getToken(XQueryParserScripting.KW_CONTINUE, 0); }
		public TerminalNode KW_LOOP() { return getToken(XQueryParserScripting.KW_LOOP, 0); }
		public TerminalNode SEMICOLON() { return getToken(XQueryParserScripting.SEMICOLON, 0); }
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterContinueStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitContinueStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1404);
			match(KW_CONTINUE);
			setState(1405);
			match(KW_LOOP);
			setState(1406);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExitStatementContext extends ParserRuleContext {
		public TerminalNode KW_EXIT() { return getToken(XQueryParserScripting.KW_EXIT, 0); }
		public TerminalNode KW_RETURNING() { return getToken(XQueryParserScripting.KW_RETURNING, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParserScripting.SEMICOLON, 0); }
		public ExitStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exitStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterExitStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitExitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitExitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExitStatementContext exitStatement() throws RecognitionException {
		ExitStatementContext _localctx = new ExitStatementContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_exitStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1408);
			match(KW_EXIT);
			setState(1409);
			match(KW_RETURNING);
			setState(1410);
			exprSingle();
			setState(1411);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlworStatementContext extends ParserRuleContext {
		public InitialClauseContext initialClause() {
			return getRuleContext(InitialClauseContext.class,0);
		}
		public ReturnStatementContext returnStatement() {
			return getRuleContext(ReturnStatementContext.class,0);
		}
		public List<IntermediateClauseContext> intermediateClause() {
			return getRuleContexts(IntermediateClauseContext.class);
		}
		public IntermediateClauseContext intermediateClause(int i) {
			return getRuleContext(IntermediateClauseContext.class,i);
		}
		public FlworStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flworStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFlworStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFlworStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFlworStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlworStatementContext flworStatement() throws RecognitionException {
		FlworStatementContext _localctx = new FlworStatementContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_flworStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1413);
			initialClause();
			setState(1417);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 72058693620858881L) != 0) || _la==KW_STABLE || _la==KW_WHERE) {
				{
				{
				setState(1414);
				intermediateClause();
				}
				}
				setState(1419);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1420);
			returnStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStatementContext extends ParserRuleContext {
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ReturnStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStatementContext returnStatement() throws RecognitionException {
		ReturnStatementContext _localctx = new ReturnStatementContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_returnStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1422);
			match(KW_RETURN);
			setState(1423);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementContext extends ParserRuleContext {
		public TerminalNode KW_IF() { return getToken(XQueryParserScripting.KW_IF, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TerminalNode KW_THEN() { return getToken(XQueryParserScripting.KW_THEN, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode KW_ELSE() { return getToken(XQueryParserScripting.KW_ELSE, 0); }
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_ifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1425);
			match(KW_IF);
			setState(1426);
			match(LPAREN);
			setState(1427);
			expr();
			setState(1428);
			match(RPAREN);
			setState(1429);
			match(KW_THEN);
			setState(1430);
			statement();
			setState(1431);
			match(KW_ELSE);
			setState(1432);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchStatementContext extends ParserRuleContext {
		public TerminalNode KW_SWITCH() { return getToken(XQueryParserScripting.KW_SWITCH, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<SwitchCaseStatementContext> switchCaseStatement() {
			return getRuleContexts(SwitchCaseStatementContext.class);
		}
		public SwitchCaseStatementContext switchCaseStatement(int i) {
			return getRuleContext(SwitchCaseStatementContext.class,i);
		}
		public SwitchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSwitchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSwitchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSwitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchStatementContext switchStatement() throws RecognitionException {
		SwitchStatementContext _localctx = new SwitchStatementContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_switchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1434);
			match(KW_SWITCH);
			setState(1435);
			match(LPAREN);
			setState(1436);
			expr();
			setState(1437);
			match(RPAREN);
			setState(1439); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1438);
				switchCaseStatement();
				}
				}
				setState(1441); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1443);
			match(KW_DEFAULT);
			setState(1444);
			match(KW_RETURN);
			setState(1445);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchCaseStatementContext extends ParserRuleContext {
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<TerminalNode> KW_CASE() { return getTokens(XQueryParserScripting.KW_CASE); }
		public TerminalNode KW_CASE(int i) {
			return getToken(XQueryParserScripting.KW_CASE, i);
		}
		public List<SwitchCaseOperandContext> switchCaseOperand() {
			return getRuleContexts(SwitchCaseOperandContext.class);
		}
		public SwitchCaseOperandContext switchCaseOperand(int i) {
			return getRuleContext(SwitchCaseOperandContext.class,i);
		}
		public SwitchCaseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCaseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSwitchCaseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSwitchCaseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSwitchCaseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseStatementContext switchCaseStatement() throws RecognitionException {
		SwitchCaseStatementContext _localctx = new SwitchCaseStatementContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_switchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1449); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1447);
				match(KW_CASE);
				setState(1448);
				switchCaseOperand();
				}
				}
				setState(1451); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1453);
			match(KW_RETURN);
			setState(1454);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TryCatchStatementContext extends ParserRuleContext {
		public TerminalNode KW_TRY() { return getToken(XQueryParserScripting.KW_TRY, 0); }
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public List<TerminalNode> KW_CATCH() { return getTokens(XQueryParserScripting.KW_CATCH); }
		public TerminalNode KW_CATCH(int i) {
			return getToken(XQueryParserScripting.KW_CATCH, i);
		}
		public List<CatchErrorListContext> catchErrorList() {
			return getRuleContexts(CatchErrorListContext.class);
		}
		public CatchErrorListContext catchErrorList(int i) {
			return getRuleContext(CatchErrorListContext.class,i);
		}
		public TryCatchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryCatchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTryCatchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTryCatchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTryCatchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryCatchStatementContext tryCatchStatement() throws RecognitionException {
		TryCatchStatementContext _localctx = new TryCatchStatementContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_tryCatchStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1456);
			match(KW_TRY);
			setState(1457);
			blockStatement();
			setState(1462); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1458);
					match(KW_CATCH);
					setState(1459);
					catchErrorList();
					setState(1460);
					blockStatement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1464); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,107,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeswitchStatementContext extends ParserRuleContext {
		public TerminalNode KW_TYPESWITCH() { return getToken(XQueryParserScripting.KW_TYPESWITCH, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<CaseStatementContext> caseStatement() {
			return getRuleContexts(CaseStatementContext.class);
		}
		public CaseStatementContext caseStatement(int i) {
			return getRuleContext(CaseStatementContext.class,i);
		}
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TypeswitchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeswitchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTypeswitchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTypeswitchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTypeswitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeswitchStatementContext typeswitchStatement() throws RecognitionException {
		TypeswitchStatementContext _localctx = new TypeswitchStatementContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_typeswitchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1466);
			match(KW_TYPESWITCH);
			setState(1467);
			match(LPAREN);
			setState(1468);
			expr();
			setState(1469);
			match(RPAREN);
			setState(1471); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1470);
				caseStatement();
				}
				}
				setState(1473); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1475);
			match(KW_DEFAULT);
			setState(1478);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(1476);
				match(DOLLAR);
				setState(1477);
				varName();
				}
			}

			setState(1480);
			match(KW_RETURN);
			setState(1481);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaseStatementContext extends ParserRuleContext {
		public TerminalNode KW_CASE() { return getToken(XQueryParserScripting.KW_CASE, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public CaseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCaseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCaseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCaseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseStatementContext caseStatement() throws RecognitionException {
		CaseStatementContext _localctx = new CaseStatementContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_caseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1483);
			match(KW_CASE);
			setState(1488);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(1484);
				match(DOLLAR);
				setState(1485);
				varName();
				setState(1486);
				match(KW_AS);
				}
			}

			setState(1490);
			sequenceType();
			setState(1491);
			match(KW_RETURN);
			setState(1492);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclStatementContext extends ParserRuleContext {
		public TerminalNode KW_VARIABLE() { return getToken(XQueryParserScripting.KW_VARIABLE, 0); }
		public List<TerminalNode> DOLLAR() { return getTokens(XQueryParserScripting.DOLLAR); }
		public TerminalNode DOLLAR(int i) {
			return getToken(XQueryParserScripting.DOLLAR, i);
		}
		public List<VarNameContext> varName() {
			return getRuleContexts(VarNameContext.class);
		}
		public VarNameContext varName(int i) {
			return getRuleContext(VarNameContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParserScripting.SEMICOLON, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public List<TerminalNode> COLON_EQ() { return getTokens(XQueryParserScripting.COLON_EQ); }
		public TerminalNode COLON_EQ(int i) {
			return getToken(XQueryParserScripting.COLON_EQ, i);
		}
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public VarDeclStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterVarDeclStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitVarDeclStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitVarDeclStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclStatementContext varDeclStatement() throws RecognitionException {
		VarDeclStatementContext _localctx = new VarDeclStatementContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_varDeclStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==MOD) {
				{
				{
				setState(1494);
				annotation();
				}
				}
				setState(1499);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1500);
			match(KW_VARIABLE);
			setState(1501);
			match(DOLLAR);
			setState(1502);
			varName();
			setState(1504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(1503);
				typeDeclaration();
				}
			}

			setState(1508);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON_EQ) {
				{
				setState(1506);
				match(COLON_EQ);
				setState(1507);
				exprSingle();
				}
			}

			setState(1522);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1510);
				match(COMMA);
				setState(1511);
				match(DOLLAR);
				setState(1512);
				varName();
				setState(1514);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KW_AS) {
					{
					setState(1513);
					typeDeclaration();
					}
				}

				setState(1518);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON_EQ) {
					{
					setState(1516);
					match(COLON_EQ);
					setState(1517);
					exprSingle();
					}
				}

				}
				}
				setState(1524);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1525);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
		public TerminalNode KW_WHILE() { return getToken(XQueryParserScripting.KW_WHILE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterWhileStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitWhileStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1527);
			match(KW_WHILE);
			setState(1528);
			match(LPAREN);
			setState(1529);
			expr();
			setState(1530);
			match(RPAREN);
			setState(1531);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PathExprContext extends ParserRuleContext {
		public RelativePathExprContext singleslash;
		public RelativePathExprContext doubleslash;
		public RelativePathExprContext relative;
		public TerminalNode SLASH() { return getToken(XQueryParserScripting.SLASH, 0); }
		public RelativePathExprContext relativePathExpr() {
			return getRuleContext(RelativePathExprContext.class,0);
		}
		public TerminalNode DSLASH() { return getToken(XQueryParserScripting.DSLASH, 0); }
		public PathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPathExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPathExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPathExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathExprContext pathExpr() throws RecognitionException {
		PathExprContext _localctx = new PathExprContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_pathExpr);
		try {
			setState(1540);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SLASH:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1533);
				match(SLASH);
				setState(1535);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
				case 1:
					{
					setState(1534);
					((PathExprContext)_localctx).singleslash = relativePathExpr();
					}
					break;
				}
				}
				}
				break;
			case DSLASH:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1537);
				match(DSLASH);
				setState(1538);
				((PathExprContext)_localctx).doubleslash = relativePathExpr();
				}
				}
				break;
			case IntegerLiteral:
			case DecimalLiteral:
			case DoubleLiteral:
			case DFPropertyName:
			case Quot:
			case Apos:
			case COMMENT:
			case PI:
			case LPAREN:
			case LBRACKET:
			case LBRACE:
			case STAR:
			case DOT:
			case DDOT:
			case LANGLE:
			case QUESTION:
			case AT:
			case DOLLAR:
			case MOD:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case URIQualifiedName:
			case FullQName:
			case NCNameWithLocalWildcard:
			case NCNameWithPrefixWildcard:
			case NCName:
			case ENTER_STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(1539);
				((PathExprContext)_localctx).relative = relativePathExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelativePathExprContext extends ParserRuleContext {
		public Token sep;
		public List<StepExprContext> stepExpr() {
			return getRuleContexts(StepExprContext.class);
		}
		public StepExprContext stepExpr(int i) {
			return getRuleContext(StepExprContext.class,i);
		}
		public List<TerminalNode> SLASH() { return getTokens(XQueryParserScripting.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(XQueryParserScripting.SLASH, i);
		}
		public List<TerminalNode> DSLASH() { return getTokens(XQueryParserScripting.DSLASH); }
		public TerminalNode DSLASH(int i) {
			return getToken(XQueryParserScripting.DSLASH, i);
		}
		public RelativePathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relativePathExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterRelativePathExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitRelativePathExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitRelativePathExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelativePathExprContext relativePathExpr() throws RecognitionException {
		RelativePathExprContext _localctx = new RelativePathExprContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_relativePathExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1542);
			stepExpr();
			setState(1547);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,119,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1543);
					((RelativePathExprContext)_localctx).sep = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==SLASH || _la==DSLASH) ) {
						((RelativePathExprContext)_localctx).sep = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1544);
					stepExpr();
					}
					} 
				}
				setState(1549);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,119,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StepExprContext extends ParserRuleContext {
		public PostfixExprContext postfixExpr() {
			return getRuleContext(PostfixExprContext.class,0);
		}
		public AxisStepContext axisStep() {
			return getRuleContext(AxisStepContext.class,0);
		}
		public StepExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stepExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStepExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStepExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStepExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StepExprContext stepExpr() throws RecognitionException {
		StepExprContext _localctx = new StepExprContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_stepExpr);
		try {
			setState(1552);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1550);
				postfixExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1551);
				axisStep();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AxisStepContext extends ParserRuleContext {
		public PredicateListContext predicateList() {
			return getRuleContext(PredicateListContext.class,0);
		}
		public ReverseStepContext reverseStep() {
			return getRuleContext(ReverseStepContext.class,0);
		}
		public ForwardStepContext forwardStep() {
			return getRuleContext(ForwardStepContext.class,0);
		}
		public AxisStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_axisStep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAxisStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAxisStep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAxisStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AxisStepContext axisStep() throws RecognitionException {
		AxisStepContext _localctx = new AxisStepContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_axisStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1556);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				{
				setState(1554);
				reverseStep();
				}
				break;
			case 2:
				{
				setState(1555);
				forwardStep();
				}
				break;
			}
			setState(1558);
			predicateList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForwardStepContext extends ParserRuleContext {
		public ForwardAxisContext forwardAxis() {
			return getRuleContext(ForwardAxisContext.class,0);
		}
		public NodeTestContext nodeTest() {
			return getRuleContext(NodeTestContext.class,0);
		}
		public AbbrevForwardStepContext abbrevForwardStep() {
			return getRuleContext(AbbrevForwardStepContext.class,0);
		}
		public ForwardStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forwardStep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterForwardStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitForwardStep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitForwardStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForwardStepContext forwardStep() throws RecognitionException {
		ForwardStepContext _localctx = new ForwardStepContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_forwardStep);
		try {
			setState(1564);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1560);
				forwardAxis();
				setState(1561);
				nodeTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1563);
				abbrevForwardStep();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForwardAxisContext extends ParserRuleContext {
		public List<TerminalNode> COLON() { return getTokens(XQueryParserScripting.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(XQueryParserScripting.COLON, i);
		}
		public TerminalNode KW_CHILD() { return getToken(XQueryParserScripting.KW_CHILD, 0); }
		public TerminalNode KW_DESCENDANT() { return getToken(XQueryParserScripting.KW_DESCENDANT, 0); }
		public TerminalNode KW_ATTRIBUTE() { return getToken(XQueryParserScripting.KW_ATTRIBUTE, 0); }
		public TerminalNode KW_SELF() { return getToken(XQueryParserScripting.KW_SELF, 0); }
		public TerminalNode KW_DESCENDANT_OR_SELF() { return getToken(XQueryParserScripting.KW_DESCENDANT_OR_SELF, 0); }
		public TerminalNode KW_FOLLOWING_SIBLING() { return getToken(XQueryParserScripting.KW_FOLLOWING_SIBLING, 0); }
		public TerminalNode KW_FOLLOWING() { return getToken(XQueryParserScripting.KW_FOLLOWING, 0); }
		public ForwardAxisContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forwardAxis; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterForwardAxis(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitForwardAxis(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitForwardAxis(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForwardAxisContext forwardAxis() throws RecognitionException {
		ForwardAxisContext _localctx = new ForwardAxisContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_forwardAxis);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1566);
			_la = _input.LA(1);
			if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & 103080002049L) != 0) || _la==KW_SELF) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1567);
			match(COLON);
			setState(1568);
			match(COLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AbbrevForwardStepContext extends ParserRuleContext {
		public NodeTestContext nodeTest() {
			return getRuleContext(NodeTestContext.class,0);
		}
		public TerminalNode AT() { return getToken(XQueryParserScripting.AT, 0); }
		public AbbrevForwardStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abbrevForwardStep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAbbrevForwardStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAbbrevForwardStep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAbbrevForwardStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbbrevForwardStepContext abbrevForwardStep() throws RecognitionException {
		AbbrevForwardStepContext _localctx = new AbbrevForwardStepContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_abbrevForwardStep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1571);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AT) {
				{
				setState(1570);
				match(AT);
				}
			}

			setState(1573);
			nodeTest();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReverseStepContext extends ParserRuleContext {
		public ReverseAxisContext reverseAxis() {
			return getRuleContext(ReverseAxisContext.class,0);
		}
		public NodeTestContext nodeTest() {
			return getRuleContext(NodeTestContext.class,0);
		}
		public AbbrevReverseStepContext abbrevReverseStep() {
			return getRuleContext(AbbrevReverseStepContext.class,0);
		}
		public ReverseStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reverseStep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterReverseStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitReverseStep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitReverseStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReverseStepContext reverseStep() throws RecognitionException {
		ReverseStepContext _localctx = new ReverseStepContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_reverseStep);
		try {
			setState(1579);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
				enterOuterAlt(_localctx, 1);
				{
				setState(1575);
				reverseAxis();
				setState(1576);
				nodeTest();
				}
				break;
			case DDOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1578);
				abbrevReverseStep();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReverseAxisContext extends ParserRuleContext {
		public List<TerminalNode> COLON() { return getTokens(XQueryParserScripting.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(XQueryParserScripting.COLON, i);
		}
		public TerminalNode KW_PARENT() { return getToken(XQueryParserScripting.KW_PARENT, 0); }
		public TerminalNode KW_ANCESTOR() { return getToken(XQueryParserScripting.KW_ANCESTOR, 0); }
		public TerminalNode KW_PRECEDING_SIBLING() { return getToken(XQueryParserScripting.KW_PRECEDING_SIBLING, 0); }
		public TerminalNode KW_PRECEDING() { return getToken(XQueryParserScripting.KW_PRECEDING, 0); }
		public TerminalNode KW_ANCESTOR_OR_SELF() { return getToken(XQueryParserScripting.KW_ANCESTOR_OR_SELF, 0); }
		public ReverseAxisContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reverseAxis; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterReverseAxis(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitReverseAxis(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitReverseAxis(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReverseAxisContext reverseAxis() throws RecognitionException {
		ReverseAxisContext _localctx = new ReverseAxisContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_reverseAxis);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1581);
			_la = _input.LA(1);
			if ( !(_la==KW_ANCESTOR || _la==KW_ANCESTOR_OR_SELF || ((((_la - 135)) & ~0x3f) == 0 && ((1L << (_la - 135)) & 7L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1582);
			match(COLON);
			setState(1583);
			match(COLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AbbrevReverseStepContext extends ParserRuleContext {
		public TerminalNode DDOT() { return getToken(XQueryParserScripting.DDOT, 0); }
		public AbbrevReverseStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abbrevReverseStep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAbbrevReverseStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAbbrevReverseStep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAbbrevReverseStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbbrevReverseStepContext abbrevReverseStep() throws RecognitionException {
		AbbrevReverseStepContext _localctx = new AbbrevReverseStepContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_abbrevReverseStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1585);
			match(DDOT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NodeTestContext extends ParserRuleContext {
		public NameTestContext nameTest() {
			return getRuleContext(NameTestContext.class,0);
		}
		public KindTestContext kindTest() {
			return getRuleContext(KindTestContext.class,0);
		}
		public NodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeTestContext nodeTest() throws RecognitionException {
		NodeTestContext _localctx = new NodeTestContext(_ctx, getState());
		enterRule(_localctx, 280, RULE_nodeTest);
		try {
			setState(1589);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1587);
				nameTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1588);
				kindTest();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NameTestContext extends ParserRuleContext {
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public WildcardContext wildcard() {
			return getRuleContext(WildcardContext.class,0);
		}
		public NameTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nameTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNameTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNameTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNameTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameTestContext nameTest() throws RecognitionException {
		NameTestContext _localctx = new NameTestContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_nameTest);
		try {
			setState(1593);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case URIQualifiedName:
			case FullQName:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1591);
				eqName();
				}
				break;
			case STAR:
			case NCNameWithLocalWildcard:
			case NCNameWithPrefixWildcard:
				enterOuterAlt(_localctx, 2);
				{
				setState(1592);
				wildcard();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WildcardContext extends ParserRuleContext {
		public WildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wildcard; }
	 
		public WildcardContext() { }
		public void copyFrom(WildcardContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AllNamesContext extends WildcardContext {
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public AllNamesContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAllNames(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAllNames(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAllNames(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AllWithLocalContext extends WildcardContext {
		public TerminalNode NCNameWithPrefixWildcard() { return getToken(XQueryParserScripting.NCNameWithPrefixWildcard, 0); }
		public AllWithLocalContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAllWithLocal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAllWithLocal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAllWithLocal(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AllWithNSContext extends WildcardContext {
		public TerminalNode NCNameWithLocalWildcard() { return getToken(XQueryParserScripting.NCNameWithLocalWildcard, 0); }
		public AllWithNSContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAllWithNS(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAllWithNS(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAllWithNS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WildcardContext wildcard() throws RecognitionException {
		WildcardContext _localctx = new WildcardContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_wildcard);
		try {
			setState(1598);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STAR:
				_localctx = new AllNamesContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1595);
				match(STAR);
				}
				break;
			case NCNameWithLocalWildcard:
				_localctx = new AllWithNSContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1596);
				match(NCNameWithLocalWildcard);
				}
				break;
			case NCNameWithPrefixWildcard:
				_localctx = new AllWithLocalContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1597);
				match(NCNameWithPrefixWildcard);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostfixExprContext extends ParserRuleContext {
		public PrimaryExprContext main_expr;
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public List<ArgumentListContext> argumentList() {
			return getRuleContexts(ArgumentListContext.class);
		}
		public ArgumentListContext argumentList(int i) {
			return getRuleContext(ArgumentListContext.class,i);
		}
		public List<LookupContext> lookup() {
			return getRuleContexts(LookupContext.class);
		}
		public LookupContext lookup(int i) {
			return getRuleContext(LookupContext.class,i);
		}
		public PostfixExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfixExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPostfixExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPostfixExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPostfixExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostfixExprContext postfixExpr() throws RecognitionException {
		PostfixExprContext _localctx = new PostfixExprContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_postfixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1600);
			((PostfixExprContext)_localctx).main_expr = primaryExpr();
			setState(1606);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,129,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1604);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LBRACKET:
						{
						setState(1601);
						predicate();
						}
						break;
					case LPAREN:
						{
						setState(1602);
						argumentList();
						}
						break;
					case QUESTION:
						{
						setState(1603);
						lookup();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(1608);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,129,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public ArgumentContext argument;
		public List<ArgumentContext> args = new ArrayList<ArgumentContext>();
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1609);
			match(LPAREN);
			setState(1618);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
				{
				setState(1610);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(1615);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1611);
					match(COMMA);
					setState(1612);
					((ArgumentListContext)_localctx).argument = argument();
					((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
					}
					}
					setState(1617);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1620);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateListContext extends ParserRuleContext {
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public PredicateListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPredicateList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPredicateList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPredicateList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateListContext predicateList() throws RecognitionException {
		PredicateListContext _localctx = new PredicateListContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_predicateList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1625);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,132,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1622);
					predicate();
					}
					} 
				}
				setState(1627);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,132,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(XQueryParserScripting.LBRACKET, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(XQueryParserScripting.RBRACKET, 0); }
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1628);
			match(LBRACKET);
			setState(1629);
			expr();
			setState(1630);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LookupContext extends ParserRuleContext {
		public TerminalNode QUESTION() { return getToken(XQueryParserScripting.QUESTION, 0); }
		public KeySpecifierContext keySpecifier() {
			return getRuleContext(KeySpecifierContext.class,0);
		}
		public LookupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lookup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterLookup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitLookup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LookupContext lookup() throws RecognitionException {
		LookupContext _localctx = new LookupContext(_ctx, getState());
		enterRule(_localctx, 294, RULE_lookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1632);
			match(QUESTION);
			setState(1633);
			keySpecifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeySpecifierContext extends ParserRuleContext {
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode IntegerLiteral() { return getToken(XQueryParserScripting.IntegerLiteral, 0); }
		public ParenthesizedExprContext parenthesizedExpr() {
			return getRuleContext(ParenthesizedExprContext.class,0);
		}
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public KeySpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keySpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterKeySpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitKeySpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitKeySpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeySpecifierContext keySpecifier() throws RecognitionException {
		KeySpecifierContext _localctx = new KeySpecifierContext(_ctx, getState());
		enterRule(_localctx, 296, RULE_keySpecifier);
		try {
			setState(1639);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1635);
				ncName();
				}
				break;
			case IntegerLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(1636);
				match(IntegerLiteral);
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 3);
				{
				setState(1637);
				parenthesizedExpr();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(1638);
				match(STAR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrowFunctionSpecifierContext extends ParserRuleContext {
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public ParenthesizedExprContext parenthesizedExpr() {
			return getRuleContext(ParenthesizedExprContext.class,0);
		}
		public ArrowFunctionSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrowFunctionSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterArrowFunctionSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitArrowFunctionSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitArrowFunctionSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowFunctionSpecifierContext arrowFunctionSpecifier() throws RecognitionException {
		ArrowFunctionSpecifierContext _localctx = new ArrowFunctionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 298, RULE_arrowFunctionSpecifier);
		try {
			setState(1644);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case URIQualifiedName:
			case FullQName:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1641);
				eqName();
				}
				break;
			case DOLLAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(1642);
				varRef();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 3);
				{
				setState(1643);
				parenthesizedExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExprContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public ParenthesizedExprContext parenthesizedExpr() {
			return getRuleContext(ParenthesizedExprContext.class,0);
		}
		public ContextItemExprContext contextItemExpr() {
			return getRuleContext(ContextItemExprContext.class,0);
		}
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public OrderedExprContext orderedExpr() {
			return getRuleContext(OrderedExprContext.class,0);
		}
		public UnorderedExprContext unorderedExpr() {
			return getRuleContext(UnorderedExprContext.class,0);
		}
		public NodeConstructorContext nodeConstructor() {
			return getRuleContext(NodeConstructorContext.class,0);
		}
		public FunctionItemExprContext functionItemExpr() {
			return getRuleContext(FunctionItemExprContext.class,0);
		}
		public MapConstructorContext mapConstructor() {
			return getRuleContext(MapConstructorContext.class,0);
		}
		public ArrayConstructorContext arrayConstructor() {
			return getRuleContext(ArrayConstructorContext.class,0);
		}
		public StringConstructorContext stringConstructor() {
			return getRuleContext(StringConstructorContext.class,0);
		}
		public UnaryLookupContext unaryLookup() {
			return getRuleContext(UnaryLookupContext.class,0);
		}
		public BlockExprContext blockExpr() {
			return getRuleContext(BlockExprContext.class,0);
		}
		public PrimaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPrimaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPrimaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExprContext primaryExpr() throws RecognitionException {
		PrimaryExprContext _localctx = new PrimaryExprContext(_ctx, getState());
		enterRule(_localctx, 300, RULE_primaryExpr);
		try {
			setState(1660);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1646);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1647);
				varRef();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1648);
				parenthesizedExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1649);
				contextItemExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1650);
				functionCall();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1651);
				orderedExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1652);
				unorderedExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1653);
				nodeConstructor();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1654);
				functionItemExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1655);
				mapConstructor();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1656);
				arrayConstructor();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1657);
				stringConstructor();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1658);
				unaryLookup();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1659);
				blockExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public NumericLiteralContext numericLiteral() {
			return getRuleContext(NumericLiteralContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 302, RULE_literal);
		try {
			setState(1664);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case DecimalLiteral:
			case DoubleLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(1662);
				numericLiteral();
				}
				break;
			case Quot:
			case Apos:
				enterOuterAlt(_localctx, 2);
				{
				setState(1663);
				stringLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NumericLiteralContext extends ParserRuleContext {
		public TerminalNode IntegerLiteral() { return getToken(XQueryParserScripting.IntegerLiteral, 0); }
		public TerminalNode DecimalLiteral() { return getToken(XQueryParserScripting.DecimalLiteral, 0); }
		public TerminalNode DoubleLiteral() { return getToken(XQueryParserScripting.DoubleLiteral, 0); }
		public NumericLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNumericLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNumericLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNumericLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericLiteralContext numericLiteral() throws RecognitionException {
		NumericLiteralContext _localctx = new NumericLiteralContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_numericLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1666);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 224L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarRefContext extends ParserRuleContext {
		public TerminalNode DOLLAR() { return getToken(XQueryParserScripting.DOLLAR, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public VarRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterVarRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitVarRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitVarRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarRefContext varRef() throws RecognitionException {
		VarRefContext _localctx = new VarRefContext(_ctx, getState());
		enterRule(_localctx, 306, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1668);
			match(DOLLAR);
			setState(1669);
			eqName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarNameContext extends ParserRuleContext {
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public VarNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterVarName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitVarName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitVarName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarNameContext varName() throws RecognitionException {
		VarNameContext _localctx = new VarNameContext(_ctx, getState());
		enterRule(_localctx, 308, RULE_varName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1671);
			eqName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParenthesizedExprContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParenthesizedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenthesizedExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterParenthesizedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitParenthesizedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitParenthesizedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenthesizedExprContext parenthesizedExpr() throws RecognitionException {
		ParenthesizedExprContext _localctx = new ParenthesizedExprContext(_ctx, getState());
		enterRule(_localctx, 310, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1673);
			match(LPAREN);
			setState(1675);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
				{
				setState(1674);
				expr();
				}
			}

			setState(1677);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContextItemExprContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(XQueryParserScripting.DOT, 0); }
		public ContextItemExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextItemExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterContextItemExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitContextItemExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitContextItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemExprContext contextItemExpr() throws RecognitionException {
		ContextItemExprContext _localctx = new ContextItemExprContext(_ctx, getState());
		enterRule(_localctx, 312, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1679);
			match(DOT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderedExprContext extends ParserRuleContext {
		public TerminalNode KW_ORDERED() { return getToken(XQueryParserScripting.KW_ORDERED, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public OrderedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderedExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterOrderedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitOrderedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitOrderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderedExprContext orderedExpr() throws RecognitionException {
		OrderedExprContext _localctx = new OrderedExprContext(_ctx, getState());
		enterRule(_localctx, 314, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1681);
			match(KW_ORDERED);
			setState(1682);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnorderedExprContext extends ParserRuleContext {
		public TerminalNode KW_UNORDERED() { return getToken(XQueryParserScripting.KW_UNORDERED, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public UnorderedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unorderedExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterUnorderedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitUnorderedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitUnorderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnorderedExprContext unorderedExpr() throws RecognitionException {
		UnorderedExprContext _localctx = new UnorderedExprContext(_ctx, getState());
		enterRule(_localctx, 316, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1684);
			match(KW_UNORDERED);
			setState(1685);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public EqNameContext fn_name;
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 318, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1687);
			((FunctionCallContext)_localctx).fn_name = eqName();
			setState(1688);
			argumentList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentContext extends ParserRuleContext {
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(XQueryParserScripting.QUESTION, 0); }
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_argument);
		try {
			setState(1692);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1690);
				exprSingle();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1691);
				match(QUESTION);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NodeConstructorContext extends ParserRuleContext {
		public DirectConstructorContext directConstructor() {
			return getRuleContext(DirectConstructorContext.class,0);
		}
		public ComputedConstructorContext computedConstructor() {
			return getRuleContext(ComputedConstructorContext.class,0);
		}
		public NodeConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNodeConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNodeConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNodeConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeConstructorContext nodeConstructor() throws RecognitionException {
		NodeConstructorContext _localctx = new NodeConstructorContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_nodeConstructor);
		try {
			setState(1696);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMMENT:
			case PI:
			case LANGLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1694);
				directConstructor();
				}
				break;
			case KW_ATTRIBUTE:
			case KW_BINARY:
			case KW_COMMENT:
			case KW_DOCUMENT:
			case KW_ELEMENT:
			case KW_NAMESPACE:
			case KW_PI:
			case KW_TEXT:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1695);
				computedConstructor();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirectConstructorContext extends ParserRuleContext {
		public DirElemConstructorOpenCloseContext dirElemConstructorOpenClose() {
			return getRuleContext(DirElemConstructorOpenCloseContext.class,0);
		}
		public DirElemConstructorSingleTagContext dirElemConstructorSingleTag() {
			return getRuleContext(DirElemConstructorSingleTagContext.class,0);
		}
		public TerminalNode COMMENT() { return getToken(XQueryParserScripting.COMMENT, 0); }
		public TerminalNode PI() { return getToken(XQueryParserScripting.PI, 0); }
		public DirectConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirectConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirectConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirectConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectConstructorContext directConstructor() throws RecognitionException {
		DirectConstructorContext _localctx = new DirectConstructorContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_directConstructor);
		int _la;
		try {
			setState(1701);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,140,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1698);
				dirElemConstructorOpenClose();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1699);
				dirElemConstructorSingleTag();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1700);
				_la = _input.LA(1);
				if ( !(_la==COMMENT || _la==PI) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirElemConstructorOpenCloseContext extends ParserRuleContext {
		public QNameContext openName;
		public Token endOpen;
		public Token startClose;
		public Token slashClose;
		public QNameContext closeName;
		public List<TerminalNode> LANGLE() { return getTokens(XQueryParserScripting.LANGLE); }
		public TerminalNode LANGLE(int i) {
			return getToken(XQueryParserScripting.LANGLE, i);
		}
		public DirAttributeListContext dirAttributeList() {
			return getRuleContext(DirAttributeListContext.class,0);
		}
		public List<TerminalNode> RANGLE() { return getTokens(XQueryParserScripting.RANGLE); }
		public TerminalNode RANGLE(int i) {
			return getToken(XQueryParserScripting.RANGLE, i);
		}
		public List<QNameContext> qName() {
			return getRuleContexts(QNameContext.class);
		}
		public QNameContext qName(int i) {
			return getRuleContext(QNameContext.class,i);
		}
		public TerminalNode SLASH() { return getToken(XQueryParserScripting.SLASH, 0); }
		public List<DirElemContentContext> dirElemContent() {
			return getRuleContexts(DirElemContentContext.class);
		}
		public DirElemContentContext dirElemContent(int i) {
			return getRuleContext(DirElemContentContext.class,i);
		}
		public DirElemConstructorOpenCloseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirElemConstructorOpenClose; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirElemConstructorOpenClose(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirElemConstructorOpenClose(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirElemConstructorOpenClose(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirElemConstructorOpenCloseContext dirElemConstructorOpenClose() throws RecognitionException {
		DirElemConstructorOpenCloseContext _localctx = new DirElemConstructorOpenCloseContext(_ctx, getState());
		enterRule(_localctx, 326, RULE_dirElemConstructorOpenClose);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1703);
			match(LANGLE);
			setState(1704);
			((DirElemConstructorOpenCloseContext)_localctx).openName = qName();
			setState(1705);
			dirAttributeList();
			setState(1706);
			((DirElemConstructorOpenCloseContext)_localctx).endOpen = match(RANGLE);
			setState(1710);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,141,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1707);
					dirElemContent();
					}
					} 
				}
				setState(1712);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,141,_ctx);
			}
			setState(1713);
			((DirElemConstructorOpenCloseContext)_localctx).startClose = match(LANGLE);
			setState(1714);
			((DirElemConstructorOpenCloseContext)_localctx).slashClose = match(SLASH);
			setState(1715);
			((DirElemConstructorOpenCloseContext)_localctx).closeName = qName();
			setState(1716);
			match(RANGLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirElemConstructorSingleTagContext extends ParserRuleContext {
		public QNameContext openName;
		public Token slashClose;
		public TerminalNode LANGLE() { return getToken(XQueryParserScripting.LANGLE, 0); }
		public DirAttributeListContext dirAttributeList() {
			return getRuleContext(DirAttributeListContext.class,0);
		}
		public TerminalNode RANGLE() { return getToken(XQueryParserScripting.RANGLE, 0); }
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(XQueryParserScripting.SLASH, 0); }
		public DirElemConstructorSingleTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirElemConstructorSingleTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirElemConstructorSingleTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirElemConstructorSingleTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirElemConstructorSingleTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirElemConstructorSingleTagContext dirElemConstructorSingleTag() throws RecognitionException {
		DirElemConstructorSingleTagContext _localctx = new DirElemConstructorSingleTagContext(_ctx, getState());
		enterRule(_localctx, 328, RULE_dirElemConstructorSingleTag);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1718);
			match(LANGLE);
			setState(1719);
			((DirElemConstructorSingleTagContext)_localctx).openName = qName();
			setState(1720);
			dirAttributeList();
			setState(1721);
			((DirElemConstructorSingleTagContext)_localctx).slashClose = match(SLASH);
			setState(1722);
			match(RANGLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirAttributeListContext extends ParserRuleContext {
		public List<QNameContext> qName() {
			return getRuleContexts(QNameContext.class);
		}
		public QNameContext qName(int i) {
			return getRuleContext(QNameContext.class,i);
		}
		public List<TerminalNode> EQUAL() { return getTokens(XQueryParserScripting.EQUAL); }
		public TerminalNode EQUAL(int i) {
			return getToken(XQueryParserScripting.EQUAL, i);
		}
		public List<DirAttributeValueContext> dirAttributeValue() {
			return getRuleContexts(DirAttributeValueContext.class);
		}
		public DirAttributeValueContext dirAttributeValue(int i) {
			return getRuleContext(DirAttributeValueContext.class,i);
		}
		public DirAttributeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirAttributeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirAttributeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirAttributeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirAttributeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeListContext dirAttributeList() throws RecognitionException {
		DirAttributeListContext _localctx = new DirAttributeListContext(_ctx, getState());
		enterRule(_localctx, 330, RULE_dirAttributeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1730);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & -35184372088831L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -1L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 342269242353123319L) != 0)) {
				{
				{
				setState(1724);
				qName();
				setState(1725);
				match(EQUAL);
				setState(1726);
				dirAttributeValue();
				}
				}
				setState(1732);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirAttributeValueAposContext extends ParserRuleContext {
		public List<TerminalNode> Quot() { return getTokens(XQueryParserScripting.Quot); }
		public TerminalNode Quot(int i) {
			return getToken(XQueryParserScripting.Quot, i);
		}
		public List<TerminalNode> PredefinedEntityRef() { return getTokens(XQueryParserScripting.PredefinedEntityRef); }
		public TerminalNode PredefinedEntityRef(int i) {
			return getToken(XQueryParserScripting.PredefinedEntityRef, i);
		}
		public List<TerminalNode> CharRef() { return getTokens(XQueryParserScripting.CharRef); }
		public TerminalNode CharRef(int i) {
			return getToken(XQueryParserScripting.CharRef, i);
		}
		public List<TerminalNode> EscapeQuot() { return getTokens(XQueryParserScripting.EscapeQuot); }
		public TerminalNode EscapeQuot(int i) {
			return getToken(XQueryParserScripting.EscapeQuot, i);
		}
		public List<DirAttributeContentQuotContext> dirAttributeContentQuot() {
			return getRuleContexts(DirAttributeContentQuotContext.class);
		}
		public DirAttributeContentQuotContext dirAttributeContentQuot(int i) {
			return getRuleContext(DirAttributeContentQuotContext.class,i);
		}
		public DirAttributeValueAposContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirAttributeValueApos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirAttributeValueApos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirAttributeValueApos(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirAttributeValueApos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeValueAposContext dirAttributeValueApos() throws RecognitionException {
		DirAttributeValueAposContext _localctx = new DirAttributeValueAposContext(_ctx, getState());
		enterRule(_localctx, 332, RULE_dirAttributeValueApos);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1733);
			match(Quot);
			setState(1740);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,144,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1738);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PredefinedEntityRef:
						{
						setState(1734);
						match(PredefinedEntityRef);
						}
						break;
					case CharRef:
						{
						setState(1735);
						match(CharRef);
						}
						break;
					case EscapeQuot:
						{
						setState(1736);
						match(EscapeQuot);
						}
						break;
					case DOUBLE_LBRACE:
					case DOUBLE_RBRACE:
					case Quot:
					case LBRACE:
					case ContentChar:
						{
						setState(1737);
						dirAttributeContentQuot();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(1742);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,144,_ctx);
			}
			setState(1743);
			match(Quot);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirAttributeValueQuotContext extends ParserRuleContext {
		public List<TerminalNode> Apos() { return getTokens(XQueryParserScripting.Apos); }
		public TerminalNode Apos(int i) {
			return getToken(XQueryParserScripting.Apos, i);
		}
		public List<TerminalNode> PredefinedEntityRef() { return getTokens(XQueryParserScripting.PredefinedEntityRef); }
		public TerminalNode PredefinedEntityRef(int i) {
			return getToken(XQueryParserScripting.PredefinedEntityRef, i);
		}
		public List<TerminalNode> CharRef() { return getTokens(XQueryParserScripting.CharRef); }
		public TerminalNode CharRef(int i) {
			return getToken(XQueryParserScripting.CharRef, i);
		}
		public List<TerminalNode> EscapeApos() { return getTokens(XQueryParserScripting.EscapeApos); }
		public TerminalNode EscapeApos(int i) {
			return getToken(XQueryParserScripting.EscapeApos, i);
		}
		public List<DirAttributeContentAposContext> dirAttributeContentApos() {
			return getRuleContexts(DirAttributeContentAposContext.class);
		}
		public DirAttributeContentAposContext dirAttributeContentApos(int i) {
			return getRuleContext(DirAttributeContentAposContext.class,i);
		}
		public DirAttributeValueQuotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirAttributeValueQuot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirAttributeValueQuot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirAttributeValueQuot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirAttributeValueQuot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeValueQuotContext dirAttributeValueQuot() throws RecognitionException {
		DirAttributeValueQuotContext _localctx = new DirAttributeValueQuotContext(_ctx, getState());
		enterRule(_localctx, 334, RULE_dirAttributeValueQuot);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1745);
			match(Apos);
			setState(1752);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,146,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1750);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PredefinedEntityRef:
						{
						setState(1746);
						match(PredefinedEntityRef);
						}
						break;
					case CharRef:
						{
						setState(1747);
						match(CharRef);
						}
						break;
					case EscapeApos:
						{
						setState(1748);
						match(EscapeApos);
						}
						break;
					case DOUBLE_LBRACE:
					case DOUBLE_RBRACE:
					case Apos:
					case LBRACE:
					case ContentChar:
						{
						setState(1749);
						dirAttributeContentApos();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(1754);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,146,_ctx);
			}
			setState(1755);
			match(Apos);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirAttributeValueContext extends ParserRuleContext {
		public DirAttributeValueAposContext dirAttributeValueApos() {
			return getRuleContext(DirAttributeValueAposContext.class,0);
		}
		public DirAttributeValueQuotContext dirAttributeValueQuot() {
			return getRuleContext(DirAttributeValueQuotContext.class,0);
		}
		public DirAttributeValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirAttributeValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirAttributeValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirAttributeValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirAttributeValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeValueContext dirAttributeValue() throws RecognitionException {
		DirAttributeValueContext _localctx = new DirAttributeValueContext(_ctx, getState());
		enterRule(_localctx, 336, RULE_dirAttributeValue);
		try {
			setState(1759);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Quot:
				enterOuterAlt(_localctx, 1);
				{
				setState(1757);
				dirAttributeValueApos();
				}
				break;
			case Apos:
				enterOuterAlt(_localctx, 2);
				{
				setState(1758);
				dirAttributeValueQuot();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirAttributeContentQuotContext extends ParserRuleContext {
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParserScripting.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParserScripting.ContentChar, i);
		}
		public TerminalNode DOUBLE_LBRACE() { return getToken(XQueryParserScripting.DOUBLE_LBRACE, 0); }
		public TerminalNode DOUBLE_RBRACE() { return getToken(XQueryParserScripting.DOUBLE_RBRACE, 0); }
		public DirAttributeValueAposContext dirAttributeValueApos() {
			return getRuleContext(DirAttributeValueAposContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DirAttributeContentQuotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirAttributeContentQuot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirAttributeContentQuot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirAttributeContentQuot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirAttributeContentQuot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeContentQuotContext dirAttributeContentQuot() throws RecognitionException {
		DirAttributeContentQuotContext _localctx = new DirAttributeContentQuotContext(_ctx, getState());
		enterRule(_localctx, 338, RULE_dirAttributeContentQuot);
		int _la;
		try {
			int _alt;
			setState(1774);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ContentChar:
				enterOuterAlt(_localctx, 1);
				{
				setState(1762); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1761);
						match(ContentChar);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1764); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,148,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case DOUBLE_LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1766);
				match(DOUBLE_LBRACE);
				}
				break;
			case DOUBLE_RBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1767);
				match(DOUBLE_RBRACE);
				}
				break;
			case Quot:
				enterOuterAlt(_localctx, 4);
				{
				setState(1768);
				dirAttributeValueApos();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 5);
				{
				setState(1769);
				match(LBRACE);
				setState(1771);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
					{
					setState(1770);
					expr();
					}
				}

				setState(1773);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirAttributeContentAposContext extends ParserRuleContext {
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParserScripting.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParserScripting.ContentChar, i);
		}
		public TerminalNode DOUBLE_LBRACE() { return getToken(XQueryParserScripting.DOUBLE_LBRACE, 0); }
		public TerminalNode DOUBLE_RBRACE() { return getToken(XQueryParserScripting.DOUBLE_RBRACE, 0); }
		public DirAttributeValueQuotContext dirAttributeValueQuot() {
			return getRuleContext(DirAttributeValueQuotContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DirAttributeContentAposContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirAttributeContentApos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirAttributeContentApos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirAttributeContentApos(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirAttributeContentApos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeContentAposContext dirAttributeContentApos() throws RecognitionException {
		DirAttributeContentAposContext _localctx = new DirAttributeContentAposContext(_ctx, getState());
		enterRule(_localctx, 340, RULE_dirAttributeContentApos);
		int _la;
		try {
			int _alt;
			setState(1789);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ContentChar:
				enterOuterAlt(_localctx, 1);
				{
				setState(1777); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1776);
						match(ContentChar);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1779); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,151,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case DOUBLE_LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1781);
				match(DOUBLE_LBRACE);
				}
				break;
			case DOUBLE_RBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1782);
				match(DOUBLE_RBRACE);
				}
				break;
			case Apos:
				enterOuterAlt(_localctx, 4);
				{
				setState(1783);
				dirAttributeValueQuot();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 5);
				{
				setState(1784);
				match(LBRACE);
				setState(1786);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
					{
					setState(1785);
					expr();
					}
				}

				setState(1788);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DirElemContentContext extends ParserRuleContext {
		public DirectConstructorContext directConstructor() {
			return getRuleContext(DirectConstructorContext.class,0);
		}
		public CommonContentContext commonContent() {
			return getRuleContext(CommonContentContext.class,0);
		}
		public TerminalNode CDATA() { return getToken(XQueryParserScripting.CDATA, 0); }
		public TerminalNode Quot() { return getToken(XQueryParserScripting.Quot, 0); }
		public TerminalNode Apos() { return getToken(XQueryParserScripting.Apos, 0); }
		public NoQuotesNoBracesNoAmpNoLAngContext noQuotesNoBracesNoAmpNoLAng() {
			return getRuleContext(NoQuotesNoBracesNoAmpNoLAngContext.class,0);
		}
		public DirElemContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirElemContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDirElemContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDirElemContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDirElemContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirElemContentContext dirElemContent() throws RecognitionException {
		DirElemContentContext _localctx = new DirElemContentContext(_ctx, getState());
		enterRule(_localctx, 342, RULE_dirElemContent);
		try {
			setState(1797);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1791);
				directConstructor();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1792);
				commonContent();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1793);
				match(CDATA);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1794);
				match(Quot);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1795);
				match(Apos);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1796);
				noQuotesNoBracesNoAmpNoLAng();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommonContentContext extends ParserRuleContext {
		public TerminalNode PredefinedEntityRef() { return getToken(XQueryParserScripting.PredefinedEntityRef, 0); }
		public TerminalNode CharRef() { return getToken(XQueryParserScripting.CharRef, 0); }
		public List<TerminalNode> LBRACE() { return getTokens(XQueryParserScripting.LBRACE); }
		public TerminalNode LBRACE(int i) {
			return getToken(XQueryParserScripting.LBRACE, i);
		}
		public List<TerminalNode> RBRACE() { return getTokens(XQueryParserScripting.RBRACE); }
		public TerminalNode RBRACE(int i) {
			return getToken(XQueryParserScripting.RBRACE, i);
		}
		public BlockExprContext blockExpr() {
			return getRuleContext(BlockExprContext.class,0);
		}
		public CommonContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commonContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCommonContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCommonContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCommonContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommonContentContext commonContent() throws RecognitionException {
		CommonContentContext _localctx = new CommonContentContext(_ctx, getState());
		enterRule(_localctx, 344, RULE_commonContent);
		int _la;
		try {
			setState(1805);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1799);
				_la = _input.LA(1);
				if ( !(_la==PredefinedEntityRef || _la==CharRef) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1800);
				match(LBRACE);
				setState(1801);
				match(LBRACE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1802);
				match(RBRACE);
				setState(1803);
				match(RBRACE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1804);
				blockExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContentExprContext extends ParserRuleContext {
		public StatementsAndExprContext statementsAndExpr() {
			return getRuleContext(StatementsAndExprContext.class,0);
		}
		public ContentExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contentExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterContentExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitContentExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitContentExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContentExprContext contentExpr() throws RecognitionException {
		ContentExprContext _localctx = new ContentExprContext(_ctx, getState());
		enterRule(_localctx, 346, RULE_contentExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1807);
			statementsAndExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComputedConstructorContext extends ParserRuleContext {
		public CompDocConstructorContext compDocConstructor() {
			return getRuleContext(CompDocConstructorContext.class,0);
		}
		public CompElemConstructorContext compElemConstructor() {
			return getRuleContext(CompElemConstructorContext.class,0);
		}
		public CompAttrConstructorContext compAttrConstructor() {
			return getRuleContext(CompAttrConstructorContext.class,0);
		}
		public CompNamespaceConstructorContext compNamespaceConstructor() {
			return getRuleContext(CompNamespaceConstructorContext.class,0);
		}
		public CompTextConstructorContext compTextConstructor() {
			return getRuleContext(CompTextConstructorContext.class,0);
		}
		public CompCommentConstructorContext compCommentConstructor() {
			return getRuleContext(CompCommentConstructorContext.class,0);
		}
		public CompPIConstructorContext compPIConstructor() {
			return getRuleContext(CompPIConstructorContext.class,0);
		}
		public CompMLJSONConstructorContext compMLJSONConstructor() {
			return getRuleContext(CompMLJSONConstructorContext.class,0);
		}
		public ComputedConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_computedConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterComputedConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitComputedConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitComputedConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComputedConstructorContext computedConstructor() throws RecognitionException {
		ComputedConstructorContext _localctx = new ComputedConstructorContext(_ctx, getState());
		enterRule(_localctx, 348, RULE_computedConstructor);
		try {
			setState(1817);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_DOCUMENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1809);
				compDocConstructor();
				}
				break;
			case KW_ELEMENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1810);
				compElemConstructor();
				}
				break;
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1811);
				compAttrConstructor();
				}
				break;
			case KW_NAMESPACE:
				enterOuterAlt(_localctx, 4);
				{
				setState(1812);
				compNamespaceConstructor();
				}
				break;
			case KW_TEXT:
				enterOuterAlt(_localctx, 5);
				{
				setState(1813);
				compTextConstructor();
				}
				break;
			case KW_COMMENT:
				enterOuterAlt(_localctx, 6);
				{
				setState(1814);
				compCommentConstructor();
				}
				break;
			case KW_PI:
				enterOuterAlt(_localctx, 7);
				{
				setState(1815);
				compPIConstructor();
				}
				break;
			case KW_BINARY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
				enterOuterAlt(_localctx, 8);
				{
				setState(1816);
				compMLJSONConstructor();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompMLJSONConstructorContext extends ParserRuleContext {
		public CompMLJSONArrayConstructorContext compMLJSONArrayConstructor() {
			return getRuleContext(CompMLJSONArrayConstructorContext.class,0);
		}
		public CompMLJSONObjectConstructorContext compMLJSONObjectConstructor() {
			return getRuleContext(CompMLJSONObjectConstructorContext.class,0);
		}
		public CompMLJSONNumberConstructorContext compMLJSONNumberConstructor() {
			return getRuleContext(CompMLJSONNumberConstructorContext.class,0);
		}
		public CompMLJSONBooleanConstructorContext compMLJSONBooleanConstructor() {
			return getRuleContext(CompMLJSONBooleanConstructorContext.class,0);
		}
		public CompMLJSONNullConstructorContext compMLJSONNullConstructor() {
			return getRuleContext(CompMLJSONNullConstructorContext.class,0);
		}
		public CompBinaryConstructorContext compBinaryConstructor() {
			return getRuleContext(CompBinaryConstructorContext.class,0);
		}
		public CompMLJSONConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompMLJSONConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompMLJSONConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompMLJSONConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONConstructorContext compMLJSONConstructor() throws RecognitionException {
		CompMLJSONConstructorContext _localctx = new CompMLJSONConstructorContext(_ctx, getState());
		enterRule(_localctx, 350, RULE_compMLJSONConstructor);
		try {
			setState(1825);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ARRAY_NODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1819);
				compMLJSONArrayConstructor();
				}
				break;
			case KW_OBJECT_NODE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1820);
				compMLJSONObjectConstructor();
				}
				break;
			case KW_NUMBER_NODE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1821);
				compMLJSONNumberConstructor();
				}
				break;
			case KW_BOOLEAN_NODE:
				enterOuterAlt(_localctx, 4);
				{
				setState(1822);
				compMLJSONBooleanConstructor();
				}
				break;
			case KW_NULL_NODE:
				enterOuterAlt(_localctx, 5);
				{
				setState(1823);
				compMLJSONNullConstructor();
				}
				break;
			case KW_BINARY:
				enterOuterAlt(_localctx, 6);
				{
				setState(1824);
				compBinaryConstructor();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompMLJSONArrayConstructorContext extends ParserRuleContext {
		public TerminalNode KW_ARRAY_NODE() { return getToken(XQueryParserScripting.KW_ARRAY_NODE, 0); }
		public EnclosedContentExprContext enclosedContentExpr() {
			return getRuleContext(EnclosedContentExprContext.class,0);
		}
		public CompMLJSONArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONArrayConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompMLJSONArrayConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompMLJSONArrayConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompMLJSONArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONArrayConstructorContext compMLJSONArrayConstructor() throws RecognitionException {
		CompMLJSONArrayConstructorContext _localctx = new CompMLJSONArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 352, RULE_compMLJSONArrayConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1827);
			match(KW_ARRAY_NODE);
			setState(1828);
			enclosedContentExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompMLJSONObjectConstructorContext extends ParserRuleContext {
		public TerminalNode KW_OBJECT_NODE() { return getToken(XQueryParserScripting.KW_OBJECT_NODE, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(XQueryParserScripting.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(XQueryParserScripting.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public CompMLJSONObjectConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONObjectConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompMLJSONObjectConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompMLJSONObjectConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompMLJSONObjectConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONObjectConstructorContext compMLJSONObjectConstructor() throws RecognitionException {
		CompMLJSONObjectConstructorContext _localctx = new CompMLJSONObjectConstructorContext(_ctx, getState());
		enterRule(_localctx, 354, RULE_compMLJSONObjectConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1830);
			match(KW_OBJECT_NODE);
			setState(1831);
			match(LBRACE);
			setState(1845);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
				{
				setState(1832);
				exprSingle();
				setState(1833);
				match(COLON);
				setState(1834);
				exprSingle();
				setState(1842);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1835);
					match(COMMA);
					setState(1836);
					exprSingle();
					setState(1837);
					match(COLON);
					setState(1838);
					exprSingle();
					}
					}
					setState(1844);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1847);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompMLJSONNumberConstructorContext extends ParserRuleContext {
		public TerminalNode KW_NUMBER_NODE() { return getToken(XQueryParserScripting.KW_NUMBER_NODE, 0); }
		public EnclosedContentExprContext enclosedContentExpr() {
			return getRuleContext(EnclosedContentExprContext.class,0);
		}
		public CompMLJSONNumberConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONNumberConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompMLJSONNumberConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompMLJSONNumberConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompMLJSONNumberConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONNumberConstructorContext compMLJSONNumberConstructor() throws RecognitionException {
		CompMLJSONNumberConstructorContext _localctx = new CompMLJSONNumberConstructorContext(_ctx, getState());
		enterRule(_localctx, 356, RULE_compMLJSONNumberConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1849);
			match(KW_NUMBER_NODE);
			setState(1850);
			enclosedContentExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompMLJSONBooleanConstructorContext extends ParserRuleContext {
		public TerminalNode KW_BOOLEAN_NODE() { return getToken(XQueryParserScripting.KW_BOOLEAN_NODE, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public CompMLJSONBooleanConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONBooleanConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompMLJSONBooleanConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompMLJSONBooleanConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompMLJSONBooleanConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONBooleanConstructorContext compMLJSONBooleanConstructor() throws RecognitionException {
		CompMLJSONBooleanConstructorContext _localctx = new CompMLJSONBooleanConstructorContext(_ctx, getState());
		enterRule(_localctx, 358, RULE_compMLJSONBooleanConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1852);
			match(KW_BOOLEAN_NODE);
			setState(1853);
			match(LBRACE);
			setState(1854);
			exprSingle();
			setState(1855);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompMLJSONNullConstructorContext extends ParserRuleContext {
		public TerminalNode KW_NULL_NODE() { return getToken(XQueryParserScripting.KW_NULL_NODE, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public CompMLJSONNullConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONNullConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompMLJSONNullConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompMLJSONNullConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompMLJSONNullConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONNullConstructorContext compMLJSONNullConstructor() throws RecognitionException {
		CompMLJSONNullConstructorContext _localctx = new CompMLJSONNullConstructorContext(_ctx, getState());
		enterRule(_localctx, 360, RULE_compMLJSONNullConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1857);
			match(KW_NULL_NODE);
			setState(1858);
			match(LBRACE);
			setState(1859);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompBinaryConstructorContext extends ParserRuleContext {
		public TerminalNode KW_BINARY() { return getToken(XQueryParserScripting.KW_BINARY, 0); }
		public EnclosedContentExprContext enclosedContentExpr() {
			return getRuleContext(EnclosedContentExprContext.class,0);
		}
		public CompBinaryConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compBinaryConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompBinaryConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompBinaryConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompBinaryConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompBinaryConstructorContext compBinaryConstructor() throws RecognitionException {
		CompBinaryConstructorContext _localctx = new CompBinaryConstructorContext(_ctx, getState());
		enterRule(_localctx, 362, RULE_compBinaryConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1861);
			match(KW_BINARY);
			setState(1862);
			enclosedContentExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompDocConstructorContext extends ParserRuleContext {
		public TerminalNode KW_DOCUMENT() { return getToken(XQueryParserScripting.KW_DOCUMENT, 0); }
		public BlockExprContext blockExpr() {
			return getRuleContext(BlockExprContext.class,0);
		}
		public CompDocConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compDocConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompDocConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompDocConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompDocConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompDocConstructorContext compDocConstructor() throws RecognitionException {
		CompDocConstructorContext _localctx = new CompDocConstructorContext(_ctx, getState());
		enterRule(_localctx, 364, RULE_compDocConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1864);
			match(KW_DOCUMENT);
			setState(1865);
			blockExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompElemConstructorContext extends ParserRuleContext {
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParserScripting.KW_ELEMENT, 0); }
		public EnclosedContentExprContext enclosedContentExpr() {
			return getRuleContext(EnclosedContentExprContext.class,0);
		}
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public CompElemConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compElemConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompElemConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompElemConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompElemConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompElemConstructorContext compElemConstructor() throws RecognitionException {
		CompElemConstructorContext _localctx = new CompElemConstructorContext(_ctx, getState());
		enterRule(_localctx, 366, RULE_compElemConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1867);
			match(KW_ELEMENT);
			setState(1873);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case URIQualifiedName:
			case FullQName:
			case NCName:
				{
				setState(1868);
				eqName();
				}
				break;
			case LBRACE:
				{
				{
				setState(1869);
				match(LBRACE);
				setState(1870);
				expr();
				setState(1871);
				match(RBRACE);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1875);
			enclosedContentExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnclosedContentExprContext extends ParserRuleContext {
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public EnclosedContentExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enclosedContentExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterEnclosedContentExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitEnclosedContentExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitEnclosedContentExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedContentExprContext enclosedContentExpr() throws RecognitionException {
		EnclosedContentExprContext _localctx = new EnclosedContentExprContext(_ctx, getState());
		enterRule(_localctx, 368, RULE_enclosedContentExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1877);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompAttrConstructorContext extends ParserRuleContext {
		public TerminalNode KW_ATTRIBUTE() { return getToken(XQueryParserScripting.KW_ATTRIBUTE, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public List<TerminalNode> LBRACE() { return getTokens(XQueryParserScripting.LBRACE); }
		public TerminalNode LBRACE(int i) {
			return getToken(XQueryParserScripting.LBRACE, i);
		}
		public List<TerminalNode> RBRACE() { return getTokens(XQueryParserScripting.RBRACE); }
		public TerminalNode RBRACE(int i) {
			return getToken(XQueryParserScripting.RBRACE, i);
		}
		public BlockExprContext blockExpr() {
			return getRuleContext(BlockExprContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public CompAttrConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compAttrConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompAttrConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompAttrConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompAttrConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompAttrConstructorContext compAttrConstructor() throws RecognitionException {
		CompAttrConstructorContext _localctx = new CompAttrConstructorContext(_ctx, getState());
		enterRule(_localctx, 370, RULE_compAttrConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1879);
			match(KW_ATTRIBUTE);
			setState(1885);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case URIQualifiedName:
			case FullQName:
			case NCName:
				{
				setState(1880);
				eqName();
				}
				break;
			case LBRACE:
				{
				{
				setState(1881);
				match(LBRACE);
				setState(1882);
				expr();
				setState(1883);
				match(RBRACE);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1890);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,162,_ctx) ) {
			case 1:
				{
				setState(1887);
				match(LBRACE);
				setState(1888);
				match(RBRACE);
				}
				break;
			case 2:
				{
				setState(1889);
				blockExpr();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompNamespaceConstructorContext extends ParserRuleContext {
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParserScripting.KW_NAMESPACE, 0); }
		public EnclosedURIExprContext enclosedURIExpr() {
			return getRuleContext(EnclosedURIExprContext.class,0);
		}
		public PrefixContext prefix() {
			return getRuleContext(PrefixContext.class,0);
		}
		public EnclosedPrefixExprContext enclosedPrefixExpr() {
			return getRuleContext(EnclosedPrefixExprContext.class,0);
		}
		public CompNamespaceConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compNamespaceConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompNamespaceConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompNamespaceConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompNamespaceConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompNamespaceConstructorContext compNamespaceConstructor() throws RecognitionException {
		CompNamespaceConstructorContext _localctx = new CompNamespaceConstructorContext(_ctx, getState());
		enterRule(_localctx, 372, RULE_compNamespaceConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1892);
			match(KW_NAMESPACE);
			setState(1895);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case NCName:
				{
				setState(1893);
				prefix();
				}
				break;
			case LBRACE:
				{
				setState(1894);
				enclosedPrefixExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1897);
			enclosedURIExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixContext extends ParserRuleContext {
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public PrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixContext prefix() throws RecognitionException {
		PrefixContext _localctx = new PrefixContext(_ctx, getState());
		enterRule(_localctx, 374, RULE_prefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1899);
			ncName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnclosedPrefixExprContext extends ParserRuleContext {
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public EnclosedPrefixExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enclosedPrefixExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterEnclosedPrefixExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitEnclosedPrefixExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitEnclosedPrefixExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedPrefixExprContext enclosedPrefixExpr() throws RecognitionException {
		EnclosedPrefixExprContext _localctx = new EnclosedPrefixExprContext(_ctx, getState());
		enterRule(_localctx, 376, RULE_enclosedPrefixExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1901);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EnclosedURIExprContext extends ParserRuleContext {
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public EnclosedURIExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enclosedURIExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterEnclosedURIExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitEnclosedURIExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitEnclosedURIExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedURIExprContext enclosedURIExpr() throws RecognitionException {
		EnclosedURIExprContext _localctx = new EnclosedURIExprContext(_ctx, getState());
		enterRule(_localctx, 378, RULE_enclosedURIExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1903);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompTextConstructorContext extends ParserRuleContext {
		public TerminalNode KW_TEXT() { return getToken(XQueryParserScripting.KW_TEXT, 0); }
		public BlockExprContext blockExpr() {
			return getRuleContext(BlockExprContext.class,0);
		}
		public CompTextConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compTextConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompTextConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompTextConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompTextConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompTextConstructorContext compTextConstructor() throws RecognitionException {
		CompTextConstructorContext _localctx = new CompTextConstructorContext(_ctx, getState());
		enterRule(_localctx, 380, RULE_compTextConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1905);
			match(KW_TEXT);
			setState(1906);
			blockExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompCommentConstructorContext extends ParserRuleContext {
		public TerminalNode KW_COMMENT() { return getToken(XQueryParserScripting.KW_COMMENT, 0); }
		public BlockExprContext blockExpr() {
			return getRuleContext(BlockExprContext.class,0);
		}
		public CompCommentConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compCommentConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompCommentConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompCommentConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompCommentConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompCommentConstructorContext compCommentConstructor() throws RecognitionException {
		CompCommentConstructorContext _localctx = new CompCommentConstructorContext(_ctx, getState());
		enterRule(_localctx, 382, RULE_compCommentConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1908);
			match(KW_COMMENT);
			setState(1909);
			blockExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompPIConstructorContext extends ParserRuleContext {
		public TerminalNode KW_PI() { return getToken(XQueryParserScripting.KW_PI, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public List<TerminalNode> LBRACE() { return getTokens(XQueryParserScripting.LBRACE); }
		public TerminalNode LBRACE(int i) {
			return getToken(XQueryParserScripting.LBRACE, i);
		}
		public List<TerminalNode> RBRACE() { return getTokens(XQueryParserScripting.RBRACE); }
		public TerminalNode RBRACE(int i) {
			return getToken(XQueryParserScripting.RBRACE, i);
		}
		public BlockExprContext blockExpr() {
			return getRuleContext(BlockExprContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public CompPIConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compPIConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCompPIConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCompPIConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCompPIConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompPIConstructorContext compPIConstructor() throws RecognitionException {
		CompPIConstructorContext _localctx = new CompPIConstructorContext(_ctx, getState());
		enterRule(_localctx, 384, RULE_compPIConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1911);
			match(KW_PI);
			setState(1917);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case NCName:
				{
				setState(1912);
				ncName();
				}
				break;
			case LBRACE:
				{
				{
				setState(1913);
				match(LBRACE);
				setState(1914);
				expr();
				setState(1915);
				match(RBRACE);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1922);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,165,_ctx) ) {
			case 1:
				{
				setState(1919);
				match(LBRACE);
				setState(1920);
				match(RBRACE);
				}
				break;
			case 2:
				{
				setState(1921);
				blockExpr();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionItemExprContext extends ParserRuleContext {
		public NamedFunctionRefContext namedFunctionRef() {
			return getRuleContext(NamedFunctionRefContext.class,0);
		}
		public InlineFunctionRefContext inlineFunctionRef() {
			return getRuleContext(InlineFunctionRefContext.class,0);
		}
		public FunctionItemExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionItemExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionItemExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionItemExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionItemExprContext functionItemExpr() throws RecognitionException {
		FunctionItemExprContext _localctx = new FunctionItemExprContext(_ctx, getState());
		enterRule(_localctx, 386, RULE_functionItemExpr);
		try {
			setState(1926);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,166,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1924);
				namedFunctionRef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1925);
				inlineFunctionRef();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamedFunctionRefContext extends ParserRuleContext {
		public EqNameContext fn_name;
		public Token arity;
		public TerminalNode HASH() { return getToken(XQueryParserScripting.HASH, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode IntegerLiteral() { return getToken(XQueryParserScripting.IntegerLiteral, 0); }
		public NamedFunctionRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedFunctionRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNamedFunctionRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNamedFunctionRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNamedFunctionRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedFunctionRefContext namedFunctionRef() throws RecognitionException {
		NamedFunctionRefContext _localctx = new NamedFunctionRefContext(_ctx, getState());
		enterRule(_localctx, 388, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1928);
			((NamedFunctionRefContext)_localctx).fn_name = eqName();
			setState(1929);
			match(HASH);
			setState(1930);
			((NamedFunctionRefContext)_localctx).arity = match(IntegerLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InlineFunctionRefContext extends ParserRuleContext {
		public SequenceTypeContext return_type;
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParserScripting.KW_FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public FunctionParamsContext functionParams() {
			return getRuleContext(FunctionParamsContext.class,0);
		}
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public InlineFunctionRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineFunctionRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterInlineFunctionRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitInlineFunctionRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitInlineFunctionRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineFunctionRefContext inlineFunctionRef() throws RecognitionException {
		InlineFunctionRefContext _localctx = new InlineFunctionRefContext(_ctx, getState());
		enterRule(_localctx, 390, RULE_inlineFunctionRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1932);
			annotations();
			setState(1933);
			match(KW_FUNCTION);
			setState(1934);
			match(LPAREN);
			setState(1936);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(1935);
				functionParams();
				}
			}

			setState(1938);
			match(RPAREN);
			setState(1941);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(1939);
				match(KW_AS);
				setState(1940);
				((InlineFunctionRefContext)_localctx).return_type = sequenceType();
				}
			}

			setState(1943);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public StatementsAndOptionalExprContext statementsAndOptionalExpr() {
			return getRuleContext(StatementsAndOptionalExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 392, RULE_functionBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1945);
			match(LBRACE);
			setState(1946);
			statementsAndOptionalExpr();
			setState(1947);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapConstructorContext extends ParserRuleContext {
		public TerminalNode KW_MAP() { return getToken(XQueryParserScripting.KW_MAP, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public List<MapConstructorEntryContext> mapConstructorEntry() {
			return getRuleContexts(MapConstructorEntryContext.class);
		}
		public MapConstructorEntryContext mapConstructorEntry(int i) {
			return getRuleContext(MapConstructorEntryContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public MapConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMapConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMapConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMapConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapConstructorContext mapConstructor() throws RecognitionException {
		MapConstructorContext _localctx = new MapConstructorContext(_ctx, getState());
		enterRule(_localctx, 394, RULE_mapConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1949);
			match(KW_MAP);
			setState(1950);
			match(LBRACE);
			setState(1959);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
				{
				setState(1951);
				mapConstructorEntry();
				setState(1956);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1952);
					match(COMMA);
					setState(1953);
					mapConstructorEntry();
					}
					}
					setState(1958);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1961);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapConstructorEntryContext extends ParserRuleContext {
		public ExprSingleContext mapKey;
		public ExprSingleContext mapValue;
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public TerminalNode COLON() { return getToken(XQueryParserScripting.COLON, 0); }
		public TerminalNode COLON_EQ() { return getToken(XQueryParserScripting.COLON_EQ, 0); }
		public MapConstructorEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapConstructorEntry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMapConstructorEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMapConstructorEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMapConstructorEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapConstructorEntryContext mapConstructorEntry() throws RecognitionException {
		MapConstructorEntryContext _localctx = new MapConstructorEntryContext(_ctx, getState());
		enterRule(_localctx, 396, RULE_mapConstructorEntry);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1963);
			((MapConstructorEntryContext)_localctx).mapKey = exprSingle();
			setState(1964);
			_la = _input.LA(1);
			if ( !(_la==COLON || _la==COLON_EQ) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1965);
			((MapConstructorEntryContext)_localctx).mapValue = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayConstructorContext extends ParserRuleContext {
		public SquareArrayConstructorContext squareArrayConstructor() {
			return getRuleContext(SquareArrayConstructorContext.class,0);
		}
		public CurlyArrayConstructorContext curlyArrayConstructor() {
			return getRuleContext(CurlyArrayConstructorContext.class,0);
		}
		public ArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterArrayConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitArrayConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayConstructorContext arrayConstructor() throws RecognitionException {
		ArrayConstructorContext _localctx = new ArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 398, RULE_arrayConstructor);
		try {
			setState(1969);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACKET:
				enterOuterAlt(_localctx, 1);
				{
				setState(1967);
				squareArrayConstructor();
				}
				break;
			case KW_ARRAY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1968);
				curlyArrayConstructor();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SquareArrayConstructorContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(XQueryParserScripting.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(XQueryParserScripting.RBRACKET, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SquareArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_squareArrayConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSquareArrayConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSquareArrayConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSquareArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SquareArrayConstructorContext squareArrayConstructor() throws RecognitionException {
		SquareArrayConstructorContext _localctx = new SquareArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 400, RULE_squareArrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1971);
			match(LBRACKET);
			setState(1973);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8939915460822560L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720800257L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 263L) != 0)) {
				{
				setState(1972);
				expr();
				}
			}

			setState(1975);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CurlyArrayConstructorContext extends ParserRuleContext {
		public TerminalNode KW_ARRAY() { return getToken(XQueryParserScripting.KW_ARRAY, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public CurlyArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_curlyArrayConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCurlyArrayConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCurlyArrayConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCurlyArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CurlyArrayConstructorContext curlyArrayConstructor() throws RecognitionException {
		CurlyArrayConstructorContext _localctx = new CurlyArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 402, RULE_curlyArrayConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1977);
			match(KW_ARRAY);
			setState(1978);
			enclosedExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringConstructorContext extends ParserRuleContext {
		public TerminalNode ENTER_STRING() { return getToken(XQueryParserScripting.ENTER_STRING, 0); }
		public StringConstructorContentContext stringConstructorContent() {
			return getRuleContext(StringConstructorContentContext.class,0);
		}
		public TerminalNode EXIT_STRING() { return getToken(XQueryParserScripting.EXIT_STRING, 0); }
		public StringConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstructorContext stringConstructor() throws RecognitionException {
		StringConstructorContext _localctx = new StringConstructorContext(_ctx, getState());
		enterRule(_localctx, 404, RULE_stringConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1980);
			match(ENTER_STRING);
			setState(1981);
			stringConstructorContent();
			setState(1982);
			match(EXIT_STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringConstructorContentContext extends ParserRuleContext {
		public List<StringConstructorCharsContext> stringConstructorChars() {
			return getRuleContexts(StringConstructorCharsContext.class);
		}
		public StringConstructorCharsContext stringConstructorChars(int i) {
			return getRuleContext(StringConstructorCharsContext.class,i);
		}
		public List<StringConstructorInterpolationContext> stringConstructorInterpolation() {
			return getRuleContexts(StringConstructorInterpolationContext.class);
		}
		public StringConstructorInterpolationContext stringConstructorInterpolation(int i) {
			return getRuleContext(StringConstructorInterpolationContext.class,i);
		}
		public StringConstructorContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConstructorContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringConstructorContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringConstructorContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringConstructorContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstructorContentContext stringConstructorContent() throws RecognitionException {
		StringConstructorContentContext _localctx = new StringConstructorContentContext(_ctx, getState());
		enterRule(_localctx, 406, RULE_stringConstructorContent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1984);
			stringConstructorChars();
			setState(1990);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ENTER_INTERPOLATION) {
				{
				{
				setState(1985);
				stringConstructorInterpolation();
				setState(1986);
				stringConstructorChars();
				}
				}
				setState(1992);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CharNoGraveContext extends ParserRuleContext {
		public TerminalNode BASIC_CHAR() { return getToken(XQueryParserScripting.BASIC_CHAR, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public TerminalNode RBRACKET() { return getToken(XQueryParserScripting.RBRACKET, 0); }
		public CharNoGraveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charNoGrave; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCharNoGrave(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCharNoGrave(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCharNoGrave(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharNoGraveContext charNoGrave() throws RecognitionException {
		CharNoGraveContext _localctx = new CharNoGraveContext(_ctx, getState());
		enterRule(_localctx, 408, RULE_charNoGrave);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1993);
			_la = _input.LA(1);
			if ( !(_la==RBRACKET || _la==LBRACE || _la==BASIC_CHAR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CharNoLBraceContext extends ParserRuleContext {
		public TerminalNode BASIC_CHAR() { return getToken(XQueryParserScripting.BASIC_CHAR, 0); }
		public TerminalNode GRAVE() { return getToken(XQueryParserScripting.GRAVE, 0); }
		public TerminalNode RBRACKET() { return getToken(XQueryParserScripting.RBRACKET, 0); }
		public CharNoLBraceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charNoLBrace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCharNoLBrace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCharNoLBrace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCharNoLBrace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharNoLBraceContext charNoLBrace() throws RecognitionException {
		CharNoLBraceContext _localctx = new CharNoLBraceContext(_ctx, getState());
		enterRule(_localctx, 410, RULE_charNoLBrace);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1995);
			_la = _input.LA(1);
			if ( !(_la==RBRACKET || _la==GRAVE || _la==BASIC_CHAR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CharNoRBrackContext extends ParserRuleContext {
		public TerminalNode BASIC_CHAR() { return getToken(XQueryParserScripting.BASIC_CHAR, 0); }
		public TerminalNode GRAVE() { return getToken(XQueryParserScripting.GRAVE, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public CharNoRBrackContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charNoRBrack; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCharNoRBrack(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCharNoRBrack(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCharNoRBrack(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharNoRBrackContext charNoRBrack() throws RecognitionException {
		CharNoRBrackContext _localctx = new CharNoRBrackContext(_ctx, getState());
		enterRule(_localctx, 412, RULE_charNoRBrack);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1997);
			_la = _input.LA(1);
			if ( !(_la==LBRACE || _la==GRAVE || _la==BASIC_CHAR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringConstructorCharsContext extends ParserRuleContext {
		public List<TerminalNode> BASIC_CHAR() { return getTokens(XQueryParserScripting.BASIC_CHAR); }
		public TerminalNode BASIC_CHAR(int i) {
			return getToken(XQueryParserScripting.BASIC_CHAR, i);
		}
		public List<CharNoGraveContext> charNoGrave() {
			return getRuleContexts(CharNoGraveContext.class);
		}
		public CharNoGraveContext charNoGrave(int i) {
			return getRuleContext(CharNoGraveContext.class,i);
		}
		public List<CharNoLBraceContext> charNoLBrace() {
			return getRuleContexts(CharNoLBraceContext.class);
		}
		public CharNoLBraceContext charNoLBrace(int i) {
			return getRuleContext(CharNoLBraceContext.class,i);
		}
		public List<CharNoRBrackContext> charNoRBrack() {
			return getRuleContexts(CharNoRBrackContext.class);
		}
		public CharNoRBrackContext charNoRBrack(int i) {
			return getRuleContext(CharNoRBrackContext.class,i);
		}
		public List<TerminalNode> LBRACE() { return getTokens(XQueryParserScripting.LBRACE); }
		public TerminalNode LBRACE(int i) {
			return getToken(XQueryParserScripting.LBRACE, i);
		}
		public StringConstructorCharsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConstructorChars; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringConstructorChars(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringConstructorChars(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringConstructorChars(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstructorCharsContext stringConstructorChars() throws RecognitionException {
		StringConstructorCharsContext _localctx = new StringConstructorCharsContext(_ctx, getState());
		enterRule(_localctx, 414, RULE_stringConstructorChars);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2011);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1125899957174272L) != 0) || _la==BASIC_CHAR) {
				{
				setState(2009);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,174,_ctx) ) {
				case 1:
					{
					setState(1999);
					match(BASIC_CHAR);
					}
					break;
				case 2:
					{
					setState(2000);
					charNoGrave();
					setState(2001);
					charNoLBrace();
					}
					break;
				case 3:
					{
					setState(2003);
					charNoRBrack();
					setState(2004);
					charNoGrave();
					setState(2005);
					charNoGrave();
					}
					break;
				case 4:
					{
					setState(2007);
					charNoGrave();
					}
					break;
				case 5:
					{
					setState(2008);
					match(LBRACE);
					}
					break;
				}
				}
				setState(2013);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringConstructorInterpolationContext extends ParserRuleContext {
		public TerminalNode ENTER_INTERPOLATION() { return getToken(XQueryParserScripting.ENTER_INTERPOLATION, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EXIT_INTERPOLATION() { return getToken(XQueryParserScripting.EXIT_INTERPOLATION, 0); }
		public StringConstructorInterpolationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConstructorInterpolation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringConstructorInterpolation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringConstructorInterpolation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringConstructorInterpolation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstructorInterpolationContext stringConstructorInterpolation() throws RecognitionException {
		StringConstructorInterpolationContext _localctx = new StringConstructorInterpolationContext(_ctx, getState());
		enterRule(_localctx, 416, RULE_stringConstructorInterpolation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2014);
			match(ENTER_INTERPOLATION);
			setState(2015);
			expr();
			setState(2016);
			match(EXIT_INTERPOLATION);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryLookupContext extends ParserRuleContext {
		public TerminalNode QUESTION() { return getToken(XQueryParserScripting.QUESTION, 0); }
		public KeySpecifierContext keySpecifier() {
			return getRuleContext(KeySpecifierContext.class,0);
		}
		public UnaryLookupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryLookup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterUnaryLookup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitUnaryLookup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitUnaryLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryLookupContext unaryLookup() throws RecognitionException {
		UnaryLookupContext _localctx = new UnaryLookupContext(_ctx, getState());
		enterRule(_localctx, 418, RULE_unaryLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2018);
			match(QUESTION);
			setState(2019);
			keySpecifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SingleTypeContext extends ParserRuleContext {
		public SimpleTypeNameContext item;
		public Token QUESTION;
		public List<Token> question = new ArrayList<Token>();
		public SimpleTypeNameContext simpleTypeName() {
			return getRuleContext(SimpleTypeNameContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(XQueryParserScripting.QUESTION, 0); }
		public SingleTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSingleType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSingleType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSingleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleTypeContext singleType() throws RecognitionException {
		SingleTypeContext _localctx = new SingleTypeContext(_ctx, getState());
		enterRule(_localctx, 420, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2021);
			((SingleTypeContext)_localctx).item = simpleTypeName();
			setState(2023);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,176,_ctx) ) {
			case 1:
				{
				setState(2022);
				((SingleTypeContext)_localctx).QUESTION = match(QUESTION);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).QUESTION);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeDeclarationContext extends ParserRuleContext {
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 422, RULE_typeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2025);
			match(KW_AS);
			setState(2026);
			sequenceType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SequenceTypeContext extends ParserRuleContext {
		public ItemTypeContext item;
		public Token QUESTION;
		public List<Token> question = new ArrayList<Token>();
		public Token STAR;
		public List<Token> star = new ArrayList<Token>();
		public Token PLUS;
		public List<Token> plus = new ArrayList<Token>();
		public TerminalNode KW_EMPTY_SEQUENCE() { return getToken(XQueryParserScripting.KW_EMPTY_SEQUENCE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(XQueryParserScripting.QUESTION, 0); }
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public TerminalNode PLUS() { return getToken(XQueryParserScripting.PLUS, 0); }
		public SequenceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSequenceType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSequenceType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSequenceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceTypeContext sequenceType() throws RecognitionException {
		SequenceTypeContext _localctx = new SequenceTypeContext(_ctx, getState());
		enterRule(_localctx, 424, RULE_sequenceType);
		try {
			setState(2037);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(2028);
				match(KW_EMPTY_SEQUENCE);
				setState(2029);
				match(LPAREN);
				setState(2030);
				match(RPAREN);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(2031);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(2035);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,177,_ctx) ) {
				case 1:
					{
					setState(2032);
					((SequenceTypeContext)_localctx).QUESTION = match(QUESTION);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).QUESTION);
					}
					break;
				case 2:
					{
					setState(2033);
					((SequenceTypeContext)_localctx).STAR = match(STAR);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).STAR);
					}
					break;
				case 3:
					{
					setState(2034);
					((SequenceTypeContext)_localctx).PLUS = match(PLUS);
					((SequenceTypeContext)_localctx).plus.add(((SequenceTypeContext)_localctx).PLUS);
					}
					break;
				}
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ItemTypeContext extends ParserRuleContext {
		public KindTestContext kindTest() {
			return getRuleContext(KindTestContext.class,0);
		}
		public TerminalNode KW_ITEM() { return getToken(XQueryParserScripting.KW_ITEM, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public FunctionTestContext functionTest() {
			return getRuleContext(FunctionTestContext.class,0);
		}
		public MapTestContext mapTest() {
			return getRuleContext(MapTestContext.class,0);
		}
		public ArrayTestContext arrayTest() {
			return getRuleContext(ArrayTestContext.class,0);
		}
		public AtomicOrUnionTypeContext atomicOrUnionType() {
			return getRuleContext(AtomicOrUnionTypeContext.class,0);
		}
		public ParenthesizedItemTestContext parenthesizedItemTest() {
			return getRuleContext(ParenthesizedItemTestContext.class,0);
		}
		public ItemTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_itemType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterItemType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitItemType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitItemType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ItemTypeContext itemType() throws RecognitionException {
		ItemTypeContext _localctx = new ItemTypeContext(_ctx, getState());
		enterRule(_localctx, 426, RULE_itemType);
		try {
			setState(2048);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,179,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2039);
				kindTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(2040);
				match(KW_ITEM);
				setState(2041);
				match(LPAREN);
				setState(2042);
				match(RPAREN);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2043);
				functionTest();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2044);
				mapTest();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2045);
				arrayTest();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2046);
				atomicOrUnionType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2047);
				parenthesizedItemTest();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AtomicOrUnionTypeContext extends ParserRuleContext {
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public AtomicOrUnionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomicOrUnionType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAtomicOrUnionType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAtomicOrUnionType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAtomicOrUnionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomicOrUnionTypeContext atomicOrUnionType() throws RecognitionException {
		AtomicOrUnionTypeContext _localctx = new AtomicOrUnionTypeContext(_ctx, getState());
		enterRule(_localctx, 428, RULE_atomicOrUnionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2050);
			eqName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KindTestContext extends ParserRuleContext {
		public DocumentTestContext documentTest() {
			return getRuleContext(DocumentTestContext.class,0);
		}
		public ElementTestContext elementTest() {
			return getRuleContext(ElementTestContext.class,0);
		}
		public AttributeTestContext attributeTest() {
			return getRuleContext(AttributeTestContext.class,0);
		}
		public SchemaElementTestContext schemaElementTest() {
			return getRuleContext(SchemaElementTestContext.class,0);
		}
		public SchemaAttributeTestContext schemaAttributeTest() {
			return getRuleContext(SchemaAttributeTestContext.class,0);
		}
		public PiTestContext piTest() {
			return getRuleContext(PiTestContext.class,0);
		}
		public CommentTestContext commentTest() {
			return getRuleContext(CommentTestContext.class,0);
		}
		public TextTestContext textTest() {
			return getRuleContext(TextTestContext.class,0);
		}
		public NamespaceNodeTestContext namespaceNodeTest() {
			return getRuleContext(NamespaceNodeTestContext.class,0);
		}
		public MlNodeTestContext mlNodeTest() {
			return getRuleContext(MlNodeTestContext.class,0);
		}
		public BinaryNodeTestContext binaryNodeTest() {
			return getRuleContext(BinaryNodeTestContext.class,0);
		}
		public AnyKindTestContext anyKindTest() {
			return getRuleContext(AnyKindTestContext.class,0);
		}
		public KindTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_kindTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterKindTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitKindTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitKindTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KindTestContext kindTest() throws RecognitionException {
		KindTestContext _localctx = new KindTestContext(_ctx, getState());
		enterRule(_localctx, 430, RULE_kindTest);
		try {
			setState(2064);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_DOCUMENT_NODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(2052);
				documentTest();
				}
				break;
			case KW_ELEMENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(2053);
				elementTest();
				}
				break;
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 3);
				{
				setState(2054);
				attributeTest();
				}
				break;
			case KW_SCHEMA_ELEM:
				enterOuterAlt(_localctx, 4);
				{
				setState(2055);
				schemaElementTest();
				}
				break;
			case KW_SCHEMA_ATTR:
				enterOuterAlt(_localctx, 5);
				{
				setState(2056);
				schemaAttributeTest();
				}
				break;
			case KW_PI:
				enterOuterAlt(_localctx, 6);
				{
				setState(2057);
				piTest();
				}
				break;
			case KW_COMMENT:
				enterOuterAlt(_localctx, 7);
				{
				setState(2058);
				commentTest();
				}
				break;
			case KW_TEXT:
				enterOuterAlt(_localctx, 8);
				{
				setState(2059);
				textTest();
				}
				break;
			case KW_NAMESPACE_NODE:
				enterOuterAlt(_localctx, 9);
				{
				setState(2060);
				namespaceNodeTest();
				}
				break;
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
				enterOuterAlt(_localctx, 10);
				{
				setState(2061);
				mlNodeTest();
				}
				break;
			case KW_BINARY:
				enterOuterAlt(_localctx, 11);
				{
				setState(2062);
				binaryNodeTest();
				}
				break;
			case KW_NODE:
				enterOuterAlt(_localctx, 12);
				{
				setState(2063);
				anyKindTest();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnyKindTestContext extends ParserRuleContext {
		public TerminalNode KW_NODE() { return getToken(XQueryParserScripting.KW_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public AnyKindTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyKindTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnyKindTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnyKindTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnyKindTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyKindTestContext anyKindTest() throws RecognitionException {
		AnyKindTestContext _localctx = new AnyKindTestContext(_ctx, getState());
		enterRule(_localctx, 432, RULE_anyKindTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2066);
			match(KW_NODE);
			setState(2067);
			match(LPAREN);
			setState(2069);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(2068);
				match(STAR);
				}
			}

			setState(2071);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BinaryNodeTestContext extends ParserRuleContext {
		public TerminalNode KW_BINARY() { return getToken(XQueryParserScripting.KW_BINARY, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public BinaryNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryNodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterBinaryNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitBinaryNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitBinaryNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryNodeTestContext binaryNodeTest() throws RecognitionException {
		BinaryNodeTestContext _localctx = new BinaryNodeTestContext(_ctx, getState());
		enterRule(_localctx, 434, RULE_binaryNodeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2073);
			match(KW_BINARY);
			setState(2074);
			match(LPAREN);
			setState(2075);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DocumentTestContext extends ParserRuleContext {
		public TerminalNode KW_DOCUMENT_NODE() { return getToken(XQueryParserScripting.KW_DOCUMENT_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public ElementTestContext elementTest() {
			return getRuleContext(ElementTestContext.class,0);
		}
		public SchemaElementTestContext schemaElementTest() {
			return getRuleContext(SchemaElementTestContext.class,0);
		}
		public DocumentTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_documentTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterDocumentTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitDocumentTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitDocumentTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentTestContext documentTest() throws RecognitionException {
		DocumentTestContext _localctx = new DocumentTestContext(_ctx, getState());
		enterRule(_localctx, 436, RULE_documentTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2077);
			match(KW_DOCUMENT_NODE);
			setState(2078);
			match(LPAREN);
			setState(2081);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ELEMENT:
				{
				setState(2079);
				elementTest();
				}
				break;
			case KW_SCHEMA_ELEM:
				{
				setState(2080);
				schemaElementTest();
				}
				break;
			case RPAREN:
				break;
			default:
				break;
			}
			setState(2083);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TextTestContext extends ParserRuleContext {
		public TerminalNode KW_TEXT() { return getToken(XQueryParserScripting.KW_TEXT, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TextTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTextTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTextTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTextTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextTestContext textTest() throws RecognitionException {
		TextTestContext _localctx = new TextTestContext(_ctx, getState());
		enterRule(_localctx, 438, RULE_textTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2085);
			match(KW_TEXT);
			setState(2086);
			match(LPAREN);
			setState(2087);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommentTestContext extends ParserRuleContext {
		public TerminalNode KW_COMMENT() { return getToken(XQueryParserScripting.KW_COMMENT, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public CommentTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commentTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterCommentTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitCommentTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitCommentTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentTestContext commentTest() throws RecognitionException {
		CommentTestContext _localctx = new CommentTestContext(_ctx, getState());
		enterRule(_localctx, 440, RULE_commentTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2089);
			match(KW_COMMENT);
			setState(2090);
			match(LPAREN);
			setState(2091);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceNodeTestContext extends ParserRuleContext {
		public TerminalNode KW_NAMESPACE_NODE() { return getToken(XQueryParserScripting.KW_NAMESPACE_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public NamespaceNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceNodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNamespaceNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNamespaceNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNamespaceNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceNodeTestContext namespaceNodeTest() throws RecognitionException {
		NamespaceNodeTestContext _localctx = new NamespaceNodeTestContext(_ctx, getState());
		enterRule(_localctx, 442, RULE_namespaceNodeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2093);
			match(KW_NAMESPACE_NODE);
			setState(2094);
			match(LPAREN);
			setState(2095);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PiTestContext extends ParserRuleContext {
		public TerminalNode KW_PI() { return getToken(XQueryParserScripting.KW_PI, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public PiTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_piTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterPiTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitPiTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitPiTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PiTestContext piTest() throws RecognitionException {
		PiTestContext _localctx = new PiTestContext(_ctx, getState());
		enterRule(_localctx, 444, RULE_piTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2097);
			match(KW_PI);
			setState(2098);
			match(LPAREN);
			setState(2101);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case NCName:
				{
				setState(2099);
				ncName();
				}
				break;
			case Quot:
			case Apos:
				{
				setState(2100);
				stringLiteral();
				}
				break;
			case RPAREN:
				break;
			default:
				break;
			}
			setState(2103);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeTestContext extends ParserRuleContext {
		public TypeNameContext type;
		public TerminalNode KW_ATTRIBUTE() { return getToken(XQueryParserScripting.KW_ATTRIBUTE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public AttributeNameOrWildcardContext attributeNameOrWildcard() {
			return getRuleContext(AttributeNameOrWildcardContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParserScripting.COMMA, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public AttributeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAttributeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAttributeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAttributeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeTestContext attributeTest() throws RecognitionException {
		AttributeTestContext _localctx = new AttributeTestContext(_ctx, getState());
		enterRule(_localctx, 446, RULE_attributeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2105);
			match(KW_ATTRIBUTE);
			setState(2106);
			match(LPAREN);
			setState(2112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & -35184371564543L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -1L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 360283640862605303L) != 0)) {
				{
				setState(2107);
				attributeNameOrWildcard();
				setState(2110);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(2108);
					match(COMMA);
					setState(2109);
					((AttributeTestContext)_localctx).type = typeName();
					}
				}

				}
			}

			setState(2114);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeNameOrWildcardContext extends ParserRuleContext {
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public AttributeNameOrWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeNameOrWildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAttributeNameOrWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAttributeNameOrWildcard(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAttributeNameOrWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeNameOrWildcardContext attributeNameOrWildcard() throws RecognitionException {
		AttributeNameOrWildcardContext _localctx = new AttributeNameOrWildcardContext(_ctx, getState());
		enterRule(_localctx, 448, RULE_attributeNameOrWildcard);
		try {
			setState(2118);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case URIQualifiedName:
			case FullQName:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2116);
				attributeName();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(2117);
				match(STAR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SchemaAttributeTestContext extends ParserRuleContext {
		public TerminalNode KW_SCHEMA_ATTR() { return getToken(XQueryParserScripting.KW_SCHEMA_ATTR, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public AttributeDeclarationContext attributeDeclaration() {
			return getRuleContext(AttributeDeclarationContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public SchemaAttributeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaAttributeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSchemaAttributeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSchemaAttributeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSchemaAttributeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaAttributeTestContext schemaAttributeTest() throws RecognitionException {
		SchemaAttributeTestContext _localctx = new SchemaAttributeTestContext(_ctx, getState());
		enterRule(_localctx, 450, RULE_schemaAttributeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2120);
			match(KW_SCHEMA_ATTR);
			setState(2121);
			match(LPAREN);
			setState(2122);
			attributeDeclaration();
			setState(2123);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementTestContext extends ParserRuleContext {
		public Token optional;
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParserScripting.KW_ELEMENT, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public ElementNameOrWildcardContext elementNameOrWildcard() {
			return getRuleContext(ElementNameOrWildcardContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParserScripting.COMMA, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(XQueryParserScripting.QUESTION, 0); }
		public ElementTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterElementTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitElementTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitElementTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementTestContext elementTest() throws RecognitionException {
		ElementTestContext _localctx = new ElementTestContext(_ctx, getState());
		enterRule(_localctx, 452, RULE_elementTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2125);
			match(KW_ELEMENT);
			setState(2126);
			match(LPAREN);
			setState(2135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & -35184371564543L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -1L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 360283640862605303L) != 0)) {
				{
				setState(2127);
				elementNameOrWildcard();
				setState(2133);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(2128);
					match(COMMA);
					setState(2129);
					typeName();
					setState(2131);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==QUESTION) {
						{
						setState(2130);
						((ElementTestContext)_localctx).optional = match(QUESTION);
						}
					}

					}
				}

				}
			}

			setState(2137);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementNameOrWildcardContext extends ParserRuleContext {
		public ElementNameContext elementName() {
			return getRuleContext(ElementNameContext.class,0);
		}
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public ElementNameOrWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementNameOrWildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterElementNameOrWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitElementNameOrWildcard(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitElementNameOrWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementNameOrWildcardContext elementNameOrWildcard() throws RecognitionException {
		ElementNameOrWildcardContext _localctx = new ElementNameOrWildcardContext(_ctx, getState());
		enterRule(_localctx, 454, RULE_elementNameOrWildcard);
		try {
			setState(2141);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case URIQualifiedName:
			case FullQName:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2139);
				elementName();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(2140);
				match(STAR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SchemaElementTestContext extends ParserRuleContext {
		public TerminalNode KW_SCHEMA_ELEM() { return getToken(XQueryParserScripting.KW_SCHEMA_ELEM, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public ElementDeclarationContext elementDeclaration() {
			return getRuleContext(ElementDeclarationContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public SchemaElementTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaElementTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSchemaElementTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSchemaElementTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSchemaElementTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaElementTestContext schemaElementTest() throws RecognitionException {
		SchemaElementTestContext _localctx = new SchemaElementTestContext(_ctx, getState());
		enterRule(_localctx, 456, RULE_schemaElementTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2143);
			match(KW_SCHEMA_ELEM);
			setState(2144);
			match(LPAREN);
			setState(2145);
			elementDeclaration();
			setState(2146);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementDeclarationContext extends ParserRuleContext {
		public ElementNameContext elementName() {
			return getRuleContext(ElementNameContext.class,0);
		}
		public ElementDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterElementDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitElementDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitElementDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementDeclarationContext elementDeclaration() throws RecognitionException {
		ElementDeclarationContext _localctx = new ElementDeclarationContext(_ctx, getState());
		enterRule(_localctx, 458, RULE_elementDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2148);
			elementName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeNameContext extends ParserRuleContext {
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAttributeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAttributeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAttributeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 460, RULE_attributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2150);
			eqName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementNameContext extends ParserRuleContext {
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public ElementNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterElementName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitElementName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitElementName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementNameContext elementName() throws RecognitionException {
		ElementNameContext _localctx = new ElementNameContext(_ctx, getState());
		enterRule(_localctx, 462, RULE_elementName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2152);
			eqName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleTypeNameContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public SimpleTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterSimpleTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitSimpleTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitSimpleTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTypeNameContext simpleTypeName() throws RecognitionException {
		SimpleTypeNameContext _localctx = new SimpleTypeNameContext(_ctx, getState());
		enterRule(_localctx, 464, RULE_simpleTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2154);
			typeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeNameContext extends ParserRuleContext {
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 466, RULE_typeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2156);
			eqName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionTestContext extends ParserRuleContext {
		public AnyFunctionTestContext anyFunctionTest() {
			return getRuleContext(AnyFunctionTestContext.class,0);
		}
		public TypedFunctionTestContext typedFunctionTest() {
			return getRuleContext(TypedFunctionTestContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public FunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionTestContext functionTest() throws RecognitionException {
		FunctionTestContext _localctx = new FunctionTestContext(_ctx, getState());
		enterRule(_localctx, 468, RULE_functionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==MOD) {
				{
				{
				setState(2158);
				annotation();
				}
				}
				setState(2163);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,192,_ctx) ) {
			case 1:
				{
				setState(2164);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(2165);
				typedFunctionTest();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnyFunctionTestContext extends ParserRuleContext {
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParserScripting.KW_FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public AnyFunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyFunctionTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnyFunctionTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnyFunctionTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnyFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyFunctionTestContext anyFunctionTest() throws RecognitionException {
		AnyFunctionTestContext _localctx = new AnyFunctionTestContext(_ctx, getState());
		enterRule(_localctx, 470, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2168);
			match(KW_FUNCTION);
			setState(2169);
			match(LPAREN);
			setState(2170);
			match(STAR);
			setState(2171);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypedFunctionTestContext extends ParserRuleContext {
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParserScripting.KW_FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public List<SequenceTypeContext> sequenceType() {
			return getRuleContexts(SequenceTypeContext.class);
		}
		public SequenceTypeContext sequenceType(int i) {
			return getRuleContext(SequenceTypeContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public TypedFunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedFunctionTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTypedFunctionTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTypedFunctionTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTypedFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedFunctionTestContext typedFunctionTest() throws RecognitionException {
		TypedFunctionTestContext _localctx = new TypedFunctionTestContext(_ctx, getState());
		enterRule(_localctx, 472, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2173);
			match(KW_FUNCTION);
			setState(2174);
			match(LPAREN);
			setState(2183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 8)) & ~0x3f) == 0 && ((1L << (_la - 8)) & -35046933127167L) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & -1L) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & 360283640862605303L) != 0)) {
				{
				setState(2175);
				sequenceType();
				setState(2180);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2176);
					match(COMMA);
					setState(2177);
					sequenceType();
					}
					}
					setState(2182);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2185);
			match(RPAREN);
			setState(2186);
			match(KW_AS);
			setState(2187);
			sequenceType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapTestContext extends ParserRuleContext {
		public AnyMapTestContext anyMapTest() {
			return getRuleContext(AnyMapTestContext.class,0);
		}
		public TypedMapTestContext typedMapTest() {
			return getRuleContext(TypedMapTestContext.class,0);
		}
		public MapTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMapTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMapTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTestContext mapTest() throws RecognitionException {
		MapTestContext _localctx = new MapTestContext(_ctx, getState());
		enterRule(_localctx, 474, RULE_mapTest);
		try {
			setState(2191);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,195,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2189);
				anyMapTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2190);
				typedMapTest();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnyMapTestContext extends ParserRuleContext {
		public TerminalNode KW_MAP() { return getToken(XQueryParserScripting.KW_MAP, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public AnyMapTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyMapTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnyMapTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnyMapTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnyMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyMapTestContext anyMapTest() throws RecognitionException {
		AnyMapTestContext _localctx = new AnyMapTestContext(_ctx, getState());
		enterRule(_localctx, 476, RULE_anyMapTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2193);
			match(KW_MAP);
			setState(2194);
			match(LPAREN);
			setState(2195);
			match(STAR);
			setState(2196);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypedMapTestContext extends ParserRuleContext {
		public TerminalNode KW_MAP() { return getToken(XQueryParserScripting.KW_MAP, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParserScripting.COMMA, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TypedMapTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedMapTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTypedMapTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTypedMapTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTypedMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedMapTestContext typedMapTest() throws RecognitionException {
		TypedMapTestContext _localctx = new TypedMapTestContext(_ctx, getState());
		enterRule(_localctx, 478, RULE_typedMapTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2198);
			match(KW_MAP);
			setState(2199);
			match(LPAREN);
			setState(2200);
			eqName();
			setState(2201);
			match(COMMA);
			setState(2202);
			sequenceType();
			setState(2203);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayTestContext extends ParserRuleContext {
		public AnyArrayTestContext anyArrayTest() {
			return getRuleContext(AnyArrayTestContext.class,0);
		}
		public TypedArrayTestContext typedArrayTest() {
			return getRuleContext(TypedArrayTestContext.class,0);
		}
		public ArrayTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterArrayTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitArrayTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTestContext arrayTest() throws RecognitionException {
		ArrayTestContext _localctx = new ArrayTestContext(_ctx, getState());
		enterRule(_localctx, 480, RULE_arrayTest);
		try {
			setState(2207);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,196,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2205);
				anyArrayTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2206);
				typedArrayTest();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnyArrayTestContext extends ParserRuleContext {
		public TerminalNode KW_ARRAY() { return getToken(XQueryParserScripting.KW_ARRAY, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(XQueryParserScripting.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public AnyArrayTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyArrayTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAnyArrayTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAnyArrayTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAnyArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyArrayTestContext anyArrayTest() throws RecognitionException {
		AnyArrayTestContext _localctx = new AnyArrayTestContext(_ctx, getState());
		enterRule(_localctx, 482, RULE_anyArrayTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2209);
			match(KW_ARRAY);
			setState(2210);
			match(LPAREN);
			setState(2211);
			match(STAR);
			setState(2212);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypedArrayTestContext extends ParserRuleContext {
		public TerminalNode KW_ARRAY() { return getToken(XQueryParserScripting.KW_ARRAY, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public TypedArrayTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedArrayTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterTypedArrayTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitTypedArrayTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitTypedArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedArrayTestContext typedArrayTest() throws RecognitionException {
		TypedArrayTestContext _localctx = new TypedArrayTestContext(_ctx, getState());
		enterRule(_localctx, 484, RULE_typedArrayTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2214);
			match(KW_ARRAY);
			setState(2215);
			match(LPAREN);
			setState(2216);
			sequenceType();
			setState(2217);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParenthesizedItemTestContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public ParenthesizedItemTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenthesizedItemTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterParenthesizedItemTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitParenthesizedItemTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitParenthesizedItemTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenthesizedItemTestContext parenthesizedItemTest() throws RecognitionException {
		ParenthesizedItemTestContext _localctx = new ParenthesizedItemTestContext(_ctx, getState());
		enterRule(_localctx, 486, RULE_parenthesizedItemTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2219);
			match(LPAREN);
			setState(2220);
			itemType();
			setState(2221);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeDeclarationContext extends ParserRuleContext {
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public AttributeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterAttributeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitAttributeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitAttributeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeDeclarationContext attributeDeclaration() throws RecognitionException {
		AttributeDeclarationContext _localctx = new AttributeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 488, RULE_attributeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2223);
			attributeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MlNodeTestContext extends ParserRuleContext {
		public MlArrayNodeTestContext mlArrayNodeTest() {
			return getRuleContext(MlArrayNodeTestContext.class,0);
		}
		public MlObjectNodeTestContext mlObjectNodeTest() {
			return getRuleContext(MlObjectNodeTestContext.class,0);
		}
		public MlNumberNodeTestContext mlNumberNodeTest() {
			return getRuleContext(MlNumberNodeTestContext.class,0);
		}
		public MlBooleanNodeTestContext mlBooleanNodeTest() {
			return getRuleContext(MlBooleanNodeTestContext.class,0);
		}
		public MlNullNodeTestContext mlNullNodeTest() {
			return getRuleContext(MlNullNodeTestContext.class,0);
		}
		public MlNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlNodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMlNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMlNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMlNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNodeTestContext mlNodeTest() throws RecognitionException {
		MlNodeTestContext _localctx = new MlNodeTestContext(_ctx, getState());
		enterRule(_localctx, 490, RULE_mlNodeTest);
		try {
			setState(2230);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ARRAY_NODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(2225);
				mlArrayNodeTest();
				}
				break;
			case KW_OBJECT_NODE:
				enterOuterAlt(_localctx, 2);
				{
				setState(2226);
				mlObjectNodeTest();
				}
				break;
			case KW_NUMBER_NODE:
				enterOuterAlt(_localctx, 3);
				{
				setState(2227);
				mlNumberNodeTest();
				}
				break;
			case KW_BOOLEAN_NODE:
				enterOuterAlt(_localctx, 4);
				{
				setState(2228);
				mlBooleanNodeTest();
				}
				break;
			case KW_NULL_NODE:
				enterOuterAlt(_localctx, 5);
				{
				setState(2229);
				mlNullNodeTest();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MlArrayNodeTestContext extends ParserRuleContext {
		public TerminalNode KW_ARRAY_NODE() { return getToken(XQueryParserScripting.KW_ARRAY_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlArrayNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlArrayNodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMlArrayNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMlArrayNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMlArrayNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlArrayNodeTestContext mlArrayNodeTest() throws RecognitionException {
		MlArrayNodeTestContext _localctx = new MlArrayNodeTestContext(_ctx, getState());
		enterRule(_localctx, 492, RULE_mlArrayNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2232);
			match(KW_ARRAY_NODE);
			setState(2233);
			match(LPAREN);
			setState(2235);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2234);
				stringLiteral();
				}
			}

			setState(2237);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MlObjectNodeTestContext extends ParserRuleContext {
		public TerminalNode KW_OBJECT_NODE() { return getToken(XQueryParserScripting.KW_OBJECT_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlObjectNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlObjectNodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMlObjectNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMlObjectNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMlObjectNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlObjectNodeTestContext mlObjectNodeTest() throws RecognitionException {
		MlObjectNodeTestContext _localctx = new MlObjectNodeTestContext(_ctx, getState());
		enterRule(_localctx, 494, RULE_mlObjectNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2239);
			match(KW_OBJECT_NODE);
			setState(2240);
			match(LPAREN);
			setState(2242);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2241);
				stringLiteral();
				}
			}

			setState(2244);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MlNumberNodeTestContext extends ParserRuleContext {
		public TerminalNode KW_NUMBER_NODE() { return getToken(XQueryParserScripting.KW_NUMBER_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlNumberNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlNumberNodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMlNumberNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMlNumberNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMlNumberNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNumberNodeTestContext mlNumberNodeTest() throws RecognitionException {
		MlNumberNodeTestContext _localctx = new MlNumberNodeTestContext(_ctx, getState());
		enterRule(_localctx, 496, RULE_mlNumberNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2246);
			match(KW_NUMBER_NODE);
			setState(2247);
			match(LPAREN);
			setState(2249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2248);
				stringLiteral();
				}
			}

			setState(2251);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MlBooleanNodeTestContext extends ParserRuleContext {
		public TerminalNode KW_BOOLEAN_NODE() { return getToken(XQueryParserScripting.KW_BOOLEAN_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlBooleanNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlBooleanNodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMlBooleanNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMlBooleanNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMlBooleanNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlBooleanNodeTestContext mlBooleanNodeTest() throws RecognitionException {
		MlBooleanNodeTestContext _localctx = new MlBooleanNodeTestContext(_ctx, getState());
		enterRule(_localctx, 498, RULE_mlBooleanNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2253);
			match(KW_BOOLEAN_NODE);
			setState(2254);
			match(LPAREN);
			setState(2256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2255);
				stringLiteral();
				}
			}

			setState(2258);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MlNullNodeTestContext extends ParserRuleContext {
		public TerminalNode KW_NULL_NODE() { return getToken(XQueryParserScripting.KW_NULL_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParserScripting.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParserScripting.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlNullNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlNullNodeTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterMlNullNodeTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitMlNullNodeTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitMlNullNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNullNodeTestContext mlNullNodeTest() throws RecognitionException {
		MlNullNodeTestContext _localctx = new MlNullNodeTestContext(_ctx, getState());
		enterRule(_localctx, 500, RULE_mlNullNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2260);
			match(KW_NULL_NODE);
			setState(2261);
			match(LPAREN);
			setState(2263);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2262);
				stringLiteral();
				}
			}

			setState(2265);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EqNameContext extends ParserRuleContext {
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public TerminalNode URIQualifiedName() { return getToken(XQueryParserScripting.URIQualifiedName, 0); }
		public EqNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eqName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterEqName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitEqName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitEqName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqNameContext eqName() throws RecognitionException {
		EqNameContext _localctx = new EqNameContext(_ctx, getState());
		enterRule(_localctx, 502, RULE_eqName);
		try {
			setState(2269);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case FullQName:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2267);
				qName();
				}
				break;
			case URIQualifiedName:
				enterOuterAlt(_localctx, 2);
				{
				setState(2268);
				match(URIQualifiedName);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QNameContext extends ParserRuleContext {
		public TerminalNode FullQName() { return getToken(XQueryParserScripting.FullQName, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public QNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterQName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitQName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitQName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QNameContext qName() throws RecognitionException {
		QNameContext _localctx = new QNameContext(_ctx, getState());
		enterRule(_localctx, 504, RULE_qName);
		try {
			setState(2273);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FullQName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2271);
				match(FullQName);
				}
				break;
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
			case NCName:
				enterOuterAlt(_localctx, 2);
				{
				setState(2272);
				ncName();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NcNameContext extends ParserRuleContext {
		public Token local_name;
		public KeywordContext local_namekw;
		public TerminalNode NCName() { return getToken(XQueryParserScripting.NCName, 0); }
		public KeywordContext keyword() {
			return getRuleContext(KeywordContext.class,0);
		}
		public NcNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ncName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNcName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNcName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNcName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NcNameContext ncName() throws RecognitionException {
		NcNameContext _localctx = new NcNameContext(_ctx, getState());
		enterRule(_localctx, 506, RULE_ncName);
		try {
			setState(2277);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2275);
				((NcNameContext)_localctx).local_name = match(NCName);
				}
				break;
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_ARRAY:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_ATTRIBUTE:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BINARY:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CATCH:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_COMMENT:
			case KW_CONSTRUCTION:
			case KW_CONTEXT:
			case KW_COPY_NS:
			case KW_COUNT:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DECIMAL_FORMAT:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_EMPTY_SEQUENCE:
			case KW_ENCODING:
			case KW_END:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IF:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_ITEM:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MAP:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_NODE:
			case KW_OF:
			case KW_ONLY:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_PI:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SELF:
			case KW_SLIDING:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_UPDATE:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHEN:
			case KW_WHERE:
			case KW_WINDOW:
			case KW_XQUERY:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(2276);
				((NcNameContext)_localctx).local_namekw = keyword();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionNameContext extends ParserRuleContext {
		public TerminalNode FullQName() { return getToken(XQueryParserScripting.FullQName, 0); }
		public TerminalNode NCName() { return getToken(XQueryParserScripting.NCName, 0); }
		public TerminalNode URIQualifiedName() { return getToken(XQueryParserScripting.URIQualifiedName, 0); }
		public KeywordOKForFunctionContext keywordOKForFunction() {
			return getRuleContext(KeywordOKForFunctionContext.class,0);
		}
		public FunctionNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterFunctionName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitFunctionName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitFunctionName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionNameContext functionName() throws RecognitionException {
		FunctionNameContext _localctx = new FunctionNameContext(_ctx, getState());
		enterRule(_localctx, 508, RULE_functionName);
		try {
			setState(2283);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FullQName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2279);
				match(FullQName);
				}
				break;
			case NCName:
				enterOuterAlt(_localctx, 2);
				{
				setState(2280);
				match(NCName);
				}
				break;
			case URIQualifiedName:
				enterOuterAlt(_localctx, 3);
				{
				setState(2281);
				match(URIQualifiedName);
				}
				break;
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_CONSTRUCTION:
			case KW_COPY_NS:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_ENCODING:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_OF:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SELF:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHERE:
			case KW_XQUERY:
				enterOuterAlt(_localctx, 4);
				{
				setState(2282);
				keywordOKForFunction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeywordContext extends ParserRuleContext {
		public KeywordOKForFunctionContext keywordOKForFunction() {
			return getRuleContext(KeywordOKForFunctionContext.class,0);
		}
		public KeywordNotOKForFunctionContext keywordNotOKForFunction() {
			return getRuleContext(KeywordNotOKForFunctionContext.class,0);
		}
		public KeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitKeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 510, RULE_keyword);
		try {
			setState(2287);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_AND:
			case KW_AS:
			case KW_ASCENDING:
			case KW_AT:
			case KW_BASE_URI:
			case KW_BOUNDARY_SPACE:
			case KW_BY:
			case KW_CASE:
			case KW_CAST:
			case KW_CASTABLE:
			case KW_CHILD:
			case KW_COLLATION:
			case KW_CONSTRUCTION:
			case KW_COPY_NS:
			case KW_DECLARE:
			case KW_DEFAULT:
			case KW_DESCENDANT:
			case KW_DESCENDANT_OR_SELF:
			case KW_DESCENDING:
			case KW_DIV:
			case KW_DOCUMENT:
			case KW_ELSE:
			case KW_EMPTY:
			case KW_ENCODING:
			case KW_EQ:
			case KW_EVERY:
			case KW_EXCEPT:
			case KW_EXTERNAL:
			case KW_FOLLOWING:
			case KW_FOLLOWING_SIBLING:
			case KW_FOR:
			case KW_FUNCTION:
			case KW_GE:
			case KW_GREATEST:
			case KW_GROUP:
			case KW_GT:
			case KW_IDIV:
			case KW_IMPORT:
			case KW_IN:
			case KW_INHERIT:
			case KW_INSTANCE:
			case KW_INTERSECT:
			case KW_IS:
			case KW_LAX:
			case KW_LE:
			case KW_LEAST:
			case KW_LET:
			case KW_LT:
			case KW_MOD:
			case KW_MODULE:
			case KW_NAMESPACE:
			case KW_NE:
			case KW_NO_INHERIT:
			case KW_NO_PRESERVE:
			case KW_OF:
			case KW_OPTION:
			case KW_OR:
			case KW_ORDER:
			case KW_ORDERED:
			case KW_ORDERING:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
			case KW_PRESERVE:
			case KW_RETURN:
			case KW_SATISFIES:
			case KW_SCHEMA:
			case KW_SELF:
			case KW_SOME:
			case KW_STABLE:
			case KW_START:
			case KW_STRICT:
			case KW_STRIP:
			case KW_THEN:
			case KW_TO:
			case KW_TREAT:
			case KW_UNION:
			case KW_UNORDERED:
			case KW_VALIDATE:
			case KW_VARIABLE:
			case KW_VERSION:
			case KW_WHERE:
			case KW_XQUERY:
				enterOuterAlt(_localctx, 1);
				{
				setState(2285);
				keywordOKForFunction();
				}
				break;
			case DFPropertyName:
			case KW_ALLOWING:
			case KW_ARRAY:
			case KW_ATTRIBUTE:
			case KW_BINARY:
			case KW_CATCH:
			case KW_COMMENT:
			case KW_CONTEXT:
			case KW_COUNT:
			case KW_DECIMAL_FORMAT:
			case KW_DOCUMENT_NODE:
			case KW_ELEMENT:
			case KW_EMPTY_SEQUENCE:
			case KW_END:
			case KW_IF:
			case KW_ITEM:
			case KW_MAP:
			case KW_NEXT:
			case KW_NAMESPACE_NODE:
			case KW_NODE:
			case KW_ONLY:
			case KW_PI:
			case KW_SCHEMA_ATTR:
			case KW_SCHEMA_ELEM:
			case KW_SLIDING:
			case KW_SWITCH:
			case KW_TEXT:
			case KW_TRY:
			case KW_TUMBLING:
			case KW_TYPE:
			case KW_TYPESWITCH:
			case KW_UPDATE:
			case KW_WHEN:
			case KW_WINDOW:
			case KW_ARRAY_NODE:
			case KW_BOOLEAN_NODE:
			case KW_NULL_NODE:
			case KW_NUMBER_NODE:
			case KW_OBJECT_NODE:
			case KW_REPLACE:
			case KW_WITH:
			case KW_VALUE:
			case KW_INSERT:
			case KW_INTO:
			case KW_DELETE:
			case KW_RENAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(2286);
				keywordNotOKForFunction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeywordNotOKForFunctionContext extends ParserRuleContext {
		public TerminalNode KW_ATTRIBUTE() { return getToken(XQueryParserScripting.KW_ATTRIBUTE, 0); }
		public TerminalNode KW_COMMENT() { return getToken(XQueryParserScripting.KW_COMMENT, 0); }
		public TerminalNode KW_DOCUMENT_NODE() { return getToken(XQueryParserScripting.KW_DOCUMENT_NODE, 0); }
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParserScripting.KW_ELEMENT, 0); }
		public TerminalNode KW_EMPTY_SEQUENCE() { return getToken(XQueryParserScripting.KW_EMPTY_SEQUENCE, 0); }
		public TerminalNode KW_IF() { return getToken(XQueryParserScripting.KW_IF, 0); }
		public TerminalNode KW_ITEM() { return getToken(XQueryParserScripting.KW_ITEM, 0); }
		public TerminalNode KW_CONTEXT() { return getToken(XQueryParserScripting.KW_CONTEXT, 0); }
		public TerminalNode KW_NODE() { return getToken(XQueryParserScripting.KW_NODE, 0); }
		public TerminalNode KW_PI() { return getToken(XQueryParserScripting.KW_PI, 0); }
		public TerminalNode KW_SCHEMA_ATTR() { return getToken(XQueryParserScripting.KW_SCHEMA_ATTR, 0); }
		public TerminalNode KW_SCHEMA_ELEM() { return getToken(XQueryParserScripting.KW_SCHEMA_ELEM, 0); }
		public TerminalNode KW_BINARY() { return getToken(XQueryParserScripting.KW_BINARY, 0); }
		public TerminalNode KW_TEXT() { return getToken(XQueryParserScripting.KW_TEXT, 0); }
		public TerminalNode KW_TYPESWITCH() { return getToken(XQueryParserScripting.KW_TYPESWITCH, 0); }
		public TerminalNode KW_SWITCH() { return getToken(XQueryParserScripting.KW_SWITCH, 0); }
		public TerminalNode KW_NAMESPACE_NODE() { return getToken(XQueryParserScripting.KW_NAMESPACE_NODE, 0); }
		public TerminalNode KW_TYPE() { return getToken(XQueryParserScripting.KW_TYPE, 0); }
		public TerminalNode KW_TUMBLING() { return getToken(XQueryParserScripting.KW_TUMBLING, 0); }
		public TerminalNode KW_TRY() { return getToken(XQueryParserScripting.KW_TRY, 0); }
		public TerminalNode KW_CATCH() { return getToken(XQueryParserScripting.KW_CATCH, 0); }
		public TerminalNode KW_ONLY() { return getToken(XQueryParserScripting.KW_ONLY, 0); }
		public TerminalNode KW_WHEN() { return getToken(XQueryParserScripting.KW_WHEN, 0); }
		public TerminalNode KW_SLIDING() { return getToken(XQueryParserScripting.KW_SLIDING, 0); }
		public TerminalNode KW_DECIMAL_FORMAT() { return getToken(XQueryParserScripting.KW_DECIMAL_FORMAT, 0); }
		public TerminalNode KW_WINDOW() { return getToken(XQueryParserScripting.KW_WINDOW, 0); }
		public TerminalNode KW_COUNT() { return getToken(XQueryParserScripting.KW_COUNT, 0); }
		public TerminalNode KW_MAP() { return getToken(XQueryParserScripting.KW_MAP, 0); }
		public TerminalNode KW_END() { return getToken(XQueryParserScripting.KW_END, 0); }
		public TerminalNode KW_ALLOWING() { return getToken(XQueryParserScripting.KW_ALLOWING, 0); }
		public TerminalNode KW_ARRAY() { return getToken(XQueryParserScripting.KW_ARRAY, 0); }
		public TerminalNode DFPropertyName() { return getToken(XQueryParserScripting.DFPropertyName, 0); }
		public TerminalNode KW_ARRAY_NODE() { return getToken(XQueryParserScripting.KW_ARRAY_NODE, 0); }
		public TerminalNode KW_BOOLEAN_NODE() { return getToken(XQueryParserScripting.KW_BOOLEAN_NODE, 0); }
		public TerminalNode KW_NULL_NODE() { return getToken(XQueryParserScripting.KW_NULL_NODE, 0); }
		public TerminalNode KW_NUMBER_NODE() { return getToken(XQueryParserScripting.KW_NUMBER_NODE, 0); }
		public TerminalNode KW_OBJECT_NODE() { return getToken(XQueryParserScripting.KW_OBJECT_NODE, 0); }
		public TerminalNode KW_UPDATE() { return getToken(XQueryParserScripting.KW_UPDATE, 0); }
		public TerminalNode KW_REPLACE() { return getToken(XQueryParserScripting.KW_REPLACE, 0); }
		public TerminalNode KW_WITH() { return getToken(XQueryParserScripting.KW_WITH, 0); }
		public TerminalNode KW_VALUE() { return getToken(XQueryParserScripting.KW_VALUE, 0); }
		public TerminalNode KW_INSERT() { return getToken(XQueryParserScripting.KW_INSERT, 0); }
		public TerminalNode KW_INTO() { return getToken(XQueryParserScripting.KW_INTO, 0); }
		public TerminalNode KW_DELETE() { return getToken(XQueryParserScripting.KW_DELETE, 0); }
		public TerminalNode KW_NEXT() { return getToken(XQueryParserScripting.KW_NEXT, 0); }
		public TerminalNode KW_RENAME() { return getToken(XQueryParserScripting.KW_RENAME, 0); }
		public KeywordNotOKForFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keywordNotOKForFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterKeywordNotOKForFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitKeywordNotOKForFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitKeywordNotOKForFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordNotOKForFunctionContext keywordNotOKForFunction() throws RecognitionException {
		KeywordNotOKForFunctionContext _localctx = new KeywordNotOKForFunctionContext(_ctx, getState());
		enterRule(_localctx, 512, RULE_keywordNotOKForFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2289);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2458965396544291072L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -7475691707260725983L) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 2305282850502838273L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class KeywordOKForFunctionContext extends ParserRuleContext {
		public TerminalNode KW_ANCESTOR() { return getToken(XQueryParserScripting.KW_ANCESTOR, 0); }
		public TerminalNode KW_ANCESTOR_OR_SELF() { return getToken(XQueryParserScripting.KW_ANCESTOR_OR_SELF, 0); }
		public TerminalNode KW_AND() { return getToken(XQueryParserScripting.KW_AND, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParserScripting.KW_AS, 0); }
		public TerminalNode KW_ASCENDING() { return getToken(XQueryParserScripting.KW_ASCENDING, 0); }
		public TerminalNode KW_AT() { return getToken(XQueryParserScripting.KW_AT, 0); }
		public TerminalNode KW_BASE_URI() { return getToken(XQueryParserScripting.KW_BASE_URI, 0); }
		public TerminalNode KW_BOUNDARY_SPACE() { return getToken(XQueryParserScripting.KW_BOUNDARY_SPACE, 0); }
		public TerminalNode KW_BY() { return getToken(XQueryParserScripting.KW_BY, 0); }
		public TerminalNode KW_CASE() { return getToken(XQueryParserScripting.KW_CASE, 0); }
		public TerminalNode KW_CAST() { return getToken(XQueryParserScripting.KW_CAST, 0); }
		public TerminalNode KW_CASTABLE() { return getToken(XQueryParserScripting.KW_CASTABLE, 0); }
		public TerminalNode KW_CHILD() { return getToken(XQueryParserScripting.KW_CHILD, 0); }
		public TerminalNode KW_COLLATION() { return getToken(XQueryParserScripting.KW_COLLATION, 0); }
		public TerminalNode KW_CONSTRUCTION() { return getToken(XQueryParserScripting.KW_CONSTRUCTION, 0); }
		public TerminalNode KW_COPY_NS() { return getToken(XQueryParserScripting.KW_COPY_NS, 0); }
		public TerminalNode KW_DECLARE() { return getToken(XQueryParserScripting.KW_DECLARE, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParserScripting.KW_DEFAULT, 0); }
		public TerminalNode KW_DESCENDANT() { return getToken(XQueryParserScripting.KW_DESCENDANT, 0); }
		public TerminalNode KW_DESCENDANT_OR_SELF() { return getToken(XQueryParserScripting.KW_DESCENDANT_OR_SELF, 0); }
		public TerminalNode KW_DESCENDING() { return getToken(XQueryParserScripting.KW_DESCENDING, 0); }
		public TerminalNode KW_DIV() { return getToken(XQueryParserScripting.KW_DIV, 0); }
		public TerminalNode KW_DOCUMENT() { return getToken(XQueryParserScripting.KW_DOCUMENT, 0); }
		public TerminalNode KW_ELSE() { return getToken(XQueryParserScripting.KW_ELSE, 0); }
		public TerminalNode KW_EMPTY() { return getToken(XQueryParserScripting.KW_EMPTY, 0); }
		public TerminalNode KW_ENCODING() { return getToken(XQueryParserScripting.KW_ENCODING, 0); }
		public TerminalNode KW_EQ() { return getToken(XQueryParserScripting.KW_EQ, 0); }
		public TerminalNode KW_EVERY() { return getToken(XQueryParserScripting.KW_EVERY, 0); }
		public TerminalNode KW_EXCEPT() { return getToken(XQueryParserScripting.KW_EXCEPT, 0); }
		public TerminalNode KW_EXTERNAL() { return getToken(XQueryParserScripting.KW_EXTERNAL, 0); }
		public TerminalNode KW_FOLLOWING() { return getToken(XQueryParserScripting.KW_FOLLOWING, 0); }
		public TerminalNode KW_FOLLOWING_SIBLING() { return getToken(XQueryParserScripting.KW_FOLLOWING_SIBLING, 0); }
		public TerminalNode KW_FOR() { return getToken(XQueryParserScripting.KW_FOR, 0); }
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParserScripting.KW_FUNCTION, 0); }
		public TerminalNode KW_GE() { return getToken(XQueryParserScripting.KW_GE, 0); }
		public TerminalNode KW_GREATEST() { return getToken(XQueryParserScripting.KW_GREATEST, 0); }
		public TerminalNode KW_GROUP() { return getToken(XQueryParserScripting.KW_GROUP, 0); }
		public TerminalNode KW_GT() { return getToken(XQueryParserScripting.KW_GT, 0); }
		public TerminalNode KW_IDIV() { return getToken(XQueryParserScripting.KW_IDIV, 0); }
		public TerminalNode KW_IMPORT() { return getToken(XQueryParserScripting.KW_IMPORT, 0); }
		public TerminalNode KW_IN() { return getToken(XQueryParserScripting.KW_IN, 0); }
		public TerminalNode KW_INHERIT() { return getToken(XQueryParserScripting.KW_INHERIT, 0); }
		public TerminalNode KW_INSTANCE() { return getToken(XQueryParserScripting.KW_INSTANCE, 0); }
		public TerminalNode KW_INTERSECT() { return getToken(XQueryParserScripting.KW_INTERSECT, 0); }
		public TerminalNode KW_IS() { return getToken(XQueryParserScripting.KW_IS, 0); }
		public TerminalNode KW_LAX() { return getToken(XQueryParserScripting.KW_LAX, 0); }
		public TerminalNode KW_LE() { return getToken(XQueryParserScripting.KW_LE, 0); }
		public TerminalNode KW_LEAST() { return getToken(XQueryParserScripting.KW_LEAST, 0); }
		public TerminalNode KW_LET() { return getToken(XQueryParserScripting.KW_LET, 0); }
		public TerminalNode KW_LT() { return getToken(XQueryParserScripting.KW_LT, 0); }
		public TerminalNode KW_MOD() { return getToken(XQueryParserScripting.KW_MOD, 0); }
		public TerminalNode KW_MODULE() { return getToken(XQueryParserScripting.KW_MODULE, 0); }
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParserScripting.KW_NAMESPACE, 0); }
		public TerminalNode KW_NE() { return getToken(XQueryParserScripting.KW_NE, 0); }
		public TerminalNode KW_NO_INHERIT() { return getToken(XQueryParserScripting.KW_NO_INHERIT, 0); }
		public TerminalNode KW_NO_PRESERVE() { return getToken(XQueryParserScripting.KW_NO_PRESERVE, 0); }
		public TerminalNode KW_OF() { return getToken(XQueryParserScripting.KW_OF, 0); }
		public TerminalNode KW_OPTION() { return getToken(XQueryParserScripting.KW_OPTION, 0); }
		public TerminalNode KW_OR() { return getToken(XQueryParserScripting.KW_OR, 0); }
		public TerminalNode KW_ORDER() { return getToken(XQueryParserScripting.KW_ORDER, 0); }
		public TerminalNode KW_ORDERED() { return getToken(XQueryParserScripting.KW_ORDERED, 0); }
		public TerminalNode KW_ORDERING() { return getToken(XQueryParserScripting.KW_ORDERING, 0); }
		public TerminalNode KW_PARENT() { return getToken(XQueryParserScripting.KW_PARENT, 0); }
		public TerminalNode KW_PRECEDING() { return getToken(XQueryParserScripting.KW_PRECEDING, 0); }
		public TerminalNode KW_PRECEDING_SIBLING() { return getToken(XQueryParserScripting.KW_PRECEDING_SIBLING, 0); }
		public TerminalNode KW_PRESERVE() { return getToken(XQueryParserScripting.KW_PRESERVE, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParserScripting.KW_RETURN, 0); }
		public TerminalNode KW_SATISFIES() { return getToken(XQueryParserScripting.KW_SATISFIES, 0); }
		public TerminalNode KW_SCHEMA() { return getToken(XQueryParserScripting.KW_SCHEMA, 0); }
		public TerminalNode KW_SELF() { return getToken(XQueryParserScripting.KW_SELF, 0); }
		public TerminalNode KW_SOME() { return getToken(XQueryParserScripting.KW_SOME, 0); }
		public TerminalNode KW_STABLE() { return getToken(XQueryParserScripting.KW_STABLE, 0); }
		public TerminalNode KW_START() { return getToken(XQueryParserScripting.KW_START, 0); }
		public TerminalNode KW_STRICT() { return getToken(XQueryParserScripting.KW_STRICT, 0); }
		public TerminalNode KW_STRIP() { return getToken(XQueryParserScripting.KW_STRIP, 0); }
		public TerminalNode KW_THEN() { return getToken(XQueryParserScripting.KW_THEN, 0); }
		public TerminalNode KW_TO() { return getToken(XQueryParserScripting.KW_TO, 0); }
		public TerminalNode KW_TREAT() { return getToken(XQueryParserScripting.KW_TREAT, 0); }
		public TerminalNode KW_UNION() { return getToken(XQueryParserScripting.KW_UNION, 0); }
		public TerminalNode KW_UNORDERED() { return getToken(XQueryParserScripting.KW_UNORDERED, 0); }
		public TerminalNode KW_VALIDATE() { return getToken(XQueryParserScripting.KW_VALIDATE, 0); }
		public TerminalNode KW_VARIABLE() { return getToken(XQueryParserScripting.KW_VARIABLE, 0); }
		public TerminalNode KW_VERSION() { return getToken(XQueryParserScripting.KW_VERSION, 0); }
		public TerminalNode KW_WHERE() { return getToken(XQueryParserScripting.KW_WHERE, 0); }
		public TerminalNode KW_XQUERY() { return getToken(XQueryParserScripting.KW_XQUERY, 0); }
		public KeywordOKForFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keywordOKForFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterKeywordOKForFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitKeywordOKForFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitKeywordOKForFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordOKForFunctionContext keywordOKForFunction() throws RecognitionException {
		KeywordOKForFunctionContext _localctx = new KeywordOKForFunctionContext(_ctx, getState());
		enterRule(_localctx, 514, RULE_keywordOKForFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2291);
			_la = _input.LA(1);
			if ( !(((((_la - 54)) & ~0x3f) == 0 && ((1L << (_la - 54)) & -290482354480514185L) != 0) || ((((_la - 119)) & ~0x3f) == 0 && ((1L << (_la - 119)) & 6148966866483919L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UriLiteralContext extends ParserRuleContext {
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public UriLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uriLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterUriLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitUriLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitUriLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UriLiteralContext uriLiteral() throws RecognitionException {
		UriLiteralContext _localctx = new UriLiteralContext(_ctx, getState());
		enterRule(_localctx, 516, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2293);
			stringLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteralQuotContext extends ParserRuleContext {
		public List<TerminalNode> Quot() { return getTokens(XQueryParserScripting.Quot); }
		public TerminalNode Quot(int i) {
			return getToken(XQueryParserScripting.Quot, i);
		}
		public List<TerminalNode> PredefinedEntityRef() { return getTokens(XQueryParserScripting.PredefinedEntityRef); }
		public TerminalNode PredefinedEntityRef(int i) {
			return getToken(XQueryParserScripting.PredefinedEntityRef, i);
		}
		public List<TerminalNode> CharRef() { return getTokens(XQueryParserScripting.CharRef); }
		public TerminalNode CharRef(int i) {
			return getToken(XQueryParserScripting.CharRef, i);
		}
		public List<TerminalNode> EscapeQuot() { return getTokens(XQueryParserScripting.EscapeQuot); }
		public TerminalNode EscapeQuot(int i) {
			return getToken(XQueryParserScripting.EscapeQuot, i);
		}
		public List<StringContentQuotContext> stringContentQuot() {
			return getRuleContexts(StringContentQuotContext.class);
		}
		public StringContentQuotContext stringContentQuot(int i) {
			return getRuleContext(StringContentQuotContext.class,i);
		}
		public StringLiteralQuotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteralQuot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringLiteralQuot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringLiteralQuot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringLiteralQuot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralQuotContext stringLiteralQuot() throws RecognitionException {
		StringLiteralQuotContext _localctx = new StringLiteralQuotContext(_ctx, getState());
		enterRule(_localctx, 518, RULE_stringLiteralQuot);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2295);
			match(Quot);
			setState(2302);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2252899325691910L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720798209L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 1039L) != 0)) {
				{
				setState(2300);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PredefinedEntityRef:
					{
					setState(2296);
					match(PredefinedEntityRef);
					}
					break;
				case CharRef:
					{
					setState(2297);
					match(CharRef);
					}
					break;
				case EscapeQuot:
					{
					setState(2298);
					match(EscapeQuot);
					}
					break;
				case DOUBLE_LBRACE:
				case DOUBLE_RBRACE:
				case IntegerLiteral:
				case DecimalLiteral:
				case DoubleLiteral:
				case DFPropertyName:
				case Apos:
				case COMMENT:
				case PRAGMA:
				case EQUAL:
				case NOT_EQUAL:
				case LPAREN:
				case RPAREN:
				case LBRACKET:
				case RBRACKET:
				case LBRACE:
				case RBRACE:
				case STAR:
				case PLUS:
				case MINUS:
				case COMMA:
				case DOT:
				case DDOT:
				case COLON:
				case COLON_EQ:
				case SEMICOLON:
				case SLASH:
				case DSLASH:
				case BACKSLASH:
				case VBAR:
				case RANGLE:
				case QUESTION:
				case AT:
				case DOLLAR:
				case MOD:
				case BANG:
				case HASH:
				case CARAT:
				case ARROW:
				case GRAVE:
				case TILDE:
				case KW_ALLOWING:
				case KW_ANCESTOR:
				case KW_ANCESTOR_OR_SELF:
				case KW_AND:
				case KW_ARRAY:
				case KW_AS:
				case KW_ASCENDING:
				case KW_AT:
				case KW_ATTRIBUTE:
				case KW_BASE_URI:
				case KW_BOUNDARY_SPACE:
				case KW_BINARY:
				case KW_BY:
				case KW_CASE:
				case KW_CAST:
				case KW_CASTABLE:
				case KW_CATCH:
				case KW_CHILD:
				case KW_COLLATION:
				case KW_COMMENT:
				case KW_CONSTRUCTION:
				case KW_CONTEXT:
				case KW_COPY_NS:
				case KW_COUNT:
				case KW_DECLARE:
				case KW_DEFAULT:
				case KW_DESCENDANT:
				case KW_DESCENDANT_OR_SELF:
				case KW_DESCENDING:
				case KW_DECIMAL_FORMAT:
				case KW_DIV:
				case KW_DOCUMENT:
				case KW_DOCUMENT_NODE:
				case KW_ELEMENT:
				case KW_ELSE:
				case KW_EMPTY:
				case KW_EMPTY_SEQUENCE:
				case KW_ENCODING:
				case KW_END:
				case KW_EQ:
				case KW_EVERY:
				case KW_EXCEPT:
				case KW_EXTERNAL:
				case KW_FOLLOWING:
				case KW_FOLLOWING_SIBLING:
				case KW_FOR:
				case KW_FUNCTION:
				case KW_GE:
				case KW_GREATEST:
				case KW_GROUP:
				case KW_GT:
				case KW_IDIV:
				case KW_IF:
				case KW_IMPORT:
				case KW_IN:
				case KW_INHERIT:
				case KW_INSTANCE:
				case KW_INTERSECT:
				case KW_IS:
				case KW_ITEM:
				case KW_LAX:
				case KW_LE:
				case KW_LEAST:
				case KW_LET:
				case KW_LT:
				case KW_MAP:
				case KW_MOD:
				case KW_MODULE:
				case KW_NAMESPACE:
				case KW_NE:
				case KW_NEXT:
				case KW_NAMESPACE_NODE:
				case KW_NO_INHERIT:
				case KW_NO_PRESERVE:
				case KW_NODE:
				case KW_OF:
				case KW_ONLY:
				case KW_OPTION:
				case KW_OR:
				case KW_ORDER:
				case KW_ORDERED:
				case KW_ORDERING:
				case KW_PARENT:
				case KW_PRECEDING:
				case KW_PRECEDING_SIBLING:
				case KW_PRESERVE:
				case KW_PREVIOUS:
				case KW_PI:
				case KW_RETURN:
				case KW_SATISFIES:
				case KW_SCHEMA:
				case KW_SCHEMA_ATTR:
				case KW_SCHEMA_ELEM:
				case KW_SELF:
				case KW_SLIDING:
				case KW_SOME:
				case KW_STABLE:
				case KW_START:
				case KW_STRICT:
				case KW_STRIP:
				case KW_SWITCH:
				case KW_TEXT:
				case KW_THEN:
				case KW_TO:
				case KW_TREAT:
				case KW_TRY:
				case KW_TUMBLING:
				case KW_TYPE:
				case KW_TYPESWITCH:
				case KW_UNION:
				case KW_UNORDERED:
				case KW_UPDATE:
				case KW_VALIDATE:
				case KW_VARIABLE:
				case KW_VERSION:
				case KW_WHEN:
				case KW_WHERE:
				case KW_WINDOW:
				case KW_XQUERY:
				case KW_ARRAY_NODE:
				case KW_BOOLEAN_NODE:
				case KW_NULL_NODE:
				case KW_NUMBER_NODE:
				case KW_OBJECT_NODE:
				case KW_REPLACE:
				case KW_WITH:
				case KW_VALUE:
				case KW_INSERT:
				case KW_INTO:
				case KW_DELETE:
				case KW_RENAME:
				case URIQualifiedName:
				case FullQName:
				case NCNameWithLocalWildcard:
				case NCNameWithPrefixWildcard:
				case NCName:
				case XQDOC_COMMENT_START:
				case ContentChar:
					{
					setState(2299);
					stringContentQuot();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2304);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2305);
			match(Quot);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteralAposContext extends ParserRuleContext {
		public List<TerminalNode> Apos() { return getTokens(XQueryParserScripting.Apos); }
		public TerminalNode Apos(int i) {
			return getToken(XQueryParserScripting.Apos, i);
		}
		public List<TerminalNode> PredefinedEntityRef() { return getTokens(XQueryParserScripting.PredefinedEntityRef); }
		public TerminalNode PredefinedEntityRef(int i) {
			return getToken(XQueryParserScripting.PredefinedEntityRef, i);
		}
		public List<TerminalNode> CharRef() { return getTokens(XQueryParserScripting.CharRef); }
		public TerminalNode CharRef(int i) {
			return getToken(XQueryParserScripting.CharRef, i);
		}
		public List<TerminalNode> EscapeApos() { return getTokens(XQueryParserScripting.EscapeApos); }
		public TerminalNode EscapeApos(int i) {
			return getToken(XQueryParserScripting.EscapeApos, i);
		}
		public List<StringContentAposContext> stringContentApos() {
			return getRuleContexts(StringContentAposContext.class);
		}
		public StringContentAposContext stringContentApos(int i) {
			return getRuleContext(StringContentAposContext.class,i);
		}
		public StringLiteralAposContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteralApos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringLiteralApos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringLiteralApos(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringLiteralApos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralAposContext stringLiteralApos() throws RecognitionException {
		StringLiteralAposContext _localctx = new StringLiteralAposContext(_ctx, getState());
		enterRule(_localctx, 520, RULE_stringLiteralApos);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2307);
			match(Apos);
			setState(2314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2252899325693956L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & -1108307720798209L) != 0) || ((((_la - 192)) & ~0x3f) == 0 && ((1L << (_la - 192)) & 1039L) != 0)) {
				{
				setState(2312);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PredefinedEntityRef:
					{
					setState(2308);
					match(PredefinedEntityRef);
					}
					break;
				case CharRef:
					{
					setState(2309);
					match(CharRef);
					}
					break;
				case EscapeApos:
					{
					setState(2310);
					match(EscapeApos);
					}
					break;
				case DOUBLE_LBRACE:
				case DOUBLE_RBRACE:
				case IntegerLiteral:
				case DecimalLiteral:
				case DoubleLiteral:
				case DFPropertyName:
				case Quot:
				case COMMENT:
				case PRAGMA:
				case EQUAL:
				case NOT_EQUAL:
				case LPAREN:
				case RPAREN:
				case LBRACKET:
				case RBRACKET:
				case LBRACE:
				case RBRACE:
				case STAR:
				case PLUS:
				case MINUS:
				case COMMA:
				case DOT:
				case DDOT:
				case COLON:
				case COLON_EQ:
				case SEMICOLON:
				case SLASH:
				case DSLASH:
				case BACKSLASH:
				case VBAR:
				case RANGLE:
				case QUESTION:
				case AT:
				case DOLLAR:
				case MOD:
				case BANG:
				case HASH:
				case CARAT:
				case ARROW:
				case GRAVE:
				case TILDE:
				case KW_ALLOWING:
				case KW_ANCESTOR:
				case KW_ANCESTOR_OR_SELF:
				case KW_AND:
				case KW_ARRAY:
				case KW_AS:
				case KW_ASCENDING:
				case KW_AT:
				case KW_ATTRIBUTE:
				case KW_BASE_URI:
				case KW_BOUNDARY_SPACE:
				case KW_BINARY:
				case KW_BY:
				case KW_CASE:
				case KW_CAST:
				case KW_CASTABLE:
				case KW_CATCH:
				case KW_CHILD:
				case KW_COLLATION:
				case KW_COMMENT:
				case KW_CONSTRUCTION:
				case KW_CONTEXT:
				case KW_COPY_NS:
				case KW_COUNT:
				case KW_DECLARE:
				case KW_DEFAULT:
				case KW_DESCENDANT:
				case KW_DESCENDANT_OR_SELF:
				case KW_DESCENDING:
				case KW_DECIMAL_FORMAT:
				case KW_DIV:
				case KW_DOCUMENT:
				case KW_DOCUMENT_NODE:
				case KW_ELEMENT:
				case KW_ELSE:
				case KW_EMPTY:
				case KW_EMPTY_SEQUENCE:
				case KW_ENCODING:
				case KW_END:
				case KW_EQ:
				case KW_EVERY:
				case KW_EXCEPT:
				case KW_EXTERNAL:
				case KW_FOLLOWING:
				case KW_FOLLOWING_SIBLING:
				case KW_FOR:
				case KW_FUNCTION:
				case KW_GE:
				case KW_GREATEST:
				case KW_GROUP:
				case KW_GT:
				case KW_IDIV:
				case KW_IF:
				case KW_IMPORT:
				case KW_IN:
				case KW_INHERIT:
				case KW_INSTANCE:
				case KW_INTERSECT:
				case KW_IS:
				case KW_ITEM:
				case KW_LAX:
				case KW_LE:
				case KW_LEAST:
				case KW_LET:
				case KW_LT:
				case KW_MAP:
				case KW_MOD:
				case KW_MODULE:
				case KW_NAMESPACE:
				case KW_NE:
				case KW_NEXT:
				case KW_NAMESPACE_NODE:
				case KW_NO_INHERIT:
				case KW_NO_PRESERVE:
				case KW_NODE:
				case KW_OF:
				case KW_ONLY:
				case KW_OPTION:
				case KW_OR:
				case KW_ORDER:
				case KW_ORDERED:
				case KW_ORDERING:
				case KW_PARENT:
				case KW_PRECEDING:
				case KW_PRECEDING_SIBLING:
				case KW_PRESERVE:
				case KW_PREVIOUS:
				case KW_PI:
				case KW_RETURN:
				case KW_SATISFIES:
				case KW_SCHEMA:
				case KW_SCHEMA_ATTR:
				case KW_SCHEMA_ELEM:
				case KW_SELF:
				case KW_SLIDING:
				case KW_SOME:
				case KW_STABLE:
				case KW_START:
				case KW_STRICT:
				case KW_STRIP:
				case KW_SWITCH:
				case KW_TEXT:
				case KW_THEN:
				case KW_TO:
				case KW_TREAT:
				case KW_TRY:
				case KW_TUMBLING:
				case KW_TYPE:
				case KW_TYPESWITCH:
				case KW_UNION:
				case KW_UNORDERED:
				case KW_UPDATE:
				case KW_VALIDATE:
				case KW_VARIABLE:
				case KW_VERSION:
				case KW_WHEN:
				case KW_WHERE:
				case KW_WINDOW:
				case KW_XQUERY:
				case KW_ARRAY_NODE:
				case KW_BOOLEAN_NODE:
				case KW_NULL_NODE:
				case KW_NUMBER_NODE:
				case KW_OBJECT_NODE:
				case KW_REPLACE:
				case KW_WITH:
				case KW_VALUE:
				case KW_INSERT:
				case KW_INTO:
				case KW_DELETE:
				case KW_RENAME:
				case URIQualifiedName:
				case FullQName:
				case NCNameWithLocalWildcard:
				case NCNameWithPrefixWildcard:
				case NCName:
				case XQDOC_COMMENT_START:
				case ContentChar:
					{
					setState(2311);
					stringContentApos();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2316);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2317);
			match(Apos);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteralContext extends ParserRuleContext {
		public StringLiteralQuotContext stringLiteralQuot() {
			return getRuleContext(StringLiteralQuotContext.class,0);
		}
		public StringLiteralAposContext stringLiteralApos() {
			return getRuleContext(StringLiteralAposContext.class,0);
		}
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 522, RULE_stringLiteral);
		try {
			setState(2321);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Quot:
				enterOuterAlt(_localctx, 1);
				{
				setState(2319);
				stringLiteralQuot();
				}
				break;
			case Apos:
				enterOuterAlt(_localctx, 2);
				{
				setState(2320);
				stringLiteralApos();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringContentQuotContext extends ParserRuleContext {
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParserScripting.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParserScripting.ContentChar, i);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public TerminalNode DOUBLE_LBRACE() { return getToken(XQueryParserScripting.DOUBLE_LBRACE, 0); }
		public TerminalNode DOUBLE_RBRACE() { return getToken(XQueryParserScripting.DOUBLE_RBRACE, 0); }
		public NoQuotesNoBracesNoAmpNoLAngContext noQuotesNoBracesNoAmpNoLAng() {
			return getRuleContext(NoQuotesNoBracesNoAmpNoLAngContext.class,0);
		}
		public StringLiteralAposContext stringLiteralApos() {
			return getRuleContext(StringLiteralAposContext.class,0);
		}
		public StringContentQuotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringContentQuot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringContentQuot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringContentQuot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringContentQuot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContentQuotContext stringContentQuot() throws RecognitionException {
		StringContentQuotContext _localctx = new StringContentQuotContext(_ctx, getState());
		enterRule(_localctx, 524, RULE_stringContentQuot);
		try {
			int _alt;
			setState(2340);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,216,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2324); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(2323);
						match(ContentChar);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(2326); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,213,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2328);
				match(LBRACE);
				setState(2330);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,214,_ctx) ) {
				case 1:
					{
					setState(2329);
					expr();
					}
					break;
				}
				setState(2333);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,215,_ctx) ) {
				case 1:
					{
					setState(2332);
					match(RBRACE);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2335);
				match(RBRACE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2336);
				match(DOUBLE_LBRACE);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2337);
				match(DOUBLE_RBRACE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2338);
				noQuotesNoBracesNoAmpNoLAng();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2339);
				stringLiteralApos();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringContentAposContext extends ParserRuleContext {
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParserScripting.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParserScripting.ContentChar, i);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParserScripting.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParserScripting.RBRACE, 0); }
		public TerminalNode DOUBLE_LBRACE() { return getToken(XQueryParserScripting.DOUBLE_LBRACE, 0); }
		public TerminalNode DOUBLE_RBRACE() { return getToken(XQueryParserScripting.DOUBLE_RBRACE, 0); }
		public NoQuotesNoBracesNoAmpNoLAngContext noQuotesNoBracesNoAmpNoLAng() {
			return getRuleContext(NoQuotesNoBracesNoAmpNoLAngContext.class,0);
		}
		public StringLiteralQuotContext stringLiteralQuot() {
			return getRuleContext(StringLiteralQuotContext.class,0);
		}
		public StringContentAposContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringContentApos; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterStringContentApos(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitStringContentApos(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitStringContentApos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContentAposContext stringContentApos() throws RecognitionException {
		StringContentAposContext _localctx = new StringContentAposContext(_ctx, getState());
		enterRule(_localctx, 526, RULE_stringContentApos);
		try {
			int _alt;
			setState(2359);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,220,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2343); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(2342);
						match(ContentChar);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(2345); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,217,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2347);
				match(LBRACE);
				setState(2349);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,218,_ctx) ) {
				case 1:
					{
					setState(2348);
					expr();
					}
					break;
				}
				setState(2352);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,219,_ctx) ) {
				case 1:
					{
					setState(2351);
					match(RBRACE);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2354);
				match(RBRACE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2355);
				match(DOUBLE_LBRACE);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2356);
				match(DOUBLE_RBRACE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2357);
				noQuotesNoBracesNoAmpNoLAng();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2358);
				stringLiteralQuot();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NoQuotesNoBracesNoAmpNoLAngContext extends ParserRuleContext {
		public List<KeywordContext> keyword() {
			return getRuleContexts(KeywordContext.class);
		}
		public KeywordContext keyword(int i) {
			return getRuleContext(KeywordContext.class,i);
		}
		public List<TerminalNode> IntegerLiteral() { return getTokens(XQueryParserScripting.IntegerLiteral); }
		public TerminalNode IntegerLiteral(int i) {
			return getToken(XQueryParserScripting.IntegerLiteral, i);
		}
		public List<TerminalNode> DecimalLiteral() { return getTokens(XQueryParserScripting.DecimalLiteral); }
		public TerminalNode DecimalLiteral(int i) {
			return getToken(XQueryParserScripting.DecimalLiteral, i);
		}
		public List<TerminalNode> DoubleLiteral() { return getTokens(XQueryParserScripting.DoubleLiteral); }
		public TerminalNode DoubleLiteral(int i) {
			return getToken(XQueryParserScripting.DoubleLiteral, i);
		}
		public List<TerminalNode> PRAGMA() { return getTokens(XQueryParserScripting.PRAGMA); }
		public TerminalNode PRAGMA(int i) {
			return getToken(XQueryParserScripting.PRAGMA, i);
		}
		public List<TerminalNode> EQUAL() { return getTokens(XQueryParserScripting.EQUAL); }
		public TerminalNode EQUAL(int i) {
			return getToken(XQueryParserScripting.EQUAL, i);
		}
		public List<TerminalNode> HASH() { return getTokens(XQueryParserScripting.HASH); }
		public TerminalNode HASH(int i) {
			return getToken(XQueryParserScripting.HASH, i);
		}
		public List<TerminalNode> NOT_EQUAL() { return getTokens(XQueryParserScripting.NOT_EQUAL); }
		public TerminalNode NOT_EQUAL(int i) {
			return getToken(XQueryParserScripting.NOT_EQUAL, i);
		}
		public List<TerminalNode> LPAREN() { return getTokens(XQueryParserScripting.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(XQueryParserScripting.LPAREN, i);
		}
		public List<TerminalNode> RPAREN() { return getTokens(XQueryParserScripting.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(XQueryParserScripting.RPAREN, i);
		}
		public List<TerminalNode> LBRACKET() { return getTokens(XQueryParserScripting.LBRACKET); }
		public TerminalNode LBRACKET(int i) {
			return getToken(XQueryParserScripting.LBRACKET, i);
		}
		public List<TerminalNode> RBRACKET() { return getTokens(XQueryParserScripting.RBRACKET); }
		public TerminalNode RBRACKET(int i) {
			return getToken(XQueryParserScripting.RBRACKET, i);
		}
		public List<TerminalNode> STAR() { return getTokens(XQueryParserScripting.STAR); }
		public TerminalNode STAR(int i) {
			return getToken(XQueryParserScripting.STAR, i);
		}
		public List<TerminalNode> PLUS() { return getTokens(XQueryParserScripting.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(XQueryParserScripting.PLUS, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(XQueryParserScripting.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(XQueryParserScripting.MINUS, i);
		}
		public List<TerminalNode> TILDE() { return getTokens(XQueryParserScripting.TILDE); }
		public TerminalNode TILDE(int i) {
			return getToken(XQueryParserScripting.TILDE, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParserScripting.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParserScripting.COMMA, i);
		}
		public List<TerminalNode> ARROW() { return getTokens(XQueryParserScripting.ARROW); }
		public TerminalNode ARROW(int i) {
			return getToken(XQueryParserScripting.ARROW, i);
		}
		public List<TerminalNode> KW_NEXT() { return getTokens(XQueryParserScripting.KW_NEXT); }
		public TerminalNode KW_NEXT(int i) {
			return getToken(XQueryParserScripting.KW_NEXT, i);
		}
		public List<TerminalNode> KW_PREVIOUS() { return getTokens(XQueryParserScripting.KW_PREVIOUS); }
		public TerminalNode KW_PREVIOUS(int i) {
			return getToken(XQueryParserScripting.KW_PREVIOUS, i);
		}
		public List<TerminalNode> MOD() { return getTokens(XQueryParserScripting.MOD); }
		public TerminalNode MOD(int i) {
			return getToken(XQueryParserScripting.MOD, i);
		}
		public List<TerminalNode> DOT() { return getTokens(XQueryParserScripting.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(XQueryParserScripting.DOT, i);
		}
		public List<TerminalNode> GRAVE() { return getTokens(XQueryParserScripting.GRAVE); }
		public TerminalNode GRAVE(int i) {
			return getToken(XQueryParserScripting.GRAVE, i);
		}
		public List<TerminalNode> DDOT() { return getTokens(XQueryParserScripting.DDOT); }
		public TerminalNode DDOT(int i) {
			return getToken(XQueryParserScripting.DDOT, i);
		}
		public List<TerminalNode> XQDOC_COMMENT_START() { return getTokens(XQueryParserScripting.XQDOC_COMMENT_START); }
		public TerminalNode XQDOC_COMMENT_START(int i) {
			return getToken(XQueryParserScripting.XQDOC_COMMENT_START, i);
		}
		public List<TerminalNode> COLON() { return getTokens(XQueryParserScripting.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(XQueryParserScripting.COLON, i);
		}
		public List<TerminalNode> CARAT() { return getTokens(XQueryParserScripting.CARAT); }
		public TerminalNode CARAT(int i) {
			return getToken(XQueryParserScripting.CARAT, i);
		}
		public List<TerminalNode> COLON_EQ() { return getTokens(XQueryParserScripting.COLON_EQ); }
		public TerminalNode COLON_EQ(int i) {
			return getToken(XQueryParserScripting.COLON_EQ, i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(XQueryParserScripting.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(XQueryParserScripting.SEMICOLON, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(XQueryParserScripting.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(XQueryParserScripting.SLASH, i);
		}
		public List<TerminalNode> DSLASH() { return getTokens(XQueryParserScripting.DSLASH); }
		public TerminalNode DSLASH(int i) {
			return getToken(XQueryParserScripting.DSLASH, i);
		}
		public List<TerminalNode> BACKSLASH() { return getTokens(XQueryParserScripting.BACKSLASH); }
		public TerminalNode BACKSLASH(int i) {
			return getToken(XQueryParserScripting.BACKSLASH, i);
		}
		public List<TerminalNode> COMMENT() { return getTokens(XQueryParserScripting.COMMENT); }
		public TerminalNode COMMENT(int i) {
			return getToken(XQueryParserScripting.COMMENT, i);
		}
		public List<TerminalNode> VBAR() { return getTokens(XQueryParserScripting.VBAR); }
		public TerminalNode VBAR(int i) {
			return getToken(XQueryParserScripting.VBAR, i);
		}
		public List<TerminalNode> RANGLE() { return getTokens(XQueryParserScripting.RANGLE); }
		public TerminalNode RANGLE(int i) {
			return getToken(XQueryParserScripting.RANGLE, i);
		}
		public List<TerminalNode> QUESTION() { return getTokens(XQueryParserScripting.QUESTION); }
		public TerminalNode QUESTION(int i) {
			return getToken(XQueryParserScripting.QUESTION, i);
		}
		public List<TerminalNode> AT() { return getTokens(XQueryParserScripting.AT); }
		public TerminalNode AT(int i) {
			return getToken(XQueryParserScripting.AT, i);
		}
		public List<TerminalNode> DOLLAR() { return getTokens(XQueryParserScripting.DOLLAR); }
		public TerminalNode DOLLAR(int i) {
			return getToken(XQueryParserScripting.DOLLAR, i);
		}
		public List<TerminalNode> BANG() { return getTokens(XQueryParserScripting.BANG); }
		public TerminalNode BANG(int i) {
			return getToken(XQueryParserScripting.BANG, i);
		}
		public List<TerminalNode> FullQName() { return getTokens(XQueryParserScripting.FullQName); }
		public TerminalNode FullQName(int i) {
			return getToken(XQueryParserScripting.FullQName, i);
		}
		public List<TerminalNode> URIQualifiedName() { return getTokens(XQueryParserScripting.URIQualifiedName); }
		public TerminalNode URIQualifiedName(int i) {
			return getToken(XQueryParserScripting.URIQualifiedName, i);
		}
		public List<TerminalNode> NCNameWithLocalWildcard() { return getTokens(XQueryParserScripting.NCNameWithLocalWildcard); }
		public TerminalNode NCNameWithLocalWildcard(int i) {
			return getToken(XQueryParserScripting.NCNameWithLocalWildcard, i);
		}
		public List<TerminalNode> NCNameWithPrefixWildcard() { return getTokens(XQueryParserScripting.NCNameWithPrefixWildcard); }
		public TerminalNode NCNameWithPrefixWildcard(int i) {
			return getToken(XQueryParserScripting.NCNameWithPrefixWildcard, i);
		}
		public List<TerminalNode> NCName() { return getTokens(XQueryParserScripting.NCName); }
		public TerminalNode NCName(int i) {
			return getToken(XQueryParserScripting.NCName, i);
		}
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParserScripting.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParserScripting.ContentChar, i);
		}
		public NoQuotesNoBracesNoAmpNoLAngContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noQuotesNoBracesNoAmpNoLAng; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).enterNoQuotesNoBracesNoAmpNoLAng(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQueryParserScriptingListener ) ((XQueryParserScriptingListener)listener).exitNoQuotesNoBracesNoAmpNoLAng(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserScriptingVisitor ) return ((XQueryParserScriptingVisitor<? extends T>)visitor).visitNoQuotesNoBracesNoAmpNoLAng(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoQuotesNoBracesNoAmpNoLAngContext noQuotesNoBracesNoAmpNoLAng() throws RecognitionException {
		NoQuotesNoBracesNoAmpNoLAngContext _localctx = new NoQuotesNoBracesNoAmpNoLAngContext(_ctx, getState());
		enterRule(_localctx, 528, RULE_noQuotesNoBracesNoAmpNoLAng);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2363); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(2363);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,221,_ctx) ) {
					case 1:
						{
						setState(2361);
						keyword();
						}
						break;
					case 2:
						{
						setState(2362);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 6754299828379872L) != 0) || _la==KW_NEXT || _la==KW_PREVIOUS || ((((_la - 190)) & ~0x3f) == 0 && ((1L << (_la - 190)) & 4159L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(2365); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,222,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u00d1\u0940\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007"+
		"6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007"+
		";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007"+
		"@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007"+
		"E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007"+
		"J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007"+
		"O\u0002P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007S\u0002T\u0007"+
		"T\u0002U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007X\u0002Y\u0007"+
		"Y\u0002Z\u0007Z\u0002[\u0007[\u0002\\\u0007\\\u0002]\u0007]\u0002^\u0007"+
		"^\u0002_\u0007_\u0002`\u0007`\u0002a\u0007a\u0002b\u0007b\u0002c\u0007"+
		"c\u0002d\u0007d\u0002e\u0007e\u0002f\u0007f\u0002g\u0007g\u0002h\u0007"+
		"h\u0002i\u0007i\u0002j\u0007j\u0002k\u0007k\u0002l\u0007l\u0002m\u0007"+
		"m\u0002n\u0007n\u0002o\u0007o\u0002p\u0007p\u0002q\u0007q\u0002r\u0007"+
		"r\u0002s\u0007s\u0002t\u0007t\u0002u\u0007u\u0002v\u0007v\u0002w\u0007"+
		"w\u0002x\u0007x\u0002y\u0007y\u0002z\u0007z\u0002{\u0007{\u0002|\u0007"+
		"|\u0002}\u0007}\u0002~\u0007~\u0002\u007f\u0007\u007f\u0002\u0080\u0007"+
		"\u0080\u0002\u0081\u0007\u0081\u0002\u0082\u0007\u0082\u0002\u0083\u0007"+
		"\u0083\u0002\u0084\u0007\u0084\u0002\u0085\u0007\u0085\u0002\u0086\u0007"+
		"\u0086\u0002\u0087\u0007\u0087\u0002\u0088\u0007\u0088\u0002\u0089\u0007"+
		"\u0089\u0002\u008a\u0007\u008a\u0002\u008b\u0007\u008b\u0002\u008c\u0007"+
		"\u008c\u0002\u008d\u0007\u008d\u0002\u008e\u0007\u008e\u0002\u008f\u0007"+
		"\u008f\u0002\u0090\u0007\u0090\u0002\u0091\u0007\u0091\u0002\u0092\u0007"+
		"\u0092\u0002\u0093\u0007\u0093\u0002\u0094\u0007\u0094\u0002\u0095\u0007"+
		"\u0095\u0002\u0096\u0007\u0096\u0002\u0097\u0007\u0097\u0002\u0098\u0007"+
		"\u0098\u0002\u0099\u0007\u0099\u0002\u009a\u0007\u009a\u0002\u009b\u0007"+
		"\u009b\u0002\u009c\u0007\u009c\u0002\u009d\u0007\u009d\u0002\u009e\u0007"+
		"\u009e\u0002\u009f\u0007\u009f\u0002\u00a0\u0007\u00a0\u0002\u00a1\u0007"+
		"\u00a1\u0002\u00a2\u0007\u00a2\u0002\u00a3\u0007\u00a3\u0002\u00a4\u0007"+
		"\u00a4\u0002\u00a5\u0007\u00a5\u0002\u00a6\u0007\u00a6\u0002\u00a7\u0007"+
		"\u00a7\u0002\u00a8\u0007\u00a8\u0002\u00a9\u0007\u00a9\u0002\u00aa\u0007"+
		"\u00aa\u0002\u00ab\u0007\u00ab\u0002\u00ac\u0007\u00ac\u0002\u00ad\u0007"+
		"\u00ad\u0002\u00ae\u0007\u00ae\u0002\u00af\u0007\u00af\u0002\u00b0\u0007"+
		"\u00b0\u0002\u00b1\u0007\u00b1\u0002\u00b2\u0007\u00b2\u0002\u00b3\u0007"+
		"\u00b3\u0002\u00b4\u0007\u00b4\u0002\u00b5\u0007\u00b5\u0002\u00b6\u0007"+
		"\u00b6\u0002\u00b7\u0007\u00b7\u0002\u00b8\u0007\u00b8\u0002\u00b9\u0007"+
		"\u00b9\u0002\u00ba\u0007\u00ba\u0002\u00bb\u0007\u00bb\u0002\u00bc\u0007"+
		"\u00bc\u0002\u00bd\u0007\u00bd\u0002\u00be\u0007\u00be\u0002\u00bf\u0007"+
		"\u00bf\u0002\u00c0\u0007\u00c0\u0002\u00c1\u0007\u00c1\u0002\u00c2\u0007"+
		"\u00c2\u0002\u00c3\u0007\u00c3\u0002\u00c4\u0007\u00c4\u0002\u00c5\u0007"+
		"\u00c5\u0002\u00c6\u0007\u00c6\u0002\u00c7\u0007\u00c7\u0002\u00c8\u0007"+
		"\u00c8\u0002\u00c9\u0007\u00c9\u0002\u00ca\u0007\u00ca\u0002\u00cb\u0007"+
		"\u00cb\u0002\u00cc\u0007\u00cc\u0002\u00cd\u0007\u00cd\u0002\u00ce\u0007"+
		"\u00ce\u0002\u00cf\u0007\u00cf\u0002\u00d0\u0007\u00d0\u0002\u00d1\u0007"+
		"\u00d1\u0002\u00d2\u0007\u00d2\u0002\u00d3\u0007\u00d3\u0002\u00d4\u0007"+
		"\u00d4\u0002\u00d5\u0007\u00d5\u0002\u00d6\u0007\u00d6\u0002\u00d7\u0007"+
		"\u00d7\u0002\u00d8\u0007\u00d8\u0002\u00d9\u0007\u00d9\u0002\u00da\u0007"+
		"\u00da\u0002\u00db\u0007\u00db\u0002\u00dc\u0007\u00dc\u0002\u00dd\u0007"+
		"\u00dd\u0002\u00de\u0007\u00de\u0002\u00df\u0007\u00df\u0002\u00e0\u0007"+
		"\u00e0\u0002\u00e1\u0007\u00e1\u0002\u00e2\u0007\u00e2\u0002\u00e3\u0007"+
		"\u00e3\u0002\u00e4\u0007\u00e4\u0002\u00e5\u0007\u00e5\u0002\u00e6\u0007"+
		"\u00e6\u0002\u00e7\u0007\u00e7\u0002\u00e8\u0007\u00e8\u0002\u00e9\u0007"+
		"\u00e9\u0002\u00ea\u0007\u00ea\u0002\u00eb\u0007\u00eb\u0002\u00ec\u0007"+
		"\u00ec\u0002\u00ed\u0007\u00ed\u0002\u00ee\u0007\u00ee\u0002\u00ef\u0007"+
		"\u00ef\u0002\u00f0\u0007\u00f0\u0002\u00f1\u0007\u00f1\u0002\u00f2\u0007"+
		"\u00f2\u0002\u00f3\u0007\u00f3\u0002\u00f4\u0007\u00f4\u0002\u00f5\u0007"+
		"\u00f5\u0002\u00f6\u0007\u00f6\u0002\u00f7\u0007\u00f7\u0002\u00f8\u0007"+
		"\u00f8\u0002\u00f9\u0007\u00f9\u0002\u00fa\u0007\u00fa\u0002\u00fb\u0007"+
		"\u00fb\u0002\u00fc\u0007\u00fc\u0002\u00fd\u0007\u00fd\u0002\u00fe\u0007"+
		"\u00fe\u0002\u00ff\u0007\u00ff\u0002\u0100\u0007\u0100\u0002\u0101\u0007"+
		"\u0101\u0002\u0102\u0007\u0102\u0002\u0103\u0007\u0103\u0002\u0104\u0007"+
		"\u0104\u0002\u0105\u0007\u0105\u0002\u0106\u0007\u0106\u0002\u0107\u0007"+
		"\u0107\u0002\u0108\u0007\u0108\u0001\u0000\u0003\u0000\u0214\b\u0000\u0001"+
		"\u0000\u0003\u0000\u0217\b\u0000\u0001\u0000\u0003\u0000\u021a\b\u0000"+
		"\u0001\u0000\u0001\u0000\u0003\u0000\u021e\b\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002"+
		"\u0227\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007"+
		"\u023f\b\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u0243\b\u0007\n\u0007"+
		"\f\u0007\u0246\t\u0007\u0001\u0007\u0003\u0007\u0249\b\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0005\u0007\u024e\b\u0007\n\u0007\f\u0007\u0251"+
		"\t\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u0257\b\b\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0003\n\u0267\b\n\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0003\u0014\u0293\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005"+
		"\u0014\u0298\b\u0014\n\u0014\f\u0014\u029b\t\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0003\u0015\u02a0\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0005\u0015\u02a7\b\u0015\n\u0015\f\u0015\u02aa"+
		"\t\u0015\u0003\u0015\u02ac\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u02b5\b\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0003\u0017\u02bd\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0005\u0017\u02c4\b\u0017\n\u0017\f\u0017\u02c7\t\u0017\u0003"+
		"\u0017\u02c9\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u02d7\b\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u02de\b\u0019\u0003\u0019\u02e0"+
		"\b\u0019\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u02eb\b\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u02f2"+
		"\b\u001c\u0003\u001c\u02f4\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u02fc\b\u001d\u0001\u001d"+
		"\u0001\u001d\u0003\u001d\u0300\b\u001d\u0001\u001d\u0001\u001d\u0003\u001d"+
		"\u0304\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u0309\b"+
		"\u001e\n\u001e\f\u001e\u030c\t\u001e\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0003\u001f\u0311\b\u001f\u0001 \u0005 \u0314\b \n \f \u0317\t \u0001"+
		"!\u0001!\u0001!\u0001!\u0001!\u0001!\u0003!\u031f\b!\u0001\"\u0001\"\u0001"+
		"\"\u0005\"\u0324\b\"\n\"\f\"\u0327\t\"\u0001#\u0001#\u0001$\u0001$\u0001"+
		"$\u0001%\u0001%\u0001%\u0001%\u0001%\u0001&\u0005&\u0334\b&\n&\f&\u0337"+
		"\t&\u0001\'\u0001\'\u0001\'\u0001(\u0001(\u0003(\u033e\b(\u0001)\u0001"+
		")\u0001)\u0005)\u0343\b)\n)\f)\u0346\t)\u0001*\u0001*\u0001*\u0001*\u0001"+
		"*\u0001*\u0003*\u034e\b*\u0001+\u0001+\u0001+\u0003+\u0353\b+\u0001,\u0001"+
		",\u0001,\u0001,\u0001-\u0001-\u0005-\u035b\b-\n-\f-\u035e\t-\u0001-\u0001"+
		"-\u0001.\u0001.\u0001.\u0003.\u0365\b.\u0001/\u0001/\u0001/\u0001/\u0001"+
		"/\u0003/\u036c\b/\u00010\u00010\u00010\u00010\u00050\u0372\b0\n0\f0\u0375"+
		"\t0\u00011\u00011\u00011\u00031\u037a\b1\u00011\u00031\u037d\b1\u0001"+
		"1\u00031\u0380\b1\u00011\u00011\u00011\u00012\u00012\u00012\u00013\u0001"+
		"3\u00013\u00013\u00014\u00014\u00014\u00014\u00054\u0390\b4\n4\f4\u0393"+
		"\t4\u00015\u00015\u00015\u00035\u0398\b5\u00015\u00015\u00015\u00016\u0001"+
		"6\u00016\u00036\u03a0\b6\u00017\u00017\u00017\u00017\u00017\u00037\u03a7"+
		"\b7\u00017\u00017\u00017\u00017\u00037\u03ad\b7\u00018\u00018\u00018\u0001"+
		"8\u00018\u00038\u03b4\b8\u00018\u00018\u00018\u00018\u00018\u00019\u0001"+
		"9\u00019\u00019\u00019\u0001:\u0003:\u03c1\b:\u0001:\u0001:\u0001:\u0001"+
		":\u0001:\u0001;\u0001;\u0003;\u03ca\b;\u0001;\u0003;\u03cd\b;\u0001;\u0001"+
		";\u0001;\u0003;\u03d2\b;\u0001;\u0001;\u0001;\u0003;\u03d7\b;\u0001<\u0001"+
		"<\u0001<\u0001<\u0001=\u0001=\u0001=\u0001>\u0001>\u0001>\u0001>\u0001"+
		"?\u0001?\u0001?\u0005?\u03e7\b?\n?\f?\u03ea\t?\u0001@\u0001@\u0001@\u0003"+
		"@\u03ef\b@\u0001@\u0001@\u0003@\u03f3\b@\u0001@\u0001@\u0003@\u03f7\b"+
		"@\u0001A\u0003A\u03fa\bA\u0001A\u0001A\u0001A\u0001A\u0001A\u0005A\u0401"+
		"\bA\nA\fA\u0404\tA\u0001B\u0001B\u0001B\u0003B\u0409\bB\u0001B\u0001B"+
		"\u0001B\u0003B\u040e\bB\u0003B\u0410\bB\u0001B\u0001B\u0003B\u0414\bB"+
		"\u0001C\u0001C\u0001C\u0001D\u0001D\u0003D\u041b\bD\u0001D\u0001D\u0001"+
		"D\u0005D\u0420\bD\nD\fD\u0423\tD\u0001D\u0001D\u0001D\u0001E\u0001E\u0001"+
		"E\u0003E\u042b\bE\u0001E\u0001E\u0001E\u0001F\u0001F\u0001F\u0001F\u0001"+
		"F\u0004F\u0435\bF\u000bF\fF\u0436\u0001F\u0001F\u0001F\u0001F\u0001G\u0001"+
		"G\u0004G\u043f\bG\u000bG\fG\u0440\u0001G\u0001G\u0001G\u0001H\u0001H\u0001"+
		"I\u0001I\u0001I\u0001I\u0001I\u0004I\u044d\bI\u000bI\fI\u044e\u0001I\u0001"+
		"I\u0001I\u0003I\u0454\bI\u0001I\u0001I\u0001I\u0001J\u0001J\u0001J\u0001"+
		"J\u0001J\u0003J\u045e\bJ\u0001J\u0001J\u0001J\u0001J\u0001K\u0001K\u0001"+
		"K\u0005K\u0467\bK\nK\fK\u046a\tK\u0001L\u0001L\u0001L\u0001L\u0001L\u0001"+
		"L\u0001L\u0001L\u0001L\u0001M\u0001M\u0004M\u0477\bM\u000bM\fM\u0478\u0001"+
		"N\u0001N\u0001N\u0001O\u0001O\u0001P\u0001P\u0001P\u0001P\u0001P\u0001"+
		"P\u0001P\u0003P\u0487\bP\u0001P\u0001P\u0001Q\u0001Q\u0003Q\u048d\bQ\u0001"+
		"Q\u0001Q\u0001R\u0001R\u0001R\u0005R\u0494\bR\nR\fR\u0497\tR\u0001S\u0001"+
		"S\u0001S\u0001S\u0001S\u0001S\u0003S\u049f\bS\u0001T\u0001T\u0001T\u0001"+
		"T\u0001T\u0001U\u0001U\u0001U\u0001U\u0001U\u0001V\u0001V\u0001V\u0001"+
		"V\u0001V\u0001W\u0001W\u0001W\u0001X\u0001X\u0001X\u0001X\u0001X\u0001"+
		"Y\u0001Y\u0001Y\u0005Y\u04bb\bY\nY\fY\u04be\tY\u0001Z\u0001Z\u0001Z\u0005"+
		"Z\u04c3\bZ\nZ\fZ\u04c6\tZ\u0001[\u0001[\u0001[\u0001[\u0003[\u04cc\b["+
		"\u0001[\u0001[\u0003[\u04d0\b[\u0001\\\u0001\\\u0001\\\u0005\\\u04d5\b"+
		"\\\n\\\f\\\u04d8\t\\\u0001]\u0001]\u0001]\u0003]\u04dd\b]\u0001^\u0001"+
		"^\u0001^\u0005^\u04e2\b^\n^\f^\u04e5\t^\u0001_\u0001_\u0001_\u0005_\u04ea"+
		"\b_\n_\f_\u04ed\t_\u0001`\u0001`\u0001`\u0005`\u04f2\b`\n`\f`\u04f5\t"+
		"`\u0001a\u0001a\u0001a\u0005a\u04fa\ba\na\fa\u04fd\ta\u0001b\u0001b\u0001"+
		"b\u0001b\u0003b\u0503\bb\u0001c\u0001c\u0001c\u0001c\u0003c\u0509\bc\u0001"+
		"d\u0001d\u0001d\u0001d\u0003d\u050f\bd\u0001e\u0001e\u0001e\u0001e\u0003"+
		"e\u0515\be\u0001f\u0001f\u0001f\u0005f\u051a\bf\nf\ff\u051d\tf\u0001g"+
		"\u0001g\u0001g\u0001h\u0005h\u0523\bh\nh\fh\u0526\th\u0001h\u0001h\u0001"+
		"i\u0001i\u0001i\u0003i\u052d\bi\u0001j\u0001j\u0001j\u0001j\u0001j\u0001"+
		"j\u0001j\u0001j\u0003j\u0537\bj\u0001k\u0001k\u0001l\u0001l\u0001l\u0001"+
		"l\u0001l\u0003l\u0540\bl\u0001m\u0001m\u0001m\u0001m\u0003m\u0546\bm\u0001"+
		"m\u0001m\u0001n\u0001n\u0001o\u0004o\u054d\bo\u000bo\fo\u054e\u0001o\u0001"+
		"o\u0001o\u0001o\u0001p\u0001p\u0001p\u0005p\u0558\bp\np\fp\u055b\tp\u0001"+
		"q\u0001q\u0001q\u0001q\u0001q\u0001q\u0001q\u0001q\u0001q\u0001q\u0001"+
		"q\u0001q\u0001q\u0003q\u056a\bq\u0001r\u0001r\u0001r\u0001s\u0001s\u0001"+
		"s\u0001s\u0001s\u0001s\u0001t\u0001t\u0001t\u0001t\u0001u\u0001u\u0001"+
		"u\u0001u\u0001v\u0001v\u0001v\u0001v\u0001w\u0001w\u0001w\u0001w\u0001"+
		"w\u0001x\u0001x\u0005x\u0588\bx\nx\fx\u058b\tx\u0001x\u0001x\u0001y\u0001"+
		"y\u0001y\u0001z\u0001z\u0001z\u0001z\u0001z\u0001z\u0001z\u0001z\u0001"+
		"z\u0001{\u0001{\u0001{\u0001{\u0001{\u0004{\u05a0\b{\u000b{\f{\u05a1\u0001"+
		"{\u0001{\u0001{\u0001{\u0001|\u0001|\u0004|\u05aa\b|\u000b|\f|\u05ab\u0001"+
		"|\u0001|\u0001|\u0001}\u0001}\u0001}\u0001}\u0001}\u0001}\u0004}\u05b7"+
		"\b}\u000b}\f}\u05b8\u0001~\u0001~\u0001~\u0001~\u0001~\u0004~\u05c0\b"+
		"~\u000b~\f~\u05c1\u0001~\u0001~\u0001~\u0003~\u05c7\b~\u0001~\u0001~\u0001"+
		"~\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0003\u007f"+
		"\u05d1\b\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u007f\u0001\u0080"+
		"\u0005\u0080\u05d8\b\u0080\n\u0080\f\u0080\u05db\t\u0080\u0001\u0080\u0001"+
		"\u0080\u0001\u0080\u0001\u0080\u0003\u0080\u05e1\b\u0080\u0001\u0080\u0001"+
		"\u0080\u0003\u0080\u05e5\b\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0001"+
		"\u0080\u0003\u0080\u05eb\b\u0080\u0001\u0080\u0001\u0080\u0003\u0080\u05ef"+
		"\b\u0080\u0005\u0080\u05f1\b\u0080\n\u0080\f\u0080\u05f4\t\u0080\u0001"+
		"\u0080\u0001\u0080\u0001\u0081\u0001\u0081\u0001\u0081\u0001\u0081\u0001"+
		"\u0081\u0001\u0081\u0001\u0082\u0001\u0082\u0003\u0082\u0600\b\u0082\u0001"+
		"\u0082\u0001\u0082\u0001\u0082\u0003\u0082\u0605\b\u0082\u0001\u0083\u0001"+
		"\u0083\u0001\u0083\u0005\u0083\u060a\b\u0083\n\u0083\f\u0083\u060d\t\u0083"+
		"\u0001\u0084\u0001\u0084\u0003\u0084\u0611\b\u0084\u0001\u0085\u0001\u0085"+
		"\u0003\u0085\u0615\b\u0085\u0001\u0085\u0001\u0085\u0001\u0086\u0001\u0086"+
		"\u0001\u0086\u0001\u0086\u0003\u0086\u061d\b\u0086\u0001\u0087\u0001\u0087"+
		"\u0001\u0087\u0001\u0087\u0001\u0088\u0003\u0088\u0624\b\u0088\u0001\u0088"+
		"\u0001\u0088\u0001\u0089\u0001\u0089\u0001\u0089\u0001\u0089\u0003\u0089"+
		"\u062c\b\u0089\u0001\u008a\u0001\u008a\u0001\u008a\u0001\u008a\u0001\u008b"+
		"\u0001\u008b\u0001\u008c\u0001\u008c\u0003\u008c\u0636\b\u008c\u0001\u008d"+
		"\u0001\u008d\u0003\u008d\u063a\b\u008d\u0001\u008e\u0001\u008e\u0001\u008e"+
		"\u0003\u008e\u063f\b\u008e\u0001\u008f\u0001\u008f\u0001\u008f\u0001\u008f"+
		"\u0005\u008f\u0645\b\u008f\n\u008f\f\u008f\u0648\t\u008f\u0001\u0090\u0001"+
		"\u0090\u0001\u0090\u0001\u0090\u0005\u0090\u064e\b\u0090\n\u0090\f\u0090"+
		"\u0651\t\u0090\u0003\u0090\u0653\b\u0090\u0001\u0090\u0001\u0090\u0001"+
		"\u0091\u0005\u0091\u0658\b\u0091\n\u0091\f\u0091\u065b\t\u0091\u0001\u0092"+
		"\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0093\u0001\u0093\u0001\u0093"+
		"\u0001\u0094\u0001\u0094\u0001\u0094\u0001\u0094\u0003\u0094\u0668\b\u0094"+
		"\u0001\u0095\u0001\u0095\u0001\u0095\u0003\u0095\u066d\b\u0095\u0001\u0096"+
		"\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096"+
		"\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096"+
		"\u0001\u0096\u0003\u0096\u067d\b\u0096\u0001\u0097\u0001\u0097\u0003\u0097"+
		"\u0681\b\u0097\u0001\u0098\u0001\u0098\u0001\u0099\u0001\u0099\u0001\u0099"+
		"\u0001\u009a\u0001\u009a\u0001\u009b\u0001\u009b\u0003\u009b\u068c\b\u009b"+
		"\u0001\u009b\u0001\u009b\u0001\u009c\u0001\u009c\u0001\u009d\u0001\u009d"+
		"\u0001\u009d\u0001\u009e\u0001\u009e\u0001\u009e\u0001\u009f\u0001\u009f"+
		"\u0001\u009f\u0001\u00a0\u0001\u00a0\u0003\u00a0\u069d\b\u00a0\u0001\u00a1"+
		"\u0001\u00a1\u0003\u00a1\u06a1\b\u00a1\u0001\u00a2\u0001\u00a2\u0001\u00a2"+
		"\u0003\u00a2\u06a6\b\u00a2\u0001\u00a3\u0001\u00a3\u0001\u00a3\u0001\u00a3"+
		"\u0001\u00a3\u0005\u00a3\u06ad\b\u00a3\n\u00a3\f\u00a3\u06b0\t\u00a3\u0001"+
		"\u00a3\u0001\u00a3\u0001\u00a3\u0001\u00a3\u0001\u00a3\u0001\u00a4\u0001"+
		"\u00a4\u0001\u00a4\u0001\u00a4\u0001\u00a4\u0001\u00a4\u0001\u00a5\u0001"+
		"\u00a5\u0001\u00a5\u0001\u00a5\u0005\u00a5\u06c1\b\u00a5\n\u00a5\f\u00a5"+
		"\u06c4\t\u00a5\u0001\u00a6\u0001\u00a6\u0001\u00a6\u0001\u00a6\u0001\u00a6"+
		"\u0005\u00a6\u06cb\b\u00a6\n\u00a6\f\u00a6\u06ce\t\u00a6\u0001\u00a6\u0001"+
		"\u00a6\u0001\u00a7\u0001\u00a7\u0001\u00a7\u0001\u00a7\u0001\u00a7\u0005"+
		"\u00a7\u06d7\b\u00a7\n\u00a7\f\u00a7\u06da\t\u00a7\u0001\u00a7\u0001\u00a7"+
		"\u0001\u00a8\u0001\u00a8\u0003\u00a8\u06e0\b\u00a8\u0001\u00a9\u0004\u00a9"+
		"\u06e3\b\u00a9\u000b\u00a9\f\u00a9\u06e4\u0001\u00a9\u0001\u00a9\u0001"+
		"\u00a9\u0001\u00a9\u0001\u00a9\u0003\u00a9\u06ec\b\u00a9\u0001\u00a9\u0003"+
		"\u00a9\u06ef\b\u00a9\u0001\u00aa\u0004\u00aa\u06f2\b\u00aa\u000b\u00aa"+
		"\f\u00aa\u06f3\u0001\u00aa\u0001\u00aa\u0001\u00aa\u0001\u00aa\u0001\u00aa"+
		"\u0003\u00aa\u06fb\b\u00aa\u0001\u00aa\u0003\u00aa\u06fe\b\u00aa\u0001"+
		"\u00ab\u0001\u00ab\u0001\u00ab\u0001\u00ab\u0001\u00ab\u0001\u00ab\u0003"+
		"\u00ab\u0706\b\u00ab\u0001\u00ac\u0001\u00ac\u0001\u00ac\u0001\u00ac\u0001"+
		"\u00ac\u0001\u00ac\u0003\u00ac\u070e\b\u00ac\u0001\u00ad\u0001\u00ad\u0001"+
		"\u00ae\u0001\u00ae\u0001\u00ae\u0001\u00ae\u0001\u00ae\u0001\u00ae\u0001"+
		"\u00ae\u0001\u00ae\u0003\u00ae\u071a\b\u00ae\u0001\u00af\u0001\u00af\u0001"+
		"\u00af\u0001\u00af\u0001\u00af\u0001\u00af\u0003\u00af\u0722\b\u00af\u0001"+
		"\u00b0\u0001\u00b0\u0001\u00b0\u0001\u00b1\u0001\u00b1\u0001\u00b1\u0001"+
		"\u00b1\u0001\u00b1\u0001\u00b1\u0001\u00b1\u0001\u00b1\u0001\u00b1\u0001"+
		"\u00b1\u0005\u00b1\u0731\b\u00b1\n\u00b1\f\u00b1\u0734\t\u00b1\u0003\u00b1"+
		"\u0736\b\u00b1\u0001\u00b1\u0001\u00b1\u0001\u00b2\u0001\u00b2\u0001\u00b2"+
		"\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b4"+
		"\u0001\u00b4\u0001\u00b4\u0001\u00b4\u0001\u00b5\u0001\u00b5\u0001\u00b5"+
		"\u0001\u00b6\u0001\u00b6\u0001\u00b6\u0001\u00b7\u0001\u00b7\u0001\u00b7"+
		"\u0001\u00b7\u0001\u00b7\u0001\u00b7\u0003\u00b7\u0752\b\u00b7\u0001\u00b7"+
		"\u0001\u00b7\u0001\u00b8\u0001\u00b8\u0001\u00b9\u0001\u00b9\u0001\u00b9"+
		"\u0001\u00b9\u0001\u00b9\u0001\u00b9\u0003\u00b9\u075e\b\u00b9\u0001\u00b9"+
		"\u0001\u00b9\u0001\u00b9\u0003\u00b9\u0763\b\u00b9\u0001\u00ba\u0001\u00ba"+
		"\u0001\u00ba\u0003\u00ba\u0768\b\u00ba\u0001\u00ba\u0001\u00ba\u0001\u00bb"+
		"\u0001\u00bb\u0001\u00bc\u0001\u00bc\u0001\u00bd\u0001\u00bd\u0001\u00be"+
		"\u0001\u00be\u0001\u00be\u0001\u00bf\u0001\u00bf\u0001\u00bf\u0001\u00c0"+
		"\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0003\u00c0"+
		"\u077e\b\u00c0\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0003\u00c0\u0783\b"+
		"\u00c0\u0001\u00c1\u0001\u00c1\u0003\u00c1\u0787\b\u00c1\u0001\u00c2\u0001"+
		"\u00c2\u0001\u00c2\u0001\u00c2\u0001\u00c3\u0001\u00c3\u0001\u00c3\u0001"+
		"\u00c3\u0003\u00c3\u0791\b\u00c3\u0001\u00c3\u0001\u00c3\u0001\u00c3\u0003"+
		"\u00c3\u0796\b\u00c3\u0001\u00c3\u0001\u00c3\u0001\u00c4\u0001\u00c4\u0001"+
		"\u00c4\u0001\u00c4\u0001\u00c5\u0001\u00c5\u0001\u00c5\u0001\u00c5\u0001"+
		"\u00c5\u0005\u00c5\u07a3\b\u00c5\n\u00c5\f\u00c5\u07a6\t\u00c5\u0003\u00c5"+
		"\u07a8\b\u00c5\u0001\u00c5\u0001\u00c5\u0001\u00c6\u0001\u00c6\u0001\u00c6"+
		"\u0001\u00c6\u0001\u00c7\u0001\u00c7\u0003\u00c7\u07b2\b\u00c7\u0001\u00c8"+
		"\u0001\u00c8\u0003\u00c8\u07b6\b\u00c8\u0001\u00c8\u0001\u00c8\u0001\u00c9"+
		"\u0001\u00c9\u0001\u00c9\u0001\u00ca\u0001\u00ca\u0001\u00ca\u0001\u00ca"+
		"\u0001\u00cb\u0001\u00cb\u0001\u00cb\u0001\u00cb\u0005\u00cb\u07c5\b\u00cb"+
		"\n\u00cb\f\u00cb\u07c8\t\u00cb\u0001\u00cc\u0001\u00cc\u0001\u00cd\u0001"+
		"\u00cd\u0001\u00ce\u0001\u00ce\u0001\u00cf\u0001\u00cf\u0001\u00cf\u0001"+
		"\u00cf\u0001\u00cf\u0001\u00cf\u0001\u00cf\u0001\u00cf\u0001\u00cf\u0001"+
		"\u00cf\u0005\u00cf\u07da\b\u00cf\n\u00cf\f\u00cf\u07dd\t\u00cf\u0001\u00d0"+
		"\u0001\u00d0\u0001\u00d0\u0001\u00d0\u0001\u00d1\u0001\u00d1\u0001\u00d1"+
		"\u0001\u00d2\u0001\u00d2\u0003\u00d2\u07e8\b\u00d2\u0001\u00d3\u0001\u00d3"+
		"\u0001\u00d3\u0001\u00d4\u0001\u00d4\u0001\u00d4\u0001\u00d4\u0001\u00d4"+
		"\u0001\u00d4\u0001\u00d4\u0003\u00d4\u07f4\b\u00d4\u0003\u00d4\u07f6\b"+
		"\u00d4\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0001"+
		"\u00d5\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0003\u00d5\u0801\b\u00d5\u0001"+
		"\u00d6\u0001\u00d6\u0001\u00d7\u0001\u00d7\u0001\u00d7\u0001\u00d7\u0001"+
		"\u00d7\u0001\u00d7\u0001\u00d7\u0001\u00d7\u0001\u00d7\u0001\u00d7\u0001"+
		"\u00d7\u0001\u00d7\u0003\u00d7\u0811\b\u00d7\u0001\u00d8\u0001\u00d8\u0001"+
		"\u00d8\u0003\u00d8\u0816\b\u00d8\u0001\u00d8\u0001\u00d8\u0001\u00d9\u0001"+
		"\u00d9\u0001\u00d9\u0001\u00d9\u0001\u00da\u0001\u00da\u0001\u00da\u0001"+
		"\u00da\u0003\u00da\u0822\b\u00da\u0001\u00da\u0001\u00da\u0001\u00db\u0001"+
		"\u00db\u0001\u00db\u0001\u00db\u0001\u00dc\u0001\u00dc\u0001\u00dc\u0001"+
		"\u00dc\u0001\u00dd\u0001\u00dd\u0001\u00dd\u0001\u00dd\u0001\u00de\u0001"+
		"\u00de\u0001\u00de\u0001\u00de\u0003\u00de\u0836\b\u00de\u0001\u00de\u0001"+
		"\u00de\u0001\u00df\u0001\u00df\u0001\u00df\u0001\u00df\u0001\u00df\u0003"+
		"\u00df\u083f\b\u00df\u0003\u00df\u0841\b\u00df\u0001\u00df\u0001\u00df"+
		"\u0001\u00e0\u0001\u00e0\u0003\u00e0\u0847\b\u00e0\u0001\u00e1\u0001\u00e1"+
		"\u0001\u00e1\u0001\u00e1\u0001\u00e1\u0001\u00e2\u0001\u00e2\u0001\u00e2"+
		"\u0001\u00e2\u0001\u00e2\u0001\u00e2\u0003\u00e2\u0854\b\u00e2\u0003\u00e2"+
		"\u0856\b\u00e2\u0003\u00e2\u0858\b\u00e2\u0001\u00e2\u0001\u00e2\u0001"+
		"\u00e3\u0001\u00e3\u0003\u00e3\u085e\b\u00e3\u0001\u00e4\u0001\u00e4\u0001"+
		"\u00e4\u0001\u00e4\u0001\u00e4\u0001\u00e5\u0001\u00e5\u0001\u00e6\u0001"+
		"\u00e6\u0001\u00e7\u0001\u00e7\u0001\u00e8\u0001\u00e8\u0001\u00e9\u0001"+
		"\u00e9\u0001\u00ea\u0005\u00ea\u0870\b\u00ea\n\u00ea\f\u00ea\u0873\t\u00ea"+
		"\u0001\u00ea\u0001\u00ea\u0003\u00ea\u0877\b\u00ea\u0001\u00eb\u0001\u00eb"+
		"\u0001\u00eb\u0001\u00eb\u0001\u00eb\u0001\u00ec\u0001\u00ec\u0001\u00ec"+
		"\u0001\u00ec\u0001\u00ec\u0005\u00ec\u0883\b\u00ec\n\u00ec\f\u00ec\u0886"+
		"\t\u00ec\u0003\u00ec\u0888\b\u00ec\u0001\u00ec\u0001\u00ec\u0001\u00ec"+
		"\u0001\u00ec\u0001\u00ed\u0001\u00ed\u0003\u00ed\u0890\b\u00ed\u0001\u00ee"+
		"\u0001\u00ee\u0001\u00ee\u0001\u00ee\u0001\u00ee\u0001\u00ef\u0001\u00ef"+
		"\u0001\u00ef\u0001\u00ef\u0001\u00ef\u0001\u00ef\u0001\u00ef\u0001\u00f0"+
		"\u0001\u00f0\u0003\u00f0\u08a0\b\u00f0\u0001\u00f1\u0001\u00f1\u0001\u00f1"+
		"\u0001\u00f1\u0001\u00f1\u0001\u00f2\u0001\u00f2\u0001\u00f2\u0001\u00f2"+
		"\u0001\u00f2\u0001\u00f3\u0001\u00f3\u0001\u00f3\u0001\u00f3\u0001\u00f4"+
		"\u0001\u00f4\u0001\u00f5\u0001\u00f5\u0001\u00f5\u0001\u00f5\u0001\u00f5"+
		"\u0003\u00f5\u08b7\b\u00f5\u0001\u00f6\u0001\u00f6\u0001\u00f6\u0003\u00f6"+
		"\u08bc\b\u00f6\u0001\u00f6\u0001\u00f6\u0001\u00f7\u0001\u00f7\u0001\u00f7"+
		"\u0003\u00f7\u08c3\b\u00f7\u0001\u00f7\u0001\u00f7\u0001\u00f8\u0001\u00f8"+
		"\u0001\u00f8\u0003\u00f8\u08ca\b\u00f8\u0001\u00f8\u0001\u00f8\u0001\u00f9"+
		"\u0001\u00f9\u0001\u00f9\u0003\u00f9\u08d1\b\u00f9\u0001\u00f9\u0001\u00f9"+
		"\u0001\u00fa\u0001\u00fa\u0001\u00fa\u0003\u00fa\u08d8\b\u00fa\u0001\u00fa"+
		"\u0001\u00fa\u0001\u00fb\u0001\u00fb\u0003\u00fb\u08de\b\u00fb\u0001\u00fc"+
		"\u0001\u00fc\u0003\u00fc\u08e2\b\u00fc\u0001\u00fd\u0001\u00fd\u0003\u00fd"+
		"\u08e6\b\u00fd\u0001\u00fe\u0001\u00fe\u0001\u00fe\u0001\u00fe\u0003\u00fe"+
		"\u08ec\b\u00fe\u0001\u00ff\u0001\u00ff\u0003\u00ff\u08f0\b\u00ff\u0001"+
		"\u0100\u0001\u0100\u0001\u0101\u0001\u0101\u0001\u0102\u0001\u0102\u0001"+
		"\u0103\u0001\u0103\u0001\u0103\u0001\u0103\u0001\u0103\u0005\u0103\u08fd"+
		"\b\u0103\n\u0103\f\u0103\u0900\t\u0103\u0001\u0103\u0001\u0103\u0001\u0104"+
		"\u0001\u0104\u0001\u0104\u0001\u0104\u0001\u0104\u0005\u0104\u0909\b\u0104"+
		"\n\u0104\f\u0104\u090c\t\u0104\u0001\u0104\u0001\u0104\u0001\u0105\u0001"+
		"\u0105\u0003\u0105\u0912\b\u0105\u0001\u0106\u0004\u0106\u0915\b\u0106"+
		"\u000b\u0106\f\u0106\u0916\u0001\u0106\u0001\u0106\u0003\u0106\u091b\b"+
		"\u0106\u0001\u0106\u0003\u0106\u091e\b\u0106\u0001\u0106\u0001\u0106\u0001"+
		"\u0106\u0001\u0106\u0001\u0106\u0003\u0106\u0925\b\u0106\u0001\u0107\u0004"+
		"\u0107\u0928\b\u0107\u000b\u0107\f\u0107\u0929\u0001\u0107\u0001\u0107"+
		"\u0003\u0107\u092e\b\u0107\u0001\u0107\u0003\u0107\u0931\b\u0107\u0001"+
		"\u0107\u0001\u0107\u0001\u0107\u0001\u0107\u0001\u0107\u0003\u0107\u0938"+
		"\b\u0107\u0001\u0108\u0001\u0108\u0004\u0108\u093c\b\u0108\u000b\u0108"+
		"\f\u0108\u093d\u0001\u0108\u0000\u0000\u0109\u0000\u0002\u0004\u0006\b"+
		"\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02"+
		"468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088"+
		"\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0"+
		"\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8"+
		"\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0"+
		"\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8"+
		"\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100"+
		"\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118"+
		"\u011a\u011c\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130"+
		"\u0132\u0134\u0136\u0138\u013a\u013c\u013e\u0140\u0142\u0144\u0146\u0148"+
		"\u014a\u014c\u014e\u0150\u0152\u0154\u0156\u0158\u015a\u015c\u015e\u0160"+
		"\u0162\u0164\u0166\u0168\u016a\u016c\u016e\u0170\u0172\u0174\u0176\u0178"+
		"\u017a\u017c\u017e\u0180\u0182\u0184\u0186\u0188\u018a\u018c\u018e\u0190"+
		"\u0192\u0194\u0196\u0198\u019a\u019c\u019e\u01a0\u01a2\u01a4\u01a6\u01a8"+
		"\u01aa\u01ac\u01ae\u01b0\u01b2\u01b4\u01b6\u01b8\u01ba\u01bc\u01be\u01c0"+
		"\u01c2\u01c4\u01c6\u01c8\u01ca\u01cc\u01ce\u01d0\u01d2\u01d4\u01d6\u01d8"+
		"\u01da\u01dc\u01de\u01e0\u01e2\u01e4\u01e6\u01e8\u01ea\u01ec\u01ee\u01f0"+
		"\u01f2\u01f4\u01f6\u01f8\u01fa\u01fc\u01fe\u0200\u0202\u0204\u0206\u0208"+
		"\u020a\u020c\u020e\u0210\u0000\u001b\u0002\u0000VVcc\u0002\u0000\u008a"+
		"\u008a\u0098\u0098\u0002\u0000\u0085\u0085\u00a3\u00a3\u0002\u0000ees"+
		"s\u0002\u0000~~\u008a\u008a\u0002\u0000ll}}\u0003\u0000``\u0088\u0088"+
		"\u00bb\u00bb\u0001\u0000\u001c\u001d\u0004\u0000\u001b\u001bSShhww\u0002"+
		"\u0000\'\'\u00a2\u00a2\u0002\u0000^^nn\u0006\u0000\\\\ddggrruuzz\u0002"+
		"\u0000::\u00a0\u00a0\u0002\u0000qq\u0097\u0097\u0001\u0000$%\u0005\u0000"+
		"==FFOP`a\u0092\u0092\u0002\u000067\u0087\u0089\u0001\u0000\u0005\u0007"+
		"\u0002\u0000\r\r\u000f\u000f\u0001\u0000\t\n\u0001\u0000!\"\u0002\u0000"+
		"\u0018\u0019\u00cb\u00cb\u0003\u0000\u0018\u001822\u00cb\u00cb\u0003\u0000"+
		"\u0019\u001922\u00cb\u00cb\u001c\u0000\b\b5599==@@EEHHJJLLRRUVYY[[iip"+
		"pvv{|\u007f\u007f\u0081\u0081\u008c\u008c\u0090\u0091\u0093\u0093\u0099"+
		"\u009a\u009e\u00a1\u00a4\u00a4\u00a8\u00a8\u00aa\u00aa\u00b2\u00bd\u001a"+
		"\u000068:<>?ADFGIIKKMQSTWXZZ\\hjoquwz}~\u0080\u0080\u0082\u008a\u008d"+
		"\u008f\u0092\u0092\u0094\u0098\u009b\u009d\u00a2\u00a3\u00a5\u00a7\u00a9"+
		"\u00a9\u00ab\u00ab\u000b\u0000\u0005\u0007\r\r\u0011\u0011\u0013\u0018"+
		"\u001b\')244{{\u008b\u008b\u00be\u00c3\u00ca\u00ca\u0996\u0000\u0213\u0001"+
		"\u0000\u0000\u0000\u0002\u021f\u0001\u0000\u0000\u0000\u0004\u0221\u0001"+
		"\u0000\u0000\u0000\u0006\u022a\u0001\u0000\u0000\u0000\b\u022d\u0001\u0000"+
		"\u0000\u0000\n\u022f\u0001\u0000\u0000\u0000\f\u0232\u0001\u0000\u0000"+
		"\u0000\u000e\u0244\u0001\u0000\u0000\u0000\u0010\u0256\u0001\u0000\u0000"+
		"\u0000\u0012\u0258\u0001\u0000\u0000\u0000\u0014\u0266\u0001\u0000\u0000"+
		"\u0000\u0016\u0268\u0001\u0000\u0000\u0000\u0018\u026c\u0001\u0000\u0000"+
		"\u0000\u001a\u0271\u0001\u0000\u0000\u0000\u001c\u0275\u0001\u0000\u0000"+
		"\u0000\u001e\u0279\u0001\u0000\u0000\u0000 \u027d\u0001\u0000\u0000\u0000"+
		"\"\u0283\u0001\u0000\u0000\u0000$\u0289\u0001\u0000\u0000\u0000&\u028b"+
		"\u0001\u0000\u0000\u0000(\u028d\u0001\u0000\u0000\u0000*\u029c\u0001\u0000"+
		"\u0000\u0000,\u02b4\u0001\u0000\u0000\u0000.\u02b6\u0001\u0000\u0000\u0000"+
		"0\u02ca\u0001\u0000\u0000\u00002\u02d0\u0001\u0000\u0000\u00004\u02e1"+
		"\u0001\u0000\u0000\u00006\u02e3\u0001\u0000\u0000\u00008\u02e5\u0001\u0000"+
		"\u0000\u0000:\u02f5\u0001\u0000\u0000\u0000<\u0305\u0001\u0000\u0000\u0000"+
		">\u030d\u0001\u0000\u0000\u0000@\u0315\u0001\u0000\u0000\u0000B\u0318"+
		"\u0001\u0000\u0000\u0000D\u0320\u0001\u0000\u0000\u0000F\u0328\u0001\u0000"+
		"\u0000\u0000H\u032a\u0001\u0000\u0000\u0000J\u032d\u0001\u0000\u0000\u0000"+
		"L\u0335\u0001\u0000\u0000\u0000N\u0338\u0001\u0000\u0000\u0000P\u033b"+
		"\u0001\u0000\u0000\u0000R\u033f\u0001\u0000\u0000\u0000T\u034d\u0001\u0000"+
		"\u0000\u0000V\u0352\u0001\u0000\u0000\u0000X\u0354\u0001\u0000\u0000\u0000"+
		"Z\u0358\u0001\u0000\u0000\u0000\\\u0364\u0001\u0000\u0000\u0000^\u036b"+
		"\u0001\u0000\u0000\u0000`\u036d\u0001\u0000\u0000\u0000b\u0376\u0001\u0000"+
		"\u0000\u0000d\u0384\u0001\u0000\u0000\u0000f\u0387\u0001\u0000\u0000\u0000"+
		"h\u038b\u0001\u0000\u0000\u0000j\u0394\u0001\u0000\u0000\u0000l\u039c"+
		"\u0001\u0000\u0000\u0000n\u03a1\u0001\u0000\u0000\u0000p\u03ae\u0001\u0000"+
		"\u0000\u0000r\u03ba\u0001\u0000\u0000\u0000t\u03c0\u0001\u0000\u0000\u0000"+
		"v\u03c9\u0001\u0000\u0000\u0000x\u03d8\u0001\u0000\u0000\u0000z\u03dc"+
		"\u0001\u0000\u0000\u0000|\u03df\u0001\u0000\u0000\u0000~\u03e3\u0001\u0000"+
		"\u0000\u0000\u0080\u03eb\u0001\u0000\u0000\u0000\u0082\u03f9\u0001\u0000"+
		"\u0000\u0000\u0084\u0405\u0001\u0000\u0000\u0000\u0086\u0415\u0001\u0000"+
		"\u0000\u0000\u0088\u041a\u0001\u0000\u0000\u0000\u008a\u0427\u0001\u0000"+
		"\u0000\u0000\u008c\u042f\u0001\u0000\u0000\u0000\u008e\u043e\u0001\u0000"+
		"\u0000\u0000\u0090\u0445\u0001\u0000\u0000\u0000\u0092\u0447\u0001\u0000"+
		"\u0000\u0000\u0094\u0458\u0001\u0000\u0000\u0000\u0096\u0463\u0001\u0000"+
		"\u0000\u0000\u0098\u046b\u0001\u0000\u0000\u0000\u009a\u0474\u0001\u0000"+
		"\u0000\u0000\u009c\u047a\u0001\u0000\u0000\u0000\u009e\u047d\u0001\u0000"+
		"\u0000\u0000\u00a0\u047f\u0001\u0000\u0000\u0000\u00a2\u048a\u0001\u0000"+
		"\u0000\u0000\u00a4\u0490\u0001\u0000\u0000\u0000\u00a6\u0498\u0001\u0000"+
		"\u0000\u0000\u00a8\u04a0\u0001\u0000\u0000\u0000\u00aa\u04a5\u0001\u0000"+
		"\u0000\u0000\u00ac\u04aa\u0001\u0000\u0000\u0000\u00ae\u04af\u0001\u0000"+
		"\u0000\u0000\u00b0\u04b2\u0001\u0000\u0000\u0000\u00b2\u04b7\u0001\u0000"+
		"\u0000\u0000\u00b4\u04bf\u0001\u0000\u0000\u0000\u00b6\u04c7\u0001\u0000"+
		"\u0000\u0000\u00b8\u04d1\u0001\u0000\u0000\u0000\u00ba\u04d9\u0001\u0000"+
		"\u0000\u0000\u00bc\u04de\u0001\u0000\u0000\u0000\u00be\u04e6\u0001\u0000"+
		"\u0000\u0000\u00c0\u04ee\u0001\u0000\u0000\u0000\u00c2\u04f6\u0001\u0000"+
		"\u0000\u0000\u00c4\u04fe\u0001\u0000\u0000\u0000\u00c6\u0504\u0001\u0000"+
		"\u0000\u0000\u00c8\u050a\u0001\u0000\u0000\u0000\u00ca\u0510\u0001\u0000"+
		"\u0000\u0000\u00cc\u0516\u0001\u0000\u0000\u0000\u00ce\u051e\u0001\u0000"+
		"\u0000\u0000\u00d0\u0524\u0001\u0000\u0000\u0000\u00d2\u052c\u0001\u0000"+
		"\u0000\u0000\u00d4\u0536\u0001\u0000\u0000\u0000\u00d6\u0538\u0001\u0000"+
		"\u0000\u0000\u00d8\u053f\u0001\u0000\u0000\u0000\u00da\u0541\u0001\u0000"+
		"\u0000\u0000\u00dc\u0549\u0001\u0000\u0000\u0000\u00de\u054c\u0001\u0000"+
		"\u0000\u0000\u00e0\u0554\u0001\u0000\u0000\u0000\u00e2\u0569\u0001\u0000"+
		"\u0000\u0000\u00e4\u056b\u0001\u0000\u0000\u0000\u00e6\u056e\u0001\u0000"+
		"\u0000\u0000\u00e8\u0574\u0001\u0000\u0000\u0000\u00ea\u0578\u0001\u0000"+
		"\u0000\u0000\u00ec\u057c\u0001\u0000\u0000\u0000\u00ee\u0580\u0001\u0000"+
		"\u0000\u0000\u00f0\u0585\u0001\u0000\u0000\u0000\u00f2\u058e\u0001\u0000"+
		"\u0000\u0000\u00f4\u0591\u0001\u0000\u0000\u0000\u00f6\u059a\u0001\u0000"+
		"\u0000\u0000\u00f8\u05a9\u0001\u0000\u0000\u0000\u00fa\u05b0\u0001\u0000"+
		"\u0000\u0000\u00fc\u05ba\u0001\u0000\u0000\u0000\u00fe\u05cb\u0001\u0000"+
		"\u0000\u0000\u0100\u05d9\u0001\u0000\u0000\u0000\u0102\u05f7\u0001\u0000"+
		"\u0000\u0000\u0104\u0604\u0001\u0000\u0000\u0000\u0106\u0606\u0001\u0000"+
		"\u0000\u0000\u0108\u0610\u0001\u0000\u0000\u0000\u010a\u0614\u0001\u0000"+
		"\u0000\u0000\u010c\u061c\u0001\u0000\u0000\u0000\u010e\u061e\u0001\u0000"+
		"\u0000\u0000\u0110\u0623\u0001\u0000\u0000\u0000\u0112\u062b\u0001\u0000"+
		"\u0000\u0000\u0114\u062d\u0001\u0000\u0000\u0000\u0116\u0631\u0001\u0000"+
		"\u0000\u0000\u0118\u0635\u0001\u0000\u0000\u0000\u011a\u0639\u0001\u0000"+
		"\u0000\u0000\u011c\u063e\u0001\u0000\u0000\u0000\u011e\u0640\u0001\u0000"+
		"\u0000\u0000\u0120\u0649\u0001\u0000\u0000\u0000\u0122\u0659\u0001\u0000"+
		"\u0000\u0000\u0124\u065c\u0001\u0000\u0000\u0000\u0126\u0660\u0001\u0000"+
		"\u0000\u0000\u0128\u0667\u0001\u0000\u0000\u0000\u012a\u066c\u0001\u0000"+
		"\u0000\u0000\u012c\u067c\u0001\u0000\u0000\u0000\u012e\u0680\u0001\u0000"+
		"\u0000\u0000\u0130\u0682\u0001\u0000\u0000\u0000\u0132\u0684\u0001\u0000"+
		"\u0000\u0000\u0134\u0687\u0001\u0000\u0000\u0000\u0136\u0689\u0001\u0000"+
		"\u0000\u0000\u0138\u068f\u0001\u0000\u0000\u0000\u013a\u0691\u0001\u0000"+
		"\u0000\u0000\u013c\u0694\u0001\u0000\u0000\u0000\u013e\u0697\u0001\u0000"+
		"\u0000\u0000\u0140\u069c\u0001\u0000\u0000\u0000\u0142\u06a0\u0001\u0000"+
		"\u0000\u0000\u0144\u06a5\u0001\u0000\u0000\u0000\u0146\u06a7\u0001\u0000"+
		"\u0000\u0000\u0148\u06b6\u0001\u0000\u0000\u0000\u014a\u06c2\u0001\u0000"+
		"\u0000\u0000\u014c\u06c5\u0001\u0000\u0000\u0000\u014e\u06d1\u0001\u0000"+
		"\u0000\u0000\u0150\u06df\u0001\u0000\u0000\u0000\u0152\u06ee\u0001\u0000"+
		"\u0000\u0000\u0154\u06fd\u0001\u0000\u0000\u0000\u0156\u0705\u0001\u0000"+
		"\u0000\u0000\u0158\u070d\u0001\u0000\u0000\u0000\u015a\u070f\u0001\u0000"+
		"\u0000\u0000\u015c\u0719\u0001\u0000\u0000\u0000\u015e\u0721\u0001\u0000"+
		"\u0000\u0000\u0160\u0723\u0001\u0000\u0000\u0000\u0162\u0726\u0001\u0000"+
		"\u0000\u0000\u0164\u0739\u0001\u0000\u0000\u0000\u0166\u073c\u0001\u0000"+
		"\u0000\u0000\u0168\u0741\u0001\u0000\u0000\u0000\u016a\u0745\u0001\u0000"+
		"\u0000\u0000\u016c\u0748\u0001\u0000\u0000\u0000\u016e\u074b\u0001\u0000"+
		"\u0000\u0000\u0170\u0755\u0001\u0000\u0000\u0000\u0172\u0757\u0001\u0000"+
		"\u0000\u0000\u0174\u0764\u0001\u0000\u0000\u0000\u0176\u076b\u0001\u0000"+
		"\u0000\u0000\u0178\u076d\u0001\u0000\u0000\u0000\u017a\u076f\u0001\u0000"+
		"\u0000\u0000\u017c\u0771\u0001\u0000\u0000\u0000\u017e\u0774\u0001\u0000"+
		"\u0000\u0000\u0180\u0777\u0001\u0000\u0000\u0000\u0182\u0786\u0001\u0000"+
		"\u0000\u0000\u0184\u0788\u0001\u0000\u0000\u0000\u0186\u078c\u0001\u0000"+
		"\u0000\u0000\u0188\u0799\u0001\u0000\u0000\u0000\u018a\u079d\u0001\u0000"+
		"\u0000\u0000\u018c\u07ab\u0001\u0000\u0000\u0000\u018e\u07b1\u0001\u0000"+
		"\u0000\u0000\u0190\u07b3\u0001\u0000\u0000\u0000\u0192\u07b9\u0001\u0000"+
		"\u0000\u0000\u0194\u07bc\u0001\u0000\u0000\u0000\u0196\u07c0\u0001\u0000"+
		"\u0000\u0000\u0198\u07c9\u0001\u0000\u0000\u0000\u019a\u07cb\u0001\u0000"+
		"\u0000\u0000\u019c\u07cd\u0001\u0000\u0000\u0000\u019e\u07db\u0001\u0000"+
		"\u0000\u0000\u01a0\u07de\u0001\u0000\u0000\u0000\u01a2\u07e2\u0001\u0000"+
		"\u0000\u0000\u01a4\u07e5\u0001\u0000\u0000\u0000\u01a6\u07e9\u0001\u0000"+
		"\u0000\u0000\u01a8\u07f5\u0001\u0000\u0000\u0000\u01aa\u0800\u0001\u0000"+
		"\u0000\u0000\u01ac\u0802\u0001\u0000\u0000\u0000\u01ae\u0810\u0001\u0000"+
		"\u0000\u0000\u01b0\u0812\u0001\u0000\u0000\u0000\u01b2\u0819\u0001\u0000"+
		"\u0000\u0000\u01b4\u081d\u0001\u0000\u0000\u0000\u01b6\u0825\u0001\u0000"+
		"\u0000\u0000\u01b8\u0829\u0001\u0000\u0000\u0000\u01ba\u082d\u0001\u0000"+
		"\u0000\u0000\u01bc\u0831\u0001\u0000\u0000\u0000\u01be\u0839\u0001\u0000"+
		"\u0000\u0000\u01c0\u0846\u0001\u0000\u0000\u0000\u01c2\u0848\u0001\u0000"+
		"\u0000\u0000\u01c4\u084d\u0001\u0000\u0000\u0000\u01c6\u085d\u0001\u0000"+
		"\u0000\u0000\u01c8\u085f\u0001\u0000\u0000\u0000\u01ca\u0864\u0001\u0000"+
		"\u0000\u0000\u01cc\u0866\u0001\u0000\u0000\u0000\u01ce\u0868\u0001\u0000"+
		"\u0000\u0000\u01d0\u086a\u0001\u0000\u0000\u0000\u01d2\u086c\u0001\u0000"+
		"\u0000\u0000\u01d4\u0871\u0001\u0000\u0000\u0000\u01d6\u0878\u0001\u0000"+
		"\u0000\u0000\u01d8\u087d\u0001\u0000\u0000\u0000\u01da\u088f\u0001\u0000"+
		"\u0000\u0000\u01dc\u0891\u0001\u0000\u0000\u0000\u01de\u0896\u0001\u0000"+
		"\u0000\u0000\u01e0\u089f\u0001\u0000\u0000\u0000\u01e2\u08a1\u0001\u0000"+
		"\u0000\u0000\u01e4\u08a6\u0001\u0000\u0000\u0000\u01e6\u08ab\u0001\u0000"+
		"\u0000\u0000\u01e8\u08af\u0001\u0000\u0000\u0000\u01ea\u08b6\u0001\u0000"+
		"\u0000\u0000\u01ec\u08b8\u0001\u0000\u0000\u0000\u01ee\u08bf\u0001\u0000"+
		"\u0000\u0000\u01f0\u08c6\u0001\u0000\u0000\u0000\u01f2\u08cd\u0001\u0000"+
		"\u0000\u0000\u01f4\u08d4\u0001\u0000\u0000\u0000\u01f6\u08dd\u0001\u0000"+
		"\u0000\u0000\u01f8\u08e1\u0001\u0000\u0000\u0000\u01fa\u08e5\u0001\u0000"+
		"\u0000\u0000\u01fc\u08eb\u0001\u0000\u0000\u0000\u01fe\u08ef\u0001\u0000"+
		"\u0000\u0000\u0200\u08f1\u0001\u0000\u0000\u0000\u0202\u08f3\u0001\u0000"+
		"\u0000\u0000\u0204\u08f5\u0001\u0000\u0000\u0000\u0206\u08f7\u0001\u0000"+
		"\u0000\u0000\u0208\u0903\u0001\u0000\u0000\u0000\u020a\u0911\u0001\u0000"+
		"\u0000\u0000\u020c\u0924\u0001\u0000\u0000\u0000\u020e\u0937\u0001\u0000"+
		"\u0000\u0000\u0210\u093b\u0001\u0000\u0000\u0000\u0212\u0214\u0003\u0002"+
		"\u0001\u0000\u0213\u0212\u0001\u0000\u0000\u0000\u0213\u0214\u0001\u0000"+
		"\u0000\u0000\u0214\u0216\u0001\u0000\u0000\u0000\u0215\u0217\u0003\u0004"+
		"\u0002\u0000\u0216\u0215\u0001\u0000\u0000\u0000\u0216\u0217\u0001\u0000"+
		"\u0000\u0000\u0217\u0219\u0001\u0000\u0000\u0000\u0218\u021a\u0003\u0002"+
		"\u0001\u0000\u0219\u0218\u0001\u0000\u0000\u0000\u0219\u021a\u0001\u0000"+
		"\u0000\u0000\u021a\u021d\u0001\u0000\u0000\u0000\u021b\u021e\u0003\n\u0005"+
		"\u0000\u021c\u021e\u0003\u0006\u0003\u0000\u021d\u021b\u0001\u0000\u0000"+
		"\u0000\u021d\u021c\u0001\u0000\u0000\u0000\u021e\u0001\u0001\u0000\u0000"+
		"\u0000\u021f\u0220\u0005\u00c5\u0000\u0000\u0220\u0003\u0001\u0000\u0000"+
		"\u0000\u0221\u0222\u0005\u00ab\u0000\u0000\u0222\u0223\u0005\u00a7\u0000"+
		"\u0000\u0223\u0226\u0003\u020a\u0105\u0000\u0224\u0225\u0005Z\u0000\u0000"+
		"\u0225\u0227\u0003\u020a\u0105\u0000\u0226\u0224\u0001\u0000\u0000\u0000"+
		"\u0226\u0227\u0001\u0000\u0000\u0000\u0227\u0228\u0001\u0000\u0000\u0000"+
		"\u0228\u0229\u0005#\u0000\u0000\u0229\u0005\u0001\u0000\u0000\u0000\u022a"+
		"\u022b\u0003\u000e\u0007\u0000\u022b\u022c\u0003\b\u0004\u0000\u022c\u0007"+
		"\u0001\u0000\u0000\u0000\u022d\u022e\u0003P(\u0000\u022e\t\u0001\u0000"+
		"\u0000\u0000\u022f\u0230\u0003\f\u0006\u0000\u0230\u0231\u0003\u000e\u0007"+
		"\u0000\u0231\u000b\u0001\u0000\u0000\u0000\u0232\u0233\u0005x\u0000\u0000"+
		"\u0233\u0234\u0005y\u0000\u0000\u0234\u0235\u0003\u01fa\u00fd\u0000\u0235"+
		"\u0236\u0005\u0013\u0000\u0000\u0236\u0237\u0003\u0204\u0102\u0000\u0237"+
		"\u0238\u0005#\u0000\u0000\u0238\r\u0001\u0000\u0000\u0000\u0239\u023f"+
		"\u0003\u0012\t\u0000\u023a\u023f\u0003\u0014\n\u0000\u023b\u023f\u0003"+
		"0\u0018\u0000\u023c\u023f\u0003*\u0015\u0000\u023d\u023f\u0003.\u0017"+
		"\u0000\u023e\u0239\u0001\u0000\u0000\u0000\u023e\u023a\u0001\u0000\u0000"+
		"\u0000\u023e\u023b\u0001\u0000\u0000\u0000\u023e\u023c\u0001\u0000\u0000"+
		"\u0000\u023e\u023d\u0001\u0000\u0000\u0000\u023f\u0240\u0001\u0000\u0000"+
		"\u0000\u0240\u0241\u0005#\u0000\u0000\u0241\u0243\u0001\u0000\u0000\u0000"+
		"\u0242\u023e\u0001\u0000\u0000\u0000\u0243\u0246\u0001\u0000\u0000\u0000"+
		"\u0244\u0242\u0001\u0000\u0000\u0000\u0244\u0245\u0001\u0000\u0000\u0000"+
		"\u0245\u024f\u0001\u0000\u0000\u0000\u0246\u0244\u0001\u0000\u0000\u0000"+
		"\u0247\u0249\u0003\u0002\u0001\u0000\u0248\u0247\u0001\u0000\u0000\u0000"+
		"\u0248\u0249\u0001\u0000\u0000\u0000\u0249\u024a\u0001\u0000\u0000\u0000"+
		"\u024a\u024b\u0003\u0010\b\u0000\u024b\u024c\u0005#\u0000\u0000\u024c"+
		"\u024e\u0001\u0000\u0000\u0000\u024d\u0248\u0001\u0000\u0000\u0000\u024e"+
		"\u0251\u0001\u0000\u0000\u0000\u024f\u024d\u0001\u0000\u0000\u0000\u024f"+
		"\u0250\u0001\u0000\u0000\u0000\u0250\u000f\u0001\u0000\u0000\u0000\u0251"+
		"\u024f\u0001\u0000\u0000\u0000\u0252\u0257\u00032\u0019\u0000\u0253\u0257"+
		"\u0003:\u001d\u0000\u0254\u0257\u00038\u001c\u0000\u0255\u0257\u0003J"+
		"%\u0000\u0256\u0252\u0001\u0000\u0000\u0000\u0256\u0253\u0001\u0000\u0000"+
		"\u0000\u0256\u0254\u0001\u0000\u0000\u0000\u0256\u0255\u0001\u0000\u0000"+
		"\u0000\u0257\u0011\u0001\u0000\u0000\u0000\u0258\u0259\u0005M\u0000\u0000"+
		"\u0259\u025a\u0005N\u0000\u0000\u025a\u025b\u0007\u0000\u0000\u0000\u025b"+
		"\u025c\u0005y\u0000\u0000\u025c\u025d\u0003\u020a\u0105\u0000\u025d\u0013"+
		"\u0001\u0000\u0000\u0000\u025e\u0267\u0003\u0016\u000b\u0000\u025f\u0267"+
		"\u0003\u0018\f\u0000\u0260\u0267\u0003\u001a\r\u0000\u0261\u0267\u0003"+
		"\u001c\u000e\u0000\u0262\u0267\u0003\u001e\u000f\u0000\u0263\u0267\u0003"+
		" \u0010\u0000\u0264\u0267\u0003\"\u0011\u0000\u0265\u0267\u0003(\u0014"+
		"\u0000\u0266\u025e\u0001\u0000\u0000\u0000\u0266\u025f\u0001\u0000\u0000"+
		"\u0000\u0266\u0260\u0001\u0000\u0000\u0000\u0266\u0261\u0001\u0000\u0000"+
		"\u0000\u0266\u0262\u0001\u0000\u0000\u0000\u0266\u0263\u0001\u0000\u0000"+
		"\u0000\u0266\u0264\u0001\u0000\u0000\u0000\u0266\u0265\u0001\u0000\u0000"+
		"\u0000\u0267\u0015\u0001\u0000\u0000\u0000\u0268\u0269\u0005M\u0000\u0000"+
		"\u0269\u026a\u0005?\u0000\u0000\u026a\u026b\u0007\u0001\u0000\u0000\u026b"+
		"\u0017\u0001\u0000\u0000\u0000\u026c\u026d\u0005M\u0000\u0000\u026d\u026e"+
		"\u0005N\u0000\u0000\u026e\u026f\u0005G\u0000\u0000\u026f\u0270\u0003\u0204"+
		"\u0102\u0000\u0270\u0019\u0001\u0000\u0000\u0000\u0271\u0272\u0005M\u0000"+
		"\u0000\u0272\u0273\u0005>\u0000\u0000\u0273\u0274\u0003\u0204\u0102\u0000"+
		"\u0274\u001b\u0001\u0000\u0000\u0000\u0275\u0276\u0005M\u0000\u0000\u0276"+
		"\u0277\u0005I\u0000\u0000\u0277\u0278\u0007\u0001\u0000\u0000\u0278\u001d"+
		"\u0001\u0000\u0000\u0000\u0279\u027a\u0005M\u0000\u0000\u027a\u027b\u0005"+
		"\u0086\u0000\u0000\u027b\u027c\u0007\u0002\u0000\u0000\u027c\u001f\u0001"+
		"\u0000\u0000\u0000\u027d\u027e\u0005M\u0000\u0000\u027e\u027f\u0005N\u0000"+
		"\u0000\u027f\u0280\u0005\u0084\u0000\u0000\u0280\u0281\u0005X\u0000\u0000"+
		"\u0281\u0282\u0007\u0003\u0000\u0000\u0282!\u0001\u0000\u0000\u0000\u0283"+
		"\u0284\u0005M\u0000\u0000\u0284\u0285\u0005K\u0000\u0000\u0285\u0286\u0003"+
		"$\u0012\u0000\u0286\u0287\u0005\u001e\u0000\u0000\u0287\u0288\u0003&\u0013"+
		"\u0000\u0288#\u0001\u0000\u0000\u0000\u0289\u028a\u0007\u0004\u0000\u0000"+
		"\u028a%\u0001\u0000\u0000\u0000\u028b\u028c\u0007\u0005\u0000\u0000\u028c"+
		"\'\u0001\u0000\u0000\u0000\u028d\u0292\u0005M\u0000\u0000\u028e\u028f"+
		"\u0005R\u0000\u0000\u028f\u0293\u0003\u01f6\u00fb\u0000\u0290\u0291\u0005"+
		"N\u0000\u0000\u0291\u0293\u0005R\u0000\u0000\u0292\u028e\u0001\u0000\u0000"+
		"\u0000\u0292\u0290\u0001\u0000\u0000\u0000\u0293\u0299\u0001\u0000\u0000"+
		"\u0000\u0294\u0295\u0005\b\u0000\u0000\u0295\u0296\u0005\u0013\u0000\u0000"+
		"\u0296\u0298\u0003\u020a\u0105\u0000\u0297\u0294\u0001\u0000\u0000\u0000"+
		"\u0298\u029b\u0001\u0000\u0000\u0000\u0299\u0297\u0001\u0000\u0000\u0000"+
		"\u0299\u029a\u0001\u0000\u0000\u0000\u029a)\u0001\u0000\u0000\u0000\u029b"+
		"\u0299\u0001\u0000\u0000\u0000\u029c\u029d\u0005j\u0000\u0000\u029d\u029f"+
		"\u0005\u008f\u0000\u0000\u029e\u02a0\u0003,\u0016\u0000\u029f\u029e\u0001"+
		"\u0000\u0000\u0000\u029f\u02a0\u0001\u0000\u0000\u0000\u02a0\u02a1\u0001"+
		"\u0000\u0000\u0000\u02a1\u02ab\u0003\u0204\u0102\u0000\u02a2\u02a3\u0005"+
		"<\u0000\u0000\u02a3\u02a8\u0003\u0204\u0102\u0000\u02a4\u02a5\u0005\u001e"+
		"\u0000\u0000\u02a5\u02a7\u0003\u0204\u0102\u0000\u02a6\u02a4\u0001\u0000"+
		"\u0000\u0000\u02a7\u02aa\u0001\u0000\u0000\u0000\u02a8\u02a6\u0001\u0000"+
		"\u0000\u0000\u02a8\u02a9\u0001\u0000\u0000\u0000\u02a9\u02ac\u0001\u0000"+
		"\u0000\u0000\u02aa\u02a8\u0001\u0000\u0000\u0000\u02ab\u02a2\u0001\u0000"+
		"\u0000\u0000\u02ab\u02ac\u0001\u0000\u0000\u0000\u02ac+\u0001\u0000\u0000"+
		"\u0000\u02ad\u02ae\u0005y\u0000\u0000\u02ae\u02af\u0003\u01fa\u00fd\u0000"+
		"\u02af\u02b0\u0005\u0013\u0000\u0000\u02b0\u02b5\u0001\u0000\u0000\u0000"+
		"\u02b1\u02b2\u0005N\u0000\u0000\u02b2\u02b3\u0005V\u0000\u0000\u02b3\u02b5"+
		"\u0005y\u0000\u0000\u02b4\u02ad\u0001\u0000\u0000\u0000\u02b4\u02b1\u0001"+
		"\u0000\u0000\u0000\u02b5-\u0001\u0000\u0000\u0000\u02b6\u02b7\u0005j\u0000"+
		"\u0000\u02b7\u02bc\u0005x\u0000\u0000\u02b8\u02b9\u0005y\u0000\u0000\u02b9"+
		"\u02ba\u0003\u01fa\u00fd\u0000\u02ba\u02bb\u0005\u0013\u0000\u0000\u02bb"+
		"\u02bd\u0001\u0000\u0000\u0000\u02bc\u02b8\u0001\u0000\u0000\u0000\u02bc"+
		"\u02bd\u0001\u0000\u0000\u0000\u02bd\u02be\u0001\u0000\u0000\u0000\u02be"+
		"\u02c8\u0003\u0204\u0102\u0000\u02bf\u02c0\u0005<\u0000\u0000\u02c0\u02c5"+
		"\u0003\u0204\u0102\u0000\u02c1\u02c2\u0005\u001e\u0000\u0000\u02c2\u02c4"+
		"\u0003\u0204\u0102\u0000\u02c3\u02c1\u0001\u0000\u0000\u0000\u02c4\u02c7"+
		"\u0001\u0000\u0000\u0000\u02c5\u02c3\u0001\u0000\u0000\u0000\u02c5\u02c6"+
		"\u0001\u0000\u0000\u0000\u02c6\u02c9\u0001\u0000\u0000\u0000\u02c7\u02c5"+
		"\u0001\u0000\u0000\u0000\u02c8\u02bf\u0001\u0000\u0000\u0000\u02c8\u02c9"+
		"\u0001\u0000\u0000\u0000\u02c9/\u0001\u0000\u0000\u0000\u02ca\u02cb\u0005"+
		"M\u0000\u0000\u02cb\u02cc\u0005y\u0000\u0000\u02cc\u02cd\u0003\u01fa\u00fd"+
		"\u0000\u02cd\u02ce\u0005\u0013\u0000\u0000\u02ce\u02cf\u0003\u0204\u0102"+
		"\u0000\u02cf1\u0001\u0000\u0000\u0000\u02d0\u02d1\u0005M\u0000\u0000\u02d1"+
		"\u02d2\u0003@ \u0000\u02d2\u02d3\u0005\u00a6\u0000\u0000\u02d3\u02d4\u0005"+
		",\u0000\u0000\u02d4\u02d6\u0003\u0134\u009a\u0000\u02d5\u02d7\u0003\u01a6"+
		"\u00d3\u0000\u02d6\u02d5\u0001\u0000\u0000\u0000\u02d6\u02d7\u0001\u0000"+
		"\u0000\u0000\u02d7\u02df\u0001\u0000\u0000\u0000\u02d8\u02d9\u0005\"\u0000"+
		"\u0000\u02d9\u02e0\u00034\u001a\u0000\u02da\u02dd\u0005_\u0000\u0000\u02db"+
		"\u02dc\u0005\"\u0000\u0000\u02dc\u02de\u00036\u001b\u0000\u02dd\u02db"+
		"\u0001\u0000\u0000\u0000\u02dd\u02de\u0001\u0000\u0000\u0000\u02de\u02e0"+
		"\u0001\u0000\u0000\u0000\u02df\u02d8\u0001\u0000\u0000\u0000\u02df\u02da"+
		"\u0001\u0000\u0000\u0000\u02e03\u0001\u0000\u0000\u0000\u02e1\u02e2\u0003"+
		"T*\u0000\u02e25\u0001\u0000\u0000\u0000\u02e3\u02e4\u0003T*\u0000\u02e4"+
		"7\u0001\u0000\u0000\u0000\u02e5\u02e6\u0005M\u0000\u0000\u02e6\u02e7\u0005"+
		"J\u0000\u0000\u02e7\u02ea\u0005p\u0000\u0000\u02e8\u02e9\u0005:\u0000"+
		"\u0000\u02e9\u02eb\u0003\u01aa\u00d5\u0000\u02ea\u02e8\u0001\u0000\u0000"+
		"\u0000\u02ea\u02eb\u0001\u0000\u0000\u0000\u02eb\u02f3\u0001\u0000\u0000"+
		"\u0000\u02ec\u02ed\u0005\"\u0000\u0000\u02ed\u02f4\u0003T*\u0000\u02ee"+
		"\u02f1\u0005_\u0000\u0000\u02ef\u02f0\u0005\"\u0000\u0000\u02f0\u02f2"+
		"\u0003T*\u0000\u02f1\u02ef\u0001\u0000\u0000\u0000\u02f1\u02f2\u0001\u0000"+
		"\u0000\u0000\u02f2\u02f4\u0001\u0000\u0000\u0000\u02f3\u02ec\u0001\u0000"+
		"\u0000\u0000\u02f3\u02ee\u0001\u0000\u0000\u0000\u02f49\u0001\u0000\u0000"+
		"\u0000\u02f5\u02f6\u0005M\u0000\u0000\u02f6\u02f7\u0003@ \u0000\u02f7"+
		"\u02f8\u0005c\u0000\u0000\u02f8\u02f9\u0003\u01f6\u00fb\u0000\u02f9\u02fb"+
		"\u0005\u0015\u0000\u0000\u02fa\u02fc\u0003<\u001e\u0000\u02fb\u02fa\u0001"+
		"\u0000\u0000\u0000\u02fb\u02fc\u0001\u0000\u0000\u0000\u02fc\u02fd\u0001"+
		"\u0000\u0000\u0000\u02fd\u02ff\u0005\u0016\u0000\u0000\u02fe\u0300\u0003"+
		"H$\u0000\u02ff\u02fe\u0001\u0000\u0000\u0000\u02ff\u0300\u0001\u0000\u0000"+
		"\u0000\u0300\u0303\u0001\u0000\u0000\u0000\u0301\u0304\u0003\u0188\u00c4"+
		"\u0000\u0302\u0304\u0005_\u0000\u0000\u0303\u0301\u0001\u0000\u0000\u0000"+
		"\u0303\u0302\u0001\u0000\u0000\u0000\u0304;\u0001\u0000\u0000\u0000\u0305"+
		"\u030a\u0003>\u001f\u0000\u0306\u0307\u0005\u001e\u0000\u0000\u0307\u0309"+
		"\u0003>\u001f\u0000\u0308\u0306\u0001\u0000\u0000\u0000\u0309\u030c\u0001"+
		"\u0000\u0000\u0000\u030a\u0308\u0001\u0000\u0000\u0000\u030a\u030b\u0001"+
		"\u0000\u0000\u0000\u030b=\u0001\u0000\u0000\u0000\u030c\u030a\u0001\u0000"+
		"\u0000\u0000\u030d\u030e\u0005,\u0000\u0000\u030e\u0310\u0003\u01f8\u00fc"+
		"\u0000\u030f\u0311\u0003\u01a6\u00d3\u0000\u0310\u030f\u0001\u0000\u0000"+
		"\u0000\u0310\u0311\u0001\u0000\u0000\u0000\u0311?\u0001\u0000\u0000\u0000"+
		"\u0312\u0314\u0003B!\u0000\u0313\u0312\u0001\u0000\u0000\u0000\u0314\u0317"+
		"\u0001\u0000\u0000\u0000\u0315\u0313\u0001\u0000\u0000\u0000\u0315\u0316"+
		"\u0001\u0000\u0000\u0000\u0316A\u0001\u0000\u0000\u0000\u0317\u0315\u0001"+
		"\u0000\u0000\u0000\u0318\u0319\u0005-\u0000\u0000\u0319\u031e\u0003\u01f8"+
		"\u00fc\u0000\u031a\u031b\u0005\u0015\u0000\u0000\u031b\u031c\u0003D\""+
		"\u0000\u031c\u031d\u0005\u0016\u0000\u0000\u031d\u031f\u0001\u0000\u0000"+
		"\u0000\u031e\u031a\u0001\u0000\u0000\u0000\u031e\u031f\u0001\u0000\u0000"+
		"\u0000\u031fC\u0001\u0000\u0000\u0000\u0320\u0325\u0003F#\u0000\u0321"+
		"\u0322\u0005\u001e\u0000\u0000\u0322\u0324\u0003F#\u0000\u0323\u0321\u0001"+
		"\u0000\u0000\u0000\u0324\u0327\u0001\u0000\u0000\u0000\u0325\u0323\u0001"+
		"\u0000\u0000\u0000\u0325\u0326\u0001\u0000\u0000\u0000\u0326E\u0001\u0000"+
		"\u0000\u0000\u0327\u0325\u0001\u0000\u0000\u0000\u0328\u0329\u0003\u012e"+
		"\u0097\u0000\u0329G\u0001\u0000\u0000\u0000\u032a\u032b\u0005:\u0000\u0000"+
		"\u032b\u032c\u0003\u01a8\u00d4\u0000\u032cI\u0001\u0000\u0000\u0000\u032d"+
		"\u032e\u0005M\u0000\u0000\u032e\u032f\u0005\u0082\u0000\u0000\u032f\u0330"+
		"\u0003\u01f8\u00fc\u0000\u0330\u0331\u0003\u020a\u0105\u0000\u0331K\u0001"+
		"\u0000\u0000\u0000\u0332\u0334\u0003\u00e2q\u0000\u0333\u0332\u0001\u0000"+
		"\u0000\u0000\u0334\u0337\u0001\u0000\u0000\u0000\u0335\u0333\u0001\u0000"+
		"\u0000\u0000\u0335\u0336\u0001\u0000\u0000\u0000\u0336M\u0001\u0000\u0000"+
		"\u0000\u0337\u0335\u0001\u0000\u0000\u0000\u0338\u0339\u0003L&\u0000\u0339"+
		"\u033a\u0003R)\u0000\u033aO\u0001\u0000\u0000\u0000\u033b\u033d\u0003"+
		"L&\u0000\u033c\u033e\u0003R)\u0000\u033d\u033c\u0001\u0000\u0000\u0000"+
		"\u033d\u033e\u0001\u0000\u0000\u0000\u033eQ\u0001\u0000\u0000\u0000\u033f"+
		"\u0344\u0003T*\u0000\u0340\u0341\u0005\u001e\u0000\u0000\u0341\u0343\u0003"+
		"T*\u0000\u0342\u0340\u0001\u0000\u0000\u0000\u0343\u0346\u0001\u0000\u0000"+
		"\u0000\u0344\u0342\u0001\u0000\u0000\u0000\u0344\u0345\u0001\u0000\u0000"+
		"\u0000\u0345S\u0001\u0000\u0000\u0000\u0346\u0344\u0001\u0000\u0000\u0000"+
		"\u0347\u034e\u0003Z-\u0000\u0348\u034e\u0003V+\u0000\u0349\u034e\u0003"+
		"\u008cF\u0000\u034a\u034e\u0003\u0092I\u0000\u034b\u034e\u0003\u0098L"+
		"\u0000\u034c\u034e\u0003\u009aM\u0000\u034d\u0347\u0001\u0000\u0000\u0000"+
		"\u034d\u0348\u0001\u0000\u0000\u0000\u034d\u0349\u0001\u0000\u0000\u0000"+
		"\u034d\u034a\u0001\u0000\u0000\u0000\u034d\u034b\u0001\u0000\u0000\u0000"+
		"\u034d\u034c\u0001\u0000\u0000\u0000\u034eU\u0001\u0000\u0000\u0000\u034f"+
		"\u0353\u0003\u0088D\u0000\u0350\u0353\u0003\u00b2Y\u0000\u0351\u0353\u0003"+
		"\u00a6S\u0000\u0352\u034f\u0001\u0000\u0000\u0000\u0352\u0350\u0001\u0000"+
		"\u0000\u0000\u0352\u0351\u0001\u0000\u0000\u0000\u0353W\u0001\u0000\u0000"+
		"\u0000\u0354\u0355\u0005\u0019\u0000\u0000\u0355\u0356\u0003N\'\u0000"+
		"\u0356\u0357\u0005\u001a\u0000\u0000\u0357Y\u0001\u0000\u0000\u0000\u0358"+
		"\u035c\u0003\\.\u0000\u0359\u035b\u0003^/\u0000\u035a\u0359\u0001\u0000"+
		"\u0000\u0000\u035b\u035e\u0001\u0000\u0000\u0000\u035c\u035a\u0001\u0000"+
		"\u0000\u0000\u035c\u035d\u0001\u0000\u0000\u0000\u035d\u035f\u0001\u0000"+
		"\u0000\u0000\u035e\u035c\u0001\u0000\u0000\u0000\u035f\u0360\u0003\u0086"+
		"C\u0000\u0360[\u0001\u0000\u0000\u0000\u0361\u0365\u0003`0\u0000\u0362"+
		"\u0365\u0003h4\u0000\u0363\u0365\u0003l6\u0000\u0364\u0361\u0001\u0000"+
		"\u0000\u0000\u0364\u0362\u0001\u0000\u0000\u0000\u0364\u0363\u0001\u0000"+
		"\u0000\u0000\u0365]\u0001\u0000\u0000\u0000\u0366\u036c\u0003\\.\u0000"+
		"\u0367\u036c\u0003z=\u0000\u0368\u036c\u0003|>\u0000\u0369\u036c\u0003"+
		"\u0082A\u0000\u036a\u036c\u0003x<\u0000\u036b\u0366\u0001\u0000\u0000"+
		"\u0000\u036b\u0367\u0001\u0000\u0000\u0000\u036b\u0368\u0001\u0000\u0000"+
		"\u0000\u036b\u0369\u0001\u0000\u0000\u0000\u036b\u036a\u0001\u0000\u0000"+
		"\u0000\u036c_\u0001\u0000\u0000\u0000\u036d\u036e\u0005b\u0000\u0000\u036e"+
		"\u0373\u0003b1\u0000\u036f\u0370\u0005\u001e\u0000\u0000\u0370\u0372\u0003"+
		"b1\u0000\u0371\u036f\u0001\u0000\u0000\u0000\u0372\u0375\u0001\u0000\u0000"+
		"\u0000\u0373\u0371\u0001\u0000\u0000\u0000\u0373\u0374\u0001\u0000\u0000"+
		"\u0000\u0374a\u0001\u0000\u0000\u0000\u0375\u0373\u0001\u0000\u0000\u0000"+
		"\u0376\u0377\u0005,\u0000\u0000\u0377\u0379\u0003\u0134\u009a\u0000\u0378"+
		"\u037a\u0003\u01a6\u00d3\u0000\u0379\u0378\u0001\u0000\u0000\u0000\u0379"+
		"\u037a\u0001\u0000\u0000\u0000\u037a\u037c\u0001\u0000\u0000\u0000\u037b"+
		"\u037d\u0003d2\u0000\u037c\u037b\u0001\u0000\u0000\u0000\u037c\u037d\u0001"+
		"\u0000\u0000\u0000\u037d\u037f\u0001\u0000\u0000\u0000\u037e\u0380\u0003"+
		"f3\u0000\u037f\u037e\u0001\u0000\u0000\u0000\u037f\u0380\u0001\u0000\u0000"+
		"\u0000\u0380\u0381\u0001\u0000\u0000\u0000\u0381\u0382\u0005k\u0000\u0000"+
		"\u0382\u0383\u0003T*\u0000\u0383c\u0001\u0000\u0000\u0000\u0384\u0385"+
		"\u00055\u0000\u0000\u0385\u0386\u0005X\u0000\u0000\u0386e\u0001\u0000"+
		"\u0000\u0000\u0387\u0388\u0005<\u0000\u0000\u0388\u0389\u0005,\u0000\u0000"+
		"\u0389\u038a\u0003\u0134\u009a\u0000\u038ag\u0001\u0000\u0000\u0000\u038b"+
		"\u038c\u0005t\u0000\u0000\u038c\u0391\u0003j5\u0000\u038d\u038e\u0005"+
		"\u001e\u0000\u0000\u038e\u0390\u0003j5\u0000\u038f\u038d\u0001\u0000\u0000"+
		"\u0000\u0390\u0393\u0001\u0000\u0000\u0000\u0391\u038f\u0001\u0000\u0000"+
		"\u0000\u0391\u0392\u0001\u0000\u0000\u0000\u0392i\u0001\u0000\u0000\u0000"+
		"\u0393\u0391\u0001\u0000\u0000\u0000\u0394\u0395\u0005,\u0000\u0000\u0395"+
		"\u0397\u0003\u0134\u009a\u0000\u0396\u0398\u0003\u01a6\u00d3\u0000\u0397"+
		"\u0396\u0001\u0000\u0000\u0000\u0397\u0398\u0001\u0000\u0000\u0000\u0398"+
		"\u0399\u0001\u0000\u0000\u0000\u0399\u039a\u0005\"\u0000\u0000\u039a\u039b"+
		"\u0003T*\u0000\u039bk\u0001\u0000\u0000\u0000\u039c\u039f\u0005b\u0000"+
		"\u0000\u039d\u03a0\u0003n7\u0000\u039e\u03a0\u0003p8\u0000\u039f\u039d"+
		"\u0001\u0000\u0000\u0000\u039f\u039e\u0001\u0000\u0000\u0000\u03a0m\u0001"+
		"\u0000\u0000\u0000\u03a1\u03a2\u0005\u009f\u0000\u0000\u03a2\u03a3\u0005"+
		"\u00aa\u0000\u0000\u03a3\u03a4\u0005,\u0000\u0000\u03a4\u03a6\u0003\u01f8"+
		"\u00fc\u0000\u03a5\u03a7\u0003\u01a6\u00d3\u0000\u03a6\u03a5\u0001\u0000"+
		"\u0000\u0000\u03a6\u03a7\u0001\u0000\u0000\u0000\u03a7\u03a8\u0001\u0000"+
		"\u0000\u0000\u03a8\u03a9\u0005k\u0000\u0000\u03a9\u03aa\u0003T*\u0000"+
		"\u03aa\u03ac\u0003r9\u0000\u03ab\u03ad\u0003t:\u0000\u03ac\u03ab\u0001"+
		"\u0000\u0000\u0000\u03ac\u03ad\u0001\u0000\u0000\u0000\u03ado\u0001\u0000"+
		"\u0000\u0000\u03ae\u03af\u0005\u0093\u0000\u0000\u03af\u03b0\u0005\u00aa"+
		"\u0000\u0000\u03b0\u03b1\u0005,\u0000\u0000\u03b1\u03b3\u0003\u01f8\u00fc"+
		"\u0000\u03b2\u03b4\u0003\u01a6\u00d3\u0000\u03b3\u03b2\u0001\u0000\u0000"+
		"\u0000\u03b3\u03b4\u0001\u0000\u0000\u0000\u03b4\u03b5\u0001\u0000\u0000"+
		"\u0000\u03b5\u03b6\u0005k\u0000\u0000\u03b6\u03b7\u0003T*\u0000\u03b7"+
		"\u03b8\u0003r9\u0000\u03b8\u03b9\u0003t:\u0000\u03b9q\u0001\u0000\u0000"+
		"\u0000\u03ba\u03bb\u0005\u0096\u0000\u0000\u03bb\u03bc\u0003v;\u0000\u03bc"+
		"\u03bd\u0005\u00a8\u0000\u0000\u03bd\u03be\u0003T*\u0000\u03bes\u0001"+
		"\u0000\u0000\u0000\u03bf\u03c1\u0005\u0081\u0000\u0000\u03c0\u03bf\u0001"+
		"\u0000\u0000\u0000\u03c0\u03c1\u0001\u0000\u0000\u0000\u03c1\u03c2\u0001"+
		"\u0000\u0000\u0000\u03c2\u03c3\u0005[\u0000\u0000\u03c3\u03c4\u0003v;"+
		"\u0000\u03c4\u03c5\u0005\u00a8\u0000\u0000\u03c5\u03c6\u0003T*\u0000\u03c6"+
		"u\u0001\u0000\u0000\u0000\u03c7\u03c8\u0005,\u0000\u0000\u03c8\u03ca\u0003"+
		"\u01f6\u00fb\u0000\u03c9\u03c7\u0001\u0000\u0000\u0000\u03c9\u03ca\u0001"+
		"\u0000\u0000\u0000\u03ca\u03cc\u0001\u0000\u0000\u0000\u03cb\u03cd\u0003"+
		"f3\u0000\u03cc\u03cb\u0001\u0000\u0000\u0000\u03cc\u03cd\u0001\u0000\u0000"+
		"\u0000\u03cd\u03d1\u0001\u0000\u0000\u0000\u03ce\u03cf\u0005\u008b\u0000"+
		"\u0000\u03cf\u03d0\u0005,\u0000\u0000\u03d0\u03d2\u0003\u01f6\u00fb\u0000"+
		"\u03d1\u03ce\u0001\u0000\u0000\u0000\u03d1\u03d2\u0001\u0000\u0000\u0000"+
		"\u03d2\u03d6\u0001\u0000\u0000\u0000\u03d3\u03d4\u0005{\u0000\u0000\u03d4"+
		"\u03d5\u0005,\u0000\u0000\u03d5\u03d7\u0003\u01f6\u00fb\u0000\u03d6\u03d3"+
		"\u0001\u0000\u0000\u0000\u03d6\u03d7\u0001\u0000\u0000\u0000\u03d7w\u0001"+
		"\u0000\u0000\u0000\u03d8\u03d9\u0005L\u0000\u0000\u03d9\u03da\u0005,\u0000"+
		"\u0000\u03da\u03db\u0003\u0134\u009a\u0000\u03dby\u0001\u0000\u0000\u0000"+
		"\u03dc\u03dd\u0005\u00a9\u0000\u0000\u03dd\u03de\u0003T*\u0000\u03de{"+
		"\u0001\u0000\u0000\u0000\u03df\u03e0\u0005f\u0000\u0000\u03e0\u03e1\u0005"+
		"A\u0000\u0000\u03e1\u03e2\u0003~?\u0000\u03e2}\u0001\u0000\u0000\u0000"+
		"\u03e3\u03e8\u0003\u0080@\u0000\u03e4\u03e5\u0005\u001e\u0000\u0000\u03e5"+
		"\u03e7\u0003\u0080@\u0000\u03e6\u03e4\u0001\u0000\u0000\u0000\u03e7\u03ea"+
		"\u0001\u0000\u0000\u0000\u03e8\u03e6\u0001\u0000\u0000\u0000\u03e8\u03e9"+
		"\u0001\u0000\u0000\u0000\u03e9\u007f\u0001\u0000\u0000\u0000\u03ea\u03e8"+
		"\u0001\u0000\u0000\u0000\u03eb\u03ec\u0005,\u0000\u0000\u03ec\u03f2\u0003"+
		"\u0134\u009a\u0000\u03ed\u03ef\u0003\u01a6\u00d3\u0000\u03ee\u03ed\u0001"+
		"\u0000\u0000\u0000\u03ee\u03ef\u0001\u0000\u0000\u0000\u03ef\u03f0\u0001"+
		"\u0000\u0000\u0000\u03f0\u03f1\u0005\"\u0000\u0000\u03f1\u03f3\u0003T"+
		"*\u0000\u03f2\u03ee\u0001\u0000\u0000\u0000\u03f2\u03f3\u0001\u0000\u0000"+
		"\u0000\u03f3\u03f6\u0001\u0000\u0000\u0000\u03f4\u03f5\u0005G\u0000\u0000"+
		"\u03f5\u03f7\u0003\u0204\u0102\u0000\u03f6\u03f4\u0001\u0000\u0000\u0000"+
		"\u03f6\u03f7\u0001\u0000\u0000\u0000\u03f7\u0081\u0001\u0000\u0000\u0000"+
		"\u03f8\u03fa\u0005\u0095\u0000\u0000\u03f9\u03f8\u0001\u0000\u0000\u0000"+
		"\u03f9\u03fa\u0001\u0000\u0000\u0000\u03fa\u03fb\u0001\u0000\u0000\u0000"+
		"\u03fb\u03fc\u0005\u0084\u0000\u0000\u03fc\u03fd\u0005A\u0000\u0000\u03fd"+
		"\u0402\u0003\u0084B\u0000\u03fe\u03ff\u0005\u001e\u0000\u0000\u03ff\u0401"+
		"\u0003\u0084B\u0000\u0400\u03fe\u0001\u0000\u0000\u0000\u0401\u0404\u0001"+
		"\u0000\u0000\u0000\u0402\u0400\u0001\u0000\u0000\u0000\u0402\u0403\u0001"+
		"\u0000\u0000\u0000\u0403\u0083\u0001\u0000\u0000\u0000\u0404\u0402\u0001"+
		"\u0000\u0000\u0000\u0405\u0408\u0003T*\u0000\u0406\u0409\u0005;\u0000"+
		"\u0000\u0407\u0409\u0005Q\u0000\u0000\u0408\u0406\u0001\u0000\u0000\u0000"+
		"\u0408\u0407\u0001\u0000\u0000\u0000\u0408\u0409\u0001\u0000\u0000\u0000"+
		"\u0409\u040f\u0001\u0000\u0000\u0000\u040a\u040d\u0005X\u0000\u0000\u040b"+
		"\u040e\u0005e\u0000\u0000\u040c\u040e\u0005s\u0000\u0000\u040d\u040b\u0001"+
		"\u0000\u0000\u0000\u040d\u040c\u0001\u0000\u0000\u0000\u040e\u0410\u0001"+
		"\u0000\u0000\u0000\u040f\u040a\u0001\u0000\u0000\u0000\u040f\u0410\u0001"+
		"\u0000\u0000\u0000\u0410\u0413\u0001\u0000\u0000\u0000\u0411\u0412\u0005"+
		"G\u0000\u0000\u0412\u0414\u0003\u0204\u0102\u0000\u0413\u0411\u0001\u0000"+
		"\u0000\u0000\u0413\u0414\u0001\u0000\u0000\u0000\u0414\u0085\u0001\u0000"+
		"\u0000\u0000\u0415\u0416\u0005\u008d\u0000\u0000\u0416\u0417\u0003T*\u0000"+
		"\u0417\u0087\u0001\u0000\u0000\u0000\u0418\u041b\u0005\u0094\u0000\u0000"+
		"\u0419\u041b\u0005]\u0000\u0000\u041a\u0418\u0001\u0000\u0000\u0000\u041a"+
		"\u0419\u0001\u0000\u0000\u0000\u041b\u041c\u0001\u0000\u0000\u0000\u041c"+
		"\u0421\u0003\u008aE\u0000\u041d\u041e\u0005\u001e\u0000\u0000\u041e\u0420"+
		"\u0003\u008aE\u0000\u041f\u041d\u0001\u0000\u0000\u0000\u0420\u0423\u0001"+
		"\u0000\u0000\u0000\u0421\u041f\u0001\u0000\u0000\u0000\u0421\u0422\u0001"+
		"\u0000\u0000\u0000\u0422\u0424\u0001\u0000\u0000\u0000\u0423\u0421\u0001"+
		"\u0000\u0000\u0000\u0424\u0425\u0005\u008e\u0000\u0000\u0425\u0426\u0003"+
		"T*\u0000\u0426\u0089\u0001\u0000\u0000\u0000\u0427\u0428\u0005,\u0000"+
		"\u0000\u0428\u042a\u0003\u0134\u009a\u0000\u0429\u042b\u0003\u01a6\u00d3"+
		"\u0000\u042a\u0429\u0001\u0000\u0000\u0000\u042a\u042b\u0001\u0000\u0000"+
		"\u0000\u042b\u042c\u0001\u0000\u0000\u0000\u042c\u042d\u0005k\u0000\u0000"+
		"\u042d\u042e\u0003T*\u0000\u042e\u008b\u0001\u0000\u0000\u0000\u042f\u0430"+
		"\u0005\u0099\u0000\u0000\u0430\u0431\u0005\u0015\u0000\u0000\u0431\u0432"+
		"\u0003R)\u0000\u0432\u0434\u0005\u0016\u0000\u0000\u0433\u0435\u0003\u008e"+
		"G\u0000\u0434\u0433\u0001\u0000\u0000\u0000\u0435\u0436\u0001\u0000\u0000"+
		"\u0000\u0436\u0434\u0001\u0000\u0000\u0000\u0436\u0437\u0001\u0000\u0000"+
		"\u0000\u0437\u0438\u0001\u0000\u0000\u0000\u0438\u0439\u0005N\u0000\u0000"+
		"\u0439\u043a\u0005\u008d\u0000\u0000\u043a\u043b\u0003T*\u0000\u043b\u008d"+
		"\u0001\u0000\u0000\u0000\u043c\u043d\u0005B\u0000\u0000\u043d\u043f\u0003"+
		"\u0090H\u0000\u043e\u043c\u0001\u0000\u0000\u0000\u043f\u0440\u0001\u0000"+
		"\u0000\u0000\u0440\u043e\u0001\u0000\u0000\u0000\u0440\u0441\u0001\u0000"+
		"\u0000\u0000\u0441\u0442\u0001\u0000\u0000\u0000\u0442\u0443\u0005\u008d"+
		"\u0000\u0000\u0443\u0444\u0003T*\u0000\u0444\u008f\u0001\u0000\u0000\u0000"+
		"\u0445\u0446\u0003T*\u0000\u0446\u0091\u0001\u0000\u0000\u0000\u0447\u0448"+
		"\u0005\u00a1\u0000\u0000\u0448\u0449\u0005\u0015\u0000\u0000\u0449\u044a"+
		"\u0003R)\u0000\u044a\u044c\u0005\u0016\u0000\u0000\u044b\u044d\u0003\u0094"+
		"J\u0000\u044c\u044b\u0001\u0000\u0000\u0000\u044d\u044e\u0001\u0000\u0000"+
		"\u0000\u044e\u044c\u0001\u0000\u0000\u0000\u044e\u044f\u0001\u0000\u0000"+
		"\u0000\u044f\u0450\u0001\u0000\u0000\u0000\u0450\u0453\u0005N\u0000\u0000"+
		"\u0451\u0452\u0005,\u0000\u0000\u0452\u0454\u0003\u0134\u009a\u0000\u0453"+
		"\u0451\u0001\u0000\u0000\u0000\u0453\u0454\u0001\u0000\u0000\u0000\u0454"+
		"\u0455\u0001\u0000\u0000\u0000\u0455\u0456\u0005\u008d\u0000\u0000\u0456"+
		"\u0457\u0003T*\u0000\u0457\u0093\u0001\u0000\u0000\u0000\u0458\u045d\u0005"+
		"B\u0000\u0000\u0459\u045a\u0005,\u0000\u0000\u045a\u045b\u0003\u0134\u009a"+
		"\u0000\u045b\u045c\u0005:\u0000\u0000\u045c\u045e\u0001\u0000\u0000\u0000"+
		"\u045d\u0459\u0001\u0000\u0000\u0000\u045d\u045e\u0001\u0000\u0000\u0000"+
		"\u045e\u045f\u0001\u0000\u0000\u0000\u045f\u0460\u0003\u0096K\u0000\u0460"+
		"\u0461\u0005\u008d\u0000\u0000\u0461\u0462\u0003T*\u0000\u0462\u0095\u0001"+
		"\u0000\u0000\u0000\u0463\u0468\u0003\u01a8\u00d4\u0000\u0464\u0465\u0005"+
		"\'\u0000\u0000\u0465\u0467\u0003\u01a8\u00d4\u0000\u0466\u0464\u0001\u0000"+
		"\u0000\u0000\u0467\u046a\u0001\u0000\u0000\u0000\u0468\u0466\u0001\u0000"+
		"\u0000\u0000\u0468\u0469\u0001\u0000\u0000\u0000\u0469\u0097\u0001\u0000"+
		"\u0000\u0000\u046a\u0468\u0001\u0000\u0000\u0000\u046b\u046c\u0005i\u0000"+
		"\u0000\u046c\u046d\u0005\u0015\u0000\u0000\u046d\u046e\u0003R)\u0000\u046e"+
		"\u046f\u0005\u0016\u0000\u0000\u046f\u0470\u0005\u009b\u0000\u0000\u0470"+
		"\u0471\u0003T*\u0000\u0471\u0472\u0005W\u0000\u0000\u0472\u0473\u0003"+
		"T*\u0000\u0473\u0099\u0001\u0000\u0000\u0000\u0474\u0476\u0003\u009cN"+
		"\u0000\u0475\u0477\u0003\u00a0P\u0000\u0476\u0475\u0001\u0000\u0000\u0000"+
		"\u0477\u0478\u0001\u0000\u0000\u0000\u0478\u0476\u0001\u0000\u0000\u0000"+
		"\u0478\u0479\u0001\u0000\u0000\u0000\u0479\u009b\u0001\u0000\u0000\u0000"+
		"\u047a\u047b\u0005\u009e\u0000\u0000\u047b\u047c\u0003\u009eO\u0000\u047c"+
		"\u009d\u0001\u0000\u0000\u0000\u047d\u047e\u0003\u00a2Q\u0000\u047e\u009f"+
		"\u0001\u0000\u0000\u0000\u047f\u0486\u0005E\u0000\u0000\u0480\u0487\u0003"+
		"\u00a4R\u0000\u0481\u0482\u0005\u0015\u0000\u0000\u0482\u0483\u0005,\u0000"+
		"\u0000\u0483\u0484\u0003\u0134\u009a\u0000\u0484\u0485\u0005\u0016\u0000"+
		"\u0000\u0485\u0487\u0001\u0000\u0000\u0000\u0486\u0480\u0001\u0000\u0000"+
		"\u0000\u0486\u0481\u0001\u0000\u0000\u0000\u0487\u0488\u0001\u0000\u0000"+
		"\u0000\u0488\u0489\u0003\u00a2Q\u0000\u0489\u00a1\u0001\u0000\u0000\u0000"+
		"\u048a\u048c\u0005\u0019\u0000\u0000\u048b\u048d\u0003R)\u0000\u048c\u048b"+
		"\u0001\u0000\u0000\u0000\u048c\u048d\u0001\u0000\u0000\u0000\u048d\u048e"+
		"\u0001\u0000\u0000\u0000\u048e\u048f\u0005\u001a\u0000\u0000\u048f\u00a3"+
		"\u0001\u0000\u0000\u0000\u0490\u0495\u0003\u011a\u008d\u0000\u0491\u0492"+
		"\u0005\'\u0000\u0000\u0492\u0494\u0003\u011a\u008d\u0000\u0493\u0491\u0001"+
		"\u0000\u0000\u0000\u0494\u0497\u0001\u0000\u0000\u0000\u0495\u0493\u0001"+
		"\u0000\u0000\u0000\u0495\u0496\u0001\u0000\u0000\u0000\u0496\u00a5\u0001"+
		"\u0000\u0000\u0000\u0497\u0495\u0001\u0000\u0000\u0000\u0498\u049e\u0005"+
		"\u00a4\u0000\u0000\u0499\u049f\u0003\u00a8T\u0000\u049a\u049f\u0003\u00aa"+
		"U\u0000\u049b\u049f\u0003\u00acV\u0000\u049c\u049f\u0003\u00aeW\u0000"+
		"\u049d\u049f\u0003\u00b0X\u0000\u049e\u0499\u0001\u0000\u0000\u0000\u049e"+
		"\u049a\u0001\u0000\u0000\u0000\u049e\u049b\u0001\u0000\u0000\u0000\u049e"+
		"\u049c\u0001\u0000\u0000\u0000\u049e\u049d\u0001\u0000\u0000\u0000\u049f"+
		"\u00a7\u0001\u0000\u0000\u0000\u04a0\u04a1\u0005\u00b7\u0000\u0000\u04a1"+
		"\u04a2\u0003R)\u0000\u04a2\u04a3\u0005\u00b8\u0000\u0000\u04a3\u04a4\u0003"+
		"T*\u0000\u04a4\u00a9\u0001\u0000\u0000\u0000\u04a5\u04a6\u0005\u00b9\u0000"+
		"\u0000\u04a6\u04a7\u0003R)\u0000\u04a7\u04a8\u0005\u00b8\u0000\u0000\u04a8"+
		"\u04a9\u0003T*\u0000\u04a9\u00ab\u0001\u0000\u0000\u0000\u04aa\u04ab\u0005"+
		"\u00ba\u0000\u0000\u04ab\u04ac\u0003T*\u0000\u04ac\u04ad\u0007\u0006\u0000"+
		"\u0000\u04ad\u04ae\u0003T*\u0000\u04ae\u00ad\u0001\u0000\u0000\u0000\u04af"+
		"\u04b0\u0005\u00bc\u0000\u0000\u04b0\u04b1\u0003T*\u0000\u04b1\u00af\u0001"+
		"\u0000\u0000\u0000\u04b2\u04b3\u0005\u00bd\u0000\u0000\u04b3\u04b4\u0003"+
		"T*\u0000\u04b4\u04b5\u0005:\u0000\u0000\u04b5\u04b6\u0003T*\u0000\u04b6"+
		"\u00b1\u0001\u0000\u0000\u0000\u04b7\u04bc\u0003\u00b4Z\u0000\u04b8\u04b9"+
		"\u0005\u0083\u0000\u0000\u04b9\u04bb\u0003\u00b4Z\u0000\u04ba\u04b8\u0001"+
		"\u0000\u0000\u0000\u04bb\u04be\u0001\u0000\u0000\u0000\u04bc\u04ba\u0001"+
		"\u0000\u0000\u0000\u04bc\u04bd\u0001\u0000\u0000\u0000\u04bd\u00b3\u0001"+
		"\u0000\u0000\u0000\u04be\u04bc\u0001\u0000\u0000\u0000\u04bf\u04c4\u0003"+
		"\u00b6[\u0000\u04c0\u04c1\u00058\u0000\u0000\u04c1\u04c3\u0003\u00b6["+
		"\u0000\u04c2\u04c0\u0001\u0000\u0000\u0000\u04c3\u04c6\u0001\u0000\u0000"+
		"\u0000\u04c4\u04c2\u0001\u0000\u0000\u0000\u04c4\u04c5\u0001\u0000\u0000"+
		"\u0000\u04c5\u00b5\u0001\u0000\u0000\u0000\u04c6\u04c4\u0001\u0000\u0000"+
		"\u0000\u04c7\u04cf\u0003\u00b8\\\u0000\u04c8\u04cc\u0003\u00d6k\u0000"+
		"\u04c9\u04cc\u0003\u00d4j\u0000\u04ca\u04cc\u0003\u00d8l\u0000\u04cb\u04c8"+
		"\u0001\u0000\u0000\u0000\u04cb\u04c9\u0001\u0000\u0000\u0000\u04cb\u04ca"+
		"\u0001\u0000\u0000\u0000\u04cc\u04cd\u0001\u0000\u0000\u0000\u04cd\u04ce"+
		"\u0003\u00b8\\\u0000\u04ce\u04d0\u0001\u0000\u0000\u0000\u04cf\u04cb\u0001"+
		"\u0000\u0000\u0000\u04cf\u04d0\u0001\u0000\u0000\u0000\u04d0\u00b7\u0001"+
		"\u0000\u0000\u0000\u04d1\u04d6\u0003\u00ba]\u0000\u04d2\u04d3\u00053\u0000"+
		"\u0000\u04d3\u04d5\u0003\u00ba]\u0000\u04d4\u04d2\u0001\u0000\u0000\u0000"+
		"\u04d5\u04d8\u0001\u0000\u0000\u0000\u04d6\u04d4\u0001\u0000\u0000\u0000"+
		"\u04d6\u04d7\u0001\u0000\u0000\u0000\u04d7\u00b9\u0001\u0000\u0000\u0000"+
		"\u04d8\u04d6\u0001\u0000\u0000\u0000\u04d9\u04dc\u0003\u00bc^\u0000\u04da"+
		"\u04db\u0005\u009c\u0000\u0000\u04db\u04dd\u0003\u00bc^\u0000\u04dc\u04da"+
		"\u0001\u0000\u0000\u0000\u04dc\u04dd\u0001\u0000\u0000\u0000\u04dd\u00bb"+
		"\u0001\u0000\u0000\u0000\u04de\u04e3\u0003\u00be_\u0000\u04df\u04e0\u0007"+
		"\u0007\u0000\u0000\u04e0\u04e2\u0003\u00be_\u0000\u04e1\u04df\u0001\u0000"+
		"\u0000\u0000\u04e2\u04e5\u0001\u0000\u0000\u0000\u04e3\u04e1\u0001\u0000"+
		"\u0000\u0000\u04e3\u04e4\u0001\u0000\u0000\u0000\u04e4\u00bd\u0001\u0000"+
		"\u0000\u0000\u04e5\u04e3\u0001\u0000\u0000\u0000\u04e6\u04eb\u0003\u00c0"+
		"`\u0000\u04e7\u04e8\u0007\b\u0000\u0000\u04e8\u04ea\u0003\u00c0`\u0000"+
		"\u04e9\u04e7\u0001\u0000\u0000\u0000\u04ea\u04ed\u0001\u0000\u0000\u0000"+
		"\u04eb\u04e9\u0001\u0000\u0000\u0000\u04eb\u04ec\u0001\u0000\u0000\u0000"+
		"\u04ec\u00bf\u0001\u0000\u0000\u0000\u04ed\u04eb\u0001\u0000\u0000\u0000"+
		"\u04ee\u04f3\u0003\u00c2a\u0000\u04ef\u04f0\u0007\t\u0000\u0000\u04f0"+
		"\u04f2\u0003\u00c2a\u0000\u04f1\u04ef\u0001\u0000\u0000\u0000\u04f2\u04f5"+
		"\u0001\u0000\u0000\u0000\u04f3\u04f1\u0001\u0000\u0000\u0000\u04f3\u04f4"+
		"\u0001\u0000\u0000\u0000\u04f4\u00c1\u0001\u0000\u0000\u0000\u04f5\u04f3"+
		"\u0001\u0000\u0000\u0000\u04f6\u04fb\u0003\u00c4b\u0000\u04f7\u04f8\u0007"+
		"\n\u0000\u0000\u04f8\u04fa\u0003\u00c4b\u0000\u04f9\u04f7\u0001\u0000"+
		"\u0000\u0000\u04fa\u04fd\u0001\u0000\u0000\u0000\u04fb\u04f9\u0001\u0000"+
		"\u0000\u0000\u04fb\u04fc\u0001\u0000\u0000\u0000\u04fc\u00c3\u0001\u0000"+
		"\u0000\u0000\u04fd\u04fb\u0001\u0000\u0000\u0000\u04fe\u0502\u0003\u00c6"+
		"c\u0000\u04ff\u0500\u0005m\u0000\u0000\u0500\u0501\u0005\u0080\u0000\u0000"+
		"\u0501\u0503\u0003\u01a8\u00d4\u0000\u0502\u04ff\u0001\u0000\u0000\u0000"+
		"\u0502\u0503\u0001\u0000\u0000\u0000\u0503\u00c5\u0001\u0000\u0000\u0000"+
		"\u0504\u0508\u0003\u00c8d\u0000\u0505\u0506\u0005\u009d\u0000\u0000\u0506"+
		"\u0507\u0005:\u0000\u0000\u0507\u0509\u0003\u01a8\u00d4\u0000\u0508\u0505"+
		"\u0001\u0000\u0000\u0000\u0508\u0509\u0001\u0000\u0000\u0000\u0509\u00c7"+
		"\u0001\u0000\u0000\u0000\u050a\u050e\u0003\u00cae\u0000\u050b\u050c\u0005"+
		"D\u0000\u0000\u050c\u050d\u0005:\u0000\u0000\u050d\u050f\u0003\u01a4\u00d2"+
		"\u0000\u050e\u050b\u0001\u0000\u0000\u0000\u050e\u050f\u0001\u0000\u0000"+
		"\u0000\u050f\u00c9\u0001\u0000\u0000\u0000\u0510\u0514\u0003\u00ccf\u0000"+
		"\u0511\u0512\u0005C\u0000\u0000\u0512\u0513\u0005:\u0000\u0000\u0513\u0515"+
		"\u0003\u01a4\u00d2\u0000\u0514\u0511\u0001\u0000\u0000\u0000\u0514\u0515"+
		"\u0001\u0000\u0000\u0000\u0515\u00cb\u0001\u0000\u0000\u0000\u0516\u051b"+
		"\u0003\u00d0h\u0000\u0517\u0518\u00051\u0000\u0000\u0518\u051a\u0003\u00ce"+
		"g\u0000\u0519\u0517\u0001\u0000\u0000\u0000\u051a\u051d\u0001\u0000\u0000"+
		"\u0000\u051b\u0519\u0001\u0000\u0000\u0000\u051b\u051c\u0001\u0000\u0000"+
		"\u0000\u051c\u00cd\u0001\u0000\u0000\u0000\u051d\u051b\u0001\u0000\u0000"+
		"\u0000\u051e\u051f\u0003\u012a\u0095\u0000\u051f\u0520\u0003\u0120\u0090"+
		"\u0000\u0520\u00cf\u0001\u0000\u0000\u0000\u0521\u0523\u0007\u0007\u0000"+
		"\u0000\u0522\u0521\u0001\u0000\u0000\u0000\u0523\u0526\u0001\u0000\u0000"+
		"\u0000\u0524\u0522\u0001\u0000\u0000\u0000\u0524\u0525\u0001\u0000\u0000"+
		"\u0000\u0525\u0527\u0001\u0000\u0000\u0000\u0526\u0524\u0001\u0000\u0000"+
		"\u0000\u0527\u0528\u0003\u00d2i\u0000\u0528\u00d1\u0001\u0000\u0000\u0000"+
		"\u0529\u052d\u0003\u00dam\u0000\u052a\u052d\u0003\u00deo\u0000\u052b\u052d"+
		"\u0003\u00e0p\u0000\u052c\u0529\u0001\u0000\u0000\u0000\u052c\u052a\u0001"+
		"\u0000\u0000\u0000\u052c\u052b\u0001\u0000\u0000\u0000\u052d\u00d3\u0001"+
		"\u0000\u0000\u0000\u052e\u0537\u0005\u0013\u0000\u0000\u052f\u0537\u0005"+
		"\u0014\u0000\u0000\u0530\u0537\u0005(\u0000\u0000\u0531\u0532\u0005(\u0000"+
		"\u0000\u0532\u0537\u0005\u0013\u0000\u0000\u0533\u0537\u0005)\u0000\u0000"+
		"\u0534\u0535\u0005)\u0000\u0000\u0535\u0537\u0005\u0013\u0000\u0000\u0536"+
		"\u052e\u0001\u0000\u0000\u0000\u0536\u052f\u0001\u0000\u0000\u0000\u0536"+
		"\u0530\u0001\u0000\u0000\u0000\u0536\u0531\u0001\u0000\u0000\u0000\u0536"+
		"\u0533\u0001\u0000\u0000\u0000\u0536\u0534\u0001\u0000\u0000\u0000\u0537"+
		"\u00d5\u0001\u0000\u0000\u0000\u0538\u0539\u0007\u000b\u0000\u0000\u0539"+
		"\u00d7\u0001\u0000\u0000\u0000\u053a\u0540\u0005o\u0000\u0000\u053b\u053c"+
		"\u0005(\u0000\u0000\u053c\u0540\u0005(\u0000\u0000\u053d\u053e\u0005)"+
		"\u0000\u0000\u053e\u0540\u0005)\u0000\u0000\u053f\u053a\u0001\u0000\u0000"+
		"\u0000\u053f\u053b\u0001\u0000\u0000\u0000\u053f\u053d\u0001\u0000\u0000"+
		"\u0000\u0540\u00d9\u0001\u0000\u0000\u0000\u0541\u0545\u0005\u00a5\u0000"+
		"\u0000\u0542\u0546\u0003\u00dcn\u0000\u0543\u0544\u0007\f\u0000\u0000"+
		"\u0544\u0546\u0003\u01d2\u00e9\u0000\u0545\u0542\u0001\u0000\u0000\u0000"+
		"\u0545\u0543\u0001\u0000\u0000\u0000\u0545\u0546\u0001\u0000\u0000\u0000"+
		"\u0546\u0547\u0001\u0000\u0000\u0000\u0547\u0548\u0003\u00a2Q\u0000\u0548"+
		"\u00db\u0001\u0000\u0000\u0000\u0549\u054a\u0007\r\u0000\u0000\u054a\u00dd"+
		"\u0001\u0000\u0000\u0000\u054b\u054d\u0005\u0011\u0000\u0000\u054c\u054b"+
		"\u0001\u0000\u0000\u0000\u054d\u054e\u0001\u0000\u0000\u0000\u054e\u054c"+
		"\u0001\u0000\u0000\u0000\u054e\u054f\u0001\u0000\u0000\u0000\u054f\u0550"+
		"\u0001\u0000\u0000\u0000\u0550\u0551\u0005\u0019\u0000\u0000\u0551\u0552"+
		"\u0003R)\u0000\u0552\u0553\u0005\u001a\u0000\u0000\u0553\u00df\u0001\u0000"+
		"\u0000\u0000\u0554\u0559\u0003\u0104\u0082\u0000\u0555\u0556\u0005.\u0000"+
		"\u0000\u0556\u0558\u0003\u0104\u0082\u0000\u0557\u0555\u0001\u0000\u0000"+
		"\u0000\u0558\u055b\u0001\u0000\u0000\u0000\u0559\u0557\u0001\u0000\u0000"+
		"\u0000\u0559\u055a\u0001\u0000\u0000\u0000\u055a\u00e1\u0001\u0000\u0000"+
		"\u0000\u055b\u0559\u0001\u0000\u0000\u0000\u055c\u056a\u0003\u00e4r\u0000"+
		"\u055d\u056a\u0003\u00e6s\u0000\u055e\u056a\u0003\u00e8t\u0000\u055f\u056a"+
		"\u0003\u00eau\u0000\u0560\u056a\u0003\u00ecv\u0000\u0561\u056a\u0003\u00ee"+
		"w\u0000\u0562\u056a\u0003\u00f0x\u0000\u0563\u056a\u0003\u00f4z\u0000"+
		"\u0564\u056a\u0003\u00f6{\u0000\u0565\u056a\u0003\u00fa}\u0000\u0566\u056a"+
		"\u0003\u00fc~\u0000\u0567\u056a\u0003\u0100\u0080\u0000\u0568\u056a\u0003"+
		"\u0102\u0081\u0000\u0569\u055c\u0001\u0000\u0000\u0000\u0569\u055d\u0001"+
		"\u0000\u0000\u0000\u0569\u055e\u0001\u0000\u0000\u0000\u0569\u055f\u0001"+
		"\u0000\u0000\u0000\u0569\u0560\u0001\u0000\u0000\u0000\u0569\u0561\u0001"+
		"\u0000\u0000\u0000\u0569\u0562\u0001\u0000\u0000\u0000\u0569\u0563\u0001"+
		"\u0000\u0000\u0000\u0569\u0564\u0001\u0000\u0000\u0000\u0569\u0565\u0001"+
		"\u0000\u0000\u0000\u0569\u0566\u0001\u0000\u0000\u0000\u0569\u0567\u0001"+
		"\u0000\u0000\u0000\u0569\u0568\u0001\u0000\u0000\u0000\u056a\u00e3\u0001"+
		"\u0000\u0000\u0000\u056b\u056c\u0003V+\u0000\u056c\u056d\u0005#\u0000"+
		"\u0000\u056d\u00e5\u0001\u0000\u0000\u0000\u056e\u056f\u0005,\u0000\u0000"+
		"\u056f\u0570\u0003\u0134\u009a\u0000\u0570\u0571\u0005\"\u0000\u0000\u0571"+
		"\u0572\u0003T*\u0000\u0572\u0573\u0005#\u0000\u0000\u0573\u00e7\u0001"+
		"\u0000\u0000\u0000\u0574\u0575\u0005\u0019\u0000\u0000\u0575\u0576\u0003"+
		"L&\u0000\u0576\u0577\u0005\u001a\u0000\u0000\u0577\u00e9\u0001\u0000\u0000"+
		"\u0000\u0578\u0579\u0005\u00ac\u0000\u0000\u0579\u057a\u0005\u00ad\u0000"+
		"\u0000\u057a\u057b\u0005#\u0000\u0000\u057b\u00eb\u0001\u0000\u0000\u0000"+
		"\u057c\u057d\u0005\u00ae\u0000\u0000\u057d\u057e\u0005\u00ad\u0000\u0000"+
		"\u057e\u057f\u0005#\u0000\u0000\u057f\u00ed\u0001\u0000\u0000\u0000\u0580"+
		"\u0581\u0005\u00af\u0000\u0000\u0581\u0582\u0005\u00b0\u0000\u0000\u0582"+
		"\u0583\u0003T*\u0000\u0583\u0584\u0005#\u0000\u0000\u0584\u00ef\u0001"+
		"\u0000\u0000\u0000\u0585\u0589\u0003\\.\u0000\u0586\u0588\u0003^/\u0000"+
		"\u0587\u0586\u0001\u0000\u0000\u0000\u0588\u058b\u0001\u0000\u0000\u0000"+
		"\u0589\u0587\u0001\u0000\u0000\u0000\u0589\u058a\u0001\u0000\u0000\u0000"+
		"\u058a\u058c\u0001\u0000\u0000\u0000\u058b\u0589\u0001\u0000\u0000\u0000"+
		"\u058c\u058d\u0003\u00f2y\u0000\u058d\u00f1\u0001\u0000\u0000\u0000\u058e"+
		"\u058f\u0005\u008d\u0000\u0000\u058f\u0590\u0003\u00e2q\u0000\u0590\u00f3"+
		"\u0001\u0000\u0000\u0000\u0591\u0592\u0005i\u0000\u0000\u0592\u0593\u0005"+
		"\u0015\u0000\u0000\u0593\u0594\u0003R)\u0000\u0594\u0595\u0005\u0016\u0000"+
		"\u0000\u0595\u0596\u0005\u009b\u0000\u0000\u0596\u0597\u0003\u00e2q\u0000"+
		"\u0597\u0598\u0005W\u0000\u0000\u0598\u0599\u0003\u00e2q\u0000\u0599\u00f5"+
		"\u0001\u0000\u0000\u0000\u059a\u059b\u0005\u0099\u0000\u0000\u059b\u059c"+
		"\u0005\u0015\u0000\u0000\u059c\u059d\u0003R)\u0000\u059d\u059f\u0005\u0016"+
		"\u0000\u0000\u059e\u05a0\u0003\u00f8|\u0000\u059f\u059e\u0001\u0000\u0000"+
		"\u0000\u05a0\u05a1\u0001\u0000\u0000\u0000\u05a1\u059f\u0001\u0000\u0000"+
		"\u0000\u05a1\u05a2\u0001\u0000\u0000\u0000\u05a2\u05a3\u0001\u0000\u0000"+
		"\u0000\u05a3\u05a4\u0005N\u0000\u0000\u05a4\u05a5\u0005\u008d\u0000\u0000"+
		"\u05a5\u05a6\u0003\u00e2q\u0000\u05a6\u00f7\u0001\u0000\u0000\u0000\u05a7"+
		"\u05a8\u0005B\u0000\u0000\u05a8\u05aa\u0003\u0090H\u0000\u05a9\u05a7\u0001"+
		"\u0000\u0000\u0000\u05aa\u05ab\u0001\u0000\u0000\u0000\u05ab\u05a9\u0001"+
		"\u0000\u0000\u0000\u05ab\u05ac\u0001\u0000\u0000\u0000\u05ac\u05ad\u0001"+
		"\u0000\u0000\u0000\u05ad\u05ae\u0005\u008d\u0000\u0000\u05ae\u05af\u0003"+
		"\u00e2q\u0000\u05af\u00f9\u0001\u0000\u0000\u0000\u05b0\u05b1\u0005\u009e"+
		"\u0000\u0000\u05b1\u05b6\u0003\u00e8t\u0000\u05b2\u05b3\u0005E\u0000\u0000"+
		"\u05b3\u05b4\u0003\u00a4R\u0000\u05b4\u05b5\u0003\u00e8t\u0000\u05b5\u05b7"+
		"\u0001\u0000\u0000\u0000\u05b6\u05b2\u0001\u0000\u0000\u0000\u05b7\u05b8"+
		"\u0001\u0000\u0000\u0000\u05b8\u05b6\u0001\u0000\u0000\u0000\u05b8\u05b9"+
		"\u0001\u0000\u0000\u0000\u05b9\u00fb\u0001\u0000\u0000\u0000\u05ba\u05bb"+
		"\u0005\u00a1\u0000\u0000\u05bb\u05bc\u0005\u0015\u0000\u0000\u05bc\u05bd"+
		"\u0003R)\u0000\u05bd\u05bf\u0005\u0016\u0000\u0000\u05be\u05c0\u0003\u00fe"+
		"\u007f\u0000\u05bf\u05be\u0001\u0000\u0000\u0000\u05c0\u05c1\u0001\u0000"+
		"\u0000\u0000\u05c1\u05bf\u0001\u0000\u0000\u0000\u05c1\u05c2\u0001\u0000"+
		"\u0000\u0000\u05c2\u05c3\u0001\u0000\u0000\u0000\u05c3\u05c6\u0005N\u0000"+
		"\u0000\u05c4\u05c5\u0005,\u0000\u0000\u05c5\u05c7\u0003\u0134\u009a\u0000"+
		"\u05c6\u05c4\u0001\u0000\u0000\u0000\u05c6\u05c7\u0001\u0000\u0000\u0000"+
		"\u05c7\u05c8\u0001\u0000\u0000\u0000\u05c8\u05c9\u0005\u008d\u0000\u0000"+
		"\u05c9\u05ca\u0003\u00e2q\u0000\u05ca\u00fd\u0001\u0000\u0000\u0000\u05cb"+
		"\u05d0\u0005B\u0000\u0000\u05cc\u05cd\u0005,\u0000\u0000\u05cd\u05ce\u0003"+
		"\u0134\u009a\u0000\u05ce\u05cf\u0005:\u0000\u0000\u05cf\u05d1\u0001\u0000"+
		"\u0000\u0000\u05d0\u05cc\u0001\u0000\u0000\u0000\u05d0\u05d1\u0001\u0000"+
		"\u0000\u0000\u05d1\u05d2\u0001\u0000\u0000\u0000\u05d2\u05d3\u0003\u01a8"+
		"\u00d4\u0000\u05d3\u05d4\u0005\u008d\u0000\u0000\u05d4\u05d5\u0003\u00e2"+
		"q\u0000\u05d5\u00ff\u0001\u0000\u0000\u0000\u05d6\u05d8\u0003B!\u0000"+
		"\u05d7\u05d6\u0001\u0000\u0000\u0000\u05d8\u05db\u0001\u0000\u0000\u0000"+
		"\u05d9\u05d7\u0001\u0000\u0000\u0000\u05d9\u05da\u0001\u0000\u0000\u0000"+
		"\u05da\u05dc\u0001\u0000\u0000\u0000\u05db\u05d9\u0001\u0000\u0000\u0000"+
		"\u05dc\u05dd\u0005\u00a6\u0000\u0000\u05dd\u05de\u0005,\u0000\u0000\u05de"+
		"\u05e0\u0003\u0134\u009a\u0000\u05df\u05e1\u0003\u01a6\u00d3\u0000\u05e0"+
		"\u05df\u0001\u0000\u0000\u0000\u05e0\u05e1\u0001\u0000\u0000\u0000\u05e1"+
		"\u05e4\u0001\u0000\u0000\u0000\u05e2\u05e3\u0005\"\u0000\u0000\u05e3\u05e5"+
		"\u0003T*\u0000\u05e4\u05e2\u0001\u0000\u0000\u0000\u05e4\u05e5\u0001\u0000"+
		"\u0000\u0000\u05e5\u05f2\u0001\u0000\u0000\u0000\u05e6\u05e7\u0005\u001e"+
		"\u0000\u0000\u05e7\u05e8\u0005,\u0000\u0000\u05e8\u05ea\u0003\u0134\u009a"+
		"\u0000\u05e9\u05eb\u0003\u01a6\u00d3\u0000\u05ea\u05e9\u0001\u0000\u0000"+
		"\u0000\u05ea\u05eb\u0001\u0000\u0000\u0000\u05eb\u05ee\u0001\u0000\u0000"+
		"\u0000\u05ec\u05ed\u0005\"\u0000\u0000\u05ed\u05ef\u0003T*\u0000\u05ee"+
		"\u05ec\u0001\u0000\u0000\u0000\u05ee\u05ef\u0001\u0000\u0000\u0000\u05ef"+
		"\u05f1\u0001\u0000\u0000\u0000\u05f0\u05e6\u0001\u0000\u0000\u0000\u05f1"+
		"\u05f4\u0001\u0000\u0000\u0000\u05f2\u05f0\u0001\u0000\u0000\u0000\u05f2"+
		"\u05f3\u0001\u0000\u0000\u0000\u05f3\u05f5\u0001\u0000\u0000\u0000\u05f4"+
		"\u05f2\u0001\u0000\u0000\u0000\u05f5\u05f6\u0005#\u0000\u0000\u05f6\u0101"+
		"\u0001\u0000\u0000\u0000\u05f7\u05f8\u0005\u00b1\u0000\u0000\u05f8\u05f9"+
		"\u0005\u0015\u0000\u0000\u05f9\u05fa\u0003R)\u0000\u05fa\u05fb\u0005\u0016"+
		"\u0000\u0000\u05fb\u05fc\u0003\u00e2q\u0000\u05fc\u0103\u0001\u0000\u0000"+
		"\u0000\u05fd\u05ff\u0005$\u0000\u0000\u05fe\u0600\u0003\u0106\u0083\u0000"+
		"\u05ff\u05fe\u0001\u0000\u0000\u0000\u05ff\u0600\u0001\u0000\u0000\u0000"+
		"\u0600\u0605\u0001\u0000\u0000\u0000\u0601\u0602\u0005%\u0000\u0000\u0602"+
		"\u0605\u0003\u0106\u0083\u0000\u0603\u0605\u0003\u0106\u0083\u0000\u0604"+
		"\u05fd\u0001\u0000\u0000\u0000\u0604\u0601\u0001\u0000\u0000\u0000\u0604"+
		"\u0603\u0001\u0000\u0000\u0000\u0605\u0105\u0001\u0000\u0000\u0000\u0606"+
		"\u060b\u0003\u0108\u0084\u0000\u0607\u0608\u0007\u000e\u0000\u0000\u0608"+
		"\u060a\u0003\u0108\u0084\u0000\u0609\u0607\u0001\u0000\u0000\u0000\u060a"+
		"\u060d\u0001\u0000\u0000\u0000\u060b\u0609\u0001\u0000\u0000\u0000\u060b"+
		"\u060c\u0001\u0000\u0000\u0000\u060c\u0107\u0001\u0000\u0000\u0000\u060d"+
		"\u060b\u0001\u0000\u0000\u0000\u060e\u0611\u0003\u011e\u008f\u0000\u060f"+
		"\u0611\u0003\u010a\u0085\u0000\u0610\u060e\u0001\u0000\u0000\u0000\u0610"+
		"\u060f\u0001\u0000\u0000\u0000\u0611\u0109\u0001\u0000\u0000\u0000\u0612"+
		"\u0615\u0003\u0112\u0089\u0000\u0613\u0615\u0003\u010c\u0086\u0000\u0614"+
		"\u0612\u0001\u0000\u0000\u0000\u0614\u0613\u0001\u0000\u0000\u0000\u0615"+
		"\u0616\u0001\u0000\u0000\u0000\u0616\u0617\u0003\u0122\u0091\u0000\u0617"+
		"\u010b\u0001\u0000\u0000\u0000\u0618\u0619\u0003\u010e\u0087\u0000\u0619"+
		"\u061a\u0003\u0118\u008c\u0000\u061a\u061d\u0001\u0000\u0000\u0000\u061b"+
		"\u061d\u0003\u0110\u0088\u0000\u061c\u0618\u0001\u0000\u0000\u0000\u061c"+
		"\u061b\u0001\u0000\u0000\u0000\u061d\u010d\u0001\u0000\u0000\u0000\u061e"+
		"\u061f\u0007\u000f\u0000\u0000\u061f\u0620\u0005!\u0000\u0000\u0620\u0621"+
		"\u0005!\u0000\u0000\u0621\u010f\u0001\u0000\u0000\u0000\u0622\u0624\u0005"+
		"+\u0000\u0000\u0623\u0622\u0001\u0000\u0000\u0000\u0623\u0624\u0001\u0000"+
		"\u0000\u0000\u0624\u0625\u0001\u0000\u0000\u0000\u0625\u0626\u0003\u0118"+
		"\u008c\u0000\u0626\u0111\u0001\u0000\u0000\u0000\u0627\u0628\u0003\u0114"+
		"\u008a\u0000\u0628\u0629\u0003\u0118\u008c\u0000\u0629\u062c\u0001\u0000"+
		"\u0000\u0000\u062a\u062c\u0003\u0116\u008b\u0000\u062b\u0627\u0001\u0000"+
		"\u0000\u0000\u062b\u062a\u0001\u0000\u0000\u0000\u062c\u0113\u0001\u0000"+
		"\u0000\u0000\u062d\u062e\u0007\u0010\u0000\u0000\u062e\u062f\u0005!\u0000"+
		"\u0000\u062f\u0630\u0005!\u0000\u0000\u0630\u0115\u0001\u0000\u0000\u0000"+
		"\u0631\u0632\u0005 \u0000\u0000\u0632\u0117\u0001\u0000\u0000\u0000\u0633"+
		"\u0636\u0003\u011a\u008d\u0000\u0634\u0636\u0003\u01ae\u00d7\u0000\u0635"+
		"\u0633\u0001\u0000\u0000\u0000\u0635\u0634\u0001\u0000\u0000\u0000\u0636"+
		"\u0119\u0001\u0000\u0000\u0000\u0637\u063a\u0003\u01f6\u00fb\u0000\u0638"+
		"\u063a\u0003\u011c\u008e\u0000\u0639\u0637\u0001\u0000\u0000\u0000\u0639"+
		"\u0638\u0001\u0000\u0000\u0000\u063a\u011b\u0001\u0000\u0000\u0000\u063b"+
		"\u063f\u0005\u001b\u0000\u0000\u063c\u063f\u0005\u00c0\u0000\u0000\u063d"+
		"\u063f\u0005\u00c1\u0000\u0000\u063e\u063b\u0001\u0000\u0000\u0000\u063e"+
		"\u063c\u0001\u0000\u0000\u0000\u063e\u063d\u0001\u0000\u0000\u0000\u063f"+
		"\u011d\u0001\u0000\u0000\u0000\u0640\u0646\u0003\u012c\u0096\u0000\u0641"+
		"\u0645\u0003\u0124\u0092\u0000\u0642\u0645\u0003\u0120\u0090\u0000\u0643"+
		"\u0645\u0003\u0126\u0093\u0000\u0644\u0641\u0001\u0000\u0000\u0000\u0644"+
		"\u0642\u0001\u0000\u0000\u0000\u0644\u0643\u0001\u0000\u0000\u0000\u0645"+
		"\u0648\u0001\u0000\u0000\u0000\u0646\u0644\u0001\u0000\u0000\u0000\u0646"+
		"\u0647\u0001\u0000\u0000\u0000\u0647\u011f\u0001\u0000\u0000\u0000\u0648"+
		"\u0646\u0001\u0000\u0000\u0000\u0649\u0652\u0005\u0015\u0000\u0000\u064a"+
		"\u064f\u0003\u0140\u00a0\u0000\u064b\u064c\u0005\u001e\u0000\u0000\u064c"+
		"\u064e\u0003\u0140\u00a0\u0000\u064d\u064b\u0001\u0000\u0000\u0000\u064e"+
		"\u0651\u0001\u0000\u0000\u0000\u064f\u064d\u0001\u0000\u0000\u0000\u064f"+
		"\u0650\u0001\u0000\u0000\u0000\u0650\u0653\u0001\u0000\u0000\u0000\u0651"+
		"\u064f\u0001\u0000\u0000\u0000\u0652\u064a\u0001\u0000\u0000\u0000\u0652"+
		"\u0653\u0001\u0000\u0000\u0000\u0653\u0654\u0001\u0000\u0000\u0000\u0654"+
		"\u0655\u0005\u0016\u0000\u0000\u0655\u0121\u0001\u0000\u0000\u0000\u0656"+
		"\u0658\u0003\u0124\u0092\u0000\u0657\u0656\u0001\u0000\u0000\u0000\u0658"+
		"\u065b\u0001\u0000\u0000\u0000\u0659\u0657\u0001\u0000\u0000\u0000\u0659"+
		"\u065a\u0001\u0000\u0000\u0000\u065a\u0123\u0001\u0000\u0000\u0000\u065b"+
		"\u0659\u0001\u0000\u0000\u0000\u065c\u065d\u0005\u0017\u0000\u0000\u065d"+
		"\u065e\u0003R)\u0000\u065e\u065f\u0005\u0018\u0000\u0000\u065f\u0125\u0001"+
		"\u0000\u0000\u0000\u0660\u0661\u0005*\u0000\u0000\u0661\u0662\u0003\u0128"+
		"\u0094\u0000\u0662\u0127\u0001\u0000\u0000\u0000\u0663\u0668\u0003\u01fa"+
		"\u00fd\u0000\u0664\u0668\u0005\u0005\u0000\u0000\u0665\u0668\u0003\u0136"+
		"\u009b\u0000\u0666\u0668\u0005\u001b\u0000\u0000\u0667\u0663\u0001\u0000"+
		"\u0000\u0000\u0667\u0664\u0001\u0000\u0000\u0000\u0667\u0665\u0001\u0000"+
		"\u0000\u0000\u0667\u0666\u0001\u0000\u0000\u0000\u0668\u0129\u0001\u0000"+
		"\u0000\u0000\u0669\u066d\u0003\u01f6\u00fb\u0000\u066a\u066d\u0003\u0132"+
		"\u0099\u0000\u066b\u066d\u0003\u0136\u009b\u0000\u066c\u0669\u0001\u0000"+
		"\u0000\u0000\u066c\u066a\u0001\u0000\u0000\u0000\u066c\u066b\u0001\u0000"+
		"\u0000\u0000\u066d\u012b\u0001\u0000\u0000\u0000\u066e\u067d\u0003\u012e"+
		"\u0097\u0000\u066f\u067d\u0003\u0132\u0099\u0000\u0670\u067d\u0003\u0136"+
		"\u009b\u0000\u0671\u067d\u0003\u0138\u009c\u0000\u0672\u067d\u0003\u013e"+
		"\u009f\u0000\u0673\u067d\u0003\u013a\u009d\u0000\u0674\u067d\u0003\u013c"+
		"\u009e\u0000\u0675\u067d\u0003\u0142\u00a1\u0000\u0676\u067d\u0003\u0182"+
		"\u00c1\u0000\u0677\u067d\u0003\u018a\u00c5\u0000\u0678\u067d\u0003\u018e"+
		"\u00c7\u0000\u0679\u067d\u0003\u0194\u00ca\u0000\u067a\u067d\u0003\u01a2"+
		"\u00d1\u0000\u067b\u067d\u0003X,\u0000\u067c\u066e\u0001\u0000\u0000\u0000"+
		"\u067c\u066f\u0001\u0000\u0000\u0000\u067c\u0670\u0001\u0000\u0000\u0000"+
		"\u067c\u0671\u0001\u0000\u0000\u0000\u067c\u0672\u0001\u0000\u0000\u0000"+
		"\u067c\u0673\u0001\u0000\u0000\u0000\u067c\u0674\u0001\u0000\u0000\u0000"+
		"\u067c\u0675\u0001\u0000\u0000\u0000\u067c\u0676\u0001\u0000\u0000\u0000"+
		"\u067c\u0677\u0001\u0000\u0000\u0000\u067c\u0678\u0001\u0000\u0000\u0000"+
		"\u067c\u0679\u0001\u0000\u0000\u0000\u067c\u067a\u0001\u0000\u0000\u0000"+
		"\u067c\u067b\u0001\u0000\u0000\u0000\u067d\u012d\u0001\u0000\u0000\u0000"+
		"\u067e\u0681\u0003\u0130\u0098\u0000\u067f\u0681\u0003\u020a\u0105\u0000"+
		"\u0680\u067e\u0001\u0000\u0000\u0000\u0680\u067f\u0001\u0000\u0000\u0000"+
		"\u0681\u012f\u0001\u0000\u0000\u0000\u0682\u0683\u0007\u0011\u0000\u0000"+
		"\u0683\u0131\u0001\u0000\u0000\u0000\u0684\u0685\u0005,\u0000\u0000\u0685"+
		"\u0686\u0003\u01f6\u00fb\u0000\u0686\u0133\u0001\u0000\u0000\u0000\u0687"+
		"\u0688\u0003\u01f6\u00fb\u0000\u0688\u0135\u0001\u0000\u0000\u0000\u0689"+
		"\u068b\u0005\u0015\u0000\u0000\u068a\u068c\u0003R)\u0000\u068b\u068a\u0001"+
		"\u0000\u0000\u0000\u068b\u068c\u0001\u0000\u0000\u0000\u068c\u068d\u0001"+
		"\u0000\u0000\u0000\u068d\u068e\u0005\u0016\u0000\u0000\u068e\u0137\u0001"+
		"\u0000\u0000\u0000\u068f\u0690\u0005\u001f\u0000\u0000\u0690\u0139\u0001"+
		"\u0000\u0000\u0000\u0691\u0692\u0005\u0085\u0000\u0000\u0692\u0693\u0003"+
		"\u00a2Q\u0000\u0693\u013b\u0001\u0000\u0000\u0000\u0694\u0695\u0005\u00a3"+
		"\u0000\u0000\u0695\u0696\u0003\u00a2Q\u0000\u0696\u013d\u0001\u0000\u0000"+
		"\u0000\u0697\u0698\u0003\u01f6\u00fb\u0000\u0698\u0699\u0003\u0120\u0090"+
		"\u0000\u0699\u013f\u0001\u0000\u0000\u0000\u069a\u069d\u0003T*\u0000\u069b"+
		"\u069d\u0005*\u0000\u0000\u069c\u069a\u0001\u0000\u0000\u0000\u069c\u069b"+
		"\u0001\u0000\u0000\u0000\u069d\u0141\u0001\u0000\u0000\u0000\u069e\u06a1"+
		"\u0003\u0144\u00a2\u0000\u069f\u06a1\u0003\u015c\u00ae\u0000\u06a0\u069e"+
		"\u0001\u0000\u0000\u0000\u06a0\u069f\u0001\u0000\u0000\u0000\u06a1\u0143"+
		"\u0001\u0000\u0000\u0000\u06a2\u06a6\u0003\u0146\u00a3\u0000\u06a3\u06a6"+
		"\u0003\u0148\u00a4\u0000\u06a4\u06a6\u0007\u0012\u0000\u0000\u06a5\u06a2"+
		"\u0001\u0000\u0000\u0000\u06a5\u06a3\u0001\u0000\u0000\u0000\u06a5\u06a4"+
		"\u0001\u0000\u0000\u0000\u06a6\u0145\u0001\u0000\u0000\u0000\u06a7\u06a8"+
		"\u0005(\u0000\u0000\u06a8\u06a9\u0003\u01f8\u00fc\u0000\u06a9\u06aa\u0003"+
		"\u014a\u00a5\u0000\u06aa\u06ae\u0005)\u0000\u0000\u06ab\u06ad\u0003\u0156"+
		"\u00ab\u0000\u06ac\u06ab\u0001\u0000\u0000\u0000\u06ad\u06b0\u0001\u0000"+
		"\u0000\u0000\u06ae\u06ac\u0001\u0000\u0000\u0000\u06ae\u06af\u0001\u0000"+
		"\u0000\u0000\u06af\u06b1\u0001\u0000\u0000\u0000\u06b0\u06ae\u0001\u0000"+
		"\u0000\u0000\u06b1\u06b2\u0005(\u0000\u0000\u06b2\u06b3\u0005$\u0000\u0000"+
		"\u06b3\u06b4\u0003\u01f8\u00fc\u0000\u06b4\u06b5\u0005)\u0000\u0000\u06b5"+
		"\u0147\u0001\u0000\u0000\u0000\u06b6\u06b7\u0005(\u0000\u0000\u06b7\u06b8"+
		"\u0003\u01f8\u00fc\u0000\u06b8\u06b9\u0003\u014a\u00a5\u0000\u06b9\u06ba"+
		"\u0005$\u0000\u0000\u06ba\u06bb\u0005)\u0000\u0000\u06bb\u0149\u0001\u0000"+
		"\u0000\u0000\u06bc\u06bd\u0003\u01f8\u00fc\u0000\u06bd\u06be\u0005\u0013"+
		"\u0000\u0000\u06be\u06bf\u0003\u0150\u00a8\u0000\u06bf\u06c1\u0001\u0000"+
		"\u0000\u0000\u06c0\u06bc\u0001\u0000\u0000\u0000\u06c1\u06c4\u0001\u0000"+
		"\u0000\u0000\u06c2\u06c0\u0001\u0000\u0000\u0000\u06c2\u06c3\u0001\u0000"+
		"\u0000\u0000\u06c3\u014b\u0001\u0000\u0000\u0000\u06c4\u06c2\u0001\u0000"+
		"\u0000\u0000\u06c5\u06cc\u0005\u000b\u0000\u0000\u06c6\u06cb\u0005\t\u0000"+
		"\u0000\u06c7\u06cb\u0005\n\u0000\u0000\u06c8\u06cb\u0005\u0001\u0000\u0000"+
		"\u06c9\u06cb\u0003\u0152\u00a9\u0000\u06ca\u06c6\u0001\u0000\u0000\u0000"+
		"\u06ca\u06c7\u0001\u0000\u0000\u0000\u06ca\u06c8\u0001\u0000\u0000\u0000"+
		"\u06ca\u06c9\u0001\u0000\u0000\u0000\u06cb\u06ce\u0001\u0000\u0000\u0000"+
		"\u06cc\u06ca\u0001\u0000\u0000\u0000\u06cc\u06cd\u0001\u0000\u0000\u0000"+
		"\u06cd\u06cf\u0001\u0000\u0000\u0000\u06ce\u06cc\u0001\u0000\u0000\u0000"+
		"\u06cf\u06d0\u0005\u000b\u0000\u0000\u06d0\u014d\u0001\u0000\u0000\u0000"+
		"\u06d1\u06d8\u0005\f\u0000\u0000\u06d2\u06d7\u0005\t\u0000\u0000\u06d3"+
		"\u06d7\u0005\n\u0000\u0000\u06d4\u06d7\u0005\u0002\u0000\u0000\u06d5\u06d7"+
		"\u0003\u0154\u00aa\u0000\u06d6\u06d2\u0001\u0000\u0000\u0000\u06d6\u06d3"+
		"\u0001\u0000\u0000\u0000\u06d6\u06d4\u0001\u0000\u0000\u0000\u06d6\u06d5"+
		"\u0001\u0000\u0000\u0000\u06d7\u06da\u0001\u0000\u0000\u0000\u06d8\u06d6"+
		"\u0001\u0000\u0000\u0000\u06d8\u06d9\u0001\u0000\u0000\u0000\u06d9\u06db"+
		"\u0001\u0000\u0000\u0000\u06da\u06d8\u0001\u0000\u0000\u0000\u06db\u06dc"+
		"\u0005\f\u0000\u0000\u06dc\u014f\u0001\u0000\u0000\u0000\u06dd\u06e0\u0003"+
		"\u014c\u00a6\u0000\u06de\u06e0\u0003\u014e\u00a7\u0000\u06df\u06dd\u0001"+
		"\u0000\u0000\u0000\u06df\u06de\u0001\u0000\u0000\u0000\u06e0\u0151\u0001"+
		"\u0000\u0000\u0000\u06e1\u06e3\u0005\u00ca\u0000\u0000\u06e2\u06e1\u0001"+
		"\u0000\u0000\u0000\u06e3\u06e4\u0001\u0000\u0000\u0000\u06e4\u06e2\u0001"+
		"\u0000\u0000\u0000\u06e4\u06e5\u0001\u0000\u0000\u0000\u06e5\u06ef\u0001"+
		"\u0000\u0000\u0000\u06e6\u06ef\u0005\u0003\u0000\u0000\u06e7\u06ef\u0005"+
		"\u0004\u0000\u0000\u06e8\u06ef\u0003\u014c\u00a6\u0000\u06e9\u06eb\u0005"+
		"\u0019\u0000\u0000\u06ea\u06ec\u0003R)\u0000\u06eb\u06ea\u0001\u0000\u0000"+
		"\u0000\u06eb\u06ec\u0001\u0000\u0000\u0000\u06ec\u06ed\u0001\u0000\u0000"+
		"\u0000\u06ed\u06ef\u0005\u001a\u0000\u0000\u06ee\u06e2\u0001\u0000\u0000"+
		"\u0000\u06ee\u06e6\u0001\u0000\u0000\u0000\u06ee\u06e7\u0001\u0000\u0000"+
		"\u0000\u06ee\u06e8\u0001\u0000\u0000\u0000\u06ee\u06e9\u0001\u0000\u0000"+
		"\u0000\u06ef\u0153\u0001\u0000\u0000\u0000\u06f0\u06f2\u0005\u00ca\u0000"+
		"\u0000\u06f1\u06f0\u0001\u0000\u0000\u0000\u06f2\u06f3\u0001\u0000\u0000"+
		"\u0000\u06f3\u06f1\u0001\u0000\u0000\u0000\u06f3\u06f4\u0001\u0000\u0000"+
		"\u0000\u06f4\u06fe\u0001\u0000\u0000\u0000\u06f5\u06fe\u0005\u0003\u0000"+
		"\u0000\u06f6\u06fe\u0005\u0004\u0000\u0000\u06f7\u06fe\u0003\u014e\u00a7"+
		"\u0000\u06f8\u06fa\u0005\u0019\u0000\u0000\u06f9\u06fb\u0003R)\u0000\u06fa"+
		"\u06f9\u0001\u0000\u0000\u0000\u06fa\u06fb\u0001\u0000\u0000\u0000\u06fb"+
		"\u06fc\u0001\u0000\u0000\u0000\u06fc\u06fe\u0005\u001a\u0000\u0000\u06fd"+
		"\u06f1\u0001\u0000\u0000\u0000\u06fd\u06f5\u0001\u0000\u0000\u0000\u06fd"+
		"\u06f6\u0001\u0000\u0000\u0000\u06fd\u06f7\u0001\u0000\u0000\u0000\u06fd"+
		"\u06f8\u0001\u0000\u0000\u0000\u06fe\u0155\u0001\u0000\u0000\u0000\u06ff"+
		"\u0706\u0003\u0144\u00a2\u0000\u0700\u0706\u0003\u0158\u00ac\u0000\u0701"+
		"\u0706\u0005\u0010\u0000\u0000\u0702\u0706\u0005\u000b\u0000\u0000\u0703"+
		"\u0706\u0005\f\u0000\u0000\u0704\u0706\u0003\u0210\u0108\u0000\u0705\u06ff"+
		"\u0001\u0000\u0000\u0000\u0705\u0700\u0001\u0000\u0000\u0000\u0705\u0701"+
		"\u0001\u0000\u0000\u0000\u0705\u0702\u0001\u0000\u0000\u0000\u0705\u0703"+
		"\u0001\u0000\u0000\u0000\u0705\u0704\u0001\u0000\u0000\u0000\u0706\u0157"+
		"\u0001\u0000\u0000\u0000\u0707\u070e\u0007\u0013\u0000\u0000\u0708\u0709"+
		"\u0005\u0019\u0000\u0000\u0709\u070e\u0005\u0019\u0000\u0000\u070a\u070b"+
		"\u0005\u001a\u0000\u0000\u070b\u070e\u0005\u001a\u0000\u0000\u070c\u070e"+
		"\u0003X,\u0000\u070d\u0707\u0001\u0000\u0000\u0000\u070d\u0708\u0001\u0000"+
		"\u0000\u0000\u070d\u070a\u0001\u0000\u0000\u0000\u070d\u070c\u0001\u0000"+
		"\u0000\u0000\u070e\u0159\u0001\u0000\u0000\u0000\u070f\u0710\u0003N\'"+
		"\u0000\u0710\u015b\u0001\u0000\u0000\u0000\u0711\u071a\u0003\u016c\u00b6"+
		"\u0000\u0712\u071a\u0003\u016e\u00b7\u0000\u0713\u071a\u0003\u0172\u00b9"+
		"\u0000\u0714\u071a\u0003\u0174\u00ba\u0000\u0715\u071a\u0003\u017c\u00be"+
		"\u0000\u0716\u071a\u0003\u017e\u00bf\u0000\u0717\u071a\u0003\u0180\u00c0"+
		"\u0000\u0718\u071a\u0003\u015e\u00af\u0000\u0719\u0711\u0001\u0000\u0000"+
		"\u0000\u0719\u0712\u0001\u0000\u0000\u0000\u0719\u0713\u0001\u0000\u0000"+
		"\u0000\u0719\u0714\u0001\u0000\u0000\u0000\u0719\u0715\u0001\u0000\u0000"+
		"\u0000\u0719\u0716\u0001\u0000\u0000\u0000\u0719\u0717\u0001\u0000\u0000"+
		"\u0000\u0719\u0718\u0001\u0000\u0000\u0000\u071a\u015d\u0001\u0000\u0000"+
		"\u0000\u071b\u0722\u0003\u0160\u00b0\u0000\u071c\u0722\u0003\u0162\u00b1"+
		"\u0000\u071d\u0722\u0003\u0164\u00b2\u0000\u071e\u0722\u0003\u0166\u00b3"+
		"\u0000\u071f\u0722\u0003\u0168\u00b4\u0000\u0720\u0722\u0003\u016a\u00b5"+
		"\u0000\u0721\u071b\u0001\u0000\u0000\u0000\u0721\u071c\u0001\u0000\u0000"+
		"\u0000\u0721\u071d\u0001\u0000\u0000\u0000\u0721\u071e\u0001\u0000\u0000"+
		"\u0000\u0721\u071f\u0001\u0000\u0000\u0000\u0721\u0720\u0001\u0000\u0000"+
		"\u0000\u0722\u015f\u0001\u0000\u0000\u0000\u0723\u0724\u0005\u00b2\u0000"+
		"\u0000\u0724\u0725\u0003\u0170\u00b8\u0000\u0725\u0161\u0001\u0000\u0000"+
		"\u0000\u0726\u0727\u0005\u00b6\u0000\u0000\u0727\u0735\u0005\u0019\u0000"+
		"\u0000\u0728\u0729\u0003T*\u0000\u0729\u072a\u0005!\u0000\u0000\u072a"+
		"\u0732\u0003T*\u0000\u072b\u072c\u0005\u001e\u0000\u0000\u072c\u072d\u0003"+
		"T*\u0000\u072d\u072e\u0005!\u0000\u0000\u072e\u072f\u0003T*\u0000\u072f"+
		"\u0731\u0001\u0000\u0000\u0000\u0730\u072b\u0001\u0000\u0000\u0000\u0731"+
		"\u0734\u0001\u0000\u0000\u0000\u0732\u0730\u0001\u0000\u0000\u0000\u0732"+
		"\u0733\u0001\u0000\u0000\u0000\u0733\u0736\u0001\u0000\u0000\u0000\u0734"+
		"\u0732\u0001\u0000\u0000\u0000\u0735\u0728\u0001\u0000\u0000\u0000\u0735"+
		"\u0736\u0001\u0000\u0000\u0000\u0736\u0737\u0001\u0000\u0000\u0000\u0737"+
		"\u0738\u0005\u001a\u0000\u0000\u0738\u0163\u0001\u0000\u0000\u0000\u0739"+
		"\u073a\u0005\u00b5\u0000\u0000\u073a\u073b\u0003\u0170\u00b8\u0000\u073b"+
		"\u0165\u0001\u0000\u0000\u0000\u073c\u073d\u0005\u00b3\u0000\u0000\u073d"+
		"\u073e\u0005\u0019\u0000\u0000\u073e\u073f\u0003T*\u0000\u073f\u0740\u0005"+
		"\u001a\u0000\u0000\u0740\u0167\u0001\u0000\u0000\u0000\u0741\u0742\u0005"+
		"\u00b4\u0000\u0000\u0742\u0743\u0005\u0019\u0000\u0000\u0743\u0744\u0005"+
		"\u001a\u0000\u0000\u0744\u0169\u0001\u0000\u0000\u0000\u0745\u0746\u0005"+
		"@\u0000\u0000\u0746\u0747\u0003\u0170\u00b8\u0000\u0747\u016b\u0001\u0000"+
		"\u0000\u0000\u0748\u0749\u0005T\u0000\u0000\u0749\u074a\u0003X,\u0000"+
		"\u074a\u016d\u0001\u0000\u0000\u0000\u074b\u0751\u0005V\u0000\u0000\u074c"+
		"\u0752\u0003\u01f6\u00fb\u0000\u074d\u074e\u0005\u0019\u0000\u0000\u074e"+
		"\u074f\u0003R)\u0000\u074f\u0750\u0005\u001a\u0000\u0000\u0750\u0752\u0001"+
		"\u0000\u0000\u0000\u0751\u074c\u0001\u0000\u0000\u0000\u0751\u074d\u0001"+
		"\u0000\u0000\u0000\u0752\u0753\u0001\u0000\u0000\u0000\u0753\u0754\u0003"+
		"\u0170\u00b8\u0000\u0754\u016f\u0001\u0000\u0000\u0000\u0755\u0756\u0003"+
		"\u00a2Q\u0000\u0756\u0171\u0001\u0000\u0000\u0000\u0757\u075d\u0005=\u0000"+
		"\u0000\u0758\u075e\u0003\u01f6\u00fb\u0000\u0759\u075a\u0005\u0019\u0000"+
		"\u0000\u075a\u075b\u0003R)\u0000\u075b\u075c\u0005\u001a\u0000\u0000\u075c"+
		"\u075e\u0001\u0000\u0000\u0000\u075d\u0758\u0001\u0000\u0000\u0000\u075d"+
		"\u0759\u0001\u0000\u0000\u0000\u075e\u0762\u0001\u0000\u0000\u0000\u075f"+
		"\u0760\u0005\u0019\u0000\u0000\u0760\u0763\u0005\u001a\u0000\u0000\u0761"+
		"\u0763\u0003X,\u0000\u0762\u075f\u0001\u0000\u0000\u0000\u0762\u0761\u0001"+
		"\u0000\u0000\u0000\u0763\u0173\u0001\u0000\u0000\u0000\u0764\u0767\u0005"+
		"y\u0000\u0000\u0765\u0768\u0003\u0176\u00bb\u0000\u0766\u0768\u0003\u0178"+
		"\u00bc\u0000\u0767\u0765\u0001\u0000\u0000\u0000\u0767\u0766\u0001\u0000"+
		"\u0000\u0000\u0768\u0769\u0001\u0000\u0000\u0000\u0769\u076a\u0003\u017a"+
		"\u00bd\u0000\u076a\u0175\u0001\u0000\u0000\u0000\u076b\u076c\u0003\u01fa"+
		"\u00fd\u0000\u076c\u0177\u0001\u0000\u0000\u0000\u076d\u076e\u0003\u00a2"+
		"Q\u0000\u076e\u0179\u0001\u0000\u0000\u0000\u076f\u0770\u0003\u00a2Q\u0000"+
		"\u0770\u017b\u0001\u0000\u0000\u0000\u0771\u0772\u0005\u009a\u0000\u0000"+
		"\u0772\u0773\u0003X,\u0000\u0773\u017d\u0001\u0000\u0000\u0000\u0774\u0775"+
		"\u0005H\u0000\u0000\u0775\u0776\u0003X,\u0000\u0776\u017f\u0001\u0000"+
		"\u0000\u0000\u0777\u077d\u0005\u008c\u0000\u0000\u0778\u077e\u0003\u01fa"+
		"\u00fd\u0000\u0779\u077a\u0005\u0019\u0000\u0000\u077a\u077b\u0003R)\u0000"+
		"\u077b\u077c\u0005\u001a\u0000\u0000\u077c\u077e\u0001\u0000\u0000\u0000"+
		"\u077d\u0778\u0001\u0000\u0000\u0000\u077d\u0779\u0001\u0000\u0000\u0000"+
		"\u077e\u0782\u0001\u0000\u0000\u0000\u077f\u0780\u0005\u0019\u0000\u0000"+
		"\u0780\u0783\u0005\u001a\u0000\u0000\u0781\u0783\u0003X,\u0000\u0782\u077f"+
		"\u0001\u0000\u0000\u0000\u0782\u0781\u0001\u0000\u0000\u0000\u0783\u0181"+
		"\u0001\u0000\u0000\u0000\u0784\u0787\u0003\u0184\u00c2\u0000\u0785\u0787"+
		"\u0003\u0186\u00c3\u0000\u0786\u0784\u0001\u0000\u0000\u0000\u0786\u0785"+
		"\u0001\u0000\u0000\u0000\u0787\u0183\u0001\u0000\u0000\u0000\u0788\u0789"+
		"\u0003\u01f6\u00fb\u0000\u0789\u078a\u0005/\u0000\u0000\u078a\u078b\u0005"+
		"\u0005\u0000\u0000\u078b\u0185\u0001\u0000\u0000\u0000\u078c\u078d\u0003"+
		"@ \u0000\u078d\u078e\u0005c\u0000\u0000\u078e\u0790\u0005\u0015\u0000"+
		"\u0000\u078f\u0791\u0003<\u001e\u0000\u0790\u078f\u0001\u0000\u0000\u0000"+
		"\u0790\u0791\u0001\u0000\u0000\u0000\u0791\u0792\u0001\u0000\u0000\u0000"+
		"\u0792\u0795\u0005\u0016\u0000\u0000\u0793\u0794\u0005:\u0000\u0000\u0794"+
		"\u0796\u0003\u01a8\u00d4\u0000\u0795\u0793\u0001\u0000\u0000\u0000\u0795"+
		"\u0796\u0001\u0000\u0000\u0000\u0796\u0797\u0001\u0000\u0000\u0000\u0797"+
		"\u0798\u0003\u0188\u00c4\u0000\u0798\u0187\u0001\u0000\u0000\u0000\u0799"+
		"\u079a\u0005\u0019\u0000\u0000\u079a\u079b\u0003P(\u0000\u079b\u079c\u0005"+
		"\u001a\u0000\u0000\u079c\u0189\u0001\u0000\u0000\u0000\u079d\u079e\u0005"+
		"v\u0000\u0000\u079e\u07a7\u0005\u0019\u0000\u0000\u079f\u07a4\u0003\u018c"+
		"\u00c6\u0000\u07a0\u07a1\u0005\u001e\u0000\u0000\u07a1\u07a3\u0003\u018c"+
		"\u00c6\u0000\u07a2\u07a0\u0001\u0000\u0000\u0000\u07a3\u07a6\u0001\u0000"+
		"\u0000\u0000\u07a4\u07a2\u0001\u0000\u0000\u0000\u07a4\u07a5\u0001\u0000"+
		"\u0000\u0000\u07a5\u07a8\u0001\u0000\u0000\u0000\u07a6\u07a4\u0001\u0000"+
		"\u0000\u0000\u07a7\u079f\u0001\u0000\u0000\u0000\u07a7\u07a8\u0001\u0000"+
		"\u0000\u0000\u07a8\u07a9\u0001\u0000\u0000\u0000\u07a9\u07aa\u0005\u001a"+
		"\u0000\u0000\u07aa\u018b\u0001\u0000\u0000\u0000\u07ab\u07ac\u0003T*\u0000"+
		"\u07ac\u07ad\u0007\u0014\u0000\u0000\u07ad\u07ae\u0003T*\u0000\u07ae\u018d"+
		"\u0001\u0000\u0000\u0000\u07af\u07b2\u0003\u0190\u00c8\u0000\u07b0\u07b2"+
		"\u0003\u0192\u00c9\u0000\u07b1\u07af\u0001\u0000\u0000\u0000\u07b1\u07b0"+
		"\u0001\u0000\u0000\u0000\u07b2\u018f\u0001\u0000\u0000\u0000\u07b3\u07b5"+
		"\u0005\u0017\u0000\u0000\u07b4\u07b6\u0003R)\u0000\u07b5\u07b4\u0001\u0000"+
		"\u0000\u0000\u07b5\u07b6\u0001\u0000\u0000\u0000\u07b6\u07b7\u0001\u0000"+
		"\u0000\u0000\u07b7\u07b8\u0005\u0018\u0000\u0000\u07b8\u0191\u0001\u0000"+
		"\u0000\u0000\u07b9\u07ba\u00059\u0000\u0000\u07ba\u07bb\u0003\u00a2Q\u0000"+
		"\u07bb\u0193\u0001\u0000\u0000\u0000\u07bc\u07bd\u0005\u00c8\u0000\u0000"+
		"\u07bd\u07be\u0003\u0196\u00cb\u0000\u07be\u07bf\u0005\u00cd\u0000\u0000"+
		"\u07bf\u0195\u0001\u0000\u0000\u0000\u07c0\u07c6\u0003\u019e\u00cf\u0000"+
		"\u07c1\u07c2\u0003\u01a0\u00d0\u0000\u07c2\u07c3\u0003\u019e\u00cf\u0000"+
		"\u07c3\u07c5\u0001\u0000\u0000\u0000\u07c4\u07c1\u0001\u0000\u0000\u0000"+
		"\u07c5\u07c8\u0001\u0000\u0000\u0000\u07c6\u07c4\u0001\u0000\u0000\u0000"+
		"\u07c6\u07c7\u0001\u0000\u0000\u0000\u07c7\u0197\u0001\u0000\u0000\u0000"+
		"\u07c8\u07c6\u0001\u0000\u0000\u0000\u07c9\u07ca\u0007\u0015\u0000\u0000"+
		"\u07ca\u0199\u0001\u0000\u0000\u0000\u07cb\u07cc\u0007\u0016\u0000\u0000"+
		"\u07cc\u019b\u0001\u0000\u0000\u0000\u07cd\u07ce\u0007\u0017\u0000\u0000"+
		"\u07ce\u019d\u0001\u0000\u0000\u0000\u07cf\u07da\u0005\u00cb\u0000\u0000"+
		"\u07d0\u07d1\u0003\u0198\u00cc\u0000\u07d1\u07d2\u0003\u019a\u00cd\u0000"+
		"\u07d2\u07da\u0001\u0000\u0000\u0000\u07d3\u07d4\u0003\u019c\u00ce\u0000"+
		"\u07d4\u07d5\u0003\u0198\u00cc\u0000\u07d5\u07d6\u0003\u0198\u00cc\u0000"+
		"\u07d6\u07da\u0001\u0000\u0000\u0000\u07d7\u07da\u0003\u0198\u00cc\u0000"+
		"\u07d8\u07da\u0005\u0019\u0000\u0000\u07d9\u07cf\u0001\u0000\u0000\u0000"+
		"\u07d9\u07d0\u0001\u0000\u0000\u0000\u07d9\u07d3\u0001\u0000\u0000\u0000"+
		"\u07d9\u07d7\u0001\u0000\u0000\u0000\u07d9\u07d8\u0001\u0000\u0000\u0000"+
		"\u07da\u07dd\u0001\u0000\u0000\u0000\u07db\u07d9\u0001\u0000\u0000\u0000"+
		"\u07db\u07dc\u0001\u0000\u0000\u0000\u07dc\u019f\u0001\u0000\u0000\u0000"+
		"\u07dd\u07db\u0001\u0000\u0000\u0000\u07de\u07df\u0005\u00cc\u0000\u0000"+
		"\u07df\u07e0\u0003R)\u0000\u07e0\u07e1\u0005\u00c9\u0000\u0000\u07e1\u01a1"+
		"\u0001\u0000\u0000\u0000\u07e2\u07e3\u0005*\u0000\u0000\u07e3\u07e4\u0003"+
		"\u0128\u0094\u0000\u07e4\u01a3\u0001\u0000\u0000\u0000\u07e5\u07e7\u0003"+
		"\u01d0\u00e8\u0000\u07e6\u07e8\u0005*\u0000\u0000\u07e7\u07e6\u0001\u0000"+
		"\u0000\u0000\u07e7\u07e8\u0001\u0000\u0000\u0000\u07e8\u01a5\u0001\u0000"+
		"\u0000\u0000\u07e9\u07ea\u0005:\u0000\u0000\u07ea\u07eb\u0003\u01a8\u00d4"+
		"\u0000\u07eb\u01a7\u0001\u0000\u0000\u0000\u07ec\u07ed\u0005Y\u0000\u0000"+
		"\u07ed\u07ee\u0005\u0015\u0000\u0000\u07ee\u07f6\u0005\u0016\u0000\u0000"+
		"\u07ef\u07f3\u0003\u01aa\u00d5\u0000\u07f0\u07f4\u0005*\u0000\u0000\u07f1"+
		"\u07f4\u0005\u001b\u0000\u0000\u07f2\u07f4\u0005\u001c\u0000\u0000\u07f3"+
		"\u07f0\u0001\u0000\u0000\u0000\u07f3\u07f1\u0001\u0000\u0000\u0000\u07f3"+
		"\u07f2\u0001\u0000\u0000\u0000\u07f3\u07f4\u0001\u0000\u0000\u0000\u07f4"+
		"\u07f6\u0001\u0000\u0000\u0000\u07f5\u07ec\u0001\u0000\u0000\u0000\u07f5"+
		"\u07ef\u0001\u0000\u0000\u0000\u07f6\u01a9\u0001\u0000\u0000\u0000\u07f7"+
		"\u0801\u0003\u01ae\u00d7\u0000\u07f8\u07f9\u0005p\u0000\u0000\u07f9\u07fa"+
		"\u0005\u0015\u0000\u0000\u07fa\u0801\u0005\u0016\u0000\u0000\u07fb\u0801"+
		"\u0003\u01d4\u00ea\u0000\u07fc\u0801\u0003\u01da\u00ed\u0000\u07fd\u0801"+
		"\u0003\u01e0\u00f0\u0000\u07fe\u0801\u0003\u01ac\u00d6\u0000\u07ff\u0801"+
		"\u0003\u01e6\u00f3\u0000\u0800\u07f7\u0001\u0000\u0000\u0000\u0800\u07f8"+
		"\u0001\u0000\u0000\u0000\u0800\u07fb\u0001\u0000\u0000\u0000\u0800\u07fc"+
		"\u0001\u0000\u0000\u0000\u0800\u07fd\u0001\u0000\u0000\u0000\u0800\u07fe"+
		"\u0001\u0000\u0000\u0000\u0800\u07ff\u0001\u0000\u0000\u0000\u0801\u01ab"+
		"\u0001\u0000\u0000\u0000\u0802\u0803\u0003\u01f6\u00fb\u0000\u0803\u01ad"+
		"\u0001\u0000\u0000\u0000\u0804\u0811\u0003\u01b4\u00da\u0000\u0805\u0811"+
		"\u0003\u01c4\u00e2\u0000\u0806\u0811\u0003\u01be\u00df\u0000\u0807\u0811"+
		"\u0003\u01c8\u00e4\u0000\u0808\u0811\u0003\u01c2\u00e1\u0000\u0809\u0811"+
		"\u0003\u01bc\u00de\u0000\u080a\u0811\u0003\u01b8\u00dc\u0000\u080b\u0811"+
		"\u0003\u01b6\u00db\u0000\u080c\u0811\u0003\u01ba\u00dd\u0000\u080d\u0811"+
		"\u0003\u01ea\u00f5\u0000\u080e\u0811\u0003\u01b2\u00d9\u0000\u080f\u0811"+
		"\u0003\u01b0\u00d8\u0000\u0810\u0804\u0001\u0000\u0000\u0000\u0810\u0805"+
		"\u0001\u0000\u0000\u0000\u0810\u0806\u0001\u0000\u0000\u0000\u0810\u0807"+
		"\u0001\u0000\u0000\u0000\u0810\u0808\u0001\u0000\u0000\u0000\u0810\u0809"+
		"\u0001\u0000\u0000\u0000\u0810\u080a\u0001\u0000\u0000\u0000\u0810\u080b"+
		"\u0001\u0000\u0000\u0000\u0810\u080c\u0001\u0000\u0000\u0000\u0810\u080d"+
		"\u0001\u0000\u0000\u0000\u0810\u080e\u0001\u0000\u0000\u0000\u0810\u080f"+
		"\u0001\u0000\u0000\u0000\u0811\u01af\u0001\u0000\u0000\u0000\u0812\u0813"+
		"\u0005\u007f\u0000\u0000\u0813\u0815\u0005\u0015\u0000\u0000\u0814\u0816"+
		"\u0005\u001b\u0000\u0000\u0815\u0814\u0001\u0000\u0000\u0000\u0815\u0816"+
		"\u0001\u0000\u0000\u0000\u0816\u0817\u0001\u0000\u0000\u0000\u0817\u0818"+
		"\u0005\u0016\u0000\u0000\u0818\u01b1\u0001\u0000\u0000\u0000\u0819\u081a"+
		"\u0005@\u0000\u0000\u081a\u081b\u0005\u0015\u0000\u0000\u081b\u081c\u0005"+
		"\u0016\u0000\u0000\u081c\u01b3\u0001\u0000\u0000\u0000\u081d\u081e\u0005"+
		"U\u0000\u0000\u081e\u0821\u0005\u0015\u0000\u0000\u081f\u0822\u0003\u01c4"+
		"\u00e2\u0000\u0820\u0822\u0003\u01c8\u00e4\u0000\u0821\u081f\u0001\u0000"+
		"\u0000\u0000\u0821\u0820\u0001\u0000\u0000\u0000\u0821\u0822\u0001\u0000"+
		"\u0000\u0000\u0822\u0823\u0001\u0000\u0000\u0000\u0823\u0824\u0005\u0016"+
		"\u0000\u0000\u0824\u01b5\u0001\u0000\u0000\u0000\u0825\u0826\u0005\u009a"+
		"\u0000\u0000\u0826\u0827\u0005\u0015\u0000\u0000\u0827\u0828\u0005\u0016"+
		"\u0000\u0000\u0828\u01b7\u0001\u0000\u0000\u0000\u0829\u082a\u0005H\u0000"+
		"\u0000\u082a\u082b\u0005\u0015\u0000\u0000\u082b\u082c\u0005\u0016\u0000"+
		"\u0000\u082c\u01b9\u0001\u0000\u0000\u0000\u082d\u082e\u0005|\u0000\u0000"+
		"\u082e\u082f\u0005\u0015\u0000\u0000\u082f\u0830\u0005\u0016\u0000\u0000"+
		"\u0830\u01bb\u0001\u0000\u0000\u0000\u0831\u0832\u0005\u008c\u0000\u0000"+
		"\u0832\u0835\u0005\u0015\u0000\u0000\u0833\u0836\u0003\u01fa\u00fd\u0000"+
		"\u0834\u0836\u0003\u020a\u0105\u0000\u0835\u0833\u0001\u0000\u0000\u0000"+
		"\u0835\u0834\u0001\u0000\u0000\u0000\u0835\u0836\u0001\u0000\u0000\u0000"+
		"\u0836\u0837\u0001\u0000\u0000\u0000\u0837\u0838\u0005\u0016\u0000\u0000"+
		"\u0838\u01bd\u0001\u0000\u0000\u0000\u0839\u083a\u0005=\u0000\u0000\u083a"+
		"\u0840\u0005\u0015\u0000\u0000\u083b\u083e\u0003\u01c0\u00e0\u0000\u083c"+
		"\u083d\u0005\u001e\u0000\u0000\u083d\u083f\u0003\u01d2\u00e9\u0000\u083e"+
		"\u083c\u0001\u0000\u0000\u0000\u083e\u083f\u0001\u0000\u0000\u0000\u083f"+
		"\u0841\u0001\u0000\u0000\u0000\u0840\u083b\u0001\u0000\u0000\u0000\u0840"+
		"\u0841\u0001\u0000\u0000\u0000\u0841\u0842\u0001\u0000\u0000\u0000\u0842"+
		"\u0843\u0005\u0016\u0000\u0000\u0843\u01bf\u0001\u0000\u0000\u0000\u0844"+
		"\u0847\u0003\u01cc\u00e6\u0000\u0845\u0847\u0005\u001b\u0000\u0000\u0846"+
		"\u0844\u0001\u0000\u0000\u0000\u0846\u0845\u0001\u0000\u0000\u0000\u0847"+
		"\u01c1\u0001\u0000\u0000\u0000\u0848\u0849\u0005\u0090\u0000\u0000\u0849"+
		"\u084a\u0005\u0015\u0000\u0000\u084a\u084b\u0003\u01e8\u00f4\u0000\u084b"+
		"\u084c\u0005\u0016\u0000\u0000\u084c\u01c3\u0001\u0000\u0000\u0000\u084d"+
		"\u084e\u0005V\u0000\u0000\u084e\u0857\u0005\u0015\u0000\u0000\u084f\u0855"+
		"\u0003\u01c6\u00e3\u0000\u0850\u0851\u0005\u001e\u0000\u0000\u0851\u0853"+
		"\u0003\u01d2\u00e9\u0000\u0852\u0854\u0005*\u0000\u0000\u0853\u0852\u0001"+
		"\u0000\u0000\u0000\u0853\u0854\u0001\u0000\u0000\u0000\u0854\u0856\u0001"+
		"\u0000\u0000\u0000\u0855\u0850\u0001\u0000\u0000\u0000\u0855\u0856\u0001"+
		"\u0000\u0000\u0000\u0856\u0858\u0001\u0000\u0000\u0000\u0857\u084f\u0001"+
		"\u0000\u0000\u0000\u0857\u0858\u0001\u0000\u0000\u0000\u0858\u0859\u0001"+
		"\u0000\u0000\u0000\u0859\u085a\u0005\u0016\u0000\u0000\u085a\u01c5\u0001"+
		"\u0000\u0000\u0000\u085b\u085e\u0003\u01ce\u00e7\u0000\u085c\u085e\u0005"+
		"\u001b\u0000\u0000\u085d\u085b\u0001\u0000\u0000\u0000\u085d\u085c\u0001"+
		"\u0000\u0000\u0000\u085e\u01c7\u0001\u0000\u0000\u0000\u085f\u0860\u0005"+
		"\u0091\u0000\u0000\u0860\u0861\u0005\u0015\u0000\u0000\u0861\u0862\u0003"+
		"\u01ca\u00e5\u0000\u0862\u0863\u0005\u0016\u0000\u0000\u0863\u01c9\u0001"+
		"\u0000\u0000\u0000\u0864\u0865\u0003\u01ce\u00e7\u0000\u0865\u01cb\u0001"+
		"\u0000\u0000\u0000\u0866\u0867\u0003\u01f6\u00fb\u0000\u0867\u01cd\u0001"+
		"\u0000\u0000\u0000\u0868\u0869\u0003\u01f6\u00fb\u0000\u0869\u01cf\u0001"+
		"\u0000\u0000\u0000\u086a\u086b\u0003\u01d2\u00e9\u0000\u086b\u01d1\u0001"+
		"\u0000\u0000\u0000\u086c\u086d\u0003\u01f6\u00fb\u0000\u086d\u01d3\u0001"+
		"\u0000\u0000\u0000\u086e\u0870\u0003B!\u0000\u086f\u086e\u0001\u0000\u0000"+
		"\u0000\u0870\u0873\u0001\u0000\u0000\u0000\u0871\u086f\u0001\u0000\u0000"+
		"\u0000\u0871\u0872\u0001\u0000\u0000\u0000\u0872\u0876\u0001\u0000\u0000"+
		"\u0000\u0873\u0871\u0001\u0000\u0000\u0000\u0874\u0877\u0003\u01d6\u00eb"+
		"\u0000\u0875\u0877\u0003\u01d8\u00ec\u0000\u0876\u0874\u0001\u0000\u0000"+
		"\u0000\u0876\u0875\u0001\u0000\u0000\u0000\u0877\u01d5\u0001\u0000\u0000"+
		"\u0000\u0878\u0879\u0005c\u0000\u0000\u0879\u087a\u0005\u0015\u0000\u0000"+
		"\u087a\u087b\u0005\u001b\u0000\u0000\u087b\u087c\u0005\u0016\u0000\u0000"+
		"\u087c\u01d7\u0001\u0000\u0000\u0000\u087d\u087e\u0005c\u0000\u0000\u087e"+
		"\u0887\u0005\u0015\u0000\u0000\u087f\u0884\u0003\u01a8\u00d4\u0000\u0880"+
		"\u0881\u0005\u001e\u0000\u0000\u0881\u0883\u0003\u01a8\u00d4\u0000\u0882"+
		"\u0880\u0001\u0000\u0000\u0000\u0883\u0886\u0001\u0000\u0000\u0000\u0884"+
		"\u0882\u0001\u0000\u0000\u0000\u0884\u0885\u0001\u0000\u0000\u0000\u0885"+
		"\u0888\u0001\u0000\u0000\u0000\u0886\u0884\u0001\u0000\u0000\u0000\u0887"+
		"\u087f\u0001\u0000\u0000\u0000\u0887\u0888\u0001\u0000\u0000\u0000\u0888"+
		"\u0889\u0001\u0000\u0000\u0000\u0889\u088a\u0005\u0016\u0000\u0000\u088a"+
		"\u088b\u0005:\u0000\u0000\u088b\u088c\u0003\u01a8\u00d4\u0000\u088c\u01d9"+
		"\u0001\u0000\u0000\u0000\u088d\u0890\u0003\u01dc\u00ee\u0000\u088e\u0890"+
		"\u0003\u01de\u00ef\u0000\u088f\u088d\u0001\u0000\u0000\u0000\u088f\u088e"+
		"\u0001\u0000\u0000\u0000\u0890\u01db\u0001\u0000\u0000\u0000\u0891\u0892"+
		"\u0005v\u0000\u0000\u0892\u0893\u0005\u0015\u0000\u0000\u0893\u0894\u0005"+
		"\u001b\u0000\u0000\u0894\u0895\u0005\u0016\u0000\u0000\u0895\u01dd\u0001"+
		"\u0000\u0000\u0000\u0896\u0897\u0005v\u0000\u0000\u0897\u0898\u0005\u0015"+
		"\u0000\u0000\u0898\u0899\u0003\u01f6\u00fb\u0000\u0899\u089a\u0005\u001e"+
		"\u0000\u0000\u089a\u089b\u0003\u01a8\u00d4\u0000\u089b\u089c\u0005\u0016"+
		"\u0000\u0000\u089c\u01df\u0001\u0000\u0000\u0000\u089d\u08a0\u0003\u01e2"+
		"\u00f1\u0000\u089e\u08a0\u0003\u01e4\u00f2\u0000\u089f\u089d\u0001\u0000"+
		"\u0000\u0000\u089f\u089e\u0001\u0000\u0000\u0000\u08a0\u01e1\u0001\u0000"+
		"\u0000\u0000\u08a1\u08a2\u00059\u0000\u0000\u08a2\u08a3\u0005\u0015\u0000"+
		"\u0000\u08a3\u08a4\u0005\u001b\u0000\u0000\u08a4\u08a5\u0005\u0016\u0000"+
		"\u0000\u08a5\u01e3\u0001\u0000\u0000\u0000\u08a6\u08a7\u00059\u0000\u0000"+
		"\u08a7\u08a8\u0005\u0015\u0000\u0000\u08a8\u08a9\u0003\u01a8\u00d4\u0000"+
		"\u08a9\u08aa\u0005\u0016\u0000\u0000\u08aa\u01e5\u0001\u0000\u0000\u0000"+
		"\u08ab\u08ac\u0005\u0015\u0000\u0000\u08ac\u08ad\u0003\u01aa\u00d5\u0000"+
		"\u08ad\u08ae\u0005\u0016\u0000\u0000\u08ae\u01e7\u0001\u0000\u0000\u0000"+
		"\u08af\u08b0\u0003\u01cc\u00e6\u0000\u08b0\u01e9\u0001\u0000\u0000\u0000"+
		"\u08b1\u08b7\u0003\u01ec\u00f6\u0000\u08b2\u08b7\u0003\u01ee\u00f7\u0000"+
		"\u08b3\u08b7\u0003\u01f0\u00f8\u0000\u08b4\u08b7\u0003\u01f2\u00f9\u0000"+
		"\u08b5\u08b7\u0003\u01f4\u00fa\u0000\u08b6\u08b1\u0001\u0000\u0000\u0000"+
		"\u08b6\u08b2\u0001\u0000\u0000\u0000\u08b6\u08b3\u0001\u0000\u0000\u0000"+
		"\u08b6\u08b4\u0001\u0000\u0000\u0000\u08b6\u08b5\u0001\u0000\u0000\u0000"+
		"\u08b7\u01eb\u0001\u0000\u0000\u0000\u08b8\u08b9\u0005\u00b2\u0000\u0000"+
		"\u08b9\u08bb\u0005\u0015\u0000\u0000\u08ba\u08bc\u0003\u020a\u0105\u0000"+
		"\u08bb\u08ba\u0001\u0000\u0000\u0000\u08bb\u08bc\u0001\u0000\u0000\u0000"+
		"\u08bc\u08bd\u0001\u0000\u0000\u0000\u08bd\u08be\u0005\u0016\u0000\u0000"+
		"\u08be\u01ed\u0001\u0000\u0000\u0000\u08bf\u08c0\u0005\u00b6\u0000\u0000"+
		"\u08c0\u08c2\u0005\u0015\u0000\u0000\u08c1\u08c3\u0003\u020a\u0105\u0000"+
		"\u08c2\u08c1\u0001\u0000\u0000\u0000\u08c2\u08c3\u0001\u0000\u0000\u0000"+
		"\u08c3\u08c4\u0001\u0000\u0000\u0000\u08c4\u08c5\u0005\u0016\u0000\u0000"+
		"\u08c5\u01ef\u0001\u0000\u0000\u0000\u08c6\u08c7\u0005\u00b5\u0000\u0000"+
		"\u08c7\u08c9\u0005\u0015\u0000\u0000\u08c8\u08ca\u0003\u020a\u0105\u0000"+
		"\u08c9\u08c8\u0001\u0000\u0000\u0000\u08c9\u08ca\u0001\u0000\u0000\u0000"+
		"\u08ca\u08cb\u0001\u0000\u0000\u0000\u08cb\u08cc\u0005\u0016\u0000\u0000"+
		"\u08cc\u01f1\u0001\u0000\u0000\u0000\u08cd\u08ce\u0005\u00b3\u0000\u0000"+
		"\u08ce\u08d0\u0005\u0015\u0000\u0000\u08cf\u08d1\u0003\u020a\u0105\u0000"+
		"\u08d0\u08cf\u0001\u0000\u0000\u0000\u08d0\u08d1\u0001\u0000\u0000\u0000"+
		"\u08d1\u08d2\u0001\u0000\u0000\u0000\u08d2\u08d3\u0005\u0016\u0000\u0000"+
		"\u08d3\u01f3\u0001\u0000\u0000\u0000\u08d4\u08d5\u0005\u00b4\u0000\u0000"+
		"\u08d5\u08d7\u0005\u0015\u0000\u0000\u08d6\u08d8\u0003\u020a\u0105\u0000"+
		"\u08d7\u08d6\u0001\u0000\u0000\u0000\u08d7\u08d8\u0001\u0000\u0000\u0000"+
		"\u08d8\u08d9\u0001\u0000\u0000\u0000\u08d9\u08da\u0005\u0016\u0000\u0000"+
		"\u08da\u01f5\u0001\u0000\u0000\u0000\u08db\u08de\u0003\u01f8\u00fc\u0000"+
		"\u08dc\u08de\u0005\u00be\u0000\u0000\u08dd\u08db\u0001\u0000\u0000\u0000"+
		"\u08dd\u08dc\u0001\u0000\u0000\u0000\u08de\u01f7\u0001\u0000\u0000\u0000"+
		"\u08df\u08e2\u0005\u00bf\u0000\u0000\u08e0\u08e2\u0003\u01fa\u00fd\u0000"+
		"\u08e1\u08df\u0001\u0000\u0000\u0000\u08e1\u08e0\u0001\u0000\u0000\u0000"+
		"\u08e2\u01f9\u0001\u0000\u0000\u0000\u08e3\u08e6\u0005\u00c2\u0000\u0000"+
		"\u08e4\u08e6\u0003\u01fe\u00ff\u0000\u08e5\u08e3\u0001\u0000\u0000\u0000"+
		"\u08e5\u08e4\u0001\u0000\u0000\u0000\u08e6\u01fb\u0001\u0000\u0000\u0000"+
		"\u08e7\u08ec\u0005\u00bf\u0000\u0000\u08e8\u08ec\u0005\u00c2\u0000\u0000"+
		"\u08e9\u08ec\u0005\u00be\u0000\u0000\u08ea\u08ec\u0003\u0202\u0101\u0000"+
		"\u08eb\u08e7\u0001\u0000\u0000\u0000\u08eb\u08e8\u0001\u0000\u0000\u0000"+
		"\u08eb\u08e9\u0001\u0000\u0000\u0000\u08eb\u08ea\u0001\u0000\u0000\u0000"+
		"\u08ec\u01fd\u0001\u0000\u0000\u0000\u08ed\u08f0\u0003\u0202\u0101\u0000"+
		"\u08ee\u08f0\u0003\u0200\u0100\u0000\u08ef\u08ed\u0001\u0000\u0000\u0000"+
		"\u08ef\u08ee\u0001\u0000\u0000\u0000\u08f0\u01ff\u0001\u0000\u0000\u0000"+
		"\u08f1\u08f2\u0007\u0018\u0000\u0000\u08f2\u0201\u0001\u0000\u0000\u0000"+
		"\u08f3\u08f4\u0007\u0019\u0000\u0000\u08f4\u0203\u0001\u0000\u0000\u0000"+
		"\u08f5\u08f6\u0003\u020a\u0105\u0000\u08f6\u0205\u0001\u0000\u0000\u0000"+
		"\u08f7\u08fe\u0005\u000b\u0000\u0000\u08f8\u08fd\u0005\t\u0000\u0000\u08f9"+
		"\u08fd\u0005\n\u0000\u0000\u08fa\u08fd\u0005\u0001\u0000\u0000\u08fb\u08fd"+
		"\u0003\u020c\u0106\u0000\u08fc\u08f8\u0001\u0000\u0000\u0000\u08fc\u08f9"+
		"\u0001\u0000\u0000\u0000\u08fc\u08fa\u0001\u0000\u0000\u0000\u08fc\u08fb"+
		"\u0001\u0000\u0000\u0000\u08fd\u0900\u0001\u0000\u0000\u0000\u08fe\u08fc"+
		"\u0001\u0000\u0000\u0000\u08fe\u08ff\u0001\u0000\u0000\u0000\u08ff\u0901"+
		"\u0001\u0000\u0000\u0000\u0900\u08fe\u0001\u0000\u0000\u0000\u0901\u0902"+
		"\u0005\u000b\u0000\u0000\u0902\u0207\u0001\u0000\u0000\u0000\u0903\u090a"+
		"\u0005\f\u0000\u0000\u0904\u0909\u0005\t\u0000\u0000\u0905\u0909\u0005"+
		"\n\u0000\u0000\u0906\u0909\u0005\u0002\u0000\u0000\u0907\u0909\u0003\u020e"+
		"\u0107\u0000\u0908\u0904\u0001\u0000\u0000\u0000\u0908\u0905\u0001\u0000"+
		"\u0000\u0000\u0908\u0906\u0001\u0000\u0000\u0000\u0908\u0907\u0001\u0000"+
		"\u0000\u0000\u0909\u090c\u0001\u0000\u0000\u0000\u090a\u0908\u0001\u0000"+
		"\u0000\u0000\u090a\u090b\u0001\u0000\u0000\u0000\u090b\u090d\u0001\u0000"+
		"\u0000\u0000\u090c\u090a\u0001\u0000\u0000\u0000\u090d\u090e\u0005\f\u0000"+
		"\u0000\u090e\u0209\u0001\u0000\u0000\u0000\u090f\u0912\u0003\u0206\u0103"+
		"\u0000\u0910\u0912\u0003\u0208\u0104\u0000\u0911\u090f\u0001\u0000\u0000"+
		"\u0000\u0911\u0910\u0001\u0000\u0000\u0000\u0912\u020b\u0001\u0000\u0000"+
		"\u0000\u0913\u0915\u0005\u00ca\u0000\u0000\u0914\u0913\u0001\u0000\u0000"+
		"\u0000\u0915\u0916\u0001\u0000\u0000\u0000\u0916\u0914\u0001\u0000\u0000"+
		"\u0000\u0916\u0917\u0001\u0000\u0000\u0000\u0917\u0925\u0001\u0000\u0000"+
		"\u0000\u0918\u091a\u0005\u0019\u0000\u0000\u0919\u091b\u0003R)\u0000\u091a"+
		"\u0919\u0001\u0000\u0000\u0000\u091a\u091b\u0001\u0000\u0000\u0000\u091b"+
		"\u091d\u0001\u0000\u0000\u0000\u091c\u091e\u0005\u001a\u0000\u0000\u091d"+
		"\u091c\u0001\u0000\u0000\u0000\u091d\u091e\u0001\u0000\u0000\u0000\u091e"+
		"\u0925\u0001\u0000\u0000\u0000\u091f\u0925\u0005\u001a\u0000\u0000\u0920"+
		"\u0925\u0005\u0003\u0000\u0000\u0921\u0925\u0005\u0004\u0000\u0000\u0922"+
		"\u0925\u0003\u0210\u0108\u0000\u0923\u0925\u0003\u0208\u0104\u0000\u0924"+
		"\u0914\u0001\u0000\u0000\u0000\u0924\u0918\u0001\u0000\u0000\u0000\u0924"+
		"\u091f\u0001\u0000\u0000\u0000\u0924\u0920\u0001\u0000\u0000\u0000\u0924"+
		"\u0921\u0001\u0000\u0000\u0000\u0924\u0922\u0001\u0000\u0000\u0000\u0924"+
		"\u0923\u0001\u0000\u0000\u0000\u0925\u020d\u0001\u0000\u0000\u0000\u0926"+
		"\u0928\u0005\u00ca\u0000\u0000\u0927\u0926\u0001\u0000\u0000\u0000\u0928"+
		"\u0929\u0001\u0000\u0000\u0000\u0929\u0927\u0001\u0000\u0000\u0000\u0929"+
		"\u092a\u0001\u0000\u0000\u0000\u092a\u0938\u0001\u0000\u0000\u0000\u092b"+
		"\u092d\u0005\u0019\u0000\u0000\u092c\u092e\u0003R)\u0000\u092d\u092c\u0001"+
		"\u0000\u0000\u0000\u092d\u092e\u0001\u0000\u0000\u0000\u092e\u0930\u0001"+
		"\u0000\u0000\u0000\u092f\u0931\u0005\u001a\u0000\u0000\u0930\u092f\u0001"+
		"\u0000\u0000\u0000\u0930\u0931\u0001\u0000\u0000\u0000\u0931\u0938\u0001"+
		"\u0000\u0000\u0000\u0932\u0938\u0005\u001a\u0000\u0000\u0933\u0938\u0005"+
		"\u0003\u0000\u0000\u0934\u0938\u0005\u0004\u0000\u0000\u0935\u0938\u0003"+
		"\u0210\u0108\u0000\u0936\u0938\u0003\u0206\u0103\u0000\u0937\u0927\u0001"+
		"\u0000\u0000\u0000\u0937\u092b\u0001\u0000\u0000\u0000\u0937\u0932\u0001"+
		"\u0000\u0000\u0000\u0937\u0933\u0001\u0000\u0000\u0000\u0937\u0934\u0001"+
		"\u0000\u0000\u0000\u0937\u0935\u0001\u0000\u0000\u0000\u0937\u0936\u0001"+
		"\u0000\u0000\u0000\u0938\u020f\u0001\u0000\u0000\u0000\u0939\u093c\u0003"+
		"\u01fe\u00ff\u0000\u093a\u093c\u0007\u001a\u0000\u0000\u093b\u0939\u0001"+
		"\u0000\u0000\u0000\u093b\u093a\u0001\u0000\u0000\u0000\u093c\u093d\u0001"+
		"\u0000\u0000\u0000\u093d\u093b\u0001\u0000\u0000\u0000\u093d\u093e\u0001"+
		"\u0000\u0000\u0000\u093e\u0211\u0001\u0000\u0000\u0000\u00df\u0213\u0216"+
		"\u0219\u021d\u0226\u023e\u0244\u0248\u024f\u0256\u0266\u0292\u0299\u029f"+
		"\u02a8\u02ab\u02b4\u02bc\u02c5\u02c8\u02d6\u02dd\u02df\u02ea\u02f1\u02f3"+
		"\u02fb\u02ff\u0303\u030a\u0310\u0315\u031e\u0325\u0335\u033d\u0344\u034d"+
		"\u0352\u035c\u0364\u036b\u0373\u0379\u037c\u037f\u0391\u0397\u039f\u03a6"+
		"\u03ac\u03b3\u03c0\u03c9\u03cc\u03d1\u03d6\u03e8\u03ee\u03f2\u03f6\u03f9"+
		"\u0402\u0408\u040d\u040f\u0413\u041a\u0421\u042a\u0436\u0440\u044e\u0453"+
		"\u045d\u0468\u0478\u0486\u048c\u0495\u049e\u04bc\u04c4\u04cb\u04cf\u04d6"+
		"\u04dc\u04e3\u04eb\u04f3\u04fb\u0502\u0508\u050e\u0514\u051b\u0524\u052c"+
		"\u0536\u053f\u0545\u054e\u0559\u0569\u0589\u05a1\u05ab\u05b8\u05c1\u05c6"+
		"\u05d0\u05d9\u05e0\u05e4\u05ea\u05ee\u05f2\u05ff\u0604\u060b\u0610\u0614"+
		"\u061c\u0623\u062b\u0635\u0639\u063e\u0644\u0646\u064f\u0652\u0659\u0667"+
		"\u066c\u067c\u0680\u068b\u069c\u06a0\u06a5\u06ae\u06c2\u06ca\u06cc\u06d6"+
		"\u06d8\u06df\u06e4\u06eb\u06ee\u06f3\u06fa\u06fd\u0705\u070d\u0719\u0721"+
		"\u0732\u0735\u0751\u075d\u0762\u0767\u077d\u0782\u0786\u0790\u0795\u07a4"+
		"\u07a7\u07b1\u07b5\u07c6\u07d9\u07db\u07e7\u07f3\u07f5\u0800\u0810\u0815"+
		"\u0821\u0835\u083e\u0840\u0846\u0853\u0855\u0857\u085d\u0871\u0876\u0884"+
		"\u0887\u088f\u089f\u08b6\u08bb\u08c2\u08c9\u08d0\u08d7\u08dd\u08e1\u08e5"+
		"\u08eb\u08ef\u08fc\u08fe\u0908\u090a\u0911\u0916\u091a\u091d\u0924\u0929"+
		"\u092d\u0930\u0937\u093b\u093d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}