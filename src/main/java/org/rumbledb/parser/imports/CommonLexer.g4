/*
 * A lexer grammar for XQuery 3.1 that includes the XQuery Scripting Extensions, and additional update features.
 * This file is based on the XQuery lexer grammar from the xqdoc project:
 * https://github.com/xqdoc/xqdoc/blob/master/src/main/antlr4/org/xqdoc/XQueryLexer.g4
 * 
 * See LICENSE-xqdoc.txt for the original license terms.
 * 
 * @author Matteo Agnoletto (EPMatt)
 */
lexer grammar CommonLexer;

IntegerLiteral
   : Digits
   ;

DecimalLiteral
   : ('.' Digits)
   | (Digits '.' [0-9]*)
   ;

DoubleLiteral
   : (('.' Digits) | (Digits ('.' [0-9]*)?)) [eE] [+-]? Digits
   ;

DFPropertyName
   : 'decimal-separator'
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

fragment Digits
   : [0-9]+
   ;
   // This could be checked elsewhere: http://www.w3.org/TR/REC-xml/#wf-Legalchar
   
PredefinedEntityRef
   : '&' ('lt' | 'gt' | 'amp' | 'quot' | 'apos') ';'
   ;
   // CharRef is additionally limited by http://www.w3.org/TR/REC-xml/#NT-Char,
   
CharRef
   : '&#' [0-9]+ ';'
   | '&#x' [0-9a-fA-F]+ ';'
   ;

Ampersand
   : '&'
   ;
/*
   Escapes are handled as two Quot or two Apos tokens, to avoid maximal munch lexer ambiguity.
*/
   
   
Quot
   : '"'
   ;

Apos
   : '\''
   ;
   // XML-SPECIFIC
   
COMMENT
   : '<!--' ('-' ~ [-] | ~ [-])* '-->'
   ;

XMLDECL
   : '<?' [Xx] [Mm] [Ll] ([ \t\r\n] .*?)? '?>'
   ;

PI
   : '<?' NCName ([ \t\r\n] .*?)? '?>'
   ;

CDATA
   : '<![CDATA[' .*? ']]>'
   ;

PRAGMA
   : '(#' WS? (URIQualifiedName | FullQName | NCName) (WS .*?)? '#)'
   ;
   // WHITESPACE
   
   // S ::= (#x20 | #x9 | #xD | #xA)+
   
WS
   : [ \t\r\n]+ -> channel (HIDDEN)
   ;
   // OPERATORS
   
EQUAL
   : '='
   ;

NOT_EQUAL
   : '!='
   ;

LPAREN
   : '('
   ;

RPAREN
   : ')'
   ;

LBRACKET
   : '['
   ;

RBRACKET
   : ']'
   ;

LBRACE
   : '{'
   ;

RBRACE
   : '}'
   ;

STAR
   : '*'
   ;

PLUS
   : '+'
   ;

MINUS
   : '-'
   ;

COMMA
   : ','
   ;

DOT
   : '.'
   ;

DDOT
   : '..'
   ;

COLON
   : ':'
   ;

COLON_EQ
   : ':='
   ;

SEMICOLON
   : ';'
   ;

SLASH
   : '/'
   ;

DSLASH
   : '//'
   ;

BACKSLASH
   : '\\'
   ;

VBAR
   : '|'
   ;

LANGLE
   : '<'
   ;

RANGLE
   : '>'
   ;

QUESTION
   : '?'
   ;

AT
   : '@'
   ;

DOLLAR
   : '$'
   ;

MOD
   : '%'
   ;

BANG
   : '!'
   ;

HASH
   : '#'
   ;

CARAT
   : '^'
   ;

ARROW
   : '=>'
   ;

GRAVE
   : '`'
   ;

CONCATENATION
   : '||'
   ;

TILDE
   : '~'
   ;
   // KEYWORDS
   
KW_ALLOWING
   : 'allowing'
   ;

KW_ANCESTOR
   : 'ancestor'
   ;

KW_ANCESTOR_OR_SELF
   : 'ancestor-or-self'
   ;

KW_AND
   : 'and'
   ;

KW_ARRAY
   : 'array'
   ;

KW_AS
   : 'as'
   ;

KW_ASCENDING
   : 'ascending'
   ;

KW_AT
   : 'at'
   ;

KW_ATTRIBUTE
   : 'attribute'
   ;

KW_BASE_URI
   : 'base-uri'
   ;

KW_BOUNDARY_SPACE
   : 'boundary-space'
   ;

KW_BINARY
   : 'binary'
   ;

KW_BY
   : 'by'
   ;

KW_CASE
   : 'case'
   ;

KW_CAST
   : 'cast'
   ;

KW_CASTABLE
   : 'castable'
   ;

KW_CATCH
   : 'catch'
   ;

KW_CHILD
   : 'child'
   ;

KW_COLLATION
   : 'collation'
   ;

KW_COMMENT
   : 'comment'
   ;

KW_CONSTRUCTION
   : 'construction'
   ;

KW_CONTEXT
   : 'context'
   ;

