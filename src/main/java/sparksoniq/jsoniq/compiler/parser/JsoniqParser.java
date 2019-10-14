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
		T__66=67, T__67=68, NullLiteral=69, Literal=70, NumericLiteral=71, BooleanLiteral=72, 
		Kfor=73, Klet=74, Kwhere=75, Kgroup=76, Kby=77, Korder=78, Kreturn=79, 
		Kif=80, Kin=81, Kas=82, Kat=83, Kallowing=84, Kempty=85, Kcount=86, Kstable=87, 
		Kascending=88, Kdescending=89, Ksome=90, Kevery=91, Ksatisfies=92, Kcollation=93, 
		Kgreatest=94, Kleast=95, Kswitch=96, Kcase=97, Ktry=98, Kcatch=99, Kdefault=100, 
		Kthen=101, Kelse=102, Ktypeswitch=103, Kor=104, Kand=105, Knot=106, Kto=107, 
		Kinstance=108, Kof=109, Ktreat=110, Kcast=111, Kcastable=112, Kversion=113, 
		Kjsoniq=114, Kjson=115, STRING=116, IntegerLiteral=117, DecimalLiteral=118, 
		DoubleLiteral=119, WS=120, NCName=121, XQComment=122, ContentChar=123;
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
		RULE_keyWordBoolean = 66, RULE_singleType = 67, RULE_keyWordDuration = 68, 
		RULE_atomicType = 69, RULE_nCNameOrKeyWord = 70, RULE_pairConstructor = 71, 
		RULE_arrayConstructor = 72, RULE_uriLiteral = 73, RULE_keyWords = 74, 
		RULE_stringLiteral = 75;
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
		"keyWordBoolean", "singleType", "keyWordDuration", "atomicType", "nCNameOrKeyWord", 
		"pairConstructor", "arrayConstructor", "uriLiteral", "keyWords", "stringLiteral"
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
		"'|}'", "'item'", "'object'", "'array'", "'boolean'", "'duration'", "'atomic'", 
		"'string'", "'integer'", "'decimal'", "'double'", "'null'", null, null, 
		null, "'for'", "'let'", "'where'", "'group'", "'by'", "'order'", "'return'", 
		"'if'", "'in'", "'as'", "'at'", "'allowing'", "'empty'", "'count'", "'stable'", 
		"'ascending'", "'descending'", "'some'", "'every'", "'satisfies'", "'collation'", 
		"'greatest'", "'least'", "'switch'", "'case'", "'try'", "'catch'", "'default'", 
		"'then'", "'else'", "'typeswitch'", "'or'", "'and'", "'not'", "'to'", 
		"'instance'", "'of'", "'treat'", "'cast'", "'castable'", "'version'", 
		"'jsoniq'", "'json-item'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, "NullLiteral", "Literal", 
		"NumericLiteral", "BooleanLiteral", "Kfor", "Klet", "Kwhere", "Kgroup", 
		"Kby", "Korder", "Kreturn", "Kif", "Kin", "Kas", "Kat", "Kallowing", "Kempty", 
		"Kcount", "Kstable", "Kascending", "Kdescending", "Ksome", "Kevery", "Ksatisfies", 
		"Kcollation", "Kgreatest", "Kleast", "Kswitch", "Kcase", "Ktry", "Kcatch", 
		"Kdefault", "Kthen", "Kelse", "Ktypeswitch", "Kor", "Kand", "Knot", "Kto", 
		"Kinstance", "Kof", "Ktreat", "Kcast", "Kcastable", "Kversion", "Kjsoniq", 
		"Kjson", "STRING", "IntegerLiteral", "DecimalLiteral", "DoubleLiteral", 
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
			setState(157);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(152);
				match(Kjsoniq);
				setState(153);
				match(Kversion);
				setState(154);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(155);
				match(T__0);
				}
				break;
			}
			setState(161);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(159);
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
			case NullLiteral:
			case Literal:
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
			case NCName:
				{
				setState(160);
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
			setState(163);
			prolog();
			setState(164);
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
			setState(166);
			match(T__1);
			setState(167);
			match(T__2);
			setState(168);
			match(NCName);
			setState(169);
			match(T__3);
			setState(170);
			uriLiteral();
			setState(171);
			match(T__0);
			setState(172);
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
			setState(185);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(179);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(174);
						defaultCollationDecl();
						}
						break;
					case 2:
						{
						setState(175);
						orderingModeDecl();
						}
						break;
					case 3:
						{
						setState(176);
						emptyOrderDecl();
						}
						break;
					case 4:
						{
						setState(177);
						decimalFormatDecl();
						}
						break;
					case 5:
						{
						setState(178);
						moduleImport();
						}
						break;
					}
					setState(181);
					match(T__0);
					}
					} 
				}
				setState(187);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(196);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(190);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(188);
					functionDecl();
					}
					break;
				case 2:
					{
					setState(189);
					varDecl();
					}
					break;
				}
				setState(192);
				match(T__0);
				}
				}
				setState(198);
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
			setState(199);
			match(T__4);
			setState(200);
			match(Kdefault);
			setState(201);
			match(Kcollation);
			setState(202);
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
			setState(204);
			match(T__4);
			setState(205);
			match(T__5);
			setState(206);
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
			setState(208);
			match(T__4);
			setState(209);
			match(Kdefault);
			setState(210);
			match(Korder);
			setState(211);
			match(Kempty);
			setState(212);
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
			setState(214);
			match(T__4);
			setState(223);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				{
				setState(215);
				match(T__8);
				setState(218);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(216);
					match(NCName);
					setState(217);
					match(T__9);
					}
					break;
				}
				setState(220);
				match(NCName);
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(221);
				match(Kdefault);
				setState(222);
				match(T__8);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(231);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) {
				{
				{
				setState(225);
				dfPropertyName();
				setState(226);
				match(T__3);
				setState(227);
				stringLiteral();
				}
				}
				setState(233);
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
			setState(234);
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
			setState(236);
			match(T__20);
			setState(237);
			match(T__1);
			setState(241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(238);
				match(T__2);
				setState(239);
				match(NCName);
				setState(240);
				match(T__3);
				}
			}

			setState(243);
			uriLiteral();
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(244);
				match(Kat);
				setState(245);
				uriLiteral();
				setState(250);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__21) {
					{
					{
					setState(246);
					match(T__21);
					setState(247);
					uriLiteral();
					}
					}
					setState(252);
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
			setState(255);
			match(T__4);
			setState(256);
			match(T__22);
			setState(257);
			varRef();
			setState(260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(258);
				match(Kas);
				setState(259);
				sequenceType();
				}
			}

			setState(269);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				{
				{
				setState(262);
				match(T__23);
				setState(263);
				exprSingle();
				}
				}
				break;
			case T__24:
				{
				{
				setState(264);
				match(T__24);
				setState(267);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__23) {
					{
					setState(265);
					match(T__23);
					setState(266);
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
			setState(271);
			match(T__4);
			setState(272);
			match(T__25);
			setState(275);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(273);
				match(NCName);
				setState(274);
				match(T__9);
				}
				break;
			}
			setState(277);
			match(NCName);
			setState(278);
			match(T__26);
			setState(280);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(279);
				paramList();
				}
			}

			setState(282);
			match(T__27);
			setState(285);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(283);
				match(Kas);
				setState(284);
				sequenceType();
				}
			}

			setState(292);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				{
				setState(287);
				match(T__28);
				setState(288);
				expr();
				setState(289);
				match(T__29);
				}
				break;
			case T__24:
				{
				setState(291);
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
			setState(294);
			match(T__30);
			setState(295);
			match(NCName);
			setState(298);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(296);
				match(Kas);
				setState(297);
				sequenceType();
				}
			}

			setState(309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(300);
				match(T__21);
				setState(301);
				match(T__30);
				setState(302);
				match(NCName);
				setState(305);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(303);
					match(Kas);
					setState(304);
					sequenceType();
					}
				}

				}
				}
				setState(311);
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
			setState(312);
			exprSingle();
			setState(317);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(313);
				match(T__21);
				setState(314);
				exprSingle();
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
			setState(327);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(320);
				flowrExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(321);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(322);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(323);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(324);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(325);
				tryCatchExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(326);
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
		public ExprSingleContext return_Expr;
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
			setState(331);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(329);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(330);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(341);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & ((1L << (Kfor - 73)) | (1L << (Klet - 73)) | (1L << (Kwhere - 73)) | (1L << (Kgroup - 73)) | (1L << (Korder - 73)) | (1L << (Kcount - 73)) | (1L << (Kstable - 73)))) != 0)) {
				{
				setState(339);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(333);
					forClause();
					}
					break;
				case Kwhere:
					{
					setState(334);
					whereClause();
					}
					break;
				case Klet:
					{
					setState(335);
					letClause();
					}
					break;
				case Kgroup:
					{
					setState(336);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(337);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(338);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(343);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(344);
			match(Kreturn);
			setState(345);
			((FlowrExprContext)_localctx).return_Expr = exprSingle();
			}
		}
		catch (RecognitionException re) {
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
			setState(347);
			match(Kfor);
			setState(348);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(349);
				match(T__21);
				setState(350);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(355);
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
			setState(356);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(359);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(357);
				match(Kas);
				setState(358);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(361);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(362);
				match(Kempty);
				}
			}

			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(365);
				match(Kat);
				setState(366);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(369);
			match(Kin);
			setState(370);
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
			setState(372);
			match(Klet);
			setState(373);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(378);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(374);
				match(T__21);
				setState(375);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(380);
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
			setState(381);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(382);
				match(Kas);
				setState(383);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(386);
			match(T__23);
			setState(387);
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
			setState(389);
			match(Kwhere);
			setState(390);
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
			setState(392);
			match(Kgroup);
			setState(393);
			match(Kby);
			setState(394);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(395);
				match(T__21);
				setState(396);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(401);
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
			setState(402);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(409);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23 || _la==Kas) {
				{
				setState(405);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(403);
					match(Kas);
					setState(404);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(407);
				((GroupByVarContext)_localctx).decl = match(T__23);
				setState(408);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(413);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(411);
				match(Kcollation);
				setState(412);
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
			setState(420);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(415);
				match(Korder);
				setState(416);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(417);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(418);
				match(Korder);
				setState(419);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(422);
			orderByExpr();
			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(423);
				match(T__21);
				setState(424);
				orderByExpr();
				}
				}
				setState(429);
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
			setState(430);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(433);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(431);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(432);
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
			setState(440);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(435);
				match(Kempty);
				setState(438);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(436);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(437);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(444);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(442);
				match(Kcollation);
				setState(443);
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
			setState(446);
			match(Kcount);
			setState(447);
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
			setState(451);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(449);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(450);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(453);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(454);
				match(T__21);
				setState(455);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(460);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(461);
			match(Ksatisfies);
			setState(462);
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
			setState(464);
			varRef();
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

			setState(469);
			match(Kin);
			setState(470);
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
		public List<SwitchCaseClauseContext> cses = new ArrayList<SwitchCaseClauseContext>();
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
			setState(472);
			match(Kswitch);
			setState(473);
			match(T__26);
			setState(474);
			((SwitchExprContext)_localctx).cond = expr();
			setState(475);
			match(T__27);
			setState(477); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(476);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cses.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(479); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(481);
			match(Kdefault);
			setState(482);
			match(Kreturn);
			setState(483);
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
			setState(487); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(485);
				match(Kcase);
				setState(486);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(489); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(491);
			match(Kreturn);
			setState(492);
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
			setState(494);
			match(Ktypeswitch);
			setState(495);
			match(T__26);
			setState(496);
			expr();
			setState(497);
			match(T__27);
			setState(499); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(498);
				caseClause();
				}
				}
				setState(501); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(503);
			match(Kdefault);
			setState(505);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(504);
				varRef();
				}
			}

			setState(507);
			match(Kreturn);
			setState(508);
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
			setState(510);
			match(Kcase);
			setState(514);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__30) {
				{
				setState(511);
				varRef();
				setState(512);
				match(Kas);
				}
			}

			setState(516);
			sequenceType();
			setState(521);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__31) {
				{
				{
				setState(517);
				match(T__31);
				setState(518);
				sequenceType();
				}
				}
				setState(523);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(524);
			match(Kreturn);
			setState(525);
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
		public ExprContext testCondition;
		public ExprSingleContext branch;
		public ExprSingleContext elseBranch;
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
			setState(527);
			match(Kif);
			setState(528);
			match(T__26);
			setState(529);
			((IfExprContext)_localctx).testCondition = expr();
			setState(530);
			match(T__27);
			setState(531);
			match(Kthen);
			setState(532);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(533);
			match(Kelse);
			setState(534);
			((IfExprContext)_localctx).elseBranch = exprSingle();
			}
		}
		catch (RecognitionException re) {
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
			setState(536);
			match(Ktry);
			setState(537);
			match(T__28);
			setState(538);
			expr();
			setState(539);
			match(T__29);
			setState(540);
			match(Kcatch);
			setState(541);
			match(T__32);
			setState(542);
			match(T__28);
			setState(543);
			expr();
			setState(544);
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
		public AndExprContext mainExpr;
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
			setState(546);
			((OrExprContext)_localctx).mainExpr = andExpr();
			setState(551);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(547);
					match(Kor);
					setState(548);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(553);
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
		public NotExprContext mainExpr;
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
			setState(554);
			((AndExprContext)_localctx).mainExpr = notExpr();
			setState(559);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(555);
					match(Kand);
					setState(556);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(561);
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
		public ComparisonExprContext mainExpr;
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
			setState(563);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				{
				setState(562);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(565);
			((NotExprContext)_localctx).mainExpr = comparisonExpr();
			}
		}
		catch (RecognitionException re) {
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
		public StringConcatExprContext mainExpr;
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
		public Token _tset1018;
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
			setState(567);
			((ComparisonExprContext)_localctx).mainExpr = stringConcatExpr();
			setState(570);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) {
				{
				setState(568);
				((ComparisonExprContext)_localctx)._tset1018 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1018 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1018);
				setState(569);
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
		public RangeExprContext mainExpr;
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
			setState(572);
			((StringConcatExprContext)_localctx).mainExpr = rangeExpr();
			setState(577);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__44) {
				{
				{
				setState(573);
				match(T__44);
				setState(574);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(579);
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
		public AdditiveExprContext mainExpr;
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
			setState(580);
			((RangeExprContext)_localctx).mainExpr = additiveExpr();
			setState(583);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				setState(581);
				match(Kto);
				setState(582);
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
		public MultiplicativeExprContext mainExpr;
		public Token s46;
		public List<Token> op = new ArrayList<Token>();
		public Token s47;
		public Token _tset1127;
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
			setState(585);
			((AdditiveExprContext)_localctx).mainExpr = multiplicativeExpr();
			setState(590);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(586);
					((AdditiveExprContext)_localctx)._tset1127 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__45 || _la==T__46) ) {
						((AdditiveExprContext)_localctx)._tset1127 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1127);
					setState(587);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(592);
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
		public InstanceOfExprContext mainExpr;
		public Token s33;
		public List<Token> op = new ArrayList<Token>();
		public Token s48;
		public Token s49;
		public Token s50;
		public Token _tset1155;
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
			setState(593);
			((MultiplicativeExprContext)_localctx).mainExpr = instanceOfExpr();
			setState(598);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) {
				{
				{
				setState(594);
				((MultiplicativeExprContext)_localctx)._tset1155 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__32) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1155 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1155);
				setState(595);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(600);
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
		public TreatExprContext mainExpr;
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
			setState(601);
			((InstanceOfExprContext)_localctx).mainExpr = treatExpr();
			setState(605);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(602);
				match(Kinstance);
				setState(603);
				match(Kof);
				setState(604);
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
		public CastableExprContext mainExpr;
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
			setState(607);
			((TreatExprContext)_localctx).mainExpr = castableExpr();
			setState(611);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(608);
				match(Ktreat);
				setState(609);
				match(Kas);
				setState(610);
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
		public CastExprContext mainExpr;
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
			setState(613);
			((CastableExprContext)_localctx).mainExpr = castExpr();
			setState(617);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(614);
				match(Kcastable);
				setState(615);
				match(Kas);
				setState(616);
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
		public UnaryExprContext mainExpr;
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
			setState(619);
			((CastExprContext)_localctx).mainExpr = unaryExpr();
			setState(623);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				{
				setState(620);
				match(Kcast);
				setState(621);
				match(Kas);
				setState(622);
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
		public Token _tset1271;
		public SimpleMapExprContext mainExpr;
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
			setState(628);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45 || _la==T__46) {
				{
				{
				setState(625);
				((UnaryExprContext)_localctx)._tset1271 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__45 || _la==T__46) ) {
					((UnaryExprContext)_localctx)._tset1271 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1271);
				}
				}
				setState(630);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(631);
			((UnaryExprContext)_localctx).mainExpr = simpleMapExpr();
			}
		}
		catch (RecognitionException re) {
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
		public PostFixExprContext mainExpr;
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
			setState(633);
			((SimpleMapExprContext)_localctx).mainExpr = postFixExpr();
			setState(638);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__50) {
				{
				{
				setState(634);
				match(T__50);
				setState(635);
				postFixExpr();
				}
				}
				setState(640);
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
		public PrimaryExprContext mainExpr;
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
			setState(641);
			((PostFixExprContext)_localctx).mainExpr = primaryExpr();
			setState(648);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(646);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
					case 1:
						{
						setState(642);
						((PostFixExprContext)_localctx).al = arrayLookup();
						}
						break;
					case 2:
						{
						setState(643);
						((PostFixExprContext)_localctx).pr = predicate();
						}
						break;
					case 3:
						{
						setState(644);
						((PostFixExprContext)_localctx).ol = objectLookup();
						}
						break;
					case 4:
						{
						setState(645);
						((PostFixExprContext)_localctx).au = arrayUnboxing();
						}
						break;
					}
					} 
				}
				setState(650);
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
			setState(651);
			match(T__51);
			setState(652);
			match(T__51);
			setState(653);
			expr();
			setState(654);
			match(T__52);
			setState(655);
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
			setState(657);
			match(T__51);
			setState(658);
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
			setState(660);
			match(T__51);
			setState(661);
			expr();
			setState(662);
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
			setState(664);
			match(T__53);
			setState(671);
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
				setState(665);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(666);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(667);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__26:
				{
				setState(668);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__30:
				{
				setState(669);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__54:
				{
				setState(670);
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
			setState(684);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NullLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(673);
				match(NullLiteral);
				}
				break;
			case Literal:
				enterOuterAlt(_localctx, 2);
				{
				setState(674);
				match(Literal);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(675);
				stringLiteral();
				}
				break;
			case T__30:
				enterOuterAlt(_localctx, 4);
				{
				setState(676);
				varRef();
				}
				break;
			case T__26:
				enterOuterAlt(_localctx, 5);
				{
				setState(677);
				parenthesizedExpr();
				}
				break;
			case T__54:
				enterOuterAlt(_localctx, 6);
				{
				setState(678);
				contextItemExpr();
				}
				break;
			case T__28:
			case T__56:
				enterOuterAlt(_localctx, 7);
				{
				setState(679);
				objectConstructor();
				}
				break;
			case T__9:
			case T__61:
			case T__62:
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
				enterOuterAlt(_localctx, 8);
				{
				setState(680);
				functionCall();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 9);
				{
				setState(681);
				orderedExpr();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 10);
				{
				setState(682);
				unorderedExpr();
				}
				break;
			case T__51:
				enterOuterAlt(_localctx, 11);
				{
				setState(683);
				arrayConstructor();
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
			setState(686);
			match(T__30);
			setState(689);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(687);
				((VarRefContext)_localctx).ns = match(NCName);
				setState(688);
				match(T__9);
				}
				break;
			}
			setState(691);
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
			setState(693);
			match(T__26);
			setState(695);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__56) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (NullLiteral - 69)) | (1L << (Literal - 69)) | (1L << (Kfor - 69)) | (1L << (Klet - 69)) | (1L << (Kwhere - 69)) | (1L << (Kgroup - 69)) | (1L << (Kby - 69)) | (1L << (Korder - 69)) | (1L << (Kreturn - 69)) | (1L << (Kif - 69)) | (1L << (Kin - 69)) | (1L << (Kas - 69)) | (1L << (Kat - 69)) | (1L << (Kallowing - 69)) | (1L << (Kempty - 69)) | (1L << (Kcount - 69)) | (1L << (Kstable - 69)) | (1L << (Kascending - 69)) | (1L << (Kdescending - 69)) | (1L << (Ksome - 69)) | (1L << (Kevery - 69)) | (1L << (Ksatisfies - 69)) | (1L << (Kcollation - 69)) | (1L << (Kgreatest - 69)) | (1L << (Kleast - 69)) | (1L << (Kswitch - 69)) | (1L << (Kcase - 69)) | (1L << (Ktry - 69)) | (1L << (Kcatch - 69)) | (1L << (Kdefault - 69)) | (1L << (Kthen - 69)) | (1L << (Kelse - 69)) | (1L << (Ktypeswitch - 69)) | (1L << (Kor - 69)) | (1L << (Kand - 69)) | (1L << (Knot - 69)) | (1L << (Kto - 69)) | (1L << (Kinstance - 69)) | (1L << (Kof - 69)) | (1L << (Ktreat - 69)) | (1L << (Kcast - 69)) | (1L << (Kcastable - 69)) | (1L << (Kversion - 69)) | (1L << (Kjsoniq - 69)) | (1L << (Kjson - 69)) | (1L << (STRING - 69)) | (1L << (NCName - 69)))) != 0)) {
				{
				setState(694);
				expr();
				}
			}

			setState(697);
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
			setState(699);
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
			setState(701);
			match(T__6);
			setState(702);
			match(T__28);
			setState(703);
			expr();
			setState(704);
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
			setState(706);
			match(T__7);
			setState(707);
			match(T__28);
			setState(708);
			expr();
			setState(709);
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
		public NCNameOrKeyWordContext fcnName;
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
			setState(717);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(714);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(711);
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
					setState(712);
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
				setState(716);
				match(T__9);
				}
				break;
			}
			setState(721);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__61:
			case T__62:
			case NCName:
				{
				setState(719);
				((FunctionCallContext)_localctx).fcnName = nCNameOrKeyWord();
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
				setState(720);
				((FunctionCallContext)_localctx).kw = keyWords();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(723);
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
			setState(725);
			match(T__26);
			setState(732);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__55) | (1L << T__56) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (NullLiteral - 69)) | (1L << (Literal - 69)) | (1L << (Kfor - 69)) | (1L << (Klet - 69)) | (1L << (Kwhere - 69)) | (1L << (Kgroup - 69)) | (1L << (Kby - 69)) | (1L << (Korder - 69)) | (1L << (Kreturn - 69)) | (1L << (Kif - 69)) | (1L << (Kin - 69)) | (1L << (Kas - 69)) | (1L << (Kat - 69)) | (1L << (Kallowing - 69)) | (1L << (Kempty - 69)) | (1L << (Kcount - 69)) | (1L << (Kstable - 69)) | (1L << (Kascending - 69)) | (1L << (Kdescending - 69)) | (1L << (Ksome - 69)) | (1L << (Kevery - 69)) | (1L << (Ksatisfies - 69)) | (1L << (Kcollation - 69)) | (1L << (Kgreatest - 69)) | (1L << (Kleast - 69)) | (1L << (Kswitch - 69)) | (1L << (Kcase - 69)) | (1L << (Ktry - 69)) | (1L << (Kcatch - 69)) | (1L << (Kdefault - 69)) | (1L << (Kthen - 69)) | (1L << (Kelse - 69)) | (1L << (Ktypeswitch - 69)) | (1L << (Kor - 69)) | (1L << (Kand - 69)) | (1L << (Knot - 69)) | (1L << (Kto - 69)) | (1L << (Kinstance - 69)) | (1L << (Kof - 69)) | (1L << (Ktreat - 69)) | (1L << (Kcast - 69)) | (1L << (Kcastable - 69)) | (1L << (Kversion - 69)) | (1L << (Kjsoniq - 69)) | (1L << (Kjson - 69)) | (1L << (STRING - 69)) | (1L << (NCName - 69)))) != 0)) {
				{
				{
				setState(726);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(728);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__21) {
					{
					setState(727);
					match(T__21);
					}
				}

				}
				}
				setState(734);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(735);
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
			setState(739);
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
			case NullLiteral:
			case Literal:
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
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(737);
				exprSingle();
				}
				break;
			case T__55:
				enterOuterAlt(_localctx, 2);
				{
				setState(738);
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
			setState(749);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
				enterOuterAlt(_localctx, 1);
				{
				setState(741);
				match(T__26);
				setState(742);
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
			case NullLiteral:
			case Kjson:
				enterOuterAlt(_localctx, 2);
				{
				setState(743);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(747);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
				case 1:
					{
					setState(744);
					((SequenceTypeContext)_localctx).s56 = match(T__55);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s56);
					}
					break;
				case 2:
					{
					setState(745);
					((SequenceTypeContext)_localctx).s33 = match(T__32);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s33);
					}
					break;
				case 3:
					{
					setState(746);
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
		public List<Token> mergeOperator = new ArrayList<Token>();
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
			setState(767);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(751);
				match(T__28);
				setState(760);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__56) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (NullLiteral - 69)) | (1L << (Literal - 69)) | (1L << (Kfor - 69)) | (1L << (Klet - 69)) | (1L << (Kwhere - 69)) | (1L << (Kgroup - 69)) | (1L << (Kby - 69)) | (1L << (Korder - 69)) | (1L << (Kreturn - 69)) | (1L << (Kif - 69)) | (1L << (Kin - 69)) | (1L << (Kas - 69)) | (1L << (Kat - 69)) | (1L << (Kallowing - 69)) | (1L << (Kempty - 69)) | (1L << (Kcount - 69)) | (1L << (Kstable - 69)) | (1L << (Kascending - 69)) | (1L << (Kdescending - 69)) | (1L << (Ksome - 69)) | (1L << (Kevery - 69)) | (1L << (Ksatisfies - 69)) | (1L << (Kcollation - 69)) | (1L << (Kgreatest - 69)) | (1L << (Kleast - 69)) | (1L << (Kswitch - 69)) | (1L << (Kcase - 69)) | (1L << (Ktry - 69)) | (1L << (Kcatch - 69)) | (1L << (Kdefault - 69)) | (1L << (Kthen - 69)) | (1L << (Kelse - 69)) | (1L << (Ktypeswitch - 69)) | (1L << (Kor - 69)) | (1L << (Kand - 69)) | (1L << (Knot - 69)) | (1L << (Kto - 69)) | (1L << (Kinstance - 69)) | (1L << (Kof - 69)) | (1L << (Ktreat - 69)) | (1L << (Kcast - 69)) | (1L << (Kcastable - 69)) | (1L << (Kversion - 69)) | (1L << (Kjsoniq - 69)) | (1L << (Kjson - 69)) | (1L << (STRING - 69)) | (1L << (NCName - 69)))) != 0)) {
					{
					setState(752);
					pairConstructor();
					setState(757);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__21) {
						{
						{
						setState(753);
						match(T__21);
						setState(754);
						pairConstructor();
						}
						}
						setState(759);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(762);
				match(T__29);
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 2);
				{
				setState(763);
				((ObjectConstructorContext)_localctx).s57 = match(T__56);
				((ObjectConstructorContext)_localctx).mergeOperator.add(((ObjectConstructorContext)_localctx).s57);
				setState(764);
				expr();
				setState(765);
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
			setState(772);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__58:
				enterOuterAlt(_localctx, 1);
				{
				setState(769);
				match(T__58);
				}
				break;
			case T__59:
			case T__60:
			case Kjson:
				enterOuterAlt(_localctx, 2);
				{
				setState(770);
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
			case NullLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(771);
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
			setState(774);
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
			setState(776);
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
		enterRule(_localctx, 134, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(778);
			((SingleTypeContext)_localctx).item = atomicType();
			setState(780);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(779);
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
		enterRule(_localctx, 136, RULE_keyWordDuration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(782);
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

	public static class AtomicTypeContext extends ParserRuleContext {
		public KeyWordBooleanContext keyWordBoolean() {
			return getRuleContext(KeyWordBooleanContext.class,0);
		}
		public KeyWordDurationContext keyWordDuration() {
			return getRuleContext(KeyWordDurationContext.class,0);
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
		enterRule(_localctx, 138, RULE_atomicType);
		try {
			setState(792);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__63:
				enterOuterAlt(_localctx, 1);
				{
				setState(784);
				match(T__63);
				}
				break;
			case T__64:
				enterOuterAlt(_localctx, 2);
				{
				setState(785);
				match(T__64);
				}
				break;
			case T__65:
				enterOuterAlt(_localctx, 3);
				{
				setState(786);
				match(T__65);
				}
				break;
			case T__66:
				enterOuterAlt(_localctx, 4);
				{
				setState(787);
				match(T__66);
				}
				break;
			case T__67:
				enterOuterAlt(_localctx, 5);
				{
				setState(788);
				match(T__67);
				}
				break;
			case T__61:
				enterOuterAlt(_localctx, 6);
				{
				setState(789);
				keyWordBoolean();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 7);
				{
				setState(790);
				keyWordDuration();
				}
				break;
			case NullLiteral:
				enterOuterAlt(_localctx, 8);
				{
				setState(791);
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
		enterRule(_localctx, 140, RULE_nCNameOrKeyWord);
		try {
			setState(797);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(794);
				match(NCName);
				}
				break;
			case T__61:
				enterOuterAlt(_localctx, 2);
				{
				setState(795);
				keyWordBoolean();
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 3);
				{
				setState(796);
				keyWordDuration();
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
		enterRule(_localctx, 142, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(801);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(799);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(800);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(803);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==T__55) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(804);
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
		enterRule(_localctx, 144, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
			match(T__51);
			setState(808);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__9) | (1L << T__26) | (1L << T__28) | (1L << T__30) | (1L << T__45) | (1L << T__46) | (1L << T__51) | (1L << T__54) | (1L << T__56) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (NullLiteral - 69)) | (1L << (Literal - 69)) | (1L << (Kfor - 69)) | (1L << (Klet - 69)) | (1L << (Kwhere - 69)) | (1L << (Kgroup - 69)) | (1L << (Kby - 69)) | (1L << (Korder - 69)) | (1L << (Kreturn - 69)) | (1L << (Kif - 69)) | (1L << (Kin - 69)) | (1L << (Kas - 69)) | (1L << (Kat - 69)) | (1L << (Kallowing - 69)) | (1L << (Kempty - 69)) | (1L << (Kcount - 69)) | (1L << (Kstable - 69)) | (1L << (Kascending - 69)) | (1L << (Kdescending - 69)) | (1L << (Ksome - 69)) | (1L << (Kevery - 69)) | (1L << (Ksatisfies - 69)) | (1L << (Kcollation - 69)) | (1L << (Kgreatest - 69)) | (1L << (Kleast - 69)) | (1L << (Kswitch - 69)) | (1L << (Kcase - 69)) | (1L << (Ktry - 69)) | (1L << (Kcatch - 69)) | (1L << (Kdefault - 69)) | (1L << (Kthen - 69)) | (1L << (Kelse - 69)) | (1L << (Ktypeswitch - 69)) | (1L << (Kor - 69)) | (1L << (Kand - 69)) | (1L << (Knot - 69)) | (1L << (Kto - 69)) | (1L << (Kinstance - 69)) | (1L << (Kof - 69)) | (1L << (Ktreat - 69)) | (1L << (Kcast - 69)) | (1L << (Kcastable - 69)) | (1L << (Kversion - 69)) | (1L << (Kjsoniq - 69)) | (1L << (Kjson - 69)) | (1L << (STRING - 69)) | (1L << (NCName - 69)))) != 0)) {
				{
				setState(807);
				expr();
				}
			}

			setState(810);
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
		enterRule(_localctx, 146, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(812);
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
		enterRule(_localctx, 148, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(814);
			_la = _input.LA(1);
			if ( !(((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & ((1L << (Kfor - 73)) | (1L << (Klet - 73)) | (1L << (Kwhere - 73)) | (1L << (Kgroup - 73)) | (1L << (Kby - 73)) | (1L << (Korder - 73)) | (1L << (Kreturn - 73)) | (1L << (Kif - 73)) | (1L << (Kin - 73)) | (1L << (Kas - 73)) | (1L << (Kat - 73)) | (1L << (Kallowing - 73)) | (1L << (Kempty - 73)) | (1L << (Kcount - 73)) | (1L << (Kstable - 73)) | (1L << (Kascending - 73)) | (1L << (Kdescending - 73)) | (1L << (Ksome - 73)) | (1L << (Kevery - 73)) | (1L << (Ksatisfies - 73)) | (1L << (Kcollation - 73)) | (1L << (Kgreatest - 73)) | (1L << (Kleast - 73)) | (1L << (Kswitch - 73)) | (1L << (Kcase - 73)) | (1L << (Ktry - 73)) | (1L << (Kcatch - 73)) | (1L << (Kdefault - 73)) | (1L << (Kthen - 73)) | (1L << (Kelse - 73)) | (1L << (Ktypeswitch - 73)) | (1L << (Kor - 73)) | (1L << (Kand - 73)) | (1L << (Knot - 73)) | (1L << (Kto - 73)) | (1L << (Kinstance - 73)) | (1L << (Kof - 73)) | (1L << (Ktreat - 73)) | (1L << (Kcast - 73)) | (1L << (Kcastable - 73)) | (1L << (Kversion - 73)) | (1L << (Kjsoniq - 73)) | (1L << (Kjson - 73)))) != 0)) ) {
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
		enterRule(_localctx, 150, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(816);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3}\u0335\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\3\2\3\2\3\2\3\2\3\2\5\2\u00a0\n\2\3\2\3\2"+
		"\5\2\u00a4\n\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\5\5\5\u00b6\n\5\3\5\3\5\7\5\u00ba\n\5\f\5\16\5\u00bd\13\5\3\5\3"+
		"\5\5\5\u00c1\n\5\3\5\3\5\7\5\u00c5\n\5\f\5\16\5\u00c8\13\5\3\6\3\6\3\6"+
		"\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\5\t\u00dd"+
		"\n\t\3\t\3\t\3\t\5\t\u00e2\n\t\3\t\3\t\3\t\3\t\7\t\u00e8\n\t\f\t\16\t"+
		"\u00eb\13\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\5\13\u00f4\n\13\3\13\3\13"+
		"\3\13\3\13\3\13\7\13\u00fb\n\13\f\13\16\13\u00fe\13\13\5\13\u0100\n\13"+
		"\3\f\3\f\3\f\3\f\3\f\5\f\u0107\n\f\3\f\3\f\3\f\3\f\3\f\5\f\u010e\n\f\5"+
		"\f\u0110\n\f\3\r\3\r\3\r\3\r\5\r\u0116\n\r\3\r\3\r\3\r\5\r\u011b\n\r\3"+
		"\r\3\r\3\r\5\r\u0120\n\r\3\r\3\r\3\r\3\r\3\r\5\r\u0127\n\r\3\16\3\16\3"+
		"\16\3\16\5\16\u012d\n\16\3\16\3\16\3\16\3\16\3\16\5\16\u0134\n\16\7\16"+
		"\u0136\n\16\f\16\16\16\u0139\13\16\3\17\3\17\3\17\7\17\u013e\n\17\f\17"+
		"\16\17\u0141\13\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u014a\n\20"+
		"\3\21\3\21\5\21\u014e\n\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u0156\n"+
		"\21\f\21\16\21\u0159\13\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\7\22\u0162"+
		"\n\22\f\22\16\22\u0165\13\22\3\23\3\23\3\23\5\23\u016a\n\23\3\23\3\23"+
		"\5\23\u016e\n\23\3\23\3\23\5\23\u0172\n\23\3\23\3\23\3\23\3\24\3\24\3"+
		"\24\3\24\7\24\u017b\n\24\f\24\16\24\u017e\13\24\3\25\3\25\3\25\5\25\u0183"+
		"\n\25\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\7\27\u0190"+
		"\n\27\f\27\16\27\u0193\13\27\3\30\3\30\3\30\5\30\u0198\n\30\3\30\3\30"+
		"\5\30\u019c\n\30\3\30\3\30\5\30\u01a0\n\30\3\31\3\31\3\31\3\31\3\31\5"+
		"\31\u01a7\n\31\3\31\3\31\3\31\7\31\u01ac\n\31\f\31\16\31\u01af\13\31\3"+
		"\32\3\32\3\32\5\32\u01b4\n\32\3\32\3\32\3\32\5\32\u01b9\n\32\5\32\u01bb"+
		"\n\32\3\32\3\32\5\32\u01bf\n\32\3\33\3\33\3\33\3\34\3\34\5\34\u01c6\n"+
		"\34\3\34\3\34\3\34\7\34\u01cb\n\34\f\34\16\34\u01ce\13\34\3\34\3\34\3"+
		"\34\3\35\3\35\3\35\5\35\u01d6\n\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36"+
		"\3\36\6\36\u01e0\n\36\r\36\16\36\u01e1\3\36\3\36\3\36\3\36\3\37\3\37\6"+
		"\37\u01ea\n\37\r\37\16\37\u01eb\3\37\3\37\3\37\3 \3 \3 \3 \3 \6 \u01f6"+
		"\n \r \16 \u01f7\3 \3 \5 \u01fc\n \3 \3 \3 \3!\3!\3!\3!\5!\u0205\n!\3"+
		"!\3!\3!\7!\u020a\n!\f!\16!\u020d\13!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\""+
		"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\7$\u0228\n$\f$\16"+
		"$\u022b\13$\3%\3%\3%\7%\u0230\n%\f%\16%\u0233\13%\3&\5&\u0236\n&\3&\3"+
		"&\3\'\3\'\3\'\5\'\u023d\n\'\3(\3(\3(\7(\u0242\n(\f(\16(\u0245\13(\3)\3"+
		")\3)\5)\u024a\n)\3*\3*\3*\7*\u024f\n*\f*\16*\u0252\13*\3+\3+\3+\7+\u0257"+
		"\n+\f+\16+\u025a\13+\3,\3,\3,\3,\5,\u0260\n,\3-\3-\3-\3-\5-\u0266\n-\3"+
		".\3.\3.\3.\5.\u026c\n.\3/\3/\3/\3/\5/\u0272\n/\3\60\7\60\u0275\n\60\f"+
		"\60\16\60\u0278\13\60\3\60\3\60\3\61\3\61\3\61\7\61\u027f\n\61\f\61\16"+
		"\61\u0282\13\61\3\62\3\62\3\62\3\62\3\62\7\62\u0289\n\62\f\62\16\62\u028c"+
		"\13\62\3\63\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\65\3\65\3\65\3\65"+
		"\3\66\3\66\3\66\3\66\3\66\3\66\3\66\5\66\u02a2\n\66\3\67\3\67\3\67\3\67"+
		"\3\67\3\67\3\67\3\67\3\67\3\67\3\67\5\67\u02af\n\67\38\38\38\58\u02b4"+
		"\n8\38\38\39\39\59\u02ba\n9\39\39\3:\3:\3;\3;\3;\3;\3;\3<\3<\3<\3<\3<"+
		"\3=\3=\3=\5=\u02cd\n=\3=\5=\u02d0\n=\3=\3=\5=\u02d4\n=\3=\3=\3>\3>\3>"+
		"\5>\u02db\n>\7>\u02dd\n>\f>\16>\u02e0\13>\3>\3>\3?\3?\5?\u02e6\n?\3@\3"+
		"@\3@\3@\3@\3@\5@\u02ee\n@\5@\u02f0\n@\3A\3A\3A\3A\7A\u02f6\nA\fA\16A\u02f9"+
		"\13A\5A\u02fb\nA\3A\3A\3A\3A\3A\5A\u0302\nA\3B\3B\3B\5B\u0307\nB\3C\3"+
		"C\3D\3D\3E\3E\5E\u030f\nE\3F\3F\3G\3G\3G\3G\3G\3G\3G\3G\5G\u031b\nG\3"+
		"H\3H\3H\5H\u0320\nH\3I\3I\5I\u0324\nI\3I\3I\3I\3J\3J\5J\u032b\nJ\3J\3"+
		"J\3K\3K\3L\3L\3M\3M\3M\2\2N\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \""+
		"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084"+
		"\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\2\13\3\2"+
		"\t\n\3\2`a\3\2\r\26\4\2\6\6$.\3\2\60\61\4\2##\62\64\4\2>?uu\4\2\f\f::"+
		"\3\2Ku\2\u0368\2\u009f\3\2\2\2\4\u00a5\3\2\2\2\6\u00a8\3\2\2\2\b\u00bb"+
		"\3\2\2\2\n\u00c9\3\2\2\2\f\u00ce\3\2\2\2\16\u00d2\3\2\2\2\20\u00d8\3\2"+
		"\2\2\22\u00ec\3\2\2\2\24\u00ee\3\2\2\2\26\u0101\3\2\2\2\30\u0111\3\2\2"+
		"\2\32\u0128\3\2\2\2\34\u013a\3\2\2\2\36\u0149\3\2\2\2 \u014d\3\2\2\2\""+
		"\u015d\3\2\2\2$\u0166\3\2\2\2&\u0176\3\2\2\2(\u017f\3\2\2\2*\u0187\3\2"+
		"\2\2,\u018a\3\2\2\2.\u0194\3\2\2\2\60\u01a6\3\2\2\2\62\u01b0\3\2\2\2\64"+
		"\u01c0\3\2\2\2\66\u01c5\3\2\2\28\u01d2\3\2\2\2:\u01da\3\2\2\2<\u01e9\3"+
		"\2\2\2>\u01f0\3\2\2\2@\u0200\3\2\2\2B\u0211\3\2\2\2D\u021a\3\2\2\2F\u0224"+
		"\3\2\2\2H\u022c\3\2\2\2J\u0235\3\2\2\2L\u0239\3\2\2\2N\u023e\3\2\2\2P"+
		"\u0246\3\2\2\2R\u024b\3\2\2\2T\u0253\3\2\2\2V\u025b\3\2\2\2X\u0261\3\2"+
		"\2\2Z\u0267\3\2\2\2\\\u026d\3\2\2\2^\u0276\3\2\2\2`\u027b\3\2\2\2b\u0283"+
		"\3\2\2\2d\u028d\3\2\2\2f\u0293\3\2\2\2h\u0296\3\2\2\2j\u029a\3\2\2\2l"+
		"\u02ae\3\2\2\2n\u02b0\3\2\2\2p\u02b7\3\2\2\2r\u02bd\3\2\2\2t\u02bf\3\2"+
		"\2\2v\u02c4\3\2\2\2x\u02cf\3\2\2\2z\u02d7\3\2\2\2|\u02e5\3\2\2\2~\u02ef"+
		"\3\2\2\2\u0080\u0301\3\2\2\2\u0082\u0306\3\2\2\2\u0084\u0308\3\2\2\2\u0086"+
		"\u030a\3\2\2\2\u0088\u030c\3\2\2\2\u008a\u0310\3\2\2\2\u008c\u031a\3\2"+
		"\2\2\u008e\u031f\3\2\2\2\u0090\u0323\3\2\2\2\u0092\u0328\3\2\2\2\u0094"+
		"\u032e\3\2\2\2\u0096\u0330\3\2\2\2\u0098\u0332\3\2\2\2\u009a\u009b\7t"+
		"\2\2\u009b\u009c\7s\2\2\u009c\u009d\5\u0098M\2\u009d\u009e\7\3\2\2\u009e"+
		"\u00a0\3\2\2\2\u009f\u009a\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a3\3\2"+
		"\2\2\u00a1\u00a4\5\6\4\2\u00a2\u00a4\5\4\3\2\u00a3\u00a1\3\2\2\2\u00a3"+
		"\u00a2\3\2\2\2\u00a4\3\3\2\2\2\u00a5\u00a6\5\b\5\2\u00a6\u00a7\5\34\17"+
		"\2\u00a7\5\3\2\2\2\u00a8\u00a9\7\4\2\2\u00a9\u00aa\7\5\2\2\u00aa\u00ab"+
		"\7{\2\2\u00ab\u00ac\7\6\2\2\u00ac\u00ad\5\u0094K\2\u00ad\u00ae\7\3\2\2"+
		"\u00ae\u00af\5\b\5\2\u00af\7\3\2\2\2\u00b0\u00b6\5\n\6\2\u00b1\u00b6\5"+
		"\f\7\2\u00b2\u00b6\5\16\b\2\u00b3\u00b6\5\20\t\2\u00b4\u00b6\5\24\13\2"+
		"\u00b5\u00b0\3\2\2\2\u00b5\u00b1\3\2\2\2\u00b5\u00b2\3\2\2\2\u00b5\u00b3"+
		"\3\2\2\2\u00b5\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\7\3\2\2\u00b8"+
		"\u00ba\3\2\2\2\u00b9\u00b5\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb\u00b9\3\2"+
		"\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00c6\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be"+
		"\u00c1\5\30\r\2\u00bf\u00c1\5\26\f\2\u00c0\u00be\3\2\2\2\u00c0\u00bf\3"+
		"\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c3\7\3\2\2\u00c3\u00c5\3\2\2\2\u00c4"+
		"\u00c0\3\2\2\2\u00c5\u00c8\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7\3\2"+
		"\2\2\u00c7\t\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c9\u00ca\7\7\2\2\u00ca\u00cb"+
		"\7f\2\2\u00cb\u00cc\7_\2\2\u00cc\u00cd\5\u0094K\2\u00cd\13\3\2\2\2\u00ce"+
		"\u00cf\7\7\2\2\u00cf\u00d0\7\b\2\2\u00d0\u00d1\t\2\2\2\u00d1\r\3\2\2\2"+
		"\u00d2\u00d3\7\7\2\2\u00d3\u00d4\7f\2\2\u00d4\u00d5\7P\2\2\u00d5\u00d6"+
		"\7W\2\2\u00d6\u00d7\t\3\2\2\u00d7\17\3\2\2\2\u00d8\u00e1\7\7\2\2\u00d9"+
		"\u00dc\7\13\2\2\u00da\u00db\7{\2\2\u00db\u00dd\7\f\2\2\u00dc\u00da\3\2"+
		"\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00e2\7{\2\2\u00df"+
		"\u00e0\7f\2\2\u00e0\u00e2\7\13\2\2\u00e1\u00d9\3\2\2\2\u00e1\u00df\3\2"+
		"\2\2\u00e2\u00e9\3\2\2\2\u00e3\u00e4\5\22\n\2\u00e4\u00e5\7\6\2\2\u00e5"+
		"\u00e6\5\u0098M\2\u00e6\u00e8\3\2\2\2\u00e7\u00e3\3\2\2\2\u00e8\u00eb"+
		"\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\21\3\2\2\2\u00eb"+
		"\u00e9\3\2\2\2\u00ec\u00ed\t\4\2\2\u00ed\23\3\2\2\2\u00ee\u00ef\7\27\2"+
		"\2\u00ef\u00f3\7\4\2\2\u00f0\u00f1\7\5\2\2\u00f1\u00f2\7{\2\2\u00f2\u00f4"+
		"\7\6\2\2\u00f3\u00f0\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5"+
		"\u00ff\5\u0094K\2\u00f6\u00f7\7U\2\2\u00f7\u00fc\5\u0094K\2\u00f8\u00f9"+
		"\7\30\2\2\u00f9\u00fb\5\u0094K\2\u00fa\u00f8\3\2\2\2\u00fb\u00fe\3\2\2"+
		"\2\u00fc\u00fa\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u0100\3\2\2\2\u00fe\u00fc"+
		"\3\2\2\2\u00ff\u00f6\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\25\3\2\2\2\u0101"+
		"\u0102\7\7\2\2\u0102\u0103\7\31\2\2\u0103\u0106\5n8\2\u0104\u0105\7T\2"+
		"\2\u0105\u0107\5~@\2\u0106\u0104\3\2\2\2\u0106\u0107\3\2\2\2\u0107\u010f"+
		"\3\2\2\2\u0108\u0109\7\32\2\2\u0109\u0110\5\36\20\2\u010a\u010d\7\33\2"+
		"\2\u010b\u010c\7\32\2\2\u010c\u010e\5\36\20\2\u010d\u010b\3\2\2\2\u010d"+
		"\u010e\3\2\2\2\u010e\u0110\3\2\2\2\u010f\u0108\3\2\2\2\u010f\u010a\3\2"+
		"\2\2\u0110\27\3\2\2\2\u0111\u0112\7\7\2\2\u0112\u0115\7\34\2\2\u0113\u0114"+
		"\7{\2\2\u0114\u0116\7\f\2\2\u0115\u0113\3\2\2\2\u0115\u0116\3\2\2\2\u0116"+
		"\u0117\3\2\2\2\u0117\u0118\7{\2\2\u0118\u011a\7\35\2\2\u0119\u011b\5\32"+
		"\16\2\u011a\u0119\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011c\3\2\2\2\u011c"+
		"\u011f\7\36\2\2\u011d\u011e\7T\2\2\u011e\u0120\5~@\2\u011f\u011d\3\2\2"+
		"\2\u011f\u0120\3\2\2\2\u0120\u0126\3\2\2\2\u0121\u0122\7\37\2\2\u0122"+
		"\u0123\5\34\17\2\u0123\u0124\7 \2\2\u0124\u0127\3\2\2\2\u0125\u0127\7"+
		"\33\2\2\u0126\u0121\3\2\2\2\u0126\u0125\3\2\2\2\u0127\31\3\2\2\2\u0128"+
		"\u0129\7!\2\2\u0129\u012c\7{\2\2\u012a\u012b\7T\2\2\u012b\u012d\5~@\2"+
		"\u012c\u012a\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u0137\3\2\2\2\u012e\u012f"+
		"\7\30\2\2\u012f\u0130\7!\2\2\u0130\u0133\7{\2\2\u0131\u0132\7T\2\2\u0132"+
		"\u0134\5~@\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134\u0136\3\2\2"+
		"\2\u0135\u012e\3\2\2\2\u0136\u0139\3\2\2\2\u0137\u0135\3\2\2\2\u0137\u0138"+
		"\3\2\2\2\u0138\33\3\2\2\2\u0139\u0137\3\2\2\2\u013a\u013f\5\36\20\2\u013b"+
		"\u013c\7\30\2\2\u013c\u013e\5\36\20\2\u013d\u013b\3\2\2\2\u013e\u0141"+
		"\3\2\2\2\u013f\u013d\3\2\2\2\u013f\u0140\3\2\2\2\u0140\35\3\2\2\2\u0141"+
		"\u013f\3\2\2\2\u0142\u014a\5 \21\2\u0143\u014a\5\66\34\2\u0144\u014a\5"+
		":\36\2\u0145\u014a\5> \2\u0146\u014a\5B\"\2\u0147\u014a\5D#\2\u0148\u014a"+
		"\5F$\2\u0149\u0142\3\2\2\2\u0149\u0143\3\2\2\2\u0149\u0144\3\2\2\2\u0149"+
		"\u0145\3\2\2\2\u0149\u0146\3\2\2\2\u0149\u0147\3\2\2\2\u0149\u0148\3\2"+
		"\2\2\u014a\37\3\2\2\2\u014b\u014e\5\"\22\2\u014c\u014e\5&\24\2\u014d\u014b"+
		"\3\2\2\2\u014d\u014c\3\2\2\2\u014e\u0157\3\2\2\2\u014f\u0156\5\"\22\2"+
		"\u0150\u0156\5*\26\2\u0151\u0156\5&\24\2\u0152\u0156\5,\27\2\u0153\u0156"+
		"\5\60\31\2\u0154\u0156\5\64\33\2\u0155\u014f\3\2\2\2\u0155\u0150\3\2\2"+
		"\2\u0155\u0151\3\2\2\2\u0155\u0152\3\2\2\2\u0155\u0153\3\2\2\2\u0155\u0154"+
		"\3\2\2\2\u0156\u0159\3\2\2\2\u0157\u0155\3\2\2\2\u0157\u0158\3\2\2\2\u0158"+
		"\u015a\3\2\2\2\u0159\u0157\3\2\2\2\u015a\u015b\7Q\2\2\u015b\u015c\5\36"+
		"\20\2\u015c!\3\2\2\2\u015d\u015e\7K\2\2\u015e\u0163\5$\23\2\u015f\u0160"+
		"\7\30\2\2\u0160\u0162\5$\23\2\u0161\u015f\3\2\2\2\u0162\u0165\3\2\2\2"+
		"\u0163\u0161\3\2\2\2\u0163\u0164\3\2\2\2\u0164#\3\2\2\2\u0165\u0163\3"+
		"\2\2\2\u0166\u0169\5n8\2\u0167\u0168\7T\2\2\u0168\u016a\5~@\2\u0169\u0167"+
		"\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016d\3\2\2\2\u016b\u016c\7V\2\2\u016c"+
		"\u016e\7W\2\2\u016d\u016b\3\2\2\2\u016d\u016e\3\2\2\2\u016e\u0171\3\2"+
		"\2\2\u016f\u0170\7U\2\2\u0170\u0172\5n8\2\u0171\u016f\3\2\2\2\u0171\u0172"+
		"\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0174\7S\2\2\u0174\u0175\5\36\20\2"+
		"\u0175%\3\2\2\2\u0176\u0177\7L\2\2\u0177\u017c\5(\25\2\u0178\u0179\7\30"+
		"\2\2\u0179\u017b\5(\25\2\u017a\u0178\3\2\2\2\u017b\u017e\3\2\2\2\u017c"+
		"\u017a\3\2\2\2\u017c\u017d\3\2\2\2\u017d\'\3\2\2\2\u017e\u017c\3\2\2\2"+
		"\u017f\u0182\5n8\2\u0180\u0181\7T\2\2\u0181\u0183\5~@\2\u0182\u0180\3"+
		"\2\2\2\u0182\u0183\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u0185\7\32\2\2\u0185"+
		"\u0186\5\36\20\2\u0186)\3\2\2\2\u0187\u0188\7M\2\2\u0188\u0189\5\36\20"+
		"\2\u0189+\3\2\2\2\u018a\u018b\7N\2\2\u018b\u018c\7O\2\2\u018c\u0191\5"+
		".\30\2\u018d\u018e\7\30\2\2\u018e\u0190\5.\30\2\u018f\u018d\3\2\2\2\u0190"+
		"\u0193\3\2\2\2\u0191\u018f\3\2\2\2\u0191\u0192\3\2\2\2\u0192-\3\2\2\2"+
		"\u0193\u0191\3\2\2\2\u0194\u019b\5n8\2\u0195\u0196\7T\2\2\u0196\u0198"+
		"\5~@\2\u0197\u0195\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0199\3\2\2\2\u0199"+
		"\u019a\7\32\2\2\u019a\u019c\5\36\20\2\u019b\u0197\3\2\2\2\u019b\u019c"+
		"\3\2\2\2\u019c\u019f\3\2\2\2\u019d\u019e\7_\2\2\u019e\u01a0\5\u0094K\2"+
		"\u019f\u019d\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0/\3\2\2\2\u01a1\u01a2\7"+
		"P\2\2\u01a2\u01a7\7O\2\2\u01a3\u01a4\7Y\2\2\u01a4\u01a5\7P\2\2\u01a5\u01a7"+
		"\7O\2\2\u01a6\u01a1\3\2\2\2\u01a6\u01a3\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8"+
		"\u01ad\5\62\32\2\u01a9\u01aa\7\30\2\2\u01aa\u01ac\5\62\32\2\u01ab\u01a9"+
		"\3\2\2\2\u01ac\u01af\3\2\2\2\u01ad\u01ab\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae"+
		"\61\3\2\2\2\u01af\u01ad\3\2\2\2\u01b0\u01b3\5\36\20\2\u01b1\u01b4\7Z\2"+
		"\2\u01b2\u01b4\7[\2\2\u01b3\u01b1\3\2\2\2\u01b3\u01b2\3\2\2\2\u01b3\u01b4"+
		"\3\2\2\2\u01b4\u01ba\3\2\2\2\u01b5\u01b8\7W\2\2\u01b6\u01b9\7`\2\2\u01b7"+
		"\u01b9\7a\2\2\u01b8\u01b6\3\2\2\2\u01b8\u01b7\3\2\2\2\u01b9\u01bb\3\2"+
		"\2\2\u01ba\u01b5\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01be\3\2\2\2\u01bc"+
		"\u01bd\7_\2\2\u01bd\u01bf\5\u0094K\2\u01be\u01bc\3\2\2\2\u01be\u01bf\3"+
		"\2\2\2\u01bf\63\3\2\2\2\u01c0\u01c1\7X\2\2\u01c1\u01c2\5n8\2\u01c2\65"+
		"\3\2\2\2\u01c3\u01c6\7\\\2\2\u01c4\u01c6\7]\2\2\u01c5\u01c3\3\2\2\2\u01c5"+
		"\u01c4\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01cc\58\35\2\u01c8\u01c9\7\30"+
		"\2\2\u01c9\u01cb\58\35\2\u01ca\u01c8\3\2\2\2\u01cb\u01ce\3\2\2\2\u01cc"+
		"\u01ca\3\2\2\2\u01cc\u01cd\3\2\2\2\u01cd\u01cf\3\2\2\2\u01ce\u01cc\3\2"+
		"\2\2\u01cf\u01d0\7^\2\2\u01d0\u01d1\5\36\20\2\u01d1\67\3\2\2\2\u01d2\u01d5"+
		"\5n8\2\u01d3\u01d4\7T\2\2\u01d4\u01d6\5~@\2\u01d5\u01d3\3\2\2\2\u01d5"+
		"\u01d6\3\2\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01d8\7S\2\2\u01d8\u01d9\5\36"+
		"\20\2\u01d99\3\2\2\2\u01da\u01db\7b\2\2\u01db\u01dc\7\35\2\2\u01dc\u01dd"+
		"\5\34\17\2\u01dd\u01df\7\36\2\2\u01de\u01e0\5<\37\2\u01df\u01de\3\2\2"+
		"\2\u01e0\u01e1\3\2\2\2\u01e1\u01df\3\2\2\2\u01e1\u01e2\3\2\2\2\u01e2\u01e3"+
		"\3\2\2\2\u01e3\u01e4\7f\2\2\u01e4\u01e5\7Q\2\2\u01e5\u01e6\5\36\20\2\u01e6"+
		";\3\2\2\2\u01e7\u01e8\7c\2\2\u01e8\u01ea\5\36\20\2\u01e9\u01e7\3\2\2\2"+
		"\u01ea\u01eb\3\2\2\2\u01eb\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ed"+
		"\3\2\2\2\u01ed\u01ee\7Q\2\2\u01ee\u01ef\5\36\20\2\u01ef=\3\2\2\2\u01f0"+
		"\u01f1\7i\2\2\u01f1\u01f2\7\35\2\2\u01f2\u01f3\5\34\17\2\u01f3\u01f5\7"+
		"\36\2\2\u01f4\u01f6\5@!\2\u01f5\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7"+
		"\u01f5\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u01f9\3\2\2\2\u01f9\u01fb\7f"+
		"\2\2\u01fa\u01fc\5n8\2\u01fb\u01fa\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01fd"+
		"\3\2\2\2\u01fd\u01fe\7Q\2\2\u01fe\u01ff\5\36\20\2\u01ff?\3\2\2\2\u0200"+
		"\u0204\7c\2\2\u0201\u0202\5n8\2\u0202\u0203\7T\2\2\u0203\u0205\3\2\2\2"+
		"\u0204\u0201\3\2\2\2\u0204\u0205\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u020b"+
		"\5~@\2\u0207\u0208\7\"\2\2\u0208\u020a\5~@\2\u0209\u0207\3\2\2\2\u020a"+
		"\u020d\3\2\2\2\u020b\u0209\3\2\2\2\u020b\u020c\3\2\2\2\u020c\u020e\3\2"+
		"\2\2\u020d\u020b\3\2\2\2\u020e\u020f\7Q\2\2\u020f\u0210\5\36\20\2\u0210"+
		"A\3\2\2\2\u0211\u0212\7R\2\2\u0212\u0213\7\35\2\2\u0213\u0214\5\34\17"+
		"\2\u0214\u0215\7\36\2\2\u0215\u0216\7g\2\2\u0216\u0217\5\36\20\2\u0217"+
		"\u0218\7h\2\2\u0218\u0219\5\36\20\2\u0219C\3\2\2\2\u021a\u021b\7d\2\2"+
		"\u021b\u021c\7\37\2\2\u021c\u021d\5\34\17\2\u021d\u021e\7 \2\2\u021e\u021f"+
		"\7e\2\2\u021f\u0220\7#\2\2\u0220\u0221\7\37\2\2\u0221\u0222\5\34\17\2"+
		"\u0222\u0223\7 \2\2\u0223E\3\2\2\2\u0224\u0229\5H%\2\u0225\u0226\7j\2"+
		"\2\u0226\u0228\5H%\2\u0227\u0225\3\2\2\2\u0228\u022b\3\2\2\2\u0229\u0227"+
		"\3\2\2\2\u0229\u022a\3\2\2\2\u022aG\3\2\2\2\u022b\u0229\3\2\2\2\u022c"+
		"\u0231\5J&\2\u022d\u022e\7k\2\2\u022e\u0230\5J&\2\u022f\u022d\3\2\2\2"+
		"\u0230\u0233\3\2\2\2\u0231\u022f\3\2\2\2\u0231\u0232\3\2\2\2\u0232I\3"+
		"\2\2\2\u0233\u0231\3\2\2\2\u0234\u0236\7l\2\2\u0235\u0234\3\2\2\2\u0235"+
		"\u0236\3\2\2\2\u0236\u0237\3\2\2\2\u0237\u0238\5L\'\2\u0238K\3\2\2\2\u0239"+
		"\u023c\5N(\2\u023a\u023b\t\5\2\2\u023b\u023d\5N(\2\u023c\u023a\3\2\2\2"+
		"\u023c\u023d\3\2\2\2\u023dM\3\2\2\2\u023e\u0243\5P)\2\u023f\u0240\7/\2"+
		"\2\u0240\u0242\5P)\2\u0241\u023f\3\2\2\2\u0242\u0245\3\2\2\2\u0243\u0241"+
		"\3\2\2\2\u0243\u0244\3\2\2\2\u0244O\3\2\2\2\u0245\u0243\3\2\2\2\u0246"+
		"\u0249\5R*\2\u0247\u0248\7m\2\2\u0248\u024a\5R*\2\u0249\u0247\3\2\2\2"+
		"\u0249\u024a\3\2\2\2\u024aQ\3\2\2\2\u024b\u0250\5T+\2\u024c\u024d\t\6"+
		"\2\2\u024d\u024f\5T+\2\u024e\u024c\3\2\2\2\u024f\u0252\3\2\2\2\u0250\u024e"+
		"\3\2\2\2\u0250\u0251\3\2\2\2\u0251S\3\2\2\2\u0252\u0250\3\2\2\2\u0253"+
		"\u0258\5V,\2\u0254\u0255\t\7\2\2\u0255\u0257\5V,\2\u0256\u0254\3\2\2\2"+
		"\u0257\u025a\3\2\2\2\u0258\u0256\3\2\2\2\u0258\u0259\3\2\2\2\u0259U\3"+
		"\2\2\2\u025a\u0258\3\2\2\2\u025b\u025f\5X-\2\u025c\u025d\7n\2\2\u025d"+
		"\u025e\7o\2\2\u025e\u0260\5~@\2\u025f\u025c\3\2\2\2\u025f\u0260\3\2\2"+
		"\2\u0260W\3\2\2\2\u0261\u0265\5Z.\2\u0262\u0263\7p\2\2\u0263\u0264\7T"+
		"\2\2\u0264\u0266\5~@\2\u0265\u0262\3\2\2\2\u0265\u0266\3\2\2\2\u0266Y"+
		"\3\2\2\2\u0267\u026b\5\\/\2\u0268\u0269\7r\2\2\u0269\u026a\7T\2\2\u026a"+
		"\u026c\5\u0088E\2\u026b\u0268\3\2\2\2\u026b\u026c\3\2\2\2\u026c[\3\2\2"+
		"\2\u026d\u0271\5^\60\2\u026e\u026f\7q\2\2\u026f\u0270\7T\2\2\u0270\u0272"+
		"\5\u0088E\2\u0271\u026e\3\2\2\2\u0271\u0272\3\2\2\2\u0272]\3\2\2\2\u0273"+
		"\u0275\t\6\2\2\u0274\u0273\3\2\2\2\u0275\u0278\3\2\2\2\u0276\u0274\3\2"+
		"\2\2\u0276\u0277\3\2\2\2\u0277\u0279\3\2\2\2\u0278\u0276\3\2\2\2\u0279"+
		"\u027a\5`\61\2\u027a_\3\2\2\2\u027b\u0280\5b\62\2\u027c\u027d\7\65\2\2"+
		"\u027d\u027f\5b\62\2\u027e\u027c\3\2\2\2\u027f\u0282\3\2\2\2\u0280\u027e"+
		"\3\2\2\2\u0280\u0281\3\2\2\2\u0281a\3\2\2\2\u0282\u0280\3\2\2\2\u0283"+
		"\u028a\5l\67\2\u0284\u0289\5d\63\2\u0285\u0289\5h\65\2\u0286\u0289\5j"+
		"\66\2\u0287\u0289\5f\64\2\u0288\u0284\3\2\2\2\u0288\u0285\3\2\2\2\u0288"+
		"\u0286\3\2\2\2\u0288\u0287\3\2\2\2\u0289\u028c\3\2\2\2\u028a\u0288\3\2"+
		"\2\2\u028a\u028b\3\2\2\2\u028bc\3\2\2\2\u028c\u028a\3\2\2\2\u028d\u028e"+
		"\7\66\2\2\u028e\u028f\7\66\2\2\u028f\u0290\5\34\17\2\u0290\u0291\7\67"+
		"\2\2\u0291\u0292\7\67\2\2\u0292e\3\2\2\2\u0293\u0294\7\66\2\2\u0294\u0295"+
		"\7\67\2\2\u0295g\3\2\2\2\u0296\u0297\7\66\2\2\u0297\u0298\5\34\17\2\u0298"+
		"\u0299\7\67\2\2\u0299i\3\2\2\2\u029a\u02a1\78\2\2\u029b\u02a2\5\u0096"+
		"L\2\u029c\u02a2\5\u0098M\2\u029d\u02a2\7{\2\2\u029e\u02a2\5p9\2\u029f"+
		"\u02a2\5n8\2\u02a0\u02a2\5r:\2\u02a1\u029b\3\2\2\2\u02a1\u029c\3\2\2\2"+
		"\u02a1\u029d\3\2\2\2\u02a1\u029e\3\2\2\2\u02a1\u029f\3\2\2\2\u02a1\u02a0"+
		"\3\2\2\2\u02a2k\3\2\2\2\u02a3\u02af\7G\2\2\u02a4\u02af\7H\2\2\u02a5\u02af"+
		"\5\u0098M\2\u02a6\u02af\5n8\2\u02a7\u02af\5p9\2\u02a8\u02af\5r:\2\u02a9"+
		"\u02af\5\u0080A\2\u02aa\u02af\5x=\2\u02ab\u02af\5t;\2\u02ac\u02af\5v<"+
		"\2\u02ad\u02af\5\u0092J\2\u02ae\u02a3\3\2\2\2\u02ae\u02a4\3\2\2\2\u02ae"+
		"\u02a5\3\2\2\2\u02ae\u02a6\3\2\2\2\u02ae\u02a7\3\2\2\2\u02ae\u02a8\3\2"+
		"\2\2\u02ae\u02a9\3\2\2\2\u02ae\u02aa\3\2\2\2\u02ae\u02ab\3\2\2\2\u02ae"+
		"\u02ac\3\2\2\2\u02ae\u02ad\3\2\2\2\u02afm\3\2\2\2\u02b0\u02b3\7!\2\2\u02b1"+
		"\u02b2\7{\2\2\u02b2\u02b4\7\f\2\2\u02b3\u02b1\3\2\2\2\u02b3\u02b4\3\2"+
		"\2\2\u02b4\u02b5\3\2\2\2\u02b5\u02b6\7{\2\2\u02b6o\3\2\2\2\u02b7\u02b9"+
		"\7\35\2\2\u02b8\u02ba\5\34\17\2\u02b9\u02b8\3\2\2\2\u02b9\u02ba\3\2\2"+
		"\2\u02ba\u02bb\3\2\2\2\u02bb\u02bc\7\36\2\2\u02bcq\3\2\2\2\u02bd\u02be"+
		"\79\2\2\u02bes\3\2\2\2\u02bf\u02c0\7\t\2\2\u02c0\u02c1\7\37\2\2\u02c1"+
		"\u02c2\5\34\17\2\u02c2\u02c3\7 \2\2\u02c3u\3\2\2\2\u02c4\u02c5\7\n\2\2"+
		"\u02c5\u02c6\7\37\2\2\u02c6\u02c7\5\34\17\2\u02c7\u02c8\7 \2\2\u02c8w"+
		"\3\2\2\2\u02c9\u02cd\7{\2\2\u02ca\u02cd\5\u0096L\2\u02cb\u02cd\3\2\2\2"+
		"\u02cc\u02c9\3\2\2\2\u02cc\u02ca\3\2\2\2\u02cc\u02cb\3\2\2\2\u02cd\u02ce"+
		"\3\2\2\2\u02ce\u02d0\7\f\2\2\u02cf\u02cc\3\2\2\2\u02cf\u02d0\3\2\2\2\u02d0"+
		"\u02d3\3\2\2\2\u02d1\u02d4\5\u008eH\2\u02d2\u02d4\5\u0096L\2\u02d3\u02d1"+
		"\3\2\2\2\u02d3\u02d2\3\2\2\2\u02d4\u02d5\3\2\2\2\u02d5\u02d6\5z>\2\u02d6"+
		"y\3\2\2\2\u02d7\u02de\7\35\2\2\u02d8\u02da\5|?\2\u02d9\u02db\7\30\2\2"+
		"\u02da\u02d9\3\2\2\2\u02da\u02db\3\2\2\2\u02db\u02dd\3\2\2\2\u02dc\u02d8"+
		"\3\2\2\2\u02dd\u02e0\3\2\2\2\u02de\u02dc\3\2\2\2\u02de\u02df\3\2\2\2\u02df"+
		"\u02e1\3\2\2\2\u02e0\u02de\3\2\2\2\u02e1\u02e2\7\36\2\2\u02e2{\3\2\2\2"+
		"\u02e3\u02e6\5\36\20\2\u02e4\u02e6\7:\2\2\u02e5\u02e3\3\2\2\2\u02e5\u02e4"+
		"\3\2\2\2\u02e6}\3\2\2\2\u02e7\u02e8\7\35\2\2\u02e8\u02f0\7\36\2\2\u02e9"+
		"\u02ed\5\u0082B\2\u02ea\u02ee\7:\2\2\u02eb\u02ee\7#\2\2\u02ec\u02ee\7"+
		"\60\2\2\u02ed\u02ea\3\2\2\2\u02ed\u02eb\3\2\2\2\u02ed\u02ec\3\2\2\2\u02ed"+
		"\u02ee\3\2\2\2\u02ee\u02f0\3\2\2\2\u02ef\u02e7\3\2\2\2\u02ef\u02e9\3\2"+
		"\2\2\u02f0\177\3\2\2\2\u02f1\u02fa\7\37\2\2\u02f2\u02f7\5\u0090I\2\u02f3"+
		"\u02f4\7\30\2\2\u02f4\u02f6\5\u0090I\2\u02f5\u02f3\3\2\2\2\u02f6\u02f9"+
		"\3\2\2\2\u02f7\u02f5\3\2\2\2\u02f7\u02f8\3\2\2\2\u02f8\u02fb\3\2\2\2\u02f9"+
		"\u02f7\3\2\2\2\u02fa\u02f2\3\2\2\2\u02fa\u02fb\3\2\2\2\u02fb\u02fc\3\2"+
		"\2\2\u02fc\u0302\7 \2\2\u02fd\u02fe\7;\2\2\u02fe\u02ff\5\34\17\2\u02ff"+
		"\u0300\7<\2\2\u0300\u0302\3\2\2\2\u0301\u02f1\3\2\2\2\u0301\u02fd\3\2"+
		"\2\2\u0302\u0081\3\2\2\2\u0303\u0307\7=\2\2\u0304\u0307\5\u0084C\2\u0305"+
		"\u0307\5\u008cG\2\u0306\u0303\3\2\2\2\u0306\u0304\3\2\2\2\u0306\u0305"+
		"\3\2\2\2\u0307\u0083\3\2\2\2\u0308\u0309\t\b\2\2\u0309\u0085\3\2\2\2\u030a"+
		"\u030b\7@\2\2\u030b\u0087\3\2\2\2\u030c\u030e\5\u008cG\2\u030d\u030f\7"+
		":\2\2\u030e\u030d\3\2\2\2\u030e\u030f\3\2\2\2\u030f\u0089\3\2\2\2\u0310"+
		"\u0311\7A\2\2\u0311\u008b\3\2\2\2\u0312\u031b\7B\2\2\u0313\u031b\7C\2"+
		"\2\u0314\u031b\7D\2\2\u0315\u031b\7E\2\2\u0316\u031b\7F\2\2\u0317\u031b"+
		"\5\u0086D\2\u0318\u031b\5\u008aF\2\u0319\u031b\7G\2\2\u031a\u0312\3\2"+
		"\2\2\u031a\u0313\3\2\2\2\u031a\u0314\3\2\2\2\u031a\u0315\3\2\2\2\u031a"+
		"\u0316\3\2\2\2\u031a\u0317\3\2\2\2\u031a\u0318\3\2\2\2\u031a\u0319\3\2"+
		"\2\2\u031b\u008d\3\2\2\2\u031c\u0320\7{\2\2\u031d\u0320\5\u0086D\2\u031e"+
		"\u0320\5\u008aF\2\u031f\u031c\3\2\2\2\u031f\u031d\3\2\2\2\u031f\u031e"+
		"\3\2\2\2\u0320\u008f\3\2\2\2\u0321\u0324\5\36\20\2\u0322\u0324\7{\2\2"+
		"\u0323\u0321\3\2\2\2\u0323\u0322\3\2\2\2\u0324\u0325\3\2\2\2\u0325\u0326"+
		"\t\t\2\2\u0326\u0327\5\36\20\2\u0327\u0091\3\2\2\2\u0328\u032a\7\66\2"+
		"\2\u0329\u032b\5\34\17\2\u032a\u0329\3\2\2\2\u032a\u032b\3\2\2\2\u032b"+
		"\u032c\3\2\2\2\u032c\u032d\7\67\2\2\u032d\u0093\3\2\2\2\u032e\u032f\5"+
		"\u0098M\2\u032f\u0095\3\2\2\2\u0330\u0331\t\n\2\2\u0331\u0097\3\2\2\2"+
		"\u0332\u0333\7v\2\2\u0333\u0099\3\2\2\2[\u009f\u00a3\u00b5\u00bb\u00c0"+
		"\u00c6\u00dc\u00e1\u00e9\u00f3\u00fc\u00ff\u0106\u010d\u010f\u0115\u011a"+
		"\u011f\u0126\u012c\u0133\u0137\u013f\u0149\u014d\u0155\u0157\u0163\u0169"+
		"\u016d\u0171\u017c\u0182\u0191\u0197\u019b\u019f\u01a6\u01ad\u01b3\u01b8"+
		"\u01ba\u01be\u01c5\u01cc\u01d5\u01e1\u01eb\u01f7\u01fb\u0204\u020b\u0229"+
		"\u0231\u0235\u023c\u0243\u0249\u0250\u0258\u025f\u0265\u026b\u0271\u0276"+
		"\u0280\u0288\u028a\u02a1\u02ae\u02b3\u02b9\u02cc\u02cf\u02d3\u02da\u02de"+
		"\u02e5\u02ed\u02ef\u02f7\u02fa\u0301\u0306\u030e\u031a\u031f\u0323\u032a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}