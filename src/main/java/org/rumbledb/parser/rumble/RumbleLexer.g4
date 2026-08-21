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
lexer grammar RumbleLexer;

import SharedLexer;
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
DOUBLE_DOLLAR
   :
   {isJsoniq()}? '$$'
   ;

LBRACE_VBAR
   :
   {isJsoniq()}? '{|'
   ;

RBRACE_VBAR
   :
   {isJsoniq()}? '|}'
   ;

KW_JSONIQ
   :
   {isJsoniq()}? 'jsoniq'
   ;

KW_JSOUND
   :
   {isJsoniq()}? 'jsound'
   ;

KW_COMPACT
   :
   {isJsoniq()}? 'compact'
   ;

KW_VERBOSE
   :
   {isJsoniq()}? 'verbose'
   ;

KW_NOT
   :
   {isJsoniq()}? 'not'
   ;

KW_NULL
   :
   {isJsoniq()}? 'null'
   ;

KW_TRUE
   :
   {isJsoniq()}? 'true'
   ;

KW_FALSE
   :
   {isJsoniq()}? 'false'
   ;

KW_XQUERY
   :
   {isXQuery()}? 'xquery'
   ;
   // MarkLogic JSON computed constructor
   
fragment NameChar
   : NameStartChar
   | '-'
   |
   {isXQuery()}? '.'
   | [0-9]
   | '\u00A1' .. '\u00BF'
   | '\u0300' .. '\u036F'
   | '\u203F' .. '\u2040'
   ;

