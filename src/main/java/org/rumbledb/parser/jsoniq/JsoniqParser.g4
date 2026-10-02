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

dialectKeywordOKForFunction
   : KW_JSONIQ
   | KW_NULL
   | KW_TRUE
   | KW_FALSE
   | KW_NOT
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

