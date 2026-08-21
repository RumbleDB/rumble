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
 * Shared parser rules for the JSONiq and XQuery grammars.
 * This file is based on the XQuery parser grammar from the xqdoc project:
 * https://github.com/xqdoc/xqdoc/blob/master/src/main/antlr4/org/xqdoc/XQueryParser.g4
 * 
 * See LICENSE-xqdoc.txt for the original license terms.
 * 
 */
parser grammar SharedParser;

moduleAndThisIsIt
   : module EOF
   ;

libraryModule
   : KW_MODULE KW_NAMESPACE ncName EQUAL uri = uriLiteral SEMICOLON prolog
   ;

prolog
   : (defaultNamespaceDecl | setter | namespaceDecl | schemaImport | moduleImport)* (annotatedDecl)*
   ;

defaultNamespaceDecl
   : KW_DECLARE KW_DEFAULT type = (KW_ELEMENT | KW_FUNCTION) KW_NAMESPACE uri = stringLiteral SEMICOLON
   ;

setter
   : boundarySpaceDecl
   | defaultCollationDecl
   | baseURIDecl
   | constructionDecl
   | orderingModeDecl
   | emptyOrderDecl
   | copyNamespacesDecl
   | decimalFormatDecl
   ;

boundarySpaceDecl
   : KW_DECLARE KW_BOUNDARY_SPACE type = (KW_PRESERVE | KW_STRIP) SEMICOLON
   ;

defaultCollationDecl
   : KW_DECLARE KW_DEFAULT KW_COLLATION uriLiteral SEMICOLON
   ;

baseURIDecl
   : KW_DECLARE KW_BASE_URI uriLiteral SEMICOLON
   ;

constructionDecl
   : KW_DECLARE KW_CONSTRUCTION type = (KW_STRIP | KW_PRESERVE) SEMICOLON
   ;

orderingModeDecl
   : KW_DECLARE KW_ORDERING type = (KW_ORDERED | KW_UNORDERED) SEMICOLON
   ;

emptyOrderDecl
   : KW_DECLARE KW_DEFAULT KW_ORDER KW_EMPTY emptySequenceOrder = (KW_GREATEST | KW_LEAST) SEMICOLON
   ;

copyNamespacesDecl
   : KW_DECLARE KW_COPY_NS preserveMode COMMA inheritMode SEMICOLON
   ;

preserveMode
   : KW_PRESERVE
   | KW_NO_PRESERVE
   ;

inheritMode
   : KW_INHERIT
   | KW_NO_INHERIT
   ;

decimalFormatDecl
   : KW_DECLARE ((KW_DECIMAL_FORMAT eqName) | (KW_DEFAULT KW_DECIMAL_FORMAT)) (DFPropertyName EQUAL stringLiteral)* SEMICOLON
   ;

schemaImport
   : KW_IMPORT KW_SCHEMA schemaPrefix? nsURI = uriLiteral (KW_AT locations += uriLiteral (COMMA locations += uriLiteral)*)? SEMICOLON
   ;

schemaPrefix
   : (KW_NAMESPACE ncName EQUAL | KW_DEFAULT KW_ELEMENT KW_NAMESPACE)
   ;

moduleImport
   : KW_IMPORT KW_MODULE (KW_NAMESPACE prefix = ncName EQUAL)? targetNamespace = uriLiteral (KW_AT locations += uriLiteral (COMMA locations += uriLiteral)*)? SEMICOLON
   ;

namespaceDecl
   : KW_DECLARE KW_NAMESPACE ncName EQUAL uriLiteral SEMICOLON
   ;

varDecl
   : KW_DECLARE (annotations | ncName) KW_VARIABLE varBinding
   // replaced with the typeDeclaration production to match the JSONiq grammar
   (KW_AS sequenceType)? (
   // replaced with the varValue production to match the JSONiq grammar
   (COLON_EQ exprSingle)
   // replaced with the varDefaultValue production to match the JSONiq grammar
   | (external = KW_EXTERNAL (COLON_EQ exprSingle)?)) SEMICOLON
   ;

