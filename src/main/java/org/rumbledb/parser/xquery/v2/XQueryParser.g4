parser grammar XQueryParser;
options {
  tokenVocab=XQueryLexer;
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

module : versionDecl? (libraryModule | (mainModule (SEMICOLON versionDecl? mainModule)* )) ;

versionDecl: KW_XQUERY KW_VERSION version=stringLiteral
             (KW_ENCODING encoding=stringLiteral)?
             SEMICOLON ;

mainModule: prolog queryBody;

queryBody: expr ;

libraryModule: KW_MODULE KW_NAMESPACE ncName EQUAL uri=stringLiteral SEMICOLON prolog;

// MODULE PROLOG ///////////////////////////////////////////////////////////////

prolog: ((defaultNamespaceDecl | setter | namespaceDecl | schemaImport | moduleImport) SEMICOLON)*
        ( (varDecl | functionDecl | contextItemDecl | optionDecl) SEMICOLON)* ;

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
emptyOrderDecl: KW_DECLARE KW_DEFAULT KW_ORDER KW_EMPTY type=(KW_GREATEST | KW_LEAST) ;
copyNamespacesDecl: KW_DECLARE KW_COPY_NS preserveMode COMMA inheritMode ;
preserveMode: KW_PRESERVE | KW_NO_PRESERVE ;
inheritMode: KW_INHERIT | KW_NO_INHERIT ;
decimalFormatDecl: KW_DECLARE (
                      (KW_DECIMAL_FORMAT eqName)
                    | (KW_DEFAULT KW_DECIMAL_FORMAT)
                   )
                   (DFPropertyName EQUAL stringLiteral)*;


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

varDecl: KW_DECLARE (annotations|ncName) KW_VARIABLE DOLLAR varName typeDeclaration?
         (
            (COLON_EQ varValue)
          | (KW_EXTERNAL (COLON_EQ varDefaultValue)?)
          | (LBRACE varValue RBRACE)
          | (KW_EXTERNAL(LBRACE varDefaultValue RBRACE)?)
         ) ;

varValue: expr ;

varDefaultValue: expr ;

contextItemDecl: KW_DECLARE KW_CONTEXT KW_ITEM
                 (KW_AS itemType)?
                 ((COLON_EQ value=exprSingle)
                 | (KW_EXTERNAL (COLON_EQ defaultValue=exprSingle)?)) ;

functionDecl: KW_DECLARE (annotations|ncName) KW_FUNCTION fn_name=eqName LPAREN paramList? RPAREN
              // replaced with the functionReturn production to match the JSONiq grammar
              (KW_AS return_type=sequenceType)?
              // replaced functionBody to match the JSONiq grammar and the XQuery Scripting Extension spec
              ( LBRACE (fn_body=statementsAndOptionalExpr) RBRACE | is_external=KW_EXTERNAL) ;

// renamed from functionParams to paramList to match the JSONiq grammar
paramList: param (COMMA param)* ;

// renamed from functionParam to param to match the JSONiq grammar
// replaced with the typeDeclaration production to match the JSONiq grammar
param: DOLLAR name=qName (KW_AS sequenceType)? ;

annotations: annotation* ;

annotation: MOD qName (LPAREN annotList RPAREN)? ;

annotList: annotationParam ( COMMA annotationParam )* ;

annotationParam: literal ;

optionDecl: KW_DECLARE KW_OPTION name=qName value=stringLiteral ;


// EXPRESSIONS /////////////////////////////////////////////////////////////////

expr: exprSingle (COMMA exprSingle)* ;

exprSingle: flworExpr
          | quantifiedExpr
          | switchExpr
          | typeswitchExpr
          | existUpdateExpr
          | ifExpr
          | tryCatchExpr
          | orExpr
          ;

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

tumblingWindowClause: KW_TUMBLING KW_WINDOW DOLLAR name=qName
                          type=typeDeclaration? KW_IN exprSingle
                          windowStartCondition windowEndCondition? ;

slidingWindowClause: KW_SLIDING KW_WINDOW DOLLAR name=qName
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

orderByClause: stb=KW_STABLE? KW_ORDER KW_BY specs+=orderSpec (COMMA specs+=orderSpec)* ;

orderSpec: ex=exprSingle
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

tryCatchExpr: tryClause catchClause+ ;
tryClause: KW_TRY enclosedTryTargetExpression ;
enclosedTryTargetExpression: enclosedExpression ;
catchClause: KW_CATCH (catchErrorList | (LPAREN DOLLAR varName RPAREN)) enclosedExpression ;
enclosedExpression: LBRACE expr? RBRACE ;

catchErrorList: nameTest (VBAR nameTest)* ;


existUpdateExpr: KW_UPDATE ( existReplaceExpr | existValueExpr | existInsertExpr | existDeleteExpr | existRenameExpr ) ;

existReplaceExpr: KW_REPLACE expr KW_WITH exprSingle ;
existValueExpr: KW_VALUE expr KW_WITH exprSingle ;
existInsertExpr: KW_INSERT exprSingle (KW_INTO | KW_PRECEDING | KW_FOLLOWING) exprSingle;
existDeleteExpr: KW_DELETE exprSingle;
existRenameExpr: KW_RENAME exprSingle KW_AS exprSingle;

orExpr: main_expr=andExpr (KW_OR rhs+=andExpr)* ;

andExpr: main_expr=comparisonExpr (KW_AND rhs+=comparisonExpr)* ;

comparisonExpr: main_expr=stringConcatExpr (  op+=compOp rhs+=stringConcatExpr )? ;

stringConcatExpr: main_expr=rangeExpr (CONCATENATION rhs+=rangeExpr)* ;

rangeExpr: main_expr=additiveExpr (KW_TO rhs+=additiveExpr)? ;

additiveExpr: main_expr=multiplicativeExpr ( op+=(PLUS | MINUS) rhs+=multiplicativeExpr )* ;

multiplicativeExpr: main_expr=unionExpr ( op+=(STAR | KW_DIV | KW_IDIV | KW_MOD) rhs+=unionExpr )* ;

unionExpr: intersectExceptExpr ( (KW_UNION | VBAR) intersectExceptExpr)* ;

intersectExceptExpr: instanceOfExpr ( (KW_INTERSECT | KW_EXCEPT) instanceOfExpr)* ;

instanceOfExpr: main_expr=treatExpr ( KW_INSTANCE KW_OF seq=sequenceType)? ;

treatExpr: main_expr=castableExpr ( KW_TREAT KW_AS seq=sequenceType)? ;

castableExpr: main_expr=castExpr ( KW_CASTABLE KW_AS single=singleType)?;

castExpr: main_expr=arrowExpr (KW_CAST KW_AS single=singleType)? ;

arrowExpr: main_expr=unaryExpr (ARROW function+=arrowFunctionSpecifier arguments+=argumentList)* ;

unaryExpr: (MINUS | PLUS)* main_expr=valueExpr ;

valueExpr: validate_expr=validateExpr | extensionExpr | simpleMap_expr=simpleMapExpr ;

/* 
 * this token was added to prevent the antlr error
 * "label assigned to a block which is not a set" in the comparisonExpr token definition
 */
compOp: valueComp | generalComp | nodeComp ;

generalComp: EQUAL | NOT_EQUAL | LANGLE| (LANGLE EQUAL) | RANGLE | (RANGLE EQUAL) ;

valueComp: KW_EQ | KW_NE | KW_LT | KW_LE | KW_GT | KW_GE ;

nodeComp: KW_IS | (LANGLE LANGLE) | (RANGLE RANGLE) ;

validateExpr: KW_VALIDATE ( validationMode | ( ( KW_TYPE | KW_AS ) typeName) )? enclosedExpression ;

validationMode: KW_LAX | KW_STRICT ;

extensionExpr: PRAGMA+ LBRACE expr RBRACE ;

simpleMapExpr: main_expr=pathExpr (BANG map_expr+=pathExpr)* ;

// PATHS ///////////////////////////////////////////////////////////////////////

pathExpr: (SLASH singleslash=relativePathExpr?) | (DSLASH doubleslash=relativePathExpr) | relative=relativePathExpr ;

relativePathExpr: stepExpr (sep=(SLASH|DSLASH) stepExpr)* ;

stepExpr: postfixExpr | axisStep ;

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
        | NCNameWithLocalWildcard  # allWithNS    // walkers must strip out the trailing :*
        | NCNameWithPrefixWildcard # allWithLocal // walkers must strip out the leading *:
        ;


postfixExpr: main_expr=primaryExpr (predicate | argumentList | lookup)* ;

argumentList: LPAREN (args+=argument (COMMA args+=argument)*)? RPAREN ;

predicateList: predicate*;

predicate: LBRACKET expr RBRACKET ;

lookup: QUESTION keySpecifier ;

// stringLiteral and varRef will be in XQuery 4.0
keySpecifier: (nc=ncName | in=IntegerLiteral | pe=parenthesizedExpr | wc=STAR | lt=stringLiteral | vr=varRef) ;

arrowFunctionSpecifier: eqName | varRef | parenthesizedExpr ;

primaryExpr: literal
           | varRef
           | parenthesizedExpr
           | contextItemExpr
           | functionCall
           | orderedExpr
           | unorderedExpr
           | nodeConstructor
           | functionItemExpr
           | mapConstructor
           | arrayConstructor
           | stringConstructor
           | unaryLookup
           ;

literal: numericLiteral | stringLiteral ;

numericLiteral: IntegerLiteral | DecimalLiteral | DoubleLiteral ;

varRef: DOLLAR var_name=eqName;

varName: eqName ;

parenthesizedExpr: LPAREN expr? RPAREN ;

contextItemExpr: DOT ;

orderedExpr: KW_ORDERED enclosedExpression ;

unorderedExpr: KW_UNORDERED enclosedExpression ;

functionCall: fn_name=eqName argumentList  ;

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
dirElemConstructorOpenClose: LANGLE open_tag_name=qName dirAttributeList endOpen=RANGLE
                             dirElemContent*
                             startClose=LANGLE slashClose=SLASH close_tag_name=qName RANGLE ;

dirElemConstructorSingleTag: LANGLE open_tag_name=qName dirAttributeList slashClose=SLASH RANGLE ;

// [97]: again, ws:explicit is better handled through the walker.
dirAttributeList: (qName EQUAL dirAttributeValue)* ;

dirAttributeValueApos : Quot (PredefinedEntityRef | CharRef | EscapeQuot | dirAttributeContentQuot )* Quot ;
dirAttributeValueQuot : Apos (PredefinedEntityRef | CharRef | EscapeApos | dirAttributeContentApos )* Apos ; 

dirAttributeValue    : dirAttributeValueApos
                     | dirAttributeValueQuot
                     ;

dirAttributeContentQuot : ContentChar+                     
                        | DOUBLE_LBRACE | DOUBLE_RBRACE
                        | dirAttributeValueApos
                        | LBRACE expr? RBRACE
                        ;

dirAttributeContentApos : ContentChar+                     
                        | DOUBLE_LBRACE | DOUBLE_RBRACE
                        | dirAttributeValueQuot
                        | LBRACE expr? RBRACE
                        ;                     

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

compAttrConstructor: KW_ATTRIBUTE (eqName | (LBRACE expr RBRACE)) enclosedExpression ;

// replaced with the prefix production to allow the usage of the prefix label in the moduleImport rule
compNamespaceConstructor: KW_NAMESPACE (ncName | enclosedPrefixExpr) enclosedURIExpr ;

enclosedPrefixExpr: enclosedExpression ;

enclosedURIExpr: enclosedExpression ;

compTextConstructor: KW_TEXT enclosedExpression ;

compCommentConstructor: KW_COMMENT enclosedExpression ;

compPIConstructor: KW_PI (ncName | (LBRACE expr RBRACE)) enclosedExpression ;

functionItemExpr: namedFunctionRef | inlineFunctionRef ;

namedFunctionRef: fn_name=eqName HASH arity=IntegerLiteral ;

inlineFunctionRef: annotations KW_FUNCTION LPAREN paramList? RPAREN (KW_AS sequenceType)? functionBody ;

functionBody: enclosedExpression ;

mapConstructor: KW_MAP LBRACE (mapConstructorEntry (COMMA mapConstructorEntry)*)? RBRACE ;

mapConstructorEntry: mapKey=exprSingle (COLON | COLON_EQ) mapValue=exprSingle ;

arrayConstructor: squareArrayConstructor | curlyArrayConstructor ;

squareArrayConstructor: LBRACKET (exprSingle (COMMA exprSingle)*)? RBRACKET ;

curlyArrayConstructor: KW_ARRAY enclosedExpression ;

stringConstructor: ENTER_STRING stringConstructorContent EXIT_STRING;

stringConstructorContent: stringConstructorChars (stringConstructorInterpolation stringConstructorChars)* ;

charNoGrave           : BASIC_CHAR | LBRACE | RBRACKET;
charNoLBrace          : BASIC_CHAR | GRAVE | RBRACKET;
charNoRBrack          : BASIC_CHAR | GRAVE | LBRACE;
stringConstructorChars: (BASIC_CHAR 
                            | charNoGrave charNoLBrace
                            | charNoRBrack charNoGrave charNoGrave
                            | charNoGrave
                            | LBRACE                                                      
                            )* ;

stringConstructorInterpolation: ENTER_INTERPOLATION expr EXIT_INTERPOLATION ;

unaryLookup: QUESTION keySpecifier ;

// TYPES AND TYPE TESTS ////////////////////////////////////////////////////////

singleType: item=simpleTypeName (question+=QUESTION)? ;

typeDeclaration: KW_AS sequenceType ;

sequenceType: (KW_EMPTY_SEQUENCE LPAREN RPAREN) | (item=itemType (question+=QUESTION|star+=STAR|plus+=PLUS)? );

itemType: kindTest
        | (KW_ITEM LPAREN RPAREN)
        | functionTest
        | mapTest
        | arrayTest
        // simplification compared to XQuery 3.1 grammar
        // removes the need for a separate atomicOrUnionType rule
        | eqName
        | parenthesizedItemTest ;

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

anyKindTest: KW_NODE LPAREN STAR? RPAREN ;

binaryNodeTest: KW_BINARY LPAREN RPAREN ;

documentTest: KW_DOCUMENT_NODE LPAREN (elementTest | schemaElementTest)? RPAREN ;

textTest: KW_TEXT LPAREN RPAREN ;

commentTest: KW_COMMENT LPAREN RPAREN ;

namespaceNodeTest: KW_NAMESPACE_NODE LPAREN RPAREN ;

piTest: KW_PI LPAREN (ncName | stringLiteral)? RPAREN ;

attributeTest: KW_ATTRIBUTE LPAREN (attributeNameOrWildcard (COMMA type=typeName)?)? RPAREN ;

attributeNameOrWildcard: attributeName | STAR ;

schemaAttributeTest: KW_SCHEMA_ATTR LPAREN attributeDeclaration RPAREN ;

elementTest: KW_ELEMENT LPAREN (elementNameOrWildcard (COMMA type=typeName optional=QUESTION?)?)? RPAREN ;

elementNameOrWildcard: elementName | STAR ;

schemaElementTest: KW_SCHEMA_ELEM LPAREN elementDeclaration RPAREN ;

elementDeclaration: elementName ;

attributeName: eqName ;

elementName: eqName ;

simpleTypeName: typeName ;

typeName: eqName;

functionTest: annotation* (anyFunctionTest | typedFunctionTest) ;

anyFunctionTest: KW_FUNCTION LPAREN STAR RPAREN ;

typedFunctionTest: KW_FUNCTION LPAREN (st+=sequenceType (COMMA st+=sequenceType)*)? RPAREN KW_AS rt=sequenceType ;

mapTest: anyMapTest | typedMapTest ;

anyMapTest: KW_MAP LPAREN STAR RPAREN ;

typedMapTest: KW_MAP LPAREN eqName COMMA sequenceType RPAREN ;

arrayTest: anyArrayTest | typedArrayTest ;

anyArrayTest: KW_ARRAY LPAREN STAR RPAREN ;

typedArrayTest: KW_ARRAY LPAREN sequenceType RPAREN ;

parenthesizedItemTest: LPAREN itemType RPAREN ;

attributeDeclaration: attributeName ;




mlNodeTest: mlArrayNodeTest
          | mlObjectNodeTest
          | mlNumberNodeTest
          | mlBooleanNodeTest
          | mlNullNodeTest
          ;

mlArrayNodeTest: KW_ARRAY_NODE LPAREN stringLiteral? RPAREN ;

mlObjectNodeTest: KW_OBJECT_NODE LPAREN stringLiteral? RPAREN ;

mlNumberNodeTest: KW_NUMBER_NODE LPAREN stringLiteral? RPAREN ;

mlBooleanNodeTest: KW_BOOLEAN_NODE LPAREN stringLiteral? RPAREN ;

mlNullNodeTest: KW_NULL_NODE LPAREN stringLiteral? RPAREN ;

// NAMES ///////////////////////////////////////////////////////////////////////

// walkers need to split into prefix+localpart by the ':'
eqName: qName | URIQualifiedName ;

qName: FullQName | ncName ;


ncName: NCName | keyword ;

functionName: FullQName | NCName | URIQualifiedName | keywordOKForFunction ;

keyword: keywordOKForFunction | keywordNotOKForFunction ;

keywordNotOKForFunction:
         KW_ATTRIBUTE
       | KW_COMMENT
       | KW_DOCUMENT_NODE
       | KW_ELEMENT
       | KW_EMPTY_SEQUENCE
       | KW_IF
       | KW_ITEM
       | KW_CONTEXT
       | KW_NODE
       | KW_PI
       | KW_SCHEMA_ATTR
       | KW_SCHEMA_ELEM
       | KW_BINARY
       | KW_TEXT
       | KW_TYPESWITCH
       | KW_SWITCH
       | KW_NAMESPACE_NODE
       | KW_TYPE
       | KW_TUMBLING
       | KW_TRY
       | KW_CATCH
       | KW_ONLY
       | KW_WHEN
       | KW_SLIDING
       | KW_DECIMAL_FORMAT
       | KW_WINDOW
       | KW_COUNT
       | KW_MAP
       | KW_END
       | KW_ALLOWING
       | KW_ARRAY
       | DFPropertyName
// MarkLogic JSON computed constructor
       | KW_ARRAY_NODE
       | KW_BOOLEAN_NODE
       | KW_NULL_NODE
       | KW_NUMBER_NODE
       | KW_OBJECT_NODE
// eXist-db update keywords
       | KW_UPDATE
       | KW_REPLACE
       | KW_WITH
       | KW_VALUE
       | KW_INSERT
       | KW_INTO
       | KW_DELETE
       | KW_NEXT
       | KW_RENAME
       ;

keywordOKForFunction: KW_ANCESTOR
       | KW_ANCESTOR_OR_SELF
       | KW_AND
       | KW_AS
       | KW_ASCENDING
       | KW_AT
       | KW_BASE_URI
       | KW_BOUNDARY_SPACE
       | KW_BY
       | KW_CASE
       | KW_CAST
       | KW_CASTABLE
       | KW_CHILD
       | KW_COLLATION
       | KW_CONSTRUCTION
       | KW_COPY_NS
       | KW_DECLARE
       | KW_DEFAULT
       | KW_DESCENDANT
       | KW_DESCENDANT_OR_SELF
       | KW_DESCENDING
       | KW_DIV
       | KW_DOCUMENT
       | KW_ELSE
       | KW_EMPTY
       | KW_ENCODING
       | KW_EQ
       | KW_EVERY
       | KW_EXCEPT
       | KW_EXTERNAL
       | KW_FOLLOWING
       | KW_FOLLOWING_SIBLING
       | KW_FOR
       | KW_FUNCTION
       | KW_GE
       | KW_GREATEST
       | KW_GROUP
       | KW_GT
       | KW_IDIV
       | KW_IMPORT
       | KW_IN
       | KW_INHERIT
       | KW_INSTANCE
       | KW_INTERSECT
       | KW_IS
       | KW_LAX
       | KW_LE
       | KW_LEAST
       | KW_LET
       | KW_LT
       | KW_MOD
       | KW_MODULE
       | KW_NAMESPACE
       | KW_NE
       | KW_NO_INHERIT
       | KW_NO_PRESERVE
       | KW_OF
       | KW_OPTION
       | KW_OR
       | KW_ORDER
       | KW_ORDERED
       | KW_ORDERING
       | KW_PARENT
       | KW_PRECEDING
       | KW_PRECEDING_SIBLING
       | KW_PRESERVE
       | KW_RETURN
       | KW_SATISFIES
       | KW_SCHEMA
       | KW_SELF
       | KW_SOME
       | KW_STABLE
       | KW_START
       | KW_STRICT
       | KW_STRIP
       | KW_THEN
       | KW_TO
       | KW_TREAT
       | KW_UNION
       | KW_UNORDERED
       | KW_VALIDATE
       | KW_VARIABLE
       | KW_VERSION
       | KW_WHERE
       | KW_XQUERY
       ;

// STRING LITERALS /////////////////////////////////////////////////////////////

uriLiteral: stringLiteral ;

stringLiteralQuot : Quot (PredefinedEntityRef | CharRef | EscapeQuot | stringContentQuot )* Quot ;
stringLiteralApos : Apos (PredefinedEntityRef | CharRef | EscapeApos | stringContentApos )* Apos ;

stringLiteral : stringLiteralQuot
              | stringLiteralApos
              ;

stringContentQuot : ContentChar+
                  | LBRACE expr? RBRACE?
                  | RBRACE
                  | DOUBLE_LBRACE
                  | DOUBLE_RBRACE
                  | noQuotesNoBracesNoAmpNoLAng                  
                  | stringLiteralApos
                  ;

stringContentApos : ContentChar+
                  | LBRACE expr? RBRACE?
                  | RBRACE
                  | DOUBLE_LBRACE
                  | DOUBLE_RBRACE
                  | noQuotesNoBracesNoAmpNoLAng                  
                  | stringLiteralQuot
                  ;

// ~['"{}<&]: a very common (and long!) subexpression in the W3C EBNF grammar //

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
                     | XQDOC_COMMENT_START
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

statement               : applyStatement
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

applyStatement          : exprSimple SEMICOLON ;

assignStatement         : DOLLAR varName COLON_EQ exprSingle SEMICOLON ;

blockStatement          : LBRACE statements RBRACE ;

breakStatement          : KW_BREAK KW_LOOP SEMICOLON ;

continueStatement       : KW_CONTINUE KW_LOOP SEMICOLON ;

exitStatement           : KW_EXIT KW_RETURNING exprSingle SEMICOLON ;

// replaced with the initialClause production to match the JSONiq grammar
flowrStatement          : (start_for=forClause| start_let=letClause)
                        // replaced with the intermediateClause production to match the JSONiq grammar
                          (forClause | letClause | whereClause | groupByClause | orderByClause | countClause)*
                        // replaced with the returnStatement production to match the JSONiq grammar
                          KW_RETURN returnStmt=statement ;

ifStatement             :  KW_IF LPAREN test_expr=expr RPAREN
                           KW_THEN branch=statement
                           KW_ELSE else_branch=statement ;

switchStatement         : KW_SWITCH LPAREN condExpr=expr RPAREN cases+=switchCaseStatement+ KW_DEFAULT KW_RETURN def=statement ;

// replaced with the switchCaseOperand production to match the JSONiq grammar
switchCaseStatement     : (KW_CASE cond+=exprSingle)+ KW_RETURN ret=statement ;

tryCatchStatement       : KW_TRY try_block=blockStatement catches+=catchCaseStatement+ ;

// added to match the JSONiq grammar
// replaced the CatchErrorList production rule to match the JSONiq grammar
catchCaseStatement      : KW_CATCH (jokers+=wildcard | errors+=eqName) (VBAR (jokers+=wildcard | errors+=eqName))* catch_block=blockStatement;

// replaced "$" varName with varRef to match the JSONiq grammar
typeSwitchStatement     : KW_TYPESWITCH LPAREN cond=expr RPAREN cases+=caseStatement+ KW_DEFAULT (var_ref=varRef)? (KW_RETURN | KW_SELECT) def=statement ;

// replaced "$" varName with varRef to match the JSONiq grammar
caseStatement           : KW_CASE (var_ref=varRef KW_AS)? union+=sequenceType (VBAR union+=sequenceType)* (KW_RETURN | KW_SELECT) ret=statement ;

varDeclStatement        : annotations KW_VARIABLE varDeclForStatement (COMMA varDeclForStatement)* SEMICOLON ;

// added to match the JSONiq grammar
varDeclForStatement     : var_ref=varRef (KW_AS sequenceType)? (COLON_EQ expr_vals+=exprSingle)? ;

whileStatement          : KW_WHILE LPAREN test_expr=expr RPAREN stmt=statement ;
