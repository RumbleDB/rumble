/*
 * A lexer grammar for XQuery 3.1 that includes the XQuery Scripting Extensions, and additional update features.
 * This file is based on the XQuery lexer grammar from the xqdoc project:
 * https://github.com/xqdoc/xqdoc/blob/master/src/main/antlr4/org/xqdoc/XQueryLexer.g4
 * 
 * See LICENSE-xqdoc.txt for the original license terms.
 * 
 * @author Matteo Agnoletto (EPMatt)
 */

lexer grammar JsoniqLexer;

@header {
// Java header
package org.rumbledb.parser.jsoniq;
}


// Note: string syntax depends on syntactic context, so they are
// handled by the parser and not the lexer.

///////////////////////// literals

COMMA                   : ',';

COLON_EQ                : ':=';

EQUAL                   : '=';

NOT_EQUAL               : '!=';

SEMICOLON               : ';';

LPAREN                  : '(';

RPAREN                  : ')';

VBAR                    : '|';

LBRACE                    : '{';

RBRACE                    : '}';

LBRACKET                  : '[';
RBRACKET                  : ']';

LANGLE                    : '<';

RANGLE                    : '>';

MOD                       : '%';

STAR                  : '*';

HASH                  : '#';

CONCATENATION                  : '||';

BANG                  : '!';

COLON                  : ':';

DDOT                    : '..';

PLUS                    : '+';

MINUS                   : '-';

TILDE : '~';

DOT : '.';

GRAVE           : '`' ;
CARAT           : '^' ;
BACKSLASH       : '\\';

ARROW : '=>';

LBRACE_VBAR : '{|';
RBRACE_VBAR : '|}';

KW_DIV : 'div';
KW_MOD : 'mod';
KW_IDIV : 'idiv';
KW_UNION : 'union';
KW_INTERSECT : 'intersect';
KW_EXCEPT : 'except';

QUESTION                : '?';

COMMENT : 'commentee637daa-5838-4675-975a-782077b371b9';

PI : 'pi637daa-5838-4675-975a-782077b371b9';

Quot : 'quot637daa-5838-4675-975a-782077b371b9';

PredefinedEntityRef : 'predefineedentityref637daa-5838-4675-975a-782077b371b9';

CharRef : 'charref637daa-5838-4675-975a-782077b371b9';

EscapeQuot: 'escapequotref637daa-5838-4675-975a-782077b371b9';

Apos: 'aposref637daa-5838-4675-975a-782077b371b9';

EscapeApos: 'escapeaposref637daa-5838-4675-975a-782077b371b9';

CDATA: 'cdataref637daa-5838-4675-975a-782077b371b9';

ENTER_STRING: 'enterstringref637daa-5838-4675-975a-782077b371b9';
EXIT_STRING: 'exitstringref637daa-5838-4675-975a-782077b371b9';
BASIC_CHAR: 'basiccharref637daa-5838-4675-975a-782077b371b9';
ENTER_INTERPOLATION: 'enterinterpolationref637daa-5838-4675-975a-782077b371b9';
EXIT_INTERPOLATION: 'exitinterpolationref637daa-5838-4675-975a-782077b371b9';
PRAGMA: 'pragmaref637daa-5838-4675-975a-782077b371b9';
FullQName: NCName ':' NCName ;
NCNameWithLocalWildcard:  NCName ':' '*' ;
NCNameWithPrefixWildcard: '*' ':' NCName ; 


KW_FOR                    : 'for';

KW_LET                    : 'let';

KW_WHERE                  : 'where';

KW_GROUP                  : 'group';

KW_TUMBLING               : 'tumbling';

KW_WINDOW                 : 'window';

DOLLAR                    : '$';

DOUBLE_DOLLAR             : '$$';

KW_SLIDING                : 'sliding';

KW_START                  : 'start';

KW_END                    : 'end';

KW_ONLY                   : 'only';

KW_WHEN                   : 'when';

KW_PREVIOUS               : 'previous';

