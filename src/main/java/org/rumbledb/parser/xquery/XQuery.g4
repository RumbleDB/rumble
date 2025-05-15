grammar XQuery;

@header {
// Java header
package org.rumbledb.parser.xquery;
}

module                  : (Kxquery Kversion vers=stringLiteral ';')?
                          (libraryModule | main=mainModule);

mainModule              : prolog program;

prolog                  : ((setter | namespaceDecl | moduleImport) ';')*
                          (annotatedDecl ';')*;

///////////////////////// Scripting addition - begin

program                 : statementsAndOptionalExpr ;

///////////////////////// Statements

statements                  : statement* ;

statementsAndExpr           : statements expr ;

statementsAndOptionalExpr   : statements expr? ;

statement                   : applyStatement
                            | assignStatement
                            | blockStatement
                            | breakStatement
                            | continueStatement
                            | exitStatement
                            | flowrStatement
                            | ifStatement
                            | switchStatement
                            | tryCatchStatement
                            | typeSwitchStatement
                            | varDeclStatement
                            | whileStatement
                            ;

applyStatement              : exprSimple ';' ;

assignStatement             : '$' qname ':=' exprSingle ';' ;

blockStatement              : LBRACE statements RBRACE ;

breakStatement              : Kbreak Kloop ';' ;

continueStatement           : Kcontinue Kloop ';' ;

exitStatement               : Kexit Kreturning exprSingle ';' ;

flowrStatement              : (start_for=forClause| start_let=letClause)
                              (forClause | letClause | whereClause | groupByClause | orderByClause | countClause)*
                              Kreturn returnStmt=statement ;

ifStatement:                Kif '(' test_expr=expr ')'
                            Kthen branch=statement
                            Kelse else_branch=statement ;

switchStatement             : Kswitch '(' condExpr=expr ')' cases+=switchCaseStatement+ Kdefault Kreturn def=statement ;

switchCaseStatement         : (Kcase cond+=exprSingle)+ Kreturn ret=statement ;

tryCatchStatement           : Ktry try_block=blockStatement catches+=catchCaseStatement+ ;

catchCaseStatement          : Kcatch (jokers+='*' | errors+=qname) ('|' (jokers+='*' | errors+=qname))* catch_block=blockStatement;

typeSwitchStatement         : Ktypeswitch '(' cond=expr ')' cases+=caseStatement+ Kdefault (var_ref=varRef)? Kreturn def=statement ;

caseStatement               : Kcase (var_ref=varRef Kas)? union+=sequenceType ('|' union+=sequenceType)* Kreturn ret=statement ;

annotation                  : ('%' name=qname ('(' Literal (',' Literal)* ')')? | updating=Kupdating);

annotations                 : annotation* ;

varDeclStatement            : annotations Kvariable varDeclForStatement (',' varDeclForStatement)* ';' ;

varDeclForStatement         : var_ref=varRef (Kas sequenceType)? (':=' expr_vals+=exprSingle)? ;

whileStatement              : Kwhile '(' test_expr=expr ')' stmt=statement ;

///////////////////////// Scripting addition - end

namespaceDecl           : Kdeclare 'namespace' NCName '=' uriLiteral;

annotatedDecl           : functionDecl
                        | varDecl
                        | typeDecl
                        | contextItemDecl;


qname                   : ((ns=NCName | nskw=keyWords)':')?
                          (local_name=NCName | local_namekw = keyWords);

moduleImport            : 'import' 'module' ('namespace' prefix=NCName '=')? targetNamespace=uriLiteral (Kat uriLiteral (',' uriLiteral)*)?;

// TODO: Assignable variable decl
varDecl                 : Kdeclare annotations Kvariable varRef (Kas sequenceType)? ((':=' exprSingle) | (external='external' (':=' exprSingle)?));

contextItemDecl         : Kdeclare Kcontext Kitem (Kas sequenceType)? ((':=' exprSingle) | (external='external' (':=' exprSingle)?));

functionDecl            : Kdeclare annotations 'function' fn_name=qname '(' paramList? ')'
                          (Kas return_type=sequenceType)?
                          (LBRACE (fn_body=statementsAndOptionalExpr) RBRACE | is_external='external');

typeDecl                : Kdeclare Ktype type_name=qname 'as' (schema=schemaLanguage)? type_definition=exprSingle;

