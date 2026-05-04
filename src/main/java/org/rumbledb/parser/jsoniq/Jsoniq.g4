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
 * A parser grammar for JSONiq 1.0/3.1 that includes the XQuery Scripting Extensions, and additional update features.
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

module : // replaced with the versionDecl production to match the JSONiq grammar
         (KW_JSONIQ KW_VERSION vers=stringLiteral (KW_ENCODING encoding=stringLiteral)? SEMICOLON)?
         // TODO: subsequent optional main modules are currently ignored
         (libraryModule | main=mainModule) ;

versionDecl: KW_JSONIQ KW_VERSION version=stringLiteral
             (KW_ENCODING encoding=stringLiteral)?
             SEMICOLON ;

// mainModule and queryBody are replaced with the mainModule and program rules according to the XQuery Scripting Extension spec

libraryModule: KW_MODULE KW_NAMESPACE NCName EQUAL uri=uriLiteral SEMICOLON prolog;

// MODULE PROLOG ///////////////////////////////////////////////////////////////

prolog: ((setter | namespaceDecl | moduleImport) SEMICOLON)*
        (annotatedDecl SEMICOLON)* ;

// added to match the JSONiq grammar
annotatedDecl: functionDecl
              | varDecl
              | typeDecl
              | contextItemDecl
              ;

setter: boundarySpaceDecl
      | defaultCollationDecl
      | baseURIDecl
      | constructionDecl
      | orderingModeDecl
      | emptyOrderDecl
      | copyNamespacesDecl
      | decimalFormatDecl ;

boundarySpaceDecl: KW_DECLARE KW_BOUNDARY_SPACE type=(KW_PRESERVE | KW_STRIP) ;
defaultCollationDecl: KW_DECLARE KW_DEFAULT KW_COLLATION uriLiteral ;
baseURIDecl: KW_DECLARE KW_BASE_URI uriLiteral ;
constructionDecl: KW_DECLARE KW_CONSTRUCTION type=(KW_STRIP | KW_PRESERVE) ;
orderingModeDecl: KW_DECLARE KW_ORDERING type=(KW_ORDERED | KW_UNORDERED) ;
emptyOrderDecl: KW_DECLARE KW_DEFAULT KW_ORDER KW_EMPTY emptySequenceOrder=(KW_GREATEST | KW_LEAST) ;
copyNamespacesDecl: KW_DECLARE KW_COPY_NS preserveMode COMMA inheritMode ;
preserveMode: KW_PRESERVE | KW_NO_PRESERVE ;
inheritMode: KW_INHERIT | KW_NO_INHERIT ;
decimalFormatDecl: KW_DECLARE (
                      (KW_DECIMAL_FORMAT eqName)
                    | (KW_DEFAULT KW_DECIMAL_FORMAT)
                   )
                   (DFPropertyName EQUAL stringLiteral)*;

eqName: qname ;

qname                   : ((ns=NCName | nskw=keyword)':')?
                          (local_name=NCName | local_namekw = keyword);

// matches the definition of NCName in the XQuery 3.1 spec
// this includes all the valid characters, including all the keywords
ncName: NCName | keyword ;

DFPropertyName          : 'decimal-separator'
                        | 'grouping-separator'
                        | 'infinity'
                        | 'minus-sign'
                        | 'NaN'
                        | 'percent'
                        | 'per-mille'
                        | 'zero-digit'
                        | 'digit'
                        | 'pattern-separator';

moduleImport: KW_IMPORT KW_MODULE
              (KW_NAMESPACE prefix=ncName EQUAL)?
              targetNamespace=uriLiteral
              (KW_AT uriLiteral (COMMA uriLiteral)*)? ;


namespaceDecl: KW_DECLARE KW_NAMESPACE ncName EQUAL uriLiteral ;