contextItemDecl
   : KW_DECLARE KW_CONTEXT KW_ITEM
/*
 * (KW_AS itemType)?
 * TODO: this is out of spec. However, it is currently kept to match the JSONiq grammar
 */
   
   (KW_AS sequenceType)? // TODO: change to itemType, update expressions to use itemType, update back JSONiq grammar
   ((COLON_EQ value = exprSingle) | (external = KW_EXTERNAL (COLON_EQ defaultValue = exprSingle)?)) SEMICOLON
   ;

functionDecl
   : KW_DECLARE (annotations) KW_FUNCTION fn_name = functionName LPAREN paramList? RPAREN
   // replaced with the functionReturn production to match the JSONiq grammar
   (KW_AS return_type = sequenceType)?
   // replaced functionBody to match the JSONiq grammar and the XQuery Scripting Extension spec
   (LBRACE (fn_body = statementsAndOptionalExpr) RBRACE | is_external = KW_EXTERNAL) SEMICOLON
   ;

paramList
   : param (COMMA param)*
   ;

param
   : name = varBinding (KW_AS sequenceType)?
   ;

annotations
   : annotation*
   ;

annotation
   : MOD name = eqName (LPAREN literal (COMMA literal)* RPAREN)?
   | updating = KW_UPDATING
   ;

optionDecl
   : KW_DECLARE KW_OPTION name = eqName value = stringLiteral SEMICOLON
   ;

expr
   : exprSingle (COMMA exprSingle)*
   ;

flworExpr
   : // replaced with the initialClause production to match the JSONiq grammar
   (start_for = forClause | start_let = letClause | start_window = windowClause)
   // replaced with the intermediateClause production to match the JSONiq grammar
   (forClause | letClause | windowClause | whereClause | groupByClause | orderByClause | countClause)*
   // replaced with the returnClause production to match the JSONiq grammar
   KW_RETURN return_expr = exprSingle
   ;

forClause
   : KW_FOR vars += forVar (COMMA vars += forVar)*
   ;

forVar
   : var_ref = varBinding
   // replaced with the typeDeclaration production to match the JSONiq grammar
   (KW_AS seq = sequenceType)? (flag = allowingEmpty)?
   // replaced with the positionalVar production to match the JSONiq grammar
   (KW_AT at = varBinding)? KW_IN ex = exprSingle
   ;

allowingEmpty
   : KW_ALLOWING KW_EMPTY
   ;

positionalVar
   : KW_AT pvar = varBinding
   ;

letClause
   : KW_LET vars += letVar (COMMA vars += letVar)*
   ;

letVar
   : var_ref = varBinding
   // replaced with the typeDeclaration production to match the JSONiq grammar
   (KW_AS seq = sequenceType)? COLON_EQ ex = exprSingle
   ;

windowClause
   : KW_FOR (tumblingWindowClause | slidingWindowClause)
   ;

tumblingWindowClause
   : KW_TUMBLING KW_WINDOW name = varBinding type = typeDeclaration? KW_IN exprSingle windowStartCondition windowEndCondition?
   ;

slidingWindowClause
   : KW_SLIDING KW_WINDOW name = varBinding type = typeDeclaration? KW_IN exprSingle windowStartCondition windowEndCondition
   ;

windowStartCondition
   : KW_START windowVars KW_WHEN exprSingle
   ;

windowEndCondition
   : KW_ONLY? KW_END windowVars KW_WHEN exprSingle
   ;

windowVars
   : (currentItem = varBinding)? positionalVar? (KW_PREVIOUS previousItem = varBinding)? (KW_NEXT nextItem = varBinding)?
   ;

countClause
   : KW_COUNT varBinding
   ;

whereClause
   : KW_WHERE exprSingle
   ;

