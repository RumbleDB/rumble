// Generated from ./src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.13.1

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
			setState(254);
			module();
			setState(255);
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
			setState(262);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
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
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			setState(268);
			prolog();
			setState(269);
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
			setState(288);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(282);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(296);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
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
			setState(299);
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
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_statements);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatementsAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsAndExprContext statementsAndExpr() throws RecognitionException {
		StatementsAndExprContext _localctx = new StatementsAndExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStatementsAndOptionalExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsAndOptionalExprContext statementsAndOptionalExpr() throws RecognitionException {
		StatementsAndOptionalExprContext _localctx = new StatementsAndOptionalExprContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_statementsAndOptionalExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			statements();
			setState(312);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1566408243247242592L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 16831L) != 0)) {
				{
				setState(311);
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
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_statement);
		try {
			setState(327);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitApplyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ApplyStatementContext applyStatement() throws RecognitionException {
		ApplyStatementContext _localctx = new ApplyStatementContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAssignStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignStatementContext assignStatement() throws RecognitionException {
		AssignStatementContext _localctx = new AssignStatementContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExitStatementContext exitStatement() throws RecognitionException {
		ExitStatementContext _localctx = new ExitStatementContext(_ctx, getState());
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
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
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
		FlowrStatementContext _localctx = new FlowrStatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_flowrStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & 24623L) != 0)) {
				{
				setState(365);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(370);
			match(Kreturn);
			setState(371);
			((FlowrStatementContext)_localctx).returnStmt = statement();
			}
		}
		catch (RecognitionException re) {
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
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
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
		SwitchStatementContext _localctx = new SwitchStatementContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(386);
				((SwitchStatementContext)_localctx).switchCaseStatement = switchCaseStatement();
				((SwitchStatementContext)_localctx).cases.add(((SwitchStatementContext)_localctx).switchCaseStatement);
				}
				}
				setState(389); 
				_errHandler.sync(this);
				_la = _input.LA(1);
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
		SwitchCaseStatementContext _localctx = new SwitchCaseStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_switchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(397); 
			_errHandler.sync(this);
			_la = _input.LA(1);
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(401);
			match(Kreturn);
			setState(402);
			((SwitchCaseStatementContext)_localctx).ret = statement();
			}
		}
		catch (RecognitionException re) {
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
		TryCatchStatementContext _localctx = new TryCatchStatementContext(_ctx, getState());
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
			_errHandler.sync(this);
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
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
		CatchCaseStatementContext _localctx = new CatchCaseStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_catchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(Kcatch);
			setState(414);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(416);
				match(T__11);
				setState(419);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(426);
			((CatchCaseStatementContext)_localctx).catch_block = blockStatement();
			}
		}
		catch (RecognitionException re) {
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
		TypeSwitchStatementContext _localctx = new TypeSwitchStatementContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(432);
				((TypeSwitchStatementContext)_localctx).caseStatement = caseStatement();
				((TypeSwitchStatementContext)_localctx).cases.add(((TypeSwitchStatementContext)_localctx).caseStatement);
				}
				}
				setState(435); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(437);
			match(Kdefault);
			setState(439);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		CaseStatementContext _localctx = new CaseStatementContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_caseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(444);
			match(Kcase);
			setState(448);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(458);
			match(Kreturn);
			setState(459);
			((CaseStatementContext)_localctx).ret = statement();
			}
		}
		catch (RecognitionException re) {
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
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(463);
				match(T__8);
				setState(464);
				match(Literal);
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
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
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(472);
				match(T__9);
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
		AnnotationsContext _localctx = new AnnotationsContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_annotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(475);
				annotation();
				}
				}
				setState(480);
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
		VarDeclStatementContext _localctx = new VarDeclStatementContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(491);
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
		VarDeclForStatementContext _localctx = new VarDeclForStatementContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_varDeclForStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			((VarDeclForStatementContext)_localctx).var_ref = varRef();
			setState(496);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(494);
				match(Kas);
				setState(495);
				sequenceType();
				}
			}

			setState(500);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSetter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetterContext setter() throws RecognitionException {
		SetterContext _localctx = new SetterContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_setter);
		try {
			setState(512);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNamespaceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclContext namespaceDecl() throws RecognitionException {
		NamespaceDeclContext _localctx = new NamespaceDeclContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnnotatedDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotatedDeclContext annotatedDecl() throws RecognitionException {
		AnnotatedDeclContext _localctx = new AnnotatedDeclContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_annotatedDecl);
		try {
			setState(524);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDefaultCollationDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefaultCollationDeclContext defaultCollationDecl() throws RecognitionException {
		DefaultCollationDeclContext _localctx = new DefaultCollationDeclContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderingModeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderingModeDeclContext orderingModeDecl() throws RecognitionException {
		OrderingModeDeclContext _localctx = new OrderingModeDeclContext(_ctx, getState());
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
			_la = _input.LA(1);
			if ( !(_la==T__15 || _la==Kunordered) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitEmptyOrderDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyOrderDeclContext emptyOrderDecl() throws RecognitionException {
		EmptyOrderDeclContext _localctx = new EmptyOrderDeclContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDecimalFormatDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalFormatDeclContext decimalFormatDecl() throws RecognitionException {
		DecimalFormatDeclContext _localctx = new DecimalFormatDeclContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(541);
			match(Kdeclare);
			setState(546);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 536346624L) != 0)) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QnameContext qname() throws RecognitionException {
		QnameContext _localctx = new QnameContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(562);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(559);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDfPropertyName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DfPropertyNameContext dfPropertyName() throws RecognitionException {
		DfPropertyNameContext _localctx = new DfPropertyNameContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 536346624L) != 0)) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitModuleImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleImportContext moduleImport() throws RecognitionException {
		ModuleImportContext _localctx = new ModuleImportContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(578);
				match(Kat);
				setState(579);
				uriLiteral();
				setState(584);
				_errHandler.sync(this);
				_la = _input.LA(1);
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
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(593);
				match(Kas);
				setState(594);
				sequenceType();
				}
			}

			setState(604);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
				_errHandler.sync(this);
				_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContextItemDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemDeclContext contextItemDecl() throws RecognitionException {
		ContextItemDeclContext _localctx = new ContextItemDeclContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(609);
				match(Kas);
				setState(610);
				sequenceType();
				}
			}

			setState(620);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
				_errHandler.sync(this);
				_la = _input.LA(1);
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
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(627);
				paramList();
				}
			}

			setState(630);
			match(T__9);
			setState(633);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(631);
				match(Kas);
				setState(632);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(640);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclContext typeDecl() throws RecognitionException {
		TypeDeclContext _localctx = new TypeDeclContext(_ctx, getState());
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
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSchemaLanguage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaLanguageContext schemaLanguage() throws RecognitionException {
		SchemaLanguageContext _localctx = new SchemaLanguageContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_schemaLanguage);
		try {
			setState(657);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(659);
			param();
			setState(664);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(673);
			exprSingle();
			setState(678);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExprSingle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSingleContext exprSingle() throws RecognitionException {
		ExprSingleContext _localctx = new ExprSingleContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_exprSingle);
		try {
			setState(687);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitExprSimple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprSimpleContext exprSimple() throws RecognitionException {
		ExprSimpleContext _localctx = new ExprSimpleContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_exprSimple);
		try {
			setState(697);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
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
		enterRule(_localctx, 98, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(701);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & 24623L) != 0)) {
				{
				setState(709);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(714);
			match(Kreturn);
			setState(715);
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitForVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForVarContext forVar() throws RecognitionException {
		ForVarContext _localctx = new ForVarContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(726);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(729);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(727);
				match(Kas);
				setState(728);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(733);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(731);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(732);
				match(Kempty);
				}
			}

			setState(737);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitLetClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetClauseContext letClause() throws RecognitionException {
		LetClauseContext _localctx = new LetClauseContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitLetVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetVarContext letVar() throws RecognitionException {
		LetVarContext _localctx = new LetVarContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(751);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(754);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitGroupByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByClauseContext groupByClause() throws RecognitionException {
		GroupByClauseContext _localctx = new GroupByClauseContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitGroupByVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByVarContext groupByVar() throws RecognitionException {
		GroupByVarContext _localctx = new GroupByVarContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(772);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(779);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5 || _la==Kas) {
				{
				setState(775);
				_errHandler.sync(this);
				_la = _input.LA(1);
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(790);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderByExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByExprContext orderByExpr() throws RecognitionException {
		OrderByExprContext _localctx = new OrderByExprContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(803);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(805);
				match(Kempty);
				setState(808);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCountClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CountClauseContext countClause() throws RecognitionException {
		CountClauseContext _localctx = new CountClauseContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQuantifiedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedExprContext quantifiedExpr() throws RecognitionException {
		QuantifiedExprContext _localctx = new QuantifiedExprContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(821);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(831);
			match(Ksatisfies);
			setState(832);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitQuantifiedExprVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantifiedExprVarContext quantifiedExprVar() throws RecognitionException {
		QuantifiedExprVarContext _localctx = new QuantifiedExprVarContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(834);
			varRef();
			setState(837);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchExprContext switchExpr() throws RecognitionException {
		SwitchExprContext _localctx = new SwitchExprContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(846);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(849); 
				_errHandler.sync(this);
				_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSwitchCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseClauseContext switchCaseClause() throws RecognitionException {
		SwitchCaseClauseContext _localctx = new SwitchCaseClauseContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(857); 
			_errHandler.sync(this);
			_la = _input.LA(1);
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(861);
			match(Kreturn);
			setState(862);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypeSwitchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSwitchExprContext typeSwitchExpr() throws RecognitionException {
		TypeSwitchExprContext _localctx = new TypeSwitchExprContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(868);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(871); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(873);
			match(Kdefault);
			setState(875);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseClauseContext caseClause() throws RecognitionException {
		CaseClauseContext _localctx = new CaseClauseContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(880);
			match(Kcase);
			setState(884);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(894);
			match(Kreturn);
			setState(895);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfExprContext ifExpr() throws RecognitionException {
		IfExprContext _localctx = new IfExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTryCatchExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryCatchExprContext tryCatchExpr() throws RecognitionException {
		TryCatchExprContext _localctx = new TryCatchExprContext(_ctx, getState());
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
			_errHandler.sync(this);
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,81,_ctx);
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
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(915);
			match(Kcatch);
			setState(918);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(920);
				match(T__11);
				setState(923);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
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
				_errHandler.sync(this);
				_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrExprContext orExpr() throws RecognitionException {
		OrExprContext _localctx = new OrExprContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(934);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(939);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,85,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(942);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(947);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNotExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotExprContext notExpr() throws RecognitionException {
		NotExprContext _localctx = new NotExprContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(951);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
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
		ComparisonExprContext _localctx = new ComparisonExprContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(955);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(958);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 140668768878608L) != 0)) {
				{
				setState(956);
				((ComparisonExprContext)_localctx)._tset1828 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 140668768878608L) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1828 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStringConcatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConcatExprContext stringConcatExpr() throws RecognitionException {
		StringConcatExprContext _localctx = new StringConcatExprContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(960);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(965);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitRangeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeExprContext rangeExpr() throws RecognitionException {
		RangeExprContext _localctx = new RangeExprContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(968);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(971);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
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
		AdditiveExprContext _localctx = new AdditiveExprContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(973);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(978);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(974);
					((AdditiveExprContext)_localctx)._tset1937 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__47 || _la==T__48) ) {
						((AdditiveExprContext)_localctx)._tset1937 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		MultiplicativeExprContext _localctx = new MultiplicativeExprContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(981);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(986);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7881299347900416L) != 0)) {
				{
				{
				setState(982);
				((MultiplicativeExprContext)_localctx)._tset1965 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 7881299347900416L) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1965 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1965);
				setState(983);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(988);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInstanceOfExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstanceOfExprContext instanceOfExpr() throws RecognitionException {
		InstanceOfExprContext _localctx = new InstanceOfExprContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(989);
			((InstanceOfExprContext)_localctx).main_expr = isStaticallyExpr();
			setState(993);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitIsStaticallyExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsStaticallyExprContext isStaticallyExpr() throws RecognitionException {
		IsStaticallyExprContext _localctx = new IsStaticallyExprContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_isStaticallyExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(995);
			((IsStaticallyExprContext)_localctx).main_expr = treatExpr();
			setState(999);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTreatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TreatExprContext treatExpr() throws RecognitionException {
		TreatExprContext _localctx = new TreatExprContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1001);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(1005);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCastableExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastableExprContext castableExpr() throws RecognitionException {
		CastableExprContext _localctx = new CastableExprContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1007);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(1011);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCastExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastExprContext castExpr() throws RecognitionException {
		CastExprContext _localctx = new CastExprContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1013);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(1017);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrowExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowExprContext arrowExpr() throws RecognitionException {
		ArrowExprContext _localctx = new ArrowExprContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1019);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(1028);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrowFunctionSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowFunctionSpecifierContext arrowFunctionSpecifier() throws RecognitionException {
		ArrowFunctionSpecifierContext _localctx = new ArrowFunctionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_arrowFunctionSpecifier);
		try {
			setState(1034);
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
		UnaryExprContext _localctx = new UnaryExprContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1039);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__47 || _la==T__48) {
				{
				{
				setState(1036);
				((UnaryExprContext)_localctx)._tset2144 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__47 || _la==T__48) ) {
					((UnaryExprContext)_localctx)._tset2144 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset2144);
				}
				}
				setState(1041);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1042);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitValueExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueExprContext valueExpr() throws RecognitionException {
		ValueExprContext _localctx = new ValueExprContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_valueExpr);
		try {
			setState(1047);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitValidateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidateExprContext validateExpr() throws RecognitionException {
		ValidateExprContext _localctx = new ValidateExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnnotateExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotateExprContext annotateExpr() throws RecognitionException {
		AnnotateExprContext _localctx = new AnnotateExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitSimpleMapExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleMapExprContext simpleMapExpr() throws RecognitionException {
		SimpleMapExprContext _localctx = new SimpleMapExprContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1063);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(1068);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPostFixExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostFixExprContext postFixExpr() throws RecognitionException {
		PostFixExprContext _localctx = new PostFixExprContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1071);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(1079);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1077);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayLookupContext arrayLookup() throws RecognitionException {
		ArrayLookupContext _localctx = new ArrayLookupContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayUnboxing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayUnboxingContext arrayUnboxing() throws RecognitionException {
		ArrayUnboxingContext _localctx = new ArrayUnboxingContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitObjectLookup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectLookupContext objectLookup() throws RecognitionException {
		ObjectLookupContext _localctx = new ObjectLookupContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1095);
			match(T__55);
			setState(1102);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPrimaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExprContext primaryExpr() throws RecognitionException {
		PrimaryExprContext _localctx = new PrimaryExprContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_primaryExpr);
		try {
			setState(1119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitBlockExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockExprContext blockExpr() throws RecognitionException {
		BlockExprContext _localctx = new BlockExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarRefContext varRef() throws RecognitionException {
		VarRefContext _localctx = new VarRefContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitParenthesizedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenthesizedExprContext parenthesizedExpr() throws RecognitionException {
		ParenthesizedExprContext _localctx = new ParenthesizedExprContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1128);
			match(T__8);
			setState(1130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1566408243247242592L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 16831L) != 0)) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitContextItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextItemExprContext contextItemExpr() throws RecognitionException {
		ContextItemExprContext _localctx = new ContextItemExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitOrderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderedExprContext orderedExpr() throws RecognitionException {
		OrderedExprContext _localctx = new OrderedExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUnorderedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnorderedExprContext unorderedExpr() throws RecognitionException {
		UnorderedExprContext _localctx = new UnorderedExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			match(T__8);
			setState(1156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1566408243247242592L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 16895L) != 0)) {
				{
				{
				setState(1150);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(1152);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__13) {
					{
					setState(1151);
					match(T__13);
					}
				}

				}
				}
				setState(1158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1159);
			match(T__9);
			}
		}
		catch (RecognitionException re) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_argument);
		try {
			setState(1163);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionItemExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionItemExprContext functionItemExpr() throws RecognitionException {
		FunctionItemExprContext _localctx = new FunctionItemExprContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_functionItemExpr);
		try {
			setState(1167);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitNamedFunctionRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedFunctionRefContext namedFunctionRef() throws RecognitionException {
		NamedFunctionRefContext _localctx = new NamedFunctionRefContext(_ctx, getState());
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
		InlineFunctionExprContext _localctx = new InlineFunctionExprContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(1176);
				paramList();
				}
			}

			setState(1179);
			match(T__9);
			setState(1182);
			_errHandler.sync(this);
			_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitInsertExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertExprContext insertExpr() throws RecognitionException {
		InsertExprContext _localctx = new InsertExprContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_insertExpr);
		int _la;
		try {
			setState(1211);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
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
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
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
				_errHandler.sync(this);
				_la = _input.LA(1);
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
					_errHandler.sync(this);
					_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitDeleteExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteExprContext deleteExpr() throws RecognitionException {
		DeleteExprContext _localctx = new DeleteExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitRenameExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RenameExprContext renameExpr() throws RecognitionException {
		RenameExprContext _localctx = new RenameExprContext(_ctx, getState());
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
		ReplaceExprContext _localctx = new ReplaceExprContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
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
				_errHandler.sync(this);
				_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAppendExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AppendExprContext appendExpr() throws RecognitionException {
		AppendExprContext _localctx = new AppendExprContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUpdateLocator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateLocatorContext updateLocator() throws RecognitionException {
		UpdateLocatorContext _localctx = new UpdateLocatorContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_updateLocator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1251);
			((UpdateLocatorContext)_localctx).main_expr = primaryExpr();
			setState(1254); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(1254);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
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
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,119,_ctx);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitCopyDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CopyDeclContext copyDecl() throws RecognitionException {
		CopyDeclContext _localctx = new CopyDeclContext(_ctx, getState());
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
		SequenceTypeContext _localctx = new SequenceTypeContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_sequenceType);
		try {
			setState(1270);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
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
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,120,_ctx) ) {
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
		ObjectConstructorContext _localctx = new ObjectConstructorContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_objectConstructor);
		int _la;
		try {
			setState(1288);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(1272);
				match(T__6);
				setState(1281);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1566408243247242592L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 16831L) != 0)) {
					{
					setState(1273);
					pairConstructor();
					setState(1278);
					_errHandler.sync(this);
					_la = _input.LA(1);
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
						_errHandler.sync(this);
						_la = _input.LA(1);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitItemType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ItemTypeContext itemType() throws RecognitionException {
		ItemTypeContext _localctx = new ItemTypeContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_itemType);
		try {
			setState(1293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionTestContext functionTest() throws RecognitionException {
		FunctionTestContext _localctx = new FunctionTestContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_functionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitAnyFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyFunctionTestContext anyFunctionTest() throws RecognitionException {
		AnyFunctionTestContext _localctx = new AnyFunctionTestContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitTypedFunctionTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedFunctionTestContext typedFunctionTest() throws RecognitionException {
		TypedFunctionTestContext _localctx = new TypedFunctionTestContext(_ctx, getState());
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
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2305843007066209792L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 16543L) != 0)) {
				{
				setState(1306);
				((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
				((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
				setState(1311);
				_errHandler.sync(this);
				_la = _input.LA(1);
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
					_errHandler.sync(this);
					_la = _input.LA(1);
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
		SingleTypeContext _localctx = new SingleTypeContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_singleType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1320);
			((SingleTypeContext)_localctx).item = itemType();
			setState(1322);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitPairConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairConstructorContext pairConstructor() throws RecognitionException {
		PairConstructorContext _localctx = new PairConstructorContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1326);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
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
			_la = _input.LA(1);
			if ( !(_la==T__17 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1329);
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitArrayConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayConstructorContext arrayConstructor() throws RecognitionException {
		ArrayConstructorContext _localctx = new ArrayConstructorContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1331);
			match(T__53);
			setState(1333);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -1566408243247242592L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -1L) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 16831L) != 0)) {
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitUriLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UriLiteralContext uriLiteral() throws RecognitionException {
		UriLiteralContext _localctx = new UriLiteralContext(_ctx, getState());
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
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
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
		KeyWordsContext _localctx = new KeyWordsContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1341);
			_la = _input.LA(1);
			if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & -1L) != 0) || ((((_la - 125)) & ~0x3f) == 0 && ((1L << (_la - 125)) & 1279L) != 0)) ) {
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
		"\u0004\u0001\u0090\u0540\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
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
		"|\u0002}\u0007}\u0002~\u0007~\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0107"+
		"\b\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u010b\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0003\u0004\u011b\b\u0004\u0001\u0004\u0001\u0004\u0005\u0004"+
		"\u011f\b\u0004\n\u0004\f\u0004\u0122\t\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0005\u0004\u0127\b\u0004\n\u0004\f\u0004\u012a\t\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0005\u0006\u012f\b\u0006\n\u0006\f\u0006\u0132"+
		"\t\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0003\b\u0139"+
		"\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u0148\b\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0003\u0010\u0166"+
		"\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0005\u0010\u016e\b\u0010\n\u0010\f\u0010\u0171\t\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0004\u0012\u0184\b\u0012"+
		"\u000b\u0012\f\u0012\u0185\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0013\u0001\u0013\u0004\u0013\u018e\b\u0013\u000b\u0013\f\u0013"+
		"\u018f\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0004\u0014\u0198\b\u0014\u000b\u0014\f\u0014\u0199\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0003\u0015\u019f\b\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0003\u0015\u01a4\b\u0015\u0005\u0015\u01a6\b\u0015\n\u0015"+
		"\f\u0015\u01a9\t\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0004\u0016\u01b2\b\u0016\u000b\u0016"+
		"\f\u0016\u01b3\u0001\u0016\u0001\u0016\u0003\u0016\u01b8\b\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0003\u0017\u01c1\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0005"+
		"\u0017\u01c6\b\u0017\n\u0017\f\u0017\u01c9\t\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0005\u0018\u01d4\b\u0018\n\u0018\f\u0018\u01d7\t\u0018\u0001"+
		"\u0018\u0003\u0018\u01da\b\u0018\u0001\u0019\u0005\u0019\u01dd\b\u0019"+
		"\n\u0019\f\u0019\u01e0\t\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0005\u001a\u01e7\b\u001a\n\u001a\f\u001a\u01ea\t\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0003\u001b"+
		"\u01f1\b\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u01f5\b\u001b\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u0201\b\u001d\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u020d\b\u001f\u0001"+
		" \u0001 \u0001 \u0001 \u0001 \u0001!\u0001!\u0001!\u0001!\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001#\u0001#\u0001#\u0001#\u0001#"+
		"\u0003#\u0223\b#\u0001#\u0001#\u0001#\u0001#\u0005#\u0229\b#\n#\f#\u022c"+
		"\t#\u0001$\u0001$\u0003$\u0230\b$\u0001$\u0003$\u0233\b$\u0001$\u0001"+
		"$\u0003$\u0237\b$\u0001%\u0001%\u0001&\u0001&\u0001&\u0001&\u0001&\u0003"+
		"&\u0240\b&\u0001&\u0001&\u0001&\u0001&\u0001&\u0005&\u0247\b&\n&\f&\u024a"+
		"\t&\u0003&\u024c\b&\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003"+
		"\'\u0254\b\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0003\'\u025b\b\'"+
		"\u0003\'\u025d\b\'\u0001(\u0001(\u0001(\u0001(\u0001(\u0003(\u0264\b("+
		"\u0001(\u0001(\u0001(\u0001(\u0001(\u0003(\u026b\b(\u0003(\u026d\b(\u0001"+
		")\u0001)\u0001)\u0001)\u0001)\u0001)\u0003)\u0275\b)\u0001)\u0001)\u0001"+
		")\u0003)\u027a\b)\u0001)\u0001)\u0001)\u0001)\u0001)\u0003)\u0281\b)\u0001"+
		"*\u0001*\u0001*\u0001*\u0001*\u0003*\u0288\b*\u0001*\u0001*\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0003+\u0292\b+\u0001,\u0001,\u0001,\u0005"+
		",\u0297\b,\n,\f,\u029a\t,\u0001-\u0001-\u0001-\u0001-\u0003-\u02a0\b-"+
		"\u0001.\u0001.\u0001.\u0005.\u02a5\b.\n.\f.\u02a8\t.\u0001/\u0001/\u0001"+
		"/\u0001/\u0001/\u0001/\u0003/\u02b0\b/\u00010\u00010\u00010\u00010\u0001"+
		"0\u00010\u00010\u00010\u00030\u02ba\b0\u00011\u00011\u00031\u02be\b1\u0001"+
		"1\u00011\u00011\u00011\u00011\u00011\u00051\u02c6\b1\n1\f1\u02c9\t1\u0001"+
		"1\u00011\u00011\u00012\u00012\u00012\u00012\u00052\u02d2\b2\n2\f2\u02d5"+
		"\t2\u00013\u00013\u00013\u00033\u02da\b3\u00013\u00013\u00033\u02de\b"+
		"3\u00013\u00013\u00033\u02e2\b3\u00013\u00013\u00013\u00014\u00014\u0001"+
		"4\u00014\u00054\u02eb\b4\n4\f4\u02ee\t4\u00015\u00015\u00015\u00035\u02f3"+
		"\b5\u00015\u00015\u00015\u00016\u00016\u00016\u00017\u00017\u00017\u0001"+
		"7\u00017\u00057\u0300\b7\n7\f7\u0303\t7\u00018\u00018\u00018\u00038\u0308"+
		"\b8\u00018\u00018\u00038\u030c\b8\u00018\u00018\u00038\u0310\b8\u0001"+
		"9\u00019\u00019\u00019\u00019\u00039\u0317\b9\u00019\u00019\u00019\u0005"+
		"9\u031c\b9\n9\f9\u031f\t9\u0001:\u0001:\u0001:\u0003:\u0324\b:\u0001:"+
		"\u0001:\u0001:\u0003:\u0329\b:\u0003:\u032b\b:\u0001:\u0001:\u0003:\u032f"+
		"\b:\u0001;\u0001;\u0001;\u0001<\u0001<\u0003<\u0336\b<\u0001<\u0001<\u0001"+
		"<\u0005<\u033b\b<\n<\f<\u033e\t<\u0001<\u0001<\u0001<\u0001=\u0001=\u0001"+
		"=\u0003=\u0346\b=\u0001=\u0001=\u0001=\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0004>\u0350\b>\u000b>\f>\u0351\u0001>\u0001>\u0001>\u0001>\u0001?\u0001"+
		"?\u0004?\u035a\b?\u000b?\f?\u035b\u0001?\u0001?\u0001?\u0001@\u0001@\u0001"+
		"@\u0001@\u0001@\u0004@\u0366\b@\u000b@\f@\u0367\u0001@\u0001@\u0003@\u036c"+
		"\b@\u0001@\u0001@\u0001@\u0001A\u0001A\u0001A\u0001A\u0003A\u0375\bA\u0001"+
		"A\u0001A\u0001A\u0005A\u037a\bA\nA\fA\u037d\tA\u0001A\u0001A\u0001A\u0001"+
		"B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001B\u0001C\u0001"+
		"C\u0001C\u0001C\u0001C\u0004C\u0390\bC\u000bC\fC\u0391\u0001D\u0001D\u0001"+
		"D\u0003D\u0397\bD\u0001D\u0001D\u0001D\u0003D\u039c\bD\u0005D\u039e\b"+
		"D\nD\fD\u03a1\tD\u0001D\u0001D\u0001D\u0001D\u0001E\u0001E\u0001E\u0005"+
		"E\u03aa\bE\nE\fE\u03ad\tE\u0001F\u0001F\u0001F\u0005F\u03b2\bF\nF\fF\u03b5"+
		"\tF\u0001G\u0003G\u03b8\bG\u0001G\u0001G\u0001H\u0001H\u0001H\u0003H\u03bf"+
		"\bH\u0001I\u0001I\u0001I\u0005I\u03c4\bI\nI\fI\u03c7\tI\u0001J\u0001J"+
		"\u0001J\u0003J\u03cc\bJ\u0001K\u0001K\u0001K\u0005K\u03d1\bK\nK\fK\u03d4"+
		"\tK\u0001L\u0001L\u0001L\u0005L\u03d9\bL\nL\fL\u03dc\tL\u0001M\u0001M"+
		"\u0001M\u0001M\u0003M\u03e2\bM\u0001N\u0001N\u0001N\u0001N\u0003N\u03e8"+
		"\bN\u0001O\u0001O\u0001O\u0001O\u0003O\u03ee\bO\u0001P\u0001P\u0001P\u0001"+
		"P\u0003P\u03f4\bP\u0001Q\u0001Q\u0001Q\u0001Q\u0003Q\u03fa\bQ\u0001R\u0001"+
		"R\u0001R\u0001R\u0001R\u0001R\u0001R\u0005R\u0403\bR\nR\fR\u0406\tR\u0001"+
		"S\u0001S\u0001S\u0003S\u040b\bS\u0001T\u0005T\u040e\bT\nT\fT\u0411\tT"+
		"\u0001T\u0001T\u0001U\u0001U\u0001U\u0003U\u0418\bU\u0001V\u0001V\u0001"+
		"V\u0001V\u0001V\u0001V\u0001V\u0001W\u0001W\u0001W\u0001W\u0001W\u0001"+
		"W\u0001W\u0001X\u0001X\u0001X\u0005X\u042b\bX\nX\fX\u042e\tX\u0001Y\u0001"+
		"Y\u0001Y\u0001Y\u0001Y\u0001Y\u0005Y\u0436\bY\nY\fY\u0439\tY\u0001Z\u0001"+
		"Z\u0001Z\u0001Z\u0001Z\u0001Z\u0001[\u0001[\u0001[\u0001\\\u0001\\\u0001"+
		"\\\u0001\\\u0001]\u0001]\u0001]\u0001]\u0001]\u0001]\u0001]\u0003]\u044f"+
		"\b]\u0001^\u0001^\u0001^\u0001^\u0001^\u0001^\u0001^\u0001^\u0001^\u0001"+
		"^\u0001^\u0001^\u0001^\u0001^\u0001^\u0003^\u0460\b^\u0001_\u0001_\u0001"+
		"_\u0001_\u0001`\u0001`\u0001`\u0001a\u0001a\u0003a\u046b\ba\u0001a\u0001"+
		"a\u0001b\u0001b\u0001c\u0001c\u0001c\u0001c\u0001c\u0001d\u0001d\u0001"+
		"d\u0001d\u0001d\u0001e\u0001e\u0001e\u0001f\u0001f\u0001f\u0003f\u0481"+
		"\bf\u0005f\u0483\bf\nf\ff\u0486\tf\u0001f\u0001f\u0001g\u0001g\u0003g"+
		"\u048c\bg\u0001h\u0001h\u0003h\u0490\bh\u0001i\u0001i\u0001i\u0001i\u0001"+
		"j\u0001j\u0001j\u0001j\u0003j\u049a\bj\u0001j\u0001j\u0001j\u0003j\u049f"+
		"\bj\u0001j\u0001j\u0001j\u0001j\u0001k\u0001k\u0001k\u0001k\u0001k\u0001"+
		"k\u0001k\u0001k\u0003k\u04ad\bk\u0001k\u0001k\u0001k\u0001k\u0001k\u0005"+
		"k\u04b4\bk\nk\fk\u04b7\tk\u0001k\u0001k\u0001k\u0003k\u04bc\bk\u0001l"+
		"\u0001l\u0001l\u0001l\u0001m\u0001m\u0001m\u0001m\u0001m\u0001m\u0001"+
		"n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001o\u0001o\u0001"+
		"o\u0001o\u0005o\u04d4\bo\no\fo\u04d7\to\u0001o\u0001o\u0001o\u0001o\u0001"+
		"o\u0001p\u0001p\u0001p\u0001p\u0001p\u0001p\u0001q\u0001q\u0001q\u0004"+
		"q\u04e7\bq\u000bq\fq\u04e8\u0001r\u0001r\u0001r\u0001r\u0001s\u0001s\u0001"+
		"s\u0001s\u0001s\u0001s\u0003s\u04f5\bs\u0003s\u04f7\bs\u0001t\u0001t\u0001"+
		"t\u0001t\u0005t\u04fd\bt\nt\ft\u0500\tt\u0003t\u0502\bt\u0001t\u0001t"+
		"\u0001t\u0001t\u0001t\u0003t\u0509\bt\u0001u\u0001u\u0001u\u0003u\u050e"+
		"\bu\u0001v\u0001v\u0003v\u0512\bv\u0001w\u0001w\u0001w\u0001w\u0001w\u0001"+
		"x\u0001x\u0001x\u0001x\u0001x\u0005x\u051e\bx\nx\fx\u0521\tx\u0003x\u0523"+
		"\bx\u0001x\u0001x\u0001x\u0001x\u0001y\u0001y\u0003y\u052b\by\u0001z\u0001"+
		"z\u0003z\u052f\bz\u0001z\u0001z\u0001z\u0001{\u0001{\u0003{\u0536\b{\u0001"+
		"{\u0001{\u0001|\u0001|\u0001}\u0001}\u0001~\u0001~\u0001~\u0000\u0000"+
		"\u007f\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080"+
		"\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098"+
		"\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0"+
		"\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8"+
		"\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0"+
		"\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8"+
		"\u00fa\u00fc\u0000\b\u0002\u0000\u0010\u0010ii\u0001\u0000RS\u0001\u0000"+
		"\u0013\u001c\u0002\u0000\u0004\u0004$.\u0001\u000001\u0002\u0000\u000b"+
		"\u000b24\u0002\u0000\u0012\u0012\u0086\u0086\u0002\u0000=\u0084\u0087"+
		"\u0087\u0581\u0000\u00fe\u0001\u0000\u0000\u0000\u0002\u0106\u0001\u0000"+
		"\u0000\u0000\u0004\u010c\u0001\u0000\u0000\u0000\u0006\u010f\u0001\u0000"+
		"\u0000\u0000\b\u0120\u0001\u0000\u0000\u0000\n\u012b\u0001\u0000\u0000"+
		"\u0000\f\u0130\u0001\u0000\u0000\u0000\u000e\u0133\u0001\u0000\u0000\u0000"+
		"\u0010\u0136\u0001\u0000\u0000\u0000\u0012\u0147\u0001\u0000\u0000\u0000"+
		"\u0014\u0149\u0001\u0000\u0000\u0000\u0016\u014c\u0001\u0000\u0000\u0000"+
		"\u0018\u0152\u0001\u0000\u0000\u0000\u001a\u0156\u0001\u0000\u0000\u0000"+
		"\u001c\u015a\u0001\u0000\u0000\u0000\u001e\u015e\u0001\u0000\u0000\u0000"+
		" \u0165\u0001\u0000\u0000\u0000\"\u0175\u0001\u0000\u0000\u0000$\u017e"+
		"\u0001\u0000\u0000\u0000&\u018d\u0001\u0000\u0000\u0000(\u0194\u0001\u0000"+
		"\u0000\u0000*\u019b\u0001\u0000\u0000\u0000,\u01ac\u0001\u0000\u0000\u0000"+
		".\u01bc\u0001\u0000\u0000\u00000\u01cd\u0001\u0000\u0000\u00002\u01de"+
		"\u0001\u0000\u0000\u00004\u01e1\u0001\u0000\u0000\u00006\u01ed\u0001\u0000"+
		"\u0000\u00008\u01f6\u0001\u0000\u0000\u0000:\u0200\u0001\u0000\u0000\u0000"+
		"<\u0202\u0001\u0000\u0000\u0000>\u020c\u0001\u0000\u0000\u0000@\u020e"+
		"\u0001\u0000\u0000\u0000B\u0213\u0001\u0000\u0000\u0000D\u0217\u0001\u0000"+
		"\u0000\u0000F\u021d\u0001\u0000\u0000\u0000H\u0232\u0001\u0000\u0000\u0000"+
		"J\u0238\u0001\u0000\u0000\u0000L\u023a\u0001\u0000\u0000\u0000N\u024d"+
		"\u0001\u0000\u0000\u0000P\u025e\u0001\u0000\u0000\u0000R\u026e\u0001\u0000"+
		"\u0000\u0000T\u0282\u0001\u0000\u0000\u0000V\u0291\u0001\u0000\u0000\u0000"+
		"X\u0293\u0001\u0000\u0000\u0000Z\u029b\u0001\u0000\u0000\u0000\\\u02a1"+
		"\u0001\u0000\u0000\u0000^\u02af\u0001\u0000\u0000\u0000`\u02b9\u0001\u0000"+
		"\u0000\u0000b\u02bd\u0001\u0000\u0000\u0000d\u02cd\u0001\u0000\u0000\u0000"+
		"f\u02d6\u0001\u0000\u0000\u0000h\u02e6\u0001\u0000\u0000\u0000j\u02ef"+
		"\u0001\u0000\u0000\u0000l\u02f7\u0001\u0000\u0000\u0000n\u02fa\u0001\u0000"+
		"\u0000\u0000p\u0304\u0001\u0000\u0000\u0000r\u0316\u0001\u0000\u0000\u0000"+
		"t\u0320\u0001\u0000\u0000\u0000v\u0330\u0001\u0000\u0000\u0000x\u0335"+
		"\u0001\u0000\u0000\u0000z\u0342\u0001\u0000\u0000\u0000|\u034a\u0001\u0000"+
		"\u0000\u0000~\u0359\u0001\u0000\u0000\u0000\u0080\u0360\u0001\u0000\u0000"+
		"\u0000\u0082\u0370\u0001\u0000\u0000\u0000\u0084\u0381\u0001\u0000\u0000"+
		"\u0000\u0086\u038a\u0001\u0000\u0000\u0000\u0088\u0393\u0001\u0000\u0000"+
		"\u0000\u008a\u03a6\u0001\u0000\u0000\u0000\u008c\u03ae\u0001\u0000\u0000"+
		"\u0000\u008e\u03b7\u0001\u0000\u0000\u0000\u0090\u03bb\u0001\u0000\u0000"+
		"\u0000\u0092\u03c0\u0001\u0000\u0000\u0000\u0094\u03c8\u0001\u0000\u0000"+
		"\u0000\u0096\u03cd\u0001\u0000\u0000\u0000\u0098\u03d5\u0001\u0000\u0000"+
		"\u0000\u009a\u03dd\u0001\u0000\u0000\u0000\u009c\u03e3\u0001\u0000\u0000"+
		"\u0000\u009e\u03e9\u0001\u0000\u0000\u0000\u00a0\u03ef\u0001\u0000\u0000"+
		"\u0000\u00a2\u03f5\u0001\u0000\u0000\u0000\u00a4\u03fb\u0001\u0000\u0000"+
		"\u0000\u00a6\u040a\u0001\u0000\u0000\u0000\u00a8\u040f\u0001\u0000\u0000"+
		"\u0000\u00aa\u0417\u0001\u0000\u0000\u0000\u00ac\u0419\u0001\u0000\u0000"+
		"\u0000\u00ae\u0420\u0001\u0000\u0000\u0000\u00b0\u0427\u0001\u0000\u0000"+
		"\u0000\u00b2\u042f\u0001\u0000\u0000\u0000\u00b4\u043a\u0001\u0000\u0000"+
		"\u0000\u00b6\u0440\u0001\u0000\u0000\u0000\u00b8\u0443\u0001\u0000\u0000"+
		"\u0000\u00ba\u0447\u0001\u0000\u0000\u0000\u00bc\u045f\u0001\u0000\u0000"+
		"\u0000\u00be\u0461\u0001\u0000\u0000\u0000\u00c0\u0465\u0001\u0000\u0000"+
		"\u0000\u00c2\u0468\u0001\u0000\u0000\u0000\u00c4\u046e\u0001\u0000\u0000"+
		"\u0000\u00c6\u0470\u0001\u0000\u0000\u0000\u00c8\u0475\u0001\u0000\u0000"+
		"\u0000\u00ca\u047a\u0001\u0000\u0000\u0000\u00cc\u047d\u0001\u0000\u0000"+
		"\u0000\u00ce\u048b\u0001\u0000\u0000\u0000\u00d0\u048f\u0001\u0000\u0000"+
		"\u0000\u00d2\u0491\u0001\u0000\u0000\u0000\u00d4\u0495\u0001\u0000\u0000"+
		"\u0000\u00d6\u04bb\u0001\u0000\u0000\u0000\u00d8\u04bd\u0001\u0000\u0000"+
		"\u0000\u00da\u04c1\u0001\u0000\u0000\u0000\u00dc\u04c7\u0001\u0000\u0000"+
		"\u0000\u00de\u04cf\u0001\u0000\u0000\u0000\u00e0\u04dd\u0001\u0000\u0000"+
		"\u0000\u00e2\u04e3\u0001\u0000\u0000\u0000\u00e4\u04ea\u0001\u0000\u0000"+
		"\u0000\u00e6\u04f6\u0001\u0000\u0000\u0000\u00e8\u0508\u0001\u0000\u0000"+
		"\u0000\u00ea\u050d\u0001\u0000\u0000\u0000\u00ec\u0511\u0001\u0000\u0000"+
		"\u0000\u00ee\u0513\u0001\u0000\u0000\u0000\u00f0\u0518\u0001\u0000\u0000"+
		"\u0000\u00f2\u0528\u0001\u0000\u0000\u0000\u00f4\u052e\u0001\u0000\u0000"+
		"\u0000\u00f6\u0533\u0001\u0000\u0000\u0000\u00f8\u0539\u0001\u0000\u0000"+
		"\u0000\u00fa\u053b\u0001\u0000\u0000\u0000\u00fc\u053d\u0001\u0000\u0000"+
		"\u0000\u00fe\u00ff\u0003\u0002\u0001\u0000\u00ff\u0100\u0005\u0000\u0000"+
		"\u0001\u0100\u0001\u0001\u0000\u0000\u0000\u0101\u0102\u0005h\u0000\u0000"+
		"\u0102\u0103\u0005g\u0000\u0000\u0103\u0104\u0003\u00fa}\u0000\u0104\u0105"+
		"\u0005\u0001\u0000\u0000\u0105\u0107\u0001\u0000\u0000\u0000\u0106\u0101"+
		"\u0001\u0000\u0000\u0000\u0106\u0107\u0001\u0000\u0000\u0000\u0107\u010a"+
		"\u0001\u0000\u0000\u0000\u0108\u010b\u0003\u0006\u0003\u0000\u0109\u010b"+
		"\u0003\u0004\u0002\u0000\u010a\u0108\u0001\u0000\u0000\u0000\u010a\u0109"+
		"\u0001\u0000\u0000\u0000\u010b\u0003\u0001\u0000\u0000\u0000\u010c\u010d"+
		"\u0003\b\u0004\u0000\u010d\u010e\u0003\n\u0005\u0000\u010e\u0005\u0001"+
		"\u0000\u0000\u0000\u010f\u0110\u0005\u0002\u0000\u0000\u0110\u0111\u0005"+
		"\u0003\u0000\u0000\u0111\u0112\u0005\u008e\u0000\u0000\u0112\u0113\u0005"+
		"\u0004\u0000\u0000\u0113\u0114\u0003\u00f8|\u0000\u0114\u0115\u0005\u0001"+
		"\u0000\u0000\u0115\u0116\u0003\b\u0004\u0000\u0116\u0007\u0001\u0000\u0000"+
		"\u0000\u0117\u011b\u0003:\u001d\u0000\u0118\u011b\u0003<\u001e\u0000\u0119"+
		"\u011b\u0003L&\u0000\u011a\u0117\u0001\u0000\u0000\u0000\u011a\u0118\u0001"+
		"\u0000\u0000\u0000\u011a\u0119\u0001\u0000\u0000\u0000\u011b\u011c\u0001"+
		"\u0000\u0000\u0000\u011c\u011d\u0005\u0001\u0000\u0000\u011d\u011f\u0001"+
		"\u0000\u0000\u0000\u011e\u011a\u0001\u0000\u0000\u0000\u011f\u0122\u0001"+
		"\u0000\u0000\u0000\u0120\u011e\u0001\u0000\u0000\u0000\u0120\u0121\u0001"+
		"\u0000\u0000\u0000\u0121\u0128\u0001\u0000\u0000\u0000\u0122\u0120\u0001"+
		"\u0000\u0000\u0000\u0123\u0124\u0003>\u001f\u0000\u0124\u0125\u0005\u0001"+
		"\u0000\u0000\u0125\u0127\u0001\u0000\u0000\u0000\u0126\u0123\u0001\u0000"+
		"\u0000\u0000\u0127\u012a\u0001\u0000\u0000\u0000\u0128\u0126\u0001\u0000"+
		"\u0000\u0000\u0128\u0129\u0001\u0000\u0000\u0000\u0129\t\u0001\u0000\u0000"+
		"\u0000\u012a\u0128\u0001\u0000\u0000\u0000\u012b\u012c\u0003\u0010\b\u0000"+
		"\u012c\u000b\u0001\u0000\u0000\u0000\u012d\u012f\u0003\u0012\t\u0000\u012e"+
		"\u012d\u0001\u0000\u0000\u0000\u012f\u0132\u0001\u0000\u0000\u0000\u0130"+
		"\u012e\u0001\u0000\u0000\u0000\u0130\u0131\u0001\u0000\u0000\u0000\u0131"+
		"\r\u0001\u0000\u0000\u0000\u0132\u0130\u0001\u0000\u0000\u0000\u0133\u0134"+
		"\u0003\f\u0006\u0000\u0134\u0135\u0003\\.\u0000\u0135\u000f\u0001\u0000"+
		"\u0000\u0000\u0136\u0138\u0003\f\u0006\u0000\u0137\u0139\u0003\\.\u0000"+
		"\u0138\u0137\u0001\u0000\u0000\u0000\u0138\u0139\u0001\u0000\u0000\u0000"+
		"\u0139\u0011\u0001\u0000\u0000\u0000\u013a\u0148\u0003\u0014\n\u0000\u013b"+
		"\u0148\u0003\u0016\u000b\u0000\u013c\u0148\u0003\u0018\f\u0000\u013d\u0148"+
		"\u0003\u001a\r\u0000\u013e\u0148\u0003\u001c\u000e\u0000\u013f\u0148\u0003"+
		"\u001e\u000f\u0000\u0140\u0148\u0003 \u0010\u0000\u0141\u0148\u0003\""+
		"\u0011\u0000\u0142\u0148\u0003$\u0012\u0000\u0143\u0148\u0003(\u0014\u0000"+
		"\u0144\u0148\u0003,\u0016\u0000\u0145\u0148\u00034\u001a\u0000\u0146\u0148"+
		"\u00038\u001c\u0000\u0147\u013a\u0001\u0000\u0000\u0000\u0147\u013b\u0001"+
		"\u0000\u0000\u0000\u0147\u013c\u0001\u0000\u0000\u0000\u0147\u013d\u0001"+
		"\u0000\u0000\u0000\u0147\u013e\u0001\u0000\u0000\u0000\u0147\u013f\u0001"+
		"\u0000\u0000\u0000\u0147\u0140\u0001\u0000\u0000\u0000\u0147\u0141\u0001"+
		"\u0000\u0000\u0000\u0147\u0142\u0001\u0000\u0000\u0000\u0147\u0143\u0001"+
		"\u0000\u0000\u0000\u0147\u0144\u0001\u0000\u0000\u0000\u0147\u0145\u0001"+
		"\u0000\u0000\u0000\u0147\u0146\u0001\u0000\u0000\u0000\u0148\u0013\u0001"+
		"\u0000\u0000\u0000\u0149\u014a\u0003`0\u0000\u014a\u014b\u0005\u0001\u0000"+
		"\u0000\u014b\u0015\u0001\u0000\u0000\u0000\u014c\u014d\u0005\u0005\u0000"+
		"\u0000\u014d\u014e\u0003H$\u0000\u014e\u014f\u0005\u0006\u0000\u0000\u014f"+
		"\u0150\u0003^/\u0000\u0150\u0151\u0005\u0001\u0000\u0000\u0151\u0017\u0001"+
		"\u0000\u0000\u0000\u0152\u0153\u0005\u0007\u0000\u0000\u0153\u0154\u0003"+
		"\f\u0006\u0000\u0154\u0155\u0005\b\u0000\u0000\u0155\u0019\u0001\u0000"+
		"\u0000\u0000\u0156\u0157\u0005\u007f\u0000\u0000\u0157\u0158\u0005\u0080"+
		"\u0000\u0000\u0158\u0159\u0005\u0001\u0000\u0000\u0159\u001b\u0001\u0000"+
		"\u0000\u0000\u015a\u015b\u0005\u0081\u0000\u0000\u015b\u015c\u0005\u0080"+
		"\u0000\u0000\u015c\u015d\u0005\u0001\u0000\u0000\u015d\u001d\u0001\u0000"+
		"\u0000\u0000\u015e\u015f\u0005\u0082\u0000\u0000\u015f\u0160\u0005\u0083"+
		"\u0000\u0000\u0160\u0161\u0003^/\u0000\u0161\u0162\u0005\u0001\u0000\u0000"+
		"\u0162\u001f\u0001\u0000\u0000\u0000\u0163\u0166\u0003d2\u0000\u0164\u0166"+
		"\u0003h4\u0000\u0165\u0163\u0001\u0000\u0000\u0000\u0165\u0164\u0001\u0000"+
		"\u0000\u0000\u0166\u016f\u0001\u0000\u0000\u0000\u0167\u016e\u0003d2\u0000"+
		"\u0168\u016e\u0003h4\u0000\u0169\u016e\u0003l6\u0000\u016a\u016e\u0003"+
		"n7\u0000\u016b\u016e\u0003r9\u0000\u016c\u016e\u0003v;\u0000\u016d\u0167"+
		"\u0001\u0000\u0000\u0000\u016d\u0168\u0001\u0000\u0000\u0000\u016d\u0169"+
		"\u0001\u0000\u0000\u0000\u016d\u016a\u0001\u0000\u0000\u0000\u016d\u016b"+
		"\u0001\u0000\u0000\u0000\u016d\u016c\u0001\u0000\u0000\u0000\u016e\u0171"+
		"\u0001\u0000\u0000\u0000\u016f\u016d\u0001\u0000\u0000\u0000\u016f\u0170"+
		"\u0001\u0000\u0000\u0000\u0170\u0172\u0001\u0000\u0000\u0000\u0171\u016f"+
		"\u0001\u0000\u0000\u0000\u0172\u0173\u0005C\u0000\u0000\u0173\u0174\u0003"+
		"\u0012\t\u0000\u0174!\u0001\u0000\u0000\u0000\u0175\u0176\u0005D\u0000"+
		"\u0000\u0176\u0177\u0005\t\u0000\u0000\u0177\u0178\u0003\\.\u0000\u0178"+
		"\u0179\u0005\n\u0000\u0000\u0179\u017a\u0005Y\u0000\u0000\u017a\u017b"+
		"\u0003\u0012\t\u0000\u017b\u017c\u0005Z\u0000\u0000\u017c\u017d\u0003"+
		"\u0012\t\u0000\u017d#\u0001\u0000\u0000\u0000\u017e\u017f\u0005T\u0000"+
		"\u0000\u017f\u0180\u0005\t\u0000\u0000\u0180\u0181\u0003\\.\u0000\u0181"+
		"\u0183\u0005\n\u0000\u0000\u0182\u0184\u0003&\u0013\u0000\u0183\u0182"+
		"\u0001\u0000\u0000\u0000\u0184\u0185\u0001\u0000\u0000\u0000\u0185\u0183"+
		"\u0001\u0000\u0000\u0000\u0185\u0186\u0001\u0000\u0000\u0000\u0186\u0187"+
		"\u0001\u0000\u0000\u0000\u0187\u0188\u0005X\u0000\u0000\u0188\u0189\u0005"+
		"C\u0000\u0000\u0189\u018a\u0003\u0012\t\u0000\u018a%\u0001\u0000\u0000"+
		"\u0000\u018b\u018c\u0005U\u0000\u0000\u018c\u018e\u0003^/\u0000\u018d"+
		"\u018b\u0001\u0000\u0000\u0000\u018e\u018f\u0001\u0000\u0000\u0000\u018f"+
		"\u018d\u0001\u0000\u0000\u0000\u018f\u0190\u0001\u0000\u0000\u0000\u0190"+
		"\u0191\u0001\u0000\u0000\u0000\u0191\u0192\u0005C\u0000\u0000\u0192\u0193"+
		"\u0003\u0012\t\u0000\u0193\'\u0001\u0000\u0000\u0000\u0194\u0195\u0005"+
		"V\u0000\u0000\u0195\u0197\u0003\u0018\f\u0000\u0196\u0198\u0003*\u0015"+
		"\u0000\u0197\u0196\u0001\u0000\u0000\u0000\u0198\u0199\u0001\u0000\u0000"+
		"\u0000\u0199\u0197\u0001\u0000\u0000\u0000\u0199\u019a\u0001\u0000\u0000"+
		"\u0000\u019a)\u0001\u0000\u0000\u0000\u019b\u019e\u0005W\u0000\u0000\u019c"+
		"\u019f\u0005\u000b\u0000\u0000\u019d\u019f\u0003H$\u0000\u019e\u019c\u0001"+
		"\u0000\u0000\u0000\u019e\u019d\u0001\u0000\u0000\u0000\u019f\u01a7\u0001"+
		"\u0000\u0000\u0000\u01a0\u01a3\u0005\f\u0000\u0000\u01a1\u01a4\u0005\u000b"+
		"\u0000\u0000\u01a2\u01a4\u0003H$\u0000\u01a3\u01a1\u0001\u0000\u0000\u0000"+
		"\u01a3\u01a2\u0001\u0000\u0000\u0000\u01a4\u01a6\u0001\u0000\u0000\u0000"+
		"\u01a5\u01a0\u0001\u0000\u0000\u0000\u01a6\u01a9\u0001\u0000\u0000\u0000"+
		"\u01a7\u01a5\u0001\u0000\u0000\u0000\u01a7\u01a8\u0001\u0000\u0000\u0000"+
		"\u01a8\u01aa\u0001\u0000\u0000\u0000\u01a9\u01a7\u0001\u0000\u0000\u0000"+
		"\u01aa\u01ab\u0003\u0018\f\u0000\u01ab+\u0001\u0000\u0000\u0000\u01ac"+
		"\u01ad\u0005[\u0000\u0000\u01ad\u01ae\u0005\t\u0000\u0000\u01ae\u01af"+
		"\u0003\\.\u0000\u01af\u01b1\u0005\n\u0000\u0000\u01b0\u01b2\u0003.\u0017"+
		"\u0000\u01b1\u01b0\u0001\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000\u0000"+
		"\u0000\u01b3\u01b1\u0001\u0000\u0000\u0000\u01b3\u01b4\u0001\u0000\u0000"+
		"\u0000\u01b4\u01b5\u0001\u0000\u0000\u0000\u01b5\u01b7\u0005X\u0000\u0000"+
		"\u01b6\u01b8\u0003\u00c0`\u0000\u01b7\u01b6\u0001\u0000\u0000\u0000\u01b7"+
		"\u01b8\u0001\u0000\u0000\u0000\u01b8\u01b9\u0001\u0000\u0000\u0000\u01b9"+
		"\u01ba\u0005C\u0000\u0000\u01ba\u01bb\u0003\u0012\t\u0000\u01bb-\u0001"+
		"\u0000\u0000\u0000\u01bc\u01c0\u0005U\u0000\u0000\u01bd\u01be\u0003\u00c0"+
		"`\u0000\u01be\u01bf\u0005F\u0000\u0000\u01bf\u01c1\u0001\u0000\u0000\u0000"+
		"\u01c0\u01bd\u0001\u0000\u0000\u0000\u01c0\u01c1\u0001\u0000\u0000\u0000"+
		"\u01c1\u01c2\u0001\u0000\u0000\u0000\u01c2\u01c7\u0003\u00e6s\u0000\u01c3"+
		"\u01c4\u0005\f\u0000\u0000\u01c4\u01c6\u0003\u00e6s\u0000\u01c5\u01c3"+
		"\u0001\u0000\u0000\u0000\u01c6\u01c9\u0001\u0000\u0000\u0000\u01c7\u01c5"+
		"\u0001\u0000\u0000\u0000\u01c7\u01c8\u0001\u0000\u0000\u0000\u01c8\u01ca"+
		"\u0001\u0000\u0000\u0000\u01c9\u01c7\u0001\u0000\u0000\u0000\u01ca\u01cb"+
		"\u0005C\u0000\u0000\u01cb\u01cc\u0003\u0012\t\u0000\u01cc/\u0001\u0000"+
		"\u0000\u0000\u01cd\u01ce\u0005\r\u0000\u0000\u01ce\u01d9\u0003H$\u0000"+
		"\u01cf\u01d0\u0005\t\u0000\u0000\u01d0\u01d5\u0005\u0088\u0000\u0000\u01d1"+
		"\u01d2\u0005\u000e\u0000\u0000\u01d2\u01d4\u0005\u0088\u0000\u0000\u01d3"+
		"\u01d1\u0001\u0000\u0000\u0000\u01d4\u01d7\u0001\u0000\u0000\u0000\u01d5"+
		"\u01d3\u0001\u0000\u0000\u0000\u01d5\u01d6\u0001\u0000\u0000\u0000\u01d6"+
		"\u01d8\u0001\u0000\u0000\u0000\u01d7\u01d5\u0001\u0000\u0000\u0000\u01d8"+
		"\u01da\u0005\n\u0000\u0000\u01d9\u01cf\u0001\u0000\u0000\u0000\u01d9\u01da"+
		"\u0001\u0000\u0000\u0000\u01da1\u0001\u0000\u0000\u0000\u01db\u01dd\u0003"+
		"0\u0018\u0000\u01dc\u01db\u0001\u0000\u0000\u0000\u01dd\u01e0\u0001\u0000"+
		"\u0000\u0000\u01de\u01dc\u0001\u0000\u0000\u0000\u01de\u01df\u0001\u0000"+
		"\u0000\u0000\u01df3\u0001\u0000\u0000\u0000\u01e0\u01de\u0001\u0000\u0000"+
		"\u0000\u01e1\u01e2\u00032\u0019\u0000\u01e2\u01e3\u0005r\u0000\u0000\u01e3"+
		"\u01e8\u00036\u001b\u0000\u01e4\u01e5\u0005\u000e\u0000\u0000\u01e5\u01e7"+
		"\u00036\u001b\u0000\u01e6\u01e4\u0001\u0000\u0000\u0000\u01e7\u01ea\u0001"+
		"\u0000\u0000\u0000\u01e8\u01e6\u0001\u0000\u0000\u0000\u01e8\u01e9\u0001"+
		"\u0000\u0000\u0000\u01e9\u01eb\u0001\u0000\u0000\u0000\u01ea\u01e8\u0001"+
		"\u0000\u0000\u0000\u01eb\u01ec\u0005\u0001\u0000\u0000\u01ec5\u0001\u0000"+
		"\u0000\u0000\u01ed\u01f0\u0003\u00c0`\u0000\u01ee\u01ef\u0005F\u0000\u0000"+
		"\u01ef\u01f1\u0003\u00e6s\u0000\u01f0\u01ee\u0001\u0000\u0000\u0000\u01f0"+
		"\u01f1\u0001\u0000\u0000\u0000\u01f1\u01f4\u0001\u0000\u0000\u0000\u01f2"+
		"\u01f3\u0005\u0006\u0000\u0000\u01f3\u01f5\u0003^/\u0000\u01f4\u01f2\u0001"+
		"\u0000\u0000\u0000\u01f4\u01f5\u0001\u0000\u0000\u0000\u01f57\u0001\u0000"+
		"\u0000\u0000\u01f6\u01f7\u0005\u0084\u0000\u0000\u01f7\u01f8\u0005\t\u0000"+
		"\u0000\u01f8\u01f9\u0003\\.\u0000\u01f9\u01fa\u0005\n\u0000\u0000\u01fa"+
		"\u01fb\u0003\u0012\t\u0000\u01fb9\u0001\u0000\u0000\u0000\u01fc\u0201"+
		"\u0003@ \u0000\u01fd\u0201\u0003B!\u0000\u01fe\u0201\u0003D\"\u0000\u01ff"+
		"\u0201\u0003F#\u0000\u0200\u01fc\u0001\u0000\u0000\u0000\u0200\u01fd\u0001"+
		"\u0000\u0000\u0000\u0200\u01fe\u0001\u0000\u0000\u0000\u0200\u01ff\u0001"+
		"\u0000\u0000\u0000\u0201;\u0001\u0000\u0000\u0000\u0202\u0203\u0005o\u0000"+
		"\u0000\u0203\u0204\u0005\u0003\u0000\u0000\u0204\u0205\u0005\u008e\u0000"+
		"\u0000\u0205\u0206\u0005\u0004\u0000\u0000\u0206\u0207\u0003\u00f8|\u0000"+
		"\u0207=\u0001\u0000\u0000\u0000\u0208\u020d\u0003R)\u0000\u0209\u020d"+
		"\u0003N\'\u0000\u020a\u020d\u0003T*\u0000\u020b\u020d\u0003P(\u0000\u020c"+
		"\u0208\u0001\u0000\u0000\u0000\u020c\u0209\u0001\u0000\u0000\u0000\u020c"+
		"\u020a\u0001\u0000\u0000\u0000\u020c\u020b\u0001\u0000\u0000\u0000\u020d"+
		"?\u0001\u0000\u0000\u0000\u020e\u020f\u0005o\u0000\u0000\u020f\u0210\u0005"+
		"X\u0000\u0000\u0210\u0211\u0005Q\u0000\u0000\u0211\u0212\u0003\u00f8|"+
		"\u0000\u0212A\u0001\u0000\u0000\u0000\u0213\u0214\u0005o\u0000\u0000\u0214"+
		"\u0215\u0005\u000f\u0000\u0000\u0215\u0216\u0007\u0000\u0000\u0000\u0216"+
		"C\u0001\u0000\u0000\u0000\u0217\u0218\u0005o\u0000\u0000\u0218\u0219\u0005"+
		"X\u0000\u0000\u0219\u021a\u0005B\u0000\u0000\u021a\u021b\u0005I\u0000"+
		"\u0000\u021b\u021c\u0007\u0001\u0000\u0000\u021cE\u0001\u0000\u0000\u0000"+
		"\u021d\u0222\u0005o\u0000\u0000\u021e\u021f\u0005\u0011\u0000\u0000\u021f"+
		"\u0223\u0003H$\u0000\u0220\u0221\u0005X\u0000\u0000\u0221\u0223\u0005"+
		"\u0011\u0000\u0000\u0222\u021e\u0001\u0000\u0000\u0000\u0222\u0220\u0001"+
		"\u0000\u0000\u0000\u0223\u022a\u0001\u0000\u0000\u0000\u0224\u0225\u0003"+
		"J%\u0000\u0225\u0226\u0005\u0004\u0000\u0000\u0226\u0227\u0003\u00fa}"+
		"\u0000\u0227\u0229\u0001\u0000\u0000\u0000\u0228\u0224\u0001\u0000\u0000"+
		"\u0000\u0229\u022c\u0001\u0000\u0000\u0000\u022a\u0228\u0001\u0000\u0000"+
		"\u0000\u022a\u022b\u0001\u0000\u0000\u0000\u022bG\u0001\u0000\u0000\u0000"+
		"\u022c\u022a\u0001\u0000\u0000\u0000\u022d\u0230\u0005\u008e\u0000\u0000"+
		"\u022e\u0230\u0003\u00fc~\u0000\u022f\u022d\u0001\u0000\u0000\u0000\u022f"+
		"\u022e\u0001\u0000\u0000\u0000\u0230\u0231\u0001\u0000\u0000\u0000\u0231"+
		"\u0233\u0005\u0012\u0000\u0000\u0232\u022f\u0001\u0000\u0000\u0000\u0232"+
		"\u0233\u0001\u0000\u0000\u0000\u0233\u0236\u0001\u0000\u0000\u0000\u0234"+
		"\u0237\u0005\u008e\u0000\u0000\u0235\u0237\u0003\u00fc~\u0000\u0236\u0234"+
		"\u0001\u0000\u0000\u0000\u0236\u0235\u0001\u0000\u0000\u0000\u0237I\u0001"+
		"\u0000\u0000\u0000\u0238\u0239\u0007\u0002\u0000\u0000\u0239K\u0001\u0000"+
		"\u0000\u0000\u023a\u023b\u0005\u001d\u0000\u0000\u023b\u023f\u0005\u0002"+
		"\u0000\u0000\u023c\u023d\u0005\u0003\u0000\u0000\u023d\u023e\u0005\u008e"+
		"\u0000\u0000\u023e\u0240\u0005\u0004\u0000\u0000\u023f\u023c\u0001\u0000"+
		"\u0000\u0000\u023f\u0240\u0001\u0000\u0000\u0000\u0240\u0241\u0001\u0000"+
		"\u0000\u0000\u0241\u024b\u0003\u00f8|\u0000\u0242\u0243\u0005G\u0000\u0000"+
		"\u0243\u0248\u0003\u00f8|\u0000\u0244\u0245\u0005\u000e\u0000\u0000\u0245"+
		"\u0247\u0003\u00f8|\u0000\u0246\u0244\u0001\u0000\u0000\u0000\u0247\u024a"+
		"\u0001\u0000\u0000\u0000\u0248\u0246\u0001\u0000\u0000\u0000\u0248\u0249"+
		"\u0001\u0000\u0000\u0000\u0249\u024c\u0001\u0000\u0000\u0000\u024a\u0248"+
		"\u0001\u0000\u0000\u0000\u024b\u0242\u0001\u0000\u0000\u0000\u024b\u024c"+
		"\u0001\u0000\u0000\u0000\u024cM\u0001\u0000\u0000\u0000\u024d\u024e\u0005"+
		"o\u0000\u0000\u024e\u024f\u00032\u0019\u0000\u024f\u0250\u0005r\u0000"+
		"\u0000\u0250\u0253\u0003\u00c0`\u0000\u0251\u0252\u0005F\u0000\u0000\u0252"+
		"\u0254\u0003\u00e6s\u0000\u0253\u0251\u0001\u0000\u0000\u0000\u0253\u0254"+
		"\u0001\u0000\u0000\u0000\u0254\u025c\u0001\u0000\u0000\u0000\u0255\u0256"+
		"\u0005\u0006\u0000\u0000\u0256\u025d\u0003^/\u0000\u0257\u025a\u0005\u001e"+
		"\u0000\u0000\u0258\u0259\u0005\u0006\u0000\u0000\u0259\u025b\u0003^/\u0000"+
		"\u025a\u0258\u0001\u0000\u0000\u0000\u025a\u025b\u0001\u0000\u0000\u0000"+
		"\u025b\u025d\u0001\u0000\u0000\u0000\u025c\u0255\u0001\u0000\u0000\u0000"+
		"\u025c\u0257\u0001\u0000\u0000\u0000\u025dO\u0001\u0000\u0000\u0000\u025e"+
		"\u025f\u0005o\u0000\u0000\u025f\u0260\u0005p\u0000\u0000\u0260\u0263\u0005"+
		"q\u0000\u0000\u0261\u0262\u0005F\u0000\u0000\u0262\u0264\u0003\u00e6s"+
		"\u0000\u0263\u0261\u0001\u0000\u0000\u0000\u0263\u0264\u0001\u0000\u0000"+
		"\u0000\u0264\u026c\u0001\u0000\u0000\u0000\u0265\u0266\u0005\u0006\u0000"+
		"\u0000\u0266\u026d\u0003^/\u0000\u0267\u026a\u0005\u001e\u0000\u0000\u0268"+
		"\u0269\u0005\u0006\u0000\u0000\u0269\u026b\u0003^/\u0000\u026a\u0268\u0001"+
		"\u0000\u0000\u0000\u026a\u026b\u0001\u0000\u0000\u0000\u026b\u026d\u0001"+
		"\u0000\u0000\u0000\u026c\u0265\u0001\u0000\u0000\u0000\u026c\u0267\u0001"+
		"\u0000\u0000\u0000\u026dQ\u0001\u0000\u0000\u0000\u026e\u026f\u0005o\u0000"+
		"\u0000\u026f\u0270\u00032\u0019\u0000\u0270\u0271\u0005\u001f\u0000\u0000"+
		"\u0271\u0272\u0003H$\u0000\u0272\u0274\u0005\t\u0000\u0000\u0273\u0275"+
		"\u0003X,\u0000\u0274\u0273\u0001\u0000\u0000\u0000\u0274\u0275\u0001\u0000"+
		"\u0000\u0000\u0275\u0276\u0001\u0000\u0000\u0000\u0276\u0279\u0005\n\u0000"+
		"\u0000\u0277\u0278\u0005F\u0000\u0000\u0278\u027a\u0003\u00e6s\u0000\u0279"+
		"\u0277\u0001\u0000\u0000\u0000\u0279\u027a\u0001\u0000\u0000\u0000\u027a"+
		"\u0280\u0001\u0000\u0000\u0000\u027b\u027c\u0005\u0007\u0000\u0000\u027c"+
		"\u027d\u0003\u0010\b\u0000\u027d\u027e\u0005\b\u0000\u0000\u027e\u0281"+
		"\u0001\u0000\u0000\u0000\u027f\u0281\u0005\u001e\u0000\u0000\u0280\u027b"+
		"\u0001\u0000\u0000\u0000\u0280\u027f\u0001\u0000\u0000\u0000\u0281S\u0001"+
		"\u0000\u0000\u0000\u0282\u0283\u0005o\u0000\u0000\u0283\u0284\u0005l\u0000"+
		"\u0000\u0284\u0285\u0003H$\u0000\u0285\u0287\u0005F\u0000\u0000\u0286"+
		"\u0288\u0003V+\u0000\u0287\u0286\u0001\u0000\u0000\u0000\u0287\u0288\u0001"+
		"\u0000\u0000\u0000\u0288\u0289\u0001\u0000\u0000\u0000\u0289\u028a\u0003"+
		"^/\u0000\u028aU\u0001\u0000\u0000\u0000\u028b\u028c\u0005 \u0000\u0000"+
		"\u028c\u0292\u0005!\u0000\u0000\u028d\u028e\u0005 \u0000\u0000\u028e\u0292"+
		"\u0005\"\u0000\u0000\u028f\u0290\u0005|\u0000\u0000\u0290\u0292\u0005"+
		"#\u0000\u0000\u0291\u028b\u0001\u0000\u0000\u0000\u0291\u028d\u0001\u0000"+
		"\u0000\u0000\u0291\u028f\u0001\u0000\u0000\u0000\u0292W\u0001\u0000\u0000"+
		"\u0000\u0293\u0298\u0003Z-\u0000\u0294\u0295\u0005\u000e\u0000\u0000\u0295"+
		"\u0297\u0003Z-\u0000\u0296\u0294\u0001\u0000\u0000\u0000\u0297\u029a\u0001"+
		"\u0000\u0000\u0000\u0298\u0296\u0001\u0000\u0000\u0000\u0298\u0299\u0001"+
		"\u0000\u0000\u0000\u0299Y\u0001\u0000\u0000\u0000\u029a\u0298\u0001\u0000"+
		"\u0000\u0000\u029b\u029c\u0005\u0005\u0000\u0000\u029c\u029f\u0003H$\u0000"+
		"\u029d\u029e\u0005F\u0000\u0000\u029e\u02a0\u0003\u00e6s\u0000\u029f\u029d"+
		"\u0001\u0000\u0000\u0000\u029f\u02a0\u0001\u0000\u0000\u0000\u02a0[\u0001"+
		"\u0000\u0000\u0000\u02a1\u02a6\u0003^/\u0000\u02a2\u02a3\u0005\u000e\u0000"+
		"\u0000\u02a3\u02a5\u0003^/\u0000\u02a4\u02a2\u0001\u0000\u0000\u0000\u02a5"+
		"\u02a8\u0001\u0000\u0000\u0000\u02a6\u02a4\u0001\u0000\u0000\u0000\u02a6"+
		"\u02a7\u0001\u0000\u0000\u0000\u02a7]\u0001\u0000\u0000\u0000\u02a8\u02a6"+
		"\u0001\u0000\u0000\u0000\u02a9\u02b0\u0003`0\u0000\u02aa\u02b0\u0003b"+
		"1\u0000\u02ab\u02b0\u0003|>\u0000\u02ac\u02b0\u0003\u0080@\u0000\u02ad"+
		"\u02b0\u0003\u0084B\u0000\u02ae\u02b0\u0003\u0086C\u0000\u02af\u02a9\u0001"+
		"\u0000\u0000\u0000\u02af\u02aa\u0001\u0000\u0000\u0000\u02af\u02ab\u0001"+
		"\u0000\u0000\u0000\u02af\u02ac\u0001\u0000\u0000\u0000\u02af\u02ad\u0001"+
		"\u0000\u0000\u0000\u02af\u02ae\u0001\u0000\u0000\u0000\u02b0_\u0001\u0000"+
		"\u0000\u0000\u02b1\u02ba\u0003x<\u0000\u02b2\u02ba\u0003\u008aE\u0000"+
		"\u02b3\u02ba\u0003\u00d6k\u0000\u02b4\u02ba\u0003\u00d8l\u0000\u02b5\u02ba"+
		"\u0003\u00dam\u0000\u02b6\u02ba\u0003\u00dcn\u0000\u02b7\u02ba\u0003\u00de"+
		"o\u0000\u02b8\u02ba\u0003\u00e0p\u0000\u02b9\u02b1\u0001\u0000\u0000\u0000"+
		"\u02b9\u02b2\u0001\u0000\u0000\u0000\u02b9\u02b3\u0001\u0000\u0000\u0000"+
		"\u02b9\u02b4\u0001\u0000\u0000\u0000\u02b9\u02b5\u0001\u0000\u0000\u0000"+
		"\u02b9\u02b6\u0001\u0000\u0000\u0000\u02b9\u02b7\u0001\u0000\u0000\u0000"+
		"\u02b9\u02b8\u0001\u0000\u0000\u0000\u02baa\u0001\u0000\u0000\u0000\u02bb"+
		"\u02be\u0003d2\u0000\u02bc\u02be\u0003h4\u0000\u02bd\u02bb\u0001\u0000"+
		"\u0000\u0000\u02bd\u02bc\u0001\u0000\u0000\u0000\u02be\u02c7\u0001\u0000"+
		"\u0000\u0000\u02bf\u02c6\u0003d2\u0000\u02c0\u02c6\u0003h4\u0000\u02c1"+
		"\u02c6\u0003l6\u0000\u02c2\u02c6\u0003n7\u0000\u02c3\u02c6\u0003r9\u0000"+
		"\u02c4\u02c6\u0003v;\u0000\u02c5\u02bf\u0001\u0000\u0000\u0000\u02c5\u02c0"+
		"\u0001\u0000\u0000\u0000\u02c5\u02c1\u0001\u0000\u0000\u0000\u02c5\u02c2"+
		"\u0001\u0000\u0000\u0000\u02c5\u02c3\u0001\u0000\u0000\u0000\u02c5\u02c4"+
		"\u0001\u0000\u0000\u0000\u02c6\u02c9\u0001\u0000\u0000\u0000\u02c7\u02c5"+
		"\u0001\u0000\u0000\u0000\u02c7\u02c8\u0001\u0000\u0000\u0000\u02c8\u02ca"+
		"\u0001\u0000\u0000\u0000\u02c9\u02c7\u0001\u0000\u0000\u0000\u02ca\u02cb"+
		"\u0005C\u0000\u0000\u02cb\u02cc\u0003^/\u0000\u02ccc\u0001\u0000\u0000"+
		"\u0000\u02cd\u02ce\u0005=\u0000\u0000\u02ce\u02d3\u0003f3\u0000\u02cf"+
		"\u02d0\u0005\u000e\u0000\u0000\u02d0\u02d2\u0003f3\u0000\u02d1\u02cf\u0001"+
		"\u0000\u0000\u0000\u02d2\u02d5\u0001\u0000\u0000\u0000\u02d3\u02d1\u0001"+
		"\u0000\u0000\u0000\u02d3\u02d4\u0001\u0000\u0000\u0000\u02d4e\u0001\u0000"+
		"\u0000\u0000\u02d5\u02d3\u0001\u0000\u0000\u0000\u02d6\u02d9\u0003\u00c0"+
		"`\u0000\u02d7\u02d8\u0005F\u0000\u0000\u02d8\u02da\u0003\u00e6s\u0000"+
		"\u02d9\u02d7\u0001\u0000\u0000\u0000\u02d9\u02da\u0001\u0000\u0000\u0000"+
		"\u02da\u02dd\u0001\u0000\u0000\u0000\u02db\u02dc\u0005H\u0000\u0000\u02dc"+
		"\u02de\u0005I\u0000\u0000\u02dd\u02db\u0001\u0000\u0000\u0000\u02dd\u02de"+
		"\u0001\u0000\u0000\u0000\u02de\u02e1\u0001\u0000\u0000\u0000\u02df\u02e0"+
		"\u0005G\u0000\u0000\u02e0\u02e2\u0003\u00c0`\u0000\u02e1\u02df\u0001\u0000"+
		"\u0000\u0000\u02e1\u02e2\u0001\u0000\u0000\u0000\u02e2\u02e3\u0001\u0000"+
		"\u0000\u0000\u02e3\u02e4\u0005E\u0000\u0000\u02e4\u02e5\u0003^/\u0000"+
		"\u02e5g\u0001\u0000\u0000\u0000\u02e6\u02e7\u0005>\u0000\u0000\u02e7\u02ec"+
		"\u0003j5\u0000\u02e8\u02e9\u0005\u000e\u0000\u0000\u02e9\u02eb\u0003j"+
		"5\u0000\u02ea\u02e8\u0001\u0000\u0000\u0000\u02eb\u02ee\u0001\u0000\u0000"+
		"\u0000\u02ec\u02ea\u0001\u0000\u0000\u0000\u02ec\u02ed\u0001\u0000\u0000"+
		"\u0000\u02edi\u0001\u0000\u0000\u0000\u02ee\u02ec\u0001\u0000\u0000\u0000"+
		"\u02ef\u02f2\u0003\u00c0`\u0000\u02f0\u02f1\u0005F\u0000\u0000\u02f1\u02f3"+
		"\u0003\u00e6s\u0000\u02f2\u02f0\u0001\u0000\u0000\u0000\u02f2\u02f3\u0001"+
		"\u0000\u0000\u0000\u02f3\u02f4\u0001\u0000\u0000\u0000\u02f4\u02f5\u0005"+
		"\u0006\u0000\u0000\u02f5\u02f6\u0003^/\u0000\u02f6k\u0001\u0000\u0000"+
		"\u0000\u02f7\u02f8\u0005?\u0000\u0000\u02f8\u02f9\u0003^/\u0000\u02f9"+
		"m\u0001\u0000\u0000\u0000\u02fa\u02fb\u0005@\u0000\u0000\u02fb\u02fc\u0005"+
		"A\u0000\u0000\u02fc\u0301\u0003p8\u0000\u02fd\u02fe\u0005\u000e\u0000"+
		"\u0000\u02fe\u0300\u0003p8\u0000\u02ff\u02fd\u0001\u0000\u0000\u0000\u0300"+
		"\u0303\u0001\u0000\u0000\u0000\u0301\u02ff\u0001\u0000\u0000\u0000\u0301"+
		"\u0302\u0001\u0000\u0000\u0000\u0302o\u0001\u0000\u0000\u0000\u0303\u0301"+
		"\u0001\u0000\u0000\u0000\u0304\u030b\u0003\u00c0`\u0000\u0305\u0306\u0005"+
		"F\u0000\u0000\u0306\u0308\u0003\u00e6s\u0000\u0307\u0305\u0001\u0000\u0000"+
		"\u0000\u0307\u0308\u0001\u0000\u0000\u0000\u0308\u0309\u0001\u0000\u0000"+
		"\u0000\u0309\u030a\u0005\u0006\u0000\u0000\u030a\u030c\u0003^/\u0000\u030b"+
		"\u0307\u0001\u0000\u0000\u0000\u030b\u030c\u0001\u0000\u0000\u0000\u030c"+
		"\u030f\u0001\u0000\u0000\u0000\u030d\u030e\u0005Q\u0000\u0000\u030e\u0310"+
		"\u0003\u00f8|\u0000\u030f\u030d\u0001\u0000\u0000\u0000\u030f\u0310\u0001"+
		"\u0000\u0000\u0000\u0310q\u0001\u0000\u0000\u0000\u0311\u0312\u0005B\u0000"+
		"\u0000\u0312\u0317\u0005A\u0000\u0000\u0313\u0314\u0005K\u0000\u0000\u0314"+
		"\u0315\u0005B\u0000\u0000\u0315\u0317\u0005A\u0000\u0000\u0316\u0311\u0001"+
		"\u0000\u0000\u0000\u0316\u0313\u0001\u0000\u0000\u0000\u0317\u0318\u0001"+
		"\u0000\u0000\u0000\u0318\u031d\u0003t:\u0000\u0319\u031a\u0005\u000e\u0000"+
		"\u0000\u031a\u031c\u0003t:\u0000\u031b\u0319\u0001\u0000\u0000\u0000\u031c"+
		"\u031f\u0001\u0000\u0000\u0000\u031d\u031b\u0001\u0000\u0000\u0000\u031d"+
		"\u031e\u0001\u0000\u0000\u0000\u031es\u0001\u0000\u0000\u0000\u031f\u031d"+
		"\u0001\u0000\u0000\u0000\u0320\u0323\u0003^/\u0000\u0321\u0324\u0005L"+
		"\u0000\u0000\u0322\u0324\u0005M\u0000\u0000\u0323\u0321\u0001\u0000\u0000"+
		"\u0000\u0323\u0322\u0001\u0000\u0000\u0000\u0323\u0324\u0001\u0000\u0000"+
		"\u0000\u0324\u032a\u0001\u0000\u0000\u0000\u0325\u0328\u0005I\u0000\u0000"+
		"\u0326\u0329\u0005R\u0000\u0000\u0327\u0329\u0005S\u0000\u0000\u0328\u0326"+
		"\u0001\u0000\u0000\u0000\u0328\u0327\u0001\u0000\u0000\u0000\u0329\u032b"+
		"\u0001\u0000\u0000\u0000\u032a\u0325\u0001\u0000\u0000\u0000\u032a\u032b"+
		"\u0001\u0000\u0000\u0000\u032b\u032e\u0001\u0000\u0000\u0000\u032c\u032d"+
		"\u0005Q\u0000\u0000\u032d\u032f\u0003\u00f8|\u0000\u032e\u032c\u0001\u0000"+
		"\u0000\u0000\u032e\u032f\u0001\u0000\u0000\u0000\u032fu\u0001\u0000\u0000"+
		"\u0000\u0330\u0331\u0005J\u0000\u0000\u0331\u0332\u0003\u00c0`\u0000\u0332"+
		"w\u0001\u0000\u0000\u0000\u0333\u0336\u0005N\u0000\u0000\u0334\u0336\u0005"+
		"O\u0000\u0000\u0335\u0333\u0001\u0000\u0000\u0000\u0335\u0334\u0001\u0000"+
		"\u0000\u0000\u0336\u0337\u0001\u0000\u0000\u0000\u0337\u033c\u0003z=\u0000"+
		"\u0338\u0339\u0005\u000e\u0000\u0000\u0339\u033b\u0003z=\u0000\u033a\u0338"+
		"\u0001\u0000\u0000\u0000\u033b\u033e\u0001\u0000\u0000\u0000\u033c\u033a"+
		"\u0001\u0000\u0000\u0000\u033c\u033d\u0001\u0000\u0000\u0000\u033d\u033f"+
		"\u0001\u0000\u0000\u0000\u033e\u033c\u0001\u0000\u0000\u0000\u033f\u0340"+
		"\u0005P\u0000\u0000\u0340\u0341\u0003^/\u0000\u0341y\u0001\u0000\u0000"+
		"\u0000\u0342\u0345\u0003\u00c0`\u0000\u0343\u0344\u0005F\u0000\u0000\u0344"+
		"\u0346\u0003\u00e6s\u0000\u0345\u0343\u0001\u0000\u0000\u0000\u0345\u0346"+
		"\u0001\u0000\u0000\u0000\u0346\u0347\u0001\u0000\u0000\u0000\u0347\u0348"+
		"\u0005E\u0000\u0000\u0348\u0349\u0003^/\u0000\u0349{\u0001\u0000\u0000"+
		"\u0000\u034a\u034b\u0005T\u0000\u0000\u034b\u034c\u0005\t\u0000\u0000"+
		"\u034c\u034d\u0003\\.\u0000\u034d\u034f\u0005\n\u0000\u0000\u034e\u0350"+
		"\u0003~?\u0000\u034f\u034e\u0001\u0000\u0000\u0000\u0350\u0351\u0001\u0000"+
		"\u0000\u0000\u0351\u034f\u0001\u0000\u0000\u0000\u0351\u0352\u0001\u0000"+
		"\u0000\u0000\u0352\u0353\u0001\u0000\u0000\u0000\u0353\u0354\u0005X\u0000"+
		"\u0000\u0354\u0355\u0005C\u0000\u0000\u0355\u0356\u0003^/\u0000\u0356"+
		"}\u0001\u0000\u0000\u0000\u0357\u0358\u0005U\u0000\u0000\u0358\u035a\u0003"+
		"^/\u0000\u0359\u0357\u0001\u0000\u0000\u0000\u035a\u035b\u0001\u0000\u0000"+
		"\u0000\u035b\u0359\u0001\u0000\u0000\u0000\u035b\u035c\u0001\u0000\u0000"+
		"\u0000\u035c\u035d\u0001\u0000\u0000\u0000\u035d\u035e\u0005C\u0000\u0000"+
		"\u035e\u035f\u0003^/\u0000\u035f\u007f\u0001\u0000\u0000\u0000\u0360\u0361"+
		"\u0005[\u0000\u0000\u0361\u0362\u0005\t\u0000\u0000\u0362\u0363\u0003"+
		"\\.\u0000\u0363\u0365\u0005\n\u0000\u0000\u0364\u0366\u0003\u0082A\u0000"+
		"\u0365\u0364\u0001\u0000\u0000\u0000\u0366\u0367\u0001\u0000\u0000\u0000"+
		"\u0367\u0365\u0001\u0000\u0000\u0000\u0367\u0368\u0001\u0000\u0000\u0000"+
		"\u0368\u0369\u0001\u0000\u0000\u0000\u0369\u036b\u0005X\u0000\u0000\u036a"+
		"\u036c\u0003\u00c0`\u0000\u036b\u036a\u0001\u0000\u0000\u0000\u036b\u036c"+
		"\u0001\u0000\u0000\u0000\u036c\u036d\u0001\u0000\u0000\u0000\u036d\u036e"+
		"\u0005C\u0000\u0000\u036e\u036f\u0003^/\u0000\u036f\u0081\u0001\u0000"+
		"\u0000\u0000\u0370\u0374\u0005U\u0000\u0000\u0371\u0372\u0003\u00c0`\u0000"+
		"\u0372\u0373\u0005F\u0000\u0000\u0373\u0375\u0001\u0000\u0000\u0000\u0374"+
		"\u0371\u0001\u0000\u0000\u0000\u0374\u0375\u0001\u0000\u0000\u0000\u0375"+
		"\u0376\u0001\u0000\u0000\u0000\u0376\u037b\u0003\u00e6s\u0000\u0377\u0378"+
		"\u0005\f\u0000\u0000\u0378\u037a\u0003\u00e6s\u0000\u0379\u0377\u0001"+
		"\u0000\u0000\u0000\u037a\u037d\u0001\u0000\u0000\u0000\u037b\u0379\u0001"+
		"\u0000\u0000\u0000\u037b\u037c\u0001\u0000\u0000\u0000\u037c\u037e\u0001"+
		"\u0000\u0000\u0000\u037d\u037b\u0001\u0000\u0000\u0000\u037e\u037f\u0005"+
		"C\u0000\u0000\u037f\u0380\u0003^/\u0000\u0380\u0083\u0001\u0000\u0000"+
		"\u0000\u0381\u0382\u0005D\u0000\u0000\u0382\u0383\u0005\t\u0000\u0000"+
		"\u0383\u0384\u0003\\.\u0000\u0384\u0385\u0005\n\u0000\u0000\u0385\u0386"+
		"\u0005Y\u0000\u0000\u0386\u0387\u0003^/\u0000\u0387\u0388\u0005Z\u0000"+
		"\u0000\u0388\u0389\u0003^/\u0000\u0389\u0085\u0001\u0000\u0000\u0000\u038a"+
		"\u038b\u0005V\u0000\u0000\u038b\u038c\u0005\u0007\u0000\u0000\u038c\u038d"+
		"\u0003\\.\u0000\u038d\u038f\u0005\b\u0000\u0000\u038e\u0390\u0003\u0088"+
		"D\u0000\u038f\u038e\u0001\u0000\u0000\u0000\u0390\u0391\u0001\u0000\u0000"+
		"\u0000\u0391\u038f\u0001\u0000\u0000\u0000\u0391\u0392\u0001\u0000\u0000"+
		"\u0000\u0392\u0087\u0001\u0000\u0000\u0000\u0393\u0396\u0005W\u0000\u0000"+
		"\u0394\u0397\u0005\u000b\u0000\u0000\u0395\u0397\u0003H$\u0000\u0396\u0394"+
		"\u0001\u0000\u0000\u0000\u0396\u0395\u0001\u0000\u0000\u0000\u0397\u039f"+
		"\u0001\u0000\u0000\u0000\u0398\u039b\u0005\f\u0000\u0000\u0399\u039c\u0005"+
		"\u000b\u0000\u0000\u039a\u039c\u0003H$\u0000\u039b\u0399\u0001\u0000\u0000"+
		"\u0000\u039b\u039a\u0001\u0000\u0000\u0000\u039c\u039e\u0001\u0000\u0000"+
		"\u0000\u039d\u0398\u0001\u0000\u0000\u0000\u039e\u03a1\u0001\u0000\u0000"+
		"\u0000\u039f\u039d\u0001\u0000\u0000\u0000\u039f\u03a0\u0001\u0000\u0000"+
		"\u0000\u03a0\u03a2\u0001\u0000\u0000\u0000\u03a1\u039f\u0001\u0000\u0000"+
		"\u0000\u03a2\u03a3\u0005\u0007\u0000\u0000\u03a3\u03a4\u0003\\.\u0000"+
		"\u03a4\u03a5\u0005\b\u0000\u0000\u03a5\u0089\u0001\u0000\u0000\u0000\u03a6"+
		"\u03ab\u0003\u008cF\u0000\u03a7\u03a8\u0005\\\u0000\u0000\u03a8\u03aa"+
		"\u0003\u008cF\u0000\u03a9\u03a7\u0001\u0000\u0000\u0000\u03aa\u03ad\u0001"+
		"\u0000\u0000\u0000\u03ab\u03a9\u0001\u0000\u0000\u0000\u03ab\u03ac\u0001"+
		"\u0000\u0000\u0000\u03ac\u008b\u0001\u0000\u0000\u0000\u03ad\u03ab\u0001"+
		"\u0000\u0000\u0000\u03ae\u03b3\u0003\u008eG\u0000\u03af\u03b0\u0005]\u0000"+
		"\u0000\u03b0\u03b2\u0003\u008eG\u0000\u03b1\u03af\u0001\u0000\u0000\u0000"+
		"\u03b2\u03b5\u0001\u0000\u0000\u0000\u03b3\u03b1\u0001\u0000\u0000\u0000"+
		"\u03b3\u03b4\u0001\u0000\u0000\u0000\u03b4\u008d\u0001\u0000\u0000\u0000"+
		"\u03b5\u03b3\u0001\u0000\u0000\u0000\u03b6\u03b8\u0005^\u0000\u0000\u03b7"+
		"\u03b6\u0001\u0000\u0000\u0000\u03b7\u03b8\u0001\u0000\u0000\u0000\u03b8"+
		"\u03b9\u0001\u0000\u0000\u0000\u03b9\u03ba\u0003\u0090H\u0000\u03ba\u008f"+
		"\u0001\u0000\u0000\u0000\u03bb\u03be\u0003\u0092I\u0000\u03bc\u03bd\u0007"+
		"\u0003\u0000\u0000\u03bd\u03bf\u0003\u0092I\u0000\u03be\u03bc\u0001\u0000"+
		"\u0000\u0000\u03be\u03bf\u0001\u0000\u0000\u0000\u03bf\u0091\u0001\u0000"+
		"\u0000\u0000\u03c0\u03c5\u0003\u0094J\u0000\u03c1\u03c2\u0005/\u0000\u0000"+
		"\u03c2\u03c4\u0003\u0094J\u0000\u03c3\u03c1\u0001\u0000\u0000\u0000\u03c4"+
		"\u03c7\u0001\u0000\u0000\u0000\u03c5\u03c3\u0001\u0000\u0000\u0000\u03c5"+
		"\u03c6\u0001\u0000\u0000\u0000\u03c6\u0093\u0001\u0000\u0000\u0000\u03c7"+
		"\u03c5\u0001\u0000\u0000\u0000\u03c8\u03cb\u0003\u0096K\u0000\u03c9\u03ca"+
		"\u0005_\u0000\u0000\u03ca\u03cc\u0003\u0096K\u0000\u03cb\u03c9\u0001\u0000"+
		"\u0000\u0000\u03cb\u03cc\u0001\u0000\u0000\u0000\u03cc\u0095\u0001\u0000"+
		"\u0000\u0000\u03cd\u03d2\u0003\u0098L\u0000\u03ce\u03cf\u0007\u0004\u0000"+
		"\u0000\u03cf\u03d1\u0003\u0098L\u0000\u03d0\u03ce\u0001\u0000\u0000\u0000"+
		"\u03d1\u03d4\u0001\u0000\u0000\u0000\u03d2\u03d0\u0001\u0000\u0000\u0000"+
		"\u03d2\u03d3\u0001\u0000\u0000\u0000\u03d3\u0097\u0001\u0000\u0000\u0000"+
		"\u03d4\u03d2\u0001\u0000\u0000\u0000\u03d5\u03da\u0003\u009aM\u0000\u03d6"+
		"\u03d7\u0007\u0005\u0000\u0000\u03d7\u03d9\u0003\u009aM\u0000\u03d8\u03d6"+
		"\u0001\u0000\u0000\u0000\u03d9\u03dc\u0001\u0000\u0000\u0000\u03da\u03d8"+
		"\u0001\u0000\u0000\u0000\u03da\u03db\u0001\u0000\u0000\u0000\u03db\u0099"+
		"\u0001\u0000\u0000\u0000\u03dc\u03da\u0001\u0000\u0000\u0000\u03dd\u03e1"+
		"\u0003\u009cN\u0000\u03de\u03df\u0005`\u0000\u0000\u03df\u03e0\u0005a"+
		"\u0000\u0000\u03e0\u03e2\u0003\u00e6s\u0000\u03e1\u03de\u0001\u0000\u0000"+
		"\u0000\u03e1\u03e2\u0001\u0000\u0000\u0000\u03e2\u009b\u0001\u0000\u0000"+
		"\u0000\u03e3\u03e7\u0003\u009eO\u0000\u03e4\u03e5\u0005c\u0000\u0000\u03e5"+
		"\u03e6\u0005b\u0000\u0000\u03e6\u03e8\u0003\u00e6s\u0000\u03e7\u03e4\u0001"+
		"\u0000\u0000\u0000\u03e7\u03e8\u0001\u0000\u0000\u0000\u03e8\u009d\u0001"+
		"\u0000\u0000\u0000\u03e9\u03ed\u0003\u00a0P\u0000\u03ea\u03eb\u0005d\u0000"+
		"\u0000\u03eb\u03ec\u0005F\u0000\u0000\u03ec\u03ee\u0003\u00e6s\u0000\u03ed"+
		"\u03ea\u0001\u0000\u0000\u0000\u03ed\u03ee\u0001\u0000\u0000\u0000\u03ee"+
		"\u009f\u0001\u0000\u0000\u0000\u03ef\u03f3\u0003\u00a2Q\u0000\u03f0\u03f1"+
		"\u0005f\u0000\u0000\u03f1\u03f2\u0005F\u0000\u0000\u03f2\u03f4\u0003\u00f2"+
		"y\u0000\u03f3\u03f0\u0001\u0000\u0000\u0000\u03f3\u03f4\u0001\u0000\u0000"+
		"\u0000\u03f4\u00a1\u0001\u0000\u0000\u0000\u03f5\u03f9\u0003\u00a4R\u0000"+
		"\u03f6\u03f7\u0005e\u0000\u0000\u03f7\u03f8\u0005F\u0000\u0000\u03f8\u03fa"+
		"\u0003\u00f2y\u0000\u03f9\u03f6\u0001\u0000\u0000\u0000\u03f9\u03fa\u0001"+
		"\u0000\u0000\u0000\u03fa\u00a3\u0001\u0000\u0000\u0000\u03fb\u0404\u0003"+
		"\u00a8T\u0000\u03fc\u03fd\u0005\u0004\u0000\u0000\u03fd\u03fe\u0005-\u0000"+
		"\u0000\u03fe\u03ff\u0001\u0000\u0000\u0000\u03ff\u0400\u0003\u00a6S\u0000"+
		"\u0400\u0401\u0003\u00ccf\u0000\u0401\u0403\u0001\u0000\u0000\u0000\u0402"+
		"\u03fc\u0001\u0000\u0000\u0000\u0403\u0406\u0001\u0000\u0000\u0000\u0404"+
		"\u0402\u0001\u0000\u0000\u0000\u0404\u0405\u0001\u0000\u0000\u0000\u0405"+
		"\u00a5\u0001\u0000\u0000\u0000\u0406\u0404\u0001\u0000\u0000\u0000\u0407"+
		"\u040b\u0003H$\u0000\u0408\u040b\u0003\u00c0`\u0000\u0409\u040b\u0003"+
		"\u00c2a\u0000\u040a\u0407\u0001\u0000\u0000\u0000\u040a\u0408\u0001\u0000"+
		"\u0000\u0000\u040a\u0409\u0001\u0000\u0000\u0000\u040b\u00a7\u0001\u0000"+
		"\u0000\u0000\u040c\u040e\u0007\u0004\u0000\u0000\u040d\u040c\u0001\u0000"+
		"\u0000\u0000\u040e\u0411\u0001\u0000\u0000\u0000\u040f\u040d\u0001\u0000"+
		"\u0000\u0000\u040f\u0410\u0001\u0000\u0000\u0000\u0410\u0412\u0001\u0000"+
		"\u0000\u0000\u0411\u040f\u0001\u0000\u0000\u0000\u0412\u0413\u0003\u00aa"+
		"U\u0000\u0413\u00a9\u0001\u0000\u0000\u0000\u0414\u0418\u0003\u00b0X\u0000"+
		"\u0415\u0418\u0003\u00acV\u0000\u0416\u0418\u0003\u00aeW\u0000\u0417\u0414"+
		"\u0001\u0000\u0000\u0000\u0417\u0415\u0001\u0000\u0000\u0000\u0417\u0416"+
		"\u0001\u0000\u0000\u0000\u0418\u00ab\u0001\u0000\u0000\u0000\u0419\u041a"+
		"\u0005m\u0000\u0000\u041a\u041b\u0005l\u0000\u0000\u041b\u041c\u0003\u00e6"+
		"s\u0000\u041c\u041d\u0005\u0007\u0000\u0000\u041d\u041e\u0003\\.\u0000"+
		"\u041e\u041f\u0005\b\u0000\u0000\u041f\u00ad\u0001\u0000\u0000\u0000\u0420"+
		"\u0421\u0005n\u0000\u0000\u0421\u0422\u0005l\u0000\u0000\u0422\u0423\u0003"+
		"\u00e6s\u0000\u0423\u0424\u0005\u0007\u0000\u0000\u0424\u0425\u0003\\"+
		".\u0000\u0425\u0426\u0005\b\u0000\u0000\u0426\u00af\u0001\u0000\u0000"+
		"\u0000\u0427\u042c\u0003\u00b2Y\u0000\u0428\u0429\u00055\u0000\u0000\u0429"+
		"\u042b\u0003\u00b2Y\u0000\u042a\u0428\u0001\u0000\u0000\u0000\u042b\u042e"+
		"\u0001\u0000\u0000\u0000\u042c\u042a\u0001\u0000\u0000\u0000\u042c\u042d"+
		"\u0001\u0000\u0000\u0000\u042d\u00b1\u0001\u0000\u0000\u0000\u042e\u042c"+
		"\u0001\u0000\u0000\u0000\u042f\u0437\u0003\u00bc^\u0000\u0430\u0436\u0003"+
		"\u00b4Z\u0000\u0431\u0436\u0003\u00b8\\\u0000\u0432\u0436\u0003\u00ba"+
		"]\u0000\u0433\u0436\u0003\u00b6[\u0000\u0434\u0436\u0003\u00ccf\u0000"+
		"\u0435\u0430\u0001\u0000\u0000\u0000\u0435\u0431\u0001\u0000\u0000\u0000"+
		"\u0435\u0432\u0001\u0000\u0000\u0000\u0435\u0433\u0001\u0000\u0000\u0000"+
		"\u0435\u0434\u0001\u0000\u0000\u0000\u0436\u0439\u0001\u0000\u0000\u0000"+
		"\u0437\u0435\u0001\u0000\u0000\u0000\u0437\u0438\u0001\u0000\u0000\u0000"+
		"\u0438\u00b3\u0001\u0000\u0000\u0000\u0439\u0437\u0001\u0000\u0000\u0000"+
		"\u043a\u043b\u00056\u0000\u0000\u043b\u043c\u00056\u0000\u0000\u043c\u043d"+
		"\u0003\\.\u0000\u043d\u043e\u00057\u0000\u0000\u043e\u043f\u00057\u0000"+
		"\u0000\u043f\u00b5\u0001\u0000\u0000\u0000\u0440\u0441\u00056\u0000\u0000"+
		"\u0441\u0442\u00057\u0000\u0000\u0442\u00b7\u0001\u0000\u0000\u0000\u0443"+
		"\u0444\u00056\u0000\u0000\u0444\u0445\u0003\\.\u0000\u0445\u0446\u0005"+
		"7\u0000\u0000\u0446\u00b9\u0001\u0000\u0000\u0000\u0447\u044e\u00058\u0000"+
		"\u0000\u0448\u044f\u0003\u00fc~\u0000\u0449\u044f\u0003\u00fa}\u0000\u044a"+
		"\u044f\u0005\u008e\u0000\u0000\u044b\u044f\u0003\u00c2a\u0000\u044c\u044f"+
		"\u0003\u00c0`\u0000\u044d\u044f\u0003\u00c4b\u0000\u044e\u0448\u0001\u0000"+
		"\u0000\u0000\u044e\u0449\u0001\u0000\u0000\u0000\u044e\u044a\u0001\u0000"+
		"\u0000\u0000\u044e\u044b\u0001\u0000\u0000\u0000\u044e\u044c\u0001\u0000"+
		"\u0000\u0000\u044e\u044d\u0001\u0000\u0000\u0000\u044f\u00bb\u0001\u0000"+
		"\u0000\u0000\u0450\u0460\u0005\u0087\u0000\u0000\u0451\u0460\u0005j\u0000"+
		"\u0000\u0452\u0460\u0005k\u0000\u0000\u0453\u0460\u0005\u0088\u0000\u0000"+
		"\u0454\u0460\u0003\u00fa}\u0000\u0455\u0460\u0003\u00c0`\u0000\u0456\u0460"+
		"\u0003\u00c2a\u0000\u0457\u0460\u0003\u00c4b\u0000\u0458\u0460\u0003\u00e8"+
		"t\u0000\u0459\u0460\u0003\u00cae\u0000\u045a\u0460\u0003\u00c6c\u0000"+
		"\u045b\u0460\u0003\u00c8d\u0000\u045c\u0460\u0003\u00f6{\u0000\u045d\u0460"+
		"\u0003\u00d0h\u0000\u045e\u0460\u0003\u00be_\u0000\u045f\u0450\u0001\u0000"+
		"\u0000\u0000\u045f\u0451\u0001\u0000\u0000\u0000\u045f\u0452\u0001\u0000"+
		"\u0000\u0000\u045f\u0453\u0001\u0000\u0000\u0000\u045f\u0454\u0001\u0000"+
		"\u0000\u0000\u045f\u0455\u0001\u0000\u0000\u0000\u045f\u0456\u0001\u0000"+
		"\u0000\u0000\u045f\u0457\u0001\u0000\u0000\u0000\u045f\u0458\u0001\u0000"+
		"\u0000\u0000\u045f\u0459\u0001\u0000\u0000\u0000\u045f\u045a\u0001\u0000"+
		"\u0000\u0000\u045f\u045b\u0001\u0000\u0000\u0000\u045f\u045c\u0001\u0000"+
		"\u0000\u0000\u045f\u045d\u0001\u0000\u0000\u0000\u045f\u045e\u0001\u0000"+
		"\u0000\u0000\u0460\u00bd\u0001\u0000\u0000\u0000\u0461\u0462\u0005\u0007"+
		"\u0000\u0000\u0462\u0463\u0003\u000e\u0007\u0000\u0463\u0464\u0005\b\u0000"+
		"\u0000\u0464\u00bf\u0001\u0000\u0000\u0000\u0465\u0466\u0005\u0005\u0000"+
		"\u0000\u0466\u0467\u0003H$\u0000\u0467\u00c1\u0001\u0000\u0000\u0000\u0468"+
		"\u046a\u0005\t\u0000\u0000\u0469\u046b\u0003\\.\u0000\u046a\u0469\u0001"+
		"\u0000\u0000\u0000\u046a\u046b\u0001\u0000\u0000\u0000\u046b\u046c\u0001"+
		"\u0000\u0000\u0000\u046c\u046d\u0005\n\u0000\u0000\u046d\u00c3\u0001\u0000"+
		"\u0000\u0000\u046e\u046f\u00059\u0000\u0000\u046f\u00c5\u0001\u0000\u0000"+
		"\u0000\u0470\u0471\u0005\u0010\u0000\u0000\u0471\u0472\u0005\u0007\u0000"+
		"\u0000\u0472\u0473\u0003\\.\u0000\u0473\u0474\u0005\b\u0000\u0000\u0474"+
		"\u00c7\u0001\u0000\u0000\u0000\u0475\u0476\u0005i\u0000\u0000\u0476\u0477"+
		"\u0005\u0007\u0000\u0000\u0477\u0478\u0003\\.\u0000\u0478\u0479\u0005"+
		"\b\u0000\u0000\u0479\u00c9\u0001\u0000\u0000\u0000\u047a\u047b\u0003H"+
		"$\u0000\u047b\u047c\u0003\u00ccf\u0000\u047c\u00cb\u0001\u0000\u0000\u0000"+
		"\u047d\u0484\u0005\t\u0000\u0000\u047e\u0480\u0003\u00ceg\u0000\u047f"+
		"\u0481\u0005\u000e\u0000\u0000\u0480\u047f\u0001\u0000\u0000\u0000\u0480"+
		"\u0481\u0001\u0000\u0000\u0000\u0481\u0483\u0001\u0000\u0000\u0000\u0482"+
		"\u047e\u0001\u0000\u0000\u0000\u0483\u0486\u0001\u0000\u0000\u0000\u0484"+
		"\u0482\u0001\u0000\u0000\u0000\u0484\u0485\u0001\u0000\u0000\u0000\u0485"+
		"\u0487\u0001\u0000\u0000\u0000\u0486\u0484\u0001\u0000\u0000\u0000\u0487"+
		"\u0488\u0005\n\u0000\u0000\u0488\u00cd\u0001\u0000\u0000\u0000\u0489\u048c"+
		"\u0003^/\u0000\u048a\u048c\u0005\u0086\u0000\u0000\u048b\u0489\u0001\u0000"+
		"\u0000\u0000\u048b\u048a\u0001\u0000\u0000\u0000\u048c\u00cf\u0001\u0000"+
		"\u0000\u0000\u048d\u0490\u0003\u00d2i\u0000\u048e\u0490\u0003\u00d4j\u0000"+
		"\u048f\u048d\u0001\u0000\u0000\u0000\u048f\u048e\u0001\u0000\u0000\u0000"+
		"\u0490\u00d1\u0001\u0000\u0000\u0000\u0491\u0492\u0003H$\u0000\u0492\u0493"+
		"\u0005:\u0000\u0000\u0493\u0494\u0005\u0088\u0000\u0000\u0494\u00d3\u0001"+
		"\u0000\u0000\u0000\u0495\u0496\u00032\u0019\u0000\u0496\u0497\u0005\u001f"+
		"\u0000\u0000\u0497\u0499\u0005\t\u0000\u0000\u0498\u049a\u0003X,\u0000"+
		"\u0499\u0498\u0001\u0000\u0000\u0000\u0499\u049a\u0001\u0000\u0000\u0000"+
		"\u049a\u049b\u0001\u0000\u0000\u0000\u049b\u049e\u0005\n\u0000\u0000\u049c"+
		"\u049d\u0005F\u0000\u0000\u049d\u049f\u0003\u00e6s\u0000\u049e\u049c\u0001"+
		"\u0000\u0000\u0000\u049e\u049f\u0001\u0000\u0000\u0000\u049f\u04a0\u0001"+
		"\u0000\u0000\u0000\u04a0\u04a1\u0005\u0007\u0000\u0000\u04a1\u04a2\u0003"+
		"\u0010\b\u0000\u04a2\u04a3\u0005\b\u0000\u0000\u04a3\u00d5\u0001\u0000"+
		"\u0000\u0000\u04a4\u04a5\u0005s\u0000\u0000\u04a5\u04a6\u0005|\u0000\u0000"+
		"\u04a6\u04a7\u0003^/\u0000\u04a7\u04a8\u0005z\u0000\u0000\u04a8\u04ac"+
		"\u0003^/\u0000\u04a9\u04aa\u0005G\u0000\u0000\u04aa\u04ab\u0005~\u0000"+
		"\u0000\u04ab\u04ad\u0003^/\u0000\u04ac\u04a9\u0001\u0000\u0000\u0000\u04ac"+
		"\u04ad\u0001\u0000\u0000\u0000\u04ad\u04bc\u0001\u0000\u0000\u0000\u04ae"+
		"\u04af\u0005s\u0000\u0000\u04af\u04b0\u0005|\u0000\u0000\u04b0\u04b5\u0003"+
		"\u00f4z\u0000\u04b1\u04b2\u0005\u000e\u0000\u0000\u04b2\u04b4\u0003\u00f4"+
		"z\u0000\u04b3\u04b1\u0001\u0000\u0000\u0000\u04b4\u04b7\u0001\u0000\u0000"+
		"\u0000\u04b5\u04b3\u0001\u0000\u0000\u0000\u04b5\u04b6\u0001\u0000\u0000"+
		"\u0000\u04b6\u04b8\u0001\u0000\u0000\u0000\u04b7\u04b5\u0001\u0000\u0000"+
		"\u0000\u04b8\u04b9\u0005z\u0000\u0000\u04b9\u04ba\u0003^/\u0000\u04ba"+
		"\u04bc\u0001\u0000\u0000\u0000\u04bb\u04a4\u0001\u0000\u0000\u0000\u04bb"+
		"\u04ae\u0001\u0000\u0000\u0000\u04bc\u00d7\u0001\u0000\u0000\u0000\u04bd"+
		"\u04be\u0005t\u0000\u0000\u04be\u04bf\u0005|\u0000\u0000\u04bf\u04c0\u0003"+
		"\u00e2q\u0000\u04c0\u00d9\u0001\u0000\u0000\u0000\u04c1\u04c2\u0005u\u0000"+
		"\u0000\u04c2\u04c3\u0005|\u0000\u0000\u04c3\u04c4\u0003\u00e2q\u0000\u04c4"+
		"\u04c5\u0005F\u0000\u0000\u04c5\u04c6\u0003^/\u0000\u04c6\u00db\u0001"+
		"\u0000\u0000\u0000\u04c7\u04c8\u0005v\u0000\u0000\u04c8\u04c9\u0005{\u0000"+
		"\u0000\u04c9\u04ca\u0005a\u0000\u0000\u04ca\u04cb\u0005|\u0000\u0000\u04cb"+
		"\u04cc\u0003\u00e2q\u0000\u04cc\u04cd\u0005}\u0000\u0000\u04cd\u04ce\u0003"+
		"^/\u0000\u04ce\u00dd\u0001\u0000\u0000\u0000\u04cf\u04d0\u0005w\u0000"+
		"\u0000\u04d0\u04d5\u0003\u00e4r\u0000\u04d1\u04d2\u0005\u000e\u0000\u0000"+
		"\u04d2\u04d4\u0003\u00e4r\u0000\u04d3\u04d1\u0001\u0000\u0000\u0000\u04d4"+
		"\u04d7\u0001\u0000\u0000\u0000\u04d5\u04d3\u0001\u0000\u0000\u0000\u04d5"+
		"\u04d6\u0001\u0000\u0000\u0000\u04d6\u04d8\u0001\u0000\u0000\u0000\u04d7"+
		"\u04d5\u0001\u0000\u0000\u0000\u04d8\u04d9\u0005x\u0000\u0000\u04d9\u04da"+
		"\u0003^/\u0000\u04da\u04db\u0005C\u0000\u0000\u04db\u04dc\u0003^/\u0000"+
		"\u04dc\u00df\u0001\u0000\u0000\u0000\u04dd\u04de\u0005y\u0000\u0000\u04de"+
		"\u04df\u0005|\u0000\u0000\u04df\u04e0\u0003^/\u0000\u04e0\u04e1\u0005"+
		"z\u0000\u0000\u04e1\u04e2\u0003^/\u0000\u04e2\u00e1\u0001\u0000\u0000"+
		"\u0000\u04e3\u04e6\u0003\u00bc^\u0000\u04e4\u04e7\u0003\u00b4Z\u0000\u04e5"+
		"\u04e7\u0003\u00ba]\u0000\u04e6\u04e4\u0001\u0000\u0000\u0000\u04e6\u04e5"+
		"\u0001\u0000\u0000\u0000\u04e7\u04e8\u0001\u0000\u0000\u0000\u04e8\u04e6"+
		"\u0001\u0000\u0000\u0000\u04e8\u04e9\u0001\u0000\u0000\u0000\u04e9\u00e3"+
		"\u0001\u0000\u0000\u0000\u04ea\u04eb\u0003\u00c0`\u0000\u04eb\u04ec\u0005"+
		"\u0006\u0000\u0000\u04ec\u04ed\u0003^/\u0000\u04ed\u00e5\u0001\u0000\u0000"+
		"\u0000\u04ee\u04ef\u0005\t\u0000\u0000\u04ef\u04f7\u0005\n\u0000\u0000"+
		"\u04f0\u04f4\u0003\u00eau\u0000\u04f1\u04f5\u0005\u0086\u0000\u0000\u04f2"+
		"\u04f5\u0005\u000b\u0000\u0000\u04f3\u04f5\u00050\u0000\u0000\u04f4\u04f1"+
		"\u0001\u0000\u0000\u0000\u04f4\u04f2\u0001\u0000\u0000\u0000\u04f4\u04f3"+
		"\u0001\u0000\u0000\u0000\u04f4\u04f5\u0001\u0000\u0000\u0000\u04f5\u04f7"+
		"\u0001\u0000\u0000\u0000\u04f6\u04ee\u0001\u0000\u0000\u0000\u04f6\u04f0"+
		"\u0001\u0000\u0000\u0000\u04f7\u00e7\u0001\u0000\u0000\u0000\u04f8\u0501"+
		"\u0005\u0007\u0000\u0000\u04f9\u04fe\u0003\u00f4z\u0000\u04fa\u04fb\u0005"+
		"\u000e\u0000\u0000\u04fb\u04fd\u0003\u00f4z\u0000\u04fc\u04fa\u0001\u0000"+
		"\u0000\u0000\u04fd\u0500\u0001\u0000\u0000\u0000\u04fe\u04fc\u0001\u0000"+
		"\u0000\u0000\u04fe\u04ff\u0001\u0000\u0000\u0000\u04ff\u0502\u0001\u0000"+
		"\u0000\u0000\u0500\u04fe\u0001\u0000\u0000\u0000\u0501\u04f9\u0001\u0000"+
		"\u0000\u0000\u0501\u0502\u0001\u0000\u0000\u0000\u0502\u0503\u0001\u0000"+
		"\u0000\u0000\u0503\u0509\u0005\b\u0000\u0000\u0504\u0505\u0005;\u0000"+
		"\u0000\u0505\u0506\u0003\\.\u0000\u0506\u0507\u0005<\u0000\u0000\u0507"+
		"\u0509\u0001\u0000\u0000\u0000\u0508\u04f8\u0001\u0000\u0000\u0000\u0508"+
		"\u0504\u0001\u0000\u0000\u0000\u0509\u00e9\u0001\u0000\u0000\u0000\u050a"+
		"\u050e\u0003H$\u0000\u050b\u050e\u0005\u0087\u0000\u0000\u050c\u050e\u0003"+
		"\u00ecv\u0000\u050d\u050a\u0001\u0000\u0000\u0000\u050d\u050b\u0001\u0000"+
		"\u0000\u0000\u050d\u050c\u0001\u0000\u0000\u0000\u050e\u00eb\u0001\u0000"+
		"\u0000\u0000\u050f\u0512\u0003\u00eew\u0000\u0510\u0512\u0003\u00f0x\u0000"+
		"\u0511\u050f\u0001\u0000\u0000\u0000\u0511\u0510\u0001\u0000\u0000\u0000"+
		"\u0512\u00ed\u0001\u0000\u0000\u0000\u0513\u0514\u0005\u001f\u0000\u0000"+
		"\u0514\u0515\u0005\t\u0000\u0000\u0515\u0516\u0005\u000b\u0000\u0000\u0516"+
		"\u0517\u0005\n\u0000\u0000\u0517\u00ef\u0001\u0000\u0000\u0000\u0518\u0519"+
		"\u0005\u001f\u0000\u0000\u0519\u0522\u0005\t\u0000\u0000\u051a\u051f\u0003"+
		"\u00e6s\u0000\u051b\u051c\u0005\u000e\u0000\u0000\u051c\u051e\u0003\u00e6"+
		"s\u0000\u051d\u051b\u0001\u0000\u0000\u0000\u051e\u0521\u0001\u0000\u0000"+
		"\u0000\u051f\u051d\u0001\u0000\u0000\u0000\u051f\u0520\u0001\u0000\u0000"+
		"\u0000\u0520\u0523\u0001\u0000\u0000\u0000\u0521\u051f\u0001\u0000\u0000"+
		"\u0000\u0522\u051a\u0001\u0000\u0000\u0000\u0522\u0523\u0001\u0000\u0000"+
		"\u0000\u0523\u0524\u0001\u0000\u0000\u0000\u0524\u0525\u0005\n\u0000\u0000"+
		"\u0525\u0526\u0005F\u0000\u0000\u0526\u0527\u0003\u00e6s\u0000\u0527\u00f1"+
		"\u0001\u0000\u0000\u0000\u0528\u052a\u0003\u00eau\u0000\u0529\u052b\u0005"+
		"\u0086\u0000\u0000\u052a\u0529\u0001\u0000\u0000\u0000\u052a\u052b\u0001"+
		"\u0000\u0000\u0000\u052b\u00f3\u0001\u0000\u0000\u0000\u052c\u052f\u0003"+
		"^/\u0000\u052d\u052f\u0005\u008e\u0000\u0000\u052e\u052c\u0001\u0000\u0000"+
		"\u0000\u052e\u052d\u0001\u0000\u0000\u0000\u052f\u0530\u0001\u0000\u0000"+
		"\u0000\u0530\u0531\u0007\u0006\u0000\u0000\u0531\u0532\u0003^/\u0000\u0532"+
		"\u00f5\u0001\u0000\u0000\u0000\u0533\u0535\u00056\u0000\u0000\u0534\u0536"+
		"\u0003\\.\u0000\u0535\u0534\u0001\u0000\u0000\u0000\u0535\u0536\u0001"+
		"\u0000\u0000\u0000\u0536\u0537\u0001\u0000\u0000\u0000\u0537\u0538\u0005"+
		"7\u0000\u0000\u0538\u00f7\u0001\u0000\u0000\u0000\u0539\u053a\u0003\u00fa"+
		"}\u0000\u053a\u00f9\u0001\u0000\u0000\u0000\u053b\u053c\u0005\u0085\u0000"+
		"\u0000\u053c\u00fb\u0001\u0000\u0000\u0000\u053d\u053e\u0007\u0007\u0000"+
		"\u0000\u053e\u00fd\u0001\u0000\u0000\u0000\u0084\u0106\u010a\u011a\u0120"+
		"\u0128\u0130\u0138\u0147\u0165\u016d\u016f\u0185\u018f\u0199\u019e\u01a3"+
		"\u01a7\u01b3\u01b7\u01c0\u01c7\u01d5\u01d9\u01de\u01e8\u01f0\u01f4\u0200"+
		"\u020c\u0222\u022a\u022f\u0232\u0236\u023f\u0248\u024b\u0253\u025a\u025c"+
		"\u0263\u026a\u026c\u0274\u0279\u0280\u0287\u0291\u0298\u029f\u02a6\u02af"+
		"\u02b9\u02bd\u02c5\u02c7\u02d3\u02d9\u02dd\u02e1\u02ec\u02f2\u0301\u0307"+
		"\u030b\u030f\u0316\u031d\u0323\u0328\u032a\u032e\u0335\u033c\u0345\u0351"+
		"\u035b\u0367\u036b\u0374\u037b\u0391\u0396\u039b\u039f\u03ab\u03b3\u03b7"+
		"\u03be\u03c5\u03cb\u03d2\u03da\u03e1\u03e7\u03ed\u03f3\u03f9\u0404\u040a"+
		"\u040f\u0417\u042c\u0435\u0437\u044e\u045f\u046a\u0480\u0484\u048b\u048f"+
		"\u0499\u049e\u04ac\u04b5\u04bb\u04d5\u04e6\u04e8\u04f4\u04f6\u04fe\u0501"+
		"\u0508\u050d\u0511\u051f\u0522\u052a\u052e\u0535";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}