varDecl: KW_DECLARE (annotations|ncName) KW_VARIABLE
         // replaced "$" varName with varRef to match the JSONiq grammar
         varRef
         // replaced with the typeDeclaration production to match the JSONiq grammar
         (KW_AS sequenceType)?
         (
          // replaced with the varValue production to match the JSONiq grammar
            (COLON_EQ expr)
          // replaced with the varDefaultValue production to match the JSONiq grammar
          | (external=KW_EXTERNAL (COLON_EQ expr)?)
          | (LBRACE varValue RBRACE)
          | (external=KW_EXTERNAL(LBRACE varDefaultValue RBRACE)?)
         ) ;

varValue: expr ;

varDefaultValue: expr ;

contextItemDecl         : KW_DECLARE Kcontext Kitem (KW_AS sequenceType)? ((COLON_EQ exprSingle) | (external=EXTERNAL (COLON_EQ exprSingle)?));

functionDecl            : KW_DECLARE annotations 'function' fn_name=qname LPAREN paramList? RPAREN
                          (KW_AS return_type=sequenceType)?
                          (LBRACE (fn_body=statementsAndOptionalExpr) RBRACE | is_external=EXTERNAL);

typeDecl                : KW_DECLARE Ktype type_name=qname 'as' (schema=schemaLanguage)? type_definition=exprSingle;

schemaLanguage          : 'jsound' 'compact'
                        | 'jsound' 'verbose'
                        | 'json' 'schema';

paramList               : param (COMMA param)*;

param                   : DOLLAR name=qname (KW_AS sequenceType)? ;

annotations: annotation* ;

// added the updating keyword to support the out-of-spec updating expressions extension
annotation                  : ('%' name=qname (LPAREN Literal (COMMA Literal)* RPAREN)? | updating=Kupdating);

// EXPRESSIONS /////////////////////////////////////////////////////////////////

expr: exprSingle (COMMA exprSingle)* ;

flworExpr: // replaced with the initialClause production to match the JSONiq grammar
           (start_for=forClause| start_let=letClause | start_window=windowClause)
           // replaced with the intermediateClause production to match the JSONiq grammar
           (forClause | letClause | windowClause| whereClause | groupByClause | orderByClause | countClause)*
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

// renamed from orderSpec to orderByExpr to match the JSONiq grammar
orderByExpr: ex=exprSingle
           (KW_ASCENDING | desc=KW_DESCENDING)?
           (KW_EMPTY (gr=KW_GREATEST|ls=KW_LEAST))?
           (KW_COLLATION uril=uriLiteral)?
         ;

quantifiedExpr: (so=KW_SOME | ev=KW_EVERY) vars+=quantifiedExprVar (COMMA vars+=quantifiedExprVar)*
                KW_SATISFIES exprSingle ;

// renamed from quantifiedVar to quantifiedExprVar to match the JSONiq grammar
quantifiedExprVar: var_ref=varRef
                  // replaced with the typeDeclaration production to match the JSONiq grammar
                  (KW_AS seq=sequenceType)?
                  KW_IN exprSingle ;

switchExpr: KW_SWITCH LPAREN cond=expr RPAREN
                cases+=switchCaseClause+
                KW_DEFAULT KW_RETURN def=exprSingle ;

switchCaseClause: (KW_CASE cond+=exprSingle)+ KW_RETURN ret=exprSingle ;

typeswitchExpr: KW_TYPESWITCH LPAREN cond=expr RPAREN
                cses+=caseClause+
                KW_DEFAULT (var_ref=varRef)? KW_RETURN def=exprSingle ;

caseClause: KW_CASE (var_ref=varRef KW_AS)? union+=sequenceType (VBAR union+=sequenceType)* KW_RETURN
            ret=exprSingle ;

ifExpr: KW_IF LPAREN test_condition=expr RPAREN
        KW_THEN branch=exprSingle
        KW_ELSE else_branch=exprSingle ;

