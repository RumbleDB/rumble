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

libraryModule: KW_MODULE KW_NAMESPACE ncName EQUAL uri=uriLiteral SEMICOLON prolog;

// MODULE PROLOG ///////////////////////////////////////////////////////////////

prolog: ((defaultNamespaceDecl | setter | namespaceDecl | schemaImport | moduleImport) SEMICOLON)*
        (annotatedDecl SEMICOLON)* ;

// added to match the JSONiq grammar
annotatedDecl: functionDecl
              | varDecl
              | typeDecl
              | contextItemDecl
              | optionDecl
              ;

defaultNamespaceDecl: KW_DECLARE KW_DEFAULT
                      type=(KW_ELEMENT | KW_FUNCTION)
                      KW_NAMESPACE
                      uri=stringLiteral ;

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

eqName: qname | URIQualifiedName ;

BracedURILiteral: 'Q' '{' (~[&{}])* '}' ;
URIQualifiedName: BracedURILiteral NCName ;

qname                   : ((ns=NCName | nskw=keyword)COLON)?
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

schemaImport: KW_IMPORT KW_SCHEMA
              schemaPrefix?
              nsURI=uriLiteral
              (KW_AT locations+=uriLiteral (COMMA locations+=uriLiteral)*)? ;

schemaPrefix: (KW_NAMESPACE ncName EQUAL | KW_DEFAULT KW_ELEMENT KW_NAMESPACE) ;

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
            (COLON_EQ exprSingle)
          // replaced with the varDefaultValue production to match the JSONiq grammar
          | (external=KW_EXTERNAL (COLON_EQ exprSingle)?)
         ) ;

contextItemDecl: KW_DECLARE KW_CONTEXT KW_ITEM
                 //(KW_AS itemType)?
                 // TODO: this is out of spec. However, it is currently kept to match the JSONiq grammar
                 (KW_AS sequenceType)? // TODO: change to itemType, update expressions to use itemType, update back JSONiq grammar
                 ((COLON_EQ value=exprSingle)
                 | (external=KW_EXTERNAL (COLON_EQ defaultValue=exprSingle)?)) ;

// constrains to valid function names only
// see https://www.w3.org/TR/xquery-31/#parse-note-reserved-function-names
functionDecl: KW_DECLARE (annotations) KW_FUNCTION fn_name=functionName LPAREN paramList? RPAREN
              // replaced with the functionReturn production to match the JSONiq grammar
              (KW_AS return_type=sequenceType)?
              // replaced functionBody to match the JSONiq grammar and the XQuery Scripting Extension spec
              ( LBRACE (fn_body=statementsAndOptionalExpr) RBRACE | is_external=KW_EXTERNAL) ;

functionName: qname ;

// renamed from functionParams to paramList to match the JSONiq grammar
paramList: param (COMMA param)* ;

// renamed from functionParam to param to match the JSONiq grammar
// replaced with the typeDeclaration production to match the JSONiq grammar
param: DOLLAR name=qname (KW_AS sequenceType)? ;

annotations: annotation* ;

// added the updating keyword to support the out-of-spec updating expressions extension
annotation: MOD name=eqName (LPAREN literal (COMMA literal)* RPAREN)? | updating=KW_UPDATING ;

optionDecl: KW_DECLARE KW_OPTION name=qname value=stringLiteral ;

typeDecl                : KW_DECLARE KW_TYPE type_name=qname 'as' (schema=schemaLanguage)? type_definition=exprSingle;

schemaLanguage          : 'jsound' 'compact'
                        | 'jsound' 'verbose'
                        | 'json' 'schema';

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
             ((jokers+=wildcard | errors+=eqName) (VBAR (jokers+=wildcard | errors+=eqName))* | (LPAREN DOLLAR varName RPAREN))
             // replaced with the enclosedExpression production to match the JSONiq grammar
             LBRACE catch_expression=expr? RBRACE ;

enclosedExpression: LBRACE expr? RBRACE ;

orExpr: main_expr=andExpr (KW_OR rhs+=andExpr)* ;

andExpr: main_expr=notExpr ( KW_AND rhs+=notExpr )*;

notExpr: op+=Knot ? main_expr=comparisonExpr;

comparisonExpr: main_expr=stringConcatExpr (  op+=compOp rhs+=stringConcatExpr )? ;

stringConcatExpr: main_expr=rangeExpr (CONCATENATION rhs+=rangeExpr)* ;

rangeExpr: main_expr=additiveExpr (KW_TO rhs+=additiveExpr)? ;

