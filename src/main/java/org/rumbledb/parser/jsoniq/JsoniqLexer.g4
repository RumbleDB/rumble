lexer grammar JsoniqLexer;

import CommonLexer;
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

mode INTERPOLATION_MODE;
INTERPOLATION_DOUBLE_DOLLAR
   : DOUBLE_DOLLAR -> type (DOUBLE_DOLLAR)
   ;

INTERPOLATION_LBRACE_VBAR
   : LBRACE_VBAR -> type (LBRACE_VBAR)
   ;

INTERPOLATION_RBRACE_VBAR
   : RBRACE_VBAR -> type (RBRACE_VBAR)
   ;

INTERPOLATION_KW_JSONIQ
   : KW_JSONIQ -> type (KW_JSONIQ)
   ;

INTERPOLATION_KW_JSOUND
   : KW_JSOUND -> type (KW_JSOUND)
   ;

INTERPOLATION_KW_COMPACT
   : KW_COMPACT -> type (KW_COMPACT)
   ;

INTERPOLATION_KW_VERBOSE
   : KW_VERBOSE -> type (KW_VERBOSE)
   ;

INTERPOLATION_KW_NOT
   : KW_NOT -> type (KW_NOT)
   ;

INTERPOLATION_KW_NULL
   : KW_NULL -> type (KW_NULL)
   ;

INTERPOLATION_KW_TRUE
   : KW_TRUE -> type (KW_TRUE)
   ;

INTERPOLATION_KW_FALSE
   : KW_FALSE -> type (KW_FALSE)
   ;