// replaced with the tryClause and the enclosedTryTargetExpression productions to match the JSONiq grammar
tryCatchExpr: KW_TRY LBRACE try_expression=expr? RBRACE catches+=catchClause+ ;

catchClause: KW_CATCH
             // replaced with the catchErrorList production to match the JSONiq grammar
             (jokers+='*' | errors+=qname) (VBAR (jokers+='*' | errors+=qname))* LBRACE catch_expression=expr RBRACE;

orExpr: main_expr=andExpr (KW_OR rhs+=andExpr)* ;

andExpr                 : main_expr=notExpr ( Kand rhs+=notExpr )*;

notExpr                 : op+=Knot ? main_expr=comparisonExpr;

comparisonExpr          : main_expr=stringConcatExpr
                          ( op+=('eq' | 'ne' | 'lt' | 'le' | 'gt' | 'ge'
                          | EQUAL | '!=' | '<' | '<=' | '>' | '>=') rhs+=stringConcatExpr )?;

stringConcatExpr        : main_expr=rangeExpr ( '||' rhs+=rangeExpr )* ;

rangeExpr               : main_expr=additiveExpr ( Kto rhs+=additiveExpr )?;

additiveExpr            : main_expr=multiplicativeExpr ( op+=('+' | '-') rhs+=multiplicativeExpr )*;

multiplicativeExpr      : main_expr=instanceOfExpr ( op+=('*' | 'div' | 'idiv' | 'mod') rhs+=instanceOfExpr )*;

instanceOfExpr          : main_expr=isStaticallyExpr ( Kinstance Kof seq=sequenceType)?;

isStaticallyExpr        : main_expr=treatExpr ( Kis Kstatically seq=sequenceType)?;

treatExpr               : main_expr=castableExpr ( Ktreat KW_AS seq=sequenceType )?;

castableExpr            : main_expr=castExpr ( Kcastable KW_AS single=singleType )?;

castExpr                : main_expr=arrowExpr ( Kcast KW_AS single=singleType )?;

arrowExpr               : main_expr=unaryExpr ((EQUAL '>') function+=arrowFunctionSpecifier arguments+=argumentList)*;

arrowFunctionSpecifier  : qname | varRef | parenthesizedExpr;

unaryExpr               : op+=('-' | '+')* main_expr=valueExpr;

valueExpr               : simpleMap_expr=simpleMapExpr
                        | validate_expr=validateExpr
                        | annotate_expr=annotateExpr;

validateExpr            : Kvalidate Ktype sequenceType LBRACE expr RBRACE;

annotateExpr            : Kannotate Ktype sequenceType LBRACE expr RBRACE;

simpleMapExpr           : main_expr=pathExpr ('!' map_expr+=pathExpr)*;

postFixExpr             : main_expr=primaryExpr (arrayLookup | predicate | objectLookup | arrayUnboxing | argumentList)*;

arrayLookup             : '[' '[' expr ']' ']';

arrayUnboxing           : '[' ']';

predicate               : '[' expr ']';

objectLookup            : '.' ( kw=keyword | lt=stringLiteral | nc=NCName | pe=parenthesizedExpr | vr=varRef | ci=contextItemExpr);

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

blockExpr : LBRACE statementsAndExpr RBRACE ;


varRef                  : DOLLAR var_name=qname;

varName: eqName ;

parenthesizedExpr       : LPAREN expr? RPAREN;

contextItemExpr         : '$$';

orderedExpr             : KW_ORDERED LBRACE expr RBRACE;

unorderedExpr           : KW_UNORDERED LBRACE expr RBRACE;

functionCall            : fn_name=qname argumentList;

argumentList            : LPAREN  (args+=argument (COMMA args+=argument)*)? RPAREN;

argument                : exprSingle | ArgumentPlaceholder;

functionItemExpr        : namedFunctionRef | inlineFunctionExpr;

namedFunctionRef        : fn_name=qname '#' arity=Literal;