KW_COPY_NS
   : 'copy-namespaces'
   ;

KW_COUNT
   : 'count'
   ;

KW_DECLARE
   : 'declare'
   ;

KW_DEFAULT
   : 'default'
   ;

KW_DESCENDANT
   : 'descendant'
   ;

KW_DESCENDANT_OR_SELF
   : 'descendant-or-self'
   ;

KW_DESCENDING
   : 'descending'
   ;

KW_DECIMAL_FORMAT
   : 'decimal-format'
   ;

KW_DIV
   : 'div'
   ;

KW_DOCUMENT
   : 'document'
   ;

KW_DOCUMENT_NODE
   : 'document-node'
   ;

KW_ELEMENT
   : 'element'
   ;

KW_ELSE
   : 'else'
   ;

KW_EMPTY
   : 'empty'
   ;

KW_EMPTY_SEQUENCE
   : 'empty-sequence'
   ;

KW_ENCODING
   : 'encoding'
   ;

KW_END
   : 'end'
   ;

KW_EQ
   : 'eq'
   ;

KW_EVERY
   : 'every'
   ;

KW_EXCEPT
   : 'except'
   ;

KW_EXTERNAL
   : 'external'
   ;

KW_FOLLOWING
   : 'following'
   ;

KW_FOLLOWING_SIBLING
   : 'following-sibling'
   ;

KW_FOR
   : 'for'
   ;

KW_FUNCTION
   : 'function'
   ;

KW_GE
   : 'ge'
   ;

KW_GREATEST
   : 'greatest'
   ;

KW_GROUP
   : 'group'
   ;

KW_GT
   : 'gt'
   ;

KW_IDIV
   : 'idiv'
   ;

KW_IF
   : 'if'
   ;

KW_IMPORT
   : 'import'
   ;

KW_IN
   : 'in'
   ;

KW_INHERIT
   : 'inherit'
   ;

KW_INSTANCE
   : 'instance'
   ;

KW_INTERSECT
   : 'intersect'
   ;

KW_IS
   : 'is'
   ;

KW_ITEM
   : 'item'
   ;

KW_LAX
   : 'lax'
   ;

KW_LE
   : 'le'
   ;

KW_LEAST
   : 'least'
   ;

KW_LET
   : 'let'
   ;

KW_LT
   : 'lt'
   ;

KW_MAP
   : 'map'
   ;

KW_MOD
   : 'mod'
   ;

KW_MODULE
   : 'module'
   ;

KW_NAMESPACE
   : 'namespace'
   ;

KW_NE
   : 'ne'
   ;

KW_NEXT
   : 'next'
   ;

KW_NAMESPACE_NODE
   : 'namespace-node'
   ;

KW_NO_INHERIT
   : 'no-inherit'
   ;

KW_NO_PRESERVE
   : 'no-preserve'
   ;

KW_NODE
   : 'node'
   ;

KW_OF
   : 'of'
   ;

KW_ONLY
   : 'only'
   ;

KW_OPTION
   : 'option'
   ;

KW_OR
   : 'or'
   ;

KW_ORDER
   : 'order'
   ;

KW_ORDERED
   : 'ordered'
   ;

KW_ORDERING
   : 'ordering'
   ;

KW_PARENT
   : 'parent'
   ;

KW_PRECEDING
   : 'preceding'
   ;

KW_PRECEDING_SIBLING
   : 'preceding-sibling'
   ;

KW_PRESERVE
   : 'preserve'
   ;

KW_PREVIOUS
   : 'previous'
   ;

KW_PI
   : 'processing-instruction'
   ;

KW_RETURN
   : 'return'
   ;

KW_SATISFIES
   : 'satisfies'
   ;

KW_SCHEMA
   : 'schema'
   ;

KW_SCHEMA_ATTR
   : 'schema-attribute'
   ;

KW_SCHEMA_ELEM
   : 'schema-element'
   ;

KW_SELF
   : 'self'
   ;

KW_SLIDING
   : 'sliding'
   ;

KW_SOME
   : 'some'
   ;

KW_STABLE
   : 'stable'
   ;

KW_START
   : 'start'
   ;

KW_STRICT
   : 'strict'
   ;

KW_STRIP
   : 'strip'
   ;

KW_SWITCH
   : 'switch'
   ;

KW_TEXT
   : 'text'
   ;

KW_THEN
   : 'then'
   ;

KW_TO
   : 'to'
   ;

KW_TREAT
   : 'treat'
   ;

KW_TRY
   : 'try'
   ;

KW_TUMBLING
   : 'tumbling'
   ;

KW_TYPE
   : 'type'
   ;

KW_TYPESWITCH
   : 'typeswitch'
   ;

KW_UNION
   : 'union'
   ;

KW_UNORDERED
   : 'unordered'
   ;

KW_UPDATE
   : 'update'
   ;

KW_VALIDATE
   : 'validate'
   ;

KW_VARIABLE
   : 'variable'
   ;

KW_VERSION
   : 'version'
   ;

