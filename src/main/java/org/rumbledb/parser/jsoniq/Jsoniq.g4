/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Matteo Agnoletto (EPMatt) and RumbleDB team.
 *
 * A parser grammar for XQuery 3.1 that includes the XQuery Scripting Extensions, and additional update features.
 * This file is based on the XQuery parser grammar from the xqdoc project:
 * https://github.com/xqdoc/xqdoc/blob/master/src/main/antlr4/org/xqdoc/XQueryParser.g4
 * 
 * See LICENSE-xqdoc.txt for the original license terms.
 * 
 */

grammar Jsoniq;

@header {
// Java header
package org.rumbledb.parser.jsoniq;
}

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

// this rule was added to match the JSONiq grammar
moduleAndThisIsIt       : module EOF;

module                  : (Kjsoniq Kversion vers=stringLiteral ';')?
                          (libraryModule | main=mainModule);

mainModule              : prolog program;

libraryModule           : 'module' 'namespace' NCName '=' uriLiteral ';' prolog;

// MODULE PROLOG ///////////////////////////////////////////////////////////////

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
                            | flworStatement
                            | ifStatement
                            | switchStatement
                            | tryCatchStatement
                            | typeSwitchStatement
                            | varDeclStatement
                            | whileStatement
                            ;

applyStatement              : exprSimple ';' ;

assignStatement             : DOLLAR qname COLON_EQ exprSingle ';' ;

blockStatement              : '{' statements '}' ;

breakStatement              : Kbreak Kloop ';' ;

continueStatement           : Kcontinue Kloop ';' ;

exitStatement               : Kexit Kreturning exprSingle ';' ;

flworStatement              : (start_for=forClause| start_let=letClause)
                              (forClause | letClause | whereClause | groupByClause | orderByClause | countClause)*
                              KW_RETURN returnStmt=statement ;

ifStatement:                Kif '(' test_expr=expr ')'
                            Kthen branch=statement
                            Kelse else_branch=statement ;

switchStatement             : Kswitch '(' condExpr=expr ')' cases+=switchCaseStatement+ Kdefault KW_RETURN def=statement ;

switchCaseStatement         : (Kcase cond+=exprSingle)+ KW_RETURN ret=statement ;

tryCatchStatement           : Ktry try_block=blockStatement catches+=catchCaseStatement+ ;

catchCaseStatement          : Kcatch (jokers+='*' | errors+=qname) ('|' (jokers+='*' | errors+=qname))* catch_block=blockStatement;

typeSwitchStatement         : Ktypeswitch '(' cond=expr ')' cases+=caseStatement+ Kdefault (var_ref=varRef)? KW_RETURN def=statement ;

caseStatement               : Kcase (var_ref=varRef KW_AS)? union+=sequenceType ('|' union+=sequenceType)* KW_RETURN ret=statement ;

annotation                  : ('%' name=qname ('(' Literal (COMMA Literal)* ')')? | updating=Kupdating);

annotations                 : annotation* ;

varDeclStatement            : annotations Kvariable varDeclForStatement (COMMA varDeclForStatement)* ';' ;

varDeclForStatement         : var_ref=varRef (KW_AS sequenceType)? (COLON_EQ expr_vals+=exprSingle)? ;

whileStatement              : Kwhile '(' test_expr=expr ')' stmt=statement ;

///////////////////////// Scripting addition - end

setter                  : defaultCollationDecl
                        | baseURIDecl
                        | orderingModeDecl
                        | emptyOrderDecl
                        | decimalFormatDecl;

namespaceDecl           : Kdeclare 'namespace' NCName '=' uriLiteral;

annotatedDecl           : functionDecl
                        | varDecl
                        | typeDecl
                        | contextItemDecl;

defaultCollationDecl    : Kdeclare Kdefault KW_COLLATION uriLiteral;

baseURIDecl            : Kdeclare 'base-uri' uriLiteral;

orderingModeDecl        : Kdeclare 'ordering' ('ordered' | 'unordered');

emptyOrderDecl          : Kdeclare Kdefault 'order' KW_EMPTY (emptySequenceOrder=(Kgreatest | Kleast));

decimalFormatDecl       : Kdeclare
                          (('decimal-format' qname) | (Kdefault 'decimal-format'))
                          (dfPropertyName '=' stringLiteral)*;

