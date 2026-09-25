lexer grammar JsoniqLexer;

import CommonLexer;
@ header
{
package org.rumbledb.parser.jsoniq;
}
DOUBLE_DOLLAR
   : '$$'
   ;

LBRACE_VBAR
   : '{|'
   ;

RBRACE_VBAR
   : '|}'
   ;

KW_JSONIQ
   : 'jsoniq'
   ;

KW_JSOUND
   : 'jsound'
   ;

KW_COMPACT
   : 'compact'
   ;

KW_VERBOSE
   : 'verbose'
   ;

KW_NOT
   : 'not'
   ;

KW_NULL
   : 'null'
   ;

KW_TRUE
   : 'true'
   ;

KW_FALSE
   : 'false'
   ;

fragment NameChar // JSONiq requires "." to remain a separate DOT token for selectors such as $store."state".
   : NameStartChar
   | '-'
   | [0-9]
   | '\u00A1' .. '\u00BF'
   | '\u0300' .. '\u036F'
   | '\u203F' .. '\u2040'
   ;

