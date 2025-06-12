lexer grammar XQueryLexer;

@header {
// Java header
package org.rumbledb.parser.xquery;
}


// Note: string syntax depends on syntactic context, so they are
// handled by the parser and not the lexer.

// Tokens declared but not defined
tokens {EscapeQuot, EscapeApos, DOUBLE_LBRACE, DOUBLE_RBRACE}

@members {
    ///
    /// FIELDS
    ///

    // for counting braces inside string literals
    private int bracesInside = 0;
}

IntegerLiteral: Digits ;
DecimalLiteral: ('.' Digits) | (Digits '.' [0-9]*) ;
DoubleLiteral: (('.' Digits) | (Digits ('.' [0-9]*)?)) [eE] [+-]? Digits ;

DFPropertyName: 'decimal-separator'
              | 'grouping-separator'
              | 'infinity'
              | 'minus-sign'
              | 'NaN'
              | 'percent'
              | 'per-mille'
              | 'zero-digit'
              | 'digit'
              | 'pattern-separator'
              | 'exponent-separator'
              ;

fragment
Digits: [0-9]+ ;

// This could be checked elsewhere: http://www.w3.org/TR/REC-xml/#wf-Legalchar
PredefinedEntityRef: '&' ('lt'|'gt'|'amp'|'quot'|'apos') ';' ;

// CharRef is additionally limited by http://www.w3.org/TR/REC-xml/#NT-Char,
CharRef: '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';' ;

// Escapes are handled as two Quot or two Apos tokens, to avoid maximal
// munch lexer ambiguity.
Quot        : '"' -> pushMode(QUOT_LITERAL_STRING);
Apos        : '\'' -> pushMode(APOS_LITERAL_STRING);

// XML-SPECIFIC

COMMENT : '<!--' ('-' ~[-] | ~[-])* '-->' ;
XMLDECL : '<?' [Xx] [Mm] [Ll] ([ \t\r\n] .*?)? '?>' ;
PI      :      '<?' NCName ([ \t\r\n] .*?)? '?>' ;
CDATA   :   '<![CDATA[' .*? ']]>' ;
PRAGMA  :  '(#' WS? (NCName ':')? NCName (WS .*?)? '#)' ;

// WHITESPACE

// S ::= (#x20 | #x9 | #xD | #xA)+
WS: [ \t\r\n]+ -> channel(HIDDEN);

// OPERATORS

EQUAL           : '=' ;
NOT_EQUAL       : '!=' ;
LPAREN          : '(' ;
RPAREN          : ')' ;
LBRACKET        : '[' ;
RBRACKET        : ']' ;
LBRACE          : '{' ;
RBRACE          : '}' ;

STAR            : '*' ;
PLUS            : '+' ;
MINUS           : '-' ;

COMMA           : ',' ;
DOT             : '.' ;
DDOT            : '..' ;
COLON           : ':' ;
COLON_EQ        : ':=' ;
SEMICOLON       : ';' ;

SLASH           : '/'  ;
DSLASH          : '//' ;
BACKSLASH       : '\\';
VBAR            : '|' ;

LANGLE          : '<' ;
RANGLE          : '>' ;

QUESTION        : '?' ;
AT              : '@' ;
DOLLAR          : '$' ;
MOD             : '%' ;
BANG            : '!' ;
HASH            : '#' ;
CARAT           : '^' ;

ARROW           : '=>' ;
GRAVE           : '`' ;
CONCATENATION   : '||' ;
TILDE           : '~' ;


// KEYWORDS

KW_ALLOWING:           'allowing';
KW_ANCESTOR:           'ancestor';
KW_ANCESTOR_OR_SELF:   'ancestor-or-self';
KW_AND:                'and';
KW_ARRAY:              'array';
KW_AS:                 'as';
KW_ASCENDING:          'ascending';
KW_AT:                 'at';
KW_ATTRIBUTE:          'attribute';
KW_BASE_URI:           'base-uri';
KW_BOUNDARY_SPACE:     'boundary-space';
KW_BINARY:             'binary';
KW_BY:                 'by';
KW_CASE:               'case';
KW_CAST:               'cast';
KW_CASTABLE:           'castable';
KW_CATCH:              'catch';
KW_CHILD:              'child';
KW_COLLATION:          'collation';
KW_COMMENT:            'comment';
KW_CONSTRUCTION:       'construction';
KW_CONTEXT:            'context';
KW_COPY_NS:            'copy-namespaces';
KW_COUNT:              'count';
KW_DECLARE:            'declare';
KW_DEFAULT:            'default';
KW_DESCENDANT:         'descendant';
KW_DESCENDANT_OR_SELF: 'descendant-or-self';
KW_DESCENDING:         'descending';
KW_DECIMAL_FORMAT:     'decimal-format' ;
KW_DIV:                'div';
KW_DOCUMENT:           'document';
KW_DOCUMENT_NODE:      'document-node';
KW_ELEMENT:            'element';
KW_ELSE:               'else';
KW_EMPTY:              'empty';
KW_EMPTY_SEQUENCE:     'empty-sequence';
KW_ENCODING:           'encoding';
KW_END:                'end';
KW_EQ:                 'eq';
KW_EVERY:              'every';
KW_EXCEPT:             'except';
KW_EXTERNAL:           'external';
KW_FOLLOWING:          'following';
KW_FOLLOWING_SIBLING:  'following-sibling';
KW_FOR:                'for';
KW_FUNCTION:           'function';
KW_GE:                 'ge';
KW_GREATEST:           'greatest';
KW_GROUP:              'group';
KW_GT:                 'gt';
KW_IDIV:               'idiv';
KW_IF:                 'if';
KW_IMPORT:             'import';
KW_IN:                 'in';
KW_INHERIT:            'inherit';
KW_INSTANCE:           'instance';
KW_INTERSECT:          'intersect';
KW_IS:                 'is';
KW_ITEM:               'item';
KW_LAX:                'lax';
KW_LE:                 'le';
KW_LEAST:              'least';
KW_LET:                'let';
KW_LT:                 'lt';
KW_MAP:                'map';
KW_MOD:                'mod';
KW_MODULE:             'module';
KW_NAMESPACE:          'namespace';
KW_NE:                 'ne';
KW_NEXT:               'next';
KW_NAMESPACE_NODE:     'namespace-node';
KW_NO_INHERIT:         'no-inherit';
KW_NO_PRESERVE:        'no-preserve';
KW_NODE:               'node';
KW_OF:                 'of';
KW_ONLY:               'only';
KW_OPTION:             'option';
KW_OR:                 'or';
KW_ORDER:              'order';
KW_ORDERED:            'ordered';
KW_ORDERING:           'ordering';
KW_PARENT:             'parent';
KW_PRECEDING:          'preceding';
KW_PRECEDING_SIBLING:  'preceding-sibling';
KW_PRESERVE:           'preserve';
KW_PREVIOUS:           'previous';
KW_PI:                 'processing-instruction';
KW_RETURN:             'return';
KW_SATISFIES:          'satisfies';
KW_SCHEMA:             'schema';
KW_SCHEMA_ATTR:        'schema-attribute';
KW_SCHEMA_ELEM:        'schema-element';
KW_SELF:               'self';
KW_SLIDING:            'sliding';
KW_SOME:               'some';
KW_STABLE:             'stable';
KW_START:              'start';
KW_STRICT:             'strict';
KW_STRIP:              'strip';
KW_SWITCH:             'switch';
KW_TEXT:               'text';
KW_THEN:               'then';
KW_TO:                 'to';
KW_TREAT:              'treat';
KW_TRY:                'try';
KW_TUMBLING:           'tumbling';
KW_TYPE:               'type';
KW_TYPESWITCH:         'typeswitch';
KW_UNION:              'union';
KW_UNORDERED:          'unordered';
KW_UPDATE:             'update';
KW_VALIDATE:           'validate';
KW_VARIABLE:           'variable';
KW_VERSION:            'version';
KW_WHEN:               'when';
KW_WHERE:              'where';
KW_WINDOW:             'window';
KW_XQUERY:             'xquery';