groupByClause
   : KW_GROUP KW_BY vars += groupByVar (COMMA vars += groupByVar)*
   ;

groupByVar
   : var_ref = varBinding
   // replaced with the typeDeclaration production to match the JSONiq grammar
   ((KW_AS seq = sequenceType)? decl = COLON_EQ ex = exprSingle)? (KW_COLLATION uri = uriLiteral)?
   ;

orderByClause
   : stb = KW_STABLE? KW_ORDER KW_BY specs += orderByExpr (COMMA specs += orderByExpr)*
   ;

orderByExpr
   : ex = exprSingle (KW_ASCENDING | desc = KW_DESCENDING)? (KW_EMPTY (gr = KW_GREATEST | ls = KW_LEAST))? (KW_COLLATION uril = uriLiteral)?
   ;

quantifiedExpr
   : (so = KW_SOME | ev = KW_EVERY) vars += quantifiedExprVar (COMMA vars += quantifiedExprVar)* KW_SATISFIES exprSingle
   ;

quantifiedExprVar
   : var_ref = varBinding
   // replaced with the typeDeclaration production to match the JSONiq grammar
   (KW_AS seq = sequenceType)? KW_IN exprSingle
   ;

switchExpr
   : KW_SWITCH LPAREN cond = expr RPAREN cases += switchCaseClause+ KW_DEFAULT KW_RETURN def = exprSingle
   ;

switchCaseClause
   : (KW_CASE cond += exprSingle)+ KW_RETURN ret = exprSingle
   ;

typeswitchExpr
   : KW_TYPESWITCH LPAREN cond = expr RPAREN cses += caseClause+ KW_DEFAULT (var_ref = varBinding)? KW_RETURN def = exprSingle
   ;

caseClause
   : KW_CASE (var_ref = varBinding KW_AS)? union += sequenceType (VBAR union += sequenceType)* KW_RETURN ret = exprSingle
   ;

ifExpr
   : KW_IF LPAREN test_condition = expr RPAREN KW_THEN branch = exprSingle KW_ELSE else_branch = exprSingle
   ;

tryCatchExpr
   : KW_TRY LBRACE try_expression = expr? RBRACE catches += catchClause+
   ;

catchClause
   : KW_CATCH nameTest (VBAR nameTest)*
   // replaced with the enclosedExpression production to match the JSONiq grammar
   LBRACE catch_expression = expr? RBRACE
   ;

enclosedExpression
   : LBRACE expr? RBRACE
   ;

orExpr
   : main_expr = andExpr (KW_OR rhs += andExpr)*
   ;

andExpr
   : main_expr = notExpr (KW_AND rhs += notExpr)*
   ;

notExpr
   : op += KW_NOT? main_expr = comparisonExpr
   ;

comparisonExpr
   : main_expr = stringConcatExpr (op += compOp rhs += stringConcatExpr)?
   ;

stringConcatExpr
   : main_expr = rangeExpr (CONCATENATION rhs += rangeExpr)*
   ;

rangeExpr
   : main_expr = additiveExpr (KW_TO rhs += additiveExpr)?
   ;

additiveExpr
   : main_expr = multiplicativeExpr (op += (PLUS | MINUS) rhs += multiplicativeExpr)*
   ;

multiplicativeExpr
   : main_expr = unionExpr (op += (STAR | KW_DIV | KW_IDIV | KW_MOD) rhs += unionExpr)*
   ;

unionExpr
   : main_expr = intersectExceptExpr (op += (KW_UNION | VBAR) rhs += intersectExceptExpr)*
   ;

intersectExceptExpr
   : main_expr = instanceOfExpr (op += (KW_INTERSECT | KW_EXCEPT) rhs += instanceOfExpr)*
   ;

instanceOfExpr
   : main_expr = isStaticallyExpr (KW_INSTANCE KW_OF seq = sequenceType)?
   ;

