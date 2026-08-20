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
parser grammar RumbleParser;


options { tokenVocab = RumbleLexer; }
import SharedParser , JsoniqDialect , XQueryDialect;
@ header
{
package org.rumbledb.parser.rumble;
}
@ members
{
    private boolean jsoniq;

    protected final void setJsoniqDialect(boolean jsoniq) {
        this.jsoniq = jsoniq;
    }

    protected boolean isJsoniq() {
        return this.jsoniq;
    }

    protected boolean isXQuery() {
        return !this.jsoniq;
    }
}
module
   :
   {isJsoniq()}? jsoniqModule
   |
   {isXQuery()}? xqueryModule
   ;

versionDecl
   :
   {isJsoniq()}? jsoniqVersionDecl
   |
   {isXQuery()}? xqueryVersionDecl
   ;

annotatedDecl
   :
   {isJsoniq()}? jsoniqAnnotatedDecl
   |
   {isXQuery()}? xqueryAnnotatedDecl
   ;

andExpr
   :
   {isJsoniq()}? jsoniqAndExpr
   |
   {isXQuery()}? xqueryAndExpr
   ;

postfixExpr
   :
   {isJsoniq()}? jsoniqPostfixExpr
   |
   {isXQuery()}? xqueryPostfixExpr
   ;

primaryExpr
   :
   {isJsoniq()}? jsoniqPrimaryExpr
   |
   {isXQuery()}? xqueryPrimaryExpr
   ;

contextItemExpr
   :
   {isJsoniq()}? jsoniqContextItemExpr
   |
   {isXQuery()}? xqueryContextItemExpr
   ;

objectConstructor
   :
   {isJsoniq()}? jsoniqObjectConstructor
   |
   {isXQuery()}? xqueryObjectConstructor
   ;

pairConstructor
   :
   {isJsoniq()}? jsoniqPairConstructor
   |
   {isXQuery()}? xqueryPairConstructor
   ;

sequenceType
   :
   {isJsoniq()}? jsoniqSequenceType
   |
   {isXQuery()}? xquerySequenceType
   ;

itemType
   :
   {isJsoniq()}? jsoniqItemType
   |
   {isXQuery()}? xqueryItemType
   ;

keywordOKForFunction
   :
   {isJsoniq()}? jsoniqKeywordOKForFunction
   |
   {isXQuery()}? xqueryKeywordOKForFunction
   ;

stringLiteralQuot
   :
   {isJsoniq()}? jsoniqStringLiteralQuot
   |
   {isXQuery()}? xqueryStringLiteralQuot
   ;

stringLiteralApos
   :
   {isJsoniq()}? jsoniqStringLiteralApos
   |
   {isXQuery()}? xqueryStringLiteralApos
   ;

