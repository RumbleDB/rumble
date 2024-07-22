grammar Jsoniq;

@header {
// Java header
package org.rumbledb.parser;
}

moduleAndThisIsIt       : module EOF;

module                  : (Kjsoniq Kversion vers=stringLiteral ';')?
                          (libraryModule | main=mainModule);

mainModule              : prolog program;

libraryModule           : 'module' 'namespace' NCName '=' uriLiteral ';' prolog;

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

blockStatement              : '{' statements '}' ;

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

annotation                  : '%' name=qname ('(' Literal (',' Literal)* ')')? ;

annotations                 : annotation* ;

varDeclStatement            : annotations Kvariable varDeclForStatement (',' varDeclForStatement)* ';' ;

varDeclForStatement         : var_ref=varRef (Kas sequenceType)? (':=' expr_vals+=exprSingle)? ;

whileStatement              : Kwhile '(' test_expr=expr ')' stmt=statement ;

///////////////////////// Scripting addition - end

setter                  : defaultCollationDecl
                        | orderingModeDecl
                        | emptyOrderDecl
                        | decimalFormatDecl;

namespaceDecl           : Kdeclare 'namespace' NCName '=' uriLiteral;

annotatedDecl           : functionDecl
                        | varDecl
                        | typeDecl
                        | contextItemDecl;

defaultCollationDecl    : Kdeclare Kdefault Kcollation uriLiteral;

orderingModeDecl        : Kdeclare 'ordering' ('ordered' | 'unordered');

emptyOrderDecl          : Kdeclare Kdefault 'order' Kempty (emptySequenceOrder=(Kgreatest | Kleast));

decimalFormatDecl       : Kdeclare
                          (('decimal-format' qname) | (Kdefault 'decimal-format'))
                          (dfPropertyName '=' stringLiteral)*;

qname                   : ((ns=NCName | nskw=keyWords)':')?
                          (local_name=NCName | local_namekw = keyWords);

dfPropertyName          : 'decimal-separator'
                        | 'grouping-separator'
                        | 'infinity'
                        | 'minus-sign'
                        | 'NaN'
                        | 'percent'
                        | 'per-mille'
                        | 'zero-digit'
                        | 'digit'
                        | 'pattern-separator';

moduleImport            : 'import' 'module' ('namespace' prefix=NCName '=')? targetNamespace=uriLiteral (Kat uriLiteral (',' uriLiteral)*)?;

// TODO: Assignable variable decl
varDecl                 : Kdeclare annotations Kvariable varRef (Kas sequenceType)? ((':=' exprSingle) | (external='external' (':=' exprSingle)?));

contextItemDecl         : Kdeclare Kcontext Kitem (Kas sequenceType)? ((':=' exprSingle) | (external='external' (':=' exprSingle)?));

functionDecl            : Kdeclare annotations 'function' fn_name=qname '(' paramList? ')'
                          (Kas return_type=sequenceType)?
                          ('{' (fn_body=statementsAndOptionalExpr) '}' | 'external');

typeDecl                : Kdeclare Ktype type_name=qname 'as' (schema=schemaLanguage)? type_definition=exprSingle;

schemaLanguage          : 'jsound' 'compact'
                        | 'jsound' 'verbose'
                        | 'json' 'schema';

paramList               : param (',' param)*;

param                   : '$' qname (Kas sequenceType)?;
///////////////////////// constructs, expression

expr                    : exprSingle (',' exprSingle)*;     // expr -> CommaExpression in visitor

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

forClause               : Kfor vars+=forVar (',' vars+=forVar)*;

forVar                  : var_ref=varRef
                          (Kas seq=sequenceType)?
                          (flag=Kallowing Kempty)?
                          (Kat at=varRef)?
                          Kin ex=exprSingle;

letClause               : Klet vars+=letVar (',' vars+=letVar)*;

letVar                  : var_ref=varRef (Kas seq=sequenceType)? ':=' ex=exprSingle ;