additiveExpr: main_expr=multiplicativeExpr ( op+=(PLUS | MINUS) rhs+=multiplicativeExpr )* ;

multiplicativeExpr: main_expr=unionExpr ( op+=(STAR | KW_DIV | KW_IDIV | KW_MOD) rhs+=unionExpr )* ;

unionExpr: intersectExceptExpr ( (KW_UNION | VBAR) intersectExceptExpr)* ;

intersectExceptExpr: instanceOfExpr ( (KW_INTERSECT | KW_EXCEPT) instanceOfExpr)* ;

instanceOfExpr: main_expr=isStaticallyExpr ( Kinstance Kof seq=sequenceType)?;

isStaticallyExpr        : main_expr=treatExpr ( KW_IS Kstatically seq=sequenceType)?;

treatExpr: main_expr=castableExpr ( KW_TREAT KW_AS seq=sequenceType)? ;

castableExpr: main_expr=castExpr ( KW_CASTABLE KW_AS single=singleType)?;

castExpr: main_expr=arrowExpr (KW_CAST KW_AS single=singleType)? ;

arrowExpr: main_expr=unaryExpr (ARROW function+=arrowFunctionSpecifier arguments+=argumentList)* ;

unaryExpr: op+=(MINUS | PLUS)* main_expr=valueExpr ;

valueExpr: validate_expr=validateExpr | extensionExpr | annotate_expr=annotateExpr | simpleMap_expr=simpleMapExpr ;

/* 
 * this token was added to prevent the antlr error
 * "label assigned to a block which is not a set" in the comparisonExpr token definition
 */
compOp: valueComp | generalComp | nodeComp ;

generalComp: EQUAL | NOT_EQUAL | LANGLE| (LANGLE EQUAL) | RANGLE | (RANGLE EQUAL) ;

valueComp: KW_EQ | KW_NE | KW_LT | KW_LE | KW_GT | KW_GE ;

nodeComp: KW_IS | (LANGLE LANGLE) | (RANGLE RANGLE) ;

// replaced with the enclosedExpression production to match the JSONiq grammar
// TODO: this is out of spec. However, it is currently kept to match the JSONiq grammar
// TODO: replace with the proper rule, throw excep.
// validateExpr: KW_VALIDATE (validationMode | (KW_TYPE typeName))? LBRACE expr? RBRACE ;
validateExpr: KW_VALIDATE (validationMode | (KW_TYPE sequenceType))? LBRACE expr? RBRACE ;

validationMode: KW_LAX | KW_STRICT ;

annotateExpr            : Kannotate KW_TYPE sequenceType LBRACE expr RBRACE;

extensionExpr: PRAGMA+ LBRACE expr RBRACE ;

simpleMapExpr: main_expr=pathExpr (BANG map_expr+=pathExpr)* ;

arrayLookup             : '[' '[' expr ']' ']';

arrayUnboxing           : '[' ']';

objectLookup            : DOT ( kw=keyword | lt=stringLiteral | nc=NCName | pe=parenthesizedExpr | vr=varRef | ci=contextItemExpr);

blockExpr : LBRACE statementsAndExpr RBRACE ;


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

pathExpr: (SLASH singleslash=relativePathExpr?) | (DSLASH doubleslash=relativePathExpr) | relative=relativePathExpr ;

relativePathExpr: stepExpr (sep+=(SLASH|DSLASH) stepExpr)* ;

stepExpr: postFixExpr | axisStep ;

axisStep: (reverseStep | forwardStep) predicateList ;

forwardStep: (forwardAxis nodeTest) | abbrevForwardStep ;

forwardAxis: ( KW_CHILD
             | KW_DESCENDANT
             | KW_ATTRIBUTE
             | KW_SELF
             | KW_DESCENDANT_OR_SELF
             | KW_FOLLOWING_SIBLING
             | KW_FOLLOWING ) COLON COLON ;

abbrevForwardStep: AT? nodeTest ;

reverseStep: (reverseAxis nodeTest) | abbrevReverseStep ;

reverseAxis: ( KW_PARENT
             | KW_ANCESTOR
             | KW_PRECEDING_SIBLING
             | KW_PRECEDING
             | KW_ANCESTOR_OR_SELF ) COLON COLON;

abbrevReverseStep: DDOT ;

nodeTest: nameTest | kindTest ;

nameTest: eqName | wildcard ;

