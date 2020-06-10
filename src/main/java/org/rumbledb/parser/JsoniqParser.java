// Generated from ./src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.7

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
		T__73=74, T__74=75, T__75=76, Kfor=77, Klet=78, Kwhere=79, Kgroup=80, 
		Kby=81, Korder=82, Kreturn=83, Kif=84, Kin=85, Kas=86, Kat=87, Kallowing=88, 
		Kempty=89, Kcount=90, Kstable=91, Kascending=92, Kdescending=93, Ksome=94, 
		Kevery=95, Ksatisfies=96, Kcollation=97, Kgreatest=98, Kleast=99, Kswitch=100, 
		Kcase=101, Ktry=102, Kcatch=103, Kdefault=104, Kthen=105, Kelse=106, Ktypeswitch=107, 
		Kor=108, Kand=109, Knot=110, Kto=111, Kinstance=112, Kof=113, Ktreat=114, 
		Kcast=115, Kcastable=116, Kversion=117, Kjsoniq=118, Kjson=119, STRING=120, 
		ArgumentPlaceholder=121, NullLiteral=122, Literal=123, NumericLiteral=124, 
		BooleanLiteral=125, IntegerLiteral=126, DecimalLiteral=127, DoubleLiteral=128, 
		WS=129, NCName=130, XQComment=131, ContentChar=132;
	public static final int
		RULE_moduleAndThisIsIt = 0, RULE_module = 1, RULE_mainModule = 2, RULE_libraryModule = 3, 
		RULE_prolog = 4, RULE_defaultCollationDecl = 5, RULE_orderingModeDecl = 6, 
		RULE_emptyOrderDecl = 7, RULE_decimalFormatDecl = 8, RULE_dfPropertyName = 9, 
		RULE_moduleImport = 10, RULE_varDecl = 11, RULE_functionDecl = 12, RULE_paramList = 13, 
		RULE_param = 14, RULE_expr = 15, RULE_exprSingle = 16, RULE_flowrExpr = 17, 
		RULE_forClause = 18, RULE_forVar = 19, RULE_letClause = 20, RULE_letVar = 21, 
		RULE_whereClause = 22, RULE_groupByClause = 23, RULE_groupByVar = 24, 
		RULE_orderByClause = 25, RULE_orderByExpr = 26, RULE_countClause = 27, 
		RULE_quantifiedExpr = 28, RULE_quantifiedExprVar = 29, RULE_switchExpr = 30, 
		RULE_switchCaseClause = 31, RULE_typeSwitchExpr = 32, RULE_caseClause = 33, 
		RULE_ifExpr = 34, RULE_tryCatchExpr = 35, RULE_orExpr = 36, RULE_andExpr = 37, 
		RULE_notExpr = 38, RULE_comparisonExpr = 39, RULE_stringConcatExpr = 40, 
		RULE_rangeExpr = 41, RULE_additiveExpr = 42, RULE_multiplicativeExpr = 43, 
		RULE_instanceOfExpr = 44, RULE_treatExpr = 45, RULE_castableExpr = 46, 
		RULE_castExpr = 47, RULE_arrowExpr = 48, RULE_unaryExpr = 49, RULE_simpleMapExpr = 50, 
		RULE_postFixExpr = 51, RULE_arrayLookup = 52, RULE_arrayUnboxing = 53, 
		RULE_predicate = 54, RULE_objectLookup = 55, RULE_primaryExpr = 56, RULE_varRef = 57, 
		RULE_parenthesizedExpr = 58, RULE_contextItemExpr = 59, RULE_orderedExpr = 60, 
		RULE_unorderedExpr = 61, RULE_functionCall = 62, RULE_argumentList = 63, 
		RULE_argument = 64, RULE_functionItemExpr = 65, RULE_namedFunctionRef = 66, 
		RULE_inlineFunctionExpr = 67, RULE_sequenceType = 68, RULE_objectConstructor = 69, 
		RULE_itemType = 70, RULE_jSONItemTest = 71, RULE_keyWordString = 72, RULE_keyWordInteger = 73, 
		RULE_keyWordDecimal = 74, RULE_keyWordDouble = 75, RULE_keyWordBoolean = 76, 
		RULE_keyWordDuration = 77, RULE_keyWordYearMonthDuration = 78, RULE_keyWordDayTimeDuration = 79, 
		RULE_keyWordHexBinary = 80, RULE_keyWordBase64Binary = 81, RULE_keyWordDateTime = 82, 
		RULE_keyWordDate = 83, RULE_keyWordTime = 84, RULE_keyWordAnyURI = 85, 
		RULE_typesKeywords = 86, RULE_singleType = 87, RULE_atomicType = 88, RULE_nCNameOrKeyWord = 89, 
		RULE_pairConstructor = 90, RULE_arrayConstructor = 91, RULE_uriLiteral = 92, 
		RULE_stringLiteral = 93, RULE_keyWords = 94;
	public static final String[] ruleNames = {
		"moduleAndThisIsIt", "module", "mainModule", "libraryModule", "prolog", 
		"defaultCollationDecl", "orderingModeDecl", "emptyOrderDecl", "decimalFormatDecl", 
		"dfPropertyName", "moduleImport", "varDecl", "functionDecl", "paramList", 
		"param", "expr", "exprSingle", "flowrExpr", "forClause", "forVar", "letClause", 
		"letVar", "whereClause", "groupByClause", "groupByVar", "orderByClause", 
		"orderByExpr", "countClause", "quantifiedExpr", "quantifiedExprVar", "switchExpr", 
		"switchCaseClause", "typeSwitchExpr", "caseClause", "ifExpr", "tryCatchExpr", 
		"orExpr", "andExpr", "notExpr", "comparisonExpr", "stringConcatExpr", 
		"rangeExpr", "additiveExpr", "multiplicativeExpr", "instanceOfExpr", "treatExpr", 
		"castableExpr", "castExpr", "arrowExpr", "unaryExpr", "simpleMapExpr", 
		"postFixExpr", "arrayLookup", "arrayUnboxing", "predicate", "objectLookup", 
		"primaryExpr", "varRef", "parenthesizedExpr", "contextItemExpr", "orderedExpr", 
		"unorderedExpr", "functionCall", "argumentList", "argument", "functionItemExpr", 
		"namedFunctionRef", "inlineFunctionExpr", "sequenceType", "objectConstructor", 
		"itemType", "jSONItemTest", "keyWordString", "keyWordInteger", "keyWordDecimal", 
		"keyWordDouble", "keyWordBoolean", "keyWordDuration", "keyWordYearMonthDuration", 
		"keyWordDayTimeDuration", "keyWordHexBinary", "keyWordBase64Binary", "keyWordDateTime", 
		"keyWordDate", "keyWordTime", "keyWordAnyURI", "typesKeywords", "singleType", 
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
		"'idiv'", "'mod'", "'!'", "'['", "']'", "'.'", "'$$'", "'#'", "'{|'", 
		"'|}'", "'item'", "'object'", "'array'", "'string'", "'integer'", "'decimal'", 
		"'double'", "'boolean'", "'duration'", "'yearMonthDuration'", "'dayTimeDuration'", 
		"'hexBinary'", "'base64Binary'", "'dateTime'", "'date'", "'time'", "'anyURI'", 
		"'atomic'", "'for'", "'let'", "'where'", "'group'", "'by'", "'order'", 
		"'return'", "'if'", "'in'", "'as'", "'at'", "'allowing'", "'empty'", "'count'", 
		"'stable'", "'ascending'", "'descending'", "'some'", "'every'", "'satisfies'", 
		"'collation'", "'greatest'", "'least'", "'switch'", "'case'", "'try'", 
		"'catch'", "'default'", "'then'", "'else'", "'typeswitch'", "'or'", "'and'", 
		"'not'", "'to'", "'instance'", "'of'", "'treat'", "'cast'", "'castable'", 
		"'version'", "'jsoniq'", "'json-item'", null, "'?'", "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, "Kfor", "Klet", "Kwhere", "Kgroup", "Kby", 
		"Korder", "Kreturn", "Kif", "Kin", "Kas", "Kat", "Kallowing", "Kempty", 
		"Kcount", "Kstable", "Kascending", "Kdescending", "Ksome", "Kevery", "Ksatisfies", 
		"Kcollation", "Kgreatest", "Kleast", "Kswitch", "Kcase", "Ktry", "Kcatch", 
		"Kdefault", "Kthen", "Kelse", "Ktypeswitch", "Kor", "Kand", "Knot", "Kto", 
		"Kinstance", "Kof", "Ktreat", "Kcast", "Kcastable", "Kversion", "Kjsoniq", 
		"Kjson", "STRING", "ArgumentPlaceholder", "NullLiteral", "Literal", "NumericLiteral", 
		"BooleanLiteral", "IntegerLiteral", "DecimalLiteral", "DoubleLiteral", 
		"WS", "NCName", "XQComment", "ContentChar"
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
			setState(190);
			module();
			setState(191);
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
			setState(198);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(193);
				match(Kjsoniq);
				setState(194);
				match(Kversion);
				setState(195);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(196);
				match(T__0);
				}
				break;
			}
			setState(202);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(200);
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
			case T__56:
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
				setState(201);
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
		enterRule(_localctx, 4, RULE_mainModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			prolog();
			setState(205);
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
		enterRule(_localctx, 6, RULE_libraryModule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(T__1);
			setState(208);
			match(T__2);
			setState(209);
			match(NCName);
			setState(210);
			match(T__3);
			setState(211);
			uriLiteral();
			setState(212);
			match(T__0);
			setState(213);
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
		enterRule(_localctx, 8, RULE_prolog);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(220);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(215);
						defaultCollationDecl();
						}
						break;
					case 2:
						{
						setState(216);
						orderingModeDecl();
						}
						break;
					case 3:
						{
						setState(217);
						emptyOrderDecl();
						}
						break;
					case 4:
						{
						setState(218);
						decimalFormatDecl();
						}
						break;
					case 5:
						{
						setState(219);
						moduleImport();
						}
						break;
					}
					setState(222);
					match(T__0);
					}
					} 
				}
				setState(228);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(231);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(229);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(230);
					varDecl();
					}
					break;
				}
				setState(233);
				match(T__0);
				}
				}
				setState(239);
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
		enterRule(_localctx, 10, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			match(T__4);
			setState(241);
			match(Kdefault);
			setState(242);
			match(Kcollation);
			setState(243);
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
		enterRule(_localctx, 12, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(T__4);
			setState(246);
			match(T__5);
			setState(247);
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
		enterRule(_localctx, 14, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(T__4);
			setState(250);
			match(Kdefault);
			setState(251);
			match(Korder);
			setState(252);
			match(Kempty);
			setState(253);
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
		enterRule(_localctx, 16, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			match(T__4);
			setState(264);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				{
				setState(256);
				match(T__8);
				setState(259);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(257);
					match(NCName);
					setState(258);
					match(T__9);
					}
					break;
				}
				setState(261);
				match(NCName);
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(262);
				match(Kdefault);
				setState(263);
				match(T__8);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(272);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) {
				{
				{
				setState(266);
				dfPropertyName();
				setState(267);
				match(T__3);
				setState(268);
				stringLiteral();
				}
				}
				setState(274);
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
		enterRule(_localctx, 18, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(275);
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
		enterRule(_localctx, 20, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			match(T__20);
			setState(278);
			match(T__1);
			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(279);
				match(T__2);
				setState(280);
				match(NCName);
				setState(281);
				match(T__3);
				}
			}

			setState(284);
			uriLiteral();
			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(285);
				match(Kat);
				setState(286);
				uriLiteral();
				setState(291);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__21) {
					{
					{
					setState(287);
					match(T__21);
					setState(288);
					uriLiteral();
					}
					}
					setState(293);
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
		enterRule(_localctx, 22, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(T__4);
			setState(297);
			match(T__22);
			setState(298);
			varRef();
			setState(301);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(299);
				match(Kas);
				setState(300);
				sequenceType();
				}
			}

			setState(310);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				{
				{
				setState(303);
				match(T__23);
				setState(304);
				exprSingle();
				}
				}
				break;
			case T__24:
				{
				{
				setState(305);
				((VarDeclContext)_localctx).external = match(T__24);
				setState(308);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__23) {
					{
					setState(306);
					match(T__23);
					setState(307);
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
		enterRule(_localctx, 24, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			match(T__4);
			setState(313);
			match(T__25);
			setState(316);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(314);
				((FunctionDeclContext)_localctx).namespace = match(NCName);
				setState(315);
				match(T__9);
				}
				break;
			}
			setState(318);
			((FunctionDeclContext)_localctx).fn_name = match(NCName);
			setState(319);
			match(T__26);
			setState(321);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(320);
				paramList();
				}
			}

			setState(323);
			match(T__27);
			setState(326);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(324);
				match(Kas);
				setState(325);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(333);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				{
				setState(328);
				match(T__28);
				setState(329);
				((FunctionDeclContext)_localctx).fn_body = expr();
				setState(330);
				match(T__29);
				}
				break;
			case T__24:
				{
				setState(332);
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
		enterRule(_localctx, 26, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			param();
			setState(340);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(336);
				match(T__21);
				setState(337);
				param();
				}
				}
				setState(342);
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
		enterRule(_localctx, 28, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			match(T__30);
			setState(344);
			match(NCName);
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(345);
				match(Kas);
				setState(346);
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
		enterRule(_localctx, 30, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(349);
			exprSingle();
			setState(354);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(350);
				match(T__21);
				setState(351);
				exprSingle();
				}
				}
				setState(356);
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
		enterRule(_localctx, 32, RULE_exprSingle);
		try {
			setState(364);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(357);
				flowrExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(358);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(359);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(360);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(361);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(362);
				tryCatchExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(363);
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
		enterRule(_localctx, 34, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(366);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(367);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(378);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (Kfor - 77)) | (1L << (Klet - 77)) | (1L << (Kwhere - 77)) | (1L << (Kgroup - 77)) | (1L << (Korder - 77)) | (1L << (Kcount - 77)) | (1L << (Kstable - 77)))) != 0)) {
				{
				setState(376);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(370);
					forClause();
					}
					break;
				case Kwhere:
					{
					setState(371);
					whereClause();
					}
					break;
				case Klet:
					{
					setState(372);
					letClause();
					}
					break;
				case Kgroup:
					{
					setState(373);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(374);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(375);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(380);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(381);
			match(Kreturn);
			setState(382);
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
		enterRule(_localctx, 36, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			match(Kfor);
			setState(385);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(390);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(386);
				match(T__21);
				setState(387);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(392);
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
		enterRule(_localctx, 38, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(396);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(394);
				match(Kas);
				setState(395);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(398);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(399);
				match(Kempty);
				}
			}

			setState(404);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(402);
				match(Kat);
				setState(403);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(406);
			match(Kin);
			setState(407);
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
		enterRule(_localctx, 40, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			match(Klet);
			setState(410);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(415);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(411);
				match(T__21);
				setState(412);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(417);
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
		enterRule(_localctx, 42, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(421);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(419);
				match(Kas);
				setState(420);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(423);
			match(T__23);
			setState(424);
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
		enterRule(_localctx, 44, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			match(Kwhere);
			setState(427);
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
		enterRule(_localctx, 46, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(429);
			match(Kgroup);
			setState(430);
			match(Kby);
			setState(431);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(436);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(432);
				match(T__21);
				setState(433);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(438);
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
		enterRule(_localctx, 48, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(446);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23 || _la==Kas) {
				{
				setState(442);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(440);
					match(Kas);
					setState(441);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(444);
				((GroupByVarContext)_localctx).decl = match(T__23);
				setState(445);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(450);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(448);
				match(Kcollation);
				setState(449);
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
		enterRule(_localctx, 50, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(457);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(452);
				match(Korder);
				setState(453);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(454);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(455);
				match(Korder);
				setState(456);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(459);
			orderByExpr();
			setState(464);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(460);
				match(T__21);
				setState(461);
				orderByExpr();
				}
				}
				setState(466);
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
		enterRule(_localctx, 52, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(470);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(468);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(469);
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
			setState(477);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(472);
				match(Kempty);
				setState(475);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(473);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(474);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(481);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(479);
				match(Kcollation);
				setState(480);
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
		enterRule(_localctx, 54, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			match(Kcount);
			setState(484);
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
		enterRule(_localctx, 56, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(488);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(486);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(487);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(490);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(495);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(491);
				match(T__21);
				setState(492);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(497);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(498);
			match(Ksatisfies);
			setState(499);
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
		enterRule(_localctx, 58, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
			varRef();
			setState(504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(502);
				match(Kas);
				setState(503);
				sequenceType();
				}
			}

			setState(506);
			match(Kin);
			setState(507);
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
		enterRule(_localctx, 60, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			match(Kswitch);
			setState(510);
			match(T__26);
			setState(511);
			((SwitchExprContext)_localctx).cond = expr();
			setState(512);
			match(T__27);
			setState(514); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(513);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(516); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(518);
			match(Kdefault);
			setState(519);
			match(Kreturn);
			setState(520);
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
		enterRule(_localctx, 62, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(524); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(522);
				match(Kcase);
				setState(523);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(526); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(528);
			match(Kreturn);
			setState(529);
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
		enterRule(_localctx, 64, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			match(Ktypeswitch);
			setState(532);
			match(T__26);
			setState(533);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(534);
			match(T__27);
			setState(536); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(535);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(538); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(540);
			match(Kdefault);
			setState(542);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(541);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(544);
			match(Kreturn);
			setState(545);
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
		enterRule(_localctx, 66, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547);
			match(Kcase);
			setState(551);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(548);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(549);
				match(Kas);
				}
			}

			setState(553);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(558);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__31) {
				{
				{
				setState(554);
				match(T__31);
				setState(555);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(560);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(561);
			match(Kreturn);
			setState(562);
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
		enterRule(_localctx, 68, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(564);
			match(Kif);
			setState(565);
			match(T__26);
			setState(566);
			((IfExprContext)_localctx).test_condition = expr();
			setState(567);
			match(T__27);
			setState(568);
			match(Kthen);
			setState(569);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(570);
			match(Kelse);
			setState(571);
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
		enterRule(_localctx, 70, RULE_tryCatchExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(573);
			match(Ktry);
			setState(574);
			match(T__28);
			setState(575);
			expr();
			setState(576);
			match(T__29);
			setState(577);
			match(Kcatch);
			setState(578);
			match(T__32);
			setState(579);
			match(T__28);
			setState(580);
			expr();
			setState(581);
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
		enterRule(_localctx, 72, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(583);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(588);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,51,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(584);
					match(Kor);
					setState(585);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(590);
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
		enterRule(_localctx, 74, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(591);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(596);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(592);
					match(Kand);
					setState(593);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(598);
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
		enterRule(_localctx, 76, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(600);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(599);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(602);
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
		public Token _tset1060;
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
		enterRule(_localctx, 78, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(604);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(607);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) {
				{
				setState(605);
				((ComparisonExprContext)_localctx)._tset1060 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1060 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1060);
				setState(606);
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
		enterRule(_localctx, 80, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(609);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(614);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44) {
				{
				{
				setState(610);
				match(T__44);
				setState(611);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(616);
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
		enterRule(_localctx, 82, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(617);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(620);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(618);
				match(Kto);
				setState(619);
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
		public Token _tset1169;
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
		enterRule(_localctx, 84, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(622);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(627);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(623);
					((AdditiveExprContext)_localctx)._tset1169 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__45 || _la==T__46) ) {
						((AdditiveExprContext)_localctx)._tset1169 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1169);
					setState(624);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(629);
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
		public Token _tset1197;
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
		enterRule(_localctx, 86, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(630);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(635);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) {
				{
				{
				setState(631);
				((MultiplicativeExprContext)_localctx)._tset1197 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1197 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1197);
				setState(632);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(637);
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
		enterRule(_localctx, 88, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(638);
			((InstanceOfExprContext)_localctx).main_expr = treatExpr();
			setState(642);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(639);
				match(Kinstance);
				setState(640);
				match(Kof);
				setState(641);
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
		enterRule(_localctx, 90, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(644);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(648);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(645);
				match(Ktreat);
				setState(646);
				match(Kas);
				setState(647);
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
		enterRule(_localctx, 92, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(650);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(654);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(651);
				match(Kcastable);
				setState(652);
				match(Kas);
				setState(653);
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
		enterRule(_localctx, 94, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(656);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(660);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(657);
				match(Kcast);
				setState(658);
				match(Kas);
				setState(659);
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
		public FunctionCallContext functionCall;
		public List<FunctionCallContext> function_call_expr = new ArrayList<FunctionCallContext>();
		public UnaryExprContext unaryExpr() {
			return getRuleContext(UnaryExprContext.class,0);
		}
		public List<FunctionCallContext> functionCall() {
			return getRuleContexts(FunctionCallContext.class);
		}
		public FunctionCallContext functionCall(int i) {
			return getRuleContext(FunctionCallContext.class,i);
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
		enterRule(_localctx, 96, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(662);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(669);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(663);
					match(T__3);
					setState(664);
					match(T__42);
					}
					setState(666);
					((ArrowExprContext)_localctx).functionCall = functionCall();
					((ArrowExprContext)_localctx).function_call_expr.add(((ArrowExprContext)_localctx).functionCall);
					}
					} 
				}
				setState(671);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public Token _tset1336;
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
		enterRule(_localctx, 98, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(675);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45 || _la==T__46) {
				{
				{
				setState(672);
				((UnaryExprContext)_localctx)._tset1336 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__45 || _la==T__46) ) {
					((UnaryExprContext)_localctx)._tset1336 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1336);
				}
				}
				setState(677);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(678);
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
		enterRule(_localctx, 100, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(680);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(685);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__50) {
				{
				{
				setState(681);
				match(T__50);
				setState(682);
				((SimpleMapExprContext)_localctx).postFixExpr = postFixExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).postFixExpr);
				}
				}
				setState(687);
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
		enterRule(_localctx, 102, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(688);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(696);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(694);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
					case 1:
						{
						setState(689);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(690);
						predicate();
						}
						break;
					case 3:
						{
						setState(691);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(692);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(693);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(698);
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
		enterRule(_localctx, 104, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(699);
			match(T__51);
			setState(700);
			match(T__51);
			setState(701);
			expr();
			setState(702);
			match(T__52);
			setState(703);
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
		enterRule(_localctx, 106, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(705);
			match(T__51);
			setState(706);
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
		enterRule(_localctx, 108, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(708);
			match(T__51);
			setState(709);
			expr();
			setState(710);
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
		enterRule(_localctx, 110, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(712);
			match(T__53);
			setState(720);
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
				setState(713);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(714);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(715);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__26:
				{
				setState(716);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__30:
				{
				setState(717);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__54:
				{
				setState(718);
				((ObjectLookupContext)_localctx).ci = contextItemExpr();
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
			case T__73:
			case T__74:
				{
				setState(719);
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
		enterRule(_localctx, 112, RULE_primaryExpr);
		try {
			setState(734);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(722);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(723);
				match(Literal);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(724);
				stringLiteral();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(725);
				varRef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(726);
				parenthesizedExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(727);
				contextItemExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(728);
				objectConstructor();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(729);
				functionCall();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(730);
				orderedExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(731);
				unorderedExpr();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(732);
				arrayConstructor();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(733);
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
		enterRule(_localctx, 114, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(736);
			match(T__30);
			setState(739);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(737);
				((VarRefContext)_localctx).ns = match(NCName);
				setState(738);
				match(T__9);
				}
				break;
			}
			setState(741);
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
		enterRule(_localctx, 116, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(743);
			match(T__26);
			setState(745);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__9 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__56 - 7)) | (1L << (T__61 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)) | (1L << (T__74 - 71)) | (1L << (Kfor - 71)) | (1L << (Klet - 71)) | (1L << (Kwhere - 71)) | (1L << (Kgroup - 71)) | (1L << (Kby - 71)) | (1L << (Korder - 71)) | (1L << (Kreturn - 71)) | (1L << (Kif - 71)) | (1L << (Kin - 71)) | (1L << (Kas - 71)) | (1L << (Kat - 71)) | (1L << (Kallowing - 71)) | (1L << (Kempty - 71)) | (1L << (Kcount - 71)) | (1L << (Kstable - 71)) | (1L << (Kascending - 71)) | (1L << (Kdescending - 71)) | (1L << (Ksome - 71)) | (1L << (Kevery - 71)) | (1L << (Ksatisfies - 71)) | (1L << (Kcollation - 71)) | (1L << (Kgreatest - 71)) | (1L << (Kleast - 71)) | (1L << (Kswitch - 71)) | (1L << (Kcase - 71)) | (1L << (Ktry - 71)) | (1L << (Kcatch - 71)) | (1L << (Kdefault - 71)) | (1L << (Kthen - 71)) | (1L << (Kelse - 71)) | (1L << (Ktypeswitch - 71)) | (1L << (Kor - 71)) | (1L << (Kand - 71)) | (1L << (Knot - 71)) | (1L << (Kto - 71)) | (1L << (Kinstance - 71)) | (1L << (Kof - 71)) | (1L << (Ktreat - 71)) | (1L << (Kcast - 71)) | (1L << (Kcastable - 71)) | (1L << (Kversion - 71)) | (1L << (Kjsoniq - 71)) | (1L << (Kjson - 71)) | (1L << (STRING - 71)) | (1L << (NullLiteral - 71)) | (1L << (Literal - 71)) | (1L << (NCName - 71)))) != 0)) {
				{
				setState(744);
				expr();
				}
			}

			setState(747);
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
		enterRule(_localctx, 118, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(749);
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
		enterRule(_localctx, 120, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(751);
			match(T__6);
			setState(752);
			match(T__28);
			setState(753);
			expr();
			setState(754);
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
		enterRule(_localctx, 122, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(756);
			match(T__7);
			setState(757);
			match(T__28);
			setState(758);
			expr();
			setState(759);
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
		enterRule(_localctx, 124, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(767);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(764);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(761);
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
					setState(762);
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
				setState(766);
				match(T__9);
				}
				break;
			}
			setState(771);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			case NCName:
				{
				setState(769);
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
				setState(770);
				((FunctionCallContext)_localctx).kw = keyWords();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(773);
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
		enterRule(_localctx, 126, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(775);
			match(T__26);
			setState(782);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__9 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__56 - 7)) | (1L << (T__61 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)) | (1L << (T__74 - 71)) | (1L << (Kfor - 71)) | (1L << (Klet - 71)) | (1L << (Kwhere - 71)) | (1L << (Kgroup - 71)) | (1L << (Kby - 71)) | (1L << (Korder - 71)) | (1L << (Kreturn - 71)) | (1L << (Kif - 71)) | (1L << (Kin - 71)) | (1L << (Kas - 71)) | (1L << (Kat - 71)) | (1L << (Kallowing - 71)) | (1L << (Kempty - 71)) | (1L << (Kcount - 71)) | (1L << (Kstable - 71)) | (1L << (Kascending - 71)) | (1L << (Kdescending - 71)) | (1L << (Ksome - 71)) | (1L << (Kevery - 71)) | (1L << (Ksatisfies - 71)) | (1L << (Kcollation - 71)) | (1L << (Kgreatest - 71)) | (1L << (Kleast - 71)) | (1L << (Kswitch - 71)) | (1L << (Kcase - 71)) | (1L << (Ktry - 71)) | (1L << (Kcatch - 71)) | (1L << (Kdefault - 71)) | (1L << (Kthen - 71)) | (1L << (Kelse - 71)) | (1L << (Ktypeswitch - 71)) | (1L << (Kor - 71)) | (1L << (Kand - 71)) | (1L << (Knot - 71)) | (1L << (Kto - 71)) | (1L << (Kinstance - 71)) | (1L << (Kof - 71)) | (1L << (Ktreat - 71)) | (1L << (Kcast - 71)) | (1L << (Kcastable - 71)) | (1L << (Kversion - 71)) | (1L << (Kjsoniq - 71)) | (1L << (Kjson - 71)) | (1L << (STRING - 71)) | (1L << (ArgumentPlaceholder - 71)) | (1L << (NullLiteral - 71)) | (1L << (Literal - 71)) | (1L << (NCName - 71)))) != 0)) {
				{
				{
				setState(776);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(778);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__21) {
					{
					setState(777);
					match(T__21);
					}
				}

				}
				}
				setState(784);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(785);
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
		enterRule(_localctx, 128, RULE_argument);
		try {
			setState(789);
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
			case T__56:
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
				setState(787);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(788);
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
		enterRule(_localctx, 130, RULE_functionItemExpr);
		try {
			setState(793);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(791);
				namedFunctionRef();
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 2);
				{
				setState(792);
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
		enterRule(_localctx, 132, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(795);
			((NamedFunctionRefContext)_localctx).fn_name = match(NCName);
			setState(796);
			match(T__55);
			setState(797);
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
		enterRule(_localctx, 134, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(799);
			match(T__25);
			setState(800);
			match(T__26);
			setState(802);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(801);
				paramList();
				}
			}

			setState(804);
			match(T__27);
			setState(807);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(805);
				match(Kas);
				setState(806);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(809);
			match(T__28);
			setState(810);
			((InlineFunctionExprContext)_localctx).fn_body = expr();
			setState(811);
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
		public Token s121;
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
		enterRule(_localctx, 136, RULE_sequenceType);
		try {
			setState(821);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
				enterOuterAlt(_localctx, 1);
				{
				setState(813);
				match(T__26);
				setState(814);
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
			case T__73:
			case T__74:
			case T__75:
			case Kjson:
			case NullLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(815);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(819);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
				case 1:
					{
					setState(816);
					((SequenceTypeContext)_localctx).s121 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s121);
					}
					break;
				case 2:
					{
					setState(817);
					((SequenceTypeContext)_localctx).s33 = match(T__32);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s33);
					}
					break;
				case 3:
					{
					setState(818);
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
		enterRule(_localctx, 138, RULE_objectConstructor);
		int _la;
		try {
			setState(839);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(823);
				match(T__28);
				setState(832);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__9 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__56 - 7)) | (1L << (T__61 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)) | (1L << (T__74 - 71)) | (1L << (Kfor - 71)) | (1L << (Klet - 71)) | (1L << (Kwhere - 71)) | (1L << (Kgroup - 71)) | (1L << (Kby - 71)) | (1L << (Korder - 71)) | (1L << (Kreturn - 71)) | (1L << (Kif - 71)) | (1L << (Kin - 71)) | (1L << (Kas - 71)) | (1L << (Kat - 71)) | (1L << (Kallowing - 71)) | (1L << (Kempty - 71)) | (1L << (Kcount - 71)) | (1L << (Kstable - 71)) | (1L << (Kascending - 71)) | (1L << (Kdescending - 71)) | (1L << (Ksome - 71)) | (1L << (Kevery - 71)) | (1L << (Ksatisfies - 71)) | (1L << (Kcollation - 71)) | (1L << (Kgreatest - 71)) | (1L << (Kleast - 71)) | (1L << (Kswitch - 71)) | (1L << (Kcase - 71)) | (1L << (Ktry - 71)) | (1L << (Kcatch - 71)) | (1L << (Kdefault - 71)) | (1L << (Kthen - 71)) | (1L << (Kelse - 71)) | (1L << (Ktypeswitch - 71)) | (1L << (Kor - 71)) | (1L << (Kand - 71)) | (1L << (Knot - 71)) | (1L << (Kto - 71)) | (1L << (Kinstance - 71)) | (1L << (Kof - 71)) | (1L << (Ktreat - 71)) | (1L << (Kcast - 71)) | (1L << (Kcastable - 71)) | (1L << (Kversion - 71)) | (1L << (Kjsoniq - 71)) | (1L << (Kjson - 71)) | (1L << (STRING - 71)) | (1L << (NullLiteral - 71)) | (1L << (Literal - 71)) | (1L << (NCName - 71)))) != 0)) {
					{
					setState(824);
					pairConstructor();
					setState(829);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__21) {
						{
						{
						setState(825);
						match(T__21);
						setState(826);
						pairConstructor();
						}
						}
						setState(831);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(834);
				match(T__29);
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 2);
				{
				setState(835);
				((ObjectConstructorContext)_localctx).s57 = match(T__56);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s57);
				setState(836);
				expr();
				setState(837);
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
		enterRule(_localctx, 140, RULE_itemType);
		try {
			setState(844);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__58:
				enterOuterAlt(_localctx, 1);
				{
				setState(841);
				match(T__58);
				}
				break;
			case T__59:
			case T__60:
			case Kjson:
				enterOuterAlt(_localctx, 2);
				{
				setState(842);
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
			case T__73:
			case T__74:
			case T__75:
			case NullLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(843);
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
		enterRule(_localctx, 142, RULE_jSONItemTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(846);
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

	public static class KeyWordStringContext extends ParserRuleContext {
		public KeyWordStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordString; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordStringContext keyWordString() throws RecognitionException {
		KeyWordStringContext _localctx = new KeyWordStringContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_keyWordString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(848);
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

	public static class KeyWordIntegerContext extends ParserRuleContext {
		public KeyWordIntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordInteger; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordIntegerContext keyWordInteger() throws RecognitionException {
		KeyWordIntegerContext _localctx = new KeyWordIntegerContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_keyWordInteger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(850);
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

	public static class KeyWordDecimalContext extends ParserRuleContext {
		public KeyWordDecimalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordDecimal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordDecimal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordDecimalContext keyWordDecimal() throws RecognitionException {
		KeyWordDecimalContext _localctx = new KeyWordDecimalContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_keyWordDecimal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(852);
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

	public static class KeyWordDoubleContext extends ParserRuleContext {
		public KeyWordDoubleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordDouble; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordDouble(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordDoubleContext keyWordDouble() throws RecognitionException {
		KeyWordDoubleContext _localctx = new KeyWordDoubleContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_keyWordDouble);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(854);
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
		enterRule(_localctx, 152, RULE_keyWordBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(856);
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
		enterRule(_localctx, 154, RULE_keyWordDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
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
		enterRule(_localctx, 156, RULE_keyWordYearMonthDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(860);
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
		enterRule(_localctx, 158, RULE_keyWordDayTimeDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(862);
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
		enterRule(_localctx, 160, RULE_keyWordHexBinary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
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
		enterRule(_localctx, 162, RULE_keyWordBase64Binary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(866);
			match(T__70);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 164, RULE_keyWordDateTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
			match(T__71);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 166, RULE_keyWordDate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(870);
			match(T__72);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordTimeContext extends ParserRuleContext {
		public KeyWordTimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordTime; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordTimeContext keyWordTime() throws RecognitionException {
		KeyWordTimeContext _localctx = new KeyWordTimeContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_keyWordTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(872);
			match(T__73);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeyWordAnyURIContext extends ParserRuleContext {
		public KeyWordAnyURIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyWordAnyURI; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWordAnyURI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordAnyURIContext keyWordAnyURI() throws RecognitionException {
		KeyWordAnyURIContext _localctx = new KeyWordAnyURIContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_keyWordAnyURI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(874);
			match(T__74);
			}
		}
		catch (RecognitionException re) {
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
		public KeyWordStringContext keyWordString() {
			return getRuleContext(KeyWordStringContext.class,0);
		}
		public KeyWordIntegerContext keyWordInteger() {
			return getRuleContext(KeyWordIntegerContext.class,0);
		}
		public KeyWordDecimalContext keyWordDecimal() {
			return getRuleContext(KeyWordDecimalContext.class,0);
		}
		public KeyWordDoubleContext keyWordDouble() {
			return getRuleContext(KeyWordDoubleContext.class,0);
		}
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
		public KeyWordTimeContext keyWordTime() {
			return getRuleContext(KeyWordTimeContext.class,0);
		}
		public KeyWordHexBinaryContext keyWordHexBinary() {
			return getRuleContext(KeyWordHexBinaryContext.class,0);
		}
		public KeyWordBase64BinaryContext keyWordBase64Binary() {
			return getRuleContext(KeyWordBase64BinaryContext.class,0);
		}
		public KeyWordAnyURIContext keyWordAnyURI() {
			return getRuleContext(KeyWordAnyURIContext.class,0);
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
		enterRule(_localctx, 172, RULE_typesKeywords);
		try {
			setState(890);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__61:
				enterOuterAlt(_localctx, 1);
				{
				setState(876);
				keyWordString();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 2);
				{
				setState(877);
				keyWordInteger();
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 3);
				{
				setState(878);
				keyWordDecimal();
				}
				break;
			case T__64:
				enterOuterAlt(_localctx, 4);
				{
				setState(879);
				keyWordDouble();
				}
				break;
			case T__65:
				enterOuterAlt(_localctx, 5);
				{
				setState(880);
				keyWordBoolean();
				}
				break;
			case T__66:
				enterOuterAlt(_localctx, 6);
				{
				setState(881);
				keyWordDuration();
				}
				break;
			case T__67:
				enterOuterAlt(_localctx, 7);
				{
				setState(882);
				keyWordYearMonthDuration();
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 8);
				{
				setState(883);
				keyWordDayTimeDuration();
				}
				break;
			case T__71:
				enterOuterAlt(_localctx, 9);
				{
				setState(884);
				keyWordDateTime();
				}
				break;
			case T__72:
				enterOuterAlt(_localctx, 10);
				{
				setState(885);
				keyWordDate();
				}
				break;
			case T__73:
				enterOuterAlt(_localctx, 11);
				{
				setState(886);
				keyWordTime();
				}
				break;
			case T__69:
				enterOuterAlt(_localctx, 12);
				{
				setState(887);
				keyWordHexBinary();
				}
				break;
			case T__70:
				enterOuterAlt(_localctx, 13);
				{
				setState(888);
				keyWordBase64Binary();
				}
				break;
			case T__74:
				enterOuterAlt(_localctx, 14);
				{
				setState(889);
				keyWordAnyURI();
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
		public Token s121;
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
		enterRule(_localctx, 174, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(892);
			((SingleTypeContext)_localctx).item = atomicType();
			setState(894);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(893);
				((SingleTypeContext)_localctx).s121 = match(ArgumentPlaceholder);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).s121);
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
		enterRule(_localctx, 176, RULE_atomicType);
		try {
			setState(899);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__75:
				enterOuterAlt(_localctx, 1);
				{
				setState(896);
				match(T__75);
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
			case T__73:
			case T__74:
				enterOuterAlt(_localctx, 2);
				{
				setState(897);
				typesKeywords();
				}
				break;
			case NullLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(898);
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
		enterRule(_localctx, 178, RULE_nCNameOrKeyWord);
		try {
			setState(903);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(901);
				match(NCName);
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
			case T__73:
			case T__74:
				enterOuterAlt(_localctx, 2);
				{
				setState(902);
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
		enterRule(_localctx, 180, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(907);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
			case 1:
				{
				setState(905);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(906);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(909);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(910);
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
		enterRule(_localctx, 182, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(912);
			match(T__51);
			setState(914);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__9 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__56 - 7)) | (1L << (T__61 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)) | (1L << (T__74 - 71)) | (1L << (Kfor - 71)) | (1L << (Klet - 71)) | (1L << (Kwhere - 71)) | (1L << (Kgroup - 71)) | (1L << (Kby - 71)) | (1L << (Korder - 71)) | (1L << (Kreturn - 71)) | (1L << (Kif - 71)) | (1L << (Kin - 71)) | (1L << (Kas - 71)) | (1L << (Kat - 71)) | (1L << (Kallowing - 71)) | (1L << (Kempty - 71)) | (1L << (Kcount - 71)) | (1L << (Kstable - 71)) | (1L << (Kascending - 71)) | (1L << (Kdescending - 71)) | (1L << (Ksome - 71)) | (1L << (Kevery - 71)) | (1L << (Ksatisfies - 71)) | (1L << (Kcollation - 71)) | (1L << (Kgreatest - 71)) | (1L << (Kleast - 71)) | (1L << (Kswitch - 71)) | (1L << (Kcase - 71)) | (1L << (Ktry - 71)) | (1L << (Kcatch - 71)) | (1L << (Kdefault - 71)) | (1L << (Kthen - 71)) | (1L << (Kelse - 71)) | (1L << (Ktypeswitch - 71)) | (1L << (Kor - 71)) | (1L << (Kand - 71)) | (1L << (Knot - 71)) | (1L << (Kto - 71)) | (1L << (Kinstance - 71)) | (1L << (Kof - 71)) | (1L << (Ktreat - 71)) | (1L << (Kcast - 71)) | (1L << (Kcastable - 71)) | (1L << (Kversion - 71)) | (1L << (Kjsoniq - 71)) | (1L << (Kjson - 71)) | (1L << (STRING - 71)) | (1L << (NullLiteral - 71)) | (1L << (Literal - 71)) | (1L << (NCName - 71)))) != 0)) {
				{
				setState(913);
				expr();
				}
			}

			setState(916);
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
		enterRule(_localctx, 184, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(918);
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
		enterRule(_localctx, 186, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(920);
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
		enterRule(_localctx, 188, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(922);
			_la = _input.LA(1);
			if ( !(((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (Kfor - 77)) | (1L << (Klet - 77)) | (1L << (Kwhere - 77)) | (1L << (Kgroup - 77)) | (1L << (Kby - 77)) | (1L << (Korder - 77)) | (1L << (Kreturn - 77)) | (1L << (Kif - 77)) | (1L << (Kin - 77)) | (1L << (Kas - 77)) | (1L << (Kat - 77)) | (1L << (Kallowing - 77)) | (1L << (Kempty - 77)) | (1L << (Kcount - 77)) | (1L << (Kstable - 77)) | (1L << (Kascending - 77)) | (1L << (Kdescending - 77)) | (1L << (Ksome - 77)) | (1L << (Kevery - 77)) | (1L << (Ksatisfies - 77)) | (1L << (Kcollation - 77)) | (1L << (Kgreatest - 77)) | (1L << (Kleast - 77)) | (1L << (Kswitch - 77)) | (1L << (Kcase - 77)) | (1L << (Ktry - 77)) | (1L << (Kcatch - 77)) | (1L << (Kdefault - 77)) | (1L << (Kthen - 77)) | (1L << (Kelse - 77)) | (1L << (Ktypeswitch - 77)) | (1L << (Kor - 77)) | (1L << (Kand - 77)) | (1L << (Knot - 77)) | (1L << (Kto - 77)) | (1L << (Kinstance - 77)) | (1L << (Kof - 77)) | (1L << (Ktreat - 77)) | (1L << (Kcast - 77)) | (1L << (Kcastable - 77)) | (1L << (Kversion - 77)) | (1L << (Kjsoniq - 77)) | (1L << (Kjson - 77)))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0086\u039f\4\2\t"+
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
		"`\t`\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3\u00c9\n\3\3\3\3\3\5\3\u00cd\n"+
		"\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\5\6"+
		"\u00df\n\6\3\6\3\6\7\6\u00e3\n\6\f\6\16\6\u00e6\13\6\3\6\3\6\5\6\u00ea"+
		"\n\6\3\6\3\6\7\6\u00ee\n\6\f\6\16\6\u00f1\13\6\3\7\3\7\3\7\3\7\3\7\3\b"+
		"\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\5\n\u0106\n\n\3\n"+
		"\3\n\3\n\5\n\u010b\n\n\3\n\3\n\3\n\3\n\7\n\u0111\n\n\f\n\16\n\u0114\13"+
		"\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\5\f\u011d\n\f\3\f\3\f\3\f\3\f\3\f\7\f"+
		"\u0124\n\f\f\f\16\f\u0127\13\f\5\f\u0129\n\f\3\r\3\r\3\r\3\r\3\r\5\r\u0130"+
		"\n\r\3\r\3\r\3\r\3\r\3\r\5\r\u0137\n\r\5\r\u0139\n\r\3\16\3\16\3\16\3"+
		"\16\5\16\u013f\n\16\3\16\3\16\3\16\5\16\u0144\n\16\3\16\3\16\3\16\5\16"+
		"\u0149\n\16\3\16\3\16\3\16\3\16\3\16\5\16\u0150\n\16\3\17\3\17\3\17\7"+
		"\17\u0155\n\17\f\17\16\17\u0158\13\17\3\20\3\20\3\20\3\20\5\20\u015e\n"+
		"\20\3\21\3\21\3\21\7\21\u0163\n\21\f\21\16\21\u0166\13\21\3\22\3\22\3"+
		"\22\3\22\3\22\3\22\3\22\5\22\u016f\n\22\3\23\3\23\5\23\u0173\n\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\7\23\u017b\n\23\f\23\16\23\u017e\13\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\7\24\u0187\n\24\f\24\16\24\u018a\13\24"+
		"\3\25\3\25\3\25\5\25\u018f\n\25\3\25\3\25\5\25\u0193\n\25\3\25\3\25\5"+
		"\25\u0197\n\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\7\26\u01a0\n\26\f\26"+
		"\16\26\u01a3\13\26\3\27\3\27\3\27\5\27\u01a8\n\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\7\31\u01b5\n\31\f\31\16\31\u01b8\13"+
		"\31\3\32\3\32\3\32\5\32\u01bd\n\32\3\32\3\32\5\32\u01c1\n\32\3\32\3\32"+
		"\5\32\u01c5\n\32\3\33\3\33\3\33\3\33\3\33\5\33\u01cc\n\33\3\33\3\33\3"+
		"\33\7\33\u01d1\n\33\f\33\16\33\u01d4\13\33\3\34\3\34\3\34\5\34\u01d9\n"+
		"\34\3\34\3\34\3\34\5\34\u01de\n\34\5\34\u01e0\n\34\3\34\3\34\5\34\u01e4"+
		"\n\34\3\35\3\35\3\35\3\36\3\36\5\36\u01eb\n\36\3\36\3\36\3\36\7\36\u01f0"+
		"\n\36\f\36\16\36\u01f3\13\36\3\36\3\36\3\36\3\37\3\37\3\37\5\37\u01fb"+
		"\n\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \6 \u0205\n \r \16 \u0206\3 \3 \3 "+
		"\3 \3!\3!\6!\u020f\n!\r!\16!\u0210\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\6\"\u021b"+
		"\n\"\r\"\16\"\u021c\3\"\3\"\5\"\u0221\n\"\3\"\3\"\3\"\3#\3#\3#\3#\5#\u022a"+
		"\n#\3#\3#\3#\7#\u022f\n#\f#\16#\u0232\13#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3"+
		"$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\7&\u024d\n&\f&\16&\u0250"+
		"\13&\3\'\3\'\3\'\7\'\u0255\n\'\f\'\16\'\u0258\13\'\3(\5(\u025b\n(\3(\3"+
		"(\3)\3)\3)\5)\u0262\n)\3*\3*\3*\7*\u0267\n*\f*\16*\u026a\13*\3+\3+\3+"+
		"\5+\u026f\n+\3,\3,\3,\7,\u0274\n,\f,\16,\u0277\13,\3-\3-\3-\7-\u027c\n"+
		"-\f-\16-\u027f\13-\3.\3.\3.\3.\5.\u0285\n.\3/\3/\3/\3/\5/\u028b\n/\3\60"+
		"\3\60\3\60\3\60\5\60\u0291\n\60\3\61\3\61\3\61\3\61\5\61\u0297\n\61\3"+
		"\62\3\62\3\62\3\62\3\62\7\62\u029e\n\62\f\62\16\62\u02a1\13\62\3\63\7"+
		"\63\u02a4\n\63\f\63\16\63\u02a7\13\63\3\63\3\63\3\64\3\64\3\64\7\64\u02ae"+
		"\n\64\f\64\16\64\u02b1\13\64\3\65\3\65\3\65\3\65\3\65\3\65\7\65\u02b9"+
		"\n\65\f\65\16\65\u02bc\13\65\3\66\3\66\3\66\3\66\3\66\3\66\3\67\3\67\3"+
		"\67\38\38\38\38\39\39\39\39\39\39\39\39\59\u02d3\n9\3:\3:\3:\3:\3:\3:"+
		"\3:\3:\3:\3:\3:\3:\5:\u02e1\n:\3;\3;\3;\5;\u02e6\n;\3;\3;\3<\3<\5<\u02ec"+
		"\n<\3<\3<\3=\3=\3>\3>\3>\3>\3>\3?\3?\3?\3?\3?\3@\3@\3@\5@\u02ff\n@\3@"+
		"\5@\u0302\n@\3@\3@\5@\u0306\n@\3@\3@\3A\3A\3A\5A\u030d\nA\7A\u030f\nA"+
		"\fA\16A\u0312\13A\3A\3A\3B\3B\5B\u0318\nB\3C\3C\5C\u031c\nC\3D\3D\3D\3"+
		"D\3E\3E\3E\5E\u0325\nE\3E\3E\3E\5E\u032a\nE\3E\3E\3E\3E\3F\3F\3F\3F\3"+
		"F\3F\5F\u0336\nF\5F\u0338\nF\3G\3G\3G\3G\7G\u033e\nG\fG\16G\u0341\13G"+
		"\5G\u0343\nG\3G\3G\3G\3G\3G\5G\u034a\nG\3H\3H\3H\5H\u034f\nH\3I\3I\3J"+
		"\3J\3K\3K\3L\3L\3M\3M\3N\3N\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U"+
		"\3V\3V\3W\3W\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\3X\5X\u037d\nX\3Y"+
		"\3Y\5Y\u0381\nY\3Z\3Z\3Z\5Z\u0386\nZ\3[\3[\5[\u038a\n[\3\\\3\\\5\\\u038e"+
		"\n\\\3\\\3\\\3\\\3]\3]\5]\u0395\n]\3]\3]\3^\3^\3_\3_\3`\3`\3`\2\2a\2\4"+
		"\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNP"+
		"RTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e"+
		"\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6"+
		"\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be"+
		"\2\13\3\2\t\n\3\2de\3\2\r\26\4\2\6\6$.\3\2\60\61\4\2##\62\64\4\2>?yy\4"+
		"\2\f\f{{\3\2Oy\2\u03cc\2\u00c0\3\2\2\2\4\u00c8\3\2\2\2\6\u00ce\3\2\2\2"+
		"\b\u00d1\3\2\2\2\n\u00e4\3\2\2\2\f\u00f2\3\2\2\2\16\u00f7\3\2\2\2\20\u00fb"+
		"\3\2\2\2\22\u0101\3\2\2\2\24\u0115\3\2\2\2\26\u0117\3\2\2\2\30\u012a\3"+
		"\2\2\2\32\u013a\3\2\2\2\34\u0151\3\2\2\2\36\u0159\3\2\2\2 \u015f\3\2\2"+
		"\2\"\u016e\3\2\2\2$\u0172\3\2\2\2&\u0182\3\2\2\2(\u018b\3\2\2\2*\u019b"+
		"\3\2\2\2,\u01a4\3\2\2\2.\u01ac\3\2\2\2\60\u01af\3\2\2\2\62\u01b9\3\2\2"+
		"\2\64\u01cb\3\2\2\2\66\u01d5\3\2\2\28\u01e5\3\2\2\2:\u01ea\3\2\2\2<\u01f7"+
		"\3\2\2\2>\u01ff\3\2\2\2@\u020e\3\2\2\2B\u0215\3\2\2\2D\u0225\3\2\2\2F"+
		"\u0236\3\2\2\2H\u023f\3\2\2\2J\u0249\3\2\2\2L\u0251\3\2\2\2N\u025a\3\2"+
		"\2\2P\u025e\3\2\2\2R\u0263\3\2\2\2T\u026b\3\2\2\2V\u0270\3\2\2\2X\u0278"+
		"\3\2\2\2Z\u0280\3\2\2\2\\\u0286\3\2\2\2^\u028c\3\2\2\2`\u0292\3\2\2\2"+
		"b\u0298\3\2\2\2d\u02a5\3\2\2\2f\u02aa\3\2\2\2h\u02b2\3\2\2\2j\u02bd\3"+
		"\2\2\2l\u02c3\3\2\2\2n\u02c6\3\2\2\2p\u02ca\3\2\2\2r\u02e0\3\2\2\2t\u02e2"+
		"\3\2\2\2v\u02e9\3\2\2\2x\u02ef\3\2\2\2z\u02f1\3\2\2\2|\u02f6\3\2\2\2~"+
		"\u0301\3\2\2\2\u0080\u0309\3\2\2\2\u0082\u0317\3\2\2\2\u0084\u031b\3\2"+
		"\2\2\u0086\u031d\3\2\2\2\u0088\u0321\3\2\2\2\u008a\u0337\3\2\2\2\u008c"+
		"\u0349\3\2\2\2\u008e\u034e\3\2\2\2\u0090\u0350\3\2\2\2\u0092\u0352\3\2"+
		"\2\2\u0094\u0354\3\2\2\2\u0096\u0356\3\2\2\2\u0098\u0358\3\2\2\2\u009a"+
		"\u035a\3\2\2\2\u009c\u035c\3\2\2\2\u009e\u035e\3\2\2\2\u00a0\u0360\3\2"+
		"\2\2\u00a2\u0362\3\2\2\2\u00a4\u0364\3\2\2\2\u00a6\u0366\3\2\2\2\u00a8"+
		"\u0368\3\2\2\2\u00aa\u036a\3\2\2\2\u00ac\u036c\3\2\2\2\u00ae\u037c\3\2"+
		"\2\2\u00b0\u037e\3\2\2\2\u00b2\u0385\3\2\2\2\u00b4\u0389\3\2\2\2\u00b6"+
		"\u038d\3\2\2\2\u00b8\u0392\3\2\2\2\u00ba\u0398\3\2\2\2\u00bc\u039a\3\2"+
		"\2\2\u00be\u039c\3\2\2\2\u00c0\u00c1\5\4\3\2\u00c1\u00c2\7\2\2\3\u00c2"+
		"\3\3\2\2\2\u00c3\u00c4\7x\2\2\u00c4\u00c5\7w\2\2\u00c5\u00c6\5\u00bc_"+
		"\2\u00c6\u00c7\7\3\2\2\u00c7\u00c9\3\2\2\2\u00c8\u00c3\3\2\2\2\u00c8\u00c9"+
		"\3\2\2\2\u00c9\u00cc\3\2\2\2\u00ca\u00cd\5\b\5\2\u00cb\u00cd\5\6\4\2\u00cc"+
		"\u00ca\3\2\2\2\u00cc\u00cb\3\2\2\2\u00cd\5\3\2\2\2\u00ce\u00cf\5\n\6\2"+
		"\u00cf\u00d0\5 \21\2\u00d0\7\3\2\2\2\u00d1\u00d2\7\4\2\2\u00d2\u00d3\7"+
		"\5\2\2\u00d3\u00d4\7\u0084\2\2\u00d4\u00d5\7\6\2\2\u00d5\u00d6\5\u00ba"+
		"^\2\u00d6\u00d7\7\3\2\2\u00d7\u00d8\5\n\6\2\u00d8\t\3\2\2\2\u00d9\u00df"+
		"\5\f\7\2\u00da\u00df\5\16\b\2\u00db\u00df\5\20\t\2\u00dc\u00df\5\22\n"+
		"\2\u00dd\u00df\5\26\f\2\u00de\u00d9\3\2\2\2\u00de\u00da\3\2\2\2\u00de"+
		"\u00db\3\2\2\2\u00de\u00dc\3\2\2\2\u00de\u00dd\3\2\2\2\u00df\u00e0\3\2"+
		"\2\2\u00e0\u00e1\7\3\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00de\3\2\2\2\u00e3"+
		"\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00ef\3\2"+
		"\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00ea\5\32\16\2\u00e8\u00ea\5\30\r\2\u00e9"+
		"\u00e7\3\2\2\2\u00e9\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00ec\7\3"+
		"\2\2\u00ec\u00ee\3\2\2\2\u00ed\u00e9\3\2\2\2\u00ee\u00f1\3\2\2\2\u00ef"+
		"\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\13\3\2\2\2\u00f1\u00ef\3\2\2"+
		"\2\u00f2\u00f3\7\7\2\2\u00f3\u00f4\7j\2\2\u00f4\u00f5\7c\2\2\u00f5\u00f6"+
		"\5\u00ba^\2\u00f6\r\3\2\2\2\u00f7\u00f8\7\7\2\2\u00f8\u00f9\7\b\2\2\u00f9"+
		"\u00fa\t\2\2\2\u00fa\17\3\2\2\2\u00fb\u00fc\7\7\2\2\u00fc\u00fd\7j\2\2"+
		"\u00fd\u00fe\7T\2\2\u00fe\u00ff\7[\2\2\u00ff\u0100\t\3\2\2\u0100\21\3"+
		"\2\2\2\u0101\u010a\7\7\2\2\u0102\u0105\7\13\2\2\u0103\u0104\7\u0084\2"+
		"\2\u0104\u0106\7\f\2\2\u0105\u0103\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u0107"+
		"\3\2\2\2\u0107\u010b\7\u0084\2\2\u0108\u0109\7j\2\2\u0109\u010b\7\13\2"+
		"\2\u010a\u0102\3\2\2\2\u010a\u0108\3\2\2\2\u010b\u0112\3\2\2\2\u010c\u010d"+
		"\5\24\13\2\u010d\u010e\7\6\2\2\u010e\u010f\5\u00bc_\2\u010f\u0111\3\2"+
		"\2\2\u0110\u010c\3\2\2\2\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112"+
		"\u0113\3\2\2\2\u0113\23\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0116\t\4\2"+
		"\2\u0116\25\3\2\2\2\u0117\u0118\7\27\2\2\u0118\u011c\7\4\2\2\u0119\u011a"+
		"\7\5\2\2\u011a\u011b\7\u0084\2\2\u011b\u011d\7\6\2\2\u011c\u0119\3\2\2"+
		"\2\u011c\u011d\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u0128\5\u00ba^\2\u011f"+
		"\u0120\7Y\2\2\u0120\u0125\5\u00ba^\2\u0121\u0122\7\30\2\2\u0122\u0124"+
		"\5\u00ba^\2\u0123\u0121\3\2\2\2\u0124\u0127\3\2\2\2\u0125\u0123\3\2\2"+
		"\2\u0125\u0126\3\2\2\2\u0126\u0129\3\2\2\2\u0127\u0125\3\2\2\2\u0128\u011f"+
		"\3\2\2\2\u0128\u0129\3\2\2\2\u0129\27\3\2\2\2\u012a\u012b\7\7\2\2\u012b"+
		"\u012c\7\31\2\2\u012c\u012f\5t;\2\u012d\u012e\7X\2\2\u012e\u0130\5\u008a"+
		"F\2\u012f\u012d\3\2\2\2\u012f\u0130\3\2\2\2\u0130\u0138\3\2\2\2\u0131"+
		"\u0132\7\32\2\2\u0132\u0139\5\"\22\2\u0133\u0136\7\33\2\2\u0134\u0135"+
		"\7\32\2\2\u0135\u0137\5\"\22\2\u0136\u0134\3\2\2\2\u0136\u0137\3\2\2\2"+
		"\u0137\u0139\3\2\2\2\u0138\u0131\3\2\2\2\u0138\u0133\3\2\2\2\u0139\31"+
		"\3\2\2\2\u013a\u013b\7\7\2\2\u013b\u013e\7\34\2\2\u013c\u013d\7\u0084"+
		"\2\2\u013d\u013f\7\f\2\2\u013e\u013c\3\2\2\2\u013e\u013f\3\2\2\2\u013f"+
		"\u0140\3\2\2\2\u0140\u0141\7\u0084\2\2\u0141\u0143\7\35\2\2\u0142\u0144"+
		"\5\34\17\2\u0143\u0142\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u0145\3\2\2\2"+
		"\u0145\u0148\7\36\2\2\u0146\u0147\7X\2\2\u0147\u0149\5\u008aF\2\u0148"+
		"\u0146\3\2\2\2\u0148\u0149\3\2\2\2\u0149\u014f\3\2\2\2\u014a\u014b\7\37"+
		"\2\2\u014b\u014c\5 \21\2\u014c\u014d\7 \2\2\u014d\u0150\3\2\2\2\u014e"+
		"\u0150\7\33\2\2\u014f\u014a\3\2\2\2\u014f\u014e\3\2\2\2\u0150\33\3\2\2"+
		"\2\u0151\u0156\5\36\20\2\u0152\u0153\7\30\2\2\u0153\u0155\5\36\20\2\u0154"+
		"\u0152\3\2\2\2\u0155\u0158\3\2\2\2\u0156\u0154\3\2\2\2\u0156\u0157\3\2"+
		"\2\2\u0157\35\3\2\2\2\u0158\u0156\3\2\2\2\u0159\u015a\7!\2\2\u015a\u015d"+
		"\7\u0084\2\2\u015b\u015c\7X\2\2\u015c\u015e\5\u008aF\2\u015d\u015b\3\2"+
		"\2\2\u015d\u015e\3\2\2\2\u015e\37\3\2\2\2\u015f\u0164\5\"\22\2\u0160\u0161"+
		"\7\30\2\2\u0161\u0163\5\"\22\2\u0162\u0160\3\2\2\2\u0163\u0166\3\2\2\2"+
		"\u0164\u0162\3\2\2\2\u0164\u0165\3\2\2\2\u0165!\3\2\2\2\u0166\u0164\3"+
		"\2\2\2\u0167\u016f\5$\23\2\u0168\u016f\5:\36\2\u0169\u016f\5> \2\u016a"+
		"\u016f\5B\"\2\u016b\u016f\5F$\2\u016c\u016f\5H%\2\u016d\u016f\5J&\2\u016e"+
		"\u0167\3\2\2\2\u016e\u0168\3\2\2\2\u016e\u0169\3\2\2\2\u016e\u016a\3\2"+
		"\2\2\u016e\u016b\3\2\2\2\u016e\u016c\3\2\2\2\u016e\u016d\3\2\2\2\u016f"+
		"#\3\2\2\2\u0170\u0173\5&\24\2\u0171\u0173\5*\26\2\u0172\u0170\3\2\2\2"+
		"\u0172\u0171\3\2\2\2\u0173\u017c\3\2\2\2\u0174\u017b\5&\24\2\u0175\u017b"+
		"\5.\30\2\u0176\u017b\5*\26\2\u0177\u017b\5\60\31\2\u0178\u017b\5\64\33"+
		"\2\u0179\u017b\58\35\2\u017a\u0174\3\2\2\2\u017a\u0175\3\2\2\2\u017a\u0176"+
		"\3\2\2\2\u017a\u0177\3\2\2\2\u017a\u0178\3\2\2\2\u017a\u0179\3\2\2\2\u017b"+
		"\u017e\3\2\2\2\u017c\u017a\3\2\2\2\u017c\u017d\3\2\2\2\u017d\u017f\3\2"+
		"\2\2\u017e\u017c\3\2\2\2\u017f\u0180\7U\2\2\u0180\u0181\5\"\22\2\u0181"+
		"%\3\2\2\2\u0182\u0183\7O\2\2\u0183\u0188\5(\25\2\u0184\u0185\7\30\2\2"+
		"\u0185\u0187\5(\25\2\u0186\u0184\3\2\2\2\u0187\u018a\3\2\2\2\u0188\u0186"+
		"\3\2\2\2\u0188\u0189\3\2\2\2\u0189\'\3\2\2\2\u018a\u0188\3\2\2\2\u018b"+
		"\u018e\5t;\2\u018c\u018d\7X\2\2\u018d\u018f\5\u008aF\2\u018e\u018c\3\2"+
		"\2\2\u018e\u018f\3\2\2\2\u018f\u0192\3\2\2\2\u0190\u0191\7Z\2\2\u0191"+
		"\u0193\7[\2\2\u0192\u0190\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0196\3\2"+
		"\2\2\u0194\u0195\7Y\2\2\u0195\u0197\5t;\2\u0196\u0194\3\2\2\2\u0196\u0197"+
		"\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0199\7W\2\2\u0199\u019a\5\"\22\2\u019a"+
		")\3\2\2\2\u019b\u019c\7P\2\2\u019c\u01a1\5,\27\2\u019d\u019e\7\30\2\2"+
		"\u019e\u01a0\5,\27\2\u019f\u019d\3\2\2\2\u01a0\u01a3\3\2\2\2\u01a1\u019f"+
		"\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2+\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a4"+
		"\u01a7\5t;\2\u01a5\u01a6\7X\2\2\u01a6\u01a8\5\u008aF\2\u01a7\u01a5\3\2"+
		"\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01aa\7\32\2\2\u01aa"+
		"\u01ab\5\"\22\2\u01ab-\3\2\2\2\u01ac\u01ad\7Q\2\2\u01ad\u01ae\5\"\22\2"+
		"\u01ae/\3\2\2\2\u01af\u01b0\7R\2\2\u01b0\u01b1\7S\2\2\u01b1\u01b6\5\62"+
		"\32\2\u01b2\u01b3\7\30\2\2\u01b3\u01b5\5\62\32\2\u01b4\u01b2\3\2\2\2\u01b5"+
		"\u01b8\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7\61\3\2\2"+
		"\2\u01b8\u01b6\3\2\2\2\u01b9\u01c0\5t;\2\u01ba\u01bb\7X\2\2\u01bb\u01bd"+
		"\5\u008aF\2\u01bc\u01ba\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be\3\2\2"+
		"\2\u01be\u01bf\7\32\2\2\u01bf\u01c1\5\"\22\2\u01c0\u01bc\3\2\2\2\u01c0"+
		"\u01c1\3\2\2\2\u01c1\u01c4\3\2\2\2\u01c2\u01c3\7c\2\2\u01c3\u01c5\5\u00ba"+
		"^\2\u01c4\u01c2\3\2\2\2\u01c4\u01c5\3\2\2\2\u01c5\63\3\2\2\2\u01c6\u01c7"+
		"\7T\2\2\u01c7\u01cc\7S\2\2\u01c8\u01c9\7]\2\2\u01c9\u01ca\7T\2\2\u01ca"+
		"\u01cc\7S\2\2\u01cb\u01c6\3\2\2\2\u01cb\u01c8\3\2\2\2\u01cc\u01cd\3\2"+
		"\2\2\u01cd\u01d2\5\66\34\2\u01ce\u01cf\7\30\2\2\u01cf\u01d1\5\66\34\2"+
		"\u01d0\u01ce\3\2\2\2\u01d1\u01d4\3\2\2\2\u01d2\u01d0\3\2\2\2\u01d2\u01d3"+
		"\3\2\2\2\u01d3\65\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d5\u01d8\5\"\22\2\u01d6"+
		"\u01d9\7^\2\2\u01d7\u01d9\7_\2\2\u01d8\u01d6\3\2\2\2\u01d8\u01d7\3\2\2"+
		"\2\u01d8\u01d9\3\2\2\2\u01d9\u01df\3\2\2\2\u01da\u01dd\7[\2\2\u01db\u01de"+
		"\7d\2\2\u01dc\u01de\7e\2\2\u01dd\u01db\3\2\2\2\u01dd\u01dc\3\2\2\2\u01de"+
		"\u01e0\3\2\2\2\u01df\u01da\3\2\2\2\u01df\u01e0\3\2\2\2\u01e0\u01e3\3\2"+
		"\2\2\u01e1\u01e2\7c\2\2\u01e2\u01e4\5\u00ba^\2\u01e3\u01e1\3\2\2\2\u01e3"+
		"\u01e4\3\2\2\2\u01e4\67\3\2\2\2\u01e5\u01e6\7\\\2\2\u01e6\u01e7\5t;\2"+
		"\u01e79\3\2\2\2\u01e8\u01eb\7`\2\2\u01e9\u01eb\7a\2\2\u01ea\u01e8\3\2"+
		"\2\2\u01ea\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01f1\5<\37\2\u01ed"+
		"\u01ee\7\30\2\2\u01ee\u01f0\5<\37\2\u01ef\u01ed\3\2\2\2\u01f0\u01f3\3"+
		"\2\2\2\u01f1\u01ef\3\2\2\2\u01f1\u01f2\3\2\2\2\u01f2\u01f4\3\2\2\2\u01f3"+
		"\u01f1\3\2\2\2\u01f4\u01f5\7b\2\2\u01f5\u01f6\5\"\22\2\u01f6;\3\2\2\2"+
		"\u01f7\u01fa\5t;\2\u01f8\u01f9\7X\2\2\u01f9\u01fb\5\u008aF\2\u01fa\u01f8"+
		"\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01fd\7W\2\2\u01fd"+
		"\u01fe\5\"\22\2\u01fe=\3\2\2\2\u01ff\u0200\7f\2\2\u0200\u0201\7\35\2\2"+
		"\u0201\u0202\5 \21\2\u0202\u0204\7\36\2\2\u0203\u0205\5@!\2\u0204\u0203"+
		"\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207"+
		"\u0208\3\2\2\2\u0208\u0209\7j\2\2\u0209\u020a\7U\2\2\u020a\u020b\5\"\22"+
		"\2\u020b?\3\2\2\2\u020c\u020d\7g\2\2\u020d\u020f\5\"\22\2\u020e\u020c"+
		"\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u020e\3\2\2\2\u0210\u0211\3\2\2\2\u0211"+
		"\u0212\3\2\2\2\u0212\u0213\7U\2\2\u0213\u0214\5\"\22\2\u0214A\3\2\2\2"+
		"\u0215\u0216\7m\2\2\u0216\u0217\7\35\2\2\u0217\u0218\5 \21\2\u0218\u021a"+
		"\7\36\2\2\u0219\u021b\5D#\2\u021a\u0219\3\2\2\2\u021b\u021c\3\2\2\2\u021c"+
		"\u021a\3\2\2\2\u021c\u021d\3\2\2\2\u021d\u021e\3\2\2\2\u021e\u0220\7j"+
		"\2\2\u021f\u0221\5t;\2\u0220\u021f\3\2\2\2\u0220\u0221\3\2\2\2\u0221\u0222"+
		"\3\2\2\2\u0222\u0223\7U\2\2\u0223\u0224\5\"\22\2\u0224C\3\2\2\2\u0225"+
		"\u0229\7g\2\2\u0226\u0227\5t;\2\u0227\u0228\7X\2\2\u0228\u022a\3\2\2\2"+
		"\u0229\u0226\3\2\2\2\u0229\u022a\3\2\2\2\u022a\u022b\3\2\2\2\u022b\u0230"+
		"\5\u008aF\2\u022c\u022d\7\"\2\2\u022d\u022f\5\u008aF\2\u022e\u022c\3\2"+
		"\2\2\u022f\u0232\3\2\2\2\u0230\u022e\3\2\2\2\u0230\u0231\3\2\2\2\u0231"+
		"\u0233\3\2\2\2\u0232\u0230\3\2\2\2\u0233\u0234\7U\2\2\u0234\u0235\5\""+
		"\22\2\u0235E\3\2\2\2\u0236\u0237\7V\2\2\u0237\u0238\7\35\2\2\u0238\u0239"+
		"\5 \21\2\u0239\u023a\7\36\2\2\u023a\u023b\7k\2\2\u023b\u023c\5\"\22\2"+
		"\u023c\u023d\7l\2\2\u023d\u023e\5\"\22\2\u023eG\3\2\2\2\u023f\u0240\7"+
		"h\2\2\u0240\u0241\7\37\2\2\u0241\u0242\5 \21\2\u0242\u0243\7 \2\2\u0243"+
		"\u0244\7i\2\2\u0244\u0245\7#\2\2\u0245\u0246\7\37\2\2\u0246\u0247\5 \21"+
		"\2\u0247\u0248\7 \2\2\u0248I\3\2\2\2\u0249\u024e\5L\'\2\u024a\u024b\7"+
		"n\2\2\u024b\u024d\5L\'\2\u024c\u024a\3\2\2\2\u024d\u0250\3\2\2\2\u024e"+
		"\u024c\3\2\2\2\u024e\u024f\3\2\2\2\u024fK\3\2\2\2\u0250\u024e\3\2\2\2"+
		"\u0251\u0256\5N(\2\u0252\u0253\7o\2\2\u0253\u0255\5N(\2\u0254\u0252\3"+
		"\2\2\2\u0255\u0258\3\2\2\2\u0256\u0254\3\2\2\2\u0256\u0257\3\2\2\2\u0257"+
		"M\3\2\2\2\u0258\u0256\3\2\2\2\u0259\u025b\7p\2\2\u025a\u0259\3\2\2\2\u025a"+
		"\u025b\3\2\2\2\u025b\u025c\3\2\2\2\u025c\u025d\5P)\2\u025dO\3\2\2\2\u025e"+
		"\u0261\5R*\2\u025f\u0260\t\5\2\2\u0260\u0262\5R*\2\u0261\u025f\3\2\2\2"+
		"\u0261\u0262\3\2\2\2\u0262Q\3\2\2\2\u0263\u0268\5T+\2\u0264\u0265\7/\2"+
		"\2\u0265\u0267\5T+\2\u0266\u0264\3\2\2\2\u0267\u026a\3\2\2\2\u0268\u0266"+
		"\3\2\2\2\u0268\u0269\3\2\2\2\u0269S\3\2\2\2\u026a\u0268\3\2\2\2\u026b"+
		"\u026e\5V,\2\u026c\u026d\7q\2\2\u026d\u026f\5V,\2\u026e\u026c\3\2\2\2"+
		"\u026e\u026f\3\2\2\2\u026fU\3\2\2\2\u0270\u0275\5X-\2\u0271\u0272\t\6"+
		"\2\2\u0272\u0274\5X-\2\u0273\u0271\3\2\2\2\u0274\u0277\3\2\2\2\u0275\u0273"+
		"\3\2\2\2\u0275\u0276\3\2\2\2\u0276W\3\2\2\2\u0277\u0275\3\2\2\2\u0278"+
		"\u027d\5Z.\2\u0279\u027a\t\7\2\2\u027a\u027c\5Z.\2\u027b\u0279\3\2\2\2"+
		"\u027c\u027f\3\2\2\2\u027d\u027b\3\2\2\2\u027d\u027e\3\2\2\2\u027eY\3"+
		"\2\2\2\u027f\u027d\3\2\2\2\u0280\u0284\5\\/\2\u0281\u0282\7r\2\2\u0282"+
		"\u0283\7s\2\2\u0283\u0285\5\u008aF\2\u0284\u0281\3\2\2\2\u0284\u0285\3"+
		"\2\2\2\u0285[\3\2\2\2\u0286\u028a\5^\60\2\u0287\u0288\7t\2\2\u0288\u0289"+
		"\7X\2\2\u0289\u028b\5\u008aF\2\u028a\u0287\3\2\2\2\u028a\u028b\3\2\2\2"+
		"\u028b]\3\2\2\2\u028c\u0290\5`\61\2\u028d\u028e\7v\2\2\u028e\u028f\7X"+
		"\2\2\u028f\u0291\5\u00b0Y\2\u0290\u028d\3\2\2\2\u0290\u0291\3\2\2\2\u0291"+
		"_\3\2\2\2\u0292\u0296\5b\62\2\u0293\u0294\7u\2\2\u0294\u0295\7X\2\2\u0295"+
		"\u0297\5\u00b0Y\2\u0296\u0293\3\2\2\2\u0296\u0297\3\2\2\2\u0297a\3\2\2"+
		"\2\u0298\u029f\5d\63\2\u0299\u029a\7\6\2\2\u029a\u029b\7-\2\2\u029b\u029c"+
		"\3\2\2\2\u029c\u029e\5~@\2\u029d\u0299\3\2\2\2\u029e\u02a1\3\2\2\2\u029f"+
		"\u029d\3\2\2\2\u029f\u02a0\3\2\2\2\u02a0c\3\2\2\2\u02a1\u029f\3\2\2\2"+
		"\u02a2\u02a4\t\6\2\2\u02a3\u02a2\3\2\2\2\u02a4\u02a7\3\2\2\2\u02a5\u02a3"+
		"\3\2\2\2\u02a5\u02a6\3\2\2\2\u02a6\u02a8\3\2\2\2\u02a7\u02a5\3\2\2\2\u02a8"+
		"\u02a9\5f\64\2\u02a9e\3\2\2\2\u02aa\u02af\5h\65\2\u02ab\u02ac\7\65\2\2"+
		"\u02ac\u02ae\5h\65\2\u02ad\u02ab\3\2\2\2\u02ae\u02b1\3\2\2\2\u02af\u02ad"+
		"\3\2\2\2\u02af\u02b0\3\2\2\2\u02b0g\3\2\2\2\u02b1\u02af\3\2\2\2\u02b2"+
		"\u02ba\5r:\2\u02b3\u02b9\5j\66\2\u02b4\u02b9\5n8\2\u02b5\u02b9\5p9\2\u02b6"+
		"\u02b9\5l\67\2\u02b7\u02b9\5\u0080A\2\u02b8\u02b3\3\2\2\2\u02b8\u02b4"+
		"\3\2\2\2\u02b8\u02b5\3\2\2\2\u02b8\u02b6\3\2\2\2\u02b8\u02b7\3\2\2\2\u02b9"+
		"\u02bc\3\2\2\2\u02ba\u02b8\3\2\2\2\u02ba\u02bb\3\2\2\2\u02bbi\3\2\2\2"+
		"\u02bc\u02ba\3\2\2\2\u02bd\u02be\7\66\2\2\u02be\u02bf\7\66\2\2\u02bf\u02c0"+
		"\5 \21\2\u02c0\u02c1\7\67\2\2\u02c1\u02c2\7\67\2\2\u02c2k\3\2\2\2\u02c3"+
		"\u02c4\7\66\2\2\u02c4\u02c5\7\67\2\2\u02c5m\3\2\2\2\u02c6\u02c7\7\66\2"+
		"\2\u02c7\u02c8\5 \21\2\u02c8\u02c9\7\67\2\2\u02c9o\3\2\2\2\u02ca\u02d2"+
		"\78\2\2\u02cb\u02d3\5\u00be`\2\u02cc\u02d3\5\u00bc_\2\u02cd\u02d3\7\u0084"+
		"\2\2\u02ce\u02d3\5v<\2\u02cf\u02d3\5t;\2\u02d0\u02d3\5x=\2\u02d1\u02d3"+
		"\5\u00aeX\2\u02d2\u02cb\3\2\2\2\u02d2\u02cc\3\2\2\2\u02d2\u02cd\3\2\2"+
		"\2\u02d2\u02ce\3\2\2\2\u02d2\u02cf\3\2\2\2\u02d2\u02d0\3\2\2\2\u02d2\u02d1"+
		"\3\2\2\2\u02d3q\3\2\2\2\u02d4\u02e1\7|\2\2\u02d5\u02e1\7}\2\2\u02d6\u02e1"+
		"\5\u00bc_\2\u02d7\u02e1\5t;\2\u02d8\u02e1\5v<\2\u02d9\u02e1\5x=\2\u02da"+
		"\u02e1\5\u008cG\2\u02db\u02e1\5~@\2\u02dc\u02e1\5z>\2\u02dd\u02e1\5|?"+
		"\2\u02de\u02e1\5\u00b8]\2\u02df\u02e1\5\u0084C\2\u02e0\u02d4\3\2\2\2\u02e0"+
		"\u02d5\3\2\2\2\u02e0\u02d6\3\2\2\2\u02e0\u02d7\3\2\2\2\u02e0\u02d8\3\2"+
		"\2\2\u02e0\u02d9\3\2\2\2\u02e0\u02da\3\2\2\2\u02e0\u02db\3\2\2\2\u02e0"+
		"\u02dc\3\2\2\2\u02e0\u02dd\3\2\2\2\u02e0\u02de\3\2\2\2\u02e0\u02df\3\2"+
		"\2\2\u02e1s\3\2\2\2\u02e2\u02e5\7!\2\2\u02e3\u02e4\7\u0084\2\2\u02e4\u02e6"+
		"\7\f\2\2\u02e5\u02e3\3\2\2\2\u02e5\u02e6\3\2\2\2\u02e6\u02e7\3\2\2\2\u02e7"+
		"\u02e8\7\u0084\2\2\u02e8u\3\2\2\2\u02e9\u02eb\7\35\2\2\u02ea\u02ec\5 "+
		"\21\2\u02eb\u02ea\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec\u02ed\3\2\2\2\u02ed"+
		"\u02ee\7\36\2\2\u02eew\3\2\2\2\u02ef\u02f0\79\2\2\u02f0y\3\2\2\2\u02f1"+
		"\u02f2\7\t\2\2\u02f2\u02f3\7\37\2\2\u02f3\u02f4\5 \21\2\u02f4\u02f5\7"+
		" \2\2\u02f5{\3\2\2\2\u02f6\u02f7\7\n\2\2\u02f7\u02f8\7\37\2\2\u02f8\u02f9"+
		"\5 \21\2\u02f9\u02fa\7 \2\2\u02fa}\3\2\2\2\u02fb\u02ff\7\u0084\2\2\u02fc"+
		"\u02ff\5\u00be`\2\u02fd\u02ff\3\2\2\2\u02fe\u02fb\3\2\2\2\u02fe\u02fc"+
		"\3\2\2\2\u02fe\u02fd\3\2\2\2\u02ff\u0300\3\2\2\2\u0300\u0302\7\f\2\2\u0301"+
		"\u02fe\3\2\2\2\u0301\u0302\3\2\2\2\u0302\u0305\3\2\2\2\u0303\u0306\5\u00b4"+
		"[\2\u0304\u0306\5\u00be`\2\u0305\u0303\3\2\2\2\u0305\u0304\3\2\2\2\u0306"+
		"\u0307\3\2\2\2\u0307\u0308\5\u0080A\2\u0308\177\3\2\2\2\u0309\u0310\7"+
		"\35\2\2\u030a\u030c\5\u0082B\2\u030b\u030d\7\30\2\2\u030c\u030b\3\2\2"+
		"\2\u030c\u030d\3\2\2\2\u030d\u030f\3\2\2\2\u030e\u030a\3\2\2\2\u030f\u0312"+
		"\3\2\2\2\u0310\u030e\3\2\2\2\u0310\u0311\3\2\2\2\u0311\u0313\3\2\2\2\u0312"+
		"\u0310\3\2\2\2\u0313\u0314\7\36\2\2\u0314\u0081\3\2\2\2\u0315\u0318\5"+
		"\"\22\2\u0316\u0318\7{\2\2\u0317\u0315\3\2\2\2\u0317\u0316\3\2\2\2\u0318"+
		"\u0083\3\2\2\2\u0319\u031c\5\u0086D\2\u031a\u031c\5\u0088E\2\u031b\u0319"+
		"\3\2\2\2\u031b\u031a\3\2\2\2\u031c\u0085\3\2\2\2\u031d\u031e\7\u0084\2"+
		"\2\u031e\u031f\7:\2\2\u031f\u0320\7}\2\2\u0320\u0087\3\2\2\2\u0321\u0322"+
		"\7\34\2\2\u0322\u0324\7\35\2\2\u0323\u0325\5\34\17\2\u0324\u0323\3\2\2"+
		"\2\u0324\u0325\3\2\2\2\u0325\u0326\3\2\2\2\u0326\u0329\7\36\2\2\u0327"+
		"\u0328\7X\2\2\u0328\u032a\5\u008aF\2\u0329\u0327\3\2\2\2\u0329\u032a\3"+
		"\2\2\2\u032a\u032b\3\2\2\2\u032b\u032c\7\37\2\2\u032c\u032d\5 \21\2\u032d"+
		"\u032e\7 \2\2\u032e\u0089\3\2\2\2\u032f\u0330\7\35\2\2\u0330\u0338\7\36"+
		"\2\2\u0331\u0335\5\u008eH\2\u0332\u0336\7{\2\2\u0333\u0336\7#\2\2\u0334"+
		"\u0336\7\60\2\2\u0335\u0332\3\2\2\2\u0335\u0333\3\2\2\2\u0335\u0334\3"+
		"\2\2\2\u0335\u0336\3\2\2\2\u0336\u0338\3\2\2\2\u0337\u032f\3\2\2\2\u0337"+
		"\u0331\3\2\2\2\u0338\u008b\3\2\2\2\u0339\u0342\7\37\2\2\u033a\u033f\5"+
		"\u00b6\\\2\u033b\u033c\7\30\2\2\u033c\u033e\5\u00b6\\\2\u033d\u033b\3"+
		"\2\2\2\u033e\u0341\3\2\2\2\u033f\u033d\3\2\2\2\u033f\u0340\3\2\2\2\u0340"+
		"\u0343\3\2\2\2\u0341\u033f\3\2\2\2\u0342\u033a\3\2\2\2\u0342\u0343\3\2"+
		"\2\2\u0343\u0344\3\2\2\2\u0344\u034a\7 \2\2\u0345\u0346\7;\2\2\u0346\u0347"+
		"\5 \21\2\u0347\u0348\7<\2\2\u0348\u034a\3\2\2\2\u0349\u0339\3\2\2\2\u0349"+
		"\u0345\3\2\2\2\u034a\u008d\3\2\2\2\u034b\u034f\7=\2\2\u034c\u034f\5\u0090"+
		"I\2\u034d\u034f\5\u00b2Z\2\u034e\u034b\3\2\2\2\u034e\u034c\3\2\2\2\u034e"+
		"\u034d\3\2\2\2\u034f\u008f\3\2\2\2\u0350\u0351\t\b\2\2\u0351\u0091\3\2"+
		"\2\2\u0352\u0353\7@\2\2\u0353\u0093\3\2\2\2\u0354\u0355\7A\2\2\u0355\u0095"+
		"\3\2\2\2\u0356\u0357\7B\2\2\u0357\u0097\3\2\2\2\u0358\u0359\7C\2\2\u0359"+
		"\u0099\3\2\2\2\u035a\u035b\7D\2\2\u035b\u009b\3\2\2\2\u035c\u035d\7E\2"+
		"\2\u035d\u009d\3\2\2\2\u035e\u035f\7F\2\2\u035f\u009f\3\2\2\2\u0360\u0361"+
		"\7G\2\2\u0361\u00a1\3\2\2\2\u0362\u0363\7H\2\2\u0363\u00a3\3\2\2\2\u0364"+
		"\u0365\7I\2\2\u0365\u00a5\3\2\2\2\u0366\u0367\7J\2\2\u0367\u00a7\3\2\2"+
		"\2\u0368\u0369\7K\2\2\u0369\u00a9\3\2\2\2\u036a\u036b\7L\2\2\u036b\u00ab"+
		"\3\2\2\2\u036c\u036d\7M\2\2\u036d\u00ad\3\2\2\2\u036e\u037d\5\u0092J\2"+
		"\u036f\u037d\5\u0094K\2\u0370\u037d\5\u0096L\2\u0371\u037d\5\u0098M\2"+
		"\u0372\u037d\5\u009aN\2\u0373\u037d\5\u009cO\2\u0374\u037d\5\u009eP\2"+
		"\u0375\u037d\5\u00a0Q\2\u0376\u037d\5\u00a6T\2\u0377\u037d\5\u00a8U\2"+
		"\u0378\u037d\5\u00aaV\2\u0379\u037d\5\u00a2R\2\u037a\u037d\5\u00a4S\2"+
		"\u037b\u037d\5\u00acW\2\u037c\u036e\3\2\2\2\u037c\u036f\3\2\2\2\u037c"+
		"\u0370\3\2\2\2\u037c\u0371\3\2\2\2\u037c\u0372\3\2\2\2\u037c\u0373\3\2"+
		"\2\2\u037c\u0374\3\2\2\2\u037c\u0375\3\2\2\2\u037c\u0376\3\2\2\2\u037c"+
		"\u0377\3\2\2\2\u037c\u0378\3\2\2\2\u037c\u0379\3\2\2\2\u037c\u037a\3\2"+
		"\2\2\u037c\u037b\3\2\2\2\u037d\u00af\3\2\2\2\u037e\u0380\5\u00b2Z\2\u037f"+
		"\u0381\7{\2\2\u0380\u037f\3\2\2\2\u0380\u0381\3\2\2\2\u0381\u00b1\3\2"+
		"\2\2\u0382\u0386\7N\2\2\u0383\u0386\5\u00aeX\2\u0384\u0386\7|\2\2\u0385"+
		"\u0382\3\2\2\2\u0385\u0383\3\2\2\2\u0385\u0384\3\2\2\2\u0386\u00b3\3\2"+
		"\2\2\u0387\u038a\7\u0084\2\2\u0388\u038a\5\u00aeX\2\u0389\u0387\3\2\2"+
		"\2\u0389\u0388\3\2\2\2\u038a\u00b5\3\2\2\2\u038b\u038e\5\"\22\2\u038c"+
		"\u038e\7\u0084\2\2\u038d\u038b\3\2\2\2\u038d\u038c\3\2\2\2\u038e\u038f"+
		"\3\2\2\2\u038f\u0390\t\t\2\2\u0390\u0391\5\"\22\2\u0391\u00b7\3\2\2\2"+
		"\u0392\u0394\7\66\2\2\u0393\u0395\5 \21\2\u0394\u0393\3\2\2\2\u0394\u0395"+
		"\3\2\2\2\u0395\u0396\3\2\2\2\u0396\u0397\7\67\2\2\u0397\u00b9\3\2\2\2"+
		"\u0398\u0399\5\u00bc_\2\u0399\u00bb\3\2\2\2\u039a\u039b\7z\2\2\u039b\u00bd"+
		"\3\2\2\2\u039c\u039d\t\n\2\2\u039d\u00bf\3\2\2\2_\u00c8\u00cc\u00de\u00e4"+
		"\u00e9\u00ef\u0105\u010a\u0112\u011c\u0125\u0128\u012f\u0136\u0138\u013e"+
		"\u0143\u0148\u014f\u0156\u015d\u0164\u016e\u0172\u017a\u017c\u0188\u018e"+
		"\u0192\u0196\u01a1\u01a7\u01b6\u01bc\u01c0\u01c4\u01cb\u01d2\u01d8\u01dd"+
		"\u01df\u01e3\u01ea\u01f1\u01fa\u0206\u0210\u021c\u0220\u0229\u0230\u024e"+
		"\u0256\u025a\u0261\u0268\u026e\u0275\u027d\u0284\u028a\u0290\u0296\u029f"+
		"\u02a5\u02af\u02b8\u02ba\u02d2\u02e0\u02e5\u02eb\u02fe\u0301\u0305\u030c"+
		"\u0310\u0317\u031b\u0324\u0329\u0335\u0337\u033f\u0342\u0349\u034e\u037c"+
		"\u0380\u0385\u0389\u038d\u0394";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}