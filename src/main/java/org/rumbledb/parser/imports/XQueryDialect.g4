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
 */
parser grammar XQueryDialect;

xqueryModule
   : // replaced with the xqueryVersionDecl production to match the JSONiq grammar
   (KW_XQUERY KW_VERSION vers = stringLiteral (KW_ENCODING encoding = stringLiteral)? SEMICOLON)?
   // TODO: subsequent optional main modules are currently ignored
   (libraryModule | (main = mainModule (SEMICOLON xqueryVersionDecl? mainModule)*))
   ;

xqueryVersionDecl
   : KW_XQUERY KW_VERSION version = stringLiteral (KW_ENCODING encoding = stringLiteral)? SEMICOLON
   ;

xqueryAnnotatedDecl
   : functionDecl
   | varDecl
   | contextItemDecl
   | optionDecl
   ;

xqueryPostfixExpr
   : main_expr = xqueryPrimaryExpr (predicate | argumentList | lookup)*
   ;

xqueryPrimaryExpr
   : literal
   | varRef
   | parenthesizedExpr
   | xqueryContextItemExpr
   | functionCall
   | orderedExpr
   | unorderedExpr
   | nodeConstructor
   | functionItemExpr
   | xqueryObjectConstructor
   | arrayConstructor
   | stringConstructor
   | unaryLookup
   | blockExpr
   ;

xqueryContextItemExpr
   : DOT
   ;

xqueryObjectConstructor
   : KW_MAP LBRACE (xqueryPairConstructor (COMMA xqueryPairConstructor)*)? RBRACE
   ;

xqueryPairConstructor
   : lhs = exprSingle (COLON | COLON_EQ) rhs = exprSingle
   ;

xquerySequenceType
   : (KW_EMPTY_SEQUENCE LPAREN RPAREN)
   | (item = xqueryItemType (question += QUESTION | star += STAR | plus += PLUS)?)
   ;

xqueryItemType
   : kindTest
   | (KW_ITEM LPAREN RPAREN)
   | functionTest
   | mapTest
   | arrayTest
/*
 * simplification compared to XQuery 3.1 grammar
 * removes the need for a separate atomicOrUnionType rule
 */
   
   | eqName
   | parenthesizedItemTest
   ;

xqueryKeywordOKForFunction
   : KW_ANCESTOR
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
   | KW_COUNT
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
   // XQuery Scripting Extension keywords
   | KW_BREAK
   | KW_LOOP
   | KW_CONTINUE
   | KW_EXIT
   | KW_RETURNING
   | KW_WHILE
   //  Updating expressions keywords
   | KW_COPY
   | KW_MODIFY
   | KW_REPLACE
   | KW_APPEND
   | KW_JSON
   | KW_POSITION
   | KW_UPDATING
   | KW_LAST
   // JSONiq specific
   | KW_STATICALLY
   | KW_INSERT
   | KW_DELETE
   | KW_RENAME
   | KW_TRUNCATE
   | KW_EDIT
   | KW_INTO
   | KW_VALUE
   | KW_FROM
   | KW_WITH
   | KW_BEFORE
   | KW_AFTER
   | KW_FIRST
   | KW_CREATE
   | KW_COLLECTION
   | KW_TABLE
   | KW_DELTA_FILE
   | KW_ICEBERG_TABLE
   | KW_NEXT
   | KW_PREVIOUS
   ;

