// Generated from ./src/main/java/org/rumbledb/parser/XQueryParser.g4 by ANTLR 4.8

// Java header
package org.rumbledb.parser;

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

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class XQueryParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

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
			while (((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (KW_COUNT - 76)) | (1L << (KW_FOR - 76)) | (1L << (KW_GROUP - 76)) | (1L << (KW_LET - 76)) | (1L << (KW_ORDER - 76)))) != 0) || _la==KW_STABLE || _la==KW_WHERE) {
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
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & ((1L << (IntegerLiteral - 5)) | (1L << (DecimalLiteral - 5)) | (1L << (DoubleLiteral - 5)) | (1L << (DFPropertyName - 5)) | (1L << (Quot - 5)) | (1L << (Apos - 5)) | (1L << (COMMENT - 5)) | (1L << (PI - 5)) | (1L << (PRAGMA - 5)) | (1L << (LPAREN - 5)) | (1L << (LBRACKET - 5)) | (1L << (STAR - 5)) | (1L << (PLUS - 5)) | (1L << (MINUS - 5)) | (1L << (DOT - 5)) | (1L << (DDOT - 5)) | (1L << (SLASH - 5)) | (1L << (DSLASH - 5)) | (1L << (LANGLE - 5)) | (1L << (QUESTION - 5)) | (1L << (AT - 5)) | (1L << (DOLLAR - 5)) | (1L << (MOD - 5)) | (1L << (KW_ALLOWING - 5)) | (1L << (KW_ANCESTOR - 5)) | (1L << (KW_ANCESTOR_OR_SELF - 5)) | (1L << (KW_AND - 5)) | (1L << (KW_ARRAY - 5)) | (1L << (KW_AS - 5)) | (1L << (KW_ASCENDING - 5)) | (1L << (KW_AT - 5)) | (1L << (KW_ATTRIBUTE - 5)) | (1L << (KW_BASE_URI - 5)) | (1L << (KW_BOUNDARY_SPACE - 5)) | (1L << (KW_BINARY - 5)) | (1L << (KW_BY - 5)) | (1L << (KW_CASE - 5)) | (1L << (KW_CAST - 5)) | (1L << (KW_CASTABLE - 5)))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (KW_CATCH - 69)) | (1L << (KW_CHILD - 69)) | (1L << (KW_COLLATION - 69)) | (1L << (KW_COMMENT - 69)) | (1L << (KW_CONSTRUCTION - 69)) | (1L << (KW_CONTEXT - 69)) | (1L << (KW_COPY_NS - 69)) | (1L << (KW_COUNT - 69)) | (1L << (KW_DECLARE - 69)) | (1L << (KW_DEFAULT - 69)) | (1L << (KW_DESCENDANT - 69)) | (1L << (KW_DESCENDANT_OR_SELF - 69)) | (1L << (KW_DESCENDING - 69)) | (1L << (KW_DECIMAL_FORMAT - 69)) | (1L << (KW_DIV - 69)) | (1L << (KW_DOCUMENT - 69)) | (1L << (KW_DOCUMENT_NODE - 69)) | (1L << (KW_ELEMENT - 69)) | (1L << (KW_ELSE - 69)) | (1L << (KW_EMPTY - 69)) | (1L << (KW_EMPTY_SEQUENCE - 69)) | (1L << (KW_ENCODING - 69)) | (1L << (KW_END - 69)) | (1L << (KW_EQ - 69)) | (1L << (KW_EVERY - 69)) | (1L << (KW_EXCEPT - 69)) | (1L << (KW_EXTERNAL - 69)) | (1L << (KW_FOLLOWING - 69)) | (1L << (KW_FOLLOWING_SIBLING - 69)) | (1L << (KW_FOR - 69)) | (1L << (KW_FUNCTION - 69)) | (1L << (KW_GE - 69)) | (1L << (KW_GREATEST - 69)) | (1L << (KW_GROUP - 69)) | (1L << (KW_GT - 69)) | (1L << (KW_IDIV - 69)) | (1L << (KW_IF - 69)) | (1L << (KW_IMPORT - 69)) | (1L << (KW_IN - 69)) | (1L << (KW_INHERIT - 69)) | (1L << (KW_INSTANCE - 69)) | (1L << (KW_INTERSECT - 69)) | (1L << (KW_IS - 69)) | (1L << (KW_ITEM - 69)) | (1L << (KW_LAX - 69)) | (1L << (KW_LE - 69)) | (1L << (KW_LEAST - 69)) | (1L << (KW_LET - 69)) | (1L << (KW_LT - 69)) | (1L << (KW_MAP - 69)) | (1L << (KW_MOD - 69)) | (1L << (KW_MODULE - 69)) | (1L << (KW_NAMESPACE - 69)) | (1L << (KW_NE - 69)) | (1L << (KW_NEXT - 69)) | (1L << (KW_NAMESPACE_NODE - 69)) | (1L << (KW_NO_INHERIT - 69)) | (1L << (KW_NO_PRESERVE - 69)) | (1L << (KW_NODE - 69)) | (1L << (KW_OF - 69)) | (1L << (KW_ONLY - 69)) | (1L << (KW_OPTION - 69)) | (1L << (KW_OR - 69)) | (1L << (KW_ORDER - 69)))) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (KW_ORDERED - 133)) | (1L << (KW_ORDERING - 133)) | (1L << (KW_PARENT - 133)) | (1L << (KW_PRECEDING - 133)) | (1L << (KW_PRECEDING_SIBLING - 133)) | (1L << (KW_PRESERVE - 133)) | (1L << (KW_PI - 133)) | (1L << (KW_RETURN - 133)) | (1L << (KW_SATISFIES - 133)) | (1L << (KW_SCHEMA - 133)) | (1L << (KW_SCHEMA_ATTR - 133)) | (1L << (KW_SCHEMA_ELEM - 133)) | (1L << (KW_SELF - 133)) | (1L << (KW_SLIDING - 133)) | (1L << (KW_SOME - 133)) | (1L << (KW_STABLE - 133)) | (1L << (KW_START - 133)) | (1L << (KW_STRICT - 133)) | (1L << (KW_STRIP - 133)) | (1L << (KW_SWITCH - 133)) | (1L << (KW_TEXT - 133)) | (1L << (KW_THEN - 133)) | (1L << (KW_TO - 133)) | (1L << (KW_TREAT - 133)) | (1L << (KW_TRY - 133)) | (1L << (KW_TUMBLING - 133)) | (1L << (KW_TYPE - 133)) | (1L << (KW_TYPESWITCH - 133)) | (1L << (KW_UNION - 133)) | (1L << (KW_UNORDERED - 133)) | (1L << (KW_UPDATE - 133)) | (1L << (KW_VALIDATE - 133)) | (1L << (KW_VARIABLE - 133)) | (1L << (KW_VERSION - 133)) | (1L << (KW_WHEN - 133)) | (1L << (KW_WHERE - 133)) | (1L << (KW_WINDOW - 133)) | (1L << (KW_XQUERY - 133)) | (1L << (KW_ARRAY_NODE - 133)) | (1L << (KW_BOOLEAN_NODE - 133)) | (1L << (KW_NULL_NODE - 133)) | (1L << (KW_NUMBER_NODE - 133)) | (1L << (KW_OBJECT_NODE - 133)) | (1L << (KW_REPLACE - 133)) | (1L << (KW_WITH - 133)) | (1L << (KW_VALUE - 133)) | (1L << (KW_INSERT - 133)) | (1L << (KW_INTO - 133)) | (1L << (KW_DELETE - 133)) | (1L << (KW_RENAME - 133)) | (1L << (URIQualifiedName - 133)) | (1L << (FullQName - 133)) | (1L << (NCNameWithLocalWildcard - 133)) | (1L << (NCNameWithPrefixWildcard - 133)) | (1L << (NCName - 133)) | (1L << (ENTER_STRING - 133)))) != 0)) {
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
					if ( !(_la==STAR || ((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (KW_DIV - 83)) | (1L << (KW_IDIV - 83)) | (1L << (KW_MOD - 83)))) != 0)) ) {
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
			if ( !(((((_la - 92)) & ~0x3f) == 0 && ((1L << (_la - 92)) & ((1L << (KW_EQ - 92)) | (1L << (KW_GE - 92)) | (1L << (KW_GT - 92)) | (1L << (KW_LE - 92)) | (1L << (KW_LT - 92)) | (1L << (KW_NE - 92)))) != 0)) ) {
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
			if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (KW_ATTRIBUTE - 61)) | (1L << (KW_CHILD - 61)) | (1L << (KW_DESCENDANT - 61)) | (1L << (KW_DESCENDANT_OR_SELF - 61)) | (1L << (KW_FOLLOWING - 61)) | (1L << (KW_FOLLOWING_SIBLING - 61)))) != 0) || _la==KW_SELF) ) {
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
			if ( !(_la==KW_ANCESTOR || _la==KW_ANCESTOR_OR_SELF || ((((_la - 135)) & ~0x3f) == 0 && ((1L << (_la - 135)) & ((1L << (KW_PARENT - 135)) | (1L << (KW_PRECEDING - 135)) | (1L << (KW_PRECEDING_SIBLING - 135)))) != 0)) ) {
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
	public static class AllNamesContext extends WildcardContext {
		public TerminalNode STAR() { return getToken(XQueryParser.STAR, 0); }
		public AllNamesContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAllNames(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AllWithLocalContext extends WildcardContext {
		public TerminalNode NCNameWithPrefixWildcard() { return getToken(XQueryParser.NCNameWithPrefixWildcard, 0); }
		public AllWithLocalContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQueryParserVisitor ) return ((XQueryParserVisitor<? extends T>)visitor).visitAllWithLocal(this);
			else return visitor.visitChildren(this);
		}
	}
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
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & ((1L << (IntegerLiteral - 5)) | (1L << (DecimalLiteral - 5)) | (1L << (DoubleLiteral - 5)) | (1L << (DFPropertyName - 5)) | (1L << (Quot - 5)) | (1L << (Apos - 5)) | (1L << (COMMENT - 5)) | (1L << (PI - 5)) | (1L << (PRAGMA - 5)) | (1L << (LPAREN - 5)) | (1L << (LBRACKET - 5)) | (1L << (STAR - 5)) | (1L << (PLUS - 5)) | (1L << (MINUS - 5)) | (1L << (DOT - 5)) | (1L << (DDOT - 5)) | (1L << (SLASH - 5)) | (1L << (DSLASH - 5)) | (1L << (LANGLE - 5)) | (1L << (QUESTION - 5)) | (1L << (AT - 5)) | (1L << (DOLLAR - 5)) | (1L << (MOD - 5)) | (1L << (KW_ALLOWING - 5)) | (1L << (KW_ANCESTOR - 5)) | (1L << (KW_ANCESTOR_OR_SELF - 5)) | (1L << (KW_AND - 5)) | (1L << (KW_ARRAY - 5)) | (1L << (KW_AS - 5)) | (1L << (KW_ASCENDING - 5)) | (1L << (KW_AT - 5)) | (1L << (KW_ATTRIBUTE - 5)) | (1L << (KW_BASE_URI - 5)) | (1L << (KW_BOUNDARY_SPACE - 5)) | (1L << (KW_BINARY - 5)) | (1L << (KW_BY - 5)) | (1L << (KW_CASE - 5)) | (1L << (KW_CAST - 5)) | (1L << (KW_CASTABLE - 5)))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (KW_CATCH - 69)) | (1L << (KW_CHILD - 69)) | (1L << (KW_COLLATION - 69)) | (1L << (KW_COMMENT - 69)) | (1L << (KW_CONSTRUCTION - 69)) | (1L << (KW_CONTEXT - 69)) | (1L << (KW_COPY_NS - 69)) | (1L << (KW_COUNT - 69)) | (1L << (KW_DECLARE - 69)) | (1L << (KW_DEFAULT - 69)) | (1L << (KW_DESCENDANT - 69)) | (1L << (KW_DESCENDANT_OR_SELF - 69)) | (1L << (KW_DESCENDING - 69)) | (1L << (KW_DECIMAL_FORMAT - 69)) | (1L << (KW_DIV - 69)) | (1L << (KW_DOCUMENT - 69)) | (1L << (KW_DOCUMENT_NODE - 69)) | (1L << (KW_ELEMENT - 69)) | (1L << (KW_ELSE - 69)) | (1L << (KW_EMPTY - 69)) | (1L << (KW_EMPTY_SEQUENCE - 69)) | (1L << (KW_ENCODING - 69)) | (1L << (KW_END - 69)) | (1L << (KW_EQ - 69)) | (1L << (KW_EVERY - 69)) | (1L << (KW_EXCEPT - 69)) | (1L << (KW_EXTERNAL - 69)) | (1L << (KW_FOLLOWING - 69)) | (1L << (KW_FOLLOWING_SIBLING - 69)) | (1L << (KW_FOR - 69)) | (1L << (KW_FUNCTION - 69)) | (1L << (KW_GE - 69)) | (1L << (KW_GREATEST - 69)) | (1L << (KW_GROUP - 69)) | (1L << (KW_GT - 69)) | (1L << (KW_IDIV - 69)) | (1L << (KW_IF - 69)) | (1L << (KW_IMPORT - 69)) | (1L << (KW_IN - 69)) | (1L << (KW_INHERIT - 69)) | (1L << (KW_INSTANCE - 69)) | (1L << (KW_INTERSECT - 69)) | (1L << (KW_IS - 69)) | (1L << (KW_ITEM - 69)) | (1L << (KW_LAX - 69)) | (1L << (KW_LE - 69)) | (1L << (KW_LEAST - 69)) | (1L << (KW_LET - 69)) | (1L << (KW_LT - 69)) | (1L << (KW_MAP - 69)) | (1L << (KW_MOD - 69)) | (1L << (KW_MODULE - 69)) | (1L << (KW_NAMESPACE - 69)) | (1L << (KW_NE - 69)) | (1L << (KW_NEXT - 69)) | (1L << (KW_NAMESPACE_NODE - 69)) | (1L << (KW_NO_INHERIT - 69)) | (1L << (KW_NO_PRESERVE - 69)) | (1L << (KW_NODE - 69)) | (1L << (KW_OF - 69)) | (1L << (KW_ONLY - 69)) | (1L << (KW_OPTION - 69)) | (1L << (KW_OR - 69)) | (1L << (KW_ORDER - 69)))) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (KW_ORDERED - 133)) | (1L << (KW_ORDERING - 133)) | (1L << (KW_PARENT - 133)) | (1L << (KW_PRECEDING - 133)) | (1L << (KW_PRECEDING_SIBLING - 133)) | (1L << (KW_PRESERVE - 133)) | (1L << (KW_PI - 133)) | (1L << (KW_RETURN - 133)) | (1L << (KW_SATISFIES - 133)) | (1L << (KW_SCHEMA - 133)) | (1L << (KW_SCHEMA_ATTR - 133)) | (1L << (KW_SCHEMA_ELEM - 133)) | (1L << (KW_SELF - 133)) | (1L << (KW_SLIDING - 133)) | (1L << (KW_SOME - 133)) | (1L << (KW_STABLE - 133)) | (1L << (KW_START - 133)) | (1L << (KW_STRICT - 133)) | (1L << (KW_STRIP - 133)) | (1L << (KW_SWITCH - 133)) | (1L << (KW_TEXT - 133)) | (1L << (KW_THEN - 133)) | (1L << (KW_TO - 133)) | (1L << (KW_TREAT - 133)) | (1L << (KW_TRY - 133)) | (1L << (KW_TUMBLING - 133)) | (1L << (KW_TYPE - 133)) | (1L << (KW_TYPESWITCH - 133)) | (1L << (KW_UNION - 133)) | (1L << (KW_UNORDERED - 133)) | (1L << (KW_UPDATE - 133)) | (1L << (KW_VALIDATE - 133)) | (1L << (KW_VARIABLE - 133)) | (1L << (KW_VERSION - 133)) | (1L << (KW_WHEN - 133)) | (1L << (KW_WHERE - 133)) | (1L << (KW_WINDOW - 133)) | (1L << (KW_XQUERY - 133)) | (1L << (KW_ARRAY_NODE - 133)) | (1L << (KW_BOOLEAN_NODE - 133)) | (1L << (KW_NULL_NODE - 133)) | (1L << (KW_NUMBER_NODE - 133)) | (1L << (KW_OBJECT_NODE - 133)) | (1L << (KW_REPLACE - 133)) | (1L << (KW_WITH - 133)) | (1L << (KW_VALUE - 133)) | (1L << (KW_INSERT - 133)) | (1L << (KW_INTO - 133)) | (1L << (KW_DELETE - 133)) | (1L << (KW_RENAME - 133)) | (1L << (URIQualifiedName - 133)) | (1L << (FullQName - 133)) | (1L << (NCNameWithLocalWildcard - 133)) | (1L << (NCNameWithPrefixWildcard - 133)) | (1L << (NCName - 133)) | (1L << (ENTER_STRING - 133)))) != 0)) {
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
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << DecimalLiteral) | (1L << DoubleLiteral))) != 0)) ) {
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
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & ((1L << (IntegerLiteral - 5)) | (1L << (DecimalLiteral - 5)) | (1L << (DoubleLiteral - 5)) | (1L << (DFPropertyName - 5)) | (1L << (Quot - 5)) | (1L << (Apos - 5)) | (1L << (COMMENT - 5)) | (1L << (PI - 5)) | (1L << (PRAGMA - 5)) | (1L << (LPAREN - 5)) | (1L << (LBRACKET - 5)) | (1L << (STAR - 5)) | (1L << (PLUS - 5)) | (1L << (MINUS - 5)) | (1L << (DOT - 5)) | (1L << (DDOT - 5)) | (1L << (SLASH - 5)) | (1L << (DSLASH - 5)) | (1L << (LANGLE - 5)) | (1L << (QUESTION - 5)) | (1L << (AT - 5)) | (1L << (DOLLAR - 5)) | (1L << (MOD - 5)) | (1L << (KW_ALLOWING - 5)) | (1L << (KW_ANCESTOR - 5)) | (1L << (KW_ANCESTOR_OR_SELF - 5)) | (1L << (KW_AND - 5)) | (1L << (KW_ARRAY - 5)) | (1L << (KW_AS - 5)) | (1L << (KW_ASCENDING - 5)) | (1L << (KW_AT - 5)) | (1L << (KW_ATTRIBUTE - 5)) | (1L << (KW_BASE_URI - 5)) | (1L << (KW_BOUNDARY_SPACE - 5)) | (1L << (KW_BINARY - 5)) | (1L << (KW_BY - 5)) | (1L << (KW_CASE - 5)) | (1L << (KW_CAST - 5)) | (1L << (KW_CASTABLE - 5)))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (KW_CATCH - 69)) | (1L << (KW_CHILD - 69)) | (1L << (KW_COLLATION - 69)) | (1L << (KW_COMMENT - 69)) | (1L << (KW_CONSTRUCTION - 69)) | (1L << (KW_CONTEXT - 69)) | (1L << (KW_COPY_NS - 69)) | (1L << (KW_COUNT - 69)) | (1L << (KW_DECLARE - 69)) | (1L << (KW_DEFAULT - 69)) | (1L << (KW_DESCENDANT - 69)) | (1L << (KW_DESCENDANT_OR_SELF - 69)) | (1L << (KW_DESCENDING - 69)) | (1L << (KW_DECIMAL_FORMAT - 69)) | (1L << (KW_DIV - 69)) | (1L << (KW_DOCUMENT - 69)) | (1L << (KW_DOCUMENT_NODE - 69)) | (1L << (KW_ELEMENT - 69)) | (1L << (KW_ELSE - 69)) | (1L << (KW_EMPTY - 69)) | (1L << (KW_EMPTY_SEQUENCE - 69)) | (1L << (KW_ENCODING - 69)) | (1L << (KW_END - 69)) | (1L << (KW_EQ - 69)) | (1L << (KW_EVERY - 69)) | (1L << (KW_EXCEPT - 69)) | (1L << (KW_EXTERNAL - 69)) | (1L << (KW_FOLLOWING - 69)) | (1L << (KW_FOLLOWING_SIBLING - 69)) | (1L << (KW_FOR - 69)) | (1L << (KW_FUNCTION - 69)) | (1L << (KW_GE - 69)) | (1L << (KW_GREATEST - 69)) | (1L << (KW_GROUP - 69)) | (1L << (KW_GT - 69)) | (1L << (KW_IDIV - 69)) | (1L << (KW_IF - 69)) | (1L << (KW_IMPORT - 69)) | (1L << (KW_IN - 69)) | (1L << (KW_INHERIT - 69)) | (1L << (KW_INSTANCE - 69)) | (1L << (KW_INTERSECT - 69)) | (1L << (KW_IS - 69)) | (1L << (KW_ITEM - 69)) | (1L << (KW_LAX - 69)) | (1L << (KW_LE - 69)) | (1L << (KW_LEAST - 69)) | (1L << (KW_LET - 69)) | (1L << (KW_LT - 69)) | (1L << (KW_MAP - 69)) | (1L << (KW_MOD - 69)) | (1L << (KW_MODULE - 69)) | (1L << (KW_NAMESPACE - 69)) | (1L << (KW_NE - 69)) | (1L << (KW_NEXT - 69)) | (1L << (KW_NAMESPACE_NODE - 69)) | (1L << (KW_NO_INHERIT - 69)) | (1L << (KW_NO_PRESERVE - 69)) | (1L << (KW_NODE - 69)) | (1L << (KW_OF - 69)) | (1L << (KW_ONLY - 69)) | (1L << (KW_OPTION - 69)) | (1L << (KW_OR - 69)) | (1L << (KW_ORDER - 69)))) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (KW_ORDERED - 133)) | (1L << (KW_ORDERING - 133)) | (1L << (KW_PARENT - 133)) | (1L << (KW_PRECEDING - 133)) | (1L << (KW_PRECEDING_SIBLING - 133)) | (1L << (KW_PRESERVE - 133)) | (1L << (KW_PI - 133)) | (1L << (KW_RETURN - 133)) | (1L << (KW_SATISFIES - 133)) | (1L << (KW_SCHEMA - 133)) | (1L << (KW_SCHEMA_ATTR - 133)) | (1L << (KW_SCHEMA_ELEM - 133)) | (1L << (KW_SELF - 133)) | (1L << (KW_SLIDING - 133)) | (1L << (KW_SOME - 133)) | (1L << (KW_STABLE - 133)) | (1L << (KW_START - 133)) | (1L << (KW_STRICT - 133)) | (1L << (KW_STRIP - 133)) | (1L << (KW_SWITCH - 133)) | (1L << (KW_TEXT - 133)) | (1L << (KW_THEN - 133)) | (1L << (KW_TO - 133)) | (1L << (KW_TREAT - 133)) | (1L << (KW_TRY - 133)) | (1L << (KW_TUMBLING - 133)) | (1L << (KW_TYPE - 133)) | (1L << (KW_TYPESWITCH - 133)) | (1L << (KW_UNION - 133)) | (1L << (KW_UNORDERED - 133)) | (1L << (KW_UPDATE - 133)) | (1L << (KW_VALIDATE - 133)) | (1L << (KW_VARIABLE - 133)) | (1L << (KW_VERSION - 133)) | (1L << (KW_WHEN - 133)) | (1L << (KW_WHERE - 133)) | (1L << (KW_WINDOW - 133)) | (1L << (KW_XQUERY - 133)) | (1L << (KW_ARRAY_NODE - 133)) | (1L << (KW_BOOLEAN_NODE - 133)) | (1L << (KW_NULL_NODE - 133)) | (1L << (KW_NUMBER_NODE - 133)) | (1L << (KW_OBJECT_NODE - 133)) | (1L << (KW_REPLACE - 133)) | (1L << (KW_WITH - 133)) | (1L << (KW_VALUE - 133)) | (1L << (KW_INSERT - 133)) | (1L << (KW_INTO - 133)) | (1L << (KW_DELETE - 133)) | (1L << (KW_RENAME - 133)) | (1L << (URIQualifiedName - 133)) | (1L << (FullQName - 133)) | (1L << (NCNameWithLocalWildcard - 133)) | (1L << (NCNameWithPrefixWildcard - 133)) | (1L << (NCName - 133)) | (1L << (ENTER_STRING - 133)))) != 0)) {
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DFPropertyName) | (1L << KW_ALLOWING) | (1L << KW_ANCESTOR) | (1L << KW_ANCESTOR_OR_SELF) | (1L << KW_AND) | (1L << KW_ARRAY) | (1L << KW_AS) | (1L << KW_ASCENDING) | (1L << KW_AT) | (1L << KW_ATTRIBUTE) | (1L << KW_BASE_URI) | (1L << KW_BOUNDARY_SPACE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (KW_BINARY - 64)) | (1L << (KW_BY - 64)) | (1L << (KW_CASE - 64)) | (1L << (KW_CAST - 64)) | (1L << (KW_CASTABLE - 64)) | (1L << (KW_CATCH - 64)) | (1L << (KW_CHILD - 64)) | (1L << (KW_COLLATION - 64)) | (1L << (KW_COMMENT - 64)) | (1L << (KW_CONSTRUCTION - 64)) | (1L << (KW_CONTEXT - 64)) | (1L << (KW_COPY_NS - 64)) | (1L << (KW_COUNT - 64)) | (1L << (KW_DECLARE - 64)) | (1L << (KW_DEFAULT - 64)) | (1L << (KW_DESCENDANT - 64)) | (1L << (KW_DESCENDANT_OR_SELF - 64)) | (1L << (KW_DESCENDING - 64)) | (1L << (KW_DECIMAL_FORMAT - 64)) | (1L << (KW_DIV - 64)) | (1L << (KW_DOCUMENT - 64)) | (1L << (KW_DOCUMENT_NODE - 64)) | (1L << (KW_ELEMENT - 64)) | (1L << (KW_ELSE - 64)) | (1L << (KW_EMPTY - 64)) | (1L << (KW_EMPTY_SEQUENCE - 64)) | (1L << (KW_ENCODING - 64)) | (1L << (KW_END - 64)) | (1L << (KW_EQ - 64)) | (1L << (KW_EVERY - 64)) | (1L << (KW_EXCEPT - 64)) | (1L << (KW_EXTERNAL - 64)) | (1L << (KW_FOLLOWING - 64)) | (1L << (KW_FOLLOWING_SIBLING - 64)) | (1L << (KW_FOR - 64)) | (1L << (KW_FUNCTION - 64)) | (1L << (KW_GE - 64)) | (1L << (KW_GREATEST - 64)) | (1L << (KW_GROUP - 64)) | (1L << (KW_GT - 64)) | (1L << (KW_IDIV - 64)) | (1L << (KW_IF - 64)) | (1L << (KW_IMPORT - 64)) | (1L << (KW_IN - 64)) | (1L << (KW_INHERIT - 64)) | (1L << (KW_INSTANCE - 64)) | (1L << (KW_INTERSECT - 64)) | (1L << (KW_IS - 64)) | (1L << (KW_ITEM - 64)) | (1L << (KW_LAX - 64)) | (1L << (KW_LE - 64)) | (1L << (KW_LEAST - 64)) | (1L << (KW_LET - 64)) | (1L << (KW_LT - 64)) | (1L << (KW_MAP - 64)) | (1L << (KW_MOD - 64)) | (1L << (KW_MODULE - 64)) | (1L << (KW_NAMESPACE - 64)) | (1L << (KW_NE - 64)) | (1L << (KW_NEXT - 64)) | (1L << (KW_NAMESPACE_NODE - 64)) | (1L << (KW_NO_INHERIT - 64)) | (1L << (KW_NO_PRESERVE - 64)) | (1L << (KW_NODE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (KW_OF - 128)) | (1L << (KW_ONLY - 128)) | (1L << (KW_OPTION - 128)) | (1L << (KW_OR - 128)) | (1L << (KW_ORDER - 128)) | (1L << (KW_ORDERED - 128)) | (1L << (KW_ORDERING - 128)) | (1L << (KW_PARENT - 128)) | (1L << (KW_PRECEDING - 128)) | (1L << (KW_PRECEDING_SIBLING - 128)) | (1L << (KW_PRESERVE - 128)) | (1L << (KW_PI - 128)) | (1L << (KW_RETURN - 128)) | (1L << (KW_SATISFIES - 128)) | (1L << (KW_SCHEMA - 128)) | (1L << (KW_SCHEMA_ATTR - 128)) | (1L << (KW_SCHEMA_ELEM - 128)) | (1L << (KW_SELF - 128)) | (1L << (KW_SLIDING - 128)) | (1L << (KW_SOME - 128)) | (1L << (KW_STABLE - 128)) | (1L << (KW_START - 128)) | (1L << (KW_STRICT - 128)) | (1L << (KW_STRIP - 128)) | (1L << (KW_SWITCH - 128)) | (1L << (KW_TEXT - 128)) | (1L << (KW_THEN - 128)) | (1L << (KW_TO - 128)) | (1L << (KW_TREAT - 128)) | (1L << (KW_TRY - 128)) | (1L << (KW_TUMBLING - 128)) | (1L << (KW_TYPE - 128)) | (1L << (KW_TYPESWITCH - 128)) | (1L << (KW_UNION - 128)) | (1L << (KW_UNORDERED - 128)) | (1L << (KW_UPDATE - 128)) | (1L << (KW_VALIDATE - 128)) | (1L << (KW_VARIABLE - 128)) | (1L << (KW_VERSION - 128)) | (1L << (KW_WHEN - 128)) | (1L << (KW_WHERE - 128)) | (1L << (KW_WINDOW - 128)) | (1L << (KW_XQUERY - 128)) | (1L << (KW_ARRAY_NODE - 128)) | (1L << (KW_BOOLEAN_NODE - 128)) | (1L << (KW_NULL_NODE - 128)) | (1L << (KW_NUMBER_NODE - 128)) | (1L << (KW_OBJECT_NODE - 128)) | (1L << (KW_REPLACE - 128)) | (1L << (KW_WITH - 128)) | (1L << (KW_VALUE - 128)) | (1L << (KW_INSERT - 128)) | (1L << (KW_INTO - 128)) | (1L << (KW_DELETE - 128)) | (1L << (KW_RENAME - 128)) | (1L << (FullQName - 128)) | (1L << (NCName - 128)))) != 0)) {
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
				if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & ((1L << (IntegerLiteral - 5)) | (1L << (DecimalLiteral - 5)) | (1L << (DoubleLiteral - 5)) | (1L << (DFPropertyName - 5)) | (1L << (Quot - 5)) | (1L << (Apos - 5)) | (1L << (COMMENT - 5)) | (1L << (PI - 5)) | (1L << (PRAGMA - 5)) | (1L << (LPAREN - 5)) | (1L << (LBRACKET - 5)) | (1L << (STAR - 5)) | (1L << (PLUS - 5)) | (1L << (MINUS - 5)) | (1L << (DOT - 5)) | (1L << (DDOT - 5)) | (1L << (SLASH - 5)) | (1L << (DSLASH - 5)) | (1L << (LANGLE - 5)) | (1L << (QUESTION - 5)) | (1L << (AT - 5)) | (1L << (DOLLAR - 5)) | (1L << (MOD - 5)) | (1L << (KW_ALLOWING - 5)) | (1L << (KW_ANCESTOR - 5)) | (1L << (KW_ANCESTOR_OR_SELF - 5)) | (1L << (KW_AND - 5)) | (1L << (KW_ARRAY - 5)) | (1L << (KW_AS - 5)) | (1L << (KW_ASCENDING - 5)) | (1L << (KW_AT - 5)) | (1L << (KW_ATTRIBUTE - 5)) | (1L << (KW_BASE_URI - 5)) | (1L << (KW_BOUNDARY_SPACE - 5)) | (1L << (KW_BINARY - 5)) | (1L << (KW_BY - 5)) | (1L << (KW_CASE - 5)) | (1L << (KW_CAST - 5)) | (1L << (KW_CASTABLE - 5)))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (KW_CATCH - 69)) | (1L << (KW_CHILD - 69)) | (1L << (KW_COLLATION - 69)) | (1L << (KW_COMMENT - 69)) | (1L << (KW_CONSTRUCTION - 69)) | (1L << (KW_CONTEXT - 69)) | (1L << (KW_COPY_NS - 69)) | (1L << (KW_COUNT - 69)) | (1L << (KW_DECLARE - 69)) | (1L << (KW_DEFAULT - 69)) | (1L << (KW_DESCENDANT - 69)) | (1L << (KW_DESCENDANT_OR_SELF - 69)) | (1L << (KW_DESCENDING - 69)) | (1L << (KW_DECIMAL_FORMAT - 69)) | (1L << (KW_DIV - 69)) | (1L << (KW_DOCUMENT - 69)) | (1L << (KW_DOCUMENT_NODE - 69)) | (1L << (KW_ELEMENT - 69)) | (1L << (KW_ELSE - 69)) | (1L << (KW_EMPTY - 69)) | (1L << (KW_EMPTY_SEQUENCE - 69)) | (1L << (KW_ENCODING - 69)) | (1L << (KW_END - 69)) | (1L << (KW_EQ - 69)) | (1L << (KW_EVERY - 69)) | (1L << (KW_EXCEPT - 69)) | (1L << (KW_EXTERNAL - 69)) | (1L << (KW_FOLLOWING - 69)) | (1L << (KW_FOLLOWING_SIBLING - 69)) | (1L << (KW_FOR - 69)) | (1L << (KW_FUNCTION - 69)) | (1L << (KW_GE - 69)) | (1L << (KW_GREATEST - 69)) | (1L << (KW_GROUP - 69)) | (1L << (KW_GT - 69)) | (1L << (KW_IDIV - 69)) | (1L << (KW_IF - 69)) | (1L << (KW_IMPORT - 69)) | (1L << (KW_IN - 69)) | (1L << (KW_INHERIT - 69)) | (1L << (KW_INSTANCE - 69)) | (1L << (KW_INTERSECT - 69)) | (1L << (KW_IS - 69)) | (1L << (KW_ITEM - 69)) | (1L << (KW_LAX - 69)) | (1L << (KW_LE - 69)) | (1L << (KW_LEAST - 69)) | (1L << (KW_LET - 69)) | (1L << (KW_LT - 69)) | (1L << (KW_MAP - 69)) | (1L << (KW_MOD - 69)) | (1L << (KW_MODULE - 69)) | (1L << (KW_NAMESPACE - 69)) | (1L << (KW_NE - 69)) | (1L << (KW_NEXT - 69)) | (1L << (KW_NAMESPACE_NODE - 69)) | (1L << (KW_NO_INHERIT - 69)) | (1L << (KW_NO_PRESERVE - 69)) | (1L << (KW_NODE - 69)) | (1L << (KW_OF - 69)) | (1L << (KW_ONLY - 69)) | (1L << (KW_OPTION - 69)) | (1L << (KW_OR - 69)) | (1L << (KW_ORDER - 69)))) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (KW_ORDERED - 133)) | (1L << (KW_ORDERING - 133)) | (1L << (KW_PARENT - 133)) | (1L << (KW_PRECEDING - 133)) | (1L << (KW_PRECEDING_SIBLING - 133)) | (1L << (KW_PRESERVE - 133)) | (1L << (KW_PI - 133)) | (1L << (KW_RETURN - 133)) | (1L << (KW_SATISFIES - 133)) | (1L << (KW_SCHEMA - 133)) | (1L << (KW_SCHEMA_ATTR - 133)) | (1L << (KW_SCHEMA_ELEM - 133)) | (1L << (KW_SELF - 133)) | (1L << (KW_SLIDING - 133)) | (1L << (KW_SOME - 133)) | (1L << (KW_STABLE - 133)) | (1L << (KW_START - 133)) | (1L << (KW_STRICT - 133)) | (1L << (KW_STRIP - 133)) | (1L << (KW_SWITCH - 133)) | (1L << (KW_TEXT - 133)) | (1L << (KW_THEN - 133)) | (1L << (KW_TO - 133)) | (1L << (KW_TREAT - 133)) | (1L << (KW_TRY - 133)) | (1L << (KW_TUMBLING - 133)) | (1L << (KW_TYPE - 133)) | (1L << (KW_TYPESWITCH - 133)) | (1L << (KW_UNION - 133)) | (1L << (KW_UNORDERED - 133)) | (1L << (KW_UPDATE - 133)) | (1L << (KW_VALIDATE - 133)) | (1L << (KW_VARIABLE - 133)) | (1L << (KW_VERSION - 133)) | (1L << (KW_WHEN - 133)) | (1L << (KW_WHERE - 133)) | (1L << (KW_WINDOW - 133)) | (1L << (KW_XQUERY - 133)) | (1L << (KW_ARRAY_NODE - 133)) | (1L << (KW_BOOLEAN_NODE - 133)) | (1L << (KW_NULL_NODE - 133)) | (1L << (KW_NUMBER_NODE - 133)) | (1L << (KW_OBJECT_NODE - 133)) | (1L << (KW_REPLACE - 133)) | (1L << (KW_WITH - 133)) | (1L << (KW_VALUE - 133)) | (1L << (KW_INSERT - 133)) | (1L << (KW_INTO - 133)) | (1L << (KW_DELETE - 133)) | (1L << (KW_RENAME - 133)) | (1L << (URIQualifiedName - 133)) | (1L << (FullQName - 133)) | (1L << (NCNameWithLocalWildcard - 133)) | (1L << (NCNameWithPrefixWildcard - 133)) | (1L << (NCName - 133)) | (1L << (ENTER_STRING - 133)))) != 0)) {
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
				if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & ((1L << (IntegerLiteral - 5)) | (1L << (DecimalLiteral - 5)) | (1L << (DoubleLiteral - 5)) | (1L << (DFPropertyName - 5)) | (1L << (Quot - 5)) | (1L << (Apos - 5)) | (1L << (COMMENT - 5)) | (1L << (PI - 5)) | (1L << (PRAGMA - 5)) | (1L << (LPAREN - 5)) | (1L << (LBRACKET - 5)) | (1L << (STAR - 5)) | (1L << (PLUS - 5)) | (1L << (MINUS - 5)) | (1L << (DOT - 5)) | (1L << (DDOT - 5)) | (1L << (SLASH - 5)) | (1L << (DSLASH - 5)) | (1L << (LANGLE - 5)) | (1L << (QUESTION - 5)) | (1L << (AT - 5)) | (1L << (DOLLAR - 5)) | (1L << (MOD - 5)) | (1L << (KW_ALLOWING - 5)) | (1L << (KW_ANCESTOR - 5)) | (1L << (KW_ANCESTOR_OR_SELF - 5)) | (1L << (KW_AND - 5)) | (1L << (KW_ARRAY - 5)) | (1L << (KW_AS - 5)) | (1L << (KW_ASCENDING - 5)) | (1L << (KW_AT - 5)) | (1L << (KW_ATTRIBUTE - 5)) | (1L << (KW_BASE_URI - 5)) | (1L << (KW_BOUNDARY_SPACE - 5)) | (1L << (KW_BINARY - 5)) | (1L << (KW_BY - 5)) | (1L << (KW_CASE - 5)) | (1L << (KW_CAST - 5)) | (1L << (KW_CASTABLE - 5)))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (KW_CATCH - 69)) | (1L << (KW_CHILD - 69)) | (1L << (KW_COLLATION - 69)) | (1L << (KW_COMMENT - 69)) | (1L << (KW_CONSTRUCTION - 69)) | (1L << (KW_CONTEXT - 69)) | (1L << (KW_COPY_NS - 69)) | (1L << (KW_COUNT - 69)) | (1L << (KW_DECLARE - 69)) | (1L << (KW_DEFAULT - 69)) | (1L << (KW_DESCENDANT - 69)) | (1L << (KW_DESCENDANT_OR_SELF - 69)) | (1L << (KW_DESCENDING - 69)) | (1L << (KW_DECIMAL_FORMAT - 69)) | (1L << (KW_DIV - 69)) | (1L << (KW_DOCUMENT - 69)) | (1L << (KW_DOCUMENT_NODE - 69)) | (1L << (KW_ELEMENT - 69)) | (1L << (KW_ELSE - 69)) | (1L << (KW_EMPTY - 69)) | (1L << (KW_EMPTY_SEQUENCE - 69)) | (1L << (KW_ENCODING - 69)) | (1L << (KW_END - 69)) | (1L << (KW_EQ - 69)) | (1L << (KW_EVERY - 69)) | (1L << (KW_EXCEPT - 69)) | (1L << (KW_EXTERNAL - 69)) | (1L << (KW_FOLLOWING - 69)) | (1L << (KW_FOLLOWING_SIBLING - 69)) | (1L << (KW_FOR - 69)) | (1L << (KW_FUNCTION - 69)) | (1L << (KW_GE - 69)) | (1L << (KW_GREATEST - 69)) | (1L << (KW_GROUP - 69)) | (1L << (KW_GT - 69)) | (1L << (KW_IDIV - 69)) | (1L << (KW_IF - 69)) | (1L << (KW_IMPORT - 69)) | (1L << (KW_IN - 69)) | (1L << (KW_INHERIT - 69)) | (1L << (KW_INSTANCE - 69)) | (1L << (KW_INTERSECT - 69)) | (1L << (KW_IS - 69)) | (1L << (KW_ITEM - 69)) | (1L << (KW_LAX - 69)) | (1L << (KW_LE - 69)) | (1L << (KW_LEAST - 69)) | (1L << (KW_LET - 69)) | (1L << (KW_LT - 69)) | (1L << (KW_MAP - 69)) | (1L << (KW_MOD - 69)) | (1L << (KW_MODULE - 69)) | (1L << (KW_NAMESPACE - 69)) | (1L << (KW_NE - 69)) | (1L << (KW_NEXT - 69)) | (1L << (KW_NAMESPACE_NODE - 69)) | (1L << (KW_NO_INHERIT - 69)) | (1L << (KW_NO_PRESERVE - 69)) | (1L << (KW_NODE - 69)) | (1L << (KW_OF - 69)) | (1L << (KW_ONLY - 69)) | (1L << (KW_OPTION - 69)) | (1L << (KW_OR - 69)) | (1L << (KW_ORDER - 69)))) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (KW_ORDERED - 133)) | (1L << (KW_ORDERING - 133)) | (1L << (KW_PARENT - 133)) | (1L << (KW_PRECEDING - 133)) | (1L << (KW_PRECEDING_SIBLING - 133)) | (1L << (KW_PRESERVE - 133)) | (1L << (KW_PI - 133)) | (1L << (KW_RETURN - 133)) | (1L << (KW_SATISFIES - 133)) | (1L << (KW_SCHEMA - 133)) | (1L << (KW_SCHEMA_ATTR - 133)) | (1L << (KW_SCHEMA_ELEM - 133)) | (1L << (KW_SELF - 133)) | (1L << (KW_SLIDING - 133)) | (1L << (KW_SOME - 133)) | (1L << (KW_STABLE - 133)) | (1L << (KW_START - 133)) | (1L << (KW_STRICT - 133)) | (1L << (KW_STRIP - 133)) | (1L << (KW_SWITCH - 133)) | (1L << (KW_TEXT - 133)) | (1L << (KW_THEN - 133)) | (1L << (KW_TO - 133)) | (1L << (KW_TREAT - 133)) | (1L << (KW_TRY - 133)) | (1L << (KW_TUMBLING - 133)) | (1L << (KW_TYPE - 133)) | (1L << (KW_TYPESWITCH - 133)) | (1L << (KW_UNION - 133)) | (1L << (KW_UNORDERED - 133)) | (1L << (KW_UPDATE - 133)) | (1L << (KW_VALIDATE - 133)) | (1L << (KW_VARIABLE - 133)) | (1L << (KW_VERSION - 133)) | (1L << (KW_WHEN - 133)) | (1L << (KW_WHERE - 133)) | (1L << (KW_WINDOW - 133)) | (1L << (KW_XQUERY - 133)) | (1L << (KW_ARRAY_NODE - 133)) | (1L << (KW_BOOLEAN_NODE - 133)) | (1L << (KW_NULL_NODE - 133)) | (1L << (KW_NUMBER_NODE - 133)) | (1L << (KW_OBJECT_NODE - 133)) | (1L << (KW_REPLACE - 133)) | (1L << (KW_WITH - 133)) | (1L << (KW_VALUE - 133)) | (1L << (KW_INSERT - 133)) | (1L << (KW_INTO - 133)) | (1L << (KW_DELETE - 133)) | (1L << (KW_RENAME - 133)) | (1L << (URIQualifiedName - 133)) | (1L << (FullQName - 133)) | (1L << (NCNameWithLocalWildcard - 133)) | (1L << (NCNameWithPrefixWildcard - 133)) | (1L << (NCName - 133)) | (1L << (ENTER_STRING - 133)))) != 0)) {
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
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & ((1L << (IntegerLiteral - 5)) | (1L << (DecimalLiteral - 5)) | (1L << (DoubleLiteral - 5)) | (1L << (DFPropertyName - 5)) | (1L << (Quot - 5)) | (1L << (Apos - 5)) | (1L << (COMMENT - 5)) | (1L << (PI - 5)) | (1L << (PRAGMA - 5)) | (1L << (LPAREN - 5)) | (1L << (LBRACKET - 5)) | (1L << (STAR - 5)) | (1L << (PLUS - 5)) | (1L << (MINUS - 5)) | (1L << (DOT - 5)) | (1L << (DDOT - 5)) | (1L << (SLASH - 5)) | (1L << (DSLASH - 5)) | (1L << (LANGLE - 5)) | (1L << (QUESTION - 5)) | (1L << (AT - 5)) | (1L << (DOLLAR - 5)) | (1L << (MOD - 5)) | (1L << (KW_ALLOWING - 5)) | (1L << (KW_ANCESTOR - 5)) | (1L << (KW_ANCESTOR_OR_SELF - 5)) | (1L << (KW_AND - 5)) | (1L << (KW_ARRAY - 5)) | (1L << (KW_AS - 5)) | (1L << (KW_ASCENDING - 5)) | (1L << (KW_AT - 5)) | (1L << (KW_ATTRIBUTE - 5)) | (1L << (KW_BASE_URI - 5)) | (1L << (KW_BOUNDARY_SPACE - 5)) | (1L << (KW_BINARY - 5)) | (1L << (KW_BY - 5)) | (1L << (KW_CASE - 5)) | (1L << (KW_CAST - 5)) | (1L << (KW_CASTABLE - 5)))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (KW_CATCH - 69)) | (1L << (KW_CHILD - 69)) | (1L << (KW_COLLATION - 69)) | (1L << (KW_COMMENT - 69)) | (1L << (KW_CONSTRUCTION - 69)) | (1L << (KW_CONTEXT - 69)) | (1L << (KW_COPY_NS - 69)) | (1L << (KW_COUNT - 69)) | (1L << (KW_DECLARE - 69)) | (1L << (KW_DEFAULT - 69)) | (1L << (KW_DESCENDANT - 69)) | (1L << (KW_DESCENDANT_OR_SELF - 69)) | (1L << (KW_DESCENDING - 69)) | (1L << (KW_DECIMAL_FORMAT - 69)) | (1L << (KW_DIV - 69)) | (1L << (KW_DOCUMENT - 69)) | (1L << (KW_DOCUMENT_NODE - 69)) | (1L << (KW_ELEMENT - 69)) | (1L << (KW_ELSE - 69)) | (1L << (KW_EMPTY - 69)) | (1L << (KW_EMPTY_SEQUENCE - 69)) | (1L << (KW_ENCODING - 69)) | (1L << (KW_END - 69)) | (1L << (KW_EQ - 69)) | (1L << (KW_EVERY - 69)) | (1L << (KW_EXCEPT - 69)) | (1L << (KW_EXTERNAL - 69)) | (1L << (KW_FOLLOWING - 69)) | (1L << (KW_FOLLOWING_SIBLING - 69)) | (1L << (KW_FOR - 69)) | (1L << (KW_FUNCTION - 69)) | (1L << (KW_GE - 69)) | (1L << (KW_GREATEST - 69)) | (1L << (KW_GROUP - 69)) | (1L << (KW_GT - 69)) | (1L << (KW_IDIV - 69)) | (1L << (KW_IF - 69)) | (1L << (KW_IMPORT - 69)) | (1L << (KW_IN - 69)) | (1L << (KW_INHERIT - 69)) | (1L << (KW_INSTANCE - 69)) | (1L << (KW_INTERSECT - 69)) | (1L << (KW_IS - 69)) | (1L << (KW_ITEM - 69)) | (1L << (KW_LAX - 69)) | (1L << (KW_LE - 69)) | (1L << (KW_LEAST - 69)) | (1L << (KW_LET - 69)) | (1L << (KW_LT - 69)) | (1L << (KW_MAP - 69)) | (1L << (KW_MOD - 69)) | (1L << (KW_MODULE - 69)) | (1L << (KW_NAMESPACE - 69)) | (1L << (KW_NE - 69)) | (1L << (KW_NEXT - 69)) | (1L << (KW_NAMESPACE_NODE - 69)) | (1L << (KW_NO_INHERIT - 69)) | (1L << (KW_NO_PRESERVE - 69)) | (1L << (KW_NODE - 69)) | (1L << (KW_OF - 69)) | (1L << (KW_ONLY - 69)) | (1L << (KW_OPTION - 69)) | (1L << (KW_OR - 69)) | (1L << (KW_ORDER - 69)))) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (KW_ORDERED - 133)) | (1L << (KW_ORDERING - 133)) | (1L << (KW_PARENT - 133)) | (1L << (KW_PRECEDING - 133)) | (1L << (KW_PRECEDING_SIBLING - 133)) | (1L << (KW_PRESERVE - 133)) | (1L << (KW_PI - 133)) | (1L << (KW_RETURN - 133)) | (1L << (KW_SATISFIES - 133)) | (1L << (KW_SCHEMA - 133)) | (1L << (KW_SCHEMA_ATTR - 133)) | (1L << (KW_SCHEMA_ELEM - 133)) | (1L << (KW_SELF - 133)) | (1L << (KW_SLIDING - 133)) | (1L << (KW_SOME - 133)) | (1L << (KW_STABLE - 133)) | (1L << (KW_START - 133)) | (1L << (KW_STRICT - 133)) | (1L << (KW_STRIP - 133)) | (1L << (KW_SWITCH - 133)) | (1L << (KW_TEXT - 133)) | (1L << (KW_THEN - 133)) | (1L << (KW_TO - 133)) | (1L << (KW_TREAT - 133)) | (1L << (KW_TRY - 133)) | (1L << (KW_TUMBLING - 133)) | (1L << (KW_TYPE - 133)) | (1L << (KW_TYPESWITCH - 133)) | (1L << (KW_UNION - 133)) | (1L << (KW_UNORDERED - 133)) | (1L << (KW_UPDATE - 133)) | (1L << (KW_VALIDATE - 133)) | (1L << (KW_VARIABLE - 133)) | (1L << (KW_VERSION - 133)) | (1L << (KW_WHEN - 133)) | (1L << (KW_WHERE - 133)) | (1L << (KW_WINDOW - 133)) | (1L << (KW_XQUERY - 133)) | (1L << (KW_ARRAY_NODE - 133)) | (1L << (KW_BOOLEAN_NODE - 133)) | (1L << (KW_NULL_NODE - 133)) | (1L << (KW_NUMBER_NODE - 133)) | (1L << (KW_OBJECT_NODE - 133)) | (1L << (KW_REPLACE - 133)) | (1L << (KW_WITH - 133)) | (1L << (KW_VALUE - 133)) | (1L << (KW_INSERT - 133)) | (1L << (KW_INTO - 133)) | (1L << (KW_DELETE - 133)) | (1L << (KW_RENAME - 133)) | (1L << (URIQualifiedName - 133)) | (1L << (FullQName - 133)) | (1L << (NCNameWithLocalWildcard - 133)) | (1L << (NCNameWithPrefixWildcard - 133)) | (1L << (NCName - 133)) | (1L << (ENTER_STRING - 133)))) != 0)) {
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
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & ((1L << (IntegerLiteral - 5)) | (1L << (DecimalLiteral - 5)) | (1L << (DoubleLiteral - 5)) | (1L << (DFPropertyName - 5)) | (1L << (Quot - 5)) | (1L << (Apos - 5)) | (1L << (COMMENT - 5)) | (1L << (PI - 5)) | (1L << (PRAGMA - 5)) | (1L << (LPAREN - 5)) | (1L << (LBRACKET - 5)) | (1L << (STAR - 5)) | (1L << (PLUS - 5)) | (1L << (MINUS - 5)) | (1L << (DOT - 5)) | (1L << (DDOT - 5)) | (1L << (SLASH - 5)) | (1L << (DSLASH - 5)) | (1L << (LANGLE - 5)) | (1L << (QUESTION - 5)) | (1L << (AT - 5)) | (1L << (DOLLAR - 5)) | (1L << (MOD - 5)) | (1L << (KW_ALLOWING - 5)) | (1L << (KW_ANCESTOR - 5)) | (1L << (KW_ANCESTOR_OR_SELF - 5)) | (1L << (KW_AND - 5)) | (1L << (KW_ARRAY - 5)) | (1L << (KW_AS - 5)) | (1L << (KW_ASCENDING - 5)) | (1L << (KW_AT - 5)) | (1L << (KW_ATTRIBUTE - 5)) | (1L << (KW_BASE_URI - 5)) | (1L << (KW_BOUNDARY_SPACE - 5)) | (1L << (KW_BINARY - 5)) | (1L << (KW_BY - 5)) | (1L << (KW_CASE - 5)) | (1L << (KW_CAST - 5)) | (1L << (KW_CASTABLE - 5)))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (KW_CATCH - 69)) | (1L << (KW_CHILD - 69)) | (1L << (KW_COLLATION - 69)) | (1L << (KW_COMMENT - 69)) | (1L << (KW_CONSTRUCTION - 69)) | (1L << (KW_CONTEXT - 69)) | (1L << (KW_COPY_NS - 69)) | (1L << (KW_COUNT - 69)) | (1L << (KW_DECLARE - 69)) | (1L << (KW_DEFAULT - 69)) | (1L << (KW_DESCENDANT - 69)) | (1L << (KW_DESCENDANT_OR_SELF - 69)) | (1L << (KW_DESCENDING - 69)) | (1L << (KW_DECIMAL_FORMAT - 69)) | (1L << (KW_DIV - 69)) | (1L << (KW_DOCUMENT - 69)) | (1L << (KW_DOCUMENT_NODE - 69)) | (1L << (KW_ELEMENT - 69)) | (1L << (KW_ELSE - 69)) | (1L << (KW_EMPTY - 69)) | (1L << (KW_EMPTY_SEQUENCE - 69)) | (1L << (KW_ENCODING - 69)) | (1L << (KW_END - 69)) | (1L << (KW_EQ - 69)) | (1L << (KW_EVERY - 69)) | (1L << (KW_EXCEPT - 69)) | (1L << (KW_EXTERNAL - 69)) | (1L << (KW_FOLLOWING - 69)) | (1L << (KW_FOLLOWING_SIBLING - 69)) | (1L << (KW_FOR - 69)) | (1L << (KW_FUNCTION - 69)) | (1L << (KW_GE - 69)) | (1L << (KW_GREATEST - 69)) | (1L << (KW_GROUP - 69)) | (1L << (KW_GT - 69)) | (1L << (KW_IDIV - 69)) | (1L << (KW_IF - 69)) | (1L << (KW_IMPORT - 69)) | (1L << (KW_IN - 69)) | (1L << (KW_INHERIT - 69)) | (1L << (KW_INSTANCE - 69)) | (1L << (KW_INTERSECT - 69)) | (1L << (KW_IS - 69)) | (1L << (KW_ITEM - 69)) | (1L << (KW_LAX - 69)) | (1L << (KW_LE - 69)) | (1L << (KW_LEAST - 69)) | (1L << (KW_LET - 69)) | (1L << (KW_LT - 69)) | (1L << (KW_MAP - 69)) | (1L << (KW_MOD - 69)) | (1L << (KW_MODULE - 69)) | (1L << (KW_NAMESPACE - 69)) | (1L << (KW_NE - 69)) | (1L << (KW_NEXT - 69)) | (1L << (KW_NAMESPACE_NODE - 69)) | (1L << (KW_NO_INHERIT - 69)) | (1L << (KW_NO_PRESERVE - 69)) | (1L << (KW_NODE - 69)) | (1L << (KW_OF - 69)) | (1L << (KW_ONLY - 69)) | (1L << (KW_OPTION - 69)) | (1L << (KW_OR - 69)) | (1L << (KW_ORDER - 69)))) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (KW_ORDERED - 133)) | (1L << (KW_ORDERING - 133)) | (1L << (KW_PARENT - 133)) | (1L << (KW_PRECEDING - 133)) | (1L << (KW_PRECEDING_SIBLING - 133)) | (1L << (KW_PRESERVE - 133)) | (1L << (KW_PI - 133)) | (1L << (KW_RETURN - 133)) | (1L << (KW_SATISFIES - 133)) | (1L << (KW_SCHEMA - 133)) | (1L << (KW_SCHEMA_ATTR - 133)) | (1L << (KW_SCHEMA_ELEM - 133)) | (1L << (KW_SELF - 133)) | (1L << (KW_SLIDING - 133)) | (1L << (KW_SOME - 133)) | (1L << (KW_STABLE - 133)) | (1L << (KW_START - 133)) | (1L << (KW_STRICT - 133)) | (1L << (KW_STRIP - 133)) | (1L << (KW_SWITCH - 133)) | (1L << (KW_TEXT - 133)) | (1L << (KW_THEN - 133)) | (1L << (KW_TO - 133)) | (1L << (KW_TREAT - 133)) | (1L << (KW_TRY - 133)) | (1L << (KW_TUMBLING - 133)) | (1L << (KW_TYPE - 133)) | (1L << (KW_TYPESWITCH - 133)) | (1L << (KW_UNION - 133)) | (1L << (KW_UNORDERED - 133)) | (1L << (KW_UPDATE - 133)) | (1L << (KW_VALIDATE - 133)) | (1L << (KW_VARIABLE - 133)) | (1L << (KW_VERSION - 133)) | (1L << (KW_WHEN - 133)) | (1L << (KW_WHERE - 133)) | (1L << (KW_WINDOW - 133)) | (1L << (KW_XQUERY - 133)) | (1L << (KW_ARRAY_NODE - 133)) | (1L << (KW_BOOLEAN_NODE - 133)) | (1L << (KW_NULL_NODE - 133)) | (1L << (KW_NUMBER_NODE - 133)) | (1L << (KW_OBJECT_NODE - 133)) | (1L << (KW_REPLACE - 133)) | (1L << (KW_WITH - 133)) | (1L << (KW_VALUE - 133)) | (1L << (KW_INSERT - 133)) | (1L << (KW_INTO - 133)) | (1L << (KW_DELETE - 133)) | (1L << (KW_RENAME - 133)) | (1L << (URIQualifiedName - 133)) | (1L << (FullQName - 133)) | (1L << (NCNameWithLocalWildcard - 133)) | (1L << (NCNameWithPrefixWildcard - 133)) | (1L << (NCName - 133)) | (1L << (ENTER_STRING - 133)))) != 0)) {
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
			if (((((_la - 5)) & ~0x3f) == 0 && ((1L << (_la - 5)) & ((1L << (IntegerLiteral - 5)) | (1L << (DecimalLiteral - 5)) | (1L << (DoubleLiteral - 5)) | (1L << (DFPropertyName - 5)) | (1L << (Quot - 5)) | (1L << (Apos - 5)) | (1L << (COMMENT - 5)) | (1L << (PI - 5)) | (1L << (PRAGMA - 5)) | (1L << (LPAREN - 5)) | (1L << (LBRACKET - 5)) | (1L << (STAR - 5)) | (1L << (PLUS - 5)) | (1L << (MINUS - 5)) | (1L << (DOT - 5)) | (1L << (DDOT - 5)) | (1L << (SLASH - 5)) | (1L << (DSLASH - 5)) | (1L << (LANGLE - 5)) | (1L << (QUESTION - 5)) | (1L << (AT - 5)) | (1L << (DOLLAR - 5)) | (1L << (MOD - 5)) | (1L << (KW_ALLOWING - 5)) | (1L << (KW_ANCESTOR - 5)) | (1L << (KW_ANCESTOR_OR_SELF - 5)) | (1L << (KW_AND - 5)) | (1L << (KW_ARRAY - 5)) | (1L << (KW_AS - 5)) | (1L << (KW_ASCENDING - 5)) | (1L << (KW_AT - 5)) | (1L << (KW_ATTRIBUTE - 5)) | (1L << (KW_BASE_URI - 5)) | (1L << (KW_BOUNDARY_SPACE - 5)) | (1L << (KW_BINARY - 5)) | (1L << (KW_BY - 5)) | (1L << (KW_CASE - 5)) | (1L << (KW_CAST - 5)) | (1L << (KW_CASTABLE - 5)))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (KW_CATCH - 69)) | (1L << (KW_CHILD - 69)) | (1L << (KW_COLLATION - 69)) | (1L << (KW_COMMENT - 69)) | (1L << (KW_CONSTRUCTION - 69)) | (1L << (KW_CONTEXT - 69)) | (1L << (KW_COPY_NS - 69)) | (1L << (KW_COUNT - 69)) | (1L << (KW_DECLARE - 69)) | (1L << (KW_DEFAULT - 69)) | (1L << (KW_DESCENDANT - 69)) | (1L << (KW_DESCENDANT_OR_SELF - 69)) | (1L << (KW_DESCENDING - 69)) | (1L << (KW_DECIMAL_FORMAT - 69)) | (1L << (KW_DIV - 69)) | (1L << (KW_DOCUMENT - 69)) | (1L << (KW_DOCUMENT_NODE - 69)) | (1L << (KW_ELEMENT - 69)) | (1L << (KW_ELSE - 69)) | (1L << (KW_EMPTY - 69)) | (1L << (KW_EMPTY_SEQUENCE - 69)) | (1L << (KW_ENCODING - 69)) | (1L << (KW_END - 69)) | (1L << (KW_EQ - 69)) | (1L << (KW_EVERY - 69)) | (1L << (KW_EXCEPT - 69)) | (1L << (KW_EXTERNAL - 69)) | (1L << (KW_FOLLOWING - 69)) | (1L << (KW_FOLLOWING_SIBLING - 69)) | (1L << (KW_FOR - 69)) | (1L << (KW_FUNCTION - 69)) | (1L << (KW_GE - 69)) | (1L << (KW_GREATEST - 69)) | (1L << (KW_GROUP - 69)) | (1L << (KW_GT - 69)) | (1L << (KW_IDIV - 69)) | (1L << (KW_IF - 69)) | (1L << (KW_IMPORT - 69)) | (1L << (KW_IN - 69)) | (1L << (KW_INHERIT - 69)) | (1L << (KW_INSTANCE - 69)) | (1L << (KW_INTERSECT - 69)) | (1L << (KW_IS - 69)) | (1L << (KW_ITEM - 69)) | (1L << (KW_LAX - 69)) | (1L << (KW_LE - 69)) | (1L << (KW_LEAST - 69)) | (1L << (KW_LET - 69)) | (1L << (KW_LT - 69)) | (1L << (KW_MAP - 69)) | (1L << (KW_MOD - 69)) | (1L << (KW_MODULE - 69)) | (1L << (KW_NAMESPACE - 69)) | (1L << (KW_NE - 69)) | (1L << (KW_NEXT - 69)) | (1L << (KW_NAMESPACE_NODE - 69)) | (1L << (KW_NO_INHERIT - 69)) | (1L << (KW_NO_PRESERVE - 69)) | (1L << (KW_NODE - 69)) | (1L << (KW_OF - 69)) | (1L << (KW_ONLY - 69)) | (1L << (KW_OPTION - 69)) | (1L << (KW_OR - 69)) | (1L << (KW_ORDER - 69)))) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (KW_ORDERED - 133)) | (1L << (KW_ORDERING - 133)) | (1L << (KW_PARENT - 133)) | (1L << (KW_PRECEDING - 133)) | (1L << (KW_PRECEDING_SIBLING - 133)) | (1L << (KW_PRESERVE - 133)) | (1L << (KW_PI - 133)) | (1L << (KW_RETURN - 133)) | (1L << (KW_SATISFIES - 133)) | (1L << (KW_SCHEMA - 133)) | (1L << (KW_SCHEMA_ATTR - 133)) | (1L << (KW_SCHEMA_ELEM - 133)) | (1L << (KW_SELF - 133)) | (1L << (KW_SLIDING - 133)) | (1L << (KW_SOME - 133)) | (1L << (KW_STABLE - 133)) | (1L << (KW_START - 133)) | (1L << (KW_STRICT - 133)) | (1L << (KW_STRIP - 133)) | (1L << (KW_SWITCH - 133)) | (1L << (KW_TEXT - 133)) | (1L << (KW_THEN - 133)) | (1L << (KW_TO - 133)) | (1L << (KW_TREAT - 133)) | (1L << (KW_TRY - 133)) | (1L << (KW_TUMBLING - 133)) | (1L << (KW_TYPE - 133)) | (1L << (KW_TYPESWITCH - 133)) | (1L << (KW_UNION - 133)) | (1L << (KW_UNORDERED - 133)) | (1L << (KW_UPDATE - 133)) | (1L << (KW_VALIDATE - 133)) | (1L << (KW_VARIABLE - 133)) | (1L << (KW_VERSION - 133)) | (1L << (KW_WHEN - 133)) | (1L << (KW_WHERE - 133)) | (1L << (KW_WINDOW - 133)) | (1L << (KW_XQUERY - 133)) | (1L << (KW_ARRAY_NODE - 133)) | (1L << (KW_BOOLEAN_NODE - 133)) | (1L << (KW_NULL_NODE - 133)) | (1L << (KW_NUMBER_NODE - 133)) | (1L << (KW_OBJECT_NODE - 133)) | (1L << (KW_REPLACE - 133)) | (1L << (KW_WITH - 133)) | (1L << (KW_VALUE - 133)) | (1L << (KW_INSERT - 133)) | (1L << (KW_INTO - 133)) | (1L << (KW_DELETE - 133)) | (1L << (KW_RENAME - 133)) | (1L << (URIQualifiedName - 133)) | (1L << (FullQName - 133)) | (1L << (NCNameWithLocalWildcard - 133)) | (1L << (NCNameWithPrefixWildcard - 133)) | (1L << (NCName - 133)) | (1L << (ENTER_STRING - 133)))) != 0)) {
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << RBRACKET) | (1L << LBRACE) | (1L << GRAVE))) != 0) || _la==BASIC_CHAR) {
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
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DFPropertyName) | (1L << STAR) | (1L << KW_ALLOWING) | (1L << KW_ANCESTOR) | (1L << KW_ANCESTOR_OR_SELF) | (1L << KW_AND) | (1L << KW_ARRAY) | (1L << KW_AS) | (1L << KW_ASCENDING) | (1L << KW_AT) | (1L << KW_ATTRIBUTE) | (1L << KW_BASE_URI) | (1L << KW_BOUNDARY_SPACE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (KW_BINARY - 64)) | (1L << (KW_BY - 64)) | (1L << (KW_CASE - 64)) | (1L << (KW_CAST - 64)) | (1L << (KW_CASTABLE - 64)) | (1L << (KW_CATCH - 64)) | (1L << (KW_CHILD - 64)) | (1L << (KW_COLLATION - 64)) | (1L << (KW_COMMENT - 64)) | (1L << (KW_CONSTRUCTION - 64)) | (1L << (KW_CONTEXT - 64)) | (1L << (KW_COPY_NS - 64)) | (1L << (KW_COUNT - 64)) | (1L << (KW_DECLARE - 64)) | (1L << (KW_DEFAULT - 64)) | (1L << (KW_DESCENDANT - 64)) | (1L << (KW_DESCENDANT_OR_SELF - 64)) | (1L << (KW_DESCENDING - 64)) | (1L << (KW_DECIMAL_FORMAT - 64)) | (1L << (KW_DIV - 64)) | (1L << (KW_DOCUMENT - 64)) | (1L << (KW_DOCUMENT_NODE - 64)) | (1L << (KW_ELEMENT - 64)) | (1L << (KW_ELSE - 64)) | (1L << (KW_EMPTY - 64)) | (1L << (KW_EMPTY_SEQUENCE - 64)) | (1L << (KW_ENCODING - 64)) | (1L << (KW_END - 64)) | (1L << (KW_EQ - 64)) | (1L << (KW_EVERY - 64)) | (1L << (KW_EXCEPT - 64)) | (1L << (KW_EXTERNAL - 64)) | (1L << (KW_FOLLOWING - 64)) | (1L << (KW_FOLLOWING_SIBLING - 64)) | (1L << (KW_FOR - 64)) | (1L << (KW_FUNCTION - 64)) | (1L << (KW_GE - 64)) | (1L << (KW_GREATEST - 64)) | (1L << (KW_GROUP - 64)) | (1L << (KW_GT - 64)) | (1L << (KW_IDIV - 64)) | (1L << (KW_IF - 64)) | (1L << (KW_IMPORT - 64)) | (1L << (KW_IN - 64)) | (1L << (KW_INHERIT - 64)) | (1L << (KW_INSTANCE - 64)) | (1L << (KW_INTERSECT - 64)) | (1L << (KW_IS - 64)) | (1L << (KW_ITEM - 64)) | (1L << (KW_LAX - 64)) | (1L << (KW_LE - 64)) | (1L << (KW_LEAST - 64)) | (1L << (KW_LET - 64)) | (1L << (KW_LT - 64)) | (1L << (KW_MAP - 64)) | (1L << (KW_MOD - 64)) | (1L << (KW_MODULE - 64)) | (1L << (KW_NAMESPACE - 64)) | (1L << (KW_NE - 64)) | (1L << (KW_NEXT - 64)) | (1L << (KW_NAMESPACE_NODE - 64)) | (1L << (KW_NO_INHERIT - 64)) | (1L << (KW_NO_PRESERVE - 64)) | (1L << (KW_NODE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (KW_OF - 128)) | (1L << (KW_ONLY - 128)) | (1L << (KW_OPTION - 128)) | (1L << (KW_OR - 128)) | (1L << (KW_ORDER - 128)) | (1L << (KW_ORDERED - 128)) | (1L << (KW_ORDERING - 128)) | (1L << (KW_PARENT - 128)) | (1L << (KW_PRECEDING - 128)) | (1L << (KW_PRECEDING_SIBLING - 128)) | (1L << (KW_PRESERVE - 128)) | (1L << (KW_PI - 128)) | (1L << (KW_RETURN - 128)) | (1L << (KW_SATISFIES - 128)) | (1L << (KW_SCHEMA - 128)) | (1L << (KW_SCHEMA_ATTR - 128)) | (1L << (KW_SCHEMA_ELEM - 128)) | (1L << (KW_SELF - 128)) | (1L << (KW_SLIDING - 128)) | (1L << (KW_SOME - 128)) | (1L << (KW_STABLE - 128)) | (1L << (KW_START - 128)) | (1L << (KW_STRICT - 128)) | (1L << (KW_STRIP - 128)) | (1L << (KW_SWITCH - 128)) | (1L << (KW_TEXT - 128)) | (1L << (KW_THEN - 128)) | (1L << (KW_TO - 128)) | (1L << (KW_TREAT - 128)) | (1L << (KW_TRY - 128)) | (1L << (KW_TUMBLING - 128)) | (1L << (KW_TYPE - 128)) | (1L << (KW_TYPESWITCH - 128)) | (1L << (KW_UNION - 128)) | (1L << (KW_UNORDERED - 128)) | (1L << (KW_UPDATE - 128)) | (1L << (KW_VALIDATE - 128)) | (1L << (KW_VARIABLE - 128)) | (1L << (KW_VERSION - 128)) | (1L << (KW_WHEN - 128)) | (1L << (KW_WHERE - 128)) | (1L << (KW_WINDOW - 128)) | (1L << (KW_XQUERY - 128)) | (1L << (KW_ARRAY_NODE - 128)) | (1L << (KW_BOOLEAN_NODE - 128)) | (1L << (KW_NULL_NODE - 128)) | (1L << (KW_NUMBER_NODE - 128)) | (1L << (KW_OBJECT_NODE - 128)) | (1L << (KW_REPLACE - 128)) | (1L << (KW_WITH - 128)) | (1L << (KW_VALUE - 128)) | (1L << (KW_INSERT - 128)) | (1L << (KW_INTO - 128)) | (1L << (KW_DELETE - 128)) | (1L << (KW_RENAME - 128)) | (1L << (URIQualifiedName - 128)) | (1L << (FullQName - 128)) | (1L << (NCName - 128)))) != 0)) {
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
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DFPropertyName) | (1L << STAR) | (1L << KW_ALLOWING) | (1L << KW_ANCESTOR) | (1L << KW_ANCESTOR_OR_SELF) | (1L << KW_AND) | (1L << KW_ARRAY) | (1L << KW_AS) | (1L << KW_ASCENDING) | (1L << KW_AT) | (1L << KW_ATTRIBUTE) | (1L << KW_BASE_URI) | (1L << KW_BOUNDARY_SPACE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (KW_BINARY - 64)) | (1L << (KW_BY - 64)) | (1L << (KW_CASE - 64)) | (1L << (KW_CAST - 64)) | (1L << (KW_CASTABLE - 64)) | (1L << (KW_CATCH - 64)) | (1L << (KW_CHILD - 64)) | (1L << (KW_COLLATION - 64)) | (1L << (KW_COMMENT - 64)) | (1L << (KW_CONSTRUCTION - 64)) | (1L << (KW_CONTEXT - 64)) | (1L << (KW_COPY_NS - 64)) | (1L << (KW_COUNT - 64)) | (1L << (KW_DECLARE - 64)) | (1L << (KW_DEFAULT - 64)) | (1L << (KW_DESCENDANT - 64)) | (1L << (KW_DESCENDANT_OR_SELF - 64)) | (1L << (KW_DESCENDING - 64)) | (1L << (KW_DECIMAL_FORMAT - 64)) | (1L << (KW_DIV - 64)) | (1L << (KW_DOCUMENT - 64)) | (1L << (KW_DOCUMENT_NODE - 64)) | (1L << (KW_ELEMENT - 64)) | (1L << (KW_ELSE - 64)) | (1L << (KW_EMPTY - 64)) | (1L << (KW_EMPTY_SEQUENCE - 64)) | (1L << (KW_ENCODING - 64)) | (1L << (KW_END - 64)) | (1L << (KW_EQ - 64)) | (1L << (KW_EVERY - 64)) | (1L << (KW_EXCEPT - 64)) | (1L << (KW_EXTERNAL - 64)) | (1L << (KW_FOLLOWING - 64)) | (1L << (KW_FOLLOWING_SIBLING - 64)) | (1L << (KW_FOR - 64)) | (1L << (KW_FUNCTION - 64)) | (1L << (KW_GE - 64)) | (1L << (KW_GREATEST - 64)) | (1L << (KW_GROUP - 64)) | (1L << (KW_GT - 64)) | (1L << (KW_IDIV - 64)) | (1L << (KW_IF - 64)) | (1L << (KW_IMPORT - 64)) | (1L << (KW_IN - 64)) | (1L << (KW_INHERIT - 64)) | (1L << (KW_INSTANCE - 64)) | (1L << (KW_INTERSECT - 64)) | (1L << (KW_IS - 64)) | (1L << (KW_ITEM - 64)) | (1L << (KW_LAX - 64)) | (1L << (KW_LE - 64)) | (1L << (KW_LEAST - 64)) | (1L << (KW_LET - 64)) | (1L << (KW_LT - 64)) | (1L << (KW_MAP - 64)) | (1L << (KW_MOD - 64)) | (1L << (KW_MODULE - 64)) | (1L << (KW_NAMESPACE - 64)) | (1L << (KW_NE - 64)) | (1L << (KW_NEXT - 64)) | (1L << (KW_NAMESPACE_NODE - 64)) | (1L << (KW_NO_INHERIT - 64)) | (1L << (KW_NO_PRESERVE - 64)) | (1L << (KW_NODE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (KW_OF - 128)) | (1L << (KW_ONLY - 128)) | (1L << (KW_OPTION - 128)) | (1L << (KW_OR - 128)) | (1L << (KW_ORDER - 128)) | (1L << (KW_ORDERED - 128)) | (1L << (KW_ORDERING - 128)) | (1L << (KW_PARENT - 128)) | (1L << (KW_PRECEDING - 128)) | (1L << (KW_PRECEDING_SIBLING - 128)) | (1L << (KW_PRESERVE - 128)) | (1L << (KW_PI - 128)) | (1L << (KW_RETURN - 128)) | (1L << (KW_SATISFIES - 128)) | (1L << (KW_SCHEMA - 128)) | (1L << (KW_SCHEMA_ATTR - 128)) | (1L << (KW_SCHEMA_ELEM - 128)) | (1L << (KW_SELF - 128)) | (1L << (KW_SLIDING - 128)) | (1L << (KW_SOME - 128)) | (1L << (KW_STABLE - 128)) | (1L << (KW_START - 128)) | (1L << (KW_STRICT - 128)) | (1L << (KW_STRIP - 128)) | (1L << (KW_SWITCH - 128)) | (1L << (KW_TEXT - 128)) | (1L << (KW_THEN - 128)) | (1L << (KW_TO - 128)) | (1L << (KW_TREAT - 128)) | (1L << (KW_TRY - 128)) | (1L << (KW_TUMBLING - 128)) | (1L << (KW_TYPE - 128)) | (1L << (KW_TYPESWITCH - 128)) | (1L << (KW_UNION - 128)) | (1L << (KW_UNORDERED - 128)) | (1L << (KW_UPDATE - 128)) | (1L << (KW_VALIDATE - 128)) | (1L << (KW_VARIABLE - 128)) | (1L << (KW_VERSION - 128)) | (1L << (KW_WHEN - 128)) | (1L << (KW_WHERE - 128)) | (1L << (KW_WINDOW - 128)) | (1L << (KW_XQUERY - 128)) | (1L << (KW_ARRAY_NODE - 128)) | (1L << (KW_BOOLEAN_NODE - 128)) | (1L << (KW_NULL_NODE - 128)) | (1L << (KW_NUMBER_NODE - 128)) | (1L << (KW_OBJECT_NODE - 128)) | (1L << (KW_REPLACE - 128)) | (1L << (KW_WITH - 128)) | (1L << (KW_VALUE - 128)) | (1L << (KW_INSERT - 128)) | (1L << (KW_INTO - 128)) | (1L << (KW_DELETE - 128)) | (1L << (KW_RENAME - 128)) | (1L << (URIQualifiedName - 128)) | (1L << (FullQName - 128)) | (1L << (NCName - 128)))) != 0)) {
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
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DFPropertyName) | (1L << LPAREN) | (1L << MOD) | (1L << KW_ALLOWING) | (1L << KW_ANCESTOR) | (1L << KW_ANCESTOR_OR_SELF) | (1L << KW_AND) | (1L << KW_ARRAY) | (1L << KW_AS) | (1L << KW_ASCENDING) | (1L << KW_AT) | (1L << KW_ATTRIBUTE) | (1L << KW_BASE_URI) | (1L << KW_BOUNDARY_SPACE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (KW_BINARY - 64)) | (1L << (KW_BY - 64)) | (1L << (KW_CASE - 64)) | (1L << (KW_CAST - 64)) | (1L << (KW_CASTABLE - 64)) | (1L << (KW_CATCH - 64)) | (1L << (KW_CHILD - 64)) | (1L << (KW_COLLATION - 64)) | (1L << (KW_COMMENT - 64)) | (1L << (KW_CONSTRUCTION - 64)) | (1L << (KW_CONTEXT - 64)) | (1L << (KW_COPY_NS - 64)) | (1L << (KW_COUNT - 64)) | (1L << (KW_DECLARE - 64)) | (1L << (KW_DEFAULT - 64)) | (1L << (KW_DESCENDANT - 64)) | (1L << (KW_DESCENDANT_OR_SELF - 64)) | (1L << (KW_DESCENDING - 64)) | (1L << (KW_DECIMAL_FORMAT - 64)) | (1L << (KW_DIV - 64)) | (1L << (KW_DOCUMENT - 64)) | (1L << (KW_DOCUMENT_NODE - 64)) | (1L << (KW_ELEMENT - 64)) | (1L << (KW_ELSE - 64)) | (1L << (KW_EMPTY - 64)) | (1L << (KW_EMPTY_SEQUENCE - 64)) | (1L << (KW_ENCODING - 64)) | (1L << (KW_END - 64)) | (1L << (KW_EQ - 64)) | (1L << (KW_EVERY - 64)) | (1L << (KW_EXCEPT - 64)) | (1L << (KW_EXTERNAL - 64)) | (1L << (KW_FOLLOWING - 64)) | (1L << (KW_FOLLOWING_SIBLING - 64)) | (1L << (KW_FOR - 64)) | (1L << (KW_FUNCTION - 64)) | (1L << (KW_GE - 64)) | (1L << (KW_GREATEST - 64)) | (1L << (KW_GROUP - 64)) | (1L << (KW_GT - 64)) | (1L << (KW_IDIV - 64)) | (1L << (KW_IF - 64)) | (1L << (KW_IMPORT - 64)) | (1L << (KW_IN - 64)) | (1L << (KW_INHERIT - 64)) | (1L << (KW_INSTANCE - 64)) | (1L << (KW_INTERSECT - 64)) | (1L << (KW_IS - 64)) | (1L << (KW_ITEM - 64)) | (1L << (KW_LAX - 64)) | (1L << (KW_LE - 64)) | (1L << (KW_LEAST - 64)) | (1L << (KW_LET - 64)) | (1L << (KW_LT - 64)) | (1L << (KW_MAP - 64)) | (1L << (KW_MOD - 64)) | (1L << (KW_MODULE - 64)) | (1L << (KW_NAMESPACE - 64)) | (1L << (KW_NE - 64)) | (1L << (KW_NEXT - 64)) | (1L << (KW_NAMESPACE_NODE - 64)) | (1L << (KW_NO_INHERIT - 64)) | (1L << (KW_NO_PRESERVE - 64)) | (1L << (KW_NODE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (KW_OF - 128)) | (1L << (KW_ONLY - 128)) | (1L << (KW_OPTION - 128)) | (1L << (KW_OR - 128)) | (1L << (KW_ORDER - 128)) | (1L << (KW_ORDERED - 128)) | (1L << (KW_ORDERING - 128)) | (1L << (KW_PARENT - 128)) | (1L << (KW_PRECEDING - 128)) | (1L << (KW_PRECEDING_SIBLING - 128)) | (1L << (KW_PRESERVE - 128)) | (1L << (KW_PI - 128)) | (1L << (KW_RETURN - 128)) | (1L << (KW_SATISFIES - 128)) | (1L << (KW_SCHEMA - 128)) | (1L << (KW_SCHEMA_ATTR - 128)) | (1L << (KW_SCHEMA_ELEM - 128)) | (1L << (KW_SELF - 128)) | (1L << (KW_SLIDING - 128)) | (1L << (KW_SOME - 128)) | (1L << (KW_STABLE - 128)) | (1L << (KW_START - 128)) | (1L << (KW_STRICT - 128)) | (1L << (KW_STRIP - 128)) | (1L << (KW_SWITCH - 128)) | (1L << (KW_TEXT - 128)) | (1L << (KW_THEN - 128)) | (1L << (KW_TO - 128)) | (1L << (KW_TREAT - 128)) | (1L << (KW_TRY - 128)) | (1L << (KW_TUMBLING - 128)) | (1L << (KW_TYPE - 128)) | (1L << (KW_TYPESWITCH - 128)) | (1L << (KW_UNION - 128)) | (1L << (KW_UNORDERED - 128)) | (1L << (KW_UPDATE - 128)) | (1L << (KW_VALIDATE - 128)) | (1L << (KW_VARIABLE - 128)) | (1L << (KW_VERSION - 128)) | (1L << (KW_WHEN - 128)) | (1L << (KW_WHERE - 128)) | (1L << (KW_WINDOW - 128)) | (1L << (KW_XQUERY - 128)) | (1L << (KW_ARRAY_NODE - 128)) | (1L << (KW_BOOLEAN_NODE - 128)) | (1L << (KW_NULL_NODE - 128)) | (1L << (KW_NUMBER_NODE - 128)) | (1L << (KW_OBJECT_NODE - 128)) | (1L << (KW_REPLACE - 128)) | (1L << (KW_WITH - 128)) | (1L << (KW_VALUE - 128)) | (1L << (KW_INSERT - 128)) | (1L << (KW_INTO - 128)) | (1L << (KW_DELETE - 128)) | (1L << (KW_RENAME - 128)) | (1L << (URIQualifiedName - 128)) | (1L << (FullQName - 128)) | (1L << (NCName - 128)))) != 0)) {
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
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DFPropertyName) | (1L << KW_ALLOWING) | (1L << KW_ARRAY) | (1L << KW_ATTRIBUTE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (KW_BINARY - 64)) | (1L << (KW_CATCH - 64)) | (1L << (KW_COMMENT - 64)) | (1L << (KW_CONTEXT - 64)) | (1L << (KW_COUNT - 64)) | (1L << (KW_DECIMAL_FORMAT - 64)) | (1L << (KW_DOCUMENT_NODE - 64)) | (1L << (KW_ELEMENT - 64)) | (1L << (KW_EMPTY_SEQUENCE - 64)) | (1L << (KW_END - 64)) | (1L << (KW_IF - 64)) | (1L << (KW_ITEM - 64)) | (1L << (KW_MAP - 64)) | (1L << (KW_NEXT - 64)) | (1L << (KW_NAMESPACE_NODE - 64)) | (1L << (KW_NODE - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (KW_ONLY - 129)) | (1L << (KW_PI - 129)) | (1L << (KW_SCHEMA_ATTR - 129)) | (1L << (KW_SCHEMA_ELEM - 129)) | (1L << (KW_SLIDING - 129)) | (1L << (KW_SWITCH - 129)) | (1L << (KW_TEXT - 129)) | (1L << (KW_TRY - 129)) | (1L << (KW_TUMBLING - 129)) | (1L << (KW_TYPE - 129)) | (1L << (KW_TYPESWITCH - 129)) | (1L << (KW_UPDATE - 129)) | (1L << (KW_WHEN - 129)) | (1L << (KW_WINDOW - 129)) | (1L << (KW_ARRAY_NODE - 129)) | (1L << (KW_BOOLEAN_NODE - 129)) | (1L << (KW_NULL_NODE - 129)) | (1L << (KW_NUMBER_NODE - 129)) | (1L << (KW_OBJECT_NODE - 129)) | (1L << (KW_REPLACE - 129)) | (1L << (KW_WITH - 129)) | (1L << (KW_VALUE - 129)) | (1L << (KW_INSERT - 129)) | (1L << (KW_INTO - 129)) | (1L << (KW_DELETE - 129)) | (1L << (KW_RENAME - 129)))) != 0)) ) {
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
			if ( !(((((_la - 54)) & ~0x3f) == 0 && ((1L << (_la - 54)) & ((1L << (KW_ANCESTOR - 54)) | (1L << (KW_ANCESTOR_OR_SELF - 54)) | (1L << (KW_AND - 54)) | (1L << (KW_AS - 54)) | (1L << (KW_ASCENDING - 54)) | (1L << (KW_AT - 54)) | (1L << (KW_BASE_URI - 54)) | (1L << (KW_BOUNDARY_SPACE - 54)) | (1L << (KW_BY - 54)) | (1L << (KW_CASE - 54)) | (1L << (KW_CAST - 54)) | (1L << (KW_CASTABLE - 54)) | (1L << (KW_CHILD - 54)) | (1L << (KW_COLLATION - 54)) | (1L << (KW_CONSTRUCTION - 54)) | (1L << (KW_COPY_NS - 54)) | (1L << (KW_DECLARE - 54)) | (1L << (KW_DEFAULT - 54)) | (1L << (KW_DESCENDANT - 54)) | (1L << (KW_DESCENDANT_OR_SELF - 54)) | (1L << (KW_DESCENDING - 54)) | (1L << (KW_DIV - 54)) | (1L << (KW_DOCUMENT - 54)) | (1L << (KW_ELSE - 54)) | (1L << (KW_EMPTY - 54)) | (1L << (KW_ENCODING - 54)) | (1L << (KW_EQ - 54)) | (1L << (KW_EVERY - 54)) | (1L << (KW_EXCEPT - 54)) | (1L << (KW_EXTERNAL - 54)) | (1L << (KW_FOLLOWING - 54)) | (1L << (KW_FOLLOWING_SIBLING - 54)) | (1L << (KW_FOR - 54)) | (1L << (KW_FUNCTION - 54)) | (1L << (KW_GE - 54)) | (1L << (KW_GREATEST - 54)) | (1L << (KW_GROUP - 54)) | (1L << (KW_GT - 54)) | (1L << (KW_IDIV - 54)) | (1L << (KW_IMPORT - 54)) | (1L << (KW_IN - 54)) | (1L << (KW_INHERIT - 54)) | (1L << (KW_INSTANCE - 54)) | (1L << (KW_INTERSECT - 54)) | (1L << (KW_IS - 54)) | (1L << (KW_LAX - 54)) | (1L << (KW_LE - 54)) | (1L << (KW_LEAST - 54)) | (1L << (KW_LET - 54)) | (1L << (KW_LT - 54)))) != 0) || ((((_la - 119)) & ~0x3f) == 0 && ((1L << (_la - 119)) & ((1L << (KW_MOD - 119)) | (1L << (KW_MODULE - 119)) | (1L << (KW_NAMESPACE - 119)) | (1L << (KW_NE - 119)) | (1L << (KW_NO_INHERIT - 119)) | (1L << (KW_NO_PRESERVE - 119)) | (1L << (KW_OF - 119)) | (1L << (KW_OPTION - 119)) | (1L << (KW_OR - 119)) | (1L << (KW_ORDER - 119)) | (1L << (KW_ORDERED - 119)) | (1L << (KW_ORDERING - 119)) | (1L << (KW_PARENT - 119)) | (1L << (KW_PRECEDING - 119)) | (1L << (KW_PRECEDING_SIBLING - 119)) | (1L << (KW_PRESERVE - 119)) | (1L << (KW_RETURN - 119)) | (1L << (KW_SATISFIES - 119)) | (1L << (KW_SCHEMA - 119)) | (1L << (KW_SELF - 119)) | (1L << (KW_SOME - 119)) | (1L << (KW_STABLE - 119)) | (1L << (KW_START - 119)) | (1L << (KW_STRICT - 119)) | (1L << (KW_STRIP - 119)) | (1L << (KW_THEN - 119)) | (1L << (KW_TO - 119)) | (1L << (KW_TREAT - 119)) | (1L << (KW_UNION - 119)) | (1L << (KW_UNORDERED - 119)) | (1L << (KW_VALIDATE - 119)) | (1L << (KW_VARIABLE - 119)) | (1L << (KW_VERSION - 119)) | (1L << (KW_WHERE - 119)) | (1L << (KW_XQUERY - 119)))) != 0)) ) {
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EscapeQuot) | (1L << DOUBLE_LBRACE) | (1L << DOUBLE_RBRACE) | (1L << IntegerLiteral) | (1L << DecimalLiteral) | (1L << DoubleLiteral) | (1L << DFPropertyName) | (1L << PredefinedEntityRef) | (1L << CharRef) | (1L << Apos) | (1L << COMMENT) | (1L << PRAGMA) | (1L << EQUAL) | (1L << NOT_EQUAL) | (1L << LPAREN) | (1L << RPAREN) | (1L << LBRACKET) | (1L << RBRACKET) | (1L << LBRACE) | (1L << RBRACE) | (1L << STAR) | (1L << PLUS) | (1L << MINUS) | (1L << COMMA) | (1L << DOT) | (1L << DDOT) | (1L << COLON) | (1L << COLON_EQ) | (1L << SEMICOLON) | (1L << SLASH) | (1L << DSLASH) | (1L << BACKSLASH) | (1L << VBAR) | (1L << RANGLE) | (1L << QUESTION) | (1L << AT) | (1L << DOLLAR) | (1L << MOD) | (1L << BANG) | (1L << HASH) | (1L << CARAT) | (1L << ARROW) | (1L << GRAVE) | (1L << TILDE) | (1L << KW_ALLOWING) | (1L << KW_ANCESTOR) | (1L << KW_ANCESTOR_OR_SELF) | (1L << KW_AND) | (1L << KW_ARRAY) | (1L << KW_AS) | (1L << KW_ASCENDING) | (1L << KW_AT) | (1L << KW_ATTRIBUTE) | (1L << KW_BASE_URI) | (1L << KW_BOUNDARY_SPACE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (KW_BINARY - 64)) | (1L << (KW_BY - 64)) | (1L << (KW_CASE - 64)) | (1L << (KW_CAST - 64)) | (1L << (KW_CASTABLE - 64)) | (1L << (KW_CATCH - 64)) | (1L << (KW_CHILD - 64)) | (1L << (KW_COLLATION - 64)) | (1L << (KW_COMMENT - 64)) | (1L << (KW_CONSTRUCTION - 64)) | (1L << (KW_CONTEXT - 64)) | (1L << (KW_COPY_NS - 64)) | (1L << (KW_COUNT - 64)) | (1L << (KW_DECLARE - 64)) | (1L << (KW_DEFAULT - 64)) | (1L << (KW_DESCENDANT - 64)) | (1L << (KW_DESCENDANT_OR_SELF - 64)) | (1L << (KW_DESCENDING - 64)) | (1L << (KW_DECIMAL_FORMAT - 64)) | (1L << (KW_DIV - 64)) | (1L << (KW_DOCUMENT - 64)) | (1L << (KW_DOCUMENT_NODE - 64)) | (1L << (KW_ELEMENT - 64)) | (1L << (KW_ELSE - 64)) | (1L << (KW_EMPTY - 64)) | (1L << (KW_EMPTY_SEQUENCE - 64)) | (1L << (KW_ENCODING - 64)) | (1L << (KW_END - 64)) | (1L << (KW_EQ - 64)) | (1L << (KW_EVERY - 64)) | (1L << (KW_EXCEPT - 64)) | (1L << (KW_EXTERNAL - 64)) | (1L << (KW_FOLLOWING - 64)) | (1L << (KW_FOLLOWING_SIBLING - 64)) | (1L << (KW_FOR - 64)) | (1L << (KW_FUNCTION - 64)) | (1L << (KW_GE - 64)) | (1L << (KW_GREATEST - 64)) | (1L << (KW_GROUP - 64)) | (1L << (KW_GT - 64)) | (1L << (KW_IDIV - 64)) | (1L << (KW_IF - 64)) | (1L << (KW_IMPORT - 64)) | (1L << (KW_IN - 64)) | (1L << (KW_INHERIT - 64)) | (1L << (KW_INSTANCE - 64)) | (1L << (KW_INTERSECT - 64)) | (1L << (KW_IS - 64)) | (1L << (KW_ITEM - 64)) | (1L << (KW_LAX - 64)) | (1L << (KW_LE - 64)) | (1L << (KW_LEAST - 64)) | (1L << (KW_LET - 64)) | (1L << (KW_LT - 64)) | (1L << (KW_MAP - 64)) | (1L << (KW_MOD - 64)) | (1L << (KW_MODULE - 64)) | (1L << (KW_NAMESPACE - 64)) | (1L << (KW_NE - 64)) | (1L << (KW_NEXT - 64)) | (1L << (KW_NAMESPACE_NODE - 64)) | (1L << (KW_NO_INHERIT - 64)) | (1L << (KW_NO_PRESERVE - 64)) | (1L << (KW_NODE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (KW_OF - 128)) | (1L << (KW_ONLY - 128)) | (1L << (KW_OPTION - 128)) | (1L << (KW_OR - 128)) | (1L << (KW_ORDER - 128)) | (1L << (KW_ORDERED - 128)) | (1L << (KW_ORDERING - 128)) | (1L << (KW_PARENT - 128)) | (1L << (KW_PRECEDING - 128)) | (1L << (KW_PRECEDING_SIBLING - 128)) | (1L << (KW_PRESERVE - 128)) | (1L << (KW_PREVIOUS - 128)) | (1L << (KW_PI - 128)) | (1L << (KW_RETURN - 128)) | (1L << (KW_SATISFIES - 128)) | (1L << (KW_SCHEMA - 128)) | (1L << (KW_SCHEMA_ATTR - 128)) | (1L << (KW_SCHEMA_ELEM - 128)) | (1L << (KW_SELF - 128)) | (1L << (KW_SLIDING - 128)) | (1L << (KW_SOME - 128)) | (1L << (KW_STABLE - 128)) | (1L << (KW_START - 128)) | (1L << (KW_STRICT - 128)) | (1L << (KW_STRIP - 128)) | (1L << (KW_SWITCH - 128)) | (1L << (KW_TEXT - 128)) | (1L << (KW_THEN - 128)) | (1L << (KW_TO - 128)) | (1L << (KW_TREAT - 128)) | (1L << (KW_TRY - 128)) | (1L << (KW_TUMBLING - 128)) | (1L << (KW_TYPE - 128)) | (1L << (KW_TYPESWITCH - 128)) | (1L << (KW_UNION - 128)) | (1L << (KW_UNORDERED - 128)) | (1L << (KW_UPDATE - 128)) | (1L << (KW_VALIDATE - 128)) | (1L << (KW_VARIABLE - 128)) | (1L << (KW_VERSION - 128)) | (1L << (KW_WHEN - 128)) | (1L << (KW_WHERE - 128)) | (1L << (KW_WINDOW - 128)) | (1L << (KW_XQUERY - 128)) | (1L << (KW_ARRAY_NODE - 128)) | (1L << (KW_BOOLEAN_NODE - 128)) | (1L << (KW_NULL_NODE - 128)) | (1L << (KW_NUMBER_NODE - 128)) | (1L << (KW_OBJECT_NODE - 128)) | (1L << (KW_REPLACE - 128)) | (1L << (KW_WITH - 128)) | (1L << (KW_VALUE - 128)) | (1L << (KW_INSERT - 128)) | (1L << (KW_INTO - 128)) | (1L << (KW_DELETE - 128)) | (1L << (KW_RENAME - 128)) | (1L << (URIQualifiedName - 128)) | (1L << (FullQName - 128)) | (1L << (NCNameWithLocalWildcard - 128)) | (1L << (NCNameWithPrefixWildcard - 128)) | (1L << (NCName - 128)) | (1L << (XQDOC_COMMENT_START - 128)))) != 0) || _la==ContentChar) {
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EscapeApos) | (1L << DOUBLE_LBRACE) | (1L << DOUBLE_RBRACE) | (1L << IntegerLiteral) | (1L << DecimalLiteral) | (1L << DoubleLiteral) | (1L << DFPropertyName) | (1L << PredefinedEntityRef) | (1L << CharRef) | (1L << Quot) | (1L << COMMENT) | (1L << PRAGMA) | (1L << EQUAL) | (1L << NOT_EQUAL) | (1L << LPAREN) | (1L << RPAREN) | (1L << LBRACKET) | (1L << RBRACKET) | (1L << LBRACE) | (1L << RBRACE) | (1L << STAR) | (1L << PLUS) | (1L << MINUS) | (1L << COMMA) | (1L << DOT) | (1L << DDOT) | (1L << COLON) | (1L << COLON_EQ) | (1L << SEMICOLON) | (1L << SLASH) | (1L << DSLASH) | (1L << BACKSLASH) | (1L << VBAR) | (1L << RANGLE) | (1L << QUESTION) | (1L << AT) | (1L << DOLLAR) | (1L << MOD) | (1L << BANG) | (1L << HASH) | (1L << CARAT) | (1L << ARROW) | (1L << GRAVE) | (1L << TILDE) | (1L << KW_ALLOWING) | (1L << KW_ANCESTOR) | (1L << KW_ANCESTOR_OR_SELF) | (1L << KW_AND) | (1L << KW_ARRAY) | (1L << KW_AS) | (1L << KW_ASCENDING) | (1L << KW_AT) | (1L << KW_ATTRIBUTE) | (1L << KW_BASE_URI) | (1L << KW_BOUNDARY_SPACE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (KW_BINARY - 64)) | (1L << (KW_BY - 64)) | (1L << (KW_CASE - 64)) | (1L << (KW_CAST - 64)) | (1L << (KW_CASTABLE - 64)) | (1L << (KW_CATCH - 64)) | (1L << (KW_CHILD - 64)) | (1L << (KW_COLLATION - 64)) | (1L << (KW_COMMENT - 64)) | (1L << (KW_CONSTRUCTION - 64)) | (1L << (KW_CONTEXT - 64)) | (1L << (KW_COPY_NS - 64)) | (1L << (KW_COUNT - 64)) | (1L << (KW_DECLARE - 64)) | (1L << (KW_DEFAULT - 64)) | (1L << (KW_DESCENDANT - 64)) | (1L << (KW_DESCENDANT_OR_SELF - 64)) | (1L << (KW_DESCENDING - 64)) | (1L << (KW_DECIMAL_FORMAT - 64)) | (1L << (KW_DIV - 64)) | (1L << (KW_DOCUMENT - 64)) | (1L << (KW_DOCUMENT_NODE - 64)) | (1L << (KW_ELEMENT - 64)) | (1L << (KW_ELSE - 64)) | (1L << (KW_EMPTY - 64)) | (1L << (KW_EMPTY_SEQUENCE - 64)) | (1L << (KW_ENCODING - 64)) | (1L << (KW_END - 64)) | (1L << (KW_EQ - 64)) | (1L << (KW_EVERY - 64)) | (1L << (KW_EXCEPT - 64)) | (1L << (KW_EXTERNAL - 64)) | (1L << (KW_FOLLOWING - 64)) | (1L << (KW_FOLLOWING_SIBLING - 64)) | (1L << (KW_FOR - 64)) | (1L << (KW_FUNCTION - 64)) | (1L << (KW_GE - 64)) | (1L << (KW_GREATEST - 64)) | (1L << (KW_GROUP - 64)) | (1L << (KW_GT - 64)) | (1L << (KW_IDIV - 64)) | (1L << (KW_IF - 64)) | (1L << (KW_IMPORT - 64)) | (1L << (KW_IN - 64)) | (1L << (KW_INHERIT - 64)) | (1L << (KW_INSTANCE - 64)) | (1L << (KW_INTERSECT - 64)) | (1L << (KW_IS - 64)) | (1L << (KW_ITEM - 64)) | (1L << (KW_LAX - 64)) | (1L << (KW_LE - 64)) | (1L << (KW_LEAST - 64)) | (1L << (KW_LET - 64)) | (1L << (KW_LT - 64)) | (1L << (KW_MAP - 64)) | (1L << (KW_MOD - 64)) | (1L << (KW_MODULE - 64)) | (1L << (KW_NAMESPACE - 64)) | (1L << (KW_NE - 64)) | (1L << (KW_NEXT - 64)) | (1L << (KW_NAMESPACE_NODE - 64)) | (1L << (KW_NO_INHERIT - 64)) | (1L << (KW_NO_PRESERVE - 64)) | (1L << (KW_NODE - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (KW_OF - 128)) | (1L << (KW_ONLY - 128)) | (1L << (KW_OPTION - 128)) | (1L << (KW_OR - 128)) | (1L << (KW_ORDER - 128)) | (1L << (KW_ORDERED - 128)) | (1L << (KW_ORDERING - 128)) | (1L << (KW_PARENT - 128)) | (1L << (KW_PRECEDING - 128)) | (1L << (KW_PRECEDING_SIBLING - 128)) | (1L << (KW_PRESERVE - 128)) | (1L << (KW_PREVIOUS - 128)) | (1L << (KW_PI - 128)) | (1L << (KW_RETURN - 128)) | (1L << (KW_SATISFIES - 128)) | (1L << (KW_SCHEMA - 128)) | (1L << (KW_SCHEMA_ATTR - 128)) | (1L << (KW_SCHEMA_ELEM - 128)) | (1L << (KW_SELF - 128)) | (1L << (KW_SLIDING - 128)) | (1L << (KW_SOME - 128)) | (1L << (KW_STABLE - 128)) | (1L << (KW_START - 128)) | (1L << (KW_STRICT - 128)) | (1L << (KW_STRIP - 128)) | (1L << (KW_SWITCH - 128)) | (1L << (KW_TEXT - 128)) | (1L << (KW_THEN - 128)) | (1L << (KW_TO - 128)) | (1L << (KW_TREAT - 128)) | (1L << (KW_TRY - 128)) | (1L << (KW_TUMBLING - 128)) | (1L << (KW_TYPE - 128)) | (1L << (KW_TYPESWITCH - 128)) | (1L << (KW_UNION - 128)) | (1L << (KW_UNORDERED - 128)) | (1L << (KW_UPDATE - 128)) | (1L << (KW_VALIDATE - 128)) | (1L << (KW_VARIABLE - 128)) | (1L << (KW_VERSION - 128)) | (1L << (KW_WHEN - 128)) | (1L << (KW_WHERE - 128)) | (1L << (KW_WINDOW - 128)) | (1L << (KW_XQUERY - 128)) | (1L << (KW_ARRAY_NODE - 128)) | (1L << (KW_BOOLEAN_NODE - 128)) | (1L << (KW_NULL_NODE - 128)) | (1L << (KW_NUMBER_NODE - 128)) | (1L << (KW_OBJECT_NODE - 128)) | (1L << (KW_REPLACE - 128)) | (1L << (KW_WITH - 128)) | (1L << (KW_VALUE - 128)) | (1L << (KW_INSERT - 128)) | (1L << (KW_INTO - 128)) | (1L << (KW_DELETE - 128)) | (1L << (KW_RENAME - 128)) | (1L << (URIQualifiedName - 128)) | (1L << (FullQName - 128)) | (1L << (NCNameWithLocalWildcard - 128)) | (1L << (NCNameWithPrefixWildcard - 128)) | (1L << (NCName - 128)) | (1L << (XQDOC_COMMENT_START - 128)))) != 0) || _la==ContentChar) {
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
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << DecimalLiteral) | (1L << DoubleLiteral) | (1L << COMMENT) | (1L << PRAGMA) | (1L << EQUAL) | (1L << NOT_EQUAL) | (1L << LPAREN) | (1L << RPAREN) | (1L << LBRACKET) | (1L << RBRACKET) | (1L << STAR) | (1L << PLUS) | (1L << MINUS) | (1L << COMMA) | (1L << DOT) | (1L << DDOT) | (1L << COLON) | (1L << COLON_EQ) | (1L << SEMICOLON) | (1L << SLASH) | (1L << DSLASH) | (1L << BACKSLASH) | (1L << VBAR) | (1L << RANGLE) | (1L << QUESTION) | (1L << AT) | (1L << DOLLAR) | (1L << MOD) | (1L << BANG) | (1L << HASH) | (1L << CARAT) | (1L << ARROW) | (1L << GRAVE) | (1L << TILDE))) != 0) || ((((_la - 123)) & ~0x3f) == 0 && ((1L << (_la - 123)) & ((1L << (KW_NEXT - 123)) | (1L << (KW_PREVIOUS - 123)) | (1L << (URIQualifiedName - 123)) | (1L << (FullQName - 123)) | (1L << (NCNameWithLocalWildcard - 123)))) != 0) || ((((_la - 187)) & ~0x3f) == 0 && ((1L << (_la - 187)) & ((1L << (NCNameWithPrefixWildcard - 187)) | (1L << (NCName - 187)) | (1L << (XQDOC_COMMENT_START - 187)) | (1L << (ContentChar - 187)))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00cd\u0857\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"+
		"\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089"+
		"\4\u008a\t\u008a\4\u008b\t\u008b\4\u008c\t\u008c\4\u008d\t\u008d\4\u008e"+
		"\t\u008e\4\u008f\t\u008f\4\u0090\t\u0090\4\u0091\t\u0091\4\u0092\t\u0092"+
		"\4\u0093\t\u0093\4\u0094\t\u0094\4\u0095\t\u0095\4\u0096\t\u0096\4\u0097"+
		"\t\u0097\4\u0098\t\u0098\4\u0099\t\u0099\4\u009a\t\u009a\4\u009b\t\u009b"+
		"\4\u009c\t\u009c\4\u009d\t\u009d\4\u009e\t\u009e\4\u009f\t\u009f\4\u00a0"+
		"\t\u00a0\4\u00a1\t\u00a1\4\u00a2\t\u00a2\4\u00a3\t\u00a3\4\u00a4\t\u00a4"+
		"\4\u00a5\t\u00a5\4\u00a6\t\u00a6\4\u00a7\t\u00a7\4\u00a8\t\u00a8\4\u00a9"+
		"\t\u00a9\4\u00aa\t\u00aa\4\u00ab\t\u00ab\4\u00ac\t\u00ac\4\u00ad\t\u00ad"+
		"\4\u00ae\t\u00ae\4\u00af\t\u00af\4\u00b0\t\u00b0\4\u00b1\t\u00b1\4\u00b2"+
		"\t\u00b2\4\u00b3\t\u00b3\4\u00b4\t\u00b4\4\u00b5\t\u00b5\4\u00b6\t\u00b6"+
		"\4\u00b7\t\u00b7\4\u00b8\t\u00b8\4\u00b9\t\u00b9\4\u00ba\t\u00ba\4\u00bb"+
		"\t\u00bb\4\u00bc\t\u00bc\4\u00bd\t\u00bd\4\u00be\t\u00be\4\u00bf\t\u00bf"+
		"\4\u00c0\t\u00c0\4\u00c1\t\u00c1\4\u00c2\t\u00c2\4\u00c3\t\u00c3\4\u00c4"+
		"\t\u00c4\4\u00c5\t\u00c5\4\u00c6\t\u00c6\4\u00c7\t\u00c7\4\u00c8\t\u00c8"+
		"\4\u00c9\t\u00c9\4\u00ca\t\u00ca\4\u00cb\t\u00cb\4\u00cc\t\u00cc\4\u00cd"+
		"\t\u00cd\4\u00ce\t\u00ce\4\u00cf\t\u00cf\4\u00d0\t\u00d0\4\u00d1\t\u00d1"+
		"\4\u00d2\t\u00d2\4\u00d3\t\u00d3\4\u00d4\t\u00d4\4\u00d5\t\u00d5\4\u00d6"+
		"\t\u00d6\4\u00d7\t\u00d7\4\u00d8\t\u00d8\4\u00d9\t\u00d9\4\u00da\t\u00da"+
		"\4\u00db\t\u00db\4\u00dc\t\u00dc\4\u00dd\t\u00dd\4\u00de\t\u00de\4\u00df"+
		"\t\u00df\4\u00e0\t\u00e0\4\u00e1\t\u00e1\4\u00e2\t\u00e2\4\u00e3\t\u00e3"+
		"\4\u00e4\t\u00e4\4\u00e5\t\u00e5\4\u00e6\t\u00e6\4\u00e7\t\u00e7\4\u00e8"+
		"\t\u00e8\4\u00e9\t\u00e9\4\u00ea\t\u00ea\4\u00eb\t\u00eb\4\u00ec\t\u00ec"+
		"\4\u00ed\t\u00ed\4\u00ee\t\u00ee\4\u00ef\t\u00ef\4\u00f0\t\u00f0\4\u00f1"+
		"\t\u00f1\4\u00f2\t\u00f2\4\u00f3\t\u00f3\3\2\5\2\u01e8\n\2\3\2\5\2\u01eb"+
		"\n\2\3\2\5\2\u01ee\n\2\3\2\3\2\5\2\u01f2\n\2\3\3\3\3\3\4\3\4\3\4\3\4\3"+
		"\4\5\4\u01fb\n\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\5\t\u0213\n\t\3\t\3\t\7\t\u0217\n\t"+
		"\f\t\16\t\u021a\13\t\3\t\5\t\u021d\n\t\3\t\3\t\3\t\7\t\u0222\n\t\f\t\16"+
		"\t\u0225\13\t\3\n\3\n\3\n\3\n\5\n\u022b\n\n\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u023b\n\f\3\r\3\r\3\r\3\r\3\16"+
		"\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26\3\26\5\26\u0267\n\26\3\26\3\26"+
		"\3\26\7\26\u026c\n\26\f\26\16\26\u026f\13\26\3\27\3\27\3\27\5\27\u0274"+
		"\n\27\3\27\3\27\3\27\3\27\3\27\7\27\u027b\n\27\f\27\16\27\u027e\13\27"+
		"\5\27\u0280\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u0289\n\30\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\5\31\u0291\n\31\3\31\3\31\3\31\3\31\3\31"+
		"\7\31\u0298\n\31\f\31\16\31\u029b\13\31\5\31\u029d\n\31\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u02ab\n\33\3\33\3\33"+
		"\3\33\3\33\3\33\5\33\u02b2\n\33\5\33\u02b4\n\33\3\34\3\34\3\35\3\35\3"+
		"\36\3\36\3\36\3\36\3\36\5\36\u02bf\n\36\3\36\3\36\3\36\3\36\3\36\5\36"+
		"\u02c6\n\36\5\36\u02c8\n\36\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u02d0\n"+
		"\37\3\37\3\37\5\37\u02d4\n\37\3\37\3\37\5\37\u02d8\n\37\3 \3 \3 \7 \u02dd"+
		"\n \f \16 \u02e0\13 \3!\3!\3!\5!\u02e5\n!\3\"\7\"\u02e8\n\"\f\"\16\"\u02eb"+
		"\13\"\3#\3#\3#\3#\3#\3#\5#\u02f3\n#\3$\3$\3$\7$\u02f8\n$\f$\16$\u02fb"+
		"\13$\3%\3%\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\7(\u030a\n(\f(\16(\u030d"+
		"\13(\3)\3)\3)\3)\3)\3)\3)\3)\5)\u0317\n)\3*\3*\7*\u031b\n*\f*\16*\u031e"+
		"\13*\3*\3*\3+\3+\3+\5+\u0325\n+\3,\3,\3,\3,\3,\5,\u032c\n,\3-\3-\3-\3"+
		"-\7-\u0332\n-\f-\16-\u0335\13-\3.\3.\3.\5.\u033a\n.\3.\5.\u033d\n.\3."+
		"\5.\u0340\n.\3.\3.\3.\3/\3/\3/\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61"+
		"\7\61\u0350\n\61\f\61\16\61\u0353\13\61\3\62\3\62\3\62\5\62\u0358\n\62"+
		"\3\62\3\62\3\62\3\63\3\63\3\63\5\63\u0360\n\63\3\64\3\64\3\64\3\64\3\64"+
		"\5\64\u0367\n\64\3\64\3\64\3\64\3\64\5\64\u036d\n\64\3\65\3\65\3\65\3"+
		"\65\3\65\5\65\u0374\n\65\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66"+
		"\3\66\3\67\5\67\u0381\n\67\3\67\3\67\3\67\3\67\3\67\38\38\58\u038a\n8"+
		"\38\58\u038d\n8\38\38\38\58\u0392\n8\38\38\38\58\u0397\n8\39\39\39\39"+
		"\3:\3:\3:\3;\3;\3;\3;\3<\3<\3<\7<\u03a7\n<\f<\16<\u03aa\13<\3=\3=\3=\5"+
		"=\u03af\n=\3=\3=\5=\u03b3\n=\3=\3=\5=\u03b7\n=\3>\5>\u03ba\n>\3>\3>\3"+
		">\3>\3>\7>\u03c1\n>\f>\16>\u03c4\13>\3?\3?\3?\5?\u03c9\n?\3?\3?\3?\5?"+
		"\u03ce\n?\5?\u03d0\n?\3?\3?\5?\u03d4\n?\3@\3@\3@\3A\3A\5A\u03db\nA\3A"+
		"\3A\3A\7A\u03e0\nA\fA\16A\u03e3\13A\3A\3A\3A\3B\3B\3B\5B\u03eb\nB\3B\3"+
		"B\3B\3C\3C\3C\3C\3C\6C\u03f5\nC\rC\16C\u03f6\3C\3C\3C\3C\3D\3D\6D\u03ff"+
		"\nD\rD\16D\u0400\3D\3D\3D\3E\3E\3F\3F\3F\3F\3F\6F\u040d\nF\rF\16F\u040e"+
		"\3F\3F\3F\5F\u0414\nF\3F\3F\3F\3G\3G\3G\3G\3G\5G\u041e\nG\3G\3G\3G\3G"+
		"\3H\3H\3H\7H\u0427\nH\fH\16H\u042a\13H\3I\3I\3I\3I\3I\3I\3I\3I\3I\3J\3"+
		"J\6J\u0437\nJ\rJ\16J\u0438\3K\3K\3K\3L\3L\3M\3M\3M\3M\3M\3M\3M\5M\u0447"+
		"\nM\3M\3M\3N\3N\5N\u044d\nN\3N\3N\3O\3O\3O\7O\u0454\nO\fO\16O\u0457\13"+
		"O\3P\3P\3P\3P\3P\3P\5P\u045f\nP\3Q\3Q\3Q\3Q\3Q\3R\3R\3R\3R\3R\3S\3S\3"+
		"S\3S\3S\3T\3T\3T\3U\3U\3U\3U\3U\3V\3V\3V\7V\u047b\nV\fV\16V\u047e\13V"+
		"\3W\3W\3W\7W\u0483\nW\fW\16W\u0486\13W\3X\3X\3X\3X\5X\u048c\nX\3X\3X\5"+
		"X\u0490\nX\3Y\3Y\3Y\7Y\u0495\nY\fY\16Y\u0498\13Y\3Z\3Z\3Z\5Z\u049d\nZ"+
		"\3[\3[\3[\7[\u04a2\n[\f[\16[\u04a5\13[\3\\\3\\\3\\\7\\\u04aa\n\\\f\\\16"+
		"\\\u04ad\13\\\3]\3]\3]\7]\u04b2\n]\f]\16]\u04b5\13]\3^\3^\3^\7^\u04ba"+
		"\n^\f^\16^\u04bd\13^\3_\3_\3_\3_\5_\u04c3\n_\3`\3`\3`\3`\5`\u04c9\n`\3"+
		"a\3a\3a\3a\5a\u04cf\na\3b\3b\3b\3b\5b\u04d5\nb\3c\3c\3c\7c\u04da\nc\f"+
		"c\16c\u04dd\13c\3d\3d\3d\3e\7e\u04e3\ne\fe\16e\u04e6\13e\3e\3e\3f\3f\3"+
		"f\5f\u04ed\nf\3g\3g\3g\3g\3g\3g\3g\3g\5g\u04f7\ng\3h\3h\3i\3i\3i\3i\3"+
		"i\5i\u0500\ni\3j\3j\3j\3j\5j\u0506\nj\3j\3j\3k\3k\3l\6l\u050d\nl\rl\16"+
		"l\u050e\3l\3l\3l\3l\3m\3m\3m\7m\u0518\nm\fm\16m\u051b\13m\3n\3n\5n\u051f"+
		"\nn\3n\3n\3n\5n\u0524\nn\3o\3o\3o\7o\u0529\no\fo\16o\u052c\13o\3p\3p\5"+
		"p\u0530\np\3q\3q\5q\u0534\nq\3q\3q\3r\3r\3r\3r\5r\u053c\nr\3s\3s\3s\3"+
		"s\3t\5t\u0543\nt\3t\3t\3u\3u\3u\3u\5u\u054b\nu\3v\3v\3v\3v\3w\3w\3x\3"+
		"x\5x\u0555\nx\3y\3y\5y\u0559\ny\3z\3z\3z\5z\u055e\nz\3{\3{\3{\3{\7{\u0564"+
		"\n{\f{\16{\u0567\13{\3|\3|\3|\3|\7|\u056d\n|\f|\16|\u0570\13|\5|\u0572"+
		"\n|\3|\3|\3}\7}\u0577\n}\f}\16}\u057a\13}\3~\3~\3~\3~\3\177\3\177\3\177"+
		"\3\u0080\3\u0080\3\u0080\3\u0080\5\u0080\u0587\n\u0080\3\u0081\3\u0081"+
		"\3\u0081\5\u0081\u058c\n\u0081\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082"+
		"\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\5\u0082"+
		"\u059b\n\u0082\3\u0083\3\u0083\5\u0083\u059f\n\u0083\3\u0084\3\u0084\3"+
		"\u0085\3\u0085\3\u0085\3\u0086\3\u0086\3\u0087\3\u0087\5\u0087\u05aa\n"+
		"\u0087\3\u0087\3\u0087\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\3\u008a"+
		"\3\u008a\3\u008a\3\u008b\3\u008b\3\u008b\3\u008c\3\u008c\5\u008c\u05bb"+
		"\n\u008c\3\u008d\3\u008d\5\u008d\u05bf\n\u008d\3\u008e\3\u008e\3\u008e"+
		"\5\u008e\u05c4\n\u008e\3\u008f\3\u008f\3\u008f\3\u008f\3\u008f\7\u008f"+
		"\u05cb\n\u008f\f\u008f\16\u008f\u05ce\13\u008f\3\u008f\3\u008f\3\u008f"+
		"\3\u008f\3\u008f\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\3\u0091"+
		"\3\u0091\3\u0091\3\u0091\7\u0091\u05df\n\u0091\f\u0091\16\u0091\u05e2"+
		"\13\u0091\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092\7\u0092\u05e9\n\u0092"+
		"\f\u0092\16\u0092\u05ec\13\u0092\3\u0092\3\u0092\3\u0093\3\u0093\3\u0093"+
		"\3\u0093\3\u0093\7\u0093\u05f5\n\u0093\f\u0093\16\u0093\u05f8\13\u0093"+
		"\3\u0093\3\u0093\3\u0094\3\u0094\5\u0094\u05fe\n\u0094\3\u0095\6\u0095"+
		"\u0601\n\u0095\r\u0095\16\u0095\u0602\3\u0095\3\u0095\3\u0095\3\u0095"+
		"\3\u0095\5\u0095\u060a\n\u0095\3\u0095\5\u0095\u060d\n\u0095\3\u0096\6"+
		"\u0096\u0610\n\u0096\r\u0096\16\u0096\u0611\3\u0096\3\u0096\3\u0096\3"+
		"\u0096\3\u0096\5\u0096\u0619\n\u0096\3\u0096\5\u0096\u061c\n\u0096\3\u0097"+
		"\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097\5\u0097\u0624\n\u0097\3\u0098"+
		"\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\5\u0098"+
		"\u062f\n\u0098\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099"+
		"\3\u0099\5\u0099\u0639\n\u0099\3\u009a\3\u009a\3\u009a\3\u009a\3\u009a"+
		"\3\u009a\5\u009a\u0641\n\u009a\3\u009b\3\u009b\3\u009b\3\u009c\3\u009c"+
		"\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\7\u009c"+
		"\u0650\n\u009c\f\u009c\16\u009c\u0653\13\u009c\5\u009c\u0655\n\u009c\3"+
		"\u009c\3\u009c\3\u009d\3\u009d\3\u009d\3\u009e\3\u009e\3\u009e\3\u009e"+
		"\3\u009e\3\u009f\3\u009f\3\u009f\3\u009f\3\u00a0\3\u00a0\3\u00a0\3\u00a1"+
		"\3\u00a1\3\u00a1\3\u00a2\3\u00a2\3\u00a2\3\u00a2\3\u00a2\3\u00a2\5\u00a2"+
		"\u0671\n\u00a2\3\u00a2\3\u00a2\3\u00a3\3\u00a3\3\u00a4\3\u00a4\3\u00a4"+
		"\3\u00a4\3\u00a4\3\u00a4\5\u00a4\u067d\n\u00a4\3\u00a4\3\u00a4\3\u00a5"+
		"\3\u00a5\3\u00a5\5\u00a5\u0684\n\u00a5\3\u00a5\3\u00a5\3\u00a6\3\u00a6"+
		"\3\u00a7\3\u00a7\3\u00a8\3\u00a8\3\u00a9\3\u00a9\3\u00a9\3\u00aa\3\u00aa"+
		"\3\u00aa\3\u00ab\3\u00ab\3\u00ab\3\u00ab\3\u00ab\3\u00ab\5\u00ab\u069a"+
		"\n\u00ab\3\u00ab\3\u00ab\3\u00ac\3\u00ac\5\u00ac\u06a0\n\u00ac\3\u00ad"+
		"\3\u00ad\3\u00ad\3\u00ad\3\u00ae\3\u00ae\3\u00ae\3\u00ae\5\u00ae\u06aa"+
		"\n\u00ae\3\u00ae\3\u00ae\3\u00ae\5\u00ae\u06af\n\u00ae\3\u00ae\3\u00ae"+
		"\3\u00af\3\u00af\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0\7\u00b0\u06ba"+
		"\n\u00b0\f\u00b0\16\u00b0\u06bd\13\u00b0\5\u00b0\u06bf\n\u00b0\3\u00b0"+
		"\3\u00b0\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b2\3\u00b2\5\u00b2\u06c9"+
		"\n\u00b2\3\u00b3\3\u00b3\5\u00b3\u06cd\n\u00b3\3\u00b3\3\u00b3\3\u00b4"+
		"\3\u00b4\3\u00b4\3\u00b5\3\u00b5\3\u00b5\3\u00b5\3\u00b6\3\u00b6\3\u00b6"+
		"\3\u00b6\7\u00b6\u06dc\n\u00b6\f\u00b6\16\u00b6\u06df\13\u00b6\3\u00b7"+
		"\3\u00b7\3\u00b8\3\u00b8\3\u00b9\3\u00b9\3\u00ba\3\u00ba\3\u00ba\3\u00ba"+
		"\3\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00ba\7\u00ba\u06f1\n\u00ba"+
		"\f\u00ba\16\u00ba\u06f4\13\u00ba\3\u00bb\3\u00bb\3\u00bb\3\u00bb\3\u00bc"+
		"\3\u00bc\3\u00bc\3\u00bd\3\u00bd\5\u00bd\u06ff\n\u00bd\3\u00be\3\u00be"+
		"\3\u00be\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\5\u00bf"+
		"\u070b\n\u00bf\5\u00bf\u070d\n\u00bf\3\u00c0\3\u00c0\3\u00c0\3\u00c0\3"+
		"\u00c0\3\u00c0\3\u00c0\3\u00c0\3\u00c0\5\u00c0\u0718\n\u00c0\3\u00c1\3"+
		"\u00c1\3\u00c2\3\u00c2\3\u00c2\3\u00c2\3\u00c2\3\u00c2\3\u00c2\3\u00c2"+
		"\3\u00c2\3\u00c2\3\u00c2\3\u00c2\5\u00c2\u0728\n\u00c2\3\u00c3\3\u00c3"+
		"\3\u00c3\5\u00c3\u072d\n\u00c3\3\u00c3\3\u00c3\3\u00c4\3\u00c4\3\u00c4"+
		"\3\u00c4\3\u00c5\3\u00c5\3\u00c5\3\u00c5\5\u00c5\u0739\n\u00c5\3\u00c5"+
		"\3\u00c5\3\u00c6\3\u00c6\3\u00c6\3\u00c6\3\u00c7\3\u00c7\3\u00c7\3\u00c7"+
		"\3\u00c8\3\u00c8\3\u00c8\3\u00c8\3\u00c9\3\u00c9\3\u00c9\3\u00c9\5\u00c9"+
		"\u074d\n\u00c9\3\u00c9\3\u00c9\3\u00ca\3\u00ca\3\u00ca\3\u00ca\3\u00ca"+
		"\5\u00ca\u0756\n\u00ca\5\u00ca\u0758\n\u00ca\3\u00ca\3\u00ca\3\u00cb\3"+
		"\u00cb\5\u00cb\u075e\n\u00cb\3\u00cc\3\u00cc\3\u00cc\3\u00cc\3\u00cc\3"+
		"\u00cd\3\u00cd\3\u00cd\3\u00cd\3\u00cd\3\u00cd\5\u00cd\u076b\n\u00cd\5"+
		"\u00cd\u076d\n\u00cd\5\u00cd\u076f\n\u00cd\3\u00cd\3\u00cd\3\u00ce\3\u00ce"+
		"\5\u00ce\u0775\n\u00ce\3\u00cf\3\u00cf\3\u00cf\3\u00cf\3\u00cf\3\u00d0"+
		"\3\u00d0\3\u00d1\3\u00d1\3\u00d2\3\u00d2\3\u00d3\3\u00d3\3\u00d4\3\u00d4"+
		"\3\u00d5\7\u00d5\u0787\n\u00d5\f\u00d5\16\u00d5\u078a\13\u00d5\3\u00d5"+
		"\3\u00d5\5\u00d5\u078e\n\u00d5\3\u00d6\3\u00d6\3\u00d6\3\u00d6\3\u00d6"+
		"\3\u00d7\3\u00d7\3\u00d7\3\u00d7\3\u00d7\7\u00d7\u079a\n\u00d7\f\u00d7"+
		"\16\u00d7\u079d\13\u00d7\5\u00d7\u079f\n\u00d7\3\u00d7\3\u00d7\3\u00d7"+
		"\3\u00d7\3\u00d8\3\u00d8\5\u00d8\u07a7\n\u00d8\3\u00d9\3\u00d9\3\u00d9"+
		"\3\u00d9\3\u00d9\3\u00da\3\u00da\3\u00da\3\u00da\3\u00da\3\u00da\3\u00da"+
		"\3\u00db\3\u00db\5\u00db\u07b7\n\u00db\3\u00dc\3\u00dc\3\u00dc\3\u00dc"+
		"\3\u00dc\3\u00dd\3\u00dd\3\u00dd\3\u00dd\3\u00dd\3\u00de\3\u00de\3\u00de"+
		"\3\u00de\3\u00df\3\u00df\3\u00e0\3\u00e0\3\u00e0\3\u00e0\3\u00e0\5\u00e0"+
		"\u07ce\n\u00e0\3\u00e1\3\u00e1\3\u00e1\5\u00e1\u07d3\n\u00e1\3\u00e1\3"+
		"\u00e1\3\u00e2\3\u00e2\3\u00e2\5\u00e2\u07da\n\u00e2\3\u00e2\3\u00e2\3"+
		"\u00e3\3\u00e3\3\u00e3\5\u00e3\u07e1\n\u00e3\3\u00e3\3\u00e3\3\u00e4\3"+
		"\u00e4\3\u00e4\5\u00e4\u07e8\n\u00e4\3\u00e4\3\u00e4\3\u00e5\3\u00e5\3"+
		"\u00e5\5\u00e5\u07ef\n\u00e5\3\u00e5\3\u00e5\3\u00e6\3\u00e6\5\u00e6\u07f5"+
		"\n\u00e6\3\u00e7\3\u00e7\5\u00e7\u07f9\n\u00e7\3\u00e8\3\u00e8\5\u00e8"+
		"\u07fd\n\u00e8\3\u00e9\3\u00e9\3\u00e9\3\u00e9\5\u00e9\u0803\n\u00e9\3"+
		"\u00ea\3\u00ea\5\u00ea\u0807\n\u00ea\3\u00eb\3\u00eb\3\u00ec\3\u00ec\3"+
		"\u00ed\3\u00ed\3\u00ee\3\u00ee\3\u00ee\3\u00ee\3\u00ee\7\u00ee\u0814\n"+
		"\u00ee\f\u00ee\16\u00ee\u0817\13\u00ee\3\u00ee\3\u00ee\3\u00ef\3\u00ef"+
		"\3\u00ef\3\u00ef\3\u00ef\7\u00ef\u0820\n\u00ef\f\u00ef\16\u00ef\u0823"+
		"\13\u00ef\3\u00ef\3\u00ef\3\u00f0\3\u00f0\5\u00f0\u0829\n\u00f0\3\u00f1"+
		"\6\u00f1\u082c\n\u00f1\r\u00f1\16\u00f1\u082d\3\u00f1\3\u00f1\5\u00f1"+
		"\u0832\n\u00f1\3\u00f1\5\u00f1\u0835\n\u00f1\3\u00f1\3\u00f1\3\u00f1\3"+
		"\u00f1\3\u00f1\5\u00f1\u083c\n\u00f1\3\u00f2\6\u00f2\u083f\n\u00f2\r\u00f2"+
		"\16\u00f2\u0840\3\u00f2\3\u00f2\5\u00f2\u0845\n\u00f2\3\u00f2\5\u00f2"+
		"\u0848\n\u00f2\3\u00f2\3\u00f2\3\u00f2\3\u00f2\3\u00f2\5\u00f2\u084f\n"+
		"\u00f2\3\u00f3\3\u00f3\6\u00f3\u0853\n\u00f3\r\u00f3\16\u00f3\u0854\3"+
		"\u00f3\2\2\u00f4\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62"+
		"\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088"+
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
		"\u01da\u01dc\u01de\u01e0\u01e2\u01e4\2\35\4\2XXee\4\2\u008c\u008c\u009a"+
		"\u009a\4\2\u0087\u0087\u00a5\u00a5\4\2gguu\4\2\u0080\u0080\u008c\u008c"+
		"\4\2nn\177\177\5\2bb\u008a\u008a\u00b7\u00b7\3\2\36\37\6\2\35\35UUjjy"+
		"y\4\2))\u00a4\u00a4\4\2``pp\b\2^^ffiittww||\4\2<<\u00a2\u00a2\4\2ss\u0099"+
		"\u0099\3\2&\'\7\2??HHQRbc\u0094\u0094\4\289\u0089\u008b\3\2\7\t\4\2\17"+
		"\17\21\21\3\2\13\f\3\2#$\4\2\32\33\u00c7\u00c7\5\2\32\32\64\64\u00c7\u00c7"+
		"\5\2\33\33\64\64\u00c7\u00c7\36\2\n\n\67\67;;??BBGGJJLLNNTTWX[[]]kkrr"+
		"xx}~\u0081\u0081\u0083\u0083\u008e\u008e\u0092\u0093\u0095\u0095\u009b"+
		"\u009c\u00a0\u00a3\u00a6\u00a6\u00aa\u00aa\u00ac\u00ac\u00ae\u00b9\34"+
		"\28:<>@ACFHIKKMMOSUVYZ\\\\^jlqswy|\177\u0080\u0082\u0082\u0084\u008c\u008f"+
		"\u0091\u0094\u0094\u0096\u009a\u009d\u009f\u00a4\u00a5\u00a7\u00a9\u00ab"+
		"\u00ab\u00ad\u00ad\r\2\7\t\17\17\23\23\25\32\35)+\64\66\66}}\u008d\u008d"+
		"\u00ba\u00bf\u00c6\u00c6\2\u08a6\2\u01e7\3\2\2\2\4\u01f3\3\2\2\2\6\u01f5"+
		"\3\2\2\2\b\u01fe\3\2\2\2\n\u0201\3\2\2\2\f\u0203\3\2\2\2\16\u0206\3\2"+
		"\2\2\20\u0218\3\2\2\2\22\u022a\3\2\2\2\24\u022c\3\2\2\2\26\u023a\3\2\2"+
		"\2\30\u023c\3\2\2\2\32\u0240\3\2\2\2\34\u0245\3\2\2\2\36\u0249\3\2\2\2"+
		" \u024d\3\2\2\2\"\u0251\3\2\2\2$\u0257\3\2\2\2&\u025d\3\2\2\2(\u025f\3"+
		"\2\2\2*\u0261\3\2\2\2,\u0270\3\2\2\2.\u0288\3\2\2\2\60\u028a\3\2\2\2\62"+
		"\u029e\3\2\2\2\64\u02a4\3\2\2\2\66\u02b5\3\2\2\28\u02b7\3\2\2\2:\u02b9"+
		"\3\2\2\2<\u02c9\3\2\2\2>\u02d9\3\2\2\2@\u02e1\3\2\2\2B\u02e9\3\2\2\2D"+
		"\u02ec\3\2\2\2F\u02f4\3\2\2\2H\u02fc\3\2\2\2J\u02fe\3\2\2\2L\u0301\3\2"+
		"\2\2N\u0306\3\2\2\2P\u0316\3\2\2\2R\u0318\3\2\2\2T\u0324\3\2\2\2V\u032b"+
		"\3\2\2\2X\u032d\3\2\2\2Z\u0336\3\2\2\2\\\u0344\3\2\2\2^\u0347\3\2\2\2"+
		"`\u034b\3\2\2\2b\u0354\3\2\2\2d\u035c\3\2\2\2f\u0361\3\2\2\2h\u036e\3"+
		"\2\2\2j\u037a\3\2\2\2l\u0380\3\2\2\2n\u0389\3\2\2\2p\u0398\3\2\2\2r\u039c"+
		"\3\2\2\2t\u039f\3\2\2\2v\u03a3\3\2\2\2x\u03ab\3\2\2\2z\u03b9\3\2\2\2|"+
		"\u03c5\3\2\2\2~\u03d5\3\2\2\2\u0080\u03da\3\2\2\2\u0082\u03e7\3\2\2\2"+
		"\u0084\u03ef\3\2\2\2\u0086\u03fe\3\2\2\2\u0088\u0405\3\2\2\2\u008a\u0407"+
		"\3\2\2\2\u008c\u0418\3\2\2\2\u008e\u0423\3\2\2\2\u0090\u042b\3\2\2\2\u0092"+
		"\u0434\3\2\2\2\u0094\u043a\3\2\2\2\u0096\u043d\3\2\2\2\u0098\u043f\3\2"+
		"\2\2\u009a\u044a\3\2\2\2\u009c\u0450\3\2\2\2\u009e\u0458\3\2\2\2\u00a0"+
		"\u0460\3\2\2\2\u00a2\u0465\3\2\2\2\u00a4\u046a\3\2\2\2\u00a6\u046f\3\2"+
		"\2\2\u00a8\u0472\3\2\2\2\u00aa\u0477\3\2\2\2\u00ac\u047f\3\2\2\2\u00ae"+
		"\u0487\3\2\2\2\u00b0\u0491\3\2\2\2\u00b2\u0499\3\2\2\2\u00b4\u049e\3\2"+
		"\2\2\u00b6\u04a6\3\2\2\2\u00b8\u04ae\3\2\2\2\u00ba\u04b6\3\2\2\2\u00bc"+
		"\u04be\3\2\2\2\u00be\u04c4\3\2\2\2\u00c0\u04ca\3\2\2\2\u00c2\u04d0\3\2"+
		"\2\2\u00c4\u04d6\3\2\2\2\u00c6\u04de\3\2\2\2\u00c8\u04e4\3\2\2\2\u00ca"+
		"\u04ec\3\2\2\2\u00cc\u04f6\3\2\2\2\u00ce\u04f8\3\2\2\2\u00d0\u04ff\3\2"+
		"\2\2\u00d2\u0501\3\2\2\2\u00d4\u0509\3\2\2\2\u00d6\u050c\3\2\2\2\u00d8"+
		"\u0514\3\2\2\2\u00da\u0523\3\2\2\2\u00dc\u0525\3\2\2\2\u00de\u052f\3\2"+
		"\2\2\u00e0\u0533\3\2\2\2\u00e2\u053b\3\2\2\2\u00e4\u053d\3\2\2\2\u00e6"+
		"\u0542\3\2\2\2\u00e8\u054a\3\2\2\2\u00ea\u054c\3\2\2\2\u00ec\u0550\3\2"+
		"\2\2\u00ee\u0554\3\2\2\2\u00f0\u0558\3\2\2\2\u00f2\u055d\3\2\2\2\u00f4"+
		"\u055f\3\2\2\2\u00f6\u0568\3\2\2\2\u00f8\u0578\3\2\2\2\u00fa\u057b\3\2"+
		"\2\2\u00fc\u057f\3\2\2\2\u00fe\u0586\3\2\2\2\u0100\u058b\3\2\2\2\u0102"+
		"\u059a\3\2\2\2\u0104\u059e\3\2\2\2\u0106\u05a0\3\2\2\2\u0108\u05a2\3\2"+
		"\2\2\u010a\u05a5\3\2\2\2\u010c\u05a7\3\2\2\2\u010e\u05ad\3\2\2\2\u0110"+
		"\u05af\3\2\2\2\u0112\u05b2\3\2\2\2\u0114\u05b5\3\2\2\2\u0116\u05ba\3\2"+
		"\2\2\u0118\u05be\3\2\2\2\u011a\u05c3\3\2\2\2\u011c\u05c5\3\2\2\2\u011e"+
		"\u05d4\3\2\2\2\u0120\u05e0\3\2\2\2\u0122\u05e3\3\2\2\2\u0124\u05ef\3\2"+
		"\2\2\u0126\u05fd\3\2\2\2\u0128\u060c\3\2\2\2\u012a\u061b\3\2\2\2\u012c"+
		"\u0623\3\2\2\2\u012e\u062e\3\2\2\2\u0130\u0638\3\2\2\2\u0132\u0640\3\2"+
		"\2\2\u0134\u0642\3\2\2\2\u0136\u0645\3\2\2\2\u0138\u0658\3\2\2\2\u013a"+
		"\u065b\3\2\2\2\u013c\u0660\3\2\2\2\u013e\u0664\3\2\2\2\u0140\u0667\3\2"+
		"\2\2\u0142\u066a\3\2\2\2\u0144\u0674\3\2\2\2\u0146\u0676\3\2\2\2\u0148"+
		"\u0680\3\2\2\2\u014a\u0687\3\2\2\2\u014c\u0689\3\2\2\2\u014e\u068b\3\2"+
		"\2\2\u0150\u068d\3\2\2\2\u0152\u0690\3\2\2\2\u0154\u0693\3\2\2\2\u0156"+
		"\u069f\3\2\2\2\u0158\u06a1\3\2\2\2\u015a\u06a5\3\2\2\2\u015c\u06b2\3\2"+
		"\2\2\u015e\u06b4\3\2\2\2\u0160\u06c2\3\2\2\2\u0162\u06c8\3\2\2\2\u0164"+
		"\u06ca\3\2\2\2\u0166\u06d0\3\2\2\2\u0168\u06d3\3\2\2\2\u016a\u06d7\3\2"+
		"\2\2\u016c\u06e0\3\2\2\2\u016e\u06e2\3\2\2\2\u0170\u06e4\3\2\2\2\u0172"+
		"\u06f2\3\2\2\2\u0174\u06f5\3\2\2\2\u0176\u06f9\3\2\2\2\u0178\u06fc\3\2"+
		"\2\2\u017a\u0700\3\2\2\2\u017c\u070c\3\2\2\2\u017e\u0717\3\2\2\2\u0180"+
		"\u0719\3\2\2\2\u0182\u0727\3\2\2\2\u0184\u0729\3\2\2\2\u0186\u0730\3\2"+
		"\2\2\u0188\u0734\3\2\2\2\u018a\u073c\3\2\2\2\u018c\u0740\3\2\2\2\u018e"+
		"\u0744\3\2\2\2\u0190\u0748\3\2\2\2\u0192\u0750\3\2\2\2\u0194\u075d\3\2"+
		"\2\2\u0196\u075f\3\2\2\2\u0198\u0764\3\2\2\2\u019a\u0774\3\2\2\2\u019c"+
		"\u0776\3\2\2\2\u019e\u077b\3\2\2\2\u01a0\u077d\3\2\2\2\u01a2\u077f\3\2"+
		"\2\2\u01a4\u0781\3\2\2\2\u01a6\u0783\3\2\2\2\u01a8\u0788\3\2\2\2\u01aa"+
		"\u078f\3\2\2\2\u01ac\u0794\3\2\2\2\u01ae\u07a6\3\2\2\2\u01b0\u07a8\3\2"+
		"\2\2\u01b2\u07ad\3\2\2\2\u01b4\u07b6\3\2\2\2\u01b6\u07b8\3\2\2\2\u01b8"+
		"\u07bd\3\2\2\2\u01ba\u07c2\3\2\2\2\u01bc\u07c6\3\2\2\2\u01be\u07cd\3\2"+
		"\2\2\u01c0\u07cf\3\2\2\2\u01c2\u07d6\3\2\2\2\u01c4\u07dd\3\2\2\2\u01c6"+
		"\u07e4\3\2\2\2\u01c8\u07eb\3\2\2\2\u01ca\u07f4\3\2\2\2\u01cc\u07f8\3\2"+
		"\2\2\u01ce\u07fc\3\2\2\2\u01d0\u0802\3\2\2\2\u01d2\u0806\3\2\2\2\u01d4"+
		"\u0808\3\2\2\2\u01d6\u080a\3\2\2\2\u01d8\u080c\3\2\2\2\u01da\u080e\3\2"+
		"\2\2\u01dc\u081a\3\2\2\2\u01de\u0828\3\2\2\2\u01e0\u083b\3\2\2\2\u01e2"+
		"\u084e\3\2\2\2\u01e4\u0852\3\2\2\2\u01e6\u01e8\5\4\3\2\u01e7\u01e6\3\2"+
		"\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01ea\3\2\2\2\u01e9\u01eb\5\6\4\2\u01ea"+
		"\u01e9\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb\u01ed\3\2\2\2\u01ec\u01ee\5\4"+
		"\3\2\u01ed\u01ec\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u01f1\3\2\2\2\u01ef"+
		"\u01f2\5\f\7\2\u01f0\u01f2\5\b\5\2\u01f1\u01ef\3\2\2\2\u01f1\u01f0\3\2"+
		"\2\2\u01f2\3\3\2\2\2\u01f3\u01f4\7\u00c1\2\2\u01f4\5\3\2\2\2\u01f5\u01f6"+
		"\7\u00ad\2\2\u01f6\u01f7\7\u00a9\2\2\u01f7\u01fa\5\u01de\u00f0\2\u01f8"+
		"\u01f9\7\\\2\2\u01f9\u01fb\5\u01de\u00f0\2\u01fa\u01f8\3\2\2\2\u01fa\u01fb"+
		"\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01fd\7%\2\2\u01fd\7\3\2\2\2\u01fe"+
		"\u01ff\5\20\t\2\u01ff\u0200\5\n\6\2\u0200\t\3\2\2\2\u0201\u0202\5N(\2"+
		"\u0202\13\3\2\2\2\u0203\u0204\5\16\b\2\u0204\u0205\5\20\t\2\u0205\r\3"+
		"\2\2\2\u0206\u0207\7z\2\2\u0207\u0208\7{\2\2\u0208\u0209\5\u01ce\u00e8"+
		"\2\u0209\u020a\7\25\2\2\u020a\u020b\5\u01d8\u00ed\2\u020b\u020c\7%\2\2"+
		"\u020c\17\3\2\2\2\u020d\u0213\5\24\13\2\u020e\u0213\5\26\f\2\u020f\u0213"+
		"\5\62\32\2\u0210\u0213\5,\27\2\u0211\u0213\5\60\31\2\u0212\u020d\3\2\2"+
		"\2\u0212\u020e\3\2\2\2\u0212\u020f\3\2\2\2\u0212\u0210\3\2\2\2\u0212\u0211"+
		"\3\2\2\2\u0213\u0214\3\2\2\2\u0214\u0215\7%\2\2\u0215\u0217\3\2\2\2\u0216"+
		"\u0212\3\2\2\2\u0217\u021a\3\2\2\2\u0218\u0216\3\2\2\2\u0218\u0219\3\2"+
		"\2\2\u0219\u0223\3\2\2\2\u021a\u0218\3\2\2\2\u021b\u021d\5\4\3\2\u021c"+
		"\u021b\3\2\2\2\u021c\u021d\3\2\2\2\u021d\u021e\3\2\2\2\u021e\u021f\5\22"+
		"\n\2\u021f\u0220\7%\2\2\u0220\u0222\3\2\2\2\u0221\u021c\3\2\2\2\u0222"+
		"\u0225\3\2\2\2\u0223\u0221\3\2\2\2\u0223\u0224\3\2\2\2\u0224\21\3\2\2"+
		"\2\u0225\u0223\3\2\2\2\u0226\u022b\5\64\33\2\u0227\u022b\5<\37\2\u0228"+
		"\u022b\5:\36\2\u0229\u022b\5L\'\2\u022a\u0226\3\2\2\2\u022a\u0227\3\2"+
		"\2\2\u022a\u0228\3\2\2\2\u022a\u0229\3\2\2\2\u022b\23\3\2\2\2\u022c\u022d"+
		"\7O\2\2\u022d\u022e\7P\2\2\u022e\u022f\t\2\2\2\u022f\u0230\7{\2\2\u0230"+
		"\u0231\5\u01de\u00f0\2\u0231\25\3\2\2\2\u0232\u023b\5\30\r\2\u0233\u023b"+
		"\5\32\16\2\u0234\u023b\5\34\17\2\u0235\u023b\5\36\20\2\u0236\u023b\5 "+
		"\21\2\u0237\u023b\5\"\22\2\u0238\u023b\5$\23\2\u0239\u023b\5*\26\2\u023a"+
		"\u0232\3\2\2\2\u023a\u0233\3\2\2\2\u023a\u0234\3\2\2\2\u023a\u0235\3\2"+
		"\2\2\u023a\u0236\3\2\2\2\u023a\u0237\3\2\2\2\u023a\u0238\3\2\2\2\u023a"+
		"\u0239\3\2\2\2\u023b\27\3\2\2\2\u023c\u023d\7O\2\2\u023d\u023e\7A\2\2"+
		"\u023e\u023f\t\3\2\2\u023f\31\3\2\2\2\u0240\u0241\7O\2\2\u0241\u0242\7"+
		"P\2\2\u0242\u0243\7I\2\2\u0243\u0244\5\u01d8\u00ed\2\u0244\33\3\2\2\2"+
		"\u0245\u0246\7O\2\2\u0246\u0247\7@\2\2\u0247\u0248\5\u01d8\u00ed\2\u0248"+
		"\35\3\2\2\2\u0249\u024a\7O\2\2\u024a\u024b\7K\2\2\u024b\u024c\t\3\2\2"+
		"\u024c\37\3\2\2\2\u024d\u024e\7O\2\2\u024e\u024f\7\u0088\2\2\u024f\u0250"+
		"\t\4\2\2\u0250!\3\2\2\2\u0251\u0252\7O\2\2\u0252\u0253\7P\2\2\u0253\u0254"+
		"\7\u0086\2\2\u0254\u0255\7Z\2\2\u0255\u0256\t\5\2\2\u0256#\3\2\2\2\u0257"+
		"\u0258\7O\2\2\u0258\u0259\7M\2\2\u0259\u025a\5&\24\2\u025a\u025b\7 \2"+
		"\2\u025b\u025c\5(\25\2\u025c%\3\2\2\2\u025d\u025e\t\6\2\2\u025e\'\3\2"+
		"\2\2\u025f\u0260\t\7\2\2\u0260)\3\2\2\2\u0261\u0266\7O\2\2\u0262\u0263"+
		"\7T\2\2\u0263\u0267\5\u01ca\u00e6\2\u0264\u0265\7P\2\2\u0265\u0267\7T"+
		"\2\2\u0266\u0262\3\2\2\2\u0266\u0264\3\2\2\2\u0267\u026d\3\2\2\2\u0268"+
		"\u0269\7\n\2\2\u0269\u026a\7\25\2\2\u026a\u026c\5\u01de\u00f0\2\u026b"+
		"\u0268\3\2\2\2\u026c\u026f\3\2\2\2\u026d\u026b\3\2\2\2\u026d\u026e\3\2"+
		"\2\2\u026e+\3\2\2\2\u026f\u026d\3\2\2\2\u0270\u0271\7l\2\2\u0271\u0273"+
		"\7\u0091\2\2\u0272\u0274\5.\30\2\u0273\u0272\3\2\2\2\u0273\u0274\3\2\2"+
		"\2\u0274\u0275\3\2\2\2\u0275\u027f\5\u01d8\u00ed\2\u0276\u0277\7>\2\2"+
		"\u0277\u027c\5\u01d8\u00ed\2\u0278\u0279\7 \2\2\u0279\u027b\5\u01d8\u00ed"+
		"\2\u027a\u0278\3\2\2\2\u027b\u027e\3\2\2\2\u027c\u027a\3\2\2\2\u027c\u027d"+
		"\3\2\2\2\u027d\u0280\3\2\2\2\u027e\u027c\3\2\2\2\u027f\u0276\3\2\2\2\u027f"+
		"\u0280\3\2\2\2\u0280-\3\2\2\2\u0281\u0282\7{\2\2\u0282\u0283\5\u01ce\u00e8"+
		"\2\u0283\u0284\7\25\2\2\u0284\u0289\3\2\2\2\u0285\u0286\7P\2\2\u0286\u0287"+
		"\7X\2\2\u0287\u0289\7{\2\2\u0288\u0281\3\2\2\2\u0288\u0285\3\2\2\2\u0289"+
		"/\3\2\2\2\u028a\u028b\7l\2\2\u028b\u0290\7z\2\2\u028c\u028d\7{\2\2\u028d"+
		"\u028e\5\u01ce\u00e8\2\u028e\u028f\7\25\2\2\u028f\u0291\3\2\2\2\u0290"+
		"\u028c\3\2\2\2\u0290\u0291\3\2\2\2\u0291\u0292\3\2\2\2\u0292\u029c\5\u01d8"+
		"\u00ed\2\u0293\u0294\7>\2\2\u0294\u0299\5\u01d8\u00ed\2\u0295\u0296\7"+
		" \2\2\u0296\u0298\5\u01d8\u00ed\2\u0297\u0295\3\2\2\2\u0298\u029b\3\2"+
		"\2\2\u0299\u0297\3\2\2\2\u0299\u029a\3\2\2\2\u029a\u029d\3\2\2\2\u029b"+
		"\u0299\3\2\2\2\u029c\u0293\3\2\2\2\u029c\u029d\3\2\2\2\u029d\61\3\2\2"+
		"\2\u029e\u029f\7O\2\2\u029f\u02a0\7{\2\2\u02a0\u02a1\5\u01ce\u00e8\2\u02a1"+
		"\u02a2\7\25\2\2\u02a2\u02a3\5\u01d8\u00ed\2\u02a3\63\3\2\2\2\u02a4\u02a5"+
		"\7O\2\2\u02a5\u02a6\5B\"\2\u02a6\u02a7\7\u00a8\2\2\u02a7\u02a8\7.\2\2"+
		"\u02a8\u02aa\5\u010a\u0086\2\u02a9\u02ab\5\u017a\u00be\2\u02aa\u02a9\3"+
		"\2\2\2\u02aa\u02ab\3\2\2\2\u02ab\u02b3\3\2\2\2\u02ac\u02ad\7$\2\2\u02ad"+
		"\u02b4\5\66\34\2\u02ae\u02b1\7a\2\2\u02af\u02b0\7$\2\2\u02b0\u02b2\58"+
		"\35\2\u02b1\u02af\3\2\2\2\u02b1\u02b2\3\2\2\2\u02b2\u02b4\3\2\2\2\u02b3"+
		"\u02ac\3\2\2\2\u02b3\u02ae\3\2\2\2\u02b4\65\3\2\2\2\u02b5\u02b6\5P)\2"+
		"\u02b6\67\3\2\2\2\u02b7\u02b8\5P)\2\u02b89\3\2\2\2\u02b9\u02ba\7O\2\2"+
		"\u02ba\u02bb\7L\2\2\u02bb\u02be\7r\2\2\u02bc\u02bd\7<\2\2\u02bd\u02bf"+
		"\5\u017e\u00c0\2\u02be\u02bc\3\2\2\2\u02be\u02bf\3\2\2\2\u02bf\u02c7\3"+
		"\2\2\2\u02c0\u02c1\7$\2\2\u02c1\u02c8\5P)\2\u02c2\u02c5\7a\2\2\u02c3\u02c4"+
		"\7$\2\2\u02c4\u02c6\5P)\2\u02c5\u02c3\3\2\2\2\u02c5\u02c6\3\2\2\2\u02c6"+
		"\u02c8\3\2\2\2\u02c7\u02c0\3\2\2\2\u02c7\u02c2\3\2\2\2\u02c8;\3\2\2\2"+
		"\u02c9\u02ca\7O\2\2\u02ca\u02cb\5B\"\2\u02cb\u02cc\7e\2\2\u02cc\u02cd"+
		"\5\u01ca\u00e6\2\u02cd\u02cf\7\27\2\2\u02ce\u02d0\5> \2\u02cf\u02ce\3"+
		"\2\2\2\u02cf\u02d0\3\2\2\2\u02d0\u02d1\3\2\2\2\u02d1\u02d3\7\30\2\2\u02d2"+
		"\u02d4\5J&\2\u02d3\u02d2\3\2\2\2\u02d3\u02d4\3\2\2\2\u02d4\u02d7\3\2\2"+
		"\2\u02d5\u02d8\5\u015c\u00af\2\u02d6\u02d8\7a\2\2\u02d7\u02d5\3\2\2\2"+
		"\u02d7\u02d6\3\2\2\2\u02d8=\3\2\2\2\u02d9\u02de\5@!\2\u02da\u02db\7 \2"+
		"\2\u02db\u02dd\5@!\2\u02dc\u02da\3\2\2\2\u02dd\u02e0\3\2\2\2\u02de\u02dc"+
		"\3\2\2\2\u02de\u02df\3\2\2\2\u02df?\3\2\2\2\u02e0\u02de\3\2\2\2\u02e1"+
		"\u02e2\7.\2\2\u02e2\u02e4\5\u01cc\u00e7\2\u02e3\u02e5\5\u017a\u00be\2"+
		"\u02e4\u02e3\3\2\2\2\u02e4\u02e5\3\2\2\2\u02e5A\3\2\2\2\u02e6\u02e8\5"+
		"D#\2\u02e7\u02e6\3\2\2\2\u02e8\u02eb\3\2\2\2\u02e9\u02e7\3\2\2\2\u02e9"+
		"\u02ea\3\2\2\2\u02eaC\3\2\2\2\u02eb\u02e9\3\2\2\2\u02ec\u02ed\7/\2\2\u02ed"+
		"\u02f2\5\u01cc\u00e7\2\u02ee\u02ef\7\27\2\2\u02ef\u02f0\5F$\2\u02f0\u02f1"+
		"\7\30\2\2\u02f1\u02f3\3\2\2\2\u02f2\u02ee\3\2\2\2\u02f2\u02f3\3\2\2\2"+
		"\u02f3E\3\2\2\2\u02f4\u02f9\5H%\2\u02f5\u02f6\7 \2\2\u02f6\u02f8\5H%\2"+
		"\u02f7\u02f5\3\2\2\2\u02f8\u02fb\3\2\2\2\u02f9\u02f7\3\2\2\2\u02f9\u02fa"+
		"\3\2\2\2\u02faG\3\2\2\2\u02fb\u02f9\3\2\2\2\u02fc\u02fd\5\u0104\u0083"+
		"\2\u02fdI\3\2\2\2\u02fe\u02ff\7<\2\2\u02ff\u0300\5\u017c\u00bf\2\u0300"+
		"K\3\2\2\2\u0301\u0302\7O\2\2\u0302\u0303\7\u0084\2\2\u0303\u0304\5\u01cc"+
		"\u00e7\2\u0304\u0305\5\u01de\u00f0\2\u0305M\3\2\2\2\u0306\u030b\5P)\2"+
		"\u0307\u0308\7 \2\2\u0308\u030a\5P)\2\u0309\u0307\3\2\2\2\u030a\u030d"+
		"\3\2\2\2\u030b\u0309\3\2\2\2\u030b\u030c\3\2\2\2\u030cO\3\2\2\2\u030d"+
		"\u030b\3\2\2\2\u030e\u0317\5R*\2\u030f\u0317\5\u0080A\2\u0310\u0317\5"+
		"\u0084C\2\u0311\u0317\5\u008aF\2\u0312\u0317\5\u009eP\2\u0313\u0317\5"+
		"\u0090I\2\u0314\u0317\5\u0092J\2\u0315\u0317\5\u00aaV\2\u0316\u030e\3"+
		"\2\2\2\u0316\u030f\3\2\2\2\u0316\u0310\3\2\2\2\u0316\u0311\3\2\2\2\u0316"+
		"\u0312\3\2\2\2\u0316\u0313\3\2\2\2\u0316\u0314\3\2\2\2\u0316\u0315\3\2"+
		"\2\2\u0317Q\3\2\2\2\u0318\u031c\5T+\2\u0319\u031b\5V,\2\u031a\u0319\3"+
		"\2\2\2\u031b\u031e\3\2\2\2\u031c\u031a\3\2\2\2\u031c\u031d\3\2\2\2\u031d"+
		"\u031f\3\2\2\2\u031e\u031c\3\2\2\2\u031f\u0320\5~@\2\u0320S\3\2\2\2\u0321"+
		"\u0325\5X-\2\u0322\u0325\5`\61\2\u0323\u0325\5d\63\2\u0324\u0321\3\2\2"+
		"\2\u0324\u0322\3\2\2\2\u0324\u0323\3\2\2\2\u0325U\3\2\2\2\u0326\u032c"+
		"\5T+\2\u0327\u032c\5r:\2\u0328\u032c\5t;\2\u0329\u032c\5z>\2\u032a\u032c"+
		"\5p9\2\u032b\u0326\3\2\2\2\u032b\u0327\3\2\2\2\u032b\u0328\3\2\2\2\u032b"+
		"\u0329\3\2\2\2\u032b\u032a\3\2\2\2\u032cW\3\2\2\2\u032d\u032e\7d\2\2\u032e"+
		"\u0333\5Z.\2\u032f\u0330\7 \2\2\u0330\u0332\5Z.\2\u0331\u032f\3\2\2\2"+
		"\u0332\u0335\3\2\2\2\u0333\u0331\3\2\2\2\u0333\u0334\3\2\2\2\u0334Y\3"+
		"\2\2\2\u0335\u0333\3\2\2\2\u0336\u0337\7.\2\2\u0337\u0339\5\u010a\u0086"+
		"\2\u0338\u033a\5\u017a\u00be\2\u0339\u0338\3\2\2\2\u0339\u033a\3\2\2\2"+
		"\u033a\u033c\3\2\2\2\u033b\u033d\5\\/\2\u033c\u033b\3\2\2\2\u033c\u033d"+
		"\3\2\2\2\u033d\u033f\3\2\2\2\u033e\u0340\5^\60\2\u033f\u033e\3\2\2\2\u033f"+
		"\u0340\3\2\2\2\u0340\u0341\3\2\2\2\u0341\u0342\7m\2\2\u0342\u0343\5P)"+
		"\2\u0343[\3\2\2\2\u0344\u0345\7\67\2\2\u0345\u0346\7Z\2\2\u0346]\3\2\2"+
		"\2\u0347\u0348\7>\2\2\u0348\u0349\7.\2\2\u0349\u034a\5\u010a\u0086\2\u034a"+
		"_\3\2\2\2\u034b\u034c\7v\2\2\u034c\u0351\5b\62\2\u034d\u034e\7 \2\2\u034e"+
		"\u0350\5b\62\2\u034f\u034d\3\2\2\2\u0350\u0353\3\2\2\2\u0351\u034f\3\2"+
		"\2\2\u0351\u0352\3\2\2\2\u0352a\3\2\2\2\u0353\u0351\3\2\2\2\u0354\u0355"+
		"\7.\2\2\u0355\u0357\5\u010a\u0086\2\u0356\u0358\5\u017a\u00be\2\u0357"+
		"\u0356\3\2\2\2\u0357\u0358\3\2\2\2\u0358\u0359\3\2\2\2\u0359\u035a\7$"+
		"\2\2\u035a\u035b\5P)\2\u035bc\3\2\2\2\u035c\u035f\7d\2\2\u035d\u0360\5"+
		"f\64\2\u035e\u0360\5h\65\2\u035f\u035d\3\2\2\2\u035f\u035e\3\2\2\2\u0360"+
		"e\3\2\2\2\u0361\u0362\7\u00a1\2\2\u0362\u0363\7\u00ac\2\2\u0363\u0364"+
		"\7.\2\2\u0364\u0366\5\u01cc\u00e7\2\u0365\u0367\5\u017a\u00be\2\u0366"+
		"\u0365\3\2\2\2\u0366\u0367\3\2\2\2\u0367\u0368\3\2\2\2\u0368\u0369\7m"+
		"\2\2\u0369\u036a\5P)\2\u036a\u036c\5j\66\2\u036b\u036d\5l\67\2\u036c\u036b"+
		"\3\2\2\2\u036c\u036d\3\2\2\2\u036dg\3\2\2\2\u036e\u036f\7\u0095\2\2\u036f"+
		"\u0370\7\u00ac\2\2\u0370\u0371\7.\2\2\u0371\u0373\5\u01cc\u00e7\2\u0372"+
		"\u0374\5\u017a\u00be\2\u0373\u0372\3\2\2\2\u0373\u0374\3\2\2\2\u0374\u0375"+
		"\3\2\2\2\u0375\u0376\7m\2\2\u0376\u0377\5P)\2\u0377\u0378\5j\66\2\u0378"+
		"\u0379\5l\67\2\u0379i\3\2\2\2\u037a\u037b\7\u0098\2\2\u037b\u037c\5n8"+
		"\2\u037c\u037d\7\u00aa\2\2\u037d\u037e\5P)\2\u037ek\3\2\2\2\u037f\u0381"+
		"\7\u0083\2\2\u0380\u037f\3\2\2\2\u0380\u0381\3\2\2\2\u0381\u0382\3\2\2"+
		"\2\u0382\u0383\7]\2\2\u0383\u0384\5n8\2\u0384\u0385\7\u00aa\2\2\u0385"+
		"\u0386\5P)\2\u0386m\3\2\2\2\u0387\u0388\7.\2\2\u0388\u038a\5\u01ca\u00e6"+
		"\2\u0389\u0387\3\2\2\2\u0389\u038a\3\2\2\2\u038a\u038c\3\2\2\2\u038b\u038d"+
		"\5^\60\2\u038c\u038b\3\2\2\2\u038c\u038d\3\2\2\2\u038d\u0391\3\2\2\2\u038e"+
		"\u038f\7\u008d\2\2\u038f\u0390\7.\2\2\u0390\u0392\5\u01ca\u00e6\2\u0391"+
		"\u038e\3\2\2\2\u0391\u0392\3\2\2\2\u0392\u0396\3\2\2\2\u0393\u0394\7}"+
		"\2\2\u0394\u0395\7.\2\2\u0395\u0397\5\u01ca\u00e6\2\u0396\u0393\3\2\2"+
		"\2\u0396\u0397\3\2\2\2\u0397o\3\2\2\2\u0398\u0399\7N\2\2\u0399\u039a\7"+
		".\2\2\u039a\u039b\5\u010a\u0086\2\u039bq\3\2\2\2\u039c\u039d\7\u00ab\2"+
		"\2\u039d\u039e\5P)\2\u039es\3\2\2\2\u039f\u03a0\7h\2\2\u03a0\u03a1\7C"+
		"\2\2\u03a1\u03a2\5v<\2\u03a2u\3\2\2\2\u03a3\u03a8\5x=\2\u03a4\u03a5\7"+
		" \2\2\u03a5\u03a7\5x=\2\u03a6\u03a4\3\2\2\2\u03a7\u03aa\3\2\2\2\u03a8"+
		"\u03a6\3\2\2\2\u03a8\u03a9\3\2\2\2\u03a9w\3\2\2\2\u03aa\u03a8\3\2\2\2"+
		"\u03ab\u03ac\7.\2\2\u03ac\u03b2\5\u010a\u0086\2\u03ad\u03af\5\u017a\u00be"+
		"\2\u03ae\u03ad\3\2\2\2\u03ae\u03af\3\2\2\2\u03af\u03b0\3\2\2\2\u03b0\u03b1"+
		"\7$\2\2\u03b1\u03b3\5P)\2\u03b2\u03ae\3\2\2\2\u03b2\u03b3\3\2\2\2\u03b3"+
		"\u03b6\3\2\2\2\u03b4\u03b5\7I\2\2\u03b5\u03b7\5\u01d8\u00ed\2\u03b6\u03b4"+
		"\3\2\2\2\u03b6\u03b7\3\2\2\2\u03b7y\3\2\2\2\u03b8\u03ba\7\u0097\2\2\u03b9"+
		"\u03b8\3\2\2\2\u03b9\u03ba\3\2\2\2\u03ba\u03bb\3\2\2\2\u03bb\u03bc\7\u0086"+
		"\2\2\u03bc\u03bd\7C\2\2\u03bd\u03c2\5|?\2\u03be\u03bf\7 \2\2\u03bf\u03c1"+
		"\5|?\2\u03c0\u03be\3\2\2\2\u03c1\u03c4\3\2\2\2\u03c2\u03c0\3\2\2\2\u03c2"+
		"\u03c3\3\2\2\2\u03c3{\3\2\2\2\u03c4\u03c2\3\2\2\2\u03c5\u03c8\5P)\2\u03c6"+
		"\u03c9\7=\2\2\u03c7\u03c9\7S\2\2\u03c8\u03c6\3\2\2\2\u03c8\u03c7\3\2\2"+
		"\2\u03c8\u03c9\3\2\2\2\u03c9\u03cf\3\2\2\2\u03ca\u03cd\7Z\2\2\u03cb\u03ce"+
		"\7g\2\2\u03cc\u03ce\7u\2\2\u03cd\u03cb\3\2\2\2\u03cd\u03cc\3\2\2\2\u03ce"+
		"\u03d0\3\2\2\2\u03cf\u03ca\3\2\2\2\u03cf\u03d0\3\2\2\2\u03d0\u03d3\3\2"+
		"\2\2\u03d1\u03d2\7I\2\2\u03d2\u03d4\5\u01d8\u00ed\2\u03d3\u03d1\3\2\2"+
		"\2\u03d3\u03d4\3\2\2\2\u03d4}\3\2\2\2\u03d5\u03d6\7\u008f\2\2\u03d6\u03d7"+
		"\5P)\2\u03d7\177\3\2\2\2\u03d8\u03db\7\u0096\2\2\u03d9\u03db\7_\2\2\u03da"+
		"\u03d8\3\2\2\2\u03da\u03d9\3\2\2\2\u03db\u03dc\3\2\2\2\u03dc\u03e1\5\u0082"+
		"B\2\u03dd\u03de\7 \2\2\u03de\u03e0\5\u0082B\2\u03df\u03dd\3\2\2\2\u03e0"+
		"\u03e3\3\2\2\2\u03e1\u03df\3\2\2\2\u03e1\u03e2\3\2\2\2\u03e2\u03e4\3\2"+
		"\2\2\u03e3\u03e1\3\2\2\2\u03e4\u03e5\7\u0090\2\2\u03e5\u03e6\5P)\2\u03e6"+
		"\u0081\3\2\2\2\u03e7\u03e8\7.\2\2\u03e8\u03ea\5\u010a\u0086\2\u03e9\u03eb"+
		"\5\u017a\u00be\2\u03ea\u03e9\3\2\2\2\u03ea\u03eb\3\2\2\2\u03eb\u03ec\3"+
		"\2\2\2\u03ec\u03ed\7m\2\2\u03ed\u03ee\5P)\2\u03ee\u0083\3\2\2\2\u03ef"+
		"\u03f0\7\u009b\2\2\u03f0\u03f1\7\27\2\2\u03f1\u03f2\5N(\2\u03f2\u03f4"+
		"\7\30\2\2\u03f3\u03f5\5\u0086D\2\u03f4\u03f3\3\2\2\2\u03f5\u03f6\3\2\2"+
		"\2\u03f6\u03f4\3\2\2\2\u03f6\u03f7\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8\u03f9"+
		"\7P\2\2\u03f9\u03fa\7\u008f\2\2\u03fa\u03fb\5P)\2\u03fb\u0085\3\2\2\2"+
		"\u03fc\u03fd\7D\2\2\u03fd\u03ff\5\u0088E\2\u03fe\u03fc\3\2\2\2\u03ff\u0400"+
		"\3\2\2\2\u0400\u03fe\3\2\2\2\u0400\u0401\3\2\2\2\u0401\u0402\3\2\2\2\u0402"+
		"\u0403\7\u008f\2\2\u0403\u0404\5P)\2\u0404\u0087\3\2\2\2\u0405\u0406\5"+
		"P)\2\u0406\u0089\3\2\2\2\u0407\u0408\7\u00a3\2\2\u0408\u0409\7\27\2\2"+
		"\u0409\u040a\5N(\2\u040a\u040c\7\30\2\2\u040b\u040d\5\u008cG\2\u040c\u040b"+
		"\3\2\2\2\u040d\u040e\3\2\2\2\u040e\u040c\3\2\2\2\u040e\u040f\3\2\2\2\u040f"+
		"\u0410\3\2\2\2\u0410\u0413\7P\2\2\u0411\u0412\7.\2\2\u0412\u0414\5\u010a"+
		"\u0086\2\u0413\u0411\3\2\2\2\u0413\u0414\3\2\2\2\u0414\u0415\3\2\2\2\u0415"+
		"\u0416\7\u008f\2\2\u0416\u0417\5P)\2\u0417\u008b\3\2\2\2\u0418\u041d\7"+
		"D\2\2\u0419\u041a\7.\2\2\u041a\u041b\5\u010a\u0086\2\u041b\u041c\7<\2"+
		"\2\u041c\u041e\3\2\2\2\u041d\u0419\3\2\2\2\u041d\u041e\3\2\2\2\u041e\u041f"+
		"\3\2\2\2\u041f\u0420\5\u008eH\2\u0420\u0421\7\u008f\2\2\u0421\u0422\5"+
		"P)\2\u0422\u008d\3\2\2\2\u0423\u0428\5\u017c\u00bf\2\u0424\u0425\7)\2"+
		"\2\u0425\u0427\5\u017c\u00bf\2\u0426\u0424\3\2\2\2\u0427\u042a\3\2\2\2"+
		"\u0428\u0426\3\2\2\2\u0428\u0429\3\2\2\2\u0429\u008f\3\2\2\2\u042a\u0428"+
		"\3\2\2\2\u042b\u042c\7k\2\2\u042c\u042d\7\27\2\2\u042d\u042e\5N(\2\u042e"+
		"\u042f\7\30\2\2\u042f\u0430\7\u009d\2\2\u0430\u0431\5P)\2\u0431\u0432"+
		"\7Y\2\2\u0432\u0433\5P)\2\u0433\u0091\3\2\2\2\u0434\u0436\5\u0094K\2\u0435"+
		"\u0437\5\u0098M\2\u0436\u0435\3\2\2\2\u0437\u0438\3\2\2\2\u0438\u0436"+
		"\3\2\2\2\u0438\u0439\3\2\2\2\u0439\u0093\3\2\2\2\u043a\u043b\7\u00a0\2"+
		"\2\u043b\u043c\5\u0096L\2\u043c\u0095\3\2\2\2\u043d\u043e\5\u009aN\2\u043e"+
		"\u0097\3\2\2\2\u043f\u0446\7G\2\2\u0440\u0447\5\u009cO\2\u0441\u0442\7"+
		"\27\2\2\u0442\u0443\7.\2\2\u0443\u0444\5\u010a\u0086\2\u0444\u0445\7\30"+
		"\2\2\u0445\u0447\3\2\2\2\u0446\u0440\3\2\2\2\u0446\u0441\3\2\2\2\u0447"+
		"\u0448\3\2\2\2\u0448\u0449\5\u009aN\2\u0449\u0099\3\2\2\2\u044a\u044c"+
		"\7\33\2\2\u044b\u044d\5N(\2\u044c\u044b\3\2\2\2\u044c\u044d\3\2\2\2\u044d"+
		"\u044e\3\2\2\2\u044e\u044f\7\34\2\2\u044f\u009b\3\2\2\2\u0450\u0455\5"+
		"\u00f0y\2\u0451\u0452\7)\2\2\u0452\u0454\5\u00f0y\2\u0453\u0451\3\2\2"+
		"\2\u0454\u0457\3\2\2\2\u0455\u0453\3\2\2\2\u0455\u0456\3\2\2\2\u0456\u009d"+
		"\3\2\2\2\u0457\u0455\3\2\2\2\u0458\u045e\7\u00a6\2\2\u0459\u045f\5\u00a0"+
		"Q\2\u045a\u045f\5\u00a2R\2\u045b\u045f\5\u00a4S\2\u045c\u045f\5\u00a6"+
		"T\2\u045d\u045f\5\u00a8U\2\u045e\u0459\3\2\2\2\u045e\u045a\3\2\2\2\u045e"+
		"\u045b\3\2\2\2\u045e\u045c\3\2\2\2\u045e\u045d\3\2\2\2\u045f\u009f\3\2"+
		"\2\2\u0460\u0461\7\u00b3\2\2\u0461\u0462\5N(\2\u0462\u0463\7\u00b4\2\2"+
		"\u0463\u0464\5P)\2\u0464\u00a1\3\2\2\2\u0465\u0466\7\u00b5\2\2\u0466\u0467"+
		"\5N(\2\u0467\u0468\7\u00b4\2\2\u0468\u0469\5P)\2\u0469\u00a3\3\2\2\2\u046a"+
		"\u046b\7\u00b6\2\2\u046b\u046c\5P)\2\u046c\u046d\t\b\2\2\u046d\u046e\5"+
		"P)\2\u046e\u00a5\3\2\2\2\u046f\u0470\7\u00b8\2\2\u0470\u0471\5P)\2\u0471"+
		"\u00a7\3\2\2\2\u0472\u0473\7\u00b9\2\2\u0473\u0474\5P)\2\u0474\u0475\7"+
		"<\2\2\u0475\u0476\5P)\2\u0476\u00a9\3\2\2\2\u0477\u047c\5\u00acW\2\u0478"+
		"\u0479\7\u0085\2\2\u0479\u047b\5\u00acW\2\u047a\u0478\3\2\2\2\u047b\u047e"+
		"\3\2\2\2\u047c\u047a\3\2\2\2\u047c\u047d\3\2\2\2\u047d\u00ab\3\2\2\2\u047e"+
		"\u047c\3\2\2\2\u047f\u0484\5\u00aeX\2\u0480\u0481\7:\2\2\u0481\u0483\5"+
		"\u00aeX\2\u0482\u0480\3\2\2\2\u0483\u0486\3\2\2\2\u0484\u0482\3\2\2\2"+
		"\u0484\u0485\3\2\2\2\u0485\u00ad\3\2\2\2\u0486\u0484\3\2\2\2\u0487\u048f"+
		"\5\u00b0Y\2\u0488\u048c\5\u00ceh\2\u0489\u048c\5\u00ccg\2\u048a\u048c"+
		"\5\u00d0i\2\u048b\u0488\3\2\2\2\u048b\u0489\3\2\2\2\u048b\u048a\3\2\2"+
		"\2\u048c\u048d\3\2\2\2\u048d\u048e\5\u00b0Y\2\u048e\u0490\3\2\2\2\u048f"+
		"\u048b\3\2\2\2\u048f\u0490\3\2\2\2\u0490\u00af\3\2\2\2\u0491\u0496\5\u00b2"+
		"Z\2\u0492\u0493\7\65\2\2\u0493\u0495\5\u00b2Z\2\u0494\u0492\3\2\2\2\u0495"+
		"\u0498\3\2\2\2\u0496\u0494\3\2\2\2\u0496\u0497\3\2\2\2\u0497\u00b1\3\2"+
		"\2\2\u0498\u0496\3\2\2\2\u0499\u049c\5\u00b4[\2\u049a\u049b\7\u009e\2"+
		"\2\u049b\u049d\5\u00b4[\2\u049c\u049a\3\2\2\2\u049c\u049d\3\2\2\2\u049d"+
		"\u00b3\3\2\2\2\u049e\u04a3\5\u00b6\\\2\u049f\u04a0\t\t\2\2\u04a0\u04a2"+
		"\5\u00b6\\\2\u04a1\u049f\3\2\2\2\u04a2\u04a5\3\2\2\2\u04a3\u04a1\3\2\2"+
		"\2\u04a3\u04a4\3\2\2\2\u04a4\u00b5\3\2\2\2\u04a5\u04a3\3\2\2\2\u04a6\u04ab"+
		"\5\u00b8]\2\u04a7\u04a8\t\n\2\2\u04a8\u04aa\5\u00b8]\2\u04a9\u04a7\3\2"+
		"\2\2\u04aa\u04ad\3\2\2\2\u04ab\u04a9\3\2\2\2\u04ab\u04ac\3\2\2\2\u04ac"+
		"\u00b7\3\2\2\2\u04ad\u04ab\3\2\2\2\u04ae\u04b3\5\u00ba^\2\u04af\u04b0"+
		"\t\13\2\2\u04b0\u04b2\5\u00ba^\2\u04b1\u04af\3\2\2\2\u04b2\u04b5\3\2\2"+
		"\2\u04b3\u04b1\3\2\2\2\u04b3\u04b4\3\2\2\2\u04b4\u00b9\3\2\2\2\u04b5\u04b3"+
		"\3\2\2\2\u04b6\u04bb\5\u00bc_\2\u04b7\u04b8\t\f\2\2\u04b8\u04ba\5\u00bc"+
		"_\2\u04b9\u04b7\3\2\2\2\u04ba\u04bd\3\2\2\2\u04bb\u04b9\3\2\2\2\u04bb"+
		"\u04bc\3\2\2\2\u04bc\u00bb\3\2\2\2\u04bd\u04bb\3\2\2\2\u04be\u04c2\5\u00be"+
		"`\2\u04bf\u04c0\7o\2\2\u04c0\u04c1\7\u0082\2\2\u04c1\u04c3\5\u017c\u00bf"+
		"\2\u04c2\u04bf\3\2\2\2\u04c2\u04c3\3\2\2\2\u04c3\u00bd\3\2\2\2\u04c4\u04c8"+
		"\5\u00c0a\2\u04c5\u04c6\7\u009f\2\2\u04c6\u04c7\7<\2\2\u04c7\u04c9\5\u017c"+
		"\u00bf\2\u04c8\u04c5\3\2\2\2\u04c8\u04c9\3\2\2\2\u04c9\u00bf\3\2\2\2\u04ca"+
		"\u04ce\5\u00c2b\2\u04cb\u04cc\7F\2\2\u04cc\u04cd\7<\2\2\u04cd\u04cf\5"+
		"\u0178\u00bd\2\u04ce\u04cb\3\2\2\2\u04ce\u04cf\3\2\2\2\u04cf\u00c1\3\2"+
		"\2\2\u04d0\u04d4\5\u00c4c\2\u04d1\u04d2\7E\2\2\u04d2\u04d3\7<\2\2\u04d3"+
		"\u04d5\5\u0178\u00bd\2\u04d4\u04d1\3\2\2\2\u04d4\u04d5\3\2\2\2\u04d5\u00c3"+
		"\3\2\2\2\u04d6\u04db\5\u00c8e\2\u04d7\u04d8\7\63\2\2\u04d8\u04da\5\u00c6"+
		"d\2\u04d9\u04d7\3\2\2\2\u04da\u04dd\3\2\2\2\u04db\u04d9\3\2\2\2\u04db"+
		"\u04dc\3\2\2\2\u04dc\u00c5\3\2\2\2\u04dd\u04db\3\2\2\2\u04de\u04df\5\u0100"+
		"\u0081\2\u04df\u04e0\5\u00f6|\2\u04e0\u00c7\3\2\2\2\u04e1\u04e3\t\t\2"+
		"\2\u04e2\u04e1\3\2\2\2\u04e3\u04e6\3\2\2\2\u04e4\u04e2\3\2\2\2\u04e4\u04e5"+
		"\3\2\2\2\u04e5\u04e7\3\2\2\2\u04e6\u04e4\3\2\2\2\u04e7\u04e8\5\u00caf"+
		"\2\u04e8\u00c9\3\2\2\2\u04e9\u04ed\5\u00d2j\2\u04ea\u04ed\5\u00d6l\2\u04eb"+
		"\u04ed\5\u00d8m\2\u04ec\u04e9\3\2\2\2\u04ec\u04ea\3\2\2\2\u04ec\u04eb"+
		"\3\2\2\2\u04ed\u00cb\3\2\2\2\u04ee\u04f7\7\25\2\2\u04ef\u04f7\7\26\2\2"+
		"\u04f0\u04f7\7*\2\2\u04f1\u04f2\7*\2\2\u04f2\u04f7\7\25\2\2\u04f3\u04f7"+
		"\7+\2\2\u04f4\u04f5\7+\2\2\u04f5\u04f7\7\25\2\2\u04f6\u04ee\3\2\2\2\u04f6"+
		"\u04ef\3\2\2\2\u04f6\u04f0\3\2\2\2\u04f6\u04f1\3\2\2\2\u04f6\u04f3\3\2"+
		"\2\2\u04f6\u04f4\3\2\2\2\u04f7\u00cd\3\2\2\2\u04f8\u04f9\t\r\2\2\u04f9"+
		"\u00cf\3\2\2\2\u04fa\u0500\7q\2\2\u04fb\u04fc\7*\2\2\u04fc\u0500\7*\2"+
		"\2\u04fd\u04fe\7+\2\2\u04fe\u0500\7+\2\2\u04ff\u04fa\3\2\2\2\u04ff\u04fb"+
		"\3\2\2\2\u04ff\u04fd\3\2\2\2\u0500\u00d1\3\2\2\2\u0501\u0505\7\u00a7\2"+
		"\2\u0502\u0506\5\u00d4k\2\u0503\u0504\t\16\2\2\u0504\u0506\5\u01a6\u00d4"+
		"\2\u0505\u0502\3\2\2\2\u0505\u0503\3\2\2\2\u0505\u0506\3\2\2\2\u0506\u0507"+
		"\3\2\2\2\u0507\u0508\5\u009aN\2\u0508\u00d3\3\2\2\2\u0509\u050a\t\17\2"+
		"\2\u050a\u00d5\3\2\2\2\u050b\u050d\7\23\2\2\u050c\u050b\3\2\2\2\u050d"+
		"\u050e\3\2\2\2\u050e\u050c\3\2\2\2\u050e\u050f\3\2\2\2\u050f\u0510\3\2"+
		"\2\2\u0510\u0511\7\33\2\2\u0511\u0512\5N(\2\u0512\u0513\7\34\2\2\u0513"+
		"\u00d7\3\2\2\2\u0514\u0519\5\u00dan\2\u0515\u0516\7\60\2\2\u0516\u0518"+
		"\5\u00dan\2\u0517\u0515\3\2\2\2\u0518\u051b\3\2\2\2\u0519\u0517\3\2\2"+
		"\2\u0519\u051a\3\2\2\2\u051a\u00d9\3\2\2\2\u051b\u0519\3\2\2\2\u051c\u051e"+
		"\7&\2\2\u051d\u051f\5\u00dco\2\u051e\u051d\3\2\2\2\u051e\u051f\3\2\2\2"+
		"\u051f\u0524\3\2\2\2\u0520\u0521\7\'\2\2\u0521\u0524\5\u00dco\2\u0522"+
		"\u0524\5\u00dco\2\u0523\u051c\3\2\2\2\u0523\u0520\3\2\2\2\u0523\u0522"+
		"\3\2\2\2\u0524\u00db\3\2\2\2\u0525\u052a\5\u00dep\2\u0526\u0527\t\20\2"+
		"\2\u0527\u0529\5\u00dep\2\u0528\u0526\3\2\2\2\u0529\u052c\3\2\2\2\u052a"+
		"\u0528\3\2\2\2\u052a\u052b\3\2\2\2\u052b\u00dd\3\2\2\2\u052c\u052a\3\2"+
		"\2\2\u052d\u0530\5\u00f4{\2\u052e\u0530\5\u00e0q\2\u052f\u052d\3\2\2\2"+
		"\u052f\u052e\3\2\2\2\u0530\u00df\3\2\2\2\u0531\u0534\5\u00e8u\2\u0532"+
		"\u0534\5\u00e2r\2\u0533\u0531\3\2\2\2\u0533\u0532\3\2\2\2\u0534\u0535"+
		"\3\2\2\2\u0535\u0536\5\u00f8}\2\u0536\u00e1\3\2\2\2\u0537\u0538\5\u00e4"+
		"s\2\u0538\u0539\5\u00eex\2\u0539\u053c\3\2\2\2\u053a\u053c\5\u00e6t\2"+
		"\u053b\u0537\3\2\2\2\u053b\u053a\3\2\2\2\u053c\u00e3\3\2\2\2\u053d\u053e"+
		"\t\21\2\2\u053e\u053f\7#\2\2\u053f\u0540\7#\2\2\u0540\u00e5\3\2\2\2\u0541"+
		"\u0543\7-\2\2\u0542\u0541\3\2\2\2\u0542\u0543\3\2\2\2\u0543\u0544\3\2"+
		"\2\2\u0544\u0545\5\u00eex\2\u0545\u00e7\3\2\2\2\u0546\u0547\5\u00eav\2"+
		"\u0547\u0548\5\u00eex\2\u0548\u054b\3\2\2\2\u0549\u054b\5\u00ecw\2\u054a"+
		"\u0546\3\2\2\2\u054a\u0549\3\2\2\2\u054b\u00e9\3\2\2\2\u054c\u054d\t\22"+
		"\2\2\u054d\u054e\7#\2\2\u054e\u054f\7#\2\2\u054f\u00eb\3\2\2\2\u0550\u0551"+
		"\7\"\2\2\u0551\u00ed\3\2\2\2\u0552\u0555\5\u00f0y\2\u0553\u0555\5\u0182"+
		"\u00c2\2\u0554\u0552\3\2\2\2\u0554\u0553\3\2\2\2\u0555\u00ef\3\2\2\2\u0556"+
		"\u0559\5\u01ca\u00e6\2\u0557\u0559\5\u00f2z\2\u0558\u0556\3\2\2\2\u0558"+
		"\u0557\3\2\2\2\u0559\u00f1\3\2\2\2\u055a\u055e\7\35\2\2\u055b\u055e\7"+
		"\u00bc\2\2\u055c\u055e\7\u00bd\2\2\u055d\u055a\3\2\2\2\u055d\u055b\3\2"+
		"\2\2\u055d\u055c\3\2\2\2\u055e\u00f3\3\2\2\2\u055f\u0565\5\u0102\u0082"+
		"\2\u0560\u0564\5\u00fa~\2\u0561\u0564\5\u00f6|\2\u0562\u0564\5\u00fc\177"+
		"\2\u0563\u0560\3\2\2\2\u0563\u0561\3\2\2\2\u0563\u0562\3\2\2\2\u0564\u0567"+
		"\3\2\2\2\u0565\u0563\3\2\2\2\u0565\u0566\3\2\2\2\u0566\u00f5\3\2\2\2\u0567"+
		"\u0565\3\2\2\2\u0568\u0571\7\27\2\2\u0569\u056e\5\u0116\u008c\2\u056a"+
		"\u056b\7 \2\2\u056b\u056d\5\u0116\u008c\2\u056c\u056a\3\2\2\2\u056d\u0570"+
		"\3\2\2\2\u056e\u056c\3\2\2\2\u056e\u056f\3\2\2\2\u056f\u0572\3\2\2\2\u0570"+
		"\u056e\3\2\2\2\u0571\u0569\3\2\2\2\u0571\u0572\3\2\2\2\u0572\u0573\3\2"+
		"\2\2\u0573\u0574\7\30\2\2\u0574\u00f7\3\2\2\2\u0575\u0577\5\u00fa~\2\u0576"+
		"\u0575\3\2\2\2\u0577\u057a\3\2\2\2\u0578\u0576\3\2\2\2\u0578\u0579\3\2"+
		"\2\2\u0579\u00f9\3\2\2\2\u057a\u0578\3\2\2\2\u057b\u057c\7\31\2\2\u057c"+
		"\u057d\5N(\2\u057d\u057e\7\32\2\2\u057e\u00fb\3\2\2\2\u057f\u0580\7,\2"+
		"\2\u0580\u0581\5\u00fe\u0080\2\u0581\u00fd\3\2\2\2\u0582\u0587\5\u01ce"+
		"\u00e8\2\u0583\u0587\7\7\2\2\u0584\u0587\5\u010c\u0087\2\u0585\u0587\7"+
		"\35\2\2\u0586\u0582\3\2\2\2\u0586\u0583\3\2\2\2\u0586\u0584\3\2\2\2\u0586"+
		"\u0585\3\2\2\2\u0587\u00ff\3\2\2\2\u0588\u058c\5\u01ca\u00e6\2\u0589\u058c"+
		"\5\u0108\u0085\2\u058a\u058c\5\u010c\u0087\2\u058b\u0588\3\2\2\2\u058b"+
		"\u0589\3\2\2\2\u058b\u058a\3\2\2\2\u058c\u0101\3\2\2\2\u058d\u059b\5\u0104"+
		"\u0083\2\u058e\u059b\5\u0108\u0085\2\u058f\u059b\5\u010c\u0087\2\u0590"+
		"\u059b\5\u010e\u0088\2\u0591\u059b\5\u0114\u008b\2\u0592\u059b\5\u0110"+
		"\u0089\2\u0593\u059b\5\u0112\u008a\2\u0594\u059b\5\u0118\u008d\2\u0595"+
		"\u059b\5\u0156\u00ac\2\u0596\u059b\5\u015e\u00b0\2\u0597\u059b\5\u0162"+
		"\u00b2\2\u0598\u059b\5\u0168\u00b5\2\u0599\u059b\5\u0176\u00bc\2\u059a"+
		"\u058d\3\2\2\2\u059a\u058e\3\2\2\2\u059a\u058f\3\2\2\2\u059a\u0590\3\2"+
		"\2\2\u059a\u0591\3\2\2\2\u059a\u0592\3\2\2\2\u059a\u0593\3\2\2\2\u059a"+
		"\u0594\3\2\2\2\u059a\u0595\3\2\2\2\u059a\u0596\3\2\2\2\u059a\u0597\3\2"+
		"\2\2\u059a\u0598\3\2\2\2\u059a\u0599\3\2\2\2\u059b\u0103\3\2\2\2\u059c"+
		"\u059f\5\u0106\u0084\2\u059d\u059f\5\u01de\u00f0\2\u059e\u059c\3\2\2\2"+
		"\u059e\u059d\3\2\2\2\u059f\u0105\3\2\2\2\u05a0\u05a1\t\23\2\2\u05a1\u0107"+
		"\3\2\2\2\u05a2\u05a3\7.\2\2\u05a3\u05a4\5\u01ca\u00e6\2\u05a4\u0109\3"+
		"\2\2\2\u05a5\u05a6\5\u01ca\u00e6\2\u05a6\u010b\3\2\2\2\u05a7\u05a9\7\27"+
		"\2\2\u05a8\u05aa\5N(\2\u05a9\u05a8\3\2\2\2\u05a9\u05aa\3\2\2\2\u05aa\u05ab"+
		"\3\2\2\2\u05ab\u05ac\7\30\2\2\u05ac\u010d\3\2\2\2\u05ad\u05ae\7!\2\2\u05ae"+
		"\u010f\3\2\2\2\u05af\u05b0\7\u0087\2\2\u05b0\u05b1\5\u009aN\2\u05b1\u0111"+
		"\3\2\2\2\u05b2\u05b3\7\u00a5\2\2\u05b3\u05b4\5\u009aN\2\u05b4\u0113\3"+
		"\2\2\2\u05b5\u05b6\5\u01ca\u00e6\2\u05b6\u05b7\5\u00f6|\2\u05b7\u0115"+
		"\3\2\2\2\u05b8\u05bb\5P)\2\u05b9\u05bb\7,\2\2\u05ba\u05b8\3\2\2\2\u05ba"+
		"\u05b9\3\2\2\2\u05bb\u0117\3\2\2\2\u05bc\u05bf\5\u011a\u008e\2\u05bd\u05bf"+
		"\5\u0130\u0099\2\u05be\u05bc\3\2\2\2\u05be\u05bd\3\2\2\2\u05bf\u0119\3"+
		"\2\2\2\u05c0\u05c4\5\u011c\u008f\2\u05c1\u05c4\5\u011e\u0090\2\u05c2\u05c4"+
		"\t\24\2\2\u05c3\u05c0\3\2\2\2\u05c3\u05c1\3\2\2\2\u05c3\u05c2\3\2\2\2"+
		"\u05c4\u011b\3\2\2\2\u05c5\u05c6\7*\2\2\u05c6\u05c7\5\u01cc\u00e7\2\u05c7"+
		"\u05c8\5\u0120\u0091\2\u05c8\u05cc\7+\2\2\u05c9\u05cb\5\u012c\u0097\2"+
		"\u05ca\u05c9\3\2\2\2\u05cb\u05ce\3\2\2\2\u05cc\u05ca\3\2\2\2\u05cc\u05cd"+
		"\3\2\2\2\u05cd\u05cf\3\2\2\2\u05ce\u05cc\3\2\2\2\u05cf\u05d0\7*\2\2\u05d0"+
		"\u05d1\7&\2\2\u05d1\u05d2\5\u01cc\u00e7\2\u05d2\u05d3\7+\2\2\u05d3\u011d"+
		"\3\2\2\2\u05d4\u05d5\7*\2\2\u05d5\u05d6\5\u01cc\u00e7\2\u05d6\u05d7\5"+
		"\u0120\u0091\2\u05d7\u05d8\7&\2\2\u05d8\u05d9\7+\2\2\u05d9\u011f\3\2\2"+
		"\2\u05da\u05db\5\u01cc\u00e7\2\u05db\u05dc\7\25\2\2\u05dc\u05dd\5\u0126"+
		"\u0094\2\u05dd\u05df\3\2\2\2\u05de\u05da\3\2\2\2\u05df\u05e2\3\2\2\2\u05e0"+
		"\u05de\3\2\2\2\u05e0\u05e1\3\2\2\2\u05e1\u0121\3\2\2\2\u05e2\u05e0\3\2"+
		"\2\2\u05e3\u05ea\7\r\2\2\u05e4\u05e9\7\13\2\2\u05e5\u05e9\7\f\2\2\u05e6"+
		"\u05e9\7\3\2\2\u05e7\u05e9\5\u0128\u0095\2\u05e8\u05e4\3\2\2\2\u05e8\u05e5"+
		"\3\2\2\2\u05e8\u05e6\3\2\2\2\u05e8\u05e7\3\2\2\2\u05e9\u05ec\3\2\2\2\u05ea"+
		"\u05e8\3\2\2\2\u05ea\u05eb\3\2\2\2\u05eb\u05ed\3\2\2\2\u05ec\u05ea\3\2"+
		"\2\2\u05ed\u05ee\7\r\2\2\u05ee\u0123\3\2\2\2\u05ef\u05f6\7\16\2\2\u05f0"+
		"\u05f5\7\13\2\2\u05f1\u05f5\7\f\2\2\u05f2\u05f5\7\4\2\2\u05f3\u05f5\5"+
		"\u012a\u0096\2\u05f4\u05f0\3\2\2\2\u05f4\u05f1\3\2\2\2\u05f4\u05f2\3\2"+
		"\2\2\u05f4\u05f3\3\2\2\2\u05f5\u05f8\3\2\2\2\u05f6\u05f4\3\2\2\2\u05f6"+
		"\u05f7\3\2\2\2\u05f7\u05f9\3\2\2\2\u05f8\u05f6\3\2\2\2\u05f9\u05fa\7\16"+
		"\2\2\u05fa\u0125\3\2\2\2\u05fb\u05fe\5\u0122\u0092\2\u05fc\u05fe\5\u0124"+
		"\u0093\2\u05fd\u05fb\3\2\2\2\u05fd\u05fc\3\2\2\2\u05fe\u0127\3\2\2\2\u05ff"+
		"\u0601\7\u00c6\2\2\u0600\u05ff\3\2\2\2\u0601\u0602\3\2\2\2\u0602\u0600"+
		"\3\2\2\2\u0602\u0603\3\2\2\2\u0603\u060d\3\2\2\2\u0604\u060d\7\5\2\2\u0605"+
		"\u060d\7\6\2\2\u0606\u060d\5\u0122\u0092\2\u0607\u0609\7\33\2\2\u0608"+
		"\u060a\5N(\2\u0609\u0608\3\2\2\2\u0609\u060a\3\2\2\2\u060a\u060b\3\2\2"+
		"\2\u060b\u060d\7\34\2\2\u060c\u0600\3\2\2\2\u060c\u0604\3\2\2\2\u060c"+
		"\u0605\3\2\2\2\u060c\u0606\3\2\2\2\u060c\u0607\3\2\2\2\u060d\u0129\3\2"+
		"\2\2\u060e\u0610\7\u00c6\2\2\u060f\u060e\3\2\2\2\u0610\u0611\3\2\2\2\u0611"+
		"\u060f\3\2\2\2\u0611\u0612\3\2\2\2\u0612\u061c\3\2\2\2\u0613\u061c\7\5"+
		"\2\2\u0614\u061c\7\6\2\2\u0615\u061c\5\u0124\u0093\2\u0616\u0618\7\33"+
		"\2\2\u0617\u0619\5N(\2\u0618\u0617\3\2\2\2\u0618\u0619\3\2\2\2\u0619\u061a"+
		"\3\2\2\2\u061a\u061c\7\34\2\2\u061b\u060f\3\2\2\2\u061b\u0613\3\2\2\2"+
		"\u061b\u0614\3\2\2\2\u061b\u0615\3\2\2\2\u061b\u0616\3\2\2\2\u061c\u012b"+
		"\3\2\2\2\u061d\u0624\5\u011a\u008e\2\u061e\u0624\5\u012e\u0098\2\u061f"+
		"\u0624\7\22\2\2\u0620\u0624\7\r\2\2\u0621\u0624\7\16\2\2\u0622\u0624\5"+
		"\u01e4\u00f3\2\u0623\u061d\3\2\2\2\u0623\u061e\3\2\2\2\u0623\u061f\3\2"+
		"\2\2\u0623\u0620\3\2\2\2\u0623\u0621\3\2\2\2\u0623\u0622\3\2\2\2\u0624"+
		"\u012d\3\2\2\2\u0625\u062f\t\25\2\2\u0626\u0627\7\33\2\2\u0627\u062f\7"+
		"\33\2\2\u0628\u0629\7\34\2\2\u0629\u062f\7\34\2\2\u062a\u062b\7\33\2\2"+
		"\u062b\u062c\5N(\2\u062c\u062d\7\34\2\2\u062d\u062f\3\2\2\2\u062e\u0625"+
		"\3\2\2\2\u062e\u0626\3\2\2\2\u062e\u0628\3\2\2\2\u062e\u062a\3\2\2\2\u062f"+
		"\u012f\3\2\2\2\u0630\u0639\5\u0140\u00a1\2\u0631\u0639\5\u0142\u00a2\2"+
		"\u0632\u0639\5\u0146\u00a4\2\u0633\u0639\5\u0148\u00a5\2\u0634\u0639\5"+
		"\u0150\u00a9\2\u0635\u0639\5\u0152\u00aa\2\u0636\u0639\5\u0154\u00ab\2"+
		"\u0637\u0639\5\u0132\u009a\2\u0638\u0630\3\2\2\2\u0638\u0631\3\2\2\2\u0638"+
		"\u0632\3\2\2\2\u0638\u0633\3\2\2\2\u0638\u0634\3\2\2\2\u0638\u0635\3\2"+
		"\2\2\u0638\u0636\3\2\2\2\u0638\u0637\3\2\2\2\u0639\u0131\3\2\2\2\u063a"+
		"\u0641\5\u0134\u009b\2\u063b\u0641\5\u0136\u009c\2\u063c\u0641\5\u0138"+
		"\u009d\2\u063d\u0641\5\u013a\u009e\2\u063e\u0641\5\u013c\u009f\2\u063f"+
		"\u0641\5\u013e\u00a0\2\u0640\u063a\3\2\2\2\u0640\u063b\3\2\2\2\u0640\u063c"+
		"\3\2\2\2\u0640\u063d\3\2\2\2\u0640\u063e\3\2\2\2\u0640\u063f\3\2\2\2\u0641"+
		"\u0133\3\2\2\2\u0642\u0643\7\u00ae\2\2\u0643\u0644\5\u0144\u00a3\2\u0644"+
		"\u0135\3\2\2\2\u0645\u0646\7\u00b2\2\2\u0646\u0654\7\33\2\2\u0647\u0648"+
		"\5P)\2\u0648\u0649\7#\2\2\u0649\u0651\5P)\2\u064a\u064b\7 \2\2\u064b\u064c"+
		"\5P)\2\u064c\u064d\7#\2\2\u064d\u064e\5P)\2\u064e\u0650\3\2\2\2\u064f"+
		"\u064a\3\2\2\2\u0650\u0653\3\2\2\2\u0651\u064f\3\2\2\2\u0651\u0652\3\2"+
		"\2\2\u0652\u0655\3\2\2\2\u0653\u0651\3\2\2\2\u0654\u0647\3\2\2\2\u0654"+
		"\u0655\3\2\2\2\u0655\u0656\3\2\2\2\u0656\u0657\7\34\2\2\u0657\u0137\3"+
		"\2\2\2\u0658\u0659\7\u00b1\2\2\u0659\u065a\5\u0144\u00a3\2\u065a\u0139"+
		"\3\2\2\2\u065b\u065c\7\u00af\2\2\u065c\u065d\7\33\2\2\u065d\u065e\5P)"+
		"\2\u065e\u065f\7\34\2\2\u065f\u013b\3\2\2\2\u0660\u0661\7\u00b0\2\2\u0661"+
		"\u0662\7\33\2\2\u0662\u0663\7\34\2\2\u0663\u013d\3\2\2\2\u0664\u0665\7"+
		"B\2\2\u0665\u0666\5\u0144\u00a3\2\u0666\u013f\3\2\2\2\u0667\u0668\7V\2"+
		"\2\u0668\u0669\5\u009aN\2\u0669\u0141\3\2\2\2\u066a\u0670\7X\2\2\u066b"+
		"\u0671\5\u01ca\u00e6\2\u066c\u066d\7\33\2\2\u066d\u066e\5N(\2\u066e\u066f"+
		"\7\34\2\2\u066f\u0671\3\2\2\2\u0670\u066b\3\2\2\2\u0670\u066c\3\2\2\2"+
		"\u0671\u0672\3\2\2\2\u0672\u0673\5\u0144\u00a3\2\u0673\u0143\3\2\2\2\u0674"+
		"\u0675\5\u009aN\2\u0675\u0145\3\2\2\2\u0676\u067c\7?\2\2\u0677\u067d\5"+
		"\u01ca\u00e6\2\u0678\u0679\7\33\2\2\u0679\u067a\5N(\2\u067a\u067b\7\34"+
		"\2\2\u067b\u067d\3\2\2\2\u067c\u0677\3\2\2\2\u067c\u0678\3\2\2\2\u067d"+
		"\u067e\3\2\2\2\u067e\u067f\5\u009aN\2\u067f\u0147\3\2\2\2\u0680\u0683"+
		"\7{\2\2\u0681\u0684\5\u014a\u00a6\2\u0682\u0684\5\u014c\u00a7\2\u0683"+
		"\u0681\3\2\2\2\u0683\u0682\3\2\2\2\u0684\u0685\3\2\2\2\u0685\u0686\5\u014e"+
		"\u00a8\2\u0686\u0149\3\2\2\2\u0687\u0688\5\u01ce\u00e8\2\u0688\u014b\3"+
		"\2\2\2\u0689\u068a\5\u009aN\2\u068a\u014d\3\2\2\2\u068b\u068c\5\u009a"+
		"N\2\u068c\u014f\3\2\2\2\u068d\u068e\7\u009c\2\2\u068e\u068f\5\u009aN\2"+
		"\u068f\u0151\3\2\2\2\u0690\u0691\7J\2\2\u0691\u0692\5\u009aN\2\u0692\u0153"+
		"\3\2\2\2\u0693\u0699\7\u008e\2\2\u0694\u069a\5\u01ce\u00e8\2\u0695\u0696"+
		"\7\33\2\2\u0696\u0697\5N(\2\u0697\u0698\7\34\2\2\u0698\u069a\3\2\2\2\u0699"+
		"\u0694\3\2\2\2\u0699\u0695\3\2\2\2\u069a\u069b\3\2\2\2\u069b\u069c\5\u009a"+
		"N\2\u069c\u0155\3\2\2\2\u069d\u06a0\5\u0158\u00ad\2\u069e\u06a0\5\u015a"+
		"\u00ae\2\u069f\u069d\3\2\2\2\u069f\u069e\3\2\2\2\u06a0\u0157\3\2\2\2\u06a1"+
		"\u06a2\5\u01ca\u00e6\2\u06a2\u06a3\7\61\2\2\u06a3\u06a4\7\7\2\2\u06a4"+
		"\u0159\3\2\2\2\u06a5\u06a6\5B\"\2\u06a6\u06a7\7e\2\2\u06a7\u06a9\7\27"+
		"\2\2\u06a8\u06aa\5> \2\u06a9\u06a8\3\2\2\2\u06a9\u06aa\3\2\2\2\u06aa\u06ab"+
		"\3\2\2\2\u06ab\u06ae\7\30\2\2\u06ac\u06ad\7<\2\2\u06ad\u06af\5\u017c\u00bf"+
		"\2\u06ae\u06ac\3\2\2\2\u06ae\u06af\3\2\2\2\u06af\u06b0\3\2\2\2\u06b0\u06b1"+
		"\5\u015c\u00af\2\u06b1\u015b\3\2\2\2\u06b2\u06b3\5\u009aN\2\u06b3\u015d"+
		"\3\2\2\2\u06b4\u06b5\7x\2\2\u06b5\u06be\7\33\2\2\u06b6\u06bb\5\u0160\u00b1"+
		"\2\u06b7\u06b8\7 \2\2\u06b8\u06ba\5\u0160\u00b1\2\u06b9\u06b7\3\2\2\2"+
		"\u06ba\u06bd\3\2\2\2\u06bb\u06b9\3\2\2\2\u06bb\u06bc\3\2\2\2\u06bc\u06bf"+
		"\3\2\2\2\u06bd\u06bb\3\2\2\2\u06be\u06b6\3\2\2\2\u06be\u06bf\3\2\2\2\u06bf"+
		"\u06c0\3\2\2\2\u06c0\u06c1\7\34\2\2\u06c1\u015f\3\2\2\2\u06c2\u06c3\5"+
		"P)\2\u06c3\u06c4\t\26\2\2\u06c4\u06c5\5P)\2\u06c5\u0161\3\2\2\2\u06c6"+
		"\u06c9\5\u0164\u00b3\2\u06c7\u06c9\5\u0166\u00b4\2\u06c8\u06c6\3\2\2\2"+
		"\u06c8\u06c7\3\2\2\2\u06c9\u0163\3\2\2\2\u06ca\u06cc\7\31\2\2\u06cb\u06cd"+
		"\5N(\2\u06cc\u06cb\3\2\2\2\u06cc\u06cd\3\2\2\2\u06cd\u06ce\3\2\2\2\u06ce"+
		"\u06cf\7\32\2\2\u06cf\u0165\3\2\2\2\u06d0\u06d1\7;\2\2\u06d1\u06d2\5\u009a"+
		"N\2\u06d2\u0167\3\2\2\2\u06d3\u06d4\7\u00c4\2\2\u06d4\u06d5\5\u016a\u00b6"+
		"\2\u06d5\u06d6\7\u00c9\2\2\u06d6\u0169\3\2\2\2\u06d7\u06dd\5\u0172\u00ba"+
		"\2\u06d8\u06d9\5\u0174\u00bb\2\u06d9\u06da\5\u0172\u00ba\2\u06da\u06dc"+
		"\3\2\2\2\u06db\u06d8\3\2\2\2\u06dc\u06df\3\2\2\2\u06dd\u06db\3\2\2\2\u06dd"+
		"\u06de\3\2\2\2\u06de\u016b\3\2\2\2\u06df\u06dd\3\2\2\2\u06e0\u06e1\t\27"+
		"\2\2\u06e1\u016d\3\2\2\2\u06e2\u06e3\t\30\2\2\u06e3\u016f\3\2\2\2\u06e4"+
		"\u06e5\t\31\2\2\u06e5\u0171\3\2\2\2\u06e6\u06f1\7\u00c7\2\2\u06e7\u06e8"+
		"\5\u016c\u00b7\2\u06e8\u06e9\5\u016e\u00b8\2\u06e9\u06f1\3\2\2\2\u06ea"+
		"\u06eb\5\u0170\u00b9\2\u06eb\u06ec\5\u016c\u00b7\2\u06ec\u06ed\5\u016c"+
		"\u00b7\2\u06ed\u06f1\3\2\2\2\u06ee\u06f1\5\u016c\u00b7\2\u06ef\u06f1\7"+
		"\33\2\2\u06f0\u06e6\3\2\2\2\u06f0\u06e7\3\2\2\2\u06f0\u06ea\3\2\2\2\u06f0"+
		"\u06ee\3\2\2\2\u06f0\u06ef\3\2\2\2\u06f1\u06f4\3\2\2\2\u06f2\u06f0\3\2"+
		"\2\2\u06f2\u06f3\3\2\2\2\u06f3\u0173\3\2\2\2\u06f4\u06f2\3\2\2\2\u06f5"+
		"\u06f6\7\u00c8\2\2\u06f6\u06f7\5N(\2\u06f7\u06f8\7\u00c5\2\2\u06f8\u0175"+
		"\3\2\2\2\u06f9\u06fa\7,\2\2\u06fa\u06fb\5\u00fe\u0080\2\u06fb\u0177\3"+
		"\2\2\2\u06fc\u06fe\5\u01a4\u00d3\2\u06fd\u06ff\7,\2\2\u06fe\u06fd\3\2"+
		"\2\2\u06fe\u06ff\3\2\2\2\u06ff\u0179\3\2\2\2\u0700\u0701\7<\2\2\u0701"+
		"\u0702\5\u017c\u00bf\2\u0702\u017b\3\2\2\2\u0703\u0704\7[\2\2\u0704\u0705"+
		"\7\27\2\2\u0705\u070d\7\30\2\2\u0706\u070a\5\u017e\u00c0\2\u0707\u070b"+
		"\7,\2\2\u0708\u070b\7\35\2\2\u0709\u070b\7\36\2\2\u070a\u0707\3\2\2\2"+
		"\u070a\u0708\3\2\2\2\u070a\u0709\3\2\2\2\u070a\u070b\3\2\2\2\u070b\u070d"+
		"\3\2\2\2\u070c\u0703\3\2\2\2\u070c\u0706\3\2\2\2\u070d\u017d\3\2\2\2\u070e"+
		"\u0718\5\u0182\u00c2\2\u070f\u0710\7r\2\2\u0710\u0711\7\27\2\2\u0711\u0718"+
		"\7\30\2\2\u0712\u0718\5\u01a8\u00d5\2\u0713\u0718\5\u01ae\u00d8\2\u0714"+
		"\u0718\5\u01b4\u00db\2\u0715\u0718\5\u0180\u00c1\2\u0716\u0718\5\u01ba"+
		"\u00de\2\u0717\u070e\3\2\2\2\u0717\u070f\3\2\2\2\u0717\u0712\3\2\2\2\u0717"+
		"\u0713\3\2\2\2\u0717\u0714\3\2\2\2\u0717\u0715\3\2\2\2\u0717\u0716\3\2"+
		"\2\2\u0718\u017f\3\2\2\2\u0719\u071a\5\u01ca\u00e6\2\u071a\u0181\3\2\2"+
		"\2\u071b\u0728\5\u0188\u00c5\2\u071c\u0728\5\u0198\u00cd\2\u071d\u0728"+
		"\5\u0192\u00ca\2\u071e\u0728\5\u019c\u00cf\2\u071f\u0728\5\u0196\u00cc"+
		"\2\u0720\u0728\5\u0190\u00c9\2\u0721\u0728\5\u018c\u00c7\2\u0722\u0728"+
		"\5\u018a\u00c6\2\u0723\u0728\5\u018e\u00c8\2\u0724\u0728\5\u01be\u00e0"+
		"\2\u0725\u0728\5\u0186\u00c4\2\u0726\u0728\5\u0184\u00c3\2\u0727\u071b"+
		"\3\2\2\2\u0727\u071c\3\2\2\2\u0727\u071d\3\2\2\2\u0727\u071e\3\2\2\2\u0727"+
		"\u071f\3\2\2\2\u0727\u0720\3\2\2\2\u0727\u0721\3\2\2\2\u0727\u0722\3\2"+
		"\2\2\u0727\u0723\3\2\2\2\u0727\u0724\3\2\2\2\u0727\u0725\3\2\2\2\u0727"+
		"\u0726\3\2\2\2\u0728\u0183\3\2\2\2\u0729\u072a\7\u0081\2\2\u072a\u072c"+
		"\7\27\2\2\u072b\u072d\7\35\2\2\u072c\u072b\3\2\2\2\u072c\u072d\3\2\2\2"+
		"\u072d\u072e\3\2\2\2\u072e\u072f\7\30\2\2\u072f\u0185\3\2\2\2\u0730\u0731"+
		"\7B\2\2\u0731\u0732\7\27\2\2\u0732\u0733\7\30\2\2\u0733\u0187\3\2\2\2"+
		"\u0734\u0735\7W\2\2\u0735\u0738\7\27\2\2\u0736\u0739\5\u0198\u00cd\2\u0737"+
		"\u0739\5\u019c\u00cf\2\u0738\u0736\3\2\2\2\u0738\u0737\3\2\2\2\u0738\u0739"+
		"\3\2\2\2\u0739\u073a\3\2\2\2\u073a\u073b\7\30\2\2\u073b\u0189\3\2\2\2"+
		"\u073c\u073d\7\u009c\2\2\u073d\u073e\7\27\2\2\u073e\u073f\7\30\2\2\u073f"+
		"\u018b\3\2\2\2\u0740\u0741\7J\2\2\u0741\u0742\7\27\2\2\u0742\u0743\7\30"+
		"\2\2\u0743\u018d\3\2\2\2\u0744\u0745\7~\2\2\u0745\u0746\7\27\2\2\u0746"+
		"\u0747\7\30\2\2\u0747\u018f\3\2\2\2\u0748\u0749\7\u008e\2\2\u0749\u074c"+
		"\7\27\2\2\u074a\u074d\5\u01ce\u00e8\2\u074b\u074d\5\u01de\u00f0\2\u074c"+
		"\u074a\3\2\2\2\u074c\u074b\3\2\2\2\u074c\u074d\3\2\2\2\u074d\u074e\3\2"+
		"\2\2\u074e\u074f\7\30\2\2\u074f\u0191\3\2\2\2\u0750\u0751\7?\2\2\u0751"+
		"\u0757\7\27\2\2\u0752\u0755\5\u0194\u00cb\2\u0753\u0754\7 \2\2\u0754\u0756"+
		"\5\u01a6\u00d4\2\u0755\u0753\3\2\2\2\u0755\u0756\3\2\2\2\u0756\u0758\3"+
		"\2\2\2\u0757\u0752\3\2\2\2\u0757\u0758\3\2\2\2\u0758\u0759\3\2\2\2\u0759"+
		"\u075a\7\30\2\2\u075a\u0193\3\2\2\2\u075b\u075e\5\u01a0\u00d1\2\u075c"+
		"\u075e\7\35\2\2\u075d\u075b\3\2\2\2\u075d\u075c\3\2\2\2\u075e\u0195\3"+
		"\2\2\2\u075f\u0760\7\u0092\2\2\u0760\u0761\7\27\2\2\u0761\u0762\5\u01bc"+
		"\u00df\2\u0762\u0763\7\30\2\2\u0763\u0197\3\2\2\2\u0764\u0765\7X\2\2\u0765"+
		"\u076e\7\27\2\2\u0766\u076c\5\u019a\u00ce\2\u0767\u0768\7 \2\2\u0768\u076a"+
		"\5\u01a6\u00d4\2\u0769\u076b\7,\2\2\u076a\u0769\3\2\2\2\u076a\u076b\3"+
		"\2\2\2\u076b\u076d\3\2\2\2\u076c\u0767\3\2\2\2\u076c\u076d\3\2\2\2\u076d"+
		"\u076f\3\2\2\2\u076e\u0766\3\2\2\2\u076e\u076f\3\2\2\2\u076f\u0770\3\2"+
		"\2\2\u0770\u0771\7\30\2\2\u0771\u0199\3\2\2\2\u0772\u0775\5\u01a2\u00d2"+
		"\2\u0773\u0775\7\35\2\2\u0774\u0772\3\2\2\2\u0774\u0773\3\2\2\2\u0775"+
		"\u019b\3\2\2\2\u0776\u0777\7\u0093\2\2\u0777\u0778\7\27\2\2\u0778\u0779"+
		"\5\u019e\u00d0\2\u0779\u077a\7\30\2\2\u077a\u019d\3\2\2\2\u077b\u077c"+
		"\5\u01a2\u00d2\2\u077c\u019f\3\2\2\2\u077d\u077e\5\u01ca\u00e6\2\u077e"+
		"\u01a1\3\2\2\2\u077f\u0780\5\u01ca\u00e6\2\u0780\u01a3\3\2\2\2\u0781\u0782"+
		"\5\u01a6\u00d4\2\u0782\u01a5\3\2\2\2\u0783\u0784\5\u01ca\u00e6\2\u0784"+
		"\u01a7\3\2\2\2\u0785\u0787\5D#\2\u0786\u0785\3\2\2\2\u0787\u078a\3\2\2"+
		"\2\u0788\u0786\3\2\2\2\u0788\u0789\3\2\2\2\u0789\u078d\3\2\2\2\u078a\u0788"+
		"\3\2\2\2\u078b\u078e\5\u01aa\u00d6\2\u078c\u078e\5\u01ac\u00d7\2\u078d"+
		"\u078b\3\2\2\2\u078d\u078c\3\2\2\2\u078e\u01a9\3\2\2\2\u078f\u0790\7e"+
		"\2\2\u0790\u0791\7\27\2\2\u0791\u0792\7\35\2\2\u0792\u0793\7\30\2\2\u0793"+
		"\u01ab\3\2\2\2\u0794\u0795\7e\2\2\u0795\u079e\7\27\2\2\u0796\u079b\5\u017c"+
		"\u00bf\2\u0797\u0798\7 \2\2\u0798\u079a\5\u017c\u00bf\2\u0799\u0797\3"+
		"\2\2\2\u079a\u079d\3\2\2\2\u079b\u0799\3\2\2\2\u079b\u079c\3\2\2\2\u079c"+
		"\u079f\3\2\2\2\u079d\u079b\3\2\2\2\u079e\u0796\3\2\2\2\u079e\u079f\3\2"+
		"\2\2\u079f\u07a0\3\2\2\2\u07a0\u07a1\7\30\2\2\u07a1\u07a2\7<\2\2\u07a2"+
		"\u07a3\5\u017c\u00bf\2\u07a3\u01ad\3\2\2\2\u07a4\u07a7\5\u01b0\u00d9\2"+
		"\u07a5\u07a7\5\u01b2\u00da\2\u07a6\u07a4\3\2\2\2\u07a6\u07a5\3\2\2\2\u07a7"+
		"\u01af\3\2\2\2\u07a8\u07a9\7x\2\2\u07a9\u07aa\7\27\2\2\u07aa\u07ab\7\35"+
		"\2\2\u07ab\u07ac\7\30\2\2\u07ac\u01b1\3\2\2\2\u07ad\u07ae\7x\2\2\u07ae"+
		"\u07af\7\27\2\2\u07af\u07b0\5\u01ca\u00e6\2\u07b0\u07b1\7 \2\2\u07b1\u07b2"+
		"\5\u017c\u00bf\2\u07b2\u07b3\7\30\2\2\u07b3\u01b3\3\2\2\2\u07b4\u07b7"+
		"\5\u01b6\u00dc\2\u07b5\u07b7\5\u01b8\u00dd\2\u07b6\u07b4\3\2\2\2\u07b6"+
		"\u07b5\3\2\2\2\u07b7\u01b5\3\2\2\2\u07b8\u07b9\7;\2\2\u07b9\u07ba\7\27"+
		"\2\2\u07ba\u07bb\7\35\2\2\u07bb\u07bc\7\30\2\2\u07bc\u01b7\3\2\2\2\u07bd"+
		"\u07be\7;\2\2\u07be\u07bf\7\27\2\2\u07bf\u07c0\5\u017c\u00bf\2\u07c0\u07c1"+
		"\7\30\2\2\u07c1\u01b9\3\2\2\2\u07c2\u07c3\7\27\2\2\u07c3\u07c4\5\u017e"+
		"\u00c0\2\u07c4\u07c5\7\30\2\2\u07c5\u01bb\3\2\2\2\u07c6\u07c7\5\u01a0"+
		"\u00d1\2\u07c7\u01bd\3\2\2\2\u07c8\u07ce\5\u01c0\u00e1\2\u07c9\u07ce\5"+
		"\u01c2\u00e2\2\u07ca\u07ce\5\u01c4\u00e3\2\u07cb\u07ce\5\u01c6\u00e4\2"+
		"\u07cc\u07ce\5\u01c8\u00e5\2\u07cd\u07c8\3\2\2\2\u07cd\u07c9\3\2\2\2\u07cd"+
		"\u07ca\3\2\2\2\u07cd\u07cb\3\2\2\2\u07cd\u07cc\3\2\2\2\u07ce\u01bf\3\2"+
		"\2\2\u07cf\u07d0\7\u00ae\2\2\u07d0\u07d2\7\27\2\2\u07d1\u07d3\5\u01de"+
		"\u00f0\2\u07d2\u07d1\3\2\2\2\u07d2\u07d3\3\2\2\2\u07d3\u07d4\3\2\2\2\u07d4"+
		"\u07d5\7\30\2\2\u07d5\u01c1\3\2\2\2\u07d6\u07d7\7\u00b2\2\2\u07d7\u07d9"+
		"\7\27\2\2\u07d8\u07da\5\u01de\u00f0\2\u07d9\u07d8\3\2\2\2\u07d9\u07da"+
		"\3\2\2\2\u07da\u07db\3\2\2\2\u07db\u07dc\7\30\2\2\u07dc\u01c3\3\2\2\2"+
		"\u07dd\u07de\7\u00b1\2\2\u07de\u07e0\7\27\2\2\u07df\u07e1\5\u01de\u00f0"+
		"\2\u07e0\u07df\3\2\2\2\u07e0\u07e1\3\2\2\2\u07e1\u07e2\3\2\2\2\u07e2\u07e3"+
		"\7\30\2\2\u07e3\u01c5\3\2\2\2\u07e4\u07e5\7\u00af\2\2\u07e5\u07e7\7\27"+
		"\2\2\u07e6\u07e8\5\u01de\u00f0\2\u07e7\u07e6\3\2\2\2\u07e7\u07e8\3\2\2"+
		"\2\u07e8\u07e9\3\2\2\2\u07e9\u07ea\7\30\2\2\u07ea\u01c7\3\2\2\2\u07eb"+
		"\u07ec\7\u00b0\2\2\u07ec\u07ee\7\27\2\2\u07ed\u07ef\5\u01de\u00f0\2\u07ee"+
		"\u07ed\3\2\2\2\u07ee\u07ef\3\2\2\2\u07ef\u07f0\3\2\2\2\u07f0\u07f1\7\30"+
		"\2\2\u07f1\u01c9\3\2\2\2\u07f2\u07f5\5\u01cc\u00e7\2\u07f3\u07f5\7\u00ba"+
		"\2\2\u07f4\u07f2\3\2\2\2\u07f4\u07f3\3\2\2\2\u07f5\u01cb\3\2\2\2\u07f6"+
		"\u07f9\7\u00bb\2\2\u07f7\u07f9\5\u01ce\u00e8\2\u07f8\u07f6\3\2\2\2\u07f8"+
		"\u07f7\3\2\2\2\u07f9\u01cd\3\2\2\2\u07fa\u07fd\7\u00be\2\2\u07fb\u07fd"+
		"\5\u01d2\u00ea\2\u07fc\u07fa\3\2\2\2\u07fc\u07fb\3\2\2\2\u07fd\u01cf\3"+
		"\2\2\2\u07fe\u0803\7\u00bb\2\2\u07ff\u0803\7\u00be\2\2\u0800\u0803\7\u00ba"+
		"\2\2\u0801\u0803\5\u01d6\u00ec\2\u0802\u07fe\3\2\2\2\u0802\u07ff\3\2\2"+
		"\2\u0802\u0800\3\2\2\2\u0802\u0801\3\2\2\2\u0803\u01d1\3\2\2\2\u0804\u0807"+
		"\5\u01d6\u00ec\2\u0805\u0807\5\u01d4\u00eb\2\u0806\u0804\3\2\2\2\u0806"+
		"\u0805\3\2\2\2\u0807\u01d3\3\2\2\2\u0808\u0809\t\32\2\2\u0809\u01d5\3"+
		"\2\2\2\u080a\u080b\t\33\2\2\u080b\u01d7\3\2\2\2\u080c\u080d\5\u01de\u00f0"+
		"\2\u080d\u01d9\3\2\2\2\u080e\u0815\7\r\2\2\u080f\u0814\7\13\2\2\u0810"+
		"\u0814\7\f\2\2\u0811\u0814\7\3\2\2\u0812\u0814\5\u01e0\u00f1\2\u0813\u080f"+
		"\3\2\2\2\u0813\u0810\3\2\2\2\u0813\u0811\3\2\2\2\u0813\u0812\3\2\2\2\u0814"+
		"\u0817\3\2\2\2\u0815\u0813\3\2\2\2\u0815\u0816\3\2\2\2\u0816\u0818\3\2"+
		"\2\2\u0817\u0815\3\2\2\2\u0818\u0819\7\r\2\2\u0819\u01db\3\2\2\2\u081a"+
		"\u0821\7\16\2\2\u081b\u0820\7\13\2\2\u081c\u0820\7\f\2\2\u081d\u0820\7"+
		"\4\2\2\u081e\u0820\5\u01e2\u00f2\2\u081f\u081b\3\2\2\2\u081f\u081c\3\2"+
		"\2\2\u081f\u081d\3\2\2\2\u081f\u081e\3\2\2\2\u0820\u0823\3\2\2\2\u0821"+
		"\u081f\3\2\2\2\u0821\u0822\3\2\2\2\u0822\u0824\3\2\2\2\u0823\u0821\3\2"+
		"\2\2\u0824\u0825\7\16\2\2\u0825\u01dd\3\2\2\2\u0826\u0829\5\u01da\u00ee"+
		"\2\u0827\u0829\5\u01dc\u00ef\2\u0828\u0826\3\2\2\2\u0828\u0827\3\2\2\2"+
		"\u0829\u01df\3\2\2\2\u082a\u082c\7\u00c6\2\2\u082b\u082a\3\2\2\2\u082c"+
		"\u082d\3\2\2\2\u082d\u082b\3\2\2\2\u082d\u082e\3\2\2\2\u082e\u083c\3\2"+
		"\2\2\u082f\u0831\7\33\2\2\u0830\u0832\5N(\2\u0831\u0830\3\2\2\2\u0831"+
		"\u0832\3\2\2\2\u0832\u0834\3\2\2\2\u0833\u0835\7\34\2\2\u0834\u0833\3"+
		"\2\2\2\u0834\u0835\3\2\2\2\u0835\u083c\3\2\2\2\u0836\u083c\7\34\2\2\u0837"+
		"\u083c\7\5\2\2\u0838\u083c\7\6\2\2\u0839\u083c\5\u01e4\u00f3\2\u083a\u083c"+
		"\5\u01dc\u00ef\2\u083b\u082b\3\2\2\2\u083b\u082f\3\2\2\2\u083b\u0836\3"+
		"\2\2\2\u083b\u0837\3\2\2\2\u083b\u0838\3\2\2\2\u083b\u0839\3\2\2\2\u083b"+
		"\u083a\3\2\2\2\u083c\u01e1\3\2\2\2\u083d\u083f\7\u00c6\2\2\u083e\u083d"+
		"\3\2\2\2\u083f\u0840\3\2\2\2\u0840\u083e\3\2\2\2\u0840\u0841\3\2\2\2\u0841"+
		"\u084f\3\2\2\2\u0842\u0844\7\33\2\2\u0843\u0845\5N(\2\u0844\u0843\3\2"+
		"\2\2\u0844\u0845\3\2\2\2\u0845\u0847\3\2\2\2\u0846\u0848\7\34\2\2\u0847"+
		"\u0846\3\2\2\2\u0847\u0848\3\2\2\2\u0848\u084f\3\2\2\2\u0849\u084f\7\34"+
		"\2\2\u084a\u084f\7\5\2\2\u084b\u084f\7\6\2\2\u084c\u084f\5\u01e4\u00f3"+
		"\2\u084d\u084f\5\u01da\u00ee\2\u084e\u083e\3\2\2\2\u084e\u0842\3\2\2\2"+
		"\u084e\u0849\3\2\2\2\u084e\u084a\3\2\2\2\u084e\u084b\3\2\2\2\u084e\u084c"+
		"\3\2\2\2\u084e\u084d\3\2\2\2\u084f\u01e3\3\2\2\2\u0850\u0853\5\u01d2\u00ea"+
		"\2\u0851\u0853\t\34\2\2\u0852\u0850\3\2\2\2\u0852\u0851\3\2\2\2\u0853"+
		"\u0854\3\2\2\2\u0854\u0852\3\2\2\2\u0854\u0855\3\2\2\2\u0855\u01e5\3\2"+
		"\2\2\u00ce\u01e7\u01ea\u01ed\u01f1\u01fa\u0212\u0218\u021c\u0223\u022a"+
		"\u023a\u0266\u026d\u0273\u027c\u027f\u0288\u0290\u0299\u029c\u02aa\u02b1"+
		"\u02b3\u02be\u02c5\u02c7\u02cf\u02d3\u02d7\u02de\u02e4\u02e9\u02f2\u02f9"+
		"\u030b\u0316\u031c\u0324\u032b\u0333\u0339\u033c\u033f\u0351\u0357\u035f"+
		"\u0366\u036c\u0373\u0380\u0389\u038c\u0391\u0396\u03a8\u03ae\u03b2\u03b6"+
		"\u03b9\u03c2\u03c8\u03cd\u03cf\u03d3\u03da\u03e1\u03ea\u03f6\u0400\u040e"+
		"\u0413\u041d\u0428\u0438\u0446\u044c\u0455\u045e\u047c\u0484\u048b\u048f"+
		"\u0496\u049c\u04a3\u04ab\u04b3\u04bb\u04c2\u04c8\u04ce\u04d4\u04db\u04e4"+
		"\u04ec\u04f6\u04ff\u0505\u050e\u0519\u051e\u0523\u052a\u052f\u0533\u053b"+
		"\u0542\u054a\u0554\u0558\u055d\u0563\u0565\u056e\u0571\u0578\u0586\u058b"+
		"\u059a\u059e\u05a9\u05ba\u05be\u05c3\u05cc\u05e0\u05e8\u05ea\u05f4\u05f6"+
		"\u05fd\u0602\u0609\u060c\u0611\u0618\u061b\u0623\u062e\u0638\u0640\u0651"+
		"\u0654\u0670\u067c\u0683\u0699\u069f\u06a9\u06ae\u06bb\u06be\u06c8\u06cc"+
		"\u06dd\u06f0\u06f2\u06fe\u070a\u070c\u0717\u0727\u072c\u0738\u074c\u0755"+
		"\u0757\u075d\u076a\u076c\u076e\u0774\u0788\u078d\u079b\u079e\u07a6\u07b6"+
		"\u07cd\u07d2\u07d9\u07e0\u07e7\u07ee\u07f4\u07f8\u07fc\u0802\u0806\u0813"+
		"\u0815\u081f\u0821\u0828\u082d\u0831\u0834\u083b\u0840\u0844\u0847\u084e"+
		"\u0852\u0854";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}