inlineFunctionExpr      : annotations 'function' LPAREN paramList? RPAREN
                           (KW_AS return_type=sequenceType)?
                           (LBRACE (fn_body=statementsAndOptionalExpr) RBRACE);

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

createCollectionExpr    : Kcreate Kcollection collectionMode=(Ktable | Kdeltafile | Kicebergtable) LPAREN collection_name=exprSimple RPAREN (Kwith content=exprSingle)?;

deleteIndexExpr         : Kdelete ( (first=Kfirst | last=Klast) num=exprSingle? ) Kfrom Kcollection collectionMode=(Ktable | Kdeltafile | Kicebergtable) LPAREN collection_name=exprSimple RPAREN;

deleteSearchExpr        : Kdelete content=exprSingle Kfrom Kcollection;

insertIndexExpr         : Kinsert content=exprSingle ( (KW_AT pos=exprSingle) | first=Kfirst | last=Klast ) Kinto Kcollection collectionMode=(Ktable | Kdeltafile | Kicebergtable) LPAREN collection_name=exprSimple RPAREN;

insertSearchExpr        : Kinsert content=exprSingle (before=Kbefore | after=Kafter) target=exprSingle Kinto Kcollection;

truncateCollectionExpr  : (Kdelete | Ktruncate) Kcollection collectionMode=(Ktable | Kdeltafile | Kicebergtable) LPAREN collection_name=exprSimple RPAREN;

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

anyKindTest: Knode LPAREN RPAREN ;

binaryNodeTest: Kbinary LPAREN RPAREN ;

documentTest: Kdocument_node LPAREN (elementTest | schemaElementTest)? RPAREN ;

textTest: Ktext LPAREN RPAREN ;

commentTest: Kcomment LPAREN RPAREN ;

namespaceNodeTest: Knamespace_node LPAREN RPAREN ;

piTest: Kpi LPAREN (NCName | stringLiteral)? RPAREN ;

attributeTest: Kattribute LPAREN (attributeNameOrWildcard (COMMA type=typeName)?)? RPAREN ;

attributeNameOrWildcard: attributeName | '*' ;

schemaAttributeTest: Kschema_attribute LPAREN attributeDeclaration RPAREN ;

attributeDeclaration: attributeName ;

elementTest: Kelement LPAREN (elementNameOrWildcard (COMMA type=typeName optional='?'?)?)? RPAREN ;

elementNameOrWildcard: elementName | '*' ;

schemaElementTest: Kschema_element LPAREN elementDeclaration RPAREN ;

elementDeclaration: elementName ;

attributeName: qname ;

elementName: qname ;

simpleTypeName: typeName ;

typeName: qname;

///////////////////////// Types

typeDeclaration: KW_AS sequenceType ;

sequenceType            : LPAREN RPAREN
                        | item=itemType (question+='?' | star+='*' | plus+='+')?;

objectConstructor       : LBRACE ( pairConstructor (COMMA pairConstructor)* )? RBRACE
                        | merge_operator+='{|' expr '|}';

itemType                : qname
                        | NullLiteral
                        | functionTest;

functionTest	        : (anyFunctionTest | typedFunctionTest);

anyFunctionTest         : 'function' LPAREN '*' RPAREN;

typedFunctionTest	    : 'function' LPAREN (st+=sequenceType (COMMA st+=sequenceType)*)? RPAREN 'as' rt=sequenceType;

singleType              : item=itemType (question +='?')?;

pairConstructor         :  ( lhs=exprSingle ) (':' | '?') rhs=exprSingle;

arrayConstructor        :  '[' expr? ']';

uriLiteral              : stringLiteral;

stringLiteral           : STRING;

