(:JIQS: ShouldCrash; ErrorCode="FORG0001" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
declare function local:code($value as xs:string) as ex:Code {
  ex:Code($value)
};
local:code("invalid")
