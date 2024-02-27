// Generated from ./src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.8

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
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

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
		XQComment=136, ContentChar=137;
	public static final int
		RULE_moduleAndThisIsIt = 0, RULE_module = 1, RULE_mainModule = 2, RULE_libraryModule = 3, 
		RULE_prolog = 4, RULE_setter = 5, RULE_namespaceDecl = 6, RULE_annotatedDecl = 7, 
		RULE_defaultCollationDecl = 8, RULE_orderingModeDecl = 9, RULE_emptyOrderDecl = 10, 
		RULE_decimalFormatDecl = 11, RULE_qname = 12, RULE_dfPropertyName = 13, 
		RULE_moduleImport = 14, RULE_varDecl = 15, RULE_contextItemDecl = 16, 
		RULE_functionDecl = 17, RULE_typeDecl = 18, RULE_schemaLanguage = 19, 
		RULE_paramList = 20, RULE_param = 21, RULE_expr = 22, RULE_exprSingle = 23, 
		RULE_flowrExpr = 24, RULE_forClause = 25, RULE_forVar = 26, RULE_letClause = 27, 
		RULE_letVar = 28, RULE_whereClause = 29, RULE_groupByClause = 30, RULE_groupByVar = 31, 
		RULE_orderByClause = 32, RULE_orderByExpr = 33, RULE_countClause = 34, 
		RULE_quantifiedExpr = 35, RULE_quantifiedExprVar = 36, RULE_switchExpr = 37, 
		RULE_switchCaseClause = 38, RULE_typeSwitchExpr = 39, RULE_caseClause = 40, 
		RULE_ifExpr = 41, RULE_tryCatchExpr = 42, RULE_catchClause = 43, RULE_orExpr = 44, 
		RULE_andExpr = 45, RULE_notExpr = 46, RULE_comparisonExpr = 47, RULE_stringConcatExpr = 48, 
		RULE_rangeExpr = 49, RULE_additiveExpr = 50, RULE_multiplicativeExpr = 51, 
		RULE_instanceOfExpr = 52, RULE_isStaticallyExpr = 53, RULE_treatExpr = 54, 
		RULE_castableExpr = 55, RULE_castExpr = 56, RULE_arrowExpr = 57, RULE_arrowFunctionSpecifier = 58, 
		RULE_unaryExpr = 59, RULE_valueExpr = 60, RULE_validateExpr = 61, RULE_annotateExpr = 62, 
		RULE_simpleMapExpr = 63, RULE_postFixExpr = 64, RULE_arrayLookup = 65, 
		RULE_arrayUnboxing = 66, RULE_predicate = 67, RULE_objectLookup = 68, 
		RULE_primaryExpr = 69, RULE_varRef = 70, RULE_parenthesizedExpr = 71, 
		RULE_contextItemExpr = 72, RULE_orderedExpr = 73, RULE_unorderedExpr = 74, 
		RULE_functionCall = 75, RULE_argumentList = 76, RULE_argument = 77, RULE_functionItemExpr = 78, 
		RULE_namedFunctionRef = 79, RULE_inlineFunctionExpr = 80, RULE_insertExpr = 81, 
		RULE_deleteExpr = 82, RULE_renameExpr = 83, RULE_replaceExpr = 84, RULE_transformExpr = 85, 
		RULE_appendExpr = 86, RULE_updateLocator = 87, RULE_copyDecl = 88, RULE_sequenceType = 89, 
		RULE_objectConstructor = 90, RULE_itemType = 91, RULE_functionTest = 92, 
		RULE_anyFunctionTest = 93, RULE_typedFunctionTest = 94, RULE_singleType = 95, 
		RULE_pairConstructor = 96, RULE_arrayConstructor = 97, RULE_uriLiteral = 98, 
		RULE_stringLiteral = 99, RULE_keyWords = 100;
	private static String[] makeRuleNames() {
		return new String[] {
			"moduleAndThisIsIt", "module", "mainModule", "libraryModule", "prolog", 
			"setter", "namespaceDecl", "annotatedDecl", "defaultCollationDecl", "orderingModeDecl", 
			"emptyOrderDecl", "decimalFormatDecl", "qname", "dfPropertyName", "moduleImport", 
			"varDecl", "contextItemDecl", "functionDecl", "typeDecl", "schemaLanguage", 
			"paramList", "param", "expr", "exprSingle", "flowrExpr", "forClause", 
			"forVar", "letClause", "letVar", "whereClause", "groupByClause", "groupByVar", 
			"orderByClause", "orderByExpr", "countClause", "quantifiedExpr", "quantifiedExprVar", 
			"switchExpr", "switchCaseClause", "typeSwitchExpr", "caseClause", "ifExpr", 
			"tryCatchExpr", "catchClause", "orExpr", "andExpr", "notExpr", "comparisonExpr", 
			"stringConcatExpr", "rangeExpr", "additiveExpr", "multiplicativeExpr", 
			"instanceOfExpr", "isStaticallyExpr", "treatExpr", "castableExpr", "castExpr", 
			"arrowExpr", "arrowFunctionSpecifier", "unaryExpr", "valueExpr", "validateExpr", 
			"annotateExpr", "simpleMapExpr", "postFixExpr", "arrayLookup", "arrayUnboxing", 
			"predicate", "objectLookup", "primaryExpr", "varRef", "parenthesizedExpr", 
			"contextItemExpr", "orderedExpr", "unorderedExpr", "functionCall", "argumentList", 
			"argument", "functionItemExpr", "namedFunctionRef", "inlineFunctionExpr", 
			"insertExpr", "deleteExpr", "renameExpr", "replaceExpr", "transformExpr", 
			"appendExpr", "updateLocator", "copyDecl", "sequenceType", "objectConstructor", 
			"itemType", "functionTest", "anyFunctionTest", "typedFunctionTest", "singleType", 
			"pairConstructor", "arrayConstructor", "uriLiteral", "stringLiteral", 
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
			"'?'", "'null'"
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
			"XQComment", "ContentChar"
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
			setState(202);
			module();
			setState(203);
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
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(205);
				match(Kjsoniq);
				setState(206);
				match(Kversion);
				setState(207);
				((ModuleContext)_localctx).vers = stringLiteral();
				setState(208);
				match(T__0);
				}
				break;
			}
			setState(214);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				{
				setState(212);
				libraryModule();
				}
				break;
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
				{
				setState(213);
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
			setState(216);
			prolog();
			setState(217);
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
			setState(219);
			match(T__1);
			setState(220);
			match(T__2);
			setState(221);
			match(NCName);
			setState(222);
			match(T__3);
			setState(223);
			uriLiteral();
			setState(224);
			match(T__0);
			setState(225);
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
			setState(236);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(230);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						setState(227);
						setter();
						}
						break;
					case 2:
						{
						setState(228);
						namespaceDecl();
						}
						break;
					case 3:
						{
						setState(229);
						moduleImport();
						}
						break;
					}
					setState(232);
					match(T__0);
					}
					} 
				}
				setState(238);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(244);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(239);
					annotatedDecl();
					setState(240);
					match(T__0);
					}
					} 
				}
				setState(246);
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
			setState(251);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				defaultCollationDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(248);
				orderingModeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(249);
				emptyOrderDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(250);
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
			setState(253);
			match(Kdeclare);
			setState(254);
			match(T__2);
			setState(255);
			match(NCName);
			setState(256);
			match(T__3);
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
		enterRule(_localctx, 14, RULE_annotatedDecl);
		try {
			setState(263);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(259);
				functionDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(260);
				varDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(261);
				typeDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(262);
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
		enterRule(_localctx, 16, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			match(Kdeclare);
			setState(266);
			match(Kdefault);
			setState(267);
			match(Kcollation);
			setState(268);
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
		enterRule(_localctx, 18, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			match(Kdeclare);
			setState(271);
			match(T__4);
			setState(272);
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
		enterRule(_localctx, 20, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			match(Kdeclare);
			setState(275);
			match(Kdefault);
			setState(276);
			match(Korder);
			setState(277);
			match(Kempty);
			{
			setState(278);
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
		enterRule(_localctx, 22, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280);
			match(Kdeclare);
			setState(285);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				{
				{
				setState(281);
				match(T__6);
				setState(282);
				qname();
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(283);
				match(Kdefault);
				setState(284);
				match(T__6);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(293);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17))) != 0)) {
				{
				{
				setState(287);
				dfPropertyName();
				setState(288);
				match(T__3);
				setState(289);
				stringLiteral();
				}
				}
				setState(295);
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
		enterRule(_localctx, 24, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(301);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(298);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(296);
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
					setState(297);
					((QnameContext)_localctx).nskw = keyWords();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(300);
				match(T__7);
				}
				break;
			}
			setState(305);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				{
				setState(303);
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
				setState(304);
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
		enterRule(_localctx, 26, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17))) != 0)) ) {
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
		enterRule(_localctx, 28, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			match(T__18);
			setState(310);
			match(T__1);
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(311);
				match(T__2);
				setState(312);
				((ModuleImportContext)_localctx).prefix = match(NCName);
				setState(313);
				match(T__3);
				}
			}

			setState(316);
			((ModuleImportContext)_localctx).targetNamespace = uriLiteral();
			setState(326);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(317);
				match(Kat);
				setState(318);
				uriLiteral();
				setState(323);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(319);
					match(T__19);
					setState(320);
					uriLiteral();
					}
					}
					setState(325);
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
		enterRule(_localctx, 30, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			match(Kdeclare);
			setState(329);
			match(Kvariable);
			setState(330);
			varRef();
			setState(333);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(331);
				match(Kas);
				setState(332);
				sequenceType();
				}
			}

			setState(342);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				{
				setState(335);
				match(T__20);
				setState(336);
				exprSingle();
				}
				}
				break;
			case T__21:
				{
				{
				setState(337);
				((VarDeclContext)_localctx).external = match(T__21);
				setState(340);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__20) {
					{
					setState(338);
					match(T__20);
					setState(339);
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
		enterRule(_localctx, 32, RULE_contextItemDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			match(Kdeclare);
			setState(345);
			match(Kcontext);
			setState(346);
			match(Kitem);
			setState(349);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(347);
				match(Kas);
				setState(348);
				sequenceType();
				}
			}

			setState(358);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				{
				setState(351);
				match(T__20);
				setState(352);
				exprSingle();
				}
				}
				break;
			case T__21:
				{
				{
				setState(353);
				((ContextItemDeclContext)_localctx).external = match(T__21);
				setState(356);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__20) {
					{
					setState(354);
					match(T__20);
					setState(355);
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
			setState(360);
			match(Kdeclare);
			setState(361);
			match(T__22);
			setState(362);
			((FunctionDeclContext)_localctx).fn_name = qname();
			setState(363);
			match(T__23);
			setState(365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(364);
				paramList();
				}
			}

			setState(367);
			match(T__24);
			setState(370);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(368);
				match(Kas);
				setState(369);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(378);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__25:
				{
				setState(372);
				match(T__25);
				setState(374);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
					{
					setState(373);
					((FunctionDeclContext)_localctx).fn_body = expr();
					}
				}

				setState(376);
				match(T__26);
				}
				break;
			case T__21:
				{
				setState(377);
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
		enterRule(_localctx, 36, RULE_typeDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			match(Kdeclare);
			setState(381);
			match(Ktype);
			setState(382);
			((TypeDeclContext)_localctx).type_name = qname();
			setState(383);
			match(Kas);
			setState(385);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(384);
				((TypeDeclContext)_localctx).schema = schemaLanguage();
				}
				break;
			}
			setState(387);
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
			setState(395);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(389);
				match(T__27);
				setState(390);
				match(T__28);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(391);
				match(T__27);
				setState(392);
				match(T__29);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(393);
				match(Kjson);
				setState(394);
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
			setState(397);
			param();
			setState(402);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(398);
				match(T__19);
				setState(399);
				param();
				}
				}
				setState(404);
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
			setState(405);
			match(T__31);
			setState(406);
			qname();
			setState(409);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(407);
				match(Kas);
				setState(408);
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
			setState(411);
			exprSingle();
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(412);
				match(T__19);
				setState(413);
				exprSingle();
				}
				}
				setState(418);
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
			setState(432);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(419);
				flowrExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(420);
				quantifiedExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(421);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(422);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(423);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(424);
				tryCatchExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(425);
				insertExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(426);
				deleteExpr();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(427);
				renameExpr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(428);
				replaceExpr();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(429);
				transformExpr();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(430);
				appendExpr();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(431);
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
			setState(436);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(434);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(435);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(446);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (Kfor - 60)) | (1L << (Klet - 60)) | (1L << (Kwhere - 60)) | (1L << (Kgroup - 60)) | (1L << (Korder - 60)) | (1L << (Kcount - 60)) | (1L << (Kstable - 60)))) != 0)) {
				{
				setState(444);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(438);
					forClause();
					}
					break;
				case Kwhere:
					{
					setState(439);
					whereClause();
					}
					break;
				case Klet:
					{
					setState(440);
					letClause();
					}
					break;
				case Kgroup:
					{
					setState(441);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(442);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(443);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(448);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(449);
			match(Kreturn);
			setState(450);
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
			setState(452);
			match(Kfor);
			setState(453);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(454);
				match(T__19);
				setState(455);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(460);
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
			setState(461);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(464);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(462);
				match(Kas);
				setState(463);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(468);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(466);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(467);
				match(Kempty);
				}
			}

			setState(472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(470);
				match(Kat);
				setState(471);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(474);
			match(Kin);
			setState(475);
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
			setState(477);
			match(Klet);
			setState(478);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(483);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(479);
				match(T__19);
				setState(480);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(485);
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
			setState(486);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(489);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(487);
				match(Kas);
				setState(488);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(491);
			match(T__20);
			setState(492);
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
			setState(494);
			match(Kwhere);
			setState(495);
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
			setState(497);
			match(Kgroup);
			setState(498);
			match(Kby);
			setState(499);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(500);
				match(T__19);
				setState(501);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(506);
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
			setState(507);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(514);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__20 || _la==Kas) {
				{
				setState(510);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(508);
					match(Kas);
					setState(509);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(512);
				((GroupByVarContext)_localctx).decl = match(T__20);
				setState(513);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(518);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(516);
				match(Kcollation);
				setState(517);
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
			setState(525);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(520);
				match(Korder);
				setState(521);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(522);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(523);
				match(Korder);
				setState(524);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(527);
			orderByExpr();
			setState(532);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(528);
				match(T__19);
				setState(529);
				orderByExpr();
				}
				}
				setState(534);
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
			setState(535);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(538);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(536);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(537);
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
			setState(545);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(540);
				match(Kempty);
				setState(543);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(541);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(542);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(549);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(547);
				match(Kcollation);
				setState(548);
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
			setState(551);
			match(Kcount);
			setState(552);
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
			setState(556);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(554);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(555);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(558);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(563);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(559);
				match(T__19);
				setState(560);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(565);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(566);
			match(Ksatisfies);
			setState(567);
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
			setState(569);
			varRef();
			setState(572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(570);
				match(Kas);
				setState(571);
				sequenceType();
				}
			}

			setState(574);
			match(Kin);
			setState(575);
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
			setState(577);
			match(Kswitch);
			setState(578);
			match(T__23);
			setState(579);
			((SwitchExprContext)_localctx).cond = expr();
			setState(580);
			match(T__24);
			setState(582); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(581);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(584); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(586);
			match(Kdefault);
			setState(587);
			match(Kreturn);
			setState(588);
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
			setState(592); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(590);
				match(Kcase);
				setState(591);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(594); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(596);
			match(Kreturn);
			setState(597);
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
			setState(599);
			match(Ktypeswitch);
			setState(600);
			match(T__23);
			setState(601);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(602);
			match(T__24);
			setState(604); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(603);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(606); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(608);
			match(Kdefault);
			setState(610);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(609);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(612);
			match(Kreturn);
			setState(613);
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
			setState(615);
			match(Kcase);
			setState(619);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(616);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(617);
				match(Kas);
				}
			}

			setState(621);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(626);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__32) {
				{
				{
				setState(622);
				match(T__32);
				setState(623);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(628);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(629);
			match(Kreturn);
			setState(630);
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
			setState(632);
			match(Kif);
			setState(633);
			match(T__23);
			setState(634);
			((IfExprContext)_localctx).test_condition = expr();
			setState(635);
			match(T__24);
			setState(636);
			match(Kthen);
			setState(637);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(638);
			match(Kelse);
			setState(639);
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
			setState(641);
			match(Ktry);
			setState(642);
			match(T__25);
			setState(643);
			((TryCatchExprContext)_localctx).try_expression = expr();
			setState(644);
			match(T__26);
			setState(646); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(645);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(648); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,59,_ctx);
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
		public Token s34;
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
			setState(650);
			match(Kcatch);
			setState(653);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__33:
				{
				setState(651);
				((CatchClauseContext)_localctx).s34 = match(T__33);
				((CatchClauseContext)_localctx).jokers.add(((CatchClauseContext)_localctx).s34);
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
				setState(652);
				((CatchClauseContext)_localctx).qname = qname();
				((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(662);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__32) {
				{
				{
				setState(655);
				match(T__32);
				setState(658);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__33:
					{
					setState(656);
					((CatchClauseContext)_localctx).s34 = match(T__33);
					((CatchClauseContext)_localctx).jokers.add(((CatchClauseContext)_localctx).s34);
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
					setState(657);
					((CatchClauseContext)_localctx).qname = qname();
					((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(664);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(665);
			match(T__25);
			setState(666);
			((CatchClauseContext)_localctx).catch_expression = expr();
			setState(667);
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
			setState(669);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(674);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(670);
					match(Kor);
					setState(671);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(676);
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
			setState(677);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(682);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(678);
					match(Kand);
					setState(679);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(684);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
			setState(686);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				{
				setState(685);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(688);
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
		public Token _tset1280;
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
			setState(690);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(693);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44))) != 0)) {
				{
				setState(691);
				((ComparisonExprContext)_localctx)._tset1280 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1280 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1280);
				setState(692);
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
			setState(695);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(700);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45) {
				{
				{
				setState(696);
				match(T__45);
				setState(697);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(702);
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
			setState(703);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(706);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(704);
				match(Kto);
				setState(705);
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
		public Token s47;
		public List<Token> op = new ArrayList<Token>();
		public Token s48;
		public Token _tset1389;
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
			setState(708);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(713);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(709);
					((AdditiveExprContext)_localctx)._tset1389 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__46 || _la==T__47) ) {
						((AdditiveExprContext)_localctx)._tset1389 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1389);
					setState(710);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
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

	public static class MultiplicativeExprContext extends ParserRuleContext {
		public InstanceOfExprContext main_expr;
		public Token s34;
		public List<Token> op = new ArrayList<Token>();
		public Token s49;
		public Token s50;
		public Token s51;
		public Token _tset1417;
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
			setState(716);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(721);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__33) | (1L << T__48) | (1L << T__49) | (1L << T__50))) != 0)) {
				{
				{
				setState(717);
				((MultiplicativeExprContext)_localctx)._tset1417 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__33) | (1L << T__48) | (1L << T__49) | (1L << T__50))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1417 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1417);
				setState(718);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(723);
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
			setState(724);
			((InstanceOfExprContext)_localctx).main_expr = isStaticallyExpr();
			setState(728);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				{
				setState(725);
				match(Kinstance);
				setState(726);
				match(Kof);
				setState(727);
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
			setState(730);
			((IsStaticallyExprContext)_localctx).main_expr = treatExpr();
			setState(734);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				{
				setState(731);
				match(Kis);
				setState(732);
				match(Kstatically);
				setState(733);
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
			setState(736);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(740);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(737);
				match(Ktreat);
				setState(738);
				match(Kas);
				setState(739);
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
			setState(742);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(746);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				{
				setState(743);
				match(Kcastable);
				setState(744);
				match(Kas);
				setState(745);
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
			setState(748);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(752);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				{
				setState(749);
				match(Kcast);
				setState(750);
				match(Kas);
				setState(751);
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
		enterRule(_localctx, 114, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(754);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(763);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(755);
					match(T__3);
					setState(756);
					match(T__43);
					}
					setState(758);
					((ArrowExprContext)_localctx).arrowFunctionSpecifier = arrowFunctionSpecifier();
					((ArrowExprContext)_localctx).function.add(((ArrowExprContext)_localctx).arrowFunctionSpecifier);
					setState(759);
					((ArrowExprContext)_localctx).argumentList = argumentList();
					((ArrowExprContext)_localctx).arguments.add(((ArrowExprContext)_localctx).argumentList);
					}
					} 
				}
				setState(765);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 116, RULE_arrowFunctionSpecifier);
		try {
			setState(769);
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
				setState(766);
				qname();
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 2);
				{
				setState(767);
				varRef();
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(768);
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
		public Token s48;
		public List<Token> op = new ArrayList<Token>();
		public Token s47;
		public Token _tset1596;
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
		enterRule(_localctx, 118, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(774);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__46 || _la==T__47) {
				{
				{
				setState(771);
				((UnaryExprContext)_localctx)._tset1596 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__46 || _la==T__47) ) {
					((UnaryExprContext)_localctx)._tset1596 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1596);
				}
				}
				setState(776);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(777);
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
		enterRule(_localctx, 120, RULE_valueExpr);
		try {
			setState(782);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(779);
				((ValueExprContext)_localctx).simpleMap_expr = simpleMapExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(780);
				((ValueExprContext)_localctx).validate_expr = validateExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(781);
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
		enterRule(_localctx, 122, RULE_validateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(784);
			match(Kvalidate);
			setState(785);
			match(Ktype);
			setState(786);
			sequenceType();
			setState(787);
			match(T__25);
			setState(788);
			expr();
			setState(789);
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
		enterRule(_localctx, 124, RULE_annotateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(791);
			match(Kannotate);
			setState(792);
			match(Ktype);
			setState(793);
			sequenceType();
			setState(794);
			match(T__25);
			setState(795);
			expr();
			setState(796);
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
		enterRule(_localctx, 126, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(798);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(803);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__51) {
				{
				{
				setState(799);
				match(T__51);
				setState(800);
				((SimpleMapExprContext)_localctx).postFixExpr = postFixExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).postFixExpr);
				}
				}
				setState(805);
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
		enterRule(_localctx, 128, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(814);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(812);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
					case 1:
						{
						setState(807);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(808);
						predicate();
						}
						break;
					case 3:
						{
						setState(809);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(810);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(811);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(816);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 130, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(817);
			match(T__52);
			setState(818);
			match(T__52);
			setState(819);
			expr();
			setState(820);
			match(T__53);
			setState(821);
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
		enterRule(_localctx, 132, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(823);
			match(T__52);
			setState(824);
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
		enterRule(_localctx, 134, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(826);
			match(T__52);
			setState(827);
			expr();
			setState(828);
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
		enterRule(_localctx, 136, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(830);
			match(T__54);
			setState(837);
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
				setState(831);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(832);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(833);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__23:
				{
				setState(834);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__31:
				{
				setState(835);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__55:
				{
				setState(836);
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
		enterRule(_localctx, 138, RULE_primaryExpr);
		try {
			setState(853);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(839);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(840);
				match(Ktrue);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(841);
				match(Kfalse);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(842);
				match(Literal);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(843);
				stringLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(844);
				varRef();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(845);
				parenthesizedExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(846);
				contextItemExpr();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(847);
				objectConstructor();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(848);
				functionCall();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(849);
				orderedExpr();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(850);
				unorderedExpr();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(851);
				arrayConstructor();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(852);
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
		enterRule(_localctx, 140, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(855);
			match(T__31);
			setState(856);
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
		enterRule(_localctx, 142, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
			match(T__23);
			setState(860);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(859);
				expr();
				}
			}

			setState(862);
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
		enterRule(_localctx, 144, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
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
		enterRule(_localctx, 146, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(866);
			match(T__5);
			setState(867);
			match(T__25);
			setState(868);
			expr();
			setState(869);
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
		enterRule(_localctx, 148, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(871);
			match(Kunordered);
			setState(872);
			match(T__25);
			setState(873);
			expr();
			setState(874);
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
		enterRule(_localctx, 150, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(876);
			((FunctionCallContext)_localctx).fn_name = qname();
			setState(877);
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
		enterRule(_localctx, 152, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(879);
			match(T__23);
			setState(886);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)) | (1L << (ArgumentPlaceholder - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				{
				setState(880);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(882);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__19) {
					{
					setState(881);
					match(T__19);
					}
				}

				}
				}
				setState(888);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(889);
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
		enterRule(_localctx, 154, RULE_argument);
		try {
			setState(893);
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
				setState(891);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(892);
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
		enterRule(_localctx, 156, RULE_functionItemExpr);
		try {
			setState(897);
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
				setState(895);
				namedFunctionRef();
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(896);
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
		enterRule(_localctx, 158, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(899);
			((NamedFunctionRefContext)_localctx).fn_name = qname();
			setState(900);
			match(T__56);
			setState(901);
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
		enterRule(_localctx, 160, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(903);
			match(T__22);
			setState(904);
			match(T__23);
			setState(906);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(905);
				paramList();
				}
			}

			setState(908);
			match(T__24);
			setState(911);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(909);
				match(Kas);
				setState(910);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(913);
			match(T__25);
			setState(915);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(914);
				((InlineFunctionExprContext)_localctx).fn_body = expr();
				}
			}

			setState(917);
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
		enterRule(_localctx, 162, RULE_insertExpr);
		int _la;
		try {
			setState(942);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(919);
				match(Kinsert);
				setState(920);
				match(Kjson);
				setState(921);
				((InsertExprContext)_localctx).to_insert_expr = exprSingle();
				setState(922);
				match(Kinto);
				setState(923);
				((InsertExprContext)_localctx).main_expr = exprSingle();
				setState(927);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
				case 1:
					{
					setState(924);
					match(Kat);
					setState(925);
					match(Kposition);
					setState(926);
					((InsertExprContext)_localctx).pos_expr = exprSingle();
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(929);
				match(Kinsert);
				setState(930);
				match(Kjson);
				setState(931);
				pairConstructor();
				setState(936);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(932);
					match(T__19);
					setState(933);
					pairConstructor();
					}
					}
					setState(938);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(939);
				match(Kinto);
				setState(940);
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
		enterRule(_localctx, 164, RULE_deleteExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(944);
			match(Kdelete);
			setState(945);
			match(Kjson);
			setState(946);
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
		enterRule(_localctx, 166, RULE_renameExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(948);
			match(Krename);
			setState(949);
			match(Kjson);
			setState(950);
			updateLocator();
			setState(951);
			match(Kas);
			setState(952);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitReplaceExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReplaceExprContext replaceExpr() throws RecognitionException {
		ReplaceExprContext _localctx = new ReplaceExprContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_replaceExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(954);
			match(Kreplace);
			setState(955);
			match(Kjson);
			setState(956);
			match(Kvalue);
			setState(957);
			match(Kof);
			setState(958);
			updateLocator();
			setState(959);
			match(Kwith);
			setState(960);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTransformExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransformExprContext transformExpr() throws RecognitionException {
		TransformExprContext _localctx = new TransformExprContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_transformExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962);
			match(Kcopy);
			setState(963);
			match(Kjson);
			setState(964);
			copyDecl();
			setState(969);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(965);
				match(T__19);
				setState(966);
				copyDecl();
				}
				}
				setState(971);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(972);
			match(Kmodify);
			setState(973);
			((TransformExprContext)_localctx).mod_expr = exprSingle();
			setState(974);
			match(Kreturn);
			setState(975);
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
		enterRule(_localctx, 172, RULE_appendExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(977);
			match(Kappend);
			setState(978);
			match(Kjson);
			setState(979);
			((AppendExprContext)_localctx).to_append_expr = exprSingle();
			setState(980);
			match(Kinto);
			setState(981);
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
		enterRule(_localctx, 174, RULE_updateLocator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(983);
			((UpdateLocatorContext)_localctx).main_expr = primaryExpr();
			setState(986); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(986);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__52:
						{
						setState(984);
						arrayLookup();
						}
						break;
					case T__54:
						{
						setState(985);
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
				setState(988); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
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
		enterRule(_localctx, 176, RULE_copyDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(990);
			((CopyDeclContext)_localctx).var_ref = varRef();
			setState(991);
			match(T__20);
			setState(992);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSequenceType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceTypeContext sequenceType() throws RecognitionException {
		SequenceTypeContext _localctx = new SequenceTypeContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_sequenceType);
		try {
			setState(1002);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(994);
				match(T__23);
				setState(995);
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
				setState(996);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(1000);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
				case 1:
					{
					setState(997);
					((SequenceTypeContext)_localctx).s127 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s127);
					}
					break;
				case 2:
					{
					setState(998);
					((SequenceTypeContext)_localctx).s34 = match(T__33);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s34);
					}
					break;
				case 3:
					{
					setState(999);
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
		enterRule(_localctx, 180, RULE_objectConstructor);
		int _la;
		try {
			setState(1020);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__25:
				enterOuterAlt(_localctx, 1);
				{
				setState(1004);
				match(T__25);
				setState(1013);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
					{
					setState(1005);
					pairConstructor();
					setState(1010);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__19) {
						{
						{
						setState(1006);
						match(T__19);
						setState(1007);
						pairConstructor();
						}
						}
						setState(1012);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1015);
				match(T__26);
				}
				break;
			case T__57:
				enterOuterAlt(_localctx, 2);
				{
				setState(1016);
				((ObjectConstructorContext)_localctx).s58 = match(T__57);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s58);
				setState(1017);
				expr();
				setState(1018);
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
		enterRule(_localctx, 182, RULE_itemType);
		try {
			setState(1025);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,104,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1022);
				qname();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1023);
				match(NullLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1024);
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
		enterRule(_localctx, 184, RULE_functionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1029);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(1027);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(1028);
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
		enterRule(_localctx, 186, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1031);
			match(T__22);
			setState(1032);
			match(T__23);
			setState(1033);
			match(T__33);
			setState(1034);
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
		enterRule(_localctx, 188, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1036);
			match(T__22);
			setState(1037);
			match(T__23);
			setState(1046);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 23)) & ~0x3f) == 0 && ((1L << (_la - 23)) & ((1L << (T__22 - 23)) | (1L << (T__23 - 23)) | (1L << (Kfor - 23)) | (1L << (Klet - 23)) | (1L << (Kwhere - 23)) | (1L << (Kgroup - 23)) | (1L << (Kby - 23)) | (1L << (Korder - 23)) | (1L << (Kreturn - 23)) | (1L << (Kif - 23)) | (1L << (Kin - 23)) | (1L << (Kas - 23)) | (1L << (Kat - 23)) | (1L << (Kallowing - 23)) | (1L << (Kempty - 23)) | (1L << (Kcount - 23)) | (1L << (Kstable - 23)) | (1L << (Kascending - 23)) | (1L << (Kdescending - 23)) | (1L << (Ksome - 23)) | (1L << (Kevery - 23)) | (1L << (Ksatisfies - 23)) | (1L << (Kcollation - 23)) | (1L << (Kgreatest - 23)) | (1L << (Kleast - 23)) | (1L << (Kswitch - 23)) | (1L << (Kcase - 23)) | (1L << (Ktry - 23)) | (1L << (Kcatch - 23)))) != 0) || ((((_la - 87)) & ~0x3f) == 0 && ((1L << (_la - 87)) & ((1L << (Kdefault - 87)) | (1L << (Kthen - 87)) | (1L << (Kelse - 87)) | (1L << (Ktypeswitch - 87)) | (1L << (Kor - 87)) | (1L << (Kand - 87)) | (1L << (Knot - 87)) | (1L << (Kto - 87)) | (1L << (Kinstance - 87)) | (1L << (Kof - 87)) | (1L << (Kstatically - 87)) | (1L << (Kis - 87)) | (1L << (Ktreat - 87)) | (1L << (Kcast - 87)) | (1L << (Kcastable - 87)) | (1L << (Kversion - 87)) | (1L << (Kjsoniq - 87)) | (1L << (Kunordered - 87)) | (1L << (Ktrue - 87)) | (1L << (Kfalse - 87)) | (1L << (Ktype - 87)) | (1L << (Kvalidate - 87)) | (1L << (Kannotate - 87)) | (1L << (Kdeclare - 87)) | (1L << (Kcontext - 87)) | (1L << (Kitem - 87)) | (1L << (Kvariable - 87)) | (1L << (Kinsert - 87)) | (1L << (Kdelete - 87)) | (1L << (Krename - 87)) | (1L << (Kreplace - 87)) | (1L << (Kcopy - 87)) | (1L << (Kmodify - 87)) | (1L << (Kappend - 87)) | (1L << (Kinto - 87)) | (1L << (Kvalue - 87)) | (1L << (Kjson - 87)) | (1L << (Kwith - 87)) | (1L << (Kposition - 87)) | (1L << (NullLiteral - 87)) | (1L << (NCName - 87)))) != 0)) {
				{
				setState(1038);
				((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
				((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
				setState(1043);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(1039);
					match(T__19);
					setState(1040);
					((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
					((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
					}
					}
					setState(1045);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1048);
			match(T__24);
			setState(1049);
			match(Kas);
			setState(1050);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSingleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleTypeContext singleType() throws RecognitionException {
		SingleTypeContext _localctx = new SingleTypeContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1052);
			((SingleTypeContext)_localctx).item = itemType();
			setState(1054);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
			case 1:
				{
				setState(1053);
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
		enterRule(_localctx, 192, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1058);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1056);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(1057);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(1060);
			_la = _input.LA(1);
			if ( !(_la==T__7 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1061);
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
		enterRule(_localctx, 194, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1063);
			match(T__52);
			setState(1065);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere) | (1L << Kgroup))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				setState(1064);
				expr();
				}
			}

			setState(1067);
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
		enterRule(_localctx, 196, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1069);
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
		enterRule(_localctx, 198, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1071);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitKeyWords(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeyWordsContext keyWords() throws RecognitionException {
		KeyWordsContext _localctx = new KeyWordsContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1073);
			_la = _input.LA(1);
			if ( !(((((_la - 60)) & ~0x3f) == 0 && ((1L << (_la - 60)) & ((1L << (Kfor - 60)) | (1L << (Klet - 60)) | (1L << (Kwhere - 60)) | (1L << (Kgroup - 60)) | (1L << (Kby - 60)) | (1L << (Korder - 60)) | (1L << (Kreturn - 60)) | (1L << (Kif - 60)) | (1L << (Kin - 60)) | (1L << (Kas - 60)) | (1L << (Kat - 60)) | (1L << (Kallowing - 60)) | (1L << (Kempty - 60)) | (1L << (Kcount - 60)) | (1L << (Kstable - 60)) | (1L << (Kascending - 60)) | (1L << (Kdescending - 60)) | (1L << (Ksome - 60)) | (1L << (Kevery - 60)) | (1L << (Ksatisfies - 60)) | (1L << (Kcollation - 60)) | (1L << (Kgreatest - 60)) | (1L << (Kleast - 60)) | (1L << (Kswitch - 60)) | (1L << (Kcase - 60)) | (1L << (Ktry - 60)) | (1L << (Kcatch - 60)) | (1L << (Kdefault - 60)) | (1L << (Kthen - 60)) | (1L << (Kelse - 60)) | (1L << (Ktypeswitch - 60)) | (1L << (Kor - 60)) | (1L << (Kand - 60)) | (1L << (Knot - 60)) | (1L << (Kto - 60)) | (1L << (Kinstance - 60)) | (1L << (Kof - 60)) | (1L << (Kstatically - 60)) | (1L << (Kis - 60)) | (1L << (Ktreat - 60)) | (1L << (Kcast - 60)) | (1L << (Kcastable - 60)) | (1L << (Kversion - 60)) | (1L << (Kjsoniq - 60)) | (1L << (Kunordered - 60)) | (1L << (Ktrue - 60)) | (1L << (Kfalse - 60)) | (1L << (Ktype - 60)) | (1L << (Kvalidate - 60)) | (1L << (Kannotate - 60)) | (1L << (Kdeclare - 60)) | (1L << (Kcontext - 60)) | (1L << (Kitem - 60)) | (1L << (Kvariable - 60)) | (1L << (Kinsert - 60)) | (1L << (Kdelete - 60)) | (1L << (Krename - 60)) | (1L << (Kreplace - 60)) | (1L << (Kcopy - 60)) | (1L << (Kmodify - 60)) | (1L << (Kappend - 60)) | (1L << (Kinto - 60)) | (1L << (Kvalue - 60)) | (1L << (Kjson - 60)))) != 0) || ((((_la - 124)) & ~0x3f) == 0 && ((1L << (_la - 124)) & ((1L << (Kwith - 124)) | (1L << (Kposition - 124)) | (1L << (NullLiteral - 124)))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u008b\u0436\4\2\t"+
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
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u00d5\n\3\3\3\3\3\5\3\u00d9\n\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\6\3\6\3\6\5\6\u00e9\n\6\3\6\3\6\7\6\u00ed\n\6\f\6\16\6"+
		"\u00f0\13\6\3\6\3\6\3\6\7\6\u00f5\n\6\f\6\16\6\u00f8\13\6\3\7\3\7\3\7"+
		"\3\7\5\7\u00fe\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\5\t\u010a\n"+
		"\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r"+
		"\3\r\3\r\3\r\3\r\5\r\u0120\n\r\3\r\3\r\3\r\3\r\7\r\u0126\n\r\f\r\16\r"+
		"\u0129\13\r\3\16\3\16\5\16\u012d\n\16\3\16\5\16\u0130\n\16\3\16\3\16\5"+
		"\16\u0134\n\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20\5\20\u013d\n\20\3\20"+
		"\3\20\3\20\3\20\3\20\7\20\u0144\n\20\f\20\16\20\u0147\13\20\5\20\u0149"+
		"\n\20\3\21\3\21\3\21\3\21\3\21\5\21\u0150\n\21\3\21\3\21\3\21\3\21\3\21"+
		"\5\21\u0157\n\21\5\21\u0159\n\21\3\22\3\22\3\22\3\22\3\22\5\22\u0160\n"+
		"\22\3\22\3\22\3\22\3\22\3\22\5\22\u0167\n\22\5\22\u0169\n\22\3\23\3\23"+
		"\3\23\3\23\3\23\5\23\u0170\n\23\3\23\3\23\3\23\5\23\u0175\n\23\3\23\3"+
		"\23\5\23\u0179\n\23\3\23\3\23\5\23\u017d\n\23\3\24\3\24\3\24\3\24\3\24"+
		"\5\24\u0184\n\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u018e\n"+
		"\25\3\26\3\26\3\26\7\26\u0193\n\26\f\26\16\26\u0196\13\26\3\27\3\27\3"+
		"\27\3\27\5\27\u019c\n\27\3\30\3\30\3\30\7\30\u01a1\n\30\f\30\16\30\u01a4"+
		"\13\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\5\31\u01b3\n\31\3\32\3\32\5\32\u01b7\n\32\3\32\3\32\3\32\3\32\3\32\3"+
		"\32\7\32\u01bf\n\32\f\32\16\32\u01c2\13\32\3\32\3\32\3\32\3\33\3\33\3"+
		"\33\3\33\7\33\u01cb\n\33\f\33\16\33\u01ce\13\33\3\34\3\34\3\34\5\34\u01d3"+
		"\n\34\3\34\3\34\5\34\u01d7\n\34\3\34\3\34\5\34\u01db\n\34\3\34\3\34\3"+
		"\34\3\35\3\35\3\35\3\35\7\35\u01e4\n\35\f\35\16\35\u01e7\13\35\3\36\3"+
		"\36\3\36\5\36\u01ec\n\36\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3 \3 "+
		"\7 \u01f9\n \f \16 \u01fc\13 \3!\3!\3!\5!\u0201\n!\3!\3!\5!\u0205\n!\3"+
		"!\3!\5!\u0209\n!\3\"\3\"\3\"\3\"\3\"\5\"\u0210\n\"\3\"\3\"\3\"\7\"\u0215"+
		"\n\"\f\"\16\"\u0218\13\"\3#\3#\3#\5#\u021d\n#\3#\3#\3#\5#\u0222\n#\5#"+
		"\u0224\n#\3#\3#\5#\u0228\n#\3$\3$\3$\3%\3%\5%\u022f\n%\3%\3%\3%\7%\u0234"+
		"\n%\f%\16%\u0237\13%\3%\3%\3%\3&\3&\3&\5&\u023f\n&\3&\3&\3&\3\'\3\'\3"+
		"\'\3\'\3\'\6\'\u0249\n\'\r\'\16\'\u024a\3\'\3\'\3\'\3\'\3(\3(\6(\u0253"+
		"\n(\r(\16(\u0254\3(\3(\3(\3)\3)\3)\3)\3)\6)\u025f\n)\r)\16)\u0260\3)\3"+
		")\5)\u0265\n)\3)\3)\3)\3*\3*\3*\3*\5*\u026e\n*\3*\3*\3*\7*\u0273\n*\f"+
		"*\16*\u0276\13*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\6,"+
		"\u0289\n,\r,\16,\u028a\3-\3-\3-\5-\u0290\n-\3-\3-\3-\5-\u0295\n-\7-\u0297"+
		"\n-\f-\16-\u029a\13-\3-\3-\3-\3-\3.\3.\3.\7.\u02a3\n.\f.\16.\u02a6\13"+
		".\3/\3/\3/\7/\u02ab\n/\f/\16/\u02ae\13/\3\60\5\60\u02b1\n\60\3\60\3\60"+
		"\3\61\3\61\3\61\5\61\u02b8\n\61\3\62\3\62\3\62\7\62\u02bd\n\62\f\62\16"+
		"\62\u02c0\13\62\3\63\3\63\3\63\5\63\u02c5\n\63\3\64\3\64\3\64\7\64\u02ca"+
		"\n\64\f\64\16\64\u02cd\13\64\3\65\3\65\3\65\7\65\u02d2\n\65\f\65\16\65"+
		"\u02d5\13\65\3\66\3\66\3\66\3\66\5\66\u02db\n\66\3\67\3\67\3\67\3\67\5"+
		"\67\u02e1\n\67\38\38\38\38\58\u02e7\n8\39\39\39\39\59\u02ed\n9\3:\3:\3"+
		":\3:\5:\u02f3\n:\3;\3;\3;\3;\3;\3;\3;\7;\u02fc\n;\f;\16;\u02ff\13;\3<"+
		"\3<\3<\5<\u0304\n<\3=\7=\u0307\n=\f=\16=\u030a\13=\3=\3=\3>\3>\3>\5>\u0311"+
		"\n>\3?\3?\3?\3?\3?\3?\3?\3@\3@\3@\3@\3@\3@\3@\3A\3A\3A\7A\u0324\nA\fA"+
		"\16A\u0327\13A\3B\3B\3B\3B\3B\3B\7B\u032f\nB\fB\16B\u0332\13B\3C\3C\3"+
		"C\3C\3C\3C\3D\3D\3D\3E\3E\3E\3E\3F\3F\3F\3F\3F\3F\3F\5F\u0348\nF\3G\3"+
		"G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\5G\u0358\nG\3H\3H\3H\3I\3I\5I\u035f"+
		"\nI\3I\3I\3J\3J\3K\3K\3K\3K\3K\3L\3L\3L\3L\3L\3M\3M\3M\3N\3N\3N\5N\u0375"+
		"\nN\7N\u0377\nN\fN\16N\u037a\13N\3N\3N\3O\3O\5O\u0380\nO\3P\3P\5P\u0384"+
		"\nP\3Q\3Q\3Q\3Q\3R\3R\3R\5R\u038d\nR\3R\3R\3R\5R\u0392\nR\3R\3R\5R\u0396"+
		"\nR\3R\3R\3S\3S\3S\3S\3S\3S\3S\3S\5S\u03a2\nS\3S\3S\3S\3S\3S\7S\u03a9"+
		"\nS\fS\16S\u03ac\13S\3S\3S\3S\5S\u03b1\nS\3T\3T\3T\3T\3U\3U\3U\3U\3U\3"+
		"U\3V\3V\3V\3V\3V\3V\3V\3V\3W\3W\3W\3W\3W\7W\u03ca\nW\fW\16W\u03cd\13W"+
		"\3W\3W\3W\3W\3W\3X\3X\3X\3X\3X\3X\3Y\3Y\3Y\6Y\u03dd\nY\rY\16Y\u03de\3"+
		"Z\3Z\3Z\3Z\3[\3[\3[\3[\3[\3[\5[\u03eb\n[\5[\u03ed\n[\3\\\3\\\3\\\3\\\7"+
		"\\\u03f3\n\\\f\\\16\\\u03f6\13\\\5\\\u03f8\n\\\3\\\3\\\3\\\3\\\3\\\5\\"+
		"\u03ff\n\\\3]\3]\3]\5]\u0404\n]\3^\3^\5^\u0408\n^\3_\3_\3_\3_\3_\3`\3"+
		"`\3`\3`\3`\7`\u0414\n`\f`\16`\u0417\13`\5`\u0419\n`\3`\3`\3`\3`\3a\3a"+
		"\5a\u0421\na\3b\3b\5b\u0425\nb\3b\3b\3b\3c\3c\5c\u042c\nc\3c\3c\3d\3d"+
		"\3e\3e\3f\3f\3f\2\2g\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60"+
		"\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086"+
		"\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e"+
		"\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6"+
		"\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\2\n\4\2\b"+
		"\bjj\3\2ST\3\2\13\24\4\2\6\6%/\3\2\61\62\4\2$$\63\65\4\2\n\n\u0081\u0081"+
		"\4\2>\177\u0082\u0082\2\u046d\2\u00cc\3\2\2\2\4\u00d4\3\2\2\2\6\u00da"+
		"\3\2\2\2\b\u00dd\3\2\2\2\n\u00ee\3\2\2\2\f\u00fd\3\2\2\2\16\u00ff\3\2"+
		"\2\2\20\u0109\3\2\2\2\22\u010b\3\2\2\2\24\u0110\3\2\2\2\26\u0114\3\2\2"+
		"\2\30\u011a\3\2\2\2\32\u012f\3\2\2\2\34\u0135\3\2\2\2\36\u0137\3\2\2\2"+
		" \u014a\3\2\2\2\"\u015a\3\2\2\2$\u016a\3\2\2\2&\u017e\3\2\2\2(\u018d\3"+
		"\2\2\2*\u018f\3\2\2\2,\u0197\3\2\2\2.\u019d\3\2\2\2\60\u01b2\3\2\2\2\62"+
		"\u01b6\3\2\2\2\64\u01c6\3\2\2\2\66\u01cf\3\2\2\28\u01df\3\2\2\2:\u01e8"+
		"\3\2\2\2<\u01f0\3\2\2\2>\u01f3\3\2\2\2@\u01fd\3\2\2\2B\u020f\3\2\2\2D"+
		"\u0219\3\2\2\2F\u0229\3\2\2\2H\u022e\3\2\2\2J\u023b\3\2\2\2L\u0243\3\2"+
		"\2\2N\u0252\3\2\2\2P\u0259\3\2\2\2R\u0269\3\2\2\2T\u027a\3\2\2\2V\u0283"+
		"\3\2\2\2X\u028c\3\2\2\2Z\u029f\3\2\2\2\\\u02a7\3\2\2\2^\u02b0\3\2\2\2"+
		"`\u02b4\3\2\2\2b\u02b9\3\2\2\2d\u02c1\3\2\2\2f\u02c6\3\2\2\2h\u02ce\3"+
		"\2\2\2j\u02d6\3\2\2\2l\u02dc\3\2\2\2n\u02e2\3\2\2\2p\u02e8\3\2\2\2r\u02ee"+
		"\3\2\2\2t\u02f4\3\2\2\2v\u0303\3\2\2\2x\u0308\3\2\2\2z\u0310\3\2\2\2|"+
		"\u0312\3\2\2\2~\u0319\3\2\2\2\u0080\u0320\3\2\2\2\u0082\u0328\3\2\2\2"+
		"\u0084\u0333\3\2\2\2\u0086\u0339\3\2\2\2\u0088\u033c\3\2\2\2\u008a\u0340"+
		"\3\2\2\2\u008c\u0357\3\2\2\2\u008e\u0359\3\2\2\2\u0090\u035c\3\2\2\2\u0092"+
		"\u0362\3\2\2\2\u0094\u0364\3\2\2\2\u0096\u0369\3\2\2\2\u0098\u036e\3\2"+
		"\2\2\u009a\u0371\3\2\2\2\u009c\u037f\3\2\2\2\u009e\u0383\3\2\2\2\u00a0"+
		"\u0385\3\2\2\2\u00a2\u0389\3\2\2\2\u00a4\u03b0\3\2\2\2\u00a6\u03b2\3\2"+
		"\2\2\u00a8\u03b6\3\2\2\2\u00aa\u03bc\3\2\2\2\u00ac\u03c4\3\2\2\2\u00ae"+
		"\u03d3\3\2\2\2\u00b0\u03d9\3\2\2\2\u00b2\u03e0\3\2\2\2\u00b4\u03ec\3\2"+
		"\2\2\u00b6\u03fe\3\2\2\2\u00b8\u0403\3\2\2\2\u00ba\u0407\3\2\2\2\u00bc"+
		"\u0409\3\2\2\2\u00be\u040e\3\2\2\2\u00c0\u041e\3\2\2\2\u00c2\u0424\3\2"+
		"\2\2\u00c4\u0429\3\2\2\2\u00c6\u042f\3\2\2\2\u00c8\u0431\3\2\2\2\u00ca"+
		"\u0433\3\2\2\2\u00cc\u00cd\5\4\3\2\u00cd\u00ce\7\2\2\3\u00ce\3\3\2\2\2"+
		"\u00cf\u00d0\7i\2\2\u00d0\u00d1\7h\2\2\u00d1\u00d2\5\u00c8e\2\u00d2\u00d3"+
		"\7\3\2\2\u00d3\u00d5\3\2\2\2\u00d4\u00cf\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5"+
		"\u00d8\3\2\2\2\u00d6\u00d9\5\b\5\2\u00d7\u00d9\5\6\4\2\u00d8\u00d6\3\2"+
		"\2\2\u00d8\u00d7\3\2\2\2\u00d9\5\3\2\2\2\u00da\u00db\5\n\6\2\u00db\u00dc"+
		"\5.\30\2\u00dc\7\3\2\2\2\u00dd\u00de\7\4\2\2\u00de\u00df\7\5\2\2\u00df"+
		"\u00e0\7\u0089\2\2\u00e0\u00e1\7\6\2\2\u00e1\u00e2\5\u00c6d\2\u00e2\u00e3"+
		"\7\3\2\2\u00e3\u00e4\5\n\6\2\u00e4\t\3\2\2\2\u00e5\u00e9\5\f\7\2\u00e6"+
		"\u00e9\5\16\b\2\u00e7\u00e9\5\36\20\2\u00e8\u00e5\3\2\2\2\u00e8\u00e6"+
		"\3\2\2\2\u00e8\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00eb\7\3\2\2\u00eb"+
		"\u00ed\3\2\2\2\u00ec\u00e8\3\2\2\2\u00ed\u00f0\3\2\2\2\u00ee\u00ec\3\2"+
		"\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f6\3\2\2\2\u00f0\u00ee\3\2\2\2\u00f1"+
		"\u00f2\5\20\t\2\u00f2\u00f3\7\3\2\2\u00f3\u00f5\3\2\2\2\u00f4\u00f1\3"+
		"\2\2\2\u00f5\u00f8\3\2\2\2\u00f6\u00f4\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7"+
		"\13\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f9\u00fe\5\22\n\2\u00fa\u00fe\5\24"+
		"\13\2\u00fb\u00fe\5\26\f\2\u00fc\u00fe\5\30\r\2\u00fd\u00f9\3\2\2\2\u00fd"+
		"\u00fa\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fc\3\2\2\2\u00fe\r\3\2\2\2"+
		"\u00ff\u0100\7p\2\2\u0100\u0101\7\5\2\2\u0101\u0102\7\u0089\2\2\u0102"+
		"\u0103\7\6\2\2\u0103\u0104\5\u00c6d\2\u0104\17\3\2\2\2\u0105\u010a\5$"+
		"\23\2\u0106\u010a\5 \21\2\u0107\u010a\5&\24\2\u0108\u010a\5\"\22\2\u0109"+
		"\u0105\3\2\2\2\u0109\u0106\3\2\2\2\u0109\u0107\3\2\2\2\u0109\u0108\3\2"+
		"\2\2\u010a\21\3\2\2\2\u010b\u010c\7p\2\2\u010c\u010d\7Y\2\2\u010d\u010e"+
		"\7R\2\2\u010e\u010f\5\u00c6d\2\u010f\23\3\2\2\2\u0110\u0111\7p\2\2\u0111"+
		"\u0112\7\7\2\2\u0112\u0113\t\2\2\2\u0113\25\3\2\2\2\u0114\u0115\7p\2\2"+
		"\u0115\u0116\7Y\2\2\u0116\u0117\7C\2\2\u0117\u0118\7J\2\2\u0118\u0119"+
		"\t\3\2\2\u0119\27\3\2\2\2\u011a\u011f\7p\2\2\u011b\u011c\7\t\2\2\u011c"+
		"\u0120\5\32\16\2\u011d\u011e\7Y\2\2\u011e\u0120\7\t\2\2\u011f\u011b\3"+
		"\2\2\2\u011f\u011d\3\2\2\2\u0120\u0127\3\2\2\2\u0121\u0122\5\34\17\2\u0122"+
		"\u0123\7\6\2\2\u0123\u0124\5\u00c8e\2\u0124\u0126\3\2\2\2\u0125\u0121"+
		"\3\2\2\2\u0126\u0129\3\2\2\2\u0127\u0125\3\2\2\2\u0127\u0128\3\2\2\2\u0128"+
		"\31\3\2\2\2\u0129\u0127\3\2\2\2\u012a\u012d\7\u0089\2\2\u012b\u012d\5"+
		"\u00caf\2\u012c\u012a\3\2\2\2\u012c\u012b\3\2\2\2\u012d\u012e\3\2\2\2"+
		"\u012e\u0130\7\n\2\2\u012f\u012c\3\2\2\2\u012f\u0130\3\2\2\2\u0130\u0133"+
		"\3\2\2\2\u0131\u0134\7\u0089\2\2\u0132\u0134\5\u00caf\2\u0133\u0131\3"+
		"\2\2\2\u0133\u0132\3\2\2\2\u0134\33\3\2\2\2\u0135\u0136\t\4\2\2\u0136"+
		"\35\3\2\2\2\u0137\u0138\7\25\2\2\u0138\u013c\7\4\2\2\u0139\u013a\7\5\2"+
		"\2\u013a\u013b\7\u0089\2\2\u013b\u013d\7\6\2\2\u013c\u0139\3\2\2\2\u013c"+
		"\u013d\3\2\2\2\u013d\u013e\3\2\2\2\u013e\u0148\5\u00c6d\2\u013f\u0140"+
		"\7H\2\2\u0140\u0145\5\u00c6d\2\u0141\u0142\7\26\2\2\u0142\u0144\5\u00c6"+
		"d\2\u0143\u0141\3\2\2\2\u0144\u0147\3\2\2\2\u0145\u0143\3\2\2\2\u0145"+
		"\u0146\3\2\2\2\u0146\u0149\3\2\2\2\u0147\u0145\3\2\2\2\u0148\u013f\3\2"+
		"\2\2\u0148\u0149\3\2\2\2\u0149\37\3\2\2\2\u014a\u014b\7p\2\2\u014b\u014c"+
		"\7s\2\2\u014c\u014f\5\u008eH\2\u014d\u014e\7G\2\2\u014e\u0150\5\u00b4"+
		"[\2\u014f\u014d\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u0158\3\2\2\2\u0151"+
		"\u0152\7\27\2\2\u0152\u0159\5\60\31\2\u0153\u0156\7\30\2\2\u0154\u0155"+
		"\7\27\2\2\u0155\u0157\5\60\31\2\u0156\u0154\3\2\2\2\u0156\u0157\3\2\2"+
		"\2\u0157\u0159\3\2\2\2\u0158\u0151\3\2\2\2\u0158\u0153\3\2\2\2\u0159!"+
		"\3\2\2\2\u015a\u015b\7p\2\2\u015b\u015c\7q\2\2\u015c\u015f\7r\2\2\u015d"+
		"\u015e\7G\2\2\u015e\u0160\5\u00b4[\2\u015f\u015d\3\2\2\2\u015f\u0160\3"+
		"\2\2\2\u0160\u0168\3\2\2\2\u0161\u0162\7\27\2\2\u0162\u0169\5\60\31\2"+
		"\u0163\u0166\7\30\2\2\u0164\u0165\7\27\2\2\u0165\u0167\5\60\31\2\u0166"+
		"\u0164\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u0169\3\2\2\2\u0168\u0161\3\2"+
		"\2\2\u0168\u0163\3\2\2\2\u0169#\3\2\2\2\u016a\u016b\7p\2\2\u016b\u016c"+
		"\7\31\2\2\u016c\u016d\5\32\16\2\u016d\u016f\7\32\2\2\u016e\u0170\5*\26"+
		"\2\u016f\u016e\3\2\2\2\u016f\u0170\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u0174"+
		"\7\33\2\2\u0172\u0173\7G\2\2\u0173\u0175\5\u00b4[\2\u0174\u0172\3\2\2"+
		"\2\u0174\u0175\3\2\2\2\u0175\u017c\3\2\2\2\u0176\u0178\7\34\2\2\u0177"+
		"\u0179\5.\30\2\u0178\u0177\3\2\2\2\u0178\u0179\3\2\2\2\u0179\u017a\3\2"+
		"\2\2\u017a\u017d\7\35\2\2\u017b\u017d\7\30\2\2\u017c\u0176\3\2\2\2\u017c"+
		"\u017b\3\2\2\2\u017d%\3\2\2\2\u017e\u017f\7p\2\2\u017f\u0180\7m\2\2\u0180"+
		"\u0181\5\32\16\2\u0181\u0183\7G\2\2\u0182\u0184\5(\25\2\u0183\u0182\3"+
		"\2\2\2\u0183\u0184\3\2\2\2\u0184\u0185\3\2\2\2\u0185\u0186\5\60\31\2\u0186"+
		"\'\3\2\2\2\u0187\u0188\7\36\2\2\u0188\u018e\7\37\2\2\u0189\u018a\7\36"+
		"\2\2\u018a\u018e\7 \2\2\u018b\u018c\7}\2\2\u018c\u018e\7!\2\2\u018d\u0187"+
		"\3\2\2\2\u018d\u0189\3\2\2\2\u018d\u018b\3\2\2\2\u018e)\3\2\2\2\u018f"+
		"\u0194\5,\27\2\u0190\u0191\7\26\2\2\u0191\u0193\5,\27\2\u0192\u0190\3"+
		"\2\2\2\u0193\u0196\3\2\2\2\u0194\u0192\3\2\2\2\u0194\u0195\3\2\2\2\u0195"+
		"+\3\2\2\2\u0196\u0194\3\2\2\2\u0197\u0198\7\"\2\2\u0198\u019b\5\32\16"+
		"\2\u0199\u019a\7G\2\2\u019a\u019c\5\u00b4[\2\u019b\u0199\3\2\2\2\u019b"+
		"\u019c\3\2\2\2\u019c-\3\2\2\2\u019d\u01a2\5\60\31\2\u019e\u019f\7\26\2"+
		"\2\u019f\u01a1\5\60\31\2\u01a0\u019e\3\2\2\2\u01a1\u01a4\3\2\2\2\u01a2"+
		"\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3/\3\2\2\2\u01a4\u01a2\3\2\2\2"+
		"\u01a5\u01b3\5\62\32\2\u01a6\u01b3\5H%\2\u01a7\u01b3\5L\'\2\u01a8\u01b3"+
		"\5P)\2\u01a9\u01b3\5T+\2\u01aa\u01b3\5V,\2\u01ab\u01b3\5\u00a4S\2\u01ac"+
		"\u01b3\5\u00a6T\2\u01ad\u01b3\5\u00a8U\2\u01ae\u01b3\5\u00aaV\2\u01af"+
		"\u01b3\5\u00acW\2\u01b0\u01b3\5\u00aeX\2\u01b1\u01b3\5Z.\2\u01b2\u01a5"+
		"\3\2\2\2\u01b2\u01a6\3\2\2\2\u01b2\u01a7\3\2\2\2\u01b2\u01a8\3\2\2\2\u01b2"+
		"\u01a9\3\2\2\2\u01b2\u01aa\3\2\2\2\u01b2\u01ab\3\2\2\2\u01b2\u01ac\3\2"+
		"\2\2\u01b2\u01ad\3\2\2\2\u01b2\u01ae\3\2\2\2\u01b2\u01af\3\2\2\2\u01b2"+
		"\u01b0\3\2\2\2\u01b2\u01b1\3\2\2\2\u01b3\61\3\2\2\2\u01b4\u01b7\5\64\33"+
		"\2\u01b5\u01b7\58\35\2\u01b6\u01b4\3\2\2\2\u01b6\u01b5\3\2\2\2\u01b7\u01c0"+
		"\3\2\2\2\u01b8\u01bf\5\64\33\2\u01b9\u01bf\5<\37\2\u01ba\u01bf\58\35\2"+
		"\u01bb\u01bf\5> \2\u01bc\u01bf\5B\"\2\u01bd\u01bf\5F$\2\u01be\u01b8\3"+
		"\2\2\2\u01be\u01b9\3\2\2\2\u01be\u01ba\3\2\2\2\u01be\u01bb\3\2\2\2\u01be"+
		"\u01bc\3\2\2\2\u01be\u01bd\3\2\2\2\u01bf\u01c2\3\2\2\2\u01c0\u01be\3\2"+
		"\2\2\u01c0\u01c1\3\2\2\2\u01c1\u01c3\3\2\2\2\u01c2\u01c0\3\2\2\2\u01c3"+
		"\u01c4\7D\2\2\u01c4\u01c5\5\60\31\2\u01c5\63\3\2\2\2\u01c6\u01c7\7>\2"+
		"\2\u01c7\u01cc\5\66\34\2\u01c8\u01c9\7\26\2\2\u01c9\u01cb\5\66\34\2\u01ca"+
		"\u01c8\3\2\2\2\u01cb\u01ce\3\2\2\2\u01cc\u01ca\3\2\2\2\u01cc\u01cd\3\2"+
		"\2\2\u01cd\65\3\2\2\2\u01ce\u01cc\3\2\2\2\u01cf\u01d2\5\u008eH\2\u01d0"+
		"\u01d1\7G\2\2\u01d1\u01d3\5\u00b4[\2\u01d2\u01d0\3\2\2\2\u01d2\u01d3\3"+
		"\2\2\2\u01d3\u01d6\3\2\2\2\u01d4\u01d5\7I\2\2\u01d5\u01d7\7J\2\2\u01d6"+
		"\u01d4\3\2\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01da\3\2\2\2\u01d8\u01d9\7H"+
		"\2\2\u01d9\u01db\5\u008eH\2\u01da\u01d8\3\2\2\2\u01da\u01db\3\2\2\2\u01db"+
		"\u01dc\3\2\2\2\u01dc\u01dd\7F\2\2\u01dd\u01de\5\60\31\2\u01de\67\3\2\2"+
		"\2\u01df\u01e0\7?\2\2\u01e0\u01e5\5:\36\2\u01e1\u01e2\7\26\2\2\u01e2\u01e4"+
		"\5:\36\2\u01e3\u01e1\3\2\2\2\u01e4\u01e7\3\2\2\2\u01e5\u01e3\3\2\2\2\u01e5"+
		"\u01e6\3\2\2\2\u01e69\3\2\2\2\u01e7\u01e5\3\2\2\2\u01e8\u01eb\5\u008e"+
		"H\2\u01e9\u01ea\7G\2\2\u01ea\u01ec\5\u00b4[\2\u01eb\u01e9\3\2\2\2\u01eb"+
		"\u01ec\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed\u01ee\7\27\2\2\u01ee\u01ef\5"+
		"\60\31\2\u01ef;\3\2\2\2\u01f0\u01f1\7@\2\2\u01f1\u01f2\5\60\31\2\u01f2"+
		"=\3\2\2\2\u01f3\u01f4\7A\2\2\u01f4\u01f5\7B\2\2\u01f5\u01fa\5@!\2\u01f6"+
		"\u01f7\7\26\2\2\u01f7\u01f9\5@!\2\u01f8\u01f6\3\2\2\2\u01f9\u01fc\3\2"+
		"\2\2\u01fa\u01f8\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb?\3\2\2\2\u01fc\u01fa"+
		"\3\2\2\2\u01fd\u0204\5\u008eH\2\u01fe\u01ff\7G\2\2\u01ff\u0201\5\u00b4"+
		"[\2\u0200\u01fe\3\2\2\2\u0200\u0201\3\2\2\2\u0201\u0202\3\2\2\2\u0202"+
		"\u0203\7\27\2\2\u0203\u0205\5\60\31\2\u0204\u0200\3\2\2\2\u0204\u0205"+
		"\3\2\2\2\u0205\u0208\3\2\2\2\u0206\u0207\7R\2\2\u0207\u0209\5\u00c6d\2"+
		"\u0208\u0206\3\2\2\2\u0208\u0209\3\2\2\2\u0209A\3\2\2\2\u020a\u020b\7"+
		"C\2\2\u020b\u0210\7B\2\2\u020c\u020d\7L\2\2\u020d\u020e\7C\2\2\u020e\u0210"+
		"\7B\2\2\u020f\u020a\3\2\2\2\u020f\u020c\3\2\2\2\u0210\u0211\3\2\2\2\u0211"+
		"\u0216\5D#\2\u0212\u0213\7\26\2\2\u0213\u0215\5D#\2\u0214\u0212\3\2\2"+
		"\2\u0215\u0218\3\2\2\2\u0216\u0214\3\2\2\2\u0216\u0217\3\2\2\2\u0217C"+
		"\3\2\2\2\u0218\u0216\3\2\2\2\u0219\u021c\5\60\31\2\u021a\u021d\7M\2\2"+
		"\u021b\u021d\7N\2\2\u021c\u021a\3\2\2\2\u021c\u021b\3\2\2\2\u021c\u021d"+
		"\3\2\2\2\u021d\u0223\3\2\2\2\u021e\u0221\7J\2\2\u021f\u0222\7S\2\2\u0220"+
		"\u0222\7T\2\2\u0221\u021f\3\2\2\2\u0221\u0220\3\2\2\2\u0222\u0224\3\2"+
		"\2\2\u0223\u021e\3\2\2\2\u0223\u0224\3\2\2\2\u0224\u0227\3\2\2\2\u0225"+
		"\u0226\7R\2\2\u0226\u0228\5\u00c6d\2\u0227\u0225\3\2\2\2\u0227\u0228\3"+
		"\2\2\2\u0228E\3\2\2\2\u0229\u022a\7K\2\2\u022a\u022b\5\u008eH\2\u022b"+
		"G\3\2\2\2\u022c\u022f\7O\2\2\u022d\u022f\7P\2\2\u022e\u022c\3\2\2\2\u022e"+
		"\u022d\3\2\2\2\u022f\u0230\3\2\2\2\u0230\u0235\5J&\2\u0231\u0232\7\26"+
		"\2\2\u0232\u0234\5J&\2\u0233\u0231\3\2\2\2\u0234\u0237\3\2\2\2\u0235\u0233"+
		"\3\2\2\2\u0235\u0236\3\2\2\2\u0236\u0238\3\2\2\2\u0237\u0235\3\2\2\2\u0238"+
		"\u0239\7Q\2\2\u0239\u023a\5\60\31\2\u023aI\3\2\2\2\u023b\u023e\5\u008e"+
		"H\2\u023c\u023d\7G\2\2\u023d\u023f\5\u00b4[\2\u023e\u023c\3\2\2\2\u023e"+
		"\u023f\3\2\2\2\u023f\u0240\3\2\2\2\u0240\u0241\7F\2\2\u0241\u0242\5\60"+
		"\31\2\u0242K\3\2\2\2\u0243\u0244\7U\2\2\u0244\u0245\7\32\2\2\u0245\u0246"+
		"\5.\30\2\u0246\u0248\7\33\2\2\u0247\u0249\5N(\2\u0248\u0247\3\2\2\2\u0249"+
		"\u024a\3\2\2\2\u024a\u0248\3\2\2\2\u024a\u024b\3\2\2\2\u024b\u024c\3\2"+
		"\2\2\u024c\u024d\7Y\2\2\u024d\u024e\7D\2\2\u024e\u024f\5\60\31\2\u024f"+
		"M\3\2\2\2\u0250\u0251\7V\2\2\u0251\u0253\5\60\31\2\u0252\u0250\3\2\2\2"+
		"\u0253\u0254\3\2\2\2\u0254\u0252\3\2\2\2\u0254\u0255\3\2\2\2\u0255\u0256"+
		"\3\2\2\2\u0256\u0257\7D\2\2\u0257\u0258\5\60\31\2\u0258O\3\2\2\2\u0259"+
		"\u025a\7\\\2\2\u025a\u025b\7\32\2\2\u025b\u025c\5.\30\2\u025c\u025e\7"+
		"\33\2\2\u025d\u025f\5R*\2\u025e\u025d\3\2\2\2\u025f\u0260\3\2\2\2\u0260"+
		"\u025e\3\2\2\2\u0260\u0261\3\2\2\2\u0261\u0262\3\2\2\2\u0262\u0264\7Y"+
		"\2\2\u0263\u0265\5\u008eH\2\u0264\u0263\3\2\2\2\u0264\u0265\3\2\2\2\u0265"+
		"\u0266\3\2\2\2\u0266\u0267\7D\2\2\u0267\u0268\5\60\31\2\u0268Q\3\2\2\2"+
		"\u0269\u026d\7V\2\2\u026a\u026b\5\u008eH\2\u026b\u026c\7G\2\2\u026c\u026e"+
		"\3\2\2\2\u026d\u026a\3\2\2\2\u026d\u026e\3\2\2\2\u026e\u026f\3\2\2\2\u026f"+
		"\u0274\5\u00b4[\2\u0270\u0271\7#\2\2\u0271\u0273\5\u00b4[\2\u0272\u0270"+
		"\3\2\2\2\u0273\u0276\3\2\2\2\u0274\u0272\3\2\2\2\u0274\u0275\3\2\2\2\u0275"+
		"\u0277\3\2\2\2\u0276\u0274\3\2\2\2\u0277\u0278\7D\2\2\u0278\u0279\5\60"+
		"\31\2\u0279S\3\2\2\2\u027a\u027b\7E\2\2\u027b\u027c\7\32\2\2\u027c\u027d"+
		"\5.\30\2\u027d\u027e\7\33\2\2\u027e\u027f\7Z\2\2\u027f\u0280\5\60\31\2"+
		"\u0280\u0281\7[\2\2\u0281\u0282\5\60\31\2\u0282U\3\2\2\2\u0283\u0284\7"+
		"W\2\2\u0284\u0285\7\34\2\2\u0285\u0286\5.\30\2\u0286\u0288\7\35\2\2\u0287"+
		"\u0289\5X-\2\u0288\u0287\3\2\2\2\u0289\u028a\3\2\2\2\u028a\u0288\3\2\2"+
		"\2\u028a\u028b\3\2\2\2\u028bW\3\2\2\2\u028c\u028f\7X\2\2\u028d\u0290\7"+
		"$\2\2\u028e\u0290\5\32\16\2\u028f\u028d\3\2\2\2\u028f\u028e\3\2\2\2\u0290"+
		"\u0298\3\2\2\2\u0291\u0294\7#\2\2\u0292\u0295\7$\2\2\u0293\u0295\5\32"+
		"\16\2\u0294\u0292\3\2\2\2\u0294\u0293\3\2\2\2\u0295\u0297\3\2\2\2\u0296"+
		"\u0291\3\2\2\2\u0297\u029a\3\2\2\2\u0298\u0296\3\2\2\2\u0298\u0299\3\2"+
		"\2\2\u0299\u029b\3\2\2\2\u029a\u0298\3\2\2\2\u029b\u029c\7\34\2\2\u029c"+
		"\u029d\5.\30\2\u029d\u029e\7\35\2\2\u029eY\3\2\2\2\u029f\u02a4\5\\/\2"+
		"\u02a0\u02a1\7]\2\2\u02a1\u02a3\5\\/\2\u02a2\u02a0\3\2\2\2\u02a3\u02a6"+
		"\3\2\2\2\u02a4\u02a2\3\2\2\2\u02a4\u02a5\3\2\2\2\u02a5[\3\2\2\2\u02a6"+
		"\u02a4\3\2\2\2\u02a7\u02ac\5^\60\2\u02a8\u02a9\7^\2\2\u02a9\u02ab\5^\60"+
		"\2\u02aa\u02a8\3\2\2\2\u02ab\u02ae\3\2\2\2\u02ac\u02aa\3\2\2\2\u02ac\u02ad"+
		"\3\2\2\2\u02ad]\3\2\2\2\u02ae\u02ac\3\2\2\2\u02af\u02b1\7_\2\2\u02b0\u02af"+
		"\3\2\2\2\u02b0\u02b1\3\2\2\2\u02b1\u02b2\3\2\2\2\u02b2\u02b3\5`\61\2\u02b3"+
		"_\3\2\2\2\u02b4\u02b7\5b\62\2\u02b5\u02b6\t\5\2\2\u02b6\u02b8\5b\62\2"+
		"\u02b7\u02b5\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8a\3\2\2\2\u02b9\u02be\5"+
		"d\63\2\u02ba\u02bb\7\60\2\2\u02bb\u02bd\5d\63\2\u02bc\u02ba\3\2\2\2\u02bd"+
		"\u02c0\3\2\2\2\u02be\u02bc\3\2\2\2\u02be\u02bf\3\2\2\2\u02bfc\3\2\2\2"+
		"\u02c0\u02be\3\2\2\2\u02c1\u02c4\5f\64\2\u02c2\u02c3\7`\2\2\u02c3\u02c5"+
		"\5f\64\2\u02c4\u02c2\3\2\2\2\u02c4\u02c5\3\2\2\2\u02c5e\3\2\2\2\u02c6"+
		"\u02cb\5h\65\2\u02c7\u02c8\t\6\2\2\u02c8\u02ca\5h\65\2\u02c9\u02c7\3\2"+
		"\2\2\u02ca\u02cd\3\2\2\2\u02cb\u02c9\3\2\2\2\u02cb\u02cc\3\2\2\2\u02cc"+
		"g\3\2\2\2\u02cd\u02cb\3\2\2\2\u02ce\u02d3\5j\66\2\u02cf\u02d0\t\7\2\2"+
		"\u02d0\u02d2\5j\66\2\u02d1\u02cf\3\2\2\2\u02d2\u02d5\3\2\2\2\u02d3\u02d1"+
		"\3\2\2\2\u02d3\u02d4\3\2\2\2\u02d4i\3\2\2\2\u02d5\u02d3\3\2\2\2\u02d6"+
		"\u02da\5l\67\2\u02d7\u02d8\7a\2\2\u02d8\u02d9\7b\2\2\u02d9\u02db\5\u00b4"+
		"[\2\u02da\u02d7\3\2\2\2\u02da\u02db\3\2\2\2\u02dbk\3\2\2\2\u02dc\u02e0"+
		"\5n8\2\u02dd\u02de\7d\2\2\u02de\u02df\7c\2\2\u02df\u02e1\5\u00b4[\2\u02e0"+
		"\u02dd\3\2\2\2\u02e0\u02e1\3\2\2\2\u02e1m\3\2\2\2\u02e2\u02e6\5p9\2\u02e3"+
		"\u02e4\7e\2\2\u02e4\u02e5\7G\2\2\u02e5\u02e7\5\u00b4[\2\u02e6\u02e3\3"+
		"\2\2\2\u02e6\u02e7\3\2\2\2\u02e7o\3\2\2\2\u02e8\u02ec\5r:\2\u02e9\u02ea"+
		"\7g\2\2\u02ea\u02eb\7G\2\2\u02eb\u02ed\5\u00c0a\2\u02ec\u02e9\3\2\2\2"+
		"\u02ec\u02ed\3\2\2\2\u02edq\3\2\2\2\u02ee\u02f2\5t;\2\u02ef\u02f0\7f\2"+
		"\2\u02f0\u02f1\7G\2\2\u02f1\u02f3\5\u00c0a\2\u02f2\u02ef\3\2\2\2\u02f2"+
		"\u02f3\3\2\2\2\u02f3s\3\2\2\2\u02f4\u02fd\5x=\2\u02f5\u02f6\7\6\2\2\u02f6"+
		"\u02f7\7.\2\2\u02f7\u02f8\3\2\2\2\u02f8\u02f9\5v<\2\u02f9\u02fa\5\u009a"+
		"N\2\u02fa\u02fc\3\2\2\2\u02fb\u02f5\3\2\2\2\u02fc\u02ff\3\2\2\2\u02fd"+
		"\u02fb\3\2\2\2\u02fd\u02fe\3\2\2\2\u02feu\3\2\2\2\u02ff\u02fd\3\2\2\2"+
		"\u0300\u0304\5\32\16\2\u0301\u0304\5\u008eH\2\u0302\u0304\5\u0090I\2\u0303"+
		"\u0300\3\2\2\2\u0303\u0301\3\2\2\2\u0303\u0302\3\2\2\2\u0304w\3\2\2\2"+
		"\u0305\u0307\t\6\2\2\u0306\u0305\3\2\2\2\u0307\u030a\3\2\2\2\u0308\u0306"+
		"\3\2\2\2\u0308\u0309\3\2\2\2\u0309\u030b\3\2\2\2\u030a\u0308\3\2\2\2\u030b"+
		"\u030c\5z>\2\u030cy\3\2\2\2\u030d\u0311\5\u0080A\2\u030e\u0311\5|?\2\u030f"+
		"\u0311\5~@\2\u0310\u030d\3\2\2\2\u0310\u030e\3\2\2\2\u0310\u030f\3\2\2"+
		"\2\u0311{\3\2\2\2\u0312\u0313\7n\2\2\u0313\u0314\7m\2\2\u0314\u0315\5"+
		"\u00b4[\2\u0315\u0316\7\34\2\2\u0316\u0317\5.\30\2\u0317\u0318\7\35\2"+
		"\2\u0318}\3\2\2\2\u0319\u031a\7o\2\2\u031a\u031b\7m\2\2\u031b\u031c\5"+
		"\u00b4[\2\u031c\u031d\7\34\2\2\u031d\u031e\5.\30\2\u031e\u031f\7\35\2"+
		"\2\u031f\177\3\2\2\2\u0320\u0325\5\u0082B\2\u0321\u0322\7\66\2\2\u0322"+
		"\u0324\5\u0082B\2\u0323\u0321\3\2\2\2\u0324\u0327\3\2\2\2\u0325\u0323"+
		"\3\2\2\2\u0325\u0326\3\2\2\2\u0326\u0081\3\2\2\2\u0327\u0325\3\2\2\2\u0328"+
		"\u0330\5\u008cG\2\u0329\u032f\5\u0084C\2\u032a\u032f\5\u0088E\2\u032b"+
		"\u032f\5\u008aF\2\u032c\u032f\5\u0086D\2\u032d\u032f\5\u009aN\2\u032e"+
		"\u0329\3\2\2\2\u032e\u032a\3\2\2\2\u032e\u032b\3\2\2\2\u032e\u032c\3\2"+
		"\2\2\u032e\u032d\3\2\2\2\u032f\u0332\3\2\2\2\u0330\u032e\3\2\2\2\u0330"+
		"\u0331\3\2\2\2\u0331\u0083\3\2\2\2\u0332\u0330\3\2\2\2\u0333\u0334\7\67"+
		"\2\2\u0334\u0335\7\67\2\2\u0335\u0336\5.\30\2\u0336\u0337\78\2\2\u0337"+
		"\u0338\78\2\2\u0338\u0085\3\2\2\2\u0339\u033a\7\67\2\2\u033a\u033b\78"+
		"\2\2\u033b\u0087\3\2\2\2\u033c\u033d\7\67\2\2\u033d\u033e\5.\30\2\u033e"+
		"\u033f\78\2\2\u033f\u0089\3\2\2\2\u0340\u0347\79\2\2\u0341\u0348\5\u00ca"+
		"f\2\u0342\u0348\5\u00c8e\2\u0343\u0348\7\u0089\2\2\u0344\u0348\5\u0090"+
		"I\2\u0345\u0348\5\u008eH\2\u0346\u0348\5\u0092J\2\u0347\u0341\3\2\2\2"+
		"\u0347\u0342\3\2\2\2\u0347\u0343\3\2\2\2\u0347\u0344\3\2\2\2\u0347\u0345"+
		"\3\2\2\2\u0347\u0346\3\2\2\2\u0348\u008b\3\2\2\2\u0349\u0358\7\u0082\2"+
		"\2\u034a\u0358\7k\2\2\u034b\u0358\7l\2\2\u034c\u0358\7\u0083\2\2\u034d"+
		"\u0358\5\u00c8e\2\u034e\u0358\5\u008eH\2\u034f\u0358\5\u0090I\2\u0350"+
		"\u0358\5\u0092J\2\u0351\u0358\5\u00b6\\\2\u0352\u0358\5\u0098M\2\u0353"+
		"\u0358\5\u0094K\2\u0354\u0358\5\u0096L\2\u0355\u0358\5\u00c4c\2\u0356"+
		"\u0358\5\u009eP\2\u0357\u0349\3\2\2\2\u0357\u034a\3\2\2\2\u0357\u034b"+
		"\3\2\2\2\u0357\u034c\3\2\2\2\u0357\u034d\3\2\2\2\u0357\u034e\3\2\2\2\u0357"+
		"\u034f\3\2\2\2\u0357\u0350\3\2\2\2\u0357\u0351\3\2\2\2\u0357\u0352\3\2"+
		"\2\2\u0357\u0353\3\2\2\2\u0357\u0354\3\2\2\2\u0357\u0355\3\2\2\2\u0357"+
		"\u0356\3\2\2\2\u0358\u008d\3\2\2\2\u0359\u035a\7\"\2\2\u035a\u035b\5\32"+
		"\16\2\u035b\u008f\3\2\2\2\u035c\u035e\7\32\2\2\u035d\u035f\5.\30\2\u035e"+
		"\u035d\3\2\2\2\u035e\u035f\3\2\2\2\u035f\u0360\3\2\2\2\u0360\u0361\7\33"+
		"\2\2\u0361\u0091\3\2\2\2\u0362\u0363\7:\2\2\u0363\u0093\3\2\2\2\u0364"+
		"\u0365\7\b\2\2\u0365\u0366\7\34\2\2\u0366\u0367\5.\30\2\u0367\u0368\7"+
		"\35\2\2\u0368\u0095\3\2\2\2\u0369\u036a\7j\2\2\u036a\u036b\7\34\2\2\u036b"+
		"\u036c\5.\30\2\u036c\u036d\7\35\2\2\u036d\u0097\3\2\2\2\u036e\u036f\5"+
		"\32\16\2\u036f\u0370\5\u009aN\2\u0370\u0099\3\2\2\2\u0371\u0378\7\32\2"+
		"\2\u0372\u0374\5\u009cO\2\u0373\u0375\7\26\2\2\u0374\u0373\3\2\2\2\u0374"+
		"\u0375\3\2\2\2\u0375\u0377\3\2\2\2\u0376\u0372\3\2\2\2\u0377\u037a\3\2"+
		"\2\2\u0378\u0376\3\2\2\2\u0378\u0379\3\2\2\2\u0379\u037b\3\2\2\2\u037a"+
		"\u0378\3\2\2\2\u037b\u037c\7\33\2\2\u037c\u009b\3\2\2\2\u037d\u0380\5"+
		"\60\31\2\u037e\u0380\7\u0081\2\2\u037f\u037d\3\2\2\2\u037f\u037e\3\2\2"+
		"\2\u0380\u009d\3\2\2\2\u0381\u0384\5\u00a0Q\2\u0382\u0384\5\u00a2R\2\u0383"+
		"\u0381\3\2\2\2\u0383\u0382\3\2\2\2\u0384\u009f\3\2\2\2\u0385\u0386\5\32"+
		"\16\2\u0386\u0387\7;\2\2\u0387\u0388\7\u0083\2\2\u0388\u00a1\3\2\2\2\u0389"+
		"\u038a\7\31\2\2\u038a\u038c\7\32\2\2\u038b\u038d\5*\26\2\u038c\u038b\3"+
		"\2\2\2\u038c\u038d\3\2\2\2\u038d\u038e\3\2\2\2\u038e\u0391\7\33\2\2\u038f"+
		"\u0390\7G\2\2\u0390\u0392\5\u00b4[\2\u0391\u038f\3\2\2\2\u0391\u0392\3"+
		"\2\2\2\u0392\u0393\3\2\2\2\u0393\u0395\7\34\2\2\u0394\u0396\5.\30\2\u0395"+
		"\u0394\3\2\2\2\u0395\u0396\3\2\2\2\u0396\u0397\3\2\2\2\u0397\u0398\7\35"+
		"\2\2\u0398\u00a3\3\2\2\2\u0399\u039a\7t\2\2\u039a\u039b\7}\2\2\u039b\u039c"+
		"\5\60\31\2\u039c\u039d\7{\2\2\u039d\u03a1\5\60\31\2\u039e\u039f\7H\2\2"+
		"\u039f\u03a0\7\177\2\2\u03a0\u03a2\5\60\31\2\u03a1\u039e\3\2\2\2\u03a1"+
		"\u03a2\3\2\2\2\u03a2\u03b1\3\2\2\2\u03a3\u03a4\7t\2\2\u03a4\u03a5\7}\2"+
		"\2\u03a5\u03aa\5\u00c2b\2\u03a6\u03a7\7\26\2\2\u03a7\u03a9\5\u00c2b\2"+
		"\u03a8\u03a6\3\2\2\2\u03a9\u03ac\3\2\2\2\u03aa\u03a8\3\2\2\2\u03aa\u03ab"+
		"\3\2\2\2\u03ab\u03ad\3\2\2\2\u03ac\u03aa\3\2\2\2\u03ad\u03ae\7{\2\2\u03ae"+
		"\u03af\5\60\31\2\u03af\u03b1\3\2\2\2\u03b0\u0399\3\2\2\2\u03b0\u03a3\3"+
		"\2\2\2\u03b1\u00a5\3\2\2\2\u03b2\u03b3\7u\2\2\u03b3\u03b4\7}\2\2\u03b4"+
		"\u03b5\5\u00b0Y\2\u03b5\u00a7\3\2\2\2\u03b6\u03b7\7v\2\2\u03b7\u03b8\7"+
		"}\2\2\u03b8\u03b9\5\u00b0Y\2\u03b9\u03ba\7G\2\2\u03ba\u03bb\5\60\31\2"+
		"\u03bb\u00a9\3\2\2\2\u03bc\u03bd\7w\2\2\u03bd\u03be\7}\2\2\u03be\u03bf"+
		"\7|\2\2\u03bf\u03c0\7b\2\2\u03c0\u03c1\5\u00b0Y\2\u03c1\u03c2\7~\2\2\u03c2"+
		"\u03c3\5\60\31\2\u03c3\u00ab\3\2\2\2\u03c4\u03c5\7x\2\2\u03c5\u03c6\7"+
		"}\2\2\u03c6\u03cb\5\u00b2Z\2\u03c7\u03c8\7\26\2\2\u03c8\u03ca\5\u00b2"+
		"Z\2\u03c9\u03c7\3\2\2\2\u03ca\u03cd\3\2\2\2\u03cb\u03c9\3\2\2\2\u03cb"+
		"\u03cc\3\2\2\2\u03cc\u03ce\3\2\2\2\u03cd\u03cb\3\2\2\2\u03ce\u03cf\7y"+
		"\2\2\u03cf\u03d0\5\60\31\2\u03d0\u03d1\7D\2\2\u03d1\u03d2\5\60\31\2\u03d2"+
		"\u00ad\3\2\2\2\u03d3\u03d4\7z\2\2\u03d4\u03d5\7}\2\2\u03d5\u03d6\5\60"+
		"\31\2\u03d6\u03d7\7{\2\2\u03d7\u03d8\5\60\31\2\u03d8\u00af\3\2\2\2\u03d9"+
		"\u03dc\5\u008cG\2\u03da\u03dd\5\u0084C\2\u03db\u03dd\5\u008aF\2\u03dc"+
		"\u03da\3\2\2\2\u03dc\u03db\3\2\2\2\u03dd\u03de\3\2\2\2\u03de\u03dc\3\2"+
		"\2\2\u03de\u03df\3\2\2\2\u03df\u00b1\3\2\2\2\u03e0\u03e1\5\u008eH\2\u03e1"+
		"\u03e2\7\27\2\2\u03e2\u03e3\5\60\31\2\u03e3\u00b3\3\2\2\2\u03e4\u03e5"+
		"\7\32\2\2\u03e5\u03ed\7\33\2\2\u03e6\u03ea\5\u00b8]\2\u03e7\u03eb\7\u0081"+
		"\2\2\u03e8\u03eb\7$\2\2\u03e9\u03eb\7\61\2\2\u03ea\u03e7\3\2\2\2\u03ea"+
		"\u03e8\3\2\2\2\u03ea\u03e9\3\2\2\2\u03ea\u03eb\3\2\2\2\u03eb\u03ed\3\2"+
		"\2\2\u03ec\u03e4\3\2\2\2\u03ec\u03e6\3\2\2\2\u03ed\u00b5\3\2\2\2\u03ee"+
		"\u03f7\7\34\2\2\u03ef\u03f4\5\u00c2b\2\u03f0\u03f1\7\26\2\2\u03f1\u03f3"+
		"\5\u00c2b\2\u03f2\u03f0\3\2\2\2\u03f3\u03f6\3\2\2\2\u03f4\u03f2\3\2\2"+
		"\2\u03f4\u03f5\3\2\2\2\u03f5\u03f8\3\2\2\2\u03f6\u03f4\3\2\2\2\u03f7\u03ef"+
		"\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8\u03f9\3\2\2\2\u03f9\u03ff\7\35\2\2"+
		"\u03fa\u03fb\7<\2\2\u03fb\u03fc\5.\30\2\u03fc\u03fd\7=\2\2\u03fd\u03ff"+
		"\3\2\2\2\u03fe\u03ee\3\2\2\2\u03fe\u03fa\3\2\2\2\u03ff\u00b7\3\2\2\2\u0400"+
		"\u0404\5\32\16\2\u0401\u0404\7\u0082\2\2\u0402\u0404\5\u00ba^\2\u0403"+
		"\u0400\3\2\2\2\u0403\u0401\3\2\2\2\u0403\u0402\3\2\2\2\u0404\u00b9\3\2"+
		"\2\2\u0405\u0408\5\u00bc_\2\u0406\u0408\5\u00be`\2\u0407\u0405\3\2\2\2"+
		"\u0407\u0406\3\2\2\2\u0408\u00bb\3\2\2\2\u0409\u040a\7\31\2\2\u040a\u040b"+
		"\7\32\2\2\u040b\u040c\7$\2\2\u040c\u040d\7\33\2\2\u040d\u00bd\3\2\2\2"+
		"\u040e\u040f\7\31\2\2\u040f\u0418\7\32\2\2\u0410\u0415\5\u00b4[\2\u0411"+
		"\u0412\7\26\2\2\u0412\u0414\5\u00b4[\2\u0413\u0411\3\2\2\2\u0414\u0417"+
		"\3\2\2\2\u0415\u0413\3\2\2\2\u0415\u0416\3\2\2\2\u0416\u0419\3\2\2\2\u0417"+
		"\u0415\3\2\2\2\u0418\u0410\3\2\2\2\u0418\u0419\3\2\2\2\u0419\u041a\3\2"+
		"\2\2\u041a\u041b\7\33\2\2\u041b\u041c\7G\2\2\u041c\u041d\5\u00b4[\2\u041d"+
		"\u00bf\3\2\2\2\u041e\u0420\5\u00b8]\2\u041f\u0421\7\u0081\2\2\u0420\u041f"+
		"\3\2\2\2\u0420\u0421\3\2\2\2\u0421\u00c1\3\2\2\2\u0422\u0425\5\60\31\2"+
		"\u0423\u0425\7\u0089\2\2\u0424\u0422\3\2\2\2\u0424\u0423\3\2\2\2\u0425"+
		"\u0426\3\2\2\2\u0426\u0427\t\b\2\2\u0427\u0428\5\60\31\2\u0428\u00c3\3"+
		"\2\2\2\u0429\u042b\7\67\2\2\u042a\u042c\5.\30\2\u042b\u042a\3\2\2\2\u042b"+
		"\u042c\3\2\2\2\u042c\u042d\3\2\2\2\u042d\u042e\78\2\2\u042e\u00c5\3\2"+
		"\2\2\u042f\u0430\5\u00c8e\2\u0430\u00c7\3\2\2\2\u0431\u0432\7\u0080\2"+
		"\2\u0432\u00c9\3\2\2\2\u0433\u0434\t\t\2\2\u0434\u00cb\3\2\2\2q\u00d4"+
		"\u00d8\u00e8\u00ee\u00f6\u00fd\u0109\u011f\u0127\u012c\u012f\u0133\u013c"+
		"\u0145\u0148\u014f\u0156\u0158\u015f\u0166\u0168\u016f\u0174\u0178\u017c"+
		"\u0183\u018d\u0194\u019b\u01a2\u01b2\u01b6\u01be\u01c0\u01cc\u01d2\u01d6"+
		"\u01da\u01e5\u01eb\u01fa\u0200\u0204\u0208\u020f\u0216\u021c\u0221\u0223"+
		"\u0227\u022e\u0235\u023e\u024a\u0254\u0260\u0264\u026d\u0274\u028a\u028f"+
		"\u0294\u0298\u02a4\u02ac\u02b0\u02b7\u02be\u02c4\u02cb\u02d3\u02da\u02e0"+
		"\u02e6\u02ec\u02f2\u02fd\u0303\u0308\u0310\u0325\u032e\u0330\u0347\u0357"+
		"\u035e\u0374\u0378\u037f\u0383\u038c\u0391\u0395\u03a1\u03aa\u03b0\u03cb"+
		"\u03dc\u03de\u03ea\u03ec\u03f4\u03f7\u03fe\u0403\u0407\u0415\u0418\u0420"+
		"\u0424\u042b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}