KW_NEXT                   : 'next';

KW_BY                     : 'by';

KW_RETURN                 : 'return';

KW_IF                     : 'if';

KW_IN                     : 'in';

KW_AS                     : 'as';

KW_AT                     : 'at';

KW_ALLOWING               : 'allowing';

KW_EMPTY                  : 'empty';

KW_COUNT                  : 'count';

KW_STABLE                 : 'stable';

KW_ASCENDING              : 'ascending';

KW_DESCENDING             : 'descending';

KW_SOME                   : 'some';

KW_EVERY                  : 'every';

KW_SATISFIES              : 'satisfies';

KW_COLLATION              : 'collation';

KW_GREATEST               : 'greatest';

KW_LEAST                  : 'least';

KW_SWITCH                 : 'switch';

KW_ORDERING               : 'ordering';

KW_ORDERED                : 'ordered';

KW_UNORDERED              : 'unordered';

KW_CASE                   : 'case';

KW_TRY                    : 'try';

KW_CATCH                  : 'catch';

KW_DEFAULT                : 'default';

KW_THEN                   : 'then';

KW_ELSE                   : 'else';

KW_TYPESWITCH             : 'typeswitch';

KW_OR                     : 'or';

KW_EQ : 'eq';
KW_NE : 'ne';
KW_LT : 'lt';
KW_LE : 'le';
KW_GT : 'gt';
KW_GE : 'ge';
KW_MAP : 'map';
KW_ARRAY : 'array';

KW_AND                    : 'and';

Knot                    : 'not' ;

KW_TO                     : 'to' ;

KW_INSTANCE               : 'instance' ;

KW_OF                     : 'of' ;

Kstatically             : 'statically' ;

KW_IS                     : 'is' ;

KW_TREAT                  : 'treat';

KW_CAST                   : 'cast';

KW_CASTABLE               : 'castable';

KW_JSONIQ                 : 'jsoniq';

KW_ENCODING                 : 'encoding';

KW_VERSION                 : 'version';

KW_MODULE               : 'module';

KW_NAMESPACE            : 'namespace';

KW_BOUNDARY_SPACE        : 'boundary-space';

KW_PRESERVE              : 'preserve';

KW_STRIP                 : 'strip';

KW_BASE_URI              : 'base-uri';

KW_CONSTRUCTION          : 'construction';

KW_ORDER                 : 'order';

KW_COPY_NS               : 'copy-namespaces';

KW_NO_PRESERVE           : 'no-preserve';

KW_INHERIT               : 'inherit';

KW_NO_INHERIT            : 'no-inherit';

KW_DECIMAL_FORMAT       : 'decimal-format';

KW_EXTERNAL             : 'external';

KW_FUNCTION             : 'function';

KW_ELEMENT             : 'element';

KW_SCHEMA             : 'schema';

Ktrue                   : 'true';

Kfalse                  : 'false';

KW_TYPE                   : 'type';

KW_VALIDATE               : 'validate';

KW_LAX : 'lax';
KW_STRICT : 'strict';

KW_DECLARE                : 'declare';

KW_CONTEXT                : 'context';

KW_ITEM                   : 'item';

KW_VARIABLE               : 'variable';

KW_OPTION                : 'option';

KW_INSERT                 : 'insert';

KW_DELETE                 : 'delete';

KW_RENAME                 : 'rename';

KW_REPLACE                : 'replace';

KW_COPY                   : 'copy';

KW_MODIFY                 : 'modify';

KW_APPEND                 : 'append';

KW_INTO                   : 'into';

KW_VALUE                  : 'value';

KW_WITH                   : 'with';

KW_POSITION               : 'position';

KW_JSON                   : 'json';

KW_UPDATING               :  'updating';

Kcreate                 : 'create';

Kcollection             : 'collection';

KW_TABLE                  : 'table';

KW_DELTA_FILE              : 'delta-file';

KW_ICEBERG_TABLE           : 'iceberg-table';

Ktruncate               : 'truncate';

