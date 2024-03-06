// Generated from /Users/dbuzatu/ETH/Thesis/rumble/src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.13.1

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
public class JsoniqParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

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
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		Kfor=60, Klet=61, Kwhere=62, Kgroup=63, Kby=64, Korder=65, Kreturn=66, 
		Kif=67, Kin=68, Kas=69, Kat=70, Kallowing=71, Kempty=72, Kcount=73, Kstable=74, 
		Kascending=75, Kdescending=76, Ksome=77, Kevery=78, Ksatisfies=79, Kcollation=80, 
		Kgreatest=81, Kleast=82, Kswitch=83, Kcase=84, Ktry=85, Kcatch=86, Kdefault=87, 
		Kthen=88, Kelse=89, Ktypeswitch=90, Kor=91, Kand=92, Knot=93, Kto=94, 
		Kinstance=95, Kof=96, Kstatically=97, Kis=98, Ktreat=99, Kcast=100, Kcastable=101, 
		Kversion=102, Kjsoniq=103, Kunordered=104, Ktrue=105, Kfalse=106, Ktype=107, 
		Kvalidate=108, Kannotate=109, Kdeclare=110, Kcontext=111, Kitem=112, Kvariable=113, 
		Kinsert=114, Kdelete=115, Krename=116, Kreplace=117, Kcopy=118, Kmodify=119, 
		Kappend=120, Kinto=121, Kvalue=122, Kjson=123, Kwith=124, Kposition=125, 
		STRING=126, ArgumentPlaceholder=127, NullLiteral=128, Literal=129, NumericLiteral=130, 
		IntegerLiteral=131, DecimalLiteral=132, DoubleLiteral=133, WS=134, NCName=135, 
		XQComment=136, ContentChar=137, Kbreak=138, Kloop=139, Kcontinue=140, 
		Kexit=141, Kreturning=142, Kwhile=143;
	public static final int
		RULE_moduleAndThisIsIt = 0, RULE_module = 1, RULE_mainModule = 2, RULE_libraryModule = 3, 
		RULE_prolog = 4, RULE_program = 5, RULE_setter = 6, RULE_namespaceDecl = 7, 
		RULE_annotatedDecl = 8, RULE_defaultCollationDecl = 9, RULE_orderingModeDecl = 10, 
		RULE_emptyOrderDecl = 11, RULE_decimalFormatDecl = 12, RULE_qname = 13, 
		RULE_dfPropertyName = 14, RULE_moduleImport = 15, RULE_varDecl = 16, RULE_contextItemDecl = 17, 
		RULE_functionDecl = 18, RULE_typeDecl = 19, RULE_schemaLanguage = 20, 
		RULE_paramList = 21, RULE_param = 22, RULE_expr = 23, RULE_exprSingle = 24, 
		RULE_exprSimple = 25, RULE_flowrExpr = 26, RULE_initialClause = 27, RULE_intermediateClause = 28, 
		RULE_forClause = 29, RULE_forVar = 30, RULE_letClause = 31, RULE_letVar = 32, 
		RULE_whereClause = 33, RULE_groupByClause = 34, RULE_groupByVar = 35, 
		RULE_orderByClause = 36, RULE_orderByExpr = 37, RULE_countClause = 38, 
		RULE_quantifiedExpr = 39, RULE_quantifiedExprVar = 40, RULE_switchExpr = 41, 
		RULE_switchCaseClause = 42, RULE_typeSwitchExpr = 43, RULE_caseClause = 44, 
		RULE_ifExpr = 45, RULE_tryCatchExpr = 46, RULE_catchClause = 47, RULE_switchCaseOperand = 48, 
		RULE_catchErrorList = 49, RULE_orExpr = 50, RULE_andExpr = 51, RULE_notExpr = 52, 
		RULE_comparisonExpr = 53, RULE_stringConcatExpr = 54, RULE_rangeExpr = 55, 
		RULE_additiveExpr = 56, RULE_multiplicativeExpr = 57, RULE_instanceOfExpr = 58, 
		RULE_isStaticallyExpr = 59, RULE_treatExpr = 60, RULE_castableExpr = 61, 
		RULE_castExpr = 62, RULE_arrowExpr = 63, RULE_arrowFunctionSpecifier = 64, 
		RULE_unaryExpr = 65, RULE_valueExpr = 66, RULE_validateExpr = 67, RULE_annotateExpr = 68, 
		RULE_simpleMapExpr = 69, RULE_postFixExpr = 70, RULE_arrayLookup = 71, 
		RULE_arrayUnboxing = 72, RULE_predicate = 73, RULE_objectLookup = 74, 
		RULE_primaryExpr = 75, RULE_blockExpr = 76, RULE_varRef = 77, RULE_parenthesizedExpr = 78, 
		RULE_contextItemExpr = 79, RULE_orderedExpr = 80, RULE_unorderedExpr = 81, 
		RULE_functionCall = 82, RULE_argumentList = 83, RULE_argument = 84, RULE_functionItemExpr = 85, 
		RULE_namedFunctionRef = 86, RULE_inlineFunctionExpr = 87, RULE_insertExpr = 88, 
		RULE_deleteExpr = 89, RULE_renameExpr = 90, RULE_replaceExpr = 91, RULE_transformExpr = 92, 
		RULE_appendExpr = 93, RULE_updateLocator = 94, RULE_copyDecl = 95, RULE_statements = 96, 
		RULE_statementsAndExpr = 97, RULE_statementsAndOptionalExpr = 98, RULE_statement = 99, 
		RULE_applyStatement = 100, RULE_assignStatement = 101, RULE_blockStatement = 102, 
		RULE_breakStatement = 103, RULE_continueStatement = 104, RULE_exitStatement = 105, 
		RULE_flworStatement = 106, RULE_returnStatement = 107, RULE_ifStatement = 108, 
		RULE_switchStatement = 109, RULE_switchCaseStatement = 110, RULE_tryCatchStatement = 111, 
		RULE_typeswitchStatement = 112, RULE_caseStatement = 113, RULE_typeDeclaration = 114, 
		RULE_varDeclStatement = 115, RULE_whileStatement = 116, RULE_sequenceType = 117, 
		RULE_objectConstructor = 118, RULE_itemType = 119, RULE_functionTest = 120, 
		RULE_anyFunctionTest = 121, RULE_typedFunctionTest = 122, RULE_singleType = 123, 
		RULE_pairConstructor = 124, RULE_arrayConstructor = 125, RULE_uriLiteral = 126, 
		RULE_stringLiteral = 127, RULE_keyWords = 128;
	private static String[] makeRuleNames() {
		return new String[] {
			"moduleAndThisIsIt", "module", "mainModule", "libraryModule", "prolog", 
			"program", "setter", "namespaceDecl", "annotatedDecl", "defaultCollationDecl", 
			"orderingModeDecl", "emptyOrderDecl", "decimalFormatDecl", "qname", "dfPropertyName", 
			"moduleImport", "varDecl", "contextItemDecl", "functionDecl", "typeDecl", 
			"schemaLanguage", "paramList", "param", "expr", "exprSingle", "exprSimple", 
			"flowrExpr", "initialClause", "intermediateClause", "forClause", "forVar", 
			"letClause", "letVar", "whereClause", "groupByClause", "groupByVar", 
			"orderByClause", "orderByExpr", "countClause", "quantifiedExpr", "quantifiedExprVar", 
			"switchExpr", "switchCaseClause", "typeSwitchExpr", "caseClause", "ifExpr", 
			"tryCatchExpr", "catchClause", "switchCaseOperand", "catchErrorList", 
			"orExpr", "andExpr", "notExpr", "comparisonExpr", "stringConcatExpr", 
			"rangeExpr", "additiveExpr", "multiplicativeExpr", "instanceOfExpr", 
			"isStaticallyExpr", "treatExpr", "castableExpr", "castExpr", "arrowExpr", 
			"arrowFunctionSpecifier", "unaryExpr", "valueExpr", "validateExpr", "annotateExpr", 
			"simpleMapExpr", "postFixExpr", "arrayLookup", "arrayUnboxing", "predicate", 
			"objectLookup", "primaryExpr", "blockExpr", "varRef", "parenthesizedExpr", 
			"contextItemExpr", "orderedExpr", "unorderedExpr", "functionCall", "argumentList", 
			"argument", "functionItemExpr", "namedFunctionRef", "inlineFunctionExpr", 
			"insertExpr", "deleteExpr", "renameExpr", "replaceExpr", "transformExpr", 
			"appendExpr", "updateLocator", "copyDecl", "statements", "statementsAndExpr", 
			"statementsAndOptionalExpr", "statement", "applyStatement", "assignStatement", 
			"blockStatement", "breakStatement", "continueStatement", "exitStatement", 
			"flworStatement", "returnStatement", "ifStatement", "switchStatement", 
			"switchCaseStatement", "tryCatchStatement", "typeswitchStatement", "caseStatement", 
			"typeDeclaration", "varDeclStatement", "whileStatement", "sequenceType", 
			"objectConstructor", "itemType", "functionTest", "anyFunctionTest", "typedFunctionTest", 
			"singleType", "pairConstructor", "arrayConstructor", "uriLiteral", "stringLiteral", 
			"keyWords"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'module'", "'namespace'", "'='", "'ordering'", "'ordered'", 
			"'decimal-format'", "':'", "'decimal-separator'", "'grouping-separator'", 
			"'infinity'", "'minus-sign'", "'NaN'", "'percent'", "'per-mille'", "'zero-digit'", 
			"'digit'", "'pattern-separator'", "'import'", "','", "':='", "'external'", 
			"'function'", "'('", "')'", "'{'", "'}'", "'jsound'", "'compact'", "'verbose'", 
			"'schema'", "'$'", "'|'", "'*'", "'eq'", "'ne'", "'lt'", "'le'", "'gt'", 
			"'ge'", "'!='", "'<'", "'<='", "'>'", "'>='", "'||'", "'+'", "'-'", "'div'", 
			"'idiv'", "'mod'", "'!'", "'['", "']'", "'.'", "'$$'", "'#'", "'{|'", 
			"'|}'", "'for'", "'let'", "'where'", "'group'", "'by'", "'order'", "'return'", 
			"'if'", "'in'", "'as'", "'at'", "'allowing'", "'empty'", "'count'", "'stable'", 
			"'ascending'", "'descending'", "'some'", "'every'", "'satisfies'", "'collation'", 
			"'greatest'", "'least'", "'switch'", "'case'", "'try'", "'catch'", "'default'", 
			"'then'", "'else'", "'typeswitch'", "'or'", "'and'", "'not'", "'to'", 
			"'instance'", "'of'", "'statically'", "'is'", "'treat'", "'cast'", "'castable'", 
			"'version'", "'jsoniq'", "'unordered'", "'true'", "'false'", "'type'", 
			"'validate'", "'annotate'", "'declare'", "'context'", "'item'", "'variable'", 
			"'insert'", "'delete'", "'rename'", "'replace'", "'copy'", "'modify'", 
			"'append'", "'into'", "'value'", "'json'", "'with'", "'position'", null, 
			"'?'", "'null'", null, null, null, null, null, null, null, null, null, 
			"'break'", "'loop'", "'continue'", "'exit'", "'returning'", "'while'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"Kfor", "Klet", "Kwhere", "Kgroup", "Kby", "Korder", "Kreturn", "Kif", 
			"Kin", "Kas", "Kat", "Kallowing", "Kempty", "Kcount", "Kstable", "Kascending", 
			"Kdescending", "Ksome", "Kevery", "Ksatisfies", "Kcollation", "Kgreatest", 
			"Kleast", "Kswitch", "Kcase", "Ktry", "Kcatch", "Kdefault", "Kthen", 
			"Kelse", "Ktypeswitch", "Kor", "Kand", "Knot", "Kto", "Kinstance", "Kof", 
			"Kstatically", "Kis", "Ktreat", "Kcast", "Kcastable", "Kversion", "Kjsoniq", 
			"Kunordered", "Ktrue", "Kfalse", "Ktype", "Kvalidate", "Kannotate", "Kdeclare", 
			"Kcontext", "Kitem", "Kvariable", "Kinsert", "Kdelete", "Krename", "Kreplace", 
			"Kcopy", "Kmodify", "Kappend", "Kinto", "Kvalue", "Kjson", "Kwith", "Kposition", 
			"STRING", "ArgumentPlaceholder", "NullLiteral", "Literal", "NumericLiteral", 
			"IntegerLiteral", "DecimalLiteral", "DoubleLiteral", "WS", "NCName", 
			"XQComment", "ContentChar", "Kbreak", "Kloop", "Kcontinue", "Kexit", 
			"Kreturning", "Kwhile"
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterModuleAndThisIsIt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitModuleAndThisIsIt(this);
		}
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
			setState(258);
			module();
			setState(259);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitModule(this);
		}
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
			setState(266);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(261);
				match(Kjsoniq);
				setState(262);
				match(Kversion);
				setState(263);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(264);
				match(T__0);
				}
				break;
			}
			setState(270);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(268);
				libraryModule();
				}
				break;
			case EOF:
			case T__5:
			case T__18:
			case T__22:
			case T__23:
			case T__25:
			case T__31:
			case T__46:
			case T__47:
			case T__52:
			case T__55:
			case T__57:
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
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
			case Kbreak:
			case Kcontinue:
			case Kexit:
			case Kwhile:
				{
				setState(269);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterMainModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitMainModule(this);
		}
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
			setState(272);
			prolog();
			setState(273);
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
	public static class LibraryModuleContext extends ParserRuleContext {
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterLibraryModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitLibraryModule(this);
		}
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
			setState(275);
			match(T__1);
			setState(276);
			match(T__2);
			setState(277);
			match(NCName);
			setState(278);
			match(T__3);
			setState(279);
			uriLiteral();
			setState(280);
			match(T__0);
			setState(281);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterProlog(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitProlog(this);
		}
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
			setState(292);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(286);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(283);
						setter();
						}
						break;
					case 2:
						{
						setState(284);
						namespaceDecl();
						}
						break;
					case 3:
						{
						setState(285);
						moduleImport();
						}
						break;
					}
					setState(288);
					match(T__0);
					}
					} 
				}
				setState(294);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(300);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(295);
					annotatedDecl();
					setState(296);
					match(T__0);
					}
					} 
				}
				setState(302);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitProgram(this);
		}
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
			setState(303);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSetter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSetter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSetter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetterContext setter() throws RecognitionException {
		SetterContext _localctx = new SetterContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_setter);
		try {
			setState(309);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(305);
				defaultCollationDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(306);
				orderingModeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(307);
				emptyOrderDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(308);
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
	public static class NamespaceDeclContext extends ParserRuleContext {
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public UriLiteralContext uriLiteral() {
			return getRuleContext(UriLiteralContext.class,0);
		}
		public NamespaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterNamespaceDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitNamespaceDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNamespaceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclContext namespaceDecl() throws RecognitionException {
		NamespaceDeclContext _localctx = new NamespaceDeclContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_namespaceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			match(Kdeclare);
			setState(312);
			match(T__2);
			setState(313);
			match(NCName);
			setState(314);
			match(T__3);
			setState(315);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterAnnotatedDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitAnnotatedDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnnotatedDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotatedDeclContext annotatedDecl() throws RecognitionException {
		AnnotatedDeclContext _localctx = new AnnotatedDeclContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_annotatedDecl);
		try {
			setState(321);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(317);
				functionDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(318);
				varDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(319);
				typeDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(320);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterDefaultCollationDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitDefaultCollationDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDefaultCollationDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultCollationDeclContext defaultCollationDecl() throws RecognitionException {
		DefaultCollationDeclContext _localctx = new DefaultCollationDeclContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323);
			match(Kdeclare);
			setState(324);
			match(Kdefault);
			setState(325);
			match(Kcollation);
			setState(326);
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
	public static class OrderingModeDeclContext extends ParserRuleContext {
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
		public TerminalNode Kunordered() { return getToken(JsoniqParser.Kunordered, 0); }
		public OrderingModeDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderingModeDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterOrderingModeDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitOrderingModeDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderingModeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderingModeDeclContext orderingModeDecl() throws RecognitionException {
		OrderingModeDeclContext _localctx = new OrderingModeDeclContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			match(Kdeclare);
			setState(329);
			match(T__4);
			setState(330);
			_la = _input.LA(1);
			if ( !(_la==T__5 || _la==Kunordered) ) {
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterEmptyOrderDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitEmptyOrderDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitEmptyOrderDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyOrderDeclContext emptyOrderDecl() throws RecognitionException {
		EmptyOrderDeclContext _localctx = new EmptyOrderDeclContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(Kdeclare);
			setState(333);
			match(Kdefault);
			setState(334);
			match(Korder);
			setState(335);
			match(Kempty);
			{
			setState(336);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterDecimalFormatDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitDecimalFormatDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDecimalFormatDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalFormatDeclContext decimalFormatDecl() throws RecognitionException {
		DecimalFormatDeclContext _localctx = new DecimalFormatDeclContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			match(Kdeclare);
			setState(343);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				{
				{
				setState(339);
				match(T__6);
				setState(340);
				qname();
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(341);
				match(Kdefault);
				setState(342);
				match(T__6);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(351);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 523776L) != 0)) {
				{
				{
				setState(345);
				dfPropertyName();
				setState(346);
				match(T__3);
				setState(347);
				stringLiteral();
				}
				}
				setState(353);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterQname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitQname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QnameContext qname() throws RecognitionException {
		QnameContext _localctx = new QnameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(356);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(354);
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
				case NullLiteral:
					{
					setState(355);
					((QnameContext)_localctx).nskw = keyWords();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(358);
				match(T__7);
				}
				break;
			}
			setState(363);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				{
				setState(361);
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
			case NullLiteral:
				{
				setState(362);
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

	@SuppressWarnings("CheckReturnValue")
	public static class DfPropertyNameContext extends ParserRuleContext {
		public DfPropertyNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dfPropertyName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterDfPropertyName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitDfPropertyName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDfPropertyName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DfPropertyNameContext dfPropertyName() throws RecognitionException {
		DfPropertyNameContext _localctx = new DfPropertyNameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(365);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 523776L) != 0)) ) {
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
	public static class ModuleImportContext extends ParserRuleContext {
		public Token prefix;
		public UriLiteralContext targetNamespace;
		public List<UriLiteralContext> uriLiteral() {
			return getRuleContexts(UriLiteralContext.class);
		}
		public UriLiteralContext uriLiteral(int i) {
			return getRuleContext(UriLiteralContext.class,i);
		}
		public TerminalNode Kat() { return getToken(JsoniqParser.Kat, 0); }
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public ModuleImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_moduleImport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterModuleImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitModuleImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitModuleImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleImportContext moduleImport() throws RecognitionException {
		ModuleImportContext _localctx = new ModuleImportContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			match(T__18);
			setState(368);
			match(T__1);
			setState(372);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(369);
				match(T__2);
				setState(370);
				((ModuleImportContext)_localctx).prefix = match(NCName);
				setState(371);
				match(T__3);
				}
			}

			setState(374);
			((ModuleImportContext)_localctx).targetNamespace = uriLiteral();
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(375);
				match(Kat);
				setState(376);
				uriLiteral();
				setState(381);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(377);
					match(T__19);
					setState(378);
					uriLiteral();
					}
					}
					setState(383);
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
	public static class VarDeclContext extends ParserRuleContext {
		public Token external;
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterVarDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitVarDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(386);
			match(Kdeclare);
			setState(387);
			match(Kvariable);
			setState(388);
			varRef();
			setState(391);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(389);
				match(Kas);
				setState(390);
				sequenceType();
				}
			}

			setState(400);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				{
				setState(393);
				match(T__20);
				setState(394);
				exprSingle();
				}
				}
				break;
			case T__21:
				{
				{
				setState(395);
				((VarDeclContext)_localctx).external = match(T__21);
				setState(398);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__20) {
					{
					setState(396);
					match(T__20);
					setState(397);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterContextItemDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitContextItemDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContextItemDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemDeclContext contextItemDecl() throws RecognitionException {
		ContextItemDeclContext _localctx = new ContextItemDeclContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_contextItemDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			match(Kdeclare);
			setState(403);
			match(Kcontext);
			setState(404);
			match(Kitem);
			setState(407);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(405);
				match(Kas);
				setState(406);
				sequenceType();
				}
			}

			setState(416);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				{
				setState(409);
				match(T__20);
				setState(410);
				exprSingle();
				}
				}
				break;
			case T__21:
				{
				{
				setState(411);
				((ContextItemDeclContext)_localctx).external = match(T__21);
				setState(414);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__20) {
					{
					setState(412);
					match(T__20);
					setState(413);
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

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDeclContext extends ParserRuleContext {
		public QnameContext fn_name;
		public SequenceTypeContext return_type;
		public StatementsAndOptionalExprContext fn_body;
		public TerminalNode Kdeclare() { return getToken(JsoniqParser.Kdeclare, 0); }
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterFunctionDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitFunctionDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			match(Kdeclare);
			setState(419);
			match(T__22);
			setState(420);
			((FunctionDeclContext)_localctx).fn_name = qname();
			setState(421);
			match(T__23);
			setState(423);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(422);
				paramList();
				}
			}

			setState(425);
			match(T__24);
			setState(428);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(426);
				match(Kas);
				setState(427);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(436);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__25:
				{
				setState(430);
				match(T__25);
				setState(432);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(431);
					((FunctionDeclContext)_localctx).fn_body = statementsAndOptionalExpr();
					}
					break;
				}
				setState(434);
				match(T__26);
				}
				break;
			case T__21:
				{
				setState(435);
				match(T__21);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTypeDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTypeDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclContext typeDecl() throws RecognitionException {
		TypeDeclContext _localctx = new TypeDeclContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_typeDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
			match(Kdeclare);
			setState(439);
			match(Ktype);
			setState(440);
			((TypeDeclContext)_localctx).type_name = qname();
			setState(441);
			match(Kas);
			setState(443);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(442);
				((TypeDeclContext)_localctx).schema = schemaLanguage();
				}
				break;
			}
			setState(445);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SchemaLanguageContext extends ParserRuleContext {
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public SchemaLanguageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaLanguage; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSchemaLanguage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSchemaLanguage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSchemaLanguage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaLanguageContext schemaLanguage() throws RecognitionException {
		SchemaLanguageContext _localctx = new SchemaLanguageContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_schemaLanguage);
		try {
			setState(453);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(447);
				match(T__27);
				setState(448);
				match(T__28);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(449);
				match(T__27);
				setState(450);
				match(T__29);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(451);
				match(Kjson);
				setState(452);
				match(T__30);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitParamList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(455);
			param();
			setState(460);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(456);
				match(T__19);
				setState(457);
				param();
				}
				}
				setState(462);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			match(T__31);
			setState(464);
			qname();
			setState(467);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(465);
				match(Kas);
				setState(466);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
			exprSingle();
			setState(474);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(470);
				match(T__19);
				setState(471);
				exprSingle();
				}
				}
				setState(476);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterExprSingle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitExprSingle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExprSingle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSingleContext exprSingle() throws RecognitionException {
		ExprSingleContext _localctx = new ExprSingleContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_exprSingle);
		try {
			setState(483);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(477);
				exprSimple();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(478);
				flowrExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(479);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(480);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(481);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(482);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterExprSimple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitExprSimple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExprSimple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSimpleContext exprSimple() throws RecognitionException {
		ExprSimpleContext _localctx = new ExprSimpleContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_exprSimple);
		try {
			setState(493);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(485);
				quantifiedExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(486);
				orExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(487);
				insertExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(488);
				deleteExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(489);
				renameExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(490);
				replaceExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(491);
				transformExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(492);
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

	@SuppressWarnings("CheckReturnValue")
	public static class FlowrExprContext extends ParserRuleContext {
		public ForClauseContext start_for;
		public LetClauseContext start_let;
		public ExprSingleContext return_expr;
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public ForClauseContext forClause() {
			return getRuleContext(ForClauseContext.class,0);
		}
		public LetClauseContext letClause() {
			return getRuleContext(LetClauseContext.class,0);
		}
		public List<IntermediateClauseContext> intermediateClause() {
			return getRuleContexts(IntermediateClauseContext.class);
		}
		public IntermediateClauseContext intermediateClause(int i) {
			return getRuleContext(IntermediateClauseContext.class,i);
		}
		public FlowrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowrExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterFlowrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitFlowrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFlowrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowrExprContext flowrExpr() throws RecognitionException {
		FlowrExprContext _localctx = new FlowrExprContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(495);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(496);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(502);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & 24623L) != 0)) {
				{
				{
				setState(499);
				intermediateClause();
				}
				}
				setState(504);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(505);
			match(Kreturn);
			setState(506);
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

	@SuppressWarnings("CheckReturnValue")
	public static class InitialClauseContext extends ParserRuleContext {
		public ForClauseContext forClause() {
			return getRuleContext(ForClauseContext.class,0);
		}
		public LetClauseContext letClause() {
			return getRuleContext(LetClauseContext.class,0);
		}
		public InitialClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initialClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterInitialClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitInitialClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInitialClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitialClauseContext initialClause() throws RecognitionException {
		InitialClauseContext _localctx = new InitialClauseContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_initialClause);
		try {
			setState(510);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				enterOuterAlt(_localctx, 1);
				{
				setState(508);
				forClause();
				}
				break;
			case Klet:
				enterOuterAlt(_localctx, 2);
				{
				setState(509);
				letClause();
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterIntermediateClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitIntermediateClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIntermediateClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntermediateClauseContext intermediateClause() throws RecognitionException {
		IntermediateClauseContext _localctx = new IntermediateClauseContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_intermediateClause);
		try {
			setState(517);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
			case Klet:
				enterOuterAlt(_localctx, 1);
				{
				setState(512);
				initialClause();
				}
				break;
			case Kwhere:
				enterOuterAlt(_localctx, 2);
				{
				setState(513);
				whereClause();
				}
				break;
			case Kgroup:
				enterOuterAlt(_localctx, 3);
				{
				setState(514);
				groupByClause();
				}
				break;
			case Korder:
			case Kstable:
				enterOuterAlt(_localctx, 4);
				{
				setState(515);
				orderByClause();
				}
				break;
			case Kcount:
				enterOuterAlt(_localctx, 5);
				{
				setState(516);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterForClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitForClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitForClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForClauseContext forClause() throws RecognitionException {
		ForClauseContext _localctx = new ForClauseContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			match(Kfor);
			setState(520);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(525);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(521);
				match(T__19);
				setState(522);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(527);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterForVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitForVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitForVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForVarContext forVar() throws RecognitionException {
		ForVarContext _localctx = new ForVarContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(529);
				match(Kas);
				setState(530);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(535);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(533);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(534);
				match(Kempty);
				}
			}

			setState(539);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(537);
				match(Kat);
				setState(538);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(541);
			match(Kin);
			setState(542);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterLetClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitLetClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitLetClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetClauseContext letClause() throws RecognitionException {
		LetClauseContext _localctx = new LetClauseContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(544);
			match(Klet);
			setState(545);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(550);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(546);
				match(T__19);
				setState(547);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(552);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterLetVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitLetVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitLetVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetVarContext letVar() throws RecognitionException {
		LetVarContext _localctx = new LetVarContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(553);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(556);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(554);
				match(Kas);
				setState(555);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(558);
			match(T__20);
			setState(559);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitWhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(561);
			match(Kwhere);
			setState(562);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterGroupByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitGroupByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitGroupByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByClauseContext groupByClause() throws RecognitionException {
		GroupByClauseContext _localctx = new GroupByClauseContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(564);
			match(Kgroup);
			setState(565);
			match(Kby);
			setState(566);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(571);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(567);
				match(T__19);
				setState(568);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(573);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterGroupByVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitGroupByVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitGroupByVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByVarContext groupByVar() throws RecognitionException {
		GroupByVarContext _localctx = new GroupByVarContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(574);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(581);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__20 || _la==Kas) {
				{
				setState(577);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(575);
					match(Kas);
					setState(576);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(579);
				((GroupByVarContext)_localctx).decl = match(T__20);
				setState(580);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(585);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(583);
				match(Kcollation);
				setState(584);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterOrderByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitOrderByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(592);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(587);
				match(Korder);
				setState(588);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(589);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(590);
				match(Korder);
				setState(591);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(594);
			orderByExpr();
			setState(599);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(595);
				match(T__19);
				setState(596);
				orderByExpr();
				}
				}
				setState(601);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterOrderByExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitOrderByExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderByExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByExprContext orderByExpr() throws RecognitionException {
		OrderByExprContext _localctx = new OrderByExprContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(602);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(605);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(603);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(604);
				((OrderByExprContext)_localctx).desc = match(Kdescending);
				}
				break;
			case T__19:
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
			setState(612);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(607);
				match(Kempty);
				setState(610);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(608);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(609);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(616);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(614);
				match(Kcollation);
				setState(615);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterCountClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitCountClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCountClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountClauseContext countClause() throws RecognitionException {
		CountClauseContext _localctx = new CountClauseContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(618);
			match(Kcount);
			setState(619);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterQuantifiedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitQuantifiedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQuantifiedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedExprContext quantifiedExpr() throws RecognitionException {
		QuantifiedExprContext _localctx = new QuantifiedExprContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(623);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(621);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(622);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(625);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(630);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(626);
				match(T__19);
				setState(627);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(632);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(633);
			match(Ksatisfies);
			setState(634);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterQuantifiedExprVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitQuantifiedExprVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQuantifiedExprVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedExprVarContext quantifiedExprVar() throws RecognitionException {
		QuantifiedExprVarContext _localctx = new QuantifiedExprVarContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(636);
			varRef();
			setState(639);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(637);
				match(Kas);
				setState(638);
				sequenceType();
				}
			}

			setState(641);
			match(Kin);
			setState(642);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSwitchExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSwitchExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchExprContext switchExpr() throws RecognitionException {
		SwitchExprContext _localctx = new SwitchExprContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(644);
			match(Kswitch);
			setState(645);
			match(T__23);
			setState(646);
			((SwitchExprContext)_localctx).cond = expr();
			setState(647);
			match(T__24);
			setState(649); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(648);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(651); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(653);
			match(Kdefault);
			setState(654);
			match(Kreturn);
			setState(655);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSwitchCaseClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSwitchCaseClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseClauseContext switchCaseClause() throws RecognitionException {
		SwitchCaseClauseContext _localctx = new SwitchCaseClauseContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(659); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(657);
				match(Kcase);
				setState(658);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(661); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(663);
			match(Kreturn);
			setState(664);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTypeSwitchExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTypeSwitchExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeSwitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSwitchExprContext typeSwitchExpr() throws RecognitionException {
		TypeSwitchExprContext _localctx = new TypeSwitchExprContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(666);
			match(Ktypeswitch);
			setState(667);
			match(T__23);
			setState(668);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(669);
			match(T__24);
			setState(671); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(670);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(673); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(675);
			match(Kdefault);
			setState(677);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(676);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(679);
			match(Kreturn);
			setState(680);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterCaseClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitCaseClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseClauseContext caseClause() throws RecognitionException {
		CaseClauseContext _localctx = new CaseClauseContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(682);
			match(Kcase);
			setState(686);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(683);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(684);
				match(Kas);
				}
			}

			setState(688);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(693);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__32) {
				{
				{
				setState(689);
				match(T__32);
				setState(690);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(695);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(696);
			match(Kreturn);
			setState(697);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterIfExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitIfExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfExprContext ifExpr() throws RecognitionException {
		IfExprContext _localctx = new IfExprContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(699);
			match(Kif);
			setState(700);
			match(T__23);
			setState(701);
			((IfExprContext)_localctx).test_condition = expr();
			setState(702);
			match(T__24);
			setState(703);
			match(Kthen);
			setState(704);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(705);
			match(Kelse);
			setState(706);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTryCatchExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTryCatchExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTryCatchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryCatchExprContext tryCatchExpr() throws RecognitionException {
		TryCatchExprContext _localctx = new TryCatchExprContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_tryCatchExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(708);
			match(Ktry);
			setState(709);
			match(T__25);
			setState(710);
			((TryCatchExprContext)_localctx).try_expression = expr();
			setState(711);
			match(T__26);
			setState(713); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(712);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(715); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
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
	public static class CatchClauseContext extends ParserRuleContext {
		public ExprContext catch_expression;
		public TerminalNode Kcatch() { return getToken(JsoniqParser.Kcatch, 0); }
		public CatchErrorListContext catchErrorList() {
			return getRuleContext(CatchErrorListContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterCatchClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitCatchClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCatchClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_catchClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(717);
			match(Kcatch);
			setState(718);
			catchErrorList();
			setState(719);
			match(T__25);
			setState(720);
			((CatchClauseContext)_localctx).catch_expression = expr();
			setState(721);
			match(T__26);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSwitchCaseOperand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSwitchCaseOperand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchCaseOperand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseOperandContext switchCaseOperand() throws RecognitionException {
		SwitchCaseOperandContext _localctx = new SwitchCaseOperandContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_switchCaseOperand);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(723);
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
	public static class CatchErrorListContext extends ParserRuleContext {
		public Token s34;
		public List<Token> jokers = new ArrayList<Token>();
		public QnameContext qname;
		public List<QnameContext> errors = new ArrayList<QnameContext>();
		public List<QnameContext> qname() {
			return getRuleContexts(QnameContext.class);
		}
		public QnameContext qname(int i) {
			return getRuleContext(QnameContext.class,i);
		}
		public CatchErrorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchErrorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterCatchErrorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitCatchErrorList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCatchErrorList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchErrorListContext catchErrorList() throws RecognitionException {
		CatchErrorListContext _localctx = new CatchErrorListContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_catchErrorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(727);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__33:
				{
				setState(725);
				((CatchErrorListContext)_localctx).s34 = match(T__33);
				((CatchErrorListContext)_localctx).jokers.add(((CatchErrorListContext)_localctx).s34);
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
			case NullLiteral:
			case NCName:
				{
				setState(726);
				((CatchErrorListContext)_localctx).qname = qname();
				((CatchErrorListContext)_localctx).errors.add(((CatchErrorListContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(736);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__32) {
				{
				{
				setState(729);
				match(T__32);
				setState(732);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__33:
					{
					setState(730);
					((CatchErrorListContext)_localctx).s34 = match(T__33);
					((CatchErrorListContext)_localctx).jokers.add(((CatchErrorListContext)_localctx).s34);
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
				case NullLiteral:
				case NCName:
					{
					setState(731);
					((CatchErrorListContext)_localctx).qname = qname();
					((CatchErrorListContext)_localctx).errors.add(((CatchErrorListContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(738);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrExprContext orExpr() throws RecognitionException {
		OrExprContext _localctx = new OrExprContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(739);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(744);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(740);
					match(Kor);
					setState(741);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(746);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(747);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(752);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(748);
					match(Kand);
					setState(749);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(754);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterNotExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitNotExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNotExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotExprContext notExpr() throws RecognitionException {
		NotExprContext _localctx = new NotExprContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(756);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				{
				setState(755);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(758);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonExprContext extends ParserRuleContext {
		public StringConcatExprContext main_expr;
		public Token s35;
		public List<Token> op = new ArrayList<Token>();
		public Token s36;
		public Token s37;
		public Token s38;
		public Token s39;
		public Token s40;
		public Token s4;
		public Token s41;
		public Token s42;
		public Token s43;
		public Token s44;
		public Token s45;
		public Token _tset1331;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterComparisonExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitComparisonExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitComparisonExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonExprContext comparisonExpr() throws RecognitionException {
		ComparisonExprContext _localctx = new ComparisonExprContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(760);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(763);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70334384439312L) != 0)) {
				{
				setState(761);
				((ComparisonExprContext)_localctx)._tset1331 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 70334384439312L) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1331 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1331);
				setState(762);
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
		public StringConcatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConcatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterStringConcatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitStringConcatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStringConcatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConcatExprContext stringConcatExpr() throws RecognitionException {
		StringConcatExprContext _localctx = new StringConcatExprContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(765);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(770);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45) {
				{
				{
				setState(766);
				match(T__45);
				setState(767);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(772);
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
		public TerminalNode Kto() { return getToken(JsoniqParser.Kto, 0); }
		public RangeExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterRangeExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitRangeExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitRangeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeExprContext rangeExpr() throws RecognitionException {
		RangeExprContext _localctx = new RangeExprContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(773);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(776);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(774);
				match(Kto);
				setState(775);
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
		public Token s47;
		public List<Token> op = new ArrayList<Token>();
		public Token s48;
		public Token _tset1440;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterAdditiveExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitAdditiveExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAdditiveExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExprContext additiveExpr() throws RecognitionException {
		AdditiveExprContext _localctx = new AdditiveExprContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(778);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(783);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(779);
					((AdditiveExprContext)_localctx)._tset1440 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__46 || _la==T__47) ) {
						((AdditiveExprContext)_localctx)._tset1440 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1440);
					setState(780);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(785);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public InstanceOfExprContext main_expr;
		public Token s34;
		public List<Token> op = new ArrayList<Token>();
		public Token s49;
		public Token s50;
		public Token s51;
		public Token _tset1468;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterMultiplicativeExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitMultiplicativeExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitMultiplicativeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExprContext multiplicativeExpr() throws RecognitionException {
		MultiplicativeExprContext _localctx = new MultiplicativeExprContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(786);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(791);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 3940666853818368L) != 0)) {
				{
				{
				setState(787);
				((MultiplicativeExprContext)_localctx)._tset1468 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 3940666853818368L) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1468 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1468);
				setState(788);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(793);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterInstanceOfExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitInstanceOfExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInstanceOfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstanceOfExprContext instanceOfExpr() throws RecognitionException {
		InstanceOfExprContext _localctx = new InstanceOfExprContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(794);
			((InstanceOfExprContext)_localctx).main_expr = isStaticallyExpr();
			setState(798);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(795);
				match(Kinstance);
				setState(796);
				match(Kof);
				setState(797);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterIsStaticallyExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitIsStaticallyExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIsStaticallyExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsStaticallyExprContext isStaticallyExpr() throws RecognitionException {
		IsStaticallyExprContext _localctx = new IsStaticallyExprContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_isStaticallyExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			((IsStaticallyExprContext)_localctx).main_expr = treatExpr();
			setState(804);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				{
				setState(801);
				match(Kis);
				setState(802);
				match(Kstatically);
				setState(803);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTreatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTreatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTreatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TreatExprContext treatExpr() throws RecognitionException {
		TreatExprContext _localctx = new TreatExprContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(810);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				{
				setState(807);
				match(Ktreat);
				setState(808);
				match(Kas);
				setState(809);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterCastableExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitCastableExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCastableExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastableExprContext castableExpr() throws RecognitionException {
		CastableExprContext _localctx = new CastableExprContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(812);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(816);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				{
				setState(813);
				match(Kcastable);
				setState(814);
				match(Kas);
				setState(815);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterCastExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitCastExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCastExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastExprContext castExpr() throws RecognitionException {
		CastExprContext _localctx = new CastExprContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(818);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(822);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
			case 1:
				{
				setState(819);
				match(Kcast);
				setState(820);
				match(Kas);
				setState(821);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterArrowExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitArrowExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrowExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowExprContext arrowExpr() throws RecognitionException {
		ArrowExprContext _localctx = new ArrowExprContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(824);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(833);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(825);
					match(T__3);
					setState(826);
					match(T__43);
					}
					setState(828);
					((ArrowExprContext)_localctx).arrowFunctionSpecifier = arrowFunctionSpecifier();
					((ArrowExprContext)_localctx).function.add(((ArrowExprContext)_localctx).arrowFunctionSpecifier);
					setState(829);
					((ArrowExprContext)_localctx).argumentList = argumentList();
					((ArrowExprContext)_localctx).arguments.add(((ArrowExprContext)_localctx).argumentList);
					}
					} 
				}
				setState(835);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterArrowFunctionSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitArrowFunctionSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrowFunctionSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowFunctionSpecifierContext arrowFunctionSpecifier() throws RecognitionException {
		ArrowFunctionSpecifierContext _localctx = new ArrowFunctionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_arrowFunctionSpecifier);
		try {
			setState(839);
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
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(836);
				qname();
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 2);
				{
				setState(837);
				varRef();
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(838);
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
	public static class UnaryExprContext extends ParserRuleContext {
		public Token s48;
		public List<Token> op = new ArrayList<Token>();
		public Token s47;
		public Token _tset1647;
		public ValueExprContext main_expr;
		public ValueExprContext valueExpr() {
			return getRuleContext(ValueExprContext.class,0);
		}
		public UnaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterUnaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitUnaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExprContext unaryExpr() throws RecognitionException {
		UnaryExprContext _localctx = new UnaryExprContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(844);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__46 || _la==T__47) {
				{
				{
				setState(841);
				((UnaryExprContext)_localctx)._tset1647 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__46 || _la==T__47) ) {
					((UnaryExprContext)_localctx)._tset1647 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1647);
				}
				}
				setState(846);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(847);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterValueExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitValueExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitValueExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueExprContext valueExpr() throws RecognitionException {
		ValueExprContext _localctx = new ValueExprContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_valueExpr);
		try {
			setState(852);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(849);
				((ValueExprContext)_localctx).simpleMap_expr = simpleMapExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(850);
				((ValueExprContext)_localctx).validate_expr = validateExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(851);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterValidateExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitValidateExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitValidateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidateExprContext validateExpr() throws RecognitionException {
		ValidateExprContext _localctx = new ValidateExprContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_validateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(854);
			match(Kvalidate);
			setState(855);
			match(Ktype);
			setState(856);
			sequenceType();
			setState(857);
			match(T__25);
			setState(858);
			expr();
			setState(859);
			match(T__26);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterAnnotateExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitAnnotateExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnnotateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotateExprContext annotateExpr() throws RecognitionException {
		AnnotateExprContext _localctx = new AnnotateExprContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_annotateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(861);
			match(Kannotate);
			setState(862);
			match(Ktype);
			setState(863);
			sequenceType();
			setState(864);
			match(T__25);
			setState(865);
			expr();
			setState(866);
			match(T__26);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSimpleMapExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSimpleMapExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSimpleMapExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleMapExprContext simpleMapExpr() throws RecognitionException {
		SimpleMapExprContext _localctx = new SimpleMapExprContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(873);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__51) {
				{
				{
				setState(869);
				match(T__51);
				setState(870);
				((SimpleMapExprContext)_localctx).postFixExpr = postFixExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).postFixExpr);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterPostFixExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitPostFixExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPostFixExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostFixExprContext postFixExpr() throws RecognitionException {
		PostFixExprContext _localctx = new PostFixExprContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(876);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(884);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(882);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
					case 1:
						{
						setState(877);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(878);
						predicate();
						}
						break;
					case 3:
						{
						setState(879);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(880);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(881);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(886);
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
	public static class ArrayLookupContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ArrayLookupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayLookup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterArrayLookup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitArrayLookup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayLookupContext arrayLookup() throws RecognitionException {
		ArrayLookupContext _localctx = new ArrayLookupContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(887);
			match(T__52);
			setState(888);
			match(T__52);
			setState(889);
			expr();
			setState(890);
			match(T__53);
			setState(891);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayUnboxingContext extends ParserRuleContext {
		public ArrayUnboxingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayUnboxing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterArrayUnboxing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitArrayUnboxing(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayUnboxing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayUnboxingContext arrayUnboxing() throws RecognitionException {
		ArrayUnboxingContext _localctx = new ArrayUnboxingContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(893);
			match(T__52);
			setState(894);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(896);
			match(T__52);
			setState(897);
			expr();
			setState(898);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterObjectLookup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitObjectLookup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitObjectLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectLookupContext objectLookup() throws RecognitionException {
		ObjectLookupContext _localctx = new ObjectLookupContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(900);
			match(T__54);
			setState(907);
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
			case NullLiteral:
				{
				setState(901);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(902);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(903);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__23:
				{
				setState(904);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__31:
				{
				setState(905);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__55:
				{
				setState(906);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterPrimaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitPrimaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExprContext primaryExpr() throws RecognitionException {
		PrimaryExprContext _localctx = new PrimaryExprContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_primaryExpr);
		try {
			setState(924);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(909);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(910);
				match(Ktrue);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(911);
				match(Kfalse);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(912);
				match(Literal);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(913);
				stringLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(914);
				varRef();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(915);
				parenthesizedExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(916);
				contextItemExpr();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(917);
				objectConstructor();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(918);
				functionCall();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(919);
				orderedExpr();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(920);
				unorderedExpr();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(921);
				arrayConstructor();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(922);
				functionItemExpr();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(923);
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
	public static class BlockExprContext extends ParserRuleContext {
		public StatementsAndExprContext statementsAndExpr() {
			return getRuleContext(StatementsAndExprContext.class,0);
		}
		public BlockExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterBlockExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitBlockExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBlockExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockExprContext blockExpr() throws RecognitionException {
		BlockExprContext _localctx = new BlockExprContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_blockExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(926);
			match(T__25);
			setState(927);
			statementsAndExpr();
			setState(928);
			match(T__26);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public QnameContext var_name;
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public VarRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterVarRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitVarRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarRefContext varRef() throws RecognitionException {
		VarRefContext _localctx = new VarRefContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(930);
			match(T__31);
			setState(931);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ParenthesizedExprContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParenthesizedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenthesizedExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterParenthesizedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitParenthesizedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParenthesizedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenthesizedExprContext parenthesizedExpr() throws RecognitionException {
		ParenthesizedExprContext _localctx = new ParenthesizedExprContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(933);
			match(T__23);
			setState(935);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -783204118310158272L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 9223372036854775807L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 131L) != 0)) {
				{
				setState(934);
				expr();
				}
			}

			setState(937);
			match(T__24);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public ContextItemExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextItemExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterContextItemExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitContextItemExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContextItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemExprContext contextItemExpr() throws RecognitionException {
		ContextItemExprContext _localctx = new ContextItemExprContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(939);
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

	@SuppressWarnings("CheckReturnValue")
	public static class OrderedExprContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public OrderedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderedExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterOrderedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitOrderedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderedExprContext orderedExpr() throws RecognitionException {
		OrderedExprContext _localctx = new OrderedExprContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(941);
			match(T__5);
			setState(942);
			match(T__25);
			setState(943);
			expr();
			setState(944);
			match(T__26);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode Kunordered() { return getToken(JsoniqParser.Kunordered, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnorderedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unorderedExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterUnorderedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitUnorderedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUnorderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnorderedExprContext unorderedExpr() throws RecognitionException {
		UnorderedExprContext _localctx = new UnorderedExprContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(946);
			match(Kunordered);
			setState(947);
			match(T__25);
			setState(948);
			expr();
			setState(949);
			match(T__26);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			((FunctionCallContext)_localctx).fn_name = qname();
			setState(952);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(954);
			match(T__23);
			setState(961);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -783204118310158272L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 131L) != 0)) {
				{
				{
				setState(955);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(957);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__19) {
					{
					setState(956);
					match(T__19);
					}
				}

				}
				}
				setState(963);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(964);
			match(T__24);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode ArgumentPlaceholder() { return getToken(JsoniqParser.ArgumentPlaceholder, 0); }
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_argument);
		try {
			setState(968);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
			case T__22:
			case T__23:
			case T__25:
			case T__31:
			case T__46:
			case T__47:
			case T__52:
			case T__55:
			case T__57:
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
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(966);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(967);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterFunctionItemExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitFunctionItemExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionItemExprContext functionItemExpr() throws RecognitionException {
		FunctionItemExprContext _localctx = new FunctionItemExprContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_functionItemExpr);
		try {
			setState(972);
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
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(970);
				namedFunctionRef();
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(971);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterNamedFunctionRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitNamedFunctionRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNamedFunctionRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedFunctionRefContext namedFunctionRef() throws RecognitionException {
		NamedFunctionRefContext _localctx = new NamedFunctionRefContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(974);
			((NamedFunctionRefContext)_localctx).fn_name = qname();
			setState(975);
			match(T__56);
			setState(976);
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

	@SuppressWarnings("CheckReturnValue")
	public static class InlineFunctionExprContext extends ParserRuleContext {
		public SequenceTypeContext return_type;
		public ExprContext fn_body;
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public InlineFunctionExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineFunctionExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterInlineFunctionExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitInlineFunctionExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInlineFunctionExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineFunctionExprContext inlineFunctionExpr() throws RecognitionException {
		InlineFunctionExprContext _localctx = new InlineFunctionExprContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(978);
			match(T__22);
			setState(979);
			match(T__23);
			setState(981);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(980);
				paramList();
				}
			}

			setState(983);
			match(T__24);
			setState(986);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(984);
				match(Kas);
				setState(985);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(988);
			match(T__25);
			setState(990);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -783204118310158272L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 9223372036854775807L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 131L) != 0)) {
				{
				setState(989);
				((InlineFunctionExprContext)_localctx).fn_body = expr();
				}
			}

			setState(992);
			match(T__26);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterInsertExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitInsertExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInsertExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertExprContext insertExpr() throws RecognitionException {
		InsertExprContext _localctx = new InsertExprContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_insertExpr);
		int _la;
		try {
			setState(1017);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(994);
				match(Kinsert);
				setState(995);
				match(Kjson);
				setState(996);
				((InsertExprContext)_localctx).to_insert_expr = exprSingle();
				setState(997);
				match(Kinto);
				setState(998);
				((InsertExprContext)_localctx).main_expr = exprSingle();
				setState(1002);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
				case 1:
					{
					setState(999);
					match(Kat);
					setState(1000);
					match(Kposition);
					setState(1001);
					((InsertExprContext)_localctx).pos_expr = exprSingle();
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1004);
				match(Kinsert);
				setState(1005);
				match(Kjson);
				setState(1006);
				pairConstructor();
				setState(1011);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(1007);
					match(T__19);
					setState(1008);
					pairConstructor();
					}
					}
					setState(1013);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1014);
				match(Kinto);
				setState(1015);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterDeleteExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitDeleteExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDeleteExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteExprContext deleteExpr() throws RecognitionException {
		DeleteExprContext _localctx = new DeleteExprContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_deleteExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1019);
			match(Kdelete);
			setState(1020);
			match(Kjson);
			setState(1021);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterRenameExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitRenameExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitRenameExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RenameExprContext renameExpr() throws RecognitionException {
		RenameExprContext _localctx = new RenameExprContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_renameExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1023);
			match(Krename);
			setState(1024);
			match(Kjson);
			setState(1025);
			updateLocator();
			setState(1026);
			match(Kas);
			setState(1027);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterReplaceExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitReplaceExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitReplaceExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReplaceExprContext replaceExpr() throws RecognitionException {
		ReplaceExprContext _localctx = new ReplaceExprContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_replaceExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1029);
			match(Kreplace);
			setState(1030);
			match(Kjson);
			setState(1031);
			match(Kvalue);
			setState(1032);
			match(Kof);
			setState(1033);
			updateLocator();
			setState(1034);
			match(Kwith);
			setState(1035);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTransformExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTransformExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTransformExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransformExprContext transformExpr() throws RecognitionException {
		TransformExprContext _localctx = new TransformExprContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_transformExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1037);
			match(Kcopy);
			setState(1038);
			match(Kjson);
			setState(1039);
			copyDecl();
			setState(1044);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(1040);
				match(T__19);
				setState(1041);
				copyDecl();
				}
				}
				setState(1046);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1047);
			match(Kmodify);
			setState(1048);
			((TransformExprContext)_localctx).mod_expr = exprSingle();
			setState(1049);
			match(Kreturn);
			setState(1050);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterAppendExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitAppendExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAppendExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AppendExprContext appendExpr() throws RecognitionException {
		AppendExprContext _localctx = new AppendExprContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_appendExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1052);
			match(Kappend);
			setState(1053);
			match(Kjson);
			setState(1054);
			((AppendExprContext)_localctx).to_append_expr = exprSingle();
			setState(1055);
			match(Kinto);
			setState(1056);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterUpdateLocator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitUpdateLocator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUpdateLocator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateLocatorContext updateLocator() throws RecognitionException {
		UpdateLocatorContext _localctx = new UpdateLocatorContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_updateLocator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1058);
			((UpdateLocatorContext)_localctx).main_expr = primaryExpr();
			setState(1061); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(1061);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__52:
						{
						setState(1059);
						arrayLookup();
						}
						break;
					case T__54:
						{
						setState(1060);
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
				setState(1063); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,100,_ctx);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterCopyDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitCopyDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCopyDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CopyDeclContext copyDecl() throws RecognitionException {
		CopyDeclContext _localctx = new CopyDeclContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_copyDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1065);
			((CopyDeclContext)_localctx).var_ref = varRef();
			setState(1066);
			match(T__20);
			setState(1067);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterStatements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitStatements(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_statements);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1072);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,101,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1069);
					statement();
					}
					} 
				}
				setState(1074);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,101,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterStatementsAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitStatementsAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatementsAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsAndExprContext statementsAndExpr() throws RecognitionException {
		StatementsAndExprContext _localctx = new StatementsAndExprContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_statementsAndExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1075);
			statements();
			setState(1076);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterStatementsAndOptionalExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitStatementsAndOptionalExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatementsAndOptionalExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsAndOptionalExprContext statementsAndOptionalExpr() throws RecognitionException {
		StatementsAndOptionalExprContext _localctx = new StatementsAndOptionalExprContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_statementsAndOptionalExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1078);
			statements();
			setState(1080);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -783204118310158272L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 9223372036854775807L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 131L) != 0)) {
				{
				setState(1079);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_statement);
		try {
			setState(1095);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1082);
				applyStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1083);
				assignStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1084);
				blockStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1085);
				breakStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1086);
				continueStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1087);
				exitStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1088);
				flworStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1089);
				ifStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1090);
				switchStatement();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1091);
				tryCatchStatement();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1092);
				typeswitchStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1093);
				varDeclStatement();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1094);
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
		public ApplyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_applyStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterApplyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitApplyStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitApplyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ApplyStatementContext applyStatement() throws RecognitionException {
		ApplyStatementContext _localctx = new ApplyStatementContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_applyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1097);
			exprSimple();
			setState(1098);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterAssignStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitAssignStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAssignStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignStatementContext assignStatement() throws RecognitionException {
		AssignStatementContext _localctx = new AssignStatementContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_assignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1100);
			match(T__31);
			setState(1101);
			qname();
			setState(1102);
			match(T__20);
			setState(1103);
			exprSingle();
			setState(1104);
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

	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementContext extends ParserRuleContext {
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitBlockStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_blockStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1106);
			match(T__25);
			setState(1107);
			statements();
			setState(1108);
			match(T__26);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public TerminalNode Kbreak() { return getToken(JsoniqParser.Kbreak, 0); }
		public TerminalNode Kloop() { return getToken(JsoniqParser.Kloop, 0); }
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterBreakStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitBreakStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1110);
			match(Kbreak);
			setState(1111);
			match(Kloop);
			setState(1112);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends ParserRuleContext {
		public TerminalNode Kcontinue() { return getToken(JsoniqParser.Kcontinue, 0); }
		public TerminalNode Kloop() { return getToken(JsoniqParser.Kloop, 0); }
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterContinueStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitContinueStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1114);
			match(Kcontinue);
			setState(1115);
			match(Kloop);
			setState(1116);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterExitStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitExitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExitStatementContext exitStatement() throws RecognitionException {
		ExitStatementContext _localctx = new ExitStatementContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_exitStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1118);
			match(Kexit);
			setState(1119);
			match(Kreturning);
			setState(1120);
			exprSingle();
			setState(1121);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterFlworStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitFlworStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFlworStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlworStatementContext flworStatement() throws RecognitionException {
		FlworStatementContext _localctx = new FlworStatementContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_flworStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1123);
			initialClause();
			setState(1127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & 24623L) != 0)) {
				{
				{
				setState(1124);
				intermediateClause();
				}
				}
				setState(1129);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1130);
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
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ReturnStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStatementContext returnStatement() throws RecognitionException {
		ReturnStatementContext _localctx = new ReturnStatementContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_returnStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1132);
			match(Kreturn);
			setState(1133);
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
		public TerminalNode Kif() { return getToken(JsoniqParser.Kif, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Kthen() { return getToken(JsoniqParser.Kthen, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode Kelse() { return getToken(JsoniqParser.Kelse, 0); }
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_ifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1135);
			match(Kif);
			setState(1136);
			match(T__23);
			setState(1137);
			expr();
			setState(1138);
			match(T__24);
			setState(1139);
			match(Kthen);
			setState(1140);
			statement();
			setState(1141);
			match(Kelse);
			setState(1142);
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
		public TerminalNode Kswitch() { return getToken(JsoniqParser.Kswitch, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSwitchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSwitchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchStatementContext switchStatement() throws RecognitionException {
		SwitchStatementContext _localctx = new SwitchStatementContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_switchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1144);
			match(Kswitch);
			setState(1145);
			match(T__23);
			setState(1146);
			expr();
			setState(1147);
			match(T__24);
			setState(1149); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1148);
				switchCaseStatement();
				}
				}
				setState(1151); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(1153);
			match(Kdefault);
			setState(1154);
			match(Kreturn);
			setState(1155);
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
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<TerminalNode> Kcase() { return getTokens(JsoniqParser.Kcase); }
		public TerminalNode Kcase(int i) {
			return getToken(JsoniqParser.Kcase, i);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSwitchCaseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSwitchCaseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchCaseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseStatementContext switchCaseStatement() throws RecognitionException {
		SwitchCaseStatementContext _localctx = new SwitchCaseStatementContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_switchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1159); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1157);
				match(Kcase);
				setState(1158);
				switchCaseOperand();
				}
				}
				setState(1161); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(1163);
			match(Kreturn);
			setState(1164);
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
		public TerminalNode Ktry() { return getToken(JsoniqParser.Ktry, 0); }
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public List<TerminalNode> Kcatch() { return getTokens(JsoniqParser.Kcatch); }
		public TerminalNode Kcatch(int i) {
			return getToken(JsoniqParser.Kcatch, i);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTryCatchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTryCatchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTryCatchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryCatchStatementContext tryCatchStatement() throws RecognitionException {
		TryCatchStatementContext _localctx = new TryCatchStatementContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_tryCatchStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1166);
			match(Ktry);
			setState(1167);
			blockStatement();
			setState(1172); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1168);
					match(Kcatch);
					setState(1169);
					catchErrorList();
					setState(1170);
					blockStatement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1174); 
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
		public TerminalNode Ktypeswitch() { return getToken(JsoniqParser.Ktypeswitch, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<CaseStatementContext> caseStatement() {
			return getRuleContexts(CaseStatementContext.class);
		}
		public CaseStatementContext caseStatement(int i) {
			return getRuleContext(CaseStatementContext.class,i);
		}
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TypeswitchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeswitchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTypeswitchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTypeswitchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeswitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeswitchStatementContext typeswitchStatement() throws RecognitionException {
		TypeswitchStatementContext _localctx = new TypeswitchStatementContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_typeswitchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1176);
			match(Ktypeswitch);
			setState(1177);
			match(T__23);
			setState(1178);
			expr();
			setState(1179);
			match(T__24);
			setState(1181); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1180);
				caseStatement();
				}
				}
				setState(1183); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(1185);
			match(Kdefault);
			setState(1188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(1186);
				match(T__31);
				setState(1187);
				qname();
				}
			}

			setState(1190);
			match(Kreturn);
			setState(1191);
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
		public TerminalNode Kcase() { return getToken(JsoniqParser.Kcase, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public CaseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterCaseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitCaseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCaseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseStatementContext caseStatement() throws RecognitionException {
		CaseStatementContext _localctx = new CaseStatementContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_caseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1193);
			match(Kcase);
			setState(1198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(1194);
				match(T__31);
				setState(1195);
				qname();
				setState(1196);
				match(Kas);
				}
			}

			setState(1200);
			sequenceType();
			setState(1201);
			match(Kreturn);
			setState(1202);
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
	public static class TypeDeclarationContext extends ParserRuleContext {
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_typeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1204);
			match(Kas);
			setState(1205);
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
	public static class VarDeclStatementContext extends ParserRuleContext {
		public TerminalNode Kvariable() { return getToken(JsoniqParser.Kvariable, 0); }
		public List<QnameContext> qname() {
			return getRuleContexts(QnameContext.class);
		}
		public QnameContext qname(int i) {
			return getRuleContext(QnameContext.class,i);
		}
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public VarDeclStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterVarDeclStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitVarDeclStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarDeclStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclStatementContext varDeclStatement() throws RecognitionException {
		VarDeclStatementContext _localctx = new VarDeclStatementContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_varDeclStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1207);
			match(Kvariable);
			setState(1208);
			match(T__31);
			setState(1209);
			qname();
			setState(1211);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(1210);
				typeDeclaration();
				}
			}

			setState(1215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(1213);
				match(T__20);
				setState(1214);
				exprSingle();
				}
			}

			setState(1229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(1217);
				match(T__19);
				setState(1218);
				match(T__31);
				setState(1219);
				qname();
				setState(1221);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(1220);
					typeDeclaration();
					}
				}

				setState(1225);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__20) {
					{
					setState(1223);
					match(T__20);
					setState(1224);
					exprSingle();
					}
				}

				}
				}
				setState(1231);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1232);
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

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterWhileStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitWhileStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1234);
			match(Kwhile);
			setState(1235);
			match(T__23);
			setState(1236);
			expr();
			setState(1237);
			match(T__24);
			setState(1238);
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
	public static class SequenceTypeContext extends ParserRuleContext {
		public ItemTypeContext item;
		public Token s127;
		public List<Token> question = new ArrayList<Token>();
		public Token s34;
		public List<Token> star = new ArrayList<Token>();
		public Token s47;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSequenceType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSequenceType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSequenceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceTypeContext sequenceType() throws RecognitionException {
		SequenceTypeContext _localctx = new SequenceTypeContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_sequenceType);
		try {
			setState(1248);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(1240);
				match(T__23);
				setState(1241);
				match(T__24);
				}
				break;
			case T__22:
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
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 2);
				{
				setState(1242);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(1246);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
				case 1:
					{
					setState(1243);
					((SequenceTypeContext)_localctx).s127 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s127);
					}
					break;
				case 2:
					{
					setState(1244);
					((SequenceTypeContext)_localctx).s34 = match(T__33);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s34);
					}
					break;
				case 3:
					{
					setState(1245);
					((SequenceTypeContext)_localctx).s47 = match(T__46);
					((SequenceTypeContext)_localctx).plus.add(((SequenceTypeContext)_localctx).s47);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectConstructorContext extends ParserRuleContext {
		public Token s58;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterObjectConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitObjectConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitObjectConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectConstructorContext objectConstructor() throws RecognitionException {
		ObjectConstructorContext _localctx = new ObjectConstructorContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_objectConstructor);
		int _la;
		try {
			setState(1266);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__25:
				enterOuterAlt(_localctx, 1);
				{
				setState(1250);
				match(T__25);
				setState(1259);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -783204118310158272L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 9223372036854775807L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 131L) != 0)) {
					{
					setState(1251);
					pairConstructor();
					setState(1256);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__19) {
						{
						{
						setState(1252);
						match(T__19);
						setState(1253);
						pairConstructor();
						}
						}
						setState(1258);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1261);
				match(T__26);
				}
				break;
			case T__57:
				enterOuterAlt(_localctx, 2);
				{
				setState(1262);
				((ObjectConstructorContext)_localctx).s58 = match(T__57);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s58);
				setState(1263);
				expr();
				setState(1264);
				match(T__58);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterItemType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitItemType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitItemType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ItemTypeContext itemType() throws RecognitionException {
		ItemTypeContext _localctx = new ItemTypeContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_itemType);
		try {
			setState(1271);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1268);
				qname();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1269);
				match(NullLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1270);
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

	@SuppressWarnings("CheckReturnValue")
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterFunctionTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitFunctionTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionTestContext functionTest() throws RecognitionException {
		FunctionTestContext _localctx = new FunctionTestContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_functionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1275);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				{
				setState(1273);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(1274);
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
		public AnyFunctionTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyFunctionTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterAnyFunctionTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitAnyFunctionTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnyFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyFunctionTestContext anyFunctionTest() throws RecognitionException {
		AnyFunctionTestContext _localctx = new AnyFunctionTestContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1277);
			match(T__22);
			setState(1278);
			match(T__23);
			setState(1279);
			match(T__33);
			setState(1280);
			match(T__24);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterTypedFunctionTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitTypedFunctionTest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypedFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedFunctionTestContext typedFunctionTest() throws RecognitionException {
		TypedFunctionTestContext _localctx = new TypedFunctionTestContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1282);
			match(T__22);
			setState(1283);
			match(T__23);
			setState(1292);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 23)) & ~0x3f) == 0 && ((1L << (_la - 23)) & -137438953469L) != 0) || ((((_la - 87)) & ~0x3f) == 0 && ((1L << (_la - 87)) & 284223755780095L) != 0)) {
				{
				setState(1284);
				((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
				((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
				setState(1289);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(1285);
					match(T__19);
					setState(1286);
					((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
					((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
					}
					}
					setState(1291);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1294);
			match(T__24);
			setState(1295);
			match(Kas);
			setState(1296);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SingleTypeContext extends ParserRuleContext {
		public ItemTypeContext item;
		public Token s127;
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterSingleType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitSingleType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSingleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleTypeContext singleType() throws RecognitionException {
		SingleTypeContext _localctx = new SingleTypeContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1298);
			((SingleTypeContext)_localctx).item = itemType();
			setState(1300);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
			case 1:
				{
				setState(1299);
				((SingleTypeContext)_localctx).s127 = match(ArgumentPlaceholder);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).s127);
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
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterPairConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitPairConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPairConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairConstructorContext pairConstructor() throws RecognitionException {
		PairConstructorContext _localctx = new PairConstructorContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1304);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
			case 1:
				{
				setState(1302);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(1303);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(1306);
			_la = _input.LA(1);
			if ( !(_la==T__7 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1307);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayConstructorContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ArrayConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayConstructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterArrayConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitArrayConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayConstructorContext arrayConstructor() throws RecognitionException {
		ArrayConstructorContext _localctx = new ArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1309);
			match(T__52);
			setState(1311);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -783204118310158272L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 9223372036854775807L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 131L) != 0)) {
				{
				setState(1310);
				expr();
				}
			}

			setState(1313);
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
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterUriLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitUriLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUriLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UriLiteralContext uriLiteral() throws RecognitionException {
		UriLiteralContext _localctx = new UriLiteralContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1315);
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
	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(JsoniqParser.STRING, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1317);
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

	@SuppressWarnings("CheckReturnValue")
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
		public KeyWordsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWords; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).enterKeyWords(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsoniqListener ) ((JsoniqListener)listener).exitKeyWords(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWords(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordsContext keyWords() throws RecognitionException {
		KeyWordsContext _localctx = new KeyWordsContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1319);
			_la = _input.LA(1);
			if ( !(((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & -1L) != 0) || ((((_la - 124)) & ~0x3f) == 0 && ((1L << (_la - 124)) & 19L) != 0)) ) {
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
		"\u0004\u0001\u008f\u052a\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"\u0080\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u010b\b\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001\u010f\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004\u011f"+
		"\b\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u0123\b\u0004\n\u0004\f\u0004"+
		"\u0126\t\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u012b\b"+
		"\u0004\n\u0004\f\u0004\u012e\t\u0004\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u0136\b\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0003\b\u0142\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0003\f\u0158\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u015e\b\f"+
		"\n\f\f\f\u0161\t\f\u0001\r\u0001\r\u0003\r\u0165\b\r\u0001\r\u0003\r\u0168"+
		"\b\r\u0001\r\u0001\r\u0003\r\u016c\b\r\u0001\u000e\u0001\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0175\b\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f"+
		"\u017c\b\u000f\n\u000f\f\u000f\u017f\t\u000f\u0003\u000f\u0181\b\u000f"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010"+
		"\u0188\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0003\u0010\u018f\b\u0010\u0003\u0010\u0191\b\u0010\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0198\b\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u019f"+
		"\b\u0011\u0003\u0011\u01a1\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0003\u0012\u01a8\b\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0003\u0012\u01ad\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012"+
		"\u01b1\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u01b5\b\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u01bc"+
		"\b\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u01c6\b\u0014\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0005\u0015\u01cb\b\u0015\n\u0015\f\u0015\u01ce\t\u0015"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u01d4\b\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0005\u0017\u01d9\b\u0017\n\u0017"+
		"\f\u0017\u01dc\t\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0003\u0018\u01e4\b\u0018\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0003\u0019\u01ee\b\u0019\u0001\u001a\u0001\u001a\u0003\u001a\u01f2\b"+
		"\u001a\u0001\u001a\u0005\u001a\u01f5\b\u001a\n\u001a\f\u001a\u01f8\t\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0003\u001b"+
		"\u01ff\b\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0003\u001c\u0206\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0005\u001d\u020c\b\u001d\n\u001d\f\u001d\u020f\t\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0003\u001e\u0214\b\u001e\u0001\u001e\u0001\u001e\u0003"+
		"\u001e\u0218\b\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u021c\b\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0005\u001f\u0225\b\u001f\n\u001f\f\u001f\u0228\t\u001f\u0001"+
		" \u0001 \u0001 \u0003 \u022d\b \u0001 \u0001 \u0001 \u0001!\u0001!\u0001"+
		"!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0005\"\u023a\b\"\n\"\f\"\u023d"+
		"\t\"\u0001#\u0001#\u0001#\u0003#\u0242\b#\u0001#\u0001#\u0003#\u0246\b"+
		"#\u0001#\u0001#\u0003#\u024a\b#\u0001$\u0001$\u0001$\u0001$\u0001$\u0003"+
		"$\u0251\b$\u0001$\u0001$\u0001$\u0005$\u0256\b$\n$\f$\u0259\t$\u0001%"+
		"\u0001%\u0001%\u0003%\u025e\b%\u0001%\u0001%\u0001%\u0003%\u0263\b%\u0003"+
		"%\u0265\b%\u0001%\u0001%\u0003%\u0269\b%\u0001&\u0001&\u0001&\u0001\'"+
		"\u0001\'\u0003\'\u0270\b\'\u0001\'\u0001\'\u0001\'\u0005\'\u0275\b\'\n"+
		"\'\f\'\u0278\t\'\u0001\'\u0001\'\u0001\'\u0001(\u0001(\u0001(\u0003(\u0280"+
		"\b(\u0001(\u0001(\u0001(\u0001)\u0001)\u0001)\u0001)\u0001)\u0004)\u028a"+
		"\b)\u000b)\f)\u028b\u0001)\u0001)\u0001)\u0001)\u0001*\u0001*\u0004*\u0294"+
		"\b*\u000b*\f*\u0295\u0001*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0004+\u02a0\b+\u000b+\f+\u02a1\u0001+\u0001+\u0003+\u02a6\b+\u0001"+
		"+\u0001+\u0001+\u0001,\u0001,\u0001,\u0001,\u0003,\u02af\b,\u0001,\u0001"+
		",\u0001,\u0005,\u02b4\b,\n,\f,\u02b7\t,\u0001,\u0001,\u0001,\u0001-\u0001"+
		"-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001.\u0001.\u0001"+
		".\u0001.\u0001.\u0004.\u02ca\b.\u000b.\f.\u02cb\u0001/\u0001/\u0001/\u0001"+
		"/\u0001/\u0001/\u00010\u00010\u00011\u00011\u00031\u02d8\b1\u00011\u0001"+
		"1\u00011\u00031\u02dd\b1\u00051\u02df\b1\n1\f1\u02e2\t1\u00012\u00012"+
		"\u00012\u00052\u02e7\b2\n2\f2\u02ea\t2\u00013\u00013\u00013\u00053\u02ef"+
		"\b3\n3\f3\u02f2\t3\u00014\u00034\u02f5\b4\u00014\u00014\u00015\u00015"+
		"\u00015\u00035\u02fc\b5\u00016\u00016\u00016\u00056\u0301\b6\n6\f6\u0304"+
		"\t6\u00017\u00017\u00017\u00037\u0309\b7\u00018\u00018\u00018\u00058\u030e"+
		"\b8\n8\f8\u0311\t8\u00019\u00019\u00019\u00059\u0316\b9\n9\f9\u0319\t"+
		"9\u0001:\u0001:\u0001:\u0001:\u0003:\u031f\b:\u0001;\u0001;\u0001;\u0001"+
		";\u0003;\u0325\b;\u0001<\u0001<\u0001<\u0001<\u0003<\u032b\b<\u0001=\u0001"+
		"=\u0001=\u0001=\u0003=\u0331\b=\u0001>\u0001>\u0001>\u0001>\u0003>\u0337"+
		"\b>\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0001?\u0005?\u0340\b?\n"+
		"?\f?\u0343\t?\u0001@\u0001@\u0001@\u0003@\u0348\b@\u0001A\u0005A\u034b"+
		"\bA\nA\fA\u034e\tA\u0001A\u0001A\u0001B\u0001B\u0001B\u0003B\u0355\bB"+
		"\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001C\u0001D\u0001D\u0001"+
		"D\u0001D\u0001D\u0001D\u0001D\u0001E\u0001E\u0001E\u0005E\u0368\bE\nE"+
		"\fE\u036b\tE\u0001F\u0001F\u0001F\u0001F\u0001F\u0001F\u0005F\u0373\b"+
		"F\nF\fF\u0376\tF\u0001G\u0001G\u0001G\u0001G\u0001G\u0001G\u0001H\u0001"+
		"H\u0001H\u0001I\u0001I\u0001I\u0001I\u0001J\u0001J\u0001J\u0001J\u0001"+
		"J\u0001J\u0001J\u0003J\u038c\bJ\u0001K\u0001K\u0001K\u0001K\u0001K\u0001"+
		"K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0003"+
		"K\u039d\bK\u0001L\u0001L\u0001L\u0001L\u0001M\u0001M\u0001M\u0001N\u0001"+
		"N\u0003N\u03a8\bN\u0001N\u0001N\u0001O\u0001O\u0001P\u0001P\u0001P\u0001"+
		"P\u0001P\u0001Q\u0001Q\u0001Q\u0001Q\u0001Q\u0001R\u0001R\u0001R\u0001"+
		"S\u0001S\u0001S\u0003S\u03be\bS\u0005S\u03c0\bS\nS\fS\u03c3\tS\u0001S"+
		"\u0001S\u0001T\u0001T\u0003T\u03c9\bT\u0001U\u0001U\u0003U\u03cd\bU\u0001"+
		"V\u0001V\u0001V\u0001V\u0001W\u0001W\u0001W\u0003W\u03d6\bW\u0001W\u0001"+
		"W\u0001W\u0003W\u03db\bW\u0001W\u0001W\u0003W\u03df\bW\u0001W\u0001W\u0001"+
		"X\u0001X\u0001X\u0001X\u0001X\u0001X\u0001X\u0001X\u0003X\u03eb\bX\u0001"+
		"X\u0001X\u0001X\u0001X\u0001X\u0005X\u03f2\bX\nX\fX\u03f5\tX\u0001X\u0001"+
		"X\u0001X\u0003X\u03fa\bX\u0001Y\u0001Y\u0001Y\u0001Y\u0001Z\u0001Z\u0001"+
		"Z\u0001Z\u0001Z\u0001Z\u0001[\u0001[\u0001[\u0001[\u0001[\u0001[\u0001"+
		"[\u0001[\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0005\\\u0413\b\\\n\\"+
		"\f\\\u0416\t\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001\\\u0001]\u0001]\u0001"+
		"]\u0001]\u0001]\u0001]\u0001^\u0001^\u0001^\u0004^\u0426\b^\u000b^\f^"+
		"\u0427\u0001_\u0001_\u0001_\u0001_\u0001`\u0005`\u042f\b`\n`\f`\u0432"+
		"\t`\u0001a\u0001a\u0001a\u0001b\u0001b\u0003b\u0439\bb\u0001c\u0001c\u0001"+
		"c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001c\u0001"+
		"c\u0003c\u0448\bc\u0001d\u0001d\u0001d\u0001e\u0001e\u0001e\u0001e\u0001"+
		"e\u0001e\u0001f\u0001f\u0001f\u0001f\u0001g\u0001g\u0001g\u0001g\u0001"+
		"h\u0001h\u0001h\u0001h\u0001i\u0001i\u0001i\u0001i\u0001i\u0001j\u0001"+
		"j\u0005j\u0466\bj\nj\fj\u0469\tj\u0001j\u0001j\u0001k\u0001k\u0001k\u0001"+
		"l\u0001l\u0001l\u0001l\u0001l\u0001l\u0001l\u0001l\u0001l\u0001m\u0001"+
		"m\u0001m\u0001m\u0001m\u0004m\u047e\bm\u000bm\fm\u047f\u0001m\u0001m\u0001"+
		"m\u0001m\u0001n\u0001n\u0004n\u0488\bn\u000bn\fn\u0489\u0001n\u0001n\u0001"+
		"n\u0001o\u0001o\u0001o\u0001o\u0001o\u0001o\u0004o\u0495\bo\u000bo\fo"+
		"\u0496\u0001p\u0001p\u0001p\u0001p\u0001p\u0004p\u049e\bp\u000bp\fp\u049f"+
		"\u0001p\u0001p\u0001p\u0003p\u04a5\bp\u0001p\u0001p\u0001p\u0001q\u0001"+
		"q\u0001q\u0001q\u0001q\u0003q\u04af\bq\u0001q\u0001q\u0001q\u0001q\u0001"+
		"r\u0001r\u0001r\u0001s\u0001s\u0001s\u0001s\u0003s\u04bc\bs\u0001s\u0001"+
		"s\u0003s\u04c0\bs\u0001s\u0001s\u0001s\u0001s\u0003s\u04c6\bs\u0001s\u0001"+
		"s\u0003s\u04ca\bs\u0005s\u04cc\bs\ns\fs\u04cf\ts\u0001s\u0001s\u0001t"+
		"\u0001t\u0001t\u0001t\u0001t\u0001t\u0001u\u0001u\u0001u\u0001u\u0001"+
		"u\u0001u\u0003u\u04df\bu\u0003u\u04e1\bu\u0001v\u0001v\u0001v\u0001v\u0005"+
		"v\u04e7\bv\nv\fv\u04ea\tv\u0003v\u04ec\bv\u0001v\u0001v\u0001v\u0001v"+
		"\u0001v\u0003v\u04f3\bv\u0001w\u0001w\u0001w\u0003w\u04f8\bw\u0001x\u0001"+
		"x\u0003x\u04fc\bx\u0001y\u0001y\u0001y\u0001y\u0001y\u0001z\u0001z\u0001"+
		"z\u0001z\u0001z\u0005z\u0508\bz\nz\fz\u050b\tz\u0003z\u050d\bz\u0001z"+
		"\u0001z\u0001z\u0001z\u0001{\u0001{\u0003{\u0515\b{\u0001|\u0001|\u0003"+
		"|\u0519\b|\u0001|\u0001|\u0001|\u0001}\u0001}\u0003}\u0520\b}\u0001}\u0001"+
		"}\u0001~\u0001~\u0001\u007f\u0001\u007f\u0001\u0080\u0001\u0080\u0001"+
		"\u0080\u0000\u0000\u0081\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012"+
		"\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\"+
		"^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"+
		"\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"+
		"\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"+
		"\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"+
		"\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0000\b\u0002\u0000\u0006"+
		"\u0006hh\u0001\u0000QR\u0001\u0000\t\u0012\u0002\u0000\u0004\u0004#-\u0001"+
		"\u0000/0\u0002\u0000\"\"13\u0002\u0000\b\b\u007f\u007f\u0002\u0000<}\u0080"+
		"\u0080\u0560\u0000\u0102\u0001\u0000\u0000\u0000\u0002\u010a\u0001\u0000"+
		"\u0000\u0000\u0004\u0110\u0001\u0000\u0000\u0000\u0006\u0113\u0001\u0000"+
		"\u0000\u0000\b\u0124\u0001\u0000\u0000\u0000\n\u012f\u0001\u0000\u0000"+
		"\u0000\f\u0135\u0001\u0000\u0000\u0000\u000e\u0137\u0001\u0000\u0000\u0000"+
		"\u0010\u0141\u0001\u0000\u0000\u0000\u0012\u0143\u0001\u0000\u0000\u0000"+
		"\u0014\u0148\u0001\u0000\u0000\u0000\u0016\u014c\u0001\u0000\u0000\u0000"+
		"\u0018\u0152\u0001\u0000\u0000\u0000\u001a\u0167\u0001\u0000\u0000\u0000"+
		"\u001c\u016d\u0001\u0000\u0000\u0000\u001e\u016f\u0001\u0000\u0000\u0000"+
		" \u0182\u0001\u0000\u0000\u0000\"\u0192\u0001\u0000\u0000\u0000$\u01a2"+
		"\u0001\u0000\u0000\u0000&\u01b6\u0001\u0000\u0000\u0000(\u01c5\u0001\u0000"+
		"\u0000\u0000*\u01c7\u0001\u0000\u0000\u0000,\u01cf\u0001\u0000\u0000\u0000"+
		".\u01d5\u0001\u0000\u0000\u00000\u01e3\u0001\u0000\u0000\u00002\u01ed"+
		"\u0001\u0000\u0000\u00004\u01f1\u0001\u0000\u0000\u00006\u01fe\u0001\u0000"+
		"\u0000\u00008\u0205\u0001\u0000\u0000\u0000:\u0207\u0001\u0000\u0000\u0000"+
		"<\u0210\u0001\u0000\u0000\u0000>\u0220\u0001\u0000\u0000\u0000@\u0229"+
		"\u0001\u0000\u0000\u0000B\u0231\u0001\u0000\u0000\u0000D\u0234\u0001\u0000"+
		"\u0000\u0000F\u023e\u0001\u0000\u0000\u0000H\u0250\u0001\u0000\u0000\u0000"+
		"J\u025a\u0001\u0000\u0000\u0000L\u026a\u0001\u0000\u0000\u0000N\u026f"+
		"\u0001\u0000\u0000\u0000P\u027c\u0001\u0000\u0000\u0000R\u0284\u0001\u0000"+
		"\u0000\u0000T\u0293\u0001\u0000\u0000\u0000V\u029a\u0001\u0000\u0000\u0000"+
		"X\u02aa\u0001\u0000\u0000\u0000Z\u02bb\u0001\u0000\u0000\u0000\\\u02c4"+
		"\u0001\u0000\u0000\u0000^\u02cd\u0001\u0000\u0000\u0000`\u02d3\u0001\u0000"+
		"\u0000\u0000b\u02d7\u0001\u0000\u0000\u0000d\u02e3\u0001\u0000\u0000\u0000"+
		"f\u02eb\u0001\u0000\u0000\u0000h\u02f4\u0001\u0000\u0000\u0000j\u02f8"+
		"\u0001\u0000\u0000\u0000l\u02fd\u0001\u0000\u0000\u0000n\u0305\u0001\u0000"+
		"\u0000\u0000p\u030a\u0001\u0000\u0000\u0000r\u0312\u0001\u0000\u0000\u0000"+
		"t\u031a\u0001\u0000\u0000\u0000v\u0320\u0001\u0000\u0000\u0000x\u0326"+
		"\u0001\u0000\u0000\u0000z\u032c\u0001\u0000\u0000\u0000|\u0332\u0001\u0000"+
		"\u0000\u0000~\u0338\u0001\u0000\u0000\u0000\u0080\u0347\u0001\u0000\u0000"+
		"\u0000\u0082\u034c\u0001\u0000\u0000\u0000\u0084\u0354\u0001\u0000\u0000"+
		"\u0000\u0086\u0356\u0001\u0000\u0000\u0000\u0088\u035d\u0001\u0000\u0000"+
		"\u0000\u008a\u0364\u0001\u0000\u0000\u0000\u008c\u036c\u0001\u0000\u0000"+
		"\u0000\u008e\u0377\u0001\u0000\u0000\u0000\u0090\u037d\u0001\u0000\u0000"+
		"\u0000\u0092\u0380\u0001\u0000\u0000\u0000\u0094\u0384\u0001\u0000\u0000"+
		"\u0000\u0096\u039c\u0001\u0000\u0000\u0000\u0098\u039e\u0001\u0000\u0000"+
		"\u0000\u009a\u03a2\u0001\u0000\u0000\u0000\u009c\u03a5\u0001\u0000\u0000"+
		"\u0000\u009e\u03ab\u0001\u0000\u0000\u0000\u00a0\u03ad\u0001\u0000\u0000"+
		"\u0000\u00a2\u03b2\u0001\u0000\u0000\u0000\u00a4\u03b7\u0001\u0000\u0000"+
		"\u0000\u00a6\u03ba\u0001\u0000\u0000\u0000\u00a8\u03c8\u0001\u0000\u0000"+
		"\u0000\u00aa\u03cc\u0001\u0000\u0000\u0000\u00ac\u03ce\u0001\u0000\u0000"+
		"\u0000\u00ae\u03d2\u0001\u0000\u0000\u0000\u00b0\u03f9\u0001\u0000\u0000"+
		"\u0000\u00b2\u03fb\u0001\u0000\u0000\u0000\u00b4\u03ff\u0001\u0000\u0000"+
		"\u0000\u00b6\u0405\u0001\u0000\u0000\u0000\u00b8\u040d\u0001\u0000\u0000"+
		"\u0000\u00ba\u041c\u0001\u0000\u0000\u0000\u00bc\u0422\u0001\u0000\u0000"+
		"\u0000\u00be\u0429\u0001\u0000\u0000\u0000\u00c0\u0430\u0001\u0000\u0000"+
		"\u0000\u00c2\u0433\u0001\u0000\u0000\u0000\u00c4\u0436\u0001\u0000\u0000"+
		"\u0000\u00c6\u0447\u0001\u0000\u0000\u0000\u00c8\u0449\u0001\u0000\u0000"+
		"\u0000\u00ca\u044c\u0001\u0000\u0000\u0000\u00cc\u0452\u0001\u0000\u0000"+
		"\u0000\u00ce\u0456\u0001\u0000\u0000\u0000\u00d0\u045a\u0001\u0000\u0000"+
		"\u0000\u00d2\u045e\u0001\u0000\u0000\u0000\u00d4\u0463\u0001\u0000\u0000"+
		"\u0000\u00d6\u046c\u0001\u0000\u0000\u0000\u00d8\u046f\u0001\u0000\u0000"+
		"\u0000\u00da\u0478\u0001\u0000\u0000\u0000\u00dc\u0487\u0001\u0000\u0000"+
		"\u0000\u00de\u048e\u0001\u0000\u0000\u0000\u00e0\u0498\u0001\u0000\u0000"+
		"\u0000\u00e2\u04a9\u0001\u0000\u0000\u0000\u00e4\u04b4\u0001\u0000\u0000"+
		"\u0000\u00e6\u04b7\u0001\u0000\u0000\u0000\u00e8\u04d2\u0001\u0000\u0000"+
		"\u0000\u00ea\u04e0\u0001\u0000\u0000\u0000\u00ec\u04f2\u0001\u0000\u0000"+
		"\u0000\u00ee\u04f7\u0001\u0000\u0000\u0000\u00f0\u04fb\u0001\u0000\u0000"+
		"\u0000\u00f2\u04fd\u0001\u0000\u0000\u0000\u00f4\u0502\u0001\u0000\u0000"+
		"\u0000\u00f6\u0512\u0001\u0000\u0000\u0000\u00f8\u0518\u0001\u0000\u0000"+
		"\u0000\u00fa\u051d\u0001\u0000\u0000\u0000\u00fc\u0523\u0001\u0000\u0000"+
		"\u0000\u00fe\u0525\u0001\u0000\u0000\u0000\u0100\u0527\u0001\u0000\u0000"+
		"\u0000\u0102\u0103\u0003\u0002\u0001\u0000\u0103\u0104\u0005\u0000\u0000"+
		"\u0001\u0104\u0001\u0001\u0000\u0000\u0000\u0105\u0106\u0005g\u0000\u0000"+
		"\u0106\u0107\u0005f\u0000\u0000\u0107\u0108\u0003\u00fe\u007f\u0000\u0108"+
		"\u0109\u0005\u0001\u0000\u0000\u0109\u010b\u0001\u0000\u0000\u0000\u010a"+
		"\u0105\u0001\u0000\u0000\u0000\u010a\u010b\u0001\u0000\u0000\u0000\u010b"+
		"\u010e\u0001\u0000\u0000\u0000\u010c\u010f\u0003\u0006\u0003\u0000\u010d"+
		"\u010f\u0003\u0004\u0002\u0000\u010e\u010c\u0001\u0000\u0000\u0000\u010e"+
		"\u010d\u0001\u0000\u0000\u0000\u010f\u0003\u0001\u0000\u0000\u0000\u0110"+
		"\u0111\u0003\b\u0004\u0000\u0111\u0112\u0003\n\u0005\u0000\u0112\u0005"+
		"\u0001\u0000\u0000\u0000\u0113\u0114\u0005\u0002\u0000\u0000\u0114\u0115"+
		"\u0005\u0003\u0000\u0000\u0115\u0116\u0005\u0087\u0000\u0000\u0116\u0117"+
		"\u0005\u0004\u0000\u0000\u0117\u0118\u0003\u00fc~\u0000\u0118\u0119\u0005"+
		"\u0001\u0000\u0000\u0119\u011a\u0003\b\u0004\u0000\u011a\u0007\u0001\u0000"+
		"\u0000\u0000\u011b\u011f\u0003\f\u0006\u0000\u011c\u011f\u0003\u000e\u0007"+
		"\u0000\u011d\u011f\u0003\u001e\u000f\u0000\u011e\u011b\u0001\u0000\u0000"+
		"\u0000\u011e\u011c\u0001\u0000\u0000\u0000\u011e\u011d\u0001\u0000\u0000"+
		"\u0000\u011f\u0120\u0001\u0000\u0000\u0000\u0120\u0121\u0005\u0001\u0000"+
		"\u0000\u0121\u0123\u0001\u0000\u0000\u0000\u0122\u011e\u0001\u0000\u0000"+
		"\u0000\u0123\u0126\u0001\u0000\u0000\u0000\u0124\u0122\u0001\u0000\u0000"+
		"\u0000\u0124\u0125\u0001\u0000\u0000\u0000\u0125\u012c\u0001\u0000\u0000"+
		"\u0000\u0126\u0124\u0001\u0000\u0000\u0000\u0127\u0128\u0003\u0010\b\u0000"+
		"\u0128\u0129\u0005\u0001\u0000\u0000\u0129\u012b\u0001\u0000\u0000\u0000"+
		"\u012a\u0127\u0001\u0000\u0000\u0000\u012b\u012e\u0001\u0000\u0000\u0000"+
		"\u012c\u012a\u0001\u0000\u0000\u0000\u012c\u012d\u0001\u0000\u0000\u0000"+
		"\u012d\t\u0001\u0000\u0000\u0000\u012e\u012c\u0001\u0000\u0000\u0000\u012f"+
		"\u0130\u0003\u00c4b\u0000\u0130\u000b\u0001\u0000\u0000\u0000\u0131\u0136"+
		"\u0003\u0012\t\u0000\u0132\u0136\u0003\u0014\n\u0000\u0133\u0136\u0003"+
		"\u0016\u000b\u0000\u0134\u0136\u0003\u0018\f\u0000\u0135\u0131\u0001\u0000"+
		"\u0000\u0000\u0135\u0132\u0001\u0000\u0000\u0000\u0135\u0133\u0001\u0000"+
		"\u0000\u0000\u0135\u0134\u0001\u0000\u0000\u0000\u0136\r\u0001\u0000\u0000"+
		"\u0000\u0137\u0138\u0005n\u0000\u0000\u0138\u0139\u0005\u0003\u0000\u0000"+
		"\u0139\u013a\u0005\u0087\u0000\u0000\u013a\u013b\u0005\u0004\u0000\u0000"+
		"\u013b\u013c\u0003\u00fc~\u0000\u013c\u000f\u0001\u0000\u0000\u0000\u013d"+
		"\u0142\u0003$\u0012\u0000\u013e\u0142\u0003 \u0010\u0000\u013f\u0142\u0003"+
		"&\u0013\u0000\u0140\u0142\u0003\"\u0011\u0000\u0141\u013d\u0001\u0000"+
		"\u0000\u0000\u0141\u013e\u0001\u0000\u0000\u0000\u0141\u013f\u0001\u0000"+
		"\u0000\u0000\u0141\u0140\u0001\u0000\u0000\u0000\u0142\u0011\u0001\u0000"+
		"\u0000\u0000\u0143\u0144\u0005n\u0000\u0000\u0144\u0145\u0005W\u0000\u0000"+
		"\u0145\u0146\u0005P\u0000\u0000\u0146\u0147\u0003\u00fc~\u0000\u0147\u0013"+
		"\u0001\u0000\u0000\u0000\u0148\u0149\u0005n\u0000\u0000\u0149\u014a\u0005"+
		"\u0005\u0000\u0000\u014a\u014b\u0007\u0000\u0000\u0000\u014b\u0015\u0001"+
		"\u0000\u0000\u0000\u014c\u014d\u0005n\u0000\u0000\u014d\u014e\u0005W\u0000"+
		"\u0000\u014e\u014f\u0005A\u0000\u0000\u014f\u0150\u0005H\u0000\u0000\u0150"+
		"\u0151\u0007\u0001\u0000\u0000\u0151\u0017\u0001\u0000\u0000\u0000\u0152"+
		"\u0157\u0005n\u0000\u0000\u0153\u0154\u0005\u0007\u0000\u0000\u0154\u0158"+
		"\u0003\u001a\r\u0000\u0155\u0156\u0005W\u0000\u0000\u0156\u0158\u0005"+
		"\u0007\u0000\u0000\u0157\u0153\u0001\u0000\u0000\u0000\u0157\u0155\u0001"+
		"\u0000\u0000\u0000\u0158\u015f\u0001\u0000\u0000\u0000\u0159\u015a\u0003"+
		"\u001c\u000e\u0000\u015a\u015b\u0005\u0004\u0000\u0000\u015b\u015c\u0003"+
		"\u00fe\u007f\u0000\u015c\u015e\u0001\u0000\u0000\u0000\u015d\u0159\u0001"+
		"\u0000\u0000\u0000\u015e\u0161\u0001\u0000\u0000\u0000\u015f\u015d\u0001"+
		"\u0000\u0000\u0000\u015f\u0160\u0001\u0000\u0000\u0000\u0160\u0019\u0001"+
		"\u0000\u0000\u0000\u0161\u015f\u0001\u0000\u0000\u0000\u0162\u0165\u0005"+
		"\u0087\u0000\u0000\u0163\u0165\u0003\u0100\u0080\u0000\u0164\u0162\u0001"+
		"\u0000\u0000\u0000\u0164\u0163\u0001\u0000\u0000\u0000\u0165\u0166\u0001"+
		"\u0000\u0000\u0000\u0166\u0168\u0005\b\u0000\u0000\u0167\u0164\u0001\u0000"+
		"\u0000\u0000\u0167\u0168\u0001\u0000\u0000\u0000\u0168\u016b\u0001\u0000"+
		"\u0000\u0000\u0169\u016c\u0005\u0087\u0000\u0000\u016a\u016c\u0003\u0100"+
		"\u0080\u0000\u016b\u0169\u0001\u0000\u0000\u0000\u016b\u016a\u0001\u0000"+
		"\u0000\u0000\u016c\u001b\u0001\u0000\u0000\u0000\u016d\u016e\u0007\u0002"+
		"\u0000\u0000\u016e\u001d\u0001\u0000\u0000\u0000\u016f\u0170\u0005\u0013"+
		"\u0000\u0000\u0170\u0174\u0005\u0002\u0000\u0000\u0171\u0172\u0005\u0003"+
		"\u0000\u0000\u0172\u0173\u0005\u0087\u0000\u0000\u0173\u0175\u0005\u0004"+
		"\u0000\u0000\u0174\u0171\u0001\u0000\u0000\u0000\u0174\u0175\u0001\u0000"+
		"\u0000\u0000\u0175\u0176\u0001\u0000\u0000\u0000\u0176\u0180\u0003\u00fc"+
		"~\u0000\u0177\u0178\u0005F\u0000\u0000\u0178\u017d\u0003\u00fc~\u0000"+
		"\u0179\u017a\u0005\u0014\u0000\u0000\u017a\u017c\u0003\u00fc~\u0000\u017b"+
		"\u0179\u0001\u0000\u0000\u0000\u017c\u017f\u0001\u0000\u0000\u0000\u017d"+
		"\u017b\u0001\u0000\u0000\u0000\u017d\u017e\u0001\u0000\u0000\u0000\u017e"+
		"\u0181\u0001\u0000\u0000\u0000\u017f\u017d\u0001\u0000\u0000\u0000\u0180"+
		"\u0177\u0001\u0000\u0000\u0000\u0180\u0181\u0001\u0000\u0000\u0000\u0181"+
		"\u001f\u0001\u0000\u0000\u0000\u0182\u0183\u0005n\u0000\u0000\u0183\u0184"+
		"\u0005q\u0000\u0000\u0184\u0187\u0003\u009aM\u0000\u0185\u0186\u0005E"+
		"\u0000\u0000\u0186\u0188\u0003\u00eau\u0000\u0187\u0185\u0001\u0000\u0000"+
		"\u0000\u0187\u0188\u0001\u0000\u0000\u0000\u0188\u0190\u0001\u0000\u0000"+
		"\u0000\u0189\u018a\u0005\u0015\u0000\u0000\u018a\u0191\u00030\u0018\u0000"+
		"\u018b\u018e\u0005\u0016\u0000\u0000\u018c\u018d\u0005\u0015\u0000\u0000"+
		"\u018d\u018f\u00030\u0018\u0000\u018e\u018c\u0001\u0000\u0000\u0000\u018e"+
		"\u018f\u0001\u0000\u0000\u0000\u018f\u0191\u0001\u0000\u0000\u0000\u0190"+
		"\u0189\u0001\u0000\u0000\u0000\u0190\u018b\u0001\u0000\u0000\u0000\u0191"+
		"!\u0001\u0000\u0000\u0000\u0192\u0193\u0005n\u0000\u0000\u0193\u0194\u0005"+
		"o\u0000\u0000\u0194\u0197\u0005p\u0000\u0000\u0195\u0196\u0005E\u0000"+
		"\u0000\u0196\u0198\u0003\u00eau\u0000\u0197\u0195\u0001\u0000\u0000\u0000"+
		"\u0197\u0198\u0001\u0000\u0000\u0000\u0198\u01a0\u0001\u0000\u0000\u0000"+
		"\u0199\u019a\u0005\u0015\u0000\u0000\u019a\u01a1\u00030\u0018\u0000\u019b"+
		"\u019e\u0005\u0016\u0000\u0000\u019c\u019d\u0005\u0015\u0000\u0000\u019d"+
		"\u019f\u00030\u0018\u0000\u019e\u019c\u0001\u0000\u0000\u0000\u019e\u019f"+
		"\u0001\u0000\u0000\u0000\u019f\u01a1\u0001\u0000\u0000\u0000\u01a0\u0199"+
		"\u0001\u0000\u0000\u0000\u01a0\u019b\u0001\u0000\u0000\u0000\u01a1#\u0001"+
		"\u0000\u0000\u0000\u01a2\u01a3\u0005n\u0000\u0000\u01a3\u01a4\u0005\u0017"+
		"\u0000\u0000\u01a4\u01a5\u0003\u001a\r\u0000\u01a5\u01a7\u0005\u0018\u0000"+
		"\u0000\u01a6\u01a8\u0003*\u0015\u0000\u01a7\u01a6\u0001\u0000\u0000\u0000"+
		"\u01a7\u01a8\u0001\u0000\u0000\u0000\u01a8\u01a9\u0001\u0000\u0000\u0000"+
		"\u01a9\u01ac\u0005\u0019\u0000\u0000\u01aa\u01ab\u0005E\u0000\u0000\u01ab"+
		"\u01ad\u0003\u00eau\u0000\u01ac\u01aa\u0001\u0000\u0000\u0000\u01ac\u01ad"+
		"\u0001\u0000\u0000\u0000\u01ad\u01b4\u0001\u0000\u0000\u0000\u01ae\u01b0"+
		"\u0005\u001a\u0000\u0000\u01af\u01b1\u0003\u00c4b\u0000\u01b0\u01af\u0001"+
		"\u0000\u0000\u0000\u01b0\u01b1\u0001\u0000\u0000\u0000\u01b1\u01b2\u0001"+
		"\u0000\u0000\u0000\u01b2\u01b5\u0005\u001b\u0000\u0000\u01b3\u01b5\u0005"+
		"\u0016\u0000\u0000\u01b4\u01ae\u0001\u0000\u0000\u0000\u01b4\u01b3\u0001"+
		"\u0000\u0000\u0000\u01b5%\u0001\u0000\u0000\u0000\u01b6\u01b7\u0005n\u0000"+
		"\u0000\u01b7\u01b8\u0005k\u0000\u0000\u01b8\u01b9\u0003\u001a\r\u0000"+
		"\u01b9\u01bb\u0005E\u0000\u0000\u01ba\u01bc\u0003(\u0014\u0000\u01bb\u01ba"+
		"\u0001\u0000\u0000\u0000\u01bb\u01bc\u0001\u0000\u0000\u0000\u01bc\u01bd"+
		"\u0001\u0000\u0000\u0000\u01bd\u01be\u00030\u0018\u0000\u01be\'\u0001"+
		"\u0000\u0000\u0000\u01bf\u01c0\u0005\u001c\u0000\u0000\u01c0\u01c6\u0005"+
		"\u001d\u0000\u0000\u01c1\u01c2\u0005\u001c\u0000\u0000\u01c2\u01c6\u0005"+
		"\u001e\u0000\u0000\u01c3\u01c4\u0005{\u0000\u0000\u01c4\u01c6\u0005\u001f"+
		"\u0000\u0000\u01c5\u01bf\u0001\u0000\u0000\u0000\u01c5\u01c1\u0001\u0000"+
		"\u0000\u0000\u01c5\u01c3\u0001\u0000\u0000\u0000\u01c6)\u0001\u0000\u0000"+
		"\u0000\u01c7\u01cc\u0003,\u0016\u0000\u01c8\u01c9\u0005\u0014\u0000\u0000"+
		"\u01c9\u01cb\u0003,\u0016\u0000\u01ca\u01c8\u0001\u0000\u0000\u0000\u01cb"+
		"\u01ce\u0001\u0000\u0000\u0000\u01cc\u01ca\u0001\u0000\u0000\u0000\u01cc"+
		"\u01cd\u0001\u0000\u0000\u0000\u01cd+\u0001\u0000\u0000\u0000\u01ce\u01cc"+
		"\u0001\u0000\u0000\u0000\u01cf\u01d0\u0005 \u0000\u0000\u01d0\u01d3\u0003"+
		"\u001a\r\u0000\u01d1\u01d2\u0005E\u0000\u0000\u01d2\u01d4\u0003\u00ea"+
		"u\u0000\u01d3\u01d1\u0001\u0000\u0000\u0000\u01d3\u01d4\u0001\u0000\u0000"+
		"\u0000\u01d4-\u0001\u0000\u0000\u0000\u01d5\u01da\u00030\u0018\u0000\u01d6"+
		"\u01d7\u0005\u0014\u0000\u0000\u01d7\u01d9\u00030\u0018\u0000\u01d8\u01d6"+
		"\u0001\u0000\u0000\u0000\u01d9\u01dc\u0001\u0000\u0000\u0000\u01da\u01d8"+
		"\u0001\u0000\u0000\u0000\u01da\u01db\u0001\u0000\u0000\u0000\u01db/\u0001"+
		"\u0000\u0000\u0000\u01dc\u01da\u0001\u0000\u0000\u0000\u01dd\u01e4\u0003"+
		"2\u0019\u0000\u01de\u01e4\u00034\u001a\u0000\u01df\u01e4\u0003R)\u0000"+
		"\u01e0\u01e4\u0003V+\u0000\u01e1\u01e4\u0003Z-\u0000\u01e2\u01e4\u0003"+
		"\\.\u0000\u01e3\u01dd\u0001\u0000\u0000\u0000\u01e3\u01de\u0001\u0000"+
		"\u0000\u0000\u01e3\u01df\u0001\u0000\u0000\u0000\u01e3\u01e0\u0001\u0000"+
		"\u0000\u0000\u01e3\u01e1\u0001\u0000\u0000\u0000\u01e3\u01e2\u0001\u0000"+
		"\u0000\u0000\u01e41\u0001\u0000\u0000\u0000\u01e5\u01ee\u0003N\'\u0000"+
		"\u01e6\u01ee\u0003d2\u0000\u01e7\u01ee\u0003\u00b0X\u0000\u01e8\u01ee"+
		"\u0003\u00b2Y\u0000\u01e9\u01ee\u0003\u00b4Z\u0000\u01ea\u01ee\u0003\u00b6"+
		"[\u0000\u01eb\u01ee\u0003\u00b8\\\u0000\u01ec\u01ee\u0003\u00ba]\u0000"+
		"\u01ed\u01e5\u0001\u0000\u0000\u0000\u01ed\u01e6\u0001\u0000\u0000\u0000"+
		"\u01ed\u01e7\u0001\u0000\u0000\u0000\u01ed\u01e8\u0001\u0000\u0000\u0000"+
		"\u01ed\u01e9\u0001\u0000\u0000\u0000\u01ed\u01ea\u0001\u0000\u0000\u0000"+
		"\u01ed\u01eb\u0001\u0000\u0000\u0000\u01ed\u01ec\u0001\u0000\u0000\u0000"+
		"\u01ee3\u0001\u0000\u0000\u0000\u01ef\u01f2\u0003:\u001d\u0000\u01f0\u01f2"+
		"\u0003>\u001f\u0000\u01f1\u01ef\u0001\u0000\u0000\u0000\u01f1\u01f0\u0001"+
		"\u0000\u0000\u0000\u01f2\u01f6\u0001\u0000\u0000\u0000\u01f3\u01f5\u0003"+
		"8\u001c\u0000\u01f4\u01f3\u0001\u0000\u0000\u0000\u01f5\u01f8\u0001\u0000"+
		"\u0000\u0000\u01f6\u01f4\u0001\u0000\u0000\u0000\u01f6\u01f7\u0001\u0000"+
		"\u0000\u0000\u01f7\u01f9\u0001\u0000\u0000\u0000\u01f8\u01f6\u0001\u0000"+
		"\u0000\u0000\u01f9\u01fa\u0005B\u0000\u0000\u01fa\u01fb\u00030\u0018\u0000"+
		"\u01fb5\u0001\u0000\u0000\u0000\u01fc\u01ff\u0003:\u001d\u0000\u01fd\u01ff"+
		"\u0003>\u001f\u0000\u01fe\u01fc\u0001\u0000\u0000\u0000\u01fe\u01fd\u0001"+
		"\u0000\u0000\u0000\u01ff7\u0001\u0000\u0000\u0000\u0200\u0206\u00036\u001b"+
		"\u0000\u0201\u0206\u0003B!\u0000\u0202\u0206\u0003D\"\u0000\u0203\u0206"+
		"\u0003H$\u0000\u0204\u0206\u0003L&\u0000\u0205\u0200\u0001\u0000\u0000"+
		"\u0000\u0205\u0201\u0001\u0000\u0000\u0000\u0205\u0202\u0001\u0000\u0000"+
		"\u0000\u0205\u0203\u0001\u0000\u0000\u0000\u0205\u0204\u0001\u0000\u0000"+
		"\u0000\u02069\u0001\u0000\u0000\u0000\u0207\u0208\u0005<\u0000\u0000\u0208"+
		"\u020d\u0003<\u001e\u0000\u0209\u020a\u0005\u0014\u0000\u0000\u020a\u020c"+
		"\u0003<\u001e\u0000\u020b\u0209\u0001\u0000\u0000\u0000\u020c\u020f\u0001"+
		"\u0000\u0000\u0000\u020d\u020b\u0001\u0000\u0000\u0000\u020d\u020e\u0001"+
		"\u0000\u0000\u0000\u020e;\u0001\u0000\u0000\u0000\u020f\u020d\u0001\u0000"+
		"\u0000\u0000\u0210\u0213\u0003\u009aM\u0000\u0211\u0212\u0005E\u0000\u0000"+
		"\u0212\u0214\u0003\u00eau\u0000\u0213\u0211\u0001\u0000\u0000\u0000\u0213"+
		"\u0214\u0001\u0000\u0000\u0000\u0214\u0217\u0001\u0000\u0000\u0000\u0215"+
		"\u0216\u0005G\u0000\u0000\u0216\u0218\u0005H\u0000\u0000\u0217\u0215\u0001"+
		"\u0000\u0000\u0000\u0217\u0218\u0001\u0000\u0000\u0000\u0218\u021b\u0001"+
		"\u0000\u0000\u0000\u0219\u021a\u0005F\u0000\u0000\u021a\u021c\u0003\u009a"+
		"M\u0000\u021b\u0219\u0001\u0000\u0000\u0000\u021b\u021c\u0001\u0000\u0000"+
		"\u0000\u021c\u021d\u0001\u0000\u0000\u0000\u021d\u021e\u0005D\u0000\u0000"+
		"\u021e\u021f\u00030\u0018\u0000\u021f=\u0001\u0000\u0000\u0000\u0220\u0221"+
		"\u0005=\u0000\u0000\u0221\u0226\u0003@ \u0000\u0222\u0223\u0005\u0014"+
		"\u0000\u0000\u0223\u0225\u0003@ \u0000\u0224\u0222\u0001\u0000\u0000\u0000"+
		"\u0225\u0228\u0001\u0000\u0000\u0000\u0226\u0224\u0001\u0000\u0000\u0000"+
		"\u0226\u0227\u0001\u0000\u0000\u0000\u0227?\u0001\u0000\u0000\u0000\u0228"+
		"\u0226\u0001\u0000\u0000\u0000\u0229\u022c\u0003\u009aM\u0000\u022a\u022b"+
		"\u0005E\u0000\u0000\u022b\u022d\u0003\u00eau\u0000\u022c\u022a\u0001\u0000"+
		"\u0000\u0000\u022c\u022d\u0001\u0000\u0000\u0000\u022d\u022e\u0001\u0000"+
		"\u0000\u0000\u022e\u022f\u0005\u0015\u0000\u0000\u022f\u0230\u00030\u0018"+
		"\u0000\u0230A\u0001\u0000\u0000\u0000\u0231\u0232\u0005>\u0000\u0000\u0232"+
		"\u0233\u00030\u0018\u0000\u0233C\u0001\u0000\u0000\u0000\u0234\u0235\u0005"+
		"?\u0000\u0000\u0235\u0236\u0005@\u0000\u0000\u0236\u023b\u0003F#\u0000"+
		"\u0237\u0238\u0005\u0014\u0000\u0000\u0238\u023a\u0003F#\u0000\u0239\u0237"+
		"\u0001\u0000\u0000\u0000\u023a\u023d\u0001\u0000\u0000\u0000\u023b\u0239"+
		"\u0001\u0000\u0000\u0000\u023b\u023c\u0001\u0000\u0000\u0000\u023cE\u0001"+
		"\u0000\u0000\u0000\u023d\u023b\u0001\u0000\u0000\u0000\u023e\u0245\u0003"+
		"\u009aM\u0000\u023f\u0240\u0005E\u0000\u0000\u0240\u0242\u0003\u00eau"+
		"\u0000\u0241\u023f\u0001\u0000\u0000\u0000\u0241\u0242\u0001\u0000\u0000"+
		"\u0000\u0242\u0243\u0001\u0000\u0000\u0000\u0243\u0244\u0005\u0015\u0000"+
		"\u0000\u0244\u0246\u00030\u0018\u0000\u0245\u0241\u0001\u0000\u0000\u0000"+
		"\u0245\u0246\u0001\u0000\u0000\u0000\u0246\u0249\u0001\u0000\u0000\u0000"+
		"\u0247\u0248\u0005P\u0000\u0000\u0248\u024a\u0003\u00fc~\u0000\u0249\u0247"+
		"\u0001\u0000\u0000\u0000\u0249\u024a\u0001\u0000\u0000\u0000\u024aG\u0001"+
		"\u0000\u0000\u0000\u024b\u024c\u0005A\u0000\u0000\u024c\u0251\u0005@\u0000"+
		"\u0000\u024d\u024e\u0005J\u0000\u0000\u024e\u024f\u0005A\u0000\u0000\u024f"+
		"\u0251\u0005@\u0000\u0000\u0250\u024b\u0001\u0000\u0000\u0000\u0250\u024d"+
		"\u0001\u0000\u0000\u0000\u0251\u0252\u0001\u0000\u0000\u0000\u0252\u0257"+
		"\u0003J%\u0000\u0253\u0254\u0005\u0014\u0000\u0000\u0254\u0256\u0003J"+
		"%\u0000\u0255\u0253\u0001\u0000\u0000\u0000\u0256\u0259\u0001\u0000\u0000"+
		"\u0000\u0257\u0255\u0001\u0000\u0000\u0000\u0257\u0258\u0001\u0000\u0000"+
		"\u0000\u0258I\u0001\u0000\u0000\u0000\u0259\u0257\u0001\u0000\u0000\u0000"+
		"\u025a\u025d\u00030\u0018\u0000\u025b\u025e\u0005K\u0000\u0000\u025c\u025e"+
		"\u0005L\u0000\u0000\u025d\u025b\u0001\u0000\u0000\u0000\u025d\u025c\u0001"+
		"\u0000\u0000\u0000\u025d\u025e\u0001\u0000\u0000\u0000\u025e\u0264\u0001"+
		"\u0000\u0000\u0000\u025f\u0262\u0005H\u0000\u0000\u0260\u0263\u0005Q\u0000"+
		"\u0000\u0261\u0263\u0005R\u0000\u0000\u0262\u0260\u0001\u0000\u0000\u0000"+
		"\u0262\u0261\u0001\u0000\u0000\u0000\u0263\u0265\u0001\u0000\u0000\u0000"+
		"\u0264\u025f\u0001\u0000\u0000\u0000\u0264\u0265\u0001\u0000\u0000\u0000"+
		"\u0265\u0268\u0001\u0000\u0000\u0000\u0266\u0267\u0005P\u0000\u0000\u0267"+
		"\u0269\u0003\u00fc~\u0000\u0268\u0266\u0001\u0000\u0000\u0000\u0268\u0269"+
		"\u0001\u0000\u0000\u0000\u0269K\u0001\u0000\u0000\u0000\u026a\u026b\u0005"+
		"I\u0000\u0000\u026b\u026c\u0003\u009aM\u0000\u026cM\u0001\u0000\u0000"+
		"\u0000\u026d\u0270\u0005M\u0000\u0000\u026e\u0270\u0005N\u0000\u0000\u026f"+
		"\u026d\u0001\u0000\u0000\u0000\u026f\u026e\u0001\u0000\u0000\u0000\u0270"+
		"\u0271\u0001\u0000\u0000\u0000\u0271\u0276\u0003P(\u0000\u0272\u0273\u0005"+
		"\u0014\u0000\u0000\u0273\u0275\u0003P(\u0000\u0274\u0272\u0001\u0000\u0000"+
		"\u0000\u0275\u0278\u0001\u0000\u0000\u0000\u0276\u0274\u0001\u0000\u0000"+
		"\u0000\u0276\u0277\u0001\u0000\u0000\u0000\u0277\u0279\u0001\u0000\u0000"+
		"\u0000\u0278\u0276\u0001\u0000\u0000\u0000\u0279\u027a\u0005O\u0000\u0000"+
		"\u027a\u027b\u00030\u0018\u0000\u027bO\u0001\u0000\u0000\u0000\u027c\u027f"+
		"\u0003\u009aM\u0000\u027d\u027e\u0005E\u0000\u0000\u027e\u0280\u0003\u00ea"+
		"u\u0000\u027f\u027d\u0001\u0000\u0000\u0000\u027f\u0280\u0001\u0000\u0000"+
		"\u0000\u0280\u0281\u0001\u0000\u0000\u0000\u0281\u0282\u0005D\u0000\u0000"+
		"\u0282\u0283\u00030\u0018\u0000\u0283Q\u0001\u0000\u0000\u0000\u0284\u0285"+
		"\u0005S\u0000\u0000\u0285\u0286\u0005\u0018\u0000\u0000\u0286\u0287\u0003"+
		".\u0017\u0000\u0287\u0289\u0005\u0019\u0000\u0000\u0288\u028a\u0003T*"+
		"\u0000\u0289\u0288\u0001\u0000\u0000\u0000\u028a\u028b\u0001\u0000\u0000"+
		"\u0000\u028b\u0289\u0001\u0000\u0000\u0000\u028b\u028c\u0001\u0000\u0000"+
		"\u0000\u028c\u028d\u0001\u0000\u0000\u0000\u028d\u028e\u0005W\u0000\u0000"+
		"\u028e\u028f\u0005B\u0000\u0000\u028f\u0290\u00030\u0018\u0000\u0290S"+
		"\u0001\u0000\u0000\u0000\u0291\u0292\u0005T\u0000\u0000\u0292\u0294\u0003"+
		"0\u0018\u0000\u0293\u0291\u0001\u0000\u0000\u0000\u0294\u0295\u0001\u0000"+
		"\u0000\u0000\u0295\u0293\u0001\u0000\u0000\u0000\u0295\u0296\u0001\u0000"+
		"\u0000\u0000\u0296\u0297\u0001\u0000\u0000\u0000\u0297\u0298\u0005B\u0000"+
		"\u0000\u0298\u0299\u00030\u0018\u0000\u0299U\u0001\u0000\u0000\u0000\u029a"+
		"\u029b\u0005Z\u0000\u0000\u029b\u029c\u0005\u0018\u0000\u0000\u029c\u029d"+
		"\u0003.\u0017\u0000\u029d\u029f\u0005\u0019\u0000\u0000\u029e\u02a0\u0003"+
		"X,\u0000\u029f\u029e\u0001\u0000\u0000\u0000\u02a0\u02a1\u0001\u0000\u0000"+
		"\u0000\u02a1\u029f\u0001\u0000\u0000\u0000\u02a1\u02a2\u0001\u0000\u0000"+
		"\u0000\u02a2\u02a3\u0001\u0000\u0000\u0000\u02a3\u02a5\u0005W\u0000\u0000"+
		"\u02a4\u02a6\u0003\u009aM\u0000\u02a5\u02a4\u0001\u0000\u0000\u0000\u02a5"+
		"\u02a6\u0001\u0000\u0000\u0000\u02a6\u02a7\u0001\u0000\u0000\u0000\u02a7"+
		"\u02a8\u0005B\u0000\u0000\u02a8\u02a9\u00030\u0018\u0000\u02a9W\u0001"+
		"\u0000\u0000\u0000\u02aa\u02ae\u0005T\u0000\u0000\u02ab\u02ac\u0003\u009a"+
		"M\u0000\u02ac\u02ad\u0005E\u0000\u0000\u02ad\u02af\u0001\u0000\u0000\u0000"+
		"\u02ae\u02ab\u0001\u0000\u0000\u0000\u02ae\u02af\u0001\u0000\u0000\u0000"+
		"\u02af\u02b0\u0001\u0000\u0000\u0000\u02b0\u02b5\u0003\u00eau\u0000\u02b1"+
		"\u02b2\u0005!\u0000\u0000\u02b2\u02b4\u0003\u00eau\u0000\u02b3\u02b1\u0001"+
		"\u0000\u0000\u0000\u02b4\u02b7\u0001\u0000\u0000\u0000\u02b5\u02b3\u0001"+
		"\u0000\u0000\u0000\u02b5\u02b6\u0001\u0000\u0000\u0000\u02b6\u02b8\u0001"+
		"\u0000\u0000\u0000\u02b7\u02b5\u0001\u0000\u0000\u0000\u02b8\u02b9\u0005"+
		"B\u0000\u0000\u02b9\u02ba\u00030\u0018\u0000\u02baY\u0001\u0000\u0000"+
		"\u0000\u02bb\u02bc\u0005C\u0000\u0000\u02bc\u02bd\u0005\u0018\u0000\u0000"+
		"\u02bd\u02be\u0003.\u0017\u0000\u02be\u02bf\u0005\u0019\u0000\u0000\u02bf"+
		"\u02c0\u0005X\u0000\u0000\u02c0\u02c1\u00030\u0018\u0000\u02c1\u02c2\u0005"+
		"Y\u0000\u0000\u02c2\u02c3\u00030\u0018\u0000\u02c3[\u0001\u0000\u0000"+
		"\u0000\u02c4\u02c5\u0005U\u0000\u0000\u02c5\u02c6\u0005\u001a\u0000\u0000"+
		"\u02c6\u02c7\u0003.\u0017\u0000\u02c7\u02c9\u0005\u001b\u0000\u0000\u02c8"+
		"\u02ca\u0003^/\u0000\u02c9\u02c8\u0001\u0000\u0000\u0000\u02ca\u02cb\u0001"+
		"\u0000\u0000\u0000\u02cb\u02c9\u0001\u0000\u0000\u0000\u02cb\u02cc\u0001"+
		"\u0000\u0000\u0000\u02cc]\u0001\u0000\u0000\u0000\u02cd\u02ce\u0005V\u0000"+
		"\u0000\u02ce\u02cf\u0003b1\u0000\u02cf\u02d0\u0005\u001a\u0000\u0000\u02d0"+
		"\u02d1\u0003.\u0017\u0000\u02d1\u02d2\u0005\u001b\u0000\u0000\u02d2_\u0001"+
		"\u0000\u0000\u0000\u02d3\u02d4\u00030\u0018\u0000\u02d4a\u0001\u0000\u0000"+
		"\u0000\u02d5\u02d8\u0005\"\u0000\u0000\u02d6\u02d8\u0003\u001a\r\u0000"+
		"\u02d7\u02d5\u0001\u0000\u0000\u0000\u02d7\u02d6\u0001\u0000\u0000\u0000"+
		"\u02d8\u02e0\u0001\u0000\u0000\u0000\u02d9\u02dc\u0005!\u0000\u0000\u02da"+
		"\u02dd\u0005\"\u0000\u0000\u02db\u02dd\u0003\u001a\r\u0000\u02dc\u02da"+
		"\u0001\u0000\u0000\u0000\u02dc\u02db\u0001\u0000\u0000\u0000\u02dd\u02df"+
		"\u0001\u0000\u0000\u0000\u02de\u02d9\u0001\u0000\u0000\u0000\u02df\u02e2"+
		"\u0001\u0000\u0000\u0000\u02e0\u02de\u0001\u0000\u0000\u0000\u02e0\u02e1"+
		"\u0001\u0000\u0000\u0000\u02e1c\u0001\u0000\u0000\u0000\u02e2\u02e0\u0001"+
		"\u0000\u0000\u0000\u02e3\u02e8\u0003f3\u0000\u02e4\u02e5\u0005[\u0000"+
		"\u0000\u02e5\u02e7\u0003f3\u0000\u02e6\u02e4\u0001\u0000\u0000\u0000\u02e7"+
		"\u02ea\u0001\u0000\u0000\u0000\u02e8\u02e6\u0001\u0000\u0000\u0000\u02e8"+
		"\u02e9\u0001\u0000\u0000\u0000\u02e9e\u0001\u0000\u0000\u0000\u02ea\u02e8"+
		"\u0001\u0000\u0000\u0000\u02eb\u02f0\u0003h4\u0000\u02ec\u02ed\u0005\\"+
		"\u0000\u0000\u02ed\u02ef\u0003h4\u0000\u02ee\u02ec\u0001\u0000\u0000\u0000"+
		"\u02ef\u02f2\u0001\u0000\u0000\u0000\u02f0\u02ee\u0001\u0000\u0000\u0000"+
		"\u02f0\u02f1\u0001\u0000\u0000\u0000\u02f1g\u0001\u0000\u0000\u0000\u02f2"+
		"\u02f0\u0001\u0000\u0000\u0000\u02f3\u02f5\u0005]\u0000\u0000\u02f4\u02f3"+
		"\u0001\u0000\u0000\u0000\u02f4\u02f5\u0001\u0000\u0000\u0000\u02f5\u02f6"+
		"\u0001\u0000\u0000\u0000\u02f6\u02f7\u0003j5\u0000\u02f7i\u0001\u0000"+
		"\u0000\u0000\u02f8\u02fb\u0003l6\u0000\u02f9\u02fa\u0007\u0003\u0000\u0000"+
		"\u02fa\u02fc\u0003l6\u0000\u02fb\u02f9\u0001\u0000\u0000\u0000\u02fb\u02fc"+
		"\u0001\u0000\u0000\u0000\u02fck\u0001\u0000\u0000\u0000\u02fd\u0302\u0003"+
		"n7\u0000\u02fe\u02ff\u0005.\u0000\u0000\u02ff\u0301\u0003n7\u0000\u0300"+
		"\u02fe\u0001\u0000\u0000\u0000\u0301\u0304\u0001\u0000\u0000\u0000\u0302"+
		"\u0300\u0001\u0000\u0000\u0000\u0302\u0303\u0001\u0000\u0000\u0000\u0303"+
		"m\u0001\u0000\u0000\u0000\u0304\u0302\u0001\u0000\u0000\u0000\u0305\u0308"+
		"\u0003p8\u0000\u0306\u0307\u0005^\u0000\u0000\u0307\u0309\u0003p8\u0000"+
		"\u0308\u0306\u0001\u0000\u0000\u0000\u0308\u0309\u0001\u0000\u0000\u0000"+
		"\u0309o\u0001\u0000\u0000\u0000\u030a\u030f\u0003r9\u0000\u030b\u030c"+
		"\u0007\u0004\u0000\u0000\u030c\u030e\u0003r9\u0000\u030d\u030b\u0001\u0000"+
		"\u0000\u0000\u030e\u0311\u0001\u0000\u0000\u0000\u030f\u030d\u0001\u0000"+
		"\u0000\u0000\u030f\u0310\u0001\u0000\u0000\u0000\u0310q\u0001\u0000\u0000"+
		"\u0000\u0311\u030f\u0001\u0000\u0000\u0000\u0312\u0317\u0003t:\u0000\u0313"+
		"\u0314\u0007\u0005\u0000\u0000\u0314\u0316\u0003t:\u0000\u0315\u0313\u0001"+
		"\u0000\u0000\u0000\u0316\u0319\u0001\u0000\u0000\u0000\u0317\u0315\u0001"+
		"\u0000\u0000\u0000\u0317\u0318\u0001\u0000\u0000\u0000\u0318s\u0001\u0000"+
		"\u0000\u0000\u0319\u0317\u0001\u0000\u0000\u0000\u031a\u031e\u0003v;\u0000"+
		"\u031b\u031c\u0005_\u0000\u0000\u031c\u031d\u0005`\u0000\u0000\u031d\u031f"+
		"\u0003\u00eau\u0000\u031e\u031b\u0001\u0000\u0000\u0000\u031e\u031f\u0001"+
		"\u0000\u0000\u0000\u031fu\u0001\u0000\u0000\u0000\u0320\u0324\u0003x<"+
		"\u0000\u0321\u0322\u0005b\u0000\u0000\u0322\u0323\u0005a\u0000\u0000\u0323"+
		"\u0325\u0003\u00eau\u0000\u0324\u0321\u0001\u0000\u0000\u0000\u0324\u0325"+
		"\u0001\u0000\u0000\u0000\u0325w\u0001\u0000\u0000\u0000\u0326\u032a\u0003"+
		"z=\u0000\u0327\u0328\u0005c\u0000\u0000\u0328\u0329\u0005E\u0000\u0000"+
		"\u0329\u032b\u0003\u00eau\u0000\u032a\u0327\u0001\u0000\u0000\u0000\u032a"+
		"\u032b\u0001\u0000\u0000\u0000\u032by\u0001\u0000\u0000\u0000\u032c\u0330"+
		"\u0003|>\u0000\u032d\u032e\u0005e\u0000\u0000\u032e\u032f\u0005E\u0000"+
		"\u0000\u032f\u0331\u0003\u00f6{\u0000\u0330\u032d\u0001\u0000\u0000\u0000"+
		"\u0330\u0331\u0001\u0000\u0000\u0000\u0331{\u0001\u0000\u0000\u0000\u0332"+
		"\u0336\u0003~?\u0000\u0333\u0334\u0005d\u0000\u0000\u0334\u0335\u0005"+
		"E\u0000\u0000\u0335\u0337\u0003\u00f6{\u0000\u0336\u0333\u0001\u0000\u0000"+
		"\u0000\u0336\u0337\u0001\u0000\u0000\u0000\u0337}\u0001\u0000\u0000\u0000"+
		"\u0338\u0341\u0003\u0082A\u0000\u0339\u033a\u0005\u0004\u0000\u0000\u033a"+
		"\u033b\u0005,\u0000\u0000\u033b\u033c\u0001\u0000\u0000\u0000\u033c\u033d"+
		"\u0003\u0080@\u0000\u033d\u033e\u0003\u00a6S\u0000\u033e\u0340\u0001\u0000"+
		"\u0000\u0000\u033f\u0339\u0001\u0000\u0000\u0000\u0340\u0343\u0001\u0000"+
		"\u0000\u0000\u0341\u033f\u0001\u0000\u0000\u0000\u0341\u0342\u0001\u0000"+
		"\u0000\u0000\u0342\u007f\u0001\u0000\u0000\u0000\u0343\u0341\u0001\u0000"+
		"\u0000\u0000\u0344\u0348\u0003\u001a\r\u0000\u0345\u0348\u0003\u009aM"+
		"\u0000\u0346\u0348\u0003\u009cN\u0000\u0347\u0344\u0001\u0000\u0000\u0000"+
		"\u0347\u0345\u0001\u0000\u0000\u0000\u0347\u0346\u0001\u0000\u0000\u0000"+
		"\u0348\u0081\u0001\u0000\u0000\u0000\u0349\u034b\u0007\u0004\u0000\u0000"+
		"\u034a\u0349\u0001\u0000\u0000\u0000\u034b\u034e\u0001\u0000\u0000\u0000"+
		"\u034c\u034a\u0001\u0000\u0000\u0000\u034c\u034d\u0001\u0000\u0000\u0000"+
		"\u034d\u034f\u0001\u0000\u0000\u0000\u034e\u034c\u0001\u0000\u0000\u0000"+
		"\u034f\u0350\u0003\u0084B\u0000\u0350\u0083\u0001\u0000\u0000\u0000\u0351"+
		"\u0355\u0003\u008aE\u0000\u0352\u0355\u0003\u0086C\u0000\u0353\u0355\u0003"+
		"\u0088D\u0000\u0354\u0351\u0001\u0000\u0000\u0000\u0354\u0352\u0001\u0000"+
		"\u0000\u0000\u0354\u0353\u0001\u0000\u0000\u0000\u0355\u0085\u0001\u0000"+
		"\u0000\u0000\u0356\u0357\u0005l\u0000\u0000\u0357\u0358\u0005k\u0000\u0000"+
		"\u0358\u0359\u0003\u00eau\u0000\u0359\u035a\u0005\u001a\u0000\u0000\u035a"+
		"\u035b\u0003.\u0017\u0000\u035b\u035c\u0005\u001b\u0000\u0000\u035c\u0087"+
		"\u0001\u0000\u0000\u0000\u035d\u035e\u0005m\u0000\u0000\u035e\u035f\u0005"+
		"k\u0000\u0000\u035f\u0360\u0003\u00eau\u0000\u0360\u0361\u0005\u001a\u0000"+
		"\u0000\u0361\u0362\u0003.\u0017\u0000\u0362\u0363\u0005\u001b\u0000\u0000"+
		"\u0363\u0089\u0001\u0000\u0000\u0000\u0364\u0369\u0003\u008cF\u0000\u0365"+
		"\u0366\u00054\u0000\u0000\u0366\u0368\u0003\u008cF\u0000\u0367\u0365\u0001"+
		"\u0000\u0000\u0000\u0368\u036b\u0001\u0000\u0000\u0000\u0369\u0367\u0001"+
		"\u0000\u0000\u0000\u0369\u036a\u0001\u0000\u0000\u0000\u036a\u008b\u0001"+
		"\u0000\u0000\u0000\u036b\u0369\u0001\u0000\u0000\u0000\u036c\u0374\u0003"+
		"\u0096K\u0000\u036d\u0373\u0003\u008eG\u0000\u036e\u0373\u0003\u0092I"+
		"\u0000\u036f\u0373\u0003\u0094J\u0000\u0370\u0373\u0003\u0090H\u0000\u0371"+
		"\u0373\u0003\u00a6S\u0000\u0372\u036d\u0001\u0000\u0000\u0000\u0372\u036e"+
		"\u0001\u0000\u0000\u0000\u0372\u036f\u0001\u0000\u0000\u0000\u0372\u0370"+
		"\u0001\u0000\u0000\u0000\u0372\u0371\u0001\u0000\u0000\u0000\u0373\u0376"+
		"\u0001\u0000\u0000\u0000\u0374\u0372\u0001\u0000\u0000\u0000\u0374\u0375"+
		"\u0001\u0000\u0000\u0000\u0375\u008d\u0001\u0000\u0000\u0000\u0376\u0374"+
		"\u0001\u0000\u0000\u0000\u0377\u0378\u00055\u0000\u0000\u0378\u0379\u0005"+
		"5\u0000\u0000\u0379\u037a\u0003.\u0017\u0000\u037a\u037b\u00056\u0000"+
		"\u0000\u037b\u037c\u00056\u0000\u0000\u037c\u008f\u0001\u0000\u0000\u0000"+
		"\u037d\u037e\u00055\u0000\u0000\u037e\u037f\u00056\u0000\u0000\u037f\u0091"+
		"\u0001\u0000\u0000\u0000\u0380\u0381\u00055\u0000\u0000\u0381\u0382\u0003"+
		".\u0017\u0000\u0382\u0383\u00056\u0000\u0000\u0383\u0093\u0001\u0000\u0000"+
		"\u0000\u0384\u038b\u00057\u0000\u0000\u0385\u038c\u0003\u0100\u0080\u0000"+
		"\u0386\u038c\u0003\u00fe\u007f\u0000\u0387\u038c\u0005\u0087\u0000\u0000"+
		"\u0388\u038c\u0003\u009cN\u0000\u0389\u038c\u0003\u009aM\u0000\u038a\u038c"+
		"\u0003\u009eO\u0000\u038b\u0385\u0001\u0000\u0000\u0000\u038b\u0386\u0001"+
		"\u0000\u0000\u0000\u038b\u0387\u0001\u0000\u0000\u0000\u038b\u0388\u0001"+
		"\u0000\u0000\u0000\u038b\u0389\u0001\u0000\u0000\u0000\u038b\u038a\u0001"+
		"\u0000\u0000\u0000\u038c\u0095\u0001\u0000\u0000\u0000\u038d\u039d\u0005"+
		"\u0080\u0000\u0000\u038e\u039d\u0005i\u0000\u0000\u038f\u039d\u0005j\u0000"+
		"\u0000\u0390\u039d\u0005\u0081\u0000\u0000\u0391\u039d\u0003\u00fe\u007f"+
		"\u0000\u0392\u039d\u0003\u009aM\u0000\u0393\u039d\u0003\u009cN\u0000\u0394"+
		"\u039d\u0003\u009eO\u0000\u0395\u039d\u0003\u00ecv\u0000\u0396\u039d\u0003"+
		"\u00a4R\u0000\u0397\u039d\u0003\u00a0P\u0000\u0398\u039d\u0003\u00a2Q"+
		"\u0000\u0399\u039d\u0003\u00fa}\u0000\u039a\u039d\u0003\u00aaU\u0000\u039b"+
		"\u039d\u0003\u0098L\u0000\u039c\u038d\u0001\u0000\u0000\u0000\u039c\u038e"+
		"\u0001\u0000\u0000\u0000\u039c\u038f\u0001\u0000\u0000\u0000\u039c\u0390"+
		"\u0001\u0000\u0000\u0000\u039c\u0391\u0001\u0000\u0000\u0000\u039c\u0392"+
		"\u0001\u0000\u0000\u0000\u039c\u0393\u0001\u0000\u0000\u0000\u039c\u0394"+
		"\u0001\u0000\u0000\u0000\u039c\u0395\u0001\u0000\u0000\u0000\u039c\u0396"+
		"\u0001\u0000\u0000\u0000\u039c\u0397\u0001\u0000\u0000\u0000\u039c\u0398"+
		"\u0001\u0000\u0000\u0000\u039c\u0399\u0001\u0000\u0000\u0000\u039c\u039a"+
		"\u0001\u0000\u0000\u0000\u039c\u039b\u0001\u0000\u0000\u0000\u039d\u0097"+
		"\u0001\u0000\u0000\u0000\u039e\u039f\u0005\u001a\u0000\u0000\u039f\u03a0"+
		"\u0003\u00c2a\u0000\u03a0\u03a1\u0005\u001b\u0000\u0000\u03a1\u0099\u0001"+
		"\u0000\u0000\u0000\u03a2\u03a3\u0005 \u0000\u0000\u03a3\u03a4\u0003\u001a"+
		"\r\u0000\u03a4\u009b\u0001\u0000\u0000\u0000\u03a5\u03a7\u0005\u0018\u0000"+
		"\u0000\u03a6\u03a8\u0003.\u0017\u0000\u03a7\u03a6\u0001\u0000\u0000\u0000"+
		"\u03a7\u03a8\u0001\u0000\u0000\u0000\u03a8\u03a9\u0001\u0000\u0000\u0000"+
		"\u03a9\u03aa\u0005\u0019\u0000\u0000\u03aa\u009d\u0001\u0000\u0000\u0000"+
		"\u03ab\u03ac\u00058\u0000\u0000\u03ac\u009f\u0001\u0000\u0000\u0000\u03ad"+
		"\u03ae\u0005\u0006\u0000\u0000\u03ae\u03af\u0005\u001a\u0000\u0000\u03af"+
		"\u03b0\u0003.\u0017\u0000\u03b0\u03b1\u0005\u001b\u0000\u0000\u03b1\u00a1"+
		"\u0001\u0000\u0000\u0000\u03b2\u03b3\u0005h\u0000\u0000\u03b3\u03b4\u0005"+
		"\u001a\u0000\u0000\u03b4\u03b5\u0003.\u0017\u0000\u03b5\u03b6\u0005\u001b"+
		"\u0000\u0000\u03b6\u00a3\u0001\u0000\u0000\u0000\u03b7\u03b8\u0003\u001a"+
		"\r\u0000\u03b8\u03b9\u0003\u00a6S\u0000\u03b9\u00a5\u0001\u0000\u0000"+
		"\u0000\u03ba\u03c1\u0005\u0018\u0000\u0000\u03bb\u03bd\u0003\u00a8T\u0000"+
		"\u03bc\u03be\u0005\u0014\u0000\u0000\u03bd\u03bc\u0001\u0000\u0000\u0000"+
		"\u03bd\u03be\u0001\u0000\u0000\u0000\u03be\u03c0\u0001\u0000\u0000\u0000"+
		"\u03bf\u03bb\u0001\u0000\u0000\u0000\u03c0\u03c3\u0001\u0000\u0000\u0000"+
		"\u03c1\u03bf\u0001\u0000\u0000\u0000\u03c1\u03c2\u0001\u0000\u0000\u0000"+
		"\u03c2\u03c4\u0001\u0000\u0000\u0000\u03c3\u03c1\u0001\u0000\u0000\u0000"+
		"\u03c4\u03c5\u0005\u0019\u0000\u0000\u03c5\u00a7\u0001\u0000\u0000\u0000"+
		"\u03c6\u03c9\u00030\u0018\u0000\u03c7\u03c9\u0005\u007f\u0000\u0000\u03c8"+
		"\u03c6\u0001\u0000\u0000\u0000\u03c8\u03c7\u0001\u0000\u0000\u0000\u03c9"+
		"\u00a9\u0001\u0000\u0000\u0000\u03ca\u03cd\u0003\u00acV\u0000\u03cb\u03cd"+
		"\u0003\u00aeW\u0000\u03cc\u03ca\u0001\u0000\u0000\u0000\u03cc\u03cb\u0001"+
		"\u0000\u0000\u0000\u03cd\u00ab\u0001\u0000\u0000\u0000\u03ce\u03cf\u0003"+
		"\u001a\r\u0000\u03cf\u03d0\u00059\u0000\u0000\u03d0\u03d1\u0005\u0081"+
		"\u0000\u0000\u03d1\u00ad\u0001\u0000\u0000\u0000\u03d2\u03d3\u0005\u0017"+
		"\u0000\u0000\u03d3\u03d5\u0005\u0018\u0000\u0000\u03d4\u03d6\u0003*\u0015"+
		"\u0000\u03d5\u03d4\u0001\u0000\u0000\u0000\u03d5\u03d6\u0001\u0000\u0000"+
		"\u0000\u03d6\u03d7\u0001\u0000\u0000\u0000\u03d7\u03da\u0005\u0019\u0000"+
		"\u0000\u03d8\u03d9\u0005E\u0000\u0000\u03d9\u03db\u0003\u00eau\u0000\u03da"+
		"\u03d8\u0001\u0000\u0000\u0000\u03da\u03db\u0001\u0000\u0000\u0000\u03db"+
		"\u03dc\u0001\u0000\u0000\u0000\u03dc\u03de\u0005\u001a\u0000\u0000\u03dd"+
		"\u03df\u0003.\u0017\u0000\u03de\u03dd\u0001\u0000\u0000\u0000\u03de\u03df"+
		"\u0001\u0000\u0000\u0000\u03df\u03e0\u0001\u0000\u0000\u0000\u03e0\u03e1"+
		"\u0005\u001b\u0000\u0000\u03e1\u00af\u0001\u0000\u0000\u0000\u03e2\u03e3"+
		"\u0005r\u0000\u0000\u03e3\u03e4\u0005{\u0000\u0000\u03e4\u03e5\u00030"+
		"\u0018\u0000\u03e5\u03e6\u0005y\u0000\u0000\u03e6\u03ea\u00030\u0018\u0000"+
		"\u03e7\u03e8\u0005F\u0000\u0000\u03e8\u03e9\u0005}\u0000\u0000\u03e9\u03eb"+
		"\u00030\u0018\u0000\u03ea\u03e7\u0001\u0000\u0000\u0000\u03ea\u03eb\u0001"+
		"\u0000\u0000\u0000\u03eb\u03fa\u0001\u0000\u0000\u0000\u03ec\u03ed\u0005"+
		"r\u0000\u0000\u03ed\u03ee\u0005{\u0000\u0000\u03ee\u03f3\u0003\u00f8|"+
		"\u0000\u03ef\u03f0\u0005\u0014\u0000\u0000\u03f0\u03f2\u0003\u00f8|\u0000"+
		"\u03f1\u03ef\u0001\u0000\u0000\u0000\u03f2\u03f5\u0001\u0000\u0000\u0000"+
		"\u03f3\u03f1\u0001\u0000\u0000\u0000\u03f3\u03f4\u0001\u0000\u0000\u0000"+
		"\u03f4\u03f6\u0001\u0000\u0000\u0000\u03f5\u03f3\u0001\u0000\u0000\u0000"+
		"\u03f6\u03f7\u0005y\u0000\u0000\u03f7\u03f8\u00030\u0018\u0000\u03f8\u03fa"+
		"\u0001\u0000\u0000\u0000\u03f9\u03e2\u0001\u0000\u0000\u0000\u03f9\u03ec"+
		"\u0001\u0000\u0000\u0000\u03fa\u00b1\u0001\u0000\u0000\u0000\u03fb\u03fc"+
		"\u0005s\u0000\u0000\u03fc\u03fd\u0005{\u0000\u0000\u03fd\u03fe\u0003\u00bc"+
		"^\u0000\u03fe\u00b3\u0001\u0000\u0000\u0000\u03ff\u0400\u0005t\u0000\u0000"+
		"\u0400\u0401\u0005{\u0000\u0000\u0401\u0402\u0003\u00bc^\u0000\u0402\u0403"+
		"\u0005E\u0000\u0000\u0403\u0404\u00030\u0018\u0000\u0404\u00b5\u0001\u0000"+
		"\u0000\u0000\u0405\u0406\u0005u\u0000\u0000\u0406\u0407\u0005{\u0000\u0000"+
		"\u0407\u0408\u0005z\u0000\u0000\u0408\u0409\u0005`\u0000\u0000\u0409\u040a"+
		"\u0003\u00bc^\u0000\u040a\u040b\u0005|\u0000\u0000\u040b\u040c\u00030"+
		"\u0018\u0000\u040c\u00b7\u0001\u0000\u0000\u0000\u040d\u040e\u0005v\u0000"+
		"\u0000\u040e\u040f\u0005{\u0000\u0000\u040f\u0414\u0003\u00be_\u0000\u0410"+
		"\u0411\u0005\u0014\u0000\u0000\u0411\u0413\u0003\u00be_\u0000\u0412\u0410"+
		"\u0001\u0000\u0000\u0000\u0413\u0416\u0001\u0000\u0000\u0000\u0414\u0412"+
		"\u0001\u0000\u0000\u0000\u0414\u0415\u0001\u0000\u0000\u0000\u0415\u0417"+
		"\u0001\u0000\u0000\u0000\u0416\u0414\u0001\u0000\u0000\u0000\u0417\u0418"+
		"\u0005w\u0000\u0000\u0418\u0419\u00030\u0018\u0000\u0419\u041a\u0005B"+
		"\u0000\u0000\u041a\u041b\u00030\u0018\u0000\u041b\u00b9\u0001\u0000\u0000"+
		"\u0000\u041c\u041d\u0005x\u0000\u0000\u041d\u041e\u0005{\u0000\u0000\u041e"+
		"\u041f\u00030\u0018\u0000\u041f\u0420\u0005y\u0000\u0000\u0420\u0421\u0003"+
		"0\u0018\u0000\u0421\u00bb\u0001\u0000\u0000\u0000\u0422\u0425\u0003\u0096"+
		"K\u0000\u0423\u0426\u0003\u008eG\u0000\u0424\u0426\u0003\u0094J\u0000"+
		"\u0425\u0423\u0001\u0000\u0000\u0000\u0425\u0424\u0001\u0000\u0000\u0000"+
		"\u0426\u0427\u0001\u0000\u0000\u0000\u0427\u0425\u0001\u0000\u0000\u0000"+
		"\u0427\u0428\u0001\u0000\u0000\u0000\u0428\u00bd\u0001\u0000\u0000\u0000"+
		"\u0429\u042a\u0003\u009aM\u0000\u042a\u042b\u0005\u0015\u0000\u0000\u042b"+
		"\u042c\u00030\u0018\u0000\u042c\u00bf\u0001\u0000\u0000\u0000\u042d\u042f"+
		"\u0003\u00c6c\u0000\u042e\u042d\u0001\u0000\u0000\u0000\u042f\u0432\u0001"+
		"\u0000\u0000\u0000\u0430\u042e\u0001\u0000\u0000\u0000\u0430\u0431\u0001"+
		"\u0000\u0000\u0000\u0431\u00c1\u0001\u0000\u0000\u0000\u0432\u0430\u0001"+
		"\u0000\u0000\u0000\u0433\u0434\u0003\u00c0`\u0000\u0434\u0435\u0003.\u0017"+
		"\u0000\u0435\u00c3\u0001\u0000\u0000\u0000\u0436\u0438\u0003\u00c0`\u0000"+
		"\u0437\u0439\u0003.\u0017\u0000\u0438\u0437\u0001\u0000\u0000\u0000\u0438"+
		"\u0439\u0001\u0000\u0000\u0000\u0439\u00c5\u0001\u0000\u0000\u0000\u043a"+
		"\u0448\u0003\u00c8d\u0000\u043b\u0448\u0003\u00cae\u0000\u043c\u0448\u0003"+
		"\u00ccf\u0000\u043d\u0448\u0003\u00ceg\u0000\u043e\u0448\u0003\u00d0h"+
		"\u0000\u043f\u0448\u0003\u00d2i\u0000\u0440\u0448\u0003\u00d4j\u0000\u0441"+
		"\u0448\u0003\u00d8l\u0000\u0442\u0448\u0003\u00dam\u0000\u0443\u0448\u0003"+
		"\u00deo\u0000\u0444\u0448\u0003\u00e0p\u0000\u0445\u0448\u0003\u00e6s"+
		"\u0000\u0446\u0448\u0003\u00e8t\u0000\u0447\u043a\u0001\u0000\u0000\u0000"+
		"\u0447\u043b\u0001\u0000\u0000\u0000\u0447\u043c\u0001\u0000\u0000\u0000"+
		"\u0447\u043d\u0001\u0000\u0000\u0000\u0447\u043e\u0001\u0000\u0000\u0000"+
		"\u0447\u043f\u0001\u0000\u0000\u0000\u0447\u0440\u0001\u0000\u0000\u0000"+
		"\u0447\u0441\u0001\u0000\u0000\u0000\u0447\u0442\u0001\u0000\u0000\u0000"+
		"\u0447\u0443\u0001\u0000\u0000\u0000\u0447\u0444\u0001\u0000\u0000\u0000"+
		"\u0447\u0445\u0001\u0000\u0000\u0000\u0447\u0446\u0001\u0000\u0000\u0000"+
		"\u0448\u00c7\u0001\u0000\u0000\u0000\u0449\u044a\u00032\u0019\u0000\u044a"+
		"\u044b\u0005\u0001\u0000\u0000\u044b\u00c9\u0001\u0000\u0000\u0000\u044c"+
		"\u044d\u0005 \u0000\u0000\u044d\u044e\u0003\u001a\r\u0000\u044e\u044f"+
		"\u0005\u0015\u0000\u0000\u044f\u0450\u00030\u0018\u0000\u0450\u0451\u0005"+
		"\u0001\u0000\u0000\u0451\u00cb\u0001\u0000\u0000\u0000\u0452\u0453\u0005"+
		"\u001a\u0000\u0000\u0453\u0454\u0003\u00c0`\u0000\u0454\u0455\u0005\u001b"+
		"\u0000\u0000\u0455\u00cd\u0001\u0000\u0000\u0000\u0456\u0457\u0005\u008a"+
		"\u0000\u0000\u0457\u0458\u0005\u008b\u0000\u0000\u0458\u0459\u0005\u0001"+
		"\u0000\u0000\u0459\u00cf\u0001\u0000\u0000\u0000\u045a\u045b\u0005\u008c"+
		"\u0000\u0000\u045b\u045c\u0005\u008b\u0000\u0000\u045c\u045d\u0005\u0001"+
		"\u0000\u0000\u045d\u00d1\u0001\u0000\u0000\u0000\u045e\u045f\u0005\u008d"+
		"\u0000\u0000\u045f\u0460\u0005\u008e\u0000\u0000\u0460\u0461\u00030\u0018"+
		"\u0000\u0461\u0462\u0005\u0001\u0000\u0000\u0462\u00d3\u0001\u0000\u0000"+
		"\u0000\u0463\u0467\u00036\u001b\u0000\u0464\u0466\u00038\u001c\u0000\u0465"+
		"\u0464\u0001\u0000\u0000\u0000\u0466\u0469\u0001\u0000\u0000\u0000\u0467"+
		"\u0465\u0001\u0000\u0000\u0000\u0467\u0468\u0001\u0000\u0000\u0000\u0468"+
		"\u046a\u0001\u0000\u0000\u0000\u0469\u0467\u0001\u0000\u0000\u0000\u046a"+
		"\u046b\u0003\u00d6k\u0000\u046b\u00d5\u0001\u0000\u0000\u0000\u046c\u046d"+
		"\u0005B\u0000\u0000\u046d\u046e\u0003\u00c6c\u0000\u046e\u00d7\u0001\u0000"+
		"\u0000\u0000\u046f\u0470\u0005C\u0000\u0000\u0470\u0471\u0005\u0018\u0000"+
		"\u0000\u0471\u0472\u0003.\u0017\u0000\u0472\u0473\u0005\u0019\u0000\u0000"+
		"\u0473\u0474\u0005X\u0000\u0000\u0474\u0475\u0003\u00c6c\u0000\u0475\u0476"+
		"\u0005Y\u0000\u0000\u0476\u0477\u0003\u00c6c\u0000\u0477\u00d9\u0001\u0000"+
		"\u0000\u0000\u0478\u0479\u0005S\u0000\u0000\u0479\u047a\u0005\u0018\u0000"+
		"\u0000\u047a\u047b\u0003.\u0017\u0000\u047b\u047d\u0005\u0019\u0000\u0000"+
		"\u047c\u047e\u0003\u00dcn\u0000\u047d\u047c\u0001\u0000\u0000\u0000\u047e"+
		"\u047f\u0001\u0000\u0000\u0000\u047f\u047d\u0001\u0000\u0000\u0000\u047f"+
		"\u0480\u0001\u0000\u0000\u0000\u0480\u0481\u0001\u0000\u0000\u0000\u0481"+
		"\u0482\u0005W\u0000\u0000\u0482\u0483\u0005B\u0000\u0000\u0483\u0484\u0003"+
		"\u00c6c\u0000\u0484\u00db\u0001\u0000\u0000\u0000\u0485\u0486\u0005T\u0000"+
		"\u0000\u0486\u0488\u0003`0\u0000\u0487\u0485\u0001\u0000\u0000\u0000\u0488"+
		"\u0489\u0001\u0000\u0000\u0000\u0489\u0487\u0001\u0000\u0000\u0000\u0489"+
		"\u048a\u0001\u0000\u0000\u0000\u048a\u048b\u0001\u0000\u0000\u0000\u048b"+
		"\u048c\u0005B\u0000\u0000\u048c\u048d\u0003\u00c6c\u0000\u048d\u00dd\u0001"+
		"\u0000\u0000\u0000\u048e\u048f\u0005U\u0000\u0000\u048f\u0494\u0003\u00cc"+
		"f\u0000\u0490\u0491\u0005V\u0000\u0000\u0491\u0492\u0003b1\u0000\u0492"+
		"\u0493\u0003\u00ccf\u0000\u0493\u0495\u0001\u0000\u0000\u0000\u0494\u0490"+
		"\u0001\u0000\u0000\u0000\u0495\u0496\u0001\u0000\u0000\u0000\u0496\u0494"+
		"\u0001\u0000\u0000\u0000\u0496\u0497\u0001\u0000\u0000\u0000\u0497\u00df"+
		"\u0001\u0000\u0000\u0000\u0498\u0499\u0005Z\u0000\u0000\u0499\u049a\u0005"+
		"\u0018\u0000\u0000\u049a\u049b\u0003.\u0017\u0000\u049b\u049d\u0005\u0019"+
		"\u0000\u0000\u049c\u049e\u0003\u00e2q\u0000\u049d\u049c\u0001\u0000\u0000"+
		"\u0000\u049e\u049f\u0001\u0000\u0000\u0000\u049f\u049d\u0001\u0000\u0000"+
		"\u0000\u049f\u04a0\u0001\u0000\u0000\u0000\u04a0\u04a1\u0001\u0000\u0000"+
		"\u0000\u04a1\u04a4\u0005W\u0000\u0000\u04a2\u04a3\u0005 \u0000\u0000\u04a3"+
		"\u04a5\u0003\u001a\r\u0000\u04a4\u04a2\u0001\u0000\u0000\u0000\u04a4\u04a5"+
		"\u0001\u0000\u0000\u0000\u04a5\u04a6\u0001\u0000\u0000\u0000\u04a6\u04a7"+
		"\u0005B\u0000\u0000\u04a7\u04a8\u0003\u00c6c\u0000\u04a8\u00e1\u0001\u0000"+
		"\u0000\u0000\u04a9\u04ae\u0005T\u0000\u0000\u04aa\u04ab\u0005 \u0000\u0000"+
		"\u04ab\u04ac\u0003\u001a\r\u0000\u04ac\u04ad\u0005E\u0000\u0000\u04ad"+
		"\u04af\u0001\u0000\u0000\u0000\u04ae\u04aa\u0001\u0000\u0000\u0000\u04ae"+
		"\u04af\u0001\u0000\u0000\u0000\u04af\u04b0\u0001\u0000\u0000\u0000\u04b0"+
		"\u04b1\u0003\u00eau\u0000\u04b1\u04b2\u0005B\u0000\u0000\u04b2\u04b3\u0003"+
		"\u00c6c\u0000\u04b3\u00e3\u0001\u0000\u0000\u0000\u04b4\u04b5\u0005E\u0000"+
		"\u0000\u04b5\u04b6\u0003\u00eau\u0000\u04b6\u00e5\u0001\u0000\u0000\u0000"+
		"\u04b7\u04b8\u0005q\u0000\u0000\u04b8\u04b9\u0005 \u0000\u0000\u04b9\u04bb"+
		"\u0003\u001a\r\u0000\u04ba\u04bc\u0003\u00e4r\u0000\u04bb\u04ba\u0001"+
		"\u0000\u0000\u0000\u04bb\u04bc\u0001\u0000\u0000\u0000\u04bc\u04bf\u0001"+
		"\u0000\u0000\u0000\u04bd\u04be\u0005\u0015\u0000\u0000\u04be\u04c0\u0003"+
		"0\u0018\u0000\u04bf\u04bd\u0001\u0000\u0000\u0000\u04bf\u04c0\u0001\u0000"+
		"\u0000\u0000\u04c0\u04cd\u0001\u0000\u0000\u0000\u04c1\u04c2\u0005\u0014"+
		"\u0000\u0000\u04c2\u04c3\u0005 \u0000\u0000\u04c3\u04c5\u0003\u001a\r"+
		"\u0000\u04c4\u04c6\u0003\u00e4r\u0000\u04c5\u04c4\u0001\u0000\u0000\u0000"+
		"\u04c5\u04c6\u0001\u0000\u0000\u0000\u04c6\u04c9\u0001\u0000\u0000\u0000"+
		"\u04c7\u04c8\u0005\u0015\u0000\u0000\u04c8\u04ca\u00030\u0018\u0000\u04c9"+
		"\u04c7\u0001\u0000\u0000\u0000\u04c9\u04ca\u0001\u0000\u0000\u0000\u04ca"+
		"\u04cc\u0001\u0000\u0000\u0000\u04cb\u04c1\u0001\u0000\u0000\u0000\u04cc"+
		"\u04cf\u0001\u0000\u0000\u0000\u04cd\u04cb\u0001\u0000\u0000\u0000\u04cd"+
		"\u04ce\u0001\u0000\u0000\u0000\u04ce\u04d0\u0001\u0000\u0000\u0000\u04cf"+
		"\u04cd\u0001\u0000\u0000\u0000\u04d0\u04d1\u0005\u0001\u0000\u0000\u04d1"+
		"\u00e7\u0001\u0000\u0000\u0000\u04d2\u04d3\u0005\u008f\u0000\u0000\u04d3"+
		"\u04d4\u0005\u0018\u0000\u0000\u04d4\u04d5\u0003.\u0017\u0000\u04d5\u04d6"+
		"\u0005\u0019\u0000\u0000\u04d6\u04d7\u0003\u00c6c\u0000\u04d7\u00e9\u0001"+
		"\u0000\u0000\u0000\u04d8\u04d9\u0005\u0018\u0000\u0000\u04d9\u04e1\u0005"+
		"\u0019\u0000\u0000\u04da\u04de\u0003\u00eew\u0000\u04db\u04df\u0005\u007f"+
		"\u0000\u0000\u04dc\u04df\u0005\"\u0000\u0000\u04dd\u04df\u0005/\u0000"+
		"\u0000\u04de\u04db\u0001\u0000\u0000\u0000\u04de\u04dc\u0001\u0000\u0000"+
		"\u0000\u04de\u04dd\u0001\u0000\u0000\u0000\u04de\u04df\u0001\u0000\u0000"+
		"\u0000\u04df\u04e1\u0001\u0000\u0000\u0000\u04e0\u04d8\u0001\u0000\u0000"+
		"\u0000\u04e0\u04da\u0001\u0000\u0000\u0000\u04e1\u00eb\u0001\u0000\u0000"+
		"\u0000\u04e2\u04eb\u0005\u001a\u0000\u0000\u04e3\u04e8\u0003\u00f8|\u0000"+
		"\u04e4\u04e5\u0005\u0014\u0000\u0000\u04e5\u04e7\u0003\u00f8|\u0000\u04e6"+
		"\u04e4\u0001\u0000\u0000\u0000\u04e7\u04ea\u0001\u0000\u0000\u0000\u04e8"+
		"\u04e6\u0001\u0000\u0000\u0000\u04e8\u04e9\u0001\u0000\u0000\u0000\u04e9"+
		"\u04ec\u0001\u0000\u0000\u0000\u04ea\u04e8\u0001\u0000\u0000\u0000\u04eb"+
		"\u04e3\u0001\u0000\u0000\u0000\u04eb\u04ec\u0001\u0000\u0000\u0000\u04ec"+
		"\u04ed\u0001\u0000\u0000\u0000\u04ed\u04f3\u0005\u001b\u0000\u0000\u04ee"+
		"\u04ef\u0005:\u0000\u0000\u04ef\u04f0\u0003.\u0017\u0000\u04f0\u04f1\u0005"+
		";\u0000\u0000\u04f1\u04f3\u0001\u0000\u0000\u0000\u04f2\u04e2\u0001\u0000"+
		"\u0000\u0000\u04f2\u04ee\u0001\u0000\u0000\u0000\u04f3\u00ed\u0001\u0000"+
		"\u0000\u0000\u04f4\u04f8\u0003\u001a\r\u0000\u04f5\u04f8\u0005\u0080\u0000"+
		"\u0000\u04f6\u04f8\u0003\u00f0x\u0000\u04f7\u04f4\u0001\u0000\u0000\u0000"+
		"\u04f7\u04f5\u0001\u0000\u0000\u0000\u04f7\u04f6\u0001\u0000\u0000\u0000"+
		"\u04f8\u00ef\u0001\u0000\u0000\u0000\u04f9\u04fc\u0003\u00f2y\u0000\u04fa"+
		"\u04fc\u0003\u00f4z\u0000\u04fb\u04f9\u0001\u0000\u0000\u0000\u04fb\u04fa"+
		"\u0001\u0000\u0000\u0000\u04fc\u00f1\u0001\u0000\u0000\u0000\u04fd\u04fe"+
		"\u0005\u0017\u0000\u0000\u04fe\u04ff\u0005\u0018\u0000\u0000\u04ff\u0500"+
		"\u0005\"\u0000\u0000\u0500\u0501\u0005\u0019\u0000\u0000\u0501\u00f3\u0001"+
		"\u0000\u0000\u0000\u0502\u0503\u0005\u0017\u0000\u0000\u0503\u050c\u0005"+
		"\u0018\u0000\u0000\u0504\u0509\u0003\u00eau\u0000\u0505\u0506\u0005\u0014"+
		"\u0000\u0000\u0506\u0508\u0003\u00eau\u0000\u0507\u0505\u0001\u0000\u0000"+
		"\u0000\u0508\u050b\u0001\u0000\u0000\u0000\u0509\u0507\u0001\u0000\u0000"+
		"\u0000\u0509\u050a\u0001\u0000\u0000\u0000\u050a\u050d\u0001\u0000\u0000"+
		"\u0000\u050b\u0509\u0001\u0000\u0000\u0000\u050c\u0504\u0001\u0000\u0000"+
		"\u0000\u050c\u050d\u0001\u0000\u0000\u0000\u050d\u050e\u0001\u0000\u0000"+
		"\u0000\u050e\u050f\u0005\u0019\u0000\u0000\u050f\u0510\u0005E\u0000\u0000"+
		"\u0510\u0511\u0003\u00eau\u0000\u0511\u00f5\u0001\u0000\u0000\u0000\u0512"+
		"\u0514\u0003\u00eew\u0000\u0513\u0515\u0005\u007f\u0000\u0000\u0514\u0513"+
		"\u0001\u0000\u0000\u0000\u0514\u0515\u0001\u0000\u0000\u0000\u0515\u00f7"+
		"\u0001\u0000\u0000\u0000\u0516\u0519\u00030\u0018\u0000\u0517\u0519\u0005"+
		"\u0087\u0000\u0000\u0518\u0516\u0001\u0000\u0000\u0000\u0518\u0517\u0001"+
		"\u0000\u0000\u0000\u0519\u051a\u0001\u0000\u0000\u0000\u051a\u051b\u0007"+
		"\u0006\u0000\u0000\u051b\u051c\u00030\u0018\u0000\u051c\u00f9\u0001\u0000"+
		"\u0000\u0000\u051d\u051f\u00055\u0000\u0000\u051e\u0520\u0003.\u0017\u0000"+
		"\u051f\u051e\u0001\u0000\u0000\u0000\u051f\u0520\u0001\u0000\u0000\u0000"+
		"\u0520\u0521\u0001\u0000\u0000\u0000\u0521\u0522\u00056\u0000\u0000\u0522"+
		"\u00fb\u0001\u0000\u0000\u0000\u0523\u0524\u0003\u00fe\u007f\u0000\u0524"+
		"\u00fd\u0001\u0000\u0000\u0000\u0525\u0526\u0005~\u0000\u0000\u0526\u00ff"+
		"\u0001\u0000\u0000\u0000\u0527\u0528\u0007\u0007\u0000\u0000\u0528\u0101"+
		"\u0001\u0000\u0000\u0000\u0080\u010a\u010e\u011e\u0124\u012c\u0135\u0141"+
		"\u0157\u015f\u0164\u0167\u016b\u0174\u017d\u0180\u0187\u018e\u0190\u0197"+
		"\u019e\u01a0\u01a7\u01ac\u01b0\u01b4\u01bb\u01c5\u01cc\u01d3\u01da\u01e3"+
		"\u01ed\u01f1\u01f6\u01fe\u0205\u020d\u0213\u0217\u021b\u0226\u022c\u023b"+
		"\u0241\u0245\u0249\u0250\u0257\u025d\u0262\u0264\u0268\u026f\u0276\u027f"+
		"\u028b\u0295\u02a1\u02a5\u02ae\u02b5\u02cb\u02d7\u02dc\u02e0\u02e8\u02f0"+
		"\u02f4\u02fb\u0302\u0308\u030f\u0317\u031e\u0324\u032a\u0330\u0336\u0341"+
		"\u0347\u034c\u0354\u0369\u0372\u0374\u038b\u039c\u03a7\u03bd\u03c1\u03c8"+
		"\u03cc\u03d5\u03da\u03de\u03ea\u03f3\u03f9\u0414\u0425\u0427\u0430\u0438"+
		"\u0447\u0467\u047f\u0489\u0496\u049f\u04a4\u04ae\u04bb\u04bf\u04c5\u04c9"+
		"\u04cd\u04de\u04e0\u04e8\u04eb\u04f2\u04f7\u04fb\u0509\u050c\u0514\u0518"+
		"\u051f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}