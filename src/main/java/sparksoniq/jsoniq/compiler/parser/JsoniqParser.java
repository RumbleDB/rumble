// Generated from ./src/main/java/sparksoniq/jsoniq/compiler/parser/Jsoniq.g4 by ANTLR 4.7

// Java header
package sparksoniq.jsoniq.compiler.parser;

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
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

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
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66, 
		T__66=67, T__67=68, T__68=69, T__69=70, T__70=71, T__71=72, T__72=73, 
		T__73=74, T__74=75, Kfor=76, Klet=77, Kwhere=78, Kgroup=79, Kby=80, Korder=81, 
		Kreturn=82, Kif=83, Kin=84, Kas=85, Kat=86, Kallowing=87, Kempty=88, Kcount=89, 
		Kstable=90, Kascending=91, Kdescending=92, Ksome=93, Kevery=94, Ksatisfies=95, 
		Kcollation=96, Kgreatest=97, Kleast=98, Kswitch=99, Kcase=100, Ktry=101, 
		Kcatch=102, Kdefault=103, Kthen=104, Kelse=105, Ktypeswitch=106, Kor=107, 
		Kand=108, Knot=109, Kto=110, Kinstance=111, Kof=112, Ktreat=113, Kcast=114, 
		Kcastable=115, Kversion=116, Kjsoniq=117, Kjson=118, STRING=119, NullLiteral=120, 
		Literal=121, NumericLiteral=122, BooleanLiteral=123, IntegerLiteral=124, 
		DecimalLiteral=125, DoubleLiteral=126, WS=127, NCName=128, XQComment=129, 
		ContentChar=130;
	public static final int
		RULE_module = 0, RULE_mainModule = 1, RULE_libraryModule = 2, RULE_prolog = 3, 
		RULE_defaultCollationDecl = 4, RULE_orderingModeDecl = 5, RULE_emptyOrderDecl = 6, 
		RULE_decimalFormatDecl = 7, RULE_dfPropertyName = 8, RULE_moduleImport = 9, 
		RULE_varDecl = 10, RULE_functionDecl = 11, RULE_paramList = 12, RULE_param = 13, 
		RULE_expr = 14, RULE_exprSingle = 15, RULE_flowrExpr = 16, RULE_forClause = 17, 
		RULE_forVar = 18, RULE_letClause = 19, RULE_letVar = 20, RULE_whereClause = 21, 
		RULE_groupByClause = 22, RULE_groupByVar = 23, RULE_orderByClause = 24, 
		RULE_orderByExpr = 25, RULE_countClause = 26, RULE_quantifiedExpr = 27, 
		RULE_quantifiedExprVar = 28, RULE_switchExpr = 29, RULE_switchCaseClause = 30, 
		RULE_typeSwitchExpr = 31, RULE_caseClause = 32, RULE_ifExpr = 33, RULE_tryCatchExpr = 34, 
		RULE_orExpr = 35, RULE_andExpr = 36, RULE_notExpr = 37, RULE_comparisonExpr = 38, 
		RULE_stringConcatExpr = 39, RULE_rangeExpr = 40, RULE_additiveExpr = 41, 
		RULE_multiplicativeExpr = 42, RULE_instanceOfExpr = 43, RULE_treatExpr = 44, 
		RULE_castableExpr = 45, RULE_castExpr = 46, RULE_unaryExpr = 47, RULE_simpleMapExpr = 48, 
		RULE_postFixExpr = 49, RULE_arrayLookup = 50, RULE_arrayUnboxing = 51, 
		RULE_predicate = 52, RULE_objectLookup = 53, RULE_primaryExpr = 54, RULE_varRef = 55, 
		RULE_parenthesizedExpr = 56, RULE_contextItemExpr = 57, RULE_orderedExpr = 58, 
		RULE_unorderedExpr = 59, RULE_functionCall = 60, RULE_argumentList = 61, 
		RULE_argument = 62, RULE_functionItemExpr = 63, RULE_namedFunctionRef = 64, 
		RULE_inlineFunctionExpr = 65, RULE_sequenceType = 66, RULE_objectConstructor = 67, 
		RULE_itemType = 68, RULE_jSONItemTest = 69, RULE_keyWordBoolean = 70, 
		RULE_keyWordDuration = 71, RULE_keyWordYearMonthDuration = 72, RULE_keyWordDayTimeDuration = 73, 
		RULE_keyWordHexBinary = 74, RULE_keyWordBase64Binary = 75, RULE_keyWordDateTime = 76, 
		RULE_keyWordDate = 77, RULE_typesKeywords = 78, RULE_singleType = 79, 
		RULE_atomicType = 80, RULE_nCNameOrKeyWord = 81, RULE_pairConstructor = 82, 
		RULE_arrayConstructor = 83, RULE_uriLiteral = 84, RULE_stringLiteral = 85, 
		RULE_keyWords = 86;
	public static final String[] ruleNames = {
		"module", "mainModule", "libraryModule", "prolog", "defaultCollationDecl", 
		"orderingModeDecl", "emptyOrderDecl", "decimalFormatDecl", "dfPropertyName", 
		"moduleImport", "varDecl", "functionDecl", "paramList", "param", "expr", 
		"exprSingle", "flowrExpr", "forClause", "forVar", "letClause", "letVar", 
		"whereClause", "groupByClause", "groupByVar", "orderByClause", "orderByExpr", 
		"countClause", "quantifiedExpr", "quantifiedExprVar", "switchExpr", "switchCaseClause", 
		"typeSwitchExpr", "caseClause", "ifExpr", "tryCatchExpr", "orExpr", "andExpr", 
		"notExpr", "comparisonExpr", "stringConcatExpr", "rangeExpr", "additiveExpr", 
		"multiplicativeExpr", "instanceOfExpr", "treatExpr", "castableExpr", "castExpr", 
		"unaryExpr", "simpleMapExpr", "postFixExpr", "arrayLookup", "arrayUnboxing", 
		"predicate", "objectLookup", "primaryExpr", "varRef", "parenthesizedExpr", 
		"contextItemExpr", "orderedExpr", "unorderedExpr", "functionCall", "argumentList", 
		"argument", "functionItemExpr", "namedFunctionRef", "inlineFunctionExpr", 
		"sequenceType", "objectConstructor", "itemType", "jSONItemTest", "keyWordBoolean", 
		"keyWordDuration", "keyWordYearMonthDuration", "keyWordDayTimeDuration", 
		"keyWordHexBinary", "keyWordBase64Binary", "keyWordDateTime", "keyWordDate", 
		"typesKeywords", "singleType", "atomicType", "nCNameOrKeyWord", "pairConstructor", 
		"arrayConstructor", "uriLiteral", "stringLiteral", "keyWords"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'module'", "'namespace'", "'='", "'declare'", "'ordering'", 
		"'ordered'", "'unordered'", "'decimal-format'", "':'", "'decimal-separator'", 
		"'grouping-separator'", "'infinity'", "'minus-sign'", "'NaN'", "'percent'", 
		"'per-mille'", "'zero-digit'", "'digit'", "'pattern-separator'", "'import'", 
		"','", "'variable'", "':='", "'external'", "'function'", "'('", "')'", 
		"'{'", "'}'", "'$'", "'|'", "'*'", "'eq'", "'ne'", "'lt'", "'le'", "'gt'", 
		"'ge'", "'!='", "'<'", "'<='", "'>'", "'>='", "'||'", "'+'", "'-'", "'div'", 
		"'idiv'", "'mod'", "'!'", "'['", "']'", "'.'", "'$$'", "'?'", "'#'", "'{|'", 
		"'|}'", "'item'", "'object'", "'array'", "'boolean'", "'duration'", "'yearMonthDuration'", 
		"'dayTimeDuration'", "'hexBinary'", "'base64Binary'", "'dateTime'", "'date'", 
		"'atomic'", "'string'", "'integer'", "'decimal'", "'double'", "'for'", 
		"'let'", "'where'", "'group'", "'by'", "'order'", "'return'", "'if'", 
		"'in'", "'as'", "'at'", "'allowing'", "'empty'", "'count'", "'stable'", 
		"'ascending'", "'descending'", "'some'", "'every'", "'satisfies'", "'collation'", 
		"'greatest'", "'least'", "'switch'", "'case'", "'try'", "'catch'", "'default'", 
		"'then'", "'else'", "'typeswitch'", "'or'", "'and'", "'not'", "'to'", 
		"'instance'", "'of'", "'treat'", "'cast'", "'castable'", "'version'", 
		"'jsoniq'", "'json-item'", null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, "Kfor", "Klet", "Kwhere", "Kgroup", "Kby", "Korder", 
		"Kreturn", "Kif", "Kin", "Kas", "Kat", "Kallowing", "Kempty", "Kcount", 
		"Kstable", "Kascending", "Kdescending", "Ksome", "Kevery", "Ksatisfies", 
		"Kcollation", "Kgreatest", "Kleast", "Kswitch", "Kcase", "Ktry", "Kcatch", 
		"Kdefault", "Kthen", "Kelse", "Ktypeswitch", "Kor", "Kand", "Knot", "Kto", 
		"Kinstance", "Kof", "Ktreat", "Kcast", "Kcastable", "Kversion", "Kjsoniq", 
		"Kjson", "STRING", "NullLiteral", "Literal", "NumericLiteral", "BooleanLiteral", 
		"IntegerLiteral", "DecimalLiteral", "DoubleLiteral", "WS", "NCName", "XQComment", 
		"ContentChar"
	};
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
		enterRule(_localctx, 0, RULE_module);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(174);
				match(Kjsoniq);
				setState(175);
				match(Kversion);
				setState(176);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(177);
				match(T__0);
				}
				break;
			}
			setState(183);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(181);
				libraryModule();
				}
				break;
			case T__4:
			case T__6:
			case T__7:
			case T__9:
			case T__20:
			case T__25:
			case T__26:
			case T__28:
			case T__30:
			case T__45:
			case T__46:
			case T__51:
			case T__54:
			case T__57:
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
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
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kjson:
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				{
				setState(182);
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
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
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
		enterRule(_localctx, 2, RULE_mainModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			prolog();
			setState(186);
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
		enterRule(_localctx, 4, RULE_libraryModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			match(T__1);
			setState(189);
			match(T__2);
			setState(190);
			match(NCName);
			setState(191);
			match(T__3);
			setState(192);
			uriLiteral();
			setState(193);
			match(T__0);
			setState(194);
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
		public List<DefaultCollationDeclContext> defaultCollationDecl() {
			return getRuleContexts(DefaultCollationDeclContext.class);
		}
		public DefaultCollationDeclContext defaultCollationDecl(int i) {
			return getRuleContext(DefaultCollationDeclContext.class,i);
		}
		public List<OrderingModeDeclContext> orderingModeDecl() {
			return getRuleContexts(OrderingModeDeclContext.class);
		}
		public OrderingModeDeclContext orderingModeDecl(int i) {
			return getRuleContext(OrderingModeDeclContext.class,i);
		}
		public List<EmptyOrderDeclContext> emptyOrderDecl() {
			return getRuleContexts(EmptyOrderDeclContext.class);
		}
		public EmptyOrderDeclContext emptyOrderDecl(int i) {
			return getRuleContext(EmptyOrderDeclContext.class,i);
		}
		public List<DecimalFormatDeclContext> decimalFormatDecl() {
			return getRuleContexts(DecimalFormatDeclContext.class);
		}
		public DecimalFormatDeclContext decimalFormatDecl(int i) {
			return getRuleContext(DecimalFormatDeclContext.class,i);
		}
		public List<ModuleImportContext> moduleImport() {
			return getRuleContexts(ModuleImportContext.class);
		}
		public ModuleImportContext moduleImport(int i) {
			return getRuleContext(ModuleImportContext.class,i);
		}
		public List<FunctionDeclContext> functionDecl() {
			return getRuleContexts(FunctionDeclContext.class);
		}
		public FunctionDeclContext functionDecl(int i) {
			return getRuleContext(FunctionDeclContext.class,i);
		}
		public List<VarDeclContext> varDecl() {
			return getRuleContexts(VarDeclContext.class);
		}
		public VarDeclContext varDecl(int i) {
			return getRuleContext(VarDeclContext.class,i);
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
		enterRule(_localctx, 6, RULE_prolog);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(201);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(196);
						defaultCollationDecl();
						}
						break;
					case 2:
						{
						setState(197);
						orderingModeDecl();
						}
						break;
					case 3:
						{
						setState(198);
						emptyOrderDecl();
						}
						break;
					case 4:
						{
						setState(199);
						decimalFormatDecl();
						}
						break;
					case 5:
						{
						setState(200);
						moduleImport();
						}
						break;
					}
					setState(203);
					match(T__0);
					}
					} 
				}
				setState(209);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(212);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(210);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(211);
					varDecl();
					}
					break;
				}
				setState(214);
				match(T__0);
				}
				}
				setState(220);
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

	public static class DefaultCollationDeclContext extends ParserRuleContext {
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
		enterRule(_localctx, 8, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(T__4);
			setState(222);
			match(Kdefault);
			setState(223);
			match(Kcollation);
			setState(224);
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
		enterRule(_localctx, 10, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			match(T__4);
			setState(227);
			match(T__5);
			setState(228);
			_la = _input.LA(1);
			if ( !(_la==T__6 || _la==T__7) ) {
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
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
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
		enterRule(_localctx, 12, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(T__4);
			setState(231);
			match(Kdefault);
			setState(232);
			match(Korder);
			setState(233);
			match(Kempty);
			setState(234);
			_la = _input.LA(1);
			if ( !(_la==Kgreatest || _la==Kleast) ) {
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
		public List<TerminalNode> NCName() { return getTokens(JsoniqParser.NCName); }
		public TerminalNode NCName(int i) {
			return getToken(JsoniqParser.NCName, i);
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
		enterRule(_localctx, 14, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(T__4);
			setState(245);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				{
				setState(237);
				match(T__8);
				setState(240);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(238);
					match(NCName);
					setState(239);
					match(T__9);
					}
					break;
				}
				setState(242);
				match(NCName);
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(243);
				match(Kdefault);
				setState(244);
				match(T__8);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) {
				{
				{
				setState(247);
				dfPropertyName();
				setState(248);
				match(T__3);
				setState(249);
				stringLiteral();
				}
				}
				setState(255);
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
		enterRule(_localctx, 16, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) ) {
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
		public List<UriLiteralContext> uriLiteral() {
			return getRuleContexts(UriLiteralContext.class);
		}
		public UriLiteralContext uriLiteral(int i) {
			return getRuleContext(UriLiteralContext.class,i);
		}
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public TerminalNode Kat() { return getToken(JsoniqParser.Kat, 0); }
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
		enterRule(_localctx, 18, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
			match(T__20);
			setState(259);
			match(T__1);
			setState(263);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(260);
				match(T__2);
				setState(261);
				match(NCName);
				setState(262);
				match(T__3);
				}
			}

			setState(265);
			uriLiteral();
			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(266);
				match(Kat);
				setState(267);
				uriLiteral();
				setState(272);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__21) {
					{
					{
					setState(268);
					match(T__21);
					setState(269);
					uriLiteral();
					}
					}
					setState(274);
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
		enterRule(_localctx, 20, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			match(T__4);
			setState(278);
			match(T__22);
			setState(279);
			varRef();
			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(280);
				match(Kas);
				setState(281);
				sequenceType();
				}
			}

			setState(291);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				{
				{
				setState(284);
				match(T__23);
				setState(285);
				exprSingle();
				}
				}
				break;
			case T__24:
				{
				{
				setState(286);
				match(T__24);
				setState(289);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__23) {
					{
					setState(287);
					match(T__23);
					setState(288);
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
		public Token namespace;
		public Token fn_name;
		public SequenceTypeContext return_type;
		public ExprContext fn_body;
		public List<TerminalNode> NCName() { return getTokens(JsoniqParser.NCName); }
		public TerminalNode NCName(int i) {
			return getToken(JsoniqParser.NCName, i);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
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
		enterRule(_localctx, 22, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(293);
			match(T__4);
			setState(294);
			match(T__25);
			setState(297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(295);
				((FunctionDeclContext)_localctx).namespace = match(NCName);
				setState(296);
				match(T__9);
				}
				break;
			}
			setState(299);
			((FunctionDeclContext)_localctx).fn_name = match(NCName);
			setState(300);
			match(T__26);
			setState(302);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(301);
				paramList();
				}
			}

			setState(304);
			match(T__27);
			setState(307);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(305);
				match(Kas);
				setState(306);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(314);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				{
				setState(309);
				match(T__28);
				setState(310);
				((FunctionDeclContext)_localctx).fn_body = expr();
				setState(311);
				match(T__29);
				}
				break;
			case T__24:
				{
				setState(313);
				match(T__24);
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
		enterRule(_localctx, 24, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			param();
			setState(321);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(317);
				match(T__21);
				setState(318);
				param();
				}
				}
				setState(323);
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
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
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
		enterRule(_localctx, 26, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			match(T__30);
			setState(325);
			match(NCName);
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(326);
				match(Kas);
				setState(327);
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
		enterRule(_localctx, 28, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			exprSingle();
			setState(335);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(331);
				match(T__21);
				setState(332);
				exprSingle();
				}
				}
				setState(337);
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
		public FlowrExprContext flowrExpr() {
			return getRuleContext(FlowrExprContext.class,0);
		}
		public QuantifiedExprContext quantifiedExpr() {
			return getRuleContext(QuantifiedExprContext.class,0);
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
		public OrExprContext orExpr() {
			return getRuleContext(OrExprContext.class,0);
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
		enterRule(_localctx, 30, RULE_exprSingle);
		try {
			setState(345);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(338);
				flowrExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(339);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(340);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(341);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(342);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(343);
				tryCatchExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(344);
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
		enterRule(_localctx, 32, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(349);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(347);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(348);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(359);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (Kfor - 76)) | (1L << (Klet - 76)) | (1L << (Kwhere - 76)) | (1L << (Kgroup - 76)) | (1L << (Korder - 76)) | (1L << (Kcount - 76)) | (1L << (Kstable - 76)))) != 0)) {
				{
				setState(357);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(351);
					forClause();
					}
					break;
				case Kwhere:
					{
					setState(352);
					whereClause();
					}
					break;
				case Klet:
					{
					setState(353);
					letClause();
					}
					break;
				case Kgroup:
					{
					setState(354);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(355);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(356);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(361);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(362);
			match(Kreturn);
			setState(363);
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
		enterRule(_localctx, 34, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(365);
			match(Kfor);
			setState(366);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(371);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(367);
				match(T__21);
				setState(368);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(373);
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
		enterRule(_localctx, 36, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(375);
				match(Kas);
				setState(376);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(381);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(379);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(380);
				match(Kempty);
				}
			}

			setState(385);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(383);
				match(Kat);
				setState(384);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(387);
			match(Kin);
			setState(388);
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
		enterRule(_localctx, 38, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			match(Klet);
			setState(391);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(396);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(392);
				match(T__21);
				setState(393);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(398);
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
		enterRule(_localctx, 40, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(402);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(400);
				match(Kas);
				setState(401);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(404);
			match(T__23);
			setState(405);
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
		enterRule(_localctx, 42, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			match(Kwhere);
			setState(408);
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
		enterRule(_localctx, 44, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(410);
			match(Kgroup);
			setState(411);
			match(Kby);
			setState(412);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(417);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(413);
				match(T__21);
				setState(414);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(419);
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
		enterRule(_localctx, 46, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23 || _la==Kas) {
				{
				setState(423);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(421);
					match(Kas);
					setState(422);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(425);
				((GroupByVarContext)_localctx).decl = match(T__23);
				setState(426);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(431);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(429);
				match(Kcollation);
				setState(430);
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
		enterRule(_localctx, 48, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(433);
				match(Korder);
				setState(434);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(435);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(436);
				match(Korder);
				setState(437);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(440);
			orderByExpr();
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(441);
				match(T__21);
				setState(442);
				orderByExpr();
				}
				}
				setState(447);
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
		enterRule(_localctx, 50, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(448);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(451);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(449);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(450);
				((OrderByExprContext)_localctx).desc = match(Kdescending);
				}
				break;
			case T__21:
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
			setState(458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(453);
				match(Kempty);
				setState(456);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(454);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(455);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(462);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(460);
				match(Kcollation);
				setState(461);
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
		enterRule(_localctx, 52, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(464);
			match(Kcount);
			setState(465);
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
		enterRule(_localctx, 54, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(467);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(468);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(471);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(476);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(472);
				match(T__21);
				setState(473);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(478);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(479);
			match(Ksatisfies);
			setState(480);
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
		enterRule(_localctx, 56, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			varRef();
			setState(485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(483);
				match(Kas);
				setState(484);
				sequenceType();
				}
			}

			setState(487);
			match(Kin);
			setState(488);
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
		enterRule(_localctx, 58, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			match(Kswitch);
			setState(491);
			match(T__26);
			setState(492);
			((SwitchExprContext)_localctx).cond = expr();
			setState(493);
			match(T__27);
			setState(495); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(494);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(497); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(499);
			match(Kdefault);
			setState(500);
			match(Kreturn);
			setState(501);
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
		enterRule(_localctx, 60, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(503);
				match(Kcase);
				setState(504);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(507); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(509);
			match(Kreturn);
			setState(510);
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
		public TerminalNode Ktypeswitch() { return getToken(JsoniqParser.Ktypeswitch, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
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
		enterRule(_localctx, 62, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(512);
			match(Ktypeswitch);
			setState(513);
			match(T__26);
			setState(514);
			expr();
			setState(515);
			match(T__27);
			setState(517); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(516);
				caseClause();
				}
				}
				setState(519); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(521);
			match(Kdefault);
			setState(523);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(522);
				varRef();
				}
			}

			setState(525);
			match(Kreturn);
			setState(526);
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

	public static class CaseClauseContext extends ParserRuleContext {
		public TerminalNode Kcase() { return getToken(JsoniqParser.Kcase, 0); }
		public List<SequenceTypeContext> sequenceType() {
			return getRuleContexts(SequenceTypeContext.class);
		}
		public SequenceTypeContext sequenceType(int i) {
			return getRuleContext(SequenceTypeContext.class,i);
		}
		public TerminalNode Kreturn() { return getToken(JsoniqParser.Kreturn, 0); }
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
		}
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
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
		enterRule(_localctx, 64, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
			match(Kcase);
			setState(532);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(529);
				varRef();
				setState(530);
				match(Kas);
				}
			}

			setState(534);
			sequenceType();
			setState(539);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__31) {
				{
				{
				setState(535);
				match(T__31);
				setState(536);
				sequenceType();
				}
				}
				setState(541);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(542);
			match(Kreturn);
			setState(543);
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
		enterRule(_localctx, 66, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			match(Kif);
			setState(546);
			match(T__26);
			setState(547);
			((IfExprContext)_localctx).test_condition = expr();
			setState(548);
			match(T__27);
			setState(549);
			match(Kthen);
			setState(550);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(551);
			match(Kelse);
			setState(552);
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
		public TerminalNode Ktry() { return getToken(JsoniqParser.Ktry, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode Kcatch() { return getToken(JsoniqParser.Kcatch, 0); }
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
		enterRule(_localctx, 68, RULE_tryCatchExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(554);
			match(Ktry);
			setState(555);
			match(T__28);
			setState(556);
			expr();
			setState(557);
			match(T__29);
			setState(558);
			match(Kcatch);
			setState(559);
			match(T__32);
			setState(560);
			match(T__28);
			setState(561);
			expr();
			setState(562);
			match(T__29);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 70, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(564);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(569);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(565);
					match(Kor);
					setState(566);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(571);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 72, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(572);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(577);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(573);
					match(Kand);
					setState(574);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(579);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 74, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(581);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(580);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(583);
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
		public Token s34;
		public List<Token> op = new ArrayList<Token>();
		public Token s35;
		public Token s36;
		public Token s37;
		public Token s38;
		public Token s39;
		public Token s4;
		public Token s40;
		public Token s41;
		public Token s42;
		public Token s43;
		public Token s44;
		public Token _tset1033;
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
		enterRule(_localctx, 76, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(585);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(588);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) {
				{
				setState(586);
				((ComparisonExprContext)_localctx)._tset1033 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1033 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1033);
				setState(587);
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
		enterRule(_localctx, 78, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(590);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(595);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44) {
				{
				{
				setState(591);
				match(T__44);
				setState(592);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(597);
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
		enterRule(_localctx, 80, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(598);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(601);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(599);
				match(Kto);
				setState(600);
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
		public Token s46;
		public List<Token> op = new ArrayList<Token>();
		public Token s47;
		public Token _tset1142;
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
		enterRule(_localctx, 82, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(603);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(608);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(604);
					((AdditiveExprContext)_localctx)._tset1142 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__45 || _la==T__46) ) {
						((AdditiveExprContext)_localctx)._tset1142 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1142);
					setState(605);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(610);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public Token s33;
		public List<Token> op = new ArrayList<Token>();
		public Token s48;
		public Token s49;
		public Token s50;
		public Token _tset1170;
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
		enterRule(_localctx, 84, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(611);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(616);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) {
				{
				{
				setState(612);
				((MultiplicativeExprContext)_localctx)._tset1170 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1170 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1170);
				setState(613);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(618);
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
		public TreatExprContext main_expr;
		public SequenceTypeContext seq;
		public TreatExprContext treatExpr() {
			return getRuleContext(TreatExprContext.class,0);
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
		enterRule(_localctx, 86, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(619);
			((InstanceOfExprContext)_localctx).main_expr = treatExpr();
			setState(623);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(620);
				match(Kinstance);
				setState(621);
				match(Kof);
				setState(622);
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
		enterRule(_localctx, 88, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(625);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(629);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(626);
				match(Ktreat);
				setState(627);
				match(Kas);
				setState(628);
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
		enterRule(_localctx, 90, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(631);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(635);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(632);
				match(Kcastable);
				setState(633);
				match(Kas);
				setState(634);
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
		public UnaryExprContext main_expr;
		public SingleTypeContext single;
		public UnaryExprContext unaryExpr() {
			return getRuleContext(UnaryExprContext.class,0);
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
		enterRule(_localctx, 92, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			((CastExprContext)_localctx).main_expr = unaryExpr();
			setState(641);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(638);
				match(Kcast);
				setState(639);
				match(Kas);
				setState(640);
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

	public static class UnaryExprContext extends ParserRuleContext {
		public Token s47;
		public List<Token> op = new ArrayList<Token>();
		public Token s46;
		public Token _tset1287;
		public SimpleMapExprContext main_expr;
		public SimpleMapExprContext simpleMapExpr() {
			return getRuleContext(SimpleMapExprContext.class,0);
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
		enterRule(_localctx, 94, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(646);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45 || _la==T__46) {
				{
				{
				setState(643);
				((UnaryExprContext)_localctx)._tset1287 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__45 || _la==T__46) ) {
					((UnaryExprContext)_localctx)._tset1287 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1287);
				}
				}
				setState(648);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(649);
			((UnaryExprContext)_localctx).main_expr = simpleMapExpr();
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 96, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(651);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(656);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__50) {
				{
				{
				setState(652);
				match(T__50);
				setState(653);
				postFixExpr();
				}
				}
				setState(658);
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
		enterRule(_localctx, 98, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(659);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(667);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(665);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
					case 1:
						{
						setState(660);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(661);
						predicate();
						}
						break;
					case 3:
						{
						setState(662);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(663);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(664);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(669);
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
		enterRule(_localctx, 100, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(670);
			match(T__51);
			setState(671);
			match(T__51);
			setState(672);
			expr();
			setState(673);
			match(T__52);
			setState(674);
			match(T__52);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 102, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
			match(T__51);
			setState(677);
			match(T__52);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 104, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(679);
			match(T__51);
			setState(680);
			expr();
			setState(681);
			match(T__52);
			}
		}
		catch (RecognitionException re) {
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
		public TypesKeywordsContext tkw;
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
		public TypesKeywordsContext typesKeywords() {
			return getRuleContext(TypesKeywordsContext.class,0);
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
		enterRule(_localctx, 106, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(683);
			match(T__53);
			setState(691);
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
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kjson:
				{
				setState(684);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(685);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(686);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__26:
				{
				setState(687);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__30:
				{
				setState(688);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__54:
				{
				setState(689);
				((ObjectLookupContext)_localctx).ci = contextItemExpr();
				}
				break;
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
				{
				setState(690);
				((ObjectLookupContext)_localctx).tkw = typesKeywords();
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
		enterRule(_localctx, 108, RULE_primaryExpr);
		try {
			setState(705);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(693);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(694);
				match(Literal);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(695);
				stringLiteral();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(696);
				varRef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(697);
				parenthesizedExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(698);
				contextItemExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(699);
				objectConstructor();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(700);
				functionCall();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(701);
				orderedExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(702);
				unorderedExpr();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(703);
				arrayConstructor();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(704);
				functionItemExpr();
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

	public static class VarRefContext extends ParserRuleContext {
		public Token ns;
		public Token name;
		public List<TerminalNode> NCName() { return getTokens(JsoniqParser.NCName); }
		public TerminalNode NCName(int i) {
			return getToken(JsoniqParser.NCName, i);
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
		enterRule(_localctx, 110, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(707);
			match(T__30);
			setState(710);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				{
				setState(708);
				((VarRefContext)_localctx).ns = match(NCName);
				setState(709);
				match(T__9);
				}
				break;
			}
			setState(712);
			((VarRefContext)_localctx).name = match(NCName);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 112, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(714);
			match(T__26);
			setState(716);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__9 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__57 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (Kfor - 76)) | (1L << (Klet - 76)) | (1L << (Kwhere - 76)) | (1L << (Kgroup - 76)) | (1L << (Kby - 76)) | (1L << (Korder - 76)) | (1L << (Kreturn - 76)) | (1L << (Kif - 76)) | (1L << (Kin - 76)) | (1L << (Kas - 76)) | (1L << (Kat - 76)) | (1L << (Kallowing - 76)) | (1L << (Kempty - 76)) | (1L << (Kcount - 76)) | (1L << (Kstable - 76)) | (1L << (Kascending - 76)) | (1L << (Kdescending - 76)) | (1L << (Ksome - 76)) | (1L << (Kevery - 76)) | (1L << (Ksatisfies - 76)) | (1L << (Kcollation - 76)) | (1L << (Kgreatest - 76)) | (1L << (Kleast - 76)) | (1L << (Kswitch - 76)) | (1L << (Kcase - 76)) | (1L << (Ktry - 76)) | (1L << (Kcatch - 76)) | (1L << (Kdefault - 76)) | (1L << (Kthen - 76)) | (1L << (Kelse - 76)) | (1L << (Ktypeswitch - 76)) | (1L << (Kor - 76)) | (1L << (Kand - 76)) | (1L << (Knot - 76)) | (1L << (Kto - 76)) | (1L << (Kinstance - 76)) | (1L << (Kof - 76)) | (1L << (Ktreat - 76)) | (1L << (Kcast - 76)) | (1L << (Kcastable - 76)) | (1L << (Kversion - 76)) | (1L << (Kjsoniq - 76)) | (1L << (Kjson - 76)) | (1L << (STRING - 76)) | (1L << (NullLiteral - 76)) | (1L << (Literal - 76)) | (1L << (NCName - 76)))) != 0)) {
				{
				setState(715);
				expr();
				}
			}

			setState(718);
			match(T__27);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 114, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(720);
			match(T__54);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 116, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(722);
			match(T__6);
			setState(723);
			match(T__28);
			setState(724);
			expr();
			setState(725);
			match(T__29);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 118, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(727);
			match(T__7);
			setState(728);
			match(T__28);
			setState(729);
			expr();
			setState(730);
			match(T__29);
			}
		}
		catch (RecognitionException re) {
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
		public Token ns;
		public KeyWordsContext kw;
		public NCNameOrKeyWordContext fn_name;
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public NCNameOrKeyWordContext nCNameOrKeyWord() {
			return getRuleContext(NCNameOrKeyWordContext.class,0);
		}
		public List<KeyWordsContext> keyWords() {
			return getRuleContexts(KeyWordsContext.class);
		}
		public KeyWordsContext keyWords(int i) {
			return getRuleContext(KeyWordsContext.class,i);
		}
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
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
		enterRule(_localctx, 120, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(738);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				{
				setState(735);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(732);
					((FunctionCallContext)_localctx).ns = match(NCName);
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
				case Ktreat:
				case Kcast:
				case Kcastable:
				case Kversion:
				case Kjsoniq:
				case Kjson:
					{
					setState(733);
					((FunctionCallContext)_localctx).kw = keyWords();
					}
					break;
				case T__9:
					{
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(737);
				match(T__9);
				}
				break;
			}
			setState(742);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case NCName:
				{
				setState(740);
				((FunctionCallContext)_localctx).fn_name = nCNameOrKeyWord();
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
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kjson:
				{
				setState(741);
				((FunctionCallContext)_localctx).kw = keyWords();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(744);
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
		enterRule(_localctx, 122, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(746);
			match(T__26);
			setState(753);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__9 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__55 - 7)) | (1L << (T__57 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (Kfor - 76)) | (1L << (Klet - 76)) | (1L << (Kwhere - 76)) | (1L << (Kgroup - 76)) | (1L << (Kby - 76)) | (1L << (Korder - 76)) | (1L << (Kreturn - 76)) | (1L << (Kif - 76)) | (1L << (Kin - 76)) | (1L << (Kas - 76)) | (1L << (Kat - 76)) | (1L << (Kallowing - 76)) | (1L << (Kempty - 76)) | (1L << (Kcount - 76)) | (1L << (Kstable - 76)) | (1L << (Kascending - 76)) | (1L << (Kdescending - 76)) | (1L << (Ksome - 76)) | (1L << (Kevery - 76)) | (1L << (Ksatisfies - 76)) | (1L << (Kcollation - 76)) | (1L << (Kgreatest - 76)) | (1L << (Kleast - 76)) | (1L << (Kswitch - 76)) | (1L << (Kcase - 76)) | (1L << (Ktry - 76)) | (1L << (Kcatch - 76)) | (1L << (Kdefault - 76)) | (1L << (Kthen - 76)) | (1L << (Kelse - 76)) | (1L << (Ktypeswitch - 76)) | (1L << (Kor - 76)) | (1L << (Kand - 76)) | (1L << (Knot - 76)) | (1L << (Kto - 76)) | (1L << (Kinstance - 76)) | (1L << (Kof - 76)) | (1L << (Ktreat - 76)) | (1L << (Kcast - 76)) | (1L << (Kcastable - 76)) | (1L << (Kversion - 76)) | (1L << (Kjsoniq - 76)) | (1L << (Kjson - 76)) | (1L << (STRING - 76)) | (1L << (NullLiteral - 76)) | (1L << (Literal - 76)) | (1L << (NCName - 76)))) != 0)) {
				{
				{
				setState(747);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(749);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__21) {
					{
					setState(748);
					match(T__21);
					}
				}

				}
				}
				setState(755);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(756);
			match(T__27);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 124, RULE_argument);
		try {
			setState(760);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
			case T__7:
			case T__9:
			case T__25:
			case T__26:
			case T__28:
			case T__30:
			case T__45:
			case T__46:
			case T__51:
			case T__54:
			case T__57:
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
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
			case Ktreat:
			case Kcast:
			case Kcastable:
			case Kversion:
			case Kjsoniq:
			case Kjson:
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(758);
				exprSingle();
				}
				break;
			case T__55:
				enterOuterAlt(_localctx, 2);
				{
				setState(759);
				match(T__55);
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
		enterRule(_localctx, 126, RULE_functionItemExpr);
		try {
			setState(764);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(762);
				namedFunctionRef();
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 2);
				{
				setState(763);
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
		public Token fn_name;
		public Token arity;
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
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
		enterRule(_localctx, 128, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(766);
			((NamedFunctionRefContext)_localctx).fn_name = match(NCName);
			setState(767);
			match(T__56);
			setState(768);
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
		public ExprContext fn_body;
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
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
		enterRule(_localctx, 130, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(770);
			match(T__25);
			setState(771);
			match(T__26);
			setState(773);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(772);
				paramList();
				}
			}

			setState(775);
			match(T__27);
			setState(778);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(776);
				match(Kas);
				setState(777);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(780);
			match(T__28);
			setState(781);
			((InlineFunctionExprContext)_localctx).fn_body = expr();
			setState(782);
			match(T__29);
			}
			}
		}
		catch (RecognitionException re) {
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
		public Token s56;
		public List<Token> question = new ArrayList<Token>();
		public Token s33;
		public List<Token> star = new ArrayList<Token>();
		public Token s46;
		public List<Token> plus = new ArrayList<Token>();
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
		}
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
		enterRule(_localctx, 132, RULE_sequenceType);
		try {
			setState(792);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
				enterOuterAlt(_localctx, 1);
				{
				setState(784);
				match(T__26);
				setState(785);
				match(T__27);
				}
				break;
			case T__59:
			case T__60:
			case T__61:
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case T__72:
			case T__73:
			case T__74:
			case Kjson:
			case NullLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(786);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(790);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
				case 1:
					{
					setState(787);
					((SequenceTypeContext)_localctx).s56 = match(T__55);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s56);
					}
					break;
				case 2:
					{
					setState(788);
					((SequenceTypeContext)_localctx).s33 = match(T__32);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s33);
					}
					break;
				case 3:
					{
					setState(789);
					((SequenceTypeContext)_localctx).s46 = match(T__45);
					((SequenceTypeContext)_localctx).plus.add(((SequenceTypeContext)_localctx).s46);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitObjectConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectConstructorContext objectConstructor() throws RecognitionException {
		ObjectConstructorContext _localctx = new ObjectConstructorContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_objectConstructor);
		int _la;
		try {
			setState(810);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(794);
				match(T__28);
				setState(803);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__9 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__57 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (Kfor - 76)) | (1L << (Klet - 76)) | (1L << (Kwhere - 76)) | (1L << (Kgroup - 76)) | (1L << (Kby - 76)) | (1L << (Korder - 76)) | (1L << (Kreturn - 76)) | (1L << (Kif - 76)) | (1L << (Kin - 76)) | (1L << (Kas - 76)) | (1L << (Kat - 76)) | (1L << (Kallowing - 76)) | (1L << (Kempty - 76)) | (1L << (Kcount - 76)) | (1L << (Kstable - 76)) | (1L << (Kascending - 76)) | (1L << (Kdescending - 76)) | (1L << (Ksome - 76)) | (1L << (Kevery - 76)) | (1L << (Ksatisfies - 76)) | (1L << (Kcollation - 76)) | (1L << (Kgreatest - 76)) | (1L << (Kleast - 76)) | (1L << (Kswitch - 76)) | (1L << (Kcase - 76)) | (1L << (Ktry - 76)) | (1L << (Kcatch - 76)) | (1L << (Kdefault - 76)) | (1L << (Kthen - 76)) | (1L << (Kelse - 76)) | (1L << (Ktypeswitch - 76)) | (1L << (Kor - 76)) | (1L << (Kand - 76)) | (1L << (Knot - 76)) | (1L << (Kto - 76)) | (1L << (Kinstance - 76)) | (1L << (Kof - 76)) | (1L << (Ktreat - 76)) | (1L << (Kcast - 76)) | (1L << (Kcastable - 76)) | (1L << (Kversion - 76)) | (1L << (Kjsoniq - 76)) | (1L << (Kjson - 76)) | (1L << (STRING - 76)) | (1L << (NullLiteral - 76)) | (1L << (Literal - 76)) | (1L << (NCName - 76)))) != 0)) {
					{
					setState(795);
					pairConstructor();
					setState(800);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__21) {
						{
						{
						setState(796);
						match(T__21);
						setState(797);
						pairConstructor();
						}
						}
						setState(802);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(805);
				match(T__29);
				}
				break;
			case T__57:
				enterOuterAlt(_localctx, 2);
				{
				setState(806);
				((ObjectConstructorContext)_localctx).s58 = match(T__57);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s58);
				setState(807);
				expr();
				setState(808);
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

	public static class ItemTypeContext extends ParserRuleContext {
		public JSONItemTestContext jSONItemTest() {
			return getRuleContext(JSONItemTestContext.class,0);
		}
		public AtomicTypeContext atomicType() {
			return getRuleContext(AtomicTypeContext.class,0);
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
		enterRule(_localctx, 136, RULE_itemType);
		try {
			setState(815);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__59:
				enterOuterAlt(_localctx, 1);
				{
				setState(812);
				match(T__59);
				}
				break;
			case T__60:
			case T__61:
			case Kjson:
				enterOuterAlt(_localctx, 2);
				{
				setState(813);
				jSONItemTest();
				}
				break;
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case T__72:
			case T__73:
			case T__74:
			case NullLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(814);
				atomicType();
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

	public static class JSONItemTestContext extends ParserRuleContext {
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public JSONItemTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jSONItemTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitJSONItemTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JSONItemTestContext jSONItemTest() throws RecognitionException {
		JSONItemTestContext _localctx = new JSONItemTestContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_jSONItemTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(817);
			_la = _input.LA(1);
			if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (T__60 - 61)) | (1L << (T__61 - 61)) | (1L << (Kjson - 61)))) != 0)) ) {
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

	public static class KeyWordBooleanContext extends ParserRuleContext {
		public KeyWordBooleanContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordBoolean; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordBoolean(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordBooleanContext keyWordBoolean() throws RecognitionException {
		KeyWordBooleanContext _localctx = new KeyWordBooleanContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_keyWordBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(819);
			match(T__62);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordDurationContext extends ParserRuleContext {
		public KeyWordDurationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordDuration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordDuration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordDurationContext keyWordDuration() throws RecognitionException {
		KeyWordDurationContext _localctx = new KeyWordDurationContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_keyWordDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			match(T__63);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordYearMonthDurationContext extends ParserRuleContext {
		public KeyWordYearMonthDurationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordYearMonthDuration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordYearMonthDuration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordYearMonthDurationContext keyWordYearMonthDuration() throws RecognitionException {
		KeyWordYearMonthDurationContext _localctx = new KeyWordYearMonthDurationContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_keyWordYearMonthDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(823);
			match(T__64);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordDayTimeDurationContext extends ParserRuleContext {
		public KeyWordDayTimeDurationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordDayTimeDuration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordDayTimeDuration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordDayTimeDurationContext keyWordDayTimeDuration() throws RecognitionException {
		KeyWordDayTimeDurationContext _localctx = new KeyWordDayTimeDurationContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_keyWordDayTimeDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(825);
			match(T__65);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordHexBinaryContext extends ParserRuleContext {
		public KeyWordHexBinaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordHexBinary; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordHexBinary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordHexBinaryContext keyWordHexBinary() throws RecognitionException {
		KeyWordHexBinaryContext _localctx = new KeyWordHexBinaryContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_keyWordHexBinary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(827);
			match(T__66);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordBase64BinaryContext extends ParserRuleContext {
		public KeyWordBase64BinaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordBase64Binary; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordBase64Binary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordBase64BinaryContext keyWordBase64Binary() throws RecognitionException {
		KeyWordBase64BinaryContext _localctx = new KeyWordBase64BinaryContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_keyWordBase64Binary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(829);
			match(T__67);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordDateTimeContext extends ParserRuleContext {
		public KeyWordDateTimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordDateTime; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordDateTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordDateTimeContext keyWordDateTime() throws RecognitionException {
		KeyWordDateTimeContext _localctx = new KeyWordDateTimeContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_keyWordDateTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(831);
			match(T__68);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordDateContext extends ParserRuleContext {
		public KeyWordDateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordDate; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordDate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordDateContext keyWordDate() throws RecognitionException {
		KeyWordDateContext _localctx = new KeyWordDateContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_keyWordDate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(833);
			match(T__69);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypesKeywordsContext extends ParserRuleContext {
		public KeyWordBooleanContext keyWordBoolean() {
			return getRuleContext(KeyWordBooleanContext.class,0);
		}
		public KeyWordDurationContext keyWordDuration() {
			return getRuleContext(KeyWordDurationContext.class,0);
		}
		public KeyWordYearMonthDurationContext keyWordYearMonthDuration() {
			return getRuleContext(KeyWordYearMonthDurationContext.class,0);
		}
		public KeyWordDayTimeDurationContext keyWordDayTimeDuration() {
			return getRuleContext(KeyWordDayTimeDurationContext.class,0);
		}
		public KeyWordDateTimeContext keyWordDateTime() {
			return getRuleContext(KeyWordDateTimeContext.class,0);
		}
		public KeyWordDateContext keyWordDate() {
			return getRuleContext(KeyWordDateContext.class,0);
		}
		public KeyWordHexBinaryContext keyWordHexBinary() {
			return getRuleContext(KeyWordHexBinaryContext.class,0);
		}
		public KeyWordBase64BinaryContext keyWordBase64Binary() {
			return getRuleContext(KeyWordBase64BinaryContext.class,0);
		}
		public TypesKeywordsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typesKeywords; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypesKeywords(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypesKeywordsContext typesKeywords() throws RecognitionException {
		TypesKeywordsContext _localctx = new TypesKeywordsContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_typesKeywords);
		try {
			setState(843);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__62:
				enterOuterAlt(_localctx, 1);
				{
				setState(835);
				keyWordBoolean();
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 2);
				{
				setState(836);
				keyWordDuration();
				}
				break;
			case T__64:
				enterOuterAlt(_localctx, 3);
				{
				setState(837);
				keyWordYearMonthDuration();
				}
				break;
			case T__65:
				enterOuterAlt(_localctx, 4);
				{
				setState(838);
				keyWordDayTimeDuration();
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 5);
				{
				setState(839);
				keyWordDateTime();
				}
				break;
			case T__69:
				enterOuterAlt(_localctx, 6);
				{
				setState(840);
				keyWordDate();
				}
				break;
			case T__66:
				enterOuterAlt(_localctx, 7);
				{
				setState(841);
				keyWordHexBinary();
				}
				break;
			case T__67:
				enterOuterAlt(_localctx, 8);
				{
				setState(842);
				keyWordBase64Binary();
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

	public static class SingleTypeContext extends ParserRuleContext {
		public AtomicTypeContext item;
		public Token s56;
		public List<Token> question = new ArrayList<Token>();
		public AtomicTypeContext atomicType() {
			return getRuleContext(AtomicTypeContext.class,0);
		}
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
		enterRule(_localctx, 158, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(845);
			((SingleTypeContext)_localctx).item = atomicType();
			setState(847);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(846);
				((SingleTypeContext)_localctx).s56 = match(T__55);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).s56);
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

	public static class AtomicTypeContext extends ParserRuleContext {
		public TypesKeywordsContext typesKeywords() {
			return getRuleContext(TypesKeywordsContext.class,0);
		}
		public TerminalNode NullLiteral() { return getToken(JsoniqParser.NullLiteral, 0); }
		public AtomicTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomicType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAtomicType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomicTypeContext atomicType() throws RecognitionException {
		AtomicTypeContext _localctx = new AtomicTypeContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_atomicType);
		try {
			setState(856);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__70:
				enterOuterAlt(_localctx, 1);
				{
				setState(849);
				match(T__70);
				}
				break;
			case T__71:
				enterOuterAlt(_localctx, 2);
				{
				setState(850);
				match(T__71);
				}
				break;
			case T__72:
				enterOuterAlt(_localctx, 3);
				{
				setState(851);
				match(T__72);
				}
				break;
			case T__73:
				enterOuterAlt(_localctx, 4);
				{
				setState(852);
				match(T__73);
				}
				break;
			case T__74:
				enterOuterAlt(_localctx, 5);
				{
				setState(853);
				match(T__74);
				}
				break;
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
				enterOuterAlt(_localctx, 6);
				{
				setState(854);
				typesKeywords();
				}
				break;
			case NullLiteral:
				enterOuterAlt(_localctx, 7);
				{
				setState(855);
				match(NullLiteral);
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

	public static class NCNameOrKeyWordContext extends ParserRuleContext {
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public TypesKeywordsContext typesKeywords() {
			return getRuleContext(TypesKeywordsContext.class,0);
		}
		public NCNameOrKeyWordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nCNameOrKeyWord; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNCNameOrKeyWord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NCNameOrKeyWordContext nCNameOrKeyWord() throws RecognitionException {
		NCNameOrKeyWordContext _localctx = new NCNameOrKeyWordContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_nCNameOrKeyWord);
		try {
			setState(860);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(858);
				match(NCName);
				}
				break;
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
				enterOuterAlt(_localctx, 2);
				{
				setState(859);
				typesKeywords();
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

	public static class PairConstructorContext extends ParserRuleContext {
		public ExprSingleContext lhs;
		public Token name;
		public ExprSingleContext rhs;
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
		enterRule(_localctx, 164, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(862);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(863);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(866);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==T__55) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(867);
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
		enterRule(_localctx, 166, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(869);
			match(T__51);
			setState(871);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__9 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__57 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (Kfor - 76)) | (1L << (Klet - 76)) | (1L << (Kwhere - 76)) | (1L << (Kgroup - 76)) | (1L << (Kby - 76)) | (1L << (Korder - 76)) | (1L << (Kreturn - 76)) | (1L << (Kif - 76)) | (1L << (Kin - 76)) | (1L << (Kas - 76)) | (1L << (Kat - 76)) | (1L << (Kallowing - 76)) | (1L << (Kempty - 76)) | (1L << (Kcount - 76)) | (1L << (Kstable - 76)) | (1L << (Kascending - 76)) | (1L << (Kdescending - 76)) | (1L << (Ksome - 76)) | (1L << (Kevery - 76)) | (1L << (Ksatisfies - 76)) | (1L << (Kcollation - 76)) | (1L << (Kgreatest - 76)) | (1L << (Kleast - 76)) | (1L << (Kswitch - 76)) | (1L << (Kcase - 76)) | (1L << (Ktry - 76)) | (1L << (Kcatch - 76)) | (1L << (Kdefault - 76)) | (1L << (Kthen - 76)) | (1L << (Kelse - 76)) | (1L << (Ktypeswitch - 76)) | (1L << (Kor - 76)) | (1L << (Kand - 76)) | (1L << (Knot - 76)) | (1L << (Kto - 76)) | (1L << (Kinstance - 76)) | (1L << (Kof - 76)) | (1L << (Ktreat - 76)) | (1L << (Kcast - 76)) | (1L << (Kcastable - 76)) | (1L << (Kversion - 76)) | (1L << (Kjsoniq - 76)) | (1L << (Kjson - 76)) | (1L << (STRING - 76)) | (1L << (NullLiteral - 76)) | (1L << (Literal - 76)) | (1L << (NCName - 76)))) != 0)) {
				{
				setState(870);
				expr();
				}
			}

			setState(873);
			match(T__52);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 168, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875);
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
		enterRule(_localctx, 170, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(877);
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
		public TerminalNode Kjson() { return getToken(JsoniqParser.Kjson, 0); }
		public TerminalNode Kversion() { return getToken(JsoniqParser.Kversion, 0); }
		public TerminalNode Ktypeswitch() { return getToken(JsoniqParser.Ktypeswitch, 0); }
		public TerminalNode Kor() { return getToken(JsoniqParser.Kor, 0); }
		public TerminalNode Kand() { return getToken(JsoniqParser.Kand, 0); }
		public TerminalNode Knot() { return getToken(JsoniqParser.Knot, 0); }
		public TerminalNode Kto() { return getToken(JsoniqParser.Kto, 0); }
		public TerminalNode Kinstance() { return getToken(JsoniqParser.Kinstance, 0); }
		public TerminalNode Kof() { return getToken(JsoniqParser.Kof, 0); }
		public TerminalNode Ktreat() { return getToken(JsoniqParser.Ktreat, 0); }
		public TerminalNode Kcast() { return getToken(JsoniqParser.Kcast, 0); }
		public TerminalNode Kcastable() { return getToken(JsoniqParser.Kcastable, 0); }
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kthen() { return getToken(JsoniqParser.Kthen, 0); }
		public TerminalNode Kelse() { return getToken(JsoniqParser.Kelse, 0); }
		public TerminalNode Kcollation() { return getToken(JsoniqParser.Kcollation, 0); }
		public TerminalNode Kgreatest() { return getToken(JsoniqParser.Kgreatest, 0); }
		public TerminalNode Kleast() { return getToken(JsoniqParser.Kleast, 0); }
		public TerminalNode Kswitch() { return getToken(JsoniqParser.Kswitch, 0); }
		public TerminalNode Kcase() { return getToken(JsoniqParser.Kcase, 0); }
		public TerminalNode Ktry() { return getToken(JsoniqParser.Ktry, 0); }
		public TerminalNode Kcatch() { return getToken(JsoniqParser.Kcatch, 0); }
		public TerminalNode Ksome() { return getToken(JsoniqParser.Ksome, 0); }
		public TerminalNode Kevery() { return getToken(JsoniqParser.Kevery, 0); }
		public TerminalNode Ksatisfies() { return getToken(JsoniqParser.Ksatisfies, 0); }
		public TerminalNode Kstable() { return getToken(JsoniqParser.Kstable, 0); }
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
		enterRule(_localctx, 172, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(879);
			_la = _input.LA(1);
			if ( !(((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (Kfor - 76)) | (1L << (Klet - 76)) | (1L << (Kwhere - 76)) | (1L << (Kgroup - 76)) | (1L << (Kby - 76)) | (1L << (Korder - 76)) | (1L << (Kreturn - 76)) | (1L << (Kif - 76)) | (1L << (Kin - 76)) | (1L << (Kas - 76)) | (1L << (Kat - 76)) | (1L << (Kallowing - 76)) | (1L << (Kempty - 76)) | (1L << (Kcount - 76)) | (1L << (Kstable - 76)) | (1L << (Kascending - 76)) | (1L << (Kdescending - 76)) | (1L << (Ksome - 76)) | (1L << (Kevery - 76)) | (1L << (Ksatisfies - 76)) | (1L << (Kcollation - 76)) | (1L << (Kgreatest - 76)) | (1L << (Kleast - 76)) | (1L << (Kswitch - 76)) | (1L << (Kcase - 76)) | (1L << (Ktry - 76)) | (1L << (Kcatch - 76)) | (1L << (Kdefault - 76)) | (1L << (Kthen - 76)) | (1L << (Kelse - 76)) | (1L << (Ktypeswitch - 76)) | (1L << (Kor - 76)) | (1L << (Kand - 76)) | (1L << (Knot - 76)) | (1L << (Kto - 76)) | (1L << (Kinstance - 76)) | (1L << (Kof - 76)) | (1L << (Ktreat - 76)) | (1L << (Kcast - 76)) | (1L << (Kcastable - 76)) | (1L << (Kversion - 76)) | (1L << (Kjsoniq - 76)) | (1L << (Kjson - 76)))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0084\u0374\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\3\2\3\2\3\2\3\2\3\2\5\2\u00b6\n\2\3\2\3\2\5\2"+
		"\u00ba\n\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\5\5\5\u00cc\n\5\3\5\3\5\7\5\u00d0\n\5\f\5\16\5\u00d3\13\5\3\5\3\5\5"+
		"\5\u00d7\n\5\3\5\3\5\7\5\u00db\n\5\f\5\16\5\u00de\13\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\5\t\u00f3"+
		"\n\t\3\t\3\t\3\t\5\t\u00f8\n\t\3\t\3\t\3\t\3\t\7\t\u00fe\n\t\f\t\16\t"+
		"\u0101\13\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\5\13\u010a\n\13\3\13\3\13"+
		"\3\13\3\13\3\13\7\13\u0111\n\13\f\13\16\13\u0114\13\13\5\13\u0116\n\13"+
		"\3\f\3\f\3\f\3\f\3\f\5\f\u011d\n\f\3\f\3\f\3\f\3\f\3\f\5\f\u0124\n\f\5"+
		"\f\u0126\n\f\3\r\3\r\3\r\3\r\5\r\u012c\n\r\3\r\3\r\3\r\5\r\u0131\n\r\3"+
		"\r\3\r\3\r\5\r\u0136\n\r\3\r\3\r\3\r\3\r\3\r\5\r\u013d\n\r\3\16\3\16\3"+
		"\16\7\16\u0142\n\16\f\16\16\16\u0145\13\16\3\17\3\17\3\17\3\17\5\17\u014b"+
		"\n\17\3\20\3\20\3\20\7\20\u0150\n\20\f\20\16\20\u0153\13\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\5\21\u015c\n\21\3\22\3\22\5\22\u0160\n\22\3"+
		"\22\3\22\3\22\3\22\3\22\3\22\7\22\u0168\n\22\f\22\16\22\u016b\13\22\3"+
		"\22\3\22\3\22\3\23\3\23\3\23\3\23\7\23\u0174\n\23\f\23\16\23\u0177\13"+
		"\23\3\24\3\24\3\24\5\24\u017c\n\24\3\24\3\24\5\24\u0180\n\24\3\24\3\24"+
		"\5\24\u0184\n\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\7\25\u018d\n\25\f"+
		"\25\16\25\u0190\13\25\3\26\3\26\3\26\5\26\u0195\n\26\3\26\3\26\3\26\3"+
		"\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\7\30\u01a2\n\30\f\30\16\30\u01a5"+
		"\13\30\3\31\3\31\3\31\5\31\u01aa\n\31\3\31\3\31\5\31\u01ae\n\31\3\31\3"+
		"\31\5\31\u01b2\n\31\3\32\3\32\3\32\3\32\3\32\5\32\u01b9\n\32\3\32\3\32"+
		"\3\32\7\32\u01be\n\32\f\32\16\32\u01c1\13\32\3\33\3\33\3\33\5\33\u01c6"+
		"\n\33\3\33\3\33\3\33\5\33\u01cb\n\33\5\33\u01cd\n\33\3\33\3\33\5\33\u01d1"+
		"\n\33\3\34\3\34\3\34\3\35\3\35\5\35\u01d8\n\35\3\35\3\35\3\35\7\35\u01dd"+
		"\n\35\f\35\16\35\u01e0\13\35\3\35\3\35\3\35\3\36\3\36\3\36\5\36\u01e8"+
		"\n\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\6\37\u01f2\n\37\r\37\16"+
		"\37\u01f3\3\37\3\37\3\37\3\37\3 \3 \6 \u01fc\n \r \16 \u01fd\3 \3 \3 "+
		"\3!\3!\3!\3!\3!\6!\u0208\n!\r!\16!\u0209\3!\3!\5!\u020e\n!\3!\3!\3!\3"+
		"\"\3\"\3\"\3\"\5\"\u0217\n\"\3\"\3\"\3\"\7\"\u021c\n\"\f\"\16\"\u021f"+
		"\13\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3"+
		"$\3$\3%\3%\3%\7%\u023a\n%\f%\16%\u023d\13%\3&\3&\3&\7&\u0242\n&\f&\16"+
		"&\u0245\13&\3\'\5\'\u0248\n\'\3\'\3\'\3(\3(\3(\5(\u024f\n(\3)\3)\3)\7"+
		")\u0254\n)\f)\16)\u0257\13)\3*\3*\3*\5*\u025c\n*\3+\3+\3+\7+\u0261\n+"+
		"\f+\16+\u0264\13+\3,\3,\3,\7,\u0269\n,\f,\16,\u026c\13,\3-\3-\3-\3-\5"+
		"-\u0272\n-\3.\3.\3.\3.\5.\u0278\n.\3/\3/\3/\3/\5/\u027e\n/\3\60\3\60\3"+
		"\60\3\60\5\60\u0284\n\60\3\61\7\61\u0287\n\61\f\61\16\61\u028a\13\61\3"+
		"\61\3\61\3\62\3\62\3\62\7\62\u0291\n\62\f\62\16\62\u0294\13\62\3\63\3"+
		"\63\3\63\3\63\3\63\3\63\7\63\u029c\n\63\f\63\16\63\u029f\13\63\3\64\3"+
		"\64\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\67\3\67\3"+
		"\67\3\67\3\67\3\67\3\67\3\67\5\67\u02b6\n\67\38\38\38\38\38\38\38\38\3"+
		"8\38\38\38\58\u02c4\n8\39\39\39\59\u02c9\n9\39\39\3:\3:\5:\u02cf\n:\3"+
		":\3:\3;\3;\3<\3<\3<\3<\3<\3=\3=\3=\3=\3=\3>\3>\3>\5>\u02e2\n>\3>\5>\u02e5"+
		"\n>\3>\3>\5>\u02e9\n>\3>\3>\3?\3?\3?\5?\u02f0\n?\7?\u02f2\n?\f?\16?\u02f5"+
		"\13?\3?\3?\3@\3@\5@\u02fb\n@\3A\3A\5A\u02ff\nA\3B\3B\3B\3B\3C\3C\3C\5"+
		"C\u0308\nC\3C\3C\3C\5C\u030d\nC\3C\3C\3C\3C\3D\3D\3D\3D\3D\3D\5D\u0319"+
		"\nD\5D\u031b\nD\3E\3E\3E\3E\7E\u0321\nE\fE\16E\u0324\13E\5E\u0326\nE\3"+
		"E\3E\3E\3E\3E\5E\u032d\nE\3F\3F\3F\5F\u0332\nF\3G\3G\3H\3H\3I\3I\3J\3"+
		"J\3K\3K\3L\3L\3M\3M\3N\3N\3O\3O\3P\3P\3P\3P\3P\3P\3P\3P\5P\u034e\nP\3"+
		"Q\3Q\5Q\u0352\nQ\3R\3R\3R\3R\3R\3R\3R\5R\u035b\nR\3S\3S\5S\u035f\nS\3"+
		"T\3T\5T\u0363\nT\3T\3T\3T\3U\3U\5U\u036a\nU\3U\3U\3V\3V\3W\3W\3X\3X\3"+
		"X\2\2Y\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<"+
		">@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a"+
		"\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2"+
		"\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\2\13\3\2\t\n\3\2cd\3\2\r\26\4\2\6"+
		"\6$.\3\2\60\61\4\2##\62\64\4\2?@xx\4\2\f\f::\3\2Nx\2\u03a6\2\u00b5\3\2"+
		"\2\2\4\u00bb\3\2\2\2\6\u00be\3\2\2\2\b\u00d1\3\2\2\2\n\u00df\3\2\2\2\f"+
		"\u00e4\3\2\2\2\16\u00e8\3\2\2\2\20\u00ee\3\2\2\2\22\u0102\3\2\2\2\24\u0104"+
		"\3\2\2\2\26\u0117\3\2\2\2\30\u0127\3\2\2\2\32\u013e\3\2\2\2\34\u0146\3"+
		"\2\2\2\36\u014c\3\2\2\2 \u015b\3\2\2\2\"\u015f\3\2\2\2$\u016f\3\2\2\2"+
		"&\u0178\3\2\2\2(\u0188\3\2\2\2*\u0191\3\2\2\2,\u0199\3\2\2\2.\u019c\3"+
		"\2\2\2\60\u01a6\3\2\2\2\62\u01b8\3\2\2\2\64\u01c2\3\2\2\2\66\u01d2\3\2"+
		"\2\28\u01d7\3\2\2\2:\u01e4\3\2\2\2<\u01ec\3\2\2\2>\u01fb\3\2\2\2@\u0202"+
		"\3\2\2\2B\u0212\3\2\2\2D\u0223\3\2\2\2F\u022c\3\2\2\2H\u0236\3\2\2\2J"+
		"\u023e\3\2\2\2L\u0247\3\2\2\2N\u024b\3\2\2\2P\u0250\3\2\2\2R\u0258\3\2"+
		"\2\2T\u025d\3\2\2\2V\u0265\3\2\2\2X\u026d\3\2\2\2Z\u0273\3\2\2\2\\\u0279"+
		"\3\2\2\2^\u027f\3\2\2\2`\u0288\3\2\2\2b\u028d\3\2\2\2d\u0295\3\2\2\2f"+
		"\u02a0\3\2\2\2h\u02a6\3\2\2\2j\u02a9\3\2\2\2l\u02ad\3\2\2\2n\u02c3\3\2"+
		"\2\2p\u02c5\3\2\2\2r\u02cc\3\2\2\2t\u02d2\3\2\2\2v\u02d4\3\2\2\2x\u02d9"+
		"\3\2\2\2z\u02e4\3\2\2\2|\u02ec\3\2\2\2~\u02fa\3\2\2\2\u0080\u02fe\3\2"+
		"\2\2\u0082\u0300\3\2\2\2\u0084\u0304\3\2\2\2\u0086\u031a\3\2\2\2\u0088"+
		"\u032c\3\2\2\2\u008a\u0331\3\2\2\2\u008c\u0333\3\2\2\2\u008e\u0335\3\2"+
		"\2\2\u0090\u0337\3\2\2\2\u0092\u0339\3\2\2\2\u0094\u033b\3\2\2\2\u0096"+
		"\u033d\3\2\2\2\u0098\u033f\3\2\2\2\u009a\u0341\3\2\2\2\u009c\u0343\3\2"+
		"\2\2\u009e\u034d\3\2\2\2\u00a0\u034f\3\2\2\2\u00a2\u035a\3\2\2\2\u00a4"+
		"\u035e\3\2\2\2\u00a6\u0362\3\2\2\2\u00a8\u0367\3\2\2\2\u00aa\u036d\3\2"+
		"\2\2\u00ac\u036f\3\2\2\2\u00ae\u0371\3\2\2\2\u00b0\u00b1\7w\2\2\u00b1"+
		"\u00b2\7v\2\2\u00b2\u00b3\5\u00acW\2\u00b3\u00b4\7\3\2\2\u00b4\u00b6\3"+
		"\2\2\2\u00b5\u00b0\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7"+
		"\u00ba\5\6\4\2\u00b8\u00ba\5\4\3\2\u00b9\u00b7\3\2\2\2\u00b9\u00b8\3\2"+
		"\2\2\u00ba\3\3\2\2\2\u00bb\u00bc\5\b\5\2\u00bc\u00bd\5\36\20\2\u00bd\5"+
		"\3\2\2\2\u00be\u00bf\7\4\2\2\u00bf\u00c0\7\5\2\2\u00c0\u00c1\7\u0082\2"+
		"\2\u00c1\u00c2\7\6\2\2\u00c2\u00c3\5\u00aaV\2\u00c3\u00c4\7\3\2\2\u00c4"+
		"\u00c5\5\b\5\2\u00c5\7\3\2\2\2\u00c6\u00cc\5\n\6\2\u00c7\u00cc\5\f\7\2"+
		"\u00c8\u00cc\5\16\b\2\u00c9\u00cc\5\20\t\2\u00ca\u00cc\5\24\13\2\u00cb"+
		"\u00c6\3\2\2\2\u00cb\u00c7\3\2\2\2\u00cb\u00c8\3\2\2\2\u00cb\u00c9\3\2"+
		"\2\2\u00cb\u00ca\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00ce\7\3\2\2\u00ce"+
		"\u00d0\3\2\2\2\u00cf\u00cb\3\2\2\2\u00d0\u00d3\3\2\2\2\u00d1\u00cf\3\2"+
		"\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00dc\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d4"+
		"\u00d7\5\30\r\2\u00d5\u00d7\5\26\f\2\u00d6\u00d4\3\2\2\2\u00d6\u00d5\3"+
		"\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00d9\7\3\2\2\u00d9\u00db\3\2\2\2\u00da"+
		"\u00d6\3\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2"+
		"\2\2\u00dd\t\3\2\2\2\u00de\u00dc\3\2\2\2\u00df\u00e0\7\7\2\2\u00e0\u00e1"+
		"\7i\2\2\u00e1\u00e2\7b\2\2\u00e2\u00e3\5\u00aaV\2\u00e3\13\3\2\2\2\u00e4"+
		"\u00e5\7\7\2\2\u00e5\u00e6\7\b\2\2\u00e6\u00e7\t\2\2\2\u00e7\r\3\2\2\2"+
		"\u00e8\u00e9\7\7\2\2\u00e9\u00ea\7i\2\2\u00ea\u00eb\7S\2\2\u00eb\u00ec"+
		"\7Z\2\2\u00ec\u00ed\t\3\2\2\u00ed\17\3\2\2\2\u00ee\u00f7\7\7\2\2\u00ef"+
		"\u00f2\7\13\2\2\u00f0\u00f1\7\u0082\2\2\u00f1\u00f3\7\f\2\2\u00f2\u00f0"+
		"\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f8\7\u0082\2"+
		"\2\u00f5\u00f6\7i\2\2\u00f6\u00f8\7\13\2\2\u00f7\u00ef\3\2\2\2\u00f7\u00f5"+
		"\3\2\2\2\u00f8\u00ff\3\2\2\2\u00f9\u00fa\5\22\n\2\u00fa\u00fb\7\6\2\2"+
		"\u00fb\u00fc\5\u00acW\2\u00fc\u00fe\3\2\2\2\u00fd\u00f9\3\2\2\2\u00fe"+
		"\u0101\3\2\2\2\u00ff\u00fd\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\21\3\2\2"+
		"\2\u0101\u00ff\3\2\2\2\u0102\u0103\t\4\2\2\u0103\23\3\2\2\2\u0104\u0105"+
		"\7\27\2\2\u0105\u0109\7\4\2\2\u0106\u0107\7\5\2\2\u0107\u0108\7\u0082"+
		"\2\2\u0108\u010a\7\6\2\2\u0109\u0106\3\2\2\2\u0109\u010a\3\2\2\2\u010a"+
		"\u010b\3\2\2\2\u010b\u0115\5\u00aaV\2\u010c\u010d\7X\2\2\u010d\u0112\5"+
		"\u00aaV\2\u010e\u010f\7\30\2\2\u010f\u0111\5\u00aaV\2\u0110\u010e\3\2"+
		"\2\2\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113"+
		"\u0116\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u010c\3\2\2\2\u0115\u0116\3\2"+
		"\2\2\u0116\25\3\2\2\2\u0117\u0118\7\7\2\2\u0118\u0119\7\31\2\2\u0119\u011c"+
		"\5p9\2\u011a\u011b\7W\2\2\u011b\u011d\5\u0086D\2\u011c\u011a\3\2\2\2\u011c"+
		"\u011d\3\2\2\2\u011d\u0125\3\2\2\2\u011e\u011f\7\32\2\2\u011f\u0126\5"+
		" \21\2\u0120\u0123\7\33\2\2\u0121\u0122\7\32\2\2\u0122\u0124\5 \21\2\u0123"+
		"\u0121\3\2\2\2\u0123\u0124\3\2\2\2\u0124\u0126\3\2\2\2\u0125\u011e\3\2"+
		"\2\2\u0125\u0120\3\2\2\2\u0126\27\3\2\2\2\u0127\u0128\7\7\2\2\u0128\u012b"+
		"\7\34\2\2\u0129\u012a\7\u0082\2\2\u012a\u012c\7\f\2\2\u012b\u0129\3\2"+
		"\2\2\u012b\u012c\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u012e\7\u0082\2\2\u012e"+
		"\u0130\7\35\2\2\u012f\u0131\5\32\16\2\u0130\u012f\3\2\2\2\u0130\u0131"+
		"\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0135\7\36\2\2\u0133\u0134\7W\2\2\u0134"+
		"\u0136\5\u0086D\2\u0135\u0133\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u013c"+
		"\3\2\2\2\u0137\u0138\7\37\2\2\u0138\u0139\5\36\20\2\u0139\u013a\7 \2\2"+
		"\u013a\u013d\3\2\2\2\u013b\u013d\7\33\2\2\u013c\u0137\3\2\2\2\u013c\u013b"+
		"\3\2\2\2\u013d\31\3\2\2\2\u013e\u0143\5\34\17\2\u013f\u0140\7\30\2\2\u0140"+
		"\u0142\5\34\17\2\u0141\u013f\3\2\2\2\u0142\u0145\3\2\2\2\u0143\u0141\3"+
		"\2\2\2\u0143\u0144\3\2\2\2\u0144\33\3\2\2\2\u0145\u0143\3\2\2\2\u0146"+
		"\u0147\7!\2\2\u0147\u014a\7\u0082\2\2\u0148\u0149\7W\2\2\u0149\u014b\5"+
		"\u0086D\2\u014a\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b\35\3\2\2\2\u014c"+
		"\u0151\5 \21\2\u014d\u014e\7\30\2\2\u014e\u0150\5 \21\2\u014f\u014d\3"+
		"\2\2\2\u0150\u0153\3\2\2\2\u0151\u014f\3\2\2\2\u0151\u0152\3\2\2\2\u0152"+
		"\37\3\2\2\2\u0153\u0151\3\2\2\2\u0154\u015c\5\"\22\2\u0155\u015c\58\35"+
		"\2\u0156\u015c\5<\37\2\u0157\u015c\5@!\2\u0158\u015c\5D#\2\u0159\u015c"+
		"\5F$\2\u015a\u015c\5H%\2\u015b\u0154\3\2\2\2\u015b\u0155\3\2\2\2\u015b"+
		"\u0156\3\2\2\2\u015b\u0157\3\2\2\2\u015b\u0158\3\2\2\2\u015b\u0159\3\2"+
		"\2\2\u015b\u015a\3\2\2\2\u015c!\3\2\2\2\u015d\u0160\5$\23\2\u015e\u0160"+
		"\5(\25\2\u015f\u015d\3\2\2\2\u015f\u015e\3\2\2\2\u0160\u0169\3\2\2\2\u0161"+
		"\u0168\5$\23\2\u0162\u0168\5,\27\2\u0163\u0168\5(\25\2\u0164\u0168\5."+
		"\30\2\u0165\u0168\5\62\32\2\u0166\u0168\5\66\34\2\u0167\u0161\3\2\2\2"+
		"\u0167\u0162\3\2\2\2\u0167\u0163\3\2\2\2\u0167\u0164\3\2\2\2\u0167\u0165"+
		"\3\2\2\2\u0167\u0166\3\2\2\2\u0168\u016b\3\2\2\2\u0169\u0167\3\2\2\2\u0169"+
		"\u016a\3\2\2\2\u016a\u016c\3\2\2\2\u016b\u0169\3\2\2\2\u016c\u016d\7T"+
		"\2\2\u016d\u016e\5 \21\2\u016e#\3\2\2\2\u016f\u0170\7N\2\2\u0170\u0175"+
		"\5&\24\2\u0171\u0172\7\30\2\2\u0172\u0174\5&\24\2\u0173\u0171\3\2\2\2"+
		"\u0174\u0177\3\2\2\2\u0175\u0173\3\2\2\2\u0175\u0176\3\2\2\2\u0176%\3"+
		"\2\2\2\u0177\u0175\3\2\2\2\u0178\u017b\5p9\2\u0179\u017a\7W\2\2\u017a"+
		"\u017c\5\u0086D\2\u017b\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u017f"+
		"\3\2\2\2\u017d\u017e\7Y\2\2\u017e\u0180\7Z\2\2\u017f\u017d\3\2\2\2\u017f"+
		"\u0180\3\2\2\2\u0180\u0183\3\2\2\2\u0181\u0182\7X\2\2\u0182\u0184\5p9"+
		"\2\u0183\u0181\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u0185\3\2\2\2\u0185\u0186"+
		"\7V\2\2\u0186\u0187\5 \21\2\u0187\'\3\2\2\2\u0188\u0189\7O\2\2\u0189\u018e"+
		"\5*\26\2\u018a\u018b\7\30\2\2\u018b\u018d\5*\26\2\u018c\u018a\3\2\2\2"+
		"\u018d\u0190\3\2\2\2\u018e\u018c\3\2\2\2\u018e\u018f\3\2\2\2\u018f)\3"+
		"\2\2\2\u0190\u018e\3\2\2\2\u0191\u0194\5p9\2\u0192\u0193\7W\2\2\u0193"+
		"\u0195\5\u0086D\2\u0194\u0192\3\2\2\2\u0194\u0195\3\2\2\2\u0195\u0196"+
		"\3\2\2\2\u0196\u0197\7\32\2\2\u0197\u0198\5 \21\2\u0198+\3\2\2\2\u0199"+
		"\u019a\7P\2\2\u019a\u019b\5 \21\2\u019b-\3\2\2\2\u019c\u019d\7Q\2\2\u019d"+
		"\u019e\7R\2\2\u019e\u01a3\5\60\31\2\u019f\u01a0\7\30\2\2\u01a0\u01a2\5"+
		"\60\31\2\u01a1\u019f\3\2\2\2\u01a2\u01a5\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a3"+
		"\u01a4\3\2\2\2\u01a4/\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a6\u01ad\5p9\2\u01a7"+
		"\u01a8\7W\2\2\u01a8\u01aa\5\u0086D\2\u01a9\u01a7\3\2\2\2\u01a9\u01aa\3"+
		"\2\2\2\u01aa\u01ab\3\2\2\2\u01ab\u01ac\7\32\2\2\u01ac\u01ae\5 \21\2\u01ad"+
		"\u01a9\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01b1\3\2\2\2\u01af\u01b0\7b"+
		"\2\2\u01b0\u01b2\5\u00aaV\2\u01b1\u01af\3\2\2\2\u01b1\u01b2\3\2\2\2\u01b2"+
		"\61\3\2\2\2\u01b3\u01b4\7S\2\2\u01b4\u01b9\7R\2\2\u01b5\u01b6\7\\\2\2"+
		"\u01b6\u01b7\7S\2\2\u01b7\u01b9\7R\2\2\u01b8\u01b3\3\2\2\2\u01b8\u01b5"+
		"\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bf\5\64\33\2\u01bb\u01bc\7\30\2"+
		"\2\u01bc\u01be\5\64\33\2\u01bd\u01bb\3\2\2\2\u01be\u01c1\3\2\2\2\u01bf"+
		"\u01bd\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\63\3\2\2\2\u01c1\u01bf\3\2\2"+
		"\2\u01c2\u01c5\5 \21\2\u01c3\u01c6\7]\2\2\u01c4\u01c6\7^\2\2\u01c5\u01c3"+
		"\3\2\2\2\u01c5\u01c4\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01cc\3\2\2\2\u01c7"+
		"\u01ca\7Z\2\2\u01c8\u01cb\7c\2\2\u01c9\u01cb\7d\2\2\u01ca\u01c8\3\2\2"+
		"\2\u01ca\u01c9\3\2\2\2\u01cb\u01cd\3\2\2\2\u01cc\u01c7\3\2\2\2\u01cc\u01cd"+
		"\3\2\2\2\u01cd\u01d0\3\2\2\2\u01ce\u01cf\7b\2\2\u01cf\u01d1\5\u00aaV\2"+
		"\u01d0\u01ce\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1\65\3\2\2\2\u01d2\u01d3"+
		"\7[\2\2\u01d3\u01d4\5p9\2\u01d4\67\3\2\2\2\u01d5\u01d8\7_\2\2\u01d6\u01d8"+
		"\7`\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d6\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9"+
		"\u01de\5:\36\2\u01da\u01db\7\30\2\2\u01db\u01dd\5:\36\2\u01dc\u01da\3"+
		"\2\2\2\u01dd\u01e0\3\2\2\2\u01de\u01dc\3\2\2\2\u01de\u01df\3\2\2\2\u01df"+
		"\u01e1\3\2\2\2\u01e0\u01de\3\2\2\2\u01e1\u01e2\7a\2\2\u01e2\u01e3\5 \21"+
		"\2\u01e39\3\2\2\2\u01e4\u01e7\5p9\2\u01e5\u01e6\7W\2\2\u01e6\u01e8\5\u0086"+
		"D\2\u01e7\u01e5\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01e9\3\2\2\2\u01e9"+
		"\u01ea\7V\2\2\u01ea\u01eb\5 \21\2\u01eb;\3\2\2\2\u01ec\u01ed\7e\2\2\u01ed"+
		"\u01ee\7\35\2\2\u01ee\u01ef\5\36\20\2\u01ef\u01f1\7\36\2\2\u01f0\u01f2"+
		"\5> \2\u01f1\u01f0\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f1\3\2\2\2\u01f3"+
		"\u01f4\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5\u01f6\7i\2\2\u01f6\u01f7\7T\2"+
		"\2\u01f7\u01f8\5 \21\2\u01f8=\3\2\2\2\u01f9\u01fa\7f\2\2\u01fa\u01fc\5"+
		" \21\2\u01fb\u01f9\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd\u01fb\3\2\2\2\u01fd"+
		"\u01fe\3\2\2\2\u01fe\u01ff\3\2\2\2\u01ff\u0200\7T\2\2\u0200\u0201\5 \21"+
		"\2\u0201?\3\2\2\2\u0202\u0203\7l\2\2\u0203\u0204\7\35\2\2\u0204\u0205"+
		"\5\36\20\2\u0205\u0207\7\36\2\2\u0206\u0208\5B\"\2\u0207\u0206\3\2\2\2"+
		"\u0208\u0209\3\2\2\2\u0209\u0207\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020b"+
		"\3\2\2\2\u020b\u020d\7i\2\2\u020c\u020e\5p9\2\u020d\u020c\3\2\2\2\u020d"+
		"\u020e\3\2\2\2\u020e\u020f\3\2\2\2\u020f\u0210\7T\2\2\u0210\u0211\5 \21"+
		"\2\u0211A\3\2\2\2\u0212\u0216\7f\2\2\u0213\u0214\5p9\2\u0214\u0215\7W"+
		"\2\2\u0215\u0217\3\2\2\2\u0216\u0213\3\2\2\2\u0216\u0217\3\2\2\2\u0217"+
		"\u0218\3\2\2\2\u0218\u021d\5\u0086D\2\u0219\u021a\7\"\2\2\u021a\u021c"+
		"\5\u0086D\2\u021b\u0219\3\2\2\2\u021c\u021f\3\2\2\2\u021d\u021b\3\2\2"+
		"\2\u021d\u021e\3\2\2\2\u021e\u0220\3\2\2\2\u021f\u021d\3\2\2\2\u0220\u0221"+
		"\7T\2\2\u0221\u0222\5 \21\2\u0222C\3\2\2\2\u0223\u0224\7U\2\2\u0224\u0225"+
		"\7\35\2\2\u0225\u0226\5\36\20\2\u0226\u0227\7\36\2\2\u0227\u0228\7j\2"+
		"\2\u0228\u0229\5 \21\2\u0229\u022a\7k\2\2\u022a\u022b\5 \21\2\u022bE\3"+
		"\2\2\2\u022c\u022d\7g\2\2\u022d\u022e\7\37\2\2\u022e\u022f\5\36\20\2\u022f"+
		"\u0230\7 \2\2\u0230\u0231\7h\2\2\u0231\u0232\7#\2\2\u0232\u0233\7\37\2"+
		"\2\u0233\u0234\5\36\20\2\u0234\u0235\7 \2\2\u0235G\3\2\2\2\u0236\u023b"+
		"\5J&\2\u0237\u0238\7m\2\2\u0238\u023a\5J&\2\u0239\u0237\3\2\2\2\u023a"+
		"\u023d\3\2\2\2\u023b\u0239\3\2\2\2\u023b\u023c\3\2\2\2\u023cI\3\2\2\2"+
		"\u023d\u023b\3\2\2\2\u023e\u0243\5L\'\2\u023f\u0240\7n\2\2\u0240\u0242"+
		"\5L\'\2\u0241\u023f\3\2\2\2\u0242\u0245\3\2\2\2\u0243\u0241\3\2\2\2\u0243"+
		"\u0244\3\2\2\2\u0244K\3\2\2\2\u0245\u0243\3\2\2\2\u0246\u0248\7o\2\2\u0247"+
		"\u0246\3\2\2\2\u0247\u0248\3\2\2\2\u0248\u0249\3\2\2\2\u0249\u024a\5N"+
		"(\2\u024aM\3\2\2\2\u024b\u024e\5P)\2\u024c\u024d\t\5\2\2\u024d\u024f\5"+
		"P)\2\u024e\u024c\3\2\2\2\u024e\u024f\3\2\2\2\u024fO\3\2\2\2\u0250\u0255"+
		"\5R*\2\u0251\u0252\7/\2\2\u0252\u0254\5R*\2\u0253\u0251\3\2\2\2\u0254"+
		"\u0257\3\2\2\2\u0255\u0253\3\2\2\2\u0255\u0256\3\2\2\2\u0256Q\3\2\2\2"+
		"\u0257\u0255\3\2\2\2\u0258\u025b\5T+\2\u0259\u025a\7p\2\2\u025a\u025c"+
		"\5T+\2\u025b\u0259\3\2\2\2\u025b\u025c\3\2\2\2\u025cS\3\2\2\2\u025d\u0262"+
		"\5V,\2\u025e\u025f\t\6\2\2\u025f\u0261\5V,\2\u0260\u025e\3\2\2\2\u0261"+
		"\u0264\3\2\2\2\u0262\u0260\3\2\2\2\u0262\u0263\3\2\2\2\u0263U\3\2\2\2"+
		"\u0264\u0262\3\2\2\2\u0265\u026a\5X-\2\u0266\u0267\t\7\2\2\u0267\u0269"+
		"\5X-\2\u0268\u0266\3\2\2\2\u0269\u026c\3\2\2\2\u026a\u0268\3\2\2\2\u026a"+
		"\u026b\3\2\2\2\u026bW\3\2\2\2\u026c\u026a\3\2\2\2\u026d\u0271\5Z.\2\u026e"+
		"\u026f\7q\2\2\u026f\u0270\7r\2\2\u0270\u0272\5\u0086D\2\u0271\u026e\3"+
		"\2\2\2\u0271\u0272\3\2\2\2\u0272Y\3\2\2\2\u0273\u0277\5\\/\2\u0274\u0275"+
		"\7s\2\2\u0275\u0276\7W\2\2\u0276\u0278\5\u0086D\2\u0277\u0274\3\2\2\2"+
		"\u0277\u0278\3\2\2\2\u0278[\3\2\2\2\u0279\u027d\5^\60\2\u027a\u027b\7"+
		"u\2\2\u027b\u027c\7W\2\2\u027c\u027e\5\u00a0Q\2\u027d\u027a\3\2\2\2\u027d"+
		"\u027e\3\2\2\2\u027e]\3\2\2\2\u027f\u0283\5`\61\2\u0280\u0281\7t\2\2\u0281"+
		"\u0282\7W\2\2\u0282\u0284\5\u00a0Q\2\u0283\u0280\3\2\2\2\u0283\u0284\3"+
		"\2\2\2\u0284_\3\2\2\2\u0285\u0287\t\6\2\2\u0286\u0285\3\2\2\2\u0287\u028a"+
		"\3\2\2\2\u0288\u0286\3\2\2\2\u0288\u0289\3\2\2\2\u0289\u028b\3\2\2\2\u028a"+
		"\u0288\3\2\2\2\u028b\u028c\5b\62\2\u028ca\3\2\2\2\u028d\u0292\5d\63\2"+
		"\u028e\u028f\7\65\2\2\u028f\u0291\5d\63\2\u0290\u028e\3\2\2\2\u0291\u0294"+
		"\3\2\2\2\u0292\u0290\3\2\2\2\u0292\u0293\3\2\2\2\u0293c\3\2\2\2\u0294"+
		"\u0292\3\2\2\2\u0295\u029d\5n8\2\u0296\u029c\5f\64\2\u0297\u029c\5j\66"+
		"\2\u0298\u029c\5l\67\2\u0299\u029c\5h\65\2\u029a\u029c\5|?\2\u029b\u0296"+
		"\3\2\2\2\u029b\u0297\3\2\2\2\u029b\u0298\3\2\2\2\u029b\u0299\3\2\2\2\u029b"+
		"\u029a\3\2\2\2\u029c\u029f\3\2\2\2\u029d\u029b\3\2\2\2\u029d\u029e\3\2"+
		"\2\2\u029ee\3\2\2\2\u029f\u029d\3\2\2\2\u02a0\u02a1\7\66\2\2\u02a1\u02a2"+
		"\7\66\2\2\u02a2\u02a3\5\36\20\2\u02a3\u02a4\7\67\2\2\u02a4\u02a5\7\67"+
		"\2\2\u02a5g\3\2\2\2\u02a6\u02a7\7\66\2\2\u02a7\u02a8\7\67\2\2\u02a8i\3"+
		"\2\2\2\u02a9\u02aa\7\66\2\2\u02aa\u02ab\5\36\20\2\u02ab\u02ac\7\67\2\2"+
		"\u02ack\3\2\2\2\u02ad\u02b5\78\2\2\u02ae\u02b6\5\u00aeX\2\u02af\u02b6"+
		"\5\u00acW\2\u02b0\u02b6\7\u0082\2\2\u02b1\u02b6\5r:\2\u02b2\u02b6\5p9"+
		"\2\u02b3\u02b6\5t;\2\u02b4\u02b6\5\u009eP\2\u02b5\u02ae\3\2\2\2\u02b5"+
		"\u02af\3\2\2\2\u02b5\u02b0\3\2\2\2\u02b5\u02b1\3\2\2\2\u02b5\u02b2\3\2"+
		"\2\2\u02b5\u02b3\3\2\2\2\u02b5\u02b4\3\2\2\2\u02b6m\3\2\2\2\u02b7\u02c4"+
		"\7z\2\2\u02b8\u02c4\7{\2\2\u02b9\u02c4\5\u00acW\2\u02ba\u02c4\5p9\2\u02bb"+
		"\u02c4\5r:\2\u02bc\u02c4\5t;\2\u02bd\u02c4\5\u0088E\2\u02be\u02c4\5z>"+
		"\2\u02bf\u02c4\5v<\2\u02c0\u02c4\5x=\2\u02c1\u02c4\5\u00a8U\2\u02c2\u02c4"+
		"\5\u0080A\2\u02c3\u02b7\3\2\2\2\u02c3\u02b8\3\2\2\2\u02c3\u02b9\3\2\2"+
		"\2\u02c3\u02ba\3\2\2\2\u02c3\u02bb\3\2\2\2\u02c3\u02bc\3\2\2\2\u02c3\u02bd"+
		"\3\2\2\2\u02c3\u02be\3\2\2\2\u02c3\u02bf\3\2\2\2\u02c3\u02c0\3\2\2\2\u02c3"+
		"\u02c1\3\2\2\2\u02c3\u02c2\3\2\2\2\u02c4o\3\2\2\2\u02c5\u02c8\7!\2\2\u02c6"+
		"\u02c7\7\u0082\2\2\u02c7\u02c9\7\f\2\2\u02c8\u02c6\3\2\2\2\u02c8\u02c9"+
		"\3\2\2\2\u02c9\u02ca\3\2\2\2\u02ca\u02cb\7\u0082\2\2\u02cbq\3\2\2\2\u02cc"+
		"\u02ce\7\35\2\2\u02cd\u02cf\5\36\20\2\u02ce\u02cd\3\2\2\2\u02ce\u02cf"+
		"\3\2\2\2\u02cf\u02d0\3\2\2\2\u02d0\u02d1\7\36\2\2\u02d1s\3\2\2\2\u02d2"+
		"\u02d3\79\2\2\u02d3u\3\2\2\2\u02d4\u02d5\7\t\2\2\u02d5\u02d6\7\37\2\2"+
		"\u02d6\u02d7\5\36\20\2\u02d7\u02d8\7 \2\2\u02d8w\3\2\2\2\u02d9\u02da\7"+
		"\n\2\2\u02da\u02db\7\37\2\2\u02db\u02dc\5\36\20\2\u02dc\u02dd\7 \2\2\u02dd"+
		"y\3\2\2\2\u02de\u02e2\7\u0082\2\2\u02df\u02e2\5\u00aeX\2\u02e0\u02e2\3"+
		"\2\2\2\u02e1\u02de\3\2\2\2\u02e1\u02df\3\2\2\2\u02e1\u02e0\3\2\2\2\u02e2"+
		"\u02e3\3\2\2\2\u02e3\u02e5\7\f\2\2\u02e4\u02e1\3\2\2\2\u02e4\u02e5\3\2"+
		"\2\2\u02e5\u02e8\3\2\2\2\u02e6\u02e9\5\u00a4S\2\u02e7\u02e9\5\u00aeX\2"+
		"\u02e8\u02e6\3\2\2\2\u02e8\u02e7\3\2\2\2\u02e9\u02ea\3\2\2\2\u02ea\u02eb"+
		"\5|?\2\u02eb{\3\2\2\2\u02ec\u02f3\7\35\2\2\u02ed\u02ef\5~@\2\u02ee\u02f0"+
		"\7\30\2\2\u02ef\u02ee\3\2\2\2\u02ef\u02f0\3\2\2\2\u02f0\u02f2\3\2\2\2"+
		"\u02f1\u02ed\3\2\2\2\u02f2\u02f5\3\2\2\2\u02f3\u02f1\3\2\2\2\u02f3\u02f4"+
		"\3\2\2\2\u02f4\u02f6\3\2\2\2\u02f5\u02f3\3\2\2\2\u02f6\u02f7\7\36\2\2"+
		"\u02f7}\3\2\2\2\u02f8\u02fb\5 \21\2\u02f9\u02fb\7:\2\2\u02fa\u02f8\3\2"+
		"\2\2\u02fa\u02f9\3\2\2\2\u02fb\177\3\2\2\2\u02fc\u02ff\5\u0082B\2\u02fd"+
		"\u02ff\5\u0084C\2\u02fe\u02fc\3\2\2\2\u02fe\u02fd\3\2\2\2\u02ff\u0081"+
		"\3\2\2\2\u0300\u0301\7\u0082\2\2\u0301\u0302\7;\2\2\u0302\u0303\7{\2\2"+
		"\u0303\u0083\3\2\2\2\u0304\u0305\7\34\2\2\u0305\u0307\7\35\2\2\u0306\u0308"+
		"\5\32\16\2\u0307\u0306\3\2\2\2\u0307\u0308\3\2\2\2\u0308\u0309\3\2\2\2"+
		"\u0309\u030c\7\36\2\2\u030a\u030b\7W\2\2\u030b\u030d\5\u0086D\2\u030c"+
		"\u030a\3\2\2\2\u030c\u030d\3\2\2\2\u030d\u030e\3\2\2\2\u030e\u030f\7\37"+
		"\2\2\u030f\u0310\5\36\20\2\u0310\u0311\7 \2\2\u0311\u0085\3\2\2\2\u0312"+
		"\u0313\7\35\2\2\u0313\u031b\7\36\2\2\u0314\u0318\5\u008aF\2\u0315\u0319"+
		"\7:\2\2\u0316\u0319\7#\2\2\u0317\u0319\7\60\2\2\u0318\u0315\3\2\2\2\u0318"+
		"\u0316\3\2\2\2\u0318\u0317\3\2\2\2\u0318\u0319\3\2\2\2\u0319\u031b\3\2"+
		"\2\2\u031a\u0312\3\2\2\2\u031a\u0314\3\2\2\2\u031b\u0087\3\2\2\2\u031c"+
		"\u0325\7\37\2\2\u031d\u0322\5\u00a6T\2\u031e\u031f\7\30\2\2\u031f\u0321"+
		"\5\u00a6T\2\u0320\u031e\3\2\2\2\u0321\u0324\3\2\2\2\u0322\u0320\3\2\2"+
		"\2\u0322\u0323\3\2\2\2\u0323\u0326\3\2\2\2\u0324\u0322\3\2\2\2\u0325\u031d"+
		"\3\2\2\2\u0325\u0326\3\2\2\2\u0326\u0327\3\2\2\2\u0327\u032d\7 \2\2\u0328"+
		"\u0329\7<\2\2\u0329\u032a\5\36\20\2\u032a\u032b\7=\2\2\u032b\u032d\3\2"+
		"\2\2\u032c\u031c\3\2\2\2\u032c\u0328\3\2\2\2\u032d\u0089\3\2\2\2\u032e"+
		"\u0332\7>\2\2\u032f\u0332\5\u008cG\2\u0330\u0332\5\u00a2R\2\u0331\u032e"+
		"\3\2\2\2\u0331\u032f\3\2\2\2\u0331\u0330\3\2\2\2\u0332\u008b\3\2\2\2\u0333"+
		"\u0334\t\b\2\2\u0334\u008d\3\2\2\2\u0335\u0336\7A\2\2\u0336\u008f\3\2"+
		"\2\2\u0337\u0338\7B\2\2\u0338\u0091\3\2\2\2\u0339\u033a\7C\2\2\u033a\u0093"+
		"\3\2\2\2\u033b\u033c\7D\2\2\u033c\u0095\3\2\2\2\u033d\u033e\7E\2\2\u033e"+
		"\u0097\3\2\2\2\u033f\u0340\7F\2\2\u0340\u0099\3\2\2\2\u0341\u0342\7G\2"+
		"\2\u0342\u009b\3\2\2\2\u0343\u0344\7H\2\2\u0344\u009d\3\2\2\2\u0345\u034e"+
		"\5\u008eH\2\u0346\u034e\5\u0090I\2\u0347\u034e\5\u0092J\2\u0348\u034e"+
		"\5\u0094K\2\u0349\u034e\5\u009aN\2\u034a\u034e\5\u009cO\2\u034b\u034e"+
		"\5\u0096L\2\u034c\u034e\5\u0098M\2\u034d\u0345\3\2\2\2\u034d\u0346\3\2"+
		"\2\2\u034d\u0347\3\2\2\2\u034d\u0348\3\2\2\2\u034d\u0349\3\2\2\2\u034d"+
		"\u034a\3\2\2\2\u034d\u034b\3\2\2\2\u034d\u034c\3\2\2\2\u034e\u009f\3\2"+
		"\2\2\u034f\u0351\5\u00a2R\2\u0350\u0352\7:\2\2\u0351\u0350\3\2\2\2\u0351"+
		"\u0352\3\2\2\2\u0352\u00a1\3\2\2\2\u0353\u035b\7I\2\2\u0354\u035b\7J\2"+
		"\2\u0355\u035b\7K\2\2\u0356\u035b\7L\2\2\u0357\u035b\7M\2\2\u0358\u035b"+
		"\5\u009eP\2\u0359\u035b\7z\2\2\u035a\u0353\3\2\2\2\u035a\u0354\3\2\2\2"+
		"\u035a\u0355\3\2\2\2\u035a\u0356\3\2\2\2\u035a\u0357\3\2\2\2\u035a\u0358"+
		"\3\2\2\2\u035a\u0359\3\2\2\2\u035b\u00a3\3\2\2\2\u035c\u035f\7\u0082\2"+
		"\2\u035d\u035f\5\u009eP\2\u035e\u035c\3\2\2\2\u035e\u035d\3\2\2\2\u035f"+
		"\u00a5\3\2\2\2\u0360\u0363\5 \21\2\u0361\u0363\7\u0082\2\2\u0362\u0360"+
		"\3\2\2\2\u0362\u0361\3\2\2\2\u0363\u0364\3\2\2\2\u0364\u0365\t\t\2\2\u0365"+
		"\u0366\5 \21\2\u0366\u00a7\3\2\2\2\u0367\u0369\7\66\2\2\u0368\u036a\5"+
		"\36\20\2\u0369\u0368\3\2\2\2\u0369\u036a\3\2\2\2\u036a\u036b\3\2\2\2\u036b"+
		"\u036c\7\67\2\2\u036c\u00a9\3\2\2\2\u036d\u036e\5\u00acW\2\u036e\u00ab"+
		"\3\2\2\2\u036f\u0370\7y\2\2\u0370\u00ad\3\2\2\2\u0371\u0372\t\n\2\2\u0372"+
		"\u00af\3\2\2\2^\u00b5\u00b9\u00cb\u00d1\u00d6\u00dc\u00f2\u00f7\u00ff"+
		"\u0109\u0112\u0115\u011c\u0123\u0125\u012b\u0130\u0135\u013c\u0143\u014a"+
		"\u0151\u015b\u015f\u0167\u0169\u0175\u017b\u017f\u0183\u018e\u0194\u01a3"+
		"\u01a9\u01ad\u01b1\u01b8\u01bf\u01c5\u01ca\u01cc\u01d0\u01d7\u01de\u01e7"+
		"\u01f3\u01fd\u0209\u020d\u0216\u021d\u023b\u0243\u0247\u024e\u0255\u025b"+
		"\u0262\u026a\u0271\u0277\u027d\u0283\u0288\u0292\u029b\u029d\u02b5\u02c3"+
		"\u02c8\u02ce\u02e1\u02e4\u02e8\u02ef\u02f3\u02fa\u02fe\u0307\u030c\u0318"+
		"\u031a\u0322\u0325\u032c\u0331\u034d\u0351\u035a\u035e\u0362\u0369";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}