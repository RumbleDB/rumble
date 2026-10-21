module namespace m = "urn:schema-isolation";
import schema namespace t = "urn:module-local-types" at "Types.xsd";
declare function m:use($value as t:Code) { $value };
