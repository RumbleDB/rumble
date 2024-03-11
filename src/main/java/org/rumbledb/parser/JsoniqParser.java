// Generated from ./src/main/java/org/rumbledb/parser/Jsoniq.g4 by ANTLR 4.9.3

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
		STRING=127, ArgumentPlaceholder=128, NullLiteral=129, Literal=130, NumericLiteral=131, 
		IntegerLiteral=132, DecimalLiteral=133, DoubleLiteral=134, WS=135, NCName=136, 
		XQComment=137, ContentChar=138, Kbreak=139, Kloop=140, Kcontinue=141, 
		Kexit=142, Kreturning=143, Kwhile=144;
	public static final int
		RULE_moduleAndThisIsIt = 0, RULE_module = 1, RULE_mainModule = 2, RULE_libraryModule = 3, 
		RULE_prolog = 4, RULE_program = 5, RULE_setter = 6, RULE_namespaceDecl = 7, 
		RULE_annotatedDecl = 8, RULE_defaultCollationDecl = 9, RULE_orderingModeDecl = 10, 
		RULE_emptyOrderDecl = 11, RULE_decimalFormatDecl = 12, RULE_qname = 13, 
		RULE_dfPropertyName = 14, RULE_moduleImport = 15, RULE_varDecl = 16, RULE_contextItemDecl = 17, 
		RULE_functionDecl = 18, RULE_typeDecl = 19, RULE_schemaLanguage = 20, 
		RULE_paramList = 21, RULE_param = 22, RULE_expr = 23, RULE_exprSingle = 24, 
		RULE_exprSimple = 25, RULE_flowrExpr = 26, RULE_forClause = 27, RULE_forVar = 28, 
		RULE_letClause = 29, RULE_letVar = 30, RULE_whereClause = 31, RULE_groupByClause = 32, 
		RULE_groupByVar = 33, RULE_orderByClause = 34, RULE_orderByExpr = 35, 
		RULE_countClause = 36, RULE_quantifiedExpr = 37, RULE_quantifiedExprVar = 38, 
		RULE_switchExpr = 39, RULE_switchCaseClause = 40, RULE_typeSwitchExpr = 41, 
		RULE_caseClause = 42, RULE_ifExpr = 43, RULE_tryCatchExpr = 44, RULE_catchClause = 45, 
		RULE_orExpr = 46, RULE_andExpr = 47, RULE_notExpr = 48, RULE_comparisonExpr = 49, 
		RULE_stringConcatExpr = 50, RULE_rangeExpr = 51, RULE_additiveExpr = 52, 
		RULE_multiplicativeExpr = 53, RULE_instanceOfExpr = 54, RULE_isStaticallyExpr = 55, 
		RULE_treatExpr = 56, RULE_castableExpr = 57, RULE_castExpr = 58, RULE_arrowExpr = 59, 
		RULE_arrowFunctionSpecifier = 60, RULE_unaryExpr = 61, RULE_valueExpr = 62, 
		RULE_validateExpr = 63, RULE_annotateExpr = 64, RULE_simpleMapExpr = 65, 
		RULE_postFixExpr = 66, RULE_arrayLookup = 67, RULE_arrayUnboxing = 68, 
		RULE_predicate = 69, RULE_objectLookup = 70, RULE_primaryExpr = 71, RULE_blockExpr = 72, 
		RULE_varRef = 73, RULE_parenthesizedExpr = 74, RULE_contextItemExpr = 75, 
		RULE_orderedExpr = 76, RULE_unorderedExpr = 77, RULE_functionCall = 78, 
		RULE_argumentList = 79, RULE_argument = 80, RULE_functionItemExpr = 81, 
		RULE_namedFunctionRef = 82, RULE_inlineFunctionExpr = 83, RULE_insertExpr = 84, 
		RULE_deleteExpr = 85, RULE_renameExpr = 86, RULE_replaceExpr = 87, RULE_transformExpr = 88, 
		RULE_appendExpr = 89, RULE_updateLocator = 90, RULE_copyDecl = 91, RULE_statements = 92, 
		RULE_statementsAndExpr = 93, RULE_statementsAndOptionalExpr = 94, RULE_statement = 95, 
		RULE_applyStatement = 96, RULE_assignStatement = 97, RULE_blockStatement = 98, 
		RULE_breakStatement = 99, RULE_continueStatement = 100, RULE_exitStatement = 101, 
		RULE_flowrStatement = 102, RULE_ifStatement = 103, RULE_switchStatement = 104, 
		RULE_switchCaseStatement = 105, RULE_tryCatchStatement = 106, RULE_catchCaseStatement = 107, 
		RULE_typeSwitchStatement = 108, RULE_caseStatement = 109, RULE_annotation = 110, 
		RULE_annotations = 111, RULE_varDeclStatement = 112, RULE_varDeclOther = 113, 
		RULE_whileStatement = 114, RULE_sequenceType = 115, RULE_objectConstructor = 116, 
		RULE_itemType = 117, RULE_functionTest = 118, RULE_anyFunctionTest = 119, 
		RULE_typedFunctionTest = 120, RULE_singleType = 121, RULE_pairConstructor = 122, 
		RULE_arrayConstructor = 123, RULE_uriLiteral = 124, RULE_stringLiteral = 125, 
		RULE_keyWords = 126;
	private static String[] makeRuleNames() {
		return new String[] {
			"moduleAndThisIsIt", "module", "mainModule", "libraryModule", "prolog", 
			"program", "setter", "namespaceDecl", "annotatedDecl", "defaultCollationDecl", 
			"orderingModeDecl", "emptyOrderDecl", "decimalFormatDecl", "qname", "dfPropertyName", 
			"moduleImport", "varDecl", "contextItemDecl", "functionDecl", "typeDecl", 
			"schemaLanguage", "paramList", "param", "expr", "exprSingle", "exprSimple", 
			"flowrExpr", "forClause", "forVar", "letClause", "letVar", "whereClause", 
			"groupByClause", "groupByVar", "orderByClause", "orderByExpr", "countClause", 
			"quantifiedExpr", "quantifiedExprVar", "switchExpr", "switchCaseClause", 
			"typeSwitchExpr", "caseClause", "ifExpr", "tryCatchExpr", "catchClause", 
			"orExpr", "andExpr", "notExpr", "comparisonExpr", "stringConcatExpr", 
			"rangeExpr", "additiveExpr", "multiplicativeExpr", "instanceOfExpr", 
			"isStaticallyExpr", "treatExpr", "castableExpr", "castExpr", "arrowExpr", 
			"arrowFunctionSpecifier", "unaryExpr", "valueExpr", "validateExpr", "annotateExpr", 
			"simpleMapExpr", "postFixExpr", "arrayLookup", "arrayUnboxing", "predicate", 
			"objectLookup", "primaryExpr", "blockExpr", "varRef", "parenthesizedExpr", 
			"contextItemExpr", "orderedExpr", "unorderedExpr", "functionCall", "argumentList", 
			"argument", "functionItemExpr", "namedFunctionRef", "inlineFunctionExpr", 
			"insertExpr", "deleteExpr", "renameExpr", "replaceExpr", "transformExpr", 
			"appendExpr", "updateLocator", "copyDecl", "statements", "statementsAndExpr", 
			"statementsAndOptionalExpr", "statement", "applyStatement", "assignStatement", 
			"blockStatement", "breakStatement", "continueStatement", "exitStatement", 
			"flowrStatement", "ifStatement", "switchStatement", "switchCaseStatement", 
			"tryCatchStatement", "catchCaseStatement", "typeSwitchStatement", "caseStatement", 
			"annotation", "annotations", "varDeclStatement", "varDeclOther", "whileStatement", 
			"sequenceType", "objectConstructor", "itemType", "functionTest", "anyFunctionTest", 
			"typedFunctionTest", "singleType", "pairConstructor", "arrayConstructor", 
			"uriLiteral", "stringLiteral", "keyWords"
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
			"'idiv'", "'mod'", "'!'", "'['", "']'", "'.'", "'$$'", "'#'", "'%'", 
			"'{|'", "'|}'", "'for'", "'let'", "'where'", "'group'", "'by'", "'order'", 
			"'return'", "'if'", "'in'", "'as'", "'at'", "'allowing'", "'empty'", 
			"'count'", "'stable'", "'ascending'", "'descending'", "'some'", "'every'", 
			"'satisfies'", "'collation'", "'greatest'", "'least'", "'switch'", "'case'", 
			"'try'", "'catch'", "'default'", "'then'", "'else'", "'typeswitch'", 
			"'or'", "'and'", "'not'", "'to'", "'instance'", "'of'", "'statically'", 
			"'is'", "'treat'", "'cast'", "'castable'", "'version'", "'jsoniq'", "'unordered'", 
			"'true'", "'false'", "'type'", "'validate'", "'annotate'", "'declare'", 
			"'context'", "'item'", "'variable'", "'insert'", "'delete'", "'rename'", 
			"'replace'", "'copy'", "'modify'", "'append'", "'into'", "'value'", "'json'", 
			"'with'", "'position'", null, "'?'", "'null'", null, null, null, null, 
			null, null, null, null, null, "'break'", "'loop'", "'continue'", "'exit'", 
			"'returning'", "'while'"
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
			"Kwith", "Kposition", "STRING", "ArgumentPlaceholder", "NullLiteral", 
			"Literal", "NumericLiteral", "IntegerLiteral", "DecimalLiteral", "DoubleLiteral", 
			"WS", "NCName", "XQComment", "ContentChar", "Kbreak", "Kloop", "Kcontinue", 
			"Kexit", "Kreturning", "Kwhile"
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
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
			case Kbreak:
			case Kcontinue:
			case Kexit:
			case Kwhile:
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
		enterRule(_localctx, 12, RULE_setter);
		try {
			setState(305);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(301);
				defaultCollationDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
				orderingModeDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(303);
				emptyOrderDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(304);
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
		enterRule(_localctx, 14, RULE_namespaceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			match(Kdeclare);
			setState(308);
			match(T__2);
			setState(309);
			match(NCName);
			setState(310);
			match(T__3);
			setState(311);
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
		enterRule(_localctx, 16, RULE_annotatedDecl);
		try {
			setState(317);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(313);
				functionDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(314);
				varDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(315);
				typeDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(316);
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
		enterRule(_localctx, 18, RULE_defaultCollationDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			match(Kdeclare);
			setState(320);
			match(Kdefault);
			setState(321);
			match(Kcollation);
			setState(322);
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
		enterRule(_localctx, 20, RULE_orderingModeDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			match(Kdeclare);
			setState(325);
			match(T__4);
			setState(326);
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
		enterRule(_localctx, 22, RULE_emptyOrderDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			match(Kdeclare);
			setState(329);
			match(Kdefault);
			setState(330);
			match(Korder);
			setState(331);
			match(Kempty);
			{
			setState(332);
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
		enterRule(_localctx, 24, RULE_decimalFormatDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(334);
			match(Kdeclare);
			setState(339);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				{
				{
				setState(335);
				match(T__6);
				setState(336);
				qname();
				}
				}
				break;
			case Kdefault:
				{
				{
				setState(337);
				match(Kdefault);
				setState(338);
				match(T__6);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17))) != 0)) {
				{
				{
				setState(341);
				dfPropertyName();
				setState(342);
				match(T__3);
				setState(343);
				stringLiteral();
				}
				}
				setState(349);
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
		enterRule(_localctx, 26, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(352);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NCName:
					{
					setState(350);
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
					setState(351);
					((QnameContext)_localctx).nskw = keyWords();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(354);
				match(T__7);
				}
				break;
			}
			setState(359);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NCName:
				{
				setState(357);
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
				setState(358);
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
		enterRule(_localctx, 28, RULE_dfPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(361);
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
		enterRule(_localctx, 30, RULE_moduleImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			match(T__18);
			setState(364);
			match(T__1);
			setState(368);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(365);
				match(T__2);
				setState(366);
				((ModuleImportContext)_localctx).prefix = match(NCName);
				setState(367);
				match(T__3);
				}
			}

			setState(370);
			((ModuleImportContext)_localctx).targetNamespace = uriLiteral();
			setState(380);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(371);
				match(Kat);
				setState(372);
				uriLiteral();
				setState(377);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(373);
					match(T__19);
					setState(374);
					uriLiteral();
					}
					}
					setState(379);
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
		enterRule(_localctx, 32, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			match(Kdeclare);
			setState(383);
			annotations();
			setState(384);
			match(Kvariable);
			setState(385);
			varRef();
			setState(388);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(386);
				match(Kas);
				setState(387);
				sequenceType();
				}
			}

			setState(397);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				{
				setState(390);
				match(T__20);
				setState(391);
				exprSingle();
				}
				}
				break;
			case T__21:
				{
				{
				setState(392);
				((VarDeclContext)_localctx).external = match(T__21);
				setState(395);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__20) {
					{
					setState(393);
					match(T__20);
					setState(394);
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
		enterRule(_localctx, 34, RULE_contextItemDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			match(Kdeclare);
			setState(400);
			match(Kcontext);
			setState(401);
			match(Kitem);
			setState(404);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(402);
				match(Kas);
				setState(403);
				sequenceType();
				}
			}

			setState(413);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				{
				setState(406);
				match(T__20);
				setState(407);
				exprSingle();
				}
				}
				break;
			case T__21:
				{
				{
				setState(408);
				((ContextItemDeclContext)_localctx).external = match(T__21);
				setState(411);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__20) {
					{
					setState(409);
					match(T__20);
					setState(410);
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
		enterRule(_localctx, 36, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(415);
			match(Kdeclare);
			setState(416);
			annotations();
			setState(417);
			match(T__22);
			setState(418);
			((FunctionDeclContext)_localctx).fn_name = qname();
			setState(419);
			match(T__23);
			setState(421);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(420);
				paramList();
				}
			}

			setState(423);
			match(T__24);
			setState(426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(424);
				match(Kas);
				setState(425);
				((FunctionDeclContext)_localctx).return_type = sequenceType();
				}
			}

			setState(433);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__25:
				{
				setState(428);
				match(T__25);
				{
				setState(429);
				((FunctionDeclContext)_localctx).fn_body = statementsAndOptionalExpr();
				}
				setState(430);
				match(T__26);
				}
				break;
			case T__21:
				{
				setState(432);
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
		enterRule(_localctx, 38, RULE_typeDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
			match(Kdeclare);
			setState(436);
			match(Ktype);
			setState(437);
			((TypeDeclContext)_localctx).type_name = qname();
			setState(438);
			match(Kas);
			setState(440);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(439);
				((TypeDeclContext)_localctx).schema = schemaLanguage();
				}
				break;
			}
			setState(442);
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
		enterRule(_localctx, 40, RULE_schemaLanguage);
		try {
			setState(450);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(444);
				match(T__27);
				setState(445);
				match(T__28);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(446);
				match(T__27);
				setState(447);
				match(T__29);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(448);
				match(Kjson);
				setState(449);
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
		enterRule(_localctx, 42, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(452);
			param();
			setState(457);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(453);
				match(T__19);
				setState(454);
				param();
				}
				}
				setState(459);
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
		enterRule(_localctx, 44, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(460);
			match(T__31);
			setState(461);
			qname();
			setState(464);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(462);
				match(Kas);
				setState(463);
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
		enterRule(_localctx, 46, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
			exprSingle();
			setState(471);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(467);
				match(T__19);
				setState(468);
				exprSingle();
				}
				}
				setState(473);
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
		enterRule(_localctx, 48, RULE_exprSingle);
		try {
			setState(480);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(474);
				exprSimple();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(475);
				flowrExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(476);
				switchExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(477);
				typeSwitchExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(478);
				ifExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(479);
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
		enterRule(_localctx, 50, RULE_exprSimple);
		try {
			setState(490);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(482);
				quantifiedExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(483);
				orExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(484);
				insertExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(485);
				deleteExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(486);
				renameExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(487);
				replaceExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(488);
				transformExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(489);
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
		enterRule(_localctx, 52, RULE_flowrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(494);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(492);
				((FlowrExprContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(493);
				((FlowrExprContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (Kfor - 61)) | (1L << (Klet - 61)) | (1L << (Kwhere - 61)) | (1L << (Kgroup - 61)) | (1L << (Korder - 61)) | (1L << (Kcount - 61)) | (1L << (Kstable - 61)))) != 0)) {
				{
				setState(502);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(496);
					forClause();
					}
					break;
				case Klet:
					{
					setState(497);
					letClause();
					}
					break;
				case Kwhere:
					{
					setState(498);
					whereClause();
					}
					break;
				case Kgroup:
					{
					setState(499);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(500);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(501);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(506);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(507);
			match(Kreturn);
			setState(508);
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
		enterRule(_localctx, 54, RULE_forClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(510);
			match(Kfor);
			setState(511);
			((ForClauseContext)_localctx).forVar = forVar();
			((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(512);
				match(T__19);
				setState(513);
				((ForClauseContext)_localctx).forVar = forVar();
				((ForClauseContext)_localctx).vars.add(((ForClauseContext)_localctx).forVar);
				}
				}
				setState(518);
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
		enterRule(_localctx, 56, RULE_forVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			((ForVarContext)_localctx).var_ref = varRef();
			setState(522);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(520);
				match(Kas);
				setState(521);
				((ForVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(526);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kallowing) {
				{
				setState(524);
				((ForVarContext)_localctx).flag = match(Kallowing);
				setState(525);
				match(Kempty);
				}
			}

			setState(530);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kat) {
				{
				setState(528);
				match(Kat);
				setState(529);
				((ForVarContext)_localctx).at = varRef();
				}
			}

			setState(532);
			match(Kin);
			setState(533);
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
		enterRule(_localctx, 58, RULE_letClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			match(Klet);
			setState(536);
			((LetClauseContext)_localctx).letVar = letVar();
			((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
			setState(541);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(537);
				match(T__19);
				setState(538);
				((LetClauseContext)_localctx).letVar = letVar();
				((LetClauseContext)_localctx).vars.add(((LetClauseContext)_localctx).letVar);
				}
				}
				setState(543);
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
		enterRule(_localctx, 60, RULE_letVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(544);
			((LetVarContext)_localctx).var_ref = varRef();
			setState(547);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(545);
				match(Kas);
				setState(546);
				((LetVarContext)_localctx).seq = sequenceType();
				}
			}

			setState(549);
			match(T__20);
			setState(550);
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
		enterRule(_localctx, 62, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(552);
			match(Kwhere);
			setState(553);
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
		enterRule(_localctx, 64, RULE_groupByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(555);
			match(Kgroup);
			setState(556);
			match(Kby);
			setState(557);
			((GroupByClauseContext)_localctx).groupByVar = groupByVar();
			((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
			setState(562);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(558);
				match(T__19);
				setState(559);
				((GroupByClauseContext)_localctx).groupByVar = groupByVar();
				((GroupByClauseContext)_localctx).vars.add(((GroupByClauseContext)_localctx).groupByVar);
				}
				}
				setState(564);
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
		enterRule(_localctx, 66, RULE_groupByVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(565);
			((GroupByVarContext)_localctx).var_ref = varRef();
			setState(572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__20 || _la==Kas) {
				{
				setState(568);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(566);
					match(Kas);
					setState(567);
					((GroupByVarContext)_localctx).seq = sequenceType();
					}
				}

				setState(570);
				((GroupByVarContext)_localctx).decl = match(T__20);
				setState(571);
				((GroupByVarContext)_localctx).ex = exprSingle();
				}
			}

			setState(576);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(574);
				match(Kcollation);
				setState(575);
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
		enterRule(_localctx, 68, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(583);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Korder:
				{
				{
				setState(578);
				match(Korder);
				setState(579);
				match(Kby);
				}
				}
				break;
			case Kstable:
				{
				{
				setState(580);
				((OrderByClauseContext)_localctx).stb = match(Kstable);
				setState(581);
				match(Korder);
				setState(582);
				match(Kby);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(585);
			orderByExpr();
			setState(590);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(586);
				match(T__19);
				setState(587);
				orderByExpr();
				}
				}
				setState(592);
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
		enterRule(_localctx, 70, RULE_orderByExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(593);
			((OrderByExprContext)_localctx).ex = exprSingle();
			setState(596);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kascending:
				{
				setState(594);
				match(Kascending);
				}
				break;
			case Kdescending:
				{
				setState(595);
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
			setState(603);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kempty) {
				{
				setState(598);
				match(Kempty);
				setState(601);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kgreatest:
					{
					setState(599);
					((OrderByExprContext)_localctx).gr = match(Kgreatest);
					}
					break;
				case Kleast:
					{
					setState(600);
					((OrderByExprContext)_localctx).ls = match(Kleast);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(607);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kcollation) {
				{
				setState(605);
				match(Kcollation);
				setState(606);
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
		enterRule(_localctx, 72, RULE_countClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(609);
			match(Kcount);
			setState(610);
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
		enterRule(_localctx, 74, RULE_quantifiedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(614);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Ksome:
				{
				setState(612);
				((QuantifiedExprContext)_localctx).so = match(Ksome);
				}
				break;
			case Kevery:
				{
				setState(613);
				((QuantifiedExprContext)_localctx).ev = match(Kevery);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(616);
			((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
			((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
			setState(621);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(617);
				match(T__19);
				setState(618);
				((QuantifiedExprContext)_localctx).quantifiedExprVar = quantifiedExprVar();
				((QuantifiedExprContext)_localctx).vars.add(((QuantifiedExprContext)_localctx).quantifiedExprVar);
				}
				}
				setState(623);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(624);
			match(Ksatisfies);
			setState(625);
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
		enterRule(_localctx, 76, RULE_quantifiedExprVar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(627);
			varRef();
			setState(630);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(628);
				match(Kas);
				setState(629);
				sequenceType();
				}
			}

			setState(632);
			match(Kin);
			setState(633);
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
		enterRule(_localctx, 78, RULE_switchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(635);
			match(Kswitch);
			setState(636);
			match(T__23);
			setState(637);
			((SwitchExprContext)_localctx).cond = expr();
			setState(638);
			match(T__24);
			setState(640); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(639);
				((SwitchExprContext)_localctx).switchCaseClause = switchCaseClause();
				((SwitchExprContext)_localctx).cases.add(((SwitchExprContext)_localctx).switchCaseClause);
				}
				}
				setState(642); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(644);
			match(Kdefault);
			setState(645);
			match(Kreturn);
			setState(646);
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
		enterRule(_localctx, 80, RULE_switchCaseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(650); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(648);
				match(Kcase);
				setState(649);
				((SwitchCaseClauseContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseClauseContext)_localctx).cond.add(((SwitchCaseClauseContext)_localctx).exprSingle);
				}
				}
				setState(652); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(654);
			match(Kreturn);
			setState(655);
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
		enterRule(_localctx, 82, RULE_typeSwitchExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(657);
			match(Ktypeswitch);
			setState(658);
			match(T__23);
			setState(659);
			((TypeSwitchExprContext)_localctx).cond = expr();
			setState(660);
			match(T__24);
			setState(662); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(661);
				((TypeSwitchExprContext)_localctx).caseClause = caseClause();
				((TypeSwitchExprContext)_localctx).cses.add(((TypeSwitchExprContext)_localctx).caseClause);
				}
				}
				setState(664); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(666);
			match(Kdefault);
			setState(668);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(667);
				((TypeSwitchExprContext)_localctx).var_ref = varRef();
				}
			}

			setState(670);
			match(Kreturn);
			setState(671);
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
		enterRule(_localctx, 84, RULE_caseClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(673);
			match(Kcase);
			setState(677);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(674);
				((CaseClauseContext)_localctx).var_ref = varRef();
				setState(675);
				match(Kas);
				}
			}

			setState(679);
			((CaseClauseContext)_localctx).sequenceType = sequenceType();
			((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
			setState(684);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__32) {
				{
				{
				setState(680);
				match(T__32);
				setState(681);
				((CaseClauseContext)_localctx).sequenceType = sequenceType();
				((CaseClauseContext)_localctx).union.add(((CaseClauseContext)_localctx).sequenceType);
				}
				}
				setState(686);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(687);
			match(Kreturn);
			setState(688);
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
		enterRule(_localctx, 86, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(690);
			match(Kif);
			setState(691);
			match(T__23);
			setState(692);
			((IfExprContext)_localctx).test_condition = expr();
			setState(693);
			match(T__24);
			setState(694);
			match(Kthen);
			setState(695);
			((IfExprContext)_localctx).branch = exprSingle();
			setState(696);
			match(Kelse);
			setState(697);
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
		enterRule(_localctx, 88, RULE_tryCatchExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(699);
			match(Ktry);
			setState(700);
			match(T__25);
			setState(701);
			((TryCatchExprContext)_localctx).try_expression = expr();
			setState(702);
			match(T__26);
			setState(704); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(703);
					((TryCatchExprContext)_localctx).catchClause = catchClause();
					((TryCatchExprContext)_localctx).catches.add(((TryCatchExprContext)_localctx).catchClause);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(706); 
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
		enterRule(_localctx, 90, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(708);
			match(Kcatch);
			setState(711);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__33:
				{
				setState(709);
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
				setState(710);
				((CatchClauseContext)_localctx).qname = qname();
				((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(720);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__32) {
				{
				{
				setState(713);
				match(T__32);
				setState(716);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__33:
					{
					setState(714);
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
					setState(715);
					((CatchClauseContext)_localctx).qname = qname();
					((CatchClauseContext)_localctx).errors.add(((CatchClauseContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(722);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(723);
			match(T__25);
			setState(724);
			((CatchClauseContext)_localctx).catch_expression = expr();
			setState(725);
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
		enterRule(_localctx, 92, RULE_orExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(727);
			((OrExprContext)_localctx).main_expr = andExpr();
			setState(732);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(728);
					match(Kor);
					setState(729);
					((OrExprContext)_localctx).andExpr = andExpr();
					((OrExprContext)_localctx).rhs.add(((OrExprContext)_localctx).andExpr);
					}
					} 
				}
				setState(734);
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
		enterRule(_localctx, 94, RULE_andExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(735);
			((AndExprContext)_localctx).main_expr = notExpr();
			setState(740);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(736);
					match(Kand);
					setState(737);
					((AndExprContext)_localctx).notExpr = notExpr();
					((AndExprContext)_localctx).rhs.add(((AndExprContext)_localctx).notExpr);
					}
					} 
				}
				setState(742);
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
		enterRule(_localctx, 96, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(744);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				{
				setState(743);
				((NotExprContext)_localctx).Knot = match(Knot);
				((NotExprContext)_localctx).op.add(((NotExprContext)_localctx).Knot);
				}
				break;
			}
			setState(746);
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
		public Token _tset1306;
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
		enterRule(_localctx, 98, RULE_comparisonExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(748);
			((ComparisonExprContext)_localctx).main_expr = stringConcatExpr();
			setState(751);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44))) != 0)) {
				{
				setState(749);
				((ComparisonExprContext)_localctx)._tset1306 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44))) != 0)) ) {
					((ComparisonExprContext)_localctx)._tset1306 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((ComparisonExprContext)_localctx).op.add(((ComparisonExprContext)_localctx)._tset1306);
				setState(750);
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
		enterRule(_localctx, 100, RULE_stringConcatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(753);
			((StringConcatExprContext)_localctx).main_expr = rangeExpr();
			setState(758);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__45) {
				{
				{
				setState(754);
				match(T__45);
				setState(755);
				((StringConcatExprContext)_localctx).rangeExpr = rangeExpr();
				((StringConcatExprContext)_localctx).rhs.add(((StringConcatExprContext)_localctx).rangeExpr);
				}
				}
				setState(760);
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
		enterRule(_localctx, 102, RULE_rangeExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(761);
			((RangeExprContext)_localctx).main_expr = additiveExpr();
			setState(764);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(762);
				match(Kto);
				setState(763);
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
		public Token _tset1415;
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
		enterRule(_localctx, 104, RULE_additiveExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(766);
			((AdditiveExprContext)_localctx).main_expr = multiplicativeExpr();
			setState(771);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(767);
					((AdditiveExprContext)_localctx)._tset1415 = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==T__46 || _la==T__47) ) {
						((AdditiveExprContext)_localctx)._tset1415 = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					((AdditiveExprContext)_localctx).op.add(((AdditiveExprContext)_localctx)._tset1415);
					setState(768);
					((AdditiveExprContext)_localctx).multiplicativeExpr = multiplicativeExpr();
					((AdditiveExprContext)_localctx).rhs.add(((AdditiveExprContext)_localctx).multiplicativeExpr);
					}
					} 
				}
				setState(773);
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
		public Token _tset1443;
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
		enterRule(_localctx, 106, RULE_multiplicativeExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(774);
			((MultiplicativeExprContext)_localctx).main_expr = instanceOfExpr();
			setState(779);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__33) | (1L << T__48) | (1L << T__49) | (1L << T__50))) != 0)) {
				{
				{
				setState(775);
				((MultiplicativeExprContext)_localctx)._tset1443 = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__33) | (1L << T__48) | (1L << T__49) | (1L << T__50))) != 0)) ) {
					((MultiplicativeExprContext)_localctx)._tset1443 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((MultiplicativeExprContext)_localctx).op.add(((MultiplicativeExprContext)_localctx)._tset1443);
				setState(776);
				((MultiplicativeExprContext)_localctx).instanceOfExpr = instanceOfExpr();
				((MultiplicativeExprContext)_localctx).rhs.add(((MultiplicativeExprContext)_localctx).instanceOfExpr);
				}
				}
				setState(781);
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
		enterRule(_localctx, 108, RULE_instanceOfExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(782);
			((InstanceOfExprContext)_localctx).main_expr = isStaticallyExpr();
			setState(786);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				{
				setState(783);
				match(Kinstance);
				setState(784);
				match(Kof);
				setState(785);
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
		enterRule(_localctx, 110, RULE_isStaticallyExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(788);
			((IsStaticallyExprContext)_localctx).main_expr = treatExpr();
			setState(792);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				{
				setState(789);
				match(Kis);
				setState(790);
				match(Kstatically);
				setState(791);
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
		enterRule(_localctx, 112, RULE_treatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(794);
			((TreatExprContext)_localctx).main_expr = castableExpr();
			setState(798);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
			case 1:
				{
				setState(795);
				match(Ktreat);
				setState(796);
				match(Kas);
				setState(797);
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
		enterRule(_localctx, 114, RULE_castableExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			((CastableExprContext)_localctx).main_expr = castExpr();
			setState(804);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				{
				setState(801);
				match(Kcastable);
				setState(802);
				match(Kas);
				setState(803);
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
		enterRule(_localctx, 116, RULE_castExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
			((CastExprContext)_localctx).main_expr = arrowExpr();
			setState(810);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				{
				setState(807);
				match(Kcast);
				setState(808);
				match(Kas);
				setState(809);
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
		enterRule(_localctx, 118, RULE_arrowExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(812);
			((ArrowExprContext)_localctx).main_expr = unaryExpr();
			setState(821);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,76,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					{
					setState(813);
					match(T__3);
					setState(814);
					match(T__43);
					}
					setState(816);
					((ArrowExprContext)_localctx).arrowFunctionSpecifier = arrowFunctionSpecifier();
					((ArrowExprContext)_localctx).function.add(((ArrowExprContext)_localctx).arrowFunctionSpecifier);
					setState(817);
					((ArrowExprContext)_localctx).argumentList = argumentList();
					((ArrowExprContext)_localctx).arguments.add(((ArrowExprContext)_localctx).argumentList);
					}
					} 
				}
				setState(823);
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
		enterRule(_localctx, 120, RULE_arrowFunctionSpecifier);
		try {
			setState(827);
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
				setState(824);
				qname();
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 2);
				{
				setState(825);
				varRef();
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(826);
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
		public Token _tset1622;
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
		enterRule(_localctx, 122, RULE_unaryExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(832);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__46 || _la==T__47) {
				{
				{
				setState(829);
				((UnaryExprContext)_localctx)._tset1622 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__46 || _la==T__47) ) {
					((UnaryExprContext)_localctx)._tset1622 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((UnaryExprContext)_localctx).op.add(((UnaryExprContext)_localctx)._tset1622);
				}
				}
				setState(834);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(835);
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
		enterRule(_localctx, 124, RULE_valueExpr);
		try {
			setState(840);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(837);
				((ValueExprContext)_localctx).simpleMap_expr = simpleMapExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(838);
				((ValueExprContext)_localctx).validate_expr = validateExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(839);
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
		enterRule(_localctx, 126, RULE_validateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(842);
			match(Kvalidate);
			setState(843);
			match(Ktype);
			setState(844);
			sequenceType();
			setState(845);
			match(T__25);
			setState(846);
			expr();
			setState(847);
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
		enterRule(_localctx, 128, RULE_annotateExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(849);
			match(Kannotate);
			setState(850);
			match(Ktype);
			setState(851);
			sequenceType();
			setState(852);
			match(T__25);
			setState(853);
			expr();
			setState(854);
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
		enterRule(_localctx, 130, RULE_simpleMapExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(856);
			((SimpleMapExprContext)_localctx).main_expr = postFixExpr();
			setState(861);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__51) {
				{
				{
				setState(857);
				match(T__51);
				setState(858);
				((SimpleMapExprContext)_localctx).postFixExpr = postFixExpr();
				((SimpleMapExprContext)_localctx).map_expr.add(((SimpleMapExprContext)_localctx).postFixExpr);
				}
				}
				setState(863);
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
		enterRule(_localctx, 132, RULE_postFixExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
			((PostFixExprContext)_localctx).main_expr = primaryExpr();
			setState(872);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(870);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
					case 1:
						{
						setState(865);
						arrayLookup();
						}
						break;
					case 2:
						{
						setState(866);
						predicate();
						}
						break;
					case 3:
						{
						setState(867);
						objectLookup();
						}
						break;
					case 4:
						{
						setState(868);
						arrayUnboxing();
						}
						break;
					case 5:
						{
						setState(869);
						argumentList();
						}
						break;
					}
					} 
				}
				setState(874);
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
		enterRule(_localctx, 134, RULE_arrayLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875);
			match(T__52);
			setState(876);
			match(T__52);
			setState(877);
			expr();
			setState(878);
			match(T__53);
			setState(879);
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
		enterRule(_localctx, 136, RULE_arrayUnboxing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(881);
			match(T__52);
			setState(882);
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
		enterRule(_localctx, 138, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(884);
			match(T__52);
			setState(885);
			expr();
			setState(886);
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
		enterRule(_localctx, 140, RULE_objectLookup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(888);
			match(T__54);
			setState(895);
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
				setState(889);
				((ObjectLookupContext)_localctx).kw = keyWords();
				}
				break;
			case STRING:
				{
				setState(890);
				((ObjectLookupContext)_localctx).lt = stringLiteral();
				}
				break;
			case NCName:
				{
				setState(891);
				((ObjectLookupContext)_localctx).nc = match(NCName);
				}
				break;
			case T__23:
				{
				setState(892);
				((ObjectLookupContext)_localctx).pe = parenthesizedExpr();
				}
				break;
			case T__31:
				{
				setState(893);
				((ObjectLookupContext)_localctx).vr = varRef();
				}
				break;
			case T__55:
				{
				setState(894);
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
		enterRule(_localctx, 142, RULE_primaryExpr);
		try {
			setState(912);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(897);
				match(NullLiteral);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(898);
				match(Ktrue);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(899);
				match(Kfalse);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(900);
				match(Literal);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(901);
				stringLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(902);
				varRef();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(903);
				parenthesizedExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(904);
				contextItemExpr();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(905);
				objectConstructor();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(906);
				functionCall();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(907);
				orderedExpr();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(908);
				unorderedExpr();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(909);
				arrayConstructor();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(910);
				functionItemExpr();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(911);
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
		enterRule(_localctx, 144, RULE_blockExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(914);
			match(T__25);
			setState(915);
			statementsAndExpr();
			setState(916);
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
		enterRule(_localctx, 146, RULE_varRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(918);
			match(T__31);
			setState(919);
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
		enterRule(_localctx, 148, RULE_parenthesizedExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(921);
			match(T__23);
			setState(923);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (NullLiteral - 129)) | (1L << (Literal - 129)) | (1L << (NCName - 129)))) != 0)) {
				{
				setState(922);
				expr();
				}
			}

			setState(925);
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
		enterRule(_localctx, 150, RULE_contextItemExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(927);
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
		enterRule(_localctx, 152, RULE_orderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(929);
			match(T__5);
			setState(930);
			match(T__25);
			setState(931);
			expr();
			setState(932);
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
		enterRule(_localctx, 154, RULE_unorderedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(934);
			match(Kunordered);
			setState(935);
			match(T__25);
			setState(936);
			expr();
			setState(937);
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
		enterRule(_localctx, 156, RULE_functionCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(939);
			((FunctionCallContext)_localctx).fn_name = qname();
			setState(940);
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
		enterRule(_localctx, 158, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(942);
			match(T__23);
			setState(949);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & ((1L << (ArgumentPlaceholder - 128)) | (1L << (NullLiteral - 128)) | (1L << (Literal - 128)) | (1L << (NCName - 128)))) != 0)) {
				{
				{
				setState(943);
				((ArgumentListContext)_localctx).argument = argument();
				((ArgumentListContext)_localctx).args.add(((ArgumentListContext)_localctx).argument);
				setState(945);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__19) {
					{
					setState(944);
					match(T__19);
					}
				}

				}
				}
				setState(951);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(952);
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
		enterRule(_localctx, 160, RULE_argument);
		try {
			setState(956);
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
			case STRING:
			case NullLiteral:
			case Literal:
			case NCName:
				enterOuterAlt(_localctx, 1);
				{
				setState(954);
				exprSingle();
				}
				break;
			case ArgumentPlaceholder:
				enterOuterAlt(_localctx, 2);
				{
				setState(955);
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
		enterRule(_localctx, 162, RULE_functionItemExpr);
		try {
			setState(960);
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
				setState(958);
				namedFunctionRef();
				}
				break;
			case T__22:
			case T__57:
				enterOuterAlt(_localctx, 2);
				{
				setState(959);
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
		enterRule(_localctx, 164, RULE_namedFunctionRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962);
			((NamedFunctionRefContext)_localctx).fn_name = qname();
			setState(963);
			match(T__56);
			setState(964);
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
		enterRule(_localctx, 166, RULE_inlineFunctionExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(966);
			annotations();
			setState(967);
			match(T__22);
			setState(968);
			match(T__23);
			setState(970);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(969);
				paramList();
				}
			}

			setState(972);
			match(T__24);
			setState(975);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(973);
				match(Kas);
				setState(974);
				((InlineFunctionExprContext)_localctx).return_type = sequenceType();
				}
			}

			{
			setState(977);
			match(T__25);
			{
			setState(978);
			((InlineFunctionExprContext)_localctx).fn_body = statementsAndOptionalExpr();
			}
			setState(979);
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
		enterRule(_localctx, 168, RULE_insertExpr);
		int _la;
		try {
			setState(1004);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,94,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(981);
				match(Kinsert);
				setState(982);
				match(Kjson);
				setState(983);
				((InsertExprContext)_localctx).to_insert_expr = exprSingle();
				setState(984);
				match(Kinto);
				setState(985);
				((InsertExprContext)_localctx).main_expr = exprSingle();
				setState(989);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
				case 1:
					{
					setState(986);
					match(Kat);
					setState(987);
					match(Kposition);
					setState(988);
					((InsertExprContext)_localctx).pos_expr = exprSingle();
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(991);
				match(Kinsert);
				setState(992);
				match(Kjson);
				setState(993);
				pairConstructor();
				setState(998);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(994);
					match(T__19);
					setState(995);
					pairConstructor();
					}
					}
					setState(1000);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1001);
				match(Kinto);
				setState(1002);
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
		enterRule(_localctx, 170, RULE_deleteExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1006);
			match(Kdelete);
			setState(1007);
			match(Kjson);
			setState(1008);
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
		enterRule(_localctx, 172, RULE_renameExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1010);
			match(Krename);
			setState(1011);
			match(Kjson);
			setState(1012);
			updateLocator();
			setState(1013);
			match(Kas);
			setState(1014);
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
		enterRule(_localctx, 174, RULE_replaceExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1016);
			match(Kreplace);
			setState(1017);
			match(Kjson);
			setState(1018);
			match(Kvalue);
			setState(1019);
			match(Kof);
			setState(1020);
			updateLocator();
			setState(1021);
			match(Kwith);
			setState(1022);
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
		enterRule(_localctx, 176, RULE_transformExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1024);
			match(Kcopy);
			setState(1025);
			match(Kjson);
			setState(1026);
			copyDecl();
			setState(1031);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(1027);
				match(T__19);
				setState(1028);
				copyDecl();
				}
				}
				setState(1033);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1034);
			match(Kmodify);
			setState(1035);
			((TransformExprContext)_localctx).mod_expr = exprSingle();
			setState(1036);
			match(Kreturn);
			setState(1037);
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
		enterRule(_localctx, 178, RULE_appendExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1039);
			match(Kappend);
			setState(1040);
			match(Kjson);
			setState(1041);
			((AppendExprContext)_localctx).to_append_expr = exprSingle();
			setState(1042);
			match(Kinto);
			setState(1043);
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
		enterRule(_localctx, 180, RULE_updateLocator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1045);
			((UpdateLocatorContext)_localctx).main_expr = primaryExpr();
			setState(1048); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(1048);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__52:
						{
						setState(1046);
						arrayLookup();
						}
						break;
					case T__54:
						{
						setState(1047);
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
				setState(1050); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,97,_ctx);
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
		enterRule(_localctx, 182, RULE_copyDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1052);
			((CopyDeclContext)_localctx).var_ref = varRef();
			setState(1053);
			match(T__20);
			setState(1054);
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
		enterRule(_localctx, 184, RULE_statements);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1059);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1056);
					statement();
					}
					} 
				}
				setState(1061);
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
		enterRule(_localctx, 186, RULE_statementsAndExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1062);
			statements();
			setState(1063);
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
		enterRule(_localctx, 188, RULE_statementsAndOptionalExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1065);
			statements();
			setState(1067);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (NullLiteral - 129)) | (1L << (Literal - 129)) | (1L << (NCName - 129)))) != 0)) {
				{
				setState(1066);
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
		enterRule(_localctx, 190, RULE_statement);
		try {
			setState(1082);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1069);
				applyStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1070);
				assignStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1071);
				blockStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1072);
				breakStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1073);
				continueStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1074);
				exitStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1075);
				flowrStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1076);
				ifStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1077);
				switchStatement();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1078);
				tryCatchStatement();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1079);
				typeSwitchStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1080);
				varDeclStatement();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1081);
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
		enterRule(_localctx, 192, RULE_applyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1084);
			exprSimple();
			setState(1085);
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
		enterRule(_localctx, 194, RULE_assignStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1087);
			match(T__31);
			setState(1088);
			qname();
			setState(1089);
			match(T__20);
			setState(1090);
			exprSingle();
			setState(1091);
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
		enterRule(_localctx, 196, RULE_blockStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1093);
			match(T__25);
			setState(1094);
			statements();
			setState(1095);
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
		enterRule(_localctx, 198, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1097);
			match(Kbreak);
			setState(1098);
			match(Kloop);
			setState(1099);
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
		enterRule(_localctx, 200, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1101);
			match(Kcontinue);
			setState(1102);
			match(Kloop);
			setState(1103);
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
		enterRule(_localctx, 202, RULE_exitStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1105);
			match(Kexit);
			setState(1106);
			match(Kreturning);
			setState(1107);
			exprSingle();
			setState(1108);
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
		enterRule(_localctx, 204, RULE_flowrStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1112);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Kfor:
				{
				setState(1110);
				((FlowrStatementContext)_localctx).start_for = forClause();
				}
				break;
			case Klet:
				{
				setState(1111);
				((FlowrStatementContext)_localctx).start_let = letClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (Kfor - 61)) | (1L << (Klet - 61)) | (1L << (Kwhere - 61)) | (1L << (Kgroup - 61)) | (1L << (Korder - 61)) | (1L << (Kcount - 61)) | (1L << (Kstable - 61)))) != 0)) {
				{
				setState(1120);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Kfor:
					{
					setState(1114);
					forClause();
					}
					break;
				case Klet:
					{
					setState(1115);
					letClause();
					}
					break;
				case Kwhere:
					{
					setState(1116);
					whereClause();
					}
					break;
				case Kgroup:
					{
					setState(1117);
					groupByClause();
					}
					break;
				case Korder:
				case Kstable:
					{
					setState(1118);
					orderByClause();
					}
					break;
				case Kcount:
					{
					setState(1119);
					countClause();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(1124);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1125);
			match(Kreturn);
			setState(1126);
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
		enterRule(_localctx, 206, RULE_ifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1128);
			match(Kif);
			setState(1129);
			match(T__23);
			setState(1130);
			((IfStatementContext)_localctx).test_expr = expr();
			setState(1131);
			match(T__24);
			setState(1132);
			match(Kthen);
			setState(1133);
			((IfStatementContext)_localctx).branch = statement();
			setState(1134);
			match(Kelse);
			setState(1135);
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
		enterRule(_localctx, 208, RULE_switchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1137);
			match(Kswitch);
			setState(1138);
			match(T__23);
			setState(1139);
			((SwitchStatementContext)_localctx).condExpr = expr();
			setState(1140);
			match(T__24);
			setState(1142); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1141);
				((SwitchStatementContext)_localctx).switchCaseStatement = switchCaseStatement();
				((SwitchStatementContext)_localctx).cases.add(((SwitchStatementContext)_localctx).switchCaseStatement);
				}
				}
				setState(1144); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(1146);
			match(Kdefault);
			setState(1147);
			match(Kreturn);
			setState(1148);
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
		enterRule(_localctx, 210, RULE_switchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1152); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1150);
				match(Kcase);
				setState(1151);
				((SwitchCaseStatementContext)_localctx).exprSingle = exprSingle();
				((SwitchCaseStatementContext)_localctx).cond.add(((SwitchCaseStatementContext)_localctx).exprSingle);
				}
				}
				setState(1154); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(1156);
			match(Kreturn);
			setState(1157);
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
		enterRule(_localctx, 212, RULE_tryCatchStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1159);
			match(Ktry);
			setState(1160);
			((TryCatchStatementContext)_localctx).try_block = blockStatement();
			setState(1162); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1161);
					((TryCatchStatementContext)_localctx).catchCaseStatement = catchCaseStatement();
					((TryCatchStatementContext)_localctx).catches.add(((TryCatchStatementContext)_localctx).catchCaseStatement);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1164); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,106,_ctx);
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

	public static class CatchCaseStatementContext extends ParserRuleContext {
		public Token s34;
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
		enterRule(_localctx, 214, RULE_catchCaseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1166);
			match(Kcatch);
			setState(1169);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__33:
				{
				setState(1167);
				((CatchCaseStatementContext)_localctx).s34 = match(T__33);
				((CatchCaseStatementContext)_localctx).jokers.add(((CatchCaseStatementContext)_localctx).s34);
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
				setState(1168);
				((CatchCaseStatementContext)_localctx).qname = qname();
				((CatchCaseStatementContext)_localctx).errors.add(((CatchCaseStatementContext)_localctx).qname);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1178);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__32) {
				{
				{
				setState(1171);
				match(T__32);
				setState(1174);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__33:
					{
					setState(1172);
					((CatchCaseStatementContext)_localctx).s34 = match(T__33);
					((CatchCaseStatementContext)_localctx).jokers.add(((CatchCaseStatementContext)_localctx).s34);
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
					setState(1173);
					((CatchCaseStatementContext)_localctx).qname = qname();
					((CatchCaseStatementContext)_localctx).errors.add(((CatchCaseStatementContext)_localctx).qname);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1180);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1181);
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
		enterRule(_localctx, 216, RULE_typeSwitchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1183);
			match(Ktypeswitch);
			setState(1184);
			match(T__23);
			setState(1185);
			((TypeSwitchStatementContext)_localctx).cond = expr();
			setState(1186);
			match(T__24);
			setState(1188); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1187);
				((TypeSwitchStatementContext)_localctx).caseStatement = caseStatement();
				((TypeSwitchStatementContext)_localctx).cases.add(((TypeSwitchStatementContext)_localctx).caseStatement);
				}
				}
				setState(1190); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Kcase );
			setState(1192);
			match(Kdefault);
			setState(1194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(1193);
				((TypeSwitchStatementContext)_localctx).var_ref = varRef();
				}
			}

			setState(1196);
			match(Kreturn);
			setState(1197);
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
		enterRule(_localctx, 218, RULE_caseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1199);
			match(Kcase);
			setState(1203);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__31) {
				{
				setState(1200);
				((CaseStatementContext)_localctx).var_ref = varRef();
				setState(1201);
				match(Kas);
				}
			}

			setState(1205);
			((CaseStatementContext)_localctx).sequenceType = sequenceType();
			((CaseStatementContext)_localctx).union.add(((CaseStatementContext)_localctx).sequenceType);
			setState(1210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__32) {
				{
				{
				setState(1206);
				match(T__32);
				setState(1207);
				((CaseStatementContext)_localctx).sequenceType = sequenceType();
				((CaseStatementContext)_localctx).union.add(((CaseStatementContext)_localctx).sequenceType);
				}
				}
				setState(1212);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1213);
			match(Kreturn);
			setState(1214);
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
		enterRule(_localctx, 220, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1216);
			match(T__57);
			setState(1217);
			((AnnotationContext)_localctx).name = qname();
			setState(1228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23) {
				{
				setState(1218);
				match(T__23);
				setState(1219);
				match(Literal);
				setState(1224);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(1220);
					match(T__19);
					setState(1221);
					match(Literal);
					}
					}
					setState(1226);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1227);
				match(T__24);
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
		enterRule(_localctx, 222, RULE_annotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1233);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__57) {
				{
				{
				setState(1230);
				annotation();
				}
				}
				setState(1235);
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

	public static class VarDeclStatementContext extends ParserRuleContext {
		public VarRefContext var_ref;
		public ExprSingleContext expr_val;
		public VarDeclOtherContext other_vars;
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode Kvariable() { return getToken(JsoniqParser.Kvariable, 0); }
		public VarRefContext varRef() {
			return getRuleContext(VarRefContext.class,0);
		}
		public VarDeclOtherContext varDeclOther() {
			return getRuleContext(VarDeclOtherContext.class,0);
		}
		public TerminalNode Kas() { return getToken(JsoniqParser.Kas, 0); }
		public SequenceTypeContext sequenceType() {
			return getRuleContext(SequenceTypeContext.class,0);
		}
		public ExprSingleContext exprSingle() {
			return getRuleContext(ExprSingleContext.class,0);
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
		enterRule(_localctx, 224, RULE_varDeclStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1236);
			annotations();
			setState(1237);
			match(Kvariable);
			setState(1238);
			((VarDeclStatementContext)_localctx).var_ref = varRef();
			setState(1241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Kas) {
				{
				setState(1239);
				match(Kas);
				setState(1240);
				sequenceType();
				}
			}

			setState(1245);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(1243);
				match(T__20);
				setState(1244);
				((VarDeclStatementContext)_localctx).expr_val = exprSingle();
				}
			}

			setState(1247);
			((VarDeclStatementContext)_localctx).other_vars = varDeclOther();
			setState(1248);
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

	public static class VarDeclOtherContext extends ParserRuleContext {
		public VarRefContext var_ref;
		public ExprSingleContext exprSingle;
		public List<ExprSingleContext> expr_vals = new ArrayList<ExprSingleContext>();
		public List<VarRefContext> varRef() {
			return getRuleContexts(VarRefContext.class);
		}
		public VarRefContext varRef(int i) {
			return getRuleContext(VarRefContext.class,i);
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
		public List<ExprSingleContext> exprSingle() {
			return getRuleContexts(ExprSingleContext.class);
		}
		public ExprSingleContext exprSingle(int i) {
			return getRuleContext(ExprSingleContext.class,i);
		}
		public VarDeclOtherContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclOther; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsoniqVisitor ) return ((JsoniqVisitor<? extends T>)visitor).visitVarDeclOther(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclOtherContext varDeclOther() throws RecognitionException {
		VarDeclOtherContext _localctx = new VarDeclOtherContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_varDeclOther);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1262);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(1250);
				match(T__19);
				setState(1251);
				((VarDeclOtherContext)_localctx).var_ref = varRef();
				setState(1254);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Kas) {
					{
					setState(1252);
					match(Kas);
					setState(1253);
					sequenceType();
					}
				}

				setState(1258);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__20) {
					{
					setState(1256);
					match(T__20);
					setState(1257);
					((VarDeclOtherContext)_localctx).exprSingle = exprSingle();
					((VarDeclOtherContext)_localctx).expr_vals.add(((VarDeclOtherContext)_localctx).exprSingle);
					}
				}

				}
				}
				setState(1264);
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
		enterRule(_localctx, 228, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1265);
			match(Kwhile);
			setState(1266);
			match(T__23);
			setState(1267);
			((WhileStatementContext)_localctx).test_expr = expr();
			setState(1268);
			match(T__24);
			setState(1269);
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

	public static class SequenceTypeContext extends ParserRuleContext {
		public ItemTypeContext item;
		public Token s128;
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
		enterRule(_localctx, 230, RULE_sequenceType);
		try {
			setState(1279);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(1271);
				match(T__23);
				setState(1272);
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
				setState(1273);
				((SequenceTypeContext)_localctx).item = itemType();
				setState(1277);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
				case 1:
					{
					setState(1274);
					((SequenceTypeContext)_localctx).s128 = match(ArgumentPlaceholder);
					((SequenceTypeContext)_localctx).question.add(((SequenceTypeContext)_localctx).s128);
					}
					break;
				case 2:
					{
					setState(1275);
					((SequenceTypeContext)_localctx).s34 = match(T__33);
					((SequenceTypeContext)_localctx).star.add(((SequenceTypeContext)_localctx).s34);
					}
					break;
				case 3:
					{
					setState(1276);
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
			setState(1297);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__25:
				enterOuterAlt(_localctx, 1);
				{
				setState(1281);
				match(T__25);
				setState(1290);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (NullLiteral - 129)) | (1L << (Literal - 129)) | (1L << (NCName - 129)))) != 0)) {
					{
					setState(1282);
					pairConstructor();
					setState(1287);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__19) {
						{
						{
						setState(1283);
						match(T__19);
						setState(1284);
						pairConstructor();
						}
						}
						setState(1289);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1292);
				match(T__26);
				}
				break;
			case T__58:
				enterOuterAlt(_localctx, 2);
				{
				setState(1293);
				((ObjectConstructorContext)_localctx).s59 = match(T__58);
				((ObjectConstructorContext)_localctx).merge_operator.add(((ObjectConstructorContext)_localctx).s59);
				setState(1294);
				expr();
				setState(1295);
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
			setState(1302);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1299);
				qname();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1300);
				match(NullLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1301);
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
		enterRule(_localctx, 236, RULE_functionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1306);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
			case 1:
				{
				setState(1304);
				anyFunctionTest();
				}
				break;
			case 2:
				{
				setState(1305);
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
		enterRule(_localctx, 238, RULE_anyFunctionTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1308);
			match(T__22);
			setState(1309);
			match(T__23);
			setState(1310);
			match(T__33);
			setState(1311);
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
		enterRule(_localctx, 240, RULE_typedFunctionTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1313);
			match(T__22);
			setState(1314);
			match(T__23);
			setState(1323);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 23)) & ~0x3f) == 0 && ((1L << (_la - 23)) & ((1L << (T__22 - 23)) | (1L << (T__23 - 23)) | (1L << (Kfor - 23)) | (1L << (Klet - 23)) | (1L << (Kwhere - 23)) | (1L << (Kgroup - 23)) | (1L << (Kby - 23)) | (1L << (Korder - 23)) | (1L << (Kreturn - 23)) | (1L << (Kif - 23)) | (1L << (Kin - 23)) | (1L << (Kas - 23)) | (1L << (Kat - 23)) | (1L << (Kallowing - 23)) | (1L << (Kempty - 23)) | (1L << (Kcount - 23)) | (1L << (Kstable - 23)) | (1L << (Kascending - 23)) | (1L << (Kdescending - 23)) | (1L << (Ksome - 23)) | (1L << (Kevery - 23)) | (1L << (Ksatisfies - 23)) | (1L << (Kcollation - 23)) | (1L << (Kgreatest - 23)) | (1L << (Kleast - 23)) | (1L << (Kswitch - 23)) | (1L << (Kcase - 23)) | (1L << (Ktry - 23)))) != 0) || ((((_la - 87)) & ~0x3f) == 0 && ((1L << (_la - 87)) & ((1L << (Kcatch - 87)) | (1L << (Kdefault - 87)) | (1L << (Kthen - 87)) | (1L << (Kelse - 87)) | (1L << (Ktypeswitch - 87)) | (1L << (Kor - 87)) | (1L << (Kand - 87)) | (1L << (Knot - 87)) | (1L << (Kto - 87)) | (1L << (Kinstance - 87)) | (1L << (Kof - 87)) | (1L << (Kstatically - 87)) | (1L << (Kis - 87)) | (1L << (Ktreat - 87)) | (1L << (Kcast - 87)) | (1L << (Kcastable - 87)) | (1L << (Kversion - 87)) | (1L << (Kjsoniq - 87)) | (1L << (Kunordered - 87)) | (1L << (Ktrue - 87)) | (1L << (Kfalse - 87)) | (1L << (Ktype - 87)) | (1L << (Kvalidate - 87)) | (1L << (Kannotate - 87)) | (1L << (Kdeclare - 87)) | (1L << (Kcontext - 87)) | (1L << (Kitem - 87)) | (1L << (Kvariable - 87)) | (1L << (Kinsert - 87)) | (1L << (Kdelete - 87)) | (1L << (Krename - 87)) | (1L << (Kreplace - 87)) | (1L << (Kcopy - 87)) | (1L << (Kmodify - 87)) | (1L << (Kappend - 87)) | (1L << (Kinto - 87)) | (1L << (Kvalue - 87)) | (1L << (Kjson - 87)) | (1L << (Kwith - 87)) | (1L << (Kposition - 87)) | (1L << (NullLiteral - 87)) | (1L << (NCName - 87)))) != 0)) {
				{
				setState(1315);
				((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
				((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
				setState(1320);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__19) {
					{
					{
					setState(1316);
					match(T__19);
					setState(1317);
					((TypedFunctionTestContext)_localctx).sequenceType = sequenceType();
					((TypedFunctionTestContext)_localctx).st.add(((TypedFunctionTestContext)_localctx).sequenceType);
					}
					}
					setState(1322);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1325);
			match(T__24);
			setState(1326);
			match(Kas);
			setState(1327);
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
		public Token s128;
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
			setState(1329);
			((SingleTypeContext)_localctx).item = itemType();
			setState(1331);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1330);
				((SingleTypeContext)_localctx).s128 = match(ArgumentPlaceholder);
				((SingleTypeContext)_localctx).question.add(((SingleTypeContext)_localctx).s128);
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
		enterRule(_localctx, 244, RULE_pairConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1335);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(1333);
				((PairConstructorContext)_localctx).lhs = exprSingle();
				}
				break;
			case 2:
				{
				setState(1334);
				((PairConstructorContext)_localctx).name = match(NCName);
				}
				break;
			}
			setState(1337);
			_la = _input.LA(1);
			if ( !(_la==T__7 || _la==ArgumentPlaceholder) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1338);
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
		enterRule(_localctx, 246, RULE_arrayConstructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1340);
			match(T__52);
			setState(1342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__46) | (1L << T__47) | (1L << T__52) | (1L << T__55) | (1L << T__57) | (1L << T__58) | (1L << Kfor) | (1L << Klet) | (1L << Kwhere))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (Kgroup - 64)) | (1L << (Kby - 64)) | (1L << (Korder - 64)) | (1L << (Kreturn - 64)) | (1L << (Kif - 64)) | (1L << (Kin - 64)) | (1L << (Kas - 64)) | (1L << (Kat - 64)) | (1L << (Kallowing - 64)) | (1L << (Kempty - 64)) | (1L << (Kcount - 64)) | (1L << (Kstable - 64)) | (1L << (Kascending - 64)) | (1L << (Kdescending - 64)) | (1L << (Ksome - 64)) | (1L << (Kevery - 64)) | (1L << (Ksatisfies - 64)) | (1L << (Kcollation - 64)) | (1L << (Kgreatest - 64)) | (1L << (Kleast - 64)) | (1L << (Kswitch - 64)) | (1L << (Kcase - 64)) | (1L << (Ktry - 64)) | (1L << (Kcatch - 64)) | (1L << (Kdefault - 64)) | (1L << (Kthen - 64)) | (1L << (Kelse - 64)) | (1L << (Ktypeswitch - 64)) | (1L << (Kor - 64)) | (1L << (Kand - 64)) | (1L << (Knot - 64)) | (1L << (Kto - 64)) | (1L << (Kinstance - 64)) | (1L << (Kof - 64)) | (1L << (Kstatically - 64)) | (1L << (Kis - 64)) | (1L << (Ktreat - 64)) | (1L << (Kcast - 64)) | (1L << (Kcastable - 64)) | (1L << (Kversion - 64)) | (1L << (Kjsoniq - 64)) | (1L << (Kunordered - 64)) | (1L << (Ktrue - 64)) | (1L << (Kfalse - 64)) | (1L << (Ktype - 64)) | (1L << (Kvalidate - 64)) | (1L << (Kannotate - 64)) | (1L << (Kdeclare - 64)) | (1L << (Kcontext - 64)) | (1L << (Kitem - 64)) | (1L << (Kvariable - 64)) | (1L << (Kinsert - 64)) | (1L << (Kdelete - 64)) | (1L << (Krename - 64)) | (1L << (Kreplace - 64)) | (1L << (Kcopy - 64)) | (1L << (Kmodify - 64)) | (1L << (Kappend - 64)) | (1L << (Kinto - 64)) | (1L << (Kvalue - 64)) | (1L << (Kjson - 64)) | (1L << (Kwith - 64)) | (1L << (Kposition - 64)) | (1L << (STRING - 64)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (NullLiteral - 129)) | (1L << (Literal - 129)) | (1L << (NCName - 129)))) != 0)) {
				{
				setState(1341);
				expr();
				}
			}

			setState(1344);
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
		enterRule(_localctx, 248, RULE_uriLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1346);
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
		enterRule(_localctx, 250, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1348);
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
		enterRule(_localctx, 252, RULE_keyWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1350);
			_la = _input.LA(1);
			if ( !(((((_la - 61)) & ~0x3f) == 0 && ((1L << (_la - 61)) & ((1L << (Kfor - 61)) | (1L << (Klet - 61)) | (1L << (Kwhere - 61)) | (1L << (Kgroup - 61)) | (1L << (Kby - 61)) | (1L << (Korder - 61)) | (1L << (Kreturn - 61)) | (1L << (Kif - 61)) | (1L << (Kin - 61)) | (1L << (Kas - 61)) | (1L << (Kat - 61)) | (1L << (Kallowing - 61)) | (1L << (Kempty - 61)) | (1L << (Kcount - 61)) | (1L << (Kstable - 61)) | (1L << (Kascending - 61)) | (1L << (Kdescending - 61)) | (1L << (Ksome - 61)) | (1L << (Kevery - 61)) | (1L << (Ksatisfies - 61)) | (1L << (Kcollation - 61)) | (1L << (Kgreatest - 61)) | (1L << (Kleast - 61)) | (1L << (Kswitch - 61)) | (1L << (Kcase - 61)) | (1L << (Ktry - 61)) | (1L << (Kcatch - 61)) | (1L << (Kdefault - 61)) | (1L << (Kthen - 61)) | (1L << (Kelse - 61)) | (1L << (Ktypeswitch - 61)) | (1L << (Kor - 61)) | (1L << (Kand - 61)) | (1L << (Knot - 61)) | (1L << (Kto - 61)) | (1L << (Kinstance - 61)) | (1L << (Kof - 61)) | (1L << (Kstatically - 61)) | (1L << (Kis - 61)) | (1L << (Ktreat - 61)) | (1L << (Kcast - 61)) | (1L << (Kcastable - 61)) | (1L << (Kversion - 61)) | (1L << (Kjsoniq - 61)) | (1L << (Kunordered - 61)) | (1L << (Ktrue - 61)) | (1L << (Kfalse - 61)) | (1L << (Ktype - 61)) | (1L << (Kvalidate - 61)) | (1L << (Kannotate - 61)) | (1L << (Kdeclare - 61)) | (1L << (Kcontext - 61)) | (1L << (Kitem - 61)) | (1L << (Kvariable - 61)) | (1L << (Kinsert - 61)) | (1L << (Kdelete - 61)) | (1L << (Krename - 61)) | (1L << (Kreplace - 61)) | (1L << (Kcopy - 61)) | (1L << (Kmodify - 61)) | (1L << (Kappend - 61)) | (1L << (Kinto - 61)) | (1L << (Kvalue - 61)) | (1L << (Kjson - 61)))) != 0) || ((((_la - 125)) & ~0x3f) == 0 && ((1L << (_la - 125)) & ((1L << (Kwith - 125)) | (1L << (Kposition - 125)) | (1L << (NullLiteral - 125)))) != 0)) ) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0092\u054b\4\2\t"+
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
		"\16\6\u012c\13\6\3\7\3\7\3\b\3\b\3\b\3\b\5\b\u0134\n\b\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\n\3\n\3\n\3\n\5\n\u0140\n\n\3\13\3\13\3\13\3\13\3\13\3\f\3"+
		"\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\5\16\u0156"+
		"\n\16\3\16\3\16\3\16\3\16\7\16\u015c\n\16\f\16\16\16\u015f\13\16\3\17"+
		"\3\17\5\17\u0163\n\17\3\17\5\17\u0166\n\17\3\17\3\17\5\17\u016a\n\17\3"+
		"\20\3\20\3\21\3\21\3\21\3\21\3\21\5\21\u0173\n\21\3\21\3\21\3\21\3\21"+
		"\3\21\7\21\u017a\n\21\f\21\16\21\u017d\13\21\5\21\u017f\n\21\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\5\22\u0187\n\22\3\22\3\22\3\22\3\22\3\22\5\22\u018e"+
		"\n\22\5\22\u0190\n\22\3\23\3\23\3\23\3\23\3\23\5\23\u0197\n\23\3\23\3"+
		"\23\3\23\3\23\3\23\5\23\u019e\n\23\5\23\u01a0\n\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\5\24\u01a8\n\24\3\24\3\24\3\24\5\24\u01ad\n\24\3\24\3\24\3"+
		"\24\3\24\3\24\5\24\u01b4\n\24\3\25\3\25\3\25\3\25\3\25\5\25\u01bb\n\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u01c5\n\26\3\27\3\27\3\27"+
		"\7\27\u01ca\n\27\f\27\16\27\u01cd\13\27\3\30\3\30\3\30\3\30\5\30\u01d3"+
		"\n\30\3\31\3\31\3\31\7\31\u01d8\n\31\f\31\16\31\u01db\13\31\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\5\32\u01e3\n\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\5\33\u01ed\n\33\3\34\3\34\5\34\u01f1\n\34\3\34\3\34\3\34\3\34\3"+
		"\34\3\34\7\34\u01f9\n\34\f\34\16\34\u01fc\13\34\3\34\3\34\3\34\3\35\3"+
		"\35\3\35\3\35\7\35\u0205\n\35\f\35\16\35\u0208\13\35\3\36\3\36\3\36\5"+
		"\36\u020d\n\36\3\36\3\36\5\36\u0211\n\36\3\36\3\36\5\36\u0215\n\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\7\37\u021e\n\37\f\37\16\37\u0221\13\37"+
		"\3 \3 \3 \5 \u0226\n \3 \3 \3 \3!\3!\3!\3\"\3\"\3\"\3\"\3\"\7\"\u0233"+
		"\n\"\f\"\16\"\u0236\13\"\3#\3#\3#\5#\u023b\n#\3#\3#\5#\u023f\n#\3#\3#"+
		"\5#\u0243\n#\3$\3$\3$\3$\3$\5$\u024a\n$\3$\3$\3$\7$\u024f\n$\f$\16$\u0252"+
		"\13$\3%\3%\3%\5%\u0257\n%\3%\3%\3%\5%\u025c\n%\5%\u025e\n%\3%\3%\5%\u0262"+
		"\n%\3&\3&\3&\3\'\3\'\5\'\u0269\n\'\3\'\3\'\3\'\7\'\u026e\n\'\f\'\16\'"+
		"\u0271\13\'\3\'\3\'\3\'\3(\3(\3(\5(\u0279\n(\3(\3(\3(\3)\3)\3)\3)\3)\6"+
		")\u0283\n)\r)\16)\u0284\3)\3)\3)\3)\3*\3*\6*\u028d\n*\r*\16*\u028e\3*"+
		"\3*\3*\3+\3+\3+\3+\3+\6+\u0299\n+\r+\16+\u029a\3+\3+\5+\u029f\n+\3+\3"+
		"+\3+\3,\3,\3,\3,\5,\u02a8\n,\3,\3,\3,\7,\u02ad\n,\f,\16,\u02b0\13,\3,"+
		"\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\6.\u02c3\n.\r.\16.\u02c4"+
		"\3/\3/\3/\5/\u02ca\n/\3/\3/\3/\5/\u02cf\n/\7/\u02d1\n/\f/\16/\u02d4\13"+
		"/\3/\3/\3/\3/\3\60\3\60\3\60\7\60\u02dd\n\60\f\60\16\60\u02e0\13\60\3"+
		"\61\3\61\3\61\7\61\u02e5\n\61\f\61\16\61\u02e8\13\61\3\62\5\62\u02eb\n"+
		"\62\3\62\3\62\3\63\3\63\3\63\5\63\u02f2\n\63\3\64\3\64\3\64\7\64\u02f7"+
		"\n\64\f\64\16\64\u02fa\13\64\3\65\3\65\3\65\5\65\u02ff\n\65\3\66\3\66"+
		"\3\66\7\66\u0304\n\66\f\66\16\66\u0307\13\66\3\67\3\67\3\67\7\67\u030c"+
		"\n\67\f\67\16\67\u030f\13\67\38\38\38\38\58\u0315\n8\39\39\39\39\59\u031b"+
		"\n9\3:\3:\3:\3:\5:\u0321\n:\3;\3;\3;\3;\5;\u0327\n;\3<\3<\3<\3<\5<\u032d"+
		"\n<\3=\3=\3=\3=\3=\3=\3=\7=\u0336\n=\f=\16=\u0339\13=\3>\3>\3>\5>\u033e"+
		"\n>\3?\7?\u0341\n?\f?\16?\u0344\13?\3?\3?\3@\3@\3@\5@\u034b\n@\3A\3A\3"+
		"A\3A\3A\3A\3A\3B\3B\3B\3B\3B\3B\3B\3C\3C\3C\7C\u035e\nC\fC\16C\u0361\13"+
		"C\3D\3D\3D\3D\3D\3D\7D\u0369\nD\fD\16D\u036c\13D\3E\3E\3E\3E\3E\3E\3F"+
		"\3F\3F\3G\3G\3G\3G\3H\3H\3H\3H\3H\3H\3H\5H\u0382\nH\3I\3I\3I\3I\3I\3I"+
		"\3I\3I\3I\3I\3I\3I\3I\3I\3I\5I\u0393\nI\3J\3J\3J\3J\3K\3K\3K\3L\3L\5L"+
		"\u039e\nL\3L\3L\3M\3M\3N\3N\3N\3N\3N\3O\3O\3O\3O\3O\3P\3P\3P\3Q\3Q\3Q"+
		"\5Q\u03b4\nQ\7Q\u03b6\nQ\fQ\16Q\u03b9\13Q\3Q\3Q\3R\3R\5R\u03bf\nR\3S\3"+
		"S\5S\u03c3\nS\3T\3T\3T\3T\3U\3U\3U\3U\5U\u03cd\nU\3U\3U\3U\5U\u03d2\n"+
		"U\3U\3U\3U\3U\3V\3V\3V\3V\3V\3V\3V\3V\5V\u03e0\nV\3V\3V\3V\3V\3V\7V\u03e7"+
		"\nV\fV\16V\u03ea\13V\3V\3V\3V\5V\u03ef\nV\3W\3W\3W\3W\3X\3X\3X\3X\3X\3"+
		"X\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Y\3Z\3Z\3Z\3Z\3Z\7Z\u0408\nZ\fZ\16Z\u040b\13Z"+
		"\3Z\3Z\3Z\3Z\3Z\3[\3[\3[\3[\3[\3[\3\\\3\\\3\\\6\\\u041b\n\\\r\\\16\\\u041c"+
		"\3]\3]\3]\3]\3^\7^\u0424\n^\f^\16^\u0427\13^\3_\3_\3_\3`\3`\5`\u042e\n"+
		"`\3a\3a\3a\3a\3a\3a\3a\3a\3a\3a\3a\3a\3a\5a\u043d\na\3b\3b\3b\3c\3c\3"+
		"c\3c\3c\3c\3d\3d\3d\3d\3e\3e\3e\3e\3f\3f\3f\3f\3g\3g\3g\3g\3g\3h\3h\5"+
		"h\u045b\nh\3h\3h\3h\3h\3h\3h\7h\u0463\nh\fh\16h\u0466\13h\3h\3h\3h\3i"+
		"\3i\3i\3i\3i\3i\3i\3i\3i\3j\3j\3j\3j\3j\6j\u0479\nj\rj\16j\u047a\3j\3"+
		"j\3j\3j\3k\3k\6k\u0483\nk\rk\16k\u0484\3k\3k\3k\3l\3l\3l\6l\u048d\nl\r"+
		"l\16l\u048e\3m\3m\3m\5m\u0494\nm\3m\3m\3m\5m\u0499\nm\7m\u049b\nm\fm\16"+
		"m\u049e\13m\3m\3m\3n\3n\3n\3n\3n\6n\u04a7\nn\rn\16n\u04a8\3n\3n\5n\u04ad"+
		"\nn\3n\3n\3n\3o\3o\3o\3o\5o\u04b6\no\3o\3o\3o\7o\u04bb\no\fo\16o\u04be"+
		"\13o\3o\3o\3o\3p\3p\3p\3p\3p\3p\7p\u04c9\np\fp\16p\u04cc\13p\3p\5p\u04cf"+
		"\np\3q\7q\u04d2\nq\fq\16q\u04d5\13q\3r\3r\3r\3r\3r\5r\u04dc\nr\3r\3r\5"+
		"r\u04e0\nr\3r\3r\3r\3s\3s\3s\3s\5s\u04e9\ns\3s\3s\5s\u04ed\ns\7s\u04ef"+
		"\ns\fs\16s\u04f2\13s\3t\3t\3t\3t\3t\3t\3u\3u\3u\3u\3u\3u\5u\u0500\nu\5"+
		"u\u0502\nu\3v\3v\3v\3v\7v\u0508\nv\fv\16v\u050b\13v\5v\u050d\nv\3v\3v"+
		"\3v\3v\3v\5v\u0514\nv\3w\3w\3w\5w\u0519\nw\3x\3x\5x\u051d\nx\3y\3y\3y"+
		"\3y\3y\3z\3z\3z\3z\3z\7z\u0529\nz\fz\16z\u052c\13z\5z\u052e\nz\3z\3z\3"+
		"z\3z\3{\3{\5{\u0536\n{\3|\3|\5|\u053a\n|\3|\3|\3|\3}\3}\5}\u0541\n}\3"+
		"}\3}\3~\3~\3\177\3\177\3\u0080\3\u0080\3\u0080\2\2\u0081\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bd"+
		"fhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092"+
		"\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa"+
		"\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2"+
		"\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da"+
		"\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2"+
		"\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\2\n\4\2\b\bkk\3\2TU\3\2\13\24\4\2"+
		"\6\6%/\3\2\61\62\4\2$$\63\65\4\2\n\n\u0082\u0082\4\2?\u0080\u0083\u0083"+
		"\2\u058e\2\u0100\3\2\2\2\4\u0108\3\2\2\2\6\u010e\3\2\2\2\b\u0111\3\2\2"+
		"\2\n\u0122\3\2\2\2\f\u012d\3\2\2\2\16\u0133\3\2\2\2\20\u0135\3\2\2\2\22"+
		"\u013f\3\2\2\2\24\u0141\3\2\2\2\26\u0146\3\2\2\2\30\u014a\3\2\2\2\32\u0150"+
		"\3\2\2\2\34\u0165\3\2\2\2\36\u016b\3\2\2\2 \u016d\3\2\2\2\"\u0180\3\2"+
		"\2\2$\u0191\3\2\2\2&\u01a1\3\2\2\2(\u01b5\3\2\2\2*\u01c4\3\2\2\2,\u01c6"+
		"\3\2\2\2.\u01ce\3\2\2\2\60\u01d4\3\2\2\2\62\u01e2\3\2\2\2\64\u01ec\3\2"+
		"\2\2\66\u01f0\3\2\2\28\u0200\3\2\2\2:\u0209\3\2\2\2<\u0219\3\2\2\2>\u0222"+
		"\3\2\2\2@\u022a\3\2\2\2B\u022d\3\2\2\2D\u0237\3\2\2\2F\u0249\3\2\2\2H"+
		"\u0253\3\2\2\2J\u0263\3\2\2\2L\u0268\3\2\2\2N\u0275\3\2\2\2P\u027d\3\2"+
		"\2\2R\u028c\3\2\2\2T\u0293\3\2\2\2V\u02a3\3\2\2\2X\u02b4\3\2\2\2Z\u02bd"+
		"\3\2\2\2\\\u02c6\3\2\2\2^\u02d9\3\2\2\2`\u02e1\3\2\2\2b\u02ea\3\2\2\2"+
		"d\u02ee\3\2\2\2f\u02f3\3\2\2\2h\u02fb\3\2\2\2j\u0300\3\2\2\2l\u0308\3"+
		"\2\2\2n\u0310\3\2\2\2p\u0316\3\2\2\2r\u031c\3\2\2\2t\u0322\3\2\2\2v\u0328"+
		"\3\2\2\2x\u032e\3\2\2\2z\u033d\3\2\2\2|\u0342\3\2\2\2~\u034a\3\2\2\2\u0080"+
		"\u034c\3\2\2\2\u0082\u0353\3\2\2\2\u0084\u035a\3\2\2\2\u0086\u0362\3\2"+
		"\2\2\u0088\u036d\3\2\2\2\u008a\u0373\3\2\2\2\u008c\u0376\3\2\2\2\u008e"+
		"\u037a\3\2\2\2\u0090\u0392\3\2\2\2\u0092\u0394\3\2\2\2\u0094\u0398\3\2"+
		"\2\2\u0096\u039b\3\2\2\2\u0098\u03a1\3\2\2\2\u009a\u03a3\3\2\2\2\u009c"+
		"\u03a8\3\2\2\2\u009e\u03ad\3\2\2\2\u00a0\u03b0\3\2\2\2\u00a2\u03be\3\2"+
		"\2\2\u00a4\u03c2\3\2\2\2\u00a6\u03c4\3\2\2\2\u00a8\u03c8\3\2\2\2\u00aa"+
		"\u03ee\3\2\2\2\u00ac\u03f0\3\2\2\2\u00ae\u03f4\3\2\2\2\u00b0\u03fa\3\2"+
		"\2\2\u00b2\u0402\3\2\2\2\u00b4\u0411\3\2\2\2\u00b6\u0417\3\2\2\2\u00b8"+
		"\u041e\3\2\2\2\u00ba\u0425\3\2\2\2\u00bc\u0428\3\2\2\2\u00be\u042b\3\2"+
		"\2\2\u00c0\u043c\3\2\2\2\u00c2\u043e\3\2\2\2\u00c4\u0441\3\2\2\2\u00c6"+
		"\u0447\3\2\2\2\u00c8\u044b\3\2\2\2\u00ca\u044f\3\2\2\2\u00cc\u0453\3\2"+
		"\2\2\u00ce\u045a\3\2\2\2\u00d0\u046a\3\2\2\2\u00d2\u0473\3\2\2\2\u00d4"+
		"\u0482\3\2\2\2\u00d6\u0489\3\2\2\2\u00d8\u0490\3\2\2\2\u00da\u04a1\3\2"+
		"\2\2\u00dc\u04b1\3\2\2\2\u00de\u04c2\3\2\2\2\u00e0\u04d3\3\2\2\2\u00e2"+
		"\u04d6\3\2\2\2\u00e4\u04f0\3\2\2\2\u00e6\u04f3\3\2\2\2\u00e8\u0501\3\2"+
		"\2\2\u00ea\u0513\3\2\2\2\u00ec\u0518\3\2\2\2\u00ee\u051c\3\2\2\2\u00f0"+
		"\u051e\3\2\2\2\u00f2\u0523\3\2\2\2\u00f4\u0533\3\2\2\2\u00f6\u0539\3\2"+
		"\2\2\u00f8\u053e\3\2\2\2\u00fa\u0544\3\2\2\2\u00fc\u0546\3\2\2\2\u00fe"+
		"\u0548\3\2\2\2\u0100\u0101\5\4\3\2\u0101\u0102\7\2\2\3\u0102\3\3\2\2\2"+
		"\u0103\u0104\7j\2\2\u0104\u0105\7i\2\2\u0105\u0106\5\u00fc\177\2\u0106"+
		"\u0107\7\3\2\2\u0107\u0109\3\2\2\2\u0108\u0103\3\2\2\2\u0108\u0109\3\2"+
		"\2\2\u0109\u010c\3\2\2\2\u010a\u010d\5\b\5\2\u010b\u010d\5\6\4\2\u010c"+
		"\u010a\3\2\2\2\u010c\u010b\3\2\2\2\u010d\5\3\2\2\2\u010e\u010f\5\n\6\2"+
		"\u010f\u0110\5\f\7\2\u0110\7\3\2\2\2\u0111\u0112\7\4\2\2\u0112\u0113\7"+
		"\5\2\2\u0113\u0114\7\u008a\2\2\u0114\u0115\7\6\2\2\u0115\u0116\5\u00fa"+
		"~\2\u0116\u0117\7\3\2\2\u0117\u0118\5\n\6\2\u0118\t\3\2\2\2\u0119\u011d"+
		"\5\16\b\2\u011a\u011d\5\20\t\2\u011b\u011d\5 \21\2\u011c\u0119\3\2\2\2"+
		"\u011c\u011a\3\2\2\2\u011c\u011b\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u011f"+
		"\7\3\2\2\u011f\u0121\3\2\2\2\u0120\u011c\3\2\2\2\u0121\u0124\3\2\2\2\u0122"+
		"\u0120\3\2\2\2\u0122\u0123\3\2\2\2\u0123\u012a\3\2\2\2\u0124\u0122\3\2"+
		"\2\2\u0125\u0126\5\22\n\2\u0126\u0127\7\3\2\2\u0127\u0129\3\2\2\2\u0128"+
		"\u0125\3\2\2\2\u0129\u012c\3\2\2\2\u012a\u0128\3\2\2\2\u012a\u012b\3\2"+
		"\2\2\u012b\13\3\2\2\2\u012c\u012a\3\2\2\2\u012d\u012e\5\u00be`\2\u012e"+
		"\r\3\2\2\2\u012f\u0134\5\24\13\2\u0130\u0134\5\26\f\2\u0131\u0134\5\30"+
		"\r\2\u0132\u0134\5\32\16\2\u0133\u012f\3\2\2\2\u0133\u0130\3\2\2\2\u0133"+
		"\u0131\3\2\2\2\u0133\u0132\3\2\2\2\u0134\17\3\2\2\2\u0135\u0136\7q\2\2"+
		"\u0136\u0137\7\5\2\2\u0137\u0138\7\u008a\2\2\u0138\u0139\7\6\2\2\u0139"+
		"\u013a\5\u00fa~\2\u013a\21\3\2\2\2\u013b\u0140\5&\24\2\u013c\u0140\5\""+
		"\22\2\u013d\u0140\5(\25\2\u013e\u0140\5$\23\2\u013f\u013b\3\2\2\2\u013f"+
		"\u013c\3\2\2\2\u013f\u013d\3\2\2\2\u013f\u013e\3\2\2\2\u0140\23\3\2\2"+
		"\2\u0141\u0142\7q\2\2\u0142\u0143\7Z\2\2\u0143\u0144\7S\2\2\u0144\u0145"+
		"\5\u00fa~\2\u0145\25\3\2\2\2\u0146\u0147\7q\2\2\u0147\u0148\7\7\2\2\u0148"+
		"\u0149\t\2\2\2\u0149\27\3\2\2\2\u014a\u014b\7q\2\2\u014b\u014c\7Z\2\2"+
		"\u014c\u014d\7D\2\2\u014d\u014e\7K\2\2\u014e\u014f\t\3\2\2\u014f\31\3"+
		"\2\2\2\u0150\u0155\7q\2\2\u0151\u0152\7\t\2\2\u0152\u0156\5\34\17\2\u0153"+
		"\u0154\7Z\2\2\u0154\u0156\7\t\2\2\u0155\u0151\3\2\2\2\u0155\u0153\3\2"+
		"\2\2\u0156\u015d\3\2\2\2\u0157\u0158\5\36\20\2\u0158\u0159\7\6\2\2\u0159"+
		"\u015a\5\u00fc\177\2\u015a\u015c\3\2\2\2\u015b\u0157\3\2\2\2\u015c\u015f"+
		"\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e\3\2\2\2\u015e\33\3\2\2\2\u015f"+
		"\u015d\3\2\2\2\u0160\u0163\7\u008a\2\2\u0161\u0163\5\u00fe\u0080\2\u0162"+
		"\u0160\3\2\2\2\u0162\u0161\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0166\7\n"+
		"\2\2\u0165\u0162\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0169\3\2\2\2\u0167"+
		"\u016a\7\u008a\2\2\u0168\u016a\5\u00fe\u0080\2\u0169\u0167\3\2\2\2\u0169"+
		"\u0168\3\2\2\2\u016a\35\3\2\2\2\u016b\u016c\t\4\2\2\u016c\37\3\2\2\2\u016d"+
		"\u016e\7\25\2\2\u016e\u0172\7\4\2\2\u016f\u0170\7\5\2\2\u0170\u0171\7"+
		"\u008a\2\2\u0171\u0173\7\6\2\2\u0172\u016f\3\2\2\2\u0172\u0173\3\2\2\2"+
		"\u0173\u0174\3\2\2\2\u0174\u017e\5\u00fa~\2\u0175\u0176\7I\2\2\u0176\u017b"+
		"\5\u00fa~\2\u0177\u0178\7\26\2\2\u0178\u017a\5\u00fa~\2\u0179\u0177\3"+
		"\2\2\2\u017a\u017d\3\2\2\2\u017b\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017c"+
		"\u017f\3\2\2\2\u017d\u017b\3\2\2\2\u017e\u0175\3\2\2\2\u017e\u017f\3\2"+
		"\2\2\u017f!\3\2\2\2\u0180\u0181\7q\2\2\u0181\u0182\5\u00e0q\2\u0182\u0183"+
		"\7t\2\2\u0183\u0186\5\u0094K\2\u0184\u0185\7H\2\2\u0185\u0187\5\u00e8"+
		"u\2\u0186\u0184\3\2\2\2\u0186\u0187\3\2\2\2\u0187\u018f\3\2\2\2\u0188"+
		"\u0189\7\27\2\2\u0189\u0190\5\62\32\2\u018a\u018d\7\30\2\2\u018b\u018c"+
		"\7\27\2\2\u018c\u018e\5\62\32\2\u018d\u018b\3\2\2\2\u018d\u018e\3\2\2"+
		"\2\u018e\u0190\3\2\2\2\u018f\u0188\3\2\2\2\u018f\u018a\3\2\2\2\u0190#"+
		"\3\2\2\2\u0191\u0192\7q\2\2\u0192\u0193\7r\2\2\u0193\u0196\7s\2\2\u0194"+
		"\u0195\7H\2\2\u0195\u0197\5\u00e8u\2\u0196\u0194\3\2\2\2\u0196\u0197\3"+
		"\2\2\2\u0197\u019f\3\2\2\2\u0198\u0199\7\27\2\2\u0199\u01a0\5\62\32\2"+
		"\u019a\u019d\7\30\2\2\u019b\u019c\7\27\2\2\u019c\u019e\5\62\32\2\u019d"+
		"\u019b\3\2\2\2\u019d\u019e\3\2\2\2\u019e\u01a0\3\2\2\2\u019f\u0198\3\2"+
		"\2\2\u019f\u019a\3\2\2\2\u01a0%\3\2\2\2\u01a1\u01a2\7q\2\2\u01a2\u01a3"+
		"\5\u00e0q\2\u01a3\u01a4\7\31\2\2\u01a4\u01a5\5\34\17\2\u01a5\u01a7\7\32"+
		"\2\2\u01a6\u01a8\5,\27\2\u01a7\u01a6\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8"+
		"\u01a9\3\2\2\2\u01a9\u01ac\7\33\2\2\u01aa\u01ab\7H\2\2\u01ab\u01ad\5\u00e8"+
		"u\2\u01ac\u01aa\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad\u01b3\3\2\2\2\u01ae"+
		"\u01af\7\34\2\2\u01af\u01b0\5\u00be`\2\u01b0\u01b1\7\35\2\2\u01b1\u01b4"+
		"\3\2\2\2\u01b2\u01b4\7\30\2\2\u01b3\u01ae\3\2\2\2\u01b3\u01b2\3\2\2\2"+
		"\u01b4\'\3\2\2\2\u01b5\u01b6\7q\2\2\u01b6\u01b7\7n\2\2\u01b7\u01b8\5\34"+
		"\17\2\u01b8\u01ba\7H\2\2\u01b9\u01bb\5*\26\2\u01ba\u01b9\3\2\2\2\u01ba"+
		"\u01bb\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01bd\5\62\32\2\u01bd)\3\2\2"+
		"\2\u01be\u01bf\7\36\2\2\u01bf\u01c5\7\37\2\2\u01c0\u01c1\7\36\2\2\u01c1"+
		"\u01c5\7 \2\2\u01c2\u01c3\7~\2\2\u01c3\u01c5\7!\2\2\u01c4\u01be\3\2\2"+
		"\2\u01c4\u01c0\3\2\2\2\u01c4\u01c2\3\2\2\2\u01c5+\3\2\2\2\u01c6\u01cb"+
		"\5.\30\2\u01c7\u01c8\7\26\2\2\u01c8\u01ca\5.\30\2\u01c9\u01c7\3\2\2\2"+
		"\u01ca\u01cd\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc-\3"+
		"\2\2\2\u01cd\u01cb\3\2\2\2\u01ce\u01cf\7\"\2\2\u01cf\u01d2\5\34\17\2\u01d0"+
		"\u01d1\7H\2\2\u01d1\u01d3\5\u00e8u\2\u01d2\u01d0\3\2\2\2\u01d2\u01d3\3"+
		"\2\2\2\u01d3/\3\2\2\2\u01d4\u01d9\5\62\32\2\u01d5\u01d6\7\26\2\2\u01d6"+
		"\u01d8\5\62\32\2\u01d7\u01d5\3\2\2\2\u01d8\u01db\3\2\2\2\u01d9\u01d7\3"+
		"\2\2\2\u01d9\u01da\3\2\2\2\u01da\61\3\2\2\2\u01db\u01d9\3\2\2\2\u01dc"+
		"\u01e3\5\64\33\2\u01dd\u01e3\5\66\34\2\u01de\u01e3\5P)\2\u01df\u01e3\5"+
		"T+\2\u01e0\u01e3\5X-\2\u01e1\u01e3\5Z.\2\u01e2\u01dc\3\2\2\2\u01e2\u01dd"+
		"\3\2\2\2\u01e2\u01de\3\2\2\2\u01e2\u01df\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e2"+
		"\u01e1\3\2\2\2\u01e3\63\3\2\2\2\u01e4\u01ed\5L\'\2\u01e5\u01ed\5^\60\2"+
		"\u01e6\u01ed\5\u00aaV\2\u01e7\u01ed\5\u00acW\2\u01e8\u01ed\5\u00aeX\2"+
		"\u01e9\u01ed\5\u00b0Y\2\u01ea\u01ed\5\u00b2Z\2\u01eb\u01ed\5\u00b4[\2"+
		"\u01ec\u01e4\3\2\2\2\u01ec\u01e5\3\2\2\2\u01ec\u01e6\3\2\2\2\u01ec\u01e7"+
		"\3\2\2\2\u01ec\u01e8\3\2\2\2\u01ec\u01e9\3\2\2\2\u01ec\u01ea\3\2\2\2\u01ec"+
		"\u01eb\3\2\2\2\u01ed\65\3\2\2\2\u01ee\u01f1\58\35\2\u01ef\u01f1\5<\37"+
		"\2\u01f0\u01ee\3\2\2\2\u01f0\u01ef\3\2\2\2\u01f1\u01fa\3\2\2\2\u01f2\u01f9"+
		"\58\35\2\u01f3\u01f9\5<\37\2\u01f4\u01f9\5@!\2\u01f5\u01f9\5B\"\2\u01f6"+
		"\u01f9\5F$\2\u01f7\u01f9\5J&\2\u01f8\u01f2\3\2\2\2\u01f8\u01f3\3\2\2\2"+
		"\u01f8\u01f4\3\2\2\2\u01f8\u01f5\3\2\2\2\u01f8\u01f6\3\2\2\2\u01f8\u01f7"+
		"\3\2\2\2\u01f9\u01fc\3\2\2\2\u01fa\u01f8\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb"+
		"\u01fd\3\2\2\2\u01fc\u01fa\3\2\2\2\u01fd\u01fe\7E\2\2\u01fe\u01ff\5\62"+
		"\32\2\u01ff\67\3\2\2\2\u0200\u0201\7?\2\2\u0201\u0206\5:\36\2\u0202\u0203"+
		"\7\26\2\2\u0203\u0205\5:\36\2\u0204\u0202\3\2\2\2\u0205\u0208\3\2\2\2"+
		"\u0206\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u02079\3\2\2\2\u0208\u0206\3"+
		"\2\2\2\u0209\u020c\5\u0094K\2\u020a\u020b\7H\2\2\u020b\u020d\5\u00e8u"+
		"\2\u020c\u020a\3\2\2\2\u020c\u020d\3\2\2\2\u020d\u0210\3\2\2\2\u020e\u020f"+
		"\7J\2\2\u020f\u0211\7K\2\2\u0210\u020e\3\2\2\2\u0210\u0211\3\2\2\2\u0211"+
		"\u0214\3\2\2\2\u0212\u0213\7I\2\2\u0213\u0215\5\u0094K\2\u0214\u0212\3"+
		"\2\2\2\u0214\u0215\3\2\2\2\u0215\u0216\3\2\2\2\u0216\u0217\7G\2\2\u0217"+
		"\u0218\5\62\32\2\u0218;\3\2\2\2\u0219\u021a\7@\2\2\u021a\u021f\5> \2\u021b"+
		"\u021c\7\26\2\2\u021c\u021e\5> \2\u021d\u021b\3\2\2\2\u021e\u0221\3\2"+
		"\2\2\u021f\u021d\3\2\2\2\u021f\u0220\3\2\2\2\u0220=\3\2\2\2\u0221\u021f"+
		"\3\2\2\2\u0222\u0225\5\u0094K\2\u0223\u0224\7H\2\2\u0224\u0226\5\u00e8"+
		"u\2\u0225\u0223\3\2\2\2\u0225\u0226\3\2\2\2\u0226\u0227\3\2\2\2\u0227"+
		"\u0228\7\27\2\2\u0228\u0229\5\62\32\2\u0229?\3\2\2\2\u022a\u022b\7A\2"+
		"\2\u022b\u022c\5\62\32\2\u022cA\3\2\2\2\u022d\u022e\7B\2\2\u022e\u022f"+
		"\7C\2\2\u022f\u0234\5D#\2\u0230\u0231\7\26\2\2\u0231\u0233\5D#\2\u0232"+
		"\u0230\3\2\2\2\u0233\u0236\3\2\2\2\u0234\u0232\3\2\2\2\u0234\u0235\3\2"+
		"\2\2\u0235C\3\2\2\2\u0236\u0234\3\2\2\2\u0237\u023e\5\u0094K\2\u0238\u0239"+
		"\7H\2\2\u0239\u023b\5\u00e8u\2\u023a\u0238\3\2\2\2\u023a\u023b\3\2\2\2"+
		"\u023b\u023c\3\2\2\2\u023c\u023d\7\27\2\2\u023d\u023f\5\62\32\2\u023e"+
		"\u023a\3\2\2\2\u023e\u023f\3\2\2\2\u023f\u0242\3\2\2\2\u0240\u0241\7S"+
		"\2\2\u0241\u0243\5\u00fa~\2\u0242\u0240\3\2\2\2\u0242\u0243\3\2\2\2\u0243"+
		"E\3\2\2\2\u0244\u0245\7D\2\2\u0245\u024a\7C\2\2\u0246\u0247\7M\2\2\u0247"+
		"\u0248\7D\2\2\u0248\u024a\7C\2\2\u0249\u0244\3\2\2\2\u0249\u0246\3\2\2"+
		"\2\u024a\u024b\3\2\2\2\u024b\u0250\5H%\2\u024c\u024d\7\26\2\2\u024d\u024f"+
		"\5H%\2\u024e\u024c\3\2\2\2\u024f\u0252\3\2\2\2\u0250\u024e\3\2\2\2\u0250"+
		"\u0251\3\2\2\2\u0251G\3\2\2\2\u0252\u0250\3\2\2\2\u0253\u0256\5\62\32"+
		"\2\u0254\u0257\7N\2\2\u0255\u0257\7O\2\2\u0256\u0254\3\2\2\2\u0256\u0255"+
		"\3\2\2\2\u0256\u0257\3\2\2\2\u0257\u025d\3\2\2\2\u0258\u025b\7K\2\2\u0259"+
		"\u025c\7T\2\2\u025a\u025c\7U\2\2\u025b\u0259\3\2\2\2\u025b\u025a\3\2\2"+
		"\2\u025c\u025e\3\2\2\2\u025d\u0258\3\2\2\2\u025d\u025e\3\2\2\2\u025e\u0261"+
		"\3\2\2\2\u025f\u0260\7S\2\2\u0260\u0262\5\u00fa~\2\u0261\u025f\3\2\2\2"+
		"\u0261\u0262\3\2\2\2\u0262I\3\2\2\2\u0263\u0264\7L\2\2\u0264\u0265\5\u0094"+
		"K\2\u0265K\3\2\2\2\u0266\u0269\7P\2\2\u0267\u0269\7Q\2\2\u0268\u0266\3"+
		"\2\2\2\u0268\u0267\3\2\2\2\u0269\u026a\3\2\2\2\u026a\u026f\5N(\2\u026b"+
		"\u026c\7\26\2\2\u026c\u026e\5N(\2\u026d\u026b\3\2\2\2\u026e\u0271\3\2"+
		"\2\2\u026f\u026d\3\2\2\2\u026f\u0270\3\2\2\2\u0270\u0272\3\2\2\2\u0271"+
		"\u026f\3\2\2\2\u0272\u0273\7R\2\2\u0273\u0274\5\62\32\2\u0274M\3\2\2\2"+
		"\u0275\u0278\5\u0094K\2\u0276\u0277\7H\2\2\u0277\u0279\5\u00e8u\2\u0278"+
		"\u0276\3\2\2\2\u0278\u0279\3\2\2\2\u0279\u027a\3\2\2\2\u027a\u027b\7G"+
		"\2\2\u027b\u027c\5\62\32\2\u027cO\3\2\2\2\u027d\u027e\7V\2\2\u027e\u027f"+
		"\7\32\2\2\u027f\u0280\5\60\31\2\u0280\u0282\7\33\2\2\u0281\u0283\5R*\2"+
		"\u0282\u0281\3\2\2\2\u0283\u0284\3\2\2\2\u0284\u0282\3\2\2\2\u0284\u0285"+
		"\3\2\2\2\u0285\u0286\3\2\2\2\u0286\u0287\7Z\2\2\u0287\u0288\7E\2\2\u0288"+
		"\u0289\5\62\32\2\u0289Q\3\2\2\2\u028a\u028b\7W\2\2\u028b\u028d\5\62\32"+
		"\2\u028c\u028a\3\2\2\2\u028d\u028e\3\2\2\2\u028e\u028c\3\2\2\2\u028e\u028f"+
		"\3\2\2\2\u028f\u0290\3\2\2\2\u0290\u0291\7E\2\2\u0291\u0292\5\62\32\2"+
		"\u0292S\3\2\2\2\u0293\u0294\7]\2\2\u0294\u0295\7\32\2\2\u0295\u0296\5"+
		"\60\31\2\u0296\u0298\7\33\2\2\u0297\u0299\5V,\2\u0298\u0297\3\2\2\2\u0299"+
		"\u029a\3\2\2\2\u029a\u0298\3\2\2\2\u029a\u029b\3\2\2\2\u029b\u029c\3\2"+
		"\2\2\u029c\u029e\7Z\2\2\u029d\u029f\5\u0094K\2\u029e\u029d\3\2\2\2\u029e"+
		"\u029f\3\2\2\2\u029f\u02a0\3\2\2\2\u02a0\u02a1\7E\2\2\u02a1\u02a2\5\62"+
		"\32\2\u02a2U\3\2\2\2\u02a3\u02a7\7W\2\2\u02a4\u02a5\5\u0094K\2\u02a5\u02a6"+
		"\7H\2\2\u02a6\u02a8\3\2\2\2\u02a7\u02a4\3\2\2\2\u02a7\u02a8\3\2\2\2\u02a8"+
		"\u02a9\3\2\2\2\u02a9\u02ae\5\u00e8u\2\u02aa\u02ab\7#\2\2\u02ab\u02ad\5"+
		"\u00e8u\2\u02ac\u02aa\3\2\2\2\u02ad\u02b0\3\2\2\2\u02ae\u02ac\3\2\2\2"+
		"\u02ae\u02af\3\2\2\2\u02af\u02b1\3\2\2\2\u02b0\u02ae\3\2\2\2\u02b1\u02b2"+
		"\7E\2\2\u02b2\u02b3\5\62\32\2\u02b3W\3\2\2\2\u02b4\u02b5\7F\2\2\u02b5"+
		"\u02b6\7\32\2\2\u02b6\u02b7\5\60\31\2\u02b7\u02b8\7\33\2\2\u02b8\u02b9"+
		"\7[\2\2\u02b9\u02ba\5\62\32\2\u02ba\u02bb\7\\\2\2\u02bb\u02bc\5\62\32"+
		"\2\u02bcY\3\2\2\2\u02bd\u02be\7X\2\2\u02be\u02bf\7\34\2\2\u02bf\u02c0"+
		"\5\60\31\2\u02c0\u02c2\7\35\2\2\u02c1\u02c3\5\\/\2\u02c2\u02c1\3\2\2\2"+
		"\u02c3\u02c4\3\2\2\2\u02c4\u02c2\3\2\2\2\u02c4\u02c5\3\2\2\2\u02c5[\3"+
		"\2\2\2\u02c6\u02c9\7Y\2\2\u02c7\u02ca\7$\2\2\u02c8\u02ca\5\34\17\2\u02c9"+
		"\u02c7\3\2\2\2\u02c9\u02c8\3\2\2\2\u02ca\u02d2\3\2\2\2\u02cb\u02ce\7#"+
		"\2\2\u02cc\u02cf\7$\2\2\u02cd\u02cf\5\34\17\2\u02ce\u02cc\3\2\2\2\u02ce"+
		"\u02cd\3\2\2\2\u02cf\u02d1\3\2\2\2\u02d0\u02cb\3\2\2\2\u02d1\u02d4\3\2"+
		"\2\2\u02d2\u02d0\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3\u02d5\3\2\2\2\u02d4"+
		"\u02d2\3\2\2\2\u02d5\u02d6\7\34\2\2\u02d6\u02d7\5\60\31\2\u02d7\u02d8"+
		"\7\35\2\2\u02d8]\3\2\2\2\u02d9\u02de\5`\61\2\u02da\u02db\7^\2\2\u02db"+
		"\u02dd\5`\61\2\u02dc\u02da\3\2\2\2\u02dd\u02e0\3\2\2\2\u02de\u02dc\3\2"+
		"\2\2\u02de\u02df\3\2\2\2\u02df_\3\2\2\2\u02e0\u02de\3\2\2\2\u02e1\u02e6"+
		"\5b\62\2\u02e2\u02e3\7_\2\2\u02e3\u02e5\5b\62\2\u02e4\u02e2\3\2\2\2\u02e5"+
		"\u02e8\3\2\2\2\u02e6\u02e4\3\2\2\2\u02e6\u02e7\3\2\2\2\u02e7a\3\2\2\2"+
		"\u02e8\u02e6\3\2\2\2\u02e9\u02eb\7`\2\2\u02ea\u02e9\3\2\2\2\u02ea\u02eb"+
		"\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec\u02ed\5d\63\2\u02edc\3\2\2\2\u02ee"+
		"\u02f1\5f\64\2\u02ef\u02f0\t\5\2\2\u02f0\u02f2\5f\64\2\u02f1\u02ef\3\2"+
		"\2\2\u02f1\u02f2\3\2\2\2\u02f2e\3\2\2\2\u02f3\u02f8\5h\65\2\u02f4\u02f5"+
		"\7\60\2\2\u02f5\u02f7\5h\65\2\u02f6\u02f4\3\2\2\2\u02f7\u02fa\3\2\2\2"+
		"\u02f8\u02f6\3\2\2\2\u02f8\u02f9\3\2\2\2\u02f9g\3\2\2\2\u02fa\u02f8\3"+
		"\2\2\2\u02fb\u02fe\5j\66\2\u02fc\u02fd\7a\2\2\u02fd\u02ff\5j\66\2\u02fe"+
		"\u02fc\3\2\2\2\u02fe\u02ff\3\2\2\2\u02ffi\3\2\2\2\u0300\u0305\5l\67\2"+
		"\u0301\u0302\t\6\2\2\u0302\u0304\5l\67\2\u0303\u0301\3\2\2\2\u0304\u0307"+
		"\3\2\2\2\u0305\u0303\3\2\2\2\u0305\u0306\3\2\2\2\u0306k\3\2\2\2\u0307"+
		"\u0305\3\2\2\2\u0308\u030d\5n8\2\u0309\u030a\t\7\2\2\u030a\u030c\5n8\2"+
		"\u030b\u0309\3\2\2\2\u030c\u030f\3\2\2\2\u030d\u030b\3\2\2\2\u030d\u030e"+
		"\3\2\2\2\u030em\3\2\2\2\u030f\u030d\3\2\2\2\u0310\u0314\5p9\2\u0311\u0312"+
		"\7b\2\2\u0312\u0313\7c\2\2\u0313\u0315\5\u00e8u\2\u0314\u0311\3\2\2\2"+
		"\u0314\u0315\3\2\2\2\u0315o\3\2\2\2\u0316\u031a\5r:\2\u0317\u0318\7e\2"+
		"\2\u0318\u0319\7d\2\2\u0319\u031b\5\u00e8u\2\u031a\u0317\3\2\2\2\u031a"+
		"\u031b\3\2\2\2\u031bq\3\2\2\2\u031c\u0320\5t;\2\u031d\u031e\7f\2\2\u031e"+
		"\u031f\7H\2\2\u031f\u0321\5\u00e8u\2\u0320\u031d\3\2\2\2\u0320\u0321\3"+
		"\2\2\2\u0321s\3\2\2\2\u0322\u0326\5v<\2\u0323\u0324\7h\2\2\u0324\u0325"+
		"\7H\2\2\u0325\u0327\5\u00f4{\2\u0326\u0323\3\2\2\2\u0326\u0327\3\2\2\2"+
		"\u0327u\3\2\2\2\u0328\u032c\5x=\2\u0329\u032a\7g\2\2\u032a\u032b\7H\2"+
		"\2\u032b\u032d\5\u00f4{\2\u032c\u0329\3\2\2\2\u032c\u032d\3\2\2\2\u032d"+
		"w\3\2\2\2\u032e\u0337\5|?\2\u032f\u0330\7\6\2\2\u0330\u0331\7.\2\2\u0331"+
		"\u0332\3\2\2\2\u0332\u0333\5z>\2\u0333\u0334\5\u00a0Q\2\u0334\u0336\3"+
		"\2\2\2\u0335\u032f\3\2\2\2\u0336\u0339\3\2\2\2\u0337\u0335\3\2\2\2\u0337"+
		"\u0338\3\2\2\2\u0338y\3\2\2\2\u0339\u0337\3\2\2\2\u033a\u033e\5\34\17"+
		"\2\u033b\u033e\5\u0094K\2\u033c\u033e\5\u0096L\2\u033d\u033a\3\2\2\2\u033d"+
		"\u033b\3\2\2\2\u033d\u033c\3\2\2\2\u033e{\3\2\2\2\u033f\u0341\t\6\2\2"+
		"\u0340\u033f\3\2\2\2\u0341\u0344\3\2\2\2\u0342\u0340\3\2\2\2\u0342\u0343"+
		"\3\2\2\2\u0343\u0345\3\2\2\2\u0344\u0342\3\2\2\2\u0345\u0346\5~@\2\u0346"+
		"}\3\2\2\2\u0347\u034b\5\u0084C\2\u0348\u034b\5\u0080A\2\u0349\u034b\5"+
		"\u0082B\2\u034a\u0347\3\2\2\2\u034a\u0348\3\2\2\2\u034a\u0349\3\2\2\2"+
		"\u034b\177\3\2\2\2\u034c\u034d\7o\2\2\u034d\u034e\7n\2\2\u034e\u034f\5"+
		"\u00e8u\2\u034f\u0350\7\34\2\2\u0350\u0351\5\60\31\2\u0351\u0352\7\35"+
		"\2\2\u0352\u0081\3\2\2\2\u0353\u0354\7p\2\2\u0354\u0355\7n\2\2\u0355\u0356"+
		"\5\u00e8u\2\u0356\u0357\7\34\2\2\u0357\u0358\5\60\31\2\u0358\u0359\7\35"+
		"\2\2\u0359\u0083\3\2\2\2\u035a\u035f\5\u0086D\2\u035b\u035c\7\66\2\2\u035c"+
		"\u035e\5\u0086D\2\u035d\u035b\3\2\2\2\u035e\u0361\3\2\2\2\u035f\u035d"+
		"\3\2\2\2\u035f\u0360\3\2\2\2\u0360\u0085\3\2\2\2\u0361\u035f\3\2\2\2\u0362"+
		"\u036a\5\u0090I\2\u0363\u0369\5\u0088E\2\u0364\u0369\5\u008cG\2\u0365"+
		"\u0369\5\u008eH\2\u0366\u0369\5\u008aF\2\u0367\u0369\5\u00a0Q\2\u0368"+
		"\u0363\3\2\2\2\u0368\u0364\3\2\2\2\u0368\u0365\3\2\2\2\u0368\u0366\3\2"+
		"\2\2\u0368\u0367\3\2\2\2\u0369\u036c\3\2\2\2\u036a\u0368\3\2\2\2\u036a"+
		"\u036b\3\2\2\2\u036b\u0087\3\2\2\2\u036c\u036a\3\2\2\2\u036d\u036e\7\67"+
		"\2\2\u036e\u036f\7\67\2\2\u036f\u0370\5\60\31\2\u0370\u0371\78\2\2\u0371"+
		"\u0372\78\2\2\u0372\u0089\3\2\2\2\u0373\u0374\7\67\2\2\u0374\u0375\78"+
		"\2\2\u0375\u008b\3\2\2\2\u0376\u0377\7\67\2\2\u0377\u0378\5\60\31\2\u0378"+
		"\u0379\78\2\2\u0379\u008d\3\2\2\2\u037a\u0381\79\2\2\u037b\u0382\5\u00fe"+
		"\u0080\2\u037c\u0382\5\u00fc\177\2\u037d\u0382\7\u008a\2\2\u037e\u0382"+
		"\5\u0096L\2\u037f\u0382\5\u0094K\2\u0380\u0382\5\u0098M\2\u0381\u037b"+
		"\3\2\2\2\u0381\u037c\3\2\2\2\u0381\u037d\3\2\2\2\u0381\u037e\3\2\2\2\u0381"+
		"\u037f\3\2\2\2\u0381\u0380\3\2\2\2\u0382\u008f\3\2\2\2\u0383\u0393\7\u0083"+
		"\2\2\u0384\u0393\7l\2\2\u0385\u0393\7m\2\2\u0386\u0393\7\u0084\2\2\u0387"+
		"\u0393\5\u00fc\177\2\u0388\u0393\5\u0094K\2\u0389\u0393\5\u0096L\2\u038a"+
		"\u0393\5\u0098M\2\u038b\u0393\5\u00eav\2\u038c\u0393\5\u009eP\2\u038d"+
		"\u0393\5\u009aN\2\u038e\u0393\5\u009cO\2\u038f\u0393\5\u00f8}\2\u0390"+
		"\u0393\5\u00a4S\2\u0391\u0393\5\u0092J\2\u0392\u0383\3\2\2\2\u0392\u0384"+
		"\3\2\2\2\u0392\u0385\3\2\2\2\u0392\u0386\3\2\2\2\u0392\u0387\3\2\2\2\u0392"+
		"\u0388\3\2\2\2\u0392\u0389\3\2\2\2\u0392\u038a\3\2\2\2\u0392\u038b\3\2"+
		"\2\2\u0392\u038c\3\2\2\2\u0392\u038d\3\2\2\2\u0392\u038e\3\2\2\2\u0392"+
		"\u038f\3\2\2\2\u0392\u0390\3\2\2\2\u0392\u0391\3\2\2\2\u0393\u0091\3\2"+
		"\2\2\u0394\u0395\7\34\2\2\u0395\u0396\5\u00bc_\2\u0396\u0397\7\35\2\2"+
		"\u0397\u0093\3\2\2\2\u0398\u0399\7\"\2\2\u0399\u039a\5\34\17\2\u039a\u0095"+
		"\3\2\2\2\u039b\u039d\7\32\2\2\u039c\u039e\5\60\31\2\u039d\u039c\3\2\2"+
		"\2\u039d\u039e\3\2\2\2\u039e\u039f\3\2\2\2\u039f\u03a0\7\33\2\2\u03a0"+
		"\u0097\3\2\2\2\u03a1\u03a2\7:\2\2\u03a2\u0099\3\2\2\2\u03a3\u03a4\7\b"+
		"\2\2\u03a4\u03a5\7\34\2\2\u03a5\u03a6\5\60\31\2\u03a6\u03a7\7\35\2\2\u03a7"+
		"\u009b\3\2\2\2\u03a8\u03a9\7k\2\2\u03a9\u03aa\7\34\2\2\u03aa\u03ab\5\60"+
		"\31\2\u03ab\u03ac\7\35\2\2\u03ac\u009d\3\2\2\2\u03ad\u03ae\5\34\17\2\u03ae"+
		"\u03af\5\u00a0Q\2\u03af\u009f\3\2\2\2\u03b0\u03b7\7\32\2\2\u03b1\u03b3"+
		"\5\u00a2R\2\u03b2\u03b4\7\26\2\2\u03b3\u03b2\3\2\2\2\u03b3\u03b4\3\2\2"+
		"\2\u03b4\u03b6\3\2\2\2\u03b5\u03b1\3\2\2\2\u03b6\u03b9\3\2\2\2\u03b7\u03b5"+
		"\3\2\2\2\u03b7\u03b8\3\2\2\2\u03b8\u03ba\3\2\2\2\u03b9\u03b7\3\2\2\2\u03ba"+
		"\u03bb\7\33\2\2\u03bb\u00a1\3\2\2\2\u03bc\u03bf\5\62\32\2\u03bd\u03bf"+
		"\7\u0082\2\2\u03be\u03bc\3\2\2\2\u03be\u03bd\3\2\2\2\u03bf\u00a3\3\2\2"+
		"\2\u03c0\u03c3\5\u00a6T\2\u03c1\u03c3\5\u00a8U\2\u03c2\u03c0\3\2\2\2\u03c2"+
		"\u03c1\3\2\2\2\u03c3\u00a5\3\2\2\2\u03c4\u03c5\5\34\17\2\u03c5\u03c6\7"+
		";\2\2\u03c6\u03c7\7\u0084\2\2\u03c7\u00a7\3\2\2\2\u03c8\u03c9\5\u00e0"+
		"q\2\u03c9\u03ca\7\31\2\2\u03ca\u03cc\7\32\2\2\u03cb\u03cd\5,\27\2\u03cc"+
		"\u03cb\3\2\2\2\u03cc\u03cd\3\2\2\2\u03cd\u03ce\3\2\2\2\u03ce\u03d1\7\33"+
		"\2\2\u03cf\u03d0\7H\2\2\u03d0\u03d2\5\u00e8u\2\u03d1\u03cf\3\2\2\2\u03d1"+
		"\u03d2\3\2\2\2\u03d2\u03d3\3\2\2\2\u03d3\u03d4\7\34\2\2\u03d4\u03d5\5"+
		"\u00be`\2\u03d5\u03d6\7\35\2\2\u03d6\u00a9\3\2\2\2\u03d7\u03d8\7u\2\2"+
		"\u03d8\u03d9\7~\2\2\u03d9\u03da\5\62\32\2\u03da\u03db\7|\2\2\u03db\u03df"+
		"\5\62\32\2\u03dc\u03dd\7I\2\2\u03dd\u03de\7\u0080\2\2\u03de\u03e0\5\62"+
		"\32\2\u03df\u03dc\3\2\2\2\u03df\u03e0\3\2\2\2\u03e0\u03ef\3\2\2\2\u03e1"+
		"\u03e2\7u\2\2\u03e2\u03e3\7~\2\2\u03e3\u03e8\5\u00f6|\2\u03e4\u03e5\7"+
		"\26\2\2\u03e5\u03e7\5\u00f6|\2\u03e6\u03e4\3\2\2\2\u03e7\u03ea\3\2\2\2"+
		"\u03e8\u03e6\3\2\2\2\u03e8\u03e9\3\2\2\2\u03e9\u03eb\3\2\2\2\u03ea\u03e8"+
		"\3\2\2\2\u03eb\u03ec\7|\2\2\u03ec\u03ed\5\62\32\2\u03ed\u03ef\3\2\2\2"+
		"\u03ee\u03d7\3\2\2\2\u03ee\u03e1\3\2\2\2\u03ef\u00ab\3\2\2\2\u03f0\u03f1"+
		"\7v\2\2\u03f1\u03f2\7~\2\2\u03f2\u03f3\5\u00b6\\\2\u03f3\u00ad\3\2\2\2"+
		"\u03f4\u03f5\7w\2\2\u03f5\u03f6\7~\2\2\u03f6\u03f7\5\u00b6\\\2\u03f7\u03f8"+
		"\7H\2\2\u03f8\u03f9\5\62\32\2\u03f9\u00af\3\2\2\2\u03fa\u03fb\7x\2\2\u03fb"+
		"\u03fc\7~\2\2\u03fc\u03fd\7}\2\2\u03fd\u03fe\7c\2\2\u03fe\u03ff\5\u00b6"+
		"\\\2\u03ff\u0400\7\177\2\2\u0400\u0401\5\62\32\2\u0401\u00b1\3\2\2\2\u0402"+
		"\u0403\7y\2\2\u0403\u0404\7~\2\2\u0404\u0409\5\u00b8]\2\u0405\u0406\7"+
		"\26\2\2\u0406\u0408\5\u00b8]\2\u0407\u0405\3\2\2\2\u0408\u040b\3\2\2\2"+
		"\u0409\u0407\3\2\2\2\u0409\u040a\3\2\2\2\u040a\u040c\3\2\2\2\u040b\u0409"+
		"\3\2\2\2\u040c\u040d\7z\2\2\u040d\u040e\5\62\32\2\u040e\u040f\7E\2\2\u040f"+
		"\u0410\5\62\32\2\u0410\u00b3\3\2\2\2\u0411\u0412\7{\2\2\u0412\u0413\7"+
		"~\2\2\u0413\u0414\5\62\32\2\u0414\u0415\7|\2\2\u0415\u0416\5\62\32\2\u0416"+
		"\u00b5\3\2\2\2\u0417\u041a\5\u0090I\2\u0418\u041b\5\u0088E\2\u0419\u041b"+
		"\5\u008eH\2\u041a\u0418\3\2\2\2\u041a\u0419\3\2\2\2\u041b\u041c\3\2\2"+
		"\2\u041c\u041a\3\2\2\2\u041c\u041d\3\2\2\2\u041d\u00b7\3\2\2\2\u041e\u041f"+
		"\5\u0094K\2\u041f\u0420\7\27\2\2\u0420\u0421\5\62\32\2\u0421\u00b9\3\2"+
		"\2\2\u0422\u0424\5\u00c0a\2\u0423\u0422\3\2\2\2\u0424\u0427\3\2\2\2\u0425"+
		"\u0423\3\2\2\2\u0425\u0426\3\2\2\2\u0426\u00bb\3\2\2\2\u0427\u0425\3\2"+
		"\2\2\u0428\u0429\5\u00ba^\2\u0429\u042a\5\60\31\2\u042a\u00bd\3\2\2\2"+
		"\u042b\u042d\5\u00ba^\2\u042c\u042e\5\60\31\2\u042d\u042c\3\2\2\2\u042d"+
		"\u042e\3\2\2\2\u042e\u00bf\3\2\2\2\u042f\u043d\5\u00c2b\2\u0430\u043d"+
		"\5\u00c4c\2\u0431\u043d\5\u00c6d\2\u0432\u043d\5\u00c8e\2\u0433\u043d"+
		"\5\u00caf\2\u0434\u043d\5\u00ccg\2\u0435\u043d\5\u00ceh\2\u0436\u043d"+
		"\5\u00d0i\2\u0437\u043d\5\u00d2j\2\u0438\u043d\5\u00d6l\2\u0439\u043d"+
		"\5\u00dan\2\u043a\u043d\5\u00e2r\2\u043b\u043d\5\u00e6t\2\u043c\u042f"+
		"\3\2\2\2\u043c\u0430\3\2\2\2\u043c\u0431\3\2\2\2\u043c\u0432\3\2\2\2\u043c"+
		"\u0433\3\2\2\2\u043c\u0434\3\2\2\2\u043c\u0435\3\2\2\2\u043c\u0436\3\2"+
		"\2\2\u043c\u0437\3\2\2\2\u043c\u0438\3\2\2\2\u043c\u0439\3\2\2\2\u043c"+
		"\u043a\3\2\2\2\u043c\u043b\3\2\2\2\u043d\u00c1\3\2\2\2\u043e\u043f\5\64"+
		"\33\2\u043f\u0440\7\3\2\2\u0440\u00c3\3\2\2\2\u0441\u0442\7\"\2\2\u0442"+
		"\u0443\5\34\17\2\u0443\u0444\7\27\2\2\u0444\u0445\5\62\32\2\u0445\u0446"+
		"\7\3\2\2\u0446\u00c5\3\2\2\2\u0447\u0448\7\34\2\2\u0448\u0449\5\u00ba"+
		"^\2\u0449\u044a\7\35\2\2\u044a\u00c7\3\2\2\2\u044b\u044c\7\u008d\2\2\u044c"+
		"\u044d\7\u008e\2\2\u044d\u044e\7\3\2\2\u044e\u00c9\3\2\2\2\u044f\u0450"+
		"\7\u008f\2\2\u0450\u0451\7\u008e\2\2\u0451\u0452\7\3\2\2\u0452\u00cb\3"+
		"\2\2\2\u0453\u0454\7\u0090\2\2\u0454\u0455\7\u0091\2\2\u0455\u0456\5\62"+
		"\32\2\u0456\u0457\7\3\2\2\u0457\u00cd\3\2\2\2\u0458\u045b\58\35\2\u0459"+
		"\u045b\5<\37\2\u045a\u0458\3\2\2\2\u045a\u0459\3\2\2\2\u045b\u0464\3\2"+
		"\2\2\u045c\u0463\58\35\2\u045d\u0463\5<\37\2\u045e\u0463\5@!\2\u045f\u0463"+
		"\5B\"\2\u0460\u0463\5F$\2\u0461\u0463\5J&\2\u0462\u045c\3\2\2\2\u0462"+
		"\u045d\3\2\2\2\u0462\u045e\3\2\2\2\u0462\u045f\3\2\2\2\u0462\u0460\3\2"+
		"\2\2\u0462\u0461\3\2\2\2\u0463\u0466\3\2\2\2\u0464\u0462\3\2\2\2\u0464"+
		"\u0465\3\2\2\2\u0465\u0467\3\2\2\2\u0466\u0464\3\2\2\2\u0467\u0468\7E"+
		"\2\2\u0468\u0469\5\u00c0a\2\u0469\u00cf\3\2\2\2\u046a\u046b\7F\2\2\u046b"+
		"\u046c\7\32\2\2\u046c\u046d\5\60\31\2\u046d\u046e\7\33\2\2\u046e\u046f"+
		"\7[\2\2\u046f\u0470\5\u00c0a\2\u0470\u0471\7\\\2\2\u0471\u0472\5\u00c0"+
		"a\2\u0472\u00d1\3\2\2\2\u0473\u0474\7V\2\2\u0474\u0475\7\32\2\2\u0475"+
		"\u0476\5\60\31\2\u0476\u0478\7\33\2\2\u0477\u0479\5\u00d4k\2\u0478\u0477"+
		"\3\2\2\2\u0479\u047a\3\2\2\2\u047a\u0478\3\2\2\2\u047a\u047b\3\2\2\2\u047b"+
		"\u047c\3\2\2\2\u047c\u047d\7Z\2\2\u047d\u047e\7E\2\2\u047e\u047f\5\u00c0"+
		"a\2\u047f\u00d3\3\2\2\2\u0480\u0481\7W\2\2\u0481\u0483\5\62\32\2\u0482"+
		"\u0480\3\2\2\2\u0483\u0484\3\2\2\2\u0484\u0482\3\2\2\2\u0484\u0485\3\2"+
		"\2\2\u0485\u0486\3\2\2\2\u0486\u0487\7E\2\2\u0487\u0488\5\u00c0a\2\u0488"+
		"\u00d5\3\2\2\2\u0489\u048a\7X\2\2\u048a\u048c\5\u00c6d\2\u048b\u048d\5"+
		"\u00d8m\2\u048c\u048b\3\2\2\2\u048d\u048e\3\2\2\2\u048e\u048c\3\2\2\2"+
		"\u048e\u048f\3\2\2\2\u048f\u00d7\3\2\2\2\u0490\u0493\7Y\2\2\u0491\u0494"+
		"\7$\2\2\u0492\u0494\5\34\17\2\u0493\u0491\3\2\2\2\u0493\u0492\3\2\2\2"+
		"\u0494\u049c\3\2\2\2\u0495\u0498\7#\2\2\u0496\u0499\7$\2\2\u0497\u0499"+
		"\5\34\17\2\u0498\u0496\3\2\2\2\u0498\u0497\3\2\2\2\u0499\u049b\3\2\2\2"+
		"\u049a\u0495\3\2\2\2\u049b\u049e\3\2\2\2\u049c\u049a\3\2\2\2\u049c\u049d"+
		"\3\2\2\2\u049d\u049f\3\2\2\2\u049e\u049c\3\2\2\2\u049f\u04a0\5\u00c6d"+
		"\2\u04a0\u00d9\3\2\2\2\u04a1\u04a2\7]\2\2\u04a2\u04a3\7\32\2\2\u04a3\u04a4"+
		"\5\60\31\2\u04a4\u04a6\7\33\2\2\u04a5\u04a7\5\u00dco\2\u04a6\u04a5\3\2"+
		"\2\2\u04a7\u04a8\3\2\2\2\u04a8\u04a6\3\2\2\2\u04a8\u04a9\3\2\2\2\u04a9"+
		"\u04aa\3\2\2\2\u04aa\u04ac\7Z\2\2\u04ab\u04ad\5\u0094K\2\u04ac\u04ab\3"+
		"\2\2\2\u04ac\u04ad\3\2\2\2\u04ad\u04ae\3\2\2\2\u04ae\u04af\7E\2\2\u04af"+
		"\u04b0\5\u00c0a\2\u04b0\u00db\3\2\2\2\u04b1\u04b5\7W\2\2\u04b2\u04b3\5"+
		"\u0094K\2\u04b3\u04b4\7H\2\2\u04b4\u04b6\3\2\2\2\u04b5\u04b2\3\2\2\2\u04b5"+
		"\u04b6\3\2\2\2\u04b6\u04b7\3\2\2\2\u04b7\u04bc\5\u00e8u\2\u04b8\u04b9"+
		"\7#\2\2\u04b9\u04bb\5\u00e8u\2\u04ba\u04b8\3\2\2\2\u04bb\u04be\3\2\2\2"+
		"\u04bc\u04ba\3\2\2\2\u04bc\u04bd\3\2\2\2\u04bd\u04bf\3\2\2\2\u04be\u04bc"+
		"\3\2\2\2\u04bf\u04c0\7E\2\2\u04c0\u04c1\5\u00c0a\2\u04c1\u00dd\3\2\2\2"+
		"\u04c2\u04c3\7<\2\2\u04c3\u04ce\5\34\17\2\u04c4\u04c5\7\32\2\2\u04c5\u04ca"+
		"\7\u0084\2\2\u04c6\u04c7\7\26\2\2\u04c7\u04c9\7\u0084\2\2\u04c8\u04c6"+
		"\3\2\2\2\u04c9\u04cc\3\2\2\2\u04ca\u04c8\3\2\2\2\u04ca\u04cb\3\2\2\2\u04cb"+
		"\u04cd\3\2\2\2\u04cc\u04ca\3\2\2\2\u04cd\u04cf\7\33\2\2\u04ce\u04c4\3"+
		"\2\2\2\u04ce\u04cf\3\2\2\2\u04cf\u00df\3\2\2\2\u04d0\u04d2\5\u00dep\2"+
		"\u04d1\u04d0\3\2\2\2\u04d2\u04d5\3\2\2\2\u04d3\u04d1\3\2\2\2\u04d3\u04d4"+
		"\3\2\2\2\u04d4\u00e1\3\2\2\2\u04d5\u04d3\3\2\2\2\u04d6\u04d7\5\u00e0q"+
		"\2\u04d7\u04d8\7t\2\2\u04d8\u04db\5\u0094K\2\u04d9\u04da\7H\2\2\u04da"+
		"\u04dc\5\u00e8u\2\u04db\u04d9\3\2\2\2\u04db\u04dc\3\2\2\2\u04dc\u04df"+
		"\3\2\2\2\u04dd\u04de\7\27\2\2\u04de\u04e0\5\62\32\2\u04df\u04dd\3\2\2"+
		"\2\u04df\u04e0\3\2\2\2\u04e0\u04e1\3\2\2\2\u04e1\u04e2\5\u00e4s\2\u04e2"+
		"\u04e3\7\3\2\2\u04e3\u00e3\3\2\2\2\u04e4\u04e5\7\26\2\2\u04e5\u04e8\5"+
		"\u0094K\2\u04e6\u04e7\7H\2\2\u04e7\u04e9\5\u00e8u\2\u04e8\u04e6\3\2\2"+
		"\2\u04e8\u04e9\3\2\2\2\u04e9\u04ec\3\2\2\2\u04ea\u04eb\7\27\2\2\u04eb"+
		"\u04ed\5\62\32\2\u04ec\u04ea\3\2\2\2\u04ec\u04ed\3\2\2\2\u04ed\u04ef\3"+
		"\2\2\2\u04ee\u04e4\3\2\2\2\u04ef\u04f2\3\2\2\2\u04f0\u04ee\3\2\2\2\u04f0"+
		"\u04f1\3\2\2\2\u04f1\u00e5\3\2\2\2\u04f2\u04f0\3\2\2\2\u04f3\u04f4\7\u0092"+
		"\2\2\u04f4\u04f5\7\32\2\2\u04f5\u04f6\5\60\31\2\u04f6\u04f7\7\33\2\2\u04f7"+
		"\u04f8\5\u00c0a\2\u04f8\u00e7\3\2\2\2\u04f9\u04fa\7\32\2\2\u04fa\u0502"+
		"\7\33\2\2\u04fb\u04ff\5\u00ecw\2\u04fc\u0500\7\u0082\2\2\u04fd\u0500\7"+
		"$\2\2\u04fe\u0500\7\61\2\2\u04ff\u04fc\3\2\2\2\u04ff\u04fd\3\2\2\2\u04ff"+
		"\u04fe\3\2\2\2\u04ff\u0500\3\2\2\2\u0500\u0502\3\2\2\2\u0501\u04f9\3\2"+
		"\2\2\u0501\u04fb\3\2\2\2\u0502\u00e9\3\2\2\2\u0503\u050c\7\34\2\2\u0504"+
		"\u0509\5\u00f6|\2\u0505\u0506\7\26\2\2\u0506\u0508\5\u00f6|\2\u0507\u0505"+
		"\3\2\2\2\u0508\u050b\3\2\2\2\u0509\u0507\3\2\2\2\u0509\u050a\3\2\2\2\u050a"+
		"\u050d\3\2\2\2\u050b\u0509\3\2\2\2\u050c\u0504\3\2\2\2\u050c\u050d\3\2"+
		"\2\2\u050d\u050e\3\2\2\2\u050e\u0514\7\35\2\2\u050f\u0510\7=\2\2\u0510"+
		"\u0511\5\60\31\2\u0511\u0512\7>\2\2\u0512\u0514\3\2\2\2\u0513\u0503\3"+
		"\2\2\2\u0513\u050f\3\2\2\2\u0514\u00eb\3\2\2\2\u0515\u0519\5\34\17\2\u0516"+
		"\u0519\7\u0083\2\2\u0517\u0519\5\u00eex\2\u0518\u0515\3\2\2\2\u0518\u0516"+
		"\3\2\2\2\u0518\u0517\3\2\2\2\u0519\u00ed\3\2\2\2\u051a\u051d\5\u00f0y"+
		"\2\u051b\u051d\5\u00f2z\2\u051c\u051a\3\2\2\2\u051c\u051b\3\2\2\2\u051d"+
		"\u00ef\3\2\2\2\u051e\u051f\7\31\2\2\u051f\u0520\7\32\2\2\u0520\u0521\7"+
		"$\2\2\u0521\u0522\7\33\2\2\u0522\u00f1\3\2\2\2\u0523\u0524\7\31\2\2\u0524"+
		"\u052d\7\32\2\2\u0525\u052a\5\u00e8u\2\u0526\u0527\7\26\2\2\u0527\u0529"+
		"\5\u00e8u\2\u0528\u0526\3\2\2\2\u0529\u052c\3\2\2\2\u052a\u0528\3\2\2"+
		"\2\u052a\u052b\3\2\2\2\u052b\u052e\3\2\2\2\u052c\u052a\3\2\2\2\u052d\u0525"+
		"\3\2\2\2\u052d\u052e\3\2\2\2\u052e\u052f\3\2\2\2\u052f\u0530\7\33\2\2"+
		"\u0530\u0531\7H\2\2\u0531\u0532\5\u00e8u\2\u0532\u00f3\3\2\2\2\u0533\u0535"+
		"\5\u00ecw\2\u0534\u0536\7\u0082\2\2\u0535\u0534\3\2\2\2\u0535\u0536\3"+
		"\2\2\2\u0536\u00f5\3\2\2\2\u0537\u053a\5\62\32\2\u0538\u053a\7\u008a\2"+
		"\2\u0539\u0537\3\2\2\2\u0539\u0538\3\2\2\2\u053a\u053b\3\2\2\2\u053b\u053c"+
		"\t\b\2\2\u053c\u053d\5\62\32\2\u053d\u00f7\3\2\2\2\u053e\u0540\7\67\2"+
		"\2\u053f\u0541\5\60\31\2\u0540\u053f\3\2\2\2\u0540\u0541\3\2\2\2\u0541"+
		"\u0542\3\2\2\2\u0542\u0543\78\2\2\u0543\u00f9\3\2\2\2\u0544\u0545\5\u00fc"+
		"\177\2\u0545\u00fb\3\2\2\2\u0546\u0547\7\u0081\2\2\u0547\u00fd\3\2\2\2"+
		"\u0548\u0549\t\t\2\2\u0549\u00ff\3\2\2\2\u0088\u0108\u010c\u011c\u0122"+
		"\u012a\u0133\u013f\u0155\u015d\u0162\u0165\u0169\u0172\u017b\u017e\u0186"+
		"\u018d\u018f\u0196\u019d\u019f\u01a7\u01ac\u01b3\u01ba\u01c4\u01cb\u01d2"+
		"\u01d9\u01e2\u01ec\u01f0\u01f8\u01fa\u0206\u020c\u0210\u0214\u021f\u0225"+
		"\u0234\u023a\u023e\u0242\u0249\u0250\u0256\u025b\u025d\u0261\u0268\u026f"+
		"\u0278\u0284\u028e\u029a\u029e\u02a7\u02ae\u02c4\u02c9\u02ce\u02d2\u02de"+
		"\u02e6\u02ea\u02f1\u02f8\u02fe\u0305\u030d\u0314\u031a\u0320\u0326\u032c"+
		"\u0337\u033d\u0342\u034a\u035f\u0368\u036a\u0381\u0392\u039d\u03b3\u03b7"+
		"\u03be\u03c2\u03cc\u03d1\u03df\u03e8\u03ee\u0409\u041a\u041c\u0425\u042d"+
		"\u043c\u045a\u0462\u0464\u047a\u0484\u048e\u0493\u0498\u049c\u04a8\u04ac"+
		"\u04b5\u04bc\u04ca\u04ce\u04d3\u04db\u04df\u04e8\u04ec\u04f0\u04ff\u0501"+
		"\u0509\u050c\u0513\u0518\u051c\u052a\u052d\u0535\u0539\u0540";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}