whereClause             : Kwhere exprSingle;

groupByClause           : Kgroup Kby vars+=groupByVar (',' vars+=groupByVar)*;

groupByVar              : var_ref=varRef
                          ((Kas seq=sequenceType)? decl=':=' ex=exprSingle)?
                          (Kcollation uri=uriLiteral)?;

orderByClause           : ((Korder Kby) | (stb=Kstable Korder Kby)) orderByExpr (',' orderByExpr)*;

orderByExpr             : ex=exprSingle
                          (Kascending | desc=Kdescending)?
                          (Kempty (gr=Kgreatest | ls=Kleast))?
                          (Kcollation uril=uriLiteral)?;

countClause             : Kcount varRef;

quantifiedExpr          : (so=Ksome | ev=Kevery)
                          vars+=quantifiedExprVar (',' vars+=quantifiedExprVar)*
                          Ksatisfies exprSingle;

quantifiedExprVar       : varRef (Kas sequenceType)? Kin exprSingle;

switchExpr              : Kswitch '(' cond=expr ')' cases+=switchCaseClause+ Kdefault Kreturn def=exprSingle;

switchCaseClause        : (Kcase cond+=exprSingle)+ Kreturn ret=exprSingle;

typeSwitchExpr          : Ktypeswitch '(' cond=expr ')' cses+=caseClause+ Kdefault (var_ref=varRef)? Kreturn def=exprSingle;

caseClause              : Kcase (var_ref=varRef Kas)? union+=sequenceType ('|' union+=sequenceType)* Kreturn ret=exprSingle;

ifExpr                  : Kif '(' test_condition=expr ')'
                          Kthen branch=exprSingle
                          Kelse else_branch=exprSingle;

tryCatchExpr            : Ktry '{' try_expression=expr '}' catches+=catchClause+;

catchClause             : Kcatch (jokers+='*' | errors+=qname) ('|' (jokers+='*' | errors+=qname))* '{' catch_expression=expr '}';

///////////////////////// expression

orExpr                  : main_expr=andExpr ( Kor rhs+=andExpr )*;

andExpr                 : main_expr=notExpr ( Kand rhs+=notExpr )*;

notExpr                 : op+=Knot ? main_expr=comparisonExpr;

comparisonExpr          : main_expr=stringConcatExpr
                          ( op+=('eq' | 'ne' | 'lt' | 'le' | 'gt' | 'ge'
                          | '=' | '!=' | '<' | '<=' | '>' | '>=') rhs+=stringConcatExpr )?;

stringConcatExpr        : main_expr=rangeExpr ( '||' rhs+=rangeExpr )* ;

rangeExpr               : main_expr=additiveExpr ( Kto rhs+=additiveExpr )?;

additiveExpr            : main_expr=multiplicativeExpr ( op+=('+' | '-') rhs+=multiplicativeExpr )*;

multiplicativeExpr      : main_expr=instanceOfExpr ( op+=('*' | 'div' | 'idiv' | 'mod') rhs+=instanceOfExpr )*;

instanceOfExpr          : main_expr=isStaticallyExpr ( Kinstance Kof seq=sequenceType)?;

isStaticallyExpr        : main_expr=treatExpr ( Kis Kstatically seq=sequenceType)?;

treatExpr               : main_expr=castableExpr ( Ktreat Kas seq=sequenceType )?;

castableExpr            : main_expr=castExpr ( Kcastable Kas single=singleType )?;

castExpr                : main_expr=arrowExpr ( Kcast Kas single=singleType )?;

arrowExpr               : main_expr=unaryExpr (('=' '>') function+=arrowFunctionSpecifier arguments+=argumentList)*;

arrowFunctionSpecifier  : qname | varRef | parenthesizedExpr;

unaryExpr               : op+=('-' | '+')* main_expr=valueExpr;

