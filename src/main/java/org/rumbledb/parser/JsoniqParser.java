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
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, Kfor=66, Klet=67, 
		Kwhere=68, Kgroup=69, Kby=70, Korder=71, Kreturn=72, Kif=73, Kin=74, Kas=75, 
		Kat=76, Kallowing=77, Kempty=78, Kcount=79, Kstable=80, Kascending=81, 
		Kdescending=82, Ksome=83, Kevery=84, Ksatisfies=85, Kcollation=86, Kgreatest=87, 
		Kleast=88, Kswitch=89, Kcase=90, Ktry=91, Kcatch=92, Kdefault=93, Kthen=94, 
		Kelse=95, Ktypeswitch=96, Kor=97, Kand=98, Knot=99, Kto=100, Kinstance=101, 
		Kof=102, Kstatically=103, Kis=104, Ktreat=105, Kcast=106, Kcastable=107, 
		Kversion=108, Kjsoniq=109, STRING=110, ArgumentPlaceholder=111, NullLiteral=112, 
		Literal=113, NumericLiteral=114, BooleanLiteral=115, IntegerLiteral=116, 
		DecimalLiteral=117, DoubleLiteral=118, WS=119, NCName=120, XQComment=121, 
		ContentChar=122;
	public static final int
		RULE_moduleAndThisIsIt = 0, RULE_module = 1, RULE_mainModule = 2, RULE_libraryModule = 3, 
		RULE_prolog = 4, RULE_setter = 5, RULE_namespaceDecl = 6, RULE_annotatedDecl = 7, 
		RULE_defaultCollationDecl = 8, RULE_orderingModeDecl = 9, RULE_emptyOrderDecl = 10, 
		RULE_decimalFormatDecl = 11, RULE_qname = 12, RULE_nCNameOrKeyWord = 13, 
		RULE_dfPropertyName = 14, RULE_moduleImport = 15, RULE_varDecl = 16, RULE_functionDecl = 17, 
		RULE_typeDecl = 18, RULE_schemaLanguage = 19, RULE_paramList = 20, RULE_param = 21, 
		RULE_expr = 22, RULE_exprSingle = 23, RULE_flowrExpr = 24, RULE_forClause = 25, 
		RULE_forVar = 26, RULE_letClause = 27, RULE_letVar = 28, RULE_whereClause = 29, 
		RULE_groupByClause = 30, RULE_groupByVar = 31, RULE_orderByClause = 32, 
		RULE_orderByExpr = 33, RULE_countClause = 34, RULE_quantifiedExpr = 35, 
		RULE_quantifiedExprVar = 36, RULE_switchExpr = 37, RULE_switchCaseClause = 38, 
		RULE_typeSwitchExpr = 39, RULE_caseClause = 40, RULE_ifExpr = 41, RULE_tryCatchExpr = 42, 
		RULE_catchClause = 43, RULE_orExpr = 44, RULE_andExpr = 45, RULE_notExpr = 46, 
		RULE_comparisonExpr = 47, RULE_stringConcatExpr = 48, RULE_rangeExpr = 49, 
		RULE_additiveExpr = 50, RULE_multiplicativeExpr = 51, RULE_instanceOfExpr = 52, 
		RULE_isStaticallyExpr = 53, RULE_treatExpr = 54, RULE_castableExpr = 55, 
		RULE_castExpr = 56, RULE_arrowExpr = 57, RULE_unaryExpr = 58, RULE_valueExpr = 59, 
		RULE_validateExpr = 60, RULE_simpleMapExpr = 61, RULE_postFixExpr = 62, 
		RULE_arrayLookup = 63, RULE_arrayUnboxing = 64, RULE_predicate = 65, RULE_objectLookup = 66, 
		RULE_primaryExpr = 67, RULE_varRef = 68, RULE_parenthesizedExpr = 69, 
		RULE_contextItemExpr = 70, RULE_orderedExpr = 71, RULE_unorderedExpr = 72, 
		RULE_functionCall = 73, RULE_argumentList = 74, RULE_argument = 75, RULE_functionItemExpr = 76, 
		RULE_namedFunctionRef = 77, RULE_inlineFunctionExpr = 78, RULE_sequenceType = 79, 
		RULE_objectConstructor = 80, RULE_itemType = 81, RULE_functionTest = 82, 
		RULE_anyFunctionTest = 83, RULE_typedFunctionTest = 84, RULE_singleType = 85, 
		RULE_pairConstructor = 86, RULE_arrayConstructor = 87, RULE_uriLiteral = 88, 
		RULE_stringLiteral = 89, RULE_keyWords = 90;
	public static final String[] ruleNames = {
		"moduleAndThisIsIt", "module", "mainModule", "libraryModule", "prolog", 
		"setter", "namespaceDecl", "annotatedDecl", "defaultCollationDecl", "orderingModeDecl", 
		"emptyOrderDecl", "decimalFormatDecl", "qname", "nCNameOrKeyWord", "dfPropertyName", 
		"moduleImport", "varDecl", "functionDecl", "typeDecl", "schemaLanguage", 
		"paramList", "param", "expr", "exprSingle", "flowrExpr", "forClause", 
		"forVar", "letClause", "letVar", "whereClause", "groupByClause", "groupByVar", 
		"orderByClause", "orderByExpr", "countClause", "quantifiedExpr", "quantifiedExprVar", 
		"switchExpr", "switchCaseClause", "typeSwitchExpr", "caseClause", "ifExpr", 
		"tryCatchExpr", "catchClause", "orExpr", "andExpr", "notExpr", "comparisonExpr", 
		"stringConcatExpr", "rangeExpr", "additiveExpr", "multiplicativeExpr", 
		"instanceOfExpr", "isStaticallyExpr", "treatExpr", "castableExpr", "castExpr", 
		"arrowExpr", "unaryExpr", "valueExpr", "validateExpr", "simpleMapExpr", 
		"postFixExpr", "arrayLookup", "arrayUnboxing", "predicate", "objectLookup", 
		"primaryExpr", "varRef", "parenthesizedExpr", "contextItemExpr", "orderedExpr", 
		"unorderedExpr", "functionCall", "argumentList", "argument", "functionItemExpr", 
		"namedFunctionRef", "inlineFunctionExpr", "sequenceType", "objectConstructor", 
		"itemType", "functionTest", "anyFunctionTest", "typedFunctionTest", "singleType", 
		"pairConstructor", "arrayConstructor", "uriLiteral", "stringLiteral", 
		"keyWords"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'module'", "'namespace'", "'='", "'declare'", "'ordering'", 
		"'ordered'", "'unordered'", "'decimal-format'", "':'", "'decimal-separator'", 
		"'grouping-separator'", "'infinity'", "'minus-sign'", "'NaN'", "'percent'", 
		"'per-mille'", "'zero-digit'", "'digit'", "'pattern-separator'", "'import'", 
		"','", "'variable'", "':='", "'external'", "'function'", "'('", "')'", 
		"'{'", "'}'", "'type'", "'jsound'", "'compact'", "'verbose'", "'json'", 
		"'schema'", "'$'", "'|'", "'*'", "'eq'", "'ne'", "'lt'", "'le'", "'gt'", 
		"'ge'", "'!='", "'<'", "'<='", "'>'", "'>='", "'||'", "'+'", "'-'", "'div'", 
		"'idiv'", "'mod'", "'validate'", "'!'", "'['", "']'", "'.'", "'$$'", "'#'", 
		"'{|'", "'|}'", "'for'", "'let'", "'where'", "'group'", "'by'", "'order'", 
		"'return'", "'if'", "'in'", "'as'", "'at'", "'allowing'", "'empty'", "'count'", 
		"'stable'", "'ascending'", "'descending'", "'some'", "'every'", "'satisfies'", 
		"'collation'", "'greatest'", "'least'", "'switch'", "'case'", "'try'", 
		"'catch'", "'default'", "'then'", "'else'", "'typeswitch'", "'or'", "'and'", 
		"'not'", "'to'", "'instance'", "'of'", "'statically'", "'is'", "'treat'", 
		"'cast'", "'castable'", "'version'", "'jsoniq'", null, "'?'", "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, "Kfor", "Klet", "Kwhere", "Kgroup", 
		"Kby", "Korder", "Kreturn", "Kif", "Kin", "Kas", "Kat", "Kallowing", "Kempty", 
		"Kcount", "Kstable", "Kascending", "Kdescending", "Ksome", "Kevery", "Ksatisfies", 
		"Kcollation", "Kgreatest", "Kleast", "Kswitch", "Kcase", "Ktry", "Kcatch", 
		"Kdefault", "Kthen", "Kelse", "Ktypeswitch", "Kor", "Kand", "Knot", "Kto", 
		"Kinstance", "Kof", "Kstatically", "Kis", "Ktreat", "Kcast", "Kcastable", 
		"Kversion", "Kjsoniq", "STRING", "ArgumentPlaceholder", "NullLiteral", 
		"Literal", "NumericLiteral", "BooleanLiteral", "IntegerLiteral", "DecimalLiteral", 
		"DoubleLiteral", "WS", "NCName", "XQComment", "ContentChar"
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
			setState(182);
			module();
			setState(183);
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
			setState(190);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(185);
				match(Kjsoniq);
				setState(186);
				match(Kversion);
				setState(187);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(188);
				match(T__0);
				}
				break;
			}
			setState(194);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(192);
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
			case T__36:
			case T__51:
			case T__52:
			case T__56:
			case T__58:
			case T__61:
			case T__63:
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
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				{
				setState(193);
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
			setState(196);
			prolog();
			setState(197);
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
			setState(199);
			match(T__1);
			setState(200);
			match(T__2);
			setState(201);
			match(NCName);
			setState(202);
			match(T__3);
			setState(203);
			uriLiteral();
			setState(204);
			match(T__0);
			setState(205);
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
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(210);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(207);
						setter();
						}
						break;
					case 2:
						{
						setState(208);
						namespaceDecl();
						}
						break;
					case 3:
						{
						setState(209);
						moduleImport();
						}
						break;
					}
					setState(212);
					match(T__0);
					}
					} 
				}
				setState(218);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(224);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(219);
				annotatedDecl();
				setState(220);
				match(T__0);
				}
				}
				setState(226);
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
		enterRule(_localctx, 10, RULE_setter);
		try {
			setState(231);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(227);
				defaultCollationDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(228);
				orderingModeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(229);
				emptyOrderDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(230);
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
		enterRule(_localctx, 12, RULE_namespaceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			match(T__4);
			setState(234);
			match(T__2);
			setState(235);
			match(NCName);
			setState(236);
			match(T__3);
			setState(237);
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
		enterRule(_localctx, 14, RULE_annotatedDecl);
		try {
			setState(242);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(239);
				functionDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(240);
				varDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(241);
				typeDecl();
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
		enterRule(_localctx, 16, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			match(T__4);
			setState(245);
			match(Kdefault);
			setState(246);
			match(Kcollation);
			setState(247);
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
		enterRule(_localctx, 18, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(T__4);
			setState(250);
			match(T__5);
			setState(251);
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
		public Token emptySequenceOrder;
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
		enterRule(_localctx, 20, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			match(T__4);
			setState(254);
			match(Kdefault);
			setState(255);
			match(Korder);
			setState(256);
			match(Kempty);
			{
			setState(257);
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
		enterRule(_localctx, 22, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259);
			match(T__4);
			setState(264);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				{
				{
				setState(260);
				match(T__8);
				setState(261);
				qname();
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

	public static class QnameContext extends ParserRuleContext {
		public Token ns;
		public KeyWordsContext nskw;
		public NCNameOrKeyWordContext local_name;
		public KeyWordsContext local_namekw;
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
		enterRule(_localctx, 24, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(277);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(275);
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
				case NullLiteral:
					{
					setState(276);
					((QnameContext)_localctx).nskw = keyWords();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(279);
				match(T__9);
				}
				break;
			}
			setState(284);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(282);
				((QnameContext)_localctx).local_name = nCNameOrKeyWord();
				}
				break;
			case 2:
				{
				setState(283);
				((QnameContext)_localctx).local_namekw = keyWords();
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

	public static class NCNameOrKeyWordContext extends ParserRuleContext {
		public TerminalNode NCName() { return getToken(JsoniqParser.NCName, 0); }
		public TerminalNode NullLiteral() { return getToken(JsoniqParser.NullLiteral, 0); }
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
		enterRule(_localctx, 26, RULE_nCNameOrKeyWord);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(286);
			_la = _input.LA(1);
			if ( !(_la==NullLiteral || _la==NCName) ) {
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
		enterRule(_localctx, 28, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
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
		ModuleImportContext _localctx = new ModuleImportContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			match(T__20);
			setState(291);
			match(T__1);
			setState(295);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(292);
				match(T__2);
				setState(293);
				((ModuleImportContext)_localctx).prefix = match(NCName);
				setState(294);
				match(T__3);
				}
			}

			setState(297);
			((ModuleImportContext)_localctx).targetNamespace = uriLiteral();
			setState(307);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(298);
				match(Kat);
				setState(299);
				uriLiteral();
				setState(304);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__21) {
					{
					{
					setState(300);
					match(T__21);
					setState(301);
					uriLiteral();
					}
					}
					setState(306);
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
		enterRule(_localctx, 32, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			match(T__4);
			setState(310);
			match(T__22);
			setState(311);
			varRef();
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(312);
				match(Kas);
				setState(313);
				sequenceType();
				}
			}

			setState(323);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				{
				{
				setState(316);
				match(T__23);
				setState(317);
				exprSingle();
				}
				}
				break;
			case T__24:
				{
				{
				setState(318);
				((VarDeclContext)_localctx).external = match(T__24);
				setState(321);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__23) {
					{
					setState(319);
					match(T__23);
					setState(320);
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
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
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
		enterRule(_localctx, 34, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(325);
			match(T__4);
			setState(326);
			match(T__25);
			setState(327);
			((FunctionDeclContext)_localctx).fn_name = qname();
			setState(328);
			match(T__26);
			setState(330);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__36) {
				{
				setState(329);
				paramList();
				}
			}

			setState(332);
			match(T__27);
			setState(335);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(333);
				match(Kas);
				setState(334);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(343);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				{
				setState(337);
				match(T__28);
				setState(339);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__36) | (1L << T__51) | (1L << T__52) | (1L << T__56) | (1L << T__58) | (1L << T__61))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
					{
					setState(338);
					((FunctionDeclContext)_localctx).fn_body = expr();
					}
				}

				setState(341);
				match(T__29);
				}
				break;
			case T__24:
				{
				setState(342);
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

	public static class TypeDeclContext extends ParserRuleContext {
		public QnameContext type_name;
		public SchemaLanguageContext schema;
		public ExprSingleContext type_definition;
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
		enterRule(_localctx, 36, RULE_typeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			match(T__4);
			setState(346);
			match(T__30);
			setState(347);
			((TypeDeclContext)_localctx).type_name = qname();
			setState(348);
			match(Kas);
			setState(350);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31 || _la==T__34) {
				{
				setState(349);
				((TypeDeclContext)_localctx).schema = schemaLanguage();
				}
			}

			setState(352);
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
		enterRule(_localctx, 38, RULE_schemaLanguage);
		try {
			setState(360);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(354);
				match(T__31);
				setState(355);
				match(T__32);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(356);
				match(T__31);
				setState(357);
				match(T__33);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(358);
				match(T__34);
				setState(359);
				match(T__35);
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
		enterRule(_localctx, 40, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			param();
			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(363);
				match(T__21);
				setState(364);
				param();
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
		enterRule(_localctx, 42, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			match(T__36);
			setState(371);
			qname();
			setState(374);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(372);
				match(Kas);
				setState(373);
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
		enterRule(_localctx, 44, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			exprSingle();
			setState(381);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(377);
				match(T__21);
				setState(378);
				exprSingle();
				}
				}
				setState(383);
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
		enterRule(_localctx, 46, RULE_exprSingle);
		try {
			setState(391);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(384);
				flowrExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(385);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(386);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(387);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(388);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(389);
				tryCatchExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(390);
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
		enterRule(_localctx, 48, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(393);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(394);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(405);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (Kfor - 66)) | (1L << (Klet - 66)) | (1L << (Kwhere - 66)) | (1L << (Kgroup - 66)) | (1L << (Korder - 66)) | (1L << (Kcount - 66)) | (1L << (Kstable - 66)))) != 0)) {
				{
				setState(403);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(397);
					forClause();
					}
					break;
				case Kwhere:
					{
					setState(398);
					whereClause();
					}
					break;
				case Klet:
					{
					setState(399);
					letClause();
					}
					break;
				case Kgroup:
					{
					setState(400);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(401);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(402);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(407);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(408);
			match(Kreturn);
			setState(409);
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
		enterRule(_localctx, 50, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(Kfor);
			setState(412);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(417);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(413);
				match(T__21);
				setState(414);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
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
		enterRule(_localctx, 52, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(423);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(421);
				match(Kas);
				setState(422);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(427);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(425);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(426);
				match(Kempty);
				}
			}

			setState(431);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(429);
				match(Kat);
				setState(430);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(433);
			match(Kin);
			setState(434);
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
		enterRule(_localctx, 54, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			match(Klet);
			setState(437);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(442);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(438);
				match(T__21);
				setState(439);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(444);
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
		enterRule(_localctx, 56, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(445);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(448);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(446);
				match(Kas);
				setState(447);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(450);
			match(T__23);
			setState(451);
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
		enterRule(_localctx, 58, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(453);
			match(Kwhere);
			setState(454);
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
		enterRule(_localctx, 60, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(456);
			match(Kgroup);
			setState(457);
			match(Kby);
			setState(458);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(463);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(459);
				match(T__21);
				setState(460);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(465);
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
		enterRule(_localctx, 62, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(473);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23 || _la==Kas) {
				{
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(467);
					match(Kas);
					setState(468);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(471);
				((GroupByVarContext)_localctx).decl = match(T__23);
				setState(472);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(477);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(475);
				match(Kcollation);
				setState(476);
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
		enterRule(_localctx, 64, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(479);
				match(Korder);
				setState(480);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(481);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(482);
				match(Korder);
				setState(483);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(486);
			orderByExpr();
			setState(491);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(487);
				match(T__21);
				setState(488);
				orderByExpr();
				}
				}
				setState(493);
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
		enterRule(_localctx, 66, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(494);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(497);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(495);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(496);
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
			setState(504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(499);
				match(Kempty);
				setState(502);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(500);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(501);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(508);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(506);
				match(Kcollation);
				setState(507);
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
		enterRule(_localctx, 68, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(510);
			match(Kcount);
			setState(511);
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
		enterRule(_localctx, 70, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(513);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(514);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(517);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(522);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__21) {
				{
				{
				setState(518);
				match(T__21);
				setState(519);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(524);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(525);
			match(Ksatisfies);
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
		enterRule(_localctx, 72, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
			varRef();
			setState(531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(529);
				match(Kas);
				setState(530);
				sequenceType();
				}
			}

			setState(533);
			match(Kin);
			setState(534);
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
		enterRule(_localctx, 74, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(536);
			match(Kswitch);
			setState(537);
			match(T__26);
			setState(538);
			((SwitchExprContext)_localctx).cond = expr();
			setState(539);
			match(T__27);
			setState(541); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(540);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(543); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(545);
			match(Kdefault);
			setState(546);
			match(Kreturn);
			setState(547);
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
		enterRule(_localctx, 76, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(551); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(549);
				match(Kcase);
				setState(550);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(553); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(555);
			match(Kreturn);
			setState(556);
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
		enterRule(_localctx, 78, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(558);
			match(Ktypeswitch);
			setState(559);
			match(T__26);
			setState(560);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(561);
			match(T__27);
			setState(563); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(562);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(565); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(567);
			match(Kdefault);
			setState(569);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__36) {
				{
				setState(568);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(571);
			match(Kreturn);
			setState(572);
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
		enterRule(_localctx, 80, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(574);
			match(Kcase);
			setState(578);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__36) {
				{
				setState(575);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(576);
				match(Kas);
				}
			}

			setState(580);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(585);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__37) {
				{
				{
				setState(581);
				match(T__37);
				setState(582);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(587);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(588);
			match(Kreturn);
			setState(589);
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
		enterRule(_localctx, 82, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(591);
			match(Kif);
			setState(592);
			match(T__26);
			setState(593);
			((IfExprContext)_localctx).test_condition = expr();
			setState(594);
			match(T__27);
			setState(595);
			match(Kthen);
			setState(596);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(597);
			match(Kelse);
			setState(598);
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
		enterRule(_localctx, 84, RULE_tryCatchExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(600);
			match(Ktry);
			setState(601);
			match(T__28);
			setState(602);
			((TryCatchExprContext)_localctx).try_expression = expr();
			setState(603);
			match(T__29);
			setState(605); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(604);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(607); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
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
		public Token s39;
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
		enterRule(_localctx, 86, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(609);
			match(Kcatch);
			setState(612);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__38:
				{
				setState(610);
				((CatchClauseContext)_localctx).s39 = match(T__38);
				((CatchClauseContext)_localctx).jokers.add(((CatchClauseContext)_localctx).s39);
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
			case NullLiteral:
			case NCName:
				{
				setState(611);
				((CatchClauseContext)_localctx).qname = qname();
				((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(621);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__37) {
				{
				{
				setState(614);
				match(T__37);
				setState(617);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__38:
					{
					setState(615);
					((CatchClauseContext)_localctx).s39 = match(T__38);
					((CatchClauseContext)_localctx).jokers.add(((CatchClauseContext)_localctx).s39);
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
				case NullLiteral:
				case NCName:
					{
					setState(616);
					((CatchClauseContext)_localctx).qname = qname();
					((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(623);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(624);
			match(T__28);
			setState(625);
			((CatchClauseContext)_localctx).catch_expression = expr();
			setState(626);
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
		enterRule(_localctx, 88, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(628);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(633);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(629);
					match(Kor);
					setState(630);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(635);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 90, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(636);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(641);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(637);
					match(Kand);
					setState(638);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(643);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 92, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(645);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(644);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(647);
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
		public Token s40;
		public List<Token> op = new ArrayList<Token>();
		public Token s41;
		public Token s42;
		public Token s43;
		public Token s44;
		public Token s45;
		public Token s4;
		public Token s46;
		public Token s47;
		public Token s48;
		public Token s49;
		public Token s50;
		public Token _tset1222;
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
		enterRule(_localctx, 94, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(649);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(652);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44) | (1L << T__45) | (1L << T__46) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) {
				{
				setState(650);
				((ComparisonExprContext)_localctx)._tset1222 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44) | (1L << T__45) | (1L << T__46) | (1L << T__47) | (1L << T__48) | (1L << T__49))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1222 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1222);
				setState(651);
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
		enterRule(_localctx, 96, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(654);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(659);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__50) {
				{
				{
				setState(655);
				match(T__50);
				setState(656);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(661);
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
		enterRule(_localctx, 98, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(662);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(665);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				{
				setState(663);
				match(Kto);
				setState(664);
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
		public Token s52;
		public List<Token> op = new ArrayList<Token>();
		public Token s53;
		public Token _tset1331;
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
		enterRule(_localctx, 100, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(667);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(672);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,66,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(668);
					((AdditiveExprContext)_localctx)._tset1331 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__51 || _la==T__52) ) {
						((AdditiveExprContext)_localctx)._tset1331 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1331);
					setState(669);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(674);
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

	public static class MultiplicativeExprContext extends ParserRuleContext {
		public InstanceOfExprContext main_expr;
		public Token s39;
		public List<Token> op = new ArrayList<Token>();
		public Token s54;
		public Token s55;
		public Token s56;
		public Token _tset1359;
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
		enterRule(_localctx, 102, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(675);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(680);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__38) | (1L << T__53) | (1L << T__54) | (1L << T__55))) != 0)) {
				{
				{
				setState(676);
				((MultiplicativeExprContext)_localctx)._tset1359 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__38) | (1L << T__53) | (1L << T__54) | (1L << T__55))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1359 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1359);
				setState(677);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(682);
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
		enterRule(_localctx, 104, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(683);
			((InstanceOfExprContext)_localctx).main_expr = isStaticallyExpr();
			setState(687);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(684);
				match(Kinstance);
				setState(685);
				match(Kof);
				setState(686);
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
		enterRule(_localctx, 106, RULE_isStaticallyExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(689);
			((IsStaticallyExprContext)_localctx).main_expr = treatExpr();
			setState(693);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				{
				setState(690);
				match(Kis);
				setState(691);
				match(Kstatically);
				setState(692);
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
		enterRule(_localctx, 108, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(695);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(699);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(696);
				match(Ktreat);
				setState(697);
				match(Kas);
				setState(698);
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
		enterRule(_localctx, 110, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(701);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(705);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				{
				setState(702);
				match(Kcastable);
				setState(703);
				match(Kas);
				setState(704);
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
		enterRule(_localctx, 112, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(707);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(711);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				{
				setState(708);
				match(Kcast);
				setState(709);
				match(Kas);
				setState(710);
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
		enterRule(_localctx, 114, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(713);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(720);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(714);
					match(T__3);
					setState(715);
					match(T__48);
					}
					setState(717);
					((ArrowExprContext)_localctx).functionCall = functionCall();
					((ArrowExprContext)_localctx).function_call_expr.add(((ArrowExprContext)_localctx).functionCall);
					}
					} 
				}
				setState(722);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public Token s53;
		public List<Token> op = new ArrayList<Token>();
		public Token s52;
		public Token _tset1519;
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
		enterRule(_localctx, 116, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(726);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__51 || _la==T__52) {
				{
				{
				setState(723);
				((UnaryExprContext)_localctx)._tset1519 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__51 || _la==T__52) ) {
					((UnaryExprContext)_localctx)._tset1519 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1519);
				}
				}
				setState(728);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(729);
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
		public SimpleMapExprContext simpleMapExpr() {
			return getRuleContext(SimpleMapExprContext.class,0);
		}
		public ValidateExprContext validateExpr() {
			return getRuleContext(ValidateExprContext.class,0);
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
		enterRule(_localctx, 118, RULE_valueExpr);
		try {
			setState(733);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
			case T__7:
			case T__25:
			case T__26:
			case T__28:
			case T__36:
			case T__58:
			case T__61:
			case T__63:
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
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(731);
				((ValueExprContext)_localctx).simpleMap_expr = simpleMapExpr();
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 2);
				{
				setState(732);
				((ValueExprContext)_localctx).validate_expr = validateExpr();
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

	public static class ValidateExprContext extends ParserRuleContext {
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
		enterRule(_localctx, 120, RULE_validateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(735);
			match(T__56);
			setState(736);
			match(T__30);
			setState(737);
			sequenceType();
			setState(738);
			match(T__28);
			setState(739);
			expr();
			setState(740);
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
		enterRule(_localctx, 122, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(742);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(747);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__57) {
				{
				{
				setState(743);
				match(T__57);
				setState(744);
				((SimpleMapExprContext)_localctx).postFixExpr = postFixExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).postFixExpr);
				}
				}
				setState(749);
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
		enterRule(_localctx, 124, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(750);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(758);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,78,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(756);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
					case 1:
						{
						setState(751);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(752);
						predicate();
						}
						break;
					case 3:
						{
						setState(753);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(754);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(755);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(760);
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
		enterRule(_localctx, 126, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(761);
			match(T__58);
			setState(762);
			match(T__58);
			setState(763);
			expr();
			setState(764);
			match(T__59);
			setState(765);
			match(T__59);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 128, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(767);
			match(T__58);
			setState(768);
			match(T__59);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 130, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(770);
			match(T__58);
			setState(771);
			expr();
			setState(772);
			match(T__59);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 132, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(774);
			match(T__60);
			setState(781);
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
			case NullLiteral:
				{
				setState(775);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(776);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(777);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__26:
				{
				setState(778);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__36:
				{
				setState(779);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__61:
				{
				setState(780);
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
		enterRule(_localctx, 134, RULE_primaryExpr);
		try {
			setState(795);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(783);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(784);
				match(Literal);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(785);
				stringLiteral();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(786);
				varRef();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(787);
				parenthesizedExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(788);
				contextItemExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(789);
				objectConstructor();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(790);
				functionCall();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(791);
				orderedExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(792);
				unorderedExpr();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(793);
				arrayConstructor();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(794);
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
		enterRule(_localctx, 136, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(797);
			match(T__36);
			setState(798);
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
		enterRule(_localctx, 138, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			match(T__26);
			setState(802);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__36) | (1L << T__51) | (1L << T__52) | (1L << T__56) | (1L << T__58) | (1L << T__61))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				setState(801);
				expr();
				}
			}

			setState(804);
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
		enterRule(_localctx, 140, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
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
		enterRule(_localctx, 142, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(808);
			match(T__6);
			setState(809);
			match(T__28);
			setState(810);
			expr();
			setState(811);
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
		enterRule(_localctx, 144, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(813);
			match(T__7);
			setState(814);
			match(T__28);
			setState(815);
			expr();
			setState(816);
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
		enterRule(_localctx, 146, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(818);
			((FunctionCallContext)_localctx).fn_name = qname();
			setState(819);
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
		enterRule(_localctx, 148, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			match(T__26);
			setState(828);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__36) | (1L << T__51) | (1L << T__52) | (1L << T__56) | (1L << T__58) | (1L << T__61))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (STRING - 64)) | (1L << (ArgumentPlaceholder - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				{
				setState(822);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(824);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__21) {
					{
					setState(823);
					match(T__21);
					}
				}

				}
				}
				setState(830);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(831);
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
		enterRule(_localctx, 150, RULE_argument);
		try {
			setState(835);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
			case T__7:
			case T__25:
			case T__26:
			case T__28:
			case T__36:
			case T__51:
			case T__52:
			case T__56:
			case T__58:
			case T__61:
			case T__63:
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
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(833);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(834);
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
		enterRule(_localctx, 152, RULE_functionItemExpr);
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
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(837);
				namedFunctionRef();
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 2);
				{
				setState(838);
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
		enterRule(_localctx, 154, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(841);
			((NamedFunctionRefContext)_localctx).fn_name = qname();
			setState(842);
			match(T__62);
			setState(843);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInlineFunctionExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InlineFunctionExprContext inlineFunctionExpr() throws RecognitionException {
		InlineFunctionExprContext _localctx = new InlineFunctionExprContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(845);
			match(T__25);
			setState(846);
			match(T__26);
			setState(848);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__36) {
				{
				setState(847);
				paramList();
				}
			}

			setState(850);
			match(T__27);
			setState(853);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(851);
				match(Kas);
				setState(852);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(855);
			match(T__28);
			setState(857);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__36) | (1L << T__51) | (1L << T__52) | (1L << T__56) | (1L << T__58) | (1L << T__61))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				setState(856);
				((InlineFunctionExprContext)_localctx).fn_body = expr();
				}
			}

			setState(859);
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
		public Token s111;
		public List<Token> question = new ArrayList<Token>();
		public Token s39;
		public List<Token> star = new ArrayList<Token>();
		public Token s52;
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
		enterRule(_localctx, 158, RULE_sequenceType);
		try {
			setState(869);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__26:
				enterOuterAlt(_localctx, 1);
				{
				setState(861);
				match(T__26);
				setState(862);
				match(T__27);
				}
				break;
			case T__25:
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
			case NullLiteral:
			case NCName:
				enterOuterAlt(_localctx, 2);
				{
				setState(863);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(867);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
				case 1:
					{
					setState(864);
					((SequenceTypeContext)_localctx).s111 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s111);
					}
					break;
				case 2:
					{
					setState(865);
					((SequenceTypeContext)_localctx).s39 = match(T__38);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s39);
					}
					break;
				case 3:
					{
					setState(866);
					((SequenceTypeContext)_localctx).s52 = match(T__51);
					((SequenceTypeContext)_localctx).plus.add(((SequenceTypeContext)_localctx).s52);
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
		public Token s64;
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
		enterRule(_localctx, 160, RULE_objectConstructor);
		int _la;
		try {
			setState(887);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
				enterOuterAlt(_localctx, 1);
				{
				setState(871);
				match(T__28);
				setState(880);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__36) | (1L << T__51) | (1L << T__52) | (1L << T__56) | (1L << T__58) | (1L << T__61))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
					{
					setState(872);
					pairConstructor();
					setState(877);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__21) {
						{
						{
						setState(873);
						match(T__21);
						setState(874);
						pairConstructor();
						}
						}
						setState(879);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(882);
				match(T__29);
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 2);
				{
				setState(883);
				((ObjectConstructorContext)_localctx).s64 = match(T__63);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s64);
				setState(884);
				expr();
				setState(885);
				match(T__64);
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
		enterRule(_localctx, 162, RULE_itemType);
		try {
			setState(892);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(889);
				qname();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(890);
				match(NullLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(891);
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
		enterRule(_localctx, 164, RULE_functionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(896);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				{
				setState(894);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(895);
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
		enterRule(_localctx, 166, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(898);
			match(T__25);
			setState(899);
			match(T__26);
			setState(900);
			match(T__38);
			setState(901);
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

	public static class TypedFunctionTestContext extends ParserRuleContext {
		public SequenceTypeContext sequenceType;
		public List<SequenceTypeContext> st = new ArrayList<SequenceTypeContext>();
		public SequenceTypeContext rt;
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
		enterRule(_localctx, 168, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(903);
			match(T__25);
			setState(904);
			match(T__26);
			setState(913);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__25 || _la==T__26 || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (Kfor - 66)) | (1L << (Klet - 66)) | (1L << (Kwhere - 66)) | (1L << (Kgroup - 66)) | (1L << (Kby - 66)) | (1L << (Korder - 66)) | (1L << (Kreturn - 66)) | (1L << (Kif - 66)) | (1L << (Kin - 66)) | (1L << (Kas - 66)) | (1L << (Kat - 66)) | (1L << (Kallowing - 66)) | (1L << (Kempty - 66)) | (1L << (Kcount - 66)) | (1L << (Kstable - 66)) | (1L << (Kascending - 66)) | (1L << (Kdescending - 66)) | (1L << (Ksome - 66)) | (1L << (Kevery - 66)) | (1L << (Ksatisfies - 66)) | (1L << (Kcollation - 66)) | (1L << (Kgreatest - 66)) | (1L << (Kleast - 66)) | (1L << (Kswitch - 66)) | (1L << (Kcase - 66)) | (1L << (Ktry - 66)) | (1L << (Kcatch - 66)) | (1L << (Kdefault - 66)) | (1L << (Kthen - 66)) | (1L << (Kelse - 66)) | (1L << (Ktypeswitch - 66)) | (1L << (Kor - 66)) | (1L << (Kand - 66)) | (1L << (Knot - 66)) | (1L << (Kto - 66)) | (1L << (Kinstance - 66)) | (1L << (Kof - 66)) | (1L << (Kstatically - 66)) | (1L << (Kis - 66)) | (1L << (Ktreat - 66)) | (1L << (Kcast - 66)) | (1L << (Kcastable - 66)) | (1L << (Kversion - 66)) | (1L << (Kjsoniq - 66)) | (1L << (NullLiteral - 66)) | (1L << (NCName - 66)))) != 0)) {
				{
				setState(905);
				((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
				((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
				setState(910);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__21) {
					{
					{
					setState(906);
					match(T__21);
					setState(907);
					((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
					((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
					}
					}
					setState(912);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(915);
			match(T__27);
			setState(916);
			match(Kas);
			setState(917);
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
		public Token s111;
		public List<Token> question = new ArrayList<Token>();
		public ItemTypeContext itemType() {
			return getRuleContext(ItemTypeContext.class,0);
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
		enterRule(_localctx, 170, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(919);
			((SingleTypeContext)_localctx).item = itemType();
			setState(921);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
			case 1:
				{
				setState(920);
				((SingleTypeContext)_localctx).s111 = match(ArgumentPlaceholder);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).s111);
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
		enterRule(_localctx, 172, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(925);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(923);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(924);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(927);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(928);
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
		enterRule(_localctx, 174, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(930);
			match(T__58);
			setState(932);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__25) | (1L << T__26) | (1L << T__28) | (1L << T__36) | (1L << T__51) | (1L << T__52) | (1L << T__56) | (1L << T__58) | (1L << T__61))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (Kfor - 64)) | (1L << (Klet - 64)) | (1L << (Kwhere - 64)) | (1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (STRING - 64)) | (1L << (NullLiteral - 64)) | (1L << (Literal - 64)) | (1L << (NCName - 64)))) != 0)) {
				{
				setState(931);
				expr();
				}
			}

			setState(934);
			match(T__59);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 176, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(936);
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
		enterRule(_localctx, 178, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(938);
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
		public TerminalNode Kdefault() { return getToken(JsoniqParser.Kdefault, 0); }
		public TerminalNode Kelse() { return getToken(JsoniqParser.Kelse, 0); }
		public TerminalNode Kgreatest() { return getToken(JsoniqParser.Kgreatest, 0); }
		public TerminalNode Kinstance() { return getToken(JsoniqParser.Kinstance, 0); }
		public TerminalNode Kstatically() { return getToken(JsoniqParser.Kstatically, 0); }
		public TerminalNode Kis() { return getToken(JsoniqParser.Kis, 0); }
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
		enterRule(_localctx, 180, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(940);
			_la = _input.LA(1);
			if ( !(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (Kfor - 66)) | (1L << (Klet - 66)) | (1L << (Kwhere - 66)) | (1L << (Kgroup - 66)) | (1L << (Kby - 66)) | (1L << (Korder - 66)) | (1L << (Kreturn - 66)) | (1L << (Kif - 66)) | (1L << (Kin - 66)) | (1L << (Kas - 66)) | (1L << (Kat - 66)) | (1L << (Kallowing - 66)) | (1L << (Kempty - 66)) | (1L << (Kcount - 66)) | (1L << (Kstable - 66)) | (1L << (Kascending - 66)) | (1L << (Kdescending - 66)) | (1L << (Ksome - 66)) | (1L << (Kevery - 66)) | (1L << (Ksatisfies - 66)) | (1L << (Kcollation - 66)) | (1L << (Kgreatest - 66)) | (1L << (Kleast - 66)) | (1L << (Kswitch - 66)) | (1L << (Kcase - 66)) | (1L << (Ktry - 66)) | (1L << (Kcatch - 66)) | (1L << (Kdefault - 66)) | (1L << (Kthen - 66)) | (1L << (Kelse - 66)) | (1L << (Ktypeswitch - 66)) | (1L << (Kor - 66)) | (1L << (Kand - 66)) | (1L << (Knot - 66)) | (1L << (Kto - 66)) | (1L << (Kinstance - 66)) | (1L << (Kof - 66)) | (1L << (Kstatically - 66)) | (1L << (Kis - 66)) | (1L << (Ktreat - 66)) | (1L << (Kcast - 66)) | (1L << (Kcastable - 66)) | (1L << (Kversion - 66)) | (1L << (Kjsoniq - 66)) | (1L << (NullLiteral - 66)))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3|\u03b1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\3\2\3\2\3\2\3\3\3\3"+
		"\3\3\3\3\3\3\5\3\u00c1\n\3\3\3\3\3\5\3\u00c5\n\3\3\4\3\4\3\4\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\5\6\u00d5\n\6\3\6\3\6\7\6\u00d9\n\6"+
		"\f\6\16\6\u00dc\13\6\3\6\3\6\3\6\7\6\u00e1\n\6\f\6\16\6\u00e4\13\6\3\7"+
		"\3\7\3\7\3\7\5\7\u00ea\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\5\t\u00f5"+
		"\n\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\r\3\r\3\r\3\r\3\r\5\r\u010b\n\r\3\r\3\r\3\r\3\r\7\r\u0111\n\r\f\r\16"+
		"\r\u0114\13\r\3\16\3\16\5\16\u0118\n\16\3\16\5\16\u011b\n\16\3\16\3\16"+
		"\5\16\u011f\n\16\3\17\3\17\3\20\3\20\3\21\3\21\3\21\3\21\3\21\5\21\u012a"+
		"\n\21\3\21\3\21\3\21\3\21\3\21\7\21\u0131\n\21\f\21\16\21\u0134\13\21"+
		"\5\21\u0136\n\21\3\22\3\22\3\22\3\22\3\22\5\22\u013d\n\22\3\22\3\22\3"+
		"\22\3\22\3\22\5\22\u0144\n\22\5\22\u0146\n\22\3\23\3\23\3\23\3\23\3\23"+
		"\5\23\u014d\n\23\3\23\3\23\3\23\5\23\u0152\n\23\3\23\3\23\5\23\u0156\n"+
		"\23\3\23\3\23\5\23\u015a\n\23\3\24\3\24\3\24\3\24\3\24\5\24\u0161\n\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u016b\n\25\3\26\3\26\3\26"+
		"\7\26\u0170\n\26\f\26\16\26\u0173\13\26\3\27\3\27\3\27\3\27\5\27\u0179"+
		"\n\27\3\30\3\30\3\30\7\30\u017e\n\30\f\30\16\30\u0181\13\30\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\5\31\u018a\n\31\3\32\3\32\5\32\u018e\n\32\3"+
		"\32\3\32\3\32\3\32\3\32\3\32\7\32\u0196\n\32\f\32\16\32\u0199\13\32\3"+
		"\32\3\32\3\32\3\33\3\33\3\33\3\33\7\33\u01a2\n\33\f\33\16\33\u01a5\13"+
		"\33\3\34\3\34\3\34\5\34\u01aa\n\34\3\34\3\34\5\34\u01ae\n\34\3\34\3\34"+
		"\5\34\u01b2\n\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\7\35\u01bb\n\35\f"+
		"\35\16\35\u01be\13\35\3\36\3\36\3\36\5\36\u01c3\n\36\3\36\3\36\3\36\3"+
		"\37\3\37\3\37\3 \3 \3 \3 \3 \7 \u01d0\n \f \16 \u01d3\13 \3!\3!\3!\5!"+
		"\u01d8\n!\3!\3!\5!\u01dc\n!\3!\3!\5!\u01e0\n!\3\"\3\"\3\"\3\"\3\"\5\""+
		"\u01e7\n\"\3\"\3\"\3\"\7\"\u01ec\n\"\f\"\16\"\u01ef\13\"\3#\3#\3#\5#\u01f4"+
		"\n#\3#\3#\3#\5#\u01f9\n#\5#\u01fb\n#\3#\3#\5#\u01ff\n#\3$\3$\3$\3%\3%"+
		"\5%\u0206\n%\3%\3%\3%\7%\u020b\n%\f%\16%\u020e\13%\3%\3%\3%\3&\3&\3&\5"+
		"&\u0216\n&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\6\'\u0220\n\'\r\'\16\'\u0221\3"+
		"\'\3\'\3\'\3\'\3(\3(\6(\u022a\n(\r(\16(\u022b\3(\3(\3(\3)\3)\3)\3)\3)"+
		"\6)\u0236\n)\r)\16)\u0237\3)\3)\5)\u023c\n)\3)\3)\3)\3*\3*\3*\3*\5*\u0245"+
		"\n*\3*\3*\3*\7*\u024a\n*\f*\16*\u024d\13*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3"+
		"+\3+\3+\3,\3,\3,\3,\3,\6,\u0260\n,\r,\16,\u0261\3-\3-\3-\5-\u0267\n-\3"+
		"-\3-\3-\5-\u026c\n-\7-\u026e\n-\f-\16-\u0271\13-\3-\3-\3-\3-\3.\3.\3."+
		"\7.\u027a\n.\f.\16.\u027d\13.\3/\3/\3/\7/\u0282\n/\f/\16/\u0285\13/\3"+
		"\60\5\60\u0288\n\60\3\60\3\60\3\61\3\61\3\61\5\61\u028f\n\61\3\62\3\62"+
		"\3\62\7\62\u0294\n\62\f\62\16\62\u0297\13\62\3\63\3\63\3\63\5\63\u029c"+
		"\n\63\3\64\3\64\3\64\7\64\u02a1\n\64\f\64\16\64\u02a4\13\64\3\65\3\65"+
		"\3\65\7\65\u02a9\n\65\f\65\16\65\u02ac\13\65\3\66\3\66\3\66\3\66\5\66"+
		"\u02b2\n\66\3\67\3\67\3\67\3\67\5\67\u02b8\n\67\38\38\38\38\58\u02be\n"+
		"8\39\39\39\39\59\u02c4\n9\3:\3:\3:\3:\5:\u02ca\n:\3;\3;\3;\3;\3;\7;\u02d1"+
		"\n;\f;\16;\u02d4\13;\3<\7<\u02d7\n<\f<\16<\u02da\13<\3<\3<\3=\3=\5=\u02e0"+
		"\n=\3>\3>\3>\3>\3>\3>\3>\3?\3?\3?\7?\u02ec\n?\f?\16?\u02ef\13?\3@\3@\3"+
		"@\3@\3@\3@\7@\u02f7\n@\f@\16@\u02fa\13@\3A\3A\3A\3A\3A\3A\3B\3B\3B\3C"+
		"\3C\3C\3C\3D\3D\3D\3D\3D\3D\3D\5D\u0310\nD\3E\3E\3E\3E\3E\3E\3E\3E\3E"+
		"\3E\3E\3E\5E\u031e\nE\3F\3F\3F\3G\3G\5G\u0325\nG\3G\3G\3H\3H\3I\3I\3I"+
		"\3I\3I\3J\3J\3J\3J\3J\3K\3K\3K\3L\3L\3L\5L\u033b\nL\7L\u033d\nL\fL\16"+
		"L\u0340\13L\3L\3L\3M\3M\5M\u0346\nM\3N\3N\5N\u034a\nN\3O\3O\3O\3O\3P\3"+
		"P\3P\5P\u0353\nP\3P\3P\3P\5P\u0358\nP\3P\3P\5P\u035c\nP\3P\3P\3Q\3Q\3"+
		"Q\3Q\3Q\3Q\5Q\u0366\nQ\5Q\u0368\nQ\3R\3R\3R\3R\7R\u036e\nR\fR\16R\u0371"+
		"\13R\5R\u0373\nR\3R\3R\3R\3R\3R\5R\u037a\nR\3S\3S\3S\5S\u037f\nS\3T\3"+
		"T\5T\u0383\nT\3U\3U\3U\3U\3U\3V\3V\3V\3V\3V\7V\u038f\nV\fV\16V\u0392\13"+
		"V\5V\u0394\nV\3V\3V\3V\3V\3W\3W\5W\u039c\nW\3X\3X\5X\u03a0\nX\3X\3X\3"+
		"X\3Y\3Y\5Y\u03a7\nY\3Y\3Y\3Z\3Z\3[\3[\3\\\3\\\3\\\2\2]\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bd"+
		"fhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092"+
		"\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa"+
		"\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\2\13\3\2\t\n\3\2YZ\4\2rrzz\3\2\r"+
		"\26\4\2\6\6*\64\3\2\66\67\4\2))8:\4\2\f\fqq\4\2Dorr\2\u03dd\2\u00b8\3"+
		"\2\2\2\4\u00c0\3\2\2\2\6\u00c6\3\2\2\2\b\u00c9\3\2\2\2\n\u00da\3\2\2\2"+
		"\f\u00e9\3\2\2\2\16\u00eb\3\2\2\2\20\u00f4\3\2\2\2\22\u00f6\3\2\2\2\24"+
		"\u00fb\3\2\2\2\26\u00ff\3\2\2\2\30\u0105\3\2\2\2\32\u011a\3\2\2\2\34\u0120"+
		"\3\2\2\2\36\u0122\3\2\2\2 \u0124\3\2\2\2\"\u0137\3\2\2\2$\u0147\3\2\2"+
		"\2&\u015b\3\2\2\2(\u016a\3\2\2\2*\u016c\3\2\2\2,\u0174\3\2\2\2.\u017a"+
		"\3\2\2\2\60\u0189\3\2\2\2\62\u018d\3\2\2\2\64\u019d\3\2\2\2\66\u01a6\3"+
		"\2\2\28\u01b6\3\2\2\2:\u01bf\3\2\2\2<\u01c7\3\2\2\2>\u01ca\3\2\2\2@\u01d4"+
		"\3\2\2\2B\u01e6\3\2\2\2D\u01f0\3\2\2\2F\u0200\3\2\2\2H\u0205\3\2\2\2J"+
		"\u0212\3\2\2\2L\u021a\3\2\2\2N\u0229\3\2\2\2P\u0230\3\2\2\2R\u0240\3\2"+
		"\2\2T\u0251\3\2\2\2V\u025a\3\2\2\2X\u0263\3\2\2\2Z\u0276\3\2\2\2\\\u027e"+
		"\3\2\2\2^\u0287\3\2\2\2`\u028b\3\2\2\2b\u0290\3\2\2\2d\u0298\3\2\2\2f"+
		"\u029d\3\2\2\2h\u02a5\3\2\2\2j\u02ad\3\2\2\2l\u02b3\3\2\2\2n\u02b9\3\2"+
		"\2\2p\u02bf\3\2\2\2r\u02c5\3\2\2\2t\u02cb\3\2\2\2v\u02d8\3\2\2\2x\u02df"+
		"\3\2\2\2z\u02e1\3\2\2\2|\u02e8\3\2\2\2~\u02f0\3\2\2\2\u0080\u02fb\3\2"+
		"\2\2\u0082\u0301\3\2\2\2\u0084\u0304\3\2\2\2\u0086\u0308\3\2\2\2\u0088"+
		"\u031d\3\2\2\2\u008a\u031f\3\2\2\2\u008c\u0322\3\2\2\2\u008e\u0328\3\2"+
		"\2\2\u0090\u032a\3\2\2\2\u0092\u032f\3\2\2\2\u0094\u0334\3\2\2\2\u0096"+
		"\u0337\3\2\2\2\u0098\u0345\3\2\2\2\u009a\u0349\3\2\2\2\u009c\u034b\3\2"+
		"\2\2\u009e\u034f\3\2\2\2\u00a0\u0367\3\2\2\2\u00a2\u0379\3\2\2\2\u00a4"+
		"\u037e\3\2\2\2\u00a6\u0382\3\2\2\2\u00a8\u0384\3\2\2\2\u00aa\u0389\3\2"+
		"\2\2\u00ac\u0399\3\2\2\2\u00ae\u039f\3\2\2\2\u00b0\u03a4\3\2\2\2\u00b2"+
		"\u03aa\3\2\2\2\u00b4\u03ac\3\2\2\2\u00b6\u03ae\3\2\2\2\u00b8\u00b9\5\4"+
		"\3\2\u00b9\u00ba\7\2\2\3\u00ba\3\3\2\2\2\u00bb\u00bc\7o\2\2\u00bc\u00bd"+
		"\7n\2\2\u00bd\u00be\5\u00b4[\2\u00be\u00bf\7\3\2\2\u00bf\u00c1\3\2\2\2"+
		"\u00c0\u00bb\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c5"+
		"\5\b\5\2\u00c3\u00c5\5\6\4\2\u00c4\u00c2\3\2\2\2\u00c4\u00c3\3\2\2\2\u00c5"+
		"\5\3\2\2\2\u00c6\u00c7\5\n\6\2\u00c7\u00c8\5.\30\2\u00c8\7\3\2\2\2\u00c9"+
		"\u00ca\7\4\2\2\u00ca\u00cb\7\5\2\2\u00cb\u00cc\7z\2\2\u00cc\u00cd\7\6"+
		"\2\2\u00cd\u00ce\5\u00b2Z\2\u00ce\u00cf\7\3\2\2\u00cf\u00d0\5\n\6\2\u00d0"+
		"\t\3\2\2\2\u00d1\u00d5\5\f\7\2\u00d2\u00d5\5\16\b\2\u00d3\u00d5\5 \21"+
		"\2\u00d4\u00d1\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d3\3\2\2\2\u00d5\u00d6"+
		"\3\2\2\2\u00d6\u00d7\7\3\2\2\u00d7\u00d9\3\2\2\2\u00d8\u00d4\3\2\2\2\u00d9"+
		"\u00dc\3\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00e2\3\2"+
		"\2\2\u00dc\u00da\3\2\2\2\u00dd\u00de\5\20\t\2\u00de\u00df\7\3\2\2\u00df"+
		"\u00e1\3\2\2\2\u00e0\u00dd\3\2\2\2\u00e1\u00e4\3\2\2\2\u00e2\u00e0\3\2"+
		"\2\2\u00e2\u00e3\3\2\2\2\u00e3\13\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e5\u00ea"+
		"\5\22\n\2\u00e6\u00ea\5\24\13\2\u00e7\u00ea\5\26\f\2\u00e8\u00ea\5\30"+
		"\r\2\u00e9\u00e5\3\2\2\2\u00e9\u00e6\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9"+
		"\u00e8\3\2\2\2\u00ea\r\3\2\2\2\u00eb\u00ec\7\7\2\2\u00ec\u00ed\7\5\2\2"+
		"\u00ed\u00ee\7z\2\2\u00ee\u00ef\7\6\2\2\u00ef\u00f0\5\u00b2Z\2\u00f0\17"+
		"\3\2\2\2\u00f1\u00f5\5$\23\2\u00f2\u00f5\5\"\22\2\u00f3\u00f5\5&\24\2"+
		"\u00f4\u00f1\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f4\u00f3\3\2\2\2\u00f5\21"+
		"\3\2\2\2\u00f6\u00f7\7\7\2\2\u00f7\u00f8\7_\2\2\u00f8\u00f9\7X\2\2\u00f9"+
		"\u00fa\5\u00b2Z\2\u00fa\23\3\2\2\2\u00fb\u00fc\7\7\2\2\u00fc\u00fd\7\b"+
		"\2\2\u00fd\u00fe\t\2\2\2\u00fe\25\3\2\2\2\u00ff\u0100\7\7\2\2\u0100\u0101"+
		"\7_\2\2\u0101\u0102\7I\2\2\u0102\u0103\7P\2\2\u0103\u0104\t\3\2\2\u0104"+
		"\27\3\2\2\2\u0105\u010a\7\7\2\2\u0106\u0107\7\13\2\2\u0107\u010b\5\32"+
		"\16\2\u0108\u0109\7_\2\2\u0109\u010b\7\13\2\2\u010a\u0106\3\2\2\2\u010a"+
		"\u0108\3\2\2\2\u010b\u0112\3\2\2\2\u010c\u010d\5\36\20\2\u010d\u010e\7"+
		"\6\2\2\u010e\u010f\5\u00b4[\2\u010f\u0111\3\2\2\2\u0110\u010c\3\2\2\2"+
		"\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113\31"+
		"\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0118\7z\2\2\u0116\u0118\5\u00b6\\"+
		"\2\u0117\u0115\3\2\2\2\u0117\u0116\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011b"+
		"\7\f\2\2\u011a\u0117\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011e\3\2\2\2\u011c"+
		"\u011f\5\34\17\2\u011d\u011f\5\u00b6\\\2\u011e\u011c\3\2\2\2\u011e\u011d"+
		"\3\2\2\2\u011f\33\3\2\2\2\u0120\u0121\t\4\2\2\u0121\35\3\2\2\2\u0122\u0123"+
		"\t\5\2\2\u0123\37\3\2\2\2\u0124\u0125\7\27\2\2\u0125\u0129\7\4\2\2\u0126"+
		"\u0127\7\5\2\2\u0127\u0128\7z\2\2\u0128\u012a\7\6\2\2\u0129\u0126\3\2"+
		"\2\2\u0129\u012a\3\2\2\2\u012a\u012b\3\2\2\2\u012b\u0135\5\u00b2Z\2\u012c"+
		"\u012d\7N\2\2\u012d\u0132\5\u00b2Z\2\u012e\u012f\7\30\2\2\u012f\u0131"+
		"\5\u00b2Z\2\u0130\u012e\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0130\3\2\2"+
		"\2\u0132\u0133\3\2\2\2\u0133\u0136\3\2\2\2\u0134\u0132\3\2\2\2\u0135\u012c"+
		"\3\2\2\2\u0135\u0136\3\2\2\2\u0136!\3\2\2\2\u0137\u0138\7\7\2\2\u0138"+
		"\u0139\7\31\2\2\u0139\u013c\5\u008aF\2\u013a\u013b\7M\2\2\u013b\u013d"+
		"\5\u00a0Q\2\u013c\u013a\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u0145\3\2\2"+
		"\2\u013e\u013f\7\32\2\2\u013f\u0146\5\60\31\2\u0140\u0143\7\33\2\2\u0141"+
		"\u0142\7\32\2\2\u0142\u0144\5\60\31\2\u0143\u0141\3\2\2\2\u0143\u0144"+
		"\3\2\2\2\u0144\u0146\3\2\2\2\u0145\u013e\3\2\2\2\u0145\u0140\3\2\2\2\u0146"+
		"#\3\2\2\2\u0147\u0148\7\7\2\2\u0148\u0149\7\34\2\2\u0149\u014a\5\32\16"+
		"\2\u014a\u014c\7\35\2\2\u014b\u014d\5*\26\2\u014c\u014b\3\2\2\2\u014c"+
		"\u014d\3\2\2\2\u014d\u014e\3\2\2\2\u014e\u0151\7\36\2\2\u014f\u0150\7"+
		"M\2\2\u0150\u0152\5\u00a0Q\2\u0151\u014f\3\2\2\2\u0151\u0152\3\2\2\2\u0152"+
		"\u0159\3\2\2\2\u0153\u0155\7\37\2\2\u0154\u0156\5.\30\2\u0155\u0154\3"+
		"\2\2\2\u0155\u0156\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u015a\7 \2\2\u0158"+
		"\u015a\7\33\2\2\u0159\u0153\3\2\2\2\u0159\u0158\3\2\2\2\u015a%\3\2\2\2"+
		"\u015b\u015c\7\7\2\2\u015c\u015d\7!\2\2\u015d\u015e\5\32\16\2\u015e\u0160"+
		"\7M\2\2\u015f\u0161\5(\25\2\u0160\u015f\3\2\2\2\u0160\u0161\3\2\2\2\u0161"+
		"\u0162\3\2\2\2\u0162\u0163\5\60\31\2\u0163\'\3\2\2\2\u0164\u0165\7\"\2"+
		"\2\u0165\u016b\7#\2\2\u0166\u0167\7\"\2\2\u0167\u016b\7$\2\2\u0168\u0169"+
		"\7%\2\2\u0169\u016b\7&\2\2\u016a\u0164\3\2\2\2\u016a\u0166\3\2\2\2\u016a"+
		"\u0168\3\2\2\2\u016b)\3\2\2\2\u016c\u0171\5,\27\2\u016d\u016e\7\30\2\2"+
		"\u016e\u0170\5,\27\2\u016f\u016d\3\2\2\2\u0170\u0173\3\2\2\2\u0171\u016f"+
		"\3\2\2\2\u0171\u0172\3\2\2\2\u0172+\3\2\2\2\u0173\u0171\3\2\2\2\u0174"+
		"\u0175\7\'\2\2\u0175\u0178\5\32\16\2\u0176\u0177\7M\2\2\u0177\u0179\5"+
		"\u00a0Q\2\u0178\u0176\3\2\2\2\u0178\u0179\3\2\2\2\u0179-\3\2\2\2\u017a"+
		"\u017f\5\60\31\2\u017b\u017c\7\30\2\2\u017c\u017e\5\60\31\2\u017d\u017b"+
		"\3\2\2\2\u017e\u0181\3\2\2\2\u017f\u017d\3\2\2\2\u017f\u0180\3\2\2\2\u0180"+
		"/\3\2\2\2\u0181\u017f\3\2\2\2\u0182\u018a\5\62\32\2\u0183\u018a\5H%\2"+
		"\u0184\u018a\5L\'\2\u0185\u018a\5P)\2\u0186\u018a\5T+\2\u0187\u018a\5"+
		"V,\2\u0188\u018a\5Z.\2\u0189\u0182\3\2\2\2\u0189\u0183\3\2\2\2\u0189\u0184"+
		"\3\2\2\2\u0189\u0185\3\2\2\2\u0189\u0186\3\2\2\2\u0189\u0187\3\2\2\2\u0189"+
		"\u0188\3\2\2\2\u018a\61\3\2\2\2\u018b\u018e\5\64\33\2\u018c\u018e\58\35"+
		"\2\u018d\u018b\3\2\2\2\u018d\u018c\3\2\2\2\u018e\u0197\3\2\2\2\u018f\u0196"+
		"\5\64\33\2\u0190\u0196\5<\37\2\u0191\u0196\58\35\2\u0192\u0196\5> \2\u0193"+
		"\u0196\5B\"\2\u0194\u0196\5F$\2\u0195\u018f\3\2\2\2\u0195\u0190\3\2\2"+
		"\2\u0195\u0191\3\2\2\2\u0195\u0192\3\2\2\2\u0195\u0193\3\2\2\2\u0195\u0194"+
		"\3\2\2\2\u0196\u0199\3\2\2\2\u0197\u0195\3\2\2\2\u0197\u0198\3\2\2\2\u0198"+
		"\u019a\3\2\2\2\u0199\u0197\3\2\2\2\u019a\u019b\7J\2\2\u019b\u019c\5\60"+
		"\31\2\u019c\63\3\2\2\2\u019d\u019e\7D\2\2\u019e\u01a3\5\66\34\2\u019f"+
		"\u01a0\7\30\2\2\u01a0\u01a2\5\66\34\2\u01a1\u019f\3\2\2\2\u01a2\u01a5"+
		"\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4\65\3\2\2\2\u01a5"+
		"\u01a3\3\2\2\2\u01a6\u01a9\5\u008aF\2\u01a7\u01a8\7M\2\2\u01a8\u01aa\5"+
		"\u00a0Q\2\u01a9\u01a7\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ad\3\2\2\2"+
		"\u01ab\u01ac\7O\2\2\u01ac\u01ae\7P\2\2\u01ad\u01ab\3\2\2\2\u01ad\u01ae"+
		"\3\2\2\2\u01ae\u01b1\3\2\2\2\u01af\u01b0\7N\2\2\u01b0\u01b2\5\u008aF\2"+
		"\u01b1\u01af\3\2\2\2\u01b1\u01b2\3\2\2\2\u01b2\u01b3\3\2\2\2\u01b3\u01b4"+
		"\7L\2\2\u01b4\u01b5\5\60\31\2\u01b5\67\3\2\2\2\u01b6\u01b7\7E\2\2\u01b7"+
		"\u01bc\5:\36\2\u01b8\u01b9\7\30\2\2\u01b9\u01bb\5:\36\2\u01ba\u01b8\3"+
		"\2\2\2\u01bb\u01be\3\2\2\2\u01bc\u01ba\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd"+
		"9\3\2\2\2\u01be\u01bc\3\2\2\2\u01bf\u01c2\5\u008aF\2\u01c0\u01c1\7M\2"+
		"\2\u01c1\u01c3\5\u00a0Q\2\u01c2\u01c0\3\2\2\2\u01c2\u01c3\3\2\2\2\u01c3"+
		"\u01c4\3\2\2\2\u01c4\u01c5\7\32\2\2\u01c5\u01c6\5\60\31\2\u01c6;\3\2\2"+
		"\2\u01c7\u01c8\7F\2\2\u01c8\u01c9\5\60\31\2\u01c9=\3\2\2\2\u01ca\u01cb"+
		"\7G\2\2\u01cb\u01cc\7H\2\2\u01cc\u01d1\5@!\2\u01cd\u01ce\7\30\2\2\u01ce"+
		"\u01d0\5@!\2\u01cf\u01cd\3\2\2\2\u01d0\u01d3\3\2\2\2\u01d1\u01cf\3\2\2"+
		"\2\u01d1\u01d2\3\2\2\2\u01d2?\3\2\2\2\u01d3\u01d1\3\2\2\2\u01d4\u01db"+
		"\5\u008aF\2\u01d5\u01d6\7M\2\2\u01d6\u01d8\5\u00a0Q\2\u01d7\u01d5\3\2"+
		"\2\2\u01d7\u01d8\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01da\7\32\2\2\u01da"+
		"\u01dc\5\60\31\2\u01db\u01d7\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01df\3"+
		"\2\2\2\u01dd\u01de\7X\2\2\u01de\u01e0\5\u00b2Z\2\u01df\u01dd\3\2\2\2\u01df"+
		"\u01e0\3\2\2\2\u01e0A\3\2\2\2\u01e1\u01e2\7I\2\2\u01e2\u01e7\7H\2\2\u01e3"+
		"\u01e4\7R\2\2\u01e4\u01e5\7I\2\2\u01e5\u01e7\7H\2\2\u01e6\u01e1\3\2\2"+
		"\2\u01e6\u01e3\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01ed\5D#\2\u01e9\u01ea"+
		"\7\30\2\2\u01ea\u01ec\5D#\2\u01eb\u01e9\3\2\2\2\u01ec\u01ef\3\2\2\2\u01ed"+
		"\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01eeC\3\2\2\2\u01ef\u01ed\3\2\2\2"+
		"\u01f0\u01f3\5\60\31\2\u01f1\u01f4\7S\2\2\u01f2\u01f4\7T\2\2\u01f3\u01f1"+
		"\3\2\2\2\u01f3\u01f2\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4\u01fa\3\2\2\2\u01f5"+
		"\u01f8\7P\2\2\u01f6\u01f9\7Y\2\2\u01f7\u01f9\7Z\2\2\u01f8\u01f6\3\2\2"+
		"\2\u01f8\u01f7\3\2\2\2\u01f9\u01fb\3\2\2\2\u01fa\u01f5\3\2\2\2\u01fa\u01fb"+
		"\3\2\2\2\u01fb\u01fe\3\2\2\2\u01fc\u01fd\7X\2\2\u01fd\u01ff\5\u00b2Z\2"+
		"\u01fe\u01fc\3\2\2\2\u01fe\u01ff\3\2\2\2\u01ffE\3\2\2\2\u0200\u0201\7"+
		"Q\2\2\u0201\u0202\5\u008aF\2\u0202G\3\2\2\2\u0203\u0206\7U\2\2\u0204\u0206"+
		"\7V\2\2\u0205\u0203\3\2\2\2\u0205\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207"+
		"\u020c\5J&\2\u0208\u0209\7\30\2\2\u0209\u020b\5J&\2\u020a\u0208\3\2\2"+
		"\2\u020b\u020e\3\2\2\2\u020c\u020a\3\2\2\2\u020c\u020d\3\2\2\2\u020d\u020f"+
		"\3\2\2\2\u020e\u020c\3\2\2\2\u020f\u0210\7W\2\2\u0210\u0211\5\60\31\2"+
		"\u0211I\3\2\2\2\u0212\u0215\5\u008aF\2\u0213\u0214\7M\2\2\u0214\u0216"+
		"\5\u00a0Q\2\u0215\u0213\3\2\2\2\u0215\u0216\3\2\2\2\u0216\u0217\3\2\2"+
		"\2\u0217\u0218\7L\2\2\u0218\u0219\5\60\31\2\u0219K\3\2\2\2\u021a\u021b"+
		"\7[\2\2\u021b\u021c\7\35\2\2\u021c\u021d\5.\30\2\u021d\u021f\7\36\2\2"+
		"\u021e\u0220\5N(\2\u021f\u021e\3\2\2\2\u0220\u0221\3\2\2\2\u0221\u021f"+
		"\3\2\2\2\u0221\u0222\3\2\2\2\u0222\u0223\3\2\2\2\u0223\u0224\7_\2\2\u0224"+
		"\u0225\7J\2\2\u0225\u0226\5\60\31\2\u0226M\3\2\2\2\u0227\u0228\7\\\2\2"+
		"\u0228\u022a\5\60\31\2\u0229\u0227\3\2\2\2\u022a\u022b\3\2\2\2\u022b\u0229"+
		"\3\2\2\2\u022b\u022c\3\2\2\2\u022c\u022d\3\2\2\2\u022d\u022e\7J\2\2\u022e"+
		"\u022f\5\60\31\2\u022fO\3\2\2\2\u0230\u0231\7b\2\2\u0231\u0232\7\35\2"+
		"\2\u0232\u0233\5.\30\2\u0233\u0235\7\36\2\2\u0234\u0236\5R*\2\u0235\u0234"+
		"\3\2\2\2\u0236\u0237\3\2\2\2\u0237\u0235\3\2\2\2\u0237\u0238\3\2\2\2\u0238"+
		"\u0239\3\2\2\2\u0239\u023b\7_\2\2\u023a\u023c\5\u008aF\2\u023b\u023a\3"+
		"\2\2\2\u023b\u023c\3\2\2\2\u023c\u023d\3\2\2\2\u023d\u023e\7J\2\2\u023e"+
		"\u023f\5\60\31\2\u023fQ\3\2\2\2\u0240\u0244\7\\\2\2\u0241\u0242\5\u008a"+
		"F\2\u0242\u0243\7M\2\2\u0243\u0245\3\2\2\2\u0244\u0241\3\2\2\2\u0244\u0245"+
		"\3\2\2\2\u0245\u0246\3\2\2\2\u0246\u024b\5\u00a0Q\2\u0247\u0248\7(\2\2"+
		"\u0248\u024a\5\u00a0Q\2\u0249\u0247\3\2\2\2\u024a\u024d\3\2\2\2\u024b"+
		"\u0249\3\2\2\2\u024b\u024c\3\2\2\2\u024c\u024e\3\2\2\2\u024d\u024b\3\2"+
		"\2\2\u024e\u024f\7J\2\2\u024f\u0250\5\60\31\2\u0250S\3\2\2\2\u0251\u0252"+
		"\7K\2\2\u0252\u0253\7\35\2\2\u0253\u0254\5.\30\2\u0254\u0255\7\36\2\2"+
		"\u0255\u0256\7`\2\2\u0256\u0257\5\60\31\2\u0257\u0258\7a\2\2\u0258\u0259"+
		"\5\60\31\2\u0259U\3\2\2\2\u025a\u025b\7]\2\2\u025b\u025c\7\37\2\2\u025c"+
		"\u025d\5.\30\2\u025d\u025f\7 \2\2\u025e\u0260\5X-\2\u025f\u025e\3\2\2"+
		"\2\u0260\u0261\3\2\2\2\u0261\u025f\3\2\2\2\u0261\u0262\3\2\2\2\u0262W"+
		"\3\2\2\2\u0263\u0266\7^\2\2\u0264\u0267\7)\2\2\u0265\u0267\5\32\16\2\u0266"+
		"\u0264\3\2\2\2\u0266\u0265\3\2\2\2\u0267\u026f\3\2\2\2\u0268\u026b\7("+
		"\2\2\u0269\u026c\7)\2\2\u026a\u026c\5\32\16\2\u026b\u0269\3\2\2\2\u026b"+
		"\u026a\3\2\2\2\u026c\u026e\3\2\2\2\u026d\u0268\3\2\2\2\u026e\u0271\3\2"+
		"\2\2\u026f\u026d\3\2\2\2\u026f\u0270\3\2\2\2\u0270\u0272\3\2\2\2\u0271"+
		"\u026f\3\2\2\2\u0272\u0273\7\37\2\2\u0273\u0274\5.\30\2\u0274\u0275\7"+
		" \2\2\u0275Y\3\2\2\2\u0276\u027b\5\\/\2\u0277\u0278\7c\2\2\u0278\u027a"+
		"\5\\/\2\u0279\u0277\3\2\2\2\u027a\u027d\3\2\2\2\u027b\u0279\3\2\2\2\u027b"+
		"\u027c\3\2\2\2\u027c[\3\2\2\2\u027d\u027b\3\2\2\2\u027e\u0283\5^\60\2"+
		"\u027f\u0280\7d\2\2\u0280\u0282\5^\60\2\u0281\u027f\3\2\2\2\u0282\u0285"+
		"\3\2\2\2\u0283\u0281\3\2\2\2\u0283\u0284\3\2\2\2\u0284]\3\2\2\2\u0285"+
		"\u0283\3\2\2\2\u0286\u0288\7e\2\2\u0287\u0286\3\2\2\2\u0287\u0288\3\2"+
		"\2\2\u0288\u0289\3\2\2\2\u0289\u028a\5`\61\2\u028a_\3\2\2\2\u028b\u028e"+
		"\5b\62\2\u028c\u028d\t\6\2\2\u028d\u028f\5b\62\2\u028e\u028c\3\2\2\2\u028e"+
		"\u028f\3\2\2\2\u028fa\3\2\2\2\u0290\u0295\5d\63\2\u0291\u0292\7\65\2\2"+
		"\u0292\u0294\5d\63\2\u0293\u0291\3\2\2\2\u0294\u0297\3\2\2\2\u0295\u0293"+
		"\3\2\2\2\u0295\u0296\3\2\2\2\u0296c\3\2\2\2\u0297\u0295\3\2\2\2\u0298"+
		"\u029b\5f\64\2\u0299\u029a\7f\2\2\u029a\u029c\5f\64\2\u029b\u0299\3\2"+
		"\2\2\u029b\u029c\3\2\2\2\u029ce\3\2\2\2\u029d\u02a2\5h\65\2\u029e\u029f"+
		"\t\7\2\2\u029f\u02a1\5h\65\2\u02a0\u029e\3\2\2\2\u02a1\u02a4\3\2\2\2\u02a2"+
		"\u02a0\3\2\2\2\u02a2\u02a3\3\2\2\2\u02a3g\3\2\2\2\u02a4\u02a2\3\2\2\2"+
		"\u02a5\u02aa\5j\66\2\u02a6\u02a7\t\b\2\2\u02a7\u02a9\5j\66\2\u02a8\u02a6"+
		"\3\2\2\2\u02a9\u02ac\3\2\2\2\u02aa\u02a8\3\2\2\2\u02aa\u02ab\3\2\2\2\u02ab"+
		"i\3\2\2\2\u02ac\u02aa\3\2\2\2\u02ad\u02b1\5l\67\2\u02ae\u02af\7g\2\2\u02af"+
		"\u02b0\7h\2\2\u02b0\u02b2\5\u00a0Q\2\u02b1\u02ae\3\2\2\2\u02b1\u02b2\3"+
		"\2\2\2\u02b2k\3\2\2\2\u02b3\u02b7\5n8\2\u02b4\u02b5\7j\2\2\u02b5\u02b6"+
		"\7i\2\2\u02b6\u02b8\5\u00a0Q\2\u02b7\u02b4\3\2\2\2\u02b7\u02b8\3\2\2\2"+
		"\u02b8m\3\2\2\2\u02b9\u02bd\5p9\2\u02ba\u02bb\7k\2\2\u02bb\u02bc\7M\2"+
		"\2\u02bc\u02be\5\u00a0Q\2\u02bd\u02ba\3\2\2\2\u02bd\u02be\3\2\2\2\u02be"+
		"o\3\2\2\2\u02bf\u02c3\5r:\2\u02c0\u02c1\7m\2\2\u02c1\u02c2\7M\2\2\u02c2"+
		"\u02c4\5\u00acW\2\u02c3\u02c0\3\2\2\2\u02c3\u02c4\3\2\2\2\u02c4q\3\2\2"+
		"\2\u02c5\u02c9\5t;\2\u02c6\u02c7\7l\2\2\u02c7\u02c8\7M\2\2\u02c8\u02ca"+
		"\5\u00acW\2\u02c9\u02c6\3\2\2\2\u02c9\u02ca\3\2\2\2\u02cas\3\2\2\2\u02cb"+
		"\u02d2\5v<\2\u02cc\u02cd\7\6\2\2\u02cd\u02ce\7\63\2\2\u02ce\u02cf\3\2"+
		"\2\2\u02cf\u02d1\5\u0094K\2\u02d0\u02cc\3\2\2\2\u02d1\u02d4\3\2\2\2\u02d2"+
		"\u02d0\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3u\3\2\2\2\u02d4\u02d2\3\2\2\2"+
		"\u02d5\u02d7\t\7\2\2\u02d6\u02d5\3\2\2\2\u02d7\u02da\3\2\2\2\u02d8\u02d6"+
		"\3\2\2\2\u02d8\u02d9\3\2\2\2\u02d9\u02db\3\2\2\2\u02da\u02d8\3\2\2\2\u02db"+
		"\u02dc\5x=\2\u02dcw\3\2\2\2\u02dd\u02e0\5|?\2\u02de\u02e0\5z>\2\u02df"+
		"\u02dd\3\2\2\2\u02df\u02de\3\2\2\2\u02e0y\3\2\2\2\u02e1\u02e2\7;\2\2\u02e2"+
		"\u02e3\7!\2\2\u02e3\u02e4\5\u00a0Q\2\u02e4\u02e5\7\37\2\2\u02e5\u02e6"+
		"\5.\30\2\u02e6\u02e7\7 \2\2\u02e7{\3\2\2\2\u02e8\u02ed\5~@\2\u02e9\u02ea"+
		"\7<\2\2\u02ea\u02ec\5~@\2\u02eb\u02e9\3\2\2\2\u02ec\u02ef\3\2\2\2\u02ed"+
		"\u02eb\3\2\2\2\u02ed\u02ee\3\2\2\2\u02ee}\3\2\2\2\u02ef\u02ed\3\2\2\2"+
		"\u02f0\u02f8\5\u0088E\2\u02f1\u02f7\5\u0080A\2\u02f2\u02f7\5\u0084C\2"+
		"\u02f3\u02f7\5\u0086D\2\u02f4\u02f7\5\u0082B\2\u02f5\u02f7\5\u0096L\2"+
		"\u02f6\u02f1\3\2\2\2\u02f6\u02f2\3\2\2\2\u02f6\u02f3\3\2\2\2\u02f6\u02f4"+
		"\3\2\2\2\u02f6\u02f5\3\2\2\2\u02f7\u02fa\3\2\2\2\u02f8\u02f6\3\2\2\2\u02f8"+
		"\u02f9\3\2\2\2\u02f9\177\3\2\2\2\u02fa\u02f8\3\2\2\2\u02fb\u02fc\7=\2"+
		"\2\u02fc\u02fd\7=\2\2\u02fd\u02fe\5.\30\2\u02fe\u02ff\7>\2\2\u02ff\u0300"+
		"\7>\2\2\u0300\u0081\3\2\2\2\u0301\u0302\7=\2\2\u0302\u0303\7>\2\2\u0303"+
		"\u0083\3\2\2\2\u0304\u0305\7=\2\2\u0305\u0306\5.\30\2\u0306\u0307\7>\2"+
		"\2\u0307\u0085\3\2\2\2\u0308\u030f\7?\2\2\u0309\u0310\5\u00b6\\\2\u030a"+
		"\u0310\5\u00b4[\2\u030b\u0310\7z\2\2\u030c\u0310\5\u008cG\2\u030d\u0310"+
		"\5\u008aF\2\u030e\u0310\5\u008eH\2\u030f\u0309\3\2\2\2\u030f\u030a\3\2"+
		"\2\2\u030f\u030b\3\2\2\2\u030f\u030c\3\2\2\2\u030f\u030d\3\2\2\2\u030f"+
		"\u030e\3\2\2\2\u0310\u0087\3\2\2\2\u0311\u031e\7r\2\2\u0312\u031e\7s\2"+
		"\2\u0313\u031e\5\u00b4[\2\u0314\u031e\5\u008aF\2\u0315\u031e\5\u008cG"+
		"\2\u0316\u031e\5\u008eH\2\u0317\u031e\5\u00a2R\2\u0318\u031e\5\u0094K"+
		"\2\u0319\u031e\5\u0090I\2\u031a\u031e\5\u0092J\2\u031b\u031e\5\u00b0Y"+
		"\2\u031c\u031e\5\u009aN\2\u031d\u0311\3\2\2\2\u031d\u0312\3\2\2\2\u031d"+
		"\u0313\3\2\2\2\u031d\u0314\3\2\2\2\u031d\u0315\3\2\2\2\u031d\u0316\3\2"+
		"\2\2\u031d\u0317\3\2\2\2\u031d\u0318\3\2\2\2\u031d\u0319\3\2\2\2\u031d"+
		"\u031a\3\2\2\2\u031d\u031b\3\2\2\2\u031d\u031c\3\2\2\2\u031e\u0089\3\2"+
		"\2\2\u031f\u0320\7\'\2\2\u0320\u0321\5\32\16\2\u0321\u008b\3\2\2\2\u0322"+
		"\u0324\7\35\2\2\u0323\u0325\5.\30\2\u0324\u0323\3\2\2\2\u0324\u0325\3"+
		"\2\2\2\u0325\u0326\3\2\2\2\u0326\u0327\7\36\2\2\u0327\u008d\3\2\2\2\u0328"+
		"\u0329\7@\2\2\u0329\u008f\3\2\2\2\u032a\u032b\7\t\2\2\u032b\u032c\7\37"+
		"\2\2\u032c\u032d\5.\30\2\u032d\u032e\7 \2\2\u032e\u0091\3\2\2\2\u032f"+
		"\u0330\7\n\2\2\u0330\u0331\7\37\2\2\u0331\u0332\5.\30\2\u0332\u0333\7"+
		" \2\2\u0333\u0093\3\2\2\2\u0334\u0335\5\32\16\2\u0335\u0336\5\u0096L\2"+
		"\u0336\u0095\3\2\2\2\u0337\u033e\7\35\2\2\u0338\u033a\5\u0098M\2\u0339"+
		"\u033b\7\30\2\2\u033a\u0339\3\2\2\2\u033a\u033b\3\2\2\2\u033b\u033d\3"+
		"\2\2\2\u033c\u0338\3\2\2\2\u033d\u0340\3\2\2\2\u033e\u033c\3\2\2\2\u033e"+
		"\u033f\3\2\2\2\u033f\u0341\3\2\2\2\u0340\u033e\3\2\2\2\u0341\u0342\7\36"+
		"\2\2\u0342\u0097\3\2\2\2\u0343\u0346\5\60\31\2\u0344\u0346\7q\2\2\u0345"+
		"\u0343\3\2\2\2\u0345\u0344\3\2\2\2\u0346\u0099\3\2\2\2\u0347\u034a\5\u009c"+
		"O\2\u0348\u034a\5\u009eP\2\u0349\u0347\3\2\2\2\u0349\u0348\3\2\2\2\u034a"+
		"\u009b\3\2\2\2\u034b\u034c\5\32\16\2\u034c\u034d\7A\2\2\u034d\u034e\7"+
		"s\2\2\u034e\u009d\3\2\2\2\u034f\u0350\7\34\2\2\u0350\u0352\7\35\2\2\u0351"+
		"\u0353\5*\26\2\u0352\u0351\3\2\2\2\u0352\u0353\3\2\2\2\u0353\u0354\3\2"+
		"\2\2\u0354\u0357\7\36\2\2\u0355\u0356\7M\2\2\u0356\u0358\5\u00a0Q\2\u0357"+
		"\u0355\3\2\2\2\u0357\u0358\3\2\2\2\u0358\u0359\3\2\2\2\u0359\u035b\7\37"+
		"\2\2\u035a\u035c\5.\30\2\u035b\u035a\3\2\2\2\u035b\u035c\3\2\2\2\u035c"+
		"\u035d\3\2\2\2\u035d\u035e\7 \2\2\u035e\u009f\3\2\2\2\u035f\u0360\7\35"+
		"\2\2\u0360\u0368\7\36\2\2\u0361\u0365\5\u00a4S\2\u0362\u0366\7q\2\2\u0363"+
		"\u0366\7)\2\2\u0364\u0366\7\66\2\2\u0365\u0362\3\2\2\2\u0365\u0363\3\2"+
		"\2\2\u0365\u0364\3\2\2\2\u0365\u0366\3\2\2\2\u0366\u0368\3\2\2\2\u0367"+
		"\u035f\3\2\2\2\u0367\u0361\3\2\2\2\u0368\u00a1\3\2\2\2\u0369\u0372\7\37"+
		"\2\2\u036a\u036f\5\u00aeX\2\u036b\u036c\7\30\2\2\u036c\u036e\5\u00aeX"+
		"\2\u036d\u036b\3\2\2\2\u036e\u0371\3\2\2\2\u036f\u036d\3\2\2\2\u036f\u0370"+
		"\3\2\2\2\u0370\u0373\3\2\2\2\u0371\u036f\3\2\2\2\u0372\u036a\3\2\2\2\u0372"+
		"\u0373\3\2\2\2\u0373\u0374\3\2\2\2\u0374\u037a\7 \2\2\u0375\u0376\7B\2"+
		"\2\u0376\u0377\5.\30\2\u0377\u0378\7C\2\2\u0378\u037a\3\2\2\2\u0379\u0369"+
		"\3\2\2\2\u0379\u0375\3\2\2\2\u037a\u00a3\3\2\2\2\u037b\u037f\5\32\16\2"+
		"\u037c\u037f\7r\2\2\u037d\u037f\5\u00a6T\2\u037e\u037b\3\2\2\2\u037e\u037c"+
		"\3\2\2\2\u037e\u037d\3\2\2\2\u037f\u00a5\3\2\2\2\u0380\u0383\5\u00a8U"+
		"\2\u0381\u0383\5\u00aaV\2\u0382\u0380\3\2\2\2\u0382\u0381\3\2\2\2\u0383"+
		"\u00a7\3\2\2\2\u0384\u0385\7\34\2\2\u0385\u0386\7\35\2\2\u0386\u0387\7"+
		")\2\2\u0387\u0388\7\36\2\2\u0388\u00a9\3\2\2\2\u0389\u038a\7\34\2\2\u038a"+
		"\u0393\7\35\2\2\u038b\u0390\5\u00a0Q\2\u038c\u038d\7\30\2\2\u038d\u038f"+
		"\5\u00a0Q\2\u038e\u038c\3\2\2\2\u038f\u0392\3\2\2\2\u0390\u038e\3\2\2"+
		"\2\u0390\u0391\3\2\2\2\u0391\u0394\3\2\2\2\u0392\u0390\3\2\2\2\u0393\u038b"+
		"\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0395\3\2\2\2\u0395\u0396\7\36\2\2"+
		"\u0396\u0397\7M\2\2\u0397\u0398\5\u00a0Q\2\u0398\u00ab\3\2\2\2\u0399\u039b"+
		"\5\u00a4S\2\u039a\u039c\7q\2\2\u039b\u039a\3\2\2\2\u039b\u039c\3\2\2\2"+
		"\u039c\u00ad\3\2\2\2\u039d\u03a0\5\60\31\2\u039e\u03a0\7z\2\2\u039f\u039d"+
		"\3\2\2\2\u039f\u039e\3\2\2\2\u03a0\u03a1\3\2\2\2\u03a1\u03a2\t\t\2\2\u03a2"+
		"\u03a3\5\60\31\2\u03a3\u00af\3\2\2\2\u03a4\u03a6\7=\2\2\u03a5\u03a7\5"+
		".\30\2\u03a6\u03a5\3\2\2\2\u03a6\u03a7\3\2\2\2\u03a7\u03a8\3\2\2\2\u03a8"+
		"\u03a9\7>\2\2\u03a9\u00b1\3\2\2\2\u03aa\u03ab\5\u00b4[\2\u03ab\u00b3\3"+
		"\2\2\2\u03ac\u03ad\7p\2\2\u03ad\u00b5\3\2\2\2\u03ae\u03af\t\n\2\2\u03af"+
		"\u00b7\3\2\2\2g\u00c0\u00c4\u00d4\u00da\u00e2\u00e9\u00f4\u010a\u0112"+
		"\u0117\u011a\u011e\u0129\u0132\u0135\u013c\u0143\u0145\u014c\u0151\u0155"+
		"\u0159\u0160\u016a\u0171\u0178\u017f\u0189\u018d\u0195\u0197\u01a3\u01a9"+
		"\u01ad\u01b1\u01bc\u01c2\u01d1\u01d7\u01db\u01df\u01e6\u01ed\u01f3\u01f8"+
		"\u01fa\u01fe\u0205\u020c\u0215\u0221\u022b\u0237\u023b\u0244\u024b\u0261"+
		"\u0266\u026b\u026f\u027b\u0283\u0287\u028e\u0295\u029b\u02a2\u02aa\u02b1"+
		"\u02b7\u02bd\u02c3\u02c9\u02d2\u02d8\u02df\u02ed\u02f6\u02f8\u030f\u031d"+
		"\u0324\u033a\u033e\u0345\u0349\u0352\u0357\u035b\u0365\u0367\u036f\u0372"+
		"\u0379\u037e\u0382\u0390\u0393\u039b\u039f\u03a6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}