wildcard: STAR            # allNames
        | nCNameWithLocalWildcard  # allWithNS    // walkers must strip out the trailing :*
        | nCNameWithPrefixWildcard # allWithLocal // walkers must strip out the leading *:
        | ( BracedURILiteral STAR )# BracedURILiteral
        ;
nCNameWithLocalWildcard :  NCName COLON STAR ;
nCNameWithPrefixWildcard: STAR COLON NCName ;

postFixExpr: main_expr=primaryExpr (arrayLookup | predicate | objectLookup | arrayUnboxing | argumentList | lookup)*;

argumentList: LPAREN (args+=argument (COMMA args+=argument)*)? RPAREN ;

predicateList: predicate*;

predicate: LBRACKET expr RBRACKET ;

lookup: QUESTION keySpecifier ;

// stringLiteral and varRef will be in XQuery 4.0
keySpecifier: (nc=ncName | in=IntegerLiteral | pe=parenthesizedExpr | wc=STAR | lt=stringLiteral | vr=varRef) ;

arrowFunctionSpecifier: eqName | varRef | parenthesizedExpr ;

primaryExpr: literal
           | NullLiteral
           | Ktrue
           | Kfalse
           | varRef
           | parenthesizedExpr
           | contextItemExpr
           | objectConstructor
           | functionCall
           | orderedExpr
           | unorderedExpr
           | nodeConstructor
           | functionItemExpr
           | objectConstructor
           | arrayConstructor
           | blockExpr
           ;

literal: numericLiteral | stringLiteral ;

numericLiteral: IntegerLiteral | DecimalLiteral | DoubleLiteral ;

varRef: DOLLAR var_name=eqName;

varName: eqName ;

parenthesizedExpr: LPAREN expr? RPAREN ;

contextItemExpr: DOUBLE_DOLLAR ;

orderedExpr: KW_ORDERED enclosedExpression ;

unorderedExpr: KW_UNORDERED enclosedExpression ;

functionCall: fn_name=functionName argumentList ;

argument: exprSingle | QUESTION ;

// CONSTRUCTORS ////////////////////////////////////////////////////////////////

nodeConstructor: directConstructor | computedConstructor ;

directConstructor: dirElemConstructorOpenClose
                 | dirElemConstructorSingleTag
                 | (COMMENT | PI)
                 ;

// [96]: we don't check that the closing tag is the same here. It should be
// done elsewhere, if we really want to know. We've also simplified the rule
// by removing the S? bits from ws:explicit. Tree walkers could handle this.
dirElemConstructorOpenClose: LANGLE open_tag_name=qname attributes=dirAttributeList endOpen=RANGLE
                             dirElemContent*
                             startClose=LANGLE slashClose=SLASH close_tag_name=qname RANGLE ;

dirElemConstructorSingleTag: LANGLE open_tag_name=qname attributes=dirAttributeList slashClose=SLASH RANGLE ;

// [97]: again, ws:explicit is better handled through the walker.
dirAttributeList: (attribute_qname+=qname EQUAL attribute_value+=dirAttributeValue)* ;

dirAttributeValueApos : Quot (PredefinedEntityRef | CharRef | EscapeQuot | dirAttributeContentQuot )* Quot ;
dirAttributeValueQuot : Apos (PredefinedEntityRef | CharRef | EscapeApos | dirAttributeContentApos )* Apos ; 

dirAttributeValue    : dirAttributeValueApos
                     | dirAttributeValueQuot
                     ;

dirAttributeContentQuot : contentChar                     
                        | dirAttributeValueApos
                        | LBRACE expr? RBRACE
                        ;

dirAttributeContentApos : contentChar                    
                        | dirAttributeValueQuot
                        | LBRACE expr? RBRACE
                        ;

// helper rule to match any content character
contentChar:              ContentChar+ ;

dirElemContent: directConstructor
              | commonContent
              | CDATA
              // ~[{}<&] = '" + ~['"{}<&]
              | Quot
              | Apos
              | noQuotesNoBracesNoAmpNoLAng
              ;

commonContent: (PredefinedEntityRef | CharRef) | LBRACE LBRACE | RBRACE RBRACE | (LBRACE expr? RBRACE) ;

computedConstructor: compDocConstructor
                   | compElemConstructor
                   | compAttrConstructor
                   | compNamespaceConstructor
                   | compTextConstructor
                   | compCommentConstructor
                   | compPIConstructor
                   ;

compDocConstructor: KW_DOCUMENT enclosedExpression ;

compElemConstructor: KW_ELEMENT ( eqName |(LBRACE expr RBRACE)) enclosedContentExpr ;

enclosedContentExpr: enclosedExpression ;