KW_WHEN
   : 'when'
   ;

KW_WHERE
   : 'where'
   ;

KW_WINDOW
   : 'window'
   ;

KW_ARRAY_NODE
   : 'array-node'
   ;

KW_BOOLEAN_NODE
   : 'boolean-node'
   ;

KW_NULL_NODE
   : 'null-node'
   ;

KW_NUMBER_NODE
   : 'number-node'
   ;

KW_OBJECT_NODE
   : 'object-node'
   ;
   // eXist-db update keywords
   
KW_REPLACE
   : 'replace'
   ;

KW_WITH
   : 'with'
   ;

KW_VALUE
   : 'value'
   ;

KW_INSERT
   : 'insert'
   ;

KW_INTO
   : 'into'
   ;

KW_DELETE
   : 'delete'
   ;

KW_RENAME
   : 'rename'
   ;
   // XQuery Scripting Extension keywords
   
KW_BREAK
   : 'break'
   ;

KW_LOOP
   : 'loop'
   ;

KW_CONTINUE
   : 'continue'
   ;

KW_EXIT
   : 'exit'
   ;

KW_RETURNING
   : 'returning'
   ;

KW_WHILE
   : 'while'
   ;
   // Updating expressions keywords
   
KW_COPY
   : 'copy'
   ;

KW_MODIFY
   : 'modify'
   ;

KW_APPEND
   : 'append'
   ;

KW_JSON
   : 'json'
   ;

KW_POSITION
   : 'position'
   ;

KW_UPDATING
   : 'updating'
   ;
   // JSONiq updates
   
KW_LAST
   : 'last'
   ;

KW_TRUNCATE
   : 'truncate'
   ;

KW_EDIT
   : 'edit'
   ;

KW_FROM
   : 'from'
   ;

KW_BEFORE
   : 'before'
   ;

KW_AFTER
   : 'after'
   ;

KW_FIRST
   : 'first'
   ;

KW_CREATE
   : 'create'
   ;

KW_COLLECTION
   : 'collection'
   ;

KW_TABLE
   : 'table'
   ;

KW_DELTA_FILE
   : 'delta-file'
   ;

KW_ICEBERG_TABLE
   : 'iceberg-table'
   ;

KW_STATICALLY
   : 'statically'
   ;
   // NAMES
   
   // added the BracedURILiteral rule
   
BracedURILiteral
   : 'Q' '{' (PredefinedEntityRef | CharRef | ~ [&{}])* '}'
   ;
   // Moved URIQualifiedName here to gather all names
   
URIQualifiedName
   : BracedURILiteral NCName
   ;
   // We create these basic variants in order to honor ws:explicit in some basic cases
   
FullQName
   : NCName ':' NCName
   ;

NCNameWithLocalWildcard
   : NCName ':' '*'
   ;

NCNameWithPrefixWildcard
   : '*' ':' NCName
   ;
/*
 * According to http://www.w3.org/TR/REC-xml-names/#NT-NCName,
 * it is 'an XML Name, minus the ":"'
 */
   
   
NCName
   : NameStartChar NameChar*
   ;

fragment NameStartChar
   : [_a-zA-Z]
   | '\u00C0' .. '\u00D6'
   | '\u00D8' .. '\u00F6'
   | '\u00F8' .. '\u02FF'
   | '\u0370' .. '\u037D'
   | '\u037F' .. '\u1FFF'
   | '\u200C' .. '\u200D'
   | '\u2070' .. '\u218F'
   | '\u2C00' .. '\u2FEF'
   | '\u3001' .. '\uD7FF'
   | '\uF900' .. '\uFDCF'
   | '\uFDF0' .. '\uFFFD'
   ;

fragment NameChar
   : NameStartChar
   | '-'
   | '.'
   | [0-9]
   | '\u00A1' .. '\u00BF'
   | '\u0300' .. '\u036F'
   | '\u203F' .. '\u2040'
   ;
/*
 * XQuery comments
 * Element content can have an unbalanced set of (: :) pairs (as XQuery comments do not really exist inside them),
 * so it is better to treat this as a single token with a recursive rule, rather than using a mode.
 */
   
   
XQComment
   : '(' ':' (XQComment | '(' ~ [:] | ':' ~ [)] | ~ [:(])* ':'* ':'+ ')' -> channel (HIDDEN)
   ;

CHAR
   : ('\t' | '\n' | '\r' | '\u0020' .. '\u0039' | '\u003B' .. '\uD7FF' | '\uE000' .. '\uFFFD')
   ;
   // These rules have been added to enter and exit the String mode
   
ENTER_STRING
   : GRAVE GRAVE LBRACKET -> pushMode (STRING_MODE)
   ;