isStaticallyExpr
   : main_expr = treatExpr (KW_IS KW_STATICALLY seq = sequenceType)?
   ;

treatExpr
   : main_expr = castableExpr (KW_TREAT KW_AS seq = sequenceType)?
   ;

castableExpr
   : main_expr = castExpr (KW_CASTABLE KW_AS single = singleType)?
   ;

castExpr
   : main_expr = arrowExpr (KW_CAST KW_AS single = singleType)?
   ;

arrowExpr
   : main_expr = unaryExpr (ARROW function += arrowFunctionSpecifier arguments += argumentList)*
   ;

unaryExpr
   : op += (MINUS | PLUS)* main_expr = valueExpr
   ;

valueExpr
   : validate_expr = validateExpr
   | extensionExpr
   | simpleMap_expr = simpleMapExpr
   ;

compOp
   : valueComp
   | generalComp
   | nodeComp
   ;

generalComp
   : EQUAL
   | NOT_EQUAL
   | LANGLE
   | (LANGLE EQUAL)
   | RANGLE
   | (RANGLE EQUAL)
   ;

valueComp
   : KW_EQ
   | KW_NE
   | KW_LT
   | KW_LE
   | KW_GT
   | KW_GE
   ;

nodeComp
   : KW_IS
   | (LANGLE LANGLE)
   | (RANGLE RANGLE)
   ;

validateExpr
   : KW_VALIDATE (validationMode | (KW_TYPE sequenceType))? LBRACE expr? RBRACE
   ;

validationMode
   : KW_LAX
   | KW_STRICT
   ;

extensionExpr
   : PRAGMA+ LBRACE expr RBRACE
   ;

simpleMapExpr
   : main_expr = pathExpr (BANG map_expr += pathExpr)*
   ;

pathExpr
   : (SLASH singleslash = relativePathExpr?)
   | (DSLASH doubleslash = relativePathExpr)
   | relative = relativePathExpr
   ;

relativePathExpr
   : stepExpr (sep += (SLASH | DSLASH) stepExpr)*
   ;

stepExpr
   : postfixExpr
   | axisStep
   ;

axisStep
   : (reverseStep | forwardStep) predicateList
   ;

forwardStep
   : (forwardAxis nodeTest)
   | abbrevForwardStep
   ;

forwardAxis
   : (KW_CHILD | KW_DESCENDANT | KW_ATTRIBUTE | KW_SELF | KW_DESCENDANT_OR_SELF | KW_FOLLOWING_SIBLING | KW_FOLLOWING) COLON COLON
   ;

abbrevForwardStep
   : AT? nodeTest
   ;

reverseStep
   : (reverseAxis nodeTest)
   | abbrevReverseStep
   ;

reverseAxis
   : (KW_PARENT | KW_ANCESTOR | KW_PRECEDING_SIBLING | KW_PRECEDING | KW_ANCESTOR_OR_SELF) COLON COLON
   ;

abbrevReverseStep
   : DDOT
   ;

nodeTest
   : nameTest
   | kindTest
   ;

nameTest
   : eqName
   | wildcard
   ;

wildcard
   : STAR # allNames
   | NCNameWithLocalWildcard # allWithNS // walkers must strip out the trailing :*
   | NCNameWithPrefixWildcard # allWithLocal // walkers must strip out the leading *:
   | (BracedURILiteral STAR) # BracedURILiteral
   ;

argumentList
   : LPAREN (args += argument (COMMA args += argument)*)? RPAREN
   ;

predicateList
   : predicate*
   ;

predicate
   : LBRACKET expr RBRACKET
   ;

lookup
   : QUESTION keySpecifier
   ;

keySpecifier
   : (nc = ncName | in = IntegerLiteral | pe = parenthesizedExpr | wc = STAR | lt = stringLiteral | vr = varRef)
   ;

arrowFunctionSpecifier
   : eqName
   | varRef
   | parenthesizedExpr
   ;