keyword                : KW_JSONIQ
                        | KW_MODULE
                        | KW_NAMESPACE
                        | Kand
                        | Kcast
                        | Kcastable
                        | KW_COLLATION
                        | Kcontext
                        | KW_DECLARE
                        | KW_DEFAULT
                        | KW_ELSE
                        | KW_GREATEST
                        | Kinstance
                        | Kstatically
                        | Kis
                        | Kitem
                        | KW_LEAST
                        | Knot
                        | NullLiteral
                        | Kof
                        | Kor
                        | KW_THEN
                        | Kto
                        | Ktreat
                        | KW_TYPESWITCH
                        | KW_VERSION
                        | KW_SWITCH
                        | KW_CASE
                        | KW_TRY
                        | KW_CATCH
                        | KW_SOME
                        | KW_EVERY
                        | KW_SATISFIES
                        | KW_STABLE
                        | KW_VARIABLE
                        | KW_ASCENDING
                        | KW_DESCENDING
                        | KW_EMPTY
                        | KW_ALLOWING
                        | KW_AS
                        | KW_AT
                        | KW_IN
                        | KW_IF
                        | KW_FOR
                        | KW_LET
                        | KW_WHERE
                        | KW_GROUP
                        | KW_BY
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
                        | KW_BASE_URI
                        | KW_CONSTRUCTION
                        | KW_PRESERVE
                        | KW_STRIP
                        | KW_BOUNDARY_SPACE
                        | KW_ENCODING
                        | KW_ORDERING
                        | KW_ORDERED
                        | KW_UNORDERED
                        | KW_ORDER
                        | KW_COPY_NS
                        | KW_NO_PRESERVE
                        | KW_INHERIT
                        | KW_NO_INHERIT
                        | KW_DECIMAL_FORMAT
                        | KW_EXTERNAL
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
                        | KW_BREAK
                        | KW_LOOP
                        | KW_CONTINUE
                        | KW_EXIT
                        | KW_RETURNING
                        | KW_WHILE
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
                        | KW_IMPORT
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

EQUAL                   : '=';

SEMICOLON               : ';';

LPAREN                  : '(';

RPAREN                  : ')';

VBAR                    : '|';

LBRACE                    : '{';

RBRACE                    : '}';

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

KW_RETURN                 : 'return';

KW_IF                     : 'if';

KW_IN                     : 'in';

KW_AS                     : 'as';

KW_AT                     : 'at';

KW_ALLOWING               : 'allowing';

KW_EMPTY                  : 'empty';

KW_COUNT                  : 'count';

KW_STABLE                 : 'stable';

KW_ASCENDING              : 'ascending';

KW_DESCENDING             : 'descending';

KW_SOME                   : 'some';

KW_EVERY                  : 'every';

KW_SATISFIES              : 'satisfies';

KW_COLLATION              : 'collation';

KW_GREATEST               : 'greatest';

KW_LEAST                  : 'least';

KW_SWITCH                 : 'switch';

KW_ORDERING               : 'ordering';

KW_ORDERED                : 'ordered';

KW_UNORDERED              : 'unordered';

KW_CASE                   : 'case';

KW_TRY                    : 'try';

KW_CATCH                  : 'catch';

KW_DEFAULT                : 'default';

KW_THEN                   : 'then';

KW_ELSE                   : 'else';

KW_TYPESWITCH             : 'typeswitch';

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

KW_JSONIQ                 : 'jsoniq';

KW_ENCODING                 : 'encoding';

KW_VERSION                 : 'version';

KW_MODULE               : 'module';

KW_NAMESPACE            : 'namespace';

KW_BOUNDARY_SPACE        : 'boundary-space';

KW_PRESERVE              : 'preserve';

KW_STRIP                 : 'strip';

KW_BASE_URI              : 'base-uri';

KW_CONSTRUCTION          : 'construction';

KW_ORDER                 : 'order';

KW_COPY_NS               : 'copy-namespaces';

KW_NO_PRESERVE           : 'no-preserve';

KW_INHERIT               : 'inherit';

KW_NO_INHERIT            : 'no-inherit';

KW_DECIMAL_FORMAT       : 'decimal-format';

KW_EXTERNAL             : 'external';

