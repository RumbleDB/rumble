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
 * Authors: Matteo Agnoletto (EPMatt)
 *
 * A parser grammar for XQuery 3.1 that includes the XQuery Scripting Extensions, and additional update features.
 * This file is based on the XQuery parser grammar from the xqdoc project:
 * https://github.com/xqdoc/xqdoc/blob/master/src/main/antlr4/org/xqdoc/XQueryParser.g4
 * 
 * See LICENSE-xqdoc.txt for the original license terms.
 * 
 */
parser grammar XQueryParser;

import CommonParser;
@ header
{
// Java header
package org.rumbledb.parser.xquery;
}

options { tokenVocab = XQueryLexer; }
module
   : // replaced with the versionDecl production to match the JSONiq grammar
   (KW_XQUERY KW_VERSION vers = stringLiteral (KW_ENCODING encoding = stringLiteral)? SEMICOLON)?
   // TODO: subsequent optional main modules are currently ignored
   (libraryModule | (main = mainModule (SEMICOLON versionDecl? mainModule)*))
   ;

versionDecl
   : KW_XQUERY KW_VERSION version = stringLiteral (KW_ENCODING encoding = stringLiteral)? SEMICOLON
   ;

annotatedDecl
   : functionDecl
   | varDecl
   | contextItemDecl
   | optionDecl
   ;

andExpr
   : main_expr = comparisonExpr (KW_AND rhs += comparisonExpr)*
   ;

postfixExpr
   : main_expr = primaryExpr (predicate | argumentList | lookup)*
   ;

primaryExpr
   : literal
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
   : DOT
   ;

validateExpr
   : KW_VALIDATE (validationMode | (KW_TYPE typeName))? LBRACE expr RBRACE
   ;

objectConstructor
   : KW_MAP LBRACE (pairConstructor (COMMA pairConstructor)*)? RBRACE
   ;

pairConstructor
   : lhs = exprSingle (COLON | COLON_EQ) rhs = exprSingle
   ;

sequenceType
   : (KW_EMPTY_SEQUENCE LPAREN RPAREN)
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
   
   | eqName
   | parenthesizedItemTest
   ;

dialectKeywordOKForFunction
   : KW_XQUERY
   ;

stringLiteralQuot
   : Quot (escapedQuot | ~ (Quot | Ampersand))* Quot
   ;

stringLiteralApos
   : Apos (escapedApos | ~ (Apos | Ampersand))* Apos
   ;