literal
   : numericLiteral
   | stringLiteral
   ;

numericLiteral
   : IntegerLiteral
   | DecimalLiteral
   | DoubleLiteral
   ;

varRef
   : DOLLAR var_name = eqName
   ;

varBinding
   : DOLLAR var_name = eqName
   ;

parenthesizedExpr
   : LPAREN expr? RPAREN
   ;

orderedExpr
   : KW_ORDERED enclosedExpression
   ;

unorderedExpr
   : KW_UNORDERED enclosedExpression
   ;

functionCall
   : fn_name = functionName argumentList
   ;

argument
   : exprSingle
   | QUESTION
   ;

nodeConstructor
   : directConstructor
   | computedConstructor
   ;

directConstructor
   : LANGLE open_tag_name = qname attributes = dirAttributeList (open_close = dirElemConstructorOpenClose | single_tag = dirElemConstructorSingleTag)
   | COMMENT
   | PI
   ;

dirElemConstructorOpenClose
   : endOpen = RANGLE dirElemContent* startClose = LANGLE slashClose = SLASH close_tag_name = qname RANGLE
   ;

dirElemConstructorSingleTag
   : slashClose = SLASH RANGLE
   ;

dirAttributeList
   : (attribute_qname += qname EQUAL attribute_value += dirAttributeValue)*
   ;

dirAttributeValueQuot
   : Quot (PredefinedEntityRef | CharRef | escapedQuot | dirAttributeContentQuot)* Quot
   ;

dirAttributeValueApos
   : Apos (PredefinedEntityRef | CharRef | escapedApos | dirAttributeContentApos)* Apos
   ;

dirAttributeValue
   : dirAttributeValueQuot
   | dirAttributeValueApos
   ;

dirAttributeContentQuot
   : LBRACE LBRACE
   | RBRACE RBRACE
   | LBRACE expr? RBRACE
   | ~ (Quot | LBRACE | RBRACE | Ampersand | PredefinedEntityRef | CharRef | LANGLE | COMMENT | XMLDECL | PI | CDATA)
   ;

dirAttributeContentApos
   : LBRACE LBRACE
   | RBRACE RBRACE
   | LBRACE expr? RBRACE
   | ~ (Apos | LBRACE | RBRACE | Ampersand | PredefinedEntityRef | CharRef | LANGLE | COMMENT | XMLDECL | PI | CDATA)
   ;

escapedQuot
   : Quot Quot
   ;

escapedApos
   : Apos Apos
   ;

dirElemContent
   : directConstructor
   | commonContent
   | CDATA
   // ~[{}<&] = '" + ~['"{}<&]
   | Quot
   | Apos
   | noQuotesNoBracesNoAmpNoLAng
   ;

commonContent
   : (PredefinedEntityRef | CharRef)
   | LBRACE LBRACE
   | RBRACE RBRACE
   | (LBRACE expr? RBRACE)
   ;

computedConstructor
   : compDocConstructor
   | compElemConstructor
   | compAttrConstructor
   | compNamespaceConstructor
   | compTextConstructor
   | compCommentConstructor
   | compPIConstructor
   ;

compDocConstructor
   : KW_DOCUMENT enclosedExpression
   ;

compElemConstructor
   : KW_ELEMENT (eqName | (LBRACE expr RBRACE)) enclosedContentExpr
   ;

enclosedContentExpr
   : enclosedExpression
   ;

compAttrConstructor
   : KW_ATTRIBUTE (name = eqName | (LBRACE name_expr = expr RBRACE)) enclosedExpression
   ;

compNamespaceConstructor
   : KW_NAMESPACE (ncName | enclosedPrefixExpr) enclosedURIExpr
   ;

enclosedPrefixExpr
   : enclosedExpression
   ;

enclosedURIExpr
   : enclosedExpression
   ;

compTextConstructor
   : KW_TEXT enclosedExpression
   ;

compCommentConstructor
   : KW_COMMENT enclosedExpression
   ;