eqName: qname ;

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

moduleImport            : 'import' 'module' ('namespace' prefix=NCName '=')? targetNamespace=uriLiteral (KW_AT uriLiteral (COMMA uriLiteral)*)?;

// TODO: Assignable variable decl
varDecl                 : Kdeclare annotations Kvariable varRef (KW_AS sequenceType)? ((COLON_EQ exprSingle) | (external='external' (COLON_EQ exprSingle)?));

contextItemDecl         : Kdeclare Kcontext Kitem (KW_AS sequenceType)? ((COLON_EQ exprSingle) | (external='external' (COLON_EQ exprSingle)?));

functionDecl            : Kdeclare annotations 'function' fn_name=qname '(' paramList? ')'
                          (KW_AS return_type=sequenceType)?
                          ('{' (fn_body=statementsAndOptionalExpr) '}' | is_external='external');

typeDecl                : Kdeclare Ktype type_name=qname 'as' (schema=schemaLanguage)? type_definition=exprSingle;

schemaLanguage          : 'jsound' 'compact'
                        | 'jsound' 'verbose'
                        | 'json' 'schema';

paramList               : param (COMMA param)*;

param                   : DOLLAR qname (KW_AS sequenceType)?;

// EXPRESSIONS /////////////////////////////////////////////////////////////////

expr: exprSingle (COMMA exprSingle)* ;

exprSingle              : exprSimple
                        | flworExpr
                        | ifExpr
                        | switchExpr
                        | tryCatchExpr
                        | typeSwitchExpr
                        ;

exprSimple              : quantifiedExpr
                        | orExpr
                        | insertExpr
                        | deleteExpr
                        | renameExpr
                        | replaceExpr
                        | transformExpr
                        | appendExpr
                        | createCollectionExpr
                        | truncateCollectionExpr
                        | deleteIndexExpr
                        | deleteSearchExpr
                        | editCollectionExpr
                        | insertIndexExpr
                        | insertSearchExpr
                        ;

flworExpr: // replaced with the initialClause production to match the JSONiq grammar
           (start_for=forClause| start_let=letClause)
           // replaced with the intermediateClause production to match the JSONiq grammar
           (forClause | letClause | whereClause | groupByClause | orderByClause | countClause)*
           // replaced with the returnClause production to match the JSONiq grammar
           KW_RETURN return_expr=exprSingle ;

forClause: KW_FOR vars+=forVar (COMMA vars+=forVar)* ;

// renamed from forBinding to forVar to match the JSONiq grammar
forVar: var_ref=varRef
        // replaced with the typeDeclaration production to match the JSONiq grammar
        (KW_AS seq=sequenceType)?
        (flag=allowingEmpty)?
        // replaced with the positionalVar production to match the JSONiq grammar
        (KW_AT at=varRef)?
        KW_IN ex=exprSingle ;

allowingEmpty: KW_ALLOWING KW_EMPTY;

positionalVar: KW_AT DOLLAR pvar=varName ;

letClause: KW_LET vars+=letVar (COMMA vars+=letVar)* ;

// renamed from letBinding to letVar to match the JSONiq grammar
letVar: var_ref=varRef
        // replaced with the typeDeclaration production to match the JSONiq grammar
        (KW_AS seq=sequenceType)?
        COLON_EQ ex=exprSingle ;

windowClause: KW_FOR (tumblingWindowClause | slidingWindowClause) ;

tumblingWindowClause: KW_TUMBLING KW_WINDOW DOLLAR name=qname
                          type=typeDeclaration? KW_IN exprSingle
                          windowStartCondition windowEndCondition? ;

slidingWindowClause: KW_SLIDING KW_WINDOW DOLLAR name=qname
                          type=typeDeclaration? KW_IN exprSingle
                          windowStartCondition windowEndCondition ;

windowStartCondition: KW_START windowVars KW_WHEN exprSingle ;

windowEndCondition: KW_ONLY? KW_END windowVars KW_WHEN exprSingle ;

windowVars: (DOLLAR currentItem=eqName)? positionalVar?
                          (KW_PREVIOUS DOLLAR previousItem=eqName)?
                          (KW_NEXT DOLLAR nextItem=eqName)?;

countClause: KW_COUNT varRef ;

whereClause: KW_WHERE exprSingle ;