valueExpr               : simpleMap_expr=simpleMapExpr
                        | validate_expr=validateExpr
                        | annotate_expr=annotateExpr;

validateExpr            : Kvalidate Ktype sequenceType '{' expr '}';

annotateExpr            : Kannotate Ktype sequenceType '{' expr '}';

simpleMapExpr           : main_expr=postFixExpr ('!' map_expr+=postFixExpr)*;

postFixExpr             : main_expr=primaryExpr (arrayLookup | predicate | objectLookup | arrayUnboxing | argumentList)*;

arrayLookup             : '[' '[' expr ']' ']';

arrayUnboxing           : '[' ']';

predicate               : '[' expr ']';

objectLookup            : '.' ( kw=keyWords | lt=stringLiteral | nc=NCName | pe=parenthesizedExpr | vr=varRef | ci=contextItemExpr);

primaryExpr             : NullLiteral
                        | Ktrue
                        | Kfalse
                        | Literal
                        | stringLiteral
                        | varRef
                        | parenthesizedExpr
                        | contextItemExpr
                        | objectConstructor
                        | functionCall
                        | orderedExpr
                        | unorderedExpr
                        | arrayConstructor
                        | functionItemExpr
                        | blockExpr
                        ;

blockExpr : '{' statementsAndExpr '}' ;


varRef                  : '$' var_name=qname;

parenthesizedExpr       : '(' expr? ')';

contextItemExpr         : '$$';

orderedExpr             : 'ordered' '{' expr '}';

unorderedExpr           : 'unordered' '{' expr '}';

functionCall            : fn_name=qname argumentList;

argumentList            : '('  (args+=argument ','?)* ')';

argument                : exprSingle | ArgumentPlaceholder;

functionItemExpr        : namedFunctionRef | inlineFunctionExpr;

namedFunctionRef        : fn_name=qname '#' arity=Literal;

inlineFunctionExpr      : annotations 'function' '(' paramList? ')'
                           (Kas return_type=sequenceType)?
                           ('{' (fn_body=statementsAndOptionalExpr) '}');

///////////////////////// Updating Expressions

insertExpr              : Kinsert Kjson to_insert_expr=exprSingle Kinto main_expr=exprSingle (Kat Kposition pos_expr=exprSingle)?
                        | Kinsert Kjson pairConstructor ( ',' pairConstructor )* Kinto main_expr=exprSingle;

deleteExpr              : Kdelete Kjson updateLocator;

renameExpr              : Krename Kjson updateLocator Kas name_expr=exprSingle;

replaceExpr             : Kreplace Kjson Kvalue Kof updateLocator Kwith replacer_expr=exprSingle;

transformExpr           : Kcopy Kjson copyDecl ( ',' copyDecl )* Kmodify mod_expr=exprSingle Kreturn ret_expr=exprSingle;

appendExpr              : Kappend Kjson to_append_expr=exprSingle Kinto array_expr=exprSingle;

updateLocator           : main_expr=primaryExpr ( arrayLookup | objectLookup )+;

copyDecl                    : var_ref=varRef ':=' src_expr=exprSingle;

// TODO: Direct element constructors


///////////////////////// XPath and XQuery

// Mostly taken from http://www.w3.org/TR/xquery/#id-grammar, with some
// simplifications:
//
// 1. The parser itself doesn't really enforce ws:explicit except for some easy
//    cases (QNames and wildcards).  Walkers will need to do this (and also parse
//    wildcards a bit).
//
// 2. When collecting element content, we will need to check the HIDDEN
//    channel as well, for whitespace and XQuery comments (these should be
//    treated as regular text inside elements).

// MODULE HEADER ///////////////////////////////////////////////////////////////

// Replace according to https://www.w3.org/TR/xquery-31/#id-query-prolog, we only have single mainModule

schemaImport: Kimport Kschema
              schemaPrefix?
              nsURI=uriLiteral
              (Kat locations+=uriLiteral (',' locations+=uriLiteral)*)? ;

