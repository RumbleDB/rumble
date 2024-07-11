// Generated from ./src/main/java/org/rumbledb/parser/XQueryParser.g4 by ANTLR 4.13.1

// Java header
package org.rumbledb.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class XQueryParser extends Parser {
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
		KW_WINDOW=170, KW_XQUERY=171, KW_ARRAY_NODE=172, KW_BOOLEAN_NODE=173, 
		KW_NULL_NODE=174, KW_NUMBER_NODE=175, KW_OBJECT_NODE=176, KW_REPLACE=177, 
		KW_WITH=178, KW_VALUE=179, KW_INSERT=180, KW_INTO=181, KW_DELETE=182, 
		KW_RENAME=183, URIQualifiedName=184, FullQName=185, NCNameWithLocalWildcard=186, 
		NCNameWithPrefixWildcard=187, NCName=188, XQDOC_COMMENT_START=189, XQDOC_COMMENT_END=190, 
		XQDocComment=191, XQComment=192, CHAR=193, ENTER_STRING=194, EXIT_INTERPOLATION=195, 
		ContentChar=196, BASIC_CHAR=197, ENTER_INTERPOLATION=198, EXIT_STRING=199, 
		EscapeQuot_QuotString=200, DOUBLE_LBRACE_QuotString=201, DOUBLE_RBRACE_QuotString=202, 
		EscapeApos_AposString=203;
	public static final int
		RULE_module = 0, RULE_xqDocComment = 1, RULE_versionDecl = 2, RULE_mainModule = 3, 
		RULE_queryBody = 4, RULE_libraryModule = 5, RULE_moduleDecl = 6, RULE_prolog = 7, 
		RULE_annotatedDecl = 8, RULE_defaultNamespaceDecl = 9, RULE_setter = 10, 
		RULE_boundarySpaceDecl = 11, RULE_defaultCollationDecl = 12, RULE_baseURIDecl = 13, 
		RULE_constructionDecl = 14, RULE_orderingModeDecl = 15, RULE_emptyOrderDecl = 16, 
		RULE_copyNamespacesDecl = 17, RULE_preserveMode = 18, RULE_inheritMode = 19, 
		RULE_decimalFormatDecl = 20, RULE_schemaImport = 21, RULE_schemaPrefix = 22, 
		RULE_moduleImport = 23, RULE_namespaceDecl = 24, RULE_varDecl = 25, RULE_varValue = 26, 
		RULE_varDefaultValue = 27, RULE_contextItemDecl = 28, RULE_functionDecl = 29, 
		RULE_functionParams = 30, RULE_functionParam = 31, RULE_annotations = 32, 
		RULE_annotation = 33, RULE_annotList = 34, RULE_annotationParam = 35, 
		RULE_functionReturn = 36, RULE_optionDecl = 37, RULE_expr = 38, RULE_exprSingle = 39, 
		RULE_flworExpr = 40, RULE_initialClause = 41, RULE_intermediateClause = 42, 
		RULE_forClause = 43, RULE_forBinding = 44, RULE_allowingEmpty = 45, RULE_positionalVar = 46, 
		RULE_letClause = 47, RULE_letBinding = 48, RULE_windowClause = 49, RULE_tumblingWindowClause = 50, 
		RULE_slidingWindowClause = 51, RULE_windowStartCondition = 52, RULE_windowEndCondition = 53, 
		RULE_windowVars = 54, RULE_countClause = 55, RULE_whereClause = 56, RULE_groupByClause = 57, 
		RULE_groupingSpecList = 58, RULE_groupingSpec = 59, RULE_orderByClause = 60, 
		RULE_orderSpec = 61, RULE_returnClause = 62, RULE_quantifiedExpr = 63, 
		RULE_quantifiedVar = 64, RULE_switchExpr = 65, RULE_switchCaseClause = 66, 
		RULE_switchCaseOperand = 67, RULE_typeswitchExpr = 68, RULE_caseClause = 69, 
		RULE_sequenceUnionType = 70, RULE_ifExpr = 71, RULE_tryCatchExpr = 72, 
		RULE_tryClause = 73, RULE_enclosedTryTargetExpression = 74, RULE_catchClause = 75, 
		RULE_enclosedExpression = 76, RULE_catchErrorList = 77, RULE_existUpdateExpr = 78, 
		RULE_existReplaceExpr = 79, RULE_existValueExpr = 80, RULE_existInsertExpr = 81, 
		RULE_existDeleteExpr = 82, RULE_existRenameExpr = 83, RULE_orExpr = 84, 
		RULE_andExpr = 85, RULE_comparisonExpr = 86, RULE_stringConcatExpr = 87, 
		RULE_rangeExpr = 88, RULE_additiveExpr = 89, RULE_multiplicativeExpr = 90, 
		RULE_unionExpr = 91, RULE_intersectExceptExpr = 92, RULE_instanceOfExpr = 93, 
		RULE_treatExpr = 94, RULE_castableExpr = 95, RULE_castExpr = 96, RULE_arrowExpr = 97, 
		RULE_complexArrow = 98, RULE_unaryExpr = 99, RULE_valueExpr = 100, RULE_generalComp = 101, 
		RULE_valueComp = 102, RULE_nodeComp = 103, RULE_validateExpr = 104, RULE_validationMode = 105, 
		RULE_extensionExpr = 106, RULE_simpleMapExpr = 107, RULE_pathExpr = 108, 
		RULE_relativePathExpr = 109, RULE_stepExpr = 110, RULE_axisStep = 111, 
		RULE_forwardStep = 112, RULE_forwardAxis = 113, RULE_abbrevForwardStep = 114, 
		RULE_reverseStep = 115, RULE_reverseAxis = 116, RULE_abbrevReverseStep = 117, 
		RULE_nodeTest = 118, RULE_nameTest = 119, RULE_wildcard = 120, RULE_postfixExpr = 121, 
		RULE_argumentList = 122, RULE_predicateList = 123, RULE_predicate = 124, 
		RULE_lookup = 125, RULE_keySpecifier = 126, RULE_arrowFunctionSpecifier = 127, 
		RULE_primaryExpr = 128, RULE_literal = 129, RULE_numericLiteral = 130, 
		RULE_varRef = 131, RULE_varName = 132, RULE_parenthesizedExpr = 133, RULE_contextItemExpr = 134, 
		RULE_orderedExpr = 135, RULE_unorderedExpr = 136, RULE_functionCall = 137, 
		RULE_argument = 138, RULE_nodeConstructor = 139, RULE_directConstructor = 140, 
		RULE_dirElemConstructorOpenClose = 141, RULE_dirElemConstructorSingleTag = 142, 
		RULE_dirAttributeList = 143, RULE_dirAttributeValueApos = 144, RULE_dirAttributeValueQuot = 145, 
		RULE_dirAttributeValue = 146, RULE_dirAttributeContentQuot = 147, RULE_dirAttributeContentApos = 148, 
		RULE_dirElemContent = 149, RULE_commonContent = 150, RULE_computedConstructor = 151, 
		RULE_compMLJSONConstructor = 152, RULE_compMLJSONArrayConstructor = 153, 
		RULE_compMLJSONObjectConstructor = 154, RULE_compMLJSONNumberConstructor = 155, 
		RULE_compMLJSONBooleanConstructor = 156, RULE_compMLJSONNullConstructor = 157, 
		RULE_compBinaryConstructor = 158, RULE_compDocConstructor = 159, RULE_compElemConstructor = 160, 
		RULE_enclosedContentExpr = 161, RULE_compAttrConstructor = 162, RULE_compNamespaceConstructor = 163, 
		RULE_prefix = 164, RULE_enclosedPrefixExpr = 165, RULE_enclosedURIExpr = 166, 
		RULE_compTextConstructor = 167, RULE_compCommentConstructor = 168, RULE_compPIConstructor = 169, 
		RULE_functionItemExpr = 170, RULE_namedFunctionRef = 171, RULE_inlineFunctionRef = 172, 
		RULE_functionBody = 173, RULE_mapConstructor = 174, RULE_mapConstructorEntry = 175, 
		RULE_arrayConstructor = 176, RULE_squareArrayConstructor = 177, RULE_curlyArrayConstructor = 178, 
		RULE_stringConstructor = 179, RULE_stringConstructorContent = 180, RULE_charNoGrave = 181, 
		RULE_charNoLBrace = 182, RULE_charNoRBrack = 183, RULE_stringConstructorChars = 184, 
		RULE_stringConstructorInterpolation = 185, RULE_unaryLookup = 186, RULE_singleType = 187, 
		RULE_typeDeclaration = 188, RULE_sequenceType = 189, RULE_itemType = 190, 
		RULE_atomicOrUnionType = 191, RULE_kindTest = 192, RULE_anyKindTest = 193, 
		RULE_binaryNodeTest = 194, RULE_documentTest = 195, RULE_textTest = 196, 
		RULE_commentTest = 197, RULE_namespaceNodeTest = 198, RULE_piTest = 199, 
		RULE_attributeTest = 200, RULE_attributeNameOrWildcard = 201, RULE_schemaAttributeTest = 202, 
		RULE_elementTest = 203, RULE_elementNameOrWildcard = 204, RULE_schemaElementTest = 205, 
		RULE_elementDeclaration = 206, RULE_attributeName = 207, RULE_elementName = 208, 
		RULE_simpleTypeName = 209, RULE_typeName = 210, RULE_functionTest = 211, 
		RULE_anyFunctionTest = 212, RULE_typedFunctionTest = 213, RULE_mapTest = 214, 
		RULE_anyMapTest = 215, RULE_typedMapTest = 216, RULE_arrayTest = 217, 
		RULE_anyArrayTest = 218, RULE_typedArrayTest = 219, RULE_parenthesizedItemTest = 220, 
		RULE_attributeDeclaration = 221, RULE_mlNodeTest = 222, RULE_mlArrayNodeTest = 223, 
		RULE_mlObjectNodeTest = 224, RULE_mlNumberNodeTest = 225, RULE_mlBooleanNodeTest = 226, 
		RULE_mlNullNodeTest = 227, RULE_eqName = 228, RULE_qName = 229, RULE_ncName = 230, 
		RULE_functionName = 231, RULE_keyword = 232, RULE_keywordNotOKForFunction = 233, 
		RULE_keywordOKForFunction = 234, RULE_uriLiteral = 235, RULE_stringLiteralQuot = 236, 
		RULE_stringLiteralApos = 237, RULE_stringLiteral = 238, RULE_stringContentQuot = 239, 
		RULE_stringContentApos = 240, RULE_noQuotesNoBracesNoAmpNoLAng = 241;
	private static String[] makeRuleNames() {
		return new String[] {
			"module", "xqDocComment", "versionDecl", "mainModule", "queryBody", "libraryModule", 
			"moduleDecl", "prolog", "annotatedDecl", "defaultNamespaceDecl", "setter", 
			"boundarySpaceDecl", "defaultCollationDecl", "baseURIDecl", "constructionDecl", 
			"orderingModeDecl", "emptyOrderDecl", "copyNamespacesDecl", "preserveMode", 
			"inheritMode", "decimalFormatDecl", "schemaImport", "schemaPrefix", "moduleImport", 
			"namespaceDecl", "varDecl", "varValue", "varDefaultValue", "contextItemDecl", 
			"functionDecl", "functionParams", "functionParam", "annotations", "annotation", 
			"annotList", "annotationParam", "functionReturn", "optionDecl", "expr", 
			"exprSingle", "flworExpr", "initialClause", "intermediateClause", "forClause", 
			"forBinding", "allowingEmpty", "positionalVar", "letClause", "letBinding", 
			"windowClause", "tumblingWindowClause", "slidingWindowClause", "windowStartCondition", 
			"windowEndCondition", "windowVars", "countClause", "whereClause", "groupByClause", 
			"groupingSpecList", "groupingSpec", "orderByClause", "orderSpec", "returnClause", 
			"quantifiedExpr", "quantifiedVar", "switchExpr", "switchCaseClause", 
			"switchCaseOperand", "typeswitchExpr", "caseClause", "sequenceUnionType", 
			"ifExpr", "tryCatchExpr", "tryClause", "enclosedTryTargetExpression", 
			"catchClause", "enclosedExpression", "catchErrorList", "existUpdateExpr", 
			"existReplaceExpr", "existValueExpr", "existInsertExpr", "existDeleteExpr", 
			"existRenameExpr", "orExpr", "andExpr", "comparisonExpr", "stringConcatExpr", 
			"rangeExpr", "additiveExpr", "multiplicativeExpr", "unionExpr", "intersectExceptExpr", 
			"instanceOfExpr", "treatExpr", "castableExpr", "castExpr", "arrowExpr", 
			"complexArrow", "unaryExpr", "valueExpr", "generalComp", "valueComp", 
			"nodeComp", "validateExpr", "validationMode", "extensionExpr", "simpleMapExpr", 
			"pathExpr", "relativePathExpr", "stepExpr", "axisStep", "forwardStep", 
			"forwardAxis", "abbrevForwardStep", "reverseStep", "reverseAxis", "abbrevReverseStep", 
			"nodeTest", "nameTest", "wildcard", "postfixExpr", "argumentList", "predicateList", 
			"predicate", "lookup", "keySpecifier", "arrowFunctionSpecifier", "primaryExpr", 
			"literal", "numericLiteral", "varRef", "varName", "parenthesizedExpr", 
			"contextItemExpr", "orderedExpr", "unorderedExpr", "functionCall", "argument", 
			"nodeConstructor", "directConstructor", "dirElemConstructorOpenClose", 
			"dirElemConstructorSingleTag", "dirAttributeList", "dirAttributeValueApos", 
			"dirAttributeValueQuot", "dirAttributeValue", "dirAttributeContentQuot", 
			"dirAttributeContentApos", "dirElemContent", "commonContent", "computedConstructor", 
			"compMLJSONConstructor", "compMLJSONArrayConstructor", "compMLJSONObjectConstructor", 
			"compMLJSONNumberConstructor", "compMLJSONBooleanConstructor", "compMLJSONNullConstructor", 
			"compBinaryConstructor", "compDocConstructor", "compElemConstructor", 
			"enclosedContentExpr", "compAttrConstructor", "compNamespaceConstructor", 
			"prefix", "enclosedPrefixExpr", "enclosedURIExpr", "compTextConstructor", 
			"compCommentConstructor", "compPIConstructor", "functionItemExpr", "namedFunctionRef", 
			"inlineFunctionRef", "functionBody", "mapConstructor", "mapConstructorEntry", 
			"arrayConstructor", "squareArrayConstructor", "curlyArrayConstructor", 
			"stringConstructor", "stringConstructorContent", "charNoGrave", "charNoLBrace", 
			"charNoRBrack", "stringConstructorChars", "stringConstructorInterpolation", 
			"unaryLookup", "singleType", "typeDeclaration", "sequenceType", "itemType", 
			"atomicOrUnionType", "kindTest", "anyKindTest", "binaryNodeTest", "documentTest", 
			"textTest", "commentTest", "namespaceNodeTest", "piTest", "attributeTest", 
			"attributeNameOrWildcard", "schemaAttributeTest", "elementTest", "elementNameOrWildcard", 
			"schemaElementTest", "elementDeclaration", "attributeName", "elementName", 
			"simpleTypeName", "typeName", "functionTest", "anyFunctionTest", "typedFunctionTest", 
			"mapTest", "anyMapTest", "typedMapTest", "arrayTest", "anyArrayTest", 
			"typedArrayTest", "parenthesizedItemTest", "attributeDeclaration", "mlNodeTest", 
			"mlArrayNodeTest", "mlObjectNodeTest", "mlNumberNodeTest", "mlBooleanNodeTest", 
			"mlNullNodeTest", "eqName", "qName", "ncName", "functionName", "keyword", 
			"keywordNotOKForFunction", "keywordOKForFunction", "uriLiteral", "stringLiteralQuot", 
			"stringLiteralApos", "stringLiteral", "stringContentQuot", "stringContentApos", 
			"noQuotesNoBracesNoAmpNoLAng"
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
			"KW_WHEN", "KW_WHERE", "KW_WINDOW", "KW_XQUERY", "KW_ARRAY_NODE", "KW_BOOLEAN_NODE", 
			"KW_NULL_NODE", "KW_NUMBER_NODE", "KW_OBJECT_NODE", "KW_REPLACE", "KW_WITH", 
			"KW_VALUE", "KW_INSERT", "KW_INTO", "KW_DELETE", "KW_RENAME", "URIQualifiedName", 
			"FullQName", "NCNameWithLocalWildcard", "NCNameWithPrefixWildcard", "NCName", 
			"XQDOC_COMMENT_START", "XQDOC_COMMENT_END", "XQDocComment", "XQComment", 
			"CHAR", "ENTER_STRING", "EXIT_INTERPOLATION", "ContentChar", "BASIC_CHAR", 
			"ENTER_INTERPOLATION", "EXIT_STRING", "EscapeQuot_QuotString", "DOUBLE_LBRACE_QuotString", 
			"DOUBLE_RBRACE_QuotString", "EscapeApos_AposString"
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
	public String getGrammarFileName() { return "XQueryParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public XQueryParser(TokenStream input) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_module);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(484);
				xqDocComment();
				}
				break;
			}
			setState(488);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(487);
				versionDecl();
				}
				break;
			}
			setState(491);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(490);
				xqDocComment();
				}
				break;
			}
			setState(495);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(493);
				libraryModule();
				}
				break;
			case 2:
				{
				setState(494);
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
		public TerminalNode XQDocComment() { return getToken(XQueryParser.XQDocComment, 0); }
		public XqDocCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xqDocComment; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitXqDocComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XqDocCommentContext xqDocComment() throws RecognitionException {
		XqDocCommentContext _localctx = new XqDocCommentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_xqDocComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
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
		public TerminalNode KW_XQUERY() { return getToken(XQueryParser.KW_XQUERY, 0); }
		public TerminalNode KW_VERSION() { return getToken(XQueryParser.KW_VERSION, 0); }
		public TerminalNode SEMICOLON() { return getToken(XQueryParser.SEMICOLON, 0); }
		public List<StringLiteralContext> stringLiteral() {
			return getRuleContexts(StringLiteralContext.class);
		}
		public StringLiteralContext stringLiteral(int i) {
			return getRuleContext(StringLiteralContext.class,i);
		}
		public TerminalNode KW_ENCODING() { return getToken(XQueryParser.KW_ENCODING, 0); }
		public VersionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_versionDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitVersionDecl(this);
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
			setState(499);
			match(KW_XQUERY);
			setState(500);
			match(KW_VERSION);
			setState(501);
			((VersionDeclContext)_localctx).version = stringLiteral();
			setState(504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_ENCODING) {
				{
				setState(502);
				match(KW_ENCODING);
				setState(503);
				((VersionDeclContext)_localctx).encoding = stringLiteral();
				}
			}

			setState(506);
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
		public QueryBodyContext queryBody() {
			return getRuleContext(QueryBodyContext.class,0);
		}
		public MainModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mainModule; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMainModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainModuleContext mainModule() throws RecognitionException {
		MainModuleContext _localctx = new MainModuleContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_mainModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(508);
			prolog();
			setState(509);
			queryBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QueryBodyContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public QueryBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitQueryBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryBodyContext queryBody() throws RecognitionException {
		QueryBodyContext _localctx = new QueryBodyContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_queryBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitLibraryModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryModuleContext libraryModule() throws RecognitionException {
		LibraryModuleContext _localctx = new LibraryModuleContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_libraryModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
			moduleDecl();
			setState(514);
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
		public TerminalNode KW_MODULE() { return getToken(XQueryParser.KW_MODULE, 0); }
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParser.KW_NAMESPACE, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(XQueryParser.EQUAL, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(XQueryParser.SEMICOLON, 0); }
		public ModuleDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitModuleDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleDeclContext moduleDecl() throws RecognitionException {
		ModuleDeclContext _localctx = new ModuleDeclContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_moduleDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(516);
			match(KW_MODULE);
			setState(517);
			match(KW_NAMESPACE);
			setState(518);
			ncName();
			setState(519);
			match(EQUAL);
			setState(520);
			uriLiteral();
			setState(521);
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
		public List<TerminalNode> SEMICOLON() { return getTokens(XQueryParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(XQueryParser.SEMICOLON, i);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitProlog(this);
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
			setState(534);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(528);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						setState(523);
						defaultNamespaceDecl();
						}
						break;
					case 2:
						{
						setState(524);
						setter();
						}
						break;
					case 3:
						{
						setState(525);
						namespaceDecl();
						}
						break;
					case 4:
						{
						setState(526);
						schemaImport();
						}
						break;
					case 5:
						{
						setState(527);
						moduleImport();
						}
						break;
					}
					setState(530);
					match(SEMICOLON);
					}
					} 
				}
				setState(536);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(545);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(538);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==XQDocComment) {
						{
						setState(537);
						xqDocComment();
						}
					}

					setState(540);
					annotatedDecl();
					setState(541);
					match(SEMICOLON);
					}
					} 
				}
				setState(547);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnnotatedDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotatedDeclContext annotatedDecl() throws RecognitionException {
		AnnotatedDeclContext _localctx = new AnnotatedDeclContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_annotatedDecl);
		try {
			setState(552);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(548);
				varDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(549);
				functionDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(550);
				contextItemDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(551);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParser.KW_DEFAULT, 0); }
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParser.KW_NAMESPACE, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParser.KW_ELEMENT, 0); }
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParser.KW_FUNCTION, 0); }
		public DefaultNamespaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultNamespaceDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDefaultNamespaceDecl(this);
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
			setState(554);
			match(KW_DECLARE);
			setState(555);
			match(KW_DEFAULT);
			setState(556);
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
			setState(557);
			match(KW_NAMESPACE);
			setState(558);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSetter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetterContext setter() throws RecognitionException {
		SetterContext _localctx = new SetterContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_setter);
		try {
			setState(568);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(560);
				boundarySpaceDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(561);
				defaultCollationDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(562);
				baseURIDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(563);
				constructionDecl();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(564);
				orderingModeDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(565);
				emptyOrderDecl();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(566);
				copyNamespacesDecl();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(567);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_BOUNDARY_SPACE() { return getToken(XQueryParser.KW_BOUNDARY_SPACE, 0); }
		public TerminalNode KW_PRESERVE() { return getToken(XQueryParser.KW_PRESERVE, 0); }
		public TerminalNode KW_STRIP() { return getToken(XQueryParser.KW_STRIP, 0); }
		public BoundarySpaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boundarySpaceDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitBoundarySpaceDecl(this);
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
			setState(570);
			match(KW_DECLARE);
			setState(571);
			match(KW_BOUNDARY_SPACE);
			setState(572);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParser.KW_DEFAULT, 0); }
		public TerminalNode KW_COLLATION() { return getToken(XQueryParser.KW_COLLATION, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public DefaultCollationDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultCollationDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDefaultCollationDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultCollationDeclContext defaultCollationDecl() throws RecognitionException {
		DefaultCollationDeclContext _localctx = new DefaultCollationDeclContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(574);
			match(KW_DECLARE);
			setState(575);
			match(KW_DEFAULT);
			setState(576);
			match(KW_COLLATION);
			setState(577);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_BASE_URI() { return getToken(XQueryParser.KW_BASE_URI, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public BaseURIDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseURIDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitBaseURIDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseURIDeclContext baseURIDecl() throws RecognitionException {
		BaseURIDeclContext _localctx = new BaseURIDeclContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_baseURIDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(579);
			match(KW_DECLARE);
			setState(580);
			match(KW_BASE_URI);
			setState(581);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_CONSTRUCTION() { return getToken(XQueryParser.KW_CONSTRUCTION, 0); }
		public TerminalNode KW_STRIP() { return getToken(XQueryParser.KW_STRIP, 0); }
		public TerminalNode KW_PRESERVE() { return getToken(XQueryParser.KW_PRESERVE, 0); }
		public ConstructionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructionDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitConstructionDecl(this);
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
			setState(583);
			match(KW_DECLARE);
			setState(584);
			match(KW_CONSTRUCTION);
			setState(585);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_ORDERING() { return getToken(XQueryParser.KW_ORDERING, 0); }
		public TerminalNode KW_ORDERED() { return getToken(XQueryParser.KW_ORDERED, 0); }
		public TerminalNode KW_UNORDERED() { return getToken(XQueryParser.KW_UNORDERED, 0); }
		public OrderingModeDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderingModeDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitOrderingModeDecl(this);
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
			setState(587);
			match(KW_DECLARE);
			setState(588);
			match(KW_ORDERING);
			setState(589);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParser.KW_DEFAULT, 0); }
		public TerminalNode KW_ORDER() { return getToken(XQueryParser.KW_ORDER, 0); }
		public TerminalNode KW_EMPTY() { return getToken(XQueryParser.KW_EMPTY, 0); }
		public TerminalNode KW_GREATEST() { return getToken(XQueryParser.KW_GREATEST, 0); }
		public TerminalNode KW_LEAST() { return getToken(XQueryParser.KW_LEAST, 0); }
		public EmptyOrderDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyOrderDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitEmptyOrderDecl(this);
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
			setState(591);
			match(KW_DECLARE);
			setState(592);
			match(KW_DEFAULT);
			setState(593);
			match(KW_ORDER);
			setState(594);
			match(KW_EMPTY);
			setState(595);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_COPY_NS() { return getToken(XQueryParser.KW_COPY_NS, 0); }
		public PreserveModeContext preserveMode() {
			return getRuleContext(PreserveModeContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParser.COMMA, 0); }
		public InheritModeContext inheritMode() {
			return getRuleContext(InheritModeContext.class,0);
		}
		public CopyNamespacesDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_copyNamespacesDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCopyNamespacesDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CopyNamespacesDeclContext copyNamespacesDecl() throws RecognitionException {
		CopyNamespacesDeclContext _localctx = new CopyNamespacesDeclContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_copyNamespacesDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(597);
			match(KW_DECLARE);
			setState(598);
			match(KW_COPY_NS);
			setState(599);
			preserveMode();
			setState(600);
			match(COMMA);
			setState(601);
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
		public TerminalNode KW_PRESERVE() { return getToken(XQueryParser.KW_PRESERVE, 0); }
		public TerminalNode KW_NO_PRESERVE() { return getToken(XQueryParser.KW_NO_PRESERVE, 0); }
		public PreserveModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_preserveMode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPreserveMode(this);
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
			setState(603);
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
		public TerminalNode KW_INHERIT() { return getToken(XQueryParser.KW_INHERIT, 0); }
		public TerminalNode KW_NO_INHERIT() { return getToken(XQueryParser.KW_NO_INHERIT, 0); }
		public InheritModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inheritMode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitInheritMode(this);
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
			setState(605);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public List<TerminalNode> DFPropertyName() { return getTokens(XQueryParser.DFPropertyName); }
		public TerminalNode DFPropertyName(int i) {
			return getToken(XQueryParser.DFPropertyName, i);
		}
		public List<TerminalNode> EQUAL() { return getTokens(XQueryParser.EQUAL); }
		public TerminalNode EQUAL(int i) {
			return getToken(XQueryParser.EQUAL, i);
		}
		public List<StringLiteralContext> stringLiteral() {
			return getRuleContexts(StringLiteralContext.class);
		}
		public StringLiteralContext stringLiteral(int i) {
			return getRuleContext(StringLiteralContext.class,i);
		}
		public TerminalNode KW_DECIMAL_FORMAT() { return getToken(XQueryParser.KW_DECIMAL_FORMAT, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParser.KW_DEFAULT, 0); }
		public DecimalFormatDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalFormatDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDecimalFormatDecl(this);
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
			setState(607);
			match(KW_DECLARE);
			setState(612);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_DECIMAL_FORMAT:
				{
				{
				setState(608);
				match(KW_DECIMAL_FORMAT);
				setState(609);
				eqName();
				}
				}
				break;
			case KW_DEFAULT:
				{
				{
				setState(610);
				match(KW_DEFAULT);
				setState(611);
				match(KW_DECIMAL_FORMAT);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(619);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DFPropertyName) {
				{
				{
				setState(614);
				match(DFPropertyName);
				setState(615);
				match(EQUAL);
				setState(616);
				stringLiteral();
				}
				}
				setState(621);
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
		public TerminalNode KW_IMPORT() { return getToken(XQueryParser.KW_IMPORT, 0); }
		public TerminalNode KW_SCHEMA() { return getToken(XQueryParser.KW_SCHEMA, 0); }
		public List<UriLiteralContext> uriLiteral() {
			return getRuleContexts(UriLiteralContext.class);
		}
		public UriLiteralContext uriLiteral(int i) {
			return getRuleContext(UriLiteralContext.class,i);
		}
		public SchemaPrefixContext schemaPrefix() {
			return getRuleContext(SchemaPrefixContext.class,0);
		}
		public TerminalNode KW_AT() { return getToken(XQueryParser.KW_AT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public SchemaImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaImport; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSchemaImport(this);
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
			setState(622);
			match(KW_IMPORT);
			setState(623);
			match(KW_SCHEMA);
			setState(625);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_DEFAULT || _la==KW_NAMESPACE) {
				{
				setState(624);
				schemaPrefix();
				}
			}

			setState(627);
			((SchemaImportContext)_localctx).nsURI = uriLiteral();
			setState(637);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AT) {
				{
				setState(628);
				match(KW_AT);
				setState(629);
				((SchemaImportContext)_localctx).uriLiteral = uriLiteral();
				((SchemaImportContext)_localctx).locations.add(((SchemaImportContext)_localctx).uriLiteral);
				setState(634);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(630);
					match(COMMA);
					setState(631);
					((SchemaImportContext)_localctx).uriLiteral = uriLiteral();
					((SchemaImportContext)_localctx).locations.add(((SchemaImportContext)_localctx).uriLiteral);
					}
					}
					setState(636);
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
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParser.KW_NAMESPACE, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(XQueryParser.EQUAL, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParser.KW_DEFAULT, 0); }
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParser.KW_ELEMENT, 0); }
		public SchemaPrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaPrefix; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSchemaPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaPrefixContext schemaPrefix() throws RecognitionException {
		SchemaPrefixContext _localctx = new SchemaPrefixContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_schemaPrefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(646);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_NAMESPACE:
				{
				setState(639);
				match(KW_NAMESPACE);
				setState(640);
				ncName();
				setState(641);
				match(EQUAL);
				}
				break;
			case KW_DEFAULT:
				{
				setState(643);
				match(KW_DEFAULT);
				setState(644);
				match(KW_ELEMENT);
				setState(645);
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
		public TerminalNode KW_IMPORT() { return getToken(XQueryParser.KW_IMPORT, 0); }
		public TerminalNode KW_MODULE() { return getToken(XQueryParser.KW_MODULE, 0); }
		public List<UriLiteralContext> uriLiteral() {
			return getRuleContexts(UriLiteralContext.class);
		}
		public UriLiteralContext uriLiteral(int i) {
			return getRuleContext(UriLiteralContext.class,i);
		}
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParser.KW_NAMESPACE, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(XQueryParser.EQUAL, 0); }
		public TerminalNode KW_AT() { return getToken(XQueryParser.KW_AT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public ModuleImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleImport; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitModuleImport(this);
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
			setState(648);
			match(KW_IMPORT);
			setState(649);
			match(KW_MODULE);
			setState(654);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_NAMESPACE) {
				{
				setState(650);
				match(KW_NAMESPACE);
				setState(651);
				ncName();
				setState(652);
				match(EQUAL);
				}
			}

			setState(656);
			((ModuleImportContext)_localctx).nsURI = uriLiteral();
			setState(666);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AT) {
				{
				setState(657);
				match(KW_AT);
				setState(658);
				((ModuleImportContext)_localctx).uriLiteral = uriLiteral();
				((ModuleImportContext)_localctx).locations.add(((ModuleImportContext)_localctx).uriLiteral);
				setState(663);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(659);
					match(COMMA);
					setState(660);
					((ModuleImportContext)_localctx).uriLiteral = uriLiteral();
					((ModuleImportContext)_localctx).locations.add(((ModuleImportContext)_localctx).uriLiteral);
					}
					}
					setState(665);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParser.KW_NAMESPACE, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(XQueryParser.EQUAL, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public NamespaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNamespaceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclContext namespaceDecl() throws RecognitionException {
		NamespaceDeclContext _localctx = new NamespaceDeclContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_namespaceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(668);
			match(KW_DECLARE);
			setState(669);
			match(KW_NAMESPACE);
			setState(670);
			ncName();
			setState(671);
			match(EQUAL);
			setState(672);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode KW_VARIABLE() { return getToken(XQueryParser.KW_VARIABLE, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParser.COLON_EQ, 0); }
		public VarValueContext varValue() {
			return getRuleContext(VarValueContext.class,0);
		}
		public TerminalNode KW_EXTERNAL() { return getToken(XQueryParser.KW_EXTERNAL, 0); }
		public VarDefaultValueContext varDefaultValue() {
			return getRuleContext(VarDefaultValueContext.class,0);
		}
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitVarDecl(this);
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
			setState(674);
			match(KW_DECLARE);
			setState(675);
			annotations();
			setState(676);
			match(KW_VARIABLE);
			setState(677);
			match(DOLLAR);
			setState(678);
			varName();
			setState(680);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(679);
				typeDeclaration();
				}
			}

			setState(689);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLON_EQ:
				{
				{
				setState(682);
				match(COLON_EQ);
				setState(683);
				varValue();
				}
				}
				break;
			case KW_EXTERNAL:
				{
				{
				setState(684);
				match(KW_EXTERNAL);
				setState(687);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON_EQ) {
					{
					setState(685);
					match(COLON_EQ);
					setState(686);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitVarValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarValueContext varValue() throws RecognitionException {
		VarValueContext _localctx = new VarValueContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_varValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(691);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitVarDefaultValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDefaultValueContext varDefaultValue() throws RecognitionException {
		VarDefaultValueContext _localctx = new VarDefaultValueContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_varDefaultValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(693);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_CONTEXT() { return getToken(XQueryParser.KW_CONTEXT, 0); }
		public TerminalNode KW_ITEM() { return getToken(XQueryParser.KW_ITEM, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParser.COLON_EQ, 0); }
		public TerminalNode KW_EXTERNAL() { return getToken(XQueryParser.KW_EXTERNAL, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ContextItemDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextItemDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitContextItemDecl(this);
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
			setState(695);
			match(KW_DECLARE);
			setState(696);
			match(KW_CONTEXT);
			setState(697);
			match(KW_ITEM);
			setState(700);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(698);
				match(KW_AS);
				setState(699);
				itemType();
				}
			}

			setState(709);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLON_EQ:
				{
				{
				setState(702);
				match(COLON_EQ);
				setState(703);
				((ContextItemDeclContext)_localctx).value = exprSingle();
				}
				}
				break;
			case KW_EXTERNAL:
				{
				{
				setState(704);
				match(KW_EXTERNAL);
				setState(707);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON_EQ) {
					{
					setState(705);
					match(COLON_EQ);
					setState(706);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParser.KW_FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public TerminalNode KW_EXTERNAL() { return getToken(XQueryParser.KW_EXTERNAL, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionDecl(this);
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
			setState(711);
			match(KW_DECLARE);
			setState(712);
			annotations();
			setState(713);
			match(KW_FUNCTION);
			setState(714);
			((FunctionDeclContext)_localctx).name = eqName();
			setState(715);
			match(LPAREN);
			setState(717);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(716);
				functionParams();
				}
			}

			setState(719);
			match(RPAREN);
			setState(721);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(720);
				functionReturn();
				}
			}

			setState(725);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				{
				setState(723);
				functionBody();
				}
				break;
			case KW_EXTERNAL:
				{
				setState(724);
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
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public FunctionParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionParams; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionParams(this);
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
			setState(727);
			functionParam();
			setState(732);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(728);
				match(COMMA);
				setState(729);
				functionParam();
				}
				}
				setState(734);
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
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionParam(this);
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
			setState(735);
			match(DOLLAR);
			setState(736);
			((FunctionParamContext)_localctx).name = qName();
			setState(738);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(737);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnnotations(this);
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
			setState(743);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==MOD) {
				{
				{
				setState(740);
				annotation();
				}
				}
				setState(745);
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
		public TerminalNode MOD() { return getToken(XQueryParser.MOD, 0); }
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public AnnotListContext annotList() {
			return getRuleContext(AnnotListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnnotation(this);
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
			setState(746);
			match(MOD);
			setState(747);
			qName();
			setState(752);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(748);
				match(LPAREN);
				setState(749);
				annotList();
				setState(750);
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
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public AnnotListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnnotList(this);
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
			setState(754);
			annotationParam();
			setState(759);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(755);
				match(COMMA);
				setState(756);
				annotationParam();
				}
				}
				setState(761);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnnotationParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationParamContext annotationParam() throws RecognitionException {
		AnnotationParamContext _localctx = new AnnotationParamContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_annotationParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(762);
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
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public FunctionReturnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReturn; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionReturn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReturnContext functionReturn() throws RecognitionException {
		FunctionReturnContext _localctx = new FunctionReturnContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_functionReturn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(764);
			match(KW_AS);
			setState(765);
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
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_OPTION() { return getToken(XQueryParser.KW_OPTION, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitOptionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OptionDeclContext optionDecl() throws RecognitionException {
		OptionDeclContext _localctx = new OptionDeclContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_optionDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(767);
			match(KW_DECLARE);
			setState(768);
			match(KW_OPTION);
			setState(769);
			((OptionDeclContext)_localctx).name = qName();
			setState(770);
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
	public static class ExprContext extends ParserRuleContext {
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_expr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(772);
			exprSingle();
			setState(777);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(773);
					match(COMMA);
					setState(774);
					exprSingle();
					}
					} 
				}
				setState(779);
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
	public static class ExprSingleContext extends ParserRuleContext {
		public FlworExprContext flworExpr() {
			return getRuleContext(FlworExprContext.class,0);
		}
		public QuantifiedExprContext quantifiedExpr() {
			return getRuleContext(QuantifiedExprContext.class,0);
		}
		public SwitchExprContext switchExpr() {
			return getRuleContext(SwitchExprContext.class,0);
		}
		public TypeswitchExprContext typeswitchExpr() {
			return getRuleContext(TypeswitchExprContext.class,0);
		}
		public ExistUpdateExprContext existUpdateExpr() {
			return getRuleContext(ExistUpdateExprContext.class,0);
		}
		public IfExprContext ifExpr() {
			return getRuleContext(IfExprContext.class,0);
		}
		public TryCatchExprContext tryCatchExpr() {
			return getRuleContext(TryCatchExprContext.class,0);
		}
		public OrExprContext orExpr() {
			return getRuleContext(OrExprContext.class,0);
		}
		public ExprSingleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprSingle; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExprSingle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSingleContext exprSingle() throws RecognitionException {
		ExprSingleContext _localctx = new ExprSingleContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_exprSingle);
		try {
			setState(788);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(780);
				flworExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(781);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(782);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(783);
				typeswitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(784);
				existUpdateExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(785);
				ifExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(786);
				tryCatchExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(787);
				orExpr();
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFlworExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlworExprContext flworExpr() throws RecognitionException {
		FlworExprContext _localctx = new FlworExprContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_flworExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(790);
			initialClause();
			setState(794);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 72058693620858881L) != 0) || _la==KW_STABLE || _la==KW_WHERE) {
				{
				{
				setState(791);
				intermediateClause();
				}
				}
				setState(796);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(797);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitInitialClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitialClauseContext initialClause() throws RecognitionException {
		InitialClauseContext _localctx = new InitialClauseContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_initialClause);
		try {
			setState(802);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(799);
				forClause();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(800);
				letClause();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(801);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitIntermediateClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntermediateClauseContext intermediateClause() throws RecognitionException {
		IntermediateClauseContext _localctx = new IntermediateClauseContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_intermediateClause);
		try {
			setState(809);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_FOR:
			case KW_LET:
				enterOuterAlt(_localctx, 1);
				{
				setState(804);
				initialClause();
				}
				break;
			case KW_WHERE:
				enterOuterAlt(_localctx, 2);
				{
				setState(805);
				whereClause();
				}
				break;
			case KW_GROUP:
				enterOuterAlt(_localctx, 3);
				{
				setState(806);
				groupByClause();
				}
				break;
			case KW_ORDER:
			case KW_STABLE:
				enterOuterAlt(_localctx, 4);
				{
				setState(807);
				orderByClause();
				}
				break;
			case KW_COUNT:
				enterOuterAlt(_localctx, 5);
				{
				setState(808);
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
		public TerminalNode KW_FOR() { return getToken(XQueryParser.KW_FOR, 0); }
		public List<ForBindingContext> forBinding() {
			return getRuleContexts(ForBindingContext.class);
		}
		public ForBindingContext forBinding(int i) {
			return getRuleContext(ForBindingContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public ForClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitForClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForClauseContext forClause() throws RecognitionException {
		ForClauseContext _localctx = new ForClauseContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(811);
			match(KW_FOR);
			setState(812);
			((ForClauseContext)_localctx).forBinding = forBinding();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forBinding);
			setState(817);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(813);
				match(COMMA);
				setState(814);
				((ForClauseContext)_localctx).forBinding = forBinding();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forBinding);
				}
				}
				setState(819);
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
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public TerminalNode KW_IN() { return getToken(XQueryParser.KW_IN, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitForBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForBindingContext forBinding() throws RecognitionException {
		ForBindingContext _localctx = new ForBindingContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_forBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(820);
			match(DOLLAR);
			setState(821);
			((ForBindingContext)_localctx).name = varName();
			setState(823);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(822);
				((ForBindingContext)_localctx).seq = typeDeclaration();
				}
			}

			setState(826);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_ALLOWING) {
				{
				setState(825);
				((ForBindingContext)_localctx).flag = allowingEmpty();
				}
			}

			setState(829);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AT) {
				{
				setState(828);
				((ForBindingContext)_localctx).at = positionalVar();
				}
			}

			setState(831);
			match(KW_IN);
			setState(832);
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
		public TerminalNode KW_ALLOWING() { return getToken(XQueryParser.KW_ALLOWING, 0); }
		public TerminalNode KW_EMPTY() { return getToken(XQueryParser.KW_EMPTY, 0); }
		public AllowingEmptyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_allowingEmpty; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAllowingEmpty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AllowingEmptyContext allowingEmpty() throws RecognitionException {
		AllowingEmptyContext _localctx = new AllowingEmptyContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_allowingEmpty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(834);
			match(KW_ALLOWING);
			setState(835);
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
		public TerminalNode KW_AT() { return getToken(XQueryParser.KW_AT, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public PositionalVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_positionalVar; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPositionalVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PositionalVarContext positionalVar() throws RecognitionException {
		PositionalVarContext _localctx = new PositionalVarContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_positionalVar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(837);
			match(KW_AT);
			setState(838);
			match(DOLLAR);
			setState(839);
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
		public TerminalNode KW_LET() { return getToken(XQueryParser.KW_LET, 0); }
		public List<LetBindingContext> letBinding() {
			return getRuleContexts(LetBindingContext.class);
		}
		public LetBindingContext letBinding(int i) {
			return getRuleContext(LetBindingContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public LetClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitLetClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetClauseContext letClause() throws RecognitionException {
		LetClauseContext _localctx = new LetClauseContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(841);
			match(KW_LET);
			setState(842);
			((LetClauseContext)_localctx).letBinding = letBinding();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letBinding);
			setState(847);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(843);
				match(COMMA);
				setState(844);
				((LetClauseContext)_localctx).letBinding = letBinding();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letBinding);
				}
				}
				setState(849);
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
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParser.COLON_EQ, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitLetBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetBindingContext letBinding() throws RecognitionException {
		LetBindingContext _localctx = new LetBindingContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_letBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(850);
			match(DOLLAR);
			setState(851);
			varName();
			setState(853);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(852);
				typeDeclaration();
				}
			}

			setState(855);
			match(COLON_EQ);
			setState(856);
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
		public TerminalNode KW_FOR() { return getToken(XQueryParser.KW_FOR, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitWindowClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowClauseContext windowClause() throws RecognitionException {
		WindowClauseContext _localctx = new WindowClauseContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_windowClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
			match(KW_FOR);
			setState(861);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_TUMBLING:
				{
				setState(859);
				tumblingWindowClause();
				}
				break;
			case KW_SLIDING:
				{
				setState(860);
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
		public TerminalNode KW_TUMBLING() { return getToken(XQueryParser.KW_TUMBLING, 0); }
		public TerminalNode KW_WINDOW() { return getToken(XQueryParser.KW_WINDOW, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public TerminalNode KW_IN() { return getToken(XQueryParser.KW_IN, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTumblingWindowClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TumblingWindowClauseContext tumblingWindowClause() throws RecognitionException {
		TumblingWindowClauseContext _localctx = new TumblingWindowClauseContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_tumblingWindowClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(863);
			match(KW_TUMBLING);
			setState(864);
			match(KW_WINDOW);
			setState(865);
			match(DOLLAR);
			setState(866);
			((TumblingWindowClauseContext)_localctx).name = qName();
			setState(868);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(867);
				((TumblingWindowClauseContext)_localctx).type = typeDeclaration();
				}
			}

			setState(870);
			match(KW_IN);
			setState(871);
			exprSingle();
			setState(872);
			windowStartCondition();
			setState(874);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_END || _la==KW_ONLY) {
				{
				setState(873);
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
		public TerminalNode KW_SLIDING() { return getToken(XQueryParser.KW_SLIDING, 0); }
		public TerminalNode KW_WINDOW() { return getToken(XQueryParser.KW_WINDOW, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public TerminalNode KW_IN() { return getToken(XQueryParser.KW_IN, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSlidingWindowClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SlidingWindowClauseContext slidingWindowClause() throws RecognitionException {
		SlidingWindowClauseContext _localctx = new SlidingWindowClauseContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_slidingWindowClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(876);
			match(KW_SLIDING);
			setState(877);
			match(KW_WINDOW);
			setState(878);
			match(DOLLAR);
			setState(879);
			((SlidingWindowClauseContext)_localctx).name = qName();
			setState(881);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(880);
				((SlidingWindowClauseContext)_localctx).type = typeDeclaration();
				}
			}

			setState(883);
			match(KW_IN);
			setState(884);
			exprSingle();
			setState(885);
			windowStartCondition();
			setState(886);
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
		public TerminalNode KW_START() { return getToken(XQueryParser.KW_START, 0); }
		public WindowVarsContext windowVars() {
			return getRuleContext(WindowVarsContext.class,0);
		}
		public TerminalNode KW_WHEN() { return getToken(XQueryParser.KW_WHEN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public WindowStartConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_windowStartCondition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitWindowStartCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowStartConditionContext windowStartCondition() throws RecognitionException {
		WindowStartConditionContext _localctx = new WindowStartConditionContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_windowStartCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(888);
			match(KW_START);
			setState(889);
			windowVars();
			setState(890);
			match(KW_WHEN);
			setState(891);
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
		public TerminalNode KW_END() { return getToken(XQueryParser.KW_END, 0); }
		public WindowVarsContext windowVars() {
			return getRuleContext(WindowVarsContext.class,0);
		}
		public TerminalNode KW_WHEN() { return getToken(XQueryParser.KW_WHEN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode KW_ONLY() { return getToken(XQueryParser.KW_ONLY, 0); }
		public WindowEndConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_windowEndCondition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitWindowEndCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowEndConditionContext windowEndCondition() throws RecognitionException {
		WindowEndConditionContext _localctx = new WindowEndConditionContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_windowEndCondition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(894);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_ONLY) {
				{
				setState(893);
				match(KW_ONLY);
				}
			}

			setState(896);
			match(KW_END);
			setState(897);
			windowVars();
			setState(898);
			match(KW_WHEN);
			setState(899);
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
		public List<TerminalNode> DOLLAR() { return getTokens(XQueryParser.DOLLAR); }
		public TerminalNode DOLLAR(int i) {
			return getToken(XQueryParser.DOLLAR, i);
		}
		public PositionalVarContext positionalVar() {
			return getRuleContext(PositionalVarContext.class,0);
		}
		public TerminalNode KW_PREVIOUS() { return getToken(XQueryParser.KW_PREVIOUS, 0); }
		public TerminalNode KW_NEXT() { return getToken(XQueryParser.KW_NEXT, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitWindowVars(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WindowVarsContext windowVars() throws RecognitionException {
		WindowVarsContext _localctx = new WindowVarsContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_windowVars);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(903);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(901);
				match(DOLLAR);
				setState(902);
				((WindowVarsContext)_localctx).currentItem = eqName();
				}
			}

			setState(906);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AT) {
				{
				setState(905);
				positionalVar();
				}
			}

			setState(911);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_PREVIOUS) {
				{
				setState(908);
				match(KW_PREVIOUS);
				setState(909);
				match(DOLLAR);
				setState(910);
				((WindowVarsContext)_localctx).previousItem = eqName();
				}
			}

			setState(916);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_NEXT) {
				{
				setState(913);
				match(KW_NEXT);
				setState(914);
				match(DOLLAR);
				setState(915);
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
		public TerminalNode KW_COUNT() { return getToken(XQueryParser.KW_COUNT, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public CountClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_countClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCountClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountClauseContext countClause() throws RecognitionException {
		CountClauseContext _localctx = new CountClauseContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(918);
			match(KW_COUNT);
			setState(919);
			match(DOLLAR);
			setState(920);
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
		public TerminalNode KW_WHERE() { return getToken(XQueryParser.KW_WHERE, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(922);
			match(KW_WHERE);
			setState(923);
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
		public TerminalNode KW_GROUP() { return getToken(XQueryParser.KW_GROUP, 0); }
		public TerminalNode KW_BY() { return getToken(XQueryParser.KW_BY, 0); }
		public GroupingSpecListContext groupingSpecList() {
			return getRuleContext(GroupingSpecListContext.class,0);
		}
		public GroupByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitGroupByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByClauseContext groupByClause() throws RecognitionException {
		GroupByClauseContext _localctx = new GroupByClauseContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_groupByClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(925);
			match(KW_GROUP);
			setState(926);
			match(KW_BY);
			setState(927);
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
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public GroupingSpecListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupingSpecList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitGroupingSpecList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupingSpecListContext groupingSpecList() throws RecognitionException {
		GroupingSpecListContext _localctx = new GroupingSpecListContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_groupingSpecList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			((GroupingSpecListContext)_localctx).groupingSpec = groupingSpec();
			((GroupingSpecListContext)_localctx).vars.add(((GroupingSpecListContext)_localctx).groupingSpec);
			setState(934);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(930);
				match(COMMA);
				setState(931);
				((GroupingSpecListContext)_localctx).groupingSpec = groupingSpec();
				((GroupingSpecListContext)_localctx).vars.add(((GroupingSpecListContext)_localctx).groupingSpec);
				}
				}
				setState(936);
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
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode COLON_EQ() { return getToken(XQueryParser.COLON_EQ, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode KW_COLLATION() { return getToken(XQueryParser.KW_COLLATION, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitGroupingSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupingSpecContext groupingSpec() throws RecognitionException {
		GroupingSpecContext _localctx = new GroupingSpecContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_groupingSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(937);
			match(DOLLAR);
			setState(938);
			((GroupingSpecContext)_localctx).name = varName();
			setState(944);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON_EQ || _la==KW_AS) {
				{
				setState(940);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KW_AS) {
					{
					setState(939);
					((GroupingSpecContext)_localctx).type = typeDeclaration();
					}
				}

				setState(942);
				match(COLON_EQ);
				setState(943);
				exprSingle();
				}
			}

			setState(948);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_COLLATION) {
				{
				setState(946);
				match(KW_COLLATION);
				setState(947);
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
		public TerminalNode KW_ORDER() { return getToken(XQueryParser.KW_ORDER, 0); }
		public TerminalNode KW_BY() { return getToken(XQueryParser.KW_BY, 0); }
		public List<OrderSpecContext> orderSpec() {
			return getRuleContexts(OrderSpecContext.class);
		}
		public OrderSpecContext orderSpec(int i) {
			return getRuleContext(OrderSpecContext.class,i);
		}
		public TerminalNode KW_STABLE() { return getToken(XQueryParser.KW_STABLE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_STABLE) {
				{
				setState(950);
				match(KW_STABLE);
				}
			}

			setState(953);
			match(KW_ORDER);
			setState(954);
			match(KW_BY);
			setState(955);
			((OrderByClauseContext)_localctx).orderSpec = orderSpec();
			((OrderByClauseContext)_localctx).specs.add(((OrderByClauseContext)_localctx).orderSpec);
			setState(960);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(956);
				match(COMMA);
				setState(957);
				((OrderByClauseContext)_localctx).orderSpec = orderSpec();
				((OrderByClauseContext)_localctx).specs.add(((OrderByClauseContext)_localctx).orderSpec);
				}
				}
				setState(962);
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
		public TerminalNode KW_ASCENDING() { return getToken(XQueryParser.KW_ASCENDING, 0); }
		public TerminalNode KW_EMPTY() { return getToken(XQueryParser.KW_EMPTY, 0); }
		public TerminalNode KW_COLLATION() { return getToken(XQueryParser.KW_COLLATION, 0); }
		public TerminalNode KW_DESCENDING() { return getToken(XQueryParser.KW_DESCENDING, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public TerminalNode KW_GREATEST() { return getToken(XQueryParser.KW_GREATEST, 0); }
		public TerminalNode KW_LEAST() { return getToken(XQueryParser.KW_LEAST, 0); }
		public OrderSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitOrderSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderSpecContext orderSpec() throws RecognitionException {
		OrderSpecContext _localctx = new OrderSpecContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_orderSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(963);
			((OrderSpecContext)_localctx).ex = exprSingle();
			setState(966);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ASCENDING:
				{
				setState(964);
				match(KW_ASCENDING);
				}
				break;
			case KW_DESCENDING:
				{
				setState(965);
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
			setState(973);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_EMPTY) {
				{
				setState(968);
				match(KW_EMPTY);
				setState(971);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case KW_GREATEST:
					{
					setState(969);
					((OrderSpecContext)_localctx).gr = match(KW_GREATEST);
					}
					break;
				case KW_LEAST:
					{
					setState(970);
					((OrderSpecContext)_localctx).ls = match(KW_LEAST);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(977);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_COLLATION) {
				{
				setState(975);
				match(KW_COLLATION);
				setState(976);
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
		public TerminalNode KW_RETURN() { return getToken(XQueryParser.KW_RETURN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ReturnClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitReturnClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnClauseContext returnClause() throws RecognitionException {
		ReturnClauseContext _localctx = new ReturnClauseContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_returnClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(979);
			match(KW_RETURN);
			setState(980);
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
		public TerminalNode KW_SATISFIES() { return getToken(XQueryParser.KW_SATISFIES, 0); }
		public List<QuantifiedVarContext> quantifiedVar() {
			return getRuleContexts(QuantifiedVarContext.class);
		}
		public QuantifiedVarContext quantifiedVar(int i) {
			return getRuleContext(QuantifiedVarContext.class,i);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode KW_SOME() { return getToken(XQueryParser.KW_SOME, 0); }
		public TerminalNode KW_EVERY() { return getToken(XQueryParser.KW_EVERY, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public QuantifiedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantifiedExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitQuantifiedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedExprContext quantifiedExpr() throws RecognitionException {
		QuantifiedExprContext _localctx = new QuantifiedExprContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(984);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_SOME:
				{
				setState(982);
				((QuantifiedExprContext)_localctx).so = match(KW_SOME);
				}
				break;
			case KW_EVERY:
				{
				setState(983);
				((QuantifiedExprContext)_localctx).ev = match(KW_EVERY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(986);
			((QuantifiedExprContext)_localctx).quantifiedVar = quantifiedVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedVar);
			setState(991);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(987);
				match(COMMA);
				setState(988);
				((QuantifiedExprContext)_localctx).quantifiedVar = quantifiedVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedVar);
				}
				}
				setState(993);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(994);
			match(KW_SATISFIES);
			setState(995);
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
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode KW_IN() { return getToken(XQueryParser.KW_IN, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitQuantifiedVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedVarContext quantifiedVar() throws RecognitionException {
		QuantifiedVarContext _localctx = new QuantifiedVarContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_quantifiedVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(997);
			match(DOLLAR);
			setState(998);
			varName();
			setState(1000);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(999);
				typeDeclaration();
				}
			}

			setState(1002);
			match(KW_IN);
			setState(1003);
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
		public TerminalNode KW_SWITCH() { return getToken(XQueryParser.KW_SWITCH, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParser.KW_DEFAULT, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParser.KW_RETURN, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSwitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchExprContext switchExpr() throws RecognitionException {
		SwitchExprContext _localctx = new SwitchExprContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1005);
			match(KW_SWITCH);
			setState(1006);
			match(LPAREN);
			setState(1007);
			((SwitchExprContext)_localctx).cond = expr();
			setState(1008);
			match(RPAREN);
			setState(1010); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1009);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(1012); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1014);
			match(KW_DEFAULT);
			setState(1015);
			match(KW_RETURN);
			setState(1016);
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
		public TerminalNode KW_RETURN() { return getToken(XQueryParser.KW_RETURN, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public List<TerminalNode> KW_CASE() { return getTokens(XQueryParser.KW_CASE); }
		public TerminalNode KW_CASE(int i) {
			return getToken(XQueryParser.KW_CASE, i);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSwitchCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseClauseContext switchCaseClause() throws RecognitionException {
		SwitchCaseClauseContext _localctx = new SwitchCaseClauseContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1020); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1018);
				match(KW_CASE);
				setState(1019);
				((SwitchCaseClauseContext)_localctx).switchCaseOperand = switchCaseOperand();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).switchCaseOperand);
				}
				}
				setState(1022); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1024);
			match(KW_RETURN);
			setState(1025);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSwitchCaseOperand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseOperandContext switchCaseOperand() throws RecognitionException {
		SwitchCaseOperandContext _localctx = new SwitchCaseOperandContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_switchCaseOperand);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1027);
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
		public TerminalNode KW_TYPESWITCH() { return getToken(XQueryParser.KW_TYPESWITCH, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParser.KW_DEFAULT, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParser.KW_RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTypeswitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeswitchExprContext typeswitchExpr() throws RecognitionException {
		TypeswitchExprContext _localctx = new TypeswitchExprContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_typeswitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1029);
			match(KW_TYPESWITCH);
			setState(1030);
			match(LPAREN);
			setState(1031);
			((TypeswitchExprContext)_localctx).cond = expr();
			setState(1032);
			match(RPAREN);
			setState(1034); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1033);
				((TypeswitchExprContext)_localctx).caseClause = caseClause();
				((TypeswitchExprContext)_localctx).cses.add(((TypeswitchExprContext)_localctx).caseClause);
				}
				}
				setState(1036); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KW_CASE );
			setState(1038);
			match(KW_DEFAULT);
			setState(1041);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(1039);
				match(DOLLAR);
				setState(1040);
				((TypeswitchExprContext)_localctx).var_ref = varName();
				}
			}

			setState(1043);
			match(KW_RETURN);
			setState(1044);
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
		public TerminalNode KW_CASE() { return getToken(XQueryParser.KW_CASE, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParser.KW_RETURN, 0); }
		public SequenceUnionTypeContext sequenceUnionType() {
			return getRuleContext(SequenceUnionTypeContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public CaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseClauseContext caseClause() throws RecognitionException {
		CaseClauseContext _localctx = new CaseClauseContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1046);
			match(KW_CASE);
			setState(1051);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(1047);
				match(DOLLAR);
				setState(1048);
				((CaseClauseContext)_localctx).var_ref = varName();
				setState(1049);
				match(KW_AS);
				}
			}

			setState(1053);
			((CaseClauseContext)_localctx).union = sequenceUnionType();
			setState(1054);
			match(KW_RETURN);
			setState(1055);
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
		public List<TerminalNode> VBAR() { return getTokens(XQueryParser.VBAR); }
		public TerminalNode VBAR(int i) {
			return getToken(XQueryParser.VBAR, i);
		}
		public SequenceUnionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceUnionType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSequenceUnionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceUnionTypeContext sequenceUnionType() throws RecognitionException {
		SequenceUnionTypeContext _localctx = new SequenceUnionTypeContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_sequenceUnionType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1057);
			((SequenceUnionTypeContext)_localctx).sequenceType = sequenceType();
			((SequenceUnionTypeContext)_localctx).seq.add(((SequenceUnionTypeContext)_localctx).sequenceType);
			setState(1062);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VBAR) {
				{
				{
				setState(1058);
				match(VBAR);
				setState(1059);
				((SequenceUnionTypeContext)_localctx).sequenceType = sequenceType();
				((SequenceUnionTypeContext)_localctx).seq.add(((SequenceUnionTypeContext)_localctx).sequenceType);
				}
				}
				setState(1064);
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
		public TerminalNode KW_IF() { return getToken(XQueryParser.KW_IF, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public TerminalNode KW_THEN() { return getToken(XQueryParser.KW_THEN, 0); }
		public TerminalNode KW_ELSE() { return getToken(XQueryParser.KW_ELSE, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitIfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfExprContext ifExpr() throws RecognitionException {
		IfExprContext _localctx = new IfExprContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1065);
			match(KW_IF);
			setState(1066);
			match(LPAREN);
			setState(1067);
			((IfExprContext)_localctx).test_condition = expr();
			setState(1068);
			match(RPAREN);
			setState(1069);
			match(KW_THEN);
			setState(1070);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(1071);
			match(KW_ELSE);
			setState(1072);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTryCatchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryCatchExprContext tryCatchExpr() throws RecognitionException {
		TryCatchExprContext _localctx = new TryCatchExprContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_tryCatchExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1074);
			tryClause();
			setState(1076); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1075);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1078); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
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
		public TerminalNode KW_TRY() { return getToken(XQueryParser.KW_TRY, 0); }
		public EnclosedTryTargetExpressionContext enclosedTryTargetExpression() {
			return getRuleContext(EnclosedTryTargetExpressionContext.class,0);
		}
		public TryClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTryClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryClauseContext tryClause() throws RecognitionException {
		TryClauseContext _localctx = new TryClauseContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_tryClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1080);
			match(KW_TRY);
			setState(1081);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitEnclosedTryTargetExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedTryTargetExpressionContext enclosedTryTargetExpression() throws RecognitionException {
		EnclosedTryTargetExpressionContext _localctx = new EnclosedTryTargetExpressionContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_enclosedTryTargetExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1083);
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
		public TerminalNode KW_CATCH() { return getToken(XQueryParser.KW_CATCH, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public CatchErrorListContext catchErrorList() {
			return getRuleContext(CatchErrorListContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public VarNameContext varName() {
			return getRuleContext(VarNameContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCatchClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_catchClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1085);
			match(KW_CATCH);
			setState(1092);
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
				setState(1086);
				catchErrorList();
				}
				break;
			case LPAREN:
				{
				{
				setState(1087);
				match(LPAREN);
				setState(1088);
				match(DOLLAR);
				setState(1089);
				varName();
				setState(1090);
				match(RPAREN);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1094);
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
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public EnclosedExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enclosedExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitEnclosedExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedExpressionContext enclosedExpression() throws RecognitionException {
		EnclosedExpressionContext _localctx = new EnclosedExpressionContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_enclosedExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1096);
			match(LBRACE);
			setState(1098);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & -279372359199281L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -1L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 2377900603251621823L) != 0)) {
				{
				setState(1097);
				expr();
				}
			}

			setState(1100);
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
		public List<TerminalNode> VBAR() { return getTokens(XQueryParser.VBAR); }
		public TerminalNode VBAR(int i) {
			return getToken(XQueryParser.VBAR, i);
		}
		public CatchErrorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchErrorList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCatchErrorList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchErrorListContext catchErrorList() throws RecognitionException {
		CatchErrorListContext _localctx = new CatchErrorListContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_catchErrorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1102);
			((CatchErrorListContext)_localctx).nameTest = nameTest();
			((CatchErrorListContext)_localctx).errors.add(((CatchErrorListContext)_localctx).nameTest);
			setState(1107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VBAR) {
				{
				{
				setState(1103);
				match(VBAR);
				setState(1104);
				((CatchErrorListContext)_localctx).nameTest = nameTest();
				((CatchErrorListContext)_localctx).errors.add(((CatchErrorListContext)_localctx).nameTest);
				}
				}
				setState(1109);
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
		public TerminalNode KW_UPDATE() { return getToken(XQueryParser.KW_UPDATE, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExistUpdateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistUpdateExprContext existUpdateExpr() throws RecognitionException {
		ExistUpdateExprContext _localctx = new ExistUpdateExprContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_existUpdateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1110);
			match(KW_UPDATE);
			setState(1116);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_REPLACE:
				{
				setState(1111);
				existReplaceExpr();
				}
				break;
			case KW_VALUE:
				{
				setState(1112);
				existValueExpr();
				}
				break;
			case KW_INSERT:
				{
				setState(1113);
				existInsertExpr();
				}
				break;
			case KW_DELETE:
				{
				setState(1114);
				existDeleteExpr();
				}
				break;
			case KW_RENAME:
				{
				setState(1115);
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
		public TerminalNode KW_REPLACE() { return getToken(XQueryParser.KW_REPLACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode KW_WITH() { return getToken(XQueryParser.KW_WITH, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ExistReplaceExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existReplaceExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExistReplaceExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistReplaceExprContext existReplaceExpr() throws RecognitionException {
		ExistReplaceExprContext _localctx = new ExistReplaceExprContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_existReplaceExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1118);
			match(KW_REPLACE);
			setState(1119);
			expr();
			setState(1120);
			match(KW_WITH);
			setState(1121);
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
		public TerminalNode KW_VALUE() { return getToken(XQueryParser.KW_VALUE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode KW_WITH() { return getToken(XQueryParser.KW_WITH, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ExistValueExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existValueExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExistValueExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistValueExprContext existValueExpr() throws RecognitionException {
		ExistValueExprContext _localctx = new ExistValueExprContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_existValueExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1123);
			match(KW_VALUE);
			setState(1124);
			expr();
			setState(1125);
			match(KW_WITH);
			setState(1126);
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
		public TerminalNode KW_INSERT() { return getToken(XQueryParser.KW_INSERT, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public TerminalNode KW_INTO() { return getToken(XQueryParser.KW_INTO, 0); }
		public TerminalNode KW_PRECEDING() { return getToken(XQueryParser.KW_PRECEDING, 0); }
		public TerminalNode KW_FOLLOWING() { return getToken(XQueryParser.KW_FOLLOWING, 0); }
		public ExistInsertExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existInsertExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExistInsertExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistInsertExprContext existInsertExpr() throws RecognitionException {
		ExistInsertExprContext _localctx = new ExistInsertExprContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_existInsertExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1128);
			match(KW_INSERT);
			setState(1129);
			exprSingle();
			setState(1130);
			_la = _input.LA(1);
			if ( !(_la==KW_FOLLOWING || _la==KW_PRECEDING || _la==KW_INTO) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1131);
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
		public TerminalNode KW_DELETE() { return getToken(XQueryParser.KW_DELETE, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ExistDeleteExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existDeleteExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExistDeleteExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistDeleteExprContext existDeleteExpr() throws RecognitionException {
		ExistDeleteExprContext _localctx = new ExistDeleteExprContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_existDeleteExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1133);
			match(KW_DELETE);
			setState(1134);
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
		public TerminalNode KW_RENAME() { return getToken(XQueryParser.KW_RENAME, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public ExistRenameExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_existRenameExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExistRenameExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExistRenameExprContext existRenameExpr() throws RecognitionException {
		ExistRenameExprContext _localctx = new ExistRenameExprContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_existRenameExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1136);
			match(KW_RENAME);
			setState(1137);
			exprSingle();
			setState(1138);
			match(KW_AS);
			setState(1139);
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
		public List<TerminalNode> KW_OR() { return getTokens(XQueryParser.KW_OR); }
		public TerminalNode KW_OR(int i) {
			return getToken(XQueryParser.KW_OR, i);
		}
		public OrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrExprContext orExpr() throws RecognitionException {
		OrExprContext _localctx = new OrExprContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1141);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(1146);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1142);
					match(KW_OR);
					setState(1143);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(1148);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
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
		public List<TerminalNode> KW_AND() { return getTokens(XQueryParser.KW_AND); }
		public TerminalNode KW_AND(int i) {
			return getToken(XQueryParser.KW_AND, i);
		}
		public AndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			((AndExprContext)_localctx).main_expr = comparisonExpr();
			setState(1154);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1150);
					match(KW_AND);
					setState(1151);
					((AndExprContext)_localctx).comparisonExpr = comparisonExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).comparisonExpr);
					}
					} 
				}
				setState(1156);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitComparisonExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonExprContext comparisonExpr() throws RecognitionException {
		ComparisonExprContext _localctx = new ComparisonExprContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_comparisonExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1157);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(1165);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				{
				setState(1161);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
				case 1:
					{
					setState(1158);
					valueComp();
					}
					break;
				case 2:
					{
					setState(1159);
					generalComp();
					}
					break;
				case 3:
					{
					setState(1160);
					nodeComp();
					}
					break;
				}
				setState(1163);
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
		public List<TerminalNode> CONCATENATION() { return getTokens(XQueryParser.CONCATENATION); }
		public TerminalNode CONCATENATION(int i) {
			return getToken(XQueryParser.CONCATENATION, i);
		}
		public StringConcatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConcatExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringConcatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConcatExprContext stringConcatExpr() throws RecognitionException {
		StringConcatExprContext _localctx = new StringConcatExprContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1167);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(1172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CONCATENATION) {
				{
				{
				setState(1168);
				match(CONCATENATION);
				setState(1169);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(1174);
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
		public TerminalNode KW_TO() { return getToken(XQueryParser.KW_TO, 0); }
		public RangeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitRangeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeExprContext rangeExpr() throws RecognitionException {
		RangeExprContext _localctx = new RangeExprContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1175);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(1178);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				{
				setState(1176);
				match(KW_TO);
				setState(1177);
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
		public Token _tset1812;
		public MultiplicativeExprContext multiplicativeExpr;
		public List<MultiplicativeExprContext> rhs = new ArrayList<MultiplicativeExprContext>();
		public List<MultiplicativeExprContext> multiplicativeExpr() {
			return getRuleContexts(MultiplicativeExprContext.class);
		}
		public MultiplicativeExprContext multiplicativeExpr(int i) {
			return getRuleContext(MultiplicativeExprContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(XQueryParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(XQueryParser.PLUS, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(XQueryParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(XQueryParser.MINUS, i);
		}
		public AdditiveExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAdditiveExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExprContext additiveExpr() throws RecognitionException {
		AdditiveExprContext _localctx = new AdditiveExprContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1180);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(1185);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1181);
					((AdditiveExprContext)_localctx)._tset1812 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==PLUS || _la==MINUS) ) {
						((AdditiveExprContext)_localctx)._tset1812 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1812);
					setState(1182);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(1187);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
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
		public Token _tset1840;
		public UnionExprContext unionExpr;
		public List<UnionExprContext> rhs = new ArrayList<UnionExprContext>();
		public List<UnionExprContext> unionExpr() {
			return getRuleContexts(UnionExprContext.class);
		}
		public UnionExprContext unionExpr(int i) {
			return getRuleContext(UnionExprContext.class,i);
		}
		public List<TerminalNode> STAR() { return getTokens(XQueryParser.STAR); }
		public TerminalNode STAR(int i) {
			return getToken(XQueryParser.STAR, i);
		}
		public List<TerminalNode> KW_DIV() { return getTokens(XQueryParser.KW_DIV); }
		public TerminalNode KW_DIV(int i) {
			return getToken(XQueryParser.KW_DIV, i);
		}
		public List<TerminalNode> KW_IDIV() { return getTokens(XQueryParser.KW_IDIV); }
		public TerminalNode KW_IDIV(int i) {
			return getToken(XQueryParser.KW_IDIV, i);
		}
		public List<TerminalNode> KW_MOD() { return getTokens(XQueryParser.KW_MOD); }
		public TerminalNode KW_MOD(int i) {
			return getToken(XQueryParser.KW_MOD, i);
		}
		public MultiplicativeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMultiplicativeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExprContext multiplicativeExpr() throws RecognitionException {
		MultiplicativeExprContext _localctx = new MultiplicativeExprContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_multiplicativeExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1188);
			((MultiplicativeExprContext)_localctx).main_expr = unionExpr();
			setState(1193);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1189);
					((MultiplicativeExprContext)_localctx)._tset1840 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==STAR || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & 68721573889L) != 0)) ) {
						((MultiplicativeExprContext)_localctx)._tset1840 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1840);
					setState(1190);
					((MultiplicativeExprContext)_localctx).unionExpr = unionExpr();
					((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).unionExpr);
					}
					} 
				}
				setState(1195);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
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
		public List<TerminalNode> KW_UNION() { return getTokens(XQueryParser.KW_UNION); }
		public TerminalNode KW_UNION(int i) {
			return getToken(XQueryParser.KW_UNION, i);
		}
		public List<TerminalNode> VBAR() { return getTokens(XQueryParser.VBAR); }
		public TerminalNode VBAR(int i) {
			return getToken(XQueryParser.VBAR, i);
		}
		public UnionExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitUnionExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionExprContext unionExpr() throws RecognitionException {
		UnionExprContext _localctx = new UnionExprContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_unionExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1196);
			((UnionExprContext)_localctx).main_expr = intersectExceptExpr();
			setState(1201);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1197);
					_la = _input.LA(1);
					if ( !(_la==VBAR || _la==KW_UNION) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1198);
					((UnionExprContext)_localctx).intersectExceptExpr = intersectExceptExpr();
					((UnionExprContext)_localctx).rhs.add(((UnionExprContext)_localctx).intersectExceptExpr);
					}
					} 
				}
				setState(1203);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
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
		public List<TerminalNode> KW_INTERSECT() { return getTokens(XQueryParser.KW_INTERSECT); }
		public TerminalNode KW_INTERSECT(int i) {
			return getToken(XQueryParser.KW_INTERSECT, i);
		}
		public List<TerminalNode> KW_EXCEPT() { return getTokens(XQueryParser.KW_EXCEPT); }
		public TerminalNode KW_EXCEPT(int i) {
			return getToken(XQueryParser.KW_EXCEPT, i);
		}
		public IntersectExceptExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intersectExceptExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitIntersectExceptExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntersectExceptExprContext intersectExceptExpr() throws RecognitionException {
		IntersectExceptExprContext _localctx = new IntersectExceptExprContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_intersectExceptExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1204);
			((IntersectExceptExprContext)_localctx).main_expr = instanceOfExpr();
			setState(1209);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,87,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1205);
					_la = _input.LA(1);
					if ( !(_la==KW_EXCEPT || _la==KW_INTERSECT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1206);
					((IntersectExceptExprContext)_localctx).instanceOfExpr = instanceOfExpr();
					((IntersectExceptExprContext)_localctx).rhs.add(((IntersectExceptExprContext)_localctx).instanceOfExpr);
					}
					} 
				}
				setState(1211);
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
	public static class InstanceOfExprContext extends ParserRuleContext {
		public TreatExprContext main_expr;
		public SequenceTypeContext seq;
		public TreatExprContext treatExpr() {
			return getRuleContext(TreatExprContext.class,0);
		}
		public TerminalNode KW_INSTANCE() { return getToken(XQueryParser.KW_INSTANCE, 0); }
		public TerminalNode KW_OF() { return getToken(XQueryParser.KW_OF, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public InstanceOfExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instanceOfExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitInstanceOfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstanceOfExprContext instanceOfExpr() throws RecognitionException {
		InstanceOfExprContext _localctx = new InstanceOfExprContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1212);
			((InstanceOfExprContext)_localctx).main_expr = treatExpr();
			setState(1216);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(1213);
				match(KW_INSTANCE);
				setState(1214);
				match(KW_OF);
				setState(1215);
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
		public TerminalNode KW_TREAT() { return getToken(XQueryParser.KW_TREAT, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TreatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_treatExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTreatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TreatExprContext treatExpr() throws RecognitionException {
		TreatExprContext _localctx = new TreatExprContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1218);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(1222);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				{
				setState(1219);
				match(KW_TREAT);
				setState(1220);
				match(KW_AS);
				setState(1221);
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
		public TerminalNode KW_CASTABLE() { return getToken(XQueryParser.KW_CASTABLE, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public SingleTypeContext singleType() {
			return getRuleContext(SingleTypeContext.class,0);
		}
		public CastableExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castableExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCastableExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastableExprContext castableExpr() throws RecognitionException {
		CastableExprContext _localctx = new CastableExprContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1224);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(1228);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(1225);
				match(KW_CASTABLE);
				setState(1226);
				match(KW_AS);
				setState(1227);
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
		public TerminalNode KW_CAST() { return getToken(XQueryParser.KW_CAST, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public SingleTypeContext singleType() {
			return getRuleContext(SingleTypeContext.class,0);
		}
		public CastExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCastExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastExprContext castExpr() throws RecognitionException {
		CastExprContext _localctx = new CastExprContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1230);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(1234);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				{
				setState(1231);
				match(KW_CAST);
				setState(1232);
				match(KW_AS);
				setState(1233);
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
		public List<TerminalNode> ARROW() { return getTokens(XQueryParser.ARROW); }
		public TerminalNode ARROW(int i) {
			return getToken(XQueryParser.ARROW, i);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitArrowExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowExprContext arrowExpr() throws RecognitionException {
		ArrowExprContext _localctx = new ArrowExprContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1236);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(1241);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1237);
					match(ARROW);
					setState(1238);
					((ArrowExprContext)_localctx).complexArrow = complexArrow();
					((ArrowExprContext)_localctx).function_call_expr.add(((ArrowExprContext)_localctx).complexArrow);
					}
					} 
				}
				setState(1243);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitComplexArrow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplexArrowContext complexArrow() throws RecognitionException {
		ComplexArrowContext _localctx = new ComplexArrowContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_complexArrow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1244);
			arrowFunctionSpecifier();
			setState(1245);
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
		public Token _tset2030;
		public ValueExprContext main_expr;
		public ValueExprContext valueExpr() {
			return getRuleContext(ValueExprContext.class,0);
		}
		public List<TerminalNode> MINUS() { return getTokens(XQueryParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(XQueryParser.MINUS, i);
		}
		public List<TerminalNode> PLUS() { return getTokens(XQueryParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(XQueryParser.PLUS, i);
		}
		public UnaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExprContext unaryExpr() throws RecognitionException {
		UnaryExprContext _localctx = new UnaryExprContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1250);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PLUS || _la==MINUS) {
				{
				{
				setState(1247);
				((UnaryExprContext)_localctx)._tset2030 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
					((UnaryExprContext)_localctx)._tset2030 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset2030);
				}
				}
				setState(1252);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1253);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitValueExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueExprContext valueExpr() throws RecognitionException {
		ValueExprContext _localctx = new ValueExprContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_valueExpr);
		try {
			setState(1258);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1255);
				validateExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1256);
				extensionExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1257);
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
		public TerminalNode EQUAL() { return getToken(XQueryParser.EQUAL, 0); }
		public TerminalNode NOT_EQUAL() { return getToken(XQueryParser.NOT_EQUAL, 0); }
		public TerminalNode LANGLE() { return getToken(XQueryParser.LANGLE, 0); }
		public TerminalNode RANGLE() { return getToken(XQueryParser.RANGLE, 0); }
		public GeneralCompContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_generalComp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitGeneralComp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GeneralCompContext generalComp() throws RecognitionException {
		GeneralCompContext _localctx = new GeneralCompContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_generalComp);
		try {
			setState(1268);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1260);
				match(EQUAL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1261);
				match(NOT_EQUAL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1262);
				match(LANGLE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				{
				setState(1263);
				match(LANGLE);
				setState(1264);
				match(EQUAL);
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1265);
				match(RANGLE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				{
				setState(1266);
				match(RANGLE);
				setState(1267);
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
		public TerminalNode KW_EQ() { return getToken(XQueryParser.KW_EQ, 0); }
		public TerminalNode KW_NE() { return getToken(XQueryParser.KW_NE, 0); }
		public TerminalNode KW_LT() { return getToken(XQueryParser.KW_LT, 0); }
		public TerminalNode KW_LE() { return getToken(XQueryParser.KW_LE, 0); }
		public TerminalNode KW_GT() { return getToken(XQueryParser.KW_GT, 0); }
		public TerminalNode KW_GE() { return getToken(XQueryParser.KW_GE, 0); }
		public ValueCompContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueComp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitValueComp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueCompContext valueComp() throws RecognitionException {
		ValueCompContext _localctx = new ValueCompContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_valueComp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1270);
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
		public TerminalNode KW_IS() { return getToken(XQueryParser.KW_IS, 0); }
		public List<TerminalNode> LANGLE() { return getTokens(XQueryParser.LANGLE); }
		public TerminalNode LANGLE(int i) {
			return getToken(XQueryParser.LANGLE, i);
		}
		public List<TerminalNode> RANGLE() { return getTokens(XQueryParser.RANGLE); }
		public TerminalNode RANGLE(int i) {
			return getToken(XQueryParser.RANGLE, i);
		}
		public NodeCompContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nodeComp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNodeComp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeCompContext nodeComp() throws RecognitionException {
		NodeCompContext _localctx = new NodeCompContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_nodeComp);
		try {
			setState(1277);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_IS:
				enterOuterAlt(_localctx, 1);
				{
				setState(1272);
				match(KW_IS);
				}
				break;
			case LANGLE:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1273);
				match(LANGLE);
				setState(1274);
				match(LANGLE);
				}
				}
				break;
			case RANGLE:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(1275);
				match(RANGLE);
				setState(1276);
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
		public TerminalNode KW_VALIDATE() { return getToken(XQueryParser.KW_VALIDATE, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public ValidationModeContext validationMode() {
			return getRuleContext(ValidationModeContext.class,0);
		}
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode KW_TYPE() { return getToken(XQueryParser.KW_TYPE, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public ValidateExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validateExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitValidateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidateExprContext validateExpr() throws RecognitionException {
		ValidateExprContext _localctx = new ValidateExprContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_validateExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1279);
			match(KW_VALIDATE);
			setState(1283);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_LAX:
			case KW_STRICT:
				{
				setState(1280);
				validationMode();
				}
				break;
			case KW_AS:
			case KW_TYPE:
				{
				{
				setState(1281);
				_la = _input.LA(1);
				if ( !(_la==KW_AS || _la==KW_TYPE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1282);
				typeName();
				}
				}
				break;
			case LBRACE:
				break;
			default:
				break;
			}
			setState(1285);
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
		public TerminalNode KW_LAX() { return getToken(XQueryParser.KW_LAX, 0); }
		public TerminalNode KW_STRICT() { return getToken(XQueryParser.KW_STRICT, 0); }
		public ValidationModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validationMode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitValidationMode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidationModeContext validationMode() throws RecognitionException {
		ValidationModeContext _localctx = new ValidationModeContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_validationMode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1287);
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
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public List<TerminalNode> PRAGMA() { return getTokens(XQueryParser.PRAGMA); }
		public TerminalNode PRAGMA(int i) {
			return getToken(XQueryParser.PRAGMA, i);
		}
		public ExtensionExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extensionExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitExtensionExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtensionExprContext extensionExpr() throws RecognitionException {
		ExtensionExprContext _localctx = new ExtensionExprContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_extensionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1290); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1289);
				match(PRAGMA);
				}
				}
				setState(1292); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==PRAGMA );
			setState(1294);
			match(LBRACE);
			setState(1295);
			expr();
			setState(1296);
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
		public List<TerminalNode> BANG() { return getTokens(XQueryParser.BANG); }
		public TerminalNode BANG(int i) {
			return getToken(XQueryParser.BANG, i);
		}
		public SimpleMapExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleMapExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSimpleMapExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleMapExprContext simpleMapExpr() throws RecognitionException {
		SimpleMapExprContext _localctx = new SimpleMapExprContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_simpleMapExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1298);
			((SimpleMapExprContext)_localctx).main_expr = pathExpr();
			setState(1303);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,99,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1299);
					match(BANG);
					setState(1300);
					((SimpleMapExprContext)_localctx).pathExpr = pathExpr();
					((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).pathExpr);
					}
					} 
				}
				setState(1305);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,99,_ctx);
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
	public static class PathExprContext extends ParserRuleContext {
		public RelativePathExprContext singleslash;
		public RelativePathExprContext doubleslash;
		public RelativePathExprContext relative;
		public TerminalNode SLASH() { return getToken(XQueryParser.SLASH, 0); }
		public RelativePathExprContext relativePathExpr() {
			return getRuleContext(RelativePathExprContext.class,0);
		}
		public TerminalNode DSLASH() { return getToken(XQueryParser.DSLASH, 0); }
		public PathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPathExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathExprContext pathExpr() throws RecognitionException {
		PathExprContext _localctx = new PathExprContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_pathExpr);
		try {
			setState(1313);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SLASH:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1306);
				match(SLASH);
				setState(1308);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
				case 1:
					{
					setState(1307);
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
				setState(1310);
				match(DSLASH);
				setState(1311);
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
				setState(1312);
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
		public List<TerminalNode> SLASH() { return getTokens(XQueryParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(XQueryParser.SLASH, i);
		}
		public List<TerminalNode> DSLASH() { return getTokens(XQueryParser.DSLASH); }
		public TerminalNode DSLASH(int i) {
			return getToken(XQueryParser.DSLASH, i);
		}
		public RelativePathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relativePathExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitRelativePathExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelativePathExprContext relativePathExpr() throws RecognitionException {
		RelativePathExprContext _localctx = new RelativePathExprContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_relativePathExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1315);
			stepExpr();
			setState(1320);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1316);
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
					setState(1317);
					stepExpr();
					}
					} 
				}
				setState(1322);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStepExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StepExprContext stepExpr() throws RecognitionException {
		StepExprContext _localctx = new StepExprContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_stepExpr);
		try {
			setState(1325);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1323);
				postfixExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1324);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAxisStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AxisStepContext axisStep() throws RecognitionException {
		AxisStepContext _localctx = new AxisStepContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_axisStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1329);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				{
				setState(1327);
				reverseStep();
				}
				break;
			case 2:
				{
				setState(1328);
				forwardStep();
				}
				break;
			}
			setState(1331);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitForwardStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForwardStepContext forwardStep() throws RecognitionException {
		ForwardStepContext _localctx = new ForwardStepContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_forwardStep);
		try {
			setState(1337);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1333);
				forwardAxis();
				setState(1334);
				nodeTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1336);
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
		public List<TerminalNode> COLON() { return getTokens(XQueryParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(XQueryParser.COLON, i);
		}
		public TerminalNode KW_CHILD() { return getToken(XQueryParser.KW_CHILD, 0); }
		public TerminalNode KW_DESCENDANT() { return getToken(XQueryParser.KW_DESCENDANT, 0); }
		public TerminalNode KW_ATTRIBUTE() { return getToken(XQueryParser.KW_ATTRIBUTE, 0); }
		public TerminalNode KW_SELF() { return getToken(XQueryParser.KW_SELF, 0); }
		public TerminalNode KW_DESCENDANT_OR_SELF() { return getToken(XQueryParser.KW_DESCENDANT_OR_SELF, 0); }
		public TerminalNode KW_FOLLOWING_SIBLING() { return getToken(XQueryParser.KW_FOLLOWING_SIBLING, 0); }
		public TerminalNode KW_FOLLOWING() { return getToken(XQueryParser.KW_FOLLOWING, 0); }
		public ForwardAxisContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forwardAxis; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitForwardAxis(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForwardAxisContext forwardAxis() throws RecognitionException {
		ForwardAxisContext _localctx = new ForwardAxisContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_forwardAxis);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1339);
			_la = _input.LA(1);
			if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & 103080002049L) != 0) || _la==KW_SELF) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1340);
			match(COLON);
			setState(1341);
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
		public TerminalNode AT() { return getToken(XQueryParser.AT, 0); }
		public AbbrevForwardStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abbrevForwardStep; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAbbrevForwardStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbbrevForwardStepContext abbrevForwardStep() throws RecognitionException {
		AbbrevForwardStepContext _localctx = new AbbrevForwardStepContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_abbrevForwardStep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1344);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AT) {
				{
				setState(1343);
				match(AT);
				}
			}

			setState(1346);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitReverseStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReverseStepContext reverseStep() throws RecognitionException {
		ReverseStepContext _localctx = new ReverseStepContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_reverseStep);
		try {
			setState(1352);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ANCESTOR:
			case KW_ANCESTOR_OR_SELF:
			case KW_PARENT:
			case KW_PRECEDING:
			case KW_PRECEDING_SIBLING:
				enterOuterAlt(_localctx, 1);
				{
				setState(1348);
				reverseAxis();
				setState(1349);
				nodeTest();
				}
				break;
			case DDOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1351);
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
		public List<TerminalNode> COLON() { return getTokens(XQueryParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(XQueryParser.COLON, i);
		}
		public TerminalNode KW_PARENT() { return getToken(XQueryParser.KW_PARENT, 0); }
		public TerminalNode KW_ANCESTOR() { return getToken(XQueryParser.KW_ANCESTOR, 0); }
		public TerminalNode KW_PRECEDING_SIBLING() { return getToken(XQueryParser.KW_PRECEDING_SIBLING, 0); }
		public TerminalNode KW_PRECEDING() { return getToken(XQueryParser.KW_PRECEDING, 0); }
		public TerminalNode KW_ANCESTOR_OR_SELF() { return getToken(XQueryParser.KW_ANCESTOR_OR_SELF, 0); }
		public ReverseAxisContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reverseAxis; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitReverseAxis(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReverseAxisContext reverseAxis() throws RecognitionException {
		ReverseAxisContext _localctx = new ReverseAxisContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_reverseAxis);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1354);
			_la = _input.LA(1);
			if ( !(_la==KW_ANCESTOR || _la==KW_ANCESTOR_OR_SELF || ((((_la - 135)) & ~0x3f) == 0 && ((1L << (_la - 135)) & 7L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1355);
			match(COLON);
			setState(1356);
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
		public TerminalNode DDOT() { return getToken(XQueryParser.DDOT, 0); }
		public AbbrevReverseStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abbrevReverseStep; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAbbrevReverseStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbbrevReverseStepContext abbrevReverseStep() throws RecognitionException {
		AbbrevReverseStepContext _localctx = new AbbrevReverseStepContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_abbrevReverseStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1358);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeTestContext nodeTest() throws RecognitionException {
		NodeTestContext _localctx = new NodeTestContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_nodeTest);
		try {
			setState(1362);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1360);
				nameTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1361);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNameTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameTestContext nameTest() throws RecognitionException {
		NameTestContext _localctx = new NameTestContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_nameTest);
		try {
			setState(1366);
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
				setState(1364);
				eqName();
				}
				break;
			case STAR:
			case NCNameWithLocalWildcard:
			case NCNameWithPrefixWildcard:
				enterOuterAlt(_localctx, 2);
				{
				setState(1365);
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
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public AllNamesContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAllNames(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AllWithLocalContext extends WildcardContext {
		public TerminalNode NCNameWithPrefixWildcard() { return getToken(XQueryParser.NCNameWithPrefixWildcard, 0); }
		public AllWithLocalContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAllWithLocal(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AllWithNSContext extends WildcardContext {
		public TerminalNode NCNameWithLocalWildcard() { return getToken(XQueryParser.NCNameWithLocalWildcard, 0); }
		public AllWithNSContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAllWithNS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WildcardContext wildcard() throws RecognitionException {
		WildcardContext _localctx = new WildcardContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_wildcard);
		try {
			setState(1371);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STAR:
				_localctx = new AllNamesContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1368);
				match(STAR);
				}
				break;
			case NCNameWithLocalWildcard:
				_localctx = new AllWithNSContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1369);
				match(NCNameWithLocalWildcard);
				}
				break;
			case NCNameWithPrefixWildcard:
				_localctx = new AllWithLocalContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1370);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPostfixExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostfixExprContext postfixExpr() throws RecognitionException {
		PostfixExprContext _localctx = new PostfixExprContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_postfixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1373);
			((PostfixExprContext)_localctx).main_expr = primaryExpr();
			setState(1379);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,112,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1377);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LBRACKET:
						{
						setState(1374);
						predicate();
						}
						break;
					case LPAREN:
						{
						setState(1375);
						argumentList();
						}
						break;
					case QUESTION:
						{
						setState(1376);
						lookup();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(1381);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,112,_ctx);
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
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1382);
			match(LPAREN);
			setState(1391);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & -279372359199281L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -1L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 2377900603251621823L) != 0)) {
				{
				setState(1383);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(1388);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1384);
					match(COMMA);
					setState(1385);
					((ArgumentListContext)_localctx).argument = argument();
					((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
					}
					}
					setState(1390);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1393);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPredicateList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateListContext predicateList() throws RecognitionException {
		PredicateListContext _localctx = new PredicateListContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_predicateList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1398);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,115,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1395);
					predicate();
					}
					} 
				}
				setState(1400);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,115,_ctx);
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
		public TerminalNode LBRACKET() { return getToken(XQueryParser.LBRACKET, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(XQueryParser.RBRACKET, 0); }
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1401);
			match(LBRACKET);
			setState(1402);
			expr();
			setState(1403);
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
		public TerminalNode QUESTION() { return getToken(XQueryParser.QUESTION, 0); }
		public KeySpecifierContext keySpecifier() {
			return getRuleContext(KeySpecifierContext.class,0);
		}
		public LookupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lookup; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LookupContext lookup() throws RecognitionException {
		LookupContext _localctx = new LookupContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_lookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1405);
			match(QUESTION);
			setState(1406);
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
		public TerminalNode IntegerLiteral() { return getToken(XQueryParser.IntegerLiteral, 0); }
		public ParenthesizedExprContext parenthesizedExpr() {
			return getRuleContext(ParenthesizedExprContext.class,0);
		}
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public KeySpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keySpecifier; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitKeySpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeySpecifierContext keySpecifier() throws RecognitionException {
		KeySpecifierContext _localctx = new KeySpecifierContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_keySpecifier);
		try {
			setState(1412);
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
				setState(1408);
				ncName();
				}
				break;
			case IntegerLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(1409);
				match(IntegerLiteral);
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 3);
				{
				setState(1410);
				parenthesizedExpr();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(1411);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitArrowFunctionSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowFunctionSpecifierContext arrowFunctionSpecifier() throws RecognitionException {
		ArrowFunctionSpecifierContext _localctx = new ArrowFunctionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_arrowFunctionSpecifier);
		try {
			setState(1417);
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
				setState(1414);
				eqName();
				}
				break;
			case DOLLAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(1415);
				varRef();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 3);
				{
				setState(1416);
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
		public PrimaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExprContext primaryExpr() throws RecognitionException {
		PrimaryExprContext _localctx = new PrimaryExprContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_primaryExpr);
		try {
			setState(1432);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,118,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1419);
				literal();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1420);
				varRef();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1421);
				parenthesizedExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1422);
				contextItemExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1423);
				functionCall();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1424);
				orderedExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1425);
				unorderedExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1426);
				nodeConstructor();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1427);
				functionItemExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1428);
				mapConstructor();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1429);
				arrayConstructor();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1430);
				stringConstructor();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1431);
				unaryLookup();
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_literal);
		try {
			setState(1436);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case DecimalLiteral:
			case DoubleLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(1434);
				numericLiteral();
				}
				break;
			case Quot:
			case Apos:
				enterOuterAlt(_localctx, 2);
				{
				setState(1435);
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
		public TerminalNode IntegerLiteral() { return getToken(XQueryParser.IntegerLiteral, 0); }
		public TerminalNode DecimalLiteral() { return getToken(XQueryParser.DecimalLiteral, 0); }
		public TerminalNode DoubleLiteral() { return getToken(XQueryParser.DoubleLiteral, 0); }
		public NumericLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNumericLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericLiteralContext numericLiteral() throws RecognitionException {
		NumericLiteralContext _localctx = new NumericLiteralContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_numericLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1438);
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
		public TerminalNode DOLLAR() { return getToken(XQueryParser.DOLLAR, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public VarRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varRef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitVarRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarRefContext varRef() throws RecognitionException {
		VarRefContext _localctx = new VarRefContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1440);
			match(DOLLAR);
			setState(1441);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitVarName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarNameContext varName() throws RecognitionException {
		VarNameContext _localctx = new VarNameContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_varName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1443);
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
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParenthesizedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenthesizedExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitParenthesizedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenthesizedExprContext parenthesizedExpr() throws RecognitionException {
		ParenthesizedExprContext _localctx = new ParenthesizedExprContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1445);
			match(LPAREN);
			setState(1447);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & -279372359199281L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -1L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 2377900603251621823L) != 0)) {
				{
				setState(1446);
				expr();
				}
			}

			setState(1449);
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
		public TerminalNode DOT() { return getToken(XQueryParser.DOT, 0); }
		public ContextItemExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextItemExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitContextItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemExprContext contextItemExpr() throws RecognitionException {
		ContextItemExprContext _localctx = new ContextItemExprContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1451);
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
		public TerminalNode KW_ORDERED() { return getToken(XQueryParser.KW_ORDERED, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public OrderedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderedExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitOrderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderedExprContext orderedExpr() throws RecognitionException {
		OrderedExprContext _localctx = new OrderedExprContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1453);
			match(KW_ORDERED);
			setState(1454);
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
		public TerminalNode KW_UNORDERED() { return getToken(XQueryParser.KW_UNORDERED, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public UnorderedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unorderedExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitUnorderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnorderedExprContext unorderedExpr() throws RecognitionException {
		UnorderedExprContext _localctx = new UnorderedExprContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1456);
			match(KW_UNORDERED);
			setState(1457);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1459);
			((FunctionCallContext)_localctx).fn_name = eqName();
			setState(1460);
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
		public TerminalNode QUESTION() { return getToken(XQueryParser.QUESTION, 0); }
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_argument);
		try {
			setState(1464);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1462);
				exprSingle();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1463);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNodeConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeConstructorContext nodeConstructor() throws RecognitionException {
		NodeConstructorContext _localctx = new NodeConstructorContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_nodeConstructor);
		try {
			setState(1468);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMMENT:
			case PI:
			case LANGLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1466);
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
				setState(1467);
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
		public TerminalNode COMMENT() { return getToken(XQueryParser.COMMENT, 0); }
		public TerminalNode PI() { return getToken(XQueryParser.PI, 0); }
		public DirectConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirectConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectConstructorContext directConstructor() throws RecognitionException {
		DirectConstructorContext _localctx = new DirectConstructorContext(_ctx, getState());
		enterRule(_localctx, 280, RULE_directConstructor);
		int _la;
		try {
			setState(1473);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1470);
				dirElemConstructorOpenClose();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1471);
				dirElemConstructorSingleTag();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1472);
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
		public List<TerminalNode> LANGLE() { return getTokens(XQueryParser.LANGLE); }
		public TerminalNode LANGLE(int i) {
			return getToken(XQueryParser.LANGLE, i);
		}
		public DirAttributeListContext dirAttributeList() {
			return getRuleContext(DirAttributeListContext.class,0);
		}
		public List<TerminalNode> RANGLE() { return getTokens(XQueryParser.RANGLE); }
		public TerminalNode RANGLE(int i) {
			return getToken(XQueryParser.RANGLE, i);
		}
		public List<QNameContext> qName() {
			return getRuleContexts(QNameContext.class);
		}
		public QNameContext qName(int i) {
			return getRuleContext(QNameContext.class,i);
		}
		public TerminalNode SLASH() { return getToken(XQueryParser.SLASH, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirElemConstructorOpenClose(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirElemConstructorOpenCloseContext dirElemConstructorOpenClose() throws RecognitionException {
		DirElemConstructorOpenCloseContext _localctx = new DirElemConstructorOpenCloseContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_dirElemConstructorOpenClose);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1475);
			match(LANGLE);
			setState(1476);
			((DirElemConstructorOpenCloseContext)_localctx).openName = qName();
			setState(1477);
			dirAttributeList();
			setState(1478);
			((DirElemConstructorOpenCloseContext)_localctx).endOpen = match(RANGLE);
			setState(1482);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,124,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1479);
					dirElemContent();
					}
					} 
				}
				setState(1484);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,124,_ctx);
			}
			setState(1485);
			((DirElemConstructorOpenCloseContext)_localctx).startClose = match(LANGLE);
			setState(1486);
			((DirElemConstructorOpenCloseContext)_localctx).slashClose = match(SLASH);
			setState(1487);
			((DirElemConstructorOpenCloseContext)_localctx).closeName = qName();
			setState(1488);
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
		public TerminalNode LANGLE() { return getToken(XQueryParser.LANGLE, 0); }
		public DirAttributeListContext dirAttributeList() {
			return getRuleContext(DirAttributeListContext.class,0);
		}
		public TerminalNode RANGLE() { return getToken(XQueryParser.RANGLE, 0); }
		public QNameContext qName() {
			return getRuleContext(QNameContext.class,0);
		}
		public TerminalNode SLASH() { return getToken(XQueryParser.SLASH, 0); }
		public DirElemConstructorSingleTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirElemConstructorSingleTag; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirElemConstructorSingleTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirElemConstructorSingleTagContext dirElemConstructorSingleTag() throws RecognitionException {
		DirElemConstructorSingleTagContext _localctx = new DirElemConstructorSingleTagContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_dirElemConstructorSingleTag);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1490);
			match(LANGLE);
			setState(1491);
			((DirElemConstructorSingleTagContext)_localctx).openName = qName();
			setState(1492);
			dirAttributeList();
			setState(1493);
			((DirElemConstructorSingleTagContext)_localctx).slashClose = match(SLASH);
			setState(1494);
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
		public List<TerminalNode> EQUAL() { return getTokens(XQueryParser.EQUAL); }
		public TerminalNode EQUAL(int i) {
			return getToken(XQueryParser.EQUAL, i);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirAttributeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeListContext dirAttributeList() throws RecognitionException {
		DirAttributeListContext _localctx = new DirAttributeListContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_dirAttributeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1502);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -9007199254740736L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 1369094286720628735L) != 0)) {
				{
				{
				setState(1496);
				qName();
				setState(1497);
				match(EQUAL);
				setState(1498);
				dirAttributeValue();
				}
				}
				setState(1504);
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
		public List<TerminalNode> Quot() { return getTokens(XQueryParser.Quot); }
		public TerminalNode Quot(int i) {
			return getToken(XQueryParser.Quot, i);
		}
		public List<TerminalNode> PredefinedEntityRef() { return getTokens(XQueryParser.PredefinedEntityRef); }
		public TerminalNode PredefinedEntityRef(int i) {
			return getToken(XQueryParser.PredefinedEntityRef, i);
		}
		public List<TerminalNode> CharRef() { return getTokens(XQueryParser.CharRef); }
		public TerminalNode CharRef(int i) {
			return getToken(XQueryParser.CharRef, i);
		}
		public List<TerminalNode> EscapeQuot() { return getTokens(XQueryParser.EscapeQuot); }
		public TerminalNode EscapeQuot(int i) {
			return getToken(XQueryParser.EscapeQuot, i);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirAttributeValueApos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeValueAposContext dirAttributeValueApos() throws RecognitionException {
		DirAttributeValueAposContext _localctx = new DirAttributeValueAposContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_dirAttributeValueApos);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1505);
			match(Quot);
			setState(1512);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,127,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1510);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PredefinedEntityRef:
						{
						setState(1506);
						match(PredefinedEntityRef);
						}
						break;
					case CharRef:
						{
						setState(1507);
						match(CharRef);
						}
						break;
					case EscapeQuot:
						{
						setState(1508);
						match(EscapeQuot);
						}
						break;
					case DOUBLE_LBRACE:
					case DOUBLE_RBRACE:
					case Quot:
					case LBRACE:
					case ContentChar:
						{
						setState(1509);
						dirAttributeContentQuot();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(1514);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,127,_ctx);
			}
			setState(1515);
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
		public List<TerminalNode> Apos() { return getTokens(XQueryParser.Apos); }
		public TerminalNode Apos(int i) {
			return getToken(XQueryParser.Apos, i);
		}
		public List<TerminalNode> PredefinedEntityRef() { return getTokens(XQueryParser.PredefinedEntityRef); }
		public TerminalNode PredefinedEntityRef(int i) {
			return getToken(XQueryParser.PredefinedEntityRef, i);
		}
		public List<TerminalNode> CharRef() { return getTokens(XQueryParser.CharRef); }
		public TerminalNode CharRef(int i) {
			return getToken(XQueryParser.CharRef, i);
		}
		public List<TerminalNode> EscapeApos() { return getTokens(XQueryParser.EscapeApos); }
		public TerminalNode EscapeApos(int i) {
			return getToken(XQueryParser.EscapeApos, i);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirAttributeValueQuot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeValueQuotContext dirAttributeValueQuot() throws RecognitionException {
		DirAttributeValueQuotContext _localctx = new DirAttributeValueQuotContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_dirAttributeValueQuot);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1517);
			match(Apos);
			setState(1524);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,129,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1522);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case PredefinedEntityRef:
						{
						setState(1518);
						match(PredefinedEntityRef);
						}
						break;
					case CharRef:
						{
						setState(1519);
						match(CharRef);
						}
						break;
					case EscapeApos:
						{
						setState(1520);
						match(EscapeApos);
						}
						break;
					case DOUBLE_LBRACE:
					case DOUBLE_RBRACE:
					case Apos:
					case LBRACE:
					case ContentChar:
						{
						setState(1521);
						dirAttributeContentApos();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(1526);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,129,_ctx);
			}
			setState(1527);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirAttributeValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeValueContext dirAttributeValue() throws RecognitionException {
		DirAttributeValueContext _localctx = new DirAttributeValueContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_dirAttributeValue);
		try {
			setState(1531);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Quot:
				enterOuterAlt(_localctx, 1);
				{
				setState(1529);
				dirAttributeValueApos();
				}
				break;
			case Apos:
				enterOuterAlt(_localctx, 2);
				{
				setState(1530);
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
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParser.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParser.ContentChar, i);
		}
		public TerminalNode DOUBLE_LBRACE() { return getToken(XQueryParser.DOUBLE_LBRACE, 0); }
		public TerminalNode DOUBLE_RBRACE() { return getToken(XQueryParser.DOUBLE_RBRACE, 0); }
		public DirAttributeValueAposContext dirAttributeValueApos() {
			return getRuleContext(DirAttributeValueAposContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DirAttributeContentQuotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirAttributeContentQuot; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirAttributeContentQuot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeContentQuotContext dirAttributeContentQuot() throws RecognitionException {
		DirAttributeContentQuotContext _localctx = new DirAttributeContentQuotContext(_ctx, getState());
		enterRule(_localctx, 294, RULE_dirAttributeContentQuot);
		int _la;
		try {
			int _alt;
			setState(1546);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ContentChar:
				enterOuterAlt(_localctx, 1);
				{
				setState(1534); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1533);
						match(ContentChar);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1536); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,131,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case DOUBLE_LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1538);
				match(DOUBLE_LBRACE);
				}
				break;
			case DOUBLE_RBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1539);
				match(DOUBLE_RBRACE);
				}
				break;
			case Quot:
				enterOuterAlt(_localctx, 4);
				{
				setState(1540);
				dirAttributeValueApos();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 5);
				{
				setState(1541);
				match(LBRACE);
				setState(1543);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & -279372359199281L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -1L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 2377900603251621823L) != 0)) {
					{
					setState(1542);
					expr();
					}
				}

				setState(1545);
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
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParser.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParser.ContentChar, i);
		}
		public TerminalNode DOUBLE_LBRACE() { return getToken(XQueryParser.DOUBLE_LBRACE, 0); }
		public TerminalNode DOUBLE_RBRACE() { return getToken(XQueryParser.DOUBLE_RBRACE, 0); }
		public DirAttributeValueQuotContext dirAttributeValueQuot() {
			return getRuleContext(DirAttributeValueQuotContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DirAttributeContentAposContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirAttributeContentApos; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirAttributeContentApos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirAttributeContentAposContext dirAttributeContentApos() throws RecognitionException {
		DirAttributeContentAposContext _localctx = new DirAttributeContentAposContext(_ctx, getState());
		enterRule(_localctx, 296, RULE_dirAttributeContentApos);
		int _la;
		try {
			int _alt;
			setState(1561);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ContentChar:
				enterOuterAlt(_localctx, 1);
				{
				setState(1549); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1548);
						match(ContentChar);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1551); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,134,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case DOUBLE_LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1553);
				match(DOUBLE_LBRACE);
				}
				break;
			case DOUBLE_RBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1554);
				match(DOUBLE_RBRACE);
				}
				break;
			case Apos:
				enterOuterAlt(_localctx, 4);
				{
				setState(1555);
				dirAttributeValueQuot();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 5);
				{
				setState(1556);
				match(LBRACE);
				setState(1558);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & -279372359199281L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -1L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 2377900603251621823L) != 0)) {
					{
					setState(1557);
					expr();
					}
				}

				setState(1560);
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
		public TerminalNode CDATA() { return getToken(XQueryParser.CDATA, 0); }
		public TerminalNode Quot() { return getToken(XQueryParser.Quot, 0); }
		public TerminalNode Apos() { return getToken(XQueryParser.Apos, 0); }
		public NoQuotesNoBracesNoAmpNoLAngContext noQuotesNoBracesNoAmpNoLAng() {
			return getRuleContext(NoQuotesNoBracesNoAmpNoLAngContext.class,0);
		}
		public DirElemContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dirElemContent; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDirElemContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirElemContentContext dirElemContent() throws RecognitionException {
		DirElemContentContext _localctx = new DirElemContentContext(_ctx, getState());
		enterRule(_localctx, 298, RULE_dirElemContent);
		try {
			setState(1569);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1563);
				directConstructor();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1564);
				commonContent();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1565);
				match(CDATA);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1566);
				match(Quot);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1567);
				match(Apos);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1568);
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
		public TerminalNode PredefinedEntityRef() { return getToken(XQueryParser.PredefinedEntityRef, 0); }
		public TerminalNode CharRef() { return getToken(XQueryParser.CharRef, 0); }
		public List<TerminalNode> LBRACE() { return getTokens(XQueryParser.LBRACE); }
		public TerminalNode LBRACE(int i) {
			return getToken(XQueryParser.LBRACE, i);
		}
		public List<TerminalNode> RBRACE() { return getTokens(XQueryParser.RBRACE); }
		public TerminalNode RBRACE(int i) {
			return getToken(XQueryParser.RBRACE, i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public CommonContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commonContent; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCommonContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommonContentContext commonContent() throws RecognitionException {
		CommonContentContext _localctx = new CommonContentContext(_ctx, getState());
		enterRule(_localctx, 300, RULE_commonContent);
		int _la;
		try {
			setState(1580);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1571);
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
				setState(1572);
				match(LBRACE);
				setState(1573);
				match(LBRACE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1574);
				match(RBRACE);
				setState(1575);
				match(RBRACE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1576);
				match(LBRACE);
				setState(1577);
				expr();
				setState(1578);
				match(RBRACE);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitComputedConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComputedConstructorContext computedConstructor() throws RecognitionException {
		ComputedConstructorContext _localctx = new ComputedConstructorContext(_ctx, getState());
		enterRule(_localctx, 302, RULE_computedConstructor);
		try {
			setState(1590);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_DOCUMENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1582);
				compDocConstructor();
				}
				break;
			case KW_ELEMENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1583);
				compElemConstructor();
				}
				break;
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1584);
				compAttrConstructor();
				}
				break;
			case KW_NAMESPACE:
				enterOuterAlt(_localctx, 4);
				{
				setState(1585);
				compNamespaceConstructor();
				}
				break;
			case KW_TEXT:
				enterOuterAlt(_localctx, 5);
				{
				setState(1586);
				compTextConstructor();
				}
				break;
			case KW_COMMENT:
				enterOuterAlt(_localctx, 6);
				{
				setState(1587);
				compCommentConstructor();
				}
				break;
			case KW_PI:
				enterOuterAlt(_localctx, 7);
				{
				setState(1588);
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
				setState(1589);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompMLJSONConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONConstructorContext compMLJSONConstructor() throws RecognitionException {
		CompMLJSONConstructorContext _localctx = new CompMLJSONConstructorContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_compMLJSONConstructor);
		try {
			setState(1598);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ARRAY_NODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1592);
				compMLJSONArrayConstructor();
				}
				break;
			case KW_OBJECT_NODE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1593);
				compMLJSONObjectConstructor();
				}
				break;
			case KW_NUMBER_NODE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1594);
				compMLJSONNumberConstructor();
				}
				break;
			case KW_BOOLEAN_NODE:
				enterOuterAlt(_localctx, 4);
				{
				setState(1595);
				compMLJSONBooleanConstructor();
				}
				break;
			case KW_NULL_NODE:
				enterOuterAlt(_localctx, 5);
				{
				setState(1596);
				compMLJSONNullConstructor();
				}
				break;
			case KW_BINARY:
				enterOuterAlt(_localctx, 6);
				{
				setState(1597);
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
		public TerminalNode KW_ARRAY_NODE() { return getToken(XQueryParser.KW_ARRAY_NODE, 0); }
		public EnclosedContentExprContext enclosedContentExpr() {
			return getRuleContext(EnclosedContentExprContext.class,0);
		}
		public CompMLJSONArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONArrayConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompMLJSONArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONArrayConstructorContext compMLJSONArrayConstructor() throws RecognitionException {
		CompMLJSONArrayConstructorContext _localctx = new CompMLJSONArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 306, RULE_compMLJSONArrayConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1600);
			match(KW_ARRAY_NODE);
			setState(1601);
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
		public TerminalNode KW_OBJECT_NODE() { return getToken(XQueryParser.KW_OBJECT_NODE, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(XQueryParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(XQueryParser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public CompMLJSONObjectConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONObjectConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompMLJSONObjectConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONObjectConstructorContext compMLJSONObjectConstructor() throws RecognitionException {
		CompMLJSONObjectConstructorContext _localctx = new CompMLJSONObjectConstructorContext(_ctx, getState());
		enterRule(_localctx, 308, RULE_compMLJSONObjectConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1603);
			match(KW_OBJECT_NODE);
			setState(1604);
			match(LBRACE);
			setState(1618);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & -279372359199281L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -1L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 2377900603251621823L) != 0)) {
				{
				setState(1605);
				exprSingle();
				setState(1606);
				match(COLON);
				setState(1607);
				exprSingle();
				setState(1615);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1608);
					match(COMMA);
					setState(1609);
					exprSingle();
					setState(1610);
					match(COLON);
					setState(1611);
					exprSingle();
					}
					}
					setState(1617);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1620);
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
		public TerminalNode KW_NUMBER_NODE() { return getToken(XQueryParser.KW_NUMBER_NODE, 0); }
		public EnclosedContentExprContext enclosedContentExpr() {
			return getRuleContext(EnclosedContentExprContext.class,0);
		}
		public CompMLJSONNumberConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONNumberConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompMLJSONNumberConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONNumberConstructorContext compMLJSONNumberConstructor() throws RecognitionException {
		CompMLJSONNumberConstructorContext _localctx = new CompMLJSONNumberConstructorContext(_ctx, getState());
		enterRule(_localctx, 310, RULE_compMLJSONNumberConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1622);
			match(KW_NUMBER_NODE);
			setState(1623);
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
		public TerminalNode KW_BOOLEAN_NODE() { return getToken(XQueryParser.KW_BOOLEAN_NODE, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public CompMLJSONBooleanConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONBooleanConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompMLJSONBooleanConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONBooleanConstructorContext compMLJSONBooleanConstructor() throws RecognitionException {
		CompMLJSONBooleanConstructorContext _localctx = new CompMLJSONBooleanConstructorContext(_ctx, getState());
		enterRule(_localctx, 312, RULE_compMLJSONBooleanConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1625);
			match(KW_BOOLEAN_NODE);
			setState(1626);
			match(LBRACE);
			setState(1627);
			exprSingle();
			setState(1628);
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
		public TerminalNode KW_NULL_NODE() { return getToken(XQueryParser.KW_NULL_NODE, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public CompMLJSONNullConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compMLJSONNullConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompMLJSONNullConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompMLJSONNullConstructorContext compMLJSONNullConstructor() throws RecognitionException {
		CompMLJSONNullConstructorContext _localctx = new CompMLJSONNullConstructorContext(_ctx, getState());
		enterRule(_localctx, 314, RULE_compMLJSONNullConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1630);
			match(KW_NULL_NODE);
			setState(1631);
			match(LBRACE);
			setState(1632);
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
		public TerminalNode KW_BINARY() { return getToken(XQueryParser.KW_BINARY, 0); }
		public EnclosedContentExprContext enclosedContentExpr() {
			return getRuleContext(EnclosedContentExprContext.class,0);
		}
		public CompBinaryConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compBinaryConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompBinaryConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompBinaryConstructorContext compBinaryConstructor() throws RecognitionException {
		CompBinaryConstructorContext _localctx = new CompBinaryConstructorContext(_ctx, getState());
		enterRule(_localctx, 316, RULE_compBinaryConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1634);
			match(KW_BINARY);
			setState(1635);
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
		public TerminalNode KW_DOCUMENT() { return getToken(XQueryParser.KW_DOCUMENT, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public CompDocConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compDocConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompDocConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompDocConstructorContext compDocConstructor() throws RecognitionException {
		CompDocConstructorContext _localctx = new CompDocConstructorContext(_ctx, getState());
		enterRule(_localctx, 318, RULE_compDocConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1637);
			match(KW_DOCUMENT);
			setState(1638);
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
	public static class CompElemConstructorContext extends ParserRuleContext {
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParser.KW_ELEMENT, 0); }
		public EnclosedContentExprContext enclosedContentExpr() {
			return getRuleContext(EnclosedContentExprContext.class,0);
		}
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public CompElemConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compElemConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompElemConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompElemConstructorContext compElemConstructor() throws RecognitionException {
		CompElemConstructorContext _localctx = new CompElemConstructorContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_compElemConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1640);
			match(KW_ELEMENT);
			setState(1646);
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
				setState(1641);
				eqName();
				}
				break;
			case LBRACE:
				{
				{
				setState(1642);
				match(LBRACE);
				setState(1643);
				expr();
				setState(1644);
				match(RBRACE);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1648);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitEnclosedContentExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedContentExprContext enclosedContentExpr() throws RecognitionException {
		EnclosedContentExprContext _localctx = new EnclosedContentExprContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_enclosedContentExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1650);
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
		public TerminalNode KW_ATTRIBUTE() { return getToken(XQueryParser.KW_ATTRIBUTE, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public CompAttrConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compAttrConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompAttrConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompAttrConstructorContext compAttrConstructor() throws RecognitionException {
		CompAttrConstructorContext _localctx = new CompAttrConstructorContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_compAttrConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1652);
			match(KW_ATTRIBUTE);
			setState(1658);
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
				setState(1653);
				eqName();
				}
				break;
			case LBRACE:
				{
				{
				setState(1654);
				match(LBRACE);
				setState(1655);
				expr();
				setState(1656);
				match(RBRACE);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1660);
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
	public static class CompNamespaceConstructorContext extends ParserRuleContext {
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParser.KW_NAMESPACE, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompNamespaceConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompNamespaceConstructorContext compNamespaceConstructor() throws RecognitionException {
		CompNamespaceConstructorContext _localctx = new CompNamespaceConstructorContext(_ctx, getState());
		enterRule(_localctx, 326, RULE_compNamespaceConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1662);
			match(KW_NAMESPACE);
			setState(1665);
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
				setState(1663);
				prefix();
				}
				break;
			case LBRACE:
				{
				setState(1664);
				enclosedPrefixExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1667);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixContext prefix() throws RecognitionException {
		PrefixContext _localctx = new PrefixContext(_ctx, getState());
		enterRule(_localctx, 328, RULE_prefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1669);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitEnclosedPrefixExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedPrefixExprContext enclosedPrefixExpr() throws RecognitionException {
		EnclosedPrefixExprContext _localctx = new EnclosedPrefixExprContext(_ctx, getState());
		enterRule(_localctx, 330, RULE_enclosedPrefixExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1671);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitEnclosedURIExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnclosedURIExprContext enclosedURIExpr() throws RecognitionException {
		EnclosedURIExprContext _localctx = new EnclosedURIExprContext(_ctx, getState());
		enterRule(_localctx, 332, RULE_enclosedURIExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1673);
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
		public TerminalNode KW_TEXT() { return getToken(XQueryParser.KW_TEXT, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public CompTextConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compTextConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompTextConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompTextConstructorContext compTextConstructor() throws RecognitionException {
		CompTextConstructorContext _localctx = new CompTextConstructorContext(_ctx, getState());
		enterRule(_localctx, 334, RULE_compTextConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1675);
			match(KW_TEXT);
			setState(1676);
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
	public static class CompCommentConstructorContext extends ParserRuleContext {
		public TerminalNode KW_COMMENT() { return getToken(XQueryParser.KW_COMMENT, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public CompCommentConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compCommentConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompCommentConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompCommentConstructorContext compCommentConstructor() throws RecognitionException {
		CompCommentConstructorContext _localctx = new CompCommentConstructorContext(_ctx, getState());
		enterRule(_localctx, 336, RULE_compCommentConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1678);
			match(KW_COMMENT);
			setState(1679);
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
	public static class CompPIConstructorContext extends ParserRuleContext {
		public TerminalNode KW_PI() { return getToken(XQueryParser.KW_PI, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public CompPIConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compPIConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCompPIConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompPIConstructorContext compPIConstructor() throws RecognitionException {
		CompPIConstructorContext _localctx = new CompPIConstructorContext(_ctx, getState());
		enterRule(_localctx, 338, RULE_compPIConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1681);
			match(KW_PI);
			setState(1687);
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
				setState(1682);
				ncName();
				}
				break;
			case LBRACE:
				{
				{
				setState(1683);
				match(LBRACE);
				setState(1684);
				expr();
				setState(1685);
				match(RBRACE);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1689);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionItemExprContext functionItemExpr() throws RecognitionException {
		FunctionItemExprContext _localctx = new FunctionItemExprContext(_ctx, getState());
		enterRule(_localctx, 340, RULE_functionItemExpr);
		try {
			setState(1693);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,147,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1691);
				namedFunctionRef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1692);
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
		public TerminalNode HASH() { return getToken(XQueryParser.HASH, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode IntegerLiteral() { return getToken(XQueryParser.IntegerLiteral, 0); }
		public NamedFunctionRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedFunctionRef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNamedFunctionRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedFunctionRefContext namedFunctionRef() throws RecognitionException {
		NamedFunctionRefContext _localctx = new NamedFunctionRefContext(_ctx, getState());
		enterRule(_localctx, 342, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1695);
			((NamedFunctionRefContext)_localctx).fn_name = eqName();
			setState(1696);
			match(HASH);
			setState(1697);
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
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParser.KW_FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public FunctionParamsContext functionParams() {
			return getRuleContext(FunctionParamsContext.class,0);
		}
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public InlineFunctionRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineFunctionRef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitInlineFunctionRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineFunctionRefContext inlineFunctionRef() throws RecognitionException {
		InlineFunctionRefContext _localctx = new InlineFunctionRefContext(_ctx, getState());
		enterRule(_localctx, 344, RULE_inlineFunctionRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1699);
			annotations();
			setState(1700);
			match(KW_FUNCTION);
			setState(1701);
			match(LPAREN);
			setState(1703);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(1702);
				functionParams();
				}
			}

			setState(1705);
			match(RPAREN);
			setState(1708);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KW_AS) {
				{
				setState(1706);
				match(KW_AS);
				setState(1707);
				((InlineFunctionRefContext)_localctx).return_type = sequenceType();
				}
			}

			setState(1710);
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
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 346, RULE_functionBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1712);
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
	public static class MapConstructorContext extends ParserRuleContext {
		public TerminalNode KW_MAP() { return getToken(XQueryParser.KW_MAP, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public List<MapConstructorEntryContext> mapConstructorEntry() {
			return getRuleContexts(MapConstructorEntryContext.class);
		}
		public MapConstructorEntryContext mapConstructorEntry(int i) {
			return getRuleContext(MapConstructorEntryContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public MapConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMapConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapConstructorContext mapConstructor() throws RecognitionException {
		MapConstructorContext _localctx = new MapConstructorContext(_ctx, getState());
		enterRule(_localctx, 348, RULE_mapConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1714);
			match(KW_MAP);
			setState(1715);
			match(LBRACE);
			setState(1724);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & -279372359199281L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -1L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 2377900603251621823L) != 0)) {
				{
				setState(1716);
				mapConstructorEntry();
				setState(1721);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1717);
					match(COMMA);
					setState(1718);
					mapConstructorEntry();
					}
					}
					setState(1723);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1726);
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
		public TerminalNode COLON() { return getToken(XQueryParser.COLON, 0); }
		public TerminalNode COLON_EQ() { return getToken(XQueryParser.COLON_EQ, 0); }
		public MapConstructorEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapConstructorEntry; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMapConstructorEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapConstructorEntryContext mapConstructorEntry() throws RecognitionException {
		MapConstructorEntryContext _localctx = new MapConstructorEntryContext(_ctx, getState());
		enterRule(_localctx, 350, RULE_mapConstructorEntry);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1728);
			((MapConstructorEntryContext)_localctx).mapKey = exprSingle();
			setState(1729);
			_la = _input.LA(1);
			if ( !(_la==COLON || _la==COLON_EQ) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1730);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayConstructorContext arrayConstructor() throws RecognitionException {
		ArrayConstructorContext _localctx = new ArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 352, RULE_arrayConstructor);
		try {
			setState(1734);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACKET:
				enterOuterAlt(_localctx, 1);
				{
				setState(1732);
				squareArrayConstructor();
				}
				break;
			case KW_ARRAY:
				enterOuterAlt(_localctx, 2);
				{
				setState(1733);
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
		public TerminalNode LBRACKET() { return getToken(XQueryParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(XQueryParser.RBRACKET, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SquareArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_squareArrayConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSquareArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SquareArrayConstructorContext squareArrayConstructor() throws RecognitionException {
		SquareArrayConstructorContext _localctx = new SquareArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 354, RULE_squareArrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1736);
			match(LBRACKET);
			setState(1738);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & -279372359199281L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -1L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 2377900603251621823L) != 0)) {
				{
				setState(1737);
				expr();
				}
			}

			setState(1740);
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
		public TerminalNode KW_ARRAY() { return getToken(XQueryParser.KW_ARRAY, 0); }
		public EnclosedExpressionContext enclosedExpression() {
			return getRuleContext(EnclosedExpressionContext.class,0);
		}
		public CurlyArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_curlyArrayConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCurlyArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CurlyArrayConstructorContext curlyArrayConstructor() throws RecognitionException {
		CurlyArrayConstructorContext _localctx = new CurlyArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 356, RULE_curlyArrayConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1742);
			match(KW_ARRAY);
			setState(1743);
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
		public TerminalNode ENTER_STRING() { return getToken(XQueryParser.ENTER_STRING, 0); }
		public StringConstructorContentContext stringConstructorContent() {
			return getRuleContext(StringConstructorContentContext.class,0);
		}
		public TerminalNode EXIT_STRING() { return getToken(XQueryParser.EXIT_STRING, 0); }
		public StringConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstructorContext stringConstructor() throws RecognitionException {
		StringConstructorContext _localctx = new StringConstructorContext(_ctx, getState());
		enterRule(_localctx, 358, RULE_stringConstructor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1745);
			match(ENTER_STRING);
			setState(1746);
			stringConstructorContent();
			setState(1747);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringConstructorContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstructorContentContext stringConstructorContent() throws RecognitionException {
		StringConstructorContentContext _localctx = new StringConstructorContentContext(_ctx, getState());
		enterRule(_localctx, 360, RULE_stringConstructorContent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1749);
			stringConstructorChars();
			setState(1755);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ENTER_INTERPOLATION) {
				{
				{
				setState(1750);
				stringConstructorInterpolation();
				setState(1751);
				stringConstructorChars();
				}
				}
				setState(1757);
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
		public TerminalNode BASIC_CHAR() { return getToken(XQueryParser.BASIC_CHAR, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public TerminalNode RBRACKET() { return getToken(XQueryParser.RBRACKET, 0); }
		public CharNoGraveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charNoGrave; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCharNoGrave(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharNoGraveContext charNoGrave() throws RecognitionException {
		CharNoGraveContext _localctx = new CharNoGraveContext(_ctx, getState());
		enterRule(_localctx, 362, RULE_charNoGrave);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1758);
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
		public TerminalNode BASIC_CHAR() { return getToken(XQueryParser.BASIC_CHAR, 0); }
		public TerminalNode GRAVE() { return getToken(XQueryParser.GRAVE, 0); }
		public TerminalNode RBRACKET() { return getToken(XQueryParser.RBRACKET, 0); }
		public CharNoLBraceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charNoLBrace; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCharNoLBrace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharNoLBraceContext charNoLBrace() throws RecognitionException {
		CharNoLBraceContext _localctx = new CharNoLBraceContext(_ctx, getState());
		enterRule(_localctx, 364, RULE_charNoLBrace);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1760);
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
		public TerminalNode BASIC_CHAR() { return getToken(XQueryParser.BASIC_CHAR, 0); }
		public TerminalNode GRAVE() { return getToken(XQueryParser.GRAVE, 0); }
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public CharNoRBrackContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charNoRBrack; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCharNoRBrack(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharNoRBrackContext charNoRBrack() throws RecognitionException {
		CharNoRBrackContext _localctx = new CharNoRBrackContext(_ctx, getState());
		enterRule(_localctx, 366, RULE_charNoRBrack);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1762);
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
		public List<TerminalNode> BASIC_CHAR() { return getTokens(XQueryParser.BASIC_CHAR); }
		public TerminalNode BASIC_CHAR(int i) {
			return getToken(XQueryParser.BASIC_CHAR, i);
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
		public List<TerminalNode> LBRACE() { return getTokens(XQueryParser.LBRACE); }
		public TerminalNode LBRACE(int i) {
			return getToken(XQueryParser.LBRACE, i);
		}
		public StringConstructorCharsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConstructorChars; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringConstructorChars(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstructorCharsContext stringConstructorChars() throws RecognitionException {
		StringConstructorCharsContext _localctx = new StringConstructorCharsContext(_ctx, getState());
		enterRule(_localctx, 368, RULE_stringConstructorChars);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1776);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1125899957174272L) != 0) || _la==BASIC_CHAR) {
				{
				setState(1774);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
				case 1:
					{
					setState(1764);
					match(BASIC_CHAR);
					}
					break;
				case 2:
					{
					setState(1765);
					charNoGrave();
					setState(1766);
					charNoLBrace();
					}
					break;
				case 3:
					{
					setState(1768);
					charNoRBrack();
					setState(1769);
					charNoGrave();
					setState(1770);
					charNoGrave();
					}
					break;
				case 4:
					{
					setState(1772);
					charNoGrave();
					}
					break;
				case 5:
					{
					setState(1773);
					match(LBRACE);
					}
					break;
				}
				}
				setState(1778);
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
		public TerminalNode ENTER_INTERPOLATION() { return getToken(XQueryParser.ENTER_INTERPOLATION, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EXIT_INTERPOLATION() { return getToken(XQueryParser.EXIT_INTERPOLATION, 0); }
		public StringConstructorInterpolationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConstructorInterpolation; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringConstructorInterpolation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstructorInterpolationContext stringConstructorInterpolation() throws RecognitionException {
		StringConstructorInterpolationContext _localctx = new StringConstructorInterpolationContext(_ctx, getState());
		enterRule(_localctx, 370, RULE_stringConstructorInterpolation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1779);
			match(ENTER_INTERPOLATION);
			setState(1780);
			expr();
			setState(1781);
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
		public TerminalNode QUESTION() { return getToken(XQueryParser.QUESTION, 0); }
		public KeySpecifierContext keySpecifier() {
			return getRuleContext(KeySpecifierContext.class,0);
		}
		public UnaryLookupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryLookup; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitUnaryLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryLookupContext unaryLookup() throws RecognitionException {
		UnaryLookupContext _localctx = new UnaryLookupContext(_ctx, getState());
		enterRule(_localctx, 372, RULE_unaryLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1783);
			match(QUESTION);
			setState(1784);
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
		public TerminalNode QUESTION() { return getToken(XQueryParser.QUESTION, 0); }
		public SingleTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSingleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleTypeContext singleType() throws RecognitionException {
		SingleTypeContext _localctx = new SingleTypeContext(_ctx, getState());
		enterRule(_localctx, 374, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1786);
			((SingleTypeContext)_localctx).item = simpleTypeName();
			setState(1788);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,157,_ctx) ) {
			case 1:
				{
				setState(1787);
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
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 376, RULE_typeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1790);
			match(KW_AS);
			setState(1791);
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
		public TerminalNode KW_EMPTY_SEQUENCE() { return getToken(XQueryParser.KW_EMPTY_SEQUENCE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(XQueryParser.QUESTION, 0); }
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public TerminalNode PLUS() { return getToken(XQueryParser.PLUS, 0); }
		public SequenceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSequenceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceTypeContext sequenceType() throws RecognitionException {
		SequenceTypeContext _localctx = new SequenceTypeContext(_ctx, getState());
		enterRule(_localctx, 378, RULE_sequenceType);
		try {
			setState(1802);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,159,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1793);
				match(KW_EMPTY_SEQUENCE);
				setState(1794);
				match(LPAREN);
				setState(1795);
				match(RPAREN);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1796);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(1800);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,158,_ctx) ) {
				case 1:
					{
					setState(1797);
					((SequenceTypeContext)_localctx).QUESTION = match(QUESTION);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).QUESTION);
					}
					break;
				case 2:
					{
					setState(1798);
					((SequenceTypeContext)_localctx).STAR = match(STAR);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).STAR);
					}
					break;
				case 3:
					{
					setState(1799);
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
		public TerminalNode KW_ITEM() { return getToken(XQueryParser.KW_ITEM, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitItemType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ItemTypeContext itemType() throws RecognitionException {
		ItemTypeContext _localctx = new ItemTypeContext(_ctx, getState());
		enterRule(_localctx, 380, RULE_itemType);
		try {
			setState(1813);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1804);
				kindTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1805);
				match(KW_ITEM);
				setState(1806);
				match(LPAREN);
				setState(1807);
				match(RPAREN);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1808);
				functionTest();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1809);
				mapTest();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1810);
				arrayTest();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1811);
				atomicOrUnionType();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1812);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAtomicOrUnionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomicOrUnionTypeContext atomicOrUnionType() throws RecognitionException {
		AtomicOrUnionTypeContext _localctx = new AtomicOrUnionTypeContext(_ctx, getState());
		enterRule(_localctx, 382, RULE_atomicOrUnionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1815);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitKindTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KindTestContext kindTest() throws RecognitionException {
		KindTestContext _localctx = new KindTestContext(_ctx, getState());
		enterRule(_localctx, 384, RULE_kindTest);
		try {
			setState(1829);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_DOCUMENT_NODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1817);
				documentTest();
				}
				break;
			case KW_ELEMENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1818);
				elementTest();
				}
				break;
			case KW_ATTRIBUTE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1819);
				attributeTest();
				}
				break;
			case KW_SCHEMA_ELEM:
				enterOuterAlt(_localctx, 4);
				{
				setState(1820);
				schemaElementTest();
				}
				break;
			case KW_SCHEMA_ATTR:
				enterOuterAlt(_localctx, 5);
				{
				setState(1821);
				schemaAttributeTest();
				}
				break;
			case KW_PI:
				enterOuterAlt(_localctx, 6);
				{
				setState(1822);
				piTest();
				}
				break;
			case KW_COMMENT:
				enterOuterAlt(_localctx, 7);
				{
				setState(1823);
				commentTest();
				}
				break;
			case KW_TEXT:
				enterOuterAlt(_localctx, 8);
				{
				setState(1824);
				textTest();
				}
				break;
			case KW_NAMESPACE_NODE:
				enterOuterAlt(_localctx, 9);
				{
				setState(1825);
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
				setState(1826);
				mlNodeTest();
				}
				break;
			case KW_BINARY:
				enterOuterAlt(_localctx, 11);
				{
				setState(1827);
				binaryNodeTest();
				}
				break;
			case KW_NODE:
				enterOuterAlt(_localctx, 12);
				{
				setState(1828);
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
		public TerminalNode KW_NODE() { return getToken(XQueryParser.KW_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public AnyKindTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyKindTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnyKindTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyKindTestContext anyKindTest() throws RecognitionException {
		AnyKindTestContext _localctx = new AnyKindTestContext(_ctx, getState());
		enterRule(_localctx, 386, RULE_anyKindTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1831);
			match(KW_NODE);
			setState(1832);
			match(LPAREN);
			setState(1834);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(1833);
				match(STAR);
				}
			}

			setState(1836);
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
		public TerminalNode KW_BINARY() { return getToken(XQueryParser.KW_BINARY, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public BinaryNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitBinaryNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryNodeTestContext binaryNodeTest() throws RecognitionException {
		BinaryNodeTestContext _localctx = new BinaryNodeTestContext(_ctx, getState());
		enterRule(_localctx, 388, RULE_binaryNodeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1838);
			match(KW_BINARY);
			setState(1839);
			match(LPAREN);
			setState(1840);
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
		public TerminalNode KW_DOCUMENT_NODE() { return getToken(XQueryParser.KW_DOCUMENT_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitDocumentTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentTestContext documentTest() throws RecognitionException {
		DocumentTestContext _localctx = new DocumentTestContext(_ctx, getState());
		enterRule(_localctx, 390, RULE_documentTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1842);
			match(KW_DOCUMENT_NODE);
			setState(1843);
			match(LPAREN);
			setState(1846);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ELEMENT:
				{
				setState(1844);
				elementTest();
				}
				break;
			case KW_SCHEMA_ELEM:
				{
				setState(1845);
				schemaElementTest();
				}
				break;
			case RPAREN:
				break;
			default:
				break;
			}
			setState(1848);
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
		public TerminalNode KW_TEXT() { return getToken(XQueryParser.KW_TEXT, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public TextTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTextTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextTestContext textTest() throws RecognitionException {
		TextTestContext _localctx = new TextTestContext(_ctx, getState());
		enterRule(_localctx, 392, RULE_textTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1850);
			match(KW_TEXT);
			setState(1851);
			match(LPAREN);
			setState(1852);
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
		public TerminalNode KW_COMMENT() { return getToken(XQueryParser.KW_COMMENT, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public CommentTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commentTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitCommentTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentTestContext commentTest() throws RecognitionException {
		CommentTestContext _localctx = new CommentTestContext(_ctx, getState());
		enterRule(_localctx, 394, RULE_commentTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1854);
			match(KW_COMMENT);
			setState(1855);
			match(LPAREN);
			setState(1856);
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
		public TerminalNode KW_NAMESPACE_NODE() { return getToken(XQueryParser.KW_NAMESPACE_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public NamespaceNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNamespaceNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceNodeTestContext namespaceNodeTest() throws RecognitionException {
		NamespaceNodeTestContext _localctx = new NamespaceNodeTestContext(_ctx, getState());
		enterRule(_localctx, 396, RULE_namespaceNodeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1858);
			match(KW_NAMESPACE_NODE);
			setState(1859);
			match(LPAREN);
			setState(1860);
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
		public TerminalNode KW_PI() { return getToken(XQueryParser.KW_PI, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitPiTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PiTestContext piTest() throws RecognitionException {
		PiTestContext _localctx = new PiTestContext(_ctx, getState());
		enterRule(_localctx, 398, RULE_piTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1862);
			match(KW_PI);
			setState(1863);
			match(LPAREN);
			setState(1866);
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
				setState(1864);
				ncName();
				}
				break;
			case Quot:
			case Apos:
				{
				setState(1865);
				stringLiteral();
				}
				break;
			case RPAREN:
				break;
			default:
				break;
			}
			setState(1868);
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
		public TerminalNode KW_ATTRIBUTE() { return getToken(XQueryParser.KW_ATTRIBUTE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public AttributeNameOrWildcardContext attributeNameOrWildcard() {
			return getRuleContext(AttributeNameOrWildcardContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParser.COMMA, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public AttributeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAttributeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeTestContext attributeTest() throws RecognitionException {
		AttributeTestContext _localctx = new AttributeTestContext(_ctx, getState());
		enterRule(_localctx, 400, RULE_attributeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1870);
			match(KW_ATTRIBUTE);
			setState(1871);
			match(LPAREN);
			setState(1877);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -9007199120523008L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 1441151880758556671L) != 0)) {
				{
				setState(1872);
				attributeNameOrWildcard();
				setState(1875);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1873);
					match(COMMA);
					setState(1874);
					((AttributeTestContext)_localctx).type = typeName();
					}
				}

				}
			}

			setState(1879);
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
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public AttributeNameOrWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeNameOrWildcard; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAttributeNameOrWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeNameOrWildcardContext attributeNameOrWildcard() throws RecognitionException {
		AttributeNameOrWildcardContext _localctx = new AttributeNameOrWildcardContext(_ctx, getState());
		enterRule(_localctx, 402, RULE_attributeNameOrWildcard);
		try {
			setState(1883);
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
				setState(1881);
				attributeName();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(1882);
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
		public TerminalNode KW_SCHEMA_ATTR() { return getToken(XQueryParser.KW_SCHEMA_ATTR, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public AttributeDeclarationContext attributeDeclaration() {
			return getRuleContext(AttributeDeclarationContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public SchemaAttributeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaAttributeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSchemaAttributeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaAttributeTestContext schemaAttributeTest() throws RecognitionException {
		SchemaAttributeTestContext _localctx = new SchemaAttributeTestContext(_ctx, getState());
		enterRule(_localctx, 404, RULE_schemaAttributeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1885);
			match(KW_SCHEMA_ATTR);
			setState(1886);
			match(LPAREN);
			setState(1887);
			attributeDeclaration();
			setState(1888);
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
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParser.KW_ELEMENT, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public ElementNameOrWildcardContext elementNameOrWildcard() {
			return getRuleContext(ElementNameOrWildcardContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParser.COMMA, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(XQueryParser.QUESTION, 0); }
		public ElementTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitElementTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementTestContext elementTest() throws RecognitionException {
		ElementTestContext _localctx = new ElementTestContext(_ctx, getState());
		enterRule(_localctx, 406, RULE_elementTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1890);
			match(KW_ELEMENT);
			setState(1891);
			match(LPAREN);
			setState(1900);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -9007199120523008L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 1441151880758556671L) != 0)) {
				{
				setState(1892);
				elementNameOrWildcard();
				setState(1898);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1893);
					match(COMMA);
					setState(1894);
					typeName();
					setState(1896);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==QUESTION) {
						{
						setState(1895);
						((ElementTestContext)_localctx).optional = match(QUESTION);
						}
					}

					}
				}

				}
			}

			setState(1902);
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
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public ElementNameOrWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementNameOrWildcard; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitElementNameOrWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementNameOrWildcardContext elementNameOrWildcard() throws RecognitionException {
		ElementNameOrWildcardContext _localctx = new ElementNameOrWildcardContext(_ctx, getState());
		enterRule(_localctx, 408, RULE_elementNameOrWildcard);
		try {
			setState(1906);
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
				setState(1904);
				elementName();
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(1905);
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
		public TerminalNode KW_SCHEMA_ELEM() { return getToken(XQueryParser.KW_SCHEMA_ELEM, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public ElementDeclarationContext elementDeclaration() {
			return getRuleContext(ElementDeclarationContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public SchemaElementTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaElementTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSchemaElementTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaElementTestContext schemaElementTest() throws RecognitionException {
		SchemaElementTestContext _localctx = new SchemaElementTestContext(_ctx, getState());
		enterRule(_localctx, 410, RULE_schemaElementTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1908);
			match(KW_SCHEMA_ELEM);
			setState(1909);
			match(LPAREN);
			setState(1910);
			elementDeclaration();
			setState(1911);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitElementDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementDeclarationContext elementDeclaration() throws RecognitionException {
		ElementDeclarationContext _localctx = new ElementDeclarationContext(_ctx, getState());
		enterRule(_localctx, 412, RULE_elementDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1913);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAttributeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 414, RULE_attributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1915);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitElementName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementNameContext elementName() throws RecognitionException {
		ElementNameContext _localctx = new ElementNameContext(_ctx, getState());
		enterRule(_localctx, 416, RULE_elementName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1917);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitSimpleTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTypeNameContext simpleTypeName() throws RecognitionException {
		SimpleTypeNameContext _localctx = new SimpleTypeNameContext(_ctx, getState());
		enterRule(_localctx, 418, RULE_simpleTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1919);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 420, RULE_typeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1921);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionTestContext functionTest() throws RecognitionException {
		FunctionTestContext _localctx = new FunctionTestContext(_ctx, getState());
		enterRule(_localctx, 422, RULE_functionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1926);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==MOD) {
				{
				{
				setState(1923);
				annotation();
				}
				}
				setState(1928);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1931);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,173,_ctx) ) {
			case 1:
				{
				setState(1929);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(1930);
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
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParser.KW_FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public AnyFunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyFunctionTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnyFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyFunctionTestContext anyFunctionTest() throws RecognitionException {
		AnyFunctionTestContext _localctx = new AnyFunctionTestContext(_ctx, getState());
		enterRule(_localctx, 424, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1933);
			match(KW_FUNCTION);
			setState(1934);
			match(LPAREN);
			setState(1935);
			match(STAR);
			setState(1936);
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
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParser.KW_FUNCTION, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public List<SequenceTypeContext> sequenceType() {
			return getRuleContexts(SequenceTypeContext.class);
		}
		public SequenceTypeContext sequenceType(int i) {
			return getRuleContext(SequenceTypeContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public TypedFunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedFunctionTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTypedFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedFunctionTestContext typedFunctionTest() throws RecognitionException {
		TypedFunctionTestContext _localctx = new TypedFunctionTestContext(_ctx, getState());
		enterRule(_localctx, 426, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1938);
			match(KW_FUNCTION);
			setState(1939);
			match(LPAREN);
			setState(1948);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -8972014880554752L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 1441151880758556671L) != 0)) {
				{
				setState(1940);
				sequenceType();
				setState(1945);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1941);
					match(COMMA);
					setState(1942);
					sequenceType();
					}
					}
					setState(1947);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1950);
			match(RPAREN);
			setState(1951);
			match(KW_AS);
			setState(1952);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTestContext mapTest() throws RecognitionException {
		MapTestContext _localctx = new MapTestContext(_ctx, getState());
		enterRule(_localctx, 428, RULE_mapTest);
		try {
			setState(1956);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,176,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1954);
				anyMapTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1955);
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
		public TerminalNode KW_MAP() { return getToken(XQueryParser.KW_MAP, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public AnyMapTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyMapTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnyMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyMapTestContext anyMapTest() throws RecognitionException {
		AnyMapTestContext _localctx = new AnyMapTestContext(_ctx, getState());
		enterRule(_localctx, 430, RULE_anyMapTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1958);
			match(KW_MAP);
			setState(1959);
			match(LPAREN);
			setState(1960);
			match(STAR);
			setState(1961);
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
		public TerminalNode KW_MAP() { return getToken(XQueryParser.KW_MAP, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public EqNameContext eqName() {
			return getRuleContext(EqNameContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQueryParser.COMMA, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public TypedMapTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedMapTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTypedMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedMapTestContext typedMapTest() throws RecognitionException {
		TypedMapTestContext _localctx = new TypedMapTestContext(_ctx, getState());
		enterRule(_localctx, 432, RULE_typedMapTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1963);
			match(KW_MAP);
			setState(1964);
			match(LPAREN);
			setState(1965);
			eqName();
			setState(1966);
			match(COMMA);
			setState(1967);
			sequenceType();
			setState(1968);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTestContext arrayTest() throws RecognitionException {
		ArrayTestContext _localctx = new ArrayTestContext(_ctx, getState());
		enterRule(_localctx, 434, RULE_arrayTest);
		try {
			setState(1972);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,177,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1970);
				anyArrayTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1971);
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
		public TerminalNode KW_ARRAY() { return getToken(XQueryParser.KW_ARRAY, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public AnyArrayTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyArrayTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAnyArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyArrayTestContext anyArrayTest() throws RecognitionException {
		AnyArrayTestContext _localctx = new AnyArrayTestContext(_ctx, getState());
		enterRule(_localctx, 436, RULE_anyArrayTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1974);
			match(KW_ARRAY);
			setState(1975);
			match(LPAREN);
			setState(1976);
			match(STAR);
			setState(1977);
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
		public TerminalNode KW_ARRAY() { return getToken(XQueryParser.KW_ARRAY, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public TypedArrayTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedArrayTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitTypedArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedArrayTestContext typedArrayTest() throws RecognitionException {
		TypedArrayTestContext _localctx = new TypedArrayTestContext(_ctx, getState());
		enterRule(_localctx, 438, RULE_typedArrayTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1979);
			match(KW_ARRAY);
			setState(1980);
			match(LPAREN);
			setState(1981);
			sequenceType();
			setState(1982);
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
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public ParenthesizedItemTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenthesizedItemTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitParenthesizedItemTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenthesizedItemTestContext parenthesizedItemTest() throws RecognitionException {
		ParenthesizedItemTestContext _localctx = new ParenthesizedItemTestContext(_ctx, getState());
		enterRule(_localctx, 440, RULE_parenthesizedItemTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1984);
			match(LPAREN);
			setState(1985);
			itemType();
			setState(1986);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAttributeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeDeclarationContext attributeDeclaration() throws RecognitionException {
		AttributeDeclarationContext _localctx = new AttributeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 442, RULE_attributeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1988);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMlNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNodeTestContext mlNodeTest() throws RecognitionException {
		MlNodeTestContext _localctx = new MlNodeTestContext(_ctx, getState());
		enterRule(_localctx, 444, RULE_mlNodeTest);
		try {
			setState(1995);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case KW_ARRAY_NODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1990);
				mlArrayNodeTest();
				}
				break;
			case KW_OBJECT_NODE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1991);
				mlObjectNodeTest();
				}
				break;
			case KW_NUMBER_NODE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1992);
				mlNumberNodeTest();
				}
				break;
			case KW_BOOLEAN_NODE:
				enterOuterAlt(_localctx, 4);
				{
				setState(1993);
				mlBooleanNodeTest();
				}
				break;
			case KW_NULL_NODE:
				enterOuterAlt(_localctx, 5);
				{
				setState(1994);
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
		public TerminalNode KW_ARRAY_NODE() { return getToken(XQueryParser.KW_ARRAY_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlArrayNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlArrayNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMlArrayNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlArrayNodeTestContext mlArrayNodeTest() throws RecognitionException {
		MlArrayNodeTestContext _localctx = new MlArrayNodeTestContext(_ctx, getState());
		enterRule(_localctx, 446, RULE_mlArrayNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1997);
			match(KW_ARRAY_NODE);
			setState(1998);
			match(LPAREN);
			setState(2000);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(1999);
				stringLiteral();
				}
			}

			setState(2002);
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
		public TerminalNode KW_OBJECT_NODE() { return getToken(XQueryParser.KW_OBJECT_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlObjectNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlObjectNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMlObjectNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlObjectNodeTestContext mlObjectNodeTest() throws RecognitionException {
		MlObjectNodeTestContext _localctx = new MlObjectNodeTestContext(_ctx, getState());
		enterRule(_localctx, 448, RULE_mlObjectNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2004);
			match(KW_OBJECT_NODE);
			setState(2005);
			match(LPAREN);
			setState(2007);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2006);
				stringLiteral();
				}
			}

			setState(2009);
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
		public TerminalNode KW_NUMBER_NODE() { return getToken(XQueryParser.KW_NUMBER_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlNumberNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlNumberNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMlNumberNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNumberNodeTestContext mlNumberNodeTest() throws RecognitionException {
		MlNumberNodeTestContext _localctx = new MlNumberNodeTestContext(_ctx, getState());
		enterRule(_localctx, 450, RULE_mlNumberNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2011);
			match(KW_NUMBER_NODE);
			setState(2012);
			match(LPAREN);
			setState(2014);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2013);
				stringLiteral();
				}
			}

			setState(2016);
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
		public TerminalNode KW_BOOLEAN_NODE() { return getToken(XQueryParser.KW_BOOLEAN_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlBooleanNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlBooleanNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMlBooleanNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlBooleanNodeTestContext mlBooleanNodeTest() throws RecognitionException {
		MlBooleanNodeTestContext _localctx = new MlBooleanNodeTestContext(_ctx, getState());
		enterRule(_localctx, 452, RULE_mlBooleanNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2018);
			match(KW_BOOLEAN_NODE);
			setState(2019);
			match(LPAREN);
			setState(2021);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2020);
				stringLiteral();
				}
			}

			setState(2023);
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
		public TerminalNode KW_NULL_NODE() { return getToken(XQueryParser.KW_NULL_NODE, 0); }
		public TerminalNode LPAREN() { return getToken(XQueryParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(XQueryParser.RPAREN, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlNullNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlNullNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitMlNullNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNullNodeTestContext mlNullNodeTest() throws RecognitionException {
		MlNullNodeTestContext _localctx = new MlNullNodeTestContext(_ctx, getState());
		enterRule(_localctx, 454, RULE_mlNullNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2025);
			match(KW_NULL_NODE);
			setState(2026);
			match(LPAREN);
			setState(2028);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Quot || _la==Apos) {
				{
				setState(2027);
				stringLiteral();
				}
			}

			setState(2030);
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
		public TerminalNode URIQualifiedName() { return getToken(XQueryParser.URIQualifiedName, 0); }
		public EqNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eqName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitEqName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqNameContext eqName() throws RecognitionException {
		EqNameContext _localctx = new EqNameContext(_ctx, getState());
		enterRule(_localctx, 456, RULE_eqName);
		try {
			setState(2034);
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
				setState(2032);
				qName();
				}
				break;
			case URIQualifiedName:
				enterOuterAlt(_localctx, 2);
				{
				setState(2033);
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
		public TerminalNode FullQName() { return getToken(XQueryParser.FullQName, 0); }
		public NcNameContext ncName() {
			return getRuleContext(NcNameContext.class,0);
		}
		public QNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitQName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QNameContext qName() throws RecognitionException {
		QNameContext _localctx = new QNameContext(_ctx, getState());
		enterRule(_localctx, 458, RULE_qName);
		try {
			setState(2038);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FullQName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2036);
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
				setState(2037);
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
		public TerminalNode NCName() { return getToken(XQueryParser.NCName, 0); }
		public KeywordContext keyword() {
			return getRuleContext(KeywordContext.class,0);
		}
		public NcNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ncName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNcName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NcNameContext ncName() throws RecognitionException {
		NcNameContext _localctx = new NcNameContext(_ctx, getState());
		enterRule(_localctx, 460, RULE_ncName);
		try {
			setState(2042);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2040);
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
				setState(2041);
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
		public TerminalNode FullQName() { return getToken(XQueryParser.FullQName, 0); }
		public TerminalNode NCName() { return getToken(XQueryParser.NCName, 0); }
		public TerminalNode URIQualifiedName() { return getToken(XQueryParser.URIQualifiedName, 0); }
		public KeywordOKForFunctionContext keywordOKForFunction() {
			return getRuleContext(KeywordOKForFunctionContext.class,0);
		}
		public FunctionNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitFunctionName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionNameContext functionName() throws RecognitionException {
		FunctionNameContext _localctx = new FunctionNameContext(_ctx, getState());
		enterRule(_localctx, 462, RULE_functionName);
		try {
			setState(2048);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FullQName:
				enterOuterAlt(_localctx, 1);
				{
				setState(2044);
				match(FullQName);
				}
				break;
			case NCName:
				enterOuterAlt(_localctx, 2);
				{
				setState(2045);
				match(NCName);
				}
				break;
			case URIQualifiedName:
				enterOuterAlt(_localctx, 3);
				{
				setState(2046);
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
				setState(2047);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 464, RULE_keyword);
		try {
			setState(2052);
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
				setState(2050);
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
				setState(2051);
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
		public TerminalNode KW_ATTRIBUTE() { return getToken(XQueryParser.KW_ATTRIBUTE, 0); }
		public TerminalNode KW_COMMENT() { return getToken(XQueryParser.KW_COMMENT, 0); }
		public TerminalNode KW_DOCUMENT_NODE() { return getToken(XQueryParser.KW_DOCUMENT_NODE, 0); }
		public TerminalNode KW_ELEMENT() { return getToken(XQueryParser.KW_ELEMENT, 0); }
		public TerminalNode KW_EMPTY_SEQUENCE() { return getToken(XQueryParser.KW_EMPTY_SEQUENCE, 0); }
		public TerminalNode KW_IF() { return getToken(XQueryParser.KW_IF, 0); }
		public TerminalNode KW_ITEM() { return getToken(XQueryParser.KW_ITEM, 0); }
		public TerminalNode KW_CONTEXT() { return getToken(XQueryParser.KW_CONTEXT, 0); }
		public TerminalNode KW_NODE() { return getToken(XQueryParser.KW_NODE, 0); }
		public TerminalNode KW_PI() { return getToken(XQueryParser.KW_PI, 0); }
		public TerminalNode KW_SCHEMA_ATTR() { return getToken(XQueryParser.KW_SCHEMA_ATTR, 0); }
		public TerminalNode KW_SCHEMA_ELEM() { return getToken(XQueryParser.KW_SCHEMA_ELEM, 0); }
		public TerminalNode KW_BINARY() { return getToken(XQueryParser.KW_BINARY, 0); }
		public TerminalNode KW_TEXT() { return getToken(XQueryParser.KW_TEXT, 0); }
		public TerminalNode KW_TYPESWITCH() { return getToken(XQueryParser.KW_TYPESWITCH, 0); }
		public TerminalNode KW_SWITCH() { return getToken(XQueryParser.KW_SWITCH, 0); }
		public TerminalNode KW_NAMESPACE_NODE() { return getToken(XQueryParser.KW_NAMESPACE_NODE, 0); }
		public TerminalNode KW_TYPE() { return getToken(XQueryParser.KW_TYPE, 0); }
		public TerminalNode KW_TUMBLING() { return getToken(XQueryParser.KW_TUMBLING, 0); }
		public TerminalNode KW_TRY() { return getToken(XQueryParser.KW_TRY, 0); }
		public TerminalNode KW_CATCH() { return getToken(XQueryParser.KW_CATCH, 0); }
		public TerminalNode KW_ONLY() { return getToken(XQueryParser.KW_ONLY, 0); }
		public TerminalNode KW_WHEN() { return getToken(XQueryParser.KW_WHEN, 0); }
		public TerminalNode KW_SLIDING() { return getToken(XQueryParser.KW_SLIDING, 0); }
		public TerminalNode KW_DECIMAL_FORMAT() { return getToken(XQueryParser.KW_DECIMAL_FORMAT, 0); }
		public TerminalNode KW_WINDOW() { return getToken(XQueryParser.KW_WINDOW, 0); }
		public TerminalNode KW_COUNT() { return getToken(XQueryParser.KW_COUNT, 0); }
		public TerminalNode KW_MAP() { return getToken(XQueryParser.KW_MAP, 0); }
		public TerminalNode KW_END() { return getToken(XQueryParser.KW_END, 0); }
		public TerminalNode KW_ALLOWING() { return getToken(XQueryParser.KW_ALLOWING, 0); }
		public TerminalNode KW_ARRAY() { return getToken(XQueryParser.KW_ARRAY, 0); }
		public TerminalNode DFPropertyName() { return getToken(XQueryParser.DFPropertyName, 0); }
		public TerminalNode KW_ARRAY_NODE() { return getToken(XQueryParser.KW_ARRAY_NODE, 0); }
		public TerminalNode KW_BOOLEAN_NODE() { return getToken(XQueryParser.KW_BOOLEAN_NODE, 0); }
		public TerminalNode KW_NULL_NODE() { return getToken(XQueryParser.KW_NULL_NODE, 0); }
		public TerminalNode KW_NUMBER_NODE() { return getToken(XQueryParser.KW_NUMBER_NODE, 0); }
		public TerminalNode KW_OBJECT_NODE() { return getToken(XQueryParser.KW_OBJECT_NODE, 0); }
		public TerminalNode KW_UPDATE() { return getToken(XQueryParser.KW_UPDATE, 0); }
		public TerminalNode KW_REPLACE() { return getToken(XQueryParser.KW_REPLACE, 0); }
		public TerminalNode KW_WITH() { return getToken(XQueryParser.KW_WITH, 0); }
		public TerminalNode KW_VALUE() { return getToken(XQueryParser.KW_VALUE, 0); }
		public TerminalNode KW_INSERT() { return getToken(XQueryParser.KW_INSERT, 0); }
		public TerminalNode KW_INTO() { return getToken(XQueryParser.KW_INTO, 0); }
		public TerminalNode KW_DELETE() { return getToken(XQueryParser.KW_DELETE, 0); }
		public TerminalNode KW_NEXT() { return getToken(XQueryParser.KW_NEXT, 0); }
		public TerminalNode KW_RENAME() { return getToken(XQueryParser.KW_RENAME, 0); }
		public KeywordNotOKForFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keywordNotOKForFunction; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitKeywordNotOKForFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordNotOKForFunctionContext keywordNotOKForFunction() throws RecognitionException {
		KeywordNotOKForFunctionContext _localctx = new KeywordNotOKForFunctionContext(_ctx, getState());
		enterRule(_localctx, 466, RULE_keywordNotOKForFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2054);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2458965396544291072L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -7475691707260725983L) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 36022792168507393L) != 0)) ) {
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
		public TerminalNode KW_ANCESTOR() { return getToken(XQueryParser.KW_ANCESTOR, 0); }
		public TerminalNode KW_ANCESTOR_OR_SELF() { return getToken(XQueryParser.KW_ANCESTOR_OR_SELF, 0); }
		public TerminalNode KW_AND() { return getToken(XQueryParser.KW_AND, 0); }
		public TerminalNode KW_AS() { return getToken(XQueryParser.KW_AS, 0); }
		public TerminalNode KW_ASCENDING() { return getToken(XQueryParser.KW_ASCENDING, 0); }
		public TerminalNode KW_AT() { return getToken(XQueryParser.KW_AT, 0); }
		public TerminalNode KW_BASE_URI() { return getToken(XQueryParser.KW_BASE_URI, 0); }
		public TerminalNode KW_BOUNDARY_SPACE() { return getToken(XQueryParser.KW_BOUNDARY_SPACE, 0); }
		public TerminalNode KW_BY() { return getToken(XQueryParser.KW_BY, 0); }
		public TerminalNode KW_CASE() { return getToken(XQueryParser.KW_CASE, 0); }
		public TerminalNode KW_CAST() { return getToken(XQueryParser.KW_CAST, 0); }
		public TerminalNode KW_CASTABLE() { return getToken(XQueryParser.KW_CASTABLE, 0); }
		public TerminalNode KW_CHILD() { return getToken(XQueryParser.KW_CHILD, 0); }
		public TerminalNode KW_COLLATION() { return getToken(XQueryParser.KW_COLLATION, 0); }
		public TerminalNode KW_CONSTRUCTION() { return getToken(XQueryParser.KW_CONSTRUCTION, 0); }
		public TerminalNode KW_COPY_NS() { return getToken(XQueryParser.KW_COPY_NS, 0); }
		public TerminalNode KW_DECLARE() { return getToken(XQueryParser.KW_DECLARE, 0); }
		public TerminalNode KW_DEFAULT() { return getToken(XQueryParser.KW_DEFAULT, 0); }
		public TerminalNode KW_DESCENDANT() { return getToken(XQueryParser.KW_DESCENDANT, 0); }
		public TerminalNode KW_DESCENDANT_OR_SELF() { return getToken(XQueryParser.KW_DESCENDANT_OR_SELF, 0); }
		public TerminalNode KW_DESCENDING() { return getToken(XQueryParser.KW_DESCENDING, 0); }
		public TerminalNode KW_DIV() { return getToken(XQueryParser.KW_DIV, 0); }
		public TerminalNode KW_DOCUMENT() { return getToken(XQueryParser.KW_DOCUMENT, 0); }
		public TerminalNode KW_ELSE() { return getToken(XQueryParser.KW_ELSE, 0); }
		public TerminalNode KW_EMPTY() { return getToken(XQueryParser.KW_EMPTY, 0); }
		public TerminalNode KW_ENCODING() { return getToken(XQueryParser.KW_ENCODING, 0); }
		public TerminalNode KW_EQ() { return getToken(XQueryParser.KW_EQ, 0); }
		public TerminalNode KW_EVERY() { return getToken(XQueryParser.KW_EVERY, 0); }
		public TerminalNode KW_EXCEPT() { return getToken(XQueryParser.KW_EXCEPT, 0); }
		public TerminalNode KW_EXTERNAL() { return getToken(XQueryParser.KW_EXTERNAL, 0); }
		public TerminalNode KW_FOLLOWING() { return getToken(XQueryParser.KW_FOLLOWING, 0); }
		public TerminalNode KW_FOLLOWING_SIBLING() { return getToken(XQueryParser.KW_FOLLOWING_SIBLING, 0); }
		public TerminalNode KW_FOR() { return getToken(XQueryParser.KW_FOR, 0); }
		public TerminalNode KW_FUNCTION() { return getToken(XQueryParser.KW_FUNCTION, 0); }
		public TerminalNode KW_GE() { return getToken(XQueryParser.KW_GE, 0); }
		public TerminalNode KW_GREATEST() { return getToken(XQueryParser.KW_GREATEST, 0); }
		public TerminalNode KW_GROUP() { return getToken(XQueryParser.KW_GROUP, 0); }
		public TerminalNode KW_GT() { return getToken(XQueryParser.KW_GT, 0); }
		public TerminalNode KW_IDIV() { return getToken(XQueryParser.KW_IDIV, 0); }
		public TerminalNode KW_IMPORT() { return getToken(XQueryParser.KW_IMPORT, 0); }
		public TerminalNode KW_IN() { return getToken(XQueryParser.KW_IN, 0); }
		public TerminalNode KW_INHERIT() { return getToken(XQueryParser.KW_INHERIT, 0); }
		public TerminalNode KW_INSTANCE() { return getToken(XQueryParser.KW_INSTANCE, 0); }
		public TerminalNode KW_INTERSECT() { return getToken(XQueryParser.KW_INTERSECT, 0); }
		public TerminalNode KW_IS() { return getToken(XQueryParser.KW_IS, 0); }
		public TerminalNode KW_LAX() { return getToken(XQueryParser.KW_LAX, 0); }
		public TerminalNode KW_LE() { return getToken(XQueryParser.KW_LE, 0); }
		public TerminalNode KW_LEAST() { return getToken(XQueryParser.KW_LEAST, 0); }
		public TerminalNode KW_LET() { return getToken(XQueryParser.KW_LET, 0); }
		public TerminalNode KW_LT() { return getToken(XQueryParser.KW_LT, 0); }
		public TerminalNode KW_MOD() { return getToken(XQueryParser.KW_MOD, 0); }
		public TerminalNode KW_MODULE() { return getToken(XQueryParser.KW_MODULE, 0); }
		public TerminalNode KW_NAMESPACE() { return getToken(XQueryParser.KW_NAMESPACE, 0); }
		public TerminalNode KW_NE() { return getToken(XQueryParser.KW_NE, 0); }
		public TerminalNode KW_NO_INHERIT() { return getToken(XQueryParser.KW_NO_INHERIT, 0); }
		public TerminalNode KW_NO_PRESERVE() { return getToken(XQueryParser.KW_NO_PRESERVE, 0); }
		public TerminalNode KW_OF() { return getToken(XQueryParser.KW_OF, 0); }
		public TerminalNode KW_OPTION() { return getToken(XQueryParser.KW_OPTION, 0); }
		public TerminalNode KW_OR() { return getToken(XQueryParser.KW_OR, 0); }
		public TerminalNode KW_ORDER() { return getToken(XQueryParser.KW_ORDER, 0); }
		public TerminalNode KW_ORDERED() { return getToken(XQueryParser.KW_ORDERED, 0); }
		public TerminalNode KW_ORDERING() { return getToken(XQueryParser.KW_ORDERING, 0); }
		public TerminalNode KW_PARENT() { return getToken(XQueryParser.KW_PARENT, 0); }
		public TerminalNode KW_PRECEDING() { return getToken(XQueryParser.KW_PRECEDING, 0); }
		public TerminalNode KW_PRECEDING_SIBLING() { return getToken(XQueryParser.KW_PRECEDING_SIBLING, 0); }
		public TerminalNode KW_PRESERVE() { return getToken(XQueryParser.KW_PRESERVE, 0); }
		public TerminalNode KW_RETURN() { return getToken(XQueryParser.KW_RETURN, 0); }
		public TerminalNode KW_SATISFIES() { return getToken(XQueryParser.KW_SATISFIES, 0); }
		public TerminalNode KW_SCHEMA() { return getToken(XQueryParser.KW_SCHEMA, 0); }
		public TerminalNode KW_SELF() { return getToken(XQueryParser.KW_SELF, 0); }
		public TerminalNode KW_SOME() { return getToken(XQueryParser.KW_SOME, 0); }
		public TerminalNode KW_STABLE() { return getToken(XQueryParser.KW_STABLE, 0); }
		public TerminalNode KW_START() { return getToken(XQueryParser.KW_START, 0); }
		public TerminalNode KW_STRICT() { return getToken(XQueryParser.KW_STRICT, 0); }
		public TerminalNode KW_STRIP() { return getToken(XQueryParser.KW_STRIP, 0); }
		public TerminalNode KW_THEN() { return getToken(XQueryParser.KW_THEN, 0); }
		public TerminalNode KW_TO() { return getToken(XQueryParser.KW_TO, 0); }
		public TerminalNode KW_TREAT() { return getToken(XQueryParser.KW_TREAT, 0); }
		public TerminalNode KW_UNION() { return getToken(XQueryParser.KW_UNION, 0); }
		public TerminalNode KW_UNORDERED() { return getToken(XQueryParser.KW_UNORDERED, 0); }
		public TerminalNode KW_VALIDATE() { return getToken(XQueryParser.KW_VALIDATE, 0); }
		public TerminalNode KW_VARIABLE() { return getToken(XQueryParser.KW_VARIABLE, 0); }
		public TerminalNode KW_VERSION() { return getToken(XQueryParser.KW_VERSION, 0); }
		public TerminalNode KW_WHERE() { return getToken(XQueryParser.KW_WHERE, 0); }
		public TerminalNode KW_XQUERY() { return getToken(XQueryParser.KW_XQUERY, 0); }
		public KeywordOKForFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keywordOKForFunction; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitKeywordOKForFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordOKForFunctionContext keywordOKForFunction() throws RecognitionException {
		KeywordOKForFunctionContext _localctx = new KeywordOKForFunctionContext(_ctx, getState());
		enterRule(_localctx, 468, RULE_keywordOKForFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2056);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitUriLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UriLiteralContext uriLiteral() throws RecognitionException {
		UriLiteralContext _localctx = new UriLiteralContext(_ctx, getState());
		enterRule(_localctx, 470, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2058);
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
		public List<TerminalNode> Quot() { return getTokens(XQueryParser.Quot); }
		public TerminalNode Quot(int i) {
			return getToken(XQueryParser.Quot, i);
		}
		public List<TerminalNode> PredefinedEntityRef() { return getTokens(XQueryParser.PredefinedEntityRef); }
		public TerminalNode PredefinedEntityRef(int i) {
			return getToken(XQueryParser.PredefinedEntityRef, i);
		}
		public List<TerminalNode> CharRef() { return getTokens(XQueryParser.CharRef); }
		public TerminalNode CharRef(int i) {
			return getToken(XQueryParser.CharRef, i);
		}
		public List<TerminalNode> EscapeQuot() { return getTokens(XQueryParser.EscapeQuot); }
		public TerminalNode EscapeQuot(int i) {
			return getToken(XQueryParser.EscapeQuot, i);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringLiteralQuot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralQuotContext stringLiteralQuot() throws RecognitionException {
		StringLiteralQuotContext _localctx = new StringLiteralQuotContext(_ctx, getState());
		enterRule(_localctx, 472, RULE_stringLiteralQuot);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2060);
			match(Quot);
			setState(2067);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2252899325691910L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 4611686018427387903L) != 0) || _la==ContentChar) {
				{
				setState(2065);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PredefinedEntityRef:
					{
					setState(2061);
					match(PredefinedEntityRef);
					}
					break;
				case CharRef:
					{
					setState(2062);
					match(CharRef);
					}
					break;
				case EscapeQuot:
					{
					setState(2063);
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
					setState(2064);
					stringContentQuot();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2069);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2070);
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
		public List<TerminalNode> Apos() { return getTokens(XQueryParser.Apos); }
		public TerminalNode Apos(int i) {
			return getToken(XQueryParser.Apos, i);
		}
		public List<TerminalNode> PredefinedEntityRef() { return getTokens(XQueryParser.PredefinedEntityRef); }
		public TerminalNode PredefinedEntityRef(int i) {
			return getToken(XQueryParser.PredefinedEntityRef, i);
		}
		public List<TerminalNode> CharRef() { return getTokens(XQueryParser.CharRef); }
		public TerminalNode CharRef(int i) {
			return getToken(XQueryParser.CharRef, i);
		}
		public List<TerminalNode> EscapeApos() { return getTokens(XQueryParser.EscapeApos); }
		public TerminalNode EscapeApos(int i) {
			return getToken(XQueryParser.EscapeApos, i);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringLiteralApos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralAposContext stringLiteralApos() throws RecognitionException {
		StringLiteralAposContext _localctx = new StringLiteralAposContext(_ctx, getState());
		enterRule(_localctx, 474, RULE_stringLiteralApos);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2072);
			match(Apos);
			setState(2079);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2252899325693956L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 4611686018427387903L) != 0) || _la==ContentChar) {
				{
				setState(2077);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PredefinedEntityRef:
					{
					setState(2073);
					match(PredefinedEntityRef);
					}
					break;
				case CharRef:
					{
					setState(2074);
					match(CharRef);
					}
					break;
				case EscapeApos:
					{
					setState(2075);
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
					setState(2076);
					stringContentApos();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(2081);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2082);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 476, RULE_stringLiteral);
		try {
			setState(2086);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Quot:
				enterOuterAlt(_localctx, 1);
				{
				setState(2084);
				stringLiteralQuot();
				}
				break;
			case Apos:
				enterOuterAlt(_localctx, 2);
				{
				setState(2085);
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
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParser.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParser.ContentChar, i);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public TerminalNode DOUBLE_LBRACE() { return getToken(XQueryParser.DOUBLE_LBRACE, 0); }
		public TerminalNode DOUBLE_RBRACE() { return getToken(XQueryParser.DOUBLE_RBRACE, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringContentQuot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContentQuotContext stringContentQuot() throws RecognitionException {
		StringContentQuotContext _localctx = new StringContentQuotContext(_ctx, getState());
		enterRule(_localctx, 478, RULE_stringContentQuot);
		try {
			int _alt;
			setState(2105);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,197,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2089); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(2088);
						match(ContentChar);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(2091); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,194,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2093);
				match(LBRACE);
				setState(2095);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,195,_ctx) ) {
				case 1:
					{
					setState(2094);
					expr();
					}
					break;
				}
				setState(2098);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,196,_ctx) ) {
				case 1:
					{
					setState(2097);
					match(RBRACE);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2100);
				match(RBRACE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2101);
				match(DOUBLE_LBRACE);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2102);
				match(DOUBLE_RBRACE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2103);
				noQuotesNoBracesNoAmpNoLAng();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2104);
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
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParser.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParser.ContentChar, i);
		}
		public TerminalNode LBRACE() { return getToken(XQueryParser.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(XQueryParser.RBRACE, 0); }
		public TerminalNode DOUBLE_LBRACE() { return getToken(XQueryParser.DOUBLE_LBRACE, 0); }
		public TerminalNode DOUBLE_RBRACE() { return getToken(XQueryParser.DOUBLE_RBRACE, 0); }
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitStringContentApos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContentAposContext stringContentApos() throws RecognitionException {
		StringContentAposContext _localctx = new StringContentAposContext(_ctx, getState());
		enterRule(_localctx, 480, RULE_stringContentApos);
		try {
			int _alt;
			setState(2124);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,201,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2108); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(2107);
						match(ContentChar);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(2110); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,198,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2112);
				match(LBRACE);
				setState(2114);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,199,_ctx) ) {
				case 1:
					{
					setState(2113);
					expr();
					}
					break;
				}
				setState(2117);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,200,_ctx) ) {
				case 1:
					{
					setState(2116);
					match(RBRACE);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2119);
				match(RBRACE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2120);
				match(DOUBLE_LBRACE);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2121);
				match(DOUBLE_RBRACE);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2122);
				noQuotesNoBracesNoAmpNoLAng();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2123);
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
		public List<TerminalNode> IntegerLiteral() { return getTokens(XQueryParser.IntegerLiteral); }
		public TerminalNode IntegerLiteral(int i) {
			return getToken(XQueryParser.IntegerLiteral, i);
		}
		public List<TerminalNode> DecimalLiteral() { return getTokens(XQueryParser.DecimalLiteral); }
		public TerminalNode DecimalLiteral(int i) {
			return getToken(XQueryParser.DecimalLiteral, i);
		}
		public List<TerminalNode> DoubleLiteral() { return getTokens(XQueryParser.DoubleLiteral); }
		public TerminalNode DoubleLiteral(int i) {
			return getToken(XQueryParser.DoubleLiteral, i);
		}
		public List<TerminalNode> PRAGMA() { return getTokens(XQueryParser.PRAGMA); }
		public TerminalNode PRAGMA(int i) {
			return getToken(XQueryParser.PRAGMA, i);
		}
		public List<TerminalNode> EQUAL() { return getTokens(XQueryParser.EQUAL); }
		public TerminalNode EQUAL(int i) {
			return getToken(XQueryParser.EQUAL, i);
		}
		public List<TerminalNode> HASH() { return getTokens(XQueryParser.HASH); }
		public TerminalNode HASH(int i) {
			return getToken(XQueryParser.HASH, i);
		}
		public List<TerminalNode> NOT_EQUAL() { return getTokens(XQueryParser.NOT_EQUAL); }
		public TerminalNode NOT_EQUAL(int i) {
			return getToken(XQueryParser.NOT_EQUAL, i);
		}
		public List<TerminalNode> LPAREN() { return getTokens(XQueryParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(XQueryParser.LPAREN, i);
		}
		public List<TerminalNode> RPAREN() { return getTokens(XQueryParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(XQueryParser.RPAREN, i);
		}
		public List<TerminalNode> LBRACKET() { return getTokens(XQueryParser.LBRACKET); }
		public TerminalNode LBRACKET(int i) {
			return getToken(XQueryParser.LBRACKET, i);
		}
		public List<TerminalNode> RBRACKET() { return getTokens(XQueryParser.RBRACKET); }
		public TerminalNode RBRACKET(int i) {
			return getToken(XQueryParser.RBRACKET, i);
		}
		public List<TerminalNode> STAR() { return getTokens(XQueryParser.STAR); }
		public TerminalNode STAR(int i) {
			return getToken(XQueryParser.STAR, i);
		}
		public List<TerminalNode> PLUS() { return getTokens(XQueryParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(XQueryParser.PLUS, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(XQueryParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(XQueryParser.MINUS, i);
		}
		public List<TerminalNode> TILDE() { return getTokens(XQueryParser.TILDE); }
		public TerminalNode TILDE(int i) {
			return getToken(XQueryParser.TILDE, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQueryParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQueryParser.COMMA, i);
		}
		public List<TerminalNode> ARROW() { return getTokens(XQueryParser.ARROW); }
		public TerminalNode ARROW(int i) {
			return getToken(XQueryParser.ARROW, i);
		}
		public List<TerminalNode> KW_NEXT() { return getTokens(XQueryParser.KW_NEXT); }
		public TerminalNode KW_NEXT(int i) {
			return getToken(XQueryParser.KW_NEXT, i);
		}
		public List<TerminalNode> KW_PREVIOUS() { return getTokens(XQueryParser.KW_PREVIOUS); }
		public TerminalNode KW_PREVIOUS(int i) {
			return getToken(XQueryParser.KW_PREVIOUS, i);
		}
		public List<TerminalNode> MOD() { return getTokens(XQueryParser.MOD); }
		public TerminalNode MOD(int i) {
			return getToken(XQueryParser.MOD, i);
		}
		public List<TerminalNode> DOT() { return getTokens(XQueryParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(XQueryParser.DOT, i);
		}
		public List<TerminalNode> GRAVE() { return getTokens(XQueryParser.GRAVE); }
		public TerminalNode GRAVE(int i) {
			return getToken(XQueryParser.GRAVE, i);
		}
		public List<TerminalNode> DDOT() { return getTokens(XQueryParser.DDOT); }
		public TerminalNode DDOT(int i) {
			return getToken(XQueryParser.DDOT, i);
		}
		public List<TerminalNode> XQDOC_COMMENT_START() { return getTokens(XQueryParser.XQDOC_COMMENT_START); }
		public TerminalNode XQDOC_COMMENT_START(int i) {
			return getToken(XQueryParser.XQDOC_COMMENT_START, i);
		}
		public List<TerminalNode> COLON() { return getTokens(XQueryParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(XQueryParser.COLON, i);
		}
		public List<TerminalNode> CARAT() { return getTokens(XQueryParser.CARAT); }
		public TerminalNode CARAT(int i) {
			return getToken(XQueryParser.CARAT, i);
		}
		public List<TerminalNode> COLON_EQ() { return getTokens(XQueryParser.COLON_EQ); }
		public TerminalNode COLON_EQ(int i) {
			return getToken(XQueryParser.COLON_EQ, i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(XQueryParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(XQueryParser.SEMICOLON, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(XQueryParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(XQueryParser.SLASH, i);
		}
		public List<TerminalNode> DSLASH() { return getTokens(XQueryParser.DSLASH); }
		public TerminalNode DSLASH(int i) {
			return getToken(XQueryParser.DSLASH, i);
		}
		public List<TerminalNode> BACKSLASH() { return getTokens(XQueryParser.BACKSLASH); }
		public TerminalNode BACKSLASH(int i) {
			return getToken(XQueryParser.BACKSLASH, i);
		}
		public List<TerminalNode> COMMENT() { return getTokens(XQueryParser.COMMENT); }
		public TerminalNode COMMENT(int i) {
			return getToken(XQueryParser.COMMENT, i);
		}
		public List<TerminalNode> VBAR() { return getTokens(XQueryParser.VBAR); }
		public TerminalNode VBAR(int i) {
			return getToken(XQueryParser.VBAR, i);
		}
		public List<TerminalNode> RANGLE() { return getTokens(XQueryParser.RANGLE); }
		public TerminalNode RANGLE(int i) {
			return getToken(XQueryParser.RANGLE, i);
		}
		public List<TerminalNode> QUESTION() { return getTokens(XQueryParser.QUESTION); }
		public TerminalNode QUESTION(int i) {
			return getToken(XQueryParser.QUESTION, i);
		}
		public List<TerminalNode> AT() { return getTokens(XQueryParser.AT); }
		public TerminalNode AT(int i) {
			return getToken(XQueryParser.AT, i);
		}
		public List<TerminalNode> DOLLAR() { return getTokens(XQueryParser.DOLLAR); }
		public TerminalNode DOLLAR(int i) {
			return getToken(XQueryParser.DOLLAR, i);
		}
		public List<TerminalNode> BANG() { return getTokens(XQueryParser.BANG); }
		public TerminalNode BANG(int i) {
			return getToken(XQueryParser.BANG, i);
		}
		public List<TerminalNode> FullQName() { return getTokens(XQueryParser.FullQName); }
		public TerminalNode FullQName(int i) {
			return getToken(XQueryParser.FullQName, i);
		}
		public List<TerminalNode> URIQualifiedName() { return getTokens(XQueryParser.URIQualifiedName); }
		public TerminalNode URIQualifiedName(int i) {
			return getToken(XQueryParser.URIQualifiedName, i);
		}
		public List<TerminalNode> NCNameWithLocalWildcard() { return getTokens(XQueryParser.NCNameWithLocalWildcard); }
		public TerminalNode NCNameWithLocalWildcard(int i) {
			return getToken(XQueryParser.NCNameWithLocalWildcard, i);
		}
		public List<TerminalNode> NCNameWithPrefixWildcard() { return getTokens(XQueryParser.NCNameWithPrefixWildcard); }
		public TerminalNode NCNameWithPrefixWildcard(int i) {
			return getToken(XQueryParser.NCNameWithPrefixWildcard, i);
		}
		public List<TerminalNode> NCName() { return getTokens(XQueryParser.NCName); }
		public TerminalNode NCName(int i) {
			return getToken(XQueryParser.NCName, i);
		}
		public List<TerminalNode> ContentChar() { return getTokens(XQueryParser.ContentChar); }
		public TerminalNode ContentChar(int i) {
			return getToken(XQueryParser.ContentChar, i);
		}
		public NoQuotesNoBracesNoAmpNoLAngContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noQuotesNoBracesNoAmpNoLAng; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitNoQuotesNoBracesNoAmpNoLAng(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoQuotesNoBracesNoAmpNoLAngContext noQuotesNoBracesNoAmpNoLAng() throws RecognitionException {
		NoQuotesNoBracesNoAmpNoLAngContext _localctx = new NoQuotesNoBracesNoAmpNoLAngContext(_ctx, getState());
		enterRule(_localctx, 482, RULE_noQuotesNoBracesNoAmpNoLAng);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(2128); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(2128);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,202,_ctx) ) {
					case 1:
						{
						setState(2126);
						keyword();
						}
						break;
					case 2:
						{
						setState(2127);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 6754299828379872L) != 0) || ((((_la - 123)) & ~0x3f) == 0 && ((1L << (_la - 123)) & -2305843009213628415L) != 0) || ((((_la - 187)) & ~0x3f) == 0 && ((1L << (_la - 187)) & 519L) != 0)) ) {
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
				setState(2130); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,203,_ctx);
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
		"\u0004\u0001\u00cb\u0855\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"\u00ef\u0002\u00f0\u0007\u00f0\u0002\u00f1\u0007\u00f1\u0001\u0000\u0003"+
		"\u0000\u01e6\b\u0000\u0001\u0000\u0003\u0000\u01e9\b\u0000\u0001\u0000"+
		"\u0003\u0000\u01ec\b\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u01f0\b"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0003\u0002\u01f9\b\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0003\u0007\u0211\b\u0007\u0001\u0007\u0001\u0007\u0005"+
		"\u0007\u0215\b\u0007\n\u0007\f\u0007\u0218\t\u0007\u0001\u0007\u0003\u0007"+
		"\u021b\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u0220\b"+
		"\u0007\n\u0007\f\u0007\u0223\t\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0003"+
		"\b\u0229\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u0239\b\n\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u0265\b\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0005\u0014\u026a\b\u0014\n\u0014\f\u0014\u026d\t\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0272\b\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u0279\b\u0015"+
		"\n\u0015\f\u0015\u027c\t\u0015\u0003\u0015\u027e\b\u0015\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003"+
		"\u0016\u0287\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0003\u0017\u028f\b\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0005\u0017\u0296\b\u0017\n\u0017\f\u0017"+
		"\u0299\t\u0017\u0003\u0017\u029b\b\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u02a9\b\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u02b0"+
		"\b\u0019\u0003\u0019\u02b2\b\u0019\u0001\u001a\u0001\u001a\u0001\u001b"+
		"\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0003\u001c\u02bd\b\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0003\u001c\u02c4\b\u001c\u0003\u001c\u02c6\b\u001c\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003"+
		"\u001d\u02ce\b\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u02d2\b\u001d"+
		"\u0001\u001d\u0001\u001d\u0003\u001d\u02d6\b\u001d\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0005\u001e\u02db\b\u001e\n\u001e\f\u001e\u02de\t\u001e\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u02e3\b\u001f\u0001 \u0005"+
		" \u02e6\b \n \f \u02e9\t \u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0003"+
		"!\u02f1\b!\u0001\"\u0001\"\u0001\"\u0005\"\u02f6\b\"\n\"\f\"\u02f9\t\""+
		"\u0001#\u0001#\u0001$\u0001$\u0001$\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0001&\u0001&\u0001&\u0005&\u0308\b&\n&\f&\u030b\t&\u0001\'\u0001\'"+
		"\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u0315\b\'\u0001"+
		"(\u0001(\u0005(\u0319\b(\n(\f(\u031c\t(\u0001(\u0001(\u0001)\u0001)\u0001"+
		")\u0003)\u0323\b)\u0001*\u0001*\u0001*\u0001*\u0001*\u0003*\u032a\b*\u0001"+
		"+\u0001+\u0001+\u0001+\u0005+\u0330\b+\n+\f+\u0333\t+\u0001,\u0001,\u0001"+
		",\u0003,\u0338\b,\u0001,\u0003,\u033b\b,\u0001,\u0003,\u033e\b,\u0001"+
		",\u0001,\u0001,\u0001-\u0001-\u0001-\u0001.\u0001.\u0001.\u0001.\u0001"+
		"/\u0001/\u0001/\u0001/\u0005/\u034e\b/\n/\f/\u0351\t/\u00010\u00010\u0001"+
		"0\u00030\u0356\b0\u00010\u00010\u00010\u00011\u00011\u00011\u00031\u035e"+
		"\b1\u00012\u00012\u00012\u00012\u00012\u00032\u0365\b2\u00012\u00012\u0001"+
		"2\u00012\u00032\u036b\b2\u00013\u00013\u00013\u00013\u00013\u00033\u0372"+
		"\b3\u00013\u00013\u00013\u00013\u00013\u00014\u00014\u00014\u00014\u0001"+
		"4\u00015\u00035\u037f\b5\u00015\u00015\u00015\u00015\u00015\u00016\u0001"+
		"6\u00036\u0388\b6\u00016\u00036\u038b\b6\u00016\u00016\u00016\u00036\u0390"+
		"\b6\u00016\u00016\u00016\u00036\u0395\b6\u00017\u00017\u00017\u00017\u0001"+
		"8\u00018\u00018\u00019\u00019\u00019\u00019\u0001:\u0001:\u0001:\u0005"+
		":\u03a5\b:\n:\f:\u03a8\t:\u0001;\u0001;\u0001;\u0003;\u03ad\b;\u0001;"+
		"\u0001;\u0003;\u03b1\b;\u0001;\u0001;\u0003;\u03b5\b;\u0001<\u0003<\u03b8"+
		"\b<\u0001<\u0001<\u0001<\u0001<\u0001<\u0005<\u03bf\b<\n<\f<\u03c2\t<"+
		"\u0001=\u0001=\u0001=\u0003=\u03c7\b=\u0001=\u0001=\u0001=\u0003=\u03cc"+
		"\b=\u0003=\u03ce\b=\u0001=\u0001=\u0003=\u03d2\b=\u0001>\u0001>\u0001"+
		">\u0001?\u0001?\u0003?\u03d9\b?\u0001?\u0001?\u0001?\u0005?\u03de\b?\n"+
		"?\f?\u03e1\t?\u0001?\u0001?\u0001?\u0001@\u0001@\u0001@\u0003@\u03e9\b"+
		"@\u0001@\u0001@\u0001@\u0001A\u0001A\u0001A\u0001A\u0001A\u0004A\u03f3"+
		"\bA\u000bA\fA\u03f4\u0001A\u0001A\u0001A\u0001A\u0001B\u0001B\u0004B\u03fd"+
		"\bB\u000bB\fB\u03fe\u0001B\u0001B\u0001B\u0001C\u0001C\u0001D\u0001D\u0001"+
		"D\u0001D\u0001D\u0004D\u040b\bD\u000bD\fD\u040c\u0001D\u0001D\u0001D\u0003"+
		"D\u0412\bD\u0001D\u0001D\u0001D\u0001E\u0001E\u0001E\u0001E\u0001E\u0003"+
		"E\u041c\bE\u0001E\u0001E\u0001E\u0001E\u0001F\u0001F\u0001F\u0005F\u0425"+
		"\bF\nF\fF\u0428\tF\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001"+
		"G\u0001G\u0001H\u0001H\u0004H\u0435\bH\u000bH\fH\u0436\u0001I\u0001I\u0001"+
		"I\u0001J\u0001J\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0003"+
		"K\u0445\bK\u0001K\u0001K\u0001L\u0001L\u0003L\u044b\bL\u0001L\u0001L\u0001"+
		"M\u0001M\u0001M\u0005M\u0452\bM\nM\fM\u0455\tM\u0001N\u0001N\u0001N\u0001"+
		"N\u0001N\u0001N\u0003N\u045d\bN\u0001O\u0001O\u0001O\u0001O\u0001O\u0001"+
		"P\u0001P\u0001P\u0001P\u0001P\u0001Q\u0001Q\u0001Q\u0001Q\u0001Q\u0001"+
		"R\u0001R\u0001R\u0001S\u0001S\u0001S\u0001S\u0001S\u0001T\u0001T\u0001"+
		"T\u0005T\u0479\bT\nT\fT\u047c\tT\u0001U\u0001U\u0001U\u0005U\u0481\bU"+
		"\nU\fU\u0484\tU\u0001V\u0001V\u0001V\u0001V\u0003V\u048a\bV\u0001V\u0001"+
		"V\u0003V\u048e\bV\u0001W\u0001W\u0001W\u0005W\u0493\bW\nW\fW\u0496\tW"+
		"\u0001X\u0001X\u0001X\u0003X\u049b\bX\u0001Y\u0001Y\u0001Y\u0005Y\u04a0"+
		"\bY\nY\fY\u04a3\tY\u0001Z\u0001Z\u0001Z\u0005Z\u04a8\bZ\nZ\fZ\u04ab\t"+
		"Z\u0001[\u0001[\u0001[\u0005[\u04b0\b[\n[\f[\u04b3\t[\u0001\\\u0001\\"+
		"\u0001\\\u0005\\\u04b8\b\\\n\\\f\\\u04bb\t\\\u0001]\u0001]\u0001]\u0001"+
		"]\u0003]\u04c1\b]\u0001^\u0001^\u0001^\u0001^\u0003^\u04c7\b^\u0001_\u0001"+
		"_\u0001_\u0001_\u0003_\u04cd\b_\u0001`\u0001`\u0001`\u0001`\u0003`\u04d3"+
		"\b`\u0001a\u0001a\u0001a\u0005a\u04d8\ba\na\fa\u04db\ta\u0001b\u0001b"+
		"\u0001b\u0001c\u0005c\u04e1\bc\nc\fc\u04e4\tc\u0001c\u0001c\u0001d\u0001"+
		"d\u0001d\u0003d\u04eb\bd\u0001e\u0001e\u0001e\u0001e\u0001e\u0001e\u0001"+
		"e\u0001e\u0003e\u04f5\be\u0001f\u0001f\u0001g\u0001g\u0001g\u0001g\u0001"+
		"g\u0003g\u04fe\bg\u0001h\u0001h\u0001h\u0001h\u0003h\u0504\bh\u0001h\u0001"+
		"h\u0001i\u0001i\u0001j\u0004j\u050b\bj\u000bj\fj\u050c\u0001j\u0001j\u0001"+
		"j\u0001j\u0001k\u0001k\u0001k\u0005k\u0516\bk\nk\fk\u0519\tk\u0001l\u0001"+
		"l\u0003l\u051d\bl\u0001l\u0001l\u0001l\u0003l\u0522\bl\u0001m\u0001m\u0001"+
		"m\u0005m\u0527\bm\nm\fm\u052a\tm\u0001n\u0001n\u0003n\u052e\bn\u0001o"+
		"\u0001o\u0003o\u0532\bo\u0001o\u0001o\u0001p\u0001p\u0001p\u0001p\u0003"+
		"p\u053a\bp\u0001q\u0001q\u0001q\u0001q\u0001r\u0003r\u0541\br\u0001r\u0001"+
		"r\u0001s\u0001s\u0001s\u0001s\u0003s\u0549\bs\u0001t\u0001t\u0001t\u0001"+
		"t\u0001u\u0001u\u0001v\u0001v\u0003v\u0553\bv\u0001w\u0001w\u0003w\u0557"+
		"\bw\u0001x\u0001x\u0001x\u0003x\u055c\bx\u0001y\u0001y\u0001y\u0001y\u0005"+
		"y\u0562\by\ny\fy\u0565\ty\u0001z\u0001z\u0001z\u0001z\u0005z\u056b\bz"+
		"\nz\fz\u056e\tz\u0003z\u0570\bz\u0001z\u0001z\u0001{\u0005{\u0575\b{\n"+
		"{\f{\u0578\t{\u0001|\u0001|\u0001|\u0001|\u0001}\u0001}\u0001}\u0001~"+
		"\u0001~\u0001~\u0001~\u0003~\u0585\b~\u0001\u007f\u0001\u007f\u0001\u007f"+
		"\u0003\u007f\u058a\b\u007f\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080"+
		"\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080"+
		"\u0001\u0080\u0001\u0080\u0001\u0080\u0003\u0080\u0599\b\u0080\u0001\u0081"+
		"\u0001\u0081\u0003\u0081\u059d\b\u0081\u0001\u0082\u0001\u0082\u0001\u0083"+
		"\u0001\u0083\u0001\u0083\u0001\u0084\u0001\u0084\u0001\u0085\u0001\u0085"+
		"\u0003\u0085\u05a8\b\u0085\u0001\u0085\u0001\u0085\u0001\u0086\u0001\u0086"+
		"\u0001\u0087\u0001\u0087\u0001\u0087\u0001\u0088\u0001\u0088\u0001\u0088"+
		"\u0001\u0089\u0001\u0089\u0001\u0089\u0001\u008a\u0001\u008a\u0003\u008a"+
		"\u05b9\b\u008a\u0001\u008b\u0001\u008b\u0003\u008b\u05bd\b\u008b\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0003\u008c\u05c2\b\u008c\u0001\u008d\u0001"+
		"\u008d\u0001\u008d\u0001\u008d\u0001\u008d\u0005\u008d\u05c9\b\u008d\n"+
		"\u008d\f\u008d\u05cc\t\u008d\u0001\u008d\u0001\u008d\u0001\u008d\u0001"+
		"\u008d\u0001\u008d\u0001\u008e\u0001\u008e\u0001\u008e\u0001\u008e\u0001"+
		"\u008e\u0001\u008e\u0001\u008f\u0001\u008f\u0001\u008f\u0001\u008f\u0005"+
		"\u008f\u05dd\b\u008f\n\u008f\f\u008f\u05e0\t\u008f\u0001\u0090\u0001\u0090"+
		"\u0001\u0090\u0001\u0090\u0001\u0090\u0005\u0090\u05e7\b\u0090\n\u0090"+
		"\f\u0090\u05ea\t\u0090\u0001\u0090\u0001\u0090\u0001\u0091\u0001\u0091"+
		"\u0001\u0091\u0001\u0091\u0001\u0091\u0005\u0091\u05f3\b\u0091\n\u0091"+
		"\f\u0091\u05f6\t\u0091\u0001\u0091\u0001\u0091\u0001\u0092\u0001\u0092"+
		"\u0003\u0092\u05fc\b\u0092\u0001\u0093\u0004\u0093\u05ff\b\u0093\u000b"+
		"\u0093\f\u0093\u0600\u0001\u0093\u0001\u0093\u0001\u0093\u0001\u0093\u0001"+
		"\u0093\u0003\u0093\u0608\b\u0093\u0001\u0093\u0003\u0093\u060b\b\u0093"+
		"\u0001\u0094\u0004\u0094\u060e\b\u0094\u000b\u0094\f\u0094\u060f\u0001"+
		"\u0094\u0001\u0094\u0001\u0094\u0001\u0094\u0001\u0094\u0003\u0094\u0617"+
		"\b\u0094\u0001\u0094\u0003\u0094\u061a\b\u0094\u0001\u0095\u0001\u0095"+
		"\u0001\u0095\u0001\u0095\u0001\u0095\u0001\u0095\u0003\u0095\u0622\b\u0095"+
		"\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096\u0001\u0096"+
		"\u0001\u0096\u0001\u0096\u0001\u0096\u0003\u0096\u062d\b\u0096\u0001\u0097"+
		"\u0001\u0097\u0001\u0097\u0001\u0097\u0001\u0097\u0001\u0097\u0001\u0097"+
		"\u0001\u0097\u0003\u0097\u0637\b\u0097\u0001\u0098\u0001\u0098\u0001\u0098"+
		"\u0001\u0098\u0001\u0098\u0001\u0098\u0003\u0098\u063f\b\u0098\u0001\u0099"+
		"\u0001\u0099\u0001\u0099\u0001\u009a\u0001\u009a\u0001\u009a\u0001\u009a"+
		"\u0001\u009a\u0001\u009a\u0001\u009a\u0001\u009a\u0001\u009a\u0001\u009a"+
		"\u0005\u009a\u064e\b\u009a\n\u009a\f\u009a\u0651\t\u009a\u0003\u009a\u0653"+
		"\b\u009a\u0001\u009a\u0001\u009a\u0001\u009b\u0001\u009b\u0001\u009b\u0001"+
		"\u009c\u0001\u009c\u0001\u009c\u0001\u009c\u0001\u009c\u0001\u009d\u0001"+
		"\u009d\u0001\u009d\u0001\u009d\u0001\u009e\u0001\u009e\u0001\u009e\u0001"+
		"\u009f\u0001\u009f\u0001\u009f\u0001\u00a0\u0001\u00a0\u0001\u00a0\u0001"+
		"\u00a0\u0001\u00a0\u0001\u00a0\u0003\u00a0\u066f\b\u00a0\u0001\u00a0\u0001"+
		"\u00a0\u0001\u00a1\u0001\u00a1\u0001\u00a2\u0001\u00a2\u0001\u00a2\u0001"+
		"\u00a2\u0001\u00a2\u0001\u00a2\u0003\u00a2\u067b\b\u00a2\u0001\u00a2\u0001"+
		"\u00a2\u0001\u00a3\u0001\u00a3\u0001\u00a3\u0003\u00a3\u0682\b\u00a3\u0001"+
		"\u00a3\u0001\u00a3\u0001\u00a4\u0001\u00a4\u0001\u00a5\u0001\u00a5\u0001"+
		"\u00a6\u0001\u00a6\u0001\u00a7\u0001\u00a7\u0001\u00a7\u0001\u00a8\u0001"+
		"\u00a8\u0001\u00a8\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001"+
		"\u00a9\u0001\u00a9\u0003\u00a9\u0698\b\u00a9\u0001\u00a9\u0001\u00a9\u0001"+
		"\u00aa\u0001\u00aa\u0003\u00aa\u069e\b\u00aa\u0001\u00ab\u0001\u00ab\u0001"+
		"\u00ab\u0001\u00ab\u0001\u00ac\u0001\u00ac\u0001\u00ac\u0001\u00ac\u0003"+
		"\u00ac\u06a8\b\u00ac\u0001\u00ac\u0001\u00ac\u0001\u00ac\u0003\u00ac\u06ad"+
		"\b\u00ac\u0001\u00ac\u0001\u00ac\u0001\u00ad\u0001\u00ad\u0001\u00ae\u0001"+
		"\u00ae\u0001\u00ae\u0001\u00ae\u0001\u00ae\u0005\u00ae\u06b8\b\u00ae\n"+
		"\u00ae\f\u00ae\u06bb\t\u00ae\u0003\u00ae\u06bd\b\u00ae\u0001\u00ae\u0001"+
		"\u00ae\u0001\u00af\u0001\u00af\u0001\u00af\u0001\u00af\u0001\u00b0\u0001"+
		"\u00b0\u0003\u00b0\u06c7\b\u00b0\u0001\u00b1\u0001\u00b1\u0003\u00b1\u06cb"+
		"\b\u00b1\u0001\u00b1\u0001\u00b1\u0001\u00b2\u0001\u00b2\u0001\u00b2\u0001"+
		"\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b4\u0001\u00b4\u0001"+
		"\u00b4\u0001\u00b4\u0005\u00b4\u06da\b\u00b4\n\u00b4\f\u00b4\u06dd\t\u00b4"+
		"\u0001\u00b5\u0001\u00b5\u0001\u00b6\u0001\u00b6\u0001\u00b7\u0001\u00b7"+
		"\u0001\u00b8\u0001\u00b8\u0001\u00b8\u0001\u00b8\u0001\u00b8\u0001\u00b8"+
		"\u0001\u00b8\u0001\u00b8\u0001\u00b8\u0001\u00b8\u0005\u00b8\u06ef\b\u00b8"+
		"\n\u00b8\f\u00b8\u06f2\t\u00b8\u0001\u00b9\u0001\u00b9\u0001\u00b9\u0001"+
		"\u00b9\u0001\u00ba\u0001\u00ba\u0001\u00ba\u0001\u00bb\u0001\u00bb\u0003"+
		"\u00bb\u06fd\b\u00bb\u0001\u00bc\u0001\u00bc\u0001\u00bc\u0001\u00bd\u0001"+
		"\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0003"+
		"\u00bd\u0709\b\u00bd\u0003\u00bd\u070b\b\u00bd\u0001\u00be\u0001\u00be"+
		"\u0001\u00be\u0001\u00be\u0001\u00be\u0001\u00be\u0001\u00be\u0001\u00be"+
		"\u0001\u00be\u0003\u00be\u0716\b\u00be\u0001\u00bf\u0001\u00bf\u0001\u00c0"+
		"\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0001\u00c0"+
		"\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0001\u00c0\u0003\u00c0"+
		"\u0726\b\u00c0\u0001\u00c1\u0001\u00c1\u0001\u00c1\u0003\u00c1\u072b\b"+
		"\u00c1\u0001\u00c1\u0001\u00c1\u0001\u00c2\u0001\u00c2\u0001\u00c2\u0001"+
		"\u00c2\u0001\u00c3\u0001\u00c3\u0001\u00c3\u0001\u00c3\u0003\u00c3\u0737"+
		"\b\u00c3\u0001\u00c3\u0001\u00c3\u0001\u00c4\u0001\u00c4\u0001\u00c4\u0001"+
		"\u00c4\u0001\u00c5\u0001\u00c5\u0001\u00c5\u0001\u00c5\u0001\u00c6\u0001"+
		"\u00c6\u0001\u00c6\u0001\u00c6\u0001\u00c7\u0001\u00c7\u0001\u00c7\u0001"+
		"\u00c7\u0003\u00c7\u074b\b\u00c7\u0001\u00c7\u0001\u00c7\u0001\u00c8\u0001"+
		"\u00c8\u0001\u00c8\u0001\u00c8\u0001\u00c8\u0003\u00c8\u0754\b\u00c8\u0003"+
		"\u00c8\u0756\b\u00c8\u0001\u00c8\u0001\u00c8\u0001\u00c9\u0001\u00c9\u0003"+
		"\u00c9\u075c\b\u00c9\u0001\u00ca\u0001\u00ca\u0001\u00ca\u0001\u00ca\u0001"+
		"\u00ca\u0001\u00cb\u0001\u00cb\u0001\u00cb\u0001\u00cb\u0001\u00cb\u0001"+
		"\u00cb\u0003\u00cb\u0769\b\u00cb\u0003\u00cb\u076b\b\u00cb\u0003\u00cb"+
		"\u076d\b\u00cb\u0001\u00cb\u0001\u00cb\u0001\u00cc\u0001\u00cc\u0003\u00cc"+
		"\u0773\b\u00cc\u0001\u00cd\u0001\u00cd\u0001\u00cd\u0001\u00cd\u0001\u00cd"+
		"\u0001\u00ce\u0001\u00ce\u0001\u00cf\u0001\u00cf\u0001\u00d0\u0001\u00d0"+
		"\u0001\u00d1\u0001\u00d1\u0001\u00d2\u0001\u00d2\u0001\u00d3\u0005\u00d3"+
		"\u0785\b\u00d3\n\u00d3\f\u00d3\u0788\t\u00d3\u0001\u00d3\u0001\u00d3\u0003"+
		"\u00d3\u078c\b\u00d3\u0001\u00d4\u0001\u00d4\u0001\u00d4\u0001\u00d4\u0001"+
		"\u00d4\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0005"+
		"\u00d5\u0798\b\u00d5\n\u00d5\f\u00d5\u079b\t\u00d5\u0003\u00d5\u079d\b"+
		"\u00d5\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0001\u00d5\u0001\u00d6\u0001"+
		"\u00d6\u0003\u00d6\u07a5\b\u00d6\u0001\u00d7\u0001\u00d7\u0001\u00d7\u0001"+
		"\u00d7\u0001\u00d7\u0001\u00d8\u0001\u00d8\u0001\u00d8\u0001\u00d8\u0001"+
		"\u00d8\u0001\u00d8\u0001\u00d8\u0001\u00d9\u0001\u00d9\u0003\u00d9\u07b5"+
		"\b\u00d9\u0001\u00da\u0001\u00da\u0001\u00da\u0001\u00da\u0001\u00da\u0001"+
		"\u00db\u0001\u00db\u0001\u00db\u0001\u00db\u0001\u00db\u0001\u00dc\u0001"+
		"\u00dc\u0001\u00dc\u0001\u00dc\u0001\u00dd\u0001\u00dd\u0001\u00de\u0001"+
		"\u00de\u0001\u00de\u0001\u00de\u0001\u00de\u0003\u00de\u07cc\b\u00de\u0001"+
		"\u00df\u0001\u00df\u0001\u00df\u0003\u00df\u07d1\b\u00df\u0001\u00df\u0001"+
		"\u00df\u0001\u00e0\u0001\u00e0\u0001\u00e0\u0003\u00e0\u07d8\b\u00e0\u0001"+
		"\u00e0\u0001\u00e0\u0001\u00e1\u0001\u00e1\u0001\u00e1\u0003\u00e1\u07df"+
		"\b\u00e1\u0001\u00e1\u0001\u00e1\u0001\u00e2\u0001\u00e2\u0001\u00e2\u0003"+
		"\u00e2\u07e6\b\u00e2\u0001\u00e2\u0001\u00e2\u0001\u00e3\u0001\u00e3\u0001"+
		"\u00e3\u0003\u00e3\u07ed\b\u00e3\u0001\u00e3\u0001\u00e3\u0001\u00e4\u0001"+
		"\u00e4\u0003\u00e4\u07f3\b\u00e4\u0001\u00e5\u0001\u00e5\u0003\u00e5\u07f7"+
		"\b\u00e5\u0001\u00e6\u0001\u00e6\u0003\u00e6\u07fb\b\u00e6\u0001\u00e7"+
		"\u0001\u00e7\u0001\u00e7\u0001\u00e7\u0003\u00e7\u0801\b\u00e7\u0001\u00e8"+
		"\u0001\u00e8\u0003\u00e8\u0805\b\u00e8\u0001\u00e9\u0001\u00e9\u0001\u00ea"+
		"\u0001\u00ea\u0001\u00eb\u0001\u00eb\u0001\u00ec\u0001\u00ec\u0001\u00ec"+
		"\u0001\u00ec\u0001\u00ec\u0005\u00ec\u0812\b\u00ec\n\u00ec\f\u00ec\u0815"+
		"\t\u00ec\u0001\u00ec\u0001\u00ec\u0001\u00ed\u0001\u00ed\u0001\u00ed\u0001"+
		"\u00ed\u0001\u00ed\u0005\u00ed\u081e\b\u00ed\n\u00ed\f\u00ed\u0821\t\u00ed"+
		"\u0001\u00ed\u0001\u00ed\u0001\u00ee\u0001\u00ee\u0003\u00ee\u0827\b\u00ee"+
		"\u0001\u00ef\u0004\u00ef\u082a\b\u00ef\u000b\u00ef\f\u00ef\u082b\u0001"+
		"\u00ef\u0001\u00ef\u0003\u00ef\u0830\b\u00ef\u0001\u00ef\u0003\u00ef\u0833"+
		"\b\u00ef\u0001\u00ef\u0001\u00ef\u0001\u00ef\u0001\u00ef\u0001\u00ef\u0003"+
		"\u00ef\u083a\b\u00ef\u0001\u00f0\u0004\u00f0\u083d\b\u00f0\u000b\u00f0"+
		"\f\u00f0\u083e\u0001\u00f0\u0001\u00f0\u0003\u00f0\u0843\b\u00f0\u0001"+
		"\u00f0\u0003\u00f0\u0846\b\u00f0\u0001\u00f0\u0001\u00f0\u0001\u00f0\u0001"+
		"\u00f0\u0001\u00f0\u0003\u00f0\u084d\b\u00f0\u0001\u00f1\u0001\u00f1\u0004"+
		"\u00f1\u0851\b\u00f1\u000b\u00f1\f\u00f1\u0852\u0001\u00f1\u0000\u0000"+
		"\u00f2\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080"+
		"\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098"+
		"\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0"+
		"\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8"+
		"\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0"+
		"\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8"+
		"\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110"+
		"\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120\u0122\u0124\u0126\u0128"+
		"\u012a\u012c\u012e\u0130\u0132\u0134\u0136\u0138\u013a\u013c\u013e\u0140"+
		"\u0142\u0144\u0146\u0148\u014a\u014c\u014e\u0150\u0152\u0154\u0156\u0158"+
		"\u015a\u015c\u015e\u0160\u0162\u0164\u0166\u0168\u016a\u016c\u016e\u0170"+
		"\u0172\u0174\u0176\u0178\u017a\u017c\u017e\u0180\u0182\u0184\u0186\u0188"+
		"\u018a\u018c\u018e\u0190\u0192\u0194\u0196\u0198\u019a\u019c\u019e\u01a0"+
		"\u01a2\u01a4\u01a6\u01a8\u01aa\u01ac\u01ae\u01b0\u01b2\u01b4\u01b6\u01b8"+
		"\u01ba\u01bc\u01be\u01c0\u01c2\u01c4\u01c6\u01c8\u01ca\u01cc\u01ce\u01d0"+
		"\u01d2\u01d4\u01d6\u01d8\u01da\u01dc\u01de\u01e0\u01e2\u0000\u001b\u0002"+
		"\u0000VVcc\u0002\u0000\u008a\u008a\u0098\u0098\u0002\u0000\u0085\u0085"+
		"\u00a3\u00a3\u0002\u0000eess\u0002\u0000~~\u008a\u008a\u0002\u0000ll}"+
		"}\u0003\u0000``\u0088\u0088\u00b5\u00b5\u0001\u0000\u001c\u001d\u0004"+
		"\u0000\u001b\u001bSShhww\u0002\u0000\'\'\u00a2\u00a2\u0002\u0000^^nn\u0006"+
		"\u0000\\\\ddggrruuzz\u0002\u0000::\u00a0\u00a0\u0002\u0000qq\u0097\u0097"+
		"\u0001\u0000$%\u0005\u0000==FFOP`a\u0092\u0092\u0002\u000067\u0087\u0089"+
		"\u0001\u0000\u0005\u0007\u0002\u0000\r\r\u000f\u000f\u0001\u0000\t\n\u0001"+
		"\u0000!\"\u0002\u0000\u0018\u0019\u00c5\u00c5\u0003\u0000\u0018\u0018"+
		"22\u00c5\u00c5\u0003\u0000\u0019\u001922\u00c5\u00c5\u001c\u0000\b\b5"+
		"599==@@EEHHJJLLRRUVYY[[iippvv{|\u007f\u007f\u0081\u0081\u008c\u008c\u0090"+
		"\u0091\u0093\u0093\u0099\u009a\u009e\u00a1\u00a4\u00a4\u00a8\u00a8\u00aa"+
		"\u00aa\u00ac\u00b7\u001a\u000068:<>?ADFGIIKKMQSTWXZZ\\hjoquwz}~\u0080"+
		"\u0080\u0082\u008a\u008d\u008f\u0092\u0092\u0094\u0098\u009b\u009d\u00a2"+
		"\u00a3\u00a5\u00a7\u00a9\u00a9\u00ab\u00ab\u000b\u0000\u0005\u0007\r\r"+
		"\u0011\u0011\u0013\u0018\u001b\')244{{\u008b\u008b\u00b8\u00bd\u00c4\u00c4"+
		"\u08a4\u0000\u01e5\u0001\u0000\u0000\u0000\u0002\u01f1\u0001\u0000\u0000"+
		"\u0000\u0004\u01f3\u0001\u0000\u0000\u0000\u0006\u01fc\u0001\u0000\u0000"+
		"\u0000\b\u01ff\u0001\u0000\u0000\u0000\n\u0201\u0001\u0000\u0000\u0000"+
		"\f\u0204\u0001\u0000\u0000\u0000\u000e\u0216\u0001\u0000\u0000\u0000\u0010"+
		"\u0228\u0001\u0000\u0000\u0000\u0012\u022a\u0001\u0000\u0000\u0000\u0014"+
		"\u0238\u0001\u0000\u0000\u0000\u0016\u023a\u0001\u0000\u0000\u0000\u0018"+
		"\u023e\u0001\u0000\u0000\u0000\u001a\u0243\u0001\u0000\u0000\u0000\u001c"+
		"\u0247\u0001\u0000\u0000\u0000\u001e\u024b\u0001\u0000\u0000\u0000 \u024f"+
		"\u0001\u0000\u0000\u0000\"\u0255\u0001\u0000\u0000\u0000$\u025b\u0001"+
		"\u0000\u0000\u0000&\u025d\u0001\u0000\u0000\u0000(\u025f\u0001\u0000\u0000"+
		"\u0000*\u026e\u0001\u0000\u0000\u0000,\u0286\u0001\u0000\u0000\u0000."+
		"\u0288\u0001\u0000\u0000\u00000\u029c\u0001\u0000\u0000\u00002\u02a2\u0001"+
		"\u0000\u0000\u00004\u02b3\u0001\u0000\u0000\u00006\u02b5\u0001\u0000\u0000"+
		"\u00008\u02b7\u0001\u0000\u0000\u0000:\u02c7\u0001\u0000\u0000\u0000<"+
		"\u02d7\u0001\u0000\u0000\u0000>\u02df\u0001\u0000\u0000\u0000@\u02e7\u0001"+
		"\u0000\u0000\u0000B\u02ea\u0001\u0000\u0000\u0000D\u02f2\u0001\u0000\u0000"+
		"\u0000F\u02fa\u0001\u0000\u0000\u0000H\u02fc\u0001\u0000\u0000\u0000J"+
		"\u02ff\u0001\u0000\u0000\u0000L\u0304\u0001\u0000\u0000\u0000N\u0314\u0001"+
		"\u0000\u0000\u0000P\u0316\u0001\u0000\u0000\u0000R\u0322\u0001\u0000\u0000"+
		"\u0000T\u0329\u0001\u0000\u0000\u0000V\u032b\u0001\u0000\u0000\u0000X"+
		"\u0334\u0001\u0000\u0000\u0000Z\u0342\u0001\u0000\u0000\u0000\\\u0345"+
		"\u0001\u0000\u0000\u0000^\u0349\u0001\u0000\u0000\u0000`\u0352\u0001\u0000"+
		"\u0000\u0000b\u035a\u0001\u0000\u0000\u0000d\u035f\u0001\u0000\u0000\u0000"+
		"f\u036c\u0001\u0000\u0000\u0000h\u0378\u0001\u0000\u0000\u0000j\u037e"+
		"\u0001\u0000\u0000\u0000l\u0387\u0001\u0000\u0000\u0000n\u0396\u0001\u0000"+
		"\u0000\u0000p\u039a\u0001\u0000\u0000\u0000r\u039d\u0001\u0000\u0000\u0000"+
		"t\u03a1\u0001\u0000\u0000\u0000v\u03a9\u0001\u0000\u0000\u0000x\u03b7"+
		"\u0001\u0000\u0000\u0000z\u03c3\u0001\u0000\u0000\u0000|\u03d3\u0001\u0000"+
		"\u0000\u0000~\u03d8\u0001\u0000\u0000\u0000\u0080\u03e5\u0001\u0000\u0000"+
		"\u0000\u0082\u03ed\u0001\u0000\u0000\u0000\u0084\u03fc\u0001\u0000\u0000"+
		"\u0000\u0086\u0403\u0001\u0000\u0000\u0000\u0088\u0405\u0001\u0000\u0000"+
		"\u0000\u008a\u0416\u0001\u0000\u0000\u0000\u008c\u0421\u0001\u0000\u0000"+
		"\u0000\u008e\u0429\u0001\u0000\u0000\u0000\u0090\u0432\u0001\u0000\u0000"+
		"\u0000\u0092\u0438\u0001\u0000\u0000\u0000\u0094\u043b\u0001\u0000\u0000"+
		"\u0000\u0096\u043d\u0001\u0000\u0000\u0000\u0098\u0448\u0001\u0000\u0000"+
		"\u0000\u009a\u044e\u0001\u0000\u0000\u0000\u009c\u0456\u0001\u0000\u0000"+
		"\u0000\u009e\u045e\u0001\u0000\u0000\u0000\u00a0\u0463\u0001\u0000\u0000"+
		"\u0000\u00a2\u0468\u0001\u0000\u0000\u0000\u00a4\u046d\u0001\u0000\u0000"+
		"\u0000\u00a6\u0470\u0001\u0000\u0000\u0000\u00a8\u0475\u0001\u0000\u0000"+
		"\u0000\u00aa\u047d\u0001\u0000\u0000\u0000\u00ac\u0485\u0001\u0000\u0000"+
		"\u0000\u00ae\u048f\u0001\u0000\u0000\u0000\u00b0\u0497\u0001\u0000\u0000"+
		"\u0000\u00b2\u049c\u0001\u0000\u0000\u0000\u00b4\u04a4\u0001\u0000\u0000"+
		"\u0000\u00b6\u04ac\u0001\u0000\u0000\u0000\u00b8\u04b4\u0001\u0000\u0000"+
		"\u0000\u00ba\u04bc\u0001\u0000\u0000\u0000\u00bc\u04c2\u0001\u0000\u0000"+
		"\u0000\u00be\u04c8\u0001\u0000\u0000\u0000\u00c0\u04ce\u0001\u0000\u0000"+
		"\u0000\u00c2\u04d4\u0001\u0000\u0000\u0000\u00c4\u04dc\u0001\u0000\u0000"+
		"\u0000\u00c6\u04e2\u0001\u0000\u0000\u0000\u00c8\u04ea\u0001\u0000\u0000"+
		"\u0000\u00ca\u04f4\u0001\u0000\u0000\u0000\u00cc\u04f6\u0001\u0000\u0000"+
		"\u0000\u00ce\u04fd\u0001\u0000\u0000\u0000\u00d0\u04ff\u0001\u0000\u0000"+
		"\u0000\u00d2\u0507\u0001\u0000\u0000\u0000\u00d4\u050a\u0001\u0000\u0000"+
		"\u0000\u00d6\u0512\u0001\u0000\u0000\u0000\u00d8\u0521\u0001\u0000\u0000"+
		"\u0000\u00da\u0523\u0001\u0000\u0000\u0000\u00dc\u052d\u0001\u0000\u0000"+
		"\u0000\u00de\u0531\u0001\u0000\u0000\u0000\u00e0\u0539\u0001\u0000\u0000"+
		"\u0000\u00e2\u053b\u0001\u0000\u0000\u0000\u00e4\u0540\u0001\u0000\u0000"+
		"\u0000\u00e6\u0548\u0001\u0000\u0000\u0000\u00e8\u054a\u0001\u0000\u0000"+
		"\u0000\u00ea\u054e\u0001\u0000\u0000\u0000\u00ec\u0552\u0001\u0000\u0000"+
		"\u0000\u00ee\u0556\u0001\u0000\u0000\u0000\u00f0\u055b\u0001\u0000\u0000"+
		"\u0000\u00f2\u055d\u0001\u0000\u0000\u0000\u00f4\u0566\u0001\u0000\u0000"+
		"\u0000\u00f6\u0576\u0001\u0000\u0000\u0000\u00f8\u0579\u0001\u0000\u0000"+
		"\u0000\u00fa\u057d\u0001\u0000\u0000\u0000\u00fc\u0584\u0001\u0000\u0000"+
		"\u0000\u00fe\u0589\u0001\u0000\u0000\u0000\u0100\u0598\u0001\u0000\u0000"+
		"\u0000\u0102\u059c\u0001\u0000\u0000\u0000\u0104\u059e\u0001\u0000\u0000"+
		"\u0000\u0106\u05a0\u0001\u0000\u0000\u0000\u0108\u05a3\u0001\u0000\u0000"+
		"\u0000\u010a\u05a5\u0001\u0000\u0000\u0000\u010c\u05ab\u0001\u0000\u0000"+
		"\u0000\u010e\u05ad\u0001\u0000\u0000\u0000\u0110\u05b0\u0001\u0000\u0000"+
		"\u0000\u0112\u05b3\u0001\u0000\u0000\u0000\u0114\u05b8\u0001\u0000\u0000"+
		"\u0000\u0116\u05bc\u0001\u0000\u0000\u0000\u0118\u05c1\u0001\u0000\u0000"+
		"\u0000\u011a\u05c3\u0001\u0000\u0000\u0000\u011c\u05d2\u0001\u0000\u0000"+
		"\u0000\u011e\u05de\u0001\u0000\u0000\u0000\u0120\u05e1\u0001\u0000\u0000"+
		"\u0000\u0122\u05ed\u0001\u0000\u0000\u0000\u0124\u05fb\u0001\u0000\u0000"+
		"\u0000\u0126\u060a\u0001\u0000\u0000\u0000\u0128\u0619\u0001\u0000\u0000"+
		"\u0000\u012a\u0621\u0001\u0000\u0000\u0000\u012c\u062c\u0001\u0000\u0000"+
		"\u0000\u012e\u0636\u0001\u0000\u0000\u0000\u0130\u063e\u0001\u0000\u0000"+
		"\u0000\u0132\u0640\u0001\u0000\u0000\u0000\u0134\u0643\u0001\u0000\u0000"+
		"\u0000\u0136\u0656\u0001\u0000\u0000\u0000\u0138\u0659\u0001\u0000\u0000"+
		"\u0000\u013a\u065e\u0001\u0000\u0000\u0000\u013c\u0662\u0001\u0000\u0000"+
		"\u0000\u013e\u0665\u0001\u0000\u0000\u0000\u0140\u0668\u0001\u0000\u0000"+
		"\u0000\u0142\u0672\u0001\u0000\u0000\u0000\u0144\u0674\u0001\u0000\u0000"+
		"\u0000\u0146\u067e\u0001\u0000\u0000\u0000\u0148\u0685\u0001\u0000\u0000"+
		"\u0000\u014a\u0687\u0001\u0000\u0000\u0000\u014c\u0689\u0001\u0000\u0000"+
		"\u0000\u014e\u068b\u0001\u0000\u0000\u0000\u0150\u068e\u0001\u0000\u0000"+
		"\u0000\u0152\u0691\u0001\u0000\u0000\u0000\u0154\u069d\u0001\u0000\u0000"+
		"\u0000\u0156\u069f\u0001\u0000\u0000\u0000\u0158\u06a3\u0001\u0000\u0000"+
		"\u0000\u015a\u06b0\u0001\u0000\u0000\u0000\u015c\u06b2\u0001\u0000\u0000"+
		"\u0000\u015e\u06c0\u0001\u0000\u0000\u0000\u0160\u06c6\u0001\u0000\u0000"+
		"\u0000\u0162\u06c8\u0001\u0000\u0000\u0000\u0164\u06ce\u0001\u0000\u0000"+
		"\u0000\u0166\u06d1\u0001\u0000\u0000\u0000\u0168\u06d5\u0001\u0000\u0000"+
		"\u0000\u016a\u06de\u0001\u0000\u0000\u0000\u016c\u06e0\u0001\u0000\u0000"+
		"\u0000\u016e\u06e2\u0001\u0000\u0000\u0000\u0170\u06f0\u0001\u0000\u0000"+
		"\u0000\u0172\u06f3\u0001\u0000\u0000\u0000\u0174\u06f7\u0001\u0000\u0000"+
		"\u0000\u0176\u06fa\u0001\u0000\u0000\u0000\u0178\u06fe\u0001\u0000\u0000"+
		"\u0000\u017a\u070a\u0001\u0000\u0000\u0000\u017c\u0715\u0001\u0000\u0000"+
		"\u0000\u017e\u0717\u0001\u0000\u0000\u0000\u0180\u0725\u0001\u0000\u0000"+
		"\u0000\u0182\u0727\u0001\u0000\u0000\u0000\u0184\u072e\u0001\u0000\u0000"+
		"\u0000\u0186\u0732\u0001\u0000\u0000\u0000\u0188\u073a\u0001\u0000\u0000"+
		"\u0000\u018a\u073e\u0001\u0000\u0000\u0000\u018c\u0742\u0001\u0000\u0000"+
		"\u0000\u018e\u0746\u0001\u0000\u0000\u0000\u0190\u074e\u0001\u0000\u0000"+
		"\u0000\u0192\u075b\u0001\u0000\u0000\u0000\u0194\u075d\u0001\u0000\u0000"+
		"\u0000\u0196\u0762\u0001\u0000\u0000\u0000\u0198\u0772\u0001\u0000\u0000"+
		"\u0000\u019a\u0774\u0001\u0000\u0000\u0000\u019c\u0779\u0001\u0000\u0000"+
		"\u0000\u019e\u077b\u0001\u0000\u0000\u0000\u01a0\u077d\u0001\u0000\u0000"+
		"\u0000\u01a2\u077f\u0001\u0000\u0000\u0000\u01a4\u0781\u0001\u0000\u0000"+
		"\u0000\u01a6\u0786\u0001\u0000\u0000\u0000\u01a8\u078d\u0001\u0000\u0000"+
		"\u0000\u01aa\u0792\u0001\u0000\u0000\u0000\u01ac\u07a4\u0001\u0000\u0000"+
		"\u0000\u01ae\u07a6\u0001\u0000\u0000\u0000\u01b0\u07ab\u0001\u0000\u0000"+
		"\u0000\u01b2\u07b4\u0001\u0000\u0000\u0000\u01b4\u07b6\u0001\u0000\u0000"+
		"\u0000\u01b6\u07bb\u0001\u0000\u0000\u0000\u01b8\u07c0\u0001\u0000\u0000"+
		"\u0000\u01ba\u07c4\u0001\u0000\u0000\u0000\u01bc\u07cb\u0001\u0000\u0000"+
		"\u0000\u01be\u07cd\u0001\u0000\u0000\u0000\u01c0\u07d4\u0001\u0000\u0000"+
		"\u0000\u01c2\u07db\u0001\u0000\u0000\u0000\u01c4\u07e2\u0001\u0000\u0000"+
		"\u0000\u01c6\u07e9\u0001\u0000\u0000\u0000\u01c8\u07f2\u0001\u0000\u0000"+
		"\u0000\u01ca\u07f6\u0001\u0000\u0000\u0000\u01cc\u07fa\u0001\u0000\u0000"+
		"\u0000\u01ce\u0800\u0001\u0000\u0000\u0000\u01d0\u0804\u0001\u0000\u0000"+
		"\u0000\u01d2\u0806\u0001\u0000\u0000\u0000\u01d4\u0808\u0001\u0000\u0000"+
		"\u0000\u01d6\u080a\u0001\u0000\u0000\u0000\u01d8\u080c\u0001\u0000\u0000"+
		"\u0000\u01da\u0818\u0001\u0000\u0000\u0000\u01dc\u0826\u0001\u0000\u0000"+
		"\u0000\u01de\u0839\u0001\u0000\u0000\u0000\u01e0\u084c\u0001\u0000\u0000"+
		"\u0000\u01e2\u0850\u0001\u0000\u0000\u0000\u01e4\u01e6\u0003\u0002\u0001"+
		"\u0000\u01e5\u01e4\u0001\u0000\u0000\u0000\u01e5\u01e6\u0001\u0000\u0000"+
		"\u0000\u01e6\u01e8\u0001\u0000\u0000\u0000\u01e7\u01e9\u0003\u0004\u0002"+
		"\u0000\u01e8\u01e7\u0001\u0000\u0000\u0000\u01e8\u01e9\u0001\u0000\u0000"+
		"\u0000\u01e9\u01eb\u0001\u0000\u0000\u0000\u01ea\u01ec\u0003\u0002\u0001"+
		"\u0000\u01eb\u01ea\u0001\u0000\u0000\u0000\u01eb\u01ec\u0001\u0000\u0000"+
		"\u0000\u01ec\u01ef\u0001\u0000\u0000\u0000\u01ed\u01f0\u0003\n\u0005\u0000"+
		"\u01ee\u01f0\u0003\u0006\u0003\u0000\u01ef\u01ed\u0001\u0000\u0000\u0000"+
		"\u01ef\u01ee\u0001\u0000\u0000\u0000\u01f0\u0001\u0001\u0000\u0000\u0000"+
		"\u01f1\u01f2\u0005\u00bf\u0000\u0000\u01f2\u0003\u0001\u0000\u0000\u0000"+
		"\u01f3\u01f4\u0005\u00ab\u0000\u0000\u01f4\u01f5\u0005\u00a7\u0000\u0000"+
		"\u01f5\u01f8\u0003\u01dc\u00ee\u0000\u01f6\u01f7\u0005Z\u0000\u0000\u01f7"+
		"\u01f9\u0003\u01dc\u00ee\u0000\u01f8\u01f6\u0001\u0000\u0000\u0000\u01f8"+
		"\u01f9\u0001\u0000\u0000\u0000\u01f9\u01fa\u0001\u0000\u0000\u0000\u01fa"+
		"\u01fb\u0005#\u0000\u0000\u01fb\u0005\u0001\u0000\u0000\u0000\u01fc\u01fd"+
		"\u0003\u000e\u0007\u0000\u01fd\u01fe\u0003\b\u0004\u0000\u01fe\u0007\u0001"+
		"\u0000\u0000\u0000\u01ff\u0200\u0003L&\u0000\u0200\t\u0001\u0000\u0000"+
		"\u0000\u0201\u0202\u0003\f\u0006\u0000\u0202\u0203\u0003\u000e\u0007\u0000"+
		"\u0203\u000b\u0001\u0000\u0000\u0000\u0204\u0205\u0005x\u0000\u0000\u0205"+
		"\u0206\u0005y\u0000\u0000\u0206\u0207\u0003\u01cc\u00e6\u0000\u0207\u0208"+
		"\u0005\u0013\u0000\u0000\u0208\u0209\u0003\u01d6\u00eb\u0000\u0209\u020a"+
		"\u0005#\u0000\u0000\u020a\r\u0001\u0000\u0000\u0000\u020b\u0211\u0003"+
		"\u0012\t\u0000\u020c\u0211\u0003\u0014\n\u0000\u020d\u0211\u00030\u0018"+
		"\u0000\u020e\u0211\u0003*\u0015\u0000\u020f\u0211\u0003.\u0017\u0000\u0210"+
		"\u020b\u0001\u0000\u0000\u0000\u0210\u020c\u0001\u0000\u0000\u0000\u0210"+
		"\u020d\u0001\u0000\u0000\u0000\u0210\u020e\u0001\u0000\u0000\u0000\u0210"+
		"\u020f\u0001\u0000\u0000\u0000\u0211\u0212\u0001\u0000\u0000\u0000\u0212"+
		"\u0213\u0005#\u0000\u0000\u0213\u0215\u0001\u0000\u0000\u0000\u0214\u0210"+
		"\u0001\u0000\u0000\u0000\u0215\u0218\u0001\u0000\u0000\u0000\u0216\u0214"+
		"\u0001\u0000\u0000\u0000\u0216\u0217\u0001\u0000\u0000\u0000\u0217\u0221"+
		"\u0001\u0000\u0000\u0000\u0218\u0216\u0001\u0000\u0000\u0000\u0219\u021b"+
		"\u0003\u0002\u0001\u0000\u021a\u0219\u0001\u0000\u0000\u0000\u021a\u021b"+
		"\u0001\u0000\u0000\u0000\u021b\u021c\u0001\u0000\u0000\u0000\u021c\u021d"+
		"\u0003\u0010\b\u0000\u021d\u021e\u0005#\u0000\u0000\u021e\u0220\u0001"+
		"\u0000\u0000\u0000\u021f\u021a\u0001\u0000\u0000\u0000\u0220\u0223\u0001"+
		"\u0000\u0000\u0000\u0221\u021f\u0001\u0000\u0000\u0000\u0221\u0222\u0001"+
		"\u0000\u0000\u0000\u0222\u000f\u0001\u0000\u0000\u0000\u0223\u0221\u0001"+
		"\u0000\u0000\u0000\u0224\u0229\u00032\u0019\u0000\u0225\u0229\u0003:\u001d"+
		"\u0000\u0226\u0229\u00038\u001c\u0000\u0227\u0229\u0003J%\u0000\u0228"+
		"\u0224\u0001\u0000\u0000\u0000\u0228\u0225\u0001\u0000\u0000\u0000\u0228"+
		"\u0226\u0001\u0000\u0000\u0000\u0228\u0227\u0001\u0000\u0000\u0000\u0229"+
		"\u0011\u0001\u0000\u0000\u0000\u022a\u022b\u0005M\u0000\u0000\u022b\u022c"+
		"\u0005N\u0000\u0000\u022c\u022d\u0007\u0000\u0000\u0000\u022d\u022e\u0005"+
		"y\u0000\u0000\u022e\u022f\u0003\u01dc\u00ee\u0000\u022f\u0013\u0001\u0000"+
		"\u0000\u0000\u0230\u0239\u0003\u0016\u000b\u0000\u0231\u0239\u0003\u0018"+
		"\f\u0000\u0232\u0239\u0003\u001a\r\u0000\u0233\u0239\u0003\u001c\u000e"+
		"\u0000\u0234\u0239\u0003\u001e\u000f\u0000\u0235\u0239\u0003 \u0010\u0000"+
		"\u0236\u0239\u0003\"\u0011\u0000\u0237\u0239\u0003(\u0014\u0000\u0238"+
		"\u0230\u0001\u0000\u0000\u0000\u0238\u0231\u0001\u0000\u0000\u0000\u0238"+
		"\u0232\u0001\u0000\u0000\u0000\u0238\u0233\u0001\u0000\u0000\u0000\u0238"+
		"\u0234\u0001\u0000\u0000\u0000\u0238\u0235\u0001\u0000\u0000\u0000\u0238"+
		"\u0236\u0001\u0000\u0000\u0000\u0238\u0237\u0001\u0000\u0000\u0000\u0239"+
		"\u0015\u0001\u0000\u0000\u0000\u023a\u023b\u0005M\u0000\u0000\u023b\u023c"+
		"\u0005?\u0000\u0000\u023c\u023d\u0007\u0001\u0000\u0000\u023d\u0017\u0001"+
		"\u0000\u0000\u0000\u023e\u023f\u0005M\u0000\u0000\u023f\u0240\u0005N\u0000"+
		"\u0000\u0240\u0241\u0005G\u0000\u0000\u0241\u0242\u0003\u01d6\u00eb\u0000"+
		"\u0242\u0019\u0001\u0000\u0000\u0000\u0243\u0244\u0005M\u0000\u0000\u0244"+
		"\u0245\u0005>\u0000\u0000\u0245\u0246\u0003\u01d6\u00eb\u0000\u0246\u001b"+
		"\u0001\u0000\u0000\u0000\u0247\u0248\u0005M\u0000\u0000\u0248\u0249\u0005"+
		"I\u0000\u0000\u0249\u024a\u0007\u0001\u0000\u0000\u024a\u001d\u0001\u0000"+
		"\u0000\u0000\u024b\u024c\u0005M\u0000\u0000\u024c\u024d\u0005\u0086\u0000"+
		"\u0000\u024d\u024e\u0007\u0002\u0000\u0000\u024e\u001f\u0001\u0000\u0000"+
		"\u0000\u024f\u0250\u0005M\u0000\u0000\u0250\u0251\u0005N\u0000\u0000\u0251"+
		"\u0252\u0005\u0084\u0000\u0000\u0252\u0253\u0005X\u0000\u0000\u0253\u0254"+
		"\u0007\u0003\u0000\u0000\u0254!\u0001\u0000\u0000\u0000\u0255\u0256\u0005"+
		"M\u0000\u0000\u0256\u0257\u0005K\u0000\u0000\u0257\u0258\u0003$\u0012"+
		"\u0000\u0258\u0259\u0005\u001e\u0000\u0000\u0259\u025a\u0003&\u0013\u0000"+
		"\u025a#\u0001\u0000\u0000\u0000\u025b\u025c\u0007\u0004\u0000\u0000\u025c"+
		"%\u0001\u0000\u0000\u0000\u025d\u025e\u0007\u0005\u0000\u0000\u025e\'"+
		"\u0001\u0000\u0000\u0000\u025f\u0264\u0005M\u0000\u0000\u0260\u0261\u0005"+
		"R\u0000\u0000\u0261\u0265\u0003\u01c8\u00e4\u0000\u0262\u0263\u0005N\u0000"+
		"\u0000\u0263\u0265\u0005R\u0000\u0000\u0264\u0260\u0001\u0000\u0000\u0000"+
		"\u0264\u0262\u0001\u0000\u0000\u0000\u0265\u026b\u0001\u0000\u0000\u0000"+
		"\u0266\u0267\u0005\b\u0000\u0000\u0267\u0268\u0005\u0013\u0000\u0000\u0268"+
		"\u026a\u0003\u01dc\u00ee\u0000\u0269\u0266\u0001\u0000\u0000\u0000\u026a"+
		"\u026d\u0001\u0000\u0000\u0000\u026b\u0269\u0001\u0000\u0000\u0000\u026b"+
		"\u026c\u0001\u0000\u0000\u0000\u026c)\u0001\u0000\u0000\u0000\u026d\u026b"+
		"\u0001\u0000\u0000\u0000\u026e\u026f\u0005j\u0000\u0000\u026f\u0271\u0005"+
		"\u008f\u0000\u0000\u0270\u0272\u0003,\u0016\u0000\u0271\u0270\u0001\u0000"+
		"\u0000\u0000\u0271\u0272\u0001\u0000\u0000\u0000\u0272\u0273\u0001\u0000"+
		"\u0000\u0000\u0273\u027d\u0003\u01d6\u00eb\u0000\u0274\u0275\u0005<\u0000"+
		"\u0000\u0275\u027a\u0003\u01d6\u00eb\u0000\u0276\u0277\u0005\u001e\u0000"+
		"\u0000\u0277\u0279\u0003\u01d6\u00eb\u0000\u0278\u0276\u0001\u0000\u0000"+
		"\u0000\u0279\u027c\u0001\u0000\u0000\u0000\u027a\u0278\u0001\u0000\u0000"+
		"\u0000\u027a\u027b\u0001\u0000\u0000\u0000\u027b\u027e\u0001\u0000\u0000"+
		"\u0000\u027c\u027a\u0001\u0000\u0000\u0000\u027d\u0274\u0001\u0000\u0000"+
		"\u0000\u027d\u027e\u0001\u0000\u0000\u0000\u027e+\u0001\u0000\u0000\u0000"+
		"\u027f\u0280\u0005y\u0000\u0000\u0280\u0281\u0003\u01cc\u00e6\u0000\u0281"+
		"\u0282\u0005\u0013\u0000\u0000\u0282\u0287\u0001\u0000\u0000\u0000\u0283"+
		"\u0284\u0005N\u0000\u0000\u0284\u0285\u0005V\u0000\u0000\u0285\u0287\u0005"+
		"y\u0000\u0000\u0286\u027f\u0001\u0000\u0000\u0000\u0286\u0283\u0001\u0000"+
		"\u0000\u0000\u0287-\u0001\u0000\u0000\u0000\u0288\u0289\u0005j\u0000\u0000"+
		"\u0289\u028e\u0005x\u0000\u0000\u028a\u028b\u0005y\u0000\u0000\u028b\u028c"+
		"\u0003\u01cc\u00e6\u0000\u028c\u028d\u0005\u0013\u0000\u0000\u028d\u028f"+
		"\u0001\u0000\u0000\u0000\u028e\u028a\u0001\u0000\u0000\u0000\u028e\u028f"+
		"\u0001\u0000\u0000\u0000\u028f\u0290\u0001\u0000\u0000\u0000\u0290\u029a"+
		"\u0003\u01d6\u00eb\u0000\u0291\u0292\u0005<\u0000\u0000\u0292\u0297\u0003"+
		"\u01d6\u00eb\u0000\u0293\u0294\u0005\u001e\u0000\u0000\u0294\u0296\u0003"+
		"\u01d6\u00eb\u0000\u0295\u0293\u0001\u0000\u0000\u0000\u0296\u0299\u0001"+
		"\u0000\u0000\u0000\u0297\u0295\u0001\u0000\u0000\u0000\u0297\u0298\u0001"+
		"\u0000\u0000\u0000\u0298\u029b\u0001\u0000\u0000\u0000\u0299\u0297\u0001"+
		"\u0000\u0000\u0000\u029a\u0291\u0001\u0000\u0000\u0000\u029a\u029b\u0001"+
		"\u0000\u0000\u0000\u029b/\u0001\u0000\u0000\u0000\u029c\u029d\u0005M\u0000"+
		"\u0000\u029d\u029e\u0005y\u0000\u0000\u029e\u029f\u0003\u01cc\u00e6\u0000"+
		"\u029f\u02a0\u0005\u0013\u0000\u0000\u02a0\u02a1\u0003\u01d6\u00eb\u0000"+
		"\u02a11\u0001\u0000\u0000\u0000\u02a2\u02a3\u0005M\u0000\u0000\u02a3\u02a4"+
		"\u0003@ \u0000\u02a4\u02a5\u0005\u00a6\u0000\u0000\u02a5\u02a6\u0005,"+
		"\u0000\u0000\u02a6\u02a8\u0003\u0108\u0084\u0000\u02a7\u02a9\u0003\u0178"+
		"\u00bc\u0000\u02a8\u02a7\u0001\u0000\u0000\u0000\u02a8\u02a9\u0001\u0000"+
		"\u0000\u0000\u02a9\u02b1\u0001\u0000\u0000\u0000\u02aa\u02ab\u0005\"\u0000"+
		"\u0000\u02ab\u02b2\u00034\u001a\u0000\u02ac\u02af\u0005_\u0000\u0000\u02ad"+
		"\u02ae\u0005\"\u0000\u0000\u02ae\u02b0\u00036\u001b\u0000\u02af\u02ad"+
		"\u0001\u0000\u0000\u0000\u02af\u02b0\u0001\u0000\u0000\u0000\u02b0\u02b2"+
		"\u0001\u0000\u0000\u0000\u02b1\u02aa\u0001\u0000\u0000\u0000\u02b1\u02ac"+
		"\u0001\u0000\u0000\u0000\u02b23\u0001\u0000\u0000\u0000\u02b3\u02b4\u0003"+
		"N\'\u0000\u02b45\u0001\u0000\u0000\u0000\u02b5\u02b6\u0003N\'\u0000\u02b6"+
		"7\u0001\u0000\u0000\u0000\u02b7\u02b8\u0005M\u0000\u0000\u02b8\u02b9\u0005"+
		"J\u0000\u0000\u02b9\u02bc\u0005p\u0000\u0000\u02ba\u02bb\u0005:\u0000"+
		"\u0000\u02bb\u02bd\u0003\u017c\u00be\u0000\u02bc\u02ba\u0001\u0000\u0000"+
		"\u0000\u02bc\u02bd\u0001\u0000\u0000\u0000\u02bd\u02c5\u0001\u0000\u0000"+
		"\u0000\u02be\u02bf\u0005\"\u0000\u0000\u02bf\u02c6\u0003N\'\u0000\u02c0"+
		"\u02c3\u0005_\u0000\u0000\u02c1\u02c2\u0005\"\u0000\u0000\u02c2\u02c4"+
		"\u0003N\'\u0000\u02c3\u02c1\u0001\u0000\u0000\u0000\u02c3\u02c4\u0001"+
		"\u0000\u0000\u0000\u02c4\u02c6\u0001\u0000\u0000\u0000\u02c5\u02be\u0001"+
		"\u0000\u0000\u0000\u02c5\u02c0\u0001\u0000\u0000\u0000\u02c69\u0001\u0000"+
		"\u0000\u0000\u02c7\u02c8\u0005M\u0000\u0000\u02c8\u02c9\u0003@ \u0000"+
		"\u02c9\u02ca\u0005c\u0000\u0000\u02ca\u02cb\u0003\u01c8\u00e4\u0000\u02cb"+
		"\u02cd\u0005\u0015\u0000\u0000\u02cc\u02ce\u0003<\u001e\u0000\u02cd\u02cc"+
		"\u0001\u0000\u0000\u0000\u02cd\u02ce\u0001\u0000\u0000\u0000\u02ce\u02cf"+
		"\u0001\u0000\u0000\u0000\u02cf\u02d1\u0005\u0016\u0000\u0000\u02d0\u02d2"+
		"\u0003H$\u0000\u02d1\u02d0\u0001\u0000\u0000\u0000\u02d1\u02d2\u0001\u0000"+
		"\u0000\u0000\u02d2\u02d5\u0001\u0000\u0000\u0000\u02d3\u02d6\u0003\u015a"+
		"\u00ad\u0000\u02d4\u02d6\u0005_\u0000\u0000\u02d5\u02d3\u0001\u0000\u0000"+
		"\u0000\u02d5\u02d4\u0001\u0000\u0000\u0000\u02d6;\u0001\u0000\u0000\u0000"+
		"\u02d7\u02dc\u0003>\u001f\u0000\u02d8\u02d9\u0005\u001e\u0000\u0000\u02d9"+
		"\u02db\u0003>\u001f\u0000\u02da\u02d8\u0001\u0000\u0000\u0000\u02db\u02de"+
		"\u0001\u0000\u0000\u0000\u02dc\u02da\u0001\u0000\u0000\u0000\u02dc\u02dd"+
		"\u0001\u0000\u0000\u0000\u02dd=\u0001\u0000\u0000\u0000\u02de\u02dc\u0001"+
		"\u0000\u0000\u0000\u02df\u02e0\u0005,\u0000\u0000\u02e0\u02e2\u0003\u01ca"+
		"\u00e5\u0000\u02e1\u02e3\u0003\u0178\u00bc\u0000\u02e2\u02e1\u0001\u0000"+
		"\u0000\u0000\u02e2\u02e3\u0001\u0000\u0000\u0000\u02e3?\u0001\u0000\u0000"+
		"\u0000\u02e4\u02e6\u0003B!\u0000\u02e5\u02e4\u0001\u0000\u0000\u0000\u02e6"+
		"\u02e9\u0001\u0000\u0000\u0000\u02e7\u02e5\u0001\u0000\u0000\u0000\u02e7"+
		"\u02e8\u0001\u0000\u0000\u0000\u02e8A\u0001\u0000\u0000\u0000\u02e9\u02e7"+
		"\u0001\u0000\u0000\u0000\u02ea\u02eb\u0005-\u0000\u0000\u02eb\u02f0\u0003"+
		"\u01ca\u00e5\u0000\u02ec\u02ed\u0005\u0015\u0000\u0000\u02ed\u02ee\u0003"+
		"D\"\u0000\u02ee\u02ef\u0005\u0016\u0000\u0000\u02ef\u02f1\u0001\u0000"+
		"\u0000\u0000\u02f0\u02ec\u0001\u0000\u0000\u0000\u02f0\u02f1\u0001\u0000"+
		"\u0000\u0000\u02f1C\u0001\u0000\u0000\u0000\u02f2\u02f7\u0003F#\u0000"+
		"\u02f3\u02f4\u0005\u001e\u0000\u0000\u02f4\u02f6\u0003F#\u0000\u02f5\u02f3"+
		"\u0001\u0000\u0000\u0000\u02f6\u02f9\u0001\u0000\u0000\u0000\u02f7\u02f5"+
		"\u0001\u0000\u0000\u0000\u02f7\u02f8\u0001\u0000\u0000\u0000\u02f8E\u0001"+
		"\u0000\u0000\u0000\u02f9\u02f7\u0001\u0000\u0000\u0000\u02fa\u02fb\u0003"+
		"\u0102\u0081\u0000\u02fbG\u0001\u0000\u0000\u0000\u02fc\u02fd\u0005:\u0000"+
		"\u0000\u02fd\u02fe\u0003\u017a\u00bd\u0000\u02feI\u0001\u0000\u0000\u0000"+
		"\u02ff\u0300\u0005M\u0000\u0000\u0300\u0301\u0005\u0082\u0000\u0000\u0301"+
		"\u0302\u0003\u01ca\u00e5\u0000\u0302\u0303\u0003\u01dc\u00ee\u0000\u0303"+
		"K\u0001\u0000\u0000\u0000\u0304\u0309\u0003N\'\u0000\u0305\u0306\u0005"+
		"\u001e\u0000\u0000\u0306\u0308\u0003N\'\u0000\u0307\u0305\u0001\u0000"+
		"\u0000\u0000\u0308\u030b\u0001\u0000\u0000\u0000\u0309\u0307\u0001\u0000"+
		"\u0000\u0000\u0309\u030a\u0001\u0000\u0000\u0000\u030aM\u0001\u0000\u0000"+
		"\u0000\u030b\u0309\u0001\u0000\u0000\u0000\u030c\u0315\u0003P(\u0000\u030d"+
		"\u0315\u0003~?\u0000\u030e\u0315\u0003\u0082A\u0000\u030f\u0315\u0003"+
		"\u0088D\u0000\u0310\u0315\u0003\u009cN\u0000\u0311\u0315\u0003\u008eG"+
		"\u0000\u0312\u0315\u0003\u0090H\u0000\u0313\u0315\u0003\u00a8T\u0000\u0314"+
		"\u030c\u0001\u0000\u0000\u0000\u0314\u030d\u0001\u0000\u0000\u0000\u0314"+
		"\u030e\u0001\u0000\u0000\u0000\u0314\u030f\u0001\u0000\u0000\u0000\u0314"+
		"\u0310\u0001\u0000\u0000\u0000\u0314\u0311\u0001\u0000\u0000\u0000\u0314"+
		"\u0312\u0001\u0000\u0000\u0000\u0314\u0313\u0001\u0000\u0000\u0000\u0315"+
		"O\u0001\u0000\u0000\u0000\u0316\u031a\u0003R)\u0000\u0317\u0319\u0003"+
		"T*\u0000\u0318\u0317\u0001\u0000\u0000\u0000\u0319\u031c\u0001\u0000\u0000"+
		"\u0000\u031a\u0318\u0001\u0000\u0000\u0000\u031a\u031b\u0001\u0000\u0000"+
		"\u0000\u031b\u031d\u0001\u0000\u0000\u0000\u031c\u031a\u0001\u0000\u0000"+
		"\u0000\u031d\u031e\u0003|>\u0000\u031eQ\u0001\u0000\u0000\u0000\u031f"+
		"\u0323\u0003V+\u0000\u0320\u0323\u0003^/\u0000\u0321\u0323\u0003b1\u0000"+
		"\u0322\u031f\u0001\u0000\u0000\u0000\u0322\u0320\u0001\u0000\u0000\u0000"+
		"\u0322\u0321\u0001\u0000\u0000\u0000\u0323S\u0001\u0000\u0000\u0000\u0324"+
		"\u032a\u0003R)\u0000\u0325\u032a\u0003p8\u0000\u0326\u032a\u0003r9\u0000"+
		"\u0327\u032a\u0003x<\u0000\u0328\u032a\u0003n7\u0000\u0329\u0324\u0001"+
		"\u0000\u0000\u0000\u0329\u0325\u0001\u0000\u0000\u0000\u0329\u0326\u0001"+
		"\u0000\u0000\u0000\u0329\u0327\u0001\u0000\u0000\u0000\u0329\u0328\u0001"+
		"\u0000\u0000\u0000\u032aU\u0001\u0000\u0000\u0000\u032b\u032c\u0005b\u0000"+
		"\u0000\u032c\u0331\u0003X,\u0000\u032d\u032e\u0005\u001e\u0000\u0000\u032e"+
		"\u0330\u0003X,\u0000\u032f\u032d\u0001\u0000\u0000\u0000\u0330\u0333\u0001"+
		"\u0000\u0000\u0000\u0331\u032f\u0001\u0000\u0000\u0000\u0331\u0332\u0001"+
		"\u0000\u0000\u0000\u0332W\u0001\u0000\u0000\u0000\u0333\u0331\u0001\u0000"+
		"\u0000\u0000\u0334\u0335\u0005,\u0000\u0000\u0335\u0337\u0003\u0108\u0084"+
		"\u0000\u0336\u0338\u0003\u0178\u00bc\u0000\u0337\u0336\u0001\u0000\u0000"+
		"\u0000\u0337\u0338\u0001\u0000\u0000\u0000\u0338\u033a\u0001\u0000\u0000"+
		"\u0000\u0339\u033b\u0003Z-\u0000\u033a\u0339\u0001\u0000\u0000\u0000\u033a"+
		"\u033b\u0001\u0000\u0000\u0000\u033b\u033d\u0001\u0000\u0000\u0000\u033c"+
		"\u033e\u0003\\.\u0000\u033d\u033c\u0001\u0000\u0000\u0000\u033d\u033e"+
		"\u0001\u0000\u0000\u0000\u033e\u033f\u0001\u0000\u0000\u0000\u033f\u0340"+
		"\u0005k\u0000\u0000\u0340\u0341\u0003N\'\u0000\u0341Y\u0001\u0000\u0000"+
		"\u0000\u0342\u0343\u00055\u0000\u0000\u0343\u0344\u0005X\u0000\u0000\u0344"+
		"[\u0001\u0000\u0000\u0000\u0345\u0346\u0005<\u0000\u0000\u0346\u0347\u0005"+
		",\u0000\u0000\u0347\u0348\u0003\u0108\u0084\u0000\u0348]\u0001\u0000\u0000"+
		"\u0000\u0349\u034a\u0005t\u0000\u0000\u034a\u034f\u0003`0\u0000\u034b"+
		"\u034c\u0005\u001e\u0000\u0000\u034c\u034e\u0003`0\u0000\u034d\u034b\u0001"+
		"\u0000\u0000\u0000\u034e\u0351\u0001\u0000\u0000\u0000\u034f\u034d\u0001"+
		"\u0000\u0000\u0000\u034f\u0350\u0001\u0000\u0000\u0000\u0350_\u0001\u0000"+
		"\u0000\u0000\u0351\u034f\u0001\u0000\u0000\u0000\u0352\u0353\u0005,\u0000"+
		"\u0000\u0353\u0355\u0003\u0108\u0084\u0000\u0354\u0356\u0003\u0178\u00bc"+
		"\u0000\u0355\u0354\u0001\u0000\u0000\u0000\u0355\u0356\u0001\u0000\u0000"+
		"\u0000\u0356\u0357\u0001\u0000\u0000\u0000\u0357\u0358\u0005\"\u0000\u0000"+
		"\u0358\u0359\u0003N\'\u0000\u0359a\u0001\u0000\u0000\u0000\u035a\u035d"+
		"\u0005b\u0000\u0000\u035b\u035e\u0003d2\u0000\u035c\u035e\u0003f3\u0000"+
		"\u035d\u035b\u0001\u0000\u0000\u0000\u035d\u035c\u0001\u0000\u0000\u0000"+
		"\u035ec\u0001\u0000\u0000\u0000\u035f\u0360\u0005\u009f\u0000\u0000\u0360"+
		"\u0361\u0005\u00aa\u0000\u0000\u0361\u0362\u0005,\u0000\u0000\u0362\u0364"+
		"\u0003\u01ca\u00e5\u0000\u0363\u0365\u0003\u0178\u00bc\u0000\u0364\u0363"+
		"\u0001\u0000\u0000\u0000\u0364\u0365\u0001\u0000\u0000\u0000\u0365\u0366"+
		"\u0001\u0000\u0000\u0000\u0366\u0367\u0005k\u0000\u0000\u0367\u0368\u0003"+
		"N\'\u0000\u0368\u036a\u0003h4\u0000\u0369\u036b\u0003j5\u0000\u036a\u0369"+
		"\u0001\u0000\u0000\u0000\u036a\u036b\u0001\u0000\u0000\u0000\u036be\u0001"+
		"\u0000\u0000\u0000\u036c\u036d\u0005\u0093\u0000\u0000\u036d\u036e\u0005"+
		"\u00aa\u0000\u0000\u036e\u036f\u0005,\u0000\u0000\u036f\u0371\u0003\u01ca"+
		"\u00e5\u0000\u0370\u0372\u0003\u0178\u00bc\u0000\u0371\u0370\u0001\u0000"+
		"\u0000\u0000\u0371\u0372\u0001\u0000\u0000\u0000\u0372\u0373\u0001\u0000"+
		"\u0000\u0000\u0373\u0374\u0005k\u0000\u0000\u0374\u0375\u0003N\'\u0000"+
		"\u0375\u0376\u0003h4\u0000\u0376\u0377\u0003j5\u0000\u0377g\u0001\u0000"+
		"\u0000\u0000\u0378\u0379\u0005\u0096\u0000\u0000\u0379\u037a\u0003l6\u0000"+
		"\u037a\u037b\u0005\u00a8\u0000\u0000\u037b\u037c\u0003N\'\u0000\u037c"+
		"i\u0001\u0000\u0000\u0000\u037d\u037f\u0005\u0081\u0000\u0000\u037e\u037d"+
		"\u0001\u0000\u0000\u0000\u037e\u037f\u0001\u0000\u0000\u0000\u037f\u0380"+
		"\u0001\u0000\u0000\u0000\u0380\u0381\u0005[\u0000\u0000\u0381\u0382\u0003"+
		"l6\u0000\u0382\u0383\u0005\u00a8\u0000\u0000\u0383\u0384\u0003N\'\u0000"+
		"\u0384k\u0001\u0000\u0000\u0000\u0385\u0386\u0005,\u0000\u0000\u0386\u0388"+
		"\u0003\u01c8\u00e4\u0000\u0387\u0385\u0001\u0000\u0000\u0000\u0387\u0388"+
		"\u0001\u0000\u0000\u0000\u0388\u038a\u0001\u0000\u0000\u0000\u0389\u038b"+
		"\u0003\\.\u0000\u038a\u0389\u0001\u0000\u0000\u0000\u038a\u038b\u0001"+
		"\u0000\u0000\u0000\u038b\u038f\u0001\u0000\u0000\u0000\u038c\u038d\u0005"+
		"\u008b\u0000\u0000\u038d\u038e\u0005,\u0000\u0000\u038e\u0390\u0003\u01c8"+
		"\u00e4\u0000\u038f\u038c\u0001\u0000\u0000\u0000\u038f\u0390\u0001\u0000"+
		"\u0000\u0000\u0390\u0394\u0001\u0000\u0000\u0000\u0391\u0392\u0005{\u0000"+
		"\u0000\u0392\u0393\u0005,\u0000\u0000\u0393\u0395\u0003\u01c8\u00e4\u0000"+
		"\u0394\u0391\u0001\u0000\u0000\u0000\u0394\u0395\u0001\u0000\u0000\u0000"+
		"\u0395m\u0001\u0000\u0000\u0000\u0396\u0397\u0005L\u0000\u0000\u0397\u0398"+
		"\u0005,\u0000\u0000\u0398\u0399\u0003\u0108\u0084\u0000\u0399o\u0001\u0000"+
		"\u0000\u0000\u039a\u039b\u0005\u00a9\u0000\u0000\u039b\u039c\u0003N\'"+
		"\u0000\u039cq\u0001\u0000\u0000\u0000\u039d\u039e\u0005f\u0000\u0000\u039e"+
		"\u039f\u0005A\u0000\u0000\u039f\u03a0\u0003t:\u0000\u03a0s\u0001\u0000"+
		"\u0000\u0000\u03a1\u03a6\u0003v;\u0000\u03a2\u03a3\u0005\u001e\u0000\u0000"+
		"\u03a3\u03a5\u0003v;\u0000\u03a4\u03a2\u0001\u0000\u0000\u0000\u03a5\u03a8"+
		"\u0001\u0000\u0000\u0000\u03a6\u03a4\u0001\u0000\u0000\u0000\u03a6\u03a7"+
		"\u0001\u0000\u0000\u0000\u03a7u\u0001\u0000\u0000\u0000\u03a8\u03a6\u0001"+
		"\u0000\u0000\u0000\u03a9\u03aa\u0005,\u0000\u0000\u03aa\u03b0\u0003\u0108"+
		"\u0084\u0000\u03ab\u03ad\u0003\u0178\u00bc\u0000\u03ac\u03ab\u0001\u0000"+
		"\u0000\u0000\u03ac\u03ad\u0001\u0000\u0000\u0000\u03ad\u03ae\u0001\u0000"+
		"\u0000\u0000\u03ae\u03af\u0005\"\u0000\u0000\u03af\u03b1\u0003N\'\u0000"+
		"\u03b0\u03ac\u0001\u0000\u0000\u0000\u03b0\u03b1\u0001\u0000\u0000\u0000"+
		"\u03b1\u03b4\u0001\u0000\u0000\u0000\u03b2\u03b3\u0005G\u0000\u0000\u03b3"+
		"\u03b5\u0003\u01d6\u00eb\u0000\u03b4\u03b2\u0001\u0000\u0000\u0000\u03b4"+
		"\u03b5\u0001\u0000\u0000\u0000\u03b5w\u0001\u0000\u0000\u0000\u03b6\u03b8"+
		"\u0005\u0095\u0000\u0000\u03b7\u03b6\u0001\u0000\u0000\u0000\u03b7\u03b8"+
		"\u0001\u0000\u0000\u0000\u03b8\u03b9\u0001\u0000\u0000\u0000\u03b9\u03ba"+
		"\u0005\u0084\u0000\u0000\u03ba\u03bb\u0005A\u0000\u0000\u03bb\u03c0\u0003"+
		"z=\u0000\u03bc\u03bd\u0005\u001e\u0000\u0000\u03bd\u03bf\u0003z=\u0000"+
		"\u03be\u03bc\u0001\u0000\u0000\u0000\u03bf\u03c2\u0001\u0000\u0000\u0000"+
		"\u03c0\u03be\u0001\u0000\u0000\u0000\u03c0\u03c1\u0001\u0000\u0000\u0000"+
		"\u03c1y\u0001\u0000\u0000\u0000\u03c2\u03c0\u0001\u0000\u0000\u0000\u03c3"+
		"\u03c6\u0003N\'\u0000\u03c4\u03c7\u0005;\u0000\u0000\u03c5\u03c7\u0005"+
		"Q\u0000\u0000\u03c6\u03c4\u0001\u0000\u0000\u0000\u03c6\u03c5\u0001\u0000"+
		"\u0000\u0000\u03c6\u03c7\u0001\u0000\u0000\u0000\u03c7\u03cd\u0001\u0000"+
		"\u0000\u0000\u03c8\u03cb\u0005X\u0000\u0000\u03c9\u03cc\u0005e\u0000\u0000"+
		"\u03ca\u03cc\u0005s\u0000\u0000\u03cb\u03c9\u0001\u0000\u0000\u0000\u03cb"+
		"\u03ca\u0001\u0000\u0000\u0000\u03cc\u03ce\u0001\u0000\u0000\u0000\u03cd"+
		"\u03c8\u0001\u0000\u0000\u0000\u03cd\u03ce\u0001\u0000\u0000\u0000\u03ce"+
		"\u03d1\u0001\u0000\u0000\u0000\u03cf\u03d0\u0005G\u0000\u0000\u03d0\u03d2"+
		"\u0003\u01d6\u00eb\u0000\u03d1\u03cf\u0001\u0000\u0000\u0000\u03d1\u03d2"+
		"\u0001\u0000\u0000\u0000\u03d2{\u0001\u0000\u0000\u0000\u03d3\u03d4\u0005"+
		"\u008d\u0000\u0000\u03d4\u03d5\u0003N\'\u0000\u03d5}\u0001\u0000\u0000"+
		"\u0000\u03d6\u03d9\u0005\u0094\u0000\u0000\u03d7\u03d9\u0005]\u0000\u0000"+
		"\u03d8\u03d6\u0001\u0000\u0000\u0000\u03d8\u03d7\u0001\u0000\u0000\u0000"+
		"\u03d9\u03da\u0001\u0000\u0000\u0000\u03da\u03df\u0003\u0080@\u0000\u03db"+
		"\u03dc\u0005\u001e\u0000\u0000\u03dc\u03de\u0003\u0080@\u0000\u03dd\u03db"+
		"\u0001\u0000\u0000\u0000\u03de\u03e1\u0001\u0000\u0000\u0000\u03df\u03dd"+
		"\u0001\u0000\u0000\u0000\u03df\u03e0\u0001\u0000\u0000\u0000\u03e0\u03e2"+
		"\u0001\u0000\u0000\u0000\u03e1\u03df\u0001\u0000\u0000\u0000\u03e2\u03e3"+
		"\u0005\u008e\u0000\u0000\u03e3\u03e4\u0003N\'\u0000\u03e4\u007f\u0001"+
		"\u0000\u0000\u0000\u03e5\u03e6\u0005,\u0000\u0000\u03e6\u03e8\u0003\u0108"+
		"\u0084\u0000\u03e7\u03e9\u0003\u0178\u00bc\u0000\u03e8\u03e7\u0001\u0000"+
		"\u0000\u0000\u03e8\u03e9\u0001\u0000\u0000\u0000\u03e9\u03ea\u0001\u0000"+
		"\u0000\u0000\u03ea\u03eb\u0005k\u0000\u0000\u03eb\u03ec\u0003N\'\u0000"+
		"\u03ec\u0081\u0001\u0000\u0000\u0000\u03ed\u03ee\u0005\u0099\u0000\u0000"+
		"\u03ee\u03ef\u0005\u0015\u0000\u0000\u03ef\u03f0\u0003L&\u0000\u03f0\u03f2"+
		"\u0005\u0016\u0000\u0000\u03f1\u03f3\u0003\u0084B\u0000\u03f2\u03f1\u0001"+
		"\u0000\u0000\u0000\u03f3\u03f4\u0001\u0000\u0000\u0000\u03f4\u03f2\u0001"+
		"\u0000\u0000\u0000\u03f4\u03f5\u0001\u0000\u0000\u0000\u03f5\u03f6\u0001"+
		"\u0000\u0000\u0000\u03f6\u03f7\u0005N\u0000\u0000\u03f7\u03f8\u0005\u008d"+
		"\u0000\u0000\u03f8\u03f9\u0003N\'\u0000\u03f9\u0083\u0001\u0000\u0000"+
		"\u0000\u03fa\u03fb\u0005B\u0000\u0000\u03fb\u03fd\u0003\u0086C\u0000\u03fc"+
		"\u03fa\u0001\u0000\u0000\u0000\u03fd\u03fe\u0001\u0000\u0000\u0000\u03fe"+
		"\u03fc\u0001\u0000\u0000\u0000\u03fe\u03ff\u0001\u0000\u0000\u0000\u03ff"+
		"\u0400\u0001\u0000\u0000\u0000\u0400\u0401\u0005\u008d\u0000\u0000\u0401"+
		"\u0402\u0003N\'\u0000\u0402\u0085\u0001\u0000\u0000\u0000\u0403\u0404"+
		"\u0003N\'\u0000\u0404\u0087\u0001\u0000\u0000\u0000\u0405\u0406\u0005"+
		"\u00a1\u0000\u0000\u0406\u0407\u0005\u0015\u0000\u0000\u0407\u0408\u0003"+
		"L&\u0000\u0408\u040a\u0005\u0016\u0000\u0000\u0409\u040b\u0003\u008aE"+
		"\u0000\u040a\u0409\u0001\u0000\u0000\u0000\u040b\u040c\u0001\u0000\u0000"+
		"\u0000\u040c\u040a\u0001\u0000\u0000\u0000\u040c\u040d\u0001\u0000\u0000"+
		"\u0000\u040d\u040e\u0001\u0000\u0000\u0000\u040e\u0411\u0005N\u0000\u0000"+
		"\u040f\u0410\u0005,\u0000\u0000\u0410\u0412\u0003\u0108\u0084\u0000\u0411"+
		"\u040f\u0001\u0000\u0000\u0000\u0411\u0412\u0001\u0000\u0000\u0000\u0412"+
		"\u0413\u0001\u0000\u0000\u0000\u0413\u0414\u0005\u008d\u0000\u0000\u0414"+
		"\u0415\u0003N\'\u0000\u0415\u0089\u0001\u0000\u0000\u0000\u0416\u041b"+
		"\u0005B\u0000\u0000\u0417\u0418\u0005,\u0000\u0000\u0418\u0419\u0003\u0108"+
		"\u0084\u0000\u0419\u041a\u0005:\u0000\u0000\u041a\u041c\u0001\u0000\u0000"+
		"\u0000\u041b\u0417\u0001\u0000\u0000\u0000\u041b\u041c\u0001\u0000\u0000"+
		"\u0000\u041c\u041d\u0001\u0000\u0000\u0000\u041d\u041e\u0003\u008cF\u0000"+
		"\u041e\u041f\u0005\u008d\u0000\u0000\u041f\u0420\u0003N\'\u0000\u0420"+
		"\u008b\u0001\u0000\u0000\u0000\u0421\u0426\u0003\u017a\u00bd\u0000\u0422"+
		"\u0423\u0005\'\u0000\u0000\u0423\u0425\u0003\u017a\u00bd\u0000\u0424\u0422"+
		"\u0001\u0000\u0000\u0000\u0425\u0428\u0001\u0000\u0000\u0000\u0426\u0424"+
		"\u0001\u0000\u0000\u0000\u0426\u0427\u0001\u0000\u0000\u0000\u0427\u008d"+
		"\u0001\u0000\u0000\u0000\u0428\u0426\u0001\u0000\u0000\u0000\u0429\u042a"+
		"\u0005i\u0000\u0000\u042a\u042b\u0005\u0015\u0000\u0000\u042b\u042c\u0003"+
		"L&\u0000\u042c\u042d\u0005\u0016\u0000\u0000\u042d\u042e\u0005\u009b\u0000"+
		"\u0000\u042e\u042f\u0003N\'\u0000\u042f\u0430\u0005W\u0000\u0000\u0430"+
		"\u0431\u0003N\'\u0000\u0431\u008f\u0001\u0000\u0000\u0000\u0432\u0434"+
		"\u0003\u0092I\u0000\u0433\u0435\u0003\u0096K\u0000\u0434\u0433\u0001\u0000"+
		"\u0000\u0000\u0435\u0436\u0001\u0000\u0000\u0000\u0436\u0434\u0001\u0000"+
		"\u0000\u0000\u0436\u0437\u0001\u0000\u0000\u0000\u0437\u0091\u0001\u0000"+
		"\u0000\u0000\u0438\u0439\u0005\u009e\u0000\u0000\u0439\u043a\u0003\u0094"+
		"J\u0000\u043a\u0093\u0001\u0000\u0000\u0000\u043b\u043c\u0003\u0098L\u0000"+
		"\u043c\u0095\u0001\u0000\u0000\u0000\u043d\u0444\u0005E\u0000\u0000\u043e"+
		"\u0445\u0003\u009aM\u0000\u043f\u0440\u0005\u0015\u0000\u0000\u0440\u0441"+
		"\u0005,\u0000\u0000\u0441\u0442\u0003\u0108\u0084\u0000\u0442\u0443\u0005"+
		"\u0016\u0000\u0000\u0443\u0445\u0001\u0000\u0000\u0000\u0444\u043e\u0001"+
		"\u0000\u0000\u0000\u0444\u043f\u0001\u0000\u0000\u0000\u0445\u0446\u0001"+
		"\u0000\u0000\u0000\u0446\u0447\u0003\u0098L\u0000\u0447\u0097\u0001\u0000"+
		"\u0000\u0000\u0448\u044a\u0005\u0019\u0000\u0000\u0449\u044b\u0003L&\u0000"+
		"\u044a\u0449\u0001\u0000\u0000\u0000\u044a\u044b\u0001\u0000\u0000\u0000"+
		"\u044b\u044c\u0001\u0000\u0000\u0000\u044c\u044d\u0005\u001a\u0000\u0000"+
		"\u044d\u0099\u0001\u0000\u0000\u0000\u044e\u0453\u0003\u00eew\u0000\u044f"+
		"\u0450\u0005\'\u0000\u0000\u0450\u0452\u0003\u00eew\u0000\u0451\u044f"+
		"\u0001\u0000\u0000\u0000\u0452\u0455\u0001\u0000\u0000\u0000\u0453\u0451"+
		"\u0001\u0000\u0000\u0000\u0453\u0454\u0001\u0000\u0000\u0000\u0454\u009b"+
		"\u0001\u0000\u0000\u0000\u0455\u0453\u0001\u0000\u0000\u0000\u0456\u045c"+
		"\u0005\u00a4\u0000\u0000\u0457\u045d\u0003\u009eO\u0000\u0458\u045d\u0003"+
		"\u00a0P\u0000\u0459\u045d\u0003\u00a2Q\u0000\u045a\u045d\u0003\u00a4R"+
		"\u0000\u045b\u045d\u0003\u00a6S\u0000\u045c\u0457\u0001\u0000\u0000\u0000"+
		"\u045c\u0458\u0001\u0000\u0000\u0000\u045c\u0459\u0001\u0000\u0000\u0000"+
		"\u045c\u045a\u0001\u0000\u0000\u0000\u045c\u045b\u0001\u0000\u0000\u0000"+
		"\u045d\u009d\u0001\u0000\u0000\u0000\u045e\u045f\u0005\u00b1\u0000\u0000"+
		"\u045f\u0460\u0003L&\u0000\u0460\u0461\u0005\u00b2\u0000\u0000\u0461\u0462"+
		"\u0003N\'\u0000\u0462\u009f\u0001\u0000\u0000\u0000\u0463\u0464\u0005"+
		"\u00b3\u0000\u0000\u0464\u0465\u0003L&\u0000\u0465\u0466\u0005\u00b2\u0000"+
		"\u0000\u0466\u0467\u0003N\'\u0000\u0467\u00a1\u0001\u0000\u0000\u0000"+
		"\u0468\u0469\u0005\u00b4\u0000\u0000\u0469\u046a\u0003N\'\u0000\u046a"+
		"\u046b\u0007\u0006\u0000\u0000\u046b\u046c\u0003N\'\u0000\u046c\u00a3"+
		"\u0001\u0000\u0000\u0000\u046d\u046e\u0005\u00b6\u0000\u0000\u046e\u046f"+
		"\u0003N\'\u0000\u046f\u00a5\u0001\u0000\u0000\u0000\u0470\u0471\u0005"+
		"\u00b7\u0000\u0000\u0471\u0472\u0003N\'\u0000\u0472\u0473\u0005:\u0000"+
		"\u0000\u0473\u0474\u0003N\'\u0000\u0474\u00a7\u0001\u0000\u0000\u0000"+
		"\u0475\u047a\u0003\u00aaU\u0000\u0476\u0477\u0005\u0083\u0000\u0000\u0477"+
		"\u0479\u0003\u00aaU\u0000\u0478\u0476\u0001\u0000\u0000\u0000\u0479\u047c"+
		"\u0001\u0000\u0000\u0000\u047a\u0478\u0001\u0000\u0000\u0000\u047a\u047b"+
		"\u0001\u0000\u0000\u0000\u047b\u00a9\u0001\u0000\u0000\u0000\u047c\u047a"+
		"\u0001\u0000\u0000\u0000\u047d\u0482\u0003\u00acV\u0000\u047e\u047f\u0005"+
		"8\u0000\u0000\u047f\u0481\u0003\u00acV\u0000\u0480\u047e\u0001\u0000\u0000"+
		"\u0000\u0481\u0484\u0001\u0000\u0000\u0000\u0482\u0480\u0001\u0000\u0000"+
		"\u0000\u0482\u0483\u0001\u0000\u0000\u0000\u0483\u00ab\u0001\u0000\u0000"+
		"\u0000\u0484\u0482\u0001\u0000\u0000\u0000\u0485\u048d\u0003\u00aeW\u0000"+
		"\u0486\u048a\u0003\u00ccf\u0000\u0487\u048a\u0003\u00cae\u0000\u0488\u048a"+
		"\u0003\u00ceg\u0000\u0489\u0486\u0001\u0000\u0000\u0000\u0489\u0487\u0001"+
		"\u0000\u0000\u0000\u0489\u0488\u0001\u0000\u0000\u0000\u048a\u048b\u0001"+
		"\u0000\u0000\u0000\u048b\u048c\u0003\u00aeW\u0000\u048c\u048e\u0001\u0000"+
		"\u0000\u0000\u048d\u0489\u0001\u0000\u0000\u0000\u048d\u048e\u0001\u0000"+
		"\u0000\u0000\u048e\u00ad\u0001\u0000\u0000\u0000\u048f\u0494\u0003\u00b0"+
		"X\u0000\u0490\u0491\u00053\u0000\u0000\u0491\u0493\u0003\u00b0X\u0000"+
		"\u0492\u0490\u0001\u0000\u0000\u0000\u0493\u0496\u0001\u0000\u0000\u0000"+
		"\u0494\u0492\u0001\u0000\u0000\u0000\u0494\u0495\u0001\u0000\u0000\u0000"+
		"\u0495\u00af\u0001\u0000\u0000\u0000\u0496\u0494\u0001\u0000\u0000\u0000"+
		"\u0497\u049a\u0003\u00b2Y\u0000\u0498\u0499\u0005\u009c\u0000\u0000\u0499"+
		"\u049b\u0003\u00b2Y\u0000\u049a\u0498\u0001\u0000\u0000\u0000\u049a\u049b"+
		"\u0001\u0000\u0000\u0000\u049b\u00b1\u0001\u0000\u0000\u0000\u049c\u04a1"+
		"\u0003\u00b4Z\u0000\u049d\u049e\u0007\u0007\u0000\u0000\u049e\u04a0\u0003"+
		"\u00b4Z\u0000\u049f\u049d\u0001\u0000\u0000\u0000\u04a0\u04a3\u0001\u0000"+
		"\u0000\u0000\u04a1\u049f\u0001\u0000\u0000\u0000\u04a1\u04a2\u0001\u0000"+
		"\u0000\u0000\u04a2\u00b3\u0001\u0000\u0000\u0000\u04a3\u04a1\u0001\u0000"+
		"\u0000\u0000\u04a4\u04a9\u0003\u00b6[\u0000\u04a5\u04a6\u0007\b\u0000"+
		"\u0000\u04a6\u04a8\u0003\u00b6[\u0000\u04a7\u04a5\u0001\u0000\u0000\u0000"+
		"\u04a8\u04ab\u0001\u0000\u0000\u0000\u04a9\u04a7\u0001\u0000\u0000\u0000"+
		"\u04a9\u04aa\u0001\u0000\u0000\u0000\u04aa\u00b5\u0001\u0000\u0000\u0000"+
		"\u04ab\u04a9\u0001\u0000\u0000\u0000\u04ac\u04b1\u0003\u00b8\\\u0000\u04ad"+
		"\u04ae\u0007\t\u0000\u0000\u04ae\u04b0\u0003\u00b8\\\u0000\u04af\u04ad"+
		"\u0001\u0000\u0000\u0000\u04b0\u04b3\u0001\u0000\u0000\u0000\u04b1\u04af"+
		"\u0001\u0000\u0000\u0000\u04b1\u04b2\u0001\u0000\u0000\u0000\u04b2\u00b7"+
		"\u0001\u0000\u0000\u0000\u04b3\u04b1\u0001\u0000\u0000\u0000\u04b4\u04b9"+
		"\u0003\u00ba]\u0000\u04b5\u04b6\u0007\n\u0000\u0000\u04b6\u04b8\u0003"+
		"\u00ba]\u0000\u04b7\u04b5\u0001\u0000\u0000\u0000\u04b8\u04bb\u0001\u0000"+
		"\u0000\u0000\u04b9\u04b7\u0001\u0000\u0000\u0000\u04b9\u04ba\u0001\u0000"+
		"\u0000\u0000\u04ba\u00b9\u0001\u0000\u0000\u0000\u04bb\u04b9\u0001\u0000"+
		"\u0000\u0000\u04bc\u04c0\u0003\u00bc^\u0000\u04bd\u04be\u0005m\u0000\u0000"+
		"\u04be\u04bf\u0005\u0080\u0000\u0000\u04bf\u04c1\u0003\u017a\u00bd\u0000"+
		"\u04c0\u04bd\u0001\u0000\u0000\u0000\u04c0\u04c1\u0001\u0000\u0000\u0000"+
		"\u04c1\u00bb\u0001\u0000\u0000\u0000\u04c2\u04c6\u0003\u00be_\u0000\u04c3"+
		"\u04c4\u0005\u009d\u0000\u0000\u04c4\u04c5\u0005:\u0000\u0000\u04c5\u04c7"+
		"\u0003\u017a\u00bd\u0000\u04c6\u04c3\u0001\u0000\u0000\u0000\u04c6\u04c7"+
		"\u0001\u0000\u0000\u0000\u04c7\u00bd\u0001\u0000\u0000\u0000\u04c8\u04cc"+
		"\u0003\u00c0`\u0000\u04c9\u04ca\u0005D\u0000\u0000\u04ca\u04cb\u0005:"+
		"\u0000\u0000\u04cb\u04cd\u0003\u0176\u00bb\u0000\u04cc\u04c9\u0001\u0000"+
		"\u0000\u0000\u04cc\u04cd\u0001\u0000\u0000\u0000\u04cd\u00bf\u0001\u0000"+
		"\u0000\u0000\u04ce\u04d2\u0003\u00c2a\u0000\u04cf\u04d0\u0005C\u0000\u0000"+
		"\u04d0\u04d1\u0005:\u0000\u0000\u04d1\u04d3\u0003\u0176\u00bb\u0000\u04d2"+
		"\u04cf\u0001\u0000\u0000\u0000\u04d2\u04d3\u0001\u0000\u0000\u0000\u04d3"+
		"\u00c1\u0001\u0000\u0000\u0000\u04d4\u04d9\u0003\u00c6c\u0000\u04d5\u04d6"+
		"\u00051\u0000\u0000\u04d6\u04d8\u0003\u00c4b\u0000\u04d7\u04d5\u0001\u0000"+
		"\u0000\u0000\u04d8\u04db\u0001\u0000\u0000\u0000\u04d9\u04d7\u0001\u0000"+
		"\u0000\u0000\u04d9\u04da\u0001\u0000\u0000\u0000\u04da\u00c3\u0001\u0000"+
		"\u0000\u0000\u04db\u04d9\u0001\u0000\u0000\u0000\u04dc\u04dd\u0003\u00fe"+
		"\u007f\u0000\u04dd\u04de\u0003\u00f4z\u0000\u04de\u00c5\u0001\u0000\u0000"+
		"\u0000\u04df\u04e1\u0007\u0007\u0000\u0000\u04e0\u04df\u0001\u0000\u0000"+
		"\u0000\u04e1\u04e4\u0001\u0000\u0000\u0000\u04e2\u04e0\u0001\u0000\u0000"+
		"\u0000\u04e2\u04e3\u0001\u0000\u0000\u0000\u04e3\u04e5\u0001\u0000\u0000"+
		"\u0000\u04e4\u04e2\u0001\u0000\u0000\u0000\u04e5\u04e6\u0003\u00c8d\u0000"+
		"\u04e6\u00c7\u0001\u0000\u0000\u0000\u04e7\u04eb\u0003\u00d0h\u0000\u04e8"+
		"\u04eb\u0003\u00d4j\u0000\u04e9\u04eb\u0003\u00d6k\u0000\u04ea\u04e7\u0001"+
		"\u0000\u0000\u0000\u04ea\u04e8\u0001\u0000\u0000\u0000\u04ea\u04e9\u0001"+
		"\u0000\u0000\u0000\u04eb\u00c9\u0001\u0000\u0000\u0000\u04ec\u04f5\u0005"+
		"\u0013\u0000\u0000\u04ed\u04f5\u0005\u0014\u0000\u0000\u04ee\u04f5\u0005"+
		"(\u0000\u0000\u04ef\u04f0\u0005(\u0000\u0000\u04f0\u04f5\u0005\u0013\u0000"+
		"\u0000\u04f1\u04f5\u0005)\u0000\u0000\u04f2\u04f3\u0005)\u0000\u0000\u04f3"+
		"\u04f5\u0005\u0013\u0000\u0000\u04f4\u04ec\u0001\u0000\u0000\u0000\u04f4"+
		"\u04ed\u0001\u0000\u0000\u0000\u04f4\u04ee\u0001\u0000\u0000\u0000\u04f4"+
		"\u04ef\u0001\u0000\u0000\u0000\u04f4\u04f1\u0001\u0000\u0000\u0000\u04f4"+
		"\u04f2\u0001\u0000\u0000\u0000\u04f5\u00cb\u0001\u0000\u0000\u0000\u04f6"+
		"\u04f7\u0007\u000b\u0000\u0000\u04f7\u00cd\u0001\u0000\u0000\u0000\u04f8"+
		"\u04fe\u0005o\u0000\u0000\u04f9\u04fa\u0005(\u0000\u0000\u04fa\u04fe\u0005"+
		"(\u0000\u0000\u04fb\u04fc\u0005)\u0000\u0000\u04fc\u04fe\u0005)\u0000"+
		"\u0000\u04fd\u04f8\u0001\u0000\u0000\u0000\u04fd\u04f9\u0001\u0000\u0000"+
		"\u0000\u04fd\u04fb\u0001\u0000\u0000\u0000\u04fe\u00cf\u0001\u0000\u0000"+
		"\u0000\u04ff\u0503\u0005\u00a5\u0000\u0000\u0500\u0504\u0003\u00d2i\u0000"+
		"\u0501\u0502\u0007\f\u0000\u0000\u0502\u0504\u0003\u01a4\u00d2\u0000\u0503"+
		"\u0500\u0001\u0000\u0000\u0000\u0503\u0501\u0001\u0000\u0000\u0000\u0503"+
		"\u0504\u0001\u0000\u0000\u0000\u0504\u0505\u0001\u0000\u0000\u0000\u0505"+
		"\u0506\u0003\u0098L\u0000\u0506\u00d1\u0001\u0000\u0000\u0000\u0507\u0508"+
		"\u0007\r\u0000\u0000\u0508\u00d3\u0001\u0000\u0000\u0000\u0509\u050b\u0005"+
		"\u0011\u0000\u0000\u050a\u0509\u0001\u0000\u0000\u0000\u050b\u050c\u0001"+
		"\u0000\u0000\u0000\u050c\u050a\u0001\u0000\u0000\u0000\u050c\u050d\u0001"+
		"\u0000\u0000\u0000\u050d\u050e\u0001\u0000\u0000\u0000\u050e\u050f\u0005"+
		"\u0019\u0000\u0000\u050f\u0510\u0003L&\u0000\u0510\u0511\u0005\u001a\u0000"+
		"\u0000\u0511\u00d5\u0001\u0000\u0000\u0000\u0512\u0517\u0003\u00d8l\u0000"+
		"\u0513\u0514\u0005.\u0000\u0000\u0514\u0516\u0003\u00d8l\u0000\u0515\u0513"+
		"\u0001\u0000\u0000\u0000\u0516\u0519\u0001\u0000\u0000\u0000\u0517\u0515"+
		"\u0001\u0000\u0000\u0000\u0517\u0518\u0001\u0000\u0000\u0000\u0518\u00d7"+
		"\u0001\u0000\u0000\u0000\u0519\u0517\u0001\u0000\u0000\u0000\u051a\u051c"+
		"\u0005$\u0000\u0000\u051b\u051d\u0003\u00dam\u0000\u051c\u051b\u0001\u0000"+
		"\u0000\u0000\u051c\u051d\u0001\u0000\u0000\u0000\u051d\u0522\u0001\u0000"+
		"\u0000\u0000\u051e\u051f\u0005%\u0000\u0000\u051f\u0522\u0003\u00dam\u0000"+
		"\u0520\u0522\u0003\u00dam\u0000\u0521\u051a\u0001\u0000\u0000\u0000\u0521"+
		"\u051e\u0001\u0000\u0000\u0000\u0521\u0520\u0001\u0000\u0000\u0000\u0522"+
		"\u00d9\u0001\u0000\u0000\u0000\u0523\u0528\u0003\u00dcn\u0000\u0524\u0525"+
		"\u0007\u000e\u0000\u0000\u0525\u0527\u0003\u00dcn\u0000\u0526\u0524\u0001"+
		"\u0000\u0000\u0000\u0527\u052a\u0001\u0000\u0000\u0000\u0528\u0526\u0001"+
		"\u0000\u0000\u0000\u0528\u0529\u0001\u0000\u0000\u0000\u0529\u00db\u0001"+
		"\u0000\u0000\u0000\u052a\u0528\u0001\u0000\u0000\u0000\u052b\u052e\u0003"+
		"\u00f2y\u0000\u052c\u052e\u0003\u00deo\u0000\u052d\u052b\u0001\u0000\u0000"+
		"\u0000\u052d\u052c\u0001\u0000\u0000\u0000\u052e\u00dd\u0001\u0000\u0000"+
		"\u0000\u052f\u0532\u0003\u00e6s\u0000\u0530\u0532\u0003\u00e0p\u0000\u0531"+
		"\u052f\u0001\u0000\u0000\u0000\u0531\u0530\u0001\u0000\u0000\u0000\u0532"+
		"\u0533\u0001\u0000\u0000\u0000\u0533\u0534\u0003\u00f6{\u0000\u0534\u00df"+
		"\u0001\u0000\u0000\u0000\u0535\u0536\u0003\u00e2q\u0000\u0536\u0537\u0003"+
		"\u00ecv\u0000\u0537\u053a\u0001\u0000\u0000\u0000\u0538\u053a\u0003\u00e4"+
		"r\u0000\u0539\u0535\u0001\u0000\u0000\u0000\u0539\u0538\u0001\u0000\u0000"+
		"\u0000\u053a\u00e1\u0001\u0000\u0000\u0000\u053b\u053c\u0007\u000f\u0000"+
		"\u0000\u053c\u053d\u0005!\u0000\u0000\u053d\u053e\u0005!\u0000\u0000\u053e"+
		"\u00e3\u0001\u0000\u0000\u0000\u053f\u0541\u0005+\u0000\u0000\u0540\u053f"+
		"\u0001\u0000\u0000\u0000\u0540\u0541\u0001\u0000\u0000\u0000\u0541\u0542"+
		"\u0001\u0000\u0000\u0000\u0542\u0543\u0003\u00ecv\u0000\u0543\u00e5\u0001"+
		"\u0000\u0000\u0000\u0544\u0545\u0003\u00e8t\u0000\u0545\u0546\u0003\u00ec"+
		"v\u0000\u0546\u0549\u0001\u0000\u0000\u0000\u0547\u0549\u0003\u00eau\u0000"+
		"\u0548\u0544\u0001\u0000\u0000\u0000\u0548\u0547\u0001\u0000\u0000\u0000"+
		"\u0549\u00e7\u0001\u0000\u0000\u0000\u054a\u054b\u0007\u0010\u0000\u0000"+
		"\u054b\u054c\u0005!\u0000\u0000\u054c\u054d\u0005!\u0000\u0000\u054d\u00e9"+
		"\u0001\u0000\u0000\u0000\u054e\u054f\u0005 \u0000\u0000\u054f\u00eb\u0001"+
		"\u0000\u0000\u0000\u0550\u0553\u0003\u00eew\u0000\u0551\u0553\u0003\u0180"+
		"\u00c0\u0000\u0552\u0550\u0001\u0000\u0000\u0000\u0552\u0551\u0001\u0000"+
		"\u0000\u0000\u0553\u00ed\u0001\u0000\u0000\u0000\u0554\u0557\u0003\u01c8"+
		"\u00e4\u0000\u0555\u0557\u0003\u00f0x\u0000\u0556\u0554\u0001\u0000\u0000"+
		"\u0000\u0556\u0555\u0001\u0000\u0000\u0000\u0557\u00ef\u0001\u0000\u0000"+
		"\u0000\u0558\u055c\u0005\u001b\u0000\u0000\u0559\u055c\u0005\u00ba\u0000"+
		"\u0000\u055a\u055c\u0005\u00bb\u0000\u0000\u055b\u0558\u0001\u0000\u0000"+
		"\u0000\u055b\u0559\u0001\u0000\u0000\u0000\u055b\u055a\u0001\u0000\u0000"+
		"\u0000\u055c\u00f1\u0001\u0000\u0000\u0000\u055d\u0563\u0003\u0100\u0080"+
		"\u0000\u055e\u0562\u0003\u00f8|\u0000\u055f\u0562\u0003\u00f4z\u0000\u0560"+
		"\u0562\u0003\u00fa}\u0000\u0561\u055e\u0001\u0000\u0000\u0000\u0561\u055f"+
		"\u0001\u0000\u0000\u0000\u0561\u0560\u0001\u0000\u0000\u0000\u0562\u0565"+
		"\u0001\u0000\u0000\u0000\u0563\u0561\u0001\u0000\u0000\u0000\u0563\u0564"+
		"\u0001\u0000\u0000\u0000\u0564\u00f3\u0001\u0000\u0000\u0000\u0565\u0563"+
		"\u0001\u0000\u0000\u0000\u0566\u056f\u0005\u0015\u0000\u0000\u0567\u056c"+
		"\u0003\u0114\u008a\u0000\u0568\u0569\u0005\u001e\u0000\u0000\u0569\u056b"+
		"\u0003\u0114\u008a\u0000\u056a\u0568\u0001\u0000\u0000\u0000\u056b\u056e"+
		"\u0001\u0000\u0000\u0000\u056c\u056a\u0001\u0000\u0000\u0000\u056c\u056d"+
		"\u0001\u0000\u0000\u0000\u056d\u0570\u0001\u0000\u0000\u0000\u056e\u056c"+
		"\u0001\u0000\u0000\u0000\u056f\u0567\u0001\u0000\u0000\u0000\u056f\u0570"+
		"\u0001\u0000\u0000\u0000\u0570\u0571\u0001\u0000\u0000\u0000\u0571\u0572"+
		"\u0005\u0016\u0000\u0000\u0572\u00f5\u0001\u0000\u0000\u0000\u0573\u0575"+
		"\u0003\u00f8|\u0000\u0574\u0573\u0001\u0000\u0000\u0000\u0575\u0578\u0001"+
		"\u0000\u0000\u0000\u0576\u0574\u0001\u0000\u0000\u0000\u0576\u0577\u0001"+
		"\u0000\u0000\u0000\u0577\u00f7\u0001\u0000\u0000\u0000\u0578\u0576\u0001"+
		"\u0000\u0000\u0000\u0579\u057a\u0005\u0017\u0000\u0000\u057a\u057b\u0003"+
		"L&\u0000\u057b\u057c\u0005\u0018\u0000\u0000\u057c\u00f9\u0001\u0000\u0000"+
		"\u0000\u057d\u057e\u0005*\u0000\u0000\u057e\u057f\u0003\u00fc~\u0000\u057f"+
		"\u00fb\u0001\u0000\u0000\u0000\u0580\u0585\u0003\u01cc\u00e6\u0000\u0581"+
		"\u0585\u0005\u0005\u0000\u0000\u0582\u0585\u0003\u010a\u0085\u0000\u0583"+
		"\u0585\u0005\u001b\u0000\u0000\u0584\u0580\u0001\u0000\u0000\u0000\u0584"+
		"\u0581\u0001\u0000\u0000\u0000\u0584\u0582\u0001\u0000\u0000\u0000\u0584"+
		"\u0583\u0001\u0000\u0000\u0000\u0585\u00fd\u0001\u0000\u0000\u0000\u0586"+
		"\u058a\u0003\u01c8\u00e4\u0000\u0587\u058a\u0003\u0106\u0083\u0000\u0588"+
		"\u058a\u0003\u010a\u0085\u0000\u0589\u0586\u0001\u0000\u0000\u0000\u0589"+
		"\u0587\u0001\u0000\u0000\u0000\u0589\u0588\u0001\u0000\u0000\u0000\u058a"+
		"\u00ff\u0001\u0000\u0000\u0000\u058b\u0599\u0003\u0102\u0081\u0000\u058c"+
		"\u0599\u0003\u0106\u0083\u0000\u058d\u0599\u0003\u010a\u0085\u0000\u058e"+
		"\u0599\u0003\u010c\u0086\u0000\u058f\u0599\u0003\u0112\u0089\u0000\u0590"+
		"\u0599\u0003\u010e\u0087\u0000\u0591\u0599\u0003\u0110\u0088\u0000\u0592"+
		"\u0599\u0003\u0116\u008b\u0000\u0593\u0599\u0003\u0154\u00aa\u0000\u0594"+
		"\u0599\u0003\u015c\u00ae\u0000\u0595\u0599\u0003\u0160\u00b0\u0000\u0596"+
		"\u0599\u0003\u0166\u00b3\u0000\u0597\u0599\u0003\u0174\u00ba\u0000\u0598"+
		"\u058b\u0001\u0000\u0000\u0000\u0598\u058c\u0001\u0000\u0000\u0000\u0598"+
		"\u058d\u0001\u0000\u0000\u0000\u0598\u058e\u0001\u0000\u0000\u0000\u0598"+
		"\u058f\u0001\u0000\u0000\u0000\u0598\u0590\u0001\u0000\u0000\u0000\u0598"+
		"\u0591\u0001\u0000\u0000\u0000\u0598\u0592\u0001\u0000\u0000\u0000\u0598"+
		"\u0593\u0001\u0000\u0000\u0000\u0598\u0594\u0001\u0000\u0000\u0000\u0598"+
		"\u0595\u0001\u0000\u0000\u0000\u0598\u0596\u0001\u0000\u0000\u0000\u0598"+
		"\u0597\u0001\u0000\u0000\u0000\u0599\u0101\u0001\u0000\u0000\u0000\u059a"+
		"\u059d\u0003\u0104\u0082\u0000\u059b\u059d\u0003\u01dc\u00ee\u0000\u059c"+
		"\u059a\u0001\u0000\u0000\u0000\u059c\u059b\u0001\u0000\u0000\u0000\u059d"+
		"\u0103\u0001\u0000\u0000\u0000\u059e\u059f\u0007\u0011\u0000\u0000\u059f"+
		"\u0105\u0001\u0000\u0000\u0000\u05a0\u05a1\u0005,\u0000\u0000\u05a1\u05a2"+
		"\u0003\u01c8\u00e4\u0000\u05a2\u0107\u0001\u0000\u0000\u0000\u05a3\u05a4"+
		"\u0003\u01c8\u00e4\u0000\u05a4\u0109\u0001\u0000\u0000\u0000\u05a5\u05a7"+
		"\u0005\u0015\u0000\u0000\u05a6\u05a8\u0003L&\u0000\u05a7\u05a6\u0001\u0000"+
		"\u0000\u0000\u05a7\u05a8\u0001\u0000\u0000\u0000\u05a8\u05a9\u0001\u0000"+
		"\u0000\u0000\u05a9\u05aa\u0005\u0016\u0000\u0000\u05aa\u010b\u0001\u0000"+
		"\u0000\u0000\u05ab\u05ac\u0005\u001f\u0000\u0000\u05ac\u010d\u0001\u0000"+
		"\u0000\u0000\u05ad\u05ae\u0005\u0085\u0000\u0000\u05ae\u05af\u0003\u0098"+
		"L\u0000\u05af\u010f\u0001\u0000\u0000\u0000\u05b0\u05b1\u0005\u00a3\u0000"+
		"\u0000\u05b1\u05b2\u0003\u0098L\u0000\u05b2\u0111\u0001\u0000\u0000\u0000"+
		"\u05b3\u05b4\u0003\u01c8\u00e4\u0000\u05b4\u05b5\u0003\u00f4z\u0000\u05b5"+
		"\u0113\u0001\u0000\u0000\u0000\u05b6\u05b9\u0003N\'\u0000\u05b7\u05b9"+
		"\u0005*\u0000\u0000\u05b8\u05b6\u0001\u0000\u0000\u0000\u05b8\u05b7\u0001"+
		"\u0000\u0000\u0000\u05b9\u0115\u0001\u0000\u0000\u0000\u05ba\u05bd\u0003"+
		"\u0118\u008c\u0000\u05bb\u05bd\u0003\u012e\u0097\u0000\u05bc\u05ba\u0001"+
		"\u0000\u0000\u0000\u05bc\u05bb\u0001\u0000\u0000\u0000\u05bd\u0117\u0001"+
		"\u0000\u0000\u0000\u05be\u05c2\u0003\u011a\u008d\u0000\u05bf\u05c2\u0003"+
		"\u011c\u008e\u0000\u05c0\u05c2\u0007\u0012\u0000\u0000\u05c1\u05be\u0001"+
		"\u0000\u0000\u0000\u05c1\u05bf\u0001\u0000\u0000\u0000\u05c1\u05c0\u0001"+
		"\u0000\u0000\u0000\u05c2\u0119\u0001\u0000\u0000\u0000\u05c3\u05c4\u0005"+
		"(\u0000\u0000\u05c4\u05c5\u0003\u01ca\u00e5\u0000\u05c5\u05c6\u0003\u011e"+
		"\u008f\u0000\u05c6\u05ca\u0005)\u0000\u0000\u05c7\u05c9\u0003\u012a\u0095"+
		"\u0000\u05c8\u05c7\u0001\u0000\u0000\u0000\u05c9\u05cc\u0001\u0000\u0000"+
		"\u0000\u05ca\u05c8\u0001\u0000\u0000\u0000\u05ca\u05cb\u0001\u0000\u0000"+
		"\u0000\u05cb\u05cd\u0001\u0000\u0000\u0000\u05cc\u05ca\u0001\u0000\u0000"+
		"\u0000\u05cd\u05ce\u0005(\u0000\u0000\u05ce\u05cf\u0005$\u0000\u0000\u05cf"+
		"\u05d0\u0003\u01ca\u00e5\u0000\u05d0\u05d1\u0005)\u0000\u0000\u05d1\u011b"+
		"\u0001\u0000\u0000\u0000\u05d2\u05d3\u0005(\u0000\u0000\u05d3\u05d4\u0003"+
		"\u01ca\u00e5\u0000\u05d4\u05d5\u0003\u011e\u008f\u0000\u05d5\u05d6\u0005"+
		"$\u0000\u0000\u05d6\u05d7\u0005)\u0000\u0000\u05d7\u011d\u0001\u0000\u0000"+
		"\u0000\u05d8\u05d9\u0003\u01ca\u00e5\u0000\u05d9\u05da\u0005\u0013\u0000"+
		"\u0000\u05da\u05db\u0003\u0124\u0092\u0000\u05db\u05dd\u0001\u0000\u0000"+
		"\u0000\u05dc\u05d8\u0001\u0000\u0000\u0000\u05dd\u05e0\u0001\u0000\u0000"+
		"\u0000\u05de\u05dc\u0001\u0000\u0000\u0000\u05de\u05df\u0001\u0000\u0000"+
		"\u0000\u05df\u011f\u0001\u0000\u0000\u0000\u05e0\u05de\u0001\u0000\u0000"+
		"\u0000\u05e1\u05e8\u0005\u000b\u0000\u0000\u05e2\u05e7\u0005\t\u0000\u0000"+
		"\u05e3\u05e7\u0005\n\u0000\u0000\u05e4\u05e7\u0005\u0001\u0000\u0000\u05e5"+
		"\u05e7\u0003\u0126\u0093\u0000\u05e6\u05e2\u0001\u0000\u0000\u0000\u05e6"+
		"\u05e3\u0001\u0000\u0000\u0000\u05e6\u05e4\u0001\u0000\u0000\u0000\u05e6"+
		"\u05e5\u0001\u0000\u0000\u0000\u05e7\u05ea\u0001\u0000\u0000\u0000\u05e8"+
		"\u05e6\u0001\u0000\u0000\u0000\u05e8\u05e9\u0001\u0000\u0000\u0000\u05e9"+
		"\u05eb\u0001\u0000\u0000\u0000\u05ea\u05e8\u0001\u0000\u0000\u0000\u05eb"+
		"\u05ec\u0005\u000b\u0000\u0000\u05ec\u0121\u0001\u0000\u0000\u0000\u05ed"+
		"\u05f4\u0005\f\u0000\u0000\u05ee\u05f3\u0005\t\u0000\u0000\u05ef\u05f3"+
		"\u0005\n\u0000\u0000\u05f0\u05f3\u0005\u0002\u0000\u0000\u05f1\u05f3\u0003"+
		"\u0128\u0094\u0000\u05f2\u05ee\u0001\u0000\u0000\u0000\u05f2\u05ef\u0001"+
		"\u0000\u0000\u0000\u05f2\u05f0\u0001\u0000\u0000\u0000\u05f2\u05f1\u0001"+
		"\u0000\u0000\u0000\u05f3\u05f6\u0001\u0000\u0000\u0000\u05f4\u05f2\u0001"+
		"\u0000\u0000\u0000\u05f4\u05f5\u0001\u0000\u0000\u0000\u05f5\u05f7\u0001"+
		"\u0000\u0000\u0000\u05f6\u05f4\u0001\u0000\u0000\u0000\u05f7\u05f8\u0005"+
		"\f\u0000\u0000\u05f8\u0123\u0001\u0000\u0000\u0000\u05f9\u05fc\u0003\u0120"+
		"\u0090\u0000\u05fa\u05fc\u0003\u0122\u0091\u0000\u05fb\u05f9\u0001\u0000"+
		"\u0000\u0000\u05fb\u05fa\u0001\u0000\u0000\u0000\u05fc\u0125\u0001\u0000"+
		"\u0000\u0000\u05fd\u05ff\u0005\u00c4\u0000\u0000\u05fe\u05fd\u0001\u0000"+
		"\u0000\u0000\u05ff\u0600\u0001\u0000\u0000\u0000\u0600\u05fe\u0001\u0000"+
		"\u0000\u0000\u0600\u0601\u0001\u0000\u0000\u0000\u0601\u060b\u0001\u0000"+
		"\u0000\u0000\u0602\u060b\u0005\u0003\u0000\u0000\u0603\u060b\u0005\u0004"+
		"\u0000\u0000\u0604\u060b\u0003\u0120\u0090\u0000\u0605\u0607\u0005\u0019"+
		"\u0000\u0000\u0606\u0608\u0003L&\u0000\u0607\u0606\u0001\u0000\u0000\u0000"+
		"\u0607\u0608\u0001\u0000\u0000\u0000\u0608\u0609\u0001\u0000\u0000\u0000"+
		"\u0609\u060b\u0005\u001a\u0000\u0000\u060a\u05fe\u0001\u0000\u0000\u0000"+
		"\u060a\u0602\u0001\u0000\u0000\u0000\u060a\u0603\u0001\u0000\u0000\u0000"+
		"\u060a\u0604\u0001\u0000\u0000\u0000\u060a\u0605\u0001\u0000\u0000\u0000"+
		"\u060b\u0127\u0001\u0000\u0000\u0000\u060c\u060e\u0005\u00c4\u0000\u0000"+
		"\u060d\u060c\u0001\u0000\u0000\u0000\u060e\u060f\u0001\u0000\u0000\u0000"+
		"\u060f\u060d\u0001\u0000\u0000\u0000\u060f\u0610\u0001\u0000\u0000\u0000"+
		"\u0610\u061a\u0001\u0000\u0000\u0000\u0611\u061a\u0005\u0003\u0000\u0000"+
		"\u0612\u061a\u0005\u0004\u0000\u0000\u0613\u061a\u0003\u0122\u0091\u0000"+
		"\u0614\u0616\u0005\u0019\u0000\u0000\u0615\u0617\u0003L&\u0000\u0616\u0615"+
		"\u0001\u0000\u0000\u0000\u0616\u0617\u0001\u0000\u0000\u0000\u0617\u0618"+
		"\u0001\u0000\u0000\u0000\u0618\u061a\u0005\u001a\u0000\u0000\u0619\u060d"+
		"\u0001\u0000\u0000\u0000\u0619\u0611\u0001\u0000\u0000\u0000\u0619\u0612"+
		"\u0001\u0000\u0000\u0000\u0619\u0613\u0001\u0000\u0000\u0000\u0619\u0614"+
		"\u0001\u0000\u0000\u0000\u061a\u0129\u0001\u0000\u0000\u0000\u061b\u0622"+
		"\u0003\u0118\u008c\u0000\u061c\u0622\u0003\u012c\u0096\u0000\u061d\u0622"+
		"\u0005\u0010\u0000\u0000\u061e\u0622\u0005\u000b\u0000\u0000\u061f\u0622"+
		"\u0005\f\u0000\u0000\u0620\u0622\u0003\u01e2\u00f1\u0000\u0621\u061b\u0001"+
		"\u0000\u0000\u0000\u0621\u061c\u0001\u0000\u0000\u0000\u0621\u061d\u0001"+
		"\u0000\u0000\u0000\u0621\u061e\u0001\u0000\u0000\u0000\u0621\u061f\u0001"+
		"\u0000\u0000\u0000\u0621\u0620\u0001\u0000\u0000\u0000\u0622\u012b\u0001"+
		"\u0000\u0000\u0000\u0623\u062d\u0007\u0013\u0000\u0000\u0624\u0625\u0005"+
		"\u0019\u0000\u0000\u0625\u062d\u0005\u0019\u0000\u0000\u0626\u0627\u0005"+
		"\u001a\u0000\u0000\u0627\u062d\u0005\u001a\u0000\u0000\u0628\u0629\u0005"+
		"\u0019\u0000\u0000\u0629\u062a\u0003L&\u0000\u062a\u062b\u0005\u001a\u0000"+
		"\u0000\u062b\u062d\u0001\u0000\u0000\u0000\u062c\u0623\u0001\u0000\u0000"+
		"\u0000\u062c\u0624\u0001\u0000\u0000\u0000\u062c\u0626\u0001\u0000\u0000"+
		"\u0000\u062c\u0628\u0001\u0000\u0000\u0000\u062d\u012d\u0001\u0000\u0000"+
		"\u0000\u062e\u0637\u0003\u013e\u009f\u0000\u062f\u0637\u0003\u0140\u00a0"+
		"\u0000\u0630\u0637\u0003\u0144\u00a2\u0000\u0631\u0637\u0003\u0146\u00a3"+
		"\u0000\u0632\u0637\u0003\u014e\u00a7\u0000\u0633\u0637\u0003\u0150\u00a8"+
		"\u0000\u0634\u0637\u0003\u0152\u00a9\u0000\u0635\u0637\u0003\u0130\u0098"+
		"\u0000\u0636\u062e\u0001\u0000\u0000\u0000\u0636\u062f\u0001\u0000\u0000"+
		"\u0000\u0636\u0630\u0001\u0000\u0000\u0000\u0636\u0631\u0001\u0000\u0000"+
		"\u0000\u0636\u0632\u0001\u0000\u0000\u0000\u0636\u0633\u0001\u0000\u0000"+
		"\u0000\u0636\u0634\u0001\u0000\u0000\u0000\u0636\u0635\u0001\u0000\u0000"+
		"\u0000\u0637\u012f\u0001\u0000\u0000\u0000\u0638\u063f\u0003\u0132\u0099"+
		"\u0000\u0639\u063f\u0003\u0134\u009a\u0000\u063a\u063f\u0003\u0136\u009b"+
		"\u0000\u063b\u063f\u0003\u0138\u009c\u0000\u063c\u063f\u0003\u013a\u009d"+
		"\u0000\u063d\u063f\u0003\u013c\u009e\u0000\u063e\u0638\u0001\u0000\u0000"+
		"\u0000\u063e\u0639\u0001\u0000\u0000\u0000\u063e\u063a\u0001\u0000\u0000"+
		"\u0000\u063e\u063b\u0001\u0000\u0000\u0000\u063e\u063c\u0001\u0000\u0000"+
		"\u0000\u063e\u063d\u0001\u0000\u0000\u0000\u063f\u0131\u0001\u0000\u0000"+
		"\u0000\u0640\u0641\u0005\u00ac\u0000\u0000\u0641\u0642\u0003\u0142\u00a1"+
		"\u0000\u0642\u0133\u0001\u0000\u0000\u0000\u0643\u0644\u0005\u00b0\u0000"+
		"\u0000\u0644\u0652\u0005\u0019\u0000\u0000\u0645\u0646\u0003N\'\u0000"+
		"\u0646\u0647\u0005!\u0000\u0000\u0647\u064f\u0003N\'\u0000\u0648\u0649"+
		"\u0005\u001e\u0000\u0000\u0649\u064a\u0003N\'\u0000\u064a\u064b\u0005"+
		"!\u0000\u0000\u064b\u064c\u0003N\'\u0000\u064c\u064e\u0001\u0000\u0000"+
		"\u0000\u064d\u0648\u0001\u0000\u0000\u0000\u064e\u0651\u0001\u0000\u0000"+
		"\u0000\u064f\u064d\u0001\u0000\u0000\u0000\u064f\u0650\u0001\u0000\u0000"+
		"\u0000\u0650\u0653\u0001\u0000\u0000\u0000\u0651\u064f\u0001\u0000\u0000"+
		"\u0000\u0652\u0645\u0001\u0000\u0000\u0000\u0652\u0653\u0001\u0000\u0000"+
		"\u0000\u0653\u0654\u0001\u0000\u0000\u0000\u0654\u0655\u0005\u001a\u0000"+
		"\u0000\u0655\u0135\u0001\u0000\u0000\u0000\u0656\u0657\u0005\u00af\u0000"+
		"\u0000\u0657\u0658\u0003\u0142\u00a1\u0000\u0658\u0137\u0001\u0000\u0000"+
		"\u0000\u0659\u065a\u0005\u00ad\u0000\u0000\u065a\u065b\u0005\u0019\u0000"+
		"\u0000\u065b\u065c\u0003N\'\u0000\u065c\u065d\u0005\u001a\u0000\u0000"+
		"\u065d\u0139\u0001\u0000\u0000\u0000\u065e\u065f\u0005\u00ae\u0000\u0000"+
		"\u065f\u0660\u0005\u0019\u0000\u0000\u0660\u0661\u0005\u001a\u0000\u0000"+
		"\u0661\u013b\u0001\u0000\u0000\u0000\u0662\u0663\u0005@\u0000\u0000\u0663"+
		"\u0664\u0003\u0142\u00a1\u0000\u0664\u013d\u0001\u0000\u0000\u0000\u0665"+
		"\u0666\u0005T\u0000\u0000\u0666\u0667\u0003\u0098L\u0000\u0667\u013f\u0001"+
		"\u0000\u0000\u0000\u0668\u066e\u0005V\u0000\u0000\u0669\u066f\u0003\u01c8"+
		"\u00e4\u0000\u066a\u066b\u0005\u0019\u0000\u0000\u066b\u066c\u0003L&\u0000"+
		"\u066c\u066d\u0005\u001a\u0000\u0000\u066d\u066f\u0001\u0000\u0000\u0000"+
		"\u066e\u0669\u0001\u0000\u0000\u0000\u066e\u066a\u0001\u0000\u0000\u0000"+
		"\u066f\u0670\u0001\u0000\u0000\u0000\u0670\u0671\u0003\u0142\u00a1\u0000"+
		"\u0671\u0141\u0001\u0000\u0000\u0000\u0672\u0673\u0003\u0098L\u0000\u0673"+
		"\u0143\u0001\u0000\u0000\u0000\u0674\u067a\u0005=\u0000\u0000\u0675\u067b"+
		"\u0003\u01c8\u00e4\u0000\u0676\u0677\u0005\u0019\u0000\u0000\u0677\u0678"+
		"\u0003L&\u0000\u0678\u0679\u0005\u001a\u0000\u0000\u0679\u067b\u0001\u0000"+
		"\u0000\u0000\u067a\u0675\u0001\u0000\u0000\u0000\u067a\u0676\u0001\u0000"+
		"\u0000\u0000\u067b\u067c\u0001\u0000\u0000\u0000\u067c\u067d\u0003\u0098"+
		"L\u0000\u067d\u0145\u0001\u0000\u0000\u0000\u067e\u0681\u0005y\u0000\u0000"+
		"\u067f\u0682\u0003\u0148\u00a4\u0000\u0680\u0682\u0003\u014a\u00a5\u0000"+
		"\u0681\u067f\u0001\u0000\u0000\u0000\u0681\u0680\u0001\u0000\u0000\u0000"+
		"\u0682\u0683\u0001\u0000\u0000\u0000\u0683\u0684\u0003\u014c\u00a6\u0000"+
		"\u0684\u0147\u0001\u0000\u0000\u0000\u0685\u0686\u0003\u01cc\u00e6\u0000"+
		"\u0686\u0149\u0001\u0000\u0000\u0000\u0687\u0688\u0003\u0098L\u0000\u0688"+
		"\u014b\u0001\u0000\u0000\u0000\u0689\u068a\u0003\u0098L\u0000\u068a\u014d"+
		"\u0001\u0000\u0000\u0000\u068b\u068c\u0005\u009a\u0000\u0000\u068c\u068d"+
		"\u0003\u0098L\u0000\u068d\u014f\u0001\u0000\u0000\u0000\u068e\u068f\u0005"+
		"H\u0000\u0000\u068f\u0690\u0003\u0098L\u0000\u0690\u0151\u0001\u0000\u0000"+
		"\u0000\u0691\u0697\u0005\u008c\u0000\u0000\u0692\u0698\u0003\u01cc\u00e6"+
		"\u0000\u0693\u0694\u0005\u0019\u0000\u0000\u0694\u0695\u0003L&\u0000\u0695"+
		"\u0696\u0005\u001a\u0000\u0000\u0696\u0698\u0001\u0000\u0000\u0000\u0697"+
		"\u0692\u0001\u0000\u0000\u0000\u0697\u0693\u0001\u0000\u0000\u0000\u0698"+
		"\u0699\u0001\u0000\u0000\u0000\u0699\u069a\u0003\u0098L\u0000\u069a\u0153"+
		"\u0001\u0000\u0000\u0000\u069b\u069e\u0003\u0156\u00ab\u0000\u069c\u069e"+
		"\u0003\u0158\u00ac\u0000\u069d\u069b\u0001\u0000\u0000\u0000\u069d\u069c"+
		"\u0001\u0000\u0000\u0000\u069e\u0155\u0001\u0000\u0000\u0000\u069f\u06a0"+
		"\u0003\u01c8\u00e4\u0000\u06a0\u06a1\u0005/\u0000\u0000\u06a1\u06a2\u0005"+
		"\u0005\u0000\u0000\u06a2\u0157\u0001\u0000\u0000\u0000\u06a3\u06a4\u0003"+
		"@ \u0000\u06a4\u06a5\u0005c\u0000\u0000\u06a5\u06a7\u0005\u0015\u0000"+
		"\u0000\u06a6\u06a8\u0003<\u001e\u0000\u06a7\u06a6\u0001\u0000\u0000\u0000"+
		"\u06a7\u06a8\u0001\u0000\u0000\u0000\u06a8\u06a9\u0001\u0000\u0000\u0000"+
		"\u06a9\u06ac\u0005\u0016\u0000\u0000\u06aa\u06ab\u0005:\u0000\u0000\u06ab"+
		"\u06ad\u0003\u017a\u00bd\u0000\u06ac\u06aa\u0001\u0000\u0000\u0000\u06ac"+
		"\u06ad\u0001\u0000\u0000\u0000\u06ad\u06ae\u0001\u0000\u0000\u0000\u06ae"+
		"\u06af\u0003\u015a\u00ad\u0000\u06af\u0159\u0001\u0000\u0000\u0000\u06b0"+
		"\u06b1\u0003\u0098L\u0000\u06b1\u015b\u0001\u0000\u0000\u0000\u06b2\u06b3"+
		"\u0005v\u0000\u0000\u06b3\u06bc\u0005\u0019\u0000\u0000\u06b4\u06b9\u0003"+
		"\u015e\u00af\u0000\u06b5\u06b6\u0005\u001e\u0000\u0000\u06b6\u06b8\u0003"+
		"\u015e\u00af\u0000\u06b7\u06b5\u0001\u0000\u0000\u0000\u06b8\u06bb\u0001"+
		"\u0000\u0000\u0000\u06b9\u06b7\u0001\u0000\u0000\u0000\u06b9\u06ba\u0001"+
		"\u0000\u0000\u0000\u06ba\u06bd\u0001\u0000\u0000\u0000\u06bb\u06b9\u0001"+
		"\u0000\u0000\u0000\u06bc\u06b4\u0001\u0000\u0000\u0000\u06bc\u06bd\u0001"+
		"\u0000\u0000\u0000\u06bd\u06be\u0001\u0000\u0000\u0000\u06be\u06bf\u0005"+
		"\u001a\u0000\u0000\u06bf\u015d\u0001\u0000\u0000\u0000\u06c0\u06c1\u0003"+
		"N\'\u0000\u06c1\u06c2\u0007\u0014\u0000\u0000\u06c2\u06c3\u0003N\'\u0000"+
		"\u06c3\u015f\u0001\u0000\u0000\u0000\u06c4\u06c7\u0003\u0162\u00b1\u0000"+
		"\u06c5\u06c7\u0003\u0164\u00b2\u0000\u06c6\u06c4\u0001\u0000\u0000\u0000"+
		"\u06c6\u06c5\u0001\u0000\u0000\u0000\u06c7\u0161\u0001\u0000\u0000\u0000"+
		"\u06c8\u06ca\u0005\u0017\u0000\u0000\u06c9\u06cb\u0003L&\u0000\u06ca\u06c9"+
		"\u0001\u0000\u0000\u0000\u06ca\u06cb\u0001\u0000\u0000\u0000\u06cb\u06cc"+
		"\u0001\u0000\u0000\u0000\u06cc\u06cd\u0005\u0018\u0000\u0000\u06cd\u0163"+
		"\u0001\u0000\u0000\u0000\u06ce\u06cf\u00059\u0000\u0000\u06cf\u06d0\u0003"+
		"\u0098L\u0000\u06d0\u0165\u0001\u0000\u0000\u0000\u06d1\u06d2\u0005\u00c2"+
		"\u0000\u0000\u06d2\u06d3\u0003\u0168\u00b4\u0000\u06d3\u06d4\u0005\u00c7"+
		"\u0000\u0000\u06d4\u0167\u0001\u0000\u0000\u0000\u06d5\u06db\u0003\u0170"+
		"\u00b8\u0000\u06d6\u06d7\u0003\u0172\u00b9\u0000\u06d7\u06d8\u0003\u0170"+
		"\u00b8\u0000\u06d8\u06da\u0001\u0000\u0000\u0000\u06d9\u06d6\u0001\u0000"+
		"\u0000\u0000\u06da\u06dd\u0001\u0000\u0000\u0000\u06db\u06d9\u0001\u0000"+
		"\u0000\u0000\u06db\u06dc\u0001\u0000\u0000\u0000\u06dc\u0169\u0001\u0000"+
		"\u0000\u0000\u06dd\u06db\u0001\u0000\u0000\u0000\u06de\u06df\u0007\u0015"+
		"\u0000\u0000\u06df\u016b\u0001\u0000\u0000\u0000\u06e0\u06e1\u0007\u0016"+
		"\u0000\u0000\u06e1\u016d\u0001\u0000\u0000\u0000\u06e2\u06e3\u0007\u0017"+
		"\u0000\u0000\u06e3\u016f\u0001\u0000\u0000\u0000\u06e4\u06ef\u0005\u00c5"+
		"\u0000\u0000\u06e5\u06e6\u0003\u016a\u00b5\u0000\u06e6\u06e7\u0003\u016c"+
		"\u00b6\u0000\u06e7\u06ef\u0001\u0000\u0000\u0000\u06e8\u06e9\u0003\u016e"+
		"\u00b7\u0000\u06e9\u06ea\u0003\u016a\u00b5\u0000\u06ea\u06eb\u0003\u016a"+
		"\u00b5\u0000\u06eb\u06ef\u0001\u0000\u0000\u0000\u06ec\u06ef\u0003\u016a"+
		"\u00b5\u0000\u06ed\u06ef\u0005\u0019\u0000\u0000\u06ee\u06e4\u0001\u0000"+
		"\u0000\u0000\u06ee\u06e5\u0001\u0000\u0000\u0000\u06ee\u06e8\u0001\u0000"+
		"\u0000\u0000\u06ee\u06ec\u0001\u0000\u0000\u0000\u06ee\u06ed\u0001\u0000"+
		"\u0000\u0000\u06ef\u06f2\u0001\u0000\u0000\u0000\u06f0\u06ee\u0001\u0000"+
		"\u0000\u0000\u06f0\u06f1\u0001\u0000\u0000\u0000\u06f1\u0171\u0001\u0000"+
		"\u0000\u0000\u06f2\u06f0\u0001\u0000\u0000\u0000\u06f3\u06f4\u0005\u00c6"+
		"\u0000\u0000\u06f4\u06f5\u0003L&\u0000\u06f5\u06f6\u0005\u00c3\u0000\u0000"+
		"\u06f6\u0173\u0001\u0000\u0000\u0000\u06f7\u06f8\u0005*\u0000\u0000\u06f8"+
		"\u06f9\u0003\u00fc~\u0000\u06f9\u0175\u0001\u0000\u0000\u0000\u06fa\u06fc"+
		"\u0003\u01a2\u00d1\u0000\u06fb\u06fd\u0005*\u0000\u0000\u06fc\u06fb\u0001"+
		"\u0000\u0000\u0000\u06fc\u06fd\u0001\u0000\u0000\u0000\u06fd\u0177\u0001"+
		"\u0000\u0000\u0000\u06fe\u06ff\u0005:\u0000\u0000\u06ff\u0700\u0003\u017a"+
		"\u00bd\u0000\u0700\u0179\u0001\u0000\u0000\u0000\u0701\u0702\u0005Y\u0000"+
		"\u0000\u0702\u0703\u0005\u0015\u0000\u0000\u0703\u070b\u0005\u0016\u0000"+
		"\u0000\u0704\u0708\u0003\u017c\u00be\u0000\u0705\u0709\u0005*\u0000\u0000"+
		"\u0706\u0709\u0005\u001b\u0000\u0000\u0707\u0709\u0005\u001c\u0000\u0000"+
		"\u0708\u0705\u0001\u0000\u0000\u0000\u0708\u0706\u0001\u0000\u0000\u0000"+
		"\u0708\u0707\u0001\u0000\u0000\u0000\u0708\u0709\u0001\u0000\u0000\u0000"+
		"\u0709\u070b\u0001\u0000\u0000\u0000\u070a\u0701\u0001\u0000\u0000\u0000"+
		"\u070a\u0704\u0001\u0000\u0000\u0000\u070b\u017b\u0001\u0000\u0000\u0000"+
		"\u070c\u0716\u0003\u0180\u00c0\u0000\u070d\u070e\u0005p\u0000\u0000\u070e"+
		"\u070f\u0005\u0015\u0000\u0000\u070f\u0716\u0005\u0016\u0000\u0000\u0710"+
		"\u0716\u0003\u01a6\u00d3\u0000\u0711\u0716\u0003\u01ac\u00d6\u0000\u0712"+
		"\u0716\u0003\u01b2\u00d9\u0000\u0713\u0716\u0003\u017e\u00bf\u0000\u0714"+
		"\u0716\u0003\u01b8\u00dc\u0000\u0715\u070c\u0001\u0000\u0000\u0000\u0715"+
		"\u070d\u0001\u0000\u0000\u0000\u0715\u0710\u0001\u0000\u0000\u0000\u0715"+
		"\u0711\u0001\u0000\u0000\u0000\u0715\u0712\u0001\u0000\u0000\u0000\u0715"+
		"\u0713\u0001\u0000\u0000\u0000\u0715\u0714\u0001\u0000\u0000\u0000\u0716"+
		"\u017d\u0001\u0000\u0000\u0000\u0717\u0718\u0003\u01c8\u00e4\u0000\u0718"+
		"\u017f\u0001\u0000\u0000\u0000\u0719\u0726\u0003\u0186\u00c3\u0000\u071a"+
		"\u0726\u0003\u0196\u00cb\u0000\u071b\u0726\u0003\u0190\u00c8\u0000\u071c"+
		"\u0726\u0003\u019a\u00cd\u0000\u071d\u0726\u0003\u0194\u00ca\u0000\u071e"+
		"\u0726\u0003\u018e\u00c7\u0000\u071f\u0726\u0003\u018a\u00c5\u0000\u0720"+
		"\u0726\u0003\u0188\u00c4\u0000\u0721\u0726\u0003\u018c\u00c6\u0000\u0722"+
		"\u0726\u0003\u01bc\u00de\u0000\u0723\u0726\u0003\u0184\u00c2\u0000\u0724"+
		"\u0726\u0003\u0182\u00c1\u0000\u0725\u0719\u0001\u0000\u0000\u0000\u0725"+
		"\u071a\u0001\u0000\u0000\u0000\u0725\u071b\u0001\u0000\u0000\u0000\u0725"+
		"\u071c\u0001\u0000\u0000\u0000\u0725\u071d\u0001\u0000\u0000\u0000\u0725"+
		"\u071e\u0001\u0000\u0000\u0000\u0725\u071f\u0001\u0000\u0000\u0000\u0725"+
		"\u0720\u0001\u0000\u0000\u0000\u0725\u0721\u0001\u0000\u0000\u0000\u0725"+
		"\u0722\u0001\u0000\u0000\u0000\u0725\u0723\u0001\u0000\u0000\u0000\u0725"+
		"\u0724\u0001\u0000\u0000\u0000\u0726\u0181\u0001\u0000\u0000\u0000\u0727"+
		"\u0728\u0005\u007f\u0000\u0000\u0728\u072a\u0005\u0015\u0000\u0000\u0729"+
		"\u072b\u0005\u001b\u0000\u0000\u072a\u0729\u0001\u0000\u0000\u0000\u072a"+
		"\u072b\u0001\u0000\u0000\u0000\u072b\u072c\u0001\u0000\u0000\u0000\u072c"+
		"\u072d\u0005\u0016\u0000\u0000\u072d\u0183\u0001\u0000\u0000\u0000\u072e"+
		"\u072f\u0005@\u0000\u0000\u072f\u0730\u0005\u0015\u0000\u0000\u0730\u0731"+
		"\u0005\u0016\u0000\u0000\u0731\u0185\u0001\u0000\u0000\u0000\u0732\u0733"+
		"\u0005U\u0000\u0000\u0733\u0736\u0005\u0015\u0000\u0000\u0734\u0737\u0003"+
		"\u0196\u00cb\u0000\u0735\u0737\u0003\u019a\u00cd\u0000\u0736\u0734\u0001"+
		"\u0000\u0000\u0000\u0736\u0735\u0001\u0000\u0000\u0000\u0736\u0737\u0001"+
		"\u0000\u0000\u0000\u0737\u0738\u0001\u0000\u0000\u0000\u0738\u0739\u0005"+
		"\u0016\u0000\u0000\u0739\u0187\u0001\u0000\u0000\u0000\u073a\u073b\u0005"+
		"\u009a\u0000\u0000\u073b\u073c\u0005\u0015\u0000\u0000\u073c\u073d\u0005"+
		"\u0016\u0000\u0000\u073d\u0189\u0001\u0000\u0000\u0000\u073e\u073f\u0005"+
		"H\u0000\u0000\u073f\u0740\u0005\u0015\u0000\u0000\u0740\u0741\u0005\u0016"+
		"\u0000\u0000\u0741\u018b\u0001\u0000\u0000\u0000\u0742\u0743\u0005|\u0000"+
		"\u0000\u0743\u0744\u0005\u0015\u0000\u0000\u0744\u0745\u0005\u0016\u0000"+
		"\u0000\u0745\u018d\u0001\u0000\u0000\u0000\u0746\u0747\u0005\u008c\u0000"+
		"\u0000\u0747\u074a\u0005\u0015\u0000\u0000\u0748\u074b\u0003\u01cc\u00e6"+
		"\u0000\u0749\u074b\u0003\u01dc\u00ee\u0000\u074a\u0748\u0001\u0000\u0000"+
		"\u0000\u074a\u0749\u0001\u0000\u0000\u0000\u074a\u074b\u0001\u0000\u0000"+
		"\u0000\u074b\u074c\u0001\u0000\u0000\u0000\u074c\u074d\u0005\u0016\u0000"+
		"\u0000\u074d\u018f\u0001\u0000\u0000\u0000\u074e\u074f\u0005=\u0000\u0000"+
		"\u074f\u0755\u0005\u0015\u0000\u0000\u0750\u0753\u0003\u0192\u00c9\u0000"+
		"\u0751\u0752\u0005\u001e\u0000\u0000\u0752\u0754\u0003\u01a4\u00d2\u0000"+
		"\u0753\u0751\u0001\u0000\u0000\u0000\u0753\u0754\u0001\u0000\u0000\u0000"+
		"\u0754\u0756\u0001\u0000\u0000\u0000\u0755\u0750\u0001\u0000\u0000\u0000"+
		"\u0755\u0756\u0001\u0000\u0000\u0000\u0756\u0757\u0001\u0000\u0000\u0000"+
		"\u0757\u0758\u0005\u0016\u0000\u0000\u0758\u0191\u0001\u0000\u0000\u0000"+
		"\u0759\u075c\u0003\u019e\u00cf\u0000\u075a\u075c\u0005\u001b\u0000\u0000"+
		"\u075b\u0759\u0001\u0000\u0000\u0000\u075b\u075a\u0001\u0000\u0000\u0000"+
		"\u075c\u0193\u0001\u0000\u0000\u0000\u075d\u075e\u0005\u0090\u0000\u0000"+
		"\u075e\u075f\u0005\u0015\u0000\u0000\u075f\u0760\u0003\u01ba\u00dd\u0000"+
		"\u0760\u0761\u0005\u0016\u0000\u0000\u0761\u0195\u0001\u0000\u0000\u0000"+
		"\u0762\u0763\u0005V\u0000\u0000\u0763\u076c\u0005\u0015\u0000\u0000\u0764"+
		"\u076a\u0003\u0198\u00cc\u0000\u0765\u0766\u0005\u001e\u0000\u0000\u0766"+
		"\u0768\u0003\u01a4\u00d2\u0000\u0767\u0769\u0005*\u0000\u0000\u0768\u0767"+
		"\u0001\u0000\u0000\u0000\u0768\u0769\u0001\u0000\u0000\u0000\u0769\u076b"+
		"\u0001\u0000\u0000\u0000\u076a\u0765\u0001\u0000\u0000\u0000\u076a\u076b"+
		"\u0001\u0000\u0000\u0000\u076b\u076d\u0001\u0000\u0000\u0000\u076c\u0764"+
		"\u0001\u0000\u0000\u0000\u076c\u076d\u0001\u0000\u0000\u0000\u076d\u076e"+
		"\u0001\u0000\u0000\u0000\u076e\u076f\u0005\u0016\u0000\u0000\u076f\u0197"+
		"\u0001\u0000\u0000\u0000\u0770\u0773\u0003\u01a0\u00d0\u0000\u0771\u0773"+
		"\u0005\u001b\u0000\u0000\u0772\u0770\u0001\u0000\u0000\u0000\u0772\u0771"+
		"\u0001\u0000\u0000\u0000\u0773\u0199\u0001\u0000\u0000\u0000\u0774\u0775"+
		"\u0005\u0091\u0000\u0000\u0775\u0776\u0005\u0015\u0000\u0000\u0776\u0777"+
		"\u0003\u019c\u00ce\u0000\u0777\u0778\u0005\u0016\u0000\u0000\u0778\u019b"+
		"\u0001\u0000\u0000\u0000\u0779\u077a\u0003\u01a0\u00d0\u0000\u077a\u019d"+
		"\u0001\u0000\u0000\u0000\u077b\u077c\u0003\u01c8\u00e4\u0000\u077c\u019f"+
		"\u0001\u0000\u0000\u0000\u077d\u077e\u0003\u01c8\u00e4\u0000\u077e\u01a1"+
		"\u0001\u0000\u0000\u0000\u077f\u0780\u0003\u01a4\u00d2\u0000\u0780\u01a3"+
		"\u0001\u0000\u0000\u0000\u0781\u0782\u0003\u01c8\u00e4\u0000\u0782\u01a5"+
		"\u0001\u0000\u0000\u0000\u0783\u0785\u0003B!\u0000\u0784\u0783\u0001\u0000"+
		"\u0000\u0000\u0785\u0788\u0001\u0000\u0000\u0000\u0786\u0784\u0001\u0000"+
		"\u0000\u0000\u0786\u0787\u0001\u0000\u0000\u0000\u0787\u078b\u0001\u0000"+
		"\u0000\u0000\u0788\u0786\u0001\u0000\u0000\u0000\u0789\u078c\u0003\u01a8"+
		"\u00d4\u0000\u078a\u078c\u0003\u01aa\u00d5\u0000\u078b\u0789\u0001\u0000"+
		"\u0000\u0000\u078b\u078a\u0001\u0000\u0000\u0000\u078c\u01a7\u0001\u0000"+
		"\u0000\u0000\u078d\u078e\u0005c\u0000\u0000\u078e\u078f\u0005\u0015\u0000"+
		"\u0000\u078f\u0790\u0005\u001b\u0000\u0000\u0790\u0791\u0005\u0016\u0000"+
		"\u0000\u0791\u01a9\u0001\u0000\u0000\u0000\u0792\u0793\u0005c\u0000\u0000"+
		"\u0793\u079c\u0005\u0015\u0000\u0000\u0794\u0799\u0003\u017a\u00bd\u0000"+
		"\u0795\u0796\u0005\u001e\u0000\u0000\u0796\u0798\u0003\u017a\u00bd\u0000"+
		"\u0797\u0795\u0001\u0000\u0000\u0000\u0798\u079b\u0001\u0000\u0000\u0000"+
		"\u0799\u0797\u0001\u0000\u0000\u0000\u0799\u079a\u0001\u0000\u0000\u0000"+
		"\u079a\u079d\u0001\u0000\u0000\u0000\u079b\u0799\u0001\u0000\u0000\u0000"+
		"\u079c\u0794\u0001\u0000\u0000\u0000\u079c\u079d\u0001\u0000\u0000\u0000"+
		"\u079d\u079e\u0001\u0000\u0000\u0000\u079e\u079f\u0005\u0016\u0000\u0000"+
		"\u079f\u07a0\u0005:\u0000\u0000\u07a0\u07a1\u0003\u017a\u00bd\u0000\u07a1"+
		"\u01ab\u0001\u0000\u0000\u0000\u07a2\u07a5\u0003\u01ae\u00d7\u0000\u07a3"+
		"\u07a5\u0003\u01b0\u00d8\u0000\u07a4\u07a2\u0001\u0000\u0000\u0000\u07a4"+
		"\u07a3\u0001\u0000\u0000\u0000\u07a5\u01ad\u0001\u0000\u0000\u0000\u07a6"+
		"\u07a7\u0005v\u0000\u0000\u07a7\u07a8\u0005\u0015\u0000\u0000\u07a8\u07a9"+
		"\u0005\u001b\u0000\u0000\u07a9\u07aa\u0005\u0016\u0000\u0000\u07aa\u01af"+
		"\u0001\u0000\u0000\u0000\u07ab\u07ac\u0005v\u0000\u0000\u07ac\u07ad\u0005"+
		"\u0015\u0000\u0000\u07ad\u07ae\u0003\u01c8\u00e4\u0000\u07ae\u07af\u0005"+
		"\u001e\u0000\u0000\u07af\u07b0\u0003\u017a\u00bd\u0000\u07b0\u07b1\u0005"+
		"\u0016\u0000\u0000\u07b1\u01b1\u0001\u0000\u0000\u0000\u07b2\u07b5\u0003"+
		"\u01b4\u00da\u0000\u07b3\u07b5\u0003\u01b6\u00db\u0000\u07b4\u07b2\u0001"+
		"\u0000\u0000\u0000\u07b4\u07b3\u0001\u0000\u0000\u0000\u07b5\u01b3\u0001"+
		"\u0000\u0000\u0000\u07b6\u07b7\u00059\u0000\u0000\u07b7\u07b8\u0005\u0015"+
		"\u0000\u0000\u07b8\u07b9\u0005\u001b\u0000\u0000\u07b9\u07ba\u0005\u0016"+
		"\u0000\u0000\u07ba\u01b5\u0001\u0000\u0000\u0000\u07bb\u07bc\u00059\u0000"+
		"\u0000\u07bc\u07bd\u0005\u0015\u0000\u0000\u07bd\u07be\u0003\u017a\u00bd"+
		"\u0000\u07be\u07bf\u0005\u0016\u0000\u0000\u07bf\u01b7\u0001\u0000\u0000"+
		"\u0000\u07c0\u07c1\u0005\u0015\u0000\u0000\u07c1\u07c2\u0003\u017c\u00be"+
		"\u0000\u07c2\u07c3\u0005\u0016\u0000\u0000\u07c3\u01b9\u0001\u0000\u0000"+
		"\u0000\u07c4\u07c5\u0003\u019e\u00cf\u0000\u07c5\u01bb\u0001\u0000\u0000"+
		"\u0000\u07c6\u07cc\u0003\u01be\u00df\u0000\u07c7\u07cc\u0003\u01c0\u00e0"+
		"\u0000\u07c8\u07cc\u0003\u01c2\u00e1\u0000\u07c9\u07cc\u0003\u01c4\u00e2"+
		"\u0000\u07ca\u07cc\u0003\u01c6\u00e3\u0000\u07cb\u07c6\u0001\u0000\u0000"+
		"\u0000\u07cb\u07c7\u0001\u0000\u0000\u0000\u07cb\u07c8\u0001\u0000\u0000"+
		"\u0000\u07cb\u07c9\u0001\u0000\u0000\u0000\u07cb\u07ca\u0001\u0000\u0000"+
		"\u0000\u07cc\u01bd\u0001\u0000\u0000\u0000\u07cd\u07ce\u0005\u00ac\u0000"+
		"\u0000\u07ce\u07d0\u0005\u0015\u0000\u0000\u07cf\u07d1\u0003\u01dc\u00ee"+
		"\u0000\u07d0\u07cf\u0001\u0000\u0000\u0000\u07d0\u07d1\u0001\u0000\u0000"+
		"\u0000\u07d1\u07d2\u0001\u0000\u0000\u0000\u07d2\u07d3\u0005\u0016\u0000"+
		"\u0000\u07d3\u01bf\u0001\u0000\u0000\u0000\u07d4\u07d5\u0005\u00b0\u0000"+
		"\u0000\u07d5\u07d7\u0005\u0015\u0000\u0000\u07d6\u07d8\u0003\u01dc\u00ee"+
		"\u0000\u07d7\u07d6\u0001\u0000\u0000\u0000\u07d7\u07d8\u0001\u0000\u0000"+
		"\u0000\u07d8\u07d9\u0001\u0000\u0000\u0000\u07d9\u07da\u0005\u0016\u0000"+
		"\u0000\u07da\u01c1\u0001\u0000\u0000\u0000\u07db\u07dc\u0005\u00af\u0000"+
		"\u0000\u07dc\u07de\u0005\u0015\u0000\u0000\u07dd\u07df\u0003\u01dc\u00ee"+
		"\u0000\u07de\u07dd\u0001\u0000\u0000\u0000\u07de\u07df\u0001\u0000\u0000"+
		"\u0000\u07df\u07e0\u0001\u0000\u0000\u0000\u07e0\u07e1\u0005\u0016\u0000"+
		"\u0000\u07e1\u01c3\u0001\u0000\u0000\u0000\u07e2\u07e3\u0005\u00ad\u0000"+
		"\u0000\u07e3\u07e5\u0005\u0015\u0000\u0000\u07e4\u07e6\u0003\u01dc\u00ee"+
		"\u0000\u07e5\u07e4\u0001\u0000\u0000\u0000\u07e5\u07e6\u0001\u0000\u0000"+
		"\u0000\u07e6\u07e7\u0001\u0000\u0000\u0000\u07e7\u07e8\u0005\u0016\u0000"+
		"\u0000\u07e8\u01c5\u0001\u0000\u0000\u0000\u07e9\u07ea\u0005\u00ae\u0000"+
		"\u0000\u07ea\u07ec\u0005\u0015\u0000\u0000\u07eb\u07ed\u0003\u01dc\u00ee"+
		"\u0000\u07ec\u07eb\u0001\u0000\u0000\u0000\u07ec\u07ed\u0001\u0000\u0000"+
		"\u0000\u07ed\u07ee\u0001\u0000\u0000\u0000\u07ee\u07ef\u0005\u0016\u0000"+
		"\u0000\u07ef\u01c7\u0001\u0000\u0000\u0000\u07f0\u07f3\u0003\u01ca\u00e5"+
		"\u0000\u07f1\u07f3\u0005\u00b8\u0000\u0000\u07f2\u07f0\u0001\u0000\u0000"+
		"\u0000\u07f2\u07f1\u0001\u0000\u0000\u0000\u07f3\u01c9\u0001\u0000\u0000"+
		"\u0000\u07f4\u07f7\u0005\u00b9\u0000\u0000\u07f5\u07f7\u0003\u01cc\u00e6"+
		"\u0000\u07f6\u07f4\u0001\u0000\u0000\u0000\u07f6\u07f5\u0001\u0000\u0000"+
		"\u0000\u07f7\u01cb\u0001\u0000\u0000\u0000\u07f8\u07fb\u0005\u00bc\u0000"+
		"\u0000\u07f9\u07fb\u0003\u01d0\u00e8\u0000\u07fa\u07f8\u0001\u0000\u0000"+
		"\u0000\u07fa\u07f9\u0001\u0000\u0000\u0000\u07fb\u01cd\u0001\u0000\u0000"+
		"\u0000\u07fc\u0801\u0005\u00b9\u0000\u0000\u07fd\u0801\u0005\u00bc\u0000"+
		"\u0000\u07fe\u0801\u0005\u00b8\u0000\u0000\u07ff\u0801\u0003\u01d4\u00ea"+
		"\u0000\u0800\u07fc\u0001\u0000\u0000\u0000\u0800\u07fd\u0001\u0000\u0000"+
		"\u0000\u0800\u07fe\u0001\u0000\u0000\u0000\u0800\u07ff\u0001\u0000\u0000"+
		"\u0000\u0801\u01cf\u0001\u0000\u0000\u0000\u0802\u0805\u0003\u01d4\u00ea"+
		"\u0000\u0803\u0805\u0003\u01d2\u00e9\u0000\u0804\u0802\u0001\u0000\u0000"+
		"\u0000\u0804\u0803\u0001\u0000\u0000\u0000\u0805\u01d1\u0001\u0000\u0000"+
		"\u0000\u0806\u0807\u0007\u0018\u0000\u0000\u0807\u01d3\u0001\u0000\u0000"+
		"\u0000\u0808\u0809\u0007\u0019\u0000\u0000\u0809\u01d5\u0001\u0000\u0000"+
		"\u0000\u080a\u080b\u0003\u01dc\u00ee\u0000\u080b\u01d7\u0001\u0000\u0000"+
		"\u0000\u080c\u0813\u0005\u000b\u0000\u0000\u080d\u0812\u0005\t\u0000\u0000"+
		"\u080e\u0812\u0005\n\u0000\u0000\u080f\u0812\u0005\u0001\u0000\u0000\u0810"+
		"\u0812\u0003\u01de\u00ef\u0000\u0811\u080d\u0001\u0000\u0000\u0000\u0811"+
		"\u080e\u0001\u0000\u0000\u0000\u0811\u080f\u0001\u0000\u0000\u0000\u0811"+
		"\u0810\u0001\u0000\u0000\u0000\u0812\u0815\u0001\u0000\u0000\u0000\u0813"+
		"\u0811\u0001\u0000\u0000\u0000\u0813\u0814\u0001\u0000\u0000\u0000\u0814"+
		"\u0816\u0001\u0000\u0000\u0000\u0815\u0813\u0001\u0000\u0000\u0000\u0816"+
		"\u0817\u0005\u000b\u0000\u0000\u0817\u01d9\u0001\u0000\u0000\u0000\u0818"+
		"\u081f\u0005\f\u0000\u0000\u0819\u081e\u0005\t\u0000\u0000\u081a\u081e"+
		"\u0005\n\u0000\u0000\u081b\u081e\u0005\u0002\u0000\u0000\u081c\u081e\u0003"+
		"\u01e0\u00f0\u0000\u081d\u0819\u0001\u0000\u0000\u0000\u081d\u081a\u0001"+
		"\u0000\u0000\u0000\u081d\u081b\u0001\u0000\u0000\u0000\u081d\u081c\u0001"+
		"\u0000\u0000\u0000\u081e\u0821\u0001\u0000\u0000\u0000\u081f\u081d\u0001"+
		"\u0000\u0000\u0000\u081f\u0820\u0001\u0000\u0000\u0000\u0820\u0822\u0001"+
		"\u0000\u0000\u0000\u0821\u081f\u0001\u0000\u0000\u0000\u0822\u0823\u0005"+
		"\f\u0000\u0000\u0823\u01db\u0001\u0000\u0000\u0000\u0824\u0827\u0003\u01d8"+
		"\u00ec\u0000\u0825\u0827\u0003\u01da\u00ed\u0000\u0826\u0824\u0001\u0000"+
		"\u0000\u0000\u0826\u0825\u0001\u0000\u0000\u0000\u0827\u01dd\u0001\u0000"+
		"\u0000\u0000\u0828\u082a\u0005\u00c4\u0000\u0000\u0829\u0828\u0001\u0000"+
		"\u0000\u0000\u082a\u082b\u0001\u0000\u0000\u0000\u082b\u0829\u0001\u0000"+
		"\u0000\u0000\u082b\u082c\u0001\u0000\u0000\u0000\u082c\u083a\u0001\u0000"+
		"\u0000\u0000\u082d\u082f\u0005\u0019\u0000\u0000\u082e\u0830\u0003L&\u0000"+
		"\u082f\u082e\u0001\u0000\u0000\u0000\u082f\u0830\u0001\u0000\u0000\u0000"+
		"\u0830\u0832\u0001\u0000\u0000\u0000\u0831\u0833\u0005\u001a\u0000\u0000"+
		"\u0832\u0831\u0001\u0000\u0000\u0000\u0832\u0833\u0001\u0000\u0000\u0000"+
		"\u0833\u083a\u0001\u0000\u0000\u0000\u0834\u083a\u0005\u001a\u0000\u0000"+
		"\u0835\u083a\u0005\u0003\u0000\u0000\u0836\u083a\u0005\u0004\u0000\u0000"+
		"\u0837\u083a\u0003\u01e2\u00f1\u0000\u0838\u083a\u0003\u01da\u00ed\u0000"+
		"\u0839\u0829\u0001\u0000\u0000\u0000\u0839\u082d\u0001\u0000\u0000\u0000"+
		"\u0839\u0834\u0001\u0000\u0000\u0000\u0839\u0835\u0001\u0000\u0000\u0000"+
		"\u0839\u0836\u0001\u0000\u0000\u0000\u0839\u0837\u0001\u0000\u0000\u0000"+
		"\u0839\u0838\u0001\u0000\u0000\u0000\u083a\u01df\u0001\u0000\u0000\u0000"+
		"\u083b\u083d\u0005\u00c4\u0000\u0000\u083c\u083b\u0001\u0000\u0000\u0000"+
		"\u083d\u083e\u0001\u0000\u0000\u0000\u083e\u083c\u0001\u0000\u0000\u0000"+
		"\u083e\u083f\u0001\u0000\u0000\u0000\u083f\u084d\u0001\u0000\u0000\u0000"+
		"\u0840\u0842\u0005\u0019\u0000\u0000\u0841\u0843\u0003L&\u0000\u0842\u0841"+
		"\u0001\u0000\u0000\u0000\u0842\u0843\u0001\u0000\u0000\u0000\u0843\u0845"+
		"\u0001\u0000\u0000\u0000\u0844\u0846\u0005\u001a\u0000\u0000\u0845\u0844"+
		"\u0001\u0000\u0000\u0000\u0845\u0846\u0001\u0000\u0000\u0000\u0846\u084d"+
		"\u0001\u0000\u0000\u0000\u0847\u084d\u0005\u001a\u0000\u0000\u0848\u084d"+
		"\u0005\u0003\u0000\u0000\u0849\u084d\u0005\u0004\u0000\u0000\u084a\u084d"+
		"\u0003\u01e2\u00f1\u0000\u084b\u084d\u0003\u01d8\u00ec\u0000\u084c\u083c"+
		"\u0001\u0000\u0000\u0000\u084c\u0840\u0001\u0000\u0000\u0000\u084c\u0847"+
		"\u0001\u0000\u0000\u0000\u084c\u0848\u0001\u0000\u0000\u0000\u084c\u0849"+
		"\u0001\u0000\u0000\u0000\u084c\u084a\u0001\u0000\u0000\u0000\u084c\u084b"+
		"\u0001\u0000\u0000\u0000\u084d\u01e1\u0001\u0000\u0000\u0000\u084e\u0851"+
		"\u0003\u01d0\u00e8\u0000\u084f\u0851\u0007\u001a\u0000\u0000\u0850\u084e"+
		"\u0001\u0000\u0000\u0000\u0850\u084f\u0001\u0000\u0000\u0000\u0851\u0852"+
		"\u0001\u0000\u0000\u0000\u0852\u0850\u0001\u0000\u0000\u0000\u0852\u0853"+
		"\u0001\u0000\u0000\u0000\u0853\u01e3\u0001\u0000\u0000\u0000\u00cc\u01e5"+
		"\u01e8\u01eb\u01ef\u01f8\u0210\u0216\u021a\u0221\u0228\u0238\u0264\u026b"+
		"\u0271\u027a\u027d\u0286\u028e\u0297\u029a\u02a8\u02af\u02b1\u02bc\u02c3"+
		"\u02c5\u02cd\u02d1\u02d5\u02dc\u02e2\u02e7\u02f0\u02f7\u0309\u0314\u031a"+
		"\u0322\u0329\u0331\u0337\u033a\u033d\u034f\u0355\u035d\u0364\u036a\u0371"+
		"\u037e\u0387\u038a\u038f\u0394\u03a6\u03ac\u03b0\u03b4\u03b7\u03c0\u03c6"+
		"\u03cb\u03cd\u03d1\u03d8\u03df\u03e8\u03f4\u03fe\u040c\u0411\u041b\u0426"+
		"\u0436\u0444\u044a\u0453\u045c\u047a\u0482\u0489\u048d\u0494\u049a\u04a1"+
		"\u04a9\u04b1\u04b9\u04c0\u04c6\u04cc\u04d2\u04d9\u04e2\u04ea\u04f4\u04fd"+
		"\u0503\u050c\u0517\u051c\u0521\u0528\u052d\u0531\u0539\u0540\u0548\u0552"+
		"\u0556\u055b\u0561\u0563\u056c\u056f\u0576\u0584\u0589\u0598\u059c\u05a7"+
		"\u05b8\u05bc\u05c1\u05ca\u05de\u05e6\u05e8\u05f2\u05f4\u05fb\u0600\u0607"+
		"\u060a\u060f\u0616\u0619\u0621\u062c\u0636\u063e\u064f\u0652\u066e\u067a"+
		"\u0681\u0697\u069d\u06a7\u06ac\u06b9\u06bc\u06c6\u06ca\u06db\u06ee\u06f0"+
		"\u06fc\u0708\u070a\u0715\u0725\u072a\u0736\u074a\u0753\u0755\u075b\u0768"+
		"\u076a\u076c\u0772\u0786\u078b\u0799\u079c\u07a4\u07b4\u07cb\u07d0\u07d7"+
		"\u07de\u07e5\u07ec\u07f2\u07f6\u07fa\u0800\u0804\u0811\u0813\u081d\u081f"+
		"\u0826\u082b\u082f\u0832\u0839\u083e\u0842\u0845\u084c\u0850\u0852";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}