// replaced with the groupingSpecList production to match the JSONiq grammar
groupByClause: KW_GROUP KW_BY vars+=groupByVar (COMMA vars+=groupByVar)* ;

// renamed from groupingSpec to groupByVar to match the JSONiq grammar
groupByVar: var_ref=varRef
            // replaced with the typeDeclaration production to match the JSONiq grammar
            ((KW_AS seq=sequenceType)? decl=COLON_EQ ex=exprSingle)?
            (KW_COLLATION uri=uriLiteral)? ;

orderByClause: stb=KW_STABLE? KW_ORDER KW_BY specs+=orderByExpr (COMMA specs+=orderByExpr)* ;

orderByExpr             : ex=exprSingle
                          (Kascending | desc=Kdescending)?
                          (KW_EMPTY (gr=Kgreatest | ls=Kleast))?
                          (KW_COLLATION uril=uriLiteral)?;

quantifiedExpr          : (so=Ksome | ev=Kevery)
                          vars+=quantifiedExprVar (COMMA vars+=quantifiedExprVar)*
                          Ksatisfies exprSingle;

quantifiedExprVar       : varRef (KW_AS sequenceType)? KW_IN exprSingle;

switchExpr              : Kswitch '(' cond=expr ')' cases+=switchCaseClause+ Kdefault KW_RETURN def=exprSingle;

switchCaseClause        : (Kcase cond+=exprSingle)+ KW_RETURN ret=exprSingle;

typeSwitchExpr          : Ktypeswitch '(' cond=expr ')' cses+=caseClause+ Kdefault (var_ref=varRef)? KW_RETURN def=exprSingle;

caseClause              : Kcase (var_ref=varRef KW_AS)? union+=sequenceType ('|' union+=sequenceType)* KW_RETURN ret=exprSingle;

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

treatExpr               : main_expr=castableExpr ( Ktreat KW_AS seq=sequenceType )?;

castableExpr            : main_expr=castExpr ( Kcastable KW_AS single=singleType )?;

castExpr                : main_expr=arrowExpr ( Kcast KW_AS single=singleType )?;

arrowExpr               : main_expr=unaryExpr (('=' '>') function+=arrowFunctionSpecifier arguments+=argumentList)*;

arrowFunctionSpecifier  : qname | varRef | parenthesizedExpr;

unaryExpr               : op+=('-' | '+')* main_expr=valueExpr;

valueExpr               : simpleMap_expr=simpleMapExpr
                        | validate_expr=validateExpr
                        | annotate_expr=annotateExpr;

validateExpr            : Kvalidate Ktype sequenceType '{' expr '}';

annotateExpr            : Kannotate Ktype sequenceType '{' expr '}';

simpleMapExpr           : main_expr=pathExpr ('!' map_expr+=pathExpr)*;

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


varRef                  : DOLLAR var_name=qname;

varName: eqName ;

parenthesizedExpr       : '(' expr? ')';

contextItemExpr         : '$$';

orderedExpr             : 'ordered' '{' expr '}';

unorderedExpr           : 'unordered' '{' expr '}';

functionCall            : fn_name=qname argumentList;

argumentList            : '('  (args+=argument (COMMA args+=argument)*)? ')';

argument                : exprSingle | ArgumentPlaceholder;

functionItemExpr        : namedFunctionRef | inlineFunctionExpr;

namedFunctionRef        : fn_name=qname '#' arity=Literal;

inlineFunctionExpr      : annotations 'function' '(' paramList? ')'
                           (KW_AS return_type=sequenceType)?
                           ('{' (fn_body=statementsAndOptionalExpr) '}');

///////////////////////// Updating Expressions

insertExpr              : Kinsert Kjson to_insert_expr=exprSingle Kinto main_expr=exprSingle (KW_AT Kposition pos_expr=exprSingle)?
                        | Kinsert Kjson pairConstructor ( COMMA pairConstructor )* Kinto main_expr=exprSingle;

deleteExpr              : Kdelete Kjson updateLocator;

renameExpr              : Krename Kjson updateLocator KW_AS name_expr=exprSingle;

replaceExpr             : Kreplace Kvalue Kof Kjson updateLocator Kwith replacer_expr=exprSingle;

