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
		Kinto=120, Kvalue=121, Kwith=122, Kposition=123, Kjson=124, Kupdating=125, 
		Kimport=126, Kschema=127, Knamespace=128, Kelement=129, Kslash=130, Kdslash=131, 
		Kat_symbol=132, Kchild=133, Kdescendant=134, Kattribute=135, Kself=136, 
		Kdescendant_or_self=137, Kfollowing_sibling=138, Kfollowing=139, Kparent=140, 
		Kancestor=141, Kpreceding_sibling=142, Kpreceding=143, Kancestor_or_self=144, 
		Knode=145, Kbinary=146, Kdocument=147, Kdocument_node=148, Ktext=149, 
		Kpi=150, Knamespace_node=151, Kschema_attribute=152, Kschema_element=153, 
		Karray_node=154, Kboolean_node=155, Knull_node=156, Knumber_node=157, 
		Kobject_node=158, Kcomment=159, Kbreak=160, Kloop=161, Kcontinue=162, 
		Kexit=163, Kreturning=164, Kwhile=165, STRING=166, ArgumentPlaceholder=167, 
		NullLiteral=168, Literal=169, NumericLiteral=170, IntegerLiteral=171, 
		DecimalLiteral=172, DoubleLiteral=173, WS=174, NCName=175, XQComment=176, 
		ContentChar=177;
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
		RULE_copyDecl = 114, RULE_pathExpr = 115, RULE_relativePathExpr = 116, 
		RULE_stepExpr = 117, RULE_axisStep = 118, RULE_forwardStep = 119, RULE_forwardAxis = 120, 
		RULE_abbrevForwardStep = 121, RULE_reverseStep = 122, RULE_reverseAxis = 123, 
		RULE_abbrevReverseStep = 124, RULE_nodeTest = 125, RULE_nameTest = 126, 
		RULE_wildcard = 127, RULE_nCNameWithLocalWildcard = 128, RULE_nCNameWithPrefixWildcard = 129, 
		RULE_predicateList = 130, RULE_kindTest = 131, RULE_anyKindTest = 132, 
		RULE_binaryNodeTest = 133, RULE_documentTest = 134, RULE_textTest = 135, 
		RULE_commentTest = 136, RULE_namespaceNodeTest = 137, RULE_piTest = 138, 
		RULE_attributeTest = 139, RULE_attributeNameOrWildcard = 140, RULE_schemaAttributeTest = 141, 
		RULE_attributeDeclaration = 142, RULE_elementTest = 143, RULE_elementNameOrWildcard = 144, 
		RULE_schemaElementTest = 145, RULE_elementDeclaration = 146, RULE_attributeName = 147, 
		RULE_elementName = 148, RULE_simpleTypeName = 149, RULE_typeName = 150, 
		RULE_sequenceType = 151, RULE_objectConstructor = 152, RULE_itemType = 153, 
		RULE_functionTest = 154, RULE_anyFunctionTest = 155, RULE_typedFunctionTest = 156, 
		RULE_singleType = 157, RULE_pairConstructor = 158, RULE_arrayConstructor = 159, 
		RULE_uriLiteral = 160, RULE_stringLiteral = 161, RULE_keyWords = 162;
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
			"copyDecl", "pathExpr", "relativePathExpr", "stepExpr", "axisStep", "forwardStep", 
			"forwardAxis", "abbrevForwardStep", "reverseStep", "reverseAxis", "abbrevReverseStep", 
			"nodeTest", "nameTest", "wildcard", "nCNameWithLocalWildcard", "nCNameWithPrefixWildcard", 
			"predicateList", "kindTest", "anyKindTest", "binaryNodeTest", "documentTest", 
			"textTest", "commentTest", "namespaceNodeTest", "piTest", "attributeTest", 
			"attributeNameOrWildcard", "schemaAttributeTest", "attributeDeclaration", 
			"elementTest", "elementNameOrWildcard", "schemaElementTest", "elementDeclaration", 
			"attributeName", "elementName", "simpleTypeName", "typeName", "sequenceType", 
			"objectConstructor", "itemType", "functionTest", "anyFunctionTest", "typedFunctionTest", 
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
			"'into'", "'value'", "'with'", "'position'", "'json'", "'updating'", 
			"'import'", "'schema'", "'namespace'", "'element'", "'/'", "'//'", "'@'", 
			"'child'", "'descendant'", "'attribute'", "'self'", "'descendant-or-self'", 
			"'following-sibling'", "'following'", "'parent'", "'ancestor'", "'preceding-sibling'", 
			"'preceding'", "'ancestor-or-self'", "'node'", "'binary'", "'document'", 
			"'document-node'", "'text'", "'processing-instruction'", "'namespace-node'", 
			"'schema-attribute'", "'schema-element'", "'array-node'", "'boolean-node'", 
			"'null-node'", "'number-node'", "'object-node'", "'comment'", "'break'", 
			"'loop'", "'continue'", "'exit'", "'returning'", "'while'", null, "'?'", 
			"'null'"
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
			"Kcopy", "Kmodify", "Kappend", "Kinto", "Kvalue", "Kwith", "Kposition", 
			"Kjson", "Kupdating", "Kimport", "Kschema", "Knamespace", "Kelement", 
			"Kslash", "Kdslash", "Kat_symbol", "Kchild", "Kdescendant", "Kattribute", 
			"Kself", "Kdescendant_or_self", "Kfollowing_sibling", "Kfollowing", "Kparent", 
			"Kancestor", "Kpreceding_sibling", "Kpreceding", "Kancestor_or_self", 
			"Knode", "Kbinary", "Kdocument", "Kdocument_node", "Ktext", "Kpi", "Knamespace_node", 
			"Kschema_attribute", "Kschema_element", "Karray_node", "Kboolean_node", 
			"Knull_node", "Knumber_node", "Kobject_node", "Kcomment", "Kbreak", "Kloop", 
			"Kcontinue", "Kexit", "Kreturning", "Kwhile", "STRING", "ArgumentPlaceholder", 
			"NullLiteral", "Literal", "NumericLiteral", "IntegerLiteral", "DecimalLiteral", 
			"DoubleLiteral", "WS", "NCName", "XQComment", "ContentChar"
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
			setState(326);
			module();
			setState(327);
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
			setState(334);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(329);
				match(Kjsoniq);
				setState(330);
				match(Kversion);
				setState(331);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(332);
				match(T__0);
				}
				break;
			}
			setState(338);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(336);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
			case Kimport:
			case Kslash:
			case Kdslash:
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
				{
				setState(337);
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
			setState(340);
			prolog();
			setState(341);
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
			setState(343);
			match(T__1);
			setState(344);
			match(Knamespace);
			setState(345);
			match(NCName);
			setState(346);
			match(T__2);
			setState(347);
			uriLiteral();
			setState(348);
			match(T__0);
			setState(349);
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
			setState(360);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(354);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(351);
						setter();
						}
						break;
					case 2:
						{
						setState(352);
						namespaceDecl();
						}
						break;
					case 3:
						{
						setState(353);
						moduleImport();
						}
						break;
					}
					setState(356);
					match(T__0);
					}
					} 
				}
				setState(362);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(368);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(363);
					annotatedDecl();
					setState(364);
					match(T__0);
					}
					} 
				}
				setState(370);
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
			setState(371);
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
			setState(376);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(373);
					statement();
					}
					} 
				}
				setState(378);
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
			setState(379);
			statements();
			setState(380);
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
			setState(382);
			statements();
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__55) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kjson - 64)) | (1L << (Kupdating - 64)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (Kslash - 130)) | (1L << (Kdslash - 130)) | (1L << (Kat_symbol - 130)) | (1L << (Kchild - 130)) | (1L << (Kdescendant - 130)) | (1L << (Kattribute - 130)) | (1L << (Kself - 130)) | (1L << (Kdescendant_or_self - 130)) | (1L << (Kfollowing_sibling - 130)) | (1L << (Kfollowing - 130)) | (1L << (Kparent - 130)) | (1L << (Kancestor - 130)) | (1L << (Kpreceding_sibling - 130)) | (1L << (Kpreceding - 130)) | (1L << (Kancestor_or_self - 130)) | (1L << (Kbreak - 130)) | (1L << (Kloop - 130)) | (1L << (Kcontinue - 130)) | (1L << (Kexit - 130)) | (1L << (Kreturning - 130)) | (1L << (Kwhile - 130)) | (1L << (STRING - 130)) | (1L << (NullLiteral - 130)) | (1L << (Literal - 130)) | (1L << (NCName - 130)))) != 0)) {
				{
				setState(383);
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
			setState(399);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(386);
				applyStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(387);
				assignStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(388);
				blockStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(389);
				breakStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(390);
				continueStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(391);
				exitStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(392);
				flowrStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(393);
				ifStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(394);
				switchStatement();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(395);
				tryCatchStatement();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(396);
				typeSwitchStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(397);
				varDeclStatement();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(398);
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
			setState(401);
			exprSimple();
			setState(402);
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
			setState(404);
			match(T__3);
			setState(405);
			qname();
			setState(406);
			match(T__4);
			setState(407);
			exprSingle();
			setState(408);
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
			setState(410);
			match(T__5);
			setState(411);
			statements();
			setState(412);
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
			setState(414);
			match(Kbreak);
			setState(415);
			match(Kloop);
			setState(416);
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
			setState(418);
			match(Kcontinue);
			setState(419);
			match(Kloop);
			setState(420);
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
			setState(422);
			match(Kexit);
			setState(423);
			match(Kreturning);
			setState(424);
			exprSingle();
			setState(425);
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
			setState(429);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(427);
				((FlowrStatementContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(428);
				((FlowrStatementContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(439);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (Kfor - 59)) | (1L << (Klet - 59)) | (1L << (Kwhere - 59)) | (1L << (Kgroup - 59)) | (1L << (Korder - 59)) | (1L << (Kcount - 59)) | (1L << (Kstable - 59)))) != 0)) {
				{
				setState(437);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(431);
					forClause();
					}
					break;
				case Klet:
					{
					setState(432);
					letClause();
					}
					break;
				case Kwhere:
					{
					setState(433);
					whereClause();
					}
					break;
				case Kgroup:
					{
					setState(434);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(435);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(436);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(441);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(442);
			match(Kreturn);
			setState(443);
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
			setState(445);
			match(Kif);
			setState(446);
			match(T__7);
			setState(447);
			((IfStatementContext)_localctx).test_expr = expr();
			setState(448);
			match(T__8);
			setState(449);
			match(Kthen);
			setState(450);
			((IfStatementContext)_localctx).branch = statement();
			setState(451);
			match(Kelse);
			setState(452);
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
			setState(454);
			match(Kswitch);
			setState(455);
			match(T__7);
			setState(456);
			((SwitchStatementContext)_localctx).condExpr = expr();
			setState(457);
			match(T__8);
			setState(459); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(458);
				((SwitchStatementContext)_localctx).switchCaseStatement = switchCaseStatement();
				((SwitchStatementContext)_localctx).cases.add(((SwitchStatementContext)_localctx).switchCaseStatement);
				}
				}
				setState(461); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(463);
			match(Kdefault);
			setState(464);
			match(Kreturn);
			setState(465);
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
			setState(469); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(467);
				match(Kcase);
				setState(468);
				((SwitchCaseStatementContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseStatementContext)_localctx).cond.add(((SwitchCaseStatementContext)_localctx).exprSingle);
				}
				}
				setState(471); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(473);
			match(Kreturn);
			setState(474);
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
			setState(476);
			match(Ktry);
			setState(477);
			((TryCatchStatementContext)_localctx).try_block = blockStatement();
			setState(479); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(478);
					((TryCatchStatementContext)_localctx).catchCaseStatement = catchCaseStatement();
					((TryCatchStatementContext)_localctx).catches.add(((TryCatchStatementContext)_localctx).catchCaseStatement);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(481); 
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
			setState(483);
			match(Kcatch);
			setState(486);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				{
				setState(484);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				{
				setState(485);
				((CatchCaseStatementContext)_localctx).qname = qname();
				((CatchCaseStatementContext)_localctx).errors.add(((CatchCaseStatementContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(495);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(488);
				match(T__10);
				setState(491);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__9:
					{
					setState(489);
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
				case Kwith:
				case Kposition:
				case Kjson:
				case Kupdating:
				case Kbreak:
				case Kloop:
				case Kcontinue:
				case Kexit:
				case Kreturning:
				case Kwhile:
				case NullLiteral:
				case NCName:
					{
					setState(490);
					((CatchCaseStatementContext)_localctx).qname = qname();
					((CatchCaseStatementContext)_localctx).errors.add(((CatchCaseStatementContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(497);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(498);
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
			setState(500);
			match(Ktypeswitch);
			setState(501);
			match(T__7);
			setState(502);
			((TypeSwitchStatementContext)_localctx).cond = expr();
			setState(503);
			match(T__8);
			setState(505); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(504);
				((TypeSwitchStatementContext)_localctx).caseStatement = caseStatement();
				((TypeSwitchStatementContext)_localctx).cases.add(((TypeSwitchStatementContext)_localctx).caseStatement);
				}
				}
				setState(507); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(509);
			match(Kdefault);
			setState(511);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(510);
				((TypeSwitchStatementContext)_localctx).var_ref = varRef();
				}
			}

			setState(513);
			match(Kreturn);
			setState(514);
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
			setState(516);
			match(Kcase);
			setState(520);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(517);
				((CaseStatementContext)_localctx).var_ref = varRef();
				setState(518);
				match(Kas);
				}
			}

			setState(522);
			((CaseStatementContext)_localctx).sequenceType = sequenceType();
			((CaseStatementContext)_localctx).union.add(((CaseStatementContext)_localctx).sequenceType);
			setState(527);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(523);
				match(T__10);
				setState(524);
				((CaseStatementContext)_localctx).sequenceType = sequenceType();
				((CaseStatementContext)_localctx).union.add(((CaseStatementContext)_localctx).sequenceType);
				}
				}
				setState(529);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(530);
			match(Kreturn);
			setState(531);
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
		public Token updating;
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TerminalNode Kupdating() { return getToken(JsoniqParser.Kupdating, 0); }
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
			setState(548);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__11:
				{
				setState(533);
				match(T__11);
				setState(534);
				((AnnotationContext)_localctx).name = qname();
				setState(545);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(535);
					match(T__7);
					setState(536);
					match(Literal);
					setState(541);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__12) {
						{
						{
						setState(537);
						match(T__12);
						setState(538);
						match(Literal);
						}
						}
						setState(543);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(544);
					match(T__8);
					}
				}

				}
				break;
			case Kupdating:
				{
				setState(547);
				((AnnotationContext)_localctx).updating = match(Kupdating);
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
			setState(553);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11 || _la==Kupdating) {
				{
				{
				setState(550);
				annotation();
				}
				}
				setState(555);
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
			setState(556);
			annotations();
			setState(557);
			match(Kvariable);
			setState(558);
			varDeclForStatement();
			setState(563);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(559);
				match(T__12);
				setState(560);
				varDeclForStatement();
				}
				}
				setState(565);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(566);
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
			setState(568);
			((VarDeclForStatementContext)_localctx).var_ref = varRef();
			setState(571);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(569);
				match(Kas);
				setState(570);
				sequenceType();
				}
			}

			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(573);
				match(T__4);
				setState(574);
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
			setState(577);
			match(Kwhile);
			setState(578);
			match(T__7);
			setState(579);
			((WhileStatementContext)_localctx).test_expr = expr();
			setState(580);
			match(T__8);
			setState(581);
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
			setState(587);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(583);
				defaultCollationDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(584);
				orderingModeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(585);
				emptyOrderDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(586);
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
			setState(589);
			match(Kdeclare);
			setState(590);
			match(Knamespace);
			setState(591);
			match(NCName);
			setState(592);
			match(T__2);
			setState(593);
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
			setState(599);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(595);
				functionDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(596);
				varDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(597);
				typeDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(598);
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
			setState(601);
			match(Kdeclare);
			setState(602);
			match(Kdefault);
			setState(603);
			match(Kcollation);
			setState(604);
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
			setState(606);
			match(Kdeclare);
			setState(607);
			match(T__13);
			setState(608);
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
			setState(610);
			match(Kdeclare);
			setState(611);
			match(Kdefault);
			setState(612);
			match(Korder);
			setState(613);
			match(Kempty);
			{
			setState(614);
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
			setState(616);
			match(Kdeclare);
			setState(621);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				{
				{
				setState(617);
				match(T__15);
				setState(618);
				qname();
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(619);
				match(Kdefault);
				setState(620);
				match(T__15);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(629);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26))) != 0)) {
				{
				{
				setState(623);
				dfPropertyName();
				setState(624);
				match(T__2);
				setState(625);
				stringLiteral();
				}
				}
				setState(631);
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
			setState(637);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(634);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(632);
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
				case Kwith:
				case Kposition:
				case Kjson:
				case Kupdating:
				case Kbreak:
				case Kloop:
				case Kcontinue:
				case Kexit:
				case Kreturning:
				case Kwhile:
				case NullLiteral:
					{
					setState(633);
					((QnameContext)_localctx).nskw = keyWords();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(636);
				match(T__16);
				}
				break;
			}
			setState(641);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				{
				setState(639);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
				{
				setState(640);
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
			setState(643);
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
			setState(645);
			match(Kimport);
			setState(646);
			match(T__1);
			setState(650);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Knamespace) {
				{
				setState(647);
				match(Knamespace);
				setState(648);
				((ModuleImportContext)_localctx).prefix = match(NCName);
				setState(649);
				match(T__2);
				}
			}

			setState(652);
			((ModuleImportContext)_localctx).targetNamespace = uriLiteral();
			setState(662);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(653);
				match(Kat);
				setState(654);
				uriLiteral();
				setState(659);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(655);
					match(T__12);
					setState(656);
					uriLiteral();
					}
					}
					setState(661);
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
			setState(664);
			match(Kdeclare);
			setState(665);
			annotations();
			setState(666);
			match(Kvariable);
			setState(667);
			varRef();
			setState(670);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(668);
				match(Kas);
				setState(669);
				sequenceType();
				}
			}

			setState(679);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				{
				{
				setState(672);
				match(T__4);
				setState(673);
				exprSingle();
				}
				}
				break;
			case T__27:
				{
				{
				setState(674);
				((VarDeclContext)_localctx).external = match(T__27);
				setState(677);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(675);
					match(T__4);
					setState(676);
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
			setState(681);
			match(Kdeclare);
			setState(682);
			match(Kcontext);
			setState(683);
			match(Kitem);
			setState(686);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(684);
				match(Kas);
				setState(685);
				sequenceType();
				}
			}

			setState(695);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				{
				{
				setState(688);
				match(T__4);
				setState(689);
				exprSingle();
				}
				}
				break;
			case T__27:
				{
				{
				setState(690);
				((ContextItemDeclContext)_localctx).external = match(T__27);
				setState(693);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(691);
					match(T__4);
					setState(692);
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
		public Token is_external;
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
			setState(697);
			match(Kdeclare);
			setState(698);
			annotations();
			setState(699);
			match(T__28);
			setState(700);
			((FunctionDeclContext)_localctx).fn_name = qname();
			setState(701);
			match(T__7);
			setState(703);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(702);
				paramList();
				}
			}

			setState(705);
			match(T__8);
			setState(708);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(706);
				match(Kas);
				setState(707);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(715);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				{
				setState(710);
				match(T__5);
				{
				setState(711);
				((FunctionDeclContext)_localctx).fn_body = statementsAndOptionalExpr();
				}
				setState(712);
				match(T__6);
				}
				break;
			case T__27:
				{
				setState(714);
				((FunctionDeclContext)_localctx).is_external = match(T__27);
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
			setState(717);
			match(Kdeclare);
			setState(718);
			match(Ktype);
			setState(719);
			((TypeDeclContext)_localctx).type_name = qname();
			setState(720);
			match(Kas);
			setState(722);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(721);
				((TypeDeclContext)_localctx).schema = schemaLanguage();
				}
				break;
			}
			setState(724);
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
			setState(732);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(726);
				match(T__29);
				setState(727);
				match(T__30);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(728);
				match(T__29);
				setState(729);
				match(T__31);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(730);
				match(Kjson);
				setState(731);
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
			setState(734);
			param();
			setState(739);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(735);
				match(T__12);
				setState(736);
				param();
				}
				}
				setState(741);
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
			setState(742);
			match(T__3);
			setState(743);
			qname();
			setState(746);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(744);
				match(Kas);
				setState(745);
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
			setState(748);
			exprSingle();
			setState(753);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(749);
				match(T__12);
				setState(750);
				exprSingle();
				}
				}
				setState(755);
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
			setState(762);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(756);
				exprSimple();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(757);
				flowrExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(758);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(759);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(760);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(761);
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
			setState(772);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(764);
				quantifiedExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(765);
				orExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(766);
				insertExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(767);
				deleteExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(768);
				renameExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(769);
				replaceExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(770);
				transformExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(771);
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
			setState(776);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(774);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(775);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(786);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (Kfor - 59)) | (1L << (Klet - 59)) | (1L << (Kwhere - 59)) | (1L << (Kgroup - 59)) | (1L << (Korder - 59)) | (1L << (Kcount - 59)) | (1L << (Kstable - 59)))) != 0)) {
				{
				setState(784);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(778);
					forClause();
					}
					break;
				case Klet:
					{
					setState(779);
					letClause();
					}
					break;
				case Kwhere:
					{
					setState(780);
					whereClause();
					}
					break;
				case Kgroup:
					{
					setState(781);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(782);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(783);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(788);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(789);
			match(Kreturn);
			setState(790);
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
			setState(792);
			match(Kfor);
			setState(793);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(798);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(794);
				match(T__12);
				setState(795);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(800);
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
			setState(801);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(804);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(802);
				match(Kas);
				setState(803);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(808);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(806);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(807);
				match(Kempty);
				}
			}

			setState(812);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(810);
				match(Kat);
				setState(811);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(814);
			match(Kin);
			setState(815);
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
			setState(817);
			match(Klet);
			setState(818);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(823);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(819);
				match(T__12);
				setState(820);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(825);
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
			setState(826);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(829);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(827);
				match(Kas);
				setState(828);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(831);
			match(T__4);
			setState(832);
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
			setState(834);
			match(Kwhere);
			setState(835);
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
			setState(837);
			match(Kgroup);
			setState(838);
			match(Kby);
			setState(839);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(844);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(840);
				match(T__12);
				setState(841);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(846);
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
			setState(847);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(854);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4 || _la==Kas) {
				{
				setState(850);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(848);
					match(Kas);
					setState(849);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(852);
				((GroupByVarContext)_localctx).decl = match(T__4);
				setState(853);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(858);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(856);
				match(Kcollation);
				setState(857);
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
			setState(865);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(860);
				match(Korder);
				setState(861);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(862);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(863);
				match(Korder);
				setState(864);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(867);
			orderByExpr();
			setState(872);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(868);
				match(T__12);
				setState(869);
				orderByExpr();
				}
				}
				setState(874);
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
			setState(875);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(878);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(876);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(877);
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
			setState(885);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(880);
				match(Kempty);
				setState(883);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(881);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(882);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(889);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(887);
				match(Kcollation);
				setState(888);
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
			setState(891);
			match(Kcount);
			setState(892);
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
			setState(896);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(894);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(895);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(898);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(903);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(899);
				match(T__12);
				setState(900);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(905);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(906);
			match(Ksatisfies);
			setState(907);
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
			setState(909);
			varRef();
			setState(912);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(910);
				match(Kas);
				setState(911);
				sequenceType();
				}
			}

			setState(914);
			match(Kin);
			setState(915);
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
			setState(917);
			match(Kswitch);
			setState(918);
			match(T__7);
			setState(919);
			((SwitchExprContext)_localctx).cond = expr();
			setState(920);
			match(T__8);
			setState(922); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(921);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(924); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(926);
			match(Kdefault);
			setState(927);
			match(Kreturn);
			setState(928);
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
			setState(932); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(930);
				match(Kcase);
				setState(931);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(934); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(936);
			match(Kreturn);
			setState(937);
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
			setState(939);
			match(Ktypeswitch);
			setState(940);
			match(T__7);
			setState(941);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(942);
			match(T__8);
			setState(944); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(943);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(946); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(948);
			match(Kdefault);
			setState(950);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(949);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(952);
			match(Kreturn);
			setState(953);
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
			setState(955);
			match(Kcase);
			setState(959);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(956);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(957);
				match(Kas);
				}
			}

			setState(961);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(966);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(962);
				match(T__10);
				setState(963);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(968);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(969);
			match(Kreturn);
			setState(970);
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
			setState(972);
			match(Kif);
			setState(973);
			match(T__7);
			setState(974);
			((IfExprContext)_localctx).test_condition = expr();
			setState(975);
			match(T__8);
			setState(976);
			match(Kthen);
			setState(977);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(978);
			match(Kelse);
			setState(979);
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
			setState(981);
			match(Ktry);
			setState(982);
			match(T__5);
			setState(983);
			((TryCatchExprContext)_localctx).try_expression = expr();
			setState(984);
			match(T__6);
			setState(986); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(985);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(988); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
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
			setState(990);
			match(Kcatch);
			setState(993);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
				{
				setState(991);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				{
				setState(992);
				((CatchClauseContext)_localctx).qname = qname();
				((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1002);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(995);
				match(T__10);
				setState(998);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__9:
					{
					setState(996);
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
				case Kwith:
				case Kposition:
				case Kjson:
				case Kupdating:
				case Kbreak:
				case Kloop:
				case Kcontinue:
				case Kexit:
				case Kreturning:
				case Kwhile:
				case NullLiteral:
				case NCName:
					{
					setState(997);
					((CatchClauseContext)_localctx).qname = qname();
					((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1004);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1005);
			match(T__5);
			setState(1006);
			((CatchClauseContext)_localctx).catch_expression = expr();
			setState(1007);
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
			setState(1009);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(1014);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1010);
					match(Kor);
					setState(1011);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(1016);
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
			setState(1017);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(1022);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,87,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1018);
					match(Kand);
					setState(1019);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(1024);
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
			setState(1026);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(1025);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(1028);
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
		public Token _tset1837;
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
			setState(1030);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(1033);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42))) != 0)) {
				{
				setState(1031);
				((ComparisonExprContext)_localctx)._tset1837 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1837 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1837);
				setState(1032);
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
			setState(1035);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(1040);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__43) {
				{
				{
				setState(1036);
				match(T__43);
				setState(1037);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(1042);
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
			setState(1043);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(1046);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				{
				setState(1044);
				match(Kto);
				setState(1045);
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
		public Token _tset1946;
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
			setState(1048);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(1053);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,92,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1049);
					((AdditiveExprContext)_localctx)._tset1946 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__44 || _la==T__45) ) {
						((AdditiveExprContext)_localctx)._tset1946 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1946);
					setState(1050);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(1055);
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

	public static class MultiplicativeExprContext extends ParserRuleContext {
		public InstanceOfExprContext main_expr;
		public Token s10;
		public List<Token> op = new ArrayList<Token>();
		public Token s47;
		public Token s48;
		public Token s49;
		public Token _tset1974;
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
			setState(1056);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(1061);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__46) | (1L << T__47) | (1L << T__48))) != 0)) {
				{
				{
				setState(1057);
				((MultiplicativeExprContext)_localctx)._tset1974 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__46) | (1L << T__47) | (1L << T__48))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1974 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1974);
				setState(1058);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(1063);
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
			setState(1064);
			((InstanceOfExprContext)_localctx).main_expr = isStaticallyExpr();
			setState(1068);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				{
				setState(1065);
				match(Kinstance);
				setState(1066);
				match(Kof);
				setState(1067);
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
			setState(1070);
			((IsStaticallyExprContext)_localctx).main_expr = treatExpr();
			setState(1074);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				{
				setState(1071);
				match(Kis);
				setState(1072);
				match(Kstatically);
				setState(1073);
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
			setState(1076);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(1080);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				{
				setState(1077);
				match(Ktreat);
				setState(1078);
				match(Kas);
				setState(1079);
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
			setState(1082);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(1086);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				{
				setState(1083);
				match(Kcastable);
				setState(1084);
				match(Kas);
				setState(1085);
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
			setState(1088);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(1092);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
			case 1:
				{
				setState(1089);
				match(Kcast);
				setState(1090);
				match(Kas);
				setState(1091);
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
			setState(1094);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(1103);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,99,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(1095);
					match(T__2);
					setState(1096);
					match(T__41);
					}
					setState(1098);
					((ArrowExprContext)_localctx).arrowFunctionSpecifier = arrowFunctionSpecifier();
					((ArrowExprContext)_localctx).function.add(((ArrowExprContext)_localctx).arrowFunctionSpecifier);
					setState(1099);
					((ArrowExprContext)_localctx).argumentList = argumentList();
					((ArrowExprContext)_localctx).arguments.add(((ArrowExprContext)_localctx).argumentList);
					}
					} 
				}
				setState(1105);
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
			setState(1109);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
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
				setState(1106);
				qname();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(1107);
				varRef();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(1108);
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
		public Token _tset2153;
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
			setState(1114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44 || _la==T__45) {
				{
				{
				setState(1111);
				((UnaryExprContext)_localctx)._tset2153 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__44 || _la==T__45) ) {
					((UnaryExprContext)_localctx)._tset2153 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset2153);
				}
				}
				setState(1116);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1117);
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
			setState(1122);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,102,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1119);
				((ValueExprContext)_localctx).simpleMap_expr = simpleMapExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1120);
				((ValueExprContext)_localctx).validate_expr = validateExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1121);
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
			setState(1124);
			match(Kvalidate);
			setState(1125);
			match(Ktype);
			setState(1126);
			sequenceType();
			setState(1127);
			match(T__5);
			setState(1128);
			expr();
			setState(1129);
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
			setState(1131);
			match(Kannotate);
			setState(1132);
			match(Ktype);
			setState(1133);
			sequenceType();
			setState(1134);
			match(T__5);
			setState(1135);
			expr();
			setState(1136);
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
		public PathExprContext main_expr;
		public PathExprContext pathExpr;
		public List<PathExprContext> map_expr = new ArrayList<PathExprContext>();
		public List<PathExprContext> pathExpr() {
			return getRuleContexts(PathExprContext.class);
		}
		public PathExprContext pathExpr(int i) {
			return getRuleContext(PathExprContext.class,i);
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
			setState(1138);
			((SimpleMapExprContext)_localctx).main_expr = pathExpr();
			setState(1143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__49) {
				{
				{
				setState(1139);
				match(T__49);
				setState(1140);
				((SimpleMapExprContext)_localctx).pathExpr = pathExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).pathExpr);
				}
				}
				setState(1145);
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
			setState(1146);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(1154);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,105,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1152);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
					case 1:
						{
						setState(1147);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(1148);
						predicate();
						}
						break;
					case 3:
						{
						setState(1149);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(1150);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(1151);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(1156);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,105,_ctx);
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
			setState(1157);
			match(T__50);
			setState(1158);
			match(T__50);
			setState(1159);
			expr();
			setState(1160);
			match(T__51);
			setState(1161);
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
			setState(1163);
			match(T__50);
			setState(1164);
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
			setState(1166);
			match(T__50);
			setState(1167);
			expr();
			setState(1168);
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
			setState(1170);
			match(T__52);
			setState(1177);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
				{
				setState(1171);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(1172);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(1173);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__7:
				{
				setState(1174);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__3:
				{
				setState(1175);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__53:
				{
				setState(1176);
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
			setState(1194);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1179);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1180);
				match(Ktrue);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1181);
				match(Kfalse);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1182);
				match(Literal);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1183);
				stringLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1184);
				varRef();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1185);
				parenthesizedExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1186);
				contextItemExpr();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1187);
				objectConstructor();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1188);
				functionCall();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1189);
				orderedExpr();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1190);
				unorderedExpr();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1191);
				arrayConstructor();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1192);
				functionItemExpr();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1193);
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
			setState(1196);
			match(T__5);
			setState(1197);
			statementsAndExpr();
			setState(1198);
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
			setState(1200);
			match(T__3);
			setState(1201);
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
			setState(1203);
			match(T__7);
			setState(1205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__55) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kjson - 64)) | (1L << (Kupdating - 64)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (Kslash - 130)) | (1L << (Kdslash - 130)) | (1L << (Kat_symbol - 130)) | (1L << (Kchild - 130)) | (1L << (Kdescendant - 130)) | (1L << (Kattribute - 130)) | (1L << (Kself - 130)) | (1L << (Kdescendant_or_self - 130)) | (1L << (Kfollowing_sibling - 130)) | (1L << (Kfollowing - 130)) | (1L << (Kparent - 130)) | (1L << (Kancestor - 130)) | (1L << (Kpreceding_sibling - 130)) | (1L << (Kpreceding - 130)) | (1L << (Kancestor_or_self - 130)) | (1L << (Kbreak - 130)) | (1L << (Kloop - 130)) | (1L << (Kcontinue - 130)) | (1L << (Kexit - 130)) | (1L << (Kreturning - 130)) | (1L << (Kwhile - 130)) | (1L << (STRING - 130)) | (1L << (NullLiteral - 130)) | (1L << (Literal - 130)) | (1L << (NCName - 130)))) != 0)) {
				{
				setState(1204);
				expr();
				}
			}

			setState(1207);
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
			setState(1209);
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
			setState(1211);
			match(T__14);
			setState(1212);
			match(T__5);
			setState(1213);
			expr();
			setState(1214);
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
			setState(1216);
			match(Kunordered);
			setState(1217);
			match(T__5);
			setState(1218);
			expr();
			setState(1219);
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
			setState(1221);
			((FunctionCallContext)_localctx).fn_name = qname();
			setState(1222);
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
			setState(1224);
			match(T__7);
			setState(1231);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__55) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kjson - 64)) | (1L << (Kupdating - 64)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (Kslash - 130)) | (1L << (Kdslash - 130)) | (1L << (Kat_symbol - 130)) | (1L << (Kchild - 130)) | (1L << (Kdescendant - 130)) | (1L << (Kattribute - 130)) | (1L << (Kself - 130)) | (1L << (Kdescendant_or_self - 130)) | (1L << (Kfollowing_sibling - 130)) | (1L << (Kfollowing - 130)) | (1L << (Kparent - 130)) | (1L << (Kancestor - 130)) | (1L << (Kpreceding_sibling - 130)) | (1L << (Kpreceding - 130)) | (1L << (Kancestor_or_self - 130)) | (1L << (Kbreak - 130)) | (1L << (Kloop - 130)) | (1L << (Kcontinue - 130)) | (1L << (Kexit - 130)) | (1L << (Kreturning - 130)) | (1L << (Kwhile - 130)) | (1L << (STRING - 130)) | (1L << (ArgumentPlaceholder - 130)) | (1L << (NullLiteral - 130)) | (1L << (Literal - 130)) | (1L << (NCName - 130)))) != 0)) {
				{
				{
				setState(1225);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(1227);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1226);
					match(T__12);
					}
				}

				}
				}
				setState(1233);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1234);
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
			setState(1238);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
			case Kslash:
			case Kdslash:
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
				setState(1236);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(1237);
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
			setState(1242);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1240);
				namedFunctionRef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1241);
				inlineFunctionExpr();
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
			setState(1244);
			((NamedFunctionRefContext)_localctx).fn_name = qname();
			setState(1245);
			match(T__54);
			setState(1246);
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
			setState(1248);
			annotations();
			setState(1249);
			match(T__28);
			setState(1250);
			match(T__7);
			setState(1252);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(1251);
				paramList();
				}
			}

			setState(1254);
			match(T__8);
			setState(1257);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(1255);
				match(Kas);
				setState(1256);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(1259);
			match(T__5);
			{
			setState(1260);
			((InlineFunctionExprContext)_localctx).fn_body = statementsAndOptionalExpr();
			}
			setState(1261);
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
			setState(1286);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1263);
				match(Kinsert);
				setState(1264);
				match(Kjson);
				setState(1265);
				((InsertExprContext)_localctx).to_insert_expr = exprSingle();
				setState(1266);
				match(Kinto);
				setState(1267);
				((InsertExprContext)_localctx).main_expr = exprSingle();
				setState(1271);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,115,_ctx) ) {
				case 1:
					{
					setState(1268);
					match(Kat);
					setState(1269);
					match(Kposition);
					setState(1270);
					((InsertExprContext)_localctx).pos_expr = exprSingle();
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1273);
				match(Kinsert);
				setState(1274);
				match(Kjson);
				setState(1275);
				pairConstructor();
				setState(1280);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(1276);
					match(T__12);
					setState(1277);
					pairConstructor();
					}
					}
					setState(1282);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1283);
				match(Kinto);
				setState(1284);
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
			setState(1288);
			match(Kdelete);
			setState(1289);
			match(Kjson);
			setState(1290);
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
			setState(1292);
			match(Krename);
			setState(1293);
			match(Kjson);
			setState(1294);
			updateLocator();
			setState(1295);
			match(Kas);
			setState(1296);
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
		public TerminalNode Kvalue() { return getToken(JsoniqParser.Kvalue, 0); }
		public TerminalNode Kof() { return getToken(JsoniqParser.Kof, 0); }
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
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
			setState(1298);
			match(Kreplace);
			setState(1299);
			match(Kvalue);
			setState(1300);
			match(Kof);
			setState(1301);
			match(Kjson);
			setState(1302);
			updateLocator();
			setState(1303);
			match(Kwith);
			setState(1304);
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
			setState(1306);
			match(Kcopy);
			setState(1307);
			copyDecl();
			setState(1312);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(1308);
				match(T__12);
				setState(1309);
				copyDecl();
				}
				}
				setState(1314);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1315);
			match(Kmodify);
			setState(1316);
			((TransformExprContext)_localctx).mod_expr = exprSingle();
			setState(1317);
			match(Kreturn);
			setState(1318);
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
			setState(1320);
			match(Kappend);
			setState(1321);
			match(Kjson);
			setState(1322);
			((AppendExprContext)_localctx).to_append_expr = exprSingle();
			setState(1323);
			match(Kinto);
			setState(1324);
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
			setState(1326);
			((UpdateLocatorContext)_localctx).main_expr = primaryExpr();
			setState(1329); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(1329);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__50:
						{
						setState(1327);
						arrayLookup();
						}
						break;
					case T__52:
						{
						setState(1328);
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
				setState(1331); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,120,_ctx);
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
			setState(1333);
			((CopyDeclContext)_localctx).var_ref = varRef();
			setState(1334);
			match(T__4);
			setState(1335);
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
		enterRule(_localctx, 230, RULE_pathExpr);
		try {
			setState(1344);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kslash:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1337);
				match(Kslash);
				setState(1339);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
				case 1:
					{
					setState(1338);
					((PathExprContext)_localctx).singleslash = relativePathExpr();
					}
					break;
				}
				}
				}
				break;
			case Kdslash:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1341);
				match(Kdslash);
				setState(1342);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
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
				enterOuterAlt(_localctx, 3);
				{
				setState(1343);
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
		public Token Kslash;
		public List<Token> sep = new ArrayList<Token>();
		public Token Kdslash;
		public Token _tset2817;
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
		enterRule(_localctx, 232, RULE_relativePathExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1346);
			stepExpr();
			setState(1351);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,123,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1347);
					((RelativePathExprContext)_localctx)._tset2817 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==Kslash || _la==Kdslash) ) {
						((RelativePathExprContext)_localctx)._tset2817 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((RelativePathExprContext)_localctx).sep.add(((RelativePathExprContext)_localctx)._tset2817);
					setState(1348);
					stepExpr();
					}
					} 
				}
				setState(1353);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,123,_ctx);
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
		enterRule(_localctx, 234, RULE_stepExpr);
		try {
			setState(1356);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
			case T__5:
			case T__7:
			case T__11:
			case T__14:
			case T__28:
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
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
				setState(1354);
				postFixExpr();
				}
				break;
			case T__55:
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
				enterOuterAlt(_localctx, 2);
				{
				setState(1355);
				axisStep();
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
		enterRule(_localctx, 236, RULE_axisStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1360);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__55:
			case Kparent:
			case Kancestor:
			case Kpreceding_sibling:
			case Kpreceding:
			case Kancestor_or_self:
				{
				setState(1358);
				reverseStep();
				}
				break;
			case Kat_symbol:
			case Kchild:
			case Kdescendant:
			case Kattribute:
			case Kself:
			case Kdescendant_or_self:
			case Kfollowing_sibling:
			case Kfollowing:
				{
				setState(1359);
				forwardStep();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1362);
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
		enterRule(_localctx, 238, RULE_forwardStep);
		try {
			setState(1368);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kchild:
			case Kdescendant:
			case Kattribute:
			case Kself:
			case Kdescendant_or_self:
			case Kfollowing_sibling:
			case Kfollowing:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1364);
				forwardAxis();
				setState(1365);
				nodeTest();
				}
				}
				break;
			case Kat_symbol:
				enterOuterAlt(_localctx, 2);
				{
				setState(1367);
				abbrevForwardStep();
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
		enterRule(_localctx, 240, RULE_forwardAxis);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1370);
			_la = _input.LA(1);
			if ( !(((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & ((1L << (Kchild - 133)) | (1L << (Kdescendant - 133)) | (1L << (Kattribute - 133)) | (1L << (Kself - 133)) | (1L << (Kdescendant_or_self - 133)) | (1L << (Kfollowing_sibling - 133)) | (1L << (Kfollowing - 133)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1371);
			match(T__16);
			setState(1372);
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
		public TerminalNode Kat_symbol() { return getToken(JsoniqParser.Kat_symbol, 0); }
		public NodeTestContext nodeTest() {
			return getRuleContext(NodeTestContext.class,0);
		}
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
		enterRule(_localctx, 242, RULE_abbrevForwardStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1374);
			match(Kat_symbol);
			setState(1375);
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
		enterRule(_localctx, 244, RULE_reverseStep);
		try {
			setState(1381);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kparent:
			case Kancestor:
			case Kpreceding_sibling:
			case Kpreceding:
			case Kancestor_or_self:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1377);
				reverseAxis();
				setState(1378);
				nodeTest();
				}
				}
				break;
			case T__55:
				enterOuterAlt(_localctx, 2);
				{
				setState(1380);
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
		enterRule(_localctx, 246, RULE_reverseAxis);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1383);
			_la = _input.LA(1);
			if ( !(((((_la - 140)) & ~0x3f) == 0 && ((1L << (_la - 140)) & ((1L << (Kparent - 140)) | (1L << (Kancestor - 140)) | (1L << (Kpreceding_sibling - 140)) | (1L << (Kpreceding - 140)) | (1L << (Kancestor_or_self - 140)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1384);
			match(T__16);
			setState(1385);
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
		enterRule(_localctx, 248, RULE_abbrevReverseStep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1387);
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
		enterRule(_localctx, 250, RULE_nodeTest);
		try {
			setState(1391);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__9:
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
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
				setState(1389);
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
			case Kcomment:
				enterOuterAlt(_localctx, 2);
				{
				setState(1390);
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
		enterRule(_localctx, 252, RULE_nameTest);
		try {
			setState(1395);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1393);
				qname();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1394);
				wildcard();
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
		enterRule(_localctx, 254, RULE_wildcard);
		try {
			setState(1400);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				_localctx = new AllNamesContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(1397);
				match(T__9);
				}
				break;
			case 2:
				_localctx = new AllWithNSContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(1398);
				nCNameWithLocalWildcard();
				}
				break;
			case 3:
				_localctx = new AllWithLocalContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(1399);
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
		enterRule(_localctx, 256, RULE_nCNameWithLocalWildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1402);
			match(NCName);
			setState(1403);
			match(T__16);
			setState(1404);
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
		enterRule(_localctx, 258, RULE_nCNameWithPrefixWildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1406);
			match(T__9);
			setState(1407);
			match(T__16);
			setState(1408);
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
		enterRule(_localctx, 260, RULE_predicateList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1413);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,131,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1410);
					predicate();
					}
					} 
				}
				setState(1415);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,131,_ctx);
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
		enterRule(_localctx, 262, RULE_kindTest);
		try {
			setState(1427);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kdocument_node:
				enterOuterAlt(_localctx, 1);
				{
				setState(1416);
				documentTest();
				}
				break;
			case Kelement:
				enterOuterAlt(_localctx, 2);
				{
				setState(1417);
				elementTest();
				}
				break;
			case Kattribute:
				enterOuterAlt(_localctx, 3);
				{
				setState(1418);
				attributeTest();
				}
				break;
			case Kschema_element:
				enterOuterAlt(_localctx, 4);
				{
				setState(1419);
				schemaElementTest();
				}
				break;
			case Kschema_attribute:
				enterOuterAlt(_localctx, 5);
				{
				setState(1420);
				schemaAttributeTest();
				}
				break;
			case Kpi:
				enterOuterAlt(_localctx, 6);
				{
				setState(1421);
				piTest();
				}
				break;
			case Kcomment:
				enterOuterAlt(_localctx, 7);
				{
				setState(1422);
				commentTest();
				}
				break;
			case Ktext:
				enterOuterAlt(_localctx, 8);
				{
				setState(1423);
				textTest();
				}
				break;
			case Knamespace_node:
				enterOuterAlt(_localctx, 9);
				{
				setState(1424);
				namespaceNodeTest();
				}
				break;
			case Kbinary:
				enterOuterAlt(_localctx, 10);
				{
				setState(1425);
				binaryNodeTest();
				}
				break;
			case Knode:
				enterOuterAlt(_localctx, 11);
				{
				setState(1426);
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
		enterRule(_localctx, 264, RULE_anyKindTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1429);
			match(Knode);
			setState(1430);
			match(T__7);
			setState(1431);
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
		enterRule(_localctx, 266, RULE_binaryNodeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1433);
			match(Kbinary);
			setState(1434);
			match(T__7);
			setState(1435);
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
		enterRule(_localctx, 268, RULE_documentTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1437);
			match(Kdocument_node);
			setState(1438);
			match(T__7);
			setState(1441);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kelement:
				{
				setState(1439);
				elementTest();
				}
				break;
			case Kschema_element:
				{
				setState(1440);
				schemaElementTest();
				}
				break;
			case T__8:
				break;
			default:
				break;
			}
			setState(1443);
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
		enterRule(_localctx, 270, RULE_textTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1445);
			match(Ktext);
			setState(1446);
			match(T__7);
			setState(1447);
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
		enterRule(_localctx, 272, RULE_commentTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1449);
			match(Kcomment);
			setState(1450);
			match(T__7);
			setState(1451);
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
		enterRule(_localctx, 274, RULE_namespaceNodeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1453);
			match(Knamespace_node);
			setState(1454);
			match(T__7);
			setState(1455);
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
		enterRule(_localctx, 276, RULE_piTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1457);
			match(Kpi);
			setState(1458);
			match(T__7);
			setState(1461);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				{
				setState(1459);
				match(NCName);
				}
				break;
			case STRING:
				{
				setState(1460);
				stringLiteral();
				}
				break;
			case T__8:
				break;
			default:
				break;
			}
			setState(1463);
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
		enterRule(_localctx, 278, RULE_attributeTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1465);
			match(Kattribute);
			setState(1466);
			match(T__7);
			setState(1472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kjson - 64)) | (1L << (Kupdating - 64)))) != 0) || ((((_la - 160)) & ~0x3f) == 0 && ((1L << (_la - 160)) & ((1L << (Kbreak - 160)) | (1L << (Kloop - 160)) | (1L << (Kcontinue - 160)) | (1L << (Kexit - 160)) | (1L << (Kreturning - 160)) | (1L << (Kwhile - 160)) | (1L << (NullLiteral - 160)) | (1L << (NCName - 160)))) != 0)) {
				{
				setState(1467);
				attributeNameOrWildcard();
				setState(1470);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1468);
					match(T__12);
					setState(1469);
					((AttributeTestContext)_localctx).type = typeName();
					}
				}

				}
			}

			setState(1474);
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
		enterRule(_localctx, 280, RULE_attributeNameOrWildcard);
		try {
			setState(1478);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
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
				setState(1476);
				attributeName();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(1477);
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
		enterRule(_localctx, 282, RULE_schemaAttributeTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1480);
			match(Kschema_attribute);
			setState(1481);
			match(T__7);
			setState(1482);
			attributeDeclaration();
			setState(1483);
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
		enterRule(_localctx, 284, RULE_attributeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1485);
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
		enterRule(_localctx, 286, RULE_elementTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1487);
			match(Kelement);
			setState(1488);
			match(T__7);
			setState(1497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kjson - 64)) | (1L << (Kupdating - 64)))) != 0) || ((((_la - 160)) & ~0x3f) == 0 && ((1L << (_la - 160)) & ((1L << (Kbreak - 160)) | (1L << (Kloop - 160)) | (1L << (Kcontinue - 160)) | (1L << (Kexit - 160)) | (1L << (Kreturning - 160)) | (1L << (Kwhile - 160)) | (1L << (NullLiteral - 160)) | (1L << (NCName - 160)))) != 0)) {
				{
				setState(1489);
				elementNameOrWildcard();
				setState(1495);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__12) {
					{
					setState(1490);
					match(T__12);
					setState(1491);
					((ElementTestContext)_localctx).type = typeName();
					setState(1493);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ArgumentPlaceholder) {
						{
						setState(1492);
						((ElementTestContext)_localctx).optional = match(ArgumentPlaceholder);
						}
					}

					}
				}

				}
			}

			setState(1499);
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
		enterRule(_localctx, 288, RULE_elementNameOrWildcard);
		try {
			setState(1503);
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
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
				setState(1501);
				elementName();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(1502);
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
		enterRule(_localctx, 290, RULE_schemaElementTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1505);
			match(Kschema_element);
			setState(1506);
			match(T__7);
			setState(1507);
			elementDeclaration();
			setState(1508);
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
		enterRule(_localctx, 292, RULE_elementDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1510);
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
		enterRule(_localctx, 294, RULE_attributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1512);
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
		enterRule(_localctx, 296, RULE_elementName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1514);
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
		enterRule(_localctx, 298, RULE_simpleTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1516);
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
		enterRule(_localctx, 300, RULE_typeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1518);
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

	public static class SequenceTypeContext extends ParserRuleContext {
		public ItemTypeContext item;
		public Token s167;
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
		enterRule(_localctx, 302, RULE_sequenceType);
		try {
			setState(1528);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(1520);
				match(T__7);
				setState(1521);
				match(T__8);
				}
				break;
			case T__28:
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
			case Kwith:
			case Kposition:
			case Kjson:
			case Kupdating:
			case Kbreak:
			case Kloop:
			case Kcontinue:
			case Kexit:
			case Kreturning:
			case Kwhile:
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 2);
				{
				setState(1522);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(1526);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
				case 1:
					{
					setState(1523);
					((SequenceTypeContext)_localctx).s167 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s167);
					}
					break;
				case 2:
					{
					setState(1524);
					((SequenceTypeContext)_localctx).s10 = match(T__9);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s10);
					}
					break;
				case 3:
					{
					setState(1525);
					((SequenceTypeContext)_localctx).s45 = match(T__44);
					((SequenceTypeContext)_localctx).plus.add(((SequenceTypeContext)_localctx).s45);
					}
					break;
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
		enterRule(_localctx, 304, RULE_objectConstructor);
		int _la;
		try {
			setState(1546);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(1530);
				match(T__5);
				setState(1539);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__55) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kjson - 64)) | (1L << (Kupdating - 64)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (Kslash - 130)) | (1L << (Kdslash - 130)) | (1L << (Kat_symbol - 130)) | (1L << (Kchild - 130)) | (1L << (Kdescendant - 130)) | (1L << (Kattribute - 130)) | (1L << (Kself - 130)) | (1L << (Kdescendant_or_self - 130)) | (1L << (Kfollowing_sibling - 130)) | (1L << (Kfollowing - 130)) | (1L << (Kparent - 130)) | (1L << (Kancestor - 130)) | (1L << (Kpreceding_sibling - 130)) | (1L << (Kpreceding - 130)) | (1L << (Kancestor_or_self - 130)) | (1L << (Kbreak - 130)) | (1L << (Kloop - 130)) | (1L << (Kcontinue - 130)) | (1L << (Kexit - 130)) | (1L << (Kreturning - 130)) | (1L << (Kwhile - 130)) | (1L << (STRING - 130)) | (1L << (NullLiteral - 130)) | (1L << (Literal - 130)) | (1L << (NCName - 130)))) != 0)) {
					{
					setState(1531);
					pairConstructor();
					setState(1536);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__12) {
						{
						{
						setState(1532);
						match(T__12);
						setState(1533);
						pairConstructor();
						}
						}
						setState(1538);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1541);
				match(T__6);
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 2);
				{
				setState(1542);
				((ObjectConstructorContext)_localctx).s57 = match(T__56);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s57);
				setState(1543);
				expr();
				setState(1544);
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

	public static class ItemTypeContext extends ParserRuleContext {
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TerminalNode NullLiteral() { return getToken(JsoniqParser.NullLiteral, 0); }
		public FunctionTestContext functionTest() {
			return getRuleContext(FunctionTestContext.class,0);
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
		enterRule(_localctx, 306, RULE_itemType);
		try {
			setState(1551);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,147,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1548);
				qname();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1549);
				match(NullLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1550);
				functionTest();
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
		enterRule(_localctx, 308, RULE_functionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1555);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				{
				setState(1553);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(1554);
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
		enterRule(_localctx, 310, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1557);
			match(T__28);
			setState(1558);
			match(T__7);
			setState(1559);
			match(T__9);
			setState(1560);
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
		enterRule(_localctx, 312, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1562);
			match(T__28);
			setState(1563);
			match(T__7);
			setState(1572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__28) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kjson - 64)) | (1L << (Kupdating - 64)))) != 0) || ((((_la - 160)) & ~0x3f) == 0 && ((1L << (_la - 160)) & ((1L << (Kbreak - 160)) | (1L << (Kloop - 160)) | (1L << (Kcontinue - 160)) | (1L << (Kexit - 160)) | (1L << (Kreturning - 160)) | (1L << (Kwhile - 160)) | (1L << (NullLiteral - 160)) | (1L << (NCName - 160)))) != 0)) {
				{
				setState(1564);
				((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
				((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
				setState(1569);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(1565);
					match(T__12);
					setState(1566);
					((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
					((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
					}
					}
					setState(1571);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1574);
			match(T__8);
			setState(1575);
			match(Kas);
			setState(1576);
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
		public Token s167;
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
		enterRule(_localctx, 314, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1578);
			((SingleTypeContext)_localctx).item = itemType();
			setState(1580);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,151,_ctx) ) {
			case 1:
				{
				setState(1579);
				((SingleTypeContext)_localctx).s167 = match(ArgumentPlaceholder);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).s167);
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
		enterRule(_localctx, 316, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1584);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				{
				setState(1582);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(1583);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(1586);
			_la = _input.LA(1);
			if ( !(_la==T__16 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1587);
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
		enterRule(_localctx, 318, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1589);
			match(T__50);
			setState(1591);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__11) | (1L << T__14) | (1L << T__28) | (1L << T__44) | (1L << T__45) | (1L << T__50) | (1L << T__53) | (1L << T__55) | (1L << T__56) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup) | (1L << Kby))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kjson - 64)) | (1L << (Kupdating - 64)))) != 0) || ((((_la - 130)) & ~0x3f) == 0 && ((1L << (_la - 130)) & ((1L << (Kslash - 130)) | (1L << (Kdslash - 130)) | (1L << (Kat_symbol - 130)) | (1L << (Kchild - 130)) | (1L << (Kdescendant - 130)) | (1L << (Kattribute - 130)) | (1L << (Kself - 130)) | (1L << (Kdescendant_or_self - 130)) | (1L << (Kfollowing_sibling - 130)) | (1L << (Kfollowing - 130)) | (1L << (Kparent - 130)) | (1L << (Kancestor - 130)) | (1L << (Kpreceding_sibling - 130)) | (1L << (Kpreceding - 130)) | (1L << (Kancestor_or_self - 130)) | (1L << (Kbreak - 130)) | (1L << (Kloop - 130)) | (1L << (Kcontinue - 130)) | (1L << (Kexit - 130)) | (1L << (Kreturning - 130)) | (1L << (Kwhile - 130)) | (1L << (STRING - 130)) | (1L << (NullLiteral - 130)) | (1L << (Literal - 130)) | (1L << (NCName - 130)))) != 0)) {
				{
				setState(1590);
				expr();
				}
			}

			setState(1593);
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
		enterRule(_localctx, 320, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1595);
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
		enterRule(_localctx, 322, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1597);
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
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public TerminalNode Kupdating() { return getToken(JsoniqParser.Kupdating, 0); }
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
		enterRule(_localctx, 324, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1599);
			_la = _input.LA(1);
			if ( !(((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (Kfor - 59)) | (1L << (Klet - 59)) | (1L << (Kwhere - 59)) | (1L << (Kgroup - 59)) | (1L << (Kby - 59)) | (1L << (Korder - 59)) | (1L << (Kreturn - 59)) | (1L << (Kif - 59)) | (1L << (Kin - 59)) | (1L << (Kas - 59)) | (1L << (Kat - 59)) | (1L << (Kallowing - 59)) | (1L << (Kempty - 59)) | (1L << (Kcount - 59)) | (1L << (Kstable - 59)) | (1L << (Kascending - 59)) | (1L << (Kdescending - 59)) | (1L << (Ksome - 59)) | (1L << (Kevery - 59)) | (1L << (Ksatisfies - 59)) | (1L << (Kcollation - 59)) | (1L << (Kgreatest - 59)) | (1L << (Kleast - 59)) | (1L << (Kswitch - 59)) | (1L << (Kcase - 59)) | (1L << (Ktry - 59)) | (1L << (Kcatch - 59)) | (1L << (Kdefault - 59)) | (1L << (Kthen - 59)) | (1L << (Kelse - 59)) | (1L << (Ktypeswitch - 59)) | (1L << (Kor - 59)) | (1L << (Kand - 59)) | (1L << (Knot - 59)) | (1L << (Kto - 59)) | (1L << (Kinstance - 59)) | (1L << (Kof - 59)) | (1L << (Kstatically - 59)) | (1L << (Kis - 59)) | (1L << (Ktreat - 59)) | (1L << (Kcast - 59)) | (1L << (Kcastable - 59)) | (1L << (Kversion - 59)) | (1L << (Kjsoniq - 59)) | (1L << (Kunordered - 59)) | (1L << (Ktrue - 59)) | (1L << (Kfalse - 59)) | (1L << (Ktype - 59)) | (1L << (Kvalidate - 59)) | (1L << (Kannotate - 59)) | (1L << (Kdeclare - 59)) | (1L << (Kcontext - 59)) | (1L << (Kitem - 59)) | (1L << (Kvariable - 59)) | (1L << (Kinsert - 59)) | (1L << (Kdelete - 59)) | (1L << (Krename - 59)) | (1L << (Kreplace - 59)) | (1L << (Kcopy - 59)) | (1L << (Kmodify - 59)) | (1L << (Kappend - 59)) | (1L << (Kinto - 59)) | (1L << (Kvalue - 59)) | (1L << (Kwith - 59)))) != 0) || ((((_la - 123)) & ~0x3f) == 0 && ((1L << (_la - 123)) & ((1L << (Kposition - 123)) | (1L << (Kjson - 123)) | (1L << (Kupdating - 123)) | (1L << (Kbreak - 123)) | (1L << (Kloop - 123)) | (1L << (Kcontinue - 123)) | (1L << (Kexit - 123)) | (1L << (Kreturning - 123)) | (1L << (Kwhile - 123)) | (1L << (NullLiteral - 123)))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00b3\u0644\4\2\t"+
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
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3\u0151\n\3\3\3\3\3\5\3\u0155\n\3\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\5\6\u0165\n\6\3"+
		"\6\3\6\7\6\u0169\n\6\f\6\16\6\u016c\13\6\3\6\3\6\3\6\7\6\u0171\n\6\f\6"+
		"\16\6\u0174\13\6\3\7\3\7\3\b\7\b\u0179\n\b\f\b\16\b\u017c\13\b\3\t\3\t"+
		"\3\t\3\n\3\n\5\n\u0183\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\5\13\u0192\n\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\5\22\u01b0\n\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\7\22\u01b8\n\22\f\22\16\22\u01bb\13\22\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\6\24\u01ce"+
		"\n\24\r\24\16\24\u01cf\3\24\3\24\3\24\3\24\3\25\3\25\6\25\u01d8\n\25\r"+
		"\25\16\25\u01d9\3\25\3\25\3\25\3\26\3\26\3\26\6\26\u01e2\n\26\r\26\16"+
		"\26\u01e3\3\27\3\27\3\27\5\27\u01e9\n\27\3\27\3\27\3\27\5\27\u01ee\n\27"+
		"\7\27\u01f0\n\27\f\27\16\27\u01f3\13\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\6\30\u01fc\n\30\r\30\16\30\u01fd\3\30\3\30\5\30\u0202\n\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\5\31\u020b\n\31\3\31\3\31\3\31\7\31\u0210"+
		"\n\31\f\31\16\31\u0213\13\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3"+
		"\32\7\32\u021e\n\32\f\32\16\32\u0221\13\32\3\32\5\32\u0224\n\32\3\32\5"+
		"\32\u0227\n\32\3\33\7\33\u022a\n\33\f\33\16\33\u022d\13\33\3\34\3\34\3"+
		"\34\3\34\3\34\7\34\u0234\n\34\f\34\16\34\u0237\13\34\3\34\3\34\3\35\3"+
		"\35\3\35\5\35\u023e\n\35\3\35\3\35\5\35\u0242\n\35\3\36\3\36\3\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\5\37\u024e\n\37\3 \3 \3 \3 \3 \3 \3!\3"+
		"!\3!\3!\5!\u025a\n!\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$"+
		"\3%\3%\3%\3%\3%\5%\u0270\n%\3%\3%\3%\3%\7%\u0276\n%\f%\16%\u0279\13%\3"+
		"&\3&\5&\u027d\n&\3&\5&\u0280\n&\3&\3&\5&\u0284\n&\3\'\3\'\3(\3(\3(\3("+
		"\3(\5(\u028d\n(\3(\3(\3(\3(\3(\7(\u0294\n(\f(\16(\u0297\13(\5(\u0299\n"+
		"(\3)\3)\3)\3)\3)\3)\5)\u02a1\n)\3)\3)\3)\3)\3)\5)\u02a8\n)\5)\u02aa\n"+
		")\3*\3*\3*\3*\3*\5*\u02b1\n*\3*\3*\3*\3*\3*\5*\u02b8\n*\5*\u02ba\n*\3"+
		"+\3+\3+\3+\3+\3+\5+\u02c2\n+\3+\3+\3+\5+\u02c7\n+\3+\3+\3+\3+\3+\5+\u02ce"+
		"\n+\3,\3,\3,\3,\3,\5,\u02d5\n,\3,\3,\3-\3-\3-\3-\3-\3-\5-\u02df\n-\3."+
		"\3.\3.\7.\u02e4\n.\f.\16.\u02e7\13.\3/\3/\3/\3/\5/\u02ed\n/\3\60\3\60"+
		"\3\60\7\60\u02f2\n\60\f\60\16\60\u02f5\13\60\3\61\3\61\3\61\3\61\3\61"+
		"\3\61\5\61\u02fd\n\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u0307"+
		"\n\62\3\63\3\63\5\63\u030b\n\63\3\63\3\63\3\63\3\63\3\63\3\63\7\63\u0313"+
		"\n\63\f\63\16\63\u0316\13\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\7\64\u031f"+
		"\n\64\f\64\16\64\u0322\13\64\3\65\3\65\3\65\5\65\u0327\n\65\3\65\3\65"+
		"\5\65\u032b\n\65\3\65\3\65\5\65\u032f\n\65\3\65\3\65\3\65\3\66\3\66\3"+
		"\66\3\66\7\66\u0338\n\66\f\66\16\66\u033b\13\66\3\67\3\67\3\67\5\67\u0340"+
		"\n\67\3\67\3\67\3\67\38\38\38\39\39\39\39\39\79\u034d\n9\f9\169\u0350"+
		"\139\3:\3:\3:\5:\u0355\n:\3:\3:\5:\u0359\n:\3:\3:\5:\u035d\n:\3;\3;\3"+
		";\3;\3;\5;\u0364\n;\3;\3;\3;\7;\u0369\n;\f;\16;\u036c\13;\3<\3<\3<\5<"+
		"\u0371\n<\3<\3<\3<\5<\u0376\n<\5<\u0378\n<\3<\3<\5<\u037c\n<\3=\3=\3="+
		"\3>\3>\5>\u0383\n>\3>\3>\3>\7>\u0388\n>\f>\16>\u038b\13>\3>\3>\3>\3?\3"+
		"?\3?\5?\u0393\n?\3?\3?\3?\3@\3@\3@\3@\3@\6@\u039d\n@\r@\16@\u039e\3@\3"+
		"@\3@\3@\3A\3A\6A\u03a7\nA\rA\16A\u03a8\3A\3A\3A\3B\3B\3B\3B\3B\6B\u03b3"+
		"\nB\rB\16B\u03b4\3B\3B\5B\u03b9\nB\3B\3B\3B\3C\3C\3C\3C\5C\u03c2\nC\3"+
		"C\3C\3C\7C\u03c7\nC\fC\16C\u03ca\13C\3C\3C\3C\3D\3D\3D\3D\3D\3D\3D\3D"+
		"\3D\3E\3E\3E\3E\3E\6E\u03dd\nE\rE\16E\u03de\3F\3F\3F\5F\u03e4\nF\3F\3"+
		"F\3F\5F\u03e9\nF\7F\u03eb\nF\fF\16F\u03ee\13F\3F\3F\3F\3F\3G\3G\3G\7G"+
		"\u03f7\nG\fG\16G\u03fa\13G\3H\3H\3H\7H\u03ff\nH\fH\16H\u0402\13H\3I\5"+
		"I\u0405\nI\3I\3I\3J\3J\3J\5J\u040c\nJ\3K\3K\3K\7K\u0411\nK\fK\16K\u0414"+
		"\13K\3L\3L\3L\5L\u0419\nL\3M\3M\3M\7M\u041e\nM\fM\16M\u0421\13M\3N\3N"+
		"\3N\7N\u0426\nN\fN\16N\u0429\13N\3O\3O\3O\3O\5O\u042f\nO\3P\3P\3P\3P\5"+
		"P\u0435\nP\3Q\3Q\3Q\3Q\5Q\u043b\nQ\3R\3R\3R\3R\5R\u0441\nR\3S\3S\3S\3"+
		"S\5S\u0447\nS\3T\3T\3T\3T\3T\3T\3T\7T\u0450\nT\fT\16T\u0453\13T\3U\3U"+
		"\3U\5U\u0458\nU\3V\7V\u045b\nV\fV\16V\u045e\13V\3V\3V\3W\3W\3W\5W\u0465"+
		"\nW\3X\3X\3X\3X\3X\3X\3X\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Z\3Z\3Z\7Z\u0478\nZ\fZ"+
		"\16Z\u047b\13Z\3[\3[\3[\3[\3[\3[\7[\u0483\n[\f[\16[\u0486\13[\3\\\3\\"+
		"\3\\\3\\\3\\\3\\\3]\3]\3]\3^\3^\3^\3^\3_\3_\3_\3_\3_\3_\3_\5_\u049c\n"+
		"_\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`\5`\u04ad\n`\3a\3a\3a\3"+
		"a\3b\3b\3b\3c\3c\5c\u04b8\nc\3c\3c\3d\3d\3e\3e\3e\3e\3e\3f\3f\3f\3f\3"+
		"f\3g\3g\3g\3h\3h\3h\5h\u04ce\nh\7h\u04d0\nh\fh\16h\u04d3\13h\3h\3h\3i"+
		"\3i\5i\u04d9\ni\3j\3j\5j\u04dd\nj\3k\3k\3k\3k\3l\3l\3l\3l\5l\u04e7\nl"+
		"\3l\3l\3l\5l\u04ec\nl\3l\3l\3l\3l\3m\3m\3m\3m\3m\3m\3m\3m\5m\u04fa\nm"+
		"\3m\3m\3m\3m\3m\7m\u0501\nm\fm\16m\u0504\13m\3m\3m\3m\5m\u0509\nm\3n\3"+
		"n\3n\3n\3o\3o\3o\3o\3o\3o\3p\3p\3p\3p\3p\3p\3p\3p\3q\3q\3q\3q\7q\u0521"+
		"\nq\fq\16q\u0524\13q\3q\3q\3q\3q\3q\3r\3r\3r\3r\3r\3r\3s\3s\3s\6s\u0534"+
		"\ns\rs\16s\u0535\3t\3t\3t\3t\3u\3u\5u\u053e\nu\3u\3u\3u\5u\u0543\nu\3"+
		"v\3v\3v\7v\u0548\nv\fv\16v\u054b\13v\3w\3w\5w\u054f\nw\3x\3x\5x\u0553"+
		"\nx\3x\3x\3y\3y\3y\3y\5y\u055b\ny\3z\3z\3z\3z\3{\3{\3{\3|\3|\3|\3|\5|"+
		"\u0568\n|\3}\3}\3}\3}\3~\3~\3\177\3\177\5\177\u0572\n\177\3\u0080\3\u0080"+
		"\5\u0080\u0576\n\u0080\3\u0081\3\u0081\3\u0081\5\u0081\u057b\n\u0081\3"+
		"\u0082\3\u0082\3\u0082\3\u0082\3\u0083\3\u0083\3\u0083\3\u0083\3\u0084"+
		"\7\u0084\u0586\n\u0084\f\u0084\16\u0084\u0589\13\u0084\3\u0085\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085"+
		"\5\u0085\u0596\n\u0085\3\u0086\3\u0086\3\u0086\3\u0086\3\u0087\3\u0087"+
		"\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088\3\u0088\5\u0088\u05a4\n\u0088"+
		"\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a"+
		"\3\u008a\3\u008b\3\u008b\3\u008b\3\u008b\3\u008c\3\u008c\3\u008c\3\u008c"+
		"\5\u008c\u05b8\n\u008c\3\u008c\3\u008c\3\u008d\3\u008d\3\u008d\3\u008d"+
		"\3\u008d\5\u008d\u05c1\n\u008d\5\u008d\u05c3\n\u008d\3\u008d\3\u008d\3"+
		"\u008e\3\u008e\5\u008e\u05c9\n\u008e\3\u008f\3\u008f\3\u008f\3\u008f\3"+
		"\u008f\3\u0090\3\u0090\3\u0091\3\u0091\3\u0091\3\u0091\3\u0091\3\u0091"+
		"\5\u0091\u05d8\n\u0091\5\u0091\u05da\n\u0091\5\u0091\u05dc\n\u0091\3\u0091"+
		"\3\u0091\3\u0092\3\u0092\5\u0092\u05e2\n\u0092\3\u0093\3\u0093\3\u0093"+
		"\3\u0093\3\u0093\3\u0094\3\u0094\3\u0095\3\u0095\3\u0096\3\u0096\3\u0097"+
		"\3\u0097\3\u0098\3\u0098\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099"+
		"\5\u0099\u05f9\n\u0099\5\u0099\u05fb\n\u0099\3\u009a\3\u009a\3\u009a\3"+
		"\u009a\7\u009a\u0601\n\u009a\f\u009a\16\u009a\u0604\13\u009a\5\u009a\u0606"+
		"\n\u009a\3\u009a\3\u009a\3\u009a\3\u009a\3\u009a\5\u009a\u060d\n\u009a"+
		"\3\u009b\3\u009b\3\u009b\5\u009b\u0612\n\u009b\3\u009c\3\u009c\5\u009c"+
		"\u0616\n\u009c\3\u009d\3\u009d\3\u009d\3\u009d\3\u009d\3\u009e\3\u009e"+
		"\3\u009e\3\u009e\3\u009e\7\u009e\u0622\n\u009e\f\u009e\16\u009e\u0625"+
		"\13\u009e\5\u009e\u0627\n\u009e\3\u009e\3\u009e\3\u009e\3\u009e\3\u009f"+
		"\3\u009f\5\u009f\u062f\n\u009f\3\u00a0\3\u00a0\5\u00a0\u0633\n\u00a0\3"+
		"\u00a0\3\u00a0\3\u00a0\3\u00a1\3\u00a1\5\u00a1\u063a\n\u00a1\3\u00a1\3"+
		"\u00a1\3\u00a2\3\u00a2\3\u00a3\3\u00a3\3\u00a4\3\u00a4\3\u00a4\2\2\u00a5"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFH"+
		"JLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"+
		"\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4"+
		"\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc"+
		"\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4"+
		"\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec"+
		"\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104"+
		"\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c"+
		"\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134"+
		"\u0136\u0138\u013a\u013c\u013e\u0140\u0142\u0144\u0146\2\r\4\2\21\21i"+
		"i\3\2RS\3\2\24\35\4\2\5\5#-\3\2/\60\4\2\f\f\61\63\3\2\u0084\u0085\3\2"+
		"\u0087\u008d\3\2\u008e\u0092\4\2\23\23\u00a9\u00a9\5\2=\177\u00a2\u00a7"+
		"\u00aa\u00aa\2\u0684\2\u0148\3\2\2\2\4\u0150\3\2\2\2\6\u0156\3\2\2\2\b"+
		"\u0159\3\2\2\2\n\u016a\3\2\2\2\f\u0175\3\2\2\2\16\u017a\3\2\2\2\20\u017d"+
		"\3\2\2\2\22\u0180\3\2\2\2\24\u0191\3\2\2\2\26\u0193\3\2\2\2\30\u0196\3"+
		"\2\2\2\32\u019c\3\2\2\2\34\u01a0\3\2\2\2\36\u01a4\3\2\2\2 \u01a8\3\2\2"+
		"\2\"\u01af\3\2\2\2$\u01bf\3\2\2\2&\u01c8\3\2\2\2(\u01d7\3\2\2\2*\u01de"+
		"\3\2\2\2,\u01e5\3\2\2\2.\u01f6\3\2\2\2\60\u0206\3\2\2\2\62\u0226\3\2\2"+
		"\2\64\u022b\3\2\2\2\66\u022e\3\2\2\28\u023a\3\2\2\2:\u0243\3\2\2\2<\u024d"+
		"\3\2\2\2>\u024f\3\2\2\2@\u0259\3\2\2\2B\u025b\3\2\2\2D\u0260\3\2\2\2F"+
		"\u0264\3\2\2\2H\u026a\3\2\2\2J\u027f\3\2\2\2L\u0285\3\2\2\2N\u0287\3\2"+
		"\2\2P\u029a\3\2\2\2R\u02ab\3\2\2\2T\u02bb\3\2\2\2V\u02cf\3\2\2\2X\u02de"+
		"\3\2\2\2Z\u02e0\3\2\2\2\\\u02e8\3\2\2\2^\u02ee\3\2\2\2`\u02fc\3\2\2\2"+
		"b\u0306\3\2\2\2d\u030a\3\2\2\2f\u031a\3\2\2\2h\u0323\3\2\2\2j\u0333\3"+
		"\2\2\2l\u033c\3\2\2\2n\u0344\3\2\2\2p\u0347\3\2\2\2r\u0351\3\2\2\2t\u0363"+
		"\3\2\2\2v\u036d\3\2\2\2x\u037d\3\2\2\2z\u0382\3\2\2\2|\u038f\3\2\2\2~"+
		"\u0397\3\2\2\2\u0080\u03a6\3\2\2\2\u0082\u03ad\3\2\2\2\u0084\u03bd\3\2"+
		"\2\2\u0086\u03ce\3\2\2\2\u0088\u03d7\3\2\2\2\u008a\u03e0\3\2\2\2\u008c"+
		"\u03f3\3\2\2\2\u008e\u03fb\3\2\2\2\u0090\u0404\3\2\2\2\u0092\u0408\3\2"+
		"\2\2\u0094\u040d\3\2\2\2\u0096\u0415\3\2\2\2\u0098\u041a\3\2\2\2\u009a"+
		"\u0422\3\2\2\2\u009c\u042a\3\2\2\2\u009e\u0430\3\2\2\2\u00a0\u0436\3\2"+
		"\2\2\u00a2\u043c\3\2\2\2\u00a4\u0442\3\2\2\2\u00a6\u0448\3\2\2\2\u00a8"+
		"\u0457\3\2\2\2\u00aa\u045c\3\2\2\2\u00ac\u0464\3\2\2\2\u00ae\u0466\3\2"+
		"\2\2\u00b0\u046d\3\2\2\2\u00b2\u0474\3\2\2\2\u00b4\u047c\3\2\2\2\u00b6"+
		"\u0487\3\2\2\2\u00b8\u048d\3\2\2\2\u00ba\u0490\3\2\2\2\u00bc\u0494\3\2"+
		"\2\2\u00be\u04ac\3\2\2\2\u00c0\u04ae\3\2\2\2\u00c2\u04b2\3\2\2\2\u00c4"+
		"\u04b5\3\2\2\2\u00c6\u04bb\3\2\2\2\u00c8\u04bd\3\2\2\2\u00ca\u04c2\3\2"+
		"\2\2\u00cc\u04c7\3\2\2\2\u00ce\u04ca\3\2\2\2\u00d0\u04d8\3\2\2\2\u00d2"+
		"\u04dc\3\2\2\2\u00d4\u04de\3\2\2\2\u00d6\u04e2\3\2\2\2\u00d8\u0508\3\2"+
		"\2\2\u00da\u050a\3\2\2\2\u00dc\u050e\3\2\2\2\u00de\u0514\3\2\2\2\u00e0"+
		"\u051c\3\2\2\2\u00e2\u052a\3\2\2\2\u00e4\u0530\3\2\2\2\u00e6\u0537\3\2"+
		"\2\2\u00e8\u0542\3\2\2\2\u00ea\u0544\3\2\2\2\u00ec\u054e\3\2\2\2\u00ee"+
		"\u0552\3\2\2\2\u00f0\u055a\3\2\2\2\u00f2\u055c\3\2\2\2\u00f4\u0560\3\2"+
		"\2\2\u00f6\u0567\3\2\2\2\u00f8\u0569\3\2\2\2\u00fa\u056d\3\2\2\2\u00fc"+
		"\u0571\3\2\2\2\u00fe\u0575\3\2\2\2\u0100\u057a\3\2\2\2\u0102\u057c\3\2"+
		"\2\2\u0104\u0580\3\2\2\2\u0106\u0587\3\2\2\2\u0108\u0595\3\2\2\2\u010a"+
		"\u0597\3\2\2\2\u010c\u059b\3\2\2\2\u010e\u059f\3\2\2\2\u0110\u05a7\3\2"+
		"\2\2\u0112\u05ab\3\2\2\2\u0114\u05af\3\2\2\2\u0116\u05b3\3\2\2\2\u0118"+
		"\u05bb\3\2\2\2\u011a\u05c8\3\2\2\2\u011c\u05ca\3\2\2\2\u011e\u05cf\3\2"+
		"\2\2\u0120\u05d1\3\2\2\2\u0122\u05e1\3\2\2\2\u0124\u05e3\3\2\2\2\u0126"+
		"\u05e8\3\2\2\2\u0128\u05ea\3\2\2\2\u012a\u05ec\3\2\2\2\u012c\u05ee\3\2"+
		"\2\2\u012e\u05f0\3\2\2\2\u0130\u05fa\3\2\2\2\u0132\u060c\3\2\2\2\u0134"+
		"\u0611\3\2\2\2\u0136\u0615\3\2\2\2\u0138\u0617\3\2\2\2\u013a\u061c\3\2"+
		"\2\2\u013c\u062c\3\2\2\2\u013e\u0632\3\2\2\2\u0140\u0637\3\2\2\2\u0142"+
		"\u063d\3\2\2\2\u0144\u063f\3\2\2\2\u0146\u0641\3\2\2\2\u0148\u0149\5\4"+
		"\3\2\u0149\u014a\7\2\2\3\u014a\3\3\2\2\2\u014b\u014c\7h\2\2\u014c\u014d"+
		"\7g\2\2\u014d\u014e\5\u0144\u00a3\2\u014e\u014f\7\3\2\2\u014f\u0151\3"+
		"\2\2\2\u0150\u014b\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0154\3\2\2\2\u0152"+
		"\u0155\5\b\5\2\u0153\u0155\5\6\4\2\u0154\u0152\3\2\2\2\u0154\u0153\3\2"+
		"\2\2\u0155\5\3\2\2\2\u0156\u0157\5\n\6\2\u0157\u0158\5\f\7\2\u0158\7\3"+
		"\2\2\2\u0159\u015a\7\4\2\2\u015a\u015b\7\u0082\2\2\u015b\u015c\7\u00b1"+
		"\2\2\u015c\u015d\7\5\2\2\u015d\u015e\5\u0142\u00a2\2\u015e\u015f\7\3\2"+
		"\2\u015f\u0160\5\n\6\2\u0160\t\3\2\2\2\u0161\u0165\5<\37\2\u0162\u0165"+
		"\5> \2\u0163\u0165\5N(\2\u0164\u0161\3\2\2\2\u0164\u0162\3\2\2\2\u0164"+
		"\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0167\7\3\2\2\u0167\u0169\3\2"+
		"\2\2\u0168\u0164\3\2\2\2\u0169\u016c\3\2\2\2\u016a\u0168\3\2\2\2\u016a"+
		"\u016b\3\2\2\2\u016b\u0172\3\2\2\2\u016c\u016a\3\2\2\2\u016d\u016e\5@"+
		"!\2\u016e\u016f\7\3\2\2\u016f\u0171\3\2\2\2\u0170\u016d\3\2\2\2\u0171"+
		"\u0174\3\2\2\2\u0172\u0170\3\2\2\2\u0172\u0173\3\2\2\2\u0173\13\3\2\2"+
		"\2\u0174\u0172\3\2\2\2\u0175\u0176\5\22\n\2\u0176\r\3\2\2\2\u0177\u0179"+
		"\5\24\13\2\u0178\u0177\3\2\2\2\u0179\u017c\3\2\2\2\u017a\u0178\3\2\2\2"+
		"\u017a\u017b\3\2\2\2\u017b\17\3\2\2\2\u017c\u017a\3\2\2\2\u017d\u017e"+
		"\5\16\b\2\u017e\u017f\5^\60\2\u017f\21\3\2\2\2\u0180\u0182\5\16\b\2\u0181"+
		"\u0183\5^\60\2\u0182\u0181\3\2\2\2\u0182\u0183\3\2\2\2\u0183\23\3\2\2"+
		"\2\u0184\u0192\5\26\f\2\u0185\u0192\5\30\r\2\u0186\u0192\5\32\16\2\u0187"+
		"\u0192\5\34\17\2\u0188\u0192\5\36\20\2\u0189\u0192\5 \21\2\u018a\u0192"+
		"\5\"\22\2\u018b\u0192\5$\23\2\u018c\u0192\5&\24\2\u018d\u0192\5*\26\2"+
		"\u018e\u0192\5.\30\2\u018f\u0192\5\66\34\2\u0190\u0192\5:\36\2\u0191\u0184"+
		"\3\2\2\2\u0191\u0185\3\2\2\2\u0191\u0186\3\2\2\2\u0191\u0187\3\2\2\2\u0191"+
		"\u0188\3\2\2\2\u0191\u0189\3\2\2\2\u0191\u018a\3\2\2\2\u0191\u018b\3\2"+
		"\2\2\u0191\u018c\3\2\2\2\u0191\u018d\3\2\2\2\u0191\u018e\3\2\2\2\u0191"+
		"\u018f\3\2\2\2\u0191\u0190\3\2\2\2\u0192\25\3\2\2\2\u0193\u0194\5b\62"+
		"\2\u0194\u0195\7\3\2\2\u0195\27\3\2\2\2\u0196\u0197\7\6\2\2\u0197\u0198"+
		"\5J&\2\u0198\u0199\7\7\2\2\u0199\u019a\5`\61\2\u019a\u019b\7\3\2\2\u019b"+
		"\31\3\2\2\2\u019c\u019d\7\b\2\2\u019d\u019e\5\16\b\2\u019e\u019f\7\t\2"+
		"\2\u019f\33\3\2\2\2\u01a0\u01a1\7\u00a2\2\2\u01a1\u01a2\7\u00a3\2\2\u01a2"+
		"\u01a3\7\3\2\2\u01a3\35\3\2\2\2\u01a4\u01a5\7\u00a4\2\2\u01a5\u01a6\7"+
		"\u00a3\2\2\u01a6\u01a7\7\3\2\2\u01a7\37\3\2\2\2\u01a8\u01a9\7\u00a5\2"+
		"\2\u01a9\u01aa\7\u00a6\2\2\u01aa\u01ab\5`\61\2\u01ab\u01ac\7\3\2\2\u01ac"+
		"!\3\2\2\2\u01ad\u01b0\5f\64\2\u01ae\u01b0\5j\66\2\u01af\u01ad\3\2\2\2"+
		"\u01af\u01ae\3\2\2\2\u01b0\u01b9\3\2\2\2\u01b1\u01b8\5f\64\2\u01b2\u01b8"+
		"\5j\66\2\u01b3\u01b8\5n8\2\u01b4\u01b8\5p9\2\u01b5\u01b8\5t;\2\u01b6\u01b8"+
		"\5x=\2\u01b7\u01b1\3\2\2\2\u01b7\u01b2\3\2\2\2\u01b7\u01b3\3\2\2\2\u01b7"+
		"\u01b4\3\2\2\2\u01b7\u01b5\3\2\2\2\u01b7\u01b6\3\2\2\2\u01b8\u01bb\3\2"+
		"\2\2\u01b9\u01b7\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bc\3\2\2\2\u01bb"+
		"\u01b9\3\2\2\2\u01bc\u01bd\7C\2\2\u01bd\u01be\5\24\13\2\u01be#\3\2\2\2"+
		"\u01bf\u01c0\7D\2\2\u01c0\u01c1\7\n\2\2\u01c1\u01c2\5^\60\2\u01c2\u01c3"+
		"\7\13\2\2\u01c3\u01c4\7Y\2\2\u01c4\u01c5\5\24\13\2\u01c5\u01c6\7Z\2\2"+
		"\u01c6\u01c7\5\24\13\2\u01c7%\3\2\2\2\u01c8\u01c9\7T\2\2\u01c9\u01ca\7"+
		"\n\2\2\u01ca\u01cb\5^\60\2\u01cb\u01cd\7\13\2\2\u01cc\u01ce\5(\25\2\u01cd"+
		"\u01cc\3\2\2\2\u01ce\u01cf\3\2\2\2\u01cf\u01cd\3\2\2\2\u01cf\u01d0\3\2"+
		"\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01d2\7X\2\2\u01d2\u01d3\7C\2\2\u01d3\u01d4"+
		"\5\24\13\2\u01d4\'\3\2\2\2\u01d5\u01d6\7U\2\2\u01d6\u01d8\5`\61\2\u01d7"+
		"\u01d5\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01d7\3\2\2\2\u01d9\u01da\3\2"+
		"\2\2\u01da\u01db\3\2\2\2\u01db\u01dc\7C\2\2\u01dc\u01dd\5\24\13\2\u01dd"+
		")\3\2\2\2\u01de\u01df\7V\2\2\u01df\u01e1\5\32\16\2\u01e0\u01e2\5,\27\2"+
		"\u01e1\u01e0\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e3\u01e4"+
		"\3\2\2\2\u01e4+\3\2\2\2\u01e5\u01e8\7W\2\2\u01e6\u01e9\7\f\2\2\u01e7\u01e9"+
		"\5J&\2\u01e8\u01e6\3\2\2\2\u01e8\u01e7\3\2\2\2\u01e9\u01f1\3\2\2\2\u01ea"+
		"\u01ed\7\r\2\2\u01eb\u01ee\7\f\2\2\u01ec\u01ee\5J&\2\u01ed\u01eb\3\2\2"+
		"\2\u01ed\u01ec\3\2\2\2\u01ee\u01f0\3\2\2\2\u01ef\u01ea\3\2\2\2\u01f0\u01f3"+
		"\3\2\2\2\u01f1\u01ef\3\2\2\2\u01f1\u01f2\3\2\2\2\u01f2\u01f4\3\2\2\2\u01f3"+
		"\u01f1\3\2\2\2\u01f4\u01f5\5\32\16\2\u01f5-\3\2\2\2\u01f6\u01f7\7[\2\2"+
		"\u01f7\u01f8\7\n\2\2\u01f8\u01f9\5^\60\2\u01f9\u01fb\7\13\2\2\u01fa\u01fc"+
		"\5\60\31\2\u01fb\u01fa\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd\u01fb\3\2\2\2"+
		"\u01fd\u01fe\3\2\2\2\u01fe\u01ff\3\2\2\2\u01ff\u0201\7X\2\2\u0200\u0202"+
		"\5\u00c2b\2\u0201\u0200\3\2\2\2\u0201\u0202\3\2\2\2\u0202\u0203\3\2\2"+
		"\2\u0203\u0204\7C\2\2\u0204\u0205\5\24\13\2\u0205/\3\2\2\2\u0206\u020a"+
		"\7U\2\2\u0207\u0208\5\u00c2b\2\u0208\u0209\7F\2\2\u0209\u020b\3\2\2\2"+
		"\u020a\u0207\3\2\2\2\u020a\u020b\3\2\2\2\u020b\u020c\3\2\2\2\u020c\u0211"+
		"\5\u0130\u0099\2\u020d\u020e\7\r\2\2\u020e\u0210\5\u0130\u0099\2\u020f"+
		"\u020d\3\2\2\2\u0210\u0213\3\2\2\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2"+
		"\2\2\u0212\u0214\3\2\2\2\u0213\u0211\3\2\2\2\u0214\u0215\7C\2\2\u0215"+
		"\u0216\5\24\13\2\u0216\61\3\2\2\2\u0217\u0218\7\16\2\2\u0218\u0223\5J"+
		"&\2\u0219\u021a\7\n\2\2\u021a\u021f\7\u00ab\2\2\u021b\u021c\7\17\2\2\u021c"+
		"\u021e\7\u00ab\2\2\u021d\u021b\3\2\2\2\u021e\u0221\3\2\2\2\u021f\u021d"+
		"\3\2\2\2\u021f\u0220\3\2\2\2\u0220\u0222\3\2\2\2\u0221\u021f\3\2\2\2\u0222"+
		"\u0224\7\13\2\2\u0223\u0219\3\2\2\2\u0223\u0224\3\2\2\2\u0224\u0227\3"+
		"\2\2\2\u0225\u0227\7\177\2\2\u0226\u0217\3\2\2\2\u0226\u0225\3\2\2\2\u0227"+
		"\63\3\2\2\2\u0228\u022a\5\62\32\2\u0229\u0228\3\2\2\2\u022a\u022d\3\2"+
		"\2\2\u022b\u0229\3\2\2\2\u022b\u022c\3\2\2\2\u022c\65\3\2\2\2\u022d\u022b"+
		"\3\2\2\2\u022e\u022f\5\64\33\2\u022f\u0230\7r\2\2\u0230\u0235\58\35\2"+
		"\u0231\u0232\7\17\2\2\u0232\u0234\58\35\2\u0233\u0231\3\2\2\2\u0234\u0237"+
		"\3\2\2\2\u0235\u0233\3\2\2\2\u0235\u0236\3\2\2\2\u0236\u0238\3\2\2\2\u0237"+
		"\u0235\3\2\2\2\u0238\u0239\7\3\2\2\u0239\67\3\2\2\2\u023a\u023d\5\u00c2"+
		"b\2\u023b\u023c\7F\2\2\u023c\u023e\5\u0130\u0099\2\u023d\u023b\3\2\2\2"+
		"\u023d\u023e\3\2\2\2\u023e\u0241\3\2\2\2\u023f\u0240\7\7\2\2\u0240\u0242"+
		"\5`\61\2\u0241\u023f\3\2\2\2\u0241\u0242\3\2\2\2\u02429\3\2\2\2\u0243"+
		"\u0244\7\u00a7\2\2\u0244\u0245\7\n\2\2\u0245\u0246\5^\60\2\u0246\u0247"+
		"\7\13\2\2\u0247\u0248\5\24\13\2\u0248;\3\2\2\2\u0249\u024e\5B\"\2\u024a"+
		"\u024e\5D#\2\u024b\u024e\5F$\2\u024c\u024e\5H%\2\u024d\u0249\3\2\2\2\u024d"+
		"\u024a\3\2\2\2\u024d\u024b\3\2\2\2\u024d\u024c\3\2\2\2\u024e=\3\2\2\2"+
		"\u024f\u0250\7o\2\2\u0250\u0251\7\u0082\2\2\u0251\u0252\7\u00b1\2\2\u0252"+
		"\u0253\7\5\2\2\u0253\u0254\5\u0142\u00a2\2\u0254?\3\2\2\2\u0255\u025a"+
		"\5T+\2\u0256\u025a\5P)\2\u0257\u025a\5V,\2\u0258\u025a\5R*\2\u0259\u0255"+
		"\3\2\2\2\u0259\u0256\3\2\2\2\u0259\u0257\3\2\2\2\u0259\u0258\3\2\2\2\u025a"+
		"A\3\2\2\2\u025b\u025c\7o\2\2\u025c\u025d\7X\2\2\u025d\u025e\7Q\2\2\u025e"+
		"\u025f\5\u0142\u00a2\2\u025fC\3\2\2\2\u0260\u0261\7o\2\2\u0261\u0262\7"+
		"\20\2\2\u0262\u0263\t\2\2\2\u0263E\3\2\2\2\u0264\u0265\7o\2\2\u0265\u0266"+
		"\7X\2\2\u0266\u0267\7B\2\2\u0267\u0268\7I\2\2\u0268\u0269\t\3\2\2\u0269"+
		"G\3\2\2\2\u026a\u026f\7o\2\2\u026b\u026c\7\22\2\2\u026c\u0270\5J&\2\u026d"+
		"\u026e\7X\2\2\u026e\u0270\7\22\2\2\u026f\u026b\3\2\2\2\u026f\u026d\3\2"+
		"\2\2\u0270\u0277\3\2\2\2\u0271\u0272\5L\'\2\u0272\u0273\7\5\2\2\u0273"+
		"\u0274\5\u0144\u00a3\2\u0274\u0276\3\2\2\2\u0275\u0271\3\2\2\2\u0276\u0279"+
		"\3\2\2\2\u0277\u0275\3\2\2\2\u0277\u0278\3\2\2\2\u0278I\3\2\2\2\u0279"+
		"\u0277\3\2\2\2\u027a\u027d\7\u00b1\2\2\u027b\u027d\5\u0146\u00a4\2\u027c"+
		"\u027a\3\2\2\2\u027c\u027b\3\2\2\2\u027d\u027e\3\2\2\2\u027e\u0280\7\23"+
		"\2\2\u027f\u027c\3\2\2\2\u027f\u0280\3\2\2\2\u0280\u0283\3\2\2\2\u0281"+
		"\u0284\7\u00b1\2\2\u0282\u0284\5\u0146\u00a4\2\u0283\u0281\3\2\2\2\u0283"+
		"\u0282\3\2\2\2\u0284K\3\2\2\2\u0285\u0286\t\4\2\2\u0286M\3\2\2\2\u0287"+
		"\u0288\7\u0080\2\2\u0288\u028c\7\4\2\2\u0289\u028a\7\u0082\2\2\u028a\u028b"+
		"\7\u00b1\2\2\u028b\u028d\7\5\2\2\u028c\u0289\3\2\2\2\u028c\u028d\3\2\2"+
		"\2\u028d\u028e\3\2\2\2\u028e\u0298\5\u0142\u00a2\2\u028f\u0290\7G\2\2"+
		"\u0290\u0295\5\u0142\u00a2\2\u0291\u0292\7\17\2\2\u0292\u0294\5\u0142"+
		"\u00a2\2\u0293\u0291\3\2\2\2\u0294\u0297\3\2\2\2\u0295\u0293\3\2\2\2\u0295"+
		"\u0296\3\2\2\2\u0296\u0299\3\2\2\2\u0297\u0295\3\2\2\2\u0298\u028f\3\2"+
		"\2\2\u0298\u0299\3\2\2\2\u0299O\3\2\2\2\u029a\u029b\7o\2\2\u029b\u029c"+
		"\5\64\33\2\u029c\u029d\7r\2\2\u029d\u02a0\5\u00c2b\2\u029e\u029f\7F\2"+
		"\2\u029f\u02a1\5\u0130\u0099\2\u02a0\u029e\3\2\2\2\u02a0\u02a1\3\2\2\2"+
		"\u02a1\u02a9\3\2\2\2\u02a2\u02a3\7\7\2\2\u02a3\u02aa\5`\61\2\u02a4\u02a7"+
		"\7\36\2\2\u02a5\u02a6\7\7\2\2\u02a6\u02a8\5`\61\2\u02a7\u02a5\3\2\2\2"+
		"\u02a7\u02a8\3\2\2\2\u02a8\u02aa\3\2\2\2\u02a9\u02a2\3\2\2\2\u02a9\u02a4"+
		"\3\2\2\2\u02aaQ\3\2\2\2\u02ab\u02ac\7o\2\2\u02ac\u02ad\7p\2\2\u02ad\u02b0"+
		"\7q\2\2\u02ae\u02af\7F\2\2\u02af\u02b1\5\u0130\u0099\2\u02b0\u02ae\3\2"+
		"\2\2\u02b0\u02b1\3\2\2\2\u02b1\u02b9\3\2\2\2\u02b2\u02b3\7\7\2\2\u02b3"+
		"\u02ba\5`\61\2\u02b4\u02b7\7\36\2\2\u02b5\u02b6\7\7\2\2\u02b6\u02b8\5"+
		"`\61\2\u02b7\u02b5\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8\u02ba\3\2\2\2\u02b9"+
		"\u02b2\3\2\2\2\u02b9\u02b4\3\2\2\2\u02baS\3\2\2\2\u02bb\u02bc\7o\2\2\u02bc"+
		"\u02bd\5\64\33\2\u02bd\u02be\7\37\2\2\u02be\u02bf\5J&\2\u02bf\u02c1\7"+
		"\n\2\2\u02c0\u02c2\5Z.\2\u02c1\u02c0\3\2\2\2\u02c1\u02c2\3\2\2\2\u02c2"+
		"\u02c3\3\2\2\2\u02c3\u02c6\7\13\2\2\u02c4\u02c5\7F\2\2\u02c5\u02c7\5\u0130"+
		"\u0099\2\u02c6\u02c4\3\2\2\2\u02c6\u02c7\3\2\2\2\u02c7\u02cd\3\2\2\2\u02c8"+
		"\u02c9\7\b\2\2\u02c9\u02ca\5\22\n\2\u02ca\u02cb\7\t\2\2\u02cb\u02ce\3"+
		"\2\2\2\u02cc\u02ce\7\36\2\2\u02cd\u02c8\3\2\2\2\u02cd\u02cc\3\2\2\2\u02ce"+
		"U\3\2\2\2\u02cf\u02d0\7o\2\2\u02d0\u02d1\7l\2\2\u02d1\u02d2\5J&\2\u02d2"+
		"\u02d4\7F\2\2\u02d3\u02d5\5X-\2\u02d4\u02d3\3\2\2\2\u02d4\u02d5\3\2\2"+
		"\2\u02d5\u02d6\3\2\2\2\u02d6\u02d7\5`\61\2\u02d7W\3\2\2\2\u02d8\u02d9"+
		"\7 \2\2\u02d9\u02df\7!\2\2\u02da\u02db\7 \2\2\u02db\u02df\7\"\2\2\u02dc"+
		"\u02dd\7~\2\2\u02dd\u02df\7\u0081\2\2\u02de\u02d8\3\2\2\2\u02de\u02da"+
		"\3\2\2\2\u02de\u02dc\3\2\2\2\u02dfY\3\2\2\2\u02e0\u02e5\5\\/\2\u02e1\u02e2"+
		"\7\17\2\2\u02e2\u02e4\5\\/\2\u02e3\u02e1\3\2\2\2\u02e4\u02e7\3\2\2\2\u02e5"+
		"\u02e3\3\2\2\2\u02e5\u02e6\3\2\2\2\u02e6[\3\2\2\2\u02e7\u02e5\3\2\2\2"+
		"\u02e8\u02e9\7\6\2\2\u02e9\u02ec\5J&\2\u02ea\u02eb\7F\2\2\u02eb\u02ed"+
		"\5\u0130\u0099\2\u02ec\u02ea\3\2\2\2\u02ec\u02ed\3\2\2\2\u02ed]\3\2\2"+
		"\2\u02ee\u02f3\5`\61\2\u02ef\u02f0\7\17\2\2\u02f0\u02f2\5`\61\2\u02f1"+
		"\u02ef\3\2\2\2\u02f2\u02f5\3\2\2\2\u02f3\u02f1\3\2\2\2\u02f3\u02f4\3\2"+
		"\2\2\u02f4_\3\2\2\2\u02f5\u02f3\3\2\2\2\u02f6\u02fd\5b\62\2\u02f7\u02fd"+
		"\5d\63\2\u02f8\u02fd\5~@\2\u02f9\u02fd\5\u0082B\2\u02fa\u02fd\5\u0086"+
		"D\2\u02fb\u02fd\5\u0088E\2\u02fc\u02f6\3\2\2\2\u02fc\u02f7\3\2\2\2\u02fc"+
		"\u02f8\3\2\2\2\u02fc\u02f9\3\2\2\2\u02fc\u02fa\3\2\2\2\u02fc\u02fb\3\2"+
		"\2\2\u02fda\3\2\2\2\u02fe\u0307\5z>\2\u02ff\u0307\5\u008cG\2\u0300\u0307"+
		"\5\u00d8m\2\u0301\u0307\5\u00dan\2\u0302\u0307\5\u00dco\2\u0303\u0307"+
		"\5\u00dep\2\u0304\u0307\5\u00e0q\2\u0305\u0307\5\u00e2r\2\u0306\u02fe"+
		"\3\2\2\2\u0306\u02ff\3\2\2\2\u0306\u0300\3\2\2\2\u0306\u0301\3\2\2\2\u0306"+
		"\u0302\3\2\2\2\u0306\u0303\3\2\2\2\u0306\u0304\3\2\2\2\u0306\u0305\3\2"+
		"\2\2\u0307c\3\2\2\2\u0308\u030b\5f\64\2\u0309\u030b\5j\66\2\u030a\u0308"+
		"\3\2\2\2\u030a\u0309\3\2\2\2\u030b\u0314\3\2\2\2\u030c\u0313\5f\64\2\u030d"+
		"\u0313\5j\66\2\u030e\u0313\5n8\2\u030f\u0313\5p9\2\u0310\u0313\5t;\2\u0311"+
		"\u0313\5x=\2\u0312\u030c\3\2\2\2\u0312\u030d\3\2\2\2\u0312\u030e\3\2\2"+
		"\2\u0312\u030f\3\2\2\2\u0312\u0310\3\2\2\2\u0312\u0311\3\2\2\2\u0313\u0316"+
		"\3\2\2\2\u0314\u0312\3\2\2\2\u0314\u0315\3\2\2\2\u0315\u0317\3\2\2\2\u0316"+
		"\u0314\3\2\2\2\u0317\u0318\7C\2\2\u0318\u0319\5`\61\2\u0319e\3\2\2\2\u031a"+
		"\u031b\7=\2\2\u031b\u0320\5h\65\2\u031c\u031d\7\17\2\2\u031d\u031f\5h"+
		"\65\2\u031e\u031c\3\2\2\2\u031f\u0322\3\2\2\2\u0320\u031e\3\2\2\2\u0320"+
		"\u0321\3\2\2\2\u0321g\3\2\2\2\u0322\u0320\3\2\2\2\u0323\u0326\5\u00c2"+
		"b\2\u0324\u0325\7F\2\2\u0325\u0327\5\u0130\u0099\2\u0326\u0324\3\2\2\2"+
		"\u0326\u0327\3\2\2\2\u0327\u032a\3\2\2\2\u0328\u0329\7H\2\2\u0329\u032b"+
		"\7I\2\2\u032a\u0328\3\2\2\2\u032a\u032b\3\2\2\2\u032b\u032e\3\2\2\2\u032c"+
		"\u032d\7G\2\2\u032d\u032f\5\u00c2b\2\u032e\u032c\3\2\2\2\u032e\u032f\3"+
		"\2\2\2\u032f\u0330\3\2\2\2\u0330\u0331\7E\2\2\u0331\u0332\5`\61\2\u0332"+
		"i\3\2\2\2\u0333\u0334\7>\2\2\u0334\u0339\5l\67\2\u0335\u0336\7\17\2\2"+
		"\u0336\u0338\5l\67\2\u0337\u0335\3\2\2\2\u0338\u033b\3\2\2\2\u0339\u0337"+
		"\3\2\2\2\u0339\u033a\3\2\2\2\u033ak\3\2\2\2\u033b\u0339\3\2\2\2\u033c"+
		"\u033f\5\u00c2b\2\u033d\u033e\7F\2\2\u033e\u0340\5\u0130\u0099\2\u033f"+
		"\u033d\3\2\2\2\u033f\u0340\3\2\2\2\u0340\u0341\3\2\2\2\u0341\u0342\7\7"+
		"\2\2\u0342\u0343\5`\61\2\u0343m\3\2\2\2\u0344\u0345\7?\2\2\u0345\u0346"+
		"\5`\61\2\u0346o\3\2\2\2\u0347\u0348\7@\2\2\u0348\u0349\7A\2\2\u0349\u034e"+
		"\5r:\2\u034a\u034b\7\17\2\2\u034b\u034d\5r:\2\u034c\u034a\3\2\2\2\u034d"+
		"\u0350\3\2\2\2\u034e\u034c\3\2\2\2\u034e\u034f\3\2\2\2\u034fq\3\2\2\2"+
		"\u0350\u034e\3\2\2\2\u0351\u0358\5\u00c2b\2\u0352\u0353\7F\2\2\u0353\u0355"+
		"\5\u0130\u0099\2\u0354\u0352\3\2\2\2\u0354\u0355\3\2\2\2\u0355\u0356\3"+
		"\2\2\2\u0356\u0357\7\7\2\2\u0357\u0359\5`\61\2\u0358\u0354\3\2\2\2\u0358"+
		"\u0359\3\2\2\2\u0359\u035c\3\2\2\2\u035a\u035b\7Q\2\2\u035b\u035d\5\u0142"+
		"\u00a2\2\u035c\u035a\3\2\2\2\u035c\u035d\3\2\2\2\u035ds\3\2\2\2\u035e"+
		"\u035f\7B\2\2\u035f\u0364\7A\2\2\u0360\u0361\7K\2\2\u0361\u0362\7B\2\2"+
		"\u0362\u0364\7A\2\2\u0363\u035e\3\2\2\2\u0363\u0360\3\2\2\2\u0364\u0365"+
		"\3\2\2\2\u0365\u036a\5v<\2\u0366\u0367\7\17\2\2\u0367\u0369\5v<\2\u0368"+
		"\u0366\3\2\2\2\u0369\u036c\3\2\2\2\u036a\u0368\3\2\2\2\u036a\u036b\3\2"+
		"\2\2\u036bu\3\2\2\2\u036c\u036a\3\2\2\2\u036d\u0370\5`\61\2\u036e\u0371"+
		"\7L\2\2\u036f\u0371\7M\2\2\u0370\u036e\3\2\2\2\u0370\u036f\3\2\2\2\u0370"+
		"\u0371\3\2\2\2\u0371\u0377\3\2\2\2\u0372\u0375\7I\2\2\u0373\u0376\7R\2"+
		"\2\u0374\u0376\7S\2\2\u0375\u0373\3\2\2\2\u0375\u0374\3\2\2\2\u0376\u0378"+
		"\3\2\2\2\u0377\u0372\3\2\2\2\u0377\u0378\3\2\2\2\u0378\u037b\3\2\2\2\u0379"+
		"\u037a\7Q\2\2\u037a\u037c\5\u0142\u00a2\2\u037b\u0379\3\2\2\2\u037b\u037c"+
		"\3\2\2\2\u037cw\3\2\2\2\u037d\u037e\7J\2\2\u037e\u037f\5\u00c2b\2\u037f"+
		"y\3\2\2\2\u0380\u0383\7N\2\2\u0381\u0383\7O\2\2\u0382\u0380\3\2\2\2\u0382"+
		"\u0381\3\2\2\2\u0383\u0384\3\2\2\2\u0384\u0389\5|?\2\u0385\u0386\7\17"+
		"\2\2\u0386\u0388\5|?\2\u0387\u0385\3\2\2\2\u0388\u038b\3\2\2\2\u0389\u0387"+
		"\3\2\2\2\u0389\u038a\3\2\2\2\u038a\u038c\3\2\2\2\u038b\u0389\3\2\2\2\u038c"+
		"\u038d\7P\2\2\u038d\u038e\5`\61\2\u038e{\3\2\2\2\u038f\u0392\5\u00c2b"+
		"\2\u0390\u0391\7F\2\2\u0391\u0393\5\u0130\u0099\2\u0392\u0390\3\2\2\2"+
		"\u0392\u0393\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0395\7E\2\2\u0395\u0396"+
		"\5`\61\2\u0396}\3\2\2\2\u0397\u0398\7T\2\2\u0398\u0399\7\n\2\2\u0399\u039a"+
		"\5^\60\2\u039a\u039c\7\13\2\2\u039b\u039d\5\u0080A\2\u039c\u039b\3\2\2"+
		"\2\u039d\u039e\3\2\2\2\u039e\u039c\3\2\2\2\u039e\u039f\3\2\2\2\u039f\u03a0"+
		"\3\2\2\2\u03a0\u03a1\7X\2\2\u03a1\u03a2\7C\2\2\u03a2\u03a3\5`\61\2\u03a3"+
		"\177\3\2\2\2\u03a4\u03a5\7U\2\2\u03a5\u03a7\5`\61\2\u03a6\u03a4\3\2\2"+
		"\2\u03a7\u03a8\3\2\2\2\u03a8\u03a6\3\2\2\2\u03a8\u03a9\3\2\2\2\u03a9\u03aa"+
		"\3\2\2\2\u03aa\u03ab\7C\2\2\u03ab\u03ac\5`\61\2\u03ac\u0081\3\2\2\2\u03ad"+
		"\u03ae\7[\2\2\u03ae\u03af\7\n\2\2\u03af\u03b0\5^\60\2\u03b0\u03b2\7\13"+
		"\2\2\u03b1\u03b3\5\u0084C\2\u03b2\u03b1\3\2\2\2\u03b3\u03b4\3\2\2\2\u03b4"+
		"\u03b2\3\2\2\2\u03b4\u03b5\3\2\2\2\u03b5\u03b6\3\2\2\2\u03b6\u03b8\7X"+
		"\2\2\u03b7\u03b9\5\u00c2b\2\u03b8\u03b7\3\2\2\2\u03b8\u03b9\3\2\2\2\u03b9"+
		"\u03ba\3\2\2\2\u03ba\u03bb\7C\2\2\u03bb\u03bc\5`\61\2\u03bc\u0083\3\2"+
		"\2\2\u03bd\u03c1\7U\2\2\u03be\u03bf\5\u00c2b\2\u03bf\u03c0\7F\2\2\u03c0"+
		"\u03c2\3\2\2\2\u03c1\u03be\3\2\2\2\u03c1\u03c2\3\2\2\2\u03c2\u03c3\3\2"+
		"\2\2\u03c3\u03c8\5\u0130\u0099\2\u03c4\u03c5\7\r\2\2\u03c5\u03c7\5\u0130"+
		"\u0099\2\u03c6\u03c4\3\2\2\2\u03c7\u03ca\3\2\2\2\u03c8\u03c6\3\2\2\2\u03c8"+
		"\u03c9\3\2\2\2\u03c9\u03cb\3\2\2\2\u03ca\u03c8\3\2\2\2\u03cb\u03cc\7C"+
		"\2\2\u03cc\u03cd\5`\61\2\u03cd\u0085\3\2\2\2\u03ce\u03cf\7D\2\2\u03cf"+
		"\u03d0\7\n\2\2\u03d0\u03d1\5^\60\2\u03d1\u03d2\7\13\2\2\u03d2\u03d3\7"+
		"Y\2\2\u03d3\u03d4\5`\61\2\u03d4\u03d5\7Z\2\2\u03d5\u03d6\5`\61\2\u03d6"+
		"\u0087\3\2\2\2\u03d7\u03d8\7V\2\2\u03d8\u03d9\7\b\2\2\u03d9\u03da\5^\60"+
		"\2\u03da\u03dc\7\t\2\2\u03db\u03dd\5\u008aF\2\u03dc\u03db\3\2\2\2\u03dd"+
		"\u03de\3\2\2\2\u03de\u03dc\3\2\2\2\u03de\u03df\3\2\2\2\u03df\u0089\3\2"+
		"\2\2\u03e0\u03e3\7W\2\2\u03e1\u03e4\7\f\2\2\u03e2\u03e4\5J&\2\u03e3\u03e1"+
		"\3\2\2\2\u03e3\u03e2\3\2\2\2\u03e4\u03ec\3\2\2\2\u03e5\u03e8\7\r\2\2\u03e6"+
		"\u03e9\7\f\2\2\u03e7\u03e9\5J&\2\u03e8\u03e6\3\2\2\2\u03e8\u03e7\3\2\2"+
		"\2\u03e9\u03eb\3\2\2\2\u03ea\u03e5\3\2\2\2\u03eb\u03ee\3\2\2\2\u03ec\u03ea"+
		"\3\2\2\2\u03ec\u03ed\3\2\2\2\u03ed\u03ef\3\2\2\2\u03ee\u03ec\3\2\2\2\u03ef"+
		"\u03f0\7\b\2\2\u03f0\u03f1\5^\60\2\u03f1\u03f2\7\t\2\2\u03f2\u008b\3\2"+
		"\2\2\u03f3\u03f8\5\u008eH\2\u03f4\u03f5\7\\\2\2\u03f5\u03f7\5\u008eH\2"+
		"\u03f6\u03f4\3\2\2\2\u03f7\u03fa\3\2\2\2\u03f8\u03f6\3\2\2\2\u03f8\u03f9"+
		"\3\2\2\2\u03f9\u008d\3\2\2\2\u03fa\u03f8\3\2\2\2\u03fb\u0400\5\u0090I"+
		"\2\u03fc\u03fd\7]\2\2\u03fd\u03ff\5\u0090I\2\u03fe\u03fc\3\2\2\2\u03ff"+
		"\u0402\3\2\2\2\u0400\u03fe\3\2\2\2\u0400\u0401\3\2\2\2\u0401\u008f\3\2"+
		"\2\2\u0402\u0400\3\2\2\2\u0403\u0405\7^\2\2\u0404\u0403\3\2\2\2\u0404"+
		"\u0405\3\2\2\2\u0405\u0406\3\2\2\2\u0406\u0407\5\u0092J\2\u0407\u0091"+
		"\3\2\2\2\u0408\u040b\5\u0094K\2\u0409\u040a\t\5\2\2\u040a\u040c\5\u0094"+
		"K\2\u040b\u0409\3\2\2\2\u040b\u040c\3\2\2\2\u040c\u0093\3\2\2\2\u040d"+
		"\u0412\5\u0096L\2\u040e\u040f\7.\2\2\u040f\u0411\5\u0096L\2\u0410\u040e"+
		"\3\2\2\2\u0411\u0414\3\2\2\2\u0412\u0410\3\2\2\2\u0412\u0413\3\2\2\2\u0413"+
		"\u0095\3\2\2\2\u0414\u0412\3\2\2\2\u0415\u0418\5\u0098M\2\u0416\u0417"+
		"\7_\2\2\u0417\u0419\5\u0098M\2\u0418\u0416\3\2\2\2\u0418\u0419\3\2\2\2"+
		"\u0419\u0097\3\2\2\2\u041a\u041f\5\u009aN\2\u041b\u041c\t\6\2\2\u041c"+
		"\u041e\5\u009aN\2\u041d\u041b\3\2\2\2\u041e\u0421\3\2\2\2\u041f\u041d"+
		"\3\2\2\2\u041f\u0420\3\2\2\2\u0420\u0099\3\2\2\2\u0421\u041f\3\2\2\2\u0422"+
		"\u0427\5\u009cO\2\u0423\u0424\t\7\2\2\u0424\u0426\5\u009cO\2\u0425\u0423"+
		"\3\2\2\2\u0426\u0429\3\2\2\2\u0427\u0425\3\2\2\2\u0427\u0428\3\2\2\2\u0428"+
		"\u009b\3\2\2\2\u0429\u0427\3\2\2\2\u042a\u042e\5\u009eP\2\u042b\u042c"+
		"\7`\2\2\u042c\u042d\7a\2\2\u042d\u042f\5\u0130\u0099\2\u042e\u042b\3\2"+
		"\2\2\u042e\u042f\3\2\2\2\u042f\u009d\3\2\2\2\u0430\u0434\5\u00a0Q\2\u0431"+
		"\u0432\7c\2\2\u0432\u0433\7b\2\2\u0433\u0435\5\u0130\u0099\2\u0434\u0431"+
		"\3\2\2\2\u0434\u0435\3\2\2\2\u0435\u009f\3\2\2\2\u0436\u043a\5\u00a2R"+
		"\2\u0437\u0438\7d\2\2\u0438\u0439\7F\2\2\u0439\u043b\5\u0130\u0099\2\u043a"+
		"\u0437\3\2\2\2\u043a\u043b\3\2\2\2\u043b\u00a1\3\2\2\2\u043c\u0440\5\u00a4"+
		"S\2\u043d\u043e\7f\2\2\u043e\u043f\7F\2\2\u043f\u0441\5\u013c\u009f\2"+
		"\u0440\u043d\3\2\2\2\u0440\u0441\3\2\2\2\u0441\u00a3\3\2\2\2\u0442\u0446"+
		"\5\u00a6T\2\u0443\u0444\7e\2\2\u0444\u0445\7F\2\2\u0445\u0447\5\u013c"+
		"\u009f\2\u0446\u0443\3\2\2\2\u0446\u0447\3\2\2\2\u0447\u00a5\3\2\2\2\u0448"+
		"\u0451\5\u00aaV\2\u0449\u044a\7\5\2\2\u044a\u044b\7,\2\2\u044b\u044c\3"+
		"\2\2\2\u044c\u044d\5\u00a8U\2\u044d\u044e\5\u00ceh\2\u044e\u0450\3\2\2"+
		"\2\u044f\u0449\3\2\2\2\u0450\u0453\3\2\2\2\u0451\u044f\3\2\2\2\u0451\u0452"+
		"\3\2\2\2\u0452\u00a7\3\2\2\2\u0453\u0451\3\2\2\2\u0454\u0458\5J&\2\u0455"+
		"\u0458\5\u00c2b\2\u0456\u0458\5\u00c4c\2\u0457\u0454\3\2\2\2\u0457\u0455"+
		"\3\2\2\2\u0457\u0456\3\2\2\2\u0458\u00a9\3\2\2\2\u0459\u045b\t\6\2\2\u045a"+
		"\u0459\3\2\2\2\u045b\u045e\3\2\2\2\u045c\u045a\3\2\2\2\u045c\u045d\3\2"+
		"\2\2\u045d\u045f\3\2\2\2\u045e\u045c\3\2\2\2\u045f\u0460\5\u00acW\2\u0460"+
		"\u00ab\3\2\2\2\u0461\u0465\5\u00b2Z\2\u0462\u0465\5\u00aeX\2\u0463\u0465"+
		"\5\u00b0Y\2\u0464\u0461\3\2\2\2\u0464\u0462\3\2\2\2\u0464\u0463\3\2\2"+
		"\2\u0465\u00ad\3\2\2\2\u0466\u0467\7m\2\2\u0467\u0468\7l\2\2\u0468\u0469"+
		"\5\u0130\u0099\2\u0469\u046a\7\b\2\2\u046a\u046b\5^\60\2\u046b\u046c\7"+
		"\t\2\2\u046c\u00af\3\2\2\2\u046d\u046e\7n\2\2\u046e\u046f\7l\2\2\u046f"+
		"\u0470\5\u0130\u0099\2\u0470\u0471\7\b\2\2\u0471\u0472\5^\60\2\u0472\u0473"+
		"\7\t\2\2\u0473\u00b1\3\2\2\2\u0474\u0479\5\u00e8u\2\u0475\u0476\7\64\2"+
		"\2\u0476\u0478\5\u00e8u\2\u0477\u0475\3\2\2\2\u0478\u047b\3\2\2\2\u0479"+
		"\u0477\3\2\2\2\u0479\u047a\3\2\2\2\u047a\u00b3\3\2\2\2\u047b\u0479\3\2"+
		"\2\2\u047c\u0484\5\u00be`\2\u047d\u0483\5\u00b6\\\2\u047e\u0483\5\u00ba"+
		"^\2\u047f\u0483\5\u00bc_\2\u0480\u0483\5\u00b8]\2\u0481\u0483\5\u00ce"+
		"h\2\u0482\u047d\3\2\2\2\u0482\u047e\3\2\2\2\u0482\u047f\3\2\2\2\u0482"+
		"\u0480\3\2\2\2\u0482\u0481\3\2\2\2\u0483\u0486\3\2\2\2\u0484\u0482\3\2"+
		"\2\2\u0484\u0485\3\2\2\2\u0485\u00b5\3\2\2\2\u0486\u0484\3\2\2\2\u0487"+
		"\u0488\7\65\2\2\u0488\u0489\7\65\2\2\u0489\u048a\5^\60\2\u048a\u048b\7"+
		"\66\2\2\u048b\u048c\7\66\2\2\u048c\u00b7\3\2\2\2\u048d\u048e\7\65\2\2"+
		"\u048e\u048f\7\66\2\2\u048f\u00b9\3\2\2\2\u0490\u0491\7\65\2\2\u0491\u0492"+
		"\5^\60\2\u0492\u0493\7\66\2\2\u0493\u00bb\3\2\2\2\u0494\u049b\7\67\2\2"+
		"\u0495\u049c\5\u0146\u00a4\2\u0496\u049c\5\u0144\u00a3\2\u0497\u049c\7"+
		"\u00b1\2\2\u0498\u049c\5\u00c4c\2\u0499\u049c\5\u00c2b\2\u049a\u049c\5"+
		"\u00c6d\2\u049b\u0495\3\2\2\2\u049b\u0496\3\2\2\2\u049b\u0497\3\2\2\2"+
		"\u049b\u0498\3\2\2\2\u049b\u0499\3\2\2\2\u049b\u049a\3\2\2\2\u049c\u00bd"+
		"\3\2\2\2\u049d\u04ad\7\u00aa\2\2\u049e\u04ad\7j\2\2\u049f\u04ad\7k\2\2"+
		"\u04a0\u04ad\7\u00ab\2\2\u04a1\u04ad\5\u0144\u00a3\2\u04a2\u04ad\5\u00c2"+
		"b\2\u04a3\u04ad\5\u00c4c\2\u04a4\u04ad\5\u00c6d\2\u04a5\u04ad\5\u0132"+
		"\u009a\2\u04a6\u04ad\5\u00ccg\2\u04a7\u04ad\5\u00c8e\2\u04a8\u04ad\5\u00ca"+
		"f\2\u04a9\u04ad\5\u0140\u00a1\2\u04aa\u04ad\5\u00d2j\2\u04ab\u04ad\5\u00c0"+
		"a\2\u04ac\u049d\3\2\2\2\u04ac\u049e\3\2\2\2\u04ac\u049f\3\2\2\2\u04ac"+
		"\u04a0\3\2\2\2\u04ac\u04a1\3\2\2\2\u04ac\u04a2\3\2\2\2\u04ac\u04a3\3\2"+
		"\2\2\u04ac\u04a4\3\2\2\2\u04ac\u04a5\3\2\2\2\u04ac\u04a6\3\2\2\2\u04ac"+
		"\u04a7\3\2\2\2\u04ac\u04a8\3\2\2\2\u04ac\u04a9\3\2\2\2\u04ac\u04aa\3\2"+
		"\2\2\u04ac\u04ab\3\2\2\2\u04ad\u00bf\3\2\2\2\u04ae\u04af\7\b\2\2\u04af"+
		"\u04b0\5\20\t\2\u04b0\u04b1\7\t\2\2\u04b1\u00c1\3\2\2\2\u04b2\u04b3\7"+
		"\6\2\2\u04b3\u04b4\5J&\2\u04b4\u00c3\3\2\2\2\u04b5\u04b7\7\n\2\2\u04b6"+
		"\u04b8\5^\60\2\u04b7\u04b6\3\2\2\2\u04b7\u04b8\3\2\2\2\u04b8\u04b9\3\2"+
		"\2\2\u04b9\u04ba\7\13\2\2\u04ba\u00c5\3\2\2\2\u04bb\u04bc\78\2\2\u04bc"+
		"\u00c7\3\2\2\2\u04bd\u04be\7\21\2\2\u04be\u04bf\7\b\2\2\u04bf\u04c0\5"+
		"^\60\2\u04c0\u04c1\7\t\2\2\u04c1\u00c9\3\2\2\2\u04c2\u04c3\7i\2\2\u04c3"+
		"\u04c4\7\b\2\2\u04c4\u04c5\5^\60\2\u04c5\u04c6\7\t\2\2\u04c6\u00cb\3\2"+
		"\2\2\u04c7\u04c8\5J&\2\u04c8\u04c9\5\u00ceh\2\u04c9\u00cd\3\2\2\2\u04ca"+
		"\u04d1\7\n\2\2\u04cb\u04cd\5\u00d0i\2\u04cc\u04ce\7\17\2\2\u04cd\u04cc"+
		"\3\2\2\2\u04cd\u04ce\3\2\2\2\u04ce\u04d0\3\2\2\2\u04cf\u04cb\3\2\2\2\u04d0"+
		"\u04d3\3\2\2\2\u04d1\u04cf\3\2\2\2\u04d1\u04d2\3\2\2\2\u04d2\u04d4\3\2"+
		"\2\2\u04d3\u04d1\3\2\2\2\u04d4\u04d5\7\13\2\2\u04d5\u00cf\3\2\2\2\u04d6"+
		"\u04d9\5`\61\2\u04d7\u04d9\7\u00a9\2\2\u04d8\u04d6\3\2\2\2\u04d8\u04d7"+
		"\3\2\2\2\u04d9\u00d1\3\2\2\2\u04da\u04dd\5\u00d4k\2\u04db\u04dd\5\u00d6"+
		"l\2\u04dc\u04da\3\2\2\2\u04dc\u04db\3\2\2\2\u04dd\u00d3\3\2\2\2\u04de"+
		"\u04df\5J&\2\u04df\u04e0\79\2\2\u04e0\u04e1\7\u00ab\2\2\u04e1\u00d5\3"+
		"\2\2\2\u04e2\u04e3\5\64\33\2\u04e3\u04e4\7\37\2\2\u04e4\u04e6\7\n\2\2"+
		"\u04e5\u04e7\5Z.\2\u04e6\u04e5\3\2\2\2\u04e6\u04e7\3\2\2\2\u04e7\u04e8"+
		"\3\2\2\2\u04e8\u04eb\7\13\2\2\u04e9\u04ea\7F\2\2\u04ea\u04ec\5\u0130\u0099"+
		"\2\u04eb\u04e9\3\2\2\2\u04eb\u04ec\3\2\2\2\u04ec\u04ed\3\2\2\2\u04ed\u04ee"+
		"\7\b\2\2\u04ee\u04ef\5\22\n\2\u04ef\u04f0\7\t\2\2\u04f0\u00d7\3\2\2\2"+
		"\u04f1\u04f2\7s\2\2\u04f2\u04f3\7~\2\2\u04f3\u04f4\5`\61\2\u04f4\u04f5"+
		"\7z\2\2\u04f5\u04f9\5`\61\2\u04f6\u04f7\7G\2\2\u04f7\u04f8\7}\2\2\u04f8"+
		"\u04fa\5`\61\2\u04f9\u04f6\3\2\2\2\u04f9\u04fa\3\2\2\2\u04fa\u0509\3\2"+
		"\2\2\u04fb\u04fc\7s\2\2\u04fc\u04fd\7~\2\2\u04fd\u0502\5\u013e\u00a0\2"+
		"\u04fe\u04ff\7\17\2\2\u04ff\u0501\5\u013e\u00a0\2\u0500\u04fe\3\2\2\2"+
		"\u0501\u0504\3\2\2\2\u0502\u0500\3\2\2\2\u0502\u0503\3\2\2\2\u0503\u0505"+
		"\3\2\2\2\u0504\u0502\3\2\2\2\u0505\u0506\7z\2\2\u0506\u0507\5`\61\2\u0507"+
		"\u0509\3\2\2\2\u0508\u04f1\3\2\2\2\u0508\u04fb\3\2\2\2\u0509\u00d9\3\2"+
		"\2\2\u050a\u050b\7t\2\2\u050b\u050c\7~\2\2\u050c\u050d\5\u00e4s\2\u050d"+
		"\u00db\3\2\2\2\u050e\u050f\7u\2\2\u050f\u0510\7~\2\2\u0510\u0511\5\u00e4"+
		"s\2\u0511\u0512\7F\2\2\u0512\u0513\5`\61\2\u0513\u00dd\3\2\2\2\u0514\u0515"+
		"\7v\2\2\u0515\u0516\7{\2\2\u0516\u0517\7a\2\2\u0517\u0518\7~\2\2\u0518"+
		"\u0519\5\u00e4s\2\u0519\u051a\7|\2\2\u051a\u051b\5`\61\2\u051b\u00df\3"+
		"\2\2\2\u051c\u051d\7w\2\2\u051d\u0522\5\u00e6t\2\u051e\u051f\7\17\2\2"+
		"\u051f\u0521\5\u00e6t\2\u0520\u051e\3\2\2\2\u0521\u0524\3\2\2\2\u0522"+
		"\u0520\3\2\2\2\u0522\u0523\3\2\2\2\u0523\u0525\3\2\2\2\u0524\u0522\3\2"+
		"\2\2\u0525\u0526\7x\2\2\u0526\u0527\5`\61\2\u0527\u0528\7C\2\2\u0528\u0529"+
		"\5`\61\2\u0529\u00e1\3\2\2\2\u052a\u052b\7y\2\2\u052b\u052c\7~\2\2\u052c"+
		"\u052d\5`\61\2\u052d\u052e\7z\2\2\u052e\u052f\5`\61\2\u052f\u00e3\3\2"+
		"\2\2\u0530\u0533\5\u00be`\2\u0531\u0534\5\u00b6\\\2\u0532\u0534\5\u00bc"+
		"_\2\u0533\u0531\3\2\2\2\u0533\u0532\3\2\2\2\u0534\u0535\3\2\2\2\u0535"+
		"\u0533\3\2\2\2\u0535\u0536\3\2\2\2\u0536\u00e5\3\2\2\2\u0537\u0538\5\u00c2"+
		"b\2\u0538\u0539\7\7\2\2\u0539\u053a\5`\61\2\u053a\u00e7\3\2\2\2\u053b"+
		"\u053d\7\u0084\2\2\u053c\u053e\5\u00eav\2\u053d\u053c\3\2\2\2\u053d\u053e"+
		"\3\2\2\2\u053e\u0543\3\2\2\2\u053f\u0540\7\u0085\2\2\u0540\u0543\5\u00ea"+
		"v\2\u0541\u0543\5\u00eav\2\u0542\u053b\3\2\2\2\u0542\u053f\3\2\2\2\u0542"+
		"\u0541\3\2\2\2\u0543\u00e9\3\2\2\2\u0544\u0549\5\u00ecw\2\u0545\u0546"+
		"\t\b\2\2\u0546\u0548\5\u00ecw\2\u0547\u0545\3\2\2\2\u0548\u054b\3\2\2"+
		"\2\u0549\u0547\3\2\2\2\u0549\u054a\3\2\2\2\u054a\u00eb\3\2\2\2\u054b\u0549"+
		"\3\2\2\2\u054c\u054f\5\u00b4[\2\u054d\u054f\5\u00eex\2\u054e\u054c\3\2"+
		"\2\2\u054e\u054d\3\2\2\2\u054f\u00ed\3\2\2\2\u0550\u0553\5\u00f6|\2\u0551"+
		"\u0553\5\u00f0y\2\u0552\u0550\3\2\2\2\u0552\u0551\3\2\2\2\u0553\u0554"+
		"\3\2\2\2\u0554\u0555\5\u0106\u0084\2\u0555\u00ef\3\2\2\2\u0556\u0557\5"+
		"\u00f2z\2\u0557\u0558\5\u00fc\177\2\u0558\u055b\3\2\2\2\u0559\u055b\5"+
		"\u00f4{\2\u055a\u0556\3\2\2\2\u055a\u0559\3\2\2\2\u055b\u00f1\3\2\2\2"+
		"\u055c\u055d\t\t\2\2\u055d\u055e\7\23\2\2\u055e\u055f\7\23\2\2\u055f\u00f3"+
		"\3\2\2\2\u0560\u0561\7\u0086\2\2\u0561\u0562\5\u00fc\177\2\u0562\u00f5"+
		"\3\2\2\2\u0563\u0564\5\u00f8}\2\u0564\u0565\5\u00fc\177\2\u0565\u0568"+
		"\3\2\2\2\u0566\u0568\5\u00fa~\2\u0567\u0563\3\2\2\2\u0567\u0566\3\2\2"+
		"\2\u0568\u00f7\3\2\2\2\u0569\u056a\t\n\2\2\u056a\u056b\7\23\2\2\u056b"+
		"\u056c\7\23\2\2\u056c\u00f9\3\2\2\2\u056d\u056e\7:\2\2\u056e\u00fb\3\2"+
		"\2\2\u056f\u0572\5\u00fe\u0080\2\u0570\u0572\5\u0108\u0085\2\u0571\u056f"+
		"\3\2\2\2\u0571\u0570\3\2\2\2\u0572\u00fd\3\2\2\2\u0573\u0576\5J&\2\u0574"+
		"\u0576\5\u0100\u0081\2\u0575\u0573\3\2\2\2\u0575\u0574\3\2\2\2\u0576\u00ff"+
		"\3\2\2\2\u0577\u057b\7\f\2\2\u0578\u057b\5\u0102\u0082\2\u0579\u057b\5"+
		"\u0104\u0083\2\u057a\u0577\3\2\2\2\u057a\u0578\3\2\2\2\u057a\u0579\3\2"+
		"\2\2\u057b\u0101\3\2\2\2\u057c\u057d\7\u00b1\2\2\u057d\u057e\7\23\2\2"+
		"\u057e\u057f\7\f\2\2\u057f\u0103\3\2\2\2\u0580\u0581\7\f\2\2\u0581\u0582"+
		"\7\23\2\2\u0582\u0583\7\u00b1\2\2\u0583\u0105\3\2\2\2\u0584\u0586\5\u00ba"+
		"^\2\u0585\u0584\3\2\2\2\u0586\u0589\3\2\2\2\u0587\u0585\3\2\2\2\u0587"+
		"\u0588\3\2\2\2\u0588\u0107\3\2\2\2\u0589\u0587\3\2\2\2\u058a\u0596\5\u010e"+
		"\u0088\2\u058b\u0596\5\u0120\u0091\2\u058c\u0596\5\u0118\u008d\2\u058d"+
		"\u0596\5\u0124\u0093\2\u058e\u0596\5\u011c\u008f\2\u058f\u0596\5\u0116"+
		"\u008c\2\u0590\u0596\5\u0112\u008a\2\u0591\u0596\5\u0110\u0089\2\u0592"+
		"\u0596\5\u0114\u008b\2\u0593\u0596\5\u010c\u0087\2\u0594\u0596\5\u010a"+
		"\u0086\2\u0595\u058a\3\2\2\2\u0595\u058b\3\2\2\2\u0595\u058c\3\2\2\2\u0595"+
		"\u058d\3\2\2\2\u0595\u058e\3\2\2\2\u0595\u058f\3\2\2\2\u0595\u0590\3\2"+
		"\2\2\u0595\u0591\3\2\2\2\u0595\u0592\3\2\2\2\u0595\u0593\3\2\2\2\u0595"+
		"\u0594\3\2\2\2\u0596\u0109\3\2\2\2\u0597\u0598\7\u0093\2\2\u0598\u0599"+
		"\7\n\2\2\u0599\u059a\7\13\2\2\u059a\u010b\3\2\2\2\u059b\u059c\7\u0094"+
		"\2\2\u059c\u059d\7\n\2\2\u059d\u059e\7\13\2\2\u059e\u010d\3\2\2\2\u059f"+
		"\u05a0\7\u0096\2\2\u05a0\u05a3\7\n\2\2\u05a1\u05a4\5\u0120\u0091\2\u05a2"+
		"\u05a4\5\u0124\u0093\2\u05a3\u05a1\3\2\2\2\u05a3\u05a2\3\2\2\2\u05a3\u05a4"+
		"\3\2\2\2\u05a4\u05a5\3\2\2\2\u05a5\u05a6\7\13\2\2\u05a6\u010f\3\2\2\2"+
		"\u05a7\u05a8\7\u0097\2\2\u05a8\u05a9\7\n\2\2\u05a9\u05aa\7\13\2\2\u05aa"+
		"\u0111\3\2\2\2\u05ab\u05ac\7\u00a1\2\2\u05ac\u05ad\7\n\2\2\u05ad\u05ae"+
		"\7\13\2\2\u05ae\u0113\3\2\2\2\u05af\u05b0\7\u0099\2\2\u05b0\u05b1\7\n"+
		"\2\2\u05b1\u05b2\7\13\2\2\u05b2\u0115\3\2\2\2\u05b3\u05b4\7\u0098\2\2"+
		"\u05b4\u05b7\7\n\2\2\u05b5\u05b8\7\u00b1\2\2\u05b6\u05b8\5\u0144\u00a3"+
		"\2\u05b7\u05b5\3\2\2\2\u05b7\u05b6\3\2\2\2\u05b7\u05b8\3\2\2\2\u05b8\u05b9"+
		"\3\2\2\2\u05b9\u05ba\7\13\2\2\u05ba\u0117\3\2\2\2\u05bb\u05bc\7\u0089"+
		"\2\2\u05bc\u05c2\7\n\2\2\u05bd\u05c0\5\u011a\u008e\2\u05be\u05bf\7\17"+
		"\2\2\u05bf\u05c1\5\u012e\u0098\2\u05c0\u05be\3\2\2\2\u05c0\u05c1\3\2\2"+
		"\2\u05c1\u05c3\3\2\2\2\u05c2\u05bd\3\2\2\2\u05c2\u05c3\3\2\2\2\u05c3\u05c4"+
		"\3\2\2\2\u05c4\u05c5\7\13\2\2\u05c5\u0119\3\2\2\2\u05c6\u05c9\5\u0128"+
		"\u0095\2\u05c7\u05c9\7\f\2\2\u05c8\u05c6\3\2\2\2\u05c8\u05c7\3\2\2\2\u05c9"+
		"\u011b\3\2\2\2\u05ca\u05cb\7\u009a\2\2\u05cb\u05cc\7\n\2\2\u05cc\u05cd"+
		"\5\u011e\u0090\2\u05cd\u05ce\7\13\2\2\u05ce\u011d\3\2\2\2\u05cf\u05d0"+
		"\5\u0128\u0095\2\u05d0\u011f\3\2\2\2\u05d1\u05d2\7\u0083\2\2\u05d2\u05db"+
		"\7\n\2\2\u05d3\u05d9\5\u0122\u0092\2\u05d4\u05d5\7\17\2\2\u05d5\u05d7"+
		"\5\u012e\u0098\2\u05d6\u05d8\7\u00a9\2\2\u05d7\u05d6\3\2\2\2\u05d7\u05d8"+
		"\3\2\2\2\u05d8\u05da\3\2\2\2\u05d9\u05d4\3\2\2\2\u05d9\u05da\3\2\2\2\u05da"+
		"\u05dc\3\2\2\2\u05db\u05d3\3\2\2\2\u05db\u05dc\3\2\2\2\u05dc\u05dd\3\2"+
		"\2\2\u05dd\u05de\7\13\2\2\u05de\u0121\3\2\2\2\u05df\u05e2\5\u012a\u0096"+
		"\2\u05e0\u05e2\7\f\2\2\u05e1\u05df\3\2\2\2\u05e1\u05e0\3\2\2\2\u05e2\u0123"+
		"\3\2\2\2\u05e3\u05e4\7\u009b\2\2\u05e4\u05e5\7\n\2\2\u05e5\u05e6\5\u0126"+
		"\u0094\2\u05e6\u05e7\7\13\2\2\u05e7\u0125\3\2\2\2\u05e8\u05e9\5\u012a"+
		"\u0096\2\u05e9\u0127\3\2\2\2\u05ea\u05eb\5J&\2\u05eb\u0129\3\2\2\2\u05ec"+
		"\u05ed\5J&\2\u05ed\u012b\3\2\2\2\u05ee\u05ef\5\u012e\u0098\2\u05ef\u012d"+
		"\3\2\2\2\u05f0\u05f1\5J&\2\u05f1\u012f\3\2\2\2\u05f2\u05f3\7\n\2\2\u05f3"+
		"\u05fb\7\13\2\2\u05f4\u05f8\5\u0134\u009b\2\u05f5\u05f9\7\u00a9\2\2\u05f6"+
		"\u05f9\7\f\2\2\u05f7\u05f9\7/\2\2\u05f8\u05f5\3\2\2\2\u05f8\u05f6\3\2"+
		"\2\2\u05f8\u05f7\3\2\2\2\u05f8\u05f9\3\2\2\2\u05f9\u05fb\3\2\2\2\u05fa"+
		"\u05f2\3\2\2\2\u05fa\u05f4\3\2\2\2\u05fb\u0131\3\2\2\2\u05fc\u0605\7\b"+
		"\2\2\u05fd\u0602\5\u013e\u00a0\2\u05fe\u05ff\7\17\2\2\u05ff\u0601\5\u013e"+
		"\u00a0\2\u0600\u05fe\3\2\2\2\u0601\u0604\3\2\2\2\u0602\u0600\3\2\2\2\u0602"+
		"\u0603\3\2\2\2\u0603\u0606\3\2\2\2\u0604\u0602\3\2\2\2\u0605\u05fd\3\2"+
		"\2\2\u0605\u0606\3\2\2\2\u0606\u0607\3\2\2\2\u0607\u060d\7\t\2\2\u0608"+
		"\u0609\7;\2\2\u0609\u060a\5^\60\2\u060a\u060b\7<\2\2\u060b\u060d\3\2\2"+
		"\2\u060c\u05fc\3\2\2\2\u060c\u0608\3\2\2\2\u060d\u0133\3\2\2\2\u060e\u0612"+
		"\5J&\2\u060f\u0612\7\u00aa\2\2\u0610\u0612\5\u0136\u009c\2\u0611\u060e"+
		"\3\2\2\2\u0611\u060f\3\2\2\2\u0611\u0610\3\2\2\2\u0612\u0135\3\2\2\2\u0613"+
		"\u0616\5\u0138\u009d\2\u0614\u0616\5\u013a\u009e\2\u0615\u0613\3\2\2\2"+
		"\u0615\u0614\3\2\2\2\u0616\u0137\3\2\2\2\u0617\u0618\7\37\2\2\u0618\u0619"+
		"\7\n\2\2\u0619\u061a\7\f\2\2\u061a\u061b\7\13\2\2\u061b\u0139\3\2\2\2"+
		"\u061c\u061d\7\37\2\2\u061d\u0626\7\n\2\2\u061e\u0623\5\u0130\u0099\2"+
		"\u061f\u0620\7\17\2\2\u0620\u0622\5\u0130\u0099\2\u0621\u061f\3\2\2\2"+
		"\u0622\u0625\3\2\2\2\u0623\u0621\3\2\2\2\u0623\u0624\3\2\2\2\u0624\u0627"+
		"\3\2\2\2\u0625\u0623\3\2\2\2\u0626\u061e\3\2\2\2\u0626\u0627\3\2\2\2\u0627"+
		"\u0628\3\2\2\2\u0628\u0629\7\13\2\2\u0629\u062a\7F\2\2\u062a\u062b\5\u0130"+
		"\u0099\2\u062b\u013b\3\2\2\2\u062c\u062e\5\u0134\u009b\2\u062d\u062f\7"+
		"\u00a9\2\2\u062e\u062d\3\2\2\2\u062e\u062f\3\2\2\2\u062f\u013d\3\2\2\2"+
		"\u0630\u0633\5`\61\2\u0631\u0633\7\u00b1\2\2\u0632\u0630\3\2\2\2\u0632"+
		"\u0631\3\2\2\2\u0633\u0634\3\2\2\2\u0634\u0635\t\13\2\2\u0635\u0636\5"+
		"`\61\2\u0636\u013f\3\2\2\2\u0637\u0639\7\65\2\2\u0638\u063a\5^\60\2\u0639"+
		"\u0638\3\2\2\2\u0639\u063a\3\2\2\2\u063a\u063b\3\2\2\2\u063b\u063c\7\66"+
		"\2\2\u063c\u0141\3\2\2\2\u063d\u063e\5\u0144\u00a3\2\u063e\u0143\3\2\2"+
		"\2\u063f\u0640\7\u00a8\2\2\u0640\u0145\3\2\2\2\u0641\u0642\t\f\2\2\u0642"+
		"\u0147\3\2\2\2\u009c\u0150\u0154\u0164\u016a\u0172\u017a\u0182\u0191\u01af"+
		"\u01b7\u01b9\u01cf\u01d9\u01e3\u01e8\u01ed\u01f1\u01fd\u0201\u020a\u0211"+
		"\u021f\u0223\u0226\u022b\u0235\u023d\u0241\u024d\u0259\u026f\u0277\u027c"+
		"\u027f\u0283\u028c\u0295\u0298\u02a0\u02a7\u02a9\u02b0\u02b7\u02b9\u02c1"+
		"\u02c6\u02cd\u02d4\u02de\u02e5\u02ec\u02f3\u02fc\u0306\u030a\u0312\u0314"+
		"\u0320\u0326\u032a\u032e\u0339\u033f\u034e\u0354\u0358\u035c\u0363\u036a"+
		"\u0370\u0375\u0377\u037b\u0382\u0389\u0392\u039e\u03a8\u03b4\u03b8\u03c1"+
		"\u03c8\u03de\u03e3\u03e8\u03ec\u03f8\u0400\u0404\u040b\u0412\u0418\u041f"+
		"\u0427\u042e\u0434\u043a\u0440\u0446\u0451\u0457\u045c\u0464\u0479\u0482"+
		"\u0484\u049b\u04ac\u04b7\u04cd\u04d1\u04d8\u04dc\u04e6\u04eb\u04f9\u0502"+
		"\u0508\u0522\u0533\u0535\u053d\u0542\u0549\u054e\u0552\u055a\u0567\u0571"+
		"\u0575\u057a\u0587\u0595\u05a3\u05b7\u05c0\u05c2\u05c8\u05d7\u05d9\u05db"+
		"\u05e1\u05f8\u05fa\u0602\u0605\u060c\u0611\u0615\u0623\u0626\u062e\u0632"+
		"\u0639";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}