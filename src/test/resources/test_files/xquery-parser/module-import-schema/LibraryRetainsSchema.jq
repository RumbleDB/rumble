(:JIQS: ShouldRun; Output="ok" :)
xquery version "3.1";
(: A library can use its own schema in its body and signature without exporting the schema. :)
import module namespace m = "urn:schema-isolation" at "a-empty.xqm", "b-schema.xqm";
m:identity(m:make())