transformExpr           : Kcopy copyDecl ( COMMA copyDecl )* Kmodify mod_expr=exprSingle KW_RETURN ret_expr=exprSingle;

appendExpr              : Kappend Kjson to_append_expr=exprSingle Kinto array_expr=exprSingle;

updateLocator           : main_expr=postFixExpr;

copyDecl                : var_ref=varRef COLON_EQ src_expr=exprSingle;

// TODO: Direct element constructors


///////////////////////// Top Level Updating Expressions

createCollectionExpr    : Kcreate Kcollection collectionMode=(Ktable | Kdeltafile | Kicebergtable) '(' collection_name=exprSimple ')' (Kwith content=exprSingle)?;

deleteIndexExpr         : Kdelete ( (first=Kfirst | last=Klast) num=exprSingle? ) Kfrom Kcollection collectionMode=(Ktable | Kdeltafile | Kicebergtable) '(' collection_name=exprSimple ')';

deleteSearchExpr        : Kdelete content=exprSingle Kfrom Kcollection;

insertIndexExpr         : Kinsert content=exprSingle ( (KW_AT pos=exprSingle) | first=Kfirst | last=Klast ) Kinto Kcollection collectionMode=(Ktable | Kdeltafile | Kicebergtable) '(' collection_name=exprSimple ')';

insertSearchExpr        : Kinsert content=exprSingle (before=Kbefore | after=Kafter) target=exprSingle Kinto Kcollection;

truncateCollectionExpr  : (Kdelete | Ktruncate) Kcollection collectionMode=(Ktable | Kdeltafile | Kicebergtable) '(' collection_name=exprSimple ')';

editCollectionExpr      : Kedit target=exprSingle Kinto content=exprSingle KW_IN Kcollection;


///////////////////////// XPath

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

abbrevForwardStep: Kat_symbol? nodeTest ;

reverseStep: (reverseAxis nodeTest) | abbrevReverseStep ;

reverseAxis: ( Kparent
             | Kancestor
             | Kpreceding_sibling
             | Kpreceding
             | Kancestor_or_self ) ':' ':';

abbrevReverseStep: '..' ;

nodeTest: nameTest | kindTest ;

nameTest: qname | wildcard ;

wildcard: '*'            # allNames
        | nCNameWithLocalWildcard  # allWithNS    // walkers must strip out the trailing :*
        | nCNameWithPrefixWildcard # allWithLocal // walkers must strip out the leading *:
        ;
nCNameWithLocalWildcard :  NCName ':' '*' ;
nCNameWithPrefixWildcard: '*' ':' NCName ;


predicateList: predicate*;

kindTest: documentTest
        | elementTest
        | attributeTest
        | schemaElementTest
        | schemaAttributeTest
        | piTest
        | commentTest
        | textTest
        | namespaceNodeTest
        | binaryNodeTest
        | anyKindTest
        ;

anyKindTest: Knode '(' ')' ;

binaryNodeTest: Kbinary '(' ')' ;

documentTest: Kdocument_node '(' (elementTest | schemaElementTest)? ')' ;

textTest: Ktext '(' ')' ;

commentTest: Kcomment '(' ')' ;

namespaceNodeTest: Knamespace_node '(' ')' ;

piTest: Kpi '(' (NCName | stringLiteral)? ')' ;

attributeTest: Kattribute '(' (attributeNameOrWildcard (COMMA type=typeName)?)? ')' ;

attributeNameOrWildcard: attributeName | '*' ;

schemaAttributeTest: Kschema_attribute '(' attributeDeclaration ')' ;

attributeDeclaration: attributeName ;

elementTest: Kelement '(' (elementNameOrWildcard (COMMA type=typeName optional='?'?)?)? ')' ;

elementNameOrWildcard: elementName | '*' ;

schemaElementTest: Kschema_element '(' elementDeclaration ')' ;

elementDeclaration: elementName ;

attributeName: qname ;

elementName: qname ;

simpleTypeName: typeName ;

typeName: qname;

///////////////////////// Types

typeDeclaration: KW_AS sequenceType ;

sequenceType            : '(' ')'
                        | item=itemType (question+='?' | star+='*' | plus+='+')?;

objectConstructor       : '{' ( pairConstructor (COMMA pairConstructor)* )? '}'
                        | merge_operator+='{|' expr '|}';

itemType                : qname
                        | NullLiteral
                        | functionTest;

