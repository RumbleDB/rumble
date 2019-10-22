grammar Jsoniq;

@header {
// Java header
package sparksoniq.jsoniq.compiler.parser;
}

module                  : (Kjsoniq Kversion vers=stringLiteral ';')?
                          (libraryModule | main=mainModule);

mainModule              : prolog expr;

libraryModule           : 'module' 'namespace' NCName '=' uriLiteral ';' prolog;

prolog                  : ((defaultCollationDecl | orderingModeDecl | emptyOrderDecl | decimalFormatDecl | moduleImport) ';')*
                          ((functionDecl | varDecl ) ';')* ;

defaultCollationDecl    : 'declare' Kdefault Kcollation uriLiteral;

orderingModeDecl        : 'declare' 'ordering' ('ordered' | 'unordered');

emptyOrderDecl          : 'declare' Kdefault 'order' Kempty (Kgreatest | Kleast);

decimalFormatDecl       : 'declare'
                          (('decimal-format' (NCName ':')? NCName) | (Kdefault 'decimal-format'))
                          (dfPropertyName '=' stringLiteral)*;

dfPropertyName          : 'decimal-separator'
                        | 'grouping-separator'
                        | 'infinity'
                        | 'minus-sign'
                        | 'NaN'
                        | 'percent'
                        | 'per-mille'
                        | 'zero-digit'
                        | 'digit'
                        | 'pattern-separator';

moduleImport            : 'import' 'module' ('namespace' NCName '=')? uriLiteral (Kat uriLiteral (',' uriLiteral)*)?;

varDecl                 : 'declare' 'variable' varRef (Kas sequenceType)? ((':=' exprSingle) | ('external' (':=' exprSingle)?));

functionDecl            : 'declare' 'function' (NCName ':')? NCName '(' paramList? ')' (Kas sequenceType)? ('{' expr '}' | 'external');

paramList               : '$' NCName (Kas sequenceType)? (',' '$' NCName (Kas sequenceType)?)*;

///////////////////////// constructs, expression

expr                    : exprSingle (',' exprSingle)*;

exprSingle              : flowrExpr
                        | quantifiedExpr
                        | switchExpr
                        | typeSwitchExpr
                        | ifExpr
                        | tryCatchExpr
                        | orExpr;

flowrExpr               : (start_for=forClause| start_let=letClause)
                          (forClause | whereClause | letClause | groupByClause | orderByClause | countClause)*
                          Kreturn return_expr=exprSingle;

forClause               : Kfor vars+=forVar (',' vars+=forVar)*;

forVar                  : var_ref=varRef
                          (Kas seq=sequenceType)?
                          (flag=Kallowing Kempty)?
                          (Kat at=varRef)?
                          Kin ex=exprSingle;

letClause               : Klet vars+=letVar (',' vars+=letVar)*;

letVar                  : var_ref=varRef (Kas seq=sequenceType)? ':=' ex=exprSingle ;

whereClause             : Kwhere exprSingle;

groupByClause           : Kgroup Kby vars+=groupByVar (',' vars+=groupByVar)*;

groupByVar              : var_ref=varRef
                          ((Kas seq=sequenceType)? decl=':=' ex=exprSingle)?
                          (Kcollation uri=uriLiteral)?;

orderByClause           : ((Korder Kby) | (stb=Kstable Korder Kby)) orderByExpr (',' orderByExpr)*;

orderByExpr             : ex=exprSingle
                          (Kascending | desc=Kdescending)?
                          (Kempty (gr=Kgreatest | ls=Kleast))?
                          (Kcollation uril=uriLiteral)?;

countClause             : Kcount varRef;

quantifiedExpr          : (so=Ksome | ev=Kevery)
                          vars+=quantifiedExprVar (',' vars+=quantifiedExprVar)*
                          Ksatisfies exprSingle;

quantifiedExprVar       : varRef (Kas sequenceType)? Kin exprSingle;

switchExpr              : Kswitch '(' cond=expr ')' cases+=switchCaseClause+ Kdefault Kreturn def=exprSingle;

