// Generated from ./src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.9.3

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

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsoniqParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, Kfor=59, Klet=60, 
		Kwhere=61, Kgroup=62, Kby=63, Korder=64, Kreturn=65, Kif=66, Kin=67, Kas=68, 
		Kat=69, Kallowing=70, Kempty=71, Kcount=72, Kstable=73, Kascending=74, 
		Kdescending=75, Ksome=76, Kevery=77, Ksatisfies=78, Kcollation=79, Kgreatest=80, 
		Kleast=81, Kswitch=82, Kcase=83, Ktry=84, Kcatch=85, Kdefault=86, Kthen=87, 
		Kelse=88, Ktypeswitch=89, Kor=90, Kand=91, Knot=92, Kto=93, Kinstance=94, 
		Kof=95, Kstatically=96, Kis=97, Ktreat=98, Kcast=99, Kcastable=100, Kversion=101, 
		Kjsoniq=102, Kunordered=103, Ktrue=104, Kfalse=105, Ktype=106, Kvalidate=107, 
		Kannotate=108, Kdeclare=109, Kcontext=110, Kitem=111, Kvariable=112, Kinsert=113, 
		Kdelete=114, Krename=115, Kreplace=116, Kcopy=117, Kmodify=118, Kappend=119, 
		Kinto=120, Kvalue=121, Kjson=122, Kwith=123, Kposition=124, Kbreak=125, 
		Kloop=126, Kcontinue=127, Kexit=128, Kreturning=129, Kwhile=130, Kimport=131, 
		Kschema=132, Knamespace=133, Kelement=134, Kslash=135, Kdslash=136, Kat_symbol=137, 
		Kchild=138, Kdescendant=139, Kattribute=140, Kself=141, Kdescendant_or_self=142, 
		Kfollowing_sibling=143, Kfollowing=144, Kparent=145, Kancestor=146, Kpreceding_sibling=147, 
		Kpreceding=148, Kancestor_or_self=149, Knode=150, Kbinary=151, Kdocument=152, 
		Kdocument_node=153, Ktext=154, Kpi=155, Knamespace_node=156, Kschema_attribute=157, 
		Kschema_element=158, Karray_node=159, Kboolean_node=160, Knull_node=161, 
		Knumber_node=162, Kobject_node=163, Kcomment=164, Karray=165, Kmap=166, 
		STRING=167, ArgumentPlaceholder=168, NullLiteral=169, Literal=170, NumericLiteral=171, 
		IntegerLiteral=172, DecimalLiteral=173, DoubleLiteral=174, WS=175, NCName=176, 
		XQComment=177, ContentChar=178;
	public static final int
		RULE_moduleAndThisIsIt = 0, RULE_module = 1, RULE_mainModule = 2, RULE_libraryModule = 3, 
		RULE_prolog = 4, RULE_program = 5, RULE_statements = 6, RULE_statementsAndExpr = 7, 
		RULE_statementsAndOptionalExpr = 8, RULE_statement = 9, RULE_applyStatement = 10, 
		RULE_assignStatement = 11, RULE_blockStatement = 12, RULE_breakStatement = 13, 
		RULE_continueStatement = 14, RULE_exitStatement = 15, RULE_flowrStatement = 16, 
		RULE_ifStatement = 17, RULE_switchStatement = 18, RULE_switchCaseStatement = 19, 
		RULE_tryCatchStatement = 20, RULE_catchCaseStatement = 21, RULE_typeSwitchStatement = 22, 
		RULE_caseStatement = 23, RULE_annotation = 24, RULE_annotations = 25, 
		RULE_varDeclStatement = 26, RULE_varDeclForStatement = 27, RULE_whileStatement = 28, 
		RULE_setter = 29, RULE_namespaceDecl = 30, RULE_annotatedDecl = 31, RULE_defaultCollationDecl = 32, 
		RULE_orderingModeDecl = 33, RULE_emptyOrderDecl = 34, RULE_decimalFormatDecl = 35, 
		RULE_qname = 36, RULE_dfPropertyName = 37, RULE_moduleImport = 38, RULE_varDecl = 39, 
		RULE_contextItemDecl = 40, RULE_functionDecl = 41, RULE_typeDecl = 42, 
		RULE_schemaLanguage = 43, RULE_paramList = 44, RULE_param = 45, RULE_expr = 46, 
		RULE_exprSingle = 47, RULE_exprSimple = 48, RULE_flowrExpr = 49, RULE_forClause = 50, 
		RULE_forVar = 51, RULE_letClause = 52, RULE_letVar = 53, RULE_whereClause = 54, 
		RULE_groupByClause = 55, RULE_groupByVar = 56, RULE_orderByClause = 57, 
		RULE_orderByExpr = 58, RULE_countClause = 59, RULE_quantifiedExpr = 60, 
		RULE_quantifiedExprVar = 61, RULE_switchExpr = 62, RULE_switchCaseClause = 63, 
		RULE_typeSwitchExpr = 64, RULE_caseClause = 65, RULE_ifExpr = 66, RULE_tryCatchExpr = 67, 
		RULE_catchClause = 68, RULE_orExpr = 69, RULE_andExpr = 70, RULE_notExpr = 71, 
		RULE_comparisonExpr = 72, RULE_stringConcatExpr = 73, RULE_rangeExpr = 74, 
		RULE_additiveExpr = 75, RULE_multiplicativeExpr = 76, RULE_instanceOfExpr = 77, 
		RULE_isStaticallyExpr = 78, RULE_treatExpr = 79, RULE_castableExpr = 80, 
		RULE_castExpr = 81, RULE_arrowExpr = 82, RULE_arrowFunctionSpecifier = 83, 
		RULE_unaryExpr = 84, RULE_valueExpr = 85, RULE_validateExpr = 86, RULE_annotateExpr = 87, 
		RULE_simpleMapExpr = 88, RULE_postFixExpr = 89, RULE_arrayLookup = 90, 
		RULE_arrayUnboxing = 91, RULE_predicate = 92, RULE_objectLookup = 93, 
		RULE_primaryExpr = 94, RULE_blockExpr = 95, RULE_varRef = 96, RULE_parenthesizedExpr = 97, 
		RULE_contextItemExpr = 98, RULE_orderedExpr = 99, RULE_unorderedExpr = 100, 
		RULE_functionCall = 101, RULE_argumentList = 102, RULE_argument = 103, 
		RULE_functionItemExpr = 104, RULE_namedFunctionRef = 105, RULE_inlineFunctionExpr = 106, 
		RULE_insertExpr = 107, RULE_deleteExpr = 108, RULE_renameExpr = 109, RULE_replaceExpr = 110, 
		RULE_transformExpr = 111, RULE_appendExpr = 112, RULE_updateLocator = 113, 
		RULE_copyDecl = 114, RULE_schemaImport = 115, RULE_schemaPrefix = 116, 
		RULE_pathExpr = 117, RULE_relativePathExpr = 118, RULE_stepExpr = 119, 
		RULE_axisStep = 120, RULE_forwardStep = 121, RULE_forwardAxis = 122, RULE_abbrevForwardStep = 123, 
		RULE_reverseStep = 124, RULE_reverseAxis = 125, RULE_abbrevReverseStep = 126, 
		RULE_nodeTest = 127, RULE_nameTest = 128, RULE_wildcard = 129, RULE_nCNameWithLocalWildcard = 130, 
		RULE_nCNameWithPrefixWildcard = 131, RULE_predicateList = 132, RULE_itemType = 133, 
		RULE_atomicOrUnionType = 134, RULE_kindTest = 135, RULE_anyKindTest = 136, 
		RULE_binaryNodeTest = 137, RULE_documentTest = 138, RULE_textTest = 139, 
		RULE_commentTest = 140, RULE_namespaceNodeTest = 141, RULE_piTest = 142, 
		RULE_attributeTest = 143, RULE_attributeNameOrWildcard = 144, RULE_schemaAttributeTest = 145, 
		RULE_elementTest = 146, RULE_elementNameOrWildcard = 147, RULE_schemaElementTest = 148, 
		RULE_elementDeclaration = 149, RULE_attributeName = 150, RULE_elementName = 151, 
		RULE_simpleTypeName = 152, RULE_typeName = 153, RULE_mapTest = 154, RULE_anyMapTest = 155, 
		RULE_typedMapTest = 156, RULE_arrayTest = 157, RULE_anyArrayTest = 158, 
		RULE_typedArrayTest = 159, RULE_parenthesizedItemTest = 160, RULE_attributeDeclaration = 161, 
		RULE_mlNodeTest = 162, RULE_mlArrayNodeTest = 163, RULE_mlObjectNodeTest = 164, 
		RULE_mlNumberNodeTest = 165, RULE_mlBooleanNodeTest = 166, RULE_mlNullNodeTest = 167, 
		RULE_sequenceType = 168, RULE_objectConstructor = 169, RULE_functionTest = 170, 
		RULE_anyFunctionTest = 171, RULE_typedFunctionTest = 172, RULE_singleType = 173, 
		RULE_pairConstructor = 174, RULE_arrayConstructor = 175, RULE_uriLiteral = 176, 
		RULE_stringLiteral = 177, RULE_keyWords = 178;
	private static String[] makeRuleNames() {
		return new String[] {
			"moduleAndThisIsIt", "module", "mainModule", "libraryModule", "prolog", 
			"program", "statements", "statementsAndExpr", "statementsAndOptionalExpr", 
			"statement", "applyStatement", "assignStatement", "blockStatement", "breakStatement", 
			"continueStatement", "exitStatement", "flowrStatement", "ifStatement", 
			"switchStatement", "switchCaseStatement", "tryCatchStatement", "catchCaseStatement", 
			"typeSwitchStatement", "caseStatement", "annotation", "annotations", 
			"varDeclStatement", "varDeclForStatement", "whileStatement", "setter", 
			"namespaceDecl", "annotatedDecl", "defaultCollationDecl", "orderingModeDecl", 
			"emptyOrderDecl", "decimalFormatDecl", "qname", "dfPropertyName", "moduleImport", 
			"varDecl", "contextItemDecl", "functionDecl", "typeDecl", "schemaLanguage", 
			"paramList", "param", "expr", "exprSingle", "exprSimple", "flowrExpr", 
			"forClause", "forVar", "letClause", "letVar", "whereClause", "groupByClause", 
			"groupByVar", "orderByClause", "orderByExpr", "countClause", "quantifiedExpr", 
			"quantifiedExprVar", "switchExpr", "switchCaseClause", "typeSwitchExpr", 
			"caseClause", "ifExpr", "tryCatchExpr", "catchClause", "orExpr", "andExpr", 
			"notExpr", "comparisonExpr", "stringConcatExpr", "rangeExpr", "additiveExpr", 
			"multiplicativeExpr", "instanceOfExpr", "isStaticallyExpr", "treatExpr", 
			"castableExpr", "castExpr", "arrowExpr", "arrowFunctionSpecifier", "unaryExpr", 
			"valueExpr", "validateExpr", "annotateExpr", "simpleMapExpr", "postFixExpr", 
			"arrayLookup", "arrayUnboxing", "predicate", "objectLookup", "primaryExpr", 
			"blockExpr", "varRef", "parenthesizedExpr", "contextItemExpr", "orderedExpr", 
			"unorderedExpr", "functionCall", "argumentList", "argument", "functionItemExpr", 
			"namedFunctionRef", "inlineFunctionExpr", "insertExpr", "deleteExpr", 
			"renameExpr", "replaceExpr", "transformExpr", "appendExpr", "updateLocator", 
			"copyDecl", "schemaImport", "schemaPrefix", "pathExpr", "relativePathExpr", 
			"stepExpr", "axisStep", "forwardStep", "forwardAxis", "abbrevForwardStep", 
			"reverseStep", "reverseAxis", "abbrevReverseStep", "nodeTest", "nameTest", 
			"wildcard", "nCNameWithLocalWildcard", "nCNameWithPrefixWildcard", "predicateList", 
			"itemType", "atomicOrUnionType", "kindTest", "anyKindTest", "binaryNodeTest", 
			"documentTest", "textTest", "commentTest", "namespaceNodeTest", "piTest", 
			"attributeTest", "attributeNameOrWildcard", "schemaAttributeTest", "elementTest", 
			"elementNameOrWildcard", "schemaElementTest", "elementDeclaration", "attributeName", 
			"elementName", "simpleTypeName", "typeName", "mapTest", "anyMapTest", 
			"typedMapTest", "arrayTest", "anyArrayTest", "typedArrayTest", "parenthesizedItemTest", 
			"attributeDeclaration", "mlNodeTest", "mlArrayNodeTest", "mlObjectNodeTest", 
			"mlNumberNodeTest", "mlBooleanNodeTest", "mlNullNodeTest", "sequenceType", 
			"objectConstructor", "functionTest", "anyFunctionTest", "typedFunctionTest", 
			"singleType", "pairConstructor", "arrayConstructor", "uriLiteral", "stringLiteral", 
			"keyWords"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'module'", "'='", "'$'", "':='", "'{'", "'}'", "'('", "')'", 
			"'*'", "'|'", "'%'", "','", "'ordering'", "'ordered'", "'decimal-format'", 
			"':'", "'decimal-separator'", "'grouping-separator'", "'infinity'", "'minus-sign'", 
			"'NaN'", "'percent'", "'per-mille'", "'zero-digit'", "'digit'", "'pattern-separator'", 
			"'external'", "'function'", "'jsound'", "'compact'", "'verbose'", "'eq'", 
			"'ne'", "'lt'", "'le'", "'gt'", "'ge'", "'!='", "'<'", "'<='", "'>'", 
			"'>='", "'||'", "'+'", "'-'", "'div'", "'idiv'", "'mod'", "'!'", "'['", 
			"']'", "'.'", "'$$'", "'#'", "'..'", "'{|'", "'|}'", "'for'", "'let'", 
			"'where'", "'group'", "'by'", "'order'", "'return'", "'if'", "'in'", 
			"'as'", "'at'", "'allowing'", "'empty'", "'count'", "'stable'", "'ascending'", 
			"'descending'", "'some'", "'every'", "'satisfies'", "'collation'", "'greatest'", 
			"'least'", "'switch'", "'case'", "'try'", "'catch'", "'default'", "'then'", 
			"'else'", "'typeswitch'", "'or'", "'and'", "'not'", "'to'", "'instance'", 
			"'of'", "'statically'", "'is'", "'treat'", "'cast'", "'castable'", "'version'", 
			"'jsoniq'", "'unordered'", "'true'", "'false'", "'type'", "'validate'", 
			"'annotate'", "'declare'", "'context'", "'item'", "'variable'", "'insert'", 
			"'delete'", "'rename'", "'replace'", "'copy'", "'modify'", "'append'", 
			"'into'", "'value'", "'json'", "'with'", "'position'", "'break'", "'loop'", 
			"'continue'", "'exit'", "'returning'", "'while'", "'import'", "'schema'", 
			"'namespace'", "'element'", "'/'", "'//'", "'@'", "'child'", "'descendant'", 
			"'attribute'", "'self'", "'descendant-or-self'", "'following-sibling'", 
			"'following'", "'parent'", "'ancestor'", "'preceding-sibling'", "'preceding'", 
			"'ancestor-or-self'", "'node'", "'binary'", "'document'", "'document-node'", 
			"'text'", "'processing-instruction'", "'namespace-node'", "'schema-attribute'", 
			"'schema-element'", "'array-node'", "'boolean-node'", "'null-node'", 
			"'number-node'", "'object-node'", "'comment'", "'array'", "'map'", null, 
			"'?'", "'null'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "Kfor", 
			"Klet", "Kwhere", "Kgroup", "Kby", "Korder", "Kreturn", "Kif", "Kin", 
			"Kas", "Kat", "Kallowing", "Kempty", "Kcount", "Kstable", "Kascending", 
			"Kdescending", "Ksome", "Kevery", "Ksatisfies", "Kcollation", "Kgreatest", 
			"Kleast", "Kswitch", "Kcase", "Ktry", "Kcatch", "Kdefault", "Kthen", 
			"Kelse", "Ktypeswitch", "Kor", "Kand", "Knot", "Kto", "Kinstance", "Kof", 
			"Kstatically", "Kis", "Ktreat", "Kcast", "Kcastable", "Kversion", "Kjsoniq", 
			"Kunordered", "Ktrue", "Kfalse", "Ktype", "Kvalidate", "Kannotate", "Kdeclare", 
			"Kcontext", "Kitem", "Kvariable", "Kinsert", "Kdelete", "Krename", "Kreplace", 
			"Kcopy", "Kmodify", "Kappend", "Kinto", "Kvalue", "Kjson", "Kwith", "Kposition", 
			"Kbreak", "Kloop", "Kcontinue", "Kexit", "Kreturning", "Kwhile", "Kimport", 
			"Kschema", "Knamespace", "Kelement", "Kslash", "Kdslash", "Kat_symbol", 
			"Kchild", "Kdescendant", "Kattribute", "Kself", "Kdescendant_or_self", 
			"Kfollowing_sibling", "Kfollowing", "Kparent", "Kancestor", "Kpreceding_sibling", 
			"Kpreceding", "Kancestor_or_self", "Knode", "Kbinary", "Kdocument", "Kdocument_node", 
			"Ktext", "Kpi", "Knamespace_node", "Kschema_attribute", "Kschema_element", 
			"Karray_node", "Kboolean_node", "Knull_node", "Knumber_node", "Kobject_node", 
			"Kcomment", "Karray", "Kmap", "STRING", "ArgumentPlaceholder", "NullLiteral", 
			"Literal", "NumericLiteral", "IntegerLiteral", "DecimalLiteral", "DoubleLiteral", 
			"WS", "NCName", "XQComment", "ContentChar"
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
	public String getGrammarFileName() { return "Jsoniq.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JsoniqParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ModuleAndThisIsItContext extends ParserRuleContext {
		public ModuleContext module() {
			return getRuleContext(ModuleContext.class,0);
		}
		public TerminalNode EOF() { return getToken(JsoniqParser.EOF, 0); }
		public ModuleAndThisIsItContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleAndThisIsIt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitModuleAndThisIsIt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleAndThisIsItContext moduleAndThisIsIt() throws RecognitionException {
		ModuleAndThisIsItContext _localctx = new ModuleAndThisIsItContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_moduleAndThisIsIt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(358);
			module();
			setState(359);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModuleContext extends ParserRuleContext {
		public StringLiteralContext vers;
		public MainModuleContext main;
		public LibraryModuleContext libraryModule() {
			return getRuleContext(LibraryModuleContext.class,0);
		}
		public TerminalNode Kjsoniq() { return getToken(JsoniqParser.Kjsoniq, 0); }
		public TerminalNode Kversion() { return getToken(JsoniqParser.Kversion, 0); }
		public MainModuleContext mainModule() {
			return getRuleContext(MainModuleContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_module);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(361);
				match(Kjsoniq);
				setState(362);
				match(Kversion);
				setState(363);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(364);
				match(T__0);
				}
				break;
			}
			setState(370);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(368);
				libraryModule();
				}
				break;
			case EOF:
			case T__3:
			case T__5:
			case T__7:
			case T__11:
			case T__14:
			case T__28:
			case T__44:
			case T__45:
			case T__50:
			case T__53:
			case T__56:
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case Kimport:
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				{
				setState(369);
				((ModuleContext)_localctx).main = mainModule();
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMainModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainModuleContext mainModule() throws RecognitionException {
		MainModuleContext _localctx = new MainModuleContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_mainModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			prolog();
			setState(373);
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

	public static class LibraryModuleContext extends ParserRuleContext {
		public TerminalNode Knamespace() { return getToken(JsoniqParser.Knamespace, 0); }
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitLibraryModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryModuleContext libraryModule() throws RecognitionException {
		LibraryModuleContext _localctx = new LibraryModuleContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_libraryModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(375);
			match(T__1);
			setState(376);
			match(Knamespace);
			setState(377);
			match(NCName);
			setState(378);
			match(T__2);
			setState(379);
			uriLiteral();
			setState(380);
			match(T__0);
			setState(381);
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

	public static class PrologContext extends ParserRuleContext {
		public List<AnnotatedDeclContext> annotatedDecl() {
			return getRuleContexts(AnnotatedDeclContext.class);
		}
		public AnnotatedDeclContext annotatedDecl(int i) {
			return getRuleContext(AnnotatedDeclContext.class,i);
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
		public List<ModuleImportContext> moduleImport() {
			return getRuleContexts(ModuleImportContext.class);
		}
		public ModuleImportContext moduleImport(int i) {
			return getRuleContext(ModuleImportContext.class,i);
		}
		public PrologContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prolog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitProlog(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrologContext prolog() throws RecognitionException {
		PrologContext _localctx = new PrologContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_prolog);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(386);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(383);
						setter();
						}
						break;
					case 2:
						{
						setState(384);
						namespaceDecl();
						}
						break;
					case 3:
						{
						setState(385);
						moduleImport();
						}
						break;
					}
					setState(388);
					match(T__0);
					}
					} 
				}
				setState(394);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(400);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(395);
					annotatedDecl();
					setState(396);
					match(T__0);
					}
					} 
				}
				setState(402);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
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

	public static class ProgramContext extends ParserRuleContext {
		public StatementsAndOptionalExprContext statementsAndOptionalExpr() {
			return getRuleContext(StatementsAndOptionalExprContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_statements);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(405);
					statement();
					}
					} 
				}
				setState(410);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatementsAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsAndExprContext statementsAndExpr() throws RecognitionException {
		StatementsAndExprContext _localctx = new StatementsAndExprContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_statementsAndExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			statements();
			setState(412);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatementsAndOptionalExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsAndOptionalExprContext statementsAndOptionalExpr() throws RecognitionException {
		StatementsAndOptionalExprContext _localctx = new StatementsAndOptionalExprContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_statementsAndOptionalExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			statements();
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(415);
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
		public FlowrStatementContext flowrStatement() {
			return getRuleContext(FlowrStatementContext.class,0);
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
		public TypeSwitchStatementContext typeSwitchStatement() {
			return getRuleContext(TypeSwitchStatementContext.class,0);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_statement);
		try {
			setState(431);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(418);
				applyStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(419);
				assignStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(420);
				blockStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(421);
				breakStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(422);
				continueStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(423);
				exitStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(424);
				flowrStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(425);
				ifStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(426);
				switchStatement();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(427);
				tryCatchStatement();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(428);
				typeSwitchStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(429);
				varDeclStatement();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(430);
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

	public static class ApplyStatementContext extends ParserRuleContext {
		public ExprSimpleContext exprSimple() {
			return getRuleContext(ExprSimpleContext.class,0);
		}
		public ApplyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_applyStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitApplyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ApplyStatementContext applyStatement() throws RecognitionException {
		ApplyStatementContext _localctx = new ApplyStatementContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_applyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			exprSimple();
			setState(434);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignStatementContext extends ParserRuleContext {
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public AssignStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAssignStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignStatementContext assignStatement() throws RecognitionException {
		AssignStatementContext _localctx = new AssignStatementContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_assignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			match(T__3);
			setState(437);
			qname();
			setState(438);
			match(T__4);
			setState(439);
			exprSingle();
			setState(440);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockStatementContext extends ParserRuleContext {
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_blockStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			match(T__5);
			setState(443);
			statements();
			setState(444);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BreakStatementContext extends ParserRuleContext {
		public TerminalNode Kbreak() { return getToken(JsoniqParser.Kbreak, 0); }
		public TerminalNode Kloop() { return getToken(JsoniqParser.Kloop, 0); }
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			match(Kbreak);
			setState(447);
			match(Kloop);
			setState(448);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContinueStatementContext extends ParserRuleContext {
		public TerminalNode Kcontinue() { return getToken(JsoniqParser.Kcontinue, 0); }
		public TerminalNode Kloop() { return getToken(JsoniqParser.Kloop, 0); }
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(450);
			match(Kcontinue);
			setState(451);
			match(Kloop);
			setState(452);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExitStatementContext extends ParserRuleContext {
		public TerminalNode Kexit() { return getToken(JsoniqParser.Kexit, 0); }
		public TerminalNode Kreturning() { return getToken(JsoniqParser.Kreturning, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ExitStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exitStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExitStatementContext exitStatement() throws RecognitionException {
		ExitStatementContext _localctx = new ExitStatementContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_exitStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			match(Kexit);
			setState(455);
			match(Kreturning);
			setState(456);
			exprSingle();
			setState(457);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FlowrStatementContext extends ParserRuleContext {
		public ForClauseContext start_for;
		public LetClauseContext start_let;
		public StatementContext returnStmt;
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ForClauseContext> forClause() {
			return getRuleContexts(ForClauseContext.class);
		}
		public ForClauseContext forClause(int i) {
			return getRuleContext(ForClauseContext.class,i);
		}
		public List<LetClauseContext> letClause() {
			return getRuleContexts(LetClauseContext.class);
		}
		public LetClauseContext letClause(int i) {
			return getRuleContext(LetClauseContext.class,i);
		}
		public List<WhereClauseContext> whereClause() {
			return getRuleContexts(WhereClauseContext.class);
		}
		public WhereClauseContext whereClause(int i) {
			return getRuleContext(WhereClauseContext.class,i);
		}
		public List<GroupByClauseContext> groupByClause() {
			return getRuleContexts(GroupByClauseContext.class);
		}
		public GroupByClauseContext groupByClause(int i) {
			return getRuleContext(GroupByClauseContext.class,i);
		}
		public List<OrderByClauseContext> orderByClause() {
			return getRuleContexts(OrderByClauseContext.class);
		}
		public OrderByClauseContext orderByClause(int i) {
			return getRuleContext(OrderByClauseContext.class,i);
		}
		public List<CountClauseContext> countClause() {
			return getRuleContexts(CountClauseContext.class);
		}
		public CountClauseContext countClause(int i) {
			return getRuleContext(CountClauseContext.class,i);
		}
		public FlowrStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowrStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFlowrStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowrStatementContext flowrStatement() throws RecognitionException {
		FlowrStatementContext _localctx = new FlowrStatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_flowrStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(459);
				((FlowrStatementContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(460);
				((FlowrStatementContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(471);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (Kfor - 59)) | (1L << (Klet - 59)) | (1L << (Kwhere - 59)) | (1L << (Kgroup - 59)) | (1L << (Korder - 59)) | (1L << (Kcount - 59)) | (1L << (Kstable - 59)))) != 0)) {
				{
				setState(469);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(463);
					forClause();
					}
					break;
				case Klet:
					{
					setState(464);
					letClause();
					}
					break;
				case Kwhere:
					{
					setState(465);
					whereClause();
					}
					break;
				case Kgroup:
					{
					setState(466);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(467);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(468);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(473);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(474);
			match(Kreturn);
			setState(475);
			((FlowrStatementContext)_localctx).returnStmt = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfStatementContext extends ParserRuleContext {
		public ExprContext test_expr;
		public StatementContext branch;
		public StatementContext else_branch;
		public TerminalNode Kif() { return getToken(JsoniqParser.Kif, 0); }
		public TerminalNode Kthen() { return getToken(JsoniqParser.Kthen, 0); }
		public TerminalNode Kelse() { return getToken(JsoniqParser.Kelse, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_ifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			match(Kif);
			setState(478);
			match(T__7);
			setState(479);
			((IfStatementContext)_localctx).test_expr = expr();
			setState(480);
			match(T__8);
			setState(481);
			match(Kthen);
			setState(482);
			((IfStatementContext)_localctx).branch = statement();
			setState(483);
			match(Kelse);
			setState(484);
			((IfStatementContext)_localctx).else_branch = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SwitchStatementContext extends ParserRuleContext {
		public ExprContext condExpr;
		public SwitchCaseStatementContext switchCaseStatement;
		public List<SwitchCaseStatementContext> cases = new ArrayList<SwitchCaseStatementContext>();
		public StatementContext def;
		public TerminalNode Kswitch() { return getToken(JsoniqParser.Kswitch, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchStatementContext switchStatement() throws RecognitionException {
		SwitchStatementContext _localctx = new SwitchStatementContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_switchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			match(Kswitch);
			setState(487);
			match(T__7);
			setState(488);
			((SwitchStatementContext)_localctx).condExpr = expr();
			setState(489);
			match(T__8);
			setState(491); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(490);
				((SwitchStatementContext)_localctx).switchCaseStatement = switchCaseStatement();
				((SwitchStatementContext)_localctx).cases.add(((SwitchStatementContext)_localctx).switchCaseStatement);
				}
				}
				setState(493); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(495);
			match(Kdefault);
			setState(496);
			match(Kreturn);
			setState(497);
			((SwitchStatementContext)_localctx).def = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SwitchCaseStatementContext extends ParserRuleContext {
		public ExprSingleContext exprSingle;
		public List<ExprSingleContext> cond = new ArrayList<ExprSingleContext>();
		public StatementContext ret;
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<TerminalNode> Kcase() { return getTokens(JsoniqParser.Kcase); }
		public TerminalNode Kcase(int i) {
			return getToken(JsoniqParser.Kcase, i);
		}
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public SwitchCaseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCaseStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchCaseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseStatementContext switchCaseStatement() throws RecognitionException {
		SwitchCaseStatementContext _localctx = new SwitchCaseStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_switchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(499);
				match(Kcase);
				setState(500);
				((SwitchCaseStatementContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseStatementContext)_localctx).cond.add(((SwitchCaseStatementContext)_localctx).exprSingle);
				}
				}
				setState(503); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(505);
			match(Kreturn);
			setState(506);
			((SwitchCaseStatementContext)_localctx).ret = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TryCatchStatementContext extends ParserRuleContext {
		public BlockStatementContext try_block;
		public CatchCaseStatementContext catchCaseStatement;
		public List<CatchCaseStatementContext> catches = new ArrayList<CatchCaseStatementContext>();
		public TerminalNode Ktry() { return getToken(JsoniqParser.Ktry, 0); }
		public BlockStatementContext blockStatement() {
			return getRuleContext(BlockStatementContext.class,0);
		}
		public List<CatchCaseStatementContext> catchCaseStatement() {
			return getRuleContexts(CatchCaseStatementContext.class);
		}
		public CatchCaseStatementContext catchCaseStatement(int i) {
			return getRuleContext(CatchCaseStatementContext.class,i);
		}
		public TryCatchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryCatchStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTryCatchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryCatchStatementContext tryCatchStatement() throws RecognitionException {
		TryCatchStatementContext _localctx = new TryCatchStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_tryCatchStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(508);
			match(Ktry);
			setState(509);
			((TryCatchStatementContext)_localctx).try_block = blockStatement();
			setState(511); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(510);
					((TryCatchStatementContext)_localctx).catchCaseStatement = catchCaseStatement();
					((TryCatchStatementContext)_localctx).catches.add(((TryCatchStatementContext)_localctx).catchCaseStatement);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(513); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
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

	public static class CatchCaseStatementContext extends ParserRuleContext {
		public Token s10;
		public List<Token> jokers = new ArrayList<Token>();
		public QnameContext qname;
		public List<QnameContext> errors = new ArrayList<QnameContext>();
		public BlockStatementContext catch_block;
		public TerminalNode Kcatch() { return getToken(JsoniqParser.Kcatch, 0); }
		public BlockStatementContext blockStatement() {
			return getRuleContext(BlockStatementContext.class,0);
		}
		public List<QnameContext> qname() {
			return getRuleContexts(QnameContext.class);
		}
		public QnameContext qname(int i) {
			return getRuleContext(QnameContext.class,i);
		}
		public CatchCaseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchCaseStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCatchCaseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchCaseStatementContext catchCaseStatement() throws RecognitionException {
		CatchCaseStatementContext _localctx = new CatchCaseStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_catchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			match(Kcatch);
			setState(518);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				{
				setState(516);
				((CatchCaseStatementContext)_localctx).s10 = match(T__9);
				((CatchCaseStatementContext)_localctx).jokers.add(((CatchCaseStatementContext)_localctx).s10);
				}
				break;
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				{
				setState(517);
				((CatchCaseStatementContext)_localctx).qname = qname();
				((CatchCaseStatementContext)_localctx).errors.add(((CatchCaseStatementContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(527);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(520);
				match(T__10);
				setState(523);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__9:
					{
					setState(521);
					((CatchCaseStatementContext)_localctx).s10 = match(T__9);
					((CatchCaseStatementContext)_localctx).jokers.add(((CatchCaseStatementContext)_localctx).s10);
					}
					break;
				case Kfor:
				case Klet:
				case Kwhere:
				case Kgroup:
				case Kby:
				case Korder:
				case Kreturn:
				case Kif:
				case Kin:
				case Kas:
				case Kat:
				case Kallowing:
				case Kempty:
				case Kcount:
				case Kstable:
				case Kascending:
				case Kdescending:
				case Ksome:
				case Kevery:
				case Ksatisfies:
				case Kcollation:
				case Kgreatest:
				case Kleast:
				case Kswitch:
				case Kcase:
				case Ktry:
				case Kcatch:
				case Kdefault:
				case Kthen:
				case Kelse:
				case Ktypeswitch:
				case Kor:
				case Kand:
				case Knot:
				case Kto:
				case Kinstance:
				case Kof:
				case Kstatically:
				case Kis:
				case Ktreat:
				case Kcast:
				case Kcastable:
				case Kversion:
				case Kjsoniq:
				case Kunordered:
				case Ktrue:
				case Kfalse:
				case Ktype:
				case Kvalidate:
				case Kannotate:
				case Kdeclare:
				case Kcontext:
				case Kitem:
				case Kvariable:
				case Kinsert:
				case Kdelete:
				case Krename:
				case Kreplace:
				case Kcopy:
				case Kmodify:
				case Kappend:
				case Kinto:
				case Kvalue:
				case Kjson:
				case Kwith:
				case Kposition:
				case Kbreak:
				case Kloop:
				case Kcontinue:
				case Kexit:
				case Kreturning:
				case Kwhile:
				case NullLiteral:
				case NCName:
					{
					setState(522);
					((CatchCaseStatementContext)_localctx).qname = qname();
					((CatchCaseStatementContext)_localctx).errors.add(((CatchCaseStatementContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(529);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(530);
			((CatchCaseStatementContext)_localctx).catch_block = blockStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeSwitchStatementContext extends ParserRuleContext {
		public ExprContext cond;
		public CaseStatementContext caseStatement;
		public List<CaseStatementContext> cases = new ArrayList<CaseStatementContext>();
		public VarRefContext var_ref;
		public StatementContext def;
		public TerminalNode Ktypeswitch() { return getToken(JsoniqParser.Ktypeswitch, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<CaseStatementContext> caseStatement() {
			return getRuleContexts(CaseStatementContext.class);
		}
		public CaseStatementContext caseStatement(int i) {
			return getRuleContext(CaseStatementContext.class,i);
		}
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public TypeSwitchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSwitchStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeSwitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSwitchStatementContext typeSwitchStatement() throws RecognitionException {
		TypeSwitchStatementContext _localctx = new TypeSwitchStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_typeSwitchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(532);
			match(Ktypeswitch);
			setState(533);
			match(T__7);
			setState(534);
			((TypeSwitchStatementContext)_localctx).cond = expr();
			setState(535);
			match(T__8);
			setState(537); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(536);
				((TypeSwitchStatementContext)_localctx).caseStatement = caseStatement();
				((TypeSwitchStatementContext)_localctx).cases.add(((TypeSwitchStatementContext)_localctx).caseStatement);
				}
				}
				setState(539); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(541);
			match(Kdefault);
			setState(543);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(542);
				((TypeSwitchStatementContext)_localctx).var_ref = varRef();
				}
			}

			setState(545);
			match(Kreturn);
			setState(546);
			((TypeSwitchStatementContext)_localctx).def = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CaseStatementContext extends ParserRuleContext {
		public VarRefContext var_ref;
		public SequenceTypeContext sequenceType;
		public List<SequenceTypeContext> union = new ArrayList<SequenceTypeContext>();
		public StatementContext ret;
		public TerminalNode Kcase() { return getToken(JsoniqParser.Kcase, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public List<SequenceTypeContext> sequenceType() {
			return getRuleContexts(SequenceTypeContext.class);
		}
		public SequenceTypeContext sequenceType(int i) {
			return getRuleContext(SequenceTypeContext.class,i);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public CaseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCaseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseStatementContext caseStatement() throws RecognitionException {
		CaseStatementContext _localctx = new CaseStatementContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_caseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(548);
			match(Kcase);
			setState(552);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(549);
				((CaseStatementContext)_localctx).var_ref = varRef();
				setState(550);
				match(Kas);
				}
			}

			setState(554);
			((CaseStatementContext)_localctx).sequenceType = sequenceType();
			((CaseStatementContext)_localctx).union.add(((CaseStatementContext)_localctx).sequenceType);
			setState(559);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(555);
				match(T__10);
				setState(556);
				((CaseStatementContext)_localctx).sequenceType = sequenceType();
				((CaseStatementContext)_localctx).union.add(((CaseStatementContext)_localctx).sequenceType);
				}
				}
				setState(561);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(562);
			match(Kreturn);
			setState(563);
			((CaseStatementContext)_localctx).ret = statement();
			}
		}
		catch (RecognitionException re) {
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
		public QnameContext name;
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public List<TerminalNode> Literal() { return getTokens(JsoniqParser.Literal); }
		public TerminalNode Literal(int i) {
			return getToken(JsoniqParser.Literal, i);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			match(T__11);
			setState(566);
			((AnnotationContext)_localctx).name = qname();
			setState(577);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(567);
				match(T__7);
				setState(568);
				match(Literal);
				setState(573);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(569);
					match(T__12);
					setState(570);
					match(Literal);
					}
					}
					setState(575);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(576);
				match(T__8);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnnotations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationsContext annotations() throws RecognitionException {
		AnnotationsContext _localctx = new AnnotationsContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_annotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(582);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(579);
				annotation();
				}
				}
				setState(584);
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

	public static class VarDeclStatementContext extends ParserRuleContext {
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode Kvariable() { return getToken(JsoniqParser.Kvariable, 0); }
		public List<VarDeclForStatementContext> varDeclForStatement() {
			return getRuleContexts(VarDeclForStatementContext.class);
		}
		public VarDeclForStatementContext varDeclForStatement(int i) {
			return getRuleContext(VarDeclForStatementContext.class,i);
		}
		public VarDeclStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarDeclStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclStatementContext varDeclStatement() throws RecognitionException {
		VarDeclStatementContext _localctx = new VarDeclStatementContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_varDeclStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(585);
			annotations();
			setState(586);
			match(Kvariable);
			setState(587);
			varDeclForStatement();
			setState(592);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(588);
				match(T__12);
				setState(589);
				varDeclForStatement();
				}
				}
				setState(594);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(595);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclForStatementContext extends ParserRuleContext {
		public VarRefContext var_ref;
		public ExprSingleContext exprSingle;
		public List<ExprSingleContext> expr_vals = new ArrayList<ExprSingleContext>();
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public VarDeclForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclForStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarDeclForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclForStatementContext varDeclForStatement() throws RecognitionException {
		VarDeclForStatementContext _localctx = new VarDeclForStatementContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_varDeclForStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(597);
			((VarDeclForStatementContext)_localctx).var_ref = varRef();
			setState(600);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(598);
				match(Kas);
				setState(599);
				sequenceType();
				}
			}

			setState(604);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(602);
				match(T__4);
				setState(603);
				((VarDeclForStatementContext)_localctx).exprSingle = exprSingle();
				((VarDeclForStatementContext)_localctx).expr_vals.add(((VarDeclForStatementContext)_localctx).exprSingle);
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

	public static class WhileStatementContext extends ParserRuleContext {
		public ExprContext test_expr;
		public StatementContext stmt;
		public TerminalNode Kwhile() { return getToken(JsoniqParser.Kwhile, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(606);
			match(Kwhile);
			setState(607);
			match(T__7);
			setState(608);
			((WhileStatementContext)_localctx).test_expr = expr();
			setState(609);
			match(T__8);
			setState(610);
			((WhileStatementContext)_localctx).stmt = statement();
			}
		}
		catch (RecognitionException re) {
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
		public DefaultCollationDeclContext defaultCollationDecl() {
			return getRuleContext(DefaultCollationDeclContext.class,0);
		}
		public OrderingModeDeclContext orderingModeDecl() {
			return getRuleContext(OrderingModeDeclContext.class,0);
		}
		public EmptyOrderDeclContext emptyOrderDecl() {
			return getRuleContext(EmptyOrderDeclContext.class,0);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSetter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetterContext setter() throws RecognitionException {
		SetterContext _localctx = new SetterContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_setter);
		try {
			setState(616);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(612);
				defaultCollationDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(613);
				orderingModeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(614);
				emptyOrderDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(615);
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

	public static class NamespaceDeclContext extends ParserRuleContext {
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode Knamespace() { return getToken(JsoniqParser.Knamespace, 0); }
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public NamespaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNamespaceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclContext namespaceDecl() throws RecognitionException {
		NamespaceDeclContext _localctx = new NamespaceDeclContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_namespaceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(618);
			match(Kdeclare);
			setState(619);
			match(Knamespace);
			setState(620);
			match(NCName);
			setState(621);
			match(T__2);
			setState(622);
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

	public static class AnnotatedDeclContext extends ParserRuleContext {
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
		}
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public TypeDeclContext typeDecl() {
			return getRuleContext(TypeDeclContext.class,0);
		}
		public ContextItemDeclContext contextItemDecl() {
			return getRuleContext(ContextItemDeclContext.class,0);
		}
		public AnnotatedDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotatedDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnnotatedDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotatedDeclContext annotatedDecl() throws RecognitionException {
		AnnotatedDeclContext _localctx = new AnnotatedDeclContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_annotatedDecl);
		try {
			setState(628);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(624);
				functionDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(625);
				varDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(626);
				typeDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(627);
				contextItemDecl();
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

	public static class DefaultCollationDeclContext extends ParserRuleContext {
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kcollation() { return getToken(JsoniqParser.Kcollation, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public DefaultCollationDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultCollationDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDefaultCollationDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultCollationDeclContext defaultCollationDecl() throws RecognitionException {
		DefaultCollationDeclContext _localctx = new DefaultCollationDeclContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(630);
			match(Kdeclare);
			setState(631);
			match(Kdefault);
			setState(632);
			match(Kcollation);
			setState(633);
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

	public static class OrderingModeDeclContext extends ParserRuleContext {
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode Kunordered() { return getToken(JsoniqParser.Kunordered, 0); }
		public OrderingModeDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderingModeDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderingModeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderingModeDeclContext orderingModeDecl() throws RecognitionException {
		OrderingModeDeclContext _localctx = new OrderingModeDeclContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(635);
			match(Kdeclare);
			setState(636);
			match(T__13);
			setState(637);
			_la = _input.LA(1);
			if ( !(_la==T__14 || _la==Kunordered) ) {
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

	public static class EmptyOrderDeclContext extends ParserRuleContext {
		public Token emptySequenceOrder;
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Korder() { return getToken(JsoniqParser.Korder, 0); }
		public TerminalNode Kempty() { return getToken(JsoniqParser.Kempty, 0); }
		public TerminalNode Kgreatest() { return getToken(JsoniqParser.Kgreatest, 0); }
		public TerminalNode Kleast() { return getToken(JsoniqParser.Kleast, 0); }
		public EmptyOrderDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyOrderDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitEmptyOrderDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyOrderDeclContext emptyOrderDecl() throws RecognitionException {
		EmptyOrderDeclContext _localctx = new EmptyOrderDeclContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			match(Kdeclare);
			setState(640);
			match(Kdefault);
			setState(641);
			match(Korder);
			setState(642);
			match(Kempty);
			{
			setState(643);
			((EmptyOrderDeclContext)_localctx).emptySequenceOrder = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==Kgreatest || _la==Kleast) ) {
				((EmptyOrderDeclContext)_localctx).emptySequenceOrder = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	public static class DecimalFormatDeclContext extends ParserRuleContext {
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public List<DfPropertyNameContext> dfPropertyName() {
			return getRuleContexts(DfPropertyNameContext.class);
		}
		public DfPropertyNameContext dfPropertyName(int i) {
			return getRuleContext(DfPropertyNameContext.class,i);
		}
		public List<StringLiteralContext> stringLiteral() {
			return getRuleContexts(StringLiteralContext.class);
		}
		public StringLiteralContext stringLiteral(int i) {
			return getRuleContext(StringLiteralContext.class,i);
		}
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public DecimalFormatDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalFormatDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDecimalFormatDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalFormatDeclContext decimalFormatDecl() throws RecognitionException {
		DecimalFormatDeclContext _localctx = new DecimalFormatDeclContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(645);
			match(Kdeclare);
			setState(650);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				{
				{
				setState(646);
				match(T__15);
				setState(647);
				qname();
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(648);
				match(Kdefault);
				setState(649);
				match(T__15);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(658);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26))) != 0)) {
				{
				{
				setState(652);
				dfPropertyName();
				setState(653);
				match(T__2);
				setState(654);
				stringLiteral();
				}
				}
				setState(660);
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

	public static class QnameContext extends ParserRuleContext {
		public Token ns;
		public KeyWordsContext nskw;
		public Token local_name;
		public KeyWordsContext local_namekw;
		public List<TerminalNode> NCName() { return getTokens(JsoniqParser.NCName); }
		public TerminalNode NCName(int i) {
			return getToken(JsoniqParser.NCName, i);
		}
		public List<KeyWordsContext> keyWords() {
			return getRuleContexts(KeyWordsContext.class);
		}
		public KeyWordsContext keyWords(int i) {
			return getRuleContext(KeyWordsContext.class,i);
		}
		public QnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qname; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QnameContext qname() throws RecognitionException {
		QnameContext _localctx = new QnameContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(666);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(663);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(661);
					((QnameContext)_localctx).ns = match(NCName);
					}
					break;
				case Kfor:
				case Klet:
				case Kwhere:
				case Kgroup:
				case Kby:
				case Korder:
				case Kreturn:
				case Kif:
				case Kin:
				case Kas:
				case Kat:
				case Kallowing:
				case Kempty:
				case Kcount:
				case Kstable:
				case Kascending:
				case Kdescending:
				case Ksome:
				case Kevery:
				case Ksatisfies:
				case Kcollation:
				case Kgreatest:
				case Kleast:
				case Kswitch:
				case Kcase:
				case Ktry:
				case Kcatch:
				case Kdefault:
				case Kthen:
				case Kelse:
				case Ktypeswitch:
				case Kor:
				case Kand:
				case Knot:
				case Kto:
				case Kinstance:
				case Kof:
				case Kstatically:
				case Kis:
				case Ktreat:
				case Kcast:
				case Kcastable:
				case Kversion:
				case Kjsoniq:
				case Kunordered:
				case Ktrue:
				case Kfalse:
				case Ktype:
				case Kvalidate:
				case Kannotate:
				case Kdeclare:
				case Kcontext:
				case Kitem:
				case Kvariable:
				case Kinsert:
				case Kdelete:
				case Krename:
				case Kreplace:
				case Kcopy:
				case Kmodify:
				case Kappend:
				case Kinto:
				case Kvalue:
				case Kjson:
				case Kwith:
				case Kposition:
				case Kbreak:
				case Kloop:
				case Kcontinue:
				case Kexit:
				case Kreturning:
				case Kwhile:
				case NullLiteral:
					{
					setState(662);
					((QnameContext)_localctx).nskw = keyWords();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(665);
				match(T__16);
				}
				break;
			}
			setState(670);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				{
				setState(668);
				((QnameContext)_localctx).local_name = match(NCName);
				}
				break;
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
				{
				setState(669);
				((QnameContext)_localctx).local_namekw = keyWords();
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

	public static class DfPropertyNameContext extends ParserRuleContext {
		public DfPropertyNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dfPropertyName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDfPropertyName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DfPropertyNameContext dfPropertyName() throws RecognitionException {
		DfPropertyNameContext _localctx = new DfPropertyNameContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(672);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26))) != 0)) ) {
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

	public static class ModuleImportContext extends ParserRuleContext {
		public Token prefix;
		public UriLiteralContext targetNamespace;
		public TerminalNode Kimport() { return getToken(JsoniqParser.Kimport, 0); }
		public List<UriLiteralContext> uriLiteral() {
			return getRuleContexts(UriLiteralContext.class);
		}
		public UriLiteralContext uriLiteral(int i) {
			return getRuleContext(UriLiteralContext.class,i);
		}
		public TerminalNode Knamespace() { return getToken(JsoniqParser.Knamespace, 0); }
		public TerminalNode Kat() { return getToken(JsoniqParser.Kat, 0); }
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public ModuleImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleImport; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitModuleImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleImportContext moduleImport() throws RecognitionException {
		ModuleImportContext _localctx = new ModuleImportContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(674);
			match(Kimport);
			setState(675);
			match(T__1);
			setState(679);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Knamespace) {
				{
				setState(676);
				match(Knamespace);
				setState(677);
				((ModuleImportContext)_localctx).prefix = match(NCName);
				setState(678);
				match(T__2);
				}
			}

			setState(681);
			((ModuleImportContext)_localctx).targetNamespace = uriLiteral();
			setState(691);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(682);
				match(Kat);
				setState(683);
				uriLiteral();
				setState(688);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(684);
					match(T__12);
					setState(685);
					uriLiteral();
					}
					}
					setState(690);
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

	public static class VarDeclContext extends ParserRuleContext {
		public Token external;
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode Kvariable() { return getToken(JsoniqParser.Kvariable, 0); }
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(693);
			match(Kdeclare);
			setState(694);
			annotations();
			setState(695);
			match(Kvariable);
			setState(696);
			varRef();
			setState(699);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(697);
				match(Kas);
				setState(698);
				sequenceType();
				}
			}

			setState(708);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				{
				{
				setState(701);
				match(T__4);
				setState(702);
				exprSingle();
				}
				}
				break;
			case T__27:
				{
				{
				setState(703);
				((VarDeclContext)_localctx).external = match(T__27);
				setState(706);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(704);
					match(T__4);
					setState(705);
					exprSingle();
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

	public static class ContextItemDeclContext extends ParserRuleContext {
		public Token external;
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode Kcontext() { return getToken(JsoniqParser.Kcontext, 0); }
		public TerminalNode Kitem() { return getToken(JsoniqParser.Kitem, 0); }
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ContextItemDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextItemDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContextItemDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemDeclContext contextItemDecl() throws RecognitionException {
		ContextItemDeclContext _localctx = new ContextItemDeclContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_contextItemDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(710);
			match(Kdeclare);
			setState(711);
			match(Kcontext);
			setState(712);
			match(Kitem);
			setState(715);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(713);
				match(Kas);
				setState(714);
				sequenceType();
				}
			}

			setState(724);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				{
				{
				setState(717);
				match(T__4);
				setState(718);
				exprSingle();
				}
				}
				break;
			case T__27:
				{
				{
				setState(719);
				((ContextItemDeclContext)_localctx).external = match(T__27);
				setState(722);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(720);
					match(T__4);
					setState(721);
					exprSingle();
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
		public QnameContext fn_name;
		public SequenceTypeContext return_type;
		public StatementsAndOptionalExprContext fn_body;
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public StatementsAndOptionalExprContext statementsAndOptionalExpr() {
			return getRuleContext(StatementsAndOptionalExprContext.class,0);
		}
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(726);
			match(Kdeclare);
			setState(727);
			annotations();
			setState(728);
			match(T__28);
			setState(729);
			((FunctionDeclContext)_localctx).fn_name = qname();
			setState(730);
			match(T__7);
			setState(732);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(731);
				paramList();
				}
			}

			setState(734);
			match(T__8);
			setState(737);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(735);
				match(Kas);
				setState(736);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(744);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				{
				setState(739);
				match(T__5);
				{
				setState(740);
				((FunctionDeclContext)_localctx).fn_body = statementsAndOptionalExpr();
				}
				setState(741);
				match(T__6);
				}
				break;
			case T__27:
				{
				setState(743);
				match(T__27);
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

	public static class TypeDeclContext extends ParserRuleContext {
		public QnameContext type_name;
		public SchemaLanguageContext schema;
		public ExprSingleContext type_definition;
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode Ktype() { return getToken(JsoniqParser.Ktype, 0); }
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public SchemaLanguageContext schemaLanguage() {
			return getRuleContext(SchemaLanguageContext.class,0);
		}
		public TypeDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclContext typeDecl() throws RecognitionException {
		TypeDeclContext _localctx = new TypeDeclContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_typeDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(746);
			match(Kdeclare);
			setState(747);
			match(Ktype);
			setState(748);
			((TypeDeclContext)_localctx).type_name = qname();
			setState(749);
			match(Kas);
			setState(751);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(750);
				((TypeDeclContext)_localctx).schema = schemaLanguage();
				}
				break;
			}
			setState(753);
			((TypeDeclContext)_localctx).type_definition = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SchemaLanguageContext extends ParserRuleContext {
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public TerminalNode Kschema() { return getToken(JsoniqParser.Kschema, 0); }
		public SchemaLanguageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaLanguage; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSchemaLanguage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaLanguageContext schemaLanguage() throws RecognitionException {
		SchemaLanguageContext _localctx = new SchemaLanguageContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_schemaLanguage);
		try {
			setState(761);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(755);
				match(T__29);
				setState(756);
				match(T__30);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(757);
				match(T__29);
				setState(758);
				match(T__31);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(759);
				match(Kjson);
				setState(760);
				match(Kschema);
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

	public static class ParamListContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(763);
			param();
			setState(768);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(764);
				match(T__12);
				setState(765);
				param();
				}
				}
				setState(770);
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

	public static class ParamContext extends ParserRuleContext {
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(771);
			match(T__3);
			setState(772);
			qname();
			setState(775);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(773);
				match(Kas);
				setState(774);
				sequenceType();
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

	public static class ExprContext extends ParserRuleContext {
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(777);
			exprSingle();
			setState(782);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(778);
				match(T__12);
				setState(779);
				exprSingle();
				}
				}
				setState(784);
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

	public static class ExprSingleContext extends ParserRuleContext {
		public ExprSimpleContext exprSimple() {
			return getRuleContext(ExprSimpleContext.class,0);
		}
		public FlowrExprContext flowrExpr() {
			return getRuleContext(FlowrExprContext.class,0);
		}
		public SwitchExprContext switchExpr() {
			return getRuleContext(SwitchExprContext.class,0);
		}
		public TypeSwitchExprContext typeSwitchExpr() {
			return getRuleContext(TypeSwitchExprContext.class,0);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExprSingle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSingleContext exprSingle() throws RecognitionException {
		ExprSingleContext _localctx = new ExprSingleContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_exprSingle);
		try {
			setState(791);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(785);
				exprSimple();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(786);
				flowrExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(787);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(788);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(789);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(790);
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

	public static class ExprSimpleContext extends ParserRuleContext {
		public QuantifiedExprContext quantifiedExpr() {
			return getRuleContext(QuantifiedExprContext.class,0);
		}
		public OrExprContext orExpr() {
			return getRuleContext(OrExprContext.class,0);
		}
		public InsertExprContext insertExpr() {
			return getRuleContext(InsertExprContext.class,0);
		}
		public DeleteExprContext deleteExpr() {
			return getRuleContext(DeleteExprContext.class,0);
		}
		public RenameExprContext renameExpr() {
			return getRuleContext(RenameExprContext.class,0);
		}
		public ReplaceExprContext replaceExpr() {
			return getRuleContext(ReplaceExprContext.class,0);
		}
		public TransformExprContext transformExpr() {
			return getRuleContext(TransformExprContext.class,0);
		}
		public AppendExprContext appendExpr() {
			return getRuleContext(AppendExprContext.class,0);
		}
		public ExprSimpleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprSimple; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExprSimple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSimpleContext exprSimple() throws RecognitionException {
		ExprSimpleContext _localctx = new ExprSimpleContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_exprSimple);
		try {
			setState(801);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(793);
				quantifiedExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(794);
				orExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(795);
				insertExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(796);
				deleteExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(797);
				renameExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(798);
				replaceExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(799);
				transformExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(800);
				appendExpr();
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

	public static class FlowrExprContext extends ParserRuleContext {
		public ForClauseContext start_for;
		public LetClauseContext start_let;
		public ExprSingleContext return_expr;
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public List<ForClauseContext> forClause() {
			return getRuleContexts(ForClauseContext.class);
		}
		public ForClauseContext forClause(int i) {
			return getRuleContext(ForClauseContext.class,i);
		}
		public List<LetClauseContext> letClause() {
			return getRuleContexts(LetClauseContext.class);
		}
		public LetClauseContext letClause(int i) {
			return getRuleContext(LetClauseContext.class,i);
		}
		public List<WhereClauseContext> whereClause() {
			return getRuleContexts(WhereClauseContext.class);
		}
		public WhereClauseContext whereClause(int i) {
			return getRuleContext(WhereClauseContext.class,i);
		}
		public List<GroupByClauseContext> groupByClause() {
			return getRuleContexts(GroupByClauseContext.class);
		}
		public GroupByClauseContext groupByClause(int i) {
			return getRuleContext(GroupByClauseContext.class,i);
		}
		public List<OrderByClauseContext> orderByClause() {
			return getRuleContexts(OrderByClauseContext.class);
		}
		public OrderByClauseContext orderByClause(int i) {
			return getRuleContext(OrderByClauseContext.class,i);
		}
		public List<CountClauseContext> countClause() {
			return getRuleContexts(CountClauseContext.class);
		}
		public CountClauseContext countClause(int i) {
			return getRuleContext(CountClauseContext.class,i);
		}
		public FlowrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowrExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFlowrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowrExprContext flowrExpr() throws RecognitionException {
		FlowrExprContext _localctx = new FlowrExprContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(805);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(803);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(804);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(815);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (Kfor - 59)) | (1L << (Klet - 59)) | (1L << (Kwhere - 59)) | (1L << (Kgroup - 59)) | (1L << (Korder - 59)) | (1L << (Kcount - 59)) | (1L << (Kstable - 59)))) != 0)) {
				{
				setState(813);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(807);
					forClause();
					}
					break;
				case Klet:
					{
					setState(808);
					letClause();
					}
					break;
				case Kwhere:
					{
					setState(809);
					whereClause();
					}
					break;
				case Kgroup:
					{
					setState(810);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(811);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(812);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(817);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(818);
			match(Kreturn);
			setState(819);
			((FlowrExprContext)_localctx).return_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
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
		public ForVarContext forVar;
		public List<ForVarContext> vars = new ArrayList<ForVarContext>();
		public TerminalNode Kfor() { return getToken(JsoniqParser.Kfor, 0); }
		public List<ForVarContext> forVar() {
			return getRuleContexts(ForVarContext.class);
		}
		public ForVarContext forVar(int i) {
			return getRuleContext(ForVarContext.class,i);
		}
		public ForClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitForClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForClauseContext forClause() throws RecognitionException {
		ForClauseContext _localctx = new ForClauseContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			match(Kfor);
			setState(822);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(827);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(823);
				match(T__12);
				setState(824);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(829);
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

	public static class ForVarContext extends ParserRuleContext {
		public VarRefContext var_ref;
		public SequenceTypeContext seq;
		public Token flag;
		public VarRefContext at;
		public ExprSingleContext ex;
		public TerminalNode Kin() { return getToken(JsoniqParser.Kin, 0); }
		public List<VarRefContext> varRef() {
			return getRuleContexts(VarRefContext.class);
		}
		public VarRefContext varRef(int i) {
			return getRuleContext(VarRefContext.class,i);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public TerminalNode Kempty() { return getToken(JsoniqParser.Kempty, 0); }
		public TerminalNode Kat() { return getToken(JsoniqParser.Kat, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TerminalNode Kallowing() { return getToken(JsoniqParser.Kallowing, 0); }
		public ForVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forVar; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitForVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForVarContext forVar() throws RecognitionException {
		ForVarContext _localctx = new ForVarContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(830);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(833);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(831);
				match(Kas);
				setState(832);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(837);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(835);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(836);
				match(Kempty);
				}
			}

			setState(841);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(839);
				match(Kat);
				setState(840);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(843);
			match(Kin);
			setState(844);
			((ForVarContext)_localctx).ex = exprSingle();
			}
		}
		catch (RecognitionException re) {
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
		public LetVarContext letVar;
		public List<LetVarContext> vars = new ArrayList<LetVarContext>();
		public TerminalNode Klet() { return getToken(JsoniqParser.Klet, 0); }
		public List<LetVarContext> letVar() {
			return getRuleContexts(LetVarContext.class);
		}
		public LetVarContext letVar(int i) {
			return getRuleContext(LetVarContext.class,i);
		}
		public LetClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitLetClause(this);
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
			setState(846);
			match(Klet);
			setState(847);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(852);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(848);
				match(T__12);
				setState(849);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(854);
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

	public static class LetVarContext extends ParserRuleContext {
		public VarRefContext var_ref;
		public SequenceTypeContext seq;
		public ExprSingleContext ex;
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public LetVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letVar; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitLetVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetVarContext letVar() throws RecognitionException {
		LetVarContext _localctx = new LetVarContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(855);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(858);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(856);
				match(Kas);
				setState(857);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(860);
			match(T__4);
			setState(861);
			((LetVarContext)_localctx).ex = exprSingle();
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kwhere() { return getToken(JsoniqParser.Kwhere, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(863);
			match(Kwhere);
			setState(864);
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

	public static class GroupByClauseContext extends ParserRuleContext {
		public GroupByVarContext groupByVar;
		public List<GroupByVarContext> vars = new ArrayList<GroupByVarContext>();
		public TerminalNode Kgroup() { return getToken(JsoniqParser.Kgroup, 0); }
		public TerminalNode Kby() { return getToken(JsoniqParser.Kby, 0); }
		public List<GroupByVarContext> groupByVar() {
			return getRuleContexts(GroupByVarContext.class);
		}
		public GroupByVarContext groupByVar(int i) {
			return getRuleContext(GroupByVarContext.class,i);
		}
		public GroupByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitGroupByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByClauseContext groupByClause() throws RecognitionException {
		GroupByClauseContext _localctx = new GroupByClauseContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(866);
			match(Kgroup);
			setState(867);
			match(Kby);
			setState(868);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(873);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(869);
				match(T__12);
				setState(870);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(875);
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

	public static class GroupByVarContext extends ParserRuleContext {
		public VarRefContext var_ref;
		public SequenceTypeContext seq;
		public Token decl;
		public ExprSingleContext ex;
		public UriLiteralContext uri;
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public TerminalNode Kcollation() { return getToken(JsoniqParser.Kcollation, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public GroupByVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByVar; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitGroupByVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByVarContext groupByVar() throws RecognitionException {
		GroupByVarContext _localctx = new GroupByVarContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(876);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(883);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4 || _la==Kas) {
				{
				setState(879);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(877);
					match(Kas);
					setState(878);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(881);
				((GroupByVarContext)_localctx).decl = match(T__4);
				setState(882);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(887);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(885);
				match(Kcollation);
				setState(886);
				((GroupByVarContext)_localctx).uri = uriLiteral();
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
		public Token stb;
		public List<OrderByExprContext> orderByExpr() {
			return getRuleContexts(OrderByExprContext.class);
		}
		public OrderByExprContext orderByExpr(int i) {
			return getRuleContext(OrderByExprContext.class,i);
		}
		public TerminalNode Korder() { return getToken(JsoniqParser.Korder, 0); }
		public TerminalNode Kby() { return getToken(JsoniqParser.Kby, 0); }
		public TerminalNode Kstable() { return getToken(JsoniqParser.Kstable, 0); }
		public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(894);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(889);
				match(Korder);
				setState(890);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(891);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(892);
				match(Korder);
				setState(893);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(896);
			orderByExpr();
			setState(901);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(897);
				match(T__12);
				setState(898);
				orderByExpr();
				}
				}
				setState(903);
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

	public static class OrderByExprContext extends ParserRuleContext {
		public ExprSingleContext ex;
		public Token desc;
		public Token gr;
		public Token ls;
		public UriLiteralContext uril;
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode Kascending() { return getToken(JsoniqParser.Kascending, 0); }
		public TerminalNode Kempty() { return getToken(JsoniqParser.Kempty, 0); }
		public TerminalNode Kcollation() { return getToken(JsoniqParser.Kcollation, 0); }
		public TerminalNode Kdescending() { return getToken(JsoniqParser.Kdescending, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public TerminalNode Kgreatest() { return getToken(JsoniqParser.Kgreatest, 0); }
		public TerminalNode Kleast() { return getToken(JsoniqParser.Kleast, 0); }
		public OrderByExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderByExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByExprContext orderByExpr() throws RecognitionException {
		OrderByExprContext _localctx = new OrderByExprContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(904);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(907);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(905);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(906);
				((OrderByExprContext)_localctx).desc = match(Kdescending);
				}
				break;
			case T__12:
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Korder:
			case Kreturn:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kcollation:
				break;
			default:
				break;
			}
			setState(914);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(909);
				match(Kempty);
				setState(912);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(910);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(911);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(918);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(916);
				match(Kcollation);
				setState(917);
				((OrderByExprContext)_localctx).uril = uriLiteral();
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
		public TerminalNode Kcount() { return getToken(JsoniqParser.Kcount, 0); }
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public CountClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_countClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCountClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountClauseContext countClause() throws RecognitionException {
		CountClauseContext _localctx = new CountClauseContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(920);
			match(Kcount);
			setState(921);
			varRef();
			}
		}
		catch (RecognitionException re) {
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
		public QuantifiedExprVarContext quantifiedExprVar;
		public List<QuantifiedExprVarContext> vars = new ArrayList<QuantifiedExprVarContext>();
		public TerminalNode Ksatisfies() { return getToken(JsoniqParser.Ksatisfies, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public List<QuantifiedExprVarContext> quantifiedExprVar() {
			return getRuleContexts(QuantifiedExprVarContext.class);
		}
		public QuantifiedExprVarContext quantifiedExprVar(int i) {
			return getRuleContext(QuantifiedExprVarContext.class,i);
		}
		public TerminalNode Ksome() { return getToken(JsoniqParser.Ksome, 0); }
		public TerminalNode Kevery() { return getToken(JsoniqParser.Kevery, 0); }
		public QuantifiedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantifiedExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQuantifiedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedExprContext quantifiedExpr() throws RecognitionException {
		QuantifiedExprContext _localctx = new QuantifiedExprContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(925);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(923);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(924);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(927);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(932);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(928);
				match(T__12);
				setState(929);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(934);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(935);
			match(Ksatisfies);
			setState(936);
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

	public static class QuantifiedExprVarContext extends ParserRuleContext {
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public TerminalNode Kin() { return getToken(JsoniqParser.Kin, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public QuantifiedExprVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantifiedExprVar; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQuantifiedExprVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedExprVarContext quantifiedExprVar() throws RecognitionException {
		QuantifiedExprVarContext _localctx = new QuantifiedExprVarContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(938);
			varRef();
			setState(941);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(939);
				match(Kas);
				setState(940);
				sequenceType();
				}
			}

			setState(943);
			match(Kin);
			setState(944);
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
		public TerminalNode Kswitch() { return getToken(JsoniqParser.Kswitch, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchExprContext switchExpr() throws RecognitionException {
		SwitchExprContext _localctx = new SwitchExprContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(946);
			match(Kswitch);
			setState(947);
			match(T__7);
			setState(948);
			((SwitchExprContext)_localctx).cond = expr();
			setState(949);
			match(T__8);
			setState(951); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(950);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(953); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(955);
			match(Kdefault);
			setState(956);
			match(Kreturn);
			setState(957);
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
		public ExprSingleContext exprSingle;
		public List<ExprSingleContext> cond = new ArrayList<ExprSingleContext>();
		public ExprSingleContext ret;
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public List<TerminalNode> Kcase() { return getTokens(JsoniqParser.Kcase); }
		public TerminalNode Kcase(int i) {
			return getToken(JsoniqParser.Kcase, i);
		}
		public SwitchCaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCaseClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseClauseContext switchCaseClause() throws RecognitionException {
		SwitchCaseClauseContext _localctx = new SwitchCaseClauseContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(961); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(959);
				match(Kcase);
				setState(960);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(963); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(965);
			match(Kreturn);
			setState(966);
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

	public static class TypeSwitchExprContext extends ParserRuleContext {
		public ExprContext cond;
		public CaseClauseContext caseClause;
		public List<CaseClauseContext> cses = new ArrayList<CaseClauseContext>();
		public VarRefContext var_ref;
		public ExprSingleContext def;
		public TerminalNode Ktypeswitch() { return getToken(JsoniqParser.Ktypeswitch, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public List<CaseClauseContext> caseClause() {
			return getRuleContexts(CaseClauseContext.class);
		}
		public CaseClauseContext caseClause(int i) {
			return getRuleContext(CaseClauseContext.class,i);
		}
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public TypeSwitchExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSwitchExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeSwitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSwitchExprContext typeSwitchExpr() throws RecognitionException {
		TypeSwitchExprContext _localctx = new TypeSwitchExprContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(968);
			match(Ktypeswitch);
			setState(969);
			match(T__7);
			setState(970);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(971);
			match(T__8);
			setState(973); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(972);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(975); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(977);
			match(Kdefault);
			setState(979);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(978);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(981);
			match(Kreturn);
			setState(982);
			((TypeSwitchExprContext)_localctx).def = exprSingle();
			}
		}
		catch (RecognitionException re) {
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
		public VarRefContext var_ref;
		public SequenceTypeContext sequenceType;
		public List<SequenceTypeContext> union = new ArrayList<SequenceTypeContext>();
		public ExprSingleContext ret;
		public TerminalNode Kcase() { return getToken(JsoniqParser.Kcase, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public List<SequenceTypeContext> sequenceType() {
			return getRuleContexts(SequenceTypeContext.class);
		}
		public SequenceTypeContext sequenceType(int i) {
			return getRuleContext(SequenceTypeContext.class,i);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public CaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseClauseContext caseClause() throws RecognitionException {
		CaseClauseContext _localctx = new CaseClauseContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(984);
			match(Kcase);
			setState(988);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(985);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(986);
				match(Kas);
				}
			}

			setState(990);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(995);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(991);
				match(T__10);
				setState(992);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(997);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(998);
			match(Kreturn);
			setState(999);
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

	public static class IfExprContext extends ParserRuleContext {
		public ExprContext test_condition;
		public ExprSingleContext branch;
		public ExprSingleContext else_branch;
		public TerminalNode Kif() { return getToken(JsoniqParser.Kif, 0); }
		public TerminalNode Kthen() { return getToken(JsoniqParser.Kthen, 0); }
		public TerminalNode Kelse() { return getToken(JsoniqParser.Kelse, 0); }
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfExprContext ifExpr() throws RecognitionException {
		IfExprContext _localctx = new IfExprContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1001);
			match(Kif);
			setState(1002);
			match(T__7);
			setState(1003);
			((IfExprContext)_localctx).test_condition = expr();
			setState(1004);
			match(T__8);
			setState(1005);
			match(Kthen);
			setState(1006);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(1007);
			match(Kelse);
			setState(1008);
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
		public ExprContext try_expression;
		public CatchClauseContext catchClause;
		public List<CatchClauseContext> catches = new ArrayList<CatchClauseContext>();
		public TerminalNode Ktry() { return getToken(JsoniqParser.Ktry, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTryCatchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryCatchExprContext tryCatchExpr() throws RecognitionException {
		TryCatchExprContext _localctx = new TryCatchExprContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_tryCatchExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1010);
			match(Ktry);
			setState(1011);
			match(T__5);
			setState(1012);
			((TryCatchExprContext)_localctx).try_expression = expr();
			setState(1013);
			match(T__6);
			setState(1015); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1014);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1017); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,81,_ctx);
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

	public static class CatchClauseContext extends ParserRuleContext {
		public Token s10;
		public List<Token> jokers = new ArrayList<Token>();
		public QnameContext qname;
		public List<QnameContext> errors = new ArrayList<QnameContext>();
		public ExprContext catch_expression;
		public TerminalNode Kcatch() { return getToken(JsoniqParser.Kcatch, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<QnameContext> qname() {
			return getRuleContexts(QnameContext.class);
		}
		public QnameContext qname(int i) {
			return getRuleContext(QnameContext.class,i);
		}
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCatchClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1019);
			match(Kcatch);
			setState(1022);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				{
				setState(1020);
				((CatchClauseContext)_localctx).s10 = match(T__9);
				((CatchClauseContext)_localctx).jokers.add(((CatchClauseContext)_localctx).s10);
				}
				break;
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				{
				setState(1021);
				((CatchClauseContext)_localctx).qname = qname();
				((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1031);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(1024);
				match(T__10);
				setState(1027);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__9:
					{
					setState(1025);
					((CatchClauseContext)_localctx).s10 = match(T__9);
					((CatchClauseContext)_localctx).jokers.add(((CatchClauseContext)_localctx).s10);
					}
					break;
				case Kfor:
				case Klet:
				case Kwhere:
				case Kgroup:
				case Kby:
				case Korder:
				case Kreturn:
				case Kif:
				case Kin:
				case Kas:
				case Kat:
				case Kallowing:
				case Kempty:
				case Kcount:
				case Kstable:
				case Kascending:
				case Kdescending:
				case Ksome:
				case Kevery:
				case Ksatisfies:
				case Kcollation:
				case Kgreatest:
				case Kleast:
				case Kswitch:
				case Kcase:
				case Ktry:
				case Kcatch:
				case Kdefault:
				case Kthen:
				case Kelse:
				case Ktypeswitch:
				case Kor:
				case Kand:
				case Knot:
				case Kto:
				case Kinstance:
				case Kof:
				case Kstatically:
				case Kis:
				case Ktreat:
				case Kcast:
				case Kcastable:
				case Kversion:
				case Kjsoniq:
				case Kunordered:
				case Ktrue:
				case Kfalse:
				case Ktype:
				case Kvalidate:
				case Kannotate:
				case Kdeclare:
				case Kcontext:
				case Kitem:
				case Kvariable:
				case Kinsert:
				case Kdelete:
				case Krename:
				case Kreplace:
				case Kcopy:
				case Kmodify:
				case Kappend:
				case Kinto:
				case Kvalue:
				case Kjson:
				case Kwith:
				case Kposition:
				case Kbreak:
				case Kloop:
				case Kcontinue:
				case Kexit:
				case Kreturning:
				case Kwhile:
				case NullLiteral:
				case NCName:
					{
					setState(1026);
					((CatchClauseContext)_localctx).qname = qname();
					((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1033);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1034);
			match(T__5);
			setState(1035);
			((CatchClauseContext)_localctx).catch_expression = expr();
			setState(1036);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
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
		public List<TerminalNode> Kor() { return getTokens(JsoniqParser.Kor); }
		public TerminalNode Kor(int i) {
			return getToken(JsoniqParser.Kor, i);
		}
		public OrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrExprContext orExpr() throws RecognitionException {
		OrExprContext _localctx = new OrExprContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1038);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(1043);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1039);
					match(Kor);
					setState(1040);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(1045);
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

	public static class AndExprContext extends ParserRuleContext {
		public NotExprContext main_expr;
		public NotExprContext notExpr;
		public List<NotExprContext> rhs = new ArrayList<NotExprContext>();
		public List<NotExprContext> notExpr() {
			return getRuleContexts(NotExprContext.class);
		}
		public NotExprContext notExpr(int i) {
			return getRuleContext(NotExprContext.class,i);
		}
		public List<TerminalNode> Kand() { return getTokens(JsoniqParser.Kand); }
		public TerminalNode Kand(int i) {
			return getToken(JsoniqParser.Kand, i);
		}
		public AndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1046);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(1051);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1047);
					match(Kand);
					setState(1048);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(1053);
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

	public static class NotExprContext extends ParserRuleContext {
		public Token Knot;
		public List<Token> op = new ArrayList<Token>();
		public ComparisonExprContext main_expr;
		public ComparisonExprContext comparisonExpr() {
			return getRuleContext(ComparisonExprContext.class,0);
		}
		public TerminalNode Knot() { return getToken(JsoniqParser.Knot, 0); }
		public NotExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNotExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotExprContext notExpr() throws RecognitionException {
		NotExprContext _localctx = new NotExprContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1055);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(1054);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(1057);
			((NotExprContext)_localctx).main_expr = comparisonExpr();
			}
		}
		catch (RecognitionException re) {
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
		public Token s33;
		public List<Token> op = new ArrayList<Token>();
		public Token s34;
		public Token s35;
		public Token s36;
		public Token s37;
		public Token s38;
		public Token s3;
		public Token s39;
		public Token s40;
		public Token s41;
		public Token s42;
		public Token s43;
		public Token _tset1828;
		public StringConcatExprContext stringConcatExpr;
		public List<StringConcatExprContext> rhs = new ArrayList<StringConcatExprContext>();
		public List<StringConcatExprContext> stringConcatExpr() {
			return getRuleContexts(StringConcatExprContext.class);
		}
		public StringConcatExprContext stringConcatExpr(int i) {
			return getRuleContext(StringConcatExprContext.class,i);
		}
		public ComparisonExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitComparisonExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonExprContext comparisonExpr() throws RecognitionException {
		ComparisonExprContext _localctx = new ComparisonExprContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1059);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(1062);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42))) != 0)) {
				{
				setState(1060);
				((ComparisonExprContext)_localctx)._tset1828 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1828 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1828);
				setState(1061);
				((ComparisonExprContext)_localctx).stringConcatExpr = stringConcatExpr();
				((ComparisonExprContext)_localctx).rhs.add(((ComparisonExprContext)_localctx).stringConcatExpr);
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
		public StringConcatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConcatExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStringConcatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConcatExprContext stringConcatExpr() throws RecognitionException {
		StringConcatExprContext _localctx = new StringConcatExprContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1064);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(1069);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__43) {
				{
				{
				setState(1065);
				match(T__43);
				setState(1066);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(1071);
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
		public TerminalNode Kto() { return getToken(JsoniqParser.Kto, 0); }
		public RangeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitRangeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeExprContext rangeExpr() throws RecognitionException {
		RangeExprContext _localctx = new RangeExprContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1072);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(1075);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(1073);
				match(Kto);
				setState(1074);
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
		public Token s45;
		public List<Token> op = new ArrayList<Token>();
		public Token s46;
		public Token _tset1937;
		public MultiplicativeExprContext multiplicativeExpr;
		public List<MultiplicativeExprContext> rhs = new ArrayList<MultiplicativeExprContext>();
		public List<MultiplicativeExprContext> multiplicativeExpr() {
			return getRuleContexts(MultiplicativeExprContext.class);
		}
		public MultiplicativeExprContext multiplicativeExpr(int i) {
			return getRuleContext(MultiplicativeExprContext.class,i);
		}
		public AdditiveExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAdditiveExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExprContext additiveExpr() throws RecognitionException {
		AdditiveExprContext _localctx = new AdditiveExprContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1077);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(1082);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1078);
					((AdditiveExprContext)_localctx)._tset1937 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__44 || _la==T__45) ) {
						((AdditiveExprContext)_localctx)._tset1937 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1937);
					setState(1079);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(1084);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
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
		public InstanceOfExprContext main_expr;
		public Token s10;
		public List<Token> op = new ArrayList<Token>();
		public Token s47;
		public Token s48;
		public Token s49;
		public Token _tset1965;
		public InstanceOfExprContext instanceOfExpr;
		public List<InstanceOfExprContext> rhs = new ArrayList<InstanceOfExprContext>();
		public List<InstanceOfExprContext> instanceOfExpr() {
			return getRuleContexts(InstanceOfExprContext.class);
		}
		public InstanceOfExprContext instanceOfExpr(int i) {
			return getRuleContext(InstanceOfExprContext.class,i);
		}
		public MultiplicativeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMultiplicativeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExprContext multiplicativeExpr() throws RecognitionException {
		MultiplicativeExprContext _localctx = new MultiplicativeExprContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1085);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(1090);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__46) | (1L << T__47) | (1L << T__48))) != 0)) {
				{
				{
				setState(1086);
				((MultiplicativeExprContext)_localctx)._tset1965 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__46) | (1L << T__47) | (1L << T__48))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1965 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1965);
				setState(1087);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(1092);
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

	public static class InstanceOfExprContext extends ParserRuleContext {
		public IsStaticallyExprContext main_expr;
		public SequenceTypeContext seq;
		public IsStaticallyExprContext isStaticallyExpr() {
			return getRuleContext(IsStaticallyExprContext.class,0);
		}
		public TerminalNode Kinstance() { return getToken(JsoniqParser.Kinstance, 0); }
		public TerminalNode Kof() { return getToken(JsoniqParser.Kof, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public InstanceOfExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instanceOfExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInstanceOfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstanceOfExprContext instanceOfExpr() throws RecognitionException {
		InstanceOfExprContext _localctx = new InstanceOfExprContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1093);
			((InstanceOfExprContext)_localctx).main_expr = isStaticallyExpr();
			setState(1097);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				{
				setState(1094);
				match(Kinstance);
				setState(1095);
				match(Kof);
				setState(1096);
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

	public static class IsStaticallyExprContext extends ParserRuleContext {
		public TreatExprContext main_expr;
		public SequenceTypeContext seq;
		public TreatExprContext treatExpr() {
			return getRuleContext(TreatExprContext.class,0);
		}
		public TerminalNode Kis() { return getToken(JsoniqParser.Kis, 0); }
		public TerminalNode Kstatically() { return getToken(JsoniqParser.Kstatically, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public IsStaticallyExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isStaticallyExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIsStaticallyExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsStaticallyExprContext isStaticallyExpr() throws RecognitionException {
		IsStaticallyExprContext _localctx = new IsStaticallyExprContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_isStaticallyExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1099);
			((IsStaticallyExprContext)_localctx).main_expr = treatExpr();
			setState(1103);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				{
				setState(1100);
				match(Kis);
				setState(1101);
				match(Kstatically);
				setState(1102);
				((IsStaticallyExprContext)_localctx).seq = sequenceType();
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
		public TerminalNode Ktreat() { return getToken(JsoniqParser.Ktreat, 0); }
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TreatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_treatExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTreatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TreatExprContext treatExpr() throws RecognitionException {
		TreatExprContext _localctx = new TreatExprContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1105);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(1109);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				{
				setState(1106);
				match(Ktreat);
				setState(1107);
				match(Kas);
				setState(1108);
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
		public TerminalNode Kcastable() { return getToken(JsoniqParser.Kcastable, 0); }
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SingleTypeContext singleType() {
			return getRuleContext(SingleTypeContext.class,0);
		}
		public CastableExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castableExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCastableExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastableExprContext castableExpr() throws RecognitionException {
		CastableExprContext _localctx = new CastableExprContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1111);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(1115);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				{
				setState(1112);
				match(Kcastable);
				setState(1113);
				match(Kas);
				setState(1114);
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
		public TerminalNode Kcast() { return getToken(JsoniqParser.Kcast, 0); }
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SingleTypeContext singleType() {
			return getRuleContext(SingleTypeContext.class,0);
		}
		public CastExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCastExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastExprContext castExpr() throws RecognitionException {
		CastExprContext _localctx = new CastExprContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1117);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(1121);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				{
				setState(1118);
				match(Kcast);
				setState(1119);
				match(Kas);
				setState(1120);
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
		public ArrowFunctionSpecifierContext arrowFunctionSpecifier;
		public List<ArrowFunctionSpecifierContext> function = new ArrayList<ArrowFunctionSpecifierContext>();
		public ArgumentListContext argumentList;
		public List<ArgumentListContext> arguments = new ArrayList<ArgumentListContext>();
		public UnaryExprContext unaryExpr() {
			return getRuleContext(UnaryExprContext.class,0);
		}
		public List<ArrowFunctionSpecifierContext> arrowFunctionSpecifier() {
			return getRuleContexts(ArrowFunctionSpecifierContext.class);
		}
		public ArrowFunctionSpecifierContext arrowFunctionSpecifier(int i) {
			return getRuleContext(ArrowFunctionSpecifierContext.class,i);
		}
		public List<ArgumentListContext> argumentList() {
			return getRuleContexts(ArgumentListContext.class);
		}
		public ArgumentListContext argumentList(int i) {
			return getRuleContext(ArgumentListContext.class,i);
		}
		public ArrowExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrowExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrowExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowExprContext arrowExpr() throws RecognitionException {
		ArrowExprContext _localctx = new ArrowExprContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1123);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(1132);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(1124);
					match(T__2);
					setState(1125);
					match(T__41);
					}
					setState(1127);
					((ArrowExprContext)_localctx).arrowFunctionSpecifier = arrowFunctionSpecifier();
					((ArrowExprContext)_localctx).function.add(((ArrowExprContext)_localctx).arrowFunctionSpecifier);
					setState(1128);
					((ArrowExprContext)_localctx).argumentList = argumentList();
					((ArrowExprContext)_localctx).arguments.add(((ArrowExprContext)_localctx).argumentList);
					}
					} 
				}
				setState(1134);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
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

	public static class ArrowFunctionSpecifierContext extends ParserRuleContext {
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrowFunctionSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowFunctionSpecifierContext arrowFunctionSpecifier() throws RecognitionException {
		ArrowFunctionSpecifierContext _localctx = new ArrowFunctionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_arrowFunctionSpecifier);
		try {
			setState(1138);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1135);
				qname();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(1136);
				varRef();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(1137);
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

	public static class UnaryExprContext extends ParserRuleContext {
		public Token s46;
		public List<Token> op = new ArrayList<Token>();
		public Token s45;
		public Token _tset2144;
		public ValueExprContext main_expr;
		public ValueExprContext valueExpr() {
			return getRuleContext(ValueExprContext.class,0);
		}
		public UnaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExprContext unaryExpr() throws RecognitionException {
		UnaryExprContext _localctx = new UnaryExprContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44 || _la==T__45) {
				{
				{
				setState(1140);
				((UnaryExprContext)_localctx)._tset2144 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__44 || _la==T__45) ) {
					((UnaryExprContext)_localctx)._tset2144 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset2144);
				}
				}
				setState(1145);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1146);
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
		public SimpleMapExprContext simpleMap_expr;
		public ValidateExprContext validate_expr;
		public AnnotateExprContext annotate_expr;
		public SimpleMapExprContext simpleMapExpr() {
			return getRuleContext(SimpleMapExprContext.class,0);
		}
		public ValidateExprContext validateExpr() {
			return getRuleContext(ValidateExprContext.class,0);
		}
		public AnnotateExprContext annotateExpr() {
			return getRuleContext(AnnotateExprContext.class,0);
		}
		public ValueExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitValueExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueExprContext valueExpr() throws RecognitionException {
		ValueExprContext _localctx = new ValueExprContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_valueExpr);
		try {
			setState(1151);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1148);
				((ValueExprContext)_localctx).simpleMap_expr = simpleMapExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1149);
				((ValueExprContext)_localctx).validate_expr = validateExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1150);
				((ValueExprContext)_localctx).annotate_expr = annotateExpr();
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

	public static class ValidateExprContext extends ParserRuleContext {
		public TerminalNode Kvalidate() { return getToken(JsoniqParser.Kvalidate, 0); }
		public TerminalNode Ktype() { return getToken(JsoniqParser.Ktype, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ValidateExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validateExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitValidateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidateExprContext validateExpr() throws RecognitionException {
		ValidateExprContext _localctx = new ValidateExprContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_validateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1153);
			match(Kvalidate);
			setState(1154);
			match(Ktype);
			setState(1155);
			sequenceType();
			setState(1156);
			match(T__5);
			setState(1157);
			expr();
			setState(1158);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotateExprContext extends ParserRuleContext {
		public TerminalNode Kannotate() { return getToken(JsoniqParser.Kannotate, 0); }
		public TerminalNode Ktype() { return getToken(JsoniqParser.Ktype, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AnnotateExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotateExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnnotateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotateExprContext annotateExpr() throws RecognitionException {
		AnnotateExprContext _localctx = new AnnotateExprContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_annotateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1160);
			match(Kannotate);
			setState(1161);
			match(Ktype);
			setState(1162);
			sequenceType();
			setState(1163);
			match(T__5);
			setState(1164);
			expr();
			setState(1165);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
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
		public PostFixExprContext main_expr;
		public PostFixExprContext postFixExpr;
		public List<PostFixExprContext> map_expr = new ArrayList<PostFixExprContext>();
		public List<PostFixExprContext> postFixExpr() {
			return getRuleContexts(PostFixExprContext.class);
		}
		public PostFixExprContext postFixExpr(int i) {
			return getRuleContext(PostFixExprContext.class,i);
		}
		public SimpleMapExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleMapExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSimpleMapExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleMapExprContext simpleMapExpr() throws RecognitionException {
		SimpleMapExprContext _localctx = new SimpleMapExprContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1167);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(1172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__49) {
				{
				{
				setState(1168);
				match(T__49);
				setState(1169);
				((SimpleMapExprContext)_localctx).postFixExpr = postFixExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).postFixExpr);
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

	public static class PostFixExprContext extends ParserRuleContext {
		public PrimaryExprContext main_expr;
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public List<ArrayLookupContext> arrayLookup() {
			return getRuleContexts(ArrayLookupContext.class);
		}
		public ArrayLookupContext arrayLookup(int i) {
			return getRuleContext(ArrayLookupContext.class,i);
		}
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public List<ObjectLookupContext> objectLookup() {
			return getRuleContexts(ObjectLookupContext.class);
		}
		public ObjectLookupContext objectLookup(int i) {
			return getRuleContext(ObjectLookupContext.class,i);
		}
		public List<ArrayUnboxingContext> arrayUnboxing() {
			return getRuleContexts(ArrayUnboxingContext.class);
		}
		public ArrayUnboxingContext arrayUnboxing(int i) {
			return getRuleContext(ArrayUnboxingContext.class,i);
		}
		public List<ArgumentListContext> argumentList() {
			return getRuleContexts(ArgumentListContext.class);
		}
		public ArgumentListContext argumentList(int i) {
			return getRuleContext(ArgumentListContext.class,i);
		}
		public PostFixExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postFixExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPostFixExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostFixExprContext postFixExpr() throws RecognitionException {
		PostFixExprContext _localctx = new PostFixExprContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1175);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(1183);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1181);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
					case 1:
						{
						setState(1176);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(1177);
						predicate();
						}
						break;
					case 3:
						{
						setState(1178);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(1179);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(1180);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(1185);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
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

	public static class ArrayLookupContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ArrayLookupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayLookup; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayLookupContext arrayLookup() throws RecognitionException {
		ArrayLookupContext _localctx = new ArrayLookupContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1186);
			match(T__50);
			setState(1187);
			match(T__50);
			setState(1188);
			expr();
			setState(1189);
			match(T__51);
			setState(1190);
			match(T__51);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayUnboxingContext extends ParserRuleContext {
		public ArrayUnboxingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayUnboxing; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayUnboxing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayUnboxingContext arrayUnboxing() throws RecognitionException {
		ArrayUnboxingContext _localctx = new ArrayUnboxingContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1192);
			match(T__50);
			setState(1193);
			match(T__51);
			}
		}
		catch (RecognitionException re) {
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
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1195);
			match(T__50);
			setState(1196);
			expr();
			setState(1197);
			match(T__51);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectLookupContext extends ParserRuleContext {
		public KeyWordsContext kw;
		public StringLiteralContext lt;
		public Token nc;
		public ParenthesizedExprContext pe;
		public VarRefContext vr;
		public ContextItemExprContext ci;
		public KeyWordsContext keyWords() {
			return getRuleContext(KeyWordsContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public ParenthesizedExprContext parenthesizedExpr() {
			return getRuleContext(ParenthesizedExprContext.class,0);
		}
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public ContextItemExprContext contextItemExpr() {
			return getRuleContext(ContextItemExprContext.class,0);
		}
		public ObjectLookupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectLookup; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitObjectLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectLookupContext objectLookup() throws RecognitionException {
		ObjectLookupContext _localctx = new ObjectLookupContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			match(T__52);
			setState(1206);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
				{
				setState(1200);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(1201);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(1202);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__7:
				{
				setState(1203);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__3:
				{
				setState(1204);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__53:
				{
				setState(1205);
				((ObjectLookupContext)_localctx).ci = contextItemExpr();
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

	public static class PrimaryExprContext extends ParserRuleContext {
		public TerminalNode NullLiteral() { return getToken(JsoniqParser.NullLiteral, 0); }
		public TerminalNode Ktrue() { return getToken(JsoniqParser.Ktrue, 0); }
		public TerminalNode Kfalse() { return getToken(JsoniqParser.Kfalse, 0); }
		public TerminalNode Literal() { return getToken(JsoniqParser.Literal, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
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
		public ObjectConstructorContext objectConstructor() {
			return getRuleContext(ObjectConstructorContext.class,0);
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
		public ArrayConstructorContext arrayConstructor() {
			return getRuleContext(ArrayConstructorContext.class,0);
		}
		public FunctionItemExprContext functionItemExpr() {
			return getRuleContext(FunctionItemExprContext.class,0);
		}
		public BlockExprContext blockExpr() {
			return getRuleContext(BlockExprContext.class,0);
		}
		public PrimaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExprContext primaryExpr() throws RecognitionException {
		PrimaryExprContext _localctx = new PrimaryExprContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_primaryExpr);
		try {
			setState(1223);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1208);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1209);
				match(Ktrue);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1210);
				match(Kfalse);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1211);
				match(Literal);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1212);
				stringLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1213);
				varRef();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1214);
				parenthesizedExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1215);
				contextItemExpr();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1216);
				objectConstructor();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1217);
				functionCall();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1218);
				orderedExpr();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1219);
				unorderedExpr();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1220);
				arrayConstructor();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1221);
				functionItemExpr();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1222);
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

	public static class BlockExprContext extends ParserRuleContext {
		public StatementsAndExprContext statementsAndExpr() {
			return getRuleContext(StatementsAndExprContext.class,0);
		}
		public BlockExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBlockExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockExprContext blockExpr() throws RecognitionException {
		BlockExprContext _localctx = new BlockExprContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_blockExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1225);
			match(T__5);
			setState(1226);
			statementsAndExpr();
			setState(1227);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
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
		public QnameContext var_name;
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public VarRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varRef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarRefContext varRef() throws RecognitionException {
		VarRefContext _localctx = new VarRefContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1229);
			match(T__3);
			setState(1230);
			((VarRefContext)_localctx).var_name = qname();
			}
		}
		catch (RecognitionException re) {
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
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParenthesizedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenthesizedExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParenthesizedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenthesizedExprContext parenthesizedExpr() throws RecognitionException {
		ParenthesizedExprContext _localctx = new ParenthesizedExprContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1232);
			match(T__7);
			setState(1234);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1233);
				expr();
				}
			}

			setState(1236);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public ContextItemExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextItemExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContextItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemExprContext contextItemExpr() throws RecognitionException {
		ContextItemExprContext _localctx = new ContextItemExprContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1238);
			match(T__53);
			}
		}
		catch (RecognitionException re) {
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
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public OrderedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderedExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderedExprContext orderedExpr() throws RecognitionException {
		OrderedExprContext _localctx = new OrderedExprContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1240);
			match(T__14);
			setState(1241);
			match(T__5);
			setState(1242);
			expr();
			setState(1243);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kunordered() { return getToken(JsoniqParser.Kunordered, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnorderedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unorderedExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUnorderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnorderedExprContext unorderedExpr() throws RecognitionException {
		UnorderedExprContext _localctx = new UnorderedExprContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1245);
			match(Kunordered);
			setState(1246);
			match(T__5);
			setState(1247);
			expr();
			setState(1248);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
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
		public QnameContext fn_name;
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1250);
			((FunctionCallContext)_localctx).fn_name = qname();
			setState(1251);
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

	public static class ArgumentListContext extends ParserRuleContext {
		public ArgumentContext argument;
		public List<ArgumentContext> args = new ArrayList<ArgumentContext>();
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1253);
			match(T__7);
			setState(1260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (ArgumentPlaceholder - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				{
				setState(1254);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(1256);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1255);
					match(T__12);
					}
				}

				}
				}
				setState(1262);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1263);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode ArgumentPlaceholder() { return getToken(JsoniqParser.ArgumentPlaceholder, 0); }
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_argument);
		try {
			setState(1267);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
			case T__5:
			case T__7:
			case T__11:
			case T__14:
			case T__28:
			case T__44:
			case T__45:
			case T__50:
			case T__53:
			case T__56:
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1265);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(1266);
				match(ArgumentPlaceholder);
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

	public static class FunctionItemExprContext extends ParserRuleContext {
		public NamedFunctionRefContext namedFunctionRef() {
			return getRuleContext(NamedFunctionRefContext.class,0);
		}
		public InlineFunctionExprContext inlineFunctionExpr() {
			return getRuleContext(InlineFunctionExprContext.class,0);
		}
		public FunctionItemExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionItemExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionItemExprContext functionItemExpr() throws RecognitionException {
		FunctionItemExprContext _localctx = new FunctionItemExprContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_functionItemExpr);
		try {
			setState(1271);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1269);
				namedFunctionRef();
				}
				break;
			case T__11:
			case T__28:
				enterOuterAlt(_localctx, 2);
				{
				setState(1270);
				inlineFunctionExpr();
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

	public static class NamedFunctionRefContext extends ParserRuleContext {
		public QnameContext fn_name;
		public Token arity;
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TerminalNode Literal() { return getToken(JsoniqParser.Literal, 0); }
		public NamedFunctionRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedFunctionRef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNamedFunctionRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedFunctionRefContext namedFunctionRef() throws RecognitionException {
		NamedFunctionRefContext _localctx = new NamedFunctionRefContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1273);
			((NamedFunctionRefContext)_localctx).fn_name = qname();
			setState(1274);
			match(T__54);
			setState(1275);
			((NamedFunctionRefContext)_localctx).arity = match(Literal);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InlineFunctionExprContext extends ParserRuleContext {
		public SequenceTypeContext return_type;
		public StatementsAndOptionalExprContext fn_body;
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public StatementsAndOptionalExprContext statementsAndOptionalExpr() {
			return getRuleContext(StatementsAndOptionalExprContext.class,0);
		}
		public InlineFunctionExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineFunctionExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInlineFunctionExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineFunctionExprContext inlineFunctionExpr() throws RecognitionException {
		InlineFunctionExprContext _localctx = new InlineFunctionExprContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1277);
			annotations();
			setState(1278);
			match(T__28);
			setState(1279);
			match(T__7);
			setState(1281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(1280);
				paramList();
				}
			}

			setState(1283);
			match(T__8);
			setState(1286);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(1284);
				match(Kas);
				setState(1285);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(1288);
			match(T__5);
			{
			setState(1289);
			((InlineFunctionExprContext)_localctx).fn_body = statementsAndOptionalExpr();
			}
			setState(1290);
			match(T__6);
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

	public static class InsertExprContext extends ParserRuleContext {
		public ExprSingleContext to_insert_expr;
		public ExprSingleContext main_expr;
		public ExprSingleContext pos_expr;
		public TerminalNode Kinsert() { return getToken(JsoniqParser.Kinsert, 0); }
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public TerminalNode Kinto() { return getToken(JsoniqParser.Kinto, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public TerminalNode Kat() { return getToken(JsoniqParser.Kat, 0); }
		public TerminalNode Kposition() { return getToken(JsoniqParser.Kposition, 0); }
		public List<PairConstructorContext> pairConstructor() {
			return getRuleContexts(PairConstructorContext.class);
		}
		public PairConstructorContext pairConstructor(int i) {
			return getRuleContext(PairConstructorContext.class,i);
		}
		public InsertExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInsertExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertExprContext insertExpr() throws RecognitionException {
		InsertExprContext _localctx = new InsertExprContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_insertExpr);
		int _la;
		try {
			setState(1315);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1292);
				match(Kinsert);
				setState(1293);
				match(Kjson);
				setState(1294);
				((InsertExprContext)_localctx).to_insert_expr = exprSingle();
				setState(1295);
				match(Kinto);
				setState(1296);
				((InsertExprContext)_localctx).main_expr = exprSingle();
				setState(1300);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
				case 1:
					{
					setState(1297);
					match(Kat);
					setState(1298);
					match(Kposition);
					setState(1299);
					((InsertExprContext)_localctx).pos_expr = exprSingle();
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1302);
				match(Kinsert);
				setState(1303);
				match(Kjson);
				setState(1304);
				pairConstructor();
				setState(1309);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(1305);
					match(T__12);
					setState(1306);
					pairConstructor();
					}
					}
					setState(1311);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1312);
				match(Kinto);
				setState(1313);
				((InsertExprContext)_localctx).main_expr = exprSingle();
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

	public static class DeleteExprContext extends ParserRuleContext {
		public TerminalNode Kdelete() { return getToken(JsoniqParser.Kdelete, 0); }
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public UpdateLocatorContext updateLocator() {
			return getRuleContext(UpdateLocatorContext.class,0);
		}
		public DeleteExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDeleteExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteExprContext deleteExpr() throws RecognitionException {
		DeleteExprContext _localctx = new DeleteExprContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_deleteExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1317);
			match(Kdelete);
			setState(1318);
			match(Kjson);
			setState(1319);
			updateLocator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RenameExprContext extends ParserRuleContext {
		public ExprSingleContext name_expr;
		public TerminalNode Krename() { return getToken(JsoniqParser.Krename, 0); }
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public UpdateLocatorContext updateLocator() {
			return getRuleContext(UpdateLocatorContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public RenameExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_renameExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitRenameExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RenameExprContext renameExpr() throws RecognitionException {
		RenameExprContext _localctx = new RenameExprContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_renameExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1321);
			match(Krename);
			setState(1322);
			match(Kjson);
			setState(1323);
			updateLocator();
			setState(1324);
			match(Kas);
			setState(1325);
			((RenameExprContext)_localctx).name_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReplaceExprContext extends ParserRuleContext {
		public ExprSingleContext replacer_expr;
		public TerminalNode Kreplace() { return getToken(JsoniqParser.Kreplace, 0); }
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public TerminalNode Kvalue() { return getToken(JsoniqParser.Kvalue, 0); }
		public TerminalNode Kof() { return getToken(JsoniqParser.Kof, 0); }
		public UpdateLocatorContext updateLocator() {
			return getRuleContext(UpdateLocatorContext.class,0);
		}
		public TerminalNode Kwith() { return getToken(JsoniqParser.Kwith, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ReplaceExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_replaceExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitReplaceExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReplaceExprContext replaceExpr() throws RecognitionException {
		ReplaceExprContext _localctx = new ReplaceExprContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_replaceExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1327);
			match(Kreplace);
			setState(1328);
			match(Kjson);
			setState(1329);
			match(Kvalue);
			setState(1330);
			match(Kof);
			setState(1331);
			updateLocator();
			setState(1332);
			match(Kwith);
			setState(1333);
			((ReplaceExprContext)_localctx).replacer_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TransformExprContext extends ParserRuleContext {
		public ExprSingleContext mod_expr;
		public ExprSingleContext ret_expr;
		public TerminalNode Kcopy() { return getToken(JsoniqParser.Kcopy, 0); }
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public List<CopyDeclContext> copyDecl() {
			return getRuleContexts(CopyDeclContext.class);
		}
		public CopyDeclContext copyDecl(int i) {
			return getRuleContext(CopyDeclContext.class,i);
		}
		public TerminalNode Kmodify() { return getToken(JsoniqParser.Kmodify, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public TransformExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transformExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTransformExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransformExprContext transformExpr() throws RecognitionException {
		TransformExprContext _localctx = new TransformExprContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_transformExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1335);
			match(Kcopy);
			setState(1336);
			match(Kjson);
			setState(1337);
			copyDecl();
			setState(1342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(1338);
				match(T__12);
				setState(1339);
				copyDecl();
				}
				}
				setState(1344);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1345);
			match(Kmodify);
			setState(1346);
			((TransformExprContext)_localctx).mod_expr = exprSingle();
			setState(1347);
			match(Kreturn);
			setState(1348);
			((TransformExprContext)_localctx).ret_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AppendExprContext extends ParserRuleContext {
		public ExprSingleContext to_append_expr;
		public ExprSingleContext array_expr;
		public TerminalNode Kappend() { return getToken(JsoniqParser.Kappend, 0); }
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public TerminalNode Kinto() { return getToken(JsoniqParser.Kinto, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public AppendExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_appendExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAppendExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AppendExprContext appendExpr() throws RecognitionException {
		AppendExprContext _localctx = new AppendExprContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_appendExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1350);
			match(Kappend);
			setState(1351);
			match(Kjson);
			setState(1352);
			((AppendExprContext)_localctx).to_append_expr = exprSingle();
			setState(1353);
			match(Kinto);
			setState(1354);
			((AppendExprContext)_localctx).array_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UpdateLocatorContext extends ParserRuleContext {
		public PrimaryExprContext main_expr;
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public List<ArrayLookupContext> arrayLookup() {
			return getRuleContexts(ArrayLookupContext.class);
		}
		public ArrayLookupContext arrayLookup(int i) {
			return getRuleContext(ArrayLookupContext.class,i);
		}
		public List<ObjectLookupContext> objectLookup() {
			return getRuleContexts(ObjectLookupContext.class);
		}
		public ObjectLookupContext objectLookup(int i) {
			return getRuleContext(ObjectLookupContext.class,i);
		}
		public UpdateLocatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_updateLocator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUpdateLocator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateLocatorContext updateLocator() throws RecognitionException {
		UpdateLocatorContext _localctx = new UpdateLocatorContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_updateLocator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1356);
			((UpdateLocatorContext)_localctx).main_expr = primaryExpr();
			setState(1359); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(1359);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__50:
						{
						setState(1357);
						arrayLookup();
						}
						break;
					case T__52:
						{
						setState(1358);
						objectLookup();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1361); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,119,_ctx);
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

	public static class CopyDeclContext extends ParserRuleContext {
		public VarRefContext var_ref;
		public ExprSingleContext src_expr;
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public CopyDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_copyDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCopyDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CopyDeclContext copyDecl() throws RecognitionException {
		CopyDeclContext _localctx = new CopyDeclContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_copyDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1363);
			((CopyDeclContext)_localctx).var_ref = varRef();
			setState(1364);
			match(T__4);
			setState(1365);
			((CopyDeclContext)_localctx).src_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kimport() { return getToken(JsoniqParser.Kimport, 0); }
		public TerminalNode Kschema() { return getToken(JsoniqParser.Kschema, 0); }
		public List<UriLiteralContext> uriLiteral() {
			return getRuleContexts(UriLiteralContext.class);
		}
		public UriLiteralContext uriLiteral(int i) {
			return getRuleContext(UriLiteralContext.class,i);
		}
		public SchemaPrefixContext schemaPrefix() {
			return getRuleContext(SchemaPrefixContext.class,0);
		}
		public TerminalNode Kat() { return getToken(JsoniqParser.Kat, 0); }
		public SchemaImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaImport; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSchemaImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaImportContext schemaImport() throws RecognitionException {
		SchemaImportContext _localctx = new SchemaImportContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_schemaImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1367);
			match(Kimport);
			setState(1368);
			match(Kschema);
			setState(1370);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kdefault || _la==Knamespace) {
				{
				setState(1369);
				schemaPrefix();
				}
			}

			setState(1372);
			((SchemaImportContext)_localctx).nsURI = uriLiteral();
			setState(1382);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(1373);
				match(Kat);
				setState(1374);
				((SchemaImportContext)_localctx).uriLiteral = uriLiteral();
				((SchemaImportContext)_localctx).locations.add(((SchemaImportContext)_localctx).uriLiteral);
				setState(1379);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(1375);
					match(T__12);
					setState(1376);
					((SchemaImportContext)_localctx).uriLiteral = uriLiteral();
					((SchemaImportContext)_localctx).locations.add(((SchemaImportContext)_localctx).uriLiteral);
					}
					}
					setState(1381);
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
		public TerminalNode Knamespace() { return getToken(JsoniqParser.Knamespace, 0); }
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kelement() { return getToken(JsoniqParser.Kelement, 0); }
		public SchemaPrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaPrefix; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSchemaPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaPrefixContext schemaPrefix() throws RecognitionException {
		SchemaPrefixContext _localctx = new SchemaPrefixContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_schemaPrefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1390);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Knamespace:
				{
				setState(1384);
				match(Knamespace);
				setState(1385);
				match(NCName);
				setState(1386);
				match(T__2);
				}
				break;
			case Kdefault:
				{
				setState(1387);
				match(Kdefault);
				setState(1388);
				match(Kelement);
				setState(1389);
				match(Knamespace);
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

	public static class PathExprContext extends ParserRuleContext {
		public RelativePathExprContext singleslash;
		public RelativePathExprContext doubleslash;
		public RelativePathExprContext relative;
		public TerminalNode Kslash() { return getToken(JsoniqParser.Kslash, 0); }
		public RelativePathExprContext relativePathExpr() {
			return getRuleContext(RelativePathExprContext.class,0);
		}
		public TerminalNode Kdslash() { return getToken(JsoniqParser.Kdslash, 0); }
		public PathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPathExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathExprContext pathExpr() throws RecognitionException {
		PathExprContext _localctx = new PathExprContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_pathExpr);
		int _la;
		try {
			setState(1399);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kslash:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1392);
				match(Kslash);
				setState(1394);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__50) | (1L << T__53) | (1L << T__55) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (Kelement - 128)) | (1L << (Kat_symbol - 128)) | (1L << (Kchild - 128)) | (1L << (Kdescendant - 128)) | (1L << (Kattribute - 128)) | (1L << (Kself - 128)) | (1L << (Kdescendant_or_self - 128)) | (1L << (Kfollowing_sibling - 128)) | (1L << (Kfollowing - 128)) | (1L << (Kparent - 128)) | (1L << (Kancestor - 128)) | (1L << (Kpreceding_sibling - 128)) | (1L << (Kpreceding - 128)) | (1L << (Kancestor_or_self - 128)) | (1L << (Knode - 128)) | (1L << (Kbinary - 128)) | (1L << (Kdocument_node - 128)) | (1L << (Ktext - 128)) | (1L << (Kpi - 128)) | (1L << (Knamespace_node - 128)) | (1L << (Kschema_attribute - 128)) | (1L << (Kschema_element - 128)) | (1L << (Karray_node - 128)) | (1L << (Kboolean_node - 128)) | (1L << (Knull_node - 128)) | (1L << (Knumber_node - 128)) | (1L << (Kobject_node - 128)) | (1L << (Kcomment - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
					{
					setState(1393);
					((PathExprContext)_localctx).singleslash = relativePathExpr();
					}
				}

				}
				}
				break;
			case Kdslash:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1396);
				match(Kdslash);
				setState(1397);
				((PathExprContext)_localctx).doubleslash = relativePathExpr();
				}
				}
				break;
			case T__3:
			case T__5:
			case T__7:
			case T__11:
			case T__14:
			case T__28:
			case T__50:
			case T__53:
			case T__55:
			case T__56:
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case Kelement:
			case Kat_symbol:
			case Kchild:
			case Kdescendant:
			case Kattribute:
			case Kself:
			case Kdescendant_or_self:
			case Kfollowing_sibling:
			case Kfollowing:
			case Kparent:
			case Kancestor:
			case Kpreceding_sibling:
			case Kpreceding:
			case Kancestor_or_self:
			case Knode:
			case Kbinary:
			case Kdocument_node:
			case Ktext:
			case Kpi:
			case Knamespace_node:
			case Kschema_attribute:
			case Kschema_element:
			case Karray_node:
			case Kboolean_node:
			case Knull_node:
			case Knumber_node:
			case Kobject_node:
			case Kcomment:
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				enterOuterAlt(_localctx, 3);
				{
				setState(1398);
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
		public List<TerminalNode> Kslash() { return getTokens(JsoniqParser.Kslash); }
		public TerminalNode Kslash(int i) {
			return getToken(JsoniqParser.Kslash, i);
		}
		public List<TerminalNode> Kdslash() { return getTokens(JsoniqParser.Kdslash); }
		public TerminalNode Kdslash(int i) {
			return getToken(JsoniqParser.Kdslash, i);
		}
		public RelativePathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relativePathExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitRelativePathExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelativePathExprContext relativePathExpr() throws RecognitionException {
		RelativePathExprContext _localctx = new RelativePathExprContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_relativePathExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1401);
			stepExpr();
			setState(1406);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Kslash || _la==Kdslash) {
				{
				{
				setState(1402);
				((RelativePathExprContext)_localctx).sep = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==Kslash || _la==Kdslash) ) {
					((RelativePathExprContext)_localctx).sep = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1403);
				stepExpr();
				}
				}
				setState(1408);
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

	public static class StepExprContext extends ParserRuleContext {
		public PostFixExprContext postFixExpr() {
			return getRuleContext(PostFixExprContext.class,0);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStepExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StepExprContext stepExpr() throws RecognitionException {
		StepExprContext _localctx = new StepExprContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_stepExpr);
		try {
			setState(1411);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1409);
				postFixExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1410);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAxisStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AxisStepContext axisStep() throws RecognitionException {
		AxisStepContext _localctx = new AxisStepContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_axisStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1415);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__55:
			case Kparent:
			case Kancestor:
			case Kpreceding_sibling:
			case Kpreceding:
			case Kancestor_or_self:
				{
				setState(1413);
				reverseStep();
				}
				break;
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case Kelement:
			case Kat_symbol:
			case Kchild:
			case Kdescendant:
			case Kattribute:
			case Kself:
			case Kdescendant_or_self:
			case Kfollowing_sibling:
			case Kfollowing:
			case Knode:
			case Kbinary:
			case Kdocument_node:
			case Ktext:
			case Kpi:
			case Knamespace_node:
			case Kschema_attribute:
			case Kschema_element:
			case Karray_node:
			case Kboolean_node:
			case Knull_node:
			case Knumber_node:
			case Kobject_node:
			case Kcomment:
			case NullLiteral:
			case NCName:
				{
				setState(1414);
				forwardStep();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1417);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitForwardStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForwardStepContext forwardStep() throws RecognitionException {
		ForwardStepContext _localctx = new ForwardStepContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_forwardStep);
		try {
			setState(1423);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1419);
				forwardAxis();
				setState(1420);
				nodeTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1422);
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
		public TerminalNode Kchild() { return getToken(JsoniqParser.Kchild, 0); }
		public TerminalNode Kdescendant() { return getToken(JsoniqParser.Kdescendant, 0); }
		public TerminalNode Kattribute() { return getToken(JsoniqParser.Kattribute, 0); }
		public TerminalNode Kself() { return getToken(JsoniqParser.Kself, 0); }
		public TerminalNode Kdescendant_or_self() { return getToken(JsoniqParser.Kdescendant_or_self, 0); }
		public TerminalNode Kfollowing_sibling() { return getToken(JsoniqParser.Kfollowing_sibling, 0); }
		public TerminalNode Kfollowing() { return getToken(JsoniqParser.Kfollowing, 0); }
		public ForwardAxisContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forwardAxis; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitForwardAxis(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForwardAxisContext forwardAxis() throws RecognitionException {
		ForwardAxisContext _localctx = new ForwardAxisContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_forwardAxis);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1425);
			_la = _input.LA(1);
			if ( !(((((_la - 138)) & ~0x3f) == 0 && ((1L << (_la - 138)) & ((1L << (Kchild - 138)) | (1L << (Kdescendant - 138)) | (1L << (Kattribute - 138)) | (1L << (Kself - 138)) | (1L << (Kdescendant_or_self - 138)) | (1L << (Kfollowing_sibling - 138)) | (1L << (Kfollowing - 138)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1426);
			match(T__16);
			setState(1427);
			match(T__16);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kat_symbol() { return getToken(JsoniqParser.Kat_symbol, 0); }
		public AbbrevForwardStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abbrevForwardStep; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAbbrevForwardStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbbrevForwardStepContext abbrevForwardStep() throws RecognitionException {
		AbbrevForwardStepContext _localctx = new AbbrevForwardStepContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_abbrevForwardStep);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat_symbol) {
				{
				setState(1429);
				match(Kat_symbol);
				}
			}

			setState(1432);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitReverseStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReverseStepContext reverseStep() throws RecognitionException {
		ReverseStepContext _localctx = new ReverseStepContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_reverseStep);
		try {
			setState(1438);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kparent:
			case Kancestor:
			case Kpreceding_sibling:
			case Kpreceding:
			case Kancestor_or_self:
				enterOuterAlt(_localctx, 1);
				{
				setState(1434);
				reverseAxis();
				setState(1435);
				nodeTest();
				}
				break;
			case T__55:
				enterOuterAlt(_localctx, 2);
				{
				setState(1437);
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
		public TerminalNode Kparent() { return getToken(JsoniqParser.Kparent, 0); }
		public TerminalNode Kancestor() { return getToken(JsoniqParser.Kancestor, 0); }
		public TerminalNode Kpreceding_sibling() { return getToken(JsoniqParser.Kpreceding_sibling, 0); }
		public TerminalNode Kpreceding() { return getToken(JsoniqParser.Kpreceding, 0); }
		public TerminalNode Kancestor_or_self() { return getToken(JsoniqParser.Kancestor_or_self, 0); }
		public ReverseAxisContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reverseAxis; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitReverseAxis(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReverseAxisContext reverseAxis() throws RecognitionException {
		ReverseAxisContext _localctx = new ReverseAxisContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_reverseAxis);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1440);
			_la = _input.LA(1);
			if ( !(((((_la - 145)) & ~0x3f) == 0 && ((1L << (_la - 145)) & ((1L << (Kparent - 145)) | (1L << (Kancestor - 145)) | (1L << (Kpreceding_sibling - 145)) | (1L << (Kpreceding - 145)) | (1L << (Kancestor_or_self - 145)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1441);
			match(T__16);
			setState(1442);
			match(T__16);
			}
		}
		catch (RecognitionException re) {
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
		public AbbrevReverseStepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abbrevReverseStep; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAbbrevReverseStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbbrevReverseStepContext abbrevReverseStep() throws RecognitionException {
		AbbrevReverseStepContext _localctx = new AbbrevReverseStepContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_abbrevReverseStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1444);
			match(T__55);
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeTestContext nodeTest() throws RecognitionException {
		NodeTestContext _localctx = new NodeTestContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_nodeTest);
		try {
			setState(1448);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1446);
				nameTest();
				}
				break;
			case Kelement:
			case Kattribute:
			case Knode:
			case Kbinary:
			case Kdocument_node:
			case Ktext:
			case Kpi:
			case Knamespace_node:
			case Kschema_attribute:
			case Kschema_element:
			case Karray_node:
			case Kboolean_node:
			case Knull_node:
			case Knumber_node:
			case Kobject_node:
			case Kcomment:
				enterOuterAlt(_localctx, 2);
				{
				setState(1447);
				kindTest();
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

	public static class NameTestContext extends ParserRuleContext {
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNameTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameTestContext nameTest() throws RecognitionException {
		NameTestContext _localctx = new NameTestContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_nameTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1450);
			qname();
			setState(1451);
			wildcard();
			}
		}
		catch (RecognitionException re) {
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
		public AllNamesContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAllNames(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AllWithLocalContext extends WildcardContext {
		public NCNameWithPrefixWildcardContext nCNameWithPrefixWildcard() {
			return getRuleContext(NCNameWithPrefixWildcardContext.class,0);
		}
		public AllWithLocalContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAllWithLocal(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AllWithNSContext extends WildcardContext {
		public NCNameWithLocalWildcardContext nCNameWithLocalWildcard() {
			return getRuleContext(NCNameWithLocalWildcardContext.class,0);
		}
		public AllWithNSContext(WildcardContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAllWithNS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WildcardContext wildcard() throws RecognitionException {
		WildcardContext _localctx = new WildcardContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_wildcard);
		try {
			setState(1456);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				_localctx = new AllNamesContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1453);
				match(T__9);
				}
				break;
			case 2:
				_localctx = new AllWithNSContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1454);
				nCNameWithLocalWildcard();
				}
				break;
			case 3:
				_localctx = new AllWithLocalContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1455);
				nCNameWithPrefixWildcard();
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

	public static class NCNameWithLocalWildcardContext extends ParserRuleContext {
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public NCNameWithLocalWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nCNameWithLocalWildcard; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNCNameWithLocalWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NCNameWithLocalWildcardContext nCNameWithLocalWildcard() throws RecognitionException {
		NCNameWithLocalWildcardContext _localctx = new NCNameWithLocalWildcardContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_nCNameWithLocalWildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1458);
			match(NCName);
			setState(1459);
			match(T__16);
			setState(1460);
			match(T__9);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NCNameWithPrefixWildcardContext extends ParserRuleContext {
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public NCNameWithPrefixWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nCNameWithPrefixWildcard; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNCNameWithPrefixWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NCNameWithPrefixWildcardContext nCNameWithPrefixWildcard() throws RecognitionException {
		NCNameWithPrefixWildcardContext _localctx = new NCNameWithPrefixWildcardContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_nCNameWithPrefixWildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1462);
			match(T__9);
			setState(1463);
			match(T__16);
			setState(1464);
			match(NCName);
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPredicateList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateListContext predicateList() throws RecognitionException {
		PredicateListContext _localctx = new PredicateListContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_predicateList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1469);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__50) {
				{
				{
				setState(1466);
				predicate();
				}
				}
				setState(1471);
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

	public static class ItemTypeContext extends ParserRuleContext {
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TerminalNode NullLiteral() { return getToken(JsoniqParser.NullLiteral, 0); }
		public KindTestContext kindTest() {
			return getRuleContext(KindTestContext.class,0);
		}
		public TerminalNode Kitem() { return getToken(JsoniqParser.Kitem, 0); }
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitItemType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ItemTypeContext itemType() throws RecognitionException {
		ItemTypeContext _localctx = new ItemTypeContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_itemType);
		try {
			setState(1483);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1472);
				qname();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1473);
				match(NullLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1474);
				kindTest();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				{
				setState(1475);
				match(Kitem);
				setState(1476);
				match(T__7);
				setState(1477);
				match(T__8);
				}
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1478);
				functionTest();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1479);
				mapTest();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1480);
				arrayTest();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1481);
				atomicOrUnionType();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1482);
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
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public AtomicOrUnionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomicOrUnionType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAtomicOrUnionType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomicOrUnionTypeContext atomicOrUnionType() throws RecognitionException {
		AtomicOrUnionTypeContext _localctx = new AtomicOrUnionTypeContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_atomicOrUnionType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1485);
			qname();
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKindTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KindTestContext kindTest() throws RecognitionException {
		KindTestContext _localctx = new KindTestContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_kindTest);
		try {
			setState(1499);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kdocument_node:
				enterOuterAlt(_localctx, 1);
				{
				setState(1487);
				documentTest();
				}
				break;
			case Kelement:
				enterOuterAlt(_localctx, 2);
				{
				setState(1488);
				elementTest();
				}
				break;
			case Kattribute:
				enterOuterAlt(_localctx, 3);
				{
				setState(1489);
				attributeTest();
				}
				break;
			case Kschema_element:
				enterOuterAlt(_localctx, 4);
				{
				setState(1490);
				schemaElementTest();
				}
				break;
			case Kschema_attribute:
				enterOuterAlt(_localctx, 5);
				{
				setState(1491);
				schemaAttributeTest();
				}
				break;
			case Kpi:
				enterOuterAlt(_localctx, 6);
				{
				setState(1492);
				piTest();
				}
				break;
			case Kcomment:
				enterOuterAlt(_localctx, 7);
				{
				setState(1493);
				commentTest();
				}
				break;
			case Ktext:
				enterOuterAlt(_localctx, 8);
				{
				setState(1494);
				textTest();
				}
				break;
			case Knamespace_node:
				enterOuterAlt(_localctx, 9);
				{
				setState(1495);
				namespaceNodeTest();
				}
				break;
			case Karray_node:
			case Kboolean_node:
			case Knull_node:
			case Knumber_node:
			case Kobject_node:
				enterOuterAlt(_localctx, 10);
				{
				setState(1496);
				mlNodeTest();
				}
				break;
			case Kbinary:
				enterOuterAlt(_localctx, 11);
				{
				setState(1497);
				binaryNodeTest();
				}
				break;
			case Knode:
				enterOuterAlt(_localctx, 12);
				{
				setState(1498);
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
		public TerminalNode Knode() { return getToken(JsoniqParser.Knode, 0); }
		public AnyKindTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyKindTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnyKindTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyKindTestContext anyKindTest() throws RecognitionException {
		AnyKindTestContext _localctx = new AnyKindTestContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_anyKindTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1501);
			match(Knode);
			setState(1502);
			match(T__7);
			setState(1504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(1503);
				match(T__9);
				}
			}

			setState(1506);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kbinary() { return getToken(JsoniqParser.Kbinary, 0); }
		public BinaryNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBinaryNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryNodeTestContext binaryNodeTest() throws RecognitionException {
		BinaryNodeTestContext _localctx = new BinaryNodeTestContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_binaryNodeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1508);
			match(Kbinary);
			setState(1509);
			match(T__7);
			setState(1510);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kdocument_node() { return getToken(JsoniqParser.Kdocument_node, 0); }
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDocumentTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentTestContext documentTest() throws RecognitionException {
		DocumentTestContext _localctx = new DocumentTestContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_documentTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1512);
			match(Kdocument_node);
			setState(1513);
			match(T__7);
			setState(1516);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kelement:
				{
				setState(1514);
				elementTest();
				}
				break;
			case Kschema_element:
				{
				setState(1515);
				schemaElementTest();
				}
				break;
			case T__8:
				break;
			default:
				break;
			}
			setState(1518);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Ktext() { return getToken(JsoniqParser.Ktext, 0); }
		public TextTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTextTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextTestContext textTest() throws RecognitionException {
		TextTestContext _localctx = new TextTestContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_textTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1520);
			match(Ktext);
			setState(1521);
			match(T__7);
			setState(1522);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kcomment() { return getToken(JsoniqParser.Kcomment, 0); }
		public CommentTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commentTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCommentTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentTestContext commentTest() throws RecognitionException {
		CommentTestContext _localctx = new CommentTestContext(_ctx, getState());
		enterRule(_localctx, 280, RULE_commentTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1524);
			match(Kcomment);
			setState(1525);
			match(T__7);
			setState(1526);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Knamespace_node() { return getToken(JsoniqParser.Knamespace_node, 0); }
		public NamespaceNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNamespaceNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceNodeTestContext namespaceNodeTest() throws RecognitionException {
		NamespaceNodeTestContext _localctx = new NamespaceNodeTestContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_namespaceNodeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1528);
			match(Knamespace_node);
			setState(1529);
			match(T__7);
			setState(1530);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kpi() { return getToken(JsoniqParser.Kpi, 0); }
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public PiTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_piTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPiTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PiTestContext piTest() throws RecognitionException {
		PiTestContext _localctx = new PiTestContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_piTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1532);
			match(Kpi);
			setState(1533);
			match(T__7);
			setState(1536);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				{
				setState(1534);
				match(NCName);
				}
				break;
			case STRING:
				{
				setState(1535);
				stringLiteral();
				}
				break;
			case T__8:
				break;
			default:
				break;
			}
			setState(1538);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kattribute() { return getToken(JsoniqParser.Kattribute, 0); }
		public AttributeNameOrWildcardContext attributeNameOrWildcard() {
			return getRuleContext(AttributeNameOrWildcardContext.class,0);
		}
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public AttributeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAttributeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeTestContext attributeTest() throws RecognitionException {
		AttributeTestContext _localctx = new AttributeTestContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_attributeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1540);
			match(Kattribute);
			setState(1541);
			match(T__7);
			setState(1547);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (NullLiteral - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1542);
				attributeNameOrWildcard();
				setState(1545);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1543);
					match(T__12);
					setState(1544);
					((AttributeTestContext)_localctx).type = typeName();
					}
				}

				}
			}

			setState(1549);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public AttributeNameOrWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeNameOrWildcard; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAttributeNameOrWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeNameOrWildcardContext attributeNameOrWildcard() throws RecognitionException {
		AttributeNameOrWildcardContext _localctx = new AttributeNameOrWildcardContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_attributeNameOrWildcard);
		try {
			setState(1553);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1551);
				attributeName();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(1552);
				match(T__9);
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
		public TerminalNode Kschema_attribute() { return getToken(JsoniqParser.Kschema_attribute, 0); }
		public AttributeDeclarationContext attributeDeclaration() {
			return getRuleContext(AttributeDeclarationContext.class,0);
		}
		public SchemaAttributeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaAttributeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSchemaAttributeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaAttributeTestContext schemaAttributeTest() throws RecognitionException {
		SchemaAttributeTestContext _localctx = new SchemaAttributeTestContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_schemaAttributeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1555);
			match(Kschema_attribute);
			setState(1556);
			match(T__7);
			setState(1557);
			attributeDeclaration();
			setState(1558);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TypeNameContext type;
		public Token optional;
		public TerminalNode Kelement() { return getToken(JsoniqParser.Kelement, 0); }
		public ElementNameOrWildcardContext elementNameOrWildcard() {
			return getRuleContext(ElementNameOrWildcardContext.class,0);
		}
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TerminalNode ArgumentPlaceholder() { return getToken(JsoniqParser.ArgumentPlaceholder, 0); }
		public ElementTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitElementTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementTestContext elementTest() throws RecognitionException {
		ElementTestContext _localctx = new ElementTestContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_elementTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1560);
			match(Kelement);
			setState(1561);
			match(T__7);
			setState(1570);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (NullLiteral - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1562);
				elementNameOrWildcard();
				setState(1568);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1563);
					match(T__12);
					setState(1564);
					((ElementTestContext)_localctx).type = typeName();
					setState(1566);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ArgumentPlaceholder) {
						{
						setState(1565);
						((ElementTestContext)_localctx).optional = match(ArgumentPlaceholder);
						}
					}

					}
				}

				}
			}

			setState(1572);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public ElementNameOrWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementNameOrWildcard; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitElementNameOrWildcard(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementNameOrWildcardContext elementNameOrWildcard() throws RecognitionException {
		ElementNameOrWildcardContext _localctx = new ElementNameOrWildcardContext(_ctx, getState());
		enterRule(_localctx, 294, RULE_elementNameOrWildcard);
		try {
			setState(1576);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
			case Klet:
			case Kwhere:
			case Kgroup:
			case Kby:
			case Korder:
			case Kreturn:
			case Kif:
			case Kin:
			case Kas:
			case Kat:
			case Kallowing:
			case Kempty:
			case Kcount:
			case Kstable:
			case Kascending:
			case Kdescending:
			case Ksome:
			case Kevery:
			case Ksatisfies:
			case Kcollation:
			case Kgreatest:
			case Kleast:
			case Kswitch:
			case Kcase:
			case Ktry:
			case Kcatch:
			case Kdefault:
			case Kthen:
			case Kelse:
			case Ktypeswitch:
			case Kor:
			case Kand:
			case Knot:
			case Kto:
			case Kinstance:
			case Kof:
			case Kstatically:
			case Kis:
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kunordered:
			case Ktrue:
			case Kfalse:
			case Ktype:
			case Kvalidate:
			case Kannotate:
			case Kdeclare:
			case Kcontext:
			case Kitem:
			case Kvariable:
			case Kinsert:
			case Kdelete:
			case Krename:
			case Kreplace:
			case Kcopy:
			case Kmodify:
			case Kappend:
			case Kinto:
			case Kvalue:
			case Kjson:
			case Kwith:
			case Kposition:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(1574);
				elementName();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(1575);
				match(T__9);
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
		public TerminalNode Kschema_element() { return getToken(JsoniqParser.Kschema_element, 0); }
		public ElementDeclarationContext elementDeclaration() {
			return getRuleContext(ElementDeclarationContext.class,0);
		}
		public SchemaElementTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaElementTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSchemaElementTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaElementTestContext schemaElementTest() throws RecognitionException {
		SchemaElementTestContext _localctx = new SchemaElementTestContext(_ctx, getState());
		enterRule(_localctx, 296, RULE_schemaElementTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1578);
			match(Kschema_element);
			setState(1579);
			match(T__7);
			setState(1580);
			elementDeclaration();
			setState(1581);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitElementDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementDeclarationContext elementDeclaration() throws RecognitionException {
		ElementDeclarationContext _localctx = new ElementDeclarationContext(_ctx, getState());
		enterRule(_localctx, 298, RULE_elementDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1583);
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
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAttributeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 300, RULE_attributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1585);
			qname();
			}
		}
		catch (RecognitionException re) {
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
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public ElementNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitElementName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementNameContext elementName() throws RecognitionException {
		ElementNameContext _localctx = new ElementNameContext(_ctx, getState());
		enterRule(_localctx, 302, RULE_elementName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1587);
			qname();
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSimpleTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTypeNameContext simpleTypeName() throws RecognitionException {
		SimpleTypeNameContext _localctx = new SimpleTypeNameContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_simpleTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1589);
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
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 306, RULE_typeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1591);
			qname();
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapTestContext mapTest() throws RecognitionException {
		MapTestContext _localctx = new MapTestContext(_ctx, getState());
		enterRule(_localctx, 308, RULE_mapTest);
		try {
			setState(1595);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,147,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1593);
				anyMapTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1594);
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
		public TerminalNode Kmap() { return getToken(JsoniqParser.Kmap, 0); }
		public AnyMapTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyMapTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnyMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyMapTestContext anyMapTest() throws RecognitionException {
		AnyMapTestContext _localctx = new AnyMapTestContext(_ctx, getState());
		enterRule(_localctx, 310, RULE_anyMapTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1597);
			match(Kmap);
			setState(1598);
			match(T__7);
			setState(1599);
			match(T__9);
			setState(1600);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kmap() { return getToken(JsoniqParser.Kmap, 0); }
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TypedMapTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedMapTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypedMapTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedMapTestContext typedMapTest() throws RecognitionException {
		TypedMapTestContext _localctx = new TypedMapTestContext(_ctx, getState());
		enterRule(_localctx, 312, RULE_typedMapTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1602);
			match(Kmap);
			setState(1603);
			match(T__7);
			setState(1604);
			qname();
			setState(1605);
			match(T__12);
			setState(1606);
			sequenceType();
			setState(1607);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTestContext arrayTest() throws RecognitionException {
		ArrayTestContext _localctx = new ArrayTestContext(_ctx, getState());
		enterRule(_localctx, 314, RULE_arrayTest);
		try {
			setState(1611);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1609);
				anyArrayTest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1610);
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
		public TerminalNode Karray() { return getToken(JsoniqParser.Karray, 0); }
		public AnyArrayTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyArrayTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnyArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyArrayTestContext anyArrayTest() throws RecognitionException {
		AnyArrayTestContext _localctx = new AnyArrayTestContext(_ctx, getState());
		enterRule(_localctx, 316, RULE_anyArrayTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1613);
			match(Karray);
			setState(1614);
			match(T__7);
			setState(1615);
			match(T__9);
			setState(1616);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Karray() { return getToken(JsoniqParser.Karray, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TypedArrayTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedArrayTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypedArrayTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedArrayTestContext typedArrayTest() throws RecognitionException {
		TypedArrayTestContext _localctx = new TypedArrayTestContext(_ctx, getState());
		enterRule(_localctx, 318, RULE_typedArrayTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1618);
			match(Karray);
			setState(1619);
			match(T__7);
			setState(1620);
			sequenceType();
			setState(1621);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public ParenthesizedItemTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenthesizedItemTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParenthesizedItemTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenthesizedItemTestContext parenthesizedItemTest() throws RecognitionException {
		ParenthesizedItemTestContext _localctx = new ParenthesizedItemTestContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_parenthesizedItemTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1623);
			match(T__7);
			setState(1624);
			itemType();
			setState(1625);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAttributeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeDeclarationContext attributeDeclaration() throws RecognitionException {
		AttributeDeclarationContext _localctx = new AttributeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_attributeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1627);
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMlNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNodeTestContext mlNodeTest() throws RecognitionException {
		MlNodeTestContext _localctx = new MlNodeTestContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_mlNodeTest);
		try {
			setState(1634);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Karray_node:
				enterOuterAlt(_localctx, 1);
				{
				setState(1629);
				mlArrayNodeTest();
				}
				break;
			case Kobject_node:
				enterOuterAlt(_localctx, 2);
				{
				setState(1630);
				mlObjectNodeTest();
				}
				break;
			case Knumber_node:
				enterOuterAlt(_localctx, 3);
				{
				setState(1631);
				mlNumberNodeTest();
				}
				break;
			case Kboolean_node:
				enterOuterAlt(_localctx, 4);
				{
				setState(1632);
				mlBooleanNodeTest();
				}
				break;
			case Knull_node:
				enterOuterAlt(_localctx, 5);
				{
				setState(1633);
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
		public TerminalNode Karray_node() { return getToken(JsoniqParser.Karray_node, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlArrayNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlArrayNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMlArrayNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlArrayNodeTestContext mlArrayNodeTest() throws RecognitionException {
		MlArrayNodeTestContext _localctx = new MlArrayNodeTestContext(_ctx, getState());
		enterRule(_localctx, 326, RULE_mlArrayNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1636);
			match(Karray_node);
			setState(1637);
			match(T__7);
			setState(1639);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(1638);
				stringLiteral();
				}
			}

			setState(1641);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kobject_node() { return getToken(JsoniqParser.Kobject_node, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlObjectNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlObjectNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMlObjectNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlObjectNodeTestContext mlObjectNodeTest() throws RecognitionException {
		MlObjectNodeTestContext _localctx = new MlObjectNodeTestContext(_ctx, getState());
		enterRule(_localctx, 328, RULE_mlObjectNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1643);
			match(Kobject_node);
			setState(1644);
			match(T__7);
			setState(1646);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(1645);
				stringLiteral();
				}
			}

			setState(1648);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Knumber_node() { return getToken(JsoniqParser.Knumber_node, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlNumberNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlNumberNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMlNumberNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNumberNodeTestContext mlNumberNodeTest() throws RecognitionException {
		MlNumberNodeTestContext _localctx = new MlNumberNodeTestContext(_ctx, getState());
		enterRule(_localctx, 330, RULE_mlNumberNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1650);
			match(Knumber_node);
			setState(1651);
			match(T__7);
			setState(1653);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(1652);
				stringLiteral();
				}
			}

			setState(1655);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Kboolean_node() { return getToken(JsoniqParser.Kboolean_node, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlBooleanNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlBooleanNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMlBooleanNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlBooleanNodeTestContext mlBooleanNodeTest() throws RecognitionException {
		MlBooleanNodeTestContext _localctx = new MlBooleanNodeTestContext(_ctx, getState());
		enterRule(_localctx, 332, RULE_mlBooleanNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1657);
			match(Kboolean_node);
			setState(1658);
			match(T__7);
			setState(1660);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(1659);
				stringLiteral();
				}
			}

			setState(1662);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode Knull_node() { return getToken(JsoniqParser.Knull_node, 0); }
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public MlNullNodeTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mlNullNodeTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMlNullNodeTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MlNullNodeTestContext mlNullNodeTest() throws RecognitionException {
		MlNullNodeTestContext _localctx = new MlNullNodeTestContext(_ctx, getState());
		enterRule(_localctx, 334, RULE_mlNullNodeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1664);
			match(Knull_node);
			setState(1665);
			match(T__7);
			setState(1667);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(1666);
				stringLiteral();
				}
			}

			setState(1669);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public Token s168;
		public List<Token> question = new ArrayList<Token>();
		public Token s10;
		public List<Token> star = new ArrayList<Token>();
		public Token s45;
		public List<Token> plus = new ArrayList<Token>();
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public TerminalNode ArgumentPlaceholder() { return getToken(JsoniqParser.ArgumentPlaceholder, 0); }
		public SequenceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSequenceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceTypeContext sequenceType() throws RecognitionException {
		SequenceTypeContext _localctx = new SequenceTypeContext(_ctx, getState());
		enterRule(_localctx, 336, RULE_sequenceType);
		try {
			setState(1679);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1671);
				match(T__7);
				setState(1672);
				match(T__8);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1673);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(1677);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
				case 1:
					{
					setState(1674);
					((SequenceTypeContext)_localctx).s168 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s168);
					}
					break;
				case 2:
					{
					setState(1675);
					((SequenceTypeContext)_localctx).s10 = match(T__9);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s10);
					}
					break;
				case 3:
					{
					setState(1676);
					((SequenceTypeContext)_localctx).s45 = match(T__44);
					((SequenceTypeContext)_localctx).plus.add(((SequenceTypeContext)_localctx).s45);
					}
					break;
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

	public static class ObjectConstructorContext extends ParserRuleContext {
		public Token s57;
		public List<Token> merge_operator = new ArrayList<Token>();
		public List<PairConstructorContext> pairConstructor() {
			return getRuleContexts(PairConstructorContext.class);
		}
		public PairConstructorContext pairConstructor(int i) {
			return getRuleContext(PairConstructorContext.class,i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ObjectConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitObjectConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectConstructorContext objectConstructor() throws RecognitionException {
		ObjectConstructorContext _localctx = new ObjectConstructorContext(_ctx, getState());
		enterRule(_localctx, 338, RULE_objectConstructor);
		int _la;
		try {
			setState(1697);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(1681);
				match(T__5);
				setState(1690);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
					{
					setState(1682);
					pairConstructor();
					setState(1687);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__12) {
						{
						{
						setState(1683);
						match(T__12);
						setState(1684);
						pairConstructor();
						}
						}
						setState(1689);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1692);
				match(T__6);
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 2);
				{
				setState(1693);
				((ObjectConstructorContext)_localctx).s57 = match(T__56);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s57);
				setState(1694);
				expr();
				setState(1695);
				match(T__57);
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

	public static class FunctionTestContext extends ParserRuleContext {
		public AnyFunctionTestContext anyFunctionTest() {
			return getRuleContext(AnyFunctionTestContext.class,0);
		}
		public TypedFunctionTestContext typedFunctionTest() {
			return getRuleContext(TypedFunctionTestContext.class,0);
		}
		public FunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionTestContext functionTest() throws RecognitionException {
		FunctionTestContext _localctx = new FunctionTestContext(_ctx, getState());
		enterRule(_localctx, 340, RULE_functionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1701);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
			case 1:
				{
				setState(1699);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(1700);
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
		public AnyFunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyFunctionTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnyFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyFunctionTestContext anyFunctionTest() throws RecognitionException {
		AnyFunctionTestContext _localctx = new AnyFunctionTestContext(_ctx, getState());
		enterRule(_localctx, 342, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1703);
			match(T__28);
			setState(1704);
			match(T__7);
			setState(1705);
			match(T__9);
			setState(1706);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
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
		public SequenceTypeContext sequenceType;
		public List<SequenceTypeContext> st = new ArrayList<SequenceTypeContext>();
		public SequenceTypeContext rt;
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public List<SequenceTypeContext> sequenceType() {
			return getRuleContexts(SequenceTypeContext.class);
		}
		public SequenceTypeContext sequenceType(int i) {
			return getRuleContext(SequenceTypeContext.class,i);
		}
		public TypedFunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedFunctionTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypedFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedFunctionTestContext typedFunctionTest() throws RecognitionException {
		TypedFunctionTestContext _localctx = new TypedFunctionTestContext(_ctx, getState());
		enterRule(_localctx, 344, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1708);
			match(T__28);
			setState(1709);
			match(T__7);
			setState(1718);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__28) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (Kelement - 128)) | (1L << (Kattribute - 128)) | (1L << (Knode - 128)) | (1L << (Kbinary - 128)) | (1L << (Kdocument_node - 128)) | (1L << (Ktext - 128)) | (1L << (Kpi - 128)) | (1L << (Knamespace_node - 128)) | (1L << (Kschema_attribute - 128)) | (1L << (Kschema_element - 128)) | (1L << (Karray_node - 128)) | (1L << (Kboolean_node - 128)) | (1L << (Knull_node - 128)) | (1L << (Knumber_node - 128)) | (1L << (Kobject_node - 128)) | (1L << (Kcomment - 128)) | (1L << (Karray - 128)) | (1L << (Kmap - 128)) | (1L << (NullLiteral - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1710);
				((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
				((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
				setState(1715);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(1711);
					match(T__12);
					setState(1712);
					((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
					((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
					}
					}
					setState(1717);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1720);
			match(T__8);
			setState(1721);
			match(Kas);
			setState(1722);
			((TypedFunctionTestContext)_localctx).rt = sequenceType();
			}
		}
		catch (RecognitionException re) {
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
		public ItemTypeContext item;
		public Token s168;
		public List<Token> question = new ArrayList<Token>();
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
		public TerminalNode ArgumentPlaceholder() { return getToken(JsoniqParser.ArgumentPlaceholder, 0); }
		public SingleTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSingleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleTypeContext singleType() throws RecognitionException {
		SingleTypeContext _localctx = new SingleTypeContext(_ctx, getState());
		enterRule(_localctx, 346, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1724);
			((SingleTypeContext)_localctx).item = itemType();
			setState(1726);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,163,_ctx) ) {
			case 1:
				{
				setState(1725);
				((SingleTypeContext)_localctx).s168 = match(ArgumentPlaceholder);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).s168);
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

	public static class PairConstructorContext extends ParserRuleContext {
		public ExprSingleContext lhs;
		public Token name;
		public ExprSingleContext rhs;
		public TerminalNode ArgumentPlaceholder() { return getToken(JsoniqParser.ArgumentPlaceholder, 0); }
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public PairConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pairConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPairConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairConstructorContext pairConstructor() throws RecognitionException {
		PairConstructorContext _localctx = new PairConstructorContext(_ctx, getState());
		enterRule(_localctx, 348, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1730);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,164,_ctx) ) {
			case 1:
				{
				setState(1728);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(1729);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(1732);
			_la = _input.LA(1);
			if ( !(_la==T__16 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1733);
			((PairConstructorContext)_localctx).rhs = exprSingle();
			}
		}
		catch (RecognitionException re) {
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
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayConstructor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayConstructorContext arrayConstructor() throws RecognitionException {
		ArrayConstructorContext _localctx = new ArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 350, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1735);
			match(T__50);
			setState(1737);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)) | (1L << (Kloop - 64)) | (1L << (Kcontinue - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1736);
				expr();
				}
			}

			setState(1739);
			match(T__51);
			}
		}
		catch (RecognitionException re) {
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
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUriLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UriLiteralContext uriLiteral() throws RecognitionException {
		UriLiteralContext _localctx = new UriLiteralContext(_ctx, getState());
		enterRule(_localctx, 352, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1741);
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

	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(JsoniqParser.STRING, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 354, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1743);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordsContext extends ParserRuleContext {
		public TerminalNode Kjsoniq() { return getToken(JsoniqParser.Kjsoniq, 0); }
		public TerminalNode Kand() { return getToken(JsoniqParser.Kand, 0); }
		public TerminalNode Kcast() { return getToken(JsoniqParser.Kcast, 0); }
		public TerminalNode Kcastable() { return getToken(JsoniqParser.Kcastable, 0); }
		public TerminalNode Kcollation() { return getToken(JsoniqParser.Kcollation, 0); }
		public TerminalNode Kcontext() { return getToken(JsoniqParser.Kcontext, 0); }
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kelse() { return getToken(JsoniqParser.Kelse, 0); }
		public TerminalNode Kgreatest() { return getToken(JsoniqParser.Kgreatest, 0); }
		public TerminalNode Kinstance() { return getToken(JsoniqParser.Kinstance, 0); }
		public TerminalNode Kstatically() { return getToken(JsoniqParser.Kstatically, 0); }
		public TerminalNode Kis() { return getToken(JsoniqParser.Kis, 0); }
		public TerminalNode Kitem() { return getToken(JsoniqParser.Kitem, 0); }
		public TerminalNode Kleast() { return getToken(JsoniqParser.Kleast, 0); }
		public TerminalNode Knot() { return getToken(JsoniqParser.Knot, 0); }
		public TerminalNode NullLiteral() { return getToken(JsoniqParser.NullLiteral, 0); }
		public TerminalNode Kof() { return getToken(JsoniqParser.Kof, 0); }
		public TerminalNode Kor() { return getToken(JsoniqParser.Kor, 0); }
		public TerminalNode Kthen() { return getToken(JsoniqParser.Kthen, 0); }
		public TerminalNode Kto() { return getToken(JsoniqParser.Kto, 0); }
		public TerminalNode Ktreat() { return getToken(JsoniqParser.Ktreat, 0); }
		public TerminalNode Ktypeswitch() { return getToken(JsoniqParser.Ktypeswitch, 0); }
		public TerminalNode Kversion() { return getToken(JsoniqParser.Kversion, 0); }
		public TerminalNode Kswitch() { return getToken(JsoniqParser.Kswitch, 0); }
		public TerminalNode Kcase() { return getToken(JsoniqParser.Kcase, 0); }
		public TerminalNode Ktry() { return getToken(JsoniqParser.Ktry, 0); }
		public TerminalNode Kcatch() { return getToken(JsoniqParser.Kcatch, 0); }
		public TerminalNode Ksome() { return getToken(JsoniqParser.Ksome, 0); }
		public TerminalNode Kevery() { return getToken(JsoniqParser.Kevery, 0); }
		public TerminalNode Ksatisfies() { return getToken(JsoniqParser.Ksatisfies, 0); }
		public TerminalNode Kstable() { return getToken(JsoniqParser.Kstable, 0); }
		public TerminalNode Kvariable() { return getToken(JsoniqParser.Kvariable, 0); }
		public TerminalNode Kascending() { return getToken(JsoniqParser.Kascending, 0); }
		public TerminalNode Kdescending() { return getToken(JsoniqParser.Kdescending, 0); }
		public TerminalNode Kempty() { return getToken(JsoniqParser.Kempty, 0); }
		public TerminalNode Kallowing() { return getToken(JsoniqParser.Kallowing, 0); }
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public TerminalNode Kat() { return getToken(JsoniqParser.Kat, 0); }
		public TerminalNode Kin() { return getToken(JsoniqParser.Kin, 0); }
		public TerminalNode Kif() { return getToken(JsoniqParser.Kif, 0); }
		public TerminalNode Kfor() { return getToken(JsoniqParser.Kfor, 0); }
		public TerminalNode Klet() { return getToken(JsoniqParser.Klet, 0); }
		public TerminalNode Kwhere() { return getToken(JsoniqParser.Kwhere, 0); }
		public TerminalNode Kgroup() { return getToken(JsoniqParser.Kgroup, 0); }
		public TerminalNode Kby() { return getToken(JsoniqParser.Kby, 0); }
		public TerminalNode Korder() { return getToken(JsoniqParser.Korder, 0); }
		public TerminalNode Kcount() { return getToken(JsoniqParser.Kcount, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public TerminalNode Kunordered() { return getToken(JsoniqParser.Kunordered, 0); }
		public TerminalNode Ktrue() { return getToken(JsoniqParser.Ktrue, 0); }
		public TerminalNode Kfalse() { return getToken(JsoniqParser.Kfalse, 0); }
		public TerminalNode Ktype() { return getToken(JsoniqParser.Ktype, 0); }
		public TerminalNode Kinsert() { return getToken(JsoniqParser.Kinsert, 0); }
		public TerminalNode Kdelete() { return getToken(JsoniqParser.Kdelete, 0); }
		public TerminalNode Krename() { return getToken(JsoniqParser.Krename, 0); }
		public TerminalNode Kreplace() { return getToken(JsoniqParser.Kreplace, 0); }
		public TerminalNode Kappend() { return getToken(JsoniqParser.Kappend, 0); }
		public TerminalNode Kcopy() { return getToken(JsoniqParser.Kcopy, 0); }
		public TerminalNode Kmodify() { return getToken(JsoniqParser.Kmodify, 0); }
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public TerminalNode Kinto() { return getToken(JsoniqParser.Kinto, 0); }
		public TerminalNode Kvalue() { return getToken(JsoniqParser.Kvalue, 0); }
		public TerminalNode Kwith() { return getToken(JsoniqParser.Kwith, 0); }
		public TerminalNode Kposition() { return getToken(JsoniqParser.Kposition, 0); }
		public TerminalNode Kvalidate() { return getToken(JsoniqParser.Kvalidate, 0); }
		public TerminalNode Kannotate() { return getToken(JsoniqParser.Kannotate, 0); }
		public TerminalNode Kbreak() { return getToken(JsoniqParser.Kbreak, 0); }
		public TerminalNode Kloop() { return getToken(JsoniqParser.Kloop, 0); }
		public TerminalNode Kcontinue() { return getToken(JsoniqParser.Kcontinue, 0); }
		public TerminalNode Kexit() { return getToken(JsoniqParser.Kexit, 0); }
		public TerminalNode Kreturning() { return getToken(JsoniqParser.Kreturning, 0); }
		public TerminalNode Kwhile() { return getToken(JsoniqParser.Kwhile, 0); }
		public KeyWordsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWords; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWords(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordsContext keyWords() throws RecognitionException {
		KeyWordsContext _localctx = new KeyWordsContext(_ctx, getState());
		enterRule(_localctx, 356, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1745);
			_la = _input.LA(1);
			if ( !(((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (Kfor - 59)) | (1L << (Klet - 59)) | (1L << (Kwhere - 59)) | (1L << (Kgroup - 59)) | (1L << (Kby - 59)) | (1L << (Korder - 59)) | (1L << (Kreturn - 59)) | (1L << (Kif - 59)) | (1L << (Kin - 59)) | (1L << (Kas - 59)) | (1L << (Kat - 59)) | (1L << (Kallowing - 59)) | (1L << (Kempty - 59)) | (1L << (Kcount - 59)) | (1L << (Kstable - 59)) | (1L << (Kascending - 59)) | (1L << (Kdescending - 59)) | (1L << (Ksome - 59)) | (1L << (Kevery - 59)) | (1L << (Ksatisfies - 59)) | (1L << (Kcollation - 59)) | (1L << (Kgreatest - 59)) | (1L << (Kleast - 59)) | (1L << (Kswitch - 59)) | (1L << (Kcase - 59)) | (1L << (Ktry - 59)) | (1L << (Kcatch - 59)) | (1L << (Kdefault - 59)) | (1L << (Kthen - 59)) | (1L << (Kelse - 59)) | (1L << (Ktypeswitch - 59)) | (1L << (Kor - 59)) | (1L << (Kand - 59)) | (1L << (Knot - 59)) | (1L << (Kto - 59)) | (1L << (Kinstance - 59)) | (1L << (Kof - 59)) | (1L << (Kstatically - 59)) | (1L << (Kis - 59)) | (1L << (Ktreat - 59)) | (1L << (Kcast - 59)) | (1L << (Kcastable - 59)) | (1L << (Kversion - 59)) | (1L << (Kjsoniq - 59)) | (1L << (Kunordered - 59)) | (1L << (Ktrue - 59)) | (1L << (Kfalse - 59)) | (1L << (Ktype - 59)) | (1L << (Kvalidate - 59)) | (1L << (Kannotate - 59)) | (1L << (Kdeclare - 59)) | (1L << (Kcontext - 59)) | (1L << (Kitem - 59)) | (1L << (Kvariable - 59)) | (1L << (Kinsert - 59)) | (1L << (Kdelete - 59)) | (1L << (Krename - 59)) | (1L << (Kreplace - 59)) | (1L << (Kcopy - 59)) | (1L << (Kmodify - 59)) | (1L << (Kappend - 59)) | (1L << (Kinto - 59)) | (1L << (Kvalue - 59)) | (1L << (Kjson - 59)))) != 0) || ((((_la - 123)) & ~0x3f) == 0 && ((1L << (_la - 123)) & ((1L << (Kwith - 123)) | (1L << (Kposition - 123)) | (1L << (Kbreak - 123)) | (1L << (Kloop - 123)) | (1L << (Kcontinue - 123)) | (1L << (Kexit - 123)) | (1L << (Kreturning - 123)) | (1L << (Kwhile - 123)) | (1L << (NullLiteral - 123)))) != 0)) ) {
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00b4\u06d6\4\2\t"+
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
		"\t\u00b2\4\u00b3\t\u00b3\4\u00b4\t\u00b4\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u0171\n\3\3\3\3\3\5\3\u0175\n\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\6\3\6\3\6\5\6\u0185\n\6\3\6\3\6\7\6\u0189\n\6\f\6\16\6"+
		"\u018c\13\6\3\6\3\6\3\6\7\6\u0191\n\6\f\6\16\6\u0194\13\6\3\7\3\7\3\b"+
		"\7\b\u0199\n\b\f\b\16\b\u019c\13\b\3\t\3\t\3\t\3\n\3\n\5\n\u01a3\n\n\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u01b2"+
		"\n\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\5\22"+
		"\u01d0\n\22\3\22\3\22\3\22\3\22\3\22\3\22\7\22\u01d8\n\22\f\22\16\22\u01db"+
		"\13\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24"+
		"\3\24\3\24\3\24\3\24\6\24\u01ee\n\24\r\24\16\24\u01ef\3\24\3\24\3\24\3"+
		"\24\3\25\3\25\6\25\u01f8\n\25\r\25\16\25\u01f9\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\6\26\u0202\n\26\r\26\16\26\u0203\3\27\3\27\3\27\5\27\u0209\n\27"+
		"\3\27\3\27\3\27\5\27\u020e\n\27\7\27\u0210\n\27\f\27\16\27\u0213\13\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\30\6\30\u021c\n\30\r\30\16\30\u021d\3"+
		"\30\3\30\5\30\u0222\n\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\5\31\u022b"+
		"\n\31\3\31\3\31\3\31\7\31\u0230\n\31\f\31\16\31\u0233\13\31\3\31\3\31"+
		"\3\31\3\32\3\32\3\32\3\32\3\32\3\32\7\32\u023e\n\32\f\32\16\32\u0241\13"+
		"\32\3\32\5\32\u0244\n\32\3\33\7\33\u0247\n\33\f\33\16\33\u024a\13\33\3"+
		"\34\3\34\3\34\3\34\3\34\7\34\u0251\n\34\f\34\16\34\u0254\13\34\3\34\3"+
		"\34\3\35\3\35\3\35\5\35\u025b\n\35\3\35\3\35\5\35\u025f\n\35\3\36\3\36"+
		"\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\5\37\u026b\n\37\3 \3 \3 \3 \3"+
		" \3 \3!\3!\3!\3!\5!\u0277\n!\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$"+
		"\3$\3$\3$\3%\3%\3%\3%\3%\5%\u028d\n%\3%\3%\3%\3%\7%\u0293\n%\f%\16%\u0296"+
		"\13%\3&\3&\5&\u029a\n&\3&\5&\u029d\n&\3&\3&\5&\u02a1\n&\3\'\3\'\3(\3("+
		"\3(\3(\3(\5(\u02aa\n(\3(\3(\3(\3(\3(\7(\u02b1\n(\f(\16(\u02b4\13(\5(\u02b6"+
		"\n(\3)\3)\3)\3)\3)\3)\5)\u02be\n)\3)\3)\3)\3)\3)\5)\u02c5\n)\5)\u02c7"+
		"\n)\3*\3*\3*\3*\3*\5*\u02ce\n*\3*\3*\3*\3*\3*\5*\u02d5\n*\5*\u02d7\n*"+
		"\3+\3+\3+\3+\3+\3+\5+\u02df\n+\3+\3+\3+\5+\u02e4\n+\3+\3+\3+\3+\3+\5+"+
		"\u02eb\n+\3,\3,\3,\3,\3,\5,\u02f2\n,\3,\3,\3-\3-\3-\3-\3-\3-\5-\u02fc"+
		"\n-\3.\3.\3.\7.\u0301\n.\f.\16.\u0304\13.\3/\3/\3/\3/\5/\u030a\n/\3\60"+
		"\3\60\3\60\7\60\u030f\n\60\f\60\16\60\u0312\13\60\3\61\3\61\3\61\3\61"+
		"\3\61\3\61\5\61\u031a\n\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\5\62"+
		"\u0324\n\62\3\63\3\63\5\63\u0328\n\63\3\63\3\63\3\63\3\63\3\63\3\63\7"+
		"\63\u0330\n\63\f\63\16\63\u0333\13\63\3\63\3\63\3\63\3\64\3\64\3\64\3"+
		"\64\7\64\u033c\n\64\f\64\16\64\u033f\13\64\3\65\3\65\3\65\5\65\u0344\n"+
		"\65\3\65\3\65\5\65\u0348\n\65\3\65\3\65\5\65\u034c\n\65\3\65\3\65\3\65"+
		"\3\66\3\66\3\66\3\66\7\66\u0355\n\66\f\66\16\66\u0358\13\66\3\67\3\67"+
		"\3\67\5\67\u035d\n\67\3\67\3\67\3\67\38\38\38\39\39\39\39\39\79\u036a"+
		"\n9\f9\169\u036d\139\3:\3:\3:\5:\u0372\n:\3:\3:\5:\u0376\n:\3:\3:\5:\u037a"+
		"\n:\3;\3;\3;\3;\3;\5;\u0381\n;\3;\3;\3;\7;\u0386\n;\f;\16;\u0389\13;\3"+
		"<\3<\3<\5<\u038e\n<\3<\3<\3<\5<\u0393\n<\5<\u0395\n<\3<\3<\5<\u0399\n"+
		"<\3=\3=\3=\3>\3>\5>\u03a0\n>\3>\3>\3>\7>\u03a5\n>\f>\16>\u03a8\13>\3>"+
		"\3>\3>\3?\3?\3?\5?\u03b0\n?\3?\3?\3?\3@\3@\3@\3@\3@\6@\u03ba\n@\r@\16"+
		"@\u03bb\3@\3@\3@\3@\3A\3A\6A\u03c4\nA\rA\16A\u03c5\3A\3A\3A\3B\3B\3B\3"+
		"B\3B\6B\u03d0\nB\rB\16B\u03d1\3B\3B\5B\u03d6\nB\3B\3B\3B\3C\3C\3C\3C\5"+
		"C\u03df\nC\3C\3C\3C\7C\u03e4\nC\fC\16C\u03e7\13C\3C\3C\3C\3D\3D\3D\3D"+
		"\3D\3D\3D\3D\3D\3E\3E\3E\3E\3E\6E\u03fa\nE\rE\16E\u03fb\3F\3F\3F\5F\u0401"+
		"\nF\3F\3F\3F\5F\u0406\nF\7F\u0408\nF\fF\16F\u040b\13F\3F\3F\3F\3F\3G\3"+
		"G\3G\7G\u0414\nG\fG\16G\u0417\13G\3H\3H\3H\7H\u041c\nH\fH\16H\u041f\13"+
		"H\3I\5I\u0422\nI\3I\3I\3J\3J\3J\5J\u0429\nJ\3K\3K\3K\7K\u042e\nK\fK\16"+
		"K\u0431\13K\3L\3L\3L\5L\u0436\nL\3M\3M\3M\7M\u043b\nM\fM\16M\u043e\13"+
		"M\3N\3N\3N\7N\u0443\nN\fN\16N\u0446\13N\3O\3O\3O\3O\5O\u044c\nO\3P\3P"+
		"\3P\3P\5P\u0452\nP\3Q\3Q\3Q\3Q\5Q\u0458\nQ\3R\3R\3R\3R\5R\u045e\nR\3S"+
		"\3S\3S\3S\5S\u0464\nS\3T\3T\3T\3T\3T\3T\3T\7T\u046d\nT\fT\16T\u0470\13"+
		"T\3U\3U\3U\5U\u0475\nU\3V\7V\u0478\nV\fV\16V\u047b\13V\3V\3V\3W\3W\3W"+
		"\5W\u0482\nW\3X\3X\3X\3X\3X\3X\3X\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Z\3Z\3Z\7Z\u0495"+
		"\nZ\fZ\16Z\u0498\13Z\3[\3[\3[\3[\3[\3[\7[\u04a0\n[\f[\16[\u04a3\13[\3"+
		"\\\3\\\3\\\3\\\3\\\3\\\3]\3]\3]\3^\3^\3^\3^\3_\3_\3_\3_\3_\3_\3_\5_\u04b9"+
		"\n_\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\5`\u04ca\n`\3a\3a\3a"+
		"\3a\3b\3b\3b\3c\3c\5c\u04d5\nc\3c\3c\3d\3d\3e\3e\3e\3e\3e\3f\3f\3f\3f"+
		"\3f\3g\3g\3g\3h\3h\3h\5h\u04eb\nh\7h\u04ed\nh\fh\16h\u04f0\13h\3h\3h\3"+
		"i\3i\5i\u04f6\ni\3j\3j\5j\u04fa\nj\3k\3k\3k\3k\3l\3l\3l\3l\5l\u0504\n"+
		"l\3l\3l\3l\5l\u0509\nl\3l\3l\3l\3l\3m\3m\3m\3m\3m\3m\3m\3m\5m\u0517\n"+
		"m\3m\3m\3m\3m\3m\7m\u051e\nm\fm\16m\u0521\13m\3m\3m\3m\5m\u0526\nm\3n"+
		"\3n\3n\3n\3o\3o\3o\3o\3o\3o\3p\3p\3p\3p\3p\3p\3p\3p\3q\3q\3q\3q\3q\7q"+
		"\u053f\nq\fq\16q\u0542\13q\3q\3q\3q\3q\3q\3r\3r\3r\3r\3r\3r\3s\3s\3s\6"+
		"s\u0552\ns\rs\16s\u0553\3t\3t\3t\3t\3u\3u\3u\5u\u055d\nu\3u\3u\3u\3u\3"+
		"u\7u\u0564\nu\fu\16u\u0567\13u\5u\u0569\nu\3v\3v\3v\3v\3v\3v\5v\u0571"+
		"\nv\3w\3w\5w\u0575\nw\3w\3w\3w\5w\u057a\nw\3x\3x\3x\7x\u057f\nx\fx\16"+
		"x\u0582\13x\3y\3y\5y\u0586\ny\3z\3z\5z\u058a\nz\3z\3z\3{\3{\3{\3{\5{\u0592"+
		"\n{\3|\3|\3|\3|\3}\5}\u0599\n}\3}\3}\3~\3~\3~\3~\5~\u05a1\n~\3\177\3\177"+
		"\3\177\3\177\3\u0080\3\u0080\3\u0081\3\u0081\5\u0081\u05ab\n\u0081\3\u0082"+
		"\3\u0082\3\u0082\3\u0083\3\u0083\3\u0083\5\u0083\u05b3\n\u0083\3\u0084"+
		"\3\u0084\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085\3\u0085\3\u0086\7\u0086"+
		"\u05be\n\u0086\f\u0086\16\u0086\u05c1\13\u0086\3\u0087\3\u0087\3\u0087"+
		"\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\5\u0087"+
		"\u05ce\n\u0087\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\3\u0089\3\u0089"+
		"\3\u0089\3\u0089\3\u0089\3\u0089\3\u0089\3\u0089\3\u0089\5\u0089\u05de"+
		"\n\u0089\3\u008a\3\u008a\3\u008a\5\u008a\u05e3\n\u008a\3\u008a\3\u008a"+
		"\3\u008b\3\u008b\3\u008b\3\u008b\3\u008c\3\u008c\3\u008c\3\u008c\5\u008c"+
		"\u05ef\n\u008c\3\u008c\3\u008c\3\u008d\3\u008d\3\u008d\3\u008d\3\u008e"+
		"\3\u008e\3\u008e\3\u008e\3\u008f\3\u008f\3\u008f\3\u008f\3\u0090\3\u0090"+
		"\3\u0090\3\u0090\5\u0090\u0603\n\u0090\3\u0090\3\u0090\3\u0091\3\u0091"+
		"\3\u0091\3\u0091\3\u0091\5\u0091\u060c\n\u0091\5\u0091\u060e\n\u0091\3"+
		"\u0091\3\u0091\3\u0092\3\u0092\5\u0092\u0614\n\u0092\3\u0093\3\u0093\3"+
		"\u0093\3\u0093\3\u0093\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094"+
		"\5\u0094\u0621\n\u0094\5\u0094\u0623\n\u0094\5\u0094\u0625\n\u0094\3\u0094"+
		"\3\u0094\3\u0095\3\u0095\5\u0095\u062b\n\u0095\3\u0096\3\u0096\3\u0096"+
		"\3\u0096\3\u0096\3\u0097\3\u0097\3\u0098\3\u0098\3\u0099\3\u0099\3\u009a"+
		"\3\u009a\3\u009b\3\u009b\3\u009c\3\u009c\5\u009c\u063e\n\u009c\3\u009d"+
		"\3\u009d\3\u009d\3\u009d\3\u009d\3\u009e\3\u009e\3\u009e\3\u009e\3\u009e"+
		"\3\u009e\3\u009e\3\u009f\3\u009f\5\u009f\u064e\n\u009f\3\u00a0\3\u00a0"+
		"\3\u00a0\3\u00a0\3\u00a0\3\u00a1\3\u00a1\3\u00a1\3\u00a1\3\u00a1\3\u00a2"+
		"\3\u00a2\3\u00a2\3\u00a2\3\u00a3\3\u00a3\3\u00a4\3\u00a4\3\u00a4\3\u00a4"+
		"\3\u00a4\5\u00a4\u0665\n\u00a4\3\u00a5\3\u00a5\3\u00a5\5\u00a5\u066a\n"+
		"\u00a5\3\u00a5\3\u00a5\3\u00a6\3\u00a6\3\u00a6\5\u00a6\u0671\n\u00a6\3"+
		"\u00a6\3\u00a6\3\u00a7\3\u00a7\3\u00a7\5\u00a7\u0678\n\u00a7\3\u00a7\3"+
		"\u00a7\3\u00a8\3\u00a8\3\u00a8\5\u00a8\u067f\n\u00a8\3\u00a8\3\u00a8\3"+
		"\u00a9\3\u00a9\3\u00a9\5\u00a9\u0686\n\u00a9\3\u00a9\3\u00a9\3\u00aa\3"+
		"\u00aa\3\u00aa\3\u00aa\3\u00aa\3\u00aa\5\u00aa\u0690\n\u00aa\5\u00aa\u0692"+
		"\n\u00aa\3\u00ab\3\u00ab\3\u00ab\3\u00ab\7\u00ab\u0698\n\u00ab\f\u00ab"+
		"\16\u00ab\u069b\13\u00ab\5\u00ab\u069d\n\u00ab\3\u00ab\3\u00ab\3\u00ab"+
		"\3\u00ab\3\u00ab\5\u00ab\u06a4\n\u00ab\3\u00ac\3\u00ac\5\u00ac\u06a8\n"+
		"\u00ac\3\u00ad\3\u00ad\3\u00ad\3\u00ad\3\u00ad\3\u00ae\3\u00ae\3\u00ae"+
		"\3\u00ae\3\u00ae\7\u00ae\u06b4\n\u00ae\f\u00ae\16\u00ae\u06b7\13\u00ae"+
		"\5\u00ae\u06b9\n\u00ae\3\u00ae\3\u00ae\3\u00ae\3\u00ae\3\u00af\3\u00af"+
		"\5\u00af\u06c1\n\u00af\3\u00b0\3\u00b0\5\u00b0\u06c5\n\u00b0\3\u00b0\3"+
		"\u00b0\3\u00b0\3\u00b1\3\u00b1\5\u00b1\u06cc\n\u00b1\3\u00b1\3\u00b1\3"+
		"\u00b2\3\u00b2\3\u00b3\3\u00b3\3\u00b4\3\u00b4\3\u00b4\2\2\u00b5\2\4\6"+
		"\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRT"+
		"VXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e"+
		"\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6"+
		"\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be"+
		"\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6"+
		"\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee"+
		"\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106"+
		"\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e"+
		"\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134\u0136"+
		"\u0138\u013a\u013c\u013e\u0140\u0142\u0144\u0146\u0148\u014a\u014c\u014e"+
		"\u0150\u0152\u0154\u0156\u0158\u015a\u015c\u015e\u0160\u0162\u0164\u0166"+
		"\2\r\4\2\21\21ii\3\2RS\3\2\24\35\4\2\5\5#-\3\2/\60\4\2\f\f\61\63\3\2\u0089"+
		"\u008a\3\2\u008c\u0092\3\2\u0093\u0097\4\2\23\23\u00aa\u00aa\4\2=\u0084"+
		"\u00ab\u00ab\2\u071c\2\u0168\3\2\2\2\4\u0170\3\2\2\2\6\u0176\3\2\2\2\b"+
		"\u0179\3\2\2\2\n\u018a\3\2\2\2\f\u0195\3\2\2\2\16\u019a\3\2\2\2\20\u019d"+
		"\3\2\2\2\22\u01a0\3\2\2\2\24\u01b1\3\2\2\2\26\u01b3\3\2\2\2\30\u01b6\3"+
		"\2\2\2\32\u01bc\3\2\2\2\34\u01c0\3\2\2\2\36\u01c4\3\2\2\2 \u01c8\3\2\2"+
		"\2\"\u01cf\3\2\2\2$\u01df\3\2\2\2&\u01e8\3\2\2\2(\u01f7\3\2\2\2*\u01fe"+
		"\3\2\2\2,\u0205\3\2\2\2.\u0216\3\2\2\2\60\u0226\3\2\2\2\62\u0237\3\2\2"+
		"\2\64\u0248\3\2\2\2\66\u024b\3\2\2\28\u0257\3\2\2\2:\u0260\3\2\2\2<\u026a"+
		"\3\2\2\2>\u026c\3\2\2\2@\u0276\3\2\2\2B\u0278\3\2\2\2D\u027d\3\2\2\2F"+
		"\u0281\3\2\2\2H\u0287\3\2\2\2J\u029c\3\2\2\2L\u02a2\3\2\2\2N\u02a4\3\2"+
		"\2\2P\u02b7\3\2\2\2R\u02c8\3\2\2\2T\u02d8\3\2\2\2V\u02ec\3\2\2\2X\u02fb"+
		"\3\2\2\2Z\u02fd\3\2\2\2\\\u0305\3\2\2\2^\u030b\3\2\2\2`\u0319\3\2\2\2"+
		"b\u0323\3\2\2\2d\u0327\3\2\2\2f\u0337\3\2\2\2h\u0340\3\2\2\2j\u0350\3"+
		"\2\2\2l\u0359\3\2\2\2n\u0361\3\2\2\2p\u0364\3\2\2\2r\u036e\3\2\2\2t\u0380"+
		"\3\2\2\2v\u038a\3\2\2\2x\u039a\3\2\2\2z\u039f\3\2\2\2|\u03ac\3\2\2\2~"+
		"\u03b4\3\2\2\2\u0080\u03c3\3\2\2\2\u0082\u03ca\3\2\2\2\u0084\u03da\3\2"+
		"\2\2\u0086\u03eb\3\2\2\2\u0088\u03f4\3\2\2\2\u008a\u03fd\3\2\2\2\u008c"+
		"\u0410\3\2\2\2\u008e\u0418\3\2\2\2\u0090\u0421\3\2\2\2\u0092\u0425\3\2"+
		"\2\2\u0094\u042a\3\2\2\2\u0096\u0432\3\2\2\2\u0098\u0437\3\2\2\2\u009a"+
		"\u043f\3\2\2\2\u009c\u0447\3\2\2\2\u009e\u044d\3\2\2\2\u00a0\u0453\3\2"+
		"\2\2\u00a2\u0459\3\2\2\2\u00a4\u045f\3\2\2\2\u00a6\u0465\3\2\2\2\u00a8"+
		"\u0474\3\2\2\2\u00aa\u0479\3\2\2\2\u00ac\u0481\3\2\2\2\u00ae\u0483\3\2"+
		"\2\2\u00b0\u048a\3\2\2\2\u00b2\u0491\3\2\2\2\u00b4\u0499\3\2\2\2\u00b6"+
		"\u04a4\3\2\2\2\u00b8\u04aa\3\2\2\2\u00ba\u04ad\3\2\2\2\u00bc\u04b1\3\2"+
		"\2\2\u00be\u04c9\3\2\2\2\u00c0\u04cb\3\2\2\2\u00c2\u04cf\3\2\2\2\u00c4"+
		"\u04d2\3\2\2\2\u00c6\u04d8\3\2\2\2\u00c8\u04da\3\2\2\2\u00ca\u04df\3\2"+
		"\2\2\u00cc\u04e4\3\2\2\2\u00ce\u04e7\3\2\2\2\u00d0\u04f5\3\2\2\2\u00d2"+
		"\u04f9\3\2\2\2\u00d4\u04fb\3\2\2\2\u00d6\u04ff\3\2\2\2\u00d8\u0525\3\2"+
		"\2\2\u00da\u0527\3\2\2\2\u00dc\u052b\3\2\2\2\u00de\u0531\3\2\2\2\u00e0"+
		"\u0539\3\2\2\2\u00e2\u0548\3\2\2\2\u00e4\u054e\3\2\2\2\u00e6\u0555\3\2"+
		"\2\2\u00e8\u0559\3\2\2\2\u00ea\u0570\3\2\2\2\u00ec\u0579\3\2\2\2\u00ee"+
		"\u057b\3\2\2\2\u00f0\u0585\3\2\2\2\u00f2\u0589\3\2\2\2\u00f4\u0591\3\2"+
		"\2\2\u00f6\u0593\3\2\2\2\u00f8\u0598\3\2\2\2\u00fa\u05a0\3\2\2\2\u00fc"+
		"\u05a2\3\2\2\2\u00fe\u05a6\3\2\2\2\u0100\u05aa\3\2\2\2\u0102\u05ac\3\2"+
		"\2\2\u0104\u05b2\3\2\2\2\u0106\u05b4\3\2\2\2\u0108\u05b8\3\2\2\2\u010a"+
		"\u05bf\3\2\2\2\u010c\u05cd\3\2\2\2\u010e\u05cf\3\2\2\2\u0110\u05dd\3\2"+
		"\2\2\u0112\u05df\3\2\2\2\u0114\u05e6\3\2\2\2\u0116\u05ea\3\2\2\2\u0118"+
		"\u05f2\3\2\2\2\u011a\u05f6\3\2\2\2\u011c\u05fa\3\2\2\2\u011e\u05fe\3\2"+
		"\2\2\u0120\u0606\3\2\2\2\u0122\u0613\3\2\2\2\u0124\u0615\3\2\2\2\u0126"+
		"\u061a\3\2\2\2\u0128\u062a\3\2\2\2\u012a\u062c\3\2\2\2\u012c\u0631\3\2"+
		"\2\2\u012e\u0633\3\2\2\2\u0130\u0635\3\2\2\2\u0132\u0637\3\2\2\2\u0134"+
		"\u0639\3\2\2\2\u0136\u063d\3\2\2\2\u0138\u063f\3\2\2\2\u013a\u0644\3\2"+
		"\2\2\u013c\u064d\3\2\2\2\u013e\u064f\3\2\2\2\u0140\u0654\3\2\2\2\u0142"+
		"\u0659\3\2\2\2\u0144\u065d\3\2\2\2\u0146\u0664\3\2\2\2\u0148\u0666\3\2"+
		"\2\2\u014a\u066d\3\2\2\2\u014c\u0674\3\2\2\2\u014e\u067b\3\2\2\2\u0150"+
		"\u0682\3\2\2\2\u0152\u0691\3\2\2\2\u0154\u06a3\3\2\2\2\u0156\u06a7\3\2"+
		"\2\2\u0158\u06a9\3\2\2\2\u015a\u06ae\3\2\2\2\u015c\u06be\3\2\2\2\u015e"+
		"\u06c4\3\2\2\2\u0160\u06c9\3\2\2\2\u0162\u06cf\3\2\2\2\u0164\u06d1\3\2"+
		"\2\2\u0166\u06d3\3\2\2\2\u0168\u0169\5\4\3\2\u0169\u016a\7\2\2\3\u016a"+
		"\3\3\2\2\2\u016b\u016c\7h\2\2\u016c\u016d\7g\2\2\u016d\u016e\5\u0164\u00b3"+
		"\2\u016e\u016f\7\3\2\2\u016f\u0171\3\2\2\2\u0170\u016b\3\2\2\2\u0170\u0171"+
		"\3\2\2\2\u0171\u0174\3\2\2\2\u0172\u0175\5\b\5\2\u0173\u0175\5\6\4\2\u0174"+
		"\u0172\3\2\2\2\u0174\u0173\3\2\2\2\u0175\5\3\2\2\2\u0176\u0177\5\n\6\2"+
		"\u0177\u0178\5\f\7\2\u0178\7\3\2\2\2\u0179\u017a\7\4\2\2\u017a\u017b\7"+
		"\u0087\2\2\u017b\u017c\7\u00b2\2\2\u017c\u017d\7\5\2\2\u017d\u017e\5\u0162"+
		"\u00b2\2\u017e\u017f\7\3\2\2\u017f\u0180\5\n\6\2\u0180\t\3\2\2\2\u0181"+
		"\u0185\5<\37\2\u0182\u0185\5> \2\u0183\u0185\5N(\2\u0184\u0181\3\2\2\2"+
		"\u0184\u0182\3\2\2\2\u0184\u0183\3\2\2\2\u0185\u0186\3\2\2\2\u0186\u0187"+
		"\7\3\2\2\u0187\u0189\3\2\2\2\u0188\u0184\3\2\2\2\u0189\u018c\3\2\2\2\u018a"+
		"\u0188\3\2\2\2\u018a\u018b\3\2\2\2\u018b\u0192\3\2\2\2\u018c\u018a\3\2"+
		"\2\2\u018d\u018e\5@!\2\u018e\u018f\7\3\2\2\u018f\u0191\3\2\2\2\u0190\u018d"+
		"\3\2\2\2\u0191\u0194\3\2\2\2\u0192\u0190\3\2\2\2\u0192\u0193\3\2\2\2\u0193"+
		"\13\3\2\2\2\u0194\u0192\3\2\2\2\u0195\u0196\5\22\n\2\u0196\r\3\2\2\2\u0197"+
		"\u0199\5\24\13\2\u0198\u0197\3\2\2\2\u0199\u019c\3\2\2\2\u019a\u0198\3"+
		"\2\2\2\u019a\u019b\3\2\2\2\u019b\17\3\2\2\2\u019c\u019a\3\2\2\2\u019d"+
		"\u019e\5\16\b\2\u019e\u019f\5^\60\2\u019f\21\3\2\2\2\u01a0\u01a2\5\16"+
		"\b\2\u01a1\u01a3\5^\60\2\u01a2\u01a1\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3"+
		"\23\3\2\2\2\u01a4\u01b2\5\26\f\2\u01a5\u01b2\5\30\r\2\u01a6\u01b2\5\32"+
		"\16\2\u01a7\u01b2\5\34\17\2\u01a8\u01b2\5\36\20\2\u01a9\u01b2\5 \21\2"+
		"\u01aa\u01b2\5\"\22\2\u01ab\u01b2\5$\23\2\u01ac\u01b2\5&\24\2\u01ad\u01b2"+
		"\5*\26\2\u01ae\u01b2\5.\30\2\u01af\u01b2\5\66\34\2\u01b0\u01b2\5:\36\2"+
		"\u01b1\u01a4\3\2\2\2\u01b1\u01a5\3\2\2\2\u01b1\u01a6\3\2\2\2\u01b1\u01a7"+
		"\3\2\2\2\u01b1\u01a8\3\2\2\2\u01b1\u01a9\3\2\2\2\u01b1\u01aa\3\2\2\2\u01b1"+
		"\u01ab\3\2\2\2\u01b1\u01ac\3\2\2\2\u01b1\u01ad\3\2\2\2\u01b1\u01ae\3\2"+
		"\2\2\u01b1\u01af\3\2\2\2\u01b1\u01b0\3\2\2\2\u01b2\25\3\2\2\2\u01b3\u01b4"+
		"\5b\62\2\u01b4\u01b5\7\3\2\2\u01b5\27\3\2\2\2\u01b6\u01b7\7\6\2\2\u01b7"+
		"\u01b8\5J&\2\u01b8\u01b9\7\7\2\2\u01b9\u01ba\5`\61\2\u01ba\u01bb\7\3\2"+
		"\2\u01bb\31\3\2\2\2\u01bc\u01bd\7\b\2\2\u01bd\u01be\5\16\b\2\u01be\u01bf"+
		"\7\t\2\2\u01bf\33\3\2\2\2\u01c0\u01c1\7\177\2\2\u01c1\u01c2\7\u0080\2"+
		"\2\u01c2\u01c3\7\3\2\2\u01c3\35\3\2\2\2\u01c4\u01c5\7\u0081\2\2\u01c5"+
		"\u01c6\7\u0080\2\2\u01c6\u01c7\7\3\2\2\u01c7\37\3\2\2\2\u01c8\u01c9\7"+
		"\u0082\2\2\u01c9\u01ca\7\u0083\2\2\u01ca\u01cb\5`\61\2\u01cb\u01cc\7\3"+
		"\2\2\u01cc!\3\2\2\2\u01cd\u01d0\5f\64\2\u01ce\u01d0\5j\66\2\u01cf\u01cd"+
		"\3\2\2\2\u01cf\u01ce\3\2\2\2\u01d0\u01d9\3\2\2\2\u01d1\u01d8\5f\64\2\u01d2"+
		"\u01d8\5j\66\2\u01d3\u01d8\5n8\2\u01d4\u01d8\5p9\2\u01d5\u01d8\5t;\2\u01d6"+
		"\u01d8\5x=\2\u01d7\u01d1\3\2\2\2\u01d7\u01d2\3\2\2\2\u01d7\u01d3\3\2\2"+
		"\2\u01d7\u01d4\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d6\3\2\2\2\u01d8\u01db"+
		"\3\2\2\2\u01d9\u01d7\3\2\2\2\u01d9\u01da\3\2\2\2\u01da\u01dc\3\2\2\2\u01db"+
		"\u01d9\3\2\2\2\u01dc\u01dd\7C\2\2\u01dd\u01de\5\24\13\2\u01de#\3\2\2\2"+
		"\u01df\u01e0\7D\2\2\u01e0\u01e1\7\n\2\2\u01e1\u01e2\5^\60\2\u01e2\u01e3"+
		"\7\13\2\2\u01e3\u01e4\7Y\2\2\u01e4\u01e5\5\24\13\2\u01e5\u01e6\7Z\2\2"+
		"\u01e6\u01e7\5\24\13\2\u01e7%\3\2\2\2\u01e8\u01e9\7T\2\2\u01e9\u01ea\7"+
		"\n\2\2\u01ea\u01eb\5^\60\2\u01eb\u01ed\7\13\2\2\u01ec\u01ee\5(\25\2\u01ed"+
		"\u01ec\3\2\2\2\u01ee\u01ef\3\2\2\2\u01ef\u01ed\3\2\2\2\u01ef\u01f0\3\2"+
		"\2\2\u01f0\u01f1\3\2\2\2\u01f1\u01f2\7X\2\2\u01f2\u01f3\7C\2\2\u01f3\u01f4"+
		"\5\24\13\2\u01f4\'\3\2\2\2\u01f5\u01f6\7U\2\2\u01f6\u01f8\5`\61\2\u01f7"+
		"\u01f5\3\2\2\2\u01f8\u01f9\3\2\2\2\u01f9\u01f7\3\2\2\2\u01f9\u01fa\3\2"+
		"\2\2\u01fa\u01fb\3\2\2\2\u01fb\u01fc\7C\2\2\u01fc\u01fd\5\24\13\2\u01fd"+
		")\3\2\2\2\u01fe\u01ff\7V\2\2\u01ff\u0201\5\32\16\2\u0200\u0202\5,\27\2"+
		"\u0201\u0200\3\2\2\2\u0202\u0203\3\2\2\2\u0203\u0201\3\2\2\2\u0203\u0204"+
		"\3\2\2\2\u0204+\3\2\2\2\u0205\u0208\7W\2\2\u0206\u0209\7\f\2\2\u0207\u0209"+
		"\5J&\2\u0208\u0206\3\2\2\2\u0208\u0207\3\2\2\2\u0209\u0211\3\2\2\2\u020a"+
		"\u020d\7\r\2\2\u020b\u020e\7\f\2\2\u020c\u020e\5J&\2\u020d\u020b\3\2\2"+
		"\2\u020d\u020c\3\2\2\2\u020e\u0210\3\2\2\2\u020f\u020a\3\2\2\2\u0210\u0213"+
		"\3\2\2\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0214\3\2\2\2\u0213"+
		"\u0211\3\2\2\2\u0214\u0215\5\32\16\2\u0215-\3\2\2\2\u0216\u0217\7[\2\2"+
		"\u0217\u0218\7\n\2\2\u0218\u0219\5^\60\2\u0219\u021b\7\13\2\2\u021a\u021c"+
		"\5\60\31\2\u021b\u021a\3\2\2\2\u021c\u021d\3\2\2\2\u021d\u021b\3\2\2\2"+
		"\u021d\u021e\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0221\7X\2\2\u0220\u0222"+
		"\5\u00c2b\2\u0221\u0220\3\2\2\2\u0221\u0222\3\2\2\2\u0222\u0223\3\2\2"+
		"\2\u0223\u0224\7C\2\2\u0224\u0225\5\24\13\2\u0225/\3\2\2\2\u0226\u022a"+
		"\7U\2\2\u0227\u0228\5\u00c2b\2\u0228\u0229\7F\2\2\u0229\u022b\3\2\2\2"+
		"\u022a\u0227\3\2\2\2\u022a\u022b\3\2\2\2\u022b\u022c\3\2\2\2\u022c\u0231"+
		"\5\u0152\u00aa\2\u022d\u022e\7\r\2\2\u022e\u0230\5\u0152\u00aa\2\u022f"+
		"\u022d\3\2\2\2\u0230\u0233\3\2\2\2\u0231\u022f\3\2\2\2\u0231\u0232\3\2"+
		"\2\2\u0232\u0234\3\2\2\2\u0233\u0231\3\2\2\2\u0234\u0235\7C\2\2\u0235"+
		"\u0236\5\24\13\2\u0236\61\3\2\2\2\u0237\u0238\7\16\2\2\u0238\u0243\5J"+
		"&\2\u0239\u023a\7\n\2\2\u023a\u023f\7\u00ac\2\2\u023b\u023c\7\17\2\2\u023c"+
		"\u023e\7\u00ac\2\2\u023d\u023b\3\2\2\2\u023e\u0241\3\2\2\2\u023f\u023d"+
		"\3\2\2\2\u023f\u0240\3\2\2\2\u0240\u0242\3\2\2\2\u0241\u023f\3\2\2\2\u0242"+
		"\u0244\7\13\2\2\u0243\u0239\3\2\2\2\u0243\u0244\3\2\2\2\u0244\63\3\2\2"+
		"\2\u0245\u0247\5\62\32\2\u0246\u0245\3\2\2\2\u0247\u024a\3\2\2\2\u0248"+
		"\u0246\3\2\2\2\u0248\u0249\3\2\2\2\u0249\65\3\2\2\2\u024a\u0248\3\2\2"+
		"\2\u024b\u024c\5\64\33\2\u024c\u024d\7r\2\2\u024d\u0252\58\35\2\u024e"+
		"\u024f\7\17\2\2\u024f\u0251\58\35\2\u0250\u024e\3\2\2\2\u0251\u0254\3"+
		"\2\2\2\u0252\u0250\3\2\2\2\u0252\u0253\3\2\2\2\u0253\u0255\3\2\2\2\u0254"+
		"\u0252\3\2\2\2\u0255\u0256\7\3\2\2\u0256\67\3\2\2\2\u0257\u025a\5\u00c2"+
		"b\2\u0258\u0259\7F\2\2\u0259\u025b\5\u0152\u00aa\2\u025a\u0258\3\2\2\2"+
		"\u025a\u025b\3\2\2\2\u025b\u025e\3\2\2\2\u025c\u025d\7\7\2\2\u025d\u025f"+
		"\5`\61\2\u025e\u025c\3\2\2\2\u025e\u025f\3\2\2\2\u025f9\3\2\2\2\u0260"+
		"\u0261\7\u0084\2\2\u0261\u0262\7\n\2\2\u0262\u0263\5^\60\2\u0263\u0264"+
		"\7\13\2\2\u0264\u0265\5\24\13\2\u0265;\3\2\2\2\u0266\u026b\5B\"\2\u0267"+
		"\u026b\5D#\2\u0268\u026b\5F$\2\u0269\u026b\5H%\2\u026a\u0266\3\2\2\2\u026a"+
		"\u0267\3\2\2\2\u026a\u0268\3\2\2\2\u026a\u0269\3\2\2\2\u026b=\3\2\2\2"+
		"\u026c\u026d\7o\2\2\u026d\u026e\7\u0087\2\2\u026e\u026f\7\u00b2\2\2\u026f"+
		"\u0270\7\5\2\2\u0270\u0271\5\u0162\u00b2\2\u0271?\3\2\2\2\u0272\u0277"+
		"\5T+\2\u0273\u0277\5P)\2\u0274\u0277\5V,\2\u0275\u0277\5R*\2\u0276\u0272"+
		"\3\2\2\2\u0276\u0273\3\2\2\2\u0276\u0274\3\2\2\2\u0276\u0275\3\2\2\2\u0277"+
		"A\3\2\2\2\u0278\u0279\7o\2\2\u0279\u027a\7X\2\2\u027a\u027b\7Q\2\2\u027b"+
		"\u027c\5\u0162\u00b2\2\u027cC\3\2\2\2\u027d\u027e\7o\2\2\u027e\u027f\7"+
		"\20\2\2\u027f\u0280\t\2\2\2\u0280E\3\2\2\2\u0281\u0282\7o\2\2\u0282\u0283"+
		"\7X\2\2\u0283\u0284\7B\2\2\u0284\u0285\7I\2\2\u0285\u0286\t\3\2\2\u0286"+
		"G\3\2\2\2\u0287\u028c\7o\2\2\u0288\u0289\7\22\2\2\u0289\u028d\5J&\2\u028a"+
		"\u028b\7X\2\2\u028b\u028d\7\22\2\2\u028c\u0288\3\2\2\2\u028c\u028a\3\2"+
		"\2\2\u028d\u0294\3\2\2\2\u028e\u028f\5L\'\2\u028f\u0290\7\5\2\2\u0290"+
		"\u0291\5\u0164\u00b3\2\u0291\u0293\3\2\2\2\u0292\u028e\3\2\2\2\u0293\u0296"+
		"\3\2\2\2\u0294\u0292\3\2\2\2\u0294\u0295\3\2\2\2\u0295I\3\2\2\2\u0296"+
		"\u0294\3\2\2\2\u0297\u029a\7\u00b2\2\2\u0298\u029a\5\u0166\u00b4\2\u0299"+
		"\u0297\3\2\2\2\u0299\u0298\3\2\2\2\u029a\u029b\3\2\2\2\u029b\u029d\7\23"+
		"\2\2\u029c\u0299\3\2\2\2\u029c\u029d\3\2\2\2\u029d\u02a0\3\2\2\2\u029e"+
		"\u02a1\7\u00b2\2\2\u029f\u02a1\5\u0166\u00b4\2\u02a0\u029e\3\2\2\2\u02a0"+
		"\u029f\3\2\2\2\u02a1K\3\2\2\2\u02a2\u02a3\t\4\2\2\u02a3M\3\2\2\2\u02a4"+
		"\u02a5\7\u0085\2\2\u02a5\u02a9\7\4\2\2\u02a6\u02a7\7\u0087\2\2\u02a7\u02a8"+
		"\7\u00b2\2\2\u02a8\u02aa\7\5\2\2\u02a9\u02a6\3\2\2\2\u02a9\u02aa\3\2\2"+
		"\2\u02aa\u02ab\3\2\2\2\u02ab\u02b5\5\u0162\u00b2\2\u02ac\u02ad\7G\2\2"+
		"\u02ad\u02b2\5\u0162\u00b2\2\u02ae\u02af\7\17\2\2\u02af\u02b1\5\u0162"+
		"\u00b2\2\u02b0\u02ae\3\2\2\2\u02b1\u02b4\3\2\2\2\u02b2\u02b0\3\2\2\2\u02b2"+
		"\u02b3\3\2\2\2\u02b3\u02b6\3\2\2\2\u02b4\u02b2\3\2\2\2\u02b5\u02ac\3\2"+
		"\2\2\u02b5\u02b6\3\2\2\2\u02b6O\3\2\2\2\u02b7\u02b8\7o\2\2\u02b8\u02b9"+
		"\5\64\33\2\u02b9\u02ba\7r\2\2\u02ba\u02bd\5\u00c2b\2\u02bb\u02bc\7F\2"+
		"\2\u02bc\u02be\5\u0152\u00aa\2\u02bd\u02bb\3\2\2\2\u02bd\u02be\3\2\2\2"+
		"\u02be\u02c6\3\2\2\2\u02bf\u02c0\7\7\2\2\u02c0\u02c7\5`\61\2\u02c1\u02c4"+
		"\7\36\2\2\u02c2\u02c3\7\7\2\2\u02c3\u02c5\5`\61\2\u02c4\u02c2\3\2\2\2"+
		"\u02c4\u02c5\3\2\2\2\u02c5\u02c7\3\2\2\2\u02c6\u02bf\3\2\2\2\u02c6\u02c1"+
		"\3\2\2\2\u02c7Q\3\2\2\2\u02c8\u02c9\7o\2\2\u02c9\u02ca\7p\2\2\u02ca\u02cd"+
		"\7q\2\2\u02cb\u02cc\7F\2\2\u02cc\u02ce\5\u0152\u00aa\2\u02cd\u02cb\3\2"+
		"\2\2\u02cd\u02ce\3\2\2\2\u02ce\u02d6\3\2\2\2\u02cf\u02d0\7\7\2\2\u02d0"+
		"\u02d7\5`\61\2\u02d1\u02d4\7\36\2\2\u02d2\u02d3\7\7\2\2\u02d3\u02d5\5"+
		"`\61\2\u02d4\u02d2\3\2\2\2\u02d4\u02d5\3\2\2\2\u02d5\u02d7\3\2\2\2\u02d6"+
		"\u02cf\3\2\2\2\u02d6\u02d1\3\2\2\2\u02d7S\3\2\2\2\u02d8\u02d9\7o\2\2\u02d9"+
		"\u02da\5\64\33\2\u02da\u02db\7\37\2\2\u02db\u02dc\5J&\2\u02dc\u02de\7"+
		"\n\2\2\u02dd\u02df\5Z.\2\u02de\u02dd\3\2\2\2\u02de\u02df\3\2\2\2\u02df"+
		"\u02e0\3\2\2\2\u02e0\u02e3\7\13\2\2\u02e1\u02e2\7F\2\2\u02e2\u02e4\5\u0152"+
		"\u00aa\2\u02e3\u02e1\3\2\2\2\u02e3\u02e4\3\2\2\2\u02e4\u02ea\3\2\2\2\u02e5"+
		"\u02e6\7\b\2\2\u02e6\u02e7\5\22\n\2\u02e7\u02e8\7\t\2\2\u02e8\u02eb\3"+
		"\2\2\2\u02e9\u02eb\7\36\2\2\u02ea\u02e5\3\2\2\2\u02ea\u02e9\3\2\2\2\u02eb"+
		"U\3\2\2\2\u02ec\u02ed\7o\2\2\u02ed\u02ee\7l\2\2\u02ee\u02ef\5J&\2\u02ef"+
		"\u02f1\7F\2\2\u02f0\u02f2\5X-\2\u02f1\u02f0\3\2\2\2\u02f1\u02f2\3\2\2"+
		"\2\u02f2\u02f3\3\2\2\2\u02f3\u02f4\5`\61\2\u02f4W\3\2\2\2\u02f5\u02f6"+
		"\7 \2\2\u02f6\u02fc\7!\2\2\u02f7\u02f8\7 \2\2\u02f8\u02fc\7\"\2\2\u02f9"+
		"\u02fa\7|\2\2\u02fa\u02fc\7\u0086\2\2\u02fb\u02f5\3\2\2\2\u02fb\u02f7"+
		"\3\2\2\2\u02fb\u02f9\3\2\2\2\u02fcY\3\2\2\2\u02fd\u0302\5\\/\2\u02fe\u02ff"+
		"\7\17\2\2\u02ff\u0301\5\\/\2\u0300\u02fe\3\2\2\2\u0301\u0304\3\2\2\2\u0302"+
		"\u0300\3\2\2\2\u0302\u0303\3\2\2\2\u0303[\3\2\2\2\u0304\u0302\3\2\2\2"+
		"\u0305\u0306\7\6\2\2\u0306\u0309\5J&\2\u0307\u0308\7F\2\2\u0308\u030a"+
		"\5\u0152\u00aa\2\u0309\u0307\3\2\2\2\u0309\u030a\3\2\2\2\u030a]\3\2\2"+
		"\2\u030b\u0310\5`\61\2\u030c\u030d\7\17\2\2\u030d\u030f\5`\61\2\u030e"+
		"\u030c\3\2\2\2\u030f\u0312\3\2\2\2\u0310\u030e\3\2\2\2\u0310\u0311\3\2"+
		"\2\2\u0311_\3\2\2\2\u0312\u0310\3\2\2\2\u0313\u031a\5b\62\2\u0314\u031a"+
		"\5d\63\2\u0315\u031a\5~@\2\u0316\u031a\5\u0082B\2\u0317\u031a\5\u0086"+
		"D\2\u0318\u031a\5\u0088E\2\u0319\u0313\3\2\2\2\u0319\u0314\3\2\2\2\u0319"+
		"\u0315\3\2\2\2\u0319\u0316\3\2\2\2\u0319\u0317\3\2\2\2\u0319\u0318\3\2"+
		"\2\2\u031aa\3\2\2\2\u031b\u0324\5z>\2\u031c\u0324\5\u008cG\2\u031d\u0324"+
		"\5\u00d8m\2\u031e\u0324\5\u00dan\2\u031f\u0324\5\u00dco\2\u0320\u0324"+
		"\5\u00dep\2\u0321\u0324\5\u00e0q\2\u0322\u0324\5\u00e2r\2\u0323\u031b"+
		"\3\2\2\2\u0323\u031c\3\2\2\2\u0323\u031d\3\2\2\2\u0323\u031e\3\2\2\2\u0323"+
		"\u031f\3\2\2\2\u0323\u0320\3\2\2\2\u0323\u0321\3\2\2\2\u0323\u0322\3\2"+
		"\2\2\u0324c\3\2\2\2\u0325\u0328\5f\64\2\u0326\u0328\5j\66\2\u0327\u0325"+
		"\3\2\2\2\u0327\u0326\3\2\2\2\u0328\u0331\3\2\2\2\u0329\u0330\5f\64\2\u032a"+
		"\u0330\5j\66\2\u032b\u0330\5n8\2\u032c\u0330\5p9\2\u032d\u0330\5t;\2\u032e"+
		"\u0330\5x=\2\u032f\u0329\3\2\2\2\u032f\u032a\3\2\2\2\u032f\u032b\3\2\2"+
		"\2\u032f\u032c\3\2\2\2\u032f\u032d\3\2\2\2\u032f\u032e\3\2\2\2\u0330\u0333"+
		"\3\2\2\2\u0331\u032f\3\2\2\2\u0331\u0332\3\2\2\2\u0332\u0334\3\2\2\2\u0333"+
		"\u0331\3\2\2\2\u0334\u0335\7C\2\2\u0335\u0336\5`\61\2\u0336e\3\2\2\2\u0337"+
		"\u0338\7=\2\2\u0338\u033d\5h\65\2\u0339\u033a\7\17\2\2\u033a\u033c\5h"+
		"\65\2\u033b\u0339\3\2\2\2\u033c\u033f\3\2\2\2\u033d\u033b\3\2\2\2\u033d"+
		"\u033e\3\2\2\2\u033eg\3\2\2\2\u033f\u033d\3\2\2\2\u0340\u0343\5\u00c2"+
		"b\2\u0341\u0342\7F\2\2\u0342\u0344\5\u0152\u00aa\2\u0343\u0341\3\2\2\2"+
		"\u0343\u0344\3\2\2\2\u0344\u0347\3\2\2\2\u0345\u0346\7H\2\2\u0346\u0348"+
		"\7I\2\2\u0347\u0345\3\2\2\2\u0347\u0348\3\2\2\2\u0348\u034b\3\2\2\2\u0349"+
		"\u034a\7G\2\2\u034a\u034c\5\u00c2b\2\u034b\u0349\3\2\2\2\u034b\u034c\3"+
		"\2\2\2\u034c\u034d\3\2\2\2\u034d\u034e\7E\2\2\u034e\u034f\5`\61\2\u034f"+
		"i\3\2\2\2\u0350\u0351\7>\2\2\u0351\u0356\5l\67\2\u0352\u0353\7\17\2\2"+
		"\u0353\u0355\5l\67\2\u0354\u0352\3\2\2\2\u0355\u0358\3\2\2\2\u0356\u0354"+
		"\3\2\2\2\u0356\u0357\3\2\2\2\u0357k\3\2\2\2\u0358\u0356\3\2\2\2\u0359"+
		"\u035c\5\u00c2b\2\u035a\u035b\7F\2\2\u035b\u035d\5\u0152\u00aa\2\u035c"+
		"\u035a\3\2\2\2\u035c\u035d\3\2\2\2\u035d\u035e\3\2\2\2\u035e\u035f\7\7"+
		"\2\2\u035f\u0360\5`\61\2\u0360m\3\2\2\2\u0361\u0362\7?\2\2\u0362\u0363"+
		"\5`\61\2\u0363o\3\2\2\2\u0364\u0365\7@\2\2\u0365\u0366\7A\2\2\u0366\u036b"+
		"\5r:\2\u0367\u0368\7\17\2\2\u0368\u036a\5r:\2\u0369\u0367\3\2\2\2\u036a"+
		"\u036d\3\2\2\2\u036b\u0369\3\2\2\2\u036b\u036c\3\2\2\2\u036cq\3\2\2\2"+
		"\u036d\u036b\3\2\2\2\u036e\u0375\5\u00c2b\2\u036f\u0370\7F\2\2\u0370\u0372"+
		"\5\u0152\u00aa\2\u0371\u036f\3\2\2\2\u0371\u0372\3\2\2\2\u0372\u0373\3"+
		"\2\2\2\u0373\u0374\7\7\2\2\u0374\u0376\5`\61\2\u0375\u0371\3\2\2\2\u0375"+
		"\u0376\3\2\2\2\u0376\u0379\3\2\2\2\u0377\u0378\7Q\2\2\u0378\u037a\5\u0162"+
		"\u00b2\2\u0379\u0377\3\2\2\2\u0379\u037a\3\2\2\2\u037as\3\2\2\2\u037b"+
		"\u037c\7B\2\2\u037c\u0381\7A\2\2\u037d\u037e\7K\2\2\u037e\u037f\7B\2\2"+
		"\u037f\u0381\7A\2\2\u0380\u037b\3\2\2\2\u0380\u037d\3\2\2\2\u0381\u0382"+
		"\3\2\2\2\u0382\u0387\5v<\2\u0383\u0384\7\17\2\2\u0384\u0386\5v<\2\u0385"+
		"\u0383\3\2\2\2\u0386\u0389\3\2\2\2\u0387\u0385\3\2\2\2\u0387\u0388\3\2"+
		"\2\2\u0388u\3\2\2\2\u0389\u0387\3\2\2\2\u038a\u038d\5`\61\2\u038b\u038e"+
		"\7L\2\2\u038c\u038e\7M\2\2\u038d\u038b\3\2\2\2\u038d\u038c\3\2\2\2\u038d"+
		"\u038e\3\2\2\2\u038e\u0394\3\2\2\2\u038f\u0392\7I\2\2\u0390\u0393\7R\2"+
		"\2\u0391\u0393\7S\2\2\u0392\u0390\3\2\2\2\u0392\u0391\3\2\2\2\u0393\u0395"+
		"\3\2\2\2\u0394\u038f\3\2\2\2\u0394\u0395\3\2\2\2\u0395\u0398\3\2\2\2\u0396"+
		"\u0397\7Q\2\2\u0397\u0399\5\u0162\u00b2\2\u0398\u0396\3\2\2\2\u0398\u0399"+
		"\3\2\2\2\u0399w\3\2\2\2\u039a\u039b\7J\2\2\u039b\u039c\5\u00c2b\2\u039c"+
		"y\3\2\2\2\u039d\u03a0\7N\2\2\u039e\u03a0\7O\2\2\u039f\u039d\3\2\2\2\u039f"+
		"\u039e\3\2\2\2\u03a0\u03a1\3\2\2\2\u03a1\u03a6\5|?\2\u03a2\u03a3\7\17"+
		"\2\2\u03a3\u03a5\5|?\2\u03a4\u03a2\3\2\2\2\u03a5\u03a8\3\2\2\2\u03a6\u03a4"+
		"\3\2\2\2\u03a6\u03a7\3\2\2\2\u03a7\u03a9\3\2\2\2\u03a8\u03a6\3\2\2\2\u03a9"+
		"\u03aa\7P\2\2\u03aa\u03ab\5`\61\2\u03ab{\3\2\2\2\u03ac\u03af\5\u00c2b"+
		"\2\u03ad\u03ae\7F\2\2\u03ae\u03b0\5\u0152\u00aa\2\u03af\u03ad\3\2\2\2"+
		"\u03af\u03b0\3\2\2\2\u03b0\u03b1\3\2\2\2\u03b1\u03b2\7E\2\2\u03b2\u03b3"+
		"\5`\61\2\u03b3}\3\2\2\2\u03b4\u03b5\7T\2\2\u03b5\u03b6\7\n\2\2\u03b6\u03b7"+
		"\5^\60\2\u03b7\u03b9\7\13\2\2\u03b8\u03ba\5\u0080A\2\u03b9\u03b8\3\2\2"+
		"\2\u03ba\u03bb\3\2\2\2\u03bb\u03b9\3\2\2\2\u03bb\u03bc\3\2\2\2\u03bc\u03bd"+
		"\3\2\2\2\u03bd\u03be\7X\2\2\u03be\u03bf\7C\2\2\u03bf\u03c0\5`\61\2\u03c0"+
		"\177\3\2\2\2\u03c1\u03c2\7U\2\2\u03c2\u03c4\5`\61\2\u03c3\u03c1\3\2\2"+
		"\2\u03c4\u03c5\3\2\2\2\u03c5\u03c3\3\2\2\2\u03c5\u03c6\3\2\2\2\u03c6\u03c7"+
		"\3\2\2\2\u03c7\u03c8\7C\2\2\u03c8\u03c9\5`\61\2\u03c9\u0081\3\2\2\2\u03ca"+
		"\u03cb\7[\2\2\u03cb\u03cc\7\n\2\2\u03cc\u03cd\5^\60\2\u03cd\u03cf\7\13"+
		"\2\2\u03ce\u03d0\5\u0084C\2\u03cf\u03ce\3\2\2\2\u03d0\u03d1\3\2\2\2\u03d1"+
		"\u03cf\3\2\2\2\u03d1\u03d2\3\2\2\2\u03d2\u03d3\3\2\2\2\u03d3\u03d5\7X"+
		"\2\2\u03d4\u03d6\5\u00c2b\2\u03d5\u03d4\3\2\2\2\u03d5\u03d6\3\2\2\2\u03d6"+
		"\u03d7\3\2\2\2\u03d7\u03d8\7C\2\2\u03d8\u03d9\5`\61\2\u03d9\u0083\3\2"+
		"\2\2\u03da\u03de\7U\2\2\u03db\u03dc\5\u00c2b\2\u03dc\u03dd\7F\2\2\u03dd"+
		"\u03df\3\2\2\2\u03de\u03db\3\2\2\2\u03de\u03df\3\2\2\2\u03df\u03e0\3\2"+
		"\2\2\u03e0\u03e5\5\u0152\u00aa\2\u03e1\u03e2\7\r\2\2\u03e2\u03e4\5\u0152"+
		"\u00aa\2\u03e3\u03e1\3\2\2\2\u03e4\u03e7\3\2\2\2\u03e5\u03e3\3\2\2\2\u03e5"+
		"\u03e6\3\2\2\2\u03e6\u03e8\3\2\2\2\u03e7\u03e5\3\2\2\2\u03e8\u03e9\7C"+
		"\2\2\u03e9\u03ea\5`\61\2\u03ea\u0085\3\2\2\2\u03eb\u03ec\7D\2\2\u03ec"+
		"\u03ed\7\n\2\2\u03ed\u03ee\5^\60\2\u03ee\u03ef\7\13\2\2\u03ef\u03f0\7"+
		"Y\2\2\u03f0\u03f1\5`\61\2\u03f1\u03f2\7Z\2\2\u03f2\u03f3\5`\61\2\u03f3"+
		"\u0087\3\2\2\2\u03f4\u03f5\7V\2\2\u03f5\u03f6\7\b\2\2\u03f6\u03f7\5^\60"+
		"\2\u03f7\u03f9\7\t\2\2\u03f8\u03fa\5\u008aF\2\u03f9\u03f8\3\2\2\2\u03fa"+
		"\u03fb\3\2\2\2\u03fb\u03f9\3\2\2\2\u03fb\u03fc\3\2\2\2\u03fc\u0089\3\2"+
		"\2\2\u03fd\u0400\7W\2\2\u03fe\u0401\7\f\2\2\u03ff\u0401\5J&\2\u0400\u03fe"+
		"\3\2\2\2\u0400\u03ff\3\2\2\2\u0401\u0409\3\2\2\2\u0402\u0405\7\r\2\2\u0403"+
		"\u0406\7\f\2\2\u0404\u0406\5J&\2\u0405\u0403\3\2\2\2\u0405\u0404\3\2\2"+
		"\2\u0406\u0408\3\2\2\2\u0407\u0402\3\2\2\2\u0408\u040b\3\2\2\2\u0409\u0407"+
		"\3\2\2\2\u0409\u040a\3\2\2\2\u040a\u040c\3\2\2\2\u040b\u0409\3\2\2\2\u040c"+
		"\u040d\7\b\2\2\u040d\u040e\5^\60\2\u040e\u040f\7\t\2\2\u040f\u008b\3\2"+
		"\2\2\u0410\u0415\5\u008eH\2\u0411\u0412\7\\\2\2\u0412\u0414\5\u008eH\2"+
		"\u0413\u0411\3\2\2\2\u0414\u0417\3\2\2\2\u0415\u0413\3\2\2\2\u0415\u0416"+
		"\3\2\2\2\u0416\u008d\3\2\2\2\u0417\u0415\3\2\2\2\u0418\u041d\5\u0090I"+
		"\2\u0419\u041a\7]\2\2\u041a\u041c\5\u0090I\2\u041b\u0419\3\2\2\2\u041c"+
		"\u041f\3\2\2\2\u041d\u041b\3\2\2\2\u041d\u041e\3\2\2\2\u041e\u008f\3\2"+
		"\2\2\u041f\u041d\3\2\2\2\u0420\u0422\7^\2\2\u0421\u0420\3\2\2\2\u0421"+
		"\u0422\3\2\2\2\u0422\u0423\3\2\2\2\u0423\u0424\5\u0092J\2\u0424\u0091"+
		"\3\2\2\2\u0425\u0428\5\u0094K\2\u0426\u0427\t\5\2\2\u0427\u0429\5\u0094"+
		"K\2\u0428\u0426\3\2\2\2\u0428\u0429\3\2\2\2\u0429\u0093\3\2\2\2\u042a"+
		"\u042f\5\u0096L\2\u042b\u042c\7.\2\2\u042c\u042e\5\u0096L\2\u042d\u042b"+
		"\3\2\2\2\u042e\u0431\3\2\2\2\u042f\u042d\3\2\2\2\u042f\u0430\3\2\2\2\u0430"+
		"\u0095\3\2\2\2\u0431\u042f\3\2\2\2\u0432\u0435\5\u0098M\2\u0433\u0434"+
		"\7_\2\2\u0434\u0436\5\u0098M\2\u0435\u0433\3\2\2\2\u0435\u0436\3\2\2\2"+
		"\u0436\u0097\3\2\2\2\u0437\u043c\5\u009aN\2\u0438\u0439\t\6\2\2\u0439"+
		"\u043b\5\u009aN\2\u043a\u0438\3\2\2\2\u043b\u043e\3\2\2\2\u043c\u043a"+
		"\3\2\2\2\u043c\u043d\3\2\2\2\u043d\u0099\3\2\2\2\u043e\u043c\3\2\2\2\u043f"+
		"\u0444\5\u009cO\2\u0440\u0441\t\7\2\2\u0441\u0443\5\u009cO\2\u0442\u0440"+
		"\3\2\2\2\u0443\u0446\3\2\2\2\u0444\u0442\3\2\2\2\u0444\u0445\3\2\2\2\u0445"+
		"\u009b\3\2\2\2\u0446\u0444\3\2\2\2\u0447\u044b\5\u009eP\2\u0448\u0449"+
		"\7`\2\2\u0449\u044a\7a\2\2\u044a\u044c\5\u0152\u00aa\2\u044b\u0448\3\2"+
		"\2\2\u044b\u044c\3\2\2\2\u044c\u009d\3\2\2\2\u044d\u0451\5\u00a0Q\2\u044e"+
		"\u044f\7c\2\2\u044f\u0450\7b\2\2\u0450\u0452\5\u0152\u00aa\2\u0451\u044e"+
		"\3\2\2\2\u0451\u0452\3\2\2\2\u0452\u009f\3\2\2\2\u0453\u0457\5\u00a2R"+
		"\2\u0454\u0455\7d\2\2\u0455\u0456\7F\2\2\u0456\u0458\5\u0152\u00aa\2\u0457"+
		"\u0454\3\2\2\2\u0457\u0458\3\2\2\2\u0458\u00a1\3\2\2\2\u0459\u045d\5\u00a4"+
		"S\2\u045a\u045b\7f\2\2\u045b\u045c\7F\2\2\u045c\u045e\5\u015c\u00af\2"+
		"\u045d\u045a\3\2\2\2\u045d\u045e\3\2\2\2\u045e\u00a3\3\2\2\2\u045f\u0463"+
		"\5\u00a6T\2\u0460\u0461\7e\2\2\u0461\u0462\7F\2\2\u0462\u0464\5\u015c"+
		"\u00af\2\u0463\u0460\3\2\2\2\u0463\u0464\3\2\2\2\u0464\u00a5\3\2\2\2\u0465"+
		"\u046e\5\u00aaV\2\u0466\u0467\7\5\2\2\u0467\u0468\7,\2\2\u0468\u0469\3"+
		"\2\2\2\u0469\u046a\5\u00a8U\2\u046a\u046b\5\u00ceh\2\u046b\u046d\3\2\2"+
		"\2\u046c\u0466\3\2\2\2\u046d\u0470\3\2\2\2\u046e\u046c\3\2\2\2\u046e\u046f"+
		"\3\2\2\2\u046f\u00a7\3\2\2\2\u0470\u046e\3\2\2\2\u0471\u0475\5J&\2\u0472"+
		"\u0475\5\u00c2b\2\u0473\u0475\5\u00c4c\2\u0474\u0471\3\2\2\2\u0474\u0472"+
		"\3\2\2\2\u0474\u0473\3\2\2\2\u0475\u00a9\3\2\2\2\u0476\u0478\t\6\2\2\u0477"+
		"\u0476\3\2\2\2\u0478\u047b\3\2\2\2\u0479\u0477\3\2\2\2\u0479\u047a\3\2"+
		"\2\2\u047a\u047c\3\2\2\2\u047b\u0479\3\2\2\2\u047c\u047d\5\u00acW\2\u047d"+
		"\u00ab\3\2\2\2\u047e\u0482\5\u00b2Z\2\u047f\u0482\5\u00aeX\2\u0480\u0482"+
		"\5\u00b0Y\2\u0481\u047e\3\2\2\2\u0481\u047f\3\2\2\2\u0481\u0480\3\2\2"+
		"\2\u0482\u00ad\3\2\2\2\u0483\u0484\7m\2\2\u0484\u0485\7l\2\2\u0485\u0486"+
		"\5\u0152\u00aa\2\u0486\u0487\7\b\2\2\u0487\u0488\5^\60\2\u0488\u0489\7"+
		"\t\2\2\u0489\u00af\3\2\2\2\u048a\u048b\7n\2\2\u048b\u048c\7l\2\2\u048c"+
		"\u048d\5\u0152\u00aa\2\u048d\u048e\7\b\2\2\u048e\u048f\5^\60\2\u048f\u0490"+
		"\7\t\2\2\u0490\u00b1\3\2\2\2\u0491\u0496\5\u00b4[\2\u0492\u0493\7\64\2"+
		"\2\u0493\u0495\5\u00b4[\2\u0494\u0492\3\2\2\2\u0495\u0498\3\2\2\2\u0496"+
		"\u0494\3\2\2\2\u0496\u0497\3\2\2\2\u0497\u00b3\3\2\2\2\u0498\u0496\3\2"+
		"\2\2\u0499\u04a1\5\u00be`\2\u049a\u04a0\5\u00b6\\\2\u049b\u04a0\5\u00ba"+
		"^\2\u049c\u04a0\5\u00bc_\2\u049d\u04a0\5\u00b8]\2\u049e\u04a0\5\u00ce"+
		"h\2\u049f\u049a\3\2\2\2\u049f\u049b\3\2\2\2\u049f\u049c\3\2\2\2\u049f"+
		"\u049d\3\2\2\2\u049f\u049e\3\2\2\2\u04a0\u04a3\3\2\2\2\u04a1\u049f\3\2"+
		"\2\2\u04a1\u04a2\3\2\2\2\u04a2\u00b5\3\2\2\2\u04a3\u04a1\3\2\2\2\u04a4"+
		"\u04a5\7\65\2\2\u04a5\u04a6\7\65\2\2\u04a6\u04a7\5^\60\2\u04a7\u04a8\7"+
		"\66\2\2\u04a8\u04a9\7\66\2\2\u04a9\u00b7\3\2\2\2\u04aa\u04ab\7\65\2\2"+
		"\u04ab\u04ac\7\66\2\2\u04ac\u00b9\3\2\2\2\u04ad\u04ae\7\65\2\2\u04ae\u04af"+
		"\5^\60\2\u04af\u04b0\7\66\2\2\u04b0\u00bb\3\2\2\2\u04b1\u04b8\7\67\2\2"+
		"\u04b2\u04b9\5\u0166\u00b4\2\u04b3\u04b9\5\u0164\u00b3\2\u04b4\u04b9\7"+
		"\u00b2\2\2\u04b5\u04b9\5\u00c4c\2\u04b6\u04b9\5\u00c2b\2\u04b7\u04b9\5"+
		"\u00c6d\2\u04b8\u04b2\3\2\2\2\u04b8\u04b3\3\2\2\2\u04b8\u04b4\3\2\2\2"+
		"\u04b8\u04b5\3\2\2\2\u04b8\u04b6\3\2\2\2\u04b8\u04b7\3\2\2\2\u04b9\u00bd"+
		"\3\2\2\2\u04ba\u04ca\7\u00ab\2\2\u04bb\u04ca\7j\2\2\u04bc\u04ca\7k\2\2"+
		"\u04bd\u04ca\7\u00ac\2\2\u04be\u04ca\5\u0164\u00b3\2\u04bf\u04ca\5\u00c2"+
		"b\2\u04c0\u04ca\5\u00c4c\2\u04c1\u04ca\5\u00c6d\2\u04c2\u04ca\5\u0154"+
		"\u00ab\2\u04c3\u04ca\5\u00ccg\2\u04c4\u04ca\5\u00c8e\2\u04c5\u04ca\5\u00ca"+
		"f\2\u04c6\u04ca\5\u0160\u00b1\2\u04c7\u04ca\5\u00d2j\2\u04c8\u04ca\5\u00c0"+
		"a\2\u04c9\u04ba\3\2\2\2\u04c9\u04bb\3\2\2\2\u04c9\u04bc\3\2\2\2\u04c9"+
		"\u04bd\3\2\2\2\u04c9\u04be\3\2\2\2\u04c9\u04bf\3\2\2\2\u04c9\u04c0\3\2"+
		"\2\2\u04c9\u04c1\3\2\2\2\u04c9\u04c2\3\2\2\2\u04c9\u04c3\3\2\2\2\u04c9"+
		"\u04c4\3\2\2\2\u04c9\u04c5\3\2\2\2\u04c9\u04c6\3\2\2\2\u04c9\u04c7\3\2"+
		"\2\2\u04c9\u04c8\3\2\2\2\u04ca\u00bf\3\2\2\2\u04cb\u04cc\7\b\2\2\u04cc"+
		"\u04cd\5\20\t\2\u04cd\u04ce\7\t\2\2\u04ce\u00c1\3\2\2\2\u04cf\u04d0\7"+
		"\6\2\2\u04d0\u04d1\5J&\2\u04d1\u00c3\3\2\2\2\u04d2\u04d4\7\n\2\2\u04d3"+
		"\u04d5\5^\60\2\u04d4\u04d3\3\2\2\2\u04d4\u04d5\3\2\2\2\u04d5\u04d6\3\2"+
		"\2\2\u04d6\u04d7\7\13\2\2\u04d7\u00c5\3\2\2\2\u04d8\u04d9\78\2\2\u04d9"+
		"\u00c7\3\2\2\2\u04da\u04db\7\21\2\2\u04db\u04dc\7\b\2\2\u04dc\u04dd\5"+
		"^\60\2\u04dd\u04de\7\t\2\2\u04de\u00c9\3\2\2\2\u04df\u04e0\7i\2\2\u04e0"+
		"\u04e1\7\b\2\2\u04e1\u04e2\5^\60\2\u04e2\u04e3\7\t\2\2\u04e3\u00cb\3\2"+
		"\2\2\u04e4\u04e5\5J&\2\u04e5\u04e6\5\u00ceh\2\u04e6\u00cd\3\2\2\2\u04e7"+
		"\u04ee\7\n\2\2\u04e8\u04ea\5\u00d0i\2\u04e9\u04eb\7\17\2\2\u04ea\u04e9"+
		"\3\2\2\2\u04ea\u04eb\3\2\2\2\u04eb\u04ed\3\2\2\2\u04ec\u04e8\3\2\2\2\u04ed"+
		"\u04f0\3\2\2\2\u04ee\u04ec\3\2\2\2\u04ee\u04ef\3\2\2\2\u04ef\u04f1\3\2"+
		"\2\2\u04f0\u04ee\3\2\2\2\u04f1\u04f2\7\13\2\2\u04f2\u00cf\3\2\2\2\u04f3"+
		"\u04f6\5`\61\2\u04f4\u04f6\7\u00aa\2\2\u04f5\u04f3\3\2\2\2\u04f5\u04f4"+
		"\3\2\2\2\u04f6\u00d1\3\2\2\2\u04f7\u04fa\5\u00d4k\2\u04f8\u04fa\5\u00d6"+
		"l\2\u04f9\u04f7\3\2\2\2\u04f9\u04f8\3\2\2\2\u04fa\u00d3\3\2\2\2\u04fb"+
		"\u04fc\5J&\2\u04fc\u04fd\79\2\2\u04fd\u04fe\7\u00ac\2\2\u04fe\u00d5\3"+
		"\2\2\2\u04ff\u0500\5\64\33\2\u0500\u0501\7\37\2\2\u0501\u0503\7\n\2\2"+
		"\u0502\u0504\5Z.\2\u0503\u0502\3\2\2\2\u0503\u0504\3\2\2\2\u0504\u0505"+
		"\3\2\2\2\u0505\u0508\7\13\2\2\u0506\u0507\7F\2\2\u0507\u0509\5\u0152\u00aa"+
		"\2\u0508\u0506\3\2\2\2\u0508\u0509\3\2\2\2\u0509\u050a\3\2\2\2\u050a\u050b"+
		"\7\b\2\2\u050b\u050c\5\22\n\2\u050c\u050d\7\t\2\2\u050d\u00d7\3\2\2\2"+
		"\u050e\u050f\7s\2\2\u050f\u0510\7|\2\2\u0510\u0511\5`\61\2\u0511\u0512"+
		"\7z\2\2\u0512\u0516\5`\61\2\u0513\u0514\7G\2\2\u0514\u0515\7~\2\2\u0515"+
		"\u0517\5`\61\2\u0516\u0513\3\2\2\2\u0516\u0517\3\2\2\2\u0517\u0526\3\2"+
		"\2\2\u0518\u0519\7s\2\2\u0519\u051a\7|\2\2\u051a\u051f\5\u015e\u00b0\2"+
		"\u051b\u051c\7\17\2\2\u051c\u051e\5\u015e\u00b0\2\u051d\u051b\3\2\2\2"+
		"\u051e\u0521\3\2\2\2\u051f\u051d\3\2\2\2\u051f\u0520\3\2\2\2\u0520\u0522"+
		"\3\2\2\2\u0521\u051f\3\2\2\2\u0522\u0523\7z\2\2\u0523\u0524\5`\61\2\u0524"+
		"\u0526\3\2\2\2\u0525\u050e\3\2\2\2\u0525\u0518\3\2\2\2\u0526\u00d9\3\2"+
		"\2\2\u0527\u0528\7t\2\2\u0528\u0529\7|\2\2\u0529\u052a\5\u00e4s\2\u052a"+
		"\u00db\3\2\2\2\u052b\u052c\7u\2\2\u052c\u052d\7|\2\2\u052d\u052e\5\u00e4"+
		"s\2\u052e\u052f\7F\2\2\u052f\u0530\5`\61\2\u0530\u00dd\3\2\2\2\u0531\u0532"+
		"\7v\2\2\u0532\u0533\7|\2\2\u0533\u0534\7{\2\2\u0534\u0535\7a\2\2\u0535"+
		"\u0536\5\u00e4s\2\u0536\u0537\7}\2\2\u0537\u0538\5`\61\2\u0538\u00df\3"+
		"\2\2\2\u0539\u053a\7w\2\2\u053a\u053b\7|\2\2\u053b\u0540\5\u00e6t\2\u053c"+
		"\u053d\7\17\2\2\u053d\u053f\5\u00e6t\2\u053e\u053c\3\2\2\2\u053f\u0542"+
		"\3\2\2\2\u0540\u053e\3\2\2\2\u0540\u0541\3\2\2\2\u0541\u0543\3\2\2\2\u0542"+
		"\u0540\3\2\2\2\u0543\u0544\7x\2\2\u0544\u0545\5`\61\2\u0545\u0546\7C\2"+
		"\2\u0546\u0547\5`\61\2\u0547\u00e1\3\2\2\2\u0548\u0549\7y\2\2\u0549\u054a"+
		"\7|\2\2\u054a\u054b\5`\61\2\u054b\u054c\7z\2\2\u054c\u054d\5`\61\2\u054d"+
		"\u00e3\3\2\2\2\u054e\u0551\5\u00be`\2\u054f\u0552\5\u00b6\\\2\u0550\u0552"+
		"\5\u00bc_\2\u0551\u054f\3\2\2\2\u0551\u0550\3\2\2\2\u0552\u0553\3\2\2"+
		"\2\u0553\u0551\3\2\2\2\u0553\u0554\3\2\2\2\u0554\u00e5\3\2\2\2\u0555\u0556"+
		"\5\u00c2b\2\u0556\u0557\7\7\2\2\u0557\u0558\5`\61\2\u0558\u00e7\3\2\2"+
		"\2\u0559\u055a\7\u0085\2\2\u055a\u055c\7\u0086\2\2\u055b\u055d\5\u00ea"+
		"v\2\u055c\u055b\3\2\2\2\u055c\u055d\3\2\2\2\u055d\u055e\3\2\2\2\u055e"+
		"\u0568\5\u0162\u00b2\2\u055f\u0560\7G\2\2\u0560\u0565\5\u0162\u00b2\2"+
		"\u0561\u0562\7\17\2\2\u0562\u0564\5\u0162\u00b2\2\u0563\u0561\3\2\2\2"+
		"\u0564\u0567\3\2\2\2\u0565\u0563\3\2\2\2\u0565\u0566\3\2\2\2\u0566\u0569"+
		"\3\2\2\2\u0567\u0565\3\2\2\2\u0568\u055f\3\2\2\2\u0568\u0569\3\2\2\2\u0569"+
		"\u00e9\3\2\2\2\u056a\u056b\7\u0087\2\2\u056b\u056c\7\u00b2\2\2\u056c\u0571"+
		"\7\5\2\2\u056d\u056e\7X\2\2\u056e\u056f\7\u0088\2\2\u056f\u0571\7\u0087"+
		"\2\2\u0570\u056a\3\2\2\2\u0570\u056d\3\2\2\2\u0571\u00eb\3\2\2\2\u0572"+
		"\u0574\7\u0089\2\2\u0573\u0575\5\u00eex\2\u0574\u0573\3\2\2\2\u0574\u0575"+
		"\3\2\2\2\u0575\u057a\3\2\2\2\u0576\u0577\7\u008a\2\2\u0577\u057a\5\u00ee"+
		"x\2\u0578\u057a\5\u00eex\2\u0579\u0572\3\2\2\2\u0579\u0576\3\2\2\2\u0579"+
		"\u0578\3\2\2\2\u057a\u00ed\3\2\2\2\u057b\u0580\5\u00f0y\2\u057c\u057d"+
		"\t\b\2\2\u057d\u057f\5\u00f0y\2\u057e\u057c\3\2\2\2\u057f\u0582\3\2\2"+
		"\2\u0580\u057e\3\2\2\2\u0580\u0581\3\2\2\2\u0581\u00ef\3\2\2\2\u0582\u0580"+
		"\3\2\2\2\u0583\u0586\5\u00b4[\2\u0584\u0586\5\u00f2z\2\u0585\u0583\3\2"+
		"\2\2\u0585\u0584\3\2\2\2\u0586\u00f1\3\2\2\2\u0587\u058a\5\u00fa~\2\u0588"+
		"\u058a\5\u00f4{\2\u0589\u0587\3\2\2\2\u0589\u0588\3\2\2\2\u058a\u058b"+
		"\3\2\2\2\u058b\u058c\5\u010a\u0086\2\u058c\u00f3\3\2\2\2\u058d\u058e\5"+
		"\u00f6|\2\u058e\u058f\5\u0100\u0081\2\u058f\u0592\3\2\2\2\u0590\u0592"+
		"\5\u00f8}\2\u0591\u058d\3\2\2\2\u0591\u0590\3\2\2\2\u0592\u00f5\3\2\2"+
		"\2\u0593\u0594\t\t\2\2\u0594\u0595\7\23\2\2\u0595\u0596\7\23\2\2\u0596"+
		"\u00f7\3\2\2\2\u0597\u0599\7\u008b\2\2\u0598\u0597\3\2\2\2\u0598\u0599"+
		"\3\2\2\2\u0599\u059a\3\2\2\2\u059a\u059b\5\u0100\u0081\2\u059b\u00f9\3"+
		"\2\2\2\u059c\u059d\5\u00fc\177\2\u059d\u059e\5\u0100\u0081\2\u059e\u05a1"+
		"\3\2\2\2\u059f\u05a1\5\u00fe\u0080\2\u05a0\u059c\3\2\2\2\u05a0\u059f\3"+
		"\2\2\2\u05a1\u00fb\3\2\2\2\u05a2\u05a3\t\n\2\2\u05a3\u05a4\7\23\2\2\u05a4"+
		"\u05a5\7\23\2\2\u05a5\u00fd\3\2\2\2\u05a6\u05a7\7:\2\2\u05a7\u00ff\3\2"+
		"\2\2\u05a8\u05ab\5\u0102\u0082\2\u05a9\u05ab\5\u0110\u0089\2\u05aa\u05a8"+
		"\3\2\2\2\u05aa\u05a9\3\2\2\2\u05ab\u0101\3\2\2\2\u05ac\u05ad\5J&\2\u05ad"+
		"\u05ae\5\u0104\u0083\2\u05ae\u0103\3\2\2\2\u05af\u05b3\7\f\2\2\u05b0\u05b3"+
		"\5\u0106\u0084\2\u05b1\u05b3\5\u0108\u0085\2\u05b2\u05af\3\2\2\2\u05b2"+
		"\u05b0\3\2\2\2\u05b2\u05b1\3\2\2\2\u05b3\u0105\3\2\2\2\u05b4\u05b5\7\u00b2"+
		"\2\2\u05b5\u05b6\7\23\2\2\u05b6\u05b7\7\f\2\2\u05b7\u0107\3\2\2\2\u05b8"+
		"\u05b9\7\f\2\2\u05b9\u05ba\7\23\2\2\u05ba\u05bb\7\u00b2\2\2\u05bb\u0109"+
		"\3\2\2\2\u05bc\u05be\5\u00ba^\2\u05bd\u05bc\3\2\2\2\u05be\u05c1\3\2\2"+
		"\2\u05bf\u05bd\3\2\2\2\u05bf\u05c0\3\2\2\2\u05c0\u010b\3\2\2\2\u05c1\u05bf"+
		"\3\2\2\2\u05c2\u05ce\5J&\2\u05c3\u05ce\7\u00ab\2\2\u05c4\u05ce\5\u0110"+
		"\u0089\2\u05c5\u05c6\7q\2\2\u05c6\u05c7\7\n\2\2\u05c7\u05ce\7\13\2\2\u05c8"+
		"\u05ce\5\u0156\u00ac\2\u05c9\u05ce\5\u0136\u009c\2\u05ca\u05ce\5\u013c"+
		"\u009f\2\u05cb\u05ce\5\u010e\u0088\2\u05cc\u05ce\5\u0142\u00a2\2\u05cd"+
		"\u05c2\3\2\2\2\u05cd\u05c3\3\2\2\2\u05cd\u05c4\3\2\2\2\u05cd\u05c5\3\2"+
		"\2\2\u05cd\u05c8\3\2\2\2\u05cd\u05c9\3\2\2\2\u05cd\u05ca\3\2\2\2\u05cd"+
		"\u05cb\3\2\2\2\u05cd\u05cc\3\2\2\2\u05ce\u010d\3\2\2\2\u05cf\u05d0\5J"+
		"&\2\u05d0\u010f\3\2\2\2\u05d1\u05de\5\u0116\u008c\2\u05d2\u05de\5\u0126"+
		"\u0094\2\u05d3\u05de\5\u0120\u0091\2\u05d4\u05de\5\u012a\u0096\2\u05d5"+
		"\u05de\5\u0124\u0093\2\u05d6\u05de\5\u011e\u0090\2\u05d7\u05de\5\u011a"+
		"\u008e\2\u05d8\u05de\5\u0118\u008d\2\u05d9\u05de\5\u011c\u008f\2\u05da"+
		"\u05de\5\u0146\u00a4\2\u05db\u05de\5\u0114\u008b\2\u05dc\u05de\5\u0112"+
		"\u008a\2\u05dd\u05d1\3\2\2\2\u05dd\u05d2\3\2\2\2\u05dd\u05d3\3\2\2\2\u05dd"+
		"\u05d4\3\2\2\2\u05dd\u05d5\3\2\2\2\u05dd\u05d6\3\2\2\2\u05dd\u05d7\3\2"+
		"\2\2\u05dd\u05d8\3\2\2\2\u05dd\u05d9\3\2\2\2\u05dd\u05da\3\2\2\2\u05dd"+
		"\u05db\3\2\2\2\u05dd\u05dc\3\2\2\2\u05de\u0111\3\2\2\2\u05df\u05e0\7\u0098"+
		"\2\2\u05e0\u05e2\7\n\2\2\u05e1\u05e3\7\f\2\2\u05e2\u05e1\3\2\2\2\u05e2"+
		"\u05e3\3\2\2\2\u05e3\u05e4\3\2\2\2\u05e4\u05e5\7\13\2\2\u05e5\u0113\3"+
		"\2\2\2\u05e6\u05e7\7\u0099\2\2\u05e7\u05e8\7\n\2\2\u05e8\u05e9\7\13\2"+
		"\2\u05e9\u0115\3\2\2\2\u05ea\u05eb\7\u009b\2\2\u05eb\u05ee\7\n\2\2\u05ec"+
		"\u05ef\5\u0126\u0094\2\u05ed\u05ef\5\u012a\u0096\2\u05ee\u05ec\3\2\2\2"+
		"\u05ee\u05ed\3\2\2\2\u05ee\u05ef\3\2\2\2\u05ef\u05f0\3\2\2\2\u05f0\u05f1"+
		"\7\13\2\2\u05f1\u0117\3\2\2\2\u05f2\u05f3\7\u009c\2\2\u05f3\u05f4\7\n"+
		"\2\2\u05f4\u05f5\7\13\2\2\u05f5\u0119\3\2\2\2\u05f6\u05f7\7\u00a6\2\2"+
		"\u05f7\u05f8\7\n\2\2\u05f8\u05f9\7\13\2\2\u05f9\u011b\3\2\2\2\u05fa\u05fb"+
		"\7\u009e\2\2\u05fb\u05fc\7\n\2\2\u05fc\u05fd\7\13\2\2\u05fd\u011d\3\2"+
		"\2\2\u05fe\u05ff\7\u009d\2\2\u05ff\u0602\7\n\2\2\u0600\u0603\7\u00b2\2"+
		"\2\u0601\u0603\5\u0164\u00b3\2\u0602\u0600\3\2\2\2\u0602\u0601\3\2\2\2"+
		"\u0602\u0603\3\2\2\2\u0603\u0604\3\2\2\2\u0604\u0605\7\13\2\2\u0605\u011f"+
		"\3\2\2\2\u0606\u0607\7\u008e\2\2\u0607\u060d\7\n\2\2\u0608\u060b\5\u0122"+
		"\u0092\2\u0609\u060a\7\17\2\2\u060a\u060c\5\u0134\u009b\2\u060b\u0609"+
		"\3\2\2\2\u060b\u060c\3\2\2\2\u060c\u060e\3\2\2\2\u060d\u0608\3\2\2\2\u060d"+
		"\u060e\3\2\2\2\u060e\u060f\3\2\2\2\u060f\u0610\7\13\2\2\u0610\u0121\3"+
		"\2\2\2\u0611\u0614\5\u012e\u0098\2\u0612\u0614\7\f\2\2\u0613\u0611\3\2"+
		"\2\2\u0613\u0612\3\2\2\2\u0614\u0123\3\2\2\2\u0615\u0616\7\u009f\2\2\u0616"+
		"\u0617\7\n\2\2\u0617\u0618\5\u0144\u00a3\2\u0618\u0619\7\13\2\2\u0619"+
		"\u0125\3\2\2\2\u061a\u061b\7\u0088\2\2\u061b\u0624\7\n\2\2\u061c\u0622"+
		"\5\u0128\u0095\2\u061d\u061e\7\17\2\2\u061e\u0620\5\u0134\u009b\2\u061f"+
		"\u0621\7\u00aa\2\2\u0620\u061f\3\2\2\2\u0620\u0621\3\2\2\2\u0621\u0623"+
		"\3\2\2\2\u0622\u061d\3\2\2\2\u0622\u0623\3\2\2\2\u0623\u0625\3\2\2\2\u0624"+
		"\u061c\3\2\2\2\u0624\u0625\3\2\2\2\u0625\u0626\3\2\2\2\u0626\u0627\7\13"+
		"\2\2\u0627\u0127\3\2\2\2\u0628\u062b\5\u0130\u0099\2\u0629\u062b\7\f\2"+
		"\2\u062a\u0628\3\2\2\2\u062a\u0629\3\2\2\2\u062b\u0129\3\2\2\2\u062c\u062d"+
		"\7\u00a0\2\2\u062d\u062e\7\n\2\2\u062e\u062f\5\u012c\u0097\2\u062f\u0630"+
		"\7\13\2\2\u0630\u012b\3\2\2\2\u0631\u0632\5\u0130\u0099\2\u0632\u012d"+
		"\3\2\2\2\u0633\u0634\5J&\2\u0634\u012f\3\2\2\2\u0635\u0636\5J&\2\u0636"+
		"\u0131\3\2\2\2\u0637\u0638\5\u0134\u009b\2\u0638\u0133\3\2\2\2\u0639\u063a"+
		"\5J&\2\u063a\u0135\3\2\2\2\u063b\u063e\5\u0138\u009d\2\u063c\u063e\5\u013a"+
		"\u009e\2\u063d\u063b\3\2\2\2\u063d\u063c\3\2\2\2\u063e\u0137\3\2\2\2\u063f"+
		"\u0640\7\u00a8\2\2\u0640\u0641\7\n\2\2\u0641\u0642\7\f\2\2\u0642\u0643"+
		"\7\13\2\2\u0643\u0139\3\2\2\2\u0644\u0645\7\u00a8\2\2\u0645\u0646\7\n"+
		"\2\2\u0646\u0647\5J&\2\u0647\u0648\7\17\2\2\u0648\u0649\5\u0152\u00aa"+
		"\2\u0649\u064a\7\13\2\2\u064a\u013b\3\2\2\2\u064b\u064e\5\u013e\u00a0"+
		"\2\u064c\u064e\5\u0140\u00a1\2\u064d\u064b\3\2\2\2\u064d\u064c\3\2\2\2"+
		"\u064e\u013d\3\2\2\2\u064f\u0650\7\u00a7\2\2\u0650\u0651\7\n\2\2\u0651"+
		"\u0652\7\f\2\2\u0652\u0653\7\13\2\2\u0653\u013f\3\2\2\2\u0654\u0655\7"+
		"\u00a7\2\2\u0655\u0656\7\n\2\2\u0656\u0657\5\u0152\u00aa\2\u0657\u0658"+
		"\7\13\2\2\u0658\u0141\3\2\2\2\u0659\u065a\7\n\2\2\u065a\u065b\5\u010c"+
		"\u0087\2\u065b\u065c\7\13\2\2\u065c\u0143\3\2\2\2\u065d\u065e\5\u012e"+
		"\u0098\2\u065e\u0145\3\2\2\2\u065f\u0665\5\u0148\u00a5\2\u0660\u0665\5"+
		"\u014a\u00a6\2\u0661\u0665\5\u014c\u00a7\2\u0662\u0665\5\u014e\u00a8\2"+
		"\u0663\u0665\5\u0150\u00a9\2\u0664\u065f\3\2\2\2\u0664\u0660\3\2\2\2\u0664"+
		"\u0661\3\2\2\2\u0664\u0662\3\2\2\2\u0664\u0663\3\2\2\2\u0665\u0147\3\2"+
		"\2\2\u0666\u0667\7\u00a1\2\2\u0667\u0669\7\n\2\2\u0668\u066a\5\u0164\u00b3"+
		"\2\u0669\u0668\3\2\2\2\u0669\u066a\3\2\2\2\u066a\u066b\3\2\2\2\u066b\u066c"+
		"\7\13\2\2\u066c\u0149\3\2\2\2\u066d\u066e\7\u00a5\2\2\u066e\u0670\7\n"+
		"\2\2\u066f\u0671\5\u0164\u00b3\2\u0670\u066f\3\2\2\2\u0670\u0671\3\2\2"+
		"\2\u0671\u0672\3\2\2\2\u0672\u0673\7\13\2\2\u0673\u014b\3\2\2\2\u0674"+
		"\u0675\7\u00a4\2\2\u0675\u0677\7\n\2\2\u0676\u0678\5\u0164\u00b3\2\u0677"+
		"\u0676\3\2\2\2\u0677\u0678\3\2\2\2\u0678\u0679\3\2\2\2\u0679\u067a\7\13"+
		"\2\2\u067a\u014d\3\2\2\2\u067b\u067c\7\u00a2\2\2\u067c\u067e\7\n\2\2\u067d"+
		"\u067f\5\u0164\u00b3\2\u067e\u067d\3\2\2\2\u067e\u067f\3\2\2\2\u067f\u0680"+
		"\3\2\2\2\u0680\u0681\7\13\2\2\u0681\u014f\3\2\2\2\u0682\u0683\7\u00a3"+
		"\2\2\u0683\u0685\7\n\2\2\u0684\u0686\5\u0164\u00b3\2\u0685\u0684\3\2\2"+
		"\2\u0685\u0686\3\2\2\2\u0686\u0687\3\2\2\2\u0687\u0688\7\13\2\2\u0688"+
		"\u0151\3\2\2\2\u0689\u068a\7\n\2\2\u068a\u0692\7\13\2\2\u068b\u068f\5"+
		"\u010c\u0087\2\u068c\u0690\7\u00aa\2\2\u068d\u0690\7\f\2\2\u068e\u0690"+
		"\7/\2\2\u068f\u068c\3\2\2\2\u068f\u068d\3\2\2\2\u068f\u068e\3\2\2\2\u068f"+
		"\u0690\3\2\2\2\u0690\u0692\3\2\2\2\u0691\u0689\3\2\2\2\u0691\u068b\3\2"+
		"\2\2\u0692\u0153\3\2\2\2\u0693\u069c\7\b\2\2\u0694\u0699\5\u015e\u00b0"+
		"\2\u0695\u0696\7\17\2\2\u0696\u0698\5\u015e\u00b0\2\u0697\u0695\3\2\2"+
		"\2\u0698\u069b\3\2\2\2\u0699\u0697\3\2\2\2\u0699\u069a\3\2\2\2\u069a\u069d"+
		"\3\2\2\2\u069b\u0699\3\2\2\2\u069c\u0694\3\2\2\2\u069c\u069d\3\2\2\2\u069d"+
		"\u069e\3\2\2\2\u069e\u06a4\7\t\2\2\u069f\u06a0\7;\2\2\u06a0\u06a1\5^\60"+
		"\2\u06a1\u06a2\7<\2\2\u06a2\u06a4\3\2\2\2\u06a3\u0693\3\2\2\2\u06a3\u069f"+
		"\3\2\2\2\u06a4\u0155\3\2\2\2\u06a5\u06a8\5\u0158\u00ad\2\u06a6\u06a8\5"+
		"\u015a\u00ae\2\u06a7\u06a5\3\2\2\2\u06a7\u06a6\3\2\2\2\u06a8\u0157\3\2"+
		"\2\2\u06a9\u06aa\7\37\2\2\u06aa\u06ab\7\n\2\2\u06ab\u06ac\7\f\2\2\u06ac"+
		"\u06ad\7\13\2\2\u06ad\u0159\3\2\2\2\u06ae\u06af\7\37\2\2\u06af\u06b8\7"+
		"\n\2\2\u06b0\u06b5\5\u0152\u00aa\2\u06b1\u06b2\7\17\2\2\u06b2\u06b4\5"+
		"\u0152\u00aa\2\u06b3\u06b1\3\2\2\2\u06b4\u06b7\3\2\2\2\u06b5\u06b3\3\2"+
		"\2\2\u06b5\u06b6\3\2\2\2\u06b6\u06b9\3\2\2\2\u06b7\u06b5\3\2\2\2\u06b8"+
		"\u06b0\3\2\2\2\u06b8\u06b9\3\2\2\2\u06b9\u06ba\3\2\2\2\u06ba\u06bb\7\13"+
		"\2\2\u06bb\u06bc\7F\2\2\u06bc\u06bd\5\u0152\u00aa\2\u06bd\u015b\3\2\2"+
		"\2\u06be\u06c0\5\u010c\u0087\2\u06bf\u06c1\7\u00aa\2\2\u06c0\u06bf\3\2"+
		"\2\2\u06c0\u06c1\3\2\2\2\u06c1\u015d\3\2\2\2\u06c2\u06c5\5`\61\2\u06c3"+
		"\u06c5\7\u00b2\2\2\u06c4\u06c2\3\2\2\2\u06c4\u06c3\3\2\2\2\u06c5\u06c6"+
		"\3\2\2\2\u06c6\u06c7\t\13\2\2\u06c7\u06c8\5`\61\2\u06c8\u015f\3\2\2\2"+
		"\u06c9\u06cb\7\65\2\2\u06ca\u06cc\5^\60\2\u06cb\u06ca\3\2\2\2\u06cb\u06cc"+
		"\3\2\2\2\u06cc\u06cd\3\2\2\2\u06cd\u06ce\7\66\2\2\u06ce\u0161\3\2\2\2"+
		"\u06cf\u06d0\5\u0164\u00b3\2\u06d0\u0163\3\2\2\2\u06d1\u06d2\7\u00a9\2"+
		"\2\u06d2\u0165\3\2\2\2\u06d3\u06d4\t\f\2\2\u06d4\u0167\3\2\2\2\u00a8\u0170"+
		"\u0174\u0184\u018a\u0192\u019a\u01a2\u01b1\u01cf\u01d7\u01d9\u01ef\u01f9"+
		"\u0203\u0208\u020d\u0211\u021d\u0221\u022a\u0231\u023f\u0243\u0248\u0252"+
		"\u025a\u025e\u026a\u0276\u028c\u0294\u0299\u029c\u02a0\u02a9\u02b2\u02b5"+
		"\u02bd\u02c4\u02c6\u02cd\u02d4\u02d6\u02de\u02e3\u02ea\u02f1\u02fb\u0302"+
		"\u0309\u0310\u0319\u0323\u0327\u032f\u0331\u033d\u0343\u0347\u034b\u0356"+
		"\u035c\u036b\u0371\u0375\u0379\u0380\u0387\u038d\u0392\u0394\u0398\u039f"+
		"\u03a6\u03af\u03bb\u03c5\u03d1\u03d5\u03de\u03e5\u03fb\u0400\u0405\u0409"+
		"\u0415\u041d\u0421\u0428\u042f\u0435\u043c\u0444\u044b\u0451\u0457\u045d"+
		"\u0463\u046e\u0474\u0479\u0481\u0496\u049f\u04a1\u04b8\u04c9\u04d4\u04ea"+
		"\u04ee\u04f5\u04f9\u0503\u0508\u0516\u051f\u0525\u0540\u0551\u0553\u055c"+
		"\u0565\u0568\u0570\u0574\u0579\u0580\u0585\u0589\u0591\u0598\u05a0\u05aa"+
		"\u05b2\u05bf\u05cd\u05dd\u05e2\u05ee\u0602\u060b\u060d\u0613\u0620\u0622"+
		"\u0624\u062a\u063d\u064d\u0664\u0669\u0670\u0677\u067e\u0685\u068f\u0691"+
		"\u0699\u069c\u06a3\u06a7\u06b5\u06b8\u06c0\u06c4\u06cb";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}