schemaPrefix: (Knamespace NCName '=' | Kdefault Kelement Knamespace) ;

// PATHS ///////////////////////////////////////////////////////////////////////

pathExpr: (Kslash singleslash=relativePathExpr?) | (Kdslash doubleslash=relativePathExpr) | relative=relativePathExpr ;

relativePathExpr: stepExpr (sep+=(Kslash|Kdslash) stepExpr)* ;

stepExpr: postFixExpr | axisStep ;

axisStep: (reverseStep | forwardStep) predicateList ;

forwardStep: (forwardAxis nodeTest) | abbrevForwardStep ;

forwardAxis: ( Kchild
             | Kdescendant
             | Kattribute
             | Kself
             | Kdescendant_or_self
             | Kfollowing_sibling
             | Kfollowing ) ':' ':' ;

abbrevForwardStep: Kat_symbol ? nodeTest ;

reverseStep: (reverseAxis nodeTest) | abbrevReverseStep ;

reverseAxis: ( Kparent
             | Kancestor
             | Kpreceding_sibling
             | Kpreceding
             | Kancestor_or_self ) ':' ':';

abbrevReverseStep: '..' ;

nodeTest: nameTest | kindTest ;

nameTest: qname wildcard ;

wildcard: '*'            # allNames
        | nCNameWithLocalWildcard  # allWithNS    // walkers must strip out the trailing :*
        | nCNameWithPrefixWildcard # allWithLocal // walkers must strip out the leading *:
        ;
nCNameWithLocalWildcard :  NCName ':' '*' ;
nCNameWithPrefixWildcard: '*' ':' NCName ;


predicateList: predicate*;

itemType: qname
        | NullLiteral
        | kindTest
        | ('item' '(' ')')
        | functionTest
        | mapTest
        | arrayTest
        | atomicOrUnionType
        | parenthesizedItemTest ;

atomicOrUnionType: qname ;

kindTest: documentTest
        | elementTest
        | attributeTest
        | schemaElementTest
        | schemaAttributeTest
        | piTest
        | commentTest
        | textTest
        | namespaceNodeTest
        | mlNodeTest
        | binaryNodeTest
        | anyKindTest
        ;

anyKindTest: Knode '(' '*'? ')' ;

binaryNodeTest: Kbinary '(' ')' ;

documentTest: Kdocument_node '(' (elementTest | schemaElementTest)? ')' ;

textTest: Ktext '(' ')' ;

commentTest: Kcomment '(' ')' ;

namespaceNodeTest: Knamespace_node '(' ')' ;

piTest: Kpi '(' (NCName | stringLiteral)? ')' ;

attributeTest: Kattribute '(' (attributeNameOrWildcard (',' type=typeName)?)? ')' ;

attributeNameOrWildcard: attributeName | '*' ;

schemaAttributeTest: Kschema_attribute '(' attributeDeclaration ')' ;

elementTest: Kelement '(' (elementNameOrWildcard (',' type=typeName optional='?'?)?)? ')' ;

elementNameOrWildcard: elementName | '*' ;

schemaElementTest: Kschema_element '(' elementDeclaration ')' ;

elementDeclaration: elementName ;

attributeName: qname ;

elementName: qname ;

simpleTypeName: typeName ;

typeName: qname;

mapTest: anyMapTest | typedMapTest ;

anyMapTest: Kmap '(' '*' ')' ;

typedMapTest: Kmap '(' qname ',' sequenceType ')' ;

arrayTest: anyArrayTest | typedArrayTest ;

anyArrayTest: Karray '(' '*' ')' ;

typedArrayTest: Karray '(' sequenceType ')' ;

parenthesizedItemTest: '(' itemType ')' ;

attributeDeclaration: attributeName ;

mlNodeTest: mlArrayNodeTest
          | mlObjectNodeTest
          | mlNumberNodeTest
          | mlBooleanNodeTest
          | mlNullNodeTest
          ;