// MarkLogic JSON computed constructor

KW_ARRAY_NODE:         'array-node';
KW_BOOLEAN_NODE:       'boolean-node';
KW_NULL_NODE:          'null-node';
KW_NUMBER_NODE:        'number-node';
KW_OBJECT_NODE:        'object-node';


// eXist-db update keywords

KW_REPLACE:            'replace';
KW_WITH:               'with';
KW_VALUE:              'value';
KW_INSERT:             'insert';
KW_INTO:               'into';
KW_DELETE:             'delete';
KW_RENAME:             'rename';

// XQuery Scripting Extension keywords
// TODO: add them as ok for function names.
// if tests break bc of this, move to not ok.
KW_BREAK:              'break';
KW_LOOP:               'loop';
KW_CONTINUE:           'continue';
KW_EXIT:               'exit';
KW_RETURNING:          'returning';
// KW_SELECT:             'select';
KW_WHILE:              'while';

// NAMES

// Moved URIQualifiedName here to gather all names
URIQualifiedName: 'Q' '{' (PredefinedEntityRef | CharRef | ~[&{}])* '}' NCName ;

// We create these basic variants in order to honor ws:explicit in some basic cases
FullQName: NCName ':' NCName ;
NCNameWithLocalWildcard:  NCName ':' '*' ;
NCNameWithPrefixWildcard: '*' ':' NCName ; 

// According to http://www.w3.org/TR/REC-xml-names/#NT-NCName,
// it is 'an XML Name, minus the ":"'
NCName: NameStartChar NameChar*;

fragment
NameStartChar: [_a-zA-Z]
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

fragment
NameChar: NameStartChar
        | '-'
        | '.'
        | [0-9]
        | '\u00A1'..'\u00BF'
        | '\u0300'..'\u036F'
        | '\u203F'..'\u2040'
        ;


// XQuery comments
//
// Element content can have an unbalanced set of (: :) pairs (as XQuery
// comments do not really exist inside them), so it is better to treat
// this as a single token with a recursive rule, rather than using a
// mode.

XQDOC_COMMENT_START: '(:~' ;
XQDOC_COMMENT_END: ':'+ ')' ;

XQComment: '(' ':' ~'~' (XQComment | '(' ~[:] | ':' ~[)] | ~[:(])* ':'* ':'+ ')' -> channel(HIDDEN);

CHAR: ( '\t' | '\n' | '\r' | '\u0020'..'\u0039' | '\u003B'..'\uD7FF' | '\uE000'..'\uFFFD' ) ;

// These rules have been added to enter and exit the String mode
ENTER_STRING        : GRAVE GRAVE LBRACKET -> pushMode(STRING_MODE);
EXIT_INTERPOLATION  : RBRACE GRAVE -> popMode;

// This is an intersection of:
//
// [148] ElementContentChar  ::= Char - [{}<&]
// [149] QuotAttrContentChar ::= Char - ["{}<&]
// [150] AposAttrContentChar ::= Char - ['{}<&]
//
// Therefore, we would have something like:
//
// ElementContentChar  ::= ContentChar | ["']
// QuotAttrContentChar ::= ContentChar | [']
// AposAttrContentChar ::= ContentChar | ["]
//
// This rule needs to be the very last one, so it has the lowest priority.