schemaLanguage          : 'jsound' 'compact'
                        | 'jsound' 'verbose'
                        | 'json' 'schema';

paramList               : param (',' param)*;

param                   : '$' qname (Kas sequenceType)?;
///////////////////////// constructs, expression

exprSingle              : exprSimple
                        | flowrExpr
                        | switchExpr
                        | typeSwitchExpr
                        | ifExpr
                        | tryCatchExpr
                        ;

exprSimple              : quantifiedExpr
                        | orExpr
                        | insertExpr
                        | deleteExpr
                        | renameExpr
                        | replaceExpr
                        | transformExpr
                        | appendExpr
                        ;

flowrExpr               : (start_for=forClause| start_let=letClause)
                          (forClause | letClause | whereClause | groupByClause | orderByClause | countClause)*
                          Kreturn return_expr=exprSingle;

letClause               : Klet vars+=letVar (',' vars+=letVar)*;

letVar                  : var_ref=varRef (Kas seq=sequenceType)? ':=' ex=exprSingle ;

groupByClause           : Kgroup Kby vars+=groupByVar (',' vars+=groupByVar)*;

groupByVar              : var_ref=varRef
                          ((Kas seq=sequenceType)? decl=':=' ex=exprSingle)?
                          (Kcollation uri=uriLiteral)?;

quantifiedExpr          : (so=Ksome | ev=Kevery)
                          vars+=quantifiedExprVar (',' vars+=quantifiedExprVar)*
                          Ksatisfies exprSingle;

quantifiedExprVar       : varRef (Kas sequenceType)? Kin exprSingle;

tryCatchExpr            : Ktry LBRACE try_expression=expr RBRACE catches+=catchClause+;

catchClause             : Kcatch (jokers+='*' | errors+=qname) ('|' (jokers+='*' | errors+=qname))* LBRACE catch_expression=expr RBRACE;

///////////////////////// expression


multiplicativeExpr      : main_expr=instanceOfExpr ( op+=('*' | 'div' | 'idiv' | 'mod') rhs+=instanceOfExpr )*;

instanceOfExpr          : main_expr=isStaticallyExpr ( Kinstance Kof seq=sequenceType)?;

isStaticallyExpr        : main_expr=treatExpr ( Kis Kstatically seq=sequenceType)?;

valueExpr               : simpleMap_expr=simpleMapExpr
                        | validate_expr=validateExpr
                        | annotate_expr=annotateExpr;

annotateExpr            : Kannotate Ktype sequenceType LBRACE expr RBRACE;

// stringLiteral and varRef will be in XQuery 4.0
keySpecifier : ( in=Literal| lt=stringLiteral | nc=NCName | pe=parenthesizedExpr | vr=varRef | wc='*');

primaryExpr             : Literal
                        | stringLiteral
                        | varRef
                        | parenthesizedExpr
                        | contextItemExpr
                        | objectConstructor
                        | functionCall
                        | orderedExpr
                        | unorderedExpr
                        | nodeConstructor
                        | arrayConstructor
                        | functionItemExpr
                        | blockExpr
                        | unaryLookup
                        ;

blockExpr : LBRACE statementsAndExpr RBRACE ;

inlineFunctionExpr      : annotations 'function' '(' paramList? ')'
                           (Kas return_type=sequenceType)?
                           (LBRACE (fn_body=statementsAndOptionalExpr) RBRACE);

///////////////////////// Updating Expressions

insertExpr              : Kinsert Kjson to_insert_expr=exprSingle Kinto main_expr=exprSingle (Kat Kposition pos_expr=exprSingle)?
                        | Kinsert Kjson pairConstructor ( ',' pairConstructor )* Kinto main_expr=exprSingle;

deleteExpr              : Kdelete Kjson updateLocator;

renameExpr              : Krename Kjson updateLocator Kas name_expr=exprSingle;

replaceExpr             : Kreplace Kvalue Kof Kjson updateLocator Kwith replacer_expr=exprSingle;

transformExpr           : Kcopy copyDecl ( ',' copyDecl )* Kmodify mod_expr=exprSingle Kreturn ret_expr=exprSingle;

appendExpr              : Kappend Kjson to_append_expr=exprSingle Kinto array_expr=exprSingle;

updateLocator           : main_expr=primaryExpr ( lookup )+; // TODO CHECK THIS,