Kfirst                  : 'first';

KW_LAST                   : 'last';

Kfrom                   : 'from';

Kedit                   : 'edit';

Kafter                  : 'after';

Kbefore                 : 'before';


///////////////////////// XPath
KW_IMPORT                 : 'import';
Knamespace              : KW_NAMESPACE;
SLASH                  : '/';
DSLASH                 : '//';
AT              : '@';
KW_CHILD                  : 'child';
KW_DESCENDANT             : 'descendant';
KW_ATTRIBUTE              : 'attribute';
KW_SELF                   : 'self';
KW_DESCENDANT_OR_SELF     : 'descendant-or-self';
KW_FOLLOWING_SIBLING      : 'following-sibling';
KW_FOLLOWING              : 'following';
KW_PARENT                 : 'parent';
KW_ANCESTOR               : 'ancestor';
KW_PRECEDING_SIBLING      : 'preceding-sibling';
KW_PRECEDING              : 'preceding';
KW_ANCESTOR_OR_SELF       : 'ancestor-or-self';
KW_NODE                   : 'node';
KW_BINARY                 : 'binary';
KW_DOCUMENT               : 'document';
KW_DOCUMENT_NODE          : 'document-node';
KW_TEXT                   : 'text';
KW_PI                     : 'processing-instruction';
KW_NAMESPACE_NODE         : 'namespace-node';
KW_SCHEMA_ATTR       : 'schema-attribute';
KW_SCHEMA_ELEM         : 'schema-element';
Karray_node             : 'array-node';
Kboolean_node           : 'boolean-node';
Knull_node              : 'null-node';
Knumber_node            : 'number-node';
Kobject_node            : 'object-node';
KW_COMMENT                : 'comment';
KW_JSOUND : 'jsound';
KW_VERBOSE : 'verbose';
KW_COMPACT : 'compact';

///////////////////////// Scripting keywords
KW_BREAK                  : 'break' ;
KW_LOOP                   : 'loop' ;
KW_CONTINUE               : 'continue' ;
KW_EXIT                   : 'exit' ;
KW_RETURNING              : 'returning' ;
KW_WHILE                  : 'while' ;

STRING                  : '"' (ESC | ~ ["\\])* '"' | '\'' (ESCapos | ~ ['\\])* '\'';

fragment ESC            : '\\' (["\\/bfnrt] | UNICODE);
fragment ESCapos            : '\\' (['\\/bfnrt] | UNICODE);

fragment UNICODE        : 'u' HEX HEX HEX HEX;

fragment HEX            : [0-9a-fA-F];

NullLiteral             : 'null';

IntegerLiteral          : Digits ;

DecimalLiteral          : DOT Digits | Digits DOT [0-9]* ;

DoubleLiteral           : (DOT Digits | Digits (DOT [0-9]*)?) [eE] [+-]? Digits ;

fragment Digits         : [0-9]+ ;

WS                      : (' '|'\r'|'\t'|'\n') -> channel(HIDDEN);

NCName                  : NameStartChar NameChar*;

fragment NameStartChar  : [_a-zA-Z]
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
                        | MINUS
                        // no . in JSONIQ names | DOT
                        | [0-9]
                        | '\u00B7'
                        | '\u0300'..'\u036F'
                        | '\u203F'..'\u2040'
                        ;

XQComment               : LPAREN COLON (XQComment | LPAREN ~[:] | COLON ~[)] | ~[:(])* COLON+ RPAREN -> channel(HIDDEN);

ContentChar             :  ~["'{}<&]  ;


BracedURILiteral: 'Q' '{' (~[&{}])* '}' ;
URIQualifiedName: BracedURILiteral NCName ;

DFPropertyName          : 'decimal-separator'
                        | 'grouping-separator'
                        | 'infinity'
                        | 'minus-sign'
                        | 'NaN'
                        | 'percent'
                        | 'per-mille'
                        | 'zero-digit'
                        | 'digit'
                        | 'pattern-separator';