ContentChar:  ~["'{}<&] ;


// Lexical modes to parse Strings
mode STRING_MODE;

BASIC_CHAR          : ( '\t' 
                        | '\u000A'
                        | '\u000D'
                        | '\u0020'..'\u005C'                        
                        | '\u005E'..'\u005F'
                        | '\u0061'..'\u007A'
                        | '\u007C'..'\uD7FF'
                        | '\uE000'..'\uFFFD'
                        | '\u{10000}'..'\u{10FFFF}' ) ;


GRAVE_STRING        : '`' -> type(GRAVE);
RBRACKET_STRING     : ']' -> type(RBRACKET);
LBRACE_STRING       : '{' -> type(LBRACE);

ENTER_INTERPOLATION : GRAVE LBRACE -> pushMode(DEFAULT_MODE);
EXIT_STRING         : RBRACKET GRAVE GRAVE -> popMode;

mode QUOT_LITERAL_STRING;

EscapeQuot_QuotString           : '""' -> type(EscapeQuot);
Quot_QuotString                 : '"' -> type(Quot), popMode;

DOUBLE_LBRACE_QuotString        : '{{' -> type(DOUBLE_LBRACE);
DOUBLE_RBRACE_QuotString        : '}}' -> type(DOUBLE_RBRACE);
LBRACE_QuotString               : '{' -> type(LBRACE), pushMode(STRING_INTERPOLATION_MODE_QUOT);
RBRACE_QuotString               : '}' -> type(RBRACE);
PredefinedEntityRef_QuotString  : '&' ('lt'|'gt'|'amp'|'quot'|'apos') ';'  -> type(PredefinedEntityRef);
CharRef_QuotString              : ('&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';') -> type(CharRef);
ContentChar_QuotString          : ~["&{}] -> type(ContentChar);

mode APOS_LITERAL_STRING;

EscapeApos_AposString           : '\'\'' -> type(EscapeApos);
Apos_AposString                 : '\'' -> type(Apos), popMode;

DOUBLE_LBRACE_AposString        : '{{' -> type(DOUBLE_LBRACE);
DOUBLE_RBRACE_AposString        : '}}' -> type(DOUBLE_RBRACE) ;
LBRACE_AposString               : '{' -> type(LBRACE), pushMode(STRING_INTERPOLATION_MODE_APOS);
RBRACE_AposString               : '}' -> type(RBRACE);
PredefinedEntityRef_AposString  : '&' ('lt'|'gt'|'amp'|'quot'|'apos') ';'  -> type(PredefinedEntityRef);
CharRef_AposString              : ('&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';') -> type(CharRef);
ContentChar_AposString          : ~['&{}] -> type(ContentChar);

mode STRING_INTERPOLATION_MODE_QUOT;

INT_QUOT_IntegerLiteral: Digits -> type(IntegerLiteral);
INT_QUOT_DecimalLiteral: ('.' Digits | Digits '.' [0-9]*) -> type(DecimalLiteral) ;
INT_QUOT_DoubleLiteral: ('.' Digits | Digits ('.' [0-9]*)?) [eE] [+-]? Digits -> type(DoubleLiteral);

INT_QUOT_DFPropertyName: ('decimal-separator'
              | 'grouping-separator'
              | 'infinity'
              | 'minus-sign'
              | 'NaN'
              | 'percent'
              | 'per-mille'
              | 'zero-digit'
              | 'digit'
              | 'pattern-separator'
              | 'exponent-separator' )
              -> type(DFPropertyName);

// This could be checked elsewhere: http://www.w3.org/TR/REC-xml/#wf-Legalchar
INT_QUOT_PredefinedEntityRef: '&' ('lt'|'gt'|'amp'|'quot'|'apos') ';' -> type(PredefinedEntityRef);

// CharRef is additionally limited by http://www.w3.org/TR/REC-xml/#NT-Char,
INT_QUOT_CharRef: ('&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';') -> type(CharRef);

INT_QUOT_EscapeQuot  : '""' -> type(EscapeQuot);

// Escapes are handled as two Quot or two Apos tokens, to avoid maximal
// munch lexer ambiguity.
INT_QUOT_Apos: '\'' -> pushMode(APOS_LITERAL_STRING), type(Apos);
// there cannot be two or more QUOT_LITERAL_STRING nested inside one another
// so this guarantees that we are going back to DEFAULT_MODE
INT_QUOT_Quot: '"'  -> type(Quot), popMode, popMode;

// XML-SPECIFIC

INT_QUOT_COMMENT : '<!--' ('-' ~[-] | ~[-])* '-->'  -> type(COMMENT);
INT_QUOT_XMLDECL : '<?' [Xx] [Mm] [Ll] ([ \t\r\n] .*?)? '?>'  -> type(XMLDECL);
INT_QUOT_PI      :      '<?' NCName ([ \t\r\n] .*?)? '?>'  -> type(PI);
INT_QUOT_CDATA   :   '<![CDATA[' .*? ']]>'  -> type(CDATA);
INT_QUOT_PRAGMA  :  '(#' WS? (NCName ':')? NCName (WS .*?)? '#)' -> type(PRAGMA);

// WHITESPACE

// S ::= (#x20 | #x9 | #xD | #xA)+
INT_QUOT_WS: [ \t\r\n]+ -> channel(HIDDEN), type(WS);

// OPERATORS

INT_QUOT_EQUAL           : '=' -> type(EQUAL) ;
INT_QUOT_NOT_EQUAL       : '!=' -> type(NOT_EQUAL);
INT_QUOT_LPAREN          : '(' -> type(LPAREN);
INT_QUOT_RPAREN          : ')' -> type(RPAREN);
INT_QUOT_LBRACKET        : '[' -> type(LBRACKET);
INT_QUOT_RBRACKET        : ']' -> type(RBRACKET);
INT_QUOT_LBRACE          : '{' {this.bracesInside++;} -> type(LBRACE);
INT_QUOT_RBRACE_EXIT     : {this.bracesInside == 0}? '}' -> type(RBRACE), popMode ;
INT_QUOT_RBRACE          : {this.bracesInside > 0}? '}' {this.bracesInside--;} -> type(RBRACE) ;

INT_QUOT_STAR            : '*' -> type(STAR);
INT_QUOT_PLUS            : '+' -> type(PLUS);
INT_QUOT_MINUS           : '-' -> type(MINUS);

INT_QUOT_COMMA           : ',' -> type(COMMA);
INT_QUOT_DOT             : '.' -> type(DOT);
INT_QUOT_DDOT            : '..' -> type(DDOT);
INT_QUOT_COLON           : ':' -> type(COLON);
INT_QUOT_COLON_EQ        : ':=' -> type(COLON_EQ);
INT_QUOT_SEMICOLON       : ';' -> type(SEMICOLON);

INT_QUOT_SLASH           : '/' -> type(SLASH);
INT_QUOT_DSLASH          : '//' -> type(DSLASH);
INT_QUOT_BACKSLASH       : '\\' -> type(BACKSLASH);
INT_QUOT_VBAR            : '|' -> type(VBAR);

INT_QUOT_LANGLE          : '<' -> type(LANGLE);
INT_QUOT_RANGLE          : '>' -> type(RANGLE);

INT_QUOT_QUESTION        : '?' -> type(QUESTION);
INT_QUOT_AT              : '@' -> type(AT);
INT_QUOT_DOLLAR          : '$' -> type(DOLLAR);
INT_QUOT_MOD             : '%' -> type(MOD);
INT_QUOT_BANG            : '!' -> type(BANG);
INT_QUOT_HASH            : '#' -> type(HASH);
INT_QUOT_CARAT           : '^' -> type(CARAT);

INT_QUOT_ARROW           : '=>' -> type(ARROW);
INT_QUOT_GRAVE           : '`' -> type(GRAVE);
INT_QUOT_CONCATENATION   : '||' -> type(CONCATENATION);
INT_QUOT_TILDE           : '~' -> type(TILDE);


// KEYWORDS

INT_QUOT_KW_ALLOWING:           'allowing' -> type(KW_ALLOWING);
INT_QUOT_KW_ANCESTOR:           'ancestor' -> type(KW_ANCESTOR);
INT_QUOT_KW_ANCESTOR_OR_SELF:   'ancestor-or-self' -> type(KW_ANCESTOR_OR_SELF);
INT_QUOT_KW_AND:                'and' -> type(KW_AND);
INT_QUOT_KW_ARRAY:              'array' -> type(KW_ARRAY);
INT_QUOT_KW_AS:                 'as' -> type(KW_AS);
INT_QUOT_KW_ASCENDING:          'ascending' -> type(KW_ASCENDING);
INT_QUOT_KW_AT:                 'at' -> type(KW_AT);
INT_QUOT_KW_ATTRIBUTE:          'attribute' -> type(KW_ATTRIBUTE);
INT_QUOT_KW_BASE_URI:           'base-uri' -> type(KW_BASE_URI);
INT_QUOT_KW_BOUNDARY_SPACE:     'boundary-space' -> type(KW_BOUNDARY_SPACE);
INT_QUOT_KW_BINARY:             'binary' -> type(KW_BINARY);
INT_QUOT_KW_BY:                 'by' -> type(KW_BY);
INT_QUOT_KW_CASE:               'case' -> type(KW_CASE);
INT_QUOT_KW_CAST:               'cast' -> type(KW_CAST);
INT_QUOT_KW_CASTABLE:           'castable' -> type(KW_CASTABLE);
INT_QUOT_KW_CATCH:              'catch' -> type(KW_CATCH);
INT_QUOT_KW_CHILD:              'child' -> type(KW_CHILD);
INT_QUOT_KW_COLLATION:          'collation' -> type(KW_COLLATION);
INT_QUOT_KW_COMMENT:            'comment' -> type(KW_COMMENT);
INT_QUOT_KW_CONSTRUCTION:       'construction' -> type(KW_CONSTRUCTION);
INT_QUOT_KW_CONTEXT:            'context' -> type(KW_CONTEXT);
INT_QUOT_KW_COPY_NS:            'copy-namespaces' -> type(KW_COPY_NS);
INT_QUOT_KW_COUNT:              'count' -> type(KW_COUNT);
INT_QUOT_KW_DECLARE:            'declare' -> type(KW_DECLARE);
INT_QUOT_KW_DEFAULT:            'default' -> type(KW_DEFAULT);
INT_QUOT_KW_DESCENDANT:         'descendant' -> type(KW_DESCENDANT);
INT_QUOT_KW_DESCENDANT_OR_SELF: 'descendant-or-self' -> type(KW_DESCENDANT_OR_SELF);
INT_QUOT_KW_DESCENDING:         'descending' -> type(KW_DESCENDING);
INT_QUOT_KW_DECIMAL_FORMAT:     'decimal-format'  -> type(KW_DECIMAL_FORMAT);
INT_QUOT_KW_DIV:                'div' -> type(KW_DIV);
INT_QUOT_KW_DOCUMENT:           'document' -> type(KW_DOCUMENT);
INT_QUOT_KW_DOCUMENT_NODE:      'document-node' -> type(KW_DOCUMENT_NODE);
INT_QUOT_KW_ELEMENT:            'element' -> type(KW_ELEMENT);
INT_QUOT_KW_ELSE:               'else' -> type(KW_ELSE);
INT_QUOT_KW_EMPTY:              'empty' -> type(KW_EMPTY);
INT_QUOT_KW_EMPTY_SEQUENCE:     'empty-sequence' -> type(KW_EMPTY_SEQUENCE);
INT_QUOT_KW_ENCODING:           'encoding' -> type(KW_ENCODING);
INT_QUOT_KW_END:                'end' -> type(KW_END);
INT_QUOT_KW_EQ:                 'eq' -> type(KW_EQ);
INT_QUOT_KW_EVERY:              'every' -> type(KW_EVERY);
INT_QUOT_KW_EXCEPT:             'except' -> type(KW_EXCEPT);
INT_QUOT_KW_EXTERNAL:           'external' -> type(KW_EXTERNAL);
INT_QUOT_KW_FOLLOWING:          'following' -> type(KW_FOLLOWING);
INT_QUOT_KW_FOLLOWING_SIBLING:  'following-sibling' -> type(KW_FOLLOWING_SIBLING);
INT_QUOT_KW_FOR:                'for' -> type(KW_FOR);
INT_QUOT_KW_FUNCTION:           'function' -> type(KW_FUNCTION);
INT_QUOT_KW_GE:                 'ge' -> type(KW_GE);
INT_QUOT_KW_GREATEST:           'greatest' -> type(KW_GREATEST);
INT_QUOT_KW_GROUP:              'group' -> type(KW_GROUP);
INT_QUOT_KW_GT:                 'gt' -> type(KW_GT);
INT_QUOT_KW_IDIV:               'idiv' -> type(KW_IDIV);
INT_QUOT_KW_IF:                 'if' -> type(KW_IF);
INT_QUOT_KW_IMPORT:             'import' -> type(KW_IMPORT);
INT_QUOT_KW_IN:                 'in' -> type(KW_IN);
INT_QUOT_KW_INHERIT:            'inherit' -> type(KW_INHERIT);
INT_QUOT_KW_INSTANCE:           'instance' -> type(KW_INSTANCE);
INT_QUOT_KW_INTERSECT:          'intersect' -> type(KW_INTERSECT);
INT_QUOT_KW_IS:                 'is' -> type(KW_IS);
INT_QUOT_KW_ITEM:               'item' -> type(KW_ITEM);
INT_QUOT_KW_LAX:                'lax' -> type(KW_LAX);
INT_QUOT_KW_LE:                 'le' -> type(KW_LE);
INT_QUOT_KW_LEAST:              'least' -> type(KW_LEAST);
INT_QUOT_KW_LET:                'let' -> type(KW_LET);
INT_QUOT_KW_LT:                 'lt' -> type(KW_LT);
INT_QUOT_KW_MAP:                'map' -> type(KW_MAP);
INT_QUOT_KW_MOD:                'mod' -> type(KW_MOD);
INT_QUOT_KW_MODULE:             'module' -> type(KW_MODULE);
INT_QUOT_KW_NAMESPACE:          'namespace' -> type(KW_NAMESPACE);
INT_QUOT_KW_NE:                 'ne' -> type(KW_NE);
INT_QUOT_KW_NEXT:               'next' -> type(KW_NEXT);
INT_QUOT_KW_NAMESPACE_NODE:     'namespace-node' -> type(KW_NAMESPACE_NODE);
INT_QUOT_KW_NO_INHERIT:         'no-inherit' -> type(KW_NO_INHERIT);
INT_QUOT_KW_NO_PRESERVE:        'no-preserve' -> type(KW_NO_PRESERVE);
INT_QUOT_KW_NODE:               'node' -> type(KW_NODE);
INT_QUOT_KW_OF:                 'of' -> type(KW_OF);
INT_QUOT_KW_ONLY:               'only' -> type(KW_ONLY);
INT_QUOT_KW_OPTION:             'option' -> type(KW_OPTION);
INT_QUOT_KW_OR:                 'or' -> type(KW_OR);
INT_QUOT_KW_ORDER:              'order' -> type(KW_ORDER);
INT_QUOT_KW_ORDERED:            'ordered' -> type(KW_ORDERED);
INT_QUOT_KW_ORDERING:           'ordering' -> type(KW_ORDERING);
INT_QUOT_KW_PARENT:             'parent' -> type(KW_PARENT);
INT_QUOT_KW_PRECEDING:          'preceding' -> type(KW_PRECEDING);
INT_QUOT_KW_PRECEDING_SIBLING:  'preceding-sibling' -> type(KW_PRECEDING_SIBLING);
INT_QUOT_KW_PRESERVE:           'preserve' -> type(KW_PRESERVE);
INT_QUOT_KW_PREVIOUS:           'previous' -> type(KW_PREVIOUS);
INT_QUOT_KW_PI:                 'processing-instruction' -> type(KW_PI);
INT_QUOT_KW_RETURN:             'return' -> type(KW_RETURN);
INT_QUOT_KW_SATISFIES:          'satisfies' -> type(KW_SATISFIES);
INT_QUOT_KW_SCHEMA:             'schema' -> type(KW_SCHEMA);
INT_QUOT_KW_SCHEMA_ATTR:        'schema-attribute' -> type(KW_SCHEMA_ATTR);
INT_QUOT_KW_SCHEMA_ELEM:        'schema-element' -> type(KW_SCHEMA_ELEM);
INT_QUOT_KW_SELF:               'self' -> type(KW_SELF);
INT_QUOT_KW_SLIDING:            'sliding' -> type(KW_SLIDING);
INT_QUOT_KW_SOME:               'some' -> type(KW_SOME);
INT_QUOT_KW_STABLE:             'stable' -> type(KW_STABLE);
INT_QUOT_KW_START:              'start' -> type(KW_START);
INT_QUOT_KW_STRICT:             'strict' -> type(KW_STRICT);
INT_QUOT_KW_STRIP:              'strip' -> type(KW_STRIP);
INT_QUOT_KW_SWITCH:             'switch' -> type(KW_SWITCH);
INT_QUOT_KW_TEXT:               'text' -> type(KW_TEXT);
INT_QUOT_KW_THEN:               'then' -> type(KW_THEN);
INT_QUOT_KW_TO:                 'to' -> type(KW_TO);
INT_QUOT_KW_TREAT:              'treat' -> type(KW_TREAT);
INT_QUOT_KW_TRY:                'try' -> type(KW_TRY);
INT_QUOT_KW_TUMBLING:           'tumbling' -> type(KW_TUMBLING);
INT_QUOT_KW_TYPE:               'type' -> type(KW_TYPE);
INT_QUOT_KW_TYPESWITCH:         'typeswitch' -> type(KW_TYPESWITCH);
INT_QUOT_KW_UNION:              'union' -> type(KW_UNION);
INT_QUOT_KW_UNORDERED:          'unordered' -> type(KW_UNORDERED);
INT_QUOT_KW_UPDATE:             'update' -> type(KW_UPDATE);
INT_QUOT_KW_VALIDATE:           'validate' -> type(KW_VALIDATE);
INT_QUOT_KW_VARIABLE:           'variable' -> type(KW_VARIABLE);
INT_QUOT_KW_VERSION:            'version' -> type(KW_VERSION);
INT_QUOT_KW_WHEN:               'when' -> type(KW_WHEN);
INT_QUOT_KW_WHERE:              'where' -> type(KW_WHERE);
INT_QUOT_KW_WINDOW:             'window' -> type(KW_WINDOW);
INT_QUOT_KW_XQUERY:             'xquery' -> type(KW_XQUERY);

// MarkLogic JSON computed constructor

INT_QUOT_KW_ARRAY_NODE:         'array-node' -> type(KW_ARRAY_NODE);
INT_QUOT_KW_BOOLEAN_NODE:       'boolean-node' -> type(KW_BOOLEAN_NODE);
INT_QUOT_KW_NULL_NODE:          'null-node' -> type(KW_NULL_NODE);
INT_QUOT_KW_NUMBER_NODE:        'number-node' -> type(KW_NUMBER_NODE);
INT_QUOT_KW_OBJECT_NODE:        'object-node' -> type(KW_OBJECT_NODE);


// eXist-db update keywords

INT_QUOT_KW_REPLACE:            'replace' -> type(KW_REPLACE);
INT_QUOT_KW_WITH:               'with' -> type(KW_WITH);
INT_QUOT_KW_VALUE:              'value' -> type(KW_VALUE);
INT_QUOT_KW_INSERT:             'insert' -> type(KW_INSERT);
INT_QUOT_KW_INTO:               'into' -> type(KW_INTO);
INT_QUOT_KW_DELETE:             'delete' -> type(KW_DELETE);
INT_QUOT_KW_RENAME:             'rename' -> type(KW_RENAME);


// NAMES

INT_QUOT_URIQualifiedName: 'Q' '{' (PredefinedEntityRef | CharRef | ~[&{}])* '}' NCName -> type(URIQualifiedName);
INT_QUOT_FullQName: NCName ':' NCName -> type(FullQName);
INT_QUOT_NCNameWithLocalWildcard:  NCName ':' '*' -> type(NCNameWithLocalWildcard);
INT_QUOT_NCNameWithPrefixWildcard: '*' ':' NCName -> type(NCNameWithPrefixWildcard); 

INT_QUOT_NCName: NameStartChar NameChar* -> type(NCName);

INT_QUOT_XQDOC_COMMENT_START: '(:~' -> type(XQDOC_COMMENT_START);
INT_QUOT_XQDOC_COMMENT_END: ':'+ ')' -> type(XQDOC_COMMENT_END);

INT_QUOT_XQComment: '(' ':' ~'~' (XQComment | '(' ~[:] | ':' ~[)] | ~[:(])* ':'* ':'+ ')' -> channel(HIDDEN), type(XQComment);

INT_QUOT_CHAR: ( '\t' | '\n' | '\r' | '\u0020'..'\u0039' | '\u003B'..'\uD7FF' | '\uE000'..'\uFFFD' ) -> type(CHAR);


INT_QUOT_ENTER_STRING        : GRAVE GRAVE LBRACKET -> pushMode(STRING_MODE), type(ENTER_STRING);
INT_QUOT_EXIT_INTERPOLATION  : RBRACE GRAVE -> popMode, type(ENTER_INTERPOLATION);

INT_ContentChar:  ~["'{}<&] -> type(ContentChar);

mode STRING_INTERPOLATION_MODE_APOS;

INT_APOS_IntegerLiteral: Digits -> type(IntegerLiteral);
INT_APOS_DecimalLiteral: ('.' Digits | Digits '.' [0-9]*) -> type(DecimalLiteral) ;
INT_APOS_DoubleLiteral: ('.' Digits | Digits ('.' [0-9]*)?) [eE] [+-]? Digits -> type(DoubleLiteral);

INT_APOS_DFPropertyName: ('decimal-separator'
              | 'grouping-separator'
              | 'infinity'
              | 'minus-sign'
              | 'NaN'
              | 'percent'
              | 'per-mille'
              | 'zero-digit'
              | 'digit'
              | 'pattern-separator'
              | 'exponent-separator' )
              -> type(DFPropertyName);

// This could be checked elsewhere: http://www.w3.org/TR/REC-xml/#wf-Legalchar
INT_APOS_PredefinedEntityRef: '&' ('lt'|'gt'|'amp'|'quot'|'apos') ';' -> type(PredefinedEntityRef);

// CharRef is additionally limited by http://www.w3.org/TR/REC-xml/#NT-Char,
INT_APOS_CharRef: ('&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';') -> type(CharRef);

INT_APOS_EscapeApos  : '\'\'' -> type(EscapeApos);

// Escapes are handled as two Quot or two Apos tokens, to avoid maximal
// munch lexer ambiguity.
INT_APOS_Quot: '"'  -> pushMode(QUOT_LITERAL_STRING), type(Quot);
// there cannot be two or more APOS_LITERAL_STRING nested inside one another
// so this guarantees that we are going back to DEFAULT_MODE
INT_APOS_Apos: '\'' -> type(Apos), popMode, popMode;

// XML-SPECIFIC

INT_APOS_COMMENT : '<!--' ('-' ~[-] | ~[-])* '-->'  -> type(COMMENT);
INT_APOS_XMLDECL : '<?' [Xx] [Mm] [Ll] ([ \t\r\n] .*?)? '?>'  -> type(XMLDECL);
INT_APOS_PI      :      '<?' NCName ([ \t\r\n] .*?)? '?>'  -> type(PI);
INT_APOS_CDATA   :   '<![CDATA[' .*? ']]>'  -> type(CDATA);
INT_APOS_PRAGMA  :  '(#' WS? (NCName ':')? NCName (WS .*?)? '#)' -> type(PRAGMA);

// WHITESPACE

// S ::= (#x20 | #x9 | #xD | #xA)+
INT_APOS_WS: [ \t\r\n]+ -> channel(HIDDEN), type(WS);

// OPERATORS

INT_APOS_EQUAL           : '=' -> type(EQUAL) ;
INT_APOS_NOT_EQUAL       : '!=' -> type(NOT_EQUAL);
INT_APOS_LPAREN          : '(' -> type(LPAREN);
INT_APOS_RPAREN          : ')' -> type(RPAREN);
INT_APOS_LBRACKET        : '[' -> type(LBRACKET);
INT_APOS_RBRACKET        : ']' -> type(RBRACKET);
//INT_APOS_LBRACE          : '{' -> type(LBRACE);
INT_APOS_LBRACE          : '{' {this.bracesInside++;} -> type(LBRACE);
INT_APOS_RBRACE_EXIT     : {this.bracesInside == 0}? '}' -> type(RBRACE), popMode ;
INT_APOS_RBRACE          : {this.bracesInside > 0}? '}' {this.bracesInside--;} -> type(RBRACE) ;
//INT_APOS_RBRACE          : '}' -> type(RBRACE), popMode ;

INT_APOS_STAR            : '*' -> type(STAR);
INT_APOS_PLUS            : '+' -> type(PLUS);
INT_APOS_MINUS           : '-' -> type(MINUS);

INT_APOS_COMMA           : ',' -> type(COMMA);
INT_APOS_DOT             : '.' -> type(DOT);
INT_APOS_DDOT            : '..' -> type(DDOT);
INT_APOS_COLON           : ':' -> type(COLON);
INT_APOS_COLON_EQ        : ':=' -> type(COLON_EQ);
INT_APOS_SEMICOLON       : ';' -> type(SEMICOLON);

INT_APOS_SLASH           : '/' -> type(SLASH);
INT_APOS_DSLASH          : '//' -> type(DSLASH);
INT_APOS_BACKSLASH       : '\\' -> type(BACKSLASH);
INT_APOS_VBAR            : '|' -> type(VBAR);

INT_APOS_LANGLE          : '<' -> type(LANGLE);
INT_APOS_RANGLE          : '>' -> type(RANGLE);

INT_APOS_QUESTION        : '?' -> type(QUESTION);
INT_APOS_AT              : '@' -> type(AT);
INT_APOS_DOLLAR          : '$' -> type(DOLLAR);
INT_APOS_MOD             : '%' -> type(MOD);
INT_APOS_BANG            : '!' -> type(BANG);
INT_APOS_HASH            : '#' -> type(HASH);
INT_APOS_CARAT           : '^' -> type(CARAT);

INT_APOS_ARROW           : '=>' -> type(ARROW);
INT_APOS_GRAVE           : '`' -> type(GRAVE);
INT_APOS_CONCATENATION   : '||' -> type(CONCATENATION);
INT_APOS_TILDE           : '~' -> type(TILDE);


// KEYWORDS

INT_APOS_KW_ALLOWING:           'allowing' -> type(KW_ALLOWING);
INT_APOS_KW_ANCESTOR:           'ancestor' -> type(KW_ANCESTOR);
INT_APOS_KW_ANCESTOR_OR_SELF:   'ancestor-or-self' -> type(KW_ANCESTOR_OR_SELF);
INT_APOS_KW_AND:                'and' -> type(KW_AND);
INT_APOS_KW_ARRAY:              'array' -> type(KW_ARRAY);
INT_APOS_KW_AS:                 'as' -> type(KW_AS);
INT_APOS_KW_ASCENDING:          'ascending' -> type(KW_ASCENDING);
INT_APOS_KW_AT:                 'at' -> type(KW_AT);
INT_APOS_KW_ATTRIBUTE:          'attribute' -> type(KW_ATTRIBUTE);
INT_APOS_KW_BASE_URI:           'base-uri' -> type(KW_BASE_URI);
INT_APOS_KW_BOUNDARY_SPACE:     'boundary-space' -> type(KW_BOUNDARY_SPACE);
INT_APOS_KW_BINARY:             'binary' -> type(KW_BINARY);
INT_APOS_KW_BY:                 'by' -> type(KW_BY);
INT_APOS_KW_CASE:               'case' -> type(KW_CASE);
INT_APOS_KW_CAST:               'cast' -> type(KW_CAST);
INT_APOS_KW_CASTABLE:           'castable' -> type(KW_CASTABLE);
INT_APOS_KW_CATCH:              'catch' -> type(KW_CATCH);
INT_APOS_KW_CHILD:              'child' -> type(KW_CHILD);
INT_APOS_KW_COLLATION:          'collation' -> type(KW_COLLATION);
INT_APOS_KW_COMMENT:            'comment' -> type(KW_COMMENT);
INT_APOS_KW_CONSTRUCTION:       'construction' -> type(KW_CONSTRUCTION);
INT_APOS_KW_CONTEXT:            'context' -> type(KW_CONTEXT);
INT_APOS_KW_COPY_NS:            'copy-namespaces' -> type(KW_COPY_NS);
INT_APOS_KW_COUNT:              'count' -> type(KW_COUNT);
INT_APOS_KW_DECLARE:            'declare' -> type(KW_DECLARE);
INT_APOS_KW_DEFAULT:            'default' -> type(KW_DEFAULT);
INT_APOS_KW_DESCENDANT:         'descendant' -> type(KW_DESCENDANT);
INT_APOS_KW_DESCENDANT_OR_SELF: 'descendant-or-self' -> type(KW_DESCENDANT_OR_SELF);
INT_APOS_KW_DESCENDING:         'descending' -> type(KW_DESCENDING);
INT_APOS_KW_DECIMAL_FORMAT:     'decimal-format'  -> type(KW_DECIMAL_FORMAT);
INT_APOS_KW_DIV:                'div' -> type(KW_DIV);
INT_APOS_KW_DOCUMENT:           'document' -> type(KW_DOCUMENT);
INT_APOS_KW_DOCUMENT_NODE:      'document-node' -> type(KW_DOCUMENT_NODE);
INT_APOS_KW_ELEMENT:            'element' -> type(KW_ELEMENT);
INT_APOS_KW_ELSE:               'else' -> type(KW_ELSE);
INT_APOS_KW_EMPTY:              'empty' -> type(KW_EMPTY);
INT_APOS_KW_EMPTY_SEQUENCE:     'empty-sequence' -> type(KW_EMPTY_SEQUENCE);
INT_APOS_KW_ENCODING:           'encoding' -> type(KW_ENCODING);
INT_APOS_KW_END:                'end' -> type(KW_END);
INT_APOS_KW_EQ:                 'eq' -> type(KW_EQ);
INT_APOS_KW_EVERY:              'every' -> type(KW_EVERY);
INT_APOS_KW_EXCEPT:             'except' -> type(KW_EXCEPT);
INT_APOS_KW_EXTERNAL:           'external' -> type(KW_EXTERNAL);
INT_APOS_KW_FOLLOWING:          'following' -> type(KW_FOLLOWING);
INT_APOS_KW_FOLLOWING_SIBLING:  'following-sibling' -> type(KW_FOLLOWING_SIBLING);
INT_APOS_KW_FOR:                'for' -> type(KW_FOR);
INT_APOS_KW_FUNCTION:           'function' -> type(KW_FUNCTION);
INT_APOS_KW_GE:                 'ge' -> type(KW_GE);
INT_APOS_KW_GREATEST:           'greatest' -> type(KW_GREATEST);
INT_APOS_KW_GROUP:              'group' -> type(KW_GROUP);
INT_APOS_KW_GT:                 'gt' -> type(KW_GT);
INT_APOS_KW_IDIV:               'idiv' -> type(KW_IDIV);
INT_APOS_KW_IF:                 'if' -> type(KW_IF);
INT_APOS_KW_IMPORT:             'import' -> type(KW_IMPORT);
INT_APOS_KW_IN:                 'in' -> type(KW_IN);
INT_APOS_KW_INHERIT:            'inherit' -> type(KW_INHERIT);
INT_APOS_KW_INSTANCE:           'instance' -> type(KW_INSTANCE);
INT_APOS_KW_INTERSECT:          'intersect' -> type(KW_INTERSECT);
INT_APOS_KW_IS:                 'is' -> type(KW_IS);
INT_APOS_KW_ITEM:               'item' -> type(KW_ITEM);
INT_APOS_KW_LAX:                'lax' -> type(KW_LAX);
INT_APOS_KW_LE:                 'le' -> type(KW_LE);
INT_APOS_KW_LEAST:              'least' -> type(KW_LEAST);
INT_APOS_KW_LET:                'let' -> type(KW_LET);
INT_APOS_KW_LT:                 'lt' -> type(KW_LT);
INT_APOS_KW_MAP:                'map' -> type(KW_MAP);
INT_APOS_KW_MOD:                'mod' -> type(KW_MOD);
INT_APOS_KW_MODULE:             'module' -> type(KW_MODULE);
INT_APOS_KW_NAMESPACE:          'namespace' -> type(KW_NAMESPACE);
INT_APOS_KW_NE:                 'ne' -> type(KW_NE);
INT_APOS_KW_NEXT:               'next' -> type(KW_NEXT);
INT_APOS_KW_NAMESPACE_NODE:     'namespace-node' -> type(KW_NAMESPACE_NODE);
INT_APOS_KW_NO_INHERIT:         'no-inherit' -> type(KW_NO_INHERIT);
INT_APOS_KW_NO_PRESERVE:        'no-preserve' -> type(KW_NO_PRESERVE);
INT_APOS_KW_NODE:               'node' -> type(KW_NODE);
INT_APOS_KW_OF:                 'of' -> type(KW_OF);
INT_APOS_KW_ONLY:               'only' -> type(KW_ONLY);
INT_APOS_KW_OPTION:             'option' -> type(KW_OPTION);
INT_APOS_KW_OR:                 'or' -> type(KW_OR);
INT_APOS_KW_ORDER:              'order' -> type(KW_ORDER);
INT_APOS_KW_ORDERED:            'ordered' -> type(KW_ORDERED);
INT_APOS_KW_ORDERING:           'ordering' -> type(KW_ORDERING);
INT_APOS_KW_PARENT:             'parent' -> type(KW_PARENT);
INT_APOS_KW_PRECEDING:          'preceding' -> type(KW_PRECEDING);
INT_APOS_KW_PRECEDING_SIBLING:  'preceding-sibling' -> type(KW_PRECEDING_SIBLING);
INT_APOS_KW_PRESERVE:           'preserve' -> type(KW_PRESERVE);
INT_APOS_KW_PREVIOUS:           'previous' -> type(KW_PREVIOUS);
INT_APOS_KW_PI:                 'processing-instruction' -> type(KW_PI);
INT_APOS_KW_RETURN:             'return' -> type(KW_RETURN);
INT_APOS_KW_SATISFIES:          'satisfies' -> type(KW_SATISFIES);
INT_APOS_KW_SCHEMA:             'schema' -> type(KW_SCHEMA);
INT_APOS_KW_SCHEMA_ATTR:        'schema-attribute' -> type(KW_SCHEMA_ATTR);
INT_APOS_KW_SCHEMA_ELEM:        'schema-element' -> type(KW_SCHEMA_ELEM);
INT_APOS_KW_SELF:               'self' -> type(KW_SELF);
INT_APOS_KW_SLIDING:            'sliding' -> type(KW_SLIDING);
INT_APOS_KW_SOME:               'some' -> type(KW_SOME);
INT_APOS_KW_STABLE:             'stable' -> type(KW_STABLE);
INT_APOS_KW_START:              'start' -> type(KW_START);
INT_APOS_KW_STRICT:             'strict' -> type(KW_STRICT);
INT_APOS_KW_STRIP:              'strip' -> type(KW_STRIP);
INT_APOS_KW_SWITCH:             'switch' -> type(KW_SWITCH);
INT_APOS_KW_TEXT:               'text' -> type(KW_TEXT);
INT_APOS_KW_THEN:               'then' -> type(KW_THEN);
INT_APOS_KW_TO:                 'to' -> type(KW_TO);
INT_APOS_KW_TREAT:              'treat' -> type(KW_TREAT);
INT_APOS_KW_TRY:                'try' -> type(KW_TRY);
INT_APOS_KW_TUMBLING:           'tumbling' -> type(KW_TUMBLING);
INT_APOS_KW_TYPE:               'type' -> type(KW_TYPE);
INT_APOS_KW_TYPESWITCH:         'typeswitch' -> type(KW_TYPESWITCH);
INT_APOS_KW_UNION:              'union' -> type(KW_UNION);
INT_APOS_KW_UNORDERED:          'unordered' -> type(KW_UNORDERED);
INT_APOS_KW_UPDATE:             'update' -> type(KW_UPDATE);
INT_APOS_KW_VALIDATE:           'validate' -> type(KW_VALIDATE);
INT_APOS_KW_VARIABLE:           'variable' -> type(KW_VARIABLE);
INT_APOS_KW_VERSION:            'version' -> type(KW_VERSION);
INT_APOS_KW_WHEN:               'when' -> type(KW_WHEN);
INT_APOS_KW_WHERE:              'where' -> type(KW_WHERE);
INT_APOS_KW_WINDOW:             'window' -> type(KW_WINDOW);
INT_APOS_KW_XQUERY:             'xquery' -> type(KW_XQUERY);

// MarkLogic JSON computed constructor

INT_APOS_KW_ARRAY_NODE:         'array-node' -> type(KW_ARRAY_NODE);
INT_APOS_KW_BOOLEAN_NODE:       'boolean-node' -> type(KW_BOOLEAN_NODE);
INT_APOS_KW_NULL_NODE:          'null-node' -> type(KW_NULL_NODE);
INT_APOS_KW_NUMBER_NODE:        'number-node' -> type(KW_NUMBER_NODE);
INT_APOS_KW_OBJECT_NODE:        'object-node' -> type(KW_OBJECT_NODE);


// eXist-db update keywords

INT_APOS_KW_REPLACE:            'replace' -> type(KW_REPLACE);
INT_APOS_KW_WITH:               'with' -> type(KW_WITH);
INT_APOS_KW_VALUE:              'value' -> type(KW_VALUE);
INT_APOS_KW_INSERT:             'insert' -> type(KW_INSERT);
INT_APOS_KW_INTO:               'into' -> type(KW_INTO);
INT_APOS_KW_DELETE:             'delete' -> type(KW_DELETE);
INT_APOS_KW_RENAME:             'rename' -> type(KW_RENAME);


// NAMES

INT_APOS_URIQualifiedName: 'Q' '{' (PredefinedEntityRef | CharRef | ~[&{}])* '}' NCName -> type(URIQualifiedName);
INT_APOS_FullQName: NCName ':' NCName -> type(FullQName);
INT_APOS_NCNameWithLocalWildcard:  NCName ':' '*' -> type(NCNameWithLocalWildcard);
INT_APOS_NCNameWithPrefixWildcard: '*' ':' NCName -> type(NCNameWithPrefixWildcard); 

INT_APOS_NCName: NameStartChar NameChar* -> type(NCName);

INT_APOS_XQDOC_COMMENT_START: '(:~' -> type(XQDOC_COMMENT_START);
INT_APOS_XQDOC_COMMENT_END: ':'+ ')' -> type(XQDOC_COMMENT_END);

INT_APOS_XQComment: '(' ':' ~'~' (XQComment | '(' ~[:] | ':' ~[)] | ~[:(])* ':'* ':'+ ')' -> channel(HIDDEN), type(XQComment);

INT_APOS_CHAR: ( '\t' | '\n' | '\r' | '\u0020'..'\u0039' | '\u003B'..'\uD7FF' | '\uE000'..'\uFFFD' ) -> type(CHAR);


INT_APOS_ENTER_STRING        : GRAVE GRAVE LBRACKET -> pushMode(STRING_MODE), type(ENTER_STRING);
INT_APOS_EXIT_INTERPOLATION  : RBRACE GRAVE -> popMode, type(ENTER_INTERPOLATION);

INT_APOS_ContentChar:  ~["'{}<&] -> type(ContentChar);