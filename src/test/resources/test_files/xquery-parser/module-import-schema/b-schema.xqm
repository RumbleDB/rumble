module namespace m = "urn:schema-isolation";
import schema namespace t = "urn:module-local-types" at "Types.xsd";
declare function m:make() as t:Code { t:Code("ok") };
declare function m:identity($value as t:Code) as t:Code { $value };