copyDecl                    : var_ref=varRef ':=' src_expr=exprSingle;

///////////////////////// Types

objectConstructor       : 'map' LBRACE ( pairConstructor (',' pairConstructor)* )? RBRACE
                        | merge_operator+='{|' expr '|}';

singleType              : item=itemType (question +='?')?;

pairConstructor         :  ( lhs=exprSingle ) (':' | '?') rhs=exprSingle;

keyWords                : Kxquery
                        | Kand
                        | Kcast
                        | Kcastable
                        | Kcollation
                        | Kcontext
                        | Kdeclare
                        | Kdefault
                        | Kelse
                        | Kgreatest
                        | Kinstance
                        | Kstatically
                        | Kis
                        | Kitem
                        | Kleast
                        | Kof
                        | Kor
                        | Kthen
                        | Kto
                        | Ktreat
                        | Ktypeswitch
                        | Kversion
                        | Kswitch
                        | Kcase
                        | Ktry
                        | Kcatch
                        | Ksome
                        | Kevery
                        | Ksatisfies
                        | Kstable
                        | Kvariable
                        | Kascending
                        | Kdescending
                        | Kempty
                        | Kallowing
                        | Kas
                        | Kat
                        | Kin
                        | Kif
                        | Kfor
                        | Klet
                        | Kwhere
                        | Kgroup
                        | Kby
                        | Korder
                        | Kcount
                        | Kreturn
                        | Kunordered
                        | Ktype
                        | Kinsert
                        | Kdelete
                        | Krename
                        | Kreplace
                        | Kappend
                        | Kcopy
                        | Kmodify
                        | Kinto
                        | Kvalue
                        | Kwith
                        | Kposition
                        | Kvalidate
                        | Kannotate
                        | Kbreak
                        | Kloop
                        | Kcontinue
                        | Kexit
                        | Kreturning
                        | Kwhile
                        | Kjson
                        | Ktext
                        | Kupdating
                        ;

///////////////////////// literals

Kfor                    : 'for';

Klet                    : 'let';

Kwhere                  : 'where';

Kgroup                  : 'group';

Kby                     : 'by';

Korder                  : 'order';

Kreturn                 : 'return';

Kif                     : 'if';

Kin                     : 'in';

Kas                     : 'as';

Kat                     : 'at';

Kallowing               : 'allowing';

Kempty                  : 'empty';

Kcount                  : 'count';

Kstable                 : 'stable';

Kascending              : 'ascending';

Kdescending             : 'descending';

Ksome                   : 'some';

Kevery                  : 'every';

Ksatisfies              : 'satisfies';

Kcollation              : 'collation';

Kgreatest               : 'greatest';

Kleast                  : 'least';

Kswitch                 : 'switch';

Kcase                   : 'case';

Ktry                    : 'try';

Kcatch                  : 'catch';

Kdefault                : 'default';

Kthen                   : 'then';

Kelse                   : 'else';

Ktypeswitch             : 'typeswitch';

Kor                     : 'or';

Kand                    : 'and';

Kto                     : 'to' ;

Kinstance               : 'instance' ;

Kof                     : 'of' ;

Kstatically             : 'statically' ;

Kis                     : 'is' ;

Ktreat                  : 'treat';

Kcast                   : 'cast';

Kcastable               : 'castable';

Kversion                : 'version';

Kxquery                 : 'xquery';

Kunordered              : 'unordered';

Ktype                   : 'type';

Kvalidate               : 'validate';

Kannotate               : 'annotate';

Kdeclare                : 'declare';

Kcontext                : 'context';

Kitem                   : 'item';

Kvariable               : 'variable';

Kinsert                 : 'insert';

Kdelete                 : 'delete';

Krename                 : 'rename';

Kreplace                : 'replace';

Kcopy                   : 'copy';

Kmodify                 : 'modify';

Kappend                 : 'append';

Kinto                   : 'into';

Kvalue                  : 'value';

Kwith                   : 'with';

Kposition               : 'position';

Kjson                   : 'json';

Kupdating               :  'updating';