mlArrayNodeTest: Karray_node '(' stringLiteral? ')' ;

mlObjectNodeTest: Kobject_node '(' stringLiteral? ')' ;

mlNumberNodeTest: Knumber_node '(' stringLiteral? ')' ;

mlBooleanNodeTest: Kboolean_node '(' stringLiteral? ')' ;

mlNullNodeTest: Knull_node '(' stringLiteral? ')' ;

///////////////////////// Types

sequenceType            : '(' ')'
                        | item=itemType (question+='?' | star+='*' | plus+='+')?;

objectConstructor       : '{' ( pairConstructor (',' pairConstructor)* )? '}'
                        | merge_operator+='{|' expr '|}';

functionTest	        : (anyFunctionTest | typedFunctionTest);

anyFunctionTest         : 'function' '(' '*' ')';

typedFunctionTest	    : 'function' '(' (st+=sequenceType (',' st+=sequenceType)*)? ')' 'as' rt=sequenceType;

singleType              : item=itemType (question +='?')?;

pairConstructor         :  ( lhs=exprSingle | name=NCName ) (':' | '?') rhs=exprSingle;

arrayConstructor        :  '[' expr? ']';

uriLiteral              : stringLiteral;

stringLiteral           : STRING;

keyWords                : Kjsoniq
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
                        | Knot
                        | NullLiteral
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
                        | Ktrue
                        | Kfalse
                        | Ktype
                        | Kinsert
                        | Kdelete
                        | Krename
                        | Kreplace
                        | Kappend
                        | Kcopy
                        | Kmodify
                        | Kjson
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

Knot                    : 'not' ;

Kto                     : 'to' ;

Kinstance               : 'instance' ;

Kof                     : 'of' ;

Kstatically             : 'statically' ;

Kis                     : 'is' ;

Ktreat                  : 'treat';

Kcast                   : 'cast';

Kcastable               : 'castable';

Kversion                : 'version';

Kjsoniq                 : 'jsoniq';

Kunordered              : 'unordered';

Ktrue                   : 'true';

Kfalse                  : 'false';

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

Kjson                   : 'json';

Kwith                   : 'with';

Kposition               : 'position';

///////////////////////// Scripting keywords
Kbreak                  : 'break' ;
Kloop                   : 'loop' ;
Kcontinue               : 'continue' ;
Kexit                   : 'exit' ;
Kreturning              : 'returning' ;
Kwhile                  : 'while' ;

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
Karray                  : 'array';
Kmap                    : 'map';

STRING                  : '"' (ESC | ~ ["\\])* '"';

fragment ESC            : '\\' (["\\/bfnrt] | UNICODE);

fragment UNICODE        : 'u' HEX HEX HEX HEX;

fragment HEX            : [0-9a-fA-F];

ArgumentPlaceholder     : '?';

NullLiteral             : 'null';

Literal                 : NumericLiteral;

NumericLiteral          : IntegerLiteral | DecimalLiteral | DoubleLiteral;

IntegerLiteral          : Digits ;

DecimalLiteral          : '.' Digits | Digits '.' [0-9]* ;

DoubleLiteral           : ('.' Digits | Digits ('.' [0-9]*)?) [eE] [+-]? Digits ;

fragment Digits         : [0-9]+ ;

WS                      : (' '|'\r'|'\t'|'\n') -> channel(HIDDEN);

NCName                  : NameStartChar NameChar*;

fragment NameStartChar  : [_a-zA-Z]
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
                        // no . in JSONIQ names | '.'
                        | [0-9]
                        | '\u00B7'
                        | '\u0300'..'\u036F'
                        | '\u203F'..'\u2040'
                        ;

XQComment               : '(' ':' (XQComment | '(' ~[:] | ':' ~[)] | ~[:(])* ':'+ ')' -> channel(HIDDEN);

ContentChar             :  ~["'{}<&]  ;