compPIConstructor
   : KW_PI (ncName | (LBRACE expr RBRACE)) enclosedExpression
   ;

functionItemExpr
   : namedFunctionRef
   | inlineFunctionExpr
   ;

namedFunctionRef
   : fn_name = functionName HASH arity = IntegerLiteral
   ;

inlineFunctionExpr
   : annotations KW_FUNCTION LPAREN paramList? RPAREN (KW_AS return_type = sequenceType)? (LBRACE (fn_body = statementsAndOptionalExpr) RBRACE)
   ;

arrayConstructor
   : squareArrayConstructor
   | curlyArrayConstructor
   ;

squareArrayConstructor
   : LBRACKET (exprSingle (COMMA exprSingle)*)? RBRACKET
   ;

curlyArrayConstructor
   : KW_ARRAY enclosedExpression
   ;

stringConstructor
   : ENTER_STRING stringConstructorContent EXIT_STRING
   ;

stringConstructorContent
   : stringConstructorChars (stringConstructorInterpolation stringConstructorChars)*
   ;

charNoGrave
   : BASIC_CHAR
   | LBRACE
   | RBRACKET
   ;

charNoLBrace
   : BASIC_CHAR
   | GRAVE
   | RBRACKET
   ;

charNoRBrack
   : BASIC_CHAR
   | GRAVE
   | LBRACE
   ;

stringConstructorChars
   : (BASIC_CHAR | charNoGrave charNoLBrace | charNoRBrack charNoGrave charNoGrave | charNoGrave | LBRACE)*
   ;

stringConstructorInterpolation
   : ENTER_INTERPOLATION expr EXIT_INTERPOLATION
   ;

unaryLookup
   : QUESTION keySpecifier
   ;

singleType
   : item = itemType (question += QUESTION)?
   ;

typeDeclaration
   : KW_AS sequenceType
   ;

kindTest
   : documentTest
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

anyKindTest
   : KW_NODE LPAREN STAR? RPAREN
   ;

binaryNodeTest
   : KW_BINARY LPAREN RPAREN
   ;

documentTest
   : KW_DOCUMENT_NODE LPAREN (elementTest | schemaElementTest)? RPAREN
   ;

textTest
   : KW_TEXT LPAREN RPAREN
   ;

commentTest
   : KW_COMMENT LPAREN RPAREN
   ;

namespaceNodeTest
   : KW_NAMESPACE_NODE LPAREN RPAREN
   ;

piTest
   : KW_PI LPAREN (ncName | stringLiteral)? RPAREN
   ;

attributeTest
   : KW_ATTRIBUTE LPAREN (attributeNameOrWildcard (COMMA type = typeName)?)? RPAREN
   ;

attributeNameOrWildcard
   : attributeName
   | STAR
   ;

schemaAttributeTest
   : KW_SCHEMA_ATTR LPAREN attributeDeclaration RPAREN
   ;

elementTest
   : KW_ELEMENT LPAREN (elementNameOrWildcard (COMMA type = typeName optional = QUESTION?)?)? RPAREN
   ;

elementNameOrWildcard
   : elementName
   | STAR
   ;

schemaElementTest
   : KW_SCHEMA_ELEM LPAREN elementDeclaration RPAREN
   ;

elementDeclaration
   : elementName
   ;

attributeName
   : eqName
   ;

elementName
   : eqName
   ;

simpleTypeName
   : typeName
   ;

typeName
   : eqName
   ;

functionTest
   : annotation* (anyFunctionTest | typedFunctionTest)
   ;

anyFunctionTest
   : KW_FUNCTION LPAREN STAR RPAREN
   ;

typedFunctionTest
   : KW_FUNCTION LPAREN (st += sequenceType (COMMA st += sequenceType)*)? RPAREN KW_AS rt = sequenceType
   ;

mapTest
   : anyMapTest
   | typedMapTest
   ;