compAttrConstructor: KW_ATTRIBUTE (name=eqName | (LBRACE name_expr=expr RBRACE)) enclosedExpression ;

// replaced with the prefix production to allow the usage of the prefix label in the moduleImport rule
compNamespaceConstructor: KW_NAMESPACE (ncName | enclosedPrefixExpr) enclosedURIExpr ;

enclosedPrefixExpr: enclosedExpression ;

enclosedURIExpr: enclosedExpression ;

compTextConstructor: KW_TEXT enclosedExpression ;

compCommentConstructor: KW_COMMENT enclosedExpression ;

compPIConstructor: KW_PI (ncName | (LBRACE expr RBRACE)) enclosedExpression ;

functionItemExpr: namedFunctionRef | inlineFunctionExpr ;

// constrains to valid function names only
// see https://www.w3.org/TR/xquery-31/#parse-note-reserved-function-names
namedFunctionRef: fn_name=functionName HASH arity=IntegerLiteral ;

// renamed from inlineFunctionRef to inlineFunctionExpr to match the JSONiq grammar
// replaced with the functionBody production to match the JSONiq grammar
inlineFunctionExpr: annotations KW_FUNCTION LPAREN paramList? RPAREN (KW_AS return_type=sequenceType)? (LBRACE (fn_body=statementsAndOptionalExpr) RBRACE) ;

// renamed from mapConstructor to objectConstructor to match the JSONiq grammar
objectConstructor: LBRACE ( pairConstructor (COMMA pairConstructor)*)? RBRACE
                 | merge_operator+='{|' expr '|}';

// renamed from mapConstructorEntry to pairConstructor to match the JSONiq grammar
pairConstructor: lhs=exprSingle (COLON | COLON_EQ | QUESTION) rhs=exprSingle ;

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

documentTest: KW_DOCUMENT_node LPAREN (elementTest | schemaElementTest)? RPAREN ;

textTest: KW_TEXT LPAREN RPAREN ;

commentTest: KW_COMMENT LPAREN RPAREN ;

namespaceNodeTest: Knamespace_node LPAREN RPAREN ;

piTest: Kpi LPAREN (NCName | stringLiteral)? RPAREN ;

attributeTest: KW_ATTRIBUTE LPAREN (attributeNameOrWildcard (COMMA type=typeName)?)? RPAREN ;

attributeNameOrWildcard: attributeName | STAR ;

schemaAttributeTest: KW_SCHEMA_ATTRIBUTE LPAREN attributeDeclaration RPAREN ;

attributeDeclaration: attributeName ;

elementTest: KW_ELEMENT LPAREN (elementNameOrWildcard (COMMA type=typeName optional=QUESTION?)?)? RPAREN ;

elementNameOrWildcard: elementName | STAR ;

schemaElementTest: KW_SCHEMA_ELEMENT LPAREN elementDeclaration RPAREN ;

elementDeclaration: elementName ;

attributeName: qname ;

elementName: qname ;

simpleTypeName: typeName ;

typeName: qname;

///////////////////////// Types

typeDeclaration: KW_AS sequenceType ;

sequenceType            : LPAREN RPAREN
                        | item=itemType (question+=QUESTION | star+=STAR | plus+=PLUS)?;

itemType                : qname
                        | NullLiteral
                        | functionTest;

functionTest	        : (anyFunctionTest | typedFunctionTest);

anyFunctionTest         : KW_FUNCTION LPAREN STAR RPAREN;

typedFunctionTest	    : KW_FUNCTION LPAREN (st+=sequenceType (COMMA st+=sequenceType)*)? RPAREN 'as' rt=sequenceType;

singleType              : item=itemType (question +=QUESTION)?;

arrayConstructor        :  '[' expr? ']';

uriLiteral              : stringLiteral;

stringLiteral           : STRING;

