(:JIQS: ShouldRun; Output="ok" :)
xquery version "3.1";
import module namespace m = "urn:schema-isolation" at "a-empty.xqm", "b-schema.xqm";
import schema namespace t = "urn:module-local-types" at "Types.xsd";
m:identity(t:Code("ok"))