anyMapTest
   : KW_MAP LPAREN STAR RPAREN
   ;

typedMapTest
   : KW_MAP LPAREN eqName COMMA sequenceType RPAREN
   ;

arrayTest
   : anyArrayTest
   | typedArrayTest
   ;

anyArrayTest
   : KW_ARRAY LPAREN STAR RPAREN
   ;

typedArrayTest
   : KW_ARRAY LPAREN sequenceType RPAREN
   ;

parenthesizedItemTest
   : LPAREN itemType RPAREN
   ;

attributeDeclaration
   : attributeName
   ;

eqName
   : qname
   | URIQualifiedName
   ;

qname
   : FullQName
   | (ns = ncName COLON)? local_name = ncName
   ;

ncName
   : NCName
   | keyword
   ;

functionName
   : FullQName
   | NCName
   | URIQualifiedName
   | keywordOKForFunction
   ;

keyword
   : keywordOKForFunction
   | keywordNotOKForFunction
   ;

keywordNotOKForFunction
   : KW_ATTRIBUTE
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
   | KW_MAP
   | KW_END
   | KW_ALLOWING
   | KW_ARRAY
   | DFPropertyName
   ;

uriLiteral
   : stringLiteral
   ;

stringLiteral
   : stringLiteralQuot
   | stringLiteralApos
   ;

noQuotesNoBracesNoAmpNoLAng
   : (keyword | (IntegerLiteral | DecimalLiteral | DoubleLiteral
   //| stringLiteral
   | PRAGMA | EQUAL | HASH | NOT_EQUAL | LPAREN | RPAREN | LBRACKET | RBRACKET | STAR | PLUS | MINUS | TILDE | COMMA | ARROW | MOD | DOT | GRAVE | DDOT | COLON | CARAT | COLON_EQ | SEMICOLON | SLASH | DSLASH | BACKSLASH | VBAR | RANGLE | QUESTION | AT | DOLLAR | BANG | FullQName | URIQualifiedName | NCNameWithLocalWildcard | NCNameWithPrefixWildcard | NCName | ContentChar))+
   ;

mainModule
   : prolog program
   ;

program
   : statementsAndOptionalExpr
   ;

statements
   : statement*
   ;

statementsAndExpr
   : statements expr
   ;

statementsAndOptionalExpr
   : statements expr?
   ;

statement
   : applyStatement
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

applyStatement
   : exprSimple SEMICOLON
   ;

assignStatement
   : var_ref = varRef COLON_EQ exprSingle SEMICOLON
   ;

blockStatement
   : LBRACE statement+ RBRACE
   ;

breakStatement
   : KW_BREAK KW_LOOP SEMICOLON
   ;

continueStatement
   : KW_CONTINUE KW_LOOP SEMICOLON
   ;

exitStatement
   : KW_EXIT KW_RETURNING exprSingle SEMICOLON
   ;

flworStatement
   : (start_for = forClause | start_let = letClause)
   // replaced with the intermediateClause production to match the JSONiq grammar
   (forClause | letClause | whereClause | groupByClause | orderByClause | countClause)*
   // replaced with the returnStatement production to match the JSONiq grammar
   KW_RETURN returnStmt = statement
   ;

ifStatement
   : KW_IF LPAREN test_expr = expr RPAREN KW_THEN branch = statement KW_ELSE else_branch = statement
   ;

switchStatement
   : KW_SWITCH LPAREN condExpr = expr RPAREN cases += switchCaseStatement+ KW_DEFAULT KW_RETURN def = statement
   ;

switchCaseStatement
   : (KW_CASE cond += exprSingle)+ KW_RETURN ret = statement
   ;

tryCatchStatement
   : KW_TRY try_block = blockStatement catches += catchCaseStatement+
   ;

catchCaseStatement
   : KW_CATCH nameTest (VBAR nameTest)* catch_block = blockStatement
   ;

