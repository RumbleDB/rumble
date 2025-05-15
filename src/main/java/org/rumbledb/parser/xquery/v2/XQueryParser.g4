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
              (KW_NAMESPACE ncName EQUAL)?
              nsURI=uriLiteral
              (KW_AT locations+=uriLiteral (COMMA locations+=uriLiteral)*)? ;


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

functionDecl: KW_DECLARE (annotations|ncName) KW_FUNCTION name=eqName LPAREN functionParams? RPAREN
              functionReturn?
              ( functionBody | KW_EXTERNAL) ;

functionParams: functionParam (COMMA functionParam)* ;

functionParam: DOLLAR name=qName type=typeDeclaration? ;

annotations: annotation* ;

annotation: MOD qName (LPAREN annotList RPAREN)? ;

annotList: annotationParam ( COMMA annotationParam )* ;

annotationParam: literal ;

functionReturn: KW_AS sequenceType ;

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

flworExpr: initialClause intermediateClause* returnClause ;

initialClause: forClause | letClause | windowClause ;
intermediateClause: initialClause
                  | whereClause
                  | groupByClause
                  | orderByClause
                  | countClause
                  ;

forClause: KW_FOR vars+=forBinding (COMMA vars+=forBinding)* ;

forBinding: DOLLAR name=varName type=typeDeclaration? allowingEmpty? positionalVar?
        KW_IN in=exprSingle ;

allowingEmpty: KW_ALLOWING KW_EMPTY;

positionalVar: KW_AT DOLLAR pvar=varName ;

letClause: KW_LET vars+=letBinding (COMMA vars+=letBinding)* ;

letBinding: DOLLAR varName typeDeclaration? COLON_EQ exprSingle ;

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

groupByClause: KW_GROUP KW_BY groupingSpecList ;

groupingSpecList: groupingSpec (COMMA groupingSpec)* ;

groupingSpec: DOLLAR name=varName
                    (type=typeDeclaration? COLON_EQ exprSingle)?
                    (KW_COLLATION uri=uriLiteral)? ;

orderByClause: stb=KW_STABLE? KW_ORDER KW_BY specs+=orderSpec (COMMA specs+=orderSpec)* ;

orderSpec: ex=exprSingle
           (KW_ASCENDING | desc=KW_DESCENDING)?
           (KW_EMPTY (gr=KW_GREATEST|ls=KW_LEAST))?
           (KW_COLLATION uril=uriLiteral)?
         ;

returnClause: KW_RETURN exprSingle ;

quantifiedExpr: quantifier=(KW_SOME | KW_EVERY) quantifiedVar (COMMA quantifiedVar)*
                KW_SATISFIES value=exprSingle ;

quantifiedVar: DOLLAR varName typeDeclaration? KW_IN exprSingle ;

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

multiplicativeExpr: unionExpr ( (STAR | KW_DIV | KW_IDIV | KW_MOD) unionExpr )* ;

unionExpr: intersectExceptExpr ( (KW_UNION | VBAR) intersectExceptExpr)* ;

intersectExceptExpr: instanceOfExpr ( (KW_INTERSECT | KW_EXCEPT) instanceOfExpr)* ;

instanceOfExpr: treatExpr ( KW_INSTANCE KW_OF sequenceType)? ;

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

keySpecifier: ncName | IntegerLiteral | parenthesizedExpr | STAR ;

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
                   | compMLJSONConstructor
                   ;

compMLJSONConstructor: compMLJSONArrayConstructor
                     | compMLJSONObjectConstructor
                     | compMLJSONNumberConstructor
                     | compMLJSONBooleanConstructor
                     | compMLJSONNullConstructor
                     | compBinaryConstructor
                     ;

compMLJSONArrayConstructor: KW_ARRAY_NODE enclosedContentExpr ;
compMLJSONObjectConstructor: KW_OBJECT_NODE LBRACE (exprSingle COLON exprSingle (COMMA exprSingle COLON exprSingle)*)? RBRACE ;
compMLJSONNumberConstructor: KW_NUMBER_NODE enclosedContentExpr ;
compMLJSONBooleanConstructor: KW_BOOLEAN_NODE LBRACE exprSingle RBRACE ;
compMLJSONNullConstructor: KW_NULL_NODE LBRACE RBRACE ;

compBinaryConstructor: KW_BINARY enclosedContentExpr ;


compDocConstructor: KW_DOCUMENT enclosedExpression ;

compElemConstructor: KW_ELEMENT ( eqName |(LBRACE expr RBRACE)) enclosedContentExpr ;

enclosedContentExpr: enclosedExpression ;

compAttrConstructor: KW_ATTRIBUTE (eqName | (LBRACE expr RBRACE)) enclosedExpression ;

compNamespaceConstructor: KW_NAMESPACE (prefix | enclosedPrefixExpr) enclosedURIExpr ;

prefix: ncName ;

enclosedPrefixExpr: enclosedExpression ;

enclosedURIExpr: enclosedExpression ;

compTextConstructor: KW_TEXT enclosedExpression ;

compCommentConstructor: KW_COMMENT enclosedExpression ;

compPIConstructor: KW_PI (ncName | (LBRACE expr RBRACE)) enclosedExpression ;

functionItemExpr: namedFunctionRef | inlineFunctionRef ;

namedFunctionRef: fn_name=eqName HASH arity=IntegerLiteral ;

inlineFunctionRef: annotations KW_FUNCTION LPAREN functionParams? RPAREN (KW_AS sequenceType)? functionBody ;

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

singleType: simpleTypeName QUESTION? ;

typeDeclaration: KW_AS sequenceType ;

sequenceType: (KW_EMPTY_SEQUENCE LPAREN RPAREN) | (item=itemType (question+=QUESTION|star+=STAR|plus+=PLUS)? );

itemType: kindTest
        | (KW_ITEM LPAREN RPAREN)
        | functionTest
        | mapTest
        | arrayTest
        | atomicOrUnionType
        | parenthesizedItemTest ;

atomicOrUnionType: eqName ;

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