switchCaseClause        : (Kcase cond+=exprSingle)+ Kreturn ret=exprSingle;

typeSwitchExpr          : Ktypeswitch '(' expr ')' caseClause+ Kdefault (varRef)? Kreturn exprSingle;

caseClause              : Kcase (varRef Kas)? sequenceType ('|' sequenceType)* Kreturn exprSingle;

ifExpr                  : Kif '(' test_condition=expr ')'
                          Kthen branch=exprSingle
                          Kelse else_branch=exprSingle;

tryCatchExpr            : Ktry '{' expr '}'
                          Kcatch '*' '{' expr '}';

///////////////////////// expression

orExpr                  : main_expr=andExpr ( Kor rhs+=andExpr )*;

andExpr                 : main_expr=notExpr ( Kand rhs+=notExpr )*;

notExpr                 : op+=Knot ? main_expr=comparisonExpr;

comparisonExpr          : main_expr=stringConcatExpr
                          ( op+=('eq' | 'ne' | 'lt' | 'le' | 'gt' | 'ge'
                          | '=' | '!=' | '<' | '<=' | '>' | '>=') rhs+=stringConcatExpr )?;

stringConcatExpr        : main_expr=rangeExpr ( '||' rhs+=rangeExpr )* ;

rangeExpr               : main_expr=additiveExpr ( Kto rhs+=additiveExpr )?;

additiveExpr            : main_expr=multiplicativeExpr ( op+=('+' | '-') rhs+=multiplicativeExpr )*;

multiplicativeExpr      : main_expr=instanceOfExpr ( op+=('*' | 'div' | 'idiv' | 'mod') rhs+=instanceOfExpr )*;

instanceOfExpr          : main_expr=treatExpr ( Kinstance Kof seq=sequenceType)?;

treatExpr               : main_expr=castableExpr ( Ktreat Kas seq=sequenceType )?;

castableExpr            : main_expr=castExpr ( Kcastable Kas single=singleType )?;

castExpr                : main_expr=unaryExpr ( Kcast Kas single=singleType )?;

unaryExpr               : op+=('-' | '+')* main_expr=simpleMapExpr;

simpleMapExpr           : main_expr=postFixExpr ('!' postFixExpr)*;

postFixExpr             : main_expr=primaryExpr (al=arrayLookup | pr=predicate | ol=objectLookup | au=arrayUnboxing)*;

arrayLookup             : '[' '[' expr ']' ']';

arrayUnboxing           : '[' ']';

predicate               : '[' expr ']';

objectLookup            : '.' ( kw=keyWords | lt=stringLiteral | nc=NCName | pe=parenthesizedExpr | vr=varRef | ci=contextItemExpr );

primaryExpr             : NullLiteral
                        | Literal
                        | stringLiteral
                        | varRef
                        | parenthesizedExpr
                        | contextItemExpr
                        | objectConstructor
                        | functionCall
                        | orderedExpr
                        | unorderedExpr
                        | arrayConstructor;


varRef                  : '$' (ns=NCName ':')? name=NCName;

parenthesizedExpr       : '(' expr? ')';

contextItemExpr         : '$$';

orderedExpr             : 'ordered' '{' expr '}';

unorderedExpr           : 'unordered' '{' expr '}';

functionCall            : ((ns=NCName | kw=keyWords |  )':')?
                          (fn_name=nCNameOrKeyWord | kw = keyWords) argumentList;

argumentList            : '('  (args+=argument ','?)* ')';

argument                : exprSingle | '?';


///////////////////////// Types

sequenceType            : '(' ')'
                        | item=itemType (question+='?' | star+='*' | plus+='+')?;

objectConstructor       : '{' ( pairConstructor (',' pairConstructor)* )? '}'
                        | merge_operator+='{|' expr '|}';

itemType                : 'item'
                        | jSONItemTest
                        | atomicType;

jSONItemTest            : 'object'
                        | 'array'
                        | Kjson;

keyWordBoolean          : 'boolean';

keyWordDuration         : 'duration';

singleType              : item=atomicType (question +='?')?;