///////////////////////// XPath
Kimport                 : 'import';
Kschema                 : 'schema';
Knamespace              : 'namespace';
Kelement                : 'element';
Kslash                  : '/';
Kdslash                 : '//';
Kat_symbol              : '@';
Kchild                  : 'child';
Kdescendant             : 'descendant';
Kattribute              : 'attribute';
Kself                   : 'self';
Kdescendant_or_self     : 'descendant-or-self';
Kfollowing_sibling      : 'following-sibling';
Kfollowing              : 'following';
Kparent                 : 'parent';
Kancestor               : 'ancestor';
Kpreceding_sibling      : 'preceding-sibling';
Kpreceding              : 'preceding';
Kancestor_or_self       : 'ancestor-or-self';
Knode                   : 'node';
Kbinary                 : 'binary';
Kdocument               : 'document';
Kdocument_node          : 'document-node';
Ktext                   : 'text';
Kpi                     : 'processing-instruction';
Knamespace_node         : 'namespace-node';
Kschema_attribute       : 'schema-attribute';
Kschema_element         : 'schema-element';
Karray_node             : 'array-node';
Kboolean_node           : 'boolean-node';
Knull_node              : 'null-node';
Knumber_node            : 'number-node';
Kobject_node            : 'object-node';
Kcomment                : 'comment';

///////////////////////// Scripting keywords
Kbreak                  : 'break' ;
Kloop                   : 'loop' ;
Kcontinue               : 'continue' ;
Kexit                   : 'exit' ;
Kreturning              : 'returning' ;
Kwhile                  : 'while' ;

STRING                  : '"' (ESC | ~ ["\\])* '"' | '\'' (ESCapos | ~ ['\\])* '\'';

fragment ESC            : '\\' (["\\/bfnrt] | UNICODE);
fragment ESCapos            : '\\' (['\\/bfnrt] | UNICODE);

fragment UNICODE        : 'u' HEX HEX HEX HEX;

fragment HEX            : [0-9a-fA-F];

ArgumentPlaceholder     : '?';

// symbols

LBRACE: '{';
RBRACE: '}';

// literals

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-Literal */
Literal                 : NumericLiteral;

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-NumericLiteral */
NumericLiteral          : IntegerLiteral | DecimalLiteral | DoubleLiteral;

/// terminal symbols

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-IntegerLiteral */
IntegerLiteral          : Digits ;

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-DecimalLiteral */
DecimalLiteral          : ('.' Digits ) | ( Digits '.' [0-9]* );

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-DoubleLiteral */
DoubleLiteral           : (( '.' Digits ) | ( Digits ('.' [0-9]*)?)) [eE] [+-]? Digits ;

WS                      : (' '|'\r'|'\t'|'\n') -> channel(HIDDEN);

/* source: https://www.w3.org/TR/REC-xml-names/#NT-NCName */
// TODO: this has been moved to a parser rule, as if it is defined as a lexer rule, it overrides other rules
// we know where this is used, so we can move it to a context-aware parser rule
NCName                  : NameStartChar (NameChar)*;


// problem. this matches too much!
ElementContentLexer     : Char+;

//Name                    : ;

fragment NameStartChar   : [_a-zA-Z]
                        | '\u00C0'..'\u00D6'
                        | '\u00D8'..'\u00F6'
                        | '\u00F8'..'\u02FF'
                        | '\u0370'..'\u037D'
                        | '\u037F'..'\u1FFF'
                        | '\u200C'..'\u200D'
                        | '\u2070'..'\u218F'
                        | '\u2C00'..'\u2FEF'
                        | '\u3001'..'\uD7FF'
                        | '\uF900'..'\uFDCF'
                        | '\uFDF0'..'\uFFFD'
                        ;

fragment NameChar       : NameStartChar
                        | '-'
                        | '.'
                        | [0-9]
                        | '\u00B7'
                        | '\u0300'..'\u036F'
                        | '\u203F'..'\u2040'
                        ;


/* source: https://www.w3.org/TR/REC-xml/#NT-Char */
fragment Char           :  '\u0009'
                        | '\u000A'
                        | '\u000D'
                        | '\u0020'..'\uD7FF'
                        | '\uE000'..'\uFFFD'
                        | '\uD800'..'\uDBFF' '\uDC00'..'\uDFFF';  // surrogate pairs for supplementary characters


XQComment               : '(' ':' (XQComment | '(' ~[:] | ':' ~[)] | ~[:(])* ':'+ ')' -> channel(HIDDEN);

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-Digits */
fragment Digits         : [0-9]+ ;