keyword                : KW_JSONIQ
                        | KW_MODULE
                        | KW_NAMESPACE
                        | KW_AND
                        | KW_CAST
                        | KW_CASTABLE
                        | KW_COLLATION
                        | KW_CONTEXT
                        | KW_DECLARE
                        | KW_DEFAULT
                        | KW_ELSE
                        | KW_GREATEST
                        | Kinstance
                        | Kstatically
                        | KW_IS
                        | KW_ITEM
                        | KW_LEAST
                        | Knot
                        | NullLiteral
                        | Kof
                        | KW_OR
                        | KW_THEN
                        | KW_TO
                        | KW_TREAT
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
                        | KW_UNORDERED
                        | KW_FUNCTION
                        | KW_OPTION
                        | KW_EQ
                        | KW_NE
                        | KW_LT
                        | KW_LE
                        | KW_GT
                        | KW_GE
                        | Ktrue
                        | Kfalse
                        | KW_TYPE
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
                        | KW_VALIDATE
                        | Kannotate
                        | KW_BREAK
                        | KW_LOOP
                        | KW_CONTINUE
                        | KW_EXIT
                        | KW_RETURNING
                        | KW_WHILE
                        | KW_PI
                        | Kjson
                        | KW_TEXT
                        | KW_UPDATING
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
                        | KW_SCHEMA
                        | Knamespace
                        | KW_ELEMENT
                        | SLASH
                        | DSLASH
                        | AT
                        | KW_CHILD
                        | KW_DESCENDANT
                        | KW_ATTRIBUTE
                        | KW_SELF
                        | KW_DESCENDANT_OR_SELF
                        | KW_FOLLOWING_SIBLING
                        | KW_FOLLOWING
                        | KW_PARENT
                        | KW_ANCESTOR
                        | KW_PRECEDING_SIBLING
                        | KW_PRECEDING
                        | KW_ANCESTOR_OR_SELF
                        | Knode
                        | Kbinary
                        | KW_DOCUMENT
                        | KW_DOCUMENT_node
                        | Kpi
                        | Knamespace_node
                        | KW_SCHEMA_ATTRIBUTE
                        | KW_SCHEMA_ELEMENT
                        | Karray_node
                        | Kboolean_node
                        | Knull_node
                        | Knumber_node
                        | Kobject_node
                        | KW_COMMENT
                        ;

///////////////////////// literals

COMMA                   : ',';

COLON_EQ                : ':=';

EQUAL                   : '=';

NOT_EQUAL               : '!=';

SEMICOLON               : ';';

LPAREN                  : '(';

RPAREN                  : ')';

VBAR                    : '|';

LBRACE                    : '{';

RBRACE                    : '}';

LBRACKET                  : '[';
RBRACKET                  : ']';

LANGLE                    : '<';

RANGLE                    : '>';

MOD                       : '%';

STAR                  : '*';

HASH                  : '#';

CONCATENATION                  : '||';

BANG                  : '!';

COLON                  : ':';

DDOT                    : '..';

PLUS                    : '+';

MINUS                   : '-';

TILDE : '~';

DOT : '.';

GRAVE           : '`' ;
CARAT           : '^' ;
BACKSLASH       : '\\';

ARROW : '=>';

KW_DIV : 'div';
KW_MOD : 'mod';
KW_IDIV : 'idiv';
KW_UNION : 'union';
KW_INTERSECT : 'intersect';
KW_EXCEPT : 'except';

QUESTION                : '?';

COMMENT : 'commentee637daa-5838-4675-975a-782077b371b9';

PI : 'pi637daa-5838-4675-975a-782077b371b9';

Quot : 'quot637daa-5838-4675-975a-782077b371b9';

PredefinedEntityRef : 'predefineedentityref637daa-5838-4675-975a-782077b371b9';

CharRef : 'charref637daa-5838-4675-975a-782077b371b9';

EscapeQuot: 'escapequotref637daa-5838-4675-975a-782077b371b9';

Apos: 'aposref637daa-5838-4675-975a-782077b371b9';

EscapeApos: 'escapeaposref637daa-5838-4675-975a-782077b371b9';

CDATA: 'cdataref637daa-5838-4675-975a-782077b371b9';

PRAGMA: 'pragmaref637daa-5838-4675-975a-782077b371b9';
FullQName: 'fullqnameref637daa-5838-4675-975a-782077b371b9';
NCNameWithLocalWildcard: 'ncnamewithlocalwildcard637daa-5838-4675-975a-782077b371b9';
NCNameWithPrefixWildcard: 'ncnamewithprefixwildcardef637daa-5838-4675-975a-782077b371b9';

KW_FOR                    : 'for';

KW_LET                    : 'let';

KW_WHERE                  : 'where';

KW_GROUP                  : 'group';

KW_TUMBLING               : 'tumbling';

KW_WINDOW                 : 'window';

DOLLAR                    : '$';

DOUBLE_DOLLAR             : '$$';

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

KW_OR                     : 'or';

KW_EQ : 'eq';
KW_NE : 'ne';
KW_LT : 'lt';
KW_LE : 'le';
KW_GT : 'gt';
KW_GE : 'ge';

KW_PI : 'pi';