Ktrue                   : 'true';

Kfalse                  : 'false';

Ktype                   : 'type';

Kvalidate               : 'validate';

Kannotate               : 'annotate';

KW_DECLARE                : 'declare';

Kcontext                : 'context';

Kitem                   : 'item';

KW_VARIABLE               : 'variable';

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
KW_IMPORT                 : 'import';
Kschema                 : 'schema';
Knamespace              : KW_NAMESPACE;
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
KW_BREAK                  : 'break' ;
KW_LOOP                   : 'loop' ;
KW_CONTINUE               : 'continue' ;
KW_EXIT                   : 'exit' ;
KW_RETURNING              : 'returning' ;
KW_WHILE                  : 'while' ;

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

XQComment               : LPAREN ':' (XQComment | LPAREN ~[:] | ':' ~[)] | ~[:(])* ':'+ RPAREN -> channel(HIDDEN);

ContentChar             :  ~["'{}<&]  ;

// XQuery Scripting Extension /////////////////////////////////////////////////////////////
// the following section contains rules for the XQuery Scripting Extension Proposal

// New query body for main modules

mainModule              : prolog program;

program                 : statementsAndOptionalExpr ;

// Mixing Expressions and Statements

statements                  : statement* ;

statementsAndExpr           : statements expr ;

statementsAndOptionalExpr   : statements expr? ;

statement               : applyStatement
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

applyStatement          : exprSimple SEMICOLON ;

assignStatement         : DOLLAR qname COLON_EQ exprSingle SEMICOLON ;

blockStatement          : LBRACE statements RBRACE ;

breakStatement          : KW_BREAK KW_LOOP SEMICOLON ;

continueStatement       : KW_CONTINUE KW_LOOP SEMICOLON ;

exitStatement           : KW_EXIT KW_RETURNING exprSingle SEMICOLON ;

flworStatement          : (start_for=forClause| start_let=letClause)
                          (forClause | letClause | whereClause | groupByClause | orderByClause | countClause)*
                          KW_RETURN returnStmt=statement ;

ifStatement:            KW_IF LPAREN test_expr=expr RPAREN
                        KW_THEN branch=statement
                        KW_ELSE else_branch=statement ;

switchStatement         : KW_SWITCH LPAREN condExpr=expr RPAREN cases+=switchCaseStatement+ KW_DEFAULT KW_RETURN def=statement ;

switchCaseStatement     : (KW_CASE cond+=exprSingle)+ KW_RETURN ret=statement ;

tryCatchStatement       : KW_TRY try_block=blockStatement catches+=catchCaseStatement+ ;

catchCaseStatement      : KW_CATCH (jokers+='*' | errors+=qname) (VBAR (jokers+='*' | errors+=qname))* catch_block=blockStatement;

// replaced "$" varName with varRef to match the JSONiq grammar
typeSwitchStatement     : KW_TYPESWITCH LPAREN cond=expr RPAREN cases+=caseStatement+ KW_DEFAULT (var_ref=varRef)? (KW_RETURN) def=statement ;

// replaced "$" varName with varRef to match the JSONiq grammar
caseStatement           : KW_CASE (var_ref=varRef KW_AS)? union+=sequenceType (VBAR union+=sequenceType)* (KW_RETURN) ret=statement ;

varDeclStatement        : annotations KW_VARIABLE varDeclForStatement (COMMA varDeclForStatement)* SEMICOLON ;

// added to match the JSONiq grammar
varDeclForStatement     : var_ref=varRef (KW_AS sequenceType)? (COLON_EQ expr_vals+=exprSingle)? ;

whileStatement          : KW_WHILE LPAREN test_expr=expr RPAREN stmt=statement ;


// Expressions

// redefined according to the XQuery Scripting Extension spec
exprSingle              : exprSimple
                        | flworExpr
                        | ifExpr
                        | switchExpr
                        | tryCatchExpr
                        | typeswitchExpr
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
