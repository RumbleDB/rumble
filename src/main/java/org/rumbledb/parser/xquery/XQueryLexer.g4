lexer grammar XQueryLexer;

import CommonLexer;
KW_XQUERY
   : 'xquery'
   ;
   // MarkLogic JSON computed constructor
   
mode INTERPOLATION_MODE;
INTERPOLATION_KW_XQUERY
   : KW_XQUERY -> type (KW_XQUERY)
   ;