functionTest	        : (anyFunctionTest | typedFunctionTest);

anyFunctionTest         : 'function' '(' '*' ')';

typedFunctionTest	    : 'function' '(' (st+=sequenceType (COMMA st+=sequenceType)*)? ')' 'as' rt=sequenceType;

singleType              : item=itemType (question +='?')?;

pairConstructor         :  ( lhs=exprSingle ) (':' | '?') rhs=exprSingle;

arrayConstructor        :  '[' expr? ']';

uriLiteral              : stringLiteral;

stringLiteral           : STRING;

keyWords                : Kjsoniq
                        | Kand
                        | Kcast
                        | Kcastable
                        | KW_COLLATION
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
                        | KW_STABLE
                        | Kvariable
                        | Kascending
                        | Kdescending
                        | KW_EMPTY
                        | KW_ALLOWING
                        | KW_AS
                        | KW_AT
                        | KW_IN
                        | Kif
                        | KW_FOR
                        | KW_LET
                        | KW_WHERE
                        | KW_GROUP
                        | KW_BY
                        | KW_ORDER
                        | KW_COUNT
                        | KW_RETURN
                        | KW_TUMBLING
                        | KW_WINDOW
                        | KW_SLIDING
                        | KW_START
                        | KW_WHEN
                        | KW_ONLY
                        | KW_END
                        | KW_PREVIOUS
                        | KW_NEXT
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
                        | Kcreate
                        | Kcollection
                        | Ktable
                        | Kdeltafile
                        | Kicebergtable
                        | Ktruncate
                        | Kfirst
                        | Klast
                        | Kfrom
                        | Kedit
                        | Kbefore
                        | Kafter
                        | Kimport
                        | Kschema
                        | Knamespace
                        | Kelement
                        | Kslash
                        | Kdslash
                        | Kat_symbol
                        | Kchild
                        | Kdescendant
                        | Kattribute
                        | Kself
                        | Kdescendant_or_self
                        | Kfollowing_sibling
                        | Kfollowing
                        | Kparent
                        | Kancestor
                        | Kpreceding_sibling
                        | Kpreceding
                        | Kancestor_or_self
                        | Knode
                        | Kbinary
                        | Kdocument
                        | Kdocument_node
                        | Kpi
                        | Knamespace_node
                        | Kschema_attribute
                        | Kschema_element
                        | Karray_node
                        | Kboolean_node
                        | Knull_node
                        | Knumber_node
                        | Kobject_node
                        | Kcomment
                        ;

///////////////////////// literals

COMMA                   : ',';

COLON_EQ                : ':=';

KW_FOR                    : 'for';

KW_LET                    : 'let';

KW_WHERE                  : 'where';

KW_GROUP                  : 'group';

KW_TUMBLING               : 'tumbling';

KW_WINDOW                 : 'window';

DOLLAR                    : '$';

KW_SLIDING                : 'sliding';

KW_START                  : 'start';

KW_END                    : 'end';

KW_ONLY                   : 'only';

KW_WHEN                   : 'when';

KW_PREVIOUS               : 'previous';

KW_NEXT                   : 'next';

KW_BY                     : 'by';

KW_ORDER                  : 'order';

KW_RETURN                 : 'return';

Kif                     : 'if';

KW_IN                     : 'in';

KW_AS                     : 'as';

KW_AT                     : 'at';

KW_ALLOWING               : 'allowing';

KW_EMPTY                  : 'empty';

KW_COUNT                  : 'count';

KW_STABLE                 : 'stable';

Kascending              : 'ascending';

Kdescending             : 'descending';

Ksome                   : 'some';

Kevery                  : 'every';

Ksatisfies              : 'satisfies';

KW_COLLATION              : 'collation';

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

Kwith                   : 'with';

Kposition               : 'position';

Kjson                   : 'json';

Kupdating               :  'updating';

Kcreate                 : 'create';

Kcollection             : 'collection';

Ktable                  : 'table';

Kdeltafile              : 'delta-file';

Kicebergtable           : 'iceberg-table';

Ktruncate               : 'truncate';

Kfirst                  : 'first';

Klast                   : 'last';

Kfrom                   : 'from';

Kedit                   : 'edit';

Kafter                  : 'after';

Kbefore                 : 'before';


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