KW_AND                    : 'and';

Knot                    : 'not' ;

KW_TO                     : 'to' ;

Kinstance               : 'instance' ;

Kof                     : 'of' ;

Kstatically             : 'statically' ;

KW_IS                     : 'is' ;

KW_TREAT                  : 'treat';

KW_CAST                   : 'cast';

KW_CASTABLE               : 'castable';

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

KW_FUNCTION             : 'function';

KW_ELEMENT             : 'element';

KW_SCHEMA             : 'schema';

Ktrue                   : 'true';

Kfalse                  : 'false';

KW_TYPE                   : 'type';

KW_VALIDATE               : 'validate';

KW_LAX : 'lax';
KW_STRICT : 'strict';

Kannotate               : 'annotate';

KW_DECLARE                : 'declare';

KW_CONTEXT                : 'context';

KW_ITEM                   : 'item';

KW_VARIABLE               : 'variable';

KW_OPTION                : 'option';

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

KW_UPDATING               :  'updating';

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
Knamespace              : KW_NAMESPACE;
SLASH                  : '/';
DSLASH                 : '//';
AT              : '@';
KW_CHILD                  : 'child';
KW_DESCENDANT             : 'descendant';
KW_ATTRIBUTE              : 'attribute';
KW_SELF                   : 'self';
KW_DESCENDANT_OR_SELF     : 'descendant-or-self';
KW_FOLLOWING_SIBLING      : 'following-sibling';
KW_FOLLOWING              : 'following';
KW_PARENT                 : 'parent';
KW_ANCESTOR               : 'ancestor';
KW_PRECEDING_SIBLING      : 'preceding-sibling';
KW_PRECEDING              : 'preceding';
KW_ANCESTOR_OR_SELF       : 'ancestor-or-self';
Knode                   : 'node';
Kbinary                 : 'binary';
KW_DOCUMENT               : 'document';
KW_DOCUMENT_node          : 'document-node';
KW_TEXT                   : 'text';
Kpi                     : 'processing-instruction';
Knamespace_node         : 'namespace-node';
KW_SCHEMA_ATTRIBUTE       : 'schema-attribute';
KW_SCHEMA_ELEMENT         : 'schema-element';
Karray_node             : 'array-node';
Kboolean_node           : 'boolean-node';
Knull_node              : 'null-node';
Knumber_node            : 'number-node';
Kobject_node            : 'object-node';
KW_COMMENT                : 'comment';

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

NullLiteral             : 'null';

IntegerLiteral          : Digits ;

DecimalLiteral          : DOT Digits | Digits DOT [0-9]* ;

DoubleLiteral           : (DOT Digits | Digits (DOT [0-9]*)?) [eE] [+-]? Digits ;

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
                        | MINUS
                        // no . in JSONIQ names | DOT
                        | [0-9]
                        | '\u00B7'
                        | '\u0300'..'\u036F'
                        | '\u203F'..'\u2040'
                        ;

XQComment               : LPAREN COLON (XQComment | LPAREN ~[:] | COLON ~[)] | ~[:(])* COLON+ RPAREN -> channel(HIDDEN);

ContentChar             :  ~["'{}<&]  ;

noQuotesNoBracesNoAmpNoLAng:
                   ( keyword
                   | ( IntegerLiteral
                     | DecimalLiteral
                     | DoubleLiteral
                     //| stringLiteral
                     | PRAGMA
                     | EQUAL
                     | HASH
                     | NOT_EQUAL
                     | LPAREN
                     | RPAREN
                     | LBRACKET
                     | RBRACKET
                     | STAR
                     | PLUS
                     | MINUS
                     | TILDE
                     | COMMA
                     | ARROW
                     | KW_NEXT
                     | KW_PREVIOUS
                     | MOD
                     | DOT
                     | GRAVE
                     | DDOT
                     | COLON
                     | CARAT
                     | COLON_EQ
                     | SEMICOLON
                     | SLASH
                     | DSLASH
                     | BACKSLASH
                     | COMMENT
                     | VBAR
                     | RANGLE
                     | QUESTION
                     | AT
                     | DOLLAR
                     | BANG
                     | FullQName
                     | URIQualifiedName
                     | NCNameWithLocalWildcard
                     | NCNameWithPrefixWildcard
                     | NCName
                     | ContentChar
                     )
                   )+
 ;

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

catchCaseStatement      : KW_CATCH (jokers+=STAR | errors+=qname) (VBAR (jokers+=STAR | errors+=qname))* catch_block=blockStatement;

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
