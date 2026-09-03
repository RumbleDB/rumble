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
parser grammar JsoniqParser;

import CommonParser;
@ header
{
// Java header
package org.rumbledb.parser.jsoniq;
}

options { tokenVocab = JsoniqLexer; }
module
   // replaced with the versionDecl production to match the JSONiq grammar
   : (KW_JSONIQ KW_VERSION vers = stringLiteral (KW_ENCODING encoding = stringLiteral)? SEMICOLON)?
   // TODO: subsequent optional main modules are currently ignored
   (libraryModule | main = mainModule)
   ;

versionDecl
   : KW_JSONIQ KW_VERSION version = stringLiteral (KW_ENCODING encoding = stringLiteral)? SEMICOLON
   ;

annotatedDecl
   : functionDecl
   | varDecl
   | typeDecl
   | contextItemDecl
   | optionDecl
   ;

typeDecl
   : KW_DECLARE KW_TYPE type_name = qname KW_AS (schema = schemaLanguage)? type_definition = exprSingle SEMICOLON
   ;

schemaLanguage
   : KW_JSOUND KW_COMPACT
   | KW_JSOUND KW_VERBOSE
   | KW_JSON KW_SCHEMA
   ;

andExpr
   : main_expr = notExpr (KW_AND rhs += notExpr)*
   ;

notExpr
   : op += KW_NOT? main_expr = comparisonExpr
   ;

arrayLookup
   : LBRACKET LBRACKET expr RBRACKET RBRACKET
   ;

arrayUnboxing
   : LBRACKET RBRACKET
   ;

objectLookup
   : DOT (kw = keyword | lt = stringLiteral | nc = NCName | pe = parenthesizedExpr | vr = varRef | ci = contextItemExpr)
   ;

postfixExpr
   : main_expr = primaryExpr (arrayLookup | predicate | objectLookup | arrayUnboxing | argumentList | lookup)*
   ;

primaryExpr
   : literal
   | KW_NULL
   | KW_TRUE
   | KW_FALSE
   | varRef
   | parenthesizedExpr
   | contextItemExpr
   | functionCall
   | orderedExpr
   | unorderedExpr
   | nodeConstructor
   | functionItemExpr
   | objectConstructor
   | arrayConstructor
   | stringConstructor
   | unaryLookup
   | blockExpr
   ;

contextItemExpr
   : DOUBLE_DOLLAR
   ;

objectConstructor
   : KW_MAP? LBRACE (pairConstructor (COMMA pairConstructor)*)? RBRACE
   | merge_operator += LBRACE_VBAR expr RBRACE_VBAR
   ;

pairConstructor
   : lhs = exprSingle (COLON | COLON_EQ | QUESTION) rhs = exprSingle
   ;

validateExpr
   : KW_VALIDATE (validationMode | (KW_TYPE sequenceType))? LBRACE expr? RBRACE
   ;

sequenceType
   : (KW_EMPTY_SEQUENCE? LPAREN RPAREN)
   | (item = itemType (question += QUESTION | star += STAR | plus += PLUS)?)
   ;

itemType
   : kindTest
   | (KW_ITEM LPAREN RPAREN)
   | functionTest
   | mapTest
   | arrayTest
/*
 * simplification compared to XQuery 3.1 grammar
 * removes the need for a separate atomicOrUnionType rule
 */
   
   | KW_NULL
   | eqName
   | parenthesizedItemTest
   ;

keywordOKForFunction
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
   | KW_JSONIQ
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
   | KW_NULL
   | KW_TRUE
   | KW_FALSE
   | KW_NOT
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

escapedJsoniqStringCharacter
   : BACKSLASH .
   ;

stringLiteralQuot
   : Quot (escapedJsoniqStringCharacter | ~ (Quot | BACKSLASH))* Quot
   ;

stringLiteralApos
   : Apos (escapedJsoniqStringCharacter | ~ (Apos | BACKSLASH))* Apos
   ;