ContentChar
/*
        This is an intersection of:
        [148] ElementContentChar  ::= Char - [{}<&]
        [149] QuotAttrContentChar ::= Char - ["{}<&]
        [150] AposAttrContentChar ::= Char - ['{}<&]

        Therefore, we would have something like:
        ElementContentChar  ::= ContentChar | ["']
        QuotAttrContentChar ::= ContentChar | [']
        AposAttrContentChar ::= ContentChar | ["]

        This rule needs to be the very last one, so it has the lowest priority.
    */
   
   : ~ ["'{}<&]
   ;

mode STRING_MODE; // Lexical mode for backtick template strings.

BASIC_CHAR
   : ('\t' | '\u000A' | '\u000D' | '\u0020' .. '\u005C' | '\u005E' .. '\u005F' | '\u0061' .. '\u007A' | '\u007C' .. '\uD7FF' | '\uE000' .. '\uFFFD' | '\u{10000}' .. '\u{10FFFF}')
   ;

GRAVE_STRING
   : '`' -> type (GRAVE)
   ;

RBRACKET_STRING
   : ']' -> type (RBRACKET)
   ;

LBRACE_STRING
   : '{' -> type (LBRACE)
   ;

ENTER_INTERPOLATION
   : GRAVE LBRACE -> pushMode (INTERPOLATION_MODE)
   ;

EXIT_STRING
   : RBRACKET GRAVE GRAVE -> popMode
   ;

mode INTERPOLATION_MODE; // Regenerate aliases below with tools/generate_interpolation_lexer_rules.py.

EXIT_INTERPOLATION
   : RBRACE GRAVE -> popMode
   ;

INTERPOLATION_IntegerLiteral
   : IntegerLiteral -> type (IntegerLiteral)
   ;

INTERPOLATION_DecimalLiteral
   : DecimalLiteral -> type (DecimalLiteral)
   ;

INTERPOLATION_DoubleLiteral
   : DoubleLiteral -> type (DoubleLiteral)
   ;

INTERPOLATION_DFPropertyName
   : DFPropertyName -> type (DFPropertyName)
   ;

INTERPOLATION_PredefinedEntityRef
   : PredefinedEntityRef -> type (PredefinedEntityRef)
   ;

INTERPOLATION_CharRef
   : CharRef -> type (CharRef)
   ;

INTERPOLATION_Ampersand
   : Ampersand -> type (Ampersand)
   ;

INTERPOLATION_Quot
   : Quot -> type (Quot)
   ;

INTERPOLATION_Apos
   : Apos -> type (Apos)
   ;

INTERPOLATION_COMMENT
   : COMMENT -> type (COMMENT)
   ;

INTERPOLATION_XMLDECL
   : XMLDECL -> type (XMLDECL)
   ;

INTERPOLATION_PI
   : PI -> type (PI)
   ;

INTERPOLATION_CDATA
   : CDATA -> type (CDATA)
   ;

INTERPOLATION_PRAGMA
   : PRAGMA -> type (PRAGMA)
   ;

INTERPOLATION_WS
   : WS -> channel (HIDDEN) , type (WS)
   ;

INTERPOLATION_EQUAL
   : EQUAL -> type (EQUAL)
   ;

INTERPOLATION_NOT_EQUAL
   : NOT_EQUAL -> type (NOT_EQUAL)
   ;

INTERPOLATION_LPAREN
   : LPAREN -> type (LPAREN)
   ;

INTERPOLATION_RPAREN
   : RPAREN -> type (RPAREN)
   ;

INTERPOLATION_LBRACKET
   : LBRACKET -> type (LBRACKET)
   ;

INTERPOLATION_RBRACKET
   : RBRACKET -> type (RBRACKET)
   ;

INTERPOLATION_LBRACE
   : LBRACE -> type (LBRACE)
   ;

INTERPOLATION_RBRACE
   : RBRACE -> type (RBRACE)
   ;

INTERPOLATION_STAR
   : STAR -> type (STAR)
   ;

INTERPOLATION_PLUS
   : PLUS -> type (PLUS)
   ;

INTERPOLATION_MINUS
   : MINUS -> type (MINUS)
   ;

INTERPOLATION_COMMA
   : COMMA -> type (COMMA)
   ;

INTERPOLATION_DOT
   : DOT -> type (DOT)
   ;

INTERPOLATION_DDOT
   : DDOT -> type (DDOT)
   ;

INTERPOLATION_COLON
   : COLON -> type (COLON)
   ;

INTERPOLATION_COLON_EQ
   : COLON_EQ -> type (COLON_EQ)
   ;

INTERPOLATION_SEMICOLON
   : SEMICOLON -> type (SEMICOLON)
   ;

INTERPOLATION_SLASH
   : SLASH -> type (SLASH)
   ;

INTERPOLATION_DSLASH
   : DSLASH -> type (DSLASH)
   ;

INTERPOLATION_BACKSLASH
   : BACKSLASH -> type (BACKSLASH)
   ;

INTERPOLATION_VBAR
   : VBAR -> type (VBAR)
   ;

INTERPOLATION_LANGLE
   : LANGLE -> type (LANGLE)
   ;

INTERPOLATION_RANGLE
   : RANGLE -> type (RANGLE)
   ;

INTERPOLATION_QUESTION
   : QUESTION -> type (QUESTION)
   ;

INTERPOLATION_AT
   : AT -> type (AT)
   ;

INTERPOLATION_DOLLAR
   : DOLLAR -> type (DOLLAR)
   ;

INTERPOLATION_MOD
   : MOD -> type (MOD)
   ;

INTERPOLATION_BANG
   : BANG -> type (BANG)
   ;

INTERPOLATION_HASH
   : HASH -> type (HASH)
   ;

INTERPOLATION_CARAT
   : CARAT -> type (CARAT)
   ;

INTERPOLATION_ARROW
   : ARROW -> type (ARROW)
   ;

INTERPOLATION_GRAVE
   : GRAVE -> type (GRAVE)
   ;

INTERPOLATION_CONCATENATION
   : CONCATENATION -> type (CONCATENATION)
   ;

INTERPOLATION_TILDE
   : TILDE -> type (TILDE)
   ;

INTERPOLATION_KW_ALLOWING
   : KW_ALLOWING -> type (KW_ALLOWING)
   ;

INTERPOLATION_KW_ANCESTOR
   : KW_ANCESTOR -> type (KW_ANCESTOR)
   ;

INTERPOLATION_KW_ANCESTOR_OR_SELF
   : KW_ANCESTOR_OR_SELF -> type (KW_ANCESTOR_OR_SELF)
   ;

INTERPOLATION_KW_AND
   : KW_AND -> type (KW_AND)
   ;

INTERPOLATION_KW_ARRAY
   : KW_ARRAY -> type (KW_ARRAY)
   ;

INTERPOLATION_KW_AS
   : KW_AS -> type (KW_AS)
   ;

INTERPOLATION_KW_ASCENDING
   : KW_ASCENDING -> type (KW_ASCENDING)
   ;

INTERPOLATION_KW_AT
   : KW_AT -> type (KW_AT)
   ;

INTERPOLATION_KW_ATTRIBUTE
   : KW_ATTRIBUTE -> type (KW_ATTRIBUTE)
   ;

INTERPOLATION_KW_BASE_URI
   : KW_BASE_URI -> type (KW_BASE_URI)
   ;

INTERPOLATION_KW_BOUNDARY_SPACE
   : KW_BOUNDARY_SPACE -> type (KW_BOUNDARY_SPACE)
   ;

INTERPOLATION_KW_BINARY
   : KW_BINARY -> type (KW_BINARY)
   ;

INTERPOLATION_KW_BY
   : KW_BY -> type (KW_BY)
   ;

INTERPOLATION_KW_CASE
   : KW_CASE -> type (KW_CASE)
   ;

INTERPOLATION_KW_CAST
   : KW_CAST -> type (KW_CAST)
   ;

INTERPOLATION_KW_CASTABLE
   : KW_CASTABLE -> type (KW_CASTABLE)
   ;

INTERPOLATION_KW_CATCH
   : KW_CATCH -> type (KW_CATCH)
   ;

INTERPOLATION_KW_CHILD
   : KW_CHILD -> type (KW_CHILD)
   ;

INTERPOLATION_KW_COLLATION
   : KW_COLLATION -> type (KW_COLLATION)
   ;

INTERPOLATION_KW_COMMENT
   : KW_COMMENT -> type (KW_COMMENT)
   ;

INTERPOLATION_KW_CONSTRUCTION
   : KW_CONSTRUCTION -> type (KW_CONSTRUCTION)
   ;

INTERPOLATION_KW_CONTEXT
   : KW_CONTEXT -> type (KW_CONTEXT)
   ;

INTERPOLATION_KW_COPY_NS
   : KW_COPY_NS -> type (KW_COPY_NS)
   ;

INTERPOLATION_KW_COUNT
   : KW_COUNT -> type (KW_COUNT)
   ;

INTERPOLATION_KW_DECLARE
   : KW_DECLARE -> type (KW_DECLARE)
   ;

INTERPOLATION_KW_DEFAULT
   : KW_DEFAULT -> type (KW_DEFAULT)
   ;

INTERPOLATION_KW_DESCENDANT
   : KW_DESCENDANT -> type (KW_DESCENDANT)
   ;

INTERPOLATION_KW_DESCENDANT_OR_SELF
   : KW_DESCENDANT_OR_SELF -> type (KW_DESCENDANT_OR_SELF)
   ;

INTERPOLATION_KW_DESCENDING
   : KW_DESCENDING -> type (KW_DESCENDING)
   ;

INTERPOLATION_KW_DECIMAL_FORMAT
   : KW_DECIMAL_FORMAT -> type (KW_DECIMAL_FORMAT)
   ;

INTERPOLATION_KW_DIV
   : KW_DIV -> type (KW_DIV)
   ;

INTERPOLATION_KW_DOCUMENT
   : KW_DOCUMENT -> type (KW_DOCUMENT)
   ;

INTERPOLATION_KW_DOCUMENT_NODE
   : KW_DOCUMENT_NODE -> type (KW_DOCUMENT_NODE)
   ;

INTERPOLATION_KW_ELEMENT
   : KW_ELEMENT -> type (KW_ELEMENT)
   ;

INTERPOLATION_KW_ELSE
   : KW_ELSE -> type (KW_ELSE)
   ;

INTERPOLATION_KW_EMPTY
   : KW_EMPTY -> type (KW_EMPTY)
   ;

INTERPOLATION_KW_EMPTY_SEQUENCE
   : KW_EMPTY_SEQUENCE -> type (KW_EMPTY_SEQUENCE)
   ;

INTERPOLATION_KW_ENCODING
   : KW_ENCODING -> type (KW_ENCODING)
   ;

INTERPOLATION_KW_END
   : KW_END -> type (KW_END)
   ;

INTERPOLATION_KW_EQ
   : KW_EQ -> type (KW_EQ)
   ;

INTERPOLATION_KW_EVERY
   : KW_EVERY -> type (KW_EVERY)
   ;

INTERPOLATION_KW_EXCEPT
   : KW_EXCEPT -> type (KW_EXCEPT)
   ;

INTERPOLATION_KW_EXTERNAL
   : KW_EXTERNAL -> type (KW_EXTERNAL)
   ;

INTERPOLATION_KW_FOLLOWING
   : KW_FOLLOWING -> type (KW_FOLLOWING)
   ;

INTERPOLATION_KW_FOLLOWING_SIBLING
   : KW_FOLLOWING_SIBLING -> type (KW_FOLLOWING_SIBLING)
   ;

INTERPOLATION_KW_FOR
   : KW_FOR -> type (KW_FOR)
   ;

INTERPOLATION_KW_FUNCTION
   : KW_FUNCTION -> type (KW_FUNCTION)
   ;

INTERPOLATION_KW_GE
   : KW_GE -> type (KW_GE)
   ;

INTERPOLATION_KW_GREATEST
   : KW_GREATEST -> type (KW_GREATEST)
   ;

INTERPOLATION_KW_GROUP
   : KW_GROUP -> type (KW_GROUP)
   ;

INTERPOLATION_KW_GT
   : KW_GT -> type (KW_GT)
   ;

INTERPOLATION_KW_IDIV
   : KW_IDIV -> type (KW_IDIV)
   ;

INTERPOLATION_KW_IF
   : KW_IF -> type (KW_IF)
   ;

INTERPOLATION_KW_IMPORT
   : KW_IMPORT -> type (KW_IMPORT)
   ;

INTERPOLATION_KW_IN
   : KW_IN -> type (KW_IN)
   ;

INTERPOLATION_KW_INHERIT
   : KW_INHERIT -> type (KW_INHERIT)
   ;

INTERPOLATION_KW_INSTANCE
   : KW_INSTANCE -> type (KW_INSTANCE)
   ;

INTERPOLATION_KW_INTERSECT
   : KW_INTERSECT -> type (KW_INTERSECT)
   ;

INTERPOLATION_KW_IS
   : KW_IS -> type (KW_IS)
   ;

INTERPOLATION_KW_ITEM
   : KW_ITEM -> type (KW_ITEM)
   ;

INTERPOLATION_KW_LAX
   : KW_LAX -> type (KW_LAX)
   ;

INTERPOLATION_KW_LE
   : KW_LE -> type (KW_LE)
   ;

INTERPOLATION_KW_LEAST
   : KW_LEAST -> type (KW_LEAST)
   ;

INTERPOLATION_KW_LET
   : KW_LET -> type (KW_LET)
   ;

INTERPOLATION_KW_LT
   : KW_LT -> type (KW_LT)
   ;

INTERPOLATION_KW_MAP
   : KW_MAP -> type (KW_MAP)
   ;

INTERPOLATION_KW_MOD
   : KW_MOD -> type (KW_MOD)
   ;

INTERPOLATION_KW_MODULE
   : KW_MODULE -> type (KW_MODULE)
   ;

INTERPOLATION_KW_NAMESPACE
   : KW_NAMESPACE -> type (KW_NAMESPACE)
   ;

INTERPOLATION_KW_NE
   : KW_NE -> type (KW_NE)
   ;

INTERPOLATION_KW_NEXT
   : KW_NEXT -> type (KW_NEXT)
   ;

INTERPOLATION_KW_NAMESPACE_NODE
   : KW_NAMESPACE_NODE -> type (KW_NAMESPACE_NODE)
   ;

INTERPOLATION_KW_NO_INHERIT
   : KW_NO_INHERIT -> type (KW_NO_INHERIT)
   ;

INTERPOLATION_KW_NO_PRESERVE
   : KW_NO_PRESERVE -> type (KW_NO_PRESERVE)
   ;

INTERPOLATION_KW_NODE
   : KW_NODE -> type (KW_NODE)
   ;

INTERPOLATION_KW_OF
   : KW_OF -> type (KW_OF)
   ;

INTERPOLATION_KW_ONLY
   : KW_ONLY -> type (KW_ONLY)
   ;

INTERPOLATION_KW_OPTION
   : KW_OPTION -> type (KW_OPTION)
   ;

INTERPOLATION_KW_OR
   : KW_OR -> type (KW_OR)
   ;

INTERPOLATION_KW_ORDER
   : KW_ORDER -> type (KW_ORDER)
   ;

INTERPOLATION_KW_ORDERED
   : KW_ORDERED -> type (KW_ORDERED)
   ;

INTERPOLATION_KW_ORDERING
   : KW_ORDERING -> type (KW_ORDERING)
   ;

INTERPOLATION_KW_PARENT
   : KW_PARENT -> type (KW_PARENT)
   ;

INTERPOLATION_KW_PRECEDING
   : KW_PRECEDING -> type (KW_PRECEDING)
   ;

INTERPOLATION_KW_PRECEDING_SIBLING
   : KW_PRECEDING_SIBLING -> type (KW_PRECEDING_SIBLING)
   ;

INTERPOLATION_KW_PRESERVE
   : KW_PRESERVE -> type (KW_PRESERVE)
   ;

INTERPOLATION_KW_PREVIOUS
   : KW_PREVIOUS -> type (KW_PREVIOUS)
   ;

INTERPOLATION_KW_PI
   : KW_PI -> type (KW_PI)
   ;

INTERPOLATION_KW_RETURN
   : KW_RETURN -> type (KW_RETURN)
   ;

INTERPOLATION_KW_SATISFIES
   : KW_SATISFIES -> type (KW_SATISFIES)
   ;

INTERPOLATION_KW_SCHEMA
   : KW_SCHEMA -> type (KW_SCHEMA)
   ;

INTERPOLATION_KW_SCHEMA_ATTR
   : KW_SCHEMA_ATTR -> type (KW_SCHEMA_ATTR)
   ;

INTERPOLATION_KW_SCHEMA_ELEM
   : KW_SCHEMA_ELEM -> type (KW_SCHEMA_ELEM)
   ;

INTERPOLATION_KW_SELF
   : KW_SELF -> type (KW_SELF)
   ;

INTERPOLATION_KW_SLIDING
   : KW_SLIDING -> type (KW_SLIDING)
   ;

INTERPOLATION_KW_SOME
   : KW_SOME -> type (KW_SOME)
   ;

INTERPOLATION_KW_STABLE
   : KW_STABLE -> type (KW_STABLE)
   ;

INTERPOLATION_KW_START
   : KW_START -> type (KW_START)
   ;

INTERPOLATION_KW_STRICT
   : KW_STRICT -> type (KW_STRICT)
   ;

INTERPOLATION_KW_STRIP
   : KW_STRIP -> type (KW_STRIP)
   ;

INTERPOLATION_KW_SWITCH
   : KW_SWITCH -> type (KW_SWITCH)
   ;

INTERPOLATION_KW_TEXT
   : KW_TEXT -> type (KW_TEXT)
   ;

INTERPOLATION_KW_THEN
   : KW_THEN -> type (KW_THEN)
   ;

INTERPOLATION_KW_TO
   : KW_TO -> type (KW_TO)
   ;

INTERPOLATION_KW_TREAT
   : KW_TREAT -> type (KW_TREAT)
   ;

INTERPOLATION_KW_TRY
   : KW_TRY -> type (KW_TRY)
   ;

INTERPOLATION_KW_TUMBLING
   : KW_TUMBLING -> type (KW_TUMBLING)
   ;

INTERPOLATION_KW_TYPE
   : KW_TYPE -> type (KW_TYPE)
   ;

INTERPOLATION_KW_TYPESWITCH
   : KW_TYPESWITCH -> type (KW_TYPESWITCH)
   ;

INTERPOLATION_KW_UNION
   : KW_UNION -> type (KW_UNION)
   ;

INTERPOLATION_KW_UNORDERED
   : KW_UNORDERED -> type (KW_UNORDERED)
   ;

INTERPOLATION_KW_UPDATE
   : KW_UPDATE -> type (KW_UPDATE)
   ;

INTERPOLATION_KW_VALIDATE
   : KW_VALIDATE -> type (KW_VALIDATE)
   ;

INTERPOLATION_KW_VARIABLE
   : KW_VARIABLE -> type (KW_VARIABLE)
   ;

INTERPOLATION_KW_VERSION
   : KW_VERSION -> type (KW_VERSION)
   ;

INTERPOLATION_KW_WHEN
   : KW_WHEN -> type (KW_WHEN)
   ;

INTERPOLATION_KW_WHERE
   : KW_WHERE -> type (KW_WHERE)
   ;

INTERPOLATION_KW_WINDOW
   : KW_WINDOW -> type (KW_WINDOW)
   ;

INTERPOLATION_KW_ARRAY_NODE
   : KW_ARRAY_NODE -> type (KW_ARRAY_NODE)
   ;

INTERPOLATION_KW_BOOLEAN_NODE
   : KW_BOOLEAN_NODE -> type (KW_BOOLEAN_NODE)
   ;

INTERPOLATION_KW_NULL_NODE
   : KW_NULL_NODE -> type (KW_NULL_NODE)
   ;

INTERPOLATION_KW_NUMBER_NODE
   : KW_NUMBER_NODE -> type (KW_NUMBER_NODE)
   ;

INTERPOLATION_KW_OBJECT_NODE
   : KW_OBJECT_NODE -> type (KW_OBJECT_NODE)
   ;

INTERPOLATION_KW_REPLACE
   : KW_REPLACE -> type (KW_REPLACE)
   ;

INTERPOLATION_KW_WITH
   : KW_WITH -> type (KW_WITH)
   ;

INTERPOLATION_KW_VALUE
   : KW_VALUE -> type (KW_VALUE)
   ;

INTERPOLATION_KW_INSERT
   : KW_INSERT -> type (KW_INSERT)
   ;

INTERPOLATION_KW_INTO
   : KW_INTO -> type (KW_INTO)
   ;

INTERPOLATION_KW_DELETE
   : KW_DELETE -> type (KW_DELETE)
   ;

INTERPOLATION_KW_RENAME
   : KW_RENAME -> type (KW_RENAME)
   ;

INTERPOLATION_KW_BREAK
   : KW_BREAK -> type (KW_BREAK)
   ;

INTERPOLATION_KW_LOOP
   : KW_LOOP -> type (KW_LOOP)
   ;

INTERPOLATION_KW_CONTINUE
   : KW_CONTINUE -> type (KW_CONTINUE)
   ;

INTERPOLATION_KW_EXIT
   : KW_EXIT -> type (KW_EXIT)
   ;

INTERPOLATION_KW_RETURNING
   : KW_RETURNING -> type (KW_RETURNING)
   ;

INTERPOLATION_KW_WHILE
   : KW_WHILE -> type (KW_WHILE)
   ;

INTERPOLATION_KW_COPY
   : KW_COPY -> type (KW_COPY)
   ;

INTERPOLATION_KW_MODIFY
   : KW_MODIFY -> type (KW_MODIFY)
   ;

INTERPOLATION_KW_APPEND
   : KW_APPEND -> type (KW_APPEND)
   ;

INTERPOLATION_KW_JSON
   : KW_JSON -> type (KW_JSON)
   ;

INTERPOLATION_KW_POSITION
   : KW_POSITION -> type (KW_POSITION)
   ;

INTERPOLATION_KW_UPDATING
   : KW_UPDATING -> type (KW_UPDATING)
   ;

INTERPOLATION_KW_LAST
   : KW_LAST -> type (KW_LAST)
   ;

INTERPOLATION_KW_TRUNCATE
   : KW_TRUNCATE -> type (KW_TRUNCATE)
   ;

INTERPOLATION_KW_EDIT
   : KW_EDIT -> type (KW_EDIT)
   ;

INTERPOLATION_KW_FROM
   : KW_FROM -> type (KW_FROM)
   ;

INTERPOLATION_KW_BEFORE
   : KW_BEFORE -> type (KW_BEFORE)
   ;

INTERPOLATION_KW_AFTER
   : KW_AFTER -> type (KW_AFTER)
   ;

INTERPOLATION_KW_FIRST
   : KW_FIRST -> type (KW_FIRST)
   ;

INTERPOLATION_KW_CREATE
   : KW_CREATE -> type (KW_CREATE)
   ;

INTERPOLATION_KW_COLLECTION
   : KW_COLLECTION -> type (KW_COLLECTION)
   ;

INTERPOLATION_KW_TABLE
   : KW_TABLE -> type (KW_TABLE)
   ;

INTERPOLATION_KW_DELTA_FILE
   : KW_DELTA_FILE -> type (KW_DELTA_FILE)
   ;

INTERPOLATION_KW_ICEBERG_TABLE
   : KW_ICEBERG_TABLE -> type (KW_ICEBERG_TABLE)
   ;

INTERPOLATION_KW_STATICALLY
   : KW_STATICALLY -> type (KW_STATICALLY)
   ;

INTERPOLATION_BracedURILiteral
   : BracedURILiteral -> type (BracedURILiteral)
   ;

INTERPOLATION_URIQualifiedName
   : URIQualifiedName -> type (URIQualifiedName)
   ;

INTERPOLATION_FullQName
   : FullQName -> type (FullQName)
   ;

INTERPOLATION_NCNameWithLocalWildcard
   : NCNameWithLocalWildcard -> type (NCNameWithLocalWildcard)
   ;

INTERPOLATION_NCNameWithPrefixWildcard
   : NCNameWithPrefixWildcard -> type (NCNameWithPrefixWildcard)
   ;

INTERPOLATION_NCName
   : NCName -> type (NCName)
   ;

INTERPOLATION_XQComment
   : XQComment -> channel (HIDDEN) , type (XQComment)
   ;

INTERPOLATION_CHAR
   : CHAR -> type (CHAR)
   ;

INTERPOLATION_ENTER_STRING
   : ENTER_STRING -> pushMode (STRING_MODE) , type (ENTER_STRING)
   ;

INTERPOLATION_ContentChar
   : ContentChar -> type (ContentChar)
   ;

