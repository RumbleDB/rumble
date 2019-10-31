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
		Kfor=74, Klet=75, Kwhere=76, Kgroup=77, Kby=78, Korder=79, Kreturn=80, 
		Kif=81, Kin=82, Kas=83, Kat=84, Kallowing=85, Kempty=86, Kcount=87, Kstable=88, 
		Kascending=89, Kdescending=90, Ksome=91, Kevery=92, Ksatisfies=93, Kcollation=94, 
		Kgreatest=95, Kleast=96, Kswitch=97, Kcase=98, Ktry=99, Kcatch=100, Kdefault=101, 
		Kthen=102, Kelse=103, Ktypeswitch=104, Kor=105, Kand=106, Knot=107, Kto=108, 
		Kinstance=109, Kof=110, Ktreat=111, Kcast=112, Kcastable=113, Kversion=114, 
		Kjsoniq=115, Kjson=116, STRING=117, NullLiteral=118, Literal=119, NumericLiteral=120, 
		BooleanLiteral=121, IntegerLiteral=122, DecimalLiteral=123, DoubleLiteral=124, 
		WS=125, NCName=126, XQComment=127, ContentChar=128;
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
		RULE_keyWordHexBinary = 74, RULE_keyWordBase64Binary = 75, RULE_typesKeywords = 76, 
		RULE_singleType = 77, RULE_atomicType = 78, RULE_nCNameOrKeyWord = 79, 
		RULE_pairConstructor = 80, RULE_arrayConstructor = 81, RULE_uriLiteral = 82, 
		RULE_stringLiteral = 83, RULE_keyWords = 84;
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
		"keyWordHexBinary", "keyWordBase64Binary", "typesKeywords", "singleType", 
		"atomicType", "nCNameOrKeyWord", "pairConstructor", "arrayConstructor", 
		"uriLiteral", "stringLiteral", "keyWords"
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
		"'dayTimeDuration'", "'hexBinary'", "'base64Binary'", "'atomic'", "'string'", 
		"'integer'", "'decimal'", "'double'", "'for'", "'let'", "'where'", "'group'", 
		"'by'", "'order'", "'return'", "'if'", "'in'", "'as'", "'at'", "'allowing'", 
		"'empty'", "'count'", "'stable'", "'ascending'", "'descending'", "'some'", 
		"'every'", "'satisfies'", "'collation'", "'greatest'", "'least'", "'switch'", 
		"'case'", "'try'", "'catch'", "'default'", "'then'", "'else'", "'typeswitch'", 
		"'or'", "'and'", "'not'", "'to'", "'instance'", "'of'", "'treat'", "'cast'", 
		"'castable'", "'version'", "'jsoniq'", "'json-item'", null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "Kfor", "Klet", "Kwhere", "Kgroup", "Kby", "Korder", "Kreturn", 
		"Kif", "Kin", "Kas", "Kat", "Kallowing", "Kempty", "Kcount", "Kstable", 
		"Kascending", "Kdescending", "Ksome", "Kevery", "Ksatisfies", "Kcollation", 
		"Kgreatest", "Kleast", "Kswitch", "Kcase", "Ktry", "Kcatch", "Kdefault", 
		"Kthen", "Kelse", "Ktypeswitch", "Kor", "Kand", "Knot", "Kto", "Kinstance", 
		"Kof", "Ktreat", "Kcast", "Kcastable", "Kversion", "Kjsoniq", "Kjson", 
		"STRING", "NullLiteral", "Literal", "NumericLiteral", "BooleanLiteral", 
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
			setState(175);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(170);
				match(Kjsoniq);
				setState(171);
				match(Kversion);
				setState(172);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(173);
				match(T__0);
				}
				break;
			}
			setState(179);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(177);
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
				setState(178);
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
			setState(181);
			prolog();
			setState(182);
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
			setState(184);
			match(T__1);
			setState(185);
			match(T__2);
			setState(186);
			match(NCName);
			setState(187);
			match(T__3);
			setState(188);
			uriLiteral();
			setState(189);
			match(T__0);
			setState(190);
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
			setState(203);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(197);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(192);
						defaultCollationDecl();
						}
						break;
					case 2:
						{
						setState(193);
						orderingModeDecl();
						}
						break;
					case 3:
						{
						setState(194);
						emptyOrderDecl();
						}
						break;
					case 4:
						{
						setState(195);
						decimalFormatDecl();
						}
						break;
					case 5:
						{
						setState(196);
						moduleImport();
						}
						break;
					}
					setState(199);
					match(T__0);
					}
					} 
				}
				setState(205);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(208);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(206);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(207);
					varDecl();
					}
					break;
				}
				setState(210);
				match(T__0);
				}
				}
				setState(216);
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
			setState(217);
			match(T__4);
			setState(218);
			match(Kdefault);
			setState(219);
			match(Kcollation);
			setState(220);
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
			setState(222);
			match(T__4);
			setState(223);
			match(T__5);
			setState(224);
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
			setState(226);
			match(T__4);
			setState(227);
			match(Kdefault);
			setState(228);
			match(Korder);
			setState(229);
			match(Kempty);
			setState(230);
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
			setState(232);
			match(T__4);
			setState(241);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				{
				setState(233);
				match(T__8);
				setState(236);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(234);
					match(NCName);
					setState(235);
					match(T__9);
					}
					break;
				}
				setState(238);
				match(NCName);
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(239);
				match(Kdefault);
				setState(240);
				match(T__8);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) {
				{
				{
				setState(243);
				dfPropertyName();
				setState(244);
				match(T__3);
				setState(245);
				stringLiteral();
				}
				}
				setState(251);
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
			setState(252);
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
			setState(254);
			match(T__20);
			setState(255);
			match(T__1);
			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(256);
				match(T__2);
				setState(257);
				match(NCName);
				setState(258);
				match(T__3);
				}
			}

			setState(261);
			uriLiteral();
			setState(271);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(262);
				match(Kat);
				setState(263);
				uriLiteral();
				setState(268);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__21) {
					{
					{
					setState(264);
					match(T__21);
					setState(265);
					uriLiteral();
					}
					}
					setState(270);
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
			setState(273);
			match(T__4);
			setState(274);
			match(T__22);
			setState(275);
			varRef();
			setState(278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(276);
				match(Kas);
				setState(277);
				sequenceType();
				}
			}

			setState(287);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				{
				{
				setState(280);
				match(T__23);
				setState(281);
				exprSingle();
				}
				}
				break;
			case T__24:
				{
				{
				setState(282);
				match(T__24);
				setState(285);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__23) {
					{
					setState(283);
					match(T__23);
					setState(284);
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
			setState(289);
			match(T__4);
			setState(290);
			match(T__25);
			setState(293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(291);
				((FunctionDeclContext)_localctx).namespace = match(NCName);
				setState(292);
				match(T__9);
				}
				break;
			}
			setState(295);
			((FunctionDeclContext)_localctx).fn_name = match(NCName);
			setState(296);
			match(T__26);
			setState(298);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(297);
				paramList();
				}
			}

			setState(300);
			match(T__27);
			setState(303);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(301);
				match(Kas);
				setState(302);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(310);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				{
				setState(305);
				match(T__28);
				setState(306);
				((FunctionDeclContext)_localctx).fn_body = expr();
				setState(307);
				match(T__29);
				}
				break;
			case T__24:
				{
				setState(309);
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
			setState(312);
			param();
			setState(317);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(313);
				match(T__21);
				setState(314);
				param();
				}
				}
				setState(319);
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
			setState(320);
			match(T__30);
			setState(321);
			match(NCName);
			setState(324);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(322);
				match(Kas);
				setState(323);
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
			setState(326);
			exprSingle();
			setState(331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(327);
				match(T__21);
				setState(328);
				exprSingle();
				}
				}
				setState(333);
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
			setState(341);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(334);
				flowrExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(335);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(336);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(337);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(338);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(339);
				tryCatchExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(340);
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
			setState(345);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(343);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(344);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (Kfor - 74)) | (1L << (Klet - 74)) | (1L << (Kwhere - 74)) | (1L << (Kgroup - 74)) | (1L << (Korder - 74)) | (1L << (Kcount - 74)) | (1L << (Kstable - 74)))) != 0)) {
				{
				setState(353);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(347);
					forClause();
					}
					break;
				case Kwhere:
					{
					setState(348);
					whereClause();
					}
					break;
				case Klet:
					{
					setState(349);
					letClause();
					}
					break;
				case Kgroup:
					{
					setState(350);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(351);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(352);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(357);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(358);
			match(Kreturn);
			setState(359);
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
			setState(361);
			match(Kfor);
			setState(362);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(363);
				match(T__21);
				setState(364);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(369);
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
			setState(370);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(373);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(371);
				match(Kas);
				setState(372);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(375);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(376);
				match(Kempty);
				}
			}

			setState(381);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(379);
				match(Kat);
				setState(380);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(383);
			match(Kin);
			setState(384);
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
			setState(386);
			match(Klet);
			setState(387);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(388);
				match(T__21);
				setState(389);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(394);
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
			setState(395);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(396);
				match(Kas);
				setState(397);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(400);
			match(T__23);
			setState(401);
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
			setState(403);
			match(Kwhere);
			setState(404);
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
			setState(406);
			match(Kgroup);
			setState(407);
			match(Kby);
			setState(408);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(413);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(409);
				match(T__21);
				setState(410);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(415);
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
			setState(416);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(423);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23 || _la==Kas) {
				{
				setState(419);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(417);
					match(Kas);
					setState(418);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(421);
				((GroupByVarContext)_localctx).decl = match(T__23);
				setState(422);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(425);
				match(Kcollation);
				setState(426);
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
			setState(434);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(429);
				match(Korder);
				setState(430);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(431);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(432);
				match(Korder);
				setState(433);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(436);
			orderByExpr();
			setState(441);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(437);
				match(T__21);
				setState(438);
				orderByExpr();
				}
				}
				setState(443);
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
			setState(444);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(447);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(445);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(446);
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
			setState(454);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(449);
				match(Kempty);
				setState(452);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(450);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(451);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(456);
				match(Kcollation);
				setState(457);
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
			setState(460);
			match(Kcount);
			setState(461);
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
			setState(465);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(463);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(464);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(467);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(468);
				match(T__21);
				setState(469);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(474);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(475);
			match(Ksatisfies);
			setState(476);
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
			setState(478);
			varRef();
			setState(481);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(479);
				match(Kas);
				setState(480);
				sequenceType();
				}
			}

			setState(483);
			match(Kin);
			setState(484);
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
			setState(486);
			match(Kswitch);
			setState(487);
			match(T__26);
			setState(488);
			((SwitchExprContext)_localctx).cond = expr();
			setState(489);
			match(T__27);
			setState(491); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(490);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
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
			setState(501); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(499);
				match(Kcase);
				setState(500);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(503); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(505);
			match(Kreturn);
			setState(506);
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
			setState(508);
			match(Ktypeswitch);
			setState(509);
			match(T__26);
			setState(510);
			expr();
			setState(511);
			match(T__27);
			setState(513); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(512);
				caseClause();
				}
				}
				setState(515); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(517);
			match(Kdefault);
			setState(519);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(518);
				varRef();
				}
			}

			setState(521);
			match(Kreturn);
			setState(522);
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
			setState(524);
			match(Kcase);
			setState(528);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(525);
				varRef();
				setState(526);
				match(Kas);
				}
			}

			setState(530);
			sequenceType();
			setState(535);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__31) {
				{
				{
				setState(531);
				match(T__31);
				setState(532);
				sequenceType();
				}
				}
				setState(537);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(538);
			match(Kreturn);
			setState(539);
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
			setState(541);
			match(Kif);
			setState(542);
			match(T__26);
			setState(543);
			((IfExprContext)_localctx).test_condition = expr();
			setState(544);
			match(T__27);
			setState(545);
			match(Kthen);
			setState(546);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(547);
			match(Kelse);
			setState(548);
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
			setState(550);
			match(Ktry);
			setState(551);
			match(T__28);
			setState(552);
			expr();
			setState(553);
			match(T__29);
			setState(554);
			match(Kcatch);
			setState(555);
			match(T__32);
			setState(556);
			match(T__28);
			setState(557);
			expr();
			setState(558);
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
			setState(560);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(565);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(561);
					match(Kor);
					setState(562);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(567);
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
			setState(568);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(573);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(569);
					match(Kand);
					setState(570);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(575);
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
			setState(577);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(576);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(579);
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
			setState(581);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(584);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) {
				{
				setState(582);
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
				setState(583);
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
			setState(586);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(591);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44) {
				{
				{
				setState(587);
				match(T__44);
				setState(588);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(593);
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
			setState(594);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(597);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(595);
				match(Kto);
				setState(596);
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
			setState(599);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(604);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(600);
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
					setState(601);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(606);
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
			setState(607);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(612);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) {
				{
				{
				setState(608);
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
				setState(609);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(614);
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
			setState(615);
			((InstanceOfExprContext)_localctx).main_expr = treatExpr();
			setState(619);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(616);
				match(Kinstance);
				setState(617);
				match(Kof);
				setState(618);
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
			setState(621);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(625);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(622);
				match(Ktreat);
				setState(623);
				match(Kas);
				setState(624);
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
			setState(627);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(631);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(628);
				match(Kcastable);
				setState(629);
				match(Kas);
				setState(630);
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
			setState(633);
			((CastExprContext)_localctx).main_expr = unaryExpr();
			setState(637);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(634);
				match(Kcast);
				setState(635);
				match(Kas);
				setState(636);
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
			setState(642);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45 || _la==T__46) {
				{
				{
				setState(639);
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
				setState(644);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(645);
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
			setState(647);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(652);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__50) {
				{
				{
				setState(648);
				match(T__50);
				setState(649);
				postFixExpr();
				}
				}
				setState(654);
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
			setState(655);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(663);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(661);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
					case 1:
						{
						setState(656);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(657);
						predicate();
						}
						break;
					case 3:
						{
						setState(658);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(659);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(660);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(665);
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
			setState(666);
			match(T__51);
			setState(667);
			match(T__51);
			setState(668);
			expr();
			setState(669);
			match(T__52);
			setState(670);
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
			setState(672);
			match(T__51);
			setState(673);
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
			setState(675);
			match(T__51);
			setState(676);
			expr();
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
		enterRule(_localctx, 106, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(679);
			match(T__53);
			setState(686);
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
				setState(680);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(681);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(682);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__26:
				{
				setState(683);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__30:
				{
				setState(684);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__54:
				{
				setState(685);
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
			setState(700);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(688);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(689);
				match(Literal);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(690);
				stringLiteral();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(691);
				varRef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(692);
				parenthesizedExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(693);
				contextItemExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(694);
				objectConstructor();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(695);
				functionCall();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(696);
				orderedExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(697);
				unorderedExpr();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(698);
				arrayConstructor();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(699);
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
			setState(702);
			match(T__30);
			setState(705);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				{
				setState(703);
				((VarRefContext)_localctx).ns = match(NCName);
				setState(704);
				match(T__9);
				}
				break;
			}
			setState(707);
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
			setState(709);
			match(T__26);
			setState(711);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__57) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (T__64 - 64)) | (1L << (T__65 - 64)) | (1L << (T__66 - 64)) | (1L << (T__67 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kjson - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				setState(710);
				expr();
				}
			}

			setState(713);
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
			setState(715);
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
			setState(717);
			match(T__6);
			setState(718);
			match(T__28);
			setState(719);
			expr();
			setState(720);
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
			setState(722);
			match(T__7);
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
			setState(733);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				{
				setState(730);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(727);
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
					setState(728);
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
				setState(732);
				match(T__9);
				}
				break;
			}
			setState(737);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case NCName:
				{
				setState(735);
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
				setState(736);
				((FunctionCallContext)_localctx).kw = keyWords();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(739);
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
			setState(741);
			match(T__26);
			setState(748);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__55) | (1L << T__57) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (T__64 - 64)) | (1L << (T__65 - 64)) | (1L << (T__66 - 64)) | (1L << (T__67 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kjson - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				{
				setState(742);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(744);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__21) {
					{
					setState(743);
					match(T__21);
					}
				}

				}
				}
				setState(750);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(751);
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
			setState(755);
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
				setState(753);
				exprSingle();
				}
				break;
			case T__55:
				enterOuterAlt(_localctx, 2);
				{
				setState(754);
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
			setState(759);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(757);
				namedFunctionRef();
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 2);
				{
				setState(758);
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
			setState(761);
			((NamedFunctionRefContext)_localctx).fn_name = match(NCName);
			setState(762);
			match(T__56);
			setState(763);
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
			setState(765);
			match(T__25);
			setState(766);
			match(T__26);
			setState(768);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(767);
				paramList();
				}
			}

			setState(770);
			match(T__27);
			setState(773);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(771);
				match(Kas);
				setState(772);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(775);
			match(T__28);
			setState(776);
			((InlineFunctionExprContext)_localctx).fn_body = expr();
			setState(777);
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
			setState(787);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
				enterOuterAlt(_localctx, 1);
				{
				setState(779);
				match(T__26);
				setState(780);
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
			case Kjson:
			case NullLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(781);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(785);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
				case 1:
					{
					setState(782);
					((SequenceTypeContext)_localctx).s56 = match(T__55);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s56);
					}
					break;
				case 2:
					{
					setState(783);
					((SequenceTypeContext)_localctx).s33 = match(T__32);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s33);
					}
					break;
				case 3:
					{
					setState(784);
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
			setState(805);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(789);
				match(T__28);
				setState(798);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__57) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (T__64 - 64)) | (1L << (T__65 - 64)) | (1L << (T__66 - 64)) | (1L << (T__67 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kjson - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
					{
					setState(790);
					pairConstructor();
					setState(795);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__21) {
						{
						{
						setState(791);
						match(T__21);
						setState(792);
						pairConstructor();
						}
						}
						setState(797);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(800);
				match(T__29);
				}
				break;
			case T__57:
				enterOuterAlt(_localctx, 2);
				{
				setState(801);
				((ObjectConstructorContext)_localctx).s58 = match(T__57);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s58);
				setState(802);
				expr();
				setState(803);
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
			setState(810);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__59:
				enterOuterAlt(_localctx, 1);
				{
				setState(807);
				match(T__59);
				}
				break;
			case T__60:
			case T__61:
			case Kjson:
				enterOuterAlt(_localctx, 2);
				{
				setState(808);
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
			case NullLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(809);
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
			setState(812);
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
			setState(814);
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
			setState(816);
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
			setState(818);
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
			setState(820);
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
			setState(822);
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
			setState(824);
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
		enterRule(_localctx, 152, RULE_typesKeywords);
		try {
			setState(832);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__62:
				enterOuterAlt(_localctx, 1);
				{
				setState(826);
				keyWordBoolean();
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 2);
				{
				setState(827);
				keyWordDuration();
				}
				break;
			case T__64:
				enterOuterAlt(_localctx, 3);
				{
				setState(828);
				keyWordYearMonthDuration();
				}
				break;
			case T__65:
				enterOuterAlt(_localctx, 4);
				{
				setState(829);
				keyWordDayTimeDuration();
				}
				break;
			case T__66:
				enterOuterAlt(_localctx, 5);
				{
				setState(830);
				keyWordHexBinary();
				}
				break;
			case T__67:
				enterOuterAlt(_localctx, 6);
				{
				setState(831);
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
		enterRule(_localctx, 154, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(834);
			((SingleTypeContext)_localctx).item = atomicType();
			setState(836);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(835);
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
		enterRule(_localctx, 156, RULE_atomicType);
		try {
			setState(845);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__68:
				enterOuterAlt(_localctx, 1);
				{
				setState(838);
				match(T__68);
				}
				break;
			case T__69:
				enterOuterAlt(_localctx, 2);
				{
				setState(839);
				match(T__69);
				}
				break;
			case T__70:
				enterOuterAlt(_localctx, 3);
				{
				setState(840);
				match(T__70);
				}
				break;
			case T__71:
				enterOuterAlt(_localctx, 4);
				{
				setState(841);
				match(T__71);
				}
				break;
			case T__72:
				enterOuterAlt(_localctx, 5);
				{
				setState(842);
				match(T__72);
				}
				break;
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
				enterOuterAlt(_localctx, 6);
				{
				setState(843);
				typesKeywords();
				}
				break;
			case NullLiteral:
				enterOuterAlt(_localctx, 7);
				{
				setState(844);
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
		enterRule(_localctx, 158, RULE_nCNameOrKeyWord);
		try {
			setState(849);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(847);
				match(NCName);
				}
				break;
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
				enterOuterAlt(_localctx, 2);
				{
				setState(848);
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
		enterRule(_localctx, 160, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(853);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(851);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(852);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(855);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==T__55) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(856);
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
		enterRule(_localctx, 162, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
			match(T__51);
			setState(860);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__57) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (T__64 - 64)) | (1L << (T__65 - 64)) | (1L << (T__66 - 64)) | (1L << (T__67 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kjson - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				setState(859);
				expr();
				}
			}

			setState(862);
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
		enterRule(_localctx, 164, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
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
		enterRule(_localctx, 166, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(866);
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
		enterRule(_localctx, 168, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
			_la = _input.LA(1);
			if ( !(((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (Kfor - 74)) | (1L << (Klet - 74)) | (1L << (Kwhere - 74)) | (1L << (Kgroup - 74)) | (1L << (Kby - 74)) | (1L << (Korder - 74)) | (1L << (Kreturn - 74)) | (1L << (Kif - 74)) | (1L << (Kin - 74)) | (1L << (Kas - 74)) | (1L << (Kat - 74)) | (1L << (Kallowing - 74)) | (1L << (Kempty - 74)) | (1L << (Kcount - 74)) | (1L << (Kstable - 74)) | (1L << (Kascending - 74)) | (1L << (Kdescending - 74)) | (1L << (Ksome - 74)) | (1L << (Kevery - 74)) | (1L << (Ksatisfies - 74)) | (1L << (Kcollation - 74)) | (1L << (Kgreatest - 74)) | (1L << (Kleast - 74)) | (1L << (Kswitch - 74)) | (1L << (Kcase - 74)) | (1L << (Ktry - 74)) | (1L << (Kcatch - 74)) | (1L << (Kdefault - 74)) | (1L << (Kthen - 74)) | (1L << (Kelse - 74)) | (1L << (Ktypeswitch - 74)) | (1L << (Kor - 74)) | (1L << (Kand - 74)) | (1L << (Knot - 74)) | (1L << (Kto - 74)) | (1L << (Kinstance - 74)) | (1L << (Kof - 74)) | (1L << (Ktreat - 74)) | (1L << (Kcast - 74)) | (1L << (Kcastable - 74)) | (1L << (Kversion - 74)) | (1L << (Kjsoniq - 74)) | (1L << (Kjson - 74)))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0082\u0369\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\3\2\3\2\3\2\3\2\3\2\5\2\u00b2\n\2\3\2\3\2\5\2\u00b6\n\2\3"+
		"\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\5\5\u00c8"+
		"\n\5\3\5\3\5\7\5\u00cc\n\5\f\5\16\5\u00cf\13\5\3\5\3\5\5\5\u00d3\n\5\3"+
		"\5\3\5\7\5\u00d7\n\5\f\5\16\5\u00da\13\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3"+
		"\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\5\t\u00ef\n\t\3\t\3\t\3"+
		"\t\5\t\u00f4\n\t\3\t\3\t\3\t\3\t\7\t\u00fa\n\t\f\t\16\t\u00fd\13\t\3\n"+
		"\3\n\3\13\3\13\3\13\3\13\3\13\5\13\u0106\n\13\3\13\3\13\3\13\3\13\3\13"+
		"\7\13\u010d\n\13\f\13\16\13\u0110\13\13\5\13\u0112\n\13\3\f\3\f\3\f\3"+
		"\f\3\f\5\f\u0119\n\f\3\f\3\f\3\f\3\f\3\f\5\f\u0120\n\f\5\f\u0122\n\f\3"+
		"\r\3\r\3\r\3\r\5\r\u0128\n\r\3\r\3\r\3\r\5\r\u012d\n\r\3\r\3\r\3\r\5\r"+
		"\u0132\n\r\3\r\3\r\3\r\3\r\3\r\5\r\u0139\n\r\3\16\3\16\3\16\7\16\u013e"+
		"\n\16\f\16\16\16\u0141\13\16\3\17\3\17\3\17\3\17\5\17\u0147\n\17\3\20"+
		"\3\20\3\20\7\20\u014c\n\20\f\20\16\20\u014f\13\20\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\5\21\u0158\n\21\3\22\3\22\5\22\u015c\n\22\3\22\3\22\3"+
		"\22\3\22\3\22\3\22\7\22\u0164\n\22\f\22\16\22\u0167\13\22\3\22\3\22\3"+
		"\22\3\23\3\23\3\23\3\23\7\23\u0170\n\23\f\23\16\23\u0173\13\23\3\24\3"+
		"\24\3\24\5\24\u0178\n\24\3\24\3\24\5\24\u017c\n\24\3\24\3\24\5\24\u0180"+
		"\n\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\7\25\u0189\n\25\f\25\16\25\u018c"+
		"\13\25\3\26\3\26\3\26\5\26\u0191\n\26\3\26\3\26\3\26\3\27\3\27\3\27\3"+
		"\30\3\30\3\30\3\30\3\30\7\30\u019e\n\30\f\30\16\30\u01a1\13\30\3\31\3"+
		"\31\3\31\5\31\u01a6\n\31\3\31\3\31\5\31\u01aa\n\31\3\31\3\31\5\31\u01ae"+
		"\n\31\3\32\3\32\3\32\3\32\3\32\5\32\u01b5\n\32\3\32\3\32\3\32\7\32\u01ba"+
		"\n\32\f\32\16\32\u01bd\13\32\3\33\3\33\3\33\5\33\u01c2\n\33\3\33\3\33"+
		"\3\33\5\33\u01c7\n\33\5\33\u01c9\n\33\3\33\3\33\5\33\u01cd\n\33\3\34\3"+
		"\34\3\34\3\35\3\35\5\35\u01d4\n\35\3\35\3\35\3\35\7\35\u01d9\n\35\f\35"+
		"\16\35\u01dc\13\35\3\35\3\35\3\35\3\36\3\36\3\36\5\36\u01e4\n\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\3\37\6\37\u01ee\n\37\r\37\16\37\u01ef\3"+
		"\37\3\37\3\37\3\37\3 \3 \6 \u01f8\n \r \16 \u01f9\3 \3 \3 \3!\3!\3!\3"+
		"!\3!\6!\u0204\n!\r!\16!\u0205\3!\3!\5!\u020a\n!\3!\3!\3!\3\"\3\"\3\"\3"+
		"\"\5\"\u0213\n\"\3\"\3\"\3\"\7\"\u0218\n\"\f\"\16\"\u021b\13\"\3\"\3\""+
		"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3"+
		"%\7%\u0236\n%\f%\16%\u0239\13%\3&\3&\3&\7&\u023e\n&\f&\16&\u0241\13&\3"+
		"\'\5\'\u0244\n\'\3\'\3\'\3(\3(\3(\5(\u024b\n(\3)\3)\3)\7)\u0250\n)\f)"+
		"\16)\u0253\13)\3*\3*\3*\5*\u0258\n*\3+\3+\3+\7+\u025d\n+\f+\16+\u0260"+
		"\13+\3,\3,\3,\7,\u0265\n,\f,\16,\u0268\13,\3-\3-\3-\3-\5-\u026e\n-\3."+
		"\3.\3.\3.\5.\u0274\n.\3/\3/\3/\3/\5/\u027a\n/\3\60\3\60\3\60\3\60\5\60"+
		"\u0280\n\60\3\61\7\61\u0283\n\61\f\61\16\61\u0286\13\61\3\61\3\61\3\62"+
		"\3\62\3\62\7\62\u028d\n\62\f\62\16\62\u0290\13\62\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\7\63\u0298\n\63\f\63\16\63\u029b\13\63\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\67\3\67\3\67\3\67\3\67"+
		"\3\67\3\67\5\67\u02b1\n\67\38\38\38\38\38\38\38\38\38\38\38\38\58\u02bf"+
		"\n8\39\39\39\59\u02c4\n9\39\39\3:\3:\5:\u02ca\n:\3:\3:\3;\3;\3<\3<\3<"+
		"\3<\3<\3=\3=\3=\3=\3=\3>\3>\3>\5>\u02dd\n>\3>\5>\u02e0\n>\3>\3>\5>\u02e4"+
		"\n>\3>\3>\3?\3?\3?\5?\u02eb\n?\7?\u02ed\n?\f?\16?\u02f0\13?\3?\3?\3@\3"+
		"@\5@\u02f6\n@\3A\3A\5A\u02fa\nA\3B\3B\3B\3B\3C\3C\3C\5C\u0303\nC\3C\3"+
		"C\3C\5C\u0308\nC\3C\3C\3C\3C\3D\3D\3D\3D\3D\3D\5D\u0314\nD\5D\u0316\n"+
		"D\3E\3E\3E\3E\7E\u031c\nE\fE\16E\u031f\13E\5E\u0321\nE\3E\3E\3E\3E\3E"+
		"\5E\u0328\nE\3F\3F\3F\5F\u032d\nF\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L"+
		"\3M\3M\3N\3N\3N\3N\3N\3N\5N\u0343\nN\3O\3O\5O\u0347\nO\3P\3P\3P\3P\3P"+
		"\3P\3P\5P\u0350\nP\3Q\3Q\5Q\u0354\nQ\3R\3R\5R\u0358\nR\3R\3R\3R\3S\3S"+
		"\5S\u035f\nS\3S\3S\3T\3T\3U\3U\3V\3V\3V\2\2W\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|"+
		"~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096"+
		"\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\2\13\3\2"+
		"\t\n\3\2ab\3\2\r\26\4\2\6\6$.\3\2\60\61\4\2##\62\64\4\2?@vv\4\2\f\f::"+
		"\3\2Lv\2\u039a\2\u00b1\3\2\2\2\4\u00b7\3\2\2\2\6\u00ba\3\2\2\2\b\u00cd"+
		"\3\2\2\2\n\u00db\3\2\2\2\f\u00e0\3\2\2\2\16\u00e4\3\2\2\2\20\u00ea\3\2"+
		"\2\2\22\u00fe\3\2\2\2\24\u0100\3\2\2\2\26\u0113\3\2\2\2\30\u0123\3\2\2"+
		"\2\32\u013a\3\2\2\2\34\u0142\3\2\2\2\36\u0148\3\2\2\2 \u0157\3\2\2\2\""+
		"\u015b\3\2\2\2$\u016b\3\2\2\2&\u0174\3\2\2\2(\u0184\3\2\2\2*\u018d\3\2"+
		"\2\2,\u0195\3\2\2\2.\u0198\3\2\2\2\60\u01a2\3\2\2\2\62\u01b4\3\2\2\2\64"+
		"\u01be\3\2\2\2\66\u01ce\3\2\2\28\u01d3\3\2\2\2:\u01e0\3\2\2\2<\u01e8\3"+
		"\2\2\2>\u01f7\3\2\2\2@\u01fe\3\2\2\2B\u020e\3\2\2\2D\u021f\3\2\2\2F\u0228"+
		"\3\2\2\2H\u0232\3\2\2\2J\u023a\3\2\2\2L\u0243\3\2\2\2N\u0247\3\2\2\2P"+
		"\u024c\3\2\2\2R\u0254\3\2\2\2T\u0259\3\2\2\2V\u0261\3\2\2\2X\u0269\3\2"+
		"\2\2Z\u026f\3\2\2\2\\\u0275\3\2\2\2^\u027b\3\2\2\2`\u0284\3\2\2\2b\u0289"+
		"\3\2\2\2d\u0291\3\2\2\2f\u029c\3\2\2\2h\u02a2\3\2\2\2j\u02a5\3\2\2\2l"+
		"\u02a9\3\2\2\2n\u02be\3\2\2\2p\u02c0\3\2\2\2r\u02c7\3\2\2\2t\u02cd\3\2"+
		"\2\2v\u02cf\3\2\2\2x\u02d4\3\2\2\2z\u02df\3\2\2\2|\u02e7\3\2\2\2~\u02f5"+
		"\3\2\2\2\u0080\u02f9\3\2\2\2\u0082\u02fb\3\2\2\2\u0084\u02ff\3\2\2\2\u0086"+
		"\u0315\3\2\2\2\u0088\u0327\3\2\2\2\u008a\u032c\3\2\2\2\u008c\u032e\3\2"+
		"\2\2\u008e\u0330\3\2\2\2\u0090\u0332\3\2\2\2\u0092\u0334\3\2\2\2\u0094"+
		"\u0336\3\2\2\2\u0096\u0338\3\2\2\2\u0098\u033a\3\2\2\2\u009a\u0342\3\2"+
		"\2\2\u009c\u0344\3\2\2\2\u009e\u034f\3\2\2\2\u00a0\u0353\3\2\2\2\u00a2"+
		"\u0357\3\2\2\2\u00a4\u035c\3\2\2\2\u00a6\u0362\3\2\2\2\u00a8\u0364\3\2"+
		"\2\2\u00aa\u0366\3\2\2\2\u00ac\u00ad\7u\2\2\u00ad\u00ae\7t\2\2\u00ae\u00af"+
		"\5\u00a8U\2\u00af\u00b0\7\3\2\2\u00b0\u00b2\3\2\2\2\u00b1\u00ac\3\2\2"+
		"\2\u00b1\u00b2\3\2\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b6\5\6\4\2\u00b4\u00b6"+
		"\5\4\3\2\u00b5\u00b3\3\2\2\2\u00b5\u00b4\3\2\2\2\u00b6\3\3\2\2\2\u00b7"+
		"\u00b8\5\b\5\2\u00b8\u00b9\5\36\20\2\u00b9\5\3\2\2\2\u00ba\u00bb\7\4\2"+
		"\2\u00bb\u00bc\7\5\2\2\u00bc\u00bd\7\u0080\2\2\u00bd\u00be\7\6\2\2\u00be"+
		"\u00bf\5\u00a6T\2\u00bf\u00c0\7\3\2\2\u00c0\u00c1\5\b\5\2\u00c1\7\3\2"+
		"\2\2\u00c2\u00c8\5\n\6\2\u00c3\u00c8\5\f\7\2\u00c4\u00c8\5\16\b\2\u00c5"+
		"\u00c8\5\20\t\2\u00c6\u00c8\5\24\13\2\u00c7\u00c2\3\2\2\2\u00c7\u00c3"+
		"\3\2\2\2\u00c7\u00c4\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c7\u00c6\3\2\2\2\u00c8"+
		"\u00c9\3\2\2\2\u00c9\u00ca\7\3\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00c7\3\2"+
		"\2\2\u00cc\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce"+
		"\u00d8\3\2\2\2\u00cf\u00cd\3\2\2\2\u00d0\u00d3\5\30\r\2\u00d1\u00d3\5"+
		"\26\f\2\u00d2\u00d0\3\2\2\2\u00d2\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4"+
		"\u00d5\7\3\2\2\u00d5\u00d7\3\2\2\2\u00d6\u00d2\3\2\2\2\u00d7\u00da\3\2"+
		"\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\t\3\2\2\2\u00da\u00d8"+
		"\3\2\2\2\u00db\u00dc\7\7\2\2\u00dc\u00dd\7g\2\2\u00dd\u00de\7`\2\2\u00de"+
		"\u00df\5\u00a6T\2\u00df\13\3\2\2\2\u00e0\u00e1\7\7\2\2\u00e1\u00e2\7\b"+
		"\2\2\u00e2\u00e3\t\2\2\2\u00e3\r\3\2\2\2\u00e4\u00e5\7\7\2\2\u00e5\u00e6"+
		"\7g\2\2\u00e6\u00e7\7Q\2\2\u00e7\u00e8\7X\2\2\u00e8\u00e9\t\3\2\2\u00e9"+
		"\17\3\2\2\2\u00ea\u00f3\7\7\2\2\u00eb\u00ee\7\13\2\2\u00ec\u00ed\7\u0080"+
		"\2\2\u00ed\u00ef\7\f\2\2\u00ee\u00ec\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef"+
		"\u00f0\3\2\2\2\u00f0\u00f4\7\u0080\2\2\u00f1\u00f2\7g\2\2\u00f2\u00f4"+
		"\7\13\2\2\u00f3\u00eb\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f4\u00fb\3\2\2\2"+
		"\u00f5\u00f6\5\22\n\2\u00f6\u00f7\7\6\2\2\u00f7\u00f8\5\u00a8U\2\u00f8"+
		"\u00fa\3\2\2\2\u00f9\u00f5\3\2\2\2\u00fa\u00fd\3\2\2\2\u00fb\u00f9\3\2"+
		"\2\2\u00fb\u00fc\3\2\2\2\u00fc\21\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fe\u00ff"+
		"\t\4\2\2\u00ff\23\3\2\2\2\u0100\u0101\7\27\2\2\u0101\u0105\7\4\2\2\u0102"+
		"\u0103\7\5\2\2\u0103\u0104\7\u0080\2\2\u0104\u0106\7\6\2\2\u0105\u0102"+
		"\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u0107\3\2\2\2\u0107\u0111\5\u00a6T"+
		"\2\u0108\u0109\7V\2\2\u0109\u010e\5\u00a6T\2\u010a\u010b\7\30\2\2\u010b"+
		"\u010d\5\u00a6T\2\u010c\u010a\3\2\2\2\u010d\u0110\3\2\2\2\u010e\u010c"+
		"\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0112\3\2\2\2\u0110\u010e\3\2\2\2\u0111"+
		"\u0108\3\2\2\2\u0111\u0112\3\2\2\2\u0112\25\3\2\2\2\u0113\u0114\7\7\2"+
		"\2\u0114\u0115\7\31\2\2\u0115\u0118\5p9\2\u0116\u0117\7U\2\2\u0117\u0119"+
		"\5\u0086D\2\u0118\u0116\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u0121\3\2\2"+
		"\2\u011a\u011b\7\32\2\2\u011b\u0122\5 \21\2\u011c\u011f\7\33\2\2\u011d"+
		"\u011e\7\32\2\2\u011e\u0120\5 \21\2\u011f\u011d\3\2\2\2\u011f\u0120\3"+
		"\2\2\2\u0120\u0122\3\2\2\2\u0121\u011a\3\2\2\2\u0121\u011c\3\2\2\2\u0122"+
		"\27\3\2\2\2\u0123\u0124\7\7\2\2\u0124\u0127\7\34\2\2\u0125\u0126\7\u0080"+
		"\2\2\u0126\u0128\7\f\2\2\u0127\u0125\3\2\2\2\u0127\u0128\3\2\2\2\u0128"+
		"\u0129\3\2\2\2\u0129\u012a\7\u0080\2\2\u012a\u012c\7\35\2\2\u012b\u012d"+
		"\5\32\16\2\u012c\u012b\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u012e\3\2\2\2"+
		"\u012e\u0131\7\36\2\2\u012f\u0130\7U\2\2\u0130\u0132\5\u0086D\2\u0131"+
		"\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0138\3\2\2\2\u0133\u0134\7\37"+
		"\2\2\u0134\u0135\5\36\20\2\u0135\u0136\7 \2\2\u0136\u0139\3\2\2\2\u0137"+
		"\u0139\7\33\2\2\u0138\u0133\3\2\2\2\u0138\u0137\3\2\2\2\u0139\31\3\2\2"+
		"\2\u013a\u013f\5\34\17\2\u013b\u013c\7\30\2\2\u013c\u013e\5\34\17\2\u013d"+
		"\u013b\3\2\2\2\u013e\u0141\3\2\2\2\u013f\u013d\3\2\2\2\u013f\u0140\3\2"+
		"\2\2\u0140\33\3\2\2\2\u0141\u013f\3\2\2\2\u0142\u0143\7!\2\2\u0143\u0146"+
		"\7\u0080\2\2\u0144\u0145\7U\2\2\u0145\u0147\5\u0086D\2\u0146\u0144\3\2"+
		"\2\2\u0146\u0147\3\2\2\2\u0147\35\3\2\2\2\u0148\u014d\5 \21\2\u0149\u014a"+
		"\7\30\2\2\u014a\u014c\5 \21\2\u014b\u0149\3\2\2\2\u014c\u014f\3\2\2\2"+
		"\u014d\u014b\3\2\2\2\u014d\u014e\3\2\2\2\u014e\37\3\2\2\2\u014f\u014d"+
		"\3\2\2\2\u0150\u0158\5\"\22\2\u0151\u0158\58\35\2\u0152\u0158\5<\37\2"+
		"\u0153\u0158\5@!\2\u0154\u0158\5D#\2\u0155\u0158\5F$\2\u0156\u0158\5H"+
		"%\2\u0157\u0150\3\2\2\2\u0157\u0151\3\2\2\2\u0157\u0152\3\2\2\2\u0157"+
		"\u0153\3\2\2\2\u0157\u0154\3\2\2\2\u0157\u0155\3\2\2\2\u0157\u0156\3\2"+
		"\2\2\u0158!\3\2\2\2\u0159\u015c\5$\23\2\u015a\u015c\5(\25\2\u015b\u0159"+
		"\3\2\2\2\u015b\u015a\3\2\2\2\u015c\u0165\3\2\2\2\u015d\u0164\5$\23\2\u015e"+
		"\u0164\5,\27\2\u015f\u0164\5(\25\2\u0160\u0164\5.\30\2\u0161\u0164\5\62"+
		"\32\2\u0162\u0164\5\66\34\2\u0163\u015d\3\2\2\2\u0163\u015e\3\2\2\2\u0163"+
		"\u015f\3\2\2\2\u0163\u0160\3\2\2\2\u0163\u0161\3\2\2\2\u0163\u0162\3\2"+
		"\2\2\u0164\u0167\3\2\2\2\u0165\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166"+
		"\u0168\3\2\2\2\u0167\u0165\3\2\2\2\u0168\u0169\7R\2\2\u0169\u016a\5 \21"+
		"\2\u016a#\3\2\2\2\u016b\u016c\7L\2\2\u016c\u0171\5&\24\2\u016d\u016e\7"+
		"\30\2\2\u016e\u0170\5&\24\2\u016f\u016d\3\2\2\2\u0170\u0173\3\2\2\2\u0171"+
		"\u016f\3\2\2\2\u0171\u0172\3\2\2\2\u0172%\3\2\2\2\u0173\u0171\3\2\2\2"+
		"\u0174\u0177\5p9\2\u0175\u0176\7U\2\2\u0176\u0178\5\u0086D\2\u0177\u0175"+
		"\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u017b\3\2\2\2\u0179\u017a\7W\2\2\u017a"+
		"\u017c\7X\2\2\u017b\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u017f\3\2"+
		"\2\2\u017d\u017e\7V\2\2\u017e\u0180\5p9\2\u017f\u017d\3\2\2\2\u017f\u0180"+
		"\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0182\7T\2\2\u0182\u0183\5 \21\2\u0183"+
		"\'\3\2\2\2\u0184\u0185\7M\2\2\u0185\u018a\5*\26\2\u0186\u0187\7\30\2\2"+
		"\u0187\u0189\5*\26\2\u0188\u0186\3\2\2\2\u0189\u018c\3\2\2\2\u018a\u0188"+
		"\3\2\2\2\u018a\u018b\3\2\2\2\u018b)\3\2\2\2\u018c\u018a\3\2\2\2\u018d"+
		"\u0190\5p9\2\u018e\u018f\7U\2\2\u018f\u0191\5\u0086D\2\u0190\u018e\3\2"+
		"\2\2\u0190\u0191\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0193\7\32\2\2\u0193"+
		"\u0194\5 \21\2\u0194+\3\2\2\2\u0195\u0196\7N\2\2\u0196\u0197\5 \21\2\u0197"+
		"-\3\2\2\2\u0198\u0199\7O\2\2\u0199\u019a\7P\2\2\u019a\u019f\5\60\31\2"+
		"\u019b\u019c\7\30\2\2\u019c\u019e\5\60\31\2\u019d\u019b\3\2\2\2\u019e"+
		"\u01a1\3\2\2\2\u019f\u019d\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0/\3\2\2\2"+
		"\u01a1\u019f\3\2\2\2\u01a2\u01a9\5p9\2\u01a3\u01a4\7U\2\2\u01a4\u01a6"+
		"\5\u0086D\2\u01a5\u01a3\3\2\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01a7\3\2\2"+
		"\2\u01a7\u01a8\7\32\2\2\u01a8\u01aa\5 \21\2\u01a9\u01a5\3\2\2\2\u01a9"+
		"\u01aa\3\2\2\2\u01aa\u01ad\3\2\2\2\u01ab\u01ac\7`\2\2\u01ac\u01ae\5\u00a6"+
		"T\2\u01ad\u01ab\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae\61\3\2\2\2\u01af\u01b0"+
		"\7Q\2\2\u01b0\u01b5\7P\2\2\u01b1\u01b2\7Z\2\2\u01b2\u01b3\7Q\2\2\u01b3"+
		"\u01b5\7P\2\2\u01b4\u01af\3\2\2\2\u01b4\u01b1\3\2\2\2\u01b5\u01b6\3\2"+
		"\2\2\u01b6\u01bb\5\64\33\2\u01b7\u01b8\7\30\2\2\u01b8\u01ba\5\64\33\2"+
		"\u01b9\u01b7\3\2\2\2\u01ba\u01bd\3\2\2\2\u01bb\u01b9\3\2\2\2\u01bb\u01bc"+
		"\3\2\2\2\u01bc\63\3\2\2\2\u01bd\u01bb\3\2\2\2\u01be\u01c1\5 \21\2\u01bf"+
		"\u01c2\7[\2\2\u01c0\u01c2\7\\\2\2\u01c1\u01bf\3\2\2\2\u01c1\u01c0\3\2"+
		"\2\2\u01c1\u01c2\3\2\2\2\u01c2\u01c8\3\2\2\2\u01c3\u01c6\7X\2\2\u01c4"+
		"\u01c7\7a\2\2\u01c5\u01c7\7b\2\2\u01c6\u01c4\3\2\2\2\u01c6\u01c5\3\2\2"+
		"\2\u01c7\u01c9\3\2\2\2\u01c8\u01c3\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9\u01cc"+
		"\3\2\2\2\u01ca\u01cb\7`\2\2\u01cb\u01cd\5\u00a6T\2\u01cc\u01ca\3\2\2\2"+
		"\u01cc\u01cd\3\2\2\2\u01cd\65\3\2\2\2\u01ce\u01cf\7Y\2\2\u01cf\u01d0\5"+
		"p9\2\u01d0\67\3\2\2\2\u01d1\u01d4\7]\2\2\u01d2\u01d4\7^\2\2\u01d3\u01d1"+
		"\3\2\2\2\u01d3\u01d2\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5\u01da\5:\36\2\u01d6"+
		"\u01d7\7\30\2\2\u01d7\u01d9\5:\36\2\u01d8\u01d6\3\2\2\2\u01d9\u01dc\3"+
		"\2\2\2\u01da\u01d8\3\2\2\2\u01da\u01db\3\2\2\2\u01db\u01dd\3\2\2\2\u01dc"+
		"\u01da\3\2\2\2\u01dd\u01de\7_\2\2\u01de\u01df\5 \21\2\u01df9\3\2\2\2\u01e0"+
		"\u01e3\5p9\2\u01e1\u01e2\7U\2\2\u01e2\u01e4\5\u0086D\2\u01e3\u01e1\3\2"+
		"\2\2\u01e3\u01e4\3\2\2\2\u01e4\u01e5\3\2\2\2\u01e5\u01e6\7T\2\2\u01e6"+
		"\u01e7\5 \21\2\u01e7;\3\2\2\2\u01e8\u01e9\7c\2\2\u01e9\u01ea\7\35\2\2"+
		"\u01ea\u01eb\5\36\20\2\u01eb\u01ed\7\36\2\2\u01ec\u01ee\5> \2\u01ed\u01ec"+
		"\3\2\2\2\u01ee\u01ef\3\2\2\2\u01ef\u01ed\3\2\2\2\u01ef\u01f0\3\2\2\2\u01f0"+
		"\u01f1\3\2\2\2\u01f1\u01f2\7g\2\2\u01f2\u01f3\7R\2\2\u01f3\u01f4\5 \21"+
		"\2\u01f4=\3\2\2\2\u01f5\u01f6\7d\2\2\u01f6\u01f8\5 \21\2\u01f7\u01f5\3"+
		"\2\2\2\u01f8\u01f9\3\2\2\2\u01f9\u01f7\3\2\2\2\u01f9\u01fa\3\2\2\2\u01fa"+
		"\u01fb\3\2\2\2\u01fb\u01fc\7R\2\2\u01fc\u01fd\5 \21\2\u01fd?\3\2\2\2\u01fe"+
		"\u01ff\7j\2\2\u01ff\u0200\7\35\2\2\u0200\u0201\5\36\20\2\u0201\u0203\7"+
		"\36\2\2\u0202\u0204\5B\"\2\u0203\u0202\3\2\2\2\u0204\u0205\3\2\2\2\u0205"+
		"\u0203\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u0209\7g"+
		"\2\2\u0208\u020a\5p9\2\u0209\u0208\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020b"+
		"\3\2\2\2\u020b\u020c\7R\2\2\u020c\u020d\5 \21\2\u020dA\3\2\2\2\u020e\u0212"+
		"\7d\2\2\u020f\u0210\5p9\2\u0210\u0211\7U\2\2\u0211\u0213\3\2\2\2\u0212"+
		"\u020f\3\2\2\2\u0212\u0213\3\2\2\2\u0213\u0214\3\2\2\2\u0214\u0219\5\u0086"+
		"D\2\u0215\u0216\7\"\2\2\u0216\u0218\5\u0086D\2\u0217\u0215\3\2\2\2\u0218"+
		"\u021b\3\2\2\2\u0219\u0217\3\2\2\2\u0219\u021a\3\2\2\2\u021a\u021c\3\2"+
		"\2\2\u021b\u0219\3\2\2\2\u021c\u021d\7R\2\2\u021d\u021e\5 \21\2\u021e"+
		"C\3\2\2\2\u021f\u0220\7S\2\2\u0220\u0221\7\35\2\2\u0221\u0222\5\36\20"+
		"\2\u0222\u0223\7\36\2\2\u0223\u0224\7h\2\2\u0224\u0225\5 \21\2\u0225\u0226"+
		"\7i\2\2\u0226\u0227\5 \21\2\u0227E\3\2\2\2\u0228\u0229\7e\2\2\u0229\u022a"+
		"\7\37\2\2\u022a\u022b\5\36\20\2\u022b\u022c\7 \2\2\u022c\u022d\7f\2\2"+
		"\u022d\u022e\7#\2\2\u022e\u022f\7\37\2\2\u022f\u0230\5\36\20\2\u0230\u0231"+
		"\7 \2\2\u0231G\3\2\2\2\u0232\u0237\5J&\2\u0233\u0234\7k\2\2\u0234\u0236"+
		"\5J&\2\u0235\u0233\3\2\2\2\u0236\u0239\3\2\2\2\u0237\u0235\3\2\2\2\u0237"+
		"\u0238\3\2\2\2\u0238I\3\2\2\2\u0239\u0237\3\2\2\2\u023a\u023f\5L\'\2\u023b"+
		"\u023c\7l\2\2\u023c\u023e\5L\'\2\u023d\u023b\3\2\2\2\u023e\u0241\3\2\2"+
		"\2\u023f\u023d\3\2\2\2\u023f\u0240\3\2\2\2\u0240K\3\2\2\2\u0241\u023f"+
		"\3\2\2\2\u0242\u0244\7m\2\2\u0243\u0242\3\2\2\2\u0243\u0244\3\2\2\2\u0244"+
		"\u0245\3\2\2\2\u0245\u0246\5N(\2\u0246M\3\2\2\2\u0247\u024a\5P)\2\u0248"+
		"\u0249\t\5\2\2\u0249\u024b\5P)\2\u024a\u0248\3\2\2\2\u024a\u024b\3\2\2"+
		"\2\u024bO\3\2\2\2\u024c\u0251\5R*\2\u024d\u024e\7/\2\2\u024e\u0250\5R"+
		"*\2\u024f\u024d\3\2\2\2\u0250\u0253\3\2\2\2\u0251\u024f\3\2\2\2\u0251"+
		"\u0252\3\2\2\2\u0252Q\3\2\2\2\u0253\u0251\3\2\2\2\u0254\u0257\5T+\2\u0255"+
		"\u0256\7n\2\2\u0256\u0258\5T+\2\u0257\u0255\3\2\2\2\u0257\u0258\3\2\2"+
		"\2\u0258S\3\2\2\2\u0259\u025e\5V,\2\u025a\u025b\t\6\2\2\u025b\u025d\5"+
		"V,\2\u025c\u025a\3\2\2\2\u025d\u0260\3\2\2\2\u025e\u025c\3\2\2\2\u025e"+
		"\u025f\3\2\2\2\u025fU\3\2\2\2\u0260\u025e\3\2\2\2\u0261\u0266\5X-\2\u0262"+
		"\u0263\t\7\2\2\u0263\u0265\5X-\2\u0264\u0262\3\2\2\2\u0265\u0268\3\2\2"+
		"\2\u0266\u0264\3\2\2\2\u0266\u0267\3\2\2\2\u0267W\3\2\2\2\u0268\u0266"+
		"\3\2\2\2\u0269\u026d\5Z.\2\u026a\u026b\7o\2\2\u026b\u026c\7p\2\2\u026c"+
		"\u026e\5\u0086D\2\u026d\u026a\3\2\2\2\u026d\u026e\3\2\2\2\u026eY\3\2\2"+
		"\2\u026f\u0273\5\\/\2\u0270\u0271\7q\2\2\u0271\u0272\7U\2\2\u0272\u0274"+
		"\5\u0086D\2\u0273\u0270\3\2\2\2\u0273\u0274\3\2\2\2\u0274[\3\2\2\2\u0275"+
		"\u0279\5^\60\2\u0276\u0277\7s\2\2\u0277\u0278\7U\2\2\u0278\u027a\5\u009c"+
		"O\2\u0279\u0276\3\2\2\2\u0279\u027a\3\2\2\2\u027a]\3\2\2\2\u027b\u027f"+
		"\5`\61\2\u027c\u027d\7r\2\2\u027d\u027e\7U\2\2\u027e\u0280\5\u009cO\2"+
		"\u027f\u027c\3\2\2\2\u027f\u0280\3\2\2\2\u0280_\3\2\2\2\u0281\u0283\t"+
		"\6\2\2\u0282\u0281\3\2\2\2\u0283\u0286\3\2\2\2\u0284\u0282\3\2\2\2\u0284"+
		"\u0285\3\2\2\2\u0285\u0287\3\2\2\2\u0286\u0284\3\2\2\2\u0287\u0288\5b"+
		"\62\2\u0288a\3\2\2\2\u0289\u028e\5d\63\2\u028a\u028b\7\65\2\2\u028b\u028d"+
		"\5d\63\2\u028c\u028a\3\2\2\2\u028d\u0290\3\2\2\2\u028e\u028c\3\2\2\2\u028e"+
		"\u028f\3\2\2\2\u028fc\3\2\2\2\u0290\u028e\3\2\2\2\u0291\u0299\5n8\2\u0292"+
		"\u0298\5f\64\2\u0293\u0298\5j\66\2\u0294\u0298\5l\67\2\u0295\u0298\5h"+
		"\65\2\u0296\u0298\5|?\2\u0297\u0292\3\2\2\2\u0297\u0293\3\2\2\2\u0297"+
		"\u0294\3\2\2\2\u0297\u0295\3\2\2\2\u0297\u0296\3\2\2\2\u0298\u029b\3\2"+
		"\2\2\u0299\u0297\3\2\2\2\u0299\u029a\3\2\2\2\u029ae\3\2\2\2\u029b\u0299"+
		"\3\2\2\2\u029c\u029d\7\66\2\2\u029d\u029e\7\66\2\2\u029e\u029f\5\36\20"+
		"\2\u029f\u02a0\7\67\2\2\u02a0\u02a1\7\67\2\2\u02a1g\3\2\2\2\u02a2\u02a3"+
		"\7\66\2\2\u02a3\u02a4\7\67\2\2\u02a4i\3\2\2\2\u02a5\u02a6\7\66\2\2\u02a6"+
		"\u02a7\5\36\20\2\u02a7\u02a8\7\67\2\2\u02a8k\3\2\2\2\u02a9\u02b0\78\2"+
		"\2\u02aa\u02b1\5\u00aaV\2\u02ab\u02b1\5\u00a8U\2\u02ac\u02b1\7\u0080\2"+
		"\2\u02ad\u02b1\5r:\2\u02ae\u02b1\5p9\2\u02af\u02b1\5t;\2\u02b0\u02aa\3"+
		"\2\2\2\u02b0\u02ab\3\2\2\2\u02b0\u02ac\3\2\2\2\u02b0\u02ad\3\2\2\2\u02b0"+
		"\u02ae\3\2\2\2\u02b0\u02af\3\2\2\2\u02b1m\3\2\2\2\u02b2\u02bf\7x\2\2\u02b3"+
		"\u02bf\7y\2\2\u02b4\u02bf\5\u00a8U\2\u02b5\u02bf\5p9\2\u02b6\u02bf\5r"+
		":\2\u02b7\u02bf\5t;\2\u02b8\u02bf\5\u0088E\2\u02b9\u02bf\5z>\2\u02ba\u02bf"+
		"\5v<\2\u02bb\u02bf\5x=\2\u02bc\u02bf\5\u00a4S\2\u02bd\u02bf\5\u0080A\2"+
		"\u02be\u02b2\3\2\2\2\u02be\u02b3\3\2\2\2\u02be\u02b4\3\2\2\2\u02be\u02b5"+
		"\3\2\2\2\u02be\u02b6\3\2\2\2\u02be\u02b7\3\2\2\2\u02be\u02b8\3\2\2\2\u02be"+
		"\u02b9\3\2\2\2\u02be\u02ba\3\2\2\2\u02be\u02bb\3\2\2\2\u02be\u02bc\3\2"+
		"\2\2\u02be\u02bd\3\2\2\2\u02bfo\3\2\2\2\u02c0\u02c3\7!\2\2\u02c1\u02c2"+
		"\7\u0080\2\2\u02c2\u02c4\7\f\2\2\u02c3\u02c1\3\2\2\2\u02c3\u02c4\3\2\2"+
		"\2\u02c4\u02c5\3\2\2\2\u02c5\u02c6\7\u0080\2\2\u02c6q\3\2\2\2\u02c7\u02c9"+
		"\7\35\2\2\u02c8\u02ca\5\36\20\2\u02c9\u02c8\3\2\2\2\u02c9\u02ca\3\2\2"+
		"\2\u02ca\u02cb\3\2\2\2\u02cb\u02cc\7\36\2\2\u02ccs\3\2\2\2\u02cd\u02ce"+
		"\79\2\2\u02ceu\3\2\2\2\u02cf\u02d0\7\t\2\2\u02d0\u02d1\7\37\2\2\u02d1"+
		"\u02d2\5\36\20\2\u02d2\u02d3\7 \2\2\u02d3w\3\2\2\2\u02d4\u02d5\7\n\2\2"+
		"\u02d5\u02d6\7\37\2\2\u02d6\u02d7\5\36\20\2\u02d7\u02d8\7 \2\2\u02d8y"+
		"\3\2\2\2\u02d9\u02dd\7\u0080\2\2\u02da\u02dd\5\u00aaV\2\u02db\u02dd\3"+
		"\2\2\2\u02dc\u02d9\3\2\2\2\u02dc\u02da\3\2\2\2\u02dc\u02db\3\2\2\2\u02dd"+
		"\u02de\3\2\2\2\u02de\u02e0\7\f\2\2\u02df\u02dc\3\2\2\2\u02df\u02e0\3\2"+
		"\2\2\u02e0\u02e3\3\2\2\2\u02e1\u02e4\5\u00a0Q\2\u02e2\u02e4\5\u00aaV\2"+
		"\u02e3\u02e1\3\2\2\2\u02e3\u02e2\3\2\2\2\u02e4\u02e5\3\2\2\2\u02e5\u02e6"+
		"\5|?\2\u02e6{\3\2\2\2\u02e7\u02ee\7\35\2\2\u02e8\u02ea\5~@\2\u02e9\u02eb"+
		"\7\30\2\2\u02ea\u02e9\3\2\2\2\u02ea\u02eb\3\2\2\2\u02eb\u02ed\3\2\2\2"+
		"\u02ec\u02e8\3\2\2\2\u02ed\u02f0\3\2\2\2\u02ee\u02ec\3\2\2\2\u02ee\u02ef"+
		"\3\2\2\2\u02ef\u02f1\3\2\2\2\u02f0\u02ee\3\2\2\2\u02f1\u02f2\7\36\2\2"+
		"\u02f2}\3\2\2\2\u02f3\u02f6\5 \21\2\u02f4\u02f6\7:\2\2\u02f5\u02f3\3\2"+
		"\2\2\u02f5\u02f4\3\2\2\2\u02f6\177\3\2\2\2\u02f7\u02fa\5\u0082B\2\u02f8"+
		"\u02fa\5\u0084C\2\u02f9\u02f7\3\2\2\2\u02f9\u02f8\3\2\2\2\u02fa\u0081"+
		"\3\2\2\2\u02fb\u02fc\7\u0080\2\2\u02fc\u02fd\7;\2\2\u02fd\u02fe\7y\2\2"+
		"\u02fe\u0083\3\2\2\2\u02ff\u0300\7\34\2\2\u0300\u0302\7\35\2\2\u0301\u0303"+
		"\5\32\16\2\u0302\u0301\3\2\2\2\u0302\u0303\3\2\2\2\u0303\u0304\3\2\2\2"+
		"\u0304\u0307\7\36\2\2\u0305\u0306\7U\2\2\u0306\u0308\5\u0086D\2\u0307"+
		"\u0305\3\2\2\2\u0307\u0308\3\2\2\2\u0308\u0309\3\2\2\2\u0309\u030a\7\37"+
		"\2\2\u030a\u030b\5\36\20\2\u030b\u030c\7 \2\2\u030c\u0085\3\2\2\2\u030d"+
		"\u030e\7\35\2\2\u030e\u0316\7\36\2\2\u030f\u0313\5\u008aF\2\u0310\u0314"+
		"\7:\2\2\u0311\u0314\7#\2\2\u0312\u0314\7\60\2\2\u0313\u0310\3\2\2\2\u0313"+
		"\u0311\3\2\2\2\u0313\u0312\3\2\2\2\u0313\u0314\3\2\2\2\u0314\u0316\3\2"+
		"\2\2\u0315\u030d\3\2\2\2\u0315\u030f\3\2\2\2\u0316\u0087\3\2\2\2\u0317"+
		"\u0320\7\37\2\2\u0318\u031d\5\u00a2R\2\u0319\u031a\7\30\2\2\u031a\u031c"+
		"\5\u00a2R\2\u031b\u0319\3\2\2\2\u031c\u031f\3\2\2\2\u031d\u031b\3\2\2"+
		"\2\u031d\u031e\3\2\2\2\u031e\u0321\3\2\2\2\u031f\u031d\3\2\2\2\u0320\u0318"+
		"\3\2\2\2\u0320\u0321\3\2\2\2\u0321\u0322\3\2\2\2\u0322\u0328\7 \2\2\u0323"+
		"\u0324\7<\2\2\u0324\u0325\5\36\20\2\u0325\u0326\7=\2\2\u0326\u0328\3\2"+
		"\2\2\u0327\u0317\3\2\2\2\u0327\u0323\3\2\2\2\u0328\u0089\3\2\2\2\u0329"+
		"\u032d\7>\2\2\u032a\u032d\5\u008cG\2\u032b\u032d\5\u009eP\2\u032c\u0329"+
		"\3\2\2\2\u032c\u032a\3\2\2\2\u032c\u032b\3\2\2\2\u032d\u008b\3\2\2\2\u032e"+
		"\u032f\t\b\2\2\u032f\u008d\3\2\2\2\u0330\u0331\7A\2\2\u0331\u008f\3\2"+
		"\2\2\u0332\u0333\7B\2\2\u0333\u0091\3\2\2\2\u0334\u0335\7C\2\2\u0335\u0093"+
		"\3\2\2\2\u0336\u0337\7D\2\2\u0337\u0095\3\2\2\2\u0338\u0339\7E\2\2\u0339"+
		"\u0097\3\2\2\2\u033a\u033b\7F\2\2\u033b\u0099\3\2\2\2\u033c\u0343\5\u008e"+
		"H\2\u033d\u0343\5\u0090I\2\u033e\u0343\5\u0092J\2\u033f\u0343\5\u0094"+
		"K\2\u0340\u0343\5\u0096L\2\u0341\u0343\5\u0098M\2\u0342\u033c\3\2\2\2"+
		"\u0342\u033d\3\2\2\2\u0342\u033e\3\2\2\2\u0342\u033f\3\2\2\2\u0342\u0340"+
		"\3\2\2\2\u0342\u0341\3\2\2\2\u0343\u009b\3\2\2\2\u0344\u0346\5\u009eP"+
		"\2\u0345\u0347\7:\2\2\u0346\u0345\3\2\2\2\u0346\u0347\3\2\2\2\u0347\u009d"+
		"\3\2\2\2\u0348\u0350\7G\2\2\u0349\u0350\7H\2\2\u034a\u0350\7I\2\2\u034b"+
		"\u0350\7J\2\2\u034c\u0350\7K\2\2\u034d\u0350\5\u009aN\2\u034e\u0350\7"+
		"x\2\2\u034f\u0348\3\2\2\2\u034f\u0349\3\2\2\2\u034f\u034a\3\2\2\2\u034f"+
		"\u034b\3\2\2\2\u034f\u034c\3\2\2\2\u034f\u034d\3\2\2\2\u034f\u034e\3\2"+
		"\2\2\u0350\u009f\3\2\2\2\u0351\u0354\7\u0080\2\2\u0352\u0354\5\u009aN"+
		"\2\u0353\u0351\3\2\2\2\u0353\u0352\3\2\2\2\u0354\u00a1\3\2\2\2\u0355\u0358"+
		"\5 \21\2\u0356\u0358\7\u0080\2\2\u0357\u0355\3\2\2\2\u0357\u0356\3\2\2"+
		"\2\u0358\u0359\3\2\2\2\u0359\u035a\t\t\2\2\u035a\u035b\5 \21\2\u035b\u00a3"+
		"\3\2\2\2\u035c\u035e\7\66\2\2\u035d\u035f\5\36\20\2\u035e\u035d\3\2\2"+
		"\2\u035e\u035f\3\2\2\2\u035f\u0360\3\2\2\2\u0360\u0361\7\67\2\2\u0361"+
		"\u00a5\3\2\2\2\u0362\u0363\5\u00a8U\2\u0363\u00a7\3\2\2\2\u0364\u0365"+
		"\7w\2\2\u0365\u00a9\3\2\2\2\u0366\u0367\t\n\2\2\u0367\u00ab\3\2\2\2^\u00b1"+
		"\u00b5\u00c7\u00cd\u00d2\u00d8\u00ee\u00f3\u00fb\u0105\u010e\u0111\u0118"+
		"\u011f\u0121\u0127\u012c\u0131\u0138\u013f\u0146\u014d\u0157\u015b\u0163"+
		"\u0165\u0171\u0177\u017b\u017f\u018a\u0190\u019f\u01a5\u01a9\u01ad\u01b4"+
		"\u01bb\u01c1\u01c6\u01c8\u01cc\u01d3\u01da\u01e3\u01ef\u01f9\u0205\u0209"+
		"\u0212\u0219\u0237\u023f\u0243\u024a\u0251\u0257\u025e\u0266\u026d\u0273"+
		"\u0279\u027f\u0284\u028e\u0297\u0299\u02b0\u02be\u02c3\u02c9\u02dc\u02df"+
		"\u02e3\u02ea\u02ee\u02f5\u02f9\u0302\u0307\u0313\u0315\u031d\u0320\u0327"+
		"\u032c\u0342\u0346\u034f\u0353\u0357\u035e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}