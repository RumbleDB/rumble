// Generated from ./src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.9.3

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
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		T__59=60, Kfor=61, Klet=62, Kwhere=63, Kgroup=64, Kby=65, Korder=66, Kreturn=67, 
		Kif=68, Kin=69, Kas=70, Kat=71, Kallowing=72, Kempty=73, Kcount=74, Kstable=75, 
		Kascending=76, Kdescending=77, Ksome=78, Kevery=79, Ksatisfies=80, Kcollation=81, 
		Kgreatest=82, Kleast=83, Kswitch=84, Kcase=85, Ktry=86, Kcatch=87, Kdefault=88, 
		Kthen=89, Kelse=90, Ktypeswitch=91, Kor=92, Kand=93, Knot=94, Kto=95, 
		Kinstance=96, Kof=97, Kstatically=98, Kis=99, Ktreat=100, Kcast=101, Kcastable=102, 
		Kversion=103, Kjsoniq=104, Kunordered=105, Ktrue=106, Kfalse=107, Ktype=108, 
		Kvalidate=109, Kannotate=110, Kdeclare=111, Kcontext=112, Kitem=113, Kvariable=114, 
		Kinsert=115, Kdelete=116, Krename=117, Kreplace=118, Kcopy=119, Kmodify=120, 
		Kappend=121, Kinto=122, Kvalue=123, Kjson=124, Kwith=125, Kposition=126, 
		Kbreak=127, Kloop=128, Kcontinue=129, Kexit=130, Kreturning=131, Kwhile=132, 
		STRING=133, ArgumentPlaceholder=134, NullLiteral=135, Literal=136, NumericLiteral=137, 
		IntegerLiteral=138, DecimalLiteral=139, DoubleLiteral=140, WS=141, NCName=142, 
		XQComment=143, ContentChar=144;
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
		RULE_copyDecl = 114, RULE_sequenceType = 115, RULE_objectConstructor = 116, 
		RULE_itemType = 117, RULE_functionTest = 118, RULE_anyFunctionTest = 119, 
		RULE_typedFunctionTest = 120, RULE_singleType = 121, RULE_pairConstructor = 122, 
		RULE_arrayConstructor = 123, RULE_uriLiteral = 124, RULE_stringLiteral = 125, 
		RULE_keyWords = 126;
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
			"copyDecl", "sequenceType", "objectConstructor", "itemType", "functionTest", 
			"anyFunctionTest", "typedFunctionTest", "singleType", "pairConstructor", 
			"arrayConstructor", "uriLiteral", "stringLiteral", "keyWords"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'module'", "'namespace'", "'='", "'$'", "':='", "'{'", 
			"'}'", "'('", "')'", "'*'", "'|'", "'%'", "','", "'ordering'", "'ordered'", 
			"'decimal-format'", "':'", "'decimal-separator'", "'grouping-separator'", 
			"'infinity'", "'minus-sign'", "'NaN'", "'percent'", "'per-mille'", "'zero-digit'", 
			"'digit'", "'pattern-separator'", "'import'", "'external'", "'function'", 
			"'jsound'", "'compact'", "'verbose'", "'schema'", "'eq'", "'ne'", "'lt'", 
			"'le'", "'gt'", "'ge'", "'!='", "'<'", "'<='", "'>'", "'>='", "'||'", 
			"'+'", "'-'", "'div'", "'idiv'", "'mod'", "'!'", "'['", "']'", "'.'", 
			"'$$'", "'#'", "'{|'", "'|}'", "'for'", "'let'", "'where'", "'group'", 
			"'by'", "'order'", "'return'", "'if'", "'in'", "'as'", "'at'", "'allowing'", 
			"'empty'", "'count'", "'stable'", "'ascending'", "'descending'", "'some'", 
			"'every'", "'satisfies'", "'collation'", "'greatest'", "'least'", "'switch'", 
			"'case'", "'try'", "'catch'", "'default'", "'then'", "'else'", "'typeswitch'", 
			"'or'", "'and'", "'not'", "'to'", "'instance'", "'of'", "'statically'", 
			"'is'", "'treat'", "'cast'", "'castable'", "'version'", "'jsoniq'", "'unordered'", 
			"'true'", "'false'", "'type'", "'validate'", "'annotate'", "'declare'", 
			"'context'", "'item'", "'variable'", "'insert'", "'delete'", "'rename'", 
			"'replace'", "'copy'", "'modify'", "'append'", "'into'", "'value'", "'json'", 
			"'with'", "'position'", "'break'", "'loop'", "'continue'", "'exit'", 
			"'returning'", "'while'", null, "'?'", "'null'"
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
			null, "Kfor", "Klet", "Kwhere", "Kgroup", "Kby", "Korder", "Kreturn", 
			"Kif", "Kin", "Kas", "Kat", "Kallowing", "Kempty", "Kcount", "Kstable", 
			"Kascending", "Kdescending", "Ksome", "Kevery", "Ksatisfies", "Kcollation", 
			"Kgreatest", "Kleast", "Kswitch", "Kcase", "Ktry", "Kcatch", "Kdefault", 
			"Kthen", "Kelse", "Ktypeswitch", "Kor", "Kand", "Knot", "Kto", "Kinstance", 
			"Kof", "Kstatically", "Kis", "Ktreat", "Kcast", "Kcastable", "Kversion", 
			"Kjsoniq", "Kunordered", "Ktrue", "Kfalse", "Ktype", "Kvalidate", "Kannotate", 
			"Kdeclare", "Kcontext", "Kitem", "Kvariable", "Kinsert", "Kdelete", "Krename", 
			"Kreplace", "Kcopy", "Kmodify", "Kappend", "Kinto", "Kvalue", "Kjson", 
			"Kwith", "Kposition", "Kbreak", "Kloop", "Kcontinue", "Kexit", "Kreturning", 
			"Kwhile", "STRING", "ArgumentPlaceholder", "NullLiteral", "Literal", 
			"NumericLiteral", "IntegerLiteral", "DecimalLiteral", "DoubleLiteral", 
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
		this._interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
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
		ModuleAndThisIsItContext _localctx = new ModuleAndThisIsItContext(this._ctx, getState());
		enterRule(_localctx, 0, RULE_moduleAndThisIsIt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			module();
			setState(255);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ModuleContext _localctx = new ModuleContext(this._ctx, getState());
		enterRule(_localctx, 2, RULE_module);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,0,this._ctx) ) {
			case 1:
				{
				setState(257);
				match(Kjsoniq);
				setState(258);
				match(Kversion);
				setState(259);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(260);
				match(T__0);
				}
				break;
			}
			setState(266);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__1:
				{
				setState(264);
				libraryModule();
				}
				break;
			case EOF:
			case T__4:
			case T__6:
			case T__8:
			case T__12:
			case T__15:
			case T__28:
			case T__30:
			case T__47:
			case T__48:
			case T__53:
			case T__56:
			case T__58:
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
				{
				setState(265);
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
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		MainModuleContext _localctx = new MainModuleContext(this._ctx, getState());
		enterRule(_localctx, 4, RULE_mainModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			prolog();
			setState(269);
			program();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitLibraryModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryModuleContext libraryModule() throws RecognitionException {
		LibraryModuleContext _localctx = new LibraryModuleContext(this._ctx, getState());
		enterRule(_localctx, 6, RULE_libraryModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(T__1);
			setState(272);
			match(T__2);
			setState(273);
			match(NCName);
			setState(274);
			match(T__3);
			setState(275);
			uriLiteral();
			setState(276);
			match(T__0);
			setState(277);
			prolog();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		PrologContext _localctx = new PrologContext(this._ctx, getState());
		enterRule(_localctx, 8, RULE_prolog);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			this._errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(this._input,3,this._ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(282);
					this._errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(this._input,2,this._ctx) ) {
					case 1:
						{
						setState(279);
						setter();
						}
						break;
					case 2:
						{
						setState(280);
						namespaceDecl();
						}
						break;
					case 3:
						{
						setState(281);
						moduleImport();
						}
						break;
					}
					setState(284);
					match(T__0);
					}
					} 
				}
				setState(290);
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,3,this._ctx);
			}
			setState(296);
			this._errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(this._input,4,this._ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(291);
					annotatedDecl();
					setState(292);
					match(T__0);
					}
					} 
				}
				setState(298);
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,4,this._ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ProgramContext _localctx = new ProgramContext(this._ctx, getState());
		enterRule(_localctx, 10, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
			statementsAndOptionalExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		StatementsContext _localctx = new StatementsContext(this._ctx, getState());
		enterRule(_localctx, 12, RULE_statements);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			this._errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(this._input,5,this._ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(301);
					statement();
					}
					} 
				}
				setState(306);
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,5,this._ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		StatementsAndExprContext _localctx = new StatementsAndExprContext(this._ctx, getState());
		enterRule(_localctx, 14, RULE_statementsAndExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			statements();
			setState(308);
			expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		StatementsAndOptionalExprContext _localctx = new StatementsAndOptionalExprContext(this._ctx, getState());
		enterRule(_localctx, 16, RULE_statementsAndOptionalExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			statements();
			setState(312);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__6) | (1L << T__8) | (1L << T__12) | (1L << T__15) | (1L << T__30) | (1L << T__47) | (1L << T__48) | (1L << T__53) | (1L << T__56) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kloop - 128)) | (1L << (Kcontinue - 128)) | (1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(311);
				expr();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		StatementContext _localctx = new StatementContext(this._ctx, getState());
		enterRule(_localctx, 18, RULE_statement);
		try {
			setState(327);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,7,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(314);
				applyStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(315);
				assignStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(316);
				blockStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(317);
				breakStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(318);
				continueStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(319);
				exitStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(320);
				flowrStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(321);
				ifStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(322);
				switchStatement();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(323);
				tryCatchStatement();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(324);
				typeSwitchStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(325);
				varDeclStatement();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(326);
				whileStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ApplyStatementContext _localctx = new ApplyStatementContext(this._ctx, getState());
		enterRule(_localctx, 20, RULE_applyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329);
			exprSimple();
			setState(330);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		AssignStatementContext _localctx = new AssignStatementContext(this._ctx, getState());
		enterRule(_localctx, 22, RULE_assignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(T__4);
			setState(333);
			qname();
			setState(334);
			match(T__5);
			setState(335);
			exprSingle();
			setState(336);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		BlockStatementContext _localctx = new BlockStatementContext(this._ctx, getState());
		enterRule(_localctx, 24, RULE_blockStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			match(T__6);
			setState(339);
			statements();
			setState(340);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		BreakStatementContext _localctx = new BreakStatementContext(this._ctx, getState());
		enterRule(_localctx, 26, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342);
			match(Kbreak);
			setState(343);
			match(Kloop);
			setState(344);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ContinueStatementContext _localctx = new ContinueStatementContext(this._ctx, getState());
		enterRule(_localctx, 28, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(346);
			match(Kcontinue);
			setState(347);
			match(Kloop);
			setState(348);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ExitStatementContext _localctx = new ExitStatementContext(this._ctx, getState());
		enterRule(_localctx, 30, RULE_exitStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(Kexit);
			setState(351);
			match(Kreturning);
			setState(352);
			exprSingle();
			setState(353);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		FlowrStatementContext _localctx = new FlowrStatementContext(this._ctx, getState());
		enterRule(_localctx, 32, RULE_flowrStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case Kfor:
				{
				setState(355);
				((FlowrStatementContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(356);
				((FlowrStatementContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(367);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (Kfor - 61)) | (1L << (Klet - 61)) | (1L << (Kwhere - 61)) | (1L << (Kgroup - 61)) | (1L << (Korder - 61)) | (1L << (Kcount - 61)) | (1L << (Kstable - 61)))) != 0)) {
				{
				setState(365);
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case Kfor:
					{
					setState(359);
					forClause();
					}
					break;
				case Klet:
					{
					setState(360);
					letClause();
					}
					break;
				case Kwhere:
					{
					setState(361);
					whereClause();
					}
					break;
				case Kgroup:
					{
					setState(362);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(363);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(364);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(369);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(370);
			match(Kreturn);
			setState(371);
			((FlowrStatementContext)_localctx).returnStmt = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		IfStatementContext _localctx = new IfStatementContext(this._ctx, getState());
		enterRule(_localctx, 34, RULE_ifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(373);
			match(Kif);
			setState(374);
			match(T__8);
			setState(375);
			((IfStatementContext)_localctx).test_expr = expr();
			setState(376);
			match(T__9);
			setState(377);
			match(Kthen);
			setState(378);
			((IfStatementContext)_localctx).branch = statement();
			setState(379);
			match(Kelse);
			setState(380);
			((IfStatementContext)_localctx).else_branch = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		SwitchStatementContext _localctx = new SwitchStatementContext(this._ctx, getState());
		enterRule(_localctx, 36, RULE_switchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			match(Kswitch);
			setState(383);
			match(T__8);
			setState(384);
			((SwitchStatementContext)_localctx).condExpr = expr();
			setState(385);
			match(T__9);
			setState(387); 
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				setState(386);
				((SwitchStatementContext)_localctx).switchCaseStatement = switchCaseStatement();
				((SwitchStatementContext)_localctx).cases.add(((SwitchStatementContext)_localctx).switchCaseStatement);
				}
				}
				setState(389); 
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ( _la==Kcase );
			setState(391);
			match(Kdefault);
			setState(392);
			match(Kreturn);
			setState(393);
			((SwitchStatementContext)_localctx).def = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		SwitchCaseStatementContext _localctx = new SwitchCaseStatementContext(this._ctx, getState());
		enterRule(_localctx, 38, RULE_switchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397); 
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				setState(395);
				match(Kcase);
				setState(396);
				((SwitchCaseStatementContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseStatementContext)_localctx).cond.add(((SwitchCaseStatementContext)_localctx).exprSingle);
				}
				}
				setState(399); 
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ( _la==Kcase );
			setState(401);
			match(Kreturn);
			setState(402);
			((SwitchCaseStatementContext)_localctx).ret = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		TryCatchStatementContext _localctx = new TryCatchStatementContext(this._ctx, getState());
		enterRule(_localctx, 40, RULE_tryCatchStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(Ktry);
			setState(405);
			((TryCatchStatementContext)_localctx).try_block = blockStatement();
			setState(407); 
			this._errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(406);
					((TryCatchStatementContext)_localctx).catchCaseStatement = catchCaseStatement();
					((TryCatchStatementContext)_localctx).catches.add(((TryCatchStatementContext)_localctx).catchCaseStatement);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(409); 
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,13,this._ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatchCaseStatementContext extends ParserRuleContext {
		public Token s11;
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
		CatchCaseStatementContext _localctx = new CatchCaseStatementContext(this._ctx, getState());
		enterRule(_localctx, 42, RULE_catchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(Kcatch);
			setState(414);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__10:
				{
				setState(412);
				((CatchCaseStatementContext)_localctx).s11 = match(T__10);
				((CatchCaseStatementContext)_localctx).jokers.add(((CatchCaseStatementContext)_localctx).s11);
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
				setState(413);
				((CatchCaseStatementContext)_localctx).qname = qname();
				((CatchCaseStatementContext)_localctx).errors.add(((CatchCaseStatementContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(423);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(416);
				match(T__11);
				setState(419);
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case T__10:
					{
					setState(417);
					((CatchCaseStatementContext)_localctx).s11 = match(T__10);
					((CatchCaseStatementContext)_localctx).jokers.add(((CatchCaseStatementContext)_localctx).s11);
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
					setState(418);
					((CatchCaseStatementContext)_localctx).qname = qname();
					((CatchCaseStatementContext)_localctx).errors.add(((CatchCaseStatementContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(425);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(426);
			((CatchCaseStatementContext)_localctx).catch_block = blockStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		TypeSwitchStatementContext _localctx = new TypeSwitchStatementContext(this._ctx, getState());
		enterRule(_localctx, 44, RULE_typeSwitchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			match(Ktypeswitch);
			setState(429);
			match(T__8);
			setState(430);
			((TypeSwitchStatementContext)_localctx).cond = expr();
			setState(431);
			match(T__9);
			setState(433); 
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				setState(432);
				((TypeSwitchStatementContext)_localctx).caseStatement = caseStatement();
				((TypeSwitchStatementContext)_localctx).cases.add(((TypeSwitchStatementContext)_localctx).caseStatement);
				}
				}
				setState(435); 
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ( _la==Kcase );
			setState(437);
			match(Kdefault);
			setState(439);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__4) {
				{
				setState(438);
				((TypeSwitchStatementContext)_localctx).var_ref = varRef();
				}
			}

			setState(441);
			match(Kreturn);
			setState(442);
			((TypeSwitchStatementContext)_localctx).def = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		CaseStatementContext _localctx = new CaseStatementContext(this._ctx, getState());
		enterRule(_localctx, 46, RULE_caseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(444);
			match(Kcase);
			setState(448);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__4) {
				{
				setState(445);
				((CaseStatementContext)_localctx).var_ref = varRef();
				setState(446);
				match(Kas);
				}
			}

			setState(450);
			((CaseStatementContext)_localctx).sequenceType = sequenceType();
			((CaseStatementContext)_localctx).union.add(((CaseStatementContext)_localctx).sequenceType);
			setState(455);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(451);
				match(T__11);
				setState(452);
				((CaseStatementContext)_localctx).sequenceType = sequenceType();
				((CaseStatementContext)_localctx).union.add(((CaseStatementContext)_localctx).sequenceType);
				}
				}
				setState(457);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(458);
			match(Kreturn);
			setState(459);
			((CaseStatementContext)_localctx).ret = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		AnnotationContext _localctx = new AnnotationContext(this._ctx, getState());
		enterRule(_localctx, 48, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			match(T__12);
			setState(462);
			((AnnotationContext)_localctx).name = qname();
			setState(473);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__8) {
				{
				setState(463);
				match(T__8);
				setState(464);
				match(Literal);
				setState(469);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la==T__13) {
					{
					{
					setState(465);
					match(T__13);
					setState(466);
					match(Literal);
					}
					}
					setState(471);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				setState(472);
				match(T__9);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		AnnotationsContext _localctx = new AnnotationsContext(this._ctx, getState());
		enterRule(_localctx, 50, RULE_annotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(475);
				annotation();
				}
				}
				setState(480);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		VarDeclStatementContext _localctx = new VarDeclStatementContext(this._ctx, getState());
		enterRule(_localctx, 52, RULE_varDeclStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(481);
			annotations();
			setState(482);
			match(Kvariable);
			setState(483);
			varDeclForStatement();
			setState(488);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(484);
				match(T__13);
				setState(485);
				varDeclForStatement();
				}
				}
				setState(490);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(491);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		VarDeclForStatementContext _localctx = new VarDeclForStatementContext(this._ctx, getState());
		enterRule(_localctx, 54, RULE_varDeclForStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			((VarDeclForStatementContext)_localctx).var_ref = varRef();
			setState(496);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(494);
				match(Kas);
				setState(495);
				sequenceType();
				}
			}

			setState(500);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__5) {
				{
				setState(498);
				match(T__5);
				setState(499);
				((VarDeclForStatementContext)_localctx).exprSingle = exprSingle();
				((VarDeclForStatementContext)_localctx).expr_vals.add(((VarDeclForStatementContext)_localctx).exprSingle);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		WhileStatementContext _localctx = new WhileStatementContext(this._ctx, getState());
		enterRule(_localctx, 56, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
			match(Kwhile);
			setState(503);
			match(T__8);
			setState(504);
			((WhileStatementContext)_localctx).test_expr = expr();
			setState(505);
			match(T__9);
			setState(506);
			((WhileStatementContext)_localctx).stmt = statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		SetterContext _localctx = new SetterContext(this._ctx, getState());
		enterRule(_localctx, 58, RULE_setter);
		try {
			setState(512);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,27,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(508);
				defaultCollationDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(509);
				orderingModeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(510);
				emptyOrderDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(511);
				decimalFormatDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNamespaceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclContext namespaceDecl() throws RecognitionException {
		NamespaceDeclContext _localctx = new NamespaceDeclContext(this._ctx, getState());
		enterRule(_localctx, 60, RULE_namespaceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(514);
			match(Kdeclare);
			setState(515);
			match(T__2);
			setState(516);
			match(NCName);
			setState(517);
			match(T__3);
			setState(518);
			uriLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		AnnotatedDeclContext _localctx = new AnnotatedDeclContext(this._ctx, getState());
		enterRule(_localctx, 62, RULE_annotatedDecl);
		try {
			setState(524);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,28,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(520);
				functionDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(521);
				varDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(522);
				typeDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(523);
				contextItemDecl();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		DefaultCollationDeclContext _localctx = new DefaultCollationDeclContext(this._ctx, getState());
		enterRule(_localctx, 64, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(526);
			match(Kdeclare);
			setState(527);
			match(Kdefault);
			setState(528);
			match(Kcollation);
			setState(529);
			uriLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		OrderingModeDeclContext _localctx = new OrderingModeDeclContext(this._ctx, getState());
		enterRule(_localctx, 66, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			match(Kdeclare);
			setState(532);
			match(T__14);
			setState(533);
			_la = this._input.LA(1);
			if ( !(_la==T__15 || _la==Kunordered) ) {
			this._errHandler.recoverInline(this);
			}
			else {
				if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
				this._errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		EmptyOrderDeclContext _localctx = new EmptyOrderDeclContext(this._ctx, getState());
		enterRule(_localctx, 68, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			match(Kdeclare);
			setState(536);
			match(Kdefault);
			setState(537);
			match(Korder);
			setState(538);
			match(Kempty);
			{
			setState(539);
			((EmptyOrderDeclContext)_localctx).emptySequenceOrder = this._input.LT(1);
			_la = this._input.LA(1);
			if ( !(_la==Kgreatest || _la==Kleast) ) {
				((EmptyOrderDeclContext)_localctx).emptySequenceOrder = (Token)this._errHandler.recoverInline(this);
			}
			else {
				if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
				this._errHandler.reportMatch(this);
				consume();
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		DecimalFormatDeclContext _localctx = new DecimalFormatDeclContext(this._ctx, getState());
		enterRule(_localctx, 70, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(541);
			match(Kdeclare);
			setState(546);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__16:
				{
				{
				setState(542);
				match(T__16);
				setState(543);
				qname();
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(544);
				match(Kdefault);
				setState(545);
				match(T__16);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(554);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27))) != 0)) {
				{
				{
				setState(548);
				dfPropertyName();
				setState(549);
				match(T__3);
				setState(550);
				stringLiteral();
				}
				}
				setState(556);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		QnameContext _localctx = new QnameContext(this._ctx, getState());
		enterRule(_localctx, 72, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(562);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,32,this._ctx) ) {
			case 1:
				{
				setState(559);
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case NCName:
					{
					setState(557);
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
					setState(558);
					((QnameContext)_localctx).nskw = keyWords();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(561);
				match(T__17);
				}
				break;
			}
			setState(566);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case NCName:
				{
				setState(564);
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
				setState(565);
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
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		DfPropertyNameContext _localctx = new DfPropertyNameContext(this._ctx, getState());
		enterRule(_localctx, 74, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568);
			_la = this._input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27))) != 0)) ) {
			this._errHandler.recoverInline(this);
			}
			else {
				if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
				this._errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitModuleImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleImportContext moduleImport() throws RecognitionException {
		ModuleImportContext _localctx = new ModuleImportContext(this._ctx, getState());
		enterRule(_localctx, 76, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(570);
			match(T__28);
			setState(571);
			match(T__1);
			setState(575);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__2) {
				{
				setState(572);
				match(T__2);
				setState(573);
				((ModuleImportContext)_localctx).prefix = match(NCName);
				setState(574);
				match(T__3);
				}
			}

			setState(577);
			((ModuleImportContext)_localctx).targetNamespace = uriLiteral();
			setState(587);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kat) {
				{
				setState(578);
				match(Kat);
				setState(579);
				uriLiteral();
				setState(584);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la==T__13) {
					{
					{
					setState(580);
					match(T__13);
					setState(581);
					uriLiteral();
					}
					}
					setState(586);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		VarDeclContext _localctx = new VarDeclContext(this._ctx, getState());
		enterRule(_localctx, 78, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(589);
			match(Kdeclare);
			setState(590);
			annotations();
			setState(591);
			match(Kvariable);
			setState(592);
			varRef();
			setState(595);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(593);
				match(Kas);
				setState(594);
				sequenceType();
				}
			}

			setState(604);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__5:
				{
				{
				setState(597);
				match(T__5);
				setState(598);
				exprSingle();
				}
				}
				break;
			case T__29:
				{
				{
				setState(599);
				((VarDeclContext)_localctx).external = match(T__29);
				setState(602);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la==T__5) {
					{
					setState(600);
					match(T__5);
					setState(601);
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
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ContextItemDeclContext _localctx = new ContextItemDeclContext(this._ctx, getState());
		enterRule(_localctx, 80, RULE_contextItemDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(606);
			match(Kdeclare);
			setState(607);
			match(Kcontext);
			setState(608);
			match(Kitem);
			setState(611);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(609);
				match(Kas);
				setState(610);
				sequenceType();
				}
			}

			setState(620);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__5:
				{
				{
				setState(613);
				match(T__5);
				setState(614);
				exprSingle();
				}
				}
				break;
			case T__29:
				{
				{
				setState(615);
				((ContextItemDeclContext)_localctx).external = match(T__29);
				setState(618);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la==T__5) {
					{
					setState(616);
					match(T__5);
					setState(617);
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
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		FunctionDeclContext _localctx = new FunctionDeclContext(this._ctx, getState());
		enterRule(_localctx, 82, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(622);
			match(Kdeclare);
			setState(623);
			annotations();
			setState(624);
			match(T__30);
			setState(625);
			((FunctionDeclContext)_localctx).fn_name = qname();
			setState(626);
			match(T__8);
			setState(628);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__4) {
				{
				setState(627);
				paramList();
				}
			}

			setState(630);
			match(T__9);
			setState(633);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(631);
				match(Kas);
				setState(632);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(640);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__6:
				{
				setState(635);
				match(T__6);
				{
				setState(636);
				((FunctionDeclContext)_localctx).fn_body = statementsAndOptionalExpr();
				}
				setState(637);
				match(T__7);
				}
				break;
			case T__29:
				{
				setState(639);
				match(T__29);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		TypeDeclContext _localctx = new TypeDeclContext(this._ctx, getState());
		enterRule(_localctx, 84, RULE_typeDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(642);
			match(Kdeclare);
			setState(643);
			match(Ktype);
			setState(644);
			((TypeDeclContext)_localctx).type_name = qname();
			setState(645);
			match(Kas);
			setState(647);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,46,this._ctx) ) {
			case 1:
				{
				setState(646);
				((TypeDeclContext)_localctx).schema = schemaLanguage();
				}
				break;
			}
			setState(649);
			((TypeDeclContext)_localctx).type_definition = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SchemaLanguageContext extends ParserRuleContext {
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
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
		SchemaLanguageContext _localctx = new SchemaLanguageContext(this._ctx, getState());
		enterRule(_localctx, 86, RULE_schemaLanguage);
		try {
			setState(657);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,47,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(651);
				match(T__31);
				setState(652);
				match(T__32);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(653);
				match(T__31);
				setState(654);
				match(T__33);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(655);
				match(Kjson);
				setState(656);
				match(T__34);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ParamListContext _localctx = new ParamListContext(this._ctx, getState());
		enterRule(_localctx, 88, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(659);
			param();
			setState(664);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(660);
				match(T__13);
				setState(661);
				param();
				}
				}
				setState(666);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ParamContext _localctx = new ParamContext(this._ctx, getState());
		enterRule(_localctx, 90, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(667);
			match(T__4);
			setState(668);
			qname();
			setState(671);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(669);
				match(Kas);
				setState(670);
				sequenceType();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ExprContext _localctx = new ExprContext(this._ctx, getState());
		enterRule(_localctx, 92, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(673);
			exprSingle();
			setState(678);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(674);
				match(T__13);
				setState(675);
				exprSingle();
				}
				}
				setState(680);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ExprSingleContext _localctx = new ExprSingleContext(this._ctx, getState());
		enterRule(_localctx, 94, RULE_exprSingle);
		try {
			setState(687);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,51,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(681);
				exprSimple();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(682);
				flowrExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(683);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(684);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(685);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(686);
				tryCatchExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ExprSimpleContext _localctx = new ExprSimpleContext(this._ctx, getState());
		enterRule(_localctx, 96, RULE_exprSimple);
		try {
			setState(697);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,52,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(689);
				quantifiedExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(690);
				orExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(691);
				insertExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(692);
				deleteExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(693);
				renameExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(694);
				replaceExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(695);
				transformExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(696);
				appendExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		FlowrExprContext _localctx = new FlowrExprContext(this._ctx, getState());
		enterRule(_localctx, 98, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(701);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case Kfor:
				{
				setState(699);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(700);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(711);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (Kfor - 61)) | (1L << (Klet - 61)) | (1L << (Kwhere - 61)) | (1L << (Kgroup - 61)) | (1L << (Korder - 61)) | (1L << (Kcount - 61)) | (1L << (Kstable - 61)))) != 0)) {
				{
				setState(709);
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case Kfor:
					{
					setState(703);
					forClause();
					}
					break;
				case Klet:
					{
					setState(704);
					letClause();
					}
					break;
				case Kwhere:
					{
					setState(705);
					whereClause();
					}
					break;
				case Kgroup:
					{
					setState(706);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(707);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(708);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(713);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(714);
			match(Kreturn);
			setState(715);
			((FlowrExprContext)_localctx).return_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ForClauseContext _localctx = new ForClauseContext(this._ctx, getState());
		enterRule(_localctx, 100, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(717);
			match(Kfor);
			setState(718);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(723);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(719);
				match(T__13);
				setState(720);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(725);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ForVarContext _localctx = new ForVarContext(this._ctx, getState());
		enterRule(_localctx, 102, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(726);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(729);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(727);
				match(Kas);
				setState(728);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(733);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kallowing) {
				{
				setState(731);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(732);
				match(Kempty);
				}
			}

			setState(737);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kat) {
				{
				setState(735);
				match(Kat);
				setState(736);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(739);
			match(Kin);
			setState(740);
			((ForVarContext)_localctx).ex = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		LetClauseContext _localctx = new LetClauseContext(this._ctx, getState());
		enterRule(_localctx, 104, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(742);
			match(Klet);
			setState(743);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(748);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(744);
				match(T__13);
				setState(745);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(750);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		LetVarContext _localctx = new LetVarContext(this._ctx, getState());
		enterRule(_localctx, 106, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(751);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(754);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(752);
				match(Kas);
				setState(753);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(756);
			match(T__5);
			setState(757);
			((LetVarContext)_localctx).ex = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		WhereClauseContext _localctx = new WhereClauseContext(this._ctx, getState());
		enterRule(_localctx, 108, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(759);
			match(Kwhere);
			setState(760);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		GroupByClauseContext _localctx = new GroupByClauseContext(this._ctx, getState());
		enterRule(_localctx, 110, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(762);
			match(Kgroup);
			setState(763);
			match(Kby);
			setState(764);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(769);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(765);
				match(T__13);
				setState(766);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(771);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		GroupByVarContext _localctx = new GroupByVarContext(this._ctx, getState());
		enterRule(_localctx, 112, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(772);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(779);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__5 || _la==Kas) {
				{
				setState(775);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la==Kas) {
					{
					setState(773);
					match(Kas);
					setState(774);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(777);
				((GroupByVarContext)_localctx).decl = match(T__5);
				setState(778);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(783);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kcollation) {
				{
				setState(781);
				match(Kcollation);
				setState(782);
				((GroupByVarContext)_localctx).uri = uriLiteral();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		OrderByClauseContext _localctx = new OrderByClauseContext(this._ctx, getState());
		enterRule(_localctx, 114, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(790);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case Korder:
				{
				{
				setState(785);
				match(Korder);
				setState(786);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(787);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(788);
				match(Korder);
				setState(789);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(792);
			orderByExpr();
			setState(797);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(793);
				match(T__13);
				setState(794);
				orderByExpr();
				}
				}
				setState(799);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		OrderByExprContext _localctx = new OrderByExprContext(this._ctx, getState());
		enterRule(_localctx, 116, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(803);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case Kascending:
				{
				setState(801);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(802);
				((OrderByExprContext)_localctx).desc = match(Kdescending);
				}
				break;
			case T__13:
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
			setState(810);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kempty) {
				{
				setState(805);
				match(Kempty);
				setState(808);
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case Kgreatest:
					{
					setState(806);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(807);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(814);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kcollation) {
				{
				setState(812);
				match(Kcollation);
				setState(813);
				((OrderByExprContext)_localctx).uril = uriLiteral();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		CountClauseContext _localctx = new CountClauseContext(this._ctx, getState());
		enterRule(_localctx, 118, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(816);
			match(Kcount);
			setState(817);
			varRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		QuantifiedExprContext _localctx = new QuantifiedExprContext(this._ctx, getState());
		enterRule(_localctx, 120, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case Ksome:
				{
				setState(819);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(820);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(823);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(828);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(824);
				match(T__13);
				setState(825);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(830);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(831);
			match(Ksatisfies);
			setState(832);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		QuantifiedExprVarContext _localctx = new QuantifiedExprVarContext(this._ctx, getState());
		enterRule(_localctx, 122, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(834);
			varRef();
			setState(837);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(835);
				match(Kas);
				setState(836);
				sequenceType();
				}
			}

			setState(839);
			match(Kin);
			setState(840);
			exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		SwitchExprContext _localctx = new SwitchExprContext(this._ctx, getState());
		enterRule(_localctx, 124, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(842);
			match(Kswitch);
			setState(843);
			match(T__8);
			setState(844);
			((SwitchExprContext)_localctx).cond = expr();
			setState(845);
			match(T__9);
			setState(847); 
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				setState(846);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(849); 
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ( _la==Kcase );
			setState(851);
			match(Kdefault);
			setState(852);
			match(Kreturn);
			setState(853);
			((SwitchExprContext)_localctx).def = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		SwitchCaseClauseContext _localctx = new SwitchCaseClauseContext(this._ctx, getState());
		enterRule(_localctx, 126, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(857); 
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				setState(855);
				match(Kcase);
				setState(856);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(859); 
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ( _la==Kcase );
			setState(861);
			match(Kreturn);
			setState(862);
			((SwitchCaseClauseContext)_localctx).ret = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		TypeSwitchExprContext _localctx = new TypeSwitchExprContext(this._ctx, getState());
		enterRule(_localctx, 128, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
			match(Ktypeswitch);
			setState(865);
			match(T__8);
			setState(866);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(867);
			match(T__9);
			setState(869); 
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				setState(868);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(871); 
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ( _la==Kcase );
			setState(873);
			match(Kdefault);
			setState(875);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__4) {
				{
				setState(874);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(877);
			match(Kreturn);
			setState(878);
			((TypeSwitchExprContext)_localctx).def = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		CaseClauseContext _localctx = new CaseClauseContext(this._ctx, getState());
		enterRule(_localctx, 130, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(880);
			match(Kcase);
			setState(884);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__4) {
				{
				setState(881);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(882);
				match(Kas);
				}
			}

			setState(886);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(891);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(887);
				match(T__11);
				setState(888);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(893);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(894);
			match(Kreturn);
			setState(895);
			((CaseClauseContext)_localctx).ret = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		IfExprContext _localctx = new IfExprContext(this._ctx, getState());
		enterRule(_localctx, 132, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(897);
			match(Kif);
			setState(898);
			match(T__8);
			setState(899);
			((IfExprContext)_localctx).test_condition = expr();
			setState(900);
			match(T__9);
			setState(901);
			match(Kthen);
			setState(902);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(903);
			match(Kelse);
			setState(904);
			((IfExprContext)_localctx).else_branch = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		TryCatchExprContext _localctx = new TryCatchExprContext(this._ctx, getState());
		enterRule(_localctx, 134, RULE_tryCatchExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(906);
			match(Ktry);
			setState(907);
			match(T__6);
			setState(908);
			((TryCatchExprContext)_localctx).try_expression = expr();
			setState(909);
			match(T__7);
			setState(911); 
			this._errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(910);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(913); 
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,81,this._ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatchClauseContext extends ParserRuleContext {
		public Token s11;
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
		CatchClauseContext _localctx = new CatchClauseContext(this._ctx, getState());
		enterRule(_localctx, 136, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(915);
			match(Kcatch);
			setState(918);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__10:
				{
				setState(916);
				((CatchClauseContext)_localctx).s11 = match(T__10);
				((CatchClauseContext)_localctx).jokers.add(((CatchClauseContext)_localctx).s11);
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
				setState(917);
				((CatchClauseContext)_localctx).qname = qname();
				((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(927);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(920);
				match(T__11);
				setState(923);
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case T__10:
					{
					setState(921);
					((CatchClauseContext)_localctx).s11 = match(T__10);
					((CatchClauseContext)_localctx).jokers.add(((CatchClauseContext)_localctx).s11);
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
					setState(922);
					((CatchClauseContext)_localctx).qname = qname();
					((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(929);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(930);
			match(T__6);
			setState(931);
			((CatchClauseContext)_localctx).catch_expression = expr();
			setState(932);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		OrExprContext _localctx = new OrExprContext(this._ctx, getState());
		enterRule(_localctx, 138, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(934);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(939);
			this._errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(this._input,85,this._ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(935);
					match(Kor);
					setState(936);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(941);
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,85,this._ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		AndExprContext _localctx = new AndExprContext(this._ctx, getState());
		enterRule(_localctx, 140, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(942);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(947);
			this._errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(this._input,86,this._ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(943);
					match(Kand);
					setState(944);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(949);
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,86,this._ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		NotExprContext _localctx = new NotExprContext(this._ctx, getState());
		enterRule(_localctx, 142, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,87,this._ctx) ) {
			case 1:
				{
				setState(950);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(953);
			((NotExprContext)_localctx).main_expr = comparisonExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonExprContext extends ParserRuleContext {
		public StringConcatExprContext main_expr;
		public Token s36;
		public List<Token> op = new ArrayList<Token>();
		public Token s37;
		public Token s38;
		public Token s39;
		public Token s40;
		public Token s41;
		public Token s4;
		public Token s42;
		public Token s43;
		public Token s44;
		public Token s45;
		public Token s46;
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
		ComparisonExprContext _localctx = new ComparisonExprContext(this._ctx, getState());
		enterRule(_localctx, 144, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(955);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(958);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44) | (1L << T__45))) != 0)) {
				{
				setState(956);
				((ComparisonExprContext)_localctx)._tset1828 = this._input.LT(1);
				_la = this._input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44) | (1L << T__45))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1828 = (Token)this._errHandler.recoverInline(this);
				}
				else {
					if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
					this._errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1828);
				setState(957);
				((ComparisonExprContext)_localctx).stringConcatExpr = stringConcatExpr();
				((ComparisonExprContext)_localctx).rhs.add(((ComparisonExprContext)_localctx).stringConcatExpr);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		StringConcatExprContext _localctx = new StringConcatExprContext(this._ctx, getState());
		enterRule(_localctx, 146, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(960);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(965);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__46) {
				{
				{
				setState(961);
				match(T__46);
				setState(962);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(967);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		RangeExprContext _localctx = new RangeExprContext(this._ctx, getState());
		enterRule(_localctx, 148, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(968);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(971);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,90,this._ctx) ) {
			case 1:
				{
				setState(969);
				match(Kto);
				setState(970);
				((RangeExprContext)_localctx).additiveExpr = additiveExpr();
				((RangeExprContext)_localctx).rhs.add(((RangeExprContext)_localctx).additiveExpr);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AdditiveExprContext extends ParserRuleContext {
		public MultiplicativeExprContext main_expr;
		public Token s48;
		public List<Token> op = new ArrayList<Token>();
		public Token s49;
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
		AdditiveExprContext _localctx = new AdditiveExprContext(this._ctx, getState());
		enterRule(_localctx, 150, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(973);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(978);
			this._errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(this._input,91,this._ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(974);
					((AdditiveExprContext)_localctx)._tset1937 = this._input.LT(1);
					_la = this._input.LA(1);
					if ( !(_la==T__47 || _la==T__48) ) {
						((AdditiveExprContext)_localctx)._tset1937 = (Token)this._errHandler.recoverInline(this);
					}
					else {
						if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
						this._errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1937);
					setState(975);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(980);
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,91,this._ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultiplicativeExprContext extends ParserRuleContext {
		public InstanceOfExprContext main_expr;
		public Token s11;
		public List<Token> op = new ArrayList<Token>();
		public Token s50;
		public Token s51;
		public Token s52;
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
		MultiplicativeExprContext _localctx = new MultiplicativeExprContext(this._ctx, getState());
		enterRule(_localctx, 152, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(981);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(986);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__49) | (1L << T__50) | (1L << T__51))) != 0)) {
				{
				{
				setState(982);
				((MultiplicativeExprContext)_localctx)._tset1965 = this._input.LT(1);
				_la = this._input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__49) | (1L << T__50) | (1L << T__51))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1965 = (Token)this._errHandler.recoverInline(this);
				}
				else {
					if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
					this._errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1965);
				setState(983);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(988);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		InstanceOfExprContext _localctx = new InstanceOfExprContext(this._ctx, getState());
		enterRule(_localctx, 154, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(989);
			((InstanceOfExprContext)_localctx).main_expr = isStaticallyExpr();
			setState(993);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,93,this._ctx) ) {
			case 1:
				{
				setState(990);
				match(Kinstance);
				setState(991);
				match(Kof);
				setState(992);
				((InstanceOfExprContext)_localctx).seq = sequenceType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		IsStaticallyExprContext _localctx = new IsStaticallyExprContext(this._ctx, getState());
		enterRule(_localctx, 156, RULE_isStaticallyExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(995);
			((IsStaticallyExprContext)_localctx).main_expr = treatExpr();
			setState(999);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,94,this._ctx) ) {
			case 1:
				{
				setState(996);
				match(Kis);
				setState(997);
				match(Kstatically);
				setState(998);
				((IsStaticallyExprContext)_localctx).seq = sequenceType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		TreatExprContext _localctx = new TreatExprContext(this._ctx, getState());
		enterRule(_localctx, 158, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1001);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(1005);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,95,this._ctx) ) {
			case 1:
				{
				setState(1002);
				match(Ktreat);
				setState(1003);
				match(Kas);
				setState(1004);
				((TreatExprContext)_localctx).seq = sequenceType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		CastableExprContext _localctx = new CastableExprContext(this._ctx, getState());
		enterRule(_localctx, 160, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1007);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(1011);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,96,this._ctx) ) {
			case 1:
				{
				setState(1008);
				match(Kcastable);
				setState(1009);
				match(Kas);
				setState(1010);
				((CastableExprContext)_localctx).single = singleType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		CastExprContext _localctx = new CastExprContext(this._ctx, getState());
		enterRule(_localctx, 162, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1013);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(1017);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,97,this._ctx) ) {
			case 1:
				{
				setState(1014);
				match(Kcast);
				setState(1015);
				match(Kas);
				setState(1016);
				((CastExprContext)_localctx).single = singleType();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ArrowExprContext _localctx = new ArrowExprContext(this._ctx, getState());
		enterRule(_localctx, 164, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1019);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(1028);
			this._errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(this._input,98,this._ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(1020);
					match(T__3);
					setState(1021);
					match(T__44);
					}
					setState(1023);
					((ArrowExprContext)_localctx).arrowFunctionSpecifier = arrowFunctionSpecifier();
					((ArrowExprContext)_localctx).function.add(((ArrowExprContext)_localctx).arrowFunctionSpecifier);
					setState(1024);
					((ArrowExprContext)_localctx).argumentList = argumentList();
					((ArrowExprContext)_localctx).arguments.add(((ArrowExprContext)_localctx).argumentList);
					}
					} 
				}
				setState(1030);
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,98,this._ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ArrowFunctionSpecifierContext _localctx = new ArrowFunctionSpecifierContext(this._ctx, getState());
		enterRule(_localctx, 166, RULE_arrowFunctionSpecifier);
		try {
			setState(1034);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
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
				setState(1031);
				qname();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(1032);
				varRef();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 3);
				{
				setState(1033);
				parenthesizedExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnaryExprContext extends ParserRuleContext {
		public Token s49;
		public List<Token> op = new ArrayList<Token>();
		public Token s48;
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
		UnaryExprContext _localctx = new UnaryExprContext(this._ctx, getState());
		enterRule(_localctx, 168, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1039);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__47 || _la==T__48) {
				{
				{
				setState(1036);
				((UnaryExprContext)_localctx)._tset2144 = this._input.LT(1);
				_la = this._input.LA(1);
				if ( !(_la==T__47 || _la==T__48) ) {
					((UnaryExprContext)_localctx)._tset2144 = (Token)this._errHandler.recoverInline(this);
				}
				else {
					if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
					this._errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset2144);
				}
				}
				setState(1041);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(1042);
			((UnaryExprContext)_localctx).main_expr = valueExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ValueExprContext _localctx = new ValueExprContext(this._ctx, getState());
		enterRule(_localctx, 170, RULE_valueExpr);
		try {
			setState(1047);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,101,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1044);
				((ValueExprContext)_localctx).simpleMap_expr = simpleMapExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1045);
				((ValueExprContext)_localctx).validate_expr = validateExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1046);
				((ValueExprContext)_localctx).annotate_expr = annotateExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ValidateExprContext _localctx = new ValidateExprContext(this._ctx, getState());
		enterRule(_localctx, 172, RULE_validateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1049);
			match(Kvalidate);
			setState(1050);
			match(Ktype);
			setState(1051);
			sequenceType();
			setState(1052);
			match(T__6);
			setState(1053);
			expr();
			setState(1054);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		AnnotateExprContext _localctx = new AnnotateExprContext(this._ctx, getState());
		enterRule(_localctx, 174, RULE_annotateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1056);
			match(Kannotate);
			setState(1057);
			match(Ktype);
			setState(1058);
			sequenceType();
			setState(1059);
			match(T__6);
			setState(1060);
			expr();
			setState(1061);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		SimpleMapExprContext _localctx = new SimpleMapExprContext(this._ctx, getState());
		enterRule(_localctx, 176, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1063);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(1068);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__52) {
				{
				{
				setState(1064);
				match(T__52);
				setState(1065);
				((SimpleMapExprContext)_localctx).postFixExpr = postFixExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).postFixExpr);
				}
				}
				setState(1070);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		PostFixExprContext _localctx = new PostFixExprContext(this._ctx, getState());
		enterRule(_localctx, 178, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1071);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(1079);
			this._errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(this._input,104,this._ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1077);
					this._errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(this._input,103,this._ctx) ) {
					case 1:
						{
						setState(1072);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(1073);
						predicate();
						}
						break;
					case 3:
						{
						setState(1074);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(1075);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(1076);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(1081);
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,104,this._ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ArrayLookupContext _localctx = new ArrayLookupContext(this._ctx, getState());
		enterRule(_localctx, 180, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1082);
			match(T__53);
			setState(1083);
			match(T__53);
			setState(1084);
			expr();
			setState(1085);
			match(T__54);
			setState(1086);
			match(T__54);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ArrayUnboxingContext _localctx = new ArrayUnboxingContext(this._ctx, getState());
		enterRule(_localctx, 182, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1088);
			match(T__53);
			setState(1089);
			match(T__54);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		PredicateContext _localctx = new PredicateContext(this._ctx, getState());
		enterRule(_localctx, 184, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1091);
			match(T__53);
			setState(1092);
			expr();
			setState(1093);
			match(T__54);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ObjectLookupContext _localctx = new ObjectLookupContext(this._ctx, getState());
		enterRule(_localctx, 186, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1095);
			match(T__55);
			setState(1102);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
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
				setState(1096);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(1097);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(1098);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__8:
				{
				setState(1099);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__4:
				{
				setState(1100);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__56:
				{
				setState(1101);
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
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		PrimaryExprContext _localctx = new PrimaryExprContext(this._ctx, getState());
		enterRule(_localctx, 188, RULE_primaryExpr);
		try {
			setState(1119);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,106,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1104);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1105);
				match(Ktrue);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1106);
				match(Kfalse);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1107);
				match(Literal);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1108);
				stringLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1109);
				varRef();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1110);
				parenthesizedExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1111);
				contextItemExpr();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1112);
				objectConstructor();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1113);
				functionCall();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1114);
				orderedExpr();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1115);
				unorderedExpr();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1116);
				arrayConstructor();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1117);
				functionItemExpr();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1118);
				blockExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		BlockExprContext _localctx = new BlockExprContext(this._ctx, getState());
		enterRule(_localctx, 190, RULE_blockExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1121);
			match(T__6);
			setState(1122);
			statementsAndExpr();
			setState(1123);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		VarRefContext _localctx = new VarRefContext(this._ctx, getState());
		enterRule(_localctx, 192, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1125);
			match(T__4);
			setState(1126);
			((VarRefContext)_localctx).var_name = qname();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ParenthesizedExprContext _localctx = new ParenthesizedExprContext(this._ctx, getState());
		enterRule(_localctx, 194, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1128);
			match(T__8);
			setState(1130);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__6) | (1L << T__8) | (1L << T__12) | (1L << T__15) | (1L << T__30) | (1L << T__47) | (1L << T__48) | (1L << T__53) | (1L << T__56) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kloop - 128)) | (1L << (Kcontinue - 128)) | (1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1129);
				expr();
				}
			}

			setState(1132);
			match(T__9);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ContextItemExprContext _localctx = new ContextItemExprContext(this._ctx, getState());
		enterRule(_localctx, 196, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1134);
			match(T__56);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		OrderedExprContext _localctx = new OrderedExprContext(this._ctx, getState());
		enterRule(_localctx, 198, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1136);
			match(T__15);
			setState(1137);
			match(T__6);
			setState(1138);
			expr();
			setState(1139);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		UnorderedExprContext _localctx = new UnorderedExprContext(this._ctx, getState());
		enterRule(_localctx, 200, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1141);
			match(Kunordered);
			setState(1142);
			match(T__6);
			setState(1143);
			expr();
			setState(1144);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		FunctionCallContext _localctx = new FunctionCallContext(this._ctx, getState());
		enterRule(_localctx, 202, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1146);
			((FunctionCallContext)_localctx).fn_name = qname();
			setState(1147);
			argumentList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ArgumentListContext _localctx = new ArgumentListContext(this._ctx, getState());
		enterRule(_localctx, 204, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			match(T__8);
			setState(1156);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__6) | (1L << T__8) | (1L << T__12) | (1L << T__15) | (1L << T__30) | (1L << T__47) | (1L << T__48) | (1L << T__53) | (1L << T__56) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kloop - 128)) | (1L << (Kcontinue - 128)) | (1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (ArgumentPlaceholder - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				{
				setState(1150);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(1152);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la==T__13) {
					{
					setState(1151);
					match(T__13);
					}
				}

				}
				}
				setState(1158);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(1159);
			match(T__9);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ArgumentContext _localctx = new ArgumentContext(this._ctx, getState());
		enterRule(_localctx, 206, RULE_argument);
		try {
			setState(1163);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__4:
			case T__6:
			case T__8:
			case T__12:
			case T__15:
			case T__30:
			case T__47:
			case T__48:
			case T__53:
			case T__56:
			case T__58:
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
				setState(1161);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(1162);
				match(ArgumentPlaceholder);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		FunctionItemExprContext _localctx = new FunctionItemExprContext(this._ctx, getState());
		enterRule(_localctx, 208, RULE_functionItemExpr);
		try {
			setState(1167);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
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
				setState(1165);
				namedFunctionRef();
				}
				break;
			case T__12:
			case T__30:
				enterOuterAlt(_localctx, 2);
				{
				setState(1166);
				inlineFunctionExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		NamedFunctionRefContext _localctx = new NamedFunctionRefContext(this._ctx, getState());
		enterRule(_localctx, 210, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1169);
			((NamedFunctionRefContext)_localctx).fn_name = qname();
			setState(1170);
			match(T__57);
			setState(1171);
			((NamedFunctionRefContext)_localctx).arity = match(Literal);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		InlineFunctionExprContext _localctx = new InlineFunctionExprContext(this._ctx, getState());
		enterRule(_localctx, 212, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1173);
			annotations();
			setState(1174);
			match(T__30);
			setState(1175);
			match(T__8);
			setState(1177);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==T__4) {
				{
				setState(1176);
				paramList();
				}
			}

			setState(1179);
			match(T__9);
			setState(1182);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la==Kas) {
				{
				setState(1180);
				match(Kas);
				setState(1181);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(1184);
			match(T__6);
			{
			setState(1185);
			((InlineFunctionExprContext)_localctx).fn_body = statementsAndOptionalExpr();
			}
			setState(1186);
			match(T__7);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		InsertExprContext _localctx = new InsertExprContext(this._ctx, getState());
		enterRule(_localctx, 214, RULE_insertExpr);
		int _la;
		try {
			setState(1211);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,116,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1188);
				match(Kinsert);
				setState(1189);
				match(Kjson);
				setState(1190);
				((InsertExprContext)_localctx).to_insert_expr = exprSingle();
				setState(1191);
				match(Kinto);
				setState(1192);
				((InsertExprContext)_localctx).main_expr = exprSingle();
				setState(1196);
				this._errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(this._input,114,this._ctx) ) {
				case 1:
					{
					setState(1193);
					match(Kat);
					setState(1194);
					match(Kposition);
					setState(1195);
					((InsertExprContext)_localctx).pos_expr = exprSingle();
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1198);
				match(Kinsert);
				setState(1199);
				match(Kjson);
				setState(1200);
				pairConstructor();
				setState(1205);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la==T__13) {
					{
					{
					setState(1201);
					match(T__13);
					setState(1202);
					pairConstructor();
					}
					}
					setState(1207);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				setState(1208);
				match(Kinto);
				setState(1209);
				((InsertExprContext)_localctx).main_expr = exprSingle();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		DeleteExprContext _localctx = new DeleteExprContext(this._ctx, getState());
		enterRule(_localctx, 216, RULE_deleteExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1213);
			match(Kdelete);
			setState(1214);
			match(Kjson);
			setState(1215);
			updateLocator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		RenameExprContext _localctx = new RenameExprContext(this._ctx, getState());
		enterRule(_localctx, 218, RULE_renameExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1217);
			match(Krename);
			setState(1218);
			match(Kjson);
			setState(1219);
			updateLocator();
			setState(1220);
			match(Kas);
			setState(1221);
			((RenameExprContext)_localctx).name_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ReplaceExprContext _localctx = new ReplaceExprContext(this._ctx, getState());
		enterRule(_localctx, 220, RULE_replaceExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1223);
			match(Kreplace);
			setState(1224);
			match(Kvalue);
			setState(1225);
			match(Kof);
			setState(1226);
			match(Kjson);
			setState(1227);
			updateLocator();
			setState(1228);
			match(Kwith);
			setState(1229);
			((ReplaceExprContext)_localctx).replacer_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		TransformExprContext _localctx = new TransformExprContext(this._ctx, getState());
		enterRule(_localctx, 222, RULE_transformExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
			match(Kcopy);
			setState(1232);
			copyDecl();
			setState(1237);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(1233);
				match(T__13);
				setState(1234);
				copyDecl();
				}
				}
				setState(1239);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			setState(1240);
			match(Kmodify);
			setState(1241);
			((TransformExprContext)_localctx).mod_expr = exprSingle();
			setState(1242);
			match(Kreturn);
			setState(1243);
			((TransformExprContext)_localctx).ret_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		AppendExprContext _localctx = new AppendExprContext(this._ctx, getState());
		enterRule(_localctx, 224, RULE_appendExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1245);
			match(Kappend);
			setState(1246);
			match(Kjson);
			setState(1247);
			((AppendExprContext)_localctx).to_append_expr = exprSingle();
			setState(1248);
			match(Kinto);
			setState(1249);
			((AppendExprContext)_localctx).array_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		UpdateLocatorContext _localctx = new UpdateLocatorContext(this._ctx, getState());
		enterRule(_localctx, 226, RULE_updateLocator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1251);
			((UpdateLocatorContext)_localctx).main_expr = primaryExpr();
			setState(1254); 
			this._errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(1254);
					this._errHandler.sync(this);
					switch (this._input.LA(1)) {
					case T__53:
						{
						setState(1252);
						arrayLookup();
						}
						break;
					case T__55:
						{
						setState(1253);
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
				setState(1256); 
				this._errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(this._input,119,this._ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		CopyDeclContext _localctx = new CopyDeclContext(this._ctx, getState());
		enterRule(_localctx, 228, RULE_copyDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1258);
			((CopyDeclContext)_localctx).var_ref = varRef();
			setState(1259);
			match(T__5);
			setState(1260);
			((CopyDeclContext)_localctx).src_expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SequenceTypeContext extends ParserRuleContext {
		public ItemTypeContext item;
		public Token s134;
		public List<Token> question = new ArrayList<Token>();
		public Token s11;
		public List<Token> star = new ArrayList<Token>();
		public Token s48;
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
		SequenceTypeContext _localctx = new SequenceTypeContext(this._ctx, getState());
		enterRule(_localctx, 230, RULE_sequenceType);
		try {
			setState(1270);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__8:
				enterOuterAlt(_localctx, 1);
				{
				setState(1262);
				match(T__8);
				setState(1263);
				match(T__9);
				}
				break;
			case T__30:
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
				enterOuterAlt(_localctx, 2);
				{
				setState(1264);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(1268);
				this._errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(this._input,120,this._ctx) ) {
				case 1:
					{
					setState(1265);
					((SequenceTypeContext)_localctx).s134 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s134);
					}
					break;
				case 2:
					{
					setState(1266);
					((SequenceTypeContext)_localctx).s11 = match(T__10);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s11);
					}
					break;
				case 3:
					{
					setState(1267);
					((SequenceTypeContext)_localctx).s48 = match(T__47);
					((SequenceTypeContext)_localctx).plus.add(((SequenceTypeContext)_localctx).s48);
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
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectConstructorContext extends ParserRuleContext {
		public Token s59;
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
		ObjectConstructorContext _localctx = new ObjectConstructorContext(this._ctx, getState());
		enterRule(_localctx, 232, RULE_objectConstructor);
		int _la;
		try {
			setState(1288);
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(1272);
				match(T__6);
				setState(1281);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__6) | (1L << T__8) | (1L << T__12) | (1L << T__15) | (1L << T__30) | (1L << T__47) | (1L << T__48) | (1L << T__53) | (1L << T__56) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kloop - 128)) | (1L << (Kcontinue - 128)) | (1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
					{
					setState(1273);
					pairConstructor();
					setState(1278);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la==T__13) {
						{
						{
						setState(1274);
						match(T__13);
						setState(1275);
						pairConstructor();
						}
						}
						setState(1280);
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					}
				}

				setState(1283);
				match(T__7);
				}
				break;
			case T__58:
				enterOuterAlt(_localctx, 2);
				{
				setState(1284);
				((ObjectConstructorContext)_localctx).s59 = match(T__58);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s59);
				setState(1285);
				expr();
				setState(1286);
				match(T__59);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ItemTypeContext _localctx = new ItemTypeContext(this._ctx, getState());
		enterRule(_localctx, 234, RULE_itemType);
		try {
			setState(1293);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,125,this._ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1290);
				qname();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1291);
				match(NullLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1292);
				functionTest();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		FunctionTestContext _localctx = new FunctionTestContext(this._ctx, getState());
		enterRule(_localctx, 236, RULE_functionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1297);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,126,this._ctx) ) {
			case 1:
				{
				setState(1295);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(1296);
				typedFunctionTest();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		AnyFunctionTestContext _localctx = new AnyFunctionTestContext(this._ctx, getState());
		enterRule(_localctx, 238, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1299);
			match(T__30);
			setState(1300);
			match(T__8);
			setState(1301);
			match(T__10);
			setState(1302);
			match(T__9);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		TypedFunctionTestContext _localctx = new TypedFunctionTestContext(this._ctx, getState());
		enterRule(_localctx, 240, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1304);
			match(T__30);
			setState(1305);
			match(T__8);
			setState(1314);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__30) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kloop - 128)) | (1L << (Kcontinue - 128)) | (1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (NullLiteral - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1306);
				((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
				((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
				setState(1311);
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la==T__13) {
					{
					{
					setState(1307);
					match(T__13);
					setState(1308);
					((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
					((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
					}
					}
					setState(1313);
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				}
			}

			setState(1316);
			match(T__9);
			setState(1317);
			match(Kas);
			setState(1318);
			((TypedFunctionTestContext)_localctx).rt = sequenceType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SingleTypeContext extends ParserRuleContext {
		public ItemTypeContext item;
		public Token s134;
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
		SingleTypeContext _localctx = new SingleTypeContext(this._ctx, getState());
		enterRule(_localctx, 242, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1320);
			((SingleTypeContext)_localctx).item = itemType();
			setState(1322);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,129,this._ctx) ) {
			case 1:
				{
				setState(1321);
				((SingleTypeContext)_localctx).s134 = match(ArgumentPlaceholder);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).s134);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		PairConstructorContext _localctx = new PairConstructorContext(this._ctx, getState());
		enterRule(_localctx, 244, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1326);
			this._errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(this._input,130,this._ctx) ) {
			case 1:
				{
				setState(1324);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(1325);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(1328);
			_la = this._input.LA(1);
			if ( !(_la==T__17 || _la==ArgumentPlaceholder) ) {
			this._errHandler.recoverInline(this);
			}
			else {
				if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
				this._errHandler.reportMatch(this);
				consume();
			}
			setState(1329);
			((PairConstructorContext)_localctx).rhs = exprSingle();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		ArrayConstructorContext _localctx = new ArrayConstructorContext(this._ctx, getState());
		enterRule(_localctx, 246, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1331);
			match(T__53);
			setState(1333);
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__6) | (1L << T__8) | (1L << T__12) | (1L << T__15) | (1L << T__30) | (1L << T__47) | (1L << T__48) | (1L << T__53) | (1L << T__56) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (Kbreak - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (Kloop - 128)) | (1L << (Kcontinue - 128)) | (1L << (Kexit - 128)) | (1L << (Kreturning - 128)) | (1L << (Kwhile - 128)) | (1L << (STRING - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1332);
				expr();
				}
			}

			setState(1335);
			match(T__54);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		UriLiteralContext _localctx = new UriLiteralContext(this._ctx, getState());
		enterRule(_localctx, 248, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1337);
			stringLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		StringLiteralContext _localctx = new StringLiteralContext(this._ctx, getState());
		enterRule(_localctx, 250, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1339);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
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
		KeyWordsContext _localctx = new KeyWordsContext(this._ctx, getState());
		enterRule(_localctx, 252, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1341);
			_la = this._input.LA(1);
			if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (Kfor - 61)) | (1L << (Klet - 61)) | (1L << (Kwhere - 61)) | (1L << (Kgroup - 61)) | (1L << (Kby - 61)) | (1L << (Korder - 61)) | (1L << (Kreturn - 61)) | (1L << (Kif - 61)) | (1L << (Kin - 61)) | (1L << (Kas - 61)) | (1L << (Kat - 61)) | (1L << (Kallowing - 61)) | (1L << (Kempty - 61)) | (1L << (Kcount - 61)) | (1L << (Kstable - 61)) | (1L << (Kascending - 61)) | (1L << (Kdescending - 61)) | (1L << (Ksome - 61)) | (1L << (Kevery - 61)) | (1L << (Ksatisfies - 61)) | (1L << (Kcollation - 61)) | (1L << (Kgreatest - 61)) | (1L << (Kleast - 61)) | (1L << (Kswitch - 61)) | (1L << (Kcase - 61)) | (1L << (Ktry - 61)) | (1L << (Kcatch - 61)) | (1L << (Kdefault - 61)) | (1L << (Kthen - 61)) | (1L << (Kelse - 61)) | (1L << (Ktypeswitch - 61)) | (1L << (Kor - 61)) | (1L << (Kand - 61)) | (1L << (Knot - 61)) | (1L << (Kto - 61)) | (1L << (Kinstance - 61)) | (1L << (Kof - 61)) | (1L << (Kstatically - 61)) | (1L << (Kis - 61)) | (1L << (Ktreat - 61)) | (1L << (Kcast - 61)) | (1L << (Kcastable - 61)) | (1L << (Kversion - 61)) | (1L << (Kjsoniq - 61)) | (1L << (Kunordered - 61)) | (1L << (Ktrue - 61)) | (1L << (Kfalse - 61)) | (1L << (Ktype - 61)) | (1L << (Kvalidate - 61)) | (1L << (Kannotate - 61)) | (1L << (Kdeclare - 61)) | (1L << (Kcontext - 61)) | (1L << (Kitem - 61)) | (1L << (Kvariable - 61)) | (1L << (Kinsert - 61)) | (1L << (Kdelete - 61)) | (1L << (Krename - 61)) | (1L << (Kreplace - 61)) | (1L << (Kcopy - 61)) | (1L << (Kmodify - 61)) | (1L << (Kappend - 61)) | (1L << (Kinto - 61)) | (1L << (Kvalue - 61)) | (1L << (Kjson - 61)))) != 0) || ((((_la - 125)) & ~0x3f) == 0 && ((1L << (_la - 125)) & ((1L << (Kwith - 125)) | (1L << (Kposition - 125)) | (1L << (Kbreak - 125)) | (1L << (Kloop - 125)) | (1L << (Kcontinue - 125)) | (1L << (Kexit - 125)) | (1L << (Kreturning - 125)) | (1L << (Kwhile - 125)) | (1L << (NullLiteral - 125)))) != 0)) ) {
			this._errHandler.recoverInline(this);
			}
			else {
				if ( this._input.LA(1)==Token.EOF ) this.matchedEOF = true;
				this._errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			this._errHandler.reportError(this, re);
			this._errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0092\u0542\4\2\t"+
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
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3\u0109\n\3\3\3\3\3\5\3\u010d\n\3\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\5\6\u011d\n\6\3"+
		"\6\3\6\7\6\u0121\n\6\f\6\16\6\u0124\13\6\3\6\3\6\3\6\7\6\u0129\n\6\f\6"+
		"\16\6\u012c\13\6\3\7\3\7\3\b\7\b\u0131\n\b\f\b\16\b\u0134\13\b\3\t\3\t"+
		"\3\t\3\n\3\n\5\n\u013b\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\5\13\u014a\n\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\5\22\u0168\n\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\7\22\u0170\n\22\f\22\16\22\u0173\13\22\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\6\24\u0186"+
		"\n\24\r\24\16\24\u0187\3\24\3\24\3\24\3\24\3\25\3\25\6\25\u0190\n\25\r"+
		"\25\16\25\u0191\3\25\3\25\3\25\3\26\3\26\3\26\6\26\u019a\n\26\r\26\16"+
		"\26\u019b\3\27\3\27\3\27\5\27\u01a1\n\27\3\27\3\27\3\27\5\27\u01a6\n\27"+
		"\7\27\u01a8\n\27\f\27\16\27\u01ab\13\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\6\30\u01b4\n\30\r\30\16\30\u01b5\3\30\3\30\5\30\u01ba\n\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\5\31\u01c3\n\31\3\31\3\31\3\31\7\31\u01c8"+
		"\n\31\f\31\16\31\u01cb\13\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3"+
		"\32\7\32\u01d6\n\32\f\32\16\32\u01d9\13\32\3\32\5\32\u01dc\n\32\3\33\7"+
		"\33\u01df\n\33\f\33\16\33\u01e2\13\33\3\34\3\34\3\34\3\34\3\34\7\34\u01e9"+
		"\n\34\f\34\16\34\u01ec\13\34\3\34\3\34\3\35\3\35\3\35\5\35\u01f3\n\35"+
		"\3\35\3\35\5\35\u01f7\n\35\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\5\37\u0203\n\37\3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\5!\u020f\n!\3\"\3"+
		"\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\5%\u0225\n"+
		"%\3%\3%\3%\3%\7%\u022b\n%\f%\16%\u022e\13%\3&\3&\5&\u0232\n&\3&\5&\u0235"+
		"\n&\3&\3&\5&\u0239\n&\3\'\3\'\3(\3(\3(\3(\3(\5(\u0242\n(\3(\3(\3(\3(\3"+
		"(\7(\u0249\n(\f(\16(\u024c\13(\5(\u024e\n(\3)\3)\3)\3)\3)\3)\5)\u0256"+
		"\n)\3)\3)\3)\3)\3)\5)\u025d\n)\5)\u025f\n)\3*\3*\3*\3*\3*\5*\u0266\n*"+
		"\3*\3*\3*\3*\3*\5*\u026d\n*\5*\u026f\n*\3+\3+\3+\3+\3+\3+\5+\u0277\n+"+
		"\3+\3+\3+\5+\u027c\n+\3+\3+\3+\3+\3+\5+\u0283\n+\3,\3,\3,\3,\3,\5,\u028a"+
		"\n,\3,\3,\3-\3-\3-\3-\3-\3-\5-\u0294\n-\3.\3.\3.\7.\u0299\n.\f.\16.\u029c"+
		"\13.\3/\3/\3/\3/\5/\u02a2\n/\3\60\3\60\3\60\7\60\u02a7\n\60\f\60\16\60"+
		"\u02aa\13\60\3\61\3\61\3\61\3\61\3\61\3\61\5\61\u02b2\n\61\3\62\3\62\3"+
		"\62\3\62\3\62\3\62\3\62\3\62\5\62\u02bc\n\62\3\63\3\63\5\63\u02c0\n\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\7\63\u02c8\n\63\f\63\16\63\u02cb\13\63"+
		"\3\63\3\63\3\63\3\64\3\64\3\64\3\64\7\64\u02d4\n\64\f\64\16\64\u02d7\13"+
		"\64\3\65\3\65\3\65\5\65\u02dc\n\65\3\65\3\65\5\65\u02e0\n\65\3\65\3\65"+
		"\5\65\u02e4\n\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\7\66\u02ed\n\66\f"+
		"\66\16\66\u02f0\13\66\3\67\3\67\3\67\5\67\u02f5\n\67\3\67\3\67\3\67\3"+
		"8\38\38\39\39\39\39\39\79\u0302\n9\f9\169\u0305\139\3:\3:\3:\5:\u030a"+
		"\n:\3:\3:\5:\u030e\n:\3:\3:\5:\u0312\n:\3;\3;\3;\3;\3;\5;\u0319\n;\3;"+
		"\3;\3;\7;\u031e\n;\f;\16;\u0321\13;\3<\3<\3<\5<\u0326\n<\3<\3<\3<\5<\u032b"+
		"\n<\5<\u032d\n<\3<\3<\5<\u0331\n<\3=\3=\3=\3>\3>\5>\u0338\n>\3>\3>\3>"+
		"\7>\u033d\n>\f>\16>\u0340\13>\3>\3>\3>\3?\3?\3?\5?\u0348\n?\3?\3?\3?\3"+
		"@\3@\3@\3@\3@\6@\u0352\n@\r@\16@\u0353\3@\3@\3@\3@\3A\3A\6A\u035c\nA\r"+
		"A\16A\u035d\3A\3A\3A\3B\3B\3B\3B\3B\6B\u0368\nB\rB\16B\u0369\3B\3B\5B"+
		"\u036e\nB\3B\3B\3B\3C\3C\3C\3C\5C\u0377\nC\3C\3C\3C\7C\u037c\nC\fC\16"+
		"C\u037f\13C\3C\3C\3C\3D\3D\3D\3D\3D\3D\3D\3D\3D\3E\3E\3E\3E\3E\6E\u0392"+
		"\nE\rE\16E\u0393\3F\3F\3F\5F\u0399\nF\3F\3F\3F\5F\u039e\nF\7F\u03a0\n"+
		"F\fF\16F\u03a3\13F\3F\3F\3F\3F\3G\3G\3G\7G\u03ac\nG\fG\16G\u03af\13G\3"+
		"H\3H\3H\7H\u03b4\nH\fH\16H\u03b7\13H\3I\5I\u03ba\nI\3I\3I\3J\3J\3J\5J"+
		"\u03c1\nJ\3K\3K\3K\7K\u03c6\nK\fK\16K\u03c9\13K\3L\3L\3L\5L\u03ce\nL\3"+
		"M\3M\3M\7M\u03d3\nM\fM\16M\u03d6\13M\3N\3N\3N\7N\u03db\nN\fN\16N\u03de"+
		"\13N\3O\3O\3O\3O\5O\u03e4\nO\3P\3P\3P\3P\5P\u03ea\nP\3Q\3Q\3Q\3Q\5Q\u03f0"+
		"\nQ\3R\3R\3R\3R\5R\u03f6\nR\3S\3S\3S\3S\5S\u03fc\nS\3T\3T\3T\3T\3T\3T"+
		"\3T\7T\u0405\nT\fT\16T\u0408\13T\3U\3U\3U\5U\u040d\nU\3V\7V\u0410\nV\f"+
		"V\16V\u0413\13V\3V\3V\3W\3W\3W\5W\u041a\nW\3X\3X\3X\3X\3X\3X\3X\3Y\3Y"+
		"\3Y\3Y\3Y\3Y\3Y\3Z\3Z\3Z\7Z\u042d\nZ\fZ\16Z\u0430\13Z\3[\3[\3[\3[\3[\3"+
		"[\7[\u0438\n[\f[\16[\u043b\13[\3\\\3\\\3\\\3\\\3\\\3\\\3]\3]\3]\3^\3^"+
		"\3^\3^\3_\3_\3_\3_\3_\3_\3_\5_\u0451\n_\3`\3`\3`\3`\3`\3`\3`\3`\3`\3`"+
		"\3`\3`\3`\3`\3`\5`\u0462\n`\3a\3a\3a\3a\3b\3b\3b\3c\3c\5c\u046d\nc\3c"+
		"\3c\3d\3d\3e\3e\3e\3e\3e\3f\3f\3f\3f\3f\3g\3g\3g\3h\3h\3h\5h\u0483\nh"+
		"\7h\u0485\nh\fh\16h\u0488\13h\3h\3h\3i\3i\5i\u048e\ni\3j\3j\5j\u0492\n"+
		"j\3k\3k\3k\3k\3l\3l\3l\3l\5l\u049c\nl\3l\3l\3l\5l\u04a1\nl\3l\3l\3l\3"+
		"l\3m\3m\3m\3m\3m\3m\3m\3m\5m\u04af\nm\3m\3m\3m\3m\3m\7m\u04b6\nm\fm\16"+
		"m\u04b9\13m\3m\3m\3m\5m\u04be\nm\3n\3n\3n\3n\3o\3o\3o\3o\3o\3o\3p\3p\3"+
		"p\3p\3p\3p\3p\3p\3q\3q\3q\3q\7q\u04d6\nq\fq\16q\u04d9\13q\3q\3q\3q\3q"+
		"\3q\3r\3r\3r\3r\3r\3r\3s\3s\3s\6s\u04e9\ns\rs\16s\u04ea\3t\3t\3t\3t\3"+
		"u\3u\3u\3u\3u\3u\5u\u04f7\nu\5u\u04f9\nu\3v\3v\3v\3v\7v\u04ff\nv\fv\16"+
		"v\u0502\13v\5v\u0504\nv\3v\3v\3v\3v\3v\5v\u050b\nv\3w\3w\3w\5w\u0510\n"+
		"w\3x\3x\5x\u0514\nx\3y\3y\3y\3y\3y\3z\3z\3z\3z\3z\7z\u0520\nz\fz\16z\u0523"+
		"\13z\5z\u0525\nz\3z\3z\3z\3z\3{\3{\5{\u052d\n{\3|\3|\5|\u0531\n|\3|\3"+
		"|\3|\3}\3}\5}\u0538\n}\3}\3}\3~\3~\3\177\3\177\3\u0080\3\u0080\3\u0080"+
		"\2\2\u0081\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66"+
		"8:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a"+
		"\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2"+
		"\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba"+
		"\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2"+
		"\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea"+
		"\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\2\n\4\2\22"+
		"\22kk\3\2TU\3\2\25\36\4\2\6\6&\60\3\2\62\63\4\2\r\r\64\66\4\2\24\24\u0088"+
		"\u0088\4\2?\u0086\u0089\u0089\2\u0583\2\u0100\3\2\2\2\4\u0108\3\2\2\2"+
		"\6\u010e\3\2\2\2\b\u0111\3\2\2\2\n\u0122\3\2\2\2\f\u012d\3\2\2\2\16\u0132"+
		"\3\2\2\2\20\u0135\3\2\2\2\22\u0138\3\2\2\2\24\u0149\3\2\2\2\26\u014b\3"+
		"\2\2\2\30\u014e\3\2\2\2\32\u0154\3\2\2\2\34\u0158\3\2\2\2\36\u015c\3\2"+
		"\2\2 \u0160\3\2\2\2\"\u0167\3\2\2\2$\u0177\3\2\2\2&\u0180\3\2\2\2(\u018f"+
		"\3\2\2\2*\u0196\3\2\2\2,\u019d\3\2\2\2.\u01ae\3\2\2\2\60\u01be\3\2\2\2"+
		"\62\u01cf\3\2\2\2\64\u01e0\3\2\2\2\66\u01e3\3\2\2\28\u01ef\3\2\2\2:\u01f8"+
		"\3\2\2\2<\u0202\3\2\2\2>\u0204\3\2\2\2@\u020e\3\2\2\2B\u0210\3\2\2\2D"+
		"\u0215\3\2\2\2F\u0219\3\2\2\2H\u021f\3\2\2\2J\u0234\3\2\2\2L\u023a\3\2"+
		"\2\2N\u023c\3\2\2\2P\u024f\3\2\2\2R\u0260\3\2\2\2T\u0270\3\2\2\2V\u0284"+
		"\3\2\2\2X\u0293\3\2\2\2Z\u0295\3\2\2\2\\\u029d\3\2\2\2^\u02a3\3\2\2\2"+
		"`\u02b1\3\2\2\2b\u02bb\3\2\2\2d\u02bf\3\2\2\2f\u02cf\3\2\2\2h\u02d8\3"+
		"\2\2\2j\u02e8\3\2\2\2l\u02f1\3\2\2\2n\u02f9\3\2\2\2p\u02fc\3\2\2\2r\u0306"+
		"\3\2\2\2t\u0318\3\2\2\2v\u0322\3\2\2\2x\u0332\3\2\2\2z\u0337\3\2\2\2|"+
		"\u0344\3\2\2\2~\u034c\3\2\2\2\u0080\u035b\3\2\2\2\u0082\u0362\3\2\2\2"+
		"\u0084\u0372\3\2\2\2\u0086\u0383\3\2\2\2\u0088\u038c\3\2\2\2\u008a\u0395"+
		"\3\2\2\2\u008c\u03a8\3\2\2\2\u008e\u03b0\3\2\2\2\u0090\u03b9\3\2\2\2\u0092"+
		"\u03bd\3\2\2\2\u0094\u03c2\3\2\2\2\u0096\u03ca\3\2\2\2\u0098\u03cf\3\2"+
		"\2\2\u009a\u03d7\3\2\2\2\u009c\u03df\3\2\2\2\u009e\u03e5\3\2\2\2\u00a0"+
		"\u03eb\3\2\2\2\u00a2\u03f1\3\2\2\2\u00a4\u03f7\3\2\2\2\u00a6\u03fd\3\2"+
		"\2\2\u00a8\u040c\3\2\2\2\u00aa\u0411\3\2\2\2\u00ac\u0419\3\2\2\2\u00ae"+
		"\u041b\3\2\2\2\u00b0\u0422\3\2\2\2\u00b2\u0429\3\2\2\2\u00b4\u0431\3\2"+
		"\2\2\u00b6\u043c\3\2\2\2\u00b8\u0442\3\2\2\2\u00ba\u0445\3\2\2\2\u00bc"+
		"\u0449\3\2\2\2\u00be\u0461\3\2\2\2\u00c0\u0463\3\2\2\2\u00c2\u0467\3\2"+
		"\2\2\u00c4\u046a\3\2\2\2\u00c6\u0470\3\2\2\2\u00c8\u0472\3\2\2\2\u00ca"+
		"\u0477\3\2\2\2\u00cc\u047c\3\2\2\2\u00ce\u047f\3\2\2\2\u00d0\u048d\3\2"+
		"\2\2\u00d2\u0491\3\2\2\2\u00d4\u0493\3\2\2\2\u00d6\u0497\3\2\2\2\u00d8"+
		"\u04bd\3\2\2\2\u00da\u04bf\3\2\2\2\u00dc\u04c3\3\2\2\2\u00de\u04c9\3\2"+
		"\2\2\u00e0\u04d1\3\2\2\2\u00e2\u04df\3\2\2\2\u00e4\u04e5\3\2\2\2\u00e6"+
		"\u04ec\3\2\2\2\u00e8\u04f8\3\2\2\2\u00ea\u050a\3\2\2\2\u00ec\u050f\3\2"+
		"\2\2\u00ee\u0513\3\2\2\2\u00f0\u0515\3\2\2\2\u00f2\u051a\3\2\2\2\u00f4"+
		"\u052a\3\2\2\2\u00f6\u0530\3\2\2\2\u00f8\u0535\3\2\2\2\u00fa\u053b\3\2"+
		"\2\2\u00fc\u053d\3\2\2\2\u00fe\u053f\3\2\2\2\u0100\u0101\5\4\3\2\u0101"+
		"\u0102\7\2\2\3\u0102\3\3\2\2\2\u0103\u0104\7j\2\2\u0104\u0105\7i\2\2\u0105"+
		"\u0106\5\u00fc\177\2\u0106\u0107\7\3\2\2\u0107\u0109\3\2\2\2\u0108\u0103"+
		"\3\2\2\2\u0108\u0109\3\2\2\2\u0109\u010c\3\2\2\2\u010a\u010d\5\b\5\2\u010b"+
		"\u010d\5\6\4\2\u010c\u010a\3\2\2\2\u010c\u010b\3\2\2\2\u010d\5\3\2\2\2"+
		"\u010e\u010f\5\n\6\2\u010f\u0110\5\f\7\2\u0110\7\3\2\2\2\u0111\u0112\7"+
		"\4\2\2\u0112\u0113\7\5\2\2\u0113\u0114\7\u0090\2\2\u0114\u0115\7\6\2\2"+
		"\u0115\u0116\5\u00fa~\2\u0116\u0117\7\3\2\2\u0117\u0118\5\n\6\2\u0118"+
		"\t\3\2\2\2\u0119\u011d\5<\37\2\u011a\u011d\5> \2\u011b\u011d\5N(\2\u011c"+
		"\u0119\3\2\2\2\u011c\u011a\3\2\2\2\u011c\u011b\3\2\2\2\u011d\u011e\3\2"+
		"\2\2\u011e\u011f\7\3\2\2\u011f\u0121\3\2\2\2\u0120\u011c\3\2\2\2\u0121"+
		"\u0124\3\2\2\2\u0122\u0120\3\2\2\2\u0122\u0123\3\2\2\2\u0123\u012a\3\2"+
		"\2\2\u0124\u0122\3\2\2\2\u0125\u0126\5@!\2\u0126\u0127\7\3\2\2\u0127\u0129"+
		"\3\2\2\2\u0128\u0125\3\2\2\2\u0129\u012c\3\2\2\2\u012a\u0128\3\2\2\2\u012a"+
		"\u012b\3\2\2\2\u012b\13\3\2\2\2\u012c\u012a\3\2\2\2\u012d\u012e\5\22\n"+
		"\2\u012e\r\3\2\2\2\u012f\u0131\5\24\13\2\u0130\u012f\3\2\2\2\u0131\u0134"+
		"\3\2\2\2\u0132\u0130\3\2\2\2\u0132\u0133\3\2\2\2\u0133\17\3\2\2\2\u0134"+
		"\u0132\3\2\2\2\u0135\u0136\5\16\b\2\u0136\u0137\5^\60\2\u0137\21\3\2\2"+
		"\2\u0138\u013a\5\16\b\2\u0139\u013b\5^\60\2\u013a\u0139\3\2\2\2\u013a"+
		"\u013b\3\2\2\2\u013b\23\3\2\2\2\u013c\u014a\5\26\f\2\u013d\u014a\5\30"+
		"\r\2\u013e\u014a\5\32\16\2\u013f\u014a\5\34\17\2\u0140\u014a\5\36\20\2"+
		"\u0141\u014a\5 \21\2\u0142\u014a\5\"\22\2\u0143\u014a\5$\23\2\u0144\u014a"+
		"\5&\24\2\u0145\u014a\5*\26\2\u0146\u014a\5.\30\2\u0147\u014a\5\66\34\2"+
		"\u0148\u014a\5:\36\2\u0149\u013c\3\2\2\2\u0149\u013d\3\2\2\2\u0149\u013e"+
		"\3\2\2\2\u0149\u013f\3\2\2\2\u0149\u0140\3\2\2\2\u0149\u0141\3\2\2\2\u0149"+
		"\u0142\3\2\2\2\u0149\u0143\3\2\2\2\u0149\u0144\3\2\2\2\u0149\u0145\3\2"+
		"\2\2\u0149\u0146\3\2\2\2\u0149\u0147\3\2\2\2\u0149\u0148\3\2\2\2\u014a"+
		"\25\3\2\2\2\u014b\u014c\5b\62\2\u014c\u014d\7\3\2\2\u014d\27\3\2\2\2\u014e"+
		"\u014f\7\7\2\2\u014f\u0150\5J&\2\u0150\u0151\7\b\2\2\u0151\u0152\5`\61"+
		"\2\u0152\u0153\7\3\2\2\u0153\31\3\2\2\2\u0154\u0155\7\t\2\2\u0155\u0156"+
		"\5\16\b\2\u0156\u0157\7\n\2\2\u0157\33\3\2\2\2\u0158\u0159\7\u0081\2\2"+
		"\u0159\u015a\7\u0082\2\2\u015a\u015b\7\3\2\2\u015b\35\3\2\2\2\u015c\u015d"+
		"\7\u0083\2\2\u015d\u015e\7\u0082\2\2\u015e\u015f\7\3\2\2\u015f\37\3\2"+
		"\2\2\u0160\u0161\7\u0084\2\2\u0161\u0162\7\u0085\2\2\u0162\u0163\5`\61"+
		"\2\u0163\u0164\7\3\2\2\u0164!\3\2\2\2\u0165\u0168\5f\64\2\u0166\u0168"+
		"\5j\66\2\u0167\u0165\3\2\2\2\u0167\u0166\3\2\2\2\u0168\u0171\3\2\2\2\u0169"+
		"\u0170\5f\64\2\u016a\u0170\5j\66\2\u016b\u0170\5n8\2\u016c\u0170\5p9\2"+
		"\u016d\u0170\5t;\2\u016e\u0170\5x=\2\u016f\u0169\3\2\2\2\u016f\u016a\3"+
		"\2\2\2\u016f\u016b\3\2\2\2\u016f\u016c\3\2\2\2\u016f\u016d\3\2\2\2\u016f"+
		"\u016e\3\2\2\2\u0170\u0173\3\2\2\2\u0171\u016f\3\2\2\2\u0171\u0172\3\2"+
		"\2\2\u0172\u0174\3\2\2\2\u0173\u0171\3\2\2\2\u0174\u0175\7E\2\2\u0175"+
		"\u0176\5\24\13\2\u0176#\3\2\2\2\u0177\u0178\7F\2\2\u0178\u0179\7\13\2"+
		"\2\u0179\u017a\5^\60\2\u017a\u017b\7\f\2\2\u017b\u017c\7[\2\2\u017c\u017d"+
		"\5\24\13\2\u017d\u017e\7\\\2\2\u017e\u017f\5\24\13\2\u017f%\3\2\2\2\u0180"+
		"\u0181\7V\2\2\u0181\u0182\7\13\2\2\u0182\u0183\5^\60\2\u0183\u0185\7\f"+
		"\2\2\u0184\u0186\5(\25\2\u0185\u0184\3\2\2\2\u0186\u0187\3\2\2\2\u0187"+
		"\u0185\3\2\2\2\u0187\u0188\3\2\2\2\u0188\u0189\3\2\2\2\u0189\u018a\7Z"+
		"\2\2\u018a\u018b\7E\2\2\u018b\u018c\5\24\13\2\u018c\'\3\2\2\2\u018d\u018e"+
		"\7W\2\2\u018e\u0190\5`\61\2\u018f\u018d\3\2\2\2\u0190\u0191\3\2\2\2\u0191"+
		"\u018f\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0194\7E"+
		"\2\2\u0194\u0195\5\24\13\2\u0195)\3\2\2\2\u0196\u0197\7X\2\2\u0197\u0199"+
		"\5\32\16\2\u0198\u019a\5,\27\2\u0199\u0198\3\2\2\2\u019a\u019b\3\2\2\2"+
		"\u019b\u0199\3\2\2\2\u019b\u019c\3\2\2\2\u019c+\3\2\2\2\u019d\u01a0\7"+
		"Y\2\2\u019e\u01a1\7\r\2\2\u019f\u01a1\5J&\2\u01a0\u019e\3\2\2\2\u01a0"+
		"\u019f\3\2\2\2\u01a1\u01a9\3\2\2\2\u01a2\u01a5\7\16\2\2\u01a3\u01a6\7"+
		"\r\2\2\u01a4\u01a6\5J&\2\u01a5\u01a3\3\2\2\2\u01a5\u01a4\3\2\2\2\u01a6"+
		"\u01a8\3\2\2\2\u01a7\u01a2\3\2\2\2\u01a8\u01ab\3\2\2\2\u01a9\u01a7\3\2"+
		"\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ac\3\2\2\2\u01ab\u01a9\3\2\2\2\u01ac"+
		"\u01ad\5\32\16\2\u01ad-\3\2\2\2\u01ae\u01af\7]\2\2\u01af\u01b0\7\13\2"+
		"\2\u01b0\u01b1\5^\60\2\u01b1\u01b3\7\f\2\2\u01b2\u01b4\5\60\31\2\u01b3"+
		"\u01b2\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b3\3\2\2\2\u01b5\u01b6\3\2"+
		"\2\2\u01b6\u01b7\3\2\2\2\u01b7\u01b9\7Z\2\2\u01b8\u01ba\5\u00c2b\2\u01b9"+
		"\u01b8\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01bc\7E"+
		"\2\2\u01bc\u01bd\5\24\13\2\u01bd/\3\2\2\2\u01be\u01c2\7W\2\2\u01bf\u01c0"+
		"\5\u00c2b\2\u01c0\u01c1\7H\2\2\u01c1\u01c3\3\2\2\2\u01c2\u01bf\3\2\2\2"+
		"\u01c2\u01c3\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4\u01c9\5\u00e8u\2\u01c5"+
		"\u01c6\7\16\2\2\u01c6\u01c8\5\u00e8u\2\u01c7\u01c5\3\2\2\2\u01c8\u01cb"+
		"\3\2\2\2\u01c9\u01c7\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cc\3\2\2\2\u01cb"+
		"\u01c9\3\2\2\2\u01cc\u01cd\7E\2\2\u01cd\u01ce\5\24\13\2\u01ce\61\3\2\2"+
		"\2\u01cf\u01d0\7\17\2\2\u01d0\u01db\5J&\2\u01d1\u01d2\7\13\2\2\u01d2\u01d7"+
		"\7\u008a\2\2\u01d3\u01d4\7\20\2\2\u01d4\u01d6\7\u008a\2\2\u01d5\u01d3"+
		"\3\2\2\2\u01d6\u01d9\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8"+
		"\u01da\3\2\2\2\u01d9\u01d7\3\2\2\2\u01da\u01dc\7\f\2\2\u01db\u01d1\3\2"+
		"\2\2\u01db\u01dc\3\2\2\2\u01dc\63\3\2\2\2\u01dd\u01df\5\62\32\2\u01de"+
		"\u01dd\3\2\2\2\u01df\u01e2\3\2\2\2\u01e0\u01de\3\2\2\2\u01e0\u01e1\3\2"+
		"\2\2\u01e1\65\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e3\u01e4\5\64\33\2\u01e4"+
		"\u01e5\7t\2\2\u01e5\u01ea\58\35\2\u01e6\u01e7\7\20\2\2\u01e7\u01e9\58"+
		"\35\2\u01e8\u01e6\3\2\2\2\u01e9\u01ec\3\2\2\2\u01ea\u01e8\3\2\2\2\u01ea"+
		"\u01eb\3\2\2\2\u01eb\u01ed\3\2\2\2\u01ec\u01ea\3\2\2\2\u01ed\u01ee\7\3"+
		"\2\2\u01ee\67\3\2\2\2\u01ef\u01f2\5\u00c2b\2\u01f0\u01f1\7H\2\2\u01f1"+
		"\u01f3\5\u00e8u\2\u01f2\u01f0\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f6"+
		"\3\2\2\2\u01f4\u01f5\7\b\2\2\u01f5\u01f7\5`\61\2\u01f6\u01f4\3\2\2\2\u01f6"+
		"\u01f7\3\2\2\2\u01f79\3\2\2\2\u01f8\u01f9\7\u0086\2\2\u01f9\u01fa\7\13"+
		"\2\2\u01fa\u01fb\5^\60\2\u01fb\u01fc\7\f\2\2\u01fc\u01fd\5\24\13\2\u01fd"+
		";\3\2\2\2\u01fe\u0203\5B\"\2\u01ff\u0203\5D#\2\u0200\u0203\5F$\2\u0201"+
		"\u0203\5H%\2\u0202\u01fe\3\2\2\2\u0202\u01ff\3\2\2\2\u0202\u0200\3\2\2"+
		"\2\u0202\u0201\3\2\2\2\u0203=\3\2\2\2\u0204\u0205\7q\2\2\u0205\u0206\7"+
		"\5\2\2\u0206\u0207\7\u0090\2\2\u0207\u0208\7\6\2\2\u0208\u0209\5\u00fa"+
		"~\2\u0209?\3\2\2\2\u020a\u020f\5T+\2\u020b\u020f\5P)\2\u020c\u020f\5V"+
		",\2\u020d\u020f\5R*\2\u020e\u020a\3\2\2\2\u020e\u020b\3\2\2\2\u020e\u020c"+
		"\3\2\2\2\u020e\u020d\3\2\2\2\u020fA\3\2\2\2\u0210\u0211\7q\2\2\u0211\u0212"+
		"\7Z\2\2\u0212\u0213\7S\2\2\u0213\u0214\5\u00fa~\2\u0214C\3\2\2\2\u0215"+
		"\u0216\7q\2\2\u0216\u0217\7\21\2\2\u0217\u0218\t\2\2\2\u0218E\3\2\2\2"+
		"\u0219\u021a\7q\2\2\u021a\u021b\7Z\2\2\u021b\u021c\7D\2\2\u021c\u021d"+
		"\7K\2\2\u021d\u021e\t\3\2\2\u021eG\3\2\2\2\u021f\u0224\7q\2\2\u0220\u0221"+
		"\7\23\2\2\u0221\u0225\5J&\2\u0222\u0223\7Z\2\2\u0223\u0225\7\23\2\2\u0224"+
		"\u0220\3\2\2\2\u0224\u0222\3\2\2\2\u0225\u022c\3\2\2\2\u0226\u0227\5L"+
		"\'\2\u0227\u0228\7\6\2\2\u0228\u0229\5\u00fc\177\2\u0229\u022b\3\2\2\2"+
		"\u022a\u0226\3\2\2\2\u022b\u022e\3\2\2\2\u022c\u022a\3\2\2\2\u022c\u022d"+
		"\3\2\2\2\u022dI\3\2\2\2\u022e\u022c\3\2\2\2\u022f\u0232\7\u0090\2\2\u0230"+
		"\u0232\5\u00fe\u0080\2\u0231\u022f\3\2\2\2\u0231\u0230\3\2\2\2\u0232\u0233"+
		"\3\2\2\2\u0233\u0235\7\24\2\2\u0234\u0231\3\2\2\2\u0234\u0235\3\2\2\2"+
		"\u0235\u0238\3\2\2\2\u0236\u0239\7\u0090\2\2\u0237\u0239\5\u00fe\u0080"+
		"\2\u0238\u0236\3\2\2\2\u0238\u0237\3\2\2\2\u0239K\3\2\2\2\u023a\u023b"+
		"\t\4\2\2\u023bM\3\2\2\2\u023c\u023d\7\37\2\2\u023d\u0241\7\4\2\2\u023e"+
		"\u023f\7\5\2\2\u023f\u0240\7\u0090\2\2\u0240\u0242\7\6\2\2\u0241\u023e"+
		"\3\2\2\2\u0241\u0242\3\2\2\2\u0242\u0243\3\2\2\2\u0243\u024d\5\u00fa~"+
		"\2\u0244\u0245\7I\2\2\u0245\u024a\5\u00fa~\2\u0246\u0247\7\20\2\2\u0247"+
		"\u0249\5\u00fa~\2\u0248\u0246\3\2\2\2\u0249\u024c\3\2\2\2\u024a\u0248"+
		"\3\2\2\2\u024a\u024b\3\2\2\2\u024b\u024e\3\2\2\2\u024c\u024a\3\2\2\2\u024d"+
		"\u0244\3\2\2\2\u024d\u024e\3\2\2\2\u024eO\3\2\2\2\u024f\u0250\7q\2\2\u0250"+
		"\u0251\5\64\33\2\u0251\u0252\7t\2\2\u0252\u0255\5\u00c2b\2\u0253\u0254"+
		"\7H\2\2\u0254\u0256\5\u00e8u\2\u0255\u0253\3\2\2\2\u0255\u0256\3\2\2\2"+
		"\u0256\u025e\3\2\2\2\u0257\u0258\7\b\2\2\u0258\u025f\5`\61\2\u0259\u025c"+
		"\7 \2\2\u025a\u025b\7\b\2\2\u025b\u025d\5`\61\2\u025c\u025a\3\2\2\2\u025c"+
		"\u025d\3\2\2\2\u025d\u025f\3\2\2\2\u025e\u0257\3\2\2\2\u025e\u0259\3\2"+
		"\2\2\u025fQ\3\2\2\2\u0260\u0261\7q\2\2\u0261\u0262\7r\2\2\u0262\u0265"+
		"\7s\2\2\u0263\u0264\7H\2\2\u0264\u0266\5\u00e8u\2\u0265\u0263\3\2\2\2"+
		"\u0265\u0266\3\2\2\2\u0266\u026e\3\2\2\2\u0267\u0268\7\b\2\2\u0268\u026f"+
		"\5`\61\2\u0269\u026c\7 \2\2\u026a\u026b\7\b\2\2\u026b\u026d\5`\61\2\u026c"+
		"\u026a\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u026f\3\2\2\2\u026e\u0267\3\2"+
		"\2\2\u026e\u0269\3\2\2\2\u026fS\3\2\2\2\u0270\u0271\7q\2\2\u0271\u0272"+
		"\5\64\33\2\u0272\u0273\7!\2\2\u0273\u0274\5J&\2\u0274\u0276\7\13\2\2\u0275"+
		"\u0277\5Z.\2\u0276\u0275\3\2\2\2\u0276\u0277\3\2\2\2\u0277\u0278\3\2\2"+
		"\2\u0278\u027b\7\f\2\2\u0279\u027a\7H\2\2\u027a\u027c\5\u00e8u\2\u027b"+
		"\u0279\3\2\2\2\u027b\u027c\3\2\2\2\u027c\u0282\3\2\2\2\u027d\u027e\7\t"+
		"\2\2\u027e\u027f\5\22\n\2\u027f\u0280\7\n\2\2\u0280\u0283\3\2\2\2\u0281"+
		"\u0283\7 \2\2\u0282\u027d\3\2\2\2\u0282\u0281\3\2\2\2\u0283U\3\2\2\2\u0284"+
		"\u0285\7q\2\2\u0285\u0286\7n\2\2\u0286\u0287\5J&\2\u0287\u0289\7H\2\2"+
		"\u0288\u028a\5X-\2\u0289\u0288\3\2\2\2\u0289\u028a\3\2\2\2\u028a\u028b"+
		"\3\2\2\2\u028b\u028c\5`\61\2\u028cW\3\2\2\2\u028d\u028e\7\"\2\2\u028e"+
		"\u0294\7#\2\2\u028f\u0290\7\"\2\2\u0290\u0294\7$\2\2\u0291\u0292\7~\2"+
		"\2\u0292\u0294\7%\2\2\u0293\u028d\3\2\2\2\u0293\u028f\3\2\2\2\u0293\u0291"+
		"\3\2\2\2\u0294Y\3\2\2\2\u0295\u029a\5\\/\2\u0296\u0297\7\20\2\2\u0297"+
		"\u0299\5\\/\2\u0298\u0296\3\2\2\2\u0299\u029c\3\2\2\2\u029a\u0298\3\2"+
		"\2\2\u029a\u029b\3\2\2\2\u029b[\3\2\2\2\u029c\u029a\3\2\2\2\u029d\u029e"+
		"\7\7\2\2\u029e\u02a1\5J&\2\u029f\u02a0\7H\2\2\u02a0\u02a2\5\u00e8u\2\u02a1"+
		"\u029f\3\2\2\2\u02a1\u02a2\3\2\2\2\u02a2]\3\2\2\2\u02a3\u02a8\5`\61\2"+
		"\u02a4\u02a5\7\20\2\2\u02a5\u02a7\5`\61\2\u02a6\u02a4\3\2\2\2\u02a7\u02aa"+
		"\3\2\2\2\u02a8\u02a6\3\2\2\2\u02a8\u02a9\3\2\2\2\u02a9_\3\2\2\2\u02aa"+
		"\u02a8\3\2\2\2\u02ab\u02b2\5b\62\2\u02ac\u02b2\5d\63\2\u02ad\u02b2\5~"+
		"@\2\u02ae\u02b2\5\u0082B\2\u02af\u02b2\5\u0086D\2\u02b0\u02b2\5\u0088"+
		"E\2\u02b1\u02ab\3\2\2\2\u02b1\u02ac\3\2\2\2\u02b1\u02ad\3\2\2\2\u02b1"+
		"\u02ae\3\2\2\2\u02b1\u02af\3\2\2\2\u02b1\u02b0\3\2\2\2\u02b2a\3\2\2\2"+
		"\u02b3\u02bc\5z>\2\u02b4\u02bc\5\u008cG\2\u02b5\u02bc\5\u00d8m\2\u02b6"+
		"\u02bc\5\u00dan\2\u02b7\u02bc\5\u00dco\2\u02b8\u02bc\5\u00dep\2\u02b9"+
		"\u02bc\5\u00e0q\2\u02ba\u02bc\5\u00e2r\2\u02bb\u02b3\3\2\2\2\u02bb\u02b4"+
		"\3\2\2\2\u02bb\u02b5\3\2\2\2\u02bb\u02b6\3\2\2\2\u02bb\u02b7\3\2\2\2\u02bb"+
		"\u02b8\3\2\2\2\u02bb\u02b9\3\2\2\2\u02bb\u02ba\3\2\2\2\u02bcc\3\2\2\2"+
		"\u02bd\u02c0\5f\64\2\u02be\u02c0\5j\66\2\u02bf\u02bd\3\2\2\2\u02bf\u02be"+
		"\3\2\2\2\u02c0\u02c9\3\2\2\2\u02c1\u02c8\5f\64\2\u02c2\u02c8\5j\66\2\u02c3"+
		"\u02c8\5n8\2\u02c4\u02c8\5p9\2\u02c5\u02c8\5t;\2\u02c6\u02c8\5x=\2\u02c7"+
		"\u02c1\3\2\2\2\u02c7\u02c2\3\2\2\2\u02c7\u02c3\3\2\2\2\u02c7\u02c4\3\2"+
		"\2\2\u02c7\u02c5\3\2\2\2\u02c7\u02c6\3\2\2\2\u02c8\u02cb\3\2\2\2\u02c9"+
		"\u02c7\3\2\2\2\u02c9\u02ca\3\2\2\2\u02ca\u02cc\3\2\2\2\u02cb\u02c9\3\2"+
		"\2\2\u02cc\u02cd\7E\2\2\u02cd\u02ce\5`\61\2\u02cee\3\2\2\2\u02cf\u02d0"+
		"\7?\2\2\u02d0\u02d5\5h\65\2\u02d1\u02d2\7\20\2\2\u02d2\u02d4\5h\65\2\u02d3"+
		"\u02d1\3\2\2\2\u02d4\u02d7\3\2\2\2\u02d5\u02d3\3\2\2\2\u02d5\u02d6\3\2"+
		"\2\2\u02d6g\3\2\2\2\u02d7\u02d5\3\2\2\2\u02d8\u02db\5\u00c2b\2\u02d9\u02da"+
		"\7H\2\2\u02da\u02dc\5\u00e8u\2\u02db\u02d9\3\2\2\2\u02db\u02dc\3\2\2\2"+
		"\u02dc\u02df\3\2\2\2\u02dd\u02de\7J\2\2\u02de\u02e0\7K\2\2\u02df\u02dd"+
		"\3\2\2\2\u02df\u02e0\3\2\2\2\u02e0\u02e3\3\2\2\2\u02e1\u02e2\7I\2\2\u02e2"+
		"\u02e4\5\u00c2b\2\u02e3\u02e1\3\2\2\2\u02e3\u02e4\3\2\2\2\u02e4\u02e5"+
		"\3\2\2\2\u02e5\u02e6\7G\2\2\u02e6\u02e7\5`\61\2\u02e7i\3\2\2\2\u02e8\u02e9"+
		"\7@\2\2\u02e9\u02ee\5l\67\2\u02ea\u02eb\7\20\2\2\u02eb\u02ed\5l\67\2\u02ec"+
		"\u02ea\3\2\2\2\u02ed\u02f0\3\2\2\2\u02ee\u02ec\3\2\2\2\u02ee\u02ef\3\2"+
		"\2\2\u02efk\3\2\2\2\u02f0\u02ee\3\2\2\2\u02f1\u02f4\5\u00c2b\2\u02f2\u02f3"+
		"\7H\2\2\u02f3\u02f5\5\u00e8u\2\u02f4\u02f2\3\2\2\2\u02f4\u02f5\3\2\2\2"+
		"\u02f5\u02f6\3\2\2\2\u02f6\u02f7\7\b\2\2\u02f7\u02f8\5`\61\2\u02f8m\3"+
		"\2\2\2\u02f9\u02fa\7A\2\2\u02fa\u02fb\5`\61\2\u02fbo\3\2\2\2\u02fc\u02fd"+
		"\7B\2\2\u02fd\u02fe\7C\2\2\u02fe\u0303\5r:\2\u02ff\u0300\7\20\2\2\u0300"+
		"\u0302\5r:\2\u0301\u02ff\3\2\2\2\u0302\u0305\3\2\2\2\u0303\u0301\3\2\2"+
		"\2\u0303\u0304\3\2\2\2\u0304q\3\2\2\2\u0305\u0303\3\2\2\2\u0306\u030d"+
		"\5\u00c2b\2\u0307\u0308\7H\2\2\u0308\u030a\5\u00e8u\2\u0309\u0307\3\2"+
		"\2\2\u0309\u030a\3\2\2\2\u030a\u030b\3\2\2\2\u030b\u030c\7\b\2\2\u030c"+
		"\u030e\5`\61\2\u030d\u0309\3\2\2\2\u030d\u030e\3\2\2\2\u030e\u0311\3\2"+
		"\2\2\u030f\u0310\7S\2\2\u0310\u0312\5\u00fa~\2\u0311\u030f\3\2\2\2\u0311"+
		"\u0312\3\2\2\2\u0312s\3\2\2\2\u0313\u0314\7D\2\2\u0314\u0319\7C\2\2\u0315"+
		"\u0316\7M\2\2\u0316\u0317\7D\2\2\u0317\u0319\7C\2\2\u0318\u0313\3\2\2"+
		"\2\u0318\u0315\3\2\2\2\u0319\u031a\3\2\2\2\u031a\u031f\5v<\2\u031b\u031c"+
		"\7\20\2\2\u031c\u031e\5v<\2\u031d\u031b\3\2\2\2\u031e\u0321\3\2\2\2\u031f"+
		"\u031d\3\2\2\2\u031f\u0320\3\2\2\2\u0320u\3\2\2\2\u0321\u031f\3\2\2\2"+
		"\u0322\u0325\5`\61\2\u0323\u0326\7N\2\2\u0324\u0326\7O\2\2\u0325\u0323"+
		"\3\2\2\2\u0325\u0324\3\2\2\2\u0325\u0326\3\2\2\2\u0326\u032c\3\2\2\2\u0327"+
		"\u032a\7K\2\2\u0328\u032b\7T\2\2\u0329\u032b\7U\2\2\u032a\u0328\3\2\2"+
		"\2\u032a\u0329\3\2\2\2\u032b\u032d\3\2\2\2\u032c\u0327\3\2\2\2\u032c\u032d"+
		"\3\2\2\2\u032d\u0330\3\2\2\2\u032e\u032f\7S\2\2\u032f\u0331\5\u00fa~\2"+
		"\u0330\u032e\3\2\2\2\u0330\u0331\3\2\2\2\u0331w\3\2\2\2\u0332\u0333\7"+
		"L\2\2\u0333\u0334\5\u00c2b\2\u0334y\3\2\2\2\u0335\u0338\7P\2\2\u0336\u0338"+
		"\7Q\2\2\u0337\u0335\3\2\2\2\u0337\u0336\3\2\2\2\u0338\u0339\3\2\2\2\u0339"+
		"\u033e\5|?\2\u033a\u033b\7\20\2\2\u033b\u033d\5|?\2\u033c\u033a\3\2\2"+
		"\2\u033d\u0340\3\2\2\2\u033e\u033c\3\2\2\2\u033e\u033f\3\2\2\2\u033f\u0341"+
		"\3\2\2\2\u0340\u033e\3\2\2\2\u0341\u0342\7R\2\2\u0342\u0343\5`\61\2\u0343"+
		"{\3\2\2\2\u0344\u0347\5\u00c2b\2\u0345\u0346\7H\2\2\u0346\u0348\5\u00e8"+
		"u\2\u0347\u0345\3\2\2\2\u0347\u0348\3\2\2\2\u0348\u0349\3\2\2\2\u0349"+
		"\u034a\7G\2\2\u034a\u034b\5`\61\2\u034b}\3\2\2\2\u034c\u034d\7V\2\2\u034d"+
		"\u034e\7\13\2\2\u034e\u034f\5^\60\2\u034f\u0351\7\f\2\2\u0350\u0352\5"+
		"\u0080A\2\u0351\u0350\3\2\2\2\u0352\u0353\3\2\2\2\u0353\u0351\3\2\2\2"+
		"\u0353\u0354\3\2\2\2\u0354\u0355\3\2\2\2\u0355\u0356\7Z\2\2\u0356\u0357"+
		"\7E\2\2\u0357\u0358\5`\61\2\u0358\177\3\2\2\2\u0359\u035a\7W\2\2\u035a"+
		"\u035c\5`\61\2\u035b\u0359\3\2\2\2\u035c\u035d\3\2\2\2\u035d\u035b\3\2"+
		"\2\2\u035d\u035e\3\2\2\2\u035e\u035f\3\2\2\2\u035f\u0360\7E\2\2\u0360"+
		"\u0361\5`\61\2\u0361\u0081\3\2\2\2\u0362\u0363\7]\2\2\u0363\u0364\7\13"+
		"\2\2\u0364\u0365\5^\60\2\u0365\u0367\7\f\2\2\u0366\u0368\5\u0084C\2\u0367"+
		"\u0366\3\2\2\2\u0368\u0369\3\2\2\2\u0369\u0367\3\2\2\2\u0369\u036a\3\2"+
		"\2\2\u036a\u036b\3\2\2\2\u036b\u036d\7Z\2\2\u036c\u036e\5\u00c2b\2\u036d"+
		"\u036c\3\2\2\2\u036d\u036e\3\2\2\2\u036e\u036f\3\2\2\2\u036f\u0370\7E"+
		"\2\2\u0370\u0371\5`\61\2\u0371\u0083\3\2\2\2\u0372\u0376\7W\2\2\u0373"+
		"\u0374\5\u00c2b\2\u0374\u0375\7H\2\2\u0375\u0377\3\2\2\2\u0376\u0373\3"+
		"\2\2\2\u0376\u0377\3\2\2\2\u0377\u0378\3\2\2\2\u0378\u037d\5\u00e8u\2"+
		"\u0379\u037a\7\16\2\2\u037a\u037c\5\u00e8u\2\u037b\u0379\3\2\2\2\u037c"+
		"\u037f\3\2\2\2\u037d\u037b\3\2\2\2\u037d\u037e\3\2\2\2\u037e\u0380\3\2"+
		"\2\2\u037f\u037d\3\2\2\2\u0380\u0381\7E\2\2\u0381\u0382\5`\61\2\u0382"+
		"\u0085\3\2\2\2\u0383\u0384\7F\2\2\u0384\u0385\7\13\2\2\u0385\u0386\5^"+
		"\60\2\u0386\u0387\7\f\2\2\u0387\u0388\7[\2\2\u0388\u0389\5`\61\2\u0389"+
		"\u038a\7\\\2\2\u038a\u038b\5`\61\2\u038b\u0087\3\2\2\2\u038c\u038d\7X"+
		"\2\2\u038d\u038e\7\t\2\2\u038e\u038f\5^\60\2\u038f\u0391\7\n\2\2\u0390"+
		"\u0392\5\u008aF\2\u0391\u0390\3\2\2\2\u0392\u0393\3\2\2\2\u0393\u0391"+
		"\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0089\3\2\2\2\u0395\u0398\7Y\2\2\u0396"+
		"\u0399\7\r\2\2\u0397\u0399\5J&\2\u0398\u0396\3\2\2\2\u0398\u0397\3\2\2"+
		"\2\u0399\u03a1\3\2\2\2\u039a\u039d\7\16\2\2\u039b\u039e\7\r\2\2\u039c"+
		"\u039e\5J&\2\u039d\u039b\3\2\2\2\u039d\u039c\3\2\2\2\u039e\u03a0\3\2\2"+
		"\2\u039f\u039a\3\2\2\2\u03a0\u03a3\3\2\2\2\u03a1\u039f\3\2\2\2\u03a1\u03a2"+
		"\3\2\2\2\u03a2\u03a4\3\2\2\2\u03a3\u03a1\3\2\2\2\u03a4\u03a5\7\t\2\2\u03a5"+
		"\u03a6\5^\60\2\u03a6\u03a7\7\n\2\2\u03a7\u008b\3\2\2\2\u03a8\u03ad\5\u008e"+
		"H\2\u03a9\u03aa\7^\2\2\u03aa\u03ac\5\u008eH\2\u03ab\u03a9\3\2\2\2\u03ac"+
		"\u03af\3\2\2\2\u03ad\u03ab\3\2\2\2\u03ad\u03ae\3\2\2\2\u03ae\u008d\3\2"+
		"\2\2\u03af\u03ad\3\2\2\2\u03b0\u03b5\5\u0090I\2\u03b1\u03b2\7_\2\2\u03b2"+
		"\u03b4\5\u0090I\2\u03b3\u03b1\3\2\2\2\u03b4\u03b7\3\2\2\2\u03b5\u03b3"+
		"\3\2\2\2\u03b5\u03b6\3\2\2\2\u03b6\u008f\3\2\2\2\u03b7\u03b5\3\2\2\2\u03b8"+
		"\u03ba\7`\2\2\u03b9\u03b8\3\2\2\2\u03b9\u03ba\3\2\2\2\u03ba\u03bb\3\2"+
		"\2\2\u03bb\u03bc\5\u0092J\2\u03bc\u0091\3\2\2\2\u03bd\u03c0\5\u0094K\2"+
		"\u03be\u03bf\t\5\2\2\u03bf\u03c1\5\u0094K\2\u03c0\u03be\3\2\2\2\u03c0"+
		"\u03c1\3\2\2\2\u03c1\u0093\3\2\2\2\u03c2\u03c7\5\u0096L\2\u03c3\u03c4"+
		"\7\61\2\2\u03c4\u03c6\5\u0096L\2\u03c5\u03c3\3\2\2\2\u03c6\u03c9\3\2\2"+
		"\2\u03c7\u03c5\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8\u0095\3\2\2\2\u03c9\u03c7"+
		"\3\2\2\2\u03ca\u03cd\5\u0098M\2\u03cb\u03cc\7a\2\2\u03cc\u03ce\5\u0098"+
		"M\2\u03cd\u03cb\3\2\2\2\u03cd\u03ce\3\2\2\2\u03ce\u0097\3\2\2\2\u03cf"+
		"\u03d4\5\u009aN\2\u03d0\u03d1\t\6\2\2\u03d1\u03d3\5\u009aN\2\u03d2\u03d0"+
		"\3\2\2\2\u03d3\u03d6\3\2\2\2\u03d4\u03d2\3\2\2\2\u03d4\u03d5\3\2\2\2\u03d5"+
		"\u0099\3\2\2\2\u03d6\u03d4\3\2\2\2\u03d7\u03dc\5\u009cO\2\u03d8\u03d9"+
		"\t\7\2\2\u03d9\u03db\5\u009cO\2\u03da\u03d8\3\2\2\2\u03db\u03de\3\2\2"+
		"\2\u03dc\u03da\3\2\2\2\u03dc\u03dd\3\2\2\2\u03dd\u009b\3\2\2\2\u03de\u03dc"+
		"\3\2\2\2\u03df\u03e3\5\u009eP\2\u03e0\u03e1\7b\2\2\u03e1\u03e2\7c\2\2"+
		"\u03e2\u03e4\5\u00e8u\2\u03e3\u03e0\3\2\2\2\u03e3\u03e4\3\2\2\2\u03e4"+
		"\u009d\3\2\2\2\u03e5\u03e9\5\u00a0Q\2\u03e6\u03e7\7e\2\2\u03e7\u03e8\7"+
		"d\2\2\u03e8\u03ea\5\u00e8u\2\u03e9\u03e6\3\2\2\2\u03e9\u03ea\3\2\2\2\u03ea"+
		"\u009f\3\2\2\2\u03eb\u03ef\5\u00a2R\2\u03ec\u03ed\7f\2\2\u03ed\u03ee\7"+
		"H\2\2\u03ee\u03f0\5\u00e8u\2\u03ef\u03ec\3\2\2\2\u03ef\u03f0\3\2\2\2\u03f0"+
		"\u00a1\3\2\2\2\u03f1\u03f5\5\u00a4S\2\u03f2\u03f3\7h\2\2\u03f3\u03f4\7"+
		"H\2\2\u03f4\u03f6\5\u00f4{\2\u03f5\u03f2\3\2\2\2\u03f5\u03f6\3\2\2\2\u03f6"+
		"\u00a3\3\2\2\2\u03f7\u03fb\5\u00a6T\2\u03f8\u03f9\7g\2\2\u03f9\u03fa\7"+
		"H\2\2\u03fa\u03fc\5\u00f4{\2\u03fb\u03f8\3\2\2\2\u03fb\u03fc\3\2\2\2\u03fc"+
		"\u00a5\3\2\2\2\u03fd\u0406\5\u00aaV\2\u03fe\u03ff\7\6\2\2\u03ff\u0400"+
		"\7/\2\2\u0400\u0401\3\2\2\2\u0401\u0402\5\u00a8U\2\u0402\u0403\5\u00ce"+
		"h\2\u0403\u0405\3\2\2\2\u0404\u03fe\3\2\2\2\u0405\u0408\3\2\2\2\u0406"+
		"\u0404\3\2\2\2\u0406\u0407\3\2\2\2\u0407\u00a7\3\2\2\2\u0408\u0406\3\2"+
		"\2\2\u0409\u040d\5J&\2\u040a\u040d\5\u00c2b\2\u040b\u040d\5\u00c4c\2\u040c"+
		"\u0409\3\2\2\2\u040c\u040a\3\2\2\2\u040c\u040b\3\2\2\2\u040d\u00a9\3\2"+
		"\2\2\u040e\u0410\t\6\2\2\u040f\u040e\3\2\2\2\u0410\u0413\3\2\2\2\u0411"+
		"\u040f\3\2\2\2\u0411\u0412\3\2\2\2\u0412\u0414\3\2\2\2\u0413\u0411\3\2"+
		"\2\2\u0414\u0415\5\u00acW\2\u0415\u00ab\3\2\2\2\u0416\u041a\5\u00b2Z\2"+
		"\u0417\u041a\5\u00aeX\2\u0418\u041a\5\u00b0Y\2\u0419\u0416\3\2\2\2\u0419"+
		"\u0417\3\2\2\2\u0419\u0418\3\2\2\2\u041a\u00ad\3\2\2\2\u041b\u041c\7o"+
		"\2\2\u041c\u041d\7n\2\2\u041d\u041e\5\u00e8u\2\u041e\u041f\7\t\2\2\u041f"+
		"\u0420\5^\60\2\u0420\u0421\7\n\2\2\u0421\u00af\3\2\2\2\u0422\u0423\7p"+
		"\2\2\u0423\u0424\7n\2\2\u0424\u0425\5\u00e8u\2\u0425\u0426\7\t\2\2\u0426"+
		"\u0427\5^\60\2\u0427\u0428\7\n\2\2\u0428\u00b1\3\2\2\2\u0429\u042e\5\u00b4"+
		"[\2\u042a\u042b\7\67\2\2\u042b\u042d\5\u00b4[\2\u042c\u042a\3\2\2\2\u042d"+
		"\u0430\3\2\2\2\u042e\u042c\3\2\2\2\u042e\u042f\3\2\2\2\u042f\u00b3\3\2"+
		"\2\2\u0430\u042e\3\2\2\2\u0431\u0439\5\u00be`\2\u0432\u0438\5\u00b6\\"+
		"\2\u0433\u0438\5\u00ba^\2\u0434\u0438\5\u00bc_\2\u0435\u0438\5\u00b8]"+
		"\2\u0436\u0438\5\u00ceh\2\u0437\u0432\3\2\2\2\u0437\u0433\3\2\2\2\u0437"+
		"\u0434\3\2\2\2\u0437\u0435\3\2\2\2\u0437\u0436\3\2\2\2\u0438\u043b\3\2"+
		"\2\2\u0439\u0437\3\2\2\2\u0439\u043a\3\2\2\2\u043a\u00b5\3\2\2\2\u043b"+
		"\u0439\3\2\2\2\u043c\u043d\78\2\2\u043d\u043e\78\2\2\u043e\u043f\5^\60"+
		"\2\u043f\u0440\79\2\2\u0440\u0441\79\2\2\u0441\u00b7\3\2\2\2\u0442\u0443"+
		"\78\2\2\u0443\u0444\79\2\2\u0444\u00b9\3\2\2\2\u0445\u0446\78\2\2\u0446"+
		"\u0447\5^\60\2\u0447\u0448\79\2\2\u0448\u00bb\3\2\2\2\u0449\u0450\7:\2"+
		"\2\u044a\u0451\5\u00fe\u0080\2\u044b\u0451\5\u00fc\177\2\u044c\u0451\7"+
		"\u0090\2\2\u044d\u0451\5\u00c4c\2\u044e\u0451\5\u00c2b\2\u044f\u0451\5"+
		"\u00c6d\2\u0450\u044a\3\2\2\2\u0450\u044b\3\2\2\2\u0450\u044c\3\2\2\2"+
		"\u0450\u044d\3\2\2\2\u0450\u044e\3\2\2\2\u0450\u044f\3\2\2\2\u0451\u00bd"+
		"\3\2\2\2\u0452\u0462\7\u0089\2\2\u0453\u0462\7l\2\2\u0454\u0462\7m\2\2"+
		"\u0455\u0462\7\u008a\2\2\u0456\u0462\5\u00fc\177\2\u0457\u0462\5\u00c2"+
		"b\2\u0458\u0462\5\u00c4c\2\u0459\u0462\5\u00c6d\2\u045a\u0462\5\u00ea"+
		"v\2\u045b\u0462\5\u00ccg\2\u045c\u0462\5\u00c8e\2\u045d\u0462\5\u00ca"+
		"f\2\u045e\u0462\5\u00f8}\2\u045f\u0462\5\u00d2j\2\u0460\u0462\5\u00c0"+
		"a\2\u0461\u0452\3\2\2\2\u0461\u0453\3\2\2\2\u0461\u0454\3\2\2\2\u0461"+
		"\u0455\3\2\2\2\u0461\u0456\3\2\2\2\u0461\u0457\3\2\2\2\u0461\u0458\3\2"+
		"\2\2\u0461\u0459\3\2\2\2\u0461\u045a\3\2\2\2\u0461\u045b\3\2\2\2\u0461"+
		"\u045c\3\2\2\2\u0461\u045d\3\2\2\2\u0461\u045e\3\2\2\2\u0461\u045f\3\2"+
		"\2\2\u0461\u0460\3\2\2\2\u0462\u00bf\3\2\2\2\u0463\u0464\7\t\2\2\u0464"+
		"\u0465\5\20\t\2\u0465\u0466\7\n\2\2\u0466\u00c1\3\2\2\2\u0467\u0468\7"+
		"\7\2\2\u0468\u0469\5J&\2\u0469\u00c3\3\2\2\2\u046a\u046c\7\13\2\2\u046b"+
		"\u046d\5^\60\2\u046c\u046b\3\2\2\2\u046c\u046d\3\2\2\2\u046d\u046e\3\2"+
		"\2\2\u046e\u046f\7\f\2\2\u046f\u00c5\3\2\2\2\u0470\u0471\7;\2\2\u0471"+
		"\u00c7\3\2\2\2\u0472\u0473\7\22\2\2\u0473\u0474\7\t\2\2\u0474\u0475\5"+
		"^\60\2\u0475\u0476\7\n\2\2\u0476\u00c9\3\2\2\2\u0477\u0478\7k\2\2\u0478"+
		"\u0479\7\t\2\2\u0479\u047a\5^\60\2\u047a\u047b\7\n\2\2\u047b\u00cb\3\2"+
		"\2\2\u047c\u047d\5J&\2\u047d\u047e\5\u00ceh\2\u047e\u00cd\3\2\2\2\u047f"+
		"\u0486\7\13\2\2\u0480\u0482\5\u00d0i\2\u0481\u0483\7\20\2\2\u0482\u0481"+
		"\3\2\2\2\u0482\u0483\3\2\2\2\u0483\u0485\3\2\2\2\u0484\u0480\3\2\2\2\u0485"+
		"\u0488\3\2\2\2\u0486\u0484\3\2\2\2\u0486\u0487\3\2\2\2\u0487\u0489\3\2"+
		"\2\2\u0488\u0486\3\2\2\2\u0489\u048a\7\f\2\2\u048a\u00cf\3\2\2\2\u048b"+
		"\u048e\5`\61\2\u048c\u048e\7\u0088\2\2\u048d\u048b\3\2\2\2\u048d\u048c"+
		"\3\2\2\2\u048e\u00d1\3\2\2\2\u048f\u0492\5\u00d4k\2\u0490\u0492\5\u00d6"+
		"l\2\u0491\u048f\3\2\2\2\u0491\u0490\3\2\2\2\u0492\u00d3\3\2\2\2\u0493"+
		"\u0494\5J&\2\u0494\u0495\7<\2\2\u0495\u0496\7\u008a\2\2\u0496\u00d5\3"+
		"\2\2\2\u0497\u0498\5\64\33\2\u0498\u0499\7!\2\2\u0499\u049b\7\13\2\2\u049a"+
		"\u049c\5Z.\2\u049b\u049a\3\2\2\2\u049b\u049c\3\2\2\2\u049c\u049d\3\2\2"+
		"\2\u049d\u04a0\7\f\2\2\u049e\u049f\7H\2\2\u049f\u04a1\5\u00e8u\2\u04a0"+
		"\u049e\3\2\2\2\u04a0\u04a1\3\2\2\2\u04a1\u04a2\3\2\2\2\u04a2\u04a3\7\t"+
		"\2\2\u04a3\u04a4\5\22\n\2\u04a4\u04a5\7\n\2\2\u04a5\u00d7\3\2\2\2\u04a6"+
		"\u04a7\7u\2\2\u04a7\u04a8\7~\2\2\u04a8\u04a9\5`\61\2\u04a9\u04aa\7|\2"+
		"\2\u04aa\u04ae\5`\61\2\u04ab\u04ac\7I\2\2\u04ac\u04ad\7\u0080\2\2\u04ad"+
		"\u04af\5`\61\2\u04ae\u04ab\3\2\2\2\u04ae\u04af\3\2\2\2\u04af\u04be\3\2"+
		"\2\2\u04b0\u04b1\7u\2\2\u04b1\u04b2\7~\2\2\u04b2\u04b7\5\u00f6|\2\u04b3"+
		"\u04b4\7\20\2\2\u04b4\u04b6\5\u00f6|\2\u04b5\u04b3\3\2\2\2\u04b6\u04b9"+
		"\3\2\2\2\u04b7\u04b5\3\2\2\2\u04b7\u04b8\3\2\2\2\u04b8\u04ba\3\2\2\2\u04b9"+
		"\u04b7\3\2\2\2\u04ba\u04bb\7|\2\2\u04bb\u04bc\5`\61\2\u04bc\u04be\3\2"+
		"\2\2\u04bd\u04a6\3\2\2\2\u04bd\u04b0\3\2\2\2\u04be\u00d9\3\2\2\2\u04bf"+
		"\u04c0\7v\2\2\u04c0\u04c1\7~\2\2\u04c1\u04c2\5\u00e4s\2\u04c2\u00db\3"+
		"\2\2\2\u04c3\u04c4\7w\2\2\u04c4\u04c5\7~\2\2\u04c5\u04c6\5\u00e4s\2\u04c6"+
		"\u04c7\7H\2\2\u04c7\u04c8\5`\61\2\u04c8\u00dd\3\2\2\2\u04c9\u04ca\7x\2"+
		"\2\u04ca\u04cb\7}\2\2\u04cb\u04cc\7c\2\2\u04cc\u04cd\7~\2\2\u04cd\u04ce"+
		"\5\u00e4s\2\u04ce\u04cf\7\177\2\2\u04cf\u04d0\5`\61\2\u04d0\u00df\3\2"+
		"\2\2\u04d1\u04d2\7y\2\2\u04d2\u04d7\5\u00e6t\2\u04d3\u04d4\7\20\2\2\u04d4"+
		"\u04d6\5\u00e6t\2\u04d5\u04d3\3\2\2\2\u04d6\u04d9\3\2\2\2\u04d7\u04d5"+
		"\3\2\2\2\u04d7\u04d8\3\2\2\2\u04d8\u04da\3\2\2\2\u04d9\u04d7\3\2\2\2\u04da"+
		"\u04db\7z\2\2\u04db\u04dc\5`\61\2\u04dc\u04dd\7E\2\2\u04dd\u04de\5`\61"+
		"\2\u04de\u00e1\3\2\2\2\u04df\u04e0\7{\2\2\u04e0\u04e1\7~\2\2\u04e1\u04e2"+
		"\5`\61\2\u04e2\u04e3\7|\2\2\u04e3\u04e4\5`\61\2\u04e4\u00e3\3\2\2\2\u04e5"+
		"\u04e8\5\u00be`\2\u04e6\u04e9\5\u00b6\\\2\u04e7\u04e9\5\u00bc_\2\u04e8"+
		"\u04e6\3\2\2\2\u04e8\u04e7\3\2\2\2\u04e9\u04ea\3\2\2\2\u04ea\u04e8\3\2"+
		"\2\2\u04ea\u04eb\3\2\2\2\u04eb\u00e5\3\2\2\2\u04ec\u04ed\5\u00c2b\2\u04ed"+
		"\u04ee\7\b\2\2\u04ee\u04ef\5`\61\2\u04ef\u00e7\3\2\2\2\u04f0\u04f1\7\13"+
		"\2\2\u04f1\u04f9\7\f\2\2\u04f2\u04f6\5\u00ecw\2\u04f3\u04f7\7\u0088\2"+
		"\2\u04f4\u04f7\7\r\2\2\u04f5\u04f7\7\62\2\2\u04f6\u04f3\3\2\2\2\u04f6"+
		"\u04f4\3\2\2\2\u04f6\u04f5\3\2\2\2\u04f6\u04f7\3\2\2\2\u04f7\u04f9\3\2"+
		"\2\2\u04f8\u04f0\3\2\2\2\u04f8\u04f2\3\2\2\2\u04f9\u00e9\3\2\2\2\u04fa"+
		"\u0503\7\t\2\2\u04fb\u0500\5\u00f6|\2\u04fc\u04fd\7\20\2\2\u04fd\u04ff"+
		"\5\u00f6|\2\u04fe\u04fc\3\2\2\2\u04ff\u0502\3\2\2\2\u0500\u04fe\3\2\2"+
		"\2\u0500\u0501\3\2\2\2\u0501\u0504\3\2\2\2\u0502\u0500\3\2\2\2\u0503\u04fb"+
		"\3\2\2\2\u0503\u0504\3\2\2\2\u0504\u0505\3\2\2\2\u0505\u050b\7\n\2\2\u0506"+
		"\u0507\7=\2\2\u0507\u0508\5^\60\2\u0508\u0509\7>\2\2\u0509\u050b\3\2\2"+
		"\2\u050a\u04fa\3\2\2\2\u050a\u0506\3\2\2\2\u050b\u00eb\3\2\2\2\u050c\u0510"+
		"\5J&\2\u050d\u0510\7\u0089\2\2\u050e\u0510\5\u00eex\2\u050f\u050c\3\2"+
		"\2\2\u050f\u050d\3\2\2\2\u050f\u050e\3\2\2\2\u0510\u00ed\3\2\2\2\u0511"+
		"\u0514\5\u00f0y\2\u0512\u0514\5\u00f2z\2\u0513\u0511\3\2\2\2\u0513\u0512"+
		"\3\2\2\2\u0514\u00ef\3\2\2\2\u0515\u0516\7!\2\2\u0516\u0517\7\13\2\2\u0517"+
		"\u0518\7\r\2\2\u0518\u0519\7\f\2\2\u0519\u00f1\3\2\2\2\u051a\u051b\7!"+
		"\2\2\u051b\u0524\7\13\2\2\u051c\u0521\5\u00e8u\2\u051d\u051e\7\20\2\2"+
		"\u051e\u0520\5\u00e8u\2\u051f\u051d\3\2\2\2\u0520\u0523\3\2\2\2\u0521"+
		"\u051f\3\2\2\2\u0521\u0522\3\2\2\2\u0522\u0525\3\2\2\2\u0523\u0521\3\2"+
		"\2\2\u0524\u051c\3\2\2\2\u0524\u0525\3\2\2\2\u0525\u0526\3\2\2\2\u0526"+
		"\u0527\7\f\2\2\u0527\u0528\7H\2\2\u0528\u0529\5\u00e8u\2\u0529\u00f3\3"+
		"\2\2\2\u052a\u052c\5\u00ecw\2\u052b\u052d\7\u0088\2\2\u052c\u052b\3\2"+
		"\2\2\u052c\u052d\3\2\2\2\u052d\u00f5\3\2\2\2\u052e\u0531\5`\61\2\u052f"+
		"\u0531\7\u0090\2\2\u0530\u052e\3\2\2\2\u0530\u052f\3\2\2\2\u0531\u0532"+
		"\3\2\2\2\u0532\u0533\t\b\2\2\u0533\u0534\5`\61\2\u0534\u00f7\3\2\2\2\u0535"+
		"\u0537\78\2\2\u0536\u0538\5^\60\2\u0537\u0536\3\2\2\2\u0537\u0538\3\2"+
		"\2\2\u0538\u0539\3\2\2\2\u0539\u053a\79\2\2\u053a\u00f9\3\2\2\2\u053b"+
		"\u053c\5\u00fc\177\2\u053c\u00fb\3\2\2\2\u053d\u053e\7\u0087\2\2\u053e"+
		"\u00fd\3\2\2\2\u053f\u0540\t\t\2\2\u0540\u00ff\3\2\2\2\u0086\u0108\u010c"+
		"\u011c\u0122\u012a\u0132\u013a\u0149\u0167\u016f\u0171\u0187\u0191\u019b"+
		"\u01a0\u01a5\u01a9\u01b5\u01b9\u01c2\u01c9\u01d7\u01db\u01e0\u01ea\u01f2"+
		"\u01f6\u0202\u020e\u0224\u022c\u0231\u0234\u0238\u0241\u024a\u024d\u0255"+
		"\u025c\u025e\u0265\u026c\u026e\u0276\u027b\u0282\u0289\u0293\u029a\u02a1"+
		"\u02a8\u02b1\u02bb\u02bf\u02c7\u02c9\u02d5\u02db\u02df\u02e3\u02ee\u02f4"+
		"\u0303\u0309\u030d\u0311\u0318\u031f\u0325\u032a\u032c\u0330\u0337\u033e"+
		"\u0347\u0353\u035d\u0369\u036d\u0376\u037d\u0393\u0398\u039d\u03a1\u03ad"+
		"\u03b5\u03b9\u03c0\u03c7\u03cd\u03d4\u03dc\u03e3\u03e9\u03ef\u03f5\u03fb"+
		"\u0406\u040c\u0411\u0419\u042e\u0437\u0439\u0450\u0461\u046c\u0482\u0486"+
		"\u048d\u0491\u049b\u04a0\u04ae\u04b7\u04bd\u04d7\u04e8\u04ea\u04f6\u04f8"+
		"\u0500\u0503\u050a\u050f\u0513\u0521\u0524\u052c\u0530\u0537";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}