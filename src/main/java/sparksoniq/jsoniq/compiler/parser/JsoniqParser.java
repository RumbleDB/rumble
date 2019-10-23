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
		RULE_varDecl = 10, RULE_functionDecl = 11, RULE_paramList = 12, RULE_expr = 13, 
		RULE_exprSingle = 14, RULE_flowrExpr = 15, RULE_forClause = 16, RULE_forVar = 17, 
		RULE_letClause = 18, RULE_letVar = 19, RULE_whereClause = 20, RULE_groupByClause = 21, 
		RULE_groupByVar = 22, RULE_orderByClause = 23, RULE_orderByExpr = 24, 
		RULE_countClause = 25, RULE_quantifiedExpr = 26, RULE_quantifiedExprVar = 27, 
		RULE_switchExpr = 28, RULE_switchCaseClause = 29, RULE_typeSwitchExpr = 30, 
		RULE_caseClause = 31, RULE_ifExpr = 32, RULE_tryCatchExpr = 33, RULE_orExpr = 34, 
		RULE_andExpr = 35, RULE_notExpr = 36, RULE_comparisonExpr = 37, RULE_stringConcatExpr = 38, 
		RULE_rangeExpr = 39, RULE_additiveExpr = 40, RULE_multiplicativeExpr = 41, 
		RULE_instanceOfExpr = 42, RULE_treatExpr = 43, RULE_castableExpr = 44, 
		RULE_castExpr = 45, RULE_unaryExpr = 46, RULE_simpleMapExpr = 47, RULE_postFixExpr = 48, 
		RULE_arrayLookup = 49, RULE_arrayUnboxing = 50, RULE_predicate = 51, RULE_objectLookup = 52, 
		RULE_primaryExpr = 53, RULE_varRef = 54, RULE_parenthesizedExpr = 55, 
		RULE_contextItemExpr = 56, RULE_orderedExpr = 57, RULE_unorderedExpr = 58, 
		RULE_functionCall = 59, RULE_argumentList = 60, RULE_argument = 61, RULE_sequenceType = 62, 
		RULE_objectConstructor = 63, RULE_itemType = 64, RULE_jSONItemTest = 65, 
		RULE_keyWordBoolean = 66, RULE_keyWordDuration = 67, RULE_keyWordYearMonthDuration = 68, 
		RULE_keyWordDayTimeDuration = 69, RULE_keyWordHexBinary = 70, RULE_keyWordBase64Binary = 71, 
		RULE_keyWordDateTime = 72, RULE_singleType = 73, RULE_atomicType = 74, 
		RULE_nCNameOrKeyWord = 75, RULE_pairConstructor = 76, RULE_arrayConstructor = 77, 
		RULE_uriLiteral = 78, RULE_stringLiteral = 79, RULE_keyWords = 80;
	public static final String[] ruleNames = {
		"module", "mainModule", "libraryModule", "prolog", "defaultCollationDecl", 
		"orderingModeDecl", "emptyOrderDecl", "decimalFormatDecl", "dfPropertyName", 
		"moduleImport", "varDecl", "functionDecl", "paramList", "expr", "exprSingle", 
		"flowrExpr", "forClause", "forVar", "letClause", "letVar", "whereClause", 
		"groupByClause", "groupByVar", "orderByClause", "orderByExpr", "countClause", 
		"quantifiedExpr", "quantifiedExprVar", "switchExpr", "switchCaseClause", 
		"typeSwitchExpr", "caseClause", "ifExpr", "tryCatchExpr", "orExpr", "andExpr", 
		"notExpr", "comparisonExpr", "stringConcatExpr", "rangeExpr", "additiveExpr", 
		"multiplicativeExpr", "instanceOfExpr", "treatExpr", "castableExpr", "castExpr", 
		"unaryExpr", "simpleMapExpr", "postFixExpr", "arrayLookup", "arrayUnboxing", 
		"predicate", "objectLookup", "primaryExpr", "varRef", "parenthesizedExpr", 
		"contextItemExpr", "orderedExpr", "unorderedExpr", "functionCall", "argumentList", 
		"argument", "sequenceType", "objectConstructor", "itemType", "jSONItemTest", 
		"keyWordBoolean", "keyWordDuration", "keyWordYearMonthDuration", "keyWordDayTimeDuration", 
		"keyWordHexBinary", "keyWordBase64Binary", "keyWordDateTime", "singleType", 
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
		"'idiv'", "'mod'", "'!'", "'['", "']'", "'.'", "'$$'", "'?'", "'{|'", 
		"'|}'", "'item'", "'object'", "'array'", "'boolean'", "'duration'", "'yearMonthDuration'", 
		"'dayTimeDuration'", "'hexBinary'", "'base64Binary'", "'dateTime'", "'atomic'", 
		"'string'", "'integer'", "'decimal'", "'double'", "'for'", "'let'", "'where'", 
		"'group'", "'by'", "'order'", "'return'", "'if'", "'in'", "'as'", "'at'", 
		"'allowing'", "'empty'", "'count'", "'stable'", "'ascending'", "'descending'", 
		"'some'", "'every'", "'satisfies'", "'collation'", "'greatest'", "'least'", 
		"'switch'", "'case'", "'try'", "'catch'", "'default'", "'then'", "'else'", 
		"'typeswitch'", "'or'", "'and'", "'not'", "'to'", "'instance'", "'of'", 
		"'treat'", "'cast'", "'castable'", "'version'", "'jsoniq'", "'json-item'", 
		null, "'null'"
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
			setState(167);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(162);
				match(Kjsoniq);
				setState(163);
				match(Kversion);
				setState(164);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(165);
				match(T__0);
				}
				break;
			}
			setState(171);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(169);
				libraryModule();
				}
				break;
			case T__4:
			case T__6:
			case T__7:
			case T__9:
			case T__20:
			case T__26:
			case T__28:
			case T__30:
			case T__45:
			case T__46:
			case T__51:
			case T__54:
			case T__56:
			case T__61:
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
				setState(170);
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
			setState(173);
			prolog();
			setState(174);
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
			setState(176);
			match(T__1);
			setState(177);
			match(T__2);
			setState(178);
			match(NCName);
			setState(179);
			match(T__3);
			setState(180);
			uriLiteral();
			setState(181);
			match(T__0);
			setState(182);
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
			setState(195);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(189);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(184);
						defaultCollationDecl();
						}
						break;
					case 2:
						{
						setState(185);
						orderingModeDecl();
						}
						break;
					case 3:
						{
						setState(186);
						emptyOrderDecl();
						}
						break;
					case 4:
						{
						setState(187);
						decimalFormatDecl();
						}
						break;
					case 5:
						{
						setState(188);
						moduleImport();
						}
						break;
					}
					setState(191);
					match(T__0);
					}
					} 
				}
				setState(197);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(206);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(200);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(198);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(199);
					varDecl();
					}
					break;
				}
				setState(202);
				match(T__0);
				}
				}
				setState(208);
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
			setState(209);
			match(T__4);
			setState(210);
			match(Kdefault);
			setState(211);
			match(Kcollation);
			setState(212);
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
			setState(214);
			match(T__4);
			setState(215);
			match(T__5);
			setState(216);
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
			setState(218);
			match(T__4);
			setState(219);
			match(Kdefault);
			setState(220);
			match(Korder);
			setState(221);
			match(Kempty);
			setState(222);
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
			setState(224);
			match(T__4);
			setState(233);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				{
				setState(225);
				match(T__8);
				setState(228);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(226);
					match(NCName);
					setState(227);
					match(T__9);
					}
					break;
				}
				setState(230);
				match(NCName);
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(231);
				match(Kdefault);
				setState(232);
				match(T__8);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) {
				{
				{
				setState(235);
				dfPropertyName();
				setState(236);
				match(T__3);
				setState(237);
				stringLiteral();
				}
				}
				setState(243);
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
			setState(244);
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
			setState(246);
			match(T__20);
			setState(247);
			match(T__1);
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(248);
				match(T__2);
				setState(249);
				match(NCName);
				setState(250);
				match(T__3);
				}
			}

			setState(253);
			uriLiteral();
			setState(263);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(254);
				match(Kat);
				setState(255);
				uriLiteral();
				setState(260);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__21) {
					{
					{
					setState(256);
					match(T__21);
					setState(257);
					uriLiteral();
					}
					}
					setState(262);
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
			setState(265);
			match(T__4);
			setState(266);
			match(T__22);
			setState(267);
			varRef();
			setState(270);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(268);
				match(Kas);
				setState(269);
				sequenceType();
				}
			}

			setState(279);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				{
				{
				setState(272);
				match(T__23);
				setState(273);
				exprSingle();
				}
				}
				break;
			case T__24:
				{
				{
				setState(274);
				match(T__24);
				setState(277);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__23) {
					{
					setState(275);
					match(T__23);
					setState(276);
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
		public List<TerminalNode> NCName() { return getTokens(JsoniqParser.NCName); }
		public TerminalNode NCName(int i) {
			return getToken(JsoniqParser.NCName, i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
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
			setState(281);
			match(T__4);
			setState(282);
			match(T__25);
			setState(285);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(283);
				match(NCName);
				setState(284);
				match(T__9);
				}
				break;
			}
			setState(287);
			match(NCName);
			setState(288);
			match(T__26);
			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(289);
				paramList();
				}
			}

			setState(292);
			match(T__27);
			setState(295);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(293);
				match(Kas);
				setState(294);
				sequenceType();
				}
			}

			setState(302);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				{
				setState(297);
				match(T__28);
				setState(298);
				expr();
				setState(299);
				match(T__29);
				}
				break;
			case T__24:
				{
				setState(301);
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
		public List<TerminalNode> NCName() { return getTokens(JsoniqParser.NCName); }
		public TerminalNode NCName(int i) {
			return getToken(JsoniqParser.NCName, i);
		}
		public List<TerminalNode> Kas() { return getTokens(JsoniqParser.Kas); }
		public TerminalNode Kas(int i) {
			return getToken(JsoniqParser.Kas, i);
		}
		public List<SequenceTypeContext> sequenceType() {
			return getRuleContexts(SequenceTypeContext.class);
		}
		public SequenceTypeContext sequenceType(int i) {
			return getRuleContext(SequenceTypeContext.class,i);
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
			setState(304);
			match(T__30);
			setState(305);
			match(NCName);
			setState(308);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(306);
				match(Kas);
				setState(307);
				sequenceType();
				}
			}

			setState(319);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(310);
				match(T__21);
				setState(311);
				match(T__30);
				setState(312);
				match(NCName);
				setState(315);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(313);
					match(Kas);
					setState(314);
					sequenceType();
					}
				}

				}
				}
				setState(321);
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
		enterRule(_localctx, 26, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			exprSingle();
			setState(327);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(323);
				match(T__21);
				setState(324);
				exprSingle();
				}
				}
				setState(329);
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
		enterRule(_localctx, 28, RULE_exprSingle);
		try {
			setState(337);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(330);
				flowrExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(331);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(332);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(333);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(334);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(335);
				tryCatchExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(336);
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
		enterRule(_localctx, 30, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(339);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(340);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(351);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (Kfor - 74)) | (1L << (Klet - 74)) | (1L << (Kwhere - 74)) | (1L << (Kgroup - 74)) | (1L << (Korder - 74)) | (1L << (Kcount - 74)) | (1L << (Kstable - 74)))) != 0)) {
				{
				setState(349);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(343);
					forClause();
					}
					break;
				case Kwhere:
					{
					setState(344);
					whereClause();
					}
					break;
				case Klet:
					{
					setState(345);
					letClause();
					}
					break;
				case Kgroup:
					{
					setState(346);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(347);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(348);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(353);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(354);
			match(Kreturn);
			setState(355);
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
		enterRule(_localctx, 32, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
			match(Kfor);
			setState(358);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(359);
				match(T__21);
				setState(360);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(365);
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
		enterRule(_localctx, 34, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(369);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(367);
				match(Kas);
				setState(368);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(373);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(371);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(372);
				match(Kempty);
				}
			}

			setState(377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(375);
				match(Kat);
				setState(376);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(379);
			match(Kin);
			setState(380);
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
		enterRule(_localctx, 36, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			match(Klet);
			setState(383);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(388);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(384);
				match(T__21);
				setState(385);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(390);
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
		enterRule(_localctx, 38, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(394);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(392);
				match(Kas);
				setState(393);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(396);
			match(T__23);
			setState(397);
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
		enterRule(_localctx, 40, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			match(Kwhere);
			setState(400);
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
		enterRule(_localctx, 42, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			match(Kgroup);
			setState(403);
			match(Kby);
			setState(404);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(409);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(405);
				match(T__21);
				setState(406);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(411);
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
		enterRule(_localctx, 44, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(412);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(419);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23 || _la==Kas) {
				{
				setState(415);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(413);
					match(Kas);
					setState(414);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(417);
				((GroupByVarContext)_localctx).decl = match(T__23);
				setState(418);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(423);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(421);
				match(Kcollation);
				setState(422);
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
		enterRule(_localctx, 46, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(430);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(425);
				match(Korder);
				setState(426);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(427);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(428);
				match(Korder);
				setState(429);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(432);
			orderByExpr();
			setState(437);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(433);
				match(T__21);
				setState(434);
				orderByExpr();
				}
				}
				setState(439);
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
		enterRule(_localctx, 48, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(440);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(443);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(441);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(442);
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
			setState(450);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(445);
				match(Kempty);
				setState(448);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(446);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(447);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(454);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(452);
				match(Kcollation);
				setState(453);
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
		enterRule(_localctx, 50, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(456);
			match(Kcount);
			setState(457);
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
		enterRule(_localctx, 52, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(459);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(460);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(463);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(468);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(464);
				match(T__21);
				setState(465);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(470);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(471);
			match(Ksatisfies);
			setState(472);
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
		enterRule(_localctx, 54, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(474);
			varRef();
			setState(477);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(475);
				match(Kas);
				setState(476);
				sequenceType();
				}
			}

			setState(479);
			match(Kin);
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
		enterRule(_localctx, 56, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			match(Kswitch);
			setState(483);
			match(T__26);
			setState(484);
			((SwitchExprContext)_localctx).cond = expr();
			setState(485);
			match(T__27);
			setState(487); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(486);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(489); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(491);
			match(Kdefault);
			setState(492);
			match(Kreturn);
			setState(493);
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
		enterRule(_localctx, 58, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(495);
				match(Kcase);
				setState(496);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(499); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(501);
			match(Kreturn);
			setState(502);
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
		enterRule(_localctx, 60, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(504);
			match(Ktypeswitch);
			setState(505);
			match(T__26);
			setState(506);
			expr();
			setState(507);
			match(T__27);
			setState(509); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(508);
				caseClause();
				}
				}
				setState(511); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(513);
			match(Kdefault);
			setState(515);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(514);
				varRef();
				}
			}

			setState(517);
			match(Kreturn);
			setState(518);
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
		enterRule(_localctx, 62, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(520);
			match(Kcase);
			setState(524);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(521);
				varRef();
				setState(522);
				match(Kas);
				}
			}

			setState(526);
			sequenceType();
			setState(531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__31) {
				{
				{
				setState(527);
				match(T__31);
				setState(528);
				sequenceType();
				}
				}
				setState(533);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(534);
			match(Kreturn);
			setState(535);
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
		enterRule(_localctx, 64, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(537);
			match(Kif);
			setState(538);
			match(T__26);
			setState(539);
			((IfExprContext)_localctx).test_condition = expr();
			setState(540);
			match(T__27);
			setState(541);
			match(Kthen);
			setState(542);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(543);
			match(Kelse);
			setState(544);
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
		enterRule(_localctx, 66, RULE_tryCatchExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(546);
			match(Ktry);
			setState(547);
			match(T__28);
			setState(548);
			expr();
			setState(549);
			match(T__29);
			setState(550);
			match(Kcatch);
			setState(551);
			match(T__32);
			setState(552);
			match(T__28);
			setState(553);
			expr();
			setState(554);
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
		enterRule(_localctx, 68, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(556);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(561);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(557);
					match(Kor);
					setState(558);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(563);
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
		enterRule(_localctx, 70, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(564);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(569);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(565);
					match(Kand);
					setState(566);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(571);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 72, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(573);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				{
				setState(572);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(575);
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
		public Token _tset1025;
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
		enterRule(_localctx, 74, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(577);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(580);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) {
				{
				setState(578);
				((ComparisonExprContext)_localctx)._tset1025 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1025 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1025);
				setState(579);
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
		enterRule(_localctx, 76, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(582);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(587);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44) {
				{
				{
				setState(583);
				match(T__44);
				setState(584);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(589);
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
		enterRule(_localctx, 78, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(590);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(593);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(591);
				match(Kto);
				setState(592);
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
		public Token _tset1134;
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
		enterRule(_localctx, 80, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(595);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(600);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(596);
					((AdditiveExprContext)_localctx)._tset1134 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__45 || _la==T__46) ) {
						((AdditiveExprContext)_localctx)._tset1134 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1134);
					setState(597);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(602);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public Token _tset1162;
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
		enterRule(_localctx, 82, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(603);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(608);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) {
				{
				{
				setState(604);
				((MultiplicativeExprContext)_localctx)._tset1162 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1162 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1162);
				setState(605);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(610);
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
		enterRule(_localctx, 84, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(611);
			((InstanceOfExprContext)_localctx).main_expr = treatExpr();
			setState(615);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(612);
				match(Kinstance);
				setState(613);
				match(Kof);
				setState(614);
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
		enterRule(_localctx, 86, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(617);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(621);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(618);
				match(Ktreat);
				setState(619);
				match(Kas);
				setState(620);
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
		enterRule(_localctx, 88, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(623);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(627);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(624);
				match(Kcastable);
				setState(625);
				match(Kas);
				setState(626);
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
		enterRule(_localctx, 90, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(629);
			((CastExprContext)_localctx).main_expr = unaryExpr();
			setState(633);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(630);
				match(Kcast);
				setState(631);
				match(Kas);
				setState(632);
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
		public Token _tset1279;
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
		enterRule(_localctx, 92, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(638);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45 || _la==T__46) {
				{
				{
				setState(635);
				((UnaryExprContext)_localctx)._tset1279 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__45 || _la==T__46) ) {
					((UnaryExprContext)_localctx)._tset1279 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1279);
				}
				}
				setState(640);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(641);
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
		enterRule(_localctx, 94, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(643);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(648);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__50) {
				{
				{
				setState(644);
				match(T__50);
				setState(645);
				postFixExpr();
				}
				}
				setState(650);
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
		public ArrayLookupContext al;
		public PredicateContext pr;
		public ObjectLookupContext ol;
		public ArrayUnboxingContext au;
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
		enterRule(_localctx, 96, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(651);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(658);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(656);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
					case 1:
						{
						setState(652);
						((PostFixExprContext)_localctx).al = arrayLookup();
						}
						break;
					case 2:
						{
						setState(653);
						((PostFixExprContext)_localctx).pr = predicate();
						}
						break;
					case 3:
						{
						setState(654);
						((PostFixExprContext)_localctx).ol = objectLookup();
						}
						break;
					case 4:
						{
						setState(655);
						((PostFixExprContext)_localctx).au = arrayUnboxing();
						}
						break;
					}
					} 
				}
				setState(660);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 98, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(661);
			match(T__51);
			setState(662);
			match(T__51);
			setState(663);
			expr();
			setState(664);
			match(T__52);
			setState(665);
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
		enterRule(_localctx, 100, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(667);
			match(T__51);
			setState(668);
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
		enterRule(_localctx, 102, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(670);
			match(T__51);
			setState(671);
			expr();
			setState(672);
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
		enterRule(_localctx, 104, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(674);
			match(T__53);
			setState(681);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(675);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case 2:
				{
				setState(676);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case 3:
				{
				setState(677);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case 4:
				{
				setState(678);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case 5:
				{
				setState(679);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case 6:
				{
				setState(680);
				((ObjectLookupContext)_localctx).ci = contextItemExpr();
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
		enterRule(_localctx, 106, RULE_primaryExpr);
		try {
			setState(694);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(683);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(684);
				match(Literal);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(685);
				stringLiteral();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(686);
				varRef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(687);
				parenthesizedExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(688);
				contextItemExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(689);
				objectConstructor();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(690);
				functionCall();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(691);
				orderedExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(692);
				unorderedExpr();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(693);
				arrayConstructor();
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
		enterRule(_localctx, 108, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(696);
			match(T__30);
			setState(699);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(697);
				((VarRefContext)_localctx).ns = match(NCName);
				setState(698);
				match(T__9);
				}
				break;
			}
			setState(701);
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
		enterRule(_localctx, 110, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(703);
			match(T__26);
			setState(705);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__56) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (T__64 - 64)) | (1L << (T__65 - 64)) | (1L << (T__66 - 64)) | (1L << (T__67 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kjson - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				setState(704);
				expr();
				}
			}

			setState(707);
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
		enterRule(_localctx, 112, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(709);
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
		enterRule(_localctx, 114, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(711);
			match(T__6);
			setState(712);
			match(T__28);
			setState(713);
			expr();
			setState(714);
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
		enterRule(_localctx, 116, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(716);
			match(T__7);
			setState(717);
			match(T__28);
			setState(718);
			expr();
			setState(719);
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
		enterRule(_localctx, 118, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(727);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(724);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
				case 1:
					{
					setState(721);
					((FunctionCallContext)_localctx).ns = match(NCName);
					}
					break;
				case 2:
					{
					setState(722);
					((FunctionCallContext)_localctx).kw = keyWords();
					}
					break;
				case 3:
					{
					}
					break;
				}
				setState(726);
				match(T__9);
				}
				break;
			}
			setState(731);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__61:
			case T__62:
			case T__63:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case NCName:
				{
				setState(729);
				((FunctionCallContext)_localctx).fn_name = nCNameOrKeyWord();
				}
				break;
			case T__26:
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
				setState(730);
				((FunctionCallContext)_localctx).kw = keyWords();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(733);
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
		enterRule(_localctx, 120, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(735);
			match(T__26);
			setState(742);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__55) | (1L << T__56) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (T__64 - 64)) | (1L << (T__65 - 64)) | (1L << (T__66 - 64)) | (1L << (T__67 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kjson - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				{
				setState(736);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(738);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__21) {
					{
					setState(737);
					match(T__21);
					}
				}

				}
				}
				setState(744);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(745);
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
		enterRule(_localctx, 122, RULE_argument);
		try {
			setState(749);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
			case T__7:
			case T__9:
			case T__26:
			case T__28:
			case T__30:
			case T__45:
			case T__46:
			case T__51:
			case T__54:
			case T__56:
			case T__61:
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
				setState(747);
				exprSingle();
				}
				break;
			case T__55:
				enterOuterAlt(_localctx, 2);
				{
				setState(748);
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
		enterRule(_localctx, 124, RULE_sequenceType);
		try {
			setState(759);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
				enterOuterAlt(_localctx, 1);
				{
				setState(751);
				match(T__26);
				setState(752);
				match(T__27);
				}
				break;
			case T__58:
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
				setState(753);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(757);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
				case 1:
					{
					setState(754);
					((SequenceTypeContext)_localctx).s56 = match(T__55);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s56);
					}
					break;
				case 2:
					{
					setState(755);
					((SequenceTypeContext)_localctx).s33 = match(T__32);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s33);
					}
					break;
				case 3:
					{
					setState(756);
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
		enterRule(_localctx, 126, RULE_objectConstructor);
		int _la;
		try {
			setState(777);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(761);
				match(T__28);
				setState(770);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__56) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (T__64 - 64)) | (1L << (T__65 - 64)) | (1L << (T__66 - 64)) | (1L << (T__67 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kjson - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
					{
					setState(762);
					pairConstructor();
					setState(767);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__21) {
						{
						{
						setState(763);
						match(T__21);
						setState(764);
						pairConstructor();
						}
						}
						setState(769);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(772);
				match(T__29);
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 2);
				{
				setState(773);
				((ObjectConstructorContext)_localctx).s57 = match(T__56);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s57);
				setState(774);
				expr();
				setState(775);
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
		enterRule(_localctx, 128, RULE_itemType);
		try {
			setState(782);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__58:
				enterOuterAlt(_localctx, 1);
				{
				setState(779);
				match(T__58);
				}
				break;
			case T__59:
			case T__60:
			case Kjson:
				enterOuterAlt(_localctx, 2);
				{
				setState(780);
				jSONItemTest();
				}
				break;
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
			case NullLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(781);
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
		enterRule(_localctx, 130, RULE_jSONItemTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(784);
			_la = _input.LA(1);
			if ( !(((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (T__59 - 60)) | (1L << (T__60 - 60)) | (1L << (Kjson - 60)))) != 0)) ) {
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
		enterRule(_localctx, 132, RULE_keyWordBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(786);
			match(T__61);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 134, RULE_keyWordDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(788);
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
		enterRule(_localctx, 136, RULE_keyWordYearMonthDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(790);
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
		enterRule(_localctx, 138, RULE_keyWordDayTimeDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(792);
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
		enterRule(_localctx, 140, RULE_keyWordHexBinary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(794);
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
		enterRule(_localctx, 142, RULE_keyWordBase64Binary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(796);
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
		enterRule(_localctx, 144, RULE_keyWordDateTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(798);
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
		enterRule(_localctx, 146, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			((SingleTypeContext)_localctx).item = atomicType();
			setState(802);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(801);
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
		public KeyWordHexBinaryContext keyWordHexBinary() {
			return getRuleContext(KeyWordHexBinaryContext.class,0);
		}
		public KeyWordBase64BinaryContext keyWordBase64Binary() {
			return getRuleContext(KeyWordBase64BinaryContext.class,0);
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
		enterRule(_localctx, 148, RULE_atomicType);
		try {
			setState(817);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__68:
				enterOuterAlt(_localctx, 1);
				{
				setState(804);
				match(T__68);
				}
				break;
			case T__69:
				enterOuterAlt(_localctx, 2);
				{
				setState(805);
				match(T__69);
				}
				break;
			case T__70:
				enterOuterAlt(_localctx, 3);
				{
				setState(806);
				match(T__70);
				}
				break;
			case T__71:
				enterOuterAlt(_localctx, 4);
				{
				setState(807);
				match(T__71);
				}
				break;
			case T__72:
				enterOuterAlt(_localctx, 5);
				{
				setState(808);
				match(T__72);
				}
				break;
			case T__61:
				enterOuterAlt(_localctx, 6);
				{
				setState(809);
				keyWordBoolean();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 7);
				{
				setState(810);
				keyWordDuration();
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 8);
				{
				setState(811);
				keyWordYearMonthDuration();
				}
				break;
			case T__64:
				enterOuterAlt(_localctx, 9);
				{
				setState(812);
				keyWordDayTimeDuration();
				}
				break;
			case T__67:
				enterOuterAlt(_localctx, 10);
				{
				setState(813);
				keyWordDateTime();
				}
				break;
			case T__65:
				enterOuterAlt(_localctx, 11);
				{
				setState(814);
				keyWordHexBinary();
				}
				break;
			case T__66:
				enterOuterAlt(_localctx, 12);
				{
				setState(815);
				keyWordBase64Binary();
				}
				break;
			case NullLiteral:
				enterOuterAlt(_localctx, 13);
				{
				setState(816);
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
		public KeyWordHexBinaryContext keyWordHexBinary() {
			return getRuleContext(KeyWordHexBinaryContext.class,0);
		}
		public KeyWordBase64BinaryContext keyWordBase64Binary() {
			return getRuleContext(KeyWordBase64BinaryContext.class,0);
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
		enterRule(_localctx, 150, RULE_nCNameOrKeyWord);
		try {
			setState(828);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(819);
				match(NCName);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(820);
				keyWordBoolean();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(821);
				keyWordDuration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(822);
				keyWordYearMonthDuration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(823);
				keyWordDayTimeDuration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(824);
				keyWordDateTime();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(825);
				keyWordHexBinary();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(826);
				keyWordBase64Binary();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(827);
				keyWordHexBinary();
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
		enterRule(_localctx, 152, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(832);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(830);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(831);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(834);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==T__55) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(835);
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
		enterRule(_localctx, 154, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(837);
			match(T__51);
			setState(839);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__56) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (T__64 - 64)) | (1L << (T__65 - 64)) | (1L << (T__66 - 64)) | (1L << (T__67 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kjson - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				setState(838);
				expr();
				}
			}

			setState(841);
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
		enterRule(_localctx, 156, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(843);
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
		enterRule(_localctx, 158, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(845);
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
		enterRule(_localctx, 160, RULE_keyWords);
		try {
			setState(891);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(847);
				match(Kjsoniq);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(848);
				match(Kjson);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(849);
				match(Kversion);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(850);
				match(Ktypeswitch);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(851);
				match(Kor);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(852);
				match(Kand);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(853);
				match(Knot);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(854);
				match(Kto);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(855);
				match(Kinstance);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(856);
				match(Kof);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(857);
				match(Ktreat);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(859);
				match(Kcast);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(860);
				match(Kcastable);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(861);
				match(Kdefault);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(862);
				match(Kthen);
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(863);
				match(Kelse);
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(864);
				match(Kcollation);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(865);
				match(Kgreatest);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(866);
				match(Kleast);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(867);
				match(Kswitch);
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(868);
				match(Kcase);
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(869);
				match(Ktry);
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(870);
				match(Kcatch);
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(871);
				match(Ksome);
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(872);
				match(Kevery);
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(873);
				match(Ksatisfies);
				}
				break;
			case 28:
				enterOuterAlt(_localctx, 28);
				{
				setState(874);
				match(Kstable);
				}
				break;
			case 29:
				enterOuterAlt(_localctx, 29);
				{
				setState(875);
				match(Kascending);
				}
				break;
			case 30:
				enterOuterAlt(_localctx, 30);
				{
				setState(876);
				match(Kdescending);
				}
				break;
			case 31:
				enterOuterAlt(_localctx, 31);
				{
				setState(877);
				match(Kempty);
				}
				break;
			case 32:
				enterOuterAlt(_localctx, 32);
				{
				setState(878);
				match(Kallowing);
				}
				break;
			case 33:
				enterOuterAlt(_localctx, 33);
				{
				setState(879);
				match(Kas);
				}
				break;
			case 34:
				enterOuterAlt(_localctx, 34);
				{
				setState(880);
				match(Kat);
				}
				break;
			case 35:
				enterOuterAlt(_localctx, 35);
				{
				setState(881);
				match(Kin);
				}
				break;
			case 36:
				enterOuterAlt(_localctx, 36);
				{
				setState(882);
				match(Kif);
				}
				break;
			case 37:
				enterOuterAlt(_localctx, 37);
				{
				setState(883);
				match(Kfor);
				}
				break;
			case 38:
				enterOuterAlt(_localctx, 38);
				{
				setState(884);
				match(Klet);
				}
				break;
			case 39:
				enterOuterAlt(_localctx, 39);
				{
				setState(885);
				match(Kwhere);
				}
				break;
			case 40:
				enterOuterAlt(_localctx, 40);
				{
				setState(886);
				match(Kgroup);
				}
				break;
			case 41:
				enterOuterAlt(_localctx, 41);
				{
				setState(887);
				match(Kby);
				}
				break;
			case 42:
				enterOuterAlt(_localctx, 42);
				{
				setState(888);
				match(Korder);
				}
				break;
			case 43:
				enterOuterAlt(_localctx, 43);
				{
				setState(889);
				match(Kcount);
				}
				break;
			case 44:
				enterOuterAlt(_localctx, 44);
				{
				setState(890);
				match(Kreturn);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0082\u0380\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\3\2\3\2\3\2"+
		"\3\2\3\2\5\2\u00aa\n\2\3\2\3\2\5\2\u00ae\n\2\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\5\5\u00c0\n\5\3\5\3\5\7\5\u00c4"+
		"\n\5\f\5\16\5\u00c7\13\5\3\5\3\5\5\5\u00cb\n\5\3\5\3\5\7\5\u00cf\n\5\f"+
		"\5\16\5\u00d2\13\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\t\3\t\3\t\3\t\5\t\u00e7\n\t\3\t\3\t\3\t\5\t\u00ec\n\t\3\t\3"+
		"\t\3\t\3\t\7\t\u00f2\n\t\f\t\16\t\u00f5\13\t\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\13\5\13\u00fe\n\13\3\13\3\13\3\13\3\13\3\13\7\13\u0105\n\13\f\13"+
		"\16\13\u0108\13\13\5\13\u010a\n\13\3\f\3\f\3\f\3\f\3\f\5\f\u0111\n\f\3"+
		"\f\3\f\3\f\3\f\3\f\5\f\u0118\n\f\5\f\u011a\n\f\3\r\3\r\3\r\3\r\5\r\u0120"+
		"\n\r\3\r\3\r\3\r\5\r\u0125\n\r\3\r\3\r\3\r\5\r\u012a\n\r\3\r\3\r\3\r\3"+
		"\r\3\r\5\r\u0131\n\r\3\16\3\16\3\16\3\16\5\16\u0137\n\16\3\16\3\16\3\16"+
		"\3\16\3\16\5\16\u013e\n\16\7\16\u0140\n\16\f\16\16\16\u0143\13\16\3\17"+
		"\3\17\3\17\7\17\u0148\n\17\f\17\16\17\u014b\13\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\5\20\u0154\n\20\3\21\3\21\5\21\u0158\n\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\7\21\u0160\n\21\f\21\16\21\u0163\13\21\3\21\3\21\3"+
		"\21\3\22\3\22\3\22\3\22\7\22\u016c\n\22\f\22\16\22\u016f\13\22\3\23\3"+
		"\23\3\23\5\23\u0174\n\23\3\23\3\23\5\23\u0178\n\23\3\23\3\23\5\23\u017c"+
		"\n\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\7\24\u0185\n\24\f\24\16\24\u0188"+
		"\13\24\3\25\3\25\3\25\5\25\u018d\n\25\3\25\3\25\3\25\3\26\3\26\3\26\3"+
		"\27\3\27\3\27\3\27\3\27\7\27\u019a\n\27\f\27\16\27\u019d\13\27\3\30\3"+
		"\30\3\30\5\30\u01a2\n\30\3\30\3\30\5\30\u01a6\n\30\3\30\3\30\5\30\u01aa"+
		"\n\30\3\31\3\31\3\31\3\31\3\31\5\31\u01b1\n\31\3\31\3\31\3\31\7\31\u01b6"+
		"\n\31\f\31\16\31\u01b9\13\31\3\32\3\32\3\32\5\32\u01be\n\32\3\32\3\32"+
		"\3\32\5\32\u01c3\n\32\5\32\u01c5\n\32\3\32\3\32\5\32\u01c9\n\32\3\33\3"+
		"\33\3\33\3\34\3\34\5\34\u01d0\n\34\3\34\3\34\3\34\7\34\u01d5\n\34\f\34"+
		"\16\34\u01d8\13\34\3\34\3\34\3\34\3\35\3\35\3\35\5\35\u01e0\n\35\3\35"+
		"\3\35\3\35\3\36\3\36\3\36\3\36\3\36\6\36\u01ea\n\36\r\36\16\36\u01eb\3"+
		"\36\3\36\3\36\3\36\3\37\3\37\6\37\u01f4\n\37\r\37\16\37\u01f5\3\37\3\37"+
		"\3\37\3 \3 \3 \3 \3 \6 \u0200\n \r \16 \u0201\3 \3 \5 \u0206\n \3 \3 "+
		"\3 \3!\3!\3!\3!\5!\u020f\n!\3!\3!\3!\7!\u0214\n!\f!\16!\u0217\13!\3!\3"+
		"!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3"+
		"#\3$\3$\3$\7$\u0232\n$\f$\16$\u0235\13$\3%\3%\3%\7%\u023a\n%\f%\16%\u023d"+
		"\13%\3&\5&\u0240\n&\3&\3&\3\'\3\'\3\'\5\'\u0247\n\'\3(\3(\3(\7(\u024c"+
		"\n(\f(\16(\u024f\13(\3)\3)\3)\5)\u0254\n)\3*\3*\3*\7*\u0259\n*\f*\16*"+
		"\u025c\13*\3+\3+\3+\7+\u0261\n+\f+\16+\u0264\13+\3,\3,\3,\3,\5,\u026a"+
		"\n,\3-\3-\3-\3-\5-\u0270\n-\3.\3.\3.\3.\5.\u0276\n.\3/\3/\3/\3/\5/\u027c"+
		"\n/\3\60\7\60\u027f\n\60\f\60\16\60\u0282\13\60\3\60\3\60\3\61\3\61\3"+
		"\61\7\61\u0289\n\61\f\61\16\61\u028c\13\61\3\62\3\62\3\62\3\62\3\62\7"+
		"\62\u0293\n\62\f\62\16\62\u0296\13\62\3\63\3\63\3\63\3\63\3\63\3\63\3"+
		"\64\3\64\3\64\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\66\3\66\5"+
		"\66\u02ac\n\66\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67"+
		"\5\67\u02b9\n\67\38\38\38\58\u02be\n8\38\38\39\39\59\u02c4\n9\39\39\3"+
		":\3:\3;\3;\3;\3;\3;\3<\3<\3<\3<\3<\3=\3=\3=\5=\u02d7\n=\3=\5=\u02da\n"+
		"=\3=\3=\5=\u02de\n=\3=\3=\3>\3>\3>\5>\u02e5\n>\7>\u02e7\n>\f>\16>\u02ea"+
		"\13>\3>\3>\3?\3?\5?\u02f0\n?\3@\3@\3@\3@\3@\3@\5@\u02f8\n@\5@\u02fa\n"+
		"@\3A\3A\3A\3A\7A\u0300\nA\fA\16A\u0303\13A\5A\u0305\nA\3A\3A\3A\3A\3A"+
		"\5A\u030c\nA\3B\3B\3B\5B\u0311\nB\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H"+
		"\3I\3I\3J\3J\3K\3K\5K\u0325\nK\3L\3L\3L\3L\3L\3L\3L\3L\3L\3L\3L\3L\3L"+
		"\5L\u0334\nL\3M\3M\3M\3M\3M\3M\3M\3M\3M\5M\u033f\nM\3N\3N\5N\u0343\nN"+
		"\3N\3N\3N\3O\3O\5O\u034a\nO\3O\3O\3P\3P\3Q\3Q\3R\3R\3R\3R\3R\3R\3R\3R"+
		"\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R"+
		"\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\5R\u037e\nR\3R\2\2S\2\4\6\b\n"+
		"\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\"+
		"^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"+
		"\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\2\n\3\2\t\n\3\2"+
		"ab\3\2\r\26\4\2\6\6$.\3\2\60\61\4\2##\62\64\4\2>?vv\4\2\f\f::\2\u03e4"+
		"\2\u00a9\3\2\2\2\4\u00af\3\2\2\2\6\u00b2\3\2\2\2\b\u00c5\3\2\2\2\n\u00d3"+
		"\3\2\2\2\f\u00d8\3\2\2\2\16\u00dc\3\2\2\2\20\u00e2\3\2\2\2\22\u00f6\3"+
		"\2\2\2\24\u00f8\3\2\2\2\26\u010b\3\2\2\2\30\u011b\3\2\2\2\32\u0132\3\2"+
		"\2\2\34\u0144\3\2\2\2\36\u0153\3\2\2\2 \u0157\3\2\2\2\"\u0167\3\2\2\2"+
		"$\u0170\3\2\2\2&\u0180\3\2\2\2(\u0189\3\2\2\2*\u0191\3\2\2\2,\u0194\3"+
		"\2\2\2.\u019e\3\2\2\2\60\u01b0\3\2\2\2\62\u01ba\3\2\2\2\64\u01ca\3\2\2"+
		"\2\66\u01cf\3\2\2\28\u01dc\3\2\2\2:\u01e4\3\2\2\2<\u01f3\3\2\2\2>\u01fa"+
		"\3\2\2\2@\u020a\3\2\2\2B\u021b\3\2\2\2D\u0224\3\2\2\2F\u022e\3\2\2\2H"+
		"\u0236\3\2\2\2J\u023f\3\2\2\2L\u0243\3\2\2\2N\u0248\3\2\2\2P\u0250\3\2"+
		"\2\2R\u0255\3\2\2\2T\u025d\3\2\2\2V\u0265\3\2\2\2X\u026b\3\2\2\2Z\u0271"+
		"\3\2\2\2\\\u0277\3\2\2\2^\u0280\3\2\2\2`\u0285\3\2\2\2b\u028d\3\2\2\2"+
		"d\u0297\3\2\2\2f\u029d\3\2\2\2h\u02a0\3\2\2\2j\u02a4\3\2\2\2l\u02b8\3"+
		"\2\2\2n\u02ba\3\2\2\2p\u02c1\3\2\2\2r\u02c7\3\2\2\2t\u02c9\3\2\2\2v\u02ce"+
		"\3\2\2\2x\u02d9\3\2\2\2z\u02e1\3\2\2\2|\u02ef\3\2\2\2~\u02f9\3\2\2\2\u0080"+
		"\u030b\3\2\2\2\u0082\u0310\3\2\2\2\u0084\u0312\3\2\2\2\u0086\u0314\3\2"+
		"\2\2\u0088\u0316\3\2\2\2\u008a\u0318\3\2\2\2\u008c\u031a\3\2\2\2\u008e"+
		"\u031c\3\2\2\2\u0090\u031e\3\2\2\2\u0092\u0320\3\2\2\2\u0094\u0322\3\2"+
		"\2\2\u0096\u0333\3\2\2\2\u0098\u033e\3\2\2\2\u009a\u0342\3\2\2\2\u009c"+
		"\u0347\3\2\2\2\u009e\u034d\3\2\2\2\u00a0\u034f\3\2\2\2\u00a2\u037d\3\2"+
		"\2\2\u00a4\u00a5\7u\2\2\u00a5\u00a6\7t\2\2\u00a6\u00a7\5\u00a0Q\2\u00a7"+
		"\u00a8\7\3\2\2\u00a8\u00aa\3\2\2\2\u00a9\u00a4\3\2\2\2\u00a9\u00aa\3\2"+
		"\2\2\u00aa\u00ad\3\2\2\2\u00ab\u00ae\5\6\4\2\u00ac\u00ae\5\4\3\2\u00ad"+
		"\u00ab\3\2\2\2\u00ad\u00ac\3\2\2\2\u00ae\3\3\2\2\2\u00af\u00b0\5\b\5\2"+
		"\u00b0\u00b1\5\34\17\2\u00b1\5\3\2\2\2\u00b2\u00b3\7\4\2\2\u00b3\u00b4"+
		"\7\5\2\2\u00b4\u00b5\7\u0080\2\2\u00b5\u00b6\7\6\2\2\u00b6\u00b7\5\u009e"+
		"P\2\u00b7\u00b8\7\3\2\2\u00b8\u00b9\5\b\5\2\u00b9\7\3\2\2\2\u00ba\u00c0"+
		"\5\n\6\2\u00bb\u00c0\5\f\7\2\u00bc\u00c0\5\16\b\2\u00bd\u00c0\5\20\t\2"+
		"\u00be\u00c0\5\24\13\2\u00bf\u00ba\3\2\2\2\u00bf\u00bb\3\2\2\2\u00bf\u00bc"+
		"\3\2\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1"+
		"\u00c2\7\3\2\2\u00c2\u00c4\3\2\2\2\u00c3\u00bf\3\2\2\2\u00c4\u00c7\3\2"+
		"\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00d0\3\2\2\2\u00c7"+
		"\u00c5\3\2\2\2\u00c8\u00cb\5\30\r\2\u00c9\u00cb\5\26\f\2\u00ca\u00c8\3"+
		"\2\2\2\u00ca\u00c9\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00cd\7\3\2\2\u00cd"+
		"\u00cf\3\2\2\2\u00ce\u00ca\3\2\2\2\u00cf\u00d2\3\2\2\2\u00d0\u00ce\3\2"+
		"\2\2\u00d0\u00d1\3\2\2\2\u00d1\t\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d3\u00d4"+
		"\7\7\2\2\u00d4\u00d5\7g\2\2\u00d5\u00d6\7`\2\2\u00d6\u00d7\5\u009eP\2"+
		"\u00d7\13\3\2\2\2\u00d8\u00d9\7\7\2\2\u00d9\u00da\7\b\2\2\u00da\u00db"+
		"\t\2\2\2\u00db\r\3\2\2\2\u00dc\u00dd\7\7\2\2\u00dd\u00de\7g\2\2\u00de"+
		"\u00df\7Q\2\2\u00df\u00e0\7X\2\2\u00e0\u00e1\t\3\2\2\u00e1\17\3\2\2\2"+
		"\u00e2\u00eb\7\7\2\2\u00e3\u00e6\7\13\2\2\u00e4\u00e5\7\u0080\2\2\u00e5"+
		"\u00e7\7\f\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\u00e8\3\2"+
		"\2\2\u00e8\u00ec\7\u0080\2\2\u00e9\u00ea\7g\2\2\u00ea\u00ec\7\13\2\2\u00eb"+
		"\u00e3\3\2\2\2\u00eb\u00e9\3\2\2\2\u00ec\u00f3\3\2\2\2\u00ed\u00ee\5\22"+
		"\n\2\u00ee\u00ef\7\6\2\2\u00ef\u00f0\5\u00a0Q\2\u00f0\u00f2\3\2\2\2\u00f1"+
		"\u00ed\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2"+
		"\2\2\u00f4\21\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00f7\t\4\2\2\u00f7\23"+
		"\3\2\2\2\u00f8\u00f9\7\27\2\2\u00f9\u00fd\7\4\2\2\u00fa\u00fb\7\5\2\2"+
		"\u00fb\u00fc\7\u0080\2\2\u00fc\u00fe\7\6\2\2\u00fd\u00fa\3\2\2\2\u00fd"+
		"\u00fe\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0109\5\u009eP\2\u0100\u0101"+
		"\7V\2\2\u0101\u0106\5\u009eP\2\u0102\u0103\7\30\2\2\u0103\u0105\5\u009e"+
		"P\2\u0104\u0102\3\2\2\2\u0105\u0108\3\2\2\2\u0106\u0104\3\2\2\2\u0106"+
		"\u0107\3\2\2\2\u0107\u010a\3\2\2\2\u0108\u0106\3\2\2\2\u0109\u0100\3\2"+
		"\2\2\u0109\u010a\3\2\2\2\u010a\25\3\2\2\2\u010b\u010c\7\7\2\2\u010c\u010d"+
		"\7\31\2\2\u010d\u0110\5n8\2\u010e\u010f\7U\2\2\u010f\u0111\5~@\2\u0110"+
		"\u010e\3\2\2\2\u0110\u0111\3\2\2\2\u0111\u0119\3\2\2\2\u0112\u0113\7\32"+
		"\2\2\u0113\u011a\5\36\20\2\u0114\u0117\7\33\2\2\u0115\u0116\7\32\2\2\u0116"+
		"\u0118\5\36\20\2\u0117\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011a\3"+
		"\2\2\2\u0119\u0112\3\2\2\2\u0119\u0114\3\2\2\2\u011a\27\3\2\2\2\u011b"+
		"\u011c\7\7\2\2\u011c\u011f\7\34\2\2\u011d\u011e\7\u0080\2\2\u011e\u0120"+
		"\7\f\2\2\u011f\u011d\3\2\2\2\u011f\u0120\3\2\2\2\u0120\u0121\3\2\2\2\u0121"+
		"\u0122\7\u0080\2\2\u0122\u0124\7\35\2\2\u0123\u0125\5\32\16\2\u0124\u0123"+
		"\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0129\7\36\2\2"+
		"\u0127\u0128\7U\2\2\u0128\u012a\5~@\2\u0129\u0127\3\2\2\2\u0129\u012a"+
		"\3\2\2\2\u012a\u0130\3\2\2\2\u012b\u012c\7\37\2\2\u012c\u012d\5\34\17"+
		"\2\u012d\u012e\7 \2\2\u012e\u0131\3\2\2\2\u012f\u0131\7\33\2\2\u0130\u012b"+
		"\3\2\2\2\u0130\u012f\3\2\2\2\u0131\31\3\2\2\2\u0132\u0133\7!\2\2\u0133"+
		"\u0136\7\u0080\2\2\u0134\u0135\7U\2\2\u0135\u0137\5~@\2\u0136\u0134\3"+
		"\2\2\2\u0136\u0137\3\2\2\2\u0137\u0141\3\2\2\2\u0138\u0139\7\30\2\2\u0139"+
		"\u013a\7!\2\2\u013a\u013d\7\u0080\2\2\u013b\u013c\7U\2\2\u013c\u013e\5"+
		"~@\2\u013d\u013b\3\2\2\2\u013d\u013e\3\2\2\2\u013e\u0140\3\2\2\2\u013f"+
		"\u0138\3\2\2\2\u0140\u0143\3\2\2\2\u0141\u013f\3\2\2\2\u0141\u0142\3\2"+
		"\2\2\u0142\33\3\2\2\2\u0143\u0141\3\2\2\2\u0144\u0149\5\36\20\2\u0145"+
		"\u0146\7\30\2\2\u0146\u0148\5\36\20\2\u0147\u0145\3\2\2\2\u0148\u014b"+
		"\3\2\2\2\u0149\u0147\3\2\2\2\u0149\u014a\3\2\2\2\u014a\35\3\2\2\2\u014b"+
		"\u0149\3\2\2\2\u014c\u0154\5 \21\2\u014d\u0154\5\66\34\2\u014e\u0154\5"+
		":\36\2\u014f\u0154\5> \2\u0150\u0154\5B\"\2\u0151\u0154\5D#\2\u0152\u0154"+
		"\5F$\2\u0153\u014c\3\2\2\2\u0153\u014d\3\2\2\2\u0153\u014e\3\2\2\2\u0153"+
		"\u014f\3\2\2\2\u0153\u0150\3\2\2\2\u0153\u0151\3\2\2\2\u0153\u0152\3\2"+
		"\2\2\u0154\37\3\2\2\2\u0155\u0158\5\"\22\2\u0156\u0158\5&\24\2\u0157\u0155"+
		"\3\2\2\2\u0157\u0156\3\2\2\2\u0158\u0161\3\2\2\2\u0159\u0160\5\"\22\2"+
		"\u015a\u0160\5*\26\2\u015b\u0160\5&\24\2\u015c\u0160\5,\27\2\u015d\u0160"+
		"\5\60\31\2\u015e\u0160\5\64\33\2\u015f\u0159\3\2\2\2\u015f\u015a\3\2\2"+
		"\2\u015f\u015b\3\2\2\2\u015f\u015c\3\2\2\2\u015f\u015d\3\2\2\2\u015f\u015e"+
		"\3\2\2\2\u0160\u0163\3\2\2\2\u0161\u015f\3\2\2\2\u0161\u0162\3\2\2\2\u0162"+
		"\u0164\3\2\2\2\u0163\u0161\3\2\2\2\u0164\u0165\7R\2\2\u0165\u0166\5\36"+
		"\20\2\u0166!\3\2\2\2\u0167\u0168\7L\2\2\u0168\u016d\5$\23\2\u0169\u016a"+
		"\7\30\2\2\u016a\u016c\5$\23\2\u016b\u0169\3\2\2\2\u016c\u016f\3\2\2\2"+
		"\u016d\u016b\3\2\2\2\u016d\u016e\3\2\2\2\u016e#\3\2\2\2\u016f\u016d\3"+
		"\2\2\2\u0170\u0173\5n8\2\u0171\u0172\7U\2\2\u0172\u0174\5~@\2\u0173\u0171"+
		"\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0177\3\2\2\2\u0175\u0176\7W\2\2\u0176"+
		"\u0178\7X\2\2\u0177\u0175\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u017b\3\2"+
		"\2\2\u0179\u017a\7V\2\2\u017a\u017c\5n8\2\u017b\u0179\3\2\2\2\u017b\u017c"+
		"\3\2\2\2\u017c\u017d\3\2\2\2\u017d\u017e\7T\2\2\u017e\u017f\5\36\20\2"+
		"\u017f%\3\2\2\2\u0180\u0181\7M\2\2\u0181\u0186\5(\25\2\u0182\u0183\7\30"+
		"\2\2\u0183\u0185\5(\25\2\u0184\u0182\3\2\2\2\u0185\u0188\3\2\2\2\u0186"+
		"\u0184\3\2\2\2\u0186\u0187\3\2\2\2\u0187\'\3\2\2\2\u0188\u0186\3\2\2\2"+
		"\u0189\u018c\5n8\2\u018a\u018b\7U\2\2\u018b\u018d\5~@\2\u018c\u018a\3"+
		"\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e\3\2\2\2\u018e\u018f\7\32\2\2\u018f"+
		"\u0190\5\36\20\2\u0190)\3\2\2\2\u0191\u0192\7N\2\2\u0192\u0193\5\36\20"+
		"\2\u0193+\3\2\2\2\u0194\u0195\7O\2\2\u0195\u0196\7P\2\2\u0196\u019b\5"+
		".\30\2\u0197\u0198\7\30\2\2\u0198\u019a\5.\30\2\u0199\u0197\3\2\2\2\u019a"+
		"\u019d\3\2\2\2\u019b\u0199\3\2\2\2\u019b\u019c\3\2\2\2\u019c-\3\2\2\2"+
		"\u019d\u019b\3\2\2\2\u019e\u01a5\5n8\2\u019f\u01a0\7U\2\2\u01a0\u01a2"+
		"\5~@\2\u01a1\u019f\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3"+
		"\u01a4\7\32\2\2\u01a4\u01a6\5\36\20\2\u01a5\u01a1\3\2\2\2\u01a5\u01a6"+
		"\3\2\2\2\u01a6\u01a9\3\2\2\2\u01a7\u01a8\7`\2\2\u01a8\u01aa\5\u009eP\2"+
		"\u01a9\u01a7\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa/\3\2\2\2\u01ab\u01ac\7"+
		"Q\2\2\u01ac\u01b1\7P\2\2\u01ad\u01ae\7Z\2\2\u01ae\u01af\7Q\2\2\u01af\u01b1"+
		"\7P\2\2\u01b0\u01ab\3\2\2\2\u01b0\u01ad\3\2\2\2\u01b1\u01b2\3\2\2\2\u01b2"+
		"\u01b7\5\62\32\2\u01b3\u01b4\7\30\2\2\u01b4\u01b6\5\62\32\2\u01b5\u01b3"+
		"\3\2\2\2\u01b6\u01b9\3\2\2\2\u01b7\u01b5\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8"+
		"\61\3\2\2\2\u01b9\u01b7\3\2\2\2\u01ba\u01bd\5\36\20\2\u01bb\u01be\7[\2"+
		"\2\u01bc\u01be\7\\\2\2\u01bd\u01bb\3\2\2\2\u01bd\u01bc\3\2\2\2\u01bd\u01be"+
		"\3\2\2\2\u01be\u01c4\3\2\2\2\u01bf\u01c2\7X\2\2\u01c0\u01c3\7a\2\2\u01c1"+
		"\u01c3\7b\2\2\u01c2\u01c0\3\2\2\2\u01c2\u01c1\3\2\2\2\u01c3\u01c5\3\2"+
		"\2\2\u01c4\u01bf\3\2\2\2\u01c4\u01c5\3\2\2\2\u01c5\u01c8\3\2\2\2\u01c6"+
		"\u01c7\7`\2\2\u01c7\u01c9\5\u009eP\2\u01c8\u01c6\3\2\2\2\u01c8\u01c9\3"+
		"\2\2\2\u01c9\63\3\2\2\2\u01ca\u01cb\7Y\2\2\u01cb\u01cc\5n8\2\u01cc\65"+
		"\3\2\2\2\u01cd\u01d0\7]\2\2\u01ce\u01d0\7^\2\2\u01cf\u01cd\3\2\2\2\u01cf"+
		"\u01ce\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01d6\58\35\2\u01d2\u01d3\7\30"+
		"\2\2\u01d3\u01d5\58\35\2\u01d4\u01d2\3\2\2\2\u01d5\u01d8\3\2\2\2\u01d6"+
		"\u01d4\3\2\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01d9\3\2\2\2\u01d8\u01d6\3\2"+
		"\2\2\u01d9\u01da\7_\2\2\u01da\u01db\5\36\20\2\u01db\67\3\2\2\2\u01dc\u01df"+
		"\5n8\2\u01dd\u01de\7U\2\2\u01de\u01e0\5~@\2\u01df\u01dd\3\2\2\2\u01df"+
		"\u01e0\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e2\7T\2\2\u01e2\u01e3\5\36"+
		"\20\2\u01e39\3\2\2\2\u01e4\u01e5\7c\2\2\u01e5\u01e6\7\35\2\2\u01e6\u01e7"+
		"\5\34\17\2\u01e7\u01e9\7\36\2\2\u01e8\u01ea\5<\37\2\u01e9\u01e8\3\2\2"+
		"\2\u01ea\u01eb\3\2\2\2\u01eb\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ed"+
		"\3\2\2\2\u01ed\u01ee\7g\2\2\u01ee\u01ef\7R\2\2\u01ef\u01f0\5\36\20\2\u01f0"+
		";\3\2\2\2\u01f1\u01f2\7d\2\2\u01f2\u01f4\5\36\20\2\u01f3\u01f1\3\2\2\2"+
		"\u01f4\u01f5\3\2\2\2\u01f5\u01f3\3\2\2\2\u01f5\u01f6\3\2\2\2\u01f6\u01f7"+
		"\3\2\2\2\u01f7\u01f8\7R\2\2\u01f8\u01f9\5\36\20\2\u01f9=\3\2\2\2\u01fa"+
		"\u01fb\7j\2\2\u01fb\u01fc\7\35\2\2\u01fc\u01fd\5\34\17\2\u01fd\u01ff\7"+
		"\36\2\2\u01fe\u0200\5@!\2\u01ff\u01fe\3\2\2\2\u0200\u0201\3\2\2\2\u0201"+
		"\u01ff\3\2\2\2\u0201\u0202\3\2\2\2\u0202\u0203\3\2\2\2\u0203\u0205\7g"+
		"\2\2\u0204\u0206\5n8\2\u0205\u0204\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u0207"+
		"\3\2\2\2\u0207\u0208\7R\2\2\u0208\u0209\5\36\20\2\u0209?\3\2\2\2\u020a"+
		"\u020e\7d\2\2\u020b\u020c\5n8\2\u020c\u020d\7U\2\2\u020d\u020f\3\2\2\2"+
		"\u020e\u020b\3\2\2\2\u020e\u020f\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u0215"+
		"\5~@\2\u0211\u0212\7\"\2\2\u0212\u0214\5~@\2\u0213\u0211\3\2\2\2\u0214"+
		"\u0217\3\2\2\2\u0215\u0213\3\2\2\2\u0215\u0216\3\2\2\2\u0216\u0218\3\2"+
		"\2\2\u0217\u0215\3\2\2\2\u0218\u0219\7R\2\2\u0219\u021a\5\36\20\2\u021a"+
		"A\3\2\2\2\u021b\u021c\7S\2\2\u021c\u021d\7\35\2\2\u021d\u021e\5\34\17"+
		"\2\u021e\u021f\7\36\2\2\u021f\u0220\7h\2\2\u0220\u0221\5\36\20\2\u0221"+
		"\u0222\7i\2\2\u0222\u0223\5\36\20\2\u0223C\3\2\2\2\u0224\u0225\7e\2\2"+
		"\u0225\u0226\7\37\2\2\u0226\u0227\5\34\17\2\u0227\u0228\7 \2\2\u0228\u0229"+
		"\7f\2\2\u0229\u022a\7#\2\2\u022a\u022b\7\37\2\2\u022b\u022c\5\34\17\2"+
		"\u022c\u022d\7 \2\2\u022dE\3\2\2\2\u022e\u0233\5H%\2\u022f\u0230\7k\2"+
		"\2\u0230\u0232\5H%\2\u0231\u022f\3\2\2\2\u0232\u0235\3\2\2\2\u0233\u0231"+
		"\3\2\2\2\u0233\u0234\3\2\2\2\u0234G\3\2\2\2\u0235\u0233\3\2\2\2\u0236"+
		"\u023b\5J&\2\u0237\u0238\7l\2\2\u0238\u023a\5J&\2\u0239\u0237\3\2\2\2"+
		"\u023a\u023d\3\2\2\2\u023b\u0239\3\2\2\2\u023b\u023c\3\2\2\2\u023cI\3"+
		"\2\2\2\u023d\u023b\3\2\2\2\u023e\u0240\7m\2\2\u023f\u023e\3\2\2\2\u023f"+
		"\u0240\3\2\2\2\u0240\u0241\3\2\2\2\u0241\u0242\5L\'\2\u0242K\3\2\2\2\u0243"+
		"\u0246\5N(\2\u0244\u0245\t\5\2\2\u0245\u0247\5N(\2\u0246\u0244\3\2\2\2"+
		"\u0246\u0247\3\2\2\2\u0247M\3\2\2\2\u0248\u024d\5P)\2\u0249\u024a\7/\2"+
		"\2\u024a\u024c\5P)\2\u024b\u0249\3\2\2\2\u024c\u024f\3\2\2\2\u024d\u024b"+
		"\3\2\2\2\u024d\u024e\3\2\2\2\u024eO\3\2\2\2\u024f\u024d\3\2\2\2\u0250"+
		"\u0253\5R*\2\u0251\u0252\7n\2\2\u0252\u0254\5R*\2\u0253\u0251\3\2\2\2"+
		"\u0253\u0254\3\2\2\2\u0254Q\3\2\2\2\u0255\u025a\5T+\2\u0256\u0257\t\6"+
		"\2\2\u0257\u0259\5T+\2\u0258\u0256\3\2\2\2\u0259\u025c\3\2\2\2\u025a\u0258"+
		"\3\2\2\2\u025a\u025b\3\2\2\2\u025bS\3\2\2\2\u025c\u025a\3\2\2\2\u025d"+
		"\u0262\5V,\2\u025e\u025f\t\7\2\2\u025f\u0261\5V,\2\u0260\u025e\3\2\2\2"+
		"\u0261\u0264\3\2\2\2\u0262\u0260\3\2\2\2\u0262\u0263\3\2\2\2\u0263U\3"+
		"\2\2\2\u0264\u0262\3\2\2\2\u0265\u0269\5X-\2\u0266\u0267\7o\2\2\u0267"+
		"\u0268\7p\2\2\u0268\u026a\5~@\2\u0269\u0266\3\2\2\2\u0269\u026a\3\2\2"+
		"\2\u026aW\3\2\2\2\u026b\u026f\5Z.\2\u026c\u026d\7q\2\2\u026d\u026e\7U"+
		"\2\2\u026e\u0270\5~@\2\u026f\u026c\3\2\2\2\u026f\u0270\3\2\2\2\u0270Y"+
		"\3\2\2\2\u0271\u0275\5\\/\2\u0272\u0273\7s\2\2\u0273\u0274\7U\2\2\u0274"+
		"\u0276\5\u0094K\2\u0275\u0272\3\2\2\2\u0275\u0276\3\2\2\2\u0276[\3\2\2"+
		"\2\u0277\u027b\5^\60\2\u0278\u0279\7r\2\2\u0279\u027a\7U\2\2\u027a\u027c"+
		"\5\u0094K\2\u027b\u0278\3\2\2\2\u027b\u027c\3\2\2\2\u027c]\3\2\2\2\u027d"+
		"\u027f\t\6\2\2\u027e\u027d\3\2\2\2\u027f\u0282\3\2\2\2\u0280\u027e\3\2"+
		"\2\2\u0280\u0281\3\2\2\2\u0281\u0283\3\2\2\2\u0282\u0280\3\2\2\2\u0283"+
		"\u0284\5`\61\2\u0284_\3\2\2\2\u0285\u028a\5b\62\2\u0286\u0287\7\65\2\2"+
		"\u0287\u0289\5b\62\2\u0288\u0286\3\2\2\2\u0289\u028c\3\2\2\2\u028a\u0288"+
		"\3\2\2\2\u028a\u028b\3\2\2\2\u028ba\3\2\2\2\u028c\u028a\3\2\2\2\u028d"+
		"\u0294\5l\67\2\u028e\u0293\5d\63\2\u028f\u0293\5h\65\2\u0290\u0293\5j"+
		"\66\2\u0291\u0293\5f\64\2\u0292\u028e\3\2\2\2\u0292\u028f\3\2\2\2\u0292"+
		"\u0290\3\2\2\2\u0292\u0291\3\2\2\2\u0293\u0296\3\2\2\2\u0294\u0292\3\2"+
		"\2\2\u0294\u0295\3\2\2\2\u0295c\3\2\2\2\u0296\u0294\3\2\2\2\u0297\u0298"+
		"\7\66\2\2\u0298\u0299\7\66\2\2\u0299\u029a\5\34\17\2\u029a\u029b\7\67"+
		"\2\2\u029b\u029c\7\67\2\2\u029ce\3\2\2\2\u029d\u029e\7\66\2\2\u029e\u029f"+
		"\7\67\2\2\u029fg\3\2\2\2\u02a0\u02a1\7\66\2\2\u02a1\u02a2\5\34\17\2\u02a2"+
		"\u02a3\7\67\2\2\u02a3i\3\2\2\2\u02a4\u02ab\78\2\2\u02a5\u02ac\5\u00a2"+
		"R\2\u02a6\u02ac\5\u00a0Q\2\u02a7\u02ac\7\u0080\2\2\u02a8\u02ac\5p9\2\u02a9"+
		"\u02ac\5n8\2\u02aa\u02ac\5r:\2\u02ab\u02a5\3\2\2\2\u02ab\u02a6\3\2\2\2"+
		"\u02ab\u02a7\3\2\2\2\u02ab\u02a8\3\2\2\2\u02ab\u02a9\3\2\2\2\u02ab\u02aa"+
		"\3\2\2\2\u02ack\3\2\2\2\u02ad\u02b9\7x\2\2\u02ae\u02b9\7y\2\2\u02af\u02b9"+
		"\5\u00a0Q\2\u02b0\u02b9\5n8\2\u02b1\u02b9\5p9\2\u02b2\u02b9\5r:\2\u02b3"+
		"\u02b9\5\u0080A\2\u02b4\u02b9\5x=\2\u02b5\u02b9\5t;\2\u02b6\u02b9\5v<"+
		"\2\u02b7\u02b9\5\u009cO\2\u02b8\u02ad\3\2\2\2\u02b8\u02ae\3\2\2\2\u02b8"+
		"\u02af\3\2\2\2\u02b8\u02b0\3\2\2\2\u02b8\u02b1\3\2\2\2\u02b8\u02b2\3\2"+
		"\2\2\u02b8\u02b3\3\2\2\2\u02b8\u02b4\3\2\2\2\u02b8\u02b5\3\2\2\2\u02b8"+
		"\u02b6\3\2\2\2\u02b8\u02b7\3\2\2\2\u02b9m\3\2\2\2\u02ba\u02bd\7!\2\2\u02bb"+
		"\u02bc\7\u0080\2\2\u02bc\u02be\7\f\2\2\u02bd\u02bb\3\2\2\2\u02bd\u02be"+
		"\3\2\2\2\u02be\u02bf\3\2\2\2\u02bf\u02c0\7\u0080\2\2\u02c0o\3\2\2\2\u02c1"+
		"\u02c3\7\35\2\2\u02c2\u02c4\5\34\17\2\u02c3\u02c2\3\2\2\2\u02c3\u02c4"+
		"\3\2\2\2\u02c4\u02c5\3\2\2\2\u02c5\u02c6\7\36\2\2\u02c6q\3\2\2\2\u02c7"+
		"\u02c8\79\2\2\u02c8s\3\2\2\2\u02c9\u02ca\7\t\2\2\u02ca\u02cb\7\37\2\2"+
		"\u02cb\u02cc\5\34\17\2\u02cc\u02cd\7 \2\2\u02cdu\3\2\2\2\u02ce\u02cf\7"+
		"\n\2\2\u02cf\u02d0\7\37\2\2\u02d0\u02d1\5\34\17\2\u02d1\u02d2\7 \2\2\u02d2"+
		"w\3\2\2\2\u02d3\u02d7\7\u0080\2\2\u02d4\u02d7\5\u00a2R\2\u02d5\u02d7\3"+
		"\2\2\2\u02d6\u02d3\3\2\2\2\u02d6\u02d4\3\2\2\2\u02d6\u02d5\3\2\2\2\u02d7"+
		"\u02d8\3\2\2\2\u02d8\u02da\7\f\2\2\u02d9\u02d6\3\2\2\2\u02d9\u02da\3\2"+
		"\2\2\u02da\u02dd\3\2\2\2\u02db\u02de\5\u0098M\2\u02dc\u02de\5\u00a2R\2"+
		"\u02dd\u02db\3\2\2\2\u02dd\u02dc\3\2\2\2\u02de\u02df\3\2\2\2\u02df\u02e0"+
		"\5z>\2\u02e0y\3\2\2\2\u02e1\u02e8\7\35\2\2\u02e2\u02e4\5|?\2\u02e3\u02e5"+
		"\7\30\2\2\u02e4\u02e3\3\2\2\2\u02e4\u02e5\3\2\2\2\u02e5\u02e7\3\2\2\2"+
		"\u02e6\u02e2\3\2\2\2\u02e7\u02ea\3\2\2\2\u02e8\u02e6\3\2\2\2\u02e8\u02e9"+
		"\3\2\2\2\u02e9\u02eb\3\2\2\2\u02ea\u02e8\3\2\2\2\u02eb\u02ec\7\36\2\2"+
		"\u02ec{\3\2\2\2\u02ed\u02f0\5\36\20\2\u02ee\u02f0\7:\2\2\u02ef\u02ed\3"+
		"\2\2\2\u02ef\u02ee\3\2\2\2\u02f0}\3\2\2\2\u02f1\u02f2\7\35\2\2\u02f2\u02fa"+
		"\7\36\2\2\u02f3\u02f7\5\u0082B\2\u02f4\u02f8\7:\2\2\u02f5\u02f8\7#\2\2"+
		"\u02f6\u02f8\7\60\2\2\u02f7\u02f4\3\2\2\2\u02f7\u02f5\3\2\2\2\u02f7\u02f6"+
		"\3\2\2\2\u02f7\u02f8\3\2\2\2\u02f8\u02fa\3\2\2\2\u02f9\u02f1\3\2\2\2\u02f9"+
		"\u02f3\3\2\2\2\u02fa\177\3\2\2\2\u02fb\u0304\7\37\2\2\u02fc\u0301\5\u009a"+
		"N\2\u02fd\u02fe\7\30\2\2\u02fe\u0300\5\u009aN\2\u02ff\u02fd\3\2\2\2\u0300"+
		"\u0303\3\2\2\2\u0301\u02ff\3\2\2\2\u0301\u0302\3\2\2\2\u0302\u0305\3\2"+
		"\2\2\u0303\u0301\3\2\2\2\u0304\u02fc\3\2\2\2\u0304\u0305\3\2\2\2\u0305"+
		"\u0306\3\2\2\2\u0306\u030c\7 \2\2\u0307\u0308\7;\2\2\u0308\u0309\5\34"+
		"\17\2\u0309\u030a\7<\2\2\u030a\u030c\3\2\2\2\u030b\u02fb\3\2\2\2\u030b"+
		"\u0307\3\2\2\2\u030c\u0081\3\2\2\2\u030d\u0311\7=\2\2\u030e\u0311\5\u0084"+
		"C\2\u030f\u0311\5\u0096L\2\u0310\u030d\3\2\2\2\u0310\u030e\3\2\2\2\u0310"+
		"\u030f\3\2\2\2\u0311\u0083\3\2\2\2\u0312\u0313\t\b\2\2\u0313\u0085\3\2"+
		"\2\2\u0314\u0315\7@\2\2\u0315\u0087\3\2\2\2\u0316\u0317\7A\2\2\u0317\u0089"+
		"\3\2\2\2\u0318\u0319\7B\2\2\u0319\u008b\3\2\2\2\u031a\u031b\7C\2\2\u031b"+
		"\u008d\3\2\2\2\u031c\u031d\7D\2\2\u031d\u008f\3\2\2\2\u031e\u031f\7E\2"+
		"\2\u031f\u0091\3\2\2\2\u0320\u0321\7F\2\2\u0321\u0093\3\2\2\2\u0322\u0324"+
		"\5\u0096L\2\u0323\u0325\7:\2\2\u0324\u0323\3\2\2\2\u0324\u0325\3\2\2\2"+
		"\u0325\u0095\3\2\2\2\u0326\u0334\7G\2\2\u0327\u0334\7H\2\2\u0328\u0334"+
		"\7I\2\2\u0329\u0334\7J\2\2\u032a\u0334\7K\2\2\u032b\u0334\5\u0086D\2\u032c"+
		"\u0334\5\u0088E\2\u032d\u0334\5\u008aF\2\u032e\u0334\5\u008cG\2\u032f"+
		"\u0334\5\u0092J\2\u0330\u0334\5\u008eH\2\u0331\u0334\5\u0090I\2\u0332"+
		"\u0334\7x\2\2\u0333\u0326\3\2\2\2\u0333\u0327\3\2\2\2\u0333\u0328\3\2"+
		"\2\2\u0333\u0329\3\2\2\2\u0333\u032a\3\2\2\2\u0333\u032b\3\2\2\2\u0333"+
		"\u032c\3\2\2\2\u0333\u032d\3\2\2\2\u0333\u032e\3\2\2\2\u0333\u032f\3\2"+
		"\2\2\u0333\u0330\3\2\2\2\u0333\u0331\3\2\2\2\u0333\u0332\3\2\2\2\u0334"+
		"\u0097\3\2\2\2\u0335\u033f\7\u0080\2\2\u0336\u033f\5\u0086D\2\u0337\u033f"+
		"\5\u0088E\2\u0338\u033f\5\u008aF\2\u0339\u033f\5\u008cG\2\u033a\u033f"+
		"\5\u0092J\2\u033b\u033f\5\u008eH\2\u033c\u033f\5\u0090I\2\u033d\u033f"+
		"\5\u008eH\2\u033e\u0335\3\2\2\2\u033e\u0336\3\2\2\2\u033e\u0337\3\2\2"+
		"\2\u033e\u0338\3\2\2\2\u033e\u0339\3\2\2\2\u033e\u033a\3\2\2\2\u033e\u033b"+
		"\3\2\2\2\u033e\u033c\3\2\2\2\u033e\u033d\3\2\2\2\u033f\u0099\3\2\2\2\u0340"+
		"\u0343\5\36\20\2\u0341\u0343\7\u0080\2\2\u0342\u0340\3\2\2\2\u0342\u0341"+
		"\3\2\2\2\u0343\u0344\3\2\2\2\u0344\u0345\t\t\2\2\u0345\u0346\5\36\20\2"+
		"\u0346\u009b\3\2\2\2\u0347\u0349\7\66\2\2\u0348\u034a\5\34\17\2\u0349"+
		"\u0348\3\2\2\2\u0349\u034a\3\2\2\2\u034a\u034b\3\2\2\2\u034b\u034c\7\67"+
		"\2\2\u034c\u009d\3\2\2\2\u034d\u034e\5\u00a0Q\2\u034e\u009f\3\2\2\2\u034f"+
		"\u0350\7w\2\2\u0350\u00a1\3\2\2\2\u0351\u037e\7u\2\2\u0352\u037e\7v\2"+
		"\2\u0353\u037e\7t\2\2\u0354\u037e\7j\2\2\u0355\u037e\7k\2\2\u0356\u037e"+
		"\7l\2\2\u0357\u037e\7m\2\2\u0358\u037e\7n\2\2\u0359\u037e\7o\2\2\u035a"+
		"\u037e\7p\2\2\u035b\u037e\7q\2\2\u035c\u037e\3\2\2\2\u035d\u037e\7r\2"+
		"\2\u035e\u037e\7s\2\2\u035f\u037e\7g\2\2\u0360\u037e\7h\2\2\u0361\u037e"+
		"\7i\2\2\u0362\u037e\7`\2\2\u0363\u037e\7a\2\2\u0364\u037e\7b\2\2\u0365"+
		"\u037e\7c\2\2\u0366\u037e\7d\2\2\u0367\u037e\7e\2\2\u0368\u037e\7f\2\2"+
		"\u0369\u037e\7]\2\2\u036a\u037e\7^\2\2\u036b\u037e\7_\2\2\u036c\u037e"+
		"\7Z\2\2\u036d\u037e\7[\2\2\u036e\u037e\7\\\2\2\u036f\u037e\7X\2\2\u0370"+
		"\u037e\7W\2\2\u0371\u037e\7U\2\2\u0372\u037e\7V\2\2\u0373\u037e\7T\2\2"+
		"\u0374\u037e\7S\2\2\u0375\u037e\7L\2\2\u0376\u037e\7M\2\2\u0377\u037e"+
		"\7N\2\2\u0378\u037e\7O\2\2\u0379\u037e\7P\2\2\u037a\u037e\7Q\2\2\u037b"+
		"\u037e\7Y\2\2\u037c\u037e\7R\2\2\u037d\u0351\3\2\2\2\u037d\u0352\3\2\2"+
		"\2\u037d\u0353\3\2\2\2\u037d\u0354\3\2\2\2\u037d\u0355\3\2\2\2\u037d\u0356"+
		"\3\2\2\2\u037d\u0357\3\2\2\2\u037d\u0358\3\2\2\2\u037d\u0359\3\2\2\2\u037d"+
		"\u035a\3\2\2\2\u037d\u035b\3\2\2\2\u037d\u035c\3\2\2\2\u037d\u035d\3\2"+
		"\2\2\u037d\u035e\3\2\2\2\u037d\u035f\3\2\2\2\u037d\u0360\3\2\2\2\u037d"+
		"\u0361\3\2\2\2\u037d\u0362\3\2\2\2\u037d\u0363\3\2\2\2\u037d\u0364\3\2"+
		"\2\2\u037d\u0365\3\2\2\2\u037d\u0366\3\2\2\2\u037d\u0367\3\2\2\2\u037d"+
		"\u0368\3\2\2\2\u037d\u0369\3\2\2\2\u037d\u036a\3\2\2\2\u037d\u036b\3\2"+
		"\2\2\u037d\u036c\3\2\2\2\u037d\u036d\3\2\2\2\u037d\u036e\3\2\2\2\u037d"+
		"\u036f\3\2\2\2\u037d\u0370\3\2\2\2\u037d\u0371\3\2\2\2\u037d\u0372\3\2"+
		"\2\2\u037d\u0373\3\2\2\2\u037d\u0374\3\2\2\2\u037d\u0375\3\2\2\2\u037d"+
		"\u0376\3\2\2\2\u037d\u0377\3\2\2\2\u037d\u0378\3\2\2\2\u037d\u0379\3\2"+
		"\2\2\u037d\u037a\3\2\2\2\u037d\u037b\3\2\2\2\u037d\u037c\3\2\2\2\u037e"+
		"\u00a3\3\2\2\2\\\u00a9\u00ad\u00bf\u00c5\u00ca\u00d0\u00e6\u00eb\u00f3"+
		"\u00fd\u0106\u0109\u0110\u0117\u0119\u011f\u0124\u0129\u0130\u0136\u013d"+
		"\u0141\u0149\u0153\u0157\u015f\u0161\u016d\u0173\u0177\u017b\u0186\u018c"+
		"\u019b\u01a1\u01a5\u01a9\u01b0\u01b7\u01bd\u01c2\u01c4\u01c8\u01cf\u01d6"+
		"\u01df\u01eb\u01f5\u0201\u0205\u020e\u0215\u0233\u023b\u023f\u0246\u024d"+
		"\u0253\u025a\u0262\u0269\u026f\u0275\u027b\u0280\u028a\u0292\u0294\u02ab"+
		"\u02b8\u02bd\u02c3\u02d6\u02d9\u02dd\u02e4\u02e8\u02ef\u02f7\u02f9\u0301"+
		"\u0304\u030b\u0310\u0324\u0333\u033e\u0342\u0349\u037d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}