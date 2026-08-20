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