atomicType              : 'atomic'
                        | 'string'
                        | 'integer'
                        | 'decimal'
                        | 'double'
                        | keyWordDuration
                        | keyWordBoolean
                        | NullLiteral;

nCNameOrKeyWord         : NCName
                        | keyWordDuration
                        | keyWordBoolean;

pairConstructor         :  ( lhs=exprSingle | name=NCName ) (':' | '?') rhs=exprSingle;

arrayConstructor        :  '[' expr? ']';

uriLiteral              : stringLiteral;

stringLiteral           : STRING;

keyWords                : Kjsoniq
                        | Kjson
                        | Kversion
                        | Ktypeswitch
                        | Kor
                        | Kand
                        | Knot
                        | Kto
                        | Kinstance
                        | Kof
                        | Ktreat
                        |
                        | Kcast
                        | Kcastable
                        | Kdefault
                        | Kthen
                        | Kelse
                        | Kcollation
                        | Kgreatest
                        | Kleast
                        | Kswitch
                        | Kcase
                        | Ktry
                        | Kcatch
                        | Ksome
                        | Kevery
                        | Ksatisfies
                        | Kstable
                        | Kascending
                        | Kdescending
                        | Kempty
                        | Kallowing
                        | Kas
                        | Kat
                        | Kin
                        | Kif
                        | Kfor
                        | Klet
                        | Kwhere
                        | Kgroup
                        | Kby
                        | Korder
                        | Kcount
                        | Kreturn
                        ;

///////////////////////// literals

Kfor                    : 'for';

Klet                    : 'let';

Kwhere                  : 'where';

Kgroup                  : 'group';

Kby                     : 'by';

Korder                  : 'order';

Kreturn                 : 'return';

Kif                     : 'if';

Kin                     : 'in';

Kas                     : 'as';

Kat                     : 'at';

Kallowing               : 'allowing';

Kempty                  : 'empty';

Kcount                  : 'count';

Kstable                 : 'stable';

Kascending              : 'ascending';

Kdescending             : 'descending';

Ksome                   : 'some';

Kevery                  : 'every';

Ksatisfies              : 'satisfies';

Kcollation              : 'collation';

Kgreatest               : 'greatest';

Kleast                  : 'least';

Kswitch                 : 'switch';

Kcase                   : 'case';

Ktry                    : 'try';

Kcatch                  : 'catch';

Kdefault                : 'default';

Kthen                   : 'then';

Kelse                   : 'else';

Ktypeswitch             : 'typeswitch';

Kor                     : 'or';

Kand                    : 'and';

Knot                    : 'not' ;

Kto                     : 'to' ;

Kinstance               : 'instance' ;

Kof                     : 'of' ;

Ktreat                  : 'treat';

Kcast                   : 'cast';

Kcastable               : 'castable';

Kversion                : 'version';

Kjsoniq                 : 'jsoniq';

Kjson                   : 'json-item';

STRING                  : '"' (ESC | ~ ["\\])* '"';

fragment ESC            : '\\' (["\\/bfnrt] | UNICODE);

fragment UNICODE        : 'u' HEX HEX HEX HEX;

fragment HEX            : [0-9a-fA-F];

NullLiteral             : 'null';

Literal                 : NumericLiteral | BooleanLiteral;

NumericLiteral          : IntegerLiteral | DecimalLiteral | DoubleLiteral;

BooleanLiteral          : 'true' | 'false';

IntegerLiteral          : Digits ;

DecimalLiteral          : '.' Digits | Digits '.' [0-9]* ;

DoubleLiteral           : ('.' Digits | Digits ('.' [0-9]*)?) [eE] [+-]? Digits ;

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
                        | '-'
                        // no . in JSONIQ names | '.'
                        | [0-9]
                        | '\u00B7'
                        | '\u0300'..'\u036F'
                        | '\u203F'..'\u2040'
                        ;

XQComment               : '(' ':' (XQComment | '(' ~[:] | ':' ~[)] | ~[:(])* ':'+ ')' -> channel(HIDDEN);

ContentChar             :  ~["'{}<&]  ;
