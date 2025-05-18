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
