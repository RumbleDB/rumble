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
		RULE_module = 0, RULE_mainModule = 1, RULE_libraryModule = 2, RULE_prolog = 3, 
		RULE_setter = 4, RULE_namespaceDecl = 5, RULE_annotatedDecl = 6, RULE_defaultCollationDecl = 7, 
		RULE_orderingModeDecl = 8, RULE_emptyOrderDecl = 9, RULE_decimalFormatDecl = 10, 
		RULE_qname = 11, RULE_dfPropertyName = 12, RULE_moduleImport = 13, RULE_varDecl = 14, 
		RULE_functionDecl = 15, RULE_paramList = 16, RULE_param = 17, RULE_expr = 18, 
		RULE_exprSingle = 19, RULE_flowrExpr = 20, RULE_forClause = 21, RULE_forVar = 22, 
		RULE_letClause = 23, RULE_letVar = 24, RULE_whereClause = 25, RULE_groupByClause = 26, 
		RULE_groupByVar = 27, RULE_orderByClause = 28, RULE_orderByExpr = 29, 
		RULE_countClause = 30, RULE_quantifiedExpr = 31, RULE_quantifiedExprVar = 32, 
		RULE_switchExpr = 33, RULE_switchCaseClause = 34, RULE_typeSwitchExpr = 35, 
		RULE_caseClause = 36, RULE_ifExpr = 37, RULE_tryCatchExpr = 38, RULE_orExpr = 39, 
		RULE_andExpr = 40, RULE_notExpr = 41, RULE_comparisonExpr = 42, RULE_stringConcatExpr = 43, 
		RULE_rangeExpr = 44, RULE_additiveExpr = 45, RULE_multiplicativeExpr = 46, 
		RULE_instanceOfExpr = 47, RULE_treatExpr = 48, RULE_castableExpr = 49, 
		RULE_castExpr = 50, RULE_arrowExpr = 51, RULE_unaryExpr = 52, RULE_simpleMapExpr = 53, 
		RULE_postFixExpr = 54, RULE_arrayLookup = 55, RULE_arrayUnboxing = 56, 
		RULE_predicate = 57, RULE_objectLookup = 58, RULE_primaryExpr = 59, RULE_varRef = 60, 
		RULE_parenthesizedExpr = 61, RULE_contextItemExpr = 62, RULE_orderedExpr = 63, 
		RULE_unorderedExpr = 64, RULE_functionCall = 65, RULE_argumentList = 66, 
		RULE_argument = 67, RULE_functionItemExpr = 68, RULE_namedFunctionRef = 69, 
		RULE_inlineFunctionExpr = 70, RULE_sequenceType = 71, RULE_objectConstructor = 72, 
		RULE_itemType = 73, RULE_jSONItemTest = 74, RULE_keyWordString = 75, RULE_keyWordInteger = 76, 
		RULE_keyWordDecimal = 77, RULE_keyWordDouble = 78, RULE_keyWordBoolean = 79, 
		RULE_keyWordDuration = 80, RULE_keyWordYearMonthDuration = 81, RULE_keyWordDayTimeDuration = 82, 
		RULE_keyWordHexBinary = 83, RULE_keyWordBase64Binary = 84, RULE_keyWordDateTime = 85, 
		RULE_keyWordDate = 86, RULE_keyWordTime = 87, RULE_keyWordAnyURI = 88, 
		RULE_typesKeywords = 89, RULE_singleType = 90, RULE_atomicType = 91, RULE_nCNameOrKeyWord = 92, 
		RULE_pairConstructor = 93, RULE_arrayConstructor = 94, RULE_uriLiteral = 95, 
		RULE_stringLiteral = 96, RULE_keyWords = 97;
	public static final String[] ruleNames = {
		"module", "mainModule", "libraryModule", "prolog", "setter", "namespaceDecl", 
		"annotatedDecl", "defaultCollationDecl", "orderingModeDecl", "emptyOrderDecl", 
		"decimalFormatDecl", "qname", "dfPropertyName", "moduleImport", "varDecl", 
		"functionDecl", "paramList", "param", "expr", "exprSingle", "flowrExpr", 
		"forClause", "forVar", "letClause", "letVar", "whereClause", "groupByClause", 
		"groupByVar", "orderByClause", "orderByExpr", "countClause", "quantifiedExpr", 
		"quantifiedExprVar", "switchExpr", "switchCaseClause", "typeSwitchExpr", 
		"caseClause", "ifExpr", "tryCatchExpr", "orExpr", "andExpr", "notExpr", 
		"comparisonExpr", "stringConcatExpr", "rangeExpr", "additiveExpr", "multiplicativeExpr", 
		"instanceOfExpr", "treatExpr", "castableExpr", "castExpr", "arrowExpr", 
		"unaryExpr", "simpleMapExpr", "postFixExpr", "arrayLookup", "arrayUnboxing", 
		"predicate", "objectLookup", "primaryExpr", "varRef", "parenthesizedExpr", 
		"contextItemExpr", "orderedExpr", "unorderedExpr", "functionCall", "argumentList", 
		"argument", "functionItemExpr", "namedFunctionRef", "inlineFunctionExpr", 
		"sequenceType", "objectConstructor", "itemType", "jSONItemTest", "keyWordString", 
		"keyWordInteger", "keyWordDecimal", "keyWordDouble", "keyWordBoolean", 
		"keyWordDuration", "keyWordYearMonthDuration", "keyWordDayTimeDuration", 
		"keyWordHexBinary", "keyWordBase64Binary", "keyWordDateTime", "keyWordDate", 
		"keyWordTime", "keyWordAnyURI", "typesKeywords", "singleType", "atomicType", 
		"nCNameOrKeyWord", "pairConstructor", "arrayConstructor", "uriLiteral", 
		"stringLiteral", "keyWords"
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
			setState(201);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(196);
				match(Kjsoniq);
				setState(197);
				match(Kversion);
				setState(198);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(199);
				match(T__0);
				}
				break;
			}
			setState(205);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(203);
				libraryModule();
				}
				break;
			case T__4:
			case T__6:
			case T__7:
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
				setState(204);
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
			setState(207);
			prolog();
			setState(208);
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
			setState(210);
			match(T__1);
			setState(211);
			match(T__2);
			setState(212);
			match(NCName);
			setState(213);
			match(T__3);
			setState(214);
			uriLiteral();
			setState(215);
			match(T__0);
			setState(216);
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
		enterRule(_localctx, 6, RULE_prolog);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(221);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(218);
						setter();
						}
						break;
					case 2:
						{
						setState(219);
						namespaceDecl();
						}
						break;
					case 3:
						{
						setState(220);
						moduleImport();
						}
						break;
					}
					setState(223);
					match(T__0);
					}
					} 
				}
				setState(229);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(235);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(230);
				annotatedDecl();
				setState(231);
				match(T__0);
				}
				}
				setState(237);
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
		enterRule(_localctx, 8, RULE_setter);
		try {
			setState(242);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(238);
				defaultCollationDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(239);
				orderingModeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(240);
				emptyOrderDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(241);
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
		enterRule(_localctx, 10, RULE_namespaceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			match(T__4);
			setState(245);
			match(T__2);
			setState(246);
			match(NCName);
			setState(247);
			match(T__3);
			setState(248);
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
		enterRule(_localctx, 12, RULE_annotatedDecl);
		try {
			setState(252);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(250);
				functionDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(251);
				varDecl();
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
		enterRule(_localctx, 14, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			match(T__4);
			setState(255);
			match(Kdefault);
			setState(256);
			match(Kcollation);
			setState(257);
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
		enterRule(_localctx, 16, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259);
			match(T__4);
			setState(260);
			match(T__5);
			setState(261);
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
		enterRule(_localctx, 18, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			match(T__4);
			setState(264);
			match(Kdefault);
			setState(265);
			match(Korder);
			setState(266);
			match(Kempty);
			setState(267);
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
		enterRule(_localctx, 20, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			match(T__4);
			setState(274);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				{
				setState(270);
				match(T__8);
				setState(271);
				qname();
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(272);
				match(Kdefault);
				setState(273);
				match(T__8);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) {
				{
				{
				setState(276);
				dfPropertyName();
				setState(277);
				match(T__3);
				setState(278);
				stringLiteral();
				}
				}
				setState(284);
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
		public NCNameOrKeyWordContext fn_name;
		public KeyWordsContext fn_namekw;
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
		enterRule(_localctx, 22, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(287);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(285);
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
				case Ktreat:
				case Kcast:
				case Kcastable:
				case Kversion:
				case Kjsoniq:
				case Kjson:
					{
					setState(286);
					((QnameContext)_localctx).nskw = keyWords();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(289);
				match(T__9);
				}
				break;
			}
			setState(294);
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
				setState(292);
				((QnameContext)_localctx).fn_name = nCNameOrKeyWord();
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
				setState(293);
				((QnameContext)_localctx).fn_namekw = keyWords();
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
		enterRule(_localctx, 24, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
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
		enterRule(_localctx, 26, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(298);
			match(T__20);
			setState(299);
			match(T__1);
			setState(303);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(300);
				match(T__2);
				setState(301);
				match(NCName);
				setState(302);
				match(T__3);
				}
			}

			setState(305);
			uriLiteral();
			setState(315);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(306);
				match(Kat);
				setState(307);
				uriLiteral();
				setState(312);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__21) {
					{
					{
					setState(308);
					match(T__21);
					setState(309);
					uriLiteral();
					}
					}
					setState(314);
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
		enterRule(_localctx, 28, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			match(T__4);
			setState(318);
			match(T__22);
			setState(319);
			varRef();
			setState(322);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(320);
				match(Kas);
				setState(321);
				sequenceType();
				}
			}

			setState(331);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				{
				{
				setState(324);
				match(T__23);
				setState(325);
				exprSingle();
				}
				}
				break;
			case T__24:
				{
				{
				setState(326);
				((VarDeclContext)_localctx).external = match(T__24);
				setState(329);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__23) {
					{
					setState(327);
					match(T__23);
					setState(328);
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
		public ExprContext fn_body;
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
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
		enterRule(_localctx, 30, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			match(T__4);
			setState(334);
			match(T__25);
			setState(335);
			((FunctionDeclContext)_localctx).fn_name = qname();
			setState(336);
			match(T__26);
			setState(338);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(337);
				paramList();
				}
			}

			setState(340);
			match(T__27);
			setState(343);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(341);
				match(Kas);
				setState(342);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(350);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				{
				setState(345);
				match(T__28);
				setState(346);
				((FunctionDeclContext)_localctx).fn_body = expr();
				setState(347);
				match(T__29);
				}
				break;
			case T__24:
				{
				setState(349);
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
		enterRule(_localctx, 32, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(352);
			param();
			setState(357);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(353);
				match(T__21);
				setState(354);
				param();
				}
				}
				setState(359);
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
		enterRule(_localctx, 34, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			match(T__30);
			setState(361);
			match(NCName);
			setState(364);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(362);
				match(Kas);
				setState(363);
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
		enterRule(_localctx, 36, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			exprSingle();
			setState(371);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(367);
				match(T__21);
				setState(368);
				exprSingle();
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
		enterRule(_localctx, 38, RULE_exprSingle);
		try {
			setState(381);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(374);
				flowrExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(375);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(376);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(377);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(378);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(379);
				tryCatchExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(380);
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
		enterRule(_localctx, 40, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(383);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(384);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(395);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & ((1L << (Kfor - 77)) | (1L << (Klet - 77)) | (1L << (Kwhere - 77)) | (1L << (Kgroup - 77)) | (1L << (Korder - 77)) | (1L << (Kcount - 77)) | (1L << (Kstable - 77)))) != 0)) {
				{
				setState(393);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(387);
					forClause();
					}
					break;
				case Kwhere:
					{
					setState(388);
					whereClause();
					}
					break;
				case Klet:
					{
					setState(389);
					letClause();
					}
					break;
				case Kgroup:
					{
					setState(390);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(391);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(392);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(397);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(398);
			match(Kreturn);
			setState(399);
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
		enterRule(_localctx, 42, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(Kfor);
			setState(402);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(407);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(403);
				match(T__21);
				setState(404);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(409);
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
		enterRule(_localctx, 44, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(410);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(413);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(411);
				match(Kas);
				setState(412);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(417);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(415);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(416);
				match(Kempty);
				}
			}

			setState(421);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(419);
				match(Kat);
				setState(420);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(423);
			match(Kin);
			setState(424);
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
		enterRule(_localctx, 46, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			match(Klet);
			setState(427);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(432);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(428);
				match(T__21);
				setState(429);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(434);
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
		enterRule(_localctx, 48, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(438);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(436);
				match(Kas);
				setState(437);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(440);
			match(T__23);
			setState(441);
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
		enterRule(_localctx, 50, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(443);
			match(Kwhere);
			setState(444);
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
		enterRule(_localctx, 52, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			match(Kgroup);
			setState(447);
			match(Kby);
			setState(448);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(453);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(449);
				match(T__21);
				setState(450);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(455);
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
		enterRule(_localctx, 54, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(456);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(463);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23 || _la==Kas) {
				{
				setState(459);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(457);
					match(Kas);
					setState(458);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(461);
				((GroupByVarContext)_localctx).decl = match(T__23);
				setState(462);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(467);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(465);
				match(Kcollation);
				setState(466);
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
		enterRule(_localctx, 56, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(474);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(469);
				match(Korder);
				setState(470);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(471);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(472);
				match(Korder);
				setState(473);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(476);
			orderByExpr();
			setState(481);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(477);
				match(T__21);
				setState(478);
				orderByExpr();
				}
				}
				setState(483);
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
		enterRule(_localctx, 58, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(487);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(485);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(486);
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
			setState(494);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(489);
				match(Kempty);
				setState(492);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(490);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(491);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(498);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(496);
				match(Kcollation);
				setState(497);
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
		enterRule(_localctx, 60, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(500);
			match(Kcount);
			setState(501);
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
		enterRule(_localctx, 62, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(503);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(504);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(507);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(512);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(508);
				match(T__21);
				setState(509);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(514);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(515);
			match(Ksatisfies);
			setState(516);
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
		enterRule(_localctx, 64, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(518);
			varRef();
			setState(521);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(519);
				match(Kas);
				setState(520);
				sequenceType();
				}
			}

			setState(523);
			match(Kin);
			setState(524);
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
		enterRule(_localctx, 66, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(526);
			match(Kswitch);
			setState(527);
			match(T__26);
			setState(528);
			((SwitchExprContext)_localctx).cond = expr();
			setState(529);
			match(T__27);
			setState(531); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(530);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(533); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(535);
			match(Kdefault);
			setState(536);
			match(Kreturn);
			setState(537);
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
		enterRule(_localctx, 68, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(541); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(539);
				match(Kcase);
				setState(540);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(543); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(545);
			match(Kreturn);
			setState(546);
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
		enterRule(_localctx, 70, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(548);
			match(Ktypeswitch);
			setState(549);
			match(T__26);
			setState(550);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(551);
			match(T__27);
			setState(553); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(552);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(555); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(557);
			match(Kdefault);
			setState(559);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(558);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(561);
			match(Kreturn);
			setState(562);
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
		enterRule(_localctx, 72, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(564);
			match(Kcase);
			setState(568);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(565);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(566);
				match(Kas);
				}
			}

			setState(570);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__31) {
				{
				{
				setState(571);
				match(T__31);
				setState(572);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(577);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(578);
			match(Kreturn);
			setState(579);
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
		enterRule(_localctx, 74, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(581);
			match(Kif);
			setState(582);
			match(T__26);
			setState(583);
			((IfExprContext)_localctx).test_condition = expr();
			setState(584);
			match(T__27);
			setState(585);
			match(Kthen);
			setState(586);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(587);
			match(Kelse);
			setState(588);
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
		enterRule(_localctx, 76, RULE_tryCatchExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(590);
			match(Ktry);
			setState(591);
			match(T__28);
			setState(592);
			expr();
			setState(593);
			match(T__29);
			setState(594);
			match(Kcatch);
			setState(595);
			match(T__32);
			setState(596);
			match(T__28);
			setState(597);
			expr();
			setState(598);
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
		enterRule(_localctx, 78, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(600);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(605);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(601);
					match(Kor);
					setState(602);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(607);
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
		enterRule(_localctx, 80, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(608);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(613);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(609);
					match(Kand);
					setState(610);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(615);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,54,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 82, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(617);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(616);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(619);
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
		public Token _tset1099;
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
		enterRule(_localctx, 84, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(621);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(624);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) {
				{
				setState(622);
				((ComparisonExprContext)_localctx)._tset1099 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1099 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1099);
				setState(623);
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
		enterRule(_localctx, 86, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(626);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(631);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44) {
				{
				{
				setState(627);
				match(T__44);
				setState(628);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(633);
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
		enterRule(_localctx, 88, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(634);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(637);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(635);
				match(Kto);
				setState(636);
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
		public Token _tset1208;
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
		enterRule(_localctx, 90, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(639);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(644);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,59,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(640);
					((AdditiveExprContext)_localctx)._tset1208 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__45 || _la==T__46) ) {
						((AdditiveExprContext)_localctx)._tset1208 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1208);
					setState(641);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(646);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,59,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public Token _tset1236;
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
		enterRule(_localctx, 92, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(647);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(652);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) {
				{
				{
				setState(648);
				((MultiplicativeExprContext)_localctx)._tset1236 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1236 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1236);
				setState(649);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
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
		enterRule(_localctx, 94, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(655);
			((InstanceOfExprContext)_localctx).main_expr = treatExpr();
			setState(659);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(656);
				match(Kinstance);
				setState(657);
				match(Kof);
				setState(658);
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
		enterRule(_localctx, 96, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(661);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(665);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(662);
				match(Ktreat);
				setState(663);
				match(Kas);
				setState(664);
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
		enterRule(_localctx, 98, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(667);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(671);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(668);
				match(Kcastable);
				setState(669);
				match(Kas);
				setState(670);
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
		enterRule(_localctx, 100, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(673);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(677);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(674);
				match(Kcast);
				setState(675);
				match(Kas);
				setState(676);
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
		enterRule(_localctx, 102, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(679);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(686);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(680);
					match(T__3);
					setState(681);
					match(T__42);
					}
					setState(683);
					((ArrowExprContext)_localctx).functionCall = functionCall();
					((ArrowExprContext)_localctx).function_call_expr.add(((ArrowExprContext)_localctx).functionCall);
					}
					} 
				}
				setState(688);
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

	public static class UnaryExprContext extends ParserRuleContext {
		public Token s47;
		public List<Token> op = new ArrayList<Token>();
		public Token s46;
		public Token _tset1375;
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
		enterRule(_localctx, 104, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(692);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45 || _la==T__46) {
				{
				{
				setState(689);
				((UnaryExprContext)_localctx)._tset1375 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__45 || _la==T__46) ) {
					((UnaryExprContext)_localctx)._tset1375 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1375);
				}
				}
				setState(694);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(695);
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
		enterRule(_localctx, 106, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(697);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(702);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__50) {
				{
				{
				setState(698);
				match(T__50);
				setState(699);
				((SimpleMapExprContext)_localctx).postFixExpr = postFixExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).postFixExpr);
				}
				}
				setState(704);
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
		enterRule(_localctx, 108, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(705);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(713);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(711);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
					case 1:
						{
						setState(706);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(707);
						predicate();
						}
						break;
					case 3:
						{
						setState(708);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(709);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(710);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(715);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 110, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(716);
			match(T__51);
			setState(717);
			match(T__51);
			setState(718);
			expr();
			setState(719);
			match(T__52);
			setState(720);
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
		enterRule(_localctx, 112, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(722);
			match(T__51);
			setState(723);
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
		enterRule(_localctx, 114, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(725);
			match(T__51);
			setState(726);
			expr();
			setState(727);
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
		enterRule(_localctx, 116, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(729);
			match(T__53);
			setState(737);
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
				setState(730);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(731);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(732);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__26:
				{
				setState(733);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__30:
				{
				setState(734);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__54:
				{
				setState(735);
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
				setState(736);
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
		enterRule(_localctx, 118, RULE_primaryExpr);
		try {
			setState(751);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(739);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(740);
				match(Literal);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(741);
				stringLiteral();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(742);
				varRef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(743);
				parenthesizedExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(744);
				contextItemExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(745);
				objectConstructor();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(746);
				functionCall();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(747);
				orderedExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(748);
				unorderedExpr();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(749);
				arrayConstructor();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(750);
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
		enterRule(_localctx, 120, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(753);
			match(T__30);
			setState(754);
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
		enterRule(_localctx, 122, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(756);
			match(T__26);
			setState(758);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__56 - 7)) | (1L << (T__61 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)) | (1L << (T__74 - 71)) | (1L << (Kfor - 71)) | (1L << (Klet - 71)) | (1L << (Kwhere - 71)) | (1L << (Kgroup - 71)) | (1L << (Kby - 71)) | (1L << (Korder - 71)) | (1L << (Kreturn - 71)) | (1L << (Kif - 71)) | (1L << (Kin - 71)) | (1L << (Kas - 71)) | (1L << (Kat - 71)) | (1L << (Kallowing - 71)) | (1L << (Kempty - 71)) | (1L << (Kcount - 71)) | (1L << (Kstable - 71)) | (1L << (Kascending - 71)) | (1L << (Kdescending - 71)) | (1L << (Ksome - 71)) | (1L << (Kevery - 71)) | (1L << (Ksatisfies - 71)) | (1L << (Kcollation - 71)) | (1L << (Kgreatest - 71)) | (1L << (Kleast - 71)) | (1L << (Kswitch - 71)) | (1L << (Kcase - 71)) | (1L << (Ktry - 71)) | (1L << (Kcatch - 71)) | (1L << (Kdefault - 71)) | (1L << (Kthen - 71)) | (1L << (Kelse - 71)) | (1L << (Ktypeswitch - 71)) | (1L << (Kor - 71)) | (1L << (Kand - 71)) | (1L << (Knot - 71)) | (1L << (Kto - 71)) | (1L << (Kinstance - 71)) | (1L << (Kof - 71)) | (1L << (Ktreat - 71)) | (1L << (Kcast - 71)) | (1L << (Kcastable - 71)) | (1L << (Kversion - 71)) | (1L << (Kjsoniq - 71)) | (1L << (Kjson - 71)) | (1L << (STRING - 71)) | (1L << (NullLiteral - 71)) | (1L << (Literal - 71)) | (1L << (NCName - 71)))) != 0)) {
				{
				setState(757);
				expr();
				}
			}

			setState(760);
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
		enterRule(_localctx, 124, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(762);
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
		enterRule(_localctx, 126, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(764);
			match(T__6);
			setState(765);
			match(T__28);
			setState(766);
			expr();
			setState(767);
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
		enterRule(_localctx, 128, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(769);
			match(T__7);
			setState(770);
			match(T__28);
			setState(771);
			expr();
			setState(772);
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
		enterRule(_localctx, 130, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(774);
			((FunctionCallContext)_localctx).fn_name = qname();
			setState(775);
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
		enterRule(_localctx, 132, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(777);
			match(T__26);
			setState(784);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__56 - 7)) | (1L << (T__61 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)) | (1L << (T__74 - 71)) | (1L << (Kfor - 71)) | (1L << (Klet - 71)) | (1L << (Kwhere - 71)) | (1L << (Kgroup - 71)) | (1L << (Kby - 71)) | (1L << (Korder - 71)) | (1L << (Kreturn - 71)) | (1L << (Kif - 71)) | (1L << (Kin - 71)) | (1L << (Kas - 71)) | (1L << (Kat - 71)) | (1L << (Kallowing - 71)) | (1L << (Kempty - 71)) | (1L << (Kcount - 71)) | (1L << (Kstable - 71)) | (1L << (Kascending - 71)) | (1L << (Kdescending - 71)) | (1L << (Ksome - 71)) | (1L << (Kevery - 71)) | (1L << (Ksatisfies - 71)) | (1L << (Kcollation - 71)) | (1L << (Kgreatest - 71)) | (1L << (Kleast - 71)) | (1L << (Kswitch - 71)) | (1L << (Kcase - 71)) | (1L << (Ktry - 71)) | (1L << (Kcatch - 71)) | (1L << (Kdefault - 71)) | (1L << (Kthen - 71)) | (1L << (Kelse - 71)) | (1L << (Ktypeswitch - 71)) | (1L << (Kor - 71)) | (1L << (Kand - 71)) | (1L << (Knot - 71)) | (1L << (Kto - 71)) | (1L << (Kinstance - 71)) | (1L << (Kof - 71)) | (1L << (Ktreat - 71)) | (1L << (Kcast - 71)) | (1L << (Kcastable - 71)) | (1L << (Kversion - 71)) | (1L << (Kjsoniq - 71)) | (1L << (Kjson - 71)) | (1L << (STRING - 71)) | (1L << (ArgumentPlaceholder - 71)) | (1L << (NullLiteral - 71)) | (1L << (Literal - 71)) | (1L << (NCName - 71)))) != 0)) {
				{
				{
				setState(778);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(780);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__21) {
					{
					setState(779);
					match(T__21);
					}
				}

				}
				}
				setState(786);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(787);
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
		enterRule(_localctx, 134, RULE_argument);
		try {
			setState(791);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
			case T__7:
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
				setState(789);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(790);
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
		enterRule(_localctx, 136, RULE_functionItemExpr);
		try {
			setState(795);
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
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(793);
				namedFunctionRef();
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 2);
				{
				setState(794);
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
		enterRule(_localctx, 138, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(797);
			((NamedFunctionRefContext)_localctx).fn_name = qname();
			setState(798);
			match(T__55);
			setState(799);
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
		enterRule(_localctx, 140, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(801);
			match(T__25);
			setState(802);
			match(T__26);
			setState(804);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(803);
				paramList();
				}
			}

			setState(806);
			match(T__27);
			setState(809);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(807);
				match(Kas);
				setState(808);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(811);
			match(T__28);
			setState(812);
			((InlineFunctionExprContext)_localctx).fn_body = expr();
			setState(813);
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
		enterRule(_localctx, 142, RULE_sequenceType);
		try {
			setState(823);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
				enterOuterAlt(_localctx, 1);
				{
				setState(815);
				match(T__26);
				setState(816);
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
				setState(817);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(821);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
				case 1:
					{
					setState(818);
					((SequenceTypeContext)_localctx).s121 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s121);
					}
					break;
				case 2:
					{
					setState(819);
					((SequenceTypeContext)_localctx).s33 = match(T__32);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s33);
					}
					break;
				case 3:
					{
					setState(820);
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
		enterRule(_localctx, 144, RULE_objectConstructor);
		int _la;
		try {
			setState(841);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(825);
				match(T__28);
				setState(834);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__56 - 7)) | (1L << (T__61 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)) | (1L << (T__74 - 71)) | (1L << (Kfor - 71)) | (1L << (Klet - 71)) | (1L << (Kwhere - 71)) | (1L << (Kgroup - 71)) | (1L << (Kby - 71)) | (1L << (Korder - 71)) | (1L << (Kreturn - 71)) | (1L << (Kif - 71)) | (1L << (Kin - 71)) | (1L << (Kas - 71)) | (1L << (Kat - 71)) | (1L << (Kallowing - 71)) | (1L << (Kempty - 71)) | (1L << (Kcount - 71)) | (1L << (Kstable - 71)) | (1L << (Kascending - 71)) | (1L << (Kdescending - 71)) | (1L << (Ksome - 71)) | (1L << (Kevery - 71)) | (1L << (Ksatisfies - 71)) | (1L << (Kcollation - 71)) | (1L << (Kgreatest - 71)) | (1L << (Kleast - 71)) | (1L << (Kswitch - 71)) | (1L << (Kcase - 71)) | (1L << (Ktry - 71)) | (1L << (Kcatch - 71)) | (1L << (Kdefault - 71)) | (1L << (Kthen - 71)) | (1L << (Kelse - 71)) | (1L << (Ktypeswitch - 71)) | (1L << (Kor - 71)) | (1L << (Kand - 71)) | (1L << (Knot - 71)) | (1L << (Kto - 71)) | (1L << (Kinstance - 71)) | (1L << (Kof - 71)) | (1L << (Ktreat - 71)) | (1L << (Kcast - 71)) | (1L << (Kcastable - 71)) | (1L << (Kversion - 71)) | (1L << (Kjsoniq - 71)) | (1L << (Kjson - 71)) | (1L << (STRING - 71)) | (1L << (NullLiteral - 71)) | (1L << (Literal - 71)) | (1L << (NCName - 71)))) != 0)) {
					{
					setState(826);
					pairConstructor();
					setState(831);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__21) {
						{
						{
						setState(827);
						match(T__21);
						setState(828);
						pairConstructor();
						}
						}
						setState(833);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(836);
				match(T__29);
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 2);
				{
				setState(837);
				((ObjectConstructorContext)_localctx).s57 = match(T__56);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s57);
				setState(838);
				expr();
				setState(839);
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
		enterRule(_localctx, 146, RULE_itemType);
		try {
			setState(846);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__58:
				enterOuterAlt(_localctx, 1);
				{
				setState(843);
				match(T__58);
				}
				break;
			case T__59:
			case T__60:
			case Kjson:
				enterOuterAlt(_localctx, 2);
				{
				setState(844);
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
				setState(845);
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
		enterRule(_localctx, 148, RULE_jSONItemTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(848);
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
		enterRule(_localctx, 150, RULE_keyWordString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(850);
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
		enterRule(_localctx, 152, RULE_keyWordInteger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(852);
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
		enterRule(_localctx, 154, RULE_keyWordDecimal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(854);
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
		enterRule(_localctx, 156, RULE_keyWordDouble);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(856);
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
		enterRule(_localctx, 158, RULE_keyWordBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
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
		enterRule(_localctx, 160, RULE_keyWordDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(860);
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
		enterRule(_localctx, 162, RULE_keyWordYearMonthDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(862);
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
		enterRule(_localctx, 164, RULE_keyWordDayTimeDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
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
		enterRule(_localctx, 166, RULE_keyWordHexBinary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(866);
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
		enterRule(_localctx, 168, RULE_keyWordBase64Binary);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
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
		enterRule(_localctx, 170, RULE_keyWordDateTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(870);
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
		enterRule(_localctx, 172, RULE_keyWordDate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(872);
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
		enterRule(_localctx, 174, RULE_keyWordTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(874);
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
		enterRule(_localctx, 176, RULE_keyWordAnyURI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(876);
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
		enterRule(_localctx, 178, RULE_typesKeywords);
		try {
			setState(892);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__61:
				enterOuterAlt(_localctx, 1);
				{
				setState(878);
				keyWordString();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 2);
				{
				setState(879);
				keyWordInteger();
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 3);
				{
				setState(880);
				keyWordDecimal();
				}
				break;
			case T__64:
				enterOuterAlt(_localctx, 4);
				{
				setState(881);
				keyWordDouble();
				}
				break;
			case T__65:
				enterOuterAlt(_localctx, 5);
				{
				setState(882);
				keyWordBoolean();
				}
				break;
			case T__66:
				enterOuterAlt(_localctx, 6);
				{
				setState(883);
				keyWordDuration();
				}
				break;
			case T__67:
				enterOuterAlt(_localctx, 7);
				{
				setState(884);
				keyWordYearMonthDuration();
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 8);
				{
				setState(885);
				keyWordDayTimeDuration();
				}
				break;
			case T__71:
				enterOuterAlt(_localctx, 9);
				{
				setState(886);
				keyWordDateTime();
				}
				break;
			case T__72:
				enterOuterAlt(_localctx, 10);
				{
				setState(887);
				keyWordDate();
				}
				break;
			case T__73:
				enterOuterAlt(_localctx, 11);
				{
				setState(888);
				keyWordTime();
				}
				break;
			case T__69:
				enterOuterAlt(_localctx, 12);
				{
				setState(889);
				keyWordHexBinary();
				}
				break;
			case T__70:
				enterOuterAlt(_localctx, 13);
				{
				setState(890);
				keyWordBase64Binary();
				}
				break;
			case T__74:
				enterOuterAlt(_localctx, 14);
				{
				setState(891);
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
		enterRule(_localctx, 180, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(894);
			((SingleTypeContext)_localctx).item = atomicType();
			setState(896);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				setState(895);
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
		enterRule(_localctx, 182, RULE_atomicType);
		try {
			setState(901);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__75:
				enterOuterAlt(_localctx, 1);
				{
				setState(898);
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
				setState(899);
				typesKeywords();
				}
				break;
			case NullLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(900);
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
		enterRule(_localctx, 184, RULE_nCNameOrKeyWord);
		try {
			setState(905);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(903);
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
				setState(904);
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
		enterRule(_localctx, 186, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(909);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
			case 1:
				{
				setState(907);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(908);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(911);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(912);
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
		enterRule(_localctx, 188, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(914);
			match(T__51);
			setState(916);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 7)) & ~0x3f) == 0 && ((1L << (_la - 7)) & ((1L << (T__6 - 7)) | (1L << (T__7 - 7)) | (1L << (T__25 - 7)) | (1L << (T__26 - 7)) | (1L << (T__28 - 7)) | (1L << (T__30 - 7)) | (1L << (T__45 - 7)) | (1L << (T__46 - 7)) | (1L << (T__51 - 7)) | (1L << (T__54 - 7)) | (1L << (T__56 - 7)) | (1L << (T__61 - 7)) | (1L << (T__62 - 7)) | (1L << (T__63 - 7)) | (1L << (T__64 - 7)) | (1L << (T__65 - 7)) | (1L << (T__66 - 7)) | (1L << (T__67 - 7)) | (1L << (T__68 - 7)) | (1L << (T__69 - 7)))) != 0) || ((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)) | (1L << (T__74 - 71)) | (1L << (Kfor - 71)) | (1L << (Klet - 71)) | (1L << (Kwhere - 71)) | (1L << (Kgroup - 71)) | (1L << (Kby - 71)) | (1L << (Korder - 71)) | (1L << (Kreturn - 71)) | (1L << (Kif - 71)) | (1L << (Kin - 71)) | (1L << (Kas - 71)) | (1L << (Kat - 71)) | (1L << (Kallowing - 71)) | (1L << (Kempty - 71)) | (1L << (Kcount - 71)) | (1L << (Kstable - 71)) | (1L << (Kascending - 71)) | (1L << (Kdescending - 71)) | (1L << (Ksome - 71)) | (1L << (Kevery - 71)) | (1L << (Ksatisfies - 71)) | (1L << (Kcollation - 71)) | (1L << (Kgreatest - 71)) | (1L << (Kleast - 71)) | (1L << (Kswitch - 71)) | (1L << (Kcase - 71)) | (1L << (Ktry - 71)) | (1L << (Kcatch - 71)) | (1L << (Kdefault - 71)) | (1L << (Kthen - 71)) | (1L << (Kelse - 71)) | (1L << (Ktypeswitch - 71)) | (1L << (Kor - 71)) | (1L << (Kand - 71)) | (1L << (Knot - 71)) | (1L << (Kto - 71)) | (1L << (Kinstance - 71)) | (1L << (Kof - 71)) | (1L << (Ktreat - 71)) | (1L << (Kcast - 71)) | (1L << (Kcastable - 71)) | (1L << (Kversion - 71)) | (1L << (Kjsoniq - 71)) | (1L << (Kjson - 71)) | (1L << (STRING - 71)) | (1L << (NullLiteral - 71)) | (1L << (Literal - 71)) | (1L << (NCName - 71)))) != 0)) {
				{
				setState(915);
				expr();
				}
			}

			setState(918);
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
		enterRule(_localctx, 190, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(920);
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
		enterRule(_localctx, 192, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(922);
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
		enterRule(_localctx, 194, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(924);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0086\u03a1\4\2\t"+
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
		"`\t`\4a\ta\4b\tb\4c\tc\3\2\3\2\3\2\3\2\3\2\5\2\u00cc\n\2\3\2\3\2\5\2\u00d0"+
		"\n\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\5\5\u00e0"+
		"\n\5\3\5\3\5\7\5\u00e4\n\5\f\5\16\5\u00e7\13\5\3\5\3\5\3\5\7\5\u00ec\n"+
		"\5\f\5\16\5\u00ef\13\5\3\6\3\6\3\6\3\6\5\6\u00f5\n\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\b\3\b\5\b\u00ff\n\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\5\f\u0115\n\f\3\f\3\f\3"+
		"\f\3\f\7\f\u011b\n\f\f\f\16\f\u011e\13\f\3\r\3\r\5\r\u0122\n\r\3\r\5\r"+
		"\u0125\n\r\3\r\3\r\5\r\u0129\n\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\5"+
		"\17\u0132\n\17\3\17\3\17\3\17\3\17\3\17\7\17\u0139\n\17\f\17\16\17\u013c"+
		"\13\17\5\17\u013e\n\17\3\20\3\20\3\20\3\20\3\20\5\20\u0145\n\20\3\20\3"+
		"\20\3\20\3\20\3\20\5\20\u014c\n\20\5\20\u014e\n\20\3\21\3\21\3\21\3\21"+
		"\3\21\5\21\u0155\n\21\3\21\3\21\3\21\5\21\u015a\n\21\3\21\3\21\3\21\3"+
		"\21\3\21\5\21\u0161\n\21\3\22\3\22\3\22\7\22\u0166\n\22\f\22\16\22\u0169"+
		"\13\22\3\23\3\23\3\23\3\23\5\23\u016f\n\23\3\24\3\24\3\24\7\24\u0174\n"+
		"\24\f\24\16\24\u0177\13\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0180"+
		"\n\25\3\26\3\26\5\26\u0184\n\26\3\26\3\26\3\26\3\26\3\26\3\26\7\26\u018c"+
		"\n\26\f\26\16\26\u018f\13\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\7\27\u0198"+
		"\n\27\f\27\16\27\u019b\13\27\3\30\3\30\3\30\5\30\u01a0\n\30\3\30\3\30"+
		"\5\30\u01a4\n\30\3\30\3\30\5\30\u01a8\n\30\3\30\3\30\3\30\3\31\3\31\3"+
		"\31\3\31\7\31\u01b1\n\31\f\31\16\31\u01b4\13\31\3\32\3\32\3\32\5\32\u01b9"+
		"\n\32\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\7\34\u01c6"+
		"\n\34\f\34\16\34\u01c9\13\34\3\35\3\35\3\35\5\35\u01ce\n\35\3\35\3\35"+
		"\5\35\u01d2\n\35\3\35\3\35\5\35\u01d6\n\35\3\36\3\36\3\36\3\36\3\36\5"+
		"\36\u01dd\n\36\3\36\3\36\3\36\7\36\u01e2\n\36\f\36\16\36\u01e5\13\36\3"+
		"\37\3\37\3\37\5\37\u01ea\n\37\3\37\3\37\3\37\5\37\u01ef\n\37\5\37\u01f1"+
		"\n\37\3\37\3\37\5\37\u01f5\n\37\3 \3 \3 \3!\3!\5!\u01fc\n!\3!\3!\3!\7"+
		"!\u0201\n!\f!\16!\u0204\13!\3!\3!\3!\3\"\3\"\3\"\5\"\u020c\n\"\3\"\3\""+
		"\3\"\3#\3#\3#\3#\3#\6#\u0216\n#\r#\16#\u0217\3#\3#\3#\3#\3$\3$\6$\u0220"+
		"\n$\r$\16$\u0221\3$\3$\3$\3%\3%\3%\3%\3%\6%\u022c\n%\r%\16%\u022d\3%\3"+
		"%\5%\u0232\n%\3%\3%\3%\3&\3&\3&\3&\5&\u023b\n&\3&\3&\3&\7&\u0240\n&\f"+
		"&\16&\u0243\13&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3("+
		"\3(\3(\3(\3(\3(\3(\3(\3)\3)\3)\7)\u025e\n)\f)\16)\u0261\13)\3*\3*\3*\7"+
		"*\u0266\n*\f*\16*\u0269\13*\3+\5+\u026c\n+\3+\3+\3,\3,\3,\5,\u0273\n,"+
		"\3-\3-\3-\7-\u0278\n-\f-\16-\u027b\13-\3.\3.\3.\5.\u0280\n.\3/\3/\3/\7"+
		"/\u0285\n/\f/\16/\u0288\13/\3\60\3\60\3\60\7\60\u028d\n\60\f\60\16\60"+
		"\u0290\13\60\3\61\3\61\3\61\3\61\5\61\u0296\n\61\3\62\3\62\3\62\3\62\5"+
		"\62\u029c\n\62\3\63\3\63\3\63\3\63\5\63\u02a2\n\63\3\64\3\64\3\64\3\64"+
		"\5\64\u02a8\n\64\3\65\3\65\3\65\3\65\3\65\7\65\u02af\n\65\f\65\16\65\u02b2"+
		"\13\65\3\66\7\66\u02b5\n\66\f\66\16\66\u02b8\13\66\3\66\3\66\3\67\3\67"+
		"\3\67\7\67\u02bf\n\67\f\67\16\67\u02c2\13\67\38\38\38\38\38\38\78\u02ca"+
		"\n8\f8\168\u02cd\138\39\39\39\39\39\39\3:\3:\3:\3;\3;\3;\3;\3<\3<\3<\3"+
		"<\3<\3<\3<\3<\5<\u02e4\n<\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\5=\u02f2"+
		"\n=\3>\3>\3>\3?\3?\5?\u02f9\n?\3?\3?\3@\3@\3A\3A\3A\3A\3A\3B\3B\3B\3B"+
		"\3B\3C\3C\3C\3D\3D\3D\5D\u030f\nD\7D\u0311\nD\fD\16D\u0314\13D\3D\3D\3"+
		"E\3E\5E\u031a\nE\3F\3F\5F\u031e\nF\3G\3G\3G\3G\3H\3H\3H\5H\u0327\nH\3"+
		"H\3H\3H\5H\u032c\nH\3H\3H\3H\3H\3I\3I\3I\3I\3I\3I\5I\u0338\nI\5I\u033a"+
		"\nI\3J\3J\3J\3J\7J\u0340\nJ\fJ\16J\u0343\13J\5J\u0345\nJ\3J\3J\3J\3J\3"+
		"J\5J\u034c\nJ\3K\3K\3K\5K\u0351\nK\3L\3L\3M\3M\3N\3N\3O\3O\3P\3P\3Q\3"+
		"Q\3R\3R\3S\3S\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3Y\3Y\3Z\3Z\3[\3[\3[\3[\3"+
		"[\3[\3[\3[\3[\3[\3[\3[\3[\3[\5[\u037f\n[\3\\\3\\\5\\\u0383\n\\\3]\3]\3"+
		"]\5]\u0388\n]\3^\3^\5^\u038c\n^\3_\3_\5_\u0390\n_\3_\3_\3_\3`\3`\5`\u0397"+
		"\n`\3`\3`\3a\3a\3b\3b\3c\3c\3c\2\2d\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082"+
		"\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a"+
		"\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2"+
		"\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\2\13\3\2\t\n\3"+
		"\2de\3\2\r\26\4\2\6\6$.\3\2\60\61\4\2##\62\64\4\2>?yy\4\2\f\f{{\3\2Oy"+
		"\2\u03c8\2\u00cb\3\2\2\2\4\u00d1\3\2\2\2\6\u00d4\3\2\2\2\b\u00e5\3\2\2"+
		"\2\n\u00f4\3\2\2\2\f\u00f6\3\2\2\2\16\u00fe\3\2\2\2\20\u0100\3\2\2\2\22"+
		"\u0105\3\2\2\2\24\u0109\3\2\2\2\26\u010f\3\2\2\2\30\u0124\3\2\2\2\32\u012a"+
		"\3\2\2\2\34\u012c\3\2\2\2\36\u013f\3\2\2\2 \u014f\3\2\2\2\"\u0162\3\2"+
		"\2\2$\u016a\3\2\2\2&\u0170\3\2\2\2(\u017f\3\2\2\2*\u0183\3\2\2\2,\u0193"+
		"\3\2\2\2.\u019c\3\2\2\2\60\u01ac\3\2\2\2\62\u01b5\3\2\2\2\64\u01bd\3\2"+
		"\2\2\66\u01c0\3\2\2\28\u01ca\3\2\2\2:\u01dc\3\2\2\2<\u01e6\3\2\2\2>\u01f6"+
		"\3\2\2\2@\u01fb\3\2\2\2B\u0208\3\2\2\2D\u0210\3\2\2\2F\u021f\3\2\2\2H"+
		"\u0226\3\2\2\2J\u0236\3\2\2\2L\u0247\3\2\2\2N\u0250\3\2\2\2P\u025a\3\2"+
		"\2\2R\u0262\3\2\2\2T\u026b\3\2\2\2V\u026f\3\2\2\2X\u0274\3\2\2\2Z\u027c"+
		"\3\2\2\2\\\u0281\3\2\2\2^\u0289\3\2\2\2`\u0291\3\2\2\2b\u0297\3\2\2\2"+
		"d\u029d\3\2\2\2f\u02a3\3\2\2\2h\u02a9\3\2\2\2j\u02b6\3\2\2\2l\u02bb\3"+
		"\2\2\2n\u02c3\3\2\2\2p\u02ce\3\2\2\2r\u02d4\3\2\2\2t\u02d7\3\2\2\2v\u02db"+
		"\3\2\2\2x\u02f1\3\2\2\2z\u02f3\3\2\2\2|\u02f6\3\2\2\2~\u02fc\3\2\2\2\u0080"+
		"\u02fe\3\2\2\2\u0082\u0303\3\2\2\2\u0084\u0308\3\2\2\2\u0086\u030b\3\2"+
		"\2\2\u0088\u0319\3\2\2\2\u008a\u031d\3\2\2\2\u008c\u031f\3\2\2\2\u008e"+
		"\u0323\3\2\2\2\u0090\u0339\3\2\2\2\u0092\u034b\3\2\2\2\u0094\u0350\3\2"+
		"\2\2\u0096\u0352\3\2\2\2\u0098\u0354\3\2\2\2\u009a\u0356\3\2\2\2\u009c"+
		"\u0358\3\2\2\2\u009e\u035a\3\2\2\2\u00a0\u035c\3\2\2\2\u00a2\u035e\3\2"+
		"\2\2\u00a4\u0360\3\2\2\2\u00a6\u0362\3\2\2\2\u00a8\u0364\3\2\2\2\u00aa"+
		"\u0366\3\2\2\2\u00ac\u0368\3\2\2\2\u00ae\u036a\3\2\2\2\u00b0\u036c\3\2"+
		"\2\2\u00b2\u036e\3\2\2\2\u00b4\u037e\3\2\2\2\u00b6\u0380\3\2\2\2\u00b8"+
		"\u0387\3\2\2\2\u00ba\u038b\3\2\2\2\u00bc\u038f\3\2\2\2\u00be\u0394\3\2"+
		"\2\2\u00c0\u039a\3\2\2\2\u00c2\u039c\3\2\2\2\u00c4\u039e\3\2\2\2\u00c6"+
		"\u00c7\7x\2\2\u00c7\u00c8\7w\2\2\u00c8\u00c9\5\u00c2b\2\u00c9\u00ca\7"+
		"\3\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00c6\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc"+
		"\u00cf\3\2\2\2\u00cd\u00d0\5\6\4\2\u00ce\u00d0\5\4\3\2\u00cf\u00cd\3\2"+
		"\2\2\u00cf\u00ce\3\2\2\2\u00d0\3\3\2\2\2\u00d1\u00d2\5\b\5\2\u00d2\u00d3"+
		"\5&\24\2\u00d3\5\3\2\2\2\u00d4\u00d5\7\4\2\2\u00d5\u00d6\7\5\2\2\u00d6"+
		"\u00d7\7\u0084\2\2\u00d7\u00d8\7\6\2\2\u00d8\u00d9\5\u00c0a\2\u00d9\u00da"+
		"\7\3\2\2\u00da\u00db\5\b\5\2\u00db\7\3\2\2\2\u00dc\u00e0\5\n\6\2\u00dd"+
		"\u00e0\5\f\7\2\u00de\u00e0\5\34\17\2\u00df\u00dc\3\2\2\2\u00df\u00dd\3"+
		"\2\2\2\u00df\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e2\7\3\2\2\u00e2"+
		"\u00e4\3\2\2\2\u00e3\u00df\3\2\2\2\u00e4\u00e7\3\2\2\2\u00e5\u00e3\3\2"+
		"\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00ed\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e8"+
		"\u00e9\5\16\b\2\u00e9\u00ea\7\3\2\2\u00ea\u00ec\3\2\2\2\u00eb\u00e8\3"+
		"\2\2\2\u00ec\u00ef\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee"+
		"\t\3\2\2\2\u00ef\u00ed\3\2\2\2\u00f0\u00f5\5\20\t\2\u00f1\u00f5\5\22\n"+
		"\2\u00f2\u00f5\5\24\13\2\u00f3\u00f5\5\26\f\2\u00f4\u00f0\3\2\2\2\u00f4"+
		"\u00f1\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f4\u00f3\3\2\2\2\u00f5\13\3\2\2"+
		"\2\u00f6\u00f7\7\7\2\2\u00f7\u00f8\7\5\2\2\u00f8\u00f9\7\u0084\2\2\u00f9"+
		"\u00fa\7\6\2\2\u00fa\u00fb\5\u00c0a\2\u00fb\r\3\2\2\2\u00fc\u00ff\5 \21"+
		"\2\u00fd\u00ff\5\36\20\2\u00fe\u00fc\3\2\2\2\u00fe\u00fd\3\2\2\2\u00ff"+
		"\17\3\2\2\2\u0100\u0101\7\7\2\2\u0101\u0102\7j\2\2\u0102\u0103\7c\2\2"+
		"\u0103\u0104\5\u00c0a\2\u0104\21\3\2\2\2\u0105\u0106\7\7\2\2\u0106\u0107"+
		"\7\b\2\2\u0107\u0108\t\2\2\2\u0108\23\3\2\2\2\u0109\u010a\7\7\2\2\u010a"+
		"\u010b\7j\2\2\u010b\u010c\7T\2\2\u010c\u010d\7[\2\2\u010d\u010e\t\3\2"+
		"\2\u010e\25\3\2\2\2\u010f\u0114\7\7\2\2\u0110\u0111\7\13\2\2\u0111\u0115"+
		"\5\30\r\2\u0112\u0113\7j\2\2\u0113\u0115\7\13\2\2\u0114\u0110\3\2\2\2"+
		"\u0114\u0112\3\2\2\2\u0115\u011c\3\2\2\2\u0116\u0117\5\32\16\2\u0117\u0118"+
		"\7\6\2\2\u0118\u0119\5\u00c2b\2\u0119\u011b\3\2\2\2\u011a\u0116\3\2\2"+
		"\2\u011b\u011e\3\2\2\2\u011c\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011d\27"+
		"\3\2\2\2\u011e\u011c\3\2\2\2\u011f\u0122\7\u0084\2\2\u0120\u0122\5\u00c4"+
		"c\2\u0121\u011f\3\2\2\2\u0121\u0120\3\2\2\2\u0122\u0123\3\2\2\2\u0123"+
		"\u0125\7\f\2\2\u0124\u0121\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0128\3\2"+
		"\2\2\u0126\u0129\5\u00ba^\2\u0127\u0129\5\u00c4c\2\u0128\u0126\3\2\2\2"+
		"\u0128\u0127\3\2\2\2\u0129\31\3\2\2\2\u012a\u012b\t\4\2\2\u012b\33\3\2"+
		"\2\2\u012c\u012d\7\27\2\2\u012d\u0131\7\4\2\2\u012e\u012f\7\5\2\2\u012f"+
		"\u0130\7\u0084\2\2\u0130\u0132\7\6\2\2\u0131\u012e\3\2\2\2\u0131\u0132"+
		"\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u013d\5\u00c0a\2\u0134\u0135\7Y\2\2"+
		"\u0135\u013a\5\u00c0a\2\u0136\u0137\7\30\2\2\u0137\u0139\5\u00c0a\2\u0138"+
		"\u0136\3\2\2\2\u0139\u013c\3\2\2\2\u013a\u0138\3\2\2\2\u013a\u013b\3\2"+
		"\2\2\u013b\u013e\3\2\2\2\u013c\u013a\3\2\2\2\u013d\u0134\3\2\2\2\u013d"+
		"\u013e\3\2\2\2\u013e\35\3\2\2\2\u013f\u0140\7\7\2\2\u0140\u0141\7\31\2"+
		"\2\u0141\u0144\5z>\2\u0142\u0143\7X\2\2\u0143\u0145\5\u0090I\2\u0144\u0142"+
		"\3\2\2\2\u0144\u0145\3\2\2\2\u0145\u014d\3\2\2\2\u0146\u0147\7\32\2\2"+
		"\u0147\u014e\5(\25\2\u0148\u014b\7\33\2\2\u0149\u014a\7\32\2\2\u014a\u014c"+
		"\5(\25\2\u014b\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u014e\3\2\2\2\u014d"+
		"\u0146\3\2\2\2\u014d\u0148\3\2\2\2\u014e\37\3\2\2\2\u014f\u0150\7\7\2"+
		"\2\u0150\u0151\7\34\2\2\u0151\u0152\5\30\r\2\u0152\u0154\7\35\2\2\u0153"+
		"\u0155\5\"\22\2\u0154\u0153\3\2\2\2\u0154\u0155\3\2\2\2\u0155\u0156\3"+
		"\2\2\2\u0156\u0159\7\36\2\2\u0157\u0158\7X\2\2\u0158\u015a\5\u0090I\2"+
		"\u0159\u0157\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u0160\3\2\2\2\u015b\u015c"+
		"\7\37\2\2\u015c\u015d\5&\24\2\u015d\u015e\7 \2\2\u015e\u0161\3\2\2\2\u015f"+
		"\u0161\7\33\2\2\u0160\u015b\3\2\2\2\u0160\u015f\3\2\2\2\u0161!\3\2\2\2"+
		"\u0162\u0167\5$\23\2\u0163\u0164\7\30\2\2\u0164\u0166\5$\23\2\u0165\u0163"+
		"\3\2\2\2\u0166\u0169\3\2\2\2\u0167\u0165\3\2\2\2\u0167\u0168\3\2\2\2\u0168"+
		"#\3\2\2\2\u0169\u0167\3\2\2\2\u016a\u016b\7!\2\2\u016b\u016e\7\u0084\2"+
		"\2\u016c\u016d\7X\2\2\u016d\u016f\5\u0090I\2\u016e\u016c\3\2\2\2\u016e"+
		"\u016f\3\2\2\2\u016f%\3\2\2\2\u0170\u0175\5(\25\2\u0171\u0172\7\30\2\2"+
		"\u0172\u0174\5(\25\2\u0173\u0171\3\2\2\2\u0174\u0177\3\2\2\2\u0175\u0173"+
		"\3\2\2\2\u0175\u0176\3\2\2\2\u0176\'\3\2\2\2\u0177\u0175\3\2\2\2\u0178"+
		"\u0180\5*\26\2\u0179\u0180\5@!\2\u017a\u0180\5D#\2\u017b\u0180\5H%\2\u017c"+
		"\u0180\5L\'\2\u017d\u0180\5N(\2\u017e\u0180\5P)\2\u017f\u0178\3\2\2\2"+
		"\u017f\u0179\3\2\2\2\u017f\u017a\3\2\2\2\u017f\u017b\3\2\2\2\u017f\u017c"+
		"\3\2\2\2\u017f\u017d\3\2\2\2\u017f\u017e\3\2\2\2\u0180)\3\2\2\2\u0181"+
		"\u0184\5,\27\2\u0182\u0184\5\60\31\2\u0183\u0181\3\2\2\2\u0183\u0182\3"+
		"\2\2\2\u0184\u018d\3\2\2\2\u0185\u018c\5,\27\2\u0186\u018c\5\64\33\2\u0187"+
		"\u018c\5\60\31\2\u0188\u018c\5\66\34\2\u0189\u018c\5:\36\2\u018a\u018c"+
		"\5> \2\u018b\u0185\3\2\2\2\u018b\u0186\3\2\2\2\u018b\u0187\3\2\2\2\u018b"+
		"\u0188\3\2\2\2\u018b\u0189\3\2\2\2\u018b\u018a\3\2\2\2\u018c\u018f\3\2"+
		"\2\2\u018d\u018b\3\2\2\2\u018d\u018e\3\2\2\2\u018e\u0190\3\2\2\2\u018f"+
		"\u018d\3\2\2\2\u0190\u0191\7U\2\2\u0191\u0192\5(\25\2\u0192+\3\2\2\2\u0193"+
		"\u0194\7O\2\2\u0194\u0199\5.\30\2\u0195\u0196\7\30\2\2\u0196\u0198\5."+
		"\30\2\u0197\u0195\3\2\2\2\u0198\u019b\3\2\2\2\u0199\u0197\3\2\2\2\u0199"+
		"\u019a\3\2\2\2\u019a-\3\2\2\2\u019b\u0199\3\2\2\2\u019c\u019f\5z>\2\u019d"+
		"\u019e\7X\2\2\u019e\u01a0\5\u0090I\2\u019f\u019d\3\2\2\2\u019f\u01a0\3"+
		"\2\2\2\u01a0\u01a3\3\2\2\2\u01a1\u01a2\7Z\2\2\u01a2\u01a4\7[\2\2\u01a3"+
		"\u01a1\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01a7\3\2\2\2\u01a5\u01a6\7Y"+
		"\2\2\u01a6\u01a8\5z>\2\u01a7\u01a5\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a9"+
		"\3\2\2\2\u01a9\u01aa\7W\2\2\u01aa\u01ab\5(\25\2\u01ab/\3\2\2\2\u01ac\u01ad"+
		"\7P\2\2\u01ad\u01b2\5\62\32\2\u01ae\u01af\7\30\2\2\u01af\u01b1\5\62\32"+
		"\2\u01b0\u01ae\3\2\2\2\u01b1\u01b4\3\2\2\2\u01b2\u01b0\3\2\2\2\u01b2\u01b3"+
		"\3\2\2\2\u01b3\61\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b5\u01b8\5z>\2\u01b6"+
		"\u01b7\7X\2\2\u01b7\u01b9\5\u0090I\2\u01b8\u01b6\3\2\2\2\u01b8\u01b9\3"+
		"\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bb\7\32\2\2\u01bb\u01bc\5(\25\2\u01bc"+
		"\63\3\2\2\2\u01bd\u01be\7Q\2\2\u01be\u01bf\5(\25\2\u01bf\65\3\2\2\2\u01c0"+
		"\u01c1\7R\2\2\u01c1\u01c2\7S\2\2\u01c2\u01c7\58\35\2\u01c3\u01c4\7\30"+
		"\2\2\u01c4\u01c6\58\35\2\u01c5\u01c3\3\2\2\2\u01c6\u01c9\3\2\2\2\u01c7"+
		"\u01c5\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\67\3\2\2\2\u01c9\u01c7\3\2\2"+
		"\2\u01ca\u01d1\5z>\2\u01cb\u01cc\7X\2\2\u01cc\u01ce\5\u0090I\2\u01cd\u01cb"+
		"\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01cf\3\2\2\2\u01cf\u01d0\7\32\2\2"+
		"\u01d0\u01d2\5(\25\2\u01d1\u01cd\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d5"+
		"\3\2\2\2\u01d3\u01d4\7c\2\2\u01d4\u01d6\5\u00c0a\2\u01d5\u01d3\3\2\2\2"+
		"\u01d5\u01d6\3\2\2\2\u01d69\3\2\2\2\u01d7\u01d8\7T\2\2\u01d8\u01dd\7S"+
		"\2\2\u01d9\u01da\7]\2\2\u01da\u01db\7T\2\2\u01db\u01dd\7S\2\2\u01dc\u01d7"+
		"\3\2\2\2\u01dc\u01d9\3\2\2\2\u01dd\u01de\3\2\2\2\u01de\u01e3\5<\37\2\u01df"+
		"\u01e0\7\30\2\2\u01e0\u01e2\5<\37\2\u01e1\u01df\3\2\2\2\u01e2\u01e5\3"+
		"\2\2\2\u01e3\u01e1\3\2\2\2\u01e3\u01e4\3\2\2\2\u01e4;\3\2\2\2\u01e5\u01e3"+
		"\3\2\2\2\u01e6\u01e9\5(\25\2\u01e7\u01ea\7^\2\2\u01e8\u01ea\7_\2\2\u01e9"+
		"\u01e7\3\2\2\2\u01e9\u01e8\3\2\2\2\u01e9\u01ea\3\2\2\2\u01ea\u01f0\3\2"+
		"\2\2\u01eb\u01ee\7[\2\2\u01ec\u01ef\7d\2\2\u01ed\u01ef\7e\2\2\u01ee\u01ec"+
		"\3\2\2\2\u01ee\u01ed\3\2\2\2\u01ef\u01f1\3\2\2\2\u01f0\u01eb\3\2\2\2\u01f0"+
		"\u01f1\3\2\2\2\u01f1\u01f4\3\2\2\2\u01f2\u01f3\7c\2\2\u01f3\u01f5\5\u00c0"+
		"a\2\u01f4\u01f2\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5=\3\2\2\2\u01f6\u01f7"+
		"\7\\\2\2\u01f7\u01f8\5z>\2\u01f8?\3\2\2\2\u01f9\u01fc\7`\2\2\u01fa\u01fc"+
		"\7a\2\2\u01fb\u01f9\3\2\2\2\u01fb\u01fa\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd"+
		"\u0202\5B\"\2\u01fe\u01ff\7\30\2\2\u01ff\u0201\5B\"\2\u0200\u01fe\3\2"+
		"\2\2\u0201\u0204\3\2\2\2\u0202\u0200\3\2\2\2\u0202\u0203\3\2\2\2\u0203"+
		"\u0205\3\2\2\2\u0204\u0202\3\2\2\2\u0205\u0206\7b\2\2\u0206\u0207\5(\25"+
		"\2\u0207A\3\2\2\2\u0208\u020b\5z>\2\u0209\u020a\7X\2\2\u020a\u020c\5\u0090"+
		"I\2\u020b\u0209\3\2\2\2\u020b\u020c\3\2\2\2\u020c\u020d\3\2\2\2\u020d"+
		"\u020e\7W\2\2\u020e\u020f\5(\25\2\u020fC\3\2\2\2\u0210\u0211\7f\2\2\u0211"+
		"\u0212\7\35\2\2\u0212\u0213\5&\24\2\u0213\u0215\7\36\2\2\u0214\u0216\5"+
		"F$\2\u0215\u0214\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0215\3\2\2\2\u0217"+
		"\u0218\3\2\2\2\u0218\u0219\3\2\2\2\u0219\u021a\7j\2\2\u021a\u021b\7U\2"+
		"\2\u021b\u021c\5(\25\2\u021cE\3\2\2\2\u021d\u021e\7g\2\2\u021e\u0220\5"+
		"(\25\2\u021f\u021d\3\2\2\2\u0220\u0221\3\2\2\2\u0221\u021f\3\2\2\2\u0221"+
		"\u0222\3\2\2\2\u0222\u0223\3\2\2\2\u0223\u0224\7U\2\2\u0224\u0225\5(\25"+
		"\2\u0225G\3\2\2\2\u0226\u0227\7m\2\2\u0227\u0228\7\35\2\2\u0228\u0229"+
		"\5&\24\2\u0229\u022b\7\36\2\2\u022a\u022c\5J&\2\u022b\u022a\3\2\2\2\u022c"+
		"\u022d\3\2\2\2\u022d\u022b\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u022f\3\2"+
		"\2\2\u022f\u0231\7j\2\2\u0230\u0232\5z>\2\u0231\u0230\3\2\2\2\u0231\u0232"+
		"\3\2\2\2\u0232\u0233\3\2\2\2\u0233\u0234\7U\2\2\u0234\u0235\5(\25\2\u0235"+
		"I\3\2\2\2\u0236\u023a\7g\2\2\u0237\u0238\5z>\2\u0238\u0239\7X\2\2\u0239"+
		"\u023b\3\2\2\2\u023a\u0237\3\2\2\2\u023a\u023b\3\2\2\2\u023b\u023c\3\2"+
		"\2\2\u023c\u0241\5\u0090I\2\u023d\u023e\7\"\2\2\u023e\u0240\5\u0090I\2"+
		"\u023f\u023d\3\2\2\2\u0240\u0243\3\2\2\2\u0241\u023f\3\2\2\2\u0241\u0242"+
		"\3\2\2\2\u0242\u0244\3\2\2\2\u0243\u0241\3\2\2\2\u0244\u0245\7U\2\2\u0245"+
		"\u0246\5(\25\2\u0246K\3\2\2\2\u0247\u0248\7V\2\2\u0248\u0249\7\35\2\2"+
		"\u0249\u024a\5&\24\2\u024a\u024b\7\36\2\2\u024b\u024c\7k\2\2\u024c\u024d"+
		"\5(\25\2\u024d\u024e\7l\2\2\u024e\u024f\5(\25\2\u024fM\3\2\2\2\u0250\u0251"+
		"\7h\2\2\u0251\u0252\7\37\2\2\u0252\u0253\5&\24\2\u0253\u0254\7 \2\2\u0254"+
		"\u0255\7i\2\2\u0255\u0256\7#\2\2\u0256\u0257\7\37\2\2\u0257\u0258\5&\24"+
		"\2\u0258\u0259\7 \2\2\u0259O\3\2\2\2\u025a\u025f\5R*\2\u025b\u025c\7n"+
		"\2\2\u025c\u025e\5R*\2\u025d\u025b\3\2\2\2\u025e\u0261\3\2\2\2\u025f\u025d"+
		"\3\2\2\2\u025f\u0260\3\2\2\2\u0260Q\3\2\2\2\u0261\u025f\3\2\2\2\u0262"+
		"\u0267\5T+\2\u0263\u0264\7o\2\2\u0264\u0266\5T+\2\u0265\u0263\3\2\2\2"+
		"\u0266\u0269\3\2\2\2\u0267\u0265\3\2\2\2\u0267\u0268\3\2\2\2\u0268S\3"+
		"\2\2\2\u0269\u0267\3\2\2\2\u026a\u026c\7p\2\2\u026b\u026a\3\2\2\2\u026b"+
		"\u026c\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u026e\5V,\2\u026eU\3\2\2\2\u026f"+
		"\u0272\5X-\2\u0270\u0271\t\5\2\2\u0271\u0273\5X-\2\u0272\u0270\3\2\2\2"+
		"\u0272\u0273\3\2\2\2\u0273W\3\2\2\2\u0274\u0279\5Z.\2\u0275\u0276\7/\2"+
		"\2\u0276\u0278\5Z.\2\u0277\u0275\3\2\2\2\u0278\u027b\3\2\2\2\u0279\u0277"+
		"\3\2\2\2\u0279\u027a\3\2\2\2\u027aY\3\2\2\2\u027b\u0279\3\2\2\2\u027c"+
		"\u027f\5\\/\2\u027d\u027e\7q\2\2\u027e\u0280\5\\/\2\u027f\u027d\3\2\2"+
		"\2\u027f\u0280\3\2\2\2\u0280[\3\2\2\2\u0281\u0286\5^\60\2\u0282\u0283"+
		"\t\6\2\2\u0283\u0285\5^\60\2\u0284\u0282\3\2\2\2\u0285\u0288\3\2\2\2\u0286"+
		"\u0284\3\2\2\2\u0286\u0287\3\2\2\2\u0287]\3\2\2\2\u0288\u0286\3\2\2\2"+
		"\u0289\u028e\5`\61\2\u028a\u028b\t\7\2\2\u028b\u028d\5`\61\2\u028c\u028a"+
		"\3\2\2\2\u028d\u0290\3\2\2\2\u028e\u028c\3\2\2\2\u028e\u028f\3\2\2\2\u028f"+
		"_\3\2\2\2\u0290\u028e\3\2\2\2\u0291\u0295\5b\62\2\u0292\u0293\7r\2\2\u0293"+
		"\u0294\7s\2\2\u0294\u0296\5\u0090I\2\u0295\u0292\3\2\2\2\u0295\u0296\3"+
		"\2\2\2\u0296a\3\2\2\2\u0297\u029b\5d\63\2\u0298\u0299\7t\2\2\u0299\u029a"+
		"\7X\2\2\u029a\u029c\5\u0090I\2\u029b\u0298\3\2\2\2\u029b\u029c\3\2\2\2"+
		"\u029cc\3\2\2\2\u029d\u02a1\5f\64\2\u029e\u029f\7v\2\2\u029f\u02a0\7X"+
		"\2\2\u02a0\u02a2\5\u00b6\\\2\u02a1\u029e\3\2\2\2\u02a1\u02a2\3\2\2\2\u02a2"+
		"e\3\2\2\2\u02a3\u02a7\5h\65\2\u02a4\u02a5\7u\2\2\u02a5\u02a6\7X\2\2\u02a6"+
		"\u02a8\5\u00b6\\\2\u02a7\u02a4\3\2\2\2\u02a7\u02a8\3\2\2\2\u02a8g\3\2"+
		"\2\2\u02a9\u02b0\5j\66\2\u02aa\u02ab\7\6\2\2\u02ab\u02ac\7-\2\2\u02ac"+
		"\u02ad\3\2\2\2\u02ad\u02af\5\u0084C\2\u02ae\u02aa\3\2\2\2\u02af\u02b2"+
		"\3\2\2\2\u02b0\u02ae\3\2\2\2\u02b0\u02b1\3\2\2\2\u02b1i\3\2\2\2\u02b2"+
		"\u02b0\3\2\2\2\u02b3\u02b5\t\6\2\2\u02b4\u02b3\3\2\2\2\u02b5\u02b8\3\2"+
		"\2\2\u02b6\u02b4\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7\u02b9\3\2\2\2\u02b8"+
		"\u02b6\3\2\2\2\u02b9\u02ba\5l\67\2\u02bak\3\2\2\2\u02bb\u02c0\5n8\2\u02bc"+
		"\u02bd\7\65\2\2\u02bd\u02bf\5n8\2\u02be\u02bc\3\2\2\2\u02bf\u02c2\3\2"+
		"\2\2\u02c0\u02be\3\2\2\2\u02c0\u02c1\3\2\2\2\u02c1m\3\2\2\2\u02c2\u02c0"+
		"\3\2\2\2\u02c3\u02cb\5x=\2\u02c4\u02ca\5p9\2\u02c5\u02ca\5t;\2\u02c6\u02ca"+
		"\5v<\2\u02c7\u02ca\5r:\2\u02c8\u02ca\5\u0086D\2\u02c9\u02c4\3\2\2\2\u02c9"+
		"\u02c5\3\2\2\2\u02c9\u02c6\3\2\2\2\u02c9\u02c7\3\2\2\2\u02c9\u02c8\3\2"+
		"\2\2\u02ca\u02cd\3\2\2\2\u02cb\u02c9\3\2\2\2\u02cb\u02cc\3\2\2\2\u02cc"+
		"o\3\2\2\2\u02cd\u02cb\3\2\2\2\u02ce\u02cf\7\66\2\2\u02cf\u02d0\7\66\2"+
		"\2\u02d0\u02d1\5&\24\2\u02d1\u02d2\7\67\2\2\u02d2\u02d3\7\67\2\2\u02d3"+
		"q\3\2\2\2\u02d4\u02d5\7\66\2\2\u02d5\u02d6\7\67\2\2\u02d6s\3\2\2\2\u02d7"+
		"\u02d8\7\66\2\2\u02d8\u02d9\5&\24\2\u02d9\u02da\7\67\2\2\u02dau\3\2\2"+
		"\2\u02db\u02e3\78\2\2\u02dc\u02e4\5\u00c4c\2\u02dd\u02e4\5\u00c2b\2\u02de"+
		"\u02e4\7\u0084\2\2\u02df\u02e4\5|?\2\u02e0\u02e4\5z>\2\u02e1\u02e4\5~"+
		"@\2\u02e2\u02e4\5\u00b4[\2\u02e3\u02dc\3\2\2\2\u02e3\u02dd\3\2\2\2\u02e3"+
		"\u02de\3\2\2\2\u02e3\u02df\3\2\2\2\u02e3\u02e0\3\2\2\2\u02e3\u02e1\3\2"+
		"\2\2\u02e3\u02e2\3\2\2\2\u02e4w\3\2\2\2\u02e5\u02f2\7|\2\2\u02e6\u02f2"+
		"\7}\2\2\u02e7\u02f2\5\u00c2b\2\u02e8\u02f2\5z>\2\u02e9\u02f2\5|?\2\u02ea"+
		"\u02f2\5~@\2\u02eb\u02f2\5\u0092J\2\u02ec\u02f2\5\u0084C\2\u02ed\u02f2"+
		"\5\u0080A\2\u02ee\u02f2\5\u0082B\2\u02ef\u02f2\5\u00be`\2\u02f0\u02f2"+
		"\5\u008aF\2\u02f1\u02e5\3\2\2\2\u02f1\u02e6\3\2\2\2\u02f1\u02e7\3\2\2"+
		"\2\u02f1\u02e8\3\2\2\2\u02f1\u02e9\3\2\2\2\u02f1\u02ea\3\2\2\2\u02f1\u02eb"+
		"\3\2\2\2\u02f1\u02ec\3\2\2\2\u02f1\u02ed\3\2\2\2\u02f1\u02ee\3\2\2\2\u02f1"+
		"\u02ef\3\2\2\2\u02f1\u02f0\3\2\2\2\u02f2y\3\2\2\2\u02f3\u02f4\7!\2\2\u02f4"+
		"\u02f5\5\30\r\2\u02f5{\3\2\2\2\u02f6\u02f8\7\35\2\2\u02f7\u02f9\5&\24"+
		"\2\u02f8\u02f7\3\2\2\2\u02f8\u02f9\3\2\2\2\u02f9\u02fa\3\2\2\2\u02fa\u02fb"+
		"\7\36\2\2\u02fb}\3\2\2\2\u02fc\u02fd\79\2\2\u02fd\177\3\2\2\2\u02fe\u02ff"+
		"\7\t\2\2\u02ff\u0300\7\37\2\2\u0300\u0301\5&\24\2\u0301\u0302\7 \2\2\u0302"+
		"\u0081\3\2\2\2\u0303\u0304\7\n\2\2\u0304\u0305\7\37\2\2\u0305\u0306\5"+
		"&\24\2\u0306\u0307\7 \2\2\u0307\u0083\3\2\2\2\u0308\u0309\5\30\r\2\u0309"+
		"\u030a\5\u0086D\2\u030a\u0085\3\2\2\2\u030b\u0312\7\35\2\2\u030c\u030e"+
		"\5\u0088E\2\u030d\u030f\7\30\2\2\u030e\u030d\3\2\2\2\u030e\u030f\3\2\2"+
		"\2\u030f\u0311\3\2\2\2\u0310\u030c\3\2\2\2\u0311\u0314\3\2\2\2\u0312\u0310"+
		"\3\2\2\2\u0312\u0313\3\2\2\2\u0313\u0315\3\2\2\2\u0314\u0312\3\2\2\2\u0315"+
		"\u0316\7\36\2\2\u0316\u0087\3\2\2\2\u0317\u031a\5(\25\2\u0318\u031a\7"+
		"{\2\2\u0319\u0317\3\2\2\2\u0319\u0318\3\2\2\2\u031a\u0089\3\2\2\2\u031b"+
		"\u031e\5\u008cG\2\u031c\u031e\5\u008eH\2\u031d\u031b\3\2\2\2\u031d\u031c"+
		"\3\2\2\2\u031e\u008b\3\2\2\2\u031f\u0320\5\30\r\2\u0320\u0321\7:\2\2\u0321"+
		"\u0322\7}\2\2\u0322\u008d\3\2\2\2\u0323\u0324\7\34\2\2\u0324\u0326\7\35"+
		"\2\2\u0325\u0327\5\"\22\2\u0326\u0325\3\2\2\2\u0326\u0327\3\2\2\2\u0327"+
		"\u0328\3\2\2\2\u0328\u032b\7\36\2\2\u0329\u032a\7X\2\2\u032a\u032c\5\u0090"+
		"I\2\u032b\u0329\3\2\2\2\u032b\u032c\3\2\2\2\u032c\u032d\3\2\2\2\u032d"+
		"\u032e\7\37\2\2\u032e\u032f\5&\24\2\u032f\u0330\7 \2\2\u0330\u008f\3\2"+
		"\2\2\u0331\u0332\7\35\2\2\u0332\u033a\7\36\2\2\u0333\u0337\5\u0094K\2"+
		"\u0334\u0338\7{\2\2\u0335\u0338\7#\2\2\u0336\u0338\7\60\2\2\u0337\u0334"+
		"\3\2\2\2\u0337\u0335\3\2\2\2\u0337\u0336\3\2\2\2\u0337\u0338\3\2\2\2\u0338"+
		"\u033a\3\2\2\2\u0339\u0331\3\2\2\2\u0339\u0333\3\2\2\2\u033a\u0091\3\2"+
		"\2\2\u033b\u0344\7\37\2\2\u033c\u0341\5\u00bc_\2\u033d\u033e\7\30\2\2"+
		"\u033e\u0340\5\u00bc_\2\u033f\u033d\3\2\2\2\u0340\u0343\3\2\2\2\u0341"+
		"\u033f\3\2\2\2\u0341\u0342\3\2\2\2\u0342\u0345\3\2\2\2\u0343\u0341\3\2"+
		"\2\2\u0344\u033c\3\2\2\2\u0344\u0345\3\2\2\2\u0345\u0346\3\2\2\2\u0346"+
		"\u034c\7 \2\2\u0347\u0348\7;\2\2\u0348\u0349\5&\24\2\u0349\u034a\7<\2"+
		"\2\u034a\u034c\3\2\2\2\u034b\u033b\3\2\2\2\u034b\u0347\3\2\2\2\u034c\u0093"+
		"\3\2\2\2\u034d\u0351\7=\2\2\u034e\u0351\5\u0096L\2\u034f\u0351\5\u00b8"+
		"]\2\u0350\u034d\3\2\2\2\u0350\u034e\3\2\2\2\u0350\u034f\3\2\2\2\u0351"+
		"\u0095\3\2\2\2\u0352\u0353\t\b\2\2\u0353\u0097\3\2\2\2\u0354\u0355\7@"+
		"\2\2\u0355\u0099\3\2\2\2\u0356\u0357\7A\2\2\u0357\u009b\3\2\2\2\u0358"+
		"\u0359\7B\2\2\u0359\u009d\3\2\2\2\u035a\u035b\7C\2\2\u035b\u009f\3\2\2"+
		"\2\u035c\u035d\7D\2\2\u035d\u00a1\3\2\2\2\u035e\u035f\7E\2\2\u035f\u00a3"+
		"\3\2\2\2\u0360\u0361\7F\2\2\u0361\u00a5\3\2\2\2\u0362\u0363\7G\2\2\u0363"+
		"\u00a7\3\2\2\2\u0364\u0365\7H\2\2\u0365\u00a9\3\2\2\2\u0366\u0367\7I\2"+
		"\2\u0367\u00ab\3\2\2\2\u0368\u0369\7J\2\2\u0369\u00ad\3\2\2\2\u036a\u036b"+
		"\7K\2\2\u036b\u00af\3\2\2\2\u036c\u036d\7L\2\2\u036d\u00b1\3\2\2\2\u036e"+
		"\u036f\7M\2\2\u036f\u00b3\3\2\2\2\u0370\u037f\5\u0098M\2\u0371\u037f\5"+
		"\u009aN\2\u0372\u037f\5\u009cO\2\u0373\u037f\5\u009eP\2\u0374\u037f\5"+
		"\u00a0Q\2\u0375\u037f\5\u00a2R\2\u0376\u037f\5\u00a4S\2\u0377\u037f\5"+
		"\u00a6T\2\u0378\u037f\5\u00acW\2\u0379\u037f\5\u00aeX\2\u037a\u037f\5"+
		"\u00b0Y\2\u037b\u037f\5\u00a8U\2\u037c\u037f\5\u00aaV\2\u037d\u037f\5"+
		"\u00b2Z\2\u037e\u0370\3\2\2\2\u037e\u0371\3\2\2\2\u037e\u0372\3\2\2\2"+
		"\u037e\u0373\3\2\2\2\u037e\u0374\3\2\2\2\u037e\u0375\3\2\2\2\u037e\u0376"+
		"\3\2\2\2\u037e\u0377\3\2\2\2\u037e\u0378\3\2\2\2\u037e\u0379\3\2\2\2\u037e"+
		"\u037a\3\2\2\2\u037e\u037b\3\2\2\2\u037e\u037c\3\2\2\2\u037e\u037d\3\2"+
		"\2\2\u037f\u00b5\3\2\2\2\u0380\u0382\5\u00b8]\2\u0381\u0383\7{\2\2\u0382"+
		"\u0381\3\2\2\2\u0382\u0383\3\2\2\2\u0383\u00b7\3\2\2\2\u0384\u0388\7N"+
		"\2\2\u0385\u0388\5\u00b4[\2\u0386\u0388\7|\2\2\u0387\u0384\3\2\2\2\u0387"+
		"\u0385\3\2\2\2\u0387\u0386\3\2\2\2\u0388\u00b9\3\2\2\2\u0389\u038c\7\u0084"+
		"\2\2\u038a\u038c\5\u00b4[\2\u038b\u0389\3\2\2\2\u038b\u038a\3\2\2\2\u038c"+
		"\u00bb\3\2\2\2\u038d\u0390\5(\25\2\u038e\u0390\7\u0084\2\2\u038f\u038d"+
		"\3\2\2\2\u038f\u038e\3\2\2\2\u0390\u0391\3\2\2\2\u0391\u0392\t\t\2\2\u0392"+
		"\u0393\5(\25\2\u0393\u00bd\3\2\2\2\u0394\u0396\7\66\2\2\u0395\u0397\5"+
		"&\24\2\u0396\u0395\3\2\2\2\u0396\u0397\3\2\2\2\u0397\u0398\3\2\2\2\u0398"+
		"\u0399\7\67\2\2\u0399\u00bf\3\2\2\2\u039a\u039b\5\u00c2b\2\u039b\u00c1"+
		"\3\2\2\2\u039c\u039d\7z\2\2\u039d\u00c3\3\2\2\2\u039e\u039f\t\n\2\2\u039f"+
		"\u00c5\3\2\2\2]\u00cb\u00cf\u00df\u00e5\u00ed\u00f4\u00fe\u0114\u011c"+
		"\u0121\u0124\u0128\u0131\u013a\u013d\u0144\u014b\u014d\u0154\u0159\u0160"+
		"\u0167\u016e\u0175\u017f\u0183\u018b\u018d\u0199\u019f\u01a3\u01a7\u01b2"+
		"\u01b8\u01c7\u01cd\u01d1\u01d5\u01dc\u01e3\u01e9\u01ee\u01f0\u01f4\u01fb"+
		"\u0202\u020b\u0217\u0221\u022d\u0231\u023a\u0241\u025f\u0267\u026b\u0272"+
		"\u0279\u027f\u0286\u028e\u0295\u029b\u02a1\u02a7\u02b0\u02b6\u02c0\u02c9"+
		"\u02cb\u02e3\u02f1\u02f8\u030e\u0312\u0319\u031d\u0326\u032b\u0337\u0339"+
		"\u0341\u0344\u034b\u0350\u037e\u0382\u0387\u038b\u038f\u0396";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}