typeSwitchStatement
   : KW_TYPESWITCH LPAREN cond = expr RPAREN cases += caseStatement+ KW_DEFAULT (var_ref = varBinding)? (KW_RETURN) def = statement
   ;

caseStatement
   : KW_CASE (var_ref = varBinding KW_AS)? union += sequenceType (VBAR union += sequenceType)* (KW_RETURN) ret = statement
   ;

varDeclStatement
   : annotations KW_VARIABLE varDeclForStatement (COMMA varDeclForStatement)* SEMICOLON
   ;

varDeclForStatement
   : var_ref = varBinding (KW_AS sequenceType)? (COLON_EQ expr_vals += exprSingle)?
   ;

whileStatement
   : KW_WHILE LPAREN test_expr = expr RPAREN stmt = statement
   ;

exprSingle
   : exprSimple
   | flworExpr
   | ifExpr
   | switchExpr
   | tryCatchExpr
   | typeswitchExpr
   ;

exprSimple
   : quantifiedExpr
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

blockExpr
   : LBRACE statementsAndExpr RBRACE
   ;

insertExpr
   : KW_INSERT KW_JSON to_insert_expr = exprSingle KW_INTO main_expr = exprSingle (KW_AT KW_POSITION pos_expr = exprSingle)?
   | KW_INSERT KW_JSON pairConstructor (COMMA pairConstructor)* KW_INTO main_expr = exprSingle
   ;

deleteExpr
   : KW_DELETE KW_JSON updateLocator
   ;

renameExpr
   : KW_RENAME KW_JSON updateLocator KW_AS name_expr = exprSingle
   ;

replaceExpr
   : KW_REPLACE KW_VALUE KW_OF KW_JSON updateLocator KW_WITH replacer_expr = exprSingle
   ;

transformExpr
   : KW_COPY copyDecl (COMMA copyDecl)* KW_MODIFY mod_expr = exprSingle KW_RETURN ret_expr = exprSingle
   ;

appendExpr
   : KW_APPEND KW_JSON to_append_expr = exprSingle KW_INTO array_expr = exprSingle
   ;

updateLocator
   : main_expr = postfixExpr
   ;

copyDecl
   : var_ref = varBinding COLON_EQ src_expr = exprSingle
   ;

createCollectionExpr
   : KW_CREATE KW_COLLECTION collectionMode = (KW_TABLE | KW_DELTA_FILE | KW_ICEBERG_TABLE) LPAREN collection_name = exprSimple RPAREN (KW_WITH content = exprSingle)?
   ;

deleteIndexExpr
   : KW_DELETE ((first = KW_FIRST | last = KW_LAST) num = exprSingle?) KW_FROM KW_COLLECTION collectionMode = (KW_TABLE | KW_DELTA_FILE | KW_ICEBERG_TABLE) LPAREN collection_name = exprSimple RPAREN
   ;

deleteSearchExpr
   : KW_DELETE content = exprSingle KW_FROM KW_COLLECTION
   ;

insertIndexExpr
   : KW_INSERT content = exprSingle ((KW_AT pos = exprSingle) | first = KW_FIRST | last = KW_LAST) KW_INTO KW_COLLECTION collectionMode = (KW_TABLE | KW_DELTA_FILE | KW_ICEBERG_TABLE) LPAREN collection_name = exprSimple RPAREN
   ;

insertSearchExpr
   : KW_INSERT content = exprSingle (before = KW_BEFORE | after = KW_AFTER) target = exprSingle KW_INTO KW_COLLECTION
   ;

truncateCollectionExpr
   : (KW_DELETE | KW_TRUNCATE) KW_COLLECTION collectionMode = (KW_TABLE | KW_DELTA_FILE | KW_ICEBERG_TABLE) LPAREN collection_name = exprSimple RPAREN
   ;

editCollectionExpr
   : KW_EDIT target = exprSingle KW_INTO content = exprSingle KW_IN KW_COLLECTION
   ;

