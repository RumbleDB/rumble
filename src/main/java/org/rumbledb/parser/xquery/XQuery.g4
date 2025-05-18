grammar XQuery;

///////////////////////// Scripting addition - begin

///////////////////////// Statements
// TODO: updating token is not referenced anywhere in the XQuery spec
// or in the XQuery Scripting Extension spec
// annotation                  : ('%' name=qname ('(' Literal (',' Literal)* ')')? | updating=Kupdating);

///////////////////////// Scripting addition - end

// TODO: typeDecl not referenced anywhere in the XQuery spec
// typeDecl                : Kdeclare Ktype type_name=qname 'as' (schema=schemaLanguage)? type_definition=exprSingle;

///////////////////////// expression

///////////////////////// Updating Expressions
// TODO: updating expressions are not referenced anywhere in the XQuery spec
// or in the XQuery Scripting Extension spec
// insertExpr              : Kinsert Kjson to_insert_expr=exprSingle Kinto main_expr=exprSingle (Kat Kposition pos_expr=exprSingle)?
//                        | Kinsert Kjson pairConstructor ( ',' pairConstructor )* Kinto main_expr=exprSingle;

// deleteExpr              : Kdelete Kjson updateLocator;

// renameExpr              : Krename Kjson updateLocator Kas name_expr=exprSingle;

// replaceExpr             : Kreplace Kvalue Kof Kjson updateLocator Kwith replacer_expr=exprSingle;

// transformExpr           : Kcopy copyDecl ( ',' copyDecl )* Kmodify mod_expr=exprSingle Kreturn ret_expr=exprSingle;

// appendExpr              : Kappend Kjson to_append_expr=exprSingle Kinto array_expr=exprSingle;

// updateLocator           : main_expr=primaryExpr ( lookup )+; // TODO CHECK THIS,

// copyDecl                    : var_ref=varRef ':=' src_expr=exprSingle;

///////////////////////// literals

// TODO: these are not referenced anywhere in the XQuery spec
//Ksome                   : 'some';
//Kstatically             : 'statically' ;
//Kannotate               : 'annotate';
//Kinsert                 : 'insert';
// Kdelete                 : 'delete';
// Krename                 : 'rename';
// Kreplace                : 'replace';
// Kcopy                   : 'copy';
// Kmodify                 : 'modify';
// Kappend                 : 'append';
// Kinto                   : 'into';
// Kvalue                  : 'value';
// Kwith                   : 'with';
// Kposition               : 'position';
// Kjson                   : 'json';
// Kupdating               :  'updating';


STRING                  : '"' (ESC | ~ ["\\])* '"' | '\'' (ESCapos | ~ ['\\])* '\'';

fragment ESC            : '\\' (["\\/bfnrt] | UNICODE);
fragment ESCapos            : '\\' (['\\/bfnrt] | UNICODE);

fragment UNICODE        : 'u' HEX HEX HEX HEX;

fragment HEX            : [0-9a-fA-F];

ArgumentPlaceholder     : '?';

// symbols

LBRACE: '{';
RBRACE: '}';

// literals

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-Literal */
Literal                 : NumericLiteral;

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-NumericLiteral */
NumericLiteral          : IntegerLiteral | DecimalLiteral | DoubleLiteral;

/// terminal symbols

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-IntegerLiteral */
IntegerLiteral          : Digits ;

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-DecimalLiteral */
DecimalLiteral          : ('.' Digits ) | ( Digits '.' [0-9]* );

/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-DoubleLiteral */
DoubleLiteral           : (( '.' Digits ) | ( Digits ('.' [0-9]*)?)) [eE] [+-]? Digits ;

WS                      : (' '|'\r'|'\t'|'\n') -> channel(HIDDEN);

/* source: https://www.w3.org/TR/REC-xml-names/#NT-NCName */
// TODO: this has been moved to a parser rule, as if it is defined as a lexer rule, it overrides other rules
// we know where this is used, so we can move it to a context-aware parser rule
NCName                  : NameStartChar (NameChar)*;


// problem. this matches too much!
ElementContentLexer     : Char+;

//Name                    : ;

fragment NameStartChar   : [_a-zA-Z]
                        | '\u00C0'..'\u00D6'
                        | '\u00D8'..'\u00F6'
                        | '\u00F8'..'\u02FF'
                        | '\u0370'..'\u037D'
                        | '\u037F'..'\u1FFF'
                        | '\u200C'..'\u200D'
                        | '\u2070'..'\u218F'
                        | '\u2C00'..'\u2FEF'
                        | '\u3001'..'\uD7FF'
                        | '\uF900'..'\uFDCF'
                        | '\uFDF0'..'\uFFFD'
                        ;

fragment NameChar       : NameStartChar
                        | '-'
                        | '.'
                        | [0-9]
                        | '\u00B7'
                        | '\u0300'..'\u036F'
                        | '\u203F'..'\u2040'
                        ;


/* source: https://www.w3.org/TR/REC-xml/#NT-Char */
fragment Char           :  '\u0009'
                        | '\u000A'
                        | '\u000D'
                        | '\u0020'..'\uD7FF'
                        | '\uE000'..'\uFFFD'
                        | '\uD800'..'\uDBFF' '\uDC00'..'\uDFFF';  // surrogate pairs for supplementary characters


/* source: https://www.w3.org/TR/xquery-31/#doc-xquery31-Digits */
fragment Digits         